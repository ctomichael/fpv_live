package dji.diagnostics;

import dji.diagnostics.model.DJIDiagnostics;
import java.util.List;

public abstract class DJIDiagnosticsManager {
    public abstract void addDiagnosticsInformationListener(DiagnosticsInformationListener diagnosticsInformationListener);

    public abstract List<DJIDiagnostics> getCurrentDiagnostics();

    public abstract boolean hasDJIDiagnosticCode(int i);

    public abstract void removeDiagnosticsInformationListener(DiagnosticsInformationListener diagnosticsInformationListener);
}
