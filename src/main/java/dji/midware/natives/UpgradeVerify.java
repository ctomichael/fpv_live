package dji.midware.natives;

import android.support.annotation.Keep;
import android.util.Log;
import dji.fieldAnnotation.EXClassNullAway;

@Keep
@EXClassNullAway
public class UpgradeVerify {
    public static native String native_getMD5Input(String str, int i);

    public static native boolean native_parserTlv(String str, String str2);

    public static native boolean native_verifyCfg(String str, String str2, boolean z);

    public static native boolean native_verifyFile(String str, String str2);

    static {
        try {
            System.loadLibrary("UpgradeVerify");
            Log.d("UpgradeVerify", "load lib suc");
        } catch (UnsatisfiedLinkError e) {
            e.printStackTrace();
            Log.d("UpgradeVerify", "Couldn't load lib");
        }
    }

    public static void loadLibrary() {
    }

    public static String native_getMD5Input(String input) {
        return native_getMD5Input(input, 0);
    }

    public static boolean native_verifyCfg(String path, String outPath) {
        return native_verifyCfg(path, outPath, true);
    }
}
