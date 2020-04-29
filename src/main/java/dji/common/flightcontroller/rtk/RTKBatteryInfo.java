package dji.common.flightcontroller.rtk;

public class RTKBatteryInfo {
    private int current = 0;
    private int fullCapacity = 0;
    private int index = 0;
    private int relativeCapacityPercentage = 0;
    private int remainCapacity = 0;
    private int temperature = 0;
    private int voltage = 0;

    public RTKBatteryInfo(Builder builder) {
        this.index = builder.index;
        this.voltage = builder.voltage;
        this.current = builder.current;
        this.fullCapacity = builder.fullCapacity;
        this.remainCapacity = builder.remainCapacity;
        this.temperature = builder.temperature;
        this.relativeCapacityPercentage = builder.relativeCapacityPercentage;
    }

    public int getIndex() {
        return this.index;
    }

    public int getVoltage() {
        return this.voltage;
    }

    public int getCurrent() {
        return this.current;
    }

    public int getFullCapacity() {
        return this.fullCapacity;
    }

    public int getRemainCapacity() {
        return this.remainCapacity;
    }

    public int getTemperature() {
        return this.temperature;
    }

    public int getRelativeCapacityPercentage() {
        return this.relativeCapacityPercentage;
    }

    public static class Builder {
        /* access modifiers changed from: private */
        public int current = 0;
        /* access modifiers changed from: private */
        public int fullCapacity = 0;
        /* access modifiers changed from: private */
        public int index = 0;
        /* access modifiers changed from: private */
        public int relativeCapacityPercentage = 0;
        /* access modifiers changed from: private */
        public int remainCapacity = 0;
        /* access modifiers changed from: private */
        public int temperature = 0;
        /* access modifiers changed from: private */
        public int voltage = 0;

        public Builder index(int index2) {
            this.index = index2;
            return this;
        }

        public Builder voltage(int voltage2) {
            this.voltage = voltage2;
            return this;
        }

        public Builder current(int current2) {
            this.current = current2;
            return this;
        }

        public Builder fullCapacity(int fullCapacity2) {
            this.fullCapacity = fullCapacity2;
            return this;
        }

        public Builder remainCapacity(int remainCapacity2) {
            this.remainCapacity = remainCapacity2;
            return this;
        }

        public Builder temperature(int temperature2) {
            this.temperature = temperature2;
            return this;
        }

        public Builder relativeCapacityPercentage(int relativeCapacityPercentage2) {
            this.relativeCapacityPercentage = relativeCapacityPercentage2;
            return this;
        }

        public RTKBatteryInfo build() {
            return new RTKBatteryInfo(this);
        }
    }
}
