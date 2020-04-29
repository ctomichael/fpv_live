package dji.publics;

import android.os.Handler;
import android.os.Looper;
import dji.fieldAnnotation.EXClassNullAway;

@EXClassNullAway
public class DJIUIHandler {
    public static Handler handler = new Handler(Looper.getMainLooper());

    public static void post(Runnable r) {
        handler.post(r);
    }

    public static Handler get() {
        return handler;
    }
}
