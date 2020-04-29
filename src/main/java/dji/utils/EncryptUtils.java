package dji.utils;

import android.text.TextUtils;
import java.io.File;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import org.bouncycastle.pqc.jcajce.spec.McElieceCCA2KeyGenParameterSpec;

public class EncryptUtils {
    private static final char[] HEX_DIGITS = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};

    public static String encryptMD5ToString(String data) {
        if (data == null || data.length() == 0) {
            return "";
        }
        return encryptMD5ToString(data.getBytes());
    }

    public static String encryptMD5ToString(String data, String salt) {
        if (data == null && salt == null) {
            return "";
        }
        if (salt == null) {
            return bytes2HexString(encryptMD5(data.getBytes()));
        }
        if (data == null) {
            return bytes2HexString(encryptMD5(salt.getBytes()));
        }
        return bytes2HexString(encryptMD5((data + salt).getBytes()));
    }

    public static String encryptMD5ToString(byte[] data) {
        return bytes2HexString(encryptMD5(data));
    }

    public static String encryptMD5ToString(byte[] data, byte[] salt) {
        if (data == null && salt == null) {
            return "";
        }
        if (salt == null) {
            return bytes2HexString(encryptMD5(data));
        }
        if (data == null) {
            return bytes2HexString(encryptMD5(salt));
        }
        byte[] dataSalt = new byte[(data.length + salt.length)];
        System.arraycopy(data, 0, dataSalt, 0, data.length);
        System.arraycopy(salt, 0, dataSalt, data.length, salt.length);
        return bytes2HexString(encryptMD5(dataSalt));
    }

    public static byte[] encryptMD5(byte[] data) {
        return hashTemplate(data, "MD5");
    }

    public static String encryptMD5File2String(String filePath) {
        return encryptMD5File2String(hasSpace(filePath) ? null : new File(filePath));
    }

    public static byte[] encryptMD5File(String filePath) {
        return encryptMD5File(hasSpace(filePath) ? null : new File(filePath));
    }

    public static String encryptMD5File2String(File file) {
        return bytes2HexString(encryptMD5File(file));
    }

    /* JADX WARNING: Removed duplicated region for block: B:22:0x003a A[SYNTHETIC, Splitter:B:22:0x003a] */
    /* JADX WARNING: Removed duplicated region for block: B:28:0x0046 A[SYNTHETIC, Splitter:B:28:0x0046] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static byte[] encryptMD5File(java.io.File r8) {
        /*
            r6 = 0
            if (r8 != 0) goto L_0x0004
        L_0x0003:
            return r6
        L_0x0004:
            r3 = 0
            java.io.FileInputStream r4 = new java.io.FileInputStream     // Catch:{ NoSuchAlgorithmException -> 0x0033, IOException -> 0x004f }
            r4.<init>(r8)     // Catch:{ NoSuchAlgorithmException -> 0x0033, IOException -> 0x004f }
            java.lang.String r7 = "MD5"
            java.security.MessageDigest r5 = java.security.MessageDigest.getInstance(r7)     // Catch:{ NoSuchAlgorithmException -> 0x0055, IOException -> 0x0058, all -> 0x0052 }
            java.security.DigestInputStream r1 = new java.security.DigestInputStream     // Catch:{ NoSuchAlgorithmException -> 0x0055, IOException -> 0x0058, all -> 0x0052 }
            r1.<init>(r4, r5)     // Catch:{ NoSuchAlgorithmException -> 0x0055, IOException -> 0x0058, all -> 0x0052 }
            r7 = 262144(0x40000, float:3.67342E-40)
            byte[] r0 = new byte[r7]     // Catch:{ NoSuchAlgorithmException -> 0x0055, IOException -> 0x0058, all -> 0x0052 }
        L_0x001a:
            int r7 = r1.read(r0)     // Catch:{ NoSuchAlgorithmException -> 0x0055, IOException -> 0x0058, all -> 0x0052 }
            if (r7 > 0) goto L_0x001a
            java.security.MessageDigest r5 = r1.getMessageDigest()     // Catch:{ NoSuchAlgorithmException -> 0x0055, IOException -> 0x0058, all -> 0x0052 }
            byte[] r6 = r5.digest()     // Catch:{ NoSuchAlgorithmException -> 0x0055, IOException -> 0x0058, all -> 0x0052 }
            if (r4 == 0) goto L_0x0003
            r4.close()     // Catch:{ IOException -> 0x002e }
            goto L_0x0003
        L_0x002e:
            r2 = move-exception
            r2.printStackTrace()
            goto L_0x0003
        L_0x0033:
            r7 = move-exception
        L_0x0034:
            r2 = r7
        L_0x0035:
            r2.printStackTrace()     // Catch:{ all -> 0x0043 }
            if (r3 == 0) goto L_0x0003
            r3.close()     // Catch:{ IOException -> 0x003e }
            goto L_0x0003
        L_0x003e:
            r2 = move-exception
            r2.printStackTrace()
            goto L_0x0003
        L_0x0043:
            r6 = move-exception
        L_0x0044:
            if (r3 == 0) goto L_0x0049
            r3.close()     // Catch:{ IOException -> 0x004a }
        L_0x0049:
            throw r6
        L_0x004a:
            r2 = move-exception
            r2.printStackTrace()
            goto L_0x0049
        L_0x004f:
            r7 = move-exception
        L_0x0050:
            r2 = r7
            goto L_0x0035
        L_0x0052:
            r6 = move-exception
            r3 = r4
            goto L_0x0044
        L_0x0055:
            r7 = move-exception
            r3 = r4
            goto L_0x0034
        L_0x0058:
            r7 = move-exception
            r3 = r4
            goto L_0x0050
        */
        throw new UnsupportedOperationException("Method not decompiled: dji.utils.EncryptUtils.encryptMD5File(java.io.File):byte[]");
    }

    public static String encryptSHA1ToString(String data) {
        if (data == null || data.length() == 0) {
            return "";
        }
        return encryptSHA1ToString(data.getBytes());
    }

    public static String encryptSHA1ToString(byte[] data) {
        return bytes2HexString(encryptSHA1(data));
    }

    public static byte[] encryptSHA1(byte[] data) {
        return hashTemplate(data, McElieceCCA2KeyGenParameterSpec.SHA1);
    }

    public static String encryptSHA256ToString(String data) {
        if (data == null || data.length() == 0) {
            return "";
        }
        return encryptSHA256ToString(data.getBytes());
    }

    public static String encryptSHA256ToString(byte[] data) {
        return bytes2HexString(encryptSHA256(data));
    }

    public static byte[] encryptSHA256(byte[] data) {
        return hashTemplate(data, McElieceCCA2KeyGenParameterSpec.SHA256);
    }

    public static byte[] encryptHmacSHA256(String encryptText, String encryptKey) {
        if (TextUtils.isEmpty(encryptText) || TextUtils.isEmpty(encryptKey)) {
            return null;
        }
        try {
            SecretKey secretKey = new SecretKeySpec(encryptKey.getBytes("UTF-8"), "HmacSHA256");
            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(secretKey);
            return mac.doFinal(encryptText.getBytes("UTF-8"));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private static byte[] hashTemplate(byte[] data, String algorithm) {
        if (data == null || data.length <= 0) {
            return null;
        }
        try {
            MessageDigest md = MessageDigest.getInstance(algorithm);
            md.update(data);
            return md.digest();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static String bytes2HexString(byte[] bytes) {
        int len;
        if (bytes == null || (len = bytes.length) <= 0) {
            return "";
        }
        char[] ret = new char[(len << 1)];
        int j = 0;
        for (int i = 0; i < len; i++) {
            int j2 = j + 1;
            ret[j] = HEX_DIGITS[(bytes[i] >> 4) & 15];
            j = j2 + 1;
            ret[j2] = HEX_DIGITS[bytes[i] & 15];
        }
        return new String(ret);
    }

    private static boolean hasSpace(String s) {
        if (s == null) {
            return true;
        }
        int len = s.length();
        for (int i = 0; i < len; i++) {
            if (!Character.isWhitespace(s.charAt(i))) {
                return false;
            }
        }
        return true;
    }
}
