package dji.sdksharedlib.hardware.abstractions.remotecontroller;

import android.text.TextUtils;
import dji.common.error.DJIError;
import dji.common.error.DJIRemoteControllerError;
import dji.common.remotecontroller.AircraftMappingStyle;
import dji.common.remotecontroller.RCMode;
import dji.common.util.CallbackUtils;
import dji.internal.RcC3SdrUsersConfigHelper;
import dji.midware.data.config.P3.Ccode;
import dji.midware.data.config.P3.DeviceType;
import dji.midware.data.model.P3.DataCommonGetSNOfMavicRC;
import dji.midware.data.model.P3.DataModule4GGetPushOperator;
import dji.midware.data.model.P3.DataModule4GGetPushSignal;
import dji.midware.data.model.P3.DataRcGetControlMode;
import dji.midware.data.model.P3.DataRcGetMaster;
import dji.midware.data.model.P3.DataRcSetControlMode;
import dji.midware.data.model.P3.DataRcSetMaster;
import dji.midware.data.model.P3.DataWifiGetCountryCode;
import dji.midware.interfaces.DJIDataCallBack;
import dji.midware.util.DJIEventBusUtil;
import dji.sdksharedlib.DJISDKCache;
import dji.sdksharedlib.hardware.abstractions.Action;
import dji.sdksharedlib.hardware.abstractions.DJISDKCacheHWAbstraction;
import dji.sdksharedlib.hardware.abstractions.Getter;
import dji.sdksharedlib.hardware.abstractions.Setter;
import dji.sdksharedlib.keycatalog.RemoteControllerKeys;
import dji.sdksharedlib.store.DJISDKCacheStoreLayer;
import dji.sdksharedlib.util.LteSignalHelper;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class DJIRCC3SDRAbstraction extends DJIRCProfessionalAbstraction {
    private LteSignalHelper mLteSignalHelper = new LteSignalHelper(new DJIRCC3SDRAbstraction$$Lambda$0(this), DeviceType.OSD.value());

    /* access modifiers changed from: package-private */
    public final /* synthetic */ void lambda$new$0$DJIRCC3SDRAbstraction(Object newValue) {
        notifyValueChangeForKeyPath(newValue, convertKeyToPath("LteStatus"));
    }

    /* access modifiers changed from: protected */
    public String getComponentDisplayName() {
        return "C3SDR Remote Controller";
    }

    public DJIRCC3SDRAbstraction() {
        this.hardwareState.getMenuButton().setPresent(true);
        this.supportMasterSlaveModeV2 = false;
        this.supportMasterSlaveMode = false;
    }

    public void init(String component, int index, DJISDKCacheStoreLayer storeLayer, DJISDKCacheHWAbstraction.OnValueChangeListener onValueChangeListener) {
        this.helper = RcC3SdrUsersConfigHelper.getInstance();
        super.init(component, index, storeLayer, onValueChangeListener);
        DJISDKCache.getInstance().getValue(convertKeyToPath("Mode"), null);
        DJIEventBusUtil.register(this);
    }

    public void destroy() {
        super.destroy();
        DJIEventBusUtil.unRegister(this);
    }

    @Getter("Mode")
    public void getRemoteControllerMode(final DJISDKCacheHWAbstraction.InnerCallback callback) {
        DataRcGetMaster.getInstance().start(new DJIDataCallBack() {
            /* class dji.sdksharedlib.hardware.abstractions.remotecontroller.DJIRCC3SDRAbstraction.AnonymousClass1 */

            public void onSuccess(Object model) {
                CallbackUtils.onSuccess(callback, RCMode.find(((DataRcGetMaster) model).getMode().value()));
            }

            public void onFailure(Ccode ccode) {
                CallbackUtils.onFailure(callback, DJIRemoteControllerError.getDJIError(ccode));
            }
        });
    }

    @Setter("Mode")
    public void setRemoteControllerMode(RCMode value, DJISDKCacheHWAbstraction.InnerCallback callback) {
        RCMode param = value;
        if (param == RCMode.MASTER || param == RCMode.SLAVE) {
            DataRcSetMaster.getInstance().setMode(DataRcSetMaster.MODE.find(DataRcSetMaster.MODE.find(param.value()).value())).start(CallbackUtils.defaultCB(callback, DJIRemoteControllerError.class));
            return;
        }
        CallbackUtils.onFailure(callback, DJIRemoteControllerError.COMMON_UNSUPPORTED);
    }

    @Getter(RemoteControllerKeys.AIRCRAFT_MAPPING_STYLE)
    public void getAircraftMappingStyle(final DJISDKCacheHWAbstraction.InnerCallback callback) {
        DataRcGetControlMode.getInstance().setReceiverType(DeviceType.RC).start(new DJIDataCallBack() {
            /* class dji.sdksharedlib.hardware.abstractions.remotecontroller.DJIRCC3SDRAbstraction.AnonymousClass2 */

            public void onSuccess(Object model) {
                AircraftMappingStyle aircraftMappingStyle = AircraftMappingStyle.STYLE_2;
                CallbackUtils.onSuccess(callback, AircraftMappingStyle.find(DataRcGetControlMode.getInstance().getControlType().value()));
            }

            public void onFailure(Ccode ccode) {
                CallbackUtils.onFailure(callback, DJIRemoteControllerError.getDJIError(ccode));
            }
        });
    }

    @Setter(RemoteControllerKeys.AIRCRAFT_MAPPING_STYLE)
    public void setAircraftMappingStyle(AircraftMappingStyle style, DJISDKCacheHWAbstraction.InnerCallback callback) {
        if (style == null) {
            CallbackUtils.onFailure(callback, DJIRemoteControllerError.COMMON_PARAM_ILLEGAL);
            return;
        }
        DataRcSetControlMode.getInstance().setControlType(DataRcSetControlMode.ControlMode.find(DataRcSetControlMode.ControlMode.find(style.value()).value())).setChannels(this.preference).setReceiverType(DeviceType.RC).start(CallbackUtils.defaultCB(callback, DJIRemoteControllerError.class));
    }

    @Action(RemoteControllerKeys.RESET_BUTTON_CONFIG)
    public void restoreToDefault(final DJISDKCacheHWAbstraction.InnerCallback callback) {
        ((RcC3SdrUsersConfigHelper) this.helper).reset(new DJIDataCallBack() {
            /* class dji.sdksharedlib.hardware.abstractions.remotecontroller.DJIRCC3SDRAbstraction.AnonymousClass3 */

            public void onSuccess(Object model) {
                CallbackUtils.onSuccess(callback, (Object) null);
            }

            public void onFailure(Ccode ccode) {
                CallbackUtils.onFailure(callback, DJIRemoteControllerError.getDJIError(ccode));
            }
        });
    }

    @Action(RemoteControllerKeys.ADD_BUTTON_PROFILE_GROUP)
    public void addProRCUser(DJISDKCacheHWAbstraction.InnerCallback callback, String userIDString) {
        CallbackUtils.onFailure(callback, DJIError.COMMON_UNSUPPORTED);
    }

    @Action(RemoteControllerKeys.REMOVE_BUTTON_PROFILE_GROUP)
    public void deleteProRCUser(DJISDKCacheHWAbstraction.InnerCallback callback, String userIDString) {
        CallbackUtils.onFailure(callback, DJIError.COMMON_UNSUPPORTED);
    }

    /* access modifiers changed from: protected */
    public void switchModeInData() {
    }

    public void getSerialNumber(final DJISDKCacheHWAbstraction.InnerCallback callback) {
        final DataCommonGetSNOfMavicRC rc = new DataCommonGetSNOfMavicRC();
        rc.start(new DJIDataCallBack() {
            /* class dji.sdksharedlib.hardware.abstractions.remotecontroller.DJIRCC3SDRAbstraction.AnonymousClass4 */

            public void onSuccess(Object model) {
                if (TextUtils.isEmpty(rc.getSN())) {
                    CallbackUtils.onFailure(callback, DJIError.COMMON_EXECUTION_FAILED);
                } else {
                    CallbackUtils.onSuccess(callback, rc.getSN());
                }
            }

            public void onFailure(Ccode ccode) {
                if (ccode == Ccode.GET_PARAM_FAILED) {
                    CallbackUtils.onSuccess(callback, "00000000000000");
                }
                CallbackUtils.onFailure(callback, ccode);
            }
        });
    }

    public void getCountryCode(final DJISDKCacheHWAbstraction.InnerCallback callback) {
        new DataWifiGetCountryCode().start(new DJIDataCallBack() {
            /* class dji.sdksharedlib.hardware.abstractions.remotecontroller.DJIRCC3SDRAbstraction.AnonymousClass5 */

            public void onSuccess(Object model) {
                String countryCode = ((DataWifiGetCountryCode) model).getCountryCode();
                if (!TextUtils.isEmpty(countryCode)) {
                    CallbackUtils.onSuccess(callback, countryCode);
                } else {
                    CallbackUtils.onFailure(callback, DJIError.COMMON_EXECUTION_FAILED);
                }
            }

            public void onFailure(Ccode ccode) {
                CallbackUtils.onFailure(callback, ccode);
            }
        });
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEvent3BackgroundThread(DataModule4GGetPushOperator operator) {
        this.mLteSignalHelper.operatorChange(operator);
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEvent3BackgroundThread(DataModule4GGetPushSignal signal) {
        this.mLteSignalHelper.signalChange(signal);
    }
}
