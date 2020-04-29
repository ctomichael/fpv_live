package dji.internal.diagnostics.handler.util;

import dji.midware.data.model.P3.DataOsdGetPushCommon;
import dji.utils.function.Predicate;
import dji.utils.function.Predicate$$CC;

final /* synthetic */ class DiagnosticsImuInitFailModel$$Lambda$0 implements Predicate {
    private final DataOsdGetPushCommon.IMU_INITFAIL_REASON arg$1;

    DiagnosticsImuInitFailModel$$Lambda$0(DataOsdGetPushCommon.IMU_INITFAIL_REASON imu_initfail_reason) {
        this.arg$1 = imu_initfail_reason;
    }

    public Predicate and(Predicate predicate) {
        return Predicate$$CC.and(this, predicate);
    }

    public Predicate negate() {
        return Predicate$$CC.negate(this);
    }

    public Predicate or(Predicate predicate) {
        return Predicate$$CC.or(this, predicate);
    }

    public boolean test(Object obj) {
        return this.arg$1.equals(((DataOsdGetPushCommon) obj).getIMUinitFailReason());
    }
}
