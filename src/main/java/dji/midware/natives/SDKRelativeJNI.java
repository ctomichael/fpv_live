package dji.midware.natives;

import android.support.annotation.Keep;
import android.util.Log;
import com.dji.megatronking.stringfog.lib.annotation.StringFogIgnore;
import dji.fieldAnnotation.EXClassNullAway;

@Keep
@EXClassNullAway
@StringFogIgnore
public class SDKRelativeJNI {
    public static native String native_getBatteryBanSnListUrl();

    public static native String native_getBatteryValidatingSPKey();

    public static native String native_getRemoteServerBindURL();

    public static native String native_getRemoteServerCountryCodeURL();

    public static native String native_getRemoteServerDevUrl();

    public static native String native_getRemoteServerDevUserName();

    public static native String native_getRemoteServerProdUrl();

    public static native String native_getRemoteServerProdUserName();

    public static native String native_getRemoteServerStageUrl();

    public static native String native_getRemoteServerStageUserName();

    public static native String native_getRequestKey();

    public static native String native_getSDKConfigFileName();

    public static native String native_getServerUrl();

    public static native String native_getStatTestUrl();

    public static native String native_getUsbAccessoryAttachedString();

    public static native byte[] native_getXXXX(String str);

    public static native boolean native_isRunningIVT(String str);

    public static native boolean native_isSDKActivated();

    public static native int native_startRegistration(String str, String str2, String str3, String str4);

    public static native int native_startRegistrationWithoutInternet(String str, String str2, String str3);

    static {
        try {
            System.loadLibrary("SDKRelativeJNI");
            Log.d("SDKRelativeJNI", "load lib suc");
        } catch (UnsatisfiedLinkError e) {
            e.printStackTrace();
            Log.d("SDKRelativeJNI", "Couldn't load lib");
        }
    }
}
