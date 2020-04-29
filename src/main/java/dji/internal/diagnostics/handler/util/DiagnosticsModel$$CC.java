package dji.internal.diagnostics.handler.util;

import dji.diagnostics.model.DJIDiagnosticsType;
import dji.utils.function.Consumer;

public abstract /* synthetic */ class DiagnosticsModel$$CC {
    public static void doIfError(DiagnosticsModel diagnosticsModel, DJIDiagnosticsType type, Consumer consumer) {
        diagnosticsModel.doIfError(type, consumer, 0);
    }
}
