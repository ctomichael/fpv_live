package dji.internal.diagnostics.handler;

import dji.diagnostics.model.DJIDiagnostics;
import dji.diagnostics.model.DJIDiagnosticsError;
import dji.diagnostics.model.DJIDiagnosticsType;
import dji.internal.diagnostics.DiagnosticsBaseHandler;
import dji.internal.diagnostics.DiagnosticsHandlerUpdateObserver;
import dji.internal.diagnostics.handler.util.DiagnosticsIfModel;
import dji.internal.diagnostics.handler.util.DiagnosticsModel;
import dji.midware.data.config.P3.ProductType;
import dji.midware.data.manager.P3.DJIProductManager;
import dji.midware.data.model.P3.DataEyeGetPushPointState;
import dji.midware.data.model.P3.DataGimbalGetPushCheckStatus;
import dji.midware.data.model.P3.DataGimbalGetPushParams;
import dji.midware.data.model.P3.DataOsdGetPushCommon;
import dji.midware.util.DoubleCameraSupportUtil;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class GimbalDiagnosticsHandler extends DiagnosticsBaseHandler {
    private static final DJIDiagnosticsType TYPE = DJIDiagnosticsType.GIMBAL;
    private int gimbalIndex;
    private List<DiagnosticsModel<DataGimbalGetPushCheckStatus>> mCheckStatusModels;
    private List<DiagnosticsModel<DataGimbalGetPushParams>> mDataGimbalGetPushParamsModels;
    private List<DiagnosticsModel<DataOsdGetPushCommon>> mDataOsdGetPushCommonModels;
    private DiagnosticsModel<DataEyeGetPushPointState> mQuickSpinningModel;

    public GimbalDiagnosticsHandler(DiagnosticsHandlerUpdateObserver observer) {
        this(observer, 0);
    }

    public GimbalDiagnosticsHandler(DiagnosticsHandlerUpdateObserver observer, int gimbalIndex2) {
        super(observer);
        this.gimbalIndex = 0;
        this.gimbalIndex = gimbalIndex2;
        initDiagnostics();
    }

    /* access modifiers changed from: protected */
    public void onRegister() {
        if (DataGimbalGetPushCheckStatus.getInstance().isGetted()) {
            onEvent3BackgroundThread(DataGimbalGetPushCheckStatus.getInstance());
        }
        if (DataGimbalGetPushParams.getInstance().isGetted()) {
            onEvent3BackgroundThread(DataGimbalGetPushParams.getInstance());
        }
        if (DataOsdGetPushCommon.getInstance().isGetted()) {
            onEvent3BackgroundThread(DataOsdGetPushCommon.getInstance());
        }
        if (DataEyeGetPushPointState.getInstance().isGetted()) {
            onEvent3BackgroundThread(DataEyeGetPushPointState.getInstance());
        }
    }

    /* access modifiers changed from: protected */
    public void onUnregister() {
    }

    private void initDiagnostics() {
        this.mDataGimbalGetPushParamsModels = new ArrayList();
        this.mCheckStatusModels = new ArrayList();
        this.mDataOsdGetPushCommonModels = new ArrayList();
        this.mDataGimbalGetPushParamsModels.addAll(Arrays.asList(new DiagnosticsIfModel((int) DJIDiagnosticsError.Gimbal.YAW_REACH_LIMT, GimbalDiagnosticsHandler$$Lambda$0.$instance), new DiagnosticsIfModel((int) DJIDiagnosticsError.Gimbal.PITCH_REACH_LIMT, GimbalDiagnosticsHandler$$Lambda$1.$instance), new DiagnosticsIfModel((int) DJIDiagnosticsError.Gimbal.ROLL_REACH_LIMT, GimbalDiagnosticsHandler$$Lambda$2.$instance), new DiagnosticsIfModel((int) DJIDiagnosticsError.Gimbal.MOTOR_OVERLOAD, new GimbalDiagnosticsHandler$$Lambda$3(this))));
        this.mCheckStatusModels.addAll(Arrays.asList(new DiagnosticsIfModel((int) DJIDiagnosticsError.Gimbal.STARTUP_BLOCK, new GimbalDiagnosticsHandler$$Lambda$4(this)), new DiagnosticsIfModel((int) DJIDiagnosticsError.Gimbal.WAIT_AUTO_RESTART, new GimbalDiagnosticsHandler$$Lambda$5(this)), new DiagnosticsIfModel((int) DJIDiagnosticsError.Gimbal.VIBRATION_ABNORMAL, new GimbalDiagnosticsHandler$$Lambda$6(this)), new DiagnosticsIfModel((int) DJIDiagnosticsError.Gimbal.BROKEN_STATE, new GimbalDiagnosticsHandler$$Lambda$7(this)), new DiagnosticsIfModel((int) DJIDiagnosticsError.Gimbal.MOTOR_PROTECTION_ENABLED, new GimbalDiagnosticsHandler$$Lambda$8(this)), new DiagnosticsIfModel((int) DJIDiagnosticsError.Gimbal.ROTATION_ERROR, new GimbalDiagnosticsHandler$$Lambda$9(this)), new DiagnosticsIfModel((int) DJIDiagnosticsError.Gimbal.REACHED_ROLL_MECHANICAL_LIMIT, new GimbalDiagnosticsHandler$$Lambda$10(this)), new DiagnosticsIfModel((int) DJIDiagnosticsError.Gimbal.REACHED_PITCH_MECHANICAL_LIMIT, new GimbalDiagnosticsHandler$$Lambda$11(this)), new DiagnosticsIfModel((int) DJIDiagnosticsError.Gimbal.SECTORS_JUDGE_ERROR, GimbalDiagnosticsHandler$$Lambda$12.$instance), new DiagnosticsIfModel((int) DJIDiagnosticsError.Gimbal.CALIBRATE_ERROR, GimbalDiagnosticsHandler$$Lambda$13.$instance), new DiagnosticsIfModel((int) DJIDiagnosticsError.Gimbal.CONNECT_TO_FC_ERROR, GimbalDiagnosticsHandler$$Lambda$14.$instance), new DiagnosticsIfModel((int) DJIDiagnosticsError.Gimbal.GYROSCOPE_DATA_ERROR, GimbalDiagnosticsHandler$$Lambda$15.$instance), new DiagnosticsIfModel((int) DJIDiagnosticsError.Gimbal.PITCH_ESC_DATA_ERROR, GimbalDiagnosticsHandler$$Lambda$16.$instance), new DiagnosticsIfModel((int) DJIDiagnosticsError.Gimbal.ROLL_ESC_DATA_ERROR, GimbalDiagnosticsHandler$$Lambda$17.$instance), new DiagnosticsIfModel((int) DJIDiagnosticsError.Gimbal.YAW_ESC_DATA_ERROR, GimbalDiagnosticsHandler$$Lambda$18.$instance), new DiagnosticsIfModel((int) DJIDiagnosticsError.Gimbal.IMU_DATA_DISMATCH, GimbalDiagnosticsHandler$$Lambda$19.$instance)));
        this.mDataOsdGetPushCommonModels.addAll(Arrays.asList(new DiagnosticsIfModel((int) DJIDiagnosticsError.Gimbal.IS_QUICK_SPINNING, GimbalDiagnosticsHandler$$Lambda$20.$instance)));
        this.mQuickSpinningModel = new DiagnosticsIfModel((int) DJIDiagnosticsError.Gimbal.IS_QUICK_SPINNING, GimbalDiagnosticsHandler$$Lambda$21.$instance);
    }

    /* access modifiers changed from: package-private */
    public final /* synthetic */ boolean lambda$initDiagnostics$3$GimbalDiagnosticsHandler(DataGimbalGetPushParams param) {
        return param.isStuck(this.gimbalIndex);
    }

    /* access modifiers changed from: package-private */
    public final /* synthetic */ boolean lambda$initDiagnostics$4$GimbalDiagnosticsHandler(DataGimbalGetPushCheckStatus status) {
        return status.getLimitStatus() == 1 && !isWM100();
    }

    /* access modifiers changed from: package-private */
    public final /* synthetic */ boolean lambda$initDiagnostics$5$GimbalDiagnosticsHandler(DataGimbalGetPushCheckStatus status) {
        return status.getLimitStatus() == 2 && !isWM100();
    }

    /* access modifiers changed from: package-private */
    public final /* synthetic */ boolean lambda$initDiagnostics$6$GimbalDiagnosticsHandler(DataGimbalGetPushCheckStatus status) {
        return status.getVibrateStatus() && !isWM100();
    }

    /* access modifiers changed from: package-private */
    public final /* synthetic */ boolean lambda$initDiagnostics$7$GimbalDiagnosticsHandler(DataGimbalGetPushCheckStatus status) {
        return status.getGimbalBrokenState() && isWM100();
    }

    /* access modifiers changed from: package-private */
    public final /* synthetic */ boolean lambda$initDiagnostics$8$GimbalDiagnosticsHandler(DataGimbalGetPushCheckStatus status) {
        return status.getGimbalMotorProtectionEnabled() && isWM100();
    }

    /* access modifiers changed from: package-private */
    public final /* synthetic */ boolean lambda$initDiagnostics$9$GimbalDiagnosticsHandler(DataGimbalGetPushCheckStatus status) {
        return status.getGimbalRotationError() && isWM100();
    }

    /* access modifiers changed from: package-private */
    public final /* synthetic */ boolean lambda$initDiagnostics$10$GimbalDiagnosticsHandler(DataGimbalGetPushCheckStatus status) {
        return status.getGimbalReachedRollMechanicalLimit() && isWM100();
    }

    /* access modifiers changed from: package-private */
    public final /* synthetic */ boolean lambda$initDiagnostics$11$GimbalDiagnosticsHandler(DataGimbalGetPushCheckStatus status) {
        return status.getGimbalReachedPitchMechanicalLimit() && isWM100();
    }

    public Set<DJIDiagnostics> getDiagnosisList() {
        Set<DJIDiagnostics> diagnosisList = new HashSet<>();
        for (DiagnosticsModel<DataGimbalGetPushCheckStatus> checkStatusModel : this.mCheckStatusModels) {
            DJIDiagnosticsType dJIDiagnosticsType = TYPE;
            diagnosisList.getClass();
            checkStatusModel.doIfError(dJIDiagnosticsType, GimbalDiagnosticsHandler$$Lambda$22.get$Lambda(diagnosisList), this.gimbalIndex);
        }
        for (DiagnosticsModel<DataGimbalGetPushParams> model : this.mDataGimbalGetPushParamsModels) {
            DJIDiagnosticsType dJIDiagnosticsType2 = TYPE;
            diagnosisList.getClass();
            model.doIfError(dJIDiagnosticsType2, GimbalDiagnosticsHandler$$Lambda$23.get$Lambda(diagnosisList), this.gimbalIndex);
        }
        for (DiagnosticsModel<DataOsdGetPushCommon> model2 : this.mDataOsdGetPushCommonModels) {
            DJIDiagnosticsType dJIDiagnosticsType3 = TYPE;
            diagnosisList.getClass();
            model2.doIfError(dJIDiagnosticsType3, GimbalDiagnosticsHandler$$Lambda$24.get$Lambda(diagnosisList), this.gimbalIndex);
        }
        DiagnosticsModel<DataEyeGetPushPointState> diagnosticsModel = this.mQuickSpinningModel;
        DJIDiagnosticsType dJIDiagnosticsType4 = TYPE;
        diagnosisList.getClass();
        diagnosticsModel.doIfError(dJIDiagnosticsType4, GimbalDiagnosticsHandler$$Lambda$25.get$Lambda(diagnosisList), this.gimbalIndex);
        return diagnosisList;
    }

    public void reset(int componentIndex) {
        for (DiagnosticsModel<DataGimbalGetPushCheckStatus> checkStatusModel : this.mCheckStatusModels) {
            checkStatusModel.reset();
        }
        for (DiagnosticsModel<DataGimbalGetPushParams> model : this.mDataGimbalGetPushParamsModels) {
            model.reset();
        }
        for (DiagnosticsModel<DataOsdGetPushCommon> model2 : this.mDataOsdGetPushCommonModels) {
            model2.reset();
        }
        this.mQuickSpinningModel.reset();
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEvent3BackgroundThread(DataGimbalGetPushCheckStatus status) {
        postToDiagnosticBackgroudThread(new GimbalDiagnosticsHandler$$Lambda$26(this, status));
    }

    /* access modifiers changed from: package-private */
    public final /* synthetic */ void lambda$onEvent3BackgroundThread$19$GimbalDiagnosticsHandler(DataGimbalGetPushCheckStatus status) {
        if (status.getSenderId() == DoubleCameraSupportUtil.getGimbalIdInProtocol(this.gimbalIndex) && status.isGetted()) {
            boolean changed = false;
            for (DiagnosticsModel<DataGimbalGetPushCheckStatus> checkStatusModel : this.mCheckStatusModels) {
                changed |= checkStatusModel.statusApply(status);
            }
            if (changed) {
                notifyChange();
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEvent3BackgroundThread(DataGimbalGetPushParams status) {
        postToDiagnosticBackgroudThread(new GimbalDiagnosticsHandler$$Lambda$27(this, status));
    }

    /* access modifiers changed from: package-private */
    public final /* synthetic */ void lambda$onEvent3BackgroundThread$20$GimbalDiagnosticsHandler(DataGimbalGetPushParams status) {
        if (status.isGetted() && status.getSenderId() == DoubleCameraSupportUtil.getGimbalIdInProtocol(this.gimbalIndex)) {
            boolean changed = false;
            for (DiagnosticsModel<DataGimbalGetPushParams> model : this.mDataGimbalGetPushParamsModels) {
                changed |= model.statusApply(status);
            }
            if (changed) {
                notifyChange();
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEvent3BackgroundThread(DataOsdGetPushCommon status) {
        postToDiagnosticBackgroudThread(new GimbalDiagnosticsHandler$$Lambda$28(this, status));
    }

    /* access modifiers changed from: package-private */
    public final /* synthetic */ void lambda$onEvent3BackgroundThread$21$GimbalDiagnosticsHandler(DataOsdGetPushCommon status) {
        if (status.isGetted() && status.getSenderId() == DoubleCameraSupportUtil.getGimbalIdInProtocol(this.gimbalIndex)) {
            boolean changed = false;
            for (DiagnosticsModel<DataOsdGetPushCommon> model : this.mDataOsdGetPushCommonModels) {
                changed |= model.statusApply(status);
            }
            if (changed) {
                notifyChange();
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEvent3BackgroundThread(DataEyeGetPushPointState status) {
        postToDiagnosticBackgroudThread(new GimbalDiagnosticsHandler$$Lambda$29(this, status));
    }

    /* access modifiers changed from: package-private */
    public final /* synthetic */ void lambda$onEvent3BackgroundThread$22$GimbalDiagnosticsHandler(DataEyeGetPushPointState status) {
        if (status.isGetted() && status.getSenderId() == DoubleCameraSupportUtil.getGimbalIdInProtocol(this.gimbalIndex) && this.mQuickSpinningModel.statusApply(status)) {
            notifyChange();
        }
    }

    private boolean isWM100() {
        return DJIProductManager.getInstance().getType() == ProductType.Mammoth;
    }
}
