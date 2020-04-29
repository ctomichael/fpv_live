package dji.sdksharedlib.hardware.abstractions.battery.merge;

import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.config.P3.Ccode;
import dji.midware.data.model.P3.DataSmartBatteryGetMultBatteryInfo;

@EXClassNullAway
public interface M600GroupDataCallback {
    void onFailure(Ccode ccode);

    void onSuccess(DataSmartBatteryGetMultBatteryInfo dataSmartBatteryGetMultBatteryInfo);
}
