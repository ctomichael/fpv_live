package dji.sdksharedlib.hardware.abstractions.camera.zenmuse;

import dji.common.error.DJICameraError;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.config.P3.Ccode;
import dji.midware.data.model.P3.DataCameraSetOpticsZoomMode;
import dji.midware.interfaces.DJIDataCallBack;
import dji.sdksharedlib.hardware.abstractions.DJISDKCacheHWAbstraction;
import dji.sdksharedlib.hardware.abstractions.camera.DJICameraAbstraction;
import dji.sdksharedlib.keycatalog.CameraKeys;
import java.nio.ByteBuffer;

@EXClassNullAway
public class DJICameraX5HandheldAbstraction extends DJICameraX5BaseAbstraction {
    private DataCameraSetOpticsZoomMode.ZoomSpeed speedCache = null;

    /* access modifiers changed from: protected */
    public void initializeComponentCharacteristics() {
        addCharacteristics(CameraKeys.class, getClass());
    }

    /* access modifiers changed from: protected */
    public boolean isMediaDownloadModeSupported() {
        return true;
    }

    /* access modifiers changed from: protected */
    public boolean isHandHeldProduct() {
        return true;
    }

    /* access modifiers changed from: protected */
    public boolean isHDRPhotoSupported() {
        return false;
    }

    /* access modifiers changed from: protected */
    public boolean isTimeLapseSupported() {
        return false;
    }

    /* access modifiers changed from: protected */
    public boolean isSlowMotionSupported() {
        return false;
    }

    /* access modifiers changed from: protected */
    public boolean isAudioRecordSupported() {
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
        return DJICameraAbstraction.DisplayNameX5;
    }

    /* access modifiers changed from: protected */
    public void doSetOpticalZoomFocalLength(int length, final DJISDKCacheHWAbstraction.InnerCallback callback) {
        byte[] bytes = ByteBuffer.allocate(4).putInt(length).array();
        Byte lowerByte = Byte.valueOf(bytes[3]);
        Byte higherByte = Byte.valueOf(bytes[2]);
        DataCameraSetOpticsZoomMode setter = new DataCameraSetOpticsZoomMode();
        setter.setReceiverId(getReceiverIdByIndex());
        setter.setOpticsZoomMode(DataCameraSetOpticsZoomMode.OpticsZommMode.SETZOOM, DataCameraSetOpticsZoomMode.ZoomSpeed.MID, lowerByte.intValue(), higherByte.intValue()).start(new DJIDataCallBack() {
            /* class dji.sdksharedlib.hardware.abstractions.camera.zenmuse.DJICameraX5HandheldAbstraction.AnonymousClass1 */

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
