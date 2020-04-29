package dji.internal.diagnostics.handler.util;

import android.util.SparseArray;
import dji.utils.function.Function;
import dji.utils.function.Predicate;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

public class DiagnosticsOnlyOneModel<T> extends DiagnosticsModelBase<T> {
    private final LinkedHashMap<Predicate<T>, Integer> mConditionToCodeMap = new LinkedHashMap<>();
    private final SparseArray<Function<T, ?>> mExtraDataFunctions = new SparseArray<>();

    public void put(int code, Predicate<T> condition) {
        this.mConditionToCodeMap.put(condition, Integer.valueOf(code));
    }

    public void put(int code, Predicate<T> condition, Function<T, ?> extraDataFunction) {
        this.mConditionToCodeMap.put(condition, Integer.valueOf(code));
        this.mExtraDataFunctions.put(code, extraDataFunction);
    }

    /* access modifiers changed from: protected */
    public Integer applyToCode(T value) {
        for (Map.Entry<Predicate<T>, Integer> integerPredicateEntry : this.mConditionToCodeMap.entrySet()) {
            if (integerPredicateEntry.getKey().test(value)) {
                return integerPredicateEntry.getValue();
            }
        }
        return null;
    }

    /* access modifiers changed from: protected */
    public boolean onCodeNotChange(T value) {
        Integer diagnosticsCode = getDiagnosticsCode();
        if (diagnosticsCode == null) {
            return super.onCodeNotChange(value);
        }
        Function<T, ?> tFunction = this.mExtraDataFunctions.get(diagnosticsCode.intValue());
        if (tFunction == null) {
            return super.onCodeNotChange(value);
        }
        Object lastExtra = this.mExtraData;
        this.mExtraData = tFunction.apply(value);
        return !Objects.equals(this.mExtraData, lastExtra);
    }

    /* access modifiers changed from: protected */
    public boolean onCodeChange(T value) {
        Object obj = null;
        Integer diagnosticsCode = getDiagnosticsCode();
        if (diagnosticsCode == null) {
            this.mExtraData = null;
        } else {
            Function<T, ?> tFunction = this.mExtraDataFunctions.get(diagnosticsCode.intValue());
            if (tFunction != null) {
                obj = tFunction.apply(value);
            }
            this.mExtraData = obj;
        }
        return super.onCodeChange(value);
    }
}
