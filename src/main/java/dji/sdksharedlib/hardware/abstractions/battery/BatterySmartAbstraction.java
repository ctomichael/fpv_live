package dji.sdksharedlib.hardware.abstractions.battery;

import android.text.TextUtils;
import dji.common.battery.BatteryHwType;
import dji.common.battery.BatterySOPTemperatureLevel;
import dji.common.battery.BatteryUtils;
import dji.common.battery.ConnectionState;
import dji.common.battery.SelfHeatingState;
import dji.common.battery.WarningRecord;
import dji.common.error.DJIBatteryError;
import dji.common.error.DJIError;
import dji.common.util.CallbackUtils;
import dji.fieldAnnotation.EXClassNullAway;
import dji.log.DJILog;
import dji.midware.data.config.P3.Ccode;
import dji.midware.data.config.P3.DeviceType;
import dji.midware.data.manager.P3.DJIProductManager;
import dji.midware.data.model.P3.DataCenterGetPushBatteryCommon;
import dji.midware.data.model.P3.DataCommonGetVersion;
import dji.midware.data.model.P3.DataSmartBatteryGetBarCode;
import dji.midware.data.model.P3.DataSmartBatteryGetHistory;
import dji.midware.data.model.P3.DataSmartBatteryGetPushCellVoltage;
import dji.midware.data.model.P3.DataSmartBatteryGetPushDynamicData;
import dji.midware.data.model.P3.DataSmartBatteryGetSetSelfDischargeDays;
import dji.midware.data.model.P3.DataSmartBatteryGetStaticData;
import dji.midware.interfaces.DJIDataCallBack;
import dji.sdksharedlib.extension.CacheHelper;
import dji.sdksharedlib.hardware.abstractions.DJISDKCacheHWAbstraction;
import dji.sdksharedlib.hardware.abstractions.Getter;
import dji.sdksharedlib.hardware.abstractions.Setter;
import dji.sdksharedlib.hardware.abstractions.battery.merge.M600DynamicDataCallback;
import dji.sdksharedlib.hardware.abstractions.battery.merge.M600MergeGetDynamicData;
import dji.sdksharedlib.hardware.abstractions.battery.merge.M600MergeGetSingleStaticData;
import dji.sdksharedlib.hardware.abstractions.battery.merge.M600SingleStaticDataCallback;
import dji.sdksharedlib.keycatalog.BatteryKeys;
import dji.sdksharedlib.keycatalog.DJISDKCacheKey;
import dji.sdksharedlib.keycatalog.DJISDKCacheKeys;
import dji.sdksharedlib.listener.DJIParamAccessListener;
import dji.sdksharedlib.store.DJISDKCacheParamValue;
import dji.sdksharedlib.store.DJISDKCacheStoreLayer;
import java.util.ArrayList;
import java.util.List;

@EXClassNullAway
public class BatterySmartAbstraction extends BatteryAbstraction {
    private static final int BIT_START_INDEX = 59;
    private static final long VALID_BIT_VALUE = 3;
    public final String TAG = "DJISDKCacheSmartBatteryAbstraction";
    /* access modifiers changed from: private */
    public final DJIParamAccessListener designCapacityKeyListener = new DJIParamAccessListener() {
        /* class dji.sdksharedlib.hardware.abstractions.battery.BatterySmartAbstraction.AnonymousClass1 */

        public void onValueChange(DJISDKCacheKey key, DJISDKCacheParamValue oldValue, DJISDKCacheParamValue newValue) {
            if (newValue != null && (newValue.getData() instanceof Integer)) {
                CacheHelper.removeListener(BatterySmartAbstraction.this.designCapacityKeyListener);
                DJILog.d("DJISDKCacheSmartBatteryAbstraction", "get design capacity " + newValue.getData() + "remove listener", new Object[0]);
            }
        }
    };
    protected M600MergeGetDynamicData mergeGetDynamicData;
    protected M600MergeGetSingleStaticData mergeGetSingleStaticData;

