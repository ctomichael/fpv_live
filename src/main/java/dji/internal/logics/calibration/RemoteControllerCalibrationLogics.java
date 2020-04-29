package dji.internal.logics.calibration;

import android.os.Handler;
import android.os.Message;
import com.dji.fieldAnnotation.EXClassNullAway;
import dji.common.remotecontroller.CalibrationState;
import dji.midware.data.config.P3.Ccode;
import dji.midware.data.model.P3.DataRcSetCalibration;
import dji.midware.interfaces.DJIDataCallBack;
import java.util.Iterator;

@EXClassNullAway
public class RemoteControllerCalibrationLogics extends DJIRemoteControllerCalibrationLogics {
    private static final int MSG_ID_SET_MODE_CB = 0;

    private static class LazyHolder {
        /* access modifiers changed from: private */
        public static RemoteControllerCalibrationLogics instance = new RemoteControllerCalibrationLogics();

        private LazyHolder() {
        }
    }

    public static RemoteControllerCalibrationLogics getInstance() {
        return LazyHolder.instance;
    }

    public void init() {
        super.init();
        this.controller = getController();
        this.handler = new Handler(new RemoteControllerCalibrationLogics$$Lambda$0(this));
    }

    /* access modifiers changed from: package-private */
    public final /* synthetic */ boolean lambda$init$0$RemoteControllerCalibrationLogics(Message message) {
        switch (message.what) {
            case 0:
                if (message.arg1 == 0) {
                    getController().getRcMode(true);
                    return false;
                }
                getController().doNext(true);
                return false;
            default:
                return false;
        }
    }

    public CalibrationState getMode() {
        return CalibrationState.find(getRcCalibrationState(false).value());
    }

    /* access modifiers changed from: protected */
    public Controller getController() {
        if (this.controller == null) {
            this.controller = new Controller();
        }
        return (Controller) this.controller;
    }

    public void startCalibration() {
        super.startCalibration();
    }

    public void handleConnectedEvent(boolean isConnected) {
        super.handleConnectedEvent(isConnected);
    }

    public void addCalibrationCallback(CalibrationCallback calibrationCallback) {
        super.addCalibrationCallback(calibrationCallback);
    }

    public void removeCalibrationCallback(CalibrationCallback calibrationCallback) {
        super.removeCalibrationCallback(calibrationCallback);
    }

    public void destroy() {
        super.destroy();
    }

    private final class Controller extends DJIRemoteControllerCalibrationController {
        private boolean hasTimeOut;

        private Controller() {
            this.hasTimeOut = false;
            init();
        }

        /* access modifiers changed from: protected */
        public void init() {
            this.setDataInstance = DataRcSetCalibration.getInstance();
            this.setModeCB = new DJIDataCallBack() {
                /* class dji.internal.logics.calibration.RemoteControllerCalibrationLogics.Controller.AnonymousClass1 */

                public void onSuccess(Object model) {
                    RemoteControllerCalibrationLogics.this.handler.obtainMessage(0, 0, 0).sendToTarget();
                }

                public void onFailure(Ccode ccode) {
                    RemoteControllerCalibrationLogics.this.handler.obtainMessage(0, 1, 0).sendToTarget();
                }
            };
        }

        /* access modifiers changed from: protected */
        public void start() {
            super.start();
            this.hasTimeOut = false;
        }

        /* access modifiers changed from: protected */
        public void stop() {
            super.stop();
            this.hasTimeOut = false;
        }

        /* access modifiers changed from: protected */
        public boolean hasStart() {
            return this.isStart;
        }

        /* access modifiers changed from: protected */
        public DataRcSetCalibration.MODE getRcMode(boolean fromRc) {
            if (fromRc && this.isConnected) {
                if (this.setDataInstance == null) {
                    return RemoteControllerCalibrationLogics.this.rcMode;
                }
                DataRcSetCalibration.MODE mode = this.setDataInstance.getMode();
                if (mode == RemoteControllerCalibrationLogics.this.rcMode || mode == DataRcSetCalibration.MODE.Limits || mode == DataRcSetCalibration.MODE.TimeOut || mode == DataRcSetCalibration.MODE.Quit) {
                    doNext(true);
                } else if (RemoteControllerCalibrationLogics.this.rcMode == DataRcSetCalibration.MODE.Normal && mode == DataRcSetCalibration.MODE.Middle) {
                    doNext(true);
                }
                RemoteControllerCalibrationLogics.this.rcMode = mode;
                Iterator it2 = RemoteControllerCalibrationLogics.this.callbacks.iterator();
                while (it2.hasNext()) {
                    ((CalibrationCallback) it2.next()).onUpdateCalibrationMode(CalibrationState.find(RemoteControllerCalibrationLogics.this.rcMode.value()));
                }
            }
            return RemoteControllerCalibrationLogics.this.rcMode;
        }

        /* access modifiers changed from: protected */
        public void doNext(boolean auto) {
            if (this.isConnected) {
                if (this.setDataInstance != null) {
                    if (RemoteControllerCalibrationLogics.this.rcMode == DataRcSetCalibration.MODE.OTHER) {
                        this.isStart = false;
                        this.setDataInstance.setMode(DataRcSetCalibration.MODE.Normal).start(this.setModeCB);
                    } else if (RemoteControllerCalibrationLogics.this.rcMode == DataRcSetCalibration.MODE.Normal) {
                        if (this.isStart) {
                            this.setDataInstance.setMode(DataRcSetCalibration.MODE.Middle).start(this.setModeCB);
                        } else {
                            this.isStart = true;
                        }
                    } else if (RemoteControllerCalibrationLogics.this.rcMode == DataRcSetCalibration.MODE.Middle) {
                        this.isStart = false;
                        this.setDataInstance.setMode(DataRcSetCalibration.MODE.Limits).start(this.setModeCB);
                    } else if (RemoteControllerCalibrationLogics.this.rcMode == DataRcSetCalibration.MODE.Limits) {
                        this.setDataInstance.setMode(DataRcSetCalibration.MODE.Middle).start(this.setModeCB);
                    } else if (RemoteControllerCalibrationLogics.this.rcMode == DataRcSetCalibration.MODE.Quit) {
                        this.setDataInstance.setMode(DataRcSetCalibration.MODE.Quit).start(this.setModeCB);
                    } else if (RemoteControllerCalibrationLogics.this.rcMode == DataRcSetCalibration.MODE.TimeOut) {
                        if (!this.hasTimeOut) {
                            this.hasTimeOut = true;
                        }
                        this.setDataInstance.setMode(DataRcSetCalibration.MODE.TimeOut).start(this.setModeCB);
                    }
                    if (RemoteControllerCalibrationLogics.this.rcMode != DataRcSetCalibration.MODE.TimeOut) {
                        this.hasTimeOut = false;
                    }
                }
            } else if (!auto) {
                Iterator it2 = RemoteControllerCalibrationLogics.this.callbacks.iterator();
                while (it2.hasNext()) {
                    ((CalibrationCallback) it2.next()).onUpdateCalibrationMode(CalibrationState.find(DataRcSetCalibration.MODE.Disconnect.value()));
                }
            }
        }
    }
}
