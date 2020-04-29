package com.dji.component.fpv.widget.preview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.support.annotation.Keep;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import com.dji.component.persistence.DJIPersistenceDataListener;
import com.dji.component.persistence.DJIPersistenceStorage;
import com.dji.video.framing.internal.opengl.surface.SurfaceInterface;
import com.dji.video.framing.utils.DJIVideoUtil;
import dji.log.DJILog;
import dji.midware.data.manager.Dpad.DpadProductManager;
import dji.midware.interfaces.RecvDataCallback;
import dji.midware.media.newframing.SurfaceManager;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

@Keep
public class VideoSurfaceView extends SurfaceView implements RecvDataCallback {
    private CountDownLatch cl;
    /* access modifiers changed from: private */
    public DJIGLSurfaceViewListener listener;
    /* access modifiers changed from: private */
    public DJIPersistenceDataListener mDJIPersistenceDataListener = new DJIPersistenceDataListener() {
        /* class com.dji.component.fpv.widget.preview.VideoSurfaceView.AnonymousClass2 */

        public void onValueUpdate(String id, String key) {
            DJILog.d("onValueUpdate() KEY_OVER_EXPOSURE_KEY:" + key + "key value:" + DJIPersistenceStorage.getBoolean(key, false), new Object[0]);
            if (DJIVideoUtil.KEY_OVER_EXPOSURE_KEY.equals(key)) {
                VideoSurfaceView.this.renderManager.enableOverExposureWarning(DJIPersistenceStorage.getBoolean(key, false), R.raw.overexposure);
            }
        }
    };
    /* access modifiers changed from: private */
    public int mRotateDegree;
    /* access modifiers changed from: private */
    public SurfaceInterface renderManager;
    private Bitmap screenBitmap;

    public interface DJIGLSurfaceViewListener {
        DJIVideoDecoderController onRenderCreated(SurfaceInterface surfaceInterface);

        void onRenderCreatedPrepared();

        void onRenderDestroy();
    }

    public VideoSurfaceView(Context context) {
        super(context);
        DJILog.d("VideoSurfaceView 1", new Object[0]);
        init();
    }

    public VideoSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        if (!isInEditMode()) {
            DJILog.d("VideoSurfaceView lcd", new Object[0]);
            init();
        }
    }

    private void init() {
        getHolder().setFormat(4);
        if (!DpadProductManager.getInstance().isDpad()) {
            getHolder().setKeepScreenOn(true);
        }
        getHolder().addCallback(new SurfaceHolder.Callback() {
            /* class com.dji.component.fpv.widget.preview.VideoSurfaceView.AnonymousClass1 */

            public void surfaceCreated(SurfaceHolder holder) {
                VideoSurfaceView.this.listener.onRenderCreatedPrepared();
                ViewGroup.LayoutParams lp = VideoSurfaceView.this.getLayoutParams();
                SurfaceInterface unused = VideoSurfaceView.this.renderManager = SurfaceManager.createInstance();
                VideoSurfaceView.this.renderManager.enableOverExposureWarning(DJIPersistenceStorage.getBoolean(DJIVideoUtil.KEY_OVER_EXPOSURE_KEY, false), R.raw.overexposure);
                VideoSurfaceView.this.renderManager.init(VideoSurfaceView.this.getHolder(), lp.width, lp.height, 0, VideoSurfaceView.this.mRotateDegree);
                VideoSurfaceView.this.listener.onRenderCreated(VideoSurfaceView.this.renderManager);
                DJIPersistenceStorage.addListener(VideoSurfaceView.this.mDJIPersistenceDataListener, DJIVideoUtil.KEY_OVER_EXPOSURE_KEY);
            }

            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
                if (VideoSurfaceView.this.renderManager != null) {
                    VideoSurfaceView.this.renderManager.reSizeSurface(width, height, 0, VideoSurfaceView.this.mRotateDegree);
                }
            }

            public void surfaceDestroyed(SurfaceHolder holder) {
                VideoSurfaceView.this.renderManager.destroy();
                VideoSurfaceView.this.listener.onRenderDestroy();
                DJIPersistenceStorage.removeListener(VideoSurfaceView.this.mDJIPersistenceDataListener);
            }
        });
    }

    public void setRotateDegree(int rotateDegree) {
        this.mRotateDegree = rotateDegree;
    }

    public void forceRotate() {
        if (this.renderManager != null) {
            RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) getLayoutParams();
            this.renderManager.reSizeSurface(lp.width, lp.height, 0, 0);
            this.renderManager.onFrameAvailable(null);
        }
    }

    public void toGray() {
        if (this.renderManager != null) {
            this.renderManager.toGray();
        }
    }

    public Bitmap getBitmap() {
        this.screenBitmap = null;
        this.cl = new CountDownLatch(1);
        if (this.renderManager != null) {
            this.renderManager.getBitmap(getWidth(), getHeight(), new VideoSurfaceView$$Lambda$0(this));
            try {
                this.cl.await(500, TimeUnit.MILLISECONDS);
            } catch (Exception e) {
            }
        }
        return this.screenBitmap;
    }

    /* access modifiers changed from: package-private */
    public final /* synthetic */ void lambda$getBitmap$0$VideoSurfaceView(Bitmap bmp) {
        this.screenBitmap = bmp;
        this.cl.countDown();
    }

    public void oneFrameComeIn() {
    }

    public void resetVideoSurface(int videoWidth, int videoHeight) {
    }

    /* access modifiers changed from: protected */
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    public void draw(Canvas canvas) {
        super.draw(canvas);
    }

    public void setRenderListener(DJIGLSurfaceViewListener listener2) {
        this.listener = listener2;
    }

    public void setRenderer() {
    }

    public void onResume() {
    }

    public void onPause() {
    }

    public void setLayoutParams(ViewGroup.LayoutParams params) {
        super.setLayoutParams(params);
    }
}
