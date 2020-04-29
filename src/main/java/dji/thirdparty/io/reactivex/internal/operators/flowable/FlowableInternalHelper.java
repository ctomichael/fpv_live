package dji.thirdparty.io.reactivex.internal.operators.flowable;

import dji.thirdparty.io.reactivex.Emitter;
import dji.thirdparty.io.reactivex.Flowable;
import dji.thirdparty.io.reactivex.Scheduler;
import dji.thirdparty.io.reactivex.flowables.ConnectableFlowable;
import dji.thirdparty.io.reactivex.functions.Action;
import dji.thirdparty.io.reactivex.functions.BiConsumer;
import dji.thirdparty.io.reactivex.functions.BiFunction;
import dji.thirdparty.io.reactivex.functions.Consumer;
import dji.thirdparty.io.reactivex.functions.Function;
import dji.thirdparty.io.reactivex.internal.functions.Functions;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;
import kotlin.jvm.internal.LongCompanionObject;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

public final class FlowableInternalHelper {
    private FlowableInternalHelper() {
        throw new IllegalStateException("No instances!");
    }

    static final class SimpleGenerator<T, S> implements BiFunction<S, Emitter<T>, S> {
        final Consumer<Emitter<T>> consumer;

        /* JADX DEBUG: Failed to find minimal casts for resolve overloaded methods, cast all args instead
         method: dji.thirdparty.io.reactivex.internal.operators.flowable.FlowableInternalHelper.SimpleGenerator.apply(java.lang.Object, dji.thirdparty.io.reactivex.Emitter):S
         arg types: [java.lang.Object, java.lang.Object]
         candidates:
          dji.thirdparty.io.reactivex.internal.operators.flowable.FlowableInternalHelper.SimpleGenerator.apply(java.lang.Object, java.lang.Object):java.lang.Object
          dji.thirdparty.io.reactivex.functions.BiFunction.apply(java.lang.Object, java.lang.Object):R
          dji.thirdparty.io.reactivex.internal.operators.flowable.FlowableInternalHelper.SimpleGenerator.apply(java.lang.Object, dji.thirdparty.io.reactivex.Emitter):S */
        public /* bridge */ /* synthetic */ Object apply(Object x0, Object x1) throws Exception {
            return apply(x0, (Emitter) ((Emitter) x1));
        }

        SimpleGenerator(Consumer<Emitter<T>> consumer2) {
            this.consumer = consumer2;
        }

        public S apply(S t1, Emitter<T> t2) throws Exception {
            this.consumer.accept(t2);
            return t1;
        }
    }

    public static <T, S> BiFunction<S, Emitter<T>, S> simpleGenerator(Consumer<Emitter<T>> consumer) {
        return new SimpleGenerator(consumer);
    }

    static final class SimpleBiGenerator<T, S> implements BiFunction<S, Emitter<T>, S> {
        final BiConsumer<S, Emitter<T>> consumer;

        /* JADX DEBUG: Failed to find minimal casts for resolve overloaded methods, cast all args instead
         method: dji.thirdparty.io.reactivex.internal.operators.flowable.FlowableInternalHelper.SimpleBiGenerator.apply(java.lang.Object, dji.thirdparty.io.reactivex.Emitter):S
         arg types: [java.lang.Object, java.lang.Object]
         candidates:
          dji.thirdparty.io.reactivex.internal.operators.flowable.FlowableInternalHelper.SimpleBiGenerator.apply(java.lang.Object, java.lang.Object):java.lang.Object
          dji.thirdparty.io.reactivex.functions.BiFunction.apply(java.lang.Object, java.lang.Object):R
          dji.thirdparty.io.reactivex.internal.operators.flowable.FlowableInternalHelper.SimpleBiGenerator.apply(java.lang.Object, dji.thirdparty.io.reactivex.Emitter):S */
        public /* bridge */ /* synthetic */ Object apply(Object x0, Object x1) throws Exception {
            return apply(x0, (Emitter) ((Emitter) x1));
        }

        SimpleBiGenerator(BiConsumer<S, Emitter<T>> consumer2) {
            this.consumer = consumer2;
        }

        public S apply(S t1, Emitter<T> t2) throws Exception {
            this.consumer.accept(t1, t2);
            return t1;
        }
    }

