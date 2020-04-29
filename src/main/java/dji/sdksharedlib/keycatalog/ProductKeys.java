package dji.sdksharedlib.keycatalog;

import dji.common.mission.activetrack.QuickShotMode;
import dji.common.product.Model;
import dji.fieldAnnotation.EXClassNullAway;
import dji.sdksharedlib.hardware.abstractions.DJISDKCacheUpdateType;
import dji.sdksharedlib.keycatalog.extension.InternalKey;
import dji.sdksharedlib.keycatalog.extension.Key;

@EXClassNullAway
public class ProductKeys extends DJISDKCacheKeys {
    public static final String COMPONENT_KEY = "Product";
    @Key(accessType = 4, type = String.class)
    public static final String FIRMWARE_PACKAGE_VERSION = "FirmwarePackageVersion";
    @InternalKey
    @Key(accessType = 4, type = Boolean.class)
    public static final String HAS_CAMERA_BEEN_ACTIVATED = "HasCameraBeenActivated";
    @InternalKey
    @Key(accessType = 4, type = Boolean.class)
    public static final String HAS_FLIGHT_CONTROLLER_BEEN_ACTIVATED = "HasFlightControllerBeenActivated";
    @InternalKey
    @Key(accessType = 4, type = Boolean.class)
    public static final String HAS_OFDM_MODULE_BEEN_ACTIVATED = "HasOFDMModuleBeenActivated";
    @InternalKey
    @Key(accessType = 4, type = Boolean.class)
    public static final String HAS_REMOTE_CONTROLLER = "HasRemoteController";
    @Key(accessType = 4, type = Boolean.class)
    public static final String IS_OSMO = "IsOSMO";
    @Key(accessType = 4, type = Model.class)
    public static final String MODEL_NAME = "ModelName";
    @InternalKey
    @Key(accessType = 4, type = Integer.class)
    public static final String PRODUCT_CODE = "ProductCode";
    @InternalKey
    @Key(accessType = 4, type = QuickShotMode[].class, updateType = DJISDKCacheUpdateType.USER_DRIVEN)
    public static final String QUICK_SHOT_MODE_RANGE = "QuickShotModeRange";

    public ProductKeys(String name) {
        super(name);
    }
}
