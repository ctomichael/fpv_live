package dji.internal.diagnostics.handler;

import dji.internal.diagnostics.handler.util.UpdateInterface;
import dji.thirdparty.io.reactivex.functions.Consumer;

final /* synthetic */ class NavigationDiagnosticsHandler$$Lambda$7 implements Consumer {
    private final UpdateInterface arg$1;

    NavigationDiagnosticsHandler$$Lambda$7(UpdateInterface updateInterface) {
        this.arg$1 = updateInterface;
    }

    public void accept(Object obj) {
        this.arg$1.onStatusUpdate();
    }
}
