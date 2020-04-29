package dji.internal.diagnostics.handler;

import dji.common.product.Model;
import dji.component.appstate.IAppStateService;
import dji.diagnostics.model.DJIDiagnostics;
import dji.diagnostics.model.DJIDiagnosticsError;
import dji.diagnostics.model.DJIDiagnosticsType;
import dji.internal.diagnostics.DJIDiagnosticsImpl;
import dji.internal.diagnostics.DiagnosticsBaseHandler;
import dji.internal.diagnostics.DiagnosticsHandlerUpdateObserver;
import dji.internal.diagnostics.handler.util.DiagnosticsIfModel;
import dji.internal.diagnostics.handler.util.DiagnosticsModelBase;
import dji.internal.logics.CommonUtil;
import dji.midware.component.DJIComponentManager;
import dji.midware.data.model.P3.DataCameraGetPushStateInfo;
import dji.midware.data.model.P3.DataCameraGetPushStorageInfo;
import dji.midware.data.model.P3.DataCameraGetStateInfo;
import dji.midware.data.model.P3.DataDm368_gGetPushCheckStatus;
import dji.midware.util.DoubleCameraSupportUtil;
import dji.sdksharedlib.extension.CacheHelper;
import dji.sdksharedlib.keycatalog.CameraKeys;
import dji.sdksharedlib.keycatalog.DJISDKCacheKey;
import dji.sdksharedlib.keycatalog.FlightControllerKeys;
import dji.sdksharedlib.keycatalog.ProductKeys;
import dji.sdksharedlib.listener.DJIParamAccessListener;
import dji.sdksharedlib.store.DJISDKCacheParamValue;
import dji.service.DJIAppServiceManager;
import java.util.HashSet;
import java.util.Set;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class CameraDiagnosticsHandler extends DiagnosticsBaseHandler implements DJIParamAccessListener {
    private static final int FREE_SIZE_THRESHOLD = 200;
    private static final DJIDiagnosticsType TYPE = DJIDiagnosticsType.CAMERA;
    private int cameraIndex = 0;
    private boolean hasCameraAbnormalReboot = false;
    private boolean hasEncryptionError = false;
    private boolean hasFirmwareUpgradeException;
    private boolean hasInAirUseByOverHeat = false;
    private boolean hasInternalExceptionPartlyCalibrated;
    private boolean hasInternalExceptionRCAMDisconnect;
    private boolean hasInternalExceptionUncalibrated;
    private boolean hasInternalExceptionUnencrypted;
    private boolean hasInternalStorageFilePreparing = false;
    private boolean hasInternalStorageFileSystemFault = false;
    private boolean hasInternalStorageIndexRunOut = false;
    private boolean hasInternalStorageInvalid = false;
    private boolean hasInternalStorageIsBusy = false;
    private boolean hasInternalStorageIsFormatting = false;
    private boolean hasInternalStorageIsInitializing = false;
    private boolean hasInternalStorageRecommendFormatting = false;
    private boolean hasInternalStorageSpaceFull = false;
    private boolean hasInternalStorageUnformmatted = false;
    private boolean hasInternalStorageUnknownError = false;
    private boolean hasInternalStorageWriteProtect = false;
    private boolean hasNoInternalStorage = false;
    private boolean hasNoSdCard = false;
    private boolean hasOverHeatLevel1 = false;
    private boolean hasOverHeatStopRecord = false;
    private boolean hasOverheatException;
    private boolean hasSdCardFilePreparing = false;
    private boolean hasSdCardFileSystemFault = false;
    private boolean hasSdCardIndexRunOut = false;
    private boolean hasSdCardInvalid = false;
    private boolean hasSdCardIsBusy = false;
    private boolean hasSdCardIsFormatting = false;
    private boolean hasSdCardIsInitializing = false;
    private boolean hasSdCardNotEnough = false;
    private boolean hasSdCardRecommendFormatting = false;
    private boolean hasSdCardSpaceFull = false;
    private boolean hasSdCardSpecSlowSpeed = false;
    private boolean hasSdCardUnformmatted = false;
    private boolean hasSdCardUnknownError = false;
    private boolean hasSdCardWriteProtect = false;
    private boolean hasSdCardWriteSlow = false;
    private boolean hasSensorException;
    private boolean hasUsbConnected = false;
    private boolean lastInAirUseByOverHeat = false;
    private boolean lastIsCameraAbnormalReboot = false;
    private boolean lastIsEncryptionError = false;
    private boolean lastIsFirmwareUpgradeError = false;
    private boolean lastIsInternalErrorPartlyCalibrated;
    private boolean lastIsInternalErrorRCAMDisconnect;
    private boolean lastIsInternalErrorUncalibrated;
    private boolean lastIsInternalErrorUnencrypted;
    private boolean lastIsInternalStorageFilePreparing = false;
    private boolean lastIsInternalStorageFileSystemFault = false;
    private boolean lastIsInternalStorageIndexRunOut = false;
    private boolean lastIsInternalStorageInvalid = false;
    private boolean lastIsInternalStorageIsBusy = false;
    private boolean lastIsInternalStorageIsFormatting = false;
    private boolean lastIsInternalStorageIsInitializing = false;
    private boolean lastIsInternalStorageRecommendFormatting = false;
    private boolean lastIsInternalStorageSpaceFull = false;
    private boolean lastIsInternalStorageUnformmatted = false;
    private boolean lastIsInternalStorageUnknownError = false;
    private boolean lastIsInternalStorageWriteProtect = false;
    private boolean lastIsNoInternalStorage = false;
    private boolean lastIsNoSdCard = false;
    private boolean lastIsOverheat = false;
    private boolean lastIsSdCardFilePreparing = false;
    private boolean lastIsSdCardFileSystemFault = false;
    private boolean lastIsSdCardIndexRunOut = false;
    private boolean lastIsSdCardInvalid = false;
    private boolean lastIsSdCardIsBusy = false;
    private boolean lastIsSdCardIsFormatting = false;
    private boolean lastIsSdCardIsInitializing = false;
    private boolean lastIsSdCardRecommendFormatting = false;
    private boolean lastIsSdCardSpaceFull = false;
    private boolean lastIsSdCardSpecSlowSpeed = false;
    private boolean lastIsSdCardUnformmatted = false;
    private boolean lastIsSdCardUnknownError = false;
    private boolean lastIsSdCardWriteProtect = false;
    private boolean lastIsSdCardWriteSlow = false;
    private boolean lastIsSensorError = false;
    private boolean lastIsUsbConnected = false;
    private boolean lastOverHeatLevel1 = false;
    private boolean lastOverHeatStopRecord = false;
    private boolean lastSdCardNotEnough = false;
    private DiagnosticsModelBase<DataDm368_gGetPushCheckStatus> mVideoDecoderConnect;
    private DiagnosticsModelBase<DataDm368_gGetPushCheckStatus> mVideoDecoderEncryption;

    public CameraDiagnosticsHandler(DiagnosticsHandlerUpdateObserver observer) {
        super(observer);
        initDiagnosticsModels();
    }

    private void initDiagnosticsModels() {
        this.mVideoDecoderEncryption = new DiagnosticsIfModel((int) DJIDiagnosticsError.Camera.VIDEO_DECODER_ENCRYPTION_ERROR, CameraDiagnosticsHandler$$Lambda$0.$instance);
        this.mVideoDecoderConnect = new DiagnosticsIfModel((int) DJIDiagnosticsError.Camera.VIDEO_DECODER_CONNECT_TO_DESERIALIZER_ERROR, CameraDiagnosticsHandler$$Lambda$1.$instance);
    }

    static final /* synthetic */ boolean lambda$initDiagnosticsModels$0$CameraDiagnosticsHandler(DataDm368_gGetPushCheckStatus check) {
        return !CommonUtil.isKumquatSeries(null) && !CommonUtil.isWM240(null) && check.getEncryptStatus();
    }

    static final /* synthetic */ boolean lambda$initDiagnosticsModels$1$CameraDiagnosticsHandler(DataDm368_gGetPushCheckStatus check) {
        return !CommonUtil.isKumquatSeries(null) && !CommonUtil.isWM240(null) && check.get68013ConnectStatus();
    }

    /* access modifiers changed from: protected */
    public void onRegister() {
        if (DataCameraGetPushStateInfo.getInstance().isGetted()) {
            onEvent3BackgroundThread(DataCameraGetPushStateInfo.getInstance());
        }
        if (DataCameraGetPushStorageInfo.getInstance().isGetted()) {
            onEvent3BackgroundThread(DataCameraGetPushStorageInfo.getInstance());
        }
        if (DataDm368_gGetPushCheckStatus.getInstance().isGetted()) {
            onEvent3BackgroundThread(DataDm368_gGetPushCheckStatus.getInstance());
        }
        CacheHelper.addCameraListener(this, CameraKeys.CAMERA_TEMPERATURE_STATE);
        CacheHelper.addFlightControllerListener(this, FlightControllerKeys.ARE_MOTOR_ON);
        handleCameraOverHeat();
    }

    /* access modifiers changed from: protected */
    public void onUnregister() {
        CacheHelper.removeListener(this);
    }

    public CameraDiagnosticsHandler(DiagnosticsHandlerUpdateObserver observer, int cameraIndex2) {
        super(observer);
        this.cameraIndex = cameraIndex2;
    }

    public Set<DJIDiagnostics> getDiagnosisList() {
        Set<DJIDiagnostics> diagnosisList = null;
        if (DJIComponentManager.getInstance().getCameraComponentType(this.cameraIndex) != DJIComponentManager.CameraComponentType.None && (this.cameraIndex != 2 || DJIComponentManager.getInstance().getCameraComponentType(0) == DJIComponentManager.CameraComponentType.XT2)) {
            diagnosisList = new HashSet<>();
            if (this.hasFirmwareUpgradeException) {
                diagnosisList.add(new DJIDiagnosticsImpl(TYPE, (int) DJIDiagnosticsError.Camera.UPGRADE_ERROR, this.cameraIndex));
            }
            if (this.hasSensorException) {
                diagnosisList.add(new DJIDiagnosticsImpl(TYPE, (int) DJIDiagnosticsError.Camera.SENSOR_ERROR, this.cameraIndex));
            }
            if (this.hasOverheatException) {
                diagnosisList.add(new DJIDiagnosticsImpl(TYPE, (int) DJIDiagnosticsError.Camera.OVER_HEAT, this.cameraIndex));
            }
            if (this.hasInternalExceptionUnencrypted) {
                diagnosisList.add(new DJIDiagnosticsImpl(TYPE, (int) DJIDiagnosticsError.Camera.INTERNAL_ERROR_UNENCRYPTED, this.cameraIndex));
            }
            if (this.hasInternalExceptionUncalibrated) {
                diagnosisList.add(new DJIDiagnosticsImpl(TYPE, (int) DJIDiagnosticsError.Camera.INTERNAL_ERROR_UNCALIBRATED, this.cameraIndex));
            }
            if (this.hasInternalExceptionPartlyCalibrated) {
                diagnosisList.add(new DJIDiagnosticsImpl(TYPE, (int) DJIDiagnosticsError.Camera.INTERNAL_ERROR_PARTLY_CALIBRATED, this.cameraIndex));
            }
            if (this.hasInternalExceptionRCAMDisconnect) {
                diagnosisList.add(new DJIDiagnosticsImpl(TYPE, (int) DJIDiagnosticsError.Camera.INTERNAL_ERROR_RCAM_DISCONNECT, this.cameraIndex));
            }
            if (this.hasEncryptionError) {
                diagnosisList.add(new DJIDiagnosticsImpl(TYPE, (int) DJIDiagnosticsError.Camera.ENCRYPTION_ERROR, this.cameraIndex));
            }
            if (this.hasCameraAbnormalReboot) {
                diagnosisList.add(new DJIDiagnosticsImpl(TYPE, (int) DJIDiagnosticsError.Camera.ABNORMAL_REBOOT, this.cameraIndex));
            }
            if (this.hasUsbConnected) {
                diagnosisList.add(new DJIDiagnosticsImpl(TYPE, (int) DJIDiagnosticsError.Camera.USB_CONNECTED, this.cameraIndex));
            }
            if (this.hasNoSdCard) {
                diagnosisList.add(new DJIDiagnosticsImpl(TYPE, (int) DJIDiagnosticsError.Camera.NO_SD_CARD, this.cameraIndex));
            }
            if (this.hasSdCardInvalid) {
                diagnosisList.add(new DJIDiagnosticsImpl(TYPE, (int) DJIDiagnosticsError.Camera.SD_CARD_INVALID, this.cameraIndex));
            }
            if (this.hasSdCardWriteProtect) {
                diagnosisList.add(new DJIDiagnosticsImpl(TYPE, (int) DJIDiagnosticsError.Camera.SD_CARD_WRITE_PROTECT, this.cameraIndex));
            }
            if (this.hasSdCardUnformmatted) {
                diagnosisList.add(new DJIDiagnosticsImpl(TYPE, 1009, this.cameraIndex));
            }
            if (this.hasSdCardIsFormatting) {
                diagnosisList.add(new DJIDiagnosticsImpl(TYPE, 1010, this.cameraIndex));
            }
            if (this.hasSdCardFileSystemFault) {
                diagnosisList.add(new DJIDiagnosticsImpl(TYPE, (int) DJIDiagnosticsError.Camera.SD_CARD_FILE_SYSTEM_FAULT, this.cameraIndex));
            }
            if (this.hasSdCardIsBusy) {
                diagnosisList.add(new DJIDiagnosticsImpl(TYPE, (int) DJIDiagnosticsError.Camera.SD_CARD_IS_BUSY, this.cameraIndex));
            }
            if (this.hasSdCardSpaceFull) {
                diagnosisList.add(new DJIDiagnosticsImpl(TYPE, (int) DJIDiagnosticsError.Camera.SD_CARD_SPACE_FULL, this.cameraIndex));
            }
            if (this.hasSdCardSpecSlowSpeed) {
                diagnosisList.add(new DJIDiagnosticsImpl(TYPE, (int) DJIDiagnosticsError.Camera.SD_CARD_SPEC_SLOW_SPEED, this.cameraIndex));
            }
            if (this.hasSdCardUnknownError) {
                diagnosisList.add(new DJIDiagnosticsImpl(TYPE, (int) DJIDiagnosticsError.Camera.SD_CARD_UNKNOWN_ERROR, this.cameraIndex));
            }
            if (this.hasSdCardIndexRunOut) {
                diagnosisList.add(new DJIDiagnosticsImpl(TYPE, (int) DJIDiagnosticsError.Camera.SD_CARD_INDEX_RUN_OUT, this.cameraIndex));
            }
            if (this.hasSdCardIsInitializing) {
                diagnosisList.add(new DJIDiagnosticsImpl(TYPE, (int) DJIDiagnosticsError.Camera.SD_CARD_IS_INITIALIZING, this.cameraIndex));
            }
            if (this.hasSdCardRecommendFormatting) {
                diagnosisList.add(new DJIDiagnosticsImpl(TYPE, (int) DJIDiagnosticsError.Camera.SD_CARD_RECOMMEND_FORMATTING, this.cameraIndex));
            }
            if (this.hasSdCardFilePreparing) {
                diagnosisList.add(new DJIDiagnosticsImpl(TYPE, (int) DJIDiagnosticsError.Camera.SD_CARD_FILE_PREPARING, this.cameraIndex));
            }
            if (this.hasSdCardWriteSlow) {
                diagnosisList.add(new DJIDiagnosticsImpl(TYPE, (int) DJIDiagnosticsError.Camera.SD_CARD_WRITE_SLOW, this.cameraIndex));
            }
            if (this.hasNoInternalStorage) {
                diagnosisList.add(new DJIDiagnosticsImpl(TYPE, 1022, this.cameraIndex));
            }
            if (this.hasInternalStorageInvalid) {
                diagnosisList.add(new DJIDiagnosticsImpl(TYPE, (int) DJIDiagnosticsError.Camera.INTERNAL_STORAGE_INVALID, this.cameraIndex));
            }
            if (this.hasInternalStorageWriteProtect) {
                diagnosisList.add(new DJIDiagnosticsImpl(TYPE, 1024, this.cameraIndex));
            }
            if (this.hasInternalStorageUnformmatted) {
                diagnosisList.add(new DJIDiagnosticsImpl(TYPE, 1025, this.cameraIndex));
            }
            if (this.hasInternalStorageIsFormatting) {
                diagnosisList.add(new DJIDiagnosticsImpl(TYPE, 1026, this.cameraIndex));
            }
            if (this.hasInternalStorageFileSystemFault) {
                diagnosisList.add(new DJIDiagnosticsImpl(TYPE, 1027, this.cameraIndex));
            }
            if (this.hasInternalStorageIsBusy) {
                diagnosisList.add(new DJIDiagnosticsImpl(TYPE, 1028, this.cameraIndex));
            }
            if (this.hasInternalStorageSpaceFull) {
                diagnosisList.add(new DJIDiagnosticsImpl(TYPE, 1029, this.cameraIndex));
            }
            if (this.hasInternalStorageUnknownError) {
                diagnosisList.add(new DJIDiagnosticsImpl(TYPE, 1031, this.cameraIndex));
            }
            if (this.hasInternalStorageIndexRunOut) {
                diagnosisList.add(new DJIDiagnosticsImpl(TYPE, 1032, this.cameraIndex));
            }
            if (this.hasInternalStorageIsInitializing) {
                diagnosisList.add(new DJIDiagnosticsImpl(TYPE, 1033, this.cameraIndex));
            }
            if (this.hasInternalStorageRecommendFormatting) {
                diagnosisList.add(new DJIDiagnosticsImpl(TYPE, 1034, this.cameraIndex));
            }
            if (this.hasInternalStorageFilePreparing) {
                diagnosisList.add(new DJIDiagnosticsImpl(TYPE, 1035, this.cameraIndex));
            }
            if (this.hasOverHeatLevel1) {
                diagnosisList.add(new DJIDiagnosticsImpl(TYPE, DJIDiagnosticsError.Camera.CHIP_OVER_HEAT_WARNING, Boolean.valueOf(this.hasInAirUseByOverHeat), this.cameraIndex));
            }
            if (this.hasOverHeatStopRecord) {
                diagnosisList.add(new DJIDiagnosticsImpl(TYPE, (int) DJIDiagnosticsError.Camera.CHIP_OVER_HEAT_STOP_RECORD_WARNING, this.cameraIndex));
            }
            if (this.hasSdCardNotEnough) {
                diagnosisList.add(new DJIDiagnosticsImpl(TYPE, (int) DJIDiagnosticsError.Camera.SD_CARD_SPACE_NOT_ENOUGH, this.cameraIndex));
            }
            DiagnosticsModelBase<DataDm368_gGetPushCheckStatus> diagnosticsModelBase = this.mVideoDecoderEncryption;
            DJIDiagnosticsType dJIDiagnosticsType = TYPE;
            diagnosisList.getClass();
            diagnosticsModelBase.doIfError(dJIDiagnosticsType, CameraDiagnosticsHandler$$Lambda$2.get$Lambda(diagnosisList), this.cameraIndex);
            DiagnosticsModelBase<DataDm368_gGetPushCheckStatus> diagnosticsModelBase2 = this.mVideoDecoderConnect;
            DJIDiagnosticsType dJIDiagnosticsType2 = TYPE;
            diagnosisList.getClass();
            diagnosticsModelBase2.doIfError(dJIDiagnosticsType2, CameraDiagnosticsHandler$$Lambda$3.get$Lambda(diagnosisList), this.cameraIndex);
        }
        return diagnosisList;
    }

    public void reset(int componentIndex) {
        this.lastIsFirmwareUpgradeError = false;
        this.lastIsSensorError = false;
        this.lastIsOverheat = false;
        this.lastIsInternalErrorUnencrypted = false;
        this.lastIsInternalErrorUncalibrated = false;
        this.lastIsInternalErrorPartlyCalibrated = false;
        this.lastIsInternalErrorRCAMDisconnect = false;
        this.lastIsEncryptionError = false;
        this.lastIsCameraAbnormalReboot = false;
        this.lastIsUsbConnected = false;
        this.lastIsNoSdCard = false;
        this.lastIsNoInternalStorage = false;
        this.lastIsSdCardInvalid = false;
        this.lastIsSdCardWriteProtect = false;
        this.lastIsSdCardUnformmatted = false;
        this.lastIsSdCardIsFormatting = false;
        this.lastIsSdCardFileSystemFault = false;
        this.lastIsSdCardIsBusy = false;
        this.lastIsSdCardSpaceFull = false;
        this.lastIsSdCardSpecSlowSpeed = false;
        this.lastIsSdCardUnknownError = false;
        this.lastIsSdCardIndexRunOut = false;
        this.lastIsSdCardIsInitializing = false;
        this.lastIsSdCardRecommendFormatting = false;
        this.lastIsSdCardFilePreparing = false;
        this.lastIsSdCardWriteSlow = false;
        this.lastIsInternalStorageInvalid = false;
        this.lastIsInternalStorageWriteProtect = false;
        this.lastIsInternalStorageUnformmatted = false;
        this.lastIsInternalStorageIsFormatting = false;
        this.lastIsInternalStorageFileSystemFault = false;
        this.lastIsInternalStorageIsBusy = false;
        this.lastIsInternalStorageSpaceFull = false;
        this.lastIsInternalStorageUnknownError = false;
        this.lastIsInternalStorageIndexRunOut = false;
        this.lastIsInternalStorageIsInitializing = false;
        this.lastIsInternalStorageRecommendFormatting = false;
        this.lastIsInternalStorageFilePreparing = false;
        this.lastInAirUseByOverHeat = false;
        this.lastOverHeatLevel1 = false;
        this.lastOverHeatStopRecord = false;
        this.lastSdCardNotEnough = false;
        this.hasFirmwareUpgradeException = false;
        this.hasSensorException = false;
        this.hasOverheatException = false;
        this.hasInternalExceptionUnencrypted = false;
        this.hasInternalExceptionUncalibrated = false;
        this.hasInternalExceptionPartlyCalibrated = false;
        this.hasInternalExceptionRCAMDisconnect = false;
        this.hasEncryptionError = false;
        this.hasCameraAbnormalReboot = false;
        this.hasUsbConnected = false;
        this.hasNoSdCard = false;
        this.hasNoInternalStorage = false;
        this.hasSdCardInvalid = false;
        this.hasSdCardWriteProtect = false;
        this.hasSdCardUnformmatted = false;
        this.hasSdCardIsFormatting = false;
        this.hasSdCardFileSystemFault = false;
        this.hasSdCardIsBusy = false;
        this.hasSdCardSpaceFull = false;
        this.hasSdCardSpecSlowSpeed = false;
        this.hasSdCardUnknownError = false;
        this.hasSdCardIndexRunOut = false;
        this.hasSdCardIsInitializing = false;
        this.hasSdCardRecommendFormatting = false;
        this.hasSdCardFilePreparing = false;
        this.hasSdCardWriteSlow = false;
        this.hasInternalStorageInvalid = false;
        this.hasInternalStorageWriteProtect = false;
        this.hasInternalStorageUnformmatted = false;
        this.hasInternalStorageIsFormatting = false;
        this.hasInternalStorageFileSystemFault = false;
        this.hasInternalStorageIsBusy = false;
        this.hasInternalStorageSpaceFull = false;
        this.hasInternalStorageUnknownError = false;
        this.hasInternalStorageIndexRunOut = false;
        this.hasInternalStorageIsInitializing = false;
        this.hasInternalStorageRecommendFormatting = false;
        this.hasInternalStorageFilePreparing = false;
        this.hasOverHeatLevel1 = false;
        this.hasOverHeatStopRecord = false;
        this.hasInAirUseByOverHeat = false;
        this.hasSdCardNotEnough = false;
        this.mVideoDecoderConnect.reset();
        this.mVideoDecoderEncryption.reset();
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEvent3BackgroundThread(DataCameraGetPushStateInfo info) {
        if (info.getSenderId() == DoubleCameraSupportUtil.getCameraIdInProtocol(this.cameraIndex) && info.isGetted()) {
            postToDiagnosticBackgroudThread(new CameraDiagnosticsHandler$$Lambda$4(this, info));
        }
    }

    /* access modifiers changed from: package-private */
    public final /* synthetic */ void lambda$onEvent3BackgroundThread$2$CameraDiagnosticsHandler(DataCameraGetPushStateInfo info) {
        boolean changed = checkCameraState(info);
        if (!isSupportMultiStorage()) {
            changed = changed | checkSdCardState(info.getSDCardState(this.cameraIndex)) | checkSdFreeSize(info.getSDCardState(this.cameraIndex), info.getSDCardFreeSize());
        }
        if (changed) {
            notifyChange();
        }
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEvent3BackgroundThread(DataCameraGetPushStorageInfo info) {
        if (isSupportMultiStorage() && info.getSenderId() == DoubleCameraSupportUtil.getCameraIdInProtocol(this.cameraIndex) && info.isGetted()) {
            postToDiagnosticBackgroudThread(new CameraDiagnosticsHandler$$Lambda$5(this, info));
        }
    }

    /* access modifiers changed from: package-private */
    public final /* synthetic */ void lambda$onEvent3BackgroundThread$3$CameraDiagnosticsHandler(DataCameraGetPushStorageInfo info) {
        if ((checkSdCardState(info.getSDCardState()) | checkSdFreeSize(info.getSDCardState(), info.getSDCardFreeSize())) || checkInternalStorageState(info.getInnerStorageStatus())) {
            notifyChange();
        }
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEvent3BackgroundThread(DataDm368_gGetPushCheckStatus checkStatus) {
        if (this.cameraIndex == 0 && checkStatus.isGetted()) {
            postToDiagnosticBackgroudThread(new CameraDiagnosticsHandler$$Lambda$6(this, checkStatus));
        }
    }

    /* access modifiers changed from: package-private */
    public final /* synthetic */ void lambda$onEvent3BackgroundThread$4$CameraDiagnosticsHandler(DataDm368_gGetPushCheckStatus checkStatus) {
        if (this.mVideoDecoderConnect.statusApply(checkStatus) || this.mVideoDecoderEncryption.statusApply(checkStatus)) {
            notifyChange();
        }
    }

    private boolean checkSdFreeSize(DataCameraGetStateInfo.SDCardState storageState, int sizeInMB) {
        boolean changed = false;
        boolean isSdCardNotEnough = storageState == DataCameraGetStateInfo.SDCardState.Normal && sizeInMB < 200;
        if (this.lastSdCardNotEnough != isSdCardNotEnough) {
            this.hasSdCardNotEnough = isSdCardNotEnough;
            changed = true;
        }
        this.lastSdCardNotEnough = isSdCardNotEnough;
        return changed;
    }

    private boolean checkCameraState(DataCameraGetPushStateInfo info) {
        boolean changed = false;
        boolean isFirmwareUpgradeError = info.getFirmUpgradeErrorState() != DataCameraGetStateInfo.FirmErrorType.NO;
        boolean isSensorError = info.getSensorState();
        boolean isOverheat = info.getHotState() && CacheHelper.getProduct(ProductKeys.MODEL_NAME) != Model.WM160;
        boolean isEncryptionError = info.getEncryptStatus() != DataCameraGetPushStateInfo.EncryptStatus.CHECK_SUCCESS;
        boolean isUsbConnected = info.getUsbState();
        DataCameraGetPushStateInfo.InternalError internalError = DataCameraGetPushStateInfo.InternalError.find(info.getInternalError());
        boolean isInternalErrorUnencrypted = internalError == DataCameraGetPushStateInfo.InternalError.UNENCRYPTED;
        boolean isInternalErrorUncalibrated = internalError == DataCameraGetPushStateInfo.InternalError.UNCALIBRATION;
        boolean isInternalErrorPartlyCalibration = internalError == DataCameraGetPushStateInfo.InternalError.PARTCALIBRATION;
        boolean isInternalErrorRCAMDisconnect = internalError == DataCameraGetPushStateInfo.InternalError.CALIBRATIONEXCEPTION;
        IAppStateService appState = (IAppStateService) DJIAppServiceManager.getInstance().getService(IAppStateService.NAME);
        boolean isCameraAbnormalReboot = appState != null && appState.isInnerApp() && info.isCameraAbnormalReboot();
        if (!compareBooleans(new boolean[]{isFirmwareUpgradeError, isSensorError, isOverheat, isInternalErrorUnencrypted, isInternalErrorUncalibrated, isInternalErrorPartlyCalibration, isInternalErrorRCAMDisconnect, isEncryptionError, isUsbConnected, isCameraAbnormalReboot}, new boolean[]{this.lastIsFirmwareUpgradeError, this.lastIsSensorError, this.lastIsOverheat, this.lastIsInternalErrorUnencrypted, this.lastIsInternalErrorUncalibrated, this.lastIsInternalErrorPartlyCalibrated, this.lastIsInternalErrorRCAMDisconnect, this.lastIsEncryptionError, this.lastIsUsbConnected, this.lastIsCameraAbnormalReboot})) {
            resetCameraState();
            this.hasFirmwareUpgradeException = isFirmwareUpgradeError;
            this.hasSensorException = isSensorError;
            this.hasOverheatException = isOverheat;
            this.hasInternalExceptionUnencrypted = isInternalErrorUnencrypted;
            this.hasInternalExceptionUncalibrated = isInternalErrorUncalibrated;
            this.hasInternalExceptionPartlyCalibrated = isInternalErrorPartlyCalibration;
            this.hasInternalExceptionRCAMDisconnect = isInternalErrorRCAMDisconnect;
            this.hasEncryptionError = isEncryptionError;
            this.hasUsbConnected = isUsbConnected;
            this.hasCameraAbnormalReboot = isCameraAbnormalReboot;
            changed = true;
        }
        this.lastIsFirmwareUpgradeError = isFirmwareUpgradeError;
        this.lastIsSensorError = isSensorError;
        this.lastIsOverheat = isOverheat;
        this.lastIsInternalErrorUnencrypted = isInternalErrorUnencrypted;
        this.lastIsInternalErrorUncalibrated = isInternalErrorUncalibrated;
        this.lastIsInternalErrorPartlyCalibrated = isInternalErrorPartlyCalibration;
        this.lastIsInternalErrorRCAMDisconnect = isInternalErrorRCAMDisconnect;
        this.lastIsEncryptionError = isEncryptionError;
        this.lastIsUsbConnected = isUsbConnected;
        this.lastIsCameraAbnormalReboot = isCameraAbnormalReboot;
        return changed;
    }

    private boolean checkSdCardState(DataCameraGetStateInfo.SDCardState storageState) {
        boolean changed = false;
        boolean isNoSdCard = storageState == DataCameraGetStateInfo.SDCardState.None;
        boolean isSdCardInvalid = storageState == DataCameraGetStateInfo.SDCardState.Invalid;
        boolean isSdCardWriteProtect = storageState == DataCameraGetStateInfo.SDCardState.WriteProtection;
        boolean isSdCardUnformatted = storageState == DataCameraGetStateInfo.SDCardState.Unformat;
        boolean isSdCardIsFormatting = storageState == DataCameraGetStateInfo.SDCardState.Formating;
        boolean isSdCardFileSystemFault = storageState == DataCameraGetStateInfo.SDCardState.Illegal;
        boolean isSdCardIsBusy = storageState == DataCameraGetStateInfo.SDCardState.Busy;
        boolean isSdCardSpaceFull = storageState == DataCameraGetStateInfo.SDCardState.Full;
        boolean isSdCardSpecSlowSpeed = storageState == DataCameraGetStateInfo.SDCardState.Slow;
        boolean isSdCardUnknownError = storageState == DataCameraGetStateInfo.SDCardState.Unknow;
        boolean isSdCardIndexRunOut = storageState == DataCameraGetStateInfo.SDCardState.IndexMax;
        boolean isSdCardIsInitializing = storageState == DataCameraGetStateInfo.SDCardState.Initialzing;
        boolean isSdCardRecommendFormatting = storageState == DataCameraGetStateInfo.SDCardState.ToFormat;
        boolean isSdCardFilePreparing = storageState == DataCameraGetStateInfo.SDCardState.TryToRecoverFile;
        boolean isSdCardWriteSlow = storageState == DataCameraGetStateInfo.SDCardState.BecomeSlow;
        if (!compareBooleans(new boolean[]{isNoSdCard, isSdCardInvalid, isSdCardWriteProtect, isSdCardUnformatted, isSdCardIsFormatting, isSdCardFileSystemFault, isSdCardIsBusy, isSdCardSpaceFull, isSdCardSpecSlowSpeed, isSdCardUnknownError, isSdCardIndexRunOut, isSdCardIsInitializing, isSdCardRecommendFormatting, isSdCardFilePreparing, isSdCardWriteSlow}, new boolean[]{this.lastIsNoSdCard, this.lastIsSdCardInvalid, this.lastIsSdCardWriteProtect, this.lastIsSdCardUnformmatted, this.lastIsSdCardIsFormatting, this.lastIsSdCardFileSystemFault, this.lastIsSdCardIsBusy, this.lastIsSdCardSpaceFull, this.lastIsSdCardSpecSlowSpeed, this.lastIsSdCardUnknownError, this.lastIsSdCardIndexRunOut, this.lastIsSdCardIsInitializing, this.lastIsSdCardRecommendFormatting, this.lastIsSdCardFilePreparing, this.lastIsSdCardWriteSlow})) {
            resetSdCardState();
            this.hasNoSdCard = isNoSdCard;
            this.hasSdCardInvalid = isSdCardInvalid;
            this.hasSdCardWriteProtect = isSdCardWriteProtect;
            this.hasSdCardUnformmatted = isSdCardUnformatted;
            this.hasSdCardIsFormatting = isSdCardIsFormatting;
            this.hasSdCardFileSystemFault = isSdCardFileSystemFault;
            this.hasSdCardIsBusy = isSdCardIsBusy;
            this.hasSdCardSpaceFull = isSdCardSpaceFull;
            this.hasSdCardSpecSlowSpeed = isSdCardSpecSlowSpeed;
            this.hasSdCardUnknownError = isSdCardUnknownError;
            this.hasSdCardIndexRunOut = isSdCardIndexRunOut;
            this.hasSdCardIsInitializing = isSdCardIsInitializing;
            this.hasSdCardRecommendFormatting = isSdCardRecommendFormatting;
            this.hasSdCardFilePreparing = isSdCardFilePreparing;
            this.hasSdCardWriteSlow = isSdCardWriteSlow;
            changed = true;
        }
        this.lastIsNoSdCard = isNoSdCard;
        this.lastIsSdCardInvalid = isSdCardInvalid;
        this.lastIsSdCardWriteProtect = isSdCardWriteProtect;
        this.lastIsSdCardUnformmatted = isSdCardUnformatted;
        this.lastIsSdCardIsFormatting = isSdCardIsFormatting;
        this.lastIsSdCardFileSystemFault = isSdCardFileSystemFault;
        this.lastIsSdCardIsBusy = isSdCardIsBusy;
        this.lastIsSdCardSpaceFull = isSdCardSpaceFull;
        this.lastIsSdCardSpecSlowSpeed = isSdCardSpecSlowSpeed;
        this.lastIsSdCardUnknownError = isSdCardUnknownError;
        this.lastIsSdCardIndexRunOut = isSdCardIndexRunOut;
        this.lastIsSdCardIsInitializing = isSdCardIsInitializing;
        this.lastIsSdCardRecommendFormatting = isSdCardRecommendFormatting;
        this.lastIsSdCardFilePreparing = isSdCardFilePreparing;
        this.lastIsSdCardWriteSlow = isSdCardWriteSlow;
        return changed;
    }

    private boolean checkInternalStorageState(DataCameraGetStateInfo.SDCardState storageState) {
        boolean changed = false;
        boolean isNoInternalStorage = storageState == DataCameraGetStateInfo.SDCardState.None;
        boolean isInternalStorageInvalid = storageState == DataCameraGetStateInfo.SDCardState.Invalid;
        boolean isInternalStorageWriteProtect = storageState == DataCameraGetStateInfo.SDCardState.WriteProtection;
        boolean isInternalStorageUnformatted = storageState == DataCameraGetStateInfo.SDCardState.Unformat;
        boolean isInternalStorageIsFormatting = storageState == DataCameraGetStateInfo.SDCardState.Formating;
        boolean isInternalStorageFileSystemFault = storageState == DataCameraGetStateInfo.SDCardState.Illegal;
        boolean isInternalStorageIsBusy = storageState == DataCameraGetStateInfo.SDCardState.Busy;
        boolean isInternalStorageSpaceFull = storageState == DataCameraGetStateInfo.SDCardState.Full;
        if (storageState == DataCameraGetStateInfo.SDCardState.Slow) {
        }
        boolean isInternalStorageUnknownError = storageState == DataCameraGetStateInfo.SDCardState.Unknow;
        boolean isInternalStorageIndexRunOut = storageState == DataCameraGetStateInfo.SDCardState.IndexMax;
        boolean isInternalStorageIsInitializing = storageState == DataCameraGetStateInfo.SDCardState.Initialzing;
        boolean isInternalStorageRecommendFormatting = storageState == DataCameraGetStateInfo.SDCardState.ToFormat;
        boolean isInternalStorageFilePreparing = storageState == DataCameraGetStateInfo.SDCardState.TryToRecoverFile;
        if (storageState == DataCameraGetStateInfo.SDCardState.BecomeSlow) {
        }
        if (!compareBooleans(new boolean[]{isNoInternalStorage, isInternalStorageInvalid, isInternalStorageWriteProtect, isInternalStorageUnformatted, isInternalStorageIsFormatting, isInternalStorageFileSystemFault, isInternalStorageIsBusy, isInternalStorageSpaceFull, isInternalStorageUnknownError, isInternalStorageIndexRunOut, isInternalStorageIsInitializing, isInternalStorageRecommendFormatting, isInternalStorageFilePreparing}, new boolean[]{this.lastIsNoInternalStorage, this.lastIsInternalStorageInvalid, this.lastIsInternalStorageWriteProtect, this.lastIsInternalStorageUnformmatted, this.lastIsInternalStorageIsFormatting, this.lastIsInternalStorageFileSystemFault, this.lastIsInternalStorageIsBusy, this.lastIsInternalStorageSpaceFull, this.lastIsInternalStorageUnknownError, this.lastIsInternalStorageIndexRunOut, this.lastIsInternalStorageIsInitializing, this.lastIsInternalStorageRecommendFormatting, this.lastIsInternalStorageFilePreparing})) {
            resetInternalStorageState();
            this.hasNoInternalStorage = isNoInternalStorage;
            this.hasInternalStorageInvalid = isInternalStorageInvalid;
            this.hasInternalStorageWriteProtect = isInternalStorageWriteProtect;
            this.hasInternalStorageUnformmatted = isInternalStorageUnformatted;
            this.hasInternalStorageIsFormatting = isInternalStorageIsFormatting;
            this.hasInternalStorageFileSystemFault = isInternalStorageFileSystemFault;
            this.hasInternalStorageIsBusy = isInternalStorageIsBusy;
            this.hasInternalStorageSpaceFull = isInternalStorageSpaceFull;
            this.hasInternalStorageUnknownError = isInternalStorageUnknownError;
            this.hasInternalStorageIndexRunOut = isInternalStorageIndexRunOut;
            this.hasInternalStorageIsInitializing = isInternalStorageIsInitializing;
            this.hasInternalStorageRecommendFormatting = isInternalStorageRecommendFormatting;
            this.hasInternalStorageFilePreparing = isInternalStorageFilePreparing;
            changed = true;
        }
        this.lastIsNoInternalStorage = isNoInternalStorage;
        this.lastIsInternalStorageInvalid = isInternalStorageInvalid;
        this.lastIsInternalStorageWriteProtect = isInternalStorageWriteProtect;
        this.lastIsInternalStorageUnformmatted = isInternalStorageUnformatted;
        this.lastIsInternalStorageIsFormatting = isInternalStorageIsFormatting;
        this.lastIsInternalStorageFileSystemFault = isInternalStorageFileSystemFault;
        this.lastIsInternalStorageIsBusy = isInternalStorageIsBusy;
        this.lastIsInternalStorageSpaceFull = isInternalStorageSpaceFull;
        this.lastIsInternalStorageUnknownError = isInternalStorageUnknownError;
        this.lastIsInternalStorageIndexRunOut = isInternalStorageIndexRunOut;
        this.lastIsInternalStorageIsInitializing = isInternalStorageIsInitializing;
        this.lastIsInternalStorageRecommendFormatting = isInternalStorageRecommendFormatting;
        this.lastIsInternalStorageFilePreparing = isInternalStorageFilePreparing;
        return changed;
    }

    private void resetCameraState() {
        this.hasInternalExceptionRCAMDisconnect = false;
        this.hasInternalExceptionPartlyCalibrated = false;
        this.hasInternalExceptionUncalibrated = false;
        this.hasInternalExceptionUnencrypted = false;
        this.hasOverheatException = false;
        this.hasSensorException = false;
        this.hasFirmwareUpgradeException = false;
        this.hasEncryptionError = false;
        this.hasUsbConnected = false;
    }

    private void resetSdCardState() {
        this.hasNoSdCard = false;
        this.hasSdCardInvalid = false;
        this.hasSdCardWriteProtect = false;
        this.hasSdCardUnformmatted = false;
        this.hasSdCardIsFormatting = false;
        this.hasSdCardFileSystemFault = false;
        this.hasSdCardIsBusy = false;
        this.hasSdCardSpaceFull = false;
        this.hasSdCardSpecSlowSpeed = false;
        this.hasSdCardUnknownError = false;
        this.hasSdCardIndexRunOut = false;
        this.hasSdCardIsInitializing = false;
        this.hasSdCardRecommendFormatting = false;
        this.hasSdCardFilePreparing = false;
        this.hasSdCardWriteSlow = false;
    }

    private void resetInternalStorageState() {
        this.hasNoInternalStorage = false;
        this.hasInternalStorageInvalid = false;
        this.hasInternalStorageWriteProtect = false;
        this.hasInternalStorageUnformmatted = false;
        this.hasInternalStorageIsFormatting = false;
        this.hasInternalStorageFileSystemFault = false;
        this.hasInternalStorageIsBusy = false;
        this.hasInternalStorageSpaceFull = false;
        this.hasInternalStorageUnknownError = false;
        this.hasInternalStorageIndexRunOut = false;
        this.hasInternalStorageIsInitializing = false;
        this.hasInternalStorageRecommendFormatting = false;
        this.hasInternalStorageFilePreparing = false;
    }

    private boolean isSupportMultiStorage() {
        DJIComponentManager.CameraComponentType type = DJIComponentManager.getInstance().getCameraComponentType(this.cameraIndex);
        return type == DJIComponentManager.CameraComponentType.WM230 || type == DJIComponentManager.CameraComponentType.WM240 || type == DJIComponentManager.CameraComponentType.WM240Hasselblad || type == DJIComponentManager.CameraComponentType.WM245 || type == DJIComponentManager.CameraComponentType.WM245DualLightCamera;
    }

    public void onValueChange(DJISDKCacheKey key, DJISDKCacheParamValue oldValue, DJISDKCacheParamValue newValue) {
        if (key.getParamKey().equals(CameraKeys.CAMERA_TEMPERATURE_STATE) || key.getParamKey().equals(FlightControllerKeys.ARE_MOTOR_ON)) {
            handleCameraOverHeat();
        }
    }

    private void handleCameraOverHeat() {
        boolean isOverHeatLevel1;
        boolean isOverHeatStopRecord;
        boolean isMotorUp = CacheHelper.toBool(CacheHelper.getFlightController(FlightControllerKeys.ARE_MOTOR_ON), false);
        DataCameraGetPushStateInfo.CameraTemperatureState tempState = (DataCameraGetPushStateInfo.CameraTemperatureState) CacheHelper.getCamera(CameraKeys.CAMERA_TEMPERATURE_STATE);
        if (tempState != null) {
            if (tempState == DataCameraGetPushStateInfo.CameraTemperatureState.OVER_HEAT_LEVEL_1) {
                isOverHeatLevel1 = true;
            } else {
                isOverHeatLevel1 = false;
            }
            if (tempState == DataCameraGetPushStateInfo.CameraTemperatureState.OVER_HEAT_LEVEL_2 || tempState == DataCameraGetPushStateInfo.CameraTemperatureState.OVER_HEAT_LEVEL_3) {
                isOverHeatStopRecord = true;
            } else {
                isOverHeatStopRecord = false;
            }
            boolean change = false;
            if (!compareBooleans(new boolean[]{isOverHeatLevel1, isMotorUp, isOverHeatStopRecord}, new boolean[]{this.lastOverHeatLevel1, this.lastInAirUseByOverHeat, this.lastOverHeatStopRecord})) {
                this.hasOverHeatLevel1 = isOverHeatLevel1;
                this.hasInAirUseByOverHeat = isMotorUp;
                this.hasOverHeatStopRecord = isOverHeatStopRecord;
                change = true;
            }
            this.lastOverHeatLevel1 = isOverHeatLevel1;
            this.lastInAirUseByOverHeat = isMotorUp;
            this.lastOverHeatStopRecord = isOverHeatStopRecord;
            if (change) {
                notifyChange();
            }
        }
    }
}
