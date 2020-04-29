package dji.thirdparty.io.reactivex.plugins;

import dji.thirdparty.io.reactivex.Completable;
import dji.thirdparty.io.reactivex.CompletableObserver;
import dji.thirdparty.io.reactivex.Flowable;
import dji.thirdparty.io.reactivex.Maybe;
import dji.thirdparty.io.reactivex.MaybeObserver;
import dji.thirdparty.io.reactivex.Observable;
import dji.thirdparty.io.reactivex.Observer;
import dji.thirdparty.io.reactivex.Scheduler;
import dji.thirdparty.io.reactivex.Single;
import dji.thirdparty.io.reactivex.SingleObserver;
import dji.thirdparty.io.reactivex.flowables.ConnectableFlowable;
import dji.thirdparty.io.reactivex.functions.BiFunction;
import dji.thirdparty.io.reactivex.functions.Consumer;
import dji.thirdparty.io.reactivex.functions.Function;
import dji.thirdparty.io.reactivex.internal.functions.ObjectHelper;
import dji.thirdparty.io.reactivex.internal.util.ExceptionHelper;
import dji.thirdparty.io.reactivex.observables.ConnectableObservable;
import java.util.concurrent.Callable;
import org.reactivestreams.Subscriber;

public final class RxJavaPlugins {
    static volatile Consumer<Throwable> errorHandler;
    static volatile boolean lockdown;
    static volatile Function<Completable, Completable> onCompletableAssembly;
    static volatile BiFunction<Completable, CompletableObserver, CompletableObserver> onCompletableSubscribe;
    static volatile Function<Scheduler, Scheduler> onComputationHandler;
    static volatile Function<ConnectableFlowable, ConnectableFlowable> onConnectableFlowableAssembly;
    static volatile Function<ConnectableObservable, ConnectableObservable> onConnectableObservableAssembly;
    static volatile Function<Flowable, Flowable> onFlowableAssembly;
    static volatile BiFunction<Flowable, Subscriber, Subscriber> onFlowableSubscribe;
    static volatile Function<Callable<Scheduler>, Scheduler> onInitComputationHandler;
    static volatile Function<Callable<Scheduler>, Scheduler> onInitIoHandler;
    static volatile Function<Callable<Scheduler>, Scheduler> onInitNewThreadHandler;
    static volatile Function<Callable<Scheduler>, Scheduler> onInitSingleHandler;
    static volatile Function<Scheduler, Scheduler> onIoHandler;
    static volatile Function<Maybe, Maybe> onMaybeAssembly;
    static volatile BiFunction<Maybe, MaybeObserver, MaybeObserver> onMaybeSubscribe;
    static volatile Function<Scheduler, Scheduler> onNewThreadHandler;
    static volatile Function<Observable, Observable> onObservableAssembly;
    static volatile BiFunction<Observable, Observer, Observer> onObservableSubscribe;
    static volatile Function<Runnable, Runnable> onScheduleHandler;
    static volatile Function<Single, Single> onSingleAssembly;
    static volatile Function<Scheduler, Scheduler> onSingleHandler;
    static volatile BiFunction<Single, SingleObserver, SingleObserver> onSingleSubscribe;

    public static void lockdown() {
        lockdown = true;
    }

    public static boolean isLockdown() {
        return lockdown;
    }

    public static Function<Scheduler, Scheduler> getComputationSchedulerHandler() {
        return onComputationHandler;
    }

    public static Consumer<Throwable> getErrorHandler() {
        return errorHandler;
    }

    public static Function<Callable<Scheduler>, Scheduler> getInitComputationSchedulerHandler() {
        return onInitComputationHandler;
    }

    public static Function<Callable<Scheduler>, Scheduler> getInitIoSchedulerHandler() {
        return onInitIoHandler;
    }

    public static Function<Callable<Scheduler>, Scheduler> getInitNewThreadSchedulerHandler() {
        return onInitNewThreadHandler;
    }

    public static Function<Callable<Scheduler>, Scheduler> getInitSingleSchedulerHandler() {
        return onInitSingleHandler;
    }

    public static Function<Scheduler, Scheduler> getIoSchedulerHandler() {
        return onIoHandler;
    }

