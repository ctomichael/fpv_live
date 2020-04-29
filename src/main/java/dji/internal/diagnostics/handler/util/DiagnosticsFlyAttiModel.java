package dji.internal.diagnostics.handler.util;

import dji.diagnostics.model.DJIDiagnosticsType;
import dji.internal.diagnostics.DJIDiagnosticsImpl;
import dji.internal.diagnostics.handler.FlightControllerDiagnosticsHandler;
import dji.internal.logics.CommonUtil;
import dji.midware.data.model.P3.DataOsdGetPushCommon;
import dji.sdksharedlib.extension.CacheHelper;
import dji.sdksharedlib.keycatalog.BatteryKeys;
import dji.utils.function.Consumer;
import java.util.Objects;

public class DiagnosticsFlyAttiModel implements DiagnosticsModel<DataOsdGetPushCommon> {
    private static final int MAX_FORBID_HEIGHT = 1000;
    private Integer mDiagnosticsCode;
    private final FunctionBool6 mDiagnosticsCodeFunction;
    private boolean mGpsValid;
    private boolean mHasPushCommonValue = false;
    private Integer mHeightLimit = null;
    private boolean mInAtti;
    private boolean mInHeightLimitZone;
    private boolean mIsMotorUp;
    private boolean mSingleBattery;
    private boolean mVisionUsed;

    public interface FunctionBool6 {
        Integer apply(boolean z, boolean z2, boolean z3, boolean z4, boolean z5, boolean z6);
    }

    public void doIfError(DJIDiagnosticsType dJIDiagnosticsType, Consumer consumer) {
        DiagnosticsModel$$CC.doIfError(this, dJIDiagnosticsType, consumer);
    }

    public DiagnosticsFlyAttiModel(FunctionBool6 diagnosticsCodeFunction) {
        this.mDiagnosticsCodeFunction = diagnosticsCodeFunction;
    }

    public boolean limitHeightApply(Integer heightLimit) {
        boolean changed;
        Integer lastHeight = this.mHeightLimit;
        this.mHeightLimit = heightLimit;
        this.mInHeightLimitZone = isInHeightLimitZone(heightLimit);
        Integer lastCode = this.mDiagnosticsCode;
        if (!this.mHasPushCommonValue) {
            this.mDiagnosticsCode = null;
        } else {
            this.mDiagnosticsCode = judgeDiagnosticsCode();
        }
        if (!Objects.equals(this.mHeightLimit, lastHeight)) {
            changed = true;
        } else {
            changed = false;
        }
        if ((this.mDiagnosticsCode == null || !this.mInHeightLimitZone || !changed) && Objects.equals(this.mDiagnosticsCode, lastCode)) {
            return false;
        }
        return true;
    }

    public boolean statusApply(DataOsdGetPushCommon push) {
        this.mHasPushCommonValue = true;
        Integer lastCode = this.mDiagnosticsCode;
        this.mGpsValid = FlightControllerDiagnosticsHandler.checkGpsValid(push);
        this.mSingleBattery = CacheHelper.toBool(CacheHelper.getBattery(BatteryKeys.IS_IN_SINGLE_BATTERY_MODE));
        this.mIsMotorUp = push.isMotorUp();
        this.mVisionUsed = push.isVisionUsed();
        this.mInAtti = CommonUtil.checkIsAttiMode(push.getFlycState());
        this.mDiagnosticsCode = judgeDiagnosticsCode();
        if (!Objects.equals(this.mDiagnosticsCode, lastCode)) {
            return true;
        }
        return false;
    }

    public void doIfError(DJIDiagnosticsType type, Consumer<DJIDiagnosticsImpl> consumer, int componentIndex) {
        DJIDiagnosticsImpl diagnostics;
        Integer diagnosticsCode = this.mDiagnosticsCode;
        if (diagnosticsCode != null) {
            if (this.mInHeightLimitZone) {
                diagnostics = new DJIDiagnosticsImpl(type, diagnosticsCode.intValue(), this.mHeightLimit, componentIndex);
            } else {
                diagnostics = new DJIDiagnosticsImpl(type, diagnosticsCode.intValue(), componentIndex);
            }
            consumer.accept(diagnostics);
        }
    }

    public void reset() {
        this.mDiagnosticsCode = null;
        this.mHasPushCommonValue = false;
        this.mGpsValid = false;
        this.mSingleBattery = false;
        this.mIsMotorUp = false;
        this.mVisionUsed = false;
        this.mInAtti = false;
        this.mInHeightLimitZone = false;
        this.mHeightLimit = 0;
    }

    private boolean isInHeightLimitZone(Integer heightLimit) {
        return heightLimit != null && heightLimit.intValue() > 0 && heightLimit.intValue() < 1000;
    }

    private Integer judgeDiagnosticsCode() {
        return this.mDiagnosticsCodeFunction.apply(this.mGpsValid, this.mSingleBattery, this.mIsMotorUp, this.mVisionUsed, this.mInAtti, this.mInHeightLimitZone);
    }
}
