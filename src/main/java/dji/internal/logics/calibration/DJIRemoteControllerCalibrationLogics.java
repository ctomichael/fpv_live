package dji.internal.logics.calibration;

import android.os.Handler;
import com.dji.fieldAnnotation.EXClassNullAway;
import dji.common.remotecontroller.CalibrationState;
import dji.midware.data.model.P3.DataRcGetPushParams;
import dji.midware.data.model.P3.DataRcSetCalibration;
import dji.midware.util.DJIEventBusUtil;
import java.util.ArrayList;
import java.util.Iterator;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

@EXClassNullAway
public abstract class DJIRemoteControllerCalibrationLogics {
    protected ArrayList<CalibrationCallback> callbacks;
    protected DJIRemoteControllerCalibrationController controller;
    protected Handler handler;
    protected DataRcSetCalibration.MODE rcMode = DataRcSetCalibration.MODE.OTHER;

    /* access modifiers changed from: protected */
    public void init() {
        this.rcMode = DataRcSetCalibration.MODE.OTHER;
        this.callbacks = new ArrayList<>();
        DJIEventBusUtil.register(this);
    }

    /* access modifiers changed from: protected */
    public void destroy() {
        DJIEventBusUtil.unRegister(this);
        if (this.callbacks != null) {
            this.callbacks.clear();
        }
        if (this.handler != null) {
            this.handler.removeCallbacksAndMessages(null);
        }
        if (this.controller != null) {
            this.controller.stop();
        }
        this.rcMode = DataRcSetCalibration.MODE.OTHER;
    }

    /* access modifiers changed from: protected */
    public CalibrationState getRcCalibrationState(boolean fromRc) {
        if (this.controller != null) {
            return CalibrationState.find(this.controller.getRcMode(fromRc).value());
        }
        return CalibrationState.find(this.rcMode.value());
    }

    /* access modifiers changed from: protected */
    public void addCalibrationCallback(CalibrationCallback calibrationCallback) {
        if (this.callbacks != null) {
            this.callbacks.add(calibrationCallback);
        }
    }

    /* access modifiers changed from: protected */
    public void removeCalibrationCallback(CalibrationCallback calibrationCallback) {
        if (this.callbacks != null) {
            this.callbacks.remove(calibrationCallback);
        }
    }

    /* access modifiers changed from: protected */
    public void startCalibration() {
        if (this.controller != null) {
            this.controller.doNext(false);
        }
    }

    /* access modifiers changed from: protected */
    public void handleConnectedEvent(boolean isConnected) {
        if (isConnected) {
            if (this.controller != null) {
                this.controller.start();
            }
        } else if (this.controller != null) {
            this.controller.stop();
        }
    }

    /* access modifiers changed from: protected */
    public DJIRemoteControllerCalibrationController getController() {
        return this.controller;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent3MainThread(DataRcGetPushParams param) {
        if (param != null && this.callbacks != null) {
            Iterator<CalibrationCallback> it2 = this.callbacks.iterator();
            while (it2.hasNext()) {
                it2.next().onUpdateCalibrationInfo(new RemoteControllerCalibrationInfo(param));
            }
        }
    }
}
