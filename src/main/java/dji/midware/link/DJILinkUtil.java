package dji.midware.link;

import dji.midware.data.manager.Dpad.DpadProductManager;
import dji.midware.usbhost.P3.NativeRcController;

public class DJILinkUtil {
    public static boolean useUsbRcOrWifi() {
        return usbUsbRc() || useUsbWifi();
    }

    public static boolean usbUsbRc() {
        return NativeRcController.useUsbdec();
    }

    public static boolean useUsbWifi() {
        return DpadProductManager.getInstance().useUsbWifiLink();
    }

    private DJILinkUtil() {
    }
}
