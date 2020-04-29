package dji.sdksharedlib.hardware.abstractions.camera;

import android.os.Handler;
import android.support.annotation.NonNull;
import com.google.android.gms.common.ConnectionResult;
import dji.common.camera.CameraParamRangeManager;
import dji.common.camera.CameraPeakThreshold;
import dji.common.camera.CameraRecordingState;
import dji.common.camera.CameraUtils;
import dji.common.camera.PhotoTimeLapseSettings;
import dji.common.camera.QuickPreviewSettings;
import dji.common.camera.SettingsDefinitions;
import dji.common.error.DJICameraError;
import dji.common.error.DJIError;
import dji.common.product.Model;
import dji.common.remotecontroller.RCMode;
import dji.common.util.CallbackUtils;
import dji.fieldAnnotation.EXClassNullAway;
import dji.internal.logics.CommonUtil;
import dji.internal.version.DJIVersionCamera;
import dji.internal.version.VersionController;
import dji.log.DJILog;
import dji.midware.component.DJIComponentManager;
import dji.midware.data.config.P3.Ccode;
import dji.midware.data.config.P3.DeviceType;
import dji.midware.data.config.P3.ProductType;
import dji.midware.data.manager.P3.DJIProductManager;
import dji.midware.data.model.P3.DataBaseCameraSetting;
import dji.midware.data.model.P3.DataCameraActiveStatus;
import dji.midware.data.model.P3.DataCameraFormatSDCard;
import dji.midware.data.model.P3.DataCameraGetPushRecordingName;
import dji.midware.data.model.P3.DataCameraGetPushShotParams;
import dji.midware.data.model.P3.DataCameraGetPushStateInfo;
import dji.midware.data.model.P3.DataCameraGetPushStorageInfo;
import dji.midware.data.model.P3.DataCameraGetStateInfo;
import dji.midware.data.model.P3.DataCameraSetDate;
import dji.midware.data.model.P3.DataCameraSetPhoto;
import dji.midware.data.model.P3.DataCameraSetPhotoMode;
import dji.midware.data.model.P3.DataCameraSetStorageInfo;
import dji.midware.data.model.P3.DataCameraSetTimeParams;
import dji.midware.data.model.P3.DataCommonGetVersion;
import dji.midware.data.model.P3.DataEyeGetPushPseudoCameraParams;
import dji.midware.data.model.P3.DataEyeGetPushPseudoCameraShotState;
import dji.midware.data.model.P3.DataEyeSetPseudoCameraControl;
import dji.midware.data.model.P3.DataSingleSendAppStateForStabilization;
import dji.midware.data.model.P3.DataSpecialControl;
import dji.midware.data.model.common.DataAbstractGetPushActiveStatus;
import dji.midware.data.model.common.DataCommonActiveGetVer;
import dji.midware.interfaces.DJIDataCallBack;
import dji.midware.media.event.BokehEvent;
import dji.midware.media.record.RecorderBase;
import dji.midware.media.record.RecorderManager;
import dji.midware.util.DJIEventBusUtil;
import dji.midware.util.DoubleCameraSupportUtil;
import dji.midware.util.RepeatDataBase;
import dji.pilot.fpv.util.DJIFlurryReport;
import dji.sdksharedlib.DJISDKCache;
import dji.sdksharedlib.extension.CacheHelper;
import dji.sdksharedlib.extension.KeyHelper;
import dji.sdksharedlib.hardware.abstractions.Action;
import dji.sdksharedlib.hardware.abstractions.DJISDKCacheHWAbstraction;
import dji.sdksharedlib.hardware.abstractions.Getter;
import dji.sdksharedlib.hardware.abstractions.Setter;
import dji.sdksharedlib.keycatalog.CameraKeys;
import dji.sdksharedlib.keycatalog.DJISDKCacheKey;
import dji.sdksharedlib.keycatalog.DJISDKCacheKeys;
import dji.sdksharedlib.keycatalog.ProductKeys;
import dji.sdksharedlib.listener.DJIGetCallback;
import dji.sdksharedlib.listener.DJIParamAccessListener;
import dji.sdksharedlib.store.DJISDKCacheParamValue;
import dji.sdksharedlib.store.DJISDKCacheStoreLayer;
import dji.sdksharedlib.util.DJISDKCacheThreadManager;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

@EXClassNullAway
public class DJICameraAbstraction extends DJISDKCacheHWAbstraction implements DJIParamAccessListener {
    private static final int AFC_CAMERA_VERSION = 9;
    public static final String DisplayNameMavic2EnterpriseCamera = "Mavic 2 Enterprise Camera";
    public static final String DisplayNameMavic2EnterpriseDual_IR = "Mavic 2 Enterprise Dual-Thermal";
    public static final String DisplayNameMavic2EnterpriseDual_VL = "Mavic 2 Enterprise Dual-Visual";
    public static final String DisplayNameMavic2ProCamera = "Mavic 2 Pro Camera";
    public static final String DisplayNameMavic2ZoomCamera = "Mavic 2 Zoom Camera";
    public static final String DisplayNameMavicAirCamera = "Mavic Air Camera";
    public static final String DisplayNameMavicProCamera = "Mavic Pro Camera";
    public static final String DisplayNamePayload = "Payload Camera";
    public static final String DisplayNamePhantom34KCamera = "Phantom 3 4K Camera";
    public static final String DisplayNamePhantom3AdvancedCamera = "Phantom 3 Advanced Camera";
    public static final String DisplayNamePhantom3ProfessionalCamera = "Phantom 3 Professional Camera";
    public static final String DisplayNamePhantom3StandardCamera = "Phantom 3 Standard Camera";
    public static final String DisplayNamePhantom4Camera = "Phantom 4 Camera";
    public static final String DisplayNameSparkCamera = "Spark Camera";
    public static final String DisplayNameWM160 = "WM160";
    public static final String DisplayNameX3 = "Zenmuse X3";
    public static final String DisplayNameX4S = "Zenmuse X4S";
    public static final String DisplayNameX5 = "Zenmuse X5";
    public static final String DisplayNameX5R = "Zenmuse X5R";
    public static final String DisplayNameX5S = "Zenmuse X5S";
    public static final String DisplayNameX7 = "Zenmuse X7";
    public static final String DisplayNameXT2_IR = "Zenmuse XT2-Thermal";
    public static final String DisplayNameXT2_VL = "Zenmuse XT2-Visual";
    public static final String DisplayNameXT336 = "Zenmuse XT";
    public static final String DisplayNameXT640 = "Zenmuse XT";
    public static final String DisplayNameZ3 = "Zenmuse Z3";
    public static final String DisplayNameZ30 = "Zenmuse Z30";
    public static final String DisplaynamePhantom4AdvancedCamera = "Phantom 4 Advanced Camera";
    public static final String DisplaynamePhantom4PSDRCamera = "Phantom 4 Pro V2.0 Camera";
    public static final String DisplaynamePhantom4ProCamera = "Phantom 4 Professional Camera";
    public static final String DisplaynamePhantom4RTKCamera = "Phantom 4 RTK Camera";
    private static final String TAG = "DJISDKCacheCameraAbstraction";
    private CameraParamRangeManager cameraParamRangeManager;
    protected DataCameraGetPushStateInfo.CameraType cameraType = DataCameraGetPushStateInfo.CameraType.OTHER;
    protected SettingsDefinitions.CameraColor[] currentCameraColorRange;
    protected Handler handler = new Handler(DJISDKCacheThreadManager.getSingletonBackgroundLooper());
    /* access modifiers changed from: private */
    public boolean isDoingSetDate = false;
    protected boolean isMFInverted = false;
    protected AtomicBoolean isShootingPhoto = new AtomicBoolean(false);
    protected AtomicBoolean isWaitingForShootPhotoFeedback = new AtomicBoolean(false);
    private CountDownLatch recordInCacheCdl;
    /* access modifiers changed from: private */
    public Runnable recordRunnable = new Runnable() {
        /* class dji.sdksharedlib.hardware.abstractions.camera.DJICameraAbstraction.AnonymousClass1 */

        public void run() {
            DataCameraGetPushStateInfo.getInstance().swapValidData(DJICameraAbstraction.this.getExpectedSenderIdByIndex());
            DJICameraAbstraction.this.onEvent3BackgroundThread(DataCameraGetPushStateInfo.getInstance());
            synchronized (DJICameraAbstraction.this.recordTimerLock) {
                DJICameraAbstraction.this.recordTimeInSec++;
                if (DJICameraAbstraction.this.recordTimeHandler != null) {
                    DJICameraAbstraction.this.recordTimeHandler.postDelayed(DJICameraAbstraction.this.recordRunnable, 1000);
                }
            }
        }
    };
    protected Handler recordTimeHandler;
    protected int recordTimeInSec = 0;
    /* access modifiers changed from: private */
    public Object recordTimerLock = new Object();
    private long setDateStartTime = 0;
    protected SettingsDefinitions.PhotoTimeIntervalSettings timeIntervalSettings;
    protected PhotoTimeLapseSettings timeLapseSettings;

    private void recordInCacheCdlInit(int count) {
        this.recordInCacheCdl = new CountDownLatch(count);
    }

    private void recordInCacheCdlWait(int time, TimeUnit timeUnit) {
        if (this.recordInCacheCdl != null) {
            try {
                this.recordInCacheCdl.await((long) time, timeUnit);
            } catch (InterruptedException e) {
            }
        }
    }

    private void recordInCacheCdlCountDown() {
        if (this.recordInCacheCdl != null) {
            this.recordInCacheCdl.countDown();
        }
    }

    public void init(String component, int index, DJISDKCacheStoreLayer storeLayer, DJISDKCacheHWAbstraction.OnValueChangeListener onValueChangeListener) {
        super.init(component, index, storeLayer, onValueChangeListener);
        this.cameraParamRangeManager = new CameraParamRangeManager(onValueChangeListener, this.defaultKeyPath);
        CacheHelper.addListener(this, false, KeyHelper.getCameraKey(index, CameraKeys.PHOTO_TIME_INTERVAL_SETTINGS), KeyHelper.getCameraKey(index, CameraKeys.SHUTTER_SPEED), KeyHelper.getCameraKey(index, CameraKeys.PHOTO_TIME_LAPSE_SETTINGS), KeyHelper.getCameraKey(index, CameraKeys.CAMERA_COLOR_RANGE), KeyHelper.getCameraKey(index, CameraKeys.SHUTTER_SPEED_RANGE));
        DJIEventBusUtil.register(this);
    }

    public void onValueChange(DJISDKCacheKey key, DJISDKCacheParamValue oldValue, DJISDKCacheParamValue newValue) {
        if (newValue != null && newValue.getData() != null) {
            if (shouldUpdateShutterSpeed(key)) {
                updateShutterSpeedIfNeeded();
                return;
            }
            this.timeIntervalSettings = (SettingsDefinitions.PhotoTimeIntervalSettings) CacheHelper.getCamera(this.index, CameraKeys.PHOTO_TIME_INTERVAL_SETTINGS);
            this.timeLapseSettings = (PhotoTimeLapseSettings) CacheHelper.getCamera(this.index, CameraKeys.PHOTO_TIME_LAPSE_SETTINGS);
            this.currentCameraColorRange = (SettingsDefinitions.CameraColor[]) CacheHelper.getCamera(this.index, CameraKeys.CAMERA_COLOR_RANGE);
            if (this.isWaitingForShootPhotoFeedback.get() && CameraUtils.isShootingPhoto(this.index)) {
                this.isShootingPhoto.set(true);
            }
        }
    }

    private boolean shouldUpdateShutterSpeed(@NonNull DJISDKCacheKey key) {
        return CameraKeys.SHUTTER_SPEED_RANGE.equals(key.getParamKey()) || CameraKeys.SHUTTER_SPEED.equals(key.getParamKey());
    }

