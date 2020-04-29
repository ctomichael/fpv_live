package dji.common.util;

import android.util.Base64;
import dji.midware.util.BytesUtil;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class HmacEncryptUtil {
    private static final String ENCODING = "UTF-8";
    private static final String HMAC_MD5_NAME = "HmacMD5";
    private static final String HMAC_SHA1_NAME = "HmacSHA1";
    private static final String HMAC_SHA256_NAME = "HmacSHA256";

    public static String HmacSHA1Encrypt(String encryptText, String encryptKey) {
        return HmacSHAEncrypt(encryptText, encryptKey, HMAC_SHA1_NAME);
    }

    public static String HmacSHA256Encrypt(String encryptText, String encryptKey) {
        return HmacSHAEncrypt(encryptText, encryptKey, HMAC_SHA256_NAME);
    }

    public static String HmacMD5Encrypt(String encryptText, String encryptKey) {
        return HmacSHAEncrypt(encryptText, encryptKey, HMAC_MD5_NAME);
    }

    public static String HmacSHAEncrypt(String encryptText, String encryptKey, String HmacSHA) {
        try {
            SecretKey secretKey = new SecretKeySpec(encryptKey.getBytes(ENCODING), HmacSHA);
            Mac mac = Mac.getInstance(HmacSHA);
            mac.init(secretKey);
            return BytesUtil.toHexString(mac.doFinal(encryptText.getBytes(ENCODING))).toLowerCase();
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public static String HmacSHA1EncryptBase64(String encryptText, String encryptKey) {
        try {
            SecretKey secretKey = new SecretKeySpec(encryptKey.getBytes(ENCODING), HMAC_SHA1_NAME);
            Mac mac = Mac.getInstance(HMAC_SHA1_NAME);
            mac.init(secretKey);
            return Base64.encodeToString(mac.doFinal(encryptText.getBytes(ENCODING)), 2);
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:33:0x0068 A[SYNTHETIC, Splitter:B:33:0x0068] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static java.lang.String HmacSHAEncrypt(java.io.File r10, java.lang.String r11, java.lang.String r12) {
        /*
            if (r10 == 0) goto L_0x0014
            boolean r8 = r10.exists()
            if (r8 == 0) goto L_0x0014
            boolean r8 = android.text.TextUtils.isEmpty(r11)
            if (r8 != 0) goto L_0x0014
            boolean r8 = android.text.TextUtils.isEmpty(r12)
            if (r8 == 0) goto L_0x0018
        L_0x0014:
            java.lang.String r8 = ""
        L_0x0017:
            return r8
        L_0x0018:
            r3 = 0
            java.lang.String r8 = "UTF-8"
            byte[] r1 = r11.getBytes(r8)     // Catch:{ Exception -> 0x0071 }
            javax.crypto.spec.SecretKeySpec r7 = new javax.crypto.spec.SecretKeySpec     // Catch:{ Exception -> 0x0071 }
            r7.<init>(r1, r12)     // Catch:{ Exception -> 0x0071 }
            javax.crypto.Mac r6 = javax.crypto.Mac.getInstance(r12)     // Catch:{ Exception -> 0x0071 }
            r6.init(r7)     // Catch:{ Exception -> 0x0071 }
            java.io.FileInputStream r4 = new java.io.FileInputStream     // Catch:{ Exception -> 0x0071 }
            r4.<init>(r10)     // Catch:{ Exception -> 0x0071 }
            r8 = 2048(0x800, float:2.87E-42)
            byte[] r0 = new byte[r8]     // Catch:{ Exception -> 0x0041, all -> 0x006e }
        L_0x0035:
            int r5 = r4.read(r0)     // Catch:{ Exception -> 0x0041, all -> 0x006e }
            r8 = -1
            if (r5 == r8) goto L_0x0051
            r8 = 0
            r6.update(r0, r8, r5)     // Catch:{ Exception -> 0x0041, all -> 0x006e }
            goto L_0x0035
        L_0x0041:
            r2 = move-exception
            r3 = r4
        L_0x0043:
            r2.printStackTrace()     // Catch:{ all -> 0x0065 }
            java.lang.String r8 = ""
            if (r3 == 0) goto L_0x0017
            r3.close()     // Catch:{ IOException -> 0x004f }
            goto L_0x0017
        L_0x004f:
            r9 = move-exception
            goto L_0x0017
        L_0x0051:
            byte[] r8 = r6.doFinal()     // Catch:{ Exception -> 0x0041, all -> 0x006e }
            java.lang.String r8 = dji.midware.util.BytesUtil.toHexString(r8)     // Catch:{ Exception -> 0x0041, all -> 0x006e }
            java.lang.String r8 = r8.toLowerCase()     // Catch:{ Exception -> 0x0041, all -> 0x006e }
            if (r4 == 0) goto L_0x0017
            r4.close()     // Catch:{ IOException -> 0x0063 }
            goto L_0x0017
        L_0x0063:
            r9 = move-exception
            goto L_0x0017
        L_0x0065:
            r8 = move-exception
        L_0x0066:
            if (r3 == 0) goto L_0x006b
            r3.close()     // Catch:{ IOException -> 0x006c }
        L_0x006b:
            throw r8
        L_0x006c:
            r9 = move-exception
            goto L_0x006b
        L_0x006e:
            r8 = move-exception
            r3 = r4
            goto L_0x0066
        L_0x0071:
            r2 = move-exception
            goto L_0x0043
        */
        throw new UnsupportedOperationException("Method not decompiled: dji.common.util.HmacEncryptUtil.HmacSHAEncrypt(java.io.File, java.lang.String, java.lang.String):java.lang.String");
    }

    private HmacEncryptUtil() {
    }

    public static byte[] encode2bytes(String source) {
        if (source == null) {
            return null;
        }
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.reset();
            md.update(source.getBytes(ENCODING));
            return md.digest();
        } catch (UnsupportedEncodingException | NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String encodeMD5(String source) {
        byte[] data = encode2bytes(source);
        if (data == null) {
            return "";
        }
        StringBuffer hexString = new StringBuffer();
        for (byte b : data) {
            String hex = Integer.toHexString(b & 255);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }
}
