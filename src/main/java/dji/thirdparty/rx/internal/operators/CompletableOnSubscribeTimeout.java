package dji.thirdparty.rx.internal.operators;

import dji.thirdparty.rx.Completable;
import dji.thirdparty.rx.Scheduler;
import dji.thirdparty.rx.Subscription;
import dji.thirdparty.rx.functions.Action0;
import dji.thirdparty.rx.plugins.RxJavaPlugins;
import dji.thirdparty.rx.subscriptions.CompositeSubscription;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicBoolean;

public final class CompletableOnSubscribeTimeout implements Completable.CompletableOnSubscribe {
    final Completable other;
    final Scheduler scheduler;
    final Completable source;
    final long timeout;
    final TimeUnit unit;

    public CompletableOnSubscribeTimeout(Completable source2, long timeout2, TimeUnit unit2, Scheduler scheduler2, Completable other2) {
        this.source = source2;
        this.timeout = timeout2;
        this.unit = unit2;
        this.scheduler = scheduler2;
        this.other = other2;
    }

    public void call(final Completable.CompletableSubscriber s) {
        final CompositeSubscription set = new CompositeSubscription();
        s.onSubscribe(set);
        final AtomicBoolean once = new AtomicBoolean();
        Scheduler.Worker w = this.scheduler.createWorker();
        set.add(w);
        w.schedule(new Action0() {
            /* class dji.thirdparty.rx.internal.operators.CompletableOnSubscribeTimeout.AnonymousClass1 */

            public void call() {
                if (once.compareAndSet(false, true)) {
                    set.clear();
                    if (CompletableOnSubscribeTimeout.this.other == null) {
                        s.onError(new TimeoutException());
                    } else {
                        CompletableOnSubscribeTimeout.this.other.subscribe(new Completable.CompletableSubscriber() {
                            /* class dji.thirdparty.rx.internal.operators.CompletableOnSubscribeTimeout.AnonymousClass1.AnonymousClass1 */

                            public void onSubscribe(Subscription d) {
                                set.add(d);
                            }

                            public void onError(Throwable e) {
                                set.unsubscribe();
                                s.onError(e);
                            }

                            public void onCompleted() {
                                set.unsubscribe();
                                s.onCompleted();
                            }
                        });
                    }
                }
            }
        }, this.timeout, this.unit);
        this.source.subscribe(new Completable.CompletableSubscriber() {
            /* class dji.thirdparty.rx.internal.operators.CompletableOnSubscribeTimeout.AnonymousClass2 */

            public void onSubscribe(Subscription d) {
                set.add(d);
            }

            public void onError(Throwable e) {
                if (once.compareAndSet(false, true)) {
                    set.unsubscribe();
                    s.onError(e);
                    return;
                }
                RxJavaPlugins.getInstance().getErrorHandler().handleError(e);
            }

            public void onCompleted() {
                if (once.compareAndSet(false, true)) {
                    set.unsubscribe();
                    s.onCompleted();
                }
            }
        });
    }
}
