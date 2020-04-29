package dji.midware.media.newframing;

import android.graphics.Bitmap;
import android.graphics.SurfaceTexture;
import android.opengl.GLES20;
import android.os.Build;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import android.view.Surface;
import android.view.SurfaceHolder;
import com.dji.video.framing.internal.decoder.common.FrameFovType;
import com.dji.video.framing.internal.decoder.decoderinterface.IDJIVideoDecoder;
import com.dji.video.framing.internal.opengl.GLContextMgr;
import com.dji.video.framing.internal.opengl.GLUtil;
import com.dji.video.framing.internal.opengl.extra.ExtraRenderThread;
import com.dji.video.framing.internal.opengl.extra.FrameBufferTexturePair;
import com.dji.video.framing.internal.opengl.renderer.GLFellowRender;
import com.dji.video.framing.internal.opengl.renderer.GLGrayRender;
import com.dji.video.framing.internal.opengl.renderer.GLIdentityRender;
import com.dji.video.framing.internal.opengl.renderer.GLLutRender;
import com.dji.video.framing.internal.opengl.renderer.GLRGB2YUVRender;
import com.dji.video.framing.internal.opengl.renderer.GLRenderBase;
import com.dji.video.framing.internal.opengl.renderer.OverExposureWarner;
import com.dji.video.framing.internal.opengl.renderer.PeakingFocusPresenter;
import com.dji.video.framing.internal.opengl.surface.CacheContainer;
import com.dji.video.framing.internal.opengl.surface.SurfaceInterface;
import dji.fieldAnnotation.EXClassNullAway;
import dji.log.DJILog;
import dji.log.DJILogHelper;
import dji.midware.R;
import dji.midware.data.config.P3.ProductType;
import dji.midware.data.manager.Dpad.DpadProductManager;
import dji.midware.data.manager.P3.DJIProductManager;
import dji.midware.data.model.P3.DataCameraGetPushStateInfo;
import dji.midware.media.DJIVideoUtil;
import dji.midware.media.MediaLogger;
import dji.midware.media.newframing.DJILiveviewRenderController;
import dji.midware.util.Affinity;
import dji.midware.util.BackgroundLooper;
import dji.midware.util.ContextUtil;
import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import org.greenrobot.eventbus.EventBus;

@EXClassNullAway
public class GLYUVSurface implements SurfaceInterface {
    private static final int CHECK_TIME = 2000;
    public static final boolean DEBUG = false;
    private static final boolean DEBUG_SYNC = false;
    /* access modifiers changed from: private */
    public static int DEFAULT_INTERVAL_TIMES = 0;
    public static int DEFAULT_SLICE_NUM = 4;
    public static final String TAG = "GLYUVSurface";
    public static final boolean VERBOSE = false;
    private GLFellowRender asyncRender = null;
    private final Object asyncRenderLock = new Object();
    private ExtraRenderThread asyncRenderThread;
    /* access modifiers changed from: private */
    public CacheContainer cacheContainer;
    private FrameBufferTexturePair[] cacheFbos;
    private int cacheNum = 4;
    /* access modifiers changed from: private */
    public GLIdentityRender cameraRenderer = null;
    /* access modifiers changed from: private */
    public GLIdentityRender cameraRendererWithoutCorrection = null;
    /* access modifiers changed from: private */
    public GLContextMgr ctxCur;
    /* access modifiers changed from: private */
    public GLContextMgr ctxPre;
    /* access modifiers changed from: private */
    public int curBufferHeight = -1;
    /* access modifiers changed from: private */
    public int curBufferWidth = -1;
    /* access modifiers changed from: private */
    public int curHeight = -1;
    /* access modifiers changed from: private */
    public int curWidth = -1;
    private int dataCount = 0;
    /* access modifiers changed from: private */
    public Runnable displayRunnable = new Runnable() {
        /* class dji.midware.media.newframing.GLYUVSurface.AnonymousClass3 */

        public void run() {
            long startTime = System.currentTimeMillis();
            if (GLYUVSurface.this.cacheContainer != null) {
                int size = GLYUVSurface.this.cacheContainer.getSize();
                if (size > 0) {
                    GLYUVSurface.this.drawDisplay((FrameBufferTexturePair) GLYUVSurface.this.cacheContainer.peak());
                }
                int delayTime = 5;
                if (GLYUVSurface.this.interval > 0) {
                    delayTime = (int) (((long) (GLYUVSurface.this.interval + ((GLYUVSurface.this.maxSize - size) * GLYUVSurface.this.extraTime))) - (System.currentTimeMillis() - startTime));
                }
                GLYUVSurface.this.renderHandler.postDelayed(GLYUVSurface.this.displayRunnable, (long) delayTime);
            }
        }
    };
    private byte[] dst;
    private boolean enableSmooth = true;
    private List<GLRenderBase> extraRenderList = new ArrayList(3);
    /* access modifiers changed from: private */
    public int extraTime = 3;
    private FrameBufferTexturePair fbo1 = null;
    private FrameBufferTexturePair fbo2 = null;
    private BlockingQueue<Runnable> frameAvailableTaskQueue = new LinkedBlockingQueue();
    private GetDataTask getRgbaDataTask = new GetDataTask();
    private GetDataTask getYuvDataTask = new GetDataTask();
    private GLGrayRender grayRender = null;
    private boolean hasInit = false;
    private boolean hasOutputSurface = false;
    private Surface inputSurface;
    /* access modifiers changed from: private */
    public SurfaceTexture inputSurfaceTexture;
    /* access modifiers changed from: private */
    public int inputTextureID = -1;
    private GLLutRender internalDlogMRender;
    private GLLutRender internalDlogRender;
    private OverExposureWarner internalOewRenderer = null;
    /* access modifiers changed from: private */
    public int interval = -1;
    private boolean isDistortion = true;
    private long lastCheckTime = -1;
    private long lastValidTime = -1;
    private Object listenerSync = new Object();
    /* access modifiers changed from: private */
    public float[] mSTMatrix = new float[16];
    /* access modifiers changed from: private */
    public int maxSize = ((this.cacheNum + 1) / 2);
    private boolean needDlog = false;
    private boolean needInputTexture;
    private boolean needResetExtraRenderList = true;
    private OverExposureWarner oewRenderer = null;
    /* access modifiers changed from: private */
    public Bitmap outputBitmap;
    private boolean overExposureWarner = false;
    private int overExposureWarnerTextureResID = R.raw.overexposure;
    private boolean peakFocusEnable = false;
    private float peakFocusThreshold = 2.7f;
    private PeakingFocusPresenter peakingFocusPresenter = null;
    private int preheight = 0;
    private int prewidth = 0;
    /* access modifiers changed from: private */
    public ByteBuffer readByteBuffer = null;
    /* access modifiers changed from: private */
    public FrameBufferTexturePair readCacheFbo = null;
    /* access modifiers changed from: private */
    public ReadPixelSeperatlyTask readPixelSeperatlyTask;
    /* access modifiers changed from: private */
    public Handler renderHandler;
    /* access modifiers changed from: private */
    public HandlerThread renderThread = null;
    /* access modifiers changed from: private */
    public GLRGB2YUVRender rgb2yuvRenderer = null;
    boolean running = true;
    private boolean saveFirstReadData = false;
    private long secondaryDrawCount = 0;
    private byte[] src;
    /* access modifiers changed from: private */
    public Object syncCreateST = new Object();
    private GLIdentityRender tempRender = null;
    private GLIdentityRender texture2DRenderer = null;
    /* access modifiers changed from: private */
    public IDJIVideoDecoder videoDecoder;
    private int viewRotateDegree;
    private Surface viewSurface;
    private int viewX;
    private int viewportHeight;
    private int viewportWidth;
    private GLRenderBase vrRenderer = null;

