package dji.midware.media;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Keep;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.Surface;
import com.dji.video.framing.DJIVideoCodecInnerManager;
import com.dji.video.framing.internal.decoder.decoderinterface.IDJIVideoDecoder;
import com.dji.video.framing.internal.opengl.surface.SurfaceInterface;
import com.drew.metadata.exif.makernotes.LeicaMakernoteDirectory;
import com.drew.metadata.exif.makernotes.OlympusMakernoteDirectory;
import com.drew.metadata.photoshop.PhotoshopDirectory;
import dji.fieldAnnotation.EXClassNullAway;
import dji.log.DJILog;
import dji.log.DJILogHelper;
import dji.log.DJILogPaths;
import dji.log.DJILogUtils;
import dji.log.FpsLog;
import dji.logic.manager.DJIUSBWifiSwitchManager;
import dji.logic.utils.DJICameraSupportUtil;
import dji.midware.R;
import dji.midware.component.DJIComponentManager;
import dji.midware.data.config.P3.ProductType;
import dji.midware.data.manager.P3.DJIProductManager;
import dji.midware.data.manager.P3.DJIVideoDataEvent;
import dji.midware.data.manager.P3.DJIVideoEvent;
import dji.midware.data.manager.P3.ServiceManager;
import dji.midware.data.model.P3.DataCameraGetMode;
import dji.midware.data.model.P3.DataCameraGetPushStateInfo;
import dji.midware.data.model.P3.DataOldSpecialControl;
import dji.midware.interfaces.DJIDataCallBack;
import dji.midware.interfaces.RecvDataCallback;
import dji.midware.interfaces.YuvDataCallback;
import dji.midware.media.DJIDecodeServer;
import dji.midware.media.transcode.online.DecoderTranscodeListener;
import dji.midware.stat.StatRate;
import dji.midware.stat.StatService;
import dji.midware.usb.P3.UsbAccessoryService;
import dji.midware.util.DebugFlag;
import dji.midware.util.DjiSharedPreferencesManager;
import dji.midware.util.DoubleCameraSupportUtil;
import dji.midware.util.save.StreamSaver;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import org.greenrobot.eventbus.EventBus;

@Keep
@EXClassNullAway
public class DJIVideoDecoder implements IDJIVideoDecoder {
    private static final boolean DEBUG_SYNC = false;
    public static final float FOCUS_PEAKING_HIGH = 4.0f;
    public static final float FOCUS_PEAKING_LOW = 1.5f;
    public static final float FOCUS_PEAKING_NORMAL = 2.7f;
    public static final String KEY_PEAKING_FOCUS_THRESHOLD = "key_peaking_focus_threshold";
    public static final String KEY_PEAKING_FOCUS_THRESHOLD_FPV = "key_peaking_focus_threshold_fpv";
    private static final long LOG_FREQ_LIMIT = 5000;
    private static final int SAVE_LOG_INTERVAL = 3000;
    private static int SEND_ERR_STATUS_INTERVAL = 100;
    private static final String TAG = "DJIVideoDecoder";
    private static final String TAG_Input = "Decoder_Input";
    private static final String TAG_Server = "Decoder_Server";
    static boolean TEST_RESTART_MECHANISM = false;
    public static final int connectLosedelay = 2000;
    static SimpleDateFormat formatter = new SimpleDateFormat(DJILogUtils.FORMAT_2);
    private static long lastErrStatusChangeTime = 0;
    private static long lastLogTime = 0;
    private static long lastSendErrorStatusTime = 0;
    private static SaveLogHelper logHelper;
    private static volatile DataCameraGetPushStateInfo stateInfo;
    private static volatile DJIUSBWifiSwitchManager switchManager;
    public static boolean testDisconnect;
    protected boolean DEBUG_CLIENT;
    protected boolean DEBUG_SERVER;
    protected boolean DEBUG_SERVER_VERBOSE;
    private CountDownLatch cl;
    @NonNull
    private DJIDecodeClient client;
    private Context context;
    private VideoDecoderEventListener eventListener;
    public int height;
    private InvokeOutputCallbackTask invokeOutputCallbackTask;
    /* access modifiers changed from: private */
    public boolean isLiveStream;
    boolean isPause;
    /* access modifiers changed from: private */
    public boolean isStop;
    Handler jpegRenderHandler;
    public int latestFrameTimestamp;
    public long latestPTS;
    public Object listenerSync;
    private RecvDataCallback mCallback;
    private YuvDataCallback mYuvDataCallback;
    /* access modifiers changed from: private */
    public Handler mainHandler;
    public DecoderOutputCallback outputCallback;
    public int outputColorFormat;
    public int outputHeight;
    public int outputWidth;
    private int playbackFrameRate;
    public byte[] pps_header;
    private SurfaceInterface renderManager;
    private ReentrantReadWriteLock renderManagerLock;
    private ReentrantReadWriteLock.ReadLock renderManagerRLock;
    private ReentrantReadWriteLock.WriteLock renderManagerWLock;
    private Bitmap screenBitmap;
    /* access modifiers changed from: private */
    public UsbAccessoryService.VideoStreamSource source;
    public byte[] sps_header;
    StatusMonitor statusMonitor;
    /* access modifiers changed from: private */
    public Surface surfaceDrawTo;
    public DecoderTranscodeListener transcodeListener;
    public int width;

    @Keep
    public enum VideoDecodeStatus {
        Unhealthy,
        Healthy
    }

    public interface VideoDecoderEventListener {
        void onFrameDropped();
    }

    public void setPause(boolean pause) {
        log2File("set pause=" + pause + " stack:" + DJILog.getCurrentStack());
        boolean needFetchIframe = !pause && needFetchIframe() && this.isPause != pause;
        this.isPause = pause;
        if (needFetchIframe) {
            setErrorStatus(true);
        }
    }

    public int getPlaybackFrameRate() {
        return this.playbackFrameRate;
    }

    public void setPlaybackFrameRate(int playbackFrameRate2) {
        this.playbackFrameRate = playbackFrameRate2;
    }

    public void resetCodec() {
        this.client.server.resetCodec();
    }

    public static void log2File(String log) {
        if (logHelper == null) {
            logHelper = new SaveLogHelper(DJILogPaths.LOG_DECODER, 3000);
        }
        logHelper.log(log);
    }

    public static void log2FileWithFreqLimit(String log) {
        long time = System.currentTimeMillis();
        if (time - lastLogTime > LOG_FREQ_LIMIT) {
            log2File(log);
            lastLogTime = time;
        }
    }

    public DJIVideoDecoder(Context context2, SurfaceInterface renderManager2) {
        this(context2, true, renderManager2);
    }

    public DJIVideoDecoder(Context context2, boolean isLiveStream2, SurfaceInterface renderManager2) {
        this(context2, isLiveStream2, renderManager2, UsbAccessoryService.VideoStreamSource.Camera);
    }

