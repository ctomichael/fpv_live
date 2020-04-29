package dji.common.battery;

import dji.fieldAnnotation.EXClassNullAway;

@EXClassNullAway
public class AggregationState {
    private final boolean anyBatteryDisconnected;
    private final BatteryOverview[] batteryOverviews;
    private final boolean cellDamaged;
    private final int chargeRemaining;
    private final int chargeRemainingInPercent;
    private final int current;
    private final boolean firmwareDifferenceDetected;
    private final int fullChargeCapacity;
    private final int highestTemperature;
    private final boolean lowCellVoltageDetected;
    private final int numberOfConnectedBatteries;
    private final int voltage;
    private final boolean voltageDifferenceDetected;

    public interface Callback {
        void onUpdate(AggregationState aggregationState);
    }

    private AggregationState(Builder builder) {
        this.batteryOverviews = builder.batteryOverviews;
        this.voltage = builder.voltage;
        this.current = builder.current;
        this.fullChargeCapacity = builder.fullChargeCapacity;
        this.chargeRemaining = builder.chargeRemaining;
        this.chargeRemainingInPercent = builder.chargeRemainingInPercent;
        this.numberOfConnectedBatteries = builder.numberOfConnectedBatteries;
        this.highestTemperature = builder.highestTemperature;
        this.anyBatteryDisconnected = builder.anyBatteryDisconnected;
        this.voltageDifferenceDetected = builder.voltageDifferenceDetected;
        this.lowCellVoltageDetected = builder.lowCellVoltageDetected;
        this.cellDamaged = builder.cellDamaged;
        this.firmwareDifferenceDetected = builder.firmwareDifferenceDetected;
    }

    public int getNumberOfConnectedBatteries() {
        return this.numberOfConnectedBatteries;
    }

    public BatteryOverview[] getBatteryOverviews() {
        return (BatteryOverview[]) this.batteryOverviews.clone();
    }

    public int getVoltage() {
        return this.voltage;
    }

    public int getCurrent() {
        return this.current;
    }

    public int getFullChargeCapacity() {
        return this.fullChargeCapacity;
    }

    public int getChargeRemaining() {
        return this.chargeRemaining;
    }

    public int getChargeRemainingInPercent() {
        return this.chargeRemainingInPercent;
    }

    public int getHighestTemperature() {
        return this.highestTemperature;
    }

    public boolean isAnyBatteryDisconnected() {
        return this.anyBatteryDisconnected;
    }

    public boolean isVoltageDifferenceDetected() {
        return this.voltageDifferenceDetected;
    }

    public boolean isLowCellVoltageDetected() {
        return this.lowCellVoltageDetected;
    }

    public boolean isCellDamaged() {
        return this.cellDamaged;
    }

    public boolean isFirmwareDifferenceDetected() {
        return this.firmwareDifferenceDetected;
    }

    public boolean equals(Object o) {
        boolean z = true;
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        AggregationState aggregationState = (AggregationState) o;
        if (getNumberOfConnectedBatteries() != aggregationState.getNumberOfConnectedBatteries() || getVoltage() != aggregationState.getVoltage() || getCurrent() != aggregationState.getCurrent() || getFullChargeCapacity() != aggregationState.getFullChargeCapacity() || getChargeRemaining() != aggregationState.getChargeRemaining() || getChargeRemainingInPercent() != aggregationState.getChargeRemainingInPercent() || getHighestTemperature() != aggregationState.getHighestTemperature() || isAnyBatteryDisconnected() != aggregationState.isAnyBatteryDisconnected() || isVoltageDifferenceDetected() != aggregationState.isVoltageDifferenceDetected() || isLowCellVoltageDetected() != aggregationState.isLowCellVoltageDetected() || isCellDamaged() != aggregationState.isCellDamaged() || isFirmwareDifferenceDetected() != aggregationState.isFirmwareDifferenceDetected()) {
            return false;
        }
        if (getBatteryOverviews() == null || aggregationState.getBatteryOverviews() == null) {
            if (!(getBatteryOverviews() == null && aggregationState.getBatteryOverviews() == null)) {
                z = false;
            }
            return z;
        } else if (getBatteryOverviews().length != aggregationState.getBatteryOverviews().length) {
            return false;
        } else {
            for (int i = 0; i < getBatteryOverviews().length; i++) {
                if (!getBatteryOverviews()[i].equals(aggregationState.getBatteryOverviews()[i])) {
                    return false;
                }
            }
            return true;
        }
    }

