package dji.sdksharedlib.hardware.abstractions.camera.FoldingDrone;

import dji.common.camera.SettingsDefinitions;
import dji.common.error.DJICameraError;
import dji.common.product.Model;
import dji.common.util.CallbackUtils;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.config.P3.Ccode;
import dji.midware.data.model.P3.DataCameraGetPushShotParams;
import dji.midware.data.model.P3.DataCameraSetPhoto;
import dji.midware.data.model.P3.DataCameraSetTimeParams;
import dji.midware.data.model.P3.DataEyeGetPushPseudoCameraParams;
import dji.midware.data.model.P3.DataEyeSetPseudoCameraControl;
import dji.midware.interfaces.DJIDataCallBack;
import dji.sdksharedlib.extension.CacheHelper;
import dji.sdksharedlib.hardware.abstractions.DJISDKCacheHWAbstraction;
import dji.sdksharedlib.hardware.abstractions.Setter;
import dji.sdksharedlib.hardware.abstractions.camera.DJICameraAbstraction;
import dji.sdksharedlib.hardware.abstractions.camera.DJICameraBaseAbstraction;
import dji.sdksharedlib.keycatalog.CameraKeys;
import dji.sdksharedlib.keycatalog.ProductKeys;

@EXClassNullAway
public class DJICameraSparkAbstraction extends DJICameraBaseAbstraction {
    /* access modifiers changed from: protected */
    public boolean isMediaDownloadModeSupported() {
        return true;
    }

    /* access modifiers changed from: protected */
    public boolean isVideoPlaybackSupported() {
        return true;
    }

    /* access modifiers changed from: protected */
    public String getDisplayName() {
        return DJICameraAbstraction.DisplayNameSparkCamera;
    }

    /* access modifiers changed from: protected */
    public boolean isHDRPhotoSupported() {
        return false;
    }

    /* access modifiers changed from: protected */
    public boolean isBurstCountSupported(SettingsDefinitions.PhotoBurstCount count) {
        return count == SettingsDefinitions.PhotoBurstCount.BURST_COUNT_3;
    }

    /* access modifiers changed from: protected */
    public boolean isShootShallowFocusSupported() {
        return true;
    }

    /* access modifiers changed from: protected */
    public boolean isShootPanoramaSupported() {
        return true;
    }

