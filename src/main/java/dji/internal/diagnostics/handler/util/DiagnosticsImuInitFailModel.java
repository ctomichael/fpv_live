package dji.internal.diagnostics.handler.util;

import dji.midware.data.model.P3.DataOsdGetPushCommon;

public class DiagnosticsImuInitFailModel extends DiagnosticsIfModel<DataOsdGetPushCommon> {
    public DiagnosticsImuInitFailModel(int diagnosticsErrorCode, DataOsdGetPushCommon.IMU_INITFAIL_REASON failReason) {
        super(diagnosticsErrorCode, new DiagnosticsImuInitFailModel$$Lambda$0(failReason));
    }
}
