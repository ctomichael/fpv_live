package dji.sdksharedlib.hardware.abstractions.battery;

import dji.common.battery.BatteryCycleLimitLevel;
import dji.common.battery.BatteryCycleLimitState;
import dji.midware.data.model.P3.DataSmartBatteryGetPushDynamicData;
import dji.sdksharedlib.hardware.abstractions.DJISDKCacheHWAbstraction;
import dji.sdksharedlib.keycatalog.BatteryKeys;
import dji.sdksharedlib.store.DJISDKCacheStoreLayer;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class BatteryWM160Abstraction extends BatterySparkAbstraction {
    public void init(String component, int index, DJISDKCacheStoreLayer storeLayer, DJISDKCacheHWAbstraction.OnValueChangeListener onValueChangeListener) {
        super.init(component, index, storeLayer, onValueChangeListener);
    }

    public void syncPushDataFromMidware() {
        super.syncPushDataFromMidware();
        if (DataSmartBatteryGetPushDynamicData.getInstance().isGetted()) {
            onEvent3BackgroundThread(DataSmartBatteryGetPushDynamicData.getInstance());
        }
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEvent3BackgroundThread(DataSmartBatteryGetPushDynamicData data) {
        boolean z;
        boolean isInLoaderState;
        BatteryCycleLimitLevel cycleLimitLevel;
        if (data.isGetted()) {
            notifyValueChangeForKeyPath(data, BatteryKeys.BATTERY_DYNAMIC_INFO_FOR_FLIGHTRECORD);
            notifyValueChangeForKeyPath(Integer.valueOf(data.getFullCapacity()), BatteryKeys.FULL_CHARGE_CAPACITY);
            notifyValueChangeForKeyPath(getBatteryConnState(data.getStatus()), BatteryKeys.CONNECTION_STATE);
            if (data.getBatteryHeatState() > 0) {
                z = true;
            } else {
                z = false;
            }
            notifyValueChangeForKeyPath(Boolean.valueOf(z), BatteryKeys.IS_BATTERY_SELF_HEATING);
            notifyValueChangeForKeyPath(Integer.valueOf(data.getCurrent()), BatteryKeys.CURRENT);
            notifyValueChangeForKeyPath(Integer.valueOf(data.getRemainCapacity()), "ChargeRemaining");
            notifyValueChangeForKeyPath(Integer.valueOf(data.getVoltage()), BatteryKeys.VOLTAGE);
            notifyValueChangeForKeyPath(Integer.valueOf(data.getRelativeCapacityPercentage()), BatteryKeys.CHARGE_REMAINING_IN_PERCENT);
            float temperature = ((float) data.getTemperature()) / 10.0f;
            notifyValueChangeForKeyPath(Float.valueOf(temperature), BatteryKeys.TEMPERATURE);
            notifyValueChangeForKeyPath(getTempWarnLevel(temperature), BatteryKeys.LOW_TEMPERATURE_LEVEL);
            if ((16777216 & data.getStatus()) != 0) {
                isInLoaderState = true;
            } else {
                isInLoaderState = false;
            }
            notifyValueChangeForKeyPath(Boolean.valueOf(isInLoaderState), BatteryKeys.IS_BATTERY_IN_LOADER_STATE);
            int sohState = data.getSOHState();
            if (sohState == 0) {
                cycleLimitLevel = BatteryCycleLimitLevel.GOOD;
            } else if (sohState == 1) {
                cycleLimitLevel = BatteryCycleLimitLevel.YELLOW_WARNING;
            } else if (sohState == 2) {
                cycleLimitLevel = BatteryCycleLimitLevel.RED_ALERT;
            } else {
                cycleLimitLevel = BatteryCycleLimitLevel.UNKNOWN;
            }
            notifyValueChangeForKeyPath(new BatteryCycleLimitState(cycleLimitLevel, data.getCycleLimit()), BatteryKeys.CYCLE_LIMIT_STATE);
        }
    }
}
