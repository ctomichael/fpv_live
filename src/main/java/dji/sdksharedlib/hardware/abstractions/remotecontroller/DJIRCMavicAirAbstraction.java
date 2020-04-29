package dji.sdksharedlib.hardware.abstractions.remotecontroller;

import android.text.TextUtils;
import dji.common.error.DJIError;
import dji.common.util.CallbackUtils;
import dji.midware.data.config.P3.Ccode;
import dji.midware.data.model.P3.DataCommonGetSNOfMavicAirRC;
import dji.midware.interfaces.DJIDataCallBack;
import dji.sdksharedlib.hardware.abstractions.DJISDKCacheHWAbstraction;

public class DJIRCMavicAirAbstraction extends DJIRCSparkAbstraction {
    private static final String TAG = "MavicAirRCAbstraction";

    public DJIRCMavicAirAbstraction() {
        this.hardwareState.getFunctionButton().setPresent(true);
        this.hardwareState.getC2Button().setPresent(true);
        this.hardwareState.getGoHomeButton().setPresent(true);
        this.hardwareState.getPauseButton().setPresent(true);
        this.hardwareState.getRecordButton().setPresent(true);
        this.hardwareState.getRightWheel().setPresent(false);
        this.hardwareState.getShutterButton().setPresent(true);
    }

    /* access modifiers changed from: protected */
    public String getComponentDisplayName() {
        return DJIRCAbstraction.DisplayNameMavicAir;
    }

    /* access modifiers changed from: protected */
    public boolean isNewProgressOfActivation() {
        return true;
    }

    public void getSerialNumber(final DJISDKCacheHWAbstraction.InnerCallback callback) {
        final DataCommonGetSNOfMavicAirRC getSNOfMavicAirRC = new DataCommonGetSNOfMavicAirRC();
        getSNOfMavicAirRC.start(new DJIDataCallBack() {
            /* class dji.sdksharedlib.hardware.abstractions.remotecontroller.DJIRCMavicAirAbstraction.AnonymousClass1 */

            public void onSuccess(Object model) {
                if (TextUtils.isEmpty(getSNOfMavicAirRC.getSN())) {
                    CallbackUtils.onFailure(callback, DJIError.COMMON_EXECUTION_FAILED);
                } else {
                    CallbackUtils.onSuccess(callback, getSNOfMavicAirRC.getSN());
                }
            }

            public void onFailure(Ccode ccode) {
                CallbackUtils.onFailure(callback, ccode);
            }
        });
    }
}
