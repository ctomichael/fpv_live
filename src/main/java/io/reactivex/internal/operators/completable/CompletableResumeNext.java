package io.reactivex.internal.operators.completable;

import io.reactivex.Completable;
import io.reactivex.CompletableObserver;
import io.reactivex.CompletableSource;
import io.reactivex.disposables.Disposable;
import io.reactivex.exceptions.CompositeException;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.functions.Function;
import io.reactivex.internal.disposables.DisposableHelper;
import io.reactivex.internal.functions.ObjectHelper;
import java.util.concurrent.atomic.AtomicReference;

public final class CompletableResumeNext extends Completable {
    final Function<? super Throwable, ? extends CompletableSource> errorMapper;
    final CompletableSource source;

    public CompletableResumeNext(CompletableSource source2, Function<? super Throwable, ? extends CompletableSource> errorMapper2) {
        this.source = source2;
        this.errorMapper = errorMapper2;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(CompletableObserver observer) {
        ResumeNextObserver parent = new ResumeNextObserver(observer, this.errorMapper);
        observer.onSubscribe(parent);
        this.source.subscribe(parent);
    }

    static final class ResumeNextObserver extends AtomicReference<Disposable> implements CompletableObserver, Disposable {
        private static final long serialVersionUID = 5018523762564524046L;
        final CompletableObserver downstream;
        final Function<? super Throwable, ? extends CompletableSource> errorMapper;
        boolean once;

        ResumeNextObserver(CompletableObserver observer, Function<? super Throwable, ? extends CompletableSource> errorMapper2) {
            this.downstream = observer;
            this.errorMapper = errorMapper2;
        }

        public void onSubscribe(Disposable d) {
            DisposableHelper.replace(this, d);
        }

        public void onComplete() {
            this.downstream.onComplete();
        }

        public void onError(Throwable e) {
            if (this.once) {
                this.downstream.onError(e);
                return;
            }
            this.once = true;
            try {
                ((CompletableSource) ObjectHelper.requireNonNull(this.errorMapper.apply(e), "The errorMapper returned a null CompletableSource")).subscribe(this);
            } catch (Throwable ex) {
                Exceptions.throwIfFatal(ex);
                this.downstream.onError(new CompositeException(e, ex));
            }
        }

        public boolean isDisposed() {
            return DisposableHelper.isDisposed((Disposable) get());
        }

        public void dispose() {
            DisposableHelper.dispose(this);
        }
    }
}
