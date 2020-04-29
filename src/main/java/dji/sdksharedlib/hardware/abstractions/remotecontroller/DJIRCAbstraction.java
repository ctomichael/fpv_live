package dji.sdksharedlib.hardware.abstractions.remotecontroller;

import android.text.TextUtils;
import android.util.SparseArray;
import dji.common.Stick;
import dji.common.error.DJIError;
import dji.common.error.DJINarrowBandModuleError;
import dji.common.error.DJIRemoteControllerError;
import dji.common.remotecontroller.AircraftMapping;
import dji.common.remotecontroller.AircraftMappingStyle;
import dji.common.remotecontroller.AircraftStickMapping;
import dji.common.remotecontroller.AircraftStickMappingTarget;
import dji.common.remotecontroller.CalibrationState;
import dji.common.remotecontroller.ChargeMobileMode;
import dji.common.remotecontroller.ChargeRemaining;
import dji.common.remotecontroller.ConnectToMasterResult;
import dji.common.remotecontroller.Credentials;
import dji.common.remotecontroller.CustomButtonTags;
import dji.common.remotecontroller.FocusControllerState;
import dji.common.remotecontroller.GPSData;
import dji.common.remotecontroller.GimbalAxis;
import dji.common.remotecontroller.GimbalControlSpeedCoefficient;
import dji.common.remotecontroller.GimbalMapping;
import dji.common.remotecontroller.GimbalMappingStyle;
import dji.common.remotecontroller.GimbalStickMapping;
import dji.common.remotecontroller.GimbalStickMappingTarget;
import dji.common.remotecontroller.HardwareState;
import dji.common.remotecontroller.Information;
import dji.common.remotecontroller.PairingState;
import dji.common.remotecontroller.RCMode;
import dji.common.remotecontroller.RequestGimbalControlResult;
import dji.common.remotecontroller.ResponseForGimbalControl;
import dji.common.remotecontroller.SoftSwitchJoyStickMode;
import dji.common.util.CallbackUtils;
import dji.fieldAnnotation.EXClassNullAway;
import dji.internal.logics.CommonUtil;
import dji.internal.version.VersionController;
import dji.log.DJILog;
import dji.midware.data.config.P3.Ccode;
import dji.midware.data.config.P3.DeviceType;
import dji.midware.data.config.P3.ProductType;
import dji.midware.data.manager.P3.DJIProductManager;
import dji.midware.data.model.P3.DataCommonGetPushUpgradeStatus;
import dji.midware.data.model.P3.DataDm368GetGParams;
import dji.midware.data.model.P3.DataDm368SetGParams;
import dji.midware.data.model.P3.DataOsdActiveStatus;
import dji.midware.data.model.P3.DataOsdGetPushCheckStatus;
import dji.midware.data.model.P3.DataOsdGetPushCommon;
import dji.midware.data.model.P3.DataRcAckGimbalCtrPermission;
import dji.midware.data.model.P3.DataRcGetConnectMaster;
import dji.midware.data.model.P3.DataRcGetControlMode;
import dji.midware.data.model.P3.DataRcGetCustomFuction;
import dji.midware.data.model.P3.DataRcGetGimbalControlMode;
import dji.midware.data.model.P3.DataRcGetGimbalSpeed;
import dji.midware.data.model.P3.DataRcGetMaster;
import dji.midware.data.model.P3.DataRcGetName;
import dji.midware.data.model.P3.DataRcGetPassword;
import dji.midware.data.model.P3.DataRcGetPushBatteryInfo;
import dji.midware.data.model.P3.DataRcGetPushCheckStatus;
import dji.midware.data.model.P3.DataRcGetPushFollowFocus;
import dji.midware.data.model.P3.DataRcGetPushFollowFocus2;
import dji.midware.data.model.P3.DataRcGetPushGpsInfo;
import dji.midware.data.model.P3.DataRcGetPushParams;
import dji.midware.data.model.P3.DataRcGetPushRcCustomButtonsStatus;
import dji.midware.data.model.P3.DataRcGetSearchMasters;
import dji.midware.data.model.P3.DataRcGetSearchMode;
import dji.midware.data.model.P3.DataRcGetSlaveList;
import dji.midware.data.model.P3.DataRcGetSlaveMode;
import dji.midware.data.model.P3.DataRcGetSlavePermission;
import dji.midware.data.model.P3.DataRcGetToggle;
import dji.midware.data.model.P3.DataRcGetWheelGain;
import dji.midware.data.model.P3.DataRcGetWifiFreqInfo;
import dji.midware.data.model.P3.DataRcRequestGimbalCtrPermission;
import dji.midware.data.model.P3.DataRcSetAppSpecialControl;
import dji.midware.data.model.P3.DataRcSetCalibration;
import dji.midware.data.model.P3.DataRcSetConnectMaster;
import dji.midware.data.model.P3.DataRcSetControlMode;
import dji.midware.data.model.P3.DataRcSetCustomFuction;
import dji.midware.data.model.P3.DataRcSetFlightChannel;
import dji.midware.data.model.P3.DataRcSetFrequency;
import dji.midware.data.model.P3.DataRcSetGimbalControlMode;
import dji.midware.data.model.P3.DataRcSetGimbalSpeed;
import dji.midware.data.model.P3.DataRcSetMaster;
import dji.midware.data.model.P3.DataRcSetName;
import dji.midware.data.model.P3.DataRcSetPassword;
import dji.midware.data.model.P3.DataRcSetRcUnitNLang;
import dji.midware.data.model.P3.DataRcSetSearchMode;
import dji.midware.data.model.P3.DataRcSetSlaveMode;
import dji.midware.data.model.P3.DataRcSetSlavePermission;
import dji.midware.data.model.P3.DataRcSetToggle;
import dji.midware.data.model.P3.DataRcSetWheelGain;
import dji.midware.data.model.common.DataAbstractGetPushActiveStatus;
import dji.midware.data.model.common.DataCommonActiveGetVer;
import dji.midware.interfaces.DJIDataCallBack;
import dji.midware.util.BytesUtil;
import dji.midware.util.DJIEventBusUtil;
import dji.sdksharedlib.extension.CacheHelper;
import dji.sdksharedlib.extension.KeyHelper;
import dji.sdksharedlib.hardware.abstractions.Action;
import dji.sdksharedlib.hardware.abstractions.DJISDKCacheHWAbstraction;
import dji.sdksharedlib.hardware.abstractions.Getter;
import dji.sdksharedlib.hardware.abstractions.Setter;
import dji.sdksharedlib.hardware.abstractions.remotecontroller.merge.CalibrationStatusMergeGet;
import dji.sdksharedlib.hardware.extension.DJISDKCacheCommonMergeCallback;
import dji.sdksharedlib.keycatalog.DJISDKCacheKey;
import dji.sdksharedlib.keycatalog.DJISDKCacheKeys;
import dji.sdksharedlib.keycatalog.RemoteControllerKeys;
import dji.sdksharedlib.listener.DJIParamAccessListener;
import dji.sdksharedlib.store.DJISDKCacheParamValue;
import dji.sdksharedlib.store.DJISDKCacheStoreLayer;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Pattern;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

@EXClassNullAway
public class DJIRCAbstraction extends DJISDKCacheHWAbstraction implements DJIParamAccessListener {
    private static final int CALLBACK_TIMEOUT = 60;
    private static final int CHANNEL_NUM = 4;
    public static final String DEFAULT_FIRMWARE_VERSION = "N/A";
    protected static final int DIAL_WHEEL_OFFSET = 1024;
    public static final String DisplayNameCendence = "Cendence Remote Controller";
    public static final String DisplayNameInspire1 = "Inspire 1 Remote Controller";
    public static final String DisplayNameInspire2 = "Inspire2 Remote Controller";
    public static final String DisplayNameLightbridge2 = "Lightbridge2 Remote Controller";
    public static final String DisplayNameMavic2 = "Mavic 2 Remote Controller";
    public static final String DisplayNameMavic2Enterprise = "Mavic 2 Enterprise Remote Controller";
    public static final String DisplayNameMavicAir = "Mavic Air Remote Controller";
    public static final String DisplayNameMavicPro = "Mavic Pro Remote Controller";
    public static final String DisplayNamePhantom3Professinal = "Phantom 3 Professional Remote Controller";
    public static final String DisplayNamePhantom3Standard = "Phantom 3S Remote Controller";
    public static final String DisplayNamePhantom4Advanced = "Phantom 4 Advanced Remote Controller";
    public static final String DisplayNamePhantom4Pro = "Phantom 4 Pro Remote Controller";
    public static final String DisplayNamePhantom4ProV2 = "Phantom 4 Pro V2 Remote Controller";
    public static final String DisplayNameSpark = "Spark Remote Controller";
    public static final String DisplayNameWM160 = "WM160 Remote Controller";
    private static final String TAG = "DJIRCAbstraction";
    private CalibrationStatusMergeGet calibrationStatusMergeGet;
    private TimerTask changeRemoteFocusConnectedTask;
    /* access modifiers changed from: private */
    public Timer changeRemoteFocusConnectedTimer;
    protected HardwareState hardwareState = new HardwareState.Builder().build();
    /* access modifiers changed from: private */
    public long lastPushTime = -1;
    protected ArrayList<DataRcSetControlMode.ChannelCustomModel> preference = null;
    private ChargeRemaining rcBatteryInfo;
    protected boolean remoteFocusCheckingSupported = false;
    protected boolean supportMasterSlaveMode = false;
    protected boolean supportMasterSlaveModeV2 = false;

    /* access modifiers changed from: protected */
    public String getComponentDisplayName() {
        return "Remote Controller";
    }

    /* access modifiers changed from: protected */
    public HardwareState.RightWheel getRightWheel(DataRcGetPushParams params) {
        int i;
        boolean isPresent = this.hardwareState.getRightWheel().isPresent();
        boolean isWheelChanged = params.isWheelChanged();
        boolean isWheelBtnDown = params.isWheelBtnDown();
        if (params.isWheelPositive()) {
            i = params.getWheelOffset();
        } else {
            i = -params.getWheelOffset();
        }
        return new HardwareState.RightWheel(isPresent, isWheelChanged, isWheelBtnDown, i);
    }

    public void init(String component, int index, DJISDKCacheStoreLayer storeLayer, DJISDKCacheHWAbstraction.OnValueChangeListener onValueChangeListener) {
        super.init(component, index, storeLayer, onValueChangeListener);
        this.calibrationStatusMergeGet = new CalibrationStatusMergeGet();
        DJIEventBusUtil.register(this);
    }

    public void syncPushDataFromMidware() {
        super.syncPushDataFromMidware();
        initializeCustomizedKey();
        if (DataRcGetPushParams.getInstance().isGetted()) {
            onEvent3BackgroundThread(DataRcGetPushParams.getInstance());
        }
        if (DataRcGetPushGpsInfo.getInstance().isGetted()) {
            onEvent3BackgroundThread(DataRcGetPushGpsInfo.getInstance());
        }
        if (DataRcGetPushBatteryInfo.getInstance().isGetted()) {
            onEvent3BackgroundThread(DataRcGetPushBatteryInfo.getInstance());
        }
        if (DataRcGetPushFollowFocus.getInstance().isGetted()) {
            onEvent3BackgroundThread(DataRcGetPushFollowFocus.getInstance());
        }
        if (DataRcGetPushFollowFocus2.getInstance().isGetted()) {
            onEvent3BackgroundThread(DataRcGetPushFollowFocus2.getInstance());
        }
        if (DataOsdGetPushCheckStatus.getInstance().isGetted()) {
            onEvent3BackgroundThread(DataOsdGetPushCheckStatus.getInstance());
        }
        notifyValueChangeForKeyPathFromSetter(getComponentDisplayName(), "DisplayName");
        CacheHelper.addRemoteControllerListener(this, RemoteControllerKeys.STICK_MAPPING);
        notifyValueChangeForKeyPath(Boolean.valueOf(isMultiDevicePairingSupported()), RemoteControllerKeys.IS_MULTI_DEVICE_PAIRING_SUPPORTED);
    }

    public void onValueChange(DJISDKCacheKey key, DJISDKCacheParamValue oldValue, DJISDKCacheParamValue newValue) {
        if (newValue != null && newValue.getData() != null) {
            this.preference = (ArrayList) CacheHelper.getRemoteController(RemoteControllerKeys.STICK_MAPPING);
        }
    }

