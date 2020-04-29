package dji.common.mission.intelligenthotpoint;

import android.graphics.RectF;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import dji.common.error.DJIError;
import dji.common.flightcontroller.flightassistant.IntelligentHotpointMissionMode;
import dji.common.model.LocationCoordinate2D;
import dji.fieldAnnotation.EXClassNullAway;

@EXClassNullAway
public class IntelligentHotpointMissionEvent {
    private final float angularVelocity;
    private final float currentAltitude;
    private final float currentRadius;
    @NonNull
    private final IntelligentHotpointMissionState currentState;
    @Nullable
    private final DJIError error;
    @Nullable
    private final LocationCoordinate2D hotpoint;
    private final float maxAngularVelocity;
    @Nullable
    private final IntelligentHotpointMissionMode missionMode;
    @Nullable
    private final IntelligentHotpointMissionState previousState;
    private final float targetAltitude;
    private final float targetRadius;
    @Nullable
    private final RectF targetRectF;

    public IntelligentHotpointMissionEvent(@NonNull Builder builder) {
        this.previousState = builder.previousState;
        this.currentState = builder.currentState;
        this.currentRadius = builder.currentRadius;
        this.targetRadius = builder.targetRadius;
        this.currentAltitude = builder.currentAltitude;
        this.targetAltitude = builder.targetAltitude;
        this.angularVelocity = builder.angularVelocity;
        this.maxAngularVelocity = builder.maxAngularVelocity;
        this.hotpoint = builder.hotpoint;
        this.targetRectF = builder.targetRectF;
        this.missionMode = builder.missionMode;
        this.error = builder.error;
    }

    public static final class Builder {
        /* access modifiers changed from: private */
        public float angularVelocity;
        /* access modifiers changed from: private */
        public float currentAltitude;
        /* access modifiers changed from: private */
        public float currentRadius;
        /* access modifiers changed from: private */
        @NonNull
        public IntelligentHotpointMissionState currentState;
        /* access modifiers changed from: private */
        @Nullable
        public DJIError error;
        /* access modifiers changed from: private */
        @Nullable
        public LocationCoordinate2D hotpoint;
        /* access modifiers changed from: private */
        public float maxAngularVelocity;
        /* access modifiers changed from: private */
        @Nullable
        public IntelligentHotpointMissionMode missionMode;
        /* access modifiers changed from: private */
        @Nullable
        public IntelligentHotpointMissionState previousState;
        /* access modifiers changed from: private */
        public float targetAltitude;
        /* access modifiers changed from: private */
        public float targetRadius;
        /* access modifiers changed from: private */
        @Nullable
        public RectF targetRectF;

        public Builder previousState(IntelligentHotpointMissionState previousState2) {
            this.previousState = previousState2;
            return this;
        }

        public Builder currentState(IntelligentHotpointMissionState currentState2) {
            this.currentState = currentState2;
            return this;
        }

        public Builder radius(float radius) {
            this.currentRadius = radius;
            return this;
        }

        public Builder altitude(float altitude) {
            this.currentAltitude = altitude;
            return this;
        }

        public Builder angularVelocity(float angularVelocity2) {
            this.angularVelocity = angularVelocity2;
            return this;
        }

        public Builder maxAngularVelocity(float maxAngularVelocity2) {
            this.maxAngularVelocity = maxAngularVelocity2;
            return this;
        }

        public Builder targetRadius(float targetRadius2) {
            this.targetRadius = targetRadius2;
            return this;
        }

        public Builder targetAltitude(float targetAltitude2) {
            this.targetAltitude = targetAltitude2;
            return this;
        }

        public Builder hotpoint(LocationCoordinate2D hotpoint2) {
            this.hotpoint = hotpoint2;
            return this;
        }

        public Builder targetRectF(RectF targetRectF2) {
            this.targetRectF = targetRectF2;
            return this;
        }

        public Builder missionMode(IntelligentHotpointMissionMode missionMode2) {
            this.missionMode = missionMode2;
            return this;
        }

        public Builder error(DJIError error2) {
            this.error = error2;
            return this;
        }

        public IntelligentHotpointMissionEvent build() {
            return new IntelligentHotpointMissionEvent(this);
        }
    }

