DB Backups Plugin
=========

Automatic S3 and local backups for H2 databases.  The plugin automatically backs up the database to S3 as a sql script which can restore the database.  This is done at the desired interval (and only when changes are detected).  It provides a simple way to create backups of small H2 databases to an external location.  It can also create the sql script backup to the local filesystem on the machine (and keeps a specified number of the most recent backups).  The latter is useful when certain filesystems are scanned by external backup servers.

## Setup

- Inject the `DBBackupService` and add `DBBackupService.registerListener()` to the end of **BootStrap.groovy**
- Change the default **stem** bucket name
- Customize any default configs by following the instructions below
- If you are using encryption and need an encryption key run the **CreateKey** script below

## Use

- The project must be run using `-Daws.accessKeyId` and `-Daws.secretKey` denoting AWS credentials with S3 permissions if **s3Backups** is set to **true**

## Config

All config items can be overwritten in Config.groovy by prepending `grails.plugin.dbbackups.` onto the option name.

### interval

Interval time for backups in milliseconds.

**Default** - 60000

### verbose

Boolean for if backup info is printed.

**Default** - false

### s3Backups

Boolean for if backups are made to S3.

**Default** - true

### stem

Stem bucket name. Bucket name will be **[stem]-db-backups-[environment]** (e.g. **sample-db-backups-dev**).

**Default** - 'sample'

### encrypt

Boolean for if backup is encrypted on S3.

**Default** - false

### key

Encryption key, required for encryption.

### localBackups

Boolean for if local backups are made.

**Default** - false

### localDirectory

Local backup directory location. Relative paths start in the **web-app** folder. Absolute paths can also be used.

**Default** - 'localBackups'

### localFileLimit

Maximum number of local files before the oldest backups start being deleted.

**Default** - 200

## Scripts

### CreateKey

The create key script generates an encryption key for storing encrypted files on S3. **We recommend not storing this key in source control. Store it in a local config file which is not checked in.**.

To generate a key run `grails CreateKey` after installing the plugin.

## License

**DB-Backups** has been released under the MIT license by [SRA International, Inc](https://www.sra.com/). It was originally developed specifically for the [SRA Rapid Application Development Team](https://github.com/SRARAD).
