package dji.internal.diagnostics.handler.util;

import dji.midware.data.model.P3.DataOsdGetPushWirelessState;
import dji.utils.function.Predicate;
import java.util.Locale;

public class DiagnosticsWirelessModel extends DiagnosticsIfModel<DataOsdGetPushWirelessState.SdrWirelessState> {
    public DiagnosticsWirelessModel(int codeIfTrue, Predicate<DataOsdGetPushWirelessState.SdrWirelessState> mCondition, UpdateInterface updateInterface) {
        super(codeIfTrue, mCondition);
        this.mAutoDisappear = true;
        this.mKeepForWhile = true;
        this.mUpdater = updateInterface;
        this.mAutoDisappearTime = 3000;
        this.mKeepTime = 3000;
    }

    /* access modifiers changed from: protected */
    public void onAutoDisappear() {
        super.onAutoDisappear();
        DiagnosticsLog.logi("diagnostics", String.format(Locale.US, "code=[%s]自动消失", getDiagnosticsCode()));
        reset();
    }
}
