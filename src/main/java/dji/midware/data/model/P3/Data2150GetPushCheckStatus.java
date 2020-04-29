package dji.midware.data.model.P3;

import android.support.annotation.Keep;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.model.base.DJICommonDataBase;

@Keep
@EXClassNullAway
public class Data2150GetPushCheckStatus extends DJICommonDataBase {
    private static Data2150GetPushCheckStatus instance = null;

    public static synchronized Data2150GetPushCheckStatus getInstance() {
        Data2150GetPushCheckStatus data2150GetPushCheckStatus;
        synchronized (Data2150GetPushCheckStatus.class) {
            if (instance == null) {
                instance = new Data2150GetPushCheckStatus();
            }
            data2150GetPushCheckStatus = instance;
        }
        return data2150GetPushCheckStatus;
    }

    /* access modifiers changed from: protected */
    public void doPack() {
    }

    public boolean doesBottomVisionSensorHasError() {
        return (((Integer) get(0, 1, Integer.class)).intValue() & 1) == 1;
    }

    public boolean doesBottomTofSensorHasError() {
        return (((Integer) get(0, 1, Integer.class)).intValue() & 2) == 2;
    }

    public boolean doesFrontVisionSensorHasError() {
        return (((Integer) get(0, 1, Integer.class)).intValue() & 4) == 4;
    }

    public boolean doesFront3DTofSensorHasError() {
        return (((Integer) get(0, 1, Integer.class)).intValue() & 8) == 8;
    }

    public boolean doesMainCameraHasError() {
        return (((Integer) get(0, 1, Integer.class)).intValue() & 16) == 16;
    }

    public boolean doesBottomVisionSensorHasCalibrationError() {
        return (((Integer) get(1, 1, Integer.class)).intValue() & 1) == 1;
    }

    public boolean doesBottomTofSensorHasCalibrationError() {
        return (((Integer) get(1, 1, Integer.class)).intValue() & 2) == 2;
    }

    public boolean doesFrontVisionSensorHasCalibrationError() {
        return (((Integer) get(1, 1, Integer.class)).intValue() & 4) == 4;
    }

    public boolean doesFront3DTofSensorHasCalibrationError() {
        return (((Integer) get(1, 1, Integer.class)).intValue() & 8) == 8;
    }

    public boolean doesUSBHasError() {
        return (((Integer) get(2, 1, Integer.class)).intValue() & 1) == 1;
    }

    public boolean doesFlightControllerReceiveingProcessHasError() {
        return (((Integer) get(2, 1, Integer.class)).intValue() & 2) == 2;
    }

    public boolean doesFlightControllerSendingProcessHasError() {
        return (((Integer) get(2, 1, Integer.class)).intValue() & 4) == 4;
    }

    public boolean doesGimbalReceiveingProcessHasError() {
        return (((Integer) get(2, 1, Integer.class)).intValue() & 8) == 8;
    }

    public boolean isAutoExposuringNotFunctional() {
        return (((Integer) get(2, 1, Integer.class)).intValue() & 32) == 32;
    }

    public boolean isVoNotFunctional() {
        return (((Integer) get(2, 1, Integer.class)).intValue() & 64) == 64;
    }

    public boolean isFusionNotFunctional() {
        return (((Integer) get(2, 1, Integer.class)).intValue() & 128) == 128;
    }

    public boolean isOANotFunctional() {
        return (((Integer) get(3, 1, Integer.class)).intValue() & 1) == 1;
    }

    public boolean isleonRTSystemNotFuntional() {
        return (((Integer) get(3, 1, Integer.class)).intValue() & 2) == 2;
    }

    public boolean isEisNotFunctional() {
        return (((Integer) get(3, 1, Integer.class)).intValue() & 4) == 4;
    }

    public boolean isWaterSurfaceDetected() {
        return (((Integer) get(3, 1, Integer.class)).intValue() & 8) == 8;
    }

    public boolean isMainCameraNotFunctional() {
        return (((Integer) get(3, 1, Integer.class)).intValue() & 16) == 16;
    }

    public boolean isCrewProtectionDetected() {
        return (((Integer) get(3, 1, Integer.class)).intValue() & 32) == 32;
    }
}