    @Setter(CameraKeys.SHOOT_PHOTO_MODE)
    public void setShootPhotoMode(final SettingsDefinitions.ShootPhotoMode photoMode, final DJISDKCacheHWAbstraction.InnerCallback callback) {
        if (isShootPhotoModeSupported(photoMode, callback)) {
            if (photoMode == SettingsDefinitions.ShootPhotoMode.SHALLOW_FOCUS) {
                final DataEyeSetPseudoCameraControl setter = new DataEyeSetPseudoCameraControl();
                setter.setPseudoCameraCmd(DataEyeSetPseudoCameraControl.PseudoCameraCmd.PSEUDO_CAMERA_CMD_SET_MODE_BOKEH).start(new DJIDataCallBack() {
                    /* class dji.sdksharedlib.hardware.abstractions.camera.FoldingDrone.DJICameraSparkAbstraction.AnonymousClass1 */

                    public void onSuccess(Object model) {
                        if (setter.getPseudoCameraCmdResult() == DataEyeSetPseudoCameraControl.PseudoCameraCmdResult.PSEUDO_CAMERA_ACK_SUCCESS) {
                            DJICameraSparkAbstraction.this.startSetShootPhotoModeCmd(SettingsDefinitions.ShootPhotoMode.SINGLE, callback);
                        } else {
                            CallbackUtils.onFailure(callback, DJICameraError.COMMON_EXECUTION_FAILED);
                        }
                    }

                    public void onFailure(Ccode ccode) {
                        if (CacheHelper.getCamera(CameraKeys.SHOOT_PHOTO_MODE) == SettingsDefinitions.ShootPhotoMode.SHALLOW_FOCUS) {
                            DJICameraSparkAbstraction.this.startSetShootPhotoModeCmd(SettingsDefinitions.ShootPhotoMode.SINGLE, callback);
                        } else {
                            CallbackUtils.onFailure(callback, ccode);
                        }
                    }
                });
            } else if (photoMode == SettingsDefinitions.ShootPhotoMode.PANORAMA && CacheHelper.getProduct(ProductKeys.MODEL_NAME) == Model.Spark) {
                SettingsDefinitions.PhotoPanoramaMode panoMode = (SettingsDefinitions.PhotoPanoramaMode) CacheHelper.getCamera(CameraKeys.PHOTO_PANORAMA_MODE);
                if (panoMode == null) {
                    panoMode = SettingsDefinitions.PhotoPanoramaMode.PANORAMA_MODE_3X3;
                }
                final DataEyeSetPseudoCameraControl setter2 = new DataEyeSetPseudoCameraControl();
                setter2.setPseudoCameraCmd(DataEyeSetPseudoCameraControl.PseudoCameraCmd.find(panoMode.value())).start(new DJIDataCallBack() {
                    /* class dji.sdksharedlib.hardware.abstractions.camera.FoldingDrone.DJICameraSparkAbstraction.AnonymousClass2 */

                    public void onSuccess(Object model) {
                        if (setter2.getPseudoCameraCmdResult() == DataEyeSetPseudoCameraControl.PseudoCameraCmdResult.PSEUDO_CAMERA_ACK_SUCCESS) {
                            DJICameraSparkAbstraction.this.startSetShootPhotoModeCmd(SettingsDefinitions.ShootPhotoMode.SINGLE, callback);
                        } else {
                            CallbackUtils.onFailure(callback, DJICameraError.COMMON_EXECUTION_FAILED);
                        }
                    }

                    public void onFailure(Ccode ccode) {
                        if (CacheHelper.getCamera(CameraKeys.SHOOT_PHOTO_MODE) == SettingsDefinitions.ShootPhotoMode.PANORAMA) {
                            DJICameraSparkAbstraction.this.startSetShootPhotoModeCmd(SettingsDefinitions.ShootPhotoMode.SINGLE, callback);
                        } else {
                            CallbackUtils.onFailure(callback, DJICameraError.COMMON_EXECUTION_FAILED);
                        }
                    }
                });
            } else {
                SettingsDefinitions.ShootPhotoMode currentMode = (SettingsDefinitions.ShootPhotoMode) CacheHelper.getCamera(CameraKeys.SHOOT_PHOTO_MODE);
                if (currentMode == null || !(currentMode == SettingsDefinitions.ShootPhotoMode.SHALLOW_FOCUS || currentMode == SettingsDefinitions.ShootPhotoMode.PANORAMA)) {
                    startSetShootPhotoModeCmd(photoMode, callback);
                    return;
                }
                final DataEyeSetPseudoCameraControl setter3 = new DataEyeSetPseudoCameraControl();
                setter3.setPseudoCameraCmd(DataEyeSetPseudoCameraControl.PseudoCameraCmd.PSEUDO_CAMERA_CMD_RELEASE).start(new DJIDataCallBack() {
                    /* class dji.sdksharedlib.hardware.abstractions.camera.FoldingDrone.DJICameraSparkAbstraction.AnonymousClass3 */

                    public void onSuccess(Object model) {
                        if (setter3.getPseudoCameraCmdResult() != DataEyeSetPseudoCameraControl.PseudoCameraCmdResult.PSEUDO_CAMERA_ACK_SUCCESS) {
                            CallbackUtils.onFailure(callback, DJICameraError.COMMON_EXECUTION_FAILED);
                        } else {
                            DJICameraSparkAbstraction.this.startSetShootPhotoModeCmd(photoMode, callback);
                        }
                    }

                    public void onFailure(Ccode ccode) {
                        CallbackUtils.onFailure(callback, DJICameraError.COMMON_EXECUTION_FAILED);
                    }
                });
            }
        }
    }

    /* access modifiers changed from: protected */
    public SettingsDefinitions.ShootPhotoMode getShootPhotoModeFrom(DataCameraGetPushShotParams pushShotParams) {
        if (pushShotParams.getPhotoType() == DataCameraSetPhoto.TYPE.TIME) {
            if (pushShotParams.getTimeParamsType() == DataCameraSetTimeParams.TYPE.Timelapse.value()) {
                return SettingsDefinitions.ShootPhotoMode.TIME_LAPSE;
            }
            return SettingsDefinitions.ShootPhotoMode.INTERVAL;
        } else if (pushShotParams.getPhotoType() == DataCameraSetPhoto.TYPE.SINGLE) {
            return transformShootPhotoMode(SettingsDefinitions.ShootPhotoMode.SINGLE, DataEyeGetPushPseudoCameraParams.getInstance());
        } else {
            return SettingsDefinitions.ShootPhotoMode.find(pushShotParams.getPhotoType());
        }
    }
}
