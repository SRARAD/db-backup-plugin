package com.sra.dbbackups

import grails.util.Environment

import javax.crypto.SecretKey
import javax.crypto.spec.SecretKeySpec

import org.apache.commons.codec.binary.Base64
import org.h2.tools.Script
import org.hibernate.cfg.Configuration
import org.hibernate.event.Initializable
import org.hibernate.event.PostDeleteEvent
import org.hibernate.event.PostDeleteEventListener
import org.hibernate.event.PostInsertEvent
import org.hibernate.event.PostInsertEventListener
import org.hibernate.event.PostUpdateEvent
import org.hibernate.event.PostUpdateEventListener

import com.amazonaws.auth.AWSCredentialsProvider
import com.amazonaws.auth.DefaultAWSCredentialsProviderChain
import com.amazonaws.services.s3.AmazonS3Client
import com.amazonaws.services.s3.AmazonS3EncryptionClient
import com.amazonaws.services.s3.model.EncryptionMaterials
import com.amazonaws.services.s3.model.ObjectMetadata
import com.amazonaws.services.s3.model.S3Object

class MyListener implements PostInsertEventListener, PostUpdateEventListener, PostDeleteEventListener, Initializable {

	public void onPostInsert(final PostInsertEvent event) {
		DBBackupService.dirty=true
		return
	}

	public void onPostUpdate(final PostUpdateEvent event) {
		DBBackupService.dirty=true
		return
	}

	public void onPostDelete(final PostDeleteEvent event) {
		DBBackupService.dirty=true
		return
	}

	public void initialize(final Configuration config) {
		return
	}
}

class DBBackupService {

	def grailsApplication
	public static boolean dirty=false

	private addEventTypeListener(listeners, listener, type) {
		def typeProperty = "${type}EventListeners"
		def typeListeners = listeners."${typeProperty}"

		def expandedTypeListeners = new Object[typeListeners.length + 1]
		System.arraycopy(typeListeners, 0, expandedTypeListeners, 0, typeListeners.length)
		expandedTypeListeners[-1] = listener

		listeners."${typeProperty}" = expandedTypeListeners
	}

	def registerListener() {
		def listeners = grailsApplication.mainContext.sessionFactory.eventListeners
		def listener = new MyListener()

		['postInsert', 'postUpdate', 'postDelete'].each({
			addEventTypeListener(listeners, listener, it)
		})
		DBBackupJob.schedule(grailsApplication.mergedConfig.grails.plugin.dbbackups.interval)
	}

	def s3Backup() {
		String stem = grailsApplication.mergedConfig.grails.plugin.dbbackups.stem;
		boolean verbose = grailsApplication.mergedConfig.grails.plugin.dbbackups.verbose;
		boolean encrypt = grailsApplication.mergedConfig.grails.plugin.dbbackups.encrypt;
		AmazonS3Client client=null;
		if (encrypt) {
			def key=grailsApplication.mergedConfig.grails.plugin.dbbackups.key
			if (key!=null) {
				SecretKey skey = new SecretKeySpec(Base64.decodeBase64(key.getBytes()), "AES")
				EncryptionMaterials materials = new EncryptionMaterials(skey)
				AWSCredentialsProvider credprov=new DefaultAWSCredentialsProviderChain()
				client = new AmazonS3EncryptionClient(credprov.getCredentials(),materials)
			} else {
				println("dbbackups.key must be defined to perform encrypted backups (use grails create-key command to generate one)")
				println("backup not performed")
				return
			}
		} else {
			client=new AmazonS3Client() //assume an instance role with ability to create and write S3 buckets
		}
		String bucketName = getBucketName(stem);
		File temp = createLocalBackup(stem);
		if (!client.doesBucketExist(bucketName)) {
			client.createBucket(bucketName);
		}
		def formatDate=(new Date()).format("yyMMdd-HHmmss-SSS")
		def name=stem+"DB"+formatDate+".sql.txt"
		if (verbose) {
			println("DB Backed Up to S3 Bucket:"+bucketName+" File:"+name)
		}
		client.putObject(bucketName,name,temp)
		client.putObject(bucketName,stem+"DBLast.sql.txt",temp)
		temp.delete();
	}
	
	def s3RestoreLatest() {
		String stem = grailsApplication.mergedConfig.grails.plugin.dbbackups.stem;
		boolean verbose = grailsApplication.mergedConfig.grails.plugin.dbbackups.verbose;
		boolean encrypt = grailsApplication.mergedConfig.grails.plugin.dbbackups.encrypt;
		AmazonS3Client client=null;
		if (encrypt) {
			def key=grailsApplication.mergedConfig.grails.plugin.dbbackups.key
			if (key!=null) {
				SecretKey skey = new SecretKeySpec(Base64.decodeBase64(key.getBytes()), "AES")
				EncryptionMaterials materials = new EncryptionMaterials(skey)
				AWSCredentialsProvider credprov=new DefaultAWSCredentialsProviderChain()
				client = new AmazonS3EncryptionClient(credprov.getCredentials(),materials)
			} else {
				println("dbbackups.key must be defined to perform encrypted backups (use grails create-key command to generate one)")
				println("restore not performed")
				return
			}
		} else {
			client=new AmazonS3Client() //assume an instance role with ability to create and write S3 buckets
		}
		String bucketName = getBucketName(stem);
		int bufsize=1000000 //1M
		def size=bufsize
		try {
		  ObjectMetadata meta=client.getObjectMetadata(bucketName,stem+"DBLast.sql.txt")
		  size=meta.getContentLength()
		} catch (Exception e) {
		  e.printStackTrace()
		}
		S3Object obj=client.getObject(bucketName,stem+"DBLast.sql.txt")
		InputStream in0=obj.getObjectContent()
		if (size<bufsize) bufsize=size //or length of object if smaller
		byte[] buf=new byte[bufsize]
		int len=-1
		String filename=grailsApplication.parentContext.getResource("restoreDb.sql").file.toString()
		File outfile=new File(filename)
		if (outfile.exists()) {
			outfile.delete()
		}
		FileOutputStream outs=new FileOutputStream(outfile)
		while((len=in0.read(buf,0,bufsize))>-1) {
			if (len>0) {
				outs.write(buf,0,len)
			}
		}
		outs.close()
		println("Last backup script has been restore to:"+filename)
	}

	/**
	 * Creates a temporary local backup.
	 * @author - Brian Conn (TheConnMan)
	 * @param stem - Stem file name
	 * @return Backup file
	 */
	File createLocalBackup(String stem) {
		String dburl=grailsApplication.config.dataSource.url;
		String dbuser=grailsApplication.config.dataSource.username;
		String dbpass=grailsApplication.config.dataSource.password;
		Script dbScript=new Script()
		ByteArrayOutputStream stream=new ByteArrayOutputStream();
		File temp=File.createTempFile(stem+"db",".sql")
		String filename=temp.absolutePath
		dbScript.execute(dburl,dbuser,dbpass,filename)
		return temp
	}

	/**
	 * Gets the bucket name based on the current environment.
	 * @param stem - Backup name stem
	 * @return Bucket name
	 */
	String getBucketName(String stem) {
		String name;
		Environment env=Environment.getCurrent();
		switch(env) {
			case Environment.DEVELOPMENT:
				name = stem + "-db-backups-dev";
				break;
			case Environment.PRODUCTION:
				name = stem + "-db-backups-prod";
				break;
			case Environment.TEST:
				name = stem + "-db-backups-test";
				break;
			default:
				name = stem + "-db-backups-misc";
		}
		name
	}
}
