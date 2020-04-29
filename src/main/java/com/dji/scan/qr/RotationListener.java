package com.dji.scan.qr;

import android.content.Context;
import android.view.OrientationEventListener;
import android.view.WindowManager;
import dji.fieldAnnotation.EXClassNullAway;

@EXClassNullAway
public class RotationListener {
    /* access modifiers changed from: private */
    public RotationCallback callback;
    /* access modifiers changed from: private */
    public int lastRotation;
    private OrientationEventListener orientationEventListener;
    /* access modifiers changed from: private */
    public WindowManager windowManager;

    public void listen(Context context, RotationCallback callback2) {
        stop();
        Context context2 = context.getApplicationContext();
        this.callback = callback2;
        this.windowManager = (WindowManager) context2.getSystemService("window");
        this.orientationEventListener = new OrientationEventListener(context2, 3) {
            /* class com.dji.scan.qr.RotationListener.AnonymousClass1 */

            public void onOrientationChanged(int orientation) {
                int newRotation;
                WindowManager localWindowManager = RotationListener.this.windowManager;
                RotationCallback localCallback = RotationListener.this.callback;
                if (RotationListener.this.windowManager != null && localCallback != null && (newRotation = localWindowManager.getDefaultDisplay().getRotation()) != RotationListener.this.lastRotation) {
                    int unused = RotationListener.this.lastRotation = newRotation;
                    localCallback.onRotationChanged(newRotation);
                }
            }
        };
        this.orientationEventListener.enable();
        this.lastRotation = this.windowManager.getDefaultDisplay().getRotation();
    }

    public void stop() {
        if (this.orientationEventListener != null) {
            this.orientationEventListener.disable();
        }
        this.orientationEventListener = null;
        this.windowManager = null;
        this.callback = null;
    }
}
