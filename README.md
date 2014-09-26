DB Backups Plugin
=========

Automatic S3 backups for H2 databases.

## Config

All config items can be overwritten in Config.groovy by prepending `grails.plugin.dbbackups.` onto the option name.

### interval

Interval time for backups in milliseconds.

**Default** - 60000

### verbose

Boolean for if backup info is printed.

**Default** - false

### stem

Stem bucket name. Bucket name will be **[stem]-db-backups-[environment]** (e.g. **sample-db-backups-dev**).

**Default** - 'sample'