package dji.thirdparty.io.reactivex.internal.functions;

import dji.thirdparty.io.reactivex.Notification;
import dji.thirdparty.io.reactivex.Scheduler;
import dji.thirdparty.io.reactivex.functions.Action;
import dji.thirdparty.io.reactivex.functions.BiConsumer;
import dji.thirdparty.io.reactivex.functions.BiFunction;
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
import dji.thirdparty.io.reactivex.plugins.RxJavaPlugins;
import dji.thirdparty.io.reactivex.schedulers.Timed;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import kotlin.jvm.internal.LongCompanionObject;
import org.reactivestreams.Subscription;

public final class Functions {
    static final Predicate<Object> ALWAYS_FALSE = new Predicate<Object>() {
        /* class dji.thirdparty.io.reactivex.internal.functions.Functions.AnonymousClass16 */

        public boolean test(Object o) {
            return false;
        }
    };
    static final Predicate<Object> ALWAYS_TRUE = new Predicate<Object>() {
        /* class dji.thirdparty.io.reactivex.internal.functions.Functions.AnonymousClass15 */

        public boolean test(Object o) {
            return true;
        }
    };
    public static final Action EMPTY_ACTION = new Action() {
        /* class dji.thirdparty.io.reactivex.internal.functions.Functions.AnonymousClass11 */

        public void run() {
        }

        public String toString() {
            return "EmptyAction";
        }
    };
    static final Consumer<Object> EMPTY_CONSUMER = new Consumer<Object>() {
        /* class dji.thirdparty.io.reactivex.internal.functions.Functions.AnonymousClass12 */

        public void accept(Object v) {
        }

        public String toString() {
            return "EmptyConsumer";
        }
    };
    public static final LongConsumer EMPTY_LONG_CONSUMER = new LongConsumer() {
        /* class dji.thirdparty.io.reactivex.internal.functions.Functions.AnonymousClass14 */

        public void accept(long v) {
        }
    };
    public static final Runnable EMPTY_RUNNABLE = new Runnable() {
        /* class dji.thirdparty.io.reactivex.internal.functions.Functions.AnonymousClass10 */

        public void run() {
        }

        public String toString() {
            return "EmptyRunnable";
        }
    };
    public static final Consumer<Throwable> ERROR_CONSUMER = new Consumer<Throwable>() {
        /* class dji.thirdparty.io.reactivex.internal.functions.Functions.AnonymousClass13 */

        public void accept(Throwable error) {
            RxJavaPlugins.onError(error);
        }
    };
    static final Function<Object, Object> IDENTITY = new Function<Object, Object>() {
        /* class dji.thirdparty.io.reactivex.internal.functions.Functions.AnonymousClass9 */

        public Object apply(Object v) {
            return v;
        }

        public String toString() {
            return "IdentityFunction";
        }
    };
    static final Comparator<Object> NATURAL_COMPARATOR = new Comparator<Object>() {
        /* class dji.thirdparty.io.reactivex.internal.functions.Functions.AnonymousClass18 */

        public int compare(Object a, Object b) {
            return ((Comparable) a).compareTo(b);
        }
    };
    static final Callable<Object> NULL_SUPPLIER = new Callable<Object>() {
        /* class dji.thirdparty.io.reactivex.internal.functions.Functions.AnonymousClass17 */

        public Object call() {
            return null;
        }
    };
    public static final Consumer<Subscription> REQUEST_MAX = new Consumer<Subscription>() {
        /* class dji.thirdparty.io.reactivex.internal.functions.Functions.AnonymousClass19 */

        public void accept(Subscription t) throws Exception {
            t.request(LongCompanionObject.MAX_VALUE);
        }
    };

    private Functions() {
        throw new IllegalStateException("No instances!");
    }

    public static <T1, T2, R> Function<Object[], R> toFunction(final BiFunction biFunction) {
        ObjectHelper.requireNonNull(biFunction, "f is null");
        return new Function<Object[], R>() {
            /* class dji.thirdparty.io.reactivex.internal.functions.Functions.AnonymousClass1 */

            public R apply(Object[] a) throws Exception {
                if (a.length == 2) {
                    return biFunction.apply(a[0], a[1]);
                }
                throw new IllegalArgumentException("Array of size 2 expected but got " + a.length);
            }
        };
    }

