package dji.thirdparty.io.reactivex.internal.operators.observable;

import dji.thirdparty.io.reactivex.ObservableSource;
import dji.thirdparty.io.reactivex.Observer;
import dji.thirdparty.io.reactivex.exceptions.Exceptions;
import dji.thirdparty.io.reactivex.functions.Function;
import dji.thirdparty.io.reactivex.internal.disposables.EmptyDisposable;
import dji.thirdparty.io.reactivex.internal.functions.ObjectHelper;
import dji.thirdparty.io.reactivex.internal.observers.BasicFuseableObserver;
import dji.thirdparty.io.reactivex.plugins.RxJavaPlugins;
import java.util.Collection;
import java.util.concurrent.Callable;

public final class ObservableDistinct<T, K> extends AbstractObservableWithUpstream<T, T> {
    final Callable<? extends Collection<? super K>> collectionSupplier;
    final Function<? super T, K> keySelector;

    public ObservableDistinct(ObservableSource<T> source, Function<? super T, K> keySelector2, Callable<? extends Collection<? super K>> collectionSupplier2) {
        super(source);
        this.keySelector = keySelector2;
        this.collectionSupplier = collectionSupplier2;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(Observer<? super T> observer) {
        try {
            this.source.subscribe(new DistinctObserver(observer, this.keySelector, (Collection) ObjectHelper.requireNonNull(this.collectionSupplier.call(), "The collectionSupplier returned a null collection. Null values are generally not allowed in 2.x operators and sources.")));
        } catch (Throwable ex) {
            Exceptions.throwIfFatal(ex);
            EmptyDisposable.error(ex, observer);
        }
    }

    static final class DistinctObserver<T, K> extends BasicFuseableObserver<T, T> {
        final Collection<? super K> collection;
        final Function<? super T, K> keySelector;

        DistinctObserver(Observer<? super T> actual, Function<? super T, K> keySelector2, Collection<? super K> collection2) {
            super(actual);
            this.keySelector = keySelector2;
            this.collection = collection2;
        }

        public void onNext(T value) {
            if (!this.done) {
                if (this.sourceMode == 0) {
                    try {
                        if (this.collection.add(ObjectHelper.requireNonNull(this.keySelector.apply(value), "The keySelector returned a null key"))) {
                            this.actual.onNext(value);
                        }
                    } catch (Throwable ex) {
                        fail(ex);
                    }
                } else {
                    this.actual.onNext(null);
                }
            }
        }

        public void onError(Throwable e) {
            if (this.done) {
                RxJavaPlugins.onError(e);
                return;
            }
            this.done = true;
            this.collection.clear();
            this.actual.onError(e);
        }

        public void onComplete() {
            if (!this.done) {
                this.done = true;
                this.collection.clear();
                this.actual.onComplete();
            }
        }

        public int requestFusion(int mode) {
            return transitiveBoundaryFusion(mode);
        }

        public T poll() throws Exception {
            T v;
            do {
                v = this.qs.poll();
                if (v == null) {
                    break;
                }
            } while (!this.collection.add(ObjectHelper.requireNonNull(this.keySelector.apply(v), "The keySelector returned a null key")));
            return v;
        }

        public void clear() {
            this.collection.clear();
            super.clear();
        }
    }
}
