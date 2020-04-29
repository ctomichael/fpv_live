package dji.common.flightcontroller;

import dji.fieldAnnotation.EXClassNullAway;

@EXClassNullAway
public class VisionControlState {
    private final boolean ascentLimitedByObstacle;
    private final boolean avoidingActiveObstacleCollision;
    private final boolean isAdvancedPilotAssistanceSystemActive;
    private final boolean isBraking;
    private final boolean landingPrecisely;
    private final VisionLandingProtectionState landingProtectionState;

    public interface Callback {
        void onUpdate(VisionControlState visionControlState);
    }

    public VisionControlState(Builder builder) {
        this.landingProtectionState = builder.landingProtectionState;
        this.landingPrecisely = builder.isPerformingPrecisionLanding;
        this.isBraking = builder.isBraking;
        this.ascentLimitedByObstacle = builder.isAscentLimitedByObstacle;
        this.avoidingActiveObstacleCollision = builder.isAvoidingActiveObstacleCollision;
        this.isAdvancedPilotAssistanceSystemActive = builder.isAdvancedPilotAssistanceSystemActive;
    }

    public boolean isAscentLimitedByObstacle() {
        return this.ascentLimitedByObstacle;
    }

    public boolean isAvoidingActiveObstacleCollision() {
        return this.avoidingActiveObstacleCollision;
    }

    public VisionLandingProtectionState landingProtectionState() {
        return this.landingProtectionState;
    }

    public boolean isPerformingPrecisionLanding() {
        return this.landingPrecisely;
    }

    public boolean isBraking() {
        return this.isBraking;
    }

    public boolean isAdvancedPilotAssistanceSystemActive() {
        return this.isAdvancedPilotAssistanceSystemActive;
    }

    public boolean equals(Object o) {
        boolean z = true;
        if (this == o) {
            return true;
        }
        if (!(o instanceof VisionControlState)) {
            return false;
        }
        VisionControlState that = (VisionControlState) o;
        if (this.landingPrecisely != that.landingPrecisely || isBraking() != that.isBraking() || isAscentLimitedByObstacle() != that.isAscentLimitedByObstacle() || isAvoidingActiveObstacleCollision() != that.isAvoidingActiveObstacleCollision() || isAdvancedPilotAssistanceSystemActive() != that.isAdvancedPilotAssistanceSystemActive()) {
            return false;
        }
        if (this.landingProtectionState != that.landingProtectionState) {
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
        int i5 = 1;
        if (this.landingProtectionState != null) {
            result = this.landingProtectionState.hashCode();
        } else {
            result = 0;
        }
        int i6 = result * 31;
        if (this.landingPrecisely) {
            i = 1;
        } else {
            i = 0;
        }
        int i7 = (i6 + i) * 31;
        if (isBraking()) {
            i2 = 1;
        } else {
            i2 = 0;
        }
        int i8 = (i7 + i2) * 31;
        if (isAscentLimitedByObstacle()) {
            i3 = 1;
        } else {
            i3 = 0;
        }
        int i9 = (i8 + i3) * 31;
        if (isAvoidingActiveObstacleCollision()) {
            i4 = 1;
        } else {
            i4 = 0;
        }
        int i10 = (i9 + i4) * 31;
        if (!isAdvancedPilotAssistanceSystemActive()) {
            i5 = 0;
        }
        return i10 + i5;
    }

    public static final class Builder {
        /* access modifiers changed from: private */
        public boolean isAdvancedPilotAssistanceSystemActive;
        /* access modifiers changed from: private */
        public boolean isAscentLimitedByObstacle;
        /* access modifiers changed from: private */
        public boolean isAvoidingActiveObstacleCollision;
        /* access modifiers changed from: private */
        public boolean isBraking;
        /* access modifiers changed from: private */
        public boolean isPerformingPrecisionLanding;
        /* access modifiers changed from: private */
        public VisionLandingProtectionState landingProtectionState;

        public Builder landingProtectionState(VisionLandingProtectionState landingProtectionState2) {
            this.landingProtectionState = landingProtectionState2;
            return this;
        }

        public Builder isPerformingPrecisionLanding(boolean landingPrecisely) {
            this.isPerformingPrecisionLanding = landingPrecisely;
            return this;
        }

        public Builder isBraking(boolean braking) {
            this.isBraking = braking;
            return this;
        }

        public Builder isAscentLimitedByObstacle(boolean ascentLimitedByObstacle) {
            this.isAscentLimitedByObstacle = ascentLimitedByObstacle;
            return this;
        }

        public Builder isAvoidingActiveObstacleCollision(boolean avoidingActiveObstacleCollision) {
            this.isAvoidingActiveObstacleCollision = avoidingActiveObstacleCollision;
            return this;
        }

        public Builder isAdvancedPilotAssistanceSystemActive(boolean advancedPilotAssistanceSystemActive) {
            this.isAdvancedPilotAssistanceSystemActive = advancedPilotAssistanceSystemActive;
            return this;
        }

        public VisionControlState build() {
            return new VisionControlState(this);
        }
    }
}