    public DJIVideoDecoder(Context context2, boolean isLiveStream2, SurfaceInterface renderManager2, UsbAccessoryService.VideoStreamSource source2) {
        this.DEBUG_CLIENT = false;
        this.DEBUG_SERVER = false;
        this.DEBUG_SERVER_VERBOSE = false;
        this.isStop = false;
        this.mainHandler = new Handler();
        this.renderManager = null;
        this.renderManagerLock = new ReentrantReadWriteLock(false);
        this.renderManagerRLock = this.renderManagerLock.readLock();
        this.renderManagerWLock = this.renderManagerLock.writeLock();
        this.transcodeListener = null;
        this.outputCallback = null;
        this.listenerSync = new Object();
        this.width = 16;
        this.height = 9;
        this.outputWidth = 0;
        this.outputHeight = 0;
        this.sps_header = null;
        this.pps_header = null;
        this.outputColorFormat = -1;
        this.latestFrameTimestamp = -1;
        this.playbackFrameRate = -1;
        this.statusMonitor = new StatusMonitor();
        this.isLiveStream = true;
        this.isPause = false;
        this.cl = null;
        this.screenBitmap = null;
        log2File("init decoder islive=" + isLiveStream2 + " rm=" + renderManager2 + " source=" + source2);
        MediaLogger.show(TAG, "new a DJIVideoDecoder = " + toString());
        this.context = context2;
        this.isLiveStream = isLiveStream2;
        this.source = source2;
        this.client = new DJIDecodeClient(this);
        if (source2 == UsbAccessoryService.VideoStreamSource.Camera) {
            ServiceManager.getInstance().setVideoDecoder(this);
        } else if (source2 == UsbAccessoryService.VideoStreamSource.Fpv) {
            ServiceManager.getInstance().setSecondaryVideoDecoder(this);
        }
        this.statusMonitor.freshDecodeStatus(5000);
        this.statusMonitor.freshDataStatus(5000);
        setSurface(renderManager2);
    }

    public boolean needLowFrequencyForSmoothing() {
        int indexInProtocol;
        if (!DoubleCameraSupportUtil.SupportDoubleCamera) {
            indexInProtocol = 0;
        } else if (getStreamSource() == UsbAccessoryService.VideoStreamSource.Camera) {
            if (DoubleCameraSupportUtil.isPrimaryVideoFromPrimaryCamera()) {
                indexInProtocol = 0;
            } else {
                indexInProtocol = 2;
            }
        } else if (!DoubleCameraSupportUtil.isSecondaryVideoFromSecondaryCamera()) {
            return false;
        } else {
            indexInProtocol = 2;
        }
        DataCameraGetPushStateInfo.CameraType cameraType = DataCameraGetPushStateInfo.getInstance().getCameraType(indexInProtocol);
        DataCameraGetMode.MODE cameraMode = DataCameraGetPushStateInfo.getInstance().getMode(indexInProtocol);
        if (cameraType == DataCameraGetPushStateInfo.CameraType.DJICameraTypeGD600 && cameraMode == DataCameraGetMode.MODE.PLAYBACK) {
            return true;
        }
        return false;
    }

    public boolean isPlayBackMode() {
        int indexInProtocol;
        if (!DoubleCameraSupportUtil.SupportDoubleCamera) {
            indexInProtocol = 0;
        } else if (getStreamSource() == UsbAccessoryService.VideoStreamSource.Camera) {
            if (DoubleCameraSupportUtil.isPrimaryVideoFromPrimaryCamera()) {
                indexInProtocol = 0;
            } else {
                indexInProtocol = 2;
            }
        } else if (!DoubleCameraSupportUtil.isSecondaryVideoFromSecondaryCamera()) {
            return false;
        } else {
            indexInProtocol = 2;
        }
        if (DataCameraGetPushStateInfo.getInstance().getMode(indexInProtocol) == DataCameraGetMode.MODE.PLAYBACK) {
            return true;
        }
        return false;
    }

    public Context getContext() {
        return this.context;
    }

    /* access modifiers changed from: package-private */
    public SurfaceInterface getRenderManager() {
        return this.renderManager;
    }

    /* access modifiers changed from: package-private */
    public Lock getRenderManagerReadLock() {
        return this.renderManagerRLock;
    }

    public void switchSource(UsbAccessoryService.VideoStreamSource source2) {
        this.source = source2;
        if (source2 == UsbAccessoryService.VideoStreamSource.Camera) {
            if (ServiceManager.getInstance().getSecondaryVideoDecoder() == this) {
                ServiceManager.getInstance().setSecondaryVideoDecoder(null);
            }
            ServiceManager.getInstance().setVideoDecoder(this);
        } else if (source2 == UsbAccessoryService.VideoStreamSource.Fpv) {
            if (ServiceManager.getInstance().getDecoder() == this) {
                ServiceManager.getInstance().setVideoDecoder(null);
            }
            ServiceManager.getInstance().setSecondaryVideoDecoder(this);
        }
        resetKeyFrame();
    }

    public boolean isDecoderOK() {
        return this.statusMonitor.isDecoderOK();
    }

    public boolean isHasVideoData() {
        return this.statusMonitor.isHasVideoData();
    }

    @Keep
    class StatusMonitor {
        private static final int MSG_ID_CONNECT_LOSE = 1;
        private static final int MSG_ID_NO_VIDEO_DATA = 3;
        int connectLosedelayMillis = DJIVideoDecoder.connectLosedelay;
        protected VideoDecodeStatus curDecodeHealthStatus = VideoDecodeStatus.Healthy;
        protected DJIVideoEvent curEvent = DJIVideoEvent.ConnectLose;
        protected DJIVideoDataEvent curVideoEvent = DJIVideoDataEvent.NoVideo;
        private Handler handler = new Handler(new Handler.Callback() {
            /* class dji.midware.media.DJIVideoDecoder.StatusMonitor.AnonymousClass1 */

            public boolean handleMessage(Message msg) {
                if (!DJIVideoDecoder.this.isStop) {
                    switch (msg.what) {
                        case 1:
                            StatusMonitor.this.setVideoEvent(DJIVideoEvent.ConnectLose);
                            if (DJIVideoDecoder.this.source == UsbAccessoryService.VideoStreamSource.Camera) {
                                EventBus.getDefault().post(StatusMonitor.this.curEvent);
                                break;
                            }
                            break;
                        case 3:
                            StatusMonitor.this.setVideoDataEvent(DJIVideoDataEvent.NoVideo);
                            EventBus.getDefault().post(StatusMonitor.this.curVideoEvent);
                            break;
                    }
                }
                return false;
            }
        });

        StatusMonitor() {
        }

        /* access modifiers changed from: private */
        public void release() {
            if (this.handler != null) {
                this.handler.removeCallbacksAndMessages(null);
                this.handler = null;
            }
        }

        /* access modifiers changed from: private */
        public boolean isHasVideoData() {
            return this.curVideoEvent == DJIVideoDataEvent.HasVideo;
        }

        /* access modifiers changed from: private */
        public boolean isDecoderOK() {
            return this.curEvent == DJIVideoEvent.ConnectOK;
        }

        /* access modifiers changed from: private */
        public void setVideoEvent(DJIVideoEvent videoEvent) {
            this.curEvent = videoEvent;
            checkVideoDecodeStatus();
        }

        /* access modifiers changed from: private */
        public void setVideoDataEvent(DJIVideoDataEvent videoDataEvent) {
            this.curVideoEvent = videoDataEvent;
            checkVideoDecodeStatus();
        }

