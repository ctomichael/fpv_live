package dji.sdksharedlib.hardware.abstractions.camera.zenmuse;

import dji.common.camera.SettingsDefinitions;
import dji.common.error.DJICameraError;
import dji.common.error.DJIError;
import dji.common.util.CallbackUtils;
import dji.common.util.DJIParamMinMaxCapability;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.config.P3.Ccode;
import dji.midware.data.model.P3.DataCameraGetPushShotParams;
import dji.midware.data.model.P3.DataCameraGetPushTauParam;
import dji.midware.data.model.P3.DataCameraSetDisplayModeOfXT2;
import dji.midware.data.model.P3.DataCameraSetPhoto;
import dji.midware.data.model.P3.DataCameraSetPhotoMode;
import dji.midware.data.model.P3.DataCameraSetTimeParams;
import dji.midware.interfaces.DJIDataCallBack;
import dji.sdksharedlib.extension.CacheHelper;
import dji.sdksharedlib.hardware.abstractions.DJISDKCacheHWAbstraction;
import dji.sdksharedlib.hardware.abstractions.Setter;
import dji.sdksharedlib.hardware.abstractions.camera.DJICameraAbstraction;
import dji.sdksharedlib.keycatalog.CameraKeys;
import dji.sdksharedlib.listener.DJIActionCallback;
import dji.sdksharedlib.listener.DJISetCallback;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

@EXClassNullAway
public class DJICameraTauXT2Abstraction extends DJICameraXTBaseAbstraction {
    /* access modifiers changed from: protected */
    public String getDisplayName() {
        return DJICameraAbstraction.DisplayNameXT2_IR;
    }

    /* access modifiers changed from: protected */
    public void initializeComponentCharacteristics() {
        addCharacteristics(CameraKeys.class, getClass());
    }

    /* access modifiers changed from: protected */
    public void initializeCustomizedKey() {
        super.initializeCustomizedKey();
        notifyValueChangeForKeyPath(initHorizontAlignmentOffsetRange(), convertKeyToPath(CameraKeys.DUAL_FEED_HORIZONTAL_ALIGNMENT_OFFSET_RANGE));
        notifyValueChangeForKeyPath(initVerticalAlignmentOffsetRange(), convertKeyToPath(CameraKeys.DUAL_FEED_VERTICAL_ALIGNMENT_OFFSET_RANGE));
    }

    /* access modifiers changed from: protected */
    public DJIParamMinMaxCapability initHorizontAlignmentOffsetRange() {
        return new DJIParamMinMaxCapability(true, -100, 100);
    }

    /* access modifiers changed from: protected */
    public DJIParamMinMaxCapability initVerticalAlignmentOffsetRange() {
        return new DJIParamMinMaxCapability(true, -8, 8);
    }

    @Setter(CameraKeys.DISPLAY_MODE)
    public void setDisplayModeForXT2(SettingsDefinitions.DisplayMode displayModeParam, DJISDKCacheHWAbstraction.InnerCallback callback) {
        SettingsDefinitions.DisplayMode[] range = (SettingsDefinitions.DisplayMode[]) CacheHelper.getCamera(this.index, CameraKeys.DISPLAY_MODE_RANGE);
        boolean isValid = false;
        if (range != null) {
            int length = range.length;
            int i = 0;
            while (true) {
                if (i >= length) {
                    break;
                } else if (range[i] == displayModeParam) {
                    isValid = true;
                    break;
                } else {
                    i++;
                }
            }
        } else {
            isValid = true;
        }
        if (!isValid) {
            CallbackUtils.onFailure(callback, DJICameraError.INVALID_PARAMETERS);
            return;
        }
        DataCameraSetDisplayModeOfXT2 setter = DataCameraSetDisplayModeOfXT2.getInstance();
        setter.setDisplayMode(DataCameraSetDisplayModeOfXT2.DisplayMode.find(displayModeParam.value()));
        setter.setReceiverId(getReceiverIdByIndex());
        setter.start(CallbackUtils.defaultCB(callback, DJICameraError.class));
    }

