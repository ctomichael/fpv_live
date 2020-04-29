package dji.sdksharedlib.hardware.abstractions.camera.FoldingDrone;

import dji.common.camera.SettingsDefinitions;
import dji.common.error.DJICameraError;
import dji.common.error.DJIError;
import dji.common.util.CallbackUtils;
import dji.midware.data.config.P3.Ccode;
import dji.midware.data.model.P3.DataCameraGetSensorID;
import dji.midware.interfaces.DJIDataCallBack;
import dji.sdksharedlib.extension.CacheHelper;
import dji.sdksharedlib.hardware.abstractions.DJISDKCacheHWAbstraction;
import dji.sdksharedlib.hardware.abstractions.Getter;
import dji.sdksharedlib.hardware.abstractions.camera.DJICameraAbstraction;
import dji.sdksharedlib.keycatalog.DJISDKCacheKeys;
import dji.sdksharedlib.store.DJISDKCacheStoreLayer;

public class DJICameraWM160Abstraction extends DJICameraFoldingDroneSAbstraction {
    /* access modifiers changed from: protected */
    public String getDisplayName() {
        return DJICameraAbstraction.DisplayNameWM160;
    }

    /* access modifiers changed from: protected */
    public boolean isInternalStorageSupported() {
        return false;
    }

    /* access modifiers changed from: protected */
    public boolean isPhotoQuickViewSupported() {
        return false;
    }

    /* access modifiers changed from: protected */
    public boolean isMediaDownloadModeSupported() {
        return true;
    }

    /* access modifiers changed from: protected */
    public boolean isVideoPlaybackSupported() {
        return true;
    }

    /* access modifiers changed from: protected */
    public boolean isDigitalZoomScaleSupported() {
        return false;
    }

    /* access modifiers changed from: protected */
    public boolean isMediaDownModeMapValue2() {
        return true;
    }

    /* access modifiers changed from: protected */
    public int mapCameraModeToProtocolValue(SettingsDefinitions.CameraMode mode) {
        if (SettingsDefinitions.CameraMode.MEDIA_DOWNLOAD == mode) {
            return 7;
        }
        if (SettingsDefinitions.CameraMode.BROADCAST == mode) {
            return -1;
        }
        return mode.value();
    }

    /* access modifiers changed from: protected */
    public boolean isShootPhotoModeSupported(SettingsDefinitions.ShootPhotoMode photoMode, DJISDKCacheHWAbstraction.InnerCallback callback) {
        if (photoMode == null) {
            CallbackUtils.onFailure(callback, DJICameraError.INVALID_PARAMETERS);
            return false;
        } else if (photoMode == SettingsDefinitions.ShootPhotoMode.SINGLE || photoMode == SettingsDefinitions.ShootPhotoMode.INTERVAL) {
            return true;
        } else {
            CallbackUtils.onFailure(callback, DJICameraError.INVALID_PARAMETERS);
            return false;
        }
    }

    public void init(String component, int index, DJISDKCacheStoreLayer storeLayer, DJISDKCacheHWAbstraction.OnValueChangeListener onValueChangeListener) {
        super.init(component, index, storeLayer, onValueChangeListener);
        CacheHelper.addCameraListener(this, 0, DJISDKCacheKeys.FIRMWARE_VERSION);
    }

    public void destroy() {
        super.destroy();
        CacheHelper.removeListener(this);
    }

    /* access modifiers changed from: protected */
    public boolean checkPhotoAspectRatioValid(SettingsDefinitions.PhotoAspectRatio photoAspectRatio) {
        return photoAspectRatio == SettingsDefinitions.PhotoAspectRatio.RATIO_4_3 || photoAspectRatio == SettingsDefinitions.PhotoAspectRatio.RATIO_16_9;
    }

    public void setPhotoRatio(SettingsDefinitions.PhotoAspectRatio photoAspectRatio, DJISDKCacheHWAbstraction.InnerCallback callback) {
        if (!checkPhotoAspectRatioValid(photoAspectRatio)) {
            CallbackUtils.onFailure(callback, DJICameraError.INVALID_PARAMETERS);
        } else {
            super.setPhotoRatio(photoAspectRatio, callback);
        }
    }

