package dji.midware.natives;

import android.support.annotation.Keep;
import android.util.Log;
import com.dji.frame.util.MD5;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.util.DJISafeUtil;
import java.security.SignatureException;

@Keep
@EXClassNullAway
public class FREncrypt {
    public static native long decryptFRData(byte[] bArr, byte[] bArr2, int i, int i2, long j);

    public static native long encryptFRData(byte[] bArr, byte[] bArr2, int i, int i2, long j);

    public static native String getVerifyStr(byte[] bArr);

    public static native void test();

    public static native void verifySign();

    static {
        try {
            System.loadLibrary("FRCorkscrew");
            Log.d("FRCorkscrew", "load lib suc");
        } catch (UnsatisfiedLinkError e) {
            e.printStackTrace();
            Log.d("FRCorkscrew", "Couldn't load lib");
        }
    }

    public static void loadLibrary() {
    }

    public static String getMD5(byte[] buffer) {
        return MD5.getMD5(buffer);
    }

    public static String calSHA1(String data, String key) {
        try {
            return DJISafeUtil.calHMACsha1(data, key);
        } catch (SignatureException e) {
            e.printStackTrace();
            return "";
        }
    }
}
