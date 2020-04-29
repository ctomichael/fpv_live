package dji.internal.diagnostics.handler.util;

import java.util.HashMap;
import java.util.Map;

public class DiagnosticsMapModel<T> extends DiagnosticsModelBase<T> {
    protected final Map<T, Integer> reasonCodeMap = new HashMap();

    public void put(Integer diagnosticsCode, T t) {
        this.reasonCodeMap.put(t, diagnosticsCode);
    }

    /* access modifiers changed from: protected */
    public Integer applyToCode(T value) {
        return this.reasonCodeMap.get(value);
    }
}
