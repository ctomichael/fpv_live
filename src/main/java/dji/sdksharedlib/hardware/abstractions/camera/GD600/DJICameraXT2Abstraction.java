package dji.sdksharedlib.hardware.abstractions.camera.GD600;

import android.graphics.Point;
import dji.common.camera.ResolutionAndFrameRate;
import dji.common.camera.SettingsDefinitions;
import dji.common.error.DJICameraError;
import dji.common.util.CallbackUtils;
import dji.common.util.DJICameraEnumMappingUtil;
import dji.fieldAnnotation.EXClassNullAway;
import dji.log.DJILog;
import dji.midware.data.config.P3.Ccode;
import dji.midware.data.model.P3.DataBaseCameraGetting;
import dji.midware.data.model.P3.DataBaseCameraSetting;
import dji.midware.data.model.P3.DataCameraGetPushShotParams;
import dji.midware.data.model.P3.DataCameraSetExposureMode;
import dji.midware.data.model.P3.DataCameraSetFocusParam;
import dji.midware.data.model.P3.DataCameraSetMeteringArea;
import dji.midware.data.model.P3.DataCameraSetVideoFormat;
import dji.midware.data.model.P3.DataSingleSendAppStateForStabilization;
import dji.midware.interfaces.DJIDataCallBack;
import dji.sdksharedlib.hardware.abstractions.DJISDKCacheHWAbstraction;
import dji.sdksharedlib.hardware.abstractions.camera.DJICameraAbstraction;

@EXClassNullAway
public class DJICameraXT2Abstraction extends DJICameraGD600Abstraction {
    /* access modifiers changed from: protected */
    public boolean isTapZoomSupported() {
        return false;
    }

    /* access modifiers changed from: protected */
    public boolean isOpticalZoomSupported() {
        return false;
    }

    /* access modifiers changed from: protected */
    public boolean isOpticalZoomScaleSupported() {
        return false;
    }

    /* access modifiers changed from: protected */
    public boolean isDigitalZoomScaleSupported() {
        return false;
    }

    /* access modifiers changed from: protected */
    public String getDisplayName() {
        return DJICameraAbstraction.DisplayNameXT2_VL;
    }

    /* access modifiers changed from: protected */
    public int getSpotMeterCol() {
        return 17;
    }

    /* access modifiers changed from: protected */
    public int getSpotMeterRow() {
        return 15;
    }

    public void getSpotMeteringAreaRowIndexAndColIndex(final DJISDKCacheHWAbstraction.InnerCallback callback) {
        if (callback != null) {
            final DataBaseCameraGetting getter = new DataBaseCameraGetting();
            getter.setCmdId("MeteringArea");
            getter.setReceiverId(getReceiverIdByIndex());
            getter.start(new DJIDataCallBack() {
                /* class dji.sdksharedlib.hardware.abstractions.camera.GD600.DJICameraXT2Abstraction.AnonymousClass1 */

                /* JADX DEBUG: Failed to find minimal casts for resolve overloaded methods, cast all args instead
                 method: dji.log.DJILog.d(java.lang.String, java.lang.String, boolean, boolean):void
                 arg types: [java.lang.String, java.lang.String, int, int]
                 candidates:
                  dji.log.DJILog.d(java.lang.String, java.lang.String, java.lang.Throwable, java.lang.Object[]):void
                  dji.log.DJILog.d(java.lang.String, java.lang.String, boolean, boolean):void */
                public void onSuccess(Object model) {
                    int colIndex;
                    int area = getter.getValue();
                    int colIndex2 = (area + 1) % DJICameraXT2Abstraction.this.getSpotMeterCol();
                    int rowIndex = (area + 1) / DJICameraXT2Abstraction.this.getSpotMeterCol();
                    if (colIndex2 > 0) {
                        colIndex = colIndex2 - 1;
                    } else {
                        colIndex = DJICameraXT2Abstraction.this.getSpotMeterCol() - 1;
                        rowIndex--;
                    }
                    DJILog.d("Version", "checkVersion", true, true);
                    callback.onSuccess(new Point(DJICameraXT2Abstraction.this.checkColIndex(colIndex), rowIndex));
                }

                public void onFailure(Ccode ccode) {
                    if (callback != null) {
                        callback.onFails(DJICameraError.getDJIError(ccode));
                    }
                }
            });
        }
    }

