package dji.midware.upgradeComponent;

import android.support.annotation.NonNull;
import dji.component.accountcenter.IMemberProtocol;
import dji.log.DJILog;
import dji.midware.data.config.P3.Ccode;
import dji.midware.data.config.P3.DeviceType;
import dji.midware.data.manager.P3.DataCameraEvent;
import dji.midware.data.manager.P3.DataEvent;
import dji.midware.data.manager.P3.ServiceManager;
import dji.midware.data.model.P3.DataCommonGetDeviceInfo;
import dji.midware.data.model.P3.DataCommonGetVersion;
import dji.midware.interfaces.DJIDataCallBack;
import dji.midware.util.BackgroundLooper;
import dji.midware.util.DJIEventBusUtil;
import java.util.Locale;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class DJIUpgradeComponentManager {
    private static final int DRONE_ACQUIRE_RETRY_TIMES = 20;
    private static final int RC_ACQUIRE_RETRY_TIMES = 20;
    private static final String TAG = "DJIUpgradeComponentManager";
    private static final int TIME_OUT_MSEC = 1000;
    /* access modifiers changed from: private */
    public DJIUpgradeModelType droneModelType;
    private DataCommonGetVersion getDeviceInfo;
    /* access modifiers changed from: private */
    public DataCommonGetDeviceInfo getDm368Info;
    /* access modifiers changed from: private */
    public DJIUpgradeModelType rcModelType;
    private Runnable updateTask;
    private DJIUpgradeComponent upgradeComponent;

    interface ModelTypeCallback {
        void onCompleted();
    }

    private static class SingletonHolder {
        /* access modifiers changed from: private */
        public static final DJIUpgradeComponentManager INSTANCE = new DJIUpgradeComponentManager();

        private SingletonHolder() {
        }
    }

    private DJIUpgradeComponentManager() {
        this.updateTask = new Runnable() {
            /* class dji.midware.upgradeComponent.DJIUpgradeComponentManager.AnonymousClass1 */

            public void run() {
                DJIUpgradeComponentManager.this.getModelType();
            }
        };
        this.getDeviceInfo = new DataCommonGetVersion();
        this.getDm368Info = new DataCommonGetDeviceInfo();
        this.rcModelType = DJIUpgradeModelType.Unknown;
        this.droneModelType = DJIUpgradeModelType.Unknown;
        this.upgradeComponent = new DJIUpgradeComponent(this.droneModelType, this.rcModelType);
    }

    public static final DJIUpgradeComponentManager getInstance() {
        return SingletonHolder.INSTANCE;
    }

    @NonNull
    public DJIUpgradeComponent getUpgradeComponent() {
        return this.upgradeComponent;
    }

    public void init() {
        updateValueDelay();
        DJIEventBusUtil.register(this);
    }

    public void destroy() {
        DJIEventBusUtil.unRegister(this);
        BackgroundLooper.remove(this.updateTask);
    }

    private void updateValueDelay() {
        BackgroundLooper.remove(this.updateTask);
        BackgroundLooper.postDelayed(this.updateTask, 400);
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEvent3BackgroundThread(DataCameraEvent event) {
        updateValueDelay();
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEvent3BackgroundThread(DataEvent event) {
        updateValueDelay();
    }

    /* access modifiers changed from: private */
    public void getModelType() {
        getRcModelType(new ModelTypeCallback() {
            /* class dji.midware.upgradeComponent.DJIUpgradeComponentManager.AnonymousClass2 */

            public void onCompleted() {
                DJIUpgradeComponentManager.this.getDroneModelType(new ModelTypeCallback() {
                    /* class dji.midware.upgradeComponent.DJIUpgradeComponentManager.AnonymousClass2.AnonymousClass1 */

                    public void onCompleted() {
                        DJIUpgradeComponentManager.this.notifyComponentTypeChanged();
                    }
                });
            }
        });
    }

    private void getRcModelType(final ModelTypeCallback callBack) {
        LOGD(TAG, "Rc Model Type serviceManager isOK? " + ServiceManager.getInstance().isOK());
        if (ServiceManager.getInstance().isOK()) {
            this.getDeviceInfo.setDeviceType(DeviceType.BROADCAST);
            this.getDeviceInfo.setDeviceModel(0);
            this.getDeviceInfo.start(new DJIDataCallBack() {
                /* class dji.midware.upgradeComponent.DJIUpgradeComponentManager.AnonymousClass3 */

                public void onSuccess(Object model) {
                    String info = ((DataCommonGetVersion) model).getHardwareVer().toLowerCase(Locale.ENGLISH);
                    DJIUpgradeModelType unused = DJIUpgradeComponentManager.getInstance().rcModelType = DJIUpgradeModelType.getDJIUpgradeModelTypeFromRCInfo(info);
                    DJIUpgradeComponentManager.LOGD(DJIUpgradeComponentManager.TAG, "Get RcModelType success:info:" + info + "-name:" + DJIUpgradeComponentManager.this.rcModelType.name());
                    if (callBack != null) {
                        callBack.onCompleted();
                    }
                }

                public void onFailure(Ccode ccode) {
                    DJIUpgradeComponentManager.LOGD(DJIUpgradeComponentManager.TAG, "Get rcModelType failure:" + ccode);
                    if (callBack != null) {
                        callBack.onCompleted();
                    }
                }
            }, 1000, 20);
        } else if (callBack != null) {
            callBack.onCompleted();
        }
    }

    /* access modifiers changed from: private */
    public void getDroneModelType(final ModelTypeCallback callBack) {
        LOGD(TAG, "Drone Model Type: serviceManager is remote OK? " + ServiceManager.getInstance().isRemoteOK());
        if (ServiceManager.getInstance().isRemoteOK()) {
            this.getDm368Info.setReceiveType(DeviceType.DM368);
            this.getDm368Info.setReceiveId(1);
            this.getDm368Info.start(new DJIDataCallBack() {
                /* class dji.midware.upgradeComponent.DJIUpgradeComponentManager.AnonymousClass4 */

                public void onSuccess(Object model) {
                    String info = DJIUpgradeComponentManager.this.getDm368Info.getInfo().toLowerCase(Locale.ENGLISH);
                    DJIUpgradeModelType unused = DJIUpgradeComponentManager.getInstance().droneModelType = DJIUpgradeModelType.getDJIUpgradeModelTypeFromDroneInfo(info);
                    DJIUpgradeComponentManager.LOGD(DJIUpgradeComponentManager.TAG, "Get droneModelType success:info:" + info + "-name:" + DJIUpgradeComponentManager.this.droneModelType.name());
                    if (callBack != null) {
                        callBack.onCompleted();
                    }
                }

                public void onFailure(Ccode ccode) {
                    DJIUpgradeComponentManager.LOGD(DJIUpgradeComponentManager.TAG, "Get droneModelType failure:" + ccode);
                    if (callBack != null) {
                        callBack.onCompleted();
                    }
                }
            }, 1000, 20);
        } else if (callBack != null) {
            callBack.onCompleted();
        }
    }

    /* access modifiers changed from: private */
    public void notifyComponentTypeChanged() {
        Object rcType;
        Object fcType;
        DJIUpgradeComponent curUpgradeComponent = new DJIUpgradeComponent(this.droneModelType, this.rcModelType);
        StringBuilder append = new StringBuilder().append("curUpgradeComponent rcModelType:").append(curUpgradeComponent.getRcType()).append("--curUpgradeComponent droneModelType:").append(curUpgradeComponent.getFcType()).append("--preUpgradeComponent rcModelType:");
        if (this.upgradeComponent == null) {
            rcType = "null";
        } else {
            rcType = this.upgradeComponent.getRcType();
        }
        StringBuilder append2 = append.append(rcType).append("--preUpgradeComponent droneModelType:");
        if (this.upgradeComponent == null) {
            fcType = "null";
        } else {
            fcType = this.upgradeComponent.getFcType();
        }
        LOGD(TAG, append2.append(fcType).toString());
        if (!curUpgradeComponent.equals(this.upgradeComponent)) {
            this.upgradeComponent = curUpgradeComponent;
            EventBus.getDefault().post(this.upgradeComponent);
        }
    }

    public static void LOGD(String TAG2, String log) {
        DJILog.logWriteD("MSDK-Android", IMemberProtocol.STRING_SEPERATOR_LEFT + TAG2 + "] " + log, "UpgradeLogSDK", new Object[0]);
    }
}
