package dji.event3.findmydrone;

import com.dji.findmydrone.ui.FindMyDroneApplicationReceiver;
import dji.midware.data.model.P3.DataOsdGetPushCommon;
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
        putIndex(new SimpleSubscriberInfo(FindMyDroneApplicationReceiver.class, true, new SubscriberMethodInfo[]{new SubscriberMethodInfo("onEvent3BackgroundThread", DataOsdGetPushCommon.class, ThreadMode.ASYNC)}));
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