    public static <T1, T2, T3, R> Function<Object[], R> toFunction(final Function3 function3) {
        ObjectHelper.requireNonNull(function3, "f is null");
        return new Function<Object[], R>() {
            /* class dji.thirdparty.io.reactivex.internal.functions.Functions.AnonymousClass2 */

            public R apply(Object[] a) throws Exception {
                if (a.length == 3) {
                    return function3.apply(a[0], a[1], a[2]);
                }
                throw new IllegalArgumentException("Array of size 3 expected but got " + a.length);
            }
        };
    }

    public static <T1, T2, T3, T4, R> Function<Object[], R> toFunction(final Function4 function4) {
        ObjectHelper.requireNonNull(function4, "f is null");
        return new Function<Object[], R>() {
            /* class dji.thirdparty.io.reactivex.internal.functions.Functions.AnonymousClass3 */

            public R apply(Object[] a) throws Exception {
                if (a.length == 4) {
                    return function4.apply(a[0], a[1], a[2], a[3]);
                }
                throw new IllegalArgumentException("Array of size 4 expected but got " + a.length);
            }
        };
    }

    public static <T1, T2, T3, T4, T5, R> Function<Object[], R> toFunction(final Function5 function5) {
        ObjectHelper.requireNonNull(function5, "f is null");
        return new Function<Object[], R>() {
            /* class dji.thirdparty.io.reactivex.internal.functions.Functions.AnonymousClass4 */

            public R apply(Object[] a) throws Exception {
                if (a.length == 5) {
                    return function5.apply(a[0], a[1], a[2], a[3], a[4]);
                }
                throw new IllegalArgumentException("Array of size 5 expected but got " + a.length);
            }
        };
    }

    public static <T1, T2, T3, T4, T5, T6, R> Function<Object[], R> toFunction(final Function6 function6) {
        ObjectHelper.requireNonNull(function6, "f is null");
        return new Function<Object[], R>() {
            /* class dji.thirdparty.io.reactivex.internal.functions.Functions.AnonymousClass5 */

            public R apply(Object[] a) throws Exception {
                if (a.length == 6) {
                    return function6.apply(a[0], a[1], a[2], a[3], a[4], a[5]);
                }
                throw new IllegalArgumentException("Array of size 6 expected but got " + a.length);
            }
        };
    }

    public static <T1, T2, T3, T4, T5, T6, T7, R> Function<Object[], R> toFunction(final Function7 function7) {
        ObjectHelper.requireNonNull(function7, "f is null");
        return new Function<Object[], R>() {
            /* class dji.thirdparty.io.reactivex.internal.functions.Functions.AnonymousClass6 */

            public R apply(Object[] a) throws Exception {
                if (a.length == 7) {
                    return function7.apply(a[0], a[1], a[2], a[3], a[4], a[5], a[6]);
                }
                throw new IllegalArgumentException("Array of size 7 expected but got " + a.length);
            }
        };
    }

    public static <T1, T2, T3, T4, T5, T6, T7, T8, R> Function<Object[], R> toFunction(final Function8 function8) {
        ObjectHelper.requireNonNull(function8, "f is null");
        return new Function<Object[], R>() {
            /* class dji.thirdparty.io.reactivex.internal.functions.Functions.AnonymousClass7 */

            public R apply(Object[] a) throws Exception {
                if (a.length == 8) {
                    return function8.apply(a[0], a[1], a[2], a[3], a[4], a[5], a[6], a[7]);
                }
                throw new IllegalArgumentException("Array of size 8 expected but got " + a.length);
            }
        };
    }

    public static <T1, T2, T3, T4, T5, T6, T7, T8, T9, R> Function<Object[], R> toFunction(final Function9 function9) {
        ObjectHelper.requireNonNull(function9, "f is null");
        return new Function<Object[], R>() {
            /* class dji.thirdparty.io.reactivex.internal.functions.Functions.AnonymousClass8 */

            public R apply(Object[] a) throws Exception {
                if (a.length == 9) {
                    return function9.apply(a[0], a[1], a[2], a[3], a[4], a[5], a[6], a[7], a[8]);
                }
                throw new IllegalArgumentException("Array of size 9 expected but got " + a.length);
            }
        };
    }

