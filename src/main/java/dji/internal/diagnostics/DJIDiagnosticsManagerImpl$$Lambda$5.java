package dji.internal.diagnostics;

import dji.diagnostics.DiagnosticsInformationListener;
import java.util.List;

final /* synthetic */ class DJIDiagnosticsManagerImpl$$Lambda$5 implements Runnable {
    private final DiagnosticsInformationListener arg$1;
    private final List arg$2;

    DJIDiagnosticsManagerImpl$$Lambda$5(DiagnosticsInformationListener diagnosticsInformationListener, List list) {
        this.arg$1 = diagnosticsInformationListener;
        this.arg$2 = list;
    }

    public void run() {
        this.arg$1.onUpdate(this.arg$2);
    }
}
