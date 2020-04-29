package dji.common.gimbal;

import android.support.annotation.NonNull;
import dji.fieldAnnotation.EXClassNullAway;

@EXClassNullAway
public class GimbalState {
    private Attitude attitudeInDegrees;
    private BalanceState balanceState;
    private int calibrationProgress;
    private GimbalMode gimbalMode;
    private boolean isAttitudeReset;
    private boolean isBalanceTesting;
    private boolean isCalibrating;
    private boolean isCalibrationSuccessful;
    private boolean isMobileDeviceMounted;
    private boolean isMotorOverloaded;
    private boolean isMountedUpwards;
    private boolean isPitchAtStop;
    private boolean isRollAtStop;
    private boolean isYawAtStop;
    private float pitchFineTuneInDegrees;
    private BalanceTestResult pitchTestResult;
    private float rollFineTuneInDegrees;
    private BalanceTestResult rollTestResult;
    private float yawFineTuneInDegrees;
    private float yawRelativeToAircraftHeading;

    public interface Callback {
        void onUpdate(@NonNull GimbalState gimbalState);
    }

    private GimbalState(Builder builder) {
        this.attitudeInDegrees = null;
        this.rollFineTuneInDegrees = -1.0f;
        this.pitchFineTuneInDegrees = -1.0f;
        this.yawFineTuneInDegrees = -1.0f;
        this.gimbalMode = null;
        this.isAttitudeReset = false;
        this.isCalibrating = false;
        this.isPitchAtStop = false;
        this.isRollAtStop = false;
        this.isCalibrationSuccessful = false;
        this.calibrationProgress = 0;
        this.isYawAtStop = false;
        this.isMountedUpwards = false;
        this.isBalanceTesting = false;
        this.attitudeInDegrees = builder.attitudeInDegrees;
        this.rollFineTuneInDegrees = builder.rollFineTuneInDegrees;
        this.pitchFineTuneInDegrees = builder.pitchFineTuneInDegrees;
        this.yawFineTuneInDegrees = builder.yawFineTuneInDegrees;
        this.gimbalMode = builder.gimbalMode;
        this.isAttitudeReset = builder.isAttitudeReset;
        this.isCalibrating = builder.isCalibrating;
        this.isPitchAtStop = builder.isPitchAtStop;
        this.isRollAtStop = builder.isRollAtStop;
        this.isCalibrationSuccessful = builder.isCalibrationSuccessful;
        this.calibrationProgress = builder.calibrationProgress;
        this.isYawAtStop = builder.isYawAtStop;
        this.isMountedUpwards = builder.isMountedUpwards;
        this.isBalanceTesting = builder.isBalanceTesting;
        this.pitchTestResult = builder.pitchTestResult;
        this.rollTestResult = builder.rollTestResult;
        this.isMobileDeviceMounted = builder.isMobileDeviceMounted;
        this.isMotorOverloaded = builder.isMotorOverloaded;
        this.balanceState = builder.balanceState;
        this.yawRelativeToAircraftHeading = builder.yawRelativeAicraftHeading;
    }

    public Attitude getAttitudeInDegrees() {
        return this.attitudeInDegrees;
    }

    public float getRollFineTuneInDegrees() {
        return this.rollFineTuneInDegrees;
    }

    public float getPitchFineTuneInDegrees() {
        return this.pitchFineTuneInDegrees;
    }

    public float getYawFineTuneInDegrees() {
        return this.yawFineTuneInDegrees;
    }

    public GimbalMode getMode() {
        return this.gimbalMode;
    }

    @Deprecated
    public boolean isAttitudeReset() {
        return this.isAttitudeReset;
    }

    public boolean isCalibrating() {
        return this.isCalibrating;
    }

    public boolean isCalibrationSuccessful() {
        return this.isCalibrationSuccessful;
    }

    public boolean isPitchAtStop() {
        return this.isPitchAtStop;
    }

    public boolean isRollAtStop() {
        return this.isRollAtStop;
    }

    public boolean isYawAtStop() {
        return this.isYawAtStop;
    }

    public boolean isBalanceTesting() {
        return this.isBalanceTesting;
    }

    public BalanceTestResult getPitchBalanceTestResult() {
        return this.pitchTestResult;
    }

    public BalanceTestResult getRollBalanceTestResult() {
        return this.rollTestResult;
    }

    public boolean isMobileDeviceMounted() {
        return this.isMobileDeviceMounted;
    }

    public boolean isMotorOverloaded() {
        return this.isMotorOverloaded;
    }

    public BalanceState getBalanceState() {
        return this.balanceState;
    }

    public int getCalibrationProgress() {
        return this.calibrationProgress;
    }

    public float getYawRelativeToAircraftHeading() {
        return this.yawRelativeToAircraftHeading;
    }

    public boolean isMountedUpwards() {
        return this.isMountedUpwards;
    }