    public void init(String component, int index, DJISDKCacheStoreLayer storeLayer, DJISDKCacheHWAbstraction.OnValueChangeListener onValueChangeListener) {
        super.init(component, index, storeLayer, onValueChangeListener);
        this.mergeGetDynamicData = new M600MergeGetDynamicData(index);
        this.mergeGetSingleStaticData = new M600MergeGetSingleStaticData(index);
    }

    public void destroy() {
        super.destroy();
    }

    /* access modifiers changed from: protected */
    public boolean useBatteryGroupProtocols() {
        return true;
    }

    public void syncPushDataFromMidware() {
        super.syncPushDataFromMidware();
        CacheHelper.addBatteryListener(this.designCapacityKeyListener, this.index, BatteryKeys.DESIGN_CAPACITY);
    }

    @Getter(BatteryKeys.LIFETIME_REMAINING)
    public void getLifetimeRemaining(final DJISDKCacheHWAbstraction.InnerCallback callback) {
        this.mergeGetSingleStaticData.get(new M600SingleStaticDataCallback() {
            /* class dji.sdksharedlib.hardware.abstractions.battery.BatterySmartAbstraction.AnonymousClass2 */

            public void onSuccess(DataSmartBatteryGetStaticData staticData) {
                if (staticData != null) {
                    int lifeTimeRemainingPercent = (int) staticData.getLifePercent();
                    if (callback != null) {
                        callback.onSuccess(Integer.valueOf(lifeTimeRemainingPercent));
                    }
                } else if (callback != null) {
                    callback.onFails(DJIBatteryError.GET_SMART_BATTERY_INFO_FAILED);
                }
            }

            public void onFailure(Ccode ccode) {
                if (callback != null) {
                    callback.onFails(DJIError.getDJIError(ccode));
                }
            }
        });
    }

    @Getter(BatteryKeys.IS_BIG_BATTERY)
    public void getBatteryType(final DJISDKCacheHWAbstraction.InnerCallback callback) {
        this.mergeGetSingleStaticData.get(new M600SingleStaticDataCallback() {
            /* class dji.sdksharedlib.hardware.abstractions.battery.BatterySmartAbstraction.AnonymousClass3 */

            public void onSuccess(DataSmartBatteryGetStaticData staticData) {
                if (staticData != null) {
                    long capacity = staticData.getDesignCapacity();
                    if (callback != null) {
                        callback.onSuccess(Boolean.valueOf(capacity > 7000));
                    }
                } else if (callback != null) {
                    callback.onFails(DJIBatteryError.GET_SMART_BATTERY_INFO_FAILED);
                }
            }

            public void onFailure(Ccode ccode) {
                if (callback != null) {
                    callback.onFails(DJIError.getDJIError(ccode));
                }
            }
        });
    }

    @Getter(BatteryKeys.NUMBER_OF_DISCHARGES)
    public void getNumberOfDischarges(final DJISDKCacheHWAbstraction.InnerCallback callback) {
        this.mergeGetSingleStaticData.get(new M600SingleStaticDataCallback() {
            /* class dji.sdksharedlib.hardware.abstractions.battery.BatterySmartAbstraction.AnonymousClass4 */

            public void onSuccess(DataSmartBatteryGetStaticData staticData) {
                if (staticData != null) {
                    int numberOfDischarge = staticData.getCycleTimes();
                    if (callback != null) {
                        callback.onSuccess(Integer.valueOf(numberOfDischarge));
                    }
                } else if (callback != null) {
                    callback.onFails(DJIBatteryError.GET_SMART_BATTERY_INFO_FAILED);
                }
            }

            public void onFailure(Ccode ccode) {
                if (callback != null) {
                    callback.onFails(DJIError.getDJIError(ccode));
                }
            }
        });
    }

