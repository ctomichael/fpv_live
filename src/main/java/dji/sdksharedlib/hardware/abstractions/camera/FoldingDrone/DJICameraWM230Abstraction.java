package dji.sdksharedlib.hardware.abstractions.camera.FoldingDrone;

import dji.common.camera.OriginalPhotoSettings;
import dji.common.camera.SettingsDefinitions;
import dji.common.error.DJICameraError;
import dji.common.error.DJIError;
import dji.common.util.CallbackUtils;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.config.P3.Ccode;
import dji.midware.data.model.P3.DataCameraGetPushStorageInfo;
import dji.midware.data.model.P3.DataCameraGetSaveOriginalPhoto;
import dji.midware.data.model.P3.DataCameraGetSensorID;
import dji.midware.data.model.P3.DataCameraSetSaveOriginalPhoto;
import dji.midware.data.model.P3.DataEyeGetPushPseudoCameraParams;
import dji.midware.data.model.P3.DataEyeSetPseudoCameraControl;
import dji.midware.data.model.P3.DataSingleVisualParam;
import dji.midware.interfaces.DJIDataCallBack;
import dji.sdksharedlib.extension.CacheHelper;
import dji.sdksharedlib.hardware.abstractions.DJISDKCacheHWAbstraction;
import dji.sdksharedlib.hardware.abstractions.Getter;
import dji.sdksharedlib.hardware.abstractions.Setter;
import dji.sdksharedlib.hardware.abstractions.camera.DJICameraAbstraction;
import dji.sdksharedlib.hardware.abstractions.camera.multistorage.DJICameraMultiStorageAbstraction;
import dji.sdksharedlib.keycatalog.CameraKeys;
import dji.sdksharedlib.keycatalog.DJISDKCacheKeys;
import dji.sdksharedlib.listener.DJIGetCallback;
import dji.sdksharedlib.store.DJISDKCacheParamValue;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

