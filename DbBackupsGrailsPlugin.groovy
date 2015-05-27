class DbBackupsGrailsPlugin {
    // the plugin version
    def version = "0.3.0"
    // the version or versions of Grails the plugin is designed for
    def grailsVersion = "2.3 > *"
    // resources that are excluded from plugin packaging
    def pluginExcludes = [
        "grails-app/views/error.gsp",
		"grails-app/conf/DbBackupsBootStrap.groovy",
		"gails-app/domain/**"
    ]

    // TODO Fill in these fields
    def title = "DB Backups" // Headline display name of the plugin
    def author = "Scott Bennett"
    def authorEmail = "swb1701@gmail.com"
    def description = 'Automatic S3 and local backups for H2 databases.'

    // URL to the plugin's documentation
    def documentation = "https://github.com/SRARAD/db-backup-plugin"

    // Extra (optional) plugin metadata

    // License: one of 'APACHE', 'GPL2', 'GPL3'
    def license = "MIT"

    // Details of company behind the plugin (if there is one)
    def organization = [ name: "SRA", url: "http://www.sra.com/" ]

    // Any additional developers beyond the author specified above.
    def developers = [ [ name: "TheConnMan", email: "brian@theconnman.com" ]]

    // Location of the plugin's issue tracker.
    def issueManagement = [ system: "GitHub", url: "https://github.com/SRARAD/db-backup-plugin/issues" ]

    // Online location of the plugin's browseable source code.
    def scm = [ url: "https://github.com/SRARAD/db-backup-plugin" ]

    def doWithWebDescriptor = { xml ->
        // TODO Implement additions to web.xml (optional), this event occurs before
    }

    def doWithSpring = {
        // TODO Implement runtime spring config (optional)
    }

    def doWithDynamicMethods = { ctx ->
        // TODO Implement registering dynamic methods to classes (optional)
    }

    def doWithApplicationContext = { ctx ->
        // TODO Implement post initialization spring config (optional)
    }

    def onChange = { event ->
        // TODO Implement code that is executed when any artefact that this plugin is
        // watching is modified and reloaded. The event contains: event.source,
        // event.application, event.manager, event.ctx, and event.plugin.
    }

    def onConfigChange = { event ->
        // TODO Implement code that is executed when the project configuration changes.
        // The event is the same as for 'onChange'.
    }

    def onShutdown = { event ->
        // TODO Implement code that is executed when the application shuts down (optional)
    }
}
