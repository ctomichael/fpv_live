package dji.dbox.upgrade.p4.statemachine;

import android.content.Context;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import dji.dbox.upgrade.p4.constants.DJIUpConstants;
import dji.dbox.upgrade.p4.interfaces.DJIUpgradeArrayListeners;
import dji.dbox.upgrade.p4.interfaces.DJIUpgradeListener;
import dji.dbox.upgrade.p4.interfaces.UpdateUIListener;
import dji.dbox.upgrade.p4.model.DJIUpStatus;
import dji.dbox.upgrade.p4.server.DJIUpServerManager;
import dji.dbox.upgrade.p4.utils.DJIUpStatusHelper;
import dji.dbox.upgrade.p4.utils.DJIUpStatusOfflineHelper;
import dji.dbox.upgrade.strategy.UpgradeStrategyContext;
import dji.fieldAnnotation.EXClassNullAway;
import dji.log.DJILogHelper;
import dji.midware.data.config.P3.Ccode;
import dji.midware.data.model.P3.DataCommonGetPushUpgradeStatus;
import dji.tools.sm.IState;
import dji.tools.sm.State;
import dji.tools.sm.StateMachine;

@EXClassNullAway
public class DJIUpgradeStateMachine extends StateMachine {
    private static final int MSG_CONNECTED_OTHER = 2;
    private static final int MSG_CONNECTED_P4 = 1;
    private static final int MSG_CollectPackDeviceComplete = 10;
    private static final int MSG_CollectPackList = 11;
    private static final int MSG_CollectPackListComplete = 13;
    private static final int MSG_CollectPackListFailed = 12;
    private static final int MSG_CollectProductTypeComplete = 40;
    private static final int MSG_DISCONNECTED = 3;
    private static final int MSG_Download = 14;
    private static final int MSG_DownloadComplete = 16;
    private static final int MSG_DownloadFailed = 15;
    private static final int MSG_EnterDownload = 9;
    private static final int MSG_ListenProgress = 23;
    private static final int MSG_ListenProgressComplete = 25;
    private static final int MSG_ListenProgressFail = 24;
    private static final int MSG_MSG = 1000;
    private static final int MSG_RECOVER = 0;
    private static final int MSG_SEND_WITH_FTP = 201;
    private static final int MSG_UploadComplete = 22;
    private static final int MSG_UploadFail = 21;
    private static final int MSG_UploadFailEnter = 102;
    private static final int MSG_UploadFailInit = 101;
    private static final int MSG_UploadFailPreTran = 104;
    private static final int MSG_UploadFailQuit = 108;
    private static final int MSG_UploadFailTraning = 106;
    private static final int MSG_UploadNextPack = 109;
    private static final int MSG_UploadStart = 20;
    private static final int MSG_Zip = 17;
    private static final int MSG_ZipComplete = 19;
    private static final int MSG_ZipFailed = 18;
    private static final int MSG_ZipProgress = 1001;
    private static final int MSG_reCoverListenProgress = 26;
    /* access modifiers changed from: private */
    public static final String TAG = DJIUpgradeStateMachine.class.getSimpleName();
    private Context context;
    /* access modifiers changed from: private */
    public UpdateUIListener listener;
    /* access modifiers changed from: private */
    public CollectPackListFailState mCollectPackListFailState;
    /* access modifiers changed from: private */
    public CollectPackListState mCollectPackListState;
    /* access modifiers changed from: private */
    public ConnectedOtherState mConnectedOtherState;
    private ConnectedP4State mConnectedP4State;
    /* access modifiers changed from: private */
    public DisConnectedState mDisConnectedState;
    /* access modifiers changed from: private */
    public DownloadState mDownloadState;
    /* access modifiers changed from: private */
    public ListenProgressState mListenProgressState;
    /* access modifiers changed from: private */
    public int mPreProgress;
    /* access modifiers changed from: private */
    public ProgressFailState mProgressFailState;
    private UpDoingState mUpDoingState;
    private UpErrorState mUpErrorState;
    /* access modifiers changed from: private */
    public UpOverState mUpOverState;
    private UpReadyState mUpReadyState;
    /* access modifiers changed from: private */
    public UploadFailEnterState mUploadFailEnterState;
    /* access modifiers changed from: private */
    public UploadFailInitState mUploadFailInitState;
    /* access modifiers changed from: private */
    public UploadFailPreTranState mUploadFailPreTranState;
    /* access modifiers changed from: private */
    public UploadFailQuitState mUploadFailQuitState;
    private UploadFailState mUploadFailState;
    /* access modifiers changed from: private */
    public UploadFailTraningState mUploadFailTraningState;
    /* access modifiers changed from: private */
    public UploadState mUploadState;
    /* access modifiers changed from: private */
    public ZipFailState mZipFailState;
    /* access modifiers changed from: private */
    public ZipState mZipState;
    /* access modifiers changed from: private */
    public UpgradeStrategyContext strategyContext;
    private DJIUpProgressManager upProgressManager;
    /* access modifiers changed from: private */
    public DJIUpgradeArrayListeners upgradeListener;
    /* access modifiers changed from: private */
    public Ccode uploadFailEnterCcode;
    /* access modifiers changed from: private */
    public DJIUpUploadManager uploadManager;
    private boolean uploading;

