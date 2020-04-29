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
import dji.thirdparty.io.reactivex.internal.fuseable.FuseToObservable;
import dji.thirdparty.io.reactivex.internal.observers.BlockingMultiObserver;
import dji.thirdparty.io.reactivex.internal.operators.flowable.FlowableConcatMap;
import dji.thirdparty.io.reactivex.internal.operators.flowable.FlowableFlatMap;
import dji.thirdparty.io.reactivex.internal.operators.maybe.MaybeAmb;
import dji.thirdparty.io.reactivex.internal.operators.maybe.MaybeCache;
import dji.thirdparty.io.reactivex.internal.operators.maybe.MaybeCallbackObserver;
import dji.thirdparty.io.reactivex.internal.operators.maybe.MaybeConcatArray;
import dji.thirdparty.io.reactivex.internal.operators.maybe.MaybeConcatArrayDelayError;
import dji.thirdparty.io.reactivex.internal.operators.maybe.MaybeConcatIterable;
import dji.thirdparty.io.reactivex.internal.operators.maybe.MaybeContains;
import dji.thirdparty.io.reactivex.internal.operators.maybe.MaybeCount;
import dji.thirdparty.io.reactivex.internal.operators.maybe.MaybeCreate;
import dji.thirdparty.io.reactivex.internal.operators.maybe.MaybeDefer;
import dji.thirdparty.io.reactivex.internal.operators.maybe.MaybeDelay;
import dji.thirdparty.io.reactivex.internal.operators.maybe.MaybeDelayOtherPublisher;
import dji.thirdparty.io.reactivex.internal.operators.maybe.MaybeDelaySubscriptionOtherPublisher;
import dji.thirdparty.io.reactivex.internal.operators.maybe.MaybeDetach;
import dji.thirdparty.io.reactivex.internal.operators.maybe.MaybeDoOnEvent;
import dji.thirdparty.io.reactivex.internal.operators.maybe.MaybeEmpty;
import dji.thirdparty.io.reactivex.internal.operators.maybe.MaybeEqualSingle;
import dji.thirdparty.io.reactivex.internal.operators.maybe.MaybeError;
import dji.thirdparty.io.reactivex.internal.operators.maybe.MaybeErrorCallable;
import dji.thirdparty.io.reactivex.internal.operators.maybe.MaybeFilter;
import dji.thirdparty.io.reactivex.internal.operators.maybe.MaybeFlatMapBiSelector;
import dji.thirdparty.io.reactivex.internal.operators.maybe.MaybeFlatMapCompletable;
import dji.thirdparty.io.reactivex.internal.operators.maybe.MaybeFlatMapIterableFlowable;
import dji.thirdparty.io.reactivex.internal.operators.maybe.MaybeFlatMapIterableObservable;
import dji.thirdparty.io.reactivex.internal.operators.maybe.MaybeFlatMapNotification;
import dji.thirdparty.io.reactivex.internal.operators.maybe.MaybeFlatMapSingle;
import dji.thirdparty.io.reactivex.internal.operators.maybe.MaybeFlatten;
import dji.thirdparty.io.reactivex.internal.operators.maybe.MaybeFromAction;
import dji.thirdparty.io.reactivex.internal.operators.maybe.MaybeFromCallable;
import dji.thirdparty.io.reactivex.internal.operators.maybe.MaybeFromCompletable;
import dji.thirdparty.io.reactivex.internal.operators.maybe.MaybeFromFuture;
import dji.thirdparty.io.reactivex.internal.operators.maybe.MaybeFromRunnable;
import dji.thirdparty.io.reactivex.internal.operators.maybe.MaybeFromSingle;
import dji.thirdparty.io.reactivex.internal.operators.maybe.MaybeHide;
import dji.thirdparty.io.reactivex.internal.operators.maybe.MaybeIgnoreElementCompletable;
import dji.thirdparty.io.reactivex.internal.operators.maybe.MaybeIsEmptySingle;
import dji.thirdparty.io.reactivex.internal.operators.maybe.MaybeJust;
import dji.thirdparty.io.reactivex.internal.operators.maybe.MaybeLift;
import dji.thirdparty.io.reactivex.internal.operators.maybe.MaybeMap;
import dji.thirdparty.io.reactivex.internal.operators.maybe.MaybeMergeArray;
import dji.thirdparty.io.reactivex.internal.operators.maybe.MaybeNever;
import dji.thirdparty.io.reactivex.internal.operators.maybe.MaybeObserveOn;
import dji.thirdparty.io.reactivex.internal.operators.maybe.MaybeOnErrorComplete;
import dji.thirdparty.io.reactivex.internal.operators.maybe.MaybeOnErrorNext;
import dji.thirdparty.io.reactivex.internal.operators.maybe.MaybeOnErrorReturn;
import dji.thirdparty.io.reactivex.internal.operators.maybe.MaybePeek;
import dji.thirdparty.io.reactivex.internal.operators.maybe.MaybeSubscribeOn;
import dji.thirdparty.io.reactivex.internal.operators.maybe.MaybeSwitchIfEmpty;
import dji.thirdparty.io.reactivex.internal.operators.maybe.MaybeTakeUntilMaybe;
import dji.thirdparty.io.reactivex.internal.operators.maybe.MaybeTakeUntilPublisher;
import dji.thirdparty.io.reactivex.internal.operators.maybe.MaybeTimeoutMaybe;
import dji.thirdparty.io.reactivex.internal.operators.maybe.MaybeTimeoutPublisher;
import dji.thirdparty.io.reactivex.internal.operators.maybe.MaybeTimer;
import dji.thirdparty.io.reactivex.internal.operators.maybe.MaybeToFlowable;
import dji.thirdparty.io.reactivex.internal.operators.maybe.MaybeToObservable;
import dji.thirdparty.io.reactivex.internal.operators.maybe.MaybeToPublisher;
import dji.thirdparty.io.reactivex.internal.operators.maybe.MaybeToSingle;
import dji.thirdparty.io.reactivex.internal.operators.maybe.MaybeUnsafeCreate;
import dji.thirdparty.io.reactivex.internal.operators.maybe.MaybeUnsubscribeOn;
import dji.thirdparty.io.reactivex.internal.operators.maybe.MaybeUsing;
import dji.thirdparty.io.reactivex.internal.operators.maybe.MaybeZipArray;
import dji.thirdparty.io.reactivex.internal.operators.maybe.MaybeZipIterable;
import dji.thirdparty.io.reactivex.internal.util.ErrorMode;
import dji.thirdparty.io.reactivex.internal.util.ExceptionHelper;
import dji.thirdparty.io.reactivex.observers.TestObserver;
import dji.thirdparty.io.reactivex.plugins.RxJavaPlugins;
import dji.thirdparty.io.reactivex.schedulers.Schedulers;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import kotlin.jvm.internal.LongCompanionObject;
import org.reactivestreams.Publisher;

public abstract class Maybe<T> implements MaybeSource<T> {
    /* access modifiers changed from: protected */
    public abstract void subscribeActual(MaybeObserver<? super T> maybeObserver);

    @SchedulerSupport("none")
    public static <T> Maybe<T> amb(Iterable<? extends MaybeSource<? extends T>> sources) {
        ObjectHelper.requireNonNull(sources, "sources is null");
        return RxJavaPlugins.onAssembly(new MaybeAmb(null, sources));
    }

