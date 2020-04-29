package dji.sdksharedlib.keycatalog;

import dji.common.battery.AggregationState;
import dji.common.battery.BatteryCellVoltageLevel;
import dji.common.battery.BatteryCycleLimitState;
import dji.common.battery.BatteryHwType;
import dji.common.battery.BatteryOverview;
import dji.common.battery.BatterySOPTemperatureLevel;
import dji.common.battery.ConnectionState;
import dji.common.battery.LowVoltageBehavior;
import dji.common.battery.PairingState;
import dji.common.battery.SelfHeatingState;
import dji.common.battery.WarningRecord;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.model.P3.DataSmartBatteryGetPushCellVoltage;
import dji.midware.data.model.P3.DataSmartBatteryGetPushDynamicData;
import dji.midware.data.model.P3.DataSmartBatteryGetStaticData;
import dji.sdksharedlib.hardware.abstractions.DJISDKCacheUpdateType;
import dji.sdksharedlib.hardware.abstractions.battery.BatteryBaseAggregationAbstraction;
import dji.sdksharedlib.hardware.abstractions.battery.BatteryHandheldAbstraction;
import dji.sdksharedlib.hardware.abstractions.battery.BatteryHandheldHG300Abstraction;
import dji.sdksharedlib.hardware.abstractions.battery.BatteryInspire2Abstraction;
import dji.sdksharedlib.hardware.abstractions.battery.BatterySmartAbstraction;
import dji.sdksharedlib.hardware.abstractions.battery.BatterySparkAbstraction;
import dji.sdksharedlib.hardware.abstractions.battery.BatteryWM160Abstraction;
import dji.sdksharedlib.hardware.abstractions.battery.BatteryWM240Abstraction;
import dji.sdksharedlib.hardware.abstractions.battery.BatteryWM245Abstraction;
import dji.sdksharedlib.hardware.abstractions.battery.NonSmartA3BatteryAbstraction;
import dji.sdksharedlib.hardware.abstractions.battery.NonSmartBatteryAbstraction;
import dji.sdksharedlib.keycatalog.extension.ComplexKey;
import dji.sdksharedlib.keycatalog.extension.InternalKey;
import dji.sdksharedlib.keycatalog.extension.Key;
import dji.sdksharedlib.keycatalog.group.SmartBatteryGroup;