    @Getter(BatteryKeys.PRODUCTION_DATE)
    public void getProductDate(final DJISDKCacheHWAbstraction.InnerCallback innerCallback) {
        this.mergeGetSingleStaticData.get(new M600SingleStaticDataCallback() {
            /* class dji.sdksharedlib.hardware.abstractions.battery.BatterySmartAbstraction.AnonymousClass5 */

            public void onSuccess(DataSmartBatteryGetStaticData staticData) {
                if (staticData != null) {
                    CallbackUtils.onSuccess(innerCallback, Integer.valueOf(staticData.getProductionDate()));
                    return;
                }
                CallbackUtils.onFailure(innerCallback, DJIBatteryError.GET_SMART_BATTERY_INFO_FAILED);
            }

            public void onFailure(Ccode ccode) {
                CallbackUtils.onFailure(innerCallback, DJIError.getDJIError(ccode));
            }
        });
    }

    @Getter(BatteryKeys.FULL_CHARGE_CAPACITY)
    public void getFullChargeCapacity(final DJISDKCacheHWAbstraction.InnerCallback callback) {
        this.mergeGetDynamicData.get(new M600DynamicDataCallback() {
            /* class dji.sdksharedlib.hardware.abstractions.battery.BatterySmartAbstraction.AnonymousClass6 */

            public void onSuccess(DataSmartBatteryGetPushDynamicData data) {
                if (data != null) {
                    int fullChargeEnergy = data.getFullCapacity();
                    if (callback != null) {
                        callback.onSuccess(Integer.valueOf(fullChargeEnergy));
                    }
                } else if (callback != null) {
                    callback.onFails(DJIBatteryError.GET_SMART_BATTERY_INFO_FAILED);
                }
            }

            public void onFailure(Ccode ccode) {
                if (callback != null) {
                    callback.onFails(DJIError.getDJIError(ccode));
                }
            }
        });
    }

    @Getter(BatteryKeys.DESIGN_CAPACITY)
    public void getDesignCapacity(final DJISDKCacheHWAbstraction.InnerCallback callback) {
        this.mergeGetSingleStaticData.get(new M600SingleStaticDataCallback() {
            /* class dji.sdksharedlib.hardware.abstractions.battery.BatterySmartAbstraction.AnonymousClass7 */

            public void onSuccess(DataSmartBatteryGetStaticData staticData) {
                if (staticData != null) {
                    long capacity = staticData.getDesignCapacity();
                    if (callback != null) {
                        callback.onSuccess(Integer.valueOf((int) capacity));
                    }
                } else if (callback != null) {
                    callback.onFails(DJIBatteryError.GET_SMART_BATTERY_INFO_FAILED);
                }
            }

            public void onFailure(Ccode ccode) {
                if (callback != null) {
                    callback.onFails(DJIError.getDJIError(ccode));
                }
            }
        });
    }

    @Getter(BatteryKeys.SELF_HEATING_STATE)
    public void getSelfHeatingState(final DJISDKCacheHWAbstraction.InnerCallback callback) {
        this.mergeGetDynamicData.get(new M600DynamicDataCallback() {
            /* class dji.sdksharedlib.hardware.abstractions.battery.BatterySmartAbstraction.AnonymousClass8 */

            public void onSuccess(DataSmartBatteryGetPushDynamicData data) {
                if (data != null) {
                    CallbackUtils.onSuccess(callback, SelfHeatingState.find((int) ((data.getStatus() >> 59) & 3)));
                } else {
                    CallbackUtils.onFailure(callback, DJIBatteryError.GET_SMART_BATTERY_INFO_FAILED);
                }
            }

            public void onFailure(Ccode ccode) {
                CallbackUtils.onFailure(callback, ccode);
            }
        });
    }

    @Getter(BatteryKeys.CONNECTION_STATE)
    public void getConnectionState(final DJISDKCacheHWAbstraction.InnerCallback callback) {
        this.mergeGetDynamicData.get(new M600DynamicDataCallback() {
            /* class dji.sdksharedlib.hardware.abstractions.battery.BatterySmartAbstraction.AnonymousClass9 */

            public void onSuccess(DataSmartBatteryGetPushDynamicData data) {
                if (data != null) {
                    ConnectionState connectionState = BatterySmartAbstraction.this.getBatteryConnState(data.getStatus());
                    if (callback != null) {
                        callback.onSuccess(connectionState);
                    }
                } else if (callback != null) {
                    callback.onFails(DJIBatteryError.GET_SMART_BATTERY_INFO_FAILED);
                }
            }

            public void onFailure(Ccode ccode) {
                if (callback != null) {
                    callback.onFails(DJIError.getDJIError(ccode));
                }
            }
        });
    }

