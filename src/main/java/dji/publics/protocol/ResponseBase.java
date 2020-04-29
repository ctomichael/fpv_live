package dji.publics.protocol;

import dji.component.accountcenter.IMemberProtocol;
import dji.fieldAnnotation.EXClassNullAway;

@EXClassNullAway
public class ResponseBase {
    public static final String STRING_ABOUT = "about";
    public static final String STRING_ABOUTME = "about_me";
    public static final String STRING_ACCOUNT = "account";
    public static final String STRING_ACCOUNTID = "account_id";
    public static final String STRING_ACCOUNT_TYPE = "account_type";
    public static final String STRING_ACTIVEDAY = "activeday";
    public static final String STRING_ACTIVEINFO = "active_info";
    public static final String STRING_APPVERSION = "appVersion";
    public static final String STRING_AVATAR = "avatar";
    public static final String STRING_BIO = "bio";
    public static final String STRING_CITY = "city";
    public static final String STRING_COMPANY = "company";
    public static final String STRING_COMPANY_DESC = "company_desc";
    public static final String STRING_CONTENT = "content";
    public static final String STRING_COUNTRY = "country";
    public static final String STRING_COUNTRYCODE = "country_code";
    public static final String STRING_CREATEDAT = "created_at";
    public static final String STRING_CURPAGE = "current_page";
    public static final String STRING_DATE = "date";
    public static final String STRING_DDS_AVAILABLE = "dds_available";
    public static final String STRING_DDS_AVAILABLE_SOON = "dds_available_soon";
    public static final String STRING_DDS_CURRENCY = "currency";
    public static final String STRING_DESCRIPTION = "description";
    public static final String STRING_DEVICENAME = "deviceName";
    public static final String STRING_DEVICETYPE = "deviceType";
    public static final String STRING_DISTANCE = "distance";
    public static final String STRING_DURATION = "duration";
    public static final String STRING_EMAIL = "email";
    public static final String STRING_EMBEDURL = "embed_url";
    public static final String STRING_EMPTY = "";
    public static final String STRING_EVENTID = "event_id";
    public static final String STRING_FACEBOOK_URL = "facebook_url";
    public static final String STRING_FAVORITES_COUNT = "favorites_count";
    public static final String STRING_FEMALE = "female";
    public static final String STRING_FILENAME = "filename";
    public static final String STRING_FILESIZE = "filesize";
    public static final String STRING_FILETYPE = "filetype";
    public static final String STRING_FIRMWARE_VERSION = "firmwareVersion";
    public static final String STRING_FIRSTNAME = "first_name";
    public static final String STRING_GEAR = "gear";
    public static final String STRING_GENDER = "gender";
    public static final String STRING_GOOGLEPLUS_URL = "googleplus_url";
    public static final String STRING_HTHUMB = "thumb_h";
    public static final String STRING_ID = "id";
    public static final String STRING_IP = "ip";
    public static final String STRING_ISJOIN = "is_join";
    public static final String STRING_ITEMS = "items";
    public static final String STRING_LARGETHUMB_URL = "thumbnail_large_url";
    public static final String STRING_LASTLOGIN = "last_login";
    public static final String STRING_LASTNAME = "last_name";
    public static final String STRING_LAT = "lat";
    public static final String STRING_LIKE_COUNT = "likes_count";
    public static final String STRING_LNG = "lng";
    public static final String STRING_LOCATION = "location";
    public static final String STRING_LTHUMB = "thumb_l";
    public static final String STRING_MAKE = "make";
    public static final String STRING_MALE = "male";
    public static final String STRING_MD5 = "md5";
    public static final String STRING_MEDIUMTHUMB_URL = "thumbnail_medium_url";
    public static final String STRING_MEMBERID = "member_id";
    public static final String STRING_MESSAGE = "message";
    public static final String STRING_MODEL = "model";
    public static final String STRING_MSG = "msg";
    public static final String STRING_NAME = "name";
    public static final String STRING_NICKNAME = "nick_name";
    public static final String STRING_NORMAL = "normal";
    public static final String STRING_NOTSPECIFIC = "not_specific";
    public static final String STRING_OPENAPI_TOKENID = "openapi_token_id";
    public static final String STRING_ORIGINAL = "original";
    public static final String STRING_PAGESIZE = "page_size";
    public static final String STRING_PASSWORD = "password";
    public static final String STRING_PHONE_NUMBER = "register_phone";
    public static final String STRING_PHOTONUM = "photo_num";
    public static final String STRING_PHOTOS_COUNT = "photos_count";
    public static final String STRING_PINTEREST_URL = "pinterest_url";
    public static final String STRING_PORTRAIT_URL = "portrait_url";
    public static final String STRING_PRODUCTTYPE = "productType";
    public static final String STRING_PROVIDER = "provider";
    public static final String STRING_PROVINCE = "province";
    public static final String STRING_PUBLIC = "public";
    public static final String STRING_REFURL = "reference_url";
    public static final String STRING_REPLYNUMBER = "reply_number";
    public static final String STRING_REPLYTIME = "reply_time";
    public static final String STRING_RUNLEVEL = "run_level";
    public static final String STRING_RUNSTATUS = "run_status";
    public static final String STRING_SHOWNAME = "show_name";
    public static final String STRING_SMALLTHUMB_URL = "thumbnail_small_url";
    public static final String STRING_SN = "sn";
    public static final String STRING_STATE = "state";
    public static final String STRING_STATUS = "status";
    public static final String STRING_STATUSMSG = "status_msg";
    public static final String STRING_STHUMB = "thumb_s";
    public static final String STRING_TIME = "time";
    public static final String STRING_TITLE = "title";
    public static final String STRING_TOKEN = "token";
    public static final String STRING_TOTALCOUNT = "total_count";
    public static final String STRING_TOTALPAGE = "total_page";
    public static final String STRING_TUMBLR_URL = "tumblr_url";
    public static final String STRING_TWITTER_URL = "twitter_url";
    public static final String STRING_UID = "uid";
    public static final String STRING_UPDATEDAT = "updated_at";
    public static final String STRING_URL = "url";
    public static final String STRING_USERNAME = "user_name";
    public static final String STRING_VERIFYAT = "verify_at";
    public static final String STRING_VERIFYSTATUS = "verify_status";
    public static final String STRING_VERIFYWAY = "verify_way";
    public static final String STRING_VIDEONUM = "video_num";
    public static final String STRING_VIDEOS_COUNT = "videos_count";
    public static final String STRING_VIP_LEVEL = "vip_level";
    public static final String STRING_WEBPAGE = "webpage";
    public Object mResultObj = null;
    public int mStatus = 1;
    public String mStatusMsg = "";

    public boolean equals(Object o) {
        return super.equals(o);
    }

    public int hashCode() {
        return super.hashCode();
    }

    public String toString() {
        StringBuilder sb = new StringBuilder(24);
        sb.append("status[").append(this.mStatus).append(IMemberProtocol.STRING_SEPERATOR_RIGHT);
        sb.append("statusMsg").append(this.mStatusMsg).append(IMemberProtocol.STRING_SEPERATOR_RIGHT);
        return sb.toString();
    }
}