    public void destroy() {
        DJIEventBusUtil.unRegister(this);
        this.calibrationStatusMergeGet = null;
        super.destroy();
    }

    /* access modifiers changed from: protected */
    public Class<? extends DJISDKCacheKeys> getDJISDKCacheKeysClass() {
        return RemoteControllerKeys.class;
    }

    /* access modifiers changed from: protected */
    public void initializeComponentCharacteristics() {
        addCharacteristics(RemoteControllerKeys.class, getClass());
    }

    /* access modifiers changed from: protected */
    public boolean isMultiDevicePairingSupported() {
        return false;
    }

    private static boolean isDecString(String str) {
        return Pattern.compile("[0-9]*").matcher(str).matches();
    }

    @Action(RemoteControllerKeys.STOP_PAIRING)
    public void exitRCPairingMode(final DJISDKCacheHWAbstraction.InnerCallback callback) {
        DataRcSetFrequency.getInstance().setMode(DataRcSetFrequency.FreqMode.Cancel).start(new DJIDataCallBack() {
            /* class dji.sdksharedlib.hardware.abstractions.remotecontroller.DJIRCAbstraction.AnonymousClass1 */

            public void onSuccess(Object model) {
                if (callback != null) {
                    callback.onSuccess(null);
                }
            }

            public void onFailure(Ccode ccode) {
                if (callback != null) {
                    callback.onFails(DJIRemoteControllerError.getDJIError(ccode));
                }
            }
        });
    }

    @Setter(RemoteControllerKeys.CUSTOM_BUTTON_TAGS)
    public void setRCCustomButtonTag(CustomButtonTags tag, final DJISDKCacheHWAbstraction.InnerCallback callback) {
        if (this.hardwareState.getC1Button().isPresent() || this.hardwareState.getC2Button().isPresent()) {
            CustomButtonTags param = tag;
            DataRcSetCustomFuction.getInstance().setC1(param.getC1ButtonTag()).setC2(param.getC2ButtonTag()).start(new DJIDataCallBack() {
                /* class dji.sdksharedlib.hardware.abstractions.remotecontroller.DJIRCAbstraction.AnonymousClass2 */

                public void onSuccess(Object model) {
                    if (callback != null) {
                        callback.onSuccess(null);
                    }
                }

                public void onFailure(Ccode ccode) {
                    if (callback != null) {
                        callback.onFails(DJIRemoteControllerError.getDJIError(ccode));
                    }
                }
            });
            return;
        }
        CallbackUtils.onFailure(callback, DJIRemoteControllerError.COMMON_UNSUPPORTED);
    }

    @Getter(RemoteControllerKeys.CUSTOM_BUTTON_TAGS)
    public void getRCCustomButtonTag(final DJISDKCacheHWAbstraction.InnerCallback callback) {
        if (this.hardwareState.getC1Button().isPresent() || this.hardwareState.getC2Button().isPresent()) {
            DataRcGetCustomFuction.getInstance().start(new DJIDataCallBack() {
                /* class dji.sdksharedlib.hardware.abstractions.remotecontroller.DJIRCAbstraction.AnonymousClass3 */

                public void onSuccess(Object model) {
                    CustomButtonTags tag = new CustomButtonTags.Builder().c1ButtonTag((short) DataRcGetCustomFuction.getInstance().getC1()).c2ButtonTag((short) DataRcGetCustomFuction.getInstance().getC2()).build();
                    if (callback != null) {
                        callback.onSuccess(tag);
                    }
                }

                public void onFailure(Ccode ccode) {
                    if (callback != null) {
                        callback.onFails(DJIRemoteControllerError.getDJIError(ccode));
                    }
                }
            });
        } else {
            CallbackUtils.onFailure(callback, DJIRemoteControllerError.COMMON_UNSUPPORTED);
        }
    }

    @Setter(RemoteControllerKeys.LEFT_WHEEL_GIMBAL_CONTROL_AXIS)
    public void setLeftWheelGimbalControlAxis(GimbalAxis value, final DJISDKCacheHWAbstraction.InnerCallback callback) {
        if (value != null) {
            DataRcSetGimbalControlMode setGimbalControlMode = DataRcSetGimbalControlMode.getInstance();
            setGimbalControlMode.setMode(DataRcSetGimbalControlMode.MODE.find(value.value()));
            setGimbalControlMode.start(new DJIDataCallBack() {
                /* class dji.sdksharedlib.hardware.abstractions.remotecontroller.DJIRCAbstraction.AnonymousClass4 */

                public void onSuccess(Object model) {
                    if (callback != null) {
                        callback.onSuccess(null);
                    }
                }

                public void onFailure(Ccode ccode) {
                    if (callback != null) {
                        callback.onFails(DJIRemoteControllerError.getDJIError(ccode));
                    }
                }
            });
        } else if (callback != null) {
            callback.onFails(DJIRemoteControllerError.getDJIError(Ccode.INVALID_PARAM));
        }
    }

    @Getter(RemoteControllerKeys.LEFT_WHEEL_GIMBAL_CONTROL_AXIS)
    public void getLeftWheelGimbalControlAxis(final DJISDKCacheHWAbstraction.InnerCallback callback) {
        final DataRcGetGimbalControlMode getGimbalControlMode = DataRcGetGimbalControlMode.getInstance();
        getGimbalControlMode.start(new DJIDataCallBack() {
            /* class dji.sdksharedlib.hardware.abstractions.remotecontroller.DJIRCAbstraction.AnonymousClass5 */

            public void onSuccess(Object model) {
                if (callback != null) {
                    callback.onSuccess(GimbalAxis.find(getGimbalControlMode.getMode().value()));
                }
            }

            public void onFailure(Ccode ccode) {
                if (callback != null) {
                    callback.onFails(DJIRemoteControllerError.getDJIError(ccode));
                }
            }
        });
    }

    @Setter(RemoteControllerKeys.AIRCRAFT_MAPPING_STYLE)
    public void setAircraftMappingStyle(AircraftMappingStyle value, final DJISDKCacheHWAbstraction.InnerCallback callback) {
        if (value == null) {
            CallbackUtils.onFailure(callback, DJIRemoteControllerError.COMMON_PARAM_ILLEGAL);
            return;
        }
        AircraftMappingStyle mode = value;
        if (mode != AircraftMappingStyle.STYLE_CUSTOM) {
            DataRcSetControlMode.getInstance().setControlType(DataRcSetControlMode.ControlMode.find(mode.value())).setChannels(this.preference).start(new DJIDataCallBack() {
                /* class dji.sdksharedlib.hardware.abstractions.remotecontroller.DJIRCAbstraction.AnonymousClass6 */

                public void onSuccess(Object model) {
                    if (callback != null) {
                        callback.onSuccess(null);
                    }
                }

                public void onFailure(Ccode ccode) {
                    if (callback != null) {
                        callback.onFails(DJIRemoteControllerError.getDJIError(ccode));
                    }
                }
            });
        } else {
            DataRcSetControlMode.getInstance().setControlType(DataRcSetControlMode.ControlMode.Custom).setChannels(this.preference).start(new DJIDataCallBack() {
                /* class dji.sdksharedlib.hardware.abstractions.remotecontroller.DJIRCAbstraction.AnonymousClass7 */

                public void onSuccess(Object model) {
                    if (callback != null) {
                        callback.onSuccess(null);
                    }
                }

                public void onFailure(Ccode ccode) {
                    if (callback != null) {
                        CallbackUtils.onFailure(callback, DJIRemoteControllerError.getDJIError(ccode));
                    }
                }
            });
        }
    }

    @Getter(RemoteControllerKeys.AIRCRAFT_MAPPING_STYLE)
    public void getAircraftMappingStyle(final DJISDKCacheHWAbstraction.InnerCallback callback) {
        DataRcGetControlMode.getInstance().start(new DJIDataCallBack() {
            /* class dji.sdksharedlib.hardware.abstractions.remotecontroller.DJIRCAbstraction.AnonymousClass8 */

            public void onSuccess(Object model) {
                AircraftMappingStyle aircraftMappingStyle = AircraftMappingStyle.STYLE_2;
                AircraftMappingStyle mode = AircraftMappingStyle.find(DataRcGetControlMode.getInstance().getControlType().value());
                if (callback != null) {
                    callback.onSuccess(mode);
                }
            }

            public void onFailure(Ccode ccode) {
                if (callback != null) {
                    CallbackUtils.onFailure(callback, DJIRemoteControllerError.getDJIError(ccode));
                }
            }
        });
    }

    @Setter(RemoteControllerKeys.AIRCRAFT_CUSTOM_MAPPING)
    public void setCustomAircraftMapping(AircraftMapping value, final DJISDKCacheHWAbstraction.InnerCallback callback) {
        int i;
        int i2;
        int i3;
        int i4 = 1;
        if (value == null) {
            CallbackUtils.onFailure(callback, DJIRemoteControllerError.getDJIError(Ccode.INVALID_PARAM));
            return;
        }
        AircraftMapping mode = value;
        if (!mode.aircraftMappingStyle._equals(4)) {
            CallbackUtils.onFailure(callback, DJIRemoteControllerError.COMMON_PARAM_ILLEGAL);
        } else if (mode.aircraftMappingStyle._equals(4)) {
            ArrayList<DataRcSetControlMode.ChannelCustomModel> preference2 = new ArrayList<>(4);
            if (mode.leftHorizontal != null && mode.leftVertical != null && mode.rightHorizontal != null && mode.rightVertical != null) {
                DataRcSetControlMode.ChannelCustomModel model1 = new DataRcSetControlMode.ChannelCustomModel();
                model1.function = mode.rightHorizontal.getMappingTarget().value();
                if (mode.rightHorizontal.isReversed()) {
                    i = 1;
                } else {
                    i = 0;
                }
                model1.direction = i;
                preference2.add(model1);
                DataRcSetControlMode.ChannelCustomModel model2 = new DataRcSetControlMode.ChannelCustomModel();
                model2.function = mode.rightVertical.getMappingTarget().value();
                if (mode.rightVertical.isReversed()) {
                    i2 = 1;
                } else {
                    i2 = 0;
                }
                model2.direction = i2;
                preference2.add(model2);
                DataRcSetControlMode.ChannelCustomModel model3 = new DataRcSetControlMode.ChannelCustomModel();
                model3.function = mode.leftVertical.getMappingTarget().value();
                if (mode.leftVertical.isReversed()) {
                    i3 = 1;
                } else {
                    i3 = 0;
                }
                model3.direction = i3;
                preference2.add(model3);
                DataRcSetControlMode.ChannelCustomModel model4 = new DataRcSetControlMode.ChannelCustomModel();
                model4.function = mode.leftHorizontal.getMappingTarget().value();
                if (!mode.leftHorizontal.isReversed()) {
                    i4 = 0;
                }
                model4.direction = i4;
                preference2.add(model4);
                DataRcSetControlMode.getInstance().setControlType(DataRcSetControlMode.ControlMode.Custom).setChannels(preference2).start(new DJIDataCallBack() {
                    /* class dji.sdksharedlib.hardware.abstractions.remotecontroller.DJIRCAbstraction.AnonymousClass9 */

                    public void onSuccess(Object model) {
                        if (callback != null) {
                            callback.onSuccess(null);
                        }
                    }

                    public void onFailure(Ccode ccode) {
                        if (callback != null) {
                            CallbackUtils.onFailure(callback, DJIRemoteControllerError.getDJIError(ccode));
                        }
                    }
                });
            } else if (callback != null) {
                CallbackUtils.onFailure(callback, DJIRemoteControllerError.getDJIError(Ccode.INVALID_PARAM));
            }
        }
    }

