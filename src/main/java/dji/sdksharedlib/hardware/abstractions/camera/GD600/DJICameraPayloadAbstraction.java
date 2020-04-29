package dji.sdksharedlib.hardware.abstractions.camera.GD600;

import dji.common.camera.SettingsDefinitions;
import dji.common.error.DJICameraError;
import dji.common.error.DJIError;
import dji.common.util.CallbackUtils;
import dji.fieldAnnotation.EXClassNullAway;
import dji.log.DJILog;
import dji.midware.data.config.P3.Ccode;
import dji.midware.data.model.P3.DataCameraSetFocusParam;
import dji.midware.data.model.P3.DataPayloadGetCameraFeatureState;
import dji.midware.data.model.P3.DataSingleSendAppStateForStabilization;
import dji.midware.interfaces.DJIDataCallBack;
import dji.sdksharedlib.hardware.abstractions.DJISDKCacheHWAbstraction;
import dji.sdksharedlib.hardware.abstractions.camera.DJICameraAbstraction;
import dji.sdksharedlib.keycatalog.CameraKeys;

@EXClassNullAway
public class DJICameraPayloadAbstraction extends DJICameraGD600Abstraction {
    private static final int MAX_RETRY_TIMES = 8;
    private static final int PROTECTION_PERIOD_IN_MILISECOND = 1000;
    private static final String TAG = "DJICameraPayloadAbstraction";
    /* access modifiers changed from: private */
    public boolean adjustableFocalPointSupported = false;
    /* access modifiers changed from: private */
    public boolean digitalZoomSupported = false;
    private long lastUpdateTime = 0;
    /* access modifiers changed from: private */
    public boolean meteringSupported = false;
    /* access modifiers changed from: private */
    public boolean opticalZoomSupported = false;
    /* access modifiers changed from: private */
    public int retry = 0;

    static /* synthetic */ int access$1008(DJICameraPayloadAbstraction x0) {
        int i = x0.retry;
        x0.retry = i + 1;
        return i;
    }

    public void destroy() {
        super.destroy();
        this.retry = 0;
    }

    /* access modifiers changed from: protected */
    public void initializeCustomizedKey() {
        super.initializeCustomizedKey();
        updateSomeSupportedKey();
    }

    /* access modifiers changed from: protected */
    public boolean isVideoPlaybackSupported() {
        return false;
    }

    /* access modifiers changed from: protected */
    public boolean isPlaybackSupported() {
        return false;
    }

    /* access modifiers changed from: protected */
    public boolean isMediaDownloadModeSupported() {
        return false;
    }

    /* access modifiers changed from: protected */
    public boolean isTapZoomSupported() {
        return false;
    }

    /* access modifiers changed from: protected */
    public String getDisplayName() {
        return DJICameraAbstraction.DisplayNamePayload;
    }

    public void setCameraMode(SettingsDefinitions.CameraMode mode, DJISDKCacheHWAbstraction.InnerCallback callback) {
        if (SettingsDefinitions.CameraMode.RECORD_VIDEO.equals(mode) || SettingsDefinitions.CameraMode.SHOOT_PHOTO.equals(mode)) {
            super.setCameraMode(mode, callback);
        } else {
            CallbackUtils.onFailure(callback, DJIError.COMMON_PARAM_ILLEGAL);
        }
    }

