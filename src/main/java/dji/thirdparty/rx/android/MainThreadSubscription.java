package dji.thirdparty.rx.android;

import android.os.Looper;
import dji.thirdparty.rx.Subscription;
import dji.thirdparty.rx.android.schedulers.AndroidSchedulers;
import dji.thirdparty.rx.functions.Action0;
import java.util.concurrent.atomic.AtomicBoolean;

public abstract class MainThreadSubscription implements Subscription {
    private final AtomicBoolean unsubscribed = new AtomicBoolean();

    /* access modifiers changed from: protected */
    public abstract void onUnsubscribe();

    public static void verifyMainThread() {
        if (Looper.myLooper() != Looper.getMainLooper()) {
            throw new IllegalStateException("Expected to be called on the main thread but was " + Thread.currentThread().getName());
        }
    }

    public final boolean isUnsubscribed() {
        return this.unsubscribed.get();
    }

    public final void unsubscribe() {
        if (!this.unsubscribed.compareAndSet(false, true)) {
            return;
        }
        if (Looper.myLooper() == Looper.getMainLooper()) {
            onUnsubscribe();
        } else {
            AndroidSchedulers.mainThread().createWorker().schedule(new Action0() {
                /* class dji.thirdparty.rx.android.MainThreadSubscription.AnonymousClass1 */

                public void call() {
                    MainThreadSubscription.this.onUnsubscribe();
                }
            });
        }
    }
}
