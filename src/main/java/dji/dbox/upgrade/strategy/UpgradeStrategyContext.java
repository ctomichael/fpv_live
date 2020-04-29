package dji.dbox.upgrade.strategy;

import dji.dbox.upgrade.collectpacks.UpCollectorListener;
import dji.dbox.upgrade.p4.constants.DJIUpConstants;
import dji.dbox.upgrade.p4.constants.DJIUpDeviceType;
import dji.dbox.upgrade.p4.constants.DJIUpNotifyEvent;
import dji.dbox.upgrade.p4.statemachine.DJIUpgradeStateMachine;
import dji.dbox.upgrade.p4.statemachine.UpDeviceInfoCollector;
import dji.dbox.upgrade.p4.utils.DJIUpStatusHelper;
import dji.fieldAnnotation.EXClassNullAway;
import org.greenrobot.eventbus.EventBus;

@EXClassNullAway
public class UpgradeStrategyContext implements UpDeviceInfoCollector.UpDeviceInfoCollectListener, UpCollectorListener {
    private static final String TAG = "UpgradeStrategyContext";
    private DJIUpDeviceType device1;
    private DJIUpDeviceType device2;
    private UpDeviceInfoCollector deviceInfoCollector = new UpDeviceInfoCollector();
    private DJIUpgradeStateMachine stateMachine;
    private AbstractStrategy tmpStrategy;

    public UpgradeStrategyContext() {
        this.deviceInfoCollector.setCollectListener(this);
    }

    public void collectDeviceInfo() {
        if (!DJIUpStatusHelper.isCheckingSetted() || !DJIUpStatusHelper.isChecking() || DJIUpStatusHelper.isIgnoreIsChecking()) {
            this.deviceInfoCollector.start();
        } else {
            DJIUpConstants.LOGD(TAG, "collect device info return by isChecking");
        }
    }

    public void collectDeviceInfoOffline() {
        this.deviceInfoCollector.startOffline();
    }

    public void doCollector() {
        if (this.tmpStrategy != null) {
            DJIUpStatusHelper.setIsChecking(true, "UpgradeStrategyContext doCollector");
            this.tmpStrategy.doNext(this.stateMachine.getContext(), this.device1, this.device2);
        }
    }

    public void disConnect() {
        if (isOfflineDevice()) {
            DJIUpConstants.LOGD(TAG, "UpgradeStrategyContext clear device1=" + this.device1 + " device2=" + this.device2);
            this.device1 = null;
            this.device2 = null;
        }
        this.deviceInfoCollector.disConnect();
    }

    public void onCollectStart() {
        DJIUpStatusHelper.setIsChecking(true, "UpgradeStrategyContext onCollectStart");
        this.stateMachine.onCollectDeviceStart();
    }

    public void onCollectOver(Class<? extends AbstractStrategy> strategyCls, DJIUpDeviceType device12, DJIUpDeviceType device22) {
        this.device1 = device12;
        this.device2 = device22;
        if (device12 == null || strategyCls == null) {
            this.stateMachine.collectPackListFail();
            return;
        }
        stop(true);
        try {
            this.tmpStrategy = (AbstractStrategy) strategyCls.getConstructor(new Class[0]).newInstance(new Object[0]);
            this.tmpStrategy.setUpCollectorListener(this);
        } catch (Exception e) {
            DJIUpConstants.LOGE(TAG, e.getMessage());
            e.printStackTrace();
        }
    }

    public void onStrategyCollectVersionOver() {
        this.stateMachine.collectPackDeviceComplete();
    }

    public void onStrategyCollectListOver() {
        this.stateMachine.collectPackListComplete();
        EventBus.getDefault().post(DJIUpNotifyEvent.CollectComplete);
        DJIUpStatusHelper.setIsChecking(false, "UpgradeStrategyContext onStrategyCollectListOver");
    }

    public void reset() {
        this.deviceInfoCollector.reset();
    }

    public void setStateMachine(DJIUpgradeStateMachine stateMachine2) {
        this.stateMachine = stateMachine2;
    }

    public void collectProductTypeComplete() {
        this.stateMachine.collectProductTypeComplete();
    }

    public void stop(boolean needQuit) {
        if (this.tmpStrategy != null) {
            this.tmpStrategy.stop(needQuit);
        }
    }

    public boolean isNotAllow() {
        return this.device1 == null && this.device2 == null;
    }

    private boolean isOfflineDevice() {
        return (this.device1 != null && this.device1.isOffline()) || (this.device2 != null && this.device2.isOffline());
    }
}
