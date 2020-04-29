package dji.pilot.publics.util;

import java.util.Locale;

public class DJIClauseUtils {
    public static String PRIVATE_POLICY_HTML_CN = "htmls/DJI_Fly_App_Privacy_Policy_cn.html";
    public static String PRIVATE_POLICY_HTML_DE = "htmls/DJI_Go_App_Privacy_Policy_de.html";
    public static String PRIVATE_POLICY_HTML_EN = "htmls/DJI_Fly_App_Privacy_Policy_en.html";
    public static String PRIVATE_POLICY_HTML_EN_GDPR = "htmls/DJI_Fly_Privacy_Notice_en.html";
    public static String PRIVATE_POLICY_HTML_ES = "htmls/DJI_Fly_App_Privacy_Policy_es.html";
    public static String PRIVATE_POLICY_HTML_FR = "htmls/DJI_Fly_App_Privacy_Policy_fr.html";
    public static String PRIVATE_POLICY_HTML_JP = "htmls/DJI_Fly_App_Privacy_Policy_jp.html";
    public static String PRIVATE_POLICY_URL_WITHOUT_LOCATION_DE = "https://content.djiservice.org/agreement/DJI_Go_App_Privacy_Policy_de.html";
    public static String PRIVATE_POLICY_URL_WITHOUT_LOCATION_EN = "https://content.djiservice.org/agreement/DJI_Fly_App_Privacy_Policy_en.html";
    public static String PRIVATE_POLICY_URL_WITHOUT_LOCATION_ES = "https://content.djiservice.org/agreement/DJI_Fly_App_Privacy_Policy_es.html";
    public static String PRIVATE_POLICY_URL_WITHOUT_LOCATION_FR = "https://content.djiservice.org/agreement/DJI_Fly_App_Privacy_Policy_fr.html";
    public static String PRIVATE_POLICY_URL_WITHOUT_LOCATION_JP = "https://content.djiservice.org/agreement/DJI_Fly_App_Privacy_Policy_jp.html";
    public static String PRIVATE_POLICY_WITHOUT_LOCATION_CN = "https://content.djiservice.org/agreement/DJI_Fly_App_Privacy_Policy_cn.html";
    public static String TERMS_OF_USE_URL_WITHOUT_LOCATION_CN = "https://content.djiservice.org/agreement/DJI_Go_App_Terms_of_Use_cn.html";
    public static String TERMS_OF_USE_URL_WITHOUT_LOCATION_EN = "https://content.djiservice.org/agreement/DJI_Go_App_Terms_of_Use_en.html";
    public static String debugBBSUrl = null;
    public static String debugStoreUrl = null;

    public static String getPrivacyPolicyUrl() {
        String language = Locale.getDefault().getLanguage();
        if (language.equalsIgnoreCase("ZH")) {
            return PRIVATE_POLICY_WITHOUT_LOCATION_CN;
        }
        if (language.equalsIgnoreCase("JA")) {
            return PRIVATE_POLICY_URL_WITHOUT_LOCATION_JP;
        }
        if (language.equalsIgnoreCase("DE")) {
            return PRIVATE_POLICY_URL_WITHOUT_LOCATION_DE;
        }
        if (language.equalsIgnoreCase("FR")) {
            return PRIVATE_POLICY_URL_WITHOUT_LOCATION_FR;
        }
        if (language.equalsIgnoreCase("ES")) {
            return PRIVATE_POLICY_URL_WITHOUT_LOCATION_ES;
        }
        return PRIVATE_POLICY_URL_WITHOUT_LOCATION_EN;
    }

    public static String getPrivacyPolicyHtml(boolean isForGdpr) {
        String language = Locale.getDefault().getLanguage();
        if (language.equalsIgnoreCase("ZH")) {
            return PRIVATE_POLICY_HTML_CN;
        }
        if (language.equalsIgnoreCase("JA")) {
            return PRIVATE_POLICY_HTML_JP;
        }
        if (language.equalsIgnoreCase("DE")) {
            return PRIVATE_POLICY_HTML_DE;
        }
        if (language.equalsIgnoreCase("FR")) {
            return PRIVATE_POLICY_HTML_FR;
        }
        if (language.equalsIgnoreCase("ES")) {
            return PRIVATE_POLICY_HTML_ES;
        }
        if (isForGdpr) {
            return PRIVATE_POLICY_HTML_EN_GDPR;
        }
        return PRIVATE_POLICY_HTML_EN;
    }

    public static String getTermsOfUseUrl() {
        if (Locale.getDefault().getLanguage().equalsIgnoreCase("ZH")) {
            return TERMS_OF_USE_URL_WITHOUT_LOCATION_CN;
        }
        return TERMS_OF_USE_URL_WITHOUT_LOCATION_EN;
    }

    public static String getDebugStoreUrl() {
        return debugStoreUrl;
    }

    public static String getDebugBBSUrl() {
        return debugBBSUrl;
    }
}