@EXClassNullAway
public class DJICameraWM230Abstraction extends DJICameraMultiStorageAbstraction {
    /* access modifiers changed from: private */
    public DJISDKCacheHWAbstraction.InnerCallback cacheInnerCallback;
    private SettingsDefinitions.PhotoPanoramaMode cachePanoramaMode;
    /* access modifiers changed from: private */
    public Object setterLock = new Object();
    private Runnable startPanoListerningRunnable = new Runnable() {
        /* class dji.sdksharedlib.hardware.abstractions.camera.FoldingDrone.DJICameraWM230Abstraction.AnonymousClass5 */

        public void run() {
            synchronized (DJICameraWM230Abstraction.this.setterLock) {
                DJISDKCacheHWAbstraction.InnerCallback callback = DJICameraWM230Abstraction.this.cacheInnerCallback;
                DJISDKCacheHWAbstraction.InnerCallback unused = DJICameraWM230Abstraction.this.cacheInnerCallback = null;
                CallbackUtils.onFailure(callback, DJIError.COMMON_TIMEOUT);
            }
        }
    };

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
        return DJICameraAbstraction.DisplayNameMavicAirCamera;
    }

    /* access modifiers changed from: protected */
    public boolean isBurstCountSupported(SettingsDefinitions.PhotoBurstCount count) {
        return (count == SettingsDefinitions.PhotoBurstCount.BURST_COUNT_10 || count == SettingsDefinitions.PhotoBurstCount.BURST_COUNT_14 || count == SettingsDefinitions.PhotoBurstCount.UNKNOWN) ? false : true;
    }

    /* access modifiers changed from: protected */
    public boolean isShootShallowFocusSupported() {
        return true;
    }

    /* access modifiers changed from: protected */
    public boolean isShootPanoramaSupported() {
        return true;
    }

    /* access modifiers changed from: protected */
    public boolean isInternalStorageSupported() {
        return true;
    }

    /* access modifiers changed from: protected */
    public boolean isMediaDownModeMapValue2() {
        return true;
    }

    /* access modifiers changed from: protected */
    public boolean isNewProtocolSavingOrgImages() {
        return false;
    }

    public void syncPushDataFromMidware() {
        super.syncPushDataFromMidware();
        if (DataCameraGetPushStorageInfo.getInstance().isGetted()) {
            DataCameraGetPushStorageInfo.getInstance().swapValidData(getExpectedSenderIdByIndex());
            onEvent3BackgroundThread(DataCameraGetPushStorageInfo.getInstance());
        }
    }

    /* access modifiers changed from: protected */
    public int mapCameraModeToProtocolValue(SettingsDefinitions.CameraMode mode) {
        if (SettingsDefinitions.CameraMode.MEDIA_DOWNLOAD == mode) {
            return 2;
        }
        if (SettingsDefinitions.CameraMode.BROADCAST == mode) {
            return -1;
        }
        return mode.value();
    }

    @Setter(CameraKeys.SHOOT_PHOTO_MODE)
    public void setShootPhotoMode(SettingsDefinitions.ShootPhotoMode photoMode, final DJISDKCacheHWAbstraction.InnerCallback callback) {
        if (isShootPhotoModeSupported(photoMode, callback)) {
            if (photoMode == SettingsDefinitions.ShootPhotoMode.PANORAMA) {
                SettingsDefinitions.PhotoPanoramaMode panoMode = (SettingsDefinitions.PhotoPanoramaMode) CacheHelper.getCamera(CameraKeys.PHOTO_PANORAMA_MODE);
                if (panoMode == null || panoMode == SettingsDefinitions.PhotoPanoramaMode.UNKNOWN) {
                    panoMode = SettingsDefinitions.PhotoPanoramaMode.PANORAMA_MODE_3X3;
                }
                final DataEyeSetPseudoCameraControl setter = new DataEyeSetPseudoCameraControl();
                setter.setPseudoCameraCmd(DataEyeSetPseudoCameraControl.PseudoCameraCmd.find(panoMode.value())).start(new DJIDataCallBack() {
                    /* class dji.sdksharedlib.hardware.abstractions.camera.FoldingDrone.DJICameraWM230Abstraction.AnonymousClass1 */

                    public void onSuccess(Object model) {
                        if (setter.getPseudoCameraCmdResult() == DataEyeSetPseudoCameraControl.PseudoCameraCmdResult.PSEUDO_CAMERA_ACK_SUCCESS) {
                            DJICameraWM230Abstraction.this.startSetShootPhotoModeCmd(SettingsDefinitions.ShootPhotoMode.SINGLE, callback);
                        } else {
                            CallbackUtils.onFailure(callback, DJICameraError.COMMON_EXECUTION_FAILED);
                        }
                    }

                    public void onFailure(Ccode ccode) {
                        if (CacheHelper.getCamera(CameraKeys.SHOOT_PHOTO_MODE) == SettingsDefinitions.ShootPhotoMode.PANORAMA) {
                            DJICameraWM230Abstraction.this.startSetShootPhotoModeCmd(SettingsDefinitions.ShootPhotoMode.SINGLE, callback);
                        } else {
                            CallbackUtils.onFailure(callback, DJICameraError.COMMON_EXECUTION_FAILED);
                        }
                    }
                });
                return;
            }
            SettingsDefinitions.ShootPhotoMode currentMode = (SettingsDefinitions.ShootPhotoMode) CacheHelper.getCamera(CameraKeys.SHOOT_PHOTO_MODE);
            if (currentMode != null && currentMode == SettingsDefinitions.ShootPhotoMode.PANORAMA) {
                new DataEyeSetPseudoCameraControl().setPseudoCameraCmd(DataEyeSetPseudoCameraControl.PseudoCameraCmd.PSEUDO_CAMERA_CMD_RELEASE).start((DJIDataCallBack) null);
            }
            startSetShootPhotoModeCmd(photoMode, callback);
        }
    }

    @Setter(CameraKeys.PANO_ORIGINAL_PHOTO_SETTINGS)
    public void setPanoOriginalImagesSetting(OriginalPhotoSettings panoSettings, DJISDKCacheHWAbstraction.InnerCallback callback) {
        if (panoSettings == null) {
            CallbackUtils.onFailure(callback, DJIError.COMMON_PARAM_ILLEGAL);
        } else if (isNewProtocolSavingOrgImages()) {
            setPanoOriginalImagesSettingsByNewProtocol(panoSettings, callback);
        } else {
            setPanoOriginalImagesSettingsByOldProtocol(panoSettings, callback);
        }
    }

    @Getter(CameraKeys.PANO_ORIGINAL_PHOTO_SETTINGS)
    public void getPanoOriginalImagesSetting(DJISDKCacheHWAbstraction.InnerCallback callback) {
        if (isNewProtocolSavingOrgImages()) {
            getPanoOriginalImagesSettingsByNewProtocol(callback);
        } else {
            getPanoOriginalImagesSettingsByOldProtocol(callback);
        }
    }

    /* access modifiers changed from: protected */
    public void setPanoOriginalImagesSettingsByOldProtocol(OriginalPhotoSettings panoSettings, DJISDKCacheHWAbstraction.InnerCallback callback) {
        new DataSingleVisualParam().setGet(false).setParamCmdId(DataSingleVisualParam.ParamCmdId.PANORAMA_SAVE_ORG).setPanoramaSaveOrg(panoSettings.shouldSaveOriginalPhotos()).start(CallbackUtils.defaultCB(callback, DJICameraError.class));
    }

    /* access modifiers changed from: protected */
    public void setPanoOriginalImagesSettingsByNewProtocol(final OriginalPhotoSettings panoSettings, final DJISDKCacheHWAbstraction.InnerCallback callback) {
        CacheHelper.getCamera(this.index, CameraKeys.HYPERLAPSE_ORIGINAL_PHOTO_SETTINGS, new DJIGetCallback() {
            /* class dji.sdksharedlib.hardware.abstractions.camera.FoldingDrone.DJICameraWM230Abstraction.AnonymousClass2 */

            public void onSuccess(DJISDKCacheParamValue value) {
                OriginalPhotoSettings hyperlapseSettings;
                if (value == null || value.getData() == null) {
                    hyperlapseSettings = new OriginalPhotoSettings(false);
                } else {
                    hyperlapseSettings = (OriginalPhotoSettings) value.getData();
                }
                DJICameraWM230Abstraction.this.sendSetterPack(panoSettings, hyperlapseSettings, callback);
            }

            public void onFails(DJIError error) {
                DJICameraWM230Abstraction.this.sendSetterPack(panoSettings, new OriginalPhotoSettings(false), callback);
            }
        });
    }

    /* access modifiers changed from: protected */
    public void getPanoOriginalImagesSettingsByOldProtocol(final DJISDKCacheHWAbstraction.InnerCallback callback) {
        final DataSingleVisualParam getter = new DataSingleVisualParam().setGet(true).setParamCmdId(DataSingleVisualParam.ParamCmdId.PANORAMA_SAVE_ORG);
        getter.start(new DJIDataCallBack() {
            /* class dji.sdksharedlib.hardware.abstractions.camera.FoldingDrone.DJICameraWM230Abstraction.AnonymousClass3 */

            public void onSuccess(Object model) {
                CallbackUtils.onSuccess(callback, new OriginalPhotoSettings(getter.getPanoramaSaveOrg()));
            }

            public void onFailure(Ccode ccode) {
                CallbackUtils.onFailure(callback, DJICameraError.getDJIError(ccode));
            }
        });
    }

    /* access modifiers changed from: protected */
    public void getPanoOriginalImagesSettingsByNewProtocol(final DJISDKCacheHWAbstraction.InnerCallback callback) {
        final DataCameraGetSaveOriginalPhoto getter = new DataCameraGetSaveOriginalPhoto();
        getter.setReceiverId(getReceiverIdByIndex());
        getter.start(new DJIDataCallBack() {
            /* class dji.sdksharedlib.hardware.abstractions.camera.FoldingDrone.DJICameraWM230Abstraction.AnonymousClass4 */

            public void onSuccess(Object model) {
                CallbackUtils.onSuccess(callback, new OriginalPhotoSettings(getter.getPanoEnable(), SettingsDefinitions.PhotoFileFormat.find(getter.getPanoFileType())));
            }

            public void onFailure(Ccode ccode) {
                CallbackUtils.onFailure(callback, DJICameraError.getDJIError(ccode));
            }
        });
    }

    /* access modifiers changed from: protected */
    public void sendSetterPack(OriginalPhotoSettings panoSettings, OriginalPhotoSettings hyperlapseSettings, DJISDKCacheHWAbstraction.InnerCallback callback) {
        DataCameraSetSaveOriginalPhoto setter = new DataCameraSetSaveOriginalPhoto();
        setter.setHyperLapseEnable(hyperlapseSettings.shouldSaveOriginalPhotos());
        setter.setPanoEnable(panoSettings.shouldSaveOriginalPhotos());
        setter.setHyperLapseFileType(hyperlapseSettings.getFormat().value());
        setter.setPanoFileType(panoSettings.getFormat().value());
        setter.setReceiverId(getReceiverIdByIndex());
        setter.start(CallbackUtils.defaultCB(callback, DJICameraError.class));
    }

    private void setupPanoramaTimer() {
        this.handler.postDelayed(this.startPanoListerningRunnable, 2000);
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEvent3BackgroundThread(DataEyeGetPushPseudoCameraParams params) {
        super.onEvent3BackgroundThread(params);
        if (params.isGetted()) {
            synchronized (this.setterLock) {
                if (this.cacheInnerCallback != null && this.cachePanoramaMode == transformPhotoPanoMode(params)) {
                    DJISDKCacheHWAbstraction.InnerCallback callback = this.cacheInnerCallback;
                    this.cacheInnerCallback = null;
                    this.handler.removeCallbacks(this.startPanoListerningRunnable);
                    CallbackUtils.onSuccess(callback, (Object) null);
                }
            }
        }
    }

    @Setter(CameraKeys.PHOTO_PANORAMA_MODE)
    public void setPhotoPanoramaMode(SettingsDefinitions.PhotoPanoramaMode panoramaMode, DJISDKCacheHWAbstraction.InnerCallback callback) {
        if (!isSupportCurrentPanoramaMode(panoramaMode.value())) {
            CallbackUtils.onFailure(callback, DJICameraError.COMMAND_NOT_SUPPORTED_BY_FIRMWARE);
            return;
        }
        DataEyeSetPseudoCameraControl setter = new DataEyeSetPseudoCameraControl();
        if (this.cacheInnerCallback != null) {
            CallbackUtils.onFailure(callback, DJIError.COMMON_SYSTEM_BUSY);
            return;
        }
        this.cacheInnerCallback = callback;
        this.cachePanoramaMode = panoramaMode;
        setupPanoramaTimer();
        setter.setPseudoCameraCmd(DataEyeSetPseudoCameraControl.PseudoCameraCmd.find(panoramaMode.value())).start((DJIDataCallBack) null);
    }

    @Getter(DJISDKCacheKeys.SERIAL_NUMBER)
    public void getSerialNumber(final DJISDKCacheHWAbstraction.InnerCallback callback) {
        final DataCameraGetSensorID getSensorID = new DataCameraGetSensorID();
        getSensorID.setReceiverId(getReceiverIdByIndex());
        getSensorID.start(new DJIDataCallBack() {
            /* class dji.sdksharedlib.hardware.abstractions.camera.FoldingDrone.DJICameraWM230Abstraction.AnonymousClass6 */

            public void onSuccess(Object model) {
                CallbackUtils.onSuccess(callback, getSensorID.getSensorId());
            }

            public void onFailure(Ccode ccode) {
                CallbackUtils.onFailure(callback, DJIError.getDJIError(ccode));
            }
        });
    }
}
