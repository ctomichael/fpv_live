package dji.midware.data.manager.P3;

import android.content.Context;
import android.support.annotation.Keep;
import dji.fieldAnnotation.EXClassNullAway;
import dji.log.DJILogHelper;
import dji.logic.album.manager.DJIAlbumCacheManager;
import dji.logic.manager.DJILogicManager;
import dji.logic.manager.DJIUSBWifiSwitchManager;
import dji.logic.utils.DJICameraSupportUtil;
import dji.midware.ble.BluetoothLeService;
import dji.midware.data.config.P3.CmdSet;
import dji.midware.data.model.P3.DataCameraGetMode;
import dji.midware.data.model.P3.DataCameraGetPushStateInfo;
import dji.midware.data.packages.P3.SendPack;
import dji.midware.data.queue.P3.Queue;
import dji.midware.link.DJILinkDaemonService;
import dji.midware.link.DJILinkUtil;
import dji.midware.media.DJIAudioDecoder;
import dji.midware.media.DJIVideoDecoder;
import dji.midware.media.DJIVideoUtil;
import dji.midware.media.newframing.DJILiveviewRenderController;
import dji.midware.natives.FPVController;
import dji.midware.stat.StatService;
import dji.midware.usb.P3.UsbAccessoryService;
import dji.midware.usbhost.P3.UsbHostServiceRC;
import dji.midware.util.DJIEventBusUtil;
import dji.midware.util.DjiSharedPreferencesManager;
import dji.midware.util.DoubleCameraSupportUtil;
import dji.midware.util.save.StreamDataLogHelper;
import dji.midware.util.save.TestUtil;
import dji.midware.util.save.VideoFrameObserver;
import dji.publics.DJIObject.DJICrashHandler;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

@Keep
@EXClassNullAway
public class ServiceManager implements DJIServiceInterface {
    private static final int SCAN_DURATION = 10;
    private static Context context;
    private static ServiceManager instance = null;
    private static volatile boolean isAlive = false;
    protected String TAG = getClass().getSimpleName();
    private DataCameraGetMode.MODE curMode;
    private boolean dataMode = false;
    private DJIAudioDecoder djiAudioDecoder;
    private DJIVideoDecoder djiVideoDecoder;
    private boolean isFix = false;
    private boolean isFpvNeedRawData = false;
    private boolean isNeedPacked = true;
    private boolean isNeedRawData = false;
    private boolean isPostMessage = false;
    private boolean isSupportOnlyForBluetoothDevice = false;
    private boolean lastInShootOrRecordForPrimaryCamera = false;
    private boolean lastInShootOrRecordForSecondaryCamera = false;
    private DJIServiceInterface mInterface = null;
    private int mPostCmdId;
    private int mPostCmdSet;
    private boolean needInsertIframe = false;
    private DJIVideoDecoder secondaryVideoDecoder;
    private StreamDataLogHelper streamDataLogHelper;

    public static synchronized void createInstance() {
        synchronized (ServiceManager.class) {
            if (instance == null) {
                instance = new ServiceManager();
            }
        }
    }

    public static ServiceManager getInstance() {
        if (instance == null) {
            createInstance();
        }
        return instance;
    }

    public static void Destroy() {
        if (instance != null) {
            instance.destroy();
        }
    }

    public static boolean isAlive() {
        return isAlive;
    }

    public static void setContext(Context ctx) {
        context = ctx;
    }

    public ServiceManager() {
        isAlive = true;
        TestUtil.checkDebugSwitch(context);
    }

    public void init() {
        DJIEventBusUtil.register(this);
        DJIFlycParamInfoManager.setContext(context);
        DJIGimbalParamInfoManager.setContext(context);
        if (!this.isSupportOnlyForBluetoothDevice) {
            FPVController.native_init(context);
        }
        BluetoothLeService.setContext(context);
        DJIUSBWifiSwitchManager.getInstance();
        DJILinkDaemonService.getInstance();
        DJILinkDaemonService.getInstance().setSupportOnlyForBluetoothDevice(this.isSupportOnlyForBluetoothDevice);
        DJILiveviewRenderController.getInstance().init();
    }

    public static Context getContext() {
        return context;
    }

