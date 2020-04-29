package dji.common.flightcontroller.rtk;

public class BaseStationBatteryState {
    private final int capacityPercent;
    private final int current;
    private final int temperature;
    private final int voltage;

    public interface Callback {
        void onUpdateBaseStationBatteryState(BaseStationBatteryState baseStationBatteryState);
    }

    public int getVoltage() {
        return this.voltage;
    }

    public int getCurrent() {
        return this.current;
    }

    public int getTemperature() {
        return this.temperature;
    }

    public int getCapacityPercent() {
        return this.capacityPercent;
    }

    private BaseStationBatteryState(Builder builder) {
        this.voltage = builder.voltage;
        this.current = builder.current;
        this.temperature = builder.temperature;
        this.capacityPercent = builder.capacityPercent;
    }

    public static final class Builder {
        /* access modifiers changed from: private */
        public int capacityPercent;
        /* access modifiers changed from: private */
        public int current;
        /* access modifiers changed from: private */
        public int temperature;
        /* access modifiers changed from: private */
        public int voltage;

        public Builder voltage(int voltage2) {
            this.voltage = voltage2;
            return this;
        }

        public Builder current(int current2) {
            this.current = current2;
            return this;
        }

        public Builder temperature(int temperature2) {
            this.temperature = temperature2;
            return this;
        }

        public Builder capacityPercent(int capacityPercent2) {
            this.capacityPercent = capacityPercent2;
            return this;
        }

        public BaseStationBatteryState build() {
            return new BaseStationBatteryState(this);
        }
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        BaseStationBatteryState that = (BaseStationBatteryState) o;
        if (this.voltage == that.voltage && this.current == that.current && this.temperature == that.temperature && this.capacityPercent == that.capacityPercent) {
            return true;
        }
        return false;
    }

    public int hashCode() {
        return (((((((getVoltage() * 31) + getCurrent()) * 31) + getTemperature()) * 31) + getCapacityPercent()) * 31) + getCurrent();
    }
}
