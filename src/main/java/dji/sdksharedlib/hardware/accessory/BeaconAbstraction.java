package dji.sdksharedlib.hardware.accessory;

import android.text.TextUtils;
import dji.common.error.DJIAccessoryAggregationError;
import dji.common.error.DJIError;
import dji.common.util.CallbackUtils;
import dji.internal.logics.CommonUtil;
import dji.midware.data.config.P3.Ccode;
import dji.midware.data.config.P3.DeviceType;
import dji.midware.data.model.P3.DataCommonGetSNOfMavicRC;
import dji.midware.data.model.P3.DataCommonGetVersion;
import dji.midware.data.model.P3.DataOnBoardSDKGetPushBeaconInfo;
import dji.midware.data.model.P3.DataOnBoardSDKSetNavigationLEDParams;
import dji.midware.interfaces.DJIDataCallBack;
import dji.midware.util.DJIEventBusUtil;
import dji.sdksharedlib.extension.CacheHelper;
import dji.sdksharedlib.hardware.abstractions.DJISDKCacheHWAbstraction;
import dji.sdksharedlib.hardware.abstractions.DJISubComponentHWAbstraction;
import dji.sdksharedlib.hardware.abstractions.Getter;
import dji.sdksharedlib.hardware.abstractions.Setter;
import dji.sdksharedlib.keycatalog.BeaconKeys;
import dji.sdksharedlib.keycatalog.DJISDKCacheKeys;
import dji.sdksharedlib.store.DJISDKCacheStoreLayer;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class BeaconAbstraction extends DJISubComponentHWAbstraction {
    public void init(String component, int index, String subComponent, int subComponentIndex, DJISDKCacheStoreLayer storeLayer, DJISDKCacheHWAbstraction.OnValueChangeListener onValueChangeListener) {
        super.init(component, index, subComponent, subComponentIndex, storeLayer, onValueChangeListener);
        DJIEventBusUtil.register(this);
    }

    /* access modifiers changed from: protected */
    public void initializeComponentCharacteristics() {
        addCharacteristics(BeaconKeys.class, getClass());
    }

    public void syncPushDataFromMidware() {
        super.syncPushDataFromMidware();
        if (DataOnBoardSDKGetPushBeaconInfo.getInstance().isGetted()) {
            onEvent3BackgroundThread(DataOnBoardSDKGetPushBeaconInfo.getInstance());
        }
    }

    public void destroy() {
        DJIEventBusUtil.unRegister(this);
        super.destroy();
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEvent3BackgroundThread(DataOnBoardSDKGetPushBeaconInfo info) {
        notifyValueChangeForKeyPath(Boolean.valueOf(info.isNavigationLEDEnabled()), convertKeyToPath(BeaconKeys.ENABLED));
    }

    @Setter(BeaconKeys.ENABLED)
    public void setEnabled(boolean enabled, DJISDKCacheHWAbstraction.InnerCallback callback) {
        Boolean hasEnabled = (Boolean) CacheHelper.getAccessory(BeaconKeys.ENABLED);
        if (hasEnabled == null || hasEnabled.booleanValue() != enabled) {
            DataOnBoardSDKSetNavigationLEDParams setter = new DataOnBoardSDKSetNavigationLEDParams();
            setter.setParam(enabled ? 1 : 0).setParamType(DataOnBoardSDKSetNavigationLEDParams.ParamType.ON_OFF.value());
            if (CommonUtil.isPM420Platform()) {
                setter.setReceiverType(DeviceType.CENTER);
                setter.setReceiverId(0);
            }
            setter.start(CallbackUtils.defaultCB(callback, DJIAccessoryAggregationError.class));
            return;
        }
        CallbackUtils.onSuccess(callback, (Object) null);
    }

    @Setter(BeaconKeys.BRIGHTNESS)
    public void setBrightness(int percentage, DJISDKCacheHWAbstraction.InnerCallback callback) {
        new DataOnBoardSDKSetNavigationLEDParams().setParam(percentage).setParamType(DataOnBoardSDKSetNavigationLEDParams.ParamType.POWER.value()).start(CallbackUtils.defaultCB(callback, DJIAccessoryAggregationError.class));
    }

    @Setter(BeaconKeys.FLICKER_FREQUENCY)
    public void setflickerFrequency(int frequency, DJISDKCacheHWAbstraction.InnerCallback callback) {
        new DataOnBoardSDKSetNavigationLEDParams().setParam(frequency).setParamType(DataOnBoardSDKSetNavigationLEDParams.ParamType.FLICKER_FREQUENCY.value()).start(CallbackUtils.defaultCB(callback, DJIAccessoryAggregationError.class));
    }

    @Getter(DJISDKCacheKeys.SERIAL_NUMBER)
    public void getSerialNumber(final DJISDKCacheHWAbstraction.InnerCallback callback) {
        final DataCommonGetSNOfMavicRC getSNOfMavicRC = new DataCommonGetSNOfMavicRC();
        ((DataCommonGetSNOfMavicRC) getSNOfMavicRC.setDeviceType(DeviceType.OFDM).setReceiverId(3, DataCommonGetSNOfMavicRC.class)).start(new DJIDataCallBack() {
            /* class dji.sdksharedlib.hardware.accessory.BeaconAbstraction.AnonymousClass1 */

            public void onSuccess(Object model) {
                if (TextUtils.isEmpty(getSNOfMavicRC.getSN())) {
                    CallbackUtils.onFailure(callback, DJIError.COMMON_EXECUTION_FAILED);
                } else {
                    CallbackUtils.onSuccess(callback, getSNOfMavicRC.getSN());
                }
            }

            public void onFailure(Ccode ccode) {
                CallbackUtils.onFailure(callback, ccode);
            }
        });
    }

    @Getter(DJISDKCacheKeys.FIRMWARE_VERSION)
    public void getFirmwareVersion(final DJISDKCacheHWAbstraction.InnerCallback callback) {
        final DataCommonGetVersion dataCommonGetVersion = new DataCommonGetVersion();
        dataCommonGetVersion.setDeviceType(DeviceType.OFDM);
        dataCommonGetVersion.setDeviceModel(3);
        dataCommonGetVersion.startForce(new DJIDataCallBack() {
            /* class dji.sdksharedlib.hardware.accessory.BeaconAbstraction.AnonymousClass2 */

            public void onSuccess(Object model) {
                String firmVersion = dataCommonGetVersion.getFirmVer(".");
                if (!TextUtils.isEmpty(firmVersion)) {
                    CallbackUtils.onSuccess(callback, firmVersion);
                } else {
                    CallbackUtils.onFailure(callback, DJIError.UNABLE_TO_GET_FIRMWARE_VERSION);
                }
            }

            public void onFailure(Ccode ccode) {
                CallbackUtils.onFailure(callback, ccode);
            }
        });
    }
}
