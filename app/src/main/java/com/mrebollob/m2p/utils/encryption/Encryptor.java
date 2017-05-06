/*
 * Copyright (c) 2017. Manuel Rebollo Báez
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.mrebollob.m2p.utils.encryption;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEParameterSpec;
import javax.inject.Inject;

/*
 * https://github.com/novoda/android-demos/tree/master/encryption
 */
public class Encryptor {

    private static final String CIPHER_TYPE = "PBEWithMD5AndDES/CBC/PKCS5Padding";
    private static final String ALGORITHM = "PBEWithMD5AndDES";
    private static final String CHARSET = "UTF-8";
    private static final String SECRET_KEY = "23r8yfhwyhaf78weh";

    private static final byte[] SALT = {
            (byte) 0x03, (byte) 0x10, (byte) 0x63, (byte) 0x64,
            (byte) 0x73, (byte) 0x15, (byte) 0x20, (byte) 0x51
    };

    private Encryptor() {
    }

    @NonNull
    public static String getAsHash(@Nullable String var) {
        String value;
        try {
            value = crypt(Cipher.ENCRYPT_MODE, var);
        } catch (Exception e) {
            value = "";
        }
        return value;
    }

    @NonNull
    public static String getUnhashed(@Nullable String hashed) {
        String value;
        try {
            value = crypt(Cipher.DECRYPT_MODE, hashed);
        } catch (Exception e) {
            value = "";
        }
        return value;
    }


    @NonNull
    private static String crypt(@NonNull int mode, @NonNull String encryptionSubject) throws Base64DecoderException,
            NoSuchAlgorithmException, InvalidAlgorithmParameterException, InvalidKeyException,
            InvalidKeySpecException, NoSuchPaddingException, BadPaddingException,
            UnsupportedEncodingException, IllegalBlockSizeException {

        final PBEParameterSpec ps = new javax.crypto.spec.PBEParameterSpec(SALT, 20);
        final SecretKeyFactory kf = SecretKeyFactory.getInstance(ALGORITHM);
        final SecretKey k = kf.generateSecret(new javax.crypto.spec.PBEKeySpec(SECRET_KEY.toCharArray()));
        final Cipher crypter = Cipher.getInstance(CIPHER_TYPE);

        String result;

        switch (mode) {
            case Cipher.DECRYPT_MODE:
                crypter.init(Cipher.DECRYPT_MODE, k, ps);
                result = new String(crypter.doFinal(Base64.decode(encryptionSubject)), CHARSET);
                break;
            case Cipher.ENCRYPT_MODE:
            default:
                crypter.init(Cipher.ENCRYPT_MODE, k, ps);
                result = Base64.encode(crypter.doFinal(encryptionSubject.getBytes(CHARSET)));
        }

        return result;
    }
}
