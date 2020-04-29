package dji.internal.diagnostics.handler;

import dji.midware.data.model.P3.DataFlycGetPushSmartBattery;

final /* synthetic */ class BatteryDiagnosticsHandler$$Lambda$4 implements Runnable {
    private final BatteryDiagnosticsHandler arg$1;
    private final DataFlycGetPushSmartBattery arg$2;

    BatteryDiagnosticsHandler$$Lambda$4(BatteryDiagnosticsHandler batteryDiagnosticsHandler, DataFlycGetPushSmartBattery dataFlycGetPushSmartBattery) {
        this.arg$1 = batteryDiagnosticsHandler;
        this.arg$2 = dataFlycGetPushSmartBattery;
    }

    public void run() {
        this.arg$1.lambda$onEvent3BackgroundThread$3$BatteryDiagnosticsHandler(this.arg$2);
    }
}