    /* access modifiers changed from: protected */
    public ConnectionState getBatteryConnState(long stateVal) {
        return ConnectionState.find(DataCenterGetPushBatteryCommon.ConnStatus.ofData(BatteryUtils.isErrorBatteryStatus(stateVal, 2) ? 1 : 0));
    }

    @Getter(BatteryKeys.IS_BATTERY_SELF_HEATING)
    public void getIsBatteryHeating(final DJISDKCacheHWAbstraction.InnerCallback callback) {
        this.mergeGetDynamicData.get(new M600DynamicDataCallback() {
            /* class dji.sdksharedlib.hardware.abstractions.battery.BatterySmartAbstraction.AnonymousClass10 */

            public void onSuccess(DataSmartBatteryGetPushDynamicData data) {
                if (data != null) {
                    boolean isHeating = data.getBatteryHeatState() > 0;
                    if (callback != null) {
                        callback.onSuccess(Boolean.valueOf(isHeating));
                    }
                } else if (callback != null) {
                    callback.onFails(DJIError.BATTERY_GET_SMART_BATTERY_INFO_FAILED);
                }
            }

            public void onFailure(Ccode ccode) {
                if (callback != null) {
                    callback.onFails(DJIError.getDJIError(ccode));
                }
            }
        });
    }

    @Getter(BatteryKeys.CURRENT)
    public void getCurrent(final DJISDKCacheHWAbstraction.InnerCallback callback) {
        this.mergeGetDynamicData.get(new M600DynamicDataCallback() {
            /* class dji.sdksharedlib.hardware.abstractions.battery.BatterySmartAbstraction.AnonymousClass11 */

            public void onSuccess(DataSmartBatteryGetPushDynamicData data) {
                if (data != null) {
                    int currentCurrent = data.getCurrent();
                    if (callback != null) {
                        callback.onSuccess(Integer.valueOf(currentCurrent));
                    }
                } else if (callback != null) {
                    callback.onFails(DJIBatteryError.GET_SMART_BATTERY_INFO_FAILED);
                }
            }

            public void onFailure(Ccode ccode) {
                if (callback != null) {
                    callback.onFails(DJIError.getDJIError(ccode));
                }
            }
        });
    }

    @Getter("ChargeRemaining")
    public void getChargeRemaining(final DJISDKCacheHWAbstraction.InnerCallback callback) {
        this.mergeGetDynamicData.get(new M600DynamicDataCallback() {
            /* class dji.sdksharedlib.hardware.abstractions.battery.BatterySmartAbstraction.AnonymousClass12 */

            public void onSuccess(DataSmartBatteryGetPushDynamicData data) {
                if (data != null) {
                    int currentEnergy = data.getRemainCapacity();
                    if (callback != null) {
                        callback.onSuccess(Integer.valueOf(currentEnergy));
                    }
                } else if (callback != null) {
                    callback.onFails(DJIBatteryError.GET_SMART_BATTERY_INFO_FAILED);
                }
            }

            public void onFailure(Ccode ccode) {
                callback.onFails(DJIError.getDJIError(ccode));
            }
        });
    }

