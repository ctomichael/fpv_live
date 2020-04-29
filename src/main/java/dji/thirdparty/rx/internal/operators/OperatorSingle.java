package dji.thirdparty.rx.internal.operators;

import dji.thirdparty.rx.Observable;
import dji.thirdparty.rx.Producer;
import dji.thirdparty.rx.Subscriber;
import java.util.NoSuchElementException;
import java.util.concurrent.atomic.AtomicBoolean;

public final class OperatorSingle<T> implements Observable.Operator<T, T> {
    private final T defaultValue;
    private final boolean hasDefaultValue;

    public /* bridge */ /* synthetic */ Object call(Object x0) {
        return call((Subscriber) ((Subscriber) x0));
    }

    private static class Holder {
        static final OperatorSingle<?> INSTANCE = new OperatorSingle<>();

        private Holder() {
        }
    }

    public static <T> OperatorSingle<T> instance() {
        return Holder.INSTANCE;
    }

    OperatorSingle() {
        this(false, null);
    }

    public OperatorSingle(T defaultValue2) {
        this(true, defaultValue2);
    }

    private OperatorSingle(boolean hasDefaultValue2, T defaultValue2) {
        this.hasDefaultValue = hasDefaultValue2;
        this.defaultValue = defaultValue2;
    }

    public Subscriber<? super T> call(Subscriber<? super T> child) {
        final ParentSubscriber<T> parent = new ParentSubscriber<>(child, this.hasDefaultValue, this.defaultValue);
        child.setProducer(new Producer() {
            /* class dji.thirdparty.rx.internal.operators.OperatorSingle.AnonymousClass1 */
            private final AtomicBoolean requestedTwo = new AtomicBoolean(false);

            public void request(long n) {
                if (n > 0 && this.requestedTwo.compareAndSet(false, true)) {
                    parent.requestMore(2);
                }
            }
        });
        child.add(parent);
        return parent;
    }

    private static final class ParentSubscriber<T> extends Subscriber<T> {
        private final Subscriber<? super T> child;
        private final T defaultValue;
        private final boolean hasDefaultValue;
        private boolean hasTooManyElements = false;
        private boolean isNonEmpty = false;
        private T value;

        ParentSubscriber(Subscriber<? super T> child2, boolean hasDefaultValue2, T defaultValue2) {
            this.child = child2;
            this.hasDefaultValue = hasDefaultValue2;
            this.defaultValue = defaultValue2;
        }

        /* access modifiers changed from: package-private */
        public void requestMore(long n) {
            request(n);
        }

        public void onNext(T value2) {
            if (this.isNonEmpty) {
                this.hasTooManyElements = true;
                this.child.onError(new IllegalArgumentException("Sequence contains too many elements"));
                unsubscribe();
                return;
            }
            this.value = value2;
            this.isNonEmpty = true;
        }

        public void onCompleted() {
            if (!this.hasTooManyElements) {
                if (this.isNonEmpty) {
                    this.child.onNext(this.value);
                    this.child.onCompleted();
                } else if (this.hasDefaultValue) {
                    this.child.onNext(this.defaultValue);
                    this.child.onCompleted();
                } else {
                    this.child.onError(new NoSuchElementException("Sequence contains no elements"));
                }
            }
        }

        public void onError(Throwable e) {
            this.child.onError(e);
        }
    }
}
