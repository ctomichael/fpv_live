package com.dji.rx.sharedlib.diagnostics;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import dji.component.accountcenter.IMemberProtocol;
import dji.diagnostics.model.DJIDiagnostics;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class DiagnosticsEvent {
    private int code;
    @NonNull
    private List<DJIDiagnostics> mDiagnosticList;

    public DiagnosticsEvent(@NonNull DJIDiagnostics diagnostic) {
        this.mDiagnosticList = Collections.singletonList(diagnostic);
        this.code = diagnostic.getCode();
    }

    public DiagnosticsEvent(int code2, @NonNull List<DJIDiagnostics> diagnosticList) {
        this.code = code2;
        this.mDiagnosticList = Collections.unmodifiableList(diagnosticList);
    }

    public DiagnosticsEvent(int code2) {
        this.code = code2;
        this.mDiagnosticList = Collections.emptyList();
    }

    public int getCode() {
        return this.code;
    }

    public boolean isExist() {
        return !this.mDiagnosticList.isEmpty();
    }

    public List<DJIDiagnostics> getDiagnosticsList() {
        return this.mDiagnosticList;
    }

    @Nullable
    public DJIDiagnostics getDiagnostic() {
        if (this.mDiagnosticList.isEmpty()) {
            return null;
        }
        return this.mDiagnosticList.get(0);
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DiagnosticsEvent)) {
            return false;
        }
        DiagnosticsEvent that = (DiagnosticsEvent) o;
        if (this.code != that.code || !this.mDiagnosticList.equals(that.mDiagnosticList)) {
            return false;
        }
        return true;
    }

    public int hashCode() {
        return Objects.hash(Integer.valueOf(this.code), this.mDiagnosticList);
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder("DiagnosticsEvent{code=");
        stringBuilder.append(this.code);
        if (!isExist()) {
            stringBuilder.append(",[not exist]");
        } else {
            for (DJIDiagnostics diagnostic : this.mDiagnosticList) {
                stringBuilder.append(",[").append(diagnostic.getCode()).append(diagnostic.getComponentIndex() == 0 ? "" : ", " + diagnostic.getComponentIndex()).append(diagnostic.getExtra() == null ? "" : ", " + diagnostic.getExtra()).append(IMemberProtocol.STRING_SEPERATOR_RIGHT);
            }
        }
        stringBuilder.append("}");
        return stringBuilder.toString();
    }
}
