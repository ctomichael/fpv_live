package dji.thirdparty.io.reactivex.disposables;

import dji.thirdparty.io.reactivex.functions.Action;
import dji.thirdparty.io.reactivex.internal.util.ExceptionHelper;

final class ActionDisposable extends ReferenceDisposable<Action> {
    private static final long serialVersionUID = -8219729196779211169L;

    ActionDisposable(Action value) {
        super(value);
    }

    /* access modifiers changed from: protected */
    public void onDisposed(Action value) {
        try {
            value.run();
        } catch (Throwable ex) {
            throw ExceptionHelper.wrapOrThrow(ex);
        }
    }
}
