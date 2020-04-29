package dji.sdksharedlib.hardware.abstractions.battery;

import android.text.TextUtils;
import dji.common.battery.BatterySOPTemperatureLevel;
import dji.common.battery.BatteryState;
import dji.common.battery.ConnectionState;
import dji.common.battery.WarningRecord;
import dji.common.error.DJIBatteryError;
import dji.common.error.DJIError;
import dji.common.product.Model;
import dji.common.util.CallbackUtils;
import dji.fieldAnnotation.EXClassNullAway;
import dji.internal.DJIBatteryUtil;
import dji.midware.component.DJIComponentManager;
import dji.midware.data.config.P3.Ccode;
import dji.midware.data.config.P3.DeviceType;
import dji.midware.data.config.P3.ProductType;
import dji.midware.data.manager.P3.DJIProductManager;
import dji.midware.data.model.P3.DataBatteryGetPushCheckStatus;
import dji.midware.data.model.P3.DataCenterGetBatteryHistory;
import dji.midware.data.model.P3.DataCenterGetBatteryProductDate;
import dji.midware.data.model.P3.DataCenterGetBoardNumber;
import dji.midware.data.model.P3.DataCenterGetPushBatteryCommon;
import dji.midware.data.model.P3.DataCenterGetSelfDischarge;
import dji.midware.data.model.P3.DataCenterSelfDischarge;
import dji.midware.data.model.P3.DataCenterSetSelfDischarge;
import dji.midware.data.model.P3.DataCommonGetVersion;
import dji.midware.data.model.P3.DataFlycGetPushSmartBattery;
import dji.midware.data.model.P3.DataOsdGetPushCommon;
import dji.midware.data.model.P3.DataSmartBatteryGetPushDynamicData;
import dji.midware.data.model.P3.DataSmartBatteryGetStaticData;
import dji.midware.interfaces.DJIDataCallBack;
import dji.midware.util.DJIEventBusUtil;
import dji.sdksharedlib.extension.CacheHelper;
import dji.sdksharedlib.extension.KeyHelper;
import dji.sdksharedlib.hardware.abstractions.DJISDKCacheHWAbstraction;
import dji.sdksharedlib.hardware.abstractions.Getter;
import dji.sdksharedlib.hardware.abstractions.Setter;
import dji.sdksharedlib.keycatalog.BatteryKeys;
import dji.sdksharedlib.keycatalog.DJISDKCacheKeys;
import dji.sdksharedlib.keycatalog.ProductKeys;
import dji.sdksharedlib.store.DJISDKCacheParamValue;
import dji.sdksharedlib.store.DJISDKCacheStoreLayer;
import java.util.ArrayList;
import java.util.List;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

@EXClassNullAway
public class BatteryAbstraction extends DJISDKCacheHWAbstraction {
    private static final String TAG = "DJISDKCacheBatteryAbstraction";
    protected static final float TEMPERATURE_K2C = 273.15f;
    Integer[] cellVotages;
    protected DataSmartBatteryGetPushDynamicData dynamicData = null;
    protected boolean isSmartBattery = true;
    protected int numberOfCell = -1;
    private BatteryState.Builder stateBuilder;
    protected DataSmartBatteryGetStaticData staticData = null;

    public void init(String component, int index, DJISDKCacheStoreLayer storeLayer, DJISDKCacheHWAbstraction.OnValueChangeListener onValueChangeListener) {
        super.init(component, index, storeLayer, onValueChangeListener);
        this.stateBuilder = new BatteryState.Builder();
        DJIEventBusUtil.register(this);
    }

    public void destroy() {
        DJIEventBusUtil.unRegister(this);
        super.destroy();
    }

