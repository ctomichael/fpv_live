package dji.sdksharedlib.hardware.abstractions.battery;

import dji.common.battery.LowVoltageBehavior;
import dji.common.error.DJIError;
import dji.common.util.CallbackUtils;
import dji.fieldAnnotation.EXClassNullAway;
import dji.log.DJILog;
import dji.midware.data.config.P3.Ccode;
import dji.midware.data.manager.P3.DJIFlycParamInfoManager;
import dji.midware.data.model.P3.DataFlycGetParams;
import dji.midware.data.model.P3.DataFlycSetParams;
import dji.midware.data.model.P3.DataOsdGetPushCommon;
import dji.midware.interfaces.DJIDataCallBack;
import dji.midware.util.DJIEventBusUtil;
import dji.sdksharedlib.hardware.abstractions.DJISDKCacheHWAbstraction;
import dji.sdksharedlib.hardware.abstractions.Getter;
import dji.sdksharedlib.hardware.abstractions.Setter;
import dji.sdksharedlib.keycatalog.BatteryKeys;
import dji.sdksharedlib.store.DJISDKCacheStoreLayer;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

@EXClassNullAway
public class NonSmartA3BatteryAbstraction extends NonSmartBatteryAbstraction {
    public static final String PARAM_NAME_CELL_NUM = "g_config.voltage.battery_cell_0";
    private static final String PARAM_NAME_CRITICAL_VOTAGE_OPERATION = "g_config.voltage.level_2_protect_type_0";
    private static final String PARAM_NAME_CRITICAL_VOTAGE_THRESHOLD = "g_config.voltage.level_2_protect_0";
    private static final String PARAM_NAME_VOTAGE_OPERATION = "g_config.voltage.level_1_protect_type_0";
    private static final String PARAM_NAME_VOTAGE_THRESHOLD = "g_config.voltage.level_1_protect_0";
    private static final String TAG = "DJINonSmartA3BatteryAbstraction";
    /* access modifiers changed from: private */
    public int level1Threshold = 0;
    /* access modifiers changed from: private */
    public int level2Threshold = 0;

    public NonSmartA3BatteryAbstraction() {
        this.isSmartBattery = false;
    }

    public void init(String component, int index, DJISDKCacheStoreLayer storeLayer, DJISDKCacheHWAbstraction.OnValueChangeListener onValueChangeListener) {
        super.init(component, index, storeLayer, onValueChangeListener);
        DJIEventBusUtil.register(this);
    }

    /* access modifiers changed from: private */
    public void initThresholds(int timeoutInMs) {
        final CountDownLatch cdl = new CountDownLatch(1);
        new DataFlycGetParams().setInfos(new String[]{PARAM_NAME_VOTAGE_THRESHOLD, PARAM_NAME_CRITICAL_VOTAGE_THRESHOLD}).start(new DJIDataCallBack() {
            /* class dji.sdksharedlib.hardware.abstractions.battery.NonSmartA3BatteryAbstraction.AnonymousClass1 */

            public void onSuccess(Object model) {
                int unused = NonSmartA3BatteryAbstraction.this.level1Threshold = DJIFlycParamInfoManager.read(NonSmartA3BatteryAbstraction.PARAM_NAME_VOTAGE_THRESHOLD).value.intValue();
                int unused2 = NonSmartA3BatteryAbstraction.this.level2Threshold = DJIFlycParamInfoManager.read(NonSmartA3BatteryAbstraction.PARAM_NAME_CRITICAL_VOTAGE_THRESHOLD).value.intValue();
                cdl.countDown();
            }

            public void onFailure(Ccode ccode) {
                cdl.countDown();
            }
        });
        if (timeoutInMs > 0) {
            try {
                cdl.await((long) timeoutInMs, TimeUnit.MILLISECONDS);
            } catch (InterruptedException e) {
                DJILog.e(TAG, DJILog.exceptionToString(e), new Object[0]);
            }
        } else {
            cdl.await();
        }
    }

