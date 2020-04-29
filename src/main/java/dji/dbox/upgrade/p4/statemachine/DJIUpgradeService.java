package dji.dbox.upgrade.p4.statemachine;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Keep;
import android.util.Log;
import com.dji.frame.interfaces.V_CallBack;
import dji.dbox.upgrade.logics.DJIGetNewestGlassVersionLogic;
import dji.dbox.upgrade.logics.DJIUpgradeWifiPrLogic;
import dji.dbox.upgrade.p4.constants.DJIUpConstants;
import dji.dbox.upgrade.p4.constants.DJIUpNotifyEvent;
import dji.dbox.upgrade.p4.utils.DJIUpGlassUtil;
import dji.dbox.upgrade.p4.utils.DJIUpStatusHelper;
import dji.dbox.upgrade.p4.utils.DJIUpStatusOfflineHelper;
import dji.dbox.upgrade.strategy.UpgradeStrategyContext;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.broadcastReceivers.DJINetWorkReceiver;
import dji.midware.data.manager.P3.DJIConnectType;
import dji.midware.data.manager.P3.DJIPackManager;
import dji.midware.data.manager.P3.DataCameraEvent;
import dji.midware.data.manager.P3.DataEvent;
import dji.midware.data.manager.P3.ServiceManager;
import dji.midware.data.model.P3.DataNotifyDisconnect;
import dji.midware.link.DJILinkDaemonService;
import dji.midware.link.DJILinkType;
import dji.midware.util.DJIEventBusUtil;
import java.lang.ref.SoftReference;
import java.util.Timer;
import java.util.TimerTask;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

@Keep
@EXClassNullAway
public class DJIUpgradeService {
    private static volatile DJIUpgradeService INSTANCE = null;
    private static final int MSG_COLLECT = 5;
    private static final int MSG_COLLECT_DEVICE_INFO = 4;
    private static final int MSG_GET_GLASS_VERSION = 3;
    private static final int MSG_INITIALIZED = 0;
    private static final int REMOUNT_CONNECT = 1;
    private static final int REMOUNT_DISCONNECT = 2;
    private static final String TAG = "DJIUpgradeService";
    /* access modifiers changed from: private */
    public static boolean isCancelDisTimer = false;
    static int timeOut = 90;
    private Timer disTimer;
    /* access modifiers changed from: private */
    public DJIGetNewestGlassVersionLogic getNewestGlassVersionLogic;
    /* access modifiers changed from: private */
    public Handler handler;
    private boolean initialized;
    int second = 0;
    /* access modifiers changed from: private */
    public UpgradeStrategyContext strategyContext;
    /* access modifiers changed from: private */
    public DJIUpgradeP4Manager upgradeP4Manager;
    private DJIUpgradeWifiPrLogic wifiPrLogic;

    @Keep
    public enum DJIUpP4Event {
        ConnectP4MC,
        ConnectP4RC,
        ConnectOther,
        Disconnect
    }

    public static void cancelTimer() {
        isCancelDisTimer = true;
    }

    private synchronized void createUpgradeManager(Context context, UpgradeStrategyContext strategyContext2) {
        if (this.upgradeP4Manager == null) {
            DJIUpStatusHelper.initTimes++;
            EventBus.getDefault().post(DJIUpNotifyEvent.Init);
            this.upgradeP4Manager = new DJIUpgradeP4Manager(context.getApplicationContext(), strategyContext2);
            this.upgradeP4Manager.init();
        }
    }

    public DJIUpgradeP4Manager getUpgradeManager() {
        return this.upgradeP4Manager;
    }

    private DJIUpgradeService() {
    }

    public static DJIUpgradeService getInstance() {
        if (INSTANCE == null) {
            synchronized (DJIUpgradeService.class) {
                if (INSTANCE == null) {
                    INSTANCE = new DJIUpgradeService();
                }
            }
        }
        return INSTANCE;
    }

    private static class UpHandler extends Handler {
        private SoftReference<DJIUpgradeService> sReference;

        UpHandler(DJIUpgradeService service) {
            this.sReference = new SoftReference<>(service);
        }

