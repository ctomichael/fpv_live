package com.dji.video.framing.demo;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.CompoundButton;
import android.widget.Switch;
import com.dji.video.framing.R;
import com.dji.video.framing.internal.VideoFrame;
import com.dji.video.framing.internal.decoder.DJIVideoDecoder;
import com.dji.video.framing.internal.opengl.surface.SurfaceInterface;
import com.dji.video.framing.internal.parser.IFrameParser;
import com.dji.video.framing.internal.parser.VideoStreamParseController;
import com.dji.video.framing.utils.BackgroundLooper;
import com.dji.video.framing.utils.VideoStreamFilePreviewer;
import java.io.File;
import java.io.IOException;

public class DemoDecodeDisplayActivity extends Activity implements CompoundButton.OnCheckedChangeListener {
    private static final String TAG = "DemoDecodeDisplayActivi";
    private Switch dlogLutSw;
    /* access modifiers changed from: private */
    public DJIVideoDecoder mainDecoder;
    private SurfaceView mainDisplaySv;
    /* access modifiers changed from: private */
    public VideoStreamParseController mainParseController;
    /* access modifiers changed from: private */
    public VideoStreamFilePreviewer mainPreviewer;
    /* access modifiers changed from: private */
    public SurfaceInterface mainRenderManager;
    /* access modifiers changed from: private */
    public File mainStreamSrc;
    private Switch overExpSw;
    private Switch peakingFocusSw;
    /* access modifiers changed from: private */
    public DJIVideoDecoder secondDecoder;
    /* access modifiers changed from: private */
    public SurfaceView secondDisplaySv;
    private Switch secondDisplaySw;
    /* access modifiers changed from: private */
    public VideoStreamParseController secondParseController;
    /* access modifiers changed from: private */
    public VideoStreamFilePreviewer secondPreviewer;
    /* access modifiers changed from: private */
    public File secondStreamSrc;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo_decode_display);
        this.secondDisplaySw = (Switch) findViewById(R.id.decode_display_second_display_sw);
        this.secondDisplaySw.setOnCheckedChangeListener(this);
        this.overExpSw = (Switch) findViewById(R.id.decode_display_over_exp_sw);
        this.overExpSw.setOnCheckedChangeListener(this);
        this.peakingFocusSw = (Switch) findViewById(R.id.decode_display_peaking_focus_sw);
        this.peakingFocusSw.setOnCheckedChangeListener(this);
        this.dlogLutSw = (Switch) findViewById(R.id.decode_display_dlog_lut_sw);
        this.dlogLutSw.setOnCheckedChangeListener(this);
        this.mainStreamSrc = new File(IFrameParser.MOCK_DJI_VIDEO_FILE_1080P);
        this.mainDisplaySv = (SurfaceView) findViewById(R.id.decode_display_main_display_view);
        this.mainDisplaySv.getHolder().addCallback(new SurfaceHolder.Callback() {
            /* class com.dji.video.framing.demo.DemoDecodeDisplayActivity.AnonymousClass1 */

            public void surfaceCreated(SurfaceHolder holder) {
                VideoStreamParseController unused = DemoDecodeDisplayActivity.this.mainParseController = new VideoStreamParseController(VideoStreamParseController.OutputMode.FpsFromSps, 1200, BackgroundLooper.getLooper());
                DemoDecodeDisplayActivity.this.mainParseController.setOutputCallback(new VideoStreamParseController.FrameDataOutputCallback() {
                    /* class com.dji.video.framing.demo.DemoDecodeDisplayActivity.AnonymousClass1.C00181 */

                    public boolean onFrameOutput(VideoFrame frame) {
                        DemoDecodeDisplayActivity.this.mainDecoder.queueInputBuffer(frame);
                        return false;
                    }
                });
                DJIVideoDecoder unused2 = DemoDecodeDisplayActivity.this.mainDecoder = new DJIVideoDecoder(DemoDecodeDisplayActivity.this, DemoDecodeDisplayActivity.this.mainRenderManager);
                if (DemoDecodeDisplayActivity.this.mainPreviewer == null) {
                    VideoStreamFilePreviewer unused3 = DemoDecodeDisplayActivity.this.mainPreviewer = new VideoStreamFilePreviewer();
                }
                new Thread(new Runnable() {
                    /* class com.dji.video.framing.demo.DemoDecodeDisplayActivity.AnonymousClass1.AnonymousClass2 */

                    public void run() {
                        try {
                            DemoDecodeDisplayActivity.this.mainPreviewer.runReadData(DemoDecodeDisplayActivity.this.mainStreamSrc, DemoDecodeDisplayActivity.this.mainParseController);
                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (InterruptedException e2) {
                            e2.printStackTrace();
                        }
                    }
                }).start();
            }

            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
                DemoDecodeDisplayActivity.this.mainRenderManager.reSizeSurface(width, height, 0, 0);
            }

            public void surfaceDestroyed(SurfaceHolder holder) {
                DemoDecodeDisplayActivity.this.mainPreviewer.stopReadData();
                DemoDecodeDisplayActivity.this.mainDecoder.release();
                DemoDecodeDisplayActivity.this.mainParseController.destroy();
            }
        });
        this.secondStreamSrc = new File(IFrameParser.MOCK_DJI_VIDEO_FILE_720P);
        this.secondDisplaySv = (SurfaceView) findViewById(R.id.decode_display_second_display_view);
        this.secondDisplaySv.getHolder().addCallback(new SurfaceHolder.Callback() {
            /* class com.dji.video.framing.demo.DemoDecodeDisplayActivity.AnonymousClass2 */

            public void surfaceCreated(SurfaceHolder holder) {
                VideoStreamParseController unused = DemoDecodeDisplayActivity.this.secondParseController = new VideoStreamParseController(VideoStreamParseController.OutputMode.FpsFromSps, 1200, BackgroundLooper.getLooper());
                DemoDecodeDisplayActivity.this.secondParseController.setOutputCallback(new VideoStreamParseController.FrameDataOutputCallback() {
                    /* class com.dji.video.framing.demo.DemoDecodeDisplayActivity.AnonymousClass2.AnonymousClass1 */

                    public boolean onFrameOutput(VideoFrame frame) {
                        Log.d(DemoDecodeDisplayActivity.TAG, "onFrameOutput: w=" + frame.width + " h=" + frame.height + " num=" + frame.frameNum + " idx=" + frame.frameIndex);
                        DemoDecodeDisplayActivity.this.secondDecoder.queueInputBuffer(frame);
                        return false;
                    }
                });
                DJIVideoDecoder unused2 = DemoDecodeDisplayActivity.this.secondDecoder = new DJIVideoDecoder(DemoDecodeDisplayActivity.this, DemoDecodeDisplayActivity.this.secondDisplaySv);
                if (DemoDecodeDisplayActivity.this.secondPreviewer == null) {
                    VideoStreamFilePreviewer unused3 = DemoDecodeDisplayActivity.this.secondPreviewer = new VideoStreamFilePreviewer();
                }
                new Thread(new Runnable() {
                    /* class com.dji.video.framing.demo.DemoDecodeDisplayActivity.AnonymousClass2.C00042 */

                    public void run() {
                        try {
                            DemoDecodeDisplayActivity.this.secondPreviewer.runReadData(DemoDecodeDisplayActivity.this.secondStreamSrc, DemoDecodeDisplayActivity.this.secondParseController);
                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (InterruptedException e2) {
                            e2.printStackTrace();
                        }
                    }
                }).start();
            }

            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            }

            public void surfaceDestroyed(SurfaceHolder holder) {
                DemoDecodeDisplayActivity.this.secondPreviewer.stopReadData();
                DemoDecodeDisplayActivity.this.secondDecoder.release();
                DemoDecodeDisplayActivity.this.secondParseController.destroy();
            }
        });
    }

    /* access modifiers changed from: protected */
    public void onResume() {
        super.onResume();
        if (this.mainDecoder != null) {
            this.mainDecoder.setPause(false);
        }
        if (this.secondDecoder != null) {
            this.secondDecoder.setPause(false);
        }
    }

    /* access modifiers changed from: protected */
    public void onPause() {
        if (this.mainDecoder != null) {
            this.mainDecoder.setPause(true);
        }
        if (this.secondDecoder != null) {
            this.secondDecoder.setPause(true);
        }
        super.onPause();
    }

    /* access modifiers changed from: protected */
    public void onDestroy() {
        super.onDestroy();
    }

    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        int id = buttonView.getId();
        if (id == R.id.decode_display_second_display_sw) {
            if (isChecked) {
                this.secondDisplaySv.setVisibility(0);
            } else {
                this.secondDisplaySv.setVisibility(4);
            }
        } else if (id == R.id.decode_display_over_exp_sw) {
            this.mainRenderManager.enableOverExposureWarning(isChecked, R.raw.overexposure);
        } else if (id == R.id.decode_display_peaking_focus_sw) {
            this.mainRenderManager.setPeakFocusEnable(isChecked);
            if (isChecked) {
                this.mainRenderManager.setPeakFocusThreshold(4.0f);
            }
        } else if (id == R.id.decode_display_dlog_lut_sw) {
            this.mainRenderManager.enableDlogRender(isChecked);
        }
    }
}
