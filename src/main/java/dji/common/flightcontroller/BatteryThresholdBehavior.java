package dji.common.flightcontroller;

import dji.fieldAnnotation.EXClassNullAway;

@EXClassNullAway
public enum BatteryThresholdBehavior {
    FLY_NORMALLY(0),
    GO_HOME(1),
    LAND_IMMEDIATELY(2),
    UNKNOWN(255);
    
    private int data;

    private BatteryThresholdBehavior(int _data) {
        this.data = _data;
    }

    public int value() {
        return this.data;
    }

    /* access modifiers changed from: package-private */
    public boolean _equals(int b) {
        return this.data == b;
    }

    public static BatteryThresholdBehavior find(int b) {
        BatteryThresholdBehavior result = UNKNOWN;
        for (int i = 0; i < values().length; i++) {
            if (values()[i]._equals(b)) {
                return values()[i];
            }
        }
        return result;
    }
}