    @Getter(BatteryKeys.NUMBER_OF_CELLS)
    public void getNumberOfCells(final DJISDKCacheHWAbstraction.InnerCallback callback) {
        new DataFlycGetParams().setInfos(new String[]{PARAM_NAME_CELL_NUM}).start(new DJIDataCallBack() {
            /* class dji.sdksharedlib.hardware.abstractions.battery.NonSmartA3BatteryAbstraction.AnonymousClass2 */

            public void onSuccess(Object model) {
                CallbackUtils.onSuccess(callback, Integer.valueOf(DJIFlycParamInfoManager.read(NonSmartA3BatteryAbstraction.PARAM_NAME_CELL_NUM).value.intValue()));
            }

            public void onFailure(Ccode ccode) {
                CallbackUtils.onFailure(callback, ccode);
            }
        });
    }

    @Setter(BatteryKeys.NUMBER_OF_CELLS)
    public void setNumberOfCells(int numberOfCells, final DJISDKCacheHWAbstraction.InnerCallback callback) {
        if (DataOsdGetPushCommon.getInstance().isMotorUp()) {
            CallbackUtils.onFailure(callback, DJIError.COMMON_EXECUTION_FAILED);
        } else if (numberOfCells < 3 || numberOfCells > 12) {
            CallbackUtils.onFailure(callback, DJIError.COMMON_PARAM_ILLEGAL);
        } else {
            DataFlycSetParams paramSet = new DataFlycSetParams();
            paramSet.setIndexs(PARAM_NAME_CELL_NUM);
            paramSet.setValues(Integer.valueOf(numberOfCells));
            paramSet.start(new DJIDataCallBack() {
                /* class dji.sdksharedlib.hardware.abstractions.battery.NonSmartA3BatteryAbstraction.AnonymousClass3 */

                public void onSuccess(Object model) {
                    CallbackUtils.onSuccess(callback, (Object) null);
                }

                public void onFailure(Ccode ccode) {
                    CallbackUtils.onFailure(callback, ccode);
                }
            });
        }
    }

    @Getter(BatteryKeys.LEVEL_1_CELL_VOLTAGE_THRESHOLD)
    public void getLevel1CellVoltageThreshold(final DJISDKCacheHWAbstraction.InnerCallback callback) {
        new DataFlycGetParams().setInfos(new String[]{PARAM_NAME_VOTAGE_THRESHOLD}).start(new DJIDataCallBack() {
            /* class dji.sdksharedlib.hardware.abstractions.battery.NonSmartA3BatteryAbstraction.AnonymousClass4 */

            public void onSuccess(Object model) {
                int unused = NonSmartA3BatteryAbstraction.this.level1Threshold = DJIFlycParamInfoManager.read(NonSmartA3BatteryAbstraction.PARAM_NAME_VOTAGE_THRESHOLD).value.intValue();
                CallbackUtils.onSuccess(callback, Integer.valueOf(NonSmartA3BatteryAbstraction.this.level1Threshold));
            }

            public void onFailure(Ccode ccode) {
                CallbackUtils.onFailure(callback, ccode);
            }
        });
    }

    @Setter(BatteryKeys.LEVEL_1_CELL_VOLTAGE_THRESHOLD)
    public void setLevel1CellVoltageThreshold(final int threshold, final DJISDKCacheHWAbstraction.InnerCallback callback) {
        if (threshold < 3600 || threshold > 4000) {
            CallbackUtils.onFailure(callback, DJIError.COMMON_PARAM_ILLEGAL);
        } else {
            new Thread(new Runnable() {
                /* class dji.sdksharedlib.hardware.abstractions.battery.NonSmartA3BatteryAbstraction.AnonymousClass5 */

                public void run() {
                    boolean needSetL2Threshold;
                    if (NonSmartA3BatteryAbstraction.this.level2Threshold <= 0 || NonSmartA3BatteryAbstraction.this.level1Threshold <= 0) {
                        NonSmartA3BatteryAbstraction.this.initThresholds(1000);
                    }
                    if (NonSmartA3BatteryAbstraction.this.level2Threshold <= 0 || NonSmartA3BatteryAbstraction.this.level2Threshold <= threshold - 100) {
                        needSetL2Threshold = false;
                    } else {
                        needSetL2Threshold = true;
                    }
                    DataFlycSetParams paramSet = new DataFlycSetParams();
                    paramSet.setIndexs(needSetL2Threshold ? new String[]{NonSmartA3BatteryAbstraction.PARAM_NAME_CRITICAL_VOTAGE_THRESHOLD, NonSmartA3BatteryAbstraction.PARAM_NAME_VOTAGE_THRESHOLD} : new String[]{NonSmartA3BatteryAbstraction.PARAM_NAME_VOTAGE_THRESHOLD});
                    paramSet.setValues(needSetL2Threshold ? new Number[]{Integer.valueOf(threshold - 100), Integer.valueOf(threshold)} : new Number[]{Integer.valueOf(threshold)});
                    paramSet.start(new DJIDataCallBack() {
                        /* class dji.sdksharedlib.hardware.abstractions.battery.NonSmartA3BatteryAbstraction.AnonymousClass5.AnonymousClass1 */

                        public void onSuccess(Object model) {
                            int unused = NonSmartA3BatteryAbstraction.this.level1Threshold = threshold;
                            CallbackUtils.onSuccess(callback, (Object) null);
                        }

                        public void onFailure(Ccode ccode) {
                            CallbackUtils.onFailure(callback, ccode);
                        }
                    });
                }
            }, "a3Battery").start();
        }
    }

