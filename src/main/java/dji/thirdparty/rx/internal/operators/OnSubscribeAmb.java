package dji.thirdparty.rx.internal.operators;

import dji.thirdparty.rx.Observable;
import dji.thirdparty.rx.Producer;
import dji.thirdparty.rx.Subscriber;
import dji.thirdparty.rx.functions.Action0;
import dji.thirdparty.rx.subscriptions.Subscriptions;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicReference;

public final class OnSubscribeAmb<T> implements Observable.OnSubscribe<T> {
    final Iterable<? extends Observable<? extends T>> sources;

    public /* bridge */ /* synthetic */ void call(Object x0) {
        call((Subscriber) ((Subscriber) x0));
    }

    public static <T> Observable.OnSubscribe<T> amb(Observable<? extends T> o1, Observable<? extends T> o2) {
        List<Observable<? extends T>> sources2 = new ArrayList<>();
        sources2.add(o1);
        sources2.add(o2);
        return amb(sources2);
    }

    public static <T> Observable.OnSubscribe<T> amb(Observable<? extends T> o1, Observable<? extends T> o2, Observable<? extends T> o3) {
        List<Observable<? extends T>> sources2 = new ArrayList<>();
        sources2.add(o1);
        sources2.add(o2);
        sources2.add(o3);
        return amb(sources2);
    }

    public static <T> Observable.OnSubscribe<T> amb(Observable<? extends T> o1, Observable<? extends T> o2, Observable<? extends T> o3, Observable<? extends T> o4) {
        List<Observable<? extends T>> sources2 = new ArrayList<>();
        sources2.add(o1);
        sources2.add(o2);
        sources2.add(o3);
        sources2.add(o4);
        return amb(sources2);
    }

    public static <T> Observable.OnSubscribe<T> amb(Observable<? extends T> o1, Observable<? extends T> o2, Observable<? extends T> o3, Observable<? extends T> o4, Observable<? extends T> o5) {
        List<Observable<? extends T>> sources2 = new ArrayList<>();
        sources2.add(o1);
        sources2.add(o2);
        sources2.add(o3);
        sources2.add(o4);
        sources2.add(o5);
        return amb(sources2);
    }

    public static <T> Observable.OnSubscribe<T> amb(Observable<? extends T> o1, Observable<? extends T> o2, Observable<? extends T> o3, Observable<? extends T> o4, Observable<? extends T> o5, Observable<? extends T> o6) {
        List<Observable<? extends T>> sources2 = new ArrayList<>();
        sources2.add(o1);
        sources2.add(o2);
        sources2.add(o3);
        sources2.add(o4);
        sources2.add(o5);
        sources2.add(o6);
        return amb(sources2);
    }

    public static <T> Observable.OnSubscribe<T> amb(Observable<? extends T> o1, Observable<? extends T> o2, Observable<? extends T> o3, Observable<? extends T> o4, Observable<? extends T> o5, Observable<? extends T> o6, Observable<? extends T> o7) {
        List<Observable<? extends T>> sources2 = new ArrayList<>();
        sources2.add(o1);
        sources2.add(o2);
        sources2.add(o3);
        sources2.add(o4);
        sources2.add(o5);
        sources2.add(o6);
        sources2.add(o7);
        return amb(sources2);
    }

    public static <T> Observable.OnSubscribe<T> amb(Observable<? extends T> o1, Observable<? extends T> o2, Observable<? extends T> o3, Observable<? extends T> o4, Observable<? extends T> o5, Observable<? extends T> o6, Observable<? extends T> o7, Observable<? extends T> o8) {
        List<Observable<? extends T>> sources2 = new ArrayList<>();
        sources2.add(o1);
        sources2.add(o2);
        sources2.add(o3);
        sources2.add(o4);
        sources2.add(o5);
        sources2.add(o6);
        sources2.add(o7);
        sources2.add(o8);
        return amb(sources2);
    }

    public static <T> Observable.OnSubscribe<T> amb(Observable<? extends T> o1, Observable<? extends T> o2, Observable<? extends T> o3, Observable<? extends T> o4, Observable<? extends T> o5, Observable<? extends T> o6, Observable<? extends T> o7, Observable<? extends T> o8, Observable<? extends T> o9) {
        List<Observable<? extends T>> sources2 = new ArrayList<>();
        sources2.add(o1);
        sources2.add(o2);
        sources2.add(o3);
        sources2.add(o4);
        sources2.add(o5);
        sources2.add(o6);
        sources2.add(o7);
        sources2.add(o8);
        sources2.add(o9);
        return amb(sources2);
    }

