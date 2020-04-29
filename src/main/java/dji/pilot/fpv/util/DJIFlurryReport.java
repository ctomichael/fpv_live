package dji.pilot.fpv.util;

import dji.fieldAnnotation.EXClassNullAway;

@EXClassNullAway
public interface DJIFlurryReport {

    public interface WhatsNew {
        public static final String V2_MAIN_BANNER_OPEN = "Goapp_main_banner_open";
        public static final String V2_MAIN_BANNER_SKIP = "Goapp_main_banner_skip";
    }

    public interface DJISign {
        public static final String V2_LOGIN_FAIL = "Goapp_login_fail";
        public static final String V2_LOGIN_INTERFACE = "Goapp_login_interface";
        public static final String V2_LOGIN_SUCCESS = "Goapp_login_success";
        public static final String V2_MAIN_BANNER = "Goapp_main_banner";
        public static final String V2_REGISTER_SUCCESS = "Goapp_register_success";
    }

    public interface Device extends DJIFlurryReportPublic {
        public static final String V2_ACTIVATE_SUCCESS = "Goapp_activate_success";
        public static final String V2_BANNER_LEARN_MORE = "Goapp_banner_learn_more";
        public static final String V2_DISCOVERY = "Goapp_discovery";
        public static final String V2_ENTER_ACADEMY_INTERFACE = "Goapp_enter_academy_interface";
        public static final String V2_ENTER_FLIGHT_RECORD_INTERFACE = "Goapp_enter_flight_record_interface";
        public static final String V2_ENTER_HOW_TO_CONNECT = " v2_enter_rhow_to_connect";
        public static final String V2_EQUIPMENT = "Goapp_equipment";
        public static final String V2_LIBRARY = "Goapp_library";
        public static final String V2_ME = "Goapp_me";
    }

    public interface Explore {
        public static final String V2_PHOTO_SHARE_EXPLORE = "Goapp_photo_share_explore";
        public static final String V2_VIDEO_SHARE_EXPLORE = "Goapp_video_share_explore";
    }

    public interface LibraryVideo extends DJIFlurryReportPublic {
        public static final String V2_ADD_FOOTAGE_CREATE_INTERFACE = "Goapp_add_footage_create_interface";
        public static final String V2_ARTWORK_DURATION = "Goapp_artwork_duration";
        public static final String V2_DELETE_FOOTAGE_CREATE_INTERFACE = "Goapp_delete_footage_create_interface";
        public static final String V2_LOCAL_MUSIC_IMPORT = "Goapp_local_music_import";
        public static final String V2_MOMENT_DURATION = "Goapp_moment_duration";
        public static final String V2_MOMENT_NUMBER = "Goapp_moment_number";
        public static final String V2_MORE_MUSIC_BUTTON = "Goapp_more_music_button";
        public static final String V2_MOVE_FOOTAGE_CREATE_INTERFACE = "Goapp_move_footage_create_interface";
        public static final String V2_MOVIE_TEMPLATE_USAGE = "Goapp_movie_template_usage";
        public static final String V2_MOVIE_TEMPLATE_USAGE_SUBKEY_NAME = "template_name";
        public static final String V2_MULTI_MOMENT_EDIT = "Goapp_multi_moment_edit";
        public static final String V2_MULTI_TEMPLATE_USAGE = "Goapp_multi_template_usage";
        public static final String V2_MUSIC_FAVORITE = "Goapp_music_favorite";
        public static final String V2_ONLINE_MUSIC_DOWNLOAD = "Goapp_online_music_download";
        public static final String V2_SAVE_FOOTAGE_LOCAL = "Goapp_save_footage_local";
        public static final String V2_SAVE_VIDEO_ARTWORK = "Goapp_save_video_artwork";
        public static final String V2_SEGMENT_MOMENT_EDIT = "Goapp_segment_moment_edit";
        public static final String V2_SELECT_FOOTAGE_COUNT = "Goapp_select_footage_count";
        public static final String V2_SINGLE_TEMPLATE_USAGE = "Goapp_single_template_usage";
        public static final String V2_UPLOAD_VIDEO = "Goapp_upload_video";
        public static final String V2_VIDEO_BRIGHTNESS = "Goapp_video_brightness";
        public static final String V2_VIDEO_CONTRAST = "Goapp_video_contrast";
        public static final String V2_VIDEO_EDIT_MUTE_AUDIO = "Goapp_video_edit_mute_audio";
        public static final String V2_VIDEO_FILTER = "Goapp_video_filter";
        public static final String V2_VIDEO_SATURATION = "Goapp_video_saturation";
        public static final String V2_VIDEO_SPEED = "Goapp_video_speed";
        public static final String V2_VIDEO_UPLOAD_AUTO_FAIL = "Goapp_video_upload_auto_fail";
        public static final String V2_VIDEO_UPLOAD_AUTO_SUCCESS = "Goapp_video_upload_auto_success";
        public static final String V2_VIDEO_UPLOAD_FAIL = "Goapp_video_upload_fail";
        public static final String V2_VIDEO_UPLOAD_LATER = "Goapp_video_upload_later";
        public static final String V2_VIDEO_UPLOAD_SUCCESS = "Goapp_video_upload_success";
        public static final String V2_VIDEO_UPLOAD_YOUKU_YOUTUBE = "Goapp_video_upload_youku_youtube";
        public static final String V2_VIDEO_WATERMARK_AUTHER_FAIL = "Goapp_video_watermark_auther_fail";
        public static final String V2_VIDEO_WATERMARK_AUTHER_SUCCESS = "Goapp_video_watermark_auther_success";
        public static final String V2_VIDEO_WATERMARK_DATE_FAIL = "Goapp_video_watermark_date_fail";
        public static final String V2_VIDEO_WATERMARK_DATE_SUCCESS = "Goapp_video_watermark_date_success";
        public static final String V2_VIDEO_WATERMARK_LOCATION_FAIL = "Goapp_video_watermark_location_fail";
        public static final String V2_VIDEO_WATERMARK_LOCATION_SUCCESS = "Goapp_video_watermark_location_success";
    }

