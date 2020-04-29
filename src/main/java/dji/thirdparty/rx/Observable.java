package dji.thirdparty.rx;

import dji.thirdparty.rx.BackpressureOverflow;
import dji.thirdparty.rx.annotations.Beta;
import dji.thirdparty.rx.annotations.Experimental;
import dji.thirdparty.rx.exceptions.Exceptions;
import dji.thirdparty.rx.exceptions.OnErrorNotImplementedException;
import dji.thirdparty.rx.functions.Action0;
import dji.thirdparty.rx.functions.Action1;
import dji.thirdparty.rx.functions.Action2;
import dji.thirdparty.rx.functions.Func0;
import dji.thirdparty.rx.functions.Func1;
import dji.thirdparty.rx.functions.Func2;
import dji.thirdparty.rx.functions.Func3;
import dji.thirdparty.rx.functions.Func4;
import dji.thirdparty.rx.functions.Func5;
import dji.thirdparty.rx.functions.Func6;
import dji.thirdparty.rx.functions.Func7;
import dji.thirdparty.rx.functions.Func8;
import dji.thirdparty.rx.functions.Func9;
import dji.thirdparty.rx.functions.FuncN;
import dji.thirdparty.rx.functions.Functions;
import dji.thirdparty.rx.internal.operators.CachedObservable;
import dji.thirdparty.rx.internal.operators.OnSubscribeAmb;
import dji.thirdparty.rx.internal.operators.OnSubscribeCombineLatest;
import dji.thirdparty.rx.internal.operators.OnSubscribeConcatMap;
import dji.thirdparty.rx.internal.operators.OnSubscribeDefer;
import dji.thirdparty.rx.internal.operators.OnSubscribeDelaySubscription;
import dji.thirdparty.rx.internal.operators.OnSubscribeDelaySubscriptionOther;
import dji.thirdparty.rx.internal.operators.OnSubscribeDelaySubscriptionWithSelector;
import dji.thirdparty.rx.internal.operators.OnSubscribeFromArray;
import dji.thirdparty.rx.internal.operators.OnSubscribeFromCallable;
import dji.thirdparty.rx.internal.operators.OnSubscribeFromIterable;
import dji.thirdparty.rx.internal.operators.OnSubscribeGroupJoin;
import dji.thirdparty.rx.internal.operators.OnSubscribeJoin;
import dji.thirdparty.rx.internal.operators.OnSubscribeRange;
import dji.thirdparty.rx.internal.operators.OnSubscribeRedo;
import dji.thirdparty.rx.internal.operators.OnSubscribeSingle;
import dji.thirdparty.rx.internal.operators.OnSubscribeTimerOnce;
import dji.thirdparty.rx.internal.operators.OnSubscribeTimerPeriodically;
import dji.thirdparty.rx.internal.operators.OnSubscribeToObservableFuture;
import dji.thirdparty.rx.internal.operators.OnSubscribeUsing;
import dji.thirdparty.rx.internal.operators.OperatorAll;
import dji.thirdparty.rx.internal.operators.OperatorAny;
import dji.thirdparty.rx.internal.operators.OperatorAsObservable;
import dji.thirdparty.rx.internal.operators.OperatorBufferWithSingleObservable;
import dji.thirdparty.rx.internal.operators.OperatorBufferWithSize;
import dji.thirdparty.rx.internal.operators.OperatorBufferWithStartEndObservable;
import dji.thirdparty.rx.internal.operators.OperatorBufferWithTime;
import dji.thirdparty.rx.internal.operators.OperatorCast;
import dji.thirdparty.rx.internal.operators.OperatorDebounceWithSelector;
import dji.thirdparty.rx.internal.operators.OperatorDebounceWithTime;
import dji.thirdparty.rx.internal.operators.OperatorDelay;
import dji.thirdparty.rx.internal.operators.OperatorDelayWithSelector;
import dji.thirdparty.rx.internal.operators.OperatorDematerialize;
import dji.thirdparty.rx.internal.operators.OperatorDistinct;
import dji.thirdparty.rx.internal.operators.OperatorDistinctUntilChanged;
import dji.thirdparty.rx.internal.operators.OperatorDoAfterTerminate;
import dji.thirdparty.rx.internal.operators.OperatorDoOnEach;
import dji.thirdparty.rx.internal.operators.OperatorDoOnRequest;
import dji.thirdparty.rx.internal.operators.OperatorDoOnSubscribe;
import dji.thirdparty.rx.internal.operators.OperatorDoOnUnsubscribe;
import dji.thirdparty.rx.internal.operators.OperatorEagerConcatMap;
import dji.thirdparty.rx.internal.operators.OperatorElementAt;
import dji.thirdparty.rx.internal.operators.OperatorFilter;
import dji.thirdparty.rx.internal.operators.OperatorGroupBy;
import dji.thirdparty.rx.internal.operators.OperatorIgnoreElements;
import dji.thirdparty.rx.internal.operators.OperatorMap;
import dji.thirdparty.rx.internal.operators.OperatorMapNotification;
import dji.thirdparty.rx.internal.operators.OperatorMapPair;
import dji.thirdparty.rx.internal.operators.OperatorMaterialize;
import dji.thirdparty.rx.internal.operators.OperatorMerge;
import dji.thirdparty.rx.internal.operators.OperatorObserveOn;
import dji.thirdparty.rx.internal.operators.OperatorOnBackpressureBuffer;
import dji.thirdparty.rx.internal.operators.OperatorOnBackpressureDrop;
import dji.thirdparty.rx.internal.operators.OperatorOnBackpressureLatest;
import dji.thirdparty.rx.internal.operators.OperatorOnErrorResumeNextViaFunction;
import dji.thirdparty.rx.internal.operators.OperatorPublish;
import dji.thirdparty.rx.internal.operators.OperatorReplay;
import dji.thirdparty.rx.internal.operators.OperatorRetryWithPredicate;
import dji.thirdparty.rx.internal.operators.OperatorSampleWithObservable;
import dji.thirdparty.rx.internal.operators.OperatorSampleWithTime;
import dji.thirdparty.rx.internal.operators.OperatorScan;
import dji.thirdparty.rx.internal.operators.OperatorSequenceEqual;
import dji.thirdparty.rx.internal.operators.OperatorSerialize;
import dji.thirdparty.rx.internal.operators.OperatorSingle;
import dji.thirdparty.rx.internal.operators.OperatorSkip;
import dji.thirdparty.rx.internal.operators.OperatorSkipLast;
import dji.thirdparty.rx.internal.operators.OperatorSkipLastTimed;
import dji.thirdparty.rx.internal.operators.OperatorSkipTimed;
import dji.thirdparty.rx.internal.operators.OperatorSkipUntil;
import dji.thirdparty.rx.internal.operators.OperatorSkipWhile;
import dji.thirdparty.rx.internal.operators.OperatorSubscribeOn;
import dji.thirdparty.rx.internal.operators.OperatorSwitchIfEmpty;
import dji.thirdparty.rx.internal.operators.OperatorTake;
import dji.thirdparty.rx.internal.operators.OperatorTakeLast;
import dji.thirdparty.rx.internal.operators.OperatorTakeLastOne;
import dji.thirdparty.rx.internal.operators.OperatorTakeLastTimed;
import dji.thirdparty.rx.internal.operators.OperatorTakeTimed;
import dji.thirdparty.rx.internal.operators.OperatorTakeUntil;
import dji.thirdparty.rx.internal.operators.OperatorTakeUntilPredicate;
import dji.thirdparty.rx.internal.operators.OperatorTakeWhile;
import dji.thirdparty.rx.internal.operators.OperatorThrottleFirst;
import dji.thirdparty.rx.internal.operators.OperatorTimeInterval;
import dji.thirdparty.rx.internal.operators.OperatorTimeout;
import dji.thirdparty.rx.internal.operators.OperatorTimeoutWithSelector;
import dji.thirdparty.rx.internal.operators.OperatorTimestamp;
import dji.thirdparty.rx.internal.operators.OperatorToMap;
import dji.thirdparty.rx.internal.operators.OperatorToMultimap;
import dji.thirdparty.rx.internal.operators.OperatorToObservableList;
import dji.thirdparty.rx.internal.operators.OperatorToObservableSortedList;
import dji.thirdparty.rx.internal.operators.OperatorUnsubscribeOn;
import dji.thirdparty.rx.internal.operators.OperatorWindowWithObservable;
import dji.thirdparty.rx.internal.operators.OperatorWindowWithObservableFactory;
import dji.thirdparty.rx.internal.operators.OperatorWindowWithSize;
import dji.thirdparty.rx.internal.operators.OperatorWindowWithStartEndObservable;
import dji.thirdparty.rx.internal.operators.OperatorWindowWithTime;
import dji.thirdparty.rx.internal.operators.OperatorWithLatestFrom;
import dji.thirdparty.rx.internal.operators.OperatorZip;
import dji.thirdparty.rx.internal.operators.OperatorZipIterable;
import dji.thirdparty.rx.internal.producers.SingleProducer;
import dji.thirdparty.rx.internal.util.RxRingBuffer;
import dji.thirdparty.rx.internal.util.ScalarSynchronousObservable;
import dji.thirdparty.rx.internal.util.UtilityFunctions;
import dji.thirdparty.rx.observables.AsyncOnSubscribe;
import dji.thirdparty.rx.observables.BlockingObservable;
import dji.thirdparty.rx.observables.ConnectableObservable;
import dji.thirdparty.rx.observables.GroupedObservable;
import dji.thirdparty.rx.observables.SyncOnSubscribe;
import dji.thirdparty.rx.observers.SafeSubscriber;
import dji.thirdparty.rx.plugins.RxJavaObservableExecutionHook;
import dji.thirdparty.rx.plugins.RxJavaPlugins;
import dji.thirdparty.rx.schedulers.Schedulers;
import dji.thirdparty.rx.schedulers.TimeInterval;
import dji.thirdparty.rx.schedulers.Timestamped;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

public class Observable<T> {
    static final RxJavaObservableExecutionHook hook = RxJavaPlugins.getInstance().getObservableExecutionHook();
    final OnSubscribe<T> onSubscribe;

    public interface OnSubscribe<T> extends Action1<Subscriber<? super T>> {
    }

    public interface Operator<R, T> extends Func1<Subscriber<? super R>, Subscriber<? super T>> {
    }

    public interface Transformer<T, R> extends Func1<Observable<T>, Observable<R>> {
    }

    protected Observable(OnSubscribe<T> f) {
        this.onSubscribe = f;
    }

    public static <T> Observable<T> create(OnSubscribe<T> f) {
        return new Observable<>(hook.onCreate(f));
    }

    @Beta
    public static <S, T> Observable<T> create(SyncOnSubscribe<S, T> syncOnSubscribe) {
        return new Observable<>(hook.onCreate(syncOnSubscribe));
    }

    @Experimental
    public static <S, T> Observable<T> create(AsyncOnSubscribe<S, T> asyncOnSubscribe) {
        return new Observable<>(hook.onCreate(asyncOnSubscribe));
    }

    @Experimental
    public <R> R extend(Func1<? super OnSubscribe<T>, ? extends R> conversion) {
        return conversion.call(new OnSubscribe<T>() {
            /* class dji.thirdparty.rx.Observable.AnonymousClass1 */

            public /* bridge */ /* synthetic */ void call(Object x0) {
                call((Subscriber) ((Subscriber) x0));
            }

            public void call(Subscriber<? super T> subscriber) {
                subscriber.add(Observable.subscribe(subscriber, Observable.this));
            }
        });
    }

    public final <R> Observable<R> lift(final Operator<? extends R, ? super T> operator) {
        return new Observable<>(new OnSubscribe<R>() {
            /* class dji.thirdparty.rx.Observable.AnonymousClass2 */

            public /* bridge */ /* synthetic */ void call(Object x0) {
                call((Subscriber) ((Subscriber) x0));
            }

            public void call(Subscriber<? super R> o) {
                Subscriber<? super T> st;
                try {
                    st = (Subscriber) Observable.hook.onLift(operator).call(o);
                    st.onStart();
                    Observable.this.onSubscribe.call(st);
                } catch (Throwable e) {
                    Exceptions.throwIfFatal(e);
                    o.onError(e);
                }
            }
        });
    }

    public <R> Observable<R> compose(Transformer<? super T, ? extends R> transformer) {
        return (Observable) transformer.call(this);
    }

    @Beta
    public Single<T> toSingle() {
        return new Single<>(OnSubscribeSingle.create(this));
    }

    @Experimental
    public Completable toCompletable() {
        return Completable.fromObservable(this);
    }

    public static <T> Observable<T> amb(Iterable<? extends Observable<? extends T>> sources) {
        return create(OnSubscribeAmb.amb(sources));
    }

    public static <T> Observable<T> amb(Observable<? extends T> o1, Observable<? extends T> o2) {
        return create(OnSubscribeAmb.amb(o1, o2));
    }

    public static <T> Observable<T> amb(Observable<? extends T> o1, Observable<? extends T> o2, Observable<? extends T> o3) {
        return create(OnSubscribeAmb.amb(o1, o2, o3));
    }

    public static <T> Observable<T> amb(Observable<? extends T> o1, Observable<? extends T> o2, Observable<? extends T> o3, Observable<? extends T> o4) {
        return create(OnSubscribeAmb.amb(o1, o2, o3, o4));
    }

    public static <T> Observable<T> amb(Observable<? extends T> o1, Observable<? extends T> o2, Observable<? extends T> o3, Observable<? extends T> o4, Observable<? extends T> o5) {
        return create(OnSubscribeAmb.amb(o1, o2, o3, o4, o5));
    }

    public static <T> Observable<T> amb(Observable<? extends T> o1, Observable<? extends T> o2, Observable<? extends T> o3, Observable<? extends T> o4, Observable<? extends T> o5, Observable<? extends T> o6) {
        return create(OnSubscribeAmb.amb(o1, o2, o3, o4, o5, o6));
    }

    public static <T> Observable<T> amb(Observable<? extends T> o1, Observable<? extends T> o2, Observable<? extends T> o3, Observable<? extends T> o4, Observable<? extends T> o5, Observable<? extends T> o6, Observable<? extends T> o7) {
        return create(OnSubscribeAmb.amb(o1, o2, o3, o4, o5, o6, o7));
    }

    public static <T> Observable<T> amb(Observable<? extends T> o1, Observable<? extends T> o2, Observable<? extends T> o3, Observable<? extends T> o4, Observable<? extends T> o5, Observable<? extends T> o6, Observable<? extends T> o7, Observable<? extends T> o8) {
        return create(OnSubscribeAmb.amb(o1, o2, o3, o4, o5, o6, o7, o8));
    }

    public static <T> Observable<T> amb(Observable<? extends T> o1, Observable<? extends T> o2, Observable<? extends T> o3, Observable<? extends T> o4, Observable<? extends T> o5, Observable<? extends T> o6, Observable<? extends T> o7, Observable<? extends T> o8, Observable<? extends T> o9) {
        return create(OnSubscribeAmb.amb(o1, o2, o3, o4, o5, o6, o7, o8, o9));
    }

