package dji.midware.media.colors;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.os.Build;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.support.annotation.Keep;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ImageView;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.manager.P3.ServiceManager;
import dji.midware.media.DJIVideoDecoder;
import dji.midware.media.colors.OscilloscopeLinesDrawer;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;
import org.greenrobot.eventbus.EventBus;

@Keep
@EXClassNullAway
public class ColorOscilloscopeDisplayView extends ImageView {
    private static final int BITMAP_MAX_HEIGHT = 360;
    private static final int BITMAP_POOL_SIZE = 3;
    private static final boolean DEBUG = false;
    private static final int DRAWERS_COLUMN_SAMPLE_INTERVAL = 2;
    private static final int DRAWERS_ROW_SAMPLE_INTERVAL = 2;
    private static final float DRAW_AREA_HORIZONTAL_RATIO = 1.0f;
    private static final float DRAW_AREA_VERTICAL_RATIO = 1.0f;
    private static final int MSG_FETCH_RGBA_DATA = 0;
    private static final int MSG_GENERATE_BITMAP = 1;
    private static final int MSG_SWITCH_COLOR_EXP = 2;
    private static final int SAMPLE_MAX_HEIGHT = 96;
    private static final String TAG = "ColorDisplayView";
    private static final int UPDATE_MIN_INTERVAL = 0;
    /* access modifiers changed from: private */
    public BitmapPool bitmapPool;
    /* access modifiers changed from: private */
    public BitmapWrapper curBitmapWrapper;
    private byte[] data;
    private OscilloscopeDisplayMode displayMode = OscilloscopeDisplayMode.Mix;
    private List<OscilloscopeDrawer> drawerList = new LinkedList();
    private OscilloscopeDrawer expDrawer;
    /* access modifiers changed from: private */
    public int horizontalLineColor = Integer.MAX_VALUE;
    /* access modifiers changed from: private */
    public int horizontalLineNum = 5;
    /* access modifiers changed from: private */
    public float horizontalLineWidth = 1.0f;
    /* access modifiers changed from: private */
    public boolean isExpOsci = true;
    /* access modifiers changed from: private */
    public boolean isRunning = false;
    /* access modifiers changed from: private */
    public long lastDrawDuration = -1;
    private Handler oscilloscopeHandler;
    private HandlerThread oscilloscopeThread;
    private OscilloscopeDrawer[] rgbDrawers;
    private int videoHeight;
    private int videoWidth;

    @Keep
    public enum OscilloscopeDisplayEvent {
        Open,
        Close,
        SwitchToExp,
        SwitchToColor
    }

    @Keep
    public enum OscilloscopeDisplayMode {
        Mix,
        Separate
    }

    public interface OscilloscopeDrawer {
        void draw(Canvas canvas, float f, byte[] bArr, int i, int i2, float f2, float f3, float f4, float f5);

        void init();

        void unInit();
    }

    /* access modifiers changed from: private */
    public static void logd(String tag, String log) {
    }

    public ColorOscilloscopeDisplayView(Context context) {
        super(context);
    }

    public ColorOscilloscopeDisplayView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ColorOscilloscopeDisplayView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Keep
    static class BitmapWrapper {
        private Bitmap bitmap;
        private Canvas canvas;

        public BitmapWrapper(Bitmap bitmap2) {
            this.bitmap = bitmap2;
            this.canvas = new Canvas(bitmap2);
        }

        public Bitmap getBitmap() {
            return this.bitmap;
        }

        public Canvas getCanvas() {
            return this.canvas;
        }
    }

    @Keep
    static class BitmapPool {
        private BlockingQueue<BitmapWrapper> bitmapWrapperQueue;
        private int height;
        private List<BitmapWrapper> outQueue;
        private int width;

        BitmapPool() {
        }

