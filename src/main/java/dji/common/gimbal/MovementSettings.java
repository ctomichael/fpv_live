package dji.common.gimbal;

import android.support.annotation.NonNull;
import dji.fieldAnnotation.EXClassNullAway;

@EXClassNullAway
public class MovementSettings {
    int pitchControllerSmoothingFactor;
    int pitchControllerSpeedCoefficient;
    int pitchSmoothTrackAcceleration;
    int pitchSmoothTrackDeadband;
    boolean pitchSmoothTrackEnable;
    int pitchSmoothTrackSpeed;
    MovementSettingsProfile profile;
    int yawControllerSmoothingFactor;
    int yawControllerSpeedCoefficient;
    int yawSmoothTrackAcceleration;
    int yawSmoothTrackDeadband;
    boolean yawSmoothTrackEnable;
    int yawSmoothTrackSpeed;

    public interface Callback {
        void onUpdate(@NonNull MovementSettings movementSettings);
    }

    public MovementSettings(MovementSettingsProfile profile2, boolean yawSmoothTrackEnable2, boolean pitchSmoothTrackEnable2, int yawSmoothTrackSpeed2, int pitchSmoothTrackSpeed2, int yawSmoothTrackDeadband2, int pitchSmoothTrackDeadband2, int yawSmoothTrackAcceleration2, int smoothTrackPitchAcceleration, int yawControllerSmoothingFactor2, int pitchControllerSmoothingFactor2, int yawControllerSpeedCoefficient2, int pitchControllerSpeedCoefficient2) {
        this.profile = profile2;
        this.yawSmoothTrackEnable = yawSmoothTrackEnable2;
        this.pitchSmoothTrackEnable = pitchSmoothTrackEnable2;
        this.yawSmoothTrackSpeed = yawSmoothTrackSpeed2;
        this.pitchSmoothTrackSpeed = pitchSmoothTrackSpeed2;
        this.yawSmoothTrackDeadband = yawSmoothTrackDeadband2;
        this.pitchSmoothTrackDeadband = pitchSmoothTrackDeadband2;
        this.yawSmoothTrackAcceleration = yawSmoothTrackAcceleration2;
        this.pitchSmoothTrackAcceleration = smoothTrackPitchAcceleration;
        this.yawControllerSmoothingFactor = yawControllerSmoothingFactor2;
        this.pitchControllerSmoothingFactor = pitchControllerSmoothingFactor2;
        this.yawControllerSpeedCoefficient = yawControllerSpeedCoefficient2;
        this.pitchControllerSpeedCoefficient = pitchControllerSpeedCoefficient2;
    }

    public MovementSettingsProfile getProfile() {
        return this.profile;
    }

    public boolean isYawSmoothTrackEnabled() {
        return this.yawSmoothTrackEnable;
    }

    public boolean isPitchSmoothTrackEnabled() {
        return this.pitchSmoothTrackEnable;
    }

    public int getYawSmoothTrackSpeed() {
        return this.yawSmoothTrackSpeed;
    }

    public int getPitchSmoothTrackSpeed() {
        return this.pitchSmoothTrackSpeed;
    }

    public int getYawSmoothTrackDeadband() {
        return this.yawSmoothTrackDeadband;
    }

    public int getPitchSmoothTrackDeadband() {
        return this.pitchSmoothTrackDeadband;
    }

    public int getYawSmoothTrackAcceleration() {
        return this.yawSmoothTrackAcceleration;
    }

    public int getPitchSmoothTrackAcceleration() {
        return this.pitchSmoothTrackAcceleration;
    }

    public int getYawControllerSmoothingFactor() {
        return this.yawControllerSmoothingFactor;
    }

    public int getPitchControllerSmoothingFactor() {
        return this.pitchControllerSmoothingFactor;
    }

    public int getYawControllerSpeedCoefficient() {
        return this.yawControllerSpeedCoefficient;
    }

    public int getPitchControllerSpeedCoefficient() {
        return this.pitchControllerSpeedCoefficient;
    }

    public boolean equals(Object o) {
        boolean z = true;
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        MovementSettings that = (MovementSettings) o;
        if (this.yawSmoothTrackEnable != that.yawSmoothTrackEnable || this.pitchSmoothTrackEnable != that.pitchSmoothTrackEnable || this.yawSmoothTrackSpeed != that.yawSmoothTrackSpeed || this.pitchSmoothTrackSpeed != that.pitchSmoothTrackSpeed || this.yawSmoothTrackDeadband != that.yawSmoothTrackDeadband || this.pitchSmoothTrackDeadband != that.pitchSmoothTrackDeadband || this.yawSmoothTrackAcceleration != that.yawSmoothTrackAcceleration || this.pitchSmoothTrackAcceleration != that.pitchSmoothTrackAcceleration || this.yawControllerSmoothingFactor != that.yawControllerSmoothingFactor || this.pitchControllerSmoothingFactor != that.pitchControllerSmoothingFactor || this.yawControllerSpeedCoefficient != that.yawControllerSpeedCoefficient || this.pitchControllerSpeedCoefficient != that.pitchControllerSpeedCoefficient) {
            return false;
        }
        if (this.profile != that.profile) {
            z = false;
        }
        return z;
    }

    public int hashCode() {
        int result;
        int i;
        int i2 = 1;
        if (this.profile != null) {
            result = this.profile.hashCode();
        } else {
            result = 0;
        }
        int i3 = result * 31;
        if (this.yawSmoothTrackEnable) {
            i = 1;
        } else {
            i = 0;
        }
        int i4 = (i3 + i) * 31;
        if (!this.pitchSmoothTrackEnable) {
            i2 = 0;
        }
        return ((((((((((((((((((((i4 + i2) * 31) + this.yawSmoothTrackSpeed) * 31) + this.pitchSmoothTrackSpeed) * 31) + this.yawSmoothTrackDeadband) * 31) + this.pitchSmoothTrackDeadband) * 31) + this.yawSmoothTrackAcceleration) * 31) + this.pitchSmoothTrackAcceleration) * 31) + this.yawControllerSmoothingFactor) * 31) + this.pitchControllerSmoothingFactor) * 31) + this.yawControllerSpeedCoefficient) * 31) + this.pitchControllerSpeedCoefficient;
    }
}