    public static <T> Observable.OnSubscribe<T> amb(Iterable<? extends Observable<? extends T>> sources2) {
        return new OnSubscribeAmb(sources2);
    }

    private static final class AmbSubscriber<T> extends Subscriber<T> {
        private boolean chosen;
        private final Selection<T> selection;
        private final Subscriber<? super T> subscriber;

        AmbSubscriber(long requested, Subscriber<? super T> subscriber2, Selection<T> selection2) {
            this.subscriber = subscriber2;
            this.selection = selection2;
            request(requested);
        }

        /* access modifiers changed from: private */
        public void requestMore(long n) {
            request(n);
        }

        public void onNext(T t) {
            if (isSelected()) {
                this.subscriber.onNext(t);
            }
        }

        public void onCompleted() {
            if (isSelected()) {
                this.subscriber.onCompleted();
            }
        }

        public void onError(Throwable e) {
            if (isSelected()) {
                this.subscriber.onError(e);
            }
        }

        private boolean isSelected() {
            if (this.chosen) {
                return true;
            }
            if (this.selection.choice.get() == this) {
                this.chosen = true;
                return true;
            } else if (this.selection.choice.compareAndSet(null, this)) {
                this.selection.unsubscribeOthers(this);
                this.chosen = true;
                return true;
            } else {
                this.selection.unsubscribeLosers();
                return false;
            }
        }
    }

    private static class Selection<T> {
        final Collection<AmbSubscriber<T>> ambSubscribers;
        final AtomicReference<AmbSubscriber<T>> choice;

        private Selection() {
            this.choice = new AtomicReference<>();
            this.ambSubscribers = new ConcurrentLinkedQueue();
        }

        public void unsubscribeLosers() {
            AmbSubscriber<T> winner = this.choice.get();
            if (winner != null) {
                unsubscribeOthers(winner);
            }
        }

        public void unsubscribeOthers(AmbSubscriber<T> notThis) {
            for (AmbSubscriber<T> other : this.ambSubscribers) {
                if (other != notThis) {
                    other.unsubscribe();
                }
            }
            this.ambSubscribers.clear();
        }
    }

    private OnSubscribeAmb(Iterable<? extends Observable<? extends T>> sources2) {
        this.sources = sources2;
    }

    public void call(Subscriber<? super T> subscriber) {
        final Selection<T> selection = new Selection<>();
        final AtomicReference<AmbSubscriber<T>> choice = selection.choice;
        subscriber.add(Subscriptions.create(new Action0() {
            /* class dji.thirdparty.rx.internal.operators.OnSubscribeAmb.AnonymousClass1 */

            public void call() {
                AmbSubscriber<T> c = (AmbSubscriber) choice.get();
                if (c != null) {
                    c.unsubscribe();
                }
                OnSubscribeAmb.unsubscribeAmbSubscribers(selection.ambSubscribers);
            }
        }));
        for (Observable<? extends T> source : this.sources) {
            if (subscriber.isUnsubscribed()) {
                break;
            }
            AmbSubscriber<T> ambSubscriber = new AmbSubscriber<>(0, subscriber, selection);
            selection.ambSubscribers.add(ambSubscriber);
            AmbSubscriber<T> c = choice.get();
            if (c != null) {
                selection.unsubscribeOthers(c);
                return;
            }
            source.unsafeSubscribe(ambSubscriber);
        }
        if (subscriber.isUnsubscribed()) {
            unsubscribeAmbSubscribers(selection.ambSubscribers);
        }
        subscriber.setProducer(new Producer() {
            /* class dji.thirdparty.rx.internal.operators.OnSubscribeAmb.AnonymousClass2 */

            public void request(long n) {
                AmbSubscriber<T> c = (AmbSubscriber) choice.get();
                if (c != null) {
                    c.requestMore(n);
                    return;
                }
                for (AmbSubscriber<T> ambSubscriber : selection.ambSubscribers) {
                    if (!ambSubscriber.isUnsubscribed()) {
                        if (choice.get() == ambSubscriber) {
                            ambSubscriber.requestMore(n);
                            return;
                        }
                        ambSubscriber.requestMore(n);
                    }
                }
            }
        });
    }

    static <T> void unsubscribeAmbSubscribers(Collection<AmbSubscriber<T>> ambSubscribers) {
        if (!ambSubscribers.isEmpty()) {
            for (AmbSubscriber<T> other : ambSubscribers) {
                other.unsubscribe();
            }
            ambSubscribers.clear();
        }
    }
}
