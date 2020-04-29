package dji.internal.diagnostics.handler.util;

import dji.diagnostics.model.DJIDiagnosticsType;
import dji.internal.diagnostics.DJIDiagnosticsImpl;
import dji.utils.function.Consumer;

public interface DiagnosticsModel<T> {
    void doIfError(DJIDiagnosticsType dJIDiagnosticsType, Consumer<DJIDiagnosticsImpl> consumer);

    void doIfError(DJIDiagnosticsType dJIDiagnosticsType, Consumer<DJIDiagnosticsImpl> consumer, int i);

    void reset();

    boolean statusApply(Object obj);
}
