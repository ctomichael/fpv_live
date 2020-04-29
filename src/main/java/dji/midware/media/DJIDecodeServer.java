package dji.midware.media;

import android.media.MediaCodec;
import android.media.MediaCrypto;
import android.media.MediaFormat;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.util.Pools;
import android.util.Log;
import android.view.Surface;
import com.dji.video.framing.internal.decoder.common.FrameFovType;
import dji.internal.network.RemoteClient;
import dji.log.DJILog;
import dji.log.DJILogHelper;
import dji.midware.data.config.P3.ProductType;
import dji.midware.data.manager.Dpad.DJISysPropManager;
import dji.midware.data.manager.Dpad.DpadProductManager;
import dji.midware.data.manager.P3.DJIProductManager;
import dji.midware.data.model.P3.DataCameraGetMode;
import dji.midware.data.model.P3.DataCameraGetPushStateInfo;
import dji.midware.media.DJIVideoProcessTimeTest;
import dji.midware.media.DJIVideoUtil;
import dji.midware.stat.StatAverage;
import dji.midware.stat.StatMax;
import dji.midware.stat.StatRate;
import dji.midware.stat.StatService;
import dji.midware.stat.StatSum;
import dji.midware.usb.P3.UsbAccessoryService;
import dji.midware.util.DebugFlag;
import dji.midware.util.DoubleCameraSupportUtil;
import dji.midware.util.save.StreamDataObserver;
import dji.midware.util.save.StreamDelaySaver;
import dji.midware.util.save.StreamSaver;
import dji.midware.util.save.VideoFrameObserver;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.LinkedList;
import java.util.Locale;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

public abstract class DJIDecodeServer extends Handler {
    protected static final boolean DEBUG = false;
    protected static final int DEQUEUE_INPUT_BUFFER = 10000;
    private static final int INPUT_RESTORE_DIFF_THRESHOLD = 200;
    private static final int INPUT_RESTORE_INTERVAL = 1000;
    protected static final int IN_OUT_DIFF_THRESHOLD = 2000;
    public static final int InputFramePoolCapacity = 32;
    public static final int InputQueueCapacity = 15;
    private static final int LOG_I_FRAME_IN_INTERVAL = 1500;
    static final int MSG_INIT = 0;
    static final int MSG_IN_OUT = 2;
    static final int MSG_QUEUEIN = 1;
    static final int MSG_REINIT_KEY_FRAME = 10;
    protected static final int REFRESH_IFRAME_RETRY_INTERVAL = 500;
    private static final int REFRESH_IFRAME_RETRY_NUM = 6;
    private static final String TAG = "DJIDecodeServer";
    LinkedList<Long> bufferChangedQueue = new LinkedList<>();
    MediaCodec.BufferInfo bufferInfo = new MediaCodec.BufferInfo();
    protected MediaCodec codec = null;
    protected int codecDequeueFailCount = 0;
    protected int codecFailResetThreshold = 20;
    private int count = 0;
    @NonNull
    protected DJIVideoDecoder decoder;
    protected boolean decoderConfigure = false;
    private boolean errorStatus = false;
    private AvgCalculator frameIntervalCalculator;
    protected boolean iframeIntoCodec = false;
    boolean iframeIntoQueue = false;
    private boolean initCodecing = false;
    protected BlockingQueue<InputFrame> inputQueue = new LinkedBlockingQueue(15);
    private long lastFrameIndex = -1;
    private long lastInitCodecTime = -1;
    private long lastLogIframeInTime = -1;
    private int lastOutputHeight;
    private int lastOutputWidth;
    private long lastT = 0;
    protected FileOutputStream liveStreamOutputStream;
    private boolean needRefreshIframe = false;
    int outputBufferIndex;
    protected final BlockingQueue<InputFrame> outputQueue = new LinkedBlockingQueue(15);
    private int refreshIframeRetryCount = 0;
    protected boolean saveLiveStream = false;
    protected long start_time = System.currentTimeMillis();
    private long tLastFrameIn = -1;
    private long tLastFrameOut = -1;
    private long tLastInitOutputTime = -1;
    private long tLastInputRestore = -1;
    long timeOutput;
    protected AtomicInteger time_initialized = new AtomicInteger(0);

    /* access modifiers changed from: protected */
    public abstract void handleInOutMsg();

