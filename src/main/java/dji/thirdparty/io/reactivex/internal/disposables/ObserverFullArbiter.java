package dji.thirdparty.io.reactivex.internal.disposables;

import dji.thirdparty.io.reactivex.Observer;
import dji.thirdparty.io.reactivex.disposables.Disposable;
import dji.thirdparty.io.reactivex.internal.queue.SpscLinkedArrayQueue;
import dji.thirdparty.io.reactivex.internal.util.NotificationLite;
import dji.thirdparty.io.reactivex.plugins.RxJavaPlugins;

public final class ObserverFullArbiter<T> extends FullArbiterPad1 implements Disposable {
    final Observer<? super T> actual;
    volatile boolean cancelled;
    final SpscLinkedArrayQueue<Object> queue;
    Disposable resource;
    volatile Disposable s = EmptyDisposable.INSTANCE;

    public ObserverFullArbiter(Observer<? super T> actual2, Disposable resource2, int capacity) {
        this.actual = actual2;
        this.resource = resource2;
        this.queue = new SpscLinkedArrayQueue<>(capacity);
    }

    public void dispose() {
        if (!this.cancelled) {
            this.cancelled = true;
            disposeResource();
        }
    }

    public boolean isDisposed() {
        Disposable d = this.resource;
        return d != null ? d.isDisposed() : this.cancelled;
    }

    /* access modifiers changed from: package-private */
    public void disposeResource() {
        Disposable d = this.resource;
        this.resource = null;
        if (d != null) {
            d.dispose();
        }
    }

    public boolean setDisposable(Disposable s2) {
        if (this.cancelled) {
            return false;
        }
        this.queue.offer(this.s, NotificationLite.disposable(s2));
        drain();
        return true;
    }

    public boolean onNext(T value, Disposable s2) {
        if (this.cancelled) {
            return false;
        }
        this.queue.offer(s2, NotificationLite.next(value));
        drain();
        return true;
    }

    public void onError(Throwable value, Disposable s2) {
        if (this.cancelled) {
            RxJavaPlugins.onError(value);
            return;
        }
        this.queue.offer(s2, NotificationLite.error(value));
        drain();
    }

    public void onComplete(Disposable s2) {
        this.queue.offer(s2, NotificationLite.complete());
        drain();
    }

    /* access modifiers changed from: package-private */
    public void drain() {
        if (this.wip.getAndIncrement() == 0) {
            int missed = 1;
            SpscLinkedArrayQueue<Object> q = this.queue;
            Observer<? super T> a = this.actual;
            while (true) {
                Object o = q.poll();
                if (o == null) {
                    missed = this.wip.addAndGet(-missed);
                    if (missed == 0) {
                        return;
                    }
                } else {
                    Object v = q.poll();
                    if (o == this.s) {
                        if (NotificationLite.isDisposable(v)) {
                            Disposable next = NotificationLite.getDisposable(v);
                            this.s.dispose();
                            if (!this.cancelled) {
                                this.s = next;
                            } else {
                                next.dispose();
                            }
                        } else if (NotificationLite.isError(v)) {
                            q.clear();
                            disposeResource();
                            Throwable ex = NotificationLite.getError(v);
                            if (!this.cancelled) {
                                this.cancelled = true;
                                a.onError(ex);
                            } else {
                                RxJavaPlugins.onError(ex);
                            }
                        } else if (NotificationLite.isComplete(v)) {
                            q.clear();
                            disposeResource();
                            if (!this.cancelled) {
                                this.cancelled = true;
                                a.onComplete();
                            }
                        } else {
                            a.onNext(NotificationLite.getValue(v));
                        }
                    }
                }
            }
        }
    }
}
