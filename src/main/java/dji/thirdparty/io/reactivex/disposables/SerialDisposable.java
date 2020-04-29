package dji.thirdparty.io.reactivex.disposables;

import dji.thirdparty.io.reactivex.internal.disposables.DisposableHelper;
import java.util.concurrent.atomic.AtomicReference;

public final class SerialDisposable implements Disposable {
    final AtomicReference<Disposable> resource;

    public SerialDisposable() {
        this.resource = new AtomicReference<>();
    }

    public SerialDisposable(Disposable initialDisposable) {
        this.resource = new AtomicReference<>(initialDisposable);
    }

    public boolean set(Disposable next) {
        return DisposableHelper.set(this.resource, next);
    }

    public boolean replace(Disposable next) {
        return DisposableHelper.replace(this.resource, next);
    }

    public Disposable get() {
        Disposable d = this.resource.get();
        if (d == DisposableHelper.DISPOSED) {
            return Disposables.disposed();
        }
        return d;
    }

    public void dispose() {
        DisposableHelper.dispose(this.resource);
    }

    public boolean isDisposed() {
        return DisposableHelper.isDisposed(this.resource.get());
    }
}
