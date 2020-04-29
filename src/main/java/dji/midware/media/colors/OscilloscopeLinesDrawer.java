package dji.midware.media.colors;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.media.colors.ColorOscilloscopeDisplayView;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@EXClassNullAway
public class OscilloscopeLinesDrawer implements ColorOscilloscopeDisplayView.OscilloscopeDrawer {
    private static final String TAG = "OscilloscopeLinesDrawer";
    /* access modifiers changed from: private */
    public float bottom;
    /* access modifiers changed from: private */
    public Canvas canvas;
    /* access modifiers changed from: private */
    public ColorChannel colorChannel;
    /* access modifiers changed from: private */
    public int columnSameleInterval;
    /* access modifiers changed from: private */
    public byte[] data;
    /* access modifiers changed from: private */
    public float horInterval;
    /* access modifiers changed from: private */
    public float left;
    private int lineCount;
    /* access modifiers changed from: private */
    public Paint paint;
    private float right;
    /* access modifiers changed from: private */
    public CountDownLatch rowCdl;
    private int rowSampleInterval;
    private ExecutorService threadPool;
    private float top;
    /* access modifiers changed from: private */
    public float verInterval;
    private int videoHeight;
    /* access modifiers changed from: private */
    public int videoWidth;

    static /* synthetic */ int access$408(OscilloscopeLinesDrawer x0) {
        int i = x0.lineCount;
        x0.lineCount = i + 1;
        return i;
    }

    public enum ColorChannel {
        Exp(-1),
        R(0),
        G(1),
        B(2),
        A(3);
        
        private int value = 0;

        public int value() {
            return this.value;
        }

        private ColorChannel(int value2) {
            this.value = value2;
        }
    }

    public OscilloscopeLinesDrawer(ColorChannel colorChannel2, int color, int rowSampleInterval2, int columnSameleInterval2) {
        this.lineCount = 0;
        this.rowSampleInterval = 1;
        this.columnSameleInterval = 1;
        this.colorChannel = colorChannel2;
        this.paint = new Paint();
        this.rowSampleInterval = rowSampleInterval2;
        this.columnSameleInterval = columnSameleInterval2;
        this.paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.ADD));
        this.paint.setStrokeWidth(1.0f);
        this.paint.setColor(color);
        this.paint.setAntiAlias(false);
    }

    public void init() {
        if (this.threadPool != null) {
            shutdownThreadPoolExecutor();
        }
        this.threadPool = new ThreadPoolExecutor(7, 7, 1000, TimeUnit.MILLISECONDS, new LinkedBlockingQueue());
    }

    private void shutdownThreadPoolExecutor() {
        if (this.threadPool != null) {
            this.threadPool.shutdown();
            try {
                if (!this.threadPool.awaitTermination(1000, TimeUnit.MILLISECONDS)) {
                    this.threadPool.shutdownNow();
                }
            } catch (InterruptedException e) {
                this.threadPool.shutdownNow();
            }
            this.threadPool = null;
        }
    }

    public void unInit() {
        shutdownThreadPoolExecutor();
    }

    public OscilloscopeLinesDrawer(ColorChannel colorChannel2, int color) {
        this(colorChannel2, color, 1, 1);
    }

    public void draw(Canvas canvas2, float paintWidth, byte[] data2, int videoWidth2, int videoHeight2, float left2, float top2, float right2, float bottom2) {
        ExecutorService tempThreadPool = this.threadPool;
        if (tempThreadPool != null) {
            this.lineCount = 0;
            this.canvas = canvas2;
            this.data = data2;
            this.paint.setStrokeWidth(paintWidth);
            this.videoWidth = videoWidth2;
            this.videoHeight = videoHeight2;
            this.left = left2;
            this.top = top2;
            this.right = right2;
            this.bottom = bottom2;
            this.horInterval = (((float) this.columnSameleInterval) * (right2 - left2)) / ((float) videoWidth2);
            this.verInterval = (bottom2 - top2) / 256.0f;
            this.rowCdl = new CountDownLatch(videoHeight2 / this.rowSampleInterval);
            int i = 0;
            while (i < videoHeight2) {
                tempThreadPool.execute(new DrawRowTask(i));
                i += this.rowSampleInterval;
            }
            try {
                this.rowCdl.await(2, TimeUnit.SECONDS);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private class DrawRowTask implements Runnable {
        private int lastValue = -1;
        private int rowNum;

        public DrawRowTask(int rowNum2) {
            this.rowNum = rowNum2;
        }

        public void run() {
            this.lastValue = -1;
            int preposedPixelNum = this.rowNum * OscilloscopeLinesDrawer.this.videoWidth;
            for (int j = 0; j < OscilloscopeLinesDrawer.this.videoWidth / OscilloscopeLinesDrawer.this.columnSameleInterval; j++) {
                int value = (OscilloscopeLinesDrawer.this.colorChannel.value() < 0 ? OscilloscopeLinesDrawer.this.data[(OscilloscopeLinesDrawer.this.columnSameleInterval * j) + preposedPixelNum] : OscilloscopeLinesDrawer.this.data[(((OscilloscopeLinesDrawer.this.columnSameleInterval * j) + preposedPixelNum) * 4) + OscilloscopeLinesDrawer.this.colorChannel.value()]) & 255;
                if (this.lastValue >= 0) {
                    OscilloscopeLinesDrawer.access$408(OscilloscopeLinesDrawer.this);
                    OscilloscopeLinesDrawer.this.canvas.drawLine(OscilloscopeLinesDrawer.this.left + (OscilloscopeLinesDrawer.this.horInterval * ((float) j)), OscilloscopeLinesDrawer.this.bottom - (((float) this.lastValue) * OscilloscopeLinesDrawer.this.verInterval), OscilloscopeLinesDrawer.this.left + (OscilloscopeLinesDrawer.this.horInterval * ((float) (j + 1))), OscilloscopeLinesDrawer.this.bottom - (((float) value) * OscilloscopeLinesDrawer.this.verInterval), OscilloscopeLinesDrawer.this.paint);
                }
                this.lastValue = value;
            }
            if (OscilloscopeLinesDrawer.this.rowCdl != null) {
                OscilloscopeLinesDrawer.this.rowCdl.countDown();
            }
        }
    }
}