    public int hashCode() {
        int i;
        int i2;
        int i3;
        int i4;
        int i5 = 0;
        int numberOfConnectedBatteries2 = (((((((((((((getNumberOfConnectedBatteries() + 31) * 31) + getVoltage()) * 31) + getCurrent()) * 31) + getFullChargeCapacity()) * 31) + getChargeRemaining()) * 31) + getChargeRemainingInPercent()) * 31) + getHighestTemperature()) * 31;
        if (isAnyBatteryDisconnected()) {
            i = 0;
        } else {
            i = 1;
        }
        int i6 = (numberOfConnectedBatteries2 + i) * 31;
        if (isVoltageDifferenceDetected()) {
            i2 = 0;
        } else {
            i2 = 1;
        }
        int i7 = (i6 + i2) * 31;
        if (isLowCellVoltageDetected()) {
            i3 = 0;
        } else {
            i3 = 1;
        }
        int i8 = (i7 + i3) * 31;
        if (isCellDamaged()) {
            i4 = 0;
        } else {
            i4 = 1;
        }
        int i9 = (i8 + i4) * 31;
        if (!isFirmwareDifferenceDetected()) {
            i5 = 1;
        }
        int result = i9 + i5;
        if (getBatteryOverviews() != null) {
            for (int i10 = 0; i10 < getBatteryOverviews().length; i10++) {
                result += getBatteryOverviews()[i10].hashCode();
            }
        }
        return result;
    }

    public static final class Builder {
        /* access modifiers changed from: private */
        public boolean anyBatteryDisconnected;
        /* access modifiers changed from: private */
        public BatteryOverview[] batteryOverviews;
        /* access modifiers changed from: private */
        public boolean cellDamaged;
        /* access modifiers changed from: private */
        public int chargeRemaining;
        /* access modifiers changed from: private */
        public int chargeRemainingInPercent;
        /* access modifiers changed from: private */
        public int current;
        /* access modifiers changed from: private */
        public boolean firmwareDifferenceDetected;
        /* access modifiers changed from: private */
        public int fullChargeCapacity;
        /* access modifiers changed from: private */
        public int highestTemperature;
        /* access modifiers changed from: private */
        public boolean lowCellVoltageDetected;
        /* access modifiers changed from: private */
        public int numberOfConnectedBatteries;
        /* access modifiers changed from: private */
        public int voltage;
        /* access modifiers changed from: private */
        public boolean voltageDifferenceDetected;

        public Builder batteryOverviews(BatteryOverview[] batteryOverviews2) {
            this.batteryOverviews = batteryOverviews2;
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

        public Builder fullChargeCapacity(int fullChargeCapacity2) {
            this.fullChargeCapacity = fullChargeCapacity2;
            return this;
        }

        public Builder chargeRemaining(int chargeRemaining2) {
            this.chargeRemaining = chargeRemaining2;
            return this;
        }

        public Builder chargeRemainingInPercent(int chargeRemainingInPercent2) {
            this.chargeRemainingInPercent = chargeRemainingInPercent2;
            return this;
        }

        public Builder numberOfConnectedBatteries(int numberOfConnectedBatteries2) {
            this.numberOfConnectedBatteries = numberOfConnectedBatteries2;
            return this;
        }

        public Builder highestTemperature(int highestTemperature2) {
            this.highestTemperature = highestTemperature2;
            return this;
        }

        public Builder anyBatteryDisconnected(boolean anyBatteryDisconnected2) {
            this.anyBatteryDisconnected = anyBatteryDisconnected2;
            return this;
        }

        public Builder voltageDifferenceDetected(boolean voltageDifferenceDetected2) {
            this.voltageDifferenceDetected = voltageDifferenceDetected2;
            return this;
        }

        public Builder lowCellVoltageDetected(boolean lowCellVoltageDetected2) {
            this.lowCellVoltageDetected = lowCellVoltageDetected2;
            return this;
        }

        public Builder cellDamaged(boolean cellDamaged2) {
            this.cellDamaged = cellDamaged2;
            return this;
        }

        public Builder firmwareDifferenceDetected(boolean firmwareDifferenceDetected2) {
            this.firmwareDifferenceDetected = firmwareDifferenceDetected2;
            return this;
        }

        public AggregationState build() {
            return new AggregationState(this);
        }
    }
}
