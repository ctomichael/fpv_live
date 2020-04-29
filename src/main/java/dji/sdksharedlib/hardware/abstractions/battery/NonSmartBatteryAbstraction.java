package dji.sdksharedlib.hardware.abstractions.battery;

import dji.common.battery.BatteryCellVoltageLevel;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.model.P3.DataFlycGetPushSmartBattery;
import dji.midware.util.DJIEventBusUtil;
import dji.sdksharedlib.hardware.abstractions.DJISDKCacheHWAbstraction;
import dji.sdksharedlib.keycatalog.BatteryKeys;
import dji.sdksharedlib.store.DJISDKCacheStoreLayer;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

@EXClassNullAway
public class NonSmartBatteryAbstraction extends BatteryAbstraction {
    public NonSmartBatteryAbstraction() {
        this.isSmartBattery = false;
    }

    public void init(String component, int index, DJISDKCacheStoreLayer storeLayer, DJISDKCacheHWAbstraction.OnValueChangeListener onValueChangeListener) {
        super.init(component, index, storeLayer, onValueChangeListener);
        DJIEventBusUtil.register(this);
    }

    public void destroy() {
        DJIEventBusUtil.unRegister(this);
        super.destroy();
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEvent3BackgroundThread(DataFlycGetPushSmartBattery event) {
        super.onEvent3BackgroundThread(event);
        if ((event.getStatus() & 256) != 0) {
            notifyValueChangeForKeyPath(BatteryCellVoltageLevel.LEVEL_3, BatteryKeys.CELL_VOLTAGE_LEVEL);
        } else if ((event.getStatus() & 32) != 0) {
            notifyValueChangeForKeyPath(BatteryCellVoltageLevel.LEVEL_2, BatteryKeys.CELL_VOLTAGE_LEVEL);
        } else if ((event.getStatus() & 16) != 0) {
            notifyValueChangeForKeyPath(BatteryCellVoltageLevel.LEVEL_1, BatteryKeys.CELL_VOLTAGE_LEVEL);
        } else {
            notifyValueChangeForKeyPath(BatteryCellVoltageLevel.LEVEL_0, BatteryKeys.CELL_VOLTAGE_LEVEL);
        }
    }
}