    public interface Mine {
        public static final String V2_ARTWORK_PREVIEW = "Goapp_artwork_preview";
        public static final String V2_CHECK_UPDATE = "Goapp_check_update";
        public static final String V2_CLEAR_PHOTO_CACHE = "Goapp_clear_photo_cache";
        public static final String V2_CLEAR_VIDEO_CACHE = "Goapp_clear_video_cache";
        public static final String V2_DJI_CIRCLE = "Goapp_dji_circle";
        public static final String V2_DJI_DDS = "Goapp_dji_dds";
        public static final String V2_DJI_FLY_RECORD = "Goapp_dji_fly_record";
        public static final String V2_DJI_FORUM = "Goapp_dji_forum";
        public static final String V2_DJI_MORE = "Goapp_dji_more";
        public static final String V2_DJI_SERVICE = "Goapp_dji_service";
        public static final String V2_DJI_STORE = "Goapp_dji_store";
        public static final String V2_DJI_SUPPORT = "Goapp_dji_support";
        public static final String V2_FOLLOWERS_FOLLOW_CLICK = "Goapp_Followers_follow_click";
        public static final String V2_FOLLOWERS_TAB = "Goapp_Followers_tab";
        public static final String V2_FOLLOWERS_USER_CLICK = "Goapp_Followers_user_click";
        public static final String V2_FOLLOWER_USER_CLICK = "Goapp_Follower_user_click";
        public static final String V2_FOLLOW_TAB = "Goapp_Follow_tab";
        public static final String V2_MEDAL_DETAIL = "Goapp_medal_detail";
        public static final String V2_ME_CONTACT_DJI = "Goapp_me_rcontact_dji";
        public static final String V2_ME_CONTACT_DJI_CALL = " v2_me_rcontact_dji_call";
        public static final String V2_ME_CONTACT_DJI_LIVECHAT = "Goapp_me_rcontact_dji_livechat";
        public static final String V2_ME_CONTACT_DJI_MAIL = "Goapp_me_rcontact_dji_mail";
        public static final String V2_ME_ENTER_ACADEMY_INTERFACE = "Goapp_me_enter_academy_interface";
        public static final String V2_MY_MEDALS = "Goapp_my_medals";
        public static final String V2_PHOTO_SHARE = "Goapp_photo_share";
        public static final String V2_PROFILE = "Goapp_profile";
        public static final String V2_PROFILE_ARTWORK_PLAY = "Goapp_profile_artwork_play";
        public static final String V2_PROFILE_EDIT = "Goapp_profile_edit";
        public static final String V2_PROFILE_EDIT_PHOTO = "Goapp_profile_edit_Photo";
        public static final String V2_SETTINGS = "Goapp_settings";
        public static final String V2_UNFOLLOW_CLICK = "Goapp_unfollow_click";
        public static final String V2_VIDEO_SHARE = "Goapp_video_share";
        public static final String VE_ME_RCONTACT_CHINA_EMAIL = "Goapp_me_rcontact_china_email";
        public static final String VE_ME_RCONTACT_EUROPEAN_EMAIL = "Goapp_me_rcontact_european_email";
        public static final String VE_ME_RCONTACT_JAPAN_EMAIL = "Goapp_me_rcontact_japan_email";
        public static final String VE_ME_RCONTACT_NORTH_MERICAN_EMAIL = "Goapp_me_rcontact_north_merican_email";
    }

    public interface ProductionShare {
        public static final String V2_PHOTO_SHARE_FACEBOOK = "Goapp_photo_share_facebook";
        public static final String V2_PHOTO_SHARE_MOMENTS = "Goapp_photo_share_moments";
        public static final String V2_PHOTO_SHARE_QQ = "Goapp_photo_share_qq";
        public static final String V2_PHOTO_SHARE_TUMBLR = "Goapp_photo_share_tumblr";
        public static final String V2_PHOTO_SHARE_TWITTER = "Goapp_photo_share_twitter";
        public static final String V2_PHOTO_SHARE_URL = "Goapp_photo_share_url";
        public static final String V2_PHOTO_SHARE_WECHAT = "Goapp_photo_share_wechat";
        public static final String V2_PHOTO_SHARE_WEIBO = "Goapp_photo_share_weibo";
        public static final String V2_PHOTO_SHARE_WHATSAPP = "Goapp_photo_share_whatsapp";
        public static final String V2_VIDEO_SHARE_FACEBOOK = "Goapp_video_share_facebook";
        public static final String V2_VIDEO_SHARE_MOMENTS = "Goapp_video_share_moments";
        public static final String V2_VIDEO_SHARE_QQ = "Goapp_video_share_qq";
        public static final String V2_VIDEO_SHARE_TUMBLR = "Goapp_video_share_tumblr";
        public static final String V2_VIDEO_SHARE_TWITTER = "Goapp_video_share_twitter";
        public static final String V2_VIDEO_SHARE_URL = "Goapp_video_share_url";
        public static final String V2_VIDEO_SHARE_WECHAT = "Goapp_video_share_wechat";
        public static final String V2_VIDEO_SHARE_WEIBO = "Goapp_video_share_weibo";
        public static final String V2_VIDEO_SHARE_WHATSAPP = "Goapp_video_share_whatsapp";
    }

