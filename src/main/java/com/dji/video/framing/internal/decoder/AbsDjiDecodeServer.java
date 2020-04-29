package com.dji.video.framing.internal.decoder;

import android.media.MediaCodec;
import android.media.MediaFormat;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.NonNull;
import com.dji.video.framing.DJIVideoHEVCFomatManager;
import com.dji.video.framing.VideoLog;
import com.dji.video.framing.internal.VideoFrame;
import com.dji.video.framing.internal.decoder.DJIVideoDecoder;
import com.dji.video.framing.internal.decoder.decoderinterface.IBlackKeyFrameGenerator;
import com.dji.video.framing.utils.AvgCalculator;
import com.dji.video.framing.utils.DJIVideoUtil;
import com.dji.video.framing.utils.TreeSetPool;
import java.io.FileOutputStream;
import java.nio.ByteBuffer;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.Locale;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

public abstract class AbsDjiDecodeServer extends Handler {
    protected static final boolean DEBUG = false;
    protected static final int DEQUEUE_INPUT_BUFFER = 10000;
    private static final int INPUT_RESTORE_DIFF_THRESHOLD = 200;
    private static final int INPUT_RESTORE_INTERVAL = 1000;
    protected static final int IN_OUT_DIFF_THRESHOLD = 2000;
    public static final int InputFramePoolCapacity = 32;
    public static final int InputQueueCapacity = 15;
    private static final int LOG_I_FRAME_IN_INTERVAL = 1500;
    public static final int MSG_INIT = 0;
    public static final int MSG_IN_OUT = 2;
    public static final int MSG_QUEUEIN = 1;
    public static final int MSG_REINIT_KEY_FRAME = 10;
    protected static final int REFRESH_IFRAME_RETRY_INTERVAL = 500;
    private static final int REFRESH_IFRAME_RETRY_NUM = 6;
    private static final String TAG = "AbsDjiDecodeServer";
    public LinkedList<Long> bufferChangedQueue = new LinkedList<>();
    public MediaCodec.BufferInfo bufferInfo = new MediaCodec.BufferInfo();
    protected MediaCodec codec = null;
    protected int codecDequeueFailCount = 0;
    protected int codecFailResetThreshold = 20;
    private int count = 0;
    protected boolean decoderConfigure = false;
    private boolean errorStatus = false;
    private AvgCalculator frameIntervalCalculator;
    protected boolean iframeIntoCodec = false;
    public boolean iframeIntoQueue = false;
    private boolean initCodecing = false;
    protected BlockingQueue<VideoFrame> inputQueue = new LinkedBlockingQueue(15);
    private long lastFrameIndex = -1;
    private int lastFrameNum = -1;
    private long lastInitCodecTime = -1;
    private long lastLogIframeInTime = -1;
    private int lastOutputHeight;
    private int lastOutputWidth;
    private long lastT = 0;
    protected FileOutputStream liveStreamOutputStream;
    private IBlackKeyFrameGenerator mBlackKeyFrameGenerator;
    @NonNull
    protected DJIVideoDecoder mDjiVideoDecoder;
    private int mFrameDropCount = 0;
    private boolean needRefreshIframe = false;
    public int outputBufferIndex;
    protected final BlockingQueue<VideoFrame> outputQueue = new LinkedBlockingQueue(15);
    private int refreshIframeRetryCount = 0;
    protected boolean saveLiveStream = false;
    private long tLastFrameIn = -1;
    private long tLastFrameOut = -1;
    private long tLastInitOutputTime = -1;
    private long tLastInputRestore = -1;
    private long timeOutput;
    protected AtomicInteger time_initialized = new AtomicInteger(0);
    TreeSetPool<ByteBuffer> yuvDataBufPool = new TreeSetPool<>(5, new Comparator<ByteBuffer>() {
        /* class com.dji.video.framing.internal.decoder.AbsDjiDecodeServer.AnonymousClass1 */

        public int compare(ByteBuffer buf0, ByteBuffer buf1) {
            if (buf0 == null) {
                if (buf1 == null) {
                    return 0;
                }
                return -1;
            } else if (buf1 == null) {
                return 1;
            } else {
                return buf0.capacity() - buf1.capacity();
            }
        }
    });

    /* access modifiers changed from: protected */
    public abstract void handleInOutMsg();

    /* access modifiers changed from: protected */
    public abstract void onCodecConfigured();

    /* access modifiers changed from: protected */
    public abstract void onCodecStarted();

    /* access modifiers changed from: protected */
    public abstract void queueInframe(@NonNull VideoFrame videoFrame);

    public AbsDjiDecodeServer(Looper looper, @NonNull DJIVideoDecoder decoder) {
        super(looper);
        this.mDjiVideoDecoder = decoder;
        this.frameIntervalCalculator = new AvgCalculator(20.0d, 70.0d, 30);
        this.mBlackKeyFrameGenerator = new GdrKeyFrameGenerator(decoder.getContext(), this.mDjiVideoDecoder.getKeyFrameResCallback());
    }

    /* access modifiers changed from: protected */
    public void clearAndReleaseInputFrame() {
        if (this.inputQueue != null) {
            for (VideoFrame elem : this.inputQueue) {
                elem.recycle();
            }
            this.inputQueue.clear();
        }
    }