    @Getter(BatteryKeys.VOLTAGE)
    public void getVoltage(final DJISDKCacheHWAbstraction.InnerCallback callback) {
        this.mergeGetDynamicData.get(new M600DynamicDataCallback() {
            /* class dji.sdksharedlib.hardware.abstractions.battery.BatterySmartAbstraction.AnonymousClass13 */

            public void onSuccess(DataSmartBatteryGetPushDynamicData data) {
                if (data != null) {
                    int currentVoltage = data.getVoltage();
                    if (callback != null) {
                        callback.onSuccess(Integer.valueOf(currentVoltage));
                    }
                } else if (callback != null) {
                    callback.onFails(DJIBatteryError.GET_SMART_BATTERY_INFO_FAILED);
                }
            }

            public void onFailure(Ccode ccode) {
                if (callback != null) {
                    callback.onFails(DJIError.getDJIError(ccode));
                }
            }
        });
    }

    @Getter(BatteryKeys.CHARGE_REMAINING_IN_PERCENT)
    public void getChargeRemainingInPercent(final DJISDKCacheHWAbstraction.InnerCallback callback) {
        this.mergeGetDynamicData.get(new M600DynamicDataCallback() {
            /* class dji.sdksharedlib.hardware.abstractions.battery.BatterySmartAbstraction.AnonymousClass14 */

            public void onSuccess(DataSmartBatteryGetPushDynamicData data) {
                if (data != null) {
                    int energyRemainingPercent = data.getRelativeCapacityPercentage();
                    if (callback != null) {
                        callback.onSuccess(Integer.valueOf(energyRemainingPercent));
                    }
                } else if (callback != null) {
                    callback.onFails(DJIBatteryError.GET_SMART_BATTERY_INFO_FAILED);
                }
            }

            public void onFailure(Ccode ccode) {
                if (callback != null) {
                    callback.onFails(DJIError.getDJIError(ccode));
                }
            }
        });
    }

    @Getter(BatteryKeys.LOW_TEMPERATURE_LEVEL)
    public void getLowTemperatureLevel(final DJISDKCacheHWAbstraction.InnerCallback callback) {
        getTemperature(new DJISDKCacheHWAbstraction.InnerCallback() {
            /* class dji.sdksharedlib.hardware.abstractions.battery.BatterySmartAbstraction.AnonymousClass15 */

            public void onSuccess(Object temp) {
                if (temp instanceof Float) {
                    BatterySOPTemperatureLevel level = BatterySmartAbstraction.this.getTempWarnLevel(((Float) temp).floatValue());
                    if (callback != null) {
                        callback.onSuccess(level);
                    }
                } else if (callback != null) {
                    callback.onFails(DJIBatteryError.GET_SMART_BATTERY_INFO_FAILED);
                }
            }

            public void onFails(DJIError error) {
                if (callback != null) {
                    callback.onFails(error);
                }
            }
        });
    }

    /* access modifiers changed from: protected */
    public BatterySOPTemperatureLevel getTempWarnLevel(float temperature) {
        BatterySOPTemperatureLevel batterySOPTemperatureLevel = BatterySOPTemperatureLevel.UNKNOWN;
        switch (DJIProductManager.getInstance().getType()) {
            case KumquatX:
            case WM230:
            case WM240:
                if (temperature < -10.0f) {
                    return BatterySOPTemperatureLevel.LEVEL_0;
                }
                if (temperature < 15.0f) {
                    return BatterySOPTemperatureLevel.LEVEL_1;
                }
                return BatterySOPTemperatureLevel.LEVEL_2;
            case Mammoth:
                if (temperature < 0.0f) {
                    return BatterySOPTemperatureLevel.LEVEL_0;
                }
                if (temperature < 15.0f) {
                    return BatterySOPTemperatureLevel.LEVEL_1;
                }
                return BatterySOPTemperatureLevel.LEVEL_2;
            case Pomato:
            case Tomato:
            case Potato:
                if (temperature < 0.0f) {
                    return BatterySOPTemperatureLevel.LEVEL_0;
                }
                return BatterySOPTemperatureLevel.LEVEL_2;
            case WM160:
                if (temperature <= 10.0f) {
                    return BatterySOPTemperatureLevel.LEVEL_1;
                }
                return BatterySOPTemperatureLevel.LEVEL_2;
            default:
                return BatterySOPTemperatureLevel.LEVEL_1;
        }
    }