    interface ReadPixelResultListener {
        void onComplete(ByteBuffer byteBuffer);

        void onFailure();
    }

    public boolean isSupportSmooth() {
        ProductType productType = DJIProductManager.getInstance().getType();
        if (productType == ProductType.Mammoth || productType == ProductType.WM230 || productType == ProductType.WM240 || productType == ProductType.WM245) {
            return false;
        }
        return true;
    }

    /* access modifiers changed from: private */
    public SurfaceTexture getInputSurfaceTexture() {
        return this.inputSurfaceTexture;
    }

    /* access modifiers changed from: private */
    public void postFrameAvailebleTask(Runnable task) {
        if (this.renderHandler != null) {
            this.renderHandler.post(task);
        }
    }

    private void updateTexImage() {
        try {
            getInputSurfaceTexture().updateTexImage();
        } catch (Exception e) {
            DJILogHelper.getInstance().LOGE(TAG, "updateTexImage: " + e);
        }
        if (DJIVideoUtil.isDebug(false)) {
            DJILogHelper.getInstance().LOGI(TAG, "after updateTexImage() : time=" + System.currentTimeMillis());
        }
    }

    public void resizeSurface(int width, int height) {
        reSizeSurface(width, height, 0, 0);
    }

    public synchronized void reSizeSurface(int width, int height, int viewx, int rotateDegree) {
        this.viewportWidth = width;
        this.viewportHeight = height;
        this.viewRotateDegree = rotateDegree;
        this.viewX = viewx;
        MediaLogger.show("GLYUVSresizeSurface: width=" + width + " height=" + height);
    }

    public void init(Object surface, int viewWidth, int viewHeight) {
        init(surface, viewWidth, viewHeight, 0, 0, true, true);
    }

    public void init(Object window, int viewWidth, int viewHeight, int viewx, int rotateDegree) {
        init(window, viewWidth, viewHeight, viewx, rotateDegree, true, true);
    }

    public synchronized void init(Object surface, int viewWidth, int viewHeight, int vp_x, int rotateDegree, boolean needInputTexture2, boolean needCache) {
        if (this.hasInit) {
            destroy();
        }
        DJILogHelper.getInstance().LOGI(TAG, "viewWidth=" + viewWidth + " viewHeight=" + viewHeight);
        if (surface != null && (surface instanceof SurfaceHolder)) {
            this.viewSurface = ((SurfaceHolder) surface).getSurface();
        }
        this.needInputTexture = needInputTexture2;
        this.viewportWidth = viewWidth;
        this.viewportHeight = viewHeight;
        this.viewRotateDegree = rotateDegree;
        this.viewX = vp_x;
        this.hasInit = true;
        if (!isSupportSmooth()) {
            needCache = false;
        }
        this.enableSmooth = needCache;
        if (DpadProductManager.getInstance().isCrystalSky()) {
            this.enableSmooth = false;
        }
        DJILogHelper.getInstance().LOGI(TAG, String.format(Locale.US, "Thread %s calls GLRenderManager's init()", Thread.currentThread().getName()));
        if (surface == null) {
            this.hasOutputSurface = false;
        } else {
            this.hasOutputSurface = true;
            if (needInputTexture2) {
                this.ctxPre = new GLContextMgr();
                this.ctxPre.loadFromThread();
                this.ctxCur = new GLContextMgr();
                this.ctxCur.createContext();
                this.ctxCur.bindSurface(surface);
                this.ctxCur.attachToThread();
                this.cameraRenderer = new DistortionCorrectionRender(true);
                if (this.isDistortion) {
                    setAntiDistortionEnabled(true);
                } else {
                    setAntiDistortionEnabled(false);
                }
                this.cameraRenderer.init();
                this.cameraRendererWithoutCorrection = new GLIdentityRender(true);
                this.cameraRendererWithoutCorrection.init();
                this.tempRender = new GLIdentityRender(false);
                this.tempRender.init();
                this.texture2DRenderer = new GLIdentityRender(false);
                this.texture2DRenderer.init();
                this.asyncRender = new GLFellowRender(true, this.cameraRenderer);
                this.asyncRender.init();
                initReadFrameBuffer();
                if (this.enableSmooth) {
                    cacheInit();
                }
                DJILogHelper.getInstance().LOGI(TAG, " create renders done");
                this.inputTextureID = GLUtil.genTexture(36197, true);
                DJILogHelper.getInstance().LOGI(TAG, "gen inputSurfaceTexture() done");
                this.ctxCur.detachFromThread();
                this.ctxPre.attachToThread();
                this.running = true;
                this.renderThread = new HandlerThread("GLYUVSurfaceThread", -1);
                this.renderThread.start();
                Affinity.bindThreadToCpu(ContextUtil.getContext(), this.renderThread);
                Looper looper = this.renderThread.getLooper();
                synchronized (this.syncCreateST) {
                    this.renderHandler = new Handler(looper, new Handler.Callback() {
                        /* class dji.midware.media.newframing.GLYUVSurface.AnonymousClass1 */

                        public boolean handleMessage(Message msg) {
                            GLYUVSurface.this.onFrameAvailable(null);
                            return false;
                        }
                    });
                    this.renderHandler.post(new Runnable() {
                        /* class dji.midware.media.newframing.GLYUVSurface.AnonymousClass2 */

                        public void run() {
                            GLYUVSurface.this.createInputSurfaceTexture();
                            synchronized (GLYUVSurface.this.syncCreateST) {
                                GLYUVSurface.this.syncCreateST.notify();
                            }
                            GLYUVSurface.this.getInputSurfaceTexture().setOnFrameAvailableListener(GLYUVSurface.this);
                        }
                    });
                    try {
                        this.syncCreateST.wait();
                    } catch (InterruptedException e) {
                    }
                }
                if (this.enableSmooth) {
                    this.renderHandler.post(this.displayRunnable);
                }
                this.needResetExtraRenderList = true;
                DJILogHelper.getInstance().LOGI(TAG, "init GLYUVSurface successful!");
            }
        }
    }

