package dji.sdksharedlib.hardware.abstractions.handheldcontroller;

import android.text.TextUtils;
import dji.common.error.DJIError;
import dji.common.error.DJISDKCacheError;
import dji.common.handheld.PowerMode;
import dji.fieldAnnotation.EXClassNullAway;
import dji.log.DJILog;
import dji.midware.data.config.P3.Ccode;
import dji.midware.data.config.P3.DeviceType;
import dji.midware.data.model.P3.DataCommonGetVersion;
import dji.midware.data.model.P3.DataOsdGetPushPowerStatus;
import dji.midware.interfaces.DJIDataCallBack;
import dji.midware.util.DJIEventBusUtil;
import dji.sdksharedlib.hardware.abstractions.DJISDKCacheHWAbstraction;
import dji.sdksharedlib.hardware.abstractions.Getter;
import dji.sdksharedlib.keycatalog.DJISDKCacheKeys;
import dji.sdksharedlib.keycatalog.HandheldControllerKeys;
import dji.sdksharedlib.store.DJISDKCacheStoreLayer;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

@EXClassNullAway
public class HandheldControllerBaseAbstraction extends DJISDKCacheHWAbstraction {
    private static final String TAG = "DJISDKCacheHandheldControllerAbstraction";

    public void init(String component, int index, DJISDKCacheStoreLayer storeLayer, DJISDKCacheHWAbstraction.OnValueChangeListener onValueChangeListener) {
        super.init(component, index, storeLayer, onValueChangeListener);
        DJIEventBusUtil.register(this);
    }

    public void destroy() {
        DJIEventBusUtil.unRegister(this);
        super.destroy();
    }

    /* access modifiers changed from: protected */
    public void initializeComponentCharacteristics() {
        addCharacteristics(HandheldControllerKeys.class, getClass());
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEvent3BackgroundThread(DataOsdGetPushPowerStatus param) {
        if (param != null) {
            notifyValueChangeForKeyPath(PowerMode.find(param.getPowerStatus()), convertKeyToPath(HandheldControllerKeys.POWER_MODE));
            DJILog.d(TAG, "DJISDKCacheHandheldControllerAbstraction onEventBackgroundThread  Powermode " + PowerMode.find(param.getPowerStatus()), new Object[0]);
        }
    }

    @Getter(DJISDKCacheKeys.FIRMWARE_VERSION)
    public void getFirmwareVersion(final DJISDKCacheHWAbstraction.InnerCallback callback) {
        final DataCommonGetVersion dcgv = new DataCommonGetVersion();
        dcgv.setDeviceType(DeviceType.DM368_G);
        dcgv.start(new DJIDataCallBack() {
            /* class dji.sdksharedlib.hardware.abstractions.handheldcontroller.HandheldControllerBaseAbstraction.AnonymousClass1 */

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
                    callback.onFails(DJISDKCacheError.DISCONNECTED);
                }
            }
        });
    }
}
