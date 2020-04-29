package dji.sdksharedlib.hardware.abstractions.battery;

import dji.common.battery.AggregationState;
import dji.common.battery.BatteryOverview;
import dji.common.battery.BatteryUtils;
import dji.common.error.DJIBatteryError;
import dji.common.error.DJIError;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.config.P3.Ccode;
import dji.midware.data.model.P3.DataSmartBatteryGetMultBatteryInfo;
import dji.midware.data.model.P3.DataSmartBatteryGetPushCellVoltage;
import dji.midware.data.model.P3.DataSmartBatteryGetPushDynamicData;
import dji.midware.interfaces.DJIDataCallBack;
import dji.sdksharedlib.DJISDKCache;
import dji.sdksharedlib.extension.CacheHelper;
import dji.sdksharedlib.hardware.abstractions.DJISDKCacheHWAbstraction;
import dji.sdksharedlib.hardware.abstractions.Getter;
import dji.sdksharedlib.hardware.abstractions.battery.merge.M600DynamicDataCallback;
import dji.sdksharedlib.hardware.abstractions.battery.merge.M600GroupDataCallback;
import dji.sdksharedlib.hardware.abstractions.battery.merge.M600MergeGetDynamicData;
import dji.sdksharedlib.hardware.abstractions.battery.merge.M600MergeGetGroupData;
import dji.sdksharedlib.keycatalog.BatteryKeys;
import dji.sdksharedlib.keycatalog.DJISDKCacheKey;
import dji.sdksharedlib.listener.DJIParamAccessListener;
import dji.sdksharedlib.store.DJISDKCacheParamValue;
import dji.sdksharedlib.store.DJISDKCacheStoreLayer;

@EXClassNullAway
public class BatteryBaseAggregationAbstraction extends DJISDKCacheHWAbstraction implements DJIParamAccessListener {
    private static String TAG = "DJISDKCacheBaseAggregationBatteryAbstraction";
    private int index = Integer.MAX_VALUE;
    private M600MergeGetDynamicData mergeGetDynamicData;
    /* access modifiers changed from: private */
    public M600MergeGetGroupData mergeGetGroupData;

    public void init(String component, int index2, DJISDKCacheStoreLayer storeLayer, DJISDKCacheHWAbstraction.OnValueChangeListener onValueChangeListener) {
        super.init(component, index2, storeLayer, onValueChangeListener);
        this.index = index2;
        this.mergeGetDynamicData = new M600MergeGetDynamicData(index2);
        this.mergeGetGroupData = new M600MergeGetGroupData();
        registerListener();
    }

    public void syncPushDataFromMidware() {
        super.syncPushDataFromMidware();
        invalidateGroupState();
    }

    public void onValueChange(DJISDKCacheKey key, DJISDKCacheParamValue oldValue, DJISDKCacheParamValue newValue) {
    }

    public void destroy() {
        super.destroy();
        unregisterListener();
    }

    /* access modifiers changed from: protected */
    public void initializeComponentCharacteristics() {
        addCharacteristics(BatteryKeys.class, getClass());
    }

    /* access modifiers changed from: package-private */
    public void registerListener() {
        CacheHelper.addBatteryAggregationListener(this, BatteryKeys.NUMBER_OF_CONNECTED_BATTERIES, BatteryKeys.OVERVIEWS, BatteryKeys.HIGHEST_TEMPERATURE, BatteryKeys.IS_VOLTAGE_DIFFERENCE_DETECTED, BatteryKeys.IS_LOW_CELL_VOLTAGE_DETECTED, BatteryKeys.IS_CELL_DAMAGED, BatteryKeys.IS_ANY_BATTERY_DISCONNECTED, BatteryKeys.IS_FIRMWARE_DIFFERENCE_DETECTED);
    }

    public void unregisterListener() {
        DJISDKCache.getInstance().stopListening(this);
    }

