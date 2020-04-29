package dji.thirdparty.io.reactivex.internal.operators.flowable;

import dji.thirdparty.io.reactivex.functions.Action;
import dji.thirdparty.io.reactivex.functions.Consumer;
import dji.thirdparty.io.reactivex.internal.functions.Functions;
import dji.thirdparty.io.reactivex.internal.subscribers.BlockingSubscriber;
import dji.thirdparty.io.reactivex.internal.subscribers.LambdaSubscriber;
import dji.thirdparty.io.reactivex.internal.util.BlockingHelper;
import dji.thirdparty.io.reactivex.internal.util.BlockingIgnoringReceiver;
import dji.thirdparty.io.reactivex.internal.util.ExceptionHelper;
import dji.thirdparty.io.reactivex.internal.util.NotificationLite;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;

public final class FlowableBlockingSubscribe {
    private FlowableBlockingSubscribe() {
        throw new IllegalStateException("No instances!");
    }

    public static <T> void subscribe(Publisher<? extends T> o, Subscriber<? super T> subscriber) {
        Object v;
        BlockingQueue<Object> queue = new LinkedBlockingQueue<>();
        BlockingSubscriber<T> bs = new BlockingSubscriber<>(queue);
        o.subscribe(bs);
        do {
            try {
                if (!bs.isCancelled()) {
                    v = queue.poll();
                    if (v == null) {
                        if (!bs.isCancelled()) {
                            v = queue.take();
                        } else {
                            return;
                        }
                    }
                    if (bs.isCancelled() || o == BlockingSubscriber.TERMINATED) {
                        return;
                    }
                } else {
                    return;
                }
            } catch (InterruptedException e) {
                bs.cancel();
                subscriber.onError(e);
                return;
            }
        } while (!NotificationLite.acceptFull(v, subscriber));
    }

    public static <T> void subscribe(Publisher<? extends T> o) {
        BlockingIgnoringReceiver callback = new BlockingIgnoringReceiver();
        LambdaSubscriber<T> ls = new LambdaSubscriber<>(Functions.emptyConsumer(), callback, callback, Functions.REQUEST_MAX);
        o.subscribe(ls);
        BlockingHelper.awaitForComplete(callback, ls);
        Throwable e = callback.error;
        if (e != null) {
            throw ExceptionHelper.wrapOrThrow(e);
        }
    }

    public static <T> void subscribe(Publisher<? extends T> o, Consumer<? super T> onNext, Consumer<? super Throwable> onError, Action onComplete) {
        subscribe(o, new LambdaSubscriber(onNext, onError, onComplete, Functions.REQUEST_MAX));
    }
}