    @Getter(BatteryKeys.TEMPERATURE)
    public void getTemperature(final DJISDKCacheHWAbstraction.InnerCallback callback) {
        this.mergeGetDynamicData.get(new M600DynamicDataCallback() {
            /* class dji.sdksharedlib.hardware.abstractions.battery.BatterySmartAbstraction.AnonymousClass16 */

            public void onSuccess(DataSmartBatteryGetPushDynamicData data) {
                if (data != null) {
                    Float temperature = Float.valueOf(((float) data.getTemperature()) / 10.0f);
                    if (callback != null) {
                        callback.onSuccess(temperature);
                    }
                } else if (callback != null) {
                    callback.onFails(DJIBatteryError.GET_SMART_BATTERY_INFO_FAILED);
                }
            }

            public void onFailure(Ccode ccode) {
                if (callback != null) {
                    callback.onFails(DJIError.getDJIError(ccode));
                }
            }
        });
    }

    @Getter(BatteryKeys.HISTORICAL_WARNING_RECORDS)
    public void getWarningRecords(final DJISDKCacheHWAbstraction.InnerCallback callback) {
        if (callback != null) {
            final DataSmartBatteryGetHistory historyGetter = DataSmartBatteryGetHistory.getInstance();
            historyGetter.setIndex(this.index + 1).start(new DJIDataCallBack() {
                /* class dji.sdksharedlib.hardware.abstractions.battery.BatterySmartAbstraction.AnonymousClass17 */

                public void onSuccess(Object model) {
                    long[] history = historyGetter.getHistory();
                    ArrayList<WarningRecord> historyList = new ArrayList<>();
                    if (history != null) {
                    }
                    if (history != null && history.length > 0) {
                        for (int i = 1; i < history.length; i++) {
                            historyList.add(new WarningRecord(history[i]));
                        }
                        WarningRecord[] historyArray = (WarningRecord[]) historyList.toArray(new WarningRecord[historyList.size()]);
                        if (callback != null) {
                            callback.onSuccess(historyArray);
                        }
                    }
                }

                public void onFailure(Ccode ccode) {
                    if (callback != null && callback != null) {
                        callback.onFails(DJIBatteryError.getDJIError(ccode));
                    }
                }
            });
        }
    }

    @Getter(BatteryKeys.LATEST_WARNING_RECORD)
    public void getLatestWarningRecord(final DJISDKCacheHWAbstraction.InnerCallback callback) {
        this.mergeGetDynamicData.get(new M600DynamicDataCallback() {
            /* class dji.sdksharedlib.hardware.abstractions.battery.BatterySmartAbstraction.AnonymousClass18 */

            public void onSuccess(DataSmartBatteryGetPushDynamicData data) {
                callback.onSuccess(new WarningRecord(data.getStatus()));
            }

            public void onFailure(Ccode ccode) {
                callback.onFails(DJIError.getDJIError(ccode));
            }
        });
    }

    @Getter(BatteryKeys.CELL_VOLTAGES)
    public void getCellVoltages(final DJISDKCacheHWAbstraction.InnerCallback callback) {
        if (callback != null) {
            final DataSmartBatteryGetPushCellVoltage cellVoltage = DataSmartBatteryGetPushCellVoltage.getInstance();
            cellVoltage.setIndex(this.index + 1).setRequestPush(false);
            cellVoltage.start(new DJIDataCallBack() {
                /* class dji.sdksharedlib.hardware.abstractions.battery.BatterySmartAbstraction.AnonymousClass19 */

                public void onSuccess(Object model) {
                    int[] partVoltageValues = cellVoltage.getVoltages();
                    if (partVoltageValues != null) {
                        for (int i : partVoltageValues) {
                            if (i != 0) {
                                List<Integer> cells = new ArrayList<>();
                                for (int cellVoltageValue : partVoltageValues) {
                                    if (cellVoltageValue == 0) {
                                        break;
                                    }
                                    cells.add(Integer.valueOf(cellVoltageValue));
                                }
                                callback.onSuccess((Integer[]) cells.toArray(new Integer[cells.size()]));
                                return;
                            }
                        }
                    } else if (callback != null) {
                        callback.onFails(DJIBatteryError.getDJIError(Ccode.INVALID_PARAM));
                    }
                }

                public void onFailure(Ccode ccode) {
                    if (callback != null) {
                        callback.onFails(DJIBatteryError.getDJIError(ccode));
                    }
                }
            });
        }
    }