    @Setter(CameraKeys.PIP_POSITION)
    public void setPipModeForXT2(SettingsDefinitions.PIPPosition pipPosition, DJISDKCacheHWAbstraction.InnerCallback callback) {
        DataCameraSetDisplayModeOfXT2 setter = DataCameraSetDisplayModeOfXT2.getInstance();
        setter.setPipMode(DataCameraSetDisplayModeOfXT2.PIPMode.find(pipPosition.value()));
        setter.setReceiverId(getReceiverIdByIndex());
        setter.start(CallbackUtils.defaultCB(callback, DJICameraError.class));
    }

    @Setter(CameraKeys.MSX_LEVEL)
    public void setBlendingLevelForXT2(int blendingLevel, DJISDKCacheHWAbstraction.InnerCallback callback) {
        DataCameraSetDisplayModeOfXT2 setter = DataCameraSetDisplayModeOfXT2.getInstance();
        setter.setBlendingLevel(blendingLevel);
        setter.setReceiverId(getReceiverIdByIndex());
        setter.start(CallbackUtils.defaultCB(callback, DJICameraError.class));
    }

    @Setter(CameraKeys.DUAL_FEED_HORIZONTAL_ALIGNMENT_OFFSET)
    public void setHorizontalPositionForXT2(int position, DJISDKCacheHWAbstraction.InnerCallback callback) {
        DJIParamMinMaxCapability capability = (DJIParamMinMaxCapability) CacheHelper.getCamera(this.index, CameraKeys.DUAL_FEED_HORIZONTAL_ALIGNMENT_OFFSET_RANGE);
        if (position < capability.getMin().intValue() || position > capability.getMax().intValue()) {
            CallbackUtils.onFailure(callback, DJICameraError.INVALID_PARAMETERS);
            return;
        }
        DataCameraSetDisplayModeOfXT2 setter = DataCameraSetDisplayModeOfXT2.getInstance();
        setter.setHorizontalPosition(position);
        setter.setReceiverId(getReceiverIdByIndex());
        setter.start(CallbackUtils.defaultCB(callback, DJICameraError.class));
    }