    public void syncPushDataFromMidware() {
        super.syncPushDataFromMidware();
        initializeCustomizedKey();
        if (DataCenterGetPushBatteryCommon.getInstance().isGetted()) {
            onEvent3BackgroundThread(DataCenterGetPushBatteryCommon.getInstance());
        }
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEvent3BackgroundThread(DataCenterGetPushBatteryCommon state) {
        int energyRemainingPercent;
        BatterySOPTemperatureLevel level;
        boolean isStateUpdated = false;
        if (!useBatteryGroupProtocols() && state != null) {
            int fullChargeCapacity = state.getFullCapacity();
            notifyValueChangeForKeyPath(Integer.valueOf(fullChargeCapacity), BatteryKeys.FULL_CHARGE_CAPACITY);
            this.stateBuilder.fullChargeCapacity(fullChargeCapacity);
            ConnectionState connectionState = ConnectionState.find(state.getConnStatus());
            notifyValueChangeForKeyPath(connectionState, BatteryKeys.CONNECTION_STATE);
            this.stateBuilder.connectionState(connectionState);
            int currentCapacity = state.getCurrentCapacity();
            notifyValueChangeForKeyPath(Integer.valueOf(currentCapacity), "ChargeRemaining");
            this.stateBuilder.chargeRemaining(currentCapacity);
            int currentPV = state.getCurrentPV();
            notifyValueChangeForKeyPath(Integer.valueOf(currentPV), BatteryKeys.VOLTAGE);
            this.stateBuilder.voltage(currentPV);
            int current = (short) state.getCurrent();
            notifyValueChangeForKeyPath(Integer.valueOf(current), BatteryKeys.CURRENT);
            this.stateBuilder.current(current);
            int lifeTimeRemaining = 0;
            if (!(CacheHelper.getProduct(ProductKeys.MODEL_NAME) == Model.PHANTOM_4_PRO || CacheHelper.getProduct(ProductKeys.MODEL_NAME) == Model.INSPIRE_2 || CacheHelper.getProduct(ProductKeys.MODEL_NAME) == Model.PHANTOM_4_PRO_V2 || CacheHelper.getProduct(ProductKeys.MODEL_NAME) == Model.PHANTOM_4_RTK || CacheHelper.getProduct(ProductKeys.MODEL_NAME) == Model.PHANTOM_4_ADVANCED)) {
                lifeTimeRemaining = state.getLife();
            }
            this.stateBuilder.lifetimeRemaining(lifeTimeRemaining);
            int energyRemainingPercent2 = 0;
            if (DataOsdGetPushCommon.getInstance().getFlycVersion() < 1) {
                energyRemainingPercent2 = DataOsdGetPushCommon.getInstance().getBattery();
            } else if (DataFlycGetPushSmartBattery.getInstance().isGetted()) {
                if (DataOsdGetPushCommon.getInstance().getBatteryType() == DataOsdGetPushCommon.BatteryType.NonSmart) {
                    energyRemainingPercent2 = DataFlycGetPushSmartBattery.getInstance().getVoltagePercent();
                } else {
                    energyRemainingPercent2 = DataFlycGetPushSmartBattery.getInstance().getBattery();
                }
            }
            if (energyRemainingPercent2 > 100) {
                energyRemainingPercent2 = 100;
            } else if (energyRemainingPercent2 < 0) {
                energyRemainingPercent2 = 0;
            }
            DJIComponentManager.PlatformType platformType = DJIComponentManager.getInstance().getPlatformType();
            if (platformType == DJIComponentManager.PlatformType.OSMO || platformType == DJIComponentManager.PlatformType.OSMOMobile) {
                energyRemainingPercent = (int) (((double) (((float) ((state.getRelativeCapacity() - 3) * 100)) / 97.0f)) + 0.5d);
                if (energyRemainingPercent > 100) {
                    energyRemainingPercent = 100;
                }
                if (energyRemainingPercent < 0) {
                    energyRemainingPercent = 0;
                }
            }
            notifyValueChangeForKeyPath(Integer.valueOf(energyRemainingPercent), BatteryKeys.CHARGE_REMAINING_IN_PERCENT);
            this.stateBuilder.chargeRemainingInPercent(energyRemainingPercent);
            float temperature = (((float) state.getTemperature()) / 10.0f) - 273.15f;
            notifyValueChangeForKeyPath(Float.valueOf(temperature), BatteryKeys.TEMPERATURE);
            this.stateBuilder.temperature(temperature);
            BatterySOPTemperatureLevel batterySOPTemperatureLevel = BatterySOPTemperatureLevel.UNKNOWN;
            switch (DJIProductManager.getInstance().getType()) {
                case KumquatX:
                case WM230:
                case WM240:
                    if (temperature >= -10.0f) {
                        if (temperature >= 15.0f) {
                            level = BatterySOPTemperatureLevel.LEVEL_2;
                            break;
                        } else {
                            level = BatterySOPTemperatureLevel.LEVEL_1;
                            break;
                        }
                    } else {
                        level = BatterySOPTemperatureLevel.LEVEL_0;
                        break;
                    }
                case Mammoth:
                    if (temperature >= 0.0f) {
                        if (temperature >= 15.0f) {
                            level = BatterySOPTemperatureLevel.LEVEL_2;
                            break;
                        } else {
                            level = BatterySOPTemperatureLevel.LEVEL_1;
                            break;
                        }
                    } else {
                        level = BatterySOPTemperatureLevel.LEVEL_0;
                        break;
                    }
                case Pomato:
                case Tomato:
                case Potato:
                    if (temperature >= 0.0f) {
                        level = BatterySOPTemperatureLevel.LEVEL_2;
                        break;
                    } else {
                        level = BatterySOPTemperatureLevel.LEVEL_0;
                        break;
                    }
                case WM160:
                    if (temperature >= -10.0f) {
                        if (temperature >= 5.0f) {
                            level = BatterySOPTemperatureLevel.LEVEL_2;
                            break;
                        } else {
                            level = BatterySOPTemperatureLevel.LEVEL_1;
                            break;
                        }
                    } else {
                        level = BatterySOPTemperatureLevel.LEVEL_0;
                        break;
                    }
                default:
                    level = BatterySOPTemperatureLevel.LEVEL_1;
                    break;
            }
            notifyValueChangeForKeyPath(level, BatteryKeys.LOW_TEMPERATURE_LEVEL);
            int loopNum = state.getLoopNum();
            notifyValueChangeForKeyPath(Integer.valueOf(loopNum), BatteryKeys.NUMBER_OF_DISCHARGES);
            this.stateBuilder.numberOfDischarges(loopNum);
            isStateUpdated = true;
        }
        int[] cells = new int[DJIBatteryUtil.getBatteryCellNumber()];
        int[] cellVoltage = state.getPartVoltages();
        if (cellVoltage != null) {
            if (cellVoltage.length < cells.length) {
                System.arraycopy(cellVoltage, 0, cells, 0, cellVoltage.length);
            } else {
                System.arraycopy(cellVoltage, 0, cells, 0, cells.length);
            }
            Integer[] array = new Integer[cells.length];
            for (int i = 0; i < cells.length; i++) {
                array[i] = Integer.valueOf(cells[i]);
            }
            notifyValueChangeForKeyPath(array, BatteryKeys.CELL_VOLTAGES);
        }
        if (isHG300Battery()) {
            boolean isBeingCharged = state.isBatteryOnCharge();
            notifyValueChangeForKeyPath(Boolean.valueOf(isBeingCharged), BatteryKeys.IS_BEING_CHARGED);
            this.stateBuilder.isBeingCharged(isBeingCharged);
            isStateUpdated = true;
        }
        if (isStateUpdated) {
            EventBus.getDefault().post(this.stateBuilder.build());
        }
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEvent3BackgroundThread(DataFlycGetPushSmartBattery smartBattery) {
        if (smartBattery != null) {
            notifyValueChangeForKeyPath(Boolean.valueOf(smartBattery.getIsSingleBatteryMode()), BatteryKeys.IS_IN_SINGLE_BATTERY_MODE);
        }
    }

    @Getter(DJISDKCacheKeys.FIRMWARE_VERSION)
    public void getVersion(final DJISDKCacheHWAbstraction.InnerCallback callback) {
        final DataCommonGetVersion dcgv = new DataCommonGetVersion();
        dcgv.setDeviceType(DeviceType.BATTERY);
        dcgv.start(new DJIDataCallBack() {
            /* class dji.sdksharedlib.hardware.abstractions.battery.BatteryAbstraction.AnonymousClass1 */

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

    @Setter(BatteryKeys.SELF_DISCHARGE_IN_DAYS)
    public void setSelfDischargeInDays(Integer value, final DJISDKCacheHWAbstraction.InnerCallback callback) {
        short day = value.shortValue();
        if (isSelfDischargeInDaysValueValid(day)) {
            ProductType type = DJIProductManager.getInstance().getType();
            if (type == ProductType.litchiS || type == ProductType.litchiC || type == ProductType.litchiX || type == ProductType.Pomato || type == ProductType.PomatoSDR || type == ProductType.Tomato || type == ProductType.Potato || type == ProductType.PomatoRTK) {
                DataCenterSetSelfDischarge dataLitchiSelfSetter = new DataCenterSetSelfDischarge();
                dataLitchiSelfSetter.setEncrypt(0);
                dataLitchiSelfSetter.setDays(day).start(new DJIDataCallBack() {
                    /* class dji.sdksharedlib.hardware.abstractions.battery.BatteryAbstraction.AnonymousClass2 */

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
                return;
            }
            DataCenterSelfDischarge.getInstance().setDays(day).setFlag(false).start(new DJIDataCallBack() {
                /* class dji.sdksharedlib.hardware.abstractions.battery.BatteryAbstraction.AnonymousClass3 */

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
        } else if (callback != null) {
            callback.onFails(DJIBatteryError.getDJIError(Ccode.INVALID_PARAM));
        }
    }

    /* access modifiers changed from: protected */
    public boolean isSelfDischargeInDaysValueValid(short days) {
        if (days < 1 || days > 20) {
            return false;
        }
        return true;
    }

    @Getter(BatteryKeys.SELF_DISCHARGE_IN_DAYS)
    public void getSelfDischargeInDays(final DJISDKCacheHWAbstraction.InnerCallback callback) {
        ProductType type = DJIProductManager.getInstance().getType();
        if (type == ProductType.litchiS || type == ProductType.litchiC || type == ProductType.litchiX || type == ProductType.Pomato || type == ProductType.PomatoSDR || type == ProductType.Tomato || type == ProductType.Potato || type == ProductType.PomatoRTK) {
            final DataCenterGetSelfDischarge dataLitchiSelfGetter = new DataCenterGetSelfDischarge();
            dataLitchiSelfGetter.setEncrypt(0);
            dataLitchiSelfGetter.start(new DJIDataCallBack() {
                /* class dji.sdksharedlib.hardware.abstractions.battery.BatteryAbstraction.AnonymousClass4 */

                public void onSuccess(Object model) {
                    int result = dataLitchiSelfGetter.getDay();
                    if (result < 1 || result > 20) {
                        result = 20;
                    }
                    if (callback != null) {
                        callback.onSuccess(Integer.valueOf(result));
                    }
                }

                public void onFailure(Ccode ccode) {
                    if (callback != null) {
                        callback.onFails(DJIBatteryError.getDJIError(ccode));
                    }
                }
            });
            return;
        }
        final DataCenterSelfDischarge instance = DataCenterSelfDischarge.getInstance();
        instance.setFlag(true).setDays(0).start(new DJIDataCallBack() {
            /* class dji.sdksharedlib.hardware.abstractions.battery.BatteryAbstraction.AnonymousClass5 */

            public void onSuccess(Object model) {
                int result = instance.getDay();
                if (callback != null) {
                    callback.onSuccess(Integer.valueOf(result));
                }
            }

            public void onFailure(Ccode ccode) {
                if (callback != null) {
                    callback.onFails(DJIBatteryError.getDJIError(ccode));
                }
            }
        });
    }

    @Getter(BatteryKeys.LATEST_WARNING_RECORD)
    public void getLatestWarningRecord(DJISDKCacheHWAbstraction.InnerCallback callback) {
        boolean z;
        boolean z2 = false;
        DataBatteryGetPushCheckStatus status = DataBatteryGetPushCheckStatus.getInstance();
        if (status == null && callback != null) {
            callback.onFails(DJIBatteryError.getDJIError(Ccode.GET_PARAM_FAILED));
        }
        if (callback != null) {
            boolean z3 = status.getFirstDischargeStatus() || status.getSecondDischargeStatus();
            if (status.getFirstOverheatStatus() || status.getSecondOverheatStatus()) {
                z = true;
            } else {
                z = false;
            }
            if (status.getFirstLowheatStatus() || status.getSecondLowheatStatus()) {
                z2 = true;
            }
            callback.onSuccess(new WarningRecord(z3, z, z2, status.getDischargeShortCircuit(), status.getCustomDischarge(), status.getUnderVoltageBatteryCellIndex(), status.getDamagedBatteryCellIndex()));
        }
    }

    @Getter(BatteryKeys.HISTORICAL_WARNING_RECORDS)
    public void getWarningRecords(final DJISDKCacheHWAbstraction.InnerCallback callback) {
        DataCenterGetBatteryHistory.getInstance().start(new DJIDataCallBack() {
            /* class dji.sdksharedlib.hardware.abstractions.battery.BatteryAbstraction.AnonymousClass6 */

            public void onSuccess(Object model) {
                int[] history = DataCenterGetBatteryHistory.getInstance().getHistory();
                List<WarningRecord> historyList = new ArrayList<>();
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
                if (callback != null) {
                    callback.onFails(DJIBatteryError.getDJIError(ccode));
                }
            }
        });
    }

    @Getter(BatteryKeys.PRODUCTION_DATE)
    public void getProductDate(final DJISDKCacheHWAbstraction.InnerCallback innerCallback) {
        DataCenterGetBatteryProductDate.getInstance().start(new DJIDataCallBack() {
            /* class dji.sdksharedlib.hardware.abstractions.battery.BatteryAbstraction.AnonymousClass7 */

            public void onSuccess(Object model) {
                int productDate = DataCenterGetBatteryProductDate.getInstance().getProductDate();
                if (innerCallback != null) {
                    innerCallback.onSuccess(Integer.valueOf(productDate));
                }
            }

            public void onFailure(Ccode ccode) {
                if (innerCallback != null) {
                    innerCallback.onFails(DJIBatteryError.getDJIError(ccode));
                }
            }
        });
    }

    @Getter(DJISDKCacheKeys.SERIAL_NUMBER)
    public void getSerialNumber(DJISDKCacheHWAbstraction.InnerCallback callback) {
        getSerialNumber(callback, 0);
    }

    @Getter("InternalSerialNumber")
    public void getInternalSerialNumber(DJISDKCacheHWAbstraction.InnerCallback callback) {
        getSerialNumber(callback, 3);
    }

    /* access modifiers changed from: protected */
    public void getSerialNumber(final DJISDKCacheHWAbstraction.InnerCallback callback, final int state) {
        DataCenterGetBoardNumber.getInstance().start(new DJIDataCallBack() {
            /* class dji.sdksharedlib.hardware.abstractions.battery.BatteryAbstraction.AnonymousClass8 */

            public void onSuccess(Object model) {
                String rawSN = DataCenterGetBoardNumber.getInstance().getBoardNumber();
                if (state != 3) {
                    rawSN = BatteryAbstraction.this.getHashSerialNum(rawSN, state);
                }
                if (callback != null) {
                    callback.onSuccess(rawSN);
                }
            }

            public void onFailure(Ccode ccode) {
                callback.onFails(DJIBatteryError.getDJIError(ccode));
            }
        });
    }

    @Getter(BatteryKeys.CELL_VOLTAGES)
    public void getCellVoltages(DJISDKCacheHWAbstraction.InnerCallback callback) {
        int[] partVoltageValues = DataCenterGetPushBatteryCommon.getInstance().getPartVoltages();
        for (int i : partVoltageValues) {
            if (i != 0) {
                List<Integer> cells = new ArrayList<>();
                for (int cellVoltageValue : partVoltageValues) {
                    if (cellVoltageValue == 0) {
                        break;
                    }
                    cells.add(Integer.valueOf(cellVoltageValue));
                }
                if (callback != null) {
                    callback.onSuccess((Integer[]) cells.toArray((Integer[]) cells.toArray(new Integer[cells.size()])));
                    return;
                }
                return;
            }
        }
        if (callback != null) {
            callback.onFails(DJIBatteryError.getDJIError(Ccode.GET_PARAM_FAILED));
        }
    }

    @Getter(BatteryKeys.NUMBER_OF_CELLS)
    public void getNumberOfCells(final DJISDKCacheHWAbstraction.InnerCallback callback) {
        new Thread(new Runnable() {
            /* class dji.sdksharedlib.hardware.abstractions.battery.BatteryAbstraction.AnonymousClass9 */

            public void run() {
                Integer[] numArr;
                int size = 0;
                if (BatteryAbstraction.this.cellVotages == null) {
                    BatteryAbstraction.this.cellVotages = (Integer[]) CacheHelper.getBattery(BatteryKeys.CELL_VOLTAGES);
                    if (BatteryAbstraction.this.cellVotages == null) {
                        DJISDKCacheParamValue value = CacheHelper.getValueSynchronously(KeyHelper.getBatteryKey(BatteryKeys.CELL_VOLTAGES), 1000);
                        BatteryAbstraction batteryAbstraction = BatteryAbstraction.this;
                        if (value == null || value.getData() == null || !(value.getData() instanceof Integer[])) {
                            numArr = null;
                        } else {
                            numArr = (Integer[]) value.getData();
                        }
                        batteryAbstraction.cellVotages = numArr;
                    }
                }
                if (BatteryAbstraction.this.cellVotages != null) {
                    int i = 0;
                    while (i < BatteryAbstraction.this.cellVotages.length && BatteryAbstraction.this.cellVotages != null && BatteryAbstraction.this.cellVotages[i].intValue() != 0) {
                        size++;
                        i++;
                    }
                }
                if (size > 0) {
                    CallbackUtils.onSuccess(callback, Integer.valueOf(size));
                } else {
                    CallbackUtils.onFailure(callback, DJIError.COMMON_UNKNOWN);
                }
            }
        }, "battAbs").start();
    }

    @Getter(BatteryKeys.IS_SMART_BATTERY)
    public void isSmartBattery(DJISDKCacheHWAbstraction.InnerCallback callback) {
        if (callback != null) {
            callback.onSuccess(Boolean.valueOf(this.isSmartBattery));
        }
    }

    /* access modifiers changed from: protected */
    public Class<? extends DJISDKCacheKeys> getDJISDKCacheKeysClass() {
        return BatteryKeys.class;
    }

    /* access modifiers changed from: protected */
    public boolean useBatteryGroupProtocols() {
        return false;
    }

    /* access modifiers changed from: protected */
    public boolean isHG300Battery() {
        return false;
    }

    /* access modifiers changed from: protected */
    public void initializeComponentCharacteristics() {
        addCharacteristics(BatteryKeys.class, getClass());
    }

    /* access modifiers changed from: protected */
    public boolean isSmartBattery() {
        return this.isSmartBattery;
    }

    /* access modifiers changed from: protected */
    public void initializeCustomizedKey() {
        notifyValueChangeForKeyPath(Boolean.valueOf(isSmartBattery()), convertKeyToPath(BatteryKeys.IS_SMART_BATTERY));
    }

    /* access modifiers changed from: protected */
    public boolean isWarningInformationRecordsSupported() {
        return true;
    }
}