        private void checkVideoDecodeStatus() {
            VideoDecodeStatus newStatus;
            if (DJIVideoDecoder.TEST_RESTART_MECHANISM) {
                MediaLogger.show(DJIVideoDecoder.TAG, "check: curEvent=" + this.curEvent + " curVideoEvent=" + this.curVideoEvent);
            }
            if (this.curEvent == DJIVideoEvent.ConnectLose && this.curVideoEvent == DJIVideoDataEvent.HasVideo) {
                newStatus = VideoDecodeStatus.Unhealthy;
            } else {
                newStatus = VideoDecodeStatus.Healthy;
            }
            if (newStatus != this.curDecodeHealthStatus) {
                this.curDecodeHealthStatus = newStatus;
                EventBus.getDefault().post(this.curDecodeHealthStatus);
            }
        }

        /* access modifiers changed from: private */
        public void setConnectLosedelay(int millis) {
            this.connectLosedelayMillis = millis;
            freshDecodeStatus(this.connectLosedelayMillis);
            freshDataStatus(this.connectLosedelayMillis);
        }

        /* access modifiers changed from: private */
        public void resumeStatusCheck() {
            freshDecodeStatus(this.connectLosedelayMillis);
            freshDataStatus(this.connectLosedelayMillis);
        }

        /* access modifiers changed from: private */
        public void freshDecodeStatus(int time) {
            if (DJIVideoDecoder.this.isLiveStream) {
                if (this.handler != null) {
                    this.handler.removeMessages(1);
                }
                if (this.curEvent == DJIVideoEvent.ConnectOK && this.handler != null) {
                    this.handler.sendEmptyMessageDelayed(1, (long) time);
                }
            }
        }

        /* access modifiers changed from: package-private */
        public void freshDecodeStatusWithOK(int time) {
            if (DJIVideoDecoder.this.isLiveStream) {
                if (this.curEvent != DJIVideoEvent.ConnectOK) {
                    setVideoEvent(DJIVideoEvent.ConnectOK);
                    DJIVideoDecoder.this.initPeakingFocusState();
                    if (DJIVideoDecoder.this.source == UsbAccessoryService.VideoStreamSource.Camera) {
                        EventBus.getDefault().post(this.curEvent);
                    }
                }
                freshDecodeStatus(time);
            }
        }

        /* access modifiers changed from: private */
        public void pauseStatusCheck() {
            if (this.handler != null) {
                this.handler.removeMessages(1);
                this.handler.removeMessages(3);
            }
        }

        /* access modifiers changed from: private */
        public void freshDataStatus(int time) {
            if (DJIVideoDecoder.this.isLiveStream) {
                if (this.handler != null) {
                    this.handler.removeMessages(3);
                }
                if (this.curVideoEvent == DJIVideoDataEvent.HasVideo && this.handler != null) {
                    this.handler.sendEmptyMessageDelayed(3, (long) time);
                }
            }
        }

        /* access modifiers changed from: private */
        public void freshDataStatusWithOK(int time) {
            if (DJIVideoDecoder.this.isLiveStream) {
                if (this.curVideoEvent != DJIVideoDataEvent.HasVideo) {
                    setVideoDataEvent(DJIVideoDataEvent.HasVideo);
                    EventBus.getDefault().post(this.curVideoEvent);
                }
                freshDataStatus(time);
            }
        }
    }

    public void setEventListener(VideoDecoderEventListener eventListener2) {
        this.eventListener = eventListener2;
    }

    /* access modifiers changed from: package-private */
    public void onFrameDropped() {
        if (this.eventListener != null) {
            this.eventListener.onFrameDropped();
        }
    }

    public void resetToManager() {
        ServiceManager.getInstance().setVideoDecoder(this);
    }

    public void resetKeyFrame() {
        this.client.resetKeyFrame();
    }

    public void setRecvDataCallBack(RecvDataCallback mCallback2) {
        this.mCallback = mCallback2;
    }

    public void setYuvDataCallBack(YuvDataCallback mCallback2) {
        this.mYuvDataCallback = mCallback2;
    }

    public void setOutputCallback(DecoderOutputCallback callback) {
        this.outputCallback = callback;
    }

    @Keep
    private class InvokeOutputCallbackTask implements Runnable {
        private DecoderOutputCallback callback;
        private int frameNum;
        private boolean isKeyFrame;

        private InvokeOutputCallbackTask() {
        }

        public void invoke(DJIDecodeServer.InputFrame frame, DecoderOutputCallback callback2) {
            if (callback2 != null) {
                this.callback = callback2;
                this.frameNum = frame.frameNum;
                this.isKeyFrame = frame.isKeyFrame;
                DJIVideoDecoder.this.mainHandler.post(this);
            }
        }

        public void run() {
            if (this.callback != null) {
                this.callback.onFrameOutput(this.frameNum, this.isKeyFrame);
            }
        }
    }

    /* access modifiers changed from: package-private */
    public void invokeOutputCallback(DJIDecodeServer.InputFrame frame) {
        if (this.outputCallback != null) {
            if (this.invokeOutputCallbackTask == null) {
                this.invokeOutputCallbackTask = new InvokeOutputCallbackTask();
            }
            this.invokeOutputCallbackTask.invoke(frame, this.outputCallback);
        }
    }

    public YuvDataCallback getYuvDataCallBack() {
        return this.mYuvDataCallback;
    }

    /* access modifiers changed from: package-private */
    public void invokeCallbackResetVideoSurface() {
        if (this.mCallback != null) {
            this.mCallback.resetVideoSurface(this.width, this.height);
        }
    }

    /* access modifiers changed from: package-private */
    public void invokeCallbackOneFrameComeIn() {
        if (this.mCallback != null) {
            this.mCallback.oneFrameComeIn();
        }
    }

    public static int getIframeRawId(ProductType pType, int width2, int height2) {
        return getIframeRawId(pType, width2, height2, UsbAccessoryService.VideoStreamSource.Camera);
    }

