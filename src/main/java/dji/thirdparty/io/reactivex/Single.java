package dji.thirdparty.io.reactivex;

import dji.thirdparty.io.reactivex.annotations.BackpressureKind;
import dji.thirdparty.io.reactivex.annotations.BackpressureSupport;
import dji.thirdparty.io.reactivex.annotations.SchedulerSupport;
import dji.thirdparty.io.reactivex.disposables.Disposable;
import dji.thirdparty.io.reactivex.exceptions.Exceptions;
import dji.thirdparty.io.reactivex.functions.Action;
import dji.thirdparty.io.reactivex.functions.BiConsumer;
import dji.thirdparty.io.reactivex.functions.BiFunction;
import dji.thirdparty.io.reactivex.functions.BiPredicate;
import dji.thirdparty.io.reactivex.functions.BooleanSupplier;
import dji.thirdparty.io.reactivex.functions.Consumer;
import dji.thirdparty.io.reactivex.functions.Function;
import dji.thirdparty.io.reactivex.functions.Function3;
import dji.thirdparty.io.reactivex.functions.Function4;
import dji.thirdparty.io.reactivex.functions.Function5;
import dji.thirdparty.io.reactivex.functions.Function6;
import dji.thirdparty.io.reactivex.functions.Function7;
import dji.thirdparty.io.reactivex.functions.Function8;
import dji.thirdparty.io.reactivex.functions.Function9;
import dji.thirdparty.io.reactivex.functions.Predicate;
import dji.thirdparty.io.reactivex.internal.functions.Functions;
import dji.thirdparty.io.reactivex.internal.functions.ObjectHelper;
import dji.thirdparty.io.reactivex.internal.fuseable.FuseToFlowable;
import dji.thirdparty.io.reactivex.internal.fuseable.FuseToMaybe;
import dji.thirdparty.io.reactivex.internal.fuseable.FuseToObservable;
import dji.thirdparty.io.reactivex.internal.observers.BiConsumerSingleObserver;
import dji.thirdparty.io.reactivex.internal.observers.BlockingMultiObserver;
import dji.thirdparty.io.reactivex.internal.observers.ConsumerSingleObserver;
import dji.thirdparty.io.reactivex.internal.observers.FutureSingleObserver;
import dji.thirdparty.io.reactivex.internal.operators.completable.CompletableFromSingle;
import dji.thirdparty.io.reactivex.internal.operators.completable.CompletableToFlowable;
import dji.thirdparty.io.reactivex.internal.operators.flowable.FlowableConcatMap;
import dji.thirdparty.io.reactivex.internal.operators.flowable.FlowableFlatMap;
import dji.thirdparty.io.reactivex.internal.operators.flowable.FlowableSingleSingle;
import dji.thirdparty.io.reactivex.internal.operators.maybe.MaybeFilterSingle;
import dji.thirdparty.io.reactivex.internal.operators.maybe.MaybeFromSingle;
import dji.thirdparty.io.reactivex.internal.operators.observable.ObservableConcatMap;
import dji.thirdparty.io.reactivex.internal.operators.observable.ObservableSingleSingle;
import dji.thirdparty.io.reactivex.internal.operators.single.SingleAmb;
import dji.thirdparty.io.reactivex.internal.operators.single.SingleCache;
import dji.thirdparty.io.reactivex.internal.operators.single.SingleContains;
import dji.thirdparty.io.reactivex.internal.operators.single.SingleCreate;
import dji.thirdparty.io.reactivex.internal.operators.single.SingleDefer;
import dji.thirdparty.io.reactivex.internal.operators.single.SingleDelay;
import dji.thirdparty.io.reactivex.internal.operators.single.SingleDelayWithCompletable;
import dji.thirdparty.io.reactivex.internal.operators.single.SingleDelayWithObservable;
import dji.thirdparty.io.reactivex.internal.operators.single.SingleDelayWithPublisher;
import dji.thirdparty.io.reactivex.internal.operators.single.SingleDelayWithSingle;
import dji.thirdparty.io.reactivex.internal.operators.single.SingleDoOnDispose;
import dji.thirdparty.io.reactivex.internal.operators.single.SingleDoOnError;
import dji.thirdparty.io.reactivex.internal.operators.single.SingleDoOnEvent;
import dji.thirdparty.io.reactivex.internal.operators.single.SingleDoOnSubscribe;
import dji.thirdparty.io.reactivex.internal.operators.single.SingleDoOnSuccess;
import dji.thirdparty.io.reactivex.internal.operators.single.SingleEquals;
import dji.thirdparty.io.reactivex.internal.operators.single.SingleError;
import dji.thirdparty.io.reactivex.internal.operators.single.SingleFlatMap;
import dji.thirdparty.io.reactivex.internal.operators.single.SingleFlatMapCompletable;
import dji.thirdparty.io.reactivex.internal.operators.single.SingleFlatMapIterableFlowable;
import dji.thirdparty.io.reactivex.internal.operators.single.SingleFlatMapIterableObservable;
import dji.thirdparty.io.reactivex.internal.operators.single.SingleFlatMapMaybe;
import dji.thirdparty.io.reactivex.internal.operators.single.SingleFromCallable;
import dji.thirdparty.io.reactivex.internal.operators.single.SingleFromPublisher;
import dji.thirdparty.io.reactivex.internal.operators.single.SingleFromUnsafeSource;
import dji.thirdparty.io.reactivex.internal.operators.single.SingleHide;
import dji.thirdparty.io.reactivex.internal.operators.single.SingleInternalHelper;
import dji.thirdparty.io.reactivex.internal.operators.single.SingleJust;
import dji.thirdparty.io.reactivex.internal.operators.single.SingleLift;
import dji.thirdparty.io.reactivex.internal.operators.single.SingleMap;
import dji.thirdparty.io.reactivex.internal.operators.single.SingleNever;
import dji.thirdparty.io.reactivex.internal.operators.single.SingleObserveOn;
import dji.thirdparty.io.reactivex.internal.operators.single.SingleOnErrorReturn;
import dji.thirdparty.io.reactivex.internal.operators.single.SingleResumeNext;
import dji.thirdparty.io.reactivex.internal.operators.single.SingleSubscribeOn;
import dji.thirdparty.io.reactivex.internal.operators.single.SingleTakeUntil;
import dji.thirdparty.io.reactivex.internal.operators.single.SingleTimeout;
import dji.thirdparty.io.reactivex.internal.operators.single.SingleTimer;
import dji.thirdparty.io.reactivex.internal.operators.single.SingleToFlowable;
import dji.thirdparty.io.reactivex.internal.operators.single.SingleToObservable;
import dji.thirdparty.io.reactivex.internal.operators.single.SingleUsing;
import dji.thirdparty.io.reactivex.internal.util.ErrorMode;
import dji.thirdparty.io.reactivex.internal.util.ExceptionHelper;
import dji.thirdparty.io.reactivex.observers.TestObserver;
import dji.thirdparty.io.reactivex.plugins.RxJavaPlugins;
import dji.thirdparty.io.reactivex.schedulers.Schedulers;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import org.reactivestreams.Publisher;

