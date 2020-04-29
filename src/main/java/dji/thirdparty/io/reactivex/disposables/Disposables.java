package dji.thirdparty.io.reactivex.disposables;

import dji.thirdparty.io.reactivex.functions.Action;
import dji.thirdparty.io.reactivex.internal.disposables.EmptyDisposable;
import dji.thirdparty.io.reactivex.internal.functions.Functions;
import dji.thirdparty.io.reactivex.internal.functions.ObjectHelper;
import java.util.concurrent.Future;
import org.reactivestreams.Subscription;

public final class Disposables {
    private Disposables() {
        throw new IllegalStateException("No instances!");
    }

    public static Disposable fromRunnable(Runnable run) {
        ObjectHelper.requireNonNull(run, "run is null");
        return new RunnableDisposable(run);
    }

    public static Disposable fromAction(Action run) {
        ObjectHelper.requireNonNull(run, "run is null");
        return new ActionDisposable(run);
    }

    public static Disposable fromFuture(Future<?> future) {
        ObjectHelper.requireNonNull(future, "future is null");
        return fromFuture(future, true);
    }

    public static Disposable fromFuture(Future<?> future, boolean allowInterrupt) {
        ObjectHelper.requireNonNull(future, "future is null");
        return new FutureDisposable(future, allowInterrupt);
    }

    public static Disposable fromSubscription(Subscription subscription) {
        ObjectHelper.requireNonNull(subscription, "subscription is null");
        return new SubscriptionDisposable(subscription);
    }

    public static Disposable empty() {
        return fromRunnable(Functions.EMPTY_RUNNABLE);
    }

    public static Disposable disposed() {
        return EmptyDisposable.INSTANCE;
    }
}
