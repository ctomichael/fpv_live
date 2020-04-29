package dji.internal.diagnostics.handler;

import dji.internal.diagnostics.handler.BatteryDiagnosticsHandler;

final /* synthetic */ class BatteryDiagnosticsHandler$1$$Lambda$0 implements Runnable {
    private final BatteryDiagnosticsHandler.AnonymousClass1 arg$1;

    BatteryDiagnosticsHandler$1$$Lambda$0(BatteryDiagnosticsHandler.AnonymousClass1 r1) {
        this.arg$1 = r1;
    }

    public void run() {
        this.arg$1.lambda$onBatteryBanListUpdate$0$BatteryDiagnosticsHandler$1();
    }
}
