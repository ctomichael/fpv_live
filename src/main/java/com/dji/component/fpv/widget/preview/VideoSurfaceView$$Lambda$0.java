package com.dji.component.fpv.widget.preview;

import android.graphics.Bitmap;
import com.dji.video.framing.internal.opengl.surface.SurfaceInterface;

final /* synthetic */ class VideoSurfaceView$$Lambda$0 implements SurfaceInterface.BitmapCallback {
    private final VideoSurfaceView arg$1;

    VideoSurfaceView$$Lambda$0(VideoSurfaceView videoSurfaceView) {
        this.arg$1 = videoSurfaceView;
    }

    public void onResult(Bitmap bitmap) {
        this.arg$1.lambda$getBitmap$0$VideoSurfaceView(bitmap);
    }
}
