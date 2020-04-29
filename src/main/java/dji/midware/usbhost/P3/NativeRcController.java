package dji.midware.usbhost.P3;

import android.support.annotation.Keep;
import android.util.Log;
import android.view.Surface;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.manager.Dpad.DpadProductManager;

@Keep
@EXClassNullAway
public class NativeRcController {
    static boolean loadSOOK;

    public static native void native_rc_exit();

    public static native void native_rc_init();

    public static native int native_rc_sendto_serial(byte[] bArr, int i);

    public static native int native_rc_setIframe(byte[] bArr, int i);

    public static native int native_rc_setPrdType(int i);

    public static native int native_rc_set_cb_obj(Object obj);

    public static native int native_rc_set_iep(int i, int i2);

    public static native int native_rc_set_sre(int i);

    public static native void native_rc_start_dec(Surface surface);

    public static native int native_rc_stop_dec();

    static {
        loadSOOK = DpadProductManager.getInstance().isPomato();
        try {
            Log.d("NativeRcController", "x try to load libusbdec.so");
            System.loadLibrary("usbdec");
        } catch (UnsatisfiedLinkError e) {
            Log.e("NativeRcController", "Couldn't load libusbdec.so");
            loadSOOK = false;
        }
    }

    public static boolean useUsbdec() {
        return loadSOOK && DpadProductManager.getInstance().useUsbdec();
    }

    public static void loadLibrary() {
    }

    public static void rc_init() {
        if (loadSOOK) {
            native_rc_init();
        }
    }

    public static void rc_exit() {
        if (loadSOOK) {
            native_rc_exit();
        }
    }

    public static void rc_start_dec(Surface sur) {
        if (loadSOOK) {
            native_rc_start_dec(sur);
        }
    }

    public static void rc_stop_dec() {
        if (loadSOOK) {
            native_rc_stop_dec();
        }
    }

    public static void rc_sendto_serial(byte[] buffer, int len) {
        if (loadSOOK) {
            native_rc_sendto_serial(buffer, len);
        }
    }

    public static void rc_setIframe(byte[] buffer, int len) {
        if (loadSOOK) {
            native_rc_setIframe(buffer, len);
        }
    }

    public static void rc_setPrdType(int ype) {
        if (loadSOOK) {
            native_rc_setPrdType(ype);
        }
    }

    public static void rc_set_sre(int value) {
        if (loadSOOK) {
            native_rc_set_sre(value);
        }
    }

    public static void rc_set_iep(int switch_, int value) {
        if (loadSOOK) {
            native_rc_set_iep(switch_, value);
        }
    }

    public static void rc_set_cb_obj(Object obj) {
        if (loadSOOK) {
            native_rc_set_cb_obj(obj);
        }
    }
}
