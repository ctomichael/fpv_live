package dji.sdksharedlib.hardware.abstractions.camera.FoldingDrone;

import dji.common.camera.OriginalPhotoSettings;
import dji.common.camera.SaveOriginalPhotoState;
import dji.common.camera.SettingsDefinitions;
import dji.common.error.DJICameraError;
import dji.common.error.DJIError;
import dji.common.util.CallbackUtils;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.config.P3.Ccode;
import dji.midware.data.model.P3.DataCameraGetSaveOriginalPhoto;
import dji.midware.data.model.P3.DataCameraSetSaveOriginalPhoto;
import dji.midware.data.model.P3.DataCameraSetVideoEncode;
import dji.midware.interfaces.DJIDataCallBack;
import dji.sdksharedlib.extension.CacheHelper;
import dji.sdksharedlib.hardware.abstractions.DJISDKCacheHWAbstraction;
import dji.sdksharedlib.hardware.abstractions.Getter;
import dji.sdksharedlib.hardware.abstractions.Setter;
import dji.sdksharedlib.hardware.abstractions.camera.DJICameraAbstraction;
import dji.sdksharedlib.keycatalog.CameraKeys;
import dji.sdksharedlib.listener.DJIGetCallback;
import dji.sdksharedlib.store.DJISDKCacheParamValue;

@EXClassNullAway
public class DJICameraWM240HasselbladAbstraction extends DJICameraWM230Abstraction {
    /* access modifiers changed from: protected */
    public boolean isDigitalZoomScaleSupported() {
        return false;
    }

    /* access modifiers changed from: protected */
    public boolean isHDRPhotoSupported() {
        return false;
    }

    /* access modifiers changed from: protected */
    public String getDisplayName() {
        return DJICameraAbstraction.DisplayNameMavic2ProCamera;
    }

    /* access modifiers changed from: protected */
    public boolean isNewProtocolSavingOrgImages() {
        return true;
    }

    @Setter(CameraKeys.VIDEO_FILE_COMPRESSION_STANDARD)
    public void setVideoFileCompressionStandard(SettingsDefinitions.VideoFileCompressionStandard standard, DJISDKCacheHWAbstraction.InnerCallback callback) {
        SettingsDefinitions.CameraType cameraType = (SettingsDefinitions.CameraType) CacheHelper.getCamera(this.index, CameraKeys.CAMERA_TYPE);
        SettingsDefinitions.CameraColor cameraColor = (SettingsDefinitions.CameraColor) CacheHelper.getCamera(this.index, CameraKeys.CAMERA_COLOR);
        if (standard == SettingsDefinitions.VideoFileCompressionStandard.H264 && cameraType == SettingsDefinitions.CameraType.DJICameraTypeFC240_1 && cameraColor != SettingsDefinitions.CameraColor.NONE) {
            CallbackUtils.onFailure(callback, DJICameraError.INVALID_PARAMETERS);
            return;
        }
        DataCameraSetVideoEncode setter = new DataCameraSetVideoEncode();
        setter.setReceiverId(getReceiverIdByIndex());
        setter.setPrimaryEncodeType(DataCameraSetVideoEncode.VideoEncodeType.find(standard.value())).start(CallbackUtils.defaultCB(callback, DJICameraError.class));
    }

    @Setter(CameraKeys.SAVE_ORIGINAL_PHOTO_ENABLED)
    public void setSaveOriginalPhotoEnable(SaveOriginalPhotoState enabled, DJISDKCacheHWAbstraction.InnerCallback callback) {
        if (enabled == null) {
            CallbackUtils.onFailure(callback, DJIError.COMMON_PARAM_ILLEGAL);
            return;
        }
        DataCameraSetSaveOriginalPhoto setter = new DataCameraSetSaveOriginalPhoto();
        setter.setHyperLapseEnable(enabled.isHyperLapseState());
        setter.setPanoEnable(enabled.isPanoState());
        setter.setHyperLapseFileType(enabled.getHyperLapseFileType().value());
        setter.setPanoFileType(enabled.getPanoFileType().value());
        setter.setReceiverId(getReceiverIdByIndex());
        setter.start(CallbackUtils.defaultCB(callback, DJICameraError.class));
    }