    public static int getIframeRawId(ProductType pType, int width2, int height2, UsbAccessoryService.VideoStreamSource streamSource) {
        if (stateInfo == null) {
            synchronized (DJIVideoDecoder.class) {
                if (stateInfo == null) {
                    stateInfo = DataCameraGetPushStateInfo.getInstance();
                }
            }
        }
        if (switchManager == null) {
            synchronized (DJIVideoDecoder.class) {
                if (switchManager == null) {
                    switchManager = DJIUSBWifiSwitchManager.getInstance();
                }
            }
        }
        int iframeId = R.raw.iframe_1280x720_ins;
        switch (pType) {
            case litchiS:
            case litchiC:
            case OrangeCV600:
                if (width2 != 960) {
                    if (width2 != 640) {
                        DJILog.i(TAG, "Selected Iframe=iframe_1280x720_3s", new Object[0]);
                        iframeId = R.raw.iframe_1280x720_3s;
                        break;
                    } else {
                        iframeId = R.raw.iframe_640x368_osmo_gop;
                        break;
                    }
                } else {
                    DJILog.i(TAG, "Selected Iframe=iframe_960x720_3s", new Object[0]);
                    iframeId = R.raw.iframe_960x720_3s;
                    break;
                }
            case P34K:
                switch (width2) {
                    case OlympusMakernoteDirectory.TAG_PREVIEW_IMAGE /*640*/:
                        DJILog.i(TAG, "Selected Iframe=iframe_640x480", new Object[0]);
                        iframeId = R.raw.iframe_640x480;
                        break;
                    case 848:
                        DJILog.i(TAG, "Selected Iframe=iframe_848x480", new Object[0]);
                        iframeId = R.raw.iframe_848x480;
                        break;
                    case 896:
                        iframeId = R.raw.iframe_896x480;
                        break;
                    case 960:
                        iframeId = R.raw.iframe_960x720_3s;
                        break;
                    default:
                        DJILog.i(TAG, "Selected Iframe=iframe_1280x720_3s", new Object[0]);
                        iframeId = R.raw.iframe_1280x720_3s;
                        break;
                }
            case Longan:
                if (stateInfo.getVerstion() < 4) {
                    iframeId = R.raw.iframe_1280x720_ins;
                    break;
                } else {
                    iframeId = -1;
                    break;
                }
            case LonganZoom:
                if (width2 == 960) {
                    iframeId = R.raw.iframe_960x720_osmo_gop;
                } else if (width2 == 1280) {
                    iframeId = R.raw.iframe_1280x720_osmo_gop;
                } else if (width2 == 640) {
                    iframeId = R.raw.iframe_640x368_osmo_gop;
                } else {
                    iframeId = R.raw.iframe_1280x720_3s;
                }
                DJILogHelper.getInstance().LOGD(TAG, "longan zoom width:" + width2);
                break;
            case LonganPro:
            case LonganRaw:
                iframeId = R.raw.iframe_1280x720_ins;
                break;
            case KumquatX:
            case KumquatS:
            case WM240:
            case WM245:
                if (!DJIUSBWifiSwitchManager.getInstance().isWifiConnected()) {
                    DJILog.i(TAG, "Selected Iframe=-1", new Object[0]);
                    iframeId = -1;
                    break;
                } else {
                    iframeId = R.raw.iframe_1280x720_wm220;
                    break;
                }
            case Mammoth:
                switch (width2) {
                    case 1024:
                        iframeId = R.raw.iframe_1024x768_wm100;
                        break;
                    case 1280:
                        iframeId = R.raw.iframe_1280x720_p4;
                        break;
                    default:
                        iframeId = R.raw.iframe_1280x720_p4;
                        break;
                }
            case WM230:
                switch (height2) {
                    case 720:
                        DJILogHelper.getInstance().LOGD(TAG, "Selected Iframe=iframe_1280x720_wm230");
                        iframeId = R.raw.iframe_1280x720_wm230;
                        break;
                    case 960:
                        DJILogHelper.getInstance().LOGD(TAG, "Selected Iframe=iframe_1280x960_wm230");
                        iframeId = R.raw.iframe_1280x960_wm230;
                        break;
                    default:
                        DJILogHelper.getInstance().LOGD(TAG, "Selected Iframe=iframe_1280x720_wm230(default)");
                        iframeId = R.raw.iframe_1280x720_wm230;
                        break;
                }
            case Tomato:
                DJILog.i(TAG, "Selected Iframe=iframe_1280x720_p4", new Object[0]);
                iframeId = R.raw.iframe_1280x720_p4;
                break;
            case Pomato:
            case Potato:
                switch (width2) {
                    case 960:
                        iframeId = R.raw.iframe_p4p_720_4x3;
                        break;
                    case PhotoshopDirectory.TAG_PATH_SELECTION_STATE /*1088*/:
                        iframeId = R.raw.iframe_p4p_720_3x2;
                        break;
                    case 1280:
                        iframeId = R.raw.iframe_p4p_720_16x9;
                        break;
                    case 1344:
                        iframeId = R.raw.iframe_p4p_1344x720;
                        break;
                    case 1440:
                        iframeId = R.raw.iframe_1440x1088_wm620;
                        break;
                    case 1632:
                        iframeId = R.raw.iframe_1632x1080_wm620;
                        break;
                    case 1920:
                        switch (height2) {
                            case LeicaMakernoteDirectory.TAG_CAMERA_TEMPERATURE /*800*/:
                                iframeId = R.raw.iframe_1920x800_wm620;
                                break;
                            case 1024:
                                iframeId = R.raw.iframe_1920x1024_wm620;
                                break;
                            default:
                                iframeId = R.raw.iframe_1920x1088_wm620;
                                break;
                        }
                    default:
                        iframeId = R.raw.iframe_p4p_720_16x9;
                        break;
                }
            case PM820:
            case PM820PRO:
                DataCameraGetPushStateInfo.CameraType cameraType = DataCameraGetPushStateInfo.getInstance().getCameraType();
                if (width2 != 720 || height2 != 480) {
                    if (width2 != 720 || height2 != 576) {
                        if (width2 != 1280 || height2 != 720) {
                            if (width2 != 1920 || (height2 != 1080 && height2 != 1088)) {
                                if (width2 != 1080 || height2 != 720) {
                                    if (width2 != 960 || height2 != 720) {
                                        iframeId = -1;
                                        break;
                                    } else {
                                        iframeId = R.raw.iframe_960x720_3s;
                                        break;
                                    }
                                } else {
                                    iframeId = R.raw.iframe_1080x720_gd600;
                                    break;
                                }
                            } else {
                                iframeId = R.raw.iframe_1920x1080_m600;
                                break;
                            }
                        } else if (cameraType != DataCameraGetPushStateInfo.CameraType.DJICameraTypeGD600) {
                            if (cameraType != DataCameraGetPushStateInfo.CameraType.DJICameraTypeCV600) {
                                if (cameraType != DataCameraGetPushStateInfo.CameraType.DJICameraTypeFC350) {
                                    iframeId = R.raw.iframe_1280x720_m600;
                                    break;
                                } else {
                                    iframeId = R.raw.iframe_1280x720_ins;
                                    break;
                                }
                            } else {
                                iframeId = R.raw.iframe_1280x720_osmo_gop;
                                break;
                            }
                        } else {
                            iframeId = R.raw.iframe_gd600_1280x720;
                            break;
                        }
                    } else {
                        iframeId = R.raw.iframe_720x576_m600;
                        break;
                    }
                } else {
                    iframeId = R.raw.iframe_720x480_m600;
                    break;
                }
                break;
            case N1:
                if (DataCameraGetPushStateInfo.getInstance().getCameraType() == DataCameraGetPushStateInfo.CameraType.DJICameraTypeGD600) {
                    if (width2 != 1280 || height2 != 720) {
                        iframeId = R.raw.iframe_1080x720_gd600;
                        break;
                    } else {
                        iframeId = R.raw.iframe_gd600_1280x720;
                        break;
                    }
                } else {
                    iframeId = R.raw.iframe_1280x720_ins;
                    break;
                }
            case M210:
            case M210RTK:
            case PM420PRO:
            case PM420PRO_RTK:
                DJIComponentManager.CameraComponentType cameraType2 = null;
                if (streamSource.equals(UsbAccessoryService.VideoStreamSource.Camera)) {
                    cameraType2 = DoubleCameraSupportUtil.isPrimaryVideoFromPrimaryCamera() ? DoubleCameraSupportUtil.getMainCameraType() : DoubleCameraSupportUtil.getSecondaryCameraType();
                } else if (streamSource.equals(UsbAccessoryService.VideoStreamSource.Fpv) && DoubleCameraSupportUtil.isSecondaryVideoFromSecondaryCamera()) {
                    cameraType2 = DoubleCameraSupportUtil.getSecondaryCameraType();
                }
                if (cameraType2 == null || !cameraType2.equals(DJIComponentManager.CameraComponentType.GD600)) {
                    if (width2 != 640 || height2 != 368) {
                        if (width2 != 608 || height2 != 448) {
                            if (width2 != 720 || height2 != 480) {
                                if (width2 != 1280 || height2 != 720) {
                                    if (width2 != 1080 || height2 != 720) {
                                        if (width2 != 1088 || height2 != 720) {
                                            if (width2 != 960 || height2 != 720) {
                                                if (width2 != 1360 || height2 != 720) {
                                                    if (width2 != 1344 || height2 != 720) {
                                                        if (width2 != 1440 || height2 != 1088) {
                                                            if (width2 != 1632 || height2 != 1080) {
                                                                if (width2 != 1760 || height2 != 720) {
                                                                    if (width2 != 1920 || height2 != 800) {
                                                                        if (width2 != 1920 || height2 != 1024) {
                                                                            if (width2 != 1920 || height2 != 1088) {
                                                                                if (width2 == 1920 && height2 == 1440) {
                                                                                    DJILog.i(TAG, "Selected Iframe=iframe_1920x1440_wm620", new Object[0]);
                                                                                    iframeId = R.raw.iframe_1920x1440_wm620;
                                                                                    break;
                                                                                }
                                                                            } else {
                                                                                DJILog.i(TAG, "Selected Iframe=iframe_1920x1080_wm620", new Object[0]);
                                                                                iframeId = R.raw.iframe_1920x1088_wm620;
                                                                                break;
                                                                            }
                                                                        } else {
                                                                            DJILog.i(TAG, "Selected Iframe=iframe_1920x1024_wm620", new Object[0]);
                                                                            iframeId = R.raw.iframe_1920x1024_wm620;
                                                                            break;
                                                                        }
                                                                    } else {
                                                                        DJILog.i(TAG, "Selected Iframe=iframe_1920x800_wm620", new Object[0]);
                                                                        iframeId = R.raw.iframe_1920x800_wm620;
                                                                        break;
                                                                    }
                                                                } else {
                                                                    DJILog.i(TAG, "Selected Iframe=iframe_1760x720_wm620", new Object[0]);
                                                                    iframeId = R.raw.iframe_1760x720_wm620;
                                                                    break;
                                                                }
                                                            } else {
                                                                DJILog.i(TAG, "Selected Iframe=iframe_1632x1080_wm620", new Object[0]);
                                                                iframeId = R.raw.iframe_1632x1080_wm620;
                                                                break;
                                                            }
                                                        } else {
                                                            DJILog.i(TAG, "Selected Iframe=iframe_1440x1088_wm620", new Object[0]);
                                                            iframeId = R.raw.iframe_1440x1088_wm620;
                                                            break;
                                                        }
                                                    } else {
                                                        DJILog.i(TAG, "Selected Iframe=iframe_1344x720_wm620", new Object[0]);
                                                        iframeId = R.raw.iframe_1344x720_wm620;
                                                        break;
                                                    }
                                                } else {
                                                    DJILog.i(TAG, "Selected Iframe=iframe_1360x720_wm620", new Object[0]);
                                                    iframeId = R.raw.iframe_1360x720_wm620;
                                                    break;
                                                }
                                            } else {
                                                DJILog.i(TAG, "Selected Iframe=iframe_960x720_wm620", new Object[0]);
                                                iframeId = R.raw.iframe_960x720_wm620;
                                                break;
                                            }
                                        } else {
                                            DJILog.i(TAG, "Selected Iframe=iframe_1088x720_wm620", new Object[0]);
                                            iframeId = R.raw.iframe_1088x720_wm620;
                                            break;
                                        }
                                    } else {
                                        DJILog.i(TAG, "Selected Iframe=iframe_1080x720_wm620", new Object[0]);
                                        iframeId = R.raw.iframe_1080x720_wm620;
                                        break;
                                    }
                                } else {
                                    DJILog.i(TAG, "Selected Iframe=iframe_1280x720_wm620", new Object[0]);
                                    iframeId = R.raw.iframe_1280x720_wm620;
                                    break;
                                }
                            } else {
                                DJILog.i(TAG, "Selected Iframe=iframe_720x480_wm620", new Object[0]);
                                iframeId = R.raw.iframe_720x480_wm620;
                                break;
                            }
                        } else {
                            DJILog.i(TAG, "Selected Iframe=iframe_608x448_wm620", new Object[0]);
                            iframeId = R.raw.iframe_608x448_wm620;
                            break;
                        }
                    } else {
                        DJILog.i(TAG, "Selected Iframe=iframe_640x368_wm620", new Object[0]);
                        iframeId = R.raw.iframe_640x368_wm620;
                        break;
                    }
                } else {
                    iframeId = R.raw.iframe_1080x720_gd600;
                    break;
                }
                break;
            case M200:
            case PM420:
            case Orange2:
                DataCameraGetPushStateInfo.CameraType cameraType3 = DataCameraGetPushStateInfo.getInstance().getCameraType();
                if (streamSource.equals(UsbAccessoryService.VideoStreamSource.Fpv) || cameraType3 != DataCameraGetPushStateInfo.CameraType.DJICameraTypeGD600) {
                    if (width2 != 640 || height2 != 368) {
                        if (width2 != 608 || height2 != 448) {
                            if (width2 != 720 || height2 != 480) {
                                if (width2 != 1280 || height2 != 720) {
                                    if (width2 != 1080 || height2 != 720) {
                                        if (width2 != 1088 || height2 != 720) {
                                            if (width2 != 960 || height2 != 720) {
                                                if (width2 != 1360 || height2 != 720) {
                                                    if (width2 != 1344 || height2 != 720) {
                                                        if (width2 != 1440 || height2 != 1088) {
                                                            if (width2 != 1632 || height2 != 1080) {
                                                                if (width2 != 1760 || height2 != 720) {
                                                                    if (width2 != 1920 || height2 != 800) {
                                                                        if (width2 != 1920 || height2 != 1024) {
                                                                            if (width2 != 1920 || height2 != 1088) {
                                                                                if (width2 == 1920 && height2 == 1440) {
                                                                                    DJILog.i(TAG, "Selected Iframe=iframe_1920x1440_wm620", new Object[0]);
                                                                                    iframeId = R.raw.iframe_1920x1440_wm620;
                                                                                    break;
                                                                                }
                                                                            } else {
                                                                                DJILog.i(TAG, "Selected Iframe=iframe_1920x1080_wm620", new Object[0]);
                                                                                iframeId = R.raw.iframe_1920x1088_wm620;
                                                                                break;
                                                                            }
                                                                        } else {
                                                                            DJILog.i(TAG, "Selected Iframe=iframe_1920x1024_wm620", new Object[0]);
                                                                            iframeId = R.raw.iframe_1920x1024_wm620;
                                                                            break;
                                                                        }
                                                                    } else {
                                                                        DJILog.i(TAG, "Selected Iframe=iframe_1920x800_wm620", new Object[0]);
                                                                        iframeId = R.raw.iframe_1920x800_wm620;
                                                                        break;
                                                                    }
                                                                } else {
                                                                    DJILog.i(TAG, "Selected Iframe=iframe_1760x720_wm620", new Object[0]);
                                                                    iframeId = R.raw.iframe_1760x720_wm620;
                                                                    break;
                                                                }
                                                            } else {
                                                                DJILog.i(TAG, "Selected Iframe=iframe_1632x1080_wm620", new Object[0]);
                                                                iframeId = R.raw.iframe_1632x1080_wm620;
                                                                break;
                                                            }
                                                        } else {
                                                            DJILog.i(TAG, "Selected Iframe=iframe_1440x1088_wm620", new Object[0]);
                                                            iframeId = R.raw.iframe_1440x1088_wm620;
                                                            break;
                                                        }
                                                    } else {
                                                        DJILog.i(TAG, "Selected Iframe=iframe_1344x720_wm620", new Object[0]);
                                                        iframeId = R.raw.iframe_1344x720_wm620;
                                                        break;
                                                    }
                                                } else {
                                                    DJILog.i(TAG, "Selected Iframe=iframe_1360x720_wm620", new Object[0]);
                                                    iframeId = R.raw.iframe_1360x720_wm620;
                                                    break;
                                                }
                                            } else {
                                                DJILog.i(TAG, "Selected Iframe=iframe_960x720_wm620", new Object[0]);
                                                iframeId = R.raw.iframe_960x720_wm620;
                                                break;
                                            }
                                        } else {
                                            DJILog.i(TAG, "Selected Iframe=iframe_1088x720_wm620", new Object[0]);
                                            iframeId = R.raw.iframe_1088x720_wm620;
                                            break;
                                        }
                                    } else {
                                        DJILog.i(TAG, "Selected Iframe=iframe_1080x720_wm620", new Object[0]);
                                        iframeId = R.raw.iframe_1080x720_wm620;
                                        break;
                                    }
                                } else {
                                    DJILog.i(TAG, "Selected Iframe=iframe_1280x720_wm620", new Object[0]);
                                    iframeId = R.raw.iframe_1280x720_wm620;
                                    break;
                                }
                            } else {
                                DJILog.i(TAG, "Selected Iframe=iframe_720x480_wm620", new Object[0]);
                                iframeId = R.raw.iframe_720x480_wm620;
                                break;
                            }
                        } else {
                            DJILog.i(TAG, "Selected Iframe=iframe_608x448_wm620", new Object[0]);
                            iframeId = R.raw.iframe_608x448_wm620;
                            break;
                        }
                    } else {
                        DJILog.i(TAG, "Selected Iframe=iframe_640x368_wm620", new Object[0]);
                        iframeId = R.raw.iframe_640x368_wm620;
                        break;
                    }
                } else {
                    iframeId = R.raw.iframe_1080x720_gd600;
                    break;
                }
                break;
            case PomatoSDR:
            case PomatoRTK:
                iframeId = -1;
                break;
            default:
                DJILog.i(TAG, "Selected Iframe=iframe_1280x720_ins", new Object[0]);
                iframeId = R.raw.iframe_1280x720_ins;
                break;
        }
        if (StreamSaver.videoDebugEnabledUsedInSDK) {
            DJILog.d(StreamSaver.VIDEO_LOG_TAG, "get I frame:" + iframeId, new Object[0]);
        }
        return iframeId;
    }

