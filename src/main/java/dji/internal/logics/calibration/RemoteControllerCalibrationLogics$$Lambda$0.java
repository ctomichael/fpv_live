package dji.internal.logics.calibration;

import android.os.Handler;
import android.os.Message;

final /* synthetic */ class RemoteControllerCalibrationLogics$$Lambda$0 implements Handler.Callback {
    private final RemoteControllerCalibrationLogics arg$1;

    RemoteControllerCalibrationLogics$$Lambda$0(RemoteControllerCalibrationLogics remoteControllerCalibrationLogics) {
        this.arg$1 = remoteControllerCalibrationLogics;
    }

    public boolean handleMessage(Message message) {
        return this.arg$1.lambda$init$0$RemoteControllerCalibrationLogics(message);
    }
}
