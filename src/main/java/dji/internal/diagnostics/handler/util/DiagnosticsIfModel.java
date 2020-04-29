package dji.internal.diagnostics.handler.util;

import dji.utils.function.BiPredicate;
import dji.utils.function.Function;
import dji.utils.function.Predicate;

public class DiagnosticsIfModel<T> extends DiagnosticsModelBase<T> {
    protected BiPredicate<T, T> mBiCondition;
    private final int mCodeIfTrue;
    private T mOldValue;

    public DiagnosticsIfModel(int codeIfTrue, Predicate predicate) {
        this(codeIfTrue, new DiagnosticsIfModel$$Lambda$0(predicate));
    }

    public DiagnosticsIfModel(int codeIfTrue, BiPredicate biPredicate) {
        this(codeIfTrue, biPredicate, (Function) null);
    }

    public DiagnosticsIfModel(int codeIfTrue, Predicate predicate, Function function) {
        this(codeIfTrue, new DiagnosticsIfModel$$Lambda$1(predicate), function);
    }

    public DiagnosticsIfModel(int codeIfTrue, BiPredicate biPredicate, Function function) {
        this.mBiCondition = biPredicate;
        this.mCodeIfTrue = codeIfTrue;
        this.extraDataFunction = function;
    }

    /* access modifiers changed from: protected */
    public Integer applyToCode(T value) {
        Integer integer = this.mBiCondition.test(value, this.mOldValue) ? Integer.valueOf(this.mCodeIfTrue) : null;
        this.mOldValue = value;
        return integer;
    }
}
