package dji.internal.diagnostics.handler;

final /* synthetic */ class BatteryDiagnosticsHandler$$Lambda$2 implements Runnable {
    private final BatteryDiagnosticsHandler arg$1;

    BatteryDiagnosticsHandler$$Lambda$2(BatteryDiagnosticsHandler batteryDiagnosticsHandler) {
        this.arg$1 = batteryDiagnosticsHandler;
    }

    public void run() {
        this.arg$1.lambda$onEvent3BackgroundThread$1$BatteryDiagnosticsHandler();
    }
}
