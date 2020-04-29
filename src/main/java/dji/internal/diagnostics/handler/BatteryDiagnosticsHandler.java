package dji.internal.diagnostics.handler;

import dji.common.battery.BatteryUtils;
import dji.common.util.HistoryInfo;
import dji.diagnostics.model.DJIDiagnostics;
import dji.diagnostics.model.DJIDiagnosticsError;
import dji.diagnostics.model.DJIDiagnosticsType;
import dji.internal.diagnostics.DJIDiagnosticsImpl;
import dji.internal.diagnostics.DiagnosticsBaseHandler;
import dji.internal.diagnostics.DiagnosticsHandlerUpdateObserver;
import dji.internal.diagnostics.checkhelper.BatteryCheckHelper;
import dji.internal.diagnostics.handler.util.DiagnosticsIfModel;
import dji.internal.diagnostics.handler.util.DiagnosticsModel;
import dji.internal.logics.CommonUtil;
import dji.midware.data.model.P3.DataBatteryGetPushCheckStatus;
import dji.midware.data.model.P3.DataCenterGetPushBatteryCommon;
import dji.midware.data.model.P3.DataFlycGetPushSmartBattery;
import dji.midware.data.model.P3.DataOsdGetPushCommon;
import dji.midware.data.model.P3.DataSmartBatteryGetPushDynamicData;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class BatteryDiagnosticsHandler extends DiagnosticsBaseHandler {
    private static final DJIDiagnosticsType TYPE = DJIDiagnosticsType.BATTERY;
    private BatteryCheckHelper.DJIBatteryCheckListener batteryIllegalListener = new BatteryCheckHelper.DJIBatteryCheckListener() {
        /* class dji.internal.diagnostics.handler.BatteryDiagnosticsHandler.AnonymousClass1 */

        public void onBatteryBanListUpdate(BatteryCheckHelper.BanSN banSN) {
            BatteryDiagnosticsHandler.this.postToDiagnosticBackgroudThread(new BatteryDiagnosticsHandler$1$$Lambda$0(this));
        }

        /* access modifiers changed from: package-private */
        public final /* synthetic */ void lambda$onBatteryBanListUpdate$0$BatteryDiagnosticsHandler$1() {
            boolean unused = BatteryDiagnosticsHandler.this.isIllegalError = !BatteryCheckHelper.getInstance().checkIsBatteryValid();
            if (BatteryDiagnosticsHandler.this.lastIllegalError != BatteryDiagnosticsHandler.this.isIllegalError) {
                BatteryDiagnosticsHandler.this.notifyChange();
            }
            boolean unused2 = BatteryDiagnosticsHandler.this.lastIllegalError = BatteryDiagnosticsHandler.this.isIllegalError;
        }

        public void onInvalidBatteryFound() {
        }
    };
    private boolean isCellAbnormal;
    private boolean isCommunicationError;
    private boolean isDangerousWarning;
    private boolean isDangerousWarningSerious;
    private boolean isDischargeError;
    private boolean isFirstChargeNotFull;
    private boolean isIllegalBattery;
    /* access modifiers changed from: private */
    public boolean isIllegalError;
    private boolean isLimitOutputMax;
    private boolean isLowTemperatureError;
    private boolean isLowVoltage;
    private boolean isNeedStudy;
    private boolean isNotEnoughError;
    private boolean isOverheatError;
    private boolean isShortCut;
    private boolean lastCommunicationError;
    private boolean lastDangerousWarning;
    private boolean lastDangerousWarningSerious;
    private boolean lastDischargeError;
    private boolean lastFirstChargeNotFull;
    private boolean lastIllegalBattery;
    /* access modifiers changed from: private */
    public boolean lastIllegalError;
    private boolean lastIsCellAbnormal;
    private boolean lastLimitOutputMax;
    private boolean lastLowTemperatureError;
    private boolean lastLowVoltage;
    private boolean lastNeedStudy;
    private boolean lastNotEnoughError;
    private boolean lastOverheatError;
    private boolean lastShortCut;
    private DataOsdGetPushCommon.MotorStartFailedCause mFailedCause;
    private List<DiagnosticsModel<DataSmartBatteryGetPushDynamicData>> mSmartBatteryGetPushDynamicModels;

    public BatteryDiagnosticsHandler(DiagnosticsHandlerUpdateObserver observer) {
        super(observer);
        initDiagnosticsList();
    }

    private void initDiagnosticsList() {
        this.mSmartBatteryGetPushDynamicModels = new ArrayList();
        this.mSmartBatteryGetPushDynamicModels.add(new DiagnosticsIfModel((int) DJIDiagnosticsError.Battery.LOW_TEMPERATURE_IN_AIR, BatteryDiagnosticsHandler$$Lambda$0.$instance).ignoreTempChange(this));
    }

    static final /* synthetic */ boolean lambda$initDiagnosticsList$0$BatteryDiagnosticsHandler(DataSmartBatteryGetPushDynamicData data) {
        return DataOsdGetPushCommon.getInstance().isMotorUp() && CommonUtil.isWM160(null) && data.getTemperature() <= 50;
    }

    /* access modifiers changed from: protected */
    public void onRegister() {
        if (DataCenterGetPushBatteryCommon.getInstance().isGetted()) {
            onEvent3BackgroundThread(DataCenterGetPushBatteryCommon.getInstance());
        }
        if (DataFlycGetPushSmartBattery.getInstance().isGetted()) {
            onEvent3BackgroundThread(DataFlycGetPushSmartBattery.getInstance());
        }
        if (DataBatteryGetPushCheckStatus.getInstance().isGetted()) {
            onEvent3BackgroundThread(DataBatteryGetPushCheckStatus.getInstance());
        }
        if (DataSmartBatteryGetPushDynamicData.getInstance().isGetted()) {
            onEvent3BackgroundThread(DataSmartBatteryGetPushDynamicData.getInstance());
        }
        BatteryCheckHelper.getInstance().addListener(this.batteryIllegalListener);
    }

    /* access modifiers changed from: protected */
    public void onUnregister() {
        BatteryCheckHelper.getInstance().removeListener(this.batteryIllegalListener);
    }

    public Set<DJIDiagnostics> getDiagnosisList() {
        Set<DJIDiagnostics> diagnosisList = new HashSet<>();
        if (this.isCellAbnormal) {
            diagnosisList.add(new DJIDiagnosticsImpl(DJIDiagnosticsType.BATTERY, DJIDiagnosticsError.Battery.CELL_BROKEN));
        }
        if (this.isNeedStudy) {
            diagnosisList.add(new DJIDiagnosticsImpl(DJIDiagnosticsType.BATTERY, DJIDiagnosticsError.Battery.NEED_STUDY));
        }
        if (this.isFirstChargeNotFull) {
            diagnosisList.add(new DJIDiagnosticsImpl(DJIDiagnosticsType.BATTERY, DJIDiagnosticsError.Battery.FIRST_CHARGE_NOT_FULL));
        }
        if (this.isDischargeError) {
            diagnosisList.add(new DJIDiagnosticsImpl(DJIDiagnosticsType.BATTERY, DJIDiagnosticsError.Battery.DISCHARGE_OVER_CURRENT));
        }
        if (this.isOverheatError) {
            diagnosisList.add(new DJIDiagnosticsImpl(DJIDiagnosticsType.BATTERY, DJIDiagnosticsError.Battery.DISCHARGE_OVER_HEAT));
        }
        if (this.isLowTemperatureError) {
            diagnosisList.add(new DJIDiagnosticsImpl(DJIDiagnosticsType.BATTERY, DJIDiagnosticsError.Battery.LOW_TEMPERATURE));
        }
        if (this.isIllegalError) {
            diagnosisList.add(new DJIDiagnosticsImpl(DJIDiagnosticsType.BATTERY, DJIDiagnosticsError.Battery.ILLEGAL));
        }
        if (this.isCommunicationError) {
            diagnosisList.add(new DJIDiagnosticsImpl(DJIDiagnosticsType.BATTERY, DJIDiagnosticsError.Battery.COMMUNICATION_FAILED));
        }
        if (this.isNotEnoughError) {
            diagnosisList.add(new DJIDiagnosticsImpl(DJIDiagnosticsType.BATTERY, DJIDiagnosticsError.Battery.NOT_ENOUGH));
        }
        if (this.isShortCut) {
            diagnosisList.add(new DJIDiagnosticsImpl(DJIDiagnosticsType.BATTERY, DJIDiagnosticsError.Battery.SHORT_CUT));
        }
        if (this.isLimitOutputMax) {
            diagnosisList.add(new DJIDiagnosticsImpl(DJIDiagnosticsType.BATTERY, DJIDiagnosticsError.Battery.LIMIT_OUTPUT_MAX));
        }
        if (this.isDangerousWarningSerious) {
            diagnosisList.add(new DJIDiagnosticsImpl(DJIDiagnosticsType.BATTERY, DJIDiagnosticsError.Battery.DANGEROUS_WARNING_SERIOUS));
        }
        if (this.isDangerousWarning) {
            diagnosisList.add(new DJIDiagnosticsImpl(DJIDiagnosticsType.BATTERY, DJIDiagnosticsError.Battery.DANGEROUS_WARNING));
        }
        if (this.isIllegalBattery) {
            diagnosisList.add(new DJIDiagnosticsImpl(DJIDiagnosticsType.BATTERY, DJIDiagnosticsError.Battery.ILLEGAL_BATTERY));
        }
        if (this.isLowVoltage) {
            diagnosisList.add(new DJIDiagnosticsImpl(TYPE, DJIDiagnosticsError.Battery.LOW_VOLTAGE));
        }
        for (DiagnosticsModel<DataSmartBatteryGetPushDynamicData> model : this.mSmartBatteryGetPushDynamicModels) {
            DJIDiagnosticsType dJIDiagnosticsType = TYPE;
            diagnosisList.getClass();
            model.doIfError(dJIDiagnosticsType, BatteryDiagnosticsHandler$$Lambda$1.get$Lambda(diagnosisList));
        }
        return diagnosisList;
    }

    public void reset(int componentIndex) {
        this.lastIsCellAbnormal = false;
        this.isCellAbnormal = false;
        this.isNeedStudy = false;
        this.lastNeedStudy = false;
        this.isFirstChargeNotFull = false;
        this.lastFirstChargeNotFull = false;
        this.isDischargeError = false;
        this.lastDischargeError = false;
        this.isOverheatError = false;
        this.lastOverheatError = false;
        this.isLowTemperatureError = false;
        this.lastLowTemperatureError = false;
        this.isIllegalError = false;
        this.lastIllegalError = false;
        this.isCommunicationError = false;
        this.lastCommunicationError = false;
        this.isNotEnoughError = false;
        this.lastNotEnoughError = false;
        this.isShortCut = false;
        this.lastShortCut = false;
        this.isLimitOutputMax = false;
        this.lastLimitOutputMax = false;
        this.isDangerousWarningSerious = false;
        this.lastDangerousWarningSerious = false;
        this.isDangerousWarning = false;
        this.lastDangerousWarning = false;
        this.isIllegalBattery = false;
        this.lastIllegalBattery = false;
        this.isLowVoltage = false;
        this.lastLowVoltage = false;
        for (DiagnosticsModel<DataSmartBatteryGetPushDynamicData> model : this.mSmartBatteryGetPushDynamicModels) {
            model.reset();
        }
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEvent3BackgroundThread(DataCenterGetPushBatteryCommon batteryCommon) {
        postToDiagnosticBackgroudThread(new BatteryDiagnosticsHandler$$Lambda$2(this));
    }

    /* access modifiers changed from: package-private */
    public final /* synthetic */ void lambda$onEvent3BackgroundThread$1$BatteryDiagnosticsHandler() {
        handleCenterException(getIsCellAbnormal(), getIsNeedStudy());
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEvent3BackgroundThread(DataOsdGetPushCommon common) {
        DataOsdGetPushCommon.MotorStartFailedCause causeNoStartAction = common.getMotorStartCauseNoStartAction();
        if (this.mFailedCause != causeNoStartAction) {
            this.mFailedCause = causeNoStartAction;
            boolean isCommunicationError2 = getIsCommunicationError();
            if (isCommunicationError2 != this.isCommunicationError) {
                postToDiagnosticBackgroudThread(new BatteryDiagnosticsHandler$$Lambda$3(this, isCommunicationError2));
            }
        }
    }

    /* access modifiers changed from: package-private */
    public final /* synthetic */ void lambda$onEvent3BackgroundThread$2$BatteryDiagnosticsHandler(boolean isCommunicationError2) {
        this.isCommunicationError = isCommunicationError2;
        notifyChange();
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEvent3BackgroundThread(DataFlycGetPushSmartBattery smartBattery) {
        postToDiagnosticBackgroudThread(new BatteryDiagnosticsHandler$$Lambda$4(this, smartBattery));
    }

    /* access modifiers changed from: package-private */
    public final /* synthetic */ void lambda$onEvent3BackgroundThread$3$BatteryDiagnosticsHandler(DataFlycGetPushSmartBattery smartBattery) {
        boolean isLimitOutputMax2;
        boolean isDangerousWarningSerious2;
        boolean isDangerousWarning2;
        boolean isIllegalBattery2 = true;
        boolean isFirstChargeNotFull2 = smartBattery.isFirstChargeNotFull();
        int status = smartBattery.getStatus();
        if ((status & 4096) != 0) {
            isLimitOutputMax2 = true;
        } else {
            isLimitOutputMax2 = false;
        }
        if ((status & 8192) != 0) {
            isDangerousWarningSerious2 = true;
        } else {
            isDangerousWarningSerious2 = false;
        }
        if ((status & 16384) != 0) {
            isDangerousWarning2 = true;
        } else {
            isDangerousWarning2 = false;
        }
        if ((65536 & status) == 0) {
            isIllegalBattery2 = false;
        }
        handleFlycSmartBatteryException(isFirstChargeNotFull2, isLimitOutputMax2, isDangerousWarningSerious2, isDangerousWarning2, isIllegalBattery2, getIsNeedStudy(), getIsCommunicationError());
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEvent3BackgroundThread(DataBatteryGetPushCheckStatus battery) {
        postToDiagnosticBackgroudThread(new BatteryDiagnosticsHandler$$Lambda$5(this));
    }

    /* access modifiers changed from: package-private */
    public final /* synthetic */ void lambda$onEvent3BackgroundThread$4$BatteryDiagnosticsHandler() {
        handleBatteryCheckStatusException(getIsDischargeError(), getIsOverheatError(), getIsLowTemperatureError());
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEvent3BackgroundThread(DataSmartBatteryGetPushDynamicData status) {
        postToDiagnosticBackgroudThread(new BatteryDiagnosticsHandler$$Lambda$6(this, status));
    }

    /* access modifiers changed from: package-private */
    public final /* synthetic */ void lambda$onEvent3BackgroundThread$5$BatteryDiagnosticsHandler(DataSmartBatteryGetPushDynamicData status) {
        if (status.isGetted()) {
            boolean changed = false;
            boolean isCommunicationError2 = getIsCommunicationError();
            boolean isNotEnough = BatteryUtils.isErrorBatteryStatus(status.getStatus(), 1);
            boolean isShortCut2 = BatteryUtils.isErrorBatteryStatus(status.getStatus(), 8);
            boolean isDischargeError2 = getIsDischargeError();
            boolean isOverHeadError = getIsOverheatError();
            boolean isLowHeatError = getIsLowTemperatureError();
            boolean isCellAbnormal2 = getIsCellAbnormal();
            boolean isLowVoltage2 = BatteryUtils.isErrorBatteryStatus(status.getStatus(), 5) || BatteryUtils.isErrorBatteryStatus(status.getStatus(), 12);
            if (!DiagnosticsBaseHandler.compareBooleans(new boolean[]{isCommunicationError2, isNotEnough, isShortCut2, isDischargeError2, isOverHeadError, isLowHeatError, isCellAbnormal2, isLowVoltage2}, new boolean[]{this.lastCommunicationError, this.lastNotEnoughError, this.lastShortCut, this.lastDischargeError, this.lastOverheatError, this.lastLowTemperatureError, this.lastIsCellAbnormal, this.lastLowVoltage})) {
                this.isCommunicationError = isCommunicationError2;
                this.isNotEnoughError = isNotEnough;
                this.isShortCut = isShortCut2;
                this.isDischargeError = isDischargeError2;
                this.isOverheatError = isOverHeadError;
                this.isLowTemperatureError = isLowHeatError;
                this.isCellAbnormal = isCellAbnormal2;
                this.isLowVoltage = isLowVoltage2;
                changed = true;
            }
            this.lastCommunicationError = isCommunicationError2;
            this.lastNotEnoughError = isNotEnough;
            this.lastShortCut = isShortCut2;
            this.lastDischargeError = isDischargeError2;
            this.lastOverheatError = isOverHeadError;
            this.lastLowTemperatureError = isLowHeatError;
            this.lastIsCellAbnormal = isCellAbnormal2;
            this.lastLowVoltage = isLowVoltage2;
            for (DiagnosticsModel<DataSmartBatteryGetPushDynamicData> model : this.mSmartBatteryGetPushDynamicModels) {
                changed |= model.statusApply(status);
            }
            if (changed) {
                notifyChange();
            }
        }
    }

    private void handleCenterException(boolean isCellAbnormal2, boolean needStudy) {
        if (!DiagnosticsBaseHandler.compareBooleans(new boolean[]{isCellAbnormal2, needStudy}, new boolean[]{this.lastIsCellAbnormal, this.lastNeedStudy})) {
            resetBatteryCommon();
            this.isCellAbnormal = isCellAbnormal2;
            this.isNeedStudy = needStudy;
            notifyChange();
        }
        this.lastIsCellAbnormal = isCellAbnormal2;
        this.lastNeedStudy = needStudy;
    }

    private void handleFlycSmartBatteryException(boolean isFirstChargeNotFull2, boolean isLimitOutputMax2, boolean isDangerousWarningSerious2, boolean isDangerousWarning2, boolean isIllegalBattery2, boolean isNeedStudy2, boolean isCommunicationError2) {
        if (!DiagnosticsBaseHandler.compareBooleans(new boolean[]{isFirstChargeNotFull2, isLimitOutputMax2, isDangerousWarningSerious2, isDangerousWarning2, isIllegalBattery2, isNeedStudy2, isCommunicationError2}, new boolean[]{this.lastFirstChargeNotFull, this.lastLimitOutputMax, this.lastDangerousWarningSerious, this.lastDangerousWarning, this.lastIllegalBattery, this.lastNeedStudy, this.lastCommunicationError})) {
            resetSmartBattery();
            this.isFirstChargeNotFull = isFirstChargeNotFull2;
            this.isLimitOutputMax = isLimitOutputMax2;
            this.isDangerousWarningSerious = isDangerousWarningSerious2;
            this.isDangerousWarning = isDangerousWarning2;
            this.isIllegalBattery = isIllegalBattery2;
            this.isNeedStudy = isNeedStudy2;
            this.isCommunicationError = isCommunicationError2;
            notifyChange();
        }
        this.lastFirstChargeNotFull = isFirstChargeNotFull2;
        this.lastLimitOutputMax = isLimitOutputMax2;
        this.lastDangerousWarningSerious = isDangerousWarningSerious2;
        this.lastDangerousWarning = isDangerousWarning2;
        this.lastIllegalBattery = isIllegalBattery2;
        this.lastNeedStudy = isNeedStudy2;
        this.lastCommunicationError = isCommunicationError2;
    }

    private void handleBatteryCheckStatusException(boolean isDischargeError2, boolean isOverheatError2, boolean isLowHeatError) {
        if (!DiagnosticsBaseHandler.compareBooleans(new boolean[]{isDischargeError2, isOverheatError2, isLowHeatError}, new boolean[]{this.lastDischargeError, this.lastOverheatError, this.lastLowTemperatureError})) {
            resetBatteryCheckStatus();
            this.isDischargeError = isDischargeError2;
            this.isOverheatError = isOverheatError2;
            this.isLowTemperatureError = isLowHeatError;
            notifyChange();
        }
        this.lastDischargeError = isDischargeError2;
        this.lastOverheatError = isOverheatError2;
        this.lastLowTemperatureError = isLowHeatError;
    }

    private void resetBatteryCommon() {
        this.isCellAbnormal = false;
        this.isNeedStudy = false;
    }

    private void resetSmartBattery() {
        this.isFirstChargeNotFull = false;
        this.isLimitOutputMax = false;
        this.isDangerousWarningSerious = false;
        this.isDangerousWarning = false;
        this.isIllegalBattery = false;
    }

    private void resetBatteryCheckStatus() {
        this.isDischargeError = false;
        this.isOverheatError = false;
        this.isLowTemperatureError = false;
    }

    private boolean getIsCellAbnormal() {
        boolean isError1 = false;
        boolean isError2 = false;
        if (DataSmartBatteryGetPushDynamicData.getInstance().isGetted()) {
            isError1 = BatteryUtils.isErrorBatteryStatus(DataSmartBatteryGetPushDynamicData.getInstance().getStatus(), 6);
        }
        if (DataCenterGetPushBatteryCommon.getInstance().isGetted()) {
            HistoryInfo parser = new HistoryInfo();
            parser.parseSimple(DataCenterGetPushBatteryCommon.getInstance().getErrorType());
            if (parser.getInvalidNum() != 0) {
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

    private boolean getIsNeedStudy() {
        boolean isError1 = false;
        boolean isError2 = false;
        if (DataFlycGetPushSmartBattery.getInstance().isGetted()) {
            isError1 = (DataFlycGetPushSmartBattery.getInstance().getStatus() & 2048) != 0;
        }
        if (DataCenterGetPushBatteryCommon.getInstance().isGetted()) {
            isError2 = DataCenterGetPushBatteryCommon.getInstance().isNeedStudy();
        }
        if (isError1 || isError2) {
            return true;
        }
        return false;
    }

    private boolean getIsDischargeError() {
        boolean isError1 = false;
        boolean isError2 = false;
        if (DataSmartBatteryGetPushDynamicData.getInstance().isGetted()) {
            isError1 = BatteryUtils.isErrorBatteryStatus(DataSmartBatteryGetPushDynamicData.getInstance().getStatus(), 9);
        }
        if (DataBatteryGetPushCheckStatus.getInstance().isGetted()) {
            if (DataBatteryGetPushCheckStatus.getInstance().getFirstDischargeStatus() || DataBatteryGetPushCheckStatus.getInstance().getSecondDischargeStatus()) {
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

    private boolean getIsOverheatError() {
        boolean isError1 = false;
        boolean isError2 = false;
        if (DataSmartBatteryGetPushDynamicData.getInstance().isGetted()) {
            isError1 = BatteryUtils.isErrorBatteryStatus(DataSmartBatteryGetPushDynamicData.getInstance().getStatus(), 10);
        }
        if (DataBatteryGetPushCheckStatus.getInstance().isGetted()) {
            if (DataBatteryGetPushCheckStatus.getInstance().getFirstOverheatStatus() || DataBatteryGetPushCheckStatus.getInstance().getSecondOverheatStatus()) {
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

    private boolean getIsLowTemperatureError() {
        boolean isError1 = false;
        boolean isError2 = false;
        if (DataSmartBatteryGetPushDynamicData.getInstance().isGetted()) {
            isError1 = BatteryUtils.isErrorBatteryStatus(DataSmartBatteryGetPushDynamicData.getInstance().getStatus(), 11);
        }
        if (DataBatteryGetPushCheckStatus.getInstance().isGetted()) {
            if (DataBatteryGetPushCheckStatus.getInstance().getFirstLowheatStatus() || DataBatteryGetPushCheckStatus.getInstance().getSecondLowheatStatus()) {
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

    private boolean getIsCommunicationError() {
        boolean isError1 = false;
        boolean isError2 = false;
        boolean isError3 = false;
        if (DataSmartBatteryGetPushDynamicData.getInstance().isGetted()) {
            isError1 = BatteryUtils.isErrorBatteryStatus(DataSmartBatteryGetPushDynamicData.getInstance().getStatus(), 2);
        }
        if (DataFlycGetPushSmartBattery.getInstance().isGetted()) {
            isError2 = (DataFlycGetPushSmartBattery.getInstance().getStatus() & 128) != 0;
        }
        if (DataOsdGetPushCommon.getInstance().isGetted()) {
            if (DataOsdGetPushCommon.getInstance().getMotorStartCauseNoStartAction() == DataOsdGetPushCommon.MotorStartFailedCause.BatteryCommuniteError || DataOsdGetPushCommon.getInstance().getMotorFailedCause() == DataOsdGetPushCommon.MotorStartFailedCause.BatteryCommuniteError) {
                isError3 = true;
            } else {
                isError3 = false;
            }
        }
        if (isError1 || isError2 || isError3) {
            return true;
        }
        return false;
    }
}