    public interface Academy {
        public static final String V2_FLIGHT_BOOK_CLICK = "Goapp_flight_book_click";
        public static final String V2_INSTRUCTIONS_CLICK = "Goapp_instructions_click";
        public static final String V2_VIDEO_COLLEGE_CLICK = "Goapp_video_college_click";
    }

    public interface Coupon {
        public static final String V2_ME_GIFTCARD = "Goapp_me_giftcard";
        public static final String VE_ME_GIFTCARD_COPYLINK = "Goapp_me_giftcard_copylink";
        public static final String VE_ME_GIFTCARD_DRONE = "Goapp_me_giftcard_drone";
        public static final String VE_ME_GIFTCARD_EMAIL = "Goapp_me_giftcard_email";
        public static final String VE_ME_GIFTCARD_FACEBOOK = "Goapp_me_giftcard_facebook";
        public static final String VE_ME_GIFTCARD_MESSAGE = "Goapp_me_giftcard_message";
        public static final String VE_ME_GIFTCARD_MESSAGE_CLEAR = "Goapp_me_giftcard_message_clear";
        public static final String VE_ME_GIFTCARD_PARTS = "Goapp_me_giftcard_parts";
        public static final String VE_ME_GIFTCARD_PHONE_MESSAGE = "Goapp_me_giftcard_phone_message";
        public static final String VE_ME_GIFTCARD_QQ = "Goapp_me_giftcard_qq";
        public static final String VE_ME_GIFTCARD_REFRESH = "Goapp_me_giftcard_refresh";
        public static final String VE_ME_GIFTCARD_SINA = "Goapp_me_giftcard_sina";
        public static final String VE_ME_GIFTCARD_TO_SHARE = "Goapp_me_giftcard_to_share";
        public static final String VE_ME_GIFTCARD_TUMBLR = "Goapp_me_giftcard_tumblr";
        public static final String VE_ME_GIFTCARD_TWITTER = "Goapp_me_giftcard_twitter";
        public static final String VE_ME_GIFTCARD_USE = "Goapp_me_giftcard_use";
        public static final String VE_ME_GIFTCARD_WECHAT = "Goapp_me_giftcard_wechat";
        public static final String VE_ME_GIFTCARD_WECHAT_MOMENT = "Goapp_me_giftcard_wechat_moment";
        public static final String VE_ME_GIFTCARD_WHATSAPP = "Goapp_me_giftcard_whatsapp";
    }

    public interface FPV extends DJIFlurryReportPublic {
        public static final String V2_DITANCE_COMBOKEY = "Email&ProductType&Distance";
        public static final String V2_DITANCE_KEY = "Distance";
        public static final String V2_DURATION_COMBOKEY = "Email&ProductType&FlightTime";
        public static final String V2_EMAIL_KEY = "Email";
        public static final String V2_FIRMWARE_UPDATEFAILE = "Goapp_Firmware_updatefaile";
        public static final String V2_FIRMWARE_UPDATESUCCESS = "Goapp_Firmware_updatesuccess";
        public static final String V2_FLIGHT_COMPLETE = "Goapp_rflight_rcomplete";
        public static final String V2_FLIGHT_COMPLETE_KEY = "Email&ProductType";
        public static final String V2_FLIGHT_DISTANCE = "Goapp_flight_distance";
        public static final String V2_FLIGHT_DURATION = "Goapp_flight_duration";
        public static final String V2_FLIGHT_HEIGHT = "Goapp_flight_height";
        public static final String V2_FLIGHT_TIME_KEY = "FlightTime";
        public static final String V2_FPV_RECORD_VIDEO = "Goapp_fpv_record_video";
        public static final String V2_FPV_TAKE_PHOTO = "Goapp_fpv_take_photo";
        public static final String V2_FPV_YOUTUBELIVESTREAM = "Goapp_fpv_YouTubeLiveStream";
        public static final String V2_MAXHEIGHT_COMBOKEY = "Email&ProductType&MaxHeight";
        public static final String V2_MAXHEIGHT_KEY = "MaxHeight";
        public static final String V2_SSD_SATA_VERSION = "ssd_sata_version";
    }

    public interface FlightRecord {
        public static final String V2_EVENT_FLIGHTDATA_SYNCHRONOUS = "Goapp_event_flightdata_synchronous";
        public static final String V2_EVENT_FLIGHTDATA_SYNCHRONOUS_SUBKEY = "success";
        public static final String V2_FLIGHTDATA = "Goapp_flightdata";
        public static final String V2_FLIGHTDATA_DETIAL = "Goapp_flightdata_detial";
        public static final String V2_FLIGHTDATA_SHARE = "Goapp_flightdata_share";
        public static final String V2_FLIGHTDATA_SYNCHRONOUS = "Goapp_rflightdata_synchronous";
        public static final String V2_FLIGHTDATA_SYNCHRONOUS_DOWNLOAD_FAIL = "download_fail";
        public static final String V2_FLIGHTDATA_SYNCHRONOUS_DOWNLOAD_SUCCESS = "download_success";
        public static final String V2_FLIGHTDATA_SYNCHRONOUS_UPLOAD_FAIL = "upload_fail";
        public static final String V2_FLIGHTDATA_SYNCHRONOUS_UPLOAD_SUCCESS = "upload_success";
        public static final String V2_FLIGHT_RCOMPLETE = "Goapp_flight_rcomplete";
        public static final String V2_MC_MODE_ATTI = "Goapp_mc_mode_atti";
    }

