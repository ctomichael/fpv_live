package com.dji.service;

import com.dji.api.protocol.IAccountCenterHttpApi;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.util.BytesUtil;
import java.security.SignatureException;
import java.util.Locale;
import javax.crypto.Cipher;
import javax.crypto.Mac;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

@EXClassNullAway
public class SignatureUtil {
    private static final String HMAC_SHA1_ALGORITHM = "HmacSHA1";
    private static byte[] iv = new byte[16];

    public static String calculateRFC2104HMAC(String data, String key) throws SignatureException {
        try {
            SecretKeySpec signingKey = new SecretKeySpec(key.getBytes(), HMAC_SHA1_ALGORITHM);
            Mac mac = Mac.getInstance(HMAC_SHA1_ALGORITHM);
            mac.init(signingKey);
            return BytesUtil.byte2hexNoSep(mac.doFinal(data.getBytes())).toUpperCase(Locale.ENGLISH);
        } catch (Exception e) {
            throw new SignatureException("Failed to generate HMAC : " + e.getMessage());
        }
    }

    public static String encryptEAS(String data, String key) throws Exception {
        byte[] input = data.getBytes("utf-8");
        SecretKeySpec skc = new SecretKeySpec(key.getBytes("utf-8"), "AES");
        Cipher cipher = Cipher.getInstance(IAccountCenterHttpApi.ENCRYPT_TRANSFORMATION);
        cipher.init(1, skc, new IvParameterSpec(iv));
        return BytesUtil.byte2hexNoSep(cipher.doFinal(input));
    }
}