    private DJIUpgradeStateMachine() {
        super(TAG);
        this.listener = null;
        this.mConnectedP4State = new ConnectedP4State();
        this.mDisConnectedState = new DisConnectedState();
        this.mConnectedOtherState = new ConnectedOtherState();
        this.mUpReadyState = new UpReadyState();
        this.mUpDoingState = new UpDoingState();
        this.mUpOverState = new UpOverState();
        this.mUpErrorState = new UpErrorState();
        this.mCollectPackListState = new CollectPackListState();
        this.mDownloadState = new DownloadState();
        this.mZipState = new ZipState();
        this.mUploadState = new UploadState();
        this.mListenProgressState = new ListenProgressState();
        this.mCollectPackListFailState = new CollectPackListFailState();
        this.mZipFailState = new ZipFailState();
        this.mUploadFailState = new UploadFailState();
        this.mProgressFailState = new ProgressFailState();
        this.mUploadFailInitState = new UploadFailInitState();
        this.mUploadFailEnterState = new UploadFailEnterState();
        this.mUploadFailPreTranState = new UploadFailPreTranState();
        this.mUploadFailTraningState = new UploadFailTraningState();
        this.mUploadFailQuitState = new UploadFailQuitState();
        addState(this.mConnectedP4State);
        addState(this.mDisConnectedState);
        addState(this.mConnectedOtherState);
        addState(this.mUpReadyState, this.mConnectedP4State);
        addState(this.mUpDoingState, this.mConnectedP4State);
        addState(this.mUpOverState, this.mConnectedP4State);
        addState(this.mUpErrorState, this.mConnectedP4State);
        addState(this.mCollectPackListState, this.mConnectedP4State);
        addState(this.mDownloadState, this.mConnectedP4State);
        addState(this.mZipState, this.mUpDoingState);
        addState(this.mUploadState, this.mUpDoingState);
        addState(this.mListenProgressState, this.mUpDoingState);
        addState(this.mCollectPackListFailState, this.mUpErrorState);
        addState(this.mZipFailState, this.mUpErrorState);
        addState(this.mUploadFailState, this.mUpErrorState);
        addState(this.mProgressFailState, this.mUpErrorState);
        addState(this.mUploadFailEnterState, this.mUploadFailState);
        addState(this.mUploadFailPreTranState, this.mUploadFailState);
        addState(this.mUploadFailTraningState, this.mUploadFailState);
        addState(this.mUploadFailQuitState, this.mUploadFailState);
        setInitialState(this.mDisConnectedState);
    }

    DJIUpgradeStateMachine(Context context2, UpgradeStrategyContext strategyContext2) {
        this();
        this.context = context2;
        this.strategyContext = strategyContext2;
        if (DJIUpStatusHelper.initTimes > 1) {
            Log.e(TAG, "init stateMachine times = " + DJIUpStatusHelper.initTimes);
        }
    }

    public void start() {
        super.start();
        if (this.upgradeListener == null) {
            this.upgradeListener = new DJIUpgradeArrayListeners();
        }
        if (this.upProgressManager == null) {
            this.upProgressManager = new DJIUpProgressManager(this);
        }
    }

    public void registerListener(UpdateUIListener l) {
        this.listener = l;
    }

    /* access modifiers changed from: package-private */
    public void registerUpgradeListener(DJIUpgradeListener l) {
        if (l != null && this.upgradeListener != null) {
            this.upgradeListener.add(l);
        }
    }

    /* access modifiers changed from: package-private */
    public void unRegisterUpgradeListener(DJIUpgradeListener l) {
        if (l != null && this.upgradeListener != null) {
            this.upgradeListener.remove(l);
        }
    }

    public Context getContext() {
        return this.context;
    }