    @Getter(BatteryKeys.LEVEL_2_CELL_VOLTAGE_THRESHOLD)
    public void getLevel2CellVoltageThreshold(final DJISDKCacheHWAbstraction.InnerCallback callback) {
        new DataFlycGetParams().setInfos(new String[]{PARAM_NAME_CRITICAL_VOTAGE_THRESHOLD}).start(new DJIDataCallBack() {
            /* class dji.sdksharedlib.hardware.abstractions.battery.NonSmartA3BatteryAbstraction.AnonymousClass6 */

            public void onSuccess(Object model) {
                CallbackUtils.onSuccess(callback, Integer.valueOf(DJIFlycParamInfoManager.read(NonSmartA3BatteryAbstraction.PARAM_NAME_CRITICAL_VOTAGE_THRESHOLD).value.intValue()));
            }

            public void onFailure(Ccode ccode) {
                CallbackUtils.onFailure(callback, ccode);
            }
        });
    }

    @Setter(BatteryKeys.LEVEL_2_CELL_VOLTAGE_THRESHOLD)
    public void setLevel2CellVoltageThreshold(final int threshold, final DJISDKCacheHWAbstraction.InnerCallback callback) {
        if (threshold < 3500 || threshold > 3800) {
            CallbackUtils.onFailure(callback, DJIError.COMMON_PARAM_ILLEGAL);
        } else {
            new Thread(new Runnable() {
                /* class dji.sdksharedlib.hardware.abstractions.battery.NonSmartA3BatteryAbstraction.AnonymousClass7 */

                public void run() {
                    if (NonSmartA3BatteryAbstraction.this.level1Threshold <= 0 || NonSmartA3BatteryAbstraction.this.level2Threshold <= 0) {
                        NonSmartA3BatteryAbstraction.this.initThresholds(1000);
                    }
                    if (NonSmartA3BatteryAbstraction.this.level1Threshold <= 0 || threshold <= NonSmartA3BatteryAbstraction.this.level1Threshold - 100) {
                        DataFlycSetParams paramSet = new DataFlycSetParams();
                        paramSet.setIndexs(NonSmartA3BatteryAbstraction.PARAM_NAME_CRITICAL_VOTAGE_THRESHOLD);
                        paramSet.setValues(Integer.valueOf(threshold));
                        paramSet.start(new DJIDataCallBack() {
                            /* class dji.sdksharedlib.hardware.abstractions.battery.NonSmartA3BatteryAbstraction.AnonymousClass7.AnonymousClass1 */

                            public void onSuccess(Object model) {
                                int unused = NonSmartA3BatteryAbstraction.this.level2Threshold = threshold;
                                CallbackUtils.onSuccess(callback, (Object) null);
                            }

                            public void onFailure(Ccode ccode) {
                                CallbackUtils.onFailure(callback, ccode);
                            }
                        });
                        return;
                    }
                    CallbackUtils.onFailure(callback, DJIError.COMMON_PARAM_ILLEGAL);
                }
            }, "a3Battery2").start();
        }
    }