    /* JADX DEBUG: Failed to find minimal casts for resolve overloaded methods, cast all args instead
     method: dji.sdksharedlib.hardware.abstractions.DJISDKCacheHWAbstraction.notifyValueChangeForKeyPath(java.lang.Object, dji.sdksharedlib.keycatalog.DJISDKCacheKey):void
     arg types: [int, dji.sdksharedlib.keycatalog.DJISDKCacheKey]
     candidates:
      dji.sdksharedlib.hardware.abstractions.DJISDKCacheHWAbstraction.notifyValueChangeForKeyPath(java.lang.Object, java.lang.String):void
      dji.sdksharedlib.hardware.abstractions.DJISDKCacheHWAbstraction.notifyValueChangeForKeyPath(java.lang.Object, dji.sdksharedlib.keycatalog.DJISDKCacheKey):void */
    /* JADX DEBUG: Failed to find minimal casts for resolve overloaded methods, cast all args instead
     method: dji.sdksharedlib.hardware.abstractions.DJISDKCacheHWAbstraction.notifyValueChangeForKeyPath(java.lang.Object, dji.sdksharedlib.keycatalog.DJISDKCacheKey):void
     arg types: [boolean, dji.sdksharedlib.keycatalog.DJISDKCacheKey]
     candidates:
      dji.sdksharedlib.hardware.abstractions.DJISDKCacheHWAbstraction.notifyValueChangeForKeyPath(java.lang.Object, java.lang.String):void
      dji.sdksharedlib.hardware.abstractions.DJISDKCacheHWAbstraction.notifyValueChangeForKeyPath(java.lang.Object, dji.sdksharedlib.keycatalog.DJISDKCacheKey):void */
    private void invalidateGroupState() {
        BatteryOverview[] batteryOverviews = {new BatteryOverview(1, false, 0)};
        notifyValueChangeForKeyPath((Object) 0, convertKeyToPath(BatteryKeys.NUMBER_OF_CONNECTED_BATTERIES));
        notifyValueChangeForKeyPath(batteryOverviews, convertKeyToPath(BatteryKeys.OVERVIEWS));
        notifyValueChangeForKeyPath((Object) 0, convertKeyToPath(BatteryKeys.HIGHEST_TEMPERATURE));
        notifyValueChangeForKeyPath((Object) true, convertKeyToPath(BatteryKeys.IS_ANY_BATTERY_DISCONNECTED));
        notifyValueChangeForKeyPath((Object) false, convertKeyToPath(BatteryKeys.IS_VOLTAGE_DIFFERENCE_DETECTED));
        notifyValueChangeForKeyPath((Object) false, convertKeyToPath(BatteryKeys.IS_LOW_CELL_VOLTAGE_DETECTED));
        notifyValueChangeForKeyPath((Object) false, convertKeyToPath(BatteryKeys.IS_CELL_DAMAGED));
        notifyValueChangeForKeyPath((Object) false, convertKeyToPath(BatteryKeys.IS_FIRMWARE_DIFFERENCE_DETECTED));
    }

