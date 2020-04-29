package dji.internal.diagnostics.handler.util;

import dji.common.battery.BatterySOPTemperatureLevel;
import dji.thirdparty.io.reactivex.functions.Predicate;

final /* synthetic */ class DiagnosticsMotorFailModel$$Lambda$2 implements Predicate {
    private final DiagnosticsMotorFailModel arg$1;

    DiagnosticsMotorFailModel$$Lambda$2(DiagnosticsMotorFailModel diagnosticsMotorFailModel) {
        this.arg$1 = diagnosticsMotorFailModel;
    }

    public boolean test(Object obj) {
        return this.arg$1.lambda$observeTempLevel$2$DiagnosticsMotorFailModel((BatterySOPTemperatureLevel) obj);
    }
}