    public void resetSurface(Object window) {
        if (window != null) {
            this.ctxPre.loadFromThread();
            this.ctxCur.attachToThread();
            this.ctxCur.resetSurface();
            this.ctxCur.bindSurface(window);
            this.ctxCur.detachFromThread();
            this.ctxPre.attachToThread();
        }
    }

    public Surface getInputSurface() {
        if (!this.needInputTexture) {
            return this.viewSurface;
        }
        if (this.inputSurface != null) {
            return this.inputSurface;
        }
        if (getInputSurfaceTexture() == null) {
            return null;
        }
        this.inputSurface = new Surface(getInputSurfaceTexture());
        return this.inputSurface;
    }

    public void setVideoDecoder(IDJIVideoDecoder decoder) {
        this.videoDecoder = decoder;
    }

    public void createInputSurfaceTexture() {
        this.inputSurfaceTexture = new SurfaceTexture(this.inputTextureID);
    }

    private void initExtraRenderList() {
        this.extraRenderList.clear();
        this.extraRenderList.add(this.cameraRenderer);
        if (!this.peakFocusEnable && !this.overExposureWarner && !this.needDlog) {
            this.extraRenderList.add(this.tempRender);
            this.extraRenderList.add(this.tempRender);
        }
        if (this.peakFocusEnable) {
            if (this.peakingFocusPresenter == null) {
                this.peakingFocusPresenter = new PeakingFocusPresenter(false);
                this.peakingFocusPresenter.setThreshold(this.peakFocusThreshold);
                this.peakingFocusPresenter.init();
            }
            this.extraRenderList.add(this.peakingFocusPresenter);
        }
        if (this.overExposureWarner) {
            if (this.oewRenderer == null) {
                this.oewRenderer = new OverExposureWarner(false, this.overExposureWarnerTextureResID);
                this.oewRenderer.init();
            }
            this.extraRenderList.add(this.oewRenderer);
        }
        if (!this.needDlog) {
            return;
        }
        if (isDlogM()) {
            if (this.internalDlogMRender == null) {
                this.internalDlogMRender = new GLLutRender(R.drawable.lut_dlog_m, false);
                this.internalDlogMRender.init();
            }
            this.extraRenderList.add(this.internalDlogMRender);
            return;
        }
        if (this.internalDlogRender == null) {
            this.internalDlogRender = new GLLutRender(R.drawable.lut, false);
            this.internalDlogRender.init();
        }
        this.extraRenderList.add(this.internalDlogRender);
    }

    private void display() {
        int width = this.videoDecoder.getVideoWidth();
        int height = this.videoDecoder.getVideoHeight();
        if (width < 100 || height < 100) {
            this.cameraRenderer.draw(this.inputTextureID, 36197, this.mSTMatrix, false, (float) this.viewRotateDegree, this.viewX, 0, this.viewportWidth, this.viewportHeight);
            if (this.enableSmooth) {
                this.ctxCur.swapBuffers();
                return;
            }
            return;
        }
        checkVideoSizeChange(width, height);
        this.inputSurfaceTexture.getTransformMatrix(this.mSTMatrix);
        if (this.needResetExtraRenderList) {
            initExtraRenderList();
            this.needResetExtraRenderList = false;
        }
        if (this.extraRenderList.size() == 1) {
            if (this.enableSmooth) {
                this.extraRenderList.get(0).draw(obtainCache(), this.inputTextureID, 36197, this.mSTMatrix, true, 0.0f, this.viewX, 0, width, height);
            } else {
                this.extraRenderList.get(0).draw(this.inputTextureID, 36197, this.mSTMatrix, false, (float) this.viewRotateDegree, this.viewX, 0, this.viewportWidth, this.viewportHeight);
            }
        } else if (this.extraRenderList.size() > 1) {
            for (int i = 0; i < this.extraRenderList.size(); i++) {
                GLRenderBase render = this.extraRenderList.get(i);
                if (i == 0) {
                    render.draw(getFbo1(), this.inputTextureID, 36197, this.mSTMatrix, false, 0.0f, this.viewX, 0, width, height);
                } else if (i == this.extraRenderList.size() - 1) {
                    if (i % 2 != 0) {
                        if (this.enableSmooth) {
                            render.draw(obtainCache(), getFbo1().texture, 3553, this.mSTMatrix, false, 0.0f, this.viewX, 0, width, height);
                        } else {
                            render.draw(getFbo1().texture, 3553, this.mSTMatrix, true, (float) this.viewRotateDegree, this.viewX, 0, this.viewportWidth, this.viewportHeight);
                        }
                    } else if (this.enableSmooth) {
                        render.draw(obtainCache(), getFbo2().texture, 3553, this.mSTMatrix, true, 0.0f, this.viewX, 0, width, height);
                    } else {
                        render.draw(getFbo2().texture, 3553, this.mSTMatrix, false, (float) this.viewRotateDegree, this.viewX, 0, this.viewportWidth, this.viewportHeight);
                    }
                } else if (i % 2 != 0) {
                    render.draw(getFbo2(), getFbo1().texture, 3553, this.mSTMatrix, false, 0.0f, this.viewX, 0, width, height);
                } else {
                    render.draw(getFbo1(), getFbo2().texture, 3553, this.mSTMatrix, false, 0.0f, this.viewX, 0, width, height);
                }
            }
        } else if (this.enableSmooth) {
            this.cameraRenderer.draw(obtainCache(), this.inputTextureID, 36197, this.mSTMatrix, true, 0.0f, this.viewX, 0, width, height);
        } else {
            this.cameraRenderer.draw(this.inputTextureID, 36197, this.mSTMatrix, false, (float) this.viewRotateDegree, this.viewX, 0, this.viewportWidth, this.viewportHeight);
        }
        if (DJIVideoUtil.isDebug(false)) {
            DJILogHelper.getInstance().LOGI(TAG, "after display() : time=" + System.currentTimeMillis());
        }
    }

