package dji.sdksharedlib.keycatalog.group;

import dji.fieldAnnotation.EXClassNullAway;
import dji.sdksharedlib.hardware.abstractions.battery.BatteryBaseAggregationAbstraction;
import dji.sdksharedlib.hardware.abstractions.battery.BatteryFoldingDroneAbstraction;
import dji.sdksharedlib.hardware.abstractions.battery.BatteryInspire2Abstraction;
import dji.sdksharedlib.hardware.abstractions.battery.BatteryM600Abstraction;
import dji.sdksharedlib.hardware.abstractions.battery.BatteryMGAbstraction;
import dji.sdksharedlib.hardware.abstractions.battery.BatterySparkAbstraction;
import dji.sdksharedlib.keycatalog.extension.IAbstractionGroup;

@EXClassNullAway
public class SmartBatteryGroup implements IAbstractionGroup {
    public final String TAG = "SmartBatteryGroup";

    public Class[] getAbstractions() {
        return new Class[]{BatterySparkAbstraction.class, BatteryFoldingDroneAbstraction.class, BatteryInspire2Abstraction.class, BatteryM600Abstraction.class, BatteryMGAbstraction.class, BatteryBaseAggregationAbstraction.class};
    }
}