    public long getLastFrameOutTime() {
        return this.tLastFrameOut;
    }

    public void handleMessage(Message msg) {
        try {
            switch (msg.what) {
                case 0:
                    try {
                        VideoLog.i(TAG, "init decoder by command", new Object[0]);
                        initVideoDecoder();
                    } catch (Exception e) {
                        VideoLog.e(TAG, e);
                    }
                    VideoLog.d(TAG, "handleMessage: removeMsg", new Object[0]);
                    removeCallbacksAndMessages(null);
                    sendEmptyMessageDelayed(2, 1);
                    return;
                case 1:
                    VideoLog.i(TAG, "server on msg_queuein", new Object[0]);
                    try {
                        onServerQueueIn(msg);
                    } catch (Exception e2) {
                        VideoLog.e(TAG, e2);
                    }
                    if (!hasMessages(2)) {
                        sendEmptyMessageDelayed(2, 1);
                        return;
                    }
                    return;
                case 2:
                    handleInOutMsg();
                    return;
                case 10:
                    VideoLog.e(TAG, "reinit codec", new Object[0]);
                    DJIVideoDecoder.log2File("reinit codec");
                    try {
                        this.iframeIntoQueue = false;
                        this.iframeIntoCodec = false;
                        clearAndReleaseInputFrame();
                        ErrorStatusManager.getInstance().onErrorStatusChange(true);
                    } catch (Exception e3) {
                        VideoLog.e(TAG, e3);
                    }
                    if (!hasMessages(2)) {
                        sendEmptyMessageDelayed(2, 1);
                        return;
                    }
                    return;
                default:
                    return;
            }
        } catch (Exception e4) {
            VideoLog.e(TAG, e4);
        }
        VideoLog.e(TAG, e4);
    }

    /* access modifiers changed from: protected */
    public boolean checkFrameIndex(VideoFrame frame) {
        if (this.lastFrameIndex < 0 || frame.isKeyFrame) {
            return true;
        }
        boolean rst = true;
        if (!(((long) frame.checkIndex) == this.lastFrameIndex + 1 || frame.checkIndex == 0)) {
            rst = false;
        }
        if (!rst) {
        }
        this.lastFrameIndex = (long) frame.checkIndex;
        return rst;
    }

    private void onServerQueueIn(Message msg) {
        VideoLog.d(TAG, "onServerQueueIn() frameNum=" + ((VideoFrame) msg.obj).frameNum, new Object[0]);
        if (this.mDjiVideoDecoder.getDecoderState() == DJIVideoDecoder.VideoDecoderState.Paused) {
            DJIVideoDecoder.log2File("dec pause");
            return;
        }
        VideoFrame newFrame = (VideoFrame) msg.obj;
        if (newFrame != null) {
            this.mDjiVideoDecoder.mStateMonitor.onFrameInput();
            if (this.codec == null && ErrorStatusManager.getInstance().needCheckFrame() && (newFrame.width == 0 || newFrame.height == 0)) {
                VideoLog.w(TAG, "onServerQueueIn() codec is null and new frame w = 0 or h = 0", new Object[0]);
                ErrorStatusManager.getInstance().onErrorStatusChange(true);
            }
            if (DJIVideoHEVCFomatManager.getInstance().isInHevcMode()) {
                newFrame.frameNum = this.lastFrameNum + 1;
            }
            if (newFrame.frameNum - this.lastFrameNum != 1) {
                VideoLog.d(TAG, "onServerQueueIn() not continuous frameNum=" + newFrame.frameNum + " last=" + this.lastFrameNum, new Object[0]);
            }
            this.lastFrameNum = newFrame.frameNum;
            VideoLog.d(TAG, "onServerQueueIn() iskey=" + newFrame.isKeyFrame + " frameNum=" + newFrame.frameNum + ", frameIndex=" + newFrame.frameIndex + ", iframeIntoQueue=" + this.iframeIntoQueue, new Object[0]);
            long time = System.currentTimeMillis();
            if (time - this.tLastFrameIn > 1000) {
                this.tLastInputRestore = time;
            }
            if (this.frameIntervalCalculator != null && this.tLastFrameIn > 0) {
                this.frameIntervalCalculator.feedData((double) (time - this.tLastFrameIn));
            }
            this.tLastFrameIn = time;
            if (newFrame.width > 0 && newFrame.height > 0 && (this.codec == null || newFrame.width != this.mDjiVideoDecoder.width || newFrame.height != this.mDjiVideoDecoder.height || this.needRefreshIframe)) {
                VideoLog.w(TAG, "onServerQueueIn() need resetDecoder() newFrame.width:" + newFrame.width + " newFrame.height:" + newFrame.height + " mDjiVideoDecoder.width:" + this.mDjiVideoDecoder.width + " mDjiVideoDecoder.height:" + this.mDjiVideoDecoder.height + " needRefreshIframe:" + this.needRefreshIframe + " codec:" + this.codec, new Object[0]);
                if (!(newFrame.width == this.mDjiVideoDecoder.width && newFrame.height == this.mDjiVideoDecoder.height)) {
                    this.mDjiVideoDecoder.setNeedResetCodecWhenResolutionChanged(true);
                }
                this.mDjiVideoDecoder.width = newFrame.width;
                this.mDjiVideoDecoder.height = newFrame.height;
                boolean isFirstInit = this.codec == null || !this.iframeIntoQueue || this.needRefreshIframe;
                if (this.codec == null || this.mDjiVideoDecoder.isNeedResetCodecWhenResolutionChanged() || this.needRefreshIframe) {
                    this.refreshIframeRetryCount++;
                    initVideoDecoder(isFirstInit, ErrorStatusManager.getInstance().isDemandI());
                    this.mDjiVideoDecoder.setNeedResetCodecWhenResolutionChanged(false);
                    this.needRefreshIframe = false;
                    this.iframeIntoQueue = false;
                }
            }
            if (!this.iframeIntoQueue) {
                VideoLog.w(TAG, "onServerQueueIn() receives a frame,  when iframe is not set, currentErrorStatus:" + ErrorStatusManager.getInstance().getErrorStatus(), new Object[0]);
                if (time - this.lastInitCodecTime > 1500 && time - this.lastLogIframeInTime > 1500) {
                    this.lastLogIframeInTime = time;
                }
                if (!this.mBlackKeyFrameGenerator.isGdrStartFrame(newFrame) && !newFrame.isKeyFrame) {
                    return;
                }
                if (newFrame.isKeyFrame) {
                    VideoLog.w(TAG, "onServerQueueIn() The coming frame is I-frame", new Object[0]);
                    this.iframeIntoQueue = true;
                } else if (ErrorStatusManager.getInstance().isDemandI() || !this.mBlackKeyFrameGenerator.needGenFakeKeyFrame(newFrame)) {
                    VideoLog.e(TAG, "onServerQueueIn() The stream is GOP and the coming frame is not I-frame", new Object[0]);
                    return;
                } else {
                    VideoLog.w(TAG, "onServerQueueIn() server black key frame", new Object[0]);
                    queueInframe(this.mBlackKeyFrameGenerator.genFakeKeyFrame(newFrame));
                    this.iframeIntoQueue = true;
                }
            }
            if (this.iframeIntoQueue) {
                queueInframe(newFrame);
                return;
            }
            return;
        }
        DJIVideoDecoder.log2File("onServerQueueIn() dec frame null");
    }

