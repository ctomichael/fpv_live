package dji.internal.diagnostics.handler;

import dji.diagnostics.model.DJIDiagnostics;
import dji.diagnostics.model.DJIDiagnosticsError;
import dji.diagnostics.model.DJIDiagnosticsType;
import dji.internal.diagnostics.DJIDiagnosticsImpl;
import dji.internal.diagnostics.DiagnosticsBaseHandler;
import dji.internal.diagnostics.DiagnosticsHandlerUpdateObserver;
import dji.midware.data.model.P3.DataCenterGetPushCheckStatus;
import java.util.HashSet;
import java.util.Set;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class CenterBoardDiagnosticsHandler extends DiagnosticsBaseHandler {
    private boolean isBatteryConnectionError;
    private boolean isGpsConnectionError;
    private boolean isMCConnectionError;
    private boolean lastIsBatteryConnectionError;
    private boolean lastIsGpsConnectionError;
    private boolean lastIsMCConnectionError;

    public CenterBoardDiagnosticsHandler(DiagnosticsHandlerUpdateObserver observer) {
        super(observer);
    }

    /* access modifiers changed from: protected */
    public void onRegister() {
        if (DataCenterGetPushCheckStatus.getInstance().isGetted()) {
            onEvent3BackgroundThread(DataCenterGetPushCheckStatus.getInstance());
        }
    }

    /* access modifiers changed from: protected */
    public void onUnregister() {
    }

    public Set<DJIDiagnostics> getDiagnosisList() {
        Set<DJIDiagnostics> diagnosisList = new HashSet<>();
        if (this.isBatteryConnectionError) {
            diagnosisList.add(new DJIDiagnosticsImpl(DJIDiagnosticsType.CENTER_BOARD, DJIDiagnosticsError.CenterBoard.CONNECT_TO_BATTERY_ERROR));
        }
        if (this.isGpsConnectionError) {
            diagnosisList.add(new DJIDiagnosticsImpl(DJIDiagnosticsType.CENTER_BOARD, DJIDiagnosticsError.CenterBoard.CONNECT_TO_GPS_ERROR));
        }
        if (this.isMCConnectionError) {
            diagnosisList.add(new DJIDiagnosticsImpl(DJIDiagnosticsType.CENTER_BOARD, DJIDiagnosticsError.CenterBoard.CONNECT_TO_FC_ERROR));
        }
        return diagnosisList;
    }

    public void reset(int componentIndex) {
        this.isBatteryConnectionError = false;
        this.lastIsBatteryConnectionError = false;
        this.isGpsConnectionError = false;
        this.lastIsGpsConnectionError = false;
        this.isMCConnectionError = false;
        this.lastIsMCConnectionError = false;
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEvent3BackgroundThread(DataCenterGetPushCheckStatus status) {
        postToDiagnosticBackgroudThread(new CenterBoardDiagnosticsHandler$$Lambda$0(this, status));
    }

    /* access modifiers changed from: package-private */
    public final /* synthetic */ void lambda$onEvent3BackgroundThread$0$CenterBoardDiagnosticsHandler(DataCenterGetPushCheckStatus status) {
        boolean isBatteryConnectionError2 = status.getBatteryConnectStatus();
        boolean isGpsConnectionError2 = status.getGpsConnectStatus();
        boolean isMCConnectionError2 = status.getMcConnectStatus();
        if (!compareBooleans(new boolean[]{isBatteryConnectionError2, isGpsConnectionError2, isMCConnectionError2}, new boolean[]{this.lastIsBatteryConnectionError, this.lastIsGpsConnectionError, this.lastIsMCConnectionError})) {
            resetCenterBoardCheckStatus();
            this.isBatteryConnectionError = isBatteryConnectionError2;
            this.isGpsConnectionError = isGpsConnectionError2;
            this.isMCConnectionError = isMCConnectionError2;
            notifyChange();
        }
        this.lastIsBatteryConnectionError = isBatteryConnectionError2;
        this.lastIsGpsConnectionError = isGpsConnectionError2;
        this.lastIsMCConnectionError = isMCConnectionError2;
    }

    private void resetCenterBoardCheckStatus() {
        this.isBatteryConnectionError = false;
        this.isGpsConnectionError = false;
        this.isMCConnectionError = false;
    }
}
