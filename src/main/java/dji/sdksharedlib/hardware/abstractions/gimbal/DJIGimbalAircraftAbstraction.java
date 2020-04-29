package dji.sdksharedlib.hardware.abstractions.gimbal;

import dji.common.error.DJIGimbalError;
import dji.common.util.CallbackUtils;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.config.P3.Ccode;
import dji.midware.data.config.P3.ProductType;
import dji.midware.data.manager.P3.DJIGimbalParamInfoManager;
import dji.midware.data.manager.P3.DJIProductManager;
import dji.midware.data.model.P3.DataCameraGetPushStateInfo;
import dji.midware.data.model.P3.DataGimbalCommonProtocol;
import dji.midware.data.model.P3.DataGimbalGetPushAutoCalibrationStatus;
import dji.midware.data.model.P3.DataGimbalGetPushParams;
import dji.midware.interfaces.DJIDataCallBack;
import dji.sdksharedlib.extension.CacheHelper;
import dji.sdksharedlib.hardware.abstractions.DJISDKCacheHWAbstraction;
import dji.sdksharedlib.hardware.abstractions.Getter;
import dji.sdksharedlib.hardware.abstractions.Setter;
import dji.sdksharedlib.hardware.abstractions.gimbal.DJIGimbalAbstraction;
import dji.sdksharedlib.keycatalog.DJISDKCacheKeys;
import dji.sdksharedlib.keycatalog.GimbalKeys;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

@EXClassNullAway
public class DJIGimbalAircraftAbstraction extends DJIGimbalAbstraction {
    private static final String MIN_GIMBAL_YAW_FOLLOW_VERSION = "01.28.00.00";

    @Setter(GimbalKeys.PITCH_SMOOTH_TRACK_SPEED)
    public void setSmoothTrackSpeedOnPitch(int speed, DJISDKCacheHWAbstraction.InnerCallback callback) {
        if (!isCustomAdvancedSettingProfile() && callback != null) {
            callback.onFails(DJIGimbalError.CANNOT_SET_PARAMETERS_IN_THIS_STATE);
        }
        setParam(speed, DJIGimbalAbstraction.SettingParamCmd.SMOOTH_TRACK_PITCH_SPEED, callback);
    }

    @Setter(GimbalKeys.YAW_SMOOTH_TRACK_SPEED)
    public void setSmoothTrackSpeedOnYaw(int speed, DJISDKCacheHWAbstraction.InnerCallback callback) {
        if (!isCustomAdvancedSettingProfile() && callback != null) {
            callback.onFails(DJIGimbalError.CANNOT_SET_PARAMETERS_IN_THIS_STATE);
        }
        setParam(speed, DJIGimbalAbstraction.SettingParamCmd.SMOOTH_TRACK_YAW_SPEED, callback);
    }

    @Getter(GimbalKeys.PITCH_SMOOTH_TRACK_SPEED)
    public void getSmoothTrackSpeedOnPitch(DJISDKCacheHWAbstraction.InnerCallback callback) {
        if (!isCustomAdvancedSettingProfile() && callback != null) {
            callback.onFails(DJIGimbalError.CANNOT_SET_PARAMETERS_IN_THIS_STATE);
        }
        getParam(DJIGimbalAbstraction.SettingParamCmd.SMOOTH_TRACK_PITCH_SPEED, callback);
    }

    @Getter(GimbalKeys.YAW_SMOOTH_TRACK_SPEED)
    public void getSmoothTrackSpeedOnYaw(DJISDKCacheHWAbstraction.InnerCallback callback) {
        if (!isCustomAdvancedSettingProfile() && callback != null) {
            callback.onFails(DJIGimbalError.CANNOT_SET_PARAMETERS_IN_THIS_STATE);
        }
        getParam(DJIGimbalAbstraction.SettingParamCmd.SMOOTH_TRACK_YAW_SPEED, callback);
    }

    @Setter(GimbalKeys.PITCH_SMOOTH_TRACK_DEADBAND)
    public void setSmoothTrackDeadbandOnPitch(int deadband, DJISDKCacheHWAbstraction.InnerCallback callback) {
        if (!isCustomAdvancedSettingProfile() && callback != null) {
            callback.onFails(DJIGimbalError.CANNOT_SET_PARAMETERS_IN_THIS_STATE);
        }
        setParam(deadband, DJIGimbalAbstraction.SettingParamCmd.SMOOTH_TRACK_PITCH_DEADBAND, callback);
    }

