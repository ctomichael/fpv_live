package dji.common.flightcontroller.adsb;

import android.support.annotation.NonNull;

public class AirSenseAvoidanceAction {
    private final AirSenseAvoidanceMode airSenseAvoidanceMode;
    private final float avoidanceCountDown;

    public interface Callback {
        void onUpdate(@NonNull AirSenseAvoidanceAction airSenseAvoidanceAction);
    }

    public AirSenseAvoidanceAction(float avoidanceCountDown2, AirSenseAvoidanceMode airSenseAvoidanceMode2) {
        this.avoidanceCountDown = avoidanceCountDown2;
        this.airSenseAvoidanceMode = airSenseAvoidanceMode2;
    }

    public float getAvoidanceCountDown() {
        return this.avoidanceCountDown;
    }

    public AirSenseAvoidanceMode getAirSenseAvoidanceMode() {
        return this.airSenseAvoidanceMode;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AirSenseAvoidanceAction)) {
            return false;
        }
        AirSenseAvoidanceAction that = (AirSenseAvoidanceAction) o;
        if (Float.compare(that.getAvoidanceCountDown(), getAvoidanceCountDown()) != 0) {
            return false;
        }
        if (getAirSenseAvoidanceMode() != that.getAirSenseAvoidanceMode()) {
            return false;
        }
        return true;
    }

    public int hashCode() {
        int result;
        int i = 0;
        if (getAvoidanceCountDown() != 0.0f) {
            result = Float.floatToIntBits(getAvoidanceCountDown());
        } else {
            result = 0;
        }
        int i2 = result * 31;
        if (getAirSenseAvoidanceMode() != null) {
            i = getAirSenseAvoidanceMode().hashCode();
        }
        return i2 + i;
    }
}
