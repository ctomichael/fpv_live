package dji.midware.util;

import android.util.Log;
import dji.fieldAnnotation.EXClassNullAway;

@EXClassNullAway
public class DebugFlag {
    public static final boolean FACTORY_FLAG = false;
    public static final boolean FLAG = true;
    public static final boolean PRO_DEBUG = false;
    public static final boolean SETTINGS_DEBUG = false;

    public static final void printfLog(String tag, String msg) {
        if (tag == null) {
            tag = "Lightbridge";
        }
        Log.d(tag, msg);
    }
}
