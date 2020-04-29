package com.dji.permission.checker;

import android.content.Context;
import android.hardware.Camera;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

class CameraTest implements PermissionTest {
    private static final SurfaceHolder.Callback CALLBACK = new SurfaceHolder.Callback() {
        /* class com.dji.permission.checker.CameraTest.AnonymousClass2 */

        public void surfaceCreated(SurfaceHolder holder) {
        }

        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        }

        public void surfaceDestroyed(SurfaceHolder holder) {
        }
    };
    private static final Camera.PreviewCallback PREVIEW_CALLBACK = new Camera.PreviewCallback() {
        /* class com.dji.permission.checker.CameraTest.AnonymousClass1 */

        public void onPreviewFrame(byte[] data, Camera camera) {
        }
    };
    private SurfaceHolder mHolder;

    CameraTest(Context context) {
        this.mHolder = new SurfaceView(context).getHolder();
        this.mHolder.addCallback(CALLBACK);
    }

    public boolean test() throws Throwable {
        Camera camera = null;
        try {
            camera = Camera.open();
            camera.setParameters(camera.getParameters());
            camera.setPreviewDisplay(this.mHolder);
            camera.setPreviewCallback(PREVIEW_CALLBACK);
            camera.startPreview();
            return true;
        } finally {
            if (camera != null) {
                camera.stopPreview();
                camera.setPreviewDisplay(null);
                camera.setPreviewCallback(null);
                camera.release();
            }
        }
    }
}
