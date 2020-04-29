package io.reactivex.internal.util;

import io.reactivex.disposables.Disposable;
import io.reactivex.exceptions.ProtocolViolationException;
import io.reactivex.internal.disposables.DisposableHelper;
import io.reactivex.internal.functions.ObjectHelper;
import io.reactivex.internal.subscriptions.SubscriptionHelper;
import io.reactivex.plugins.RxJavaPlugins;
import java.util.concurrent.atomic.AtomicReference;
import org.reactivestreams.Subscription;

public final class EndConsumerHelper {
    private EndConsumerHelper() {
        throw new IllegalStateException("No instances!");
    }

    public static boolean validate(Disposable upstream, Disposable next, Class<?> observer) {
        ObjectHelper.requireNonNull(next, "next is null");
        if (upstream == null) {
            return true;
        }
        next.dispose();
        if (upstream != DisposableHelper.DISPOSED) {
            reportDoubleSubscription(observer);
        }
        return false;
    }

    public static boolean setOnce(AtomicReference<Disposable> upstream, Disposable next, Class<?> observer) {
        ObjectHelper.requireNonNull(next, "next is null");
        if (upstream.compareAndSet(null, next)) {
            return true;
        }
        next.dispose();
        if (upstream.get() != DisposableHelper.DISPOSED) {
            reportDoubleSubscription(observer);
        }
        return false;
    }

    public static boolean validate(Subscription upstream, Subscription next, Class<?> subscriber) {
        ObjectHelper.requireNonNull(next, "next is null");
        if (upstream == null) {
            return true;
        }
        next.cancel();
        if (upstream != SubscriptionHelper.CANCELLED) {
            reportDoubleSubscription(subscriber);
        }
        return false;
    }

    public static boolean setOnce(AtomicReference<Subscription> upstream, Subscription next, Class<?> subscriber) {
        ObjectHelper.requireNonNull(next, "next is null");
        if (upstream.compareAndSet(null, next)) {
            return true;
        }
        next.cancel();
        if (upstream.get() != SubscriptionHelper.CANCELLED) {
            reportDoubleSubscription(subscriber);
        }
        return false;
    }

    public static String composeMessage(String consumer) {
        return "It is not allowed to subscribe with a(n) " + consumer + " multiple times. Please create a fresh instance of " + consumer + " and subscribe that to the target source instead.";
    }

    public static void reportDoubleSubscription(Class<?> consumer) {
        RxJavaPlugins.onError(new ProtocolViolationException(composeMessage(consumer.getName())));
    }
}
