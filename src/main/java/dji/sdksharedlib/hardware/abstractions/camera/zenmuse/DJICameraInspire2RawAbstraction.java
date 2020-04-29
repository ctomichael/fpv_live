package dji.sdksharedlib.hardware.abstractions.camera.zenmuse;

import dji.common.camera.CameraParamRangeManager;
import dji.common.camera.CameraSSDVideoLicense;
import dji.common.camera.ResolutionAndFrameRate;
import dji.common.camera.SettingsDefinitions;
import dji.common.error.DJICameraError;
import dji.common.error.DJIError;
import dji.common.util.CallbackUtils;
import dji.common.util.DJICameraEnumMappingUtil;
import dji.fieldAnnotation.EXClassNullAway;
import dji.internal.camera.SSDFileSystem;
import dji.internal.camera.SSDRawMode;
import dji.midware.data.config.P3.Ccode;
import dji.midware.data.model.P3.DataCameraFormatSSD;
import dji.midware.data.model.P3.DataCameraGetPushRawParams;
import dji.midware.data.model.P3.DataCameraGetPushStateInfo;
import dji.midware.data.model.P3.DataCameraSetRawVideoFormat;
import dji.midware.data.model.P3.DataCameraSetVideoSync;
import dji.midware.interfaces.DJIDataCallBack;
import dji.sdksharedlib.DJISDKCache;
import dji.sdksharedlib.extension.KeyHelper;
import dji.sdksharedlib.hardware.abstractions.Action;
import dji.sdksharedlib.hardware.abstractions.DJISDKCacheHWAbstraction;
import dji.sdksharedlib.hardware.abstractions.Setter;
import dji.sdksharedlib.keycatalog.CameraKeys;
import dji.sdksharedlib.store.DJISDKCacheParamValue;
import dji.sdksharedlib.store.DJISDKCacheStoreLayer;
import java.util.ArrayList;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

@EXClassNullAway
public class DJICameraInspire2RawAbstraction extends DJICameraInspire2Abstraction {
    private static final String TAG = "In2 Raw Camera";

    public void init(String component, int index, DJISDKCacheStoreLayer storeLayer, DJISDKCacheHWAbstraction.OnValueChangeListener onValueChangeListener) {
        super.init(component, index, storeLayer, onValueChangeListener);
    }

    /* access modifiers changed from: protected */
    public boolean isSSDSupported() {
        return true;
    }

    /* access modifiers changed from: protected */
    public boolean isAFSupported() {
        return true;
    }

    /* access modifiers changed from: protected */
    public boolean isMFSupported() {
        return true;
    }

    /* access modifiers changed from: protected */
    public boolean isShootPhotoRawBurstSupported() {
        return true;
    }

    /* JADX DEBUG: Failed to find minimal casts for resolve overloaded methods, cast all args instead
     method: dji.sdksharedlib.hardware.abstractions.DJISDKCacheHWAbstraction.notifyValueChangeForKeyPath(java.lang.Object, dji.sdksharedlib.keycatalog.DJISDKCacheKey):void
     arg types: [boolean, dji.sdksharedlib.keycatalog.DJISDKCacheKey]
     candidates:
      dji.sdksharedlib.hardware.abstractions.DJISDKCacheHWAbstraction.notifyValueChangeForKeyPath(java.lang.Object, java.lang.String):void
      dji.sdksharedlib.hardware.abstractions.DJISDKCacheHWAbstraction.notifyValueChangeForKeyPath(java.lang.Object, dji.sdksharedlib.keycatalog.DJISDKCacheKey):void */
    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEvent3BackgroundThread(DataCameraGetPushRawParams pushInfo) {
        super.onEvent3BackgroundThread(pushInfo);
        if (pushInfo.isGetted() && isValidSender(pushInfo.getSenderId())) {
            notifyValueChangeForKeyPath(Boolean.valueOf(pushInfo.isVideoSyncEnable()), convertKeyToPath(CameraKeys.SSD_VIDEO_SYNC));
            notifyValueChangeForKeyPath(Integer.valueOf(pushInfo.getSsdAccumulativeData()), convertKeyToPath(CameraKeys.SSD_ACCUMULATE_DATA));
            notifyValueChangeForKeyPath(Boolean.valueOf(pushInfo.isDiskConnected()), convertKeyToPath(CameraKeys.DISKCONNECTED));
            DataCameraGetPushRawParams.RawMode rawMode = pushInfo.getRawMode();
            notifyValueChangeForKeyPath(SSDRawMode.find(rawMode.value()), convertKeyToPath(CameraKeys.SSD_RAW_MODE));
            if (rawMode == DataCameraGetPushRawParams.RawMode.ProrseOFF) {
                notifyValueChangeForKeyPath((Object) false, convertKeyToPath(CameraKeys.SSD_VIDEO_RECORDING_ENABLED));
                return;
            }
            notifyValueChangeForKeyPath(DJICameraEnumMappingUtil.getSDKLicenseFromRAWMode(rawMode), convertKeyToPath(CameraKeys.ACTIVATE_SSD_VIDEO_LICENSE));
            notifyValueChangeForKeyPath((Object) true, convertKeyToPath(CameraKeys.SSD_VIDEO_RECORDING_ENABLED));
        }
    }