    public void setSpotMeteringAreaRowIndexAndColIndex(Point area, final DJISDKCacheHWAbstraction.InnerCallback callback) {
        if (!DataCameraGetPushShotParams.getInstance().isAELock(getExpectedSenderIdByIndex())) {
            int colIndex = area.x;
            int rowIndex = area.y;
            if (rowIndex < 0 || rowIndex >= getSpotMeterRow()) {
                if (callback != null) {
                    callback.onFails(DJICameraError.COMMON_PARAM_ILLEGAL);
                }
            } else if (colIndex >= 0 && colIndex < getSpotMeterCol()) {
                int areaIndex = (getSpotMeterCol() * rowIndex) + colIndex;
                DataCameraSetMeteringArea setter = DataCameraSetMeteringArea.getInstance();
                setter.setReceiverId(getReceiverIdByIndex());
                setter.setIndex(areaIndex).start(new DJIDataCallBack() {
                    /* class dji.sdksharedlib.hardware.abstractions.camera.GD600.DJICameraXT2Abstraction.AnonymousClass2 */

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
                callback.onFails(DJICameraError.COMMON_PARAM_ILLEGAL);
            }
        } else if (callback != null) {
            callback.onFails(DJICameraError.CANNOT_SET_PARAMETERS_IN_THIS_STATE);
        }
    }

    public void setVideoResolutionAndFrameRate(ResolutionAndFrameRate resolutionAndFrameRate, final DJISDKCacheHWAbstraction.InnerCallback callback) {
        SettingsDefinitions.VideoResolution videoResolution = resolutionAndFrameRate.getResolution();
        SettingsDefinitions.VideoFrameRate videoFrameRate = resolutionAndFrameRate.getFrameRate();
        if (checkIfSupported(videoResolution, videoFrameRate)) {
            int ratio = DJICameraEnumMappingUtil.getResolutionProtocolValue(videoResolution);
            int fps = DJICameraEnumMappingUtil.getFrameRateProtocolValue(videoFrameRate);
            if (ratio != -1 && fps != -1) {
                DataCameraSetVideoFormat setter = new DataCameraSetVideoFormat();
                setter.setAll();
                setter.setRatio(ratio);
                setter.setFps(fps);
                setter.setFov(0);
                setter.setReceiverId(getReceiverIdByIndex());
                setter.start(new DJIDataCallBack() {
                    /* class dji.sdksharedlib.hardware.abstractions.camera.GD600.DJICameraXT2Abstraction.AnonymousClass3 */

                    public void onSuccess(Object model) {
                        CallbackUtils.onSuccess(callback, model);
                    }

                    public void onFailure(Ccode ccode) {
                        CallbackUtils.onFailure(callback, DJICameraError.getDJIError(ccode));
                    }
                });
            } else if (callback != null) {
                callback.onFails(DJICameraError.COMMON_PARAM_ILLEGAL);
            }
        } else if (callback != null) {
            callback.onFails(DJICameraError.COMMON_UNSUPPORTED);
        }
    }

    public void setAntiFlicker(SettingsDefinitions.AntiFlickerFrequency antiFlicker, final DJISDKCacheHWAbstraction.InnerCallback callback) {
        if (DataCameraGetPushShotParams.getInstance().getExposureMode(getExpectedSenderIdByIndex()) != DataCameraSetExposureMode.ExposureMode.P) {
            if (callback != null) {
                callback.onFails(DJICameraError.CANNOT_SET_PARAMETERS_IN_THIS_STATE);
            }
        } else if (antiFlicker != SettingsDefinitions.AntiFlickerFrequency.UNKNOWN) {
            DataBaseCameraSetting setter = new DataBaseCameraSetting();
            setter.setReceiverId(getReceiverIdByIndex());
            setter.setCmdId("AntiFlicker");
            setter.setValue(antiFlicker.value());
            setter.setPackParam(0, 1);
            setter.start(new DJIDataCallBack() {
                /* class dji.sdksharedlib.hardware.abstractions.camera.GD600.DJICameraXT2Abstraction.AnonymousClass4 */

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
            callback.onFails(DJICameraError.INVALID_PARAMETERS);
        }
    }

    public void setDigitalZoomScale(float scale, final DJISDKCacheHWAbstraction.InnerCallback callback) {
        if (scale == 1.0f || scale == 2.0f || scale == 4.0f || scale == 8.0f) {
            int scaleValue = (int) scale;
            if (scaleValue != -1) {
                checkIfNeedWaitForPauseTheStabilization(callback, DataSingleSendAppStateForStabilization.CameraState.ZOOM_IN);
                DataCameraSetFocusParam setter = DataCameraSetFocusParam.getInstance();
                setter.setDigitalZoom(true).setDigitalMode(DataCameraSetFocusParam.ZoomMode.POSITION).setDigitalPosScale((float) scaleValue);
                setter.setReceiverId(getReceiverIdByIndex());
                setter.start(new DJIDataCallBack() {
                    /* class dji.sdksharedlib.hardware.abstractions.camera.GD600.DJICameraXT2Abstraction.AnonymousClass5 */

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
                return;
            }
            callback.onFails(DJICameraError.INVALID_PARAMETERS);
        } else if (callback != null) {
            callback.onFails(DJICameraError.INVALID_PARAMETERS);
        }
    }
}