    /* access modifiers changed from: protected */
    public void offerFrameToInputQueue(@NonNull VideoFrame inputFrame) {
        if (this.inputQueue.offer(inputFrame)) {
            this.mFrameDropCount = 0;
            VideoLog.i(TAG, "put a frame into the Extended-Queue with index=" + inputFrame.frameIndex, new Object[0]);
            return;
        }
        VideoLog.e(TAG, "offerFrameToInputQueue: drop frame frameIndex=" + inputFrame.frameIndex, new Object[0]);
        VideoFrame dropFrame = this.inputQueue.poll();
        if (dropFrame != null) {
            this.mDjiVideoDecoder.onFrameDropped();
            dropFrame.recycle();
        }
        this.inputQueue.offer(inputFrame);
        this.mFrameDropCount++;
        if (this.mFrameDropCount > 100) {
            VideoLog.e(TAG, "offerFrameToInputQueue() mFrameDropCount 100 times, the coded may be stuck,  need restart", new Object[0]);
            this.needRefreshIframe = true;
        }
    }

    /* access modifiers changed from: protected */
    public boolean onDecoderInput(@NonNull VideoFrame inputFrame, int inIndex, ByteBuffer buffer) {
        VideoLog.d(TAG, "onDecoderInput() frameSize=" + inputFrame.size + ", frameNum=" + inputFrame.frameNum + ", frameIndex=" + inputFrame.frameIndex + ", iframeIn=" + this.iframeIntoQueue, new Object[0]);
        if (buffer == null) {
            buffer = this.codec.getInputBuffer(inIndex);
        }
        if (!checkFrameIndex(inputFrame)) {
            ErrorStatusManager.getInstance().onErrorStatusChange(true);
            inputFrame.recycle();
            clearAndReleaseInputFrame();
            return false;
        } else if (this.codec == null || isInitingCodec() || buffer == null) {
            return false;
        } else {
            buffer.clear();
            buffer.rewind();
            buffer.put(inputFrame.data);
            try {
                inputFrame.fedIntoCodecTime = System.currentTimeMillis();
                VideoLog.d(TAG, "onDecoderInput: width=" + inputFrame.width + " height=" + inputFrame.height + " iskey=" + inputFrame.isKeyFrame + " frameNum=" + inputFrame.frameNum, new Object[0]);
                queueToCodec(this.codec, inputFrame.data, inIndex, 0, inputFrame.size, inputFrame.pts, 0);
                if (inputFrame == this.inputQueue.peek()) {
                    this.inputQueue.poll();
                }
                outputQueueEnqueue(inputFrame);
                this.iframeIntoCodec = true;
                return true;
            } catch (Exception e) {
                VideoLog.e(TAG, "onDecoderInput() error:" + e, new Object[0]);
                throw e;
            }
        }
    }

