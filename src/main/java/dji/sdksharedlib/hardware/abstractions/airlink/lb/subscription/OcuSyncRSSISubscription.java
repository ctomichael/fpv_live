package dji.sdksharedlib.hardware.abstractions.airlink.lb.subscription;

import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.config.P3.Ccode;
import dji.midware.data.model.P3.DataOsdSetSweepFrequency;
import dji.midware.interfaces.DJIDataCallBack;
import dji.sdksharedlib.hardware.extension.DJISDKCacheSubscription;

@EXClassNullAway
public class OcuSyncRSSISubscription extends DJISDKCacheSubscription {
    /* access modifiers changed from: protected */
    public void turnOnSubscription() {
        DataOsdSetSweepFrequency.getInstance().setType(false).start(this.callback);
    }

    /* access modifiers changed from: protected */
    public void turnOffSubscription() {
        DataOsdSetSweepFrequency.getInstance().setType(true).start(new DJIDataCallBack() {
            /* class dji.sdksharedlib.hardware.abstractions.airlink.lb.subscription.OcuSyncRSSISubscription.AnonymousClass1 */

            public void onSuccess(Object object) {
            }

            public void onFailure(Ccode ccode) {
            }
        });
    }
}