    private void cacheInit() {
        this.cacheFbos = new FrameBufferTexturePair[this.cacheNum];
        for (int i = 0; i < this.cacheNum; i++) {
            this.cacheFbos[i] = new FrameBufferTexturePair();
        }
        this.cacheContainer = new CacheContainer(this.cacheFbos);
    }

    private void cacheUnit() {
        if (this.cacheFbos != null) {
            for (FrameBufferTexturePair fbo : this.cacheFbos) {
                fbo.destroy();
            }
            this.cacheContainer = null;
        }
    }

    public void checkVideoSizeChange(int w, int h) {
        if (this.curWidth != w || this.curHeight != h) {
            this.curWidth = w;
            this.curHeight = h;
            if (this.enableSmooth) {
                FrameBufferTexturePair[] frameBufferTexturePairArr = this.cacheFbos;
                for (FrameBufferTexturePair render : frameBufferTexturePairArr) {
                    render.destroy();
                    render.create(this.curWidth, this.curHeight);
                }
                this.readCacheFbo.destroy();
                this.readCacheFbo.create(this.curWidth, this.curHeight);
            }
            if (this.fbo1 != null) {
                this.fbo1.destroy();
                this.fbo1 = null;
            }
            if (this.fbo2 != null) {
                this.fbo2.destroy();
                this.fbo2 = null;
            }
        }
    }

    /* access modifiers changed from: private */
    public void drawDisplay(FrameBufferTexturePair fboCache) {
        this.ctxPre.loadFromThread();
        this.ctxCur.attachToThread();
        this.inputSurfaceTexture.getTransformMatrix(this.mSTMatrix);
        this.texture2DRenderer.draw(fboCache.texture, this.mSTMatrix, (float) this.viewRotateDegree, this.viewX, 0, this.viewportWidth, this.viewportHeight);
        this.ctxCur.swapBuffers();
        onReadPixel();
        this.ctxCur.detachFromThread();
        this.ctxPre.attachToThread();
    }

    private FrameBufferTexturePair obtainCache() {
        return (FrameBufferTexturePair) this.cacheContainer.obtain();
    }

    private void updateInterval() {
        long curTime = System.currentTimeMillis();
        this.dataCount++;
        if (this.lastCheckTime == -1) {
            this.lastCheckTime = curTime;
        } else if (curTime - this.lastCheckTime > 2000) {
            this.interval = (int) ((curTime - this.lastCheckTime) / ((long) (this.dataCount - 1)));
            if (this.interval > 40) {
                this.interval = -1;
            }
            this.lastCheckTime = -1;
            this.dataCount = 0;
        }
    }

    public void toGray() {
        if (this.renderHandler != null) {
            this.renderHandler.post(new GLYUVSurface$$Lambda$0(this));
        }
    }

    /* access modifiers changed from: package-private */
    public final /* synthetic */ void lambda$toGray$0$GLYUVSurface() {
        int width = this.videoDecoder.getVideoWidth();
        int height = this.videoDecoder.getVideoHeight();
        this.ctxPre.loadFromThread();
        this.ctxCur.attachToThread();
        this.inputSurfaceTexture.getTransformMatrix(this.mSTMatrix);
        this.cameraRenderer.draw(getFbo1(), this.inputTextureID, 36197, this.mSTMatrix, false, 0.0f, this.viewX, 0, width, height);
        this.grayRender.draw(getFbo1().texture, 3553, this.mSTMatrix, true, (float) this.viewRotateDegree, this.viewX, 0, this.viewportWidth, this.viewportHeight);
        this.ctxCur.swapBuffers();
        this.ctxCur.detachFromThread();
        this.ctxPre.attachToThread();
    }

    public void toBlack() {
        if (this.renderHandler != null) {
            this.renderHandler.post(new GLYUVSurface$$Lambda$1(this));
        }
    }

    /* access modifiers changed from: package-private */
    public final /* synthetic */ void lambda$toBlack$1$GLYUVSurface() {
        this.ctxPre.loadFromThread();
        this.ctxCur.attachToThread();
        this.inputSurfaceTexture.getTransformMatrix(this.mSTMatrix);
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
        GLES20.glClear(16384);
        this.ctxCur.swapBuffers();
        this.ctxCur.detachFromThread();
        this.ctxPre.attachToThread();
    }

    public void getBitmap(SurfaceInterface.BitmapCallback bitmapCallback) {
        getBitmap(this.curWidth, this.curHeight, bitmapCallback);
    }

    public void getBitmap(int w, int h, SurfaceInterface.BitmapCallback bitmapCallback) {
        if (this.renderHandler != null) {
            this.renderHandler.post(new GLYUVSurface$$Lambda$2(this, w, h, bitmapCallback));
        } else if (bitmapCallback != null) {
            bitmapCallback.onResult(null);
        }
    }

    /* access modifiers changed from: package-private */
    public final /* synthetic */ void lambda$getBitmap$2$GLYUVSurface(int w, int h, SurfaceInterface.BitmapCallback bitmapCallback) {
        int width = w;
        int height = h;
        if (width > this.curWidth || height > this.curHeight) {
            width = this.curWidth;
            height = this.curHeight;
        }
        boolean temp = this.running;
        this.running = false;
        this.ctxPre.loadFromThread();
        this.ctxCur.attachToThread();
        DJILog.startTime(5);
        int originFrameBuffer = GLUtil.getFrameBufferBinding();
        GLUtil.bindFrameBuffer(getFbo1().frameBuffer);
        try {
            this.inputSurfaceTexture.getTransformMatrix(this.mSTMatrix);
            this.cameraRenderer.draw(this.inputTextureID, 36197, this.mSTMatrix, true, 0.0f, 0, 0, width, height);
            if (!(this.curBufferWidth == width && this.curBufferHeight == height)) {
                this.readByteBuffer = null;
                Runtime.getRuntime().gc();
                this.curBufferWidth = width;
                this.curBufferHeight = height;
                this.readByteBuffer = ByteBuffer.allocate(this.curBufferWidth * this.curBufferHeight * 4);
            }
            this.cameraRenderer.readRGBAData(this.readByteBuffer, width, height);
        } catch (Exception e) {
            MediaLogger.e(TAG, e);
        }
        GLUtil.bindFrameBuffer(originFrameBuffer);
        this.ctxCur.detachFromThread();
        this.ctxPre.attachToThread();
        this.running = temp;
        try {
            this.outputBitmap = null;
            int limit = width * height * 4;
            if (limit <= this.readByteBuffer.capacity()) {
                this.outputBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
                this.readByteBuffer.position(0);
                this.readByteBuffer.limit(limit);
                this.outputBitmap.copyPixelsFromBuffer(this.readByteBuffer);
            }
            if (bitmapCallback != null) {
                final SurfaceInterface.BitmapCallback bitmapCallback2 = bitmapCallback;
                BackgroundLooper.post(new Runnable() {
                    /* class dji.midware.media.newframing.GLYUVSurface.AnonymousClass4 */

                    public void run() {
                        bitmapCallback2.onResult(GLYUVSurface.this.outputBitmap);
                    }
                });
            }
        } catch (OutOfMemoryError e2) {
            Runtime.getRuntime().gc();
        } catch (Exception e3) {
            DJILogHelper.getInstance().LOGE(TAG, "error");
        }
    }