    @Setter(CameraKeys.SSD_VIDEO_RESOLUTION_AND_FRAME_RATE)
    public void setSSDRawVideoResolutionAndFrameRate(ResolutionAndFrameRate resolutionAndFrameRate, DJISDKCacheHWAbstraction.InnerCallback callback) {
        if (!isSSDRAWRecordingEnabled()) {
            if (callback != null) {
                callback.onFails(DJICameraError.CANNOT_SET_PARAMETERS_IN_THIS_STATE);
            }
        } else if (isSSDResolutionAndFrameRateValid(resolutionAndFrameRate) || callback == null) {
            int ssdFPS = DJICameraEnumMappingUtil.getFrameRateProtocolValue(resolutionAndFrameRate.getFrameRate());
            SettingsDefinitions.VideoResolution sdResolution = DJICameraEnumMappingUtil.getAvailableSDResolutionFromSSD(resolutionAndFrameRate, CameraParamRangeManager.getVideoResolutionAndFrameRateRange(this.index));
            int ssdRes = DJICameraEnumMappingUtil.getResolutionProtocolValue(resolutionAndFrameRate.getResolution());
            DataCameraSetRawVideoFormat setter = new DataCameraSetRawVideoFormat();
            setter.setReceiverId(getReceiverIdByIndex());
            setter.setRawVideoFrameRate(ssdFPS).setRawVideoResolution(ssdRes);
            if (sdResolution != null) {
                setter.setVideoFrameRate(ssdFPS).setVideoResolution(DJICameraEnumMappingUtil.getResolutionProtocolValue(sdResolution));
            }
            setter.start(CallbackUtils.defaultCB(callback));
        } else {
            callback.onFails(DJICameraError.COMMON_PARAM_ILLEGAL);
        }
    }

    @Setter(CameraKeys.SSD_VIDEO_RECORDING_ENABLED)
    public void setSSDRAWRecordingEnabled(boolean enabled, DJISDKCacheHWAbstraction.InnerCallback callback) {
        ResolutionAndFrameRate value = getSDCardResolutionAndFrameRate();
        if (value != null) {
            int sdCardFPS = DJICameraEnumMappingUtil.getFrameRateProtocolValue(value.getFrameRate());
            setSSDRawResolutionAndFrameRateProtocol((enabled ? DataCameraGetPushRawParams.RawMode.JPEGLossLess : DataCameraGetPushRawParams.RawMode.ProrseOFF).value(), DJICameraEnumMappingUtil.getResolutionProtocolValue(value.getResolution()), sdCardFPS, DJICameraEnumMappingUtil.getResolutionProtocolValue(SettingsDefinitions.VideoResolution.NO_SSD_VIDEO), sdCardFPS, callback);
            return;
        }
        CallbackUtils.onFailure(callback, DJIError.COMMON_EXECUTION_FAILED);
    }

    @Setter(CameraKeys.ACTIVATE_SSD_VIDEO_LICENSE)
    public void setSSDRAWLicense(CameraSSDVideoLicense license, DJISDKCacheHWAbstraction.InnerCallback callback) {
        if (!isSSDRAWRecordingEnabled()) {
            CallbackUtils.onFailure(callback, DJICameraError.CANNOT_SET_PARAMETERS_IN_THIS_STATE);
        } else if (license == CameraSSDVideoLicense.Unknown) {
            CallbackUtils.onFailure(callback, DJICameraError.COMMON_PARAM_ILLEGAL);
        } else {
            DataCameraGetPushRawParams.RawMode rawMode = DJICameraEnumMappingUtil.getRAWModeFromSDKLicense(license);
            DataCameraSetRawVideoFormat setter = new DataCameraSetRawVideoFormat();
            setter.setReceiverId(getReceiverIdByIndex());
            setter.setRawMode(rawMode.value()).start(CallbackUtils.defaultCB(callback));
        }
    }

