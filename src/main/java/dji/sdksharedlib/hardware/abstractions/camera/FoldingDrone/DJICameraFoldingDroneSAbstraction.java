package dji.sdksharedlib.hardware.abstractions.camera.FoldingDrone;

import dji.common.camera.SettingsDefinitions;
import dji.common.error.DJICameraError;
import dji.common.util.CallbackUtils;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.config.P3.Ccode;
import dji.midware.data.model.P3.DataCameraGetCameraRotationMode;
import dji.midware.data.model.P3.DataCameraGetPushStateInfo;
import dji.midware.data.model.P3.DataCameraGetVOutParams;
import dji.midware.data.model.P3.DataCameraSetCameraRotationMode;
import dji.midware.data.model.P3.DataCameraSetVOutParams;
import dji.midware.interfaces.DJIDataCallBack;
import dji.sdksharedlib.hardware.abstractions.DJISDKCacheHWAbstraction;
import dji.sdksharedlib.hardware.abstractions.Getter;
import dji.sdksharedlib.hardware.abstractions.Setter;
import dji.sdksharedlib.hardware.abstractions.camera.DJICameraBaseAbstraction;
import dji.sdksharedlib.keycatalog.CameraKeys;

@EXClassNullAway
public class DJICameraFoldingDroneSAbstraction extends DJICameraBaseAbstraction {
    /* access modifiers changed from: protected */
    public boolean isMediaDownloadModeSupported() {
        return true;
    }

    /* access modifiers changed from: protected */
    public boolean isVideoPlaybackSupported() {
        return true;
    }

    /* access modifiers changed from: protected */
    public boolean isNewProcessOfActivation() {
        return true;
    }

    /* access modifiers changed from: protected */
    public boolean isDigitalZoomScaleSupported() {
        return true;
    }

    @Setter(CameraKeys.ORIENTATION)
    public void setOrientation(SettingsDefinitions.Orientation mode, final DJISDKCacheHWAbstraction.InnerCallback callback) {
        ((DataCameraSetCameraRotationMode) DataCameraSetCameraRotationMode.getInstance().setReceiverId(getExpectedSenderIdByIndex(), DataCameraSetCameraRotationMode.class)).setImageOrientationMode(0).setOrientation(DataCameraSetCameraRotationMode.RotationAngleType.find(mode.value())).start(new DJIDataCallBack() {
            /* class dji.sdksharedlib.hardware.abstractions.camera.FoldingDrone.DJICameraFoldingDroneSAbstraction.AnonymousClass1 */

            public void onSuccess(Object model) {
                CallbackUtils.onSuccess(callback, (Object) null);
            }

            public void onFailure(Ccode ccode) {
                CallbackUtils.onFailure(callback, ccode);
            }
        });
    }

    @Getter(CameraKeys.ORIENTATION)
    public void getOrientation(final DJISDKCacheHWAbstraction.InnerCallback callback) {
        ((DataCameraGetCameraRotationMode) DataCameraGetCameraRotationMode.getInstance().setReceiverId(getExpectedSenderIdByIndex(), DataCameraGetCameraRotationMode.class)).start(new DJIDataCallBack() {
            /* class dji.sdksharedlib.hardware.abstractions.camera.FoldingDrone.DJICameraFoldingDroneSAbstraction.AnonymousClass2 */

            public void onSuccess(Object model) {
                CallbackUtils.onSuccess(callback, SettingsDefinitions.Orientation.values()[DataCameraGetCameraRotationMode.getInstance().getRotationAngleType().value()]);
            }

            public void onFailure(Ccode ccode) {
                CallbackUtils.onFailure(callback, ccode);
            }
        });
    }

    /* access modifiers changed from: protected */
    public boolean checkTrueColorDigitalFilterSupported() {
        return true;
    }

    @Setter(CameraKeys.STREAM_QUALITY)
    public void setStreamQuality(DataCameraSetVOutParams.LCDFormat format, final DJISDKCacheHWAbstraction.InnerCallback callback) {
        if (DataCameraGetPushStateInfo.getInstance().beInTrackingMode(getExpectedSenderIdByIndex())) {
            CallbackUtils.onFailure(callback, DJICameraError.COMMON_SYSTEM_BUSY);
        } else {
            ((DataCameraSetVOutParams) new DataCameraSetVOutParams().setReceiverId(getReceiverIdByIndex(), DataCameraSetVOutParams.class)).setIsSettingLCD(true).setLCDFormat(format).start(new DJIDataCallBack() {
                /* class dji.sdksharedlib.hardware.abstractions.camera.FoldingDrone.DJICameraFoldingDroneSAbstraction.AnonymousClass3 */

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

    @Getter(CameraKeys.STREAM_QUALITY)
    public void getStreamQuality(final DJISDKCacheHWAbstraction.InnerCallback callback) {
        final DataCameraGetVOutParams getter = new DataCameraGetVOutParams();
        getter.setReceiverId(getReceiverIdByIndex());
        getter.start(new DJIDataCallBack() {
            /* class dji.sdksharedlib.hardware.abstractions.camera.FoldingDrone.DJICameraFoldingDroneSAbstraction.AnonymousClass4 */

            public void onSuccess(Object model) {
                CallbackUtils.onSuccess(callback, getter.getLCDFormat(DJICameraFoldingDroneSAbstraction.this.getExpectedSenderIdByIndex()));
            }

            public void onFailure(Ccode ccode) {
                callback.onFails(DJICameraError.getDJIError(ccode));
            }
        });
    }
}
