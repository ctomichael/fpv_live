package dji.common.bus;

import dji.fieldAnnotation.EXClassNullAway;
import dji.thirdparty.rx.Observable;
import dji.thirdparty.rx.subjects.BehaviorSubject;
import dji.thirdparty.rx.subjects.PublishSubject;
import dji.thirdparty.rx.subjects.ReplaySubject;
import dji.thirdparty.rx.subjects.Subject;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@EXClassNullAway
public class BusFactory {
    public static <T> EventBus<T> createSimple() {
        return new EventBus<>(PublishSubject.create());
    }

    public static <T> EventBus<T> createWithLatest() {
        return new EventBus<>(BehaviorSubject.create());
    }

    public static <T> EventBus<T> createRepeating(int numberOfEventsToRepeat) {
        return new EventBus<>(ReplaySubject.createWithSize(numberOfEventsToRepeat));
    }

    public static <T> EventBus<T> createCaching() {
        return new CachingEventBus(BehaviorSubject.create());
    }

    private static class CachingEventBus<T> extends EventBus<T> {
        private EventCache<T> eventCache = new EventCache<>();

        CachingEventBus(Subject<T, T> subject) {
            super(subject);
        }

        public <E extends T> void post(E event) {
            if (event != null) {
                this.eventCache.put(event);
            }
            super.post(event);
        }

        public <E extends T> Observable<E> register(Class<E> eventClass) {
            if (this.eventCache.get(eventClass) != null) {
                return super.register(eventClass).startWith(this.eventCache.get(eventClass));
            }
            return super.register(eventClass);
        }
    }

    private static class EventCache<T> {
        private Map<Class, T> simpleCachingMap = new ConcurrentHashMap();

        EventCache() {
        }

        public <E extends T> void put(E eventToBeCached) {
            if (eventToBeCached != null) {
                this.simpleCachingMap.put(eventToBeCached.getClass(), eventToBeCached);
            }
        }

        public <E extends T> E get(Class<E> eventClassToGet) {
            return this.simpleCachingMap.get(eventClassToGet);
        }
    }
}