    public interface GroundStation {
        public static final String V2_GS_ENABLE_KEY = "enable";
        public static final String V2_GS_NO_VAL = "no";
        public static final String V2_GS_YES_VAL = "yes";
        public static final String V2_NAV_CL_FUNC = "Goapp_nav_cl_func";
        public static final String V2_NAV_FM_APPLYED = "Goapp_nav_fm_applyed";
        public static final String V2_NAV_FM_FUNC = "Goapp_nav_fm_func";
        public static final String V2_NAV_HL_FUNC = "Goapp_nav_hl_func";
        public static final String V2_NAV_POI_FUNC = "Goapp_nav_poi_func";
        public static final String V2_NAV_POI_PAUSE = "Goapp_nav_poi_pause";
        public static final String V2_NAV_WP_FUNC = "Goapp_nav_wp_func";
        public static final String V2_NAV_WP_PAUSE = "Goapp_nav_wp_pause";
        public static final String V2_NAV_WP_SAVE_WP = "Goapp_nav_wp_save_wp";
        public static final String V2_NAV_WP_USE_WP = "Goapp_nav_wp_use_wp";
    }

    public interface MultiMoment {
        public static final String V2_ADD_TRANSITIONS = "Goapp_add_transitions";
    }

    public interface NoFlyZone {
        public static final String NFZ_AUTHENTICATION_CLOSE = "nfz_authentication_close";
        public static final String NFZ_AUTHENTICATION_SUCCESS = "nfz_authentication_success";
        public static final String NFZ_IN_RED_ZONE = "nfz_in_red_zone";
        public static final String NFZ_IN_YELLOW_ZONE = "nfz_in_yellow_zone";
        public static final String NFZ_IN_YELLOW_ZONE_UNLOCK = "nfz_in_yellow_zone_unlock";
        public static final String NFZ_LOGIN = "nfz_login";
        public static final String NFZ_UNLOCK_FAILED = "nfz_unlock_failed";
        public static final String NFZ_UNLOCK_SUCCESS = "nfz_unlock_success";
    }

    public interface ShareLaguage_photo {
        public static final String V2_PHOTO_SHARE_FACEBOOK_EN = "Goapp_photo_share_facebook_en";
        public static final String V2_PHOTO_SHARE_FACEBOOK_HANT = "Goapp_photo_share_facebook_hant";
        public static final String V2_PHOTO_SHARE_FACEBOOK_OTHER = "Goapp_photo_share_facebook_other";
        public static final String V2_PHOTO_SHARE_FACEBOOK_ZH = "Goapp_photo_share_facebook_zh";
        public static final String V2_PHOTO_SHARE_INSTAGRAM_EN = "Goapp_photo_share_instagram_en";
        public static final String V2_PHOTO_SHARE_INSTAGRAM_HANT = "Goapp_photo_share_instagram_hant";
        public static final String V2_PHOTO_SHARE_INSTAGRAM_OTHER = "Goapp_photo_share_Instagram_other";
        public static final String V2_PHOTO_SHARE_INSTAGRAM_ZH = "Goapp_photo_share_instagram_zh";
        public static final String V2_PHOTO_SHARE_MOMENTS_EN = "Goapp_photo_share_moments_en";
        public static final String V2_PHOTO_SHARE_MOMENTS_HANT = "Goapp_photo_share_moments_hant";
        public static final String V2_PHOTO_SHARE_MOMENTS_OTHER = "Goapp_photo_share_moments_other";
        public static final String V2_PHOTO_SHARE_MOMENTS_ZH = "Goapp_photo_share_moments_zh";
        public static final String V2_PHOTO_SHARE_QQ_EN = "Goapp_photo_share_qq_en";
        public static final String V2_PHOTO_SHARE_QQ_HANT = "Goapp_photo_share_qq_hant";
        public static final String V2_PHOTO_SHARE_QQ_OTHER = "Goapp_photo_share_qq_other";
        public static final String V2_PHOTO_SHARE_QQ_ZH = "Goapp_photo_share_qq_zh";
        public static final String V2_PHOTO_SHARE_TWITTER_EN = "Goapp_photo_share_twitter_en";
        public static final String V2_PHOTO_SHARE_TWITTER_HANT = "Goapp_photo_share_twitter_hant";
        public static final String V2_PHOTO_SHARE_TWITTER_OTHER = "Goapp_photo_share_twitter_other";
        public static final String V2_PHOTO_SHARE_TWITTER_ZH = "Goapp_photo_share_twitter_zh";
        public static final String V2_PHOTO_SHARE_URL_EN = "Goapp_photo_share_url_en";
        public static final String V2_PHOTO_SHARE_URL_HANT = "Goapp_photo_share_url_hant";
        public static final String V2_PHOTO_SHARE_URL_OTHER = "Goapp_photo_share_url_other";
        public static final String V2_PHOTO_SHARE_URL_ZH = "Goapp_photo_share_url_zh";
        public static final String V2_PHOTO_SHARE_WECHAT_EN = "Goapp_photo_share_wechat_en";
        public static final String V2_PHOTO_SHARE_WECHAT_HANT = "Goapp_photo_share_wechat_hant";
        public static final String V2_PHOTO_SHARE_WECHAT_OTHER = "Goapp_photo_share_wechat_other";
        public static final String V2_PHOTO_SHARE_WECHAT_ZH = "Goapp_photo_share_wechat_zh";
        public static final String V2_PHOTO_SHARE_WEIBO_EN = "Goapp_photo_share_weibo_en";
        public static final String V2_PHOTO_SHARE_WEIBO_HANT = "Goapp_photo_share_weibo_hant";
        public static final String V2_PHOTO_SHARE_WEIBO_OTHER = "Goapp_photo_share_weibo_other";
        public static final String V2_PHOTO_SHARE_WEIBO_ZH = "Goapp_photo_share_weibo_zh";
        public static final String V2_PHOTO_SHARE_WHATSAPP_EN = "Goapp_photo_share_whatsapp_en";
        public static final String V2_PHOTO_SHARE_WHATSAPP_HANT = "Goapp_photo_share_whatsapp_hant";
        public static final String V2_PHOTO_SHARE_WHATSAPP_OTHER = "Goapp_photo_share_whatsapp_other";
        public static final String V2_PHOTO_SHARE_WHATSAPP_ZH = "Goapp_photo_share_whatsapp_zh";
    }