    private boolean isNeedResetCodecWhenResolutionChanged() {
        ProductType type = DJIProductManager.getInstance().getType();
        if (type == ProductType.Pomato || type == ProductType.Orange2 || ProductType.Potato == type || type == ProductType.M200 || type == ProductType.M210 || type == ProductType.M210RTK || type == ProductType.PM420 || type == ProductType.PM420PRO || type == ProductType.PM420PRO_RTK || type == ProductType.Mammoth || type == ProductType.PomatoSDR || getIframeRawId(type, 0, 0) < 0) {
            return false;
        }
        return true;
    }

    public void queueInputBuffer(byte[] videoBuffer, int size, long com_pts, int frameNum, boolean isKeyFrame, long frameIndex, int spsPos, int spsLen, int ppsPos, int ppsLen, int newWidth, int newHeight) {
        this.statusMonitor.freshDataStatusWithOK(this.statusMonitor.connectLosedelayMillis);
        StatService.getInstance().postEvent(StatRate.class, "Input_Receiver_FPS", 1.0f);
        if (testDisconnect || this.renderManager == null) {
            if (this.DEBUG_CLIENT) {
                DJILogHelper.getInstance().LOGD(TAG_Input, String.format(Locale.US, "testDisconnect=%s, renderManager=%s", Boolean.valueOf(testDisconnect), this.renderManager), false, false);
            }
            log2FileWithFreqLimit("dec initing");
        } else if (this.isStop) {
            if (this.DEBUG_CLIENT) {
                DJILogHelper.getInstance().LOGD(TAG_Input, String.format(Locale.US, "isStop=%s", Boolean.valueOf(this.isStop)), false, true);
            }
            log2FileWithFreqLimit("dec stopped");
        } else {
            if (spsPos >= 0 && spsPos + spsLen <= videoBuffer.length) {
                this.sps_header = Arrays.copyOfRange(videoBuffer, spsPos, spsPos + spsLen);
            }
            if (ppsPos > 0 && ppsPos + ppsLen <= videoBuffer.length) {
                this.pps_header = Arrays.copyOfRange(videoBuffer, ppsPos, ppsPos + ppsLen);
            }
            if (this.DEBUG_CLIENT) {
                MediaLogger.i(TAG_Input, "queueInputBuffer an input frame. frameNum=" + frameNum + " frameIndex=" + frameIndex + " at " + System.currentTimeMillis());
            }
            this.client.queueInFrame(videoBuffer, size, frameNum, isKeyFrame, frameIndex, com_pts, newWidth, newHeight, spsPos, spsLen, ppsPos, ppsLen);
        }
    }

