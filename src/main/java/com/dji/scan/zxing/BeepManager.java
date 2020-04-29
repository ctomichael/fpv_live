package com.dji.scan.zxing;

import android.app.Activity;
import android.content.Context;
import android.os.Vibrator;
import dji.fieldAnnotation.EXClassNullAway;

@EXClassNullAway
public final class BeepManager {
    private static final float BEEP_VOLUME = 0.1f;
    /* access modifiers changed from: private */
    public static final String TAG = BeepManager.class.getSimpleName();
    private static final long VIBRATE_DURATION = 200;
    private boolean beepEnabled = true;
    private final Context context;
    private boolean vibrateEnabled = false;

    public BeepManager(Activity activity) {
        activity.setVolumeControlStream(3);
        this.context = activity.getApplicationContext();
    }

    public boolean isBeepEnabled() {
        return this.beepEnabled;
    }

    public void setBeepEnabled(boolean beepEnabled2) {
        this.beepEnabled = beepEnabled2;
    }

    public boolean isVibrateEnabled() {
        return this.vibrateEnabled;
    }

    public void setVibrateEnabled(boolean vibrateEnabled2) {
        this.vibrateEnabled = vibrateEnabled2;
    }

    public synchronized void playBeepSoundAndVibrate() {
        if (this.beepEnabled) {
            playBeepSound();
        }
        if (this.vibrateEnabled) {
            ((Vibrator) this.context.getSystemService("vibrator")).vibrate(200);
        }
    }

    /* JADX WARNING: No exception handlers in catch block: Catch:{  } */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public android.media.MediaPlayer playBeepSound() {
        /*
            r8 = this;
            android.media.MediaPlayer r0 = new android.media.MediaPlayer
            r0.<init>()
            r1 = 3
            r0.setAudioStreamType(r1)
            com.dji.scan.zxing.BeepManager$1 r1 = new com.dji.scan.zxing.BeepManager$1
            r1.<init>()
            r0.setOnCompletionListener(r1)
            com.dji.scan.zxing.BeepManager$2 r1 = new com.dji.scan.zxing.BeepManager$2
            r1.<init>()
            r0.setOnErrorListener(r1)
            android.content.Context r1 = r8.context     // Catch:{ IOException -> 0x004c }
            android.content.res.Resources r1 = r1.getResources()     // Catch:{ IOException -> 0x004c }
            int r2 = com.dji.pubmodule.R.raw.zxing_beep     // Catch:{ IOException -> 0x004c }
            android.content.res.AssetFileDescriptor r6 = r1.openRawResourceFd(r2)     // Catch:{ IOException -> 0x004c }
            java.io.FileDescriptor r1 = r6.getFileDescriptor()     // Catch:{ all -> 0x0047 }
            long r2 = r6.getStartOffset()     // Catch:{ all -> 0x0047 }
            long r4 = r6.getLength()     // Catch:{ all -> 0x0047 }
            r0.setDataSource(r1, r2, r4)     // Catch:{ all -> 0x0047 }
            r6.close()     // Catch:{ IOException -> 0x004c }
            r1 = 1036831949(0x3dcccccd, float:0.1)
            r2 = 1036831949(0x3dcccccd, float:0.1)
            r0.setVolume(r1, r2)     // Catch:{ IOException -> 0x004c }
            r0.prepare()     // Catch:{ IOException -> 0x004c }
            r0.start()     // Catch:{ IOException -> 0x004c }
        L_0x0046:
            return r0
        L_0x0047:
            r1 = move-exception
            r6.close()     // Catch:{ IOException -> 0x004c }
            throw r1     // Catch:{ IOException -> 0x004c }
        L_0x004c:
            r7 = move-exception
            java.lang.String r1 = com.dji.scan.zxing.BeepManager.TAG
            android.util.Log.w(r1, r7)
            r0.release()
            r0 = 0
            goto L_0x0046
        */
        throw new UnsupportedOperationException("Method not decompiled: com.dji.scan.zxing.BeepManager.playBeepSound():android.media.MediaPlayer");
    }
}
