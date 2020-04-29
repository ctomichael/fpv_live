package dji.sdksharedlib.hardware.abstractions.battery.merge;

import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.config.P3.Ccode;
import dji.midware.data.model.P3.DataSmartBatteryGetStaticData;
import dji.midware.interfaces.DJIDataCallBack;
import dji.sdksharedlib.hardware.extension.DJISDKMergeGet;
import java.util.List;

@EXClassNullAway
public class M600MergeGetSingleStaticData extends DJISDKMergeGet {
    private int batteryIndex = 0;
    protected DataSmartBatteryGetStaticData staticData = null;

    public M600MergeGetSingleStaticData(int batteryIndex2) {
        this.batteryIndex = batteryIndex2;
        this.staticData = new DataSmartBatteryGetStaticData();
        this.staticData.setIndex(batteryIndex2 + 1);
    }

    public void get(M600SingleStaticDataCallback callback) {
        addCommand(callback);
    }

    /* access modifiers changed from: protected */
    public void execute(final List<Object> list) {
        this.staticData.start(new DJIDataCallBack() {
            /* class dji.sdksharedlib.hardware.abstractions.battery.merge.M600MergeGetSingleStaticData.AnonymousClass1 */

            public void onSuccess(Object model) {
                for (M600SingleStaticDataCallback cb : list) {
                    cb.onSuccess(M600MergeGetSingleStaticData.this.staticData);
                }
            }

            public void onFailure(Ccode ccode) {
                for (M600SingleStaticDataCallback cb : list) {
                    cb.onFailure(ccode);
                }
            }
        });
    }
}
