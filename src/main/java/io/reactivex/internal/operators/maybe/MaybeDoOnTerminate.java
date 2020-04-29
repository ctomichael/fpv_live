package io.reactivex.internal.operators.maybe;

import io.reactivex.Maybe;
import io.reactivex.MaybeObserver;
import io.reactivex.MaybeSource;
import io.reactivex.disposables.Disposable;
import io.reactivex.exceptions.CompositeException;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.functions.Action;

public final class MaybeDoOnTerminate<T> extends Maybe<T> {
    final Action onTerminate;
    final MaybeSource<T> source;

    public MaybeDoOnTerminate(MaybeSource<T> source2, Action onTerminate2) {
        this.source = source2;
        this.onTerminate = onTerminate2;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(MaybeObserver<? super T> observer) {
        this.source.subscribe(new DoOnTerminate(observer));
    }

    final class DoOnTerminate implements MaybeObserver<T> {
        final MaybeObserver<? super T> downstream;

        DoOnTerminate(MaybeObserver<? super T> observer) {
            this.downstream = observer;
        }

        public void onSubscribe(Disposable d) {
            this.downstream.onSubscribe(d);
        }

        public void onSuccess(T value) {
            try {
                MaybeDoOnTerminate.this.onTerminate.run();
                this.downstream.onSuccess(value);
            } catch (Throwable ex) {
                Exceptions.throwIfFatal(ex);
                this.downstream.onError(ex);
            }
        }

        public void onError(Throwable e) {
            try {
                MaybeDoOnTerminate.this.onTerminate.run();
            } catch (Throwable ex) {
                Exceptions.throwIfFatal(ex);
                e = new CompositeException(e, ex);
            }
            this.downstream.onError(e);
        }

        public void onComplete() {
            try {
                MaybeDoOnTerminate.this.onTerminate.run();
                this.downstream.onComplete();
            } catch (Throwable ex) {
                Exceptions.throwIfFatal(ex);
                this.downstream.onError(ex);
            }
        }
    }
}
