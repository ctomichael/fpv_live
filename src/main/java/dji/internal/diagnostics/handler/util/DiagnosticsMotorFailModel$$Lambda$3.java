package dji.internal.diagnostics.handler.util;

import dji.common.battery.BatterySOPTemperatureLevel;
import dji.thirdparty.io.reactivex.functions.Consumer;

final /* synthetic */ class DiagnosticsMotorFailModel$$Lambda$3 implements Consumer {
    private final DiagnosticsMotorFailModel arg$1;

    DiagnosticsMotorFailModel$$Lambda$3(DiagnosticsMotorFailModel diagnosticsMotorFailModel) {
        this.arg$1 = diagnosticsMotorFailModel;
    }

    public void accept(Object obj) {
        this.arg$1.lambda$observeTempLevel$3$DiagnosticsMotorFailModel((BatterySOPTemperatureLevel) obj);
    }
}