    public interface Vision extends DJIFlurryReportPublic {
        public static final String V2_SELFCAL_FAIL = "Goapp_vision_calibrate_fail";
        public static final String V2_SELFCAL_NEED_ASSISTANT = "Goapp_vision_calibrate_need_cali_assitant";
        public static final String V2_SELFCAL_SUCCESS = "Goapp_vision_calibrate_success";
        public static final String V2_SELFCAL_TIME = "Goapp_vision_calibrate_time";
        public static final String V2_SELFCAL_TIME_KEY = "calibrate_time";
    }

    public interface AbnormalCondition {
        public static final String V2_BAROMETER_ERROR = "Goapp_Barometer_error";
        public static final String V2_BATTERY_CONNECT_ERROR = "Goapp_battery_connect_error";
        public static final String V2_IMU_CALIBRATION = "Goapp_imu_Calibration";
        public static final String V2_MC_COMP_ERROR = "Goapp_mc_comp_error";
        public static final String V2_MC_COMP_INTERF = "Goapp_mc_comp_interf";
        public static final String V2_MC_ERROR = "Goapp_mc_error";
        public static final String V2_MC_IMU_ERROR = "Goapp_mc_imu_error";
        public static final String V2_MC_MODE_FS_GOHOME = "Goapp_mc_mode_fs_gohome";
        public static final String V2_MC_MODE_INTL_GOHOME = "Goapp_mc_mode_intl_gohome";
        public static final String V2_RC_SIGNALLOST = "Goapp_rc_signallost";
        public static final String V2_SEC_LOWBATTERY = "Goapp_sec_lowbattery";
        public static final String V2_VISUAL_ERROR = "Goapp_Visual_error";
    }

    public interface LibraryPhoto extends DJIFlurryReportPublic {
        public static final String V2_DOWNLOAD_ORIGINAL_PHOTO_NUMBER = "Goapp_download_original_photo_number";
        public static final String V2_DOWNLOAD_ORIGINAL_VIDEO_NUMBER = "Goapp_download_original_video_number";
        public static final String V2_EDIT_PHOTO_NUMBER = "Goapp_edit_photo_number";
        public static final String V2_KEY_HTTP_ERROR = "http_error";
        public static final String V2_KEY_SERVER_ERROR = "server_error";
        public static final String V2_LIBRARY_PHOTO_COUNT = "Goapp_library_photo_count";
        public static final String V2_PHOTO_BRIGHTNESS = "Goapp_photo_brightness";
        public static final String V2_PHOTO_CONTRAST = "Goapp_photo_contrast";
        public static final String V2_PHOTO_CROP_16_9 = "Goapp_photo_crop_16_9";
        public static final String V2_PHOTO_CROP_1_1 = "Goapp_photo_crop_1_1";
        public static final String V2_PHOTO_CROP_3_4 = "Goapp_photo_crop_3_4";
        public static final String V2_PHOTO_CROP_4_3 = "Goapp_photo_crop_4_3";
        public static final String V2_PHOTO_CROP_9_16 = "Goapp_photo_crop_9_16";
        public static final String V2_PHOTO_CROP_REVERT = "Goapp_photo_crop_revert";
        public static final String V2_PHOTO_FILTER = "Goapp_photo_filter";
        public static final String V2_PHOTO_ROTATE = "Goapp_photo_rotate";
        public static final String V2_PHOTO_SATURATION = "Goapp_photo_saturation";
        public static final String V2_PHOTO_UPLOAD_FAIL = "Goapp_photo_upload_fail";
        public static final String V2_PHOTO_UPLOAD_LATER = "Goapp_photo_upload_later";
        public static final String V2_PHOTO_UPLOAD_SUCCESS = "Goapp_photo_upload_success";
        public static final String V2_PHOTO_WATERMARK_AUTHER_FAIL = "Goapp_photo_watermark_auther_fail";
        public static final String V2_PHOTO_WATERMARK_AUTHER_SUCCESS = "Goapp_photo_watermark_auther_success";
        public static final String V2_PHOTO_WATERMARK_DATE_FAIL = "Goapp_photo_watermark_date_fail";
        public static final String V2_PHOTO_WATERMARK_DATE_SUCCESS = "Goapp_photo_watermark_date_success";
        public static final String V2_PHOTO_WATERMARK_LOCATION_FAIL = "Goapp_photo_watermark_location_fail";
        public static final String V2_PHOTO_WATERMARK_LOCATION_SUCCESS = "Goapp_photo_watermark_location_success";
        public static final String V2_SAVE_PHOTO_ARTWORK = "Goapp_save_photo_artwork";
        public static final String V2_SAVE_PHOTO_LOCAL = "Goapp_save_photo_local";
    }