    @Setter(BatteryKeys.SELF_DISCHARGE_IN_DAYS)
    public void setSelfDischargeInDays(Integer value, final DJISDKCacheHWAbstraction.InnerCallback callback) {
        Integer day = value;
        if ((day.intValue() < 1 || day.intValue() > 20) && callback != null) {
            callback.onFails(DJIBatteryError.getDJIError(Ccode.INVALID_PARAM));
        } else {
            DataSmartBatteryGetSetSelfDischargeDays.getInstance().setIndex(this.index + 1).setType(true).setDays(day.intValue()).start(new DJIDataCallBack() {
                /* class dji.sdksharedlib.hardware.abstractions.battery.BatterySmartAbstraction.AnonymousClass20 */

                public void onSuccess(Object model) {
                    if (callback != null) {
                        callback.onSuccess(null);
                    }
                }

                public void onFailure(Ccode ccode) {
                    if (callback != null) {
                        callback.onFails(DJIBatteryError.getDJIError(ccode));
                    }
                }
            });
        }
    }

    @Getter(BatteryKeys.SELF_DISCHARGE_IN_DAYS)
    public void getSelfDischargeInDays(final DJISDKCacheHWAbstraction.InnerCallback callback) {
        if (callback != null) {
            final DataSmartBatteryGetSetSelfDischargeDays selfDischargeDays = new DataSmartBatteryGetSetSelfDischargeDays();
            selfDischargeDays.setIndex(this.index + 1).setType(false).start(new DJIDataCallBack() {
                /* class dji.sdksharedlib.hardware.abstractions.battery.BatterySmartAbstraction.AnonymousClass21 */

                public void onSuccess(Object model) {
                    if (callback != null) {
                        callback.onSuccess(Integer.valueOf(selfDischargeDays.getDays()));
                    }
                }

                public void onFailure(Ccode ccode) {
                    if (callback != null) {
                        callback.onFails(DJIBatteryError.getDJIError(ccode));
                    }
                }
            });
        }
    }

    @Getter(DJISDKCacheKeys.FIRMWARE_VERSION)
    public void getVersion(final DJISDKCacheHWAbstraction.InnerCallback callback) {
        final DataCommonGetVersion dcgv = new DataCommonGetVersion();
        dcgv.setDeviceType(DeviceType.BATTERY);
        dcgv.setDeviceModel(this.index);
        dcgv.startForce(new DJIDataCallBack() {
            /* class dji.sdksharedlib.hardware.abstractions.battery.BatterySmartAbstraction.AnonymousClass22 */

            public void onSuccess(Object model) {
                String firmVersion = dcgv.getFirmVer(".");
                if (callback == null) {
                    return;
                }
                if (!TextUtils.isEmpty(firmVersion)) {
                    callback.onSuccess(firmVersion);
                } else {
                    callback.onFails(DJIError.UNABLE_TO_GET_FIRMWARE_VERSION);
                }
            }

            public void onFailure(Ccode ccode) {
                if (callback != null) {
                    callback.onFails(DJIBatteryError.getDJIError(ccode));
                }
            }
        });
    }