    @Getter(RemoteControllerKeys.AIRCRAFT_CUSTOM_MAPPING)
    public void getCustomAircraftMapping(final DJISDKCacheHWAbstraction.InnerCallback callback) {
        DataRcGetControlMode.getInstance().start(new DJIDataCallBack() {
            /* class dji.sdksharedlib.hardware.abstractions.remotecontroller.DJIRCAbstraction.AnonymousClass10 */

            public void onSuccess(Object model) {
                boolean z;
                boolean z2;
                boolean z3 = true;
                AircraftMapping mode = new AircraftMapping(AircraftMappingStyle.STYLE_2);
                mode.aircraftMappingStyle = AircraftMappingStyle.find(DataRcGetControlMode.getInstance().getControlType().value());
                if (mode.aircraftMappingStyle._equals(1)) {
                    mode = new AircraftMapping(AircraftMappingStyle.STYLE_1);
                } else if (mode.aircraftMappingStyle._equals(2)) {
                    mode = new AircraftMapping(AircraftMappingStyle.STYLE_2);
                } else if (mode.aircraftMappingStyle._equals(3)) {
                    mode = new AircraftMapping(AircraftMappingStyle.STYLE_3);
                } else if (mode.aircraftMappingStyle._equals(4)) {
                    ArrayList<DataRcSetControlMode.ChannelCustomModel> list = DataRcGetControlMode.getInstance().getChannels();
                    mode.rightHorizontal = new AircraftStickMapping.Builder().mappingTarget(AircraftStickMappingTarget.find(list.get(0).function)).isReversed(list.get(0).direction == 1).build();
                    AircraftStickMapping.Builder mappingTarget = new AircraftStickMapping.Builder().mappingTarget(AircraftStickMappingTarget.find(list.get(1).function));
                    if (list.get(1).direction == 1) {
                        z = true;
                    } else {
                        z = false;
                    }
                    mode.rightVertical = mappingTarget.isReversed(z).build();
                    AircraftStickMapping.Builder mappingTarget2 = new AircraftStickMapping.Builder().mappingTarget(AircraftStickMappingTarget.find(list.get(2).function));
                    if (list.get(2).direction == 1) {
                        z2 = true;
                    } else {
                        z2 = false;
                    }
                    mode.leftVertical = mappingTarget2.isReversed(z2).build();
                    AircraftStickMapping.Builder mappingTarget3 = new AircraftStickMapping.Builder().mappingTarget(AircraftStickMappingTarget.find(list.get(3).function));
                    if (list.get(3).direction != 1) {
                        z3 = false;
                    }
                    mode.leftHorizontal = mappingTarget3.isReversed(z3).build();
                }
                if (callback != null) {
                    callback.onSuccess(mode);
                }
            }

            public void onFailure(Ccode ccode) {
                if (callback != null) {
                    CallbackUtils.onFailure(callback, DJIRemoteControllerError.getDJIError(ccode));
                }
            }
        });
    }

    @Setter("Password")
    public void setRCPassword(String password, final DJISDKCacheHWAbstraction.InnerCallback callback) {
        if (password == null || password.length() != 4 || !isDecString(password)) {
            CallbackUtils.onFailure(callback, DJIRemoteControllerError.COMMON_PARAM_ILLEGAL);
        } else {
            DataRcSetPassword.getInstance().setPw(Integer.parseInt(password)).start(new DJIDataCallBack() {
                /* class dji.sdksharedlib.hardware.abstractions.remotecontroller.DJIRCAbstraction.AnonymousClass11 */

                public void onSuccess(Object model) {
                    if (callback != null) {
                        callback.onSuccess(null);
                    }
                }

                public void onFailure(Ccode ccode) {
                    CallbackUtils.onFailure(callback, DJIRemoteControllerError.getDJIError(ccode));
                }
            });
        }
    }

    @Getter("Password")
    public void getRCPassword(final DJISDKCacheHWAbstraction.InnerCallback callback) {
        DataRcGetPassword.getInstance().start(new DJIDataCallBack() {
            /* class dji.sdksharedlib.hardware.abstractions.remotecontroller.DJIRCAbstraction.AnonymousClass12 */

            public void onSuccess(Object model) {
                int result = DataRcGetPassword.getInstance().getPw();
                if (result > 9999) {
                    result = 9999;
                }
                if (result < 0) {
                    result = 0;
                }
                NumberFormat nf = NumberFormat.getInstance();
                nf.setGroupingUsed(false);
                nf.setMaximumIntegerDigits(4);
                nf.setMinimumIntegerDigits(4);
                String resultString = nf.format((long) result);
                if (callback != null) {
                    callback.onSuccess(resultString);
                }
            }

            public void onFailure(Ccode ccode) {
                if (callback != null) {
                    callback.onFails(DJIRemoteControllerError.getDJIError(ccode));
                }
            }
        });
    }

    @Getter("Name")
    public void getRCName(final DJISDKCacheHWAbstraction.InnerCallback callback) {
        DataRcGetName.getInstance().start(new DJIDataCallBack() {
            /* class dji.sdksharedlib.hardware.abstractions.remotecontroller.DJIRCAbstraction.AnonymousClass13 */

            public void onSuccess(Object model) {
                CallbackUtils.onSuccess(callback, DataRcGetName.getInstance().getName());
            }

            public void onFailure(Ccode ccode) {
                CallbackUtils.onFailure(callback, ccode);
            }
        });
    }

    @Setter("Name")
    public void setRCName(String value, final DJISDKCacheHWAbstraction.InnerCallback callback) {
        String name = value;
        if (name == null || BytesUtil.getBytesUTF8(name).length > 6) {
            CallbackUtils.onFailure(callback, DJIRemoteControllerError.getDJIError(Ccode.INVALID_PARAM));
        } else {
            DataRcSetName.getInstance().setName(name).start(new DJIDataCallBack() {
                /* class dji.sdksharedlib.hardware.abstractions.remotecontroller.DJIRCAbstraction.AnonymousClass14 */

                public void onSuccess(Object model) {
                    CallbackUtils.onSuccess(callback, (Object) null);
                }

                public void onFailure(Ccode ccode) {
                    CallbackUtils.onFailure(callback, ccode);
                }
            });
        }
    }

    @Getter(RemoteControllerKeys.REQUEST_LEGACY_GIMBAL_CONTROL)
    public void requestSlaveGimbalControlRight(final DJISDKCacheHWAbstraction.InnerCallback callback) {
        if (this.supportMasterSlaveMode || this.supportMasterSlaveModeV2) {
            DataRcRequestGimbalCtrPermission.getInstance().start(new DJIDataCallBack() {
                /* class dji.sdksharedlib.hardware.abstractions.remotecontroller.DJIRCAbstraction.AnonymousClass15 */

                public void onSuccess(Object model) {
                    if (callback != null) {
                        callback.onSuccess(RequestGimbalControlResult.find(0));
                    }
                }

                public void onFailure(Ccode ccode) {
                    if (ccode != Ccode.SUCCEED) {
                        DataRcRequestGimbalCtrPermission.RcGimbalError error = DataRcRequestGimbalCtrPermission.getInstance().getError(ccode);
                        if (callback == null) {
                            return;
                        }
                        if (error != null) {
                            callback.onSuccess(RequestGimbalControlResult.find(error.value()));
                        } else {
                            callback.onFails(DJIRemoteControllerError.getDJIError(ccode));
                        }
                    } else if (callback != null) {
                        callback.onSuccess(RequestGimbalControlResult.find(1));
                    }
                }
            });
        } else {
            CallbackUtils.onFailure(callback, DJIRemoteControllerError.COMMON_UNSUPPORTED);
        }
    }

    @Getter(RemoteControllerKeys.GIMBAL_CONTROL_SPEED_COEFFICIENT)
    public void getSlaveJoystickControlGimbalSpeed(final DJISDKCacheHWAbstraction.InnerCallback callback) {
        if (this.supportMasterSlaveMode || this.supportMasterSlaveModeV2) {
            final DataRcGetGimbalSpeed getSpeed = DataRcGetGimbalSpeed.getInstance();
            getSpeed.start(new DJIDataCallBack() {
                /* class dji.sdksharedlib.hardware.abstractions.remotecontroller.DJIRCAbstraction.AnonymousClass16 */

                public void onSuccess(Object model) {
                    CallbackUtils.onSuccess(callback, new GimbalControlSpeedCoefficient((short) getSpeed.getPitch(), (short) getSpeed.getRoll(), (short) getSpeed.getYaw()));
                }

                public void onFailure(Ccode ccode) {
                    CallbackUtils.onFailure(callback, DJIRemoteControllerError.getDJIError(ccode));
                }
            });
        } else if (callback != null) {
            CallbackUtils.onFailure(callback, DJIRemoteControllerError.getDJIError(Ccode.NOT_SUPPORT_CURRENT_STATE));
        }
    }

    @Setter(RemoteControllerKeys.GIMBAL_CONTROL_SPEED_COEFFICIENT)
    public void setSlaveJoystickControlGimbalSpeed(GimbalControlSpeedCoefficient gimbalControlSpeedCoefficient, DJISDKCacheHWAbstraction.InnerCallback callback) {
        if (this.supportMasterSlaveMode || this.supportMasterSlaveModeV2) {
            DataRcSetGimbalSpeed setSpeed = DataRcSetGimbalSpeed.getInstance();
            setSpeed.setPitch(gimbalControlSpeedCoefficient.getPitchSpeedCoefficient());
            setSpeed.setRoll(gimbalControlSpeedCoefficient.getRollSpeedCoefficient());
            setSpeed.setYaw(gimbalControlSpeedCoefficient.getYawSpeedCoefficient());
            setSpeed.start(CallbackUtils.defaultCB(callback, DJIRemoteControllerError.class));
        } else if (callback != null) {
            CallbackUtils.onFailure(callback, DJIRemoteControllerError.getDJIError(Ccode.NOT_SUPPORT_CURRENT_STATE));
        }
    }

    @Setter(RemoteControllerKeys.GIMBAL_MAPPING_STYLE)
    public void setGimbalMappingStyle(GimbalMappingStyle style, DJISDKCacheHWAbstraction.InnerCallback callback) {
        DataRcSetSlaveMode.ControlMode controlMode;
        if (style == null) {
            CallbackUtils.onFailure(callback, DJIRemoteControllerError.COMMON_PARAM_ILLEGAL);
        } else if (this.supportMasterSlaveMode || this.supportMasterSlaveModeV2) {
            switch (style) {
                case CUSTOM:
                    controlMode = DataRcSetSlaveMode.ControlMode.Custom;
                    break;
                case DEFAULT:
                    controlMode = DataRcSetSlaveMode.ControlMode.Default;
                    break;
                default:
                    controlMode = DataRcSetSlaveMode.ControlMode.Default;
                    break;
            }
            if (controlMode == DataRcSetSlaveMode.ControlMode.Default) {
                DataRcSetSlaveMode.getInstance().setControlType(controlMode).start(CallbackUtils.defaultCB(callback, DJIRemoteControllerError.class));
                return;
            }
            DataRcSetSlaveMode.getInstance().setControlType(controlMode).setChannels(DataRcGetSlaveMode.getInstance().getChannels()).start(CallbackUtils.defaultCB(callback, DJIRemoteControllerError.class));
        } else {
            CallbackUtils.onFailure(callback, DJIRemoteControllerError.COMMON_UNSUPPORTED);
        }
    }

    @Getter(RemoteControllerKeys.GIMBAL_MAPPING_STYLE)
    public void getGimbalMappingStyle(final DJISDKCacheHWAbstraction.InnerCallback callback) {
        if (this.supportMasterSlaveMode || this.supportMasterSlaveModeV2) {
            DataRcGetSlaveMode.getInstance().start(new DJIDataCallBack() {
                /* class dji.sdksharedlib.hardware.abstractions.remotecontroller.DJIRCAbstraction.AnonymousClass17 */

                public void onSuccess(Object model) {
                    CallbackUtils.onSuccess(callback, DataRcGetSlaveMode.getInstance().getControlType().value() == 0 ? GimbalMappingStyle.DEFAULT : GimbalMappingStyle.CUSTOM);
                }

                public void onFailure(Ccode ccode) {
                    CallbackUtils.onFailure(callback, DJIRemoteControllerError.getDJIError(ccode));
                }
            });
        } else {
            CallbackUtils.onFailure(callback, DJIRemoteControllerError.getDJIError(Ccode.NOT_SUPPORT_CURRENT_STATE));
        }
    }