public abstract class Single<T> implements SingleSource<T> {
    /* access modifiers changed from: protected */
    public abstract void subscribeActual(SingleObserver<? super T> singleObserver);

    @SchedulerSupport("none")
    public static <T> Single<T> amb(Iterable<? extends SingleSource<? extends T>> sources) {
        ObjectHelper.requireNonNull(sources, "sources is null");
        return RxJavaPlugins.onAssembly(new SingleAmb(null, sources));
    }

    @SchedulerSupport("none")
    public static <T> Single<T> ambArray(SingleSource<? extends T>... sources) {
        if (sources.length == 0) {
            return error(SingleInternalHelper.emptyThrower());
        }
        if (sources.length == 1) {
            return wrap(sources[0]);
        }
        return RxJavaPlugins.onAssembly(new SingleAmb(sources, null));
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public static <T> Flowable<T> concat(Iterable<? extends SingleSource<? extends T>> sources) {
        return concat(Flowable.fromIterable(sources));
    }

    @SchedulerSupport("none")
    public static <T> Observable<T> concat(ObservableSource<? extends SingleSource<? extends T>> sources) {
        return RxJavaPlugins.onAssembly(new ObservableConcatMap(sources, SingleInternalHelper.toObservable(), 2, ErrorMode.IMMEDIATE));
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public static <T> Flowable<T> concat(Publisher<? extends SingleSource<? extends T>> sources) {
        return concat(sources, 2);
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public static <T> Flowable<T> concat(Publisher<? extends SingleSource<? extends T>> sources, int prefetch) {
        ObjectHelper.verifyPositive(prefetch, "prefetch");
        return RxJavaPlugins.onAssembly(new FlowableConcatMap(sources, SingleInternalHelper.toFlowable(), prefetch, ErrorMode.IMMEDIATE));
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public static <T> Flowable<T> concat(SingleSource<? extends T> source1, SingleSource<? extends T> source2) {
        ObjectHelper.requireNonNull(source1, "source1 is null");
        ObjectHelper.requireNonNull(source2, "source2 is null");
        return concat(Flowable.fromArray(source1, source2));
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public static <T> Flowable<T> concat(SingleSource<? extends T> source1, SingleSource<? extends T> source2, SingleSource<? extends T> source3) {
        ObjectHelper.requireNonNull(source1, "source1 is null");
        ObjectHelper.requireNonNull(source2, "source2 is null");
        ObjectHelper.requireNonNull(source3, "source3 is null");
        return concat(Flowable.fromArray(source1, source2, source3));
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public static <T> Flowable<T> concat(SingleSource<? extends T> source1, SingleSource<? extends T> source2, SingleSource<? extends T> source3, SingleSource<? extends T> source4) {
        ObjectHelper.requireNonNull(source1, "source1 is null");
        ObjectHelper.requireNonNull(source2, "source2 is null");
        ObjectHelper.requireNonNull(source3, "source3 is null");
        ObjectHelper.requireNonNull(source4, "source4 is null");
        return concat(Flowable.fromArray(source1, source2, source3, source4));
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public static <T> Flowable<T> concatArray(SingleSource<? extends T>... sources) {
        return RxJavaPlugins.onAssembly(new FlowableConcatMap(Flowable.fromArray(sources), SingleInternalHelper.toFlowable(), 2, ErrorMode.BOUNDARY));
    }

    @SchedulerSupport("none")
    public static <T> Single<T> create(SingleOnSubscribe<T> source) {
        ObjectHelper.requireNonNull(source, "source is null");
        return RxJavaPlugins.onAssembly(new SingleCreate(source));
    }

    @SchedulerSupport("none")
    public static <T> Single<T> defer(Callable<? extends SingleSource<? extends T>> singleSupplier) {
        ObjectHelper.requireNonNull(singleSupplier, "singleSupplier is null");
        return RxJavaPlugins.onAssembly(new SingleDefer(singleSupplier));
    }

    @SchedulerSupport("none")
    public static <T> Single<T> error(Callable<? extends Throwable> errorSupplier) {
        ObjectHelper.requireNonNull(errorSupplier, "errorSupplier is null");
        return RxJavaPlugins.onAssembly(new SingleError(errorSupplier));
    }

    @SchedulerSupport("none")
    public static <T> Single<T> error(Throwable exception) {
        ObjectHelper.requireNonNull(exception, "error is null");
        return error(Functions.justCallable(exception));
    }

    @SchedulerSupport("none")
    public static <T> Single<T> fromCallable(Callable<? extends T> callable) {
        ObjectHelper.requireNonNull(callable, "callable is null");
        return RxJavaPlugins.onAssembly(new SingleFromCallable(callable));
    }

    @SchedulerSupport("none")
    public static <T> Single<T> fromFuture(Future<? extends T> future) {
        return toSingle(Flowable.fromFuture(future));
    }

    @SchedulerSupport("none")
    public static <T> Single<T> fromFuture(Future<? extends T> future, long timeout, TimeUnit unit) {
        return toSingle(Flowable.fromFuture(future, timeout, unit));
    }

    @SchedulerSupport("custom")
    public static <T> Single<T> fromFuture(Future<? extends T> future, long timeout, TimeUnit unit, Scheduler scheduler) {
        return toSingle(Flowable.fromFuture(future, timeout, unit, scheduler));
    }

    @SchedulerSupport("custom")
    public static <T> Single<T> fromFuture(Future<? extends T> future, Scheduler scheduler) {
        return toSingle(Flowable.fromFuture(future, scheduler));
    }

    @BackpressureSupport(BackpressureKind.UNBOUNDED_IN)
    @SchedulerSupport("none")
    public static <T> Single<T> fromPublisher(Publisher<? extends T> publisher) {
        ObjectHelper.requireNonNull(publisher, "publisher is null");
        return RxJavaPlugins.onAssembly(new SingleFromPublisher(publisher));
    }

    @SchedulerSupport("none")
    public static <T> Single<T> fromObservable(ObservableSource<? extends T> observableSource) {
        ObjectHelper.requireNonNull(observableSource, "observableSource is null");
        return RxJavaPlugins.onAssembly(new ObservableSingleSingle(observableSource, null));
    }

    @SchedulerSupport("none")
    public static <T> Single<T> just(T item) {
        ObjectHelper.requireNonNull(item, "value is null");
        return RxJavaPlugins.onAssembly(new SingleJust(item));
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public static <T> Flowable<T> merge(Iterable<? extends SingleSource<? extends T>> sources) {
        return merge(Flowable.fromIterable(sources));
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public static <T> Flowable<T> merge(Publisher<? extends SingleSource<? extends T>> sources) {
        return RxJavaPlugins.onAssembly(new FlowableFlatMap(sources, SingleInternalHelper.toFlowable(), false, Integer.MAX_VALUE, Flowable.bufferSize()));
    }

    @SchedulerSupport("none")
    public static <T> Single<T> merge(SingleSource<? extends SingleSource<? extends T>> source) {
        ObjectHelper.requireNonNull(source, "source is null");
        return RxJavaPlugins.onAssembly(new SingleFlatMap(source, Functions.identity()));
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public static <T> Flowable<T> merge(SingleSource<? extends T> source1, SingleSource<? extends T> source2) {
        ObjectHelper.requireNonNull(source1, "source1 is null");
        ObjectHelper.requireNonNull(source2, "source2 is null");
        return merge(Flowable.fromArray(source1, source2));
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public static <T> Flowable<T> merge(SingleSource<? extends T> source1, SingleSource<? extends T> source2, SingleSource<? extends T> source3) {
        ObjectHelper.requireNonNull(source1, "source1 is null");
        ObjectHelper.requireNonNull(source2, "source2 is null");
        ObjectHelper.requireNonNull(source3, "source3 is null");
        return merge(Flowable.fromArray(source1, source2, source3));
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public static <T> Flowable<T> merge(SingleSource<? extends T> source1, SingleSource<? extends T> source2, SingleSource<? extends T> source3, SingleSource<? extends T> source4) {
        ObjectHelper.requireNonNull(source1, "source1 is null");
        ObjectHelper.requireNonNull(source2, "source2 is null");
        ObjectHelper.requireNonNull(source3, "source3 is null");
        ObjectHelper.requireNonNull(source4, "source4 is null");
        return merge(Flowable.fromArray(source1, source2, source3, source4));
    }

    @SchedulerSupport("none")
    public static <T> Single<T> never() {
        return RxJavaPlugins.onAssembly(SingleNever.INSTANCE);
    }

    @SchedulerSupport("io.reactivex:computation")
    public static Single<Long> timer(long delay, TimeUnit unit) {
        return timer(delay, unit, Schedulers.computation());
    }

    @SchedulerSupport("custom")
    public static Single<Long> timer(long delay, TimeUnit unit, Scheduler scheduler) {
        ObjectHelper.requireNonNull(unit, "unit is null");
        ObjectHelper.requireNonNull(scheduler, "scheduler is null");
        return RxJavaPlugins.onAssembly(new SingleTimer(delay, unit, scheduler));
    }

    @SchedulerSupport("none")
    public static <T> Single<Boolean> equals(SingleSource<? extends T> first, SingleSource<? extends T> second) {
        ObjectHelper.requireNonNull(first, "first is null");
        ObjectHelper.requireNonNull(second, "second is null");
        return RxJavaPlugins.onAssembly(new SingleEquals(first, second));
    }

    @SchedulerSupport("none")
    public static <T> Single<T> unsafeCreate(SingleSource<T> onSubscribe) {
        ObjectHelper.requireNonNull(onSubscribe, "onSubscribe is null");
        if (!(onSubscribe instanceof Single)) {
            return RxJavaPlugins.onAssembly(new SingleFromUnsafeSource(onSubscribe));
        }
        throw new IllegalArgumentException("unsafeCreate(Single) should be upgraded");
    }

    @SchedulerSupport("none")
    public static <T, U> Single<T> using(Callable<U> resourceSupplier, Function<? super U, ? extends SingleSource<? extends T>> singleFunction, Consumer<? super U> disposer) {
        return using(resourceSupplier, singleFunction, disposer, true);
    }

    @SchedulerSupport("none")
    public static <T, U> Single<T> using(Callable<U> resourceSupplier, Function<? super U, ? extends SingleSource<? extends T>> singleFunction, Consumer<? super U> disposer, boolean eager) {
        ObjectHelper.requireNonNull(resourceSupplier, "resourceSupplier is null");
        ObjectHelper.requireNonNull(singleFunction, "singleFunction is null");
        ObjectHelper.requireNonNull(disposer, "disposer is null");
        return RxJavaPlugins.onAssembly(new SingleUsing(resourceSupplier, singleFunction, disposer, eager));
    }

    @SchedulerSupport("none")
    public static <T> Single<T> wrap(SingleSource<T> source) {
        ObjectHelper.requireNonNull(source, "source is null");
        if (source instanceof Single) {
            return RxJavaPlugins.onAssembly((Single) source);
        }
        return RxJavaPlugins.onAssembly(new SingleFromUnsafeSource(source));
    }

    @SchedulerSupport("none")
    public static <T, R> Single<R> zip(Iterable<? extends SingleSource<? extends T>> sources, Function<? super Object[], ? extends R> zipper) {
        ObjectHelper.requireNonNull(sources, "sources is null");
        return toSingle(Flowable.zipIterable(SingleInternalHelper.iterableToFlowable(sources), zipper, false, 1));
    }

    @SchedulerSupport("none")
    public static <T1, T2, R> Single<R> zip(SingleSource<? extends T1> source1, SingleSource<? extends T2> source2, BiFunction<? super T1, ? super T2, ? extends R> zipper) {
        ObjectHelper.requireNonNull(source1, "source1 is null");
        ObjectHelper.requireNonNull(source2, "source2 is null");
        return zipArray(Functions.toFunction(zipper), source1, source2);
    }

    @SchedulerSupport("none")
    public static <T1, T2, T3, R> Single<R> zip(SingleSource<? extends T1> source1, SingleSource<? extends T2> source2, SingleSource<? extends T3> source3, Function3<? super T1, ? super T2, ? super T3, ? extends R> zipper) {
        ObjectHelper.requireNonNull(source1, "source1 is null");
        ObjectHelper.requireNonNull(source2, "source2 is null");
        ObjectHelper.requireNonNull(source3, "source3 is null");
        return zipArray(Functions.toFunction(zipper), source1, source2, source3);
    }

    @SchedulerSupport("none")
    public static <T1, T2, T3, T4, R> Single<R> zip(SingleSource<? extends T1> source1, SingleSource<? extends T2> source2, SingleSource<? extends T3> source3, SingleSource<? extends T4> source4, Function4<? super T1, ? super T2, ? super T3, ? super T4, ? extends R> zipper) {
        ObjectHelper.requireNonNull(source1, "source1 is null");
        ObjectHelper.requireNonNull(source2, "source2 is null");
        ObjectHelper.requireNonNull(source3, "source3 is null");
        ObjectHelper.requireNonNull(source4, "source4 is null");
        return zipArray(Functions.toFunction(zipper), source1, source2, source3, source4);
    }

    @SchedulerSupport("none")
    public static <T1, T2, T3, T4, T5, R> Single<R> zip(SingleSource<? extends T1> source1, SingleSource<? extends T2> source2, SingleSource<? extends T3> source3, SingleSource<? extends T4> source4, SingleSource<? extends T5> source5, Function5<? super T1, ? super T2, ? super T3, ? super T4, ? super T5, ? extends R> zipper) {
        ObjectHelper.requireNonNull(source1, "source1 is null");
        ObjectHelper.requireNonNull(source2, "source2 is null");
        ObjectHelper.requireNonNull(source3, "source3 is null");
        ObjectHelper.requireNonNull(source4, "source4 is null");
        ObjectHelper.requireNonNull(source5, "source5 is null");
        return zipArray(Functions.toFunction(zipper), source1, source2, source3, source4, source5);
    }

    @SchedulerSupport("none")
    public static <T1, T2, T3, T4, T5, T6, R> Single<R> zip(SingleSource<? extends T1> source1, SingleSource<? extends T2> source2, SingleSource<? extends T3> source3, SingleSource<? extends T4> source4, SingleSource<? extends T5> source5, SingleSource<? extends T6> source6, Function6<? super T1, ? super T2, ? super T3, ? super T4, ? super T5, ? super T6, ? extends R> zipper) {
        ObjectHelper.requireNonNull(source1, "source1 is null");
        ObjectHelper.requireNonNull(source2, "source2 is null");
        ObjectHelper.requireNonNull(source3, "source3 is null");
        ObjectHelper.requireNonNull(source4, "source4 is null");
        ObjectHelper.requireNonNull(source5, "source5 is null");
        ObjectHelper.requireNonNull(source6, "source6 is null");
        return zipArray(Functions.toFunction(zipper), source1, source2, source3, source4, source5, source6);
    }

    @SchedulerSupport("none")
    public static <T1, T2, T3, T4, T5, T6, T7, R> Single<R> zip(SingleSource<? extends T1> source1, SingleSource<? extends T2> source2, SingleSource<? extends T3> source3, SingleSource<? extends T4> source4, SingleSource<? extends T5> source5, SingleSource<? extends T6> source6, SingleSource<? extends T7> source7, Function7<? super T1, ? super T2, ? super T3, ? super T4, ? super T5, ? super T6, ? super T7, ? extends R> zipper) {
        ObjectHelper.requireNonNull(source1, "source1 is null");
        ObjectHelper.requireNonNull(source2, "source2 is null");
        ObjectHelper.requireNonNull(source3, "source3 is null");
        ObjectHelper.requireNonNull(source4, "source4 is null");
        ObjectHelper.requireNonNull(source5, "source5 is null");
        ObjectHelper.requireNonNull(source6, "source6 is null");
        ObjectHelper.requireNonNull(source7, "source7 is null");
        return zipArray(Functions.toFunction(zipper), source1, source2, source3, source4, source5, source6, source7);
    }

    @SchedulerSupport("none")
    public static <T1, T2, T3, T4, T5, T6, T7, T8, R> Single<R> zip(SingleSource<? extends T1> source1, SingleSource<? extends T2> source2, SingleSource<? extends T3> source3, SingleSource<? extends T4> source4, SingleSource<? extends T5> source5, SingleSource<? extends T6> source6, SingleSource<? extends T7> source7, SingleSource<? extends T8> source8, Function8<? super T1, ? super T2, ? super T3, ? super T4, ? super T5, ? super T6, ? super T7, ? super T8, ? extends R> zipper) {
        ObjectHelper.requireNonNull(source1, "source1 is null");
        ObjectHelper.requireNonNull(source2, "source2 is null");
        ObjectHelper.requireNonNull(source3, "source3 is null");
        ObjectHelper.requireNonNull(source4, "source4 is null");
        ObjectHelper.requireNonNull(source5, "source5 is null");
        ObjectHelper.requireNonNull(source6, "source6 is null");
        ObjectHelper.requireNonNull(source7, "source7 is null");
        ObjectHelper.requireNonNull(source8, "source8 is null");
        return zipArray(Functions.toFunction(zipper), source1, source2, source3, source4, source5, source6, source7, source8);
    }

    @SchedulerSupport("none")
    public static <T1, T2, T3, T4, T5, T6, T7, T8, T9, R> Single<R> zip(SingleSource<? extends T1> source1, SingleSource<? extends T2> source2, SingleSource<? extends T3> source3, SingleSource<? extends T4> source4, SingleSource<? extends T5> source5, SingleSource<? extends T6> source6, SingleSource<? extends T7> source7, SingleSource<? extends T8> source8, SingleSource<? extends T9> source9, Function9<? super T1, ? super T2, ? super T3, ? super T4, ? super T5, ? super T6, ? super T7, ? super T8, ? super T9, ? extends R> zipper) {
        ObjectHelper.requireNonNull(source1, "source1 is null");
        ObjectHelper.requireNonNull(source2, "source2 is null");
        ObjectHelper.requireNonNull(source3, "source3 is null");
        ObjectHelper.requireNonNull(source4, "source4 is null");
        ObjectHelper.requireNonNull(source5, "source5 is null");
        ObjectHelper.requireNonNull(source6, "source6 is null");
        ObjectHelper.requireNonNull(source7, "source7 is null");
        ObjectHelper.requireNonNull(source8, "source8 is null");
        ObjectHelper.requireNonNull(source9, "source9 is null");
        return zipArray(Functions.toFunction(zipper), source1, source2, source3, source4, source5, source6, source7, source8, source9);
    }

    @SchedulerSupport("none")
    public static <T, R> Single<R> zipArray(Function<? super Object[], ? extends R> zipper, SingleSource<? extends T>... sources) {
        ObjectHelper.requireNonNull(sources, "sources is null");
        Publisher[] sourcePublishers = new Publisher[sources.length];
        int i = 0;
        SingleSource<? extends T>[] arr$ = sources;
        for (SingleSource<? extends T> s : arr$) {
            ObjectHelper.requireNonNull(s, "The " + i + "th source is null");
            sourcePublishers[i] = RxJavaPlugins.onAssembly(new SingleToFlowable(s));
            i++;
        }
        return toSingle(Flowable.zipArray(zipper, false, 1, sourcePublishers));
    }

    @SchedulerSupport("none")
    public final Single<T> ambWith(SingleSource<? extends T> other) {
        ObjectHelper.requireNonNull(other, "other is null");
        return ambArray(this, other);
    }

    @SchedulerSupport("none")
    public final Single<T> hide() {
        return RxJavaPlugins.onAssembly(new SingleHide(this));
    }

    @SchedulerSupport("none")
    public final <R> Single<R> compose(SingleTransformer<T, R> transformer) {
        return wrap(transformer.apply(this));
    }

    @SchedulerSupport("none")
    public final Single<T> cache() {
        return RxJavaPlugins.onAssembly(new SingleCache(this));
    }

    @SchedulerSupport("none")
    public final <U> Single<U> cast(Class<? extends U> clazz) {
        ObjectHelper.requireNonNull(clazz, "clazz is null");
        return map(Functions.castFunction(clazz));
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public final Flowable<T> concatWith(SingleSource<? extends T> other) {
        return concat(this, other);
    }

    @SchedulerSupport("io.reactivex:computation")
    public final Single<T> delay(long time, TimeUnit unit) {
        return delay(time, unit, Schedulers.computation());
    }

    @SchedulerSupport("custom")
    public final Single<T> delay(long time, TimeUnit unit, Scheduler scheduler) {
        ObjectHelper.requireNonNull(unit, "unit is null");
        ObjectHelper.requireNonNull(scheduler, "scheduler is null");
        return RxJavaPlugins.onAssembly(new SingleDelay(this, time, unit, scheduler));
    }

    @SchedulerSupport("none")
    public final Single<T> delaySubscription(CompletableSource other) {
        return RxJavaPlugins.onAssembly(new SingleDelayWithCompletable(this, other));
    }

    @SchedulerSupport("none")
    public final <U> Single<T> delaySubscription(SingleSource<U> other) {
        return RxJavaPlugins.onAssembly(new SingleDelayWithSingle(this, other));
    }

    @SchedulerSupport("none")
    public final <U> Single<T> delaySubscription(ObservableSource<U> other) {
        return RxJavaPlugins.onAssembly(new SingleDelayWithObservable(this, other));
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public final <U> Single<T> delaySubscription(Publisher<U> other) {
        return RxJavaPlugins.onAssembly(new SingleDelayWithPublisher(this, other));
    }

    @SchedulerSupport("io.reactivex:computation")
    public final <U> Single<T> delaySubscription(long time, TimeUnit unit) {
        return delaySubscription(time, unit, Schedulers.computation());
    }

    @SchedulerSupport("custom")
    public final <U> Single<T> delaySubscription(long time, TimeUnit unit, Scheduler scheduler) {
        return delaySubscription(Observable.timer(time, unit, scheduler));
    }

    @SchedulerSupport("none")
    public final Single<T> doOnSubscribe(Consumer<? super Disposable> onSubscribe) {
        ObjectHelper.requireNonNull(onSubscribe, "onSubscribe is null");
        return RxJavaPlugins.onAssembly(new SingleDoOnSubscribe(this, onSubscribe));
    }

    @SchedulerSupport("none")
    public final Single<T> doOnSuccess(Consumer<? super T> onSuccess) {
        ObjectHelper.requireNonNull(onSuccess, "onSuccess is null");
        return RxJavaPlugins.onAssembly(new SingleDoOnSuccess(this, onSuccess));
    }

    @SchedulerSupport("none")
    public final Single<T> doOnEvent(BiConsumer<? super T, ? super Throwable> onEvent) {
        ObjectHelper.requireNonNull(onEvent, "onEvent is null");
        return RxJavaPlugins.onAssembly(new SingleDoOnEvent(this, onEvent));
    }

    @SchedulerSupport("none")
    public final Single<T> doOnError(Consumer<? super Throwable> onError) {
        ObjectHelper.requireNonNull(onError, "onError is null");
        return RxJavaPlugins.onAssembly(new SingleDoOnError(this, onError));
    }

    @SchedulerSupport("none")
    public final Single<T> doOnDispose(Action onDispose) {
        ObjectHelper.requireNonNull(onDispose, "onDispose is null");
        return RxJavaPlugins.onAssembly(new SingleDoOnDispose(this, onDispose));
    }

    @SchedulerSupport("none")
    public final Maybe<T> filter(Predicate<? super T> predicate) {
        ObjectHelper.requireNonNull(predicate, "predicate is null");
        return RxJavaPlugins.onAssembly(new MaybeFilterSingle(this, predicate));
    }

    @SchedulerSupport("none")
    public final <R> Single<R> flatMap(Function<? super T, ? extends SingleSource<? extends R>> mapper) {
        ObjectHelper.requireNonNull(mapper, "mapper is null");
        return RxJavaPlugins.onAssembly(new SingleFlatMap(this, mapper));
    }

    @SchedulerSupport("none")
    public final <R> Maybe<R> flatMapMaybe(Function<? super T, ? extends MaybeSource<? extends R>> mapper) {
        ObjectHelper.requireNonNull(mapper, "mapper is null");
        return RxJavaPlugins.onAssembly(new SingleFlatMapMaybe(this, mapper));
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public final <R> Flowable<R> flatMapPublisher(Function<? super T, ? extends Publisher<? extends R>> mapper) {
        return toFlowable().flatMap(mapper);
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public final <U> Flowable<U> flattenAsFlowable(Function<? super T, ? extends Iterable<? extends U>> mapper) {
        return new SingleFlatMapIterableFlowable(this, mapper);
    }

    @SchedulerSupport("none")
    public final <U> Observable<U> flattenAsObservable(Function<? super T, ? extends Iterable<? extends U>> mapper) {
        return new SingleFlatMapIterableObservable(this, mapper);
    }

    @SchedulerSupport("none")
    public final <R> Observable<R> flatMapObservable(Function<? super T, ? extends ObservableSource<? extends R>> mapper) {
        return toObservable().flatMap(mapper);
    }

    @SchedulerSupport("none")
    public final Completable flatMapCompletable(Function<? super T, ? extends Completable> mapper) {
        ObjectHelper.requireNonNull(mapper, "mapper is null");
        return RxJavaPlugins.onAssembly(new SingleFlatMapCompletable(this, mapper));
    }

    @SchedulerSupport("none")
    public final T blockingGet() {
        BlockingMultiObserver<T> observer = new BlockingMultiObserver<>();
        subscribe(observer);
        return observer.blockingGet();
    }

    @SchedulerSupport("none")
    public final <R> Single<R> lift(SingleOperator<? extends R, ? super T> lift) {
        ObjectHelper.requireNonNull(lift, "onLift is null");
        return RxJavaPlugins.onAssembly(new SingleLift(this, lift));
    }

    @SchedulerSupport("none")
    public final <R> Single<R> map(Function<? super T, ? extends R> mapper) {
        return RxJavaPlugins.onAssembly(new SingleMap(this, mapper));
    }

    @SchedulerSupport("none")
    public final Single<Boolean> contains(Object value) {
        return contains(value, ObjectHelper.equalsPredicate());
    }

    @SchedulerSupport("none")
    public final Single<Boolean> contains(Object value, BiPredicate<Object, Object> comparer) {
        ObjectHelper.requireNonNull(value, "value is null");
        ObjectHelper.requireNonNull(comparer, "comparer is null");
        return RxJavaPlugins.onAssembly(new SingleContains(this, value, comparer));
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public final Flowable<T> mergeWith(SingleSource<? extends T> other) {
        return merge(this, other);
    }

    @SchedulerSupport("custom")
    public final Single<T> observeOn(Scheduler scheduler) {
        ObjectHelper.requireNonNull(scheduler, "scheduler is null");
        return RxJavaPlugins.onAssembly(new SingleObserveOn(this, scheduler));
    }

    @SchedulerSupport("none")
    public final Single<T> onErrorReturn(Function<Throwable, ? extends T> resumeFunction) {
        ObjectHelper.requireNonNull(resumeFunction, "resumeFunction is null");
        return RxJavaPlugins.onAssembly(new SingleOnErrorReturn(this, resumeFunction, null));
    }

    @SchedulerSupport("none")
    public final Single<T> onErrorReturnItem(T value) {
        ObjectHelper.requireNonNull(value, "value is null");
        return RxJavaPlugins.onAssembly(new SingleOnErrorReturn(this, null, value));
    }

    @SchedulerSupport("none")
    public final Single<T> onErrorResumeNext(Single<? extends T> resumeSingleInCaseOfError) {
        ObjectHelper.requireNonNull(resumeSingleInCaseOfError, "resumeSingleInCaseOfError is null");
        return onErrorResumeNext(Functions.justFunction(resumeSingleInCaseOfError));
    }

    @SchedulerSupport("none")
    public final Single<T> onErrorResumeNext(Function<? super Throwable, ? extends SingleSource<? extends T>> resumeFunctionInCaseOfError) {
        ObjectHelper.requireNonNull(resumeFunctionInCaseOfError, "resumeFunctionInCaseOfError is null");
        return RxJavaPlugins.onAssembly(new SingleResumeNext(this, resumeFunctionInCaseOfError));
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public final Flowable<T> repeat() {
        return toFlowable().repeat();
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public final Flowable<T> repeat(long times) {
        return toFlowable().repeat(times);
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public final Flowable<T> repeatWhen(Function<? super Flowable<Object>, ? extends Publisher<Object>> handler) {
        return toFlowable().repeatWhen(handler);
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public final Flowable<T> repeatUntil(BooleanSupplier stop) {
        return toFlowable().repeatUntil(stop);
    }

    @SchedulerSupport("none")
    public final Single<T> retry() {
        return toSingle(toFlowable().retry());
    }

    @SchedulerSupport("none")
    public final Single<T> retry(long times) {
        return toSingle(toFlowable().retry(times));
    }

    @SchedulerSupport("none")
    public final Single<T> retry(BiPredicate<? super Integer, ? super Throwable> predicate) {
        return toSingle(toFlowable().retry(predicate));
    }

    @SchedulerSupport("none")
    public final Single<T> retry(Predicate<? super Throwable> predicate) {
        return toSingle(toFlowable().retry(predicate));
    }

    @SchedulerSupport("none")
    public final Single<T> retryWhen(Function<? super Flowable<Throwable>, ? extends Publisher<Object>> handler) {
        return toSingle(toFlowable().retryWhen(handler));
    }

    @SchedulerSupport("none")
    public final Disposable subscribe() {
        return subscribe(Functions.emptyConsumer(), Functions.ERROR_CONSUMER);
    }

    @SchedulerSupport("none")
    public final Disposable subscribe(BiConsumer<? super T, ? super Throwable> onCallback) {
        ObjectHelper.requireNonNull(onCallback, "onCallback is null");
        BiConsumerSingleObserver<T> s = new BiConsumerSingleObserver<>(onCallback);
        subscribe(s);
        return s;
    }

    @SchedulerSupport("none")
    public final Disposable subscribe(Consumer<? super T> onSuccess) {
        return subscribe(onSuccess, Functions.ERROR_CONSUMER);
    }

    @SchedulerSupport("none")
    public final Disposable subscribe(Consumer<? super T> onSuccess, Consumer<? super Throwable> onError) {
        ObjectHelper.requireNonNull(onSuccess, "onSuccess is null");
        ObjectHelper.requireNonNull(onError, "onError is null");
        ConsumerSingleObserver<T> s = new ConsumerSingleObserver<>(onSuccess, onError);
        subscribe(s);
        return s;
    }

    @SchedulerSupport("none")
    public final void subscribe(SingleObserver<? super T> subscriber) {
        ObjectHelper.requireNonNull(subscriber, "subscriber is null");
        SingleObserver<? super T> subscriber2 = RxJavaPlugins.onSubscribe(this, subscriber);
        ObjectHelper.requireNonNull(subscriber2, "subscriber returned by the RxJavaPlugins hook is null");
        try {
            subscribeActual(subscriber2);
        } catch (NullPointerException ex) {
            throw ex;
        } catch (Throwable ex2) {
            Exceptions.throwIfFatal(ex2);
            NullPointerException npe = new NullPointerException("subscribeActual failed");
            npe.initCause(ex2);
            throw npe;
        }
    }

    @SchedulerSupport("none")
    public final <E extends SingleObserver<? super T>> E subscribeWith(E observer) {
        subscribe((SingleObserver) observer);
        return observer;
    }

    @SchedulerSupport("custom")
    public final Single<T> subscribeOn(Scheduler scheduler) {
        ObjectHelper.requireNonNull(scheduler, "scheduler is null");
        return RxJavaPlugins.onAssembly(new SingleSubscribeOn(this, scheduler));
    }

    @SchedulerSupport("none")
    public final Single<T> takeUntil(CompletableSource other) {
        return takeUntil(new CompletableToFlowable(other));
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public final <E> Single<T> takeUntil(Publisher<E> other) {
        return RxJavaPlugins.onAssembly(new SingleTakeUntil(this, other));
    }

    @SchedulerSupport("none")
    public final <E> Single<T> takeUntil(SingleSource<? extends E> other) {
        return takeUntil(new SingleToFlowable(other));
    }

    @SchedulerSupport("io.reactivex:computation")
    public final Single<T> timeout(long timeout, TimeUnit unit) {
        return timeout0(timeout, unit, Schedulers.computation(), null);
    }

    @SchedulerSupport("custom")
    public final Single<T> timeout(long timeout, TimeUnit unit, Scheduler scheduler) {
        return timeout0(timeout, unit, scheduler, null);
    }

    @SchedulerSupport("custom")
    public final Single<T> timeout(long timeout, TimeUnit unit, Scheduler scheduler, SingleSource<? extends T> other) {
        ObjectHelper.requireNonNull(other, "other is null");
        return timeout0(timeout, unit, scheduler, other);
    }

    @SchedulerSupport("io.reactivex:computation")
    public final Single<T> timeout(long timeout, TimeUnit unit, SingleSource<? extends T> other) {
        ObjectHelper.requireNonNull(other, "other is null");
        return timeout0(timeout, unit, Schedulers.computation(), other);
    }

    private Single<T> timeout0(long timeout, TimeUnit unit, Scheduler scheduler, SingleSource<? extends T> other) {
        ObjectHelper.requireNonNull(unit, "unit is null");
        ObjectHelper.requireNonNull(scheduler, "scheduler is null");
        return RxJavaPlugins.onAssembly(new SingleTimeout(this, timeout, unit, scheduler, other));
    }

    @SchedulerSupport("none")
    public final <R> R to(Function<? super Single<T>, R> convert) {
        try {
            return convert.apply(this);
        } catch (Throwable ex) {
            Exceptions.throwIfFatal(ex);
            throw ExceptionHelper.wrapOrThrow(ex);
        }
    }

    @SchedulerSupport("none")
    public final Completable toCompletable() {
        return RxJavaPlugins.onAssembly(new CompletableFromSingle(this));
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public final Flowable<T> toFlowable() {
        if (this instanceof FuseToFlowable) {
            return ((FuseToFlowable) this).fuseToFlowable();
        }
        return RxJavaPlugins.onAssembly(new SingleToFlowable(this));
    }

    @SchedulerSupport("none")
    public final Future<T> toFuture() {
        return (Future) subscribeWith(new FutureSingleObserver());
    }

    @SchedulerSupport("none")
    public final Maybe<T> toMaybe() {
        if (this instanceof FuseToMaybe) {
            return ((FuseToMaybe) this).fuseToMaybe();
        }
        return RxJavaPlugins.onAssembly(new MaybeFromSingle(this));
    }

    @SchedulerSupport("none")
    public final Observable<T> toObservable() {
        if (this instanceof FuseToObservable) {
            return ((FuseToObservable) this).fuseToObservable();
        }
        return RxJavaPlugins.onAssembly(new SingleToObservable(this));
    }

    @SchedulerSupport("none")
    public final <U, R> Single<R> zipWith(SingleSource<U> other, BiFunction<? super T, ? super U, ? extends R> zipper) {
        return zip(this, other, zipper);
    }

    @SchedulerSupport("none")
    public final TestObserver<T> test() {
        TestObserver<T> ts = new TestObserver<>();
        subscribe(ts);
        return ts;
    }

    @SchedulerSupport("none")
    public final TestObserver<T> test(boolean cancelled) {
        TestObserver<T> ts = new TestObserver<>();
        if (cancelled) {
            ts.cancel();
        }
        subscribe(ts);
        return ts;
    }

    private static <T> Single<T> toSingle(Flowable<T> source) {
        return RxJavaPlugins.onAssembly(new FlowableSingleSingle(source, null));
    }
}
