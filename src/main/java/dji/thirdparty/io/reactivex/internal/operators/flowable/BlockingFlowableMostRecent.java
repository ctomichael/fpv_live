package dji.thirdparty.io.reactivex.internal.operators.flowable;

import dji.thirdparty.io.reactivex.internal.util.ExceptionHelper;
import dji.thirdparty.io.reactivex.internal.util.NotificationLite;
import dji.thirdparty.io.reactivex.subscribers.DefaultSubscriber;
import java.util.Iterator;
import java.util.NoSuchElementException;
import org.reactivestreams.Publisher;

public final class BlockingFlowableMostRecent<T> implements Iterable<T> {
    final T initialValue;
    final Publisher<? extends T> source;

    public BlockingFlowableMostRecent(Publisher<? extends T> source2, T initialValue2) {
        this.source = source2;
        this.initialValue = initialValue2;
    }

    public Iterator<T> iterator() {
        MostRecentSubscriber<T> mostRecentSubscriber = new MostRecentSubscriber<>(this.initialValue);
        this.source.subscribe(mostRecentSubscriber);
        return mostRecentSubscriber.getIterable();
    }

    static final class MostRecentSubscriber<T> extends DefaultSubscriber<T> {
        volatile Object value;

        MostRecentSubscriber(T value2) {
            this.value = NotificationLite.next(value2);
        }

        public void onComplete() {
            this.value = NotificationLite.complete();
        }

        public void onError(Throwable e) {
            this.value = NotificationLite.error(e);
        }

        public void onNext(T args) {
            this.value = NotificationLite.next(args);
        }

        public Iterator<T> getIterable() {
            return new Iterator<T>() {
                /* class dji.thirdparty.io.reactivex.internal.operators.flowable.BlockingFlowableMostRecent.MostRecentSubscriber.AnonymousClass1 */
                private Object buf;

                public boolean hasNext() {
                    this.buf = MostRecentSubscriber.this.value;
                    return !NotificationLite.isComplete(this.buf);
                }

                public T next() {
                    Object obj = null;
                    try {
                        if (this.buf == null) {
                            obj = MostRecentSubscriber.this.value;
                        }
                        if (NotificationLite.isComplete(this.buf)) {
                            throw new NoSuchElementException();
                        } else if (NotificationLite.isError(this.buf)) {
                            throw ExceptionHelper.wrapOrThrow(NotificationLite.getError(this.buf));
                        } else {
                            T value = NotificationLite.getValue(this.buf);
                            this.buf = obj;
                            return value;
                        }
                    } finally {
                        this.buf = obj;
                    }
                }

                public void remove() {
                    throw new UnsupportedOperationException("Read only iterator");
                }
            };
        }
    }
}
