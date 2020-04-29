package dji.common.battery;

import dji.fieldAnnotation.EXClassNullAway;

@EXClassNullAway
public class BatteryState {
    private final BatteryCellVoltageLevel cellVoltageLevel;
    private final int chargeRemaining;
    private final int chargeRemainingInPercent;
    private final ConnectionState connectionState;
    private final int current;
    private final int designCapacity;
    private final int fullChargeCapacity;
    private final boolean isBeingCharged;
    private final boolean isBigBattery;
    private final boolean isSingleBattery;
    private final int lifetimeRemaining;
    private final int numberOfDischarges;
    private final SelfHeatingState selfHeatingState;
    private final float temperature;
    private final int voltage;

    public interface Callback {
        void onUpdate(BatteryState batteryState);
    }

    private BatteryState(Builder builder) {
        this.cellVoltageLevel = builder.cellVoltageLevel;
        this.fullChargeCapacity = builder.fullChargeCapacity;
        this.designCapacity = builder.designCapacity;
        this.chargeRemaining = builder.chargeRemaining;
        this.voltage = builder.voltage;
        this.current = builder.current;
        this.lifetimeRemaining = builder.lifetimeRemaining;
        this.chargeRemainingInPercent = builder.chargeRemainingInPercent;
        this.temperature = builder.temperature;
        this.numberOfDischarges = builder.numberOfDischarges;
        this.isBeingCharged = builder.isBeingCharged;
        this.isSingleBattery = builder.isSingleBattery;
        this.isBigBattery = builder.isBigBattery;
        this.connectionState = builder.connectionState;
        this.selfHeatingState = builder.selfHeatingState;
    }

    public int getFullChargeCapacity() {
        return this.fullChargeCapacity;
    }

    public int getDesignCapacity() {
        return this.designCapacity;
    }

    public int getChargeRemaining() {
        return this.chargeRemaining;
    }

    public int getVoltage() {
        return this.voltage;
    }

    public int getCurrent() {
        return this.current;
    }

    public int getLifetimeRemaining() {
        return this.lifetimeRemaining;
    }

    public int getChargeRemainingInPercent() {
        return this.chargeRemainingInPercent;
    }

    public float getTemperature() {
        return this.temperature;
    }

    public int getNumberOfDischarges() {
        return this.numberOfDischarges;
    }

    public boolean isBeingCharged() {
        return this.isBeingCharged;
    }

    public boolean isInSingleBatteryMode() {
        return this.isSingleBattery;
    }

    public boolean isBigBattery() {
        return this.isBigBattery;
    }

    public BatteryCellVoltageLevel getCellVoltageLevel() {
        return this.cellVoltageLevel;
    }

    public ConnectionState getConnectionState() {
        return this.connectionState;
    }

    public SelfHeatingState getSelfHeatingState() {
        return this.selfHeatingState;
    }