        public synchronized void init(int width2, int height2, int size) {
            this.bitmapWrapperQueue = new ArrayBlockingQueue(size);
            this.outQueue = new ArrayList(size);
            this.width = width2;
            this.height = height2;
            for (int i = 0; i < size; i++) {
                if (!this.bitmapWrapperQueue.offer(new BitmapWrapper(Bitmap.createBitmap(width2, height2, Bitmap.Config.ARGB_8888)))) {
                    Log.e(ColorOscilloscopeDisplayView.TAG, "BitmapPool: constructor can't offer to queue: size: " + this.bitmapWrapperQueue.size());
                }
            }
        }

        public int getWidth() {
            return this.width;
        }

        public int getHeight() {
            return this.height;
        }

        public synchronized BitmapWrapper getBitmap(long timeoutInMs) {
            BitmapWrapper bitmapWrapper;
            if (this.bitmapWrapperQueue == null) {
                bitmapWrapper = null;
            } else {
                try {
                    bitmapWrapper = this.bitmapWrapperQueue.poll(timeoutInMs, TimeUnit.MILLISECONDS);
                    if (bitmapWrapper != null) {
                        this.outQueue.add(bitmapWrapper);
                    } else {
                        bitmapWrapper = null;
                    }
                } catch (InterruptedException e) {
                    Log.e(ColorOscilloscopeDisplayView.TAG, "bitmap wrapper pool getBitmap error: ", e);
                    bitmapWrapper = null;
                }
            }
            return bitmapWrapper;
        }

        public synchronized boolean isFromBitmapPool(BitmapWrapper bitmapWrapper) {
            boolean z = false;
            synchronized (this) {
                if (!(this.outQueue == null || bitmapWrapper == null)) {
                    z = this.outQueue.contains(bitmapWrapper);
                }
            }
            return z;
        }

        public synchronized boolean isFromBitmapPool(Bitmap bitmap) {
            boolean z = false;
            synchronized (this) {
                if (this.outQueue != null && bitmap != null) {
                    Iterator<BitmapWrapper> it2 = this.outQueue.iterator();
                    while (true) {
                        if (it2.hasNext()) {
                            if (bitmap.equals(it2.next().getBitmap())) {
                                z = true;
                                break;
                            }
                        } else {
                            break;
                        }
                    }
                }
            }
            return z;
        }

        public synchronized boolean releaseBitmap(BitmapWrapper bitmapWapper) {
            boolean z = false;
            synchronized (this) {
                if (this.bitmapWrapperQueue == null) {
                    _releaseBitmap(bitmapWapper);
                } else if (bitmapWapper != null) {
                    if (!this.outQueue.contains(bitmapWapper)) {
                        _releaseBitmap(bitmapWapper);
                    } else if (this.bitmapWrapperQueue.offer(bitmapWapper)) {
                        this.outQueue.remove(bitmapWapper);
                        z = true;
                    } else {
                        _releaseBitmap(bitmapWapper);
                    }
                }
            }
            return z;
        }

        private void _releaseBitmap(BitmapWrapper bitmapWrapper) {
            if (bitmapWrapper != null && bitmapWrapper.getBitmap() != null && !bitmapWrapper.getBitmap().isRecycled()) {
                bitmapWrapper.getBitmap().recycle();
            }
        }

        public synchronized void release() {
            if (this.bitmapWrapperQueue != null) {
                for (BitmapWrapper bitmapWrapper : this.bitmapWrapperQueue) {
                    _releaseBitmap(bitmapWrapper);
                }
            }
            this.bitmapWrapperQueue = null;
            this.outQueue = null;
        }
    }

    public void setIsExpOsci(boolean isExp, boolean needForcePost) {
        int i;
        int i2 = 1;
        if (this.oscilloscopeHandler.hasMessages(2)) {
            this.oscilloscopeHandler.removeMessages(2);
        }
        Handler handler = this.oscilloscopeHandler;
        Handler handler2 = this.oscilloscopeHandler;
        if (isExp) {
            i = 1;
        } else {
            i = 0;
        }
        if (!needForcePost) {
            i2 = 0;
        }
        handler.sendMessage(handler2.obtainMessage(2, i, i2));
    }

    public void setIsExpOsci(boolean isExp) {
        setIsExpOsci(isExp, false);
    }

