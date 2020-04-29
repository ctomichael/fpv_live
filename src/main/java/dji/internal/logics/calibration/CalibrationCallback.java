package dji.internal.logics.calibration;

import dji.common.remotecontroller.CalibrationState;
import dji.fieldAnnotation.EXClassNullAway;

@EXClassNullAway
public interface CalibrationCallback {
    void onUpdateCalibrationInfo(RemoteControllerCalibrationInfo remoteControllerCalibrationInfo);

    void onUpdateCalibrationMode(CalibrationState calibrationState);
}
