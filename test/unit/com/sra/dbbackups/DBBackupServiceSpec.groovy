package com.sra.dbbackups

import grails.test.mixin.TestFor
import spock.lang.Specification

/**
 * See the API for {@link grails.test.mixin.services.ServiceUnitTestMixin} for usage instructions
 */
@TestFor(DBBackupService)
class DBBackupServiceSpec extends Specification {

    def setup() {
		
    }

    def cleanup() {
		
    }

    void testGetBucketName(String stem, String bucketName) {
		given:
		
		when:
			String name = service.getBucketName(stem);
		
		then:
			name == bucketName
			
		where:
			stem | bucketName
			'Test' | 'Test-db-backups-test'
			'Prod' | 'Prod-db-backups-test'
    }
	
	void testS3Backup() {
		
	}
}
