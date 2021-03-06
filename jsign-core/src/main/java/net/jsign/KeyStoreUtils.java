/**
 * Copyright 2017 Emmanuel Bourg
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package net.jsign;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.KeyStore;
import java.security.KeyStoreException;

/**
 * Helper class for loading KeyStores.
 * 
 * @author Emmanuel Bourg
 * @since 1.4
 */
public class KeyStoreUtils {

    public static KeyStore load(File keystore, String storetype, String storepass) throws SignerException {
        if (keystore != null && storetype == null) {
            // guess the type of the keystore from the extension of the file
            String filename = keystore.getName().toLowerCase();
            if (filename.endsWith(".p12") || filename.endsWith(".pfx")) {
                storetype = "PKCS12";
            } else {
                storetype = "JKS";
            }
        }
        
        // JKS or PKCS12 keystore
        KeyStore ks;
        try {
            ks = KeyStore.getInstance(storetype);
        } catch (KeyStoreException e) {
            throw new SignerException("keystore type '" + storetype + "' is not supported", e);
        }

        if (keystore == null || !keystore.exists()) {
            throw new SignerException("The keystore " + keystore + " couldn't be found");
        }
        FileInputStream in = null;
        try {
            in = new FileInputStream(keystore);
            ks.load(in, storepass != null ? storepass.toCharArray() : null);
        } catch (Exception e) {
            throw new SignerException("Unable to load the keystore " + keystore, e);
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (IOException e) {
                // ignore
            }
        }
        
        return ks;
    }
}