    @Setter(GimbalKeys.YAW_SMOOTH_TRACK_DEADBAND)
    public void setSmoothTrackDeadbandOnYaw(int deadband, DJISDKCacheHWAbstraction.InnerCallback callback) {
        if (!isCustomAdvancedSettingProfile() && callback != null) {
            callback.onFails(DJIGimbalError.CANNOT_SET_PARAMETERS_IN_THIS_STATE);
        }
        setParam(deadband, DJIGimbalAbstraction.SettingParamCmd.SMOOTH_TRACK_YAW_DEADBAND, callback);
    }

    @Getter(GimbalKeys.PITCH_SMOOTH_TRACK_DEADBAND)
    public void getSmoothTrackDeadbandOnPitch(DJISDKCacheHWAbstraction.InnerCallback callback) {
        if (!isCustomAdvancedSettingProfile() && callback != null) {
            callback.onFails(DJIGimbalError.CANNOT_SET_PARAMETERS_IN_THIS_STATE);
        }
        getParam(DJIGimbalAbstraction.SettingParamCmd.SMOOTH_TRACK_PITCH_DEADBAND, callback);
    }

    @Getter(GimbalKeys.YAW_SMOOTH_TRACK_DEADBAND)
    public void getSmoothTrackDeadbandOnYaw(DJISDKCacheHWAbstraction.InnerCallback callback) {
        if (!isCustomAdvancedSettingProfile() && callback != null) {
            callback.onFails(DJIGimbalError.CANNOT_SET_PARAMETERS_IN_THIS_STATE);
        }
        getParam(DJIGimbalAbstraction.SettingParamCmd.SMOOTH_TRACK_YAW_DEADBAND, callback);
    }

    @Setter(GimbalKeys.PITCH_SMOOTH_TRACK_ACCELERATION)
    public void setSmoothTrackAccelerationOnPitch(int acceleration, DJISDKCacheHWAbstraction.InnerCallback callback) {
        if (!isCustomAdvancedSettingProfile() && callback != null) {
            callback.onFails(DJIGimbalError.CANNOT_SET_PARAMETERS_IN_THIS_STATE);
        }
        setParam(acceleration, DJIGimbalAbstraction.SettingParamCmd.SMOOTH_TRACK_PITCH_ACCEL, callback);
    }

    @Setter(GimbalKeys.YAW_SMOOTH_TRACK_ACCELERATION)
    public void setSmoothTrackAccelerationOnYaw(int acceleration, DJISDKCacheHWAbstraction.InnerCallback callback) {
        if (!isCustomAdvancedSettingProfile() && callback != null) {
            callback.onFails(DJIGimbalError.CANNOT_SET_PARAMETERS_IN_THIS_STATE);
        }
        setParam(acceleration, DJIGimbalAbstraction.SettingParamCmd.SMOOTH_TRACK_YAW_ACCEL, callback);
    }

    @Getter(GimbalKeys.PITCH_SMOOTH_TRACK_ACCELERATION)
    public void getSmoothTrackAccelerationOnPitch(DJISDKCacheHWAbstraction.InnerCallback callback) {
        if (!isCustomAdvancedSettingProfile() && callback != null) {
            callback.onFails(DJIGimbalError.CANNOT_SET_PARAMETERS_IN_THIS_STATE);
        }
        getParam(DJIGimbalAbstraction.SettingParamCmd.SMOOTH_TRACK_PITCH_ACCEL, callback);
    }

    @Getter(GimbalKeys.YAW_SMOOTH_TRACK_ACCELERATION)
    public void getSmoothTrackAccelerationOnYaw(DJISDKCacheHWAbstraction.InnerCallback callback) {
        if (!isCustomAdvancedSettingProfile() && callback != null) {
            callback.onFails(DJIGimbalError.CANNOT_SET_PARAMETERS_IN_THIS_STATE);
        }
        getParam(DJIGimbalAbstraction.SettingParamCmd.SMOOTH_TRACK_YAW_ACCEL, callback);
    }

