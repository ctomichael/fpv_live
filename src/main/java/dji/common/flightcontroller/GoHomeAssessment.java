package dji.common.flightcontroller;

import dji.fieldAnnotation.EXClassNullAway;

@EXClassNullAway
public final class GoHomeAssessment {
    private final int batteryPercentageNeededToGoHome;
    private final int batteryPercentageNeededToLandFromCurrentHeight;
    private final float maxRadiusAircraftCanFlyAndGoHome;
    private final int remainingFlightTime;
    private final int smartRTHCountdown;
    private final SmartRTHState smartRTHState;
    private final int timeNeededToGoHome;
    private final int timeNeededToLandFromCurrentHeight;

    private GoHomeAssessment(Builder builder) {
        this.remainingFlightTime = builder.remainingFlightTime;
        this.timeNeededToGoHome = builder.timeNeededToGoHome;
        this.timeNeededToLandFromCurrentHeight = builder.timeNeededToLandFromCurrentHeight;
        this.batteryPercentageNeededToGoHome = builder.batteryPercentageNeededToGoHome;
        this.maxRadiusAircraftCanFlyAndGoHome = builder.maxRadiusAircraftCanFlyAndGoHome;
        this.batteryPercentageNeededToLandFromCurrentHeight = builder.batteryPercentageNeededToLandFromCurrentHeight;
        this.smartRTHState = builder.smartRTHState;
        this.smartRTHCountdown = builder.smartRTHCountdown;
    }

    public int getRemainingFlightTime() {
        return this.remainingFlightTime;
    }

    public int getTimeNeededToGoHome() {
        return this.timeNeededToGoHome;
    }

    public int getTimeNeededToLandFromCurrentHeight() {
        return this.timeNeededToLandFromCurrentHeight;
    }

    public int getBatteryPercentageNeededToGoHome() {
        return this.batteryPercentageNeededToGoHome;
    }

    public int getBatteryPercentageNeededToLandFromCurrentHeight() {
        return this.batteryPercentageNeededToLandFromCurrentHeight;
    }

    public float getMaxRadiusAircraftCanFlyAndGoHome() {
        return this.maxRadiusAircraftCanFlyAndGoHome;
    }

    public SmartRTHState getSmartRTHState() {
        return this.smartRTHState;
    }

    public int getSmartRTHCountdown() {
        return this.smartRTHCountdown;
    }

    public boolean equals(Object o) {
        boolean z = true;
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        GoHomeAssessment that = (GoHomeAssessment) o;
        if (getRemainingFlightTime() != that.getRemainingFlightTime() || getTimeNeededToGoHome() != that.getTimeNeededToGoHome() || getTimeNeededToLandFromCurrentHeight() != that.getTimeNeededToLandFromCurrentHeight() || getBatteryPercentageNeededToGoHome() != that.getBatteryPercentageNeededToGoHome() || Float.compare(that.getMaxRadiusAircraftCanFlyAndGoHome(), getMaxRadiusAircraftCanFlyAndGoHome()) != 0 || getBatteryPercentageNeededToLandFromCurrentHeight() != that.getBatteryPercentageNeededToLandFromCurrentHeight() || getSmartRTHCountdown() != that.getSmartRTHCountdown()) {
            return false;
        }
        if (getSmartRTHState() != that.getSmartRTHState()) {
            z = false;
        }
        return z;
    }

    public int hashCode() {
        int i = 0;
        int remainingFlightTime2 = ((((((((((getRemainingFlightTime() * 31) + getTimeNeededToGoHome()) * 31) + getTimeNeededToLandFromCurrentHeight()) * 31) + getBatteryPercentageNeededToGoHome()) * 31) + (getMaxRadiusAircraftCanFlyAndGoHome() != 0.0f ? Float.floatToIntBits(getMaxRadiusAircraftCanFlyAndGoHome()) : 0)) * 31) + getBatteryPercentageNeededToLandFromCurrentHeight()) * 31;
        if (getSmartRTHState() != null) {
            i = getSmartRTHState().hashCode();
        }
        return ((remainingFlightTime2 + i) * 31) + getSmartRTHCountdown();
    }

    public static final class Builder {
        /* access modifiers changed from: private */
        public int batteryPercentageNeededToGoHome;
        /* access modifiers changed from: private */
        public int batteryPercentageNeededToLandFromCurrentHeight;
        /* access modifiers changed from: private */
        public float maxRadiusAircraftCanFlyAndGoHome = Float.NaN;
        /* access modifiers changed from: private */
        public int remainingFlightTime;
        /* access modifiers changed from: private */
        public int smartRTHCountdown = Integer.MAX_VALUE;
        /* access modifiers changed from: private */
        public SmartRTHState smartRTHState = SmartRTHState.UNKNOWN;
        /* access modifiers changed from: private */
        public int timeNeededToGoHome;
        /* access modifiers changed from: private */
        public int timeNeededToLandFromCurrentHeight;

        public Builder remainingFlightTime(int remainingFlightTime2) {
            this.remainingFlightTime = remainingFlightTime2;
            return this;
        }

        public Builder timeNeededToGoHome(int timeNeededToGoHome2) {
            this.timeNeededToGoHome = timeNeededToGoHome2;
            return this;
        }

        public Builder timeNeededToLandFromCurrentHeight(int timeNeededToLandFromCurrentHeight2) {
            this.timeNeededToLandFromCurrentHeight = timeNeededToLandFromCurrentHeight2;
            return this;
        }

        public Builder batteryPercentageNeededToGoHome(int batteryPercentageNeededToGoHome2) {
            this.batteryPercentageNeededToGoHome = batteryPercentageNeededToGoHome2;
            return this;
        }

        public Builder maxRadiusAircraftCanFlyAndGoHome(float maxRadiusAircraftCanFlyAndGoHome2) {
            this.maxRadiusAircraftCanFlyAndGoHome = maxRadiusAircraftCanFlyAndGoHome2;
            return this;
        }

        public Builder batteryPercentageNeededToLandFromCurrentHeight(int batteryPercentageNeededToLandFromCurrentHeight2) {
            this.batteryPercentageNeededToLandFromCurrentHeight = batteryPercentageNeededToLandFromCurrentHeight2;
            return this;
        }

        public Builder smartRTHState(SmartRTHState smartRTHState2) {
            this.smartRTHState = smartRTHState2;
            return this;
        }

        public Builder smartRTHCountdown(int smartRTHCountdown2) {
            this.smartRTHCountdown = smartRTHCountdown2;
            return this;
        }

        public GoHomeAssessment build() {
            return new GoHomeAssessment(this);
        }
    }
}
