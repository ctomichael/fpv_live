package dji.common.util;

import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.component.DJIComponentManager;
import dji.midware.data.config.P3.Ccode;
import dji.midware.data.model.P3.DataFlycSetParams;
import dji.midware.data.params.P3.ParamCfgName;
import dji.midware.interfaces.DJIDataCallBack;
import dji.sdksharedlib.hardware.abstractions.DJISDKCacheHWAbstraction;

@EXClassNullAway
public class MultiModeEnabledUtil {
    private static final String INTERNAL_FIRMWARE_VERSION = "03.01";

    public static void setMultiModeEnabled(final DJISDKCacheHWAbstraction.InnerCallback callback) {
        String[] modes = {ParamCfgName.GCONFIG_BEGINNER_MODE, "g_config.control.multi_control_mode_enable_0"};
        DataFlycSetParams flycSetParams = new DataFlycSetParams();
        flycSetParams.setIndexs(modes);
        flycSetParams.setValues(0, 1);
        flycSetParams.start(new DJIDataCallBack() {
            /* class dji.common.util.MultiModeEnabledUtil.AnonymousClass1 */

            public void onSuccess(Object model) {
                CallbackUtils.onSuccess(callback, (Object) null);
            }

            public void onFailure(Ccode ccode) {
                CallbackUtils.onFailure(callback, ccode);
            }
        });
    }

    public static boolean verifyRCMode(DJISDKCacheHWAbstraction.InnerCallback callback) {
        if (DJIComponentManager.getInstance().getPlatformType() == null || !DJIComponentManager.getInstance().getPlatformType().equals(DJIComponentManager.PlatformType.OSMO)) {
            return true;
        }
        return false;
    }
}