    public static <T, S> BiFunction<S, Emitter<T>, S> simpleBiGenerator(BiConsumer<S, Emitter<T>> consumer) {
        return new SimpleBiGenerator(consumer);
    }

    static final class ItemDelayFunction<T, U> implements Function<T, Publisher<T>> {
        final Function<? super T, ? extends Publisher<U>> itemDelay;

        ItemDelayFunction(Function<? super T, ? extends Publisher<U>> itemDelay2) {
            this.itemDelay = itemDelay2;
        }

        public Publisher<T> apply(T v) throws Exception {
            return new FlowableTake((Publisher) this.itemDelay.apply(v), 1).map(Functions.justFunction(v)).defaultIfEmpty(v);
        }
    }

    public static <T, U> Function<T, Publisher<T>> itemDelay(Function<? super T, ? extends Publisher<U>> itemDelay) {
        return new ItemDelayFunction(itemDelay);
    }

    static final class SubscriberOnNext<T> implements Consumer<T> {
        final Subscriber<T> subscriber;

        SubscriberOnNext(Subscriber<T> subscriber2) {
            this.subscriber = subscriber2;
        }

        public void accept(T v) throws Exception {
            this.subscriber.onNext(v);
        }
    }

    static final class SubscriberOnError<T> implements Consumer<Throwable> {
        final Subscriber<T> subscriber;

        SubscriberOnError(Subscriber<T> subscriber2) {
            this.subscriber = subscriber2;
        }

        public void accept(Throwable v) throws Exception {
            this.subscriber.onError(v);
        }
    }

    static final class SubscriberOnComplete<T> implements Action {
        final Subscriber<T> subscriber;

        SubscriberOnComplete(Subscriber<T> subscriber2) {
            this.subscriber = subscriber2;
        }

        public void run() throws Exception {
            this.subscriber.onComplete();
        }
    }

    public static <T> Consumer<T> subscriberOnNext(Subscriber<T> subscriber) {
        return new SubscriberOnNext(subscriber);
    }

    public static <T> Consumer<Throwable> subscriberOnError(Subscriber<T> subscriber) {
        return new SubscriberOnError(subscriber);
    }

    public static <T> Action subscriberOnComplete(Subscriber<T> subscriber) {
        return new SubscriberOnComplete(subscriber);
    }

    static final class FlatMapWithCombinerInner<U, R, T> implements Function<U, R> {
        private final BiFunction<? super T, ? super U, ? extends R> combiner;
        private final T t;

        FlatMapWithCombinerInner(BiFunction<? super T, ? super U, ? extends R> combiner2, T t2) {
            this.combiner = combiner2;
            this.t = t2;
        }

        public R apply(U w) throws Exception {
            return this.combiner.apply(this.t, w);
        }
    }

    static final class FlatMapWithCombinerOuter<T, R, U> implements Function<T, Publisher<R>> {
        private final BiFunction<? super T, ? super U, ? extends R> combiner;
        private final Function<? super T, ? extends Publisher<? extends U>> mapper;

        FlatMapWithCombinerOuter(BiFunction<? super T, ? super U, ? extends R> combiner2, Function<? super T, ? extends Publisher<? extends U>> mapper2) {
            this.combiner = combiner2;
            this.mapper = mapper2;
        }

        public Publisher<R> apply(T t) throws Exception {
            return new FlowableMap((Publisher) this.mapper.apply(t), new FlatMapWithCombinerInner(this.combiner, t));
        }
    }

    public static <T, U, R> Function<T, Publisher<R>> flatMapWithCombiner(Function<? super T, ? extends Publisher<? extends U>> mapper, BiFunction<? super T, ? super U, ? extends R> combiner) {
        return new FlatMapWithCombinerOuter(combiner, mapper);
    }

    static final class FlatMapIntoIterable<T, U> implements Function<T, Publisher<U>> {
        private final Function<? super T, ? extends Iterable<? extends U>> mapper;

        FlatMapIntoIterable(Function<? super T, ? extends Iterable<? extends U>> mapper2) {
            this.mapper = mapper2;
        }

