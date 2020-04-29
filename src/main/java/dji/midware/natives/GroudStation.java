package dji.midware.natives;

import android.support.annotation.Keep;
import android.util.Log;
import dji.fieldAnnotation.EXClassNullAway;

@Keep
@EXClassNullAway
public class GroudStation {
    public static native short native_calcCrc16(byte[] bArr, int i);

    public static native byte native_calcCrc8(byte[] bArr);

    public static native int native_checkCRCForData(byte[] bArr);

    public static native byte[] native_decodeData(byte[] bArr);

    public static native byte[] native_encodeData(byte[] bArr);

    public static native byte[] native_getCRCFromData(byte[] bArr);

    public static native long native_hashFromString(byte[] bArr);

    public static native byte[] native_rcDataDeal(byte[] bArr, int i);

    public static native boolean native_verifyCrc16(byte[] bArr);

    public static native boolean native_verifyCrc8(byte[] bArr);

    public static native int[] native_yuv422ToImage(byte[] bArr, int i, int i2);

    static {
        try {
            System.loadLibrary("GroudStation");
            Log.d("GroudStation", "load lib suc");
        } catch (UnsatisfiedLinkError e) {
            e.printStackTrace();
            Log.d("GroudStation", "Couldn't load lib");
        }
    }

    public static void loadLibrary() {
    }

    public static short native_calcCrc16(byte[] buffer) {
        return native_calcCrc16(buffer, buffer.length);
    }
}