    public boolean equals(Object o) {
        boolean z = true;
        if (this == o) {
            return true;
        }
        if (!(o instanceof IntelligentHotpointMissionEvent)) {
            return false;
        }
        IntelligentHotpointMissionEvent that = (IntelligentHotpointMissionEvent) o;
        if (Float.compare(that.getRadius(), getRadius()) != 0 || Float.compare(that.getTargetRadius(), getTargetRadius()) != 0 || Float.compare(that.getAltitude(), getAltitude()) != 0 || Float.compare(that.getTargetAltitude(), getTargetAltitude()) != 0 || Float.compare(that.getAngularVelocity(), getAngularVelocity()) != 0 || Float.compare(that.getMaxAngularVelocity(), getMaxAngularVelocity()) != 0) {
            return false;
        }
        if (getTargetRectF() != null) {
            if (!getTargetRectF().equals(that.getTargetRectF())) {
                return false;
            }
        } else if (that.getTargetRectF() != null) {
            return false;
        }
        if (getHotpoint() != null) {
            if (!getHotpoint().equals(that.getHotpoint())) {
                return false;
            }
        } else if (that.getHotpoint() != null) {
            return false;
        }
        if (getMissionMode() != null) {
            if (!getMissionMode().equals(that.getMissionMode())) {
                return false;
            }
        } else if (that.getMissionMode() != null) {
            return false;
        }
        if (getPreviousState() != null) {
            if (!getPreviousState().equals(that.getPreviousState())) {
                return false;
            }
        } else if (that.getPreviousState() != null) {
            return false;
        }
        if (getCurrentState() != null) {
            if (!getCurrentState().equals(that.getCurrentState())) {
                return false;
            }
        } else if (that.getCurrentState() != null) {
            return false;
        }
        if (getError() != null) {
            z = getError().equals(that.getError());
        } else if (that.getError() != null) {
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
        int i11 = 0;
        if (getPreviousState() != null) {
            result = getPreviousState().hashCode();
        } else {
            result = 0;
        }
        int i12 = result * 31;
        if (getCurrentState() != null) {
            i = getCurrentState().hashCode();
        } else {
            i = 0;
        }
        int i13 = (i12 + i) * 31;
        if (getRadius() != 0.0f) {
            i2 = Float.floatToIntBits(getRadius());
        } else {
            i2 = 0;
        }
        int i14 = (i13 + i2) * 31;
        if (getTargetRadius() != 0.0f) {
            i3 = Float.floatToIntBits(getTargetRadius());
        } else {
            i3 = 0;
        }
        int i15 = (i14 + i3) * 31;
        if (getAltitude() != 0.0f) {
            i4 = Float.floatToIntBits(getAltitude());
        } else {
            i4 = 0;
        }
        int i16 = (i15 + i4) * 31;
        if (getTargetAltitude() != 0.0f) {
            i5 = Float.floatToIntBits(getTargetAltitude());
        } else {
            i5 = 0;
        }
        int i17 = (i16 + i5) * 31;
        if (getAngularVelocity() != 0.0f) {
            i6 = Float.floatToIntBits(getAngularVelocity());
        } else {
            i6 = 0;
        }
        int i18 = (i17 + i6) * 31;
        if (getMaxAngularVelocity() != 0.0f) {
            i7 = Float.floatToIntBits(getMaxAngularVelocity());
        } else {
            i7 = 0;
        }
        int i19 = (i18 + i7) * 31;
        if (getHotpoint() != null) {
            i8 = getHotpoint().hashCode();
        } else {
            i8 = 0;
        }
        int i20 = (i19 + i8) * 31;
        if (getTargetRectF() != null) {
            i9 = getTargetRectF().hashCode();
        } else {
            i9 = 0;
        }
        int i21 = (i20 + i9) * 31;
        if (getMissionMode() != null) {
            i10 = getMissionMode().hashCode();
        } else {
            i10 = 0;
        }
        int i22 = (i21 + i10) * 31;
        if (getError() != null) {
            i11 = getError().hashCode();
        }
        return i22 + i11;
    }

    @Nullable
    public IntelligentHotpointMissionState getPreviousState() {
        return this.previousState;
    }

    @NonNull
    public IntelligentHotpointMissionState getCurrentState() {
        return this.currentState;
    }

    public float getRadius() {
        return this.currentRadius;
    }

    public float getTargetRadius() {
        return this.targetRadius;
    }

    public float getAltitude() {
        return this.currentAltitude;
    }

    public float getTargetAltitude() {
        return this.targetAltitude;
    }

    public float getAngularVelocity() {
        return this.angularVelocity;
    }

    public float getMaxAngularVelocity() {
        return this.maxAngularVelocity;
    }

    public LocationCoordinate2D getHotpoint() {
        return this.hotpoint;
    }

    public RectF getTargetRectF() {
        return this.targetRectF;
    }

    public IntelligentHotpointMissionMode getMissionMode() {
        return this.missionMode;
    }

    @Nullable
    public DJIError getError() {
        return this.error;
    }
}