    public static <T> Function<T, T> identity() {
        return IDENTITY;
    }

    public static <T> Consumer<T> emptyConsumer() {
        return EMPTY_CONSUMER;
    }

    public static <T> Predicate<T> alwaysTrue() {
        return ALWAYS_TRUE;
    }

    public static <T> Predicate<T> alwaysFalse() {
        return ALWAYS_FALSE;
    }

    public static <T> Callable<T> nullSupplier() {
        return NULL_SUPPLIER;
    }

    public static <T> Comparator<T> naturalOrder() {
        return NATURAL_COMPARATOR;
    }

    static final class FutureAction implements Action {
        final Future<?> future;

        FutureAction(Future<?> future2) {
            this.future = future2;
        }

        public void run() throws Exception {
            this.future.get();
        }
    }

    public static Action futureAction(Future<?> future) {
        return new FutureAction(future);
    }

    static final class JustValue<T, U> implements Callable<U>, Function<T, U> {
        final U value;

        JustValue(U value2) {
            this.value = value2;
        }

        public U call() throws Exception {
            return this.value;
        }

        public U apply(T t) throws Exception {
            return this.value;
        }
    }

    public static <T> Callable<T> justCallable(T value) {
        return new JustValue(value);
    }

    public static <T, U> Function<T, U> justFunction(U value) {
        return new JustValue(value);
    }

    static final class CastToClass<T, U> implements Function<T, U> {
        final Class<U> clazz;

        CastToClass(Class<U> clazz2) {
            this.clazz = clazz2;
        }

        public U apply(T t) throws Exception {
            return this.clazz.cast(t);
        }
    }

    public static <T, U> Function<T, U> castFunction(Class<U> target) {
        return new CastToClass(target);
    }

    static final class ArrayListCapacityCallable<T> implements Callable<List<T>> {
        final int capacity;

        ArrayListCapacityCallable(int capacity2) {
            this.capacity = capacity2;
        }

        public List<T> call() throws Exception {
            return new ArrayList(this.capacity);
        }
    }

    public static <T> Callable<List<T>> createArrayList(int capacity) {
        return new ArrayListCapacityCallable(capacity);
    }

    static final class EqualsPredicate<T> implements Predicate<T> {
        final T value;

        EqualsPredicate(T value2) {
            this.value = value2;
        }

        public boolean test(T t) throws Exception {
            return ObjectHelper.equals(t, this.value);
        }
    }

    public static <T> Predicate<T> equalsWith(T value) {
        return new EqualsPredicate(value);
    }

    enum HashSetCallable implements Callable<Set<Object>> {
        INSTANCE;

        public Set<Object> call() throws Exception {
            return new HashSet();
        }
    }

    public static <T> Callable<Set<T>> createHashSet() {
        return HashSetCallable.INSTANCE;
    }

    static final class NotificationOnNext<T> implements Consumer<T> {
        final Consumer<? super Notification<T>> onNotification;

        NotificationOnNext(Consumer<? super Notification<T>> onNotification2) {
            this.onNotification = onNotification2;
        }

        public void accept(T v) throws Exception {
            this.onNotification.accept(Notification.createOnNext(v));
        }
    }

    static final class NotificationOnError<T> implements Consumer<Throwable> {
        final Consumer<? super Notification<T>> onNotification;

        NotificationOnError(Consumer<? super Notification<T>> onNotification2) {
            this.onNotification = onNotification2;
        }

        public void accept(Throwable v) throws Exception {
            this.onNotification.accept(Notification.createOnError(v));
        }
    }

    static final class NotificationOnComplete<T> implements Action {
        final Consumer<? super Notification<T>> onNotification;

        NotificationOnComplete(Consumer<? super Notification<T>> onNotification2) {
            this.onNotification = onNotification2;
        }

        public void run() throws Exception {
            this.onNotification.accept(Notification.createOnComplete());
        }
    }

    public static <T> Consumer<T> notificationOnNext(Consumer<? super Notification<T>> onNotification) {
        return new NotificationOnNext(onNotification);
    }

    public static <T> Consumer<Throwable> notificationOnError(Consumer<? super Notification<T>> onNotification) {
        return new NotificationOnError(onNotification);
    }

