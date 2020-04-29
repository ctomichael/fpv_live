package dji.diagnostics;

import dji.diagnostics.model.DJIDiagnostics;
import java.util.List;

public interface DiagnosticsInformationListener {
    void onUpdate(List<DJIDiagnostics> list);
}
