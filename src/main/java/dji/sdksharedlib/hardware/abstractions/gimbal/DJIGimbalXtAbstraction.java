package dji.sdksharedlib.hardware.abstractions.gimbal;

import dji.common.error.DJIError;
import dji.midware.data.config.P3.Ccode;
import dji.midware.data.model.P3.DataGimbalNewResetAndSetMode;
import dji.midware.interfaces.DJIDataCallBack;
import dji.sdksharedlib.hardware.abstractions.DJISDKCacheHWAbstraction;

public class DJIGimbalXtAbstraction extends DJIGimbalInspire2Abstraction {
    public void resetGimbal(final DJISDKCacheHWAbstraction.InnerCallback callback) {
        ((DataGimbalNewResetAndSetMode) new DataGimbalNewResetAndSetMode().setReset(true).setReceiverId(getReceiverIdByIndex(), DataGimbalNewResetAndSetMode.class)).start(new DJIDataCallBack() {
            /* class dji.sdksharedlib.hardware.abstractions.gimbal.DJIGimbalXtAbstraction.AnonymousClass1 */

            public void onSuccess(Object model) {
                if (callback != null) {
                    callback.onSuccess(null);
                }
            }

            public void onFailure(Ccode ccode) {
                if (callback != null) {
                    callback.onFails(DJIError.getDJIError(ccode));
                }
            }
        });
    }
}
