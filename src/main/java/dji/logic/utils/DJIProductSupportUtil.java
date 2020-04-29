package dji.logic.utils;

import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.config.P3.ProductType;
import dji.midware.data.manager.P3.DJIProductManager;
import dji.midware.data.model.P3.DataCameraGetPushStateInfo;
import java.util.HashMap;

@EXClassNullAway
public class DJIProductSupportUtil {
    public static boolean isLonganSeries(ProductType productType) {
        if (productType == null) {
            productType = DJIProductManager.getInstance().getType();
        }
        if (productType == ProductType.Longan || productType == ProductType.LonganPro || productType == ProductType.LonganRaw || productType == ProductType.LonganZoom || productType == ProductType.LonganMobile || productType == ProductType.LonganMobile2) {
            return true;
        }
        return false;
    }

    public static boolean isLonganMobile(ProductType productType) {
        if (productType == null) {
            productType = DJIProductManager.getInstance().getType();
        }
        if (productType == ProductType.LonganMobile) {
            return true;
        }
        return false;
    }

    public static boolean isLonganMobile2(ProductType productType) {
        if (productType == null) {
            productType = DJIProductManager.getInstance().getType();
        }
        if (productType == ProductType.LonganMobile2) {
            return true;
        }
        return false;
    }

    public static boolean isLonganPhone(ProductType productType) {
        if (productType == null) {
            productType = DJIProductManager.getInstance().getType();
        }
        if (productType == ProductType.LonganMobile) {
            return true;
        }
        return false;
    }

    public static boolean isSupportPano(DataCameraGetPushStateInfo.CameraType type) {
        if (type == null) {
            type = DJIProductManager.getInstance().getCameraType();
        }
        return type == DataCameraGetPushStateInfo.CameraType.DJICameraTypeFC350;
    }

    public static boolean isProductLongan(ProductType type) {
        if (type == null) {
            type = DJIProductManager.getInstance().getType();
        }
        return isLonganSeries(type);
    }

    public static boolean isNo368(ProductType type) {
        if (type == null) {
            type = DJIProductManager.getInstance().getType();
        }
        return (type == ProductType.Longan && DataCameraGetPushStateInfo.getInstance().getVerstion() >= 4) || type == ProductType.LonganZoom;
    }

    public static ProductType getProductTypeFormExfMap(HashMap<String, String> map) {
        if (map == null) {
            return ProductType.OTHER;
        }
        ProductType productType = ProductType.None;
        String camera = map.get("mdl");
        DataCameraGetPushStateInfo.CameraType cameraType = null;
        if ("FC300S".equals(camera)) {
            cameraType = DataCameraGetPushStateInfo.CameraType.DJICameraTypeFC300S;
        } else if ("FC300X".equals(camera)) {
            cameraType = DataCameraGetPushStateInfo.CameraType.DJICameraTypeFC300X;
        } else if ("FC260".equals(camera)) {
            cameraType = DataCameraGetPushStateInfo.CameraType.DJICameraTypeFC260;
        } else if ("FC350".equals(camera)) {
            cameraType = DataCameraGetPushStateInfo.CameraType.DJICameraTypeFC350;
        } else if ("HG310".equals(camera)) {
            return ProductType.Longan;
        } else {
            if ("OSMO RAW".equals(camera)) {
                return ProductType.LonganRaw;
            }
            if ("OSMO PRO".equals(camera)) {
                return ProductType.LonganPro;
            }
            if ("HF310Z".equalsIgnoreCase(camera)) {
                return ProductType.LonganZoom;
            }
            if ("FC300XW".equalsIgnoreCase(camera)) {
                cameraType = DataCameraGetPushStateInfo.CameraType.DJICameraTypeFC300XW;
            } else if ("FC550RAW".equalsIgnoreCase(camera)) {
                cameraType = DataCameraGetPushStateInfo.CameraType.DJICameraTypeFC550Raw;
            } else if ("FC550".equalsIgnoreCase(camera)) {
                cameraType = DataCameraGetPushStateInfo.CameraType.DJICameraTypeFC550;
            } else if ("FC330".equalsIgnoreCase(camera)) {
                cameraType = DataCameraGetPushStateInfo.CameraType.DJICameraTypeFC330X;
            } else if (camera == null) {
            }
        }
        if (productType != ProductType.None) {
            return productType;
        }
        if (cameraType == DataCameraGetPushStateInfo.CameraType.DJICameraTypeFC300S) {
            return ProductType.litchiS;
        }
        if (cameraType == DataCameraGetPushStateInfo.CameraType.DJICameraTypeFC300X) {
            return ProductType.litchiX;
        }
        if (cameraType == DataCameraGetPushStateInfo.CameraType.DJICameraTypeFC260) {
            return ProductType.litchiC;
        }
        if (cameraType == DataCameraGetPushStateInfo.CameraType.DJICameraTypeFC350) {
            return ProductType.Orange;
        }
        if (cameraType == DataCameraGetPushStateInfo.CameraType.DJICameraTypeFC300XW) {
            return ProductType.P34K;
        }
        if (cameraType == DataCameraGetPushStateInfo.CameraType.DJICameraTypeFC550) {
            return ProductType.Orange;
        }
        if (cameraType == DataCameraGetPushStateInfo.CameraType.DJICameraTypeFC550Raw) {
            return ProductType.OrangeRAW;
        }
        if (cameraType == DataCameraGetPushStateInfo.CameraType.DJICameraTypeFC330X) {
            return ProductType.Tomato;
        }
        return ProductType.OTHER;
    }

    public static String getProductName(ProductType type) {
        if (ProductType.Orange == type || ProductType.BigBanana == type || ProductType.OrangeRAW == type || ProductType.Olives == type) {
            return "WM610";
        }
        if (ProductType.litchiS == type || ProductType.litchiX == type) {
            return "P3XS";
        }
        if (ProductType.litchiC == type) {
            return "P3C";
        }
        if (ProductType.Longan == type) {
            return "OSMO";
        }
        if (ProductType.LonganPro == type) {
            return "OSMO_X5";
        }
        if (ProductType.LonganRaw == type) {
            return "OSMO_X5R";
        }
        if (ProductType.LonganMobile == type) {
            return "HG300";
        }
        if (ProductType.P34K == type) {
            return "P3XW";
        }
        if (ProductType.LonganZoom == type) {
            return "OSMO_FC350Z";
        }
        return null;
    }

    public static boolean isSupportMotionTlp(ProductType type) {
        if (type == null) {
            type = DJIProductManager.getInstance().getType();
        }
        return type == ProductType.LonganZoom;
    }

    public static boolean isSupportAutoGotoPreview(ProductType type) {
        if (type == null) {
            type = DJIProductManager.getInstance().getType();
        }
        return type == ProductType.LonganMobile;
    }

    public static int getUpgradeBatteryLimit(ProductType type) {
        if (type == null) {
            type = DJIProductManager.getInstance().getType();
        }
        if (type == ProductType.LonganMobile) {
            return 30;
        }
        return 50;
    }

    public static boolean hasESC(ProductType type) {
        if (type == null) {
            type = DJIProductManager.getInstance().getType();
        }
        return type == ProductType.LonganZoom;
    }
}
