package dji.component.privacy;

public interface IPrivacyConfigUIComponent {
    public static final String ACTION_GET_GDPR_PP_URL = "action_get_gdpr_pp_url";
    public static final String ACTION_GET_PP_URL = "action_get_pp_url";
    public static final String ACTION_GET_TERMS_URL = "action_get_terms_url";
    public static final String ACTIVITY_PRIVACY_AUTHORIZE_REQUEST = "ActivityAuthorize";
    public static final String ACTIVITY_PRIVACY_CONFIG_REQUEST = "ActivityPrivacyConfig";
    public static final String ACTIVITY_USER_TERMS_REQUEST = "ActivityUserTerms";
    public static final String COMPONENT_NAME = "IPrivacyConfigUIComponent";
    public static final String DIALOG_LISTENER = "dialogPrivacyConfigListener";
    public static final String DIALOG_SHOW_GPS_REQUEST = "dialogPrivacyConfigShowGPSRequest";
    public static final String DIALOG_SHOW_SN_REQUEST = "dialogPrivacyConfigShowSnRequest";
    public static final String DIALOG_STRING = "dialogPrivacyConfigString";
    public static final String PARAM_PP_URL = "param_pp_url";
    public static final String PARAM_TERMS_URL = "param_terms_url";
}