    public void resetCodec() {
        VideoLog.i(TAG, "reset codec", new Object[0]);
        DJIVideoDecoder.log2File("reset codec!");
        clearAndReleaseInputFrame();
        this.iframeIntoQueue = false;
        this.codecDequeueFailCount = 0;
        if (this.codec != null) {
            this.codec.flush();
        }
    }

    private void LOG(String log) {
    }

    /* access modifiers changed from: protected */
    public void onOutputFormatChanged() {
        if (this.codec != null) {
            MediaFormat newFormat = this.codec.getOutputFormat();
            VideoLog.d(TAG, newFormat.toString(), new Object[0]);
            DJIVideoUtil.Resolution res = DJIVideoUtil.getResolutionReliably(newFormat);
            this.mDjiVideoDecoder.mOutPutWidth = res.width;
            this.mDjiVideoDecoder.mOutputHeight = res.height;
            this.mDjiVideoDecoder.mOutputColorFormat = newFormat.getInteger("color-format");
            VideoLog.d(TAG, String.format(Locale.US, "Format changed. color=%d, width=%d, height=%d", Integer.valueOf(this.mDjiVideoDecoder.mOutputColorFormat), Integer.valueOf(this.mDjiVideoDecoder.mOutPutWidth), Integer.valueOf(this.mDjiVideoDecoder.mOutputHeight)), new Object[0]);
            VideoLog.e(TAG, "dequeueOutputBuffer INFO_OUTPUT_FORMAT_CHANGED", new Object[0]);
        }
    }

    private void printRate() {
        this.count++;
        if (System.currentTimeMillis() - this.lastT > 5000) {
            this.lastT = System.currentTimeMillis();
            this.count = 0;
        }
    }

    /* JADX DEBUG: Failed to find minimal casts for resolve overloaded methods, cast all args instead
     method: ClspMth{android.media.MediaCodec.releaseOutputBuffer(int, boolean):void}
     arg types: [int, int]
     candidates:
      ClspMth{android.media.MediaCodec.releaseOutputBuffer(int, long):void}
      ClspMth{android.media.MediaCodec.releaseOutputBuffer(int, boolean):void} */
    private void onOutputFrame(int outputIndex, MediaCodec.BufferInfo bufferInfo2) throws Exception {
        VideoLog.d(TAG, "onOutputFrame() outputIndex:" + outputIndex + " bufferInfo:" + bufferInfo2, new Object[0]);
        if (this.codec != null && !isInitingCodec() && !this.needRefreshIframe) {
            if (!DJIVideoHEVCFomatManager.getInstance().isInHevcMode() || bufferInfo2 == null || bufferInfo2.presentationTimeUs >= this.mDjiVideoDecoder.mLatestPTS || this.needRefreshIframe) {
                this.timeOutput = System.currentTimeMillis();
                this.tLastFrameOut = this.timeOutput;
                this.refreshIframeRetryCount = 0;
                if (this.tLastInitOutputTime < 0) {
                    this.tLastInitOutputTime = this.timeOutput;
                }
                VideoLog.i(TAG, "decoder outputs a frame at " + System.currentTimeMillis(), new Object[0]);
                this.mDjiVideoDecoder.mStateMonitor.onFrameOutput();
                this.mDjiVideoDecoder.mLatestPTS = bufferInfo2 == null ? this.mDjiVideoDecoder.mLatestPTS : bufferInfo2.presentationTimeUs;
                if (this.mDjiVideoDecoder.getmRenderManager() == null && this.mDjiVideoDecoder.getOutputSurfaceObject() == null) {
                    VideoLog.e(TAG, "mGLRenderManager == null || renderer==null", new Object[0]);
                    updateYUVDataCallback(outputIndex, bufferInfo2);
                    this.codec.releaseOutputBuffer(outputIndex, false);
                    return;
                }
                long frameIndex = 0;
                if (this.mDjiVideoDecoder.mLatestPTS != 0) {
                    VideoFrame outputFrame = outputQueueDequeue(this.mDjiVideoDecoder.mLatestPTS);
                    if (outputFrame != null) {
                        if (outputFrame.timeStamp >= 0) {
                            this.mDjiVideoDecoder.mLatestFrameTimestamp = outputFrame.timeStamp;
                        }
                        this.mDjiVideoDecoder.getmRenderManager().updateFrameInfo(outputFrame.width, outputFrame.height, outputFrame.zoomIndex, outputFrame.fovType);
                        if (!(outputFrame.width == this.lastOutputWidth && outputFrame.height == this.lastOutputHeight)) {
                            this.mDjiVideoDecoder.invokeCallbackResetVideoSurface();
                            this.lastOutputWidth = outputFrame.width;
                            this.lastOutputHeight = outputFrame.height;
                        }
                        this.mDjiVideoDecoder.invokeOutputCallback(outputFrame);
                        outputFrame.recycle();
                    }
                    frameIndex = (long) DJIVideoUtil.getFrameIndex(this.mDjiVideoDecoder.mLatestPTS);
                    VideoLog.i(TAG, "Decoding_delay=" + (System.currentTimeMillis() - DJIVideoUtil.getPtsMs(this.mDjiVideoDecoder.mLatestPTS)) + " frameIndex=" + frameIndex + " frameNum=" + DJIVideoUtil.getFrameNum(this.mDjiVideoDecoder.mLatestPTS) + " comPts=" + this.mDjiVideoDecoder.mLatestPTS + " queueLen=" + this.inputQueue.size(), new Object[0]);
                }
                printRate();
                long currentTimeMillis = System.currentTimeMillis();
                this.codec.releaseOutputBuffer(outputIndex, true);
                VideoLog.d(TAG, "onOutputFrame: latestPts=" + this.mDjiVideoDecoder.mLatestPTS, new Object[0]);
                VideoLog.i(TAG, "sync releaseOutputBuffer over at " + frameIndex + " " + System.currentTimeMillis() + " duration=" + (System.currentTimeMillis() - this.timeOutput), new Object[0]);
                this.mDjiVideoDecoder.invokeCallbackOneFrameComeIn();
                VideoLog.i(TAG, "after mCallback.oneFrameComeIn", new Object[0]);
                return;
            }
            VideoLog.e(TAG, "onOutputFrame(), older pts decode output, need restart decoder presentationTimeUs:" + bufferInfo2.presentationTimeUs + " mLatestPTS:" + this.mDjiVideoDecoder.mLatestPTS, new Object[0]);
            this.needRefreshIframe = true;
        }
    }