    @Setter(RemoteControllerKeys.CUSTOM_GIMBAL_MAPPING)
    public void setCustomGimbalMapping(GimbalMapping style, DJISDKCacheHWAbstraction.InnerCallback callback) {
        DataRcSetSlaveMode.ControlMode controlMode;
        if (style == null) {
            CallbackUtils.onFailure(callback, DJIRemoteControllerError.getDJIError(Ccode.INVALID_PARAM));
        } else if (this.supportMasterSlaveMode || this.supportMasterSlaveModeV2) {
            switch (style.gimbalMappingStyle) {
                case CUSTOM:
                    controlMode = DataRcSetSlaveMode.ControlMode.Custom;
                    break;
                case DEFAULT:
                    controlMode = DataRcSetSlaveMode.ControlMode.Default;
                    break;
                default:
                    controlMode = DataRcSetSlaveMode.ControlMode.Default;
                    break;
            }
            if (controlMode == DataRcSetSlaveMode.ControlMode.Default) {
                DataRcSetSlaveMode.getInstance().setControlType(controlMode).start(CallbackUtils.defaultCB(callback, DJIRemoteControllerError.class));
                return;
            }
            ArrayList<DataRcSetSlaveMode.SlaveCustomModel> arrayList = new ArrayList<>();
            arrayList.add(convertGimbalStickMappingToProtocolModel(style.rightHorizontal));
            arrayList.add(convertGimbalStickMappingToProtocolModel(style.rightVertical));
            arrayList.add(convertGimbalStickMappingToProtocolModel(style.leftVertical));
            arrayList.add(convertGimbalStickMappingToProtocolModel(style.leftHorizontal));
            DataRcSetSlaveMode.getInstance().setControlType(controlMode).setChannels(arrayList).start(CallbackUtils.defaultCB(callback, DJIRemoteControllerError.class));
        } else {
            CallbackUtils.onFailure(callback, DJIRemoteControllerError.getDJIError(Ccode.NOT_SUPPORT_CURRENT_STATE));
        }
    }

    @Getter(RemoteControllerKeys.CUSTOM_GIMBAL_MAPPING)
    public void getCustomGimbalMapping(final DJISDKCacheHWAbstraction.InnerCallback callback) {
        if (this.supportMasterSlaveMode || this.supportMasterSlaveModeV2) {
            DataRcGetSlaveMode.getInstance().start(new DJIDataCallBack() {
                /* class dji.sdksharedlib.hardware.abstractions.remotecontroller.DJIRCAbstraction.AnonymousClass18 */

                public void onSuccess(Object model) {
                    boolean z;
                    boolean z2;
                    boolean z3;
                    boolean z4 = true;
                    GimbalMapping mode = new GimbalMapping();
                    mode.gimbalMappingStyle = DataRcGetSlaveMode.getInstance().getControlType().value() == 0 ? GimbalMappingStyle.DEFAULT : GimbalMappingStyle.CUSTOM;
                    if (mode.gimbalMappingStyle == GimbalMappingStyle.CUSTOM) {
                        ArrayList<DataRcSetSlaveMode.SlaveCustomModel> list = DataRcGetSlaveMode.getInstance().getChannels();
                        GimbalStickMapping.Builder mappingTarget = new GimbalStickMapping.Builder().mappingTarget(GimbalStickMappingTarget.find(list.get(0).function));
                        if (list.get(0).direction == 1) {
                            z = true;
                        } else {
                            z = false;
                        }
                        mode.rightHorizontal = mappingTarget.isReversed(z).build();
                        GimbalStickMapping.Builder mappingTarget2 = new GimbalStickMapping.Builder().mappingTarget(GimbalStickMappingTarget.find(list.get(1).function));
                        if (list.get(1).direction == 1) {
                            z2 = true;
                        } else {
                            z2 = false;
                        }
                        mode.rightVertical = mappingTarget2.isReversed(z2).build();
                        GimbalStickMapping.Builder mappingTarget3 = new GimbalStickMapping.Builder().mappingTarget(GimbalStickMappingTarget.find(list.get(2).function));
                        if (list.get(2).direction == 1) {
                            z3 = true;
                        } else {
                            z3 = false;
                        }
                        mode.leftVertical = mappingTarget3.isReversed(z3).build();
                        GimbalStickMapping.Builder mappingTarget4 = new GimbalStickMapping.Builder().mappingTarget(GimbalStickMappingTarget.find(list.get(3).function));
                        if (list.get(3).direction != 1) {
                            z4 = false;
                        }
                        mode.leftHorizontal = mappingTarget4.isReversed(z4).build();
                    }
                    CallbackUtils.onSuccess(callback, mode);
                }

                public void onFailure(Ccode ccode) {
                    CallbackUtils.onFailure(callback, DJIRemoteControllerError.getDJIError(ccode));
                }
            });
        } else {
            CallbackUtils.onFailure(callback, DJIRemoteControllerError.getDJIError(Ccode.INVALID_PARAM));
        }
    }

    private DataRcSetSlaveMode.SlaveCustomModel convertGimbalStickMappingToProtocolModel(GimbalStickMapping mapping) {
        DataRcSetSlaveMode.SlaveCustomModel model = new DataRcSetSlaveMode.SlaveCustomModel();
        if (!(mapping == null || mapping.getMappingTarget() == null)) {
            model.function = mapping.getMappingTarget().value();
            model.direction = mapping.isReversed() ? 1 : 0;
        }
        return model;
    }

    @Setter(RemoteControllerKeys.CALIBRATION_STATE)
    public void setRemoteControllerCalibrationMode(CalibrationState mode, final DJISDKCacheHWAbstraction.InnerCallback callback) {
        DataRcSetCalibration.getInstance().setMode(DataRcSetCalibration.MODE.find(mode.value())).start(new DJIDataCallBack() {
            /* class dji.sdksharedlib.hardware.abstractions.remotecontroller.DJIRCAbstraction.AnonymousClass19 */

            public void onSuccess(Object model) {
                callback.onSuccess(model);
            }

            public void onFailure(Ccode ccode) {
                callback.onFails(DJIRemoteControllerError.getDJIError(ccode));
            }
        });
    }

    @Getter(RemoteControllerKeys.REMOTE_CONTROLLER_CALIBRATION_B_AXIS_STATUS)
    public void getRemoteControllerCalibrationBAxisStatus(final DJISDKCacheHWAbstraction.InnerCallback callback) {
        this.calibrationStatusMergeGet.getInfo(RemoteControllerKeys.REMOTE_CONTROLLER_CALIBRATION_B_AXIS_STATUS, new DJISDKCacheCommonMergeCallback() {
            /* class dji.sdksharedlib.hardware.abstractions.remotecontroller.DJIRCAbstraction.AnonymousClass20 */

            public void onSuccess(Object object) {
                CallbackUtils.onSuccess(callback, object);
            }

            public void onFailure(DJIError error) {
                CallbackUtils.onFailure(callback, error);
            }
        });
    }

    @Getter(RemoteControllerKeys.REMOTE_CONTROLLER_CALIBRATION_C_AXIS_STATUS)
    public void getRemoteControllerCalibrationCAxisStatus(final DJISDKCacheHWAbstraction.InnerCallback callback) {
        this.calibrationStatusMergeGet.getInfo(RemoteControllerKeys.REMOTE_CONTROLLER_CALIBRATION_C_AXIS_STATUS, new DJISDKCacheCommonMergeCallback() {
            /* class dji.sdksharedlib.hardware.abstractions.remotecontroller.DJIRCAbstraction.AnonymousClass21 */

            public void onSuccess(Object object) {
                CallbackUtils.onSuccess(callback, object);
            }

            public void onFailure(DJIError error) {
                CallbackUtils.onFailure(callback, error);
            }
        });
    }

    @Getter(RemoteControllerKeys.REMOTE_CONTROLLER_CALIBRATION_D_AXIS_STATUS)
    public void getRemoteControllerCalibrationDAxisStatus(final DJISDKCacheHWAbstraction.InnerCallback callback) {
        this.calibrationStatusMergeGet.getInfo(RemoteControllerKeys.REMOTE_CONTROLLER_CALIBRATION_D_AXIS_STATUS, new DJISDKCacheCommonMergeCallback() {
            /* class dji.sdksharedlib.hardware.abstractions.remotecontroller.DJIRCAbstraction.AnonymousClass22 */

            public void onSuccess(Object object) {
                CallbackUtils.onSuccess(callback, object);
            }

            public void onFailure(DJIError error) {
                CallbackUtils.onFailure(callback, error);
            }
        });
    }

    @Getter(RemoteControllerKeys.REMOTE_CONTROLLER_CALIBRATION_E_AXIS_STATUS)
    public void getRemoteControllerCalibrationEAxisStatus(final DJISDKCacheHWAbstraction.InnerCallback callback) {
        this.calibrationStatusMergeGet.getInfo(RemoteControllerKeys.REMOTE_CONTROLLER_CALIBRATION_E_AXIS_STATUS, new DJISDKCacheCommonMergeCallback() {
            /* class dji.sdksharedlib.hardware.abstractions.remotecontroller.DJIRCAbstraction.AnonymousClass23 */

            public void onSuccess(Object object) {
                CallbackUtils.onSuccess(callback, object);
            }

            public void onFailure(DJIError error) {
                CallbackUtils.onFailure(callback, error);
            }
        });
    }

    @Getter(RemoteControllerKeys.REMOTE_CONTROLLER_CALIBRATION_F_AXIS_STATUS)
    public void getRemoteControllerCalibrationFAxisStatus(final DJISDKCacheHWAbstraction.InnerCallback callback) {
        this.calibrationStatusMergeGet.getInfo(RemoteControllerKeys.REMOTE_CONTROLLER_CALIBRATION_F_AXIS_STATUS, new DJISDKCacheCommonMergeCallback() {
            /* class dji.sdksharedlib.hardware.abstractions.remotecontroller.DJIRCAbstraction.AnonymousClass24 */

            public void onSuccess(Object object) {
                CallbackUtils.onSuccess(callback, object);
            }

            public void onFailure(DJIError error) {
                CallbackUtils.onFailure(callback, error);
            }
        });
    }

    @Getter(RemoteControllerKeys.REMOTE_CONTROLLER_CALIBRATION_G_AXIS_STATUS)
    public void getRemoteControllerCalibrationGAxisStatus(final DJISDKCacheHWAbstraction.InnerCallback callback) {
        this.calibrationStatusMergeGet.getInfo(RemoteControllerKeys.REMOTE_CONTROLLER_CALIBRATION_G_AXIS_STATUS, new DJISDKCacheCommonMergeCallback() {
            /* class dji.sdksharedlib.hardware.abstractions.remotecontroller.DJIRCAbstraction.AnonymousClass25 */

            public void onSuccess(Object object) {
                CallbackUtils.onSuccess(callback, object);
            }

            public void onFailure(DJIError error) {
                CallbackUtils.onFailure(callback, error);
            }
        });
    }

    @Getter(RemoteControllerKeys.REMOTE_CONTROLLER_CALIBRATION_A_AXIS_STATUS)
    public void getRemoteControllerCalibrationAAxisStatus(final DJISDKCacheHWAbstraction.InnerCallback callback) {
        this.calibrationStatusMergeGet.getInfo(RemoteControllerKeys.REMOTE_CONTROLLER_CALIBRATION_A_AXIS_STATUS, new DJISDKCacheCommonMergeCallback() {
            /* class dji.sdksharedlib.hardware.abstractions.remotecontroller.DJIRCAbstraction.AnonymousClass26 */

            public void onSuccess(Object object) {
                CallbackUtils.onSuccess(callback, object);
            }

            public void onFailure(DJIError error) {
                CallbackUtils.onFailure(callback, error);
            }
        });
    }

    @Getter(RemoteControllerKeys.REMOTE_CONTROLLER_CALIBRATION_H_AXIS_STATUS)
    public void getRemoteControllerCalibrationHAxisStatus(final DJISDKCacheHWAbstraction.InnerCallback callback) {
        this.calibrationStatusMergeGet.getInfo(RemoteControllerKeys.REMOTE_CONTROLLER_CALIBRATION_H_AXIS_STATUS, new DJISDKCacheCommonMergeCallback() {
            /* class dji.sdksharedlib.hardware.abstractions.remotecontroller.DJIRCAbstraction.AnonymousClass27 */

            public void onSuccess(Object object) {
                CallbackUtils.onSuccess(callback, object);
            }

            public void onFailure(DJIError error) {
                CallbackUtils.onFailure(callback, error);
            }
        });
    }

