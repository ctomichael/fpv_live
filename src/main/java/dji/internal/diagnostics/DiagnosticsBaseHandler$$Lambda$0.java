package dji.internal.diagnostics;

final /* synthetic */ class DiagnosticsBaseHandler$$Lambda$0 implements Runnable {
    private final DiagnosticsBaseHandler arg$1;

    DiagnosticsBaseHandler$$Lambda$0(DiagnosticsBaseHandler diagnosticsBaseHandler) {
        this.arg$1 = diagnosticsBaseHandler;
    }

    public void run() {
        this.arg$1.notifyChange();
    }
}
