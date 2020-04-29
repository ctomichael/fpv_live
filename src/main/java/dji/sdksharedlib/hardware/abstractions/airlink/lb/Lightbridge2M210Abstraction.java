package dji.sdksharedlib.hardware.abstractions.airlink.lb;

import dji.common.error.DJIAirLinkError;
import dji.common.error.DJIError;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.MidWare;
import dji.midware.data.config.P3.Ccode;
import dji.midware.data.model.P3.DataOnboardGetPushMixInfo;
import dji.midware.data.model.P3.DataSingleSetMainCameraBandwidthPercent;
import dji.midware.interfaces.DJIDataCallBack;
import dji.midware.util.DjiSharedPreferencesManager;
import dji.midware.util.DoubleCameraSupportUtil;
import dji.sdksharedlib.hardware.abstractions.DJISDKCacheHWAbstraction;
import dji.sdksharedlib.keycatalog.airlink.LightbridgeLinkKeys;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

@EXClassNullAway
public class Lightbridge2M210Abstraction extends Lightbridge2Phantom4PAbstraction {
    public void syncPushDataFromMidware() {
        super.syncPushDataFromMidware();
        onEvent3BackgroundThread(DataOnboardGetPushMixInfo.getInstance());
    }

    public void setBandwidthAllocationForLeftCamera(float percent, final DJISDKCacheHWAbstraction.InnerCallback callback) {
        if (percent >= 0.0f && percent <= 1.0f) {
            final int tenths = Math.round(10.0f * percent);
            new DataSingleSetMainCameraBandwidthPercent().setPercent(tenths).start(new DJIDataCallBack() {
                /* class dji.sdksharedlib.hardware.abstractions.airlink.lb.Lightbridge2M210Abstraction.AnonymousClass1 */

                public void onSuccess(Object model) {
                    if (callback != null) {
                        callback.onSuccess(null);
                    }
                    if (MidWare.context.get() != null) {
                        DjiSharedPreferencesManager.putInt(MidWare.context.get(), DoubleCameraSupportUtil.USER_SET_MAIN_CAMERA_BANDWIDTH_PERCENT, tenths);
                    }
                }

                public void onFailure(Ccode ccode) {
                    if (callback != null) {
                        callback.onFails(DJIError.getDJIError(ccode));
                    }
                }
            });
        } else if (callback != null) {
            callback.onFails(DJIAirLinkError.COMMON_PARAM_ILLEGAL);
        }
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEvent3BackgroundThread(DataOnboardGetPushMixInfo params) {
        if (params != null && params.isGetted()) {
            int data = params.getMainCameraPercentRelativeToWholeCameraBandwidth();
            notifyValueChangeForKeyPath(Float.valueOf(((float) data) / 10.0f), convertKeyToPath(LightbridgeLinkKeys.BANDWIDTH_ALLOCATION_FOR_LEFT_CAMERA));
            DoubleCameraSupportUtil.setMainCameraBandwidthPercent(data);
        }
    }
}
