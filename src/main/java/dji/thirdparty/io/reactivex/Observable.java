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
import dji.thirdparty.io.reactivex.internal.observers.BlockingFirstObserver;
import dji.thirdparty.io.reactivex.internal.observers.BlockingLastObserver;
import dji.thirdparty.io.reactivex.internal.observers.ForEachWhileObserver;
import dji.thirdparty.io.reactivex.internal.observers.FutureObserver;
import dji.thirdparty.io.reactivex.internal.observers.LambdaObserver;
import dji.thirdparty.io.reactivex.internal.operators.flowable.FlowableFromObservable;
import dji.thirdparty.io.reactivex.internal.operators.flowable.FlowableOnBackpressureError;
import dji.thirdparty.io.reactivex.internal.operators.observable.BlockingObservableIterable;
import dji.thirdparty.io.reactivex.internal.operators.observable.BlockingObservableLatest;
import dji.thirdparty.io.reactivex.internal.operators.observable.BlockingObservableMostRecent;
import dji.thirdparty.io.reactivex.internal.operators.observable.BlockingObservableNext;
import dji.thirdparty.io.reactivex.internal.operators.observable.ObservableAllSingle;
import dji.thirdparty.io.reactivex.internal.operators.observable.ObservableAmb;
import dji.thirdparty.io.reactivex.internal.operators.observable.ObservableAnySingle;
import dji.thirdparty.io.reactivex.internal.operators.observable.ObservableBlockingSubscribe;
import dji.thirdparty.io.reactivex.internal.operators.observable.ObservableBuffer;
import dji.thirdparty.io.reactivex.internal.operators.observable.ObservableBufferBoundary;
import dji.thirdparty.io.reactivex.internal.operators.observable.ObservableBufferBoundarySupplier;
import dji.thirdparty.io.reactivex.internal.operators.observable.ObservableBufferExactBoundary;
import dji.thirdparty.io.reactivex.internal.operators.observable.ObservableBufferTimed;
import dji.thirdparty.io.reactivex.internal.operators.observable.ObservableCache;
import dji.thirdparty.io.reactivex.internal.operators.observable.ObservableCollectSingle;
import dji.thirdparty.io.reactivex.internal.operators.observable.ObservableCombineLatest;
import dji.thirdparty.io.reactivex.internal.operators.observable.ObservableConcatMap;
import dji.thirdparty.io.reactivex.internal.operators.observable.ObservableConcatMapEager;
import dji.thirdparty.io.reactivex.internal.operators.observable.ObservableCountSingle;
import dji.thirdparty.io.reactivex.internal.operators.observable.ObservableCreate;
import dji.thirdparty.io.reactivex.internal.operators.observable.ObservableDebounce;
import dji.thirdparty.io.reactivex.internal.operators.observable.ObservableDebounceTimed;
import dji.thirdparty.io.reactivex.internal.operators.observable.ObservableDefer;
import dji.thirdparty.io.reactivex.internal.operators.observable.ObservableDelay;
import dji.thirdparty.io.reactivex.internal.operators.observable.ObservableDelaySubscriptionOther;
import dji.thirdparty.io.reactivex.internal.operators.observable.ObservableDematerialize;
import dji.thirdparty.io.reactivex.internal.operators.observable.ObservableDetach;
import dji.thirdparty.io.reactivex.internal.operators.observable.ObservableDistinct;
import dji.thirdparty.io.reactivex.internal.operators.observable.ObservableDistinctUntilChanged;
import dji.thirdparty.io.reactivex.internal.operators.observable.ObservableDoOnEach;
import dji.thirdparty.io.reactivex.internal.operators.observable.ObservableDoOnLifecycle;
import dji.thirdparty.io.reactivex.internal.operators.observable.ObservableElementAtMaybe;
import dji.thirdparty.io.reactivex.internal.operators.observable.ObservableElementAtSingle;
import dji.thirdparty.io.reactivex.internal.operators.observable.ObservableEmpty;
import dji.thirdparty.io.reactivex.internal.operators.observable.ObservableError;
import dji.thirdparty.io.reactivex.internal.operators.observable.ObservableFilter;
import dji.thirdparty.io.reactivex.internal.operators.observable.ObservableFlatMap;
import dji.thirdparty.io.reactivex.internal.operators.observable.ObservableFlatMapCompletableCompletable;
import dji.thirdparty.io.reactivex.internal.operators.observable.ObservableFlatMapMaybe;
import dji.thirdparty.io.reactivex.internal.operators.observable.ObservableFlatMapSingle;
import dji.thirdparty.io.reactivex.internal.operators.observable.ObservableFlattenIterable;
import dji.thirdparty.io.reactivex.internal.operators.observable.ObservableFromArray;
import dji.thirdparty.io.reactivex.internal.operators.observable.ObservableFromCallable;
import dji.thirdparty.io.reactivex.internal.operators.observable.ObservableFromFuture;
import dji.thirdparty.io.reactivex.internal.operators.observable.ObservableFromIterable;
import dji.thirdparty.io.reactivex.internal.operators.observable.ObservableFromPublisher;
import dji.thirdparty.io.reactivex.internal.operators.observable.ObservableFromUnsafeSource;
import dji.thirdparty.io.reactivex.internal.operators.observable.ObservableGenerate;
import dji.thirdparty.io.reactivex.internal.operators.observable.ObservableGroupBy;
import dji.thirdparty.io.reactivex.internal.operators.observable.ObservableGroupJoin;
import dji.thirdparty.io.reactivex.internal.operators.observable.ObservableHide;
import dji.thirdparty.io.reactivex.internal.operators.observable.ObservableIgnoreElements;
import dji.thirdparty.io.reactivex.internal.operators.observable.ObservableIgnoreElementsCompletable;
import dji.thirdparty.io.reactivex.internal.operators.observable.ObservableInternalHelper;
import dji.thirdparty.io.reactivex.internal.operators.observable.ObservableInterval;
import dji.thirdparty.io.reactivex.internal.operators.observable.ObservableIntervalRange;
import dji.thirdparty.io.reactivex.internal.operators.observable.ObservableJoin;
import dji.thirdparty.io.reactivex.internal.operators.observable.ObservableJust;
import dji.thirdparty.io.reactivex.internal.operators.observable.ObservableLastMaybe;
import dji.thirdparty.io.reactivex.internal.operators.observable.ObservableLastSingle;
import dji.thirdparty.io.reactivex.internal.operators.observable.ObservableLift;
import dji.thirdparty.io.reactivex.internal.operators.observable.ObservableMap;
import dji.thirdparty.io.reactivex.internal.operators.observable.ObservableMapNotification;
import dji.thirdparty.io.reactivex.internal.operators.observable.ObservableMaterialize;
import dji.thirdparty.io.reactivex.internal.operators.observable.ObservableNever;
import dji.thirdparty.io.reactivex.internal.operators.observable.ObservableObserveOn;
import dji.thirdparty.io.reactivex.internal.operators.observable.ObservableOnErrorNext;
import dji.thirdparty.io.reactivex.internal.operators.observable.ObservableOnErrorReturn;
import dji.thirdparty.io.reactivex.internal.operators.observable.ObservablePublish;
import dji.thirdparty.io.reactivex.internal.operators.observable.ObservablePublishSelector;
import dji.thirdparty.io.reactivex.internal.operators.observable.ObservableRange;
import dji.thirdparty.io.reactivex.internal.operators.observable.ObservableRangeLong;
import dji.thirdparty.io.reactivex.internal.operators.observable.ObservableRedo;
import dji.thirdparty.io.reactivex.internal.operators.observable.ObservableRepeat;
import dji.thirdparty.io.reactivex.internal.operators.observable.ObservableRepeatUntil;
import dji.thirdparty.io.reactivex.internal.operators.observable.ObservableReplay;
import dji.thirdparty.io.reactivex.internal.operators.observable.ObservableRetryBiPredicate;
import dji.thirdparty.io.reactivex.internal.operators.observable.ObservableRetryPredicate;
import dji.thirdparty.io.reactivex.internal.operators.observable.ObservableSampleTimed;
import dji.thirdparty.io.reactivex.internal.operators.observable.ObservableSampleWithObservable;
import dji.thirdparty.io.reactivex.internal.operators.observable.ObservableScan;
import dji.thirdparty.io.reactivex.internal.operators.observable.ObservableScanSeed;
import dji.thirdparty.io.reactivex.internal.operators.observable.ObservableSequenceEqualSingle;
import dji.thirdparty.io.reactivex.internal.operators.observable.ObservableSerialized;
import dji.thirdparty.io.reactivex.internal.operators.observable.ObservableSingleMaybe;
import dji.thirdparty.io.reactivex.internal.operators.observable.ObservableSingleSingle;
import dji.thirdparty.io.reactivex.internal.operators.observable.ObservableSkip;
import dji.thirdparty.io.reactivex.internal.operators.observable.ObservableSkipLast;
import dji.thirdparty.io.reactivex.internal.operators.observable.ObservableSkipLastTimed;
import dji.thirdparty.io.reactivex.internal.operators.observable.ObservableSkipUntil;
import dji.thirdparty.io.reactivex.internal.operators.observable.ObservableSkipWhile;
import dji.thirdparty.io.reactivex.internal.operators.observable.ObservableSubscribeOn;
import dji.thirdparty.io.reactivex.internal.operators.observable.ObservableSwitchIfEmpty;
import dji.thirdparty.io.reactivex.internal.operators.observable.ObservableSwitchMap;
import dji.thirdparty.io.reactivex.internal.operators.observable.ObservableTake;
import dji.thirdparty.io.reactivex.internal.operators.observable.ObservableTakeLast;
import dji.thirdparty.io.reactivex.internal.operators.observable.ObservableTakeLastOne;
import dji.thirdparty.io.reactivex.internal.operators.observable.ObservableTakeLastTimed;
import dji.thirdparty.io.reactivex.internal.operators.observable.ObservableTakeUntil;
import dji.thirdparty.io.reactivex.internal.operators.observable.ObservableTakeUntilPredicate;
import dji.thirdparty.io.reactivex.internal.operators.observable.ObservableTakeWhile;
import dji.thirdparty.io.reactivex.internal.operators.observable.ObservableThrottleFirstTimed;
import dji.thirdparty.io.reactivex.internal.operators.observable.ObservableTimeInterval;
import dji.thirdparty.io.reactivex.internal.operators.observable.ObservableTimeout;
import dji.thirdparty.io.reactivex.internal.operators.observable.ObservableTimeoutTimed;
import dji.thirdparty.io.reactivex.internal.operators.observable.ObservableTimer;
import dji.thirdparty.io.reactivex.internal.operators.observable.ObservableToList;
import dji.thirdparty.io.reactivex.internal.operators.observable.ObservableToListSingle;
import dji.thirdparty.io.reactivex.internal.operators.observable.ObservableUnsubscribeOn;
import dji.thirdparty.io.reactivex.internal.operators.observable.ObservableUsing;
import dji.thirdparty.io.reactivex.internal.operators.observable.ObservableWindow;
import dji.thirdparty.io.reactivex.internal.operators.observable.ObservableWindowBoundary;
import dji.thirdparty.io.reactivex.internal.operators.observable.ObservableWindowBoundarySelector;
import dji.thirdparty.io.reactivex.internal.operators.observable.ObservableWindowBoundarySupplier;
import dji.thirdparty.io.reactivex.internal.operators.observable.ObservableWindowTimed;
import dji.thirdparty.io.reactivex.internal.operators.observable.ObservableWithLatestFrom;
import dji.thirdparty.io.reactivex.internal.operators.observable.ObservableWithLatestFromMany;
import dji.thirdparty.io.reactivex.internal.operators.observable.ObservableZip;
import dji.thirdparty.io.reactivex.internal.operators.observable.ObservableZipIterable;
import dji.thirdparty.io.reactivex.internal.util.ArrayListSupplier;
import dji.thirdparty.io.reactivex.internal.util.ErrorMode;
import dji.thirdparty.io.reactivex.internal.util.ExceptionHelper;
import dji.thirdparty.io.reactivex.internal.util.HashMapSupplier;
import dji.thirdparty.io.reactivex.observables.ConnectableObservable;
import dji.thirdparty.io.reactivex.observables.GroupedObservable;
import dji.thirdparty.io.reactivex.observers.SafeObserver;
import dji.thirdparty.io.reactivex.observers.TestObserver;
import dji.thirdparty.io.reactivex.plugins.RxJavaPlugins;
import dji.thirdparty.io.reactivex.schedulers.Schedulers;
import dji.thirdparty.io.reactivex.schedulers.Timed;
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

public abstract class Observable<T> implements ObservableSource<T> {
    /* access modifiers changed from: protected */
    public abstract void subscribeActual(Observer<? super T> observer);

    @SchedulerSupport("none")
    public static <T> Observable<T> amb(Iterable<? extends ObservableSource<? extends T>> sources) {
        ObjectHelper.requireNonNull(sources, "sources is null");
        return RxJavaPlugins.onAssembly(new ObservableAmb(null, sources));
    }

    @SchedulerSupport("none")
    public static <T> Observable<T> ambArray(ObservableSource<? extends T>... sources) {
        ObjectHelper.requireNonNull(sources, "sources is null");
        int len = sources.length;
        if (len == 0) {
            return empty();
        }
        if (len == 1) {
            return wrap(sources[0]);
        }
        return RxJavaPlugins.onAssembly(new ObservableAmb(sources, null));
    }

    public static int bufferSize() {
        return Flowable.bufferSize();
    }

    @SchedulerSupport("none")
    public static <T, R> Observable<R> combineLatest(Function<? super Object[], ? extends R> combiner, int bufferSize, ObservableSource<? extends T>... sources) {
        return combineLatest(sources, combiner, bufferSize);
    }

    @SchedulerSupport("none")
    public static <T, R> Observable<R> combineLatest(Iterable<? extends ObservableSource<? extends T>> sources, Function<? super Object[], ? extends R> combiner) {
        return combineLatest(sources, combiner, bufferSize());
    }

    @SchedulerSupport("none")
    public static <T, R> Observable<R> combineLatest(Iterable<? extends ObservableSource<? extends T>> sources, Function<? super Object[], ? extends R> combiner, int bufferSize) {
        ObjectHelper.requireNonNull(sources, "sources is null");
        ObjectHelper.requireNonNull(combiner, "combiner is null");
        ObjectHelper.verifyPositive(bufferSize, "bufferSize");
        return RxJavaPlugins.onAssembly(new ObservableCombineLatest(null, sources, combiner, bufferSize << 1, false));
    }

    @SchedulerSupport("none")
    public static <T, R> Observable<R> combineLatest(ObservableSource<? extends T>[] sources, Function<? super Object[], ? extends R> combiner) {
        return combineLatest(sources, combiner, bufferSize());
    }

    @SchedulerSupport("none")
    public static <T, R> Observable<R> combineLatest(ObservableSource<? extends T>[] sources, Function<? super Object[], ? extends R> combiner, int bufferSize) {
        ObjectHelper.requireNonNull(sources, "sources is null");
        if (sources.length == 0) {
            return empty();
        }
        ObjectHelper.requireNonNull(combiner, "combiner is null");
        ObjectHelper.verifyPositive(bufferSize, "bufferSize");
        return RxJavaPlugins.onAssembly(new ObservableCombineLatest(sources, null, combiner, bufferSize << 1, false));
    }

    @SchedulerSupport("none")
    public static <T1, T2, R> Observable<R> combineLatest(ObservableSource<? extends T1> source1, ObservableSource<? extends T2> source2, BiFunction<? super T1, ? super T2, ? extends R> combiner) {
        return combineLatest(Functions.toFunction(combiner), bufferSize(), source1, source2);
    }

    @SchedulerSupport("none")
    public static <T1, T2, T3, R> Observable<R> combineLatest(ObservableSource<? extends T1> source1, ObservableSource<? extends T2> source2, ObservableSource<? extends T3> source3, Function3<? super T1, ? super T2, ? super T3, ? extends R> combiner) {
        return combineLatest(Functions.toFunction(combiner), bufferSize(), source1, source2, source3);
    }

    @SchedulerSupport("none")
    public static <T1, T2, T3, T4, R> Observable<R> combineLatest(ObservableSource<? extends T1> source1, ObservableSource<? extends T2> source2, ObservableSource<? extends T3> source3, ObservableSource<? extends T4> source4, Function4<? super T1, ? super T2, ? super T3, ? super T4, ? extends R> combiner) {
        return combineLatest(Functions.toFunction(combiner), bufferSize(), source1, source2, source3, source4);
    }

    @SchedulerSupport("none")
    public static <T1, T2, T3, T4, T5, R> Observable<R> combineLatest(ObservableSource<? extends T1> source1, ObservableSource<? extends T2> source2, ObservableSource<? extends T3> source3, ObservableSource<? extends T4> source4, ObservableSource<? extends T5> source5, Function5<? super T1, ? super T2, ? super T3, ? super T4, ? super T5, ? extends R> combiner) {
        return combineLatest(Functions.toFunction(combiner), bufferSize(), source1, source2, source3, source4, source5);
    }

    @SchedulerSupport("none")
    public static <T1, T2, T3, T4, T5, T6, R> Observable<R> combineLatest(ObservableSource<? extends T1> source1, ObservableSource<? extends T2> source2, ObservableSource<? extends T3> source3, ObservableSource<? extends T4> source4, ObservableSource<? extends T5> source5, ObservableSource<? extends T6> source6, Function6<? super T1, ? super T2, ? super T3, ? super T4, ? super T5, ? super T6, ? extends R> combiner) {
        return combineLatest(Functions.toFunction(combiner), bufferSize(), source1, source2, source3, source4, source5, source6);
    }

    @SchedulerSupport("none")
    public static <T1, T2, T3, T4, T5, T6, T7, R> Observable<R> combineLatest(ObservableSource<? extends T1> source1, ObservableSource<? extends T2> source2, ObservableSource<? extends T3> source3, ObservableSource<? extends T4> source4, ObservableSource<? extends T5> source5, ObservableSource<? extends T6> source6, ObservableSource<? extends T7> source7, Function7<? super T1, ? super T2, ? super T3, ? super T4, ? super T5, ? super T6, ? super T7, ? extends R> combiner) {
        return combineLatest(Functions.toFunction(combiner), bufferSize(), source1, source2, source3, source4, source5, source6, source7);
    }

    @SchedulerSupport("none")
    public static <T1, T2, T3, T4, T5, T6, T7, T8, R> Observable<R> combineLatest(ObservableSource<? extends T1> source1, ObservableSource<? extends T2> source2, ObservableSource<? extends T3> source3, ObservableSource<? extends T4> source4, ObservableSource<? extends T5> source5, ObservableSource<? extends T6> source6, ObservableSource<? extends T7> source7, ObservableSource<? extends T8> source8, Function8<? super T1, ? super T2, ? super T3, ? super T4, ? super T5, ? super T6, ? super T7, ? super T8, ? extends R> combiner) {
        return combineLatest(Functions.toFunction(combiner), bufferSize(), source1, source2, source3, source4, source5, source6, source7, source8);
    }

    @SchedulerSupport("none")
    public static <T1, T2, T3, T4, T5, T6, T7, T8, T9, R> Observable<R> combineLatest(ObservableSource<? extends T1> source1, ObservableSource<? extends T2> source2, ObservableSource<? extends T3> source3, ObservableSource<? extends T4> source4, ObservableSource<? extends T5> source5, ObservableSource<? extends T6> source6, ObservableSource<? extends T7> source7, ObservableSource<? extends T8> source8, ObservableSource<? extends T9> source9, Function9<? super T1, ? super T2, ? super T3, ? super T4, ? super T5, ? super T6, ? super T7, ? super T8, ? super T9, ? extends R> combiner) {
        return combineLatest(Functions.toFunction(combiner), bufferSize(), source1, source2, source3, source4, source5, source6, source7, source8, source9);
    }

    @SchedulerSupport("none")
    public static <T, R> Observable<R> combineLatestDelayError(ObservableSource<? extends T>[] sources, Function<? super Object[], ? extends R> combiner) {
        return combineLatestDelayError(sources, combiner, bufferSize());
    }

    @SchedulerSupport("none")
    public static <T, R> Observable<R> combineLatestDelayError(Function<? super Object[], ? extends R> combiner, int bufferSize, ObservableSource<? extends T>... sources) {
        return combineLatestDelayError(sources, combiner, bufferSize);
    }

    @SchedulerSupport("none")
    public static <T, R> Observable<R> combineLatestDelayError(ObservableSource<? extends T>[] sources, Function<? super Object[], ? extends R> combiner, int bufferSize) {
        ObjectHelper.verifyPositive(bufferSize, "bufferSize");
        ObjectHelper.requireNonNull(combiner, "combiner is null");
        if (sources.length == 0) {
            return empty();
        }
        return RxJavaPlugins.onAssembly(new ObservableCombineLatest(sources, null, combiner, bufferSize << 1, true));
    }

    @SchedulerSupport("none")
    public static <T, R> Observable<R> combineLatestDelayError(Iterable<? extends ObservableSource<? extends T>> sources, Function<? super Object[], ? extends R> combiner) {
        return combineLatestDelayError(sources, combiner, bufferSize());
    }

    @SchedulerSupport("none")
    public static <T, R> Observable<R> combineLatestDelayError(Iterable<? extends ObservableSource<? extends T>> sources, Function<? super Object[], ? extends R> combiner, int bufferSize) {
        ObjectHelper.requireNonNull(sources, "sources is null");
        ObjectHelper.requireNonNull(combiner, "combiner is null");
        ObjectHelper.verifyPositive(bufferSize, "bufferSize");
        return RxJavaPlugins.onAssembly(new ObservableCombineLatest(null, sources, combiner, bufferSize << 1, true));
    }

