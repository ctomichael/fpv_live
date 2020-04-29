package dji.sdksharedlib.hardware.abstractions.airlink.lb.subscription;

import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.config.P3.Ccode;
import dji.midware.data.model.P3.DataWifiSetSweepFrequency;
import dji.midware.interfaces.DJIDataCallBack;
import dji.sdksharedlib.hardware.extension.DJISDKCacheSubscription;

@EXClassNullAway
public class WifiFoldingDroneRSSISubscription extends DJISDKCacheSubscription {
    /* access modifiers changed from: protected */
    public void turnOnSubscription() {
        DataWifiSetSweepFrequency.getInstance().setIsOpen(true).start(this.callback);
    }

    /* access modifiers changed from: protected */
    public void turnOffSubscription() {
        DataWifiSetSweepFrequency.getInstance().setIsOpen(false).start(new DJIDataCallBack() {
            /* class dji.sdksharedlib.hardware.abstractions.airlink.lb.subscription.WifiFoldingDroneRSSISubscription.AnonymousClass1 */

            public void onSuccess(Object object) {
            }

            public void onFailure(Ccode ccode) {
            }
        });
    }
}
