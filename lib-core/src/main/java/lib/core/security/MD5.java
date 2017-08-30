package lib.core.security;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5 {

    /*
     * MD5加密
     * @method md5
     *
     */
    public static final String encrypt(String input) {
        byte[] hash = null;

        try {
            hash = MessageDigest.getInstance("MD5").digest(input.getBytes("UTF-8"));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        if (hash == null) {

            return "";
        }

        StringBuffer hex = new StringBuffer(hash.length * 2);

        for (byte b : hash) {
            if ((b & 0xFF) < 0x10) {
                hex.append("0");
            }

            hex.append(Integer.toHexString(b & 0xFF));
        }

        return hex.toString();
    }

}