    public void onCollectDeviceStart() {
        if (this.upgradeListener != null) {
            this.upgradeListener.onCollectProductTypeStart();
        }
    }

    public void sendWithFtp() {
        sendMessage(201);
    }

    public boolean isUploading() {
        return getUpCurrentState() == this.mUploadState;
    }

    class MyState extends State {
        MyState() {
        }

        public void enter() {
            super.enter();
            if (DJIUpgradeStateMachine.this.listener != null) {
                DJIUpgradeStateMachine.this.listener.update(getClass().getSimpleName());
            }
            DJILogHelper.getInstance().LOGI(DJIUpgradeStateMachine.TAG, "enter " + getClass().getSimpleName());
        }

        public void exit() {
            super.exit();
            DJILogHelper.getInstance().LOGI(DJIUpgradeStateMachine.TAG, "exit " + getClass().getSimpleName());
        }
    }

    private class DisConnectedState extends MyState {
        private DisConnectedState() {
            super();
        }

        public void enter() {
            super.enter();
        }

        public boolean processMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    DJIUpgradeStateMachine.this.transitionTo(DJIUpgradeStateMachine.this.mCollectPackListState);
                    DJIUpgradeStateMachine.this.sendMessage(11);
                    return true;
                case 2:
                    DJIUpgradeStateMachine.this.transitionTo(DJIUpgradeStateMachine.this.mConnectedOtherState);
                    return true;
                default:
                    return super.processMessage(msg);
            }
        }

        public void exit() {
            super.exit();
        }
    }

    private class ConnectedOtherState extends MyState {
        private ConnectedOtherState() {
            super();
        }

        public void enter() {
            super.enter();
        }

        public boolean processMessage(Message msg) {
            switch (msg.what) {
                case 3:
                    DJIUpgradeStateMachine.this.transitionTo(DJIUpgradeStateMachine.this.mDisConnectedState);
                    return true;
                default:
                    return super.processMessage(msg);
            }
        }

        public void exit() {
            super.exit();
        }
    }

    class ConnectedP4State extends MyState {
        ConnectedP4State() {
            super();
        }

        public void enter() {
            super.enter();
        }

        public boolean processMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    DJIUpgradeStateMachine.this.transitionTo(DJIUpgradeStateMachine.this.mCollectPackListState);
                    DJIUpgradeStateMachine.this.sendMessage(11);
                    return true;
                case 2:
                default:
                    return super.processMessage(msg);
                case 3:
                    DJIUpgradeStateMachine.this.transitionTo(DJIUpgradeStateMachine.this.mDisConnectedState);
                    return true;
            }
        }

        public void exit() {
            super.exit();
        }
    }

    private class UpReadyState extends ConnectedP4State {
        private UpReadyState() {
            super();
        }

        public void enter() {
            super.enter();
        }

        public boolean processMessage(Message msg) {
            return super.processMessage(msg);
        }

        public void exit() {
            super.exit();
        }
    }

    class UpDownloadState extends ConnectedP4State {
        UpDownloadState() {
            super();
        }

        public void enter() {
            super.enter();
        }

        public boolean processMessage(Message msg) {
            return super.processMessage(msg);
        }

        public void exit() {
            super.exit();
        }
    }

    class UpDoingState extends ConnectedP4State {
        UpDoingState() {
            super();
        }

        public void enter() {
            super.enter();
        }

        public boolean processMessage(Message msg) {
            return super.processMessage(msg);
        }

        public void exit() {
            super.exit();
        }
    }

    private class UpOverState extends ConnectedP4State {
        private UpOverState() {
            super();
        }

        public void enter() {
            super.enter();
            DJIUpStatus current = DJIUpStatusHelper.getUpgradingStatus();
            if (current != null) {
                current.setNeedUpgrade(false);
            }
            if (DJIUpgradeStateMachine.this.upgradeListener != null) {
                DJIUpgradeStateMachine.this.upgradeListener.onUpgradeComplete();
            }
            DJIUpgradeStateMachine.this.removeMessages(11);
            if (!DJIUpStatusHelper.isNeedUpgrade() || !DJIUpStatusHelper.isUnderSyncUpgradeContext()) {
                DJIUpgradeStateMachine.this.sendMessageDelayed(11, 5000);
            }
        }

        public boolean processMessage(Message msg) {
            switch (msg.what) {
                case 11:
                    DJIUpgradeStateMachine.this.transitionTo(DJIUpgradeStateMachine.this.mCollectPackListState);
                    DJIUpgradeStateMachine.this.sendMessage(11);
                    break;
            }
            return super.processMessage(msg);
        }

        public void exit() {
            super.exit();
        }
    }

    class UpErrorState extends ConnectedP4State {
        UpErrorState() {
            super();
        }

        public void enter() {
            super.enter();
        }

        public boolean processMessage(Message msg) {
            return super.processMessage(msg);
        }

        public void exit() {
            super.exit();
        }
    }

    private class CollectPackListState extends ConnectedP4State {
        private CollectPackListState() {
            super();
        }

        public void enter() {
            super.enter();
        }

        public boolean processMessage(Message msg) {
            switch (msg.what) {
                case 9:
                    DJIUpgradeStateMachine.this.transitionTo(DJIUpgradeStateMachine.this.mDownloadState);
                    DJIUpgradeStateMachine.this.sendMessage(14);
                    return true;
                case 10:
                    if (DJIUpgradeStateMachine.this.upgradeListener == null) {
                        return true;
                    }
                    DJIUpgradeStateMachine.this.upgradeListener.onCollectDeviceCfgComplete();
                    return true;
                case 11:
                    if (DJIUpgradeStateMachine.this.strategyContext == null || DJIUpgradeStateMachine.this.strategyContext.isNotAllow()) {
                        DJIUpgradeStateMachine.this.onCollectFail("no devices");
                        return true;
                    }
                    DJIUpgradeStateMachine.this.strategyContext.stop(false);
                    DJIUpgradeStateMachine.this.strategyContext.collectProductTypeComplete();
                    DJIUpgradeStateMachine.this.strategyContext.doCollector();
                    if (DJIUpgradeStateMachine.this.upgradeListener == null) {
                        return true;
                    }
                    DJIUpgradeStateMachine.this.upgradeListener.onCollectVersionStart();
                    return true;
                case 12:
                    DJIUpgradeStateMachine.this.transitionTo(DJIUpgradeStateMachine.this.mCollectPackListFailState);
                    return true;
                case 13:
                    if (DJIUpgradeStateMachine.this.upgradeListener == null) {
                        return true;
                    }
                    DJIUpgradeStateMachine.this.upgradeListener.onCollectVersionComplete();
                    return true;
                case 26:
                    DJIUpgradeStateMachine.this.transitionTo(DJIUpgradeStateMachine.this.mListenProgressState);
                    DJIUpgradeStateMachine.this.sendMessage(23);
                    return true;
                case 40:
                    if (DJIUpgradeStateMachine.this.upgradeListener == null) {
                        return true;
                    }
                    DJIUpgradeStateMachine.this.upgradeListener.onCollectProductTypeComplete();
                    return true;
                case 1000:
                    return true;
                default:
                    return super.processMessage(msg);
            }
        }

        public void exit() {
            super.exit();
        }
    }

    private class DownloadState extends ConnectedP4State {
        private DownloadState() {
            super();
        }

        public void enter() {
            super.enter();
        }

        public boolean processMessage(Message msg) {
            switch (msg.what) {
                case 14:
                case 15:
                case 16:
                    return true;
                default:
                    return super.processMessage(msg);
            }
        }

        public void exit() {
            super.exit();
        }
    }

    private class ZipState extends UpDoingState {
        private ZipState() {
            super();
        }

        public void enter() {
            super.enter();
            DJIUpStatusHelper.setIsUpDownloading(true);
        }

        public boolean processMessage(Message msg) {
            switch (msg.what) {
                case 17:
                    if (DJIUpgradeStateMachine.this.upgradeListener != null) {
                        DJIUpgradeStateMachine.this.upgradeListener.onZipStart();
                    }
                    DJIUpTarManager.start(new DJIUpServerManager.TarCallBack() {
                        /* class dji.dbox.upgrade.p4.statemachine.DJIUpgradeStateMachine.ZipState.AnonymousClass1 */

                        public void onZipProgress(int progress) {
                            DJIUpgradeStateMachine.this.sendMessage(1001, progress);
                        }

                        public void onSuccess() {
                            DJIUpgradeStateMachine.this.sendMessage(19);
                            DJIUpConstants.LOGD(DJIUpgradeStateMachine.TAG, "send MSG_ZipComplete");
                        }

                        public void onFailed() {
                            DJIUpgradeStateMachine.this.sendMessage(18);
                        }
                    });
                    return true;
                case 18:
                    DJIUpgradeStateMachine.this.transitionTo(DJIUpgradeStateMachine.this.mZipFailState);
                    return true;
                case 19:
                    DJIUpConstants.LOGD(DJIUpgradeStateMachine.TAG, "MSG_ZipComplete");
                    if (DJIUpgradeStateMachine.this.upgradeListener != null) {
                        DJIUpgradeStateMachine.this.upgradeListener.onZipComplete();
                    }
                    DJIUpgradeStateMachine.this.transitionTo(DJIUpgradeStateMachine.this.mUploadState);
                    DJIUpgradeStateMachine.this.sendMessage(20, DJIUpgradeStateMachine.this.obtainMessage(20, 1, 0));
                    return true;
                case 1000:
                    return true;
                case 1001:
                    if (DJIUpgradeStateMachine.this.upgradeListener == null) {
                        return true;
                    }
                    DJIUpgradeStateMachine.this.upgradeListener.onZipProgress(msg.arg1);
                    return true;
                default:
                    return super.processMessage(msg);
            }
        }

        public void exit() {
            super.exit();
        }
    }

    private class UploadState extends UpDoingState {
        private UploadState() {
            super();
        }

        public void enter() {
            super.enter();
            DJIUpStatusHelper.setIsUpDownloading(false);
            DJIUpStatusHelper.setIsUpProgressing(true);
        }

        public boolean processMessage(Message msg) {
            switch (msg.what) {
                case 20:
                    if (DJIUpgradeStateMachine.this.upgradeListener != null) {
                        DJIUpgradeStateMachine.this.upgradeListener.onUploadStart();
                    }
                    if (DJIUpgradeStateMachine.this.uploadManager == null) {
                        DJIUpUploadManager unused = DJIUpgradeStateMachine.this.uploadManager = new DJIUpUploadManager(DJIUpgradeStateMachine.this);
                    }
                    if (!DJIUpgradeStateMachine.this.uploadManager.init(msg.arg1 == 1)) {
                        return true;
                    }
                    DJIUpgradeStateMachine.this.uploadManager.start();
                    int unused2 = DJIUpgradeStateMachine.this.mPreProgress = -1;
                    return true;
                case 22:
                    int unused3 = DJIUpgradeStateMachine.this.mPreProgress = -1;
                    if (DJIUpgradeStateMachine.this.upgradeListener != null) {
                        DJIUpgradeStateMachine.this.upgradeListener.onUploadComplete();
                    }
                    DJIUpgradeStateMachine.this.transitionTo(DJIUpgradeStateMachine.this.mListenProgressState);
                    DJIUpgradeStateMachine.this.sendMessage(23);
                    return true;
                case 101:
                    DJIUpgradeStateMachine.this.transitionTo(DJIUpgradeStateMachine.this.mUploadFailInitState);
                    return true;
                case 102:
                    Ccode unused4 = DJIUpgradeStateMachine.this.uploadFailEnterCcode = (Ccode) msg.obj;
                    DJIUpgradeStateMachine.this.transitionTo(DJIUpgradeStateMachine.this.mUploadFailEnterState);
                    return true;
                case 104:
                    DJIUpgradeStateMachine.this.transitionTo(DJIUpgradeStateMachine.this.mUploadFailPreTranState);
                    return true;
                case 106:
                    DJIUpgradeStateMachine.this.transitionTo(DJIUpgradeStateMachine.this.mUploadFailTraningState);
                    return true;
                case 108:
                    DJIUpgradeStateMachine.this.transitionTo(DJIUpgradeStateMachine.this.mUploadFailQuitState);
                    DJIUpgradeStateMachine.this.sendMessage(DJIUpgradeStateMachine.this.obtainMessage(msg.what, msg.obj));
                    return true;
                case 109:
                    boolean isNeedNext = ((Boolean) msg.obj).booleanValue();
                    int progress = DJIUpgradeStateMachine.this.uploadManager.getProgress();
                    if (progress > DJIUpgradeStateMachine.this.mPreProgress) {
                        int unused5 = DJIUpgradeStateMachine.this.mPreProgress = progress;
                        if (DJIUpgradeStateMachine.this.upgradeListener != null) {
                            DJIUpgradeStateMachine.this.upgradeListener.onUploadProgress(progress);
                        }
                    }
                    if (!isNeedNext) {
                        return true;
                    }
                    DJIUpgradeStateMachine.this.uploadManager.uploadNextPack();
                    return true;
                case 201:
                    DJIUpgradeStateMachine.this.uploadManager.sendWithFtp();
                    return true;
                default:
                    return super.processMessage(msg);
            }
        }

        public void exit() {
            super.exit();
        }
    }

    private class ListenProgressState extends UpDoingState {
        private ListenProgressState() {
            super();
        }

        public void enter() {
            super.enter();
            DJIUpStatusHelper.setIsUpProgressing(true, DJIUpStatusHelper.isRecovered());
        }

        public boolean processMessage(Message msg) {
            switch (msg.what) {
                case 23:
                    return true;
                case 24:
                    if (DJIUpgradeStateMachine.this.upgradeListener != null) {
                        DJIUpgradeStateMachine.this.upgradeListener.onUpgradeFail((DataCommonGetPushUpgradeStatus.DJIUpgradeCompleteReason) msg.obj);
                    }
                    DJIUpgradeStateMachine.this.transitionTo(DJIUpgradeStateMachine.this.mProgressFailState);
                    return true;
                case 25:
                    DJIUpgradeStateMachine.this.transitionTo(DJIUpgradeStateMachine.this.mUpOverState);
                    return true;
                default:
                    return super.processMessage(msg);
            }
        }

        public void exit() {
            super.exit();
        }
    }

    private class CollectPackListFailState extends UpErrorState {
        private CollectPackListFailState() {
            super();
        }

        public void enter() {
            super.enter();
        }

        public boolean processMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    DJIUpgradeStateMachine.this.transitionTo(DJIUpgradeStateMachine.this.mCollectPackListState);
                    DJIUpgradeStateMachine.this.sendMessage(11);
                    return true;
                default:
                    return super.processMessage(msg);
            }
        }

        public void exit() {
            super.exit();
        }
    }

    private class ZipFailState extends UpErrorState {
        private ZipFailState() {
            super();
        }

        public void enter() {
            super.enter();
            DJIUpStatusHelper.setIsUpDownloading(false);
            if (DJIUpgradeStateMachine.this.upgradeListener != null) {
                DJIUpgradeStateMachine.this.upgradeListener.onZipFail();
            }
        }

        public boolean processMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    DJIUpgradeStateMachine.this.transitionTo(DJIUpgradeStateMachine.this.mZipState);
                    DJIUpgradeStateMachine.this.sendMessage(17);
                    return true;
                default:
                    return super.processMessage(msg);
            }
        }

        public void exit() {
            super.exit();
        }
    }

    class UploadFailState extends UpErrorState {
        UploadFailState() {
            super();
        }

        public void enter() {
            super.enter();
            DJIUpStatusHelper.setIsUpDownloading(false);
            DJIUpStatusHelper.setIsUpProgressing(false);
            DJIUpgradeStateMachine.this.tryCancelProgressTimer();
        }

        public boolean processMessage(Message msg) {
            return super.processMessage(msg);
        }

        public void exit() {
            super.exit();
        }
    }

    private class ProgressFailState extends UpErrorState {
        private ProgressFailState() {
            super();
        }

        public void enter() {
            super.enter();
            DJIUpStatusHelper.setIsUpProgressing(false);
        }

        public boolean processMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    DJIUpgradeStateMachine.this.uploadManager.init(true);
                    DJIUpgradeStateMachine.this.transitionTo(DJIUpgradeStateMachine.this.mUploadState);
                    DJIUpgradeStateMachine.this.sendMessage(20);
                    break;
            }
            return super.processMessage(msg);
        }

        public void exit() {
            super.exit();
        }
    }

    private class UploadFailInitState extends UploadFailState {
        private UploadFailInitState() {
            super();
        }

        public void enter() {
            super.enter();
            if (DJIUpgradeStateMachine.this.upgradeListener != null) {
                DJIUpgradeStateMachine.this.upgradeListener.onUploadFail(DJIUpgradeListener.UploadFailReason.Init, null);
            }
        }

        public boolean processMessage(Message msg) {
            int i = msg.what;
            return super.processMessage(msg);
        }

        public void exit() {
            super.exit();
        }
    }

    private class UploadFailEnterState extends UploadFailState {
        private UploadFailEnterState() {
            super();
        }

        public void enter() {
            super.enter();
            if (DJIUpgradeStateMachine.this.upgradeListener != null) {
                DJIUpgradeStateMachine.this.upgradeListener.onUploadFail(DJIUpgradeListener.UploadFailReason.Enter, DJIUpgradeStateMachine.this.uploadFailEnterCcode);
            }
        }

        public boolean processMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    DJIUpgradeStateMachine.this.transitionTo(DJIUpgradeStateMachine.this.mUploadState);
                    DJIUpgradeStateMachine.this.sendMessage(20, DJIUpgradeStateMachine.this.obtainMessage(20, 0, 0));
                    return true;
                default:
                    return super.processMessage(msg);
            }
        }

        public void exit() {
            super.exit();
        }
    }

    private class UploadFailPreTranState extends UploadFailState {
        private UploadFailPreTranState() {
            super();
        }

        public void enter() {
            super.enter();
            if (DJIUpgradeStateMachine.this.upgradeListener != null) {
                DJIUpgradeStateMachine.this.upgradeListener.onUploadFail(DJIUpgradeListener.UploadFailReason.Pretrans, null);
            }
        }

        public boolean processMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    DJIUpgradeStateMachine.this.transitionTo(DJIUpgradeStateMachine.this.mUploadState);
                    DJIUpgradeStateMachine.this.sendMessage(20, DJIUpgradeStateMachine.this.obtainMessage(20, 0, 0));
                    return true;
                default:
                    return super.processMessage(msg);
            }
        }

        public void exit() {
            super.exit();
        }
    }

    private class UploadFailTraningState extends UploadFailState {
        private UploadFailTraningState() {
            super();
        }

        public void enter() {
            super.enter();
            if (DJIUpgradeStateMachine.this.upgradeListener != null) {
                DJIUpgradeStateMachine.this.upgradeListener.onUploadFail(DJIUpgradeListener.UploadFailReason.Transing, null);
            }
        }

        public boolean processMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    DJIUpgradeStateMachine.this.transitionTo(DJIUpgradeStateMachine.this.mUploadState);
                    DJIUpgradeStateMachine.this.sendMessage(20, DJIUpgradeStateMachine.this.obtainMessage(20, 0, 0));
                    return true;
                default:
                    return super.processMessage(msg);
            }
        }

        public void exit() {
            super.exit();
        }
    }

    private class UploadFailQuitState extends UploadFailState {
        private UploadFailQuitState() {
            super();
        }

        public void enter() {
            super.enter();
        }

        public boolean processMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    DJIUpgradeStateMachine.this.transitionTo(DJIUpgradeStateMachine.this.mUploadState);
                    DJIUpgradeStateMachine.this.sendMessage(20, DJIUpgradeStateMachine.this.obtainMessage(20, 0, 0));
                    return true;
                case 108:
                    if (DJIUpgradeStateMachine.this.upgradeListener != null) {
                        DJIUpgradeStateMachine.this.upgradeListener.onUploadFail(DJIUpgradeListener.UploadFailReason.Quit, (Ccode) msg.obj);
                    }
                    return true;
                default:
                    return super.processMessage(msg);
            }
        }

        public void exit() {
            super.exit();
        }
    }

    /* access modifiers changed from: protected */
    public void recover() {
        sendMessage(0);
    }

    /* access modifiers changed from: protected */
    public void disconnected() {
        sendMessage(3);
    }

    /* access modifiers changed from: package-private */
    public void connectedP4() {
        IState state = getUpCurrentState();
        if (state != this.mDownloadState && state != this.mZipState && state != this.mUploadState && state != this.mListenProgressState) {
            sendMessage(1);
        }
    }

    /* access modifiers changed from: protected */
    public void connectedOther() {
        sendMessage(2);
    }

    /* access modifiers changed from: package-private */
    public void collectPackList(String reason) {
        DJIUpConstants.LOGE(TAG, "re collectPackList reason：" + reason);
        IState state = getUpCurrentState();
        if (state == this.mDownloadState || state == this.mZipState || state == this.mUploadState || state == this.mListenProgressState) {
            DJIUpConstants.LOGE(TAG, "re collectPackList failed state=" + state);
            return;
        }
        if (state != this.mCollectPackListState) {
            transitionTo(this.mCollectPackListState);
        }
        sendMessage(1000);
        sendMessage(11);
    }

    public void collectPackListComplete() {
        IState state = getUpCurrentState();
        if (state != null && (state instanceof UpErrorState)) {
            DJIUpConstants.LOGD(TAG, "collectPackListComplete but state=" + state);
            transitionTo(this.mCollectPackListState);
            sendMessage(1000);
        }
        sendMessage(13);
    }

    public void collectProductTypeComplete() {
        sendMessage(40);
    }

    public void collectPackDeviceComplete() {
        sendMessage(10);
    }

    public void collectPackListFail() {
        onCollectFail("not p4 series");
    }

    /* access modifiers changed from: package-private */
    public void startUpgrade() {
        DJIUpStatus status = DJIUpStatusHelper.getUpgradingStatus();
        if (status == null) {
            transitionTo(this.mUploadFailState);
        } else if (status.isUseMultiFile() || DJIUpStatusHelper.isUpgradeFromSDCard()) {
            if (getUpCurrentState() != this.mUploadState) {
                transitionTo(this.mUploadState);
                sendMessage(1000);
            }
            sendMessage(20);
        } else {
            if (getUpCurrentState() != this.mZipState) {
                transitionTo(this.mZipState);
                sendMessage(1000);
            }
            sendMessage(17);
        }
    }

    /* access modifiers changed from: protected */
    public void zipFailed() {
        sendMessage(18);
    }

    /* access modifiers changed from: protected */
    public void zipComplete() {
        sendMessage(19);
    }

    /* access modifiers changed from: package-private */
    public void uploadFailInit() {
        sendMessage(101);
    }

    /* access modifiers changed from: package-private */
    public void uploadFailEnter() {
        sendMessage(102);
    }

    /* access modifiers changed from: package-private */
    public void uploadFailEnter(Ccode ccode) {
        sendMessage(102, ccode);
    }

    /* access modifiers changed from: package-private */
    public void uploadFailPreTran() {
        sendMessage(104);
    }

    /* access modifiers changed from: package-private */
    public void uploadFailTraning() {
        sendMessage(106);
    }

    /* access modifiers changed from: package-private */
    public void uploadFailQuit(Ccode ccode) {
        sendMessage(obtainMessage(108, ccode));
    }

    /* access modifiers changed from: package-private */
    public void uploadNextPack(boolean isNeedNext) {
        if (getUpCurrentState() != this.mUploadState) {
            transitionTo(this.mUploadState);
            sendMessage(109, Boolean.valueOf(isNeedNext));
        }
        sendMessage(109, Boolean.valueOf(isNeedNext));
    }

    /* access modifiers changed from: package-private */
    public void uploadComplete() {
        sendMessage(22);
    }

    /* access modifiers changed from: package-private */
    public void listenProgressFail(DataCommonGetPushUpgradeStatus.DJIUpgradeCompleteReason completeReason) {
        IState state = getUpCurrentState();
        DJIUpConstants.LOGD(TAG, "listenProgressComplete state=" + state.getName());
        if (state != this.mListenProgressState) {
            transitionTo(this.mListenProgressState);
            sendMessage(24, completeReason);
        }
        sendMessage(24, completeReason);
    }

    /* access modifiers changed from: package-private */
    public void listenProgressComplete() {
        IState state = getUpCurrentState();
        DJIUpConstants.LOGD(TAG, "listenProgressComplete state=" + state.getName());
        if (state != this.mListenProgressState) {
            transitionTo(this.mListenProgressState);
            sendMessage(25);
        }
        sendMessage(25);
    }

    /* access modifiers changed from: package-private */
    public void reCoverListenProgress() {
        sendMessage(26);
    }

    /* access modifiers changed from: package-private */
    public void notifyUpgradePgs(String detail, int progress) {
        if (this.upgradeListener != null) {
            this.upgradeListener.onUpgradeProgress(detail, progress);
        }
    }

    public boolean isUpgrading() {
        IState state = getUpCurrentState();
        return (state == null || !(state instanceof UpDoingState) || state == this.mCollectPackListState) ? false : true;
    }

    public void timeout() {
        DJIUpStatusOfflineHelper.setDeviceLastUp(DJIUpStatusHelper.getUpgradingProductId());
        if (this.upgradeListener != null) {
            this.upgradeListener.onWaitTimeout();
        }
    }

    public void tryCancelProgressTimer() {
        if (this.upProgressManager != null) {
            this.upProgressManager.cancelTimer();
        }
    }

    /* access modifiers changed from: package-private */
    public void analogTellReason(DataCommonGetPushUpgradeStatus.DJIUpgradeCompleteReason reason) {
        if (this.upProgressManager != null) {
            this.upProgressManager.tellReason(reason);
        }
    }

    /* access modifiers changed from: private */
    public void onCollectFail(String s) {
        transitionTo(this.mCollectPackListFailState);
        DJIUpStatusHelper.setIsChecking(false, "DJIUpgradeStateMachine onCollectFail " + s);
        if (this.upgradeListener != null) {
            this.upgradeListener.onCollectFail(s);
        }
    }

    @Nullable
    private IState getUpCurrentState() {
        try {
            return getCurrentState();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
