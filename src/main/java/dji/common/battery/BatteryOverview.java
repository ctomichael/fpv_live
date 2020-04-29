package dji.common.battery;

import dji.fieldAnnotation.EXClassNullAway;

@EXClassNullAway
public class BatteryOverview {
    private final int chargeRemainingInPercent;
    private final boolean connected;
    private final int index;

    public BatteryOverview(int index2, boolean connected2, int chargeRemainingInPercent2) {
        this.index = index2;
        this.connected = connected2;
        this.chargeRemainingInPercent = chargeRemainingInPercent2;
    }

    public int getIndex() {
        return this.index;
    }

    public boolean isConnected() {
        return this.connected;
    }

    public int getChargeRemainingInPercent() {
        return this.chargeRemainingInPercent;
    }

    public boolean equals(Object o) {
        boolean z = true;
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        BatteryOverview batteryOverview = (BatteryOverview) o;
        if (getIndex() != batteryOverview.getIndex() || isConnected() != batteryOverview.isConnected()) {
            return false;
        }
        if (getChargeRemainingInPercent() != batteryOverview.getChargeRemainingInPercent()) {
            z = false;
        }
        return z;
    }

    public int hashCode() {
        return ((((getIndex() + 31) * 31) + getChargeRemainingInPercent()) * 31) + (isConnected() ? 0 : 1);
    }
}
