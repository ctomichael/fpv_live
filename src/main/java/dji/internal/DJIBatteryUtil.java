package dji.internal;

import dji.common.product.Model;
import dji.fieldAnnotation.EXClassNullAway;
import dji.sdksharedlib.extension.CacheHelper;
import dji.sdksharedlib.keycatalog.ProductKeys;

@EXClassNullAway
public class DJIBatteryUtil {
    public static int getBatteryCellNumber() {
        Model model = (Model) CacheHelper.getProduct(ProductKeys.MODEL_NAME);
        if (isInspireSeries(model)) {
            return 6;
        }
        if (isKumquatSeries(model)) {
            return 3;
        }
        if (isPhantomSeries(model)) {
            return 4;
        }
        return 0;
    }

    public static boolean isInspireSeries(Model model) {
        if (model == null) {
            model = (Model) CacheHelper.getProduct(ProductKeys.MODEL_NAME);
        }
        if (model == null) {
            return false;
        }
        if (model == Model.INSPIRE_1 || model == Model.INSPIRE_1_PRO || model == Model.INSPIRE_1_RAW || model == Model.MATRICE_100 || model == Model.ZENMUSE_Z3 || model == Model.INSPIRE_2 || model == Model.MATRICE_200 || model == Model.MATRICE_210 || model == Model.MATRICE_210_RTK || model == Model.MATRICE_PM420 || model == Model.MATRICE_PM420PRO || model == Model.MATRICE_PM420PRO_RTK) {
            return true;
        }
        return false;
    }

    public static boolean isKumquatSeries(Model model) {
        if (model == null) {
            model = (Model) CacheHelper.getProduct(ProductKeys.MODEL_NAME);
        }
        if (model != null && model == Model.MAVIC_PRO) {
            return true;
        }
        return false;
    }

    public static boolean isPhantomSeries(Model model) {
        if (model == null) {
            model = (Model) CacheHelper.getProduct(ProductKeys.MODEL_NAME);
        }
        if (model == null) {
            return false;
        }
        if (model == Model.Phantom_3_4K || model == Model.PHANTOM_3_ADVANCED || model == Model.PHANTOM_3_PROFESSIONAL || model == Model.PHANTOM_3_STANDARD || model == Model.PHANTOM_4 || model == Model.PHANTOM_4_PRO || model == Model.PHANTOM_4_ADVANCED || model == Model.PHANTOM_4_PRO_V2 || model == Model.PHANTOM_4_RTK) {
            return true;
        }
        return false;
    }
}
