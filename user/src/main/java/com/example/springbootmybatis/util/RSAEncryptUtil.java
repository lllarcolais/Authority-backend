package com.example.springbootmybatis.util;

import org.apache.commons.codec.binary.Base64;

import javax.crypto.Cipher;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;


public class RSAEncryptUtil {

    private static Map<Integer, String> keyMap = new HashMap<Integer, String>();  //用于封装随机产生的公钥与私钥

    public static void main(String[] args) throws Exception {
        //生成公钥和私钥
        if (false) {
            genKeyPair();
        }else{
            String publicKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCylcSfjuWQIvdYW7IWs8d+Hd6rtjxH6DMF8BClB3YZU3v0YACU1gVNiw8YjmMijYWGfsiHHJlWKvzJMLdbCvun+O8nctihZY27OfGuWP24OpYyp1Wrdk6BQTQmfds2yVommFJ5nBY6FpbhVHbUW5LCrp+yVydGyvc1hrbBORILuQIDAQAB";
            String privateKey = "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBALKVxJ+O5ZAi91hbshazx34d3qu2PEfoMwXwEKUHdhlTe/RgAJTWBU2LDxiOYyKNhYZ+yIccmVYq/Mkwt1sK+6f47ydy2KFljbs58a5Y/bg6ljKnVat2ToFBNCZ92zbJWiaYUnmcFjoWluFUdtRbksKun7JXJ0bK9zWGtsE5Egu5AgMBAAECgYEAraDbaF5gZ4D3htqwDU6BeObFpaEqfqcNZIqBRFI6ymjWrUPhjNOAmGHJLZDSZbe+yILfqC1wD8z/tQQ+YLAO9x/slnrmo4h6jvj9/KIsxAAa7i53vrk81pBRU6DkAI+G8Bh++fkgrsJU3oIrpUKYdDLPxjN/MIh292/vLOUHWlkCQQDy/x0y0g1UUw1sSKoZ1n/vW4HxQ5ymNDSVRnzBoB3aGJuKMIgEIefggoxtR/2qslwtgTyrfzpTN6Mga6+IV6WjAkEAvCRCsV5fGfirll9uCkpqDbsFyNNSFuNtL61lZK21ZuOgetjqg5Lutnh7SLVD5i9BtpQiVIn89fhyZiNa0+oG8wJAY2Xn07oIqc2vV/QBYMBVZNvRs5cspPAF8mn67llI3MXkGgrwGICmEThVqP4hzYlYG2UaJCzO9utzVve1vdfccQJAU82zB16J7f+eLrUDRwIuiz8rLHE0t41GLV6HM3lMvC/YY+ALVeA1BIJWJ+TWAco68yC4yf9M9iNckbJd2tb7kQJBAOB6HPJVJmENYyZBHdNMCC1zKciRE5Rkf6eUkI0eG46aGWSc7yoc7aXIys6LqkiZlzamF670GFCMks/y1WobJFY=";
            keyMap.put(0,publicKey);  //0表示公钥
            keyMap.put(1,privateKey);  //1表示私钥
        }
        //加密字符串
        String message = "一号的pwd";
        System.out.println("随机生成的公钥为:" + keyMap.get(0));
        System.out.println("随机生成的私钥为:" + keyMap.get(1));
        String messageEn = encrypt(message,keyMap.get(0));
        System.out.println(message + "\t加密后的字符串为:" + messageEn);
        String messageDe = decrypt(messageEn,keyMap.get(1));
        System.out.println("还原后的字符串为:" + messageDe);

        String messageEnVue = "ES27VPc4uVK2ABWgvWTtS2HB0lKmqgPt6lxB31CxW6CnHlqOsODz6I+WlIZsFEsmpqs1nvvZYFee4Gop6jOLDUbbQuZzkml1PyhIQmcp1BQelJcdy/6cW/+qoD9XZjErz0A8xHGFWPwGU8M7QuKiHQcbDyELNOKOxgsu2Z9kweo=";
        System.out.println(message + "\t前端加密后的字符串为:" + messageEnVue);
        String messageDeVue = decrypt(messageEn,keyMap.get(1));
        System.out.println("还原后的字符串为:" + messageDeVue);
    }


    /**
     * 随机生成密钥对
     * @throws NoSuchAlgorithmException
     */
    public static void genKeyPair() throws NoSuchAlgorithmException {
        // KeyPairGenerator类用于生成公钥和私钥对，基于RSA算法生成对象
        KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance("RSA");
        // 初始化密钥对生成器，密钥大小为96-1024位
        keyPairGen.initialize(1024,new SecureRandom());
        // 生成一个密钥对，保存在keyPair中
        KeyPair keyPair = keyPairGen.generateKeyPair();
        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();   // 得到私钥
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();  // 得到公钥
        String publicKeyString = new String(Base64.encodeBase64(publicKey.getEncoded()));
        // 得到私钥字符串
        String privateKeyString = new String(Base64.encodeBase64((privateKey.getEncoded())));
        // 将公钥和私钥保存到Map
        keyMap.put(0,publicKeyString);  //0表示公钥
        keyMap.put(1,privateKeyString);  //1表示私钥
    }
    /**
     * RSA公钥加密
     *
     * @param str
     *            加密字符串
     * @param publicKey
     *            公钥
     * @return 密文
     * @throws Exception
     *             加密过程中的异常信息
     */
    public static String encrypt( String str, String publicKey ) throws Exception{
        //base64编码的公钥
        byte[] decoded = Base64.decodeBase64(publicKey);
        RSAPublicKey pubKey = (RSAPublicKey) KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(decoded));
        //RSA加密
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, pubKey);
        String outStr = Base64.encodeBase64String(cipher.doFinal(str.getBytes("UTF-8")));
        return outStr;
    }

    /**
     * RSA私钥解密
     *
     * @param str
     *            加密字符串
     * @param privateKey
     *            私钥
     * @return 铭文
     * @throws Exception
     *             解密过程中的异常信息
     */
    public static String decrypt(String str, String privateKey) throws Exception{
        //64位解码加密后的字符串
        byte[] inputByte = Base64.decodeBase64(str.getBytes("UTF-8"));
        //base64编码的私钥
        byte[] decoded = Base64.decodeBase64(privateKey);
        RSAPrivateKey priKey = (RSAPrivateKey) KeyFactory.getInstance("RSA").generatePrivate(new PKCS8EncodedKeySpec(decoded));
        //RSA解密
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, priKey);
        String outStr = new String(cipher.doFinal(inputByte));
        return outStr;
    }
}
