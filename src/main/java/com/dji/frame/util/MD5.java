package com.dji.frame.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5 {
    public static String getMD5(String content) {
        try {
            MessageDigest digest = MessageDigest.getInstance("MD5");
            digest.update(content.getBytes());
            return getHashString(digest);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String getMD5For16(String content) {
        return getMD5(content).substring(8, 24);
    }

    public static String getMD5(byte[] bytes) {
        try {
            MessageDigest digest = MessageDigest.getInstance("MD5");
            digest.update(bytes);
            return getHashString(digest);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static String getHashString(MessageDigest digest) {
        StringBuilder builder = new StringBuilder();
        byte[] digest2 = digest.digest();
        for (byte b : digest2) {
            builder.append(Integer.toHexString((b >> 4) & 15));
            builder.append(Integer.toHexString(b & 15));
        }
        return builder.toString();
    }

    public static byte[] getMD5(File file) {
        byte[] desc = new byte[16];
        try {
            InputStream ins = new FileInputStream(file);
            byte[] buffer = new byte[8192];
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            while (true) {
                int len = ins.read(buffer);
                if (len == -1) {
                    break;
                }
                md5.update(buffer, 0, len);
            }
            ins.close();
            System.arraycopy(md5.digest(), 0, desc, 0, desc.length);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e2) {
            e2.printStackTrace();
        } catch (IOException e3) {
            e3.printStackTrace();
        }
        return desc;
    }
}
