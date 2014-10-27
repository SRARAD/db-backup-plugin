import com.sra.dbbackups.TestDomain;

class DbBackupsBootStrap {
	
	def DBBackupService
	
	def init = { servletContext ->
		println 'Generating test data'
		(1..100).each {
			new TestDomain(string: 'String ' + it, integer: it).save(flush: true);
		}
		println 'Creating backup'
		DBBackupService.s3Backup();
		
		println 'Creating backup'
		DBBackupService.s3RestoreLatest();
	}
}