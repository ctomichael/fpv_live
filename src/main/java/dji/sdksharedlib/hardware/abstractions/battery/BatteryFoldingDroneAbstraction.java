package dji.sdksharedlib.hardware.abstractions.battery;

import dji.common.error.DJIBatteryError;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.config.P3.Ccode;
import dji.midware.data.model.P3.DataSmartBatteryGetPushCellVoltage;
import dji.midware.interfaces.DJIDataCallBack;
import dji.sdksharedlib.hardware.abstractions.DJISDKCacheHWAbstraction;
import dji.sdksharedlib.hardware.abstractions.Getter;
import dji.sdksharedlib.hardware.abstractions.battery.merge.M600MergeGetDynamicData;
import dji.sdksharedlib.hardware.abstractions.battery.merge.M600MergeGetSingleStaticData;
import dji.sdksharedlib.keycatalog.BatteryKeys;
import dji.sdksharedlib.store.DJISDKCacheStoreLayer;

@EXClassNullAway
public class BatteryFoldingDroneAbstraction extends BatterySmartAbstraction {
    private static String TAG = "DJIBatteryFoldingDroneAbstraction";

    public void init(String component, int index, DJISDKCacheStoreLayer storeLayer, DJISDKCacheHWAbstraction.OnValueChangeListener onValueChangeListener) {
        super.init(component, index, storeLayer, onValueChangeListener);
        this.numberOfCell = 3;
        this.isSmartBattery = true;
        this.mergeGetDynamicData = new M600MergeGetDynamicData(Integer.MAX_VALUE);
        this.mergeGetSingleStaticData = new M600MergeGetSingleStaticData(Integer.MAX_VALUE);
    }

    @Getter(BatteryKeys.CELL_VOLTAGES)
    public void getCellVoltages(final DJISDKCacheHWAbstraction.InnerCallback callback) {
        if (callback != null) {
            final DataSmartBatteryGetPushCellVoltage cellVoltage = DataSmartBatteryGetPushCellVoltage.getInstance();
            cellVoltage.setIndex(1).setRequestPush(false);
            cellVoltage.start(new DJIDataCallBack() {
                /* class dji.sdksharedlib.hardware.abstractions.battery.BatteryFoldingDroneAbstraction.AnonymousClass1 */

                public void onSuccess(Object model) {
                    int[] partVoltageValues = cellVoltage.getVoltages();
                    if (partVoltageValues != null) {
                        int count = 0;
                        int length = partVoltageValues.length;
                        int i = 0;
                        while (i < length && partVoltageValues[i] != 0) {
                            count++;
                            i++;
                        }
                        Integer[] cells = new Integer[count];
                        for (int i2 = 0; i2 < count; i2++) {
                            cells[i2] = Integer.valueOf(partVoltageValues[i2]);
                        }
                        callback.onSuccess(cells);
                        return;
                    }
                    callback.onFails(DJIBatteryError.getDJIError(Ccode.GET_PARAM_FAILED));
                }

                public void onFailure(Ccode ccode) {
                    if (callback != null) {
                        callback.onFails(DJIBatteryError.getDJIError(ccode));
                    }
                }
            });
        }
    }
}