    /* access modifiers changed from: protected */
    public void trigerLensChanged() {
        super.trigerLensChanged();
        this.handler.postDelayed(new Runnable() {
            /* class dji.sdksharedlib.hardware.abstractions.camera.zenmuse.DJICameraInspire2RawAbstraction.AnonymousClass1 */

            public void run() {
                DJICameraInspire2RawAbstraction.this.getLensInformation(new DJISDKCacheHWAbstraction.InnerCallback() {
                    /* class dji.sdksharedlib.hardware.abstractions.camera.zenmuse.DJICameraInspire2RawAbstraction.AnonymousClass1.AnonymousClass1 */

                    public void onSuccess(Object o) {
                    }

                    public void onFails(DJIError error) {
                    }
                });
            }
        }, 1000);
    }

    private ResolutionAndFrameRate getSDCardResolutionAndFrameRate() {
        DJISDKCacheParamValue value = DJISDKCache.getInstance().getAvailableValue(KeyHelper.getCameraKey(this.index, CameraKeys.RESOLUTION_FRAME_RATE));
        if (value == null || value.getData() == null) {
            return null;
        }
        return (ResolutionAndFrameRate) value.getData();
    }

    private boolean isSSDRAWRecordingEnabled() {
        DJISDKCacheParamValue value = DJISDKCache.getInstance().getAvailableValue(KeyHelper.getCameraKey(this.index, CameraKeys.SSD_VIDEO_RECORDING_ENABLED));
        if (value == null || value.getData() == null) {
            return false;
        }
        return ((Boolean) value.getData()).booleanValue();
    }

    private boolean isSSDInserted() {
        DJISDKCacheParamValue value = DJISDKCache.getInstance().getAvailableValue(KeyHelper.getCameraKey(this.index, CameraKeys.SSD_IS_CONNECTED));
        if (value == null || value.getData() == null) {
            return false;
        }
        return ((Boolean) value.getData()).booleanValue();
    }

    /* access modifiers changed from: protected */
    public ArrayList<SettingsDefinitions.VideoResolution> rangeWithCameraType(DataCameraGetPushRawParams.RawMode rawMode, int fps) {
        ArrayList<SettingsDefinitions.VideoResolution> resolutionRange = new ArrayList<>();
        if (DataCameraGetPushStateInfo.getInstance().getCameraType(getExpectedSenderIdByIndex()) == DataCameraGetPushStateInfo.CameraType.DJICameraTypeFC6520) {
            switch (rawMode) {
                case JPEGLossLess:
                    if (fps > DJICameraEnumMappingUtil.getFrameRateProtocolValue(SettingsDefinitions.VideoFrameRate.FRAME_RATE_29_DOT_970_FPS) && fps != DJICameraEnumMappingUtil.getFrameRateProtocolValue(SettingsDefinitions.VideoFrameRate.FRAME_RATE_24_FPS) && fps != DJICameraEnumMappingUtil.getFrameRateProtocolValue(SettingsDefinitions.VideoFrameRate.FRAME_RATE_30_FPS)) {
                        if (fps <= DJICameraEnumMappingUtil.getFrameRateProtocolValue(SettingsDefinitions.VideoFrameRate.FRAME_RATE_59_DOT_940_FPS) || fps == DJICameraEnumMappingUtil.getFrameRateProtocolValue(SettingsDefinitions.VideoFrameRate.FRAME_RATE_48_FPS) || fps == DJICameraEnumMappingUtil.getFrameRateProtocolValue(SettingsDefinitions.VideoFrameRate.FRAME_RATE_60_FPS)) {
                            resolutionRange.add(SettingsDefinitions.VideoResolution.RESOLUTION_3840x2160);
                            resolutionRange.add(SettingsDefinitions.VideoResolution.RESOLUTION_4096x2160);
                            break;
                        }
                    } else {
                        resolutionRange.add(SettingsDefinitions.VideoResolution.RESOLUTION_3840x2160);
                        resolutionRange.add(SettingsDefinitions.VideoResolution.RESOLUTION_4096x2160);
                        resolutionRange.add(SettingsDefinitions.VideoResolution.RESOLUTION_MAX);
                        break;
                    }
                    break;
                case ProresHQ422:
                    if (fps <= DJICameraEnumMappingUtil.getFrameRateProtocolValue(SettingsDefinitions.VideoFrameRate.FRAME_RATE_29_DOT_970_FPS) || fps == DJICameraEnumMappingUtil.getFrameRateProtocolValue(SettingsDefinitions.VideoFrameRate.FRAME_RATE_24_FPS) || fps == DJICameraEnumMappingUtil.getFrameRateProtocolValue(SettingsDefinitions.VideoFrameRate.FRAME_RATE_30_FPS)) {
                        resolutionRange.add(SettingsDefinitions.VideoResolution.RESOLUTION_3840x2160);
                        resolutionRange.add(SettingsDefinitions.VideoResolution.RESOLUTION_5280x2160);
                        break;
                    }
                case ProresHQ444:
                    if (fps <= DJICameraEnumMappingUtil.getFrameRateProtocolValue(SettingsDefinitions.VideoFrameRate.FRAME_RATE_29_DOT_970_FPS) || fps == DJICameraEnumMappingUtil.getFrameRateProtocolValue(SettingsDefinitions.VideoFrameRate.FRAME_RATE_24_FPS) || fps == DJICameraEnumMappingUtil.getFrameRateProtocolValue(SettingsDefinitions.VideoFrameRate.FRAME_RATE_30_FPS)) {
                        resolutionRange.add(SettingsDefinitions.VideoResolution.RESOLUTION_3840x2160);
                        break;
                    }
            }
            resolutionRange.add(SettingsDefinitions.VideoResolution.NO_SSD_VIDEO);
        }
        return resolutionRange;
    }

