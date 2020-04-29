package dji.dbox.upgrade.p4.statemachine;

import dji.dbox.upgrade.p4.constants.DJIUpConstants;
import dji.dbox.upgrade.p4.constants.DJIUpDeviceType;
import dji.dbox.upgrade.p4.model.DJIUpStatus;
import dji.dbox.upgrade.p4.statemachine.DJIUpgradeService;
import dji.dbox.upgrade.p4.utils.DJIUpStatusHelper;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.config.P3.Ccode;
import dji.midware.data.config.P3.DeviceType;
import dji.midware.data.manager.P3.ServiceManager;
import dji.midware.data.model.P3.DataCameraGetPushUpgradeStatus;
import dji.midware.data.model.P3.DataCommonControlUpgrade;
import dji.midware.data.model.P3.DataCommonGetPushUpgradeStatus;
import dji.midware.data.model.P3.DataNotifyDisconnect;
import dji.midware.interfaces.DJIDataCallBack;
import dji.publics.DJIObject.DJIObject;
import java.util.Timer;
import java.util.TimerTask;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

@EXClassNullAway
public class DJIUpProgressManager extends DJIObject {
    /* access modifiers changed from: private */
    public String TAG = getClass().getSimpleName();
    /* access modifiers changed from: private */
    public DataCommonGetPushUpgradeStatus.DJIUpgradeCompleteReason completeReasonTmp = null;
    private DataCommonControlUpgrade controlUpgrade;
    /* access modifiers changed from: private */
    public int mUpgradeProcess;
    private DataCommonGetPushUpgradeStatus pushUpgradeStatus = new DataCommonGetPushUpgradeStatus();
    /* access modifiers changed from: private */
    public DJIUpgradeStateMachine stateMachine;
    /* access modifiers changed from: private */
    public int timeout;
    private Timer timer;
    /* access modifiers changed from: private */
    public float tmpProcess;

    static /* synthetic */ int access$010(DJIUpProgressManager x0) {
        int i = x0.timeout;
        x0.timeout = i - 1;
        return i;
    }

    public DJIUpProgressManager(DJIUpgradeStateMachine stateMachine2) {
        this.stateMachine = stateMachine2;
        this.controlUpgrade = new DataCommonControlUpgrade();
        this.controlUpgrade.setReceiveId(1);
        init();
    }

    /* access modifiers changed from: protected */
    public void onCreate() {
        EventBus.getDefault().register(this);
    }

    public void onDestroy() {
        EventBus.getDefault().unregister(this);
    }

    private void init() {
        DataCommonGetPushUpgradeStatus.DataCommonGetPushUpgradeStatusInfo statusInfo = DataCommonGetPushUpgradeStatus.getInstance().getDescList();
        if (statusInfo != null && statusInfo.mUpgradeStep != null) {
            this.stateMachine.reCoverListenProgress();
            onEvent3MainThread(DataCommonGetPushUpgradeStatus.getInstance());
        }
    }

