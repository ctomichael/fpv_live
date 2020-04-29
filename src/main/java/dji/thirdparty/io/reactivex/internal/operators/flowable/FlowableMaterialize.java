package dji.thirdparty.io.reactivex.internal.operators.flowable;

import dji.thirdparty.io.reactivex.Notification;
import dji.thirdparty.io.reactivex.internal.subscribers.SinglePostCompleteSubscriber;
import dji.thirdparty.io.reactivex.plugins.RxJavaPlugins;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;

public final class FlowableMaterialize<T> extends AbstractFlowableWithUpstream<T, Notification<T>> {
    public FlowableMaterialize(Publisher<T> source) {
        super(source);
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(Subscriber<? super Notification<T>> s) {
        this.source.subscribe(new MaterializeSubscriber(s));
    }

    static final class MaterializeSubscriber<T> extends SinglePostCompleteSubscriber<T, Notification<T>> {
        private static final long serialVersionUID = -3740826063558713822L;

        /* access modifiers changed from: protected */
        public /* bridge */ /* synthetic */ void onDrop(Object x0) {
            onDrop((Notification) ((Notification) x0));
        }

        MaterializeSubscriber(Subscriber<? super Notification<T>> actual) {
            super(actual);
        }

        public void onNext(T t) {
            this.produced++;
            this.actual.onNext(Notification.createOnNext(t));
        }

        public void onError(Throwable t) {
            complete(Notification.createOnError(t));
        }

        public void onComplete() {
            complete(Notification.createOnComplete());
        }

        /* access modifiers changed from: protected */
        public void onDrop(Notification<T> n) {
            if (n.isOnError()) {
                RxJavaPlugins.onError(n.getError());
            }
        }
    }
}
