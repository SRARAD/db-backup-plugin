grails {
	plugin {
		dbbackups {
			interval = 60000
			verbose = false
			stem = 'sample'
			encrypt = false
			localDirectory = 'localBackups'
			localBackups = false
			s3Backups = true
			localFileLimit = 200
		}
	}
}