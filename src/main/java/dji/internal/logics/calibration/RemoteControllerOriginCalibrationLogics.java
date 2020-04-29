package dji.internal.logics.calibration;

import android.os.Handler;
import android.os.Message;
import dji.common.remotecontroller.CalibrationState;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.config.P3.Ccode;
import dji.midware.data.config.P3.ProductType;
import dji.midware.data.manager.P3.DJIProductManager;
import dji.midware.data.model.P3.DataRcSetCalibration;
import dji.midware.interfaces.DJIDataCallBack;
import java.util.Iterator;

@EXClassNullAway
public class RemoteControllerOriginCalibrationLogics extends DJIRemoteControllerCalibrationLogics {
    private static final long DELAY_SEND_LIMIT = 500;
    private static final int MSG_ID_SEND_LIMIT = 4097;
    private static final int MSG_ID_SET_MODE_CB = 4096;
    private static final String TAG = "RemoteControllerOriginCalibrationLogics";

    public interface OriginCalibrationCallback extends CalibrationCallback {
        void sendModeSuccess();
    }

    private RemoteControllerOriginCalibrationLogics() {
    }

    private static class LazyHolder {
        /* access modifiers changed from: private */
        public static RemoteControllerOriginCalibrationLogics instance = new RemoteControllerOriginCalibrationLogics();

        private LazyHolder() {
        }
    }

    public static RemoteControllerOriginCalibrationLogics getInstance() {
        return LazyHolder.instance;
    }

    public CalibrationState getState() {
        return getRcCalibrationState(false);
    }

    /* access modifiers changed from: protected */
    public Controller getController() {
        if (this.controller == null) {
            this.controller = new Controller();
        }
        return (Controller) this.controller;
    }

    public void startCalibration() {
        RcCaliLog.logWriteI(TAG, "startCalibration");
        super.startCalibration();
    }

    public boolean hasStart() {
        return getController().hasStart();
    }

    public void doNext(boolean auto) {
        getController().doNext(auto);
    }

    public void init() {
        super.init();
        RcCaliLog.logWriteI(TAG, "Logics init");
        this.controller = getController();
        this.handler = new Handler(new RemoteControllerOriginCalibrationLogics$$Lambda$0(this));
    }

    /* access modifiers changed from: package-private */
    public final /* synthetic */ boolean lambda$init$0$RemoteControllerOriginCalibrationLogics(Message message) {
        switch (message.what) {
            case 4096:
                if (message.arg1 == 0) {
                    getController().getRcMode(true);
                    Iterator it2 = this.callbacks.iterator();
                    while (it2.hasNext()) {
                        CalibrationCallback callback = (CalibrationCallback) it2.next();
                        if (callback instanceof OriginCalibrationCallback) {
                            ((OriginCalibrationCallback) callback).sendModeSuccess();
                        }
                    }
                    return false;
                }
                getController().doNext(true);
                return false;
            case 4097:
                getController().sendLimits();
                return false;
            default:
                return false;
        }
    }

    /* access modifiers changed from: private */
    public boolean isWm240() {
        return ProductType.WM240 == DJIProductManager.getInstance().getType();
    }

    /* access modifiers changed from: private */
    public boolean isWm245() {
        return ProductType.WM245 == DJIProductManager.getInstance().getType();
    }

    public void destroy() {
        super.destroy();
    }

    public void addCalibrationCallback(CalibrationCallback calibrationCallback) {
        super.addCalibrationCallback(calibrationCallback);
    }

    public void removeCalibrationCallback(CalibrationCallback calibrationCallback) {
        super.removeCalibrationCallback(calibrationCallback);
    }

    public void handleConnectedEvent(boolean isConnected) {
        super.handleConnectedEvent(isConnected);
    }

    private final class Controller extends DJIRemoteControllerCalibrationController {
        private boolean printed;

        private Controller() {
            init();
        }

        /* access modifiers changed from: protected */
        public void init() {
            RcCaliLog.logWriteI(RemoteControllerOriginCalibrationLogics.TAG, "Controller init");
            this.setDataInstance = DataRcSetCalibration.getInstance();
            this.setModeCB = new DJIDataCallBack() {
                /* class dji.internal.logics.calibration.RemoteControllerOriginCalibrationLogics.Controller.AnonymousClass1 */

                public void onSuccess(Object model) {
                    if (RemoteControllerOriginCalibrationLogics.this.handler != null) {
                        RemoteControllerOriginCalibrationLogics.this.handler.obtainMessage(4096, 0, 0).sendToTarget();
                    }
                }

                public void onFailure(Ccode ccode) {
                    RcCaliLog.logWriteE(RemoteControllerOriginCalibrationLogics.TAG, "set mode fails");
                    if (RemoteControllerOriginCalibrationLogics.this.handler != null) {
                        RemoteControllerOriginCalibrationLogics.this.handler.obtainMessage(4096, 1, 0).sendToTarget();
                    }
                }
            };
        }

        /* access modifiers changed from: protected */
        public boolean hasStart() {
            return this.isStart;
        }

