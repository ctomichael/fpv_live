package dji.internal.diagnostics.handler;

final /* synthetic */ class BatteryDiagnosticsHandler$$Lambda$3 implements Runnable {
    private final BatteryDiagnosticsHandler arg$1;
    private final boolean arg$2;

    BatteryDiagnosticsHandler$$Lambda$3(BatteryDiagnosticsHandler batteryDiagnosticsHandler, boolean z) {
        this.arg$1 = batteryDiagnosticsHandler;
        this.arg$2 = z;
    }

    public void run() {
        this.arg$1.lambda$onEvent3BackgroundThread$2$BatteryDiagnosticsHandler(this.arg$2);
    }
}
