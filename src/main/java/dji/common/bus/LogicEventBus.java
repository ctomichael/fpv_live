package dji.common.bus;

import dji.fieldAnnotation.EXClassNullAway;

@EXClassNullAway
public class LogicEventBus {

    private static class LazyHolder {
        /* access modifiers changed from: private */
        public static final EventBus<Object> INSTANCE = BusFactory.createCaching();

        private LazyHolder() {
        }
    }

    public static EventBus<Object> getInstance() {
        return LazyHolder.INSTANCE;
    }
}
