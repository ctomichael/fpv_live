package dji.internal.logics.calibration;

import com.dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.model.P3.DataRcGetPushParams;

@EXClassNullAway
public class RemoteControllerCalibrationInfo {
    private final int aileron;
    private final int elevator;
    private final int gyroValue;
    private final int leftDialValue;
    private final int rightDialValue;
    private final int rightGyroValue;
    private final int rightGyroValue1;
    private final int rudder;
    private final int throttle;

    public RemoteControllerCalibrationInfo(DataRcGetPushParams param) {
        this.throttle = param.getThrottle();
        this.rudder = param.getRudder();
        this.elevator = param.getElevator();
        this.aileron = param.getAileron();
        this.gyroValue = param.getGyroValue();
        this.rightGyroValue = param.getRightGyroValue();
        this.leftDialValue = param.getLeftDialValue();
        this.rightDialValue = param.getRightDialValue();
        this.rightGyroValue1 = param.getRightGyroValueV1();
    }

    public int getThrottle() {
        return this.throttle;
    }

    public int getRudder() {
        return this.rudder;
    }

    public int getElevator() {
        return this.elevator;
    }

    public int getAileron() {
        return this.aileron;
    }

    public int getGyroValue() {
        return this.gyroValue;
    }

    public int getRightGyroValue() {
        return this.rightGyroValue;
    }

    public int getRightGyroValue1() {
        return this.rightGyroValue1;
    }

    public int getLeftDialValue() {
        return this.leftDialValue;
    }

    public int getRightDialValue() {
        return this.rightDialValue;
    }
}