    public interface LocalImport extends DJIFlurryReportPublic {
        public static final String V2_LIBRARY_CACHE_MANAGE_BUTTON = "Goapp_library_cache_manage_button";
        public static final String V2_LIBRARY_CACHE_MANAGE_DELETE = "Goapp_library_cache_manage_delete";
        public static final String V2_LOCAL_FILE_BUTTON = "Goapp_local_file_button";
        public static final String V2_LOCAL_VIDEO_CROP = "Goapp_local_video_crop";
        public static final String V2_LOCAL_VIDEO_CROP_BACK = "Goapp_local_video_crop_back";
        public static final String V2_LOCAL_VIDEO_CROP_SUCCESS = "Goapp_local_video_crop_success";
        public static final String V2_MOBILE_PHOTO_BUTTON = "Goapp_mobile_photo_button";
        public static final String V2_MOBILE_PHOTO_INPUT_BACK = "Goapp_mobile_photo_input_back";
        public static final String V2_MOBILE_PHOTO_INPUT_NUMBER = "Goapp_mobile_photo_input_number";
        public static final String V2_MOBILE_VIDEO_BUTTON = "Goapp_mobile_video_button";
        public static final String V2_MOBILE_VIDEO_INPUT_BACK = "Goapp_mobile_video_input_back";
        public static final String V2_MOBILE_VIDEO_INPUT_DURATION = "Goapp_mobile_video_input_duration";
        public static final String V2_MOBILE_VIDEO_INPUT_NUMBER = "Goapp_mobile_video_input_number";
        public static final String V2_SD_FILE_BUTTON = "Goapp_sd_file_button";
        public static final String V2_VIDEO_EDIT_MUTE_AUDIO = "Goapp_video_edit_mute_audio";
    }

    public interface NativeExplore extends DJIFlurryReportPublic {
        public static final String V2_2015_EVENT_SHARE = "Goapp_2015_event_share";
        public static final String V2_EXPLORE_BANNER = "Goapp_explore_banner";
        public static final String V2_EXPLORE_BANNER_SHARE = "Goapp_explore_banner_share";
        public static final String V2_EXPLORE_COMMENT = "Goapp_explore_comment";
        public static final String V2_EXPLORE_COMMENT_SEND = "Goapp_explore_comment_send";
        public static final String V2_EXPLORE_DETAIL = "Goapp_explore_detail";
        public static final String V2_EXPLORE_FEED_AD = "Goapp_explore_feed_ad";
        public static final String V2_EXPLORE_FEED_ARTWORK_PHOTO = "Goapp_explore_feed_artwork_photo";
        public static final String V2_EXPLORE_FEED_ARTWORK_VIDEO = "Goapp_explore_feed_artwork_video";
        public static final String V2_EXPLORE_FEED_FOLLOW = "Goapp_explore_feed_follow";
        public static final String V2_EXPLORE_FEED_UNFOLLOW = "Goapp_explore_feed_unfollow";
        public static final String V2_EXPLORE_FOLLOWING = "Goapp_explore_following";
        public static final String V2_EXPLORE_LATEST = "Goapp_explore_latest";
        public static final String V2_EXPLORE_LIKE = "Goapp_explore_like";
        public static final String V2_EXPLORE_NOTIFICATION = "Goapp_explore_notification";
        public static final String V2_EXPLORE_NOTIFICATION_MESSAGE = "Goapp_explore_notification_message";
        public static final String V2_EXPLORE_NOTIFICATION_MESSAGE_DETAIL = "Goapp_explore_notification_message_detail";
        public static final String V2_EXPLORE_NOTIFICATION_NOTICE = "Goapp_explore_notification_notice";
        public static final String V2_EXPLORE_NOTIFICATION_NOTICE_DETAIL = "Goapp_explore_notification_notice_detail";
        public static final String V2_EXPLORE_PROFILE_ARTWORK = "Goapp_explore_profile_artwork";
        public static final String V2_EXPLORE_PROFILE_BADGE = "Goapp_explore_profile_badge";
        public static final String V2_EXPLORE_PROFILE_FOLLOW = "Goapp_explore_profile_follow";
        public static final String V2_EXPLORE_PROFILE_UNFOLLOW = "Goapp_explore_profile_unfollow";
        public static final String V2_EXPLORE_PROTRAIT = "Goapp_explore_protrait";
        public static final String V2_EXPLORE_RECOMMENDED = "Goapp_explore_recommended";
        public static final String V2_EXPLORE_SHARE_ARTWORK = "Goapp_explore_share_artwork";
        public static final String V2_EXPLORE_SHARE_ARTWORK_FACEBOOK = "Goapp_explore_share_artwork_facebook";
        public static final String V2_EXPLORE_SHARE_ARTWORK_MOMENT = "Goapp_explore_share_artwork_moment";
        public static final String V2_EXPLORE_SHARE_ARTWORK_QQ = "Goapp_explore_share_artwork_qq";
        public static final String V2_EXPLORE_SHARE_ARTWORK_TUMBLR = "Goapp_explore_share_artwork_tumblr";
        public static final String V2_EXPLORE_SHARE_ARTWORK_TWITTER = "Goapp_explore_share_artwork_twitter";
        public static final String V2_EXPLORE_SHARE_ARTWORK_URL = "Goapp_explore_share_artwork_url";
        public static final String V2_EXPLORE_SHARE_ARTWORK_WECHAT = "Goapp_explore_share_artwork_wechat";
        public static final String V2_EXPLORE_SHARE_ARTWORK_WEIBO = "Goapp_explore_share_artwork_weibo";
        public static final String V2_EXPLORE_SHARE_ARTWORK_WHATSAPP = "Goapp_explore_share_artwork_whatsapp";
        public static final String V2_EXPLORE_SHARE_PHOTO = "Goapp_explore_share_photo";
        public static final String V2_EXPLORE_SHARE_VIDEO = "Goapp_explore_share_video";
        public static final String V2_EXPLORE_SMALLBANNER = "Goapp_explore_smallbanner";
        public static final String V2_EXPLORE_SMALLBANNER_SUBKEY_INDEX = "index";
        public static final String V2_EXPLORE_SMALLBANNER_SUBKEY_NAME = "name";
        public static final String V2_EXPLORE_TAG = "Goapp_explore_tag";
        public static final String V2_EXPLORE_UNLIKE = "Goapp_explore_unlike";
        public static final String VE_SHARE_COMPLETED_FINISH = "Goapp_share_completed_finsh";
        public static final String VE_SHARE_COMPLETED_JOIN_EVENT = "Goapp_share_completed_join_event";
        public static final String VE_SHARE_COMPLETED_SHARE_AGAIN = "Goapp_share_completed_share_again";
    }

