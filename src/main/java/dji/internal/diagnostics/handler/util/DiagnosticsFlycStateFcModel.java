package dji.internal.diagnostics.handler.util;

import dji.midware.data.model.P3.DataOsdGetPushCommon;
import dji.utils.function.BiFunction;

public class DiagnosticsFlycStateFcModel extends DiagnosticsModelBase<DataOsdGetPushCommon> {
    private DataOsdGetPushCommon.FLYC_STATE mOldFlycState;
    private BiFunction<DataOsdGetPushCommon.FLYC_STATE, DataOsdGetPushCommon, Integer> mOldFlycStatePredicateToCode;

    public DiagnosticsFlycStateFcModel(BiFunction<DataOsdGetPushCommon.FLYC_STATE, DataOsdGetPushCommon, Integer> oldFlycStatePredicateToCode, UpdateInterface autoDisappear) {
        this.mOldFlycStatePredicateToCode = oldFlycStatePredicateToCode;
        this.mKeepForWhile = true;
        this.mUpdater = autoDisappear;
        this.mKeepTime = 3000;
    }

    /* access modifiers changed from: protected */
    public Integer applyToCode(DataOsdGetPushCommon value) {
        Integer currentCode = this.mOldFlycStatePredicateToCode.apply(this.mOldFlycState, value);
        this.mOldFlycState = value.getFlycState();
        return currentCode;
    }
}
