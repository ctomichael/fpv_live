package dji.pilot.publics.util;

import android.content.Context;
import android.os.Build;
import android.os.VibrationEffect;
import android.os.Vibrator;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.manager.Dpad.DpadProductManager;

@EXClassNullAway
public class DJIVibrator {
    private Context mContext;
    private Vibrator mOsVibrator;

    public static DJIVibrator getInstance() {
        return SingletonHolder.mInstance;
    }

    public void initialize(Context context) {
        if (this.mContext == null) {
            this.mOsVibrator = (Vibrator) context.getSystemService("vibrator");
            this.mContext = context.getApplicationContext();
        }
    }

    public void finalizeMe() {
        if (this.mOsVibrator != null && this.mOsVibrator.hasVibrator()) {
            this.mOsVibrator.cancel();
        }
        this.mContext = null;
        this.mOsVibrator = null;
    }

    public void vibrate(long[] pattern, int repeat) {
        if (canVibrate()) {
            this.mOsVibrator.vibrate(pattern, repeat);
        }
    }

    public void vibrate(long milliseconds) {
        if (!canVibrate()) {
            return;
        }
        if (Build.VERSION.SDK_INT >= 26) {
            this.mOsVibrator.vibrate(VibrationEffect.createOneShot(milliseconds, -1));
        } else {
            this.mOsVibrator.vibrate(milliseconds);
        }
    }

    public void cancel() {
        if (canVibrate()) {
            this.mOsVibrator.cancel();
        }
    }

    private boolean canVibrate() {
        return !DpadProductManager.getInstance().isDpad() && this.mOsVibrator != null && this.mOsVibrator.hasVibrator();
    }

    private DJIVibrator() {
        this.mOsVibrator = null;
        this.mContext = null;
    }

    private static final class SingletonHolder {
        /* access modifiers changed from: private */
        public static final DJIVibrator mInstance = new DJIVibrator();

        private SingletonHolder() {
        }
    }
}