    public static <T> Action notificationOnComplete(Consumer<? super Notification<T>> onNotification) {
        return new NotificationOnComplete(onNotification);
    }

    static final class ActionConsumer<T> implements Consumer<T> {
        final Action action;

        ActionConsumer(Action action2) {
            this.action = action2;
        }

        public void accept(T t) throws Exception {
            this.action.run();
        }
    }

    public static <T> Consumer<T> actionConsumer(Action action) {
        return new ActionConsumer(action);
    }

    static final class ClassFilter<T, U> implements Predicate<T> {
        final Class<U> clazz;

        ClassFilter(Class<U> clazz2) {
            this.clazz = clazz2;
        }

        public boolean test(T t) throws Exception {
            return this.clazz.isInstance(t);
        }
    }

    public static <T, U> Predicate<T> isInstanceOf(Class<U> clazz) {
        return new ClassFilter(clazz);
    }

    static final class BooleanSupplierPredicateReverse<T> implements Predicate<T> {
        final BooleanSupplier supplier;

        BooleanSupplierPredicateReverse(BooleanSupplier supplier2) {
            this.supplier = supplier2;
        }

        public boolean test(T t) throws Exception {
            return !this.supplier.getAsBoolean();
        }
    }

    public static <T> Predicate<T> predicateReverseFor(BooleanSupplier supplier) {
        return new BooleanSupplierPredicateReverse(supplier);
    }

    static final class TimestampFunction<T> implements Function<T, Timed<T>> {
        final Scheduler scheduler;
        final TimeUnit unit;

        TimestampFunction(TimeUnit unit2, Scheduler scheduler2) {
            this.unit = unit2;
            this.scheduler = scheduler2;
        }

        public Timed<T> apply(T t) throws Exception {
            return new Timed<>(t, this.scheduler.now(this.unit), this.unit);
        }
    }

    public static <T> Function<T, Timed<T>> timestampWith(TimeUnit unit, Scheduler scheduler) {
        return new TimestampFunction(unit, scheduler);
    }

    static final class ToMapKeySelector<K, T> implements BiConsumer<Map<K, T>, T> {
        private final Function<? super T, ? extends K> keySelector;

        /* JADX DEBUG: Failed to find minimal casts for resolve overloaded methods, cast all args instead
         method: dji.thirdparty.io.reactivex.internal.functions.Functions.ToMapKeySelector.accept(java.util.Map, java.lang.Object):void
         arg types: [java.lang.Object, java.lang.Object]
         candidates:
          dji.thirdparty.io.reactivex.internal.functions.Functions.ToMapKeySelector.accept(java.lang.Object, java.lang.Object):void
          dji.thirdparty.io.reactivex.functions.BiConsumer.accept(java.lang.Object, java.lang.Object):void
          dji.thirdparty.io.reactivex.internal.functions.Functions.ToMapKeySelector.accept(java.util.Map, java.lang.Object):void */
        public /* bridge */ /* synthetic */ void accept(Object x0, Object x1) throws Exception {
            accept((Map) ((Map) x0), x1);
        }

        ToMapKeySelector(Function<? super T, ? extends K> keySelector2) {
            this.keySelector = keySelector2;
        }

        public void accept(Map<K, T> m, T t) throws Exception {
            m.put(this.keySelector.apply(t), t);
        }
    }

    public static <T, K> BiConsumer<Map<K, T>, T> toMapKeySelector(Function<? super T, ? extends K> keySelector) {
        return new ToMapKeySelector(keySelector);
    }

    static final class ToMapKeyValueSelector<K, V, T> implements BiConsumer<Map<K, V>, T> {
        private final Function<? super T, ? extends K> keySelector;
        private final Function<? super T, ? extends V> valueSelector;

        /* JADX DEBUG: Failed to find minimal casts for resolve overloaded methods, cast all args instead
         method: dji.thirdparty.io.reactivex.internal.functions.Functions.ToMapKeyValueSelector.accept(java.util.Map, java.lang.Object):void
         arg types: [java.lang.Object, java.lang.Object]
         candidates:
          dji.thirdparty.io.reactivex.internal.functions.Functions.ToMapKeyValueSelector.accept(java.lang.Object, java.lang.Object):void
          dji.thirdparty.io.reactivex.functions.BiConsumer.accept(java.lang.Object, java.lang.Object):void
          dji.thirdparty.io.reactivex.internal.functions.Functions.ToMapKeyValueSelector.accept(java.util.Map, java.lang.Object):void */
        public /* bridge */ /* synthetic */ void accept(Object x0, Object x1) throws Exception {
            accept((Map) ((Map) x0), x1);
        }

