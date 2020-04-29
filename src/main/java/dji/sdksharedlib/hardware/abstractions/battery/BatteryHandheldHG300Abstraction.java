package dji.sdksharedlib.hardware.abstractions.battery;

import dji.fieldAnnotation.EXClassNullAway;

@EXClassNullAway
public class BatteryHandheldHG300Abstraction extends BatteryHandheldAbstraction {
    /* access modifiers changed from: protected */
    public boolean isHG300Battery() {
        return true;
    }
}
