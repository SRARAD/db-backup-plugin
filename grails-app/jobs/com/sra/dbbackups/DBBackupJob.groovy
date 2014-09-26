package com.sra.dbbackups

class DBBackupJob {
	
	def DBBackupService
	
	def checkBackup() {
		if (DBBackupService.dirty) {
			DBBackupService.s3Backup()
			DBBackupService.dirty=false
		}
	}

	def execute() {
		checkBackup()
	}
}