@EXClassNullAway
public class BatteryKeys extends DJISDKCacheKeys {
    @Key(accessType = 1, includedAbstractions = {BatteryBaseAggregationAbstraction.class}, type = AggregationState.class)
    public static final String AGGREGATION_STATE = "AggregationState";
    @InternalKey
    @Key(accessType = 1, type = DataSmartBatteryGetPushCellVoltage.class)
    public static final String BATTERY_CELL_VOLTAGE_FOR_FLIGHTRECORD = "BatteryCellVoltageForFR";
    @InternalKey
    @Key(accessType = 1, type = DataSmartBatteryGetPushDynamicData.class)
    public static final String BATTERY_DYNAMIC_INFO_FOR_FLIGHTRECORD = "BatteryDynamicInfoForFR";
    @InternalKey
    @Key(accessType = 1, includedAbstractions = {BatteryWM160Abstraction.class}, type = BatteryHwType.class, updateType = DJISDKCacheUpdateType.USER_DRIVEN)
    public static final String BATTERY_HW_TYPE = "BatteryHwType";
    @InternalKey
    @Key(accessType = 1, type = DataSmartBatteryGetStaticData.class, updateType = DJISDKCacheUpdateType.DYNAMIC)
    public static final String BATTERY_STATIC_INFO_FOR_FLIGHTRECORD = "BatteryStaticInfoForFR";
    @Key(accessType = 1, excludedAbstractions = {NonSmartA3BatteryAbstraction.class}, type = Integer[].class)
    public static final String CELL_VOLTAGES = "CellVoltages";
    @Key(accessType = 4, includedAbstractions = {NonSmartA3BatteryAbstraction.class}, type = BatteryCellVoltageLevel.class)
    public static final String CELL_VOLTAGE_LEVEL = "CellVoltageLevel";
    @ComplexKey({@Key(accessType = 4, excludedAbstractions = {NonSmartA3BatteryAbstraction.class}, includedAbstractions = {BatteryWM160Abstraction.class}, type = Integer.class), @Key(accessType = 1, includedAbstractions = {SmartBatteryGroup.class}, type = Integer.class)})
    public static final String CHARGE_REMAINING = "ChargeRemaining";
    @ComplexKey({@Key(accessType = 4, excludedAbstractions = {NonSmartA3BatteryAbstraction.class}, includedAbstractions = {BatteryWM160Abstraction.class}, type = Integer.class), @Key(accessType = 1, includedAbstractions = {SmartBatteryGroup.class}, type = Integer.class)})
    public static final String CHARGE_REMAINING_IN_PERCENT = "ChargeRemainingInPercent";
    public static final String COMPONENT_KEY = "Battery";
    @ComplexKey({@Key(accessType = 4, includedAbstractions = {BatteryWM160Abstraction.class}, type = ConnectionState.class), @Key(accessType = 1, includedAbstractions = {BatterySmartAbstraction.class}, type = ConnectionState.class)})
    public static final String CONNECTION_STATE = "ConnectionState";
    @ComplexKey({@Key(accessType = 4, excludedAbstractions = {NonSmartA3BatteryAbstraction.class}, includedAbstractions = {BatteryWM160Abstraction.class}, type = Integer.class), @Key(accessType = 1, includedAbstractions = {SmartBatteryGroup.class}, type = Integer.class)})
    public static final String CURRENT = "Current";
    @InternalKey
    @Key(accessType = 4, excludedAbstractions = {NonSmartA3BatteryAbstraction.class}, includedAbstractions = {BatteryWM160Abstraction.class}, type = BatteryCycleLimitState.class)
    public static final String CYCLE_LIMIT_STATE = "CycleLimitState";
    @Key(accessType = 1, includedAbstractions = {BatterySmartAbstraction.class}, type = Integer.class, updateType = DJISDKCacheUpdateType.USER_DRIVEN)
    public static final String DESIGN_CAPACITY = "DesignCapacity";
    @ComplexKey({@Key(accessType = 4, excludedAbstractions = {NonSmartA3BatteryAbstraction.class}, includedAbstractions = {BatteryWM160Abstraction.class}, type = Integer.class), @Key(accessType = 1, includedAbstractions = {SmartBatteryGroup.class}, type = Integer.class)})
    public static final String FULL_CHARGE_CAPACITY = "FullChargeCapacity";
    @Key(accessType = 1, includedAbstractions = {BatteryBaseAggregationAbstraction.class}, type = Integer.class, updateType = DJISDKCacheUpdateType.DYNAMIC)
    public static final String HIGHEST_TEMPERATURE = "HighestTemperature";
    @Key(accessType = 1, excludedAbstractions = {BatteryHandheldAbstraction.class, BatteryHandheldHG300Abstraction.class, BatterySparkAbstraction.class, BatteryWM240Abstraction.class, BatteryWM245Abstraction.class}, type = WarningRecord[].class, updateType = DJISDKCacheUpdateType.USER_DRIVEN)
    public static final String HISTORICAL_WARNING_RECORDS = "HistoricalWarningRecords";
    @InternalKey
    @Key(accessType = 1, excludedAbstractions = {NonSmartBatteryAbstraction.class}, type = String.class, updateType = DJISDKCacheUpdateType.USER_DRIVEN)
    public static final String INTERNAL_SERIAL_NUMBER = "InternalSerialNumber";
    @Key(accessType = 1, includedAbstractions = {BatteryBaseAggregationAbstraction.class}, type = Boolean.class, updateType = DJISDKCacheUpdateType.DYNAMIC)
    public static final String IS_ANY_BATTERY_DISCONNECTED = "IsAnyBatteryDisconnected";
    @Key(accessType = 4, includedAbstractions = {BatteryWM160Abstraction.class}, type = Boolean.class)
    public static final String IS_BATTERY_IN_LOADER_STATE = "IsBatteryInLoaderState";
    @InternalKey
    @ComplexKey({@Key(accessType = 4, includedAbstractions = {BatteryWM160Abstraction.class}, type = ConnectionState.class), @Key(accessType = 1, includedAbstractions = {SmartBatteryGroup.class}, type = ConnectionState.class)})
    public static final String IS_BATTERY_SELF_HEATING = "IsBatterySelfHeating";
    @Key(accessType = 4, includedAbstractions = {BatteryHandheldHG300Abstraction.class}, type = Boolean.class, updateType = DJISDKCacheUpdateType.DYNAMIC)
    public static final String IS_BEING_CHARGED = "IsBeingCharged";
    @Key(accessType = 1, includedAbstractions = {BatterySmartAbstraction.class}, type = Boolean.class)
    public static final String IS_BIG_BATTERY = "IsBigBattery";
    @Key(accessType = 1, includedAbstractions = {BatteryBaseAggregationAbstraction.class}, type = Boolean.class, updateType = DJISDKCacheUpdateType.DYNAMIC)
    public static final String IS_CELL_DAMAGED = "IsCellDamaged";
    @Key(accessType = 1, includedAbstractions = {BatteryBaseAggregationAbstraction.class}, type = Boolean.class, updateType = DJISDKCacheUpdateType.DYNAMIC)
    public static final String IS_FIRMWARE_DIFFERENCE_DETECTED = "IsFirmwareDifferenceDetected";
    @Key(accessType = 4, type = Boolean.class)
    public static final String IS_IN_SINGLE_BATTERY_MODE = "isInSingleBatteryMode";
    @Key(accessType = 1, includedAbstractions = {BatteryBaseAggregationAbstraction.class}, type = Boolean.class, updateType = DJISDKCacheUpdateType.DYNAMIC)
    public static final String IS_LOW_CELL_VOLTAGE_DETECTED = "IsLowCellVoltageDetected";
    @Key(accessType = 1, excludedAbstractions = {NonSmartA3BatteryAbstraction.class}, protectDuration = 0, type = Boolean.class, updateType = DJISDKCacheUpdateType.USER_DRIVEN)
    public static final String IS_SMART_BATTERY = "isSmartBattery";
    @Key(accessType = 1, includedAbstractions = {BatteryBaseAggregationAbstraction.class}, type = Boolean.class, updateType = DJISDKCacheUpdateType.DYNAMIC)
    public static final String IS_VOLTAGE_DIFFERENCE_DETECTED = "IsVoltageDifferenceDetected";
    @Key(accessType = 1, excludedAbstractions = {NonSmartA3BatteryAbstraction.class}, type = WarningRecord.class)
    public static final String LATEST_WARNING_RECORD = "LatestWarningRecord";
    @Key(accessType = 3, includedAbstractions = {NonSmartA3BatteryAbstraction.class}, type = LowVoltageBehavior.class, updateType = DJISDKCacheUpdateType.USER_DRIVEN)
    public static final String LEVEL_1_CELL_VOLTAGE_BEHAVIOR = "Level1CellVoltageBehavior";
    @Key(accessType = 3, includedAbstractions = {NonSmartA3BatteryAbstraction.class}, type = Integer.class)
    public static final String LEVEL_1_CELL_VOLTAGE_THRESHOLD = "Level1CellVoltageThreshold";
    @Key(accessType = 3, includedAbstractions = {NonSmartA3BatteryAbstraction.class}, type = LowVoltageBehavior.class, updateType = DJISDKCacheUpdateType.USER_DRIVEN)
    public static final String LEVEL_2_CELL_VOLTAGE_BEHAVIOR = "Level2CellVoltageBehavior";
    @Key(accessType = 3, includedAbstractions = {NonSmartA3BatteryAbstraction.class}, type = Integer.class)
    public static final String LEVEL_2_CELL_VOLTAGE_THRESHOLD = "Level2CellVoltageThreshold";
    @ComplexKey({@Key(accessType = 4, excludedAbstractions = {NonSmartA3BatteryAbstraction.class}, type = Integer.class), @Key(accessType = 1, includedAbstractions = {SmartBatteryGroup.class}, type = Integer.class)})
    public static final String LIFETIME_REMAINING = "LifetimeRemaining";
    @InternalKey
    @ComplexKey({@Key(accessType = 4, excludedAbstractions = {NonSmartA3BatteryAbstraction.class}, includedAbstractions = {BatteryWM160Abstraction.class}, type = BatterySOPTemperatureLevel.class), @Key(accessType = 1, includedAbstractions = {SmartBatteryGroup.class}, type = BatterySOPTemperatureLevel.class)})
    public static final String LOW_TEMPERATURE_LEVEL = "LowTemperatureLevel";
    @ComplexKey({@Key(accessType = 1, type = Integer.class, updateType = DJISDKCacheUpdateType.USER_DRIVEN), @Key(accessType = 3, includedAbstractions = {NonSmartA3BatteryAbstraction.class}, type = Integer.class, updateType = DJISDKCacheUpdateType.USER_DRIVEN)})
    public static final String NUMBER_OF_CELLS = "NumberOfCells";
    @Key(accessType = 1, includedAbstractions = {BatteryBaseAggregationAbstraction.class}, type = Integer.class, updateType = DJISDKCacheUpdateType.DYNAMIC)
    public static final String NUMBER_OF_CONNECTED_BATTERIES = "NumberOfConnectedBatteries";
    @ComplexKey({@Key(accessType = 4, excludedAbstractions = {NonSmartA3BatteryAbstraction.class}, type = Integer.class), @Key(accessType = 1, includedAbstractions = {SmartBatteryGroup.class}, type = Integer.class)})
    public static final String NUMBER_OF_DISCHARGES = "NumberOfDischarges";
    @Key(accessType = 1, includedAbstractions = {BatteryBaseAggregationAbstraction.class}, type = BatteryOverview[].class, updateType = DJISDKCacheUpdateType.DYNAMIC)
    public static final String OVERVIEWS = "Overviews";
    @InternalKey
    @Key(accessType = 1, includedAbstractions = {BatteryInspire2Abstraction.class}, type = PairingState.class, updateType = DJISDKCacheUpdateType.USER_DRIVEN)
    public static final String PAIRING_STATE = "PairingState";
    @InternalKey
    @Key(accessType = 8, includedAbstractions = {BatteryInspire2Abstraction.class}, updateType = DJISDKCacheUpdateType.USER_DRIVEN)
    public static final String PAIR_BATTERIES = "PairBatteries";
    @Key(accessType = 1, excludedAbstractions = {NonSmartA3BatteryAbstraction.class}, type = Integer.class)
    public static final String PRODUCTION_DATE = "ProductionDate";
    @Key(accessType = 3, excludedAbstractions = {NonSmartA3BatteryAbstraction.class, BatterySparkAbstraction.class, BatteryWM240Abstraction.class, BatteryWM245Abstraction.class, BatteryWM160Abstraction.class}, type = Integer.class)
    public static final String SELF_DISCHARGE_IN_DAYS = "SelfDischargeInDays";
    @Key(accessType = 1, includedAbstractions = {BatteryWM245Abstraction.class}, type = SelfHeatingState.class)
    public static final String SELF_HEATING_STATE = "SelfHeatingState";
    @ComplexKey({@Key(accessType = 4, excludedAbstractions = {NonSmartA3BatteryAbstraction.class}, includedAbstractions = {BatteryWM160Abstraction.class}, type = Float.class), @Key(accessType = 1, includedAbstractions = {SmartBatteryGroup.class}, type = Float.class)})
    public static final String TEMPERATURE = "Temperature";
    @ComplexKey({@Key(accessType = 4, includedAbstractions = {BatteryWM160Abstraction.class}, type = Integer.class), @Key(accessType = 1, includedAbstractions = {SmartBatteryGroup.class}, type = Integer.class)})
    public static final String VOLTAGE = "Voltage";

    public BatteryKeys(String name) {
        super(name);
    }

    /* access modifiers changed from: protected */
    public String getDefaultAbstractionName() {
        return COMPONENT_KEY;
    }
}
