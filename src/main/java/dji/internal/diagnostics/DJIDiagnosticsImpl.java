package dji.internal.diagnostics;

import dji.diagnostics.model.DJIDiagnostics;
import dji.diagnostics.model.DJIDiagnosticsType;
import java.util.Objects;

public class DJIDiagnosticsImpl implements DJIDiagnostics {
    private final int code;
    private final int componentIndex;
    private final Object extra;
    private final String reason;
    private final String solution;
    private int subCode;
    private int subComponentIndex;
    private DJIDiagnosticsType type;

    public DJIDiagnosticsImpl(DJIDiagnosticsType type2, int code2, Object extra2) {
        this(type2, code2, 0, "", "", 0, extra2);
    }

    public DJIDiagnosticsImpl(DJIDiagnosticsType type2, int code2, Object extra2, int componentIndex2) {
        this(type2, code2, 0, "", "", componentIndex2, extra2);
    }

    public DJIDiagnosticsImpl(DJIDiagnosticsType type2, int code2, String reason2, String solution2, int subComponentIndex2, Object extra2) {
        this(type2, code2, 0, reason2, solution2, 0, subComponentIndex2, extra2);
    }

    public DJIDiagnosticsImpl(DJIDiagnosticsType type2, int code2) {
        this(type2, code2, 0, "", "", 0, null);
    }

    public DJIDiagnosticsImpl(DJIDiagnosticsType type2, int code2, int componentIndex2) {
        this(type2, code2, 0, "", "", componentIndex2, null);
    }

    private DJIDiagnosticsImpl(DJIDiagnosticsType type2, int code2, int subCode2, String reason2, String solution2, int componentIndex2, Object extra2) {
        this(type2, code2, subCode2, reason2, solution2, componentIndex2, 0, extra2);
    }

    private DJIDiagnosticsImpl(DJIDiagnosticsType type2, int code2, int subCode2, String reason2, String solution2, int componentIndex2, int subComponentIndex2, Object extra2) {
        this.type = DJIDiagnosticsType.OTHER;
        this.type = type2;
        this.code = code2;
        this.subCode = subCode2;
        this.reason = reason2;
        this.solution = solution2;
        this.componentIndex = componentIndex2;
        this.extra = extra2;
        this.subComponentIndex = subComponentIndex2;
    }

    public DJIDiagnosticsType getType() {
        return this.type;
    }

    public int getCode() {
        return this.code;
    }

    public int getSubCode() {
        return this.subCode;
    }

    public String getReason() {
        return this.reason;
    }

    public String getSolution() {
        return this.solution;
    }

    public int getComponentIndex() {
        return this.componentIndex;
    }

    public Object getExtra() {
        return this.extra;
    }

    public int getSubComponentIndex() {
        return this.subComponentIndex;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DJIDiagnosticsImpl)) {
            return false;
        }
        DJIDiagnosticsImpl that = (DJIDiagnosticsImpl) o;
        if (getCode() == that.getCode() && getSubCode() == that.getSubCode() && getComponentIndex() == that.getComponentIndex() && getSubComponentIndex() == that.getSubComponentIndex() && getType() == that.getType() && Objects.equals(getReason(), that.getReason()) && Objects.equals(getSolution(), that.getSolution()) && Objects.equals(getExtra(), that.getExtra())) {
            return true;
        }
        return false;
    }

    public int hashCode() {
        return Objects.hash(getType(), Integer.valueOf(getCode()), Integer.valueOf(getSubCode()), Integer.valueOf(getComponentIndex()), getReason(), getSolution(), getExtra(), Integer.valueOf(getSubComponentIndex()));
    }

    public String toString() {
        return "DJIDiagnosticsImpl{type=" + this.type + ", code=" + this.code + ", subCode=" + this.subCode + ", componentIndex=" + this.componentIndex + ", subComponentIndex=" + this.subComponentIndex + ", reason='" + this.reason + '\'' + ", solution='" + this.solution + '\'' + ", extra=" + this.extra + '}';
    }
}
