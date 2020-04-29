package dji.sdksharedlib.util.abstractions;

import dji.common.camera.SettingsDefinitions;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.config.P3.ProductType;
import dji.midware.data.manager.P3.DJIProductManager;
import dji.midware.data.model.P3.DataRcGetPushParams;
import dji.sdksharedlib.extension.CacheHelper;

@EXClassNullAway
public class DJIRCAbstractionUtil {
    private static final String TAG = "DJIRCAbstractionUtil";

    public static void executeCameraAction(DataRcGetPushParams params, int cameraIndex) {
        SettingsDefinitions.CameraMode cameraMode = (SettingsDefinitions.CameraMode) CacheHelper.getCamera(cameraIndex, "Mode");
    }

    public static boolean isRemoteControllerHaveTwoSwitchMode(ProductType type) {
        if (type == null) {
            type = DJIProductManager.getInstance().getType();
        }
        return type == ProductType.KumquatS || type == ProductType.KumquatX || type == ProductType.WM230 || type == ProductType.Mammoth;
    }
}
