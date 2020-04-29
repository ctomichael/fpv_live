package dji.sdksharedlib.hardware.abstractions.remotecontroller;

import android.text.TextUtils;
import dji.common.error.DJIError;
import dji.common.util.CallbackUtils;
import dji.midware.data.config.P3.Ccode;
import dji.midware.data.config.P3.DeviceType;
import dji.midware.data.model.P3.DataCommonGetSNOfMavicRC;
import dji.midware.interfaces.DJIDataCallBack;
import dji.sdksharedlib.hardware.abstractions.DJISDKCacheHWAbstraction;

public class DJIRCPhantom4PSDRAbstraction extends DJIRCPhantom3Abstraction {
    public void getSerialNumber(final DJISDKCacheHWAbstraction.InnerCallback callback) {
        final DataCommonGetSNOfMavicRC rc = new DataCommonGetSNOfMavicRC();
        ((DataCommonGetSNOfMavicRC) rc.setDeviceType(DeviceType.DM368_G).setReceiverId(1, DataCommonGetSNOfMavicRC.class)).start(new DJIDataCallBack() {
            /* class dji.sdksharedlib.hardware.abstractions.remotecontroller.DJIRCPhantom4PSDRAbstraction.AnonymousClass1 */

            public void onSuccess(Object model) {
                if (TextUtils.isEmpty(rc.getSN())) {
                    CallbackUtils.onFailure(callback, DJIError.COMMON_EXECUTION_FAILED);
                } else {
                    CallbackUtils.onSuccess(callback, rc.getSN());
                }
            }

            public void onFailure(Ccode ccode) {
                CallbackUtils.onFailure(callback, ccode);
            }
        });
    }

    /* access modifiers changed from: protected */
    public String getComponentDisplayName() {
        return DJIRCAbstraction.DisplayNamePhantom4ProV2;
    }
}
