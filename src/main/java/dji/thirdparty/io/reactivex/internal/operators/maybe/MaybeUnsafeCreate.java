package dji.thirdparty.io.reactivex.internal.operators.maybe;

import dji.thirdparty.io.reactivex.MaybeObserver;
import dji.thirdparty.io.reactivex.MaybeSource;

public final class MaybeUnsafeCreate<T> extends AbstractMaybeWithUpstream<T, T> {
    public MaybeUnsafeCreate(MaybeSource<T> source) {
        super(source);
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(MaybeObserver<? super T> observer) {
        this.source.subscribe(observer);
    }
}
