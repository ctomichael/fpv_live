package dji.thirdparty.io.reactivex.internal.subscribers;

import dji.thirdparty.io.reactivex.internal.subscriptions.SubscriptionHelper;
import dji.thirdparty.io.reactivex.internal.util.ExceptionHelper;
import java.util.concurrent.CountDownLatch;
import kotlin.jvm.internal.LongCompanionObject;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

public abstract class BlockingBaseSubscriber<T> extends CountDownLatch implements Subscriber<T> {
    volatile boolean cancelled;
    Throwable error;
    Subscription s;
    T value;

    public BlockingBaseSubscriber() {
        super(1);
    }

    public final void onSubscribe(Subscription s2) {
        if (SubscriptionHelper.validate(this.s, s2)) {
            this.s = s2;
            if (!this.cancelled) {
                s2.request(LongCompanionObject.MAX_VALUE);
                if (this.cancelled) {
                    this.s = SubscriptionHelper.CANCELLED;
                    s2.cancel();
                }
            }
        }
    }

    public final void onComplete() {
        countDown();
    }

    public final T blockingGet() {
        if (getCount() != 0) {
            try {
                await();
            } catch (InterruptedException ex) {
                Subscription s2 = this.s;
                this.s = SubscriptionHelper.CANCELLED;
                if (s2 != null) {
                    s2.cancel();
                }
                throw ExceptionHelper.wrapOrThrow(ex);
            }
        }
        Throwable e = this.error;
        if (e == null) {
            return this.value;
        }
        throw ExceptionHelper.wrapOrThrow(e);
    }
}
