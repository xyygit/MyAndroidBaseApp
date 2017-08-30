package lib.core.security;

import android.util.Base64;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import lib.core.common.ExAppID;
import lib.core.utils.ExCommonUtil;

public class AES {

    private static final String ALGORITHM = "AES";
    private static final String TRANSFORMATION = "AES/CBC/PKCS5Padding";
    private static final String ENCODING = "UTF-8";

    private static byte[] getKey() {
        byte[] key = null;
        try {
            KeyGenerator keyGenerator = KeyGenerator.getInstance(ALGORITHM);
            keyGenerator.init(256);
            SecretKey secretKey = keyGenerator.generateKey();
            key = secretKey.getEncoded();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        try {
            key = "!!10309833372465".getBytes(ENCODING);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return key;
    }

    private static String getIV() {
        String appId = ExAppID.getAppID();
        if (ExCommonUtil.isEmpty(appId) && appId.length() >= 16) {
            return appId.substring(0, 15);
        } else {
            return "10309833372465!!";
        }
    }

    public static String encrypt(String msg) {
        if(ExCommonUtil.isEmpty(msg)) return msg;

        try {
            byte[] sKey = getKey();
            SecretKeySpec skeySpec = new SecretKeySpec(sKey, ALGORITHM);
            Cipher cipher = Cipher.getInstance(TRANSFORMATION);
            IvParameterSpec iv = new IvParameterSpec(getIV().getBytes());
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);
            byte[] encrypted = cipher.doFinal(msg.getBytes());
            return Base64.encodeToString(encrypted, Base64.DEFAULT);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return msg;
    }

    public static String decrypt(String msg) {
        if(ExCommonUtil.isEmpty(msg)) return msg;
        try {
            byte[] sKey = getKey();
            SecretKeySpec skeySpec = new SecretKeySpec(sKey, ALGORITHM);
            Cipher cipher = Cipher.getInstance(TRANSFORMATION);
            IvParameterSpec iv = new IvParameterSpec(getIV().getBytes());
            cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);
            byte[] encrypted1 = Base64.decode(msg, Base64.DEFAULT);
            byte[] original = cipher.doFinal(encrypted1);
            return new String(original);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return msg;
    }

}
