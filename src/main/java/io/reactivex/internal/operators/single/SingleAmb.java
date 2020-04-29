package io.reactivex.internal.operators.single;

import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.SingleSource;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.internal.disposables.EmptyDisposable;
import io.reactivex.plugins.RxJavaPlugins;
import java.util.Iterator;
import java.util.concurrent.atomic.AtomicBoolean;

public final class SingleAmb<T> extends Single<T> {
    private final SingleSource<? extends T>[] sources;
    private final Iterable<? extends SingleSource<? extends T>> sourcesIterable;

    public SingleAmb(SingleSource<? extends T>[] sources2, Iterable<? extends SingleSource<? extends T>> sourcesIterable2) {
        this.sources = sources2;
        this.sourcesIterable = sourcesIterable2;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(SingleObserver<? super T> observer) {
        int count;
        SingleSource<? extends T>[] sources2 = this.sources;
        int count2 = 0;
        if (sources2 == null) {
            sources2 = new SingleSource[8];
            try {
                Iterator<? extends SingleSource<? extends T>> it2 = this.sourcesIterable.iterator();
                while (true) {
                    try {
                        int count3 = count2;
                        if (!it2.hasNext()) {
                            count = count3;
                            break;
                        }
                        SingleSource<? extends T> element = (SingleSource) it2.next();
                        if (element == null) {
                            EmptyDisposable.error(new NullPointerException("One of the sources is null"), observer);
                            int i = count3;
                            return;
                        }
                        if (count3 == sources2.length) {
                            SingleSource<? extends T>[] b = new SingleSource[((count3 >> 2) + count3)];
                            System.arraycopy(sources2, 0, b, 0, count3);
                            sources2 = b;
                        }
                        count2 = count3 + 1;
                        sources2[count3] = element;
                    } catch (Throwable th) {
                        e = th;
                        Exceptions.throwIfFatal(e);
                        EmptyDisposable.error(e, observer);
                        return;
                    }
                }
            } catch (Throwable th2) {
                e = th2;
            }
        } else {
            count = sources2.length;
        }
        AtomicBoolean winner = new AtomicBoolean();
        CompositeDisposable set = new CompositeDisposable();
        observer.onSubscribe(set);
        int i2 = 0;
        while (i2 < count) {
            SingleSource<? extends T> s1 = sources2[i2];
            if (set.isDisposed()) {
                return;
            }
            if (s1 == null) {
                set.dispose();
                Throwable e = new NullPointerException("One of the sources is null");
                if (winner.compareAndSet(false, true)) {
                    observer.onError(e);
                    return;
                } else {
                    RxJavaPlugins.onError(e);
                    return;
                }
            } else {
                s1.subscribe(new AmbSingleObserver(observer, set, winner));
                i2++;
            }
        }
    }

    static final class AmbSingleObserver<T> implements SingleObserver<T> {
        final SingleObserver<? super T> downstream;
        final CompositeDisposable set;
        Disposable upstream;
        final AtomicBoolean winner;

        AmbSingleObserver(SingleObserver<? super T> observer, CompositeDisposable set2, AtomicBoolean winner2) {
            this.downstream = observer;
            this.set = set2;
            this.winner = winner2;
        }

        public void onSubscribe(Disposable d) {
            this.upstream = d;
            this.set.add(d);
        }

        public void onSuccess(T value) {
            if (this.winner.compareAndSet(false, true)) {
                this.set.delete(this.upstream);
                this.set.dispose();
                this.downstream.onSuccess(value);
            }
        }

        public void onError(Throwable e) {
            if (this.winner.compareAndSet(false, true)) {
                this.set.delete(this.upstream);
                this.set.dispose();
                this.downstream.onError(e);
                return;
            }
            RxJavaPlugins.onError(e);
        }
    }
}
