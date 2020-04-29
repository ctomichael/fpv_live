package dji.thirdparty.io.reactivex.internal.operators.observable;

import dji.thirdparty.io.reactivex.Notification;
import dji.thirdparty.io.reactivex.Observable;
import dji.thirdparty.io.reactivex.ObservableSource;
import dji.thirdparty.io.reactivex.Observer;
import dji.thirdparty.io.reactivex.disposables.Disposable;
import dji.thirdparty.io.reactivex.exceptions.Exceptions;
import dji.thirdparty.io.reactivex.functions.Consumer;
import dji.thirdparty.io.reactivex.functions.Function;
import dji.thirdparty.io.reactivex.internal.disposables.SequentialDisposable;
import dji.thirdparty.io.reactivex.internal.functions.ObjectHelper;
import dji.thirdparty.io.reactivex.internal.observers.ToNotificationObserver;
import dji.thirdparty.io.reactivex.subjects.BehaviorSubject;
import dji.thirdparty.io.reactivex.subjects.Subject;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public final class ObservableRedo<T> extends AbstractObservableWithUpstream<T, T> {
    final Function<? super Observable<Notification<Object>>, ? extends ObservableSource<?>> manager;

    public ObservableRedo(ObservableSource<T> source, Function<? super Observable<Notification<Object>>, ? extends ObservableSource<?>> manager2) {
        super(source);
        this.manager = manager2;
    }

    public void subscribeActual(Observer<? super T> s) {
        Subject<Notification<Object>> subject = BehaviorSubject.create().toSerialized();
        final RedoObserver<T> parent = new RedoObserver<>(s, subject, this.source);
        s.onSubscribe(parent.arbiter);
        try {
            ((ObservableSource) ObjectHelper.requireNonNull(this.manager.apply(subject), "The function returned a null ObservableSource")).subscribe(new ToNotificationObserver(new Consumer<Notification<Object>>() {
                /* class dji.thirdparty.io.reactivex.internal.operators.observable.ObservableRedo.AnonymousClass1 */

                public /* bridge */ /* synthetic */ void accept(Object x0) throws Exception {
                    accept((Notification<Object>) ((Notification) x0));
                }

                public void accept(Notification<Object> o) {
                    parent.handle(o);
                }
            }));
            parent.handle(Notification.createOnNext(0));
        } catch (Throwable ex) {
            Exceptions.throwIfFatal(ex);
            s.onError(ex);
        }
    }

    static final class RedoObserver<T> extends AtomicBoolean implements Observer<T> {
        private static final long serialVersionUID = -1151903143112844287L;
        final Observer<? super T> actual;
        final SequentialDisposable arbiter;
        final ObservableSource<? extends T> source;
        final Subject<Notification<Object>> subject;
        final AtomicInteger wip = new AtomicInteger();

        RedoObserver(Observer<? super T> actual2, Subject<Notification<Object>> subject2, ObservableSource<? extends T> source2) {
            this.actual = actual2;
            this.subject = subject2;
            this.source = source2;
            this.arbiter = new SequentialDisposable();
            lazySet(true);
        }

        public void onSubscribe(Disposable s) {
            this.arbiter.replace(s);
        }

        public void onNext(T t) {
            this.actual.onNext(t);
        }

        public void onError(Throwable t) {
            if (compareAndSet(false, true)) {
                this.subject.onNext(Notification.createOnError(t));
            }
        }

        public void onComplete() {
            if (compareAndSet(false, true)) {
                this.subject.onNext(Notification.createOnComplete());
            }
        }

        /* access modifiers changed from: package-private */
        public void handle(Notification<Object> notification) {
            if (!compareAndSet(true, false)) {
                return;
            }
            if (notification.isOnError()) {
                this.arbiter.dispose();
                this.actual.onError(notification.getError());
            } else if (!notification.isOnNext()) {
                this.arbiter.dispose();
                this.actual.onComplete();
            } else if (this.wip.getAndIncrement() == 0) {
                int missed = 1;
                while (!this.arbiter.isDisposed()) {
                    this.source.subscribe(this);
                    missed = this.wip.addAndGet(-missed);
                    if (missed == 0) {
                        return;
                    }
                }
            }
        }
    }
}
