package io.reactivex.internal.operators.maybe;

import io.reactivex.MaybeObserver;
import io.reactivex.MaybeSource;
import io.reactivex.disposables.Disposable;
import io.reactivex.exceptions.CompositeException;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.internal.disposables.DisposableHelper;
import io.reactivex.internal.disposables.EmptyDisposable;
import io.reactivex.plugins.RxJavaPlugins;

public final class MaybePeek<T> extends AbstractMaybeWithUpstream<T, T> {
    final Action onAfterTerminate;
    final Action onCompleteCall;
    final Action onDisposeCall;
    final Consumer<? super Throwable> onErrorCall;
    final Consumer<? super Disposable> onSubscribeCall;
    final Consumer<? super T> onSuccessCall;

    public MaybePeek(MaybeSource<T> source, Consumer<? super Disposable> onSubscribeCall2, Consumer<? super T> onSuccessCall2, Consumer<? super Throwable> onErrorCall2, Action onCompleteCall2, Action onAfterTerminate2, Action onDispose) {
        super(source);
        this.onSubscribeCall = onSubscribeCall2;
        this.onSuccessCall = onSuccessCall2;
        this.onErrorCall = onErrorCall2;
        this.onCompleteCall = onCompleteCall2;
        this.onAfterTerminate = onAfterTerminate2;
        this.onDisposeCall = onDispose;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(MaybeObserver<? super T> observer) {
        this.source.subscribe(new MaybePeekObserver(observer, this));
    }

    static final class MaybePeekObserver<T> implements MaybeObserver<T>, Disposable {
        final MaybeObserver<? super T> downstream;
        final MaybePeek<T> parent;
        Disposable upstream;

        MaybePeekObserver(MaybeObserver<? super T> actual, MaybePeek<T> parent2) {
            this.downstream = actual;
            this.parent = parent2;
        }

        public void dispose() {
            try {
                this.parent.onDisposeCall.run();
            } catch (Throwable ex) {
                Exceptions.throwIfFatal(ex);
                RxJavaPlugins.onError(ex);
            }
            this.upstream.dispose();
            this.upstream = DisposableHelper.DISPOSED;
        }

        public boolean isDisposed() {
            return this.upstream.isDisposed();
        }

        public void onSubscribe(Disposable d) {
            if (DisposableHelper.validate(this.upstream, d)) {
                try {
                    this.parent.onSubscribeCall.accept(d);
                    this.upstream = d;
                    this.downstream.onSubscribe(this);
                } catch (Throwable ex) {
                    Exceptions.throwIfFatal(ex);
                    d.dispose();
                    this.upstream = DisposableHelper.DISPOSED;
                    EmptyDisposable.error(ex, this.downstream);
                }
            }
        }

        public void onSuccess(T value) {
            if (this.upstream != DisposableHelper.DISPOSED) {
                try {
                    this.parent.onSuccessCall.accept(value);
                    this.upstream = DisposableHelper.DISPOSED;
                    this.downstream.onSuccess(value);
                    onAfterTerminate();
                } catch (Throwable ex) {
                    Exceptions.throwIfFatal(ex);
                    onErrorInner(ex);
                }
            }
        }

        public void onError(Throwable e) {
            if (this.upstream == DisposableHelper.DISPOSED) {
                RxJavaPlugins.onError(e);
            } else {
                onErrorInner(e);
            }
        }

        /* access modifiers changed from: package-private */
        public void onErrorInner(Throwable e) {
            try {
                this.parent.onErrorCall.accept(e);
            } catch (Throwable ex) {
                Exceptions.throwIfFatal(ex);
                e = new CompositeException(e, ex);
            }
            this.upstream = DisposableHelper.DISPOSED;
            this.downstream.onError(e);
            onAfterTerminate();
        }

        public void onComplete() {
            if (this.upstream != DisposableHelper.DISPOSED) {
                try {
                    this.parent.onCompleteCall.run();
                    this.upstream = DisposableHelper.DISPOSED;
                    this.downstream.onComplete();
                    onAfterTerminate();
                } catch (Throwable ex) {
                    Exceptions.throwIfFatal(ex);
                    onErrorInner(ex);
                }
            }
        }

        /* access modifiers changed from: package-private */
        public void onAfterTerminate() {
            try {
                this.parent.onAfterTerminate.run();
            } catch (Throwable ex) {
                Exceptions.throwIfFatal(ex);
                RxJavaPlugins.onError(ex);
            }
        }
    }
}