    public static Function<Scheduler, Scheduler> getNewThreadSchedulerHandler() {
        return onNewThreadHandler;
    }

    public static Function<Runnable, Runnable> getScheduleHandler() {
        return onScheduleHandler;
    }

    public static Function<Scheduler, Scheduler> getSingleSchedulerHandler() {
        return onSingleHandler;
    }

    public static Scheduler initComputationScheduler(Callable<Scheduler> defaultScheduler) {
        ObjectHelper.requireNonNull(defaultScheduler, "Scheduler Callable can't be null");
        Function<Callable<Scheduler>, Scheduler> f = onInitComputationHandler;
        if (f == null) {
            return callRequireNonNull(defaultScheduler);
        }
        return applyRequireNonNull(f, defaultScheduler);
    }

    public static Scheduler initIoScheduler(Callable<Scheduler> defaultScheduler) {
        ObjectHelper.requireNonNull(defaultScheduler, "Scheduler Callable can't be null");
        Function<Callable<Scheduler>, Scheduler> f = onInitIoHandler;
        if (f == null) {
            return callRequireNonNull(defaultScheduler);
        }
        return applyRequireNonNull(f, defaultScheduler);
    }

    public static Scheduler initNewThreadScheduler(Callable<Scheduler> defaultScheduler) {
        ObjectHelper.requireNonNull(defaultScheduler, "Scheduler Callable can't be null");
        Function<Callable<Scheduler>, Scheduler> f = onInitNewThreadHandler;
        if (f == null) {
            return callRequireNonNull(defaultScheduler);
        }
        return applyRequireNonNull(f, defaultScheduler);
    }

    public static Scheduler initSingleScheduler(Callable<Scheduler> defaultScheduler) {
        ObjectHelper.requireNonNull(defaultScheduler, "Scheduler Callable can't be null");
        Function<Callable<Scheduler>, Scheduler> f = onInitSingleHandler;
        if (f == null) {
            return callRequireNonNull(defaultScheduler);
        }
        return applyRequireNonNull(f, defaultScheduler);
    }

    public static Scheduler onComputationScheduler(Scheduler defaultScheduler) {
        Function<Scheduler, Scheduler> f = onComputationHandler;
        return f == null ? defaultScheduler : (Scheduler) apply(f, defaultScheduler);
    }

    public static void onError(Throwable error) {
        Consumer<Throwable> f = errorHandler;
        if (error == null) {
            error = new NullPointerException("onError called with null. Null values are generally not allowed in 2.x operators and sources.");
        }
        if (f != null) {
            try {
                f.accept(error);
                return;
            } catch (Throwable e) {
                e.printStackTrace();
                uncaught(e);
            }
        }
        error.printStackTrace();
        uncaught(error);
    }

    static void uncaught(Throwable error) {
        Thread currentThread = Thread.currentThread();
        currentThread.getUncaughtExceptionHandler().uncaughtException(currentThread, error);
    }

    public static Scheduler onIoScheduler(Scheduler defaultScheduler) {
        Function<Scheduler, Scheduler> f = onIoHandler;
        return f == null ? defaultScheduler : (Scheduler) apply(f, defaultScheduler);
    }

    public static Scheduler onNewThreadScheduler(Scheduler defaultScheduler) {
        Function<Scheduler, Scheduler> f = onNewThreadHandler;
        return f == null ? defaultScheduler : (Scheduler) apply(f, defaultScheduler);
    }

    public static Runnable onSchedule(Runnable run) {
        Function<Runnable, Runnable> f = onScheduleHandler;
        return f == null ? run : (Runnable) apply(f, run);
    }

    public static Scheduler onSingleScheduler(Scheduler defaultScheduler) {
        Function<Scheduler, Scheduler> f = onSingleHandler;
        return f == null ? defaultScheduler : (Scheduler) apply(f, defaultScheduler);
    }