    /* JADX DEBUG: Failed to find minimal casts for resolve overloaded methods, cast all args instead
     method: dji.thirdparty.rx.Observable.combineLatest(java.util.List, dji.thirdparty.rx.functions.FuncN):dji.thirdparty.rx.Observable<R>
     arg types: [java.util.List, dji.thirdparty.rx.functions.FuncN<? extends R>]
     candidates:
      dji.thirdparty.rx.Observable.combineLatest(java.lang.Iterable, dji.thirdparty.rx.functions.FuncN):dji.thirdparty.rx.Observable<R>
      dji.thirdparty.rx.Observable.combineLatest(java.util.List, dji.thirdparty.rx.functions.FuncN):dji.thirdparty.rx.Observable<R> */
    public static <T1, T2, R> Observable<R> combineLatest(Observable<? extends T1> o1, Observable<? extends T2> o2, Func2<? super T1, ? super T2, ? extends R> combineFunction) {
        return combineLatest(Arrays.asList(o1, o2), (FuncN) Functions.fromFunc(combineFunction));
    }

    /* JADX DEBUG: Failed to find minimal casts for resolve overloaded methods, cast all args instead
     method: dji.thirdparty.rx.Observable.combineLatest(java.util.List, dji.thirdparty.rx.functions.FuncN):dji.thirdparty.rx.Observable<R>
     arg types: [java.util.List, dji.thirdparty.rx.functions.FuncN<? extends R>]
     candidates:
      dji.thirdparty.rx.Observable.combineLatest(java.lang.Iterable, dji.thirdparty.rx.functions.FuncN):dji.thirdparty.rx.Observable<R>
      dji.thirdparty.rx.Observable.combineLatest(java.util.List, dji.thirdparty.rx.functions.FuncN):dji.thirdparty.rx.Observable<R> */
    public static <T1, T2, T3, R> Observable<R> combineLatest(Observable<? extends T1> o1, Observable<? extends T2> o2, Observable<? extends T3> o3, Func3<? super T1, ? super T2, ? super T3, ? extends R> combineFunction) {
        return combineLatest(Arrays.asList(o1, o2, o3), (FuncN) Functions.fromFunc(combineFunction));
    }

    /* JADX DEBUG: Failed to find minimal casts for resolve overloaded methods, cast all args instead
     method: dji.thirdparty.rx.Observable.combineLatest(java.util.List, dji.thirdparty.rx.functions.FuncN):dji.thirdparty.rx.Observable<R>
     arg types: [java.util.List, dji.thirdparty.rx.functions.FuncN<? extends R>]
     candidates:
      dji.thirdparty.rx.Observable.combineLatest(java.lang.Iterable, dji.thirdparty.rx.functions.FuncN):dji.thirdparty.rx.Observable<R>
      dji.thirdparty.rx.Observable.combineLatest(java.util.List, dji.thirdparty.rx.functions.FuncN):dji.thirdparty.rx.Observable<R> */
    public static <T1, T2, T3, T4, R> Observable<R> combineLatest(Observable<? extends T1> o1, Observable<? extends T2> o2, Observable<? extends T3> o3, Observable<? extends T4> o4, Func4<? super T1, ? super T2, ? super T3, ? super T4, ? extends R> combineFunction) {
        return combineLatest(Arrays.asList(o1, o2, o3, o4), (FuncN) Functions.fromFunc(combineFunction));
    }

    /* JADX DEBUG: Failed to find minimal casts for resolve overloaded methods, cast all args instead
     method: dji.thirdparty.rx.Observable.combineLatest(java.util.List, dji.thirdparty.rx.functions.FuncN):dji.thirdparty.rx.Observable<R>
     arg types: [java.util.List, dji.thirdparty.rx.functions.FuncN<? extends R>]
     candidates:
      dji.thirdparty.rx.Observable.combineLatest(java.lang.Iterable, dji.thirdparty.rx.functions.FuncN):dji.thirdparty.rx.Observable<R>
      dji.thirdparty.rx.Observable.combineLatest(java.util.List, dji.thirdparty.rx.functions.FuncN):dji.thirdparty.rx.Observable<R> */
    public static <T1, T2, T3, T4, T5, R> Observable<R> combineLatest(Observable<? extends T1> o1, Observable<? extends T2> o2, Observable<? extends T3> o3, Observable<? extends T4> o4, Observable<? extends T5> o5, Func5<? super T1, ? super T2, ? super T3, ? super T4, ? super T5, ? extends R> combineFunction) {
        return combineLatest(Arrays.asList(o1, o2, o3, o4, o5), (FuncN) Functions.fromFunc(combineFunction));
    }

    /* JADX DEBUG: Failed to find minimal casts for resolve overloaded methods, cast all args instead
     method: dji.thirdparty.rx.Observable.combineLatest(java.util.List, dji.thirdparty.rx.functions.FuncN):dji.thirdparty.rx.Observable<R>
     arg types: [java.util.List, dji.thirdparty.rx.functions.FuncN<? extends R>]
     candidates:
      dji.thirdparty.rx.Observable.combineLatest(java.lang.Iterable, dji.thirdparty.rx.functions.FuncN):dji.thirdparty.rx.Observable<R>
      dji.thirdparty.rx.Observable.combineLatest(java.util.List, dji.thirdparty.rx.functions.FuncN):dji.thirdparty.rx.Observable<R> */
    public static <T1, T2, T3, T4, T5, T6, R> Observable<R> combineLatest(Observable<? extends T1> o1, Observable<? extends T2> o2, Observable<? extends T3> o3, Observable<? extends T4> o4, Observable<? extends T5> o5, Observable<? extends T6> o6, Func6<? super T1, ? super T2, ? super T3, ? super T4, ? super T5, ? super T6, ? extends R> combineFunction) {
        return combineLatest(Arrays.asList(o1, o2, o3, o4, o5, o6), (FuncN) Functions.fromFunc(combineFunction));
    }

    /* JADX DEBUG: Failed to find minimal casts for resolve overloaded methods, cast all args instead
     method: dji.thirdparty.rx.Observable.combineLatest(java.util.List, dji.thirdparty.rx.functions.FuncN):dji.thirdparty.rx.Observable<R>
     arg types: [java.util.List, dji.thirdparty.rx.functions.FuncN<? extends R>]
     candidates:
      dji.thirdparty.rx.Observable.combineLatest(java.lang.Iterable, dji.thirdparty.rx.functions.FuncN):dji.thirdparty.rx.Observable<R>
      dji.thirdparty.rx.Observable.combineLatest(java.util.List, dji.thirdparty.rx.functions.FuncN):dji.thirdparty.rx.Observable<R> */
    public static <T1, T2, T3, T4, T5, T6, T7, R> Observable<R> combineLatest(Observable<? extends T1> o1, Observable<? extends T2> o2, Observable<? extends T3> o3, Observable<? extends T4> o4, Observable<? extends T5> o5, Observable<? extends T6> o6, Observable<? extends T7> o7, Func7<? super T1, ? super T2, ? super T3, ? super T4, ? super T5, ? super T6, ? super T7, ? extends R> combineFunction) {
        return combineLatest(Arrays.asList(o1, o2, o3, o4, o5, o6, o7), (FuncN) Functions.fromFunc(combineFunction));
    }

    /* JADX DEBUG: Failed to find minimal casts for resolve overloaded methods, cast all args instead
     method: dji.thirdparty.rx.Observable.combineLatest(java.util.List, dji.thirdparty.rx.functions.FuncN):dji.thirdparty.rx.Observable<R>
     arg types: [java.util.List, dji.thirdparty.rx.functions.FuncN<? extends R>]
     candidates:
      dji.thirdparty.rx.Observable.combineLatest(java.lang.Iterable, dji.thirdparty.rx.functions.FuncN):dji.thirdparty.rx.Observable<R>
      dji.thirdparty.rx.Observable.combineLatest(java.util.List, dji.thirdparty.rx.functions.FuncN):dji.thirdparty.rx.Observable<R> */
    public static <T1, T2, T3, T4, T5, T6, T7, T8, R> Observable<R> combineLatest(Observable<? extends T1> o1, Observable<? extends T2> o2, Observable<? extends T3> o3, Observable<? extends T4> o4, Observable<? extends T5> o5, Observable<? extends T6> o6, Observable<? extends T7> o7, Observable<? extends T8> o8, Func8<? super T1, ? super T2, ? super T3, ? super T4, ? super T5, ? super T6, ? super T7, ? super T8, ? extends R> combineFunction) {
        return combineLatest(Arrays.asList(o1, o2, o3, o4, o5, o6, o7, o8), (FuncN) Functions.fromFunc(combineFunction));
    }

    /* JADX DEBUG: Failed to find minimal casts for resolve overloaded methods, cast all args instead
     method: dji.thirdparty.rx.Observable.combineLatest(java.util.List, dji.thirdparty.rx.functions.FuncN):dji.thirdparty.rx.Observable<R>
     arg types: [java.util.List, dji.thirdparty.rx.functions.FuncN<? extends R>]
     candidates:
      dji.thirdparty.rx.Observable.combineLatest(java.lang.Iterable, dji.thirdparty.rx.functions.FuncN):dji.thirdparty.rx.Observable<R>
      dji.thirdparty.rx.Observable.combineLatest(java.util.List, dji.thirdparty.rx.functions.FuncN):dji.thirdparty.rx.Observable<R> */
    public static <T1, T2, T3, T4, T5, T6, T7, T8, T9, R> Observable<R> combineLatest(Observable<? extends T1> o1, Observable<? extends T2> o2, Observable<? extends T3> o3, Observable<? extends T4> o4, Observable<? extends T5> o5, Observable<? extends T6> o6, Observable<? extends T7> o7, Observable<? extends T8> o8, Observable<? extends T9> o9, Func9<? super T1, ? super T2, ? super T3, ? super T4, ? super T5, ? super T6, ? super T7, ? super T8, ? super T9, ? extends R> combineFunction) {
        return combineLatest(Arrays.asList(o1, o2, o3, o4, o5, o6, o7, o8, o9), (FuncN) Functions.fromFunc(combineFunction));
    }

    public static <T, R> Observable<R> combineLatest(List<? extends Observable<? extends T>> sources, FuncN<? extends R> combineFunction) {
        return create(new OnSubscribeCombineLatest(sources, combineFunction));
    }

    public static <T, R> Observable<R> combineLatest(Iterable<? extends Observable<? extends T>> sources, FuncN<? extends R> combineFunction) {
        return create(new OnSubscribeCombineLatest(sources, combineFunction));
    }

    public static <T, R> Observable<R> combineLatestDelayError(Iterable<? extends Observable<? extends T>> sources, FuncN<? extends R> combineFunction) {
        return create(new OnSubscribeCombineLatest(null, sources, combineFunction, RxRingBuffer.SIZE, true));
    }

