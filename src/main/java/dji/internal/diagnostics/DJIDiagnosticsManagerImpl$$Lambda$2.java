package dji.internal.diagnostics;

import dji.diagnostics.DiagnosticsInformationListener;

final /* synthetic */ class DJIDiagnosticsManagerImpl$$Lambda$2 implements Runnable {
    private final DJIDiagnosticsManagerImpl arg$1;
    private final DiagnosticsInformationListener arg$2;

    DJIDiagnosticsManagerImpl$$Lambda$2(DJIDiagnosticsManagerImpl dJIDiagnosticsManagerImpl, DiagnosticsInformationListener diagnosticsInformationListener) {
        this.arg$1 = dJIDiagnosticsManagerImpl;
        this.arg$2 = diagnosticsInformationListener;
    }

    public void run() {
        this.arg$1.lambda$addDiagnosticsInformationListener$2$DJIDiagnosticsManagerImpl(this.arg$2);
    }
}
