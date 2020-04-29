package dji.pilot.aes;

import android.util.Base64;
import com.dji.api.protocol.IAccountCenterHttpApi;
import dji.fieldAnnotation.EXClassNullAway;
import java.math.BigInteger;
import java.util.Locale;
import javax.crypto.Cipher;
import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

@EXClassNullAway
public class AES {
    public static boolean DEBUG_LOG_ENABLED = true;
    private static byte[] iv = new byte[16];

    public static String decrypt(String encrypted, String key) throws Exception {
        byte[] bArr = new byte[32];
        SecretKeySpec skey = new SecretKeySpec(key.getBytes("utf-8"), "AES");
        Cipher dcipher = Cipher.getInstance(IAccountCenterHttpApi.ENCRYPT_TRANSFORMATION);
        dcipher.init(2, skey, new IvParameterSpec(iv));
        return new String(dcipher.doFinal(Base64.decode(encrypted, 8)));
    }

    public static String encrypt(String content, String key) throws Exception {
        byte[] input = content.getBytes("utf-8");
        byte[] bArr = new byte[32];
        SecretKeySpec skc = new SecretKeySpec(key.getBytes("utf-8"), "AES");
        Cipher cipher = Cipher.getInstance(IAccountCenterHttpApi.ENCRYPT_TRANSFORMATION);
        cipher.init(1, skc, new IvParameterSpec(iv));
        return Base64.encodeToString(cipher.doFinal(input), 8).replace("\n", "");
    }

    public static String encryptHmacSha256(String data, String key) {
        try {
            SecretKey secretKey = new SecretKeySpec(key.getBytes(), "HmacSha256");
            Mac mac = Mac.getInstance(secretKey.getAlgorithm());
            mac.init(secretKey);
            byte[] res = mac.doFinal(data.getBytes());
            return String.format(Locale.US, "%0" + (res.length * 2) + "X", new BigInteger(1, res));
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }
}
