package com.example.springbootmybatis.util;

import org.apache.commons.codec.binary.Base64;
import sun.misc.BASE64Decoder;

import javax.crypto.Cipher;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

//Rsa工具
public class RsaUtil {
    private static Provider bouncyCastleProvider;

    static {
        try {
            bouncyCastleProvider = (Provider) Class.forName("org.bouncycastle.jce.provider.BouncyCastleProvider")
                    .newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static PublicKey getPubKey(String pubKey) {
        PublicKey publicKey = null;
        try {
            byte[] keyBytes = new BASE64Decoder().decodeBuffer(pubKey);
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            publicKey = keyFactory.generatePublic(keySpec);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return publicKey;
    }

    private static PrivateKey convertPrivateKeytoKeyObject(String privateKey)
            throws NoSuchAlgorithmException, InvalidKeySpecException {
        byte[] key = Base64.decodeBase64(privateKey);
        PKCS8EncodedKeySpec privateKeySpec = new PKCS8EncodedKeySpec(key);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        return keyFactory.generatePrivate(privateKeySpec);
    }

    public static byte[] encrypt(String pubKey, byte[] data) throws Exception {
        PublicKey publicKey = getPubKey(pubKey);
        Cipher ci = Cipher.getInstance("RSA");
        ci.init(1, publicKey);
        return ci.doFinal(data);
    }

    public static byte[] encrypt(String pubKey, String data) throws Exception {
        PublicKey publicKey = getPubKey(pubKey);
        Cipher ci = Cipher.getInstance("RSA");
        ci.init(1, publicKey);
        return ci.doFinal(data.getBytes("UTF-8"));
    }

    public static byte[] encryptPadding(String pubKey, String data) throws Exception {
        PublicKey publicKey = getPubKey(pubKey);
        Cipher ci = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        ci.init(1, publicKey);
        return ci.doFinal(data.getBytes("UTF-8"));
    }

    public static byte[] decrypt(String privateKeyStr, String data) throws Exception {
        return decrypt(convertPrivateKeytoKeyObject(privateKeyStr), data);
    }

    public static byte[] decrypt(PrivateKey privateKey, byte[] data) throws Exception {
        Cipher ci = Cipher.getInstance("RSA/ECB/PKCS1Padding", bouncyCastleProvider);
        ci.init(Cipher.DECRYPT_MODE, privateKey);
        return ci.doFinal(data);
    }

    public static byte[] decrypt(PrivateKey privateKey, String data) throws Exception {
        byte[] pwdata = Base64.decodeBase64(data);
        return decrypt(privateKey, pwdata);
    }

    public static String genRandomNum() {
        int maxNum = 36;
        int i;
        int count = 0;
        char[] str = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K',
                'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W',
                'X', 'Y', 'Z', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};
        char[] str1 = {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k',
                'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w',
                'x', 'y', 'z'};
        char[] str2 = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};
        StringBuffer pwd = new StringBuffer();
        SecureRandom r = new SecureRandom();
        while (count < 8) {
            i = Math.abs(r.nextInt(maxNum));
            if (i >= 0 && i < str.length) {
                if (count == 0) {
                    pwd.append(str[i]);
                }
                pwd.append(str[i]);
                count++;
            }
        }
        pwd.append(str1[Math.abs(r.nextInt(25))]).append(str2[Math.abs(r.nextInt(9))]);
        return pwd.toString();
    }

}
