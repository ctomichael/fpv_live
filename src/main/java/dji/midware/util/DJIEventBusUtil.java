package dji.midware.util;

import dji.fieldAnnotation.EXClassNullAway;
import org.greenrobot.eventbus.EventBus;

@EXClassNullAway
public class DJIEventBusUtil {
    public static void register(Object o) {
        if (!EventBus.getDefault().isRegistered(o)) {
            EventBus.getDefault().register(o);
        }
    }

    public static void unRegister(Object o) {
        if (EventBus.getDefault().isRegistered(o)) {
            EventBus.getDefault().unregister(o);
        }
    }
}
