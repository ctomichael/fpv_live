package dji.thirdparty.rx.internal.operators;

import dji.thirdparty.rx.Observable;
import dji.thirdparty.rx.Subscriber;
import dji.thirdparty.rx.exceptions.Exceptions;
import java.util.Iterator;
import java.util.NoSuchElementException;

public final class BlockingOperatorMostRecent {
    private BlockingOperatorMostRecent() {
        throw new IllegalStateException("No instances!");
    }

    public static <T> Iterable<T> mostRecent(final Observable<? extends T> source, final T initialValue) {
        return new Iterable<T>() {
            /* class dji.thirdparty.rx.internal.operators.BlockingOperatorMostRecent.AnonymousClass1 */

            public Iterator<T> iterator() {
                MostRecentObserver<T> mostRecentObserver = new MostRecentObserver<>(initialValue);
                source.subscribe((Subscriber) mostRecentObserver);
                return mostRecentObserver.getIterable();
            }
        };
    }

    private static final class MostRecentObserver<T> extends Subscriber<T> {
        final NotificationLite<T> nl = NotificationLite.instance();
        volatile Object value;

        MostRecentObserver(T value2) {
            this.value = this.nl.next(value2);
        }

        public void onCompleted() {
            this.value = this.nl.completed();
        }

        public void onError(Throwable e) {
            this.value = this.nl.error(e);
        }

        public void onNext(T args) {
            this.value = this.nl.next(args);
        }

        public Iterator<T> getIterable() {
            return new Iterator<T>() {
                /* class dji.thirdparty.rx.internal.operators.BlockingOperatorMostRecent.MostRecentObserver.AnonymousClass1 */
                private Object buf = null;

                public boolean hasNext() {
                    this.buf = MostRecentObserver.this.value;
                    return !MostRecentObserver.this.nl.isCompleted(this.buf);
                }

                public T next() {
                    Object obj = null;
                    try {
                        if (this.buf == null) {
                            obj = MostRecentObserver.this.value;
                        }
                        if (MostRecentObserver.this.nl.isCompleted(this.buf)) {
                            throw new NoSuchElementException();
                        } else if (MostRecentObserver.this.nl.isError(this.buf)) {
                            throw Exceptions.propagate(MostRecentObserver.this.nl.getError(this.buf));
                        } else {
                            T value = MostRecentObserver.this.nl.getValue(this.buf);
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
