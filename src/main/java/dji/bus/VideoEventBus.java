package dji.bus;

import dji.common.bus.BusFactory;
import dji.common.bus.EventBus;
import dji.fieldAnnotation.EXClassNullAway;

@EXClassNullAway
public class VideoEventBus {
    private static final int NUMBER_EVENT_REPEAT = 1;

    private static class LazyHolder {
        /* access modifiers changed from: private */
        public static final EventBus<Object> INSTANCE = BusFactory.createRepeating(1);

        private LazyHolder() {
        }
    }

    public static EventBus<Object> getInstance() {
        return LazyHolder.INSTANCE;
    }

    public static final class VideoFeedPhysicalSourceChangeEvent {
        private final int newPhysicalSource;
        private final Object videoFeed;

        public VideoFeedPhysicalSourceChangeEvent(Object videoFeed2, int newPhysicalSource2) {
            this.videoFeed = videoFeed2;
            this.newPhysicalSource = newPhysicalSource2;
        }

        public Object getVideoFeed() {
            return this.videoFeed;
        }

        public int getNewPhysicalSource() {
            return this.newPhysicalSource;
        }
    }
}