    public static void reset() {
        setErrorHandler(null);
        setScheduleHandler(null);
        setComputationSchedulerHandler(null);
        setInitComputationSchedulerHandler(null);
        setIoSchedulerHandler(null);
        setInitIoSchedulerHandler(null);
        setSingleSchedulerHandler(null);
        setInitSingleSchedulerHandler(null);
        setNewThreadSchedulerHandler(null);
        setInitNewThreadSchedulerHandler(null);
        setOnFlowableAssembly(null);
        setOnFlowableSubscribe(null);
        setOnObservableAssembly(null);
        setOnObservableSubscribe(null);
        setOnSingleAssembly(null);
        setOnSingleSubscribe(null);
        setOnCompletableAssembly(null);
        setOnCompletableSubscribe(null);
        setOnConnectableFlowableAssembly(null);
        setOnConnectableObservableAssembly(null);
        setOnMaybeAssembly(null);
        setOnMaybeSubscribe(null);
    }

    public static void setComputationSchedulerHandler(Function<Scheduler, Scheduler> handler) {
        if (lockdown) {
            throw new IllegalStateException("Plugins can't be changed anymore");
        }
        onComputationHandler = handler;
    }

    public static void setErrorHandler(Consumer<Throwable> handler) {
        if (lockdown) {
            throw new IllegalStateException("Plugins can't be changed anymore");
        }
        errorHandler = handler;
    }

    public static void setInitComputationSchedulerHandler(Function<Callable<Scheduler>, Scheduler> handler) {
        if (lockdown) {
            throw new IllegalStateException("Plugins can't be changed anymore");
        }
        onInitComputationHandler = handler;
    }

    public static void setInitIoSchedulerHandler(Function<Callable<Scheduler>, Scheduler> handler) {
        if (lockdown) {
            throw new IllegalStateException("Plugins can't be changed anymore");
        }
        onInitIoHandler = handler;
    }

    public static void setInitNewThreadSchedulerHandler(Function<Callable<Scheduler>, Scheduler> handler) {
        if (lockdown) {
            throw new IllegalStateException("Plugins can't be changed anymore");
        }
        onInitNewThreadHandler = handler;
    }

    public static void setInitSingleSchedulerHandler(Function<Callable<Scheduler>, Scheduler> handler) {
        if (lockdown) {
            throw new IllegalStateException("Plugins can't be changed anymore");
        }
        onInitSingleHandler = handler;
    }

    public static void setIoSchedulerHandler(Function<Scheduler, Scheduler> handler) {
        if (lockdown) {
            throw new IllegalStateException("Plugins can't be changed anymore");
        }
        onIoHandler = handler;
    }

    public static void setNewThreadSchedulerHandler(Function<Scheduler, Scheduler> handler) {
        if (lockdown) {
            throw new IllegalStateException("Plugins can't be changed anymore");
        }
        onNewThreadHandler = handler;
    }

    public static void setScheduleHandler(Function<Runnable, Runnable> handler) {
        if (lockdown) {
            throw new IllegalStateException("Plugins can't be changed anymore");
        }
        onScheduleHandler = handler;
    }

    public static void setSingleSchedulerHandler(Function<Scheduler, Scheduler> handler) {
        if (lockdown) {
            throw new IllegalStateException("Plugins can't be changed anymore");
        }
        onSingleHandler = handler;
    }

    static void unlock() {
        lockdown = false;
    }

    public static Function<Completable, Completable> getOnCompletableAssembly() {
        return onCompletableAssembly;
    }

    public static BiFunction<Completable, CompletableObserver, CompletableObserver> getOnCompletableSubscribe() {
        return onCompletableSubscribe;
    }

    public static Function<Flowable, Flowable> getOnFlowableAssembly() {
        return onFlowableAssembly;
    }

    public static Function<ConnectableFlowable, ConnectableFlowable> getOnConnectableFlowableAssembly() {
        return onConnectableFlowableAssembly;
    }

    public static BiFunction<Flowable, Subscriber, Subscriber> getOnFlowableSubscribe() {
        return onFlowableSubscribe;
    }

    public static BiFunction<Maybe, MaybeObserver, MaybeObserver> getOnMaybeSubscribe() {
        return onMaybeSubscribe;
    }

    public static Function<Maybe, Maybe> getOnMaybeAssembly() {
        return onMaybeAssembly;
    }

    public static Function<Single, Single> getOnSingleAssembly() {
        return onSingleAssembly;
    }

    public static BiFunction<Single, SingleObserver, SingleObserver> getOnSingleSubscribe() {
        return onSingleSubscribe;
    }