    public synchronized void destroy() {
        if (this.renderHandler != null) {
            final CountDownLatch destroyCdl = new CountDownLatch(1);
            this.renderHandler.post(new Runnable() {
                /* class dji.midware.media.newframing.GLYUVSurface.AnonymousClass5 */

                public void run() {
                    GLYUVSurface.this._destroy();
                    destroyCdl.countDown();
                    if (GLYUVSurface.this.renderHandler != null) {
                        GLYUVSurface.this.renderHandler.removeCallbacksAndMessages(null);
                        Handler unused = GLYUVSurface.this.renderHandler = null;
                    }
                    if (GLYUVSurface.this.renderThread != null && GLYUVSurface.this.renderThread.isAlive()) {
                        if (Build.VERSION.SDK_INT >= 18) {
                            GLYUVSurface.this.renderThread.quitSafely();
                        } else {
                            GLYUVSurface.this.renderThread.quit();
                        }
                    }
                }
            });
            try {
                destroyCdl.await(500, TimeUnit.MILLISECONDS);
            } catch (InterruptedException e) {
                DJILogHelper.getInstance().LOGE(TAG, "destroy: " + e);
            }
        }
        return;
    }

    /* access modifiers changed from: private */
    public void _destroy() {
        if (this.hasInit) {
            this.viewSurface = null;
            this.running = false;
            this.hasInit = false;
            this.videoDecoder = null;
            if (this.hasOutputSurface) {
                if (this.ctxPre != null) {
                    this.ctxPre.loadFromThread();
                }
                if (this.ctxCur != null) {
                    this.ctxCur.attachToThread();
                }
                if (this.enableSmooth) {
                    cacheUnit();
                }
                destroyReadFrameBuffer();
                GLUtil.destroyTexture(this.inputTextureID);
                this.inputTextureID = -1;
                if (this.readByteBuffer != null) {
                    this.readByteBuffer = null;
                }
                if (this.cameraRenderer != null) {
                    this.cameraRenderer.release();
                    this.cameraRenderer = null;
                }
                if (this.vrRenderer != null) {
                    this.vrRenderer.release();
                    this.vrRenderer = null;
                }
                if (this.oewRenderer != null) {
                    this.oewRenderer.release();
                    this.oewRenderer = null;
                }
                if (this.peakingFocusPresenter != null) {
                    this.peakingFocusPresenter.release();
                    this.peakingFocusPresenter = null;
                }
                if (this.ctxCur != null) {
                    this.ctxCur.destroyAll();
                }
                if (this.ctxPre != null) {
                    this.ctxPre.attachToThread();
                }
                if (this.inputSurface != null) {
                    this.inputSurface.release();
                    this.inputSurface = null;
                }
                if (this.inputSurfaceTexture != null) {
                    this.inputSurfaceTexture.release();
                }
                if (this.fbo1 != null) {
                    this.fbo1.destroy();
                    this.fbo1 = null;
                }
                if (this.fbo2 != null) {
                    this.fbo2.destroy();
                    this.fbo2 = null;
                }
                DJILogHelper.getInstance().LOGE(TAG, "OpenGL destoryed", false, true);
            }
        }
    }

    /* JADX INFO: finally extract failed */
    public synchronized void onFrameAvailable(SurfaceTexture surfaceTexture) {
        updateInterval();
        if (this.running && this.videoDecoder != null && this.hasOutputSurface) {
            long pts = this.videoDecoder.getLastFrameOutTime();
            int srcframeWidth = this.videoDecoder.getVideoWidth();
            int srcframeHeight = this.videoDecoder.getVideoHeight();
            try {
                MediaLogger.i(false, TAG, "before makeOnScreenCurrent(): " + System.currentTimeMillis());
                this.ctxPre.loadFromThread();
                this.ctxCur.attachToThread();
                updateTexImage();
                boolean ret = sendDataToListener(pts, srcframeWidth, srcframeHeight);
                if (!this.enableSmooth && !ret) {
                    onReadPixel();
                }
                long currentTimeMillis = System.currentTimeMillis();
                display();
                long currentTimeMillis2 = System.currentTimeMillis();
                if (!this.enableSmooth) {
                    this.ctxCur.swapBuffers();
                } else {
                    GLES20.glFlush();
                }
                asyncDisplay(this.inputTextureID);
                this.secondaryDrawCount++;
                try {
                    this.ctxCur.detachFromThread();
                    this.ctxPre.attachToThread();
                } catch (Exception e) {
                    DJILogHelper.getInstance().LOGE(TAG, "onFrameAvailable: " + e);
                }
            } catch (Exception e2) {
                DJILog.d(TAG, "onFrameAvailable error:" + e2, new Object[0]);
                MediaLogger.show(e2);
                try {
                    this.ctxCur.detachFromThread();
                    this.ctxPre.attachToThread();
                } catch (Exception e3) {
                    DJILogHelper.getInstance().LOGE(TAG, "onFrameAvailable: " + e3);
                }
            } catch (Throwable th) {
                try {
                    this.ctxCur.detachFromThread();
                    this.ctxPre.attachToThread();
                } catch (Exception e4) {
                    DJILogHelper.getInstance().LOGE(TAG, "onFrameAvailable: " + e4);
                }
                throw th;
            }
        }
        return;
    }

    class GetDataTask implements Runnable {
        private ByteBuffer byteBufferRst;
        private CountDownLatch cdl;
        private Object cdlLock = new Object();
        private int height;
        private boolean isYuvData;
        private int width;

