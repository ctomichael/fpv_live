package dji.sdksharedlib.hardware.abstractions.product;

import android.support.annotation.NonNull;
import dji.common.flightcontroller.flightassistant.FlightAssistantParamRangeManager;
import dji.common.product.Model;
import dji.fieldAnnotation.EXClassNullAway;
import dji.internal.version.VersionController;
import dji.log.DJILog;
import dji.midware.component.DJIComponentManager;
import dji.midware.data.config.P3.ProductType;
import dji.midware.data.manager.P3.DJIProductManager;
import dji.midware.data.manager.P3.DataEvent;
import dji.midware.data.model.P3.DataCameraActiveStatus;
import dji.midware.data.model.P3.DataFlycActiveStatus;
import dji.midware.data.model.P3.DataOsdActiveStatus;
import dji.midware.data.model.P3.DataOsdGetPushCommon;
import dji.midware.util.DJIEventBusUtil;
import dji.sdksharedlib.extension.KeyHelper;
import dji.sdksharedlib.hardware.abstractions.DJISDKCacheHWAbstraction;
import dji.sdksharedlib.keycatalog.DJISDKCacheKey;
import dji.sdksharedlib.keycatalog.DJISDKCacheKeys;
import dji.sdksharedlib.keycatalog.ProductKeys;
import dji.sdksharedlib.store.DJISDKCacheStoreLayer;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

@EXClassNullAway
public class DJIProductAbstraction extends DJISDKCacheHWAbstraction implements VersionController.VersionChangeObserver {
    private static final String TAG = "DJISDKCacheProductAbstraction";
    private DJIComponentManager.CameraComponentType lastCameraType;
    private DataOsdGetPushCommon.DroneType lastDroneType;
    private DJIComponentManager.PlatformType lastPlatformType;
    private FlightAssistantParamRangeManager mFlightAssistantParamRangeManager;

    /* access modifiers changed from: protected */
    public void initializeComponentCharacteristics() {
        addCharacteristics(ProductKeys.class, getClass());
        VersionController.getInstance().addListener(this);
    }

    /* JADX DEBUG: Failed to find minimal casts for resolve overloaded methods, cast all args instead
     method: dji.sdksharedlib.hardware.abstractions.DJISDKCacheHWAbstraction.notifyValueChangeForKeyPath(java.lang.Object, dji.sdksharedlib.keycatalog.DJISDKCacheKey):void
     arg types: [boolean, dji.sdksharedlib.keycatalog.DJISDKCacheKey]
     candidates:
      dji.sdksharedlib.hardware.abstractions.DJISDKCacheHWAbstraction.notifyValueChangeForKeyPath(java.lang.Object, java.lang.String):void
      dji.sdksharedlib.hardware.abstractions.DJISDKCacheHWAbstraction.notifyValueChangeForKeyPath(java.lang.Object, dji.sdksharedlib.keycatalog.DJISDKCacheKey):void */
    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEvent3BackgroundThread(DataFlycActiveStatus status) {
        notifyValueChangeForKeyPath((Object) false, KeyHelper.getProductKey(ProductKeys.HAS_FLIGHT_CONTROLLER_BEEN_ACTIVATED));
    }

    /* JADX DEBUG: Failed to find minimal casts for resolve overloaded methods, cast all args instead
     method: dji.sdksharedlib.hardware.abstractions.DJISDKCacheHWAbstraction.notifyValueChangeForKeyPath(java.lang.Object, dji.sdksharedlib.keycatalog.DJISDKCacheKey):void
     arg types: [boolean, dji.sdksharedlib.keycatalog.DJISDKCacheKey]
     candidates:
      dji.sdksharedlib.hardware.abstractions.DJISDKCacheHWAbstraction.notifyValueChangeForKeyPath(java.lang.Object, java.lang.String):void
      dji.sdksharedlib.hardware.abstractions.DJISDKCacheHWAbstraction.notifyValueChangeForKeyPath(java.lang.Object, dji.sdksharedlib.keycatalog.DJISDKCacheKey):void */
    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEvent3BackgroundThread(DataCameraActiveStatus status) {
        notifyValueChangeForKeyPath((Object) false, KeyHelper.getProductKey(ProductKeys.HAS_CAMERA_BEEN_ACTIVATED));
    }