    public static Function<Observable, Observable> getOnObservableAssembly() {
        return onObservableAssembly;
    }

    public static Function<ConnectableObservable, ConnectableObservable> getOnConnectableObservableAssembly() {
        return onConnectableObservableAssembly;
    }

    public static BiFunction<Observable, Observer, Observer> getOnObservableSubscribe() {
        return onObservableSubscribe;
    }

    public static void setOnCompletableAssembly(Function<Completable, Completable> onCompletableAssembly2) {
        if (lockdown) {
            throw new IllegalStateException("Plugins can't be changed anymore");
        }
        onCompletableAssembly = onCompletableAssembly2;
    }

    public static void setOnCompletableSubscribe(BiFunction<Completable, CompletableObserver, CompletableObserver> onCompletableSubscribe2) {
        if (lockdown) {
            throw new IllegalStateException("Plugins can't be changed anymore");
        }
        onCompletableSubscribe = onCompletableSubscribe2;
    }

    public static void setOnFlowableAssembly(Function<Flowable, Flowable> onFlowableAssembly2) {
        if (lockdown) {
            throw new IllegalStateException("Plugins can't be changed anymore");
        }
        onFlowableAssembly = onFlowableAssembly2;
    }

    public static void setOnMaybeAssembly(Function<Maybe, Maybe> onMaybeAssembly2) {
        if (lockdown) {
            throw new IllegalStateException("Plugins can't be changed anymore");
        }
        onMaybeAssembly = onMaybeAssembly2;
    }

    public static void setOnConnectableFlowableAssembly(Function<ConnectableFlowable, ConnectableFlowable> onConnectableFlowableAssembly2) {
        if (lockdown) {
            throw new IllegalStateException("Plugins can't be changed anymore");
        }
        onConnectableFlowableAssembly = onConnectableFlowableAssembly2;
    }

    public static void setOnFlowableSubscribe(BiFunction<Flowable, Subscriber, Subscriber> onFlowableSubscribe2) {
        if (lockdown) {
            throw new IllegalStateException("Plugins can't be changed anymore");
        }
        onFlowableSubscribe = onFlowableSubscribe2;
    }

    public static void setOnMaybeSubscribe(BiFunction<Maybe, MaybeObserver, MaybeObserver> onMaybeSubscribe2) {
        if (lockdown) {
            throw new IllegalStateException("Plugins can't be changed anymore");
        }
        onMaybeSubscribe = onMaybeSubscribe2;
    }

    public static void setOnObservableAssembly(Function<Observable, Observable> onObservableAssembly2) {
        if (lockdown) {
            throw new IllegalStateException("Plugins can't be changed anymore");
        }
        onObservableAssembly = onObservableAssembly2;
    }

    public static void setOnConnectableObservableAssembly(Function<ConnectableObservable, ConnectableObservable> onConnectableObservableAssembly2) {
        if (lockdown) {
            throw new IllegalStateException("Plugins can't be changed anymore");
        }
        onConnectableObservableAssembly = onConnectableObservableAssembly2;
    }

    public static void setOnObservableSubscribe(BiFunction<Observable, Observer, Observer> onObservableSubscribe2) {
        if (lockdown) {
            throw new IllegalStateException("Plugins can't be changed anymore");
        }
        onObservableSubscribe = onObservableSubscribe2;
    }

    public static void setOnSingleAssembly(Function<Single, Single> onSingleAssembly2) {
        if (lockdown) {
            throw new IllegalStateException("Plugins can't be changed anymore");
        }
        onSingleAssembly = onSingleAssembly2;
    }

    public static void setOnSingleSubscribe(BiFunction<Single, SingleObserver, SingleObserver> onSingleSubscribe2) {
        if (lockdown) {
            throw new IllegalStateException("Plugins can't be changed anymore");
        }
        onSingleSubscribe = onSingleSubscribe2;
    }

    public static <T> Subscriber<? super T> onSubscribe(Flowable flowable, Subscriber subscriber) {
        BiFunction<Flowable, Subscriber, Subscriber> f = onFlowableSubscribe;
        if (f != null) {
            return (Subscriber) apply(f, flowable, subscriber);
        }
        return subscriber;
    }

