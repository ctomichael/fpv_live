package dji.thirdparty.rx.internal.operators;

import dji.thirdparty.rx.Completable;
import dji.thirdparty.rx.Subscription;
import dji.thirdparty.rx.plugins.RxJavaPlugins;
import dji.thirdparty.rx.subscriptions.CompositeSubscription;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public final class CompletableOnSubscribeMergeArray implements Completable.CompletableOnSubscribe {
    final Completable[] sources;

    public CompletableOnSubscribeMergeArray(Completable[] sources2) {
        this.sources = sources2;
    }

    public void call(Completable.CompletableSubscriber s) {
        final CompositeSubscription set = new CompositeSubscription();
        final AtomicInteger wip = new AtomicInteger(this.sources.length + 1);
        final AtomicBoolean once = new AtomicBoolean();
        s.onSubscribe(set);
        Completable[] arr$ = this.sources;
        int len$ = arr$.length;
        int i$ = 0;
        while (i$ < len$) {
            Completable c = arr$[i$];
            if (!set.isUnsubscribed()) {
                if (c == null) {
                    set.unsubscribe();
                    NullPointerException npe = new NullPointerException("A completable source is null");
                    if (once.compareAndSet(false, true)) {
                        s.onError(npe);
                        return;
                    }
                    RxJavaPlugins.getInstance().getErrorHandler().handleError(npe);
                }
                final Completable.CompletableSubscriber completableSubscriber = s;
                c.subscribe(new Completable.CompletableSubscriber() {
                    /* class dji.thirdparty.rx.internal.operators.CompletableOnSubscribeMergeArray.AnonymousClass1 */

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
                i$++;
            } else {
                return;
            }
        }
        if (wip.decrementAndGet() == 0 && once.compareAndSet(false, true)) {
            s.onCompleted();
        }
    }
}
