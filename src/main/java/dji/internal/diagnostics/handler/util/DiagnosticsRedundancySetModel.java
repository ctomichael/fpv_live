package dji.internal.diagnostics.handler.util;

import dji.diagnostics.model.DJIDiagnosticsType;
import dji.internal.diagnostics.DJIDiagnosticsImpl;
import dji.internal.diagnostics.handler.redundancy.RedundancyErrorInfo;
import dji.utils.function.Consumer;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class DiagnosticsRedundancySetModel implements DiagnosticsModel<Set<RedundancyErrorInfo>> {
    private Set<RedundancyErrorInfo> mRedundancyErrorInfoSet = new HashSet();

    public void doIfError(DJIDiagnosticsType dJIDiagnosticsType, Consumer consumer) {
        DiagnosticsModel$$CC.doIfError(this, dJIDiagnosticsType, consumer);
    }

    public /* bridge */ /* synthetic */ boolean statusApply(Object obj) {
        return statusApply((Set<RedundancyErrorInfo>) ((Set) obj));
    }

    public boolean statusApply(Set<RedundancyErrorInfo> value) {
        if (Objects.equals(this.mRedundancyErrorInfoSet, value)) {
            return false;
        }
        if (value == null) {
            value = new HashSet<>();
        }
        this.mRedundancyErrorInfoSet = value;
        return true;
    }

    public void doIfError(DJIDiagnosticsType type, Consumer<DJIDiagnosticsImpl> diagnosticsConsumer, int componentIndex) {
        if (this.mRedundancyErrorInfoSet != null && !this.mRedundancyErrorInfoSet.isEmpty()) {
            for (RedundancyErrorInfo errorInfo : this.mRedundancyErrorInfoSet) {
                diagnosticsConsumer.accept(new DJIDiagnosticsImpl(type, errorInfo.getDiagnosticsCode(), errorInfo.getReason(), errorInfo.getSolution(), errorInfo.getImuIndex(), errorInfo));
            }
        }
    }

    public void reset() {
        this.mRedundancyErrorInfoSet = new HashSet();
    }
}