    /* access modifiers changed from: private */
    public void updateSomeSupportedKey() {
        long now = System.currentTimeMillis();
        if (now - this.lastUpdateTime >= 1000) {
            this.lastUpdateTime = now;
            final DataPayloadGetCameraFeatureState getter = new DataPayloadGetCameraFeatureState();
            getter.setReceiverId(getReceiverIdByIndex());
            getter.start(new DJIDataCallBack() {
                /* class dji.sdksharedlib.hardware.abstractions.camera.GD600.DJICameraPayloadAbstraction.AnonymousClass1 */

                public void onSuccess(Object model) {
                    boolean unused = DJICameraPayloadAbstraction.this.digitalZoomSupported = getter.isDigitalZoomSupported();
                    boolean unused2 = DJICameraPayloadAbstraction.this.opticalZoomSupported = getter.isOpticalZoomSupported();
                    boolean unused3 = DJICameraPayloadAbstraction.this.adjustableFocalPointSupported = getter.isAdjustableFocalPointSupported();
                    boolean unused4 = DJICameraPayloadAbstraction.this.meteringSupported = getter.isMeteringSupported();
                    DJICameraPayloadAbstraction.this.notifyValueChangeForKeyPath(Boolean.valueOf(DJICameraPayloadAbstraction.this.digitalZoomSupported), CameraKeys.IS_DIGITAL_ZOOM_SUPPORTED);
                    DJICameraPayloadAbstraction.this.notifyValueChangeForKeyPath(Boolean.valueOf(DJICameraPayloadAbstraction.this.opticalZoomSupported), CameraKeys.IS_OPTICAL_ZOOM_SUPPORTED);
                    DJICameraPayloadAbstraction.this.notifyValueChangeForKeyPath(Boolean.valueOf(DJICameraPayloadAbstraction.this.adjustableFocalPointSupported), CameraKeys.IS_ADJUSTABLE_FOCAL_POINT_SUPPORTED);
                    DJICameraPayloadAbstraction.this.notifyValueChangeForKeyPath(Boolean.valueOf(DJICameraPayloadAbstraction.this.meteringSupported), CameraKeys.IS_METERING_MODE_SUPPORTED);
                    if (!DJICameraPayloadAbstraction.this.meteringSupported) {
                        DJICameraPayloadAbstraction.this.notifyValueChangeForKeyPath(SettingsDefinitions.MeteringMode.UNKNOWN, DJICameraPayloadAbstraction.this.convertKeyToPath(CameraKeys.METERING_MODE));
                    }
                    int unused5 = DJICameraPayloadAbstraction.this.retry = 0;
                }

                public void onFailure(Ccode ccode) {
                    DJICameraPayloadAbstraction.access$1008(DJICameraPayloadAbstraction.this);
                    if (DJICameraPayloadAbstraction.this.retry < 8) {
                        DJICameraPayloadAbstraction.this.handler.postDelayed(new Runnable() {
                            /* class dji.sdksharedlib.hardware.abstractions.camera.GD600.DJICameraPayloadAbstraction.AnonymousClass1.C00201 */

                            public void run() {
                                DJICameraPayloadAbstraction.this.updateSomeSupportedKey();
                            }
                        }, 1000);
                    } else {
                        DJILog.e(DJICameraPayloadAbstraction.TAG, "update support key failed! retry=" + DJICameraPayloadAbstraction.this.retry, new Object[0]);
                    }
                }
            });
        }
    }

    /* access modifiers changed from: protected */
    public boolean isBurstCountSupported(SettingsDefinitions.PhotoBurstCount count) {
        return count == SettingsDefinitions.PhotoBurstCount.BURST_COUNT_2 || count == SettingsDefinitions.PhotoBurstCount.BURST_COUNT_3 || count == SettingsDefinitions.PhotoBurstCount.BURST_COUNT_5 || count == SettingsDefinitions.PhotoBurstCount.BURST_COUNT_7 || count == SettingsDefinitions.PhotoBurstCount.BURST_COUNT_10;
    }

    /* access modifiers changed from: protected */
    public boolean isPhotoIntervalParamValid(int interval, int count) {
        boolean isValid = true;
        switch (interval) {
            case 1:
            case 3:
            case 5:
            case 7:
            case 10:
            case 15:
            case 20:
            case 30:
                break;
            default:
                isValid = false;
                break;
        }
        if (count < 1 || count > 254) {
            return false;
        }
        return isValid;
    }

    /* access modifiers changed from: protected */
    public boolean isDigitalZoomScaleSupported() {
        return this.digitalZoomSupported;
    }

    /* access modifiers changed from: protected */
    public boolean isOpticalZoomSupported() {
        return this.opticalZoomSupported;
    }

    /* access modifiers changed from: protected */
    public boolean isOpticalZoomScaleSupported() {
        return this.opticalZoomSupported;
    }

    /* access modifiers changed from: protected */
    public boolean isAdjustableFocalPointSupported() {
        return this.adjustableFocalPointSupported;
    }

    public void setDigitalZoomScale(float scale, final DJISDKCacheHWAbstraction.InnerCallback callback) {
        if (isDigitalZoomScaleSupported()) {
            DJIError preError = checkPrerequisiteForSetDigitalZoomScale();
            if (preError != null) {
                if (callback != null) {
                    callback.onFails(preError);
                }
            } else if (((double) scale) >= 1.0d && ((double) scale) <= 12.0d) {
                checkIfNeedWaitForPauseTheStabilization(callback, DataSingleSendAppStateForStabilization.CameraState.ZOOM_IN);
                DataCameraSetFocusParam setter = DataCameraSetFocusParam.getInstance();
                setter.setReceiverId(getReceiverIdByIndex());
                setter.setDigitalZoom(true).setDigitalMode(DataCameraSetFocusParam.ZoomMode.POSITION).setDigitalPosScale(scale);
                setter.start(new DJIDataCallBack() {
                    /* class dji.sdksharedlib.hardware.abstractions.camera.GD600.DJICameraPayloadAbstraction.AnonymousClass2 */

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
                callback.onFails(DJIError.COMMON_PARAM_ILLEGAL);
            }
        } else if (callback != null) {
            callback.onFails(DJICameraError.COMMON_UNSUPPORTED);
        }
    }
}