    @Getter(BatteryKeys.LEVEL_1_CELL_VOLTAGE_BEHAVIOR)
    public void getLevel1CellVoltageBehavior(final DJISDKCacheHWAbstraction.InnerCallback callback) {
        new DataFlycGetParams().setInfos(new String[]{PARAM_NAME_VOTAGE_OPERATION}).start(new DJIDataCallBack() {
            /* class dji.sdksharedlib.hardware.abstractions.battery.NonSmartA3BatteryAbstraction.AnonymousClass8 */

            public void onSuccess(Object model) {
                CallbackUtils.onSuccess(callback, LowVoltageBehavior.find(DJIFlycParamInfoManager.read(NonSmartA3BatteryAbstraction.PARAM_NAME_VOTAGE_OPERATION).value.intValue()));
            }

            public void onFailure(Ccode ccode) {
                CallbackUtils.onFailure(callback, ccode);
            }
        });
    }

    @Setter(BatteryKeys.LEVEL_1_CELL_VOLTAGE_BEHAVIOR)
    public void setLevel1CellVoltageBehavior(LowVoltageBehavior behavior, final DJISDKCacheHWAbstraction.InnerCallback callback) {
        if (DataOsdGetPushCommon.getInstance().isMotorUp()) {
            CallbackUtils.onFailure(callback, DJIError.COMMON_EXECUTION_FAILED);
        } else if (behavior == null || behavior == LowVoltageBehavior.UNKNOWN) {
            CallbackUtils.onFailure(callback, DJIError.COMMON_PARAM_ILLEGAL);
        } else {
            DataFlycSetParams paramSet = new DataFlycSetParams();
            paramSet.setIndexs(PARAM_NAME_VOTAGE_OPERATION);
            paramSet.setValues(Integer.valueOf(behavior.value()));
            paramSet.start(new DJIDataCallBack() {
                /* class dji.sdksharedlib.hardware.abstractions.battery.NonSmartA3BatteryAbstraction.AnonymousClass9 */

                public void onSuccess(Object model) {
                    CallbackUtils.onSuccess(callback, (Object) null);
                }

                public void onFailure(Ccode ccode) {
                    CallbackUtils.onFailure(callback, ccode);
                }
            });
        }
    }

    @Getter(BatteryKeys.LEVEL_2_CELL_VOLTAGE_BEHAVIOR)
    public void getLevel2CellVoltageBehavior(final DJISDKCacheHWAbstraction.InnerCallback callback) {
        new DataFlycGetParams().setInfos(new String[]{PARAM_NAME_CRITICAL_VOTAGE_OPERATION}).start(new DJIDataCallBack() {
            /* class dji.sdksharedlib.hardware.abstractions.battery.NonSmartA3BatteryAbstraction.AnonymousClass10 */

            public void onSuccess(Object model) {
                CallbackUtils.onSuccess(callback, LowVoltageBehavior.find(DJIFlycParamInfoManager.read(NonSmartA3BatteryAbstraction.PARAM_NAME_CRITICAL_VOTAGE_OPERATION).value.intValue()));
            }

            public void onFailure(Ccode ccode) {
                CallbackUtils.onFailure(callback, ccode);
            }
        });
    }

    @Setter(BatteryKeys.LEVEL_2_CELL_VOLTAGE_BEHAVIOR)
    public void setLevel2CellVoltageBehavior(LowVoltageBehavior behavior, final DJISDKCacheHWAbstraction.InnerCallback callback) {
        if (DataOsdGetPushCommon.getInstance().isMotorUp()) {
            CallbackUtils.onFailure(callback, DJIError.COMMON_EXECUTION_FAILED);
        } else if (behavior == null || behavior == LowVoltageBehavior.UNKNOWN) {
            CallbackUtils.onFailure(callback, DJIError.COMMON_PARAM_ILLEGAL);
        } else {
            DataFlycSetParams paramSet = new DataFlycSetParams();
            paramSet.setIndexs(PARAM_NAME_CRITICAL_VOTAGE_OPERATION);
            paramSet.setValues(Integer.valueOf(behavior.value()));
            paramSet.start(new DJIDataCallBack() {
                /* class dji.sdksharedlib.hardware.abstractions.battery.NonSmartA3BatteryAbstraction.AnonymousClass11 */

                public void onSuccess(Object model) {
                    CallbackUtils.onSuccess(callback, (Object) null);
                }

                public void onFailure(Ccode ccode) {
                    CallbackUtils.onFailure(callback, ccode);
                }
            });
        }
    }

    public void destroy() {
        DJIEventBusUtil.unRegister(this);
        super.destroy();
    }
}