    public interface Osmo {
        public static final String V2_OSMO_360PANO = "Goapp_OSMO_360Pano";
        public static final String V2_OSMO_GIMBALMODE = "Goapp_OSMO_GimbalMode";
        public static final String V2_OSMO_RECENTER = "Goapp_OSMO_recenter";
        public static final String V2_OSMO_SEFIEPANO = "Goapp_OSMO_SefiePano";
        public static final String V2_OSMO_TIMELAPSE = "Goapp_OSMO_Timelapse";
    }

    public interface RemoteControl extends DJIFlurryReportPublic {
        public static final String V2_DEIVCE_BATTERY_FRIST_SET = "Goapp_deivce_battery_frist_set";
        public static final String V2_DEIVCE_BATTERY_HISTORY = "Goapp_deivce_battery_history";
        public static final String V2_DEIVCE_BATTERY_SEC_SET = "Goapp_deivce_battery_sec_set";
        public static final String V2_DEIVCE_CAMERA_EXPOMODE = "Goapp_deivce_camera_expomode";
        public static final String V2_DEIVCE_CAMERA_EXPO_OPEN = "Goapp_deivce_camera_expo_open";
        public static final String V2_DEIVCE_MAP_HIDE = "Goapp_deivce_map_hide";
        public static final String V2_DEIVCE_MAP_OPEN = "Goapp_deivce_map_open";
        public static final String V2_DEVICE_BATTERY_TIME = "Goapp_device_battery_time";
        public static final String V2_DEVICE_C1_RC = "Goapp_device_c1_rc";
        public static final String V2_DEVICE_C2_RC = "Goapp_device_c2_rc";
        public static final String V2_DEVICE_CAMERASETTING = "Goapp_device_camerasetting";
        public static final String V2_DEVICE_CAMERASETTING_FLITER = "Goapp_device_camerasetting_fliter";
        public static final String V2_DEVICE_CAMERASETTING_VIDEOFORMAT = "Goapp_device_camerasetting_videoformat";
        public static final String V2_DEVICE_CAMERA_AELOCK = "Goapp_device_camera_aelock";
        public static final String V2_DEVICE_CAMERA_EXPOSURE = "Goapp_device_camera_exposure";
        public static final String V2_DEVICE_FULLSCREEN = "Goapp_device-fullscreen";
        public static final String V2_DEVICE_GIMBALMODE = "Goapp_device_gimbalmode";
        public static final String V2_DEVICE_GIMBALMODE_ADV = "Goapp_device_gimbalmode_adv";
        public static final String V2_DEVICE_GIMBALMODE_FOLLOW = "Goapp_device_gimbalmode_follow";
        public static final String V2_DEVICE_GIMBALMODE_FPV = "Goapp_device_gimbalmode_fpv";
        public static final String V2_DEVICE_GIMBALMODE_FREE = "Goapp_device_gimbalmode_free";
        public static final String V2_DEVICE_GIMBALMODE_RESET = "Goapp_device_gimbalmode_reset";
        public static final String V2_DEVICE_GIMBAL_DRAG = "Goapp_device_gimbal-drag";
        public static final String V2_DEVICE_GOHOME = "Goapp_device_gohome";
        public static final String V2_DEVICE_GOHOME_RC = "Goapp_device_gohome_rc";
        public static final String V2_DEVICE_JOYSTICK_CUSTOM = "Goapp_device_joystick_custom";
        public static final String V2_DEVICE_JOYSTICK_MODE1 = "Goapp_device_joystick_mode1";
        public static final String V2_DEVICE_JOYSTICK_MODE2 = "Goapp_device_joystick_mode2";
        public static final String V2_DEVICE_JOYSTICK_MODE3 = "Goapp_device_joystick_mode3";
        public static final String V2_DEVICE_LANDING = "Goapp_device_landing";
        public static final String V2_DEVICE_LEIDA = "Goapp_device_leida";
        public static final String V2_DEVICE_MC_AMODE_RC = "Goapp_device_mc_amode_rc";
        public static final String V2_DEVICE_MC_FMODE_RC = "Goapp_device_mc_fmode_rc";
        public static final String V2_DEVICE_MC_LIMT_DISTACE = "Goapp_device_mc_limt_distace";
        public static final String V2_DEVICE_MC_LIMT_HIGHT = "Goapp_device_mc_limt_hight";
        public static final String V2_DEVICE_MC_SMODE_RC = "Goapp_device_mc_smode_rc";
        public static final String V2_DEVICE_PALYBACK_RC = "Goapp_device_palyback_rc";
        public static final String V2_DEVICE_SEHOME_AIRCRAFT = "Goapp_device_sehome_aircraft";
        public static final String V2_DEVICE_SETHOME_PERSON = "Goapp_device_sethome_person";
        public static final String V2_DEVICE_TAKEOFF = "Goapp_device_takeoff";
        public static final String V2_DEVICE_TAKEOFF_RC = "Goapp_device_takeoff_rc";
        public static final String V2_DEVICE_TAKE_PHOTO_RC = "Goapp_device_take_photo_rc";
        public static final String V2_DEVICE_VIDEO_RECORD_RC = "Goapp_device_video_record_rc";
        public static final String V2_DJI_LANDING = "Goapp_dji_landing";
        public static final String V2_DJI_TAKEOFF = "Goapp_dji_takeoff";
    }

