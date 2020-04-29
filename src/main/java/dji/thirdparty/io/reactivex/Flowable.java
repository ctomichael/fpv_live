package dji.thirdparty.io.reactivex;

import dji.thirdparty.io.reactivex.annotations.BackpressureKind;
import dji.thirdparty.io.reactivex.annotations.BackpressureSupport;
import dji.thirdparty.io.reactivex.annotations.SchedulerSupport;
import dji.thirdparty.io.reactivex.disposables.Disposable;
import dji.thirdparty.io.reactivex.exceptions.Exceptions;
import dji.thirdparty.io.reactivex.flowables.ConnectableFlowable;
import dji.thirdparty.io.reactivex.flowables.GroupedFlowable;
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
import dji.thirdparty.io.reactivex.functions.LongConsumer;
import dji.thirdparty.io.reactivex.functions.Predicate;
import dji.thirdparty.io.reactivex.internal.functions.Functions;
import dji.thirdparty.io.reactivex.internal.functions.ObjectHelper;
import dji.thirdparty.io.reactivex.internal.operators.flowable.BlockingFlowableIterable;
import dji.thirdparty.io.reactivex.internal.operators.flowable.BlockingFlowableLatest;
import dji.thirdparty.io.reactivex.internal.operators.flowable.BlockingFlowableMostRecent;
import dji.thirdparty.io.reactivex.internal.operators.flowable.BlockingFlowableNext;
import dji.thirdparty.io.reactivex.internal.operators.flowable.FlowableAllSingle;
import dji.thirdparty.io.reactivex.internal.operators.flowable.FlowableAmb;
import dji.thirdparty.io.reactivex.internal.operators.flowable.FlowableAnySingle;
import dji.thirdparty.io.reactivex.internal.operators.flowable.FlowableBlockingSubscribe;
import dji.thirdparty.io.reactivex.internal.operators.flowable.FlowableBuffer;
import dji.thirdparty.io.reactivex.internal.operators.flowable.FlowableBufferBoundary;
import dji.thirdparty.io.reactivex.internal.operators.flowable.FlowableBufferBoundarySupplier;
import dji.thirdparty.io.reactivex.internal.operators.flowable.FlowableBufferExactBoundary;
import dji.thirdparty.io.reactivex.internal.operators.flowable.FlowableBufferTimed;
import dji.thirdparty.io.reactivex.internal.operators.flowable.FlowableCache;
import dji.thirdparty.io.reactivex.internal.operators.flowable.FlowableCollectSingle;
import dji.thirdparty.io.reactivex.internal.operators.flowable.FlowableCombineLatest;
import dji.thirdparty.io.reactivex.internal.operators.flowable.FlowableConcatArray;
import dji.thirdparty.io.reactivex.internal.operators.flowable.FlowableConcatMapEager;
import dji.thirdparty.io.reactivex.internal.operators.flowable.FlowableCountSingle;
import dji.thirdparty.io.reactivex.internal.operators.flowable.FlowableCreate;
import dji.thirdparty.io.reactivex.internal.operators.flowable.FlowableDebounce;
import dji.thirdparty.io.reactivex.internal.operators.flowable.FlowableDebounceTimed;
import dji.thirdparty.io.reactivex.internal.operators.flowable.FlowableDefer;
import dji.thirdparty.io.reactivex.internal.operators.flowable.FlowableDelay;
import dji.thirdparty.io.reactivex.internal.operators.flowable.FlowableDelaySubscriptionOther;
import dji.thirdparty.io.reactivex.internal.operators.flowable.FlowableDematerialize;
import dji.thirdparty.io.reactivex.internal.operators.flowable.FlowableDetach;
import dji.thirdparty.io.reactivex.internal.operators.flowable.FlowableDistinct;
import dji.thirdparty.io.reactivex.internal.operators.flowable.FlowableDistinctUntilChanged;
import dji.thirdparty.io.reactivex.internal.operators.flowable.FlowableDoOnEach;
import dji.thirdparty.io.reactivex.internal.operators.flowable.FlowableDoOnLifecycle;
import dji.thirdparty.io.reactivex.internal.operators.flowable.FlowableElementAtMaybe;
import dji.thirdparty.io.reactivex.internal.operators.flowable.FlowableElementAtSingle;
import dji.thirdparty.io.reactivex.internal.operators.flowable.FlowableEmpty;
import dji.thirdparty.io.reactivex.internal.operators.flowable.FlowableError;
import dji.thirdparty.io.reactivex.internal.operators.flowable.FlowableFilter;
import dji.thirdparty.io.reactivex.internal.operators.flowable.FlowableFlatMapCompletableCompletable;
import dji.thirdparty.io.reactivex.internal.operators.flowable.FlowableFlatMapMaybe;
import dji.thirdparty.io.reactivex.internal.operators.flowable.FlowableFlatMapSingle;
import dji.thirdparty.io.reactivex.internal.operators.flowable.FlowableFlattenIterable;
import dji.thirdparty.io.reactivex.internal.operators.flowable.FlowableFromArray;
import dji.thirdparty.io.reactivex.internal.operators.flowable.FlowableFromCallable;
import dji.thirdparty.io.reactivex.internal.operators.flowable.FlowableFromFuture;
import dji.thirdparty.io.reactivex.internal.operators.flowable.FlowableFromIterable;
import dji.thirdparty.io.reactivex.internal.operators.flowable.FlowableFromPublisher;
import dji.thirdparty.io.reactivex.internal.operators.flowable.FlowableGenerate;
import dji.thirdparty.io.reactivex.internal.operators.flowable.FlowableGroupBy;
import dji.thirdparty.io.reactivex.internal.operators.flowable.FlowableGroupJoin;
import dji.thirdparty.io.reactivex.internal.operators.flowable.FlowableHide;
import dji.thirdparty.io.reactivex.internal.operators.flowable.FlowableIgnoreElements;
import dji.thirdparty.io.reactivex.internal.operators.flowable.FlowableIgnoreElementsCompletable;
import dji.thirdparty.io.reactivex.internal.operators.flowable.FlowableInternalHelper;
import dji.thirdparty.io.reactivex.internal.operators.flowable.FlowableInterval;
import dji.thirdparty.io.reactivex.internal.operators.flowable.FlowableIntervalRange;
import dji.thirdparty.io.reactivex.internal.operators.flowable.FlowableJoin;
import dji.thirdparty.io.reactivex.internal.operators.flowable.FlowableJust;
import dji.thirdparty.io.reactivex.internal.operators.flowable.FlowableLastMaybe;
import dji.thirdparty.io.reactivex.internal.operators.flowable.FlowableLastSingle;
import dji.thirdparty.io.reactivex.internal.operators.flowable.FlowableLift;
import dji.thirdparty.io.reactivex.internal.operators.flowable.FlowableMap;
import dji.thirdparty.io.reactivex.internal.operators.flowable.FlowableMapNotification;
import dji.thirdparty.io.reactivex.internal.operators.flowable.FlowableMaterialize;
import dji.thirdparty.io.reactivex.internal.operators.flowable.FlowableNever;
import dji.thirdparty.io.reactivex.internal.operators.flowable.FlowableObserveOn;
import dji.thirdparty.io.reactivex.internal.operators.flowable.FlowableOnBackpressureBuffer;
import dji.thirdparty.io.reactivex.internal.operators.flowable.FlowableOnBackpressureBufferStrategy;
import dji.thirdparty.io.reactivex.internal.operators.flowable.FlowableOnBackpressureDrop;
import dji.thirdparty.io.reactivex.internal.operators.flowable.FlowableOnBackpressureLatest;
import dji.thirdparty.io.reactivex.internal.operators.flowable.FlowableOnErrorNext;
import dji.thirdparty.io.reactivex.internal.operators.flowable.FlowableOnErrorReturn;
import dji.thirdparty.io.reactivex.internal.operators.flowable.FlowablePublish;
import dji.thirdparty.io.reactivex.internal.operators.flowable.FlowablePublishMulticast;
import dji.thirdparty.io.reactivex.internal.operators.flowable.FlowableRange;
import dji.thirdparty.io.reactivex.internal.operators.flowable.FlowableRangeLong;
import dji.thirdparty.io.reactivex.internal.operators.flowable.FlowableReduceMaybe;
import dji.thirdparty.io.reactivex.internal.operators.flowable.FlowableRepeat;
import dji.thirdparty.io.reactivex.internal.operators.flowable.FlowableRepeatUntil;
import dji.thirdparty.io.reactivex.internal.operators.flowable.FlowableRepeatWhen;
import dji.thirdparty.io.reactivex.internal.operators.flowable.FlowableReplay;
import dji.thirdparty.io.reactivex.internal.operators.flowable.FlowableRetryBiPredicate;
import dji.thirdparty.io.reactivex.internal.operators.flowable.FlowableRetryPredicate;
import dji.thirdparty.io.reactivex.internal.operators.flowable.FlowableRetryWhen;
import dji.thirdparty.io.reactivex.internal.operators.flowable.FlowableSamplePublisher;
import dji.thirdparty.io.reactivex.internal.operators.flowable.FlowableSampleTimed;
import dji.thirdparty.io.reactivex.internal.operators.flowable.FlowableScan;
import dji.thirdparty.io.reactivex.internal.operators.flowable.FlowableScanSeed;
import dji.thirdparty.io.reactivex.internal.operators.flowable.FlowableSequenceEqualSingle;
import dji.thirdparty.io.reactivex.internal.operators.flowable.FlowableSerialized;
import dji.thirdparty.io.reactivex.internal.operators.flowable.FlowableSingleMaybe;
import dji.thirdparty.io.reactivex.internal.operators.flowable.FlowableSingleSingle;
import dji.thirdparty.io.reactivex.internal.operators.flowable.FlowableSkip;
import dji.thirdparty.io.reactivex.internal.operators.flowable.FlowableSkipLast;
import dji.thirdparty.io.reactivex.internal.operators.flowable.FlowableSkipLastTimed;
import dji.thirdparty.io.reactivex.internal.operators.flowable.FlowableSkipUntil;
import dji.thirdparty.io.reactivex.internal.operators.flowable.FlowableSkipWhile;
import dji.thirdparty.io.reactivex.internal.operators.flowable.FlowableSubscribeOn;
import dji.thirdparty.io.reactivex.internal.operators.flowable.FlowableSwitchIfEmpty;
import dji.thirdparty.io.reactivex.internal.operators.flowable.FlowableTake;
import dji.thirdparty.io.reactivex.internal.operators.flowable.FlowableTakeLast;
import dji.thirdparty.io.reactivex.internal.operators.flowable.FlowableTakeLastOne;
import dji.thirdparty.io.reactivex.internal.operators.flowable.FlowableTakeLastTimed;
import dji.thirdparty.io.reactivex.internal.operators.flowable.FlowableTakeUntil;
import dji.thirdparty.io.reactivex.internal.operators.flowable.FlowableTakeUntilPredicate;
import dji.thirdparty.io.reactivex.internal.operators.flowable.FlowableTakeWhile;
import dji.thirdparty.io.reactivex.internal.operators.flowable.FlowableThrottleFirstTimed;
import dji.thirdparty.io.reactivex.internal.operators.flowable.FlowableTimeInterval;
import dji.thirdparty.io.reactivex.internal.operators.flowable.FlowableTimeout;
import dji.thirdparty.io.reactivex.internal.operators.flowable.FlowableTimeoutTimed;
import dji.thirdparty.io.reactivex.internal.operators.flowable.FlowableTimer;
import dji.thirdparty.io.reactivex.internal.operators.flowable.FlowableToListSingle;
import dji.thirdparty.io.reactivex.internal.operators.flowable.FlowableUnsubscribeOn;
import dji.thirdparty.io.reactivex.internal.operators.flowable.FlowableUsing;
import dji.thirdparty.io.reactivex.internal.operators.flowable.FlowableWindow;
import dji.thirdparty.io.reactivex.internal.operators.flowable.FlowableWindowBoundary;
import dji.thirdparty.io.reactivex.internal.operators.flowable.FlowableWindowBoundarySelector;
import dji.thirdparty.io.reactivex.internal.operators.flowable.FlowableWindowBoundarySupplier;
import dji.thirdparty.io.reactivex.internal.operators.flowable.FlowableWindowTimed;
import dji.thirdparty.io.reactivex.internal.operators.flowable.FlowableWithLatestFrom;
import dji.thirdparty.io.reactivex.internal.operators.flowable.FlowableWithLatestFromMany;
import dji.thirdparty.io.reactivex.internal.operators.flowable.FlowableZip;
import dji.thirdparty.io.reactivex.internal.operators.flowable.FlowableZipIterable;
import dji.thirdparty.io.reactivex.internal.operators.observable.ObservableFromPublisher;
import dji.thirdparty.io.reactivex.internal.schedulers.ImmediateThinScheduler;
import dji.thirdparty.io.reactivex.internal.subscribers.BlockingFirstSubscriber;
import dji.thirdparty.io.reactivex.internal.subscribers.BlockingLastSubscriber;
import dji.thirdparty.io.reactivex.internal.subscribers.ForEachWhileSubscriber;
import dji.thirdparty.io.reactivex.internal.subscribers.FutureSubscriber;
import dji.thirdparty.io.reactivex.internal.subscribers.LambdaSubscriber;
import dji.thirdparty.io.reactivex.internal.util.ArrayListSupplier;
import dji.thirdparty.io.reactivex.internal.util.ErrorMode;
import dji.thirdparty.io.reactivex.internal.util.ExceptionHelper;
import dji.thirdparty.io.reactivex.internal.util.HashMapSupplier;
import dji.thirdparty.io.reactivex.plugins.RxJavaPlugins;
import dji.thirdparty.io.reactivex.schedulers.Schedulers;
import dji.thirdparty.io.reactivex.schedulers.Timed;
import dji.thirdparty.io.reactivex.subscribers.SafeSubscriber;
import dji.thirdparty.io.reactivex.subscribers.TestSubscriber;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import kotlin.jvm.internal.LongCompanionObject;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

public abstract class Flowable<T> implements Publisher<T> {
    static final int BUFFER_SIZE = Math.max(16, Integer.getInteger("rx2.buffer-size", 128).intValue());

    /* access modifiers changed from: protected */
    public abstract void subscribeActual(Subscriber<? super T> subscriber);

    @BackpressureSupport(BackpressureKind.PASS_THROUGH)
    @SchedulerSupport("none")
    public static <T> Flowable<T> amb(Iterable<? extends Publisher<? extends T>> sources) {
        ObjectHelper.requireNonNull(sources, "sources is null");
        return RxJavaPlugins.onAssembly(new FlowableAmb(null, sources));
    }

    @BackpressureSupport(BackpressureKind.PASS_THROUGH)
    @SchedulerSupport("none")
    public static <T> Flowable<T> ambArray(Publisher<? extends T>... sources) {
        ObjectHelper.requireNonNull(sources, "sources is null");
        int len = sources.length;
        if (len == 0) {
            return empty();
        }
        if (len == 1) {
            return fromPublisher(sources[0]);
        }
        return RxJavaPlugins.onAssembly(new FlowableAmb(sources, null));
    }