    public OscilloscopeDisplayMode getDisplayMode() {
        return this.displayMode;
    }

    public void setDisplayMode(OscilloscopeDisplayMode displayMode2) {
        this.displayMode = displayMode2;
    }

    public int getHorizontalLineNum() {
        return this.horizontalLineNum;
    }

    public void setHorizontalLineNum(int horizontalLineNum2) {
        this.horizontalLineNum = horizontalLineNum2;
    }

    public int getHorizontalLineColor() {
        return this.horizontalLineColor;
    }

    public void setHorizontalLineColor(int horizontalLineColor2) {
        this.horizontalLineColor = horizontalLineColor2;
    }

    public float getHorizontalLineWidth() {
        return this.horizontalLineWidth;
    }

    public void setHorizontalLineWidth(float horizontalLineWidth2) {
        this.horizontalLineWidth = horizontalLineWidth2;
    }

    /* access modifiers changed from: private */
    public void recycleBitmapWrapper(BitmapWrapper bitmapWrapper) {
        if (bitmapWrapper != null) {
            logd(TAG, "recycleBitmapWrapper: 0");
            if (this.bitmapPool == null || !this.bitmapPool.isFromBitmapPool(bitmapWrapper)) {
                logd(TAG, "recycleBitmapWrapper: not bitmap pool");
                if (!bitmapWrapper.getBitmap().isRecycled()) {
                    bitmapWrapper.getBitmap().recycle();
                    return;
                }
                return;
            }
            logd(TAG, "recycleBitmapWrapper: bitmap pool recycle");
            this.bitmapPool.releaseBitmap(bitmapWrapper);
        }
    }

