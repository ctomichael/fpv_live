package dji.dbox.upgrade.p4.statemachine;

import android.content.Context;
import com.dji.frame.util.V_ActivityUtil;
import dji.dbox.upgrade.p4.constants.DJIUpConstants;
import dji.dbox.upgrade.p4.interfaces.DJIUpDownloadArrayListeners;
import dji.dbox.upgrade.p4.interfaces.DJIUpDownloadListener;
import dji.dbox.upgrade.p4.interfaces.DJIUpgradeListener;
import dji.dbox.upgrade.p4.interfaces.UpdateUIListener;
import dji.dbox.upgrade.p4.model.DJIUpListElement;
import dji.dbox.upgrade.p4.model.DJIUpStatus;
import dji.dbox.upgrade.p4.statemachine.DJIUpgradeService;
import dji.dbox.upgrade.p4.utils.DJIUpStatusHelper;
import dji.dbox.upgrade.strategy.UpgradeStrategyContext;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.broadcastReceivers.DJINetWorkReceiver;
import dji.midware.data.config.P3.Ccode;
import dji.midware.data.config.P3.DeviceType;
import dji.midware.data.model.P3.DataCommonControlUpgrade;
import dji.midware.data.model.P3.DataCommonGetPushUpgradeStatus;
import dji.midware.interfaces.DJIDataCallBack;
import dji.publics.DJIObject.DJIObject;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

@EXClassNullAway
public class DJIUpgradeP4Manager extends DJIObject {
    private static final String TAG = "DJIUpgradeP4Manager";
    private DJIUpDownloadArrayListeners downloadListeners = new DJIUpDownloadArrayListeners();
    private DJIUpDownloadManager downloadManager;
    /* access modifiers changed from: private */
    public DJIUpgradeStateMachine stateMachine;

    DJIUpgradeP4Manager(Context context, UpgradeStrategyContext strategyContext) {
        this.stateMachine = new DJIUpgradeStateMachine(context, strategyContext);
        strategyContext.setStateMachine(this.stateMachine);
        this.stateMachine.setDbg(V_ActivityUtil.isApkDebugable(context));
    }

    /* access modifiers changed from: protected */
    public void onCreate() {
        EventBus.getDefault().register(this);
    }

    public void onDestroy() {
        EventBus.getDefault().unregister(this);
    }

    public boolean isUpgrading() {
        return this.stateMachine.isUpgrading() || DJIUpStatusHelper.isUpProgressing();
    }

    public void outAnalogTellReason(boolean success) {
        this.stateMachine.analogTellReason(success ? DataCommonGetPushUpgradeStatus.DJIUpgradeCompleteReason.Success : DataCommonGetPushUpgradeStatus.DJIUpgradeCompleteReason.Failure);
    }

    public void registerListener(UpdateUIListener updateUIListener) {
        this.stateMachine.registerListener(updateUIListener);
    }

    public void registerUpgradeListener(DJIUpgradeListener mUpgradeListener) {
        if (DJIUpStatusHelper.isChecking() && DJINetWorkReceiver.getNetWorkStatus(this.stateMachine.getContext())) {
            DJIUpConstants.LOGD(TAG, "registerUpgradeListener DJIUpStatusHelper.isChecking() = true");
            mUpgradeListener.onCollectVersionStart();
        }
        this.stateMachine.registerUpgradeListener(mUpgradeListener);
    }

    public void unRegisterUpgradeListener(DJIUpgradeListener mUpgradeListener) {
        this.stateMachine.unRegisterUpgradeListener(mUpgradeListener);
    }

    public void registerDownloadListener(DJIUpDownloadListener downloadListener) {
        this.downloadListeners.add(downloadListener);
    }

    public void unRegisterDownloadListener(DJIUpDownloadListener downloadListener) {
        this.downloadListeners.remove(downloadListener);
    }

    public void init() {
        this.stateMachine.start();
    }

    private void download(boolean isRollback) {
        if (this.downloadManager == null) {
            this.downloadManager = new DJIUpDownloadManager(this);
        }
        this.downloadManager.start(this.downloadListeners, isRollback);
    }

    public void startDownloadLatest() {
        DJIUpStatusHelper.setUpgradingStatus(true);
        for (DJIUpStatus upStatus : DJIUpStatusHelper.getMainPageStatusTogether()) {
            if (upStatus.isNeedUpgrade()) {
                upStatus.setChoiceElementToLatest();
            }
        }
        download(false);
    }

    public void startDownload(DJIUpListElement choiceElement) {
        DJIUpStatusHelper.setUpgradingStatus(false);
        DJIUpStatusHelper.getUpgradingStatus().setChoiceElement(choiceElement);
        download(true);
    }

    public void startUpgrade() {
        DJIUpStatusHelper.setUpgradingStatus(true);
        DJIUpStatusHelper.getUpgradingStatus().setChoiceElementToLatest();
        DJIUpStatusHelper.setIsUpDownloading(true);
        this.stateMachine.startUpgrade();
    }

    public void startUpgrade(DJIUpListElement choiceElement) {
        DJIUpStatusHelper.setUpgradingStatus(false);
        DJIUpStatusHelper.getUpgradingStatus().setChoiceElement(choiceElement);
        DJIUpStatusHelper.setIsUpDownloading(true);
        this.stateMachine.startUpgrade();
    }

    public void recover() {
        DataCommonControlUpgrade.getInstance().setReceiveType(DeviceType.DM368).setReceiveId(1).setControlCmd(DataCommonControlUpgrade.ControlCmd.Stop).start(new DJIDataCallBack() {
            /* class dji.dbox.upgrade.p4.statemachine.DJIUpgradeP4Manager.AnonymousClass1 */

            public void onSuccess(Object model) {
                DJIUpgradeP4Manager.this.stateMachine.recover();
            }

            public void onFailure(Ccode ccode) {
                DJIUpConstants.LOGE("", "recover DataCameraControlUpgrade =" + ccode);
            }
        });
    }

    /* access modifiers changed from: package-private */
    public void reCollect(String reason) {
        if (DJIUpStatusHelper.isNewUpgradeSystem()) {
            this.stateMachine.collectPackList(reason);
        }
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEvent3BackgroundThread(DJIUpgradeService.DJIUpP4Event event) {
        switch (event) {
            case ConnectP4RC:
                this.stateMachine.connectedP4();
                return;
            case ConnectP4MC:
                this.stateMachine.connectedP4();
                return;
            case ConnectOther:
                DJIUpStatusHelper.setIsChecking(false, "DJIUpgradeP4Manager onEvent3BackgroundThread " + event);
                return;
            case Disconnect:
                this.stateMachine.disconnected();
                DJIUpStatusHelper.setIsChecking(false, "DJIUpgradeP4Manager onEvent3BackgroundThread " + event);
                return;
            default:
                return;
        }
    }

    public void timeout() {
        this.stateMachine.timeout();
    }
}