    private void updateShutterSpeedIfNeeded() {
        SettingsDefinitions.ShutterSpeed shutterSpeed = (SettingsDefinitions.ShutterSpeed) CacheHelper.getCamera(this.index, CameraKeys.SHUTTER_SPEED);
        SettingsDefinitions.ShutterSpeed[] shutterSpeedArray = (SettingsDefinitions.ShutterSpeed[]) CacheHelper.getCamera(this.index, CameraKeys.SHUTTER_SPEED_RANGE);
        if (shutterSpeed != null && shutterSpeed != SettingsDefinitions.ShutterSpeed.UNKNOWN && shutterSpeedArray != null) {
            SettingsDefinitions.ShutterSpeed maxValue = shutterSpeedArray[shutterSpeedArray.length - 1];
            if (shutterSpeed.value() > maxValue.value()) {
                CacheHelper.setCamera(this.index, CameraKeys.SHUTTER_SPEED, maxValue, null);
            }
        }
    }

    public void destroy() {
        DJIEventBusUtil.unRegister(this);
        CacheHelper.removeListener(this);
        this.cameraParamRangeManager.onDestory();
        super.destroy();
    }

    public void syncPushDataFromMidware() {
        super.syncPushDataFromMidware();
        initializeCustomizedKey();
        if (DataCameraGetPushStateInfo.getInstance().isGetted()) {
            DataCameraGetPushStateInfo.getInstance().swapValidData(getExpectedSenderIdByIndex());
            onEvent3BackgroundThread(DataCameraGetPushStateInfo.getInstance());
        }
        if (DataCameraGetPushStorageInfo.getInstance().isGetted()) {
            DataCameraGetPushStorageInfo.getInstance().swapValidData(getExpectedSenderIdByIndex());
            onEvent3BackgroundThread(DataCameraGetPushStorageInfo.getInstance());
        }
        if (DJIComponentManager.getInstance().getPlatformType() == DJIComponentManager.PlatformType.Inspire2) {
            updateDate(true);
        }
    }

