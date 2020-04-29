package io.reactivex.internal.operators.mixed;

import io.reactivex.CompletableObserver;
import io.reactivex.CompletableSource;
import io.reactivex.MaybeSource;
import io.reactivex.Observer;
import io.reactivex.SingleSource;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.functions.Function;
import io.reactivex.internal.disposables.EmptyDisposable;
import io.reactivex.internal.functions.ObjectHelper;
import io.reactivex.internal.operators.maybe.MaybeToObservable;
import io.reactivex.internal.operators.single.SingleToObservable;
import java.util.concurrent.Callable;

final class ScalarXMapZHelper {
    private ScalarXMapZHelper() {
        throw new IllegalStateException("No instances!");
    }

    static <T> boolean tryAsCompletable(Object source, Function<? super T, ? extends CompletableSource> mapper, CompletableObserver observer) {
        if (!(source instanceof Callable)) {
            return false;
        }
        CompletableSource cs = null;
        try {
            T item = ((Callable) source).call();
            if (item != null) {
                cs = (CompletableSource) ObjectHelper.requireNonNull(mapper.apply(item), "The mapper returned a null CompletableSource");
            }
            if (cs == null) {
                EmptyDisposable.complete(observer);
                return true;
            }
            cs.subscribe(observer);
            return true;
        } catch (Throwable ex) {
            Exceptions.throwIfFatal(ex);
            EmptyDisposable.error(ex, observer);
            return true;
        }
    }

    static <T, R> boolean tryAsMaybe(Object source, Function<? super T, ? extends MaybeSource<? extends R>> mapper, Observer<? super R> observer) {
        if (!(source instanceof Callable)) {
            return false;
        }
        MaybeSource<? extends R> cs = null;
        try {
            T item = ((Callable) source).call();
            if (item != null) {
                cs = (MaybeSource) ObjectHelper.requireNonNull(mapper.apply(item), "The mapper returned a null MaybeSource");
            }
            if (cs == null) {
                EmptyDisposable.complete(observer);
                return true;
            }
            cs.subscribe(MaybeToObservable.create(observer));
            return true;
        } catch (Throwable ex) {
            Exceptions.throwIfFatal(ex);
            EmptyDisposable.error(ex, observer);
            return true;
        }
    }

    static <T, R> boolean tryAsSingle(Object source, Function<? super T, ? extends SingleSource<? extends R>> mapper, Observer<? super R> observer) {
        if (!(source instanceof Callable)) {
            return false;
        }
        SingleSource<? extends R> cs = null;
        try {
            T item = ((Callable) source).call();
            if (item != null) {
                cs = (SingleSource) ObjectHelper.requireNonNull(mapper.apply(item), "The mapper returned a null SingleSource");
            }
            if (cs == null) {
                EmptyDisposable.complete(observer);
                return true;
            }
            cs.subscribe(SingleToObservable.create(observer));
            return true;
        } catch (Throwable ex) {
            Exceptions.throwIfFatal(ex);
            EmptyDisposable.error(ex, observer);
            return true;
        }
    }
}
