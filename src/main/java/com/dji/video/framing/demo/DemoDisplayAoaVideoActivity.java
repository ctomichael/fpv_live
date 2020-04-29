package com.dji.video.framing.demo;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.ToggleButton;
import com.dji.video.framing.R;
import com.dji.video.framing.internal.VideoFrame;
import com.dji.video.framing.internal.decoder.DJIVideoDecoder;
import com.dji.video.framing.internal.opengl.surface.SurfaceInterface;
import com.dji.video.framing.internal.parser.VideoStreamParseController;
import com.dji.video.framing.utils.BackgroundLooper;

public class DemoDisplayAoaVideoActivity extends Activity implements CompoundButton.OnCheckedChangeListener {
    private static final String TAG = "DemoDisplayAoaVideoActi";
    private static final float rate = 1.3333334f;
    private long createTime;
    /* access modifiers changed from: private */
    public int lastMainHeight;
    /* access modifiers changed from: private */
    public int lastMainWidth;
    /* access modifiers changed from: private */
    public int lastSecondHeight;
    /* access modifiers changed from: private */
    public int lastSecondWidth;
    /* access modifiers changed from: private */
    public DJIVideoDecoder mainDecoder;
    /* access modifiers changed from: private */
    public SurfaceView mainDisplaySv;
    /* access modifiers changed from: private */
    public VideoStreamParseController mainParseController;
    /* access modifiers changed from: private */
    public SurfaceInterface mainRenderManager;
    /* access modifiers changed from: private */
    public DJIVideoDecoder secondDecoder;
    /* access modifiers changed from: private */
    public SurfaceView secondDisplaySv;
    private ToggleButton secondDisplaySw;
    /* access modifiers changed from: private */
    public VideoStreamParseController secondParseController;
    /* access modifiers changed from: private */
    public SurfaceInterface secondRenderManager;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo_display_aoa_video);
        this.createTime = System.currentTimeMillis();
        this.mainDisplaySv = (SurfaceView) findViewById(R.id.aoa_display_main_display_view);
        this.secondDisplaySv = (SurfaceView) findViewById(R.id.aoa_display_second_display_view);
        this.secondDisplaySw = (ToggleButton) findViewById(R.id.aoa_display_second_display_sw);
        this.secondDisplaySw.setOnCheckedChangeListener(this);
        this.secondDisplaySv.setZOrderOnTop(true);
        initDecoding();
    }

    private void initDecoding() {
        this.mainDisplaySv.getHolder().addCallback(new SurfaceHolder.Callback() {
            /* class com.dji.video.framing.demo.DemoDisplayAoaVideoActivity.AnonymousClass1 */

            public void surfaceCreated(SurfaceHolder holder) {
                DJIVideoDecoder unused = DemoDisplayAoaVideoActivity.this.mainDecoder = new DJIVideoDecoder(DemoDisplayAoaVideoActivity.this, DemoDisplayAoaVideoActivity.this.mainRenderManager);
                VideoStreamParseController unused2 = DemoDisplayAoaVideoActivity.this.mainParseController = new VideoStreamParseController(VideoStreamParseController.OutputMode.Directly, 1200, BackgroundLooper.getLooper());
                DemoDisplayAoaVideoActivity.this.mainParseController.setOutputCallback(new VideoStreamParseController.FrameDataOutputCallback() {
                    /* class com.dji.video.framing.demo.DemoDisplayAoaVideoActivity.AnonymousClass1.AnonymousClass1 */

                    public boolean onFrameOutput(VideoFrame frame) {
                        DemoDisplayAoaVideoActivity.this.mainDecoder.queueInputBuffer(frame);
                        if (!(frame.width == DemoDisplayAoaVideoActivity.this.lastMainWidth && frame.height == DemoDisplayAoaVideoActivity.this.lastMainHeight)) {
                            DemoDisplayAoaVideoActivity.this.resizeSurfaceView(DemoDisplayAoaVideoActivity.this.mainDisplaySv, frame.width, frame.height);
                        }
                        int unused = DemoDisplayAoaVideoActivity.this.lastMainWidth = frame.width;
                        int unused2 = DemoDisplayAoaVideoActivity.this.lastMainHeight = frame.height;
                        return false;
                    }
                });
            }

            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
                DemoDisplayAoaVideoActivity.this.mainRenderManager.reSizeSurface(width, height, 0, 0);
            }

            public void surfaceDestroyed(SurfaceHolder holder) {
                DemoDisplayAoaVideoActivity.this.mainDecoder.release();
                DemoDisplayAoaVideoActivity.this.mainParseController.destroy();
            }
        });
        this.secondDisplaySv.getHolder().addCallback(new SurfaceHolder.Callback() {
            /* class com.dji.video.framing.demo.DemoDisplayAoaVideoActivity.AnonymousClass2 */

            public void surfaceCreated(SurfaceHolder holder) {
                DJIVideoDecoder unused = DemoDisplayAoaVideoActivity.this.secondDecoder = new DJIVideoDecoder(DemoDisplayAoaVideoActivity.this, DemoDisplayAoaVideoActivity.this.secondRenderManager);
                VideoStreamParseController unused2 = DemoDisplayAoaVideoActivity.this.secondParseController = new VideoStreamParseController(VideoStreamParseController.OutputMode.Directly, 1200, BackgroundLooper.getLooper());
                DemoDisplayAoaVideoActivity.this.secondParseController.setOutputCallback(new VideoStreamParseController.FrameDataOutputCallback() {
                    /* class com.dji.video.framing.demo.DemoDisplayAoaVideoActivity.AnonymousClass2.AnonymousClass1 */

                    public boolean onFrameOutput(VideoFrame frame) {
                        Log.d(DemoDisplayAoaVideoActivity.TAG, "onFrameOutput: size=" + frame.size + " w=" + frame.width + " h=" + frame.height + " num=" + frame.frameNum + " iskey=" + frame.isKeyFrame + " idx=" + frame.frameIndex);
                        DemoDisplayAoaVideoActivity.this.secondDecoder.queueInputBuffer(frame);
                        if (!(frame.width == DemoDisplayAoaVideoActivity.this.lastSecondWidth && frame.height == DemoDisplayAoaVideoActivity.this.lastSecondHeight)) {
                            DemoDisplayAoaVideoActivity.this.resizeSurfaceView(DemoDisplayAoaVideoActivity.this.secondDisplaySv, frame.width, frame.height);
                        }
                        int unused = DemoDisplayAoaVideoActivity.this.lastSecondWidth = frame.width;
                        int unused2 = DemoDisplayAoaVideoActivity.this.lastSecondHeight = frame.height;
                        return false;
                    }
                });
            }

            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
                DemoDisplayAoaVideoActivity.this.secondRenderManager.reSizeSurface(width, height, 0, 0);
            }

            public void surfaceDestroyed(SurfaceHolder holder) {
                DemoDisplayAoaVideoActivity.this.secondDecoder.release();
                DemoDisplayAoaVideoActivity.this.secondParseController.destroy();
            }
        });
    }

    /* access modifiers changed from: private */
    public void resizeSurfaceView(final SurfaceView sv, final int videoWidth, final int videoHeight) {
        runOnUiThread(new Runnable() {
            /* class com.dji.video.framing.demo.DemoDisplayAoaVideoActivity.AnonymousClass3 */

            public void run() {
                RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) sv.getLayoutParams();
                lp.height = (sv.getWidth() * videoHeight) / videoWidth;
                sv.setLayoutParams(lp);
            }
        });
    }

    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (buttonView.getId() != R.id.aoa_display_second_display_sw) {
            return;
        }
        if (isChecked) {
            this.secondDisplaySv.setVisibility(0);
        } else {
            this.secondDisplaySv.setVisibility(4);
        }
    }
}
