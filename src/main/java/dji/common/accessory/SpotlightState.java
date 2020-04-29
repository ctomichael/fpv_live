package dji.common.accessory;

public class SpotlightState {
    private final int brightness;
    private final float temperature;

    public interface Callback {
        void onUpdate(SpotlightState spotlightState);
    }

    private SpotlightState(Builder builder) {
        this.brightness = builder.brightness;
        this.temperature = builder.temperature;
    }

    public int getBrightness() {
        return this.brightness;
    }

    public float getTemperature() {
        return this.temperature;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        SpotlightState that = (SpotlightState) o;
        if (getBrightness() == that.getBrightness() && Float.compare(that.getTemperature(), getTemperature()) == 0) {
            return true;
        }
        return false;
    }

    public int hashCode() {
        return (getBrightness() * 31) + (getTemperature() != 0.0f ? Float.floatToIntBits(getTemperature()) : 0);
    }

    public static final class Builder {
        /* access modifiers changed from: private */
        public int brightness;
        /* access modifiers changed from: private */
        public float temperature;

        public Builder powerPercentage(int brightness2) {
            this.brightness = brightness2;
            return this;
        }

        public Builder temperature(float temperature2) {
            this.temperature = temperature2;
            return this;
        }

        public SpotlightState build() {
            return new SpotlightState(this);
        }
    }
}