    @Getter(CameraKeys.SAVE_ORIGINAL_PHOTO_ENABLED)
    public void getSaveOriginalPhotoEnable(final DJISDKCacheHWAbstraction.InnerCallback callback) {
        final DataCameraGetSaveOriginalPhoto getter = new DataCameraGetSaveOriginalPhoto();
        getter.setReceiverId(getReceiverIdByIndex());
        getter.start(new DJIDataCallBack() {
            /* class dji.sdksharedlib.hardware.abstractions.camera.FoldingDrone.DJICameraWM240HasselbladAbstraction.AnonymousClass1 */

            public void onSuccess(Object model) {
                SaveOriginalPhotoState.Builder stateBuilder = new SaveOriginalPhotoState.Builder();
                stateBuilder.setHyperLapseState(getter.getHyperLapseEnable());
                stateBuilder.setPanoState(getter.getPanoEnable());
                stateBuilder.setHyperLapseFileType(SettingsDefinitions.PhotoFileFormat.find(getter.getHyperLapseFileType()));
                stateBuilder.setPanoFileType(SettingsDefinitions.PhotoFileFormat.find(getter.getPanoFileType()));
                CallbackUtils.onSuccess(callback, stateBuilder.build());
            }

            public void onFailure(Ccode ccode) {
                CallbackUtils.onFailure(callback, DJICameraError.getDJIError(ccode));
            }
        });
    }

    @Setter(CameraKeys.HYPERLAPSE_ORIGINAL_PHOTO_SETTINGS)
    public void setHyperlapseOriginalImagesSetting(final OriginalPhotoSettings hyperlapseSettings, final DJISDKCacheHWAbstraction.InnerCallback callback) {
        if (hyperlapseSettings == null) {
            CallbackUtils.onFailure(callback, DJIError.COMMON_PARAM_ILLEGAL);
        } else {
            CacheHelper.getCamera(this.index, CameraKeys.PANO_ORIGINAL_PHOTO_SETTINGS, new DJIGetCallback() {
                /* class dji.sdksharedlib.hardware.abstractions.camera.FoldingDrone.DJICameraWM240HasselbladAbstraction.AnonymousClass2 */

                public void onSuccess(DJISDKCacheParamValue value) {
                    OriginalPhotoSettings panoSettings;
                    if (value == null || value.getData() == null) {
                        panoSettings = new OriginalPhotoSettings(false);
                    } else {
                        panoSettings = (OriginalPhotoSettings) value.getData();
                    }
                    DJICameraWM240HasselbladAbstraction.this.sendSetterPack(panoSettings, hyperlapseSettings, callback);
                }

                public void onFails(DJIError error) {
                    DJICameraWM240HasselbladAbstraction.this.sendSetterPack(new OriginalPhotoSettings(false), hyperlapseSettings, callback);
                }
            });
        }
    }

    @Getter(CameraKeys.HYPERLAPSE_ORIGINAL_PHOTO_SETTINGS)
    public void getHyperlapseOriginalImagesSetting(final DJISDKCacheHWAbstraction.InnerCallback callback) {
        final DataCameraGetSaveOriginalPhoto getter = new DataCameraGetSaveOriginalPhoto();
        getter.setReceiverId(getReceiverIdByIndex());
        getter.start(new DJIDataCallBack() {
            /* class dji.sdksharedlib.hardware.abstractions.camera.FoldingDrone.DJICameraWM240HasselbladAbstraction.AnonymousClass3 */

            public void onSuccess(Object model) {
                CallbackUtils.onSuccess(callback, new OriginalPhotoSettings(getter.getHyperLapseEnable(), SettingsDefinitions.PhotoFileFormat.find(getter.getHyperLapseFileType())));
            }

            public void onFailure(Ccode ccode) {
                CallbackUtils.onFailure(callback, DJICameraError.getDJIError(ccode));
            }
        });
    }

    /* access modifiers changed from: protected */
    public boolean isHDLiveViewAvailable() {
        return true;
    }
}
