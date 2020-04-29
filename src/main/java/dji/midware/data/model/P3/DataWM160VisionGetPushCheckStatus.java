package dji.midware.data.model.P3;

import android.support.annotation.Keep;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.model.base.DJICommonDataBase;

@Keep
@EXClassNullAway
public class DataWM160VisionGetPushCheckStatus extends DJICommonDataBase {
    private DataWM160VisionGetPushCheckStatus() {
    }

    private static class InstanceHolder {
        /* access modifiers changed from: private */
        public static final DataWM160VisionGetPushCheckStatus INSTANCE = new DataWM160VisionGetPushCheckStatus();

        private InstanceHolder() {
        }
    }

    public static DataWM160VisionGetPushCheckStatus getInstance() {
        return InstanceHolder.INSTANCE;
    }

    public boolean downLeftCameraSensorStatus() {
        return (((Integer) get(0, 1, Integer.class)).intValue() & 1) != 0;
    }

    public boolean downRightCameraSensorStatus() {
        return (((Integer) get(0, 1, Integer.class)).intValue() & 2) != 0;
    }

    public boolean frontLeftCameraSensorStatus() {
        return (((Integer) get(0, 1, Integer.class)).intValue() & 4) != 0;
    }

    public boolean frontRightCameraSensorStatus() {
        return (((Integer) get(0, 1, Integer.class)).intValue() & 8) != 0;
    }

    public boolean bottomTOFCameraSensorStatus() {
        return (((Integer) get(0, 1, Integer.class)).intValue() & 16) != 0;
    }

    public int reservedSensor() {
        return ((Integer) get(0, 2, Integer.class)).intValue() & 992;
    }

    public boolean downCailStatus() {
        return (((Integer) get(0, 2, Integer.class)).intValue() & 1024) != 0;
    }

    public boolean front3DTOFCailStatus() {
        return (((Integer) get(0, 2, Integer.class)).intValue() & 2048) != 0;
    }

    public int reservedSensorCail() {
        return ((Integer) get(0, 2, Integer.class)).intValue() & 61440;
    }

    public boolean rangeDownCailStatus() {
        return (((Integer) get(2, 1, Integer.class)).intValue() & 1) != 0;
    }

    public boolean rangeTopCailStatus() {
        return (((Integer) get(2, 1, Integer.class)).intValue() & 2) != 0;
    }

    public boolean lrxStatus() {
        return (((Integer) get(2, 1, Integer.class)).intValue() & 4) != 0;
    }

    public boolean receiveFcStatus() {
        return (((Integer) get(2, 1, Integer.class)).intValue() & 8) != 0;
    }

    public boolean transmitFcStatus() {
        return (((Integer) get(2, 1, Integer.class)).intValue() & 16) != 0;
    }

    public boolean ipcStatus() {
        return (((Integer) get(2, 1, Integer.class)).intValue() & 32) != 0;
    }

    public boolean cnnStatus() {
        return (((Integer) get(2, 1, Integer.class)).intValue() & 64) != 0;
    }

    public boolean autoExposureComponentStatus() {
        return (((Integer) get(2, 1, Integer.class)).intValue() & 128) != 0;
    }

    public boolean voStatus() {
        return (((Integer) get(3, 1, Integer.class)).intValue() & 1) != 0;
    }

    public boolean vioStatus() {
        return (((Integer) get(3, 1, Integer.class)).intValue() & 2) != 0;
    }

    public boolean eisStatus() {
        return (((Integer) get(3, 1, Integer.class)).intValue() & 4) != 0;
    }

    public boolean netStatus() {
        return (((Integer) get(3, 1, Integer.class)).intValue() & 8) != 0;
    }

    public boolean cameraStatus() {
        return (((Integer) get(3, 1, Integer.class)).intValue() & 16) != 0;
    }

    public boolean airscrewProtectCoverDetectStatus() {
        return (((Integer) get(3, 1, Integer.class)).intValue() & 32) != 0;
    }

    public int reservedFunction() {
        return ((Integer) get(3, 1, Integer.class)).intValue() & 192;
    }

    /* access modifiers changed from: protected */
    public void doPack() {
    }
}
