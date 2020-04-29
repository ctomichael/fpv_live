package dji.midware.sdr.log;

import android.util.Base64;
import com.dji.megatronking.stringfog.lib.annotation.DJIStringFog;
import dji.log.IEncryption;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

public class CommonLogEncryption implements IEncryption {
    static final String ALGORITHM = "PBKDF2WithHmacSHA1";
    static final String CHARSET_NAME = "UTF-8";
    static final String HEX_STR = "0123456789abcdef";
    static final int ITERATION_COUNT = 1000;
    @DJIStringFog
    static final String IV_PARAMETER = "9d6c5cab5b0281255a222d1c861ddfdf";
    static final String KEY_ALGORITHM = "AES";
    static final int KEY_LENGTH = 128;
    @DJIStringFog
    private static final String KEY_STR = "DJILog@SimpleEncryption";
    @DJIStringFog
    static final String SALT = "c8570ac98cc615aa6a6b97b3f20f1b41";
    static final String TRANSFORMATION = "AES/CBC/PKCS5Padding";
    private SecretKey KEY;

    public byte[] encrypt(byte[] msg) throws Exception {
        if (!check()) {
            return msg;
        }
        Cipher enCipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        enCipher.init(1, this.KEY, new IvParameterSpec(hexStringToBinary(IV_PARAMETER)));
        return Base64.encode(enCipher.doFinal(msg), 2);
    }

    public String encrypt(String msg) throws Exception {
        if (!check()) {
            return msg;
        }
        Cipher enCipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        enCipher.init(1, this.KEY, new IvParameterSpec(hexStringToBinary(IV_PARAMETER)));
        return new String(Base64.encode(enCipher.doFinal(msg.getBytes(CHARSET_NAME)), 2), CHARSET_NAME);
    }

    public String decrypt(String msg) throws Exception {
        if (!check()) {
            return msg;
        }
        Cipher deCipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        deCipher.init(2, this.KEY, new IvParameterSpec(hexStringToBinary(IV_PARAMETER)));
        return new String(deCipher.doFinal(Base64.decode(msg.getBytes(CHARSET_NAME), 2)), CHARSET_NAME);
    }

    private boolean check() {
        if (this.KEY == null) {
            try {
                this.KEY = new SecretKeySpec(SecretKeyFactory.getInstance(ALGORITHM).generateSecret(new PBEKeySpec(KEY_STR.toCharArray(), hexStringToBinary(SALT), 1000, 128)).getEncoded(), KEY_ALGORITHM);
            } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
                e.printStackTrace();
                return false;
            }
        }
        return true;
    }

    public static String binaryToHexString(byte[] bytes) {
        String result = "";
        for (byte aByte : bytes) {
            result = result + (String.valueOf(HEX_STR.charAt((aByte & 240) >> 4)) + String.valueOf(HEX_STR.charAt(aByte & 15)));
        }
        return result;
    }

    public static byte[] hexStringToBinary(String hexString) {
        int len = hexString.length() / 2;
        byte[] bytes = new byte[len];
        for (int i = 0; i < len; i++) {
            bytes[i] = (byte) (((byte) (HEX_STR.indexOf(hexString.charAt(i * 2)) << 4)) | ((byte) HEX_STR.indexOf(hexString.charAt((i * 2) + 1))));
        }
        return bytes;
    }
}
