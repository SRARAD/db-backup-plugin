DB Backups Plugin
=========

Automatic S3 backups for H2 databases.

## Setup

- Inject the `DBBackupService` and add `DBBackupService.registerListener()` to the end of **BootStrap.groovy**
- Change the default **stem** bucket name
- Customize any default configs by following the instructions below
- If you are using encryption and need an encryption key run the **CreateKey** script below

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

### encrypt

Boolean for if backup is encrypted on S3.

**Default** - false

### key

Encryption key, required for encryption.

## Scripts

### CreateKey

The create key script generates an encryption key for storing encrypted files on S3. **We recommend not storing this key in source control. Store it in a local config file which is not checked in.**.

To generate a key run `grails CreateKey` after installing the plugin.