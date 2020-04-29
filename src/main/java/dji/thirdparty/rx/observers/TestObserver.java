package dji.thirdparty.rx.observers;

import dji.component.accountcenter.IMemberProtocol;
import dji.thirdparty.rx.Notification;
import dji.thirdparty.rx.Observer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TestObserver<T> implements Observer<T> {
    private static Observer<Object> INERT = new Observer<Object>() {
        /* class dji.thirdparty.rx.observers.TestObserver.AnonymousClass1 */

        public void onCompleted() {
        }

        public void onError(Throwable e) {
        }

        public void onNext(Object t) {
        }
    };
    private final Observer<T> delegate;
    private final ArrayList<Notification<T>> onCompletedEvents;
    private final ArrayList<Throwable> onErrorEvents;
    private final ArrayList<T> onNextEvents;

    public TestObserver(Observer<T> delegate2) {
        this.onNextEvents = new ArrayList<>();
        this.onErrorEvents = new ArrayList<>();
        this.onCompletedEvents = new ArrayList<>();
        this.delegate = delegate2;
    }

    public TestObserver() {
        this.onNextEvents = new ArrayList<>();
        this.onErrorEvents = new ArrayList<>();
        this.onCompletedEvents = new ArrayList<>();
        this.delegate = INERT;
    }

    public void onCompleted() {
        this.onCompletedEvents.add(Notification.createOnCompleted());
        this.delegate.onCompleted();
    }

    public List<Notification<T>> getOnCompletedEvents() {
        return Collections.unmodifiableList(this.onCompletedEvents);
    }

    public void onError(Throwable e) {
        this.onErrorEvents.add(e);
        this.delegate.onError(e);
    }

    public List<Throwable> getOnErrorEvents() {
        return Collections.unmodifiableList(this.onErrorEvents);
    }

    public void onNext(T t) {
        this.onNextEvents.add(t);
        this.delegate.onNext(t);
    }

    public List<T> getOnNextEvents() {
        return Collections.unmodifiableList(this.onNextEvents);
    }

    public List<Object> getEvents() {
        ArrayList<Object> events = new ArrayList<>();
        events.add(this.onNextEvents);
        events.add(this.onErrorEvents);
        events.add(this.onCompletedEvents);
        return Collections.unmodifiableList(events);
    }

    public void assertReceivedOnNext(List<T> items) {
        if (this.onNextEvents.size() != items.size()) {
            throw new AssertionError("Number of items does not match. Provided: " + items.size() + "  Actual: " + this.onNextEvents.size());
        }
        for (int i = 0; i < items.size(); i++) {
            T expected = items.get(i);
            T actual = this.onNextEvents.get(i);
            if (expected == null) {
                if (actual != null) {
                    throw new AssertionError("Value at index: " + i + " expected to be [null] but was: [" + ((Object) actual) + IMemberProtocol.STRING_SEPERATOR_RIGHT);
                }
            } else if (!expected.equals(actual)) {
                throw new AssertionError("Value at index: " + i + " expected to be [" + ((Object) expected) + "] (" + expected.getClass().getSimpleName() + ") but was: [" + ((Object) actual) + "] (" + (actual != null ? actual.getClass().getSimpleName() : "null") + ")");
            }
        }
    }

    public void assertTerminalEvent() {
        if (this.onErrorEvents.size() > 1) {
            throw new AssertionError("Too many onError events: " + this.onErrorEvents.size());
        } else if (this.onCompletedEvents.size() > 1) {
            throw new AssertionError("Too many onCompleted events: " + this.onCompletedEvents.size());
        } else if (this.onCompletedEvents.size() == 1 && this.onErrorEvents.size() == 1) {
            throw new AssertionError("Received both an onError and onCompleted. Should be one or the other.");
        } else if (this.onCompletedEvents.size() == 0 && this.onErrorEvents.size() == 0) {
            throw new AssertionError("No terminal events received.");
        }
    }
}