    public static <T> Observer<? super T> onSubscribe(Observable observable, Observer observer) {
        BiFunction<Observable, Observer, Observer> f = onObservableSubscribe;
        if (f != null) {
            return (Observer) apply(f, observable, observer);
        }
        return observer;
    }

    public static <T> SingleObserver<? super T> onSubscribe(Single single, SingleObserver singleObserver) {
        BiFunction<Single, SingleObserver, SingleObserver> f = onSingleSubscribe;
        if (f != null) {
            return (SingleObserver) apply(f, single, singleObserver);
        }
        return singleObserver;
    }

    public static CompletableObserver onSubscribe(Completable source, CompletableObserver observer) {
        BiFunction<Completable, CompletableObserver, CompletableObserver> f = onCompletableSubscribe;
        if (f != null) {
            return (CompletableObserver) apply(f, source, observer);
        }
        return observer;
    }

    public static <T> MaybeObserver<? super T> onSubscribe(Maybe maybe, MaybeObserver maybeObserver) {
        BiFunction<Maybe, MaybeObserver, MaybeObserver> f = onMaybeSubscribe;
        if (f != null) {
            return (MaybeObserver) apply(f, maybe, maybeObserver);
        }
        return maybeObserver;
    }

    public static <T> Maybe<T> onAssembly(Maybe maybe) {
        Function<Maybe, Maybe> f = onMaybeAssembly;
        if (f != null) {
            return (Maybe) apply(f, maybe);
        }
        return maybe;
    }

    public static <T> Flowable<T> onAssembly(Flowable flowable) {
        Function<Flowable, Flowable> f = onFlowableAssembly;
        if (f != null) {
            return (Flowable) apply(f, flowable);
        }
        return flowable;
    }

    public static <T> ConnectableFlowable<T> onAssembly(ConnectableFlowable connectableFlowable) {
        Function<ConnectableFlowable, ConnectableFlowable> f = onConnectableFlowableAssembly;
        if (f != null) {
            return (ConnectableFlowable) apply(f, connectableFlowable);
        }
        return connectableFlowable;
    }

    public static <T> Observable<T> onAssembly(Observable observable) {
        Function<Observable, Observable> f = onObservableAssembly;
        if (f != null) {
            return (Observable) apply(f, observable);
        }
        return observable;
    }

    public static <T> ConnectableObservable<T> onAssembly(ConnectableObservable connectableObservable) {
        Function<ConnectableObservable, ConnectableObservable> f = onConnectableObservableAssembly;
        if (f != null) {
            return (ConnectableObservable) apply(f, connectableObservable);
        }
        return connectableObservable;
    }

    public static <T> Single<T> onAssembly(Single single) {
        Function<Single, Single> f = onSingleAssembly;
        if (f != null) {
            return (Single) apply(f, single);
        }
        return single;
    }

    public static Completable onAssembly(Completable source) {
        Function<Completable, Completable> f = onCompletableAssembly;
        if (f != null) {
            return (Completable) apply(f, source);
        }
        return source;
    }

    static <T, R> R apply(Function<T, R> f, T t) {
        try {
            return f.apply(t);
        } catch (Throwable ex) {
            throw ExceptionHelper.wrapOrThrow(ex);
        }
    }

    static <T, U, R> R apply(BiFunction<T, U, R> f, T t, U u) {
        try {
            return f.apply(t, u);
        } catch (Throwable ex) {
            throw ExceptionHelper.wrapOrThrow(ex);
        }
    }

    static Scheduler callRequireNonNull(Callable<Scheduler> s) {
        try {
            return (Scheduler) ObjectHelper.requireNonNull(s.call(), "Scheduler Callable result can't be null");
        } catch (Throwable ex) {
            throw ExceptionHelper.wrapOrThrow(ex);
        }
    }

    static Scheduler applyRequireNonNull(Function<Callable<Scheduler>, Scheduler> f, Callable<Scheduler> s) {
        return (Scheduler) ObjectHelper.requireNonNull(apply(f, s), "Scheduler Callable result can't be null");
    }

    private RxJavaPlugins() {
        throw new IllegalStateException("No instances!");
    }
}
