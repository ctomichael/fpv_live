package dji.diagnostics.model;

public interface DJIDiagnostics {
    int getCode();

    int getComponentIndex();

    Object getExtra();

    String getReason();

    String getSolution();

    int getSubCode();

    DJIDiagnosticsType getType();
}
