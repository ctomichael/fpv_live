package dji.sdksharedlib.hardware.abstractions.gimbal;

import dji.common.error.DJIError;
import dji.common.gimbal.CapabilityKey;
import dji.common.util.DJIParamCapability;
import dji.common.util.DJIParamMinMaxCapability;
import dji.log.DJILog;
import dji.midware.data.config.P3.Ccode;
import dji.midware.data.manager.P3.DJIGimbalParamInfoManager;
import dji.midware.data.model.P3.DataGimbalResetUserParams;
import dji.midware.data.model.P3.DataGimbalSetUserParams;
import dji.midware.interfaces.DJIDataCallBack;
import dji.sdksharedlib.hardware.abstractions.Action;
import dji.sdksharedlib.hardware.abstractions.DJISDKCacheHWAbstraction;
import dji.sdksharedlib.hardware.abstractions.Setter;
import dji.sdksharedlib.hardware.abstractions.gimbal.DJIGimbalAbstraction;
import dji.sdksharedlib.keycatalog.GimbalKeys;
import java.util.HashMap;
import java.util.Map;

public class DJIGimbalWM160Abstraction extends DJIGimbalWM230Abstraction {
    private static final String TAG = "DJIGimbalWM160Abstraction";

    public void initGimbalCapability() {
        super.initGimbalCapability();
        addMinMaxToCapability(CapabilityKey.ADJUST_PITCH, -90, 0);
    }

    public void didInitAbstraction() {
        getPitchRangeExtensionEnabled(new DJISDKCacheHWAbstraction.InnerCallback() {
            /* class dji.sdksharedlib.hardware.abstractions.gimbal.DJIGimbalWM160Abstraction.AnonymousClass1 */

            public void onSuccess(Object o) {
                if (o.equals(Boolean.TRUE)) {
                    DJIGimbalWM160Abstraction.this.gimbalCapability.put(CapabilityKey.ADJUST_PITCH, new DJIParamMinMaxCapability(true, -90, 20));
                }
                DJIGimbalWM160Abstraction.this.saveGimbalCapabilityToStore();
            }

            public void onFails(DJIError error) {
                DJIGimbalWM160Abstraction.this.saveGimbalCapabilityToStore();
            }
        });
    }

    @Setter(GimbalKeys.PITCH_RANGE_EXTENSION_ENABLED)
    public void setPitchRangeExtensionEnabled(final boolean enable, final DJISDKCacheHWAbstraction.InnerCallback callback) {
        int i;
        DataGimbalSetUserParams dataGimbalSetUserParams = (DataGimbalSetUserParams) DataGimbalSetUserParams.getInstance().setReceiverId(getReceiverIdByIndex(), DataGimbalSetUserParams.class);
        String cmdString = DJIGimbalAbstraction.SettingParamCmd.PITCH_EXP.getCmdString();
        if (enable) {
            i = 1;
        } else {
            i = 0;
        }
        dataGimbalSetUserParams.setInfo(cmdString, Integer.valueOf(i)).start(new DJIDataCallBack() {
            /* class dji.sdksharedlib.hardware.abstractions.gimbal.DJIGimbalWM160Abstraction.AnonymousClass2 */

            public void onSuccess(Object model) {
            }

            public void onFailure(Ccode ccode) {
            }
        });
        new Thread(new Runnable() {
            /* class dji.sdksharedlib.hardware.abstractions.gimbal.DJIGimbalWM160Abstraction.AnonymousClass3 */

            public void run() {
                int i = 0;
                while (i < 15) {
                    try {
                        Thread.sleep(200);
                        if (enable == (DJIGimbalParamInfoManager.read(DJIGimbalAbstraction.SettingParamCmd.PITCH_EXP.getCmdString()).value.intValue() == 1)) {
                            callback.onSuccess(Boolean.valueOf(enable));
                            Map<CapabilityKey, DJIParamCapability> cloneGimbalCapability = new HashMap<>(DJIGimbalWM160Abstraction.this.gimbalCapability);
                            if (enable) {
                                cloneGimbalCapability.put(CapabilityKey.ADJUST_PITCH, new DJIParamMinMaxCapability(true, -90, 20));
                            } else {
                                cloneGimbalCapability.put(CapabilityKey.ADJUST_PITCH, new DJIParamMinMaxCapability(true, -90, 0));
                            }
                            DJIGimbalWM160Abstraction.this.notifyValueChangeForKeyPath(cloneGimbalCapability, DJIGimbalWM160Abstraction.this.convertKeyToPath(GimbalKeys.CAPABILITIES));
                            DJIGimbalWM160Abstraction.this.gimbalCapability = cloneGimbalCapability;
                            return;
                        } else if (i == 14) {
                            callback.onFails(DJIError.COMMON_TIMEOUT);
                            return;
                        } else {
                            continue;
                            i++;
                        }
                    } catch (InterruptedException e) {
                        DJILog.e(DJIGimbalWM160Abstraction.TAG, DJILog.exceptionToString(e), new Object[0]);
                    }
                }
            }
        }, "gimbalAbs").start();
    }

    @Action(GimbalKeys.RESTORE_FACTORY_SETTINGS)
    public void loadFactorySettings(final DJISDKCacheHWAbstraction.InnerCallback callback) {
        ((DataGimbalResetUserParams) DataGimbalResetUserParams.getInstance().setReceiverId(getReceiverIdByIndex(), DataGimbalResetUserParams.class)).start(new DJIDataCallBack() {
            /* class dji.sdksharedlib.hardware.abstractions.gimbal.DJIGimbalWM160Abstraction.AnonymousClass4 */

            public void onSuccess(Object model) {
                callback.onSuccess(model);
                Map<CapabilityKey, DJIParamCapability> cloneGimbalCapability = new HashMap<>(DJIGimbalWM160Abstraction.this.gimbalCapability);
                cloneGimbalCapability.put(CapabilityKey.ADJUST_PITCH, new DJIParamMinMaxCapability(true, -90, 0));
                DJIGimbalWM160Abstraction.this.notifyValueChangeForKeyPath(cloneGimbalCapability, DJIGimbalWM160Abstraction.this.convertKeyToPath(GimbalKeys.CAPABILITIES));
                DJIGimbalWM160Abstraction.this.gimbalCapability = cloneGimbalCapability;
            }

            public void onFailure(Ccode ccode) {
                callback.onFails(DJIError.getDJIError(ccode));
            }
        });
    }
}