    public interface ShareLaguage_video {
        public static final String V2_VIDEO_SHARE_FACEBOOK_EN = "Goapp_video_share_facebook_en";
        public static final String V2_VIDEO_SHARE_FACEBOOK_HANT = "Goapp_video_share_facebook_hant";
        public static final String V2_VIDEO_SHARE_FACEBOOK_QITA = "Goapp_video_share_facebook_qita";
        public static final String V2_VIDEO_SHARE_FACEBOOK_ZH = "Goapp_video_share_facebook_zh";
        public static final String V2_VIDEO_SHARE_INSTAGRAM_EN = "Goapp_video_share_instagram_en";
        public static final String V2_VIDEO_SHARE_INSTAGRAM_HANT = "Goapp_video_share_instagram_hant";
        public static final String V2_VIDEO_SHARE_INSTAGRAM_QITA = "Goapp_video_share_Instagram_qita";
        public static final String V2_VIDEO_SHARE_INSTAGRAM_ZH = "Goapp_video_share_instagram_zh";
        public static final String V2_VIDEO_SHARE_MOMENTS_EN = "Goapp_video_share_moments_en";
        public static final String V2_VIDEO_SHARE_MOMENTS_HANT = "Goapp_video_share_moments_hant";
        public static final String V2_VIDEO_SHARE_MOMENTS_QITA = "Goapp_video_share_moments_qita";
        public static final String V2_VIDEO_SHARE_MOMENTS_ZH = "Goapp_video_share_moments_zh";
        public static final String V2_VIDEO_SHARE_QQ_EN = "Goapp_video_share_qq_en";
        public static final String V2_VIDEO_SHARE_QQ_HANT = "Goapp_video_share_qq_hant";
        public static final String V2_VIDEO_SHARE_QQ_QITA = "Goapp_video_share_qq_qita";
        public static final String V2_VIDEO_SHARE_QQ_ZH = "Goapp_video_share_qq_zh";
        public static final String V2_VIDEO_SHARE_TWITTER_EN = "Goapp_video_share_twitter_en";
        public static final String V2_VIDEO_SHARE_TWITTER_HANT = "Goapp_video_share_twitter_hant";
        public static final String V2_VIDEO_SHARE_TWITTER_QITA = "Goapp_video_share_twitter_qita";
        public static final String V2_VIDEO_SHARE_TWITTER_ZH = "Goapp_video_share_twitter_zh";
        public static final String V2_VIDEO_SHARE_URL_EN = "Goapp_video_share_url_en";
        public static final String V2_VIDEO_SHARE_URL_HANT = "Goapp_video_share_url_hant";
        public static final String V2_VIDEO_SHARE_URL_QITA = "Goapp_video_share_url_qita";
        public static final String V2_VIDEO_SHARE_URL_ZH = "Goapp_video_share_url_zh";
        public static final String V2_VIDEO_SHARE_WECHAT_EN = "Goapp_video_share_wechat_en";
        public static final String V2_VIDEO_SHARE_WECHAT_HANT = "Goapp_video_share_wechat_hant";
        public static final String V2_VIDEO_SHARE_WECHAT_QITA = "Goapp_video_share_wechat_qita";
        public static final String V2_VIDEO_SHARE_WECHAT_ZH = "Goapp_video_share_wechat_zh";
        public static final String V2_VIDEO_SHARE_WEIBO_EN = "Goapp_video_share_weibo_en";
        public static final String V2_VIDEO_SHARE_WEIBO_HANT = "Goapp_video_share_weibo_hant";
        public static final String V2_VIDEO_SHARE_WEIBO_QITA = "Goapp_video_share_weibo_qita";
        public static final String V2_VIDEO_SHARE_WEIBO_ZH = "Goapp_video_share_weibo_zh";
        public static final String V2_VIDEO_SHARE_WHATSAPP_EN = "Goapp_video_share_whatsapp_en";
        public static final String V2_VIDEO_SHARE_WHATSAPP_HANT = "Goapp_video_share_whatsapp_hant";
        public static final String V2_VIDEO_SHARE_WHATSAPP_QITA = "Goapp_video_share_whatsapp_qita";
        public static final String V2_VIDEO_SHARE_WHATSAPP_ZH = "Goapp_video_share_whatsapp_zh";
    }
}
