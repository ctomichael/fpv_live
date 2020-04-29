package dji.thirdparty.io.reactivex;

import dji.thirdparty.io.reactivex.annotations.BackpressureKind;
import dji.thirdparty.io.reactivex.annotations.BackpressureSupport;
import dji.thirdparty.io.reactivex.annotations.SchedulerSupport;
import dji.thirdparty.io.reactivex.disposables.Disposable;
import dji.thirdparty.io.reactivex.exceptions.Exceptions;
import dji.thirdparty.io.reactivex.functions.Action;
import dji.thirdparty.io.reactivex.functions.BiPredicate;
import dji.thirdparty.io.reactivex.functions.BooleanSupplier;
import dji.thirdparty.io.reactivex.functions.Consumer;
import dji.thirdparty.io.reactivex.functions.Function;
import dji.thirdparty.io.reactivex.functions.Predicate;
import dji.thirdparty.io.reactivex.internal.functions.Functions;
import dji.thirdparty.io.reactivex.internal.functions.ObjectHelper;
import dji.thirdparty.io.reactivex.internal.fuseable.FuseToFlowable;
import dji.thirdparty.io.reactivex.internal.fuseable.FuseToMaybe;
import dji.thirdparty.io.reactivex.internal.fuseable.FuseToObservable;
import dji.thirdparty.io.reactivex.internal.observers.BlockingMultiObserver;
import dji.thirdparty.io.reactivex.internal.observers.CallbackCompletableObserver;
import dji.thirdparty.io.reactivex.internal.observers.EmptyCompletableObserver;
import dji.thirdparty.io.reactivex.internal.operators.completable.CompletableAmb;
import dji.thirdparty.io.reactivex.internal.operators.completable.CompletableConcat;
import dji.thirdparty.io.reactivex.internal.operators.completable.CompletableConcatArray;
import dji.thirdparty.io.reactivex.internal.operators.completable.CompletableConcatIterable;
import dji.thirdparty.io.reactivex.internal.operators.completable.CompletableCreate;
import dji.thirdparty.io.reactivex.internal.operators.completable.CompletableDefer;
import dji.thirdparty.io.reactivex.internal.operators.completable.CompletableDelay;
import dji.thirdparty.io.reactivex.internal.operators.completable.CompletableDisposeOn;
import dji.thirdparty.io.reactivex.internal.operators.completable.CompletableDoOnEvent;
import dji.thirdparty.io.reactivex.internal.operators.completable.CompletableEmpty;
import dji.thirdparty.io.reactivex.internal.operators.completable.CompletableError;
import dji.thirdparty.io.reactivex.internal.operators.completable.CompletableErrorSupplier;
import dji.thirdparty.io.reactivex.internal.operators.completable.CompletableFromAction;
import dji.thirdparty.io.reactivex.internal.operators.completable.CompletableFromCallable;
import dji.thirdparty.io.reactivex.internal.operators.completable.CompletableFromObservable;
import dji.thirdparty.io.reactivex.internal.operators.completable.CompletableFromPublisher;
import dji.thirdparty.io.reactivex.internal.operators.completable.CompletableFromRunnable;
import dji.thirdparty.io.reactivex.internal.operators.completable.CompletableFromSingle;
import dji.thirdparty.io.reactivex.internal.operators.completable.CompletableFromUnsafeSource;
import dji.thirdparty.io.reactivex.internal.operators.completable.CompletableLift;
import dji.thirdparty.io.reactivex.internal.operators.completable.CompletableMerge;
import dji.thirdparty.io.reactivex.internal.operators.completable.CompletableMergeArray;
import dji.thirdparty.io.reactivex.internal.operators.completable.CompletableMergeDelayErrorArray;
import dji.thirdparty.io.reactivex.internal.operators.completable.CompletableMergeDelayErrorIterable;
import dji.thirdparty.io.reactivex.internal.operators.completable.CompletableMergeIterable;
import dji.thirdparty.io.reactivex.internal.operators.completable.CompletableNever;
import dji.thirdparty.io.reactivex.internal.operators.completable.CompletableObserveOn;
import dji.thirdparty.io.reactivex.internal.operators.completable.CompletableOnErrorComplete;
import dji.thirdparty.io.reactivex.internal.operators.completable.CompletablePeek;
import dji.thirdparty.io.reactivex.internal.operators.completable.CompletableResumeNext;
import dji.thirdparty.io.reactivex.internal.operators.completable.CompletableSubscribeOn;
import dji.thirdparty.io.reactivex.internal.operators.completable.CompletableTimeout;
import dji.thirdparty.io.reactivex.internal.operators.completable.CompletableTimer;
import dji.thirdparty.io.reactivex.internal.operators.completable.CompletableToFlowable;
import dji.thirdparty.io.reactivex.internal.operators.completable.CompletableToObservable;
import dji.thirdparty.io.reactivex.internal.operators.completable.CompletableToSingle;
import dji.thirdparty.io.reactivex.internal.operators.completable.CompletableUsing;
import dji.thirdparty.io.reactivex.internal.operators.flowable.FlowableDelaySubscriptionOther;
import dji.thirdparty.io.reactivex.internal.operators.maybe.MaybeDelayWithCompletable;
import dji.thirdparty.io.reactivex.internal.operators.maybe.MaybeFromCompletable;
import dji.thirdparty.io.reactivex.internal.operators.observable.ObservableDelaySubscriptionOther;
import dji.thirdparty.io.reactivex.internal.operators.single.SingleDelayWithCompletable;
import dji.thirdparty.io.reactivex.internal.util.ExceptionHelper;
import dji.thirdparty.io.reactivex.observers.TestObserver;
import dji.thirdparty.io.reactivex.plugins.RxJavaPlugins;
import dji.thirdparty.io.reactivex.schedulers.Schedulers;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import org.reactivestreams.Publisher;