    /* JADX INFO: finally extract failed */
    public void displayJpegFrame(byte[] jpegBuffer, int offset, int size) {
        this.renderManagerRLock.lock();
        try {
            if (this.surfaceDrawTo != null || this.renderManager == null) {
                MediaLogger.e(TAG, "displayJpegFrame renderManager==null");
                this.renderManagerRLock.unlock();
                return;
            }
            this.surfaceDrawTo = this.renderManager.getInputSurface();
            this.renderManagerRLock.unlock();
            if (this.jpegRenderHandler != null || this.context == null) {
                MediaLogger.e(TAG, "displayJpegFrame context==null");
                return;
            }
            this.jpegRenderHandler = new Handler(this.context.getMainLooper());
            final Bitmap bitmap = BitmapFactory.decodeByteArray(jpegBuffer, offset, size, new BitmapFactory.Options());
            if (bitmap == null) {
                MediaLogger.e(TAG, "displayJpegFrame bitmap decoding failed");
            } else {
                this.jpegRenderHandler.post(new Runnable() {
                    /* class dji.midware.media.DJIVideoDecoder.AnonymousClass1 */

                    public void run() {
                        Canvas canvas = DJIVideoDecoder.this.surfaceDrawTo.lockCanvas(null);
                        if (canvas == null) {
                            MediaLogger.e(DJIVideoDecoder.TAG, "displayJpegFrame handler lockCanvas failed");
                        } else {
                            canvas.drawBitmap(bitmap, (Rect) null, new Rect(0, 0, canvas.getWidth(), canvas.getHeight()), (Paint) null);
                            DJIVideoDecoder.this.surfaceDrawTo.unlockCanvasAndPost(canvas);
                        }
                        bitmap.recycle();
                    }
                });
            }
        } catch (Throwable th) {
            this.renderManagerRLock.unlock();
            throw th;
        }
    }

