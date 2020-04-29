package dji.common.bus;

import dji.fieldAnnotation.EXClassNullAway;
import java.util.Map;

@EXClassNullAway
public class UXSDKEventBus {

    private static class LazyHolder {
        /* access modifiers changed from: private */
        public static final EventBus<Object> INSTANCE = BusFactory.createSimple();

        private LazyHolder() {
        }
    }

    public static EventBus<Object> getInstance() {
        return LazyHolder.INSTANCE;
    }

    public static final class UXSDKEvent {
        private final String categoryName;
        private final String eventName;
        private final Map<String, Object> extras;

        public UXSDKEvent(String eventName2, String categoryName2, Map<String, Object> extras2) {
            this.eventName = eventName2;
            this.categoryName = categoryName2;
            this.extras = extras2;
        }

        public String getEventName() {
            return this.eventName;
        }

        public String getCategoryName() {
            return this.categoryName;
        }

        public Map<String, Object> getExtras() {
            return this.extras;
        }
    }
}