    /* JADX DEBUG: Failed to find minimal casts for resolve overloaded methods, cast all args instead
     method: dji.sdksharedlib.hardware.abstractions.DJISDKCacheHWAbstraction.notifyValueChangeForKeyPath(java.lang.Object, dji.sdksharedlib.keycatalog.DJISDKCacheKey):void
     arg types: [boolean, dji.sdksharedlib.keycatalog.DJISDKCacheKey]
     candidates:
      dji.sdksharedlib.hardware.abstractions.DJISDKCacheHWAbstraction.notifyValueChangeForKeyPath(java.lang.Object, java.lang.String):void
      dji.sdksharedlib.hardware.abstractions.DJISDKCacheHWAbstraction.notifyValueChangeForKeyPath(java.lang.Object, dji.sdksharedlib.keycatalog.DJISDKCacheKey):void */
    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEvent3BackgroundThread(DataOsdActiveStatus status) {
        notifyValueChangeForKeyPath((Object) false, KeyHelper.getProductKey(ProductKeys.HAS_OFDM_MODULE_BEEN_ACTIVATED));
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEvent3BackgroundThread(ProductType type) {
        notifyValueChangeForKeyPath(Integer.valueOf(type.value()), KeyHelper.getProductKey(ProductKeys.PRODUCT_CODE));
    }

    /* access modifiers changed from: protected */
    public DJISDKCacheKey genKeyPath(String key) {
        return new DJISDKCacheKey.Builder().component(ProductKeys.COMPONENT_KEY).paramKey(key).build();
    }

    /* JADX DEBUG: Failed to find minimal casts for resolve overloaded methods, cast all args instead
     method: dji.sdksharedlib.hardware.abstractions.DJISDKCacheHWAbstraction.notifyValueChangeForKeyPath(java.lang.Object, dji.sdksharedlib.keycatalog.DJISDKCacheKey):void
     arg types: [boolean, dji.sdksharedlib.keycatalog.DJISDKCacheKey]
     candidates:
      dji.sdksharedlib.hardware.abstractions.DJISDKCacheHWAbstraction.notifyValueChangeForKeyPath(java.lang.Object, java.lang.String):void
      dji.sdksharedlib.hardware.abstractions.DJISDKCacheHWAbstraction.notifyValueChangeForKeyPath(java.lang.Object, dji.sdksharedlib.keycatalog.DJISDKCacheKey):void */
    /* access modifiers changed from: protected */
    public void updateProduct() {
        boolean needUpdate;
        Model modelByCamera;
        boolean z = false;
        DataOsdGetPushCommon.DroneType droneType = DataOsdGetPushCommon.getInstance().getDroneType();
        DJIComponentManager.PlatformType platformType = DJIComponentManager.getInstance().getPlatformType();
        if (platformType == null) {
            platformType = DJIComponentManager.getInstance().getLastPlatformType();
            DJILog.d(TAG, "last platformType : " + platformType, new Object[0]);
        }
        DJIComponentManager.CameraComponentType cameraType = DJIComponentManager.getInstance().getCameraComponentType();
        if (cameraType == null) {
            cameraType = DJIComponentManager.getInstance().getLastCameraComponentType();
            DJILog.d(TAG, "last cameraType: " + cameraType, new Object[0]);
        }
        if (this.lastPlatformType != null && platformType == this.lastPlatformType && droneType == this.lastDroneType && cameraType == this.lastCameraType) {
            needUpdate = false;
        } else {
            needUpdate = true;
        }
        if (needUpdate) {
            Model model = getModel(platformType, cameraType);
            if ((model == null || model == Model.UNKNOWN_AIRCRAFT) && (modelByCamera = getModelByCameraType(cameraType)) != null) {
                model = modelByCamera;
            }
            DJILog.d(TAG, "model : " + model, new Object[0]);
            if (model != null) {
                notifyValueChangeForKeyPath((Object) true, genKeyPath(DJISDKCacheKeys.CONNECTION));
                notifyValueChangeForKeyPath(model, genKeyPath(ProductKeys.MODEL_NAME));
                if (platformType == DJIComponentManager.PlatformType.OSMO || platformType == DJIComponentManager.PlatformType.OSMOMobile) {
                    z = true;
                }
                notifyValueChangeForKeyPath(Boolean.valueOf(z), genKeyPath(ProductKeys.IS_OSMO));
            } else {
                notifyValueChangeForKeyPath((Object) false, genKeyPath(DJISDKCacheKeys.CONNECTION));
                notifyValueChangeForKeyPath((Object) null, genKeyPath(ProductKeys.MODEL_NAME));
                notifyValueChangeForKeyPath((Object) null, genKeyPath(ProductKeys.IS_OSMO));
            }
        }
        this.lastPlatformType = platformType;
        this.lastDroneType = droneType;
        this.lastCameraType = cameraType;
    }

    private Model getModel(@NonNull DJIComponentManager.PlatformType platformType, @NonNull DJIComponentManager.CameraComponentType cameraType) {
        switch (platformType) {
            case OSMOMobile:
                if (DJIComponentManager.getInstance().getGimbalComponentType() == DJIComponentManager.GimbalComponentType.OSMO_2) {
                    return Model.OSMO_MOBILE_2;
                }
                return Model.OSMO_MOBILE;
            case Inspire:
                if (cameraType == DJIComponentManager.CameraComponentType.X3 || cameraType == DJIComponentManager.CameraComponentType.TAU336 || cameraType == DJIComponentManager.CameraComponentType.TAU640 || cameraType == DJIComponentManager.CameraComponentType.Z3) {
                    return Model.INSPIRE_1;
                }
                if (cameraType == DJIComponentManager.CameraComponentType.X5) {
                    return Model.INSPIRE_1_PRO;
                }
                if (cameraType == DJIComponentManager.CameraComponentType.X5R) {
                    return Model.INSPIRE_1_RAW;
                }
                return Model.INSPIRE_1;
            case M100:
                return Model.MATRICE_100;
            case P3c:
                return Model.PHANTOM_3_STANDARD;
            case P3s:
                return Model.PHANTOM_3_ADVANCED;
            case P3x:
                return Model.PHANTOM_3_PROFESSIONAL;
            case P3w:
                return Model.Phantom_3_4K;
            case P3se:
                return Model.Phantom_3_SE;
            case OSMO:
                if (cameraType == DJIComponentManager.CameraComponentType.X5) {
                    return Model.OSMO_PRO;
                }
                if (cameraType == DJIComponentManager.CameraComponentType.X5R) {
                    return Model.OSMO_RAW;
                }
                if (cameraType == DJIComponentManager.CameraComponentType.Z3) {
                    return Model.OSMO_PLUS;
                }
                return Model.OSMO;
            case A3:
                return Model.A3;
            case N3:
                return Model.N3;
            case Unknown:
                return Model.UNKNOWN_AIRCRAFT;
            case P4:
                return Model.PHANTOM_4;
            case M600:
                return Model.MATRICE_600;
            case M600Pro:
                return Model.MATRICE_600_PRO;
            case Inspire2:
                return Model.INSPIRE_2;
            case M200:
                return Model.MATRICE_200;
            case M210:
                return Model.MATRICE_210;
            case M210RTK:
                return Model.MATRICE_210_RTK;
            case PM420:
                return Model.MATRICE_PM420;
            case PM420PRO:
                return Model.MATRICE_PM420PRO;
            case PM420PRO_RTK:
                return Model.MATRICE_PM420PRO_RTK;
            case FoldingDrone:
                return Model.MAVIC_PRO;
            case P4P:
                return Model.PHANTOM_4_PRO;
            case P4A:
                return Model.PHANTOM_4_ADVANCED;
            case P4PSDR:
                return Model.PHANTOM_4_PRO_V2;
            case P4RTK:
                return Model.PHANTOM_4_RTK;
            case Spark:
                return Model.Spark;
            case WM230:
                return Model.MAVIC_AIR;
            case WM240:
                return getMavic2Model(cameraType);
            case WM245:
                return getMavic2EnterpriseModel(cameraType);
            case WM160:
                return Model.WM160;
            default:
                return null;
        }
    }

    private Model getModelByCameraType(DJIComponentManager.CameraComponentType cameraType) {
        switch (cameraType) {
            case WM160:
                return Model.WM160;
            default:
                return null;
        }
    }

    private Model getMavic2Model(DJIComponentManager.CameraComponentType cameraComponentType) {
        switch (cameraComponentType) {
            case WM240:
                return Model.MAVIC_2_ZOOM;
            case WM240Hasselblad:
                return Model.MAVIC_2_PRO;
            default:
                return Model.MAVIC_2;
        }
    }

    private Model getMavic2EnterpriseModel(DJIComponentManager.CameraComponentType cameraComponentType) {
        switch (cameraComponentType) {
            case WM245:
                return Model.MAVIC_2_ENTERPRISE;
            case WM245DualLightCamera:
                return Model.MAVIC_2_ENTERPRISE_DUAL;
            default:
                return Model.MAVIC_2_ENTERPRISE;
        }
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEvent3BackgroundThread(DJIComponentManager.RcComponentType type) {
        updateProduct();
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEvent3BackgroundThread(DJIComponentManager.CameraComponentType type) {
        if (!type.equals(DJIComponentManager.CameraComponentType.Unknow)) {
            updateProduct();
        }
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEvent3BackgroundThread(DJIComponentManager.PlatformType type) {
        updateProduct();
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEvent3BackgroundThread(DataEvent event) {
        DJIComponentManager.RcComponentType rcComponentType = DJIComponentManager.getInstance().getRcComponentType();
        notifyValueChangeForKeyPath(Boolean.valueOf(event.equals(DataEvent.ConnectOK)), KeyHelper.getProductKey(ProductKeys.HAS_REMOTE_CONTROLLER));
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onEvent3BackgroundThread(DataOsdGetPushCommon data) {
        if (data.getDroneType() != this.lastDroneType) {
            updateProduct();
        }
    }

    public void init(String component, int index, DJISDKCacheStoreLayer storeLayer, DJISDKCacheHWAbstraction.OnValueChangeListener onValueChangeListener) {
        super.init(component, index, storeLayer, onValueChangeListener);
        DJIEventBusUtil.register(this);
        this.mFlightAssistantParamRangeManager = new FlightAssistantParamRangeManager(onValueChangeListener, this.defaultKeyPath);
    }

    public void destroy() {
        DJIEventBusUtil.unRegister(this);
        VersionController.getInstance().removeListener(this);
        super.destroy();
    }

    /* JADX DEBUG: Failed to find minimal casts for resolve overloaded methods, cast all args instead
     method: dji.sdksharedlib.hardware.abstractions.DJISDKCacheHWAbstraction.notifyValueChangeForKeyPath(java.lang.Object, dji.sdksharedlib.keycatalog.DJISDKCacheKey):void
     arg types: [boolean, dji.sdksharedlib.keycatalog.DJISDKCacheKey]
     candidates:
      dji.sdksharedlib.hardware.abstractions.DJISDKCacheHWAbstraction.notifyValueChangeForKeyPath(java.lang.Object, java.lang.String):void
      dji.sdksharedlib.hardware.abstractions.DJISDKCacheHWAbstraction.notifyValueChangeForKeyPath(java.lang.Object, dji.sdksharedlib.keycatalog.DJISDKCacheKey):void */
    public void syncPushDataFromMidware() {
        super.syncPushDataFromMidware();
        updateProduct();
        notifyValueChangeForKeyPath((Object) true, KeyHelper.getProductKey(ProductKeys.HAS_FLIGHT_CONTROLLER_BEEN_ACTIVATED));
        notifyValueChangeForKeyPath((Object) true, KeyHelper.getProductKey(ProductKeys.HAS_CAMERA_BEEN_ACTIVATED));
        notifyValueChangeForKeyPath((Object) true, KeyHelper.getProductKey(ProductKeys.HAS_OFDM_MODULE_BEEN_ACTIVATED));
        notifyValueChangeForKeyPath(Integer.valueOf(DJIProductManager.getInstance().getType().value()), KeyHelper.getProductKey(ProductKeys.PRODUCT_CODE));
    }

    public void onVersionChange(String oldVersion, String newVersion) {
        notifyValueChangeForKeyPath(newVersion, KeyHelper.getProductKey(ProductKeys.FIRMWARE_PACKAGE_VERSION));
    }
}
