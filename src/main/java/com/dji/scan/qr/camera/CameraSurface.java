package com.dji.scan.qr.camera;

import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.os.Build;
import android.view.SurfaceHolder;
import dji.fieldAnnotation.EXClassNullAway;
import java.io.IOException;

@EXClassNullAway
public class CameraSurface {
    private SurfaceHolder surfaceHolder;
    private SurfaceTexture surfaceTexture;

    public CameraSurface(SurfaceHolder surfaceHolder2) {
        if (surfaceHolder2 == null) {
            throw new IllegalArgumentException("surfaceHolder may not be null");
        }
        this.surfaceHolder = surfaceHolder2;
    }

    public CameraSurface(SurfaceTexture surfaceTexture2) {
        if (surfaceTexture2 == null) {
            throw new IllegalArgumentException("surfaceTexture may not be null");
        }
        this.surfaceTexture = surfaceTexture2;
    }

    public SurfaceHolder getSurfaceHolder() {
        return this.surfaceHolder;
    }

    public SurfaceTexture getSurfaceTexture() {
        return this.surfaceTexture;
    }

    public void setPreview(Camera camera) throws IOException {
        if (this.surfaceHolder != null) {
            camera.setPreviewDisplay(this.surfaceHolder);
        } else if (Build.VERSION.SDK_INT >= 11) {
            camera.setPreviewTexture(this.surfaceTexture);
        } else {
            throw new IllegalStateException("SurfaceTexture not supported.");
        }
    }
}