public abstract class Completable implements CompletableSource {
    /* access modifiers changed from: protected */
    public abstract void subscribeActual(CompletableObserver completableObserver);

    @SchedulerSupport("none")
    public static Completable ambArray(CompletableSource... sources) {
        ObjectHelper.requireNonNull(sources, "sources is null");
        if (sources.length == 0) {
            return complete();
        }
        if (sources.length == 1) {
            return wrap(sources[0]);
        }
        return RxJavaPlugins.onAssembly(new CompletableAmb(sources, null));
    }

    @SchedulerSupport("none")
    public static Completable amb(Iterable<? extends CompletableSource> sources) {
        ObjectHelper.requireNonNull(sources, "sources is null");
        return RxJavaPlugins.onAssembly(new CompletableAmb(null, sources));
    }

    @SchedulerSupport("none")
    public static Completable complete() {
        return RxJavaPlugins.onAssembly(CompletableEmpty.INSTANCE);
    }

    @SchedulerSupport("none")
    public static Completable concatArray(CompletableSource... sources) {
        ObjectHelper.requireNonNull(sources, "sources is null");
        if (sources.length == 0) {
            return complete();
        }
        if (sources.length == 1) {
            return wrap(sources[0]);
        }
        return RxJavaPlugins.onAssembly(new CompletableConcatArray(sources));
    }