        GetDataTask() {
        }

        public synchronized ByteBuffer execute(int width2, int height2, int time, TimeUnit timeUnit, boolean isYuvData2) {
            this.width = width2;
            this.height = height2;
            this.isYuvData = isYuvData2;
            synchronized (this.cdlLock) {
                if (this.cdl == null) {
                    this.cdl = new CountDownLatch(1);
                    GLYUVSurface.this.postFrameAvailebleTask(this);
                }
            }
            await(time, timeUnit);
            return this.byteBufferRst;
        }

        public void await(int time, TimeUnit timeUnit) {
            try {
                if (this.cdl != null) {
                    this.cdl.await((long) time, timeUnit);
                }
                synchronized (this.cdlLock) {
                    this.cdl = null;
                }
            } catch (InterruptedException e) {
                DJILogHelper.getInstance().LOGE(GLYUVSurface.TAG, "await: get rgba data task" + e);
            }
        }

        public void run() {
            long currentTimeMillis = System.currentTimeMillis();
            if (this.width > GLYUVSurface.this.curWidth || this.height > GLYUVSurface.this.curHeight) {
                this.width = GLYUVSurface.this.curWidth;
                this.height = GLYUVSurface.this.curHeight;
            }
            boolean temp = GLYUVSurface.this.running;
            GLYUVSurface.this.running = false;
            GLYUVSurface.this.ctxPre.loadFromThread();
            GLYUVSurface.this.ctxCur.attachToThread();
            DJILog.startTime(5);
            int originFrameBuffer = GLUtil.getFrameBufferBinding();
            GLUtil.bindFrameBuffer(GLYUVSurface.this.getFbo1().frameBuffer);
            try {
                GLYUVSurface.this.inputSurfaceTexture.getTransformMatrix(GLYUVSurface.this.mSTMatrix);
                if (!(GLYUVSurface.this.curBufferWidth == this.width && GLYUVSurface.this.curBufferHeight == this.height)) {
                    ByteBuffer unused = GLYUVSurface.this.readByteBuffer = null;
                    Runtime.getRuntime().gc();
                    int unused2 = GLYUVSurface.this.curBufferWidth = this.width;
                    int unused3 = GLYUVSurface.this.curBufferHeight = this.height;
                    ByteBuffer unused4 = GLYUVSurface.this.readByteBuffer = ByteBuffer.allocate(GLYUVSurface.this.curBufferWidth * GLYUVSurface.this.curBufferHeight * 4);
                }
                if (this.isYuvData) {
                    GLYUVSurface.this.rgb2yuvRenderer.draw(GLYUVSurface.this.inputTextureID, 36197, GLYUVSurface.this.mSTMatrix, true, 0.0f, 0, 0, this.width, this.height);
                    GLYUVSurface.this.rgb2yuvRenderer.readYUVData(GLYUVSurface.this.readByteBuffer, this.width, this.height);
                } else {
                    GLYUVSurface.this.cameraRenderer.draw(GLYUVSurface.this.inputTextureID, 36197, GLYUVSurface.this.mSTMatrix, true, 0.0f, 0, 0, this.width, this.height);
                    GLYUVSurface.this.cameraRenderer.readRGBAData(GLYUVSurface.this.readByteBuffer, this.width, this.height);
                }
            } catch (Exception e) {
                MediaLogger.e(GLYUVSurface.TAG, e);
            }
            GLUtil.bindFrameBuffer(originFrameBuffer);
            GLYUVSurface.this.ctxCur.detachFromThread();
            GLYUVSurface.this.ctxPre.attachToThread();
            GLYUVSurface.this.running = temp;
            this.byteBufferRst = GLYUVSurface.this.readByteBuffer;
            synchronized (this.cdlLock) {
                if (this.cdl != null) {
                    this.cdl.countDown();
                }
            }
        }
    }

    class GetYuvDataTask implements Runnable {
        public ByteBuffer byteBufferRst;
        public CountDownLatch cdl;
        public int height;
        public int width;

        GetYuvDataTask() {
        }

        public void execute(int time, TimeUnit timeUnit) {
            this.byteBufferRst = null;
            if (this.cdl == null) {
                this.cdl = new CountDownLatch(1);
                GLYUVSurface.this.postFrameAvailebleTask(this);
            }
            await(time, timeUnit);
        }

        public void await(int time, TimeUnit timeUnit) {
            try {
                this.cdl.await((long) time, timeUnit);
            } catch (InterruptedException e) {
                DJILogHelper.getInstance().LOGE(GLYUVSurface.TAG, "await: get rgba data task" + e);
            }
        }

        public void run() {
            long currentTimeMillis = System.currentTimeMillis();
            if (this.width > GLYUVSurface.this.curWidth || this.height > GLYUVSurface.this.curHeight) {
                this.width = GLYUVSurface.this.curWidth;
                this.height = GLYUVSurface.this.curHeight;
            }
            boolean temp = GLYUVSurface.this.running;
            GLYUVSurface.this.running = false;
            GLYUVSurface.this.ctxPre.loadFromThread();
            GLYUVSurface.this.ctxCur.attachToThread();
            DJILog.startTime(5);
            int originFrameBuffer = GLUtil.getFrameBufferBinding();
            GLUtil.bindFrameBuffer(GLYUVSurface.this.getFbo1().frameBuffer);
            try {
                GLYUVSurface.this.inputSurfaceTexture.getTransformMatrix(GLYUVSurface.this.mSTMatrix);
                GLYUVSurface.this.rgb2yuvRenderer.draw(GLYUVSurface.this.inputTextureID, 36197, GLYUVSurface.this.mSTMatrix, true, 0.0f, 0, 0, this.width, this.height);
                if (!(GLYUVSurface.this.curBufferWidth == this.width && GLYUVSurface.this.curBufferHeight == this.height)) {
                    ByteBuffer unused = GLYUVSurface.this.readByteBuffer = null;
                    Runtime.getRuntime().gc();
                    int unused2 = GLYUVSurface.this.curBufferWidth = this.width;
                    int unused3 = GLYUVSurface.this.curBufferHeight = this.height;
                    ByteBuffer unused4 = GLYUVSurface.this.readByteBuffer = ByteBuffer.allocate(GLYUVSurface.this.curBufferWidth * GLYUVSurface.this.curBufferHeight * 4);
                }
                GLYUVSurface.this.rgb2yuvRenderer.readYUVData(GLYUVSurface.this.readByteBuffer, this.width, this.height);
            } catch (Exception e) {
                MediaLogger.e(GLYUVSurface.TAG, e);
            }
            GLUtil.bindFrameBuffer(originFrameBuffer);
            GLYUVSurface.this.ctxCur.detachFromThread();
            GLYUVSurface.this.ctxPre.attachToThread();
            GLYUVSurface.this.running = temp;
            this.byteBufferRst = GLYUVSurface.this.readByteBuffer;
            if (this.cdl != null) {
                this.cdl.countDown();
                this.cdl = null;
            }
        }
    }