    @Setter(CameraKeys.DUAL_FEED_VERTICAL_ALIGNMENT_OFFSET)
    public void setVerticalPositionForXT2(int position, DJISDKCacheHWAbstraction.InnerCallback callback) {
        DJIParamMinMaxCapability capability = (DJIParamMinMaxCapability) CacheHelper.getCamera(this.index, CameraKeys.DUAL_FEED_VERTICAL_ALIGNMENT_OFFSET_RANGE);
        if (position < capability.getMin().intValue() || position > capability.getMax().intValue()) {
            CallbackUtils.onFailure(callback, DJICameraError.INVALID_PARAMETERS);
            return;
        }
        DataCameraSetDisplayModeOfXT2 setter = DataCameraSetDisplayModeOfXT2.getInstance();
        setter.setVerticalPosition(position);
        setter.setReceiverId(getReceiverIdByIndex());
        setter.start(CallbackUtils.defaultCB(callback, DJICameraError.class));
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEvent3BackgroundThread(DataCameraGetPushTauParam pushShotParams) {
        super.onEvent3BackgroundThread(pushShotParams);
        if (pushShotParams != null && pushShotParams.isGetted() && isValidSender(pushShotParams.getSenderId())) {
            updateThermalCameraState(pushShotParams);
        }
    }

    /* access modifiers changed from: protected */
    public void updateThermalCameraState(DataCameraGetPushTauParam pushShotParams) {
        notifyValueChangeForKeyPath(SettingsDefinitions.VideoFileFormat.find(pushShotParams.getVideoFileFormat()), convertKeyToPath(CameraKeys.VIDEO_FILE_FORMAT));
        notifyValueChangeForKeyPath(SettingsDefinitions.DisplayMode.find(pushShotParams.getDisplayModeOfLiveStream()), convertKeyToPath(CameraKeys.DISPLAY_MODE));
        notifyValueChangeForKeyPath(SettingsDefinitions.PIPPosition.find(pushShotParams.getPipModeOfLiveStream()), convertKeyToPath(CameraKeys.PIP_POSITION));
        notifyValueChangeForKeyPath(pushShotParams.getBlendingLevel(), convertKeyToPath(CameraKeys.MSX_LEVEL));
        notifyValueChangeForKeyPath(pushShotParams.getHorizontalPosition(), convertKeyToPath(CameraKeys.DUAL_FEED_HORIZONTAL_ALIGNMENT_OFFSET));
        notifyValueChangeForKeyPath(pushShotParams.getVerticalPosition(), convertKeyToPath(CameraKeys.DUAL_FEED_VERTICAL_ALIGNMENT_OFFSET));
    }

    public void onEvent3BackgroundThread(DataCameraGetPushShotParams pushShotParams) {
        SettingsDefinitions.ShootPhotoMode shootPhotoMode;
        if (pushShotParams.isGetted() && pushShotParams.getSenderId() == 0) {
            if (pushShotParams.getPhotoType() != DataCameraSetPhoto.TYPE.TIME) {
                shootPhotoMode = SettingsDefinitions.ShootPhotoMode.find(pushShotParams.getPhotoType());
            } else if (pushShotParams.getTimeParamsType() == DataCameraSetTimeParams.TYPE.Timelapse.value()) {
                shootPhotoMode = SettingsDefinitions.ShootPhotoMode.TIME_LAPSE;
            } else {
                shootPhotoMode = SettingsDefinitions.ShootPhotoMode.INTERVAL;
            }
            notifyValueChangeForKeyPath(SettingsDefinitions.PhotoBurstCount.find(pushShotParams.getContinuous()), convertKeyToPath(CameraKeys.PHOTO_BURST_COUNT));
            notifyValueChangeForKeyPath(Integer.valueOf(pushShotParams.getTimeCountdown()), convertKeyToPath(CameraKeys.INTERVAL_SHOOT_COUNTDOWN));
            notifyValueChangeForKeyPath(shootPhotoMode, convertKeyToPath(CameraKeys.SHOOT_PHOTO_MODE));
            notifyValueChangeForKeyPath(new SettingsDefinitions.PhotoTimeIntervalSettings(pushShotParams.getTimeParamsNum(), pushShotParams.getTimeParamsPeriod()), convertKeyToPath(CameraKeys.PHOTO_TIME_INTERVAL_SETTINGS));
        }
    }

    public void startShootPhoto(final DJISDKCacheHWAbstraction.InnerCallback callback) {
        CacheHelper.performActionToCamera(0, CameraKeys.START_SHOOT_PHOTO, new DJIActionCallback() {
            /* class dji.sdksharedlib.hardware.abstractions.camera.zenmuse.DJICameraTauXT2Abstraction.AnonymousClass1 */

            public void onSuccess(Object result) {
                CallbackUtils.onSuccess(callback, result);
            }

            public void onFails(DJIError error) {
                CallbackUtils.onFailure(callback, error);
            }
        }, new Object[0]);
    }

    public void stopShootPhoto(final DJISDKCacheHWAbstraction.InnerCallback callback) {
        CacheHelper.performActionToCamera(0, CameraKeys.STOP_SHOOT_PHOTO, new DJIActionCallback() {
            /* class dji.sdksharedlib.hardware.abstractions.camera.zenmuse.DJICameraTauXT2Abstraction.AnonymousClass2 */

            public void onSuccess(Object result) {
                CallbackUtils.onSuccess(callback, result);
            }

            public void onFails(DJIError error) {
                CallbackUtils.onFailure(callback, error);
            }
        }, new Object[0]);
    }

    public void startRecordVideo(final DJISDKCacheHWAbstraction.InnerCallback callback) {
        CacheHelper.performActionToCamera(0, CameraKeys.START_RECORD_VIDEO, new DJIActionCallback() {
            /* class dji.sdksharedlib.hardware.abstractions.camera.zenmuse.DJICameraTauXT2Abstraction.AnonymousClass3 */

            public void onSuccess(Object result) {
                CallbackUtils.onSuccess(callback, result);
            }

            public void onFails(DJIError error) {
                CallbackUtils.onFailure(callback, error);
            }
        }, new Object[0]);
    }

    public void stopRecordVideo(final DJISDKCacheHWAbstraction.InnerCallback callback) {
        CacheHelper.performActionToCamera(0, CameraKeys.STOP_RECORD_VIDEO, new DJIActionCallback() {
            /* class dji.sdksharedlib.hardware.abstractions.camera.zenmuse.DJICameraTauXT2Abstraction.AnonymousClass4 */

            public void onSuccess(Object result) {
                CallbackUtils.onSuccess(callback, result);
            }

            public void onFails(DJIError error) {
                CallbackUtils.onFailure(callback, error);
            }
        }, new Object[0]);
    }

    @Setter(CameraKeys.PHOTO_BURST_COUNT)
    public void setPhotoBurstCount(SettingsDefinitions.PhotoBurstCount count, final DJISDKCacheHWAbstraction.InnerCallback callback) {
        CacheHelper.setCamera(0, CameraKeys.PHOTO_BURST_COUNT, count, new DJISetCallback() {
            /* class dji.sdksharedlib.hardware.abstractions.camera.zenmuse.DJICameraTauXT2Abstraction.AnonymousClass5 */

            public void onSuccess() {
                CallbackUtils.onSuccess(callback, (Object) null);
            }

            public void onFails(DJIError error) {
                CallbackUtils.onFailure(callback, error);
            }
        });
    }

    public void setShootPhotoMode(SettingsDefinitions.ShootPhotoMode photoMode, final DJISDKCacheHWAbstraction.InnerCallback callback) {
        if (photoMode == null) {
            CallbackUtils.onFailure(callback, DJICameraError.INVALID_PARAMETERS);
        } else if (photoMode == SettingsDefinitions.ShootPhotoMode.SINGLE || photoMode == SettingsDefinitions.ShootPhotoMode.INTERVAL || photoMode == SettingsDefinitions.ShootPhotoMode.BURST) {
            DataCameraSetPhotoMode setter = prepareShootPhotoMode(photoMode);
            setter.setReceiverId(getReceiverIdByIndex());
            setter.start(new DJIDataCallBack() {
                /* class dji.sdksharedlib.hardware.abstractions.camera.zenmuse.DJICameraTauXT2Abstraction.AnonymousClass6 */

                public void onSuccess(Object model) {
                    CallbackUtils.onSuccess(callback, model);
                }

                public void onFailure(Ccode ccode) {
                    CallbackUtils.onFailure(callback, DJICameraError.getDJIError(ccode));
                }
            });
        } else {
            CallbackUtils.onFailure(callback, DJICameraError.INVALID_PARAMETERS);
        }
    }

    public void setPhotoTimeIntervalSettings(SettingsDefinitions.PhotoTimeIntervalSettings param, final DJISDKCacheHWAbstraction.InnerCallback callback) {
        CacheHelper.setCamera(0, CameraKeys.PHOTO_TIME_INTERVAL_SETTINGS, param, new DJISetCallback() {
            /* class dji.sdksharedlib.hardware.abstractions.camera.zenmuse.DJICameraTauXT2Abstraction.AnonymousClass7 */

            public void onSuccess() {
                CallbackUtils.onSuccess(callback, (Object) null);
            }

            public void onFails(DJIError error) {
                CallbackUtils.onFailure(callback, error);
            }
        });
    }
}