    @Getter(RemoteControllerKeys.REMOTE_CONTROLLER_CALIBRATION_NUMBER_OF_SEGMENT)
    public void getRemoteControllerCalibrationSegmentNumber(final DJISDKCacheHWAbstraction.InnerCallback callback) {
        this.calibrationStatusMergeGet.getInfo(RemoteControllerKeys.REMOTE_CONTROLLER_CALIBRATION_NUMBER_OF_SEGMENT, new DJISDKCacheCommonMergeCallback() {
            /* class dji.sdksharedlib.hardware.abstractions.remotecontroller.DJIRCAbstraction.AnonymousClass28 */

            public void onSuccess(Object object) {
                CallbackUtils.onSuccess(callback, object);
            }

            public void onFailure(DJIError error) {
                CallbackUtils.onFailure(callback, error);
            }
        });
    }

    @Getter(RemoteControllerKeys.CONNECTED_MASTER_CREDENTIALS)
    public void getJoinedMasterNameAndPassword(final DJISDKCacheHWAbstraction.InnerCallback callback) {
        if (!this.supportMasterSlaveMode) {
            CallbackUtils.onFailure(callback, DJIRemoteControllerError.COMMON_UNSUPPORTED);
        } else {
            DataRcGetConnectMaster.getInstance().start(new DJIDataCallBack() {
                /* class dji.sdksharedlib.hardware.abstractions.remotecontroller.DJIRCAbstraction.AnonymousClass29 */

                public void onSuccess(Object model) {
                    int id = DataRcGetConnectMaster.getInstance().getMaster().id;
                    int password = DataRcGetConnectMaster.getInstance().getMaster().password;
                    if (password > 9999) {
                        password = 9999;
                    }
                    if (password < 0) {
                        password = 0;
                    }
                    NumberFormat nf = NumberFormat.getInstance();
                    nf.setGroupingUsed(false);
                    nf.setMaximumIntegerDigits(4);
                    nf.setMinimumIntegerDigits(4);
                    Credentials result = new Credentials(id, DataRcGetConnectMaster.getInstance().getMaster().name, nf.format((long) password));
                    if (callback != null) {
                        callback.onSuccess(result);
                    }
                }

                public void onFailure(Ccode ccode) {
                    if (callback != null) {
                        callback.onFails(DJIRemoteControllerError.getDJIError(ccode));
                    }
                }
            });
        }
    }

    @Getter(RemoteControllerKeys.SLAVE_LIST)
    public void getSlaveList(final DJISDKCacheHWAbstraction.InnerCallback callback) {
        if (!this.supportMasterSlaveMode) {
            CallbackUtils.onFailure(callback, DJIRemoteControllerError.COMMON_UNSUPPORTED);
            return;
        }
        final ArrayList<Information> result = new ArrayList<>();
        DataRcGetSlaveList.getInstance().start(new DJIDataCallBack() {
            /* class dji.sdksharedlib.hardware.abstractions.remotecontroller.DJIRCAbstraction.AnonymousClass30 */

            public void onSuccess(Object model) {
                SparseArray<DataRcGetSlaveList.RcModel> list = DataRcGetSlaveList.getInstance().getList();
                int cnt = list.size();
                for (int i = 0; i < cnt; i++) {
                    DataRcGetSlaveList.RcModel tmp = list.get(list.keyAt(i));
                    DataRcSetSlavePermission.RcSlavePermission permission = DataRcGetSlavePermission.getInstance().getPermission(tmp.id);
                    int pwd = tmp.password;
                    if (pwd > 9999) {
                        pwd = 9999;
                    }
                    if (pwd < 0) {
                        pwd = 0;
                    }
                    NumberFormat nf = NumberFormat.getInstance();
                    nf.setGroupingUsed(false);
                    nf.setMaximumIntegerDigits(4);
                    nf.setMinimumIntegerDigits(4);
                    result.add(new Information.Builder().identifier(tmp.id).name(tmp.name).password(nf.format((long) pwd)).signalQuality((short) tmp.quality).hasGimbalControlPermission(tmp.pitch || tmp.roll || tmp.yaw).build());
                }
                if (callback != null) {
                    callback.onSuccess((Information[]) result.toArray(new Information[result.size()]));
                }
            }

            public void onFailure(Ccode ccode) {
                if (callback != null) {
                    callback.onFails(DJIRemoteControllerError.getDJIError(ccode));
                }
            }
        });
    }

    @Action(RemoteControllerKeys.STOP_MASTER_SEARCHING)
    public void stopSearchMaster(final DJISDKCacheHWAbstraction.InnerCallback callback) {
        if (this.supportMasterSlaveMode) {
            DataRcSetSearchMode.getInstance().setIsOpen(false).start(new DJIDataCallBack() {
                /* class dji.sdksharedlib.hardware.abstractions.remotecontroller.DJIRCAbstraction.AnonymousClass31 */

                public void onSuccess(Object model) {
                    if (callback != null) {
                        callback.onSuccess(null);
                    }
                }

                public void onFailure(Ccode ccode) {
                    if (callback != null) {
                        callback.onFails(DJIRemoteControllerError.getDJIError(ccode));
                    }
                }
            });
        } else if (callback != null) {
            callback.onFails(DJIRemoteControllerError.getDJIError(Ccode.NOT_SUPPORT_CURRENT_STATE));
        }
    }

    @Getter(RemoteControllerKeys.MASTER_SEARCHING_STATE)
    public void getMasterRCSearchState(final DJISDKCacheHWAbstraction.InnerCallback callback) {
        if (this.supportMasterSlaveMode) {
            DataRcGetSearchMode.getInstance().start(new DJIDataCallBack() {
                /* class dji.sdksharedlib.hardware.abstractions.remotecontroller.DJIRCAbstraction.AnonymousClass32 */

                public void onSuccess(Object model) {
                    if (callback != null) {
                        callback.onSuccess(Boolean.valueOf(DataRcGetSearchMode.getInstance().getIsOpen()));
                    }
                }

                public void onFailure(Ccode ccode) {
                    if (callback != null) {
                        callback.onFails(DJIRemoteControllerError.getDJIError(ccode));
                    }
                }
            });
        } else if (callback != null) {
            callback.onFails(DJIRemoteControllerError.getDJIError(Ccode.NOT_SUPPORT_CURRENT_STATE));
        }
    }

    @Action(RemoteControllerKeys.START_SEARCH_MASTER)
    public void startSearchMaster(final DJISDKCacheHWAbstraction.InnerCallback callback) {
        if (!this.supportMasterSlaveMode) {
            CallbackUtils.onFailure(callback, DJIRemoteControllerError.COMMON_UNSUPPORTED);
        } else {
            DataRcSetSearchMode.getInstance().setIsOpen(true).start(new DJIDataCallBack() {
                /* class dji.sdksharedlib.hardware.abstractions.remotecontroller.DJIRCAbstraction.AnonymousClass33 */

                public void onSuccess(Object model) {
                    if (callback != null) {
                        callback.onSuccess(null);
                    }
                }

                public void onFailure(Ccode ccode) {
                    if (callback != null) {
                        callback.onFails(DJIRemoteControllerError.getDJIError(ccode));
                    }
                }
            });
        }
    }

    @Action(RemoteControllerKeys.CONNECT_TO_MASTER)
    public void connectToMaster(final DJISDKCacheHWAbstraction.InnerCallback callback, Credentials credentials) {
        if (credentials == null || credentials.getName() == null || credentials.getPassword() == null) {
            CallbackUtils.onFailure(callback, DJIError.COMMON_PARAM_ILLEGAL);
        } else if (TextUtils.isEmpty(credentials.getName()) || TextUtils.isEmpty(credentials.getPassword())) {
            CallbackUtils.onFailure(callback, DJIError.COMMON_PARAM_ILLEGAL);
        } else if (!this.supportMasterSlaveMode) {
            CallbackUtils.onFailure(callback, DJIRemoteControllerError.COMMON_UNSUPPORTED);
        } else {
            DataRcGetSlaveList.RcModel model = new DataRcGetSlaveList.RcModel();
            model.id = credentials.getID();
            model.isOpen = true;
            model.name = credentials.getName();
            model.password = Integer.parseInt(credentials.getPassword());
            DataRcSetConnectMaster.getInstance().setMaster(model).start(new DJIDataCallBack() {
                /* class dji.sdksharedlib.hardware.abstractions.remotecontroller.DJIRCAbstraction.AnonymousClass34 */

                public void onSuccess(Object model) {
                    if (callback != null) {
                        callback.onSuccess(ConnectToMasterResult.ACCEPTED);
                    }
                }

                public void onFailure(Ccode ccode) {
                    if (callback != null) {
                        callback.onFails(DJIRemoteControllerError.getDJIError(ccode));
                    }
                }
            });
        }
    }

    @Getter(RemoteControllerKeys.AVAILABLE_MASTERS)
    public void getAvailableMasters(final DJISDKCacheHWAbstraction.InnerCallback callback) {
        if (!this.supportMasterSlaveMode) {
            CallbackUtils.onFailure(callback, DJIRemoteControllerError.COMMON_UNSUPPORTED);
            return;
        }
        final ArrayList<Information> result = new ArrayList<>();
        DataRcGetSearchMasters.getInstance().start(new DJIDataCallBack() {
            /* class dji.sdksharedlib.hardware.abstractions.remotecontroller.DJIRCAbstraction.AnonymousClass35 */

            public void onSuccess(Object model) {
                SparseArray<DataRcGetSlaveList.RcModel> list = DataRcGetSearchMasters.getInstance().getList();
                int cnt = list.size();
                for (int i = 0; i < cnt; i++) {
                    DataRcGetSlaveList.RcModel tmp = list.get(list.keyAt(i));
                    result.add(new Information.Builder().identifier(tmp.id).name(tmp.name).password("").signalQuality((short) tmp.quality).hasGimbalControlPermission(tmp.pitch || tmp.roll || tmp.yaw).build());
                }
                CallbackUtils.onSuccess(callback, (Information[]) result.toArray(new Information[result.size()]));
            }

            public void onFailure(Ccode ccode) {
                CallbackUtils.onFailure(callback, DJIError.getDJIError(ccode));
            }
        });
    }

    @Getter("CountryCode")
    public void getCountryCode(final DJISDKCacheHWAbstraction.InnerCallback callback) {
        DataRcGetWifiFreqInfo.getCcInstance().start(new DJIDataCallBack() {
            /* class dji.sdksharedlib.hardware.abstractions.remotecontroller.DJIRCAbstraction.AnonymousClass36 */

            public void onSuccess(Object model) {
                DataRcGetWifiFreqInfo retMode = (DataRcGetWifiFreqInfo) model;
                if (callback != null) {
                    callback.onSuccess(retMode.getCountryCode());
                }
            }

            public void onFailure(Ccode ccode) {
                if (callback != null) {
                    callback.onFails(DJIRemoteControllerError.getDJIError(ccode));
                }
            }
        });
    }

    @Getter(DJISDKCacheKeys.SERIAL_NUMBER)
    public void getSerialNumber(DJISDKCacheHWAbstraction.InnerCallback callback) {
        getSerialNumber(callback, 0);
    }

    @Getter("FullSerialNumberHash")
    public void getFullSerialNumber(DJISDKCacheHWAbstraction.InnerCallback callback) {
        getSerialNumber(callback, 2);
    }

    @Getter(RemoteControllerKeys.STICK_MAPPING)
    public void getStickMapping(final DJISDKCacheHWAbstraction.InnerCallback callback) {
        DataRcGetControlMode.getInstance().start(new DJIDataCallBack() {
            /* class dji.sdksharedlib.hardware.abstractions.remotecontroller.DJIRCAbstraction.AnonymousClass37 */

            public void onSuccess(Object model) {
                CallbackUtils.onSuccess(callback, DataRcGetControlMode.getInstance().getChannels());
            }

            public void onFailure(Ccode ccode) {
                CallbackUtils.onFailure(callback, DJIRemoteControllerError.getDJIError(ccode));
            }
        });
    }

