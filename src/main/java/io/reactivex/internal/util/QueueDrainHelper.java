package io.reactivex.internal.util;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.exceptions.MissingBackpressureException;
import io.reactivex.functions.BooleanSupplier;
import io.reactivex.internal.fuseable.SimplePlainQueue;
import io.reactivex.internal.fuseable.SimpleQueue;
import io.reactivex.internal.queue.SpscArrayQueue;
import io.reactivex.internal.queue.SpscLinkedArrayQueue;
import java.util.Queue;
import java.util.concurrent.atomic.AtomicLong;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

public final class QueueDrainHelper {
    static final long COMPLETED_MASK = Long.MIN_VALUE;
    static final long REQUESTED_MASK = Long.MAX_VALUE;

    private QueueDrainHelper() {
        throw new IllegalStateException("No instances!");
    }

    public static <T, U> void drainMaxLoop(SimplePlainQueue<T> q, Subscriber<? super U> a, boolean delayError, Disposable dispose, QueueDrain<T, U> qd) {
        int missed = 1;
        while (true) {
            boolean d = qd.done();
            T v = q.poll();
            boolean empty = v == null;
            if (checkTerminated(d, empty, a, delayError, q, qd)) {
                if (dispose != null) {
                    dispose.dispose();
                    return;
                }
                return;
            } else if (empty) {
                missed = qd.leave(-missed);
                if (missed == 0) {
                    return;
                }
            } else {
                long r = qd.requested();
                if (r == 0) {
                    q.clear();
                    if (dispose != null) {
                        dispose.dispose();
                    }
                    a.onError(new MissingBackpressureException("Could not emit value due to lack of requests."));
                    return;
                } else if (qd.accept(a, v) && r != Long.MAX_VALUE) {
                    qd.produced(1);
                }
            }
        }
    }

    public static <T, U> boolean checkTerminated(boolean d, boolean empty, Subscriber<?> s, boolean delayError, SimpleQueue<?> q, QueueDrain<T, U> qd) {
        if (qd.cancelled()) {
            q.clear();
            return true;
        }
        if (d) {
            if (!delayError) {
                Throwable err = qd.error();
                if (err != null) {
                    q.clear();
                    s.onError(err);
                    return true;
                } else if (empty) {
                    s.onComplete();
                    return true;
                }
            } else if (empty) {
                Throwable err2 = qd.error();
                if (err2 != null) {
                    s.onError(err2);
                    return true;
                }
                s.onComplete();
                return true;
            }
        }
        return false;
    }

    public static <T, U> void drainLoop(SimplePlainQueue<T> q, Observer<? super U> a, boolean delayError, Disposable dispose, ObservableQueueDrain<T, U> qd) {
        int missed = 1;
        while (!checkTerminated(qd.done(), q.isEmpty(), a, delayError, q, dispose, qd)) {
            while (true) {
                boolean d = qd.done();
                T v = q.poll();
                boolean empty = v == null;
                if (checkTerminated(d, empty, a, delayError, q, dispose, qd)) {
                    return;
                }
                if (empty) {
                    missed = qd.leave(-missed);
                    if (missed == 0) {
                        return;
                    }
                } else {
                    qd.accept(a, v);
                }
            }
        }
    }

    public static <T, U> boolean checkTerminated(boolean d, boolean empty, Observer<?> observer, boolean delayError, SimpleQueue<?> q, Disposable disposable, ObservableQueueDrain<T, U> qd) {
        if (qd.cancelled()) {
            q.clear();
            disposable.dispose();
            return true;
        }
        if (d) {
            if (!delayError) {
                Throwable err = qd.error();
                if (err != null) {
                    q.clear();
                    if (disposable != null) {
                        disposable.dispose();
                    }
                    observer.onError(err);
                    return true;
                } else if (empty) {
                    if (disposable != null) {
                        disposable.dispose();
                    }
                    observer.onComplete();
                    return true;
                }
            } else if (empty) {
                if (disposable != null) {
                    disposable.dispose();
                }
                Throwable err2 = qd.error();
                if (err2 != null) {
                    observer.onError(err2);
                    return true;
                }
                observer.onComplete();
                return true;
            }
        }
        return false;
    }

    public static <T> SimpleQueue<T> createQueue(int capacityHint) {
        if (capacityHint < 0) {
            return new SpscLinkedArrayQueue(-capacityHint);
        }
        return new SpscArrayQueue(capacityHint);
    }

    public static void request(Subscription s, int prefetch) {
        s.request(prefetch < 0 ? Long.MAX_VALUE : (long) prefetch);
    }

    public static <T> boolean postCompleteRequest(long n, Subscriber<? super T> actual, Queue<T> queue, AtomicLong state, BooleanSupplier isCancelled) {
        long r;
        do {
            r = state.get();
        } while (!state.compareAndSet(r, (Long.MIN_VALUE & r) | BackpressureHelper.addCap(r & Long.MAX_VALUE, n)));
        if (r != Long.MIN_VALUE) {
            return false;
        }
        postCompleteDrain(Long.MIN_VALUE | n, actual, queue, state, isCancelled);
        return true;
    }

    static boolean isCancelled(BooleanSupplier cancelled) {
        try {
            return cancelled.getAsBoolean();
        } catch (Throwable ex) {
            Exceptions.throwIfFatal(ex);
            return true;
        }
    }

    static <T> boolean postCompleteDrain(long n, Subscriber<? super T> actual, Queue<T> queue, AtomicLong state, BooleanSupplier isCancelled) {
        long e = n & Long.MIN_VALUE;
        while (true) {
            if (e != n) {
                if (isCancelled(isCancelled)) {
                    return true;
                }
                T t = queue.poll();
                if (t == null) {
                    actual.onComplete();
                    return true;
                }
                actual.onNext(t);
                e++;
            } else if (isCancelled(isCancelled)) {
                return true;
            } else {
                if (queue.isEmpty()) {
                    actual.onComplete();
                    return true;
                }
                n = state.get();
                if (n == e) {
                    n = state.addAndGet(-(Long.MAX_VALUE & e));
                    if ((Long.MAX_VALUE & n) == 0) {
                        return false;
                    }
                    e = n & Long.MIN_VALUE;
                } else {
                    continue;
                }
            }
        }
    }

    public static <T> void postComplete(Subscriber<? super T> actual, Queue<T> queue, AtomicLong state, BooleanSupplier isCancelled) {
        long r;
        long u;
        if (queue.isEmpty()) {
            actual.onComplete();
        } else if (!postCompleteDrain(state.get(), actual, queue, state, isCancelled)) {
            do {
                r = state.get();
                if ((r & Long.MIN_VALUE) == 0) {
                    u = r | Long.MIN_VALUE;
                } else {
                    return;
                }
            } while (!state.compareAndSet(r, u));
            if (r != 0) {
                postCompleteDrain(u, actual, queue, state, isCancelled);
            }
        }
    }
}
