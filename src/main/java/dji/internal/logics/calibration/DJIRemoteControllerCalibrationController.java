package dji.internal.logics.calibration;

import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.model.P3.DataRcSetCalibration;
import dji.midware.interfaces.DJIDataCallBack;

@EXClassNullAway
public abstract class DJIRemoteControllerCalibrationController {
    private static final String TAG = "DJIRemoteControllerCalibrationController";
    protected boolean isConnected = false;
    protected boolean isStart = false;
    private DataRcSetCalibration.MODE rcMode = DataRcSetCalibration.MODE.OTHER;
    protected DataRcSetCalibration setDataInstance = null;
    protected DJIDataCallBack setModeCB = null;

    /* access modifiers changed from: protected */
    public abstract void init();

    /* access modifiers changed from: protected */
    public void start() {
        RcCaliLog.logWriteI(TAG, "Controller start, rcMode " + this.rcMode + " -> normal");
        this.isConnected = true;
        if (DataRcSetCalibration.MODE.OTHER == this.rcMode && this.setDataInstance != null) {
            this.setDataInstance.setMode(DataRcSetCalibration.MODE.Normal).start(this.setModeCB);
        }
    }

    /* access modifiers changed from: protected */
    public void stop() {
        RcCaliLog.logWriteI(TAG, "Controller stop");
        this.isStart = false;
        this.isConnected = false;
        this.rcMode = DataRcSetCalibration.MODE.OTHER;
    }

    /* access modifiers changed from: protected */
    public boolean hasStart() {
        return this.isStart;
    }

    /* access modifiers changed from: protected */
    public DataRcSetCalibration.MODE getRcMode(boolean fromRc) {
        return this.rcMode;
    }

    /* access modifiers changed from: protected */
    public void doNext(boolean auto) {
    }
}
