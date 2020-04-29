package dji.pilot.battery.control;

import dji.fieldAnnotation.EXClassNullAway;
import dji.log.DJILogHelper;
import dji.midware.data.config.P3.Ccode;
import dji.midware.data.manager.P3.DJIFlycParamInfoManager;
import dji.midware.data.model.P3.DataFlycGetParams;
import dji.midware.interfaces.DJIDataCallBack;
import dji.sdksharedlib.hardware.abstractions.battery.NonSmartA3BatteryAbstraction;

@EXClassNullAway
public class NoSmartBatteryVoltManager {
    public static final String[] VOLTAGE_CONFIG = {"g_config.voltage.level_1_protect_0", "g_config.voltage.level_2_protect_0", NonSmartA3BatteryAbstraction.PARAM_NAME_CELL_NUM};
    private static NoSmartBatteryVoltManager mInstance = null;
    /* access modifiers changed from: private */
    public float mLowVoltageThreshold = -1.0f;
    /* access modifiers changed from: private */
    public float mSeriousLowVoltageThreshold = -1.0f;

    public static NoSmartBatteryVoltManager getInstance() {
        if (mInstance == null) {
            mInstance = new NoSmartBatteryVoltManager();
        }
        return mInstance;
    }

    private NoSmartBatteryVoltManager() {
        init();
    }

    public void init() {
        new DataFlycGetParams().setInfos(VOLTAGE_CONFIG).start(new DJIDataCallBack() {
            /* class dji.pilot.battery.control.NoSmartBatteryVoltManager.AnonymousClass1 */

            public void onSuccess(Object model) {
                int cells = DJIFlycParamInfoManager.read(NoSmartBatteryVoltManager.VOLTAGE_CONFIG[2]).value.intValue();
                float unused = NoSmartBatteryVoltManager.this.mLowVoltageThreshold = ((float) (DJIFlycParamInfoManager.read(NoSmartBatteryVoltManager.VOLTAGE_CONFIG[0]).value.intValue() * cells)) / 1000.0f;
                float unused2 = NoSmartBatteryVoltManager.this.mSeriousLowVoltageThreshold = ((float) (DJIFlycParamInfoManager.read(NoSmartBatteryVoltManager.VOLTAGE_CONFIG[1]).value.intValue() * cells)) / 1000.0f;
                DJILogHelper.getInstance().LOGD("pm820", "***1: " + NoSmartBatteryVoltManager.this.mLowVoltageThreshold + " 2: " + NoSmartBatteryVoltManager.this.mSeriousLowVoltageThreshold, false, true);
            }

            public void onFailure(Ccode ccode) {
                DJILogHelper.getInstance().LOGD("pm820", "***on nosmart battery voltage get fail!", false, true);
            }
        });
    }

    public void deinit() {
        this.mLowVoltageThreshold = -1.0f;
        this.mSeriousLowVoltageThreshold = -1.0f;
    }

    public float getLowVoltageThreshold() {
        return this.mLowVoltageThreshold;
    }

    public float getSeriousLowVoltageThreshold() {
        return this.mSeriousLowVoltageThreshold;
    }
}