    @Setter(GimbalKeys.PITCH_CONTROLLER_SMOOTHING_FACTOR)
    public void setControllerSmoothingOnPitch(int smoothing, DJISDKCacheHWAbstraction.InnerCallback callback) {
        if (!isCustomAdvancedSettingProfile() && callback != null) {
            callback.onFails(DJIGimbalError.CANNOT_SET_PARAMETERS_IN_THIS_STATE);
        }
        setParam(smoothing, DJIGimbalAbstraction.SettingParamCmd.CONTROLLER_PITCH_SMOOTH, callback);
    }

    @Setter(GimbalKeys.YAW_CONTROLLER_SMOOTHING_FACTOR)
    public void setControllerSmoothingOnYaw(int smoothing, DJISDKCacheHWAbstraction.InnerCallback callback) {
        if (!isCustomAdvancedSettingProfile() && callback != null) {
            callback.onFails(DJIGimbalError.CANNOT_SET_PARAMETERS_IN_THIS_STATE);
        }
        setParam(smoothing, DJIGimbalAbstraction.SettingParamCmd.CONTROLLER_YAW_SMOOTH, callback);
    }

    @Getter(GimbalKeys.PITCH_CONTROLLER_SMOOTHING_FACTOR)
    public void getControllerSmoothingOnPitch(DJISDKCacheHWAbstraction.InnerCallback callback) {
        if (!isCustomAdvancedSettingProfile() && callback != null) {
            callback.onFails(DJIGimbalError.CANNOT_SET_PARAMETERS_IN_THIS_STATE);
        }
        getParam(DJIGimbalAbstraction.SettingParamCmd.CONTROLLER_PITCH_SMOOTH, callback);
    }

    @Getter(GimbalKeys.YAW_CONTROLLER_SMOOTHING_FACTOR)
    public void getControllerSmoothingOnYaw(DJISDKCacheHWAbstraction.InnerCallback callback) {
        if (!isCustomAdvancedSettingProfile() && callback != null) {
            callback.onFails(DJIGimbalError.CANNOT_SET_PARAMETERS_IN_THIS_STATE);
        }
        getParam(DJIGimbalAbstraction.SettingParamCmd.CONTROLLER_YAW_SMOOTH, callback);
    }

    @Setter(GimbalKeys.PITCH_CONTROLLER_SPEED_COEFFICIENT)
    public void setControllerSpeedOnPitch(int speed, DJISDKCacheHWAbstraction.InnerCallback callback) {
        if (!isCustomAdvancedSettingProfile() && callback != null) {
            callback.onFails(DJIGimbalError.CANNOT_SET_PARAMETERS_IN_THIS_STATE);
        }
        setParam(speed, DJIGimbalAbstraction.SettingParamCmd.CONTROLLER_PITCH_SPEED, callback);
    }

    @Setter(GimbalKeys.YAW_CONTROLLER_SPEED_COEFFICIENT)
    public void setControllerSpeedOnYaw(int speed, DJISDKCacheHWAbstraction.InnerCallback callback) {
        if (!isCustomAdvancedSettingProfile() && callback != null) {
            callback.onFails(DJIGimbalError.CANNOT_SET_PARAMETERS_IN_THIS_STATE);
        }
        setParam(speed, DJIGimbalAbstraction.SettingParamCmd.CONTROLLER_YAW_SPEED, callback);
    }

    @Setter(GimbalKeys.PITCH_CONTROLLER_MAX_SPEED)
    public void setControllerMaxSpeedOnPitch(int maxSpeed, DJISDKCacheHWAbstraction.InnerCallback callback) {
        if (!isCustomAdvancedSettingProfile() && callback != null) {
            callback.onFails(DJIGimbalError.CANNOT_SET_PARAMETERS_IN_THIS_STATE);
        }
        setParam(maxSpeed, DJIGimbalAbstraction.SettingParamCmd.CONTROLLER_PITCH_MAX_SPEED, callback);
    }

