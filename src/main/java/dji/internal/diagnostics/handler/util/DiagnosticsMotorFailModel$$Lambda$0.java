package dji.internal.diagnostics.handler.util;

import dji.thirdparty.io.reactivex.functions.Function;
import dji.utils.Optional;

final /* synthetic */ class DiagnosticsMotorFailModel$$Lambda$0 implements Function {
    static final Function $instance = new DiagnosticsMotorFailModel$$Lambda$0();

    private DiagnosticsMotorFailModel$$Lambda$0() {
    }

    public Object apply(Object obj) {
        return DiagnosticsMotorFailModel.lambda$observeTempLevel$0$DiagnosticsMotorFailModel((Optional) obj);
    }
}
