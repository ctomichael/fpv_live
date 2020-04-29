package dji.common.flightcontroller.adsb;

import android.support.annotation.NonNull;
import dji.fieldAnnotation.EXClassNullAway;

@EXClassNullAway
public class AirSenseAirplaneState {
    private final String code;
    private final int distance;
    private final float heading;
    private final AirSenseDirection relativeDirection;
    private final AirSenseWarningLevel warningLevel;

    public interface Callback {
        void onUpdate(@NonNull AirSenseAirplaneState[] airSenseAirplaneStateArr);
    }

    public AirSenseAirplaneState(Builder builder) {
        this.code = builder.code;
        this.heading = builder.heading;
        this.distance = builder.distance;
        this.warningLevel = builder.warningLevel;
        this.relativeDirection = builder.relativeDirection;
    }

    public String getCode() {
        return this.code;
    }

    public AirSenseWarningLevel getWarningLevel() {
        return this.warningLevel;
    }

    public AirSenseDirection getRelativeDirection() {
        return this.relativeDirection;
    }

    public float getHeading() {
        return this.heading;
    }

    public int getDistance() {
        return this.distance;
    }

    public boolean equals(Object o) {
        boolean z = true;
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        AirSenseAirplaneState that = (AirSenseAirplaneState) o;
        if (Float.compare(that.getHeading(), getHeading()) != 0 || getDistance() != that.getDistance()) {
            return false;
        }
        if (getCode() != null) {
            if (!getCode().equals(that.getCode())) {
                return false;
            }
        } else if (that.getCode() != null) {
            return false;
        }
        if (getWarningLevel() != that.getWarningLevel()) {
            return false;
        }
        if (getRelativeDirection() != that.getRelativeDirection()) {
            z = false;
        }
        return z;
    }

    public int hashCode() {
        int result;
        int i;
        int i2;
        int i3 = 0;
        if (getCode() != null) {
            result = getCode().hashCode();
        } else {
            result = 0;
        }
        int i4 = result * 31;
        if (getHeading() != 0.0f) {
            i = Float.floatToIntBits(getHeading());
        } else {
            i = 0;
        }
        int distance2 = (((i4 + i) * 31) + getDistance()) * 31;
        if (getWarningLevel() != null) {
            i2 = getWarningLevel().hashCode();
        } else {
            i2 = 0;
        }
        int i5 = (distance2 + i2) * 31;
        if (getRelativeDirection() != null) {
            i3 = getRelativeDirection().hashCode();
        }
        return i5 + i3;
    }

    public static class Builder {
        /* access modifiers changed from: private */
        public String code;
        /* access modifiers changed from: private */
        public int distance;
        /* access modifiers changed from: private */
        public float heading;
        /* access modifiers changed from: private */
        public AirSenseDirection relativeDirection;
        /* access modifiers changed from: private */
        public AirSenseWarningLevel warningLevel;

        public Builder code(String code2) {
            this.code = code2;
            return this;
        }

        public Builder heading(float heading2) {
            this.heading = heading2;
            return this;
        }

        public Builder distance(int distance2) {
            this.distance = distance2;
            return this;
        }

        public Builder warningLevel(AirSenseWarningLevel level) {
            this.warningLevel = level;
            return this;
        }

        public Builder relativeDirection(AirSenseDirection relativeDirection2) {
            this.relativeDirection = relativeDirection2;
            return this;
        }

        public AirSenseAirplaneState build() {
            return new AirSenseAirplaneState(this);
        }
    }
}