    private void getSerialNumber(final DJISDKCacheHWAbstraction.InnerCallback callback, final int state) {
        if (isNewProgressOfActivation()) {
            new DataCommonActiveGetVer().setDevice(DeviceType.OSD).start(new DJIDataCallBack() {
                /* class dji.sdksharedlib.hardware.abstractions.remotecontroller.DJIRCAbstraction.AnonymousClass38 */

                public void onSuccess(Object model) {
                    DataOsdActiveStatus.getInstance().setType(DataAbstractGetPushActiveStatus.TYPE.GET).start(new DJIDataCallBack() {
                        /* class dji.sdksharedlib.hardware.abstractions.remotecontroller.DJIRCAbstraction.AnonymousClass38.AnonymousClass1 */

                        public void onSuccess(Object model) {
                            String MD5SN = DJIRCAbstraction.this.getHashSerialNum(DataOsdActiveStatus.getInstance().getSN(), state);
                            if (callback != null) {
                                callback.onSuccess(MD5SN);
                            }
                        }

                        public void onFailure(Ccode ccode) {
                            if (callback != null) {
                                callback.onFails(DJIRemoteControllerError.getDJIError(ccode));
                            }
                        }
                    });
                }

                public void onFailure(Ccode ccode) {
                    if (callback != null) {
                        callback.onFails(DJIRemoteControllerError.getDJIError(ccode));
                    }
                }
            });
            return;
        }
        final DataOsdActiveStatus status = new DataOsdActiveStatus();
        status.setType(DataAbstractGetPushActiveStatus.TYPE.GET).start(new DJIDataCallBack() {
            /* class dji.sdksharedlib.hardware.abstractions.remotecontroller.DJIRCAbstraction.AnonymousClass39 */

            public void onSuccess(Object model) {
                String MD5SN = DJIRCAbstraction.this.getHashSerialNum(status.getSN(), state);
                if (callback != null) {
                    callback.onSuccess(MD5SN);
                }
            }

            public void onFailure(Ccode ccode) {
                if (callback != null) {
                    callback.onFails(DJIRemoteControllerError.getDJIError(ccode));
                }
            }
        });
    }

