package dji.thirdparty.rx.plugins;

import dji.thirdparty.rx.annotations.Beta;
import dji.thirdparty.rx.exceptions.Exceptions;

public abstract class RxJavaErrorHandler {
    protected static final String ERROR_IN_RENDERING_SUFFIX = ".errorRendering";

    public void handleError(Throwable e) {
    }

    @Beta
    public final String handleOnNextValueRendering(Object item) {
        try {
            return render(item);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } catch (Throwable t) {
            Exceptions.throwIfFatal(t);
        }
        return item.getClass().getName() + ERROR_IN_RENDERING_SUFFIX;
    }

    /* access modifiers changed from: protected */
    @Beta
    public String render(Object item) throws InterruptedException {
        return null;
    }
}