    /* access modifiers changed from: protected */
    public boolean checkExposureModeValid(SettingsDefinitions.ExposureMode exposureMode) {
        return exposureMode == SettingsDefinitions.ExposureMode.PROGRAM || exposureMode == SettingsDefinitions.ExposureMode.MANUAL;
    }

    public void setExposureMode(SettingsDefinitions.ExposureMode exposureMode, DJISDKCacheHWAbstraction.InnerCallback callback) {
        if (!checkExposureModeValid(exposureMode)) {
            CallbackUtils.onFailure(callback, DJICameraError.INVALID_PARAMETERS);
        } else {
            super.setExposureMode(exposureMode, callback);
        }
    }

    /* access modifiers changed from: protected */
    public boolean checkVideoStandardValid(SettingsDefinitions.VideoStandard videoStandard) {
        return videoStandard == SettingsDefinitions.VideoStandard.NTSC;
    }

    public void setVideoStandard(SettingsDefinitions.VideoStandard videoStandard, DJISDKCacheHWAbstraction.InnerCallback callback) {
        if (!checkVideoStandardValid(videoStandard)) {
            CallbackUtils.onFailure(callback, DJICameraError.INVALID_PARAMETERS);
        } else {
            super.setVideoStandard(videoStandard, callback);
        }
    }

    /* access modifiers changed from: protected */
    public boolean checkVideoFileFormatValid(SettingsDefinitions.VideoFileFormat videoFileFormat) {
        return videoFileFormat == SettingsDefinitions.VideoFileFormat.MP4;
    }

    public void setVideoFileFormat(SettingsDefinitions.VideoFileFormat videoFileFormat, DJISDKCacheHWAbstraction.InnerCallback callback) {
        if (!checkVideoFileFormatValid(videoFileFormat)) {
            CallbackUtils.onFailure(callback, DJICameraError.INVALID_PARAMETERS);
        } else {
            super.setVideoFileFormat(videoFileFormat, callback);
        }
    }

    @Getter(DJISDKCacheKeys.SERIAL_NUMBER)
    public void getSerialNumber(final DJISDKCacheHWAbstraction.InnerCallback callback) {
        final DataCameraGetSensorID getSensorID = new DataCameraGetSensorID();
        getSensorID.setReceiverId(getReceiverIdByIndex());
        getSensorID.start(new DJIDataCallBack() {
            /* class dji.sdksharedlib.hardware.abstractions.camera.FoldingDrone.DJICameraWM160Abstraction.AnonymousClass1 */

            public void onSuccess(Object model) {
                CallbackUtils.onSuccess(callback, getSensorID.getSensorId());
            }

            public void onFailure(Ccode ccode) {
                CallbackUtils.onFailure(callback, DJIError.getDJIError(ccode));
            }
        });
    }

    /* access modifiers changed from: protected */
    public boolean checkOrientationValid(SettingsDefinitions.Orientation mode) {
        return mode == SettingsDefinitions.Orientation.LANDSCAPE;
    }

    public void setOrientation(SettingsDefinitions.Orientation mode, DJISDKCacheHWAbstraction.InnerCallback callback) {
        if (!checkOrientationValid(mode)) {
            CallbackUtils.onFailure(callback, DJICameraError.INVALID_PARAMETERS);
        } else {
            super.setOrientation(mode, callback);
        }
    }

    /* access modifiers changed from: protected */
    public boolean checkFileIndexModeValid(SettingsDefinitions.FileIndexMode fileIndexMode) {
        return fileIndexMode == SettingsDefinitions.FileIndexMode.SEQUENCE;
    }

    public void setFileIndexMode(SettingsDefinitions.FileIndexMode fileIndexMode, DJISDKCacheHWAbstraction.InnerCallback callback) {
        if (!checkFileIndexModeValid(fileIndexMode)) {
            CallbackUtils.onFailure(callback, DJICameraError.INVALID_PARAMETERS);
        } else {
            super.setFileIndexMode(fileIndexMode, callback);
        }
    }
}