    /* access modifiers changed from: protected */
    public boolean isNewProgressOfActivation() {
        return false;
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEvent3BackgroundThread(DataCommonGetPushUpgradeStatus upgradeStatus) {
        if (upgradeStatus.getDescList().mUpgradeStep == DataCommonGetPushUpgradeStatus.DJIUpgradeStep.Complete) {
            VersionController.logD(TAG, "Rc Abstraction receive upgrade complete event and reset the cache value");
            notifyValueChangeForKeyPath((Object) null, convertKeyToPath(DJISDKCacheKeys.FIRMWARE_VERSION));
        }
    }

    @Getter(DJISDKCacheKeys.FIRMWARE_VERSION)
    public void getFirmwareVersion(DJISDKCacheHWAbstraction.InnerCallback callback) {
        if (VersionController.getInstance().getDJIVersionRC() != null) {
            VersionController.getInstance().getDJIVersionRC().getVersion(new DJIRCAbstraction$$Lambda$0(callback), 60);
        } else if (callback != null) {
            callback.onFails(DJIError.COMMON_UNSUPPORTED);
        }
    }

    static final /* synthetic */ void lambda$getFirmwareVersion$0$DJIRCAbstraction(DJISDKCacheHWAbstraction.InnerCallback callback, String version) {
        if (callback == null) {
            return;
        }
        if (TextUtils.isEmpty(version) || DEFAULT_FIRMWARE_VERSION.equals(version)) {
            callback.onFails(DJIRemoteControllerError.UNABLE_TO_GET_FIRMWARE_VERSION);
        } else {
            callback.onSuccess(version);
        }
    }

    @Action(RemoteControllerKeys.START_PAIRING)
    public void enterPairingMode(final DJISDKCacheHWAbstraction.InnerCallback callback) {
        DataRcSetFrequency.getInstance().setMode(DataRcSetFrequency.FreqMode.Enter).start(new DJIDataCallBack() {
            /* class dji.sdksharedlib.hardware.abstractions.remotecontroller.DJIRCAbstraction.AnonymousClass40 */

            public void onSuccess(Object model) {
                if (callback != null) {
                    callback.onSuccess(null);
                }
            }

            public void onFailure(Ccode ccode) {
                if (callback != null) {
                    callback.onFails(DJIRemoteControllerError.getDJIError(ccode));
                }
            }
        });
    }

    @Getter("PairingState")
    public void getRCToAircraftPairingState(final DJISDKCacheHWAbstraction.InnerCallback callback) {
        DataRcSetFrequency.getInstance().setMode(DataRcSetFrequency.FreqMode.Current).start(new DJIDataCallBack() {
            /* class dji.sdksharedlib.hardware.abstractions.remotecontroller.DJIRCAbstraction.AnonymousClass41 */

            public void onSuccess(Object model) {
                if (callback != null) {
                    PairingState state = PairingState.find(DataRcSetFrequency.getInstance().getRecData()[0]);
                    DataOsdGetPushCommon.DroneType droneType = DataOsdGetPushCommon.getInstance().getDroneType();
                    if (state == PairingState.UNPAIRED && droneType != DataOsdGetPushCommon.DroneType.Unknown) {
                        state = PairingState.PAIRED;
                    }
                    callback.onSuccess(state);
                }
            }

            public void onFailure(Ccode ccode) {
                if (callback != null) {
                    callback.onFails(DJIRemoteControllerError.getDJIError(ccode));
                }
            }
        });
    }

    /* JADX DEBUG: Failed to find minimal casts for resolve overloaded methods, cast all args instead
     method: dji.log.DJILog.d(java.lang.String, java.lang.String, boolean, boolean):void
     arg types: [java.lang.String, java.lang.String, int, int]
     candidates:
      dji.log.DJILog.d(java.lang.String, java.lang.String, java.lang.Throwable, java.lang.Object[]):void
      dji.log.DJILog.d(java.lang.String, java.lang.String, boolean, boolean):void */
    @Setter("Mode")
    public void setRemoteControllerMode(RCMode value, final DJISDKCacheHWAbstraction.InnerCallback callback) {
        boolean x = true;
        if (((Boolean) CacheHelper.getValue(KeyHelper.getRemoteControllerKey(RemoteControllerKeys.IS_NARROW_BAND_MODULE_CONNECTED), false)).booleanValue()) {
            callback.onFails(DJINarrowBandModuleError.NARROW_BAND_INSERTED);
            return;
        }
        DJILog.d(TAG, "set workmode start", true, true);
        if (value == null) {
            DJILog.d(TAG, "set workmode 0", true, true);
            if (callback != null) {
                callback.onFails(DJIError.COMMON_PARAM_INVALID);
            }
        } else if (this.supportMasterSlaveMode || this.supportMasterSlaveModeV2) {
            final RCMode param = value;
            if (param == RCMode.NORMAL) {
                x = false;
            }
            DataRcSetToggle.getInstance().setIsOpen(x).start(new DJIDataCallBack() {
                /* class dji.sdksharedlib.hardware.abstractions.remotecontroller.DJIRCAbstraction.AnonymousClass42 */

                public void onSuccess(Object model) {
                    if (param != RCMode.NORMAL) {
                        DataRcSetMaster.getInstance().setMode(DataRcSetMaster.MODE.find(DataRcSetMaster.MODE.find(param.value()).value())).start(new DJIDataCallBack() {
                            /* class dji.sdksharedlib.hardware.abstractions.remotecontroller.DJIRCAbstraction.AnonymousClass42.AnonymousClass1 */

                            /* JADX DEBUG: Failed to find minimal casts for resolve overloaded methods, cast all args instead
                             method: dji.log.DJILog.d(java.lang.String, java.lang.String, boolean, boolean):void
                             arg types: [java.lang.String, java.lang.String, int, int]
                             candidates:
                              dji.log.DJILog.d(java.lang.String, java.lang.String, java.lang.Throwable, java.lang.Object[]):void
                              dji.log.DJILog.d(java.lang.String, java.lang.String, boolean, boolean):void */
                            public void onSuccess(Object model) {
                                DJILog.d(DJIRCAbstraction.TAG, "set workmode 2", true, true);
                                if (callback != null) {
                                    callback.onSuccess(null);
                                }
                            }

                            /* JADX DEBUG: Failed to find minimal casts for resolve overloaded methods, cast all args instead
                             method: dji.log.DJILog.d(java.lang.String, java.lang.String, boolean, boolean):void
                             arg types: [java.lang.String, java.lang.String, int, int]
                             candidates:
                              dji.log.DJILog.d(java.lang.String, java.lang.String, java.lang.Throwable, java.lang.Object[]):void
                              dji.log.DJILog.d(java.lang.String, java.lang.String, boolean, boolean):void */
                            public void onFailure(Ccode ccode) {
                                DJILog.d(DJIRCAbstraction.TAG, "set workmode 3", true, true);
                                if (callback != null) {
                                    callback.onFails(DJIRemoteControllerError.getDJIError(ccode));
                                }
                            }
                        });
                    } else if (callback != null) {
                        callback.onSuccess(null);
                    }
                }

                /* JADX DEBUG: Failed to find minimal casts for resolve overloaded methods, cast all args instead
                 method: dji.log.DJILog.d(java.lang.String, java.lang.String, boolean, boolean):void
                 arg types: [java.lang.String, java.lang.String, int, int]
                 candidates:
                  dji.log.DJILog.d(java.lang.String, java.lang.String, java.lang.Throwable, java.lang.Object[]):void
                  dji.log.DJILog.d(java.lang.String, java.lang.String, boolean, boolean):void */
                public void onFailure(Ccode ccode) {
                    DJILog.d(DJIRCAbstraction.TAG, "set workmode 4", true, true);
                    if (callback != null) {
                        callback.onFails(DJIRemoteControllerError.getDJIError(ccode));
                    }
                }
            });
        } else {
            DJILog.d(TAG, "set workmode 1", true, true);
            if (callback != null) {
                callback.onFails(DJIError.COMMON_UNSUPPORTED);
            }
        }
    }

    @Setter(RemoteControllerKeys.RC_MASTER_SLAVE_OPEN)
    public void setRcMasterSlaveOpen(final Boolean isOpen, final DJISDKCacheHWAbstraction.InnerCallback callback) {
        if (((Boolean) CacheHelper.getValue(KeyHelper.getRemoteControllerKey(RemoteControllerKeys.IS_NARROW_BAND_MODULE_CONNECTED), false)).booleanValue()) {
            callback.onFails(DJINarrowBandModuleError.NARROW_BAND_INSERTED);
        } else if (isOpen != null) {
            DataRcSetToggle.getInstance().setIsOpen(isOpen.booleanValue()).start(new DJIDataCallBack() {
                /* class dji.sdksharedlib.hardware.abstractions.remotecontroller.DJIRCAbstraction.AnonymousClass43 */

                public void onSuccess(Object model) {
                    if (callback != null) {
                        callback.onSuccess(isOpen);
                        if (!isOpen.booleanValue()) {
                            DJIRCAbstraction.this.notifyValueChangeForKeyPath(RCMode.NORMAL, DJIRCAbstraction.this.genKeyPath("Mode"));
                        }
                    }
                }

                public void onFailure(Ccode ccode) {
                    if (callback != null) {
                        callback.onFails(DJIRemoteControllerError.getDJIError(ccode));
                    }
                }
            });
        } else if (callback != null) {
            callback.onFails(DJIRemoteControllerError.getDJIError(Ccode.INVALID_PARAM));
        }
    }

    @Getter("Mode")
    public void getRemoteControllerMode(final DJISDKCacheHWAbstraction.InnerCallback callback) {
        if (((Boolean) CacheHelper.getValue(KeyHelper.getRemoteControllerKey(RemoteControllerKeys.IS_NARROW_BAND_MODULE_CONNECTED), false)).booleanValue()) {
            callback.onFails(DJINarrowBandModuleError.NARROW_BAND_INSERTED);
        } else {
            new DataRcGetToggle().start(new DJIDataCallBack() {
                /* class dji.sdksharedlib.hardware.abstractions.remotecontroller.DJIRCAbstraction.AnonymousClass44 */

                public void onSuccess(Object model) {
                    if (!((DataRcGetToggle) model).getIsOpen()) {
                        RCMode result = RCMode.NORMAL;
                        if (callback != null) {
                            callback.onSuccess(result);
                            return;
                        }
                        return;
                    }
                    new DataRcGetMaster().start(new DJIDataCallBack() {
                        /* class dji.sdksharedlib.hardware.abstractions.remotecontroller.DJIRCAbstraction.AnonymousClass44.AnonymousClass1 */

                        public void onSuccess(Object model) {
                            RCMode result = RCMode.find(((DataRcGetMaster) model).getMode().value());
                            if (callback != null) {
                                callback.onSuccess(result);
                            }
                        }

                        public void onFailure(Ccode ccode) {
                            if (callback != null) {
                                callback.onFails(DJIRemoteControllerError.getDJIError(ccode));
                            }
                        }
                    });
                }

                public void onFailure(Ccode ccode) {
                    if (callback != null) {
                        callback.onFails(DJIRemoteControllerError.getDJIError(ccode));
                    }
                }
            });
        }
    }

    @Getter(RemoteControllerKeys.RC_MASTER_SLAVE_OPEN)
    public void getRcMasterSlaveOpen(final DJISDKCacheHWAbstraction.InnerCallback callback) {
        if (((Boolean) CacheHelper.getValue(KeyHelper.getRemoteControllerKey(RemoteControllerKeys.IS_NARROW_BAND_MODULE_CONNECTED), false)).booleanValue()) {
            callback.onFails(DJINarrowBandModuleError.NARROW_BAND_INSERTED);
        } else {
            new DataRcGetToggle().start(new DJIDataCallBack() {
                /* class dji.sdksharedlib.hardware.abstractions.remotecontroller.DJIRCAbstraction.AnonymousClass45 */

                public void onSuccess(Object model) {
                    if (callback != null) {
                        callback.onSuccess(Boolean.valueOf(((DataRcGetToggle) model).getIsOpen()));
                    }
                }

                public void onFailure(Ccode ccode) {
                    if (callback != null) {
                        callback.onFails(DJIRemoteControllerError.getDJIError(ccode));
                    }
                }
            });
        }
    }

    @Getter("RcAircraftType")
    public void getRcAircraftType(final DJISDKCacheHWAbstraction.InnerCallback callback) {
        final DataRcSetAppSpecialControl getter = new DataRcSetAppSpecialControl();
        getter.setCmdType(DataRcSetAppSpecialControl.CmdType.GET_AIRCRAFT_TYPE).start(new DJIDataCallBack() {
            /* class dji.sdksharedlib.hardware.abstractions.remotecontroller.DJIRCAbstraction.AnonymousClass46 */

            public void onSuccess(Object model) {
                if (callback != null) {
                    callback.onSuccess(DataRcSetAppSpecialControl.RcAircraftType.find(getter.getValue()));
                }
            }

            public void onFailure(Ccode ccode) {
                if (callback != null) {
                    callback.onFails(DJIRemoteControllerError.getDJIError(ccode));
                }
            }
        });
    }

    @Setter("RcAircraftType")
    public void setRcAircraftType(final DataRcSetAppSpecialControl.RcAircraftType type, final DJISDKCacheHWAbstraction.InnerCallback callback) {
        new DataRcSetAppSpecialControl().setCmdType(DataRcSetAppSpecialControl.CmdType.SET_AIRCRAFT_TYPE).setValue((byte) type.value()).start(new DJIDataCallBack() {
            /* class dji.sdksharedlib.hardware.abstractions.remotecontroller.DJIRCAbstraction.AnonymousClass47 */

            public void onSuccess(Object model) {
                if (callback != null) {
                    callback.onSuccess(type);
                }
            }

            public void onFailure(Ccode ccode) {
                if (callback != null) {
                    callback.onFails(DJIRemoteControllerError.getDJIError(ccode));
                }
            }
        });
    }

    @Getter(RemoteControllerKeys.CHARGE_MOBILE_MODE)
    public void getChargeMobileMode(DJISDKCacheHWAbstraction.InnerCallback callback) {
        if (callback != null) {
            callback.onFails(DJIRemoteControllerError.getDJIError(Ccode.NOT_SUPPORT_CURRENT_STATE));
        }
    }

    @Setter(RemoteControllerKeys.CHARGE_MOBILE_MODE)
    public void setChargeMobileMode(ChargeMobileMode mode, DJISDKCacheHWAbstraction.InnerCallback callback) {
        if (callback != null) {
            callback.onFails(DJIRemoteControllerError.getDJIError(Ccode.NOT_SUPPORT_CURRENT_STATE));
        }
    }

    @Action(RemoteControllerKeys.RESPONSE_TO_REQUEST_FOR_GIMBAL_CONTROL)
    public void respondToRequestForGimbalControl(DJISDKCacheHWAbstraction.InnerCallback callback, ResponseForGimbalControl response) {
        DataRcAckGimbalCtrPermission permission = DataRcAckGimbalCtrPermission.getInstance();
        permission.setIsAgree(response.isAgree());
        permission.setRecData(new byte[]{(byte) response.getRequesterId()});
        permission.start();
    }

    /* access modifiers changed from: protected */
    public DJISDKCacheKey genKeyPath(String paramKey) {
        return new DJISDKCacheKey.Builder().component(RemoteControllerKeys.COMPONENT_KEY).paramKey(paramKey).build();
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEvent3BackgroundThread(DataRcGetPushParams params) {
        int i;
        boolean z;
        boolean z2;
        boolean z3;
        boolean z4 = true;
        notifyValueChangeForKeyPath(new Stick(params.getAileron() - 1024, params.getElevator() - 1024), genKeyPath(RemoteControllerKeys.RIGHT_STICK_VALUE));
        notifyValueChangeForKeyPath(new Stick(params.getRudder() - 1024, params.getThrottle() - 1024), genKeyPath(RemoteControllerKeys.LEFT_STICK_VALUE));
        notifyValueChangeForKeyPath(Integer.valueOf(params.getGyroValue() - 1024), genKeyPath(RemoteControllerKeys.LEFT_WHEEL));
        notifyValueChangeForKeyPath(getRightWheel(params), genKeyPath(RemoteControllerKeys.RIGHT_WHEEL));
        if (this instanceof DJIRCRM500Abstraction) {
            notifyValueChangeForKeyPath(new HardwareState.Button(this.hardwareState.getC3Button().isPresent(), params.getCustom3() == 1), genKeyPath(RemoteControllerKeys.CUSTOM_BUTTON_3));
        } else {
            boolean isPresent = this.hardwareState.getTransformationSwitch().isPresent();
            if (params.getFootStool()) {
                i = 1;
            } else {
                i = 0;
            }
            notifyValueChangeForKeyPath(new HardwareState.TransformationSwitch(isPresent, HardwareState.TransformationSwitch.State.find(i)), genKeyPath(RemoteControllerKeys.TRANSFORMATION_SWITCH));
        }
        if (this instanceof DJIRCWM160Abstaction) {
            notifyValueChangeForKeyPath(SoftSwitchJoyStickMode.find(params.getMode()), genKeyPath(RemoteControllerKeys.SOFT_SWITCH_STICK_MODE));
        } else {
            notifyValueChangeForKeyPath(HardwareState.FlightModeSwitch.find(DJIProductManager.getInstance().getType(), params.getMode()), genKeyPath(RemoteControllerKeys.FLIGHT_MODE_SWITCH_POSITION));
        }
        notifyValueChangeForKeyPath(new HardwareState.Button(this.hardwareState.getGoHomeButton().isPresent(), params.isGoHomeButtonPressed()), genKeyPath(RemoteControllerKeys.GO_HOME_BUTTON));
        notifyValueChangeForKeyPath(new HardwareState.Button(this.hardwareState.getGoHomeButton().isPresent(), params.getRecordStatus()), genKeyPath(RemoteControllerKeys.RECORD_BUTTON));
        notifyValueChangeForKeyPath(new HardwareState.Button(this.hardwareState.getShutterButton().isPresent(), params.getShutterStatus()), genKeyPath(RemoteControllerKeys.SHUTTER_BUTTON));
        notifyValueChangeForKeyPath(new HardwareState.Button(this.hardwareState.getPlaybackButton().isPresent(), params.getPlayback() == 1), genKeyPath(RemoteControllerKeys.PLAYBACK_BUTTON));
        boolean isPresent2 = this.hardwareState.getPauseButton().isPresent();
        if (params.getPlayback() == 1) {
            z = true;
        } else {
            z = false;
        }
        notifyValueChangeForKeyPath(new HardwareState.Button(isPresent2, z), genKeyPath(RemoteControllerKeys.PAUSE_BUTTON));
        boolean isPresent3 = this.hardwareState.getC1Button().isPresent();
        if (params.getCustom1() == 1) {
            z2 = true;
        } else {
            z2 = false;
        }
        notifyValueChangeForKeyPath(new HardwareState.Button(isPresent3, z2), genKeyPath(RemoteControllerKeys.CUSTOM_BUTTON_1));
        boolean isPresent4 = this.hardwareState.getFunctionButton().isPresent();
        if (params.getCustom1() == 1) {
            z3 = true;
        } else {
            z3 = false;
        }
        notifyValueChangeForKeyPath(new HardwareState.Button(isPresent4, z3), genKeyPath(RemoteControllerKeys.FUNCTION_BUTTON));
        boolean isPresent5 = this.hardwareState.getC2Button().isPresent();
        if (params.getCustom2() != 1) {
            z4 = false;
        }
        notifyValueChangeForKeyPath(new HardwareState.Button(isPresent5, z4), genKeyPath(RemoteControllerKeys.CUSTOM_BUTTON_2));
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEvent3BackgroundThread(DataRcGetPushRcCustomButtonsStatus status) {
        if (this.hardwareState.getFiveDButton().isPresent()) {
            HardwareState.FiveDButtonDirection verticalDirection = HardwareState.FiveDButtonDirection.MIDDLE;
            HardwareState.FiveDButtonDirection horizontalDirection = HardwareState.FiveDButtonDirection.MIDDLE;
            if (status.isUp()) {
                verticalDirection = HardwareState.FiveDButtonDirection.POSITIVE;
            }
            if (status.isDown()) {
                verticalDirection = HardwareState.FiveDButtonDirection.NEGATIVE;
            }
            if (status.isLeft()) {
                horizontalDirection = HardwareState.FiveDButtonDirection.NEGATIVE;
            }
            if (status.isRight()) {
                horizontalDirection = HardwareState.FiveDButtonDirection.POSITIVE;
            }
            notifyValueChangeForKeyPath(new HardwareState.FiveDButton(status.isPressed(), this.hardwareState.getFiveDButton().isPresent(), verticalDirection, horizontalDirection), genKeyPath(RemoteControllerKeys.FIVE_D_BUTTON));
        }
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEvent3BackgroundThread(DataRcGetPushGpsInfo gpsInfo) {
        GPSData.Time currentTime = new GPSData.Time.Builder().hour((byte) gpsInfo.getHour()).minute((byte) gpsInfo.getMinute()).second((byte) gpsInfo.getSecond()).year(gpsInfo.getYear()).month((byte) gpsInfo.getMonth()).day((byte) gpsInfo.getDay()).build();
        notifyValueChangeForKeyPath(new GPSData.Builder().time(currentTime).location(new GPSData.GPSLocation(gpsInfo.getLongitude(), gpsInfo.getLatitude())).eastSpeed((float) gpsInfo.getXSpeed()).northSpeed((float) gpsInfo.getYSpeed()).satelliteCount(gpsInfo.getGpsNum()).locationAccuracy(gpsInfo.getAccuracy().floatValue()).isValid(gpsInfo.getGpsStatus()).build(), genKeyPath(RemoteControllerKeys.GPS_DATA));
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEvent3BackgroundThread(DataRcGetPushBatteryInfo batteryInfo) {
        if (!(this instanceof DJIRCRM500Abstraction)) {
            this.rcBatteryInfo = new ChargeRemaining(batteryInfo.getBatteryVolume(), batteryInfo.getBattery());
            notifyValueChangeForKeyPath(this.rcBatteryInfo, genKeyPath("ChargeRemaining"));
            updateLowRcBatteryKey();
        }
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEvent3BackgroundThread(DataOsdGetPushCheckStatus checkStatus) {
        updateLowRcBatteryKey();
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEvent3BackgroundThread(DataRcGetPushFollowFocus rcRemoteFocusInfo) {
        boolean z = true;
        if (rcRemoteFocusInfo != null && rcRemoteFocusInfo.getRecData() != null && this.remoteFocusCheckingSupported) {
            if (rcRemoteFocusInfo.getCurCtrlStatus() != 1) {
                z = false;
            }
            notifyValueChangeForKeyPath(Boolean.valueOf(z), genKeyPath(RemoteControllerKeys.FOCUS_CONTROLLER_IS_WORKING));
            transformRemoteFocusStateFromProtocol(rcRemoteFocusInfo.getCtrlType().value(), rcRemoteFocusInfo.getCtrlDirection().value());
        }
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEvent3BackgroundThread(DataRcGetPushFollowFocus2 rcRemoteFocusInfo) {
        if (rcRemoteFocusInfo.isGetted()) {
            if (this.lastPushTime == -1) {
                this.changeRemoteFocusConnectedTask = new TimerTask() {
                    /* class dji.sdksharedlib.hardware.abstractions.remotecontroller.DJIRCAbstraction.AnonymousClass48 */

                    public void run() {
                        if (System.currentTimeMillis() - DJIRCAbstraction.this.lastPushTime > 2000) {
                            DJIRCAbstraction.this.notifyValueChangeForKeyPath(false, DJIRCAbstraction.this.genKeyPath(RemoteControllerKeys.FOCUS_CONTROLLER_IS_WORKING));
                            long unused = DJIRCAbstraction.this.lastPushTime = -1;
                            cancel();
                            if (DJIRCAbstraction.this.changeRemoteFocusConnectedTimer != null) {
                                DJIRCAbstraction.this.changeRemoteFocusConnectedTimer.cancel();
                            }
                        }
                    }
                };
                this.changeRemoteFocusConnectedTimer = new Timer();
                this.changeRemoteFocusConnectedTimer.schedule(this.changeRemoteFocusConnectedTask, 0, 2000);
            }
            this.lastPushTime = System.currentTimeMillis();
            notifyValueChangeForKeyPath(Boolean.valueOf(rcRemoteFocusInfo.getCurCtrlStatus() == 1), genKeyPath(RemoteControllerKeys.FOCUS_CONTROLLER_IS_WORKING));
            transformRemoteFocusStateFromProtocol(rcRemoteFocusInfo.getCtrlType().value(), rcRemoteFocusInfo.getCtrlDirection().value());
        }
    }

    @Getter(RemoteControllerKeys.LEFT_WHEEL_GIMBAL_CONTROL_SPEED_COEFFICIENT)
    public void getLeftWheelGimbalControlSpeedCoefficient(final DJISDKCacheHWAbstraction.InnerCallback callback) {
        DataRcGetWheelGain.getInstance().start(new DJIDataCallBack() {
            /* class dji.sdksharedlib.hardware.abstractions.remotecontroller.DJIRCAbstraction.AnonymousClass49 */

            public void onSuccess(Object model) {
                CallbackUtils.onSuccess(callback, Integer.valueOf(DataRcGetWheelGain.getInstance().getGain()));
            }

            public void onFailure(Ccode ccode) {
                CallbackUtils.onFailure(callback, ccode);
            }
        });
    }

    @Setter(RemoteControllerKeys.LEFT_WHEEL_GIMBAL_CONTROL_SPEED_COEFFICIENT)
    public void setLeftWheelGimbalControlSpeedCoefficient(Integer data, DJISDKCacheHWAbstraction.InnerCallback callback) {
        if (data.intValue() < 0 || data.intValue() > 100) {
            CallbackUtils.onFailure(callback, DJIError.COMMON_PARAM_ILLEGAL);
        } else {
            DataRcSetWheelGain.getInstance().setGain(data.intValue()).start(CallbackUtils.defaultCB(callback));
        }
    }

    /* access modifiers changed from: protected */
    public boolean isPhantom4RC() {
        return DJIProductManager.getInstance().getType() == ProductType.Tomato;
    }

    /* access modifiers changed from: protected */
    public boolean isFoldingDrone() {
        ProductType type = DJIProductManager.getInstance().getType();
        return type == ProductType.KumquatS || type == ProductType.KumquatX;
    }

    /* access modifiers changed from: protected */
    public boolean isP4PAndP4ARC() {
        ProductType type = DJIProductManager.getInstance().getType();
        return type == ProductType.Pomato || type == ProductType.Potato;
    }

    /* access modifiers changed from: protected */
    public boolean isPlaybackButtonSupported() {
        ProductType type = DJIProductManager.getInstance().getType();
        return type == ProductType.P34K || type == ProductType.litchiS || type == ProductType.litchiX;
    }

    /* access modifiers changed from: protected */
    public void initializeCustomizedKey() {
        notifyValueChangeForKeyPath(Boolean.valueOf(this.supportMasterSlaveMode), convertKeyToPath(RemoteControllerKeys.IS_MASTER_SLAVE_MODE_SUPPORTED));
        notifyValueChangeForKeyPath(Boolean.valueOf(this.supportMasterSlaveModeV2), convertKeyToPath(RemoteControllerKeys.IS_MASTER_SLAVE_MODE_V2_SUPPORTED));
        notifyValueChangeForKeyPath(Boolean.valueOf(this.remoteFocusCheckingSupported), convertKeyToPath(RemoteControllerKeys.IS_FOCUS_CONTROLLER_SUPPORTED));
    }

    /* access modifiers changed from: protected */
    public void updateLowRcBatteryKey() {
        DataRcGetPushBatteryInfo batteryInfo = DataRcGetPushBatteryInfo.getInstance();
        DataOsdGetPushCheckStatus checkStatus = DataOsdGetPushCheckStatus.getInstance();
        DataRcGetPushCheckStatus rcCheckSatus = DataRcGetPushCheckStatus.getInstance();
        if (checkStatus.isGetted()) {
            notifyValueChangeForKeyPath(Boolean.valueOf(checkStatus.getPowerStatus()), genKeyPath(RemoteControllerKeys.IS_CHARGE_REMAINING_LOW));
        } else if (CommonUtil.isWM240Series(null) && rcCheckSatus.isGetted()) {
            notifyValueChangeForKeyPath(Boolean.valueOf(rcCheckSatus.getIsRcBatteryTooLow()), genKeyPath(RemoteControllerKeys.IS_CHARGE_REMAINING_LOW));
        } else if (batteryInfo.isGetted()) {
            this.rcBatteryInfo = new ChargeRemaining(batteryInfo.getBatteryVolume(), batteryInfo.getBattery());
            int batteryThreshold = 30;
            if (CommonUtil.isKumquatSeries(null) || CommonUtil.isWM240Series(null)) {
                batteryThreshold = 15;
            }
            notifyValueChangeForKeyPath(Boolean.valueOf(this.rcBatteryInfo.getRemainingChargeInPercent() < batteryThreshold), genKeyPath(RemoteControllerKeys.IS_CHARGE_REMAINING_LOW));
        }
    }

    private void transformRemoteFocusStateFromProtocol(int controlType, int direction) {
        switch (controlType) {
            case 0:
                notifyValueChangeForKeyPath(FocusControllerState.ControlType.APERTURE, genKeyPath(RemoteControllerKeys.FOCUS_CONTROLLER_CONTROL_TYPE));
                break;
            case 1:
                notifyValueChangeForKeyPath(FocusControllerState.ControlType.FOCUS_DISTANCE, genKeyPath(RemoteControllerKeys.FOCUS_CONTROLLER_CONTROL_TYPE));
                break;
            case 2:
                notifyValueChangeForKeyPath(FocusControllerState.ControlType.FOCAL_LENGTH, genKeyPath(RemoteControllerKeys.FOCUS_CONTROLLER_CONTROL_TYPE));
                break;
            case 10:
                notifyValueChangeForKeyPath(FocusControllerState.ControlType.UNKNOWN, genKeyPath(RemoteControllerKeys.FOCUS_CONTROLLER_CONTROL_TYPE));
                break;
        }
        switch (direction) {
            case 0:
                notifyValueChangeForKeyPath(FocusControllerState.Direction.CLOCKWISE, genKeyPath(RemoteControllerKeys.FOCUS_CONTROLLER_DIRECTION));
                return;
            case 1:
                notifyValueChangeForKeyPath(FocusControllerState.Direction.COUNTER_CLOCKWISE, genKeyPath(RemoteControllerKeys.FOCUS_CONTROLLER_DIRECTION));
                return;
            case 10:
                notifyValueChangeForKeyPath(FocusControllerState.Direction.UNKNOWN, genKeyPath(RemoteControllerKeys.FOCUS_CONTROLLER_DIRECTION));
                return;
            default:
                return;
        }
    }

    @Getter(RemoteControllerKeys.RC_UPGRADE_VOICE_DISABLE)
    public void isRcUpgradeVoiceDisable(final DJISDKCacheHWAbstraction.InnerCallback callback) {
        final DataDm368GetGParams getParams = DataDm368GetGParams.getInstance();
        getParams.setType(true).start(new DJIDataCallBack() {
            /* class dji.sdksharedlib.hardware.abstractions.remotecontroller.DJIRCAbstraction.AnonymousClass50 */

            public void onSuccess(Object model) {
                callback.onSuccess(Boolean.valueOf(getParams.getIsDisableUpgradeVoice()));
            }

            public void onFailure(Ccode ccode) {
                callback.onFails(DJIRemoteControllerError.getDJIError(ccode));
            }
        });
    }

    @Setter(RemoteControllerKeys.RC_UPGRADE_VOICE_DISABLE)
    public void setRcVoiceStatus(boolean isDisable, final DJISDKCacheHWAbstraction.InnerCallback callback) {
        int i;
        DataDm368SetGParams setParams = new DataDm368SetGParams();
        DataDm368SetGParams.CmdId cmdId = DataDm368SetGParams.CmdId.DisableUpgradeSound;
        if (isDisable) {
            i = 1;
        } else {
            i = 0;
        }
        setParams.set(cmdId, i).start(new DJIDataCallBack() {
            /* class dji.sdksharedlib.hardware.abstractions.remotecontroller.DJIRCAbstraction.AnonymousClass51 */

            public void onSuccess(Object model) {
                callback.onSuccess(true);
            }

            public void onFailure(Ccode ccode) {
                callback.onFails(DJIRemoteControllerError.getDJIError(ccode));
            }
        });
    }

    @Setter(RemoteControllerKeys.REMOTE_CONTROLLER_UNIT)
    public void setRcUnit(int rcUnit, final DJISDKCacheHWAbstraction.InnerCallback callback) {
        DataRcSetRcUnitNLang.getInstance().setUnit(rcUnit).start(new DJIDataCallBack() {
            /* class dji.sdksharedlib.hardware.abstractions.remotecontroller.DJIRCAbstraction.AnonymousClass52 */

            public void onSuccess(Object model) {
                callback.onSuccess(model);
            }

            public void onFailure(Ccode ccode) {
                callback.onSuccess(ccode);
            }
        });
    }

    @Setter(RemoteControllerKeys.FLIGHT_MODE_SWITCH_POSITION)
    public void setRcFlightChannel(HardwareState.FlightModeSwitch flightModeSwitch, final DJISDKCacheHWAbstraction.InnerCallback callback) {
        DataRcSetFlightChannel.getInstance().setFlightChannel(flightModeSwitch.value()).start(new DJIDataCallBack() {
            /* class dji.sdksharedlib.hardware.abstractions.remotecontroller.DJIRCAbstraction.AnonymousClass53 */

            public void onSuccess(Object model) {
                callback.onSuccess(model);
            }

            public void onFailure(Ccode ccode) {
                callback.onSuccess(ccode);
            }
        });
    }

    @Setter(RemoteControllerKeys.SOFT_SWITCH_STICK_MODE)
    public void setRcFlightChannel(SoftSwitchJoyStickMode joyStickMode, final DJISDKCacheHWAbstraction.InnerCallback callback) {
        if (joyStickMode == SoftSwitchJoyStickMode.UNKNOWN) {
            CallbackUtils.onFailure(callback, DJIError.COMMON_PARAM_INVALID);
        } else {
            DataRcSetFlightChannel.getInstance().setFlightChannel(joyStickMode.value()).start(new DJIDataCallBack() {
                /* class dji.sdksharedlib.hardware.abstractions.remotecontroller.DJIRCAbstraction.AnonymousClass54 */

                public void onSuccess(Object model) {
                    CallbackUtils.onSuccess(callback, model);
                }

                public void onFailure(Ccode ccode) {
                    CallbackUtils.onFailure(callback, DJIError.getDJIError(ccode));
                }
            });
        }
    }
}
