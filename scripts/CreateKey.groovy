import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;

includeTargets << grailsScript("_GrailsInit")

target(createKey: "The description of the script goes here!") {
 		//Generate symmetric 256 AES key.
        KeyGenerator symKeyGenerator = KeyGenerator.getInstance("AES")
        symKeyGenerator.init(256);
        SecretKey symKey = symKeyGenerator.generateKey()
        println("Place the following entry in your Config.groovy (or we recommend in a local file included in Config so as to keep the key out of version control):")
        println('dbbackups.key="'+new String(Base64.encodeBase64(symKey.getEncoded()))+'"')	
}

setDefaultTarget(createKey)