    @Setter(GimbalKeys.YAW_CONTROLLER_MAX_SPEED)
    public void setControllerMaxSpeedOnYaw(int maxSpeed, DJISDKCacheHWAbstraction.InnerCallback callback) {
        if (!isCustomAdvancedSettingProfile() && callback != null) {
            callback.onFails(DJIGimbalError.CANNOT_SET_PARAMETERS_IN_THIS_STATE);
        }
        setParam(maxSpeed, DJIGimbalAbstraction.SettingParamCmd.CONTROLLER_YAW_MAX_SPEED, callback);
    }

    @Getter(GimbalKeys.PITCH_CONTROLLER_SPEED_COEFFICIENT)
    public void getControllerSpeedOnPitch(DJISDKCacheHWAbstraction.InnerCallback callback) {
        if (!isCustomAdvancedSettingProfile() && callback != null) {
            callback.onFails(DJIGimbalError.CANNOT_SET_PARAMETERS_IN_THIS_STATE);
        }
        getParam(DJIGimbalAbstraction.SettingParamCmd.CONTROLLER_PITCH_SPEED, callback);
    }

    @Getter(GimbalKeys.YAW_CONTROLLER_SPEED_COEFFICIENT)
    public void getControllerSpeedOnYaw(DJISDKCacheHWAbstraction.InnerCallback callback) {
        if (!isCustomAdvancedSettingProfile() && callback != null) {
            callback.onFails(DJIGimbalError.CANNOT_SET_PARAMETERS_IN_THIS_STATE);
        }
        getParam(DJIGimbalAbstraction.SettingParamCmd.CONTROLLER_YAW_SPEED, callback);
    }

    @Getter(GimbalKeys.PITCH_CONTROLLER_MAX_SPEED)
    public void getControllerMaxSpeedOnPitch(DJISDKCacheHWAbstraction.InnerCallback callback) {
        if (!isCustomAdvancedSettingProfile() && callback != null) {
            callback.onFails(DJIGimbalError.CANNOT_SET_PARAMETERS_IN_THIS_STATE);
        }
        getParam(DJIGimbalAbstraction.SettingParamCmd.CONTROLLER_PITCH_MAX_SPEED, callback);
    }

    @Getter(GimbalKeys.YAW_CONTROLLER_MAX_SPEED)
    public void getControllerMaxSpeedOnYaw(DJISDKCacheHWAbstraction.InnerCallback callback) {
        if (!isCustomAdvancedSettingProfile() && callback != null) {
            callback.onFails(DJIGimbalError.CANNOT_SET_PARAMETERS_IN_THIS_STATE);
        }
        getParam(DJIGimbalAbstraction.SettingParamCmd.CONTROLLER_YAW_MAX_SPEED, callback);
    }

    @Setter(GimbalKeys.IS_LIMITATION_ENABLED)
    public void setLimitationEnabled(boolean isEnabled, final DJISDKCacheHWAbstraction.InnerCallback callback) {
        int i = 0;
        DataGimbalCommonProtocol commandId = new DataGimbalCommonProtocol().setCommandType(false).setCommandId(0);
        if (isEnabled) {
            i = 1;
        }
        commandId.setCommandValue(i).start(new DJIDataCallBack() {
            /* class dji.sdksharedlib.hardware.abstractions.gimbal.DJIGimbalAircraftAbstraction.AnonymousClass1 */

            public void onSuccess(Object model) {
                CallbackUtils.onSuccess(callback, (Object) null);
            }

            public void onFailure(Ccode ccode) {
                CallbackUtils.onFailure(callback, ccode);
            }
        });
    }

    @Getter(GimbalKeys.IS_LIMITATION_ENABLED)
    public void isLimitationEnabled(final DJISDKCacheHWAbstraction.InnerCallback callback) {
        final DataGimbalCommonProtocol getter = new DataGimbalCommonProtocol();
        getter.setCommandType(true).setCommandId(0).start(new DJIDataCallBack() {
            /* class dji.sdksharedlib.hardware.abstractions.gimbal.DJIGimbalAircraftAbstraction.AnonymousClass2 */

            public void onSuccess(Object model) {
                CallbackUtils.onSuccess(callback, Boolean.valueOf(getter.isLimitationEnabled()));
            }

            public void onFailure(Ccode ccode) {
                CallbackUtils.onFailure(callback, ccode);
            }
        });
    }

