DB Backups Plugin
=========

Automatic S3 backups for H2 databases.

## Setup

- Inject the `DBBackupService` and add `DBBackupService.registerListener()` to the end of **BootStrap.groovy**
- Change the default **stem** bucket name
- Customize any default configs by following the instructions below

## Use

- The project must be run using `-Daws.accessKeyId` and `-Daws.secretKey` denoting AWS credentials with S3 permissions

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