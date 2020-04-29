package dji.sdksharedlib.hardware.abstractions.camera.GD600;

import dji.common.camera.SettingsDefinitions;
import dji.common.error.DJICameraError;
import dji.common.error.DJIError;
import dji.common.util.CallbackUtils;
import dji.midware.data.config.P3.Ccode;
import dji.midware.data.model.P3.DataBaseCameraSetting;
import dji.midware.data.model.P3.DataCameraGetImageSize;
import dji.midware.data.model.P3.DataCameraGetSensorID;
import dji.midware.data.model.P3.DataCameraSetImageSize;
import dji.midware.interfaces.DJIDataCallBack;
import dji.sdksharedlib.extension.CacheHelper;
import dji.sdksharedlib.hardware.abstractions.DJISDKCacheHWAbstraction;
import dji.sdksharedlib.hardware.abstractions.Getter;
import dji.sdksharedlib.hardware.abstractions.Setter;
import dji.sdksharedlib.hardware.abstractions.camera.DJICameraAbstraction;
import dji.sdksharedlib.keycatalog.CameraKeys;
import dji.sdksharedlib.keycatalog.DJISDKCacheKeys;

public class DJICameraWM245DualLightVLAbstraction extends DJICameraXT2Abstraction {
    private static final int PHOTO_COUNT_MIN = 1;

    /* access modifiers changed from: protected */
    public boolean isInternalStorageSupported() {
        return true;
    }

    /* access modifiers changed from: protected */
    public String getDisplayName() {
        return DJICameraAbstraction.DisplayNameMavic2EnterpriseDual_VL;
    }

    /* access modifiers changed from: protected */
    public int getProtocolVersion() {
        return 0;
    }

    /* access modifiers changed from: protected */
    public int getSpotMeterCol() {
        return 15;
    }

    /* access modifiers changed from: protected */
    public int getSpotMeterRow() {
        return 15;
    }

    @Setter(CameraKeys.PHOTO_ASPECT_RATIO)
    public void setPhotoRatio(SettingsDefinitions.PhotoAspectRatio photoAspectRatio, final DJISDKCacheHWAbstraction.InnerCallback callback) {
        DataCameraGetImageSize.SizeType sizeType = DataCameraGetImageSize.SizeType.DEFAULT;
        SettingsDefinitions.PhotoAspectRatio[] range = (SettingsDefinitions.PhotoAspectRatio[]) CacheHelper.getCamera(this.index, CameraKeys.PHOTO_ASPECT_RATIO_RANGE);
        boolean isValid = false;
        if (range != null) {
            int length = range.length;
            int i = 0;
            while (true) {
                if (i >= length) {
                    break;
                } else if (range[i] == photoAspectRatio) {
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
        DataCameraGetImageSize.RatioType ratioType = DataCameraGetImageSize.RatioType.find(photoAspectRatio.value());
        DataCameraSetImageSize setter = DataCameraSetImageSize.getInstance();
        setter.setReceiverId(getReceiverIdByIndex());
        setter.setSize(sizeType).setRatio(ratioType).start(new DJIDataCallBack() {
            /* class dji.sdksharedlib.hardware.abstractions.camera.GD600.DJICameraWM245DualLightVLAbstraction.AnonymousClass1 */

            public void onSuccess(Object model) {
                CallbackUtils.onSuccess(callback, model);
            }

            public void onFailure(Ccode ccode) {
                CallbackUtils.onFailure(callback, DJICameraError.getDJIError(ccode));
            }
        });
    }

    @Setter(CameraKeys.PHOTO_FILE_FORMAT)
    public void setPhotoFileFormat(SettingsDefinitions.PhotoFileFormat photoFormat, DJISDKCacheHWAbstraction.InnerCallback callback) {
        if (photoFormat != SettingsDefinitions.PhotoFileFormat.UNKNOWN) {
            SettingsDefinitions.PhotoFileFormat[] range = (SettingsDefinitions.PhotoFileFormat[]) CacheHelper.getCamera(this.index, CameraKeys.PHOTO_FILE_FORMAT_RANGE);
            boolean isValid = false;
            if (range != null) {
                int length = range.length;
                int i = 0;
                while (true) {
                    if (i >= length) {
                        break;
                    } else if (range[i] == photoFormat) {
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
            } else {
                setImageFormat(photoFormat, callback);
            }
        } else if (callback != null) {
            callback.onFails(DJICameraError.INVALID_PARAMETERS);
        }
    }

    @Getter(DJISDKCacheKeys.SERIAL_NUMBER)
    public void getSerialNumber(final DJISDKCacheHWAbstraction.InnerCallback callback) {
        final DataCameraGetSensorID getSensorID = new DataCameraGetSensorID();
        getSensorID.setReceiverId(getReceiverIdByIndex());
        getSensorID.start(new DJIDataCallBack() {
            /* class dji.sdksharedlib.hardware.abstractions.camera.GD600.DJICameraWM245DualLightVLAbstraction.AnonymousClass2 */

            public void onSuccess(Object model) {
                CallbackUtils.onSuccess(callback, getSensorID.getSensorId());
            }

            public void onFailure(Ccode ccode) {
                CallbackUtils.onFailure(callback, DJIError.getDJIError(ccode));
            }
        });
    }

    @Setter(CameraKeys.PHOTO_BURST_COUNT)
    public void setPhotoBurstCount(SettingsDefinitions.PhotoBurstCount count, final DJISDKCacheHWAbstraction.InnerCallback callback) {
        DataBaseCameraSetting setter = new DataBaseCameraSetting();
        setter.setCmdId("Continuous");
        setter.setValue(count.value());
        setter.setReceiverId(getReceiverIdByIndex());
        setter.setPackParam(0, 1);
        setter.start(new DJIDataCallBack() {
            /* class dji.sdksharedlib.hardware.abstractions.camera.GD600.DJICameraWM245DualLightVLAbstraction.AnonymousClass3 */

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

    /* access modifiers changed from: protected */
    public boolean isPhotoIntervalParamValid(int interval, int count) {
        if (count < 1 || count > 255) {
            return false;
        }
        return isIntervalValueSupported(interval);
    }
}
