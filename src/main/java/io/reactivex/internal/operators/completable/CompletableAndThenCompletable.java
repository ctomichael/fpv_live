package io.reactivex.internal.operators.completable;

import io.reactivex.Completable;
import io.reactivex.CompletableObserver;
import io.reactivex.CompletableSource;
import io.reactivex.disposables.Disposable;
import io.reactivex.internal.disposables.DisposableHelper;
import java.util.concurrent.atomic.AtomicReference;

public final class CompletableAndThenCompletable extends Completable {
    final CompletableSource next;
    final CompletableSource source;

    public CompletableAndThenCompletable(CompletableSource source2, CompletableSource next2) {
        this.source = source2;
        this.next = next2;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(CompletableObserver observer) {
        this.source.subscribe(new SourceObserver(observer, this.next));
    }

    static final class SourceObserver extends AtomicReference<Disposable> implements CompletableObserver, Disposable {
        private static final long serialVersionUID = -4101678820158072998L;
        final CompletableObserver actualObserver;
        final CompletableSource next;

        SourceObserver(CompletableObserver actualObserver2, CompletableSource next2) {
            this.actualObserver = actualObserver2;
            this.next = next2;
        }

        public void onSubscribe(Disposable d) {
            if (DisposableHelper.setOnce(this, d)) {
                this.actualObserver.onSubscribe(this);
            }
        }

        public void onError(Throwable e) {
            this.actualObserver.onError(e);
        }

        public void onComplete() {
            this.next.subscribe(new NextObserver(this, this.actualObserver));
        }

        public void dispose() {
            DisposableHelper.dispose(this);
        }

        public boolean isDisposed() {
            return DisposableHelper.isDisposed((Disposable) get());
        }
    }

    static final class NextObserver implements CompletableObserver {
        final CompletableObserver downstream;
        final AtomicReference<Disposable> parent;

        public NextObserver(AtomicReference<Disposable> parent2, CompletableObserver downstream2) {
            this.parent = parent2;
            this.downstream = downstream2;
        }

        public void onSubscribe(Disposable d) {
            DisposableHelper.replace(this.parent, d);
        }

        public void onComplete() {
            this.downstream.onComplete();
        }

        public void onError(Throwable e) {
            this.downstream.onError(e);
        }
    }
}
