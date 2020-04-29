package dji.thirdparty.io.reactivex.internal.util;

import dji.thirdparty.io.reactivex.disposables.Disposable;
import dji.thirdparty.io.reactivex.functions.Consumer;

public final class ConnectConsumer implements Consumer<Disposable> {
    public Disposable disposable;

    public void accept(Disposable t) throws Exception {
        this.disposable = t;
    }
}
