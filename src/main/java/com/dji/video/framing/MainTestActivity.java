package com.dji.video.framing;

import android.app.Activity;
import android.os.Bundle;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import java.util.concurrent.CountDownLatch;

public class MainTestActivity extends Activity {
    private SurfaceView displayView;
    /* access modifiers changed from: private */
    public CountDownLatch displayViewInitCdl;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_test);
        this.displayView = (SurfaceView) findViewById(R.id.test_display_view);
        this.displayViewInitCdl = new CountDownLatch(1);
        this.displayView.getHolder().addCallback(new SurfaceHolder.Callback() {
            /* class com.dji.video.framing.MainTestActivity.AnonymousClass1 */

            public void surfaceCreated(SurfaceHolder holder) {
                MainTestActivity.this.displayViewInitCdl.countDown();
                CountDownLatch unused = MainTestActivity.this.displayViewInitCdl = null;
            }

            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            }

            public void surfaceDestroyed(SurfaceHolder holder) {
            }
        });
    }

    public SurfaceView getDisplayView() {
        if (this.displayViewInitCdl != null) {
            try {
                this.displayViewInitCdl.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return this.displayView;
    }
}
