package dji.midware.component.battery;

import dji.log.DJILog;
import dji.midware.data.config.P3.Ccode;
import dji.midware.data.manager.P3.DataCameraEvent;
import dji.midware.data.model.P3.DataSmartBatteryGetStaticData;
import dji.midware.interfaces.DJIDataCallBack;
import dji.midware.link.DJILinkDaemonService;
import dji.midware.link.DJILinkType;
import dji.midware.util.DJIEventBusUtil;
import dji.midware.util.RepeatDataBase;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class DJIBatteryDetectHelper {
    /* access modifiers changed from: private */
    public static final String TAG = DJIBatteryDetectHelper.class.getSimpleName();
    /* access modifiers changed from: private */
    public DataSmartBatteryGetStaticData smartBatteryStaticDataGetter;

    private static class SingletonHolder {
        /* access modifiers changed from: private */
        public static final DJIBatteryDetectHelper INSTANCE = new DJIBatteryDetectHelper();

        private SingletonHolder() {
        }
    }

    private DJIBatteryDetectHelper() {
    }

    public static final DJIBatteryDetectHelper getInstance() {
        return SingletonHolder.INSTANCE;
    }

    public void init() {
        DJIEventBusUtil.register(this);
        if (DJILinkDaemonService.getInstance().getLinkType() == DJILinkType.NON) {
            onEvent3BackgroundThread(DataCameraEvent.ConnectLose);
        } else {
            onEvent3BackgroundThread(DataCameraEvent.ConnectOK);
        }
    }

    public void destroy() {
        DJIEventBusUtil.unRegister(this);
    }

    public DataSmartBatteryGetStaticData getSmartBatteryStaticDataGetter() {
        return this.smartBatteryStaticDataGetter;
    }

    public DataSmartBatteryGetStaticData.SmartBatteryType getSmartBatteryType() {
        if (this.smartBatteryStaticDataGetter == null) {
            return null;
        }
        return DataSmartBatteryGetStaticData.SmartBatteryType.find(((Integer) this.smartBatteryStaticDataGetter.get(40, 1, Integer.class)).intValue());
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEvent3BackgroundThread(DataCameraEvent event) {
        if (event == DataCameraEvent.ConnectLose) {
            destroySmartBatteryStaticDataGetter();
        } else {
            initSmartBatteryStaticDataGetter();
        }
    }

    private void initSmartBatteryStaticDataGetter() {
        if (this.smartBatteryStaticDataGetter == null) {
            final DataSmartBatteryGetStaticData staticDataTemp = new DataSmartBatteryGetStaticData();
            staticDataTemp.setIndex(0);
            new RepeatDataBase(staticDataTemp, 6, 500, new DJIDataCallBack() {
                /* class dji.midware.component.battery.DJIBatteryDetectHelper.AnonymousClass1 */

                public void onSuccess(Object model) {
                    DataSmartBatteryGetStaticData unused = DJIBatteryDetectHelper.this.smartBatteryStaticDataGetter = staticDataTemp;
                    EventBus.getDefault().post(DJIBatteryDetectHelper.this);
                }

                public void onFailure(Ccode ccode) {
                    DJILog.d(DJIBatteryDetectHelper.TAG, "osdGetter fails : " + ccode, new Object[0]);
                }
            }).start();
        }
    }

    private void destroySmartBatteryStaticDataGetter() {
        if (this.smartBatteryStaticDataGetter != null) {
            this.smartBatteryStaticDataGetter = null;
            EventBus.getDefault().post(this);
        }
    }
}