    @SchedulerSupport("none")
    public static Completable concat(Iterable<? extends CompletableSource> sources) {
        ObjectHelper.requireNonNull(sources, "sources is null");
        return RxJavaPlugins.onAssembly(new CompletableConcatIterable(sources));
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public static Completable concat(Publisher<? extends CompletableSource> sources) {
        return concat(sources, 2);
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public static Completable concat(Publisher<? extends CompletableSource> sources, int prefetch) {
        ObjectHelper.requireNonNull(sources, "sources is null");
        ObjectHelper.verifyPositive(prefetch, "prefetch");
        return RxJavaPlugins.onAssembly(new CompletableConcat(sources, prefetch));
    }

    @SchedulerSupport("none")
    public static Completable create(CompletableOnSubscribe source) {
        ObjectHelper.requireNonNull(source, "source is null");
        return RxJavaPlugins.onAssembly(new CompletableCreate(source));
    }

    @SchedulerSupport("none")
    public static Completable unsafeCreate(CompletableSource source) {
        ObjectHelper.requireNonNull(source, "source is null");
        if (!(source instanceof Completable)) {
            return RxJavaPlugins.onAssembly(new CompletableFromUnsafeSource(source));
        }
        throw new IllegalArgumentException("Use of unsafeCreate(Completable)!");
    }

    @SchedulerSupport("none")
    public static Completable defer(Callable<? extends CompletableSource> completableSupplier) {
        ObjectHelper.requireNonNull(completableSupplier, "completableSupplier");
        return RxJavaPlugins.onAssembly(new CompletableDefer(completableSupplier));
    }

    @SchedulerSupport("none")
    public static Completable error(Callable<? extends Throwable> errorSupplier) {
        ObjectHelper.requireNonNull(errorSupplier, "errorSupplier is null");
        return RxJavaPlugins.onAssembly(new CompletableErrorSupplier(errorSupplier));
    }

    @SchedulerSupport("none")
    public static Completable error(Throwable error) {
        ObjectHelper.requireNonNull(error, "error is null");
        return RxJavaPlugins.onAssembly(new CompletableError(error));
    }

    @SchedulerSupport("none")
    public static Completable fromAction(Action run) {
        ObjectHelper.requireNonNull(run, "run is null");
        return RxJavaPlugins.onAssembly(new CompletableFromAction(run));
    }

    @SchedulerSupport("none")
    public static Completable fromCallable(Callable<?> callable) {
        ObjectHelper.requireNonNull(callable, "callable is null");
        return RxJavaPlugins.onAssembly(new CompletableFromCallable(callable));
    }

    @SchedulerSupport("none")
    public static Completable fromFuture(Future<?> future) {
        ObjectHelper.requireNonNull(future, "future is null");
        return fromAction(Functions.futureAction(future));
    }

    @SchedulerSupport("none")
    public static Completable fromRunnable(Runnable run) {
        ObjectHelper.requireNonNull(run, "run is null");
        return RxJavaPlugins.onAssembly(new CompletableFromRunnable(run));
    }

    @SchedulerSupport("none")
    public static <T> Completable fromObservable(ObservableSource<T> observable) {
        ObjectHelper.requireNonNull(observable, "observable is null");
        return RxJavaPlugins.onAssembly(new CompletableFromObservable(observable));
    }

    @BackpressureSupport(BackpressureKind.UNBOUNDED_IN)
    @SchedulerSupport("none")
    public static <T> Completable fromPublisher(Publisher<T> publisher) {
        ObjectHelper.requireNonNull(publisher, "publisher is null");
        return RxJavaPlugins.onAssembly(new CompletableFromPublisher(publisher));
    }

    @SchedulerSupport("none")
    public static <T> Completable fromSingle(SingleSource<T> single) {
        ObjectHelper.requireNonNull(single, "single is null");
        return RxJavaPlugins.onAssembly(new CompletableFromSingle(single));
    }

    @SchedulerSupport("none")
    public static Completable mergeArray(CompletableSource... sources) {
        ObjectHelper.requireNonNull(sources, "sources is null");
        if (sources.length == 0) {
            return complete();
        }
        if (sources.length == 1) {
            return wrap(sources[0]);
        }
        return RxJavaPlugins.onAssembly(new CompletableMergeArray(sources));
    }

    @SchedulerSupport("none")
    public static Completable merge(Iterable<? extends CompletableSource> sources) {
        ObjectHelper.requireNonNull(sources, "sources is null");
        return RxJavaPlugins.onAssembly(new CompletableMergeIterable(sources));
    }

    @BackpressureSupport(BackpressureKind.UNBOUNDED_IN)
    @SchedulerSupport("none")
    public static Completable merge(Publisher<? extends CompletableSource> sources) {
        return merge0(sources, Integer.MAX_VALUE, false);
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public static Completable merge(Publisher<? extends CompletableSource> sources, int maxConcurrency) {
        return merge0(sources, maxConcurrency, false);
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    private static Completable merge0(Publisher<? extends CompletableSource> sources, int maxConcurrency, boolean delayErrors) {
        ObjectHelper.requireNonNull(sources, "sources is null");
        ObjectHelper.verifyPositive(maxConcurrency, "maxConcurrency");
        return RxJavaPlugins.onAssembly(new CompletableMerge(sources, maxConcurrency, delayErrors));
    }

    @SchedulerSupport("none")
    public static Completable mergeArrayDelayError(CompletableSource... sources) {
        ObjectHelper.requireNonNull(sources, "sources is null");
        return RxJavaPlugins.onAssembly(new CompletableMergeDelayErrorArray(sources));
    }

    @SchedulerSupport("none")
    public static Completable mergeDelayError(Iterable<? extends CompletableSource> sources) {
        ObjectHelper.requireNonNull(sources, "sources is null");
        return RxJavaPlugins.onAssembly(new CompletableMergeDelayErrorIterable(sources));
    }

    @BackpressureSupport(BackpressureKind.UNBOUNDED_IN)
    @SchedulerSupport("none")
    public static Completable mergeDelayError(Publisher<? extends CompletableSource> sources) {
        return merge0(sources, Integer.MAX_VALUE, true);
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public static Completable mergeDelayError(Publisher<? extends CompletableSource> sources, int maxConcurrency) {
        return merge0(sources, maxConcurrency, true);
    }

    @SchedulerSupport("none")
    public static Completable never() {
        return RxJavaPlugins.onAssembly(CompletableNever.INSTANCE);
    }

    @SchedulerSupport("io.reactivex:computation")
    public static Completable timer(long delay, TimeUnit unit) {
        return timer(delay, unit, Schedulers.computation());
    }

    @SchedulerSupport("custom")
    public static Completable timer(long delay, TimeUnit unit, Scheduler scheduler) {
        ObjectHelper.requireNonNull(unit, "unit is null");
        ObjectHelper.requireNonNull(scheduler, "scheduler is null");
        return RxJavaPlugins.onAssembly(new CompletableTimer(delay, unit, scheduler));
    }

    private static NullPointerException toNpe(Throwable ex) {
        NullPointerException npe = new NullPointerException("Actually not, but can't pass out an exception otherwise...");
        npe.initCause(ex);
        return npe;
    }

    @SchedulerSupport("none")
    public static <R> Completable using(Callable<R> resourceSupplier, Function<? super R, ? extends CompletableSource> completableFunction, Consumer<? super R> disposer) {
        return using(resourceSupplier, completableFunction, disposer, true);
    }

    @SchedulerSupport("none")
    public static <R> Completable using(Callable<R> resourceSupplier, Function<? super R, ? extends CompletableSource> completableFunction, Consumer<? super R> disposer, boolean eager) {
        ObjectHelper.requireNonNull(resourceSupplier, "resourceSupplier is null");
        ObjectHelper.requireNonNull(completableFunction, "completableFunction is null");
        ObjectHelper.requireNonNull(disposer, "disposer is null");
        return RxJavaPlugins.onAssembly(new CompletableUsing(resourceSupplier, completableFunction, disposer, eager));
    }

    @SchedulerSupport("none")
    public static Completable wrap(CompletableSource source) {
        ObjectHelper.requireNonNull(source, "source is null");
        if (source instanceof Completable) {
            return RxJavaPlugins.onAssembly((Completable) source);
        }
        return RxJavaPlugins.onAssembly(new CompletableFromUnsafeSource(source));
    }

    @SchedulerSupport("none")
    public final Completable ambWith(CompletableSource other) {
        ObjectHelper.requireNonNull(other, "other is null");
        return ambArray(this, other);
    }

    @SchedulerSupport("none")
    public final <T> Observable<T> andThen(ObservableSource<T> next) {
        ObjectHelper.requireNonNull(next, "next is null");
        return RxJavaPlugins.onAssembly(new ObservableDelaySubscriptionOther(next, toObservable()));
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public final <T> Flowable<T> andThen(Publisher<T> next) {
        ObjectHelper.requireNonNull(next, "next is null");
        return RxJavaPlugins.onAssembly(new FlowableDelaySubscriptionOther(next, toFlowable()));
    }

    @SchedulerSupport("none")
    public final <T> Single<T> andThen(SingleSource<T> next) {
        ObjectHelper.requireNonNull(next, "next is null");
        return RxJavaPlugins.onAssembly(new SingleDelayWithCompletable(next, this));
    }

    @SchedulerSupport("none")
    public final <T> Maybe<T> andThen(MaybeSource<T> next) {
        ObjectHelper.requireNonNull(next, "next is null");
        return RxJavaPlugins.onAssembly(new MaybeDelayWithCompletable(next, this));
    }

    @SchedulerSupport("none")
    public final Completable andThen(CompletableSource next) {
        return concatWith(next);
    }

    @SchedulerSupport("none")
    public final void blockingAwait() {
        BlockingMultiObserver<Void> observer = new BlockingMultiObserver<>();
        subscribe(observer);
        observer.blockingGet();
    }

    @SchedulerSupport("none")
    public final boolean blockingAwait(long timeout, TimeUnit unit) {
        BlockingMultiObserver<Void> observer = new BlockingMultiObserver<>();
        subscribe(observer);
        return observer.blockingAwait(timeout, unit);
    }

    @SchedulerSupport("none")
    public final Throwable blockingGet() {
        BlockingMultiObserver<Void> observer = new BlockingMultiObserver<>();
        subscribe(observer);
        return observer.blockingGetError();
    }

    @SchedulerSupport("none")
    public final Throwable blockingGet(long timeout, TimeUnit unit) {
        ObjectHelper.requireNonNull(unit, "unit is null");
        BlockingMultiObserver<Void> observer = new BlockingMultiObserver<>();
        subscribe(observer);
        return observer.blockingGetError(timeout, unit);
    }

    @SchedulerSupport("none")
    public final Completable compose(CompletableTransformer transformer) {
        return wrap(transformer.apply(this));
    }

    @SchedulerSupport("none")
    public final Completable concatWith(CompletableSource other) {
        ObjectHelper.requireNonNull(other, "other is null");
        return concatArray(this, other);
    }

    @SchedulerSupport("io.reactivex:computation")
    public final Completable delay(long delay, TimeUnit unit) {
        return delay(delay, unit, Schedulers.computation(), false);
    }

    @SchedulerSupport("custom")
    public final Completable delay(long delay, TimeUnit unit, Scheduler scheduler) {
        return delay(delay, unit, scheduler, false);
    }

    @SchedulerSupport("custom")
    public final Completable delay(long delay, TimeUnit unit, Scheduler scheduler, boolean delayError) {
        ObjectHelper.requireNonNull(unit, "unit is null");
        ObjectHelper.requireNonNull(scheduler, "scheduler is null");
        return RxJavaPlugins.onAssembly(new CompletableDelay(this, delay, unit, scheduler, delayError));
    }

    @SchedulerSupport("none")
    public final Completable doOnComplete(Action onComplete) {
        return doOnLifecycle(Functions.emptyConsumer(), Functions.emptyConsumer(), onComplete, Functions.EMPTY_ACTION, Functions.EMPTY_ACTION, Functions.EMPTY_ACTION);
    }

    @SchedulerSupport("none")
    public final Completable doOnDispose(Action onDispose) {
        return doOnLifecycle(Functions.emptyConsumer(), Functions.emptyConsumer(), Functions.EMPTY_ACTION, Functions.EMPTY_ACTION, Functions.EMPTY_ACTION, onDispose);
    }

    @SchedulerSupport("none")
    public final Completable doOnError(Consumer<? super Throwable> onError) {
        return doOnLifecycle(Functions.emptyConsumer(), onError, Functions.EMPTY_ACTION, Functions.EMPTY_ACTION, Functions.EMPTY_ACTION, Functions.EMPTY_ACTION);
    }

    @SchedulerSupport("none")
    public final Completable doOnEvent(Consumer<? super Throwable> onEvent) {
        ObjectHelper.requireNonNull(onEvent, "onEvent is null");
        return RxJavaPlugins.onAssembly(new CompletableDoOnEvent(this, onEvent));
    }

    @SchedulerSupport("none")
    private Completable doOnLifecycle(Consumer<? super Disposable> onSubscribe, Consumer<? super Throwable> onError, Action onComplete, Action onTerminate, Action onAfterTerminate, Action onDispose) {
        ObjectHelper.requireNonNull(onSubscribe, "onSubscribe is null");
        ObjectHelper.requireNonNull(onError, "onError is null");
        ObjectHelper.requireNonNull(onComplete, "onComplete is null");
        ObjectHelper.requireNonNull(onTerminate, "onTerminate is null");
        ObjectHelper.requireNonNull(onAfterTerminate, "onAfterTerminate is null");
        ObjectHelper.requireNonNull(onDispose, "onDispose is null");
        return RxJavaPlugins.onAssembly(new CompletablePeek(this, onSubscribe, onError, onComplete, onTerminate, onAfterTerminate, onDispose));
    }

    @SchedulerSupport("none")
    public final Completable doOnSubscribe(Consumer<? super Disposable> onSubscribe) {
        return doOnLifecycle(onSubscribe, Functions.emptyConsumer(), Functions.EMPTY_ACTION, Functions.EMPTY_ACTION, Functions.EMPTY_ACTION, Functions.EMPTY_ACTION);
    }

    @SchedulerSupport("none")
    public final Completable doOnTerminate(Action onTerminate) {
        return doOnLifecycle(Functions.emptyConsumer(), Functions.emptyConsumer(), Functions.EMPTY_ACTION, onTerminate, Functions.EMPTY_ACTION, Functions.EMPTY_ACTION);
    }

    @SchedulerSupport("none")
    public final Completable doAfterTerminate(Action onAfterTerminate) {
        return doOnLifecycle(Functions.emptyConsumer(), Functions.emptyConsumer(), Functions.EMPTY_ACTION, Functions.EMPTY_ACTION, onAfterTerminate, Functions.EMPTY_ACTION);
    }

    @SchedulerSupport("none")
    public final Completable lift(CompletableOperator onLift) {
        ObjectHelper.requireNonNull(onLift, "onLift is null");
        return RxJavaPlugins.onAssembly(new CompletableLift(this, onLift));
    }

    @SchedulerSupport("none")
    public final Completable mergeWith(CompletableSource other) {
        ObjectHelper.requireNonNull(other, "other is null");
        return mergeArray(this, other);
    }

    @SchedulerSupport("custom")
    public final Completable observeOn(Scheduler scheduler) {
        ObjectHelper.requireNonNull(scheduler, "scheduler is null");
        return RxJavaPlugins.onAssembly(new CompletableObserveOn(this, scheduler));
    }

    @SchedulerSupport("none")
    public final Completable onErrorComplete() {
        return onErrorComplete(Functions.alwaysTrue());
    }

    @SchedulerSupport("none")
    public final Completable onErrorComplete(Predicate<? super Throwable> predicate) {
        ObjectHelper.requireNonNull(predicate, "predicate is null");
        return RxJavaPlugins.onAssembly(new CompletableOnErrorComplete(this, predicate));
    }

    @SchedulerSupport("none")
    public final Completable onErrorResumeNext(Function<? super Throwable, ? extends CompletableSource> errorMapper) {
        ObjectHelper.requireNonNull(errorMapper, "errorMapper is null");
        return RxJavaPlugins.onAssembly(new CompletableResumeNext(this, errorMapper));
    }

    @SchedulerSupport("none")
    public final Completable repeat() {
        return fromPublisher(toFlowable().repeat());
    }

    @SchedulerSupport("none")
    public final Completable repeat(long times) {
        return fromPublisher(toFlowable().repeat(times));
    }

    @SchedulerSupport("none")
    public final Completable repeatUntil(BooleanSupplier stop) {
        return fromPublisher(toFlowable().repeatUntil(stop));
    }

    @SchedulerSupport("none")
    public final Completable repeatWhen(Function<? super Flowable<Object>, ? extends Publisher<Object>> handler) {
        return fromPublisher(toFlowable().repeatWhen(handler));
    }

    @SchedulerSupport("none")
    public final Completable retry() {
        return fromPublisher(toFlowable().retry());
    }

    @SchedulerSupport("none")
    public final Completable retry(BiPredicate<? super Integer, ? super Throwable> predicate) {
        return fromPublisher(toFlowable().retry(predicate));
    }

    @SchedulerSupport("none")
    public final Completable retry(long times) {
        return fromPublisher(toFlowable().retry(times));
    }

    @SchedulerSupport("none")
    public final Completable retry(Predicate<? super Throwable> predicate) {
        return fromPublisher(toFlowable().retry(predicate));
    }

    @SchedulerSupport("none")
    public final Completable retryWhen(Function<? super Flowable<Throwable>, ? extends Publisher<Object>> handler) {
        return fromPublisher(toFlowable().retryWhen(handler));
    }

    @SchedulerSupport("none")
    public final Completable startWith(CompletableSource other) {
        ObjectHelper.requireNonNull(other, "other is null");
        return concatArray(other, this);
    }

    @SchedulerSupport("none")
    public final <T> Observable<T> startWith(Observable<T> other) {
        ObjectHelper.requireNonNull(other, "other is null");
        return other.concatWith(toObservable());
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public final <T> Flowable<T> startWith(Publisher<T> other) {
        ObjectHelper.requireNonNull(other, "other is null");
        return toFlowable().startWith((Publisher) other);
    }

    @SchedulerSupport("none")
    public final Disposable subscribe() {
        EmptyCompletableObserver s = new EmptyCompletableObserver();
        subscribe(s);
        return s;
    }

    @SchedulerSupport("none")
    public final void subscribe(CompletableObserver s) {
        ObjectHelper.requireNonNull(s, "s is null");
        try {
            subscribeActual(RxJavaPlugins.onSubscribe(this, s));
        } catch (NullPointerException ex) {
            throw ex;
        } catch (Throwable ex2) {
            Exceptions.throwIfFatal(ex2);
            RxJavaPlugins.onError(ex2);
            throw toNpe(ex2);
        }
    }

    @SchedulerSupport("none")
    public final <E extends CompletableObserver> E subscribeWith(E observer) {
        subscribe((CompletableObserver) observer);
        return observer;
    }

    @SchedulerSupport("none")
    public final Disposable subscribe(Action onComplete, Consumer<? super Throwable> onError) {
        ObjectHelper.requireNonNull(onError, "onError is null");
        ObjectHelper.requireNonNull(onComplete, "onComplete is null");
        CallbackCompletableObserver s = new CallbackCompletableObserver(onError, onComplete);
        subscribe(s);
        return s;
    }

    @SchedulerSupport("none")
    public final Disposable subscribe(Action onComplete) {
        ObjectHelper.requireNonNull(onComplete, "onComplete is null");
        CallbackCompletableObserver s = new CallbackCompletableObserver(onComplete);
        subscribe(s);
        return s;
    }

    @SchedulerSupport("custom")
    public final Completable subscribeOn(Scheduler scheduler) {
        ObjectHelper.requireNonNull(scheduler, "scheduler is null");
        return RxJavaPlugins.onAssembly(new CompletableSubscribeOn(this, scheduler));
    }

    @SchedulerSupport("io.reactivex:computation")
    public final Completable timeout(long timeout, TimeUnit unit) {
        return timeout0(timeout, unit, Schedulers.computation(), null);
    }

    @SchedulerSupport("io.reactivex:computation")
    public final Completable timeout(long timeout, TimeUnit unit, CompletableSource other) {
        ObjectHelper.requireNonNull(other, "other is null");
        return timeout0(timeout, unit, Schedulers.computation(), other);
    }

    @SchedulerSupport("custom")
    public final Completable timeout(long timeout, TimeUnit unit, Scheduler scheduler) {
        return timeout0(timeout, unit, scheduler, null);
    }

    @SchedulerSupport("custom")
    public final Completable timeout(long timeout, TimeUnit unit, Scheduler scheduler, CompletableSource other) {
        ObjectHelper.requireNonNull(other, "other is null");
        return timeout0(timeout, unit, scheduler, other);
    }

    @SchedulerSupport("custom")
    private Completable timeout0(long timeout, TimeUnit unit, Scheduler scheduler, CompletableSource other) {
        ObjectHelper.requireNonNull(unit, "unit is null");
        ObjectHelper.requireNonNull(scheduler, "scheduler is null");
        return RxJavaPlugins.onAssembly(new CompletableTimeout(this, timeout, unit, scheduler, other));
    }

    @SchedulerSupport("none")
    public final <U> U to(Function<? super Completable, U> converter) {
        try {
            return converter.apply(this);
        } catch (Throwable ex) {
            Exceptions.throwIfFatal(ex);
            throw ExceptionHelper.wrapOrThrow(ex);
        }
    }

    /* Debug info: failed to restart local var, previous not found, register: 1 */
    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public final <T> Flowable<T> toFlowable() {
        if (this instanceof FuseToFlowable) {
            return ((FuseToFlowable) this).fuseToFlowable();
        }
        return RxJavaPlugins.onAssembly(new CompletableToFlowable(this));
    }

    /* Debug info: failed to restart local var, previous not found, register: 1 */
    @SchedulerSupport("none")
    public final <T> Maybe<T> toMaybe() {
        if (this instanceof FuseToMaybe) {
            return ((FuseToMaybe) this).fuseToMaybe();
        }
        return RxJavaPlugins.onAssembly(new MaybeFromCompletable(this));
    }

    /* Debug info: failed to restart local var, previous not found, register: 1 */
    @SchedulerSupport("none")
    public final <T> Observable<T> toObservable() {
        if (this instanceof FuseToObservable) {
            return ((FuseToObservable) this).fuseToObservable();
        }
        return RxJavaPlugins.onAssembly(new CompletableToObservable(this));
    }

    @SchedulerSupport("none")
    public final <T> Single<T> toSingle(Callable<? extends T> completionValueSupplier) {
        ObjectHelper.requireNonNull(completionValueSupplier, "completionValueSupplier is null");
        return RxJavaPlugins.onAssembly(new CompletableToSingle(this, completionValueSupplier, null));
    }

    @SchedulerSupport("none")
    public final <T> Single<T> toSingleDefault(T completionValue) {
        ObjectHelper.requireNonNull(completionValue, "completionValue is null");
        return RxJavaPlugins.onAssembly(new CompletableToSingle(this, null, completionValue));
    }

    @SchedulerSupport("custom")
    public final Completable unsubscribeOn(Scheduler scheduler) {
        ObjectHelper.requireNonNull(scheduler, "scheduler is null");
        return RxJavaPlugins.onAssembly(new CompletableDisposeOn(this, scheduler));
    }

    @SchedulerSupport("none")
    public final TestObserver<Void> test() {
        TestObserver<Void> ts = new TestObserver<>();
        subscribe(ts);
        return ts;
    }

    @SchedulerSupport("none")
    public final TestObserver<Void> test(boolean cancelled) {
        TestObserver<Void> ts = new TestObserver<>();
        if (cancelled) {
            ts.cancel();
        }
        subscribe(ts);
        return ts;
    }
}