    public synchronized void start() {
        DJIAlbumCacheManager.getInstance(context);
        DJIVideoUtil.setContext(context);
        StatService.setContext(context);
        DJILinkDaemonService.getInstance().setSupportOnlyForBluetoothDevice(this.isSupportOnlyForBluetoothDevice);
        DJILinkDaemonService.getInstance().init();
        if (!DJILinkUtil.useUsbRcOrWifi()) {
            BluetoothLeService.getInstance().start(10);
        }
        if (DJILinkUtil.usbUsbRc()) {
            UsbHostServiceRC.getInstance().start();
        }
        if (!this.isSupportOnlyForBluetoothDevice) {
            DJILogicManager.getInstance();
            startParser();
        }
    }

    public void pauseService(boolean isPause) {
        if (this.mInterface != null) {
            this.mInterface.pauseService(isPause);
        }
    }

    public void pauseRecvThread() {
        if (this.mInterface != null) {
            this.mInterface.pauseRecvThread();
        }
    }

    public void resumeRecvThread() {
        if (this.mInterface != null) {
            this.mInterface.resumeRecvThread();
        }
    }

    public void pauseParseThread() {
        if (this.mInterface != null) {
            this.mInterface.pauseParseThread();
        }
        FPVController.native_pauseParseThread(true);
    }

    public void resetDecoderKeyFrame() {
        DJIVideoDecoder decoder = getDecoder();
        if (decoder != null) {
            decoder.resetKeyFrame();
        }
    }

    public void resumeParseThread() {
        if (this.mInterface != null) {
            this.mInterface.resumeParseThread();
        }
        FPVController.native_pauseParseThread(false);
    }

    private void startParser() {
        FPVController.native_startParseThread();
    }

    private void stopParser() {
        FPVController.native_stopParseThread();
    }

    @Deprecated
    public void setCallObject(Object object) {
        this.djiVideoDecoder = (DJIVideoDecoder) object;
        FPVController.native_setCallObject(object);
    }

    public void setVideoDecoder(DJIVideoDecoder videoDecoder) {
        this.djiVideoDecoder = videoDecoder;
        FPVController.native_setCallObject(videoDecoder);
        if (this.streamDataLogHelper == null) {
            this.streamDataLogHelper = new StreamDataLogHelper();
        }
        if (videoDecoder != null) {
            this.streamDataLogHelper.start();
            return;
        }
        this.streamDataLogHelper.stop();
        VideoFrameObserver.getInstance().clean();
    }

    public void setSecondaryVideoDecoder(DJIVideoDecoder videoDecoder) {
        this.secondaryVideoDecoder = videoDecoder;
    }

    public void setAudioDecoder(DJIAudioDecoder audioDecoder) {
        this.djiAudioDecoder = audioDecoder;
    }

    public void createAudioDecoder() {
        if (this.djiAudioDecoder == null) {
            setAudioDecoder(new DJIAudioDecoder(DJIAudioDecoder.PacketFormat.ADTS, context.getApplicationContext()));
        }
    }

    public void setDecoderType(int type) {
        if (!this.isSupportOnlyForBluetoothDevice) {
            FPVController.native_setDecoderType(type);
        }
    }

    public DJIVideoDecoder getDecoder() {
        return this.djiVideoDecoder;
    }

    public DJIVideoDecoder getSecondaryVideoDecoder() {
        return this.secondaryVideoDecoder;
    }

    public boolean isDecoderOK() {
        return true;
    }

    public boolean isSecondaryDecoderOK() {
        return this.secondaryVideoDecoder != null && this.secondaryVideoDecoder.isDecoderOK();
    }

    public boolean isHasVideoData() {
        return true;
    }

    public DJIAudioDecoder getAudioDecoder() {
        return this.djiAudioDecoder;
    }

    public void setIsFix(boolean isFix2) {
        this.isFix = isFix2;
        FPVController.native_setIsFixRate(isFix2);
    }

    public boolean getIsFix() {
        return this.isFix;
    }

    public void startStream() {
    }

    public void stopStream() {
    }

    public void setInnerToolPostMsg(boolean isPost, int cmdSet, int cmdId) {
        this.isPostMessage = isPost;
        this.mPostCmdSet = cmdSet;
        this.mPostCmdId = cmdId;
    }

