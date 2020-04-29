package dji.internal.diagnostics.handler;

import dji.internal.diagnostics.handler.util.DiagnosticsModel;
import dji.thirdparty.io.reactivex.functions.Function;
import dji.utils.Optional;

final /* synthetic */ class NavigationDiagnosticsHandler$$Lambda$6 implements Function {
    private final DiagnosticsModel arg$1;

    NavigationDiagnosticsHandler$$Lambda$6(DiagnosticsModel diagnosticsModel) {
        this.arg$1 = diagnosticsModel;
    }

    public Object apply(Object obj) {
        return Boolean.valueOf(this.arg$1.statusApply(((Optional) obj).orElse(null)));
    }
}