    @Setter(GimbalKeys.YAW_SIMULTANEOUS_FOLLOW_ENABLED)
    public void setYawSynchronousFollowEnabled(boolean isEnabled, DJISDKCacheHWAbstraction.InnerCallback callback) {
        if (isYawSynchronousFollowEnabledSupported()) {
            setParam(isEnabled ? 1 : 0, DJIGimbalAbstraction.SettingParamCmd.YAW_SYNCHRONOUS_FOLLOW_ENABLED, callback);
        } else {
            CallbackUtils.onFailure(callback, DJIGimbalError.COMMON_UNSUPPORTED);
        }
    }

    @Getter(GimbalKeys.YAW_SIMULTANEOUS_FOLLOW_ENABLED)
    public void getYawSynchronousFollowEnabled(DJISDKCacheHWAbstraction.InnerCallback callback) {
        boolean z = true;
        if (isYawSynchronousFollowEnabledSupported()) {
            if (DJIGimbalParamInfoManager.read(DJIGimbalAbstraction.SettingParamCmd.YAW_SYNCHRONOUS_FOLLOW_ENABLED.getCmdString()).value.intValue() != 1) {
                z = false;
            }
            CallbackUtils.onSuccess(callback, Boolean.valueOf(z));
            return;
        }
        CallbackUtils.onFailure(callback, DJIGimbalError.COMMON_UNSUPPORTED);
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEvent3BackgroundThread(DataGimbalGetPushAutoCalibrationStatus gimbalPush) {
        boolean z;
        boolean z2 = true;
        if (gimbalPush.isGetted() && gimbalPush.getSenderId() == getExpectedSenderIdByIndex()) {
            this.calibrationProgress = gimbalPush.getProgress();
            if (gimbalPush.getStatus() == 1) {
                z = true;
            } else {
                z = false;
            }
            this.isCalibrating = z;
            if (gimbalPush.getStatus() != 0) {
                z2 = false;
            }
            this.isCalibrationSuccessful = z2;
            notifyValueChangeForKeyPath(Integer.valueOf(this.calibrationProgress), convertKeyToPath(GimbalKeys.CALIBRATION_PROGRESS));
            notifyValueChangeForKeyPath(Boolean.valueOf(this.isCalibrating), convertKeyToPath(GimbalKeys.IS_CALIBRATING));
            notifyValueChangeForKeyPath(Boolean.valueOf(this.isCalibrationSuccessful), convertKeyToPath(GimbalKeys.IS_CALIBRATION_SUCCESSFUL));
        }
    }

    public void syncPushDataFromMidware() {
        super.syncPushDataFromMidware();
        if (DataGimbalGetPushParams.getInstance().isGetted()) {
            DataGimbalGetPushParams.getInstance().swapValidData(getExpectedSenderIdByIndex());
            onEvent3BackgroundThread(DataGimbalGetPushParams.getInstance());
        }
    }

    private boolean isYawSynchronousFollowEnabledSupported() {
        DataCameraGetPushStateInfo.CameraType cameraType = DataCameraGetPushStateInfo.getInstance().getCameraType(getExpectedSenderIdByIndex());
        ProductType productType = DJIProductManager.getInstance().getType();
        if (cameraType == DataCameraGetPushStateInfo.CameraType.DJICameraTypeFC350 || cameraType == DataCameraGetPushStateInfo.CameraType.DJICameraTypeFC550 || cameraType == DataCameraGetPushStateInfo.CameraType.DJICameraTypeFC550Raw || cameraType == DataCameraGetPushStateInfo.CameraType.DJICameraTypeFC6520 || cameraType == DataCameraGetPushStateInfo.CameraType.DJICameraTypeFC6510 || productType == ProductType.BigBanana || productType == ProductType.Orange || productType == ProductType.OrangeRAW || productType == ProductType.Olives) {
            String firmwareVersion = (String) CacheHelper.getGimbal(DJISDKCacheKeys.FIRMWARE_VERSION);
            if (firmwareVersion == null) {
                return false;
            }
            if (firmwareVersion.compareTo(MIN_GIMBAL_YAW_FOLLOW_VERSION) < 0) {
                return false;
            }
            return true;
        } else if (productType == ProductType.WM230) {
            return false;
        } else {
            return true;
        }
    }
}
