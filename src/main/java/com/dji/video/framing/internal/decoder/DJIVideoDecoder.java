package com.dji.video.framing.internal.decoder;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.NonNull;
import android.view.Surface;
import com.dji.video.framing.DJIVideoCodecInnerManager;
import com.dji.video.framing.VideoLog;
import com.dji.video.framing.internal.VideoFrame;
import com.dji.video.framing.internal.decoder.ErrorStatusManager;
import com.dji.video.framing.internal.decoder.ExtraImageReaderManager;
import com.dji.video.framing.internal.decoder.GdrKeyFrameGenerator;
import com.dji.video.framing.internal.decoder.decoderinterface.IDJIVideoDecoder;
import com.dji.video.framing.internal.decoder.decoderinterface.IDecoderOutputCallback;
import com.dji.video.framing.internal.decoder.decoderinterface.IDecoderStateListener;
import com.dji.video.framing.internal.decoder.decoderinterface.IReceiveDataCallback;
import com.dji.video.framing.internal.decoder.decoderinterface.IVideoRecordDataListener;
import com.dji.video.framing.internal.decoder.decoderinterface.IYuvDataCallback;
import com.dji.video.framing.internal.opengl.surface.SurfaceInterface;
import com.dji.video.framing.utils.BackgroundLooper;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class DJIVideoDecoder implements IDJIVideoDecoder {
    private static final boolean DEBUG_SYNC = false;
    public static final float FOCUS_PEAKING_HIGH = 4.0f;
    public static final float FOCUS_PEAKING_LOW = 1.5f;
    public static final float FOCUS_PEAKING_NORMAL = 2.7f;
    private static final String KEY_EXTRA_IMAGE_READER_SURFACE = "ExtraImageReaderSurface";
    public static final String KEY_PEAKING_FOCUS_THRESHOLD = "key_peaking_focus_threshold";
    public static final String KEY_PEAKING_FOCUS_THRESHOLD_FPV = "key_peaking_focus_threshold_fpv";
    private static final long LOG_FREQ_LIMIT = 5000;
    private static final int SAVE_LOG_INTERVAL = 3000;
    private static final String TAG = "DJIVideoDecoder";
    private static final String TAG_Input = "Decoder_Input";
    private static final String TAG_Server = "Decoder_Server";
    private static long lastLogTime = 0;
    protected boolean DEBUG_CLIENT = false;
    protected boolean DEBUG_SERVER = false;
    protected boolean DEBUG_SERVER_VERBOSE = false;
    /* access modifiers changed from: private */
    public CountDownLatch cl = null;
    /* access modifiers changed from: private */
    public VideoDecoderState decoderState = VideoDecoderState.Initialized;
    public int height = 9;
    private IReceiveDataCallback mCallback;
    private ReentrantReadWriteLock mCallbackLock = new ReentrantReadWriteLock();
    private ReentrantReadWriteLock.ReadLock mCallbackReadLock = this.mCallbackLock.readLock();
    private ReentrantReadWriteLock.WriteLock mCallbackWriteLock = this.mCallbackLock.writeLock();
    private Context mContext;
    private IDecoderOutputCallback mDecoderOutputCallback = null;
    /* access modifiers changed from: private */
    public List<IDecoderStateListener> mDecoderStateListenerList = new LinkedList();
    /* access modifiers changed from: private */
    @NonNull
    public DJIDecodeClient mDjiDecodeClient;
    private HandlerThread mExtraImageReaderHandlerThread;
    private ExtraImageReaderManager mExtraImageReaderManager;
    private InvokeOutputCallbackTask mInvokeOutputCallbackTask;
    private boolean mIsHevcMode = false;
    private boolean mIsStop = false;
    private Handler mJpegRenderHandler;
    private GdrKeyFrameGenerator.KeyFrameResCallback mKeyFrameResCallback;
    public int mLatestFrameTimestamp = -1;
    public long mLatestPTS;
    /* access modifiers changed from: private */
    public Handler mMainHandler = new Handler(Looper.getMainLooper());
    private boolean mNeedLowFreqForSmoothing = false;
    private boolean mNeedResetWhenResolutionChange = false;
    public int mOutPutWidth = 0;
    public int mOutputColorFormat = -1;
    public int mOutputHeight = 0;
    private Object mOutputSurfaceObject = null;
    private int mPlaybackFrameRate = -1;
    public byte[] mPpsHeader = null;
    private List<IVideoRecordDataListener> mRecordDataListenerList = new LinkedList();
    private SurfaceInterface mRenderManager = null;
    private ReentrantReadWriteLock mRenderManagerLock = new ReentrantReadWriteLock(false);
    private ReentrantReadWriteLock.ReadLock mRenderManagerReadLock = this.mRenderManagerLock.readLock();
    private ReentrantReadWriteLock.WriteLock mRenderManagerWriteLock = this.mRenderManagerLock.writeLock();
    public byte[] mSpsHeader = null;
    StateMonitor mStateMonitor = new StateMonitor();
    private Surface mSurfaceDrawTo;
    private VideoDecoderEventListener mVideoDecoderEventListener;
    private IYuvDataCallback mYuvDataCallback;
    /* access modifiers changed from: private */
    public Bitmap screenBitmap = null;
    public int width = 16;

    public interface VideoDecoderEventListener {
        void onCodecInit();

        void onFrameDropped();
    }

    public enum VideoDecoderState {
        Initialized,
        VideoFrameInput,
        Decoding,
        Paused
    }

    public DJIVideoDecoder(Context context, SurfaceInterface renderManager, GdrKeyFrameGenerator.KeyFrameResCallback callback, ErrorStatusManager.FrameCheckerCallback frameCheckerCallback, boolean isHevcMode) {
        this.mIsHevcMode = isHevcMode;
        if (renderManager != null) {
            renderManager.setVideoDecoder(this);
            this.mRenderManager = renderManager;
            this.mKeyFrameResCallback = callback;
            ErrorStatusManager.getInstance().setFrameCheckerCallback(frameCheckerCallback);
            init(context);
            return;
        }
        throw new NullPointerException();
    }

    public DJIVideoDecoder(Context context, Object surface) {
        if (surface != null) {
            this.mOutputSurfaceObject = surface;
            init(context);
            return;
        }
        throw new NullPointerException();
    }

    public DJIVideoDecoder(Context context, IYuvDataCallback yuvDataCallback) {
        if (yuvDataCallback != null) {
            this.mYuvDataCallback = yuvDataCallback;
            init(context);
            return;
        }
        throw new NullPointerException();
    }

    public DJIVideoDecoder(Context context, SurfaceInterface renderManager, IYuvDataCallback yuvDataCallback) {
        if (renderManager == null || yuvDataCallback == null) {
            throw new NullPointerException();
        }
        renderManager.setVideoDecoder(this);
        this.mRenderManager = renderManager;
        this.mYuvDataCallback = yuvDataCallback;
        init(context);
    }

    private void init(Context context) {
        VideoLog.d(TAG, "new a DJIVideoDecoder = " + toString(), new Object[0]);
        this.mContext = context;
        this.mDjiDecodeClient = new DJIDecodeClient(this);
        setSurface(this.mRenderManager);
        this.mDjiDecodeClient.startServer();
    }

    public void setmVideoDecoderEventListener(VideoDecoderEventListener mVideoDecoderEventListener2) {
        this.mCallbackWriteLock.lock();
        try {
            this.mVideoDecoderEventListener = mVideoDecoderEventListener2;
        } finally {
            this.mCallbackWriteLock.unlock();
        }
    }

    public void resetCodec() {
        this.mDjiDecodeClient.server.resetCodec();
    }

    /* access modifiers changed from: package-private */
    public void onFrameDropped() {
        this.mCallbackReadLock.lock();
        try {
            if (this.mVideoDecoderEventListener != null) {
                this.mVideoDecoderEventListener.onFrameDropped();
            }
        } finally {
            this.mCallbackReadLock.unlock();
        }
    }

    /* access modifiers changed from: package-private */
    public void invokeOutputCallback(VideoFrame frame) {
        if (this.mDecoderOutputCallback != null) {
            if (this.mInvokeOutputCallbackTask == null) {
                this.mInvokeOutputCallbackTask = new InvokeOutputCallbackTask();
            }
            this.mInvokeOutputCallbackTask.invoke(frame, this.mDecoderOutputCallback);
        }
        if (this.mExtraImageReaderManager != null) {
            this.mExtraImageReaderManager.offer(frame);
        }
    }

    public IYuvDataCallback getYuvDataCallBack() {
        return this.mYuvDataCallback;
    }

    /* access modifiers changed from: package-private */
    public void invokeCallbackResetVideoSurface() {
        if (this.mCallback != null) {
            this.mCallback.resetVideoSurface(getVideoWidth(), getVideoHeight());
        }
        this.mMainHandler.post(new DJIVideoDecoder$$Lambda$0(this));
    }

    /* access modifiers changed from: package-private */
    public final /* synthetic */ void lambda$invokeCallbackResetVideoSurface$0$DJIVideoDecoder() {
        if (this.mExtraImageReaderManager != null) {
            setAsyncRenderSurface(KEY_EXTRA_IMAGE_READER_SURFACE, null, getVideoWidth(), getVideoHeight(), 1);
            this.mExtraImageReaderManager.resetVideo(getVideoWidth(), getVideoHeight());
            if (this.mExtraImageReaderManager.getSurface().isValid()) {
                setAsyncRenderSurface(KEY_EXTRA_IMAGE_READER_SURFACE, this.mExtraImageReaderManager.getSurface(), getVideoWidth(), getVideoHeight(), 1);
                return;
            }
            VideoLog.e(TAG, "invokeCallbackResetVideoSurface() surface valid, width:" + getVideoWidth() + " height:" + getVideoHeight(), new Object[0]);
        }
    }

    /* access modifiers changed from: package-private */
    public void checkAndRestartImageReader() {
        if (this.mExtraImageReaderManager != null && this.mExtraImageReaderManager.hasImageReaderCallback() && !checkAsyncRenderSurfaceExist(KEY_EXTRA_IMAGE_READER_SURFACE)) {
            VideoLog.w(TAG, "checkAndRestartImageReader() reset Surface", new Object[0]);
            invokeCallbackResetVideoSurface();
        }
    }

    /* access modifiers changed from: package-private */
    public void invokeCallbackOneFrameComeIn() {
        if (this.mCallback != null) {
            this.mCallback.oneFrameComeIn();
        }
    }

    public void addRecordDataListener(IVideoRecordDataListener recordDataListener) {
        this.mCallbackWriteLock.lock();
        try {
            this.mRecordDataListenerList.add(recordDataListener);
        } finally {
            this.mCallbackWriteLock.unlock();
        }
    }

    public void removeRecordDataListener(IVideoRecordDataListener recordDataListener) {
        this.mCallbackWriteLock.lock();
        try {
            this.mRecordDataListenerList.remove(recordDataListener);
        } finally {
            this.mCallbackWriteLock.unlock();
        }
    }

    /* access modifiers changed from: package-private */
    public void invokeOnVideoFrameInput(VideoFrame frame) {
        this.mCallbackReadLock.lock();
        try {
            for (IVideoRecordDataListener recordDataListener : this.mRecordDataListenerList) {
                recordDataListener.onVideoFrameInput(frame);
            }
        } finally {
            this.mCallbackReadLock.unlock();
        }
    }

    /* access modifiers changed from: package-private */
    public void onCodecInit() {
        this.mCallbackReadLock.lock();
        try {
            if (this.mVideoDecoderEventListener != null) {
                this.mVideoDecoderEventListener.onCodecInit();
            }
        } finally {
            this.mCallbackReadLock.unlock();
        }
    }

    public void addDecoderStateListener(IDecoderStateListener IDecoderStateListener) {
        synchronized (this.mDecoderStateListenerList) {
            this.mDecoderStateListenerList.add(IDecoderStateListener);
        }
    }

    public void removeDecoderStateListener(IDecoderStateListener IDecoderStateListener) {
        synchronized (this.mDecoderStateListenerList) {
            this.mDecoderStateListenerList.remove(IDecoderStateListener);
        }
    }

    public void clearDecoderStateListeners() {
        synchronized (this.mDecoderStateListenerList) {
            this.mDecoderStateListenerList.clear();
        }
    }

    public void setRecvDataCallBack(IReceiveDataCallback mCallback2) {
        this.mCallback = mCallback2;
    }

    public void setYuvDataCallBack(IYuvDataCallback mCallback2) {
        this.mCallbackWriteLock.lock();
        if (mCallback2 == null) {
            try {
                if (this.mRenderManager == null) {
                    throw new NullPointerException();
                }
            } catch (Throwable th) {
                this.mCallbackWriteLock.unlock();
                throw th;
            }
        }
        this.mYuvDataCallback = mCallback2;
        this.mCallbackWriteLock.unlock();
    }

    public void setmDecoderOutputCallback(IDecoderOutputCallback callback) {
        this.mDecoderOutputCallback = callback;
    }

    private class InvokeOutputCallbackTask implements Runnable {
        private IDecoderOutputCallback callback;
        private int frameNum;
        private boolean isKeyFrame;

        private InvokeOutputCallbackTask() {
        }

        public void invoke(VideoFrame frame, IDecoderOutputCallback callback2) {
            if (callback2 != null) {
                this.callback = callback2;
                this.frameNum = frame.frameNum;
                this.isKeyFrame = frame.isKeyFrame;
                DJIVideoDecoder.this.mMainHandler.post(this);
            }
        }

        public void run() {
            if (this.callback != null) {
                this.callback.onFrameOutput(this.frameNum, this.isKeyFrame);
            }
        }
    }

    public VideoDecoderState getDecoderState() {
        return this.decoderState;
    }

    public boolean isDecoderOK() {
        return getDecoderState() == VideoDecoderState.Decoding;
    }

    public void setPause(boolean pause) {
        if (getDecoderState() == VideoDecoderState.Paused) {
            if (!pause) {
                this.mStateMonitor.setDecoderState(VideoDecoderState.Initialized);
            }
        } else if (pause) {
            this.mStateMonitor.setDecoderState(VideoDecoderState.Paused);
        }
    }

    class StateMonitor {
        private static final int CHECK_INPUT_TIME_INTERVAL = 1000;
        private static final int INPUT_BREAK_TIME_LIMIT = 3000;
        private static final int MSG_INPUT_BROKEN = 0;
        private static final int MSG_INVOKE_STATE_LISTENERS = 2;
        private static final int MSG_OUTPUT_BROKEN = 1;
        private static final int OUTPUT_BREAK_TIME_LIMIT = 3000;
        private Handler stateUpdateHandler = new Handler(BackgroundLooper.getLooper()) {
            /* class com.dji.video.framing.internal.decoder.DJIVideoDecoder.StateMonitor.AnonymousClass1 */

            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case 0:
                        StateMonitor.this.onInputBroken();
                        return;
                    case 1:
                        StateMonitor.this.onOutputBroken();
                        return;
                    case 2:
                        if (msg.obj != null && (msg.obj instanceof VideoDecoderState)) {
                            synchronized (DJIVideoDecoder.this.mDecoderStateListenerList) {
                                for (IDecoderStateListener listener : DJIVideoDecoder.this.mDecoderStateListenerList) {
                                    listener.onStateChange((VideoDecoderState) msg.obj);
                                }
                            }
                            return;
                        }
                        return;
                    default:
                        return;
                }
            }
        };

        StateMonitor() {
        }

        /* access modifiers changed from: private */
        public void setDecoderState(VideoDecoderState decoderState) {
            if (DJIVideoDecoder.this.decoderState != decoderState) {
                VideoDecoderState unused = DJIVideoDecoder.this.decoderState = decoderState;
                this.stateUpdateHandler.obtainMessage(2, decoderState).sendToTarget();
            }
        }

        /* access modifiers changed from: package-private */
        public void onFrameInput() {
            switch (DJIVideoDecoder.this.getDecoderState()) {
                case Initialized:
                    setDecoderState(VideoDecoderState.VideoFrameInput);
                    break;
            }
            if (this.stateUpdateHandler.hasMessages(0)) {
                this.stateUpdateHandler.removeMessages(0);
            }
            this.stateUpdateHandler.sendEmptyMessageDelayed(0, 3000);
        }

        /* access modifiers changed from: package-private */
        public void onFrameOutput() {
            switch (DJIVideoDecoder.this.getDecoderState()) {
                case VideoFrameInput:
                    setDecoderState(VideoDecoderState.Decoding);
                    DJIVideoDecoder.this.checkAndRestartImageReader();
                    DJIVideoCodecInnerManager.getInstance().getDJIVideoDecoderInterface().postStartDecodeEvent();
                    break;
            }
            if (this.stateUpdateHandler.hasMessages(1)) {
                this.stateUpdateHandler.removeMessages(1);
            }
            this.stateUpdateHandler.sendEmptyMessageDelayed(1, 3000);
        }

        /* access modifiers changed from: private */
        public void onInputBroken() {
            switch (DJIVideoDecoder.this.decoderState) {
                case VideoFrameInput:
                    setDecoderState(VideoDecoderState.Initialized);
                    return;
                case Decoding:
                    this.stateUpdateHandler.sendEmptyMessageDelayed(0, 1000);
                    return;
                default:
                    return;
            }
        }

        /* access modifiers changed from: private */
        public void onOutputBroken() {
            switch (DJIVideoDecoder.this.decoderState) {
                case Decoding:
                    setDecoderState(VideoDecoderState.VideoFrameInput);
                    return;
                default:
                    return;
            }
        }

        /* access modifiers changed from: package-private */
        public void release() {
            this.stateUpdateHandler.removeCallbacksAndMessages(null);
        }
    }

    public boolean needLowFrequencyForSmoothing() {
        return this.mNeedLowFreqForSmoothing;
    }

    public void setNeedLowFrequencyForSmoothing(boolean needLowFreqForSmoothing) {
        this.mNeedLowFreqForSmoothing = needLowFreqForSmoothing;
    }

    public Context getContext() {
        return this.mContext;
    }

    public SurfaceInterface getmRenderManager() {
        return this.mRenderManager;
    }

    /* access modifiers changed from: package-private */
    public Object getOutputSurfaceObject() {
        return this.mOutputSurfaceObject;
    }

    /* access modifiers changed from: package-private */
    public Lock getRenderManagerReadLock() {
        return this.mRenderManagerReadLock;
    }

    public void resetKeyFrame() {
        this.mDjiDecodeClient.resetKeyFrame();
    }

    public static void log2File(String log) {
        VideoLog.w(TAG, "log2File: " + log, new Object[0]);
    }

    public static void log2FileWithFreqLimit(String log) {
        long time = System.currentTimeMillis();
        if (time - lastLogTime > LOG_FREQ_LIMIT) {
            log2File(log);
            lastLogTime = time;
        }
    }

    public void queueInputBuffer(VideoFrame frame) {
        if (this.mIsStop) {
            if (this.DEBUG_CLIENT) {
                VideoLog.d(TAG_Input, String.format(Locale.US, "mIsStop=%s", Boolean.valueOf(this.mIsStop)), new Object[0]);
            }
            log2FileWithFreqLimit("dec stopped");
            return;
        }
        if (frame.spsPos >= 0 && frame.spsPos + frame.spsLen <= frame.data.length) {
            this.mSpsHeader = Arrays.copyOfRange(frame.data, frame.spsPos, frame.spsPos + frame.spsLen);
        }
        if (frame.ppsPos > 0 && frame.ppsPos + frame.ppsLen <= frame.data.length) {
            this.mPpsHeader = Arrays.copyOfRange(frame.data, frame.ppsPos, frame.ppsPos + frame.ppsLen);
        }
        if (this.DEBUG_CLIENT) {
            VideoLog.i(TAG_Input, "queueInputBuffer an input frame. frameNum=" + frame.frameNum + " frameIndex=" + frame.frameIndex + " at " + System.currentTimeMillis(), new Object[0]);
        }
        VideoLog.d(TAG, "queueInputBuffer: 0", new Object[0]);
        this.mDjiDecodeClient.queueInFrame(frame);
        invokeOnVideoFrameInput(frame);
    }

    public int getVideoWidth() {
        return this.width;
    }

    public int getVideoHeight() {
        return this.height;
    }

    public void initDecoder() {
        if (this.mDjiDecodeClient.server != null) {
            this.mDjiDecodeClient.server.sendEmptyMessage(0);
        }
    }

    public void release() {
        log2File("release decoder");
        if (this.screenBitmap != null) {
            this.screenBitmap.recycle();
            this.screenBitmap = null;
        }
        clearDecoderStateListeners();
        stopGetAsyncRgbaData(true);
        this.mIsStop = true;
        this.mStateMonitor.release();
        this.mDjiDecodeClient.stopServer();
        this.mCallback = null;
    }

    public void recvTimeout() {
        VideoLog.i(TAG, "recvTimeout()", new Object[0]);
    }

    public void setSurface(SurfaceInterface newRenderManager) {
        setSurface(newRenderManager, false);
    }

    public void setSurface(SurfaceInterface newRenderManager, boolean needRefreshServer) {
        log2File("setSurface() SurfaceInterface:" + newRenderManager + " needRefreshServer:" + needRefreshServer);
        this.mRenderManagerWriteLock.lock();
        if (needRefreshServer) {
            try {
                this.mDjiDecodeClient.stopServer();
                this.mDjiDecodeClient.startServer();
            } catch (Throwable th) {
                this.mRenderManagerWriteLock.unlock();
                throw th;
            }
        } else {
            if (this.mRenderManager != null) {
                this.mRenderManager = null;
            }
            if (newRenderManager != null) {
                this.mRenderManager = newRenderManager;
                VideoLog.e(TAG, "start DJIDecodeInoutThread() create", new Object[0]);
            }
        }
        this.mRenderManagerWriteLock.unlock();
    }

    public boolean isSurfaceAvailable() {
        return this.mRenderManager != null;
    }

    public void setPeakFocusEnable(boolean enable) {
        setPeakFocusEnable(enable, false);
    }

    public void setPeakFocusEnable(boolean enable, boolean isTemporary) {
        if (this.mRenderManager != null) {
            this.mRenderManagerReadLock.lock();
            try {
                if (isSurfaceAvailable()) {
                    this.mRenderManager.setPeakFocusEnable(enable);
                }
            } finally {
                this.mRenderManagerReadLock.unlock();
            }
        }
    }

    public boolean getPeakFocusEnable() {
        boolean z = false;
        if (this.mRenderManager != null) {
            this.mRenderManagerReadLock.lock();
            try {
                if (isSurfaceAvailable()) {
                    z = this.mRenderManager.getPeakFocusEnable();
                } else {
                    this.mRenderManagerReadLock.unlock();
                }
            } finally {
                this.mRenderManagerReadLock.unlock();
            }
        }
        return z;
    }

    public void setPeakFocusThreshold(float threshold) {
        if (this.mRenderManager != null) {
            if (((double) (threshold - 0.0f)) < 0.001d) {
                setPeakFocusEnable(false);
            } else {
                setPeakFocusEnable(true);
            }
            this.mRenderManagerReadLock.lock();
            try {
                if (isSurfaceAvailable()) {
                    this.mRenderManager.setPeakFocusThreshold(threshold);
                }
            } finally {
                this.mRenderManagerReadLock.unlock();
            }
        }
    }

    public float getPeakFocusThreshold() {
        if (this.mRenderManager == null) {
            return Float.MAX_VALUE;
        }
        this.mRenderManagerReadLock.lock();
        try {
            if (isSurfaceAvailable()) {
                return this.mRenderManager.getPeakFocusThreshold();
            }
            this.mRenderManagerReadLock.unlock();
            return -1.0f;
        } finally {
            this.mRenderManagerReadLock.unlock();
        }
    }

    public byte[] getRgbaData(int width2, int height2) {
        byte[] bArr = null;
        if (this.mRenderManager != null) {
            this.mRenderManagerReadLock.lock();
            try {
                if (this.mRenderManager != null) {
                    VideoLog.d(TAG, "getRgbaData: ColorDisplayView get rgba", new Object[0]);
                    bArr = this.mRenderManager.getRgbaData(width2, height2);
                } else {
                    VideoLog.d(TAG, "getRgbaData: ColorDisplayView render manager null", new Object[0]);
                    this.mRenderManagerReadLock.unlock();
                }
            } finally {
                this.mRenderManagerReadLock.unlock();
            }
        }
        return bArr;
    }

    public boolean startGetAsyncRgbaData(ExtraImageReaderManager.ExtraImageReaderCallback callback) {
        VideoLog.d(TAG, "startGetAsyncRgbaData() isDecoderOK():" + isDecoderOK(), new Object[0]);
        this.mMainHandler.post(new DJIVideoDecoder$$Lambda$1(this, callback));
        return true;
    }

    /* access modifiers changed from: package-private */
    public final /* synthetic */ void lambda$startGetAsyncRgbaData$1$DJIVideoDecoder(ExtraImageReaderManager.ExtraImageReaderCallback callback) {
        if (this.mExtraImageReaderHandlerThread == null || this.mExtraImageReaderManager == null) {
            this.mExtraImageReaderHandlerThread = new HandlerThread("RMThread" + (System.currentTimeMillis() % 1000));
            this.mExtraImageReaderHandlerThread.start();
            this.mExtraImageReaderManager = new ExtraImageReaderManager(this.mExtraImageReaderHandlerThread.getLooper());
        }
        this.mExtraImageReaderManager.setExtraImageReaderCallback(callback);
        if (isDecoderOK()) {
            this.mExtraImageReaderManager.startGetExtraARGBImage(getVideoWidth(), getVideoHeight());
            if (this.mExtraImageReaderManager.getSurface().isValid()) {
                setAsyncRenderSurface(KEY_EXTRA_IMAGE_READER_SURFACE, this.mExtraImageReaderManager.getSurface(), getVideoWidth(), getVideoHeight(), 1);
            }
        }
    }

    public void stopGetAsyncRgbaData(boolean fullStop) {
        VideoLog.d(TAG, "stopGetAsyncRgbaData() fullStop:" + fullStop, new Object[0]);
        this.mMainHandler.post(new DJIVideoDecoder$$Lambda$2(this, fullStop));
    }

    /* access modifiers changed from: package-private */
    public final /* synthetic */ void lambda$stopGetAsyncRgbaData$2$DJIVideoDecoder(boolean fullStop) {
        if (this.mExtraImageReaderManager != null && this.mExtraImageReaderHandlerThread != null) {
            this.mExtraImageReaderManager.setExtraImageReaderCallback(null);
            setAsyncRenderSurface(KEY_EXTRA_IMAGE_READER_SURFACE, null, getVideoWidth(), getVideoHeight(), 1);
            if (fullStop) {
                this.mExtraImageReaderManager.stopGetExtraARGBImage();
                this.mExtraImageReaderManager = null;
                this.mExtraImageReaderHandlerThread.quitSafely();
                this.mExtraImageReaderHandlerThread = null;
            }
        }
    }

    public byte[] getYuvData(int width2, int height2, int format) {
        byte[] bArr = null;
        if (this.mRenderManager != null) {
            this.mRenderManagerReadLock.lock();
            try {
                if (this.mRenderManager != null) {
                    VideoLog.d(TAG, "getRgbaData: ColorDisplayView get rgba", new Object[0]);
                    bArr = this.mRenderManager.getYuvData(width2, height2, format);
                } else {
                    VideoLog.d(TAG, "getRgbaData: ColorDisplayView render manager null", new Object[0]);
                    this.mRenderManagerReadLock.unlock();
                }
            } finally {
                this.mRenderManagerReadLock.unlock();
            }
        }
        return bArr;
    }

    public byte[] getYuvData(int width2, int height2) {
        return getYuvData(width2, height2, 21);
    }

    /* JADX DEBUG: Failed to find minimal casts for resolve overloaded methods, cast all args instead
     method: ClspMth{android.graphics.Bitmap.createBitmap(android.graphics.Bitmap, int, int, int, int, android.graphics.Matrix, boolean):android.graphics.Bitmap}
     arg types: [android.graphics.Bitmap, int, int, int, int, android.graphics.Matrix, int]
     candidates:
      ClspMth{android.graphics.Bitmap.createBitmap(android.util.DisplayMetrics, int[], int, int, int, int, android.graphics.Bitmap$Config):android.graphics.Bitmap}
      ClspMth{android.graphics.Bitmap.createBitmap(android.graphics.Bitmap, int, int, int, int, android.graphics.Matrix, boolean):android.graphics.Bitmap} */
    public Bitmap getBitmap(boolean isPortrait) {
        Bitmap bmp = getBitmap(this.width, this.height);
        if (!isPortrait || bmp == null) {
            return bmp;
        }
        Matrix matrix = new Matrix();
        matrix.setRotate(90.0f, (float) (this.width / 2), (float) (this.height / 2));
        Bitmap tmp = Bitmap.createBitmap(bmp, 0, 0, this.width, this.height, matrix, true);
        bmp.recycle();
        return tmp;
    }

    public Bitmap getBitmap(int w, int h) {
        this.cl = new CountDownLatch(1);
        if (this.screenBitmap != null) {
            this.screenBitmap.recycle();
            this.screenBitmap = null;
        }
        this.mRenderManagerReadLock.lock();
        try {
            if (this.mRenderManager == null) {
                return null;
            }
            this.mRenderManager.getBitmap(w, h, new SurfaceInterface.BitmapCallback() {
                /* class com.dji.video.framing.internal.decoder.DJIVideoDecoder.AnonymousClass1 */

                public void onResult(Bitmap bmp) {
                    Bitmap unused = DJIVideoDecoder.this.screenBitmap = bmp;
                    DJIVideoDecoder.this.cl.countDown();
                }
            });
            this.mRenderManagerReadLock.unlock();
            try {
                this.cl.await(3, TimeUnit.SECONDS);
            } catch (Exception e) {
            }
            return this.screenBitmap;
        } finally {
            this.mRenderManagerReadLock.unlock();
        }
    }

    public long getLastFrameOutTime() {
        if (this.mDjiDecodeClient.server != null) {
            return this.mDjiDecodeClient.server.getLastFrameOutTime();
        }
        return -1;
    }

    public GdrKeyFrameGenerator.KeyFrameResCallback getKeyFrameResCallback() {
        return this.mKeyFrameResCallback;
    }

    public double getFrameQueueinIntervalAvgValue() {
        if (this.mDjiDecodeClient.server != null) {
            return this.mDjiDecodeClient.server.getFrameQueueinIntervalAvgValue();
        }
        return -1.0d;
    }

    public long getLastInitCodecOutputTime() {
        if (this.mDjiDecodeClient == null || this.mDjiDecodeClient.server == null) {
            return -1;
        }
        return this.mDjiDecodeClient.server.getLastInitCodecOutputTime();
    }

    public void updateYuvDataCallback(ByteBuffer yuvFrame, int offset, int dataSize, int width2, int height2, int frameNum, long frameIndex) {
        if (getYuvDataCallBack() != null) {
            final ByteBuffer byteBuffer = yuvFrame;
            final int i = offset;
            final int i2 = dataSize;
            final int i3 = width2;
            final int i4 = height2;
            final int i5 = frameNum;
            final long j = frameIndex;
            BackgroundLooper.post(new Runnable() {
                /* class com.dji.video.framing.internal.decoder.DJIVideoDecoder.AnonymousClass2 */

                public void run() {
                    DJIVideoDecoder.this.getYuvDataCallBack().onYuvDataReceived(byteBuffer, i, i2, i3, i4, i5, j);
                    if (byteBuffer != null && DJIVideoDecoder.this.mDjiDecodeClient.server != null) {
                        DJIVideoDecoder.this.mDjiDecodeClient.server.yuvDataBufPool.recycle(byteBuffer);
                    }
                }
            });
        }
    }

    public void setNeedResetCodecWhenResolutionChanged(boolean need) {
        this.mNeedResetWhenResolutionChange = need;
    }

    public boolean isNeedResetCodecWhenResolutionChanged() {
        return this.mNeedResetWhenResolutionChange;
    }

    /* access modifiers changed from: package-private */
    public boolean checkAsyncRenderSurfaceExist(String key) {
        return this.mRenderManager != null && this.mRenderManager.getExtraAsyncRenderInterval(key) > -1;
    }

    public boolean setAsyncRenderSurface(String key, Object surface, int width2, int height2, int interval) {
        if (this.mRenderManager != null) {
            return this.mRenderManager.setExtraAsyncRenderSurface(key, surface, width2, height2, interval);
        }
        return false;
    }

    public boolean isHevcMode() {
        return this.mIsHevcMode;
    }

    public void setHevcMode(boolean hevcMode) {
        this.mIsHevcMode = hevcMode;
    }
}
