package dji.internal.diagnostics.handler;

import dji.midware.data.model.P3.DataCenterGetPushCheckStatus;

final /* synthetic */ class CenterBoardDiagnosticsHandler$$Lambda$0 implements Runnable {
    private final CenterBoardDiagnosticsHandler arg$1;
    private final DataCenterGetPushCheckStatus arg$2;

    CenterBoardDiagnosticsHandler$$Lambda$0(CenterBoardDiagnosticsHandler centerBoardDiagnosticsHandler, DataCenterGetPushCheckStatus dataCenterGetPushCheckStatus) {
        this.arg$1 = centerBoardDiagnosticsHandler;
        this.arg$2 = dataCenterGetPushCheckStatus;
    }

    public void run() {
        this.arg$1.lambda$onEvent3BackgroundThread$0$CenterBoardDiagnosticsHandler(this.arg$2);
    }
}