        public void handleMessage(Message msg) {
            if (this.sReference.get() != null) {
                DJIUpgradeService service = this.sReference.get();
                switch (msg.what) {
                    case 0:
                        service.innerInitialize((Context) msg.obj);
                        return;
                    case 1:
                        DJIUpConstants.LOGE(DJIUpgradeService.TAG, "********check*******mc connect");
                        service.resetDisTimer();
                        service.handler.removeMessages(4);
                        if (ServiceManager.getInstance().isConnected()) {
                            service.handler.sendEmptyMessageDelayed(4, 500);
                            return;
                        }
                        return;
                    case 2:
                        DJIUpConstants.LOGE(DJIUpgradeService.TAG, "mc disconnect");
                        service.disconnect();
                        return;
                    case 3:
                        if (!DJIUpStatusHelper.isUpProgressing()) {
                            DJIUpConstants.LOGE(DJIUpgradeService.TAG, "MSG_GET_GLASS_VERSION isGlassConnected = " + DJIUpGlassUtil.isGlassConnected() + " glassType = " + DJIUpGlassUtil.getGlassType());
                            if (DJIUpGlassUtil.isUpgradeSame()) {
                                DJIUpConstants.LOGE(DJIUpgradeService.TAG, "to get glass version");
                                service.getNewestGlassVersionLogic.getGlassNewestVersion();
                                return;
                            }
                            return;
                        }
                        return;
                    case 4:
                        DJIUpConstants.LOGE(DJIUpgradeService.TAG, "********check*******collectDeviceInfo isCheckingSetted=" + DJIUpStatusHelper.isCheckingSetted() + " isChecking=" + DJIUpStatusHelper.isChecking());
                        service.strategyContext.collectDeviceInfo();
                        return;
                    case 5:
                        if (!ServiceManager.getInstance().isConnected()) {
                            DJIUpConstants.LOGE(DJIUpgradeService.TAG, "********check*******reCollect for network but not connected");
                            return;
                        }
                        DJIUpConstants.LOGE(DJIUpgradeService.TAG, "********check*******reCollect for network");
                        if (service.upgradeP4Manager != null) {
                            service.upgradeP4Manager.reCollect("network status changed");
                            return;
                        }
                        return;
                    default:
                        return;
                }
            }
        }
    }

    public void onCreate(Context context) {
        if (this.initialized) {
            DJIUpConstants.LOGD(TAG, "DJIUpgradeService initialized exception->repeated");
            return;
        }
        this.handler = new UpHandler(this);
        this.handler.sendMessage(this.handler.obtainMessage(0, context.getApplicationContext()));
    }

    /* access modifiers changed from: private */
    public synchronized void innerInitialize(Context context) {
        if (context == null) {
            DJIUpConstants.LOGD(TAG, "DJIUpgradeService initialized exception->context is null");
        } else if (this.initialized) {
            DJIUpConstants.LOGD(TAG, "DJIUpgradeService initialized exception->repeated 2");
        } else {
            DJIUpConstants.LOGD(TAG, "DJIUpgradeService start initialize");
            this.strategyContext = new UpgradeStrategyContext();
            createUpgradeManager(context, this.strategyContext);
            this.getNewestGlassVersionLogic = new DJIGetNewestGlassVersionLogic(context);
            if (ServiceManager.getInstance().isConnected()) {
                DJIUpConstants.LOGE(TAG, "onCreate startDeamonTimer");
                this.handler.removeMessages(4);
                this.handler.sendEmptyMessageDelayed(4, 500);
            } else {
                DJIUpConstants.LOGE(TAG, "onCreate disconnect");
                disconnect();
            }
            DJIEventBusUtil.register(this);
            this.wifiPrLogic = new DJIUpgradeWifiPrLogic(context, new V_CallBack() {
                /* class dji.dbox.upgrade.p4.statemachine.DJIUpgradeService.AnonymousClass1 */

                public void exec() {
                    DJIUpgradeService.this.handler.postDelayed(new Runnable() {
                        /* class dji.dbox.upgrade.p4.statemachine.DJIUpgradeService.AnonymousClass1.AnonymousClass1 */

                        public void run() {
                            DJIUpConstants.LOGD(DJIUpgradeService.TAG, "check offline device " + DJIUpStatusOfflineHelper.getDevice1() + " " + DJIUpStatusOfflineHelper.getDevice2());
                            if (ServiceManager.getInstance().isConnected() || !DJIUpStatusOfflineHelper.isNeedCompareOffline()) {
                                DJIUpConstants.LOGE(DJIUpgradeService.TAG, "can`t exec collectDeviceInfoOffline");
                                return;
                            }
                            DJIUpConstants.LOGE(DJIUpgradeService.TAG, "********check*******exec collectDeviceInfoOffline");
                            if (DJIUpStatusHelper.isUnderSyncUpgradeContext()) {
                                DJIUpConstants.LOGE(DJIUpgradeService.TAG, "can`t exec collectDeviceInfoOffline->isUnderSyncUpgradeContext");
                            } else {
                                DJIUpgradeService.this.strategyContext.collectDeviceInfoOffline();
                            }
                        }
                    }, 600);
                }
            });
            this.initialized = true;
        }
    }