    private void setSSDRawResolutionAndFrameRateProtocol(int rawMode, int sdCardRes, int sdCardFPS, int ssdRes, int ssdFPS, final DJISDKCacheHWAbstraction.InnerCallback callback) {
        DataCameraSetRawVideoFormat setter = new DataCameraSetRawVideoFormat();
        setter.setReceiverId(getReceiverIdByIndex());
        setter.setRawMode(rawMode).setVideoResolution(sdCardRes).setVideoFrameRate(sdCardFPS).setRawVideoResolution(ssdRes).setRawVideoFrameRate(ssdFPS).start(new DJIDataCallBack() {
            /* class dji.sdksharedlib.hardware.abstractions.camera.zenmuse.DJICameraInspire2RawAbstraction.AnonymousClass2 */

            public void onSuccess(Object model) {
                CallbackUtils.onSuccess(callback, (Object) null);
            }

            public void onFailure(Ccode ccode) {
                CallbackUtils.onFailure(callback, ccode);
            }
        });
    }

    @Action(CameraKeys.FORMAT_SSD_WITH_TYPE)
    public void formatSSDWithType(final DJISDKCacheHWAbstraction.InnerCallback callback, SSDFileSystem fileSystem) {
        if (!isSSDSupported()) {
            if (callback != null) {
                callback.onFails(DJICameraError.COMMON_UNSUPPORTED);
            }
        } else if (DataCameraGetPushStateInfo.getInstance().getWm620CameraProtocolVersion(getExpectedSenderIdByIndex()) < 2 || DataCameraGetPushRawParams.getInstance().getRawProtocolVersion(getExpectedSenderIdByIndex()) < 3 || fileSystem == null) {
            super.formatSSD(callback);
        } else {
            DataCameraFormatSSD setter = DataCameraFormatSSD.getInstance();
            setter.setReceiverId(getReceiverIdByIndex());
            setter.setFormatSpeed(0).setFileSystem(fileSystem.value()).start(new DJIDataCallBack() {
                /* class dji.sdksharedlib.hardware.abstractions.camera.zenmuse.DJICameraInspire2RawAbstraction.AnonymousClass3 */

                public void onSuccess(Object model) {
                    if (callback != null) {
                        callback.onSuccess(null);
                    }
                }

                public void onFailure(Ccode ccode) {
                    if (callback != null) {
                        callback.onFails(DJICameraError.getDJIError(ccode));
                    }
                }
            });
        }
    }

    @Setter(CameraKeys.SSD_VIDEO_SYNC)
    public void syncSSDVideo(boolean enable, final DJISDKCacheHWAbstraction.InnerCallback callback) {
        if (isSSDSupported() && DataCameraGetPushStateInfo.getInstance().getWm620CameraProtocolVersion(getExpectedSenderIdByIndex()) >= Integer.MAX_VALUE) {
            ((DataCameraSetVideoSync) new DataCameraSetVideoSync().setReceiverId(getReceiverIdByIndex(), DataCameraSetVideoSync.class)).setEnable(enable).start(new DJIDataCallBack() {
                /* class dji.sdksharedlib.hardware.abstractions.camera.zenmuse.DJICameraInspire2RawAbstraction.AnonymousClass4 */

                public void onSuccess(Object model) {
                    if (callback != null) {
                        callback.onSuccess(null);
                    }
                }

                public void onFailure(Ccode ccode) {
                    if (callback != null) {
                        callback.onFails(DJICameraError.getDJIError(ccode));
                    }
                }
            });
        } else if (callback != null) {
            callback.onFails(DJICameraError.COMMON_UNSUPPORTED);
        }
    }
}
