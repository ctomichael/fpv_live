package dji.sdksharedlib.hardware.accessory;

import android.text.TextUtils;
import dji.common.error.DJIAccessoryAggregationError;
import dji.common.error.DJIError;
import dji.common.util.CallbackUtils;
import dji.midware.data.config.P3.Ccode;
import dji.midware.data.config.P3.DeviceType;
import dji.midware.data.model.P3.DataCommonGetSNOfMavicRC;
import dji.midware.data.model.P3.DataCommonGetVersion;
import dji.midware.data.model.P3.DataOnBoardSDKGetPushSearchlightInfo;
import dji.midware.data.model.P3.DataOnBoardSDKSetSearchlightParams;
import dji.midware.interfaces.DJIDataCallBack;
import dji.midware.util.DJIEventBusUtil;
import dji.sdksharedlib.extension.CacheHelper;
import dji.sdksharedlib.hardware.abstractions.DJISDKCacheHWAbstraction;
import dji.sdksharedlib.hardware.abstractions.DJISubComponentHWAbstraction;
import dji.sdksharedlib.hardware.abstractions.Getter;
import dji.sdksharedlib.hardware.abstractions.Setter;
import dji.sdksharedlib.keycatalog.DJISDKCacheKeys;
import dji.sdksharedlib.keycatalog.SpotlightKeys;
import dji.sdksharedlib.store.DJISDKCacheStoreLayer;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class SpotlightAbstraction extends DJISubComponentHWAbstraction {
    public void init(String component, int index, String subComponent, int subComponentIndex, DJISDKCacheStoreLayer storeLayer, DJISDKCacheHWAbstraction.OnValueChangeListener onValueChangeListener) {
        super.init(component, index, subComponent, subComponentIndex, storeLayer, onValueChangeListener);
        DJIEventBusUtil.register(this);
    }

    /* access modifiers changed from: protected */
    public void initializeComponentCharacteristics() {
        addCharacteristics(SpotlightKeys.class, getClass());
    }

    public void syncPushDataFromMidware() {
        super.syncPushDataFromMidware();
        if (DataOnBoardSDKGetPushSearchlightInfo.getInstance().isGetted()) {
            onEvent3BackgroundThread(DataOnBoardSDKGetPushSearchlightInfo.getInstance());
        }
    }

    public void destroy() {
        DJIEventBusUtil.unRegister(this);
        super.destroy();
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEvent3BackgroundThread(DataOnBoardSDKGetPushSearchlightInfo info) {
        notifyValueChangeForKeyPath(Integer.valueOf(info.getSearchlightPower()), convertKeyToPath(SpotlightKeys.BRIGHTNESS));
        notifyValueChangeForKeyPath(Boolean.valueOf(info.isSearchlightEnabled()), convertKeyToPath(SpotlightKeys.ENABLED));
        notifyValueChangeForKeyPath(Float.valueOf(((float) info.getSearchlightTemperature()) / 10.0f), convertKeyToPath(SpotlightKeys.TEMPERATURE));
    }

    @Setter(SpotlightKeys.ENABLED)
    public void setEnabled(boolean enabled, DJISDKCacheHWAbstraction.InnerCallback callback) {
        Boolean hasEnabled = (Boolean) CacheHelper.getAccessory(SpotlightKeys.ENABLED);
        if (hasEnabled == null || hasEnabled.booleanValue() != enabled) {
            new DataOnBoardSDKSetSearchlightParams().setParam(enabled ? 1 : 0).setParamType(DataOnBoardSDKSetSearchlightParams.ParamType.ON_OFF.value()).start(CallbackUtils.defaultCB(callback, DJIAccessoryAggregationError.class));
        } else {
            CallbackUtils.onSuccess(callback, (Object) null);
        }
    }

    @Setter(SpotlightKeys.BRIGHTNESS)
    public void setBrightness(int percentage, DJISDKCacheHWAbstraction.InnerCallback callback) {
        new DataOnBoardSDKSetSearchlightParams().setParam(percentage).setParamType(DataOnBoardSDKSetSearchlightParams.ParamType.POWER.value()).start(CallbackUtils.defaultCB(callback, DJIAccessoryAggregationError.class));
    }

    @Getter(DJISDKCacheKeys.SERIAL_NUMBER)
    public void getSerialNumber(final DJISDKCacheHWAbstraction.InnerCallback callback) {
        final DataCommonGetSNOfMavicRC getSNOfMavicRC = new DataCommonGetSNOfMavicRC();
        ((DataCommonGetSNOfMavicRC) getSNOfMavicRC.setDeviceType(DeviceType.OFDM).setReceiverId(3, DataCommonGetSNOfMavicRC.class)).start(new DJIDataCallBack() {
            /* class dji.sdksharedlib.hardware.accessory.SpotlightAbstraction.AnonymousClass1 */

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
            /* class dji.sdksharedlib.hardware.accessory.SpotlightAbstraction.AnonymousClass2 */

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
