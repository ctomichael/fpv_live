package dji.publics.utils;

import dji.fieldAnnotation.EXClassNullAway;
import java.util.Locale;

@EXClassNullAway
public class LanguageUtils {
    public static final String CN_DJI_LANG_CODE = "cn";
    public static final String HK_DJI_LANG_CODE = "hk";
    public static final String JA_DJI_LANG_CODE = "ja";
    public static final String JP_DJI_LANG_CODE = "jp";
    public static final String MO_DJI_LANG_CODE = "mo";
    private static final String TAG = "LanguageUtils";
    public static final String TW_DJI_LANG_CODE = "tw";
    public static final String ZH_DJI_LANG_CODE = "zh";

    public static String getLanguage() {
        String systemLanguage = Locale.getDefault().getLanguage().toLowerCase();
        if (ZH_DJI_LANG_CODE.equals(systemLanguage)) {
            String areaLanguage = Locale.getDefault().getCountry().toLowerCase();
            if (TW_DJI_LANG_CODE.equals(areaLanguage) || HK_DJI_LANG_CODE.equals(areaLanguage) || MO_DJI_LANG_CODE.equals(areaLanguage)) {
                return TW_DJI_LANG_CODE;
            }
            return CN_DJI_LANG_CODE;
        } else if (JA_DJI_LANG_CODE.equals(systemLanguage)) {
            return JP_DJI_LANG_CODE;
        } else {
            return systemLanguage;
        }
    }
}
