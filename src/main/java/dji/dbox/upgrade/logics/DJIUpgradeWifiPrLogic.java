package dji.dbox.upgrade.logics;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import com.dji.frame.interfaces.V_CallBack;
import dji.dbox.upgrade.p4.constants.DJIUpConstants;
import dji.dbox.upgrade.p4.constants.DJIUpDeviceType;
import dji.dbox.upgrade.p4.model.DJIUpListElement;
import dji.dbox.upgrade.p4.server.DJIUpGetServerCfgManager;
import dji.dbox.upgrade.p4.utils.DJIUpStatusHelper;
import dji.dbox.upgrade.p4.utils.DJIUpStatusOfflineHelper;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.broadcastReceivers.DJINetWorkReceiver;
import dji.midware.data.manager.P3.ServiceManager;
import java.util.HashMap;
import java.util.List;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

@EXClassNullAway
public class DJIUpgradeWifiPrLogic {
    /* access modifiers changed from: private */
    public final String TAG = getClass().getSimpleName();
    private final V_CallBack callBack;
    private HashMap<String, DJIUpGetServerCfgManager> cfgManagerList;
    private final Context context;
    private volatile int count = 0;
    private volatile int failCount = 0;
    private Handler handler;
    private boolean isDoing = false;

    static /* synthetic */ int access$310(DJIUpgradeWifiPrLogic x0) {
        int i = x0.failCount;
        x0.failCount = i - 1;
        return i;
    }

    public DJIUpgradeWifiPrLogic(Context context2, V_CallBack callBack2) {
        this.context = context2;
        this.callBack = callBack2;
        this.handler = new Handler(new Handler.Callback() {
            /* class dji.dbox.upgrade.logics.DJIUpgradeWifiPrLogic.AnonymousClass1 */

            public boolean handleMessage(Message msg) {
                switch (msg.what) {
                    case 0:
                        DJIUpgradeWifiPrLogic.this.startCollect();
                        return false;
                    default:
                        return false;
                }
            }
        });
        DJIUpStatusOfflineHelper.init(context2);
        if (!ServiceManager.getInstance().isConnected()) {
            if (DJINetWorkReceiver.getNetWorkStatusNoPing(context2)) {
                this.handler.sendEmptyMessageDelayed(0, 500);
            } else if (DJIUpStatusOfflineHelper.isOfflineEnableMode(context2)) {
                DJIUpConstants.LOGE_WIFI(this.TAG, "isOfflineEnableMode true callBack.exec()");
                callBack2.exec();
            } else {
                DJIUpConstants.LOGE_WIFI(this.TAG, "isOfflineEnableMode false");
            }
        }
        EventBus.getDefault().register(this);
    }

    public void onDestroy() {
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEvent3BackgroundThread(DJINetWorkReceiver.DJINetWorkStatusEvent event) {
        switch (event) {
            case CONNECT_OK:
                if (DJINetWorkReceiver.isWifiConnected(this.context)) {
                    this.handler.removeMessages(0);
                    this.handler.sendEmptyMessageDelayed(0, 500);
                    return;
                }
                return;
            default:
                stopCollect();
                return;
        }
    }

    public void reDo() {
        stopCollect();
        startCollect();
    }

    private void stopCollect() {
        if (this.cfgManagerList != null && this.cfgManagerList.size() > 0) {
            for (String pid : this.cfgManagerList.keySet()) {
                this.cfgManagerList.get(pid).stop();
            }
            this.isDoing = false;
        }
    }

    /* access modifiers changed from: private */
    public void startCollect() {
        DJIUpGetServerCfgManager cfgManager;
        if (this.isDoing) {
            DJIUpConstants.LOGE_WIFI(this.TAG, "正在执行中，跳过固件网络缓存");
        } else if (DJIUpStatusHelper.isUpDownloading() || DJIUpStatusHelper.isUpProgressing()) {
            DJIUpConstants.LOGE_WIFI(this.TAG, "正在下载或升级，跳过固件网络缓存");
        } else {
            DJIUpConstants.LOGE_WIFI(this.TAG, "startCollect");
            this.isDoing = true;
            if (this.cfgManagerList == null) {
                this.cfgManagerList = new HashMap<>();
            }
            this.count = 0;
            this.failCount = 0;
            DJIUpDeviceType firstDevice = DJIUpStatusOfflineHelper.getDevice1();
            DJIUpDeviceType secondDevice = DJIUpStatusOfflineHelper.getDevice2();
            DJIUpDeviceType[] values = DJIUpDeviceType.values();
            for (DJIUpDeviceType wifiPr : values) {
                if (wifiPr.isOffline() || wifiPr == firstDevice || wifiPr == secondDevice) {
                    DJIUpConstants.LOGE_WIFI(this.TAG, "start offline collect device=" + wifiPr);
                    this.count++;
                    final String pid = wifiPr.toString();
                    if (!this.cfgManagerList.containsKey(pid)) {
                        cfgManager = new DJIUpGetServerCfgManager(this.context, pid);
                        this.cfgManagerList.put(pid, cfgManager);
                    } else {
                        cfgManager = this.cfgManagerList.get(pid);
                    }
                    cfgManager.setIsOffline(wifiPr.isOffline());
                    cfgManager.setListener(new DJIUpGetServerCfgManager.DJIUpGetServerCfgListener() {
                        /* class dji.dbox.upgrade.logics.DJIUpgradeWifiPrLogic.AnonymousClass2 */

                        public void onGetSuccess(List<DJIUpListElement> list) {
                            DJIUpgradeWifiPrLogic.this.monitor();
                            DJIUpConstants.LOGE_WIFI(DJIUpgradeWifiPrLogic.this.TAG, "WifiPrLogic getSuc pid=" + pid);
                        }

                        public void onGetFail(String s) {
                            DJIUpgradeWifiPrLogic.access$310(DJIUpgradeWifiPrLogic.this);
                            DJIUpgradeWifiPrLogic.this.monitor();
                            DJIUpConstants.LOGE_WIFI(DJIUpgradeWifiPrLogic.this.TAG, "WifiPrLogic getFail pid=" + pid + " reason:" + s);
                        }
                    });
                    cfgManager.start();
                }
            }
        }
    }

    /* access modifiers changed from: private */
    public void monitor() {
        synchronized (this) {
            this.count--;
        }
        if (this.count == 0) {
            DJIUpConstants.LOGE_WIFI(this.TAG, "isOfflineEnableMode false callBack.exec()");
            this.callBack.exec();
            this.isDoing = false;
        }
    }
}