    public void reDo(String tag) {
        if (tag.contains("login")) {
            EventBus.getDefault().post(UpgradeReDoEvent.ReDo);
        }
        if (!DJIUpStatusHelper.isAllowRedo() || DJIUpStatusHelper.isUpProgressing() || DJIUpStatusHelper.isUpDownloading()) {
            DJIUpConstants.LOGE("reDo", "redo for " + tag + ".............but uping or notallow");
            return;
        }
        DJIUpConstants.LOGE("reDo", "********check******* redo for " + tag + ".....................");
        if (this.upgradeP4Manager != null) {
            this.upgradeP4Manager.reCollect(tag);
        }
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEvent3BackgroundThread(UpgradeReDoEvent event) {
        switch (event) {
            case ReDo:
                this.wifiPrLogic.reDo();
                this.handler.removeMessages(3);
                this.handler.sendEmptyMessageDelayed(3, 1000);
                return;
            case RedoFromSetting:
                reDo("");
                return;
            default:
                return;
        }
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEvent3BackgroundThread(DJINetWorkReceiver.DJINetWorkStatusEvent event) {
        switch (event) {
            case CONNECT_OK:
                DJIUpConstants.LOGE(TAG, "********check*******DJINetWorkStatusEvent event=" + event + " isChecking=" + DJIUpStatusHelper.isChecking());
                if (!DJIUpStatusHelper.isChecking() && !DJIUpStatusHelper.isUnderSyncUpgradeContext() && !isUpgrading()) {
                    this.handler.removeMessages(3);
                    this.handler.sendEmptyMessageDelayed(3, 1000);
                    this.handler.removeMessages(5);
                    this.handler.sendEmptyMessageDelayed(5, 1000);
                    return;
                }
                return;
            default:
                return;
        }
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEvent3BackgroundThread(DataEvent event) {
        switch (event) {
            case ConnectOK:
                this.handler.removeMessages(3);
                this.handler.sendEmptyMessageDelayed(3, 1000);
                DJIUpConstants.LOGE(TAG, "********check*******DataEvent ok isUpProgressing=" + DJIUpStatusHelper.isUpProgressing() + " isUpgrading=" + isUpgrading());
                if (!isUpgrading() && !DJIUpStatusHelper.isUnderSyncUpgradeContext()) {
                    resetDisTimer();
                    this.handler.removeMessages(5);
                    this.handler.removeMessages(1);
                    this.handler.removeMessages(4);
                    this.handler.sendEmptyMessageDelayed(4, 500);
                    return;
                }
                return;
            case ConnectLose:
                DJIUpConstants.LOGE(TAG, "********check*******DataEvent lose");
                disconnect();
                return;
            default:
                return;
        }
    }

    private boolean isUpgrading() {
        return DJIUpStatusHelper.isUpProgressing() || (this.upgradeP4Manager != null && this.upgradeP4Manager.isUpgrading());
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEvent3BackgroundThread(DataCameraEvent event) {
        DJILinkType linkType = DJILinkDaemonService.getInstance().getLinkType();
        if ((DJIUpStatusHelper.isNewUpgradeSystem() || linkType == DJILinkType.HOSTRC || linkType == DJILinkType.USB_WIFI) && !DJIUpStatusHelper.isUpProgressing() && !DJIUpStatusHelper.isUpDownloading() && !DJIUpStatusHelper.isUnderSyncUpgradeContext()) {
            switch (event) {
                case ConnectOK:
                    this.handler.removeMessages(5);
                    this.handler.removeMessages(2);
                    this.handler.removeMessages(1);
                    this.handler.removeMessages(4);
                    DJIUpConstants.LOGE(TAG, "********check*******DataCameraEvent ok but need reCollect info");
                    this.handler.sendEmptyMessageDelayed(1, 1000);
                    return;
                case ConnectLose:
                    this.handler.removeMessages(5);
                    this.handler.removeMessages(1);
                    this.handler.removeMessages(4);
                    DJIUpConstants.LOGE(TAG, "********check*******DataCameraEvent lose but need reCollect info isConnected=" + ServiceManager.getInstance().isConnected());
                    if (ServiceManager.getInstance().isConnected()) {
                        this.handler.sendEmptyMessageDelayed(1, 1000);
                        return;
                    }
                    return;
                default:
                    return;
            }
        } else {
            DJIUpConstants.LOGE(TAG, "when check mc is connected , " + event + " isupprogressing=" + DJIUpStatusHelper.isUpProgressing());
        }
    }

    /* access modifiers changed from: private */
    public void resetDisTimer() {
        this.second = 0;
        if (this.disTimer != null) {
            this.disTimer.cancel();
            this.disTimer = null;
        }
    }

    /* access modifiers changed from: private */
    public void stopProgress() {
        DJIUpStatusHelper.setIsUpProgressing(false);
        DJIUpConstants.LOGE(TAG, "disconnect");
        resetStatus();
        if (!ServiceManager.getInstance().isConnected()) {
            this.strategyContext.disConnect();
        }
        resetDisTimer();
    }

    /* access modifiers changed from: private */
    public void disconnect() {
        if (DJIUpStatusHelper.isUpProgressing()) {
            DJIUpConstants.LOGE(TAG, "升级中重启的特殊处理 延时60s结束升级状态");
            resetDisTimer();
            if (!DataNotifyDisconnect.getInstance().isGetted()) {
                DJIUpConstants.LOGE(TAG, "1860重启的推送没获取到，按90秒超时计算");
                EventBus.getDefault().post(DataNotifyDisconnect.getInstance());
            }
            isCancelDisTimer = false;
            this.disTimer = new Timer("disTimer");
            this.disTimer.schedule(new TimerTask() {
                /* class dji.dbox.upgrade.p4.statemachine.DJIUpgradeService.AnonymousClass2 */

                public void run() {
                    DJIUpConstants.LOGE(DJIUpgradeService.TAG, "disconnect second=" + DJIUpgradeService.this.second + " isUpProgressing=" + DJIUpStatusHelper.isUpProgressing());
                    if (DJIUpgradeService.isCancelDisTimer) {
                        DJIUpgradeService.this.resetDisTimer();
                        return;
                    }
                    if (DJIUpgradeService.this.second == DJIUpgradeService.timeOut) {
                        DJIUpgradeService.this.upgradeP4Manager.timeout();
                        DJIUpgradeService.this.stopProgress();
                    } else if (!DJIUpStatusHelper.isUpProgressing()) {
                        DJIUpgradeService.this.stopProgress();
                    }
                    DJIUpgradeService.this.second++;
                }
            }, 0, 1000);
            return;
        }
        DJIUpConstants.LOGE(TAG, "disconnect");
        resetStatus();
        this.strategyContext.disConnect();
        DJIPackManager.getInstance().setConnectType(DJIConnectType.IDLE);
    }

    private void resetStatus() {
        this.strategyContext.reset();
        DJIUpStatusHelper.resetStatus();
    }

    public void onDestroy() {
        Log.e(TAG, "onDestroy");
        DJIEventBusUtil.unRegister(this);
        resetStatus();
        resetDisTimer();
        this.upgradeP4Manager = null;
        if (this.wifiPrLogic != null) {
            this.wifiPrLogic.onDestroy();
            this.wifiPrLogic = null;
        }
    }
}
