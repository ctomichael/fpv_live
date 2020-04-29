package dji.event3.apppublic;

import com.dji.frame.util.V_AppUtils;
import dji.midware.data.config.P3.ProductType;
import dji.midware.data.manager.P3.DJIProductManager;
import dji.midware.data.manager.P3.DataCameraEvent;
import dji.midware.data.manager.P3.DataEvent;
import dji.midware.data.model.P3.DataCameraGetPushUpgradeStatus;
import dji.midware.data.model.P3.DataCenterGetPushBatteryCommon;
import dji.midware.data.model.P3.DataFlycGetPushSmartBattery;
import dji.midware.data.model.P3.DataFlycPushRedundancyStatus;
import dji.midware.data.model.P3.DataOsdGetPushCommon;
import dji.midware.data.model.P3.DataSmartBatteryGetPushCellVoltage;
import dji.midware.data.model.P3.DataSmartBatteryGetPushDynamicData;
import dji.midware.parser.plugins.DJIPluginRingBufferAsyncParser;
import dji.pilot.battery.control.BatteryManager;
import dji.pilot.battery.control.DJIBatteryHistoryManager;
import dji.pilot.fpv.control.DJIHDDelayNotifyManager;
import dji.pilot.fpv.control.DJIRedundancySysController;
import dji.pilot.fpv.control.DJIWifiController;
import dji.pilot.fpv.control.JoystickController;
import dji.pilot.fpv.model.IEventObjects;
import dji.pilot.publics.control.rc.DJIRcUpgradeManager;
import dji.publics.activity.DJIFragmentActivityNoFullScreen;
import dji.publics.widget.DJIBaseDialog;
import java.util.HashMap;
import java.util.Map;
import org.greenrobot.eventbus.ThreadMode;
import org.greenrobot.eventbus.meta.SimpleSubscriberInfo;
import org.greenrobot.eventbus.meta.SubscriberInfo;
import org.greenrobot.eventbus.meta.SubscriberInfoIndex;
import org.greenrobot.eventbus.meta.SubscriberMethodInfo;

public class DJIEventBusIndex implements SubscriberInfoIndex {
    private static final Map<Class<?>, SubscriberInfo> SUBSCRIBER_INDEX = new HashMap();

    static {
        putIndex(new SimpleSubscriberInfo(DJIRcUpgradeManager.class, true, new SubscriberMethodInfo[]{new SubscriberMethodInfo("onEvent3MainThread", DataEvent.class, ThreadMode.MAIN, 100, false), new SubscriberMethodInfo("onEvent3MainThread", DJIProductManager.DJIProductRcEvent.class, ThreadMode.MAIN, 100, false), new SubscriberMethodInfo("onEvent3MainThread", IEventObjects.DJIUpgradeStatus.class, ThreadMode.MAIN, 100, false)}));
        putIndex(new SimpleSubscriberInfo(DJIHDDelayNotifyManager.class, true, new SubscriberMethodInfo[]{new SubscriberMethodInfo("onEvent3BackgroundThread", DJIPluginRingBufferAsyncParser.AOA_BUFFER_EVENT.class, ThreadMode.MAIN)}));
        putIndex(new SimpleSubscriberInfo(DJIBaseDialog.class, true, new SubscriberMethodInfo[]{new SubscriberMethodInfo("onEvent3MainThread", V_AppUtils.DJI_SYS_UI_EVENT.class, ThreadMode.MAIN)}));
        putIndex(new SimpleSubscriberInfo(JoystickController.class, true, new SubscriberMethodInfo[]{new SubscriberMethodInfo("onEvent3BackgroundThread", JoystickController.WifiStickModeChanged.class, ThreadMode.BACKGROUND), new SubscriberMethodInfo("onEvent3BackgroundThread", DataCameraEvent.class, ThreadMode.BACKGROUND)}));
        putIndex(new SimpleSubscriberInfo(DJIBatteryHistoryManager.class, true, new SubscriberMethodInfo[]{new SubscriberMethodInfo("onEvent3MainThread", DataCameraEvent.class, ThreadMode.MAIN)}));
        putIndex(new SimpleSubscriberInfo(DJIWifiController.class, true, new SubscriberMethodInfo[]{new SubscriberMethodInfo("onEvent3MainThread", DataEvent.class, ThreadMode.MAIN)}));
        putIndex(new SimpleSubscriberInfo(DJIFragmentActivityNoFullScreen.class, true, new SubscriberMethodInfo[]{new SubscriberMethodInfo("onEvent3MainThread", V_AppUtils.DJI_SYS_UI_EVENT.class, ThreadMode.MAIN), new SubscriberMethodInfo("onEvent3BackgroundThread", DataOsdGetPushCommon.class, ThreadMode.BACKGROUND), new SubscriberMethodInfo("onEvent3MainThread", DataCameraGetPushUpgradeStatus.class, ThreadMode.MAIN)}));
        putIndex(new SimpleSubscriberInfo(DJIRedundancySysController.class, true, new SubscriberMethodInfo[]{new SubscriberMethodInfo("onEvent3BackgroundThread", DataFlycPushRedundancyStatus.class, ThreadMode.BACKGROUND), new SubscriberMethodInfo("onEvent3BackgroundThread", DataCameraEvent.class, ThreadMode.BACKGROUND), new SubscriberMethodInfo("onEvent3BackgroundThread", DataEvent.class, ThreadMode.BACKGROUND), new SubscriberMethodInfo("onEvent3BackgroundThread", DataOsdGetPushCommon.class, ThreadMode.BACKGROUND)}));
        putIndex(new SimpleSubscriberInfo(BatteryManager.class, true, new SubscriberMethodInfo[]{new SubscriberMethodInfo("onEvent3BackgroundThread", DataCenterGetPushBatteryCommon.class, ThreadMode.BACKGROUND), new SubscriberMethodInfo("onEvent3BackgroundThread", DataSmartBatteryGetPushDynamicData.class, ThreadMode.BACKGROUND), new SubscriberMethodInfo("onEvent3BackgroundThread", DataSmartBatteryGetPushCellVoltage.class, ThreadMode.BACKGROUND), new SubscriberMethodInfo("onEvent3MainThread", DataFlycGetPushSmartBattery.class, ThreadMode.MAIN), new SubscriberMethodInfo("onEvent3MainThread", ProductType.class, ThreadMode.MAIN), new SubscriberMethodInfo("onEvent3MainThread", DataEvent.class, ThreadMode.MAIN), new SubscriberMethodInfo("onEvent3MainThread", DataCameraEvent.class, ThreadMode.MAIN)}));
    }

    private static void putIndex(SubscriberInfo info) {
        SUBSCRIBER_INDEX.put(info.getSubscriberClass(), info);
    }

    public SubscriberInfo getSubscriberInfo(Class<?> subscriberClass) {
        SubscriberInfo info = SUBSCRIBER_INDEX.get(subscriberClass);
        if (info != null) {
            return info;
        }
        return null;
    }
}
