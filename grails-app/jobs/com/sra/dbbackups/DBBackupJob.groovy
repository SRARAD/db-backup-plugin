package com.sra.dbbackups

class DBBackupJob {
	
	def DBBackupService
	
	def checkBackup() {
		if (DBBackupService.dirty) {
			DBBackupService.backup()
			DBBackupService.dirty=false
		}
	}

	def execute() {
		checkBackup()
	}
}
