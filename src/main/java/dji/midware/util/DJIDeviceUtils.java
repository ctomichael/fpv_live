package dji.midware.util;

import android.os.Build;
import dji.fieldAnnotation.EXClassNullAway;

@EXClassNullAway
public class DJIDeviceUtils {
    public static boolean isZS600Device() {
        return Build.DEVICE != null && Build.DEVICE.startsWith("zs600");
    }
}
