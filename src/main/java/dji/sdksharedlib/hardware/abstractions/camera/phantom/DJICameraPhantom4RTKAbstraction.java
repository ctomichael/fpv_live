package dji.sdksharedlib.hardware.abstractions.camera.phantom;

import dji.common.camera.SettingsDefinitions;
import dji.common.error.DJICameraError;
import dji.common.error.DJIError;
import dji.common.util.CallbackUtils;
import dji.midware.data.config.P3.Ccode;
import dji.midware.data.model.P3.DataCameraGetCalibrationControl;
import dji.midware.data.model.P3.DataCameraGetPushShotParams;
import dji.midware.data.model.P3.DataCameraParamsOption;
import dji.midware.data.model.P3.DataCameraSetCalibrationControl;
import dji.midware.interfaces.DJIDataCallBack;
import dji.sdksharedlib.hardware.abstractions.DJISDKCacheHWAbstraction;
import dji.sdksharedlib.hardware.abstractions.Getter;
import dji.sdksharedlib.hardware.abstractions.Setter;
import dji.sdksharedlib.hardware.abstractions.camera.DJICameraAbstraction;
import dji.sdksharedlib.keycatalog.CameraKeys;

public class DJICameraPhantom4RTKAbstraction extends DJICameraPhantom4PAbstraction {
    private static final String RTK_CUSTOMIZED_NETWORK_SETTINGS = "rtk_custom_network_settings";

    /* access modifiers changed from: protected */
    public String getDisplayName() {
        return DJICameraAbstraction.DisplaynamePhantom4RTKCamera;
    }

    /* access modifiers changed from: protected */
    public void initializeComponentCharacteristics() {
        addCharacteristics(CameraKeys.class, getClass());
    }

    /* access modifiers changed from: protected */
    public boolean isVideoPlaybackSupported() {
        return false;
    }

    /* access modifiers changed from: protected */
    public boolean isVideoDewarpingSupported() {
        return true;
    }

    @Setter(CameraKeys.DEWARPING_ENABLED)
    public void setDewarpingEnabled(Boolean data, DJISDKCacheHWAbstraction.InnerCallback callback) {
        if (!isVideoDewarpingSupported()) {
            CallbackUtils.onFailure(callback, DJIError.COMMAND_NOT_SUPPORTED_BY_FIRMWARE);
            return;
        }
        DataCameraSetCalibrationControl setter = new DataCameraSetCalibrationControl();
        setter.setReceiverId(getReceiverIdByIndex());
        setter.setDeWarpCalibration(data.booleanValue());
        setter.start(CallbackUtils.defaultCB(callback));
    }

    @Getter(CameraKeys.DEWARPING_ENABLED)
    public void getDewarpingEnabled(final DJISDKCacheHWAbstraction.InnerCallback callback) {
        if (!isVideoDewarpingSupported()) {
            CallbackUtils.onFailure(callback, DJIError.COMMAND_NOT_SUPPORTED_BY_FIRMWARE);
            return;
        }
        final DataCameraGetCalibrationControl getter = new DataCameraGetCalibrationControl();
        getter.setReceiverId(getReceiverIdByIndex());
        getter.start(new DJIDataCallBack() {
            /* class dji.sdksharedlib.hardware.abstractions.camera.phantom.DJICameraPhantom4RTKAbstraction.AnonymousClass1 */

            public void onSuccess(Object model) {
                CallbackUtils.onSuccess(callback, Boolean.valueOf(getter.isDeWarpCalibrationEnabled()));
            }

            public void onFailure(Ccode ccode) {
                CallbackUtils.onFailure(callback, DJICameraError.getDJIError(ccode));
            }
        });
    }

    @Setter(CameraKeys.HIGH_QUALITY_PREVIEW_ENABLED)
    public void setHighQualityPreviewEnabled(boolean enabled, DJISDKCacheHWAbstraction.InnerCallback callback) {
        DataCameraParamsOption option = new DataCameraParamsOption();
        option.isSetter(true).setCmdOption(DataCameraParamsOption.ParamsCMD.SCREEN_NAIL_HD_SWITCH);
        if (enabled) {
            option.setPhotoSize(DataCameraParamsOption.PreviewPhotoSize.X_LARGE);
        } else {
            option.setPhotoSize(DataCameraParamsOption.PreviewPhotoSize.DEFAULT);
        }
        option.start(CallbackUtils.defaultCB(callback, DJICameraError.class));
    }

    @Getter(CameraKeys.HIGH_QUALITY_PREVIEW_ENABLED)
    public void getHighQualityPreviewEnabled(final DJISDKCacheHWAbstraction.InnerCallback callback) {
        final DataCameraParamsOption option = new DataCameraParamsOption();
        option.isGetter(true).setCmdOption(DataCameraParamsOption.ParamsCMD.SCREEN_NAIL_HD_SWITCH).start(new DJIDataCallBack() {
            /* class dji.sdksharedlib.hardware.abstractions.camera.phantom.DJICameraPhantom4RTKAbstraction.AnonymousClass2 */

            public void onSuccess(Object model) {
                CallbackUtils.onSuccess(callback, Boolean.valueOf(option.isScreenNailEnabled()));
            }

            public void onFailure(Ccode ccode) {
                CallbackUtils.onFailure(callback, DJICameraError.getDJIError(ccode));
            }
        });
    }

    /* access modifiers changed from: protected */
    public boolean isPhotoIntervalParamValid(int interval, int count) {
        if (SettingsDefinitions.PhotoFileFormat.values()[DataCameraGetPushShotParams.getInstance().getImageFormat(getExpectedSenderIdByIndex())] != SettingsDefinitions.PhotoFileFormat.JPEG || interval >= 3) {
            return true;
        }
        return false;
    }
}