    private void startOscilloscopeThread() {
        stopOscilloscopeThread();
        this.oscilloscopeThread = new HandlerThread("Oscilloscope_Thread");
        this.oscilloscopeThread.start();
        this.oscilloscopeHandler = new Handler(this.oscilloscopeThread.getLooper()) {
            /* class dji.midware.media.colors.ColorOscilloscopeDisplayView.AnonymousClass1 */

            public void handleMessage(Message msg) {
                OscilloscopeDisplayEvent oscilloscopeDisplayEvent;
                long access$800;
                byte[] bArray;
                switch (msg.what) {
                    case 0:
                        ColorOscilloscopeDisplayView.logd(ColorOscilloscopeDisplayView.TAG, "handleMessage: width: " + ColorOscilloscopeDisplayView.this.getWidth() + ", height: " + ColorOscilloscopeDisplayView.this.getHeight());
                        DJIVideoDecoder decoder = ServiceManager.getInstance().getDecoder();
                        if (ColorOscilloscopeDisplayView.this.isRunning && decoder != null && ServiceManager.getInstance().isDecoderOK() && ColorOscilloscopeDisplayView.this.getWidth() > 0 && ColorOscilloscopeDisplayView.this.getHeight() > 0) {
                            int width = ColorOscilloscopeDisplayView.this.getWidth() / 4;
                            int height = ColorOscilloscopeDisplayView.this.getHeight() / 4;
                            if (height > 96) {
                                float ratio = ((float) width) / ((float) height);
                                height = 96;
                                width = (int) (((float) 96) * ratio);
                            }
                            if (ColorOscilloscopeDisplayView.this.isExpOsci) {
                                bArray = decoder.getYuvData(width, height);
                            } else {
                                bArray = decoder.getRgbaData(width, height);
                            }
                            if (bArray == null || bArray.length <= 0) {
                                sendEmptyMessageDelayed(0, 1000);
                                Log.e(ColorOscilloscopeDisplayView.TAG, "handleMessage: get rgba data failed");
                                return;
                            }
                            ColorOscilloscopeDisplayView.this.setData(bArray, width, height);
                            return;
                        } else if (ColorOscilloscopeDisplayView.this.isRunning) {
                            sendEmptyMessageDelayed(0, 1000);
                            return;
                        } else {
                            return;
                        }
                    case 1:
                        try {
                            long tStartSetData = System.currentTimeMillis();
                            int bmWidth = 5760 / 9;
                            if (ColorOscilloscopeDisplayView.this.bitmapPool == null) {
                                BitmapPool unused = ColorOscilloscopeDisplayView.this.bitmapPool = new BitmapPool();
                                ColorOscilloscopeDisplayView.this.bitmapPool.init(bmWidth, ColorOscilloscopeDisplayView.BITMAP_MAX_HEIGHT, 3);
                            }
                            final BitmapWrapper bitmapWrapper = ColorOscilloscopeDisplayView.this.generateOscilloscopeDisplay(bmWidth, ColorOscilloscopeDisplayView.BITMAP_MAX_HEIGHT, ColorOscilloscopeDisplayView.this.horizontalLineNum, ColorOscilloscopeDisplayView.this.horizontalLineColor, ColorOscilloscopeDisplayView.this.horizontalLineWidth);
                            if (bitmapWrapper != null) {
                                long unused2 = ColorOscilloscopeDisplayView.this.lastDrawDuration = System.currentTimeMillis() - tStartSetData;
                                ColorOscilloscopeDisplayView.this.post(new Runnable() {
                                    /* class dji.midware.media.colors.ColorOscilloscopeDisplayView.AnonymousClass1.AnonymousClass1 */

                                    public void run() {
                                        ColorOscilloscopeDisplayView.this.setImageBitmap(bitmapWrapper.getBitmap());
                                        ColorOscilloscopeDisplayView.logd(ColorOscilloscopeDisplayView.TAG, "run: set data time : " + ColorOscilloscopeDisplayView.this.lastDrawDuration);
                                        if (ColorOscilloscopeDisplayView.this.curBitmapWrapper != null) {
                                            ColorOscilloscopeDisplayView.this.recycleBitmapWrapper(ColorOscilloscopeDisplayView.this.curBitmapWrapper);
                                        }
                                        BitmapWrapper unused = ColorOscilloscopeDisplayView.this.curBitmapWrapper = bitmapWrapper;
                                    }
                                });
                                if (ColorOscilloscopeDisplayView.this.isRunning && !hasMessages(0)) {
                                    ColorOscilloscopeDisplayView.logd(ColorOscilloscopeDisplayView.TAG, "handleMessage: interval: " + (ColorOscilloscopeDisplayView.this.lastDrawDuration > 0 ? 0 : 0 - ColorOscilloscopeDisplayView.this.lastDrawDuration));
                                    if (ColorOscilloscopeDisplayView.this.lastDrawDuration > 0) {
                                        access$800 = 0;
                                    } else {
                                        access$800 = 0 - ColorOscilloscopeDisplayView.this.lastDrawDuration;
                                    }
                                    sendEmptyMessageDelayed(0, access$800);
                                    return;
                                }
                                return;
                            }
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        if (ColorOscilloscopeDisplayView.this.isRunning && !hasMessages(0)) {
                            sendEmptyMessageDelayed(0, 0);
                            return;
                        }
                        return;
                    case 2:
                        boolean isExp = msg.arg1 == 1;
                        boolean needPost = msg.arg2 == 1;
                        if (ColorOscilloscopeDisplayView.this.isExpOsci != isExp) {
                            boolean unused3 = ColorOscilloscopeDisplayView.this.isExpOsci = isExp;
                            ColorOscilloscopeUtils.setOscilloscopeIsExpToSp(isExp);
                            ColorOscilloscopeDisplayView.this.initDrawers();
                            EventBus eventBus = EventBus.getDefault();
                            if (isExp) {
                                oscilloscopeDisplayEvent = OscilloscopeDisplayEvent.SwitchToExp;
                            } else {
                                oscilloscopeDisplayEvent = OscilloscopeDisplayEvent.SwitchToColor;
                            }
                            eventBus.post(oscilloscopeDisplayEvent);
                            return;
                        } else if (needPost) {
                            EventBus.getDefault().post(isExp ? OscilloscopeDisplayEvent.SwitchToExp : OscilloscopeDisplayEvent.SwitchToColor);
                            return;
                        } else {
                            return;
                        }
                    default:
                        return;
                }
            }
        };
    }

    public void startUpdate() {
        this.isRunning = true;
        if (this.oscilloscopeHandler == null) {
            startOscilloscopeThread();
        }
        this.oscilloscopeHandler.sendEmptyMessage(0);
        EventBus.getDefault().post(this.isExpOsci ? OscilloscopeDisplayEvent.SwitchToExp : OscilloscopeDisplayEvent.SwitchToColor);
    }

    public void stopUpdate() {
        this.isRunning = false;
    }

    private void stopOscilloscopeThread() {
        if (this.oscilloscopeHandler != null) {
            this.oscilloscopeHandler.removeCallbacksAndMessages(null);
            this.oscilloscopeHandler = null;
        }
        if (this.oscilloscopeThread != null) {
            if (Build.VERSION.SDK_INT >= 18) {
                this.oscilloscopeThread.quitSafely();
            } else {
                this.oscilloscopeThread.quit();
            }
            this.oscilloscopeThread = null;
        }
    }

    public void addDrawer(OscilloscopeDrawer drawer) {
        this.drawerList.add(drawer);
    }

    public void addDrawers(OscilloscopeDrawer[] drawers) {
        this.drawerList.addAll(Arrays.asList(drawers));
    }

    public void removeDrawer(OscilloscopeDrawer drawer) {
        this.drawerList.remove(drawer);
    }

    public void clearDrawers() {
        this.drawerList.clear();
    }

    public List<OscilloscopeDrawer> getDrawerList() {
        return this.drawerList;
    }

    public void initDrawers() {
        if (this.expDrawer == null) {
            this.expDrawer = new OscilloscopeLinesDrawer(OscilloscopeLinesDrawer.ColorChannel.Exp, 352321535);
            this.expDrawer.init();
        }
        if (this.rgbDrawers == null) {
            this.rgbDrawers = new OscilloscopeDrawer[]{new OscilloscopeLinesDrawer(OscilloscopeLinesDrawer.ColorChannel.R, 687800320, 2, 2), new OscilloscopeLinesDrawer(OscilloscopeLinesDrawer.ColorChannel.G, 671153920, 2, 2), new OscilloscopeLinesDrawer(OscilloscopeLinesDrawer.ColorChannel.B, 671088895, 2, 2)};
            for (OscilloscopeDrawer rgbDrawer : this.rgbDrawers) {
                rgbDrawer.init();
            }
        }
        clearDrawers();
        if (this.isExpOsci) {
            addDrawer(this.expDrawer);
        } else {
            addDrawers(this.rgbDrawers);
        }
    }

    public void unInitDrawers() {
        if (this.expDrawer != null) {
            this.expDrawer.unInit();
            this.expDrawer = null;
        }
        if (this.rgbDrawers != null) {
            for (OscilloscopeDrawer rgbDrawer : this.rgbDrawers) {
                rgbDrawer.unInit();
            }
            this.rgbDrawers = null;
        }
        clearDrawers();
    }

    public void setData(byte[] data2, int width, int height) {
        this.data = data2;
        this.videoWidth = width;
        this.videoHeight = height;
        this.oscilloscopeHandler.sendEmptyMessage(1);
    }

    /* access modifiers changed from: private */
    public BitmapWrapper generateOscilloscopeDisplay(int width, int height, int horLineNum, int horLineColor, float horLineWidth) throws InterruptedException {
        BitmapWrapper bitmapWrapper;
        if (width <= 0 || height <= 0) {
            return null;
        }
        if (this.bitmapPool != null && this.bitmapPool.getWidth() == width && this.bitmapPool.getHeight() == height) {
            logd(TAG, "get bitmap from bitmap pool");
            bitmapWrapper = this.bitmapPool.getBitmap(200);
            if (bitmapWrapper != null) {
                logd(TAG, "get bitmap from bitmap pool success");
            } else {
                logd(TAG, "get bitmap from bitmap pool failed");
                bitmapWrapper = new BitmapWrapper(Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888));
            }
        } else {
            logd(TAG, "create new bitmap: width: " + width + ", height: " + height);
            bitmapWrapper = new BitmapWrapper(Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888));
        }
        Canvas canvas = bitmapWrapper.getCanvas();
        Paint clearPaint = new Paint();
        clearPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
        canvas.drawPaint(clearPaint);
        canvas.drawARGB(25, 255, 255, 255);
        float left = 0.0f * ((float) width);
        float right = ((float) width) * 1.0f;
        float top = 0.0f * ((float) height);
        float bottom = ((float) height) * 1.0f;
        float lineYInterval = ((bottom - 0.0f) - 0.0f) / ((float) (horLineNum - 1));
        Paint linePaint = new Paint();
        linePaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_OVER));
        linePaint.setStrokeWidth(horLineWidth);
        linePaint.setColor(horLineColor);
        for (int i = 0; i < horLineNum; i++) {
            float lineY = 0.0f + (((float) i) * lineYInterval);
            canvas.drawLine(left, lineY, right, lineY, linePaint);
        }
        float paintWidth = ((float) height) / 205.0f;
        if (this.data == null) {
            return bitmapWrapper;
        }
        logd(TAG, "generateOscilloscopeDisplay: drawer num: " + this.drawerList.size() + ", width: " + width + ", height: " + height + ", video width: " + this.videoWidth + ", video height: " + this.videoHeight + ", paint width: " + paintWidth);
        switch (getDisplayMode()) {
            case Mix:
                for (OscilloscopeDrawer drawer : this.drawerList) {
                    drawer.draw(canvas, paintWidth, this.data, this.videoWidth, this.videoHeight, left, top, right, bottom);
                }
                return bitmapWrapper;
            case Separate:
                int drawerNum = this.drawerList.size();
                if (drawerNum <= 0) {
                    return bitmapWrapper;
                }
                float horInterval = (right - left) / ((float) drawerNum);
                for (int i2 = 0; i2 < drawerNum; i2++) {
                    this.drawerList.get(i2).draw(canvas, paintWidth, this.data, this.videoWidth, this.videoHeight, left + (((float) i2) * horInterval), top, left + (((float) (i2 + 1)) * horInterval), bottom);
                }
                return bitmapWrapper;
            default:
                return bitmapWrapper;
        }
    }

    /* access modifiers changed from: protected */
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (this.drawerList.size() == 0) {
            initDrawers();
        }
        startOscilloscopeThread();
        if (getVisibility() == 0) {
            startUpdate();
        }
    }

    /* access modifiers changed from: protected */
    public void onDetachedFromWindow() {
        stopUpdate();
        stopOscilloscopeThread();
        unInitDrawers();
        if (this.bitmapPool != null) {
            this.bitmapPool.release();
            this.bitmapPool = null;
        }
        super.onDetachedFromWindow();
    }

    public void setVisibility(int visibility) {
        logd(TAG, "setVisibility: " + visibility);
        super.setVisibility(visibility);
        if (visibility == 0) {
            startUpdate();
        } else {
            stopUpdate();
        }
    }

    /* JADX DEBUG: Failed to find minimal casts for resolve overloaded methods, cast all args instead
     method: ClspMth{java.io.FileWriter.<init>(java.io.File, boolean):void throws java.io.IOException}
     arg types: [java.io.File, int]
     candidates:
      ClspMth{java.io.FileWriter.<init>(java.lang.String, boolean):void throws java.io.IOException}
      ClspMth{java.io.FileWriter.<init>(java.io.File, boolean):void throws java.io.IOException} */
    public void logFile(String logPath, String log) {
        File logFile = new File(logPath);
        try {
            if (!logFile.exists() || !logFile.isFile()) {
                logFile.createNewFile();
            }
            BufferedWriter bw = new BufferedWriter(new FileWriter(logFile, true));
            bw.write(log);
            bw.flush();
            bw.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e2) {
            e2.printStackTrace();
        }
    }

    public boolean isExpOsciDisplaying() {
        return this.isExpOsci;
    }
}
