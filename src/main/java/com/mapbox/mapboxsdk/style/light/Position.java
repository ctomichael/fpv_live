package com.mapbox.mapboxsdk.style.light;

import android.support.annotation.Keep;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

public class Position {
    @Keep
    private float azimuthalAngle;
    @Keep
    private float polarAngle;
    @Keep
    private float radialCoordinate;

    public Position(float radialCoordinate2, float azimuthalAngle2, float polarAngle2) {
        this.radialCoordinate = radialCoordinate2;
        this.azimuthalAngle = azimuthalAngle2;
        this.polarAngle = polarAngle2;
    }

    @Keep
    public static Position fromPosition(float radialCoordinate2, float azimuthalAngle2, float polarAngle2) {
        return new Position(radialCoordinate2, azimuthalAngle2, polarAngle2);
    }

    public boolean equals(@Nullable Object o) {
        boolean z = true;
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Position position = (Position) o;
        if (Float.compare(position.radialCoordinate, this.radialCoordinate) != 0 || Float.compare(position.azimuthalAngle, this.azimuthalAngle) != 0) {
            return false;
        }
        if (Float.compare(position.polarAngle, this.polarAngle) != 0) {
            z = false;
        }
        return z;
    }

    public int hashCode() {
        int result;
        int i;
        int i2 = 0;
        if (this.radialCoordinate != 0.0f) {
            result = Float.floatToIntBits(this.radialCoordinate);
        } else {
            result = 0;
        }
        int i3 = result * 31;
        if (this.azimuthalAngle != 0.0f) {
            i = Float.floatToIntBits(this.azimuthalAngle);
        } else {
            i = 0;
        }
        int i4 = (i3 + i) * 31;
        if (this.polarAngle != 0.0f) {
            i2 = Float.floatToIntBits(this.polarAngle);
        }
        return i4 + i2;
    }

    @NonNull
    public String toString() {
        return "Position{radialCoordinate=" + this.radialCoordinate + ", azimuthalAngle=" + this.azimuthalAngle + ", polarAngle=" + this.polarAngle + '}';
    }
}
