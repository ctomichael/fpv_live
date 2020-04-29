package io.reactivex.internal.operators.maybe;

import io.reactivex.Maybe;
import io.reactivex.MaybeObserver;
import io.reactivex.MaybeSource;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.internal.disposables.EmptyDisposable;
import io.reactivex.plugins.RxJavaPlugins;
import java.util.Iterator;
import java.util.concurrent.atomic.AtomicBoolean;

public final class MaybeAmb<T> extends Maybe<T> {
    private final MaybeSource<? extends T>[] sources;
    private final Iterable<? extends MaybeSource<? extends T>> sourcesIterable;

    public MaybeAmb(MaybeSource<? extends T>[] sources2, Iterable<? extends MaybeSource<? extends T>> sourcesIterable2) {
        this.sources = sources2;
        this.sourcesIterable = sourcesIterable2;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(MaybeObserver<? super T> observer) {
        int count;
        MaybeSource<? extends T>[] sources2 = this.sources;
        int count2 = 0;
        if (sources2 == null) {
            sources2 = new MaybeSource[8];
            try {
                Iterator<? extends MaybeSource<? extends T>> it2 = this.sourcesIterable.iterator();
                while (true) {
                    try {
                        int count3 = count2;
                        if (!it2.hasNext()) {
                            count = count3;
                            break;
                        }
                        MaybeSource<? extends T> element = (MaybeSource) it2.next();
                        if (element == null) {
                            EmptyDisposable.error(new NullPointerException("One of the sources is null"), observer);
                            int i = count3;
                            return;
                        }
                        if (count3 == sources2.length) {
                            MaybeSource<? extends T>[] b = new MaybeSource[((count3 >> 2) + count3)];
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
        CompositeDisposable set = new CompositeDisposable();
        observer.onSubscribe(set);
        AtomicBoolean winner = new AtomicBoolean();
        int i2 = 0;
        while (i2 < count) {
            MaybeSource<? extends T> s = sources2[i2];
            if (set.isDisposed()) {
                return;
            }
            if (s == null) {
                set.dispose();
                NullPointerException ex = new NullPointerException("One of the MaybeSources is null");
                if (winner.compareAndSet(false, true)) {
                    observer.onError(ex);
                    return;
                } else {
                    RxJavaPlugins.onError(ex);
                    return;
                }
            } else {
                s.subscribe(new AmbMaybeObserver(observer, set, winner));
                i2++;
            }
        }
        if (count == 0) {
            observer.onComplete();
        }
    }

    static final class AmbMaybeObserver<T> implements MaybeObserver<T> {
        final MaybeObserver<? super T> downstream;
        final CompositeDisposable set;
        Disposable upstream;
        final AtomicBoolean winner;

        AmbMaybeObserver(MaybeObserver<? super T> downstream2, CompositeDisposable set2, AtomicBoolean winner2) {
            this.downstream = downstream2;
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

        public void onComplete() {
            if (this.winner.compareAndSet(false, true)) {
                this.set.delete(this.upstream);
                this.set.dispose();
                this.downstream.onComplete();
            }
        }
    }
}
