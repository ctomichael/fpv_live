package dji.sdksharedlib.hardware.abstractions.camera.zenmuse;

import dji.common.camera.ResolutionAndFrameRate;
import dji.common.camera.SettingsDefinitions;
import dji.common.error.DJICameraError;
import dji.common.error.DJIError;
import dji.common.util.CallbackUtils;
import dji.common.util.DJICameraEnumMappingUtil;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.model.P3.DataCameraGetPushShotInfo;
import dji.midware.data.model.P3.DataCameraSetDustReduction;
import dji.midware.data.model.P3.DataCameraSetNDFilter;
import dji.midware.data.model.P3.DataCameraSetRawVideoFormat;
import dji.sdksharedlib.extension.CacheHelper;
import dji.sdksharedlib.hardware.abstractions.Action;
import dji.sdksharedlib.hardware.abstractions.DJISDKCacheHWAbstraction;
import dji.sdksharedlib.hardware.abstractions.Setter;
import dji.sdksharedlib.hardware.abstractions.camera.DJICameraAbstraction;
import dji.sdksharedlib.keycatalog.CameraKeys;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

@EXClassNullAway
public class DJICameraX7Abstraction extends DJICameraInspire2RawAbstraction {
    private static final String TAG = "DJICameraX7Abstraction";

    /* access modifiers changed from: protected */
    public String getDisplayName() {
        return DJICameraAbstraction.DisplayNameX7;
    }

    /* access modifiers changed from: protected */
    public boolean isSensorCleaningSupported() {
        return true;
    }

    public void setVideoResolutionAndFrameRate(ResolutionAndFrameRate resolutionAndFrameRate, DJISDKCacheHWAbstraction.InnerCallback callback) {
        if (isResolutionAndFrameRateAlreadySet(resolutionAndFrameRate)) {
            CallbackUtils.onSuccess(callback, (Object) null);
        } else if (!isResolutionAndFrameRateValid(resolutionAndFrameRate)) {
            CallbackUtils.onFailure(callback, DJIError.COMMON_PARAM_ILLEGAL);
        } else {
            SettingsDefinitions.VideoResolution resolution = resolutionAndFrameRate.getResolution();
            SettingsDefinitions.VideoFrameRate frameRate = resolutionAndFrameRate.getFrameRate();
            SettingsDefinitions.VideoFov videoFovType = resolutionAndFrameRate.getFov();
            if (!checkIfSupported(resolution, frameRate)) {
                CallbackUtils.onFailure(callback, DJICameraError.COMMON_UNSUPPORTED);
                return;
            }
            int fps = DJICameraEnumMappingUtil.getFrameRateProtocolValue(frameRate);
            int ratio = DJICameraEnumMappingUtil.getResolutionProtocolValue(resolution);
            int fov = videoFovType == null ? 0 : videoFovType.value();
            DataCameraSetRawVideoFormat videoFormat = new DataCameraSetRawVideoFormat();
            videoFormat.setVideoFrameRate(fps);
            videoFormat.setVideoResolution(ratio);
            videoFormat.setFOV(fov);
            videoFormat.start(CallbackUtils.defaultCB(callback));
        }
    }

    @Action(CameraKeys.INIT_SENSOR_CLEANING_MODE)
    public void enterDedustingMode(DJISDKCacheHWAbstraction.InnerCallback callback) {
        DataCameraSetDustReduction setter = new DataCameraSetDustReduction();
        setter.setState(DataCameraSetDustReduction.State.ENTER);
        setter.setReceiverId(getReceiverIdByIndex());
        setter.start(CallbackUtils.defaultCB(callback));
    }

    @Action(CameraKeys.EXIT_SENSOR_CLEANING_MODE)
    public void exitDedustingMode(DJISDKCacheHWAbstraction.InnerCallback callback) {
        DataCameraSetDustReduction setter = new DataCameraSetDustReduction();
        setter.setState(DataCameraSetDustReduction.State.EXIT);
        setter.setReceiverId(getReceiverIdByIndex());
        setter.start(CallbackUtils.defaultCB(callback));
    }

    @Action(CameraKeys.START_SENSOR_CLEANING)
    public void performAction(DJISDKCacheHWAbstraction.InnerCallback callback) {
        if (this.currentDedustingState != SettingsDefinitions.SensorCleaningState.READY) {
            CallbackUtils.onFailure(callback, DJICameraError.CANNOT_SET_PARAMETERS_IN_THIS_STATE);
            return;
        }
        DataCameraSetDustReduction setter = new DataCameraSetDustReduction();
        setter.setState(DataCameraSetDustReduction.State.START);
        setter.setReceiverId(getReceiverIdByIndex());
        setter.start(CallbackUtils.defaultCB(callback));
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEvent3BackgroundThread(DataCameraGetPushShotInfo pushInfo) {
        super.onEvent3BackgroundThread(pushInfo);
        if (pushInfo.isGetted() && isValidSender(pushInfo.getSenderId())) {
            this.currentDedustingState = SettingsDefinitions.SensorCleaningState.find(pushInfo.getDustReductionStage().value());
            notifyValueChangeForKeyPath(this.currentDedustingState, CameraKeys.SENSOR_CLEANING_STATE);
        }
    }

    @Setter(CameraKeys.NDFILTER_MODE)
    public void setNDFilterMode(SettingsDefinitions.NDFilterMode mode, DJISDKCacheHWAbstraction.InnerCallback callback) {
        if (!isNDFilterModeSupported()) {
            CallbackUtils.onFailure(callback, DJIError.COMMAND_NOT_SUPPORTED_BY_FIRMWARE);
            return;
        }
        DataCameraSetNDFilter setter = new DataCameraSetNDFilter();
        setter.setReceiverId(getReceiverIdByIndex());
        setter.setNDFilter(DataCameraSetNDFilter.NDFilterMode.find(mode.value()));
        setter.start(CallbackUtils.defaultCB(callback));
    }

    /* access modifiers changed from: protected */
    public boolean isMechanicalShutterSupported() {
        return ((Boolean) CacheHelper.getValue(convertKeyToPath(CameraKeys.IS_MECHANICAL_SHUTTER_SUPPORTED), false)).booleanValue();
    }

    /* access modifiers changed from: protected */
    public boolean isNDFilterModeSupported() {
        return ((Boolean) CacheHelper.getValue(convertKeyToPath(CameraKeys.IS_ND_FILTER_MODE_SUPPORTED), false)).booleanValue();
    }
}