    /* access modifiers changed from: protected */
    public abstract void onCodecConfigured();

    /* access modifiers changed from: protected */
    public abstract void onCodecStarted();

    /* access modifiers changed from: protected */
    public abstract void queueInframe(@NonNull InputFrame inputFrame);

    public DJIDecodeServer(Looper looper, @NonNull DJIVideoDecoder decoder2) {
        super(looper);
        this.decoder = decoder2;
        this.frameIntervalCalculator = new AvgCalculator(20.0d, 70.0d, 30);
    }

    static class InputFrame {
        private static final Pools.SynchronizedPool<InputFrame> sPool = new Pools.SynchronizedPool<>(32);
        public long checkFrameCrc = -1;
        public int checkFrameLen = -1;
        public int checkIndex = -1;
        public boolean checkIsKeyFrame = false;
        protected long codecOutputTime;
        protected long comPts;
        protected long fedIntoCodecTime;
        public FrameFovType fovType = FrameFovType.Unknown;
        protected long frameIndex;
        protected int frameNum;
        protected int height;
        protected long incomingTimeMs;
        protected boolean isKeyFrame;
        private int ppsLen;
        private int ppsPos;
        protected int size;
        private int spsLen;
        private int spsPos;
        public int timeStamp = -1;
        protected byte[] videoBuffer;
        protected int width;
        public int zoomIndex = -1;

        private InputFrame(byte[] videoBuffer2, int size2, long comPts2, long incomingTimeUs, boolean isKeyFrame2, int frameNum2, long frameIndex2, int width2, int height2, int spsPos2, int spsLen2, int ppsPos2, int ppsLen2) {
            updateDate(videoBuffer2, size2, comPts2, incomingTimeUs, isKeyFrame2, frameNum2, frameIndex2, width2, height2, spsPos2, spsLen2, ppsPos2, ppsLen2);
        }

        private void updateDate(byte[] videoBuffer2, int size2, long comPts2, long incomingTimeUs, boolean isKeyFrame2, int frameNum2, long frameIndex2, int width2, int height2, int spsPos2, int spsLen2, int ppsPos2, int ppsLen2) {
            this.videoBuffer = videoBuffer2;
            this.size = size2;
            this.comPts = comPts2;
            this.incomingTimeMs = incomingTimeUs;
            this.isKeyFrame = isKeyFrame2;
            this.frameNum = frameNum2;
            this.frameIndex = frameIndex2;
            this.width = width2;
            this.height = height2;
            this.spsPos = spsPos2;
            this.spsLen = spsLen2;
            this.ppsPos = ppsPos2;
            this.ppsLen = ppsLen2;
            this.fovType = FrameFovType.Unknown;
            this.zoomIndex = -1;
            this.timeStamp = -1;
            this.checkIndex = -1;
            this.checkFrameLen = -1;
            this.checkFrameCrc = -1;
            this.checkIsKeyFrame = false;
        }

        public static InputFrame obtain(byte[] videoBuffer2, int size2, long comPts2, long incomingTimeUs, boolean isKeyFrame2, int frameNum2, long frameIndex2, int width2, int height2, int spsPos2, int spsLen2, int ppsPos2, int ppsLen2) {
            InputFrame instance = sPool.acquire();
            if (instance == null) {
                return new InputFrame(videoBuffer2, size2, comPts2, incomingTimeUs, isKeyFrame2, frameNum2, frameIndex2, width2, height2, spsPos2, spsLen2, ppsPos2, ppsLen2);
            }
            instance.updateDate(videoBuffer2, size2, comPts2, incomingTimeUs, isKeyFrame2, frameNum2, frameIndex2, width2, height2, spsPos2, spsLen2, ppsPos2, ppsLen2);
            return instance;
        }

        public void recycle() {
            sPool.release(this);
        }

        public long getQueueDelay() {
            return this.fedIntoCodecTime - this.incomingTimeMs;
        }

        public long getDecodingDelay() {
            return this.codecOutputTime - this.fedIntoCodecTime;
        }

        public long getTotalDelay() {
            return this.codecOutputTime - this.fedIntoCodecTime;
        }
    }

