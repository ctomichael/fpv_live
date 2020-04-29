package dji.thirdparty.io.reactivex.internal.operators.observable;

import dji.thirdparty.io.reactivex.Emitter;
import dji.thirdparty.io.reactivex.Observable;
import dji.thirdparty.io.reactivex.Observer;
import dji.thirdparty.io.reactivex.disposables.Disposable;
import dji.thirdparty.io.reactivex.exceptions.Exceptions;
import dji.thirdparty.io.reactivex.functions.BiFunction;
import dji.thirdparty.io.reactivex.functions.Consumer;
import dji.thirdparty.io.reactivex.internal.disposables.EmptyDisposable;
import dji.thirdparty.io.reactivex.plugins.RxJavaPlugins;
import java.util.concurrent.Callable;

public final class ObservableGenerate<T, S> extends Observable<T> {
    final Consumer<? super S> disposeState;
    final BiFunction<S, Emitter<T>, S> generator;
    final Callable<S> stateSupplier;

    public ObservableGenerate(Callable<S> stateSupplier2, BiFunction<S, Emitter<T>, S> generator2, Consumer<? super S> disposeState2) {
        this.stateSupplier = stateSupplier2;
        this.generator = generator2;
        this.disposeState = disposeState2;
    }

    public void subscribeActual(Observer<? super T> s) {
        try {
            GeneratorDisposable<T, S> gd = new GeneratorDisposable<>(s, this.generator, this.disposeState, this.stateSupplier.call());
            s.onSubscribe(gd);
            gd.run();
        } catch (Throwable e) {
            Exceptions.throwIfFatal(e);
            EmptyDisposable.error(e, s);
        }
    }

    static final class GeneratorDisposable<T, S> implements Emitter<T>, Disposable {
        final Observer<? super T> actual;
        volatile boolean cancelled;
        final Consumer<? super S> disposeState;
        final BiFunction<S, ? super Emitter<T>, S> generator;
        S state;
        boolean terminate;

        GeneratorDisposable(Observer<? super T> actual2, BiFunction<S, ? super Emitter<T>, S> generator2, Consumer<? super S> disposeState2, S initialState) {
            this.actual = actual2;
            this.generator = generator2;
            this.disposeState = disposeState2;
            this.state = initialState;
        }

        public void run() {
            S s = this.state;
            if (this.cancelled) {
                this.state = null;
                dispose(s);
                return;
            }
            BiFunction<S, ? super Emitter<T>, S> f = this.generator;
            while (!this.cancelled) {
                try {
                    s = f.apply(s, this);
                    if (this.terminate) {
                        this.cancelled = true;
                        this.state = null;
                        dispose(s);
                        return;
                    }
                } catch (Throwable ex) {
                    Exceptions.throwIfFatal(ex);
                    this.state = null;
                    this.cancelled = true;
                    this.actual.onError(ex);
                    return;
                }
            }
            this.state = null;
            dispose(s);
        }

        private void dispose(S s) {
            try {
                this.disposeState.accept(s);
            } catch (Throwable ex) {
                Exceptions.throwIfFatal(ex);
                RxJavaPlugins.onError(ex);
            }
        }

        public void dispose() {
            this.cancelled = true;
        }

        public boolean isDisposed() {
            return this.cancelled;
        }

        public void onNext(T t) {
            if (t == null) {
                onError(new NullPointerException("onNext called with null. Null values are generally not allowed in 2.x operators and sources."));
            } else {
                this.actual.onNext(t);
            }
        }

        public void onError(Throwable t) {
            if (t == null) {
                t = new NullPointerException("onError called with null. Null values are generally not allowed in 2.x operators and sources.");
            }
            this.terminate = true;
            this.actual.onError(t);
        }

        public void onComplete() {
            this.terminate = true;
            this.actual.onComplete();
        }
    }
}