    /* JADX WARN: Type inference failed for: r4v0, types: [java.lang.Iterable<? extends dji.thirdparty.io.reactivex.ObservableSource<? extends T>>, java.lang.Object, java.lang.Iterable], assign insn: null */
    /* JADX WARNING: Unknown variable types count: 1 */
    @dji.thirdparty.io.reactivex.annotations.SchedulerSupport("none")
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static <T> dji.thirdparty.io.reactivex.Observable<T> concat(java.lang.Iterable<? extends dji.thirdparty.io.reactivex.ObservableSource<? extends T>> r4) {
        /*
            java.lang.String r0 = "sources is null"
            dji.thirdparty.io.reactivex.internal.functions.ObjectHelper.requireNonNull(r4, r0)
            dji.thirdparty.io.reactivex.Observable r0 = fromIterable(r4)
            dji.thirdparty.io.reactivex.functions.Function r1 = dji.thirdparty.io.reactivex.internal.functions.Functions.identity()
            int r2 = bufferSize()
            r3 = 0
            dji.thirdparty.io.reactivex.Observable r0 = r0.concatMapDelayError(r1, r2, r3)
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: dji.thirdparty.io.reactivex.Observable.concat(java.lang.Iterable):dji.thirdparty.io.reactivex.Observable");
    }

    @SchedulerSupport("none")
    public static <T> Observable<T> concat(ObservableSource<? extends ObservableSource<? extends T>> sources) {
        return concat(sources, bufferSize());
    }

    @SchedulerSupport("none")
    public static <T> Observable<T> concat(ObservableSource<? extends ObservableSource<? extends T>> sources, int prefetch) {
        ObjectHelper.requireNonNull(sources, "sources is null");
        return RxJavaPlugins.onAssembly(new ObservableConcatMap(sources, Functions.identity(), prefetch, ErrorMode.IMMEDIATE));
    }

    @SchedulerSupport("none")
    public static <T> Observable<T> concat(ObservableSource<? extends T> source1, ObservableSource<? extends T> source2) {
        return concatArray(source1, source2);
    }

    @SchedulerSupport("none")
    public static <T> Observable<T> concat(ObservableSource<? extends T> source1, ObservableSource<? extends T> source2, ObservableSource<? extends T> source3) {
        return concatArray(source1, source2, source3);
    }

    @SchedulerSupport("none")
    public static <T> Observable<T> concat(ObservableSource<? extends T> source1, ObservableSource<? extends T> source2, ObservableSource<? extends T> source3, ObservableSource<? extends T> source4) {
        return concatArray(source1, source2, source3, source4);
    }

    @SchedulerSupport("none")
    public static <T> Observable<T> concatArray(ObservableSource<? extends T>... sources) {
        if (sources.length == 0) {
            return empty();
        }
        if (sources.length == 1) {
            return wrap(sources[0]);
        }
        return RxJavaPlugins.onAssembly(new ObservableConcatMap(fromArray(sources), Functions.identity(), bufferSize(), ErrorMode.BOUNDARY));
    }

    @SchedulerSupport("none")
    public static <T> Observable<T> concatArrayDelayError(ObservableSource<? extends T>... sources) {
        if (sources.length == 0) {
            return empty();
        }
        if (sources.length == 1) {
            return wrap(sources[0]);
        }
        return concatDelayError(fromArray(sources));
    }

    @SchedulerSupport("none")
    public static <T> Observable<T> concatArrayEager(ObservableSource<? extends T>... sources) {
        return concatArrayEager(bufferSize(), bufferSize(), sources);
    }

    @SchedulerSupport("none")
    public static <T> Observable<T> concatArrayEager(int maxConcurrency, int prefetch, ObservableSource<? extends T>... sources) {
        return fromArray(sources).concatMapEagerDelayError(Functions.identity(), maxConcurrency, prefetch, false);
    }

    @SchedulerSupport("none")
    public static <T> Observable<T> concatDelayError(Iterable<? extends ObservableSource<? extends T>> sources) {
        ObjectHelper.requireNonNull(sources, "sources is null");
        return concatDelayError(fromIterable(sources));
    }

    @SchedulerSupport("none")
    public static <T> Observable<T> concatDelayError(ObservableSource<? extends ObservableSource<? extends T>> sources) {
        return concatDelayError(sources, bufferSize(), true);
    }

    @SchedulerSupport("none")
    public static <T> Observable<T> concatDelayError(ObservableSource<? extends ObservableSource<? extends T>> sources, int prefetch, boolean tillTheEnd) {
        return RxJavaPlugins.onAssembly(new ObservableConcatMap(sources, Functions.identity(), prefetch, tillTheEnd ? ErrorMode.END : ErrorMode.BOUNDARY));
    }

    @SchedulerSupport("none")
    public static <T> Observable<T> concatEager(ObservableSource<? extends ObservableSource<? extends T>> sources) {
        return concatEager(sources, bufferSize(), bufferSize());
    }

    /* JADX WARN: Type inference failed for: r2v0, types: [dji.thirdparty.io.reactivex.ObservableSource<? extends dji.thirdparty.io.reactivex.ObservableSource<? extends T>>, dji.thirdparty.io.reactivex.ObservableSource], assign insn: null */
    /* JADX WARNING: Unknown variable types count: 1 */
    @dji.thirdparty.io.reactivex.annotations.SchedulerSupport("none")
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static <T> dji.thirdparty.io.reactivex.Observable<T> concatEager(dji.thirdparty.io.reactivex.ObservableSource<? extends dji.thirdparty.io.reactivex.ObservableSource<? extends T>> r2, int r3, int r4) {
        /*
            dji.thirdparty.io.reactivex.Observable r0 = wrap(r2)
            dji.thirdparty.io.reactivex.functions.Function r1 = dji.thirdparty.io.reactivex.internal.functions.Functions.identity()
            dji.thirdparty.io.reactivex.Observable r0 = r0.concatMapEager(r1, r3, r4)
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: dji.thirdparty.io.reactivex.Observable.concatEager(dji.thirdparty.io.reactivex.ObservableSource, int, int):dji.thirdparty.io.reactivex.Observable");
    }

    @SchedulerSupport("none")
    public static <T> Observable<T> concatEager(Iterable<? extends ObservableSource<? extends T>> sources) {
        return concatEager(sources, bufferSize(), bufferSize());
    }

    /* JADX WARN: Type inference failed for: r3v0, types: [java.lang.Iterable<? extends dji.thirdparty.io.reactivex.ObservableSource<? extends T>>, java.lang.Iterable], assign insn: null */
    /* JADX WARNING: Unknown variable types count: 1 */
    @dji.thirdparty.io.reactivex.annotations.SchedulerSupport("none")
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static <T> dji.thirdparty.io.reactivex.Observable<T> concatEager(java.lang.Iterable<? extends dji.thirdparty.io.reactivex.ObservableSource<? extends T>> r3, int r4, int r5) {
        /*
            dji.thirdparty.io.reactivex.Observable r0 = fromIterable(r3)
            dji.thirdparty.io.reactivex.functions.Function r1 = dji.thirdparty.io.reactivex.internal.functions.Functions.identity()
            r2 = 0
            dji.thirdparty.io.reactivex.Observable r0 = r0.concatMapEagerDelayError(r1, r4, r5, r2)
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: dji.thirdparty.io.reactivex.Observable.concatEager(java.lang.Iterable, int, int):dji.thirdparty.io.reactivex.Observable");
    }

    @SchedulerSupport("none")
    public static <T> Observable<T> create(ObservableOnSubscribe<T> source) {
        ObjectHelper.requireNonNull(source, "source is null");
        return RxJavaPlugins.onAssembly(new ObservableCreate(source));
    }

    @SchedulerSupport("none")
    public static <T> Observable<T> defer(Callable<? extends ObservableSource<? extends T>> supplier) {
        ObjectHelper.requireNonNull(supplier, "supplier is null");
        return RxJavaPlugins.onAssembly(new ObservableDefer(supplier));
    }

    @SchedulerSupport("none")
    public static <T> Observable<T> empty() {
        return RxJavaPlugins.onAssembly(ObservableEmpty.INSTANCE);
    }

    @SchedulerSupport("none")
    public static <T> Observable<T> error(Callable<? extends Throwable> errorSupplier) {
        ObjectHelper.requireNonNull(errorSupplier, "errorSupplier is null");
        return RxJavaPlugins.onAssembly(new ObservableError(errorSupplier));
    }

    @SchedulerSupport("none")
    public static <T> Observable<T> error(Throwable exception) {
        ObjectHelper.requireNonNull(exception, "e is null");
        return error(Functions.justCallable(exception));
    }

    @SchedulerSupport("none")
    public static <T> Observable<T> fromArray(T... items) {
        ObjectHelper.requireNonNull(items, "items is null");
        if (items.length == 0) {
            return empty();
        }
        if (items.length == 1) {
            return just(items[0]);
        }
        return RxJavaPlugins.onAssembly(new ObservableFromArray(items));
    }

    @SchedulerSupport("none")
    public static <T> Observable<T> fromCallable(Callable<? extends T> supplier) {
        ObjectHelper.requireNonNull(supplier, "supplier is null");
        return RxJavaPlugins.onAssembly(new ObservableFromCallable(supplier));
    }

    @SchedulerSupport("none")
    public static <T> Observable<T> fromFuture(Future<? extends T> future) {
        ObjectHelper.requireNonNull(future, "future is null");
        return RxJavaPlugins.onAssembly(new ObservableFromFuture(future, 0, null));
    }

    @SchedulerSupport("none")
    public static <T> Observable<T> fromFuture(Future<? extends T> future, long timeout, TimeUnit unit) {
        ObjectHelper.requireNonNull(future, "future is null");
        ObjectHelper.requireNonNull(unit, "unit is null");
        return RxJavaPlugins.onAssembly(new ObservableFromFuture(future, timeout, unit));
    }

    @SchedulerSupport("custom")
    public static <T> Observable<T> fromFuture(Future<? extends T> future, long timeout, TimeUnit unit, Scheduler scheduler) {
        ObjectHelper.requireNonNull(scheduler, "scheduler is null");
        return fromFuture(future, timeout, unit).subscribeOn(scheduler);
    }

    @SchedulerSupport("custom")
    public static <T> Observable<T> fromFuture(Future<? extends T> future, Scheduler scheduler) {
        ObjectHelper.requireNonNull(scheduler, "scheduler is null");
        return fromFuture(future).subscribeOn(scheduler);
    }

    @SchedulerSupport("none")
    public static <T> Observable<T> fromIterable(Iterable<? extends T> source) {
        ObjectHelper.requireNonNull(source, "source is null");
        return RxJavaPlugins.onAssembly(new ObservableFromIterable(source));
    }

    @BackpressureSupport(BackpressureKind.UNBOUNDED_IN)
    @SchedulerSupport("none")
    public static <T> Observable<T> fromPublisher(Publisher<? extends T> publisher) {
        ObjectHelper.requireNonNull(publisher, "publisher is null");
        return RxJavaPlugins.onAssembly(new ObservableFromPublisher(publisher));
    }

    @SchedulerSupport("none")
    public static <T> Observable<T> generate(Consumer<Emitter<T>> generator) {
        ObjectHelper.requireNonNull(generator, "generator  is null");
        return generate(Functions.nullSupplier(), ObservableInternalHelper.simpleGenerator(generator), Functions.emptyConsumer());
    }

    @SchedulerSupport("none")
    public static <T, S> Observable<T> generate(Callable<S> initialState, BiConsumer<S, Emitter<T>> generator) {
        ObjectHelper.requireNonNull(generator, "generator  is null");
        return generate(initialState, ObservableInternalHelper.simpleBiGenerator(generator), Functions.emptyConsumer());
    }

    @SchedulerSupport("none")
    public static <T, S> Observable<T> generate(Callable<S> initialState, BiConsumer<S, Emitter<T>> generator, Consumer<? super S> disposeState) {
        ObjectHelper.requireNonNull(generator, "generator  is null");
        return generate(initialState, ObservableInternalHelper.simpleBiGenerator(generator), disposeState);
    }

    @SchedulerSupport("none")
    public static <T, S> Observable<T> generate(Callable<S> initialState, BiFunction<S, Emitter<T>, S> generator) {
        return generate(initialState, generator, Functions.emptyConsumer());
    }

    @SchedulerSupport("none")
    public static <T, S> Observable<T> generate(Callable<S> initialState, BiFunction<S, Emitter<T>, S> generator, Consumer<? super S> disposeState) {
        ObjectHelper.requireNonNull(initialState, "initialState is null");
        ObjectHelper.requireNonNull(generator, "generator  is null");
        ObjectHelper.requireNonNull(disposeState, "disposeState is null");
        return RxJavaPlugins.onAssembly(new ObservableGenerate(initialState, generator, disposeState));
    }

    @SchedulerSupport("io.reactivex:computation")
    public static Observable<Long> interval(long initialDelay, long period, TimeUnit unit) {
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
    @SchedulerSupport("custom")
    public static Observable<Long> interval(long initialDelay, long period, TimeUnit unit, Scheduler scheduler) {
        ObjectHelper.requireNonNull(unit, "unit is null");
        ObjectHelper.requireNonNull(scheduler, "scheduler is null");
        return RxJavaPlugins.onAssembly(new ObservableInterval(Math.max(0L, initialDelay), Math.max(0L, period), unit, scheduler));
    }

    @SchedulerSupport("io.reactivex:computation")
    public static Observable<Long> interval(long period, TimeUnit unit) {
        return interval(period, period, unit, Schedulers.computation());
    }

    @SchedulerSupport("custom")
    public static Observable<Long> interval(long period, TimeUnit unit, Scheduler scheduler) {
        return interval(period, period, unit, scheduler);
    }

    @SchedulerSupport("io.reactivex:computation")
    public static Observable<Long> intervalRange(long start, long count, long initialDelay, long period, TimeUnit unit) {
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
    @SchedulerSupport("custom")
    public static Observable<Long> intervalRange(long start, long count, long initialDelay, long period, TimeUnit unit, Scheduler scheduler) {
        if (count < 0) {
            throw new IllegalArgumentException("count >= 0 required but it was " + count);
        } else if (count == 0) {
            return empty().delay(initialDelay, unit, scheduler);
        } else {
            long end = start + (count - 1);
            if (start <= 0 || end >= 0) {
                ObjectHelper.requireNonNull(unit, "unit is null");
                ObjectHelper.requireNonNull(scheduler, "scheduler is null");
                return RxJavaPlugins.onAssembly(new ObservableIntervalRange(start, end, Math.max(0L, initialDelay), Math.max(0L, period), unit, scheduler));
            }
            throw new IllegalArgumentException("Overflow! start + count is bigger than Long.MAX_VALUE");
        }
    }

    @SchedulerSupport("none")
    public static <T> Observable<T> just(T item) {
        ObjectHelper.requireNonNull(item, "The item is null");
        return RxJavaPlugins.onAssembly(new ObservableJust(item));
    }

    @SchedulerSupport("none")
    public static <T> Observable<T> just(T item1, T item2) {
        ObjectHelper.requireNonNull(item1, "The first item is null");
        ObjectHelper.requireNonNull(item2, "The second item is null");
        return fromArray(item1, item2);
    }

    @SchedulerSupport("none")
    public static <T> Observable<T> just(T item1, T item2, T item3) {
        ObjectHelper.requireNonNull(item1, "The first item is null");
        ObjectHelper.requireNonNull(item2, "The second item is null");
        ObjectHelper.requireNonNull(item3, "The third item is null");
        return fromArray(item1, item2, item3);
    }

    @SchedulerSupport("none")
    public static <T> Observable<T> just(T item1, T item2, T item3, T item4) {
        ObjectHelper.requireNonNull(item1, "The first item is null");
        ObjectHelper.requireNonNull(item2, "The second item is null");
        ObjectHelper.requireNonNull(item3, "The third item is null");
        ObjectHelper.requireNonNull(item4, "The fourth item is null");
        return fromArray(item1, item2, item3, item4);
    }

    @SchedulerSupport("none")
    public static <T> Observable<T> just(T item1, T item2, T item3, T item4, T item5) {
        ObjectHelper.requireNonNull(item1, "The first item is null");
        ObjectHelper.requireNonNull(item2, "The second item is null");
        ObjectHelper.requireNonNull(item3, "The third item is null");
        ObjectHelper.requireNonNull(item4, "The fourth item is null");
        ObjectHelper.requireNonNull(item5, "The fifth item is null");
        return fromArray(item1, item2, item3, item4, item5);
    }

    @SchedulerSupport("none")
    public static <T> Observable<T> just(T item1, T item2, T item3, T item4, T item5, T item6) {
        ObjectHelper.requireNonNull(item1, "The first item is null");
        ObjectHelper.requireNonNull(item2, "The second item is null");
        ObjectHelper.requireNonNull(item3, "The third item is null");
        ObjectHelper.requireNonNull(item4, "The fourth item is null");
        ObjectHelper.requireNonNull(item5, "The fifth item is null");
        ObjectHelper.requireNonNull(item6, "The sixth item is null");
        return fromArray(item1, item2, item3, item4, item5, item6);
    }

    @SchedulerSupport("none")
    public static <T> Observable<T> just(T item1, T item2, T item3, T item4, T item5, T item6, T item7) {
        ObjectHelper.requireNonNull(item1, "The first item is null");
        ObjectHelper.requireNonNull(item2, "The second item is null");
        ObjectHelper.requireNonNull(item3, "The third item is null");
        ObjectHelper.requireNonNull(item4, "The fourth item is null");
        ObjectHelper.requireNonNull(item5, "The fifth item is null");
        ObjectHelper.requireNonNull(item6, "The sixth item is null");
        ObjectHelper.requireNonNull(item7, "The seventh item is null");
        return fromArray(item1, item2, item3, item4, item5, item6, item7);
    }

    @SchedulerSupport("none")
    public static <T> Observable<T> just(T item1, T item2, T item3, T item4, T item5, T item6, T item7, T item8) {
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

    @SchedulerSupport("none")
    public static <T> Observable<T> just(T item1, T item2, T item3, T item4, T item5, T item6, T item7, T item8, T item9) {
        ObjectHelper.requireNonNull(item1, "The first item is null");
        ObjectHelper.requireNonNull(item2, "The second item is null");
        ObjectHelper.requireNonNull(item3, "The third item is null");
        ObjectHelper.requireNonNull(item4, "The fourth item is null");
        ObjectHelper.requireNonNull(item5, "The fifth item is null");
        ObjectHelper.requireNonNull(item6, "The sixth item is null");
        ObjectHelper.requireNonNull(item7, "The seventh item is null");
        ObjectHelper.requireNonNull(item8, "The eighth item is null");
        ObjectHelper.requireNonNull(item9, "The ninth item is null");
        return fromArray(item1, item2, item3, item4, item5, item6, item7, item8, item9);
    }

    @SchedulerSupport("none")
    public static <T> Observable<T> just(T item1, T item2, T item3, T item4, T item5, T item6, T item7, T item8, T item9, T item10) {
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

    /* JADX WARN: Type inference failed for: r3v0, types: [java.lang.Iterable<? extends dji.thirdparty.io.reactivex.ObservableSource<? extends T>>, java.lang.Iterable], assign insn: null */
    /* JADX DEBUG: Failed to find minimal casts for resolve overloaded methods, cast all args instead
     method: dji.thirdparty.io.reactivex.Observable.flatMap(dji.thirdparty.io.reactivex.functions.Function, boolean, int, int):dji.thirdparty.io.reactivex.Observable<R>
     arg types: [dji.thirdparty.io.reactivex.functions.Function, int, int, int]
     candidates:
      dji.thirdparty.io.reactivex.Observable.flatMap(dji.thirdparty.io.reactivex.functions.Function, dji.thirdparty.io.reactivex.functions.BiFunction, boolean, int):dji.thirdparty.io.reactivex.Observable<R>
      dji.thirdparty.io.reactivex.Observable.flatMap(dji.thirdparty.io.reactivex.functions.Function, dji.thirdparty.io.reactivex.functions.Function, java.util.concurrent.Callable, int):dji.thirdparty.io.reactivex.Observable<R>
      dji.thirdparty.io.reactivex.Observable.flatMap(dji.thirdparty.io.reactivex.functions.Function, boolean, int, int):dji.thirdparty.io.reactivex.Observable<R> */
    /* JADX WARNING: Unknown variable types count: 1 */
    @dji.thirdparty.io.reactivex.annotations.SchedulerSupport("none")
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static <T> dji.thirdparty.io.reactivex.Observable<T> merge(java.lang.Iterable<? extends dji.thirdparty.io.reactivex.ObservableSource<? extends T>> r3, int r4, int r5) {
        /*
            dji.thirdparty.io.reactivex.Observable r0 = fromIterable(r3)
            dji.thirdparty.io.reactivex.functions.Function r1 = dji.thirdparty.io.reactivex.internal.functions.Functions.identity()
            r2 = 0
            dji.thirdparty.io.reactivex.Observable r0 = r0.flatMap(r1, r2, r4, r5)
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: dji.thirdparty.io.reactivex.Observable.merge(java.lang.Iterable, int, int):dji.thirdparty.io.reactivex.Observable");
    }

    /* JADX DEBUG: Failed to find minimal casts for resolve overloaded methods, cast all args instead
     method: dji.thirdparty.io.reactivex.Observable.flatMap(dji.thirdparty.io.reactivex.functions.Function, boolean, int, int):dji.thirdparty.io.reactivex.Observable<R>
     arg types: [dji.thirdparty.io.reactivex.functions.Function, int, int, int]
     candidates:
      dji.thirdparty.io.reactivex.Observable.flatMap(dji.thirdparty.io.reactivex.functions.Function, dji.thirdparty.io.reactivex.functions.BiFunction, boolean, int):dji.thirdparty.io.reactivex.Observable<R>
      dji.thirdparty.io.reactivex.Observable.flatMap(dji.thirdparty.io.reactivex.functions.Function, dji.thirdparty.io.reactivex.functions.Function, java.util.concurrent.Callable, int):dji.thirdparty.io.reactivex.Observable<R>
      dji.thirdparty.io.reactivex.Observable.flatMap(dji.thirdparty.io.reactivex.functions.Function, boolean, int, int):dji.thirdparty.io.reactivex.Observable<R> */
    @SchedulerSupport("none")
    public static <T> Observable<T> mergeArray(int maxConcurrency, int bufferSize, ObservableSource<? extends T>... sources) {
        return fromArray(sources).flatMap(Functions.identity(), false, maxConcurrency, bufferSize);
    }

    /* JADX WARN: Type inference failed for: r2v0, types: [java.lang.Iterable<? extends dji.thirdparty.io.reactivex.ObservableSource<? extends T>>, java.lang.Iterable], assign insn: null */
    /* JADX WARNING: Unknown variable types count: 1 */
    @dji.thirdparty.io.reactivex.annotations.SchedulerSupport("none")
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static <T> dji.thirdparty.io.reactivex.Observable<T> merge(java.lang.Iterable<? extends dji.thirdparty.io.reactivex.ObservableSource<? extends T>> r2) {
        /*
            dji.thirdparty.io.reactivex.Observable r0 = fromIterable(r2)
            dji.thirdparty.io.reactivex.functions.Function r1 = dji.thirdparty.io.reactivex.internal.functions.Functions.identity()
            dji.thirdparty.io.reactivex.Observable r0 = r0.flatMap(r1)
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: dji.thirdparty.io.reactivex.Observable.merge(java.lang.Iterable):dji.thirdparty.io.reactivex.Observable");
    }

    /* JADX WARN: Type inference failed for: r2v0, types: [java.lang.Iterable<? extends dji.thirdparty.io.reactivex.ObservableSource<? extends T>>, java.lang.Iterable], assign insn: null */
    /* JADX WARNING: Unknown variable types count: 1 */
    @dji.thirdparty.io.reactivex.annotations.SchedulerSupport("none")
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static <T> dji.thirdparty.io.reactivex.Observable<T> merge(java.lang.Iterable<? extends dji.thirdparty.io.reactivex.ObservableSource<? extends T>> r2, int r3) {
        /*
            dji.thirdparty.io.reactivex.Observable r0 = fromIterable(r2)
            dji.thirdparty.io.reactivex.functions.Function r1 = dji.thirdparty.io.reactivex.internal.functions.Functions.identity()
            dji.thirdparty.io.reactivex.Observable r0 = r0.flatMap(r1, r3)
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: dji.thirdparty.io.reactivex.Observable.merge(java.lang.Iterable, int):dji.thirdparty.io.reactivex.Observable");
    }

    @SchedulerSupport("none")
    public static <T> Observable<T> merge(ObservableSource<? extends ObservableSource<? extends T>> sources) {
        return RxJavaPlugins.onAssembly(new ObservableFlatMap(sources, Functions.identity(), false, Integer.MAX_VALUE, bufferSize()));
    }

    @SchedulerSupport("none")
    public static <T> Observable<T> merge(ObservableSource<? extends ObservableSource<? extends T>> sources, int maxConcurrency) {
        return RxJavaPlugins.onAssembly(new ObservableFlatMap(sources, Functions.identity(), false, maxConcurrency, bufferSize()));
    }

    /* JADX DEBUG: Failed to find minimal casts for resolve overloaded methods, cast all args instead
     method: dji.thirdparty.io.reactivex.Observable.flatMap(dji.thirdparty.io.reactivex.functions.Function, boolean, int):dji.thirdparty.io.reactivex.Observable<R>
     arg types: [dji.thirdparty.io.reactivex.functions.Function, int, int]
     candidates:
      dji.thirdparty.io.reactivex.Observable.flatMap(dji.thirdparty.io.reactivex.functions.Function, dji.thirdparty.io.reactivex.functions.BiFunction, int):dji.thirdparty.io.reactivex.Observable<R>
      dji.thirdparty.io.reactivex.Observable.flatMap(dji.thirdparty.io.reactivex.functions.Function, dji.thirdparty.io.reactivex.functions.BiFunction, boolean):dji.thirdparty.io.reactivex.Observable<R>
      dji.thirdparty.io.reactivex.Observable.flatMap(dji.thirdparty.io.reactivex.functions.Function, dji.thirdparty.io.reactivex.functions.Function, java.util.concurrent.Callable):dji.thirdparty.io.reactivex.Observable<R>
      dji.thirdparty.io.reactivex.Observable.flatMap(dji.thirdparty.io.reactivex.functions.Function, boolean, int):dji.thirdparty.io.reactivex.Observable<R> */
    @SchedulerSupport("none")
    public static <T> Observable<T> merge(ObservableSource<? extends T> source1, ObservableSource<? extends T> source2) {
        ObjectHelper.requireNonNull(source1, "source1 is null");
        ObjectHelper.requireNonNull(source2, "source2 is null");
        return fromArray(source1, source2).flatMap(Functions.identity(), false, 2);
    }

    /* JADX DEBUG: Failed to find minimal casts for resolve overloaded methods, cast all args instead
     method: dji.thirdparty.io.reactivex.Observable.flatMap(dji.thirdparty.io.reactivex.functions.Function, boolean, int):dji.thirdparty.io.reactivex.Observable<R>
     arg types: [dji.thirdparty.io.reactivex.functions.Function, int, int]
     candidates:
      dji.thirdparty.io.reactivex.Observable.flatMap(dji.thirdparty.io.reactivex.functions.Function, dji.thirdparty.io.reactivex.functions.BiFunction, int):dji.thirdparty.io.reactivex.Observable<R>
      dji.thirdparty.io.reactivex.Observable.flatMap(dji.thirdparty.io.reactivex.functions.Function, dji.thirdparty.io.reactivex.functions.BiFunction, boolean):dji.thirdparty.io.reactivex.Observable<R>
      dji.thirdparty.io.reactivex.Observable.flatMap(dji.thirdparty.io.reactivex.functions.Function, dji.thirdparty.io.reactivex.functions.Function, java.util.concurrent.Callable):dji.thirdparty.io.reactivex.Observable<R>
      dji.thirdparty.io.reactivex.Observable.flatMap(dji.thirdparty.io.reactivex.functions.Function, boolean, int):dji.thirdparty.io.reactivex.Observable<R> */
    @SchedulerSupport("none")
    public static <T> Observable<T> merge(ObservableSource<? extends T> source1, ObservableSource<? extends T> source2, ObservableSource<? extends T> source3) {
        ObjectHelper.requireNonNull(source1, "source1 is null");
        ObjectHelper.requireNonNull(source2, "source2 is null");
        ObjectHelper.requireNonNull(source3, "source3 is null");
        return fromArray(source1, source2, source3).flatMap(Functions.identity(), false, 3);
    }

    /* JADX DEBUG: Failed to find minimal casts for resolve overloaded methods, cast all args instead
     method: dji.thirdparty.io.reactivex.Observable.flatMap(dji.thirdparty.io.reactivex.functions.Function, boolean, int):dji.thirdparty.io.reactivex.Observable<R>
     arg types: [dji.thirdparty.io.reactivex.functions.Function, int, int]
     candidates:
      dji.thirdparty.io.reactivex.Observable.flatMap(dji.thirdparty.io.reactivex.functions.Function, dji.thirdparty.io.reactivex.functions.BiFunction, int):dji.thirdparty.io.reactivex.Observable<R>
      dji.thirdparty.io.reactivex.Observable.flatMap(dji.thirdparty.io.reactivex.functions.Function, dji.thirdparty.io.reactivex.functions.BiFunction, boolean):dji.thirdparty.io.reactivex.Observable<R>
      dji.thirdparty.io.reactivex.Observable.flatMap(dji.thirdparty.io.reactivex.functions.Function, dji.thirdparty.io.reactivex.functions.Function, java.util.concurrent.Callable):dji.thirdparty.io.reactivex.Observable<R>
      dji.thirdparty.io.reactivex.Observable.flatMap(dji.thirdparty.io.reactivex.functions.Function, boolean, int):dji.thirdparty.io.reactivex.Observable<R> */
    @SchedulerSupport("none")
    public static <T> Observable<T> merge(ObservableSource<? extends T> source1, ObservableSource<? extends T> source2, ObservableSource<? extends T> source3, ObservableSource<? extends T> source4) {
        ObjectHelper.requireNonNull(source1, "source1 is null");
        ObjectHelper.requireNonNull(source2, "source2 is null");
        ObjectHelper.requireNonNull(source3, "source3 is null");
        ObjectHelper.requireNonNull(source4, "source4 is null");
        return fromArray(source1, source2, source3, source4).flatMap(Functions.identity(), false, 4);
    }

    @SchedulerSupport("none")
    public static <T> Observable<T> mergeArray(ObservableSource<? extends T>... sources) {
        return fromArray(sources).flatMap(Functions.identity(), sources.length);
    }

    /* JADX WARN: Type inference failed for: r3v0, types: [java.lang.Iterable<? extends dji.thirdparty.io.reactivex.ObservableSource<? extends T>>, java.lang.Iterable], assign insn: null */
    /* JADX DEBUG: Failed to find minimal casts for resolve overloaded methods, cast all args instead
     method: dji.thirdparty.io.reactivex.Observable.flatMap(dji.thirdparty.io.reactivex.functions.Function, boolean):dji.thirdparty.io.reactivex.Observable<R>
     arg types: [dji.thirdparty.io.reactivex.functions.Function, int]
     candidates:
      dji.thirdparty.io.reactivex.Observable.flatMap(dji.thirdparty.io.reactivex.functions.Function, int):dji.thirdparty.io.reactivex.Observable<R>
      dji.thirdparty.io.reactivex.Observable.flatMap(dji.thirdparty.io.reactivex.functions.Function, dji.thirdparty.io.reactivex.functions.BiFunction):dji.thirdparty.io.reactivex.Observable<R>
      dji.thirdparty.io.reactivex.Observable.flatMap(dji.thirdparty.io.reactivex.functions.Function, boolean):dji.thirdparty.io.reactivex.Observable<R> */
    /* JADX WARNING: Unknown variable types count: 1 */
    @dji.thirdparty.io.reactivex.annotations.SchedulerSupport("none")
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static <T> dji.thirdparty.io.reactivex.Observable<T> mergeDelayError(java.lang.Iterable<? extends dji.thirdparty.io.reactivex.ObservableSource<? extends T>> r3) {
        /*
            dji.thirdparty.io.reactivex.Observable r0 = fromIterable(r3)
            dji.thirdparty.io.reactivex.functions.Function r1 = dji.thirdparty.io.reactivex.internal.functions.Functions.identity()
            r2 = 1
            dji.thirdparty.io.reactivex.Observable r0 = r0.flatMap(r1, r2)
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: dji.thirdparty.io.reactivex.Observable.mergeDelayError(java.lang.Iterable):dji.thirdparty.io.reactivex.Observable");
    }

    /* JADX WARN: Type inference failed for: r3v0, types: [java.lang.Iterable<? extends dji.thirdparty.io.reactivex.ObservableSource<? extends T>>, java.lang.Iterable], assign insn: null */
    /* JADX DEBUG: Failed to find minimal casts for resolve overloaded methods, cast all args instead
     method: dji.thirdparty.io.reactivex.Observable.flatMap(dji.thirdparty.io.reactivex.functions.Function, boolean, int, int):dji.thirdparty.io.reactivex.Observable<R>
     arg types: [dji.thirdparty.io.reactivex.functions.Function, int, int, int]
     candidates:
      dji.thirdparty.io.reactivex.Observable.flatMap(dji.thirdparty.io.reactivex.functions.Function, dji.thirdparty.io.reactivex.functions.BiFunction, boolean, int):dji.thirdparty.io.reactivex.Observable<R>
      dji.thirdparty.io.reactivex.Observable.flatMap(dji.thirdparty.io.reactivex.functions.Function, dji.thirdparty.io.reactivex.functions.Function, java.util.concurrent.Callable, int):dji.thirdparty.io.reactivex.Observable<R>
      dji.thirdparty.io.reactivex.Observable.flatMap(dji.thirdparty.io.reactivex.functions.Function, boolean, int, int):dji.thirdparty.io.reactivex.Observable<R> */
    /* JADX WARNING: Unknown variable types count: 1 */
    @dji.thirdparty.io.reactivex.annotations.SchedulerSupport("none")
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static <T> dji.thirdparty.io.reactivex.Observable<T> mergeDelayError(java.lang.Iterable<? extends dji.thirdparty.io.reactivex.ObservableSource<? extends T>> r3, int r4, int r5) {
        /*
            dji.thirdparty.io.reactivex.Observable r0 = fromIterable(r3)
            dji.thirdparty.io.reactivex.functions.Function r1 = dji.thirdparty.io.reactivex.internal.functions.Functions.identity()
            r2 = 1
            dji.thirdparty.io.reactivex.Observable r0 = r0.flatMap(r1, r2, r4, r5)
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: dji.thirdparty.io.reactivex.Observable.mergeDelayError(java.lang.Iterable, int, int):dji.thirdparty.io.reactivex.Observable");
    }

    /* JADX DEBUG: Failed to find minimal casts for resolve overloaded methods, cast all args instead
     method: dji.thirdparty.io.reactivex.Observable.flatMap(dji.thirdparty.io.reactivex.functions.Function, boolean, int, int):dji.thirdparty.io.reactivex.Observable<R>
     arg types: [dji.thirdparty.io.reactivex.functions.Function, int, int, int]
     candidates:
      dji.thirdparty.io.reactivex.Observable.flatMap(dji.thirdparty.io.reactivex.functions.Function, dji.thirdparty.io.reactivex.functions.BiFunction, boolean, int):dji.thirdparty.io.reactivex.Observable<R>
      dji.thirdparty.io.reactivex.Observable.flatMap(dji.thirdparty.io.reactivex.functions.Function, dji.thirdparty.io.reactivex.functions.Function, java.util.concurrent.Callable, int):dji.thirdparty.io.reactivex.Observable<R>
      dji.thirdparty.io.reactivex.Observable.flatMap(dji.thirdparty.io.reactivex.functions.Function, boolean, int, int):dji.thirdparty.io.reactivex.Observable<R> */
    @SchedulerSupport("none")
    public static <T> Observable<T> mergeArrayDelayError(int maxConcurrency, int bufferSize, ObservableSource<? extends T>... sources) {
        return fromArray(sources).flatMap(Functions.identity(), true, maxConcurrency, bufferSize);
    }

    /* JADX WARN: Type inference failed for: r3v0, types: [java.lang.Iterable<? extends dji.thirdparty.io.reactivex.ObservableSource<? extends T>>, java.lang.Iterable], assign insn: null */
    /* JADX DEBUG: Failed to find minimal casts for resolve overloaded methods, cast all args instead
     method: dji.thirdparty.io.reactivex.Observable.flatMap(dji.thirdparty.io.reactivex.functions.Function, boolean, int):dji.thirdparty.io.reactivex.Observable<R>
     arg types: [dji.thirdparty.io.reactivex.functions.Function, int, int]
     candidates:
      dji.thirdparty.io.reactivex.Observable.flatMap(dji.thirdparty.io.reactivex.functions.Function, dji.thirdparty.io.reactivex.functions.BiFunction, int):dji.thirdparty.io.reactivex.Observable<R>
      dji.thirdparty.io.reactivex.Observable.flatMap(dji.thirdparty.io.reactivex.functions.Function, dji.thirdparty.io.reactivex.functions.BiFunction, boolean):dji.thirdparty.io.reactivex.Observable<R>
      dji.thirdparty.io.reactivex.Observable.flatMap(dji.thirdparty.io.reactivex.functions.Function, dji.thirdparty.io.reactivex.functions.Function, java.util.concurrent.Callable):dji.thirdparty.io.reactivex.Observable<R>
      dji.thirdparty.io.reactivex.Observable.flatMap(dji.thirdparty.io.reactivex.functions.Function, boolean, int):dji.thirdparty.io.reactivex.Observable<R> */
    /* JADX WARNING: Unknown variable types count: 1 */
    @dji.thirdparty.io.reactivex.annotations.SchedulerSupport("none")
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static <T> dji.thirdparty.io.reactivex.Observable<T> mergeDelayError(java.lang.Iterable<? extends dji.thirdparty.io.reactivex.ObservableSource<? extends T>> r3, int r4) {
        /*
            dji.thirdparty.io.reactivex.Observable r0 = fromIterable(r3)
            dji.thirdparty.io.reactivex.functions.Function r1 = dji.thirdparty.io.reactivex.internal.functions.Functions.identity()
            r2 = 1
            dji.thirdparty.io.reactivex.Observable r0 = r0.flatMap(r1, r2, r4)
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: dji.thirdparty.io.reactivex.Observable.mergeDelayError(java.lang.Iterable, int):dji.thirdparty.io.reactivex.Observable");
    }

    @SchedulerSupport("none")
    public static <T> Observable<T> mergeDelayError(ObservableSource<? extends ObservableSource<? extends T>> sources) {
        return RxJavaPlugins.onAssembly(new ObservableFlatMap(sources, Functions.identity(), true, Integer.MAX_VALUE, bufferSize()));
    }

    @SchedulerSupport("none")
    public static <T> Observable<T> mergeDelayError(ObservableSource<? extends ObservableSource<? extends T>> sources, int maxConcurrency) {
        return RxJavaPlugins.onAssembly(new ObservableFlatMap(sources, Functions.identity(), true, maxConcurrency, bufferSize()));
    }

    /* JADX DEBUG: Failed to find minimal casts for resolve overloaded methods, cast all args instead
     method: dji.thirdparty.io.reactivex.Observable.flatMap(dji.thirdparty.io.reactivex.functions.Function, boolean, int):dji.thirdparty.io.reactivex.Observable<R>
     arg types: [dji.thirdparty.io.reactivex.functions.Function, int, int]
     candidates:
      dji.thirdparty.io.reactivex.Observable.flatMap(dji.thirdparty.io.reactivex.functions.Function, dji.thirdparty.io.reactivex.functions.BiFunction, int):dji.thirdparty.io.reactivex.Observable<R>
      dji.thirdparty.io.reactivex.Observable.flatMap(dji.thirdparty.io.reactivex.functions.Function, dji.thirdparty.io.reactivex.functions.BiFunction, boolean):dji.thirdparty.io.reactivex.Observable<R>
      dji.thirdparty.io.reactivex.Observable.flatMap(dji.thirdparty.io.reactivex.functions.Function, dji.thirdparty.io.reactivex.functions.Function, java.util.concurrent.Callable):dji.thirdparty.io.reactivex.Observable<R>
      dji.thirdparty.io.reactivex.Observable.flatMap(dji.thirdparty.io.reactivex.functions.Function, boolean, int):dji.thirdparty.io.reactivex.Observable<R> */
    @SchedulerSupport("none")
    public static <T> Observable<T> mergeDelayError(ObservableSource<? extends T> source1, ObservableSource<? extends T> source2) {
        ObjectHelper.requireNonNull(source1, "source1 is null");
        ObjectHelper.requireNonNull(source2, "source2 is null");
        return fromArray(source1, source2).flatMap(Functions.identity(), true, 2);
    }

    /* JADX DEBUG: Failed to find minimal casts for resolve overloaded methods, cast all args instead
     method: dji.thirdparty.io.reactivex.Observable.flatMap(dji.thirdparty.io.reactivex.functions.Function, boolean, int):dji.thirdparty.io.reactivex.Observable<R>
     arg types: [dji.thirdparty.io.reactivex.functions.Function, int, int]
     candidates:
      dji.thirdparty.io.reactivex.Observable.flatMap(dji.thirdparty.io.reactivex.functions.Function, dji.thirdparty.io.reactivex.functions.BiFunction, int):dji.thirdparty.io.reactivex.Observable<R>
      dji.thirdparty.io.reactivex.Observable.flatMap(dji.thirdparty.io.reactivex.functions.Function, dji.thirdparty.io.reactivex.functions.BiFunction, boolean):dji.thirdparty.io.reactivex.Observable<R>
      dji.thirdparty.io.reactivex.Observable.flatMap(dji.thirdparty.io.reactivex.functions.Function, dji.thirdparty.io.reactivex.functions.Function, java.util.concurrent.Callable):dji.thirdparty.io.reactivex.Observable<R>
      dji.thirdparty.io.reactivex.Observable.flatMap(dji.thirdparty.io.reactivex.functions.Function, boolean, int):dji.thirdparty.io.reactivex.Observable<R> */
    @SchedulerSupport("none")
    public static <T> Observable<T> mergeDelayError(ObservableSource<? extends T> source1, ObservableSource<? extends T> source2, ObservableSource<? extends T> source3) {
        ObjectHelper.requireNonNull(source1, "source1 is null");
        ObjectHelper.requireNonNull(source2, "source2 is null");
        ObjectHelper.requireNonNull(source3, "source3 is null");
        return fromArray(source1, source2, source3).flatMap(Functions.identity(), true, 3);
    }

    /* JADX DEBUG: Failed to find minimal casts for resolve overloaded methods, cast all args instead
     method: dji.thirdparty.io.reactivex.Observable.flatMap(dji.thirdparty.io.reactivex.functions.Function, boolean, int):dji.thirdparty.io.reactivex.Observable<R>
     arg types: [dji.thirdparty.io.reactivex.functions.Function, int, int]
     candidates:
      dji.thirdparty.io.reactivex.Observable.flatMap(dji.thirdparty.io.reactivex.functions.Function, dji.thirdparty.io.reactivex.functions.BiFunction, int):dji.thirdparty.io.reactivex.Observable<R>
      dji.thirdparty.io.reactivex.Observable.flatMap(dji.thirdparty.io.reactivex.functions.Function, dji.thirdparty.io.reactivex.functions.BiFunction, boolean):dji.thirdparty.io.reactivex.Observable<R>
      dji.thirdparty.io.reactivex.Observable.flatMap(dji.thirdparty.io.reactivex.functions.Function, dji.thirdparty.io.reactivex.functions.Function, java.util.concurrent.Callable):dji.thirdparty.io.reactivex.Observable<R>
      dji.thirdparty.io.reactivex.Observable.flatMap(dji.thirdparty.io.reactivex.functions.Function, boolean, int):dji.thirdparty.io.reactivex.Observable<R> */
    @SchedulerSupport("none")
    public static <T> Observable<T> mergeDelayError(ObservableSource<? extends T> source1, ObservableSource<? extends T> source2, ObservableSource<? extends T> source3, ObservableSource<? extends T> source4) {
        ObjectHelper.requireNonNull(source1, "source1 is null");
        ObjectHelper.requireNonNull(source2, "source2 is null");
        ObjectHelper.requireNonNull(source3, "source3 is null");
        ObjectHelper.requireNonNull(source4, "source4 is null");
        return fromArray(source1, source2, source3, source4).flatMap(Functions.identity(), true, 4);
    }

    /* JADX DEBUG: Failed to find minimal casts for resolve overloaded methods, cast all args instead
     method: dji.thirdparty.io.reactivex.Observable.flatMap(dji.thirdparty.io.reactivex.functions.Function, boolean, int):dji.thirdparty.io.reactivex.Observable<R>
     arg types: [dji.thirdparty.io.reactivex.functions.Function, int, int]
     candidates:
      dji.thirdparty.io.reactivex.Observable.flatMap(dji.thirdparty.io.reactivex.functions.Function, dji.thirdparty.io.reactivex.functions.BiFunction, int):dji.thirdparty.io.reactivex.Observable<R>
      dji.thirdparty.io.reactivex.Observable.flatMap(dji.thirdparty.io.reactivex.functions.Function, dji.thirdparty.io.reactivex.functions.BiFunction, boolean):dji.thirdparty.io.reactivex.Observable<R>
      dji.thirdparty.io.reactivex.Observable.flatMap(dji.thirdparty.io.reactivex.functions.Function, dji.thirdparty.io.reactivex.functions.Function, java.util.concurrent.Callable):dji.thirdparty.io.reactivex.Observable<R>
      dji.thirdparty.io.reactivex.Observable.flatMap(dji.thirdparty.io.reactivex.functions.Function, boolean, int):dji.thirdparty.io.reactivex.Observable<R> */
    @SchedulerSupport("none")
    public static <T> Observable<T> mergeArrayDelayError(ObservableSource<? extends T>... sources) {
        return fromArray(sources).flatMap(Functions.identity(), true, sources.length);
    }

    @SchedulerSupport("none")
    public static <T> Observable<T> never() {
        return RxJavaPlugins.onAssembly(ObservableNever.INSTANCE);
    }

    @SchedulerSupport("none")
    public static Observable<Integer> range(int start, int count) {
        if (count < 0) {
            throw new IllegalArgumentException("count >= 0 required but it was " + count);
        } else if (count == 0) {
            return empty();
        } else {
            if (count == 1) {
                return just(Integer.valueOf(start));
            }
            if (((long) start) + ((long) (count - 1)) <= 2147483647L) {
                return RxJavaPlugins.onAssembly(new ObservableRange(start, count));
            }
            throw new IllegalArgumentException("Integer overflow");
        }
    }

    @SchedulerSupport("none")
    public static Observable<Long> rangeLong(long start, long count) {
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
                return RxJavaPlugins.onAssembly(new ObservableRangeLong(start, count));
            }
            throw new IllegalArgumentException("Overflow! start + count is bigger than Long.MAX_VALUE");
        }
    }

    @SchedulerSupport("none")
    public static <T> Single<Boolean> sequenceEqual(ObservableSource<? extends T> source1, ObservableSource<? extends T> source2) {
        return sequenceEqual(source1, source2, ObjectHelper.equalsPredicate(), bufferSize());
    }

    @SchedulerSupport("none")
    public static <T> Single<Boolean> sequenceEqual(ObservableSource<? extends T> source1, ObservableSource<? extends T> source2, BiPredicate<? super T, ? super T> isEqual) {
        return sequenceEqual(source1, source2, isEqual, bufferSize());
    }

    @SchedulerSupport("none")
    public static <T> Single<Boolean> sequenceEqual(ObservableSource<? extends T> source1, ObservableSource<? extends T> source2, BiPredicate<? super T, ? super T> isEqual, int bufferSize) {
        ObjectHelper.requireNonNull(source1, "source1 is null");
        ObjectHelper.requireNonNull(source2, "source2 is null");
        ObjectHelper.requireNonNull(isEqual, "isEqual is null");
        ObjectHelper.verifyPositive(bufferSize, "bufferSize");
        return RxJavaPlugins.onAssembly(new ObservableSequenceEqualSingle(source1, source2, isEqual, bufferSize));
    }

    @SchedulerSupport("none")
    public static <T> Single<Boolean> sequenceEqual(ObservableSource<? extends T> source1, ObservableSource<? extends T> source2, int bufferSize) {
        return sequenceEqual(source1, source2, ObjectHelper.equalsPredicate(), bufferSize);
    }

    @SchedulerSupport("none")
    public static <T> Observable<T> switchOnNext(ObservableSource<? extends ObservableSource<? extends T>> sources, int bufferSize) {
        ObjectHelper.requireNonNull(sources, "sources is null");
        return RxJavaPlugins.onAssembly(new ObservableSwitchMap(sources, Functions.identity(), bufferSize, false));
    }

    @SchedulerSupport("none")
    public static <T> Observable<T> switchOnNext(ObservableSource<? extends ObservableSource<? extends T>> sources) {
        return switchOnNext(sources, bufferSize());
    }

    @SchedulerSupport("none")
    public static <T> Observable<T> switchOnNextDelayError(ObservableSource<? extends ObservableSource<? extends T>> sources) {
        return switchOnNextDelayError(sources, bufferSize());
    }

    @SchedulerSupport("none")
    public static <T> Observable<T> switchOnNextDelayError(ObservableSource<? extends ObservableSource<? extends T>> sources, int prefetch) {
        ObjectHelper.requireNonNull(sources, "sources is null");
        ObjectHelper.verifyPositive(prefetch, "prefetch");
        return RxJavaPlugins.onAssembly(new ObservableSwitchMap(sources, Functions.identity(), prefetch, true));
    }

    @SchedulerSupport("io.reactivex:computation")
    public static Observable<Long> timer(long delay, TimeUnit unit) {
        return timer(delay, unit, Schedulers.computation());
    }

    /* JADX DEBUG: Failed to find minimal casts for resolve overloaded methods, cast all args instead
     method: ClspMth{java.lang.Math.max(long, long):long}
     arg types: [long, int]
     candidates:
      ClspMth{java.lang.Math.max(double, double):double}
      ClspMth{java.lang.Math.max(int, int):int}
      ClspMth{java.lang.Math.max(float, float):float}
      ClspMth{java.lang.Math.max(long, long):long} */
    @SchedulerSupport("custom")
    public static Observable<Long> timer(long delay, TimeUnit unit, Scheduler scheduler) {
        ObjectHelper.requireNonNull(unit, "unit is null");
        ObjectHelper.requireNonNull(scheduler, "scheduler is null");
        return RxJavaPlugins.onAssembly(new ObservableTimer(Math.max(delay, 0L), unit, scheduler));
    }

    @SchedulerSupport("none")
    public static <T> Observable<T> unsafeCreate(ObservableSource<T> onSubscribe) {
        ObjectHelper.requireNonNull(onSubscribe, "source is null");
        ObjectHelper.requireNonNull(onSubscribe, "onSubscribe is null");
        if (!(onSubscribe instanceof Observable)) {
            return RxJavaPlugins.onAssembly(new ObservableFromUnsafeSource(onSubscribe));
        }
        throw new IllegalArgumentException("unsafeCreate(Observable) should be upgraded");
    }

    @SchedulerSupport("none")
    public static <T, D> Observable<T> using(Callable<? extends D> resourceSupplier, Function<? super D, ? extends ObservableSource<? extends T>> sourceSupplier, Consumer<? super D> disposer) {
        return using(resourceSupplier, sourceSupplier, disposer, true);
    }

    @SchedulerSupport("none")
    public static <T, D> Observable<T> using(Callable<? extends D> resourceSupplier, Function<? super D, ? extends ObservableSource<? extends T>> sourceSupplier, Consumer<? super D> disposer, boolean eager) {
        ObjectHelper.requireNonNull(resourceSupplier, "resourceSupplier is null");
        ObjectHelper.requireNonNull(sourceSupplier, "sourceSupplier is null");
        ObjectHelper.requireNonNull(disposer, "disposer is null");
        return RxJavaPlugins.onAssembly(new ObservableUsing(resourceSupplier, sourceSupplier, disposer, eager));
    }

    @SchedulerSupport("none")
    public static <T> Observable<T> wrap(ObservableSource<T> source) {
        ObjectHelper.requireNonNull(source, "source is null");
        if (source instanceof Observable) {
            return RxJavaPlugins.onAssembly((Observable) ((Observable) source));
        }
        return RxJavaPlugins.onAssembly(new ObservableFromUnsafeSource(source));
    }

    @SchedulerSupport("none")
    public static <T, R> Observable<R> zip(Iterable<? extends ObservableSource<? extends T>> sources, Function<? super Object[], ? extends R> zipper) {
        ObjectHelper.requireNonNull(zipper, "zipper is null");
        ObjectHelper.requireNonNull(sources, "sources is null");
        return RxJavaPlugins.onAssembly(new ObservableZip(null, sources, zipper, bufferSize(), false));
    }

    @SchedulerSupport("none")
    public static <T, R> Observable<R> zip(ObservableSource<? extends ObservableSource<? extends T>> sources, Function<? super Object[], ? extends R> zipper) {
        ObjectHelper.requireNonNull(zipper, "zipper is null");
        ObjectHelper.requireNonNull(sources, "sources is null");
        return RxJavaPlugins.onAssembly(new ObservableToList(sources, 16).flatMap(ObservableInternalHelper.zipIterable(zipper)));
    }

    @SchedulerSupport("none")
    public static <T1, T2, R> Observable<R> zip(ObservableSource<? extends T1> source1, ObservableSource<? extends T2> source2, BiFunction<? super T1, ? super T2, ? extends R> zipper) {
        return zipArray(Functions.toFunction(zipper), false, bufferSize(), source1, source2);
    }

    @SchedulerSupport("none")
    public static <T1, T2, R> Observable<R> zip(ObservableSource<? extends T1> source1, ObservableSource<? extends T2> source2, BiFunction<? super T1, ? super T2, ? extends R> zipper, boolean delayError) {
        return zipArray(Functions.toFunction(zipper), delayError, bufferSize(), source1, source2);
    }

    @SchedulerSupport("none")
    public static <T1, T2, R> Observable<R> zip(ObservableSource<? extends T1> source1, ObservableSource<? extends T2> source2, BiFunction<? super T1, ? super T2, ? extends R> zipper, boolean delayError, int bufferSize) {
        return zipArray(Functions.toFunction(zipper), delayError, bufferSize, source1, source2);
    }

    @SchedulerSupport("none")
    public static <T1, T2, T3, R> Observable<R> zip(ObservableSource<? extends T1> source1, ObservableSource<? extends T2> source2, ObservableSource<? extends T3> source3, Function3<? super T1, ? super T2, ? super T3, ? extends R> zipper) {
        return zipArray(Functions.toFunction(zipper), false, bufferSize(), source1, source2, source3);
    }

    @SchedulerSupport("none")
    public static <T1, T2, T3, T4, R> Observable<R> zip(ObservableSource<? extends T1> source1, ObservableSource<? extends T2> source2, ObservableSource<? extends T3> source3, ObservableSource<? extends T4> source4, Function4<? super T1, ? super T2, ? super T3, ? super T4, ? extends R> zipper) {
        return zipArray(Functions.toFunction(zipper), false, bufferSize(), source1, source2, source3, source4);
    }

    @SchedulerSupport("none")
    public static <T1, T2, T3, T4, T5, R> Observable<R> zip(ObservableSource<? extends T1> source1, ObservableSource<? extends T2> source2, ObservableSource<? extends T3> source3, ObservableSource<? extends T4> source4, ObservableSource<? extends T5> source5, Function5<? super T1, ? super T2, ? super T3, ? super T4, ? super T5, ? extends R> zipper) {
        return zipArray(Functions.toFunction(zipper), false, bufferSize(), source1, source2, source3, source4, source5);
    }

    @SchedulerSupport("none")
    public static <T1, T2, T3, T4, T5, T6, R> Observable<R> zip(ObservableSource<? extends T1> source1, ObservableSource<? extends T2> source2, ObservableSource<? extends T3> source3, ObservableSource<? extends T4> source4, ObservableSource<? extends T5> source5, ObservableSource<? extends T6> source6, Function6<? super T1, ? super T2, ? super T3, ? super T4, ? super T5, ? super T6, ? extends R> zipper) {
        return zipArray(Functions.toFunction(zipper), false, bufferSize(), source1, source2, source3, source4, source5, source6);
    }

    @SchedulerSupport("none")
    public static <T1, T2, T3, T4, T5, T6, T7, R> Observable<R> zip(ObservableSource<? extends T1> source1, ObservableSource<? extends T2> source2, ObservableSource<? extends T3> source3, ObservableSource<? extends T4> source4, ObservableSource<? extends T5> source5, ObservableSource<? extends T6> source6, ObservableSource<? extends T7> source7, Function7<? super T1, ? super T2, ? super T3, ? super T4, ? super T5, ? super T6, ? super T7, ? extends R> zipper) {
        return zipArray(Functions.toFunction(zipper), false, bufferSize(), source1, source2, source3, source4, source5, source6, source7);
    }

    @SchedulerSupport("none")
    public static <T1, T2, T3, T4, T5, T6, T7, T8, R> Observable<R> zip(ObservableSource<? extends T1> source1, ObservableSource<? extends T2> source2, ObservableSource<? extends T3> source3, ObservableSource<? extends T4> source4, ObservableSource<? extends T5> source5, ObservableSource<? extends T6> source6, ObservableSource<? extends T7> source7, ObservableSource<? extends T8> source8, Function8<? super T1, ? super T2, ? super T3, ? super T4, ? super T5, ? super T6, ? super T7, ? super T8, ? extends R> zipper) {
        return zipArray(Functions.toFunction(zipper), false, bufferSize(), source1, source2, source3, source4, source5, source6, source7, source8);
    }

    @SchedulerSupport("none")
    public static <T1, T2, T3, T4, T5, T6, T7, T8, T9, R> Observable<R> zip(ObservableSource<? extends T1> source1, ObservableSource<? extends T2> source2, ObservableSource<? extends T3> source3, ObservableSource<? extends T4> source4, ObservableSource<? extends T5> source5, ObservableSource<? extends T6> source6, ObservableSource<? extends T7> source7, ObservableSource<? extends T8> source8, ObservableSource<? extends T9> source9, Function9<? super T1, ? super T2, ? super T3, ? super T4, ? super T5, ? super T6, ? super T7, ? super T8, ? super T9, ? extends R> zipper) {
        return zipArray(Functions.toFunction(zipper), false, bufferSize(), source1, source2, source3, source4, source5, source6, source7, source8, source9);
    }

    @SchedulerSupport("none")
    public static <T, R> Observable<R> zipArray(Function<? super Object[], ? extends R> zipper, boolean delayError, int bufferSize, ObservableSource<? extends T>... sources) {
        if (sources.length == 0) {
            return empty();
        }
        ObjectHelper.requireNonNull(zipper, "zipper is null");
        ObjectHelper.verifyPositive(bufferSize, "bufferSize");
        return RxJavaPlugins.onAssembly(new ObservableZip(sources, null, zipper, bufferSize, delayError));
    }

    @SchedulerSupport("none")
    public static <T, R> Observable<R> zipIterable(Iterable<? extends ObservableSource<? extends T>> sources, Function<? super Object[], ? extends R> zipper, boolean delayError, int bufferSize) {
        ObjectHelper.requireNonNull(zipper, "zipper is null");
        ObjectHelper.requireNonNull(sources, "sources is null");
        ObjectHelper.verifyPositive(bufferSize, "bufferSize");
        return RxJavaPlugins.onAssembly(new ObservableZip(null, sources, zipper, bufferSize, delayError));
    }

    @SchedulerSupport("none")
    public final Single<Boolean> all(Predicate<? super T> predicate) {
        ObjectHelper.requireNonNull(predicate, "predicate is null");
        return RxJavaPlugins.onAssembly(new ObservableAllSingle(this, predicate));
    }

    @SchedulerSupport("none")
    public final Observable<T> ambWith(ObservableSource<? extends T> other) {
        ObjectHelper.requireNonNull(other, "other is null");
        return ambArray(this, other);
    }

    @SchedulerSupport("none")
    public final Single<Boolean> any(Predicate<? super T> predicate) {
        ObjectHelper.requireNonNull(predicate, "predicate is null");
        return RxJavaPlugins.onAssembly(new ObservableAnySingle(this, predicate));
    }

    @SchedulerSupport("none")
    public final T blockingFirst() {
        BlockingFirstObserver<T> s = new BlockingFirstObserver<>();
        subscribe(s);
        T v = s.blockingGet();
        if (v != null) {
            return v;
        }
        throw new NoSuchElementException();
    }

    @SchedulerSupport("none")
    public final T blockingFirst(T defaultItem) {
        BlockingFirstObserver<T> s = new BlockingFirstObserver<>();
        subscribe(s);
        T v = s.blockingGet();
        return v != null ? v : defaultItem;
    }

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

    @SchedulerSupport("none")
    public final Iterable<T> blockingIterable() {
        return blockingIterable(bufferSize());
    }

    @SchedulerSupport("none")
    public final Iterable<T> blockingIterable(int bufferSize) {
        ObjectHelper.verifyPositive(bufferSize, "bufferSize");
        return new BlockingObservableIterable(this, bufferSize);
    }

    @SchedulerSupport("none")
    public final T blockingLast() {
        BlockingLastObserver<T> s = new BlockingLastObserver<>();
        subscribe(s);
        T v = s.blockingGet();
        if (v != null) {
            return v;
        }
        throw new NoSuchElementException();
    }

    @SchedulerSupport("none")
    public final T blockingLast(T defaultItem) {
        BlockingLastObserver<T> s = new BlockingLastObserver<>();
        subscribe(s);
        T v = s.blockingGet();
        return v != null ? v : defaultItem;
    }

    @SchedulerSupport("none")
    public final Iterable<T> blockingLatest() {
        return new BlockingObservableLatest(this);
    }

    @SchedulerSupport("none")
    public final Iterable<T> blockingMostRecent(T initialValue) {
        return new BlockingObservableMostRecent(this, initialValue);
    }

    @SchedulerSupport("none")
    public final Iterable<T> blockingNext() {
        return new BlockingObservableNext(this);
    }

    @SchedulerSupport("none")
    public final T blockingSingle() {
        T v = singleElement().blockingGet();
        if (v != null) {
            return v;
        }
        throw new NoSuchElementException();
    }

    @SchedulerSupport("none")
    public final T blockingSingle(T defaultItem) {
        return single(defaultItem).blockingGet();
    }

    @SchedulerSupport("none")
    public final Future<T> toFuture() {
        return (Future) subscribeWith(new FutureObserver());
    }

    @SchedulerSupport("none")
    public final void blockingSubscribe() {
        ObservableBlockingSubscribe.subscribe(this);
    }

    @SchedulerSupport("none")
    public final void blockingSubscribe(Consumer<? super T> onNext) {
        ObservableBlockingSubscribe.subscribe(this, onNext, Functions.ERROR_CONSUMER, Functions.EMPTY_ACTION);
    }

    @SchedulerSupport("none")
    public final void blockingSubscribe(Consumer<? super T> onNext, Consumer<? super Throwable> onError) {
        ObservableBlockingSubscribe.subscribe(this, onNext, onError, Functions.EMPTY_ACTION);
    }

    @SchedulerSupport("none")
    public final void blockingSubscribe(Consumer<? super T> onNext, Consumer<? super Throwable> onError, Action onComplete) {
        ObservableBlockingSubscribe.subscribe(this, onNext, onError, onComplete);
    }

    @SchedulerSupport("none")
    public final void blockingSubscribe(Observer<? super T> subscriber) {
        ObservableBlockingSubscribe.subscribe(this, subscriber);
    }

    @SchedulerSupport("none")
    public final Observable<List<T>> buffer(int count) {
        return buffer(count, count);
    }

    @SchedulerSupport("none")
    public final Observable<List<T>> buffer(int count, int skip) {
        return buffer(count, skip, ArrayListSupplier.asCallable());
    }

    @SchedulerSupport("none")
    public final <U extends Collection<? super T>> Observable<U> buffer(int count, int skip, Callable<U> bufferSupplier) {
        ObjectHelper.verifyPositive(count, "count");
        ObjectHelper.verifyPositive(skip, "skip");
        ObjectHelper.requireNonNull(bufferSupplier, "bufferSupplier is null");
        return RxJavaPlugins.onAssembly(new ObservableBuffer(this, count, skip, bufferSupplier));
    }

    @SchedulerSupport("none")
    public final <U extends Collection<? super T>> Observable<U> buffer(int count, Callable<U> bufferSupplier) {
        return buffer(count, count, bufferSupplier);
    }

    @SchedulerSupport("io.reactivex:computation")
    public final Observable<List<T>> buffer(long timespan, long timeskip, TimeUnit unit) {
        return buffer(timespan, timeskip, unit, Schedulers.computation(), ArrayListSupplier.asCallable());
    }

    @SchedulerSupport("custom")
    public final Observable<List<T>> buffer(long timespan, long timeskip, TimeUnit unit, Scheduler scheduler) {
        return buffer(timespan, timeskip, unit, scheduler, ArrayListSupplier.asCallable());
    }

    @SchedulerSupport("custom")
    public final <U extends Collection<? super T>> Observable<U> buffer(long timespan, long timeskip, TimeUnit unit, Scheduler scheduler, Callable<U> bufferSupplier) {
        ObjectHelper.requireNonNull(unit, "unit is null");
        ObjectHelper.requireNonNull(scheduler, "scheduler is null");
        ObjectHelper.requireNonNull(bufferSupplier, "bufferSupplier is null");
        return RxJavaPlugins.onAssembly(new ObservableBufferTimed(this, timespan, timeskip, unit, scheduler, bufferSupplier, Integer.MAX_VALUE, false));
    }

    @SchedulerSupport("io.reactivex:computation")
    public final Observable<List<T>> buffer(long timespan, TimeUnit unit) {
        return buffer(timespan, unit, Schedulers.computation(), Integer.MAX_VALUE);
    }

    @SchedulerSupport("io.reactivex:computation")
    public final Observable<List<T>> buffer(long timespan, TimeUnit unit, int count) {
        return buffer(timespan, unit, Schedulers.computation(), count);
    }

    @SchedulerSupport("custom")
    public final Observable<List<T>> buffer(long timespan, TimeUnit unit, Scheduler scheduler, int count) {
        return buffer(timespan, unit, scheduler, count, ArrayListSupplier.asCallable(), false);
    }

    @SchedulerSupport("custom")
    public final <U extends Collection<? super T>> Observable<U> buffer(long timespan, TimeUnit unit, Scheduler scheduler, int count, Callable<U> bufferSupplier, boolean restartTimerOnMaxSize) {
        ObjectHelper.requireNonNull(unit, "unit is null");
        ObjectHelper.requireNonNull(scheduler, "scheduler is null");
        ObjectHelper.requireNonNull(bufferSupplier, "bufferSupplier is null");
        ObjectHelper.verifyPositive(count, "count");
        return RxJavaPlugins.onAssembly(new ObservableBufferTimed(this, timespan, timespan, unit, scheduler, bufferSupplier, count, restartTimerOnMaxSize));
    }

    @SchedulerSupport("custom")
    public final Observable<List<T>> buffer(long timespan, TimeUnit unit, Scheduler scheduler) {
        return buffer(timespan, unit, scheduler, Integer.MAX_VALUE, ArrayListSupplier.asCallable(), false);
    }

    @SchedulerSupport("none")
    public final <TOpening, TClosing> Observable<List<T>> buffer(ObservableSource<? extends TOpening> openingIndicator, Function<? super TOpening, ? extends ObservableSource<? extends TClosing>> closingIndicator) {
        return buffer(openingIndicator, closingIndicator, ArrayListSupplier.asCallable());
    }

    @SchedulerSupport("none")
    public final <TOpening, TClosing, U extends Collection<? super T>> Observable<U> buffer(ObservableSource<? extends TOpening> openingIndicator, Function<? super TOpening, ? extends ObservableSource<? extends TClosing>> closingIndicator, Callable<U> bufferSupplier) {
        ObjectHelper.requireNonNull(openingIndicator, "openingIndicator is null");
        ObjectHelper.requireNonNull(closingIndicator, "closingIndicator is null");
        ObjectHelper.requireNonNull(bufferSupplier, "bufferSupplier is null");
        return RxJavaPlugins.onAssembly(new ObservableBufferBoundary(this, openingIndicator, closingIndicator, bufferSupplier));
    }

    @SchedulerSupport("none")
    public final <B> Observable<List<T>> buffer(ObservableSource<B> boundary) {
        return buffer(boundary, ArrayListSupplier.asCallable());
    }

    @SchedulerSupport("none")
    public final <B> Observable<List<T>> buffer(ObservableSource<B> boundary, int initialCapacity) {
        return buffer(boundary, Functions.createArrayList(initialCapacity));
    }

    @SchedulerSupport("none")
    public final <B, U extends Collection<? super T>> Observable<U> buffer(ObservableSource<B> boundary, Callable<U> bufferSupplier) {
        ObjectHelper.requireNonNull(boundary, "boundary is null");
        ObjectHelper.requireNonNull(bufferSupplier, "bufferSupplier is null");
        return RxJavaPlugins.onAssembly(new ObservableBufferExactBoundary(this, boundary, bufferSupplier));
    }

    @SchedulerSupport("none")
    public final <B> Observable<List<T>> buffer(Callable<? extends ObservableSource<B>> boundarySupplier) {
        return buffer(boundarySupplier, ArrayListSupplier.asCallable());
    }

    @SchedulerSupport("none")
    public final <B, U extends Collection<? super T>> Observable<U> buffer(Callable<? extends ObservableSource<B>> boundarySupplier, Callable<U> bufferSupplier) {
        ObjectHelper.requireNonNull(boundarySupplier, "boundarySupplier is null");
        ObjectHelper.requireNonNull(bufferSupplier, "bufferSupplier is null");
        return RxJavaPlugins.onAssembly(new ObservableBufferBoundarySupplier(this, boundarySupplier, bufferSupplier));
    }

    @SchedulerSupport("none")
    public final Observable<T> cache() {
        return ObservableCache.from(this);
    }

    @SchedulerSupport("none")
    public final Observable<T> cacheWithInitialCapacity(int initialCapacity) {
        return ObservableCache.from(this, initialCapacity);
    }

    @SchedulerSupport("none")
    public final <U> Observable<U> cast(Class<U> clazz) {
        ObjectHelper.requireNonNull(clazz, "clazz is null");
        return map(Functions.castFunction(clazz));
    }

    @SchedulerSupport("none")
    public final <U> Single<U> collect(Callable<? extends U> initialValueSupplier, BiConsumer<? super U, ? super T> collector) {
        ObjectHelper.requireNonNull(initialValueSupplier, "initialValueSupplier is null");
        ObjectHelper.requireNonNull(collector, "collector is null");
        return RxJavaPlugins.onAssembly(new ObservableCollectSingle(this, initialValueSupplier, collector));
    }

    @SchedulerSupport("none")
    public final <U> Single<U> collectInto(U initialValue, BiConsumer<? super U, ? super T> collector) {
        ObjectHelper.requireNonNull(initialValue, "initialValue is null");
        return collect(Functions.justCallable(initialValue), collector);
    }

    @SchedulerSupport("none")
    public final <R> Observable<R> compose(ObservableTransformer<T, R> composer) {
        return wrap(composer.apply(this));
    }

    @SchedulerSupport("none")
    public final <R> Observable<R> concatMap(Function<? super T, ? extends ObservableSource<? extends R>> mapper) {
        return concatMap(mapper, 2);
    }

    /* JADX WARN: Type inference failed for: r4v0, types: [dji.thirdparty.io.reactivex.functions.Function, java.lang.Object, dji.thirdparty.io.reactivex.functions.Function<? super T, ? extends dji.thirdparty.io.reactivex.ObservableSource<? extends R>>], assign insn: null */
    /* JADX WARNING: Unknown variable types count: 1 */
    @dji.thirdparty.io.reactivex.annotations.SchedulerSupport("none")
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final <R> dji.thirdparty.io.reactivex.Observable<R> concatMap(dji.thirdparty.io.reactivex.functions.Function<? super T, ? extends dji.thirdparty.io.reactivex.ObservableSource<? extends R>> r4, int r5) {
        /*
            r3 = this;
            java.lang.String r1 = "mapper is null"
            dji.thirdparty.io.reactivex.internal.functions.ObjectHelper.requireNonNull(r4, r1)
            java.lang.String r1 = "prefetch"
            dji.thirdparty.io.reactivex.internal.functions.ObjectHelper.verifyPositive(r5, r1)
            boolean r1 = r3 instanceof dji.thirdparty.io.reactivex.internal.fuseable.ScalarCallable
            if (r1 == 0) goto L_0x0022
            dji.thirdparty.io.reactivex.internal.fuseable.ScalarCallable r3 = (dji.thirdparty.io.reactivex.internal.fuseable.ScalarCallable) r3
            java.lang.Object r0 = r3.call()
            if (r0 != 0) goto L_0x001d
            dji.thirdparty.io.reactivex.Observable r1 = empty()
        L_0x001c:
            return r1
        L_0x001d:
            dji.thirdparty.io.reactivex.Observable r1 = dji.thirdparty.io.reactivex.internal.operators.observable.ObservableScalarXMap.scalarXMap(r0, r4)
            goto L_0x001c
        L_0x0022:
            dji.thirdparty.io.reactivex.internal.operators.observable.ObservableConcatMap r1 = new dji.thirdparty.io.reactivex.internal.operators.observable.ObservableConcatMap
            dji.thirdparty.io.reactivex.internal.util.ErrorMode r2 = dji.thirdparty.io.reactivex.internal.util.ErrorMode.IMMEDIATE
            r1.<init>(r3, r4, r5, r2)
            dji.thirdparty.io.reactivex.Observable r1 = dji.thirdparty.io.reactivex.plugins.RxJavaPlugins.onAssembly(r1)
            goto L_0x001c
        */
        throw new UnsupportedOperationException("Method not decompiled: dji.thirdparty.io.reactivex.Observable.concatMap(dji.thirdparty.io.reactivex.functions.Function, int):dji.thirdparty.io.reactivex.Observable");
    }

    @SchedulerSupport("none")
    public final <R> Observable<R> concatMapDelayError(Function<? super T, ? extends ObservableSource<? extends R>> mapper) {
        return concatMapDelayError(mapper, bufferSize(), true);
    }

    /* JADX WARN: Type inference failed for: r4v0, types: [dji.thirdparty.io.reactivex.functions.Function, dji.thirdparty.io.reactivex.functions.Function<? super T, ? extends dji.thirdparty.io.reactivex.ObservableSource<? extends R>>], assign insn: null */
    /* JADX WARNING: Unknown variable types count: 1 */
    @dji.thirdparty.io.reactivex.annotations.SchedulerSupport("none")
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final <R> dji.thirdparty.io.reactivex.Observable<R> concatMapDelayError(dji.thirdparty.io.reactivex.functions.Function<? super T, ? extends dji.thirdparty.io.reactivex.ObservableSource<? extends R>> r4, int r5, boolean r6) {
        /*
            r3 = this;
            java.lang.String r1 = "prefetch"
            dji.thirdparty.io.reactivex.internal.functions.ObjectHelper.verifyPositive(r5, r1)
            boolean r1 = r3 instanceof dji.thirdparty.io.reactivex.internal.fuseable.ScalarCallable
            if (r1 == 0) goto L_0x001c
            dji.thirdparty.io.reactivex.internal.fuseable.ScalarCallable r3 = (dji.thirdparty.io.reactivex.internal.fuseable.ScalarCallable) r3
            java.lang.Object r0 = r3.call()
            if (r0 != 0) goto L_0x0017
            dji.thirdparty.io.reactivex.Observable r1 = empty()
        L_0x0016:
            return r1
        L_0x0017:
            dji.thirdparty.io.reactivex.Observable r1 = dji.thirdparty.io.reactivex.internal.operators.observable.ObservableScalarXMap.scalarXMap(r0, r4)
            goto L_0x0016
        L_0x001c:
            dji.thirdparty.io.reactivex.internal.operators.observable.ObservableConcatMap r2 = new dji.thirdparty.io.reactivex.internal.operators.observable.ObservableConcatMap
            if (r6 == 0) goto L_0x002a
            dji.thirdparty.io.reactivex.internal.util.ErrorMode r1 = dji.thirdparty.io.reactivex.internal.util.ErrorMode.END
        L_0x0022:
            r2.<init>(r3, r4, r5, r1)
            dji.thirdparty.io.reactivex.Observable r1 = dji.thirdparty.io.reactivex.plugins.RxJavaPlugins.onAssembly(r2)
            goto L_0x0016
        L_0x002a:
            dji.thirdparty.io.reactivex.internal.util.ErrorMode r1 = dji.thirdparty.io.reactivex.internal.util.ErrorMode.BOUNDARY
            goto L_0x0022
        */
        throw new UnsupportedOperationException("Method not decompiled: dji.thirdparty.io.reactivex.Observable.concatMapDelayError(dji.thirdparty.io.reactivex.functions.Function, int, boolean):dji.thirdparty.io.reactivex.Observable");
    }

    @SchedulerSupport("none")
    public final <R> Observable<R> concatMapEager(Function<? super T, ? extends ObservableSource<? extends R>> mapper) {
        return concatMapEager(mapper, Integer.MAX_VALUE, bufferSize());
    }

    @SchedulerSupport("none")
    public final <R> Observable<R> concatMapEager(Function<? super T, ? extends ObservableSource<? extends R>> mapper, int maxConcurrency, int prefetch) {
        ObjectHelper.requireNonNull(mapper, "mapper is null");
        ObjectHelper.verifyPositive(maxConcurrency, "maxConcurrency");
        ObjectHelper.verifyPositive(prefetch, "prefetch");
        return RxJavaPlugins.onAssembly(new ObservableConcatMapEager(this, mapper, ErrorMode.IMMEDIATE, maxConcurrency, prefetch));
    }

    @SchedulerSupport("none")
    public final <R> Observable<R> concatMapEagerDelayError(Function<? super T, ? extends ObservableSource<? extends R>> mapper, boolean tillTheEnd) {
        return concatMapEagerDelayError(mapper, Integer.MAX_VALUE, bufferSize(), tillTheEnd);
    }

    @SchedulerSupport("none")
    public final <R> Observable<R> concatMapEagerDelayError(Function<? super T, ? extends ObservableSource<? extends R>> mapper, int maxConcurrency, int prefetch, boolean tillTheEnd) {
        return RxJavaPlugins.onAssembly(new ObservableConcatMapEager(this, mapper, tillTheEnd ? ErrorMode.END : ErrorMode.BOUNDARY, maxConcurrency, prefetch));
    }

    @SchedulerSupport("none")
    public final <U> Observable<U> concatMapIterable(Function<? super T, ? extends Iterable<? extends U>> mapper) {
        ObjectHelper.requireNonNull(mapper, "mapper is null");
        return RxJavaPlugins.onAssembly(new ObservableFlattenIterable(this, mapper));
    }

    @SchedulerSupport("none")
    public final <U> Observable<U> concatMapIterable(Function<? super T, ? extends Iterable<? extends U>> mapper, int prefetch) {
        return concatMap(ObservableInternalHelper.flatMapIntoIterable(mapper), prefetch);
    }

    @SchedulerSupport("none")
    public final Observable<T> concatWith(ObservableSource<? extends T> other) {
        ObjectHelper.requireNonNull(other, "other is null");
        return concat(this, other);
    }

    @SchedulerSupport("none")
    public final Single<Boolean> contains(Object element) {
        ObjectHelper.requireNonNull(element, "element is null");
        return any(Functions.equalsWith(element));
    }

    @SchedulerSupport("none")
    public final Single<Long> count() {
        return RxJavaPlugins.onAssembly(new ObservableCountSingle(this));
    }

    @SchedulerSupport("none")
    public final <U> Observable<T> debounce(Function<? super T, ? extends ObservableSource<U>> debounceSelector) {
        ObjectHelper.requireNonNull(debounceSelector, "debounceSelector is null");
        return RxJavaPlugins.onAssembly(new ObservableDebounce(this, debounceSelector));
    }

    @SchedulerSupport("io.reactivex:computation")
    public final Observable<T> debounce(long timeout, TimeUnit unit) {
        return debounce(timeout, unit, Schedulers.computation());
    }

    @SchedulerSupport("custom")
    public final Observable<T> debounce(long timeout, TimeUnit unit, Scheduler scheduler) {
        ObjectHelper.requireNonNull(unit, "unit is null");
        ObjectHelper.requireNonNull(scheduler, "scheduler is null");
        return RxJavaPlugins.onAssembly(new ObservableDebounceTimed(this, timeout, unit, scheduler));
    }

    @SchedulerSupport("none")
    public final Observable<T> defaultIfEmpty(T defaultItem) {
        ObjectHelper.requireNonNull(defaultItem, "defaultItem is null");
        return switchIfEmpty(just(defaultItem));
    }

    @SchedulerSupport("none")
    public final <U> Observable<T> delay(Function<? super T, ? extends ObservableSource<U>> itemDelay) {
        ObjectHelper.requireNonNull(itemDelay, "itemDelay is null");
        return flatMap(ObservableInternalHelper.itemDelay(itemDelay));
    }

    @SchedulerSupport("io.reactivex:computation")
    public final Observable<T> delay(long delay, TimeUnit unit) {
        return delay(delay, unit, Schedulers.computation(), false);
    }

    @SchedulerSupport("io.reactivex:computation")
    public final Observable<T> delay(long delay, TimeUnit unit, boolean delayError) {
        return delay(delay, unit, Schedulers.computation(), delayError);
    }

    @SchedulerSupport("custom")
    public final Observable<T> delay(long delay, TimeUnit unit, Scheduler scheduler) {
        return delay(delay, unit, scheduler, false);
    }

    @SchedulerSupport("custom")
    public final Observable<T> delay(long delay, TimeUnit unit, Scheduler scheduler, boolean delayError) {
        ObjectHelper.requireNonNull(unit, "unit is null");
        ObjectHelper.requireNonNull(scheduler, "scheduler is null");
        return RxJavaPlugins.onAssembly(new ObservableDelay(this, delay, unit, scheduler, delayError));
    }

    /* JADX WARN: Type inference failed for: r2v0, types: [dji.thirdparty.io.reactivex.ObservableSource, dji.thirdparty.io.reactivex.ObservableSource<U>], assign insn: null */
    /* JADX WARNING: Unknown variable types count: 1 */
    @dji.thirdparty.io.reactivex.annotations.SchedulerSupport("none")
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final <U, V> dji.thirdparty.io.reactivex.Observable<T> delay(dji.thirdparty.io.reactivex.ObservableSource<U> r2, dji.thirdparty.io.reactivex.functions.Function<? super T, ? extends dji.thirdparty.io.reactivex.ObservableSource<V>> r3) {
        /*
            r1 = this;
            dji.thirdparty.io.reactivex.Observable r0 = r1.delaySubscription(r2)
            dji.thirdparty.io.reactivex.Observable r0 = r0.delay(r3)
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: dji.thirdparty.io.reactivex.Observable.delay(dji.thirdparty.io.reactivex.ObservableSource, dji.thirdparty.io.reactivex.functions.Function):dji.thirdparty.io.reactivex.Observable");
    }

    @SchedulerSupport("none")
    public final <U> Observable<T> delaySubscription(ObservableSource<U> other) {
        ObjectHelper.requireNonNull(other, "other is null");
        return RxJavaPlugins.onAssembly(new ObservableDelaySubscriptionOther(this, other));
    }

    @SchedulerSupport("io.reactivex:computation")
    public final Observable<T> delaySubscription(long delay, TimeUnit unit) {
        return delaySubscription(delay, unit, Schedulers.computation());
    }

    @SchedulerSupport("custom")
    public final Observable<T> delaySubscription(long delay, TimeUnit unit, Scheduler scheduler) {
        return delaySubscription(timer(delay, unit, scheduler));
    }

    @SchedulerSupport("none")
    public final <T2> Observable<T2> dematerialize() {
        return RxJavaPlugins.onAssembly(new ObservableDematerialize(this));
    }

    @SchedulerSupport("none")
    public final Observable<T> distinct() {
        return distinct(Functions.identity(), Functions.createHashSet());
    }

    @SchedulerSupport("none")
    public final <K> Observable<T> distinct(Function<? super T, K> keySelector) {
        return distinct(keySelector, Functions.createHashSet());
    }

    @SchedulerSupport("none")
    public final <K> Observable<T> distinct(Function<? super T, K> keySelector, Callable<? extends Collection<? super K>> collectionSupplier) {
        ObjectHelper.requireNonNull(keySelector, "keySelector is null");
        ObjectHelper.requireNonNull(collectionSupplier, "collectionSupplier is null");
        return new ObservableDistinct(this, keySelector, collectionSupplier);
    }

    @SchedulerSupport("none")
    public final Observable<T> distinctUntilChanged() {
        return distinctUntilChanged(Functions.identity());
    }

    @SchedulerSupport("none")
    public final <K> Observable<T> distinctUntilChanged(Function<? super T, K> keySelector) {
        ObjectHelper.requireNonNull(keySelector, "keySelector is null");
        return RxJavaPlugins.onAssembly(new ObservableDistinctUntilChanged(this, keySelector, ObjectHelper.equalsPredicate()));
    }

    @SchedulerSupport("none")
    public final Observable<T> distinctUntilChanged(BiPredicate<? super T, ? super T> comparer) {
        ObjectHelper.requireNonNull(comparer, "comparer is null");
        return RxJavaPlugins.onAssembly(new ObservableDistinctUntilChanged(this, Functions.identity(), comparer));
    }

    @SchedulerSupport("none")
    public final Observable<T> doAfterTerminate(Action onFinally) {
        ObjectHelper.requireNonNull(onFinally, "onFinally is null");
        return doOnEach(Functions.emptyConsumer(), Functions.emptyConsumer(), Functions.EMPTY_ACTION, onFinally);
    }

    @SchedulerSupport("none")
    public final Observable<T> doOnDispose(Action onDispose) {
        return doOnLifecycle(Functions.emptyConsumer(), onDispose);
    }

    @SchedulerSupport("none")
    public final Observable<T> doOnComplete(Action onComplete) {
        return doOnEach(Functions.emptyConsumer(), Functions.emptyConsumer(), onComplete, Functions.EMPTY_ACTION);
    }

    @SchedulerSupport("none")
    private Observable<T> doOnEach(Consumer<? super T> onNext, Consumer<? super Throwable> onError, Action onComplete, Action onAfterTerminate) {
        ObjectHelper.requireNonNull(onNext, "onNext is null");
        ObjectHelper.requireNonNull(onError, "onError is null");
        ObjectHelper.requireNonNull(onComplete, "onComplete is null");
        ObjectHelper.requireNonNull(onAfterTerminate, "onAfterTerminate is null");
        return RxJavaPlugins.onAssembly(new ObservableDoOnEach(this, onNext, onError, onComplete, onAfterTerminate));
    }

    @SchedulerSupport("none")
    public final Observable<T> doOnEach(Consumer<? super Notification<T>> onNotification) {
        ObjectHelper.requireNonNull(onNotification, "consumer is null");
        return doOnEach(Functions.notificationOnNext(onNotification), Functions.notificationOnError(onNotification), Functions.notificationOnComplete(onNotification), Functions.EMPTY_ACTION);
    }

    @SchedulerSupport("none")
    public final Observable<T> doOnEach(Observer<? super T> observer) {
        ObjectHelper.requireNonNull(observer, "observer is null");
        return doOnEach(ObservableInternalHelper.observerOnNext(observer), ObservableInternalHelper.observerOnError(observer), ObservableInternalHelper.observerOnComplete(observer), Functions.EMPTY_ACTION);
    }

    @SchedulerSupport("none")
    public final Observable<T> doOnError(Consumer<? super Throwable> onError) {
        return doOnEach(Functions.emptyConsumer(), onError, Functions.EMPTY_ACTION, Functions.EMPTY_ACTION);
    }

    @SchedulerSupport("none")
    public final Observable<T> doOnLifecycle(Consumer<? super Disposable> onSubscribe, Action onDispose) {
        ObjectHelper.requireNonNull(onSubscribe, "onSubscribe is null");
        ObjectHelper.requireNonNull(onDispose, "onDispose is null");
        return RxJavaPlugins.onAssembly(new ObservableDoOnLifecycle(this, onSubscribe, onDispose));
    }

    @SchedulerSupport("none")
    public final Observable<T> doOnNext(Consumer<? super T> onNext) {
        return doOnEach(onNext, Functions.emptyConsumer(), Functions.EMPTY_ACTION, Functions.EMPTY_ACTION);
    }

    @SchedulerSupport("none")
    public final Observable<T> doOnSubscribe(Consumer<? super Disposable> onSubscribe) {
        return doOnLifecycle(onSubscribe, Functions.EMPTY_ACTION);
    }

    @SchedulerSupport("none")
    public final Observable<T> doOnTerminate(Action onTerminate) {
        ObjectHelper.requireNonNull(onTerminate, "onTerminate is null");
        return doOnEach(Functions.emptyConsumer(), Functions.actionConsumer(onTerminate), onTerminate, Functions.EMPTY_ACTION);
    }

    @SchedulerSupport("none")
    public final Maybe<T> elementAt(long index) {
        if (index >= 0) {
            return RxJavaPlugins.onAssembly(new ObservableElementAtMaybe(this, index));
        }
        throw new IndexOutOfBoundsException("index >= 0 required but it was " + index);
    }

    @SchedulerSupport("none")
    public final Single<T> elementAt(long index, T defaultItem) {
        if (index < 0) {
            throw new IndexOutOfBoundsException("index >= 0 required but it was " + index);
        }
        ObjectHelper.requireNonNull(defaultItem, "defaultItem is null");
        return RxJavaPlugins.onAssembly(new ObservableElementAtSingle(this, index, defaultItem));
    }

    @SchedulerSupport("none")
    public final Single<T> elementAtOrError(long index) {
        if (index >= 0) {
            return RxJavaPlugins.onAssembly(new ObservableElementAtSingle(this, index, null));
        }
        throw new IndexOutOfBoundsException("index >= 0 required but it was " + index);
    }

    @SchedulerSupport("none")
    public final Observable<T> filter(Predicate<? super T> predicate) {
        ObjectHelper.requireNonNull(predicate, "predicate is null");
        return RxJavaPlugins.onAssembly(new ObservableFilter(this, predicate));
    }

    @SchedulerSupport("none")
    public final Maybe<T> firstElement() {
        return elementAt(0);
    }

    @SchedulerSupport("none")
    public final Single<T> first(T defaultItem) {
        return elementAt(0, defaultItem);
    }

    @SchedulerSupport("none")
    public final Single<T> firstOrError() {
        return elementAtOrError(0);
    }

    /* JADX DEBUG: Failed to find minimal casts for resolve overloaded methods, cast all args instead
     method: dji.thirdparty.io.reactivex.Observable.flatMap(dji.thirdparty.io.reactivex.functions.Function, boolean):dji.thirdparty.io.reactivex.Observable<R>
     arg types: [dji.thirdparty.io.reactivex.functions.Function<? super T, ? extends dji.thirdparty.io.reactivex.ObservableSource<? extends R>>, int]
     candidates:
      dji.thirdparty.io.reactivex.Observable.flatMap(dji.thirdparty.io.reactivex.functions.Function, int):dji.thirdparty.io.reactivex.Observable<R>
      dji.thirdparty.io.reactivex.Observable.flatMap(dji.thirdparty.io.reactivex.functions.Function, dji.thirdparty.io.reactivex.functions.BiFunction):dji.thirdparty.io.reactivex.Observable<R>
      dji.thirdparty.io.reactivex.Observable.flatMap(dji.thirdparty.io.reactivex.functions.Function, boolean):dji.thirdparty.io.reactivex.Observable<R> */
    @SchedulerSupport("none")
    public final <R> Observable<R> flatMap(Function<? super T, ? extends ObservableSource<? extends R>> mapper) {
        return flatMap((Function) mapper, false);
    }

    @SchedulerSupport("none")
    public final <R> Observable<R> flatMap(Function<? super T, ? extends ObservableSource<? extends R>> mapper, boolean delayErrors) {
        return flatMap(mapper, delayErrors, Integer.MAX_VALUE);
    }

    @SchedulerSupport("none")
    public final <R> Observable<R> flatMap(Function<? super T, ? extends ObservableSource<? extends R>> mapper, boolean delayErrors, int maxConcurrency) {
        return flatMap(mapper, delayErrors, maxConcurrency, bufferSize());
    }

    /* JADX WARN: Type inference failed for: r8v0, types: [dji.thirdparty.io.reactivex.functions.Function, java.lang.Object, dji.thirdparty.io.reactivex.functions.Function<? super T, ? extends dji.thirdparty.io.reactivex.ObservableSource<? extends R>>], assign insn: null */
    /* JADX WARNING: Unknown variable types count: 1 */
    @dji.thirdparty.io.reactivex.annotations.SchedulerSupport("none")
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final <R> dji.thirdparty.io.reactivex.Observable<R> flatMap(dji.thirdparty.io.reactivex.functions.Function<? super T, ? extends dji.thirdparty.io.reactivex.ObservableSource<? extends R>> r8, boolean r9, int r10, int r11) {
        /*
            r7 = this;
            java.lang.String r0 = "mapper is null"
            dji.thirdparty.io.reactivex.internal.functions.ObjectHelper.requireNonNull(r8, r0)
            java.lang.String r0 = "maxConcurrency"
            dji.thirdparty.io.reactivex.internal.functions.ObjectHelper.verifyPositive(r10, r0)
            java.lang.String r0 = "bufferSize"
            dji.thirdparty.io.reactivex.internal.functions.ObjectHelper.verifyPositive(r11, r0)
            boolean r0 = r7 instanceof dji.thirdparty.io.reactivex.internal.fuseable.ScalarCallable
            if (r0 == 0) goto L_0x0028
            dji.thirdparty.io.reactivex.internal.fuseable.ScalarCallable r7 = (dji.thirdparty.io.reactivex.internal.fuseable.ScalarCallable) r7
            java.lang.Object r6 = r7.call()
            if (r6 != 0) goto L_0x0023
            dji.thirdparty.io.reactivex.Observable r0 = empty()
        L_0x0022:
            return r0
        L_0x0023:
            dji.thirdparty.io.reactivex.Observable r0 = dji.thirdparty.io.reactivex.internal.operators.observable.ObservableScalarXMap.scalarXMap(r6, r8)
            goto L_0x0022
        L_0x0028:
            dji.thirdparty.io.reactivex.internal.operators.observable.ObservableFlatMap r0 = new dji.thirdparty.io.reactivex.internal.operators.observable.ObservableFlatMap
            r1 = r7
            r2 = r8
            r3 = r9
            r4 = r10
            r5 = r11
            r0.<init>(r1, r2, r3, r4, r5)
            dji.thirdparty.io.reactivex.Observable r0 = dji.thirdparty.io.reactivex.plugins.RxJavaPlugins.onAssembly(r0)
            goto L_0x0022
        */
        throw new UnsupportedOperationException("Method not decompiled: dji.thirdparty.io.reactivex.Observable.flatMap(dji.thirdparty.io.reactivex.functions.Function, boolean, int, int):dji.thirdparty.io.reactivex.Observable");
    }

    @SchedulerSupport("none")
    public final <R> Observable<R> flatMap(Function<? super T, ? extends ObservableSource<? extends R>> onNextMapper, Function<? super Throwable, ? extends ObservableSource<? extends R>> onErrorMapper, Callable<? extends ObservableSource<? extends R>> onCompleteSupplier) {
        ObjectHelper.requireNonNull(onNextMapper, "onNextMapper is null");
        ObjectHelper.requireNonNull(onErrorMapper, "onErrorMapper is null");
        ObjectHelper.requireNonNull(onCompleteSupplier, "onCompleteSupplier is null");
        return merge(new ObservableMapNotification(this, onNextMapper, onErrorMapper, onCompleteSupplier));
    }

    @SchedulerSupport("none")
    public final <R> Observable<R> flatMap(Function<? super T, ? extends ObservableSource<? extends R>> onNextMapper, Function<Throwable, ? extends ObservableSource<? extends R>> onErrorMapper, Callable<? extends ObservableSource<? extends R>> onCompleteSupplier, int maxConcurrency) {
        ObjectHelper.requireNonNull(onNextMapper, "onNextMapper is null");
        ObjectHelper.requireNonNull(onErrorMapper, "onErrorMapper is null");
        ObjectHelper.requireNonNull(onCompleteSupplier, "onCompleteSupplier is null");
        return merge(new ObservableMapNotification(this, onNextMapper, onErrorMapper, onCompleteSupplier), maxConcurrency);
    }

    /* JADX DEBUG: Failed to find minimal casts for resolve overloaded methods, cast all args instead
     method: dji.thirdparty.io.reactivex.Observable.flatMap(dji.thirdparty.io.reactivex.functions.Function, boolean, int, int):dji.thirdparty.io.reactivex.Observable<R>
     arg types: [dji.thirdparty.io.reactivex.functions.Function<? super T, ? extends dji.thirdparty.io.reactivex.ObservableSource<? extends R>>, int, int, int]
     candidates:
      dji.thirdparty.io.reactivex.Observable.flatMap(dji.thirdparty.io.reactivex.functions.Function, dji.thirdparty.io.reactivex.functions.BiFunction, boolean, int):dji.thirdparty.io.reactivex.Observable<R>
      dji.thirdparty.io.reactivex.Observable.flatMap(dji.thirdparty.io.reactivex.functions.Function, dji.thirdparty.io.reactivex.functions.Function, java.util.concurrent.Callable, int):dji.thirdparty.io.reactivex.Observable<R>
      dji.thirdparty.io.reactivex.Observable.flatMap(dji.thirdparty.io.reactivex.functions.Function, boolean, int, int):dji.thirdparty.io.reactivex.Observable<R> */
    @SchedulerSupport("none")
    public final <R> Observable<R> flatMap(Function<? super T, ? extends ObservableSource<? extends R>> mapper, int maxConcurrency) {
        return flatMap((Function) mapper, false, maxConcurrency, bufferSize());
    }

    @SchedulerSupport("none")
    public final <U, R> Observable<R> flatMap(Function<? super T, ? extends ObservableSource<? extends U>> mapper, BiFunction<? super T, ? super U, ? extends R> resultSelector) {
        return flatMap(mapper, resultSelector, false, bufferSize(), bufferSize());
    }

    @SchedulerSupport("none")
    public final <U, R> Observable<R> flatMap(Function<? super T, ? extends ObservableSource<? extends U>> mapper, BiFunction<? super T, ? super U, ? extends R> combiner, boolean delayErrors) {
        return flatMap(mapper, combiner, delayErrors, bufferSize(), bufferSize());
    }

    @SchedulerSupport("none")
    public final <U, R> Observable<R> flatMap(Function<? super T, ? extends ObservableSource<? extends U>> mapper, BiFunction<? super T, ? super U, ? extends R> combiner, boolean delayErrors, int maxConcurrency) {
        return flatMap(mapper, combiner, delayErrors, maxConcurrency, bufferSize());
    }

    @SchedulerSupport("none")
    public final <U, R> Observable<R> flatMap(Function<? super T, ? extends ObservableSource<? extends U>> mapper, BiFunction<? super T, ? super U, ? extends R> combiner, boolean delayErrors, int maxConcurrency, int bufferSize) {
        ObjectHelper.requireNonNull(mapper, "mapper is null");
        ObjectHelper.requireNonNull(combiner, "combiner is null");
        return flatMap(ObservableInternalHelper.flatMapWithCombiner(mapper, combiner), delayErrors, maxConcurrency, bufferSize);
    }

    @SchedulerSupport("none")
    public final <U, R> Observable<R> flatMap(Function<? super T, ? extends ObservableSource<? extends U>> mapper, BiFunction<? super T, ? super U, ? extends R> combiner, int maxConcurrency) {
        return flatMap(mapper, combiner, false, maxConcurrency, bufferSize());
    }

    @SchedulerSupport("none")
    public final Completable flatMapCompletable(Function<? super T, ? extends CompletableSource> mapper) {
        return flatMapCompletable(mapper, false);
    }

    @SchedulerSupport("none")
    public final Completable flatMapCompletable(Function<? super T, ? extends CompletableSource> mapper, boolean delayErrors) {
        ObjectHelper.requireNonNull(mapper, "mapper is null");
        return RxJavaPlugins.onAssembly(new ObservableFlatMapCompletableCompletable(this, mapper, delayErrors));
    }

    @SchedulerSupport("none")
    public final <U> Observable<U> flatMapIterable(Function<? super T, ? extends Iterable<? extends U>> mapper) {
        ObjectHelper.requireNonNull(mapper, "mapper is null");
        return RxJavaPlugins.onAssembly(new ObservableFlattenIterable(this, mapper));
    }

    @SchedulerSupport("none")
    public final <U, V> Observable<V> flatMapIterable(Function<? super T, ? extends Iterable<? extends U>> mapper, BiFunction<? super T, ? super U, ? extends V> resultSelector) {
        return flatMap(ObservableInternalHelper.flatMapIntoIterable(mapper), resultSelector, false, bufferSize(), bufferSize());
    }

    @SchedulerSupport("none")
    public final <R> Observable<R> flatMapMaybe(Function<? super T, ? extends MaybeSource<? extends R>> mapper) {
        return flatMapMaybe(mapper, false);
    }

    @SchedulerSupport("none")
    public final <R> Observable<R> flatMapMaybe(Function<? super T, ? extends MaybeSource<? extends R>> mapper, boolean delayErrors) {
        ObjectHelper.requireNonNull(mapper, "mapper is null");
        return RxJavaPlugins.onAssembly(new ObservableFlatMapMaybe(this, mapper, delayErrors));
    }

    @SchedulerSupport("none")
    public final <R> Observable<R> flatMapSingle(Function<? super T, ? extends SingleSource<? extends R>> mapper) {
        return flatMapSingle(mapper, false);
    }

    @SchedulerSupport("none")
    public final <R> Observable<R> flatMapSingle(Function<? super T, ? extends SingleSource<? extends R>> mapper, boolean delayErrors) {
        ObjectHelper.requireNonNull(mapper, "mapper is null");
        return RxJavaPlugins.onAssembly(new ObservableFlatMapSingle(this, mapper, delayErrors));
    }

    @SchedulerSupport("none")
    public final Disposable forEach(Consumer<? super T> onNext) {
        return subscribe(onNext);
    }

    @SchedulerSupport("none")
    public final Disposable forEachWhile(Predicate<? super T> onNext) {
        return forEachWhile(onNext, Functions.ERROR_CONSUMER, Functions.EMPTY_ACTION);
    }

    @SchedulerSupport("none")
    public final Disposable forEachWhile(Predicate<? super T> onNext, Consumer<? super Throwable> onError) {
        return forEachWhile(onNext, onError, Functions.EMPTY_ACTION);
    }

    @SchedulerSupport("none")
    public final Disposable forEachWhile(Predicate<? super T> onNext, Consumer<? super Throwable> onError, Action onComplete) {
        ObjectHelper.requireNonNull(onNext, "onNext is null");
        ObjectHelper.requireNonNull(onError, "onError is null");
        ObjectHelper.requireNonNull(onComplete, "onComplete is null");
        ForEachWhileObserver<T> o = new ForEachWhileObserver<>(onNext, onError, onComplete);
        subscribe(o);
        return o;
    }

    /* JADX WARN: Type inference failed for: r4v0, types: [dji.thirdparty.io.reactivex.functions.Function, dji.thirdparty.io.reactivex.functions.Function<? super T, ? extends K>], assign insn: null */
    /* JADX WARNING: Unknown variable types count: 1 */
    @dji.thirdparty.io.reactivex.annotations.SchedulerSupport("none")
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final <K> dji.thirdparty.io.reactivex.Observable<dji.thirdparty.io.reactivex.observables.GroupedObservable<K, T>> groupBy(dji.thirdparty.io.reactivex.functions.Function<? super T, ? extends K> r4) {
        /*
            r3 = this;
            dji.thirdparty.io.reactivex.functions.Function r0 = dji.thirdparty.io.reactivex.internal.functions.Functions.identity()
            r1 = 0
            int r2 = bufferSize()
            dji.thirdparty.io.reactivex.Observable r0 = r3.groupBy(r4, r0, r1, r2)
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: dji.thirdparty.io.reactivex.Observable.groupBy(dji.thirdparty.io.reactivex.functions.Function):dji.thirdparty.io.reactivex.Observable");
    }

    /* JADX WARN: Type inference failed for: r3v0, types: [dji.thirdparty.io.reactivex.functions.Function, dji.thirdparty.io.reactivex.functions.Function<? super T, ? extends K>], assign insn: null */
    /* JADX WARNING: Unknown variable types count: 1 */
    @dji.thirdparty.io.reactivex.annotations.SchedulerSupport("none")
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final <K> dji.thirdparty.io.reactivex.Observable<dji.thirdparty.io.reactivex.observables.GroupedObservable<K, T>> groupBy(dji.thirdparty.io.reactivex.functions.Function<? super T, ? extends K> r3, boolean r4) {
        /*
            r2 = this;
            dji.thirdparty.io.reactivex.functions.Function r0 = dji.thirdparty.io.reactivex.internal.functions.Functions.identity()
            int r1 = bufferSize()
            dji.thirdparty.io.reactivex.Observable r0 = r2.groupBy(r3, r0, r4, r1)
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: dji.thirdparty.io.reactivex.Observable.groupBy(dji.thirdparty.io.reactivex.functions.Function, boolean):dji.thirdparty.io.reactivex.Observable");
    }

    @SchedulerSupport("none")
    public final <K, V> Observable<GroupedObservable<K, V>> groupBy(Function<? super T, ? extends K> keySelector, Function<? super T, ? extends V> valueSelector) {
        return groupBy(keySelector, valueSelector, false, bufferSize());
    }

    @SchedulerSupport("none")
    public final <K, V> Observable<GroupedObservable<K, V>> groupBy(Function<? super T, ? extends K> keySelector, Function<? super T, ? extends V> valueSelector, boolean delayError) {
        return groupBy(keySelector, valueSelector, delayError, bufferSize());
    }

    @SchedulerSupport("none")
    public final <K, V> Observable<GroupedObservable<K, V>> groupBy(Function<? super T, ? extends K> keySelector, Function<? super T, ? extends V> valueSelector, boolean delayError, int bufferSize) {
        ObjectHelper.requireNonNull(keySelector, "keySelector is null");
        ObjectHelper.requireNonNull(valueSelector, "valueSelector is null");
        ObjectHelper.verifyPositive(bufferSize, "bufferSize");
        return RxJavaPlugins.onAssembly(new ObservableGroupBy(this, keySelector, valueSelector, bufferSize, delayError));
    }

    @SchedulerSupport("none")
    public final <TRight, TLeftEnd, TRightEnd, R> Observable<R> groupJoin(ObservableSource<? extends TRight> other, Function<? super T, ? extends ObservableSource<TLeftEnd>> leftEnd, Function<? super TRight, ? extends ObservableSource<TRightEnd>> rightEnd, BiFunction<? super T, ? super Observable<TRight>, ? extends R> resultSelector) {
        return RxJavaPlugins.onAssembly(new ObservableGroupJoin(this, other, leftEnd, rightEnd, resultSelector));
    }

    @SchedulerSupport("none")
    public final Observable<T> hide() {
        return RxJavaPlugins.onAssembly(new ObservableHide(this));
    }

    @SchedulerSupport("none")
    public final Completable ignoreElements() {
        return RxJavaPlugins.onAssembly(new ObservableIgnoreElementsCompletable(this));
    }

    @SchedulerSupport("none")
    public final Single<Boolean> isEmpty() {
        return all(Functions.alwaysFalse());
    }

    @SchedulerSupport("none")
    public final <TRight, TLeftEnd, TRightEnd, R> Observable<R> join(ObservableSource<? extends TRight> other, Function<? super T, ? extends ObservableSource<TLeftEnd>> leftEnd, Function<? super TRight, ? extends ObservableSource<TRightEnd>> rightEnd, BiFunction<? super T, ? super TRight, ? extends R> resultSelector) {
        return RxJavaPlugins.onAssembly(new ObservableJoin(this, other, leftEnd, rightEnd, resultSelector));
    }

    @SchedulerSupport("none")
    public final Maybe<T> lastElement() {
        return RxJavaPlugins.onAssembly(new ObservableLastMaybe(this));
    }

    @SchedulerSupport("none")
    public final Single<T> last(T defaultItem) {
        ObjectHelper.requireNonNull(defaultItem, "defaultItem is null");
        return RxJavaPlugins.onAssembly(new ObservableLastSingle(this, defaultItem));
    }

    @SchedulerSupport("none")
    public final Single<T> lastOrError() {
        return RxJavaPlugins.onAssembly(new ObservableLastSingle(this, null));
    }

    @SchedulerSupport("none")
    public final <R> Observable<R> lift(ObservableOperator<? extends R, ? super T> lifter) {
        ObjectHelper.requireNonNull(lifter, "onLift is null");
        return RxJavaPlugins.onAssembly(new ObservableLift(this, lifter));
    }

    @SchedulerSupport("none")
    public final <R> Observable<R> map(Function<? super T, ? extends R> mapper) {
        ObjectHelper.requireNonNull(mapper, "mapper is null");
        return RxJavaPlugins.onAssembly(new ObservableMap(this, mapper));
    }

    @SchedulerSupport("none")
    public final Observable<Notification<T>> materialize() {
        return RxJavaPlugins.onAssembly(new ObservableMaterialize(this));
    }

    @SchedulerSupport("none")
    public final Observable<T> mergeWith(ObservableSource<? extends T> other) {
        ObjectHelper.requireNonNull(other, "other is null");
        return merge(this, other);
    }

    @SchedulerSupport("custom")
    public final Observable<T> observeOn(Scheduler scheduler) {
        return observeOn(scheduler, false, bufferSize());
    }

    @SchedulerSupport("custom")
    public final Observable<T> observeOn(Scheduler scheduler, boolean delayError) {
        return observeOn(scheduler, delayError, bufferSize());
    }

    @SchedulerSupport("custom")
    public final Observable<T> observeOn(Scheduler scheduler, boolean delayError, int bufferSize) {
        ObjectHelper.requireNonNull(scheduler, "scheduler is null");
        ObjectHelper.verifyPositive(bufferSize, "bufferSize");
        return RxJavaPlugins.onAssembly(new ObservableObserveOn(this, scheduler, delayError, bufferSize));
    }

    @SchedulerSupport("none")
    public final <U> Observable<U> ofType(Class<U> clazz) {
        ObjectHelper.requireNonNull(clazz, "clazz is null");
        return filter(Functions.isInstanceOf(clazz)).cast(clazz);
    }

    @SchedulerSupport("none")
    public final Observable<T> onErrorResumeNext(Function<? super Throwable, ? extends ObservableSource<? extends T>> resumeFunction) {
        ObjectHelper.requireNonNull(resumeFunction, "resumeFunction is null");
        return RxJavaPlugins.onAssembly(new ObservableOnErrorNext(this, resumeFunction, false));
    }

    @SchedulerSupport("none")
    public final Observable<T> onErrorResumeNext(ObservableSource<? extends T> next) {
        ObjectHelper.requireNonNull(next, "next is null");
        return onErrorResumeNext(Functions.justFunction(next));
    }

    @SchedulerSupport("none")
    public final Observable<T> onErrorReturn(Function<? super Throwable, ? extends T> valueSupplier) {
        ObjectHelper.requireNonNull(valueSupplier, "valueSupplier is null");
        return RxJavaPlugins.onAssembly(new ObservableOnErrorReturn(this, valueSupplier));
    }

    @SchedulerSupport("none")
    public final Observable<T> onErrorReturnItem(T item) {
        ObjectHelper.requireNonNull(item, "item is null");
        return onErrorReturn(Functions.justFunction(item));
    }

    @SchedulerSupport("none")
    public final Observable<T> onExceptionResumeNext(ObservableSource<? extends T> next) {
        ObjectHelper.requireNonNull(next, "next is null");
        return RxJavaPlugins.onAssembly(new ObservableOnErrorNext(this, Functions.justFunction(next), true));
    }

    @SchedulerSupport("none")
    public final Observable<T> onTerminateDetach() {
        return RxJavaPlugins.onAssembly(new ObservableDetach(this));
    }

    @SchedulerSupport("none")
    public final ConnectableObservable<T> publish() {
        return ObservablePublish.create(this);
    }

    @SchedulerSupport("none")
    public final <R> Observable<R> publish(Function<? super Observable<T>, ? extends ObservableSource<R>> selector) {
        ObjectHelper.requireNonNull(selector, "selector is null");
        return new ObservablePublishSelector(this, selector);
    }

    @SchedulerSupport("none")
    public final Maybe<T> reduce(BiFunction<T, T, T> reducer) {
        return scan(reducer).takeLast(1).singleElement();
    }

    @SchedulerSupport("none")
    public final <R> Single<R> reduce(R seed, BiFunction<R, ? super T, R> reducer) {
        return RxJavaPlugins.onAssembly(new ObservableSingleSingle(scan(seed, reducer).takeLast(1), null));
    }

    @SchedulerSupport("none")
    public final <R> Single<R> reduceWith(Callable<R> seedSupplier, BiFunction<R, ? super T, R> reducer) {
        return RxJavaPlugins.onAssembly(new ObservableSingleSingle(scanWith(seedSupplier, reducer).takeLast(1), null));
    }

    @SchedulerSupport("none")
    public final Observable<T> repeat() {
        return repeat(LongCompanionObject.MAX_VALUE);
    }

    @SchedulerSupport("none")
    public final Observable<T> repeat(long times) {
        if (times < 0) {
            throw new IllegalArgumentException("times >= 0 required but it was " + times);
        } else if (times == 0) {
            return empty();
        } else {
            return RxJavaPlugins.onAssembly(new ObservableRepeat(this, times));
        }
    }

    @SchedulerSupport("none")
    public final Observable<T> repeatUntil(BooleanSupplier stop) {
        ObjectHelper.requireNonNull(stop, "stop is null");
        return RxJavaPlugins.onAssembly(new ObservableRepeatUntil(this, stop));
    }

    @SchedulerSupport("none")
    public final Observable<T> repeatWhen(Function<? super Observable<Object>, ? extends ObservableSource<?>> handler) {
        ObjectHelper.requireNonNull(handler, "handler is null");
        return RxJavaPlugins.onAssembly(new ObservableRedo(this, ObservableInternalHelper.repeatWhenHandler(handler)));
    }

    @SchedulerSupport("none")
    public final ConnectableObservable<T> replay() {
        return ObservableReplay.createFrom(this);
    }

    @SchedulerSupport("none")
    public final <R> Observable<R> replay(Function<? super Observable<T>, ? extends ObservableSource<R>> selector) {
        ObjectHelper.requireNonNull(selector, "selector is null");
        return ObservableReplay.multicastSelector(ObservableInternalHelper.replayCallable(this), selector);
    }

    @SchedulerSupport("none")
    public final <R> Observable<R> replay(Function<? super Observable<T>, ? extends ObservableSource<R>> selector, int bufferSize) {
        ObjectHelper.requireNonNull(selector, "selector is null");
        return ObservableReplay.multicastSelector(ObservableInternalHelper.replayCallable(this, bufferSize), selector);
    }

    @SchedulerSupport("io.reactivex:computation")
    public final <R> Observable<R> replay(Function<? super Observable<T>, ? extends ObservableSource<R>> selector, int bufferSize, long time, TimeUnit unit) {
        return replay(selector, bufferSize, time, unit, Schedulers.computation());
    }

    @SchedulerSupport("custom")
    public final <R> Observable<R> replay(Function<? super Observable<T>, ? extends ObservableSource<R>> selector, int bufferSize, long time, TimeUnit unit, Scheduler scheduler) {
        ObjectHelper.verifyPositive(bufferSize, "bufferSize");
        ObjectHelper.requireNonNull(selector, "selector is null");
        return ObservableReplay.multicastSelector(ObservableInternalHelper.replayCallable(this, bufferSize, time, unit, scheduler), selector);
    }

    @SchedulerSupport("custom")
    public final <R> Observable<R> replay(Function<? super Observable<T>, ? extends ObservableSource<R>> selector, int bufferSize, Scheduler scheduler) {
        return ObservableReplay.multicastSelector(ObservableInternalHelper.replayCallable(this, bufferSize), ObservableInternalHelper.replayFunction(selector, scheduler));
    }

    @SchedulerSupport("io.reactivex:computation")
    public final <R> Observable<R> replay(Function<? super Observable<T>, ? extends ObservableSource<R>> selector, long time, TimeUnit unit) {
        return replay(selector, time, unit, Schedulers.computation());
    }

    @SchedulerSupport("custom")
    public final <R> Observable<R> replay(Function<? super Observable<T>, ? extends ObservableSource<R>> selector, long time, TimeUnit unit, Scheduler scheduler) {
        ObjectHelper.requireNonNull(selector, "selector is null");
        ObjectHelper.requireNonNull(unit, "unit is null");
        ObjectHelper.requireNonNull(scheduler, "scheduler is null");
        return ObservableReplay.multicastSelector(ObservableInternalHelper.replayCallable(this, time, unit, scheduler), selector);
    }

    @SchedulerSupport("custom")
    public final <R> Observable<R> replay(Function<? super Observable<T>, ? extends ObservableSource<R>> selector, Scheduler scheduler) {
        ObjectHelper.requireNonNull(selector, "selector is null");
        ObjectHelper.requireNonNull(scheduler, "scheduler is null");
        return ObservableReplay.multicastSelector(ObservableInternalHelper.replayCallable(this), ObservableInternalHelper.replayFunction(selector, scheduler));
    }

    @SchedulerSupport("none")
    public final ConnectableObservable<T> replay(int bufferSize) {
        return ObservableReplay.create(this, bufferSize);
    }

    @SchedulerSupport("io.reactivex:computation")
    public final ConnectableObservable<T> replay(int bufferSize, long time, TimeUnit unit) {
        return replay(bufferSize, time, unit, Schedulers.computation());
    }

    @SchedulerSupport("custom")
    public final ConnectableObservable<T> replay(int bufferSize, long time, TimeUnit unit, Scheduler scheduler) {
        ObjectHelper.verifyPositive(bufferSize, "bufferSize");
        ObjectHelper.requireNonNull(unit, "unit is null");
        ObjectHelper.requireNonNull(scheduler, "scheduler is null");
        return ObservableReplay.create(this, time, unit, scheduler, bufferSize);
    }

    @SchedulerSupport("custom")
    public final ConnectableObservable<T> replay(int bufferSize, Scheduler scheduler) {
        return ObservableReplay.observeOn(replay(bufferSize), scheduler);
    }

    @SchedulerSupport("io.reactivex:computation")
    public final ConnectableObservable<T> replay(long time, TimeUnit unit) {
        return replay(time, unit, Schedulers.computation());
    }

    @SchedulerSupport("custom")
    public final ConnectableObservable<T> replay(long time, TimeUnit unit, Scheduler scheduler) {
        ObjectHelper.requireNonNull(unit, "unit is null");
        ObjectHelper.requireNonNull(scheduler, "scheduler is null");
        return ObservableReplay.create(this, time, unit, scheduler);
    }

    @SchedulerSupport("custom")
    public final ConnectableObservable<T> replay(Scheduler scheduler) {
        ObjectHelper.requireNonNull(scheduler, "scheduler is null");
        return ObservableReplay.observeOn(replay(), scheduler);
    }

    @SchedulerSupport("none")
    public final Observable<T> retry() {
        return retry(LongCompanionObject.MAX_VALUE, Functions.alwaysTrue());
    }

    @SchedulerSupport("none")
    public final Observable<T> retry(BiPredicate<? super Integer, ? super Throwable> predicate) {
        ObjectHelper.requireNonNull(predicate, "predicate is null");
        return RxJavaPlugins.onAssembly(new ObservableRetryBiPredicate(this, predicate));
    }

    @SchedulerSupport("none")
    public final Observable<T> retry(long times) {
        return retry(times, Functions.alwaysTrue());
    }

    @SchedulerSupport("none")
    public final Observable<T> retry(long times, Predicate<? super Throwable> predicate) {
        if (times < 0) {
            throw new IllegalArgumentException("times >= 0 required but it was " + times);
        }
        ObjectHelper.requireNonNull(predicate, "predicate is null");
        return RxJavaPlugins.onAssembly(new ObservableRetryPredicate(this, times, predicate));
    }

    @SchedulerSupport("none")
    public final Observable<T> retry(Predicate<? super Throwable> predicate) {
        return retry(LongCompanionObject.MAX_VALUE, predicate);
    }

    @SchedulerSupport("none")
    public final Observable<T> retryUntil(BooleanSupplier stop) {
        ObjectHelper.requireNonNull(stop, "stop is null");
        return retry(LongCompanionObject.MAX_VALUE, Functions.predicateReverseFor(stop));
    }

    @SchedulerSupport("none")
    public final Observable<T> retryWhen(Function<? super Observable<Throwable>, ? extends ObservableSource<?>> handler) {
        ObjectHelper.requireNonNull(handler, "handler is null");
        return RxJavaPlugins.onAssembly(new ObservableRedo(this, ObservableInternalHelper.retryWhenHandler(handler)));
    }

    @SchedulerSupport("none")
    public final void safeSubscribe(Observer<? super T> s) {
        ObjectHelper.requireNonNull(s, "s is null");
        if (s instanceof SafeObserver) {
            subscribe(s);
        } else {
            subscribe(new SafeObserver(s));
        }
    }

    @SchedulerSupport("io.reactivex:computation")
    public final Observable<T> sample(long period, TimeUnit unit) {
        return sample(period, unit, Schedulers.computation());
    }

    @SchedulerSupport("custom")
    public final Observable<T> sample(long period, TimeUnit unit, Scheduler scheduler) {
        ObjectHelper.requireNonNull(unit, "unit is null");
        ObjectHelper.requireNonNull(scheduler, "scheduler is null");
        return RxJavaPlugins.onAssembly(new ObservableSampleTimed(this, period, unit, scheduler));
    }

    @SchedulerSupport("none")
    public final <U> Observable<T> sample(ObservableSource<U> sampler) {
        ObjectHelper.requireNonNull(sampler, "sampler is null");
        return RxJavaPlugins.onAssembly(new ObservableSampleWithObservable(this, sampler));
    }

    @SchedulerSupport("none")
    public final Observable<T> scan(BiFunction<T, T, T> accumulator) {
        ObjectHelper.requireNonNull(accumulator, "accumulator is null");
        return RxJavaPlugins.onAssembly(new ObservableScan(this, accumulator));
    }

    @SchedulerSupport("none")
    public final <R> Observable<R> scan(R initialValue, BiFunction<R, ? super T, R> accumulator) {
        ObjectHelper.requireNonNull(initialValue, "seed is null");
        return scanWith(Functions.justCallable(initialValue), accumulator);
    }

    @SchedulerSupport("none")
    public final <R> Observable<R> scanWith(Callable<R> seedSupplier, BiFunction<R, ? super T, R> accumulator) {
        ObjectHelper.requireNonNull(seedSupplier, "seedSupplier is null");
        ObjectHelper.requireNonNull(accumulator, "accumulator is null");
        return RxJavaPlugins.onAssembly(new ObservableScanSeed(this, seedSupplier, accumulator));
    }

    @SchedulerSupport("none")
    public final Observable<T> serialize() {
        return RxJavaPlugins.onAssembly(new ObservableSerialized(this));
    }

    @SchedulerSupport("none")
    public final Observable<T> share() {
        return publish().refCount();
    }

    @SchedulerSupport("none")
    public final Maybe<T> singleElement() {
        return RxJavaPlugins.onAssembly(new ObservableSingleMaybe(this));
    }

    @SchedulerSupport("none")
    public final Single<T> single(T defaultItem) {
        ObjectHelper.requireNonNull(defaultItem, "defaultItem is null");
        return RxJavaPlugins.onAssembly(new ObservableSingleSingle(this, defaultItem));
    }

    @SchedulerSupport("none")
    public final Single<T> singleOrError() {
        return RxJavaPlugins.onAssembly(new ObservableSingleSingle(this, null));
    }

    @SchedulerSupport("none")
    public final Observable<T> skip(long count) {
        if (count <= 0) {
            return RxJavaPlugins.onAssembly(this);
        }
        return RxJavaPlugins.onAssembly(new ObservableSkip(this, count));
    }

    @SchedulerSupport("io.reactivex:computation")
    public final Observable<T> skip(long time, TimeUnit unit) {
        return skipUntil(timer(time, unit));
    }

    @SchedulerSupport("custom")
    public final Observable<T> skip(long time, TimeUnit unit, Scheduler scheduler) {
        return skipUntil(timer(time, unit, scheduler));
    }

    @SchedulerSupport("none")
    public final Observable<T> skipLast(int count) {
        if (count < 0) {
            throw new IndexOutOfBoundsException("count >= 0 required but it was " + count);
        } else if (count == 0) {
            return RxJavaPlugins.onAssembly(this);
        } else {
            return RxJavaPlugins.onAssembly(new ObservableSkipLast(this, count));
        }
    }

    @SchedulerSupport("io.reactivex:trampoline")
    public final Observable<T> skipLast(long time, TimeUnit unit) {
        return skipLast(time, unit, Schedulers.trampoline(), false, bufferSize());
    }

    @SchedulerSupport("io.reactivex:trampoline")
    public final Observable<T> skipLast(long time, TimeUnit unit, boolean delayError) {
        return skipLast(time, unit, Schedulers.trampoline(), delayError, bufferSize());
    }

    @SchedulerSupport("custom")
    public final Observable<T> skipLast(long time, TimeUnit unit, Scheduler scheduler) {
        return skipLast(time, unit, scheduler, false, bufferSize());
    }

    @SchedulerSupport("custom")
    public final Observable<T> skipLast(long time, TimeUnit unit, Scheduler scheduler, boolean delayError) {
        return skipLast(time, unit, scheduler, delayError, bufferSize());
    }

    @SchedulerSupport("custom")
    public final Observable<T> skipLast(long time, TimeUnit unit, Scheduler scheduler, boolean delayError, int bufferSize) {
        ObjectHelper.requireNonNull(unit, "unit is null");
        ObjectHelper.requireNonNull(scheduler, "scheduler is null");
        ObjectHelper.verifyPositive(bufferSize, "bufferSize");
        return RxJavaPlugins.onAssembly(new ObservableSkipLastTimed(this, time, unit, scheduler, bufferSize << 1, delayError));
    }

    @SchedulerSupport("none")
    public final <U> Observable<T> skipUntil(ObservableSource<U> other) {
        ObjectHelper.requireNonNull(other, "other is null");
        return RxJavaPlugins.onAssembly(new ObservableSkipUntil(this, other));
    }

    @SchedulerSupport("none")
    public final Observable<T> skipWhile(Predicate<? super T> predicate) {
        ObjectHelper.requireNonNull(predicate, "predicate is null");
        return RxJavaPlugins.onAssembly(new ObservableSkipWhile(this, predicate));
    }

    @SchedulerSupport("none")
    public final Observable<T> sorted() {
        return toList().toObservable().map(Functions.listSorter(Functions.naturalComparator())).flatMapIterable(Functions.identity());
    }

    /* JADX WARN: Type inference failed for: r3v0, types: [java.util.Comparator<? super T>, java.util.Comparator], assign insn: null */
    /* JADX WARNING: Unknown variable types count: 1 */
    @dji.thirdparty.io.reactivex.annotations.SchedulerSupport("none")
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final dji.thirdparty.io.reactivex.Observable<T> sorted(java.util.Comparator<? super T> r3) {
        /*
            r2 = this;
            dji.thirdparty.io.reactivex.Single r0 = r2.toList()
            dji.thirdparty.io.reactivex.Observable r0 = r0.toObservable()
            dji.thirdparty.io.reactivex.functions.Function r1 = dji.thirdparty.io.reactivex.internal.functions.Functions.listSorter(r3)
            dji.thirdparty.io.reactivex.Observable r0 = r0.map(r1)
            dji.thirdparty.io.reactivex.functions.Function r1 = dji.thirdparty.io.reactivex.internal.functions.Functions.identity()
            dji.thirdparty.io.reactivex.Observable r0 = r0.flatMapIterable(r1)
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: dji.thirdparty.io.reactivex.Observable.sorted(java.util.Comparator):dji.thirdparty.io.reactivex.Observable");
    }

    @SchedulerSupport("none")
    public final Observable<T> startWith(Iterable<? extends T> items) {
        return concatArray(fromIterable(items), this);
    }

    @SchedulerSupport("none")
    public final Observable<T> startWith(ObservableSource<? extends T> other) {
        ObjectHelper.requireNonNull(other, "other is null");
        return concatArray(other, this);
    }

    @SchedulerSupport("none")
    public final Observable<T> startWith(T item) {
        ObjectHelper.requireNonNull(item, "item is null");
        return concatArray(just(item), this);
    }

    @SchedulerSupport("none")
    public final Observable<T> startWithArray(T... items) {
        Observable<T> fromArray = fromArray(items);
        if (fromArray == empty()) {
            return RxJavaPlugins.onAssembly(this);
        }
        return concatArray(fromArray, this);
    }

    @SchedulerSupport("none")
    public final Disposable subscribe() {
        return subscribe(Functions.emptyConsumer(), Functions.ERROR_CONSUMER, Functions.EMPTY_ACTION, Functions.emptyConsumer());
    }

    @SchedulerSupport("none")
    public final Disposable subscribe(Consumer<? super Long> consumer) {
        return subscribe(consumer, Functions.ERROR_CONSUMER, Functions.EMPTY_ACTION, Functions.emptyConsumer());
    }

    @SchedulerSupport("none")
    public final Disposable subscribe(Consumer<? super T> onNext, Consumer<? super Throwable> onError) {
        return subscribe(onNext, onError, Functions.EMPTY_ACTION, Functions.emptyConsumer());
    }

    @SchedulerSupport("none")
    public final Disposable subscribe(Consumer<? super T> onNext, Consumer<? super Throwable> onError, Action onComplete) {
        return subscribe(onNext, onError, onComplete, Functions.emptyConsumer());
    }

    @SchedulerSupport("none")
    public final Disposable subscribe(Consumer<? super T> onNext, Consumer<? super Throwable> onError, Action onComplete, Consumer<? super Disposable> onSubscribe) {
        ObjectHelper.requireNonNull(onNext, "onNext is null");
        ObjectHelper.requireNonNull(onError, "onError is null");
        ObjectHelper.requireNonNull(onComplete, "onComplete is null");
        ObjectHelper.requireNonNull(onSubscribe, "onSubscribe is null");
        LambdaObserver<T> ls = new LambdaObserver<>(onNext, onError, onComplete, onSubscribe);
        subscribe(ls);
        return ls;
    }

    @SchedulerSupport("none")
    public final void subscribe(Observer<? super Long> observer) {
        ObjectHelper.requireNonNull(observer, "observer is null");
        try {
            Observer<? super T> observer2 = RxJavaPlugins.onSubscribe(this, observer);
            ObjectHelper.requireNonNull(observer2, "Plugin returned null Observer");
            subscribeActual(observer2);
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

    @SchedulerSupport("none")
    public final <E extends Observer<? super T>> E subscribeWith(E observer) {
        subscribe((Observer<? super Long>) observer);
        return observer;
    }

    @SchedulerSupport("custom")
    public final Observable<T> subscribeOn(Scheduler scheduler) {
        ObjectHelper.requireNonNull(scheduler, "scheduler is null");
        return RxJavaPlugins.onAssembly(new ObservableSubscribeOn(this, scheduler));
    }

    @SchedulerSupport("none")
    public final Observable<T> switchIfEmpty(ObservableSource<? extends T> other) {
        ObjectHelper.requireNonNull(other, "other is null");
        return RxJavaPlugins.onAssembly(new ObservableSwitchIfEmpty(this, other));
    }

    @SchedulerSupport("none")
    public final <R> Observable<R> switchMap(Function<? super T, ? extends ObservableSource<? extends R>> mapper) {
        return switchMap(mapper, bufferSize());
    }

    /* JADX WARN: Type inference failed for: r4v0, types: [dji.thirdparty.io.reactivex.functions.Function, java.lang.Object, dji.thirdparty.io.reactivex.functions.Function<? super T, ? extends dji.thirdparty.io.reactivex.ObservableSource<? extends R>>], assign insn: null */
    /* JADX WARNING: Unknown variable types count: 1 */
    @dji.thirdparty.io.reactivex.annotations.SchedulerSupport("none")
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final <R> dji.thirdparty.io.reactivex.Observable<R> switchMap(dji.thirdparty.io.reactivex.functions.Function<? super T, ? extends dji.thirdparty.io.reactivex.ObservableSource<? extends R>> r4, int r5) {
        /*
            r3 = this;
            java.lang.String r1 = "mapper is null"
            dji.thirdparty.io.reactivex.internal.functions.ObjectHelper.requireNonNull(r4, r1)
            java.lang.String r1 = "bufferSize"
            dji.thirdparty.io.reactivex.internal.functions.ObjectHelper.verifyPositive(r5, r1)
            boolean r1 = r3 instanceof dji.thirdparty.io.reactivex.internal.fuseable.ScalarCallable
            if (r1 == 0) goto L_0x0022
            dji.thirdparty.io.reactivex.internal.fuseable.ScalarCallable r3 = (dji.thirdparty.io.reactivex.internal.fuseable.ScalarCallable) r3
            java.lang.Object r0 = r3.call()
            if (r0 != 0) goto L_0x001d
            dji.thirdparty.io.reactivex.Observable r1 = empty()
        L_0x001c:
            return r1
        L_0x001d:
            dji.thirdparty.io.reactivex.Observable r1 = dji.thirdparty.io.reactivex.internal.operators.observable.ObservableScalarXMap.scalarXMap(r0, r4)
            goto L_0x001c
        L_0x0022:
            dji.thirdparty.io.reactivex.internal.operators.observable.ObservableSwitchMap r1 = new dji.thirdparty.io.reactivex.internal.operators.observable.ObservableSwitchMap
            r2 = 0
            r1.<init>(r3, r4, r5, r2)
            dji.thirdparty.io.reactivex.Observable r1 = dji.thirdparty.io.reactivex.plugins.RxJavaPlugins.onAssembly(r1)
            goto L_0x001c
        */
        throw new UnsupportedOperationException("Method not decompiled: dji.thirdparty.io.reactivex.Observable.switchMap(dji.thirdparty.io.reactivex.functions.Function, int):dji.thirdparty.io.reactivex.Observable");
    }

    @SchedulerSupport("none")
    public final <R> Observable<R> switchMapDelayError(Function<? super T, ? extends ObservableSource<? extends R>> mapper) {
        return switchMapDelayError(mapper, bufferSize());
    }

    /* JADX WARN: Type inference failed for: r4v0, types: [dji.thirdparty.io.reactivex.functions.Function, java.lang.Object, dji.thirdparty.io.reactivex.functions.Function<? super T, ? extends dji.thirdparty.io.reactivex.ObservableSource<? extends R>>], assign insn: null */
    /* JADX WARNING: Unknown variable types count: 1 */
    @dji.thirdparty.io.reactivex.annotations.SchedulerSupport("none")
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final <R> dji.thirdparty.io.reactivex.Observable<R> switchMapDelayError(dji.thirdparty.io.reactivex.functions.Function<? super T, ? extends dji.thirdparty.io.reactivex.ObservableSource<? extends R>> r4, int r5) {
        /*
            r3 = this;
            java.lang.String r1 = "mapper is null"
            dji.thirdparty.io.reactivex.internal.functions.ObjectHelper.requireNonNull(r4, r1)
            java.lang.String r1 = "bufferSize"
            dji.thirdparty.io.reactivex.internal.functions.ObjectHelper.verifyPositive(r5, r1)
            boolean r1 = r3 instanceof dji.thirdparty.io.reactivex.internal.fuseable.ScalarCallable
            if (r1 == 0) goto L_0x0022
            dji.thirdparty.io.reactivex.internal.fuseable.ScalarCallable r3 = (dji.thirdparty.io.reactivex.internal.fuseable.ScalarCallable) r3
            java.lang.Object r0 = r3.call()
            if (r0 != 0) goto L_0x001d
            dji.thirdparty.io.reactivex.Observable r1 = empty()
        L_0x001c:
            return r1
        L_0x001d:
            dji.thirdparty.io.reactivex.Observable r1 = dji.thirdparty.io.reactivex.internal.operators.observable.ObservableScalarXMap.scalarXMap(r0, r4)
            goto L_0x001c
        L_0x0022:
            dji.thirdparty.io.reactivex.internal.operators.observable.ObservableSwitchMap r1 = new dji.thirdparty.io.reactivex.internal.operators.observable.ObservableSwitchMap
            r2 = 1
            r1.<init>(r3, r4, r5, r2)
            dji.thirdparty.io.reactivex.Observable r1 = dji.thirdparty.io.reactivex.plugins.RxJavaPlugins.onAssembly(r1)
            goto L_0x001c
        */
        throw new UnsupportedOperationException("Method not decompiled: dji.thirdparty.io.reactivex.Observable.switchMapDelayError(dji.thirdparty.io.reactivex.functions.Function, int):dji.thirdparty.io.reactivex.Observable");
    }

    @SchedulerSupport("none")
    public final Observable<T> take(long count) {
        if (count >= 0) {
            return RxJavaPlugins.onAssembly(new ObservableTake(this, count));
        }
        throw new IllegalArgumentException("count >= 0 required but it was " + count);
    }

    @SchedulerSupport("none")
    public final Observable<T> take(long time, TimeUnit unit) {
        return takeUntil(timer(time, unit));
    }

    @SchedulerSupport("custom")
    public final Observable<T> take(long time, TimeUnit unit, Scheduler scheduler) {
        return takeUntil(timer(time, unit, scheduler));
    }

    @SchedulerSupport("none")
    public final Observable<T> takeLast(int count) {
        if (count < 0) {
            throw new IndexOutOfBoundsException("count >= 0 required but it was " + count);
        } else if (count == 0) {
            return RxJavaPlugins.onAssembly(new ObservableIgnoreElements(this));
        } else {
            if (count == 1) {
                return RxJavaPlugins.onAssembly(new ObservableTakeLastOne(this));
            }
            return RxJavaPlugins.onAssembly(new ObservableTakeLast(this, count));
        }
    }

    @SchedulerSupport("io.reactivex:trampoline")
    public final Observable<T> takeLast(long count, long time, TimeUnit unit) {
        return takeLast(count, time, unit, Schedulers.trampoline(), false, bufferSize());
    }

    @SchedulerSupport("custom")
    public final Observable<T> takeLast(long count, long time, TimeUnit unit, Scheduler scheduler) {
        return takeLast(count, time, unit, scheduler, false, bufferSize());
    }

    @SchedulerSupport("custom")
    public final Observable<T> takeLast(long count, long time, TimeUnit unit, Scheduler scheduler, boolean delayError, int bufferSize) {
        ObjectHelper.requireNonNull(unit, "unit is null");
        ObjectHelper.requireNonNull(scheduler, "scheduler is null");
        ObjectHelper.verifyPositive(bufferSize, "bufferSize");
        if (count >= 0) {
            return RxJavaPlugins.onAssembly(new ObservableTakeLastTimed(this, count, time, unit, scheduler, bufferSize, delayError));
        }
        throw new IndexOutOfBoundsException("count >= 0 required but it was " + count);
    }

    @SchedulerSupport("io.reactivex:trampoline")
    public final Observable<T> takeLast(long time, TimeUnit unit) {
        return takeLast(time, unit, Schedulers.trampoline(), false, bufferSize());
    }

    @SchedulerSupport("io.reactivex:trampoline")
    public final Observable<T> takeLast(long time, TimeUnit unit, boolean delayError) {
        return takeLast(time, unit, Schedulers.trampoline(), delayError, bufferSize());
    }

    @SchedulerSupport("custom")
    public final Observable<T> takeLast(long time, TimeUnit unit, Scheduler scheduler) {
        return takeLast(time, unit, scheduler, false, bufferSize());
    }

    @SchedulerSupport("custom")
    public final Observable<T> takeLast(long time, TimeUnit unit, Scheduler scheduler, boolean delayError) {
        return takeLast(time, unit, scheduler, delayError, bufferSize());
    }

    @SchedulerSupport("custom")
    public final Observable<T> takeLast(long time, TimeUnit unit, Scheduler scheduler, boolean delayError, int bufferSize) {
        return takeLast(LongCompanionObject.MAX_VALUE, time, unit, scheduler, delayError, bufferSize);
    }

    @SchedulerSupport("none")
    public final <U> Observable<T> takeUntil(ObservableSource<U> other) {
        ObjectHelper.requireNonNull(other, "other is null");
        return RxJavaPlugins.onAssembly(new ObservableTakeUntil(this, other));
    }

    @SchedulerSupport("none")
    public final Observable<T> takeUntil(Predicate<? super T> stopPredicate) {
        ObjectHelper.requireNonNull(stopPredicate, "predicate is null");
        return RxJavaPlugins.onAssembly(new ObservableTakeUntilPredicate(this, stopPredicate));
    }

    @SchedulerSupport("none")
    public final Observable<T> takeWhile(Predicate<? super T> predicate) {
        ObjectHelper.requireNonNull(predicate, "predicate is null");
        return RxJavaPlugins.onAssembly(new ObservableTakeWhile(this, predicate));
    }

    @SchedulerSupport("io.reactivex:computation")
    public final Observable<T> throttleFirst(long windowDuration, TimeUnit unit) {
        return throttleFirst(windowDuration, unit, Schedulers.computation());
    }

    @SchedulerSupport("custom")
    public final Observable<T> throttleFirst(long skipDuration, TimeUnit unit, Scheduler scheduler) {
        ObjectHelper.requireNonNull(unit, "unit is null");
        ObjectHelper.requireNonNull(scheduler, "scheduler is null");
        return RxJavaPlugins.onAssembly(new ObservableThrottleFirstTimed(this, skipDuration, unit, scheduler));
    }

    @SchedulerSupport("io.reactivex:computation")
    public final Observable<T> throttleLast(long intervalDuration, TimeUnit unit) {
        return sample(intervalDuration, unit);
    }

    @SchedulerSupport("custom")
    public final Observable<T> throttleLast(long intervalDuration, TimeUnit unit, Scheduler scheduler) {
        return sample(intervalDuration, unit, scheduler);
    }

    @SchedulerSupport("io.reactivex:computation")
    public final Observable<T> throttleWithTimeout(long timeout, TimeUnit unit) {
        return debounce(timeout, unit);
    }

    @SchedulerSupport("custom")
    public final Observable<T> throttleWithTimeout(long timeout, TimeUnit unit, Scheduler scheduler) {
        return debounce(timeout, unit, scheduler);
    }

    @SchedulerSupport("none")
    public final Observable<Timed<T>> timeInterval() {
        return timeInterval(TimeUnit.MILLISECONDS, Schedulers.computation());
    }

    @SchedulerSupport("none")
    public final Observable<Timed<T>> timeInterval(Scheduler scheduler) {
        return timeInterval(TimeUnit.MILLISECONDS, scheduler);
    }

    @SchedulerSupport("none")
    public final Observable<Timed<T>> timeInterval(TimeUnit unit) {
        return timeInterval(unit, Schedulers.computation());
    }

    @SchedulerSupport("none")
    public final Observable<Timed<T>> timeInterval(TimeUnit unit, Scheduler scheduler) {
        ObjectHelper.requireNonNull(unit, "unit is null");
        ObjectHelper.requireNonNull(scheduler, "scheduler is null");
        return RxJavaPlugins.onAssembly(new ObservableTimeInterval(this, unit, scheduler));
    }

    @SchedulerSupport("none")
    public final <V> Observable<T> timeout(Function<? super T, ? extends ObservableSource<V>> itemTimeoutIndicator) {
        return timeout0(null, itemTimeoutIndicator, null);
    }

    @SchedulerSupport("none")
    public final <V> Observable<T> timeout(Function<? super T, ? extends ObservableSource<V>> itemTimeoutIndicator, ObservableSource<? extends T> other) {
        ObjectHelper.requireNonNull(other, "other is null");
        return timeout0(null, itemTimeoutIndicator, other);
    }

    @SchedulerSupport("io.reactivex:computation")
    public final Observable<T> timeout(long timeout, TimeUnit timeUnit) {
        return timeout0(timeout, timeUnit, null, Schedulers.computation());
    }

    @SchedulerSupport("io.reactivex:computation")
    public final Observable<T> timeout(long timeout, TimeUnit timeUnit, ObservableSource<? extends T> other) {
        ObjectHelper.requireNonNull(other, "other is null");
        return timeout0(timeout, timeUnit, other, Schedulers.computation());
    }

    @SchedulerSupport("custom")
    public final Observable<T> timeout(long timeout, TimeUnit timeUnit, Scheduler scheduler, ObservableSource<? extends T> other) {
        ObjectHelper.requireNonNull(other, "other is null");
        return timeout0(timeout, timeUnit, other, scheduler);
    }

    @SchedulerSupport("custom")
    public final Observable<T> timeout(long timeout, TimeUnit timeUnit, Scheduler scheduler) {
        return timeout0(timeout, timeUnit, null, scheduler);
    }

    /* JADX WARN: Type inference failed for: r2v0, types: [dji.thirdparty.io.reactivex.ObservableSource, dji.thirdparty.io.reactivex.ObservableSource<U>, java.lang.Object], assign insn: null */
    /* JADX WARNING: Unknown variable types count: 1 */
    @dji.thirdparty.io.reactivex.annotations.SchedulerSupport("none")
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final <U, V> dji.thirdparty.io.reactivex.Observable<T> timeout(dji.thirdparty.io.reactivex.ObservableSource<U> r2, dji.thirdparty.io.reactivex.functions.Function<? super T, ? extends dji.thirdparty.io.reactivex.ObservableSource<V>> r3) {
        /*
            r1 = this;
            java.lang.String r0 = "firstTimeoutIndicator is null"
            dji.thirdparty.io.reactivex.internal.functions.ObjectHelper.requireNonNull(r2, r0)
            r0 = 0
            dji.thirdparty.io.reactivex.Observable r0 = r1.timeout0(r2, r3, r0)
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: dji.thirdparty.io.reactivex.Observable.timeout(dji.thirdparty.io.reactivex.ObservableSource, dji.thirdparty.io.reactivex.functions.Function):dji.thirdparty.io.reactivex.Observable");
    }

    /* JADX WARN: Type inference failed for: r2v0, types: [dji.thirdparty.io.reactivex.ObservableSource, dji.thirdparty.io.reactivex.ObservableSource<U>, java.lang.Object], assign insn: null */
    /* JADX WARNING: Unknown variable types count: 1 */
    @dji.thirdparty.io.reactivex.annotations.SchedulerSupport("none")
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final <U, V> dji.thirdparty.io.reactivex.Observable<T> timeout(dji.thirdparty.io.reactivex.ObservableSource<U> r2, dji.thirdparty.io.reactivex.functions.Function<? super T, ? extends dji.thirdparty.io.reactivex.ObservableSource<V>> r3, dji.thirdparty.io.reactivex.ObservableSource<? extends T> r4) {
        /*
            r1 = this;
            java.lang.String r0 = "firstTimeoutIndicator is null"
            dji.thirdparty.io.reactivex.internal.functions.ObjectHelper.requireNonNull(r2, r0)
            java.lang.String r0 = "other is null"
            dji.thirdparty.io.reactivex.internal.functions.ObjectHelper.requireNonNull(r4, r0)
            dji.thirdparty.io.reactivex.Observable r0 = r1.timeout0(r2, r3, r4)
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: dji.thirdparty.io.reactivex.Observable.timeout(dji.thirdparty.io.reactivex.ObservableSource, dji.thirdparty.io.reactivex.functions.Function, dji.thirdparty.io.reactivex.ObservableSource):dji.thirdparty.io.reactivex.Observable");
    }

    private Observable<T> timeout0(long timeout, TimeUnit timeUnit, ObservableSource<? extends T> other, Scheduler scheduler) {
        ObjectHelper.requireNonNull(timeUnit, "timeUnit is null");
        ObjectHelper.requireNonNull(scheduler, "scheduler is null");
        return RxJavaPlugins.onAssembly(new ObservableTimeoutTimed(this, timeout, timeUnit, scheduler, other));
    }

    private <U, V> Observable<T> timeout0(ObservableSource<U> firstTimeoutIndicator, Function<? super T, ? extends ObservableSource<V>> itemTimeoutIndicator, ObservableSource<? extends T> other) {
        ObjectHelper.requireNonNull(itemTimeoutIndicator, "itemTimeoutIndicator is null");
        return RxJavaPlugins.onAssembly(new ObservableTimeout(this, firstTimeoutIndicator, itemTimeoutIndicator, other));
    }

    @SchedulerSupport("none")
    public final Observable<Timed<T>> timestamp() {
        return timestamp(TimeUnit.MILLISECONDS, Schedulers.computation());
    }

    @SchedulerSupport("none")
    public final Observable<Timed<T>> timestamp(Scheduler scheduler) {
        return timestamp(TimeUnit.MILLISECONDS, scheduler);
    }

    @SchedulerSupport("none")
    public final Observable<Timed<T>> timestamp(TimeUnit unit) {
        return timestamp(unit, Schedulers.computation());
    }

    @SchedulerSupport("none")
    public final Observable<Timed<T>> timestamp(TimeUnit unit, Scheduler scheduler) {
        ObjectHelper.requireNonNull(unit, "unit is null");
        ObjectHelper.requireNonNull(scheduler, "scheduler is null");
        return map(Functions.timestampWith(unit, scheduler));
    }

    @SchedulerSupport("none")
    public final <R> R to(Function<? super Observable<T>, R> converter) {
        try {
            return converter.apply(this);
        } catch (Throwable ex) {
            Exceptions.throwIfFatal(ex);
            throw ExceptionHelper.wrapOrThrow(ex);
        }
    }

    @SchedulerSupport("none")
    public final Single<List<T>> toList() {
        return toList(16);
    }

    @SchedulerSupport("none")
    public final Single<List<T>> toList(int capacityHint) {
        ObjectHelper.verifyPositive(capacityHint, "capacityHint");
        return RxJavaPlugins.onAssembly(new ObservableToListSingle(this, capacityHint));
    }

    @SchedulerSupport("none")
    public final <U extends Collection<? super T>> Single<U> toList(Callable<U> collectionSupplier) {
        ObjectHelper.requireNonNull(collectionSupplier, "collectionSupplier is null");
        return RxJavaPlugins.onAssembly(new ObservableToListSingle(this, collectionSupplier));
    }

    @SchedulerSupport("none")
    public final <K> Single<Map<K, T>> toMap(Function<? super T, ? extends K> keySelector) {
        return collect(HashMapSupplier.asCallable(), Functions.toMapKeySelector(keySelector));
    }

    @SchedulerSupport("none")
    public final <K, V> Single<Map<K, V>> toMap(Function<? super T, ? extends K> keySelector, Function<? super T, ? extends V> valueSelector) {
        ObjectHelper.requireNonNull(keySelector, "keySelector is null");
        ObjectHelper.requireNonNull(valueSelector, "valueSelector is null");
        return collect(HashMapSupplier.asCallable(), Functions.toMapKeyValueSelector(keySelector, valueSelector));
    }

    @SchedulerSupport("none")
    public final <K, V> Single<Map<K, V>> toMap(Function<? super T, ? extends K> keySelector, Function<? super T, ? extends V> valueSelector, Callable<? extends Map<K, V>> mapSupplier) {
        return collect(mapSupplier, Functions.toMapKeyValueSelector(keySelector, valueSelector));
    }

    /* JADX WARN: Type inference failed for: r5v0, types: [dji.thirdparty.io.reactivex.functions.Function, dji.thirdparty.io.reactivex.functions.Function<? super T, ? extends K>], assign insn: null */
    /* JADX WARNING: Unknown variable types count: 1 */
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
        throw new UnsupportedOperationException("Method not decompiled: dji.thirdparty.io.reactivex.Observable.toMultimap(dji.thirdparty.io.reactivex.functions.Function):dji.thirdparty.io.reactivex.Single");
    }

    @SchedulerSupport("none")
    public final <K, V> Single<Map<K, Collection<V>>> toMultimap(Function<? super T, ? extends K> keySelector, Function<? super T, ? extends V> valueSelector) {
        return toMultimap(keySelector, valueSelector, HashMapSupplier.asCallable(), ArrayListSupplier.asFunction());
    }

    @SchedulerSupport("none")
    public final <K, V> Single<Map<K, Collection<V>>> toMultimap(Function<? super T, ? extends K> keySelector, Function<? super T, ? extends V> valueSelector, Callable<? extends Map<K, Collection<V>>> mapSupplier, Function<? super K, ? extends Collection<? super V>> collectionFactory) {
        ObjectHelper.requireNonNull(keySelector, "keySelector is null");
        ObjectHelper.requireNonNull(valueSelector, "valueSelector is null");
        ObjectHelper.requireNonNull(mapSupplier, "mapSupplier is null");
        ObjectHelper.requireNonNull(collectionFactory, "collectionFactory is null");
        return collect(mapSupplier, Functions.toMultimapKeyValueSelector(keySelector, valueSelector, collectionFactory));
    }

    @SchedulerSupport("none")
    public final <K, V> Single<Map<K, Collection<V>>> toMultimap(Function<? super T, ? extends K> keySelector, Function<? super T, ? extends V> valueSelector, Callable<Map<K, Collection<V>>> mapSupplier) {
        return toMultimap(keySelector, valueSelector, mapSupplier, ArrayListSupplier.asFunction());
    }

    @BackpressureSupport(BackpressureKind.SPECIAL)
    @SchedulerSupport("none")
    public final Flowable<T> toFlowable(BackpressureStrategy strategy) {
        Flowable<T> o = new FlowableFromObservable<>(this);
        switch (strategy) {
            case DROP:
                return o.onBackpressureDrop();
            case LATEST:
                return o.onBackpressureLatest();
            case MISSING:
                return o;
            case ERROR:
                return RxJavaPlugins.onAssembly(new FlowableOnBackpressureError(o));
            default:
                return o.onBackpressureBuffer();
        }
    }

    @SchedulerSupport("none")
    public final Single<List<T>> toSortedList() {
        return toSortedList(Functions.naturalOrder());
    }

    @SchedulerSupport("none")
    public final Single<List<T>> toSortedList(Comparator<? super T> comparator) {
        ObjectHelper.requireNonNull(comparator, "comparator is null");
        return toList().map(Functions.listSorter(comparator));
    }

    @SchedulerSupport("none")
    public final Single<List<T>> toSortedList(Comparator<? super T> comparator, int capacityHint) {
        ObjectHelper.requireNonNull(comparator, "comparator is null");
        return toList(capacityHint).map(Functions.listSorter(comparator));
    }

    @SchedulerSupport("none")
    public final Single<List<T>> toSortedList(int capacityHint) {
        return toSortedList(Functions.naturalOrder(), capacityHint);
    }

    @SchedulerSupport("custom")
    public final Observable<T> unsubscribeOn(Scheduler scheduler) {
        ObjectHelper.requireNonNull(scheduler, "scheduler is null");
        return RxJavaPlugins.onAssembly(new ObservableUnsubscribeOn(this, scheduler));
    }

    @SchedulerSupport("none")
    public final Observable<Observable<T>> window(long count) {
        return window(count, count, bufferSize());
    }

    @SchedulerSupport("none")
    public final Observable<Observable<T>> window(long count, long skip) {
        return window(count, skip, bufferSize());
    }

    @SchedulerSupport("none")
    public final Observable<Observable<T>> window(long count, long skip, int bufferSize) {
        ObjectHelper.verifyPositive(count, "count");
        ObjectHelper.verifyPositive(skip, "skip");
        ObjectHelper.verifyPositive(bufferSize, "bufferSize");
        return RxJavaPlugins.onAssembly(new ObservableWindow(this, count, skip, bufferSize));
    }

    @SchedulerSupport("io.reactivex:computation")
    public final Observable<Observable<T>> window(long timespan, long timeskip, TimeUnit unit) {
        return window(timespan, timeskip, unit, Schedulers.computation(), bufferSize());
    }

    @SchedulerSupport("custom")
    public final Observable<Observable<T>> window(long timespan, long timeskip, TimeUnit unit, Scheduler scheduler) {
        return window(timespan, timeskip, unit, scheduler, bufferSize());
    }

    @SchedulerSupport("custom")
    public final Observable<Observable<T>> window(long timespan, long timeskip, TimeUnit unit, Scheduler scheduler, int bufferSize) {
        ObjectHelper.verifyPositive(timespan, "timespan");
        ObjectHelper.verifyPositive(timeskip, "timeskip");
        ObjectHelper.verifyPositive(bufferSize, "bufferSize");
        ObjectHelper.requireNonNull(scheduler, "scheduler is null");
        ObjectHelper.requireNonNull(unit, "unit is null");
        return RxJavaPlugins.onAssembly(new ObservableWindowTimed(this, timespan, timeskip, unit, scheduler, LongCompanionObject.MAX_VALUE, bufferSize, false));
    }

    /* JADX DEBUG: Failed to find minimal casts for resolve overloaded methods, cast all args instead
     method: dji.thirdparty.io.reactivex.Observable.window(long, java.util.concurrent.TimeUnit, dji.thirdparty.io.reactivex.Scheduler, long, boolean):dji.thirdparty.io.reactivex.Observable<dji.thirdparty.io.reactivex.Observable<T>>
     arg types: [long, java.util.concurrent.TimeUnit, dji.thirdparty.io.reactivex.Scheduler, ?, int]
     candidates:
      dji.thirdparty.io.reactivex.Observable.window(long, long, java.util.concurrent.TimeUnit, dji.thirdparty.io.reactivex.Scheduler, int):dji.thirdparty.io.reactivex.Observable<dji.thirdparty.io.reactivex.Observable<T>>
      dji.thirdparty.io.reactivex.Observable.window(long, java.util.concurrent.TimeUnit, dji.thirdparty.io.reactivex.Scheduler, long, boolean):dji.thirdparty.io.reactivex.Observable<dji.thirdparty.io.reactivex.Observable<T>> */
    @SchedulerSupport("io.reactivex:computation")
    public final Observable<Observable<T>> window(long timespan, TimeUnit unit) {
        return window(timespan, unit, Schedulers.computation(), (long) LongCompanionObject.MAX_VALUE, false);
    }

    /* JADX DEBUG: Failed to find minimal casts for resolve overloaded methods, cast all args instead
     method: dji.thirdparty.io.reactivex.Observable.window(long, java.util.concurrent.TimeUnit, dji.thirdparty.io.reactivex.Scheduler, long, boolean):dji.thirdparty.io.reactivex.Observable<dji.thirdparty.io.reactivex.Observable<T>>
     arg types: [long, java.util.concurrent.TimeUnit, dji.thirdparty.io.reactivex.Scheduler, long, int]
     candidates:
      dji.thirdparty.io.reactivex.Observable.window(long, long, java.util.concurrent.TimeUnit, dji.thirdparty.io.reactivex.Scheduler, int):dji.thirdparty.io.reactivex.Observable<dji.thirdparty.io.reactivex.Observable<T>>
      dji.thirdparty.io.reactivex.Observable.window(long, java.util.concurrent.TimeUnit, dji.thirdparty.io.reactivex.Scheduler, long, boolean):dji.thirdparty.io.reactivex.Observable<dji.thirdparty.io.reactivex.Observable<T>> */
    @SchedulerSupport("io.reactivex:computation")
    public final Observable<Observable<T>> window(long timespan, TimeUnit unit, long count) {
        return window(timespan, unit, Schedulers.computation(), count, false);
    }

    @SchedulerSupport("io.reactivex:computation")
    public final Observable<Observable<T>> window(long timespan, TimeUnit unit, long count, boolean restart) {
        return window(timespan, unit, Schedulers.computation(), count, restart);
    }

    /* JADX DEBUG: Failed to find minimal casts for resolve overloaded methods, cast all args instead
     method: dji.thirdparty.io.reactivex.Observable.window(long, java.util.concurrent.TimeUnit, dji.thirdparty.io.reactivex.Scheduler, long, boolean):dji.thirdparty.io.reactivex.Observable<dji.thirdparty.io.reactivex.Observable<T>>
     arg types: [long, java.util.concurrent.TimeUnit, dji.thirdparty.io.reactivex.Scheduler, ?, int]
     candidates:
      dji.thirdparty.io.reactivex.Observable.window(long, long, java.util.concurrent.TimeUnit, dji.thirdparty.io.reactivex.Scheduler, int):dji.thirdparty.io.reactivex.Observable<dji.thirdparty.io.reactivex.Observable<T>>
      dji.thirdparty.io.reactivex.Observable.window(long, java.util.concurrent.TimeUnit, dji.thirdparty.io.reactivex.Scheduler, long, boolean):dji.thirdparty.io.reactivex.Observable<dji.thirdparty.io.reactivex.Observable<T>> */
    @SchedulerSupport("custom")
    public final Observable<Observable<T>> window(long timespan, TimeUnit unit, Scheduler scheduler) {
        return window(timespan, unit, scheduler, (long) LongCompanionObject.MAX_VALUE, false);
    }

    /* JADX DEBUG: Failed to find minimal casts for resolve overloaded methods, cast all args instead
     method: dji.thirdparty.io.reactivex.Observable.window(long, java.util.concurrent.TimeUnit, dji.thirdparty.io.reactivex.Scheduler, long, boolean):dji.thirdparty.io.reactivex.Observable<dji.thirdparty.io.reactivex.Observable<T>>
     arg types: [long, java.util.concurrent.TimeUnit, dji.thirdparty.io.reactivex.Scheduler, long, int]
     candidates:
      dji.thirdparty.io.reactivex.Observable.window(long, long, java.util.concurrent.TimeUnit, dji.thirdparty.io.reactivex.Scheduler, int):dji.thirdparty.io.reactivex.Observable<dji.thirdparty.io.reactivex.Observable<T>>
      dji.thirdparty.io.reactivex.Observable.window(long, java.util.concurrent.TimeUnit, dji.thirdparty.io.reactivex.Scheduler, long, boolean):dji.thirdparty.io.reactivex.Observable<dji.thirdparty.io.reactivex.Observable<T>> */
    @SchedulerSupport("custom")
    public final Observable<Observable<T>> window(long timespan, TimeUnit unit, Scheduler scheduler, long count) {
        return window(timespan, unit, scheduler, count, false);
    }

    @SchedulerSupport("custom")
    public final Observable<Observable<T>> window(long timespan, TimeUnit unit, Scheduler scheduler, long count, boolean restart) {
        return window(timespan, unit, scheduler, count, restart, bufferSize());
    }

    @SchedulerSupport("custom")
    public final Observable<Observable<T>> window(long timespan, TimeUnit unit, Scheduler scheduler, long count, boolean restart, int bufferSize) {
        ObjectHelper.verifyPositive(bufferSize, "bufferSize");
        ObjectHelper.requireNonNull(scheduler, "scheduler is null");
        ObjectHelper.requireNonNull(unit, "unit is null");
        ObjectHelper.verifyPositive(count, "count");
        return RxJavaPlugins.onAssembly(new ObservableWindowTimed(this, timespan, timespan, unit, scheduler, count, bufferSize, restart));
    }

    /* JADX WARN: Type inference failed for: r2v0, types: [dji.thirdparty.io.reactivex.ObservableSource, dji.thirdparty.io.reactivex.ObservableSource<B>], assign insn: null */
    /* JADX WARNING: Unknown variable types count: 1 */
    @dji.thirdparty.io.reactivex.annotations.SchedulerSupport("none")
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final <B> dji.thirdparty.io.reactivex.Observable<dji.thirdparty.io.reactivex.Observable<T>> window(dji.thirdparty.io.reactivex.ObservableSource<B> r2) {
        /*
            r1 = this;
            int r0 = bufferSize()
            dji.thirdparty.io.reactivex.Observable r0 = r1.window(r2, r0)
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: dji.thirdparty.io.reactivex.Observable.window(dji.thirdparty.io.reactivex.ObservableSource):dji.thirdparty.io.reactivex.Observable");
    }

    @SchedulerSupport("none")
    public final <B> Observable<Observable<T>> window(ObservableSource<B> boundary, int bufferSize) {
        ObjectHelper.requireNonNull(boundary, "boundary is null");
        return RxJavaPlugins.onAssembly(new ObservableWindowBoundary(this, boundary, bufferSize));
    }

    /* JADX WARN: Type inference failed for: r2v0, types: [dji.thirdparty.io.reactivex.ObservableSource, dji.thirdparty.io.reactivex.ObservableSource<U>], assign insn: null */
    /* JADX WARNING: Unknown variable types count: 1 */
    @dji.thirdparty.io.reactivex.annotations.SchedulerSupport("none")
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final <U, V> dji.thirdparty.io.reactivex.Observable<dji.thirdparty.io.reactivex.Observable<T>> window(dji.thirdparty.io.reactivex.ObservableSource<U> r2, dji.thirdparty.io.reactivex.functions.Function<? super U, ? extends dji.thirdparty.io.reactivex.ObservableSource<V>> r3) {
        /*
            r1 = this;
            int r0 = bufferSize()
            dji.thirdparty.io.reactivex.Observable r0 = r1.window(r2, r3, r0)
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: dji.thirdparty.io.reactivex.Observable.window(dji.thirdparty.io.reactivex.ObservableSource, dji.thirdparty.io.reactivex.functions.Function):dji.thirdparty.io.reactivex.Observable");
    }

    @SchedulerSupport("none")
    public final <U, V> Observable<Observable<T>> window(ObservableSource<U> openingIndicator, Function<? super U, ? extends ObservableSource<V>> closingIndicator, int bufferSize) {
        ObjectHelper.requireNonNull(openingIndicator, "openingIndicator is null");
        ObjectHelper.requireNonNull(closingIndicator, "closingIndicator is null");
        return RxJavaPlugins.onAssembly(new ObservableWindowBoundarySelector(this, openingIndicator, closingIndicator, bufferSize));
    }

    @SchedulerSupport("none")
    public final <B> Observable<Observable<T>> window(Callable<? extends ObservableSource<B>> boundary) {
        return window(boundary, bufferSize());
    }

    @SchedulerSupport("none")
    public final <B> Observable<Observable<T>> window(Callable<? extends ObservableSource<B>> boundary, int bufferSize) {
        ObjectHelper.requireNonNull(boundary, "boundary is null");
        return RxJavaPlugins.onAssembly(new ObservableWindowBoundarySupplier(this, boundary, bufferSize));
    }

    @SchedulerSupport("none")
    public final <U, R> Observable<R> withLatestFrom(ObservableSource<? extends U> other, BiFunction<? super T, ? super U, ? extends R> combiner) {
        ObjectHelper.requireNonNull(other, "other is null");
        ObjectHelper.requireNonNull(combiner, "combiner is null");
        return RxJavaPlugins.onAssembly(new ObservableWithLatestFrom(this, combiner, other));
    }

    @SchedulerSupport("none")
    public final <T1, T2, R> Observable<R> withLatestFrom(ObservableSource<T1> o1, ObservableSource<T2> o2, Function3<? super T, ? super T1, ? super T2, R> combiner) {
        ObjectHelper.requireNonNull(o1, "o1 is null");
        ObjectHelper.requireNonNull(o2, "o2 is null");
        ObjectHelper.requireNonNull(combiner, "combiner is null");
        return withLatestFrom(new ObservableSource[]{o1, o2}, Functions.toFunction(combiner));
    }

    @SchedulerSupport("none")
    public final <T1, T2, T3, R> Observable<R> withLatestFrom(ObservableSource<T1> o1, ObservableSource<T2> o2, ObservableSource<T3> o3, Function4<? super T, ? super T1, ? super T2, ? super T3, R> combiner) {
        ObjectHelper.requireNonNull(o1, "o1 is null");
        ObjectHelper.requireNonNull(o2, "o2 is null");
        ObjectHelper.requireNonNull(o3, "o3 is null");
        ObjectHelper.requireNonNull(combiner, "combiner is null");
        return withLatestFrom(new ObservableSource[]{o1, o2, o3}, Functions.toFunction(combiner));
    }

    @SchedulerSupport("none")
    public final <T1, T2, T3, T4, R> Observable<R> withLatestFrom(ObservableSource<T1> o1, ObservableSource<T2> o2, ObservableSource<T3> o3, ObservableSource<T4> o4, Function5<? super T, ? super T1, ? super T2, ? super T3, ? super T4, R> combiner) {
        ObjectHelper.requireNonNull(o1, "o1 is null");
        ObjectHelper.requireNonNull(o2, "o2 is null");
        ObjectHelper.requireNonNull(o3, "o3 is null");
        ObjectHelper.requireNonNull(o4, "o4 is null");
        ObjectHelper.requireNonNull(combiner, "combiner is null");
        return withLatestFrom(new ObservableSource[]{o1, o2, o3, o4}, Functions.toFunction(combiner));
    }

    @SchedulerSupport("none")
    public final <R> Observable<R> withLatestFrom(ObservableSource<?>[] others, Function<? super Object[], R> combiner) {
        ObjectHelper.requireNonNull(others, "others is null");
        ObjectHelper.requireNonNull(combiner, "combiner is null");
        return RxJavaPlugins.onAssembly(new ObservableWithLatestFromMany(this, others, combiner));
    }

    @SchedulerSupport("none")
    public final <R> Observable<R> withLatestFrom(Iterable<? extends ObservableSource<?>> others, Function<? super Object[], R> combiner) {
        ObjectHelper.requireNonNull(others, "others is null");
        ObjectHelper.requireNonNull(combiner, "combiner is null");
        return RxJavaPlugins.onAssembly(new ObservableWithLatestFromMany(this, others, combiner));
    }

    @SchedulerSupport("none")
    public final <U, R> Observable<R> zipWith(Iterable<U> other, BiFunction<? super T, ? super U, ? extends R> zipper) {
        ObjectHelper.requireNonNull(other, "other is null");
        ObjectHelper.requireNonNull(zipper, "zipper is null");
        return RxJavaPlugins.onAssembly(new ObservableZipIterable(this, other, zipper));
    }

    @SchedulerSupport("none")
    public final <U, R> Observable<R> zipWith(ObservableSource<? extends U> other, BiFunction<? super T, ? super U, ? extends R> zipper) {
        ObjectHelper.requireNonNull(other, "other is null");
        return zip(this, other, zipper);
    }

    @SchedulerSupport("none")
    public final <U, R> Observable<R> zipWith(ObservableSource<? extends U> other, BiFunction<? super T, ? super U, ? extends R> zipper, boolean delayError) {
        return zip(this, other, zipper, delayError);
    }

    @SchedulerSupport("none")
    public final <U, R> Observable<R> zipWith(ObservableSource<? extends U> other, BiFunction<? super T, ? super U, ? extends R> zipper, boolean delayError, int bufferSize) {
        return zip(this, other, zipper, delayError, bufferSize);
    }

    @SchedulerSupport("none")
    public final TestObserver<T> test() {
        TestObserver<T> ts = new TestObserver<>();
        subscribe(ts);
        return ts;
    }

    @SchedulerSupport("none")
    public final TestObserver<T> test(boolean dispose) {
        TestObserver<T> ts = new TestObserver<>();
        if (dispose) {
            ts.dispose();
        }
        subscribe(ts);
        return ts;
    }
}