    /* access modifiers changed from: package-private */
    public void updateYUVDataCallback(int outputIndex, MediaCodec.BufferInfo bufferInfo2) {
        ByteBuffer srcBuf;
        if (this.mDjiVideoDecoder.getYuvDataCallBack() == null) {
            return;
        }
        if (outputIndex >= 0) {
            MediaFormat format = this.codec.getOutputFormat();
            VideoLog.d(TAG, "updateYUVDataCallback: stride=" + format.getInteger("stride") + " sliceHeight=" + format.getInteger("slice-height"), new Object[0]);
            if (Build.VERSION.SDK_INT < 21) {
                srcBuf = this.codec.getOutputBuffers()[outputIndex];
            } else {
                srcBuf = this.codec.getOutputBuffer(outputIndex);
            }
            ByteBuffer yuvDataBuf = this.yuvDataBufPool.obtain(srcBuf);
            if (yuvDataBuf == null) {
                yuvDataBuf = ByteBuffer.allocate(srcBuf.capacity());
            }
            yuvDataBuf.clear();
            VideoLog.d(TAG, "yuvDataBuf  w " + this.mDjiVideoDecoder.mOutPutWidth + " width " + this.mDjiVideoDecoder.width + " h " + this.mDjiVideoDecoder.mOutputHeight + "height" + this.mDjiVideoDecoder.height, new Object[0]);
            yuvDataBuf.position(0);
            yuvDataBuf.limit(bufferInfo2.size);
            this.mDjiVideoDecoder.updateYuvDataCallback(yuvDataBuf, bufferInfo2.offset, bufferInfo2.size - bufferInfo2.offset, this.mDjiVideoDecoder.mOutPutWidth, this.mDjiVideoDecoder.mOutputHeight, DJIVideoUtil.getFrameNum(this.mDjiVideoDecoder.mLatestPTS), (long) DJIVideoUtil.getFrameIndex(this.mDjiVideoDecoder.mLatestPTS));
            return;
        }
        this.mDjiVideoDecoder.updateYuvDataCallback(null, bufferInfo2.offset, bufferInfo2.size - bufferInfo2.offset, this.mDjiVideoDecoder.mOutPutWidth, this.mDjiVideoDecoder.mOutputHeight, DJIVideoUtil.getFrameNum(this.mDjiVideoDecoder.mLatestPTS), (long) DJIVideoUtil.getFrameIndex(this.mDjiVideoDecoder.mLatestPTS));
    }

    public boolean isInitingCodec() {
        return this.initCodecing;
    }

    /* access modifiers changed from: protected */
    public void initVideoDecoder() {
        initVideoDecoder(false, true);
    }