    public boolean equals(Object o) {
        boolean z = true;
        if (this == o) {
            return true;
        }
        if (!(o instanceof GimbalState)) {
            return false;
        }
        GimbalState that = (GimbalState) o;
        if (Float.compare(that.getRollFineTuneInDegrees(), getRollFineTuneInDegrees()) != 0 || Float.compare(that.getPitchFineTuneInDegrees(), getPitchFineTuneInDegrees()) != 0 || Float.compare(that.getYawFineTuneInDegrees(), getYawFineTuneInDegrees()) != 0 || isAttitudeReset() != that.isAttitudeReset() || isCalibrating() != that.isCalibrating() || isPitchAtStop() != that.isPitchAtStop() || isRollAtStop() != that.isRollAtStop() || isCalibrationSuccessful() != that.isCalibrationSuccessful() || getCalibrationProgress() != that.getCalibrationProgress() || isYawAtStop() != that.isYawAtStop() || isMountedUpwards() != that.isMountedUpwards() || isBalanceTesting() != that.isBalanceTesting() || isMobileDeviceMounted() != that.isMobileDeviceMounted() || isMotorOverloaded() != that.isMotorOverloaded() || Float.compare(that.getYawRelativeToAircraftHeading(), getYawRelativeToAircraftHeading()) != 0) {
            return false;
        }
        if (getAttitudeInDegrees() != null) {
            if (!getAttitudeInDegrees().equals(that.getAttitudeInDegrees())) {
                return false;
            }
        } else if (that.getAttitudeInDegrees() != null) {
            return false;
        }
        if (this.gimbalMode != that.gimbalMode || this.pitchTestResult != that.pitchTestResult || this.rollTestResult != that.rollTestResult) {
            return false;
        }
        if (getBalanceState() != that.getBalanceState()) {
            z = false;
        }
        return z;
    }

    public int hashCode() {
        int result;
        int i;
        int i2;
        int i3;
        int i4;
        int i5;
        int i6;
        int i7;
        int i8;
        int i9;
        int i10;
        int i11;
        int i12;
        int i13;
        int i14;
        int i15;
        int i16;
        int i17 = 1;
        int i18 = 0;
        if (getAttitudeInDegrees() != null) {
            result = getAttitudeInDegrees().hashCode();
        } else {
            result = 0;
        }
        int i19 = result * 31;
        if (getRollFineTuneInDegrees() != 0.0f) {
            i = Float.floatToIntBits(getRollFineTuneInDegrees());
        } else {
            i = 0;
        }
        int i20 = (i19 + i) * 31;
        if (getPitchFineTuneInDegrees() != 0.0f) {
            i2 = Float.floatToIntBits(getPitchFineTuneInDegrees());
        } else {
            i2 = 0;
        }
        int i21 = (i20 + i2) * 31;
        if (getYawFineTuneInDegrees() != 0.0f) {
            i3 = Float.floatToIntBits(getYawFineTuneInDegrees());
        } else {
            i3 = 0;
        }
        int i22 = (i21 + i3) * 31;
        if (this.gimbalMode != null) {
            i4 = this.gimbalMode.hashCode();
        } else {
            i4 = 0;
        }
        int i23 = (i22 + i4) * 31;
        if (isAttitudeReset()) {
            i5 = 1;
        } else {
            i5 = 0;
        }
        int i24 = (i23 + i5) * 31;
        if (isCalibrating()) {
            i6 = 1;
        } else {
            i6 = 0;
        }
        int i25 = (i24 + i6) * 31;
        if (isPitchAtStop()) {
            i7 = 1;
        } else {
            i7 = 0;
        }
        int i26 = (i25 + i7) * 31;
        if (isRollAtStop()) {
            i8 = 1;
        } else {
            i8 = 0;
        }
        int i27 = (i26 + i8) * 31;
        if (isCalibrationSuccessful()) {
            i9 = 1;
        } else {
            i9 = 0;
        }
        int calibrationProgress2 = (((i27 + i9) * 31) + getCalibrationProgress()) * 31;
        if (isYawAtStop()) {
            i10 = 1;
        } else {
            i10 = 0;
        }
        int i28 = (calibrationProgress2 + i10) * 31;
        if (isMountedUpwards()) {
            i11 = 1;
        } else {
            i11 = 0;
        }
        int i29 = (i28 + i11) * 31;
        if (isBalanceTesting()) {
            i12 = 1;
        } else {
            i12 = 0;
        }
        int i30 = (i29 + i12) * 31;
        if (this.pitchTestResult != null) {
            i13 = this.pitchTestResult.hashCode();
        } else {
            i13 = 0;
        }
        int i31 = (i30 + i13) * 31;
        if (this.rollTestResult != null) {
            i14 = this.rollTestResult.hashCode();
        } else {
            i14 = 0;
        }
        int i32 = (i31 + i14) * 31;
        if (isMobileDeviceMounted()) {
            i15 = 1;
        } else {
            i15 = 0;
        }
        int i33 = (i32 + i15) * 31;
        if (!isMotorOverloaded()) {
            i17 = 0;
        }
        int i34 = (i33 + i17) * 31;
        if (getBalanceState() != null) {
            i16 = getBalanceState().hashCode();
        } else {
            i16 = 0;
        }
        int i35 = (i34 + i16) * 31;
        if (getYawRelativeToAircraftHeading() != 0.0f) {
            i18 = Float.floatToIntBits(getYawRelativeToAircraftHeading());
        }
        return i35 + i18;
    }