    public byte[] getRgbaData(int width, int height) {
        ByteBuffer rstBuffer = this.getRgbaDataTask.execute(width, height, 3, TimeUnit.SECONDS, false);
        if (rstBuffer != null) {
            byte[] rst = new byte[(width * height * 4)];
            try {
                rstBuffer.get(rst);
                return rst;
            } catch (BufferUnderflowException e) {
                DJILogHelper.getInstance().LOGE(TAG, "getRgbaData: failed:  " + e);
                return null;
            }
        } else {
            DJILogHelper.getInstance().LOGE(TAG, "getRgbaData: ColorDisplayView failed");
            return null;
        }
    }

    public byte[] getYuvData(int width, int height, int type) {
        ByteBuffer rstBuffer = this.getYuvDataTask.execute(width, height, 3, TimeUnit.SECONDS, true);
        if (rstBuffer == null) {
            return null;
        }
        byte[] rst = new byte[(width * height * 4)];
        try {
            rstBuffer.get(rst);
            byte[] yuvRst = new byte[(width * height * 4)];
            DJIVideoUtil.YUVFormatConvert(rst, 0, yuvRst, 0, width, height, 19);
            return yuvRst;
        } catch (BufferUnderflowException e) {
            DJILogHelper.getInstance().LOGE(TAG, "getYuvData: failed: " + e);
            return null;
        }
    }

    /* access modifiers changed from: private */
    public FrameBufferTexturePair getReadCacheFbo() {
        if (this.readCacheFbo == null) {
            this.readCacheFbo = new FrameBufferTexturePair(this.curWidth, this.curHeight);
        }
        if (this.readCacheFbo.texture < 0) {
            this.readCacheFbo.create(this.curWidth, this.curHeight);
        }
        return this.readCacheFbo;
    }

    private boolean sendDataToListener(long pts, int srcframeWidth, int srcframeHeight) {
        synchronized (this.listenerSync) {
        }
        return false;
    }

    private void onReadPixel() {
        if (this.readPixelSeperatlyTask != null) {
            this.readPixelSeperatlyTask.run();
        }
    }

    private void readPixelSeperate(ByteBuffer container, int width, int height, ReadPixelResultListener listener) {
        if (this.readPixelSeperatlyTask != null) {
            DJILogHelper.getInstance().LOGD(TAG, "readPixelSeperatlyTask != null");
        }
        this.readPixelSeperatlyTask = new ReadPixelSeperatlyTask(container, width, height, listener);
        this.readPixelSeperatlyTask.execute();
    }

    private class ReadPixelSeperatlyTask implements Runnable {
        private ByteBuffer container;
        private int height;
        private int intervalTimes = 0;
        private boolean isFboCopyDone = false;
        private ReadPixelResultListener listener;
        private int originFrameBuffer;
        private int readSliceNum;
        private int width;

        public ReadPixelSeperatlyTask(ByteBuffer container2, int width2, int height2, ReadPixelResultListener listener2) {
            this.container = container2;
            this.width = width2;
            this.height = height2;
            this.listener = listener2;
            this.isFboCopyDone = false;
        }

        public void execute() {
            if (this.container != null) {
                this.readSliceNum = 0;
                this.intervalTimes = GLYUVSurface.DEFAULT_INTERVAL_TIMES;
                this.container.clear();
                GLYUVSurface.this.inputSurfaceTexture.getTransformMatrix(GLYUVSurface.this.mSTMatrix);
                GLYUVSurface.this.cameraRendererWithoutCorrection.draw(GLYUVSurface.this.getReadCacheFbo(), GLYUVSurface.this.inputTextureID, 36197, GLYUVSurface.this.mSTMatrix, false, 0.0f, 0, 0, this.width, this.height);
            }
        }

        public void run() {
            if (this.intervalTimes > 0) {
                this.intervalTimes--;
                return;
            }
            this.originFrameBuffer = GLUtil.getFrameBufferBinding();
            GLUtil.bindFrameBuffer(GLYUVSurface.this.getFbo1().frameBuffer);
            boolean isComplete = false;
            int bottom = this.readSliceNum * (this.height / GLYUVSurface.DEFAULT_SLICE_NUM);
            if ((this.readSliceNum + 1) * (this.height / GLYUVSurface.DEFAULT_SLICE_NUM) >= this.height) {
                int top = this.height;
                isComplete = true;
            }
            int position = this.container.position();
            this.container.position((((this.width * this.height) * 4) * this.readSliceNum) / GLYUVSurface.DEFAULT_SLICE_NUM);
            if (GLYUVSurface.this.readCacheFbo != null) {
                GLYUVSurface.this.rgb2yuvRenderer.draw(GLYUVSurface.this.getReadCacheFbo().texture, 3553, GLYUVSurface.this.mSTMatrix, false, 0.0f, 0, -bottom, this.width, this.height);
            }
            if (this.width == GLYUVSurface.this.videoDecoder.getVideoWidth() && this.height == GLYUVSurface.this.videoDecoder.getVideoHeight()) {
                GLYUVSurface.this.rgb2yuvRenderer.readYUVData(this.container, this.width, this.height / GLYUVSurface.DEFAULT_SLICE_NUM);
            } else {
                DJILog.save(GLYUVSurface.TAG, "width : " + this.width + ", height : " + this.height + ", videoDecoder.outputWidth : " + GLYUVSurface.this.videoDecoder.getVideoWidth() + ", videoDecoder.outputHeight : " + GLYUVSurface.this.videoDecoder.getVideoHeight());
            }
            if (isComplete) {
                if (this.listener != null) {
                    this.listener.onComplete(this.container);
                }
                ReadPixelSeperatlyTask unused = GLYUVSurface.this.readPixelSeperatlyTask = null;
                this.readSliceNum++;
            } else {
                this.readSliceNum++;
                this.intervalTimes = GLYUVSurface.DEFAULT_INTERVAL_TIMES;
            }
            GLUtil.bindFrameBuffer(this.originFrameBuffer);
        }
    }

