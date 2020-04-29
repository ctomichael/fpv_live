package com.dji.api.protocol;

import com.dji.api.base.IHttpApi;
import com.dji.fieldAnnotation.EXClassNullAway;

@EXClassNullAway
public interface ISkyPixelHttpApi extends IHttpApi {
    public static final String DOMAIN = "https://www.skypixel.com";

    public interface IExplore {
        public static final String API_ARTWORK_DETAIL = "api/mobile/pilot/";
        public static final String API_BANNER_ADS = "api/mobile/explore/banners_and_ads";
        public static final String API_COMMENT_FREE_EYE = "api/video_splited_collections/%1$s/comments";
        public static final String API_COMMENT_PHOTO = "api/photos/%1$s/comments";
        public static final String API_COMMENT_VIDEO = "api/videos/%1$s/comments";
        public static final String API_DELETE_FREE_EYE = "api/video_splited_collections/%1$s";
        public static final String API_DELETE_PHOTO = "api/photos/%1$s";
        public static final String API_DELETE_VIDEO = "api/videos/%1$s";
        public static final String API_EXPLORE_MSG_LIST = "api/msg/list?";
        public static final String API_EXPLORE_NOTIFICATIONS = "api/mobile/explore/notices?";
        public static final String API_FAVORITES = "api/users/%1$s/favorites";
        public static final String API_FAVORITES_ADD = "api/%1$s/%2$s/favorites/add";
        public static final String API_FAVORITES_REMOVE = "api/%1$s/%2$s/favorites/remove";
        public static final String API_FOLLOWER_LIST = "api/users/%1$s/followers";
        public static final String API_FOLLOWING_LIST = "api/users/%1$s/followings";
        public static final String API_FOLLOW_ADD = "api/users/%1$s/follow";
        public static final String API_FOLLOW_CHECK = "api/users/%1$s/is_follow";
        public static final String API_FOLLOW_REMOVE = "api/users/%1$s/unfollow";
        public static final String API_GL_COMMENTS = "api/stories/%1$s/comments";
        public static final String API_GL_DETAIL = "api/stories/%1$s";
        public static final String API_GL_DETAIL_WITH_TOKEN = "api/stories/%1$s?token=%2$s";
        public static final String API_GL_LIKE_ADD = "api/stories/%1$s/likes/add";
        public static final String API_GL_LIKE_REMOVE = "api/stories/%1$s/likes/remove";
        public static final String API_GL_SHARE = "tips/%1$s";
        public static final String API_GUIDE_LIST = "api/stories";
        public static final String API_LATEST_PHOTO = "api/photos/latest";
        public static final String API_LATEST_VIDEO = "api/videos/latest";
        public static final String API_LIKE_ADD = "api/%1$s/%2$s/likes/add";
        public static final String API_LIKE_LIST = "api/%1$s/%2$s/likes";
        public static final String API_LIKE_REMOVE = "api/%1$s/%2$s/likes/remove";
        public static final String API_POPULAR_PHOTO = "api/photos/popular";
        public static final String API_POPULAR_VIDEO = "api/videos/popular";
        public static final String API_SEARCH_PHOTO = "api/search/photos";
        public static final String API_SEARCH_URL = "api/search_with_keyword";
        public static final String API_SEARCH_VIDEO = "api/search/videos";
        public static final String API_USER_FOLLOWING_WORKS = "api/users/following";
        public static final String TAG_FREEEYE = "video_splited_collections";
        public static final String TAG_PHOTO = "photos";
        public static final String TAG_VIDEO = "videos";
    }

    public interface IMine {
        public static final String API_ARTWORK_LIST = "api/users/%1$s/home/?page=%2$d&page_size=%3$d";
        public static final String API_ARTWORK_LIST_WITH_TOKEN = "api/users/%1$s/home/?page=%2$d&page_size=%3$d&token=%4$s";
        public static final String API_ARTWORK_PHOTO_DETAIL = "pilot/photos/share/%1$s";
        public static final String API_ARTWORK_TAGS = "pilot/tags";
        public static final String API_ARTWORK_VIDEO_DETAIL = "pilot/videos/share/%1$s";
    }

    public interface IAccount {
        public static final String API_FORGOT_PASSWORD = "api/forgot_password";
        public static final String API_GET_MY_PROFILES = "api/profiles/my";
        public static final String API_GET_VALIDATE_CODE_IMAGE = "api/website/validate_code_img?key=%1$s";
        public static final String API_LOGOUT = "api/logout.json";
        public static final String API_NEW_SIGN_IN = "api/new_sign_in";
        public static final String API_REGISTER = "api/register";
        public static final String API_UPDATE_MY_PROFILES_AVATAR = "api/profiles/my/avatar";
    }

    public interface INotice {
        public static final String API_NOTICE = "api/mobile/explore/alert?";
    }

    public interface IShare {
        public static final String API_FREE_EYE_TOKEN_WITH_BUCKETS = "api/token_with_buckets";
        public static final String API_FREE_EYE_VIDEO_SPLITED_COLLECTIONS = "api/video_splited_collections";
        public static final String API_PANO_UPLOAD_DONE = "api/panoramas/upload_done";
        public static final String API_PANO_UPLOAD_TOKEN = "api/panoramas/upload_token";
        public static final String API_SHARE_COMPLETE_GET_CONTEST = "api/events/active_events?type=%1$s";
        public static final String API_SHARE_COMPLETE_JOIN_CONTEST = "api/events/join_events";
        public static final String API_UPLOAD_IMAGE = "imageserver/upload";
        public static final String API_UPLOAD_PANO = "api/panoramas/upload";
        public static final String API_UPLOAD_VIDEO = "api/website/upload/video";
        public static final String API_UPLOAD_VIDEO_COVER = "api/videos/%1$s/update_cover";
        public static final String API_VIDEO_DIRECT_UPLOAD_FINISH = "api/website/upload/direct_upload_finished";
        public static final String API_VIDEO_DIRECT_UPLOAD_INIT = "api/website/upload/init_direct_video_uploader";
    }

    public interface ISplash {
        public static final String API_SPLASH = "api/mobile/explore/splashes";
    }

    public interface ICoupon {
        public static final String API_GIFT_CARDS_CLEAR_MSG = "api/giftcards/clear_notifications?token=%1$s";
        public static final String API_GIFT_CARDS_DETAIL = "api/giftcards/%1$s?lang=%2$s";
        public static final String API_GIFT_CARDS_INFO = "api/giftcards/info?token=%1$s&lang=%2$s";
        public static final String API_GIFT_CARDS_MSG = "api/giftcards/notifications?token=%1$s&contain_read=%2$b";
        public static final String API_GIFT_CARDS_POPUP = "api/giftcards/popup?token=%1$s&lang=%2$s";
        public static final String API_GIFT_CARDS_SHARE = "api/giftcards/detail_page/%1$s?lang=%2$s";
        public static final String API_HAS_NEW_GIFT_CARDS = "api/giftcards/has_new_giftcard?token=%1$s";
    }
}