    /* JADX DEBUG: Failed to find minimal casts for resolve overloaded methods, cast all args instead
     method: dji.sdksharedlib.hardware.abstractions.DJISDKCacheHWAbstraction.notifyValueChangeForKeyPath(java.lang.Object, dji.sdksharedlib.keycatalog.DJISDKCacheKey):void
     arg types: [boolean, dji.sdksharedlib.keycatalog.DJISDKCacheKey]
     candidates:
      dji.sdksharedlib.hardware.abstractions.DJISDKCacheHWAbstraction.notifyValueChangeForKeyPath(java.lang.Object, java.lang.String):void
      dji.sdksharedlib.hardware.abstractions.DJISDKCacheHWAbstraction.notifyValueChangeForKeyPath(java.lang.Object, dji.sdksharedlib.keycatalog.DJISDKCacheKey):void */
    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEvent3BackgroundThread(BokehEvent event) {
        if (event.type == BokehEvent.BokehType.PreprocessStart) {
            notifyValueChangeForKeyPath((Object) true, convertKeyToPath(CameraKeys.IS_DOWNLOAD_BOKEH));
        } else if (event.type == BokehEvent.BokehType.PreprocessOver) {
            notifyValueChangeForKeyPath((Object) false, convertKeyToPath(CameraKeys.IS_DOWNLOAD_BOKEH));
        }
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEvent3BackgroundThread(DataEyeGetPushPseudoCameraParams pseudoCameraParams) {
        boolean isPseudoCameraShooting;
        boolean isPseudoCameraProcessing;
        if (pseudoCameraParams.isGetted()) {
            DataEyeGetPushPseudoCameraParams.PseudoCameraMissionState state = pseudoCameraParams.getMissionState();
            if (state == DataEyeGetPushPseudoCameraParams.PseudoCameraMissionState.PSEUDO_CAMERA_MISSION_STATE_PREPARE || state == DataEyeGetPushPseudoCameraParams.PseudoCameraMissionState.PSEUDO_CAMERA_MISSION_STATE_RUNNING || state == DataEyeGetPushPseudoCameraParams.PseudoCameraMissionState.PSEUDO_CAMERA_MISSION_STATE_PROCESSING) {
                isPseudoCameraShooting = true;
            } else {
                isPseudoCameraShooting = false;
            }
            notifyValueChangeForKeyPath(Boolean.valueOf(isPseudoCameraShooting), convertKeyToPath(CameraKeys.IS_PSEUDO_CAMERA_SHOOTING));
            if (state == DataEyeGetPushPseudoCameraParams.PseudoCameraMissionState.PSEUDO_CAMERA_MISSION_STATE_PROCESSING) {
                isPseudoCameraProcessing = true;
            } else {
                isPseudoCameraProcessing = false;
            }
            notifyValueChangeForKeyPath(Boolean.valueOf(isPseudoCameraProcessing), convertKeyToPath(CameraKeys.IS_PSEUDO_CAMERA_PROCESSING));
            boolean isShootingShallowFocusPhoto = false;
            boolean isShootingPanoramaPhoto = false;
            if (isPseudoCameraShooting) {
                if (pseudoCameraParams.getCameraMode() == DataEyeGetPushPseudoCameraParams.PseudoCameraMode.PSEUDO_CAMERA_MODE_BOKEH) {
                    isShootingShallowFocusPhoto = true;
                } else if (isPanoramaMode(pseudoCameraParams)) {
                    isShootingPanoramaPhoto = true;
                }
            }
            notifyValueChangeForKeyPath(Boolean.valueOf(isShootingShallowFocusPhoto), convertKeyToPath(CameraKeys.IS_SHOOTING_SHALLOW_FOCUS_PHOTO));
            notifyValueChangeForKeyPath(Boolean.valueOf(isShootingPanoramaPhoto), convertKeyToPath(CameraKeys.IS_SHOOTING_PANORAMA_PHOTO));
            int progress = pseudoCameraParams.getCaptureDone();
            int total = pseudoCameraParams.getCaptureTotal();
            if (pseudoCameraParams.isInPanoCapture()) {
                notifyValueChangeForKeyPath(Integer.valueOf(total), convertKeyToPath(CameraKeys.PSEUDO_CAMERA_CAPTURE_TOTAL));
                notifyValueChangeForKeyPath(Integer.valueOf(progress), convertKeyToPath(CameraKeys.PSEUDO_CAMERA_CAPTURE_PROGRESS));
            }
            int process = pseudoCameraParams.getProcessProgress();
            if (process != -1) {
                notifyValueChangeForKeyPath(Integer.valueOf(process), convertKeyToPath(CameraKeys.PSEUDO_CAMERA_PROCESS_PROGRESS));
            }
        }
    }

    /* access modifiers changed from: protected */
    public boolean isPanoramaMode(@NonNull DataEyeGetPushPseudoCameraParams pseudoCameraParams) {
        DataEyeGetPushPseudoCameraParams.PseudoCameraMode pseudoCameraMode = pseudoCameraParams.getCameraMode();
        return pseudoCameraMode == DataEyeGetPushPseudoCameraParams.PseudoCameraMode.PSEUDO_CAMERA_MODE_PANO_1x3 || pseudoCameraMode == DataEyeGetPushPseudoCameraParams.PseudoCameraMode.PSEUDO_CAMERA_MODE_PANO_3x1 || pseudoCameraMode == DataEyeGetPushPseudoCameraParams.PseudoCameraMode.PSEUDO_CAMERA_MODE_PANO_3x3 || pseudoCameraMode == DataEyeGetPushPseudoCameraParams.PseudoCameraMode.PSEUDO_CAMERA_MODE_PANO_SUPER_RESOLUTION || pseudoCameraMode == DataEyeGetPushPseudoCameraParams.PseudoCameraMode.PSEUDO_CAMERA_MODE_PANO_SPHERE || pseudoCameraMode == DataEyeGetPushPseudoCameraParams.PseudoCameraMode.PSEUDO_CAMERA_MODE_PANO_180;
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEvent3BackgroundThread(DataEyeGetPushPseudoCameraShotState pseudoCameraShotState) {
        if (pseudoCameraShotState.isGetted()) {
            notifyValueChangeForKeyPath(pseudoCameraShotState.getPseudoCameraCmdResult(), convertKeyToPath(CameraKeys.PSEUDO_CAMERA_CAPTURE_RESULT));
        }
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEvent3BackgroundThread(DataCameraGetPushRecordingName pushRecordingName) {
        if (isValidSender(pushRecordingName.getSenderId()) && pushRecordingName.isGetted() && pushRecordingName != null) {
            int fileIndex = pushRecordingName.getIndex();
            int subIndex = pushRecordingName.getSubIndex();
            int fileType = pushRecordingName.getFileType();
            long time = pushRecordingName.getTime();
            notifyValueChangeForKeyPath(Integer.valueOf(fileIndex), convertKeyToPath(CameraKeys.LAST_FILE_INDEX));
            notifyValueChangeForKeyPath(Integer.valueOf(subIndex), convertKeyToPath(CameraKeys.LAST_FILE_SUBINDEX));
            notifyValueChangeForKeyPath(Integer.valueOf(fileType), convertKeyToPath(CameraKeys.LAST_FILE_TYPE));
            notifyValueChangeForKeyPath(Long.valueOf(time), convertKeyToPath(CameraKeys.LAST_FILE_CREATE_TIME));
        }
    }

    /* access modifiers changed from: protected */
    public void startRecordTimeTimer() {
        synchronized (this.recordTimerLock) {
            this.recordTimeInSec = 0;
            if (this.recordTimeHandler == null) {
                this.recordTimeHandler = new Handler(DJISDKCacheThreadManager.getSingletonBackgroundLooper());
            } else {
                this.recordTimeHandler.removeCallbacksAndMessages(null);
            }
            this.recordTimeHandler.postDelayed(this.recordRunnable, 1000);
        }
    }

    /* access modifiers changed from: protected */
    public void stopRecordTimeTimer() {
        synchronized (this.recordTimerLock) {
            if (this.recordTimeHandler != null) {
                this.recordTimeHandler.removeCallbacks(this.recordRunnable);
                this.recordTimeHandler = null;
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEvent3BackgroundThread(DataCameraGetPushStateInfo pushStateInfo) {
        if (pushStateInfo != null && isValidSender(pushStateInfo.getSenderId()) && pushStateInfo.isGetted()) {
            onGetCameraStateInfo(pushStateInfo);
        }
    }

    /* access modifiers changed from: protected */
    public void onGetCameraStateInfo(DataCameraGetPushStateInfo pushStateInfo) {
        SettingsDefinitions.ShootPhotoMode shootPhotoMode;
        DataCameraGetPushStateInfo.CameraType type = pushStateInfo.getCameraType();
        if (type != this.cameraType) {
            this.cameraType = type;
            this.isMFInverted = this.cameraType == DataCameraGetPushStateInfo.CameraType.DJICameraTypeFC6510 || this.cameraType == DataCameraGetPushStateInfo.CameraType.DJICameraTypeFC6520 || this.cameraType == DataCameraGetPushStateInfo.CameraType.DJICameraTypeFC550 || this.cameraType == DataCameraGetPushStateInfo.CameraType.DJICameraTypeFC550Raw || this.cameraType == DataCameraGetPushStateInfo.CameraType.DJICameraTypeFC220 || this.cameraType == DataCameraGetPushStateInfo.CameraType.DJICameraTypeFC220S || this.cameraType == DataCameraGetPushStateInfo.CameraType.DJICameraTypeGD600 || this.cameraType == DataCameraGetPushStateInfo.CameraType.DJICameraTypeFC6310;
        }
        if (pushStateInfo != null) {
            boolean isUsbConnected = pushStateInfo.getUsbState();
            boolean isInserted = pushStateInfo.getSDCardInsertState();
            boolean isShootPhotoEnabled = pushStateInfo.getEnabledPhoto();
            int totalSpace = pushStateInfo.getSDCardTotalSize();
            int remainSpace = pushStateInfo.getSDCardFreeSize();
            SettingsDefinitions.SDCardStateOperationState sdCardStateOperationState = SettingsDefinitions.SDCardStateOperationState.find(pushStateInfo.getSDCardState(true).value());
            long availableCaptureCount = pushStateInfo.getRemainedShots();
            int availableRecordingTime = pushStateInfo.getRemainedTime();
            boolean quickViewEnabled = pushStateInfo.getFastPlayBackEnabled();
            notifyValueChangeForKeyPath(new QuickPreviewSettings(quickViewEnabled, pushStateInfo.getFastPlayBackTime()), CameraKeys.FAST_PLAYBACK_SETTINGS);
            notifyValueChangeForKeyPath(SettingsDefinitions.FileIndexMode.find(pushStateInfo.getFileIndexMode().value()), CameraKeys.FILE_INDEX_MODE);
            notifyValueChangeForKeyPath(Boolean.valueOf(pushStateInfo.beInDebugMode()), CameraKeys.IS_CAMERA_IN_DEBUG_MODE);
            if (!quickViewEnabled) {
                notifyValueChangeForKeyPath((Object) null, convertKeyToPath(CameraKeys.PHOTO_QUICK_VIEW_DURATION));
            }
            if (isThermalFFCModeSupported()) {
                notifyValueChangeForKeyPath(Boolean.valueOf(isThermalFFCModeSupported()), convertKeyToPath(CameraKeys.THERMAL_IS_FFC_MODE_SUPPORTED));
            }
            notifyValueChangeForKeyPath(Integer.valueOf(pushStateInfo.getVerstion()), convertKeyToPath(CameraKeys.PROTOCOL_VERSION));
            notifyValueChangeForKeyPath(Boolean.valueOf(isShootPhotoEnabled), convertKeyToPath(CameraKeys.IS_SHOOTING_PHOTO_ENABLED));
            if (!isInternalStorageSupported()) {
                notifyValueChangeForKeyPath(Integer.valueOf(remainSpace), convertKeyToPath(CameraKeys.SDCARD_REMAINING_SPACE_IN_MB));
                notifyValueChangeForKeyPath(sdCardStateOperationState, convertKeyToPath(CameraKeys.SDCARD_STATE));
                notifyValueChangeForKeyPath(Boolean.valueOf(isInserted), convertKeyToPath(CameraKeys.SDCARD_IS_INSERTED));
                notifyValueChangeForKeyPath(Long.valueOf(availableCaptureCount), convertKeyToPath(CameraKeys.SDCARD_AVAILABLE_CAPTURE_COUNT));
                notifyValueChangeForKeyPath(Integer.valueOf(availableRecordingTime), convertKeyToPath(CameraKeys.SDCARD_AVAILABLE_RECORDING_TIME_IN_SECONDS));
                notifyValueChangeForKeyPath(Boolean.valueOf(isUsbConnected), convertKeyToPath(CameraKeys.SDCARD_IS_CONNECTED_TO_PC));
                notifyValueChangeForKeyPath(Integer.valueOf(totalSpace), convertKeyToPath(CameraKeys.SDCARD_TOTAL_SPACE_IN_MB));
                dispatchState(pushStateInfo.getSDCardState(), DataCameraSetStorageInfo.Storage.SDCARD);
            }
            boolean isShootingIntervalPhoto = pushStateInfo.getIsTimePhotoing();
            DataCameraGetStateInfo.PhotoState photoState = pushStateInfo.getPhotoState();
            boolean isShootingRawBurstPhoto = photoState == DataCameraGetStateInfo.PhotoState.RawBurst;
            boolean isShootingPhoto2 = (DataCameraGetStateInfo.PhotoState.NO == photoState || DataCameraGetStateInfo.PhotoState.OTHER == photoState) ? false : true;
            boolean isShootingBurstPhoto = photoState == DataCameraGetStateInfo.PhotoState.Multiple;
            boolean isShootingSinglePhoto = photoState == DataCameraGetStateInfo.PhotoState.Single;
            if (DJIComponentManager.getInstance().getPlatformType() == DJIComponentManager.PlatformType.Spark && (((shootPhotoMode = (SettingsDefinitions.ShootPhotoMode) CacheHelper.getCamera(this.index, CameraKeys.SHOOT_PHOTO_MODE)) == SettingsDefinitions.ShootPhotoMode.BURST || shootPhotoMode == SettingsDefinitions.ShootPhotoMode.AEB) && isShootingSinglePhoto)) {
                isShootingSinglePhoto = false;
                isShootingBurstPhoto = true;
            }
            boolean isShootingRawPhoto = false;
            if (!(DataCameraGetPushShotParams.getInstance() == null || DataCameraGetPushShotParams.getInstance().getImageFormat() == 1 || !isShootingSinglePhoto)) {
                isShootingRawPhoto = true;
            }
            CameraRecordingState recordingState = CameraRecordingState.find(pushStateInfo.getRecordState().value());
            if (!(recordingState == CameraRecordingState.RECORDING || recordingState == CameraRecordingState.PREPARING || recordingState == CameraRecordingState.STOPPING || this.recordTimeHandler == null || !RecorderManager.isRecording())) {
                recordingState = CameraRecordingState.RECORDING_TO_CACHE;
            }
            boolean isRecording = pushStateInfo.getRecordState() == DataCameraGetPushStateInfo.RecordType.STARTING || pushStateInfo.getRecordState() == DataCameraGetPushStateInfo.RecordType.START;
            boolean isCameraOverHeated = pushStateInfo.getHotState();
            boolean isCameraSensorError = pushStateInfo.getSensorState();
            int currentVideoRecordingTimeInSeconds = pushStateInfo.getVideoRecordTime();
            boolean isStoringPhoto = pushStateInfo.getIsStoring();
            SettingsDefinitions.CameraMode mode = mapProtocolValueToCameraMode(pushStateInfo.getMode().value());
            boolean beTracking = pushStateInfo.beInTrackingMode();
            notifyValueChangeForKeyPath(Boolean.valueOf(isShootingRawBurstPhoto), convertKeyToPath("IsShootingRawBurstPhoto"));
            notifyValueChangeForKeyPath(Boolean.valueOf(isShootingRawPhoto), convertKeyToPath(CameraKeys.IS_SHOOTING_SINGLE_PHOTO_IN_RAW_FORMAT));
            notifyValueChangeForKeyPath(Boolean.valueOf(isShootingIntervalPhoto), convertKeyToPath(CameraKeys.IS_SHOOTING_INTERVAL_PHOTO));
            notifyValueChangeForKeyPath(Boolean.valueOf(isShootingBurstPhoto), convertKeyToPath(CameraKeys.IS_SHOOTING_BURST_PHOTO));
            notifyValueChangeForKeyPath(Boolean.valueOf(isShootingSinglePhoto), convertKeyToPath(CameraKeys.IS_SHOOTING_SINGLE_PHOTO));
            notifyValueChangeForKeyPath(Boolean.valueOf(isRecording), convertKeyToPath(CameraKeys.IS_RECORDING));
            notifyValueChangeForKeyPath(Boolean.valueOf(isShootingRawBurstPhoto), convertKeyToPath("IsShootingRawBurstPhoto"));
            notifyValueChangeForKeyPath(recordingState, convertKeyToPath(CameraKeys.RECORDING_STATE));
            notifyValueChangeForKeyPath(Boolean.valueOf(isCameraOverHeated), convertKeyToPath(CameraKeys.IS_OVERHEATING));
            notifyValueChangeForKeyPath(Boolean.valueOf(isCameraSensorError), convertKeyToPath(CameraKeys.HAS_ERROR));
            notifyValueChangeForKeyPath(Integer.valueOf(currentVideoRecordingTimeInSeconds), convertKeyToPath(CameraKeys.CURRENT_VIDEO_RECORDING_TIME_IN_SECONDS));
            if (this.recordTimeHandler == null) {
                notifyValueChangeForKeyPath(Integer.valueOf(currentVideoRecordingTimeInSeconds), convertKeyToPath(CameraKeys.CURRENT_VIDEO_RECORDING_TIME_IN_SECONDS));
            } else {
                notifyValueChangeForKeyPath(Integer.valueOf(this.recordTimeInSec), convertKeyToPath(CameraKeys.CURRENT_VIDEO_RECORDING_TIME_IN_SECONDS));
            }
            notifyValueChangeForKeyPath(Boolean.valueOf(isStoringPhoto), convertKeyToPath(CameraKeys.IS_STORING_PHOTO));
            notifyValueChangeForKeyPath(mode, convertKeyToPath("Mode"));
            notifyValueChangeForKeyPath(Float.valueOf(pushStateInfo.getHyperLapseMarginX()), convertKeyToPath(CameraKeys.HYPER_LAPSE_FRAME_X));
            notifyValueChangeForKeyPath(Float.valueOf(pushStateInfo.getHyperLapseMarginY()), convertKeyToPath(CameraKeys.HYPER_LAPSE_FRAME_Y));
            notifyValueChangeForKeyPath(SettingsDefinitions.CameraType.find(type.value()), convertKeyToPath(CameraKeys.CAMERA_TYPE));
            notifyValueChangeForKeyPath(pushStateInfo.getTempAlarmState(), convertKeyToPath(CameraKeys.CAMERA_TEMPERATURE_STATE));
            notifyValueChangeForKeyPath(Boolean.valueOf(isShootingPhoto2), convertKeyToPath(CameraKeys.IS_SHOOTING_PHOTO));
            notifyValueChangeForKeyPath(Boolean.valueOf(beTracking), convertKeyToPath(CameraKeys.TRACKING_MODE));
            notifyValueChangeForKeyPath(Boolean.valueOf(pushStateInfo.isHistogramEnable()), convertKeyToPath(CameraKeys.HISTOGRAM_ENABLED));
            if (isNDFilterModeSupported()) {
                notifyValueChangeForKeyPath(SettingsDefinitions.NDFilterMode.find(pushStateInfo.getNDFilter().getCMD()), convertKeyToPath(CameraKeys.NDFILTER_MODE));
            }
            updateDate(!pushStateInfo.getTimeSyncState());
        }
    }

    private void updateDate(boolean isNeedSync) {
        if (isNeedSync && !this.isDoingSetDate) {
            this.isDoingSetDate = true;
            if (DJIComponentManager.getInstance().getPlatformType() == DJIComponentManager.PlatformType.Inspire2) {
                CacheHelper.getRemoteController("Mode", new DJIGetCallback() {
                    /* class dji.sdksharedlib.hardware.abstractions.camera.DJICameraAbstraction.AnonymousClass2 */

                    public void onSuccess(DJISDKCacheParamValue value) {
                        RCMode mode = (RCMode) CacheHelper.getRemoteController("Mode");
                        if (mode == null || !(mode == RCMode.MASTER || mode == RCMode.NORMAL)) {
                            boolean unused = DJICameraAbstraction.this.isDoingSetDate = false;
                            return;
                        }
                        DataCameraSetDate setDate = new DataCameraSetDate();
                        setDate.setReceiverId(DJICameraAbstraction.this.getReceiverIdByIndex(), DataCameraSetDate.class);
                        new RepeatDataBase(setDate, 10, 3500, new DJIDataCallBack() {
                            /* class dji.sdksharedlib.hardware.abstractions.camera.DJICameraAbstraction.AnonymousClass2.AnonymousClass1 */

                            public void onSuccess(Object model) {
                                DJILog.save("DataCameraSetDate", DJIFlurryReport.FlightRecord.V2_EVENT_FLIGHTDATA_SYNCHRONOUS_SUBKEY);
                                boolean unused = DJICameraAbstraction.this.isDoingSetDate = false;
                            }

                            public void onFailure(Ccode ccode) {
                                DJILog.save("DataCameraSetDate", "fails : " + ccode);
                                boolean unused = DJICameraAbstraction.this.isDoingSetDate = false;
                            }
                        }).start();
                    }

                    public void onFails(DJIError error) {
                        boolean unused = DJICameraAbstraction.this.isDoingSetDate = false;
                    }
                });
            } else {
                ((DataCameraSetDate) DataCameraSetDate.getInstance().setReceiverId(getReceiverIdByIndex(), DataCameraSetDate.class)).start(new DJIDataCallBack() {
                    /* class dji.sdksharedlib.hardware.abstractions.camera.DJICameraAbstraction.AnonymousClass3 */

                    public void onSuccess(Object model) {
                        boolean unused = DJICameraAbstraction.this.isDoingSetDate = false;
                    }

                    public void onFailure(Ccode ccode) {
                        boolean unused = DJICameraAbstraction.this.isDoingSetDate = false;
                    }
                });
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEvent3BackgroundThread(DataCameraGetPushStorageInfo status) {
        if (status != null && isValidSender(status.getSenderId()) && status.isGetted()) {
            updateCameraStorageInfo(status);
        }
    }

    /* access modifiers changed from: protected */
    public void updateCameraStorageInfo(DataCameraGetPushStorageInfo info) {
        if (isInternalStorageSupported()) {
            notifyValueChangeForKeyPath(info.getStorageLocation(), convertKeyToPath(CameraKeys.STORAGE_LOCATION));
            notifyValueChangeForKeyPath(SettingsDefinitions.StorageLocation.find(info.getStorageLocation().value()), convertKeyToPath(CameraKeys.CAMERA_STORAGE_LOCATION));
            notifyValueChangeForKeyPath(SettingsDefinitions.SDCardStateOperationState.find(info.getInnerStorageStatus().value()), convertKeyToPath(CameraKeys.INNERSTORAGE_STATE));
            notifyValueChangeForKeyPath(Integer.valueOf(info.getInnerStorageFreeSize()), convertKeyToPath(CameraKeys.INNERSTORAGE_REMAINING_SPACE_IN_MB));
            notifyValueChangeForKeyPath(Boolean.valueOf(info.getInnerStorageInsertStatus()), convertKeyToPath(CameraKeys.INNERSTORAGE_IS_INSERTED));
            notifyValueChangeForKeyPath(Integer.valueOf(info.getInnerStorageTotalSize()), convertKeyToPath(CameraKeys.INNERSTORAGE_TOTAL_SPACE_IN_MB));
            notifyValueChangeForKeyPath(Long.valueOf(info.getInnerStorageRemainedShots()), convertKeyToPath(CameraKeys.INNERSTORAGE_AVAILABLE_CAPTURE_COUNT));
            notifyValueChangeForKeyPath(Integer.valueOf(info.getInnerStorageRemainedTime()), convertKeyToPath(CameraKeys.INNERSTORAGE_AVAILABLE_RECORDING_TIME_IN_SECONDS));
            dispatchState(info.getInnerStorageStatus(), DataCameraSetStorageInfo.Storage.INNER_STORAGE);
            notifyValueChangeForKeyPath(Integer.valueOf(info.getSDCardFreeSize()), convertKeyToPath(CameraKeys.SDCARD_REMAINING_SPACE_IN_MB));
            notifyValueChangeForKeyPath(SettingsDefinitions.SDCardStateOperationState.find(info.getSDCardState().value()), convertKeyToPath(CameraKeys.SDCARD_STATE));
            notifyValueChangeForKeyPath(Boolean.valueOf(info.getSDCardInsertState()), convertKeyToPath(CameraKeys.SDCARD_IS_INSERTED));
            notifyValueChangeForKeyPath(Long.valueOf(info.getSDCardRemainedShots()), convertKeyToPath(CameraKeys.SDCARD_AVAILABLE_CAPTURE_COUNT));
            notifyValueChangeForKeyPath(Integer.valueOf(info.getSDCardRemainedTime()), convertKeyToPath(CameraKeys.SDCARD_AVAILABLE_RECORDING_TIME_IN_SECONDS));
            notifyValueChangeForKeyPath(Integer.valueOf(info.getSDCardTotalSize()), convertKeyToPath(CameraKeys.SDCARD_TOTAL_SPACE_IN_MB));
            dispatchState(info.getSDCardState(), DataCameraSetStorageInfo.Storage.SDCARD);
        }
    }

    private void dispatchState(DataCameraGetStateInfo.SDCardState sdcardState, DataCameraSetStorageInfo.Storage storage) {
        boolean isInitializing = sdcardState == DataCameraGetStateInfo.SDCardState.Initialzing;
        boolean hasError = sdcardState == DataCameraGetStateInfo.SDCardState.Invalid || sdcardState == DataCameraGetStateInfo.SDCardState.Illegal || sdcardState == DataCameraGetStateInfo.SDCardState.Unknow;
        boolean readOnly = sdcardState == DataCameraGetStateInfo.SDCardState.WriteProtection;
        boolean invalidFormat = sdcardState == DataCameraGetStateInfo.SDCardState.Illegal;
        boolean isFormated = sdcardState != DataCameraGetStateInfo.SDCardState.Unformat;
        boolean isFormating = sdcardState == DataCameraGetStateInfo.SDCardState.Formating;
        boolean isFull = sdcardState == DataCameraGetStateInfo.SDCardState.Full;
        boolean isVerified = sdcardState != DataCameraGetStateInfo.SDCardState.Invalid;
        boolean isBusy = sdcardState == DataCameraGetStateInfo.SDCardState.Busy;
        boolean isSlow = sdcardState == DataCameraGetStateInfo.SDCardState.Slow;
        boolean needsFormatting = sdcardState == DataCameraGetStateInfo.SDCardState.ToFormat;
        boolean isRepairingFiles = sdcardState == DataCameraGetStateInfo.SDCardState.TryToRecoverFile;
        boolean isWriteSlow = sdcardState == DataCameraGetStateInfo.SDCardState.BecomeSlow;
        if (storage == DataCameraSetStorageInfo.Storage.INNER_STORAGE) {
            notifyValueChangeForKeyPath(Boolean.valueOf(isInitializing), convertKeyToPath(CameraKeys.INNERSTORAGE_IS_INITIALIZING));
            notifyValueChangeForKeyPath(Boolean.valueOf(hasError), convertKeyToPath(CameraKeys.INNERSTORAGE_HAS_ERROR));
            notifyValueChangeForKeyPath(Boolean.valueOf(readOnly), convertKeyToPath(CameraKeys.INNERSTORAGE_IS_READ_ONLY));
            notifyValueChangeForKeyPath(Boolean.valueOf(invalidFormat), convertKeyToPath(CameraKeys.INNERSTORAGE_IS_INVALID_FORMAT));
            notifyValueChangeForKeyPath(Boolean.valueOf(isFormated), convertKeyToPath(CameraKeys.INNERSTORAGE_IS_FORMATTED));
            notifyValueChangeForKeyPath(Boolean.valueOf(isFormating), convertKeyToPath(CameraKeys.INNERSTORAGE_IS_FORMATTING));
            notifyValueChangeForKeyPath(Boolean.valueOf(isFull), convertKeyToPath(CameraKeys.INNERSTORAGE_IS_FULL));
            notifyValueChangeForKeyPath(Boolean.valueOf(isVerified), convertKeyToPath(CameraKeys.INNERSTORAGE_IS_VERIFIED));
            notifyValueChangeForKeyPath(Boolean.valueOf(isSlow), convertKeyToPath(CameraKeys.INNERSTORAGE_IS_SLOW));
            notifyValueChangeForKeyPath(Boolean.valueOf(isBusy), convertKeyToPath(CameraKeys.INNERSTORAGE_IS_BUSY));
        } else if (storage == DataCameraSetStorageInfo.Storage.SDCARD) {
            notifyValueChangeForKeyPath(Boolean.valueOf(isInitializing), convertKeyToPath(CameraKeys.SDCARD_IS_INITIALIZING));
            notifyValueChangeForKeyPath(Boolean.valueOf(hasError), convertKeyToPath(CameraKeys.SDCARD_HAS_ERROR));
            notifyValueChangeForKeyPath(Boolean.valueOf(readOnly), convertKeyToPath(CameraKeys.SDCARD_IS_READ_ONLY));
            notifyValueChangeForKeyPath(Boolean.valueOf(invalidFormat), convertKeyToPath(CameraKeys.SDCARD_IS_INVALID_FORMAT));
            notifyValueChangeForKeyPath(Boolean.valueOf(isFormated), convertKeyToPath(CameraKeys.SDCARD_IS_FORMATTED));
            notifyValueChangeForKeyPath(Boolean.valueOf(isFormating), convertKeyToPath(CameraKeys.SDCARD_IS_FORMATTING));
            notifyValueChangeForKeyPath(Boolean.valueOf(isFull), convertKeyToPath(CameraKeys.SDCARD_IS_FULL));
            notifyValueChangeForKeyPath(Boolean.valueOf(isVerified), convertKeyToPath(CameraKeys.SDCARD_IS_VERIFIED));
            notifyValueChangeForKeyPath(Boolean.valueOf(isSlow), convertKeyToPath(CameraKeys.SDCARD_IS_SLOW));
            notifyValueChangeForKeyPath(Boolean.valueOf(isBusy), convertKeyToPath(CameraKeys.SDCARD_IS_BUSY));
            notifyValueChangeForKeyPath(Boolean.valueOf(needsFormatting), convertKeyToPath(CameraKeys.SDCARD_NEEDS_FORMATTING));
            notifyValueChangeForKeyPath(Boolean.valueOf(isRepairingFiles), convertKeyToPath(CameraKeys.SDCARD_IS_REPAIRING_FILES));
            notifyValueChangeForKeyPath(Boolean.valueOf(isWriteSlow), convertKeyToPath(CameraKeys.SDCARD_IS_WRITE_SLOW));
        }
    }

    /* access modifiers changed from: protected */
    public SettingsDefinitions.CameraMode mapProtocolValueToCameraMode(int value) {
        switch (value) {
            case 2:
                if (isMediaDownModeMapValue2()) {
                    value = 4;
                    break;
                }
                break;
            case 6:
                value = 2;
                break;
            case 7:
                value = 4;
                break;
            case 8:
                value = 5;
                break;
        }
        return SettingsDefinitions.CameraMode.find(value);
    }

    /* access modifiers changed from: protected */
    public void initializeComponentCharacteristics() {
        addCharacteristics(CameraKeys.class, getClass());
    }

    /* JADX DEBUG: Failed to find minimal casts for resolve overloaded methods, cast all args instead
     method: dji.sdksharedlib.hardware.abstractions.DJISDKCacheHWAbstraction.notifyValueChangeForKeyPath(java.lang.Object, dji.sdksharedlib.keycatalog.DJISDKCacheKey):void
     arg types: [boolean, dji.sdksharedlib.keycatalog.DJISDKCacheKey]
     candidates:
      dji.sdksharedlib.hardware.abstractions.DJISDKCacheHWAbstraction.notifyValueChangeForKeyPath(java.lang.Object, java.lang.String):void
      dji.sdksharedlib.hardware.abstractions.DJISDKCacheHWAbstraction.notifyValueChangeForKeyPath(java.lang.Object, dji.sdksharedlib.keycatalog.DJISDKCacheKey):void */
    /* access modifiers changed from: protected */
    public void initializeCustomizedKey() {
        notifyValueChangeForKeyPath(Boolean.valueOf(isTapZoomSupported()), convertKeyToPath(CameraKeys.IS_TAP_ZOOM_SUPPORTED));
        notifyValueChangeForKeyPath(Boolean.valueOf(isMediaDownModeMapValue2()), convertKeyToPath(CameraKeys.IS_MEDIA_DOWN_MODE_MAP_VALUE_2));
        notifyValueChangeForKeyPath(Boolean.valueOf(isPlaybackSupported()), convertKeyToPath(CameraKeys.IS_PLAYBACK_SUPPORTED));
        notifyValueChangeForKeyPath(Boolean.valueOf(isMediaDownloadModeSupported()), convertKeyToPath(CameraKeys.IS_MEDIA_DOWNLOAD_MODE_SUPPORTED));
        notifyValueChangeForKeyPath(Boolean.valueOf(isAdjustableApertureSupported()), convertKeyToPath(CameraKeys.IS_ADJUSTABLE_APERTURE_SUPPORTED));
        notifyValueChangeForKeyPath(Boolean.valueOf(isAdjustableFocalPointSupported()), convertKeyToPath(CameraKeys.IS_ADJUSTABLE_FOCAL_POINT_SUPPORTED));
        notifyValueChangeForKeyPath(Boolean.valueOf(isOpticalZoomSupported()), convertKeyToPath(CameraKeys.IS_OPTICAL_ZOOM_SUPPORTED));
        notifyValueChangeForKeyPath(Boolean.valueOf(isAudioRecordSupported()), convertKeyToPath(CameraKeys.IS_AUDIO_RECORDING_SUPPORTED));
        notifyValueChangeForKeyPath(Boolean.valueOf(isTimeLapseSupported()), convertKeyToPath(CameraKeys.IS_TIME_LAPSE_SUPPORTED));
        notifyValueChangeForKeyPath(Boolean.valueOf(isDigitalZoomScaleSupported()), convertKeyToPath(CameraKeys.IS_DIGITAL_ZOOM_SUPPORTED));
        notifyValueChangeForKeyPath(Boolean.valueOf(isChangeableLensSupported()), convertKeyToPath(CameraKeys.IS_INTERCHANGEABLE_LENS_SUPPORTED));
        notifyValueChangeForKeyPath(Boolean.valueOf(isPhotoQuickViewSupported()), convertKeyToPath(CameraKeys.IS_PHOTO_QUICK_VIEW_SUPPORTED));
        notifyValueChangeForKeyPath(Boolean.valueOf(isSSDSupported()), convertKeyToPath(CameraKeys.IS_SSD_SUPPORTED));
        if (!isSSDSupported()) {
            notifyValueChangeForKeyPath((Object) false, convertKeyToPath(CameraKeys.SSD_VIDEO_RECORDING_ENABLED));
        }
        notifyValueChangeForKeyPath(getDisplayName(), convertKeyToPath("DisplayName"));
        notifyValueChangeForKeyPath(Boolean.valueOf(isThermalImagingCamera()), convertKeyToPath(CameraKeys.IS_THERMAL_CAMERA));
        notifyValueChangeForKeyPath(Boolean.valueOf(isOverallTemperatureMeterSupported()), convertKeyToPath(CameraKeys.THERMAL_IS_OVERALL_TEMPERATURE_METER_SUPPORTED));
        notifyValueChangeForKeyPath(Boolean.valueOf(isThermalFFCModeSupported()), convertKeyToPath(CameraKeys.THERMAL_IS_FFC_MODE_SUPPORTED));
        notifyValueChangeForKeyPath(Boolean.valueOf(isVideoPlaybackSupported()), convertKeyToPath(CameraKeys.IS_VIDEO_PLAYBACK_SUPPORTED));
        notifyValueChangeForKeyPath(Boolean.valueOf(isMechanicalShutterSupported()), convertKeyToPath(CameraKeys.IS_MECHANICAL_SHUTTER_SUPPORTED));
        notifyValueChangeForKeyPath(Boolean.valueOf(isNDFilterModeSupported()), convertKeyToPath(CameraKeys.IS_ND_FILTER_MODE_SUPPORTED));
        notifyValueChangeForKeyPath(Boolean.valueOf(isEIModeSupported()), convertKeyToPath(CameraKeys.IS_EI_MODE_SUPPORTED));
        notifyValueChangeForKeyPath(Boolean.valueOf(isAFCSupported()), convertKeyToPath(CameraKeys.IS_AFC_SUPPORTED));
        notifyValueChangeForKeyPath(Boolean.valueOf(isVideoDewarpingSupported()), convertKeyToPath(CameraKeys.IS_DEWARPING_SUPPORTED));
        notifyValueChangeForKeyPath(Boolean.valueOf(isSensorCleaningSupported()), convertKeyToPath(CameraKeys.IS_SENSOR_CLEANING_SUPPORTED));
        notifyValueChangeForKeyPath(Boolean.valueOf(isInternalStorageSupported()), convertKeyToPath(CameraKeys.IS_INTERNAL_STORAGE_SUPPORTED));
        notifyValueChangeForKeyPath(Boolean.valueOf(isColorWaveformSupported()), convertKeyToPath(CameraKeys.IS_COLOR_WAVEFORM_SUPPORTED));
        if (!isInternalStorageSupported()) {
            notifyValueChangeForKeyPath(SettingsDefinitions.StorageLocation.SDCARD, convertKeyToPath(CameraKeys.CAMERA_STORAGE_LOCATION));
        }
        notifyValueChangeForKeyPath(new CameraPeakThreshold(1.5f, 2.7f, 4.0f), convertKeyToPath(CameraKeys.CAMERA_PEAK_THRESHOLD));
    }

    /* access modifiers changed from: protected */
    public boolean isShootPanoramaSupported() {
        return false;
    }

    /* access modifiers changed from: protected */
    public boolean isShootShallowFocusSupported() {
        return false;
    }

    /* access modifiers changed from: protected */
    public boolean isShootPhotoRawBurstSupported() {
        return false;
    }

    /* access modifiers changed from: protected */
    public boolean isOpticalZoomScaleSupported() {
        return false;
    }

    /* access modifiers changed from: protected */
    public boolean isTapZoomSupported() {
        return false;
    }

    /* access modifiers changed from: protected */
    public boolean isAFTargetSupported() {
        return false;
    }

    /* access modifiers changed from: protected */
    public boolean isMediaDownModeMapValue2() {
        return false;
    }

    /* access modifiers changed from: protected */
    public boolean isHandHeldProduct() {
        return false;
    }

    /* access modifiers changed from: protected */
    public boolean isTurnOffFanSupported() {
        return false;
    }

    /* access modifiers changed from: protected */
    public boolean isTimeLapseSupported() {
        return false;
    }

    /* access modifiers changed from: protected */
    public boolean isDigitalZoomScaleSupported() {
        return false;
    }

    /* access modifiers changed from: protected */
    public boolean isSlowMotionSupported() {
        return false;
    }

    /* access modifiers changed from: protected */
    public boolean isPhotoQuickViewSupported() {
        return true;
    }

    /* access modifiers changed from: protected */
    public boolean isAudioRecordSupported() {
        return false;
    }

    /* access modifiers changed from: protected */
    public boolean isChangeableLensSupported() {
        return false;
    }

    /* access modifiers changed from: protected */
    public boolean isAdjustableApertureSupported() {
        return false;
    }

    /* access modifiers changed from: protected */
    public boolean isSSDSupported() {
        return false;
    }

    /* access modifiers changed from: protected */
    public boolean isAdjustableFocalPointSupported() {
        return false;
    }

    /* access modifiers changed from: protected */
    public boolean isHDRPhotoSupported() {
        return true;
    }

    /* access modifiers changed from: protected */
    public boolean isPlaybackSupported() {
        return false;
    }

    /* access modifiers changed from: protected */
    public boolean isMediaDownloadModeSupported() {
        return false;
    }

    /* access modifiers changed from: protected */
    public boolean isOpticalZoomSupported() {
        return false;
    }

    /* access modifiers changed from: protected */
    public boolean isVideoPlaybackSupported() {
        return false;
    }

    /* access modifiers changed from: protected */
    public boolean isColorWaveformSupported() {
        return false;
    }

    public boolean isThermalImagingCamera() {
        return false;
    }

    /* access modifiers changed from: protected */
    public boolean isTau336Camera() {
        return false;
    }

    /* access modifiers changed from: protected */
    public boolean isTau640Camera() {
        return false;
    }

    /* access modifiers changed from: protected */
    public boolean isMFSupported() {
        return false;
    }

    /* access modifiers changed from: protected */
    public boolean isAFSupported() {
        return false;
    }

    /* access modifiers changed from: protected */
    public boolean isBroadcastModeSupported() {
        return false;
    }

    /* access modifiers changed from: protected */
    public boolean isEIModeSupported() {
        return false;
    }

    /* access modifiers changed from: protected */
    public boolean isInternalStorageSupported() {
        return false;
    }

    public boolean isAFCSupported() {
        return supportAFC(DataCameraGetPushStateInfo.getInstance().getCameraType(getExpectedSenderIdByIndex()), DataCameraGetPushStateInfo.getInstance().getVerstion(getExpectedSenderIdByIndex()));
    }

    public boolean supportAFC(DataCameraGetPushStateInfo.CameraType cameraType2, int version) {
        return (version >= 9 && (cameraType2 == DataCameraGetPushStateInfo.CameraType.DJICameraTypeFC220 || cameraType2 == DataCameraGetPushStateInfo.CameraType.DJICameraTypeFC6310)) || wm620SupportAFC() || CommonUtil.isWM240Series(null);
    }

    public boolean wm620SupportAFC() {
        DataCameraGetPushStateInfo info = DataCameraGetPushStateInfo.getInstance();
        if (info.getCameraType(getExpectedSenderIdByIndex()) != DataCameraGetPushStateInfo.CameraType.DJICameraTypeFC6510 || info.getWm620CameraProtocolVersion(getExpectedSenderIdByIndex()) < 1) {
            return false;
        }
        return true;
    }

    /* access modifiers changed from: protected */
    public boolean isVideoDewarpingSupported() {
        return false;
    }

    /* access modifiers changed from: protected */
    public boolean isSensorCleaningSupported() {
        return false;
    }

    /* access modifiers changed from: protected */
    public boolean isMechanicalShutterSupported() {
        return false;
    }

    /* access modifiers changed from: protected */
    public boolean isNDFilterModeSupported() {
        return false;
    }

    /* access modifiers changed from: protected */
    public String getDisplayName() {
        return "";
    }

    /* access modifiers changed from: protected */
    public boolean isThermalFFCModeSupported() {
        return false;
    }

    /* access modifiers changed from: protected */
    public boolean isOverallTemperatureMeterSupported() {
        return false;
    }

    @Action(CameraKeys.FORMAT_SD_CARD)
    public void formatSDCard(DJISDKCacheHWAbstraction.InnerCallback callback) {
        DataCameraFormatSDCard setter = new DataCameraFormatSDCard();
        setter.setStorageLocation(DataCameraSetStorageInfo.Storage.SDCARD);
        setter.setReceiverId(getReceiverIdByIndex());
        setter.start(CallbackUtils.defaultCB(callback));
    }

    @Action(CameraKeys.FORMAT_INTERNAL_STORAGE)
    public void formatStorage(DJISDKCacheHWAbstraction.InnerCallback callback, SettingsDefinitions.StorageLocation location) {
        if (location == null || location == SettingsDefinitions.StorageLocation.UNKNOWN) {
            CallbackUtils.onFailure(callback, DJIError.COMMON_PARAM_ILLEGAL);
            return;
        }
        DataCameraFormatSDCard setter = new DataCameraFormatSDCard();
        setter.setStorageLocation(DataCameraSetStorageInfo.Storage.find(location.value()));
        setter.setReceiverId(getReceiverIdByIndex());
        setter.start(CallbackUtils.defaultCB(callback, DJICameraError.class));
    }

    @Getter(DJISDKCacheKeys.SERIAL_NUMBER)
    public void getSerialNumber(DJISDKCacheHWAbstraction.InnerCallback callback) {
        getSerialNumber(callback, 0);
    }

    private void getSerialNumber(final DJISDKCacheHWAbstraction.InnerCallback callback, final int state) {
        if (isNewProcessOfActivation()) {
            DataCommonActiveGetVer getVer = new DataCommonActiveGetVer();
            getVer.setReceiverId(getReceiverIdByIndex());
            getVer.setDevice(DeviceType.CAMERA).start(new DJIDataCallBack() {
                /* class dji.sdksharedlib.hardware.abstractions.camera.DJICameraAbstraction.AnonymousClass4 */

                public void onSuccess(Object model) {
                    ((DataCameraActiveStatus) DataCameraActiveStatus.getInstance().setReceiverId(DJICameraAbstraction.this.getReceiverIdByIndex(), DataCameraActiveStatus.class)).setType(DataAbstractGetPushActiveStatus.TYPE.GET).start(new DJIDataCallBack() {
                        /* class dji.sdksharedlib.hardware.abstractions.camera.DJICameraAbstraction.AnonymousClass4.AnonymousClass1 */

                        public void onSuccess(Object model) {
                            CallbackUtils.onSuccess(callback, DJICameraAbstraction.this.getHashSerialNum(DataCameraActiveStatus.getInstance().getSN(), state));
                        }

                        public void onFailure(Ccode ccode) {
                            CallbackUtils.onFailure(callback, DJIError.getDJIError(ccode));
                        }
                    });
                }

                public void onFailure(Ccode ccode) {
                    CallbackUtils.onFailure(callback, DJIError.getDJIError(ccode));
                }
            });
            return;
        }
        ((DataCameraActiveStatus) DataCameraActiveStatus.getInstance().setReceiverId(getReceiverIdByIndex(), DataCameraActiveStatus.class)).setType(DataAbstractGetPushActiveStatus.TYPE.GET).start(new DJIDataCallBack() {
            /* class dji.sdksharedlib.hardware.abstractions.camera.DJICameraAbstraction.AnonymousClass5 */

            public void onSuccess(Object model) {
                CallbackUtils.onSuccess(callback, DJICameraAbstraction.this.getHashSerialNum(DataCameraActiveStatus.getInstance().getSN(), state));
            }

            public void onFailure(Ccode ccode) {
                CallbackUtils.onFailure(callback, DJIError.getDJIError(ccode));
            }
        });
    }

    /* access modifiers changed from: protected */
    public boolean isNewProcessOfActivation() {
        return false;
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEvent3BackgroundThread(DJIVersionCamera event) {
        if (checkNeedUpdate()) {
            notifyValueChangeForKeyPath(VersionController.getInstance().getCameraVersion(), DJISDKCacheKeys.FIRMWARE_VERSION);
        }
    }

    private boolean checkNeedUpdate() {
        DJIComponentManager.PlatformType platformType = DJIComponentManager.getInstance().getPlatformType();
        DJIComponentManager.CameraComponentType cameraComponentType = DJIComponentManager.getInstance().getCameraComponentType(this.index);
        return (cameraComponentType == DJIComponentManager.CameraComponentType.Z3 || cameraComponentType == DJIComponentManager.CameraComponentType.X5 || cameraComponentType == DJIComponentManager.CameraComponentType.X5R) && platformType != DJIComponentManager.PlatformType.OSMO;
    }

    @Getter(DJISDKCacheKeys.FIRMWARE_VERSION)
    public void getFirmwareVersion(final DJISDKCacheHWAbstraction.InnerCallback callback) {
        if (checkNeedUpdate()) {
            DJISDKCacheParamValue value = DJISDKCache.getInstance().getAvailableValue(convertKeyToPath(DJISDKCacheKeys.FIRMWARE_VERSION));
            if (value != null) {
                CallbackUtils.onSuccess(callback, (String) value.getData());
            } else {
                CallbackUtils.onFailure(callback, DJICameraError.PARAMETERS_GET_FAILED);
            }
        } else {
            final DataCommonGetVersion dcgv = new DataCommonGetVersion();
            dcgv.setDeviceType(DeviceType.CAMERA);
            dcgv.setDeviceModel(getReceiverIdByIndex());
            dcgv.startForce(new DJIDataCallBack() {
                /* class dji.sdksharedlib.hardware.abstractions.camera.DJICameraAbstraction.AnonymousClass6 */

                public void onSuccess(Object model) {
                    CallbackUtils.onSuccess(callback, dcgv.getFirmVer("."));
                }

                public void onFailure(Ccode ccode) {
                    CallbackUtils.onFailure(callback, DJICameraError.getDJIError(ccode));
                }
            });
        }
    }

    @Getter(CameraKeys.HARDWARE_VERSION)
    public void getHardWareVersion(final DJISDKCacheHWAbstraction.InnerCallback callback) {
        final DataCommonGetVersion dcgv = new DataCommonGetVersion();
        dcgv.setDeviceType(DeviceType.CAMERA);
        dcgv.setDeviceModel(getReceiverIdByIndex());
        dcgv.start(new DJIDataCallBack() {
            /* class dji.sdksharedlib.hardware.abstractions.camera.DJICameraAbstraction.AnonymousClass7 */

            public void onSuccess(Object model) {
                CallbackUtils.onSuccess(callback, dcgv.getHardwareVer());
            }

            public void onFailure(Ccode ccode) {
                CallbackUtils.onFailure(callback, DJICameraError.getDJIError(ccode));
            }
        });
    }

    /* access modifiers changed from: protected */
    public int getExpectedSenderIdByIndex() {
        return DoubleCameraSupportUtil.getCameraIdInProtocol(this.index);
    }

    /* access modifiers changed from: protected */
    public int getReceiverIdByIndex() {
        return DoubleCameraSupportUtil.getCameraIdInProtocol(this.index);
    }

    /* access modifiers changed from: protected */
    public boolean isValidSender(int senderId) {
        if (senderId != getExpectedSenderIdByIndex()) {
            return false;
        }
        return true;
    }

    /* access modifiers changed from: protected */
    public boolean checkValueInArray(int[] array, int value) {
        for (int item : array) {
            if (value == item) {
                return true;
            }
        }
        return false;
    }

    /* access modifiers changed from: protected */
    public DataCameraSetPhotoMode prepareShootPhotoMode(SettingsDefinitions.ShootPhotoMode photoMode) {
        DataCameraSetPhotoMode photeModeSetter = DataCameraSetPhotoMode.getInstance();
        DataCameraSetPhoto.TYPE type = DataCameraSetPhoto.TYPE.find(photoMode.getInternalTypeValue());
        photeModeSetter.setType(type);
        if (!(type == DataCameraSetPhoto.TYPE.SINGLE || type == DataCameraSetPhoto.TYPE.HDR || type == DataCameraSetPhoto.TYPE.HDR_PLUS || type == DataCameraSetPhoto.TYPE.HYPER_NIGHT || type == DataCameraSetPhoto.TYPE.FULLVIEW)) {
            int[][] childRange = (int[][]) DJISDKCache.getInstance().getAvailableValue(convertKeyToPath(CameraKeys.SHOOT_PHOTO_MODE_CHILD_RANGE)).getData();
            if (type == DataCameraSetPhoto.TYPE.AEB) {
                int[] aebRange = childRange[SettingsDefinitions.ShootPhotoMode.AEB.value()];
                DJISDKCacheParamValue aebValue = DJISDKCache.getInstance().getAvailableValue(KeyHelper.getCameraKey(this.index, CameraKeys.PHOTO_AEB_COUNT));
                int aebCount = ((SettingsDefinitions.PhotoAEBCount) aebValue.getData()).value();
                if (!(aebValue == null || aebValue.getData() == null)) {
                    if (aebRange != null && !checkValueInArray(aebRange, aebCount) && aebRange.length > 0) {
                        aebCount = aebRange[0];
                    }
                    photeModeSetter.setContinueNum(aebCount);
                }
            } else if (type == DataCameraSetPhoto.TYPE.BURST) {
                int[] burstRange = childRange[SettingsDefinitions.ShootPhotoMode.BURST.value()];
                int burstCount = ((SettingsDefinitions.PhotoBurstCount) DJISDKCache.getInstance().getAvailableValue(KeyHelper.getCameraKey(this.index, CameraKeys.PHOTO_BURST_COUNT)).getData()).value();
                if (burstRange != null && !checkValueInArray(burstRange, burstCount) && burstRange.length > 0) {
                    burstCount = burstRange[0];
                }
                photeModeSetter.setContinueNum(burstCount);
            } else if (type == DataCameraSetPhoto.TYPE.RAWBURST) {
                int[] rawBurstRange = childRange[SettingsDefinitions.ShootPhotoMode.RAW_BURST.value()];
                int rawBurstCount = ((SettingsDefinitions.PhotoBurstCount) DJISDKCache.getInstance().getAvailableValue(KeyHelper.getCameraKey(this.index, CameraKeys.PHOTO_RAW_BURST_COUNT)).getData()).value();
                if (rawBurstRange != null && rawBurstRange.length > 0 && !checkValueInArray(rawBurstRange, rawBurstCount)) {
                    rawBurstCount = rawBurstRange[0];
                }
                photeModeSetter.setContinueNum(rawBurstCount);
            } else if (type == DataCameraSetPhoto.TYPE.TIME) {
                int[] intervalRange = childRange[SettingsDefinitions.ShootPhotoMode.INTERVAL.value()];
                SettingsDefinitions.PhotoTimeIntervalSettings intervalParam = (SettingsDefinitions.PhotoTimeIntervalSettings) DJISDKCache.getInstance().getAvailableValue(KeyHelper.getCameraKey(this.index, CameraKeys.PHOTO_TIME_INTERVAL_SETTINGS)).getData();
                int intervalTime = intervalParam.getTimeIntervalInSeconds();
                if (!(intervalRange == null || intervalRange.length == 0 || intervalTime >= intervalRange[0])) {
                    intervalTime = intervalRange[0];
                }
                photeModeSetter.setTimeInterval(intervalTime);
                photeModeSetter.setTimeNum(intervalParam.getCaptureCount());
                photeModeSetter.setTimeType(photoMode == SettingsDefinitions.ShootPhotoMode.TIME_LAPSE ? 2 : 0);
            }
        }
        return photeModeSetter;
    }

    /* access modifiers changed from: protected */
    public boolean checkPhotoShootMode(SettingsDefinitions.ShootPhotoMode shootPhotoMode) {
        if (shootPhotoMode != SettingsDefinitions.ShootPhotoMode.RAW_BURST || isShootPhotoRawBurstSupported()) {
            return true;
        }
        return false;
    }

    @Action(CameraKeys.START_SHOOT_PHOTO)
    public void startShootPhoto(DJISDKCacheHWAbstraction.InnerCallback callback) {
        startShootPhoto(callback, (SettingsDefinitions.ShootPhotoMode) DJISDKCache.getInstance().getAvailableValue(KeyHelper.getCameraKey(this.index, CameraKeys.SHOOT_PHOTO_MODE)).getData());
    }

    /* access modifiers changed from: protected */
    public void startShootPhoto(final DJISDKCacheHWAbstraction.InnerCallback callback, SettingsDefinitions.ShootPhotoMode mode) {
        final int timeout;
        if (!checkPhotoShootMode(mode)) {
            CallbackUtils.onFailure(callback, DJICameraError.INVALID_PARAMETERS);
            return;
        }
        DataCameraGetPushStateInfo pushInfo = DataCameraGetPushStateInfo.getInstance();
        if (pushInfo.getIsStoring(getExpectedSenderIdByIndex())) {
            CallbackUtils.onFailure(callback, DJICameraError.UNSUPPORTED_CMD_STATE);
        } else if (!CameraUtils.isStorageAvailable(this.index)) {
            CallbackUtils.onFailure(callback, DJICameraError.UNSUPPORTED_CMD_STATE);
        } else if (!CameraUtils.isInActionMode(this.index) || !CameraUtils.isPhotoActionExecutable(this.index)) {
            CallbackUtils.onFailure(callback, DJICameraError.UNSUPPORTED_CMD_STATE);
        } else {
            if (DataCameraGetPushStateInfo.CameraType.DJICameraTypeFC1705 == DataCameraGetPushStateInfo.getInstance().getCameraType(getExpectedSenderIdByIndex())) {
                if (pushInfo.getIsStoring(1)) {
                    CallbackUtils.onFailure(callback, DJICameraError.UNSUPPORTED_CMD_STATE);
                    return;
                } else if (!CameraUtils.isInActionMode(2) || !CameraUtils.isPhotoActionExecutable(2)) {
                    CallbackUtils.onFailure(callback, DJICameraError.UNSUPPORTED_CMD_STATE);
                    return;
                }
            }
            final int[] MODE = {-1};
            if (getReceiverIdByIndex() != 0) {
                timeout = 500;
            } else {
                timeout = ConnectionResult.DRIVE_EXTERNAL_STORAGE_REQUIRED;
            }
            final Runnable runStartShootPhoto = new Runnable() {
                /* class dji.sdksharedlib.hardware.abstractions.camera.DJICameraAbstraction.AnonymousClass8 */

                public void run() {
                    DataBaseCameraSetting setter = new DataBaseCameraSetting();
                    setter.setReceiverId(DJICameraAbstraction.this.getReceiverIdByIndex());
                    setter.setCmdId("Photo");
                    setter.setValue(MODE[0]);
                    if (DataCameraGetPushStateInfo.getInstance().getCameraType(DJICameraAbstraction.this.getExpectedSenderIdByIndex()).equals(DataCameraGetPushStateInfo.CameraType.DJICameraTypeFC1102)) {
                        setter.setPackParam(3000, 3);
                    } else {
                        setter.setPackParam(timeout, 3);
                    }
                    setter.start(new DJIDataCallBack() {
                        /* class dji.sdksharedlib.hardware.abstractions.camera.DJICameraAbstraction.AnonymousClass8.AnonymousClass1 */

                        public void onSuccess(Object model) {
                            CallbackUtils.onSuccess(callback, (Object) null);
                        }

                        public void onFailure(Ccode ccode) {
                            if (DJICameraError.getDJIError(ccode) == DJICameraError.COMMON_TIMEOUT) {
                                CallbackUtils.onSuccess(callback, (Object) null);
                            } else {
                                CallbackUtils.onFailure(callback, ccode);
                            }
                        }
                    });
                }
            };
            Runnable runSetTimeLapseParam = new Runnable() {
                /* class dji.sdksharedlib.hardware.abstractions.camera.DJICameraAbstraction.AnonymousClass9 */

                public void run() {
                    if (DJICameraAbstraction.this.timeLapseSettings == null) {
                        CallbackUtils.onFailure(callback, DJICameraError.COMMON_EXECUTION_FAILED);
                    } else {
                        ((DataCameraSetTimeParams) DataCameraSetTimeParams.getInstance().setReceiverId(DJICameraAbstraction.this.getReceiverIdByIndex(), DataCameraSetTimeParams.class)).setNum(DJICameraAbstraction.this.timeLapseSettings.getInterval()).setPeriod(DJICameraAbstraction.this.timeLapseSettings.getDuration()).setType(DataCameraSetTimeParams.TYPE.Timelapse).start(new DJIDataCallBack() {
                            /* class dji.sdksharedlib.hardware.abstractions.camera.DJICameraAbstraction.AnonymousClass9.AnonymousClass1 */

                            public void onSuccess(Object model) {
                                DJICameraAbstraction.this.handler.post(runStartShootPhoto);
                            }

                            public void onFailure(Ccode ccode) {
                                CallbackUtils.onFailure(callback, ccode);
                            }
                        });
                    }
                }
            };
            Runnable runSetIntervalParam = new Runnable() {
                /* class dji.sdksharedlib.hardware.abstractions.camera.DJICameraAbstraction.AnonymousClass10 */

                public void run() {
                    if (DJICameraAbstraction.this.timeIntervalSettings == null) {
                        CallbackUtils.onFailure(callback, DJICameraError.COMMON_EXECUTION_FAILED);
                    } else {
                        ((DataCameraSetTimeParams) DataCameraSetTimeParams.getInstance().setReceiverId(DJICameraAbstraction.this.getReceiverIdByIndex(), DataCameraSetTimeParams.class)).setNum(DJICameraAbstraction.this.timeIntervalSettings.getCaptureCount()).setPeriod(DJICameraAbstraction.this.timeIntervalSettings.getTimeIntervalInSeconds()).setType(DataCameraSetTimeParams.TYPE.Single).start(new DJIDataCallBack() {
                            /* class dji.sdksharedlib.hardware.abstractions.camera.DJICameraAbstraction.AnonymousClass10.AnonymousClass1 */

                            public void onSuccess(Object model) {
                                DJICameraAbstraction.this.handler.post(runStartShootPhoto);
                            }

                            public void onFailure(Ccode ccode) {
                                CallbackUtils.onFailure(callback, ccode);
                            }
                        });
                    }
                }
            };
            Runnable runStartPseudoCameraAction = new Runnable() {
                /* class dji.sdksharedlib.hardware.abstractions.camera.DJICameraAbstraction.AnonymousClass11 */

                public void run() {
                    final DataEyeSetPseudoCameraControl setter = new DataEyeSetPseudoCameraControl();
                    setter.setPseudoCameraCmd(DataEyeSetPseudoCameraControl.PseudoCameraCmd.PSEUDO_CAMERA_CMD_CAPTURE).start(new DJIDataCallBack() {
                        /* class dji.sdksharedlib.hardware.abstractions.camera.DJICameraAbstraction.AnonymousClass11.AnonymousClass1 */

                        public void onSuccess(Object model) {
                            if (setter.getPseudoCameraCmdResult() != DataEyeSetPseudoCameraControl.PseudoCameraCmdResult.PSEUDO_CAMERA_ACK_SUCCESS) {
                                CallbackUtils.onFailure(callback, DJICameraError.COMMON_EXECUTION_FAILED);
                            }
                        }

                        public void onFailure(Ccode ccode) {
                            if (DJICameraError.getDJIError(ccode) == DJIError.COMMON_TIMEOUT) {
                                CallbackUtils.onSuccess(callback, (Object) null);
                            } else {
                                CallbackUtils.onFailure(callback, DJICameraError.getDJIError(ccode));
                            }
                        }
                    });
                }
            };
            if (mode != SettingsDefinitions.ShootPhotoMode.TIME_LAPSE) {
                if (mode == SettingsDefinitions.ShootPhotoMode.SINGLE) {
                    MODE[0] = SettingsDefinitions.ShootPhotoMode.SINGLE.getInternalTypeValue();
                } else if (mode == SettingsDefinitions.ShootPhotoMode.HDR) {
                    if (isHDRPhotoSupported()) {
                        MODE[0] = SettingsDefinitions.ShootPhotoMode.HDR.getInternalTypeValue();
                    } else {
                        CallbackUtils.onFailure(callback, DJICameraError.COMMON_UNSUPPORTED);
                        return;
                    }
                } else if (mode == SettingsDefinitions.ShootPhotoMode.BURST) {
                    MODE[0] = SettingsDefinitions.ShootPhotoMode.BURST.getInternalTypeValue();
                } else if (mode == SettingsDefinitions.ShootPhotoMode.RAW_BURST) {
                    MODE[0] = SettingsDefinitions.ShootPhotoMode.RAW_BURST.getInternalTypeValue();
                } else if (mode == SettingsDefinitions.ShootPhotoMode.AEB) {
                    MODE[0] = SettingsDefinitions.ShootPhotoMode.AEB.getInternalTypeValue();
                } else if (mode == SettingsDefinitions.ShootPhotoMode.INTERVAL) {
                    MODE[0] = SettingsDefinitions.ShootPhotoMode.INTERVAL.getInternalTypeValue();
                    this.handler.post(runSetIntervalParam);
                    return;
                } else if (mode == SettingsDefinitions.ShootPhotoMode.PANORAMA || mode == SettingsDefinitions.ShootPhotoMode.SHALLOW_FOCUS) {
                    if (!isPseudoCameraEnable()) {
                        CallbackUtils.onFailure(callback, DJICameraError.CANNOT_SET_PARAMETERS_IN_THIS_STATE);
                        return;
                    } else if (isAircraftSupportPanoORShallowFocus()) {
                        this.handler.post(runStartPseudoCameraAction);
                        return;
                    } else {
                        CallbackUtils.onFailure(callback, DJICameraError.COMMON_UNSUPPORTED);
                        return;
                    }
                } else if (mode == SettingsDefinitions.ShootPhotoMode.EHDR) {
                    MODE[0] = SettingsDefinitions.ShootPhotoMode.EHDR.getInternalTypeValue();
                } else if (mode == SettingsDefinitions.ShootPhotoMode.HYPER_LIGHT) {
                    MODE[0] = SettingsDefinitions.ShootPhotoMode.HYPER_LIGHT.getInternalTypeValue();
                } else {
                    CallbackUtils.onFailure(callback, DJICameraError.COMMON_PARAM_ILLEGAL);
                    return;
                }
                this.handler.post(runStartShootPhoto);
            } else if (isTimeLapseSupported()) {
                MODE[0] = SettingsDefinitions.ShootPhotoMode.TIME_LAPSE.getInternalTypeValue();
                this.handler.post(runSetTimeLapseParam);
            } else {
                CallbackUtils.onFailure(callback, DJICameraError.COMMON_UNSUPPORTED);
            }
        }
    }

    private boolean isAircraftSupportPanoORShallowFocus() {
        Model model = (Model) CacheHelper.getProduct(ProductKeys.MODEL_NAME);
        return model == Model.Spark || model == Model.MAVIC_AIR || CommonUtil.isMavic2SeriesProduct();
    }

    private boolean isPseudoCameraEnable() {
        return !DataCameraGetPushStateInfo.getInstance().beInTrackingMode(getExpectedSenderIdByIndex());
    }

    @Action(CameraKeys.STOP_SHOOT_PHOTO)
    public void stopShootPhoto(final DJISDKCacheHWAbstraction.InnerCallback callback) {
        Object isShootingPanoram = CacheHelper.getCamera(this.index, CameraKeys.IS_SHOOTING_PANORAMA_PHOTO);
        Object isShootingShallowFocus = CacheHelper.getCamera(this.index, CameraKeys.IS_SHOOTING_SHALLOW_FOCUS_PHOTO);
        if ((isShootingPanoram != null && ((Boolean) isShootingPanoram).booleanValue()) || (isShootingShallowFocus != null && ((Boolean) isShootingShallowFocus).booleanValue())) {
            final DataEyeSetPseudoCameraControl setter = new DataEyeSetPseudoCameraControl();
            setter.setPseudoCameraCmd(DataEyeSetPseudoCameraControl.PseudoCameraCmd.PSEUDO_CAMERA_CMD_ABORT).start(new DJIDataCallBack() {
                /* class dji.sdksharedlib.hardware.abstractions.camera.DJICameraAbstraction.AnonymousClass12 */

                public void onSuccess(Object model) {
                    if (setter.getPseudoCameraCmdResult() == DataEyeSetPseudoCameraControl.PseudoCameraCmdResult.PSEUDO_CAMERA_ACK_SUCCESS) {
                        CallbackUtils.onSuccess(callback, (Object) null);
                    } else {
                        CallbackUtils.onFailure(callback, DJICameraError.COMMON_EXECUTION_FAILED);
                    }
                }

                public void onFailure(Ccode ccode) {
                    if (CacheHelper.getCamera(DJICameraAbstraction.this.index, CameraKeys.SHOOT_PHOTO_MODE) == SettingsDefinitions.ShootPhotoMode.PANORAMA) {
                        CallbackUtils.onSuccess(callback, (Object) null);
                    } else {
                        CallbackUtils.onFailure(callback, DJICameraError.COMMON_EXECUTION_FAILED);
                    }
                }
            });
        } else if (canStopShootPhoto(DataCameraGetPushStateInfo.getInstance().getCameraType(getExpectedSenderIdByIndex()))) {
            DataBaseCameraSetting setter2 = new DataBaseCameraSetting();
            setter2.setReceiverId(getReceiverIdByIndex());
            setter2.setCmdId("Photo");
            setter2.setValue(0);
            setter2.setPackParam(0, 1);
            setter2.start(new DJIDataCallBack() {
                /* class dji.sdksharedlib.hardware.abstractions.camera.DJICameraAbstraction.AnonymousClass13 */

                public void onSuccess(Object model) {
                    CallbackUtils.onSuccess(callback, (Object) null);
                }

                public void onFailure(Ccode ccode) {
                    CallbackUtils.onFailure(callback, DJICameraError.getDJIError(ccode));
                }
            });
        } else {
            CallbackUtils.onFailure(callback, DJICameraError.COMMON_EXECUTION_FAILED);
        }
    }

    private boolean canStopShootPhoto(DataCameraGetPushStateInfo.CameraType cameraType2) {
        return !CameraUtils.isPhotoActionExecutable(this.index) || (DataCameraGetPushStateInfo.CameraType.DJICameraTypeFC1705 == cameraType2 && !CameraUtils.isPhotoActionExecutable(2));
    }

    @Action(CameraKeys.START_RECORD_VIDEO)
    public void startRecordVideo(final DJISDKCacheHWAbstraction.InnerCallback callback) {
        if (CacheHelper.getCamera(this.index, "Mode") != SettingsDefinitions.CameraMode.RECORD_VIDEO && CacheHelper.getCamera(this.index, "Mode") != SettingsDefinitions.CameraMode.BROADCAST) {
            CallbackUtils.onFailure(callback, DJICameraError.UNSUPPORTED_CMD_STATE);
        } else if (!CameraUtils.isStorageAvailable(this.index)) {
            CallbackUtils.onFailure(callback, DJICameraError.UNSUPPORTED_CMD_STATE);
        } else if (!CameraUtils.isInActionMode(this.index) || !CameraUtils.isRecordActionExecutable(this.index)) {
            CallbackUtils.onFailure(callback, DJICameraError.UNSUPPORTED_CMD_STATE);
        } else {
            if (DataCameraGetPushStateInfo.CameraType.DJICameraTypeFC1705 == DataCameraGetPushStateInfo.getInstance().getCameraType(getExpectedSenderIdByIndex())) {
                SettingsDefinitions.VideoFileFormat videoFileFormat = (SettingsDefinitions.VideoFileFormat) CacheHelper.getCamera(2, CameraKeys.VIDEO_FILE_FORMAT);
                if ((videoFileFormat == SettingsDefinitions.VideoFileFormat.SEQ || videoFileFormat == SettingsDefinitions.VideoFileFormat.TIFF_SEQ) && !CameraUtils.isSDCardReady(2)) {
                    CallbackUtils.onFailure(callback, DJICameraError.SD_CARD_ERROR);
                    return;
                } else if (!CameraUtils.isInActionMode(2) || !CameraUtils.isRecordActionExecutable(2)) {
                    CallbackUtils.onFailure(callback, DJICameraError.UNSUPPORTED_CMD_STATE);
                    return;
                }
            }
            DataBaseCameraSetting setter = new DataBaseCameraSetting();
            setter.setReceiverId(getReceiverIdByIndex());
            setter.setCmdId("Record");
            setter.setValue(1);
            setter.setPackParam(ConnectionResult.DRIVE_EXTERNAL_STORAGE_REQUIRED, 3);
            setter.start(new DJIDataCallBack() {
                /* class dji.sdksharedlib.hardware.abstractions.camera.DJICameraAbstraction.AnonymousClass14 */

                public void onSuccess(Object model) {
                    CallbackUtils.onSuccess(callback, (Object) null);
                }

                public void onFailure(Ccode ccode) {
                    if ((DJICameraError.getDJIError(ccode) == DJICameraError.EXEC_TIMEOUT || DJICameraError.getDJIError(ccode) == DJIError.COMMON_TIMEOUT) && ((Boolean) CacheHelper.getCamera(DJICameraAbstraction.this.index, CameraKeys.IS_RECORDING)).booleanValue()) {
                        CallbackUtils.onSuccess(callback, (Object) null);
                    } else {
                        CallbackUtils.onFailure(callback, DJICameraError.getDJIError(ccode));
                    }
                }
            });
        }
    }

    @Action(CameraKeys.STOP_RECORD_VIDEO)
    public void stopRecordVideo(final DJISDKCacheHWAbstraction.InnerCallback callback) {
        stopRecordTimeTimer();
        ProductType type = DJIProductManager.getInstance().getType();
        if (getReceiverIdByIndex() != 0 || Model.PHANTOM_4_PRO_V2 == CacheHelper.getProduct(ProductKeys.MODEL_NAME) || CommonUtil.isPM420Platform()) {
            DataBaseCameraSetting setter = new DataBaseCameraSetting();
            setter.setReceiverId(getReceiverIdByIndex());
            setter.setCmdId("Record");
            setter.setValue(0);
            setter.setPackParam(0, 1);
            setter.start(new DJIDataCallBack() {
                /* class dji.sdksharedlib.hardware.abstractions.camera.DJICameraAbstraction.AnonymousClass15 */

                public void onSuccess(Object model) {
                    CallbackUtils.onSuccess(callback, (Object) null);
                }

                public void onFailure(Ccode ccode) {
                    CallbackUtils.onFailure(callback, DJICameraError.getDJIError(ccode));
                }
            });
        } else if (((Boolean) CacheHelper.getCamera(CameraKeys.IS_RECORDING)).booleanValue()) {
            DataSpecialControl.getInstance().setRecordType(false).start(20);
            CallbackUtils.onSuccess(callback, (Object) null);
        } else {
            CallbackUtils.onFailure(callback, DJICameraError.COMMON_EXECUTION_FAILED);
        }
    }

    /* access modifiers changed from: protected */
    public void checkIfNeedWaitForPauseTheStabilization(DJISDKCacheHWAbstraction.InnerCallback callback, DataSingleSendAppStateForStabilization.CameraState state) {
        new DataSingleSendAppStateForStabilization().setCameraState(state).start();
    }

    @Setter(CameraKeys.VIDEO_FILE_FORMAT)
    public void setVideoFileFormat(SettingsDefinitions.VideoFileFormat videoFileFormat, final DJISDKCacheHWAbstraction.InnerCallback callback) {
        if (videoFileFormat != SettingsDefinitions.VideoFileFormat.UNKNOWN) {
            DataBaseCameraSetting setter = new DataBaseCameraSetting();
            setter.setCmdId("VideoStoreFormat");
            setter.setValue(videoFileFormat.value());
            setter.setReceiverId(getReceiverIdByIndex());
            setter.start(new DJIDataCallBack() {
                /* class dji.sdksharedlib.hardware.abstractions.camera.DJICameraAbstraction.AnonymousClass16 */

                public void onSuccess(Object model) {
                    CallbackUtils.onSuccess(callback, model);
                }

                public void onFailure(Ccode ccode) {
                    CallbackUtils.onFailure(callback, DJICameraError.getDJIError(ccode));
                }
            });
        } else if (callback != null) {
            callback.onFails(DJICameraError.COMMON_PARAM_ILLEGAL);
        }
    }

    @Setter(CameraKeys.CAMERA_STORAGE_LOCATION)
    public void setCameraStorageLocation(SettingsDefinitions.StorageLocation mode, DJISDKCacheHWAbstraction.InnerCallback callback) {
        if (!isInternalStorageSupported()) {
            CallbackUtils.onFailure(callback, DJIError.COMMON_UNSUPPORTED);
        } else if (mode == null || mode == SettingsDefinitions.StorageLocation.UNKNOWN) {
            CallbackUtils.onFailure(callback, DJICameraError.INVALID_PARAMETERS);
        } else {
            DataCameraSetStorageInfo setter = new DataCameraSetStorageInfo();
            setter.setStorageGet(false);
            setter.setStorageLocation(DataCameraSetStorageInfo.Storage.find(mode.value()));
            setter.setReceiverId(getReceiverIdByIndex());
            setter.start(CallbackUtils.defaultCB(callback, DJICameraError.class));
        }
    }

    @Action(CameraKeys.START_QUICK_SHOT_RECORD_VIDEO_IN_CACHE)
    public void startQuickShotRecordVideoInCache(DJISDKCacheHWAbstraction.InnerCallback callback, Boolean needClean) {
        RecorderManager.start(RecorderManager.BufferMode.QUICK_MOVIE);
        RecorderManager.setBufferAutoClean(needClean.booleanValue());
        EventBus.getDefault().post(RecorderManager.Service_Action.START_RECORD);
        recordInCacheCdlInit(1);
        recordInCacheCdlWait(1, TimeUnit.SECONDS);
        startRecordTimeTimer();
    }

    @Action(CameraKeys.START_RECORD_VIDEO_IN_CACHE)
    public void startRecordVideoInCache(DJISDKCacheHWAbstraction.InnerCallback callback, Boolean needClean) {
        RecorderManager.start(RecorderManager.BufferMode.GDR_ONLINE);
        RecorderManager.setBufferAutoClean(needClean.booleanValue());
        EventBus.getDefault().post(RecorderManager.Service_Action.START_RECORD);
        recordInCacheCdlInit(1);
        recordInCacheCdlWait(1, TimeUnit.SECONDS);
        startRecordTimeTimer();
        CallbackUtils.onSuccess(callback, (Object) null);
    }

    @Action(CameraKeys.STOP_RECORD_VIDEO_IN_CACHE)
    public void stopRecordVideoInCache(DJISDKCacheHWAbstraction.InnerCallback callback) {
        if (CacheHelper.getCamera(this.index, CameraKeys.RECORDING_STATE) == CameraRecordingState.RECORDING_TO_CACHE) {
            EventBus.getDefault().post(RecorderManager.Service_Action.END_RECORD);
            stopRecordTimeTimer();
            DataCameraGetPushStateInfo.getInstance().swapValidData(getExpectedSenderIdByIndex());
            onEvent3BackgroundThread(DataCameraGetPushStateInfo.getInstance());
            CallbackUtils.onSuccess(callback, (Object) null);
            return;
        }
        CallbackUtils.onFailure(callback, DJIError.COMMON_UNKNOWN);
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEvent3BackgroundThread(RecorderBase.RecorderStatus status) {
        recordInCacheCdlCountDown();
        if (status == RecorderBase.RecorderStatus.STANDBY && CacheHelper.getCamera(this.index, CameraKeys.RECORDING_STATE) == CameraRecordingState.RECORDING_TO_CACHE) {
            notifyValueChangeForKeyPath(CameraRecordingState.NOT_RECORDING, convertKeyToPath(CameraKeys.RECORDING_STATE));
        }
    }

    /* access modifiers changed from: protected */
    public SettingsDefinitions.ThermalDigitalZoomFactor checkDigitalZoomScale(int scale) {
        if (scale == 100) {
            return SettingsDefinitions.ThermalDigitalZoomFactor.X_1;
        }
        if (scale == 200) {
            return SettingsDefinitions.ThermalDigitalZoomFactor.X_2;
        }
        if (scale == 400) {
            return SettingsDefinitions.ThermalDigitalZoomFactor.X_4;
        }
        if (scale != 800) {
            return SettingsDefinitions.ThermalDigitalZoomFactor.UNKNOWN;
        }
        DataCameraGetPushStateInfo.CameraType type = DataCameraGetPushStateInfo.getInstance().getCameraType(getExpectedSenderIdByIndex());
        if (type == DataCameraGetPushStateInfo.CameraType.DJICameraTypeTau640 || type == DataCameraGetPushStateInfo.CameraType.DJICameraTypeFC1705 || type == DataCameraGetPushStateInfo.CameraType.DJICameraTypeFC2403) {
            return SettingsDefinitions.ThermalDigitalZoomFactor.X_8;
        }
        return SettingsDefinitions.ThermalDigitalZoomFactor.X_4;
    }
}
