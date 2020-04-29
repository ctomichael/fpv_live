package dji.sdksharedlib.hardware.abstractions.camera.zenmuse;

import android.text.TextUtils;
import dji.common.camera.SettingsDefinitions;
import dji.common.error.DJICameraError;
import dji.common.error.DJIError;
import dji.common.util.CallbackUtils;
import dji.common.util.DJIParamMinMaxCapability;
import dji.midware.data.config.P3.Ccode;
import dji.midware.data.model.P3.DataCameraGetPushShotParams;
import dji.midware.data.model.P3.DataCameraGetPushStateInfo;
import dji.midware.data.model.P3.DataCameraGetPushStorageInfo;
import dji.midware.data.model.P3.DataCameraGetPushTauParam;
import dji.midware.data.model.P3.DataCameraTauParamIsothermValue;
import dji.midware.data.model.P3.DataCameraTauParamer;
import dji.midware.interfaces.DJIDataCallBack;
import dji.sdksharedlib.extension.CacheHelper;
import dji.sdksharedlib.hardware.abstractions.DJISDKCacheHWAbstraction;
import dji.sdksharedlib.hardware.abstractions.Getter;
import dji.sdksharedlib.hardware.abstractions.Setter;
import dji.sdksharedlib.hardware.abstractions.camera.DJICameraAbstraction;
import dji.sdksharedlib.keycatalog.CameraKeys;
import dji.sdksharedlib.listener.DJIActionCallback;
import dji.sdksharedlib.listener.DJIGetCallback;
import dji.sdksharedlib.listener.DJISetCallback;
import dji.sdksharedlib.store.DJISDKCacheParamValue;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class DJICameraWM245DualLightIRAbstraction extends DJICameraTauXT2Abstraction {
    /* access modifiers changed from: protected */
    public boolean isInternalStorageSupported() {
        return true;
    }

    /* access modifiers changed from: protected */
    public String getDisplayName() {
        return DJICameraAbstraction.DisplayNameMavic2EnterpriseDual_IR;
    }

    /* access modifiers changed from: protected */
    public boolean isThermalFFCModeSupported() {
        return true;
    }

    /* access modifiers changed from: protected */
    public DJIParamMinMaxCapability initHorizontAlignmentOffsetRange() {
        return new DJIParamMinMaxCapability(true, -40, 40);
    }

    /* access modifiers changed from: protected */
    public DJIParamMinMaxCapability initVerticalAlignmentOffsetRange() {
        return new DJIParamMinMaxCapability(true, -40, 40);
    }

    @Setter(CameraKeys.PHOTO_ASPECT_RATIO)
    public void setPhotoRatio(SettingsDefinitions.PhotoAspectRatio photoAspectRatio, final DJISDKCacheHWAbstraction.InnerCallback callback) {
        CacheHelper.setCamera(0, CameraKeys.PHOTO_ASPECT_RATIO, photoAspectRatio, new DJISetCallback() {
            /* class dji.sdksharedlib.hardware.abstractions.camera.zenmuse.DJICameraWM245DualLightIRAbstraction.AnonymousClass1 */

            public void onSuccess() {
                CallbackUtils.onSuccess(callback, (Object) null);
            }

            public void onFails(DJIError error) {
                CallbackUtils.onFailure(callback, error);
            }
        });
    }

    public void setCameraMode(SettingsDefinitions.CameraMode mode, final DJISDKCacheHWAbstraction.InnerCallback callback) {
        CacheHelper.setCamera(0, "Mode", mode, new DJISetCallback() {
            /* class dji.sdksharedlib.hardware.abstractions.camera.zenmuse.DJICameraWM245DualLightIRAbstraction.AnonymousClass2 */

            public void onSuccess() {
                CallbackUtils.onSuccess(callback, (Object) null);
            }

            public void onFails(DJIError error) {
                CallbackUtils.onFailure(callback, error);
            }
        });
    }

    public void setCameraStorageLocation(SettingsDefinitions.StorageLocation mode, final DJISDKCacheHWAbstraction.InnerCallback callback) {
        CacheHelper.setCamera(0, CameraKeys.CAMERA_STORAGE_LOCATION, mode, new DJISetCallback() {
            /* class dji.sdksharedlib.hardware.abstractions.camera.zenmuse.DJICameraWM245DualLightIRAbstraction.AnonymousClass3 */

            public void onSuccess() {
                CallbackUtils.onSuccess(callback, (Object) null);
            }

            public void onFails(DJIError error) {
                CallbackUtils.onFailure(callback, error);
            }
        });
    }

    public void formatSDCard(final DJISDKCacheHWAbstraction.InnerCallback callback) {
        CacheHelper.performActionToCamera(0, CameraKeys.FORMAT_SD_CARD, new DJIActionCallback() {
            /* class dji.sdksharedlib.hardware.abstractions.camera.zenmuse.DJICameraWM245DualLightIRAbstraction.AnonymousClass4 */

            public void onSuccess(Object result) {
                CallbackUtils.onSuccess(callback, (Object) null);
            }

            public void onFails(DJIError error) {
                CallbackUtils.onFailure(callback, error);
            }
        }, new Object[0]);
    }

    public void formatStorage(final DJISDKCacheHWAbstraction.InnerCallback callback, SettingsDefinitions.StorageLocation location) {
        CacheHelper.performActionToCamera(0, CameraKeys.FORMAT_INTERNAL_STORAGE, new DJIActionCallback() {
            /* class dji.sdksharedlib.hardware.abstractions.camera.zenmuse.DJICameraWM245DualLightIRAbstraction.AnonymousClass5 */

            public void onSuccess(Object result) {
                CallbackUtils.onSuccess(callback, (Object) null);
            }

            public void onFails(DJIError error) {
                CallbackUtils.onFailure(callback, error);
            }
        }, new Object[0]);
    }

    public void setPhotoFileFormat(SettingsDefinitions.PhotoFileFormat photoFormat, final DJISDKCacheHWAbstraction.InnerCallback callback) {
        CacheHelper.setCamera(0, CameraKeys.PHOTO_FILE_FORMAT, photoFormat, new DJISetCallback() {
            /* class dji.sdksharedlib.hardware.abstractions.camera.zenmuse.DJICameraWM245DualLightIRAbstraction.AnonymousClass6 */

            public void onSuccess() {
                CallbackUtils.onSuccess(callback, (Object) null);
            }

            public void onFails(DJIError error) {
                CallbackUtils.onFailure(callback, error);
            }
        });
    }

    @Setter(CameraKeys.LED_AUTO_TURN_OFF_ENABLED)
    public void setTurnOffLEDDuringShootPhotoEnabled(boolean enabled, final DJISDKCacheHWAbstraction.InnerCallback callback) {
        CacheHelper.setCamera(0, CameraKeys.LED_AUTO_TURN_OFF_ENABLED, Boolean.valueOf(enabled), new DJISetCallback() {
            /* class dji.sdksharedlib.hardware.abstractions.camera.zenmuse.DJICameraWM245DualLightIRAbstraction.AnonymousClass7 */

            public void onSuccess() {
                CallbackUtils.onSuccess(callback, (Object) null);
            }

            public void onFails(DJIError error) {
                CallbackUtils.onFailure(callback, error);
            }
        });
    }

    @Setter(CameraKeys.AUTO_LOCK_GIMBAL_ENABLED)
    public void setLockGimbalWhileShooting(boolean enabled, final DJISDKCacheHWAbstraction.InnerCallback callback) {
        CacheHelper.setCamera(0, CameraKeys.AUTO_LOCK_GIMBAL_ENABLED, Boolean.valueOf(enabled), new DJISetCallback() {
            /* class dji.sdksharedlib.hardware.abstractions.camera.zenmuse.DJICameraWM245DualLightIRAbstraction.AnonymousClass8 */

            public void onSuccess() {
                CallbackUtils.onSuccess(callback, (Object) null);
            }

            public void onFails(DJIError error) {
                CallbackUtils.onFailure(callback, error);
            }
        });
    }

    public void setShootPhotoMode(SettingsDefinitions.ShootPhotoMode photoMode, final DJISDKCacheHWAbstraction.InnerCallback callback) {
        CacheHelper.setCamera(0, CameraKeys.SHOOT_PHOTO_MODE, photoMode, new DJISetCallback() {
            /* class dji.sdksharedlib.hardware.abstractions.camera.zenmuse.DJICameraWM245DualLightIRAbstraction.AnonymousClass9 */

            public void onSuccess() {
                CallbackUtils.onSuccess(callback, (Object) null);
            }

            public void onFails(DJIError error) {
                CallbackUtils.onFailure(callback, error);
            }
        });
    }

    public void setVideoFileFormat(SettingsDefinitions.VideoFileFormat videoFileFormat, final DJISDKCacheHWAbstraction.InnerCallback callback) {
        CacheHelper.setCamera(0, CameraKeys.VIDEO_FILE_FORMAT, videoFileFormat, new DJISetCallback() {
            /* class dji.sdksharedlib.hardware.abstractions.camera.zenmuse.DJICameraWM245DualLightIRAbstraction.AnonymousClass10 */

            public void onSuccess() {
                CallbackUtils.onSuccess(callback, (Object) null);
            }

            public void onFails(DJIError error) {
                CallbackUtils.onFailure(callback, error);
            }
        });
    }

    @Setter(CameraKeys.THERMAL_ISOTHERM_UNIT)
    public void setThermalIsothermUnit(SettingsDefinitions.ThermalIsothermUnit unit, DJISDKCacheHWAbstraction.InnerCallback callback) {
        if (unit == SettingsDefinitions.ThermalIsothermUnit.PERCENTAGE) {
            callback.onFails(DJICameraError.INVALID_PARAMETERS);
        } else {
            super.setThermalIsothermUnit(unit, callback);
        }
    }

    @Setter(CameraKeys.THERMAL_ISOTHERM_UPPER_VALUE)
    public void setThermalIsothermUpperValue(int value, final DJISDKCacheHWAbstraction.InnerCallback callback) {
        if (DataCameraGetPushTauParam.getInstance().getIsothermUnit(getExpectedSenderIdByIndex()) == 0) {
            CallbackUtils.onFailure(callback, DJICameraError.INVALID_PARAMETERS);
        } else if (value < DataCameraGetPushTauParam.getInstance().getIsothermLower(getExpectedSenderIdByIndex())) {
            CallbackUtils.onFailure(callback, DJICameraError.INVALID_PARAMETERS);
        } else {
            DataCameraTauParamIsothermValue setter = new DataCameraTauParamIsothermValue();
            setter.setType(DataCameraTauParamer.ParamCmd.ISOTHERM_UPPER).setValue((short) value).setOpt(false);
            setter.setReceiverId(getReceiverIdByIndex());
            setter.start(new DJIDataCallBack() {
                /* class dji.sdksharedlib.hardware.abstractions.camera.zenmuse.DJICameraWM245DualLightIRAbstraction.AnonymousClass11 */

                public void onSuccess(Object model) {
                    CallbackUtils.onSuccess(callback, (Object) null);
                }

                public void onFailure(Ccode ccode) {
                    CallbackUtils.onFailure(callback, DJICameraError.getDJIError(ccode));
                }
            });
        }
    }

    @Setter(CameraKeys.THERMAL_ISOTHERM_LOWER_VALUE)
    public void setThermalIsothermLowerValue(int value, final DJISDKCacheHWAbstraction.InnerCallback callback) {
        if (DataCameraGetPushTauParam.getInstance().getIsothermUnit(getExpectedSenderIdByIndex()) == 0) {
            CallbackUtils.onFailure(callback, DJICameraError.INVALID_PARAMETERS);
        } else if (value > DataCameraGetPushTauParam.getInstance().getIsothermUpper(getExpectedSenderIdByIndex())) {
            CallbackUtils.onFailure(callback, DJICameraError.INVALID_PARAMETERS);
        } else {
            DataCameraTauParamIsothermValue setter = new DataCameraTauParamIsothermValue();
            setter.setType(DataCameraTauParamer.ParamCmd.ISOTHERM_LOWER).setValue((short) value).setOpt(false);
            setter.setReceiverId(getReceiverIdByIndex());
            setter.start(new DJIDataCallBack() {
                /* class dji.sdksharedlib.hardware.abstractions.camera.zenmuse.DJICameraWM245DualLightIRAbstraction.AnonymousClass12 */

                public void onSuccess(Object model) {
                    CallbackUtils.onSuccess(callback, (Object) null);
                }

                public void onFailure(Ccode ccode) {
                    CallbackUtils.onFailure(callback, DJICameraError.getDJIError(ccode));
                }
            });
        }
    }

    @Setter(CameraKeys.CUSTOM_INFORMATION)
    public void setMediaFileCustomInformation(String information, final DJISDKCacheHWAbstraction.InnerCallback callback) {
        CacheHelper.setCamera(0, CameraKeys.CUSTOM_INFORMATION, information, new DJISetCallback() {
            /* class dji.sdksharedlib.hardware.abstractions.camera.zenmuse.DJICameraWM245DualLightIRAbstraction.AnonymousClass13 */

            public void onSuccess() {
                CallbackUtils.onSuccess(callback, (Object) null);
            }

            public void onFails(DJIError error) {
                CallbackUtils.onFailure(callback, error);
            }
        });
    }

    @Getter(CameraKeys.CUSTOM_INFORMATION)
    public void getMediaFileCustomInformation(final DJISDKCacheHWAbstraction.InnerCallback callback) {
        CacheHelper.getCamera(0, CameraKeys.CUSTOM_INFORMATION, new DJIGetCallback() {
            /* class dji.sdksharedlib.hardware.abstractions.camera.zenmuse.DJICameraWM245DualLightIRAbstraction.AnonymousClass14 */

            public void onSuccess(DJISDKCacheParamValue value) {
                if (value != null) {
                    String information = (String) value.getData();
                    if (!TextUtils.isEmpty(information)) {
                        CallbackUtils.onSuccess(callback, information);
                        return;
                    }
                }
                CallbackUtils.onFailure(callback, DJICameraError.PARAMETERS_GET_FAILED);
            }

            public void onFails(DJIError error) {
                CallbackUtils.onFailure(callback, error);
            }
        });
    }

    @Setter(CameraKeys.VIDEO_CAPTION_ENABLED)
    public void setVideoCaptionEnabled(boolean enabled, final DJISDKCacheHWAbstraction.InnerCallback callback) {
        CacheHelper.setCamera(0, CameraKeys.VIDEO_CAPTION_ENABLED, Boolean.valueOf(enabled), new DJISetCallback() {
            /* class dji.sdksharedlib.hardware.abstractions.camera.zenmuse.DJICameraWM245DualLightIRAbstraction.AnonymousClass15 */

            public void onSuccess() {
                CallbackUtils.onSuccess(callback, (Object) null);
            }

            public void onFails(DJIError error) {
                CallbackUtils.onFailure(callback, error);
            }
        });
    }

    @Getter(CameraKeys.VIDEO_CAPTION_ENABLED)
    public void getVideoCaptionEnabled(final DJISDKCacheHWAbstraction.InnerCallback callback) {
        CacheHelper.getCamera(0, CameraKeys.VIDEO_CAPTION_ENABLED, new DJIGetCallback() {
            /* class dji.sdksharedlib.hardware.abstractions.camera.zenmuse.DJICameraWM245DualLightIRAbstraction.AnonymousClass16 */

            public void onSuccess(DJISDKCacheParamValue value) {
                Boolean enabled;
                if (value == null || (enabled = (Boolean) value.getData()) == null) {
                    CallbackUtils.onFailure(callback, DJICameraError.PARAMETERS_GET_FAILED);
                } else {
                    CallbackUtils.onSuccess(callback, enabled);
                }
            }

            public void onFails(DJIError error) {
                CallbackUtils.onFailure(callback, error);
            }
        });
    }

    @Setter(CameraKeys.FILE_INDEX_MODE)
    public void setFileIndexMode(SettingsDefinitions.FileIndexMode fileIndexMode, final DJISDKCacheHWAbstraction.InnerCallback callback) {
        CacheHelper.setCamera(0, CameraKeys.FILE_INDEX_MODE, fileIndexMode, new DJISetCallback() {
            /* class dji.sdksharedlib.hardware.abstractions.camera.zenmuse.DJICameraWM245DualLightIRAbstraction.AnonymousClass17 */

            public void onSuccess() {
                CallbackUtils.onSuccess(callback, (Object) null);
            }

            public void onFails(DJIError error) {
                CallbackUtils.onFailure(callback, error);
            }
        });
    }

    /* access modifiers changed from: protected */
    public void updateThermalCameraState(DataCameraGetPushTauParam pushShotParams) {
        notifyValueChangeForKeyPath((SettingsDefinitions.VideoFileFormat) CacheHelper.getCamera(0, CameraKeys.VIDEO_FILE_FORMAT), convertKeyToPath(CameraKeys.VIDEO_FILE_FORMAT));
        notifyValueChangeForKeyPath(SettingsDefinitions.DisplayMode.find(pushShotParams.getDisplayModeOfLiveStream()), convertKeyToPath(CameraKeys.DISPLAY_MODE));
        notifyValueChangeForKeyPath(SettingsDefinitions.PIPPosition.find(pushShotParams.getPipModeOfLiveStream()), convertKeyToPath(CameraKeys.PIP_POSITION));
        notifyValueChangeForKeyPath(pushShotParams.getBlendingLevel(), convertKeyToPath(CameraKeys.MSX_LEVEL));
        notifyValueChangeForKeyPath(pushShotParams.getHorizontalPosition(), convertKeyToPath(CameraKeys.DUAL_FEED_HORIZONTAL_ALIGNMENT_OFFSET));
        notifyValueChangeForKeyPath(pushShotParams.getVerticalPosition(), convertKeyToPath(CameraKeys.DUAL_FEED_VERTICAL_ALIGNMENT_OFFSET));
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEvent3BackgroundThread(DataCameraGetPushStateInfo pushStateInfo) {
        if (pushStateInfo != null && pushStateInfo.isGetted()) {
            onGetCameraStateInfo(pushStateInfo);
            checkShootPhotoStatus();
            checkRecordVideoStatus();
        }
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEvent3BackgroundThread(DataCameraGetPushStorageInfo status) {
        if (status != null && status.isGetted()) {
            updateCameraStorageInfo(status);
        }
    }

    public void onEvent3BackgroundThread(DataCameraGetPushShotParams pushShotParams) {
        super.onEvent3BackgroundThread(pushShotParams);
        boolean isLockedGimbalWhenShot = pushShotParams.isLockedGimbalWhenShot();
        notifyValueChangeForKeyPath(Boolean.valueOf(isLockedGimbalWhenShot), CameraKeys.IS_LOCK_GIMBAL_WHEN_SHOT);
        notifyValueChangeForKeyPath(Boolean.valueOf(isLockedGimbalWhenShot), CameraKeys.AUTO_LOCK_GIMBAL_ENABLED);
        notifyValueChangeForKeyPath(SettingsDefinitions.PhotoAspectRatio.find(pushShotParams.getImageRatio().value()), convertKeyToPath(CameraKeys.PHOTO_ASPECT_RATIO));
    }
}
