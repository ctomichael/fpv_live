package dji.internal.diagnostics.handler;

import dji.midware.data.model.P3.DataSmartBatteryGetPushDynamicData;

final /* synthetic */ class BatteryDiagnosticsHandler$$Lambda$6 implements Runnable {
    private final BatteryDiagnosticsHandler arg$1;
    private final DataSmartBatteryGetPushDynamicData arg$2;

    BatteryDiagnosticsHandler$$Lambda$6(BatteryDiagnosticsHandler batteryDiagnosticsHandler, DataSmartBatteryGetPushDynamicData dataSmartBatteryGetPushDynamicData) {
        this.arg$1 = batteryDiagnosticsHandler;
        this.arg$2 = dataSmartBatteryGetPushDynamicData;
    }

    public void run() {
        this.arg$1.lambda$onEvent3BackgroundThread$5$BatteryDiagnosticsHandler(this.arg$2);
    }
}
