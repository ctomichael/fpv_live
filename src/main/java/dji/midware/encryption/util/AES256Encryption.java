package dji.midware.encryption.util;

import android.text.TextUtils;
import android.util.Base64;
import com.dji.megatronking.stringfog.lib.annotation.DJIStringFog;
import dji.fieldAnnotation.EXClassNullAway;
import dji.log.DJILog;
import java.math.BigInteger;
import java.security.Key;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.Signature;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.Locale;
import javax.crypto.Cipher;
import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

@EXClassNullAway
public class AES256Encryption {
    private static final String CIPHER_ALGORITHM = "AES/CBC/PKCS5Padding";
    private static final String KEY_ALGORITHM = "AES";
    @DJIStringFog
    public static final String RSA_PUBLIC_KEY = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAy2lB/iaW3KSx8IR2H74g\nk25qQqwRA1eNa7kAjGQ7mswtkcVayF2ftP7xSDeOvlmzoBpzvB6dopZbF5qcVYlk\nRN330ao6P09tnZ7416eB91fF1jWivo+y0uSvIP1+rv93r5aJOnMoCTRmaBIira0a\n++/mCsp800Lp5/qUViGdSUpNzMRn7f7d99bgEbOXPu2Ig0zIelbLL1vAuepaOQKf\n7lpvQVNpl4IhoIvWI7KvOahMuNKVzAfNa53N1vNGT1F/o0sqvun/AfejX7pQog0D\n58WvHpySXlQOtbLCvmoaZFyVos8dCgmRRCGxE4VG45Lt0teK1I2vFVYLwNyXbNtK\nuQIDAQAB";
    private static final String TAG = AES256Encryption.class.getSimpleName();
    private static byte[] ivBytes = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};

    public static boolean RSAVerifyWithPublicKey(String signature) {
        String[] certArray;
        if (TextUtils.isEmpty(signature)) {
            return false;
        }
        try {
            for (String str : new String[]{"MD5withRSA", "SHA1withRSA", "SHA224withRSA", "SHA256withRSA", "SHA384withRSA", "SHA512withRSA"}) {
                Signature sig = Signature.getInstance(str);
                sig.initVerify(createRSAPublicKey());
                if (sig.verify(signature.getBytes())) {
                    return true;
                }
            }
            return false;
        } catch (Exception e) {
            DJILog.e(TAG, "Error encrypting secret key " + e.getMessage(), new Object[0]);
            return false;
        }
    }

    public static PublicKey createRSAPublicKey() throws NoSuchAlgorithmException, InvalidKeySpecException {
        return KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(Base64.decode(RSA_PUBLIC_KEY.getBytes(), 2)));
    }

    private static byte[] initKey(String mKey) throws Exception {
        return mKey.getBytes();
    }

    private static Key toKey(byte[] key) throws Exception {
        return new SecretKeySpec(key, KEY_ALGORITHM);
    }

    public static byte[] encrypt(byte[] data, String key) throws Exception {
        Key k = toKey(initKey(key));
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding", BouncyCastleProvider.PROVIDER_NAME);
        cipher.init(1, k, new IvParameterSpec(ivBytes));
        return cipher.doFinal(data);
    }

    public static byte[] decrypt(byte[] data, String key) throws Exception {
        Key k = toKey(initKey(key));
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding", BouncyCastleProvider.PROVIDER_NAME);
        cipher.init(2, k, new IvParameterSpec(ivBytes));
        return cipher.doFinal(data);
    }

    public static boolean isValid() {
        try {
            if (Cipher.getMaxAllowedKeyLength(KEY_ALGORITHM) >= 256) {
                return true;
            }
            return false;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return false;
        }
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

    public static byte[] getAESKeyFromString(String name) {
        byte[] bytes = new byte[32];
        for (int i = 0; i < 32; i++) {
            if (i + 1 <= name.length()) {
                bytes[i] = (byte) name.charAt(i);
            } else {
                bytes[i] = 0;
            }
        }
        return bytes;
    }
}