    @Getter(BatteryKeys.AGGREGATION_STATE)
    public void getAggregationState(final DJISDKCacheHWAbstraction.InnerCallback callback) {
        this.mergeGetDynamicData.get(new M600DynamicDataCallback() {
            /* class dji.sdksharedlib.hardware.abstractions.battery.BatteryBaseAggregationAbstraction.AnonymousClass1 */

            public void onSuccess(DataSmartBatteryGetPushDynamicData data) {
                if (data != null) {
                    final int voltage = data.getVoltage();
                    final int current = data.getCurrent();
                    final int fullChargeCapacity = data.getFullCapacity();
                    final int chargeRemaining = data.getRemainCapacity();
                    final int chargeRemainingInPercent = data.getRelativeCapacityPercentage();
                    final int highestTemperature = (int) (((double) data.getTemperature()) / 10.0d);
                    long status = data.getStatus();
                    final boolean anyBatteryDisconnected = BatteryUtils.isErrorBatteryStatus(status, 1) || BatteryUtils.isErrorBatteryStatus(status, 2);
                    final boolean voltageDifferenceDetected = BatteryUtils.isErrorBatteryStatus(status, 4) || BatteryUtils.isErrorBatteryStatus(status, 3);
                    final boolean lowCellVoltageDetected = BatteryUtils.isErrorBatteryStatus(status, 5);
                    final boolean cellDamaged = BatteryUtils.isErrorBatteryStatus(status, 6);
                    final boolean firmwareDifferenceDetected = BatteryUtils.isErrorBatteryStatus(status, 7);
                    BatteryBaseAggregationAbstraction.this.mergeGetGroupData.get(new M600GroupDataCallback() {
                        /* class dji.sdksharedlib.hardware.abstractions.battery.BatteryBaseAggregationAbstraction.AnonymousClass1.AnonymousClass1 */

                        public void onSuccess(DataSmartBatteryGetMultBatteryInfo groupData) {
                            if (groupData != null) {
                                int numberOfConnectedBatteries = groupData.getNum();
                                BatteryOverview[] batteryOverviews = new BatteryOverview[groupData.getNum()];
                                for (int i = 0; i < groupData.getNum(); i++) {
                                    int energy = groupData.getValues()[i];
                                    if (energy == 0) {
                                        batteryOverviews[i] = new BatteryOverview(i, false, energy);
                                        numberOfConnectedBatteries--;
                                    } else {
                                        batteryOverviews[i] = new BatteryOverview(i, true, energy);
                                    }
                                }
                                if (callback != null) {
                                    callback.onSuccess(new AggregationState.Builder().numberOfConnectedBatteries(numberOfConnectedBatteries).batteryOverviews(batteryOverviews).voltage(voltage).current(current).chargeRemaining(chargeRemaining).fullChargeCapacity(fullChargeCapacity).chargeRemainingInPercent(chargeRemainingInPercent).highestTemperature(highestTemperature).voltageDifferenceDetected(voltageDifferenceDetected).lowCellVoltageDetected(lowCellVoltageDetected).cellDamaged(cellDamaged).anyBatteryDisconnected(anyBatteryDisconnected).firmwareDifferenceDetected(firmwareDifferenceDetected).build());
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

    @Getter(BatteryKeys.HIGHEST_TEMPERATURE)
    public void getHighestTemperature(final DJISDKCacheHWAbstraction.InnerCallback callback) {
        this.mergeGetDynamicData.get(new M600DynamicDataCallback() {
            /* class dji.sdksharedlib.hardware.abstractions.battery.BatteryBaseAggregationAbstraction.AnonymousClass2 */

            public void onSuccess(DataSmartBatteryGetPushDynamicData data) {
                if (data != null) {
                    int temperature = (int) (((double) data.getTemperature()) / 10.0d);
                    if (callback != null) {
                        callback.onSuccess(Integer.valueOf(temperature));
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

    @Getter(BatteryKeys.NUMBER_OF_CONNECTED_BATTERIES)
    public void getNumberOfConnectedBatteries(final DJISDKCacheHWAbstraction.InnerCallback callback) {
        this.mergeGetGroupData.get(new M600GroupDataCallback() {
            /* class dji.sdksharedlib.hardware.abstractions.battery.BatteryBaseAggregationAbstraction.AnonymousClass3 */

            public void onSuccess(DataSmartBatteryGetMultBatteryInfo groupData) {
                if (groupData != null) {
                    int batteryNum = groupData.getNum();
                    for (int i = 0; i < groupData.getNum(); i++) {
                        if (groupData.getValue(i) == 0) {
                            batteryNum--;
                        }
                    }
                    if (callback != null) {
                        callback.onSuccess(Integer.valueOf(batteryNum));
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

    @Getter(BatteryKeys.OVERVIEWS)
    public void getBatteryOverviews(final DJISDKCacheHWAbstraction.InnerCallback callback) {
        this.mergeGetGroupData.get(new M600GroupDataCallback() {
            /* class dji.sdksharedlib.hardware.abstractions.battery.BatteryBaseAggregationAbstraction.AnonymousClass4 */

            public void onSuccess(DataSmartBatteryGetMultBatteryInfo groupData) {
                if (groupData != null) {
                    int batteryNum = groupData.getNum();
                    BatteryOverview[] overviews = new BatteryOverview[batteryNum];
                    for (int i = 0; i < batteryNum; i++) {
                        int energy = groupData.getValues()[i];
                        if (energy == 0) {
                            overviews[i] = new BatteryOverview(i, false, energy);
                        } else {
                            overviews[i] = new BatteryOverview(i, true, energy);
                        }
                    }
                    if (callback != null) {
                        callback.onSuccess(overviews);
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

    @Getter(BatteryKeys.IS_ANY_BATTERY_DISCONNECTED)
    public void isAnyBatteryDisconnected(final DJISDKCacheHWAbstraction.InnerCallback callback) {
        this.mergeGetDynamicData.get(new M600DynamicDataCallback() {
            /* class dji.sdksharedlib.hardware.abstractions.battery.BatteryBaseAggregationAbstraction.AnonymousClass5 */

            public void onSuccess(DataSmartBatteryGetPushDynamicData dynamicData) {
                boolean disconnected = true;
                if (dynamicData != null) {
                    long status = dynamicData.getStatus();
                    if (!BatteryUtils.isErrorBatteryStatus(status, 1) && !BatteryUtils.isErrorBatteryStatus(status, 2)) {
                        disconnected = false;
                    }
                    if (callback != null) {
                        callback.onSuccess(Boolean.valueOf(disconnected));
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

    @Getter(BatteryKeys.IS_VOLTAGE_DIFFERENCE_DETECTED)
    public void isVoltageDifferenceDetected(final DJISDKCacheHWAbstraction.InnerCallback callback) {
        this.mergeGetDynamicData.get(new M600DynamicDataCallback() {
            /* class dji.sdksharedlib.hardware.abstractions.battery.BatteryBaseAggregationAbstraction.AnonymousClass6 */

            public void onSuccess(DataSmartBatteryGetPushDynamicData dynamicData) {
                if (dynamicData != null) {
                    long status = dynamicData.getStatus();
                    boolean voltageDifference = BatteryUtils.isErrorBatteryStatus(status, 4) || BatteryUtils.isErrorBatteryStatus(status, 3);
                    if (callback != null) {
                        callback.onSuccess(Boolean.valueOf(voltageDifference));
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

    @Getter(BatteryKeys.IS_LOW_CELL_VOLTAGE_DETECTED)
    public void isLowCellVoltageDetected(final DJISDKCacheHWAbstraction.InnerCallback callback) {
        this.mergeGetDynamicData.get(new M600DynamicDataCallback() {
            /* class dji.sdksharedlib.hardware.abstractions.battery.BatteryBaseAggregationAbstraction.AnonymousClass7 */

            public void onSuccess(DataSmartBatteryGetPushDynamicData dynamicData) {
                if (dynamicData != null) {
                    boolean lowCellVoltage = BatteryUtils.isErrorBatteryStatus(dynamicData.getStatus(), 5);
                    if (callback != null) {
                        callback.onSuccess(Boolean.valueOf(lowCellVoltage));
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

    @Getter(BatteryKeys.IS_CELL_DAMAGED)
    public void isCellDamaged(final DJISDKCacheHWAbstraction.InnerCallback callback) {
        this.mergeGetDynamicData.get(new M600DynamicDataCallback() {
            /* class dji.sdksharedlib.hardware.abstractions.battery.BatteryBaseAggregationAbstraction.AnonymousClass8 */

            public void onSuccess(DataSmartBatteryGetPushDynamicData dynamicData) {
                if (dynamicData != null) {
                    boolean hasDamagedCell = BatteryUtils.isErrorBatteryStatus(dynamicData.getStatus(), 6);
                    if (callback != null) {
                        callback.onSuccess(Boolean.valueOf(hasDamagedCell));
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

    @Getter(BatteryKeys.IS_FIRMWARE_DIFFERENCE_DETECTED)
    public void isFirmwareDifferenceDetected(final DJISDKCacheHWAbstraction.InnerCallback callback) {
        this.mergeGetDynamicData.get(new M600DynamicDataCallback() {
            /* class dji.sdksharedlib.hardware.abstractions.battery.BatteryBaseAggregationAbstraction.AnonymousClass9 */

            public void onSuccess(DataSmartBatteryGetPushDynamicData dynamicData) {
                if (dynamicData != null) {
                    boolean firmwareDifferenceDetected = BatteryUtils.isErrorBatteryStatus(dynamicData.getStatus(), 7);
                    if (callback != null) {
                        callback.onSuccess(Boolean.valueOf(firmwareDifferenceDetected));
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

    @Getter(BatteryKeys.BATTERY_DYNAMIC_INFO_FOR_FLIGHTRECORD)
    public void getAggregationBatteryDynamicInfoForFR(final DJISDKCacheHWAbstraction.InnerCallback callback) {
        this.mergeGetDynamicData.get(new M600DynamicDataCallback() {
            /* class dji.sdksharedlib.hardware.abstractions.battery.BatteryBaseAggregationAbstraction.AnonymousClass10 */

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

    @Getter(BatteryKeys.BATTERY_CELL_VOLTAGE_FOR_FLIGHTRECORD)
    public void getAggregationBatteryCellVoltageForFR(final DJISDKCacheHWAbstraction.InnerCallback callback) {
        if (callback != null) {
            final DataSmartBatteryGetPushCellVoltage cellVoltage = DataSmartBatteryGetPushCellVoltage.getInstance();
            cellVoltage.setIndex(this.index).setRequestPush(false);
            cellVoltage.start(new DJIDataCallBack() {
                /* class dji.sdksharedlib.hardware.abstractions.battery.BatteryBaseAggregationAbstraction.AnonymousClass11 */

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