    /* JADX WARN: Type inference failed for: r1v0, types: [dji.thirdparty.rx.Observable, dji.thirdparty.rx.Observable<? extends dji.thirdparty.rx.Observable<? extends T>>], assign insn: null */
    /* JADX WARNING: Unknown variable types count: 1 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static <T> dji.thirdparty.rx.Observable<T> concat(dji.thirdparty.rx.Observable<? extends dji.thirdparty.rx.Observable<? extends T>> r1) {
        /*
            dji.thirdparty.rx.functions.Func1 r0 = dji.thirdparty.rx.internal.util.UtilityFunctions.identity()
            dji.thirdparty.rx.Observable r0 = r1.concatMap(r0)
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: dji.thirdparty.rx.Observable.concat(dji.thirdparty.rx.Observable):dji.thirdparty.rx.Observable");
    }

    public static <T> Observable<T> concat(Observable<? extends T> t1, Observable<? extends T> t2) {
        return concat(just(t1, t2));
    }

    public static <T> Observable<T> concat(Observable<? extends T> t1, Observable<? extends T> t2, Observable<? extends T> t3) {
        return concat(just(t1, t2, t3));
    }

    public static <T> Observable<T> concat(Observable<? extends T> t1, Observable<? extends T> t2, Observable<? extends T> t3, Observable<? extends T> t4) {
        return concat(just(t1, t2, t3, t4));
    }

    public static <T> Observable<T> concat(Observable<? extends T> t1, Observable<? extends T> t2, Observable<? extends T> t3, Observable<? extends T> t4, Observable<? extends T> t5) {
        return concat(just(t1, t2, t3, t4, t5));
    }

    public static <T> Observable<T> concat(Observable<? extends T> t1, Observable<? extends T> t2, Observable<? extends T> t3, Observable<? extends T> t4, Observable<? extends T> t5, Observable<? extends T> t6) {
        return concat(just(t1, t2, t3, t4, t5, t6));
    }

    public static <T> Observable<T> concat(Observable<? extends T> t1, Observable<? extends T> t2, Observable<? extends T> t3, Observable<? extends T> t4, Observable<? extends T> t5, Observable<? extends T> t6, Observable<? extends T> t7) {
        return concat(just(t1, t2, t3, t4, t5, t6, t7));
    }

    public static <T> Observable<T> concat(Observable<? extends T> t1, Observable<? extends T> t2, Observable<? extends T> t3, Observable<? extends T> t4, Observable<? extends T> t5, Observable<? extends T> t6, Observable<? extends T> t7, Observable<? extends T> t8) {
        return concat(just(t1, t2, t3, t4, t5, t6, t7, t8));
    }

    public static <T> Observable<T> concat(Observable<? extends T> t1, Observable<? extends T> t2, Observable<? extends T> t3, Observable<? extends T> t4, Observable<? extends T> t5, Observable<? extends T> t6, Observable<? extends T> t7, Observable<? extends T> t8, Observable<? extends T> t9) {
        return concat(just(t1, t2, t3, t4, t5, t6, t7, t8, t9));
    }

    /* JADX WARN: Type inference failed for: r1v0, types: [dji.thirdparty.rx.Observable, dji.thirdparty.rx.Observable<? extends dji.thirdparty.rx.Observable<? extends T>>], assign insn: null */
    /* JADX WARNING: Unknown variable types count: 1 */
    @dji.thirdparty.rx.annotations.Experimental
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static <T> dji.thirdparty.rx.Observable<T> concatDelayError(dji.thirdparty.rx.Observable<? extends dji.thirdparty.rx.Observable<? extends T>> r1) {
        /*
            dji.thirdparty.rx.functions.Func1 r0 = dji.thirdparty.rx.internal.util.UtilityFunctions.identity()
            dji.thirdparty.rx.Observable r0 = r1.concatMapDelayError(r0)
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: dji.thirdparty.rx.Observable.concatDelayError(dji.thirdparty.rx.Observable):dji.thirdparty.rx.Observable");
    }

    @Experimental
    public static <T> Observable<T> concatDelayError(Iterable<? extends Observable<? extends T>> sources) {
        return concatDelayError(from(sources));
    }

    public static <T> Observable<T> defer(Func0<Observable<T>> observableFactory) {
        return create(new OnSubscribeDefer(observableFactory));
    }

    private static final class EmptyHolder {
        static final Observable<Object> INSTANCE = Observable.create(new OnSubscribe<Object>() {
            /* class dji.thirdparty.rx.Observable.EmptyHolder.AnonymousClass1 */

            public /* bridge */ /* synthetic */ void call(Object x0) {
                call((Subscriber<? super Object>) ((Subscriber) x0));
            }

            public void call(Subscriber<? super Object> subscriber) {
                subscriber.onCompleted();
            }
        });

        private EmptyHolder() {
        }
    }

    public static <T> Observable<T> empty() {
        return EmptyHolder.INSTANCE;
    }

    public static <T> Observable<T> error(Throwable exception) {
        return new ThrowObservable(exception);
    }

    public static <T> Observable<T> from(Future<? extends T> future) {
        return create(OnSubscribeToObservableFuture.toObservableFuture(future));
    }

    public static <T> Observable<T> from(Future<? extends T> future, long timeout, TimeUnit unit) {
        return create(OnSubscribeToObservableFuture.toObservableFuture(future, timeout, unit));
    }

    public static <T> Observable<T> from(Future<? extends T> future, Scheduler scheduler) {
        return create(OnSubscribeToObservableFuture.toObservableFuture(future)).subscribeOn(scheduler);
    }

    public static <T> Observable<T> from(Iterable<? extends T> iterable) {
        return create(new OnSubscribeFromIterable(iterable));
    }

    public static <T> Observable<T> from(T[] array) {
        int n = array.length;
        if (n == 0) {
            return empty();
        }
        if (n == 1) {
            return just(array[0]);
        }
        return create(new OnSubscribeFromArray(array));
    }

    @Beta
    public static <T> Observable<T> fromCallable(Callable<? extends T> func) {
        return create(new OnSubscribeFromCallable(func));
    }

    public static Observable<Long> interval(long interval, TimeUnit unit) {
        return interval(interval, interval, unit, Schedulers.computation());
    }

    public static Observable<Long> interval(long interval, TimeUnit unit, Scheduler scheduler) {
        return interval(interval, interval, unit, scheduler);
    }

    public static Observable<Long> interval(long initialDelay, long period, TimeUnit unit) {
        return interval(initialDelay, period, unit, Schedulers.computation());
    }

    public static Observable<Long> interval(long initialDelay, long period, TimeUnit unit, Scheduler scheduler) {
        return create(new OnSubscribeTimerPeriodically(initialDelay, period, unit, scheduler));
    }

    public static <T> Observable<T> just(T value) {
        return ScalarSynchronousObservable.create((Object) value);
    }

    public static <T> Observable<T> just(T t1, T t2) {
        return from(new Object[]{t1, t2});
    }

    public static <T> Observable<T> just(T t1, T t2, T t3) {
        return from(new Object[]{t1, t2, t3});
    }

    public static <T> Observable<T> just(T t1, T t2, T t3, T t4) {
        return from(new Object[]{t1, t2, t3, t4});
    }

    public static <T> Observable<T> just(T t1, T t2, T t3, T t4, T t5) {
        return from(new Object[]{t1, t2, t3, t4, t5});
    }

    public static <T> Observable<T> just(T t1, T t2, T t3, T t4, T t5, T t6) {
        return from(new Object[]{t1, t2, t3, t4, t5, t6});
    }

    public static <T> Observable<T> just(T t1, T t2, T t3, T t4, T t5, T t6, T t7) {
        return from(new Object[]{t1, t2, t3, t4, t5, t6, t7});
    }

    public static <T> Observable<T> just(T t1, T t2, T t3, T t4, T t5, T t6, T t7, T t8) {
        return from(new Object[]{t1, t2, t3, t4, t5, t6, t7, t8});
    }

    public static <T> Observable<T> just(T t1, T t2, T t3, T t4, T t5, T t6, T t7, T t8, T t9) {
        return from(new Object[]{t1, t2, t3, t4, t5, t6, t7, t8, t9});
    }

    public static <T> Observable<T> just(T t1, T t2, T t3, T t4, T t5, T t6, T t7, T t8, T t9, T t10) {
        return from(new Object[]{t1, t2, t3, t4, t5, t6, t7, t8, t9, t10});
    }

    public static <T> Observable<T> merge(Iterable<? extends Observable<? extends T>> sequences) {
        return merge(from(sequences));
    }

    public static <T> Observable<T> merge(Iterable iterable, int maxConcurrent) {
        return merge(from(iterable), maxConcurrent);
    }

    /* JADX WARN: Type inference failed for: r2v0, types: [dji.thirdparty.rx.Observable, java.lang.Object, dji.thirdparty.rx.Observable<? extends dji.thirdparty.rx.Observable<? extends T>>], assign insn: null */
    /* JADX WARNING: Unknown variable types count: 1 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static <T> dji.thirdparty.rx.Observable<T> merge(dji.thirdparty.rx.Observable<? extends dji.thirdparty.rx.Observable<? extends T>> r2) {
        /*
            java.lang.Class r0 = r2.getClass()
            java.lang.Class<dji.thirdparty.rx.internal.util.ScalarSynchronousObservable> r1 = dji.thirdparty.rx.internal.util.ScalarSynchronousObservable.class
            if (r0 != r1) goto L_0x0013
            dji.thirdparty.rx.internal.util.ScalarSynchronousObservable r2 = (dji.thirdparty.rx.internal.util.ScalarSynchronousObservable) r2
            dji.thirdparty.rx.functions.Func1 r0 = dji.thirdparty.rx.internal.util.UtilityFunctions.identity()
            dji.thirdparty.rx.Observable r0 = r2.scalarFlatMap(r0)
        L_0x0012:
            return r0
        L_0x0013:
            r0 = 0
            dji.thirdparty.rx.internal.operators.OperatorMerge r0 = dji.thirdparty.rx.internal.operators.OperatorMerge.instance(r0)
            dji.thirdparty.rx.Observable r0 = r2.lift(r0)
            goto L_0x0012
        */
        throw new UnsupportedOperationException("Method not decompiled: dji.thirdparty.rx.Observable.merge(dji.thirdparty.rx.Observable):dji.thirdparty.rx.Observable");
    }

    public static <T> Observable<T> merge(Observable observable, int maxConcurrent) {
        if (observable.getClass() == ScalarSynchronousObservable.class) {
            return ((ScalarSynchronousObservable) observable).scalarFlatMap(UtilityFunctions.identity());
        }
        return observable.lift(OperatorMerge.instance(false, maxConcurrent));
    }

    public static <T> Observable<T> merge(Observable observable, Observable observable2) {
        return merge(new Observable[]{observable, observable2});
    }

    public static <T> Observable<T> merge(Observable<? extends T> t1, Observable<? extends T> t2, Observable<? extends T> t3) {
        return merge(new Observable[]{t1, t2, t3});
    }

    public static <T> Observable<T> merge(Observable<? extends T> t1, Observable<? extends T> t2, Observable<? extends T> t3, Observable<? extends T> t4) {
        return merge(new Observable[]{t1, t2, t3, t4});
    }

    public static <T> Observable<T> merge(Observable<? extends T> t1, Observable<? extends T> t2, Observable<? extends T> t3, Observable<? extends T> t4, Observable<? extends T> t5) {
        return merge(new Observable[]{t1, t2, t3, t4, t5});
    }

    public static <T> Observable<T> merge(Observable<? extends T> t1, Observable<? extends T> t2, Observable<? extends T> t3, Observable<? extends T> t4, Observable<? extends T> t5, Observable<? extends T> t6) {
        return merge(new Observable[]{t1, t2, t3, t4, t5, t6});
    }

    public static <T> Observable<T> merge(Observable<? extends T> t1, Observable<? extends T> t2, Observable<? extends T> t3, Observable<? extends T> t4, Observable<? extends T> t5, Observable<? extends T> t6, Observable<? extends T> t7) {
        return merge(new Observable[]{t1, t2, t3, t4, t5, t6, t7});
    }

    public static <T> Observable<T> merge(Observable<? extends T> t1, Observable<? extends T> t2, Observable<? extends T> t3, Observable<? extends T> t4, Observable<? extends T> t5, Observable<? extends T> t6, Observable<? extends T> t7, Observable<? extends T> t8) {
        return merge(new Observable[]{t1, t2, t3, t4, t5, t6, t7, t8});
    }

    public static <T> Observable<T> merge(Observable<? extends T> t1, Observable<? extends T> t2, Observable<? extends T> t3, Observable<? extends T> t4, Observable<? extends T> t5, Observable<? extends T> t6, Observable<? extends T> t7, Observable<? extends T> t8, Observable<? extends T> t9) {
        return merge(new Observable[]{t1, t2, t3, t4, t5, t6, t7, t8, t9});
    }

    public static <T> Observable<T> merge(Observable<? extends T>[] sequences) {
        return merge(from(sequences));
    }

    public static <T> Observable<T> merge(Observable[] observableArr, int maxConcurrent) {
        return merge(from(observableArr), maxConcurrent);
    }

    /* JADX WARN: Type inference failed for: r1v0, types: [dji.thirdparty.rx.Observable, dji.thirdparty.rx.Observable<? extends dji.thirdparty.rx.Observable<? extends T>>], assign insn: null */
    /* JADX WARNING: Unknown variable types count: 1 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static <T> dji.thirdparty.rx.Observable<T> mergeDelayError(dji.thirdparty.rx.Observable<? extends dji.thirdparty.rx.Observable<? extends T>> r1) {
        /*
            r0 = 1
            dji.thirdparty.rx.internal.operators.OperatorMerge r0 = dji.thirdparty.rx.internal.operators.OperatorMerge.instance(r0)
            dji.thirdparty.rx.Observable r0 = r1.lift(r0)
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: dji.thirdparty.rx.Observable.mergeDelayError(dji.thirdparty.rx.Observable):dji.thirdparty.rx.Observable");
    }

    /* JADX WARN: Type inference failed for: r1v0, types: [dji.thirdparty.rx.Observable, dji.thirdparty.rx.Observable<? extends dji.thirdparty.rx.Observable<? extends T>>], assign insn: null */
    /* JADX WARNING: Unknown variable types count: 1 */
    @dji.thirdparty.rx.annotations.Experimental
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static <T> dji.thirdparty.rx.Observable<T> mergeDelayError(dji.thirdparty.rx.Observable<? extends dji.thirdparty.rx.Observable<? extends T>> r1, int r2) {
        /*
            r0 = 1
            dji.thirdparty.rx.internal.operators.OperatorMerge r0 = dji.thirdparty.rx.internal.operators.OperatorMerge.instance(r0, r2)
            dji.thirdparty.rx.Observable r0 = r1.lift(r0)
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: dji.thirdparty.rx.Observable.mergeDelayError(dji.thirdparty.rx.Observable, int):dji.thirdparty.rx.Observable");
    }

    public static <T> Observable<T> mergeDelayError(Iterable<? extends Observable<? extends T>> sequences) {
        return mergeDelayError(from(sequences));
    }

    public static <T> Observable<T> mergeDelayError(Iterable<? extends Observable<? extends T>> sequences, int maxConcurrent) {
        return mergeDelayError(from(sequences), maxConcurrent);
    }

    public static <T> Observable<T> mergeDelayError(Observable<? extends T> t1, Observable<? extends T> t2) {
        return mergeDelayError(just(t1, t2));
    }

    public static <T> Observable<T> mergeDelayError(Observable<? extends T> t1, Observable<? extends T> t2, Observable<? extends T> t3) {
        return mergeDelayError(just(t1, t2, t3));
    }

    public static <T> Observable<T> mergeDelayError(Observable<? extends T> t1, Observable<? extends T> t2, Observable<? extends T> t3, Observable<? extends T> t4) {
        return mergeDelayError(just(t1, t2, t3, t4));
    }

    public static <T> Observable<T> mergeDelayError(Observable<? extends T> t1, Observable<? extends T> t2, Observable<? extends T> t3, Observable<? extends T> t4, Observable<? extends T> t5) {
        return mergeDelayError(just(t1, t2, t3, t4, t5));
    }

    public static <T> Observable<T> mergeDelayError(Observable<? extends T> t1, Observable<? extends T> t2, Observable<? extends T> t3, Observable<? extends T> t4, Observable<? extends T> t5, Observable<? extends T> t6) {
        return mergeDelayError(just(t1, t2, t3, t4, t5, t6));
    }

    public static <T> Observable<T> mergeDelayError(Observable<? extends T> t1, Observable<? extends T> t2, Observable<? extends T> t3, Observable<? extends T> t4, Observable<? extends T> t5, Observable<? extends T> t6, Observable<? extends T> t7) {
        return mergeDelayError(just(t1, t2, t3, t4, t5, t6, t7));
    }

    public static <T> Observable<T> mergeDelayError(Observable<? extends T> t1, Observable<? extends T> t2, Observable<? extends T> t3, Observable<? extends T> t4, Observable<? extends T> t5, Observable<? extends T> t6, Observable<? extends T> t7, Observable<? extends T> t8) {
        return mergeDelayError(just(t1, t2, t3, t4, t5, t6, t7, t8));
    }

    public static <T> Observable<T> mergeDelayError(Observable<? extends T> t1, Observable<? extends T> t2, Observable<? extends T> t3, Observable<? extends T> t4, Observable<? extends T> t5, Observable<? extends T> t6, Observable<? extends T> t7, Observable<? extends T> t8, Observable<? extends T> t9) {
        return mergeDelayError(just(t1, t2, t3, t4, t5, t6, t7, t8, t9));
    }

    public final Observable<Observable<T>> nest() {
        return just(this);
    }

    public static <T> Observable<T> never() {
        return NeverObservable.instance();
    }

    public static Observable<Integer> range(int start, int count) {
        if (count < 0) {
            throw new IllegalArgumentException("Count can not be negative");
        } else if (count == 0) {
            return empty();
        } else {
            if (start > (Integer.MAX_VALUE - count) + 1) {
                throw new IllegalArgumentException("start + count can not exceed Integer.MAX_VALUE");
            } else if (count == 1) {
                return just(Integer.valueOf(start));
            } else {
                return create(new OnSubscribeRange(start, (count - 1) + start));
            }
        }
    }

    public static Observable<Integer> range(int start, int count, Scheduler scheduler) {
        return range(start, count).subscribeOn(scheduler);
    }

    public static <T> Observable<Boolean> sequenceEqual(Observable<? extends T> first, Observable<? extends T> second) {
        return sequenceEqual(first, second, new Func2<T, T, Boolean>() {
            /* class dji.thirdparty.rx.Observable.AnonymousClass3 */

            public final Boolean call(T first, T second) {
                if (first != null) {
                    return Boolean.valueOf(first.equals(second));
                }
                return Boolean.valueOf(second == null);
            }
        });
    }

    public static <T> Observable<Boolean> sequenceEqual(Observable<? extends T> first, Observable<? extends T> second, Func2<? super T, ? super T, Boolean> equality) {
        return OperatorSequenceEqual.sequenceEqual(first, second, equality);
    }

    /* JADX WARN: Type inference failed for: r1v0, types: [dji.thirdparty.rx.Observable, dji.thirdparty.rx.Observable<? extends dji.thirdparty.rx.Observable<? extends T>>], assign insn: null */
    /* JADX WARNING: Unknown variable types count: 1 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static <T> dji.thirdparty.rx.Observable<T> switchOnNext(dji.thirdparty.rx.Observable<? extends dji.thirdparty.rx.Observable<? extends T>> r1) {
        /*
            r0 = 0
            dji.thirdparty.rx.internal.operators.OperatorSwitch r0 = dji.thirdparty.rx.internal.operators.OperatorSwitch.instance(r0)
            dji.thirdparty.rx.Observable r0 = r1.lift(r0)
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: dji.thirdparty.rx.Observable.switchOnNext(dji.thirdparty.rx.Observable):dji.thirdparty.rx.Observable");
    }

    /* JADX WARN: Type inference failed for: r1v0, types: [dji.thirdparty.rx.Observable, dji.thirdparty.rx.Observable<? extends dji.thirdparty.rx.Observable<? extends T>>], assign insn: null */
    /* JADX WARNING: Unknown variable types count: 1 */
    @dji.thirdparty.rx.annotations.Experimental
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static <T> dji.thirdparty.rx.Observable<T> switchOnNextDelayError(dji.thirdparty.rx.Observable<? extends dji.thirdparty.rx.Observable<? extends T>> r1) {
        /*
            r0 = 1
            dji.thirdparty.rx.internal.operators.OperatorSwitch r0 = dji.thirdparty.rx.internal.operators.OperatorSwitch.instance(r0)
            dji.thirdparty.rx.Observable r0 = r1.lift(r0)
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: dji.thirdparty.rx.Observable.switchOnNextDelayError(dji.thirdparty.rx.Observable):dji.thirdparty.rx.Observable");
    }

    @Deprecated
    public static Observable<Long> timer(long initialDelay, long period, TimeUnit unit) {
        return interval(initialDelay, period, unit, Schedulers.computation());
    }

    @Deprecated
    public static Observable<Long> timer(long initialDelay, long period, TimeUnit unit, Scheduler scheduler) {
        return interval(initialDelay, period, unit, scheduler);
    }

    public static Observable<Long> timer(long delay, TimeUnit unit) {
        return timer(delay, unit, Schedulers.computation());
    }

    public static Observable<Long> timer(long delay, TimeUnit unit, Scheduler scheduler) {
        return create(new OnSubscribeTimerOnce(delay, unit, scheduler));
    }

    public static <T, Resource> Observable<T> using(Func0<Resource> resourceFactory, Func1<? super Resource, ? extends Observable<? extends T>> observableFactory, Action1<? super Resource> disposeAction) {
        return using(resourceFactory, observableFactory, disposeAction, false);
    }

    @Experimental
    public static <T, Resource> Observable<T> using(Func0<Resource> resourceFactory, Func1<? super Resource, ? extends Observable<? extends T>> observableFactory, Action1<? super Resource> disposeAction, boolean disposeEagerly) {
        return create(new OnSubscribeUsing(resourceFactory, observableFactory, disposeAction, disposeEagerly));
    }

    public static <R> Observable<R> zip(Iterable<? extends Observable<?>> ws, FuncN<? extends R> zipFunction) {
        List<Observable<?>> os = new ArrayList<>();
        for (Observable<?> o : ws) {
            os.add(o);
        }
        return just(os.toArray(new Observable[os.size()])).lift(new OperatorZip(zipFunction));
    }

    public static <R> Observable<R> zip(Observable<? extends Observable<?>> ws, FuncN<? extends R> zipFunction) {
        return ws.toList().map(new Func1<List<? extends Observable<?>>, Observable<?>[]>() {
            /* class dji.thirdparty.rx.Observable.AnonymousClass4 */

            public /* bridge */ /* synthetic */ Object call(Object x0) {
                return call((List<? extends Observable<?>>) ((List) x0));
            }

            public Observable<?>[] call(List<? extends Observable<?>> o) {
                return (Observable[]) o.toArray(new Observable[o.size()]);
            }
        }).lift(new OperatorZip(zipFunction));
    }

    public static <T1, T2, R> Observable<R> zip(Observable<? extends T1> o1, Observable<? extends T2> o2, Func2<? super T1, ? super T2, ? extends R> zipFunction) {
        return just(new Observable[]{o1, o2}).lift(new OperatorZip(zipFunction));
    }

    public static <T1, T2, T3, R> Observable<R> zip(Observable<? extends T1> o1, Observable<? extends T2> o2, Observable<? extends T3> o3, Func3<? super T1, ? super T2, ? super T3, ? extends R> zipFunction) {
        return just(new Observable[]{o1, o2, o3}).lift(new OperatorZip(zipFunction));
    }

    public static <T1, T2, T3, T4, R> Observable<R> zip(Observable<? extends T1> o1, Observable<? extends T2> o2, Observable<? extends T3> o3, Observable<? extends T4> o4, Func4<? super T1, ? super T2, ? super T3, ? super T4, ? extends R> zipFunction) {
        return just(new Observable[]{o1, o2, o3, o4}).lift(new OperatorZip(zipFunction));
    }

    public static <T1, T2, T3, T4, T5, R> Observable<R> zip(Observable<? extends T1> o1, Observable<? extends T2> o2, Observable<? extends T3> o3, Observable<? extends T4> o4, Observable<? extends T5> o5, Func5<? super T1, ? super T2, ? super T3, ? super T4, ? super T5, ? extends R> zipFunction) {
        return just(new Observable[]{o1, o2, o3, o4, o5}).lift(new OperatorZip(zipFunction));
    }

    public static <T1, T2, T3, T4, T5, T6, R> Observable<R> zip(Observable<? extends T1> o1, Observable<? extends T2> o2, Observable<? extends T3> o3, Observable<? extends T4> o4, Observable<? extends T5> o5, Observable<? extends T6> o6, Func6<? super T1, ? super T2, ? super T3, ? super T4, ? super T5, ? super T6, ? extends R> zipFunction) {
        return just(new Observable[]{o1, o2, o3, o4, o5, o6}).lift(new OperatorZip(zipFunction));
    }

    public static <T1, T2, T3, T4, T5, T6, T7, R> Observable<R> zip(Observable<? extends T1> o1, Observable<? extends T2> o2, Observable<? extends T3> o3, Observable<? extends T4> o4, Observable<? extends T5> o5, Observable<? extends T6> o6, Observable<? extends T7> o7, Func7<? super T1, ? super T2, ? super T3, ? super T4, ? super T5, ? super T6, ? super T7, ? extends R> zipFunction) {
        return just(new Observable[]{o1, o2, o3, o4, o5, o6, o7}).lift(new OperatorZip(zipFunction));
    }

    public static <T1, T2, T3, T4, T5, T6, T7, T8, R> Observable<R> zip(Observable<? extends T1> o1, Observable<? extends T2> o2, Observable<? extends T3> o3, Observable<? extends T4> o4, Observable<? extends T5> o5, Observable<? extends T6> o6, Observable<? extends T7> o7, Observable<? extends T8> o8, Func8<? super T1, ? super T2, ? super T3, ? super T4, ? super T5, ? super T6, ? super T7, ? super T8, ? extends R> zipFunction) {
        return just(new Observable[]{o1, o2, o3, o4, o5, o6, o7, o8}).lift(new OperatorZip(zipFunction));
    }

    public static <T1, T2, T3, T4, T5, T6, T7, T8, T9, R> Observable<R> zip(Observable<? extends T1> o1, Observable<? extends T2> o2, Observable<? extends T3> o3, Observable<? extends T4> o4, Observable<? extends T5> o5, Observable<? extends T6> o6, Observable<? extends T7> o7, Observable<? extends T8> o8, Observable<? extends T9> o9, Func9<? super T1, ? super T2, ? super T3, ? super T4, ? super T5, ? super T6, ? super T7, ? super T8, ? super T9, ? extends R> zipFunction) {
        return just(new Observable[]{o1, o2, o3, o4, o5, o6, o7, o8, o9}).lift(new OperatorZip(zipFunction));
    }

    public final Observable<Boolean> all(Func1<? super T, Boolean> predicate) {
        return lift(new OperatorAll(predicate));
    }

    public final Observable<T> ambWith(Observable<? extends T> t1) {
        return amb(this, t1);
    }

    public final Observable<T> asObservable() {
        return lift(OperatorAsObservable.instance());
    }

    public final <TClosing> Observable<List<T>> buffer(Func0<? extends Observable<? extends TClosing>> bufferClosingSelector) {
        return lift(new OperatorBufferWithSingleObservable(bufferClosingSelector, 16));
    }

    public final Observable<List<T>> buffer(int count) {
        return buffer(count, count);
    }

    public final Observable<List<T>> buffer(int count, int skip) {
        return lift(new OperatorBufferWithSize(count, skip));
    }

    public final Observable<List<T>> buffer(long timespan, long timeshift, TimeUnit unit) {
        return buffer(timespan, timeshift, unit, Schedulers.computation());
    }

    public final Observable<List<T>> buffer(long timespan, long timeshift, TimeUnit unit, Scheduler scheduler) {
        return lift(new OperatorBufferWithTime(timespan, timeshift, unit, Integer.MAX_VALUE, scheduler));
    }

    public final Observable<List<T>> buffer(long timespan, TimeUnit unit) {
        return buffer(timespan, unit, Integer.MAX_VALUE, Schedulers.computation());
    }

    public final Observable<List<T>> buffer(long timespan, TimeUnit unit, int count) {
        return lift(new OperatorBufferWithTime(timespan, timespan, unit, count, Schedulers.computation()));
    }

    public final Observable<List<T>> buffer(long timespan, TimeUnit unit, int count, Scheduler scheduler) {
        return lift(new OperatorBufferWithTime(timespan, timespan, unit, count, scheduler));
    }

    public final Observable<List<T>> buffer(long timespan, TimeUnit unit, Scheduler scheduler) {
        return buffer(timespan, timespan, unit, scheduler);
    }

    public final <TOpening, TClosing> Observable<List<T>> buffer(Observable<? extends TOpening> bufferOpenings, Func1<? super TOpening, ? extends Observable<? extends TClosing>> bufferClosingSelector) {
        return lift(new OperatorBufferWithStartEndObservable(bufferOpenings, bufferClosingSelector));
    }

    /* JADX WARN: Type inference failed for: r2v0, types: [dji.thirdparty.rx.Observable<B>, dji.thirdparty.rx.Observable], assign insn: null */
    /* JADX WARNING: Unknown variable types count: 1 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final <B> dji.thirdparty.rx.Observable<java.util.List<T>> buffer(dji.thirdparty.rx.Observable<B> r2) {
        /*
            r1 = this;
            r0 = 16
            dji.thirdparty.rx.Observable r0 = r1.buffer(r2, r0)
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: dji.thirdparty.rx.Observable.buffer(dji.thirdparty.rx.Observable):dji.thirdparty.rx.Observable");
    }

    public final <B> Observable<List<T>> buffer(Observable<B> boundary, int initialCapacity) {
        return lift(new OperatorBufferWithSingleObservable(boundary, initialCapacity));
    }

    public final Observable<T> cache() {
        return CachedObservable.from(this);
    }

    @Deprecated
    public final Observable<T> cache(int initialCapacity) {
        return cacheWithInitialCapacity(initialCapacity);
    }

    public final Observable<T> cacheWithInitialCapacity(int initialCapacity) {
        return CachedObservable.from(this, initialCapacity);
    }

    public final <R> Observable<R> cast(Class<R> klass) {
        return lift(new OperatorCast(klass));
    }

    /* JADX DEBUG: Failed to find minimal casts for resolve overloaded methods, cast all args instead
     method: dji.thirdparty.rx.internal.operators.OperatorScan.<init>(dji.thirdparty.rx.functions.Func0, dji.thirdparty.rx.functions.Func2):void
     arg types: [dji.thirdparty.rx.functions.Func0<R>, dji.thirdparty.rx.functions.Func2<R, T, R>]
     candidates:
      dji.thirdparty.rx.internal.operators.OperatorScan.<init>(java.lang.Object, dji.thirdparty.rx.functions.Func2):void
      dji.thirdparty.rx.internal.operators.OperatorScan.<init>(dji.thirdparty.rx.functions.Func0, dji.thirdparty.rx.functions.Func2):void */
    public final <R> Observable<R> collect(Func0<R> stateFactory, final Action2<R, ? super T> collector) {
        return lift(new OperatorScan((Func0) stateFactory, (Func2) new Func2<R, T, R>() {
            /* class dji.thirdparty.rx.Observable.AnonymousClass5 */

            public final R call(R state, T value) {
                collector.call(state, value);
                return state;
            }
        })).last();
    }

    public final <R> Observable<R> concatMap(Func1<? super T, ? extends Observable<? extends R>> func) {
        if (this instanceof ScalarSynchronousObservable) {
            return ((ScalarSynchronousObservable) this).scalarFlatMap(func);
        }
        return create(new OnSubscribeConcatMap(this, func, 2, 0));
    }

    @Experimental
    public final <R> Observable<R> concatMapDelayError(Func1<? super T, ? extends Observable<? extends R>> func) {
        if (this instanceof ScalarSynchronousObservable) {
            return ((ScalarSynchronousObservable) this).scalarFlatMap(func);
        }
        return create(new OnSubscribeConcatMap(this, func, 2, 2));
    }

    public final <R> Observable<R> concatMapIterable(Func1<? super T, ? extends Iterable<? extends R>> collectionSelector) {
        return concat(map(OperatorMapPair.convertSelector(collectionSelector)));
    }

    public final Observable<T> concatWith(Observable<? extends T> t1) {
        return concat(this, t1);
    }

    public final Observable<Boolean> contains(final Object element) {
        return exists(new Func1<T, Boolean>() {
            /* class dji.thirdparty.rx.Observable.AnonymousClass6 */

            public final Boolean call(T t1) {
                return Boolean.valueOf(element == null ? t1 == null : element.equals(t1));
            }
        });
    }

    public final Observable<Integer> count() {
        return reduce(0, CountHolder.INSTANCE);
    }

    private static final class CountHolder {
        static final Func2<Integer, Object, Integer> INSTANCE = new Func2<Integer, Object, Integer>() {
            /* class dji.thirdparty.rx.Observable.CountHolder.AnonymousClass1 */

            public final Integer call(Integer count, Object o) {
                return Integer.valueOf(count.intValue() + 1);
            }
        };

        private CountHolder() {
        }
    }

    public final Observable<Long> countLong() {
        return reduce(0L, CountLongHolder.INSTANCE);
    }

    private static final class CountLongHolder {
        static final Func2<Long, Object, Long> INSTANCE = new Func2<Long, Object, Long>() {
            /* class dji.thirdparty.rx.Observable.CountLongHolder.AnonymousClass1 */

            public final Long call(Long count, Object o) {
                return Long.valueOf(count.longValue() + 1);
            }
        };

        private CountLongHolder() {
        }
    }

    public final <U> Observable<T> debounce(Func1<? super T, ? extends Observable<U>> debounceSelector) {
        return lift(new OperatorDebounceWithSelector(debounceSelector));
    }

    public final Observable<T> debounce(long timeout, TimeUnit unit) {
        return debounce(timeout, unit, Schedulers.computation());
    }

    public final Observable<T> debounce(long timeout, TimeUnit unit, Scheduler scheduler) {
        return lift(new OperatorDebounceWithTime(timeout, unit, scheduler));
    }

    public final Observable<T> defaultIfEmpty(final T defaultValue) {
        return switchIfEmpty(create(new OnSubscribe<T>() {
            /* class dji.thirdparty.rx.Observable.AnonymousClass7 */

            public /* bridge */ /* synthetic */ void call(Object x0) {
                call((Subscriber) ((Subscriber) x0));
            }

            public void call(Subscriber<? super T> subscriber) {
                subscriber.setProducer(new SingleProducer(subscriber, defaultValue));
            }
        }));
    }

    public final Observable<T> switchIfEmpty(Observable<? extends T> alternate) {
        return lift(new OperatorSwitchIfEmpty(alternate));
    }

    /* JADX WARN: Type inference failed for: r3v0, types: [dji.thirdparty.rx.functions.Func0, dji.thirdparty.rx.functions.Func0<? extends dji.thirdparty.rx.Observable<U>>], assign insn: null */
    /* JADX WARNING: Unknown variable types count: 1 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final <U, V> dji.thirdparty.rx.Observable<T> delay(dji.thirdparty.rx.functions.Func0<? extends dji.thirdparty.rx.Observable<U>> r3, dji.thirdparty.rx.functions.Func1<? super T, ? extends dji.thirdparty.rx.Observable<V>> r4) {
        /*
            r2 = this;
            dji.thirdparty.rx.Observable r0 = r2.delaySubscription(r3)
            dji.thirdparty.rx.internal.operators.OperatorDelayWithSelector r1 = new dji.thirdparty.rx.internal.operators.OperatorDelayWithSelector
            r1.<init>(r2, r4)
            dji.thirdparty.rx.Observable r0 = r0.lift(r1)
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: dji.thirdparty.rx.Observable.delay(dji.thirdparty.rx.functions.Func0, dji.thirdparty.rx.functions.Func1):dji.thirdparty.rx.Observable");
    }

    public final <U> Observable<T> delay(Func1<? super T, ? extends Observable<U>> itemDelay) {
        return lift(new OperatorDelayWithSelector(this, itemDelay));
    }

    public final Observable<T> delay(long delay, TimeUnit unit) {
        return delay(delay, unit, Schedulers.computation());
    }

    public final Observable<T> delay(long delay, TimeUnit unit, Scheduler scheduler) {
        return lift(new OperatorDelay(delay, unit, scheduler));
    }

    public final Observable<T> delaySubscription(long delay, TimeUnit unit) {
        return delaySubscription(delay, unit, Schedulers.computation());
    }

    public final Observable<T> delaySubscription(long delay, TimeUnit unit, Scheduler scheduler) {
        return create(new OnSubscribeDelaySubscription(this, delay, unit, scheduler));
    }

    public final <U> Observable<T> delaySubscription(Func0<? extends Observable<U>> subscriptionDelay) {
        return create(new OnSubscribeDelaySubscriptionWithSelector(this, subscriptionDelay));
    }

    @Experimental
    public final <U> Observable<T> delaySubscription(Observable<U> other) {
        if (other != null) {
            return create(new OnSubscribeDelaySubscriptionOther(this, other));
        }
        throw new NullPointerException();
    }

    public final <T2> Observable<T2> dematerialize() {
        return lift(OperatorDematerialize.instance());
    }

    public final Observable<T> distinct() {
        return lift(OperatorDistinct.instance());
    }

    public final <U> Observable<T> distinct(Func1<? super T, ? extends U> keySelector) {
        return lift(new OperatorDistinct(keySelector));
    }

    public final Observable<T> distinctUntilChanged() {
        return lift(OperatorDistinctUntilChanged.instance());
    }

    public final <U> Observable<T> distinctUntilChanged(Func1<? super T, ? extends U> keySelector) {
        return lift(new OperatorDistinctUntilChanged(keySelector));
    }

    public final Observable<T> doOnCompleted(final Action0 onCompleted) {
        return lift(new OperatorDoOnEach(new Observer<T>() {
            /* class dji.thirdparty.rx.Observable.AnonymousClass8 */

            public final void onCompleted() {
                onCompleted.call();
            }

            public final void onError(Throwable e) {
            }

            public final void onNext(T t) {
            }
        }));
    }

    public final Observable<T> doOnEach(final Action1<Notification<? super T>> onNotification) {
        return lift(new OperatorDoOnEach(new Observer<T>() {
            /* class dji.thirdparty.rx.Observable.AnonymousClass9 */

            public final void onCompleted() {
                onNotification.call(Notification.createOnCompleted());
            }

            public final void onError(Throwable e) {
                onNotification.call(Notification.createOnError(e));
            }

            public final void onNext(T v) {
                onNotification.call(Notification.createOnNext(v));
            }
        }));
    }

    public final Observable<T> doOnEach(Observer<? super T> observer) {
        return lift(new OperatorDoOnEach(observer));
    }

    public final Observable<T> doOnError(final Action1<Throwable> onError) {
        return lift(new OperatorDoOnEach(new Observer<T>() {
            /* class dji.thirdparty.rx.Observable.AnonymousClass10 */

            public final void onCompleted() {
            }

            public final void onError(Throwable e) {
                onError.call(e);
            }

            public final void onNext(T t) {
            }
        }));
    }

    public final Observable<T> doOnNext(final Action1<? super T> onNext) {
        return lift(new OperatorDoOnEach(new Observer<T>() {
            /* class dji.thirdparty.rx.Observable.AnonymousClass11 */

            public final void onCompleted() {
            }

            public final void onError(Throwable e) {
            }

            public final void onNext(T args) {
                onNext.call(args);
            }
        }));
    }

    @Beta
    public final Observable<T> doOnRequest(Action1<Long> onRequest) {
        return lift(new OperatorDoOnRequest(onRequest));
    }

    public final Observable<T> doOnSubscribe(Action0 subscribe) {
        return lift(new OperatorDoOnSubscribe(subscribe));
    }

    public final Observable<T> doOnTerminate(final Action0 onTerminate) {
        return lift(new OperatorDoOnEach(new Observer<T>() {
            /* class dji.thirdparty.rx.Observable.AnonymousClass12 */

            public final void onCompleted() {
                onTerminate.call();
            }

            public final void onError(Throwable e) {
                onTerminate.call();
            }

            public final void onNext(T t) {
            }
        }));
    }

    public final Observable<T> doOnUnsubscribe(Action0 unsubscribe) {
        return lift(new OperatorDoOnUnsubscribe(unsubscribe));
    }

    @Experimental
    public static <T> Observable<T> concatEager(Observable<? extends T> o1, Observable<? extends T> o2) {
        return concatEager(Arrays.asList(o1, o2));
    }

    @Experimental
    public static <T> Observable<T> concatEager(Observable<? extends T> o1, Observable<? extends T> o2, Observable<? extends T> o3) {
        return concatEager(Arrays.asList(o1, o2, o3));
    }

    @Experimental
    public static <T> Observable<T> concatEager(Observable<? extends T> o1, Observable<? extends T> o2, Observable<? extends T> o3, Observable<? extends T> o4) {
        return concatEager(Arrays.asList(o1, o2, o3, o4));
    }

    @Experimental
    public static <T> Observable<T> concatEager(Observable<? extends T> o1, Observable<? extends T> o2, Observable<? extends T> o3, Observable<? extends T> o4, Observable<? extends T> o5) {
        return concatEager(Arrays.asList(o1, o2, o3, o4, o5));
    }

    @Experimental
    public static <T> Observable<T> concatEager(Observable<? extends T> o1, Observable<? extends T> o2, Observable<? extends T> o3, Observable<? extends T> o4, Observable<? extends T> o5, Observable<? extends T> o6) {
        return concatEager(Arrays.asList(o1, o2, o3, o4, o5, o6));
    }

    @Experimental
    public static <T> Observable<T> concatEager(Observable<? extends T> o1, Observable<? extends T> o2, Observable<? extends T> o3, Observable<? extends T> o4, Observable<? extends T> o5, Observable<? extends T> o6, Observable<? extends T> o7) {
        return concatEager(Arrays.asList(o1, o2, o3, o4, o5, o6, o7));
    }

    @Experimental
    public static <T> Observable<T> concatEager(Observable<? extends T> o1, Observable<? extends T> o2, Observable<? extends T> o3, Observable<? extends T> o4, Observable<? extends T> o5, Observable<? extends T> o6, Observable<? extends T> o7, Observable<? extends T> o8) {
        return concatEager(Arrays.asList(o1, o2, o3, o4, o5, o6, o7, o8));
    }

    @Experimental
    public static <T> Observable<T> concatEager(Observable<? extends T> o1, Observable<? extends T> o2, Observable<? extends T> o3, Observable<? extends T> o4, Observable<? extends T> o5, Observable<? extends T> o6, Observable<? extends T> o7, Observable<? extends T> o8, Observable<? extends T> o9) {
        return concatEager(Arrays.asList(o1, o2, o3, o4, o5, o6, o7, o8, o9));
    }

    /* JADX WARN: Type inference failed for: r2v0, types: [java.lang.Iterable<? extends dji.thirdparty.rx.Observable<? extends T>>, java.lang.Iterable], assign insn: null */
    /* JADX WARNING: Unknown variable types count: 1 */
    @dji.thirdparty.rx.annotations.Experimental
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static <T> dji.thirdparty.rx.Observable<T> concatEager(java.lang.Iterable<? extends dji.thirdparty.rx.Observable<? extends T>> r2) {
        /*
            dji.thirdparty.rx.Observable r0 = from(r2)
            dji.thirdparty.rx.functions.Func1 r1 = dji.thirdparty.rx.internal.util.UtilityFunctions.identity()
            dji.thirdparty.rx.Observable r0 = r0.concatMapEager(r1)
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: dji.thirdparty.rx.Observable.concatEager(java.lang.Iterable):dji.thirdparty.rx.Observable");
    }

    /* JADX WARN: Type inference failed for: r2v0, types: [java.lang.Iterable<? extends dji.thirdparty.rx.Observable<? extends T>>, java.lang.Iterable], assign insn: null */
    /* JADX WARNING: Unknown variable types count: 1 */
    @dji.thirdparty.rx.annotations.Experimental
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static <T> dji.thirdparty.rx.Observable<T> concatEager(java.lang.Iterable<? extends dji.thirdparty.rx.Observable<? extends T>> r2, int r3) {
        /*
            dji.thirdparty.rx.Observable r0 = from(r2)
            dji.thirdparty.rx.functions.Func1 r1 = dji.thirdparty.rx.internal.util.UtilityFunctions.identity()
            dji.thirdparty.rx.Observable r0 = r0.concatMapEager(r1, r3)
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: dji.thirdparty.rx.Observable.concatEager(java.lang.Iterable, int):dji.thirdparty.rx.Observable");
    }

    /* JADX WARN: Type inference failed for: r1v0, types: [dji.thirdparty.rx.Observable, dji.thirdparty.rx.Observable<? extends dji.thirdparty.rx.Observable<? extends T>>], assign insn: null */
    /* JADX WARNING: Unknown variable types count: 1 */
    @dji.thirdparty.rx.annotations.Experimental
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static <T> dji.thirdparty.rx.Observable<T> concatEager(dji.thirdparty.rx.Observable<? extends dji.thirdparty.rx.Observable<? extends T>> r1) {
        /*
            dji.thirdparty.rx.functions.Func1 r0 = dji.thirdparty.rx.internal.util.UtilityFunctions.identity()
            dji.thirdparty.rx.Observable r0 = r1.concatMapEager(r0)
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: dji.thirdparty.rx.Observable.concatEager(dji.thirdparty.rx.Observable):dji.thirdparty.rx.Observable");
    }

    /* JADX WARN: Type inference failed for: r1v0, types: [dji.thirdparty.rx.Observable, dji.thirdparty.rx.Observable<? extends dji.thirdparty.rx.Observable<? extends T>>], assign insn: null */
    /* JADX WARNING: Unknown variable types count: 1 */
    @dji.thirdparty.rx.annotations.Experimental
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static <T> dji.thirdparty.rx.Observable<T> concatEager(dji.thirdparty.rx.Observable<? extends dji.thirdparty.rx.Observable<? extends T>> r1, int r2) {
        /*
            dji.thirdparty.rx.functions.Func1 r0 = dji.thirdparty.rx.internal.util.UtilityFunctions.identity()
            dji.thirdparty.rx.Observable r0 = r1.concatMapEager(r0, r2)
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: dji.thirdparty.rx.Observable.concatEager(dji.thirdparty.rx.Observable, int):dji.thirdparty.rx.Observable");
    }

    @Experimental
    public final <R> Observable<R> concatMapEager(Func1<? super T, ? extends Observable<? extends R>> mapper) {
        return concatMapEager(mapper, RxRingBuffer.SIZE);
    }

    @Experimental
    public final <R> Observable<R> concatMapEager(Func1<? super T, ? extends Observable<? extends R>> mapper, int capacityHint) {
        if (capacityHint >= 1) {
            return lift(new OperatorEagerConcatMap(mapper, capacityHint, Integer.MAX_VALUE));
        }
        throw new IllegalArgumentException("capacityHint > 0 required but it was " + capacityHint);
    }

    @Experimental
    public final <R> Observable<R> concatMapEager(Func1<? super T, ? extends Observable<? extends R>> mapper, int capacityHint, int maxConcurrent) {
        if (capacityHint < 1) {
            throw new IllegalArgumentException("capacityHint > 0 required but it was " + capacityHint);
        } else if (maxConcurrent >= 1) {
            return lift(new OperatorEagerConcatMap(mapper, capacityHint, maxConcurrent));
        } else {
            throw new IllegalArgumentException("maxConcurrent > 0 required but it was " + capacityHint);
        }
    }

    public final Observable<T> elementAt(int index) {
        return lift(new OperatorElementAt(index));
    }

    public final Observable<T> elementAtOrDefault(int index, T defaultValue) {
        return lift(new OperatorElementAt(index, defaultValue));
    }

    public final Observable<Boolean> exists(Func1<? super T, Boolean> predicate) {
        return lift(new OperatorAny(predicate, false));
    }

    public final Observable<T> filter(Func1<? super T, Boolean> predicate) {
        return lift(new OperatorFilter(predicate));
    }

    @Deprecated
    public final Observable<T> finallyDo(Action0 action) {
        return lift(new OperatorDoAfterTerminate(action));
    }

    public final Observable<T> doAfterTerminate(Action0 action) {
        return lift(new OperatorDoAfterTerminate(action));
    }

    public final Observable<T> first() {
        return take(1).single();
    }

    public final Observable<T> first(Func1<? super T, Boolean> predicate) {
        return takeFirst(predicate).single();
    }

    public final Observable<T> firstOrDefault(T defaultValue) {
        return take(1).singleOrDefault(defaultValue);
    }

    public final Observable<T> firstOrDefault(T defaultValue, Func1<? super T, Boolean> predicate) {
        return takeFirst(predicate).singleOrDefault(defaultValue);
    }

    public final <R> Observable<R> flatMap(Func1<? super T, ? extends Observable<? extends R>> func) {
        if (getClass() == ScalarSynchronousObservable.class) {
            return ((ScalarSynchronousObservable) this).scalarFlatMap(func);
        }
        return merge(map(func));
    }

    @Beta
    public final <R> Observable<R> flatMap(Func1<? super T, ? extends Observable<? extends R>> func, int maxConcurrent) {
        if (getClass() == ScalarSynchronousObservable.class) {
            return ((ScalarSynchronousObservable) this).scalarFlatMap(func);
        }
        return merge(map(func), maxConcurrent);
    }

    public final <R> Observable<R> flatMap(Func1<? super T, ? extends Observable<? extends R>> onNext, Func1<? super Throwable, ? extends Observable<? extends R>> onError, Func0<? extends Observable<? extends R>> onCompleted) {
        return merge(mapNotification(onNext, onError, onCompleted));
    }

    @Beta
    public final <R> Observable<R> flatMap(Func1<? super T, ? extends Observable<? extends R>> onNext, Func1<? super Throwable, ? extends Observable<? extends R>> onError, Func0<? extends Observable<? extends R>> onCompleted, int maxConcurrent) {
        return merge(mapNotification(onNext, onError, onCompleted), maxConcurrent);
    }

    public final <U, R> Observable<R> flatMap(Func1<? super T, ? extends Observable<? extends U>> collectionSelector, Func2<? super T, ? super U, ? extends R> resultSelector) {
        return merge(lift(new OperatorMapPair(collectionSelector, resultSelector)));
    }

    @Beta
    public final <U, R> Observable<R> flatMap(Func1<? super T, ? extends Observable<? extends U>> collectionSelector, Func2<? super T, ? super U, ? extends R> resultSelector, int maxConcurrent) {
        return merge(lift(new OperatorMapPair(collectionSelector, resultSelector)), maxConcurrent);
    }

    public final <R> Observable<R> flatMapIterable(Func1<? super T, ? extends Iterable<? extends R>> collectionSelector) {
        return merge(map(OperatorMapPair.convertSelector(collectionSelector)));
    }

    @Beta
    public final <R> Observable<R> flatMapIterable(Func1<? super T, ? extends Iterable<? extends R>> collectionSelector, int maxConcurrent) {
        return merge(map(OperatorMapPair.convertSelector(collectionSelector)), maxConcurrent);
    }

    public final <U, R> Observable<R> flatMapIterable(Func1<? super T, ? extends Iterable<? extends U>> collectionSelector, Func2<? super T, ? super U, ? extends R> resultSelector) {
        return flatMap(OperatorMapPair.convertSelector(collectionSelector), resultSelector);
    }

    @Beta
    public final <U, R> Observable<R> flatMapIterable(Func1<? super T, ? extends Iterable<? extends U>> collectionSelector, Func2<? super T, ? super U, ? extends R> resultSelector, int maxConcurrent) {
        return flatMap(OperatorMapPair.convertSelector(collectionSelector), resultSelector, maxConcurrent);
    }

    public final void forEach(Action1<? super T> onNext) {
        subscribe(onNext);
    }

    public final void forEach(Action1<? super T> onNext, Action1<Throwable> onError) {
        subscribe(onNext, onError);
    }

    public final void forEach(Action1<? super T> onNext, Action1<Throwable> onError, Action0 onComplete) {
        subscribe(onNext, onError, onComplete);
    }

    public final <K, R> Observable<GroupedObservable<K, R>> groupBy(Func1<? super T, ? extends K> keySelector, Func1<? super T, ? extends R> elementSelector) {
        return lift(new OperatorGroupBy(keySelector, elementSelector));
    }

    public final <K> Observable<GroupedObservable<K, T>> groupBy(Func1<? super T, ? extends K> keySelector) {
        return lift(new OperatorGroupBy(keySelector));
    }

    public final <T2, D1, D2, R> Observable<R> groupJoin(Observable<T2> right, Func1<? super T, ? extends Observable<D1>> leftDuration, Func1<? super T2, ? extends Observable<D2>> rightDuration, Func2<? super T, ? super Observable<T2>, ? extends R> resultSelector) {
        return create(new OnSubscribeGroupJoin(this, right, leftDuration, rightDuration, resultSelector));
    }

    public final Observable<T> ignoreElements() {
        return lift(OperatorIgnoreElements.instance());
    }

    public final Observable<Boolean> isEmpty() {
        return lift(HolderAnyForEmpty.INSTANCE);
    }

    private static class HolderAnyForEmpty {
        static final OperatorAny<?> INSTANCE = new OperatorAny<>(UtilityFunctions.alwaysTrue(), true);

        private HolderAnyForEmpty() {
        }
    }

    public final <TRight, TLeftDuration, TRightDuration, R> Observable<R> join(Observable<TRight> right, Func1<T, Observable<TLeftDuration>> leftDurationSelector, Func1<TRight, Observable<TRightDuration>> rightDurationSelector, Func2<T, TRight, R> resultSelector) {
        return create(new OnSubscribeJoin(this, right, leftDurationSelector, rightDurationSelector, resultSelector));
    }

    public final Observable<T> last() {
        return takeLast(1).single();
    }

    public final Observable<T> last(Func1<? super T, Boolean> predicate) {
        return filter(predicate).takeLast(1).single();
    }

    public final Observable<T> lastOrDefault(T defaultValue) {
        return takeLast(1).singleOrDefault(defaultValue);
    }

    public final Observable<T> lastOrDefault(T defaultValue, Func1<? super T, Boolean> predicate) {
        return filter(predicate).takeLast(1).singleOrDefault(defaultValue);
    }

    public final Observable<T> limit(int count) {
        return take(count);
    }

    public final <R> Observable<R> map(Func1<? super T, ? extends R> func) {
        return lift(new OperatorMap(func));
    }

    private <R> Observable<R> mapNotification(Func1<? super T, ? extends R> onNext, Func1<? super Throwable, ? extends R> onError, Func0<? extends R> onCompleted) {
        return lift(new OperatorMapNotification(onNext, onError, onCompleted));
    }

    public final Observable<Notification<T>> materialize() {
        return lift(OperatorMaterialize.instance());
    }

    public final Observable<T> mergeWith(Observable<? extends T> t1) {
        return merge(this, t1);
    }

    public final Observable<T> observeOn(Scheduler scheduler) {
        return observeOn(scheduler, RxRingBuffer.SIZE);
    }

    public final Observable<T> observeOn(Scheduler scheduler, int bufferSize) {
        return observeOn(scheduler, false, bufferSize);
    }

    public final Observable<T> observeOn(Scheduler scheduler, boolean delayError) {
        return observeOn(scheduler, delayError, RxRingBuffer.SIZE);
    }

    public final Observable<T> observeOn(Scheduler scheduler, boolean delayError, int bufferSize) {
        if (this instanceof ScalarSynchronousObservable) {
            return ((ScalarSynchronousObservable) this).scalarScheduleOn(scheduler);
        }
        return lift(new OperatorObserveOn(scheduler, delayError, bufferSize));
    }

    public final <R> Observable<R> ofType(final Class<R> klass) {
        return filter(new Func1<T, Boolean>() {
            /* class dji.thirdparty.rx.Observable.AnonymousClass13 */

            public final Boolean call(T t) {
                return Boolean.valueOf(klass.isInstance(t));
            }
        }).cast(klass);
    }

    public final Observable<T> onBackpressureBuffer() {
        return lift(OperatorOnBackpressureBuffer.instance());
    }

    public final Observable<T> onBackpressureBuffer(long capacity) {
        return lift(new OperatorOnBackpressureBuffer(capacity));
    }

    public final Observable<T> onBackpressureBuffer(long capacity, Action0 onOverflow) {
        return lift(new OperatorOnBackpressureBuffer(capacity, onOverflow));
    }

    @Experimental
    public final Observable<T> onBackpressureBuffer(long capacity, Action0 onOverflow, BackpressureOverflow.Strategy overflowStrategy) {
        return lift(new OperatorOnBackpressureBuffer(capacity, onOverflow, overflowStrategy));
    }

    public final Observable<T> onBackpressureDrop(Action1<? super T> onDrop) {
        return lift(new OperatorOnBackpressureDrop(onDrop));
    }

    public final Observable<T> onBackpressureDrop() {
        return lift(OperatorOnBackpressureDrop.instance());
    }

    public final Observable<T> onBackpressureLatest() {
        return lift(OperatorOnBackpressureLatest.instance());
    }

    public final Observable<T> onErrorResumeNext(Func1<Throwable, ? extends Observable<? extends T>> resumeFunction) {
        return lift(new OperatorOnErrorResumeNextViaFunction(resumeFunction));
    }

    /* JADX WARN: Type inference failed for: r2v0, types: [dji.thirdparty.rx.Observable, dji.thirdparty.rx.Observable<? extends T>], assign insn: null */
    /* JADX WARNING: Unknown variable types count: 1 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final dji.thirdparty.rx.Observable<T> onErrorResumeNext(dji.thirdparty.rx.Observable<? extends T> r2) {
        /*
            r1 = this;
            dji.thirdparty.rx.internal.operators.OperatorOnErrorResumeNextViaFunction r0 = dji.thirdparty.rx.internal.operators.OperatorOnErrorResumeNextViaFunction.withOther(r2)
            dji.thirdparty.rx.Observable r0 = r1.lift(r0)
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: dji.thirdparty.rx.Observable.onErrorResumeNext(dji.thirdparty.rx.Observable):dji.thirdparty.rx.Observable");
    }

    public final Observable<T> onErrorReturn(Func1<Throwable, ? extends T> resumeFunction) {
        return lift(OperatorOnErrorResumeNextViaFunction.withSingle(resumeFunction));
    }

    /* JADX WARN: Type inference failed for: r2v0, types: [dji.thirdparty.rx.Observable, dji.thirdparty.rx.Observable<? extends T>], assign insn: null */
    /* JADX WARNING: Unknown variable types count: 1 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final dji.thirdparty.rx.Observable<T> onExceptionResumeNext(dji.thirdparty.rx.Observable<? extends T> r2) {
        /*
            r1 = this;
            dji.thirdparty.rx.internal.operators.OperatorOnErrorResumeNextViaFunction r0 = dji.thirdparty.rx.internal.operators.OperatorOnErrorResumeNextViaFunction.withException(r2)
            dji.thirdparty.rx.Observable r0 = r1.lift(r0)
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: dji.thirdparty.rx.Observable.onExceptionResumeNext(dji.thirdparty.rx.Observable):dji.thirdparty.rx.Observable");
    }

    public final ConnectableObservable<T> publish() {
        return OperatorPublish.create(this);
    }

    public final <R> Observable<R> publish(Func1<? super Observable<T>, ? extends Observable<R>> selector) {
        return OperatorPublish.create(this, selector);
    }

    public final Observable<T> reduce(Func2<T, T, T> accumulator) {
        return scan(accumulator).last();
    }

    public final <R> Observable<R> reduce(R initialValue, Func2<R, ? super T, R> accumulator) {
        return scan(initialValue, accumulator).takeLast(1);
    }

    public final Observable<T> repeat() {
        return OnSubscribeRedo.repeat(this);
    }

    public final Observable<T> repeat(Scheduler scheduler) {
        return OnSubscribeRedo.repeat(this, scheduler);
    }

    public final Observable<T> repeat(long count) {
        return OnSubscribeRedo.repeat(this, count);
    }

    public final Observable<T> repeat(long count, Scheduler scheduler) {
        return OnSubscribeRedo.repeat(this, count, scheduler);
    }

    public final Observable<T> repeatWhen(final Func1<? super Observable<? extends Void>, ? extends Observable<?>> notificationHandler, Scheduler scheduler) {
        return OnSubscribeRedo.repeat(this, new Func1<Observable<? extends Notification<?>>, Observable<?>>() {
            /* class dji.thirdparty.rx.Observable.AnonymousClass14 */

            public /* bridge */ /* synthetic */ Object call(Object x0) {
                return call((Observable<? extends Notification<?>>) ((Observable) x0));
            }

            public Observable<?> call(Observable<? extends Notification<?>> notifications) {
                return (Observable) notificationHandler.call(notifications.map(new Func1<Notification<?>, Void>() {
                    /* class dji.thirdparty.rx.Observable.AnonymousClass14.AnonymousClass1 */

                    public /* bridge */ /* synthetic */ Object call(Object x0) {
                        return call((Notification<?>) ((Notification) x0));
                    }

                    public Void call(Notification<?> notification) {
                        return null;
                    }
                }));
            }
        }, scheduler);
    }

    public final Observable<T> repeatWhen(final Func1<? super Observable<? extends Void>, ? extends Observable<?>> notificationHandler) {
        return OnSubscribeRedo.repeat(this, new Func1<Observable<? extends Notification<?>>, Observable<?>>() {
            /* class dji.thirdparty.rx.Observable.AnonymousClass15 */

            public /* bridge */ /* synthetic */ Object call(Object x0) {
                return call((Observable<? extends Notification<?>>) ((Observable) x0));
            }

            public Observable<?> call(Observable<? extends Notification<?>> notifications) {
                return (Observable) notificationHandler.call(notifications.map(new Func1<Notification<?>, Void>() {
                    /* class dji.thirdparty.rx.Observable.AnonymousClass15.AnonymousClass1 */

                    public /* bridge */ /* synthetic */ Object call(Object x0) {
                        return call((Notification<?>) ((Notification) x0));
                    }

                    public Void call(Notification<?> notification) {
                        return null;
                    }
                }));
            }
        });
    }

    public final ConnectableObservable<T> replay() {
        return OperatorReplay.create(this);
    }

    public final <R> Observable<R> replay(Func1<? super Observable<T>, ? extends Observable<R>> selector) {
        return OperatorReplay.multicastSelector(new Func0<ConnectableObservable<T>>() {
            /* class dji.thirdparty.rx.Observable.AnonymousClass16 */

            public ConnectableObservable<T> call() {
                return Observable.this.replay();
            }
        }, selector);
    }

    public final <R> Observable<R> replay(Func1<? super Observable<T>, ? extends Observable<R>> selector, final int bufferSize) {
        return OperatorReplay.multicastSelector(new Func0<ConnectableObservable<T>>() {
            /* class dji.thirdparty.rx.Observable.AnonymousClass17 */

            public ConnectableObservable<T> call() {
                return Observable.this.replay(bufferSize);
            }
        }, selector);
    }

    public final <R> Observable<R> replay(Func1<? super Observable<T>, ? extends Observable<R>> selector, int bufferSize, long time, TimeUnit unit) {
        return replay(selector, bufferSize, time, unit, Schedulers.computation());
    }

    public final <R> Observable<R> replay(Func1<? super Observable<T>, ? extends Observable<R>> selector, int bufferSize, long time, TimeUnit unit, Scheduler scheduler) {
        if (bufferSize < 0) {
            throw new IllegalArgumentException("bufferSize < 0");
        }
        final int i = bufferSize;
        final long j = time;
        final TimeUnit timeUnit = unit;
        final Scheduler scheduler2 = scheduler;
        return OperatorReplay.multicastSelector(new Func0<ConnectableObservable<T>>() {
            /* class dji.thirdparty.rx.Observable.AnonymousClass18 */

            public ConnectableObservable<T> call() {
                return Observable.this.replay(i, j, timeUnit, scheduler2);
            }
        }, selector);
    }

    public final <R> Observable<R> replay(final Func1<? super Observable<T>, ? extends Observable<R>> selector, final int bufferSize, final Scheduler scheduler) {
        return OperatorReplay.multicastSelector(new Func0<ConnectableObservable<T>>() {
            /* class dji.thirdparty.rx.Observable.AnonymousClass19 */

            public ConnectableObservable<T> call() {
                return Observable.this.replay(bufferSize);
            }
        }, new Func1<Observable<T>, Observable<R>>() {
            /* class dji.thirdparty.rx.Observable.AnonymousClass20 */

            public /* bridge */ /* synthetic */ Object call(Object x0) {
                return call((Observable) ((Observable) x0));
            }

            public Observable<R> call(Observable<T> t) {
                return ((Observable) selector.call(t)).observeOn(scheduler);
            }
        });
    }

    public final <R> Observable<R> replay(Func1<? super Observable<T>, ? extends Observable<R>> selector, long time, TimeUnit unit) {
        return replay(selector, time, unit, Schedulers.computation());
    }

    public final <R> Observable<R> replay(Func1<? super Observable<T>, ? extends Observable<R>> selector, long time, TimeUnit unit, Scheduler scheduler) {
        final long j = time;
        final TimeUnit timeUnit = unit;
        final Scheduler scheduler2 = scheduler;
        return OperatorReplay.multicastSelector(new Func0<ConnectableObservable<T>>() {
            /* class dji.thirdparty.rx.Observable.AnonymousClass21 */

            public ConnectableObservable<T> call() {
                return Observable.this.replay(j, timeUnit, scheduler2);
            }
        }, selector);
    }

    public final <R> Observable<R> replay(final Func1<? super Observable<T>, ? extends Observable<R>> selector, final Scheduler scheduler) {
        return OperatorReplay.multicastSelector(new Func0<ConnectableObservable<T>>() {
            /* class dji.thirdparty.rx.Observable.AnonymousClass22 */

            public ConnectableObservable<T> call() {
                return Observable.this.replay();
            }
        }, new Func1<Observable<T>, Observable<R>>() {
            /* class dji.thirdparty.rx.Observable.AnonymousClass23 */

            public /* bridge */ /* synthetic */ Object call(Object x0) {
                return call((Observable) ((Observable) x0));
            }

            public Observable<R> call(Observable<T> t) {
                return ((Observable) selector.call(t)).observeOn(scheduler);
            }
        });
    }

    public final ConnectableObservable<T> replay(int bufferSize) {
        return OperatorReplay.create(this, bufferSize);
    }

    public final ConnectableObservable<T> replay(int bufferSize, long time, TimeUnit unit) {
        return replay(bufferSize, time, unit, Schedulers.computation());
    }

    public final ConnectableObservable<T> replay(int bufferSize, long time, TimeUnit unit, Scheduler scheduler) {
        if (bufferSize >= 0) {
            return OperatorReplay.create(this, time, unit, scheduler, bufferSize);
        }
        throw new IllegalArgumentException("bufferSize < 0");
    }

    public final ConnectableObservable<T> replay(int bufferSize, Scheduler scheduler) {
        return OperatorReplay.observeOn(replay(bufferSize), scheduler);
    }

    public final ConnectableObservable<T> replay(long time, TimeUnit unit) {
        return replay(time, unit, Schedulers.computation());
    }

    public final ConnectableObservable<T> replay(long time, TimeUnit unit, Scheduler scheduler) {
        return OperatorReplay.create(this, time, unit, scheduler);
    }

    public final ConnectableObservable<T> replay(Scheduler scheduler) {
        return OperatorReplay.observeOn(replay(), scheduler);
    }

    public final Observable<T> retry() {
        return OnSubscribeRedo.retry(this);
    }

    public final Observable<T> retry(long count) {
        return OnSubscribeRedo.retry(this, count);
    }

    public final Observable<T> retry(Func2<Integer, Throwable, Boolean> predicate) {
        return nest().lift(new OperatorRetryWithPredicate(predicate));
    }

    public final Observable<T> retryWhen(final Func1<? super Observable<? extends Throwable>, ? extends Observable<?>> notificationHandler) {
        return OnSubscribeRedo.retry(this, new Func1<Observable<? extends Notification<?>>, Observable<?>>() {
            /* class dji.thirdparty.rx.Observable.AnonymousClass24 */

            public /* bridge */ /* synthetic */ Object call(Object x0) {
                return call((Observable<? extends Notification<?>>) ((Observable) x0));
            }

            public Observable<?> call(Observable<? extends Notification<?>> notifications) {
                return (Observable) notificationHandler.call(notifications.map(new Func1<Notification<?>, Throwable>() {
                    /* class dji.thirdparty.rx.Observable.AnonymousClass24.AnonymousClass1 */

                    public /* bridge */ /* synthetic */ Object call(Object x0) {
                        return call((Notification<?>) ((Notification) x0));
                    }

                    public Throwable call(Notification<?> notification) {
                        return notification.getThrowable();
                    }
                }));
            }
        });
    }

    public final Observable<T> retryWhen(final Func1<? super Observable<? extends Throwable>, ? extends Observable<?>> notificationHandler, Scheduler scheduler) {
        return OnSubscribeRedo.retry(this, new Func1<Observable<? extends Notification<?>>, Observable<?>>() {
            /* class dji.thirdparty.rx.Observable.AnonymousClass25 */

            public /* bridge */ /* synthetic */ Object call(Object x0) {
                return call((Observable<? extends Notification<?>>) ((Observable) x0));
            }

            public Observable<?> call(Observable<? extends Notification<?>> notifications) {
                return (Observable) notificationHandler.call(notifications.map(new Func1<Notification<?>, Throwable>() {
                    /* class dji.thirdparty.rx.Observable.AnonymousClass25.AnonymousClass1 */

                    public /* bridge */ /* synthetic */ Object call(Object x0) {
                        return call((Notification<?>) ((Notification) x0));
                    }

                    public Throwable call(Notification<?> notification) {
                        return notification.getThrowable();
                    }
                }));
            }
        }, scheduler);
    }

    public final Observable<T> sample(long period, TimeUnit unit) {
        return sample(period, unit, Schedulers.computation());
    }

    public final Observable<T> sample(long period, TimeUnit unit, Scheduler scheduler) {
        return lift(new OperatorSampleWithTime(period, unit, scheduler));
    }

    public final <U> Observable<T> sample(Observable<U> sampler) {
        return lift(new OperatorSampleWithObservable(sampler));
    }

    public final Observable<T> scan(Func2<T, T, T> accumulator) {
        return lift(new OperatorScan(accumulator));
    }

    /* JADX DEBUG: Failed to find minimal casts for resolve overloaded methods, cast all args instead
     method: dji.thirdparty.rx.internal.operators.OperatorScan.<init>(java.lang.Object, dji.thirdparty.rx.functions.Func2):void
     arg types: [R, dji.thirdparty.rx.functions.Func2<R, ? super T, R>]
     candidates:
      dji.thirdparty.rx.internal.operators.OperatorScan.<init>(dji.thirdparty.rx.functions.Func0, dji.thirdparty.rx.functions.Func2):void
      dji.thirdparty.rx.internal.operators.OperatorScan.<init>(java.lang.Object, dji.thirdparty.rx.functions.Func2):void */
    public final <R> Observable<R> scan(R initialValue, Func2<R, ? super T, R> accumulator) {
        return lift(new OperatorScan((Object) initialValue, (Func2) accumulator));
    }

    public final Observable<T> serialize() {
        return lift(OperatorSerialize.instance());
    }

    public final Observable<T> share() {
        return publish().refCount();
    }

    public final Observable<T> single() {
        return lift(OperatorSingle.instance());
    }

    public final Observable<T> single(Func1<? super T, Boolean> predicate) {
        return filter(predicate).single();
    }

    public final Observable<T> singleOrDefault(T defaultValue) {
        return lift(new OperatorSingle(defaultValue));
    }

    public final Observable<T> singleOrDefault(T defaultValue, Func1<? super T, Boolean> predicate) {
        return filter(predicate).singleOrDefault(defaultValue);
    }

    public final Observable<T> skip(int count) {
        return lift(new OperatorSkip(count));
    }

    public final Observable<T> skip(long time, TimeUnit unit) {
        return skip(time, unit, Schedulers.computation());
    }

    public final Observable<T> skip(long time, TimeUnit unit, Scheduler scheduler) {
        return lift(new OperatorSkipTimed(time, unit, scheduler));
    }

    public final Observable<T> skipLast(int count) {
        return lift(new OperatorSkipLast(count));
    }

    public final Observable<T> skipLast(long time, TimeUnit unit) {
        return skipLast(time, unit, Schedulers.computation());
    }

    public final Observable<T> skipLast(long time, TimeUnit unit, Scheduler scheduler) {
        return lift(new OperatorSkipLastTimed(time, unit, scheduler));
    }

    public final <U> Observable<T> skipUntil(Observable<U> other) {
        return lift(new OperatorSkipUntil(other));
    }

    public final Observable<T> skipWhile(Func1<? super T, Boolean> predicate) {
        return lift(new OperatorSkipWhile(OperatorSkipWhile.toPredicate2(predicate)));
    }

    public final Observable<T> startWith(Observable<T> values) {
        return concat(values, this);
    }

    public final Observable<T> startWith(Iterable<T> values) {
        return concat(from(values), this);
    }

    public final Observable<T> startWith(T t1) {
        return concat(just(t1), this);
    }

    public final Observable<T> startWith(T t1, T t2) {
        return concat(just(t1, t2), this);
    }

    public final Observable<T> startWith(T t1, T t2, T t3) {
        return concat(just(t1, t2, t3), this);
    }

    public final Observable<T> startWith(T t1, T t2, T t3, T t4) {
        return concat(just(t1, t2, t3, t4), this);
    }

    public final Observable<T> startWith(T t1, T t2, T t3, T t4, T t5) {
        return concat(just(t1, t2, t3, t4, t5), this);
    }

    public final Observable<T> startWith(T t1, T t2, T t3, T t4, T t5, T t6) {
        return concat(just(t1, t2, t3, t4, t5, t6), this);
    }

    public final Observable<T> startWith(T t1, T t2, T t3, T t4, T t5, T t6, T t7) {
        return concat(just(t1, t2, t3, t4, t5, t6, t7), this);
    }

    public final Observable<T> startWith(T t1, T t2, T t3, T t4, T t5, T t6, T t7, T t8) {
        return concat(just(t1, t2, t3, t4, t5, t6, t7, t8), this);
    }

    public final Observable<T> startWith(T t1, T t2, T t3, T t4, T t5, T t6, T t7, T t8, T t9) {
        return concat(just(t1, t2, t3, t4, t5, t6, t7, t8, t9), this);
    }

    public final Subscription subscribe() {
        return subscribe((Subscriber) new Subscriber<T>() {
            /* class dji.thirdparty.rx.Observable.AnonymousClass26 */

            public final void onCompleted() {
            }

            public final void onError(Throwable e) {
                throw new OnErrorNotImplementedException(e);
            }

            public final void onNext(T t) {
            }
        });
    }

    public final Subscription subscribe(final Action1 action1) {
        if (action1 != null) {
            return subscribe((Subscriber) new Subscriber<T>() {
                /* class dji.thirdparty.rx.Observable.AnonymousClass27 */

                public final void onCompleted() {
                }

                public final void onError(Throwable e) {
                    throw new OnErrorNotImplementedException(e);
                }

                public final void onNext(T args) {
                    action1.call(args);
                }
            });
        }
        throw new IllegalArgumentException("onNext can not be null");
    }

    public final Subscription subscribe(final Action1<? super T> onNext, final Action1<Throwable> onError) {
        if (onNext == null) {
            throw new IllegalArgumentException("onNext can not be null");
        } else if (onError != null) {
            return subscribe((Subscriber) new Subscriber<T>() {
                /* class dji.thirdparty.rx.Observable.AnonymousClass28 */

                public final void onCompleted() {
                }

                public final void onError(Throwable e) {
                    onError.call(e);
                }

                public final void onNext(T args) {
                    onNext.call(args);
                }
            });
        } else {
            throw new IllegalArgumentException("onError can not be null");
        }
    }

    public final Subscription subscribe(final Action1<? super T> onNext, final Action1<Throwable> onError, final Action0 onComplete) {
        if (onNext == null) {
            throw new IllegalArgumentException("onNext can not be null");
        } else if (onError == null) {
            throw new IllegalArgumentException("onError can not be null");
        } else if (onComplete != null) {
            return subscribe((Subscriber) new Subscriber<T>() {
                /* class dji.thirdparty.rx.Observable.AnonymousClass29 */

                public final void onCompleted() {
                    onComplete.call();
                }

                public final void onError(Throwable e) {
                    onError.call(e);
                }

                public final void onNext(T args) {
                    onNext.call(args);
                }
            });
        } else {
            throw new IllegalArgumentException("onComplete can not be null");
        }
    }

    public final Subscription subscribe(final Observer observer) {
        if (observer instanceof Subscriber) {
            return subscribe((Subscriber) observer);
        }
        return subscribe((Subscriber) new Subscriber<T>() {
            /* class dji.thirdparty.rx.Observable.AnonymousClass30 */

            public void onCompleted() {
                observer.onCompleted();
            }

            public void onError(Throwable e) {
                observer.onError(e);
            }

            public void onNext(T t) {
                observer.onNext(t);
            }
        });
    }

    public final Subscription unsafeSubscribe(Subscriber<? super T> subscriber) {
        try {
            subscriber.onStart();
            hook.onSubscribeStart(this, this.onSubscribe).call(subscriber);
            return hook.onSubscribeReturn(subscriber);
        } catch (Throwable e2) {
            Exceptions.throwIfFatal(e2);
            RuntimeException r = new RuntimeException("Error occurred attempting to subscribe [" + e.getMessage() + "] and then again while trying to pass to onError.", e2);
            hook.onSubscribeError(r);
            throw r;
        }
    }

    public final Subscription subscribe(Subscriber subscriber) {
        return subscribe(subscriber, this);
    }

    /* access modifiers changed from: private */
    public static <T> Subscription subscribe(Subscriber<? super T> subscriber, Observable<T> observable) {
        if (subscriber == null) {
            throw new IllegalArgumentException("observer can not be null");
        } else if (observable.onSubscribe == null) {
            throw new IllegalStateException("onSubscribe function can not be null.");
        } else {
            subscriber.onStart();
            if (!(subscriber instanceof SafeSubscriber)) {
                subscriber = new SafeSubscriber<>(subscriber);
            }
            try {
                hook.onSubscribeStart(observable, observable.onSubscribe).call(subscriber);
                return hook.onSubscribeReturn(subscriber);
            } catch (Throwable e2) {
                Exceptions.throwIfFatal(e2);
                RuntimeException r = new RuntimeException("Error occurred attempting to subscribe [" + e.getMessage() + "] and then again while trying to pass to onError.", e2);
                hook.onSubscribeError(r);
                throw r;
            }
        }
    }

    public final Observable<T> subscribeOn(Scheduler scheduler) {
        if (this instanceof ScalarSynchronousObservable) {
            return ((ScalarSynchronousObservable) this).scalarScheduleOn(scheduler);
        }
        return create(new OperatorSubscribeOn(this, scheduler));
    }

    public final <R> Observable<R> switchMap(Func1<? super T, ? extends Observable<? extends R>> func) {
        return switchOnNext(map(func));
    }

    @Experimental
    public final <R> Observable<R> switchMapDelayError(Func1<? super T, ? extends Observable<? extends R>> func) {
        return switchOnNextDelayError(map(func));
    }

    public final Observable<T> take(int count) {
        return lift(new OperatorTake(count));
    }

    public final Observable<T> take(long time, TimeUnit unit) {
        return take(time, unit, Schedulers.computation());
    }

    public final Observable<T> take(long time, TimeUnit unit, Scheduler scheduler) {
        return lift(new OperatorTakeTimed(time, unit, scheduler));
    }

    public final Observable<T> takeFirst(Func1<? super T, Boolean> predicate) {
        return filter(predicate).take(1);
    }

    public final Observable<T> takeLast(int count) {
        if (count == 0) {
            return ignoreElements();
        }
        if (count == 1) {
            return lift(OperatorTakeLastOne.instance());
        }
        return lift(new OperatorTakeLast(count));
    }

    public final Observable<T> takeLast(int count, long time, TimeUnit unit) {
        return takeLast(count, time, unit, Schedulers.computation());
    }

    public final Observable<T> takeLast(int count, long time, TimeUnit unit, Scheduler scheduler) {
        return lift(new OperatorTakeLastTimed(count, time, unit, scheduler));
    }

    public final Observable<T> takeLast(long time, TimeUnit unit) {
        return takeLast(time, unit, Schedulers.computation());
    }

    public final Observable<T> takeLast(long time, TimeUnit unit, Scheduler scheduler) {
        return lift(new OperatorTakeLastTimed(time, unit, scheduler));
    }

    public final Observable<List<T>> takeLastBuffer(int count) {
        return takeLast(count).toList();
    }

    public final Observable<List<T>> takeLastBuffer(int count, long time, TimeUnit unit) {
        return takeLast(count, time, unit).toList();
    }

    public final Observable<List<T>> takeLastBuffer(int count, long time, TimeUnit unit, Scheduler scheduler) {
        return takeLast(count, time, unit, scheduler).toList();
    }

    public final Observable<List<T>> takeLastBuffer(long time, TimeUnit unit) {
        return takeLast(time, unit).toList();
    }

    public final Observable<List<T>> takeLastBuffer(long time, TimeUnit unit, Scheduler scheduler) {
        return takeLast(time, unit, scheduler).toList();
    }

    public final <E> Observable<T> takeUntil(Observable<? extends E> other) {
        return lift(new OperatorTakeUntil(other));
    }

    public final Observable<T> takeWhile(Func1<? super T, Boolean> predicate) {
        return lift(new OperatorTakeWhile(predicate));
    }

    public final Observable<T> takeUntil(Func1<? super T, Boolean> stopPredicate) {
        return lift(new OperatorTakeUntilPredicate(stopPredicate));
    }

    public final Observable<T> throttleFirst(long windowDuration, TimeUnit unit) {
        return throttleFirst(windowDuration, unit, Schedulers.computation());
    }

    public final Observable<T> throttleFirst(long skipDuration, TimeUnit unit, Scheduler scheduler) {
        return lift(new OperatorThrottleFirst(skipDuration, unit, scheduler));
    }

    public final Observable<T> throttleLast(long intervalDuration, TimeUnit unit) {
        return sample(intervalDuration, unit);
    }

    public final Observable<T> throttleLast(long intervalDuration, TimeUnit unit, Scheduler scheduler) {
        return sample(intervalDuration, unit, scheduler);
    }

    public final Observable<T> throttleWithTimeout(long timeout, TimeUnit unit) {
        return debounce(timeout, unit);
    }

    public final Observable<T> throttleWithTimeout(long timeout, TimeUnit unit, Scheduler scheduler) {
        return debounce(timeout, unit, scheduler);
    }

    public final Observable<TimeInterval<T>> timeInterval() {
        return timeInterval(Schedulers.immediate());
    }

    public final Observable<TimeInterval<T>> timeInterval(Scheduler scheduler) {
        return lift(new OperatorTimeInterval(scheduler));
    }

    public final <U, V> Observable<T> timeout(Func0<? extends Observable<U>> firstTimeoutSelector, Func1<? super T, ? extends Observable<V>> timeoutSelector) {
        return timeout(firstTimeoutSelector, timeoutSelector, (Observable) null);
    }

    public final <U, V> Observable<T> timeout(Func0<? extends Observable<U>> firstTimeoutSelector, Func1<? super T, ? extends Observable<V>> timeoutSelector, Observable<? extends T> other) {
        if (timeoutSelector != null) {
            return lift(new OperatorTimeoutWithSelector(firstTimeoutSelector, timeoutSelector, other));
        }
        throw new NullPointerException("timeoutSelector is null");
    }

    public final <V> Observable<T> timeout(Func1<? super T, ? extends Observable<V>> timeoutSelector) {
        return timeout((Func0) null, timeoutSelector, (Observable) null);
    }

    public final <V> Observable<T> timeout(Func1<? super T, ? extends Observable<V>> timeoutSelector, Observable<? extends T> other) {
        return timeout((Func0) null, timeoutSelector, other);
    }

    public final Observable<T> timeout(long timeout, TimeUnit timeUnit) {
        return timeout(timeout, timeUnit, null, Schedulers.computation());
    }

    public final Observable<T> timeout(long timeout, TimeUnit timeUnit, Observable<? extends T> other) {
        return timeout(timeout, timeUnit, other, Schedulers.computation());
    }

    public final Observable<T> timeout(long timeout, TimeUnit timeUnit, Observable<? extends T> other, Scheduler scheduler) {
        return lift(new OperatorTimeout(timeout, timeUnit, other, scheduler));
    }

    public final Observable<T> timeout(long timeout, TimeUnit timeUnit, Scheduler scheduler) {
        return timeout(timeout, timeUnit, null, scheduler);
    }

    public final Observable<Timestamped<T>> timestamp() {
        return timestamp(Schedulers.immediate());
    }

    public final Observable<Timestamped<T>> timestamp(Scheduler scheduler) {
        return lift(new OperatorTimestamp(scheduler));
    }

    public final BlockingObservable<T> toBlocking() {
        return BlockingObservable.from(this);
    }

    public final Observable<List<T>> toList() {
        return lift(OperatorToObservableList.instance());
    }

    public final <K> Observable<Map<K, T>> toMap(Func1<? super T, ? extends K> keySelector) {
        return lift(new OperatorToMap(keySelector, UtilityFunctions.identity()));
    }

    public final <K, V> Observable<Map<K, V>> toMap(Func1<? super T, ? extends K> keySelector, Func1<? super T, ? extends V> valueSelector) {
        return lift(new OperatorToMap(keySelector, valueSelector));
    }

    public final <K, V> Observable<Map<K, V>> toMap(Func1<? super T, ? extends K> keySelector, Func1<? super T, ? extends V> valueSelector, Func0<? extends Map<K, V>> mapFactory) {
        return lift(new OperatorToMap(keySelector, valueSelector, mapFactory));
    }

    public final <K> Observable<Map<K, Collection<T>>> toMultimap(Func1<? super T, ? extends K> keySelector) {
        return lift(new OperatorToMultimap(keySelector, UtilityFunctions.identity()));
    }

    public final <K, V> Observable<Map<K, Collection<V>>> toMultimap(Func1<? super T, ? extends K> keySelector, Func1<? super T, ? extends V> valueSelector) {
        return lift(new OperatorToMultimap(keySelector, valueSelector));
    }

    public final <K, V> Observable<Map<K, Collection<V>>> toMultimap(Func1<? super T, ? extends K> keySelector, Func1<? super T, ? extends V> valueSelector, Func0<? extends Map<K, Collection<V>>> mapFactory) {
        return lift(new OperatorToMultimap(keySelector, valueSelector, mapFactory));
    }

    public final <K, V> Observable<Map<K, Collection<V>>> toMultimap(Func1<? super T, ? extends K> keySelector, Func1<? super T, ? extends V> valueSelector, Func0<? extends Map<K, Collection<V>>> mapFactory, Func1<? super K, ? extends Collection<V>> collectionFactory) {
        return lift(new OperatorToMultimap(keySelector, valueSelector, mapFactory, collectionFactory));
    }

    public final Observable<List<T>> toSortedList() {
        return lift(new OperatorToObservableSortedList(10));
    }

    public final Observable<List<T>> toSortedList(Func2<? super T, ? super T, Integer> sortFunction) {
        return lift(new OperatorToObservableSortedList(sortFunction, 10));
    }

    @Experimental
    public final Observable<List<T>> toSortedList(int initialCapacity) {
        return lift(new OperatorToObservableSortedList(initialCapacity));
    }

    @Experimental
    public final Observable<List<T>> toSortedList(Func2<? super T, ? super T, Integer> sortFunction, int initialCapacity) {
        return lift(new OperatorToObservableSortedList(sortFunction, initialCapacity));
    }

    public final Observable<T> unsubscribeOn(Scheduler scheduler) {
        return lift(new OperatorUnsubscribeOn(scheduler));
    }

    @Experimental
    public final <U, R> Observable<R> withLatestFrom(Observable<? extends U> other, Func2<? super T, ? super U, ? extends R> resultSelector) {
        return lift(new OperatorWithLatestFrom(other, resultSelector));
    }

    public final <TClosing> Observable<Observable<T>> window(Func0<? extends Observable<? extends TClosing>> closingSelector) {
        return lift(new OperatorWindowWithObservableFactory(closingSelector));
    }

    public final Observable<Observable<T>> window(int count) {
        return window(count, count);
    }

    public final Observable<Observable<T>> window(int count, int skip) {
        if (count <= 0) {
            throw new IllegalArgumentException("count > 0 required but it was " + count);
        } else if (skip > 0) {
            return lift(new OperatorWindowWithSize(count, skip));
        } else {
            throw new IllegalArgumentException("skip > 0 required but it was " + skip);
        }
    }

    public final Observable<Observable<T>> window(long timespan, long timeshift, TimeUnit unit) {
        return window(timespan, timeshift, unit, Integer.MAX_VALUE, Schedulers.computation());
    }

    public final Observable<Observable<T>> window(long timespan, long timeshift, TimeUnit unit, Scheduler scheduler) {
        return window(timespan, timeshift, unit, Integer.MAX_VALUE, scheduler);
    }

    public final Observable<Observable<T>> window(long timespan, long timeshift, TimeUnit unit, int count, Scheduler scheduler) {
        return lift(new OperatorWindowWithTime(timespan, timeshift, unit, count, scheduler));
    }

    public final Observable<Observable<T>> window(long timespan, TimeUnit unit) {
        return window(timespan, timespan, unit, Schedulers.computation());
    }

    public final Observable<Observable<T>> window(long timespan, TimeUnit unit, int count) {
        return window(timespan, unit, count, Schedulers.computation());
    }

    public final Observable<Observable<T>> window(long timespan, TimeUnit unit, int count, Scheduler scheduler) {
        return window(timespan, timespan, unit, count, scheduler);
    }

    public final Observable<Observable<T>> window(long timespan, TimeUnit unit, Scheduler scheduler) {
        return window(timespan, unit, Integer.MAX_VALUE, scheduler);
    }

    public final <TOpening, TClosing> Observable<Observable<T>> window(Observable<? extends TOpening> windowOpenings, Func1<? super TOpening, ? extends Observable<? extends TClosing>> closingSelector) {
        return lift(new OperatorWindowWithStartEndObservable(windowOpenings, closingSelector));
    }

    public final <U> Observable<Observable<T>> window(Observable<U> boundary) {
        return lift(new OperatorWindowWithObservable(boundary));
    }

    public final <T2, R> Observable<R> zipWith(Iterable<? extends T2> other, Func2<? super T, ? super T2, ? extends R> zipFunction) {
        return lift(new OperatorZipIterable(other, zipFunction));
    }

    public final <T2, R> Observable<R> zipWith(Observable<? extends T2> other, Func2<? super T, ? super T2, ? extends R> zipFunction) {
        return zip(this, other, zipFunction);
    }

    private static class NeverObservable<T> extends Observable<T> {

        private static class Holder {
            static final NeverObservable<?> INSTANCE = new NeverObservable<>();

            private Holder() {
            }
        }

        static <T> NeverObservable<T> instance() {
            return Holder.INSTANCE;
        }

        NeverObservable() {
            super(new OnSubscribe<T>() {
                /* class dji.thirdparty.rx.Observable.NeverObservable.AnonymousClass1 */

                public /* bridge */ /* synthetic */ void call(Object x0) {
                    call((Subscriber) ((Subscriber) x0));
                }

                public void call(Subscriber<? super T> subscriber) {
                }
            });
        }
    }

    private static class ThrowObservable<T> extends Observable<T> {
        public ThrowObservable(final Throwable exception) {
            super(new OnSubscribe<T>() {
                /* class dji.thirdparty.rx.Observable.ThrowObservable.AnonymousClass1 */

                public /* bridge */ /* synthetic */ void call(Object x0) {
                    call((Subscriber) ((Subscriber) x0));
                }

                public void call(Subscriber<? super T> observer) {
                    observer.onError(exception);
                }
            });
        }
    }
}
