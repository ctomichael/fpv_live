package com.dji.scan.qr.camera;

import android.hardware.Camera;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import dji.fieldAnnotation.EXClassNullAway;
import java.util.ArrayList;
import java.util.Collection;

@EXClassNullAway
public final class AutoFocusManager {
    private static final long AUTO_FOCUS_INTERVAL_MS = 1000;
    private static final Collection<String> FOCUS_MODES_CALLING_AF = new ArrayList(2);
    private static final String TAG = AutoFocusManager.class.getSimpleName();
    /* access modifiers changed from: private */
    public int MESSAGE_FOCUS = 1;
    private final Camera.AutoFocusCallback autoFocusCallback = new Camera.AutoFocusCallback() {
        /* class com.dji.scan.qr.camera.AutoFocusManager.AnonymousClass2 */

        public void onAutoFocus(boolean success, Camera theCamera) {
            AutoFocusManager.this.handler.post(new Runnable() {
                /* class com.dji.scan.qr.camera.AutoFocusManager.AnonymousClass2.AnonymousClass1 */

                public void run() {
                    boolean unused = AutoFocusManager.this.focusing = false;
                    AutoFocusManager.this.autoFocusAgainLater();
                }
            });
        }
    };
    private final Camera camera;
    private final Handler.Callback focusHandlerCallback = new Handler.Callback() {
        /* class com.dji.scan.qr.camera.AutoFocusManager.AnonymousClass1 */

        public boolean handleMessage(Message msg) {
            if (msg.what != AutoFocusManager.this.MESSAGE_FOCUS) {
                return false;
            }
            AutoFocusManager.this.focus();
            return true;
        }
    };
    /* access modifiers changed from: private */
    public boolean focusing;
    /* access modifiers changed from: private */
    public Handler handler = new Handler(this.focusHandlerCallback);
    private boolean stopped;
    private final boolean useAutoFocus;

    static {
        FOCUS_MODES_CALLING_AF.add("auto");
        FOCUS_MODES_CALLING_AF.add("macro");
    }

    public AutoFocusManager(Camera camera2, CameraSettings settings) {
        boolean z = true;
        this.camera = camera2;
        String currentFocusMode = camera2.getParameters().getFocusMode();
        this.useAutoFocus = (!settings.isAutoFocusEnabled() || !FOCUS_MODES_CALLING_AF.contains(currentFocusMode)) ? false : z;
        Log.i(TAG, "Current focus mode '" + currentFocusMode + "'; use auto focus? " + this.useAutoFocus);
        start();
    }

    /* access modifiers changed from: private */
    public synchronized void autoFocusAgainLater() {
        if (!this.stopped && !this.handler.hasMessages(this.MESSAGE_FOCUS)) {
            this.handler.sendMessageDelayed(this.handler.obtainMessage(this.MESSAGE_FOCUS), 1000);
        }
    }

    public void start() {
        this.stopped = false;
        focus();
    }

    /* access modifiers changed from: private */
    public void focus() {
        if (this.useAutoFocus && !this.stopped && !this.focusing) {
            try {
                this.camera.autoFocus(this.autoFocusCallback);
                this.focusing = true;
            } catch (RuntimeException re) {
                Log.w(TAG, "Unexpected exception while focusing", re);
                autoFocusAgainLater();
            }
        }
    }

    private void cancelOutstandingTask() {
        this.handler.removeMessages(this.MESSAGE_FOCUS);
    }

    public void stop() {
        this.stopped = true;
        this.focusing = false;
        cancelOutstandingTask();
        if (this.useAutoFocus) {
            try {
                this.camera.cancelAutoFocus();
            } catch (RuntimeException re) {
                Log.w(TAG, "Unexpected exception while cancelling focusing", re);
            }
        }
    }
}