    public void ConnectStatus(int status) {
        if (status == 0) {
            DJILogHelper.getInstance().LOGD("", " ADB_CONN_ERR连接失败或者错误");
        }
        if (status == 1) {
            DJILogHelper.getInstance().LOGD("", " ADB_CONN_ACCEPT连接成功");
        }
    }

    public void pauseStatusCheck() {
        this.statusMonitor.pauseStatusCheck();
    }

    public void freshDecodeStatus(int time) {
        this.statusMonitor.freshDataStatus(time);
    }

    public void setConnectLosedelay(int millis) {
        this.statusMonitor.setConnectLosedelay(millis);
    }

    public void resumeStatusCheck() {
        this.statusMonitor.resumeStatusCheck();
    }

    public void debugLOG(String s) {
        DJILogHelper.getInstance().LOGD("", "JNI:" + s);
    }

    public int getVideoWidth() {
        return this.width;
    }

    public int getVideoHeight() {
        return this.height;
    }

    public void initDecoder() {
        if (this.client.server != null) {
            this.client.server.sendEmptyMessage(0);
        }
    }

    public void release() {
        log2File("release decoder");
        if (this.screenBitmap != null) {
            this.screenBitmap.recycle();
            this.screenBitmap = null;
        }
        this.isStop = true;
        this.statusMonitor.release();
        if (this.source == UsbAccessoryService.VideoStreamSource.Camera) {
            if (this == ServiceManager.getInstance().getDecoder()) {
                ServiceManager.getInstance().setVideoDecoder(null);
            }
        } else if (this == ServiceManager.getInstance().getSecondaryVideoDecoder()) {
            ServiceManager.getInstance().setSecondaryVideoDecoder(null);
        }
        this.client.stopServer();
        this.mCallback = null;
        DebugFlag.printfLog(TAG, "stopVideoDecoder()");
    }

    public void recvTimeout() {
        Log.i(TAG, "recvTimeout()");
    }

    public void setSurface(SurfaceInterface newRenderManager) {
        log2File("set surface=" + newRenderManager + " stack:" + DJILog.getCurrentStack());
        this.renderManagerWLock.lock();
        try {
            if (this.renderManager != null) {
                this.client.stopServer();
                this.renderManager = null;
            }
            if (newRenderManager != null) {
                this.renderManager = newRenderManager;
                this.client.startServer();
                initPeakingFocusState();
                Log.e(TAG, "start DJIDecodeInoutThread() create");
            }
        } finally {
            this.renderManagerWLock.unlock();
        }
    }

    public void setDecoderTranscodeListener(DecoderTranscodeListener listener) {
        synchronized (this.listenerSync) {
            this.transcodeListener = listener;
        }
    }

    public boolean isSurfaceAvailable() {
        return this.renderManager != null;
    }

    public UsbAccessoryService.VideoStreamSource getStreamSource() {
        return this.source;
    }

    public void setPeakFocusEnable(boolean enable) {
        setPeakFocusEnable(enable, false);
    }

    public void setPeakFocusEnable(boolean enable, boolean isTemporary) {
        this.renderManagerRLock.lock();
        try {
            if (isSurfaceAvailable()) {
                this.renderManager.setPeakFocusEnable(enable);
            }
            if (!enable && !isTemporary) {
                if (equals(ServiceManager.getInstance().getDecoder())) {
                    DjiSharedPreferencesManager.putFloat(this.context, "key_peaking_focus_threshold", 0.0f);
                } else {
                    DjiSharedPreferencesManager.putFloat(this.context, "key_peaking_focus_threshold_fpv", 0.0f);
                }
            }
        } finally {
            this.renderManagerRLock.unlock();
        }
    }

    public boolean getPeakFocusEnable() {
        this.renderManagerRLock.lock();
        try {
            if (isSurfaceAvailable()) {
                return this.renderManager.getPeakFocusEnable();
            }
            this.renderManagerRLock.unlock();
            return false;
        } finally {
            this.renderManagerRLock.unlock();
        }
    }

    /* JADX INFO: finally extract failed */
    public void setPeakFocusThreshold(float threshold) {
        if (((double) (threshold - 0.0f)) < 0.001d) {
            setPeakFocusEnable(false);
        } else {
            setPeakFocusEnable(true);
        }
        this.renderManagerRLock.lock();
        try {
            if (isSurfaceAvailable()) {
                this.renderManager.setPeakFocusThreshold(threshold);
            }
            this.renderManagerRLock.unlock();
            if (equals(ServiceManager.getInstance().getDecoder())) {
                DjiSharedPreferencesManager.putFloat(this.context, "key_peaking_focus_threshold", threshold);
            } else {
                DjiSharedPreferencesManager.putFloat(this.context, "key_peaking_focus_threshold_fpv", threshold);
            }
        } catch (Throwable th) {
            this.renderManagerRLock.unlock();
            throw th;
        }
    }

    public float getPeakFocusThreshold() {
        this.renderManagerRLock.lock();
        try {
            if (isSurfaceAvailable()) {
                return this.renderManager.getPeakFocusThreshold();
            }
            this.renderManagerRLock.unlock();
            return -1.0f;
        } finally {
            this.renderManagerRLock.unlock();
        }
    }

    /* access modifiers changed from: private */
    public void initPeakingFocusState() {
        if (DoubleCameraSupportUtil.SupportDoubleCamera) {
            if (this.source == UsbAccessoryService.VideoStreamSource.Camera) {
                if (DoubleCameraSupportUtil.isPrimaryVideoFromPrimaryCamera()) {
                    initPeakingFocusState(0);
                } else {
                    initPeakingFocusState(2);
                }
            } else if (DoubleCameraSupportUtil.isSecondaryVideoFromSecondaryCamera()) {
                initPeakingFocusState(2);
            } else {
                setPeakFocusEnable(false);
            }
        } else if (this.source == UsbAccessoryService.VideoStreamSource.Camera) {
            initPeakingFocusState(0);
        } else {
            setPeakFocusEnable(false);
        }
    }

    private void initPeakingFocusState(int index) {
        boolean isInShootOrRecord;
        float threshold;
        if (DataCameraGetPushStateInfo.getInstance().getMode(index) == DataCameraGetMode.MODE.TAKEPHOTO || DataCameraGetPushStateInfo.getInstance().getMode(index) == DataCameraGetMode.MODE.RECORD) {
            isInShootOrRecord = true;
        } else {
            isInShootOrRecord = false;
        }
        if (!DJICameraSupportUtil.isSupportPeakingFocus(DataCameraGetPushStateInfo.getInstance().getCameraType(index))) {
            return;
        }
        if (isInShootOrRecord) {
            if (equals(ServiceManager.getInstance().getDecoder())) {
                threshold = DjiSharedPreferencesManager.getFloat(this.context, "key_peaking_focus_threshold", 0.0f);
            } else {
                threshold = DjiSharedPreferencesManager.getFloat(this.context, "key_peaking_focus_threshold_fpv", 0.0f);
            }
            if (Math.abs(threshold - 0.0f) < 0.001f) {
                setPeakFocusEnable(false);
                return;
            }
            setPeakFocusEnable(true);
            setPeakFocusThreshold(threshold);
            return;
        }
        setPeakFocusEnable(false, true);
    }