    public static final class Builder {
        /* access modifiers changed from: private */
        public Attitude attitudeInDegrees = null;
        /* access modifiers changed from: private */
        public BalanceState balanceState = BalanceState.UNKNOWN;
        /* access modifiers changed from: private */
        public int calibrationProgress = 0;
        /* access modifiers changed from: private */
        public GimbalMode gimbalMode = null;
        /* access modifiers changed from: private */
        public boolean isAttitudeReset = false;
        /* access modifiers changed from: private */
        public boolean isBalanceTesting = false;
        /* access modifiers changed from: private */
        public boolean isCalibrating = false;
        /* access modifiers changed from: private */
        public boolean isCalibrationSuccessful = false;
        /* access modifiers changed from: private */
        public boolean isMobileDeviceMounted = false;
        /* access modifiers changed from: private */
        public boolean isMotorOverloaded = false;
        /* access modifiers changed from: private */
        public boolean isMountedUpwards = false;
        /* access modifiers changed from: private */
        public boolean isPitchAtStop = false;
        /* access modifiers changed from: private */
        public boolean isRollAtStop = false;
        /* access modifiers changed from: private */
        public boolean isYawAtStop = false;
        /* access modifiers changed from: private */
        public float pitchFineTuneInDegrees = -1.0f;
        /* access modifiers changed from: private */
        public BalanceTestResult pitchTestResult = BalanceTestResult.UNKNOWN;
        /* access modifiers changed from: private */
        public float rollFineTuneInDegrees = -1.0f;
        /* access modifiers changed from: private */
        public BalanceTestResult rollTestResult = BalanceTestResult.UNKNOWN;
        /* access modifiers changed from: private */
        public float yawFineTuneInDegrees = -1.0f;
        /* access modifiers changed from: private */
        public float yawRelativeAicraftHeading;

        public Builder attitudeInDegrees(Attitude attitude) {
            this.attitudeInDegrees = attitude;
            return this;
        }

        public Builder rollFineTuneInDegrees(float rollFineTuneInDegrees2) {
            this.rollFineTuneInDegrees = rollFineTuneInDegrees2;
            return this;
        }

        public Builder pitchFineTuneInDegrees(float pitchFineTuneInDegrees2) {
            this.pitchFineTuneInDegrees = pitchFineTuneInDegrees2;
            return this;
        }

        public Builder yawFineTuneInDegrees(float yawFineTuneInDegrees2) {
            this.yawFineTuneInDegrees = yawFineTuneInDegrees2;
            return this;
        }

        public Builder mode(GimbalMode gimbalMode2) {
            this.gimbalMode = gimbalMode2;
            return this;
        }

        @Deprecated
        public Builder isAttitudeReset(boolean isAttitudeReset2) {
            this.isAttitudeReset = isAttitudeReset2;
            return this;
        }

        public Builder isCalibrating(boolean isCalibrating2) {
            this.isCalibrating = isCalibrating2;
            return this;
        }

        public Builder isPitchAtStop(boolean isPitchAtStop2) {
            this.isPitchAtStop = isPitchAtStop2;
            return this;
        }

        public Builder isRollAtStop(boolean isRollAtStop2) {
            this.isRollAtStop = isRollAtStop2;
            return this;
        }

        public Builder isCalibrationSuccessful(boolean isCalibrationSuccessful2) {
            this.isCalibrationSuccessful = isCalibrationSuccessful2;
            return this;
        }

        public Builder calibrationProgress(int progress) {
            this.calibrationProgress = progress;
            return this;
        }

        public Builder isYawAtStop(boolean isYawAtStop2) {
            this.isYawAtStop = isYawAtStop2;
            return this;
        }

        public Builder isMountedUpwards(boolean isTop) {
            this.isMountedUpwards = isTop;
            return this;
        }

        public Builder isBalanceTesting(boolean isBalanceTesting2) {
            this.isBalanceTesting = isBalanceTesting2;
            return this;
        }

        public Builder pitchTestResult(BalanceTestResult result) {
            this.pitchTestResult = result;
            return this;
        }

        public Builder rollTestResult(BalanceTestResult result) {
            this.rollTestResult = result;
            return this;
        }

        public Builder isMobileDeviceMounted(boolean isMobileDeviceMounted2) {
            this.isMobileDeviceMounted = isMobileDeviceMounted2;
            return this;
        }

        public Builder isMotorOverloaded(boolean isMotorOverloaded2) {
            this.isMotorOverloaded = isMotorOverloaded2;
            return this;
        }

        public Builder balanceState(BalanceState state) {
            this.balanceState = state;
            return this;
        }

        public Builder yawRelativeAicraftHeading(float yawRelativeAicraftHeading2) {
            this.yawRelativeAicraftHeading = yawRelativeAicraftHeading2;
            return this;
        }

        public GimbalState build() {
            return new GimbalState(this);
        }
    }
}
