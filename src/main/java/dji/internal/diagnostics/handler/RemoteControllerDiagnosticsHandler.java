package dji.internal.diagnostics.handler;

import dji.component.appstate.IAppStateService;
import dji.diagnostics.model.DJIDiagnostics;
import dji.diagnostics.model.DJIDiagnosticsError;
import dji.diagnostics.model.DJIDiagnosticsType;
import dji.internal.diagnostics.DJIDiagnosticsImpl;
import dji.internal.diagnostics.DiagnosticsBaseHandler;
import dji.internal.diagnostics.DiagnosticsHandlerUpdateObserver;
import dji.internal.diagnostics.handler.util.DiagnosticsIfModel;
import dji.internal.diagnostics.handler.util.DiagnosticsModel;
import dji.midware.data.model.P3.DataCameraGetPushShotParams;
import dji.midware.data.model.P3.DataCameraSetExposureMode;
import dji.midware.data.model.P3.DataOsdGetPushCheckStatus;
import dji.midware.data.model.P3.DataOsdGetPushCheckStatusV2;
import dji.midware.data.model.P3.DataRcGetPushBatteryInfo;
import dji.midware.data.model.P3.DataRcGetPushFollowFocus2;
import dji.midware.data.model.P3.DataRcGetPushGpsInfo;
import dji.midware.data.model.P3.DataWifi_gGetPushCheckStatus;
import dji.service.DJIAppServiceManager;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class RemoteControllerDiagnosticsHandler extends DiagnosticsBaseHandler {
    private static final DJIDiagnosticsType TYPE = DJIDiagnosticsType.REMOTE_CONTROLLER;
    private boolean isCannotUseFocusInExposureMode;
    private boolean lastCannotUseFocusInExposureMode;
    private List<DiagnosticsModel<DataRcGetPushBatteryInfo>> mBatteryInfoModels;
    private List<DiagnosticsModel<DataRcGetPushGpsInfo>> mGpsInfoModels;
    private List<DiagnosticsModel<DataOsdGetPushCheckStatus>> mOsdCheckStatusModels;
    private List<DiagnosticsModel<DataOsdGetPushCheckStatusV2>> mOsdCheckStatusV2Models;
    private List<DiagnosticsModel<DataWifi_gGetPushCheckStatus>> mWifiGInfoModels;

    public RemoteControllerDiagnosticsHandler(DiagnosticsHandlerUpdateObserver observer) {
        super(observer);
        init();
    }

    private void init() {
        this.mOsdCheckStatusModels = new ArrayList();
        this.mOsdCheckStatusV2Models = new ArrayList();
        this.mBatteryInfoModels = new ArrayList();
        this.mGpsInfoModels = new ArrayList();
        this.mWifiGInfoModels = new ArrayList();
        this.mOsdCheckStatusModels.addAll(Arrays.asList(new DiagnosticsIfModel((int) DJIDiagnosticsError.RemoteController.FPGA_ERROR, RemoteControllerDiagnosticsHandler$$Lambda$0.$instance), new DiagnosticsIfModel((int) DJIDiagnosticsError.RemoteController.TRANSMITTER_ERROR, RemoteControllerDiagnosticsHandler$$Lambda$1.$instance), new DiagnosticsIfModel((int) DJIDiagnosticsError.RemoteController.BATTERY_ERROR, RemoteControllerDiagnosticsHandler$$Lambda$2.$instance), new DiagnosticsIfModel((int) DJIDiagnosticsError.RemoteController.GPS_ERROR, RemoteControllerDiagnosticsHandler$$Lambda$3.$instance), new DiagnosticsIfModel((int) DJIDiagnosticsError.RemoteController.ENCRYPTION_ERROR, RemoteControllerDiagnosticsHandler$$Lambda$4.$instance), new DiagnosticsIfModel((int) DJIDiagnosticsError.RemoteController.NEED_CALIBRATION, RemoteControllerDiagnosticsHandler$$Lambda$5.$instance), new DiagnosticsIfModel((int) DJIDiagnosticsError.RemoteController.LOW_RC_POWER, RemoteControllerDiagnosticsHandler$$Lambda$6.$instance), new DiagnosticsIfModel((int) DJIDiagnosticsError.RemoteController.IDLE_TOO_LONG, RemoteControllerDiagnosticsHandler$$Lambda$7.$instance), new DiagnosticsIfModel((int) DJIDiagnosticsError.RemoteController.RESET, RemoteControllerDiagnosticsHandler$$Lambda$8.$instance), new DiagnosticsIfModel((int) DJIDiagnosticsError.RemoteController.OVER_HEAT, RemoteControllerDiagnosticsHandler$$Lambda$9.$instance)));
        this.mOsdCheckStatusV2Models.addAll(Arrays.asList(new DiagnosticsIfModel((int) DJIDiagnosticsError.RemoteController.LOW_RC_POWER, RemoteControllerDiagnosticsHandler$$Lambda$10.$instance), new DiagnosticsIfModel((int) DJIDiagnosticsError.RemoteController.NEED_CALIBRATION, RemoteControllerDiagnosticsHandler$$Lambda$11.$instance), new DiagnosticsIfModel((int) DJIDiagnosticsError.RemoteController.OVER_HEAT, RemoteControllerDiagnosticsHandler$$Lambda$12.$instance)));
        this.mBatteryInfoModels.addAll(Arrays.asList(new DiagnosticsIfModel((int) DJIDiagnosticsError.RemoteController.LOW_RC_POWER, RemoteControllerDiagnosticsHandler$$Lambda$13.$instance)));
        this.mGpsInfoModels.addAll(Arrays.asList(new DiagnosticsIfModel((int) DJIDiagnosticsError.RemoteController.GPS_ERROR, RemoteControllerDiagnosticsHandler$$Lambda$14.$instance)));
        this.mWifiGInfoModels.addAll(Arrays.asList(new DiagnosticsIfModel((int) DJIDiagnosticsError.RemoteController.APSRV_WAS_DOWN, new RemoteControllerDiagnosticsHandler$$Lambda$15(this)), new DiagnosticsIfModel((int) DJIDiagnosticsError.RemoteController.CHIP_9342_WAS_DOWN, new RemoteControllerDiagnosticsHandler$$Lambda$16(this)), new DiagnosticsIfModel((int) DJIDiagnosticsError.RemoteController.GO_HOME_FAIL, RemoteControllerDiagnosticsHandler$$Lambda$17.$instance)));
    }

    static final /* synthetic */ boolean lambda$init$0$RemoteControllerDiagnosticsHandler(DataOsdGetPushCheckStatus status) {
        return status.getEncryptStatus() || status.getEncryptStatusWifiRc();
    }

    static final /* synthetic */ boolean lambda$init$1$RemoteControllerDiagnosticsHandler(DataOsdGetPushCheckStatus status) {
        return status.getStickMiddleStatus() || status.getStickMiddleStatusWifiRc();
    }

    static final /* synthetic */ boolean lambda$init$2$RemoteControllerDiagnosticsHandler(DataRcGetPushBatteryInfo info) {
        return info.getBattery() <= 10;
    }

    /* access modifiers changed from: package-private */
    public final /* synthetic */ boolean lambda$init$3$RemoteControllerDiagnosticsHandler(DataWifi_gGetPushCheckStatus dataWifi_gGetPushCheckStatus) {
        return isInnerVersion() && dataWifi_gGetPushCheckStatus.wasApsrvDown();
    }

    /* access modifiers changed from: package-private */
    public final /* synthetic */ boolean lambda$init$4$RemoteControllerDiagnosticsHandler(DataWifi_gGetPushCheckStatus dataWifi_gGetPushCheckStatus1) {
        return isInnerVersion() && dataWifi_gGetPushCheckStatus1.was9342Down();
    }

    private boolean isInnerVersion() {
        IAppStateService appState = (IAppStateService) DJIAppServiceManager.getInstance().getService(IAppStateService.NAME);
        return appState != null && appState.isInnerApp();
    }

    /* access modifiers changed from: protected */
    public void onRegister() {
        if (DataOsdGetPushCheckStatus.getInstance().isGetted()) {
            onEvent3BackgroundThread(DataOsdGetPushCheckStatus.getInstance());
        }
        if (DataOsdGetPushCheckStatusV2.getInstance().isGetted()) {
            onEvent3BackgroundThread(DataOsdGetPushCheckStatusV2.getInstance());
        }
        if (DataRcGetPushBatteryInfo.getInstance().isGetted()) {
            onEvent3BackgroundThread(DataRcGetPushBatteryInfo.getInstance());
        }
        if (DataRcGetPushFollowFocus2.getInstance().isGetted()) {
            onEvent3BackgroundThread(DataRcGetPushFollowFocus2.getInstance());
        }
        if (DataCameraGetPushShotParams.getInstance().isGetted()) {
            onEvent3BackgroundThread(DataCameraGetPushShotParams.getInstance());
        }
        if (DataRcGetPushGpsInfo.getInstance().isGetted()) {
            onEvent3BackgroundThread(DataRcGetPushGpsInfo.getInstance());
        }
        if (DataWifi_gGetPushCheckStatus.getInstance().isGetted()) {
            onEvent3BackgroundThread(DataWifi_gGetPushCheckStatus.getInstance());
        }
    }

    /* access modifiers changed from: protected */
    public void onUnregister() {
    }

    public Set<DJIDiagnostics> getDiagnosisList() {
        Set<DJIDiagnostics> diagnosisList = new HashSet<>();
        if (this.isCannotUseFocusInExposureMode) {
            diagnosisList.add(new DJIDiagnosticsImpl(TYPE, DJIDiagnosticsError.RemoteController.CANNOT_USE_FOCUS_IN_EXPOSURE_MODE));
        }
        for (DiagnosticsModel<DataOsdGetPushCheckStatus> model : this.mOsdCheckStatusModels) {
            DJIDiagnosticsType dJIDiagnosticsType = TYPE;
            diagnosisList.getClass();
            model.doIfError(dJIDiagnosticsType, RemoteControllerDiagnosticsHandler$$Lambda$18.get$Lambda(diagnosisList));
        }
        for (DiagnosticsModel<DataOsdGetPushCheckStatusV2> model2 : this.mOsdCheckStatusV2Models) {
            DJIDiagnosticsType dJIDiagnosticsType2 = TYPE;
            diagnosisList.getClass();
            model2.doIfError(dJIDiagnosticsType2, RemoteControllerDiagnosticsHandler$$Lambda$19.get$Lambda(diagnosisList));
        }
        for (DiagnosticsModel<DataRcGetPushBatteryInfo> model3 : this.mBatteryInfoModels) {
            DJIDiagnosticsType dJIDiagnosticsType3 = TYPE;
            diagnosisList.getClass();
            model3.doIfError(dJIDiagnosticsType3, RemoteControllerDiagnosticsHandler$$Lambda$20.get$Lambda(diagnosisList));
        }
        for (DiagnosticsModel<DataRcGetPushGpsInfo> model4 : this.mGpsInfoModels) {
            DJIDiagnosticsType dJIDiagnosticsType4 = TYPE;
            diagnosisList.getClass();
            model4.doIfError(dJIDiagnosticsType4, RemoteControllerDiagnosticsHandler$$Lambda$21.get$Lambda(diagnosisList));
        }
        for (DiagnosticsModel<DataWifi_gGetPushCheckStatus> model5 : this.mWifiGInfoModels) {
            DJIDiagnosticsType dJIDiagnosticsType5 = TYPE;
            diagnosisList.getClass();
            model5.doIfError(dJIDiagnosticsType5, RemoteControllerDiagnosticsHandler$$Lambda$22.get$Lambda(diagnosisList));
        }
        return diagnosisList;
    }

    public void reset(int componentIndex) {
        this.isCannotUseFocusInExposureMode = false;
        this.lastCannotUseFocusInExposureMode = false;
        for (DiagnosticsModel<DataOsdGetPushCheckStatus> model : this.mOsdCheckStatusModels) {
            model.reset();
        }
        for (DiagnosticsModel<DataOsdGetPushCheckStatusV2> model2 : this.mOsdCheckStatusV2Models) {
            model2.reset();
        }
        for (DiagnosticsModel<DataRcGetPushBatteryInfo> model3 : this.mBatteryInfoModels) {
            model3.reset();
        }
        for (DiagnosticsModel<DataRcGetPushGpsInfo> model4 : this.mGpsInfoModels) {
            model4.reset();
        }
        for (DiagnosticsModel<DataWifi_gGetPushCheckStatus> model5 : this.mWifiGInfoModels) {
            model5.reset();
        }
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEvent3BackgroundThread(DataOsdGetPushCheckStatusV2 status) {
        postToDiagnosticBackgroudThread(new RemoteControllerDiagnosticsHandler$$Lambda$23(this, status));
    }

    /* access modifiers changed from: package-private */
    public final /* synthetic */ void lambda$onEvent3BackgroundThread$5$RemoteControllerDiagnosticsHandler(DataOsdGetPushCheckStatusV2 status) {
        boolean changed = false;
        for (DiagnosticsModel<DataOsdGetPushCheckStatusV2> model : this.mOsdCheckStatusV2Models) {
            changed |= model.statusApply(status);
        }
        if (changed) {
            notifyChange();
        }
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEvent3BackgroundThread(DataOsdGetPushCheckStatus status) {
        postToDiagnosticBackgroudThread(new RemoteControllerDiagnosticsHandler$$Lambda$24(this, status));
    }

    /* access modifiers changed from: package-private */
    public final /* synthetic */ void lambda$onEvent3BackgroundThread$6$RemoteControllerDiagnosticsHandler(DataOsdGetPushCheckStatus status) {
        boolean changed = false;
        for (DiagnosticsModel<DataOsdGetPushCheckStatus> model : this.mOsdCheckStatusModels) {
            changed |= model.statusApply(status);
        }
        if (changed) {
            notifyChange();
        }
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEvent3BackgroundThread(DataRcGetPushBatteryInfo status) {
        postToDiagnosticBackgroudThread(new RemoteControllerDiagnosticsHandler$$Lambda$25(this, status));
    }

    /* access modifiers changed from: package-private */
    public final /* synthetic */ void lambda$onEvent3BackgroundThread$7$RemoteControllerDiagnosticsHandler(DataRcGetPushBatteryInfo status) {
        boolean changed = false;
        for (DiagnosticsModel<DataRcGetPushBatteryInfo> model : this.mBatteryInfoModels) {
            changed |= model.statusApply(status);
        }
        if (changed) {
            notifyChange();
        }
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEvent3BackgroundThread(DataRcGetPushFollowFocus2 status) {
        if (status.isGetted()) {
            notifyCannotUseFocusInExposureMode();
        }
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEvent3BackgroundThread(DataCameraGetPushShotParams status) {
        if (status.isGetted()) {
            notifyCannotUseFocusInExposureMode();
        }
    }

    private void notifyCannotUseFocusInExposureMode() {
        postToDiagnosticBackgroudThread(new RemoteControllerDiagnosticsHandler$$Lambda$26(this));
    }

    /* access modifiers changed from: package-private */
    public final /* synthetic */ void lambda$notifyCannotUseFocusInExposureMode$8$RemoteControllerDiagnosticsHandler() {
        boolean isCannotUseFocusInExposureMode2 = getIsCannotUseFocusInExposureMode();
        if (this.lastCannotUseFocusInExposureMode != isCannotUseFocusInExposureMode2) {
            this.isCannotUseFocusInExposureMode = isCannotUseFocusInExposureMode2;
            notifyChange();
        }
        this.lastCannotUseFocusInExposureMode = isCannotUseFocusInExposureMode2;
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEvent3BackgroundThread(DataRcGetPushGpsInfo status) {
        postToDiagnosticBackgroudThread(new RemoteControllerDiagnosticsHandler$$Lambda$27(this, status));
    }

    /* access modifiers changed from: package-private */
    public final /* synthetic */ void lambda$onEvent3BackgroundThread$9$RemoteControllerDiagnosticsHandler(DataRcGetPushGpsInfo status) {
        boolean changed = false;
        for (DiagnosticsModel<DataRcGetPushGpsInfo> model : this.mGpsInfoModels) {
            changed |= model.statusApply(status);
        }
        if (changed) {
            notifyChange();
        }
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEvent3BackgroundThread(DataWifi_gGetPushCheckStatus status) {
        postToDiagnosticBackgroudThread(new RemoteControllerDiagnosticsHandler$$Lambda$28(this, status));
    }

    /* access modifiers changed from: package-private */
    public final /* synthetic */ void lambda$onEvent3BackgroundThread$10$RemoteControllerDiagnosticsHandler(DataWifi_gGetPushCheckStatus status) {
        boolean changed = false;
        for (DiagnosticsModel<DataWifi_gGetPushCheckStatus> model : this.mWifiGInfoModels) {
            changed |= model.statusApply(status);
        }
        if (changed) {
            notifyChange();
        }
    }

    private boolean getIsCannotUseFocusInExposureMode() {
        boolean isError1 = false;
        boolean isError2 = false;
        if (DataRcGetPushFollowFocus2.getInstance().isGetted()) {
            isError1 = DataRcGetPushFollowFocus2.getInstance().getCtrlType() == DataRcGetPushFollowFocus2.CtrlType.FOCUS_POSITION;
        }
        if (DataCameraGetPushShotParams.getInstance().isGetted()) {
            if (DataCameraGetPushShotParams.getInstance().getExposureMode() == DataCameraSetExposureMode.ExposureMode.S || DataCameraGetPushShotParams.getInstance().getExposureMode() == DataCameraSetExposureMode.ExposureMode.A) {
                isError2 = true;
            } else {
                isError2 = false;
            }
        }
        if (isError1 || isError2) {
            return true;
        }
        return false;
    }
}