    public boolean equals(Object o) {
        boolean z = true;
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        BatteryState batteryState = (BatteryState) o;
        if (getFullChargeCapacity() != batteryState.getFullChargeCapacity() || getDesignCapacity() != batteryState.getDesignCapacity() || getChargeRemaining() != batteryState.getChargeRemaining() || getVoltage() != batteryState.getVoltage() || getCurrent() != batteryState.getCurrent() || getLifetimeRemaining() != batteryState.getLifetimeRemaining() || getChargeRemainingInPercent() != batteryState.getChargeRemainingInPercent() || Float.compare(batteryState.getTemperature(), getTemperature()) != 0 || getNumberOfDischarges() != batteryState.getNumberOfDischarges() || isBeingCharged() != batteryState.isBeingCharged() || this.isSingleBattery != batteryState.isSingleBattery || isBigBattery() != batteryState.isBigBattery() || getCellVoltageLevel() != batteryState.getCellVoltageLevel() || getConnectionState() != batteryState.getConnectionState()) {
            return false;
        }
        if (this.selfHeatingState != batteryState.selfHeatingState) {
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
        int i6 = 0;
        if (getCellVoltageLevel() != null) {
            result = getCellVoltageLevel().hashCode();
        } else {
            result = 0;
        }
        int fullChargeCapacity2 = ((((((((((((((result * 31) + getFullChargeCapacity()) * 31) + getDesignCapacity()) * 31) + getChargeRemaining()) * 31) + getVoltage()) * 31) + getCurrent()) * 31) + getLifetimeRemaining()) * 31) + getChargeRemainingInPercent()) * 31;
        if (getTemperature() != 0.0f) {
            i = Float.floatToIntBits(getTemperature());
        } else {
            i = 0;
        }
        int numberOfDischarges2 = (((fullChargeCapacity2 + i) * 31) + getNumberOfDischarges()) * 31;
        if (isBeingCharged()) {
            i2 = 1;
        } else {
            i2 = 0;
        }
        int i7 = (numberOfDischarges2 + i2) * 31;
        if (this.isSingleBattery) {
            i3 = 1;
        } else {
            i3 = 0;
        }
        int i8 = (i7 + i3) * 31;
        if (!isBigBattery()) {
            i5 = 0;
        }
        int i9 = (i8 + i5) * 31;
        if (getConnectionState() != null) {
            i4 = getConnectionState().hashCode();
        } else {
            i4 = 0;
        }
        int i10 = (i9 + i4) * 31;
        if (this.selfHeatingState != null) {
            i6 = this.selfHeatingState.hashCode();
        }
        return i10 + i6;
    }

    public static final class Builder {
        /* access modifiers changed from: private */
        public BatteryCellVoltageLevel cellVoltageLevel;
        /* access modifiers changed from: private */
        public int chargeRemaining;
        /* access modifiers changed from: private */
        public int chargeRemainingInPercent;
        /* access modifiers changed from: private */
        public ConnectionState connectionState;
        /* access modifiers changed from: private */
        public int current;
        /* access modifiers changed from: private */
        public int designCapacity;
        /* access modifiers changed from: private */
        public int fullChargeCapacity;
        /* access modifiers changed from: private */
        public boolean isBeingCharged;
        /* access modifiers changed from: private */
        public boolean isBigBattery;
        /* access modifiers changed from: private */
        public boolean isSingleBattery;
        /* access modifiers changed from: private */
        public int lifetimeRemaining;
        /* access modifiers changed from: private */
        public int numberOfDischarges;
        /* access modifiers changed from: private */
        public SelfHeatingState selfHeatingState;
        /* access modifiers changed from: private */
        public float temperature;
        /* access modifiers changed from: private */
        public int voltage;

        public Builder cellVoltageLevel(BatteryCellVoltageLevel cellVoltageLevel2) {
            this.cellVoltageLevel = cellVoltageLevel2;
            return this;
        }

        public Builder fullChargeCapacity(int fullChargeCapacity2) {
            this.fullChargeCapacity = fullChargeCapacity2;
            return this;
        }

        public Builder designCapacity(int capacity) {
            this.designCapacity = capacity;
            return this;
        }

        public Builder chargeRemaining(int chargeRemaining2) {
            this.chargeRemaining = chargeRemaining2;
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

        public Builder lifetimeRemaining(int lifetimeRemaining2) {
            this.lifetimeRemaining = lifetimeRemaining2;
            return this;
        }

        public Builder chargeRemainingInPercent(int chargeRemainingInPercent2) {
            this.chargeRemainingInPercent = chargeRemainingInPercent2;
            return this;
        }

        public Builder temperature(float temperature2) {
            this.temperature = temperature2;
            return this;
        }

        public Builder numberOfDischarges(int numberOfDischarges2) {
            this.numberOfDischarges = numberOfDischarges2;
            return this;
        }

        public Builder isBeingCharged(boolean isBeingCharged2) {
            this.isBeingCharged = isBeingCharged2;
            return this;
        }

        public Builder isSingleBattery(boolean isSingleBattery2) {
            this.isSingleBattery = isSingleBattery2;
            return this;
        }

        public Builder isBigBattery(boolean isBig) {
            this.isBigBattery = isBig;
            return this;
        }

        public Builder connectionState(ConnectionState connectionState2) {
            this.connectionState = connectionState2;
            return this;
        }

        public Builder selfHeatingState(SelfHeatingState selfHeatingState2) {
            this.selfHeatingState = selfHeatingState2;
            return this;
        }

        public BatteryState build() {
            return new BatteryState(this);
        }
    }
}