    private void initReadFrameBuffer() {
        this.rgb2yuvRenderer = new GLRGB2YUVRender(true, true);
        this.rgb2yuvRenderer.init();
        this.grayRender = new GLGrayRender(false, false);
        this.grayRender.init();
        this.readCacheFbo = new FrameBufferTexturePair();
    }

    private void destroyReadFrameBuffer() {
        if (this.readCacheFbo != null) {
            this.readCacheFbo.destroy();
            this.readCacheFbo = null;
        }
        if (this.rgb2yuvRenderer != null) {
            this.rgb2yuvRenderer.release();
            this.rgb2yuvRenderer = null;
        }
        if (this.grayRender != null) {
            this.grayRender.release();
            this.grayRender = null;
        }
    }

    /* access modifiers changed from: private */
    public FrameBufferTexturePair getFbo1() {
        if (this.fbo1 == null) {
            this.fbo1 = new FrameBufferTexturePair(this.curWidth, this.curHeight);
        }
        if (this.fbo1.texture < 0) {
            this.fbo1.create(this.curWidth, this.curHeight);
        }
        return this.fbo1;
    }

    private FrameBufferTexturePair getFbo2() {
        if (this.fbo2 == null) {
            this.fbo2 = new FrameBufferTexturePair(this.curWidth, this.curHeight);
        }
        if (this.fbo2.texture < 0) {
            this.fbo2.create(this.curWidth, this.curHeight);
        }
        return this.fbo2;
    }

    public void enableOverExposureWarning(boolean enable, int textureResID) {
        this.overExposureWarner = enable;
        this.overExposureWarnerTextureResID = textureResID;
        this.needResetExtraRenderList = true;
    }

    public void enableDlogRender(boolean enable) {
        if (this.needDlog != enable) {
            this.needDlog = enable;
            this.needResetExtraRenderList = true;
            EventBus.getDefault().post(enable ? DJILiveviewRenderController.DlogRenderEvent.Open : DJILiveviewRenderController.DlogRenderEvent.Close);
        }
    }

    private boolean isDlogM() {
        return DJIProductManager.getInstance().getType() == ProductType.WM240;
    }

    private void setAntiDistortionEnabled(boolean enabled) {
        if (this.cameraRenderer != null) {
            ((DistortionCorrectionRender) this.cameraRenderer).setEnableAntiDistortion(enabled);
        }
    }

    public void setDistortion(boolean distortion) {
        this.isDistortion = distortion;
    }

    public void setYUVScale(float scale) {
        if (this.cameraRenderer != null) {
            this.cameraRenderer.setYUVScale(scale);
        }
    }

    public void setPeakFocusEnable(boolean enable) {
        this.peakFocusEnable = enable;
        this.needResetExtraRenderList = true;
    }

    public boolean getPeakFocusEnable() {
        return this.peakFocusEnable;
    }

    public void setPeakFocusThreshold(float threshold) {
        this.peakFocusThreshold = threshold;
        if (this.peakingFocusPresenter != null) {
            this.peakingFocusPresenter.setThreshold(threshold);
        }
    }

    public float getPeakFocusThreshold() {
        return this.peakingFocusPresenter == null ? this.peakFocusThreshold : this.peakingFocusPresenter.getThreshold();
    }

    public void setSecondaryOutputSurface(String name, Object surface, int width, int height, int vp_x, int rotateDegree) {
        if (this.ctxCur != null) {
            this.ctxCur.bindExtraSurface(name, surface, width, height, 1);
        }
    }

    public void setSecondaryOutputInterval(String name, int interval2) {
        GLContextMgr.ExtraSurfaceInfo surfaceInfo = this.ctxCur.getExtraSurfaceInfo(name);
        if (surfaceInfo != null) {
            surfaceInfo.drawInterval = interval2;
        }
    }

    public int getSecondaryOutputInterval(String name) {
        GLContextMgr.ExtraSurfaceInfo surfaceInfo = this.ctxCur.getExtraSurfaceInfo(name);
        if (surfaceInfo != null) {
            return surfaceInfo.drawInterval;
        }
        return -1;
    }

    public long getLastExtraDrawTime(String key) {
        if (this.ctxCur == null) {
            return -1;
        }
        return this.ctxCur.getLastExtraDrawTime(key);
    }

    public void updateFrameInfo(int width, int height, int zoomIndex, FrameFovType fovType) {
        DataCameraGetPushStateInfo.CameraType cameraType = DataCameraGetPushStateInfo.getInstance().getCameraType();
        if (this.cameraRenderer != null) {
            ((DistortionCorrectionRender) this.cameraRenderer).updateFrameInfo(cameraType, width, height, zoomIndex, fovType);
        } else if (zoomIndex >= 0) {
            this.cameraRenderer = new DistortionCorrectionRender(true);
            if (this.isDistortion) {
                setAntiDistortionEnabled(true);
            } else {
                setAntiDistortionEnabled(false);
            }
            ((DistortionCorrectionRender) this.cameraRenderer).updateFrameInfo(cameraType, width, height, zoomIndex, fovType);
        }
    }

    private void asyncDisplay(int texId) {
        if (this.asyncRenderThread != null) {
            synchronized (this.asyncRenderLock) {
                this.asyncRenderThread.drawNotify(texId);
            }
        }
    }

    public boolean setExtraAsyncRenderSurface(String key, Object surface, int width, int height, int interval2) {
        DJILog.d(TAG, "setExtraAsyncRenderSurface() key:" + key + " surface:" + surface + " width:" + width + " height：" + height, new Object[0]);
        if (key == null) {
            return false;
        }
        if (this.asyncRenderThread == null) {
            this.asyncRenderThread = new ExtraRenderThread(this.ctxCur.getEglContext(), this.asyncRender, this.inputTextureID, this.mSTMatrix, 0, 0);
        }
        return this.asyncRenderThread.setAsyncRenderSurface(key, surface, width, height, interval2);
    }

    public void setExtraAsyncRenderInterval(String key, int interval2) {
        if (key != null && this.asyncRenderThread != null) {
            this.asyncRenderThread.setAsyncRenderInterval(key, interval2);
        }
    }

    public int getExtraAsyncRenderInterval(String key) {
        if (TextUtils.isEmpty(key) || this.asyncRenderThread == null) {
            return -1;
        }
        return this.asyncRenderThread.getAsyncRenderInterval(key);
    }
}