    public void getSerialNumber(final DJISDKCacheHWAbstraction.InnerCallback callback, final int state) {
        final DataSmartBatteryGetBarCode getter = new DataSmartBatteryGetBarCode();
        getter.setIndex(this.index + 1).start(new DJIDataCallBack() {
            /* class dji.sdksharedlib.hardware.abstractions.battery.BatterySmartAbstraction.AnonymousClass23 */

            public void onSuccess(Object model) {
                String bsn = getter.getBarCode();
                if (!(bsn == null || bsn.length() == 0 || state == 3)) {
                    bsn = BatterySmartAbstraction.this.getHashSerialNum(bsn, state);
                }
                CallbackUtils.onSuccess(callback, bsn);
            }

            public void onFailure(Ccode ccode) {
                DJILog.d("DJISDKCacheSmartBatteryAbstraction", "M600 get serial number fail. index " + BatterySmartAbstraction.this.index, new Object[0]);
                CallbackUtils.onFailure(callback, DJIBatteryError.getDJIError(ccode));
            }
        });
    }

    @Getter(BatteryKeys.BATTERY_STATIC_INFO_FOR_FLIGHTRECORD)
    public void getAggregationBatteryStaticInfoForFR(final DJISDKCacheHWAbstraction.InnerCallback callback) {
        if (callback != null) {
            final DataSmartBatteryGetStaticData staticData = new DataSmartBatteryGetStaticData();
            staticData.setIndex(this.index + 1).start(new DJIDataCallBack() {
                /* class dji.sdksharedlib.hardware.abstractions.battery.BatterySmartAbstraction.AnonymousClass24 */

                public void onSuccess(Object model) {
                    if (callback != null) {
                        callback.onSuccess(staticData);
                    }
                }

                public void onFailure(Ccode ccode) {
                    if (callback != null) {
                        callback.onFails(DJIBatteryError.getDJIError(ccode));
                    }
                }
            });
        }
    }

    @Getter(BatteryKeys.BATTERY_DYNAMIC_INFO_FOR_FLIGHTRECORD)
    public void getAggregationBatteryDynamicInfoForFR(final DJISDKCacheHWAbstraction.InnerCallback callback) {
        if (callback != null) {
            this.mergeGetDynamicData.get(new M600DynamicDataCallback() {
                /* class dji.sdksharedlib.hardware.abstractions.battery.BatterySmartAbstraction.AnonymousClass25 */

                public void onSuccess(DataSmartBatteryGetPushDynamicData data) {
                    if (callback != null) {
                        callback.onSuccess(data);
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

    @Getter(BatteryKeys.BATTERY_HW_TYPE)
    public void getBatteryHwType(final DJISDKCacheHWAbstraction.InnerCallback callback) {
        if (callback != null) {
            final DataSmartBatteryGetStaticData staticData = new DataSmartBatteryGetStaticData();
            staticData.setIndex(this.index + 1).start(new DJIDataCallBack() {
                /* class dji.sdksharedlib.hardware.abstractions.battery.BatterySmartAbstraction.AnonymousClass26 */

                public void onSuccess(Object model) {
                    if (callback != null) {
                        callback.onSuccess(BatteryHwType.find(staticData.getBatteryType()));
                    }
                }

                public void onFailure(Ccode ccode) {
                    if (callback != null) {
                        callback.onFails(DJIBatteryError.getDJIError(ccode));
                    }
                }
            });
        }
    }

    @Getter(BatteryKeys.BATTERY_CELL_VOLTAGE_FOR_FLIGHTRECORD)
    public void getAggregationBatteryCellVoltageForFR(final DJISDKCacheHWAbstraction.InnerCallback callback) {
        if (callback != null) {
            final DataSmartBatteryGetPushCellVoltage cellVoltage = new DataSmartBatteryGetPushCellVoltage();
            cellVoltage.setIndex(this.index + 1).setRequestPush(false);
            cellVoltage.start(new DJIDataCallBack() {
                /* class dji.sdksharedlib.hardware.abstractions.battery.BatterySmartAbstraction.AnonymousClass27 */

                public void onSuccess(Object model) {
                    if (callback != null) {
                        callback.onSuccess(cellVoltage);
                    }
                }

                public void onFailure(Ccode ccode) {
                    if (callback != null) {
                        callback.onFails(DJIBatteryError.getDJIError(ccode));
                    }
                }
            });
        }
    }
}
