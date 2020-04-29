package dji.thirdparty.rx.internal.operators;

import dji.thirdparty.rx.Completable;
import dji.thirdparty.rx.Subscription;
import dji.thirdparty.rx.subscriptions.CompositeSubscription;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;

public final class CompletableOnSubscribeMergeDelayErrorArray implements Completable.CompletableOnSubscribe {
    final Completable[] sources;

    public CompletableOnSubscribeMergeDelayErrorArray(Completable[] sources2) {
        this.sources = sources2;
    }

    public void call(Completable.CompletableSubscriber s) {
        final CompositeSubscription set = new CompositeSubscription();
        final AtomicInteger wip = new AtomicInteger(this.sources.length + 1);
        final Queue<Throwable> q = new ConcurrentLinkedQueue<>();
        s.onSubscribe(set);
        Completable[] arr$ = this.sources;
        int len$ = arr$.length;
        int i$ = 0;
        while (i$ < len$) {
            Completable c = arr$[i$];
            if (!set.isUnsubscribed()) {
                if (c == null) {
                    q.offer(new NullPointerException("A completable source is null"));
                    wip.decrementAndGet();
                } else {
                    final Completable.CompletableSubscriber completableSubscriber = s;
                    c.subscribe(new Completable.CompletableSubscriber() {
                        /* class dji.thirdparty.rx.internal.operators.CompletableOnSubscribeMergeDelayErrorArray.AnonymousClass1 */

                        public void onSubscribe(Subscription d) {
                            set.add(d);
                        }

                        public void onError(Throwable e) {
                            q.offer(e);
                            tryTerminate();
                        }

                        public void onCompleted() {
                            tryTerminate();
                        }

                        /* access modifiers changed from: package-private */
                        public void tryTerminate() {
                            if (wip.decrementAndGet() != 0) {
                                return;
                            }
                            if (q.isEmpty()) {
                                completableSubscriber.onCompleted();
                            } else {
                                completableSubscriber.onError(CompletableOnSubscribeMerge.collectErrors(q));
                            }
                        }
                    });
                }
                i$++;
            } else {
                return;
            }
        }
        if (wip.decrementAndGet() != 0) {
            return;
        }
        if (q.isEmpty()) {
            s.onCompleted();
        } else {
            s.onError(CompletableOnSubscribeMerge.collectErrors(q));
        }
    }
}
