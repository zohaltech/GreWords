package com.zohaltech.app.grewords.classes;


import android.util.Base64;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class Cryptography {
    private static SecretKeySpec secretKey;
    
    private static void setKey() {
        MessageDigest sha;
        try {
            String myKey = "85f2901cd0d6e4131cb777f2d8dd92b2";
            byte[] key = myKey.getBytes("UTF-8");
            sha = MessageDigest.getInstance("SHA-1");
            key = sha.digest(key);
            key = Arrays.copyOf(key, 32); // use only first 128 bit
            secretKey = new SecretKeySpec(key, "AES");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }
    
    //private static String encrypt(String strToEncrypt) {
    //    String result = "";
    //    try {
    //        setKey();
    //        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
    //        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
    //        result = Base64.encodeToString(cipher.doFinal(strToEncrypt.getBytes("UTF-8")), Base64.DEFAULT);
    //    } catch (Exception e) {
    //        e.printStackTrace();
    //    }
    //    return result;
    //}
    
    public static String decrypt(String strToDecrypt) {
        String result = "";
        try {
            setKey();
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5PADDING");
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
            result = new String(cipher.doFinal(Base64.decode(strToDecrypt, Base64.DEFAULT)));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
    
    //public static void test(String strToEncrypt) {
    //    String strEncrypt = encrypt(strToEncrypt.trim());
    //    Log.i("LOG", "String to Encrypt: " + strToEncrypt);
    //    Log.i("LOG", "Encrypted: " + strEncrypt);
    //    String strDecrypt = decrypt(strEncrypt.trim());
    //    Log.i("LOG", "String To Decrypt : " + strEncrypt);
    //    Log.i("LOG", "Decrypted : " + strDecrypt);
    //}
}
