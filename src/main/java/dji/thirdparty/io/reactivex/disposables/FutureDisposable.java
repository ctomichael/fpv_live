package dji.thirdparty.io.reactivex.disposables;

import java.util.concurrent.Future;

final class FutureDisposable extends ReferenceDisposable<Future<?>> {
    private static final long serialVersionUID = 6545242830671168775L;
    private final boolean allowInterrupt;

    /* access modifiers changed from: protected */
    public /* bridge */ /* synthetic */ void onDisposed(Object x0) {
        onDisposed((Future<?>) ((Future) x0));
    }

    FutureDisposable(Future<?> run, boolean allowInterrupt2) {
        super(run);
        this.allowInterrupt = allowInterrupt2;
    }

    /* access modifiers changed from: protected */
    public void onDisposed(Future<?> value) {
        value.cancel(this.allowInterrupt);
    }
}
