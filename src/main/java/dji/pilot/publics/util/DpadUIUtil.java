package dji.pilot.publics.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.view.Window;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.manager.Dpad.DpadProductManager;
import dji.pilot.fpv.util.DJIFlurryReport;

@EXClassNullAway
public class DpadUIUtil {

    public interface DpadOrientationHelper {
        void requestedDpadOrientation();
    }

    public static void hideSystemUIImmediately(Window window) {
        window.getDecorView().setSystemUiVisibility(3847);
    }

    public static void hideStatusBar(Activity activity) {
        if (Build.VERSION.SDK_INT >= 21) {
            Window window = activity.getWindow();
            window.getDecorView().setSystemUiVisibility(3847);
            window.addFlags(1024);
        }
        setSystemFullScreen(activity, true);
    }

    public static void setSystemFullScreen(Context context, boolean enable) {
        if (DpadProductManager.getInstance().isDpad()) {
            Intent intent = new Intent("dji.intent.action.FULLSCREEN");
            intent.putExtra(DJIFlurryReport.GroundStation.V2_GS_ENABLE_KEY, enable);
            context.sendBroadcast(intent);
        }
    }
}