        ToMapKeyValueSelector(Function<? super T, ? extends V> valueSelector2, Function<? super T, ? extends K> keySelector2) {
            this.valueSelector = valueSelector2;
            this.keySelector = keySelector2;
        }

        public void accept(Map<K, V> m, T t) throws Exception {
            m.put(this.keySelector.apply(t), this.valueSelector.apply(t));
        }
    }

    public static <T, K, V> BiConsumer<Map<K, V>, T> toMapKeyValueSelector(Function<? super T, ? extends K> keySelector, Function<? super T, ? extends V> valueSelector) {
        return new ToMapKeyValueSelector(valueSelector, keySelector);
    }

    static final class ToMultimapKeyValueSelector<K, V, T> implements BiConsumer<Map<K, Collection<V>>, T> {
        private final Function<? super K, ? extends Collection<? super V>> collectionFactory;
        private final Function<? super T, ? extends K> keySelector;
        private final Function<? super T, ? extends V> valueSelector;

        /* JADX DEBUG: Failed to find minimal casts for resolve overloaded methods, cast all args instead
         method: dji.thirdparty.io.reactivex.internal.functions.Functions.ToMultimapKeyValueSelector.accept(java.util.Map, java.lang.Object):void
         arg types: [java.lang.Object, java.lang.Object]
         candidates:
          dji.thirdparty.io.reactivex.internal.functions.Functions.ToMultimapKeyValueSelector.accept(java.lang.Object, java.lang.Object):void
          dji.thirdparty.io.reactivex.functions.BiConsumer.accept(java.lang.Object, java.lang.Object):void
          dji.thirdparty.io.reactivex.internal.functions.Functions.ToMultimapKeyValueSelector.accept(java.util.Map, java.lang.Object):void */
        public /* bridge */ /* synthetic */ void accept(Object x0, Object x1) throws Exception {
            accept((Map) ((Map) x0), x1);
        }

        ToMultimapKeyValueSelector(Function<? super K, ? extends Collection<? super V>> collectionFactory2, Function<? super T, ? extends V> valueSelector2, Function<? super T, ? extends K> keySelector2) {
            this.collectionFactory = collectionFactory2;
            this.valueSelector = valueSelector2;
            this.keySelector = keySelector2;
        }

        public void accept(Map<K, Collection<V>> m, T t) throws Exception {
            K key = this.keySelector.apply(t);
            Collection<V> coll = m.get(key);
            if (coll == null) {
                coll = (Collection) this.collectionFactory.apply(key);
                m.put(key, coll);
            }
            coll.add(this.valueSelector.apply(t));
        }
    }

    public static <T, K, V> BiConsumer<Map<K, Collection<V>>, T> toMultimapKeyValueSelector(Function<? super T, ? extends K> keySelector, Function<? super T, ? extends V> valueSelector, Function<? super K, ? extends Collection<? super V>> collectionFactory) {
        return new ToMultimapKeyValueSelector(collectionFactory, valueSelector, keySelector);
    }

    enum NaturalComparator implements Comparator<Object> {
        INSTANCE;

        public int compare(Object o1, Object o2) {
            return ((Comparable) o1).compareTo(o2);
        }
    }

    public static <T> Comparator<T> naturalComparator() {
        return NaturalComparator.INSTANCE;
    }

    static final class ListSorter<T> implements Function<List<T>, List<T>> {
        private final Comparator<? super T> comparator;

        public /* bridge */ /* synthetic */ Object apply(Object x0) throws Exception {
            return apply((List) ((List) x0));
        }

        ListSorter(Comparator<? super T> comparator2) {
            this.comparator = comparator2;
        }

        public List<T> apply(List<T> v) {
            Collections.sort(v, this.comparator);
            return v;
        }
    }

    public static <T> Function<List<T>, List<T>> listSorter(Comparator<? super T> comparator) {
        return new ListSorter(comparator);
    }
}