    public static int bufferSize() {
        return BUFFER_SIZE;
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public static <T, R> Flowable<R> combineLatest(Publisher<? extends T>[] sources, Function<? super Object[], ? extends R> combiner) {
        return combineLatest(sources, combiner, bufferSize());
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public static <T, R> Flowable<R> combineLatest(Function<? super Object[], ? extends R> combiner, Publisher<? extends T>... sources) {
        return combineLatest(sources, combiner, bufferSize());
    }

    /* JADX DEBUG: Failed to find minimal casts for resolve overloaded methods, cast all args instead
     method: dji.thirdparty.io.reactivex.internal.operators.flowable.FlowableCombineLatest.<init>(org.reactivestreams.Publisher[], dji.thirdparty.io.reactivex.functions.Function, int, boolean):void
     arg types: [org.reactivestreams.Publisher<? extends T>[], dji.thirdparty.io.reactivex.functions.Function<? super java.lang.Object[], ? extends R>, int, int]
     candidates:
      dji.thirdparty.io.reactivex.internal.operators.flowable.FlowableCombineLatest.<init>(java.lang.Iterable, dji.thirdparty.io.reactivex.functions.Function, int, boolean):void
      dji.thirdparty.io.reactivex.internal.operators.flowable.FlowableCombineLatest.<init>(org.reactivestreams.Publisher[], dji.thirdparty.io.reactivex.functions.Function, int, boolean):void */
    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public static <T, R> Flowable<R> combineLatest(Publisher<? extends T>[] sources, Function<? super Object[], ? extends R> combiner, int bufferSize) {
        ObjectHelper.requireNonNull(sources, "sources is null");
        if (sources.length == 0) {
            return empty();
        }
        ObjectHelper.requireNonNull(combiner, "combiner is null");
        ObjectHelper.verifyPositive(bufferSize, "bufferSize");
        return RxJavaPlugins.onAssembly(new FlowableCombineLatest((Publisher[]) sources, (Function) combiner, bufferSize, false));
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public static <T, R> Flowable<R> combineLatest(Iterable<? extends Publisher<? extends T>> sources, Function<? super Object[], ? extends R> combiner) {
        return combineLatest(sources, combiner, bufferSize());
    }

    /* JADX DEBUG: Failed to find minimal casts for resolve overloaded methods, cast all args instead
     method: dji.thirdparty.io.reactivex.internal.operators.flowable.FlowableCombineLatest.<init>(java.lang.Iterable, dji.thirdparty.io.reactivex.functions.Function, int, boolean):void
     arg types: [java.lang.Iterable<? extends org.reactivestreams.Publisher<? extends T>>, dji.thirdparty.io.reactivex.functions.Function<? super java.lang.Object[], ? extends R>, int, int]
     candidates:
      dji.thirdparty.io.reactivex.internal.operators.flowable.FlowableCombineLatest.<init>(org.reactivestreams.Publisher[], dji.thirdparty.io.reactivex.functions.Function, int, boolean):void
      dji.thirdparty.io.reactivex.internal.operators.flowable.FlowableCombineLatest.<init>(java.lang.Iterable, dji.thirdparty.io.reactivex.functions.Function, int, boolean):void */
    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public static <T, R> Flowable<R> combineLatest(Iterable<? extends Publisher<? extends T>> sources, Function<? super Object[], ? extends R> combiner, int bufferSize) {
        ObjectHelper.requireNonNull(sources, "sources is null");
        ObjectHelper.requireNonNull(combiner, "combiner is null");
        ObjectHelper.verifyPositive(bufferSize, "bufferSize");
        return RxJavaPlugins.onAssembly(new FlowableCombineLatest((Iterable) sources, (Function) combiner, bufferSize, false));
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public static <T, R> Flowable<R> combineLatestDelayError(Publisher<? extends T>[] sources, Function<? super Object[], ? extends R> combiner) {
        return combineLatestDelayError(sources, combiner, bufferSize());
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public static <T, R> Flowable<R> combineLatestDelayError(Function<? super Object[], ? extends R> combiner, Publisher<? extends T>... sources) {
        return combineLatestDelayError(sources, combiner, bufferSize());
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public static <T, R> Flowable<R> combineLatestDelayError(Function<? super Object[], ? extends R> combiner, int bufferSize, Publisher<? extends T>... sources) {
        return combineLatestDelayError(sources, combiner, bufferSize);
    }

    /* JADX DEBUG: Failed to find minimal casts for resolve overloaded methods, cast all args instead
     method: dji.thirdparty.io.reactivex.internal.operators.flowable.FlowableCombineLatest.<init>(org.reactivestreams.Publisher[], dji.thirdparty.io.reactivex.functions.Function, int, boolean):void
     arg types: [org.reactivestreams.Publisher<? extends T>[], dji.thirdparty.io.reactivex.functions.Function<? super java.lang.Object[], ? extends R>, int, int]
     candidates:
      dji.thirdparty.io.reactivex.internal.operators.flowable.FlowableCombineLatest.<init>(java.lang.Iterable, dji.thirdparty.io.reactivex.functions.Function, int, boolean):void
      dji.thirdparty.io.reactivex.internal.operators.flowable.FlowableCombineLatest.<init>(org.reactivestreams.Publisher[], dji.thirdparty.io.reactivex.functions.Function, int, boolean):void */
    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public static <T, R> Flowable<R> combineLatestDelayError(Publisher<? extends T>[] sources, Function<? super Object[], ? extends R> combiner, int bufferSize) {
        ObjectHelper.requireNonNull(sources, "sources is null");
        ObjectHelper.requireNonNull(combiner, "combiner is null");
        ObjectHelper.verifyPositive(bufferSize, "bufferSize");
        if (sources.length == 0) {
            return empty();
        }
        return RxJavaPlugins.onAssembly(new FlowableCombineLatest((Publisher[]) sources, (Function) combiner, bufferSize, true));
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public static <T, R> Flowable<R> combineLatestDelayError(Iterable<? extends Publisher<? extends T>> sources, Function<? super Object[], ? extends R> combiner) {
        return combineLatestDelayError(sources, combiner, bufferSize());
    }

    /* JADX DEBUG: Failed to find minimal casts for resolve overloaded methods, cast all args instead
     method: dji.thirdparty.io.reactivex.internal.operators.flowable.FlowableCombineLatest.<init>(java.lang.Iterable, dji.thirdparty.io.reactivex.functions.Function, int, boolean):void
     arg types: [java.lang.Iterable<? extends org.reactivestreams.Publisher<? extends T>>, dji.thirdparty.io.reactivex.functions.Function<? super java.lang.Object[], ? extends R>, int, int]
     candidates:
      dji.thirdparty.io.reactivex.internal.operators.flowable.FlowableCombineLatest.<init>(org.reactivestreams.Publisher[], dji.thirdparty.io.reactivex.functions.Function, int, boolean):void
      dji.thirdparty.io.reactivex.internal.operators.flowable.FlowableCombineLatest.<init>(java.lang.Iterable, dji.thirdparty.io.reactivex.functions.Function, int, boolean):void */
    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public static <T, R> Flowable<R> combineLatestDelayError(Iterable<? extends Publisher<? extends T>> sources, Function<? super Object[], ? extends R> combiner, int bufferSize) {
        ObjectHelper.requireNonNull(sources, "sources is null");
        ObjectHelper.requireNonNull(combiner, "combiner is null");
        ObjectHelper.verifyPositive(bufferSize, "bufferSize");
        return RxJavaPlugins.onAssembly(new FlowableCombineLatest((Iterable) sources, (Function) combiner, bufferSize, true));
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public static <T1, T2, R> Flowable<R> combineLatest(Publisher<? extends T1> source1, Publisher<? extends T2> source2, BiFunction<? super T1, ? super T2, ? extends R> combiner) {
        ObjectHelper.requireNonNull(source1, "source1 is null");
        ObjectHelper.requireNonNull(source2, "source2 is null");
        return combineLatest(Functions.toFunction(combiner), source1, source2);
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public static <T1, T2, T3, R> Flowable<R> combineLatest(Publisher<? extends T1> source1, Publisher<? extends T2> source2, Publisher<? extends T3> source3, Function3<? super T1, ? super T2, ? super T3, ? extends R> combiner) {
        ObjectHelper.requireNonNull(source1, "source1 is null");
        ObjectHelper.requireNonNull(source2, "source2 is null");
        ObjectHelper.requireNonNull(source3, "source3 is null");
        return combineLatest(Functions.toFunction(combiner), source1, source2, source3);
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public static <T1, T2, T3, T4, R> Flowable<R> combineLatest(Publisher<? extends T1> source1, Publisher<? extends T2> source2, Publisher<? extends T3> source3, Publisher<? extends T4> source4, Function4<? super T1, ? super T2, ? super T3, ? super T4, ? extends R> combiner) {
        ObjectHelper.requireNonNull(source1, "source1 is null");
        ObjectHelper.requireNonNull(source2, "source2 is null");
        ObjectHelper.requireNonNull(source3, "source3 is null");
        ObjectHelper.requireNonNull(source4, "source4 is null");
        return combineLatest(Functions.toFunction(combiner), source1, source2, source3, source4);
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public static <T1, T2, T3, T4, T5, R> Flowable<R> combineLatest(Publisher<? extends T1> source1, Publisher<? extends T2> source2, Publisher<? extends T3> source3, Publisher<? extends T4> source4, Publisher<? extends T5> source5, Function5<? super T1, ? super T2, ? super T3, ? super T4, ? super T5, ? extends R> combiner) {
        ObjectHelper.requireNonNull(source1, "source1 is null");
        ObjectHelper.requireNonNull(source2, "source2 is null");
        ObjectHelper.requireNonNull(source3, "source3 is null");
        ObjectHelper.requireNonNull(source4, "source4 is null");
        ObjectHelper.requireNonNull(source5, "source5 is null");
        return combineLatest(Functions.toFunction(combiner), source1, source2, source3, source4, source5);
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public static <T1, T2, T3, T4, T5, T6, R> Flowable<R> combineLatest(Publisher<? extends T1> source1, Publisher<? extends T2> source2, Publisher<? extends T3> source3, Publisher<? extends T4> source4, Publisher<? extends T5> source5, Publisher<? extends T6> source6, Function6<? super T1, ? super T2, ? super T3, ? super T4, ? super T5, ? super T6, ? extends R> combiner) {
        ObjectHelper.requireNonNull(source1, "source1 is null");
        ObjectHelper.requireNonNull(source2, "source2 is null");
        ObjectHelper.requireNonNull(source3, "source3 is null");
        ObjectHelper.requireNonNull(source4, "source4 is null");
        ObjectHelper.requireNonNull(source5, "source5 is null");
        ObjectHelper.requireNonNull(source6, "source6 is null");
        return combineLatest(Functions.toFunction(combiner), source1, source2, source3, source4, source5, source6);
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public static <T1, T2, T3, T4, T5, T6, T7, R> Flowable<R> combineLatest(Publisher<? extends T1> source1, Publisher<? extends T2> source2, Publisher<? extends T3> source3, Publisher<? extends T4> source4, Publisher<? extends T5> source5, Publisher<? extends T6> source6, Publisher<? extends T7> source7, Function7<? super T1, ? super T2, ? super T3, ? super T4, ? super T5, ? super T6, ? super T7, ? extends R> combiner) {
        ObjectHelper.requireNonNull(source1, "source1 is null");
        ObjectHelper.requireNonNull(source2, "source2 is null");
        ObjectHelper.requireNonNull(source3, "source3 is null");
        ObjectHelper.requireNonNull(source4, "source4 is null");
        ObjectHelper.requireNonNull(source5, "source5 is null");
        ObjectHelper.requireNonNull(source6, "source6 is null");
        ObjectHelper.requireNonNull(source7, "source7 is null");
        return combineLatest(Functions.toFunction(combiner), source1, source2, source3, source4, source5, source6, source7);
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public static <T1, T2, T3, T4, T5, T6, T7, T8, R> Flowable<R> combineLatest(Publisher<? extends T1> source1, Publisher<? extends T2> source2, Publisher<? extends T3> source3, Publisher<? extends T4> source4, Publisher<? extends T5> source5, Publisher<? extends T6> source6, Publisher<? extends T7> source7, Publisher<? extends T8> source8, Function8<? super T1, ? super T2, ? super T3, ? super T4, ? super T5, ? super T6, ? super T7, ? super T8, ? extends R> combiner) {
        ObjectHelper.requireNonNull(source1, "source1 is null");
        ObjectHelper.requireNonNull(source2, "source2 is null");
        ObjectHelper.requireNonNull(source3, "source3 is null");
        ObjectHelper.requireNonNull(source4, "source4 is null");
        ObjectHelper.requireNonNull(source5, "source5 is null");
        ObjectHelper.requireNonNull(source6, "source6 is null");
        ObjectHelper.requireNonNull(source7, "source7 is null");
        ObjectHelper.requireNonNull(source8, "source8 is null");
        return combineLatest(Functions.toFunction(combiner), source1, source2, source3, source4, source5, source6, source7, source8);
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public static <T1, T2, T3, T4, T5, T6, T7, T8, T9, R> Flowable<R> combineLatest(Publisher<? extends T1> source1, Publisher<? extends T2> source2, Publisher<? extends T3> source3, Publisher<? extends T4> source4, Publisher<? extends T5> source5, Publisher<? extends T6> source6, Publisher<? extends T7> source7, Publisher<? extends T8> source8, Publisher<? extends T9> source9, Function9<? super T1, ? super T2, ? super T3, ? super T4, ? super T5, ? super T6, ? super T7, ? super T8, ? super T9, ? extends R> combiner) {
        ObjectHelper.requireNonNull(source1, "source1 is null");
        ObjectHelper.requireNonNull(source2, "source2 is null");
        ObjectHelper.requireNonNull(source3, "source3 is null");
        ObjectHelper.requireNonNull(source4, "source4 is null");
        ObjectHelper.requireNonNull(source5, "source5 is null");
        ObjectHelper.requireNonNull(source6, "source6 is null");
        ObjectHelper.requireNonNull(source7, "source7 is null");
        ObjectHelper.requireNonNull(source8, "source8 is null");
        ObjectHelper.requireNonNull(source9, "source9 is null");
        return combineLatest(Functions.toFunction(combiner), source1, source2, source3, source4, source5, source6, source7, source8, source9);
    }

    /* JADX WARN: Type inference failed for: r4v0, types: [java.lang.Iterable<? extends org.reactivestreams.Publisher<? extends T>>, java.lang.Object, java.lang.Iterable], assign insn: null */
    /* JADX WARNING: Unknown variable types count: 1 */
    @dji.thirdparty.io.reactivex.annotations.BackpressureSupport(dji.thirdparty.io.reactivex.annotations.BackpressureKind.FULL)
    @dji.thirdparty.io.reactivex.annotations.SchedulerSupport("none")
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static <T> dji.thirdparty.io.reactivex.Flowable<T> concat(java.lang.Iterable<? extends org.reactivestreams.Publisher<? extends T>> r4) {
        /*
            java.lang.String r0 = "sources is null"
            dji.thirdparty.io.reactivex.internal.functions.ObjectHelper.requireNonNull(r4, r0)
            dji.thirdparty.io.reactivex.Flowable r0 = fromIterable(r4)
            dji.thirdparty.io.reactivex.functions.Function r1 = dji.thirdparty.io.reactivex.internal.functions.Functions.identity()
            r2 = 2
            r3 = 0
            dji.thirdparty.io.reactivex.Flowable r0 = r0.concatMapDelayError(r1, r2, r3)
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: dji.thirdparty.io.reactivex.Flowable.concat(java.lang.Iterable):dji.thirdparty.io.reactivex.Flowable");
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public static <T> Flowable<T> concat(Publisher<? extends Publisher<? extends T>> sources) {
        return concat(sources, bufferSize());
    }

    /* JADX WARN: Type inference failed for: r2v0, types: [org.reactivestreams.Publisher<? extends org.reactivestreams.Publisher<? extends T>>, org.reactivestreams.Publisher], assign insn: null */
    /* JADX WARNING: Unknown variable types count: 1 */
    @dji.thirdparty.io.reactivex.annotations.BackpressureSupport(dji.thirdparty.io.reactivex.annotations.BackpressureKind.FULL)
    @dji.thirdparty.io.reactivex.annotations.SchedulerSupport("none")
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static <T> dji.thirdparty.io.reactivex.Flowable<T> concat(org.reactivestreams.Publisher<? extends org.reactivestreams.Publisher<? extends T>> r2, int r3) {
        /*
            dji.thirdparty.io.reactivex.Flowable r0 = fromPublisher(r2)
            dji.thirdparty.io.reactivex.functions.Function r1 = dji.thirdparty.io.reactivex.internal.functions.Functions.identity()
            dji.thirdparty.io.reactivex.Flowable r0 = r0.concatMap(r1, r3)
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: dji.thirdparty.io.reactivex.Flowable.concat(org.reactivestreams.Publisher, int):dji.thirdparty.io.reactivex.Flowable");
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public static <T> Flowable<T> concat(Publisher<? extends T> source1, Publisher<? extends T> source2) {
        return concatArray(source1, source2);
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public static <T> Flowable<T> concat(Publisher<? extends T> source1, Publisher<? extends T> source2, Publisher<? extends T> source3) {
        return concatArray(source1, source2, source3);
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public static <T> Flowable<T> concat(Publisher<? extends T> source1, Publisher<? extends T> source2, Publisher<? extends T> source3, Publisher<? extends T> source4) {
        return concatArray(source1, source2, source3, source4);
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public static <T> Flowable<T> concatArray(Publisher<? extends T>... sources) {
        if (sources.length == 0) {
            return empty();
        }
        if (sources.length == 1) {
            return fromPublisher(sources[0]);
        }
        return RxJavaPlugins.onAssembly(new FlowableConcatArray(sources, false));
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public static <T> Flowable<T> concatArrayDelayError(Publisher<? extends T>... sources) {
        if (sources.length == 0) {
            return empty();
        }
        if (sources.length == 1) {
            return fromPublisher(sources[0]);
        }
        return RxJavaPlugins.onAssembly(new FlowableConcatArray(sources, true));
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public static <T> Flowable<T> concatArrayEager(Publisher<? extends T>... sources) {
        return concatArrayEager(bufferSize(), bufferSize(), sources);
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public static <T> Flowable<T> concatArrayEager(int maxConcurrency, int prefetch, Publisher<? extends T>... sources) {
        return RxJavaPlugins.onAssembly(new FlowableConcatMapEager(new FlowableFromArray(sources), Functions.identity(), maxConcurrency, prefetch, ErrorMode.IMMEDIATE));
    }

    /* JADX WARN: Type inference failed for: r2v0, types: [java.lang.Iterable<? extends org.reactivestreams.Publisher<? extends T>>, java.lang.Object, java.lang.Iterable], assign insn: null */
    /* JADX WARNING: Unknown variable types count: 1 */
    @dji.thirdparty.io.reactivex.annotations.BackpressureSupport(dji.thirdparty.io.reactivex.annotations.BackpressureKind.FULL)
    @dji.thirdparty.io.reactivex.annotations.SchedulerSupport("none")
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static <T> dji.thirdparty.io.reactivex.Flowable<T> concatDelayError(java.lang.Iterable<? extends org.reactivestreams.Publisher<? extends T>> r2) {
        /*
            java.lang.String r0 = "sources is null"
            dji.thirdparty.io.reactivex.internal.functions.ObjectHelper.requireNonNull(r2, r0)
            dji.thirdparty.io.reactivex.Flowable r0 = fromIterable(r2)
            dji.thirdparty.io.reactivex.functions.Function r1 = dji.thirdparty.io.reactivex.internal.functions.Functions.identity()
            dji.thirdparty.io.reactivex.Flowable r0 = r0.concatMapDelayError(r1)
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: dji.thirdparty.io.reactivex.Flowable.concatDelayError(java.lang.Iterable):dji.thirdparty.io.reactivex.Flowable");
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public static <T> Flowable<T> concatDelayError(Publisher<? extends Publisher<? extends T>> sources) {
        return concatDelayError(sources, bufferSize(), true);
    }

    /* JADX WARN: Type inference failed for: r2v0, types: [org.reactivestreams.Publisher<? extends org.reactivestreams.Publisher<? extends T>>, org.reactivestreams.Publisher], assign insn: null */
    /* JADX WARNING: Unknown variable types count: 1 */
    @dji.thirdparty.io.reactivex.annotations.BackpressureSupport(dji.thirdparty.io.reactivex.annotations.BackpressureKind.FULL)
    @dji.thirdparty.io.reactivex.annotations.SchedulerSupport("none")
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static <T> dji.thirdparty.io.reactivex.Flowable<T> concatDelayError(org.reactivestreams.Publisher<? extends org.reactivestreams.Publisher<? extends T>> r2, int r3, boolean r4) {
        /*
            dji.thirdparty.io.reactivex.Flowable r0 = fromPublisher(r2)
            dji.thirdparty.io.reactivex.functions.Function r1 = dji.thirdparty.io.reactivex.internal.functions.Functions.identity()
            dji.thirdparty.io.reactivex.Flowable r0 = r0.concatMapDelayError(r1, r3, r4)
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: dji.thirdparty.io.reactivex.Flowable.concatDelayError(org.reactivestreams.Publisher, int, boolean):dji.thirdparty.io.reactivex.Flowable");
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public static <T> Flowable<T> concatEager(Publisher<? extends Publisher<? extends T>> sources) {
        return concatEager(sources, bufferSize(), bufferSize());
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public static <T> Flowable<T> concatEager(Publisher<? extends Publisher<? extends T>> sources, int maxConcurrency, int prefetch) {
        return RxJavaPlugins.onAssembly(new FlowableConcatMapEager(sources, Functions.identity(), maxConcurrency, prefetch, ErrorMode.IMMEDIATE));
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public static <T> Flowable<T> concatEager(Iterable<? extends Publisher<? extends T>> sources) {
        return concatEager(sources, bufferSize(), bufferSize());
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public static <T> Flowable<T> concatEager(Iterable<? extends Publisher<? extends T>> sources, int maxConcurrency, int prefetch) {
        return RxJavaPlugins.onAssembly(new FlowableConcatMapEager(new FlowableFromIterable(sources), Functions.identity(), maxConcurrency, prefetch, ErrorMode.IMMEDIATE));
    }

    @BackpressureSupport(BackpressureKind.SPECIAL)
    @SchedulerSupport("none")
    public static <T> Flowable<T> create(FlowableOnSubscribe<T> source, BackpressureStrategy mode) {
        ObjectHelper.requireNonNull(source, "source is null");
        ObjectHelper.requireNonNull(mode, "mode is null");
        return RxJavaPlugins.onAssembly(new FlowableCreate(source, mode));
    }

    @BackpressureSupport(BackpressureKind.PASS_THROUGH)
    @SchedulerSupport("none")
    public static <T> Flowable<T> defer(Callable<? extends Publisher<? extends T>> supplier) {
        ObjectHelper.requireNonNull(supplier, "supplier is null");
        return RxJavaPlugins.onAssembly(new FlowableDefer(supplier));
    }

    @BackpressureSupport(BackpressureKind.PASS_THROUGH)
    @SchedulerSupport("none")
    public static <T> Flowable<T> empty() {
        return RxJavaPlugins.onAssembly(FlowableEmpty.INSTANCE);
    }

    @BackpressureSupport(BackpressureKind.PASS_THROUGH)
    @SchedulerSupport("none")
    public static <T> Flowable<T> error(Callable<? extends Throwable> supplier) {
        ObjectHelper.requireNonNull(supplier, "errorSupplier is null");
        return RxJavaPlugins.onAssembly(new FlowableError(supplier));
    }

    @BackpressureSupport(BackpressureKind.PASS_THROUGH)
    @SchedulerSupport("none")
    public static <T> Flowable<T> error(Throwable throwable) {
        ObjectHelper.requireNonNull(throwable, "throwable is null");
        return error(Functions.justCallable(throwable));
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public static <T> Flowable<T> fromArray(T... items) {
        ObjectHelper.requireNonNull(items, "items is null");
        if (items.length == 0) {
            return empty();
        }
        if (items.length == 1) {
            return just(items[0]);
        }
        return RxJavaPlugins.onAssembly(new FlowableFromArray(items));
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public static <T> Flowable<T> fromCallable(Callable<? extends T> supplier) {
        ObjectHelper.requireNonNull(supplier, "supplier is null");
        return RxJavaPlugins.onAssembly(new FlowableFromCallable(supplier));
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public static <T> Flowable<T> fromFuture(Future<? extends T> future) {
        ObjectHelper.requireNonNull(future, "future is null");
        return RxJavaPlugins.onAssembly(new FlowableFromFuture(future, 0, null));
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public static <T> Flowable<T> fromFuture(Future<? extends T> future, long timeout, TimeUnit unit) {
        ObjectHelper.requireNonNull(future, "future is null");
        ObjectHelper.requireNonNull(unit, "unit is null");
        return RxJavaPlugins.onAssembly(new FlowableFromFuture(future, timeout, unit));
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("custom")
    public static <T> Flowable<T> fromFuture(Future<? extends T> future, long timeout, TimeUnit unit, Scheduler scheduler) {
        ObjectHelper.requireNonNull(scheduler, "scheduler is null");
        return fromFuture(future, timeout, unit).subscribeOn(scheduler);
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("custom")
    public static <T> Flowable<T> fromFuture(Future<? extends T> future, Scheduler scheduler) {
        ObjectHelper.requireNonNull(scheduler, "scheduler is null");
        return fromFuture(future).subscribeOn(scheduler);
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public static <T> Flowable<T> fromIterable(Iterable<? extends T> source) {
        ObjectHelper.requireNonNull(source, "source is null");
        return RxJavaPlugins.onAssembly(new FlowableFromIterable(source));
    }

    @BackpressureSupport(BackpressureKind.PASS_THROUGH)
    @SchedulerSupport("none")
    public static <T> Flowable<T> fromPublisher(Publisher<? extends T> source) {
        if (source instanceof Flowable) {
            return RxJavaPlugins.onAssembly((Flowable) source);
        }
        ObjectHelper.requireNonNull(source, "publisher is null");
        return RxJavaPlugins.onAssembly(new FlowableFromPublisher(source));
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public static <T> Flowable<T> generate(Consumer<Emitter<T>> generator) {
        ObjectHelper.requireNonNull(generator, "generator is null");
        return generate(Functions.nullSupplier(), FlowableInternalHelper.simpleGenerator(generator), Functions.emptyConsumer());
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public static <T, S> Flowable<T> generate(Callable<S> initialState, BiConsumer<S, Emitter<T>> generator) {
        ObjectHelper.requireNonNull(generator, "generator is null");
        return generate(initialState, FlowableInternalHelper.simpleBiGenerator(generator), Functions.emptyConsumer());
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public static <T, S> Flowable<T> generate(Callable<S> initialState, BiConsumer<S, Emitter<T>> generator, Consumer<? super S> disposeState) {
        ObjectHelper.requireNonNull(generator, "generator is null");
        return generate(initialState, FlowableInternalHelper.simpleBiGenerator(generator), disposeState);
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public static <T, S> Flowable<T> generate(Callable<S> initialState, BiFunction<S, Emitter<T>, S> generator) {
        return generate(initialState, generator, Functions.emptyConsumer());
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public static <T, S> Flowable<T> generate(Callable<S> initialState, BiFunction<S, Emitter<T>, S> generator, Consumer<? super S> disposeState) {
        ObjectHelper.requireNonNull(initialState, "initialState is null");
        ObjectHelper.requireNonNull(generator, "generator is null");
        ObjectHelper.requireNonNull(disposeState, "disposeState is null");
        return RxJavaPlugins.onAssembly(new FlowableGenerate(initialState, generator, disposeState));
    }

    @BackpressureSupport(BackpressureKind.ERROR)
    @SchedulerSupport("io.reactivex:computation")
    public static Flowable<Long> interval(long initialDelay, long period, TimeUnit unit) {
        return interval(initialDelay, period, unit, Schedulers.computation());
    }

    /* JADX DEBUG: Failed to find minimal casts for resolve overloaded methods, cast all args instead
     method: ClspMth{java.lang.Math.max(long, long):long}
     arg types: [int, long]
     candidates:
      ClspMth{java.lang.Math.max(double, double):double}
      ClspMth{java.lang.Math.max(int, int):int}
      ClspMth{java.lang.Math.max(float, float):float}
      ClspMth{java.lang.Math.max(long, long):long} */
    @BackpressureSupport(BackpressureKind.ERROR)
    @SchedulerSupport("custom")
    public static Flowable<Long> interval(long initialDelay, long period, TimeUnit unit, Scheduler scheduler) {
        ObjectHelper.requireNonNull(unit, "unit is null");
        ObjectHelper.requireNonNull(scheduler, "scheduler is null");
        return RxJavaPlugins.onAssembly(new FlowableInterval(Math.max(0L, initialDelay), Math.max(0L, period), unit, scheduler));
    }

    @BackpressureSupport(BackpressureKind.ERROR)
    @SchedulerSupport("io.reactivex:computation")
    public static Flowable<Long> interval(long period, TimeUnit unit) {
        return interval(period, period, unit, Schedulers.computation());
    }

    @BackpressureSupport(BackpressureKind.ERROR)
    @SchedulerSupport("custom")
    public static Flowable<Long> interval(long period, TimeUnit unit, Scheduler scheduler) {
        return interval(period, period, unit, scheduler);
    }

    @BackpressureSupport(BackpressureKind.ERROR)
    @SchedulerSupport("io.reactivex:computation")
    public static Flowable<Long> intervalRange(long start, long count, long initialDelay, long period, TimeUnit unit) {
        return intervalRange(start, count, initialDelay, period, unit, Schedulers.computation());
    }

    /* JADX DEBUG: Failed to find minimal casts for resolve overloaded methods, cast all args instead
     method: ClspMth{java.lang.Math.max(long, long):long}
     arg types: [int, long]
     candidates:
      ClspMth{java.lang.Math.max(double, double):double}
      ClspMth{java.lang.Math.max(int, int):int}
      ClspMth{java.lang.Math.max(float, float):float}
      ClspMth{java.lang.Math.max(long, long):long} */
    @BackpressureSupport(BackpressureKind.ERROR)
    @SchedulerSupport("custom")
    public static Flowable<Long> intervalRange(long start, long count, long initialDelay, long period, TimeUnit unit, Scheduler scheduler) {
        if (count < 0) {
            throw new IllegalArgumentException("count >= 0 required but it was " + count);
        } else if (count == 0) {
            return empty().delay(initialDelay, unit, scheduler);
        } else {
            long end = start + (count - 1);
            if (start <= 0 || end >= 0) {
                ObjectHelper.requireNonNull(unit, "unit is null");
                ObjectHelper.requireNonNull(scheduler, "scheduler is null");
                return RxJavaPlugins.onAssembly(new FlowableIntervalRange(start, end, Math.max(0L, initialDelay), Math.max(0L, period), unit, scheduler));
            }
            throw new IllegalArgumentException("Overflow! start + count is bigger than Long.MAX_VALUE");
        }
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public static <T> Flowable<T> just(T item) {
        ObjectHelper.requireNonNull(item, "item is null");
        return RxJavaPlugins.onAssembly(new FlowableJust(item));
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public static <T> Flowable<T> just(T item1, T item2) {
        ObjectHelper.requireNonNull(item1, "The first item is null");
        ObjectHelper.requireNonNull(item2, "The second item is null");
        return fromArray(item1, item2);
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public static <T> Flowable<T> just(T item1, T item2, T item3) {
        ObjectHelper.requireNonNull(item1, "The first item is null");
        ObjectHelper.requireNonNull(item2, "The second item is null");
        ObjectHelper.requireNonNull(item3, "The third item is null");
        return fromArray(item1, item2, item3);
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public static <T> Flowable<T> just(T item1, T item2, T item3, T item4) {
        ObjectHelper.requireNonNull(item1, "The first item is null");
        ObjectHelper.requireNonNull(item2, "The second item is null");
        ObjectHelper.requireNonNull(item3, "The third item is null");
        ObjectHelper.requireNonNull(item4, "The fourth item is null");
        return fromArray(item1, item2, item3, item4);
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public static <T> Flowable<T> just(T item1, T item2, T item3, T item4, T item5) {
        ObjectHelper.requireNonNull(item1, "The first item is null");
        ObjectHelper.requireNonNull(item2, "The second item is null");
        ObjectHelper.requireNonNull(item3, "The third item is null");
        ObjectHelper.requireNonNull(item4, "The fourth item is null");
        ObjectHelper.requireNonNull(item5, "The fifth item is null");
        return fromArray(item1, item2, item3, item4, item5);
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public static <T> Flowable<T> just(T item1, T item2, T item3, T item4, T item5, T item6) {
        ObjectHelper.requireNonNull(item1, "The first item is null");
        ObjectHelper.requireNonNull(item2, "The second item is null");
        ObjectHelper.requireNonNull(item3, "The third item is null");
        ObjectHelper.requireNonNull(item4, "The fourth item is null");
        ObjectHelper.requireNonNull(item5, "The fifth item is null");
        ObjectHelper.requireNonNull(item6, "The sixth item is null");
        return fromArray(item1, item2, item3, item4, item5, item6);
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public static <T> Flowable<T> just(T item1, T item2, T item3, T item4, T item5, T item6, T item7) {
        ObjectHelper.requireNonNull(item1, "The first item is null");
        ObjectHelper.requireNonNull(item2, "The second item is null");
        ObjectHelper.requireNonNull(item3, "The third item is null");
        ObjectHelper.requireNonNull(item4, "The fourth item is null");
        ObjectHelper.requireNonNull(item5, "The fifth item is null");
        ObjectHelper.requireNonNull(item6, "The sixth item is null");
        ObjectHelper.requireNonNull(item7, "The seventh item is null");
        return fromArray(item1, item2, item3, item4, item5, item6, item7);
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public static <T> Flowable<T> just(T item1, T item2, T item3, T item4, T item5, T item6, T item7, T item8) {
        ObjectHelper.requireNonNull(item1, "The first item is null");
        ObjectHelper.requireNonNull(item2, "The second item is null");
        ObjectHelper.requireNonNull(item3, "The third item is null");
        ObjectHelper.requireNonNull(item4, "The fourth item is null");
        ObjectHelper.requireNonNull(item5, "The fifth item is null");
        ObjectHelper.requireNonNull(item6, "The sixth item is null");
        ObjectHelper.requireNonNull(item7, "The seventh item is null");
        ObjectHelper.requireNonNull(item8, "The eighth item is null");
        return fromArray(item1, item2, item3, item4, item5, item6, item7, item8);
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public static <T> Flowable<T> just(T item1, T item2, T item3, T item4, T item5, T item6, T item7, T item8, T item9) {
        ObjectHelper.requireNonNull(item1, "The first item is null");
        ObjectHelper.requireNonNull(item2, "The second item is null");
        ObjectHelper.requireNonNull(item3, "The third item is null");
        ObjectHelper.requireNonNull(item4, "The fourth item is null");
        ObjectHelper.requireNonNull(item5, "The fifth item is null");
        ObjectHelper.requireNonNull(item6, "The sixth item is null");
        ObjectHelper.requireNonNull(item7, "The seventh item is null");
        ObjectHelper.requireNonNull(item8, "The eighth item is null");
        ObjectHelper.requireNonNull(item9, "The ninth is null");
        return fromArray(item1, item2, item3, item4, item5, item6, item7, item8, item9);
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public static <T> Flowable<T> just(T item1, T item2, T item3, T item4, T item5, T item6, T item7, T item8, T item9, T item10) {
        ObjectHelper.requireNonNull(item1, "The first item is null");
        ObjectHelper.requireNonNull(item2, "The second item is null");
        ObjectHelper.requireNonNull(item3, "The third item is null");
        ObjectHelper.requireNonNull(item4, "The fourth item is null");
        ObjectHelper.requireNonNull(item5, "The fifth item is null");
        ObjectHelper.requireNonNull(item6, "The sixth item is null");
        ObjectHelper.requireNonNull(item7, "The seventh item is null");
        ObjectHelper.requireNonNull(item8, "The eighth item is null");
        ObjectHelper.requireNonNull(item9, "The ninth item is null");
        ObjectHelper.requireNonNull(item10, "The tenth item is null");
        return fromArray(item1, item2, item3, item4, item5, item6, item7, item8, item9, item10);
    }

    /* JADX WARN: Type inference failed for: r3v0, types: [java.lang.Iterable<? extends org.reactivestreams.Publisher<? extends T>>, java.lang.Iterable], assign insn: null */
    /* JADX DEBUG: Failed to find minimal casts for resolve overloaded methods, cast all args instead
     method: dji.thirdparty.io.reactivex.Flowable.flatMap(dji.thirdparty.io.reactivex.functions.Function, boolean, int, int):dji.thirdparty.io.reactivex.Flowable<R>
     arg types: [dji.thirdparty.io.reactivex.functions.Function, int, int, int]
     candidates:
      dji.thirdparty.io.reactivex.Flowable.flatMap(dji.thirdparty.io.reactivex.functions.Function, dji.thirdparty.io.reactivex.functions.BiFunction, boolean, int):dji.thirdparty.io.reactivex.Flowable<R>
      dji.thirdparty.io.reactivex.Flowable.flatMap(dji.thirdparty.io.reactivex.functions.Function, dji.thirdparty.io.reactivex.functions.Function, java.util.concurrent.Callable, int):dji.thirdparty.io.reactivex.Flowable<R>
      dji.thirdparty.io.reactivex.Flowable.flatMap(dji.thirdparty.io.reactivex.functions.Function, boolean, int, int):dji.thirdparty.io.reactivex.Flowable<R> */
    /* JADX WARNING: Unknown variable types count: 1 */
    @dji.thirdparty.io.reactivex.annotations.BackpressureSupport(dji.thirdparty.io.reactivex.annotations.BackpressureKind.FULL)
    @dji.thirdparty.io.reactivex.annotations.SchedulerSupport("none")
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static <T> dji.thirdparty.io.reactivex.Flowable<T> merge(java.lang.Iterable<? extends org.reactivestreams.Publisher<? extends T>> r3, int r4, int r5) {
        /*
            dji.thirdparty.io.reactivex.Flowable r0 = fromIterable(r3)
            dji.thirdparty.io.reactivex.functions.Function r1 = dji.thirdparty.io.reactivex.internal.functions.Functions.identity()
            r2 = 0
            dji.thirdparty.io.reactivex.Flowable r0 = r0.flatMap(r1, r2, r4, r5)
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: dji.thirdparty.io.reactivex.Flowable.merge(java.lang.Iterable, int, int):dji.thirdparty.io.reactivex.Flowable");
    }

    /* JADX DEBUG: Failed to find minimal casts for resolve overloaded methods, cast all args instead
     method: dji.thirdparty.io.reactivex.Flowable.flatMap(dji.thirdparty.io.reactivex.functions.Function, boolean, int, int):dji.thirdparty.io.reactivex.Flowable<R>
     arg types: [dji.thirdparty.io.reactivex.functions.Function, int, int, int]
     candidates:
      dji.thirdparty.io.reactivex.Flowable.flatMap(dji.thirdparty.io.reactivex.functions.Function, dji.thirdparty.io.reactivex.functions.BiFunction, boolean, int):dji.thirdparty.io.reactivex.Flowable<R>
      dji.thirdparty.io.reactivex.Flowable.flatMap(dji.thirdparty.io.reactivex.functions.Function, dji.thirdparty.io.reactivex.functions.Function, java.util.concurrent.Callable, int):dji.thirdparty.io.reactivex.Flowable<R>
      dji.thirdparty.io.reactivex.Flowable.flatMap(dji.thirdparty.io.reactivex.functions.Function, boolean, int, int):dji.thirdparty.io.reactivex.Flowable<R> */
    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public static <T> Flowable<T> mergeArray(int maxConcurrency, int bufferSize, Publisher<? extends T>... sources) {
        return fromArray(sources).flatMap(Functions.identity(), false, maxConcurrency, bufferSize);
    }

    /* JADX WARN: Type inference failed for: r2v0, types: [java.lang.Iterable<? extends org.reactivestreams.Publisher<? extends T>>, java.lang.Iterable], assign insn: null */
    /* JADX WARNING: Unknown variable types count: 1 */
    @dji.thirdparty.io.reactivex.annotations.BackpressureSupport(dji.thirdparty.io.reactivex.annotations.BackpressureKind.FULL)
    @dji.thirdparty.io.reactivex.annotations.SchedulerSupport("none")
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static <T> dji.thirdparty.io.reactivex.Flowable<T> merge(java.lang.Iterable<? extends org.reactivestreams.Publisher<? extends T>> r2) {
        /*
            dji.thirdparty.io.reactivex.Flowable r0 = fromIterable(r2)
            dji.thirdparty.io.reactivex.functions.Function r1 = dji.thirdparty.io.reactivex.internal.functions.Functions.identity()
            dji.thirdparty.io.reactivex.Flowable r0 = r0.flatMap(r1)
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: dji.thirdparty.io.reactivex.Flowable.merge(java.lang.Iterable):dji.thirdparty.io.reactivex.Flowable");
    }

    /* JADX WARN: Type inference failed for: r2v0, types: [java.lang.Iterable<? extends org.reactivestreams.Publisher<? extends T>>, java.lang.Iterable], assign insn: null */
    /* JADX WARNING: Unknown variable types count: 1 */
    @dji.thirdparty.io.reactivex.annotations.BackpressureSupport(dji.thirdparty.io.reactivex.annotations.BackpressureKind.FULL)
    @dji.thirdparty.io.reactivex.annotations.SchedulerSupport("none")
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static <T> dji.thirdparty.io.reactivex.Flowable<T> merge(java.lang.Iterable<? extends org.reactivestreams.Publisher<? extends T>> r2, int r3) {
        /*
            dji.thirdparty.io.reactivex.Flowable r0 = fromIterable(r2)
            dji.thirdparty.io.reactivex.functions.Function r1 = dji.thirdparty.io.reactivex.internal.functions.Functions.identity()
            dji.thirdparty.io.reactivex.Flowable r0 = r0.flatMap(r1, r3)
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: dji.thirdparty.io.reactivex.Flowable.merge(java.lang.Iterable, int):dji.thirdparty.io.reactivex.Flowable");
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public static <T> Flowable<T> merge(Publisher<? extends Publisher<? extends T>> sources) {
        return merge(sources, bufferSize());
    }

    /* JADX WARN: Type inference failed for: r2v0, types: [org.reactivestreams.Publisher<? extends org.reactivestreams.Publisher<? extends T>>, org.reactivestreams.Publisher], assign insn: null */
    /* JADX WARNING: Unknown variable types count: 1 */
    @dji.thirdparty.io.reactivex.annotations.BackpressureSupport(dji.thirdparty.io.reactivex.annotations.BackpressureKind.FULL)
    @dji.thirdparty.io.reactivex.annotations.SchedulerSupport("none")
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static <T> dji.thirdparty.io.reactivex.Flowable<T> merge(org.reactivestreams.Publisher<? extends org.reactivestreams.Publisher<? extends T>> r2, int r3) {
        /*
            dji.thirdparty.io.reactivex.Flowable r0 = fromPublisher(r2)
            dji.thirdparty.io.reactivex.functions.Function r1 = dji.thirdparty.io.reactivex.internal.functions.Functions.identity()
            dji.thirdparty.io.reactivex.Flowable r0 = r0.flatMap(r1, r3)
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: dji.thirdparty.io.reactivex.Flowable.merge(org.reactivestreams.Publisher, int):dji.thirdparty.io.reactivex.Flowable");
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public static <T> Flowable<T> mergeArray(Publisher<? extends T>... sources) {
        return fromArray(sources).flatMap(Functions.identity(), sources.length);
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
    public static <T> Flowable<T> merge(Publisher<? extends T> source1, Publisher<? extends T> source2) {
        ObjectHelper.requireNonNull(source1, "source1 is null");
        ObjectHelper.requireNonNull(source2, "source2 is null");
        return fromArray(source1, source2).flatMap(Functions.identity(), false, 2);
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
    public static <T> Flowable<T> merge(Publisher<? extends T> source1, Publisher<? extends T> source2, Publisher<? extends T> source3) {
        ObjectHelper.requireNonNull(source1, "source1 is null");
        ObjectHelper.requireNonNull(source2, "source2 is null");
        ObjectHelper.requireNonNull(source3, "source3 is null");
        return fromArray(source1, source2, source3).flatMap(Functions.identity(), false, 3);
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
    public static <T> Flowable<T> merge(Publisher<? extends T> source1, Publisher<? extends T> source2, Publisher<? extends T> source3, Publisher<? extends T> source4) {
        ObjectHelper.requireNonNull(source1, "source1 is null");
        ObjectHelper.requireNonNull(source2, "source2 is null");
        ObjectHelper.requireNonNull(source3, "source3 is null");
        ObjectHelper.requireNonNull(source4, "source4 is null");
        return fromArray(source1, source2, source3, source4).flatMap(Functions.identity(), false, 4);
    }

    /* JADX WARN: Type inference failed for: r3v0, types: [java.lang.Iterable<? extends org.reactivestreams.Publisher<? extends T>>, java.lang.Iterable], assign insn: null */
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
    public static <T> dji.thirdparty.io.reactivex.Flowable<T> mergeDelayError(java.lang.Iterable<? extends org.reactivestreams.Publisher<? extends T>> r3) {
        /*
            dji.thirdparty.io.reactivex.Flowable r0 = fromIterable(r3)
            dji.thirdparty.io.reactivex.functions.Function r1 = dji.thirdparty.io.reactivex.internal.functions.Functions.identity()
            r2 = 1
            dji.thirdparty.io.reactivex.Flowable r0 = r0.flatMap(r1, r2)
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: dji.thirdparty.io.reactivex.Flowable.mergeDelayError(java.lang.Iterable):dji.thirdparty.io.reactivex.Flowable");
    }

    /* JADX WARN: Type inference failed for: r3v0, types: [java.lang.Iterable<? extends org.reactivestreams.Publisher<? extends T>>, java.lang.Iterable], assign insn: null */
    /* JADX DEBUG: Failed to find minimal casts for resolve overloaded methods, cast all args instead
     method: dji.thirdparty.io.reactivex.Flowable.flatMap(dji.thirdparty.io.reactivex.functions.Function, boolean, int, int):dji.thirdparty.io.reactivex.Flowable<R>
     arg types: [dji.thirdparty.io.reactivex.functions.Function, int, int, int]
     candidates:
      dji.thirdparty.io.reactivex.Flowable.flatMap(dji.thirdparty.io.reactivex.functions.Function, dji.thirdparty.io.reactivex.functions.BiFunction, boolean, int):dji.thirdparty.io.reactivex.Flowable<R>
      dji.thirdparty.io.reactivex.Flowable.flatMap(dji.thirdparty.io.reactivex.functions.Function, dji.thirdparty.io.reactivex.functions.Function, java.util.concurrent.Callable, int):dji.thirdparty.io.reactivex.Flowable<R>
      dji.thirdparty.io.reactivex.Flowable.flatMap(dji.thirdparty.io.reactivex.functions.Function, boolean, int, int):dji.thirdparty.io.reactivex.Flowable<R> */
    /* JADX WARNING: Unknown variable types count: 1 */
    @dji.thirdparty.io.reactivex.annotations.BackpressureSupport(dji.thirdparty.io.reactivex.annotations.BackpressureKind.FULL)
    @dji.thirdparty.io.reactivex.annotations.SchedulerSupport("none")
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static <T> dji.thirdparty.io.reactivex.Flowable<T> mergeDelayError(java.lang.Iterable<? extends org.reactivestreams.Publisher<? extends T>> r3, int r4, int r5) {
        /*
            dji.thirdparty.io.reactivex.Flowable r0 = fromIterable(r3)
            dji.thirdparty.io.reactivex.functions.Function r1 = dji.thirdparty.io.reactivex.internal.functions.Functions.identity()
            r2 = 1
            dji.thirdparty.io.reactivex.Flowable r0 = r0.flatMap(r1, r2, r4, r5)
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: dji.thirdparty.io.reactivex.Flowable.mergeDelayError(java.lang.Iterable, int, int):dji.thirdparty.io.reactivex.Flowable");
    }

    /* JADX DEBUG: Failed to find minimal casts for resolve overloaded methods, cast all args instead
     method: dji.thirdparty.io.reactivex.Flowable.flatMap(dji.thirdparty.io.reactivex.functions.Function, boolean, int, int):dji.thirdparty.io.reactivex.Flowable<R>
     arg types: [dji.thirdparty.io.reactivex.functions.Function, int, int, int]
     candidates:
      dji.thirdparty.io.reactivex.Flowable.flatMap(dji.thirdparty.io.reactivex.functions.Function, dji.thirdparty.io.reactivex.functions.BiFunction, boolean, int):dji.thirdparty.io.reactivex.Flowable<R>
      dji.thirdparty.io.reactivex.Flowable.flatMap(dji.thirdparty.io.reactivex.functions.Function, dji.thirdparty.io.reactivex.functions.Function, java.util.concurrent.Callable, int):dji.thirdparty.io.reactivex.Flowable<R>
      dji.thirdparty.io.reactivex.Flowable.flatMap(dji.thirdparty.io.reactivex.functions.Function, boolean, int, int):dji.thirdparty.io.reactivex.Flowable<R> */
    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public static <T> Flowable<T> mergeArrayDelayError(int maxConcurrency, int bufferSize, Publisher<? extends T>... sources) {
        return fromArray(sources).flatMap(Functions.identity(), true, maxConcurrency, bufferSize);
    }

    /* JADX WARN: Type inference failed for: r3v0, types: [java.lang.Iterable<? extends org.reactivestreams.Publisher<? extends T>>, java.lang.Iterable], assign insn: null */
    /* JADX DEBUG: Failed to find minimal casts for resolve overloaded methods, cast all args instead
     method: dji.thirdparty.io.reactivex.Flowable.flatMap(dji.thirdparty.io.reactivex.functions.Function, boolean, int):dji.thirdparty.io.reactivex.Flowable<R>
     arg types: [dji.thirdparty.io.reactivex.functions.Function, int, int]
     candidates:
      dji.thirdparty.io.reactivex.Flowable.flatMap(dji.thirdparty.io.reactivex.functions.Function, dji.thirdparty.io.reactivex.functions.BiFunction, int):dji.thirdparty.io.reactivex.Flowable<R>
      dji.thirdparty.io.reactivex.Flowable.flatMap(dji.thirdparty.io.reactivex.functions.Function, dji.thirdparty.io.reactivex.functions.BiFunction, boolean):dji.thirdparty.io.reactivex.Flowable<R>
      dji.thirdparty.io.reactivex.Flowable.flatMap(dji.thirdparty.io.reactivex.functions.Function, dji.thirdparty.io.reactivex.functions.Function, java.util.concurrent.Callable):dji.thirdparty.io.reactivex.Flowable<R>
      dji.thirdparty.io.reactivex.Flowable.flatMap(dji.thirdparty.io.reactivex.functions.Function, boolean, int):dji.thirdparty.io.reactivex.Flowable<R> */
    /* JADX WARNING: Unknown variable types count: 1 */
    @dji.thirdparty.io.reactivex.annotations.BackpressureSupport(dji.thirdparty.io.reactivex.annotations.BackpressureKind.FULL)
    @dji.thirdparty.io.reactivex.annotations.SchedulerSupport("none")
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static <T> dji.thirdparty.io.reactivex.Flowable<T> mergeDelayError(java.lang.Iterable<? extends org.reactivestreams.Publisher<? extends T>> r3, int r4) {
        /*
            dji.thirdparty.io.reactivex.Flowable r0 = fromIterable(r3)
            dji.thirdparty.io.reactivex.functions.Function r1 = dji.thirdparty.io.reactivex.internal.functions.Functions.identity()
            r2 = 1
            dji.thirdparty.io.reactivex.Flowable r0 = r0.flatMap(r1, r2, r4)
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: dji.thirdparty.io.reactivex.Flowable.mergeDelayError(java.lang.Iterable, int):dji.thirdparty.io.reactivex.Flowable");
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public static <T> Flowable<T> mergeDelayError(Publisher<? extends Publisher<? extends T>> sources) {
        return mergeDelayError(sources, bufferSize());
    }

    /* JADX WARN: Type inference failed for: r3v0, types: [org.reactivestreams.Publisher<? extends org.reactivestreams.Publisher<? extends T>>, org.reactivestreams.Publisher], assign insn: null */
    /* JADX DEBUG: Failed to find minimal casts for resolve overloaded methods, cast all args instead
     method: dji.thirdparty.io.reactivex.Flowable.flatMap(dji.thirdparty.io.reactivex.functions.Function, boolean, int):dji.thirdparty.io.reactivex.Flowable<R>
     arg types: [dji.thirdparty.io.reactivex.functions.Function, int, int]
     candidates:
      dji.thirdparty.io.reactivex.Flowable.flatMap(dji.thirdparty.io.reactivex.functions.Function, dji.thirdparty.io.reactivex.functions.BiFunction, int):dji.thirdparty.io.reactivex.Flowable<R>
      dji.thirdparty.io.reactivex.Flowable.flatMap(dji.thirdparty.io.reactivex.functions.Function, dji.thirdparty.io.reactivex.functions.BiFunction, boolean):dji.thirdparty.io.reactivex.Flowable<R>
      dji.thirdparty.io.reactivex.Flowable.flatMap(dji.thirdparty.io.reactivex.functions.Function, dji.thirdparty.io.reactivex.functions.Function, java.util.concurrent.Callable):dji.thirdparty.io.reactivex.Flowable<R>
      dji.thirdparty.io.reactivex.Flowable.flatMap(dji.thirdparty.io.reactivex.functions.Function, boolean, int):dji.thirdparty.io.reactivex.Flowable<R> */
    /* JADX WARNING: Unknown variable types count: 1 */
    @dji.thirdparty.io.reactivex.annotations.BackpressureSupport(dji.thirdparty.io.reactivex.annotations.BackpressureKind.FULL)
    @dji.thirdparty.io.reactivex.annotations.SchedulerSupport("none")
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static <T> dji.thirdparty.io.reactivex.Flowable<T> mergeDelayError(org.reactivestreams.Publisher<? extends org.reactivestreams.Publisher<? extends T>> r3, int r4) {
        /*
            dji.thirdparty.io.reactivex.Flowable r0 = fromPublisher(r3)
            dji.thirdparty.io.reactivex.functions.Function r1 = dji.thirdparty.io.reactivex.internal.functions.Functions.identity()
            r2 = 1
            dji.thirdparty.io.reactivex.Flowable r0 = r0.flatMap(r1, r2, r4)
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: dji.thirdparty.io.reactivex.Flowable.mergeDelayError(org.reactivestreams.Publisher, int):dji.thirdparty.io.reactivex.Flowable");
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
    public static <T> Flowable<T> mergeArrayDelayError(Publisher<? extends T>... sources) {
        return fromArray(sources).flatMap(Functions.identity(), true, sources.length);
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
    public static <T> Flowable<T> mergeDelayError(Publisher<? extends T> source1, Publisher<? extends T> source2) {
        ObjectHelper.requireNonNull(source1, "source1 is null");
        ObjectHelper.requireNonNull(source2, "source2 is null");
        return fromArray(source1, source2).flatMap(Functions.identity(), true, 2);
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
    public static <T> Flowable<T> mergeDelayError(Publisher<? extends T> source1, Publisher<? extends T> source2, Publisher<? extends T> source3) {
        ObjectHelper.requireNonNull(source1, "source1 is null");
        ObjectHelper.requireNonNull(source2, "source2 is null");
        ObjectHelper.requireNonNull(source3, "source3 is null");
        return fromArray(source1, source2, source3).flatMap(Functions.identity(), true, 3);
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
    public static <T> Flowable<T> mergeDelayError(Publisher<? extends T> source1, Publisher<? extends T> source2, Publisher<? extends T> source3, Publisher<? extends T> source4) {
        ObjectHelper.requireNonNull(source1, "source1 is null");
        ObjectHelper.requireNonNull(source2, "source2 is null");
        ObjectHelper.requireNonNull(source3, "source3 is null");
        ObjectHelper.requireNonNull(source4, "source4 is null");
        return fromArray(source1, source2, source3, source4).flatMap(Functions.identity(), true, 4);
    }

    @BackpressureSupport(BackpressureKind.PASS_THROUGH)
    @SchedulerSupport("none")
    public static <T> Flowable<T> never() {
        return RxJavaPlugins.onAssembly(FlowableNever.INSTANCE);
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public static Flowable<Integer> range(int start, int count) {
        if (count < 0) {
            throw new IllegalArgumentException("count >= 0 required but it was " + count);
        } else if (count == 0) {
            return empty();
        } else {
            if (count == 1) {
                return just(Integer.valueOf(start));
            }
            if (((long) start) + ((long) (count - 1)) <= 2147483647L) {
                return RxJavaPlugins.onAssembly(new FlowableRange(start, count));
            }
            throw new IllegalArgumentException("Integer overflow");
        }
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public static Flowable<Long> rangeLong(long start, long count) {
        if (count < 0) {
            throw new IllegalArgumentException("count >= 0 required but it was " + count);
        } else if (count == 0) {
            return empty();
        } else {
            if (count == 1) {
                return just(Long.valueOf(start));
            }
            long end = start + (count - 1);
            if (start <= 0 || end >= 0) {
                return RxJavaPlugins.onAssembly(new FlowableRangeLong(start, count));
            }
            throw new IllegalArgumentException("Overflow! start + count is bigger than Long.MAX_VALUE");
        }
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public static <T> Single<Boolean> sequenceEqual(Publisher<? extends T> source1, Publisher<? extends T> source2) {
        return sequenceEqual(source1, source2, ObjectHelper.equalsPredicate(), bufferSize());
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public static <T> Single<Boolean> sequenceEqual(Publisher<? extends T> source1, Publisher<? extends T> source2, BiPredicate<? super T, ? super T> isEqual) {
        return sequenceEqual(source1, source2, isEqual, bufferSize());
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public static <T> Single<Boolean> sequenceEqual(Publisher<? extends T> source1, Publisher<? extends T> source2, BiPredicate<? super T, ? super T> isEqual, int bufferSize) {
        ObjectHelper.requireNonNull(source1, "source1 is null");
        ObjectHelper.requireNonNull(source2, "source2 is null");
        ObjectHelper.requireNonNull(isEqual, "isEqual is null");
        ObjectHelper.verifyPositive(bufferSize, "bufferSize");
        return RxJavaPlugins.onAssembly(new FlowableSequenceEqualSingle(source1, source2, isEqual, bufferSize));
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public static <T> Single<Boolean> sequenceEqual(Publisher<? extends T> source1, Publisher<? extends T> source2, int bufferSize) {
        return sequenceEqual(source1, source2, ObjectHelper.equalsPredicate(), bufferSize);
    }

    /* JADX WARN: Type inference failed for: r2v0, types: [org.reactivestreams.Publisher<? extends org.reactivestreams.Publisher<? extends T>>, org.reactivestreams.Publisher], assign insn: null */
    /* JADX WARNING: Unknown variable types count: 1 */
    @dji.thirdparty.io.reactivex.annotations.BackpressureSupport(dji.thirdparty.io.reactivex.annotations.BackpressureKind.FULL)
    @dji.thirdparty.io.reactivex.annotations.SchedulerSupport("none")
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static <T> dji.thirdparty.io.reactivex.Flowable<T> switchOnNext(org.reactivestreams.Publisher<? extends org.reactivestreams.Publisher<? extends T>> r2, int r3) {
        /*
            dji.thirdparty.io.reactivex.Flowable r0 = fromPublisher(r2)
            dji.thirdparty.io.reactivex.functions.Function r1 = dji.thirdparty.io.reactivex.internal.functions.Functions.identity()
            dji.thirdparty.io.reactivex.Flowable r0 = r0.switchMap(r1, r3)
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: dji.thirdparty.io.reactivex.Flowable.switchOnNext(org.reactivestreams.Publisher, int):dji.thirdparty.io.reactivex.Flowable");
    }

    /* JADX WARN: Type inference failed for: r2v0, types: [org.reactivestreams.Publisher<? extends org.reactivestreams.Publisher<? extends T>>, org.reactivestreams.Publisher], assign insn: null */
    /* JADX WARNING: Unknown variable types count: 1 */
    @dji.thirdparty.io.reactivex.annotations.BackpressureSupport(dji.thirdparty.io.reactivex.annotations.BackpressureKind.FULL)
    @dji.thirdparty.io.reactivex.annotations.SchedulerSupport("none")
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static <T> dji.thirdparty.io.reactivex.Flowable<T> switchOnNext(org.reactivestreams.Publisher<? extends org.reactivestreams.Publisher<? extends T>> r2) {
        /*
            dji.thirdparty.io.reactivex.Flowable r0 = fromPublisher(r2)
            dji.thirdparty.io.reactivex.functions.Function r1 = dji.thirdparty.io.reactivex.internal.functions.Functions.identity()
            dji.thirdparty.io.reactivex.Flowable r0 = r0.switchMap(r1)
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: dji.thirdparty.io.reactivex.Flowable.switchOnNext(org.reactivestreams.Publisher):dji.thirdparty.io.reactivex.Flowable");
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public static <T> Flowable<T> switchOnNextDelayError(Publisher<? extends Publisher<? extends T>> sources) {
        return switchOnNextDelayError(sources, bufferSize());
    }

    /* JADX WARN: Type inference failed for: r2v0, types: [org.reactivestreams.Publisher<? extends org.reactivestreams.Publisher<? extends T>>, org.reactivestreams.Publisher], assign insn: null */
    /* JADX WARNING: Unknown variable types count: 1 */
    @dji.thirdparty.io.reactivex.annotations.BackpressureSupport(dji.thirdparty.io.reactivex.annotations.BackpressureKind.FULL)
    @dji.thirdparty.io.reactivex.annotations.SchedulerSupport("none")
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static <T> dji.thirdparty.io.reactivex.Flowable<T> switchOnNextDelayError(org.reactivestreams.Publisher<? extends org.reactivestreams.Publisher<? extends T>> r2, int r3) {
        /*
            dji.thirdparty.io.reactivex.Flowable r0 = fromPublisher(r2)
            dji.thirdparty.io.reactivex.functions.Function r1 = dji.thirdparty.io.reactivex.internal.functions.Functions.identity()
            dji.thirdparty.io.reactivex.Flowable r0 = r0.switchMapDelayError(r1, r3)
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: dji.thirdparty.io.reactivex.Flowable.switchOnNextDelayError(org.reactivestreams.Publisher, int):dji.thirdparty.io.reactivex.Flowable");
    }

    @BackpressureSupport(BackpressureKind.ERROR)
    @SchedulerSupport("io.reactivex:computation")
    public static Flowable<Long> timer(long delay, TimeUnit unit) {
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
    @BackpressureSupport(BackpressureKind.ERROR)
    @SchedulerSupport("custom")
    public static Flowable<Long> timer(long delay, TimeUnit unit, Scheduler scheduler) {
        ObjectHelper.requireNonNull(unit, "unit is null");
        ObjectHelper.requireNonNull(scheduler, "scheduler is null");
        return RxJavaPlugins.onAssembly(new FlowableTimer(Math.max(0L, delay), unit, scheduler));
    }

    @BackpressureSupport(BackpressureKind.NONE)
    @SchedulerSupport("none")
    public static <T> Flowable<T> unsafeCreate(Publisher<T> onSubscribe) {
        ObjectHelper.requireNonNull(onSubscribe, "onSubscribe is null");
        if (!(onSubscribe instanceof Flowable)) {
            return RxJavaPlugins.onAssembly(new FlowableFromPublisher(onSubscribe));
        }
        throw new IllegalArgumentException("unsafeCreate(Flowable) should be upgraded");
    }

    @BackpressureSupport(BackpressureKind.PASS_THROUGH)
    @SchedulerSupport("none")
    public static <T, D> Flowable<T> using(Callable<? extends D> resourceSupplier, Function<? super D, ? extends Publisher<? extends T>> sourceSupplier, Consumer<? super D> resourceDisposer) {
        return using(resourceSupplier, sourceSupplier, resourceDisposer, true);
    }

    @BackpressureSupport(BackpressureKind.PASS_THROUGH)
    @SchedulerSupport("none")
    public static <T, D> Flowable<T> using(Callable<? extends D> resourceSupplier, Function<? super D, ? extends Publisher<? extends T>> sourceSupplier, Consumer<? super D> resourceDisposer, boolean eager) {
        ObjectHelper.requireNonNull(resourceSupplier, "resourceSupplier is null");
        ObjectHelper.requireNonNull(sourceSupplier, "sourceSupplier is null");
        ObjectHelper.requireNonNull(resourceDisposer, "disposer is null");
        return RxJavaPlugins.onAssembly(new FlowableUsing(resourceSupplier, sourceSupplier, resourceDisposer, eager));
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public static <T, R> Flowable<R> zip(Iterable<? extends Publisher<? extends T>> sources, Function<? super Object[], ? extends R> zipper) {
        ObjectHelper.requireNonNull(zipper, "zipper is null");
        ObjectHelper.requireNonNull(sources, "sources is null");
        return RxJavaPlugins.onAssembly(new FlowableZip(null, sources, zipper, bufferSize(), false));
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public static <T, R> Flowable<R> zip(Publisher<? extends Publisher<? extends T>> sources, Function<? super Object[], ? extends R> zipper) {
        ObjectHelper.requireNonNull(zipper, "zipper is null");
        return fromPublisher(sources).toList().flatMapPublisher(FlowableInternalHelper.zipIterable(zipper));
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public static <T1, T2, R> Flowable<R> zip(Publisher<? extends T1> source1, Publisher<? extends T2> source2, BiFunction<? super T1, ? super T2, ? extends R> zipper) {
        ObjectHelper.requireNonNull(source1, "source1 is null");
        ObjectHelper.requireNonNull(source2, "source2 is null");
        return zipArray(Functions.toFunction(zipper), false, bufferSize(), source1, source2);
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public static <T1, T2, R> Flowable<R> zip(Publisher<? extends T1> source1, Publisher<? extends T2> source2, BiFunction<? super T1, ? super T2, ? extends R> zipper, boolean delayError) {
        ObjectHelper.requireNonNull(source1, "source1 is null");
        ObjectHelper.requireNonNull(source2, "source2 is null");
        return zipArray(Functions.toFunction(zipper), delayError, bufferSize(), source1, source2);
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public static <T1, T2, R> Flowable<R> zip(Publisher<? extends T1> source1, Publisher<? extends T2> source2, BiFunction<? super T1, ? super T2, ? extends R> zipper, boolean delayError, int bufferSize) {
        ObjectHelper.requireNonNull(source1, "source1 is null");
        ObjectHelper.requireNonNull(source2, "source2 is null");
        return zipArray(Functions.toFunction(zipper), delayError, bufferSize, source1, source2);
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public static <T1, T2, T3, R> Flowable<R> zip(Publisher<? extends T1> source1, Publisher<? extends T2> source2, Publisher<? extends T3> source3, Function3<? super T1, ? super T2, ? super T3, ? extends R> zipper) {
        ObjectHelper.requireNonNull(source1, "source1 is null");
        ObjectHelper.requireNonNull(source2, "source2 is null");
        ObjectHelper.requireNonNull(source3, "source3 is null");
        return zipArray(Functions.toFunction(zipper), false, bufferSize(), source1, source2, source3);
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public static <T1, T2, T3, T4, R> Flowable<R> zip(Publisher<? extends T1> source1, Publisher<? extends T2> source2, Publisher<? extends T3> source3, Publisher<? extends T4> source4, Function4<? super T1, ? super T2, ? super T3, ? super T4, ? extends R> zipper) {
        ObjectHelper.requireNonNull(source1, "source1 is null");
        ObjectHelper.requireNonNull(source2, "source2 is null");
        ObjectHelper.requireNonNull(source3, "source3 is null");
        ObjectHelper.requireNonNull(source4, "source4 is null");
        return zipArray(Functions.toFunction(zipper), false, bufferSize(), source1, source2, source3, source4);
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public static <T1, T2, T3, T4, T5, R> Flowable<R> zip(Publisher<? extends T1> source1, Publisher<? extends T2> source2, Publisher<? extends T3> source3, Publisher<? extends T4> source4, Publisher<? extends T5> source5, Function5<? super T1, ? super T2, ? super T3, ? super T4, ? super T5, ? extends R> zipper) {
        ObjectHelper.requireNonNull(source1, "source1 is null");
        ObjectHelper.requireNonNull(source2, "source2 is null");
        ObjectHelper.requireNonNull(source3, "source3 is null");
        ObjectHelper.requireNonNull(source4, "source4 is null");
        ObjectHelper.requireNonNull(source5, "source5 is null");
        return zipArray(Functions.toFunction(zipper), false, bufferSize(), source1, source2, source3, source4, source5);
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public static <T1, T2, T3, T4, T5, T6, R> Flowable<R> zip(Publisher<? extends T1> source1, Publisher<? extends T2> source2, Publisher<? extends T3> source3, Publisher<? extends T4> source4, Publisher<? extends T5> source5, Publisher<? extends T6> source6, Function6<? super T1, ? super T2, ? super T3, ? super T4, ? super T5, ? super T6, ? extends R> zipper) {
        ObjectHelper.requireNonNull(source1, "source1 is null");
        ObjectHelper.requireNonNull(source2, "source2 is null");
        ObjectHelper.requireNonNull(source3, "source3 is null");
        ObjectHelper.requireNonNull(source4, "source4 is null");
        ObjectHelper.requireNonNull(source5, "source5 is null");
        ObjectHelper.requireNonNull(source6, "source6 is null");
        return zipArray(Functions.toFunction(zipper), false, bufferSize(), source1, source2, source3, source4, source5, source6);
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public static <T1, T2, T3, T4, T5, T6, T7, R> Flowable<R> zip(Publisher<? extends T1> source1, Publisher<? extends T2> source2, Publisher<? extends T3> source3, Publisher<? extends T4> source4, Publisher<? extends T5> source5, Publisher<? extends T6> source6, Publisher<? extends T7> source7, Function7<? super T1, ? super T2, ? super T3, ? super T4, ? super T5, ? super T6, ? super T7, ? extends R> zipper) {
        ObjectHelper.requireNonNull(source1, "source1 is null");
        ObjectHelper.requireNonNull(source2, "source2 is null");
        ObjectHelper.requireNonNull(source3, "source3 is null");
        ObjectHelper.requireNonNull(source4, "source4 is null");
        ObjectHelper.requireNonNull(source5, "source5 is null");
        ObjectHelper.requireNonNull(source6, "source6 is null");
        ObjectHelper.requireNonNull(source7, "source7 is null");
        return zipArray(Functions.toFunction(zipper), false, bufferSize(), source1, source2, source3, source4, source5, source6, source7);
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public static <T1, T2, T3, T4, T5, T6, T7, T8, R> Flowable<R> zip(Publisher<? extends T1> source1, Publisher<? extends T2> source2, Publisher<? extends T3> source3, Publisher<? extends T4> source4, Publisher<? extends T5> source5, Publisher<? extends T6> source6, Publisher<? extends T7> source7, Publisher<? extends T8> source8, Function8<? super T1, ? super T2, ? super T3, ? super T4, ? super T5, ? super T6, ? super T7, ? super T8, ? extends R> zipper) {
        ObjectHelper.requireNonNull(source1, "source1 is null");
        ObjectHelper.requireNonNull(source2, "source2 is null");
        ObjectHelper.requireNonNull(source3, "source3 is null");
        ObjectHelper.requireNonNull(source4, "source4 is null");
        ObjectHelper.requireNonNull(source5, "source5 is null");
        ObjectHelper.requireNonNull(source6, "source6 is null");
        ObjectHelper.requireNonNull(source7, "source7 is null");
        ObjectHelper.requireNonNull(source8, "source8 is null");
        return zipArray(Functions.toFunction(zipper), false, bufferSize(), source1, source2, source3, source4, source5, source6, source7, source8);
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public static <T1, T2, T3, T4, T5, T6, T7, T8, T9, R> Flowable<R> zip(Publisher<? extends T1> source1, Publisher<? extends T2> source2, Publisher<? extends T3> source3, Publisher<? extends T4> source4, Publisher<? extends T5> source5, Publisher<? extends T6> source6, Publisher<? extends T7> source7, Publisher<? extends T8> source8, Publisher<? extends T9> source9, Function9<? super T1, ? super T2, ? super T3, ? super T4, ? super T5, ? super T6, ? super T7, ? super T8, ? super T9, ? extends R> zipper) {
        ObjectHelper.requireNonNull(source1, "source1 is null");
        ObjectHelper.requireNonNull(source2, "source2 is null");
        ObjectHelper.requireNonNull(source3, "source3 is null");
        ObjectHelper.requireNonNull(source4, "source4 is null");
        ObjectHelper.requireNonNull(source5, "source5 is null");
        ObjectHelper.requireNonNull(source6, "source6 is null");
        ObjectHelper.requireNonNull(source7, "source7 is null");
        ObjectHelper.requireNonNull(source8, "source8 is null");
        ObjectHelper.requireNonNull(source9, "source9 is null");
        return zipArray(Functions.toFunction(zipper), false, bufferSize(), source1, source2, source3, source4, source5, source6, source7, source8, source9);
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public static <T, R> Flowable<R> zipArray(Function<? super Object[], ? extends R> zipper, boolean delayError, int bufferSize, Publisher<? extends T>... sources) {
        if (sources.length == 0) {
            return empty();
        }
        ObjectHelper.requireNonNull(zipper, "zipper is null");
        ObjectHelper.verifyPositive(bufferSize, "bufferSize");
        return RxJavaPlugins.onAssembly(new FlowableZip(sources, null, zipper, bufferSize, delayError));
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public static <T, R> Flowable<R> zipIterable(Iterable<? extends Publisher<? extends T>> sources, Function<? super Object[], ? extends R> zipper, boolean delayError, int bufferSize) {
        ObjectHelper.requireNonNull(zipper, "zipper is null");
        ObjectHelper.requireNonNull(sources, "sources is null");
        ObjectHelper.verifyPositive(bufferSize, "bufferSize");
        return RxJavaPlugins.onAssembly(new FlowableZip(null, sources, zipper, bufferSize, delayError));
    }

    @BackpressureSupport(BackpressureKind.UNBOUNDED_IN)
    @SchedulerSupport("none")
    public final Single<Boolean> all(Predicate<? super T> predicate) {
        ObjectHelper.requireNonNull(predicate, "predicate is null");
        return RxJavaPlugins.onAssembly(new FlowableAllSingle(this, predicate));
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public final Flowable<T> ambWith(Publisher<? extends T> other) {
        ObjectHelper.requireNonNull(other, "other is null");
        return ambArray(this, other);
    }

    @BackpressureSupport(BackpressureKind.UNBOUNDED_IN)
    @SchedulerSupport("none")
    public final Single<Boolean> any(Predicate<? super T> predicate) {
        ObjectHelper.requireNonNull(predicate, "predicate is null");
        return RxJavaPlugins.onAssembly(new FlowableAnySingle(this, predicate));
    }

    @BackpressureSupport(BackpressureKind.UNBOUNDED_IN)
    @SchedulerSupport("none")
    public final T blockingFirst() {
        BlockingFirstSubscriber<T> s = new BlockingFirstSubscriber<>();
        subscribe(s);
        T v = s.blockingGet();
        if (v != null) {
            return v;
        }
        throw new NoSuchElementException();
    }

    @BackpressureSupport(BackpressureKind.UNBOUNDED_IN)
    @SchedulerSupport("none")
    public final T blockingFirst(T defaultItem) {
        BlockingFirstSubscriber<T> s = new BlockingFirstSubscriber<>();
        subscribe(s);
        T v = s.blockingGet();
        return v != null ? v : defaultItem;
    }

    @BackpressureSupport(BackpressureKind.UNBOUNDED_IN)
    @SchedulerSupport("none")
    public final void blockingForEach(Consumer<? super T> onNext) {
        Iterator<T> it2 = blockingIterable().iterator();
        while (it2.hasNext()) {
            try {
                onNext.accept(it2.next());
            } catch (Throwable e) {
                Exceptions.throwIfFatal(e);
                ((Disposable) it2).dispose();
                throw ExceptionHelper.wrapOrThrow(e);
            }
        }
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public final Iterable<T> blockingIterable() {
        return blockingIterable(bufferSize());
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public final Iterable<T> blockingIterable(int bufferSize) {
        ObjectHelper.verifyPositive(bufferSize, "bufferSize");
        return new BlockingFlowableIterable(this, bufferSize);
    }

    @BackpressureSupport(BackpressureKind.UNBOUNDED_IN)
    @SchedulerSupport("none")
    public final T blockingLast() {
        BlockingLastSubscriber<T> s = new BlockingLastSubscriber<>();
        subscribe(s);
        T v = s.blockingGet();
        if (v != null) {
            return v;
        }
        throw new NoSuchElementException();
    }

    @BackpressureSupport(BackpressureKind.UNBOUNDED_IN)
    @SchedulerSupport("none")
    public final T blockingLast(T defaultItem) {
        BlockingLastSubscriber<T> s = new BlockingLastSubscriber<>();
        subscribe(s);
        T v = s.blockingGet();
        return v != null ? v : defaultItem;
    }

    @BackpressureSupport(BackpressureKind.UNBOUNDED_IN)
    @SchedulerSupport("none")
    public final Iterable<T> blockingLatest() {
        return new BlockingFlowableLatest(this);
    }

    @BackpressureSupport(BackpressureKind.UNBOUNDED_IN)
    @SchedulerSupport("none")
    public final Iterable<T> blockingMostRecent(T initialItem) {
        return new BlockingFlowableMostRecent(this, initialItem);
    }

    @BackpressureSupport(BackpressureKind.UNBOUNDED_IN)
    @SchedulerSupport("none")
    public final Iterable<T> blockingNext() {
        return new BlockingFlowableNext(this);
    }

    @BackpressureSupport(BackpressureKind.UNBOUNDED_IN)
    @SchedulerSupport("none")
    public final T blockingSingle() {
        return singleOrError().blockingGet();
    }

    @BackpressureSupport(BackpressureKind.UNBOUNDED_IN)
    @SchedulerSupport("none")
    public final T blockingSingle(T defaultItem) {
        return single(defaultItem).blockingGet();
    }

    @BackpressureSupport(BackpressureKind.UNBOUNDED_IN)
    @SchedulerSupport("none")
    public final Future<T> toFuture() {
        return (Future) subscribeWith(new FutureSubscriber());
    }

    @BackpressureSupport(BackpressureKind.UNBOUNDED_IN)
    @SchedulerSupport("none")
    public final void blockingSubscribe() {
        FlowableBlockingSubscribe.subscribe(this);
    }

    @BackpressureSupport(BackpressureKind.UNBOUNDED_IN)
    @SchedulerSupport("none")
    public final void blockingSubscribe(Consumer<? super T> onNext) {
        FlowableBlockingSubscribe.subscribe(this, onNext, Functions.ERROR_CONSUMER, Functions.EMPTY_ACTION);
    }

    @BackpressureSupport(BackpressureKind.UNBOUNDED_IN)
    @SchedulerSupport("none")
    public final void blockingSubscribe(Consumer<? super T> onNext, Consumer<? super Throwable> onError) {
        FlowableBlockingSubscribe.subscribe(this, onNext, onError, Functions.EMPTY_ACTION);
    }

    @BackpressureSupport(BackpressureKind.UNBOUNDED_IN)
    @SchedulerSupport("none")
    public final void blockingSubscribe(Consumer<? super T> onNext, Consumer<? super Throwable> onError, Action onComplete) {
        FlowableBlockingSubscribe.subscribe(this, onNext, onError, onComplete);
    }

    @BackpressureSupport(BackpressureKind.SPECIAL)
    @SchedulerSupport("none")
    public final void blockingSubscribe(Subscriber<? super T> subscriber) {
        FlowableBlockingSubscribe.subscribe(this, subscriber);
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public final Flowable<List<T>> buffer(int count) {
        return buffer(count, count);
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public final Flowable<List<T>> buffer(int count, int skip) {
        return buffer(count, skip, ArrayListSupplier.asCallable());
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public final <U extends Collection<? super T>> Flowable<U> buffer(int count, int skip, Callable<U> bufferSupplier) {
        ObjectHelper.verifyPositive(count, "count");
        ObjectHelper.verifyPositive(skip, "skip");
        ObjectHelper.requireNonNull(bufferSupplier, "bufferSupplier is null");
        return RxJavaPlugins.onAssembly(new FlowableBuffer(this, count, skip, bufferSupplier));
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public final <U extends Collection<? super T>> Flowable<U> buffer(int count, Callable<U> bufferSupplier) {
        return buffer(count, count, bufferSupplier);
    }

    @BackpressureSupport(BackpressureKind.ERROR)
    @SchedulerSupport("io.reactivex:computation")
    public final Flowable<List<T>> buffer(long timespan, long timeskip, TimeUnit unit) {
        return buffer(timespan, timeskip, unit, Schedulers.computation(), ArrayListSupplier.asCallable());
    }

    @BackpressureSupport(BackpressureKind.ERROR)
    @SchedulerSupport("custom")
    public final Flowable<List<T>> buffer(long timespan, long timeskip, TimeUnit unit, Scheduler scheduler) {
        return buffer(timespan, timeskip, unit, scheduler, ArrayListSupplier.asCallable());
    }

    @BackpressureSupport(BackpressureKind.ERROR)
    @SchedulerSupport("custom")
    public final <U extends Collection<? super T>> Flowable<U> buffer(long timespan, long timeskip, TimeUnit unit, Scheduler scheduler, Callable<U> bufferSupplier) {
        ObjectHelper.requireNonNull(unit, "unit is null");
        ObjectHelper.requireNonNull(scheduler, "scheduler is null");
        ObjectHelper.requireNonNull(bufferSupplier, "bufferSupplier is null");
        return RxJavaPlugins.onAssembly(new FlowableBufferTimed(this, timespan, timeskip, unit, scheduler, bufferSupplier, Integer.MAX_VALUE, false));
    }

    @BackpressureSupport(BackpressureKind.ERROR)
    @SchedulerSupport("io.reactivex:computation")
    public final Flowable<List<T>> buffer(long timespan, TimeUnit unit) {
        return buffer(timespan, unit, Schedulers.computation(), Integer.MAX_VALUE);
    }

    @BackpressureSupport(BackpressureKind.ERROR)
    @SchedulerSupport("io.reactivex:computation")
    public final Flowable<List<T>> buffer(long timespan, TimeUnit unit, int count) {
        return buffer(timespan, unit, Schedulers.computation(), count);
    }

    @BackpressureSupport(BackpressureKind.ERROR)
    @SchedulerSupport("custom")
    public final Flowable<List<T>> buffer(long timespan, TimeUnit unit, Scheduler scheduler, int count) {
        return buffer(timespan, unit, scheduler, count, ArrayListSupplier.asCallable(), false);
    }

    @BackpressureSupport(BackpressureKind.ERROR)
    @SchedulerSupport("custom")
    public final <U extends Collection<? super T>> Flowable<U> buffer(long timespan, TimeUnit unit, Scheduler scheduler, int count, Callable<U> bufferSupplier, boolean restartTimerOnMaxSize) {
        ObjectHelper.requireNonNull(unit, "unit is null");
        ObjectHelper.requireNonNull(scheduler, "scheduler is null");
        ObjectHelper.requireNonNull(bufferSupplier, "bufferSupplier is null");
        ObjectHelper.verifyPositive(count, "count");
        return RxJavaPlugins.onAssembly(new FlowableBufferTimed(this, timespan, timespan, unit, scheduler, bufferSupplier, count, restartTimerOnMaxSize));
    }

    @BackpressureSupport(BackpressureKind.ERROR)
    @SchedulerSupport("custom")
    public final Flowable<List<T>> buffer(long timespan, TimeUnit unit, Scheduler scheduler) {
        return buffer(timespan, unit, scheduler, Integer.MAX_VALUE, ArrayListSupplier.asCallable(), false);
    }

    @BackpressureSupport(BackpressureKind.ERROR)
    @SchedulerSupport("none")
    public final <TOpening, TClosing> Flowable<List<T>> buffer(Flowable<? extends TOpening> openingIndicator, Function<? super TOpening, ? extends Publisher<? extends TClosing>> closingIndicator) {
        return buffer(openingIndicator, closingIndicator, ArrayListSupplier.asCallable());
    }

    @BackpressureSupport(BackpressureKind.ERROR)
    @SchedulerSupport("none")
    public final <TOpening, TClosing, U extends Collection<? super T>> Flowable<U> buffer(Flowable<? extends TOpening> openingIndicator, Function<? super TOpening, ? extends Publisher<? extends TClosing>> closingIndicator, Callable<U> bufferSupplier) {
        ObjectHelper.requireNonNull(openingIndicator, "openingIndicator is null");
        ObjectHelper.requireNonNull(closingIndicator, "closingIndicator is null");
        ObjectHelper.requireNonNull(bufferSupplier, "bufferSupplier is null");
        return RxJavaPlugins.onAssembly(new FlowableBufferBoundary(this, openingIndicator, closingIndicator, bufferSupplier));
    }

    @BackpressureSupport(BackpressureKind.ERROR)
    @SchedulerSupport("none")
    public final <B> Flowable<List<T>> buffer(Publisher<B> boundaryIndicator) {
        return buffer(boundaryIndicator, ArrayListSupplier.asCallable());
    }

    @BackpressureSupport(BackpressureKind.ERROR)
    @SchedulerSupport("none")
    public final <B> Flowable<List<T>> buffer(Publisher<B> boundaryIndicator, int initialCapacity) {
        return buffer(boundaryIndicator, Functions.createArrayList(initialCapacity));
    }

    @BackpressureSupport(BackpressureKind.ERROR)
    @SchedulerSupport("none")
    public final <B, U extends Collection<? super T>> Flowable<U> buffer(Publisher<B> boundaryIndicator, Callable<U> bufferSupplier) {
        ObjectHelper.requireNonNull(boundaryIndicator, "boundaryIndicator is null");
        ObjectHelper.requireNonNull(bufferSupplier, "bufferSupplier is null");
        return RxJavaPlugins.onAssembly(new FlowableBufferExactBoundary(this, boundaryIndicator, bufferSupplier));
    }

    @BackpressureSupport(BackpressureKind.ERROR)
    @SchedulerSupport("none")
    public final <B> Flowable<List<T>> buffer(Callable<? extends Publisher<B>> boundaryIndicatorSupplier) {
        return buffer(boundaryIndicatorSupplier, ArrayListSupplier.asCallable());
    }

    @BackpressureSupport(BackpressureKind.ERROR)
    @SchedulerSupport("none")
    public final <B, U extends Collection<? super T>> Flowable<U> buffer(Callable<? extends Publisher<B>> boundaryIndicatorSupplier, Callable<U> bufferSupplier) {
        ObjectHelper.requireNonNull(boundaryIndicatorSupplier, "boundaryIndicatorSupplier is null");
        ObjectHelper.requireNonNull(bufferSupplier, "bufferSupplier is null");
        return RxJavaPlugins.onAssembly(new FlowableBufferBoundarySupplier(this, boundaryIndicatorSupplier, bufferSupplier));
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public final Flowable<T> cache() {
        return cacheWithInitialCapacity(16);
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public final Flowable<T> cacheWithInitialCapacity(int initialCapacity) {
        ObjectHelper.verifyPositive(initialCapacity, "initialCapacity");
        return RxJavaPlugins.onAssembly(new FlowableCache(this, initialCapacity));
    }

    @BackpressureSupport(BackpressureKind.PASS_THROUGH)
    @SchedulerSupport("none")
    public final <U> Flowable<U> cast(Class<U> clazz) {
        ObjectHelper.requireNonNull(clazz, "clazz is null");
        return map(Functions.castFunction(clazz));
    }

    @BackpressureSupport(BackpressureKind.UNBOUNDED_IN)
    @SchedulerSupport("none")
    public final <U> Single<U> collect(Callable<? extends U> initialItemSupplier, BiConsumer<? super U, ? super T> collector) {
        ObjectHelper.requireNonNull(initialItemSupplier, "initialItemSupplier is null");
        ObjectHelper.requireNonNull(collector, "collector is null");
        return RxJavaPlugins.onAssembly(new FlowableCollectSingle(this, initialItemSupplier, collector));
    }

    @BackpressureSupport(BackpressureKind.UNBOUNDED_IN)
    @SchedulerSupport("none")
    public final <U> Single<U> collectInto(U initialItem, BiConsumer<? super U, ? super T> collector) {
        ObjectHelper.requireNonNull(initialItem, "initialItem is null");
        return collect(Functions.justCallable(initialItem), collector);
    }

    @BackpressureSupport(BackpressureKind.PASS_THROUGH)
    @SchedulerSupport("none")
    public final <R> Flowable<R> compose(FlowableTransformer<T, R> composer) {
        return fromPublisher(composer.apply(this));
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public final <R> Flowable<R> concatMap(Function<? super T, ? extends Publisher<? extends R>> mapper) {
        return concatMap(mapper, 2);
    }

    /* JADX WARN: Type inference failed for: r4v0, types: [dji.thirdparty.io.reactivex.functions.Function, java.lang.Object, dji.thirdparty.io.reactivex.functions.Function<? super T, ? extends org.reactivestreams.Publisher<? extends R>>], assign insn: null */
    /* JADX WARNING: Unknown variable types count: 1 */
    @dji.thirdparty.io.reactivex.annotations.BackpressureSupport(dji.thirdparty.io.reactivex.annotations.BackpressureKind.FULL)
    @dji.thirdparty.io.reactivex.annotations.SchedulerSupport("none")
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final <R> dji.thirdparty.io.reactivex.Flowable<R> concatMap(dji.thirdparty.io.reactivex.functions.Function<? super T, ? extends org.reactivestreams.Publisher<? extends R>> r4, int r5) {
        /*
            r3 = this;
            java.lang.String r1 = "mapper is null"
            dji.thirdparty.io.reactivex.internal.functions.ObjectHelper.requireNonNull(r4, r1)
            boolean r1 = r3 instanceof dji.thirdparty.io.reactivex.internal.fuseable.ScalarCallable
            if (r1 == 0) goto L_0x001c
            dji.thirdparty.io.reactivex.internal.fuseable.ScalarCallable r3 = (dji.thirdparty.io.reactivex.internal.fuseable.ScalarCallable) r3
            java.lang.Object r0 = r3.call()
            if (r0 != 0) goto L_0x0017
            dji.thirdparty.io.reactivex.Flowable r1 = empty()
        L_0x0016:
            return r1
        L_0x0017:
            dji.thirdparty.io.reactivex.Flowable r1 = dji.thirdparty.io.reactivex.internal.operators.flowable.FlowableScalarXMap.scalarXMap(r0, r4)
            goto L_0x0016
        L_0x001c:
            java.lang.String r1 = "prefetch"
            dji.thirdparty.io.reactivex.internal.functions.ObjectHelper.verifyPositive(r5, r1)
            dji.thirdparty.io.reactivex.internal.operators.flowable.FlowableConcatMap r1 = new dji.thirdparty.io.reactivex.internal.operators.flowable.FlowableConcatMap
            dji.thirdparty.io.reactivex.internal.util.ErrorMode r2 = dji.thirdparty.io.reactivex.internal.util.ErrorMode.IMMEDIATE
            r1.<init>(r3, r4, r5, r2)
            dji.thirdparty.io.reactivex.Flowable r1 = dji.thirdparty.io.reactivex.plugins.RxJavaPlugins.onAssembly(r1)
            goto L_0x0016
        */
        throw new UnsupportedOperationException("Method not decompiled: dji.thirdparty.io.reactivex.Flowable.concatMap(dji.thirdparty.io.reactivex.functions.Function, int):dji.thirdparty.io.reactivex.Flowable");
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public final <R> Flowable<R> concatMapDelayError(Function<? super T, ? extends Publisher<? extends R>> mapper) {
        return concatMapDelayError(mapper, 2, true);
    }

    /* JADX WARN: Type inference failed for: r4v0, types: [dji.thirdparty.io.reactivex.functions.Function, java.lang.Object, dji.thirdparty.io.reactivex.functions.Function<? super T, ? extends org.reactivestreams.Publisher<? extends R>>], assign insn: null */
    /* JADX WARNING: Unknown variable types count: 1 */
    @dji.thirdparty.io.reactivex.annotations.BackpressureSupport(dji.thirdparty.io.reactivex.annotations.BackpressureKind.FULL)
    @dji.thirdparty.io.reactivex.annotations.SchedulerSupport("none")
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final <R> dji.thirdparty.io.reactivex.Flowable<R> concatMapDelayError(dji.thirdparty.io.reactivex.functions.Function<? super T, ? extends org.reactivestreams.Publisher<? extends R>> r4, int r5, boolean r6) {
        /*
            r3 = this;
            java.lang.String r1 = "mapper is null"
            dji.thirdparty.io.reactivex.internal.functions.ObjectHelper.requireNonNull(r4, r1)
            boolean r1 = r3 instanceof dji.thirdparty.io.reactivex.internal.fuseable.ScalarCallable
            if (r1 == 0) goto L_0x001c
            dji.thirdparty.io.reactivex.internal.fuseable.ScalarCallable r3 = (dji.thirdparty.io.reactivex.internal.fuseable.ScalarCallable) r3
            java.lang.Object r0 = r3.call()
            if (r0 != 0) goto L_0x0017
            dji.thirdparty.io.reactivex.Flowable r1 = empty()
        L_0x0016:
            return r1
        L_0x0017:
            dji.thirdparty.io.reactivex.Flowable r1 = dji.thirdparty.io.reactivex.internal.operators.flowable.FlowableScalarXMap.scalarXMap(r0, r4)
            goto L_0x0016
        L_0x001c:
            java.lang.String r1 = "prefetch"
            dji.thirdparty.io.reactivex.internal.functions.ObjectHelper.verifyPositive(r5, r1)
            dji.thirdparty.io.reactivex.internal.operators.flowable.FlowableConcatMap r2 = new dji.thirdparty.io.reactivex.internal.operators.flowable.FlowableConcatMap
            if (r6 == 0) goto L_0x0030
            dji.thirdparty.io.reactivex.internal.util.ErrorMode r1 = dji.thirdparty.io.reactivex.internal.util.ErrorMode.END
        L_0x0028:
            r2.<init>(r3, r4, r5, r1)
            dji.thirdparty.io.reactivex.Flowable r1 = dji.thirdparty.io.reactivex.plugins.RxJavaPlugins.onAssembly(r2)
            goto L_0x0016
        L_0x0030:
            dji.thirdparty.io.reactivex.internal.util.ErrorMode r1 = dji.thirdparty.io.reactivex.internal.util.ErrorMode.BOUNDARY
            goto L_0x0028
        */
        throw new UnsupportedOperationException("Method not decompiled: dji.thirdparty.io.reactivex.Flowable.concatMapDelayError(dji.thirdparty.io.reactivex.functions.Function, int, boolean):dji.thirdparty.io.reactivex.Flowable");
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public final <R> Flowable<R> concatMapEager(Function<? super T, ? extends Publisher<? extends R>> mapper) {
        return concatMapEager(mapper, bufferSize(), bufferSize());
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public final <R> Flowable<R> concatMapEager(Function<? super T, ? extends Publisher<? extends R>> mapper, int maxConcurrency, int prefetch) {
        ObjectHelper.verifyPositive(maxConcurrency, "maxConcurrency");
        ObjectHelper.verifyPositive(prefetch, "prefetch");
        return RxJavaPlugins.onAssembly(new FlowableConcatMapEager(this, mapper, maxConcurrency, prefetch, ErrorMode.IMMEDIATE));
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public final <R> Flowable<R> concatMapEagerDelayError(Function<? super T, ? extends Publisher<? extends R>> mapper, boolean tillTheEnd) {
        return concatMapEagerDelayError(mapper, bufferSize(), bufferSize(), tillTheEnd);
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public final <R> Flowable<R> concatMapEagerDelayError(Function<? super T, ? extends Publisher<? extends R>> mapper, int maxConcurrency, int prefetch, boolean tillTheEnd) {
        return RxJavaPlugins.onAssembly(new FlowableConcatMapEager(this, mapper, maxConcurrency, prefetch, tillTheEnd ? ErrorMode.END : ErrorMode.BOUNDARY));
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public final <U> Flowable<U> concatMapIterable(Function<? super T, ? extends Iterable<? extends U>> mapper) {
        return concatMapIterable(mapper, 2);
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public final <U> Flowable<U> concatMapIterable(Function<? super T, ? extends Iterable<? extends U>> mapper, int prefetch) {
        ObjectHelper.requireNonNull(mapper, "mapper is null");
        ObjectHelper.verifyPositive(prefetch, "prefetch");
        return RxJavaPlugins.onAssembly(new FlowableFlattenIterable(this, mapper, prefetch));
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public final Flowable<T> concatWith(Publisher<? extends T> other) {
        ObjectHelper.requireNonNull(other, "other is null");
        return concat(this, other);
    }

    @BackpressureSupport(BackpressureKind.UNBOUNDED_IN)
    @SchedulerSupport("none")
    public final Single<Boolean> contains(Object item) {
        ObjectHelper.requireNonNull(item, "item is null");
        return any(Functions.equalsWith(item));
    }

    @BackpressureSupport(BackpressureKind.UNBOUNDED_IN)
    @SchedulerSupport("none")
    public final Single<Long> count() {
        return RxJavaPlugins.onAssembly(new FlowableCountSingle(this));
    }

    @BackpressureSupport(BackpressureKind.ERROR)
    @SchedulerSupport("none")
    public final <U> Flowable<T> debounce(Function<? super T, ? extends Publisher<U>> debounceIndicator) {
        ObjectHelper.requireNonNull(debounceIndicator, "debounceIndicator is null");
        return RxJavaPlugins.onAssembly(new FlowableDebounce(this, debounceIndicator));
    }

    @BackpressureSupport(BackpressureKind.ERROR)
    @SchedulerSupport("io.reactivex:computation")
    public final Flowable<T> debounce(long timeout, TimeUnit unit) {
        return debounce(timeout, unit, Schedulers.computation());
    }

    @BackpressureSupport(BackpressureKind.ERROR)
    @SchedulerSupport("custom")
    public final Flowable<T> debounce(long timeout, TimeUnit unit, Scheduler scheduler) {
        ObjectHelper.requireNonNull(unit, "unit is null");
        ObjectHelper.requireNonNull(scheduler, "scheduler is null");
        return RxJavaPlugins.onAssembly(new FlowableDebounceTimed(this, timeout, unit, scheduler));
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public final Flowable<T> defaultIfEmpty(T defaultItem) {
        ObjectHelper.requireNonNull(defaultItem, "item is null");
        return switchIfEmpty(just(defaultItem));
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public final <U> Flowable<T> delay(Function<? super T, ? extends Publisher<U>> itemDelayIndicator) {
        ObjectHelper.requireNonNull(itemDelayIndicator, "itemDelayIndicator is null");
        return flatMap(FlowableInternalHelper.itemDelay(itemDelayIndicator));
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("io.reactivex:computation")
    public final Flowable<T> delay(long delay, TimeUnit unit) {
        return delay(delay, unit, Schedulers.computation(), false);
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("io.reactivex:computation")
    public final Flowable<T> delay(long delay, TimeUnit unit, boolean delayError) {
        return delay(delay, unit, Schedulers.computation(), delayError);
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("custom")
    public final Flowable<T> delay(long delay, TimeUnit unit, Scheduler scheduler) {
        return delay(delay, unit, scheduler, false);
    }

    /* JADX DEBUG: Failed to find minimal casts for resolve overloaded methods, cast all args instead
     method: ClspMth{java.lang.Math.max(long, long):long}
     arg types: [int, long]
     candidates:
      ClspMth{java.lang.Math.max(double, double):double}
      ClspMth{java.lang.Math.max(int, int):int}
      ClspMth{java.lang.Math.max(float, float):float}
      ClspMth{java.lang.Math.max(long, long):long} */
    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("custom")
    public final Flowable<T> delay(long delay, TimeUnit unit, Scheduler scheduler, boolean delayError) {
        ObjectHelper.requireNonNull(unit, "unit is null");
        ObjectHelper.requireNonNull(scheduler, "scheduler is null");
        return RxJavaPlugins.onAssembly(new FlowableDelay(this, Math.max(0L, delay), unit, scheduler, delayError));
    }

    /* JADX WARN: Type inference failed for: r2v0, types: [org.reactivestreams.Publisher<U>, org.reactivestreams.Publisher], assign insn: null */
    /* JADX WARNING: Unknown variable types count: 1 */
    @dji.thirdparty.io.reactivex.annotations.BackpressureSupport(dji.thirdparty.io.reactivex.annotations.BackpressureKind.FULL)
    @dji.thirdparty.io.reactivex.annotations.SchedulerSupport("none")
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final <U, V> dji.thirdparty.io.reactivex.Flowable<T> delay(org.reactivestreams.Publisher<U> r2, dji.thirdparty.io.reactivex.functions.Function<? super T, ? extends org.reactivestreams.Publisher<V>> r3) {
        /*
            r1 = this;
            dji.thirdparty.io.reactivex.Flowable r0 = r1.delaySubscription(r2)
            dji.thirdparty.io.reactivex.Flowable r0 = r0.delay(r3)
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: dji.thirdparty.io.reactivex.Flowable.delay(org.reactivestreams.Publisher, dji.thirdparty.io.reactivex.functions.Function):dji.thirdparty.io.reactivex.Flowable");
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public final <U> Flowable<T> delaySubscription(Publisher<U> subscriptionIndicator) {
        ObjectHelper.requireNonNull(subscriptionIndicator, "subscriptionIndicator is null");
        return RxJavaPlugins.onAssembly(new FlowableDelaySubscriptionOther(this, subscriptionIndicator));
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("io.reactivex:computation")
    public final Flowable<T> delaySubscription(long delay, TimeUnit unit) {
        return delaySubscription(delay, unit, Schedulers.computation());
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("custom")
    public final Flowable<T> delaySubscription(long delay, TimeUnit unit, Scheduler scheduler) {
        return delaySubscription(timer(delay, unit, scheduler));
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public final <T2> Flowable<T2> dematerialize() {
        return RxJavaPlugins.onAssembly(new FlowableDematerialize(this));
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public final Flowable<T> distinct() {
        return distinct(Functions.identity(), Functions.createHashSet());
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public final <K> Flowable<T> distinct(Function<? super T, K> keySelector) {
        return distinct(keySelector, Functions.createHashSet());
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public final <K> Flowable<T> distinct(Function<? super T, K> keySelector, Callable<? extends Collection<? super K>> collectionSupplier) {
        ObjectHelper.requireNonNull(keySelector, "keySelector is null");
        ObjectHelper.requireNonNull(collectionSupplier, "collectionSupplier is null");
        return RxJavaPlugins.onAssembly(new FlowableDistinct(this, keySelector, collectionSupplier));
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public final Flowable<T> distinctUntilChanged() {
        return distinctUntilChanged(Functions.identity());
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public final <K> Flowable<T> distinctUntilChanged(Function<? super T, K> keySelector) {
        ObjectHelper.requireNonNull(keySelector, "keySelector is null");
        return RxJavaPlugins.onAssembly(new FlowableDistinctUntilChanged(this, keySelector, ObjectHelper.equalsPredicate()));
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public final Flowable<T> distinctUntilChanged(BiPredicate<? super T, ? super T> comparer) {
        ObjectHelper.requireNonNull(comparer, "comparer is null");
        return RxJavaPlugins.onAssembly(new FlowableDistinctUntilChanged(this, Functions.identity(), comparer));
    }

    @BackpressureSupport(BackpressureKind.PASS_THROUGH)
    @SchedulerSupport("none")
    public final Flowable<T> doAfterTerminate(Action onAfterTerminate) {
        return doOnEach(Functions.emptyConsumer(), Functions.emptyConsumer(), Functions.EMPTY_ACTION, onAfterTerminate);
    }

    @BackpressureSupport(BackpressureKind.PASS_THROUGH)
    @SchedulerSupport("none")
    public final Flowable<T> doOnCancel(Action onCancel) {
        return doOnLifecycle(Functions.emptyConsumer(), Functions.EMPTY_LONG_CONSUMER, onCancel);
    }

    @BackpressureSupport(BackpressureKind.PASS_THROUGH)
    @SchedulerSupport("none")
    public final Flowable<T> doOnComplete(Action onComplete) {
        return doOnEach(Functions.emptyConsumer(), Functions.emptyConsumer(), onComplete, Functions.EMPTY_ACTION);
    }

    @BackpressureSupport(BackpressureKind.PASS_THROUGH)
    @SchedulerSupport("none")
    private Flowable<T> doOnEach(Consumer<? super T> onNext, Consumer<? super Throwable> onError, Action onComplete, Action onAfterTerminate) {
        ObjectHelper.requireNonNull(onNext, "onNext is null");
        ObjectHelper.requireNonNull(onError, "onError is null");
        ObjectHelper.requireNonNull(onComplete, "onComplete is null");
        ObjectHelper.requireNonNull(onAfterTerminate, "onAfterTerminate is null");
        return RxJavaPlugins.onAssembly(new FlowableDoOnEach(this, onNext, onError, onComplete, onAfterTerminate));
    }

    @BackpressureSupport(BackpressureKind.PASS_THROUGH)
    @SchedulerSupport("none")
    public final Flowable<T> doOnEach(Consumer<? super Notification<T>> onNotification) {
        ObjectHelper.requireNonNull(onNotification, "consumer is null");
        return doOnEach(Functions.notificationOnNext(onNotification), Functions.notificationOnError(onNotification), Functions.notificationOnComplete(onNotification), Functions.EMPTY_ACTION);
    }

    @BackpressureSupport(BackpressureKind.PASS_THROUGH)
    @SchedulerSupport("none")
    public final Flowable<T> doOnEach(Subscriber<? super T> subscriber) {
        ObjectHelper.requireNonNull(subscriber, "subscriber is null");
        return doOnEach(FlowableInternalHelper.subscriberOnNext(subscriber), FlowableInternalHelper.subscriberOnError(subscriber), FlowableInternalHelper.subscriberOnComplete(subscriber), Functions.EMPTY_ACTION);
    }

    @BackpressureSupport(BackpressureKind.PASS_THROUGH)
    @SchedulerSupport("none")
    public final Flowable<T> doOnError(Consumer<? super Throwable> onError) {
        return doOnEach(Functions.emptyConsumer(), onError, Functions.EMPTY_ACTION, Functions.EMPTY_ACTION);
    }

    @BackpressureSupport(BackpressureKind.PASS_THROUGH)
    @SchedulerSupport("none")
    public final Flowable<T> doOnLifecycle(Consumer<? super Subscription> onSubscribe, LongConsumer onRequest, Action onCancel) {
        ObjectHelper.requireNonNull(onSubscribe, "onSubscribe is null");
        ObjectHelper.requireNonNull(onRequest, "onRequest is null");
        ObjectHelper.requireNonNull(onCancel, "onCancel is null");
        return RxJavaPlugins.onAssembly(new FlowableDoOnLifecycle(this, onSubscribe, onRequest, onCancel));
    }

    @BackpressureSupport(BackpressureKind.PASS_THROUGH)
    @SchedulerSupport("none")
    public final Flowable<T> doOnNext(Consumer<? super T> onNext) {
        return doOnEach(onNext, Functions.emptyConsumer(), Functions.EMPTY_ACTION, Functions.EMPTY_ACTION);
    }

    @BackpressureSupport(BackpressureKind.PASS_THROUGH)
    @SchedulerSupport("none")
    public final Flowable<T> doOnRequest(LongConsumer onRequest) {
        return doOnLifecycle(Functions.emptyConsumer(), onRequest, Functions.EMPTY_ACTION);
    }

    @BackpressureSupport(BackpressureKind.PASS_THROUGH)
    @SchedulerSupport("none")
    public final Flowable<T> doOnSubscribe(Consumer<? super Subscription> onSubscribe) {
        return doOnLifecycle(onSubscribe, Functions.EMPTY_LONG_CONSUMER, Functions.EMPTY_ACTION);
    }

    @BackpressureSupport(BackpressureKind.PASS_THROUGH)
    @SchedulerSupport("none")
    public final Flowable<T> doOnTerminate(Action onTerminate) {
        return doOnEach(Functions.emptyConsumer(), Functions.actionConsumer(onTerminate), onTerminate, Functions.EMPTY_ACTION);
    }

    @BackpressureSupport(BackpressureKind.UNBOUNDED_IN)
    @SchedulerSupport("none")
    public final Maybe<T> elementAt(long index) {
        if (index >= 0) {
            return RxJavaPlugins.onAssembly(new FlowableElementAtMaybe(this, index));
        }
        throw new IndexOutOfBoundsException("index >= 0 required but it was " + index);
    }

    @BackpressureSupport(BackpressureKind.UNBOUNDED_IN)
    @SchedulerSupport("none")
    public final Single<T> elementAt(long index, T defaultItem) {
        if (index < 0) {
            throw new IndexOutOfBoundsException("index >= 0 required but it was " + index);
        }
        ObjectHelper.requireNonNull(defaultItem, "defaultItem is null");
        return RxJavaPlugins.onAssembly(new FlowableElementAtSingle(this, index, defaultItem));
    }

    @BackpressureSupport(BackpressureKind.UNBOUNDED_IN)
    @SchedulerSupport("none")
    public final Single<T> elementAtOrError(long index) {
        if (index >= 0) {
            return RxJavaPlugins.onAssembly(new FlowableElementAtSingle(this, index, null));
        }
        throw new IndexOutOfBoundsException("index >= 0 required but it was " + index);
    }

    @BackpressureSupport(BackpressureKind.PASS_THROUGH)
    @SchedulerSupport("none")
    public final Flowable<T> filter(Predicate<? super T> predicate) {
        ObjectHelper.requireNonNull(predicate, "predicate is null");
        return RxJavaPlugins.onAssembly(new FlowableFilter(this, predicate));
    }

    @BackpressureSupport(BackpressureKind.SPECIAL)
    @SchedulerSupport("none")
    public final Maybe<T> firstElement() {
        return elementAt(0);
    }

    @BackpressureSupport(BackpressureKind.SPECIAL)
    @SchedulerSupport("none")
    public final Single<T> first(T defaultItem) {
        return elementAt(0, defaultItem);
    }

    @BackpressureSupport(BackpressureKind.SPECIAL)
    @SchedulerSupport("none")
    public final Single<T> firstOrError() {
        return elementAtOrError(0);
    }

    /* JADX DEBUG: Failed to find minimal casts for resolve overloaded methods, cast all args instead
     method: dji.thirdparty.io.reactivex.Flowable.flatMap(dji.thirdparty.io.reactivex.functions.Function, boolean, int, int):dji.thirdparty.io.reactivex.Flowable<R>
     arg types: [dji.thirdparty.io.reactivex.functions.Function<? super T, ? extends org.reactivestreams.Publisher<? extends R>>, int, int, int]
     candidates:
      dji.thirdparty.io.reactivex.Flowable.flatMap(dji.thirdparty.io.reactivex.functions.Function, dji.thirdparty.io.reactivex.functions.BiFunction, boolean, int):dji.thirdparty.io.reactivex.Flowable<R>
      dji.thirdparty.io.reactivex.Flowable.flatMap(dji.thirdparty.io.reactivex.functions.Function, dji.thirdparty.io.reactivex.functions.Function, java.util.concurrent.Callable, int):dji.thirdparty.io.reactivex.Flowable<R>
      dji.thirdparty.io.reactivex.Flowable.flatMap(dji.thirdparty.io.reactivex.functions.Function, boolean, int, int):dji.thirdparty.io.reactivex.Flowable<R> */
    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public final <R> Flowable<R> flatMap(Function<? super T, ? extends Publisher<? extends R>> mapper) {
        return flatMap((Function) mapper, false, bufferSize(), bufferSize());
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public final <R> Flowable<R> flatMap(Function<? super T, ? extends Publisher<? extends R>> mapper, boolean delayErrors) {
        return flatMap(mapper, delayErrors, bufferSize(), bufferSize());
    }

    /* JADX DEBUG: Failed to find minimal casts for resolve overloaded methods, cast all args instead
     method: dji.thirdparty.io.reactivex.Flowable.flatMap(dji.thirdparty.io.reactivex.functions.Function, boolean, int, int):dji.thirdparty.io.reactivex.Flowable<R>
     arg types: [dji.thirdparty.io.reactivex.functions.Function<? super T, ? extends org.reactivestreams.Publisher<? extends R>>, int, int, int]
     candidates:
      dji.thirdparty.io.reactivex.Flowable.flatMap(dji.thirdparty.io.reactivex.functions.Function, dji.thirdparty.io.reactivex.functions.BiFunction, boolean, int):dji.thirdparty.io.reactivex.Flowable<R>
      dji.thirdparty.io.reactivex.Flowable.flatMap(dji.thirdparty.io.reactivex.functions.Function, dji.thirdparty.io.reactivex.functions.Function, java.util.concurrent.Callable, int):dji.thirdparty.io.reactivex.Flowable<R>
      dji.thirdparty.io.reactivex.Flowable.flatMap(dji.thirdparty.io.reactivex.functions.Function, boolean, int, int):dji.thirdparty.io.reactivex.Flowable<R> */
    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public final <R> Flowable<R> flatMap(Function<? super T, ? extends Publisher<? extends R>> mapper, int maxConcurrency) {
        return flatMap((Function) mapper, false, maxConcurrency, bufferSize());
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public final <R> Flowable<R> flatMap(Function<? super T, ? extends Publisher<? extends R>> mapper, boolean delayErrors, int maxConcurrency) {
        return flatMap(mapper, delayErrors, maxConcurrency, bufferSize());
    }

    /* JADX WARN: Type inference failed for: r8v0, types: [dji.thirdparty.io.reactivex.functions.Function, java.lang.Object, dji.thirdparty.io.reactivex.functions.Function<? super T, ? extends org.reactivestreams.Publisher<? extends R>>], assign insn: null */
    /* JADX WARNING: Unknown variable types count: 1 */
    @dji.thirdparty.io.reactivex.annotations.BackpressureSupport(dji.thirdparty.io.reactivex.annotations.BackpressureKind.FULL)
    @dji.thirdparty.io.reactivex.annotations.SchedulerSupport("none")
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final <R> dji.thirdparty.io.reactivex.Flowable<R> flatMap(dji.thirdparty.io.reactivex.functions.Function<? super T, ? extends org.reactivestreams.Publisher<? extends R>> r8, boolean r9, int r10, int r11) {
        /*
            r7 = this;
            java.lang.String r0 = "mapper is null"
            dji.thirdparty.io.reactivex.internal.functions.ObjectHelper.requireNonNull(r8, r0)
            boolean r0 = r7 instanceof dji.thirdparty.io.reactivex.internal.fuseable.ScalarCallable
            if (r0 == 0) goto L_0x001c
            dji.thirdparty.io.reactivex.internal.fuseable.ScalarCallable r7 = (dji.thirdparty.io.reactivex.internal.fuseable.ScalarCallable) r7
            java.lang.Object r6 = r7.call()
            if (r6 != 0) goto L_0x0017
            dji.thirdparty.io.reactivex.Flowable r0 = empty()
        L_0x0016:
            return r0
        L_0x0017:
            dji.thirdparty.io.reactivex.Flowable r0 = dji.thirdparty.io.reactivex.internal.operators.flowable.FlowableScalarXMap.scalarXMap(r6, r8)
            goto L_0x0016
        L_0x001c:
            java.lang.String r0 = "maxConcurrency"
            dji.thirdparty.io.reactivex.internal.functions.ObjectHelper.verifyPositive(r10, r0)
            java.lang.String r0 = "bufferSize"
            dji.thirdparty.io.reactivex.internal.functions.ObjectHelper.verifyPositive(r11, r0)
            dji.thirdparty.io.reactivex.internal.operators.flowable.FlowableFlatMap r0 = new dji.thirdparty.io.reactivex.internal.operators.flowable.FlowableFlatMap
            r1 = r7
            r2 = r8
            r3 = r9
            r4 = r10
            r5 = r11
            r0.<init>(r1, r2, r3, r4, r5)
            dji.thirdparty.io.reactivex.Flowable r0 = dji.thirdparty.io.reactivex.plugins.RxJavaPlugins.onAssembly(r0)
            goto L_0x0016
        */
        throw new UnsupportedOperationException("Method not decompiled: dji.thirdparty.io.reactivex.Flowable.flatMap(dji.thirdparty.io.reactivex.functions.Function, boolean, int, int):dji.thirdparty.io.reactivex.Flowable");
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public final <R> Flowable<R> flatMap(Function<? super T, ? extends Publisher<? extends R>> onNextMapper, Function<? super Throwable, ? extends Publisher<? extends R>> onErrorMapper, Callable<? extends Publisher<? extends R>> onCompleteSupplier) {
        ObjectHelper.requireNonNull(onNextMapper, "onNextMapper is null");
        ObjectHelper.requireNonNull(onErrorMapper, "onErrorMapper is null");
        ObjectHelper.requireNonNull(onCompleteSupplier, "onCompleteSupplier is null");
        return merge(new FlowableMapNotification(this, onNextMapper, onErrorMapper, onCompleteSupplier));
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public final <R> Flowable<R> flatMap(Function<? super T, ? extends Publisher<? extends R>> onNextMapper, Function<Throwable, ? extends Publisher<? extends R>> onErrorMapper, Callable<? extends Publisher<? extends R>> onCompleteSupplier, int maxConcurrency) {
        ObjectHelper.requireNonNull(onNextMapper, "onNextMapper is null");
        ObjectHelper.requireNonNull(onErrorMapper, "onErrorMapper is null");
        ObjectHelper.requireNonNull(onCompleteSupplier, "onCompleteSupplier is null");
        return merge(new FlowableMapNotification(this, onNextMapper, onErrorMapper, onCompleteSupplier), maxConcurrency);
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public final <U, R> Flowable<R> flatMap(Function<? super T, ? extends Publisher<? extends U>> mapper, BiFunction<? super T, ? super U, ? extends R> resultSelector) {
        return flatMap(mapper, resultSelector, false, bufferSize(), bufferSize());
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public final <U, R> Flowable<R> flatMap(Function<? super T, ? extends Publisher<? extends U>> mapper, BiFunction<? super T, ? super U, ? extends R> combiner, boolean delayErrors) {
        return flatMap(mapper, combiner, delayErrors, bufferSize(), bufferSize());
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public final <U, R> Flowable<R> flatMap(Function<? super T, ? extends Publisher<? extends U>> mapper, BiFunction<? super T, ? super U, ? extends R> combiner, boolean delayErrors, int maxConcurrency) {
        return flatMap(mapper, combiner, delayErrors, maxConcurrency, bufferSize());
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public final <U, R> Flowable<R> flatMap(Function<? super T, ? extends Publisher<? extends U>> mapper, BiFunction<? super T, ? super U, ? extends R> combiner, boolean delayErrors, int maxConcurrency, int bufferSize) {
        ObjectHelper.requireNonNull(mapper, "mapper is null");
        ObjectHelper.requireNonNull(combiner, "combiner is null");
        return flatMap(FlowableInternalHelper.flatMapWithCombiner(mapper, combiner), delayErrors, maxConcurrency, bufferSize);
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public final <U, R> Flowable<R> flatMap(Function<? super T, ? extends Publisher<? extends U>> mapper, BiFunction<? super T, ? super U, ? extends R> combiner, int maxConcurrency) {
        return flatMap(mapper, combiner, false, maxConcurrency, bufferSize());
    }

    @BackpressureSupport(BackpressureKind.UNBOUNDED_IN)
    @SchedulerSupport("none")
    public final Completable flatMapCompletable(Function<? super T, ? extends CompletableSource> mapper) {
        return flatMapCompletable(mapper, false, Integer.MAX_VALUE);
    }

    @BackpressureSupport(BackpressureKind.UNBOUNDED_IN)
    @SchedulerSupport("none")
    public final Completable flatMapCompletable(Function<? super T, ? extends CompletableSource> mapper, boolean delayErrors, int maxConcurrency) {
        ObjectHelper.requireNonNull(mapper, "mapper is null");
        ObjectHelper.verifyPositive(maxConcurrency, "maxConcurrency");
        return RxJavaPlugins.onAssembly(new FlowableFlatMapCompletableCompletable(this, mapper, delayErrors, maxConcurrency));
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public final <U> Flowable<U> flatMapIterable(Function<? super T, ? extends Iterable<? extends U>> mapper) {
        return flatMapIterable(mapper, bufferSize());
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public final <U> Flowable<U> flatMapIterable(Function<? super T, ? extends Iterable<? extends U>> mapper, int bufferSize) {
        ObjectHelper.requireNonNull(mapper, "mapper is null");
        ObjectHelper.verifyPositive(bufferSize, "bufferSize");
        return RxJavaPlugins.onAssembly(new FlowableFlattenIterable(this, mapper, bufferSize));
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public final <U, V> Flowable<V> flatMapIterable(Function<? super T, ? extends Iterable<? extends U>> mapper, BiFunction<? super T, ? super U, ? extends V> resultSelector) {
        ObjectHelper.requireNonNull(mapper, "mapper is null");
        ObjectHelper.requireNonNull(resultSelector, "resultSelector is null");
        return flatMap(FlowableInternalHelper.flatMapIntoIterable(mapper), resultSelector, false, bufferSize(), bufferSize());
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public final <U, V> Flowable<V> flatMapIterable(Function<? super T, ? extends Iterable<? extends U>> mapper, BiFunction<? super T, ? super U, ? extends V> resultSelector, int prefetch) {
        ObjectHelper.requireNonNull(mapper, "mapper is null");
        ObjectHelper.requireNonNull(resultSelector, "resultSelector is null");
        return flatMap(FlowableInternalHelper.flatMapIntoIterable(mapper), resultSelector, false, bufferSize(), prefetch);
    }

    @BackpressureSupport(BackpressureKind.UNBOUNDED_IN)
    @SchedulerSupport("none")
    public final <R> Flowable<R> flatMapMaybe(Function<? super T, ? extends MaybeSource<? extends R>> mapper) {
        return flatMapMaybe(mapper, false, Integer.MAX_VALUE);
    }

    @BackpressureSupport(BackpressureKind.UNBOUNDED_IN)
    @SchedulerSupport("none")
    public final <R> Flowable<R> flatMapMaybe(Function<? super T, ? extends MaybeSource<? extends R>> mapper, boolean delayErrors, int maxConcurrency) {
        ObjectHelper.requireNonNull(mapper, "mapper is null");
        ObjectHelper.verifyPositive(maxConcurrency, "maxConcurrency");
        return RxJavaPlugins.onAssembly(new FlowableFlatMapMaybe(this, mapper, delayErrors, maxConcurrency));
    }

    @BackpressureSupport(BackpressureKind.UNBOUNDED_IN)
    @SchedulerSupport("none")
    public final <R> Flowable<R> flatMapSingle(Function<? super T, ? extends SingleSource<? extends R>> mapper) {
        return flatMapSingle(mapper, false, Integer.MAX_VALUE);
    }

    @BackpressureSupport(BackpressureKind.UNBOUNDED_IN)
    @SchedulerSupport("none")
    public final <R> Flowable<R> flatMapSingle(Function<? super T, ? extends SingleSource<? extends R>> mapper, boolean delayErrors, int maxConcurrency) {
        ObjectHelper.requireNonNull(mapper, "mapper is null");
        ObjectHelper.verifyPositive(maxConcurrency, "maxConcurrency");
        return RxJavaPlugins.onAssembly(new FlowableFlatMapSingle(this, mapper, delayErrors, maxConcurrency));
    }

    @BackpressureSupport(BackpressureKind.NONE)
    @SchedulerSupport("none")
    public final Disposable forEach(Consumer<? super T> onNext) {
        return subscribe(onNext);
    }

    @BackpressureSupport(BackpressureKind.NONE)
    @SchedulerSupport("none")
    public final Disposable forEachWhile(Predicate<? super T> onNext) {
        return forEachWhile(onNext, Functions.ERROR_CONSUMER, Functions.EMPTY_ACTION);
    }

    @BackpressureSupport(BackpressureKind.NONE)
    @SchedulerSupport("none")
    public final Disposable forEachWhile(Predicate<? super T> onNext, Consumer<? super Throwable> onError) {
        return forEachWhile(onNext, onError, Functions.EMPTY_ACTION);
    }

    @BackpressureSupport(BackpressureKind.NONE)
    @SchedulerSupport("none")
    public final Disposable forEachWhile(Predicate<? super T> onNext, Consumer<? super Throwable> onError, Action onComplete) {
        ObjectHelper.requireNonNull(onNext, "onNext is null");
        ObjectHelper.requireNonNull(onError, "onError is null");
        ObjectHelper.requireNonNull(onComplete, "onComplete is null");
        ForEachWhileSubscriber<T> s = new ForEachWhileSubscriber<>(onNext, onError, onComplete);
        subscribe(s);
        return s;
    }

    /* JADX WARN: Type inference failed for: r4v0, types: [dji.thirdparty.io.reactivex.functions.Function, dji.thirdparty.io.reactivex.functions.Function<? super T, ? extends K>], assign insn: null */
    /* JADX WARNING: Unknown variable types count: 1 */
    @dji.thirdparty.io.reactivex.annotations.BackpressureSupport(dji.thirdparty.io.reactivex.annotations.BackpressureKind.FULL)
    @dji.thirdparty.io.reactivex.annotations.SchedulerSupport("none")
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final <K> dji.thirdparty.io.reactivex.Flowable<dji.thirdparty.io.reactivex.flowables.GroupedFlowable<K, T>> groupBy(dji.thirdparty.io.reactivex.functions.Function<? super T, ? extends K> r4) {
        /*
            r3 = this;
            dji.thirdparty.io.reactivex.functions.Function r0 = dji.thirdparty.io.reactivex.internal.functions.Functions.identity()
            r1 = 0
            int r2 = bufferSize()
            dji.thirdparty.io.reactivex.Flowable r0 = r3.groupBy(r4, r0, r1, r2)
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: dji.thirdparty.io.reactivex.Flowable.groupBy(dji.thirdparty.io.reactivex.functions.Function):dji.thirdparty.io.reactivex.Flowable");
    }

    /* JADX WARN: Type inference failed for: r3v0, types: [dji.thirdparty.io.reactivex.functions.Function, dji.thirdparty.io.reactivex.functions.Function<? super T, ? extends K>], assign insn: null */
    /* JADX WARNING: Unknown variable types count: 1 */
    @dji.thirdparty.io.reactivex.annotations.BackpressureSupport(dji.thirdparty.io.reactivex.annotations.BackpressureKind.FULL)
    @dji.thirdparty.io.reactivex.annotations.SchedulerSupport("none")
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final <K> dji.thirdparty.io.reactivex.Flowable<dji.thirdparty.io.reactivex.flowables.GroupedFlowable<K, T>> groupBy(dji.thirdparty.io.reactivex.functions.Function<? super T, ? extends K> r3, boolean r4) {
        /*
            r2 = this;
            dji.thirdparty.io.reactivex.functions.Function r0 = dji.thirdparty.io.reactivex.internal.functions.Functions.identity()
            int r1 = bufferSize()
            dji.thirdparty.io.reactivex.Flowable r0 = r2.groupBy(r3, r0, r4, r1)
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: dji.thirdparty.io.reactivex.Flowable.groupBy(dji.thirdparty.io.reactivex.functions.Function, boolean):dji.thirdparty.io.reactivex.Flowable");
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public final <K, V> Flowable<GroupedFlowable<K, V>> groupBy(Function<? super T, ? extends K> keySelector, Function<? super T, ? extends V> valueSelector) {
        return groupBy(keySelector, valueSelector, false, bufferSize());
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public final <K, V> Flowable<GroupedFlowable<K, V>> groupBy(Function<? super T, ? extends K> keySelector, Function<? super T, ? extends V> valueSelector, boolean delayError) {
        return groupBy(keySelector, valueSelector, delayError, bufferSize());
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public final <K, V> Flowable<GroupedFlowable<K, V>> groupBy(Function<? super T, ? extends K> keySelector, Function<? super T, ? extends V> valueSelector, boolean delayError, int bufferSize) {
        ObjectHelper.requireNonNull(keySelector, "keySelector is null");
        ObjectHelper.requireNonNull(valueSelector, "valueSelector is null");
        ObjectHelper.verifyPositive(bufferSize, "bufferSize");
        return RxJavaPlugins.onAssembly(new FlowableGroupBy(this, keySelector, valueSelector, bufferSize, delayError));
    }

    @BackpressureSupport(BackpressureKind.ERROR)
    @SchedulerSupport("none")
    public final <TRight, TLeftEnd, TRightEnd, R> Flowable<R> groupJoin(Publisher<? extends TRight> other, Function<? super T, ? extends Publisher<TLeftEnd>> leftEnd, Function<? super TRight, ? extends Publisher<TRightEnd>> rightEnd, BiFunction<? super T, ? super Flowable<TRight>, ? extends R> resultSelector) {
        return RxJavaPlugins.onAssembly(new FlowableGroupJoin(this, other, leftEnd, rightEnd, resultSelector));
    }

    @BackpressureSupport(BackpressureKind.PASS_THROUGH)
    @SchedulerSupport("none")
    public final Flowable<T> hide() {
        return RxJavaPlugins.onAssembly(new FlowableHide(this));
    }

    @BackpressureSupport(BackpressureKind.UNBOUNDED_IN)
    @SchedulerSupport("none")
    public final Completable ignoreElements() {
        return RxJavaPlugins.onAssembly(new FlowableIgnoreElementsCompletable(this));
    }

    @BackpressureSupport(BackpressureKind.UNBOUNDED_IN)
    @SchedulerSupport("none")
    public final Single<Boolean> isEmpty() {
        return all(Functions.alwaysFalse());
    }

    @BackpressureSupport(BackpressureKind.ERROR)
    @SchedulerSupport("none")
    public final <TRight, TLeftEnd, TRightEnd, R> Flowable<R> join(Publisher<? extends TRight> other, Function<? super T, ? extends Publisher<TLeftEnd>> leftEnd, Function<? super TRight, ? extends Publisher<TRightEnd>> rightEnd, BiFunction<? super T, ? super TRight, ? extends R> resultSelector) {
        return RxJavaPlugins.onAssembly(new FlowableJoin(this, other, leftEnd, rightEnd, resultSelector));
    }

    @BackpressureSupport(BackpressureKind.UNBOUNDED_IN)
    @SchedulerSupport("none")
    public final Maybe<T> lastElement() {
        return RxJavaPlugins.onAssembly(new FlowableLastMaybe(this));
    }

    @BackpressureSupport(BackpressureKind.UNBOUNDED_IN)
    @SchedulerSupport("none")
    public final Single<T> last(T defaultItem) {
        ObjectHelper.requireNonNull(defaultItem, "defaultItem");
        return RxJavaPlugins.onAssembly(new FlowableLastSingle(this, defaultItem));
    }

    @BackpressureSupport(BackpressureKind.UNBOUNDED_IN)
    @SchedulerSupport("none")
    public final Single<T> lastOrError() {
        return RxJavaPlugins.onAssembly(new FlowableLastSingle(this, null));
    }

    @BackpressureSupport(BackpressureKind.SPECIAL)
    @SchedulerSupport("none")
    public final <R> Flowable<R> lift(FlowableOperator<? extends R, ? super T> lifter) {
        ObjectHelper.requireNonNull(lifter, "lifter is null");
        return RxJavaPlugins.onAssembly(new FlowableLift(this, lifter));
    }

    @BackpressureSupport(BackpressureKind.PASS_THROUGH)
    @SchedulerSupport("none")
    public final <R> Flowable<R> map(Function<? super T, ? extends R> mapper) {
        ObjectHelper.requireNonNull(mapper, "mapper is null");
        return RxJavaPlugins.onAssembly(new FlowableMap(this, mapper));
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public final Flowable<Notification<T>> materialize() {
        return RxJavaPlugins.onAssembly(new FlowableMaterialize(this));
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public final Flowable<T> mergeWith(Publisher<? extends T> other) {
        ObjectHelper.requireNonNull(other, "other is null");
        return merge(this, other);
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("custom")
    public final Flowable<T> observeOn(Scheduler scheduler) {
        return observeOn(scheduler, false, bufferSize());
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("custom")
    public final Flowable<T> observeOn(Scheduler scheduler, boolean delayError) {
        return observeOn(scheduler, delayError, bufferSize());
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("custom")
    public final Flowable<T> observeOn(Scheduler scheduler, boolean delayError, int bufferSize) {
        ObjectHelper.requireNonNull(scheduler, "scheduler is null");
        ObjectHelper.verifyPositive(bufferSize, "bufferSize");
        return RxJavaPlugins.onAssembly(new FlowableObserveOn(this, scheduler, delayError, bufferSize));
    }

    @BackpressureSupport(BackpressureKind.PASS_THROUGH)
    @SchedulerSupport("none")
    public final <U> Flowable<U> ofType(Class<U> clazz) {
        ObjectHelper.requireNonNull(clazz, "clazz is null");
        return filter(Functions.isInstanceOf(clazz)).cast(clazz);
    }

    /* JADX DEBUG: Failed to find minimal casts for resolve overloaded methods, cast all args instead
     method: dji.thirdparty.io.reactivex.Flowable.onBackpressureBuffer(int, boolean, boolean):dji.thirdparty.io.reactivex.Flowable<T>
     arg types: [int, int, int]
     candidates:
      dji.thirdparty.io.reactivex.Flowable.onBackpressureBuffer(long, dji.thirdparty.io.reactivex.functions.Action, dji.thirdparty.io.reactivex.BackpressureOverflowStrategy):dji.thirdparty.io.reactivex.Flowable<T>
      dji.thirdparty.io.reactivex.Flowable.onBackpressureBuffer(int, boolean, boolean):dji.thirdparty.io.reactivex.Flowable<T> */
    @BackpressureSupport(BackpressureKind.UNBOUNDED_IN)
    @SchedulerSupport("none")
    public final Flowable<T> onBackpressureBuffer() {
        return onBackpressureBuffer(bufferSize(), false, true);
    }

    /* JADX DEBUG: Failed to find minimal casts for resolve overloaded methods, cast all args instead
     method: dji.thirdparty.io.reactivex.Flowable.onBackpressureBuffer(int, boolean, boolean):dji.thirdparty.io.reactivex.Flowable<T>
     arg types: [int, boolean, int]
     candidates:
      dji.thirdparty.io.reactivex.Flowable.onBackpressureBuffer(long, dji.thirdparty.io.reactivex.functions.Action, dji.thirdparty.io.reactivex.BackpressureOverflowStrategy):dji.thirdparty.io.reactivex.Flowable<T>
      dji.thirdparty.io.reactivex.Flowable.onBackpressureBuffer(int, boolean, boolean):dji.thirdparty.io.reactivex.Flowable<T> */
    @BackpressureSupport(BackpressureKind.UNBOUNDED_IN)
    @SchedulerSupport("none")
    public final Flowable<T> onBackpressureBuffer(boolean delayError) {
        return onBackpressureBuffer(bufferSize(), delayError, true);
    }

    /* JADX DEBUG: Failed to find minimal casts for resolve overloaded methods, cast all args instead
     method: dji.thirdparty.io.reactivex.Flowable.onBackpressureBuffer(int, boolean, boolean):dji.thirdparty.io.reactivex.Flowable<T>
     arg types: [int, int, int]
     candidates:
      dji.thirdparty.io.reactivex.Flowable.onBackpressureBuffer(long, dji.thirdparty.io.reactivex.functions.Action, dji.thirdparty.io.reactivex.BackpressureOverflowStrategy):dji.thirdparty.io.reactivex.Flowable<T>
      dji.thirdparty.io.reactivex.Flowable.onBackpressureBuffer(int, boolean, boolean):dji.thirdparty.io.reactivex.Flowable<T> */
    @BackpressureSupport(BackpressureKind.ERROR)
    @SchedulerSupport("none")
    public final Flowable<T> onBackpressureBuffer(int capacity) {
        return onBackpressureBuffer(capacity, false, false);
    }

    /* JADX DEBUG: Failed to find minimal casts for resolve overloaded methods, cast all args instead
     method: dji.thirdparty.io.reactivex.Flowable.onBackpressureBuffer(int, boolean, boolean):dji.thirdparty.io.reactivex.Flowable<T>
     arg types: [int, boolean, int]
     candidates:
      dji.thirdparty.io.reactivex.Flowable.onBackpressureBuffer(long, dji.thirdparty.io.reactivex.functions.Action, dji.thirdparty.io.reactivex.BackpressureOverflowStrategy):dji.thirdparty.io.reactivex.Flowable<T>
      dji.thirdparty.io.reactivex.Flowable.onBackpressureBuffer(int, boolean, boolean):dji.thirdparty.io.reactivex.Flowable<T> */
    @BackpressureSupport(BackpressureKind.ERROR)
    @SchedulerSupport("none")
    public final Flowable<T> onBackpressureBuffer(int capacity, boolean delayError) {
        return onBackpressureBuffer(capacity, delayError, false);
    }

    @BackpressureSupport(BackpressureKind.SPECIAL)
    @SchedulerSupport("none")
    public final Flowable<T> onBackpressureBuffer(int capacity, boolean delayError, boolean unbounded) {
        ObjectHelper.verifyPositive(capacity, "bufferSize");
        return RxJavaPlugins.onAssembly(new FlowableOnBackpressureBuffer(this, capacity, unbounded, delayError, Functions.EMPTY_ACTION));
    }

    @BackpressureSupport(BackpressureKind.SPECIAL)
    @SchedulerSupport("none")
    public final Flowable<T> onBackpressureBuffer(int capacity, boolean delayError, boolean unbounded, Action onOverflow) {
        ObjectHelper.requireNonNull(onOverflow, "onOverflow is null");
        return RxJavaPlugins.onAssembly(new FlowableOnBackpressureBuffer(this, capacity, unbounded, delayError, onOverflow));
    }

    @BackpressureSupport(BackpressureKind.ERROR)
    @SchedulerSupport("none")
    public final Flowable<T> onBackpressureBuffer(int capacity, Action onOverflow) {
        return onBackpressureBuffer(capacity, false, false, onOverflow);
    }

    @BackpressureSupport(BackpressureKind.SPECIAL)
    @SchedulerSupport("none")
    public final Flowable<T> onBackpressureBuffer(long capacity, Action onOverflow, BackpressureOverflowStrategy overflowStrategy) {
        ObjectHelper.requireNonNull(overflowStrategy, "strategy is null");
        ObjectHelper.verifyPositive(capacity, "capacity");
        return RxJavaPlugins.onAssembly(new FlowableOnBackpressureBufferStrategy(this, capacity, onOverflow, overflowStrategy));
    }

    @BackpressureSupport(BackpressureKind.UNBOUNDED_IN)
    @SchedulerSupport("none")
    public final Flowable<T> onBackpressureDrop() {
        return RxJavaPlugins.onAssembly(new FlowableOnBackpressureDrop(this));
    }

    @BackpressureSupport(BackpressureKind.UNBOUNDED_IN)
    @SchedulerSupport("none")
    public final Flowable<T> onBackpressureDrop(Consumer<? super T> onDrop) {
        ObjectHelper.requireNonNull(onDrop, "onDrop is null");
        return RxJavaPlugins.onAssembly(new FlowableOnBackpressureDrop(this, onDrop));
    }

    @BackpressureSupport(BackpressureKind.UNBOUNDED_IN)
    @SchedulerSupport("none")
    public final Flowable<T> onBackpressureLatest() {
        return RxJavaPlugins.onAssembly(new FlowableOnBackpressureLatest(this));
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public final Flowable<T> onErrorResumeNext(Function<? super Throwable, ? extends Publisher<? extends T>> resumeFunction) {
        ObjectHelper.requireNonNull(resumeFunction, "resumeFunction is null");
        return RxJavaPlugins.onAssembly(new FlowableOnErrorNext(this, resumeFunction, false));
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public final Flowable<T> onErrorResumeNext(Publisher<? extends T> next) {
        ObjectHelper.requireNonNull(next, "next is null");
        return onErrorResumeNext(Functions.justFunction(next));
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public final Flowable<T> onErrorReturn(Function<? super Throwable, ? extends T> valueSupplier) {
        ObjectHelper.requireNonNull(valueSupplier, "valueSupplier is null");
        return RxJavaPlugins.onAssembly(new FlowableOnErrorReturn(this, valueSupplier));
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public final Flowable<T> onErrorReturnItem(T item) {
        ObjectHelper.requireNonNull(item, "item is null");
        return onErrorReturn(Functions.justFunction(item));
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public final Flowable<T> onExceptionResumeNext(Publisher<? extends T> next) {
        ObjectHelper.requireNonNull(next, "next is null");
        return RxJavaPlugins.onAssembly(new FlowableOnErrorNext(this, Functions.justFunction(next), true));
    }

    @BackpressureSupport(BackpressureKind.PASS_THROUGH)
    @SchedulerSupport("none")
    public final Flowable<T> onTerminateDetach() {
        return RxJavaPlugins.onAssembly(new FlowableDetach(this));
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public final ConnectableFlowable<T> publish() {
        return publish(bufferSize());
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public final <R> Flowable<R> publish(Function<? super Flowable<T>, ? extends Publisher<R>> selector) {
        return publish(selector, bufferSize());
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public final <R> Flowable<R> publish(Function<? super Flowable<T>, ? extends Publisher<? extends R>> selector, int prefetch) {
        ObjectHelper.requireNonNull(selector, "selector is null");
        ObjectHelper.verifyPositive(prefetch, "prefetch");
        return RxJavaPlugins.onAssembly(new FlowablePublishMulticast(this, selector, prefetch, false));
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public final ConnectableFlowable<T> publish(int bufferSize) {
        ObjectHelper.verifyPositive(bufferSize, "bufferSize");
        return FlowablePublish.create(this, bufferSize);
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public final Flowable<T> rebatchRequests(int n) {
        return observeOn(ImmediateThinScheduler.INSTANCE, true, n);
    }

    @BackpressureSupport(BackpressureKind.UNBOUNDED_IN)
    @SchedulerSupport("none")
    public final Maybe<T> reduce(BiFunction<T, T, T> reducer) {
        ObjectHelper.requireNonNull(reducer, "reducer is null");
        return RxJavaPlugins.onAssembly(new FlowableReduceMaybe(this, reducer));
    }

    @BackpressureSupport(BackpressureKind.UNBOUNDED_IN)
    @SchedulerSupport("none")
    public final <R> Single<R> reduce(R seed, BiFunction<R, ? super T, R> reducer) {
        return RxJavaPlugins.onAssembly(new FlowableSingleSingle(scan(seed, reducer).takeLast(1), null));
    }

    @BackpressureSupport(BackpressureKind.UNBOUNDED_IN)
    @SchedulerSupport("none")
    public final <R> Single<R> reduceWith(Callable<R> seedSupplier, BiFunction<R, ? super T, R> reducer) {
        return RxJavaPlugins.onAssembly(new FlowableSingleSingle(scanWith(seedSupplier, reducer).takeLast(1), null));
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public final Flowable<T> repeat() {
        return repeat(LongCompanionObject.MAX_VALUE);
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public final Flowable<T> repeat(long times) {
        if (times < 0) {
            throw new IllegalArgumentException("times >= 0 required but it was " + times);
        } else if (times == 0) {
            return empty();
        } else {
            return RxJavaPlugins.onAssembly(new FlowableRepeat(this, times));
        }
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public final Flowable<T> repeatUntil(BooleanSupplier stop) {
        ObjectHelper.requireNonNull(stop, "stop is null");
        return RxJavaPlugins.onAssembly(new FlowableRepeatUntil(this, stop));
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public final Flowable<T> repeatWhen(Function<? super Flowable<Object>, ? extends Publisher<?>> handler) {
        ObjectHelper.requireNonNull(handler, "handler is null");
        return RxJavaPlugins.onAssembly(new FlowableRepeatWhen(this, handler));
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public final ConnectableFlowable<T> replay() {
        return FlowableReplay.createFrom(this);
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public final <R> Flowable<R> replay(Function<? super Flowable<T>, ? extends Publisher<R>> selector) {
        ObjectHelper.requireNonNull(selector, "selector is null");
        return FlowableReplay.multicastSelector(FlowableInternalHelper.replayCallable(this), selector);
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public final <R> Flowable<R> replay(Function<? super Flowable<T>, ? extends Publisher<R>> selector, int bufferSize) {
        ObjectHelper.requireNonNull(selector, "selector is null");
        return FlowableReplay.multicastSelector(FlowableInternalHelper.replayCallable(this, bufferSize), selector);
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("io.reactivex:computation")
    public final <R> Flowable<R> replay(Function<? super Flowable<T>, ? extends Publisher<R>> selector, int bufferSize, long time, TimeUnit unit) {
        return replay(selector, bufferSize, time, unit, Schedulers.computation());
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("custom")
    public final <R> Flowable<R> replay(Function<? super Flowable<T>, ? extends Publisher<R>> selector, int bufferSize, long time, TimeUnit unit, Scheduler scheduler) {
        ObjectHelper.verifyPositive(bufferSize, "bufferSize");
        ObjectHelper.requireNonNull(selector, "selector is null");
        return FlowableReplay.multicastSelector(FlowableInternalHelper.replayCallable(this, bufferSize, time, unit, scheduler), selector);
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("custom")
    public final <R> Flowable<R> replay(Function<? super Flowable<T>, ? extends Publisher<R>> selector, int bufferSize, Scheduler scheduler) {
        return FlowableReplay.multicastSelector(FlowableInternalHelper.replayCallable(this, bufferSize), FlowableInternalHelper.replayFunction(selector, scheduler));
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("io.reactivex:computation")
    public final <R> Flowable<R> replay(Function<? super Flowable<T>, ? extends Publisher<R>> selector, long time, TimeUnit unit) {
        return replay(selector, time, unit, Schedulers.computation());
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("custom")
    public final <R> Flowable<R> replay(Function<? super Flowable<T>, ? extends Publisher<R>> selector, long time, TimeUnit unit, Scheduler scheduler) {
        ObjectHelper.requireNonNull(selector, "selector is null");
        ObjectHelper.requireNonNull(unit, "unit is null");
        ObjectHelper.requireNonNull(scheduler, "scheduler is null");
        return FlowableReplay.multicastSelector(FlowableInternalHelper.replayCallable(this, time, unit, scheduler), selector);
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("custom")
    public final <R> Flowable<R> replay(Function<? super Flowable<T>, ? extends Publisher<R>> selector, Scheduler scheduler) {
        ObjectHelper.requireNonNull(selector, "selector is null");
        ObjectHelper.requireNonNull(scheduler, "scheduler is null");
        return FlowableReplay.multicastSelector(FlowableInternalHelper.replayCallable(this), FlowableInternalHelper.replayFunction(selector, scheduler));
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public final ConnectableFlowable<T> replay(int bufferSize) {
        return FlowableReplay.create(this, bufferSize);
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("io.reactivex:computation")
    public final ConnectableFlowable<T> replay(int bufferSize, long time, TimeUnit unit) {
        return replay(bufferSize, time, unit, Schedulers.computation());
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("custom")
    public final ConnectableFlowable<T> replay(int bufferSize, long time, TimeUnit unit, Scheduler scheduler) {
        ObjectHelper.requireNonNull(unit, "unit is null");
        ObjectHelper.requireNonNull(scheduler, "scheduler is null");
        ObjectHelper.verifyPositive(bufferSize, "bufferSize");
        return FlowableReplay.create(this, time, unit, scheduler, bufferSize);
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("custom")
    public final ConnectableFlowable<T> replay(int bufferSize, Scheduler scheduler) {
        ObjectHelper.requireNonNull(scheduler, "scheduler is null");
        return FlowableReplay.observeOn(replay(bufferSize), scheduler);
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("io.reactivex:computation")
    public final ConnectableFlowable<T> replay(long time, TimeUnit unit) {
        return replay(time, unit, Schedulers.computation());
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("custom")
    public final ConnectableFlowable<T> replay(long time, TimeUnit unit, Scheduler scheduler) {
        ObjectHelper.requireNonNull(unit, "unit is null");
        ObjectHelper.requireNonNull(scheduler, "scheduler is null");
        return FlowableReplay.create(this, time, unit, scheduler);
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("custom")
    public final ConnectableFlowable<T> replay(Scheduler scheduler) {
        ObjectHelper.requireNonNull(scheduler, "scheduler is null");
        return FlowableReplay.observeOn(replay(), scheduler);
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public final Flowable<T> retry() {
        return retry(LongCompanionObject.MAX_VALUE, Functions.alwaysTrue());
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public final Flowable<T> retry(BiPredicate<? super Integer, ? super Throwable> predicate) {
        ObjectHelper.requireNonNull(predicate, "predicate is null");
        return RxJavaPlugins.onAssembly(new FlowableRetryBiPredicate(this, predicate));
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public final Flowable<T> retry(long count) {
        return retry(count, Functions.alwaysTrue());
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public final Flowable<T> retry(long times, Predicate<? super Throwable> predicate) {
        if (times < 0) {
            throw new IllegalArgumentException("times >= 0 required but it was " + times);
        }
        ObjectHelper.requireNonNull(predicate, "predicate is null");
        return RxJavaPlugins.onAssembly(new FlowableRetryPredicate(this, times, predicate));
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public final Flowable<T> retry(Predicate<? super Throwable> predicate) {
        return retry(LongCompanionObject.MAX_VALUE, predicate);
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public final Flowable<T> retryUntil(BooleanSupplier stop) {
        ObjectHelper.requireNonNull(stop, "stop is null");
        return retry(LongCompanionObject.MAX_VALUE, Functions.predicateReverseFor(stop));
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public final Flowable<T> retryWhen(Function<? super Flowable<Throwable>, ? extends Publisher<?>> handler) {
        ObjectHelper.requireNonNull(handler, "handler is null");
        return RxJavaPlugins.onAssembly(new FlowableRetryWhen(this, handler));
    }

    @BackpressureSupport(BackpressureKind.PASS_THROUGH)
    @SchedulerSupport("none")
    public final void safeSubscribe(Subscriber<? super T> s) {
        ObjectHelper.requireNonNull(s, "s is null");
        if (s instanceof SafeSubscriber) {
            subscribe(s);
        } else {
            subscribe(new SafeSubscriber(s));
        }
    }

    @BackpressureSupport(BackpressureKind.ERROR)
    @SchedulerSupport("io.reactivex:computation")
    public final Flowable<T> sample(long period, TimeUnit unit) {
        return sample(period, unit, Schedulers.computation());
    }

    @BackpressureSupport(BackpressureKind.ERROR)
    @SchedulerSupport("custom")
    public final Flowable<T> sample(long period, TimeUnit unit, Scheduler scheduler) {
        ObjectHelper.requireNonNull(unit, "unit is null");
        ObjectHelper.requireNonNull(scheduler, "scheduler is null");
        return RxJavaPlugins.onAssembly(new FlowableSampleTimed(this, period, unit, scheduler));
    }

    @BackpressureSupport(BackpressureKind.ERROR)
    @SchedulerSupport("none")
    public final <U> Flowable<T> sample(Publisher<U> sampler) {
        ObjectHelper.requireNonNull(sampler, "sampler is null");
        return RxJavaPlugins.onAssembly(new FlowableSamplePublisher(this, sampler));
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public final Flowable<T> scan(BiFunction<T, T, T> accumulator) {
        ObjectHelper.requireNonNull(accumulator, "accumulator is null");
        return RxJavaPlugins.onAssembly(new FlowableScan(this, accumulator));
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public final <R> Flowable<R> scan(R initialValue, BiFunction<R, ? super T, R> accumulator) {
        ObjectHelper.requireNonNull(initialValue, "seed is null");
        return scanWith(Functions.justCallable(initialValue), accumulator);
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public final <R> Flowable<R> scanWith(Callable<R> seedSupplier, BiFunction<R, ? super T, R> accumulator) {
        ObjectHelper.requireNonNull(seedSupplier, "seedSupplier is null");
        ObjectHelper.requireNonNull(accumulator, "accumulator is null");
        return RxJavaPlugins.onAssembly(new FlowableScanSeed(this, seedSupplier, accumulator));
    }

    @BackpressureSupport(BackpressureKind.PASS_THROUGH)
    @SchedulerSupport("none")
    public final Flowable<T> serialize() {
        return RxJavaPlugins.onAssembly(new FlowableSerialized(this));
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public final Flowable<T> share() {
        return publish().refCount();
    }

    @BackpressureSupport(BackpressureKind.UNBOUNDED_IN)
    @SchedulerSupport("none")
    public final Maybe<T> singleElement() {
        return RxJavaPlugins.onAssembly(new FlowableSingleMaybe(this));
    }

    @BackpressureSupport(BackpressureKind.UNBOUNDED_IN)
    @SchedulerSupport("none")
    public final Single<T> single(T defaultItem) {
        ObjectHelper.requireNonNull(defaultItem, "defaultItem is null");
        return RxJavaPlugins.onAssembly(new FlowableSingleSingle(this, defaultItem));
    }

    @BackpressureSupport(BackpressureKind.UNBOUNDED_IN)
    @SchedulerSupport("none")
    public final Single<T> singleOrError() {
        return RxJavaPlugins.onAssembly(new FlowableSingleSingle(this, null));
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public final Flowable<T> skip(long count) {
        if (count <= 0) {
            return RxJavaPlugins.onAssembly(this);
        }
        return RxJavaPlugins.onAssembly(new FlowableSkip(this, count));
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public final Flowable<T> skip(long time, TimeUnit unit) {
        return skipUntil(timer(time, unit));
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("custom")
    public final Flowable<T> skip(long time, TimeUnit unit, Scheduler scheduler) {
        return skipUntil(timer(time, unit, scheduler));
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public final Flowable<T> skipLast(int count) {
        if (count < 0) {
            throw new IndexOutOfBoundsException("count >= 0 required but it was " + count);
        } else if (count == 0) {
            return RxJavaPlugins.onAssembly(this);
        } else {
            return RxJavaPlugins.onAssembly(new FlowableSkipLast(this, count));
        }
    }

    @BackpressureSupport(BackpressureKind.UNBOUNDED_IN)
    @SchedulerSupport("none")
    public final Flowable<T> skipLast(long time, TimeUnit unit) {
        return skipLast(time, unit, Schedulers.computation(), false, bufferSize());
    }

    @BackpressureSupport(BackpressureKind.UNBOUNDED_IN)
    @SchedulerSupport("none")
    public final Flowable<T> skipLast(long time, TimeUnit unit, boolean delayError) {
        return skipLast(time, unit, Schedulers.computation(), delayError, bufferSize());
    }

    @BackpressureSupport(BackpressureKind.UNBOUNDED_IN)
    @SchedulerSupport("custom")
    public final Flowable<T> skipLast(long time, TimeUnit unit, Scheduler scheduler) {
        return skipLast(time, unit, scheduler, false, bufferSize());
    }

    @BackpressureSupport(BackpressureKind.UNBOUNDED_IN)
    @SchedulerSupport("custom")
    public final Flowable<T> skipLast(long time, TimeUnit unit, Scheduler scheduler, boolean delayError) {
        return skipLast(time, unit, scheduler, delayError, bufferSize());
    }

    @BackpressureSupport(BackpressureKind.UNBOUNDED_IN)
    @SchedulerSupport("custom")
    public final Flowable<T> skipLast(long time, TimeUnit unit, Scheduler scheduler, boolean delayError, int bufferSize) {
        ObjectHelper.requireNonNull(unit, "unit is null");
        ObjectHelper.requireNonNull(scheduler, "scheduler is null");
        ObjectHelper.verifyPositive(bufferSize, "bufferSize");
        return RxJavaPlugins.onAssembly(new FlowableSkipLastTimed(this, time, unit, scheduler, bufferSize << 1, delayError));
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public final <U> Flowable<T> skipUntil(Publisher<U> other) {
        ObjectHelper.requireNonNull(other, "other is null");
        return RxJavaPlugins.onAssembly(new FlowableSkipUntil(this, other));
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public final Flowable<T> skipWhile(Predicate<? super T> predicate) {
        ObjectHelper.requireNonNull(predicate, "predicate is null");
        return RxJavaPlugins.onAssembly(new FlowableSkipWhile(this, predicate));
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public final Flowable<T> sorted() {
        return toList().toFlowable().map(Functions.listSorter(Functions.naturalComparator())).flatMapIterable(Functions.identity());
    }

    /* JADX WARN: Type inference failed for: r3v0, types: [java.util.Comparator<? super T>, java.util.Comparator], assign insn: null */
    /* JADX WARNING: Unknown variable types count: 1 */
    @dji.thirdparty.io.reactivex.annotations.BackpressureSupport(dji.thirdparty.io.reactivex.annotations.BackpressureKind.FULL)
    @dji.thirdparty.io.reactivex.annotations.SchedulerSupport("none")
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final dji.thirdparty.io.reactivex.Flowable<T> sorted(java.util.Comparator<? super T> r3) {
        /*
            r2 = this;
            dji.thirdparty.io.reactivex.Single r0 = r2.toList()
            dji.thirdparty.io.reactivex.Flowable r0 = r0.toFlowable()
            dji.thirdparty.io.reactivex.functions.Function r1 = dji.thirdparty.io.reactivex.internal.functions.Functions.listSorter(r3)
            dji.thirdparty.io.reactivex.Flowable r0 = r0.map(r1)
            dji.thirdparty.io.reactivex.functions.Function r1 = dji.thirdparty.io.reactivex.internal.functions.Functions.identity()
            dji.thirdparty.io.reactivex.Flowable r0 = r0.flatMapIterable(r1)
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: dji.thirdparty.io.reactivex.Flowable.sorted(java.util.Comparator):dji.thirdparty.io.reactivex.Flowable");
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public final Flowable<T> startWith(Iterable iterable) {
        return concatArray(fromIterable(iterable), this);
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public final Flowable<T> startWith(Publisher publisher) {
        ObjectHelper.requireNonNull(publisher, "other is null");
        return concatArray(publisher, this);
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public final Flowable<T> startWith(Object obj) {
        ObjectHelper.requireNonNull(obj, "item is null");
        return concatArray(just(obj), this);
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public final Flowable<T> startWithArray(T... items) {
        Flowable<T> fromArray = fromArray(items);
        if (fromArray == empty()) {
            return RxJavaPlugins.onAssembly(this);
        }
        return concatArray(fromArray, this);
    }

    @BackpressureSupport(BackpressureKind.UNBOUNDED_IN)
    @SchedulerSupport("none")
    public final Disposable subscribe() {
        return subscribe(Functions.emptyConsumer(), Functions.ERROR_CONSUMER, Functions.EMPTY_ACTION, FlowableInternalHelper.RequestMax.INSTANCE);
    }

    @BackpressureSupport(BackpressureKind.UNBOUNDED_IN)
    @SchedulerSupport("none")
    public final Disposable subscribe(Consumer<? super T> onNext) {
        return subscribe(onNext, Functions.ERROR_CONSUMER, Functions.EMPTY_ACTION, FlowableInternalHelper.RequestMax.INSTANCE);
    }

    @BackpressureSupport(BackpressureKind.UNBOUNDED_IN)
    @SchedulerSupport("none")
    public final Disposable subscribe(Consumer<? super T> onNext, Consumer<? super Throwable> onError) {
        return subscribe(onNext, onError, Functions.EMPTY_ACTION, FlowableInternalHelper.RequestMax.INSTANCE);
    }

    @BackpressureSupport(BackpressureKind.UNBOUNDED_IN)
    @SchedulerSupport("none")
    public final Disposable subscribe(Consumer<? super T> onNext, Consumer<? super Throwable> onError, Action onComplete) {
        return subscribe(onNext, onError, onComplete, FlowableInternalHelper.RequestMax.INSTANCE);
    }

    @BackpressureSupport(BackpressureKind.SPECIAL)
    @SchedulerSupport("none")
    public final Disposable subscribe(Consumer<? super T> onNext, Consumer<? super Throwable> onError, Action onComplete, Consumer<? super Subscription> onSubscribe) {
        ObjectHelper.requireNonNull(onNext, "onNext is null");
        ObjectHelper.requireNonNull(onError, "onError is null");
        ObjectHelper.requireNonNull(onComplete, "onComplete is null");
        ObjectHelper.requireNonNull(onSubscribe, "onSubscribe is null");
        LambdaSubscriber<T> ls = new LambdaSubscriber<>(onNext, onError, onComplete, onSubscribe);
        subscribe(ls);
        return ls;
    }

    @BackpressureSupport(BackpressureKind.SPECIAL)
    @SchedulerSupport("none")
    public final void subscribe(Subscriber<? super T> s) {
        ObjectHelper.requireNonNull(s, "s is null");
        try {
            Subscriber<? super T> s2 = RxJavaPlugins.onSubscribe(this, s);
            ObjectHelper.requireNonNull(s2, "Plugin returned null Subscriber");
            subscribeActual(s2);
        } catch (NullPointerException e) {
            throw e;
        } catch (Throwable e2) {
            Exceptions.throwIfFatal(e2);
            RxJavaPlugins.onError(e2);
            NullPointerException npe = new NullPointerException("Actually not, but can't throw other exceptions due to RS");
            npe.initCause(e2);
            throw npe;
        }
    }

    @BackpressureSupport(BackpressureKind.SPECIAL)
    @SchedulerSupport("none")
    public final <E extends Subscriber<? super T>> E subscribeWith(E subscriber) {
        subscribe((Subscriber) subscriber);
        return subscriber;
    }

    @BackpressureSupport(BackpressureKind.PASS_THROUGH)
    @SchedulerSupport("custom")
    public final Flowable<T> subscribeOn(Scheduler scheduler) {
        ObjectHelper.requireNonNull(scheduler, "scheduler is null");
        return RxJavaPlugins.onAssembly(new FlowableSubscribeOn(this, scheduler, this instanceof FlowableCreate));
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public final Flowable<T> switchIfEmpty(Publisher<? extends T> other) {
        ObjectHelper.requireNonNull(other, "other is null");
        return RxJavaPlugins.onAssembly(new FlowableSwitchIfEmpty(this, other));
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public final <R> Flowable<R> switchMap(Function<? super T, ? extends Publisher<? extends R>> mapper) {
        return switchMap(mapper, bufferSize());
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public final <R> Flowable<R> switchMap(Function<? super T, ? extends Publisher<? extends R>> mapper, int bufferSize) {
        return switchMap0(mapper, bufferSize, false);
    }

    @BackpressureSupport(BackpressureKind.SPECIAL)
    @SchedulerSupport("none")
    public final <R> Flowable<R> switchMapDelayError(Function<? super T, ? extends Publisher<? extends R>> mapper) {
        return switchMapDelayError(mapper, bufferSize());
    }

    @BackpressureSupport(BackpressureKind.SPECIAL)
    @SchedulerSupport("none")
    public final <R> Flowable<R> switchMapDelayError(Function<? super T, ? extends Publisher<? extends R>> mapper, int bufferSize) {
        return switchMap0(mapper, bufferSize, true);
    }

    /* JADX WARN: Type inference failed for: r3v0, types: [dji.thirdparty.io.reactivex.functions.Function, java.lang.Object, dji.thirdparty.io.reactivex.functions.Function<? super T, ? extends org.reactivestreams.Publisher<? extends R>>], assign insn: null */
    /* access modifiers changed from: package-private */
    /* JADX WARNING: Unknown variable types count: 1 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public <R> dji.thirdparty.io.reactivex.Flowable<R> switchMap0(dji.thirdparty.io.reactivex.functions.Function<? super T, ? extends org.reactivestreams.Publisher<? extends R>> r3, int r4, boolean r5) {
        /*
            r2 = this;
            java.lang.String r1 = "mapper is null"
            dji.thirdparty.io.reactivex.internal.functions.ObjectHelper.requireNonNull(r3, r1)
            boolean r1 = r2 instanceof dji.thirdparty.io.reactivex.internal.fuseable.ScalarCallable
            if (r1 == 0) goto L_0x001c
            dji.thirdparty.io.reactivex.internal.fuseable.ScalarCallable r2 = (dji.thirdparty.io.reactivex.internal.fuseable.ScalarCallable) r2
            java.lang.Object r0 = r2.call()
            if (r0 != 0) goto L_0x0017
            dji.thirdparty.io.reactivex.Flowable r1 = empty()
        L_0x0016:
            return r1
        L_0x0017:
            dji.thirdparty.io.reactivex.Flowable r1 = dji.thirdparty.io.reactivex.internal.operators.flowable.FlowableScalarXMap.scalarXMap(r0, r3)
            goto L_0x0016
        L_0x001c:
            java.lang.String r1 = "bufferSize"
            dji.thirdparty.io.reactivex.internal.functions.ObjectHelper.verifyPositive(r4, r1)
            dji.thirdparty.io.reactivex.internal.operators.flowable.FlowableSwitchMap r1 = new dji.thirdparty.io.reactivex.internal.operators.flowable.FlowableSwitchMap
            r1.<init>(r2, r3, r4, r5)
            dji.thirdparty.io.reactivex.Flowable r1 = dji.thirdparty.io.reactivex.plugins.RxJavaPlugins.onAssembly(r1)
            goto L_0x0016
        */
        throw new UnsupportedOperationException("Method not decompiled: dji.thirdparty.io.reactivex.Flowable.switchMap0(dji.thirdparty.io.reactivex.functions.Function, int, boolean):dji.thirdparty.io.reactivex.Flowable");
    }

    @BackpressureSupport(BackpressureKind.SPECIAL)
    @SchedulerSupport("none")
    public final Flowable<T> take(long count) {
        if (count >= 0) {
            return RxJavaPlugins.onAssembly(new FlowableTake(this, count));
        }
        throw new IllegalArgumentException("count >= 0 required but it was " + count);
    }

    @BackpressureSupport(BackpressureKind.PASS_THROUGH)
    @SchedulerSupport("io.reactivex:computation")
    public final Flowable<T> take(long time, TimeUnit unit) {
        return takeUntil(timer(time, unit));
    }

    @BackpressureSupport(BackpressureKind.PASS_THROUGH)
    @SchedulerSupport("custom")
    public final Flowable<T> take(long time, TimeUnit unit, Scheduler scheduler) {
        return takeUntil(timer(time, unit, scheduler));
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public final Flowable<T> takeLast(int count) {
        if (count < 0) {
            throw new IndexOutOfBoundsException("count >= 0 required but it was " + count);
        } else if (count == 0) {
            return RxJavaPlugins.onAssembly(new FlowableIgnoreElements(this));
        } else {
            if (count == 1) {
                return RxJavaPlugins.onAssembly(new FlowableTakeLastOne(this));
            }
            return RxJavaPlugins.onAssembly(new FlowableTakeLast(this, count));
        }
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public final Flowable<T> takeLast(long count, long time, TimeUnit unit) {
        return takeLast(count, time, unit, Schedulers.computation(), false, bufferSize());
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("custom")
    public final Flowable<T> takeLast(long count, long time, TimeUnit unit, Scheduler scheduler) {
        return takeLast(count, time, unit, scheduler, false, bufferSize());
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("custom")
    public final Flowable<T> takeLast(long count, long time, TimeUnit unit, Scheduler scheduler, boolean delayError, int bufferSize) {
        ObjectHelper.requireNonNull(unit, "unit is null");
        ObjectHelper.requireNonNull(scheduler, "scheduler is null");
        ObjectHelper.verifyPositive(bufferSize, "bufferSize");
        if (count >= 0) {
            return RxJavaPlugins.onAssembly(new FlowableTakeLastTimed(this, count, time, unit, scheduler, bufferSize, delayError));
        }
        throw new IndexOutOfBoundsException("count >= 0 required but it was " + count);
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("io.reactivex:computation")
    public final Flowable<T> takeLast(long time, TimeUnit unit) {
        return takeLast(time, unit, Schedulers.computation(), false, bufferSize());
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("io.reactivex:computation")
    public final Flowable<T> takeLast(long time, TimeUnit unit, boolean delayError) {
        return takeLast(time, unit, Schedulers.computation(), delayError, bufferSize());
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("custom")
    public final Flowable<T> takeLast(long time, TimeUnit unit, Scheduler scheduler) {
        return takeLast(time, unit, scheduler, false, bufferSize());
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("custom")
    public final Flowable<T> takeLast(long time, TimeUnit unit, Scheduler scheduler, boolean delayError) {
        return takeLast(time, unit, scheduler, delayError, bufferSize());
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("custom")
    public final Flowable<T> takeLast(long time, TimeUnit unit, Scheduler scheduler, boolean delayError, int bufferSize) {
        return takeLast(LongCompanionObject.MAX_VALUE, time, unit, scheduler, delayError, bufferSize);
    }

    @BackpressureSupport(BackpressureKind.PASS_THROUGH)
    @SchedulerSupport("none")
    public final Flowable<T> takeUntil(Predicate<? super T> stopPredicate) {
        ObjectHelper.requireNonNull(stopPredicate, "stopPredicate is null");
        return RxJavaPlugins.onAssembly(new FlowableTakeUntilPredicate(this, stopPredicate));
    }

    @BackpressureSupport(BackpressureKind.PASS_THROUGH)
    @SchedulerSupport("none")
    public final <U> Flowable<T> takeUntil(Publisher<U> other) {
        ObjectHelper.requireNonNull(other, "other is null");
        return RxJavaPlugins.onAssembly(new FlowableTakeUntil(this, other));
    }

    @BackpressureSupport(BackpressureKind.PASS_THROUGH)
    @SchedulerSupport("none")
    public final Flowable<T> takeWhile(Predicate<? super T> predicate) {
        ObjectHelper.requireNonNull(predicate, "predicate is null");
        return RxJavaPlugins.onAssembly(new FlowableTakeWhile(this, predicate));
    }

    @BackpressureSupport(BackpressureKind.ERROR)
    @SchedulerSupport("io.reactivex:computation")
    public final Flowable<T> throttleFirst(long windowDuration, TimeUnit unit) {
        return throttleFirst(windowDuration, unit, Schedulers.computation());
    }

    @BackpressureSupport(BackpressureKind.ERROR)
    @SchedulerSupport("custom")
    public final Flowable<T> throttleFirst(long skipDuration, TimeUnit unit, Scheduler scheduler) {
        ObjectHelper.requireNonNull(unit, "unit is null");
        ObjectHelper.requireNonNull(scheduler, "scheduler is null");
        return RxJavaPlugins.onAssembly(new FlowableThrottleFirstTimed(this, skipDuration, unit, scheduler));
    }

    @BackpressureSupport(BackpressureKind.ERROR)
    @SchedulerSupport("io.reactivex:computation")
    public final Flowable<T> throttleLast(long intervalDuration, TimeUnit unit) {
        return sample(intervalDuration, unit);
    }

    @BackpressureSupport(BackpressureKind.ERROR)
    @SchedulerSupport("custom")
    public final Flowable<T> throttleLast(long intervalDuration, TimeUnit unit, Scheduler scheduler) {
        return sample(intervalDuration, unit, scheduler);
    }

    @BackpressureSupport(BackpressureKind.ERROR)
    @SchedulerSupport("io.reactivex:computation")
    public final Flowable<T> throttleWithTimeout(long timeout, TimeUnit unit) {
        return debounce(timeout, unit);
    }

    @BackpressureSupport(BackpressureKind.ERROR)
    @SchedulerSupport("custom")
    public final Flowable<T> throttleWithTimeout(long timeout, TimeUnit unit, Scheduler scheduler) {
        return debounce(timeout, unit, scheduler);
    }

    @BackpressureSupport(BackpressureKind.PASS_THROUGH)
    @SchedulerSupport("none")
    public final Flowable<Timed<T>> timeInterval() {
        return timeInterval(TimeUnit.MILLISECONDS, Schedulers.computation());
    }

    @BackpressureSupport(BackpressureKind.PASS_THROUGH)
    @SchedulerSupport("none")
    public final Flowable<Timed<T>> timeInterval(Scheduler scheduler) {
        return timeInterval(TimeUnit.MILLISECONDS, scheduler);
    }

    @BackpressureSupport(BackpressureKind.PASS_THROUGH)
    @SchedulerSupport("none")
    public final Flowable<Timed<T>> timeInterval(TimeUnit unit) {
        return timeInterval(unit, Schedulers.computation());
    }

    @BackpressureSupport(BackpressureKind.PASS_THROUGH)
    @SchedulerSupport("none")
    public final Flowable<Timed<T>> timeInterval(TimeUnit unit, Scheduler scheduler) {
        ObjectHelper.requireNonNull(unit, "unit is null");
        ObjectHelper.requireNonNull(scheduler, "scheduler is null");
        return RxJavaPlugins.onAssembly(new FlowableTimeInterval(this, unit, scheduler));
    }

    @BackpressureSupport(BackpressureKind.PASS_THROUGH)
    @SchedulerSupport("none")
    public final <V> Flowable<T> timeout(Function<? super T, ? extends Publisher<V>> itemTimeoutIndicator) {
        return timeout0(null, itemTimeoutIndicator, null);
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public final <V> Flowable<T> timeout(Function<? super T, ? extends Publisher<V>> itemTimeoutIndicator, Flowable<? extends T> other) {
        ObjectHelper.requireNonNull(other, "other is null");
        return timeout0(null, itemTimeoutIndicator, other);
    }

    @BackpressureSupport(BackpressureKind.PASS_THROUGH)
    @SchedulerSupport("io.reactivex:computation")
    public final Flowable<T> timeout(long timeout, TimeUnit timeUnit) {
        return timeout0(timeout, timeUnit, null, Schedulers.computation());
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("io.reactivex:computation")
    public final Flowable<T> timeout(long timeout, TimeUnit timeUnit, Flowable<? extends T> other) {
        ObjectHelper.requireNonNull(other, "other is null");
        return timeout0(timeout, timeUnit, other, Schedulers.computation());
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("custom")
    public final Flowable<T> timeout(long timeout, TimeUnit timeUnit, Scheduler scheduler, Flowable<? extends T> other) {
        ObjectHelper.requireNonNull(other, "other is null");
        return timeout0(timeout, timeUnit, other, scheduler);
    }

    @BackpressureSupport(BackpressureKind.PASS_THROUGH)
    @SchedulerSupport("custom")
    public final Flowable<T> timeout(long timeout, TimeUnit timeUnit, Scheduler scheduler) {
        return timeout0(timeout, timeUnit, null, scheduler);
    }

    /* JADX WARN: Type inference failed for: r2v0, types: [org.reactivestreams.Publisher<U>, org.reactivestreams.Publisher, java.lang.Object], assign insn: null */
    /* JADX WARNING: Unknown variable types count: 1 */
    @dji.thirdparty.io.reactivex.annotations.BackpressureSupport(dji.thirdparty.io.reactivex.annotations.BackpressureKind.PASS_THROUGH)
    @dji.thirdparty.io.reactivex.annotations.SchedulerSupport("none")
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final <U, V> dji.thirdparty.io.reactivex.Flowable<T> timeout(org.reactivestreams.Publisher<U> r2, dji.thirdparty.io.reactivex.functions.Function<? super T, ? extends org.reactivestreams.Publisher<V>> r3) {
        /*
            r1 = this;
            java.lang.String r0 = "firstTimeoutIndicator is null"
            dji.thirdparty.io.reactivex.internal.functions.ObjectHelper.requireNonNull(r2, r0)
            r0 = 0
            dji.thirdparty.io.reactivex.Flowable r0 = r1.timeout0(r2, r3, r0)
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: dji.thirdparty.io.reactivex.Flowable.timeout(org.reactivestreams.Publisher, dji.thirdparty.io.reactivex.functions.Function):dji.thirdparty.io.reactivex.Flowable");
    }

    /* JADX WARN: Type inference failed for: r2v0, types: [org.reactivestreams.Publisher<U>, org.reactivestreams.Publisher, java.lang.Object], assign insn: null */
    /* JADX WARNING: Unknown variable types count: 1 */
    @dji.thirdparty.io.reactivex.annotations.BackpressureSupport(dji.thirdparty.io.reactivex.annotations.BackpressureKind.FULL)
    @dji.thirdparty.io.reactivex.annotations.SchedulerSupport("none")
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final <U, V> dji.thirdparty.io.reactivex.Flowable<T> timeout(org.reactivestreams.Publisher<U> r2, dji.thirdparty.io.reactivex.functions.Function<? super T, ? extends org.reactivestreams.Publisher<V>> r3, org.reactivestreams.Publisher<? extends T> r4) {
        /*
            r1 = this;
            java.lang.String r0 = "firstTimeoutSelector is null"
            dji.thirdparty.io.reactivex.internal.functions.ObjectHelper.requireNonNull(r2, r0)
            java.lang.String r0 = "other is null"
            dji.thirdparty.io.reactivex.internal.functions.ObjectHelper.requireNonNull(r4, r0)
            dji.thirdparty.io.reactivex.Flowable r0 = r1.timeout0(r2, r3, r4)
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: dji.thirdparty.io.reactivex.Flowable.timeout(org.reactivestreams.Publisher, dji.thirdparty.io.reactivex.functions.Function, org.reactivestreams.Publisher):dji.thirdparty.io.reactivex.Flowable");
    }

    private Flowable<T> timeout0(long timeout, TimeUnit timeUnit, Flowable<? extends T> other, Scheduler scheduler) {
        ObjectHelper.requireNonNull(timeUnit, "timeUnit is null");
        ObjectHelper.requireNonNull(scheduler, "scheduler is null");
        return RxJavaPlugins.onAssembly(new FlowableTimeoutTimed(this, timeout, timeUnit, scheduler, other));
    }

    private <U, V> Flowable<T> timeout0(Publisher<U> firstTimeoutIndicator, Function<? super T, ? extends Publisher<V>> itemTimeoutIndicator, Publisher<? extends T> other) {
        ObjectHelper.requireNonNull(itemTimeoutIndicator, "itemTimeoutIndicator is null");
        return RxJavaPlugins.onAssembly(new FlowableTimeout(this, firstTimeoutIndicator, itemTimeoutIndicator, other));
    }

    @BackpressureSupport(BackpressureKind.PASS_THROUGH)
    @SchedulerSupport("none")
    public final Flowable<Timed<T>> timestamp() {
        return timestamp(TimeUnit.MILLISECONDS, Schedulers.computation());
    }

    @BackpressureSupport(BackpressureKind.PASS_THROUGH)
    @SchedulerSupport("none")
    public final Flowable<Timed<T>> timestamp(Scheduler scheduler) {
        return timestamp(TimeUnit.MILLISECONDS, scheduler);
    }

    @BackpressureSupport(BackpressureKind.PASS_THROUGH)
    @SchedulerSupport("none")
    public final Flowable<Timed<T>> timestamp(TimeUnit unit) {
        return timestamp(unit, Schedulers.computation());
    }

    @BackpressureSupport(BackpressureKind.PASS_THROUGH)
    @SchedulerSupport("none")
    public final Flowable<Timed<T>> timestamp(TimeUnit unit, Scheduler scheduler) {
        ObjectHelper.requireNonNull(unit, "unit is null");
        ObjectHelper.requireNonNull(scheduler, "scheduler is null");
        return map(Functions.timestampWith(unit, scheduler));
    }

    @BackpressureSupport(BackpressureKind.SPECIAL)
    @SchedulerSupport("none")
    public final <R> R to(Function<? super Flowable<T>, R> converter) {
        try {
            return converter.apply(this);
        } catch (Throwable ex) {
            Exceptions.throwIfFatal(ex);
            throw ExceptionHelper.wrapOrThrow(ex);
        }
    }

    @BackpressureSupport(BackpressureKind.UNBOUNDED_IN)
    @SchedulerSupport("none")
    public final Single<List<T>> toList() {
        return RxJavaPlugins.onAssembly(new FlowableToListSingle(this));
    }

    @BackpressureSupport(BackpressureKind.UNBOUNDED_IN)
    @SchedulerSupport("none")
    public final Single<List<T>> toList(int capacityHint) {
        ObjectHelper.verifyPositive(capacityHint, "capacityHint");
        return RxJavaPlugins.onAssembly(new FlowableToListSingle(this, Functions.createArrayList(capacityHint)));
    }

    @BackpressureSupport(BackpressureKind.UNBOUNDED_IN)
    @SchedulerSupport("none")
    public final <U extends Collection<? super T>> Single<U> toList(Callable<U> collectionSupplier) {
        ObjectHelper.requireNonNull(collectionSupplier, "collectionSupplier is null");
        return RxJavaPlugins.onAssembly(new FlowableToListSingle(this, collectionSupplier));
    }

    @BackpressureSupport(BackpressureKind.UNBOUNDED_IN)
    @SchedulerSupport("none")
    public final <K> Single<Map<K, T>> toMap(Function<? super T, ? extends K> keySelector) {
        ObjectHelper.requireNonNull(keySelector, "keySelector is null");
        return collect(HashMapSupplier.asCallable(), Functions.toMapKeySelector(keySelector));
    }

    @BackpressureSupport(BackpressureKind.UNBOUNDED_IN)
    @SchedulerSupport("none")
    public final <K, V> Single<Map<K, V>> toMap(Function<? super T, ? extends K> keySelector, Function<? super T, ? extends V> valueSelector) {
        ObjectHelper.requireNonNull(keySelector, "keySelector is null");
        ObjectHelper.requireNonNull(valueSelector, "valueSelector is null");
        return collect(HashMapSupplier.asCallable(), Functions.toMapKeyValueSelector(keySelector, valueSelector));
    }

    @BackpressureSupport(BackpressureKind.UNBOUNDED_IN)
    @SchedulerSupport("none")
    public final <K, V> Single<Map<K, V>> toMap(Function<? super T, ? extends K> keySelector, Function<? super T, ? extends V> valueSelector, Callable<? extends Map<K, V>> mapSupplier) {
        ObjectHelper.requireNonNull(keySelector, "keySelector is null");
        ObjectHelper.requireNonNull(valueSelector, "valueSelector is null");
        return collect(mapSupplier, Functions.toMapKeyValueSelector(keySelector, valueSelector));
    }

    /* JADX WARN: Type inference failed for: r5v0, types: [dji.thirdparty.io.reactivex.functions.Function, dji.thirdparty.io.reactivex.functions.Function<? super T, ? extends K>], assign insn: null */
    /* JADX WARNING: Unknown variable types count: 1 */
    @dji.thirdparty.io.reactivex.annotations.BackpressureSupport(dji.thirdparty.io.reactivex.annotations.BackpressureKind.UNBOUNDED_IN)
    @dji.thirdparty.io.reactivex.annotations.SchedulerSupport("none")
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final <K> dji.thirdparty.io.reactivex.Single<java.util.Map<K, java.util.Collection<T>>> toMultimap(dji.thirdparty.io.reactivex.functions.Function<? super T, ? extends K> r5) {
        /*
            r4 = this;
            dji.thirdparty.io.reactivex.functions.Function r2 = dji.thirdparty.io.reactivex.internal.functions.Functions.identity()
            java.util.concurrent.Callable r1 = dji.thirdparty.io.reactivex.internal.util.HashMapSupplier.asCallable()
            dji.thirdparty.io.reactivex.functions.Function r0 = dji.thirdparty.io.reactivex.internal.util.ArrayListSupplier.asFunction()
            dji.thirdparty.io.reactivex.Single r3 = r4.toMultimap(r5, r2, r1, r0)
            return r3
        */
        throw new UnsupportedOperationException("Method not decompiled: dji.thirdparty.io.reactivex.Flowable.toMultimap(dji.thirdparty.io.reactivex.functions.Function):dji.thirdparty.io.reactivex.Single");
    }

    @BackpressureSupport(BackpressureKind.UNBOUNDED_IN)
    @SchedulerSupport("none")
    public final <K, V> Single<Map<K, Collection<V>>> toMultimap(Function<? super T, ? extends K> keySelector, Function<? super T, ? extends V> valueSelector) {
        return toMultimap(keySelector, valueSelector, HashMapSupplier.asCallable(), ArrayListSupplier.asFunction());
    }

    @BackpressureSupport(BackpressureKind.UNBOUNDED_IN)
    @SchedulerSupport("none")
    public final <K, V> Single<Map<K, Collection<V>>> toMultimap(Function<? super T, ? extends K> keySelector, Function<? super T, ? extends V> valueSelector, Callable<? extends Map<K, Collection<V>>> mapSupplier, Function<? super K, ? extends Collection<? super V>> collectionFactory) {
        ObjectHelper.requireNonNull(keySelector, "keySelector is null");
        ObjectHelper.requireNonNull(valueSelector, "valueSelector is null");
        ObjectHelper.requireNonNull(mapSupplier, "mapSupplier is null");
        ObjectHelper.requireNonNull(collectionFactory, "collectionFactory is null");
        return collect(mapSupplier, Functions.toMultimapKeyValueSelector(keySelector, valueSelector, collectionFactory));
    }

    @BackpressureSupport(BackpressureKind.UNBOUNDED_IN)
    @SchedulerSupport("none")
    public final <K, V> Single<Map<K, Collection<V>>> toMultimap(Function<? super T, ? extends K> keySelector, Function<? super T, ? extends V> valueSelector, Callable<Map<K, Collection<V>>> mapSupplier) {
        return toMultimap(keySelector, valueSelector, mapSupplier, ArrayListSupplier.asFunction());
    }

    @BackpressureSupport(BackpressureKind.NONE)
    @SchedulerSupport("none")
    public final Observable<T> toObservable() {
        return RxJavaPlugins.onAssembly(new ObservableFromPublisher(this));
    }

    @BackpressureSupport(BackpressureKind.UNBOUNDED_IN)
    @SchedulerSupport("none")
    public final Single<List<T>> toSortedList() {
        return toSortedList(Functions.naturalComparator());
    }

    @BackpressureSupport(BackpressureKind.UNBOUNDED_IN)
    @SchedulerSupport("none")
    public final Single<List<T>> toSortedList(Comparator<? super T> comparator) {
        ObjectHelper.requireNonNull(comparator, "comparator is null");
        return toList().map(Functions.listSorter(comparator));
    }

    @BackpressureSupport(BackpressureKind.UNBOUNDED_IN)
    @SchedulerSupport("none")
    public final Single<List<T>> toSortedList(Comparator<? super T> comparator, int capacityHint) {
        ObjectHelper.requireNonNull(comparator, "comparator is null");
        return toList(capacityHint).map(Functions.listSorter(comparator));
    }

    @BackpressureSupport(BackpressureKind.UNBOUNDED_IN)
    @SchedulerSupport("none")
    public final Single<List<T>> toSortedList(int capacityHint) {
        return toSortedList(Functions.naturalComparator(), capacityHint);
    }

    @BackpressureSupport(BackpressureKind.PASS_THROUGH)
    @SchedulerSupport("custom")
    public final Flowable<T> unsubscribeOn(Scheduler scheduler) {
        ObjectHelper.requireNonNull(scheduler, "scheduler is null");
        return RxJavaPlugins.onAssembly(new FlowableUnsubscribeOn(this, scheduler));
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public final Flowable<Flowable<T>> window(long count) {
        return window(count, count, bufferSize());
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public final Flowable<Flowable<T>> window(long count, long skip) {
        return window(count, skip, bufferSize());
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public final Flowable<Flowable<T>> window(long count, long skip, int bufferSize) {
        ObjectHelper.verifyPositive(skip, "skip");
        ObjectHelper.verifyPositive(count, "count");
        ObjectHelper.verifyPositive(bufferSize, "bufferSize");
        return RxJavaPlugins.onAssembly(new FlowableWindow(this, count, skip, bufferSize));
    }

    @BackpressureSupport(BackpressureKind.ERROR)
    @SchedulerSupport("io.reactivex:computation")
    public final Flowable<Flowable<T>> window(long timespan, long timeskip, TimeUnit unit) {
        return window(timespan, timeskip, unit, Schedulers.computation(), bufferSize());
    }

    @BackpressureSupport(BackpressureKind.ERROR)
    @SchedulerSupport("custom")
    public final Flowable<Flowable<T>> window(long timespan, long timeskip, TimeUnit unit, Scheduler scheduler) {
        return window(timespan, timeskip, unit, scheduler, bufferSize());
    }

    @BackpressureSupport(BackpressureKind.ERROR)
    @SchedulerSupport("custom")
    public final Flowable<Flowable<T>> window(long timespan, long timeskip, TimeUnit unit, Scheduler scheduler, int bufferSize) {
        ObjectHelper.verifyPositive(bufferSize, "bufferSize");
        ObjectHelper.verifyPositive(timespan, "timespan");
        ObjectHelper.verifyPositive(timeskip, "timeskip");
        ObjectHelper.requireNonNull(scheduler, "scheduler is null");
        ObjectHelper.requireNonNull(unit, "unit is null");
        return RxJavaPlugins.onAssembly(new FlowableWindowTimed(this, timespan, timeskip, unit, scheduler, LongCompanionObject.MAX_VALUE, bufferSize, false));
    }

    /* JADX DEBUG: Failed to find minimal casts for resolve overloaded methods, cast all args instead
     method: dji.thirdparty.io.reactivex.Flowable.window(long, java.util.concurrent.TimeUnit, dji.thirdparty.io.reactivex.Scheduler, long, boolean):dji.thirdparty.io.reactivex.Flowable<dji.thirdparty.io.reactivex.Flowable<T>>
     arg types: [long, java.util.concurrent.TimeUnit, dji.thirdparty.io.reactivex.Scheduler, ?, int]
     candidates:
      dji.thirdparty.io.reactivex.Flowable.window(long, long, java.util.concurrent.TimeUnit, dji.thirdparty.io.reactivex.Scheduler, int):dji.thirdparty.io.reactivex.Flowable<dji.thirdparty.io.reactivex.Flowable<T>>
      dji.thirdparty.io.reactivex.Flowable.window(long, java.util.concurrent.TimeUnit, dji.thirdparty.io.reactivex.Scheduler, long, boolean):dji.thirdparty.io.reactivex.Flowable<dji.thirdparty.io.reactivex.Flowable<T>> */
    @BackpressureSupport(BackpressureKind.ERROR)
    @SchedulerSupport("io.reactivex:computation")
    public final Flowable<Flowable<T>> window(long timespan, TimeUnit unit) {
        return window(timespan, unit, Schedulers.computation(), (long) LongCompanionObject.MAX_VALUE, false);
    }

    /* JADX DEBUG: Failed to find minimal casts for resolve overloaded methods, cast all args instead
     method: dji.thirdparty.io.reactivex.Flowable.window(long, java.util.concurrent.TimeUnit, dji.thirdparty.io.reactivex.Scheduler, long, boolean):dji.thirdparty.io.reactivex.Flowable<dji.thirdparty.io.reactivex.Flowable<T>>
     arg types: [long, java.util.concurrent.TimeUnit, dji.thirdparty.io.reactivex.Scheduler, long, int]
     candidates:
      dji.thirdparty.io.reactivex.Flowable.window(long, long, java.util.concurrent.TimeUnit, dji.thirdparty.io.reactivex.Scheduler, int):dji.thirdparty.io.reactivex.Flowable<dji.thirdparty.io.reactivex.Flowable<T>>
      dji.thirdparty.io.reactivex.Flowable.window(long, java.util.concurrent.TimeUnit, dji.thirdparty.io.reactivex.Scheduler, long, boolean):dji.thirdparty.io.reactivex.Flowable<dji.thirdparty.io.reactivex.Flowable<T>> */
    @BackpressureSupport(BackpressureKind.ERROR)
    @SchedulerSupport("io.reactivex:computation")
    public final Flowable<Flowable<T>> window(long timespan, TimeUnit unit, long count) {
        return window(timespan, unit, Schedulers.computation(), count, false);
    }

    @BackpressureSupport(BackpressureKind.ERROR)
    @SchedulerSupport("io.reactivex:computation")
    public final Flowable<Flowable<T>> window(long timespan, TimeUnit unit, long count, boolean restart) {
        return window(timespan, unit, Schedulers.computation(), count, restart);
    }

    /* JADX DEBUG: Failed to find minimal casts for resolve overloaded methods, cast all args instead
     method: dji.thirdparty.io.reactivex.Flowable.window(long, java.util.concurrent.TimeUnit, dji.thirdparty.io.reactivex.Scheduler, long, boolean):dji.thirdparty.io.reactivex.Flowable<dji.thirdparty.io.reactivex.Flowable<T>>
     arg types: [long, java.util.concurrent.TimeUnit, dji.thirdparty.io.reactivex.Scheduler, ?, int]
     candidates:
      dji.thirdparty.io.reactivex.Flowable.window(long, long, java.util.concurrent.TimeUnit, dji.thirdparty.io.reactivex.Scheduler, int):dji.thirdparty.io.reactivex.Flowable<dji.thirdparty.io.reactivex.Flowable<T>>
      dji.thirdparty.io.reactivex.Flowable.window(long, java.util.concurrent.TimeUnit, dji.thirdparty.io.reactivex.Scheduler, long, boolean):dji.thirdparty.io.reactivex.Flowable<dji.thirdparty.io.reactivex.Flowable<T>> */
    @BackpressureSupport(BackpressureKind.ERROR)
    @SchedulerSupport("custom")
    public final Flowable<Flowable<T>> window(long timespan, TimeUnit unit, Scheduler scheduler) {
        return window(timespan, unit, scheduler, (long) LongCompanionObject.MAX_VALUE, false);
    }

    /* JADX DEBUG: Failed to find minimal casts for resolve overloaded methods, cast all args instead
     method: dji.thirdparty.io.reactivex.Flowable.window(long, java.util.concurrent.TimeUnit, dji.thirdparty.io.reactivex.Scheduler, long, boolean):dji.thirdparty.io.reactivex.Flowable<dji.thirdparty.io.reactivex.Flowable<T>>
     arg types: [long, java.util.concurrent.TimeUnit, dji.thirdparty.io.reactivex.Scheduler, long, int]
     candidates:
      dji.thirdparty.io.reactivex.Flowable.window(long, long, java.util.concurrent.TimeUnit, dji.thirdparty.io.reactivex.Scheduler, int):dji.thirdparty.io.reactivex.Flowable<dji.thirdparty.io.reactivex.Flowable<T>>
      dji.thirdparty.io.reactivex.Flowable.window(long, java.util.concurrent.TimeUnit, dji.thirdparty.io.reactivex.Scheduler, long, boolean):dji.thirdparty.io.reactivex.Flowable<dji.thirdparty.io.reactivex.Flowable<T>> */
    @BackpressureSupport(BackpressureKind.ERROR)
    @SchedulerSupport("custom")
    public final Flowable<Flowable<T>> window(long timespan, TimeUnit unit, Scheduler scheduler, long count) {
        return window(timespan, unit, scheduler, count, false);
    }

    @BackpressureSupport(BackpressureKind.ERROR)
    @SchedulerSupport("custom")
    public final Flowable<Flowable<T>> window(long timespan, TimeUnit unit, Scheduler scheduler, long count, boolean restart) {
        return window(timespan, unit, scheduler, count, restart, bufferSize());
    }

    @BackpressureSupport(BackpressureKind.ERROR)
    @SchedulerSupport("custom")
    public final Flowable<Flowable<T>> window(long timespan, TimeUnit unit, Scheduler scheduler, long count, boolean restart, int bufferSize) {
        ObjectHelper.verifyPositive(bufferSize, "bufferSize");
        ObjectHelper.requireNonNull(scheduler, "scheduler is null");
        ObjectHelper.requireNonNull(unit, "unit is null");
        ObjectHelper.verifyPositive(count, "count");
        return RxJavaPlugins.onAssembly(new FlowableWindowTimed(this, timespan, timespan, unit, scheduler, count, bufferSize, restart));
    }

    /* JADX WARN: Type inference failed for: r2v0, types: [org.reactivestreams.Publisher<B>, org.reactivestreams.Publisher], assign insn: null */
    /* JADX WARNING: Unknown variable types count: 1 */
    @dji.thirdparty.io.reactivex.annotations.BackpressureSupport(dji.thirdparty.io.reactivex.annotations.BackpressureKind.ERROR)
    @dji.thirdparty.io.reactivex.annotations.SchedulerSupport("none")
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final <B> dji.thirdparty.io.reactivex.Flowable<dji.thirdparty.io.reactivex.Flowable<T>> window(org.reactivestreams.Publisher<B> r2) {
        /*
            r1 = this;
            int r0 = bufferSize()
            dji.thirdparty.io.reactivex.Flowable r0 = r1.window(r2, r0)
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: dji.thirdparty.io.reactivex.Flowable.window(org.reactivestreams.Publisher):dji.thirdparty.io.reactivex.Flowable");
    }

    @BackpressureSupport(BackpressureKind.ERROR)
    @SchedulerSupport("none")
    public final <B> Flowable<Flowable<T>> window(Publisher<B> boundaryIndicator, int bufferSize) {
        ObjectHelper.requireNonNull(boundaryIndicator, "boundaryIndicator is null");
        return RxJavaPlugins.onAssembly(new FlowableWindowBoundary(this, boundaryIndicator, bufferSize));
    }

    /* JADX WARN: Type inference failed for: r2v0, types: [org.reactivestreams.Publisher<U>, org.reactivestreams.Publisher], assign insn: null */
    /* JADX WARNING: Unknown variable types count: 1 */
    @dji.thirdparty.io.reactivex.annotations.BackpressureSupport(dji.thirdparty.io.reactivex.annotations.BackpressureKind.ERROR)
    @dji.thirdparty.io.reactivex.annotations.SchedulerSupport("none")
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final <U, V> dji.thirdparty.io.reactivex.Flowable<dji.thirdparty.io.reactivex.Flowable<T>> window(org.reactivestreams.Publisher<U> r2, dji.thirdparty.io.reactivex.functions.Function<? super U, ? extends org.reactivestreams.Publisher<V>> r3) {
        /*
            r1 = this;
            int r0 = bufferSize()
            dji.thirdparty.io.reactivex.Flowable r0 = r1.window(r2, r3, r0)
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: dji.thirdparty.io.reactivex.Flowable.window(org.reactivestreams.Publisher, dji.thirdparty.io.reactivex.functions.Function):dji.thirdparty.io.reactivex.Flowable");
    }

    @BackpressureSupport(BackpressureKind.ERROR)
    @SchedulerSupport("none")
    public final <U, V> Flowable<Flowable<T>> window(Publisher<U> openingIndicator, Function<? super U, ? extends Publisher<V>> closingIndicator, int bufferSize) {
        ObjectHelper.requireNonNull(openingIndicator, "openingIndicator is null");
        ObjectHelper.requireNonNull(closingIndicator, "closingIndicator is null");
        return RxJavaPlugins.onAssembly(new FlowableWindowBoundarySelector(this, openingIndicator, closingIndicator, bufferSize));
    }

    @BackpressureSupport(BackpressureKind.ERROR)
    @SchedulerSupport("none")
    public final <B> Flowable<Flowable<T>> window(Callable<? extends Publisher<B>> boundaryIndicatorSupplier) {
        return window(boundaryIndicatorSupplier, bufferSize());
    }

    @BackpressureSupport(BackpressureKind.ERROR)
    @SchedulerSupport("none")
    public final <B> Flowable<Flowable<T>> window(Callable<? extends Publisher<B>> boundaryIndicatorSupplier, int bufferSize) {
        ObjectHelper.requireNonNull(boundaryIndicatorSupplier, "boundaryIndicatorSupplier is null");
        return RxJavaPlugins.onAssembly(new FlowableWindowBoundarySupplier(this, boundaryIndicatorSupplier, bufferSize));
    }

    @BackpressureSupport(BackpressureKind.PASS_THROUGH)
    @SchedulerSupport("none")
    public final <U, R> Flowable<R> withLatestFrom(Publisher<? extends U> other, BiFunction<? super T, ? super U, ? extends R> combiner) {
        ObjectHelper.requireNonNull(other, "other is null");
        ObjectHelper.requireNonNull(combiner, "combiner is null");
        return RxJavaPlugins.onAssembly(new FlowableWithLatestFrom(this, combiner, other));
    }

    @BackpressureSupport(BackpressureKind.PASS_THROUGH)
    @SchedulerSupport("none")
    public final <T1, T2, R> Flowable<R> withLatestFrom(Publisher<T1> source1, Publisher<T2> source2, Function3<? super T, ? super T1, ? super T2, R> combiner) {
        return withLatestFrom(new Publisher[]{source1, source2}, Functions.toFunction(combiner));
    }

    @BackpressureSupport(BackpressureKind.PASS_THROUGH)
    @SchedulerSupport("none")
    public final <T1, T2, T3, R> Flowable<R> withLatestFrom(Publisher<T1> source1, Publisher<T2> source2, Publisher<T3> source3, Function4<? super T, ? super T1, ? super T2, ? super T3, R> combiner) {
        return withLatestFrom(new Publisher[]{source1, source2, source3}, Functions.toFunction(combiner));
    }

    @BackpressureSupport(BackpressureKind.PASS_THROUGH)
    @SchedulerSupport("none")
    public final <T1, T2, T3, T4, R> Flowable<R> withLatestFrom(Publisher<T1> source1, Publisher<T2> source2, Publisher<T3> source3, Publisher<T4> source4, Function5<? super T, ? super T1, ? super T2, ? super T3, ? super T4, R> combiner) {
        return withLatestFrom(new Publisher[]{source1, source2, source3, source4}, Functions.toFunction(combiner));
    }

    @BackpressureSupport(BackpressureKind.PASS_THROUGH)
    @SchedulerSupport("none")
    public final <R> Flowable<R> withLatestFrom(Publisher<?>[] others, Function<? super Object[], R> combiner) {
        ObjectHelper.requireNonNull(others, "others is null");
        ObjectHelper.requireNonNull(combiner, "combiner is null");
        return RxJavaPlugins.onAssembly(new FlowableWithLatestFromMany(this, others, combiner));
    }

    @BackpressureSupport(BackpressureKind.PASS_THROUGH)
    @SchedulerSupport("none")
    public final <R> Flowable<R> withLatestFrom(Iterable<? extends Publisher<?>> others, Function<? super Object[], R> combiner) {
        ObjectHelper.requireNonNull(others, "others is null");
        ObjectHelper.requireNonNull(combiner, "combiner is null");
        return RxJavaPlugins.onAssembly(new FlowableWithLatestFromMany(this, others, combiner));
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public final <U, R> Flowable<R> zipWith(Iterable<U> other, BiFunction<? super T, ? super U, ? extends R> zipper) {
        ObjectHelper.requireNonNull(other, "other is null");
        ObjectHelper.requireNonNull(zipper, "zipper is null");
        return RxJavaPlugins.onAssembly(new FlowableZipIterable(this, other, zipper));
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public final <U, R> Flowable<R> zipWith(Publisher<? extends U> other, BiFunction<? super T, ? super U, ? extends R> zipper) {
        ObjectHelper.requireNonNull(other, "other is null");
        return zip(this, other, zipper);
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public final <U, R> Flowable<R> zipWith(Publisher<? extends U> other, BiFunction<? super T, ? super U, ? extends R> zipper, boolean delayError) {
        return zip(this, other, zipper, delayError);
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public final <U, R> Flowable<R> zipWith(Publisher<? extends U> other, BiFunction<? super T, ? super U, ? extends R> zipper, boolean delayError, int bufferSize) {
        return zip(this, other, zipper, delayError, bufferSize);
    }

    @BackpressureSupport(BackpressureKind.UNBOUNDED_IN)
    @SchedulerSupport("none")
    public final TestSubscriber<T> test() {
        TestSubscriber<T> ts = new TestSubscriber<>();
        subscribe(ts);
        return ts;
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public final TestSubscriber<T> test(long initialRequest) {
        TestSubscriber<T> ts = new TestSubscriber<>(initialRequest);
        subscribe(ts);
        return ts;
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public final TestSubscriber<T> test(long initialRequest, boolean cancel) {
        TestSubscriber<T> ts = new TestSubscriber<>(initialRequest);
        if (cancel) {
            ts.cancel();
        }
        subscribe(ts);
        return ts;
    }
}
