package dji.sdksharedlib.hardware.abstractions.battery.merge;

import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.config.P3.Ccode;
import dji.midware.data.model.P3.DataSmartBatteryGetStaticData;

@EXClassNullAway
public interface M600SingleStaticDataCallback {
    void onFailure(Ccode ccode);

    void onSuccess(DataSmartBatteryGetStaticData dataSmartBatteryGetStaticData);
}
