package dji.internal.logics.calibration;

import android.os.Handler;
import android.os.Message;

final /* synthetic */ class RemoteControllerOriginCalibrationLogics$$Lambda$0 implements Handler.Callback {
    private final RemoteControllerOriginCalibrationLogics arg$1;

    RemoteControllerOriginCalibrationLogics$$Lambda$0(RemoteControllerOriginCalibrationLogics remoteControllerOriginCalibrationLogics) {
        this.arg$1 = remoteControllerOriginCalibrationLogics;
    }

    public boolean handleMessage(Message message) {
        return this.arg$1.lambda$init$0$RemoteControllerOriginCalibrationLogics(message);
    }
}