        public Publisher<U> apply(T t) throws Exception {
            return new FlowableFromIterable((Iterable) this.mapper.apply(t));
        }
    }

    public static <T, U> Function<T, Publisher<U>> flatMapIntoIterable(Function<? super T, ? extends Iterable<? extends U>> mapper) {
        return new FlatMapIntoIterable(mapper);
    }

    public static <T> Callable<ConnectableFlowable<T>> replayCallable(final Flowable<T> parent) {
        return new Callable<ConnectableFlowable<T>>() {
            /* class dji.thirdparty.io.reactivex.internal.operators.flowable.FlowableInternalHelper.AnonymousClass1 */

            public ConnectableFlowable<T> call() {
                return parent.replay();
            }
        };
    }

    public static <T> Callable<ConnectableFlowable<T>> replayCallable(final Flowable<T> parent, final int bufferSize) {
        return new Callable<ConnectableFlowable<T>>() {
            /* class dji.thirdparty.io.reactivex.internal.operators.flowable.FlowableInternalHelper.AnonymousClass2 */

            public ConnectableFlowable<T> call() {
                return parent.replay(bufferSize);
            }
        };
    }

    public static <T> Callable<ConnectableFlowable<T>> replayCallable(Flowable<T> parent, int bufferSize, long time, TimeUnit unit, Scheduler scheduler) {
        final Flowable<T> flowable = parent;
        final int i = bufferSize;
        final long j = time;
        final TimeUnit timeUnit = unit;
        final Scheduler scheduler2 = scheduler;
        return new Callable<ConnectableFlowable<T>>() {
            /* class dji.thirdparty.io.reactivex.internal.operators.flowable.FlowableInternalHelper.AnonymousClass3 */

            public ConnectableFlowable<T> call() {
                return flowable.replay(i, j, timeUnit, scheduler2);
            }
        };
    }

    public static <T> Callable<ConnectableFlowable<T>> replayCallable(Flowable<T> parent, long time, TimeUnit unit, Scheduler scheduler) {
        final Flowable<T> flowable = parent;
        final long j = time;
        final TimeUnit timeUnit = unit;
        final Scheduler scheduler2 = scheduler;
        return new Callable<ConnectableFlowable<T>>() {
            /* class dji.thirdparty.io.reactivex.internal.operators.flowable.FlowableInternalHelper.AnonymousClass4 */

            public ConnectableFlowable<T> call() {
                return flowable.replay(j, timeUnit, scheduler2);
            }
        };
    }

    public static <T, R> Function<Flowable<T>, Publisher<R>> replayFunction(final Function<? super Flowable<T>, ? extends Publisher<R>> selector, final Scheduler scheduler) {
        return new Function<Flowable<T>, Publisher<R>>() {
            /* class dji.thirdparty.io.reactivex.internal.operators.flowable.FlowableInternalHelper.AnonymousClass5 */

            public /* bridge */ /* synthetic */ Object apply(Object x0) throws Exception {
                return apply((Flowable) ((Flowable) x0));
            }

            public Publisher<R> apply(Flowable<T> t) throws Exception {
                return Flowable.fromPublisher((Publisher) selector.apply(t)).observeOn(scheduler);
            }
        };
    }

    public enum RequestMax implements Consumer<Subscription> {
        INSTANCE;

        public void accept(Subscription t) throws Exception {
            t.request(LongCompanionObject.MAX_VALUE);
        }
    }

    static final class ZipIterableFunction<T, R> implements Function<List<Publisher<? extends T>>, Publisher<? extends R>> {
        private final Function<? super Object[], ? extends R> zipper;

        public /* bridge */ /* synthetic */ Object apply(Object x0) throws Exception {
            return apply((List) ((List) x0));
        }

        ZipIterableFunction(Function<? super Object[], ? extends R> zipper2) {
            this.zipper = zipper2;
        }

        public Publisher<? extends R> apply(List<Publisher<? extends T>> list) {
            return Flowable.zipIterable(list, this.zipper, false, Flowable.bufferSize());
        }
    }

    public static <T, R> Function<List<Publisher<? extends T>>, Publisher<? extends R>> zipIterable(Function<? super Object[], ? extends R> zipper) {
        return new ZipIterableFunction(zipper);
    }
}
