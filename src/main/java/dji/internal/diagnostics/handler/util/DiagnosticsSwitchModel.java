package dji.internal.diagnostics.handler.util;

import dji.utils.function.Predicate;

public class DiagnosticsSwitchModel<T> extends DiagnosticsModelBase<T> {
    private final Predicate<T> mCondition;
    private final int mDiagnosticsCode;
    private boolean mOldValue = false;

    public DiagnosticsSwitchModel(int diagnosticsErrorCode, Predicate<T> statePredicate, UpdateInterface autoDisappear) {
        this.mCondition = statePredicate;
        this.mDiagnosticsCode = diagnosticsErrorCode;
        this.mAutoDisappear = true;
        this.mKeepForWhile = true;
        this.mUpdater = autoDisappear;
        this.mKeepTime = 3000;
    }

    /* access modifiers changed from: protected */
    public Integer applyToCode(T value) {
        boolean newValue = this.mCondition.test(value);
        if (newValue == this.mOldValue) {
            return null;
        }
        this.mOldValue = newValue;
        return Integer.valueOf(this.mDiagnosticsCode);
    }
}
