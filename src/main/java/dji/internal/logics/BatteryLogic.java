package dji.internal.logics;

import dji.common.battery.ConnectionState;
import dji.common.bus.LogicEventBus;
import dji.common.flightcontroller.BatteryThresholdBehavior;
import dji.fieldAnnotation.EXClassNullAway;
import dji.internal.logics.Message;
import dji.sdksharedlib.extension.CacheHelper;
import dji.sdksharedlib.extension.KeyHelper;
import dji.sdksharedlib.keycatalog.BatteryKeys;
import dji.sdksharedlib.keycatalog.DJISDKCacheKey;
import dji.sdksharedlib.keycatalog.FlightControllerKeys;
import dji.sdksharedlib.listener.DJIParamAccessListener;
import dji.sdksharedlib.store.DJISDKCacheParamValue;

@EXClassNullAway
public class BatteryLogic implements DJIParamAccessListener {
    private static final String BATTERY_CONNECTION_ERROR = "Battery connection status is not normal.";
    private static final String BATTERY_LOW_CRITICAL = "Remaining battery life sufficient to land immediately.";
    private static final String BATTERY_LOW_WARINIG = "Remaining battery life sufficient to go home.";
    private ConnectionState batteryConnectionState;
    private int batteryPercentage;
    private BatteryThresholdBehavior batteryWarningLevel;
    private int goHomeBattery;
    private volatile Message previousMessage;

    public void init() {
        this.previousMessage = null;
        getValueOfAllListenedKeys();
    }

    private static final class HOLDER {
        /* access modifiers changed from: private */
        public static BatteryLogic instance = new BatteryLogic();

        private HOLDER() {
        }
    }

    private BatteryLogic() {
        this.batteryConnectionState = ConnectionState.NORMAL;
        CacheHelper.addBatteryListener(this, BatteryKeys.CHARGE_REMAINING_IN_PERCENT, BatteryKeys.CONNECTION_STATE);
        CacheHelper.addFlightControllerListener(this, FlightControllerKeys.BATTERY_THRESHOLD_BEHAVIOR, FlightControllerKeys.BATTERY_PERCENTAGE_NEEDED_TO_GO_HOME);
    }

    private void getValueOfAllListenedKeys() {
        Integer percentage = (Integer) CacheHelper.getValue(KeyHelper.getBatteryKey(BatteryKeys.CHARGE_REMAINING_IN_PERCENT));
        if (percentage != null) {
            this.batteryPercentage = percentage.intValue();
        }
        this.batteryConnectionState = (ConnectionState) CacheHelper.getValue(KeyHelper.getBatteryKey(BatteryKeys.CONNECTION_STATE));
        this.batteryWarningLevel = (BatteryThresholdBehavior) CacheHelper.getValue(KeyHelper.getFlightControllerKey(FlightControllerKeys.BATTERY_THRESHOLD_BEHAVIOR));
        Integer percentageToGoHome = (Integer) CacheHelper.getValue(KeyHelper.getFlightControllerKey(FlightControllerKeys.BATTERY_PERCENTAGE_NEEDED_TO_GO_HOME));
        if (percentageToGoHome != null) {
            this.goHomeBattery = percentageToGoHome.intValue();
        }
        updateBatteryStatus();
    }

    public void onValueChange(DJISDKCacheKey key, DJISDKCacheParamValue oldValue, DJISDKCacheParamValue newValue) {
        if (key != null && newValue != null && newValue.getData() != null) {
            if (key.getParamKey() == BatteryKeys.CHARGE_REMAINING_IN_PERCENT) {
                this.batteryPercentage = ((Integer) newValue.getData()).intValue();
            }
            if (key.getParamKey() == BatteryKeys.CONNECTION_STATE) {
                this.batteryConnectionState = (ConnectionState) newValue.getData();
            }
            if (key.getParamKey() == FlightControllerKeys.BATTERY_THRESHOLD_BEHAVIOR) {
                this.batteryWarningLevel = (BatteryThresholdBehavior) newValue.getData();
            }
            if (key.getParamKey() == FlightControllerKeys.BATTERY_PERCENTAGE_NEEDED_TO_GO_HOME) {
                this.goHomeBattery = ((Integer) newValue.getData()).intValue();
            }
            updateBatteryStatus();
        }
    }

    private void updateBatteryStatus() {
        Message.Type level;
        String description;
        Message.Type type = Message.Type.GOOD;
        String title = String.format("%1d%%", Integer.valueOf(this.batteryPercentage));
        if (this.previousMessage != null) {
            Message.Type level2 = this.previousMessage.getType();
            String description2 = this.previousMessage.getDescription();
        }
        if (this.batteryWarningLevel == BatteryThresholdBehavior.LAND_IMMEDIATELY) {
            level = Message.Type.ERROR;
            description = BATTERY_LOW_CRITICAL;
        } else if (this.batteryPercentage <= this.goHomeBattery || this.batteryWarningLevel == BatteryThresholdBehavior.GO_HOME) {
            level = Message.Type.ERROR;
            description = BATTERY_LOW_WARINIG;
        } else {
            level = Message.Type.GOOD;
            description = null;
        }
        if (!(this.batteryConnectionState == null || this.batteryConnectionState == ConnectionState.NORMAL)) {
            level = Message.Type.OFFLINE;
            description = BATTERY_CONNECTION_ERROR;
        }
        if (this.previousMessage == null || !this.previousMessage.equals(level, title, description)) {
            Message newMessage = new Message(level, title, description);
            LogicEventBus.getInstance().post(new BatteryEvent(newMessage));
            this.previousMessage = newMessage;
        }
    }

    public static BatteryLogic getInstance() {
        return HOLDER.instance;
    }

    public static final class BatteryEvent {
        private Message message;

        public BatteryEvent(Message message2) {
            this.message = message2;
        }

        public Message getMessage() {
            return this.message;
        }
    }
}
