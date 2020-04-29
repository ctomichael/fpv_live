package dji.publics.utils;

import dji.fieldAnnotation.EXClassNullAway;
import org.greenrobot.eventbus.EventBus;

@EXClassNullAway
public class EventBusUtil {
    private static final String TAG = "EventBusUtil";

    public static void regist(Object o) {
        if (!EventBus.getDefault().isRegistered(o)) {
            EventBus.getDefault().register(o);
        }
    }

    public static void unregist(Object o) {
        if (EventBus.getDefault().isRegistered(o)) {
            EventBus.getDefault().unregister(o);
        }
    }
}