    public boolean isInnerToolsPostMsg() {
        return this.isPostMessage;
    }

    public void sendmessage(SendPack buffer) {
        if (this.mInterface != null) {
            this.mInterface.sendmessage(buffer);
        }
        if (this.isPostMessage && this.mPostCmdSet == buffer.cmdSet && this.mPostCmdId == buffer.cmdId) {
            EventBus.getDefault().post(buffer);
        }
    }

    public boolean isConnected() {
        if (this.mInterface == null) {
            return false;
        }
        return this.mInterface.isOK();
    }

    public void setDataMode(boolean dataMode2) {
        if (this.dataMode != dataMode2) {
            if (this.mInterface != null) {
                this.mInterface.setDataMode(dataMode2);
            }
            this.dataMode = dataMode2;
        }
    }

    public boolean isOK() {
        if (this.mInterface == null) {
            return false;
        }
        return this.mInterface.isOK();
    }

    public boolean isRemoteOK() {
        if (this.mInterface == null) {
            return false;
        }
        return this.mInterface.isRemoteOK();
    }

    public void destroy() {
        isAlive = false;
        if (!this.isSupportOnlyForBluetoothDevice) {
            stopParser();
            FPVController.native_unInit();
        }
        DJILinkDaemonService.getInstance().onDestroy();
        if (DJILinkUtil.usbUsbRc()) {
            UsbHostServiceRC.getInstance().stop();
        }
        DJILiveviewRenderController.getInstance().destroy();
        DJILogicManager.getInstance().destroy();
        DJIEventBusUtil.unRegister(this);
        EventBus.getDefault().removeAllStickyEvents();
        instance = null;
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEvent3BackgroundThread(DJICrashHandler.DJICrashEvent event) {
        switch (event) {
            case Crashed:
                pauseService(true);
                UsbAccessoryService.Destroy();
                return;
            default:
                return;
        }
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEvent3BackgroundThread(DataCameraGetPushStateInfo info) {
        boolean isInShootOrRecord = true;
        if (info.isGetted() && info.getSenderId() == 0) {
            if (!(info.getMode(0) == DataCameraGetMode.MODE.TAKEPHOTO || info.getMode(0) == DataCameraGetMode.MODE.RECORD)) {
                isInShootOrRecord = false;
            }
            if (isInShootOrRecord != this.lastInShootOrRecordForPrimaryCamera) {
                initPeakingFocus(0);
            }
            this.lastInShootOrRecordForPrimaryCamera = isInShootOrRecord;
        } else if (info.isGetted() && info.getSenderId() == 2) {
            if (!(info.getMode(2) == DataCameraGetMode.MODE.TAKEPHOTO || info.getMode(0) == DataCameraGetMode.MODE.RECORD)) {
                isInShootOrRecord = false;
            }
            if (isInShootOrRecord != this.lastInShootOrRecordForSecondaryCamera) {
                initPeakingFocus(2);
            }
            this.lastInShootOrRecordForSecondaryCamera = isInShootOrRecord;
        }
    }

    public void initPeakingFocusForMainDecoder(int cameraIndex) {
        boolean isInShootOrRecord;
        if (getDecoder() != null) {
            if (DJICameraSupportUtil.isSupportPeakingFocus(DataCameraGetPushStateInfo.getInstance().getCameraType(cameraIndex))) {
                DataCameraGetMode.MODE mode = DataCameraGetPushStateInfo.getInstance().getMode(cameraIndex);
                if (mode == DataCameraGetMode.MODE.TAKEPHOTO || mode == DataCameraGetMode.MODE.RECORD) {
                    isInShootOrRecord = true;
                } else {
                    isInShootOrRecord = false;
                }
                if (isInShootOrRecord) {
                    float threshold = DjiSharedPreferencesManager.getFloat(getContext(), "key_peaking_focus_threshold", 0.0f);
                    if (((double) Math.abs(threshold - 0.0f)) < 0.001d) {
                        getDecoder().setPeakFocusEnable(false);
                        return;
                    }
                    getDecoder().setPeakFocusEnable(true);
                    getDecoder().setPeakFocusThreshold(threshold);
                    return;
                }
                getDecoder().setPeakFocusEnable(false, true);
                return;
            }
            getDecoder().setPeakFocusEnable(false);
        }
    }

    public void initPeakingFocus(int cameraIndex) {
        boolean isInShootOrRecord;
        if (cameraIndex == 0) {
            if (DoubleCameraSupportUtil.isPrimaryVideoFromPrimaryCamera()) {
                initPeakingFocusForMainDecoder(cameraIndex);
            }
        } else if (cameraIndex != 2) {
        } else {
            if (!DoubleCameraSupportUtil.isPrimaryVideoFromPrimaryCamera()) {
                initPeakingFocusForMainDecoder(cameraIndex);
            } else if (DoubleCameraSupportUtil.isSecondaryVideoFromSecondaryCamera()) {
                if (DJICameraSupportUtil.isSupportPeakingFocus(DataCameraGetPushStateInfo.getInstance().getCameraType(cameraIndex))) {
                    float threshold = DjiSharedPreferencesManager.getFloat(getContext(), "key_peaking_focus_threshold_fpv", 0.0f);
                    DataCameraGetMode.MODE mode = DataCameraGetPushStateInfo.getInstance().getMode(cameraIndex);
                    if (mode == DataCameraGetMode.MODE.TAKEPHOTO || mode == DataCameraGetMode.MODE.RECORD) {
                        isInShootOrRecord = true;
                    } else {
                        isInShootOrRecord = false;
                    }
                    if (isInShootOrRecord) {
                        if (getSecondaryVideoDecoder() == null) {
                            return;
                        }
                        if (((double) Math.abs(threshold - 0.0f)) < 0.001d) {
                            getSecondaryVideoDecoder().setPeakFocusEnable(false);
                            return;
                        }
                        getSecondaryVideoDecoder().setPeakFocusEnable(true);
                        getSecondaryVideoDecoder().setPeakFocusThreshold(threshold);
                    } else if (getSecondaryVideoDecoder() != null) {
                        getSecondaryVideoDecoder().setPeakFocusEnable(false);
                    }
                } else if (getSecondaryVideoDecoder() != null) {
                    getSecondaryVideoDecoder().setPeakFocusEnable(false);
                }
            } else if (getSecondaryVideoDecoder() != null) {
                getSecondaryVideoDecoder().setPeakFocusEnable(false);
            }
        }
    }

    public void LOGD(String s) {
        DJILogHelper.getInstance().LOGD(this.TAG, s, false, false);
    }

    public void LOGE(String s) {
        DJILogHelper.getInstance().LOGE(this.TAG, s, false, false);
    }

    public Queue getQueue(CmdSet cmdSet) {
        return DJIQueueManager.getInstance().getQueue(cmdSet.value());
    }

    public Queue getQueue(int cmdSet) {
        return DJIQueueManager.getInstance().getQueue(cmdSet);
    }

    public void setIsNeedPacked(boolean isNeedPacked2) {
        this.isNeedPacked = isNeedPacked2;
        FPVController.native_setIsNeedPacked(isNeedPacked2);
    }

    public boolean isNeedPacked() {
        return this.isNeedPacked;
    }

    public void setIsNeedRawData(boolean isNeedRawData2) {
        this.isNeedRawData = isNeedRawData2;
        FPVController.native_setIsNeedRawData(isNeedRawData2);
    }

    public void setIsFpvNeedRawData(boolean isNeedRawData2) {
        this.isFpvNeedRawData = isNeedRawData2;
        FPVController.native_setIsNeedRawData(isNeedRawData2);
    }

    public boolean isNeedRawData() {
        return this.isNeedRawData;
    }

    public boolean isNeedFpvRawData() {
        return this.isFpvNeedRawData;
    }

    public void changeTo(DJIServiceInterface serviceInterface) {
        this.mInterface = serviceInterface;
    }

    public void onDisconnect() {
        if (this.djiVideoDecoder != null) {
            this.djiVideoDecoder.freshDecodeStatus(500);
        }
        if (this.secondaryVideoDecoder != null) {
            this.secondaryVideoDecoder.freshDecodeStatus(500);
        }
    }

    public void onConnect() {
    }

    public void setSupportOnlyForBluetoothDevice(boolean isSupportOnlyForBluetoothDevice2) {
        this.isSupportOnlyForBluetoothDevice = isSupportOnlyForBluetoothDevice2;
    }
}
