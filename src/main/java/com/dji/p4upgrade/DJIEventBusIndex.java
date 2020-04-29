package com.dji.p4upgrade;

import dji.dbox.upgrade.logics.DJIUpgradeWifiPrLogic;
import dji.dbox.upgrade.p4.statemachine.DJIUpProgressManager;
import dji.dbox.upgrade.p4.statemachine.DJIUpgradeP4Manager;
import dji.dbox.upgrade.p4.statemachine.DJIUpgradeService;
import dji.dbox.upgrade.p4.statemachine.UpgradeReDoEvent;
import dji.midware.broadcastReceivers.DJINetWorkReceiver;
import dji.midware.data.manager.P3.DataCameraEvent;
import dji.midware.data.manager.P3.DataEvent;
import dji.midware.data.model.P3.DataCommonGetPushUpgradeStatus;
import dji.midware.data.model.P3.DataNotifyDisconnect;
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
        putIndex(new SimpleSubscriberInfo(DJIUpgradeP4Manager.class, true, new SubscriberMethodInfo[]{new SubscriberMethodInfo("onEvent3BackgroundThread", DJIUpgradeService.DJIUpP4Event.class, ThreadMode.BACKGROUND)}));
        putIndex(new SimpleSubscriberInfo(DJIUpProgressManager.class, true, new SubscriberMethodInfo[]{new SubscriberMethodInfo("onEvent3MainThread", DataNotifyDisconnect.class, ThreadMode.MAIN), new SubscriberMethodInfo("onEvent3MainThread", DJIUpgradeService.DJIUpP4Event.class, ThreadMode.MAIN), new SubscriberMethodInfo("onEvent3MainThread", DataCommonGetPushUpgradeStatus.class, ThreadMode.MAIN)}));
        putIndex(new SimpleSubscriberInfo(DJIUpgradeWifiPrLogic.class, true, new SubscriberMethodInfo[]{new SubscriberMethodInfo("onEvent3BackgroundThread", DJINetWorkReceiver.DJINetWorkStatusEvent.class, ThreadMode.BACKGROUND)}));
        putIndex(new SimpleSubscriberInfo(DJIUpgradeService.class, true, new SubscriberMethodInfo[]{new SubscriberMethodInfo("onEvent3BackgroundThread", UpgradeReDoEvent.class, ThreadMode.BACKGROUND), new SubscriberMethodInfo("onEvent3BackgroundThread", DJINetWorkReceiver.DJINetWorkStatusEvent.class, ThreadMode.BACKGROUND), new SubscriberMethodInfo("onEvent3BackgroundThread", DataEvent.class, ThreadMode.BACKGROUND), new SubscriberMethodInfo("onEvent3BackgroundThread", DataCameraEvent.class, ThreadMode.BACKGROUND)}));
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
