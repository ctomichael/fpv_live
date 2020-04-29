package dji.thirdparty.io.reactivex.disposables;

import dji.thirdparty.io.reactivex.internal.functions.ObjectHelper;
import java.util.concurrent.atomic.AtomicReference;

abstract class ReferenceDisposable<T> extends AtomicReference<T> implements Disposable {
    private static final long serialVersionUID = 6537757548749041217L;

    /* access modifiers changed from: protected */
    public abstract void onDisposed(Object obj);

    ReferenceDisposable(T value) {
        super(ObjectHelper.requireNonNull(value, "value is null"));
    }

    public final void dispose() {
        T value;
        if (get() != null && (value = getAndSet(null)) != null) {
            onDisposed(value);
        }
    }

    public final boolean isDisposed() {
        return get() == null;
    }
}
