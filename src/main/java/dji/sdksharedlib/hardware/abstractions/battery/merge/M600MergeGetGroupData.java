package dji.sdksharedlib.hardware.abstractions.battery.merge;

import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.config.P3.Ccode;
import dji.midware.data.model.P3.DataSmartBatteryGetMultBatteryInfo;
import dji.midware.interfaces.DJIDataCallBack;
import dji.sdksharedlib.hardware.extension.DJISDKMergeGet;
import java.util.List;

@EXClassNullAway
public class M600MergeGetGroupData extends DJISDKMergeGet {
    protected DataSmartBatteryGetMultBatteryInfo groupData;

    public M600MergeGetGroupData() {
        this.groupData = null;
        this.groupData = new DataSmartBatteryGetMultBatteryInfo();
    }

    public void get(M600GroupDataCallback callback) {
        addCommand(callback);
    }

    /* access modifiers changed from: protected */
    public void execute(final List<Object> list) {
        this.groupData.start(new DJIDataCallBack() {
            /* class dji.sdksharedlib.hardware.abstractions.battery.merge.M600MergeGetGroupData.AnonymousClass1 */

            public void onSuccess(Object model) {
                for (M600GroupDataCallback cb : list) {
                    cb.onSuccess(M600MergeGetGroupData.this.groupData);
                }
            }

            public void onFailure(Ccode ccode) {
                for (M600GroupDataCallback cb : list) {
                    cb.onFailure(ccode);
                }
            }
        });
    }
}
