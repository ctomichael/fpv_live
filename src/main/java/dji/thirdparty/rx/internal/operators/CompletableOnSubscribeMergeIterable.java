package dji.thirdparty.rx.internal.operators;

import dji.thirdparty.rx.Completable;
import dji.thirdparty.rx.Subscription;
import dji.thirdparty.rx.plugins.RxJavaPlugins;
import dji.thirdparty.rx.subscriptions.CompositeSubscription;
import java.util.Iterator;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public final class CompletableOnSubscribeMergeIterable implements Completable.CompletableOnSubscribe {
    final Iterable<? extends Completable> sources;

    public CompletableOnSubscribeMergeIterable(Iterable<? extends Completable> sources2) {
        this.sources = sources2;
    }

    public void call(Completable.CompletableSubscriber s) {
        final CompositeSubscription set = new CompositeSubscription();
        final AtomicInteger wip = new AtomicInteger(1);
        final AtomicBoolean once = new AtomicBoolean();
        s.onSubscribe(set);
        try {
            Iterator<? extends Completable> iterator = this.sources.iterator();
            if (iterator == null) {
                s.onError(new NullPointerException("The source iterator returned is null"));
                return;
            }
            while (!set.isUnsubscribed()) {
                try {
                    if (!iterator.hasNext()) {
                        if (wip.decrementAndGet() == 0 && once.compareAndSet(false, true)) {
                            s.onCompleted();
                            return;
                        }
                        return;
                    } else if (!set.isUnsubscribed()) {
                        try {
                            Completable c = (Completable) iterator.next();
                            if (set.isUnsubscribed()) {
                                return;
                            }
                            if (c == null) {
                                set.unsubscribe();
                                NullPointerException npe = new NullPointerException("A completable source is null");
                                if (once.compareAndSet(false, true)) {
                                    s.onError(npe);
                                    return;
                                } else {
                                    RxJavaPlugins.getInstance().getErrorHandler().handleError(npe);
                                    return;
                                }
                            } else {
                                wip.getAndIncrement();
                                final Completable.CompletableSubscriber completableSubscriber = s;
                                c.subscribe(new Completable.CompletableSubscriber() {
                                    /* class dji.thirdparty.rx.internal.operators.CompletableOnSubscribeMergeIterable.AnonymousClass1 */

                                    public void onSubscribe(Subscription d) {
                                        set.add(d);
                                    }

                                    public void onError(Throwable e) {
                                        set.unsubscribe();
                                        if (once.compareAndSet(false, true)) {
                                            completableSubscriber.onError(e);
                                        } else {
                                            RxJavaPlugins.getInstance().getErrorHandler().handleError(e);
                                        }
                                    }

                                    public void onCompleted() {
                                        if (wip.decrementAndGet() == 0 && once.compareAndSet(false, true)) {
                                            completableSubscriber.onCompleted();
                                        }
                                    }
                                });
                            }
                        } catch (Throwable e) {
                            set.unsubscribe();
                            if (once.compareAndSet(false, true)) {
                                s.onError(e);
                                return;
                            } else {
                                RxJavaPlugins.getInstance().getErrorHandler().handleError(e);
                                return;
                            }
                        }
                    } else {
                        return;
                    }
                } catch (Throwable e2) {
                    set.unsubscribe();
                    if (once.compareAndSet(false, true)) {
                        s.onError(e2);
                        return;
                    } else {
                        RxJavaPlugins.getInstance().getErrorHandler().handleError(e2);
                        return;
                    }
                }
            }
        } catch (Throwable e3) {
            s.onError(e3);
        }
    }
}