        /* access modifiers changed from: protected */
        public DataRcSetCalibration.MODE getRcMode(boolean fromRc) {
            if (fromRc && this.isConnected && this.setDataInstance != null) {
                DataRcSetCalibration.MODE mode = this.setDataInstance.getMode();
                if (mode == DataRcSetCalibration.MODE.Limits && RemoteControllerOriginCalibrationLogics.this.rcMode == DataRcSetCalibration.MODE.Limits && !this.printed) {
                    this.printed = true;
                    RcCaliLog.logWriteI(RemoteControllerOriginCalibrationLogics.TAG, "getRcMode mode " + mode + ", rcMode " + RemoteControllerOriginCalibrationLogics.this.rcMode);
                } else if (!(mode == DataRcSetCalibration.MODE.Limits && RemoteControllerOriginCalibrationLogics.this.rcMode == DataRcSetCalibration.MODE.Limits)) {
                    RcCaliLog.logWriteI(RemoteControllerOriginCalibrationLogics.TAG, "getRcMode mode " + mode + ", rcMode " + RemoteControllerOriginCalibrationLogics.this.rcMode);
                }
                if (mode == RemoteControllerOriginCalibrationLogics.this.rcMode) {
                    doNext(true);
                } else if (mode == DataRcSetCalibration.MODE.Limits || ((RemoteControllerOriginCalibrationLogics.this.isWm240() || RemoteControllerOriginCalibrationLogics.this.isWm245()) && mode == DataRcSetCalibration.MODE.Quit)) {
                    doNext(true);
                } else if (RemoteControllerOriginCalibrationLogics.this.rcMode == DataRcSetCalibration.MODE.Normal && mode == DataRcSetCalibration.MODE.Middle) {
                    doNext(true);
                }
                RemoteControllerOriginCalibrationLogics.this.rcMode = mode;
                Iterator it2 = RemoteControllerOriginCalibrationLogics.this.callbacks.iterator();
                while (it2.hasNext()) {
                    ((CalibrationCallback) it2.next()).onUpdateCalibrationMode(CalibrationState.find(RemoteControllerOriginCalibrationLogics.this.rcMode.value()));
                }
            }
            return RemoteControllerOriginCalibrationLogics.this.rcMode;
        }

        /* access modifiers changed from: private */
        public void sendLimits() {
            if (this.setDataInstance != null) {
                this.setDataInstance.setMode(DataRcSetCalibration.MODE.Limits).start(this.setModeCB);
            }
        }

        /* access modifiers changed from: protected */
        public void doNext(boolean auto) {
            if (this.isConnected) {
                if (this.setDataInstance != null) {
                    if (RemoteControllerOriginCalibrationLogics.this.rcMode == DataRcSetCalibration.MODE.OTHER) {
                        RcCaliLog.logWriteI(RemoteControllerOriginCalibrationLogics.TAG, "set Rc mode other -> normal");
                        this.setDataInstance.setMode(DataRcSetCalibration.MODE.Normal).start(this.setModeCB);
                    } else if (RemoteControllerOriginCalibrationLogics.this.rcMode == DataRcSetCalibration.MODE.Normal) {
                        if (this.isStart) {
                            RcCaliLog.logWriteI(RemoteControllerOriginCalibrationLogics.TAG, "set Rc mode normal -> middle");
                            this.setDataInstance.setMode(DataRcSetCalibration.MODE.Middle).start(this.setModeCB);
                            return;
                        }
                        this.isStart = true;
                    } else if (RemoteControllerOriginCalibrationLogics.this.rcMode == DataRcSetCalibration.MODE.Middle) {
                        Iterator it2 = RemoteControllerOriginCalibrationLogics.this.callbacks.iterator();
                        while (it2.hasNext()) {
                            ((CalibrationCallback) it2.next()).onUpdateCalibrationMode(CalibrationState.find(DataRcSetCalibration.MODE.Middle.value()));
                        }
                        this.isStart = false;
                        if (RemoteControllerOriginCalibrationLogics.this.handler != null) {
                            RcCaliLog.logWriteI(RemoteControllerOriginCalibrationLogics.TAG, "set Rc mode middle -> limit");
                            RemoteControllerOriginCalibrationLogics.this.handler.sendEmptyMessageDelayed(4097, 500);
                        }
                    } else if (RemoteControllerOriginCalibrationLogics.this.rcMode == DataRcSetCalibration.MODE.Limits) {
                        this.setDataInstance.setMode(DataRcSetCalibration.MODE.Limits).start(this.setModeCB);
                    } else if (RemoteControllerOriginCalibrationLogics.this.rcMode == DataRcSetCalibration.MODE.Quit) {
                        RcCaliLog.logWriteI(RemoteControllerOriginCalibrationLogics.TAG, "set Rc mode Quit -> Quit, auto " + auto);
                        if (!RemoteControllerOriginCalibrationLogics.this.isWm240() && !RemoteControllerOriginCalibrationLogics.this.isWm245()) {
                            this.setDataInstance.setMode(DataRcSetCalibration.MODE.Quit).start(this.setModeCB);
                        } else if (!auto) {
                            this.setDataInstance.setMode(DataRcSetCalibration.MODE.Quit).start(this.setModeCB);
                        } else if (RemoteControllerOriginCalibrationLogics.this.handler != null) {
                            RemoteControllerOriginCalibrationLogics.this.handler.sendEmptyMessageDelayed(4097, 500);
                        }
                    }
                }
            } else if (!auto) {
                Iterator it3 = RemoteControllerOriginCalibrationLogics.this.callbacks.iterator();
                while (it3.hasNext()) {
                    ((CalibrationCallback) it3.next()).onUpdateCalibrationMode(CalibrationState.find(DataRcSetCalibration.MODE.Disconnect.value()));
                }
            }
        }
    }
}
