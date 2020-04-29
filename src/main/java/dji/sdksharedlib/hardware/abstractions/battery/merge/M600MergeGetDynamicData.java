package dji.sdksharedlib.hardware.abstractions.battery.merge;

import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.config.P3.Ccode;
import dji.midware.data.model.P3.DataSmartBatteryGetPushDynamicData;
import dji.midware.interfaces.DJIDataCallBack;
import dji.sdksharedlib.hardware.extension.DJISDKMergeGet;
import java.util.List;

@EXClassNullAway
public class M600MergeGetDynamicData extends DJISDKMergeGet {
    private int batteryIndex = 0;
    protected DataSmartBatteryGetPushDynamicData dynamicData = null;

    public M600MergeGetDynamicData(int batteryIndex2) {
        this.batteryIndex = batteryIndex2;
        this.dynamicData = new DataSmartBatteryGetPushDynamicData();
        if (batteryIndex2 == Integer.MAX_VALUE) {
            this.dynamicData.setIndex(0).setRequestPush(false);
        } else {
            this.dynamicData.setIndex(batteryIndex2 + 1).setRequestPush(false);
        }
    }

    public void get(M600DynamicDataCallback callback) {
        addCommand(callback);
    }

    /* access modifiers changed from: protected */
    public void execute(final List<Object> list) {
        this.dynamicData.start(new DJIDataCallBack() {
            /* class dji.sdksharedlib.hardware.abstractions.battery.merge.M600MergeGetDynamicData.AnonymousClass1 */

            public void onSuccess(Object model) {
                for (M600DynamicDataCallback cb : list) {
                    cb.onSuccess(M600MergeGetDynamicData.this.dynamicData);
                }
            }

            public void onFailure(Ccode ccode) {
                for (M600DynamicDataCallback cb : list) {
                    cb.onFailure(ccode);
                }
            }
        });
    }
}
