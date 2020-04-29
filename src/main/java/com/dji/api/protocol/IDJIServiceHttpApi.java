package com.dji.api.protocol;

import com.dji.api.base.IHttpApi;
import com.dji.fieldAnnotation.EXClassNullAway;

@EXClassNullAway
public interface IDJIServiceHttpApi extends IHttpApi {
    public static final String API_ABFUNCS_QUERY = "api/v1/ab_funcs/query";
    public static final String API_ABFUNC_CONFIRMATION = "api/v1/ab_funcs/confirmation";
    public static final String API_COUNTRY_CODE_REVERSE_ONLINE = "api/v1/geocoder/reverse_online";
    public static final String API_DJI_CARE_GEN_TASK = "api/v1/djicare/gen_task";
    public static final String API_DJI_CARE_TASK_RESULT = "api/v1/djicare/result";
    public static final String API_DJI_DEVICE_CHECK = "api/v1/sn/status";
    public static final String API_GET_CAAC_UAS = "ds-path/api/v3/real_name_for_cn/uas";
    public static final String API_GET_CAAC_UAS_DEBUG = "api/v3/real_name_for_cn/uas";
    public static final String DOMAIN = "https://apigateway.djiservice.org";
    public static final String DOMAIN_FOR_AUTH = "djiservice.org";
    public static final String KEY_FOR_AUTH = "5C4AtJjmSif5LePjWHpAhQeMFMbhld8F";

    public interface IDJIGOApi {
        public static final String API_ACADEMY_BANNER = "academy/banner/%1$s";
        public static final String API_ACADEMY_BOOK = "academy/book/%1$s/%2$s";
        public static final String API_ACADEMY_FAQ = "academy/faq/%1$s/%2$d/%3$s";
        public static final String API_ACADEMY_FAQ_SEARCH = "academy/faq/search";
        public static final String API_ACADEMY_SEARCH = "academy/search";
        public static final String API_ACADEMY_VIDEO = "academy/video/%1$s/%2$s/%3$s";
        public static final String API_ACADEMY_VIDEO_SEARCH = "academy/video/search";
        public static final String API_FEED_BACK = "feedback?platform=android&language=%1$s&content=%2$s&contact=%3$s";
        public static final String API_LEARN_MORE = "cms_config/product_learnmore_%1$s_%2$s";
        public static final String DOMAIN = "https://djigoapi.djiservice.org";
    }

    public interface IDeviceStore {
        public static final String API_DEVICE = "api/components/djigo-device-%1$s.json";
        public static final String API_DEVICE_ALL = "api/components/djigo-device.json";
        public static final String DOMAIN = "https://s.detcms.com";
    }

    public interface IGeo {
        public static final String API_GEO_GPS = "/geo/gps";
        public static final String API_GEO_GPS_WITH_IP = "/geo/gpsWithIP";
        public static final String API_GEO_IP = "/geo/ip";
        public static final String DOMAIN = "https://dict.djiservice.org";
        public static final String GEO_APP_ID = "123814";
        public static final String GEO_APP_KEY = "XynyNa6I1tUZAEi0xRvd49J";
    }

    public interface IPush {
        public static final String API_AUTHENTICATE = "api/authenticate";
        public static final String BR_SERVER_APP_ID = "dji";
        public static final String BR_SERVER_APP_KEY = "K2QD7GcVXlrOFTeT";
        public static final String DOMAIN = "https://push.djiservice.org";
        public static final String PUSH_APP_ID = "123814";
        public static final String PUSH_APP_KEY = "XynyNa6I1tUZAEi0xRvd49J";
    }

    public interface IFAQ {
        public static final String API_FREQUENTLY_FAQ = "faq/%1$s/%2$s";
        public static final String DOMAIN = "https://djigofaq.djiservice.org";
    }
}
