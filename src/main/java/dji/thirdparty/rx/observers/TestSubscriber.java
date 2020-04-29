package dji.thirdparty.rx.observers;

import dji.thirdparty.rx.Notification;
import dji.thirdparty.rx.Observer;
import dji.thirdparty.rx.Subscriber;
import dji.thirdparty.rx.exceptions.CompositeException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class TestSubscriber<T> extends Subscriber<T> {
    private static final Observer<Object> INERT = new Observer<Object>() {
        /* class dji.thirdparty.rx.observers.TestSubscriber.AnonymousClass1 */

        public void onCompleted() {
        }

        public void onError(Throwable e) {
        }

        public void onNext(Object t) {
        }
    };
    private volatile Thread lastSeenThread;
    private final CountDownLatch latch;
    private final TestObserver<T> testObserver;

    public TestSubscriber(long initialRequest) {
        this(INERT, initialRequest);
    }

    public TestSubscriber(Observer<T> delegate, long initialRequest) {
        this.latch = new CountDownLatch(1);
        if (delegate == null) {
            throw new NullPointerException();
        }
        this.testObserver = new TestObserver<>(delegate);
        if (initialRequest >= 0) {
            request(initialRequest);
        }
    }

    public TestSubscriber(Subscriber<T> delegate) {
        this(delegate, -1);
    }

    public TestSubscriber(Observer<T> delegate) {
        this(delegate, -1);
    }

    public TestSubscriber() {
        this(-1);
    }

    public static <T> TestSubscriber<T> create() {
        return new TestSubscriber<>();
    }

    public static <T> TestSubscriber<T> create(long initialRequest) {
        return new TestSubscriber<>(initialRequest);
    }

    public static <T> TestSubscriber<T> create(Observer<T> delegate, long initialRequest) {
        return new TestSubscriber<>(delegate, initialRequest);
    }

    public static <T> TestSubscriber<T> create(Subscriber<T> delegate) {
        return new TestSubscriber<>((Subscriber) delegate);
    }

    public static <T> TestSubscriber<T> create(Observer<T> delegate) {
        return new TestSubscriber<>(delegate);
    }

    public void onCompleted() {
        try {
            this.lastSeenThread = Thread.currentThread();
            this.testObserver.onCompleted();
        } finally {
            this.latch.countDown();
        }
    }

    public List<Notification<T>> getOnCompletedEvents() {
        return this.testObserver.getOnCompletedEvents();
    }

    public void onError(Throwable e) {
        try {
            this.lastSeenThread = Thread.currentThread();
            this.testObserver.onError(e);
        } finally {
            this.latch.countDown();
        }
    }

    public List<Throwable> getOnErrorEvents() {
        return this.testObserver.getOnErrorEvents();
    }

    public void onNext(T t) {
        this.lastSeenThread = Thread.currentThread();
        this.testObserver.onNext(t);
    }

    public void requestMore(long n) {
        request(n);
    }

    public List<T> getOnNextEvents() {
        return this.testObserver.getOnNextEvents();
    }

    public void assertReceivedOnNext(List<T> items) {
        this.testObserver.assertReceivedOnNext(items);
    }

    public void assertTerminalEvent() {
        this.testObserver.assertTerminalEvent();
    }

    public void assertUnsubscribed() {
        if (!isUnsubscribed()) {
            throw new AssertionError("Not unsubscribed.");
        }
    }

    public void assertNoErrors() {
        List<Throwable> onErrorEvents = getOnErrorEvents();
        if (onErrorEvents.size() > 0) {
            AssertionError ae = new AssertionError("Unexpected onError events: " + getOnErrorEvents().size());
            if (onErrorEvents.size() == 1) {
                ae.initCause(getOnErrorEvents().get(0));
            } else {
                ae.initCause(new CompositeException(onErrorEvents));
            }
            throw ae;
        }
    }

    public void awaitTerminalEvent() {
        try {
            this.latch.await();
        } catch (InterruptedException e) {
            throw new RuntimeException("Interrupted", e);
        }
    }

    public void awaitTerminalEvent(long timeout, TimeUnit unit) {
        try {
            this.latch.await(timeout, unit);
        } catch (InterruptedException e) {
            throw new RuntimeException("Interrupted", e);
        }
    }

    public void awaitTerminalEventAndUnsubscribeOnTimeout(long timeout, TimeUnit unit) {
        try {
            if (!this.latch.await(timeout, unit)) {
                unsubscribe();
            }
        } catch (InterruptedException e) {
            unsubscribe();
        }
    }

    public Thread getLastSeenThread() {
        return this.lastSeenThread;
    }

    public void assertCompleted() {
        int s = this.testObserver.getOnCompletedEvents().size();
        if (s == 0) {
            throw new AssertionError("Not completed!");
        } else if (s > 1) {
            throw new AssertionError("Completed multiple times: " + s);
        }
    }

    public void assertNotCompleted() {
        int s = this.testObserver.getOnCompletedEvents().size();
        if (s == 1) {
            throw new AssertionError("Completed!");
        } else if (s > 1) {
            throw new AssertionError("Completed multiple times: " + s);
        }
    }

    public void assertError(Class<? extends Throwable> clazz) {
        List<Throwable> err = this.testObserver.getOnErrorEvents();
        if (err.size() == 0) {
            throw new AssertionError("No errors");
        } else if (err.size() > 1) {
            AssertionError ae = new AssertionError("Multiple errors: " + err.size());
            ae.initCause(new CompositeException(err));
            throw ae;
        } else if (!clazz.isInstance(err.get(0))) {
            AssertionError ae2 = new AssertionError("Exceptions differ; expected: " + clazz + ", actual: " + err.get(0));
            ae2.initCause(err.get(0));
            throw ae2;
        }
    }

    public void assertError(Throwable throwable) {
        List<Throwable> err = this.testObserver.getOnErrorEvents();
        if (err.size() == 0) {
            throw new AssertionError("No errors");
        } else if (err.size() > 1) {
            AssertionError ae = new AssertionError("Multiple errors: " + err.size());
            ae.initCause(new CompositeException(err));
            throw ae;
        } else if (!throwable.equals(err.get(0))) {
            AssertionError ae2 = new AssertionError("Exceptions differ; expected: " + throwable + ", actual: " + err.get(0));
            ae2.initCause(err.get(0));
            throw ae2;
        }
    }

    public void assertNoTerminalEvent() {
        List<Throwable> err = this.testObserver.getOnErrorEvents();
        int s = this.testObserver.getOnCompletedEvents().size();
        if (err.size() <= 0 && s <= 0) {
            return;
        }
        if (err.isEmpty()) {
            throw new AssertionError("Found " + err.size() + " errors and " + s + " completion events instead of none");
        } else if (err.size() == 1) {
            AssertionError ae = new AssertionError("Found " + err.size() + " errors and " + s + " completion events instead of none");
            ae.initCause(err.get(0));
            throw ae;
        } else {
            AssertionError ae2 = new AssertionError("Found " + err.size() + " errors and " + s + " completion events instead of none");
            ae2.initCause(new CompositeException(err));
            throw ae2;
        }
    }

    public void assertNoValues() {
        int s = this.testObserver.getOnNextEvents().size();
        if (s > 0) {
            throw new AssertionError("No onNext events expected yet some received: " + s);
        }
    }

    public void assertValueCount(int count) {
        int s = this.testObserver.getOnNextEvents().size();
        if (s != count) {
            throw new AssertionError("Number of onNext events differ; expected: " + count + ", actual: " + s);
        }
    }

    public void assertValues(T... values) {
        assertReceivedOnNext(Arrays.asList(values));
    }

    public void assertValue(T value) {
        assertReceivedOnNext(Collections.singletonList(value));
    }
}