    private void recover() {
        DataCommonGetPushUpgradeStatus.DataCommonGetPushUpgradeStatusInfo statusInfo = DataCommonGetPushUpgradeStatus.getInstance().getDescList();
        if (statusInfo != null && statusInfo.mUpgradeStep != null && statusInfo.completeReason != DataCommonGetPushUpgradeStatus.DJIUpgradeCompleteReason.Success) {
            this.stateMachine.reCoverListenProgress();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent3MainThread(DataNotifyDisconnect notifyDisconnect) {
        this.timeout = notifyDisconnect.getTimeout() * 2;
        if (this.timeout > DJIUpgradeService.timeOut) {
            DJIUpgradeService.timeOut = this.timeout;
        } else {
            this.timeout = DJIUpgradeService.timeOut;
        }
        DJIUpConstants.LOGE(this.TAG, "notifyDisconnect timeout=" + this.timeout);
        startWaiting();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent3MainThread(DJIUpgradeService.DJIUpP4Event event) {
        switch (event) {
            case Disconnect:
                if (DJIUpStatusHelper.isUpProgressing()) {
                    this.timeout = 2;
                    return;
                }
                return;
            default:
                return;
        }
    }

    private void startWaiting() {
        int remainProcess = 99 - this.mUpgradeProcess;
        final float perProcess = (((float) remainProcess) * 1.0f) / ((float) this.timeout);
        this.tmpProcess = (float) this.mUpgradeProcess;
        cancelTimer();
        this.timer = new Timer();
        DJIUpConstants.LOGE(this.TAG, "timer schedule remainProcess=" + remainProcess);
        this.timer.schedule(new TimerTask() {
            /* class dji.dbox.upgrade.p4.statemachine.DJIUpProgressManager.AnonymousClass1 */

            public void run() {
                DJIUpProgressManager.access$010(DJIUpProgressManager.this);
                if (DJIUpProgressManager.this.timeout <= 0) {
                    DJIUpProgressManager.this.cancelTimer();
                    if (DJIUpProgressManager.this.stateMachine != null) {
                        DJIUpProgressManager.this.stateMachine.timeout();
                        return;
                    }
                    return;
                }
                float unused = DJIUpProgressManager.this.tmpProcess = DJIUpProgressManager.this.tmpProcess + perProcess;
                int mpgs = Math.round(DJIUpProgressManager.this.tmpProcess);
                DJIUpConstants.LOGE(DJIUpProgressManager.this.TAG, "tmpProcess=" + DJIUpProgressManager.this.tmpProcess + " mpgs=" + mpgs + " mUpgradeProcess" + DJIUpProgressManager.this.mUpgradeProcess);
                if (mpgs >= 100) {
                    mpgs = 99;
                }
                DJIUpProgressManager.this.tellProgress(mpgs);
            }
        }, 0, 1000);
    }

    private static boolean isNeedFakeProgress() {
        DJIUpStatus status = DJIUpStatusHelper.getUpgradingStatus();
        if (status == null || status.getProductId() != DJIUpDeviceType.wm240) {
            return true;
        }
        return ServiceManager.getInstance().isConnected();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent3MainThread(DataCommonGetPushUpgradeStatus upgradeStatus) {
        if (!DJIUpStatusHelper.isNewUpgradeSystem()) {
            DJIUpConstants.LOGE(this.TAG, "DataCommonGetPushUpgradeStatus be return by type curEvent = " + DJIUpStatusHelper.getCurEvent());
            return;
        }
        DataCommonGetPushUpgradeStatus.DataCommonGetPushUpgradeStatusInfo statusInfo = upgradeStatus.getDescList();
        if (statusInfo.mUpgradeStep == DataCommonGetPushUpgradeStatus.DJIUpgradeStep.DataUpgrading) {
            DJIUpConstants.LOGE(this.TAG, "DataCommonGetPushUpgradeStatus be return by dataUp " + statusInfo.dataUpgradeStatusInfo.mUpgradeProcess);
            if (statusInfo.dataUpgradeStatusInfo.mUpgradeProcess == 100) {
                DJIUpConstants.LOGE(this.TAG, "DataCommonGetPushUpgradeStatus complete by dataUp");
                this.controlUpgrade.setReceiveType(DataCommonGetPushUpgradeStatus.getInstance().getSenderDeviceType());
                this.controlUpgrade.setReceiveId(DataCommonGetPushUpgradeStatus.getInstance().getSenderDeviceId());
                this.controlUpgrade.setControlCmd(DataCommonControlUpgrade.ControlCmd.StopPush).start(new DJIDataCallBack() {
                    /* class dji.dbox.upgrade.p4.statemachine.DJIUpProgressManager.AnonymousClass2 */

                    public void onSuccess(Object model) {
                        DJIUpConstants.LOGE(DJIUpProgressManager.this.TAG, "setControlCmd StopPush suc");
                    }

                    public void onFailure(Ccode ccode) {
                        DJIUpConstants.LOGE(DJIUpProgressManager.this.TAG, "setControlCmd StopPush ccode+" + ccode);
                    }
                });
                return;
            }
            return;
        }
        if (!DJIUpStatusHelper.isUpProgressing()) {
            if (!DJIUpStatusHelper.isChecking()) {
                int dType = upgradeStatus.getSenderDeviceType().value();
                int dId = upgradeStatus.getSenderDeviceId();
                int firmwareType = 0;
                if (statusInfo.mUpgradeDescList != null && statusInfo.mUpgradeDescList.size() > 0) {
                    firmwareType = statusInfo.mUpgradeDescList.get(0).mFirmwareType;
                }
                DJIUpStatusHelper.setUpgradingStatus(DJIUpStatus.factoryRecover(dType, dId, firmwareType));
                DJIUpStatusHelper.setIsUpProgressing(true, true);
                DJIUpStatusHelper.setIsUpDownloading(false);
                DJIUpConstants.LOGE(this.TAG, "recover to progress!!!!!!!!!! status dType=" + dType + " dId=" + dId);
                recover();
            } else {
                DJIUpConstants.LOGE(this.TAG, "DataCommonGetPushUpgradeStatus be return by checking");
                return;
            }
        }
        if (DJIUpStatusHelper.isNeedAppPushUpgradeStatus()) {
            this.pushUpgradeStatus.pushStatus(DataCameraGetPushUpgradeStatus.UpgradeStep.Progress, DeviceType.OSD.value(), 0);
        }
        switch (statusInfo.mUpgradeStep) {
            case Verify:
                tellVerify();
                return;
            case UserConfirm:
                tellConfirm();
                return;
            case Upgrading:
                cancelTimer();
                tellProgress(Math.min(100, statusInfo.mUpgradeProcess == 100 ? 99 : statusInfo.mUpgradeProcess));
                return;
            case Complete:
                cancelTimer();
                tellReason(statusInfo.completeReason);
                return;
            default:
                return;
        }
    }

    /* access modifiers changed from: package-private */
    public void cancelTimer() {
        DJIUpgradeService.cancelTimer();
        if (this.timer != null) {
            this.timer.cancel();
            this.timer = null;
        }
    }

    private void tellVerify() {
        this.stateMachine.notifyUpgradePgs("固件校验中", 0);
    }

    private void tellConfirm() {
        this.stateMachine.notifyUpgradePgs("用户确认中", 0);
    }

    /* access modifiers changed from: private */
    public void tellProgress(int progress) {
        if (this.stateMachine.isUploading()) {
            DJIUpConstants.LOGE(this.TAG, "isUploading not tellProgress");
            return;
        }
        if (progress != this.mUpgradeProcess && progress >= this.mUpgradeProcess) {
            this.mUpgradeProcess = progress;
            DJIUpConstants.LOGE(this.TAG, "mUpgradeProcess=" + this.mUpgradeProcess);
        }
        this.stateMachine.notifyUpgradePgs("升级中", this.mUpgradeProcess);
    }

    /* access modifiers changed from: package-private */
    public void tellReason(DataCommonGetPushUpgradeStatus.DJIUpgradeCompleteReason completeReason) {
        if (this.completeReasonTmp == completeReason) {
            DJIUpConstants.LOGE(this.TAG, "DataCommonGetPushUpgradeStatus be return completeReason same");
            return;
        }
        this.completeReasonTmp = completeReason;
        this.stateMachine.notifyUpgradePgs("结束原因：" + completeReason.toString(), this.mUpgradeProcess);
        this.mUpgradeProcess = 0;
        DJIUpConstants.LOGE(this.TAG, "结束原因：" + completeReason.toString());
        this.controlUpgrade.setReceiveType(DataCommonGetPushUpgradeStatus.getInstance().getSenderDeviceType());
        this.controlUpgrade.setReceiveId(DataCommonGetPushUpgradeStatus.getInstance().getSenderDeviceId());
        this.controlUpgrade.setControlCmd(DataCommonControlUpgrade.ControlCmd.StopPush).start(new DJIDataCallBack() {
            /* class dji.dbox.upgrade.p4.statemachine.DJIUpProgressManager.AnonymousClass3 */

            public void onSuccess(Object model) {
                DJIUpConstants.LOGE(DJIUpProgressManager.this.TAG, "setControlCmd StopPush suc");
                DataCommonGetPushUpgradeStatus.DJIUpgradeCompleteReason unused = DJIUpProgressManager.this.completeReasonTmp = null;
                DJIUpStatusHelper.setIsUpProgressing(false);
            }

            public void onFailure(Ccode ccode) {
                DJIUpConstants.LOGE(DJIUpProgressManager.this.TAG, "setControlCmd StopPush ccode+" + ccode);
                DataCommonGetPushUpgradeStatus.DJIUpgradeCompleteReason unused = DJIUpProgressManager.this.completeReasonTmp = null;
                DJIUpStatusHelper.setIsUpProgressing(false);
            }
        });
        if (DJIUpStatusHelper.isNeedAppPushUpgradeStatus()) {
            this.pushUpgradeStatus.pushStatus(DataCameraGetPushUpgradeStatus.UpgradeStep.End, DeviceType.OSD.value(), 0);
        }
        switch (completeReason) {
            case Success:
                this.stateMachine.listenProgressComplete();
                return;
            default:
                this.stateMachine.listenProgressFail(completeReason);
                return;
        }
    }
}