    /* JADX DEBUG: Failed to find minimal casts for resolve overloaded methods, cast all args instead
     method: ClspMth{android.graphics.Bitmap.createBitmap(android.graphics.Bitmap, int, int, int, int, android.graphics.Matrix, boolean):android.graphics.Bitmap}
     arg types: [android.graphics.Bitmap, int, int, int, int, android.graphics.Matrix, int]
     candidates:
      ClspMth{android.graphics.Bitmap.createBitmap(android.util.DisplayMetrics, int[], int, int, int, int, android.graphics.Bitmap$Config):android.graphics.Bitmap}
      ClspMth{android.graphics.Bitmap.createBitmap(android.graphics.Bitmap, int, int, int, int, android.graphics.Matrix, boolean):android.graphics.Bitmap} */
    public static Bitmap zoomImg(Bitmap bm, int newWidth, int newHeight) {
        int width2 = bm.getWidth();
        int height2 = bm.getHeight();
        Matrix matrix = new Matrix();
        matrix.postScale(((float) newWidth) / ((float) width2), ((float) newHeight) / ((float) height2));
        return Bitmap.createBitmap(bm, 0, 0, width2, height2, matrix, true);
    }

    public static boolean needFetchIframe() {
        return DJIProductManager.getInstance().getType() == ProductType.WM240 || DJIProductManager.getInstance().getType() == ProductType.WM245;
    }

    public boolean isRealTimeStream() {
        int indexInProtocol;
        if (!DoubleCameraSupportUtil.SupportDoubleCamera) {
            indexInProtocol = 0;
        } else if (this.source == UsbAccessoryService.VideoStreamSource.Camera) {
            if (DoubleCameraSupportUtil.isPrimaryVideoFromPrimaryCamera()) {
                indexInProtocol = 0;
            } else {
                indexInProtocol = 2;
            }
        } else if (!DoubleCameraSupportUtil.isSecondaryVideoFromSecondaryCamera()) {
            return false;
        } else {
            indexInProtocol = 2;
        }
        DataCameraGetMode.MODE cameraMode = DataCameraGetPushStateInfo.getInstance().getMode(indexInProtocol);
        if (cameraMode == DataCameraGetMode.MODE.TAKEPHOTO || cameraMode == DataCameraGetMode.MODE.RECORD) {
            return true;
        }
        return false;
    }

    public static void setErrorStatus(boolean newStatus) {
        long time = System.currentTimeMillis();
        if (newStatus != getErrorStatus()) {
            lastErrStatusChangeTime = time;
            DataOldSpecialControl.getInstance().setStreamErrorStatus(newStatus).start((DJIDataCallBack) null);
            lastSendErrorStatusTime = time;
        } else if (time - lastSendErrorStatusTime > ((long) SEND_ERR_STATUS_INTERVAL)) {
            DataOldSpecialControl.getInstance().setStreamErrorStatus(newStatus).start((DJIDataCallBack) null);
            lastSendErrorStatusTime = time;
        }
    }

    public static boolean getErrorStatus() {
        return DataOldSpecialControl.getInstance().isStreamErrorStatus();
    }

    public byte[] getRgbaData(int width2, int height2) {
        this.renderManagerRLock.lock();
        try {
            if (this.renderManager != null) {
                Log.d(TAG, "getRgbaData: ColorDisplayView get rgba");
                return this.renderManager.getRgbaData(width2, height2);
            }
            Log.d(TAG, "getRgbaData: ColorDisplayView render manager null");
            this.renderManagerRLock.unlock();
            return null;
        } finally {
            this.renderManagerRLock.unlock();
        }
    }

    public byte[] getYuvData(int width2, int height2) {
        this.renderManagerRLock.lock();
        try {
            if (this.renderManager != null) {
                Log.d(TAG, "getRgbaData: ColorDisplayView get rgba");
            } else {
                Log.d(TAG, "getRgbaData: ColorDisplayView render manager null");
                this.renderManagerRLock.unlock();
            }
            return null;
        } finally {
            this.renderManagerRLock.unlock();
        }
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
        this.renderManagerRLock.lock();
        try {
            if (this.renderManager == null) {
                return null;
            }
            this.renderManagerRLock.unlock();
            try {
                this.cl.await(3, TimeUnit.SECONDS);
            } catch (Exception e) {
            }
            return this.screenBitmap;
        } finally {
            this.renderManagerRLock.unlock();
        }
    }

    private void fpsLog(String log) {
        FpsLog.getInstance().d(log);
    }

    public static String getStringDate() {
        return formatter.format(new Date());
    }

    public void setSecondaryOutputSurface(String name, Object surface, int width2, int height2, int vp_x, int rotateDegree) {
        this.renderManagerRLock.lock();
        try {
            if (this.renderManager != null) {
                this.renderManager.setSecondaryOutputSurface(name, surface, width2, height2, vp_x, rotateDegree);
            }
        } finally {
            this.renderManagerRLock.unlock();
        }
    }

    public void setSecondaryOutputInterval(String name, int interval) {
        this.renderManagerRLock.lock();
        try {
            if (this.renderManager != null) {
                this.renderManager.setSecondaryOutputInterval(name, interval);
            }
        } finally {
            this.renderManagerRLock.unlock();
        }
    }

    public int getSecondaryOutputInterval(String name) {
        this.renderManagerRLock.lock();
        try {
            if (this.renderManager != null) {
                return this.renderManager.getSecondaryOutputInterval(name);
            }
            this.renderManagerRLock.unlock();
            return -1;
        } finally {
            this.renderManagerRLock.unlock();
        }
    }

    public long getLastExtraDrawTime(String key) {
        this.renderManagerRLock.lock();
        try {
            if (this.renderManager != null) {
                return this.renderManager.getLastExtraDrawTime(key);
            }
            this.renderManagerRLock.unlock();
            return -1;
        } finally {
            this.renderManagerRLock.unlock();
        }
    }

    public long getLastFrameOutTime() {
        if (this.client.server != null) {
            return this.client.server.getLastFrameOutTime();
        }
        return -1;
    }

    public boolean enableDlogRender(boolean enable) {
        this.renderManagerRLock.lock();
        try {
            if (this.renderManager != null) {
                this.renderManager.enableDlogRender(enable);
                return true;
            }
            this.renderManagerRLock.unlock();
            return false;
        } finally {
            this.renderManagerRLock.unlock();
        }
    }

    public double getFrameQueueinIntervalAvgValue() {
        if (this.client.server != null) {
            return this.client.server.getFrameQueueinIntervalAvgValue();
        }
        return -1.0d;
    }

    public boolean setAsyncRenderSurface(String key, Object surface, int width2, int height2, int interval) {
        DJIVideoCodecInnerManager.getInstance().getDJIVideoDecoderInterface().getDJIVideoDecoder().getmRenderManager().setExtraAsyncRenderSurface(key, surface, width2, height2, interval);
        return false;
    }

    public void setAsyncRenderInterval(String key, int interval) {
        if (this.renderManager != null) {
            this.renderManager.setExtraAsyncRenderInterval(key, interval);
        }
    }

    public void setOverExposureWarningEnable(boolean enable) {
        this.renderManagerRLock.lock();
        try {
            if (this.renderManager != null) {
                this.renderManager.enableOverExposureWarning(enable, R.raw.overexposure);
            }
        } finally {
            this.renderManagerRLock.unlock();
        }
    }

    public long getLastInitCodecOutputTime() {
        if (this.client == null || this.client.server == null) {
            return -1;
        }
        return this.client.server.getLastInitCodecOutputTime();
    }
}
