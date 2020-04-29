package dji.internal.diagnostics.handler;

import dji.diagnostics.model.DJIDiagnostics;
import dji.diagnostics.model.DJIDiagnosticsError;
import dji.diagnostics.model.DJIDiagnosticsType;
import dji.internal.diagnostics.DiagnosticsBaseHandler;
import dji.internal.diagnostics.DiagnosticsHandlerUpdateObserver;
import dji.internal.diagnostics.handler.util.DiagnosticsIfModel;
import dji.internal.diagnostics.handler.util.DiagnosticsModel;
import dji.midware.data.model.P3.Data2100GetPushCheckStatus;
import dji.midware.data.model.P3.Data2150GetPushCheckStatus;
import dji.midware.data.model.P3.DataEyeGetPushSensorException;
import dji.midware.data.model.P3.DataWM160VisionGetPushCheckStatus;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class VisionDiagnosticsHandler extends DiagnosticsBaseHandler {
    private static final DJIDiagnosticsType TYPE = DJIDiagnosticsType.VISION;
    private List<DiagnosticsModel<Data2100GetPushCheckStatus>> m2100VisionModels;
    private List<DiagnosticsModel<Data2150GetPushCheckStatus>> m2150VisionModels;
    private List<DiagnosticsModel<DataEyeGetPushSensorException>> mVisionSensorExceptionModels;
    private List<DiagnosticsModel<DataWM160VisionGetPushCheckStatus>> mWM160VisionModels;

    public VisionDiagnosticsHandler(DiagnosticsHandlerUpdateObserver observer) {
        super(observer);
        initModels();
    }

    private void initModels() {
        this.mWM160VisionModels = new ArrayList();
        this.mVisionSensorExceptionModels = new ArrayList();
        this.m2100VisionModels = new ArrayList();
        this.m2150VisionModels = new ArrayList();
        this.mWM160VisionModels.addAll(Arrays.asList(new DiagnosticsIfModel((int) DJIDiagnosticsError.Vision.FRONT_SENSOR_CALIBRATION_ERROR, VisionDiagnosticsHandler$$Lambda$0.$instance), new DiagnosticsIfModel((int) DJIDiagnosticsError.Vision.DOWN_SENSOR_CALIBRATION_ERROR, VisionDiagnosticsHandler$$Lambda$1.$instance), new DiagnosticsIfModel((int) DJIDiagnosticsError.Vision.DOWN_TOF_CALIBRATION_ERROR, VisionDiagnosticsHandler$$Lambda$2.$instance), new DiagnosticsIfModel((int) DJIDiagnosticsError.Vision.DOWN_LEFT_SENSOR_ERROR, VisionDiagnosticsHandler$$Lambda$3.$instance), new DiagnosticsIfModel((int) DJIDiagnosticsError.Vision.DOWN_RIGHT_SENSOR_ERROR, VisionDiagnosticsHandler$$Lambda$4.$instance), new DiagnosticsIfModel((int) DJIDiagnosticsError.Vision.FRONT_LEFT_SENSOR_ERROR, VisionDiagnosticsHandler$$Lambda$5.$instance), new DiagnosticsIfModel((int) DJIDiagnosticsError.Vision.FRONT_RIGHT_SENSOR_ERROR, VisionDiagnosticsHandler$$Lambda$6.$instance), new DiagnosticsIfModel((int) DJIDiagnosticsError.Vision.DOWN_TOF_SENSOR_ERROR, VisionDiagnosticsHandler$$Lambda$7.$instance), new DiagnosticsIfModel((int) DJIDiagnosticsError.Vision.RECEIVE_FC_DATA_STATUS, VisionDiagnosticsHandler$$Lambda$8.$instance), new DiagnosticsIfModel((int) DJIDiagnosticsError.Vision.SEND_FC_DATA_STATUS, VisionDiagnosticsHandler$$Lambda$9.$instance), new DiagnosticsIfModel((int) DJIDiagnosticsError.Vision.RTOS_COMMUNICATION_STATUS, VisionDiagnosticsHandler$$Lambda$10.$instance), new DiagnosticsIfModel((int) DJIDiagnosticsError.Vision.CNN_ALGORITHM_STATUS, VisionDiagnosticsHandler$$Lambda$11.$instance), new DiagnosticsIfModel((int) DJIDiagnosticsError.Vision.AUTO_EXPOSURE_MODULE_STATUS, VisionDiagnosticsHandler$$Lambda$12.$instance), new DiagnosticsIfModel((int) DJIDiagnosticsError.Vision.VIO_ALGORITHM_STATUS, VisionDiagnosticsHandler$$Lambda$13.$instance)));
        this.mVisionSensorExceptionModels.addAll(Arrays.asList(new DiagnosticsIfModel((int) DJIDiagnosticsError.Vision._3D_TOF_SENSOR_ERROR, VisionDiagnosticsHandler$$Lambda$14.$instance), new DiagnosticsIfModel((int) DJIDiagnosticsError.Vision.WEAK_AMBIENT_LIGHT_FLY_WITH_CAUTION, VisionDiagnosticsHandler$$Lambda$15.$instance)));
        this.m2100VisionModels.addAll(Arrays.asList(new DiagnosticsIfModel((int) DJIDiagnosticsError.Vision.FRONT_SENSOR_CALIBRATION_ERROR, VisionDiagnosticsHandler$$Lambda$16.$instance), new DiagnosticsIfModel((int) DJIDiagnosticsError.Vision.BACK_SENSOR_CALIBRATION_ERROR, VisionDiagnosticsHandler$$Lambda$17.$instance), new DiagnosticsIfModel((int) DJIDiagnosticsError.Vision.DOWN_SENSOR_CALIBRATION_ERROR, VisionDiagnosticsHandler$$Lambda$18.$instance), new DiagnosticsIfModel((int) DJIDiagnosticsError.Vision.DOWN_TOF_CALIBRATION_ERROR, VisionDiagnosticsHandler$$Lambda$19.$instance), new DiagnosticsIfModel((int) DJIDiagnosticsError.Vision.UP_SENSOR_CALIBRATION_ERROR, VisionDiagnosticsHandler$$Lambda$20.$instance), new DiagnosticsIfModel((int) DJIDiagnosticsError.Vision.SYSTEM_RECOMMEND_CALIBRATION, VisionDiagnosticsHandler$$Lambda$21.$instance), new DiagnosticsIfModel((int) DJIDiagnosticsError.Vision.SYSTEM_NEED_CALIBRATION, VisionDiagnosticsHandler$$Lambda$22.$instance), new DiagnosticsIfModel((int) DJIDiagnosticsError.Vision.SYSTEM_ERROR_NEED_MAINTENANCE, VisionDiagnosticsHandler$$Lambda$23.$instance), new DiagnosticsIfModel((int) DJIDiagnosticsError.Vision.DISABLE_FRONT_PERCEPTION_FOR_PROPELLER_GUARD, VisionDiagnosticsHandler$$Lambda$24.$instance), new DiagnosticsIfModel((int) DJIDiagnosticsError.Vision.DOWN_LEFT_SENSOR_ERROR, VisionDiagnosticsHandler$$Lambda$25.$instance), new DiagnosticsIfModel((int) DJIDiagnosticsError.Vision.DOWN_RIGHT_SENSOR_ERROR, VisionDiagnosticsHandler$$Lambda$26.$instance), new DiagnosticsIfModel((int) DJIDiagnosticsError.Vision.FRONT_LEFT_SENSOR_ERROR, VisionDiagnosticsHandler$$Lambda$27.$instance), new DiagnosticsIfModel((int) DJIDiagnosticsError.Vision.FRONT_RIGHT_SENSOR_ERROR, VisionDiagnosticsHandler$$Lambda$28.$instance), new DiagnosticsIfModel((int) DJIDiagnosticsError.Vision.TOP_TOF_SENSOR_ERROR, VisionDiagnosticsHandler$$Lambda$29.$instance), new DiagnosticsIfModel((int) DJIDiagnosticsError.Vision._3D_TOF_SENSOR_ERROR, VisionDiagnosticsHandler$$Lambda$30.$instance), new DiagnosticsIfModel((int) DJIDiagnosticsError.Vision.ULTRA_SONIC_SENSOR_ERROR, VisionDiagnosticsHandler$$Lambda$31.$instance), new DiagnosticsIfModel((int) DJIDiagnosticsError.Vision.SENSOR_COMMUNICATION_ERROR, VisionDiagnosticsHandler$$Lambda$32.$instance), new DiagnosticsIfModel((int) DJIDiagnosticsError.Vision.LEFT_SENSOR_CALIBRATION_ERROR, VisionDiagnosticsHandler$$Lambda$33.$instance), new DiagnosticsIfModel((int) DJIDiagnosticsError.Vision.RIGHT_SENSOR_CALIBRATION_ERROR, VisionDiagnosticsHandler$$Lambda$34.$instance), new DiagnosticsIfModel((int) DJIDiagnosticsError.Vision.LEFT_OBSTACLE_FOUND, VisionDiagnosticsHandler$$Lambda$35.$instance), new DiagnosticsIfModel((int) DJIDiagnosticsError.Vision.RIGHT_OBSTACLE_FOUND, VisionDiagnosticsHandler$$Lambda$36.$instance), new DiagnosticsIfModel((int) DJIDiagnosticsError.Vision.TOF_FIRMWARE_VERSION_ERROR, VisionDiagnosticsHandler$$Lambda$37.$instance), new DiagnosticsIfModel((int) DJIDiagnosticsError.Vision.DOWN_TOF_SENSOR_ERROR, VisionDiagnosticsHandler$$Lambda$38.$instance)));
        this.m2150VisionModels.addAll(Arrays.asList(new DiagnosticsIfModel((int) DJIDiagnosticsError.Vision.FRONT_SENSOR_CALIBRATION_ERROR, VisionDiagnosticsHandler$$Lambda$39.$instance), new DiagnosticsIfModel((int) DJIDiagnosticsError.Vision.DOWN_SENSOR_CALIBRATION_ERROR, VisionDiagnosticsHandler$$Lambda$40.$instance), new DiagnosticsIfModel((int) DJIDiagnosticsError.Vision.DOWN_TOF_CALIBRATION_ERROR, VisionDiagnosticsHandler$$Lambda$41.$instance), new DiagnosticsIfModel((int) DJIDiagnosticsError.Vision.DOWN_SENSOR_ERROR, VisionDiagnosticsHandler$$Lambda$42.$instance), new DiagnosticsIfModel((int) DJIDiagnosticsError.Vision.FRONT_SENSOR_ERROR, VisionDiagnosticsHandler$$Lambda$43.$instance), new DiagnosticsIfModel((int) DJIDiagnosticsError.Vision.MAIN_CAMERA_SENSOR_ERROR, VisionDiagnosticsHandler$$Lambda$44.$instance), new DiagnosticsIfModel((int) DJIDiagnosticsError.Vision.DOWN_TOF_SENSOR_ERROR, VisionDiagnosticsHandler$$Lambda$45.$instance), new DiagnosticsIfModel((int) DJIDiagnosticsError.Vision._3D_TOF_SENSOR_ERROR, VisionDiagnosticsHandler$$Lambda$46.$instance)));
    }

    static final /* synthetic */ boolean lambda$initModels$0$VisionDiagnosticsHandler(DataEyeGetPushSensorException exception) {
        return exception.isLeft3DTOFAbnormal() || exception.isRight3DTOFAbnormal();
    }

    static final /* synthetic */ boolean lambda$initModels$1$VisionDiagnosticsHandler(Data2100GetPushCheckStatus status) {
        return status.needPcCalibrate() || status.isEasySelfCalResult();
    }

    static final /* synthetic */ boolean lambda$initModels$2$VisionDiagnosticsHandler(Data2100GetPushCheckStatus status) {
        return status.isAutoExpAbnormal() || status.isDepthImageAbnormal() || status.isVOAbnormal() || status.isAvoidanceAbnormal();
    }

    static final /* synthetic */ boolean lambda$initModels$3$VisionDiagnosticsHandler(Data2100GetPushCheckStatus status) {
        return status.isStoreAbnormal() || status.isInnerAbnormal();
    }

    static final /* synthetic */ boolean lambda$initModels$4$VisionDiagnosticsHandler(Data2100GetPushCheckStatus status) {
        return status.isLeft3DTOFSensorAbnormal() || status.isRight3DTOFSensorAbnormal();
    }

    static final /* synthetic */ boolean lambda$initModels$5$VisionDiagnosticsHandler(Data2100GetPushCheckStatus status) {
        return status.is1860UsbAbnormal() || status.isMCUARTAbnormal() || status.isSwaveAbnormal() || status.isCPLDConnAbnormal() || status.isMCUARTSendAbnormal();
    }

    static final /* synthetic */ boolean lambda$initModels$6$VisionDiagnosticsHandler(Data2150GetPushCheckStatus status) {
        return status.doesFront3DTofSensorHasCalibrationError() || status.doesFrontVisionSensorHasCalibrationError();
    }

    /* access modifiers changed from: protected */
    public void onRegister() {
        if (Data2100GetPushCheckStatus.getInstance().isGetted()) {
            onEvent3BackgroundThread(Data2100GetPushCheckStatus.getInstance());
        }
        if (Data2150GetPushCheckStatus.getInstance().isGetted()) {
            onEvent3MainThread(Data2150GetPushCheckStatus.getInstance());
        }
        if (DataEyeGetPushSensorException.getInstance().isGetted()) {
            onEvent3MainThread(DataEyeGetPushSensorException.getInstance());
        }
        if (DataWM160VisionGetPushCheckStatus.getInstance().isGetted()) {
            onEvent3MainThread(DataWM160VisionGetPushCheckStatus.getInstance());
        }
    }

    /* access modifiers changed from: protected */
    public void onUnregister() {
    }

    public Set<DJIDiagnostics> getDiagnosisList() {
        Set<DJIDiagnostics> diagnosisList = new HashSet<>();
        for (DiagnosticsModel<DataWM160VisionGetPushCheckStatus> model : this.mWM160VisionModels) {
            DJIDiagnosticsType dJIDiagnosticsType = TYPE;
            diagnosisList.getClass();
            model.doIfError(dJIDiagnosticsType, VisionDiagnosticsHandler$$Lambda$47.get$Lambda(diagnosisList));
        }
        for (DiagnosticsModel<DataEyeGetPushSensorException> model2 : this.mVisionSensorExceptionModels) {
            DJIDiagnosticsType dJIDiagnosticsType2 = TYPE;
            diagnosisList.getClass();
            model2.doIfError(dJIDiagnosticsType2, VisionDiagnosticsHandler$$Lambda$48.get$Lambda(diagnosisList));
        }
        for (DiagnosticsModel<Data2100GetPushCheckStatus> model3 : this.m2100VisionModels) {
            DJIDiagnosticsType dJIDiagnosticsType3 = TYPE;
            diagnosisList.getClass();
            model3.doIfError(dJIDiagnosticsType3, VisionDiagnosticsHandler$$Lambda$49.get$Lambda(diagnosisList));
        }
        for (DiagnosticsModel<Data2150GetPushCheckStatus> model4 : this.m2150VisionModels) {
            DJIDiagnosticsType dJIDiagnosticsType4 = TYPE;
            diagnosisList.getClass();
            model4.doIfError(dJIDiagnosticsType4, VisionDiagnosticsHandler$$Lambda$50.get$Lambda(diagnosisList));
        }
        return diagnosisList;
    }

    public void reset(int componentIndex) {
        for (DiagnosticsModel<Data2100GetPushCheckStatus> model : this.m2100VisionModels) {
            model.reset();
        }
        for (DiagnosticsModel<Data2150GetPushCheckStatus> model2 : this.m2150VisionModels) {
            model2.reset();
        }
        for (DiagnosticsModel<DataWM160VisionGetPushCheckStatus> model3 : this.mWM160VisionModels) {
            model3.reset();
        }
        for (DiagnosticsModel<DataEyeGetPushSensorException> model4 : this.mVisionSensorExceptionModels) {
            model4.reset();
        }
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEvent3BackgroundThread(Data2100GetPushCheckStatus status) {
        postToDiagnosticBackgroudThread(new VisionDiagnosticsHandler$$Lambda$51(this, status));
    }

    /* access modifiers changed from: package-private */
    public final /* synthetic */ void lambda$onEvent3BackgroundThread$7$VisionDiagnosticsHandler(Data2100GetPushCheckStatus status) {
        boolean changed = false;
        for (DiagnosticsModel<Data2100GetPushCheckStatus> model : this.m2100VisionModels) {
            changed |= model.statusApply(status);
        }
        if (changed) {
            notifyChange();
        }
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEvent3MainThread(Data2150GetPushCheckStatus status) {
        postToDiagnosticBackgroudThread(new VisionDiagnosticsHandler$$Lambda$52(this, status));
    }

    /* access modifiers changed from: package-private */
    public final /* synthetic */ void lambda$onEvent3MainThread$8$VisionDiagnosticsHandler(Data2150GetPushCheckStatus status) {
        boolean changed = false;
        for (DiagnosticsModel<Data2150GetPushCheckStatus> model : this.m2150VisionModels) {
            changed |= model.statusApply(status);
        }
        if (changed) {
            notifyChange();
        }
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEvent3MainThread(DataEyeGetPushSensorException sensor) {
        postToDiagnosticBackgroudThread(new VisionDiagnosticsHandler$$Lambda$53(this, sensor));
    }

    /* access modifiers changed from: package-private */
    public final /* synthetic */ void lambda$onEvent3MainThread$9$VisionDiagnosticsHandler(DataEyeGetPushSensorException sensor) {
        boolean changed = false;
        for (DiagnosticsModel<DataEyeGetPushSensorException> model : this.mVisionSensorExceptionModels) {
            changed |= model.statusApply(sensor);
        }
        if (changed) {
            notifyChange();
        }
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEvent3MainThread(DataWM160VisionGetPushCheckStatus status) {
        postToDiagnosticBackgroudThread(new VisionDiagnosticsHandler$$Lambda$54(this, status));
    }

    /* access modifiers changed from: package-private */
    public final /* synthetic */ void lambda$onEvent3MainThread$10$VisionDiagnosticsHandler(DataWM160VisionGetPushCheckStatus status) {
        boolean changed = false;
        for (DiagnosticsModel<DataWM160VisionGetPushCheckStatus> model : this.mWM160VisionModels) {
            changed |= model.statusApply(status);
        }
        if (changed) {
            notifyChange();
        }
    }
}