    /* access modifiers changed from: protected */
    public void clearAndReleaseInputFrame() {
        if (this.inputQueue != null) {
            for (InputFrame elem : this.inputQueue) {
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
                        MediaLogger.i(TAG, "init decoder by command");
                        initVideoDecoder();
                    } catch (Exception e) {
                        MediaLogger.e(TAG, e);
                    }
                    removeCallbacksAndMessages(null);
                    sendEmptyMessageDelayed(2, 1);
                    return;
                case 1:
                    MediaLogger.i(false, TAG, "server on msg_queuein");
                    try {
                        onServerQueuein(msg);
                    } catch (Exception e2) {
                        MediaLogger.e(TAG, e2);
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
                    MediaLogger.e(TAG, "reinit codec");
                    DJIVideoDecoder.log2File("reinit codec");
                    try {
                        this.iframeIntoQueue = false;
                        this.iframeIntoCodec = false;
                        clearAndReleaseInputFrame();
                        DJIVideoDecoder.setErrorStatus(true);
                    } catch (Exception e3) {
                        MediaLogger.e(TAG, e3);
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
            MediaLogger.e(TAG, e4);
        }
        MediaLogger.e(TAG, e4);
    }

    private boolean isGdrStartFrame(InputFrame frame) {
        boolean z = true;
        if (frame == null) {
            return false;
        }
        if (frame.frameNum != 1) {
            z = false;
        }
        return z;
    }

    public byte[] getDefaultKeyFrame(int newWidth, int newHeight) {
        if (!DJIProductManager.getInstance().isRemoteSeted()) {
        }
        try {
            int iframeId = DJIVideoDecoder.getIframeRawId(DJIProductManager.getInstance().getType(), newWidth, newHeight, this.decoder.getStreamSource());
            DJILogHelper.getInstance().LOGD(TAG, "onIframe come in " + newWidth + "x" + newHeight + " isRemotedSeted=" + DJIProductManager.getInstance().isRemoteSeted() + " ptype=" + DJIProductManager.getInstance().getType() + " iFrame=" + iframeId, false, false);
            if (iframeId >= 0) {
                InputStream inputStream = this.decoder.getContext().getResources().openRawResource(iframeId);
                int length = inputStream.available();
                DJILogHelper.getInstance().LOGD(TAG, "iframeId length=" + length, false, true);
                byte[] buffer = new byte[length];
                inputStream.read(buffer);
                inputStream.close();
                return buffer;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /* access modifiers changed from: protected */
    public boolean checkFrame(InputFrame frame) {
        if (!DJIVideoDecoder.needFetchIframe() || this.lastFrameIndex < 0 || frame.isKeyFrame) {
            return true;
        }
        boolean rst = true;
        if (!(((long) frame.checkIndex) == this.lastFrameIndex + 1 || frame.checkIndex == 0)) {
            rst = false;
        }
        if (!rst) {
            DJIVideoDecoder.log2File("server checkFrame: index=" + frame.checkIndex + " lastIndex=" + this.lastFrameIndex + DJILog.getCurrentStack());
        }
        this.lastFrameIndex = (long) frame.checkIndex;
        return rst;
    }

    private void onServerQueuein(Message msg) {
        if (this.decoder.isPause) {
            DJIVideoDecoder.log2File("dec pause");
            return;
        }
        InputFrame newFrame = (InputFrame) msg.obj;
        if (newFrame != null) {
            DJIVideoProcessTimeTest.getInstance().onVideoDataProcessing(DJIVideoProcessTimeTest.VideoProcessPoint.DecoderServerIn, newFrame.videoBuffer, 6);
            long time = System.currentTimeMillis();
            if (time - this.tLastFrameIn > 1000) {
                this.tLastInputRestore = time;
            }
            if (this.frameIntervalCalculator != null && this.tLastFrameIn > 0) {
                this.frameIntervalCalculator.feedData((double) (time - this.tLastFrameIn));
            }
            this.tLastFrameIn = time;
            String name = this.decoder.getStreamSource().name();
            StreamDataObserver.getInstance(StreamDataObserver.ObservingPoint.DecoderQueueIn, this.decoder.getStreamSource()).onDataRecv(StreamDataObserver.ObservingContext.FrameRate, 1).onDataRecv(StreamDataObserver.ObservingContext.ByteRate, (long) newFrame.size).onDataRecv(StreamDataObserver.ObservingContext.Width, (long) newFrame.width).onDataRecv(StreamDataObserver.ObservingContext.Height, (long) newFrame.height).onDataRecv(StreamDataObserver.ObservingContext.KeyframeNum, newFrame.isKeyFrame ? 1 : 0);
            if (newFrame.width > 0 && newFrame.height > 0 && (this.codec == null || newFrame.width != this.decoder.width || newFrame.height != this.decoder.height || this.needRefreshIframe)) {
                MediaLogger.i(TAG, "init decoder for the 1st time or when resolution changes, hashcode: " + this.decoder.hashCode() + ", source: " + this.decoder.getStreamSource().name() + ", old width: " + this.decoder.width + ", new width: " + this.decoder.height);
                this.decoder.width = newFrame.width;
                this.decoder.height = newFrame.height;
                boolean isFirstInit = this.codec == null || !this.iframeIntoQueue || this.needRefreshIframe;
                if (this.codec == null || isNeedResetCodecWhenResolutionChanged() || this.needRefreshIframe) {
                    this.refreshIframeRetryCount++;
                    boolean is230 = DJIProductManager.getInstance().getType() == ProductType.WM230;
                    if (DataCameraGetPushStateInfo.getInstance().getMode() == DataCameraGetMode.MODE.PLAYBACK) {
                        initVideoDecoder(false, !newFrame.isKeyFrame);
                        this.iframeIntoQueue = true;
                    } else if (is230) {
                        initVideoDecoder(isFirstInit, true);
                        this.iframeIntoQueue = !isFirstInit;
                    } else {
                        initVideoDecoder();
                    }
                    this.needRefreshIframe = false;
                }
            }
            InputFrame iFrame = null;
            if (!this.iframeIntoQueue) {
                MediaLogger.i(false, TAG, "server receives a frame. when iframe is not yet set");
                if (time - this.lastInitCodecTime > 1500 && time - this.lastLogIframeInTime > 1500) {
                    DJIVideoDecoder.log2File("no I frame in queue, product=" + DJIProductManager.getInstance().getType() + " toLastInitCodec=" + (time - this.lastInitCodecTime) + "!!!");
                    this.lastLogIframeInTime = time;
                    if (DJIVideoDecoder.needFetchIframe()) {
                        DJIVideoDecoder.setErrorStatus(true);
                    }
                }
                if (isGdrStartFrame(newFrame) || newFrame.isKeyFrame) {
                    byte[] defaultKeyFrame = getDefaultKeyFrame(newFrame.width, newFrame.height);
                    if (newFrame.isKeyFrame) {
                        MediaLogger.i(false, TAG, "The coming frame is I-frame");
                        this.iframeIntoQueue = true;
                    } else if (defaultKeyFrame != null) {
                        MediaLogger.i(false, TAG, "queuein a default iframe");
                        iFrame = InputFrame.obtain(defaultKeyFrame, defaultKeyFrame.length, newFrame.comPts, System.currentTimeMillis(), newFrame.isKeyFrame, 0, newFrame.frameIndex - 1, newFrame.width, newFrame.height, -1, 0, -1, 0);
                        clearAndReleaseInputFrame();
                        this.iframeIntoQueue = true;
                    } else {
                        MediaLogger.i(false, TAG, "The stream is GOP and the coming frame is not I-frame");
                        return;
                    }
                } else {
                    DJILogHelper.getInstance().LOGD(TAG, "the timing for setting iframe has not yet come. frameNum: " + newFrame.frameNum + ", isKeyFrame: " + newFrame.isKeyFrame);
                    return;
                }
            }
            if (iFrame != null) {
                queueInframe(iFrame);
            }
            if (this.iframeIntoQueue) {
                queueInframe(newFrame);
            }
        } else {
            DJIVideoDecoder.log2File("dec frame null");
        }
        StatService.getInstance().postEvent(StatMax.class, "Input_Queue_Size_Max", (float) this.inputQueue.size());
    }

    /* access modifiers changed from: protected */
    public void offerFrameToInputQueue(@NonNull InputFrame inputFrame) {
        if (this.inputQueue.offer(inputFrame)) {
            MediaLogger.i(false, TAG, "put a frame into the Extended-Queue with index=" + inputFrame.frameIndex);
            return;
        }
        if (DJIVideoDecoder.needFetchIframe()) {
            removeMessages(1);
            clearAndReleaseInputFrame();
            DJIVideoDecoder.setErrorStatus(true);
        } else {
            InputFrame dropFrame = this.inputQueue.poll();
            if (dropFrame != null) {
                this.decoder.onFrameDropped();
                dropFrame.recycle();
            }
            this.inputQueue.offer(inputFrame);
        }
        StatService.getInstance().postEvent(StatSum.class, "Input_DROP", 1.0f);
    }

    /* access modifiers changed from: protected */
    public void onDecoderInput(@NonNull InputFrame inputFrame, int inIndex, @NonNull ByteBuffer buffer) {
        if (this.codec != null && !isInitingCodec()) {
            buffer.clear();
            buffer.rewind();
            buffer.put(inputFrame.videoBuffer);
            try {
                inputFrame.fedIntoCodecTime = System.currentTimeMillis();
                long queueingDelay = inputFrame.getQueueDelay();
                StatService.getInstance().postEvent(StatMax.class, "Input_Queue_Delay_Max", (float) queueingDelay);
                if (StreamDelaySaver.IS_SAVE_PACKET_DELAY) {
                    try {
                        StreamDelaySaver.getInstance().frameDelayFile.append((CharSequence) String.format(Locale.US, "[DECODER_FEED_INPUT_BUFFER] pts=%d queueing_delay=%d time=%d\n", Long.valueOf(inputFrame.comPts), Long.valueOf(queueingDelay), Long.valueOf(System.currentTimeMillis())));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                MediaLogger.i(false, TAG, "before queueToCodec");
                queueToCodec(this.codec, inputFrame.videoBuffer, inIndex, 0, inputFrame.size, inputFrame.comPts, 0);
                MediaLogger.i(false, TAG, "after queueToCodec");
                if (inputFrame == this.inputQueue.peek()) {
                    this.inputQueue.poll();
                }
                outputQueueEnqueue(inputFrame);
                this.iframeIntoCodec = true;
            } catch (Exception e2) {
                MediaLogger.e(TAG, "queueInputBuffer error");
                MediaLogger.e(TAG, e2);
                throw e2;
            }
        }
    }

    public void resetCodec() {
        MediaLogger.i(false, TAG, "reset codec");
        DJIVideoDecoder.log2File("reset codec!");
        clearAndReleaseInputFrame();
        this.iframeIntoQueue = false;
        if (DJIVideoDecoder.needFetchIframe()) {
            DJIVideoDecoder.setErrorStatus(true);
        }
        this.codecDequeueFailCount = 0;
        if (this.codec != null) {
            this.codec.flush();
        }
    }

    private void LOG(String log) {
    }

    private boolean isNeedResetCodecWhenResolutionChanged() {
        ProductType type = DJIProductManager.getInstance().getType();
        if (type == ProductType.Pomato || type == ProductType.Orange2 || type == ProductType.Potato || type == ProductType.M200 || type == ProductType.M210 || type == ProductType.M210RTK || type == ProductType.Mammoth || type == ProductType.PomatoSDR || type == ProductType.PM420 || type == ProductType.PM420PRO || type == ProductType.PM420PRO_RTK || type == ProductType.WM240 || type == ProductType.WM245 || DJIVideoDecoder.getIframeRawId(type, 0, 0) < 0) {
            return false;
        }
        return true;
    }

    /* access modifiers changed from: protected */
    public void onOutputFormatChanged() {
        if (this.codec != null) {
            MediaFormat newFormat = this.codec.getOutputFormat();
            MediaLogger.show(TAG, newFormat.toString());
            DJIVideoUtil.Resolution res = DJIVideoUtil.getResolutionReliably(newFormat);
            this.decoder.outputWidth = res.width;
            this.decoder.outputHeight = res.height;
            this.decoder.outputColorFormat = newFormat.getInteger("color-format");
            MediaLogger.show(TAG, String.format(Locale.US, "Format changed. color=%d, width=%d, height=%d", Integer.valueOf(this.decoder.outputColorFormat), Integer.valueOf(this.decoder.outputWidth), Integer.valueOf(this.decoder.outputHeight)));
            Log.e(TAG, "dequeueOutputBuffer INFO_OUTPUT_FORMAT_CHANGED");
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
        if (this.codec != null && !isInitingCodec()) {
            this.timeOutput = System.currentTimeMillis();
            this.tLastFrameOut = this.timeOutput;
            this.refreshIframeRetryCount = 0;
            if (this.tLastInitOutputTime < 0) {
                this.tLastInitOutputTime = this.timeOutput;
            }
            this.decoder.statusMonitor.freshDecodeStatusWithOK(this.decoder.statusMonitor.connectLosedelayMillis);
            updateYUVDataCallback(outputIndex, bufferInfo2);
            VideoFrameObserver.getInstance().saveTimeStamp(VideoFrameObserver.TimeSavingPoint.OutputFrame, bufferInfo2.presentationTimeUs, true);
            if (this.decoder.getRenderManager() == null) {
                Log.i(TAG, "mGLRenderManager == null || renderer==null");
                this.codec.releaseOutputBuffer(outputIndex, false);
                return;
            }
            this.decoder.latestPTS = bufferInfo2 == null ? this.decoder.latestPTS : bufferInfo2.presentationTimeUs;
            if (this.decoder.latestPTS != 0) {
                InputFrame outputFrame = outputQueueDequeue(this.decoder.latestPTS);
                if (outputFrame != null) {
                    if (outputFrame.timeStamp >= 0) {
                        this.decoder.latestFrameTimestamp = outputFrame.timeStamp;
                    }
                    if (!(outputFrame.width == this.lastOutputWidth && outputFrame.height == this.lastOutputHeight)) {
                        this.decoder.invokeCallbackResetVideoSurface();
                        this.lastOutputWidth = outputFrame.width;
                        this.lastOutputHeight = outputFrame.height;
                    }
                    this.decoder.invokeOutputCallback(outputFrame);
                    DJIVideoProcessTimeTest.getInstance().onVideoDataProcessing(DJIVideoProcessTimeTest.VideoProcessPoint.DecoderCodecOut, outputFrame.videoBuffer, 6, true);
                    outputFrame.recycle();
                }
                long frameIndex = (long) DJIVideoUtil.getFrameIndex(this.decoder.latestPTS);
                long timeIntoCodec = DJIVideoUtil.getPtsMs(this.decoder.latestPTS);
                int frameNum = DJIVideoUtil.getFrameNum(this.decoder.latestPTS);
                long decodingDelay = System.currentTimeMillis() - timeIntoCodec;
                MediaLogger.i(TAG, "Decoding_delay=" + decodingDelay + " frameIndex=" + frameIndex + " frameNum=" + frameNum + " comPts=" + this.decoder.latestPTS + " queueLen=" + this.inputQueue.size());
                StatService.getInstance().postEvent(StatAverage.class, "Decoding_delay_avg", (float) decodingDelay);
                StatService.getInstance().postEvent(StatMax.class, "Decoding_delay_MAX", (float) decodingDelay);
            }
            printRate();
            long time_beforeReleaseOutputBuffer = System.currentTimeMillis();
            this.codec.releaseOutputBuffer(outputIndex, true);
            this.decoder.invokeCallbackOneFrameComeIn();
            MediaLogger.i(false, TAG, "after mCallback.oneFrameComeIn");
            StatService.getInstance().postEvent(StatAverage.class, "Output_dur_avg", (float) (System.currentTimeMillis() - time_beforeReleaseOutputBuffer));
            StatService.getInstance().postEvent(StatRate.class, "Output_FPS", 1.0f);
        }
    }

    private void updateYUVDataCallback(int outputIndex, MediaCodec.BufferInfo bufferInfo2) {
        ByteBuffer yuvDataBuf;
        if (outputIndex >= 0 && this.decoder.getYuvDataCallBack() != null) {
            if (Build.VERSION.SDK_INT < 23) {
                yuvDataBuf = this.codec.getOutputBuffers()[outputIndex];
            } else {
                yuvDataBuf = this.codec.getOutputBuffer(outputIndex);
            }
            yuvDataBuf.position(bufferInfo2.offset);
            yuvDataBuf.limit(bufferInfo2.size - bufferInfo2.offset);
            this.decoder.getYuvDataCallBack().onYuvDataReceived(yuvDataBuf, bufferInfo2.size - bufferInfo2.offset, this.decoder.outputWidth, this.decoder.outputHeight);
        }
    }

    public boolean isInitingCodec() {
        return this.initCodecing;
    }

    /* access modifiers changed from: protected */
    public void initVideoDecoder() {
        initVideoDecoder(true, true);
    }

    /* access modifiers changed from: protected */
    public void initVideoDecoder(boolean needRemoveMsgs, boolean needUpdateErrorStatus) {
        if (DJIVideoDecoder.TEST_RESTART_MECHANISM) {
            this.time_initialized.addAndGet(1);
            MediaLogger.show("\n ... time_initialized = " + this.time_initialized + "\n ");
        }
        DJIVideoDecoder.log2File("init codec!");
        this.lastInitCodecTime = System.currentTimeMillis();
        this.decoder.getRenderManagerReadLock().lock();
        try {
            if (this.decoder.getRenderManager() == null) {
                DJILogHelper.getInstance().LOGE(TAG, "call initVideoDecoder with renderManager==null");
                return;
            }
            Surface surface = this.decoder.getRenderManager().getInputSurface();
            this.decoder.getRenderManagerReadLock().unlock();
            this.initCodecing = true;
            if (this.codec != null) {
                releaseDecoder();
            }
            this.iframeIntoCodec = false;
            this.iframeIntoQueue = false;
            this.lastFrameIndex = -1;
            MediaFormat format = MediaFormat.createVideoFormat(DJIVideoUtil.VIDEO_ENCODING_FORMAT[0], this.decoder.width, this.decoder.height);
            if (surface == null) {
                format.setInteger("color-format", 19);
            } else {
                format.setInteger("color-format", 2130708361);
            }
            format.setInteger(RemoteClient.FLAG_SUB_URI, 2);
            if (Build.VERSION.SDK_INT < 21) {
                format.setInteger("color-format", 21);
            }
            try {
                if (DpadProductManager.getInstance().supportSystemVideoSmooth() && !DpadProductManager.getInstance().isRM500()) {
                    DJILog.logWriteE("DpadFpsManager", "initVideoDecoder, source = " + this.decoder, "DpadFpsManager", new Object[0]);
                    int camId = 0;
                    if (DJISysPropManager.getEnableGo4Fps()) {
                        if (this.decoder.getStreamSource() == UsbAccessoryService.VideoStreamSource.Camera) {
                            camId = 1;
                        } else if (this.decoder.getStreamSource() == UsbAccessoryService.VideoStreamSource.Fpv) {
                            camId = 2;
                        } else if (this.decoder.getStreamSource() == UsbAccessoryService.VideoStreamSource.SecondaryCamera) {
                            camId = 3;
                        }
                    }
                    DJISysPropManager.setFpv(camId);
                }
                this.codec = MediaCodec.createDecoderByType(DJIVideoUtil.VIDEO_ENCODING_FORMAT[0]);
                Log.e("VideoDecoder", "initVideoDecoder create");
                if (!DJIVideoDecoder.TEST_RESTART_MECHANISM || this.time_initialized.get() % 3 != 0) {
                    this.codec.configure(format, surface, (MediaCrypto) null, 0);
                }
                Log.e("VideoDecoder", "initVideoDecoder configure");
                if (this.codec == null) {
                    Log.e("VideoDecoder", "Can't find video info!");
                    return;
                }
                onCodecConfigured();
                this.codec.start();
                onCodecStarted();
                this.decoderConfigure = true;
                this.tLastFrameIn = -1;
                this.tLastFrameOut = -1;
                if (needRemoveMsgs) {
                    removeMessages(1);
                }
                this.initCodecing = false;
                this.tLastInitOutputTime = -1;
                if (needUpdateErrorStatus) {
                    DJIVideoDecoder.setErrorStatus(true);
                }
            } catch (Exception e) {
                e.printStackTrace();
                Log.e(TAG, "start failed, do it again");
            }
        } finally {
            this.decoder.getRenderManagerReadLock().unlock();
        }
    }

    /* access modifiers changed from: protected */
    public void queueToCodec(MediaCodec codec2, byte[] data, int index, int offset, int size, long pts, int flags) {
        MediaLogger.i(false, TAG, "feed into codec: index=" + index);
        StreamDataObserver.getInstance(StreamDataObserver.ObservingPoint.DecoderQueueToCodec, this.decoder.getStreamSource()).onDataRecv(StreamDataObserver.ObservingContext.FrameRate, 1).onDataRecv(StreamDataObserver.ObservingContext.ByteRate, (long) size);
        DJIVideoProcessTimeTest.getInstance().onVideoDataProcessing(DJIVideoProcessTimeTest.VideoProcessPoint.DecoderCodecIn, data, offset + 6);
        if (this.decoder.getStreamSource() == UsbAccessoryService.VideoStreamSource.Camera) {
            VideoFrameObserver.getInstance().saveTimeStamp(VideoFrameObserver.TimeSavingPoint.BeforeQueueToCodec, data, offset, pts, false);
        }
        codec2.queueInputBuffer(index, offset, size, pts, flags);
        if (StreamSaver.SAVE_videoCodecQueueIn_Open) {
            StreamSaver.getInstance("dji_video_codecqueuein_" + this.decoder.getStreamSource().name()).write(data, 0, size);
        }
    }

    /* access modifiers changed from: package-private */
    public void releaseDecoder() {
        DJIVideoDecoder.log2File("release codec! " + DJILog.getCurrentStack());
        MediaLogger.i(false, TAG, "releaseDecoder() start");
        DebugFlag.printfLog(TAG, "releaseDecoder() start");
        if (this.codec != null) {
            try {
                this.codec.flush();
            } catch (Exception e) {
                MediaLogger.e(TAG, e);
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
        DebugFlag.printfLog(TAG, "releaseDecoder() end");
    }

    /* access modifiers changed from: protected */
    public void onDecoderOutput(int outputBufferIndex2, MediaCodec.BufferInfo bufferInfo2) throws Exception {
        DataCameraGetMode.MODE cameraMode;
        if (outputBufferIndex2 >= 0) {
            onOutputFrame(outputBufferIndex2, bufferInfo2);
        } else if (outputBufferIndex2 == -2) {
            onOutputFormatChanged();
        } else if (this.refreshIframeRetryCount < 6 && this.tLastFrameIn > 0 && this.tLastFrameOut > 0 && this.tLastFrameIn - this.tLastInputRestore > 200 && this.tLastFrameIn - this.tLastFrameOut > ((long) ((this.refreshIframeRetryCount * 500) + 2000))) {
            if (DoubleCameraSupportUtil.SupportDoubleCamera) {
                if (this.decoder.getStreamSource() == UsbAccessoryService.VideoStreamSource.Camera) {
                    if (DoubleCameraSupportUtil.isPrimaryVideoFromPrimaryCamera()) {
                        cameraMode = DataCameraGetPushStateInfo.getInstance().getMode(0);
                    } else {
                        cameraMode = DataCameraGetPushStateInfo.getInstance().getMode(2);
                    }
                } else if (DoubleCameraSupportUtil.isSecondaryVideoFromSecondaryCamera()) {
                    cameraMode = DataCameraGetPushStateInfo.getInstance().getMode(2);
                } else {
                    this.needRefreshIframe = false;
                    return;
                }
                if (cameraMode == DataCameraGetMode.MODE.TAKEPHOTO || cameraMode == DataCameraGetMode.MODE.RECORD) {
                    this.needRefreshIframe = true;
                    return;
                }
                return;
            }
            DataCameraGetMode.MODE cameraMode2 = DataCameraGetPushStateInfo.getInstance().getMode();
            if (cameraMode2 == DataCameraGetMode.MODE.TAKEPHOTO || cameraMode2 == DataCameraGetMode.MODE.RECORD) {
                this.needRefreshIframe = true;
            }
        }
    }

    public double getFrameQueueinIntervalAvgValue() {
        if (this.frameIntervalCalculator == null) {
            return -1.0d;
        }
        return this.frameIntervalCalculator.getAvgValue();
    }

    public void outputQueueEnqueue(InputFrame frame) {
        if (!this.outputQueue.offer(frame)) {
            InputFrame dropFrame = this.outputQueue.poll();
            if (dropFrame != null) {
                dropFrame.recycle();
            }
            this.outputQueue.offer(frame);
        }
    }

    public InputFrame outputQueueDequeue(long comPts) {
        InputFrame curFrame;
        while (!this.outputQueue.isEmpty() && (curFrame = this.outputQueue.poll()) != null) {
            if (curFrame.comPts == comPts) {
                return curFrame;
            }
            curFrame.recycle();
        }
        return null;
    }

    public void clearOutputQueue() {
        for (InputFrame frame : this.outputQueue) {
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
