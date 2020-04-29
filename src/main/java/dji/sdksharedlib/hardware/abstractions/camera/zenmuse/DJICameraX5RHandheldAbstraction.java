package dji.sdksharedlib.hardware.abstractions.camera.zenmuse;

import dji.common.camera.ResolutionAndFrameRate;
import dji.common.error.DJICameraError;
import dji.common.error.DJIError;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.config.P3.Ccode;
import dji.midware.data.model.P3.DataCameraSetOpticsZoomMode;
import dji.midware.interfaces.DJIDataCallBack;
import dji.sdksharedlib.DJISDKCache;
import dji.sdksharedlib.hardware.abstractions.DJISDKCacheHWAbstraction;
import dji.sdksharedlib.hardware.abstractions.camera.DJICameraAbstraction;
import dji.sdksharedlib.keycatalog.CameraKeys;
import dji.sdksharedlib.store.DJISDKCacheParamValue;
import java.nio.ByteBuffer;

@EXClassNullAway
public class DJICameraX5RHandheldAbstraction extends DJICameraX5BaseAbstraction {
    private static String TAG = "X5RHandHeldCamera";

    /* access modifiers changed from: protected */
    public void initializeComponentCharacteristics() {
        addCharacteristics(CameraKeys.class, getClass());
    }

    /* access modifiers changed from: protected */
    public boolean isPlaybackSupported() {
        return true;
    }

    /* access modifiers changed from: protected */
    public boolean isMediaDownloadModeSupported() {
        return true;
    }

    /* access modifiers changed from: protected */
    public boolean isAudioRecordSupported() {
        return true;
    }

    /* access modifiers changed from: protected */
    public boolean isHandHeldProduct() {
        return true;
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
    public String getDisplayName() {
        return DJICameraAbstraction.DisplayNameX5R;
    }

    /* access modifiers changed from: protected */
    public void doSetOpticalZoomFocalLength(int length, final DJISDKCacheHWAbstraction.InnerCallback callback) {
        byte[] bytes = ByteBuffer.allocate(4).putInt(length).array();
        Byte lowerByte = Byte.valueOf(bytes[3]);
        Byte higherByte = Byte.valueOf(bytes[2]);
        DataCameraSetOpticsZoomMode setter = new DataCameraSetOpticsZoomMode();
        setter.setReceiverId(getReceiverIdByIndex());
        setter.setOpticsZoomMode(DataCameraSetOpticsZoomMode.OpticsZommMode.SETZOOM, DataCameraSetOpticsZoomMode.ZoomSpeed.MID, lowerByte.intValue(), higherByte.intValue()).start(new DJIDataCallBack() {
            /* class dji.sdksharedlib.hardware.abstractions.camera.zenmuse.DJICameraX5RHandheldAbstraction.AnonymousClass1 */

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

    public void setVideoResolutionAndFrameRate(final ResolutionAndFrameRate resolutionAndFrameRate, final DJISDKCacheHWAbstraction.InnerCallback callback) {
        if (isResolutionAndFrameRateValid(resolutionAndFrameRate) || callback == null) {
            super.setVideoResolutionAndFrameRate(resolutionAndFrameRate, new DJISDKCacheHWAbstraction.InnerCallback() {
                /* class dji.sdksharedlib.hardware.abstractions.camera.zenmuse.DJICameraX5RHandheldAbstraction.AnonymousClass2 */

                public void onSuccess(Object o) {
                    DJICameraX5RHandheldAbstraction.this.setSSDRawVideoResolutionAndFrameRate(resolutionAndFrameRate, callback);
                }

                public void onFails(DJIError error) {
                    if (callback != null) {
                        callback.onFails(error);
                    }
                }
            });
        } else {
            callback.onFails(DJICameraError.COMMON_PARAM_ILLEGAL);
        }
    }

    private boolean isResolutionAndFrameRateValid(ResolutionAndFrameRate value) {
        DJISDKCacheParamValue rangeValue = DJISDKCache.getInstance().getAvailableValue(convertKeyToPath(CameraKeys.VIDEO_RESOLUTION_FRAME_RATE_RANGE));
        if (!(rangeValue == null || rangeValue.getData() == null)) {
            for (ResolutionAndFrameRate resolutionAndFrameRate : (ResolutionAndFrameRate[]) rangeValue.getData()) {
                if (resolutionAndFrameRate.equals(value)) {
                    return true;
                }
            }
        }
        return false;
    }
}