    /* JADX INFO: finally extract failed */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r7v0, resolved type: java.lang.Object} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r0v0, resolved type: android.view.Surface} */
    /* access modifiers changed from: protected */
    /* JADX WARNING: Multi-variable type inference failed */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void initVideoDecoder(boolean r12, boolean r13) {
        /*
            r11 = this;
            java.lang.String r8 = "AbsDjiDecodeServer"
            java.lang.String r9 = "initVideoDecoder()"
            r10 = 0
            java.lang.Object[] r10 = new java.lang.Object[r10]
            com.dji.video.framing.VideoLog.w(r8, r9, r10)
            long r8 = java.lang.System.currentTimeMillis()
            r11.lastInitCodecTime = r8
            r6 = 0
            com.dji.video.framing.internal.decoder.DJIVideoDecoder r8 = r11.mDjiVideoDecoder
            java.util.concurrent.locks.Lock r8 = r8.getRenderManagerReadLock()
            r8.lock()
            com.dji.video.framing.internal.decoder.DJIVideoDecoder r8 = r11.mDjiVideoDecoder     // Catch:{ all -> 0x00da }
            com.dji.video.framing.internal.opengl.surface.SurfaceInterface r8 = r8.getmRenderManager()     // Catch:{ all -> 0x00da }
            if (r8 == 0) goto L_0x0099
            com.dji.video.framing.internal.decoder.DJIVideoDecoder r8 = r11.mDjiVideoDecoder     // Catch:{ all -> 0x00da }
            com.dji.video.framing.internal.opengl.surface.SurfaceInterface r8 = r8.getmRenderManager()     // Catch:{ all -> 0x00da }
            android.view.Surface r6 = r8.getInputSurface()     // Catch:{ all -> 0x00da }
        L_0x002e:
            com.dji.video.framing.internal.decoder.DJIVideoDecoder r8 = r11.mDjiVideoDecoder
            java.util.concurrent.locks.Lock r8 = r8.getRenderManagerReadLock()
            r8.unlock()
            r8 = 1
            r11.initCodecing = r8
            android.media.MediaCodec r8 = r11.codec
            if (r8 == 0) goto L_0x0041
            r11.releaseDecoder()
        L_0x0041:
            r8 = 0
            r11.iframeIntoCodec = r8
            r8 = 0
            r11.iframeIntoQueue = r8
            r8 = -1
            r11.lastFrameIndex = r8
            r8 = 0
            r11.lastOutputWidth = r8
            r8 = 0
            r11.lastOutputHeight = r8
            r2 = 0
            com.dji.video.framing.internal.decoder.ErrorStatusManager r8 = com.dji.video.framing.internal.decoder.ErrorStatusManager.getInstance()
            boolean r8 = r8.isDemandI()
            if (r8 == 0) goto L_0x0071
            com.dji.video.framing.DJIVideoHEVCFomatManager r8 = com.dji.video.framing.DJIVideoHEVCFomatManager.getInstance()
            boolean r5 = r8.isInHevcMode()
            com.dji.video.framing.internal.decoder.DJIVideoDecoder r8 = r11.mDjiVideoDecoder
            boolean r8 = r8.isHevcMode()
            if (r5 == r8) goto L_0x0071
            com.dji.video.framing.internal.decoder.DJIVideoDecoder r8 = r11.mDjiVideoDecoder
            r8.setHevcMode(r5)
        L_0x0071:
            com.dji.video.framing.internal.decoder.DJIVideoDecoder r8 = r11.mDjiVideoDecoder
            boolean r8 = r8.isHevcMode()
            if (r8 == 0) goto L_0x00e5
            java.lang.String[] r8 = com.dji.video.framing.utils.DJIVideoUtil.VIDEO_ENCODING_FORMAT
            r9 = 1
            r2 = r8[r9]
        L_0x007e:
            com.dji.video.framing.internal.decoder.DJIVideoDecoder r8 = r11.mDjiVideoDecoder
            int r8 = r8.width
            com.dji.video.framing.internal.decoder.DJIVideoDecoder r9 = r11.mDjiVideoDecoder
            int r9 = r9.height
            android.media.MediaFormat r4 = android.media.MediaFormat.createVideoFormat(r2, r8, r9)
            if (r6 != 0) goto L_0x00eb
            java.lang.String r8 = "AbsDjiDecodeServer"
            java.lang.String r9 = "initVideoDecoder() surface == null "
            r10 = 0
            java.lang.Object[] r10 = new java.lang.Object[r10]
            com.dji.video.framing.VideoLog.e(r8, r9, r10)
        L_0x0098:
            return
        L_0x0099:
            com.dji.video.framing.internal.decoder.DJIVideoDecoder r8 = r11.mDjiVideoDecoder     // Catch:{ all -> 0x00da }
            java.lang.Object r8 = r8.getOutputSurfaceObject()     // Catch:{ all -> 0x00da }
            if (r8 == 0) goto L_0x002e
            com.dji.video.framing.internal.decoder.DJIVideoDecoder r8 = r11.mDjiVideoDecoder     // Catch:{ all -> 0x00da }
            java.lang.Object r7 = r8.getOutputSurfaceObject()     // Catch:{ all -> 0x00da }
            boolean r8 = r7 instanceof android.view.Surface     // Catch:{ all -> 0x00da }
            if (r8 == 0) goto L_0x00b1
            r0 = r7
            android.view.Surface r0 = (android.view.Surface) r0     // Catch:{ all -> 0x00da }
            r6 = r0
            goto L_0x002e
        L_0x00b1:
            boolean r8 = r7 instanceof android.view.SurfaceHolder     // Catch:{ all -> 0x00da }
            if (r8 == 0) goto L_0x00bd
            android.view.SurfaceHolder r7 = (android.view.SurfaceHolder) r7     // Catch:{ all -> 0x00da }
            android.view.Surface r6 = r7.getSurface()     // Catch:{ all -> 0x00da }
            goto L_0x002e
        L_0x00bd:
            boolean r8 = r7 instanceof android.graphics.SurfaceTexture     // Catch:{ all -> 0x00da }
            if (r8 == 0) goto L_0x00ca
            android.view.Surface r6 = new android.view.Surface     // Catch:{ all -> 0x00da }
            android.graphics.SurfaceTexture r7 = (android.graphics.SurfaceTexture) r7     // Catch:{ all -> 0x00da }
            r6.<init>(r7)     // Catch:{ all -> 0x00da }
            goto L_0x002e
        L_0x00ca:
            boolean r8 = r7 instanceof android.view.SurfaceView     // Catch:{ all -> 0x00da }
            if (r8 == 0) goto L_0x002e
            android.view.SurfaceView r7 = (android.view.SurfaceView) r7     // Catch:{ all -> 0x00da }
            android.view.SurfaceHolder r8 = r7.getHolder()     // Catch:{ all -> 0x00da }
            android.view.Surface r6 = r8.getSurface()     // Catch:{ all -> 0x00da }
            goto L_0x002e
        L_0x00da:
            r8 = move-exception
            com.dji.video.framing.internal.decoder.DJIVideoDecoder r9 = r11.mDjiVideoDecoder
            java.util.concurrent.locks.Lock r9 = r9.getRenderManagerReadLock()
            r9.unlock()
            throw r8
        L_0x00e5:
            java.lang.String[] r8 = com.dji.video.framing.utils.DJIVideoUtil.VIDEO_ENCODING_FORMAT
            r9 = 0
            r2 = r8[r9]
            goto L_0x007e
        L_0x00eb:
            java.lang.String r8 = "color-format"
            r9 = 2130708361(0x7f000789, float:1.701803E38)
            r4.setInteger(r8, r9)
            java.lang.String r8 = "flags"
            r9 = 2
            r4.setInteger(r8, r9)
            android.media.MediaCodec r8 = android.media.MediaCodec.createDecoderByType(r2)     // Catch:{ Exception -> 0x015f }
            r11.codec = r8     // Catch:{ Exception -> 0x015f }
            java.lang.String r8 = "AbsDjiDecodeServer"
            java.lang.StringBuilder r9 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x015f }
            r9.<init>()     // Catch:{ Exception -> 0x015f }
            java.lang.String r10 = "initVideoDecoder() create isHevc:"
            java.lang.StringBuilder r9 = r9.append(r10)     // Catch:{ Exception -> 0x015f }
            com.dji.video.framing.internal.decoder.DJIVideoDecoder r10 = r11.mDjiVideoDecoder     // Catch:{ Exception -> 0x015f }
            boolean r10 = r10.isHevcMode()     // Catch:{ Exception -> 0x015f }
            java.lang.StringBuilder r9 = r9.append(r10)     // Catch:{ Exception -> 0x015f }
            java.lang.String r10 = " needUpdateErrorStatus："
            java.lang.StringBuilder r9 = r9.append(r10)     // Catch:{ Exception -> 0x015f }
            java.lang.StringBuilder r9 = r9.append(r13)     // Catch:{ Exception -> 0x015f }
            java.lang.String r10 = " surface:"
            java.lang.StringBuilder r9 = r9.append(r10)     // Catch:{ Exception -> 0x015f }
            java.lang.StringBuilder r9 = r9.append(r6)     // Catch:{ Exception -> 0x015f }
            java.lang.String r9 = r9.toString()     // Catch:{ Exception -> 0x015f }
            r10 = 0
            java.lang.Object[] r10 = new java.lang.Object[r10]     // Catch:{ Exception -> 0x015f }
            com.dji.video.framing.VideoLog.w(r8, r9, r10)     // Catch:{ Exception -> 0x015f }
            android.media.MediaCodec r8 = r11.codec     // Catch:{ Exception -> 0x015f }
            r9 = 0
            r10 = 0
            r8.configure(r4, r6, r9, r10)     // Catch:{ Exception -> 0x015f }
            java.lang.String r8 = "AbsDjiDecodeServer"
            java.lang.String r9 = "initVideoDecoder() configure"
            r10 = 0
            java.lang.Object[] r10 = new java.lang.Object[r10]     // Catch:{ Exception -> 0x015f }
            com.dji.video.framing.VideoLog.w(r8, r9, r10)     // Catch:{ Exception -> 0x015f }
            android.media.MediaCodec r8 = r11.codec     // Catch:{ Exception -> 0x015f }
            if (r8 != 0) goto L_0x0184
            java.lang.String r8 = "AbsDjiDecodeServer"
            java.lang.String r9 = "initVideoDecoder() Can't find video info!"
            r10 = 0
            java.lang.Object[] r10 = new java.lang.Object[r10]     // Catch:{ Exception -> 0x015f }
            com.dji.video.framing.VideoLog.e(r8, r9, r10)     // Catch:{ Exception -> 0x015f }
            goto L_0x0098
        L_0x015f:
            r3 = move-exception
            java.lang.String r8 = "AbsDjiDecodeServer"
            java.lang.String r9 = "initVideoDecoder() start failed, do it again"
            r10 = 0
            java.lang.Object[] r10 = new java.lang.Object[r10]
            com.dji.video.framing.VideoLog.e(r8, r9, r3, r10)
        L_0x016c:
            r8 = 0
            r11.initCodecing = r8
            r8 = -1
            r11.tLastInitOutputTime = r8
            if (r13 == 0) goto L_0x017d
            com.dji.video.framing.internal.decoder.ErrorStatusManager r8 = com.dji.video.framing.internal.decoder.ErrorStatusManager.getInstance()
            r9 = 1
            r8.onErrorStatusChange(r9)
        L_0x017d:
            com.dji.video.framing.internal.decoder.DJIVideoDecoder r8 = r11.mDjiVideoDecoder
            r8.onCodecInit()
            goto L_0x0098
        L_0x0184:
            r11.onCodecConfigured()     // Catch:{ Exception -> 0x015f }
            android.media.MediaCodec r8 = r11.codec     // Catch:{ Exception -> 0x015f }
            r8.start()     // Catch:{ Exception -> 0x015f }
            r11.onCodecStarted()     // Catch:{ Exception -> 0x015f }
            r8 = 1
            r11.decoderConfigure = r8     // Catch:{ Exception -> 0x015f }
            r8 = -1
            r11.tLastFrameIn = r8     // Catch:{ Exception -> 0x015f }
            r8 = -1
            r11.tLastFrameOut = r8     // Catch:{ Exception -> 0x015f }
            if (r12 == 0) goto L_0x016c
            java.lang.String r8 = "AbsDjiDecodeServer"
            java.lang.StringBuilder r9 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x015f }
            r9.<init>()     // Catch:{ Exception -> 0x015f }
            java.lang.String r10 = "initVideoDecoder()  removeMsg stack="
            java.lang.StringBuilder r9 = r9.append(r10)     // Catch:{ Exception -> 0x015f }
            java.lang.String r10 = com.dji.video.framing.utils.DJIVideoUtil.getCurrentStack()     // Catch:{ Exception -> 0x015f }
            java.lang.StringBuilder r9 = r9.append(r10)     // Catch:{ Exception -> 0x015f }
            java.lang.String r9 = r9.toString()     // Catch:{ Exception -> 0x015f }
            r10 = 0
            java.lang.Object[] r10 = new java.lang.Object[r10]     // Catch:{ Exception -> 0x015f }
            com.dji.video.framing.VideoLog.d(r8, r9, r10)     // Catch:{ Exception -> 0x015f }
            r8 = 1
            r11.removeMessages(r8)     // Catch:{ Exception -> 0x015f }
            goto L_0x016c
        */
        throw new UnsupportedOperationException("Method not decompiled: com.dji.video.framing.internal.decoder.AbsDjiDecodeServer.initVideoDecoder(boolean, boolean):void");
    }

    /* access modifiers changed from: protected */
    public void queueToCodec(MediaCodec codec2, byte[] data, int index, int offset, int size, long pts, int flags) {
        VideoLog.d(TAG, "queueToCodec: 0", new Object[0]);
        VideoLog.i(TAG, "feed into codec: index=" + index, new Object[0]);
        codec2.queueInputBuffer(index, offset, size, pts, flags);
    }

    /* access modifiers changed from: package-private */
    public void releaseDecoder() {
        VideoLog.i(TAG, "releaseDecoder() start", new Object[0]);
        if (this.codec != null) {
            try {
                this.codec.flush();
            } catch (Exception e) {
                VideoLog.e(TAG, e);
            }
            try {
                this.codec.release();
            } catch (Exception e2) {
                e2.printStackTrace();
            } finally {
                this.codec = null;
            }
        }
        this.decoderConfigure = false;
        this.iframeIntoCodec = false;
        this.iframeIntoQueue = false;
        clearAndReleaseInputFrame();
    }

    /* access modifiers changed from: protected */
    public void onDecoderOutput(int outputBufferIndex2, MediaCodec.BufferInfo bufferInfo2) throws Exception {
        VideoLog.d(TAG, "onDecoderOutput() outputBufferIndex:" + outputBufferIndex2 + " bufferInfo:" + bufferInfo2, new Object[0]);
        if (outputBufferIndex2 >= 0) {
            onOutputFrame(outputBufferIndex2, bufferInfo2);
        } else if (outputBufferIndex2 == -2) {
            onOutputFormatChanged();
        } else if (this.refreshIframeRetryCount < 6 && this.tLastFrameIn > 0 && this.tLastFrameOut > 0 && this.tLastFrameIn - this.tLastInputRestore > 200 && this.tLastFrameIn - this.tLastFrameOut > ((long) ((this.refreshIframeRetryCount * 500) + 2000))) {
            this.needRefreshIframe = true;
        }
    }

    public double getFrameQueueinIntervalAvgValue() {
        if (this.frameIntervalCalculator == null) {
            return -1.0d;
        }
        return this.frameIntervalCalculator.getAvgValue();
    }

    public void outputQueueEnqueue(VideoFrame frame) {
        if (!this.outputQueue.offer(frame)) {
            VideoFrame dropFrame = this.outputQueue.poll();
            if (dropFrame != null) {
                dropFrame.recycle();
            }
            this.outputQueue.offer(frame);
        }
    }

    public VideoFrame outputQueueDequeue(long comPts) {
        VideoFrame curFrame;
        while (!this.outputQueue.isEmpty() && comPts > 0 && (curFrame = this.outputQueue.poll()) != null) {
            if (curFrame.pts == comPts) {
                return curFrame;
            }
            curFrame.recycle();
        }
        return null;
    }

    public void clearOutputQueue() {
        for (VideoFrame frame : this.outputQueue) {
            if (frame != null) {
                frame.recycle();
            }
            this.outputQueue.clear();
        }
    }

    public long getLastInitCodecOutputTime() {
        return this.tLastInitOutputTime;
    }
}