    @SchedulerSupport("none")
    public static <T> Maybe<T> ambArray(MaybeSource<? extends T>... sources) {
        if (sources.length == 0) {
            return empty();
        }
        if (sources.length == 1) {
            return wrap(sources[0]);
        }
        return RxJavaPlugins.onAssembly(new MaybeAmb(sources, null));
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public static <T> Flowable<T> concat(Iterable<? extends MaybeSource<? extends T>> sources) {
        ObjectHelper.requireNonNull(sources, "sources is null");
        return RxJavaPlugins.onAssembly(new MaybeConcatIterable(sources));
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public static <T> Flowable<T> concat(MaybeSource<? extends T> source1, MaybeSource<? extends T> source2) {
        ObjectHelper.requireNonNull(source1, "source1 is null");
        ObjectHelper.requireNonNull(source2, "source2 is null");
        return concatArray(source1, source2);
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public static <T> Flowable<T> concat(MaybeSource<? extends T> source1, MaybeSource<? extends T> source2, MaybeSource<? extends T> source3) {
        ObjectHelper.requireNonNull(source1, "source1 is null");
        ObjectHelper.requireNonNull(source2, "source2 is null");
        ObjectHelper.requireNonNull(source3, "source3 is null");
        return concatArray(source1, source2, source3);
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public static <T> Flowable<T> concat(MaybeSource<? extends T> source1, MaybeSource<? extends T> source2, MaybeSource<? extends T> source3, MaybeSource<? extends T> source4) {
        ObjectHelper.requireNonNull(source1, "source1 is null");
        ObjectHelper.requireNonNull(source2, "source2 is null");
        ObjectHelper.requireNonNull(source3, "source3 is null");
        ObjectHelper.requireNonNull(source4, "source4 is null");
        return concatArray(source1, source2, source3, source4);
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public static <T> Flowable<T> concat(Publisher<? extends MaybeSource<? extends T>> sources) {
        return concat(sources, 2);
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public static <T> Flowable<T> concat(Publisher<? extends MaybeSource<? extends T>> sources, int prefetch) {
        ObjectHelper.requireNonNull(sources, "sources is null");
        ObjectHelper.verifyPositive(prefetch, "prefetch");
        return RxJavaPlugins.onAssembly(new FlowableConcatMap(sources, MaybeToPublisher.instance(), prefetch, ErrorMode.IMMEDIATE));
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public static <T> Flowable<T> concatArray(MaybeSource<? extends T>... sources) {
        ObjectHelper.requireNonNull(sources, "sources is null");
        if (sources.length == 0) {
            return Flowable.empty();
        }
        if (sources.length == 1) {
            return RxJavaPlugins.onAssembly(new MaybeToFlowable(sources[0]));
        }
        return RxJavaPlugins.onAssembly(new MaybeConcatArray(sources));
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public static <T> Flowable<T> concatArrayDelayError(MaybeSource<? extends T>... sources) {
        if (sources.length == 0) {
            return Flowable.empty();
        }
        if (sources.length == 1) {
            return RxJavaPlugins.onAssembly(new MaybeToFlowable(sources[0]));
        }
        return RxJavaPlugins.onAssembly(new MaybeConcatArrayDelayError(sources));
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public static <T> Flowable<T> concatArrayEager(MaybeSource<? extends T>... sources) {
        return Flowable.fromArray(sources).concatMapEager(MaybeToPublisher.instance());
    }

    /* JADX WARN: Type inference failed for: r2v0, types: [java.lang.Iterable<? extends dji.thirdparty.io.reactivex.MaybeSource<? extends T>>, java.lang.Object, java.lang.Iterable], assign insn: null */
    /* JADX WARNING: Unknown variable types count: 1 */
    @dji.thirdparty.io.reactivex.annotations.BackpressureSupport(dji.thirdparty.io.reactivex.annotations.BackpressureKind.FULL)
    @dji.thirdparty.io.reactivex.annotations.SchedulerSupport("none")
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static <T> dji.thirdparty.io.reactivex.Flowable<T> concatDelayError(java.lang.Iterable<? extends dji.thirdparty.io.reactivex.MaybeSource<? extends T>> r2) {
        /*
            java.lang.String r0 = "sources is null"
            dji.thirdparty.io.reactivex.internal.functions.ObjectHelper.requireNonNull(r2, r0)
            dji.thirdparty.io.reactivex.Flowable r0 = dji.thirdparty.io.reactivex.Flowable.fromIterable(r2)
            dji.thirdparty.io.reactivex.functions.Function r1 = dji.thirdparty.io.reactivex.internal.operators.maybe.MaybeToPublisher.instance()
            dji.thirdparty.io.reactivex.Flowable r0 = r0.concatMapDelayError(r1)
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: dji.thirdparty.io.reactivex.Maybe.concatDelayError(java.lang.Iterable):dji.thirdparty.io.reactivex.Flowable");
    }

    /* JADX WARN: Type inference failed for: r2v0, types: [org.reactivestreams.Publisher<? extends dji.thirdparty.io.reactivex.MaybeSource<? extends T>>, org.reactivestreams.Publisher], assign insn: null */
    /* JADX WARNING: Unknown variable types count: 1 */
    @dji.thirdparty.io.reactivex.annotations.BackpressureSupport(dji.thirdparty.io.reactivex.annotations.BackpressureKind.FULL)
    @dji.thirdparty.io.reactivex.annotations.SchedulerSupport("none")
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static <T> dji.thirdparty.io.reactivex.Flowable<T> concatDelayError(org.reactivestreams.Publisher<? extends dji.thirdparty.io.reactivex.MaybeSource<? extends T>> r2) {
        /*
            dji.thirdparty.io.reactivex.Flowable r0 = dji.thirdparty.io.reactivex.Flowable.fromPublisher(r2)
            dji.thirdparty.io.reactivex.functions.Function r1 = dji.thirdparty.io.reactivex.internal.operators.maybe.MaybeToPublisher.instance()
            dji.thirdparty.io.reactivex.Flowable r0 = r0.concatMapDelayError(r1)
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: dji.thirdparty.io.reactivex.Maybe.concatDelayError(org.reactivestreams.Publisher):dji.thirdparty.io.reactivex.Flowable");
    }

    /* JADX WARN: Type inference failed for: r2v0, types: [java.lang.Iterable<? extends dji.thirdparty.io.reactivex.MaybeSource<? extends T>>, java.lang.Iterable], assign insn: null */
    /* JADX WARNING: Unknown variable types count: 1 */
    @dji.thirdparty.io.reactivex.annotations.BackpressureSupport(dji.thirdparty.io.reactivex.annotations.BackpressureKind.FULL)
    @dji.thirdparty.io.reactivex.annotations.SchedulerSupport("none")
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static <T> dji.thirdparty.io.reactivex.Flowable<T> concatEager(java.lang.Iterable<? extends dji.thirdparty.io.reactivex.MaybeSource<? extends T>> r2) {
        /*
            dji.thirdparty.io.reactivex.Flowable r0 = dji.thirdparty.io.reactivex.Flowable.fromIterable(r2)
            dji.thirdparty.io.reactivex.functions.Function r1 = dji.thirdparty.io.reactivex.internal.operators.maybe.MaybeToPublisher.instance()
            dji.thirdparty.io.reactivex.Flowable r0 = r0.concatMapEager(r1)
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: dji.thirdparty.io.reactivex.Maybe.concatEager(java.lang.Iterable):dji.thirdparty.io.reactivex.Flowable");
    }

    /* JADX WARN: Type inference failed for: r2v0, types: [org.reactivestreams.Publisher<? extends dji.thirdparty.io.reactivex.MaybeSource<? extends T>>, org.reactivestreams.Publisher], assign insn: null */
    /* JADX WARNING: Unknown variable types count: 1 */
    @dji.thirdparty.io.reactivex.annotations.BackpressureSupport(dji.thirdparty.io.reactivex.annotations.BackpressureKind.FULL)
    @dji.thirdparty.io.reactivex.annotations.SchedulerSupport("none")
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static <T> dji.thirdparty.io.reactivex.Flowable<T> concatEager(org.reactivestreams.Publisher<? extends dji.thirdparty.io.reactivex.MaybeSource<? extends T>> r2) {
        /*
            dji.thirdparty.io.reactivex.Flowable r0 = dji.thirdparty.io.reactivex.Flowable.fromPublisher(r2)
            dji.thirdparty.io.reactivex.functions.Function r1 = dji.thirdparty.io.reactivex.internal.operators.maybe.MaybeToPublisher.instance()
            dji.thirdparty.io.reactivex.Flowable r0 = r0.concatMapEager(r1)
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: dji.thirdparty.io.reactivex.Maybe.concatEager(org.reactivestreams.Publisher):dji.thirdparty.io.reactivex.Flowable");
    }

    @SchedulerSupport("none")
    public static <T> Maybe<T> create(MaybeOnSubscribe<T> onSubscribe) {
        ObjectHelper.requireNonNull(onSubscribe, "onSubscribe is null");
        return RxJavaPlugins.onAssembly(new MaybeCreate(onSubscribe));
    }

    @SchedulerSupport("none")
    public static <T> Maybe<T> defer(Callable<? extends MaybeSource<? extends T>> maybeSupplier) {
        ObjectHelper.requireNonNull(maybeSupplier, "maybeSupplier is null");
        return RxJavaPlugins.onAssembly(new MaybeDefer(maybeSupplier));
    }

    @SchedulerSupport("none")
    public static <T> Maybe<T> empty() {
        return RxJavaPlugins.onAssembly(MaybeEmpty.INSTANCE);
    }

    @SchedulerSupport("none")
    public static <T> Maybe<T> error(Throwable exception) {
        ObjectHelper.requireNonNull(exception, "exception is null");
        return RxJavaPlugins.onAssembly(new MaybeError(exception));
    }

    @SchedulerSupport("none")
    public static <T> Maybe<T> error(Callable<? extends Throwable> supplier) {
        ObjectHelper.requireNonNull(supplier, "errorSupplier is null");
        return RxJavaPlugins.onAssembly(new MaybeErrorCallable(supplier));
    }

    @SchedulerSupport("none")
    public static <T> Maybe<T> fromAction(Action run) {
        ObjectHelper.requireNonNull(run, "run is null");
        return RxJavaPlugins.onAssembly(new MaybeFromAction(run));
    }

    @SchedulerSupport("none")
    public static <T> Maybe<T> fromCompletable(CompletableSource completableSource) {
        ObjectHelper.requireNonNull(completableSource, "completableSource is null");
        return RxJavaPlugins.onAssembly(new MaybeFromCompletable(completableSource));
    }

    @SchedulerSupport("none")
    public static <T> Maybe<T> fromSingle(SingleSource<T> singleSource) {
        ObjectHelper.requireNonNull(singleSource, "singleSource is null");
        return RxJavaPlugins.onAssembly(new MaybeFromSingle(singleSource));
    }

    @SchedulerSupport("none")
    public static <T> Maybe<T> fromCallable(Callable<? extends T> callable) {
        ObjectHelper.requireNonNull(callable, "callable is null");
        return RxJavaPlugins.onAssembly(new MaybeFromCallable(callable));
    }

    @SchedulerSupport("none")
    public static <T> Maybe<T> fromFuture(Future<? extends T> future) {
        ObjectHelper.requireNonNull(future, "future is null");
        return RxJavaPlugins.onAssembly(new MaybeFromFuture(future, 0, null));
    }

    @SchedulerSupport("none")
    public static <T> Maybe<T> fromFuture(Future<? extends T> future, long timeout, TimeUnit unit) {
        ObjectHelper.requireNonNull(future, "future is null");
        ObjectHelper.requireNonNull(unit, "unit is null");
        return RxJavaPlugins.onAssembly(new MaybeFromFuture(future, timeout, unit));
    }

    @SchedulerSupport("none")
    public static <T> Maybe<T> fromRunnable(Runnable run) {
        ObjectHelper.requireNonNull(run, "run is null");
        return RxJavaPlugins.onAssembly(new MaybeFromRunnable(run));
    }

    @SchedulerSupport("none")
    public static <T> Maybe<T> just(T item) {
        ObjectHelper.requireNonNull(item, "item is null");
        return RxJavaPlugins.onAssembly(new MaybeJust(item));
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public static <T> Flowable<T> merge(Iterable<? extends MaybeSource<? extends T>> sources) {
        return merge(Flowable.fromIterable(sources));
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public static <T> Flowable<T> merge(Publisher<? extends MaybeSource<? extends T>> sources) {
        return merge(sources, Integer.MAX_VALUE);
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public static <T> Flowable<T> merge(Publisher<? extends MaybeSource<? extends T>> sources, int maxConcurrency) {
        return RxJavaPlugins.onAssembly(new FlowableFlatMap(sources, MaybeToPublisher.instance(), false, maxConcurrency, Flowable.bufferSize()));
    }

    @SchedulerSupport("none")
    public static <T> Maybe<T> merge(MaybeSource<? extends MaybeSource<? extends T>> source) {
        return RxJavaPlugins.onAssembly(new MaybeFlatten(source, Functions.identity()));
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public static <T> Flowable<T> merge(MaybeSource<? extends T> source1, MaybeSource<? extends T> source2) {
        ObjectHelper.requireNonNull(source1, "source1 is null");
        ObjectHelper.requireNonNull(source2, "source2 is null");
        return mergeArray(source1, source2);
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public static <T> Flowable<T> merge(MaybeSource<? extends T> source1, MaybeSource<? extends T> source2, MaybeSource<? extends T> source3) {
        ObjectHelper.requireNonNull(source1, "source1 is null");
        ObjectHelper.requireNonNull(source2, "source2 is null");
        ObjectHelper.requireNonNull(source3, "source3 is null");
        return mergeArray(source1, source2, source3);
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public static <T> Flowable<T> merge(MaybeSource<? extends T> source1, MaybeSource<? extends T> source2, MaybeSource<? extends T> source3, MaybeSource<? extends T> source4) {
        ObjectHelper.requireNonNull(source1, "source1 is null");
        ObjectHelper.requireNonNull(source2, "source2 is null");
        ObjectHelper.requireNonNull(source3, "source3 is null");
        ObjectHelper.requireNonNull(source4, "source4 is null");
        return mergeArray(source1, source2, source3, source4);
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public static <T> Flowable<T> mergeArray(MaybeSource<? extends T>... sources) {
        ObjectHelper.requireNonNull(sources, "sources is null");
        if (sources.length == 0) {
            return Flowable.empty();
        }
        if (sources.length == 1) {
            return RxJavaPlugins.onAssembly(new MaybeToFlowable(sources[0]));
        }
        return RxJavaPlugins.onAssembly(new MaybeMergeArray(sources));
    }

    /* JADX DEBUG: Failed to find minimal casts for resolve overloaded methods, cast all args instead
     method: dji.thirdparty.io.reactivex.Flowable.flatMap(dji.thirdparty.io.reactivex.functions.Function, boolean, int):dji.thirdparty.io.reactivex.Flowable<R>
     arg types: [dji.thirdparty.io.reactivex.functions.Function, int, int]
     candidates:
      dji.thirdparty.io.reactivex.Flowable.flatMap(dji.thirdparty.io.reactivex.functions.Function, dji.thirdparty.io.reactivex.functions.BiFunction, int):dji.thirdparty.io.reactivex.Flowable<R>
      dji.thirdparty.io.reactivex.Flowable.flatMap(dji.thirdparty.io.reactivex.functions.Function, dji.thirdparty.io.reactivex.functions.BiFunction, boolean):dji.thirdparty.io.reactivex.Flowable<R>
      dji.thirdparty.io.reactivex.Flowable.flatMap(dji.thirdparty.io.reactivex.functions.Function, dji.thirdparty.io.reactivex.functions.Function, java.util.concurrent.Callable):dji.thirdparty.io.reactivex.Flowable<R>
      dji.thirdparty.io.reactivex.Flowable.flatMap(dji.thirdparty.io.reactivex.functions.Function, boolean, int):dji.thirdparty.io.reactivex.Flowable<R> */
    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public static <T> Flowable<T> mergeArrayDelayError(MaybeSource<? extends T>... sources) {
        return Flowable.fromArray(sources).flatMap(MaybeToPublisher.instance(), true, sources.length);
    }

    /* JADX WARN: Type inference failed for: r3v0, types: [java.lang.Iterable<? extends dji.thirdparty.io.reactivex.MaybeSource<? extends T>>, java.lang.Iterable], assign insn: null */
    /* JADX DEBUG: Failed to find minimal casts for resolve overloaded methods, cast all args instead
     method: dji.thirdparty.io.reactivex.Flowable.flatMap(dji.thirdparty.io.reactivex.functions.Function, boolean):dji.thirdparty.io.reactivex.Flowable<R>
     arg types: [dji.thirdparty.io.reactivex.functions.Function, int]
     candidates:
      dji.thirdparty.io.reactivex.Flowable.flatMap(dji.thirdparty.io.reactivex.functions.Function, int):dji.thirdparty.io.reactivex.Flowable<R>
      dji.thirdparty.io.reactivex.Flowable.flatMap(dji.thirdparty.io.reactivex.functions.Function, dji.thirdparty.io.reactivex.functions.BiFunction):dji.thirdparty.io.reactivex.Flowable<R>
      dji.thirdparty.io.reactivex.Flowable.flatMap(dji.thirdparty.io.reactivex.functions.Function, boolean):dji.thirdparty.io.reactivex.Flowable<R> */
    /* JADX WARNING: Unknown variable types count: 1 */
    @dji.thirdparty.io.reactivex.annotations.BackpressureSupport(dji.thirdparty.io.reactivex.annotations.BackpressureKind.FULL)
    @dji.thirdparty.io.reactivex.annotations.SchedulerSupport("none")
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static <T> dji.thirdparty.io.reactivex.Flowable<T> mergeDelayError(java.lang.Iterable<? extends dji.thirdparty.io.reactivex.MaybeSource<? extends T>> r3) {
        /*
            dji.thirdparty.io.reactivex.Flowable r0 = dji.thirdparty.io.reactivex.Flowable.fromIterable(r3)
            dji.thirdparty.io.reactivex.functions.Function r1 = dji.thirdparty.io.reactivex.internal.operators.maybe.MaybeToPublisher.instance()
            r2 = 1
            dji.thirdparty.io.reactivex.Flowable r0 = r0.flatMap(r1, r2)
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: dji.thirdparty.io.reactivex.Maybe.mergeDelayError(java.lang.Iterable):dji.thirdparty.io.reactivex.Flowable");
    }

    /* JADX WARN: Type inference failed for: r3v0, types: [org.reactivestreams.Publisher<? extends dji.thirdparty.io.reactivex.MaybeSource<? extends T>>, org.reactivestreams.Publisher], assign insn: null */
    /* JADX DEBUG: Failed to find minimal casts for resolve overloaded methods, cast all args instead
     method: dji.thirdparty.io.reactivex.Flowable.flatMap(dji.thirdparty.io.reactivex.functions.Function, boolean):dji.thirdparty.io.reactivex.Flowable<R>
     arg types: [dji.thirdparty.io.reactivex.functions.Function, int]
     candidates:
      dji.thirdparty.io.reactivex.Flowable.flatMap(dji.thirdparty.io.reactivex.functions.Function, int):dji.thirdparty.io.reactivex.Flowable<R>
      dji.thirdparty.io.reactivex.Flowable.flatMap(dji.thirdparty.io.reactivex.functions.Function, dji.thirdparty.io.reactivex.functions.BiFunction):dji.thirdparty.io.reactivex.Flowable<R>
      dji.thirdparty.io.reactivex.Flowable.flatMap(dji.thirdparty.io.reactivex.functions.Function, boolean):dji.thirdparty.io.reactivex.Flowable<R> */
    /* JADX WARNING: Unknown variable types count: 1 */
    @dji.thirdparty.io.reactivex.annotations.BackpressureSupport(dji.thirdparty.io.reactivex.annotations.BackpressureKind.FULL)
    @dji.thirdparty.io.reactivex.annotations.SchedulerSupport("none")
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static <T> dji.thirdparty.io.reactivex.Flowable<T> mergeDelayError(org.reactivestreams.Publisher<? extends dji.thirdparty.io.reactivex.MaybeSource<? extends T>> r3) {
        /*
            dji.thirdparty.io.reactivex.Flowable r0 = dji.thirdparty.io.reactivex.Flowable.fromPublisher(r3)
            dji.thirdparty.io.reactivex.functions.Function r1 = dji.thirdparty.io.reactivex.internal.operators.maybe.MaybeToPublisher.instance()
            r2 = 1
            dji.thirdparty.io.reactivex.Flowable r0 = r0.flatMap(r1, r2)
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: dji.thirdparty.io.reactivex.Maybe.mergeDelayError(org.reactivestreams.Publisher):dji.thirdparty.io.reactivex.Flowable");
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public static <T> Flowable<T> mergeDelayError(MaybeSource<? extends T> source1, MaybeSource<? extends T> source2) {
        ObjectHelper.requireNonNull(source1, "source1 is null");
        ObjectHelper.requireNonNull(source2, "source2 is null");
        return mergeArrayDelayError(source1, source2);
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public static <T> Flowable<T> mergeDelayError(MaybeSource<? extends T> source1, MaybeSource<? extends T> source2, MaybeSource<? extends T> source3) {
        ObjectHelper.requireNonNull(source1, "source1 is null");
        ObjectHelper.requireNonNull(source2, "source2 is null");
        ObjectHelper.requireNonNull(source3, "source3 is null");
        return mergeArrayDelayError(source1, source2, source3);
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public static <T> Flowable<T> mergeDelayError(MaybeSource<? extends T> source1, MaybeSource<? extends T> source2, MaybeSource<? extends T> source3, MaybeSource<? extends T> source4) {
        ObjectHelper.requireNonNull(source1, "source1 is null");
        ObjectHelper.requireNonNull(source2, "source2 is null");
        ObjectHelper.requireNonNull(source3, "source3 is null");
        ObjectHelper.requireNonNull(source4, "source4 is null");
        return mergeArrayDelayError(source1, source2, source3, source4);
    }

    @SchedulerSupport("none")
    public static <T> Maybe<T> never() {
        return RxJavaPlugins.onAssembly(MaybeNever.INSTANCE);
    }

    @SchedulerSupport("none")
    public static <T> Single<Boolean> sequenceEqual(MaybeSource<? extends T> source1, MaybeSource<? extends T> source2) {
        return sequenceEqual(source1, source2, ObjectHelper.equalsPredicate());
    }

    @SchedulerSupport("none")
    public static <T> Single<Boolean> sequenceEqual(MaybeSource<? extends T> source1, MaybeSource<? extends T> source2, BiPredicate<? super T, ? super T> isEqual) {
        return RxJavaPlugins.onAssembly(new MaybeEqualSingle(source1, source2, isEqual));
    }

    @SchedulerSupport("io.reactivex:computation")
    public static Maybe<Long> timer(long delay, TimeUnit unit) {
        return timer(delay, unit, Schedulers.computation());
    }

    /* JADX DEBUG: Failed to find minimal casts for resolve overloaded methods, cast all args instead
     method: ClspMth{java.lang.Math.max(long, long):long}
     arg types: [int, long]
     candidates:
      ClspMth{java.lang.Math.max(double, double):double}
      ClspMth{java.lang.Math.max(int, int):int}
      ClspMth{java.lang.Math.max(float, float):float}
      ClspMth{java.lang.Math.max(long, long):long} */
    @SchedulerSupport("custom")
    public static Maybe<Long> timer(long delay, TimeUnit unit, Scheduler scheduler) {
        ObjectHelper.requireNonNull(unit, "unit is null");
        ObjectHelper.requireNonNull(scheduler, "scheduler is null");
        return RxJavaPlugins.onAssembly(new MaybeTimer(Math.max(0L, delay), unit, scheduler));
    }

    @SchedulerSupport("none")
    public static <T> Maybe<T> unsafeCreate(MaybeSource<T> onSubscribe) {
        if (onSubscribe instanceof Maybe) {
            throw new IllegalArgumentException("unsafeCreate(Maybe) should be upgraded");
        }
        ObjectHelper.requireNonNull(onSubscribe, "onSubscribe is null");
        return RxJavaPlugins.onAssembly(new MaybeUnsafeCreate(onSubscribe));
    }

    @SchedulerSupport("none")
    public static <T, D> Maybe<T> using(Callable<? extends D> resourceSupplier, Function<? super D, ? extends MaybeSource<? extends T>> sourceSupplier, Consumer<? super D> resourceDisposer) {
        return using(resourceSupplier, sourceSupplier, resourceDisposer, true);
    }

    @SchedulerSupport("none")
    public static <T, D> Maybe<T> using(Callable<? extends D> resourceSupplier, Function<? super D, ? extends MaybeSource<? extends T>> sourceSupplier, Consumer<? super D> resourceDisposer, boolean eager) {
        ObjectHelper.requireNonNull(resourceSupplier, "resourceSupplier is null");
        ObjectHelper.requireNonNull(sourceSupplier, "sourceSupplier is null");
        ObjectHelper.requireNonNull(resourceDisposer, "disposer is null");
        return RxJavaPlugins.onAssembly(new MaybeUsing(resourceSupplier, sourceSupplier, resourceDisposer, eager));
    }

    @SchedulerSupport("none")
    public static <T> Maybe<T> wrap(MaybeSource<T> source) {
        if (source instanceof Maybe) {
            return RxJavaPlugins.onAssembly((Maybe) source);
        }
        ObjectHelper.requireNonNull(source, "onSubscribe is null");
        return RxJavaPlugins.onAssembly(new MaybeUnsafeCreate(source));
    }

    @SchedulerSupport("none")
    public static <T, R> Maybe<R> zip(Iterable<? extends MaybeSource<? extends T>> sources, Function<? super Object[], ? extends R> zipper) {
        ObjectHelper.requireNonNull(zipper, "zipper is null");
        ObjectHelper.requireNonNull(sources, "sources is null");
        return RxJavaPlugins.onAssembly(new MaybeZipIterable(sources, zipper));
    }

    @SchedulerSupport("none")
    public static <T1, T2, R> Maybe<R> zip(MaybeSource<? extends T1> source1, MaybeSource<? extends T2> source2, BiFunction<? super T1, ? super T2, ? extends R> zipper) {
        ObjectHelper.requireNonNull(source1, "source1 is null");
        ObjectHelper.requireNonNull(source2, "source2 is null");
        return zipArray(Functions.toFunction(zipper), source1, source2);
    }

    @SchedulerSupport("none")
    public static <T1, T2, T3, R> Maybe<R> zip(MaybeSource<? extends T1> source1, MaybeSource<? extends T2> source2, MaybeSource<? extends T3> source3, Function3<? super T1, ? super T2, ? super T3, ? extends R> zipper) {
        ObjectHelper.requireNonNull(source1, "source1 is null");
        ObjectHelper.requireNonNull(source2, "source2 is null");
        ObjectHelper.requireNonNull(source3, "source3 is null");
        return zipArray(Functions.toFunction(zipper), source1, source2, source3);
    }

    @SchedulerSupport("none")
    public static <T1, T2, T3, T4, R> Maybe<R> zip(MaybeSource<? extends T1> source1, MaybeSource<? extends T2> source2, MaybeSource<? extends T3> source3, MaybeSource<? extends T4> source4, Function4<? super T1, ? super T2, ? super T3, ? super T4, ? extends R> zipper) {
        ObjectHelper.requireNonNull(source1, "source1 is null");
        ObjectHelper.requireNonNull(source2, "source2 is null");
        ObjectHelper.requireNonNull(source3, "source3 is null");
        ObjectHelper.requireNonNull(source4, "source4 is null");
        return zipArray(Functions.toFunction(zipper), source1, source2, source3, source4);
    }

    @SchedulerSupport("none")
    public static <T1, T2, T3, T4, T5, R> Maybe<R> zip(MaybeSource<? extends T1> source1, MaybeSource<? extends T2> source2, MaybeSource<? extends T3> source3, MaybeSource<? extends T4> source4, MaybeSource<? extends T5> source5, Function5<? super T1, ? super T2, ? super T3, ? super T4, ? super T5, ? extends R> zipper) {
        ObjectHelper.requireNonNull(source1, "source1 is null");
        ObjectHelper.requireNonNull(source2, "source2 is null");
        ObjectHelper.requireNonNull(source3, "source3 is null");
        ObjectHelper.requireNonNull(source4, "source4 is null");
        ObjectHelper.requireNonNull(source5, "source5 is null");
        return zipArray(Functions.toFunction(zipper), source1, source2, source3, source4, source5);
    }

    @SchedulerSupport("none")
    public static <T1, T2, T3, T4, T5, T6, R> Maybe<R> zip(MaybeSource<? extends T1> source1, MaybeSource<? extends T2> source2, MaybeSource<? extends T3> source3, MaybeSource<? extends T4> source4, MaybeSource<? extends T5> source5, MaybeSource<? extends T6> source6, Function6<? super T1, ? super T2, ? super T3, ? super T4, ? super T5, ? super T6, ? extends R> zipper) {
        ObjectHelper.requireNonNull(source1, "source1 is null");
        ObjectHelper.requireNonNull(source2, "source2 is null");
        ObjectHelper.requireNonNull(source3, "source3 is null");
        ObjectHelper.requireNonNull(source4, "source4 is null");
        ObjectHelper.requireNonNull(source5, "source5 is null");
        ObjectHelper.requireNonNull(source6, "source6 is null");
        return zipArray(Functions.toFunction(zipper), source1, source2, source3, source4, source5, source6);
    }

    @SchedulerSupport("none")
    public static <T1, T2, T3, T4, T5, T6, T7, R> Maybe<R> zip(MaybeSource<? extends T1> source1, MaybeSource<? extends T2> source2, MaybeSource<? extends T3> source3, MaybeSource<? extends T4> source4, MaybeSource<? extends T5> source5, MaybeSource<? extends T6> source6, MaybeSource<? extends T7> source7, Function7<? super T1, ? super T2, ? super T3, ? super T4, ? super T5, ? super T6, ? super T7, ? extends R> zipper) {
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
    public static <T1, T2, T3, T4, T5, T6, T7, T8, R> Maybe<R> zip(MaybeSource<? extends T1> source1, MaybeSource<? extends T2> source2, MaybeSource<? extends T3> source3, MaybeSource<? extends T4> source4, MaybeSource<? extends T5> source5, MaybeSource<? extends T6> source6, MaybeSource<? extends T7> source7, MaybeSource<? extends T8> source8, Function8<? super T1, ? super T2, ? super T3, ? super T4, ? super T5, ? super T6, ? super T7, ? super T8, ? extends R> zipper) {
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
    public static <T1, T2, T3, T4, T5, T6, T7, T8, T9, R> Maybe<R> zip(MaybeSource<? extends T1> source1, MaybeSource<? extends T2> source2, MaybeSource<? extends T3> source3, MaybeSource<? extends T4> source4, MaybeSource<? extends T5> source5, MaybeSource<? extends T6> source6, MaybeSource<? extends T7> source7, MaybeSource<? extends T8> source8, MaybeSource<? extends T9> source9, Function9<? super T1, ? super T2, ? super T3, ? super T4, ? super T5, ? super T6, ? super T7, ? super T8, ? super T9, ? extends R> zipper) {
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
    public static <T, R> Maybe<R> zipArray(Function<? super Object[], ? extends R> zipper, MaybeSource<? extends T>... sources) {
        ObjectHelper.requireNonNull(sources, "sources is null");
        if (sources.length == 0) {
            return empty();
        }
        ObjectHelper.requireNonNull(zipper, "zipper is null");
        return RxJavaPlugins.onAssembly(new MaybeZipArray(sources, zipper));
    }

    @SchedulerSupport("none")
    public final Maybe<T> ambWith(MaybeSource<? extends T> other) {
        ObjectHelper.requireNonNull(other, "other is null");
        return ambArray(this, other);
    }

    @SchedulerSupport("none")
    public final T blockingGet() {
        BlockingMultiObserver<T> observer = new BlockingMultiObserver<>();
        subscribe(observer);
        return observer.blockingGet();
    }

    @SchedulerSupport("none")
    public final T blockingGet(T defaultValue) {
        ObjectHelper.requireNonNull(defaultValue, "defaultValue is null");
        BlockingMultiObserver<T> observer = new BlockingMultiObserver<>();
        subscribe(observer);
        return observer.blockingGet(defaultValue);
    }

    @SchedulerSupport("none")
    public final Maybe<T> cache() {
        return RxJavaPlugins.onAssembly(new MaybeCache(this));
    }

    @SchedulerSupport("none")
    public final <U> Maybe<U> cast(Class<? extends U> clazz) {
        ObjectHelper.requireNonNull(clazz, "clazz is null");
        return map(Functions.castFunction(clazz));
    }

    @SchedulerSupport("none")
    public final <R> Maybe<R> compose(MaybeTransformer<T, R> transformer) {
        return wrap(transformer.apply(this));
    }

    @SchedulerSupport("none")
    public final <R> Maybe<R> concatMap(Function<? super T, ? extends MaybeSource<? extends R>> mapper) {
        ObjectHelper.requireNonNull(mapper, "mapper is null");
        return RxJavaPlugins.onAssembly(new MaybeFlatten(this, mapper));
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public final Flowable<T> concatWith(MaybeSource<? extends T> other) {
        ObjectHelper.requireNonNull(other, "other is null");
        return concat(this, other);
    }

    @SchedulerSupport("none")
    public final Single<Boolean> contains(Object item) {
        ObjectHelper.requireNonNull(item, "item is null");
        return RxJavaPlugins.onAssembly(new MaybeContains(this, item));
    }

    @SchedulerSupport("none")
    public final Single<Long> count() {
        return RxJavaPlugins.onAssembly(new MaybeCount(this));
    }

    @SchedulerSupport("none")
    public final Maybe<T> defaultIfEmpty(T defaultItem) {
        ObjectHelper.requireNonNull(defaultItem, "item is null");
        return switchIfEmpty(just(defaultItem));
    }

    @SchedulerSupport("io.reactivex:computation")
    public final Maybe<T> delay(long delay, TimeUnit unit) {
        return delay(delay, unit, Schedulers.computation());
    }

    /* JADX DEBUG: Failed to find minimal casts for resolve overloaded methods, cast all args instead
     method: ClspMth{java.lang.Math.max(long, long):long}
     arg types: [int, long]
     candidates:
      ClspMth{java.lang.Math.max(double, double):double}
      ClspMth{java.lang.Math.max(int, int):int}
      ClspMth{java.lang.Math.max(float, float):float}
      ClspMth{java.lang.Math.max(long, long):long} */
    @SchedulerSupport("custom")
    public final Maybe<T> delay(long delay, TimeUnit unit, Scheduler scheduler) {
        ObjectHelper.requireNonNull(unit, "unit is null");
        ObjectHelper.requireNonNull(scheduler, "scheduler is null");
        return RxJavaPlugins.onAssembly(new MaybeDelay(this, Math.max(0L, delay), unit, scheduler));
    }

    @BackpressureSupport(BackpressureKind.UNBOUNDED_IN)
    @SchedulerSupport("none")
    public final <U, V> Maybe<T> delay(Publisher<U> delayIndicator) {
        return RxJavaPlugins.onAssembly(new MaybeDelayOtherPublisher(this, delayIndicator));
    }

    @BackpressureSupport(BackpressureKind.UNBOUNDED_IN)
    @SchedulerSupport("none")
    public final <U> Maybe<T> delaySubscription(Publisher<U> subscriptionIndicator) {
        ObjectHelper.requireNonNull(subscriptionIndicator, "subscriptionIndicator is null");
        return RxJavaPlugins.onAssembly(new MaybeDelaySubscriptionOtherPublisher(this, subscriptionIndicator));
    }

    @SchedulerSupport("io.reactivex:computation")
    public final Maybe<T> delaySubscription(long delay, TimeUnit unit) {
        return delaySubscription(delay, unit, Schedulers.computation());
    }

    @SchedulerSupport("custom")
    public final Maybe<T> delaySubscription(long delay, TimeUnit unit, Scheduler scheduler) {
        return delaySubscription(Flowable.timer(delay, unit, scheduler));
    }

    @SchedulerSupport("none")
    public final Maybe<T> doAfterTerminate(Action onAfterTerminate) {
        return RxJavaPlugins.onAssembly(new MaybePeek(this, Functions.emptyConsumer(), Functions.emptyConsumer(), Functions.emptyConsumer(), Functions.EMPTY_ACTION, (Action) ObjectHelper.requireNonNull(onAfterTerminate, "onAfterTerminate is null"), Functions.EMPTY_ACTION));
    }

    @SchedulerSupport("none")
    public final Maybe<T> doOnDispose(Action onDispose) {
        return RxJavaPlugins.onAssembly(new MaybePeek(this, Functions.emptyConsumer(), Functions.emptyConsumer(), Functions.emptyConsumer(), Functions.EMPTY_ACTION, Functions.EMPTY_ACTION, (Action) ObjectHelper.requireNonNull(onDispose, "onDispose is null")));
    }

    @SchedulerSupport("none")
    public final Maybe<T> doOnComplete(Action onComplete) {
        return RxJavaPlugins.onAssembly(new MaybePeek(this, Functions.emptyConsumer(), Functions.emptyConsumer(), Functions.emptyConsumer(), (Action) ObjectHelper.requireNonNull(onComplete, "onComplete is null"), Functions.EMPTY_ACTION, Functions.EMPTY_ACTION));
    }

    @SchedulerSupport("none")
    public final Maybe<T> doOnError(Consumer<? super Throwable> onError) {
        return RxJavaPlugins.onAssembly(new MaybePeek(this, Functions.emptyConsumer(), Functions.emptyConsumer(), (Consumer) ObjectHelper.requireNonNull(onError, "onError is null"), Functions.EMPTY_ACTION, Functions.EMPTY_ACTION, Functions.EMPTY_ACTION));
    }

    @SchedulerSupport("none")
    public final Maybe<T> doOnEvent(BiConsumer<? super T, ? super Throwable> onEvent) {
        ObjectHelper.requireNonNull(onEvent, "onEvent is null");
        return RxJavaPlugins.onAssembly(new MaybeDoOnEvent(this, onEvent));
    }

    @SchedulerSupport("none")
    public final Maybe<T> doOnSubscribe(Consumer<? super Disposable> onSubscribe) {
        return RxJavaPlugins.onAssembly(new MaybePeek(this, (Consumer) ObjectHelper.requireNonNull(onSubscribe, "onSubscribe is null"), Functions.emptyConsumer(), Functions.emptyConsumer(), Functions.EMPTY_ACTION, Functions.EMPTY_ACTION, Functions.EMPTY_ACTION));
    }

    @SchedulerSupport("none")
    public final Maybe<T> doOnSuccess(Consumer<? super T> onSuccess) {
        return RxJavaPlugins.onAssembly(new MaybePeek(this, Functions.emptyConsumer(), (Consumer) ObjectHelper.requireNonNull(onSuccess, "onSubscribe is null"), Functions.emptyConsumer(), Functions.EMPTY_ACTION, Functions.EMPTY_ACTION, Functions.EMPTY_ACTION));
    }

    @SchedulerSupport("none")
    public final Maybe<T> filter(Predicate<? super T> predicate) {
        ObjectHelper.requireNonNull(predicate, "predicate is null");
        return RxJavaPlugins.onAssembly(new MaybeFilter(this, predicate));
    }

    @SchedulerSupport("none")
    public final <R> Maybe<R> flatMap(Function<? super T, ? extends MaybeSource<? extends R>> mapper) {
        ObjectHelper.requireNonNull(mapper, "mapper is null");
        return RxJavaPlugins.onAssembly(new MaybeFlatten(this, mapper));
    }

    @SchedulerSupport("none")
    public final <R> Maybe<R> flatMap(Function<? super T, ? extends MaybeSource<? extends R>> onSuccessMapper, Function<? super Throwable, ? extends MaybeSource<? extends R>> onErrorMapper, Callable<? extends MaybeSource<? extends R>> onCompleteSupplier) {
        ObjectHelper.requireNonNull(onSuccessMapper, "onSuccessMapper is null");
        ObjectHelper.requireNonNull(onErrorMapper, "onErrorMapper is null");
        ObjectHelper.requireNonNull(onCompleteSupplier, "onCompleteSupplier is null");
        return RxJavaPlugins.onAssembly(new MaybeFlatMapNotification(this, onSuccessMapper, onErrorMapper, onCompleteSupplier));
    }

    @SchedulerSupport("none")
    public final <U, R> Maybe<R> flatMap(Function<? super T, ? extends MaybeSource<? extends U>> mapper, BiFunction<? super T, ? super U, ? extends R> resultSelector) {
        return RxJavaPlugins.onAssembly(new MaybeFlatMapBiSelector(this, mapper, resultSelector));
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public final <U> Flowable<U> flattenAsFlowable(Function<? super T, ? extends Iterable<? extends U>> mapper) {
        return new MaybeFlatMapIterableFlowable(this, mapper);
    }

    @SchedulerSupport("none")
    public final <U> Observable<U> flattenAsObservable(Function<? super T, ? extends Iterable<? extends U>> mapper) {
        return new MaybeFlatMapIterableObservable(this, mapper);
    }

    @SchedulerSupport("none")
    public final <R> Observable<R> flatMapObservable(Function<? super T, ? extends ObservableSource<? extends R>> mapper) {
        return toObservable().flatMap(mapper);
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public final <R> Flowable<R> flatMapPublisher(Function<? super T, ? extends Publisher<? extends R>> mapper) {
        return toFlowable().flatMap(mapper);
    }

    @SchedulerSupport("none")
    public final <R> Single<R> flatMapSingle(Function<? super T, ? extends SingleSource<? extends R>> mapper) {
        ObjectHelper.requireNonNull(mapper, "mapper is null");
        return RxJavaPlugins.onAssembly(new MaybeFlatMapSingle(this, mapper));
    }

    @SchedulerSupport("none")
    public final Completable flatMapCompletable(Function<? super T, ? extends Completable> mapper) {
        ObjectHelper.requireNonNull(mapper, "mapper is null");
        return RxJavaPlugins.onAssembly(new MaybeFlatMapCompletable(this, mapper));
    }

    @SchedulerSupport("none")
    public final Maybe<T> hide() {
        return RxJavaPlugins.onAssembly(new MaybeHide(this));
    }

    @SchedulerSupport("none")
    public final Completable ignoreElement() {
        return RxJavaPlugins.onAssembly(new MaybeIgnoreElementCompletable(this));
    }

    @SchedulerSupport("none")
    public final Single<Boolean> isEmpty() {
        return RxJavaPlugins.onAssembly(new MaybeIsEmptySingle(this));
    }

    @SchedulerSupport("none")
    public final <R> Maybe<R> lift(MaybeOperator<? extends R, ? super T> lift) {
        ObjectHelper.requireNonNull(lift, "onLift is null");
        return RxJavaPlugins.onAssembly(new MaybeLift(this, lift));
    }

    @SchedulerSupport("none")
    public final <R> Maybe<R> map(Function<? super T, ? extends R> mapper) {
        ObjectHelper.requireNonNull(mapper, "mapper is null");
        return RxJavaPlugins.onAssembly(new MaybeMap(this, mapper));
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public final Flowable<T> mergeWith(MaybeSource<? extends T> other) {
        ObjectHelper.requireNonNull(other, "other is null");
        return merge(this, other);
    }

    @SchedulerSupport("custom")
    public final Maybe<T> observeOn(Scheduler scheduler) {
        ObjectHelper.requireNonNull(scheduler, "scheduler is null");
        return RxJavaPlugins.onAssembly(new MaybeObserveOn(this, scheduler));
    }

    @SchedulerSupport("none")
    public final <U> Maybe<U> ofType(Class<U> clazz) {
        ObjectHelper.requireNonNull(clazz, "clazz is null");
        return filter(Functions.isInstanceOf(clazz)).cast(clazz);
    }

    @SchedulerSupport("none")
    public final <R> R to(Function<? super Maybe<T>, R> convert) {
        try {
            return convert.apply(this);
        } catch (Throwable ex) {
            Exceptions.throwIfFatal(ex);
            throw ExceptionHelper.wrapOrThrow(ex);
        }
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public final Flowable<T> toFlowable() {
        if (this instanceof FuseToFlowable) {
            return ((FuseToFlowable) this).fuseToFlowable();
        }
        return RxJavaPlugins.onAssembly(new MaybeToFlowable(this));
    }

    @SchedulerSupport("none")
    public final Observable<T> toObservable() {
        if (this instanceof FuseToObservable) {
            return ((FuseToObservable) this).fuseToObservable();
        }
        return RxJavaPlugins.onAssembly(new MaybeToObservable(this));
    }

    @SchedulerSupport("none")
    public final Single<T> toSingle(T defaultValue) {
        ObjectHelper.requireNonNull(defaultValue, "defaultValue is null");
        return RxJavaPlugins.onAssembly(new MaybeToSingle(this, defaultValue));
    }

    @SchedulerSupport("none")
    public final Single<T> toSingle() {
        return RxJavaPlugins.onAssembly(new MaybeToSingle(this, null));
    }

    @SchedulerSupport("none")
    public final Maybe<T> onErrorComplete() {
        return onErrorComplete(Functions.alwaysTrue());
    }

    @SchedulerSupport("none")
    public final Maybe<T> onErrorComplete(Predicate<? super Throwable> predicate) {
        ObjectHelper.requireNonNull(predicate, "predicate is null");
        return RxJavaPlugins.onAssembly(new MaybeOnErrorComplete(this, predicate));
    }

    @SchedulerSupport("none")
    public final Maybe<T> onErrorResumeNext(MaybeSource<? extends T> next) {
        ObjectHelper.requireNonNull(next, "next is null");
        return onErrorResumeNext(Functions.justFunction(next));
    }

    @SchedulerSupport("none")
    public final Maybe<T> onErrorResumeNext(Function<? super Throwable, ? extends MaybeSource<? extends T>> resumeFunction) {
        ObjectHelper.requireNonNull(resumeFunction, "resumeFunction is null");
        return RxJavaPlugins.onAssembly(new MaybeOnErrorNext(this, resumeFunction, true));
    }

    @SchedulerSupport("none")
    public final Maybe<T> onErrorReturn(Function<? super Throwable, ? extends T> valueSupplier) {
        ObjectHelper.requireNonNull(valueSupplier, "valueSupplier is null");
        return RxJavaPlugins.onAssembly(new MaybeOnErrorReturn(this, valueSupplier));
    }

    @SchedulerSupport("none")
    public final Maybe<T> onErrorReturnItem(T item) {
        ObjectHelper.requireNonNull(item, "item is null");
        return onErrorReturn(Functions.justFunction(item));
    }

    @SchedulerSupport("none")
    public final Maybe<T> onExceptionResumeNext(MaybeSource<? extends T> next) {
        ObjectHelper.requireNonNull(next, "next is null");
        return RxJavaPlugins.onAssembly(new MaybeOnErrorNext(this, Functions.justFunction(next), false));
    }

    @SchedulerSupport("none")
    public final Maybe<T> onTerminateDetach() {
        return RxJavaPlugins.onAssembly(new MaybeDetach(this));
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public final Flowable<T> repeat() {
        return repeat(LongCompanionObject.MAX_VALUE);
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public final Flowable<T> repeat(long times) {
        return toFlowable().repeat(times);
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public final Flowable<T> repeatUntil(BooleanSupplier stop) {
        return toFlowable().repeatUntil(stop);
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public final Flowable<T> repeatWhen(Function<? super Flowable<Object>, ? extends Publisher<?>> handler) {
        return toFlowable().repeatWhen(handler);
    }

    @SchedulerSupport("none")
    public final Maybe<T> retry() {
        return retry(LongCompanionObject.MAX_VALUE, Functions.alwaysTrue());
    }

    @SchedulerSupport("none")
    public final Maybe<T> retry(BiPredicate<? super Integer, ? super Throwable> predicate) {
        return toFlowable().retry(predicate).singleElement();
    }

    @SchedulerSupport("none")
    public final Maybe<T> retry(long count) {
        return retry(count, Functions.alwaysTrue());
    }

    @SchedulerSupport("none")
    public final Maybe<T> retry(long times, Predicate<? super Throwable> predicate) {
        return toFlowable().retry(times, predicate).singleElement();
    }

    @SchedulerSupport("none")
    public final Maybe<T> retry(Predicate<? super Throwable> predicate) {
        return retry(LongCompanionObject.MAX_VALUE, predicate);
    }

    @SchedulerSupport("none")
    public final Maybe<T> retryUntil(BooleanSupplier stop) {
        ObjectHelper.requireNonNull(stop, "stop is null");
        return retry(LongCompanionObject.MAX_VALUE, Functions.predicateReverseFor(stop));
    }

    @SchedulerSupport("none")
    public final Maybe<T> retryWhen(Function<? super Flowable<Throwable>, ? extends Publisher<?>> handler) {
        return toFlowable().retryWhen(handler).singleElement();
    }

    @SchedulerSupport("none")
    public final Disposable subscribe() {
        return subscribe(Functions.emptyConsumer(), Functions.ERROR_CONSUMER, Functions.EMPTY_ACTION);
    }

    @SchedulerSupport("none")
    public final Disposable subscribe(Consumer<? super T> onSuccess) {
        return subscribe(onSuccess, Functions.ERROR_CONSUMER, Functions.EMPTY_ACTION);
    }

    @SchedulerSupport("none")
    public final Disposable subscribe(Consumer<? super T> onSuccess, Consumer<? super Throwable> onError) {
        return subscribe(onSuccess, onError, Functions.EMPTY_ACTION);
    }

    @SchedulerSupport("none")
    public final Disposable subscribe(Consumer<? super T> onSuccess, Consumer<? super Throwable> onError, Action onComplete) {
        return (Disposable) subscribeWith(new MaybeCallbackObserver(onSuccess, onError, onComplete));
    }

    @SchedulerSupport("none")
    public final void subscribe(MaybeObserver<? super T> observer) {
        ObjectHelper.requireNonNull(observer, "observer is null");
        MaybeObserver<? super T> observer2 = RxJavaPlugins.onSubscribe(this, observer);
        ObjectHelper.requireNonNull(observer2, "observer returned by the RxJavaPlugins hook is null");
        try {
            subscribeActual(observer2);
        } catch (NullPointerException ex) {
            throw ex;
        } catch (Throwable ex2) {
            Exceptions.throwIfFatal(ex2);
            NullPointerException npe = new NullPointerException("subscribeActual failed");
            npe.initCause(ex2);
            throw npe;
        }
    }

    @SchedulerSupport("custom")
    public final Maybe<T> subscribeOn(Scheduler scheduler) {
        ObjectHelper.requireNonNull(scheduler, "scheduler is null");
        return RxJavaPlugins.onAssembly(new MaybeSubscribeOn(this, scheduler));
    }

    @SchedulerSupport("none")
    public final <E extends MaybeObserver<? super T>> E subscribeWith(E observer) {
        subscribe((MaybeObserver) observer);
        return observer;
    }

    @SchedulerSupport("none")
    public final Maybe<T> switchIfEmpty(MaybeSource<? extends T> other) {
        ObjectHelper.requireNonNull(other, "other is null");
        return RxJavaPlugins.onAssembly(new MaybeSwitchIfEmpty(this, other));
    }

    @SchedulerSupport("none")
    public final <U> Maybe<T> takeUntil(MaybeSource<U> other) {
        ObjectHelper.requireNonNull(other, "other is null");
        return RxJavaPlugins.onAssembly(new MaybeTakeUntilMaybe(this, other));
    }

    @BackpressureSupport(BackpressureKind.UNBOUNDED_IN)
    @SchedulerSupport("none")
    public final <U> Maybe<T> takeUntil(Publisher<U> other) {
        ObjectHelper.requireNonNull(other, "other is null");
        return RxJavaPlugins.onAssembly(new MaybeTakeUntilPublisher(this, other));
    }

    @SchedulerSupport("io.reactivex:computation")
    public final Maybe<T> timeout(long timeout, TimeUnit timeUnit) {
        return timeout(timeout, timeUnit, Schedulers.computation());
    }

    @SchedulerSupport("io.reactivex:computation")
    public final Maybe<T> timeout(long timeout, TimeUnit timeUnit, MaybeSource<? extends T> fallback) {
        ObjectHelper.requireNonNull(fallback, "other is null");
        return timeout(timeout, timeUnit, Schedulers.computation(), fallback);
    }

    @SchedulerSupport("custom")
    public final Maybe<T> timeout(long timeout, TimeUnit timeUnit, Scheduler scheduler, MaybeSource<? extends T> fallback) {
        ObjectHelper.requireNonNull(fallback, "fallback is null");
        return timeout(timer(timeout, timeUnit, scheduler), fallback);
    }

    @SchedulerSupport("custom")
    public final Maybe<T> timeout(long timeout, TimeUnit timeUnit, Scheduler scheduler) {
        return timeout(timer(timeout, timeUnit, scheduler));
    }

    @SchedulerSupport("none")
    public final <U> Maybe<T> timeout(MaybeSource<U> timeoutIndicator) {
        ObjectHelper.requireNonNull(timeoutIndicator, "timeoutIndicator is null");
        return RxJavaPlugins.onAssembly(new MaybeTimeoutMaybe(this, timeoutIndicator, null));
    }

    @SchedulerSupport("none")
    public final <U> Maybe<T> timeout(MaybeSource<U> timeoutIndicator, MaybeSource<? extends T> fallback) {
        ObjectHelper.requireNonNull(timeoutIndicator, "timeoutIndicator is null");
        ObjectHelper.requireNonNull(fallback, "fallback is null");
        return RxJavaPlugins.onAssembly(new MaybeTimeoutMaybe(this, timeoutIndicator, fallback));
    }

    @BackpressureSupport(BackpressureKind.UNBOUNDED_IN)
    @SchedulerSupport("none")
    public final <U> Maybe<T> timeout(Publisher<U> timeoutIndicator) {
        ObjectHelper.requireNonNull(timeoutIndicator, "timeoutIndicator is null");
        return RxJavaPlugins.onAssembly(new MaybeTimeoutPublisher(this, timeoutIndicator, null));
    }

    @BackpressureSupport(BackpressureKind.UNBOUNDED_IN)
    @SchedulerSupport("none")
    public final <U> Maybe<T> timeout(Publisher<U> timeoutIndicator, MaybeSource<? extends T> fallback) {
        ObjectHelper.requireNonNull(timeoutIndicator, "timeoutIndicator is null");
        ObjectHelper.requireNonNull(fallback, "fallback is null");
        return RxJavaPlugins.onAssembly(new MaybeTimeoutPublisher(this, timeoutIndicator, fallback));
    }

    @SchedulerSupport("custom")
    public final Maybe<T> unsubscribeOn(Scheduler scheduler) {
        ObjectHelper.requireNonNull(scheduler, "scheduler is null");
        return RxJavaPlugins.onAssembly(new MaybeUnsubscribeOn(this, scheduler));
    }

    @SchedulerSupport("none")
    public final <U, R> Maybe<R> zipWith(MaybeSource<? extends U> other, BiFunction<? super T, ? super U, ? extends R> zipper) {
        ObjectHelper.requireNonNull(other, "other is null");
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
}
