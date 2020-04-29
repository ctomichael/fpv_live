package dji.common.bus;

import dji.fieldAnnotation.EXClassNullAway;

@EXClassNullAway
public class MissionEventBus {

    private static class LazyHolder {
        /* access modifiers changed from: private */
        public static final EventBus<Object> INSTANCE = BusFactory.createSimple();

        private LazyHolder() {
        }
    }

    public static EventBus<Object> getInstance() {
        return LazyHolder.INSTANCE;
    }
}
