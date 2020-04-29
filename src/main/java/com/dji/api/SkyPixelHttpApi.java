package com.dji.api;

import android.text.TextUtils;
import com.dji.api.protocol.ISkyPixelHttpApi;
import com.dji.fieldAnnotation.EXClassNullAway;
import com.dji.util.Util;
import java.util.Locale;

@EXClassNullAway
public class SkyPixelHttpApi implements ISkyPixelHttpApi {
    private static final String TAG = SkyPixelHttpApi.class.getSimpleName();
    private static String sDomainTest;

    public static void setDomainTest(String domainTest) {
        sDomainTest = domainTest;
    }

    public static String getNoticeUrl() {
        return getUrl(ISkyPixelHttpApi.INotice.API_NOTICE, new Object[0]);
    }

    public static String getSplashUrl() {
        return getUrl(ISkyPixelHttpApi.ISplash.API_SPLASH, new Object[0]);
    }

    public static String getExploreMsgListUrl() {
        return getUrl(ISkyPixelHttpApi.IExplore.API_EXPLORE_MSG_LIST, new Object[0]);
    }

    public static String getExploreNotificationsUrl() {
        return getUrl(ISkyPixelHttpApi.IExplore.API_EXPLORE_NOTIFICATIONS, new Object[0]);
    }

    public static String getExplorePopularPhotoUrl() {
        return getUrl(ISkyPixelHttpApi.IExplore.API_POPULAR_PHOTO, new Object[0]);
    }

    public static String getExplorePopularVideoUrl() {
        return getUrl(ISkyPixelHttpApi.IExplore.API_POPULAR_VIDEO, new Object[0]);
    }

    public static String getExploreLatestPhotoUrl() {
        return getUrl(ISkyPixelHttpApi.IExplore.API_LATEST_PHOTO, new Object[0]);
    }

    public static String getExploreLatestVideoUrl() {
        return getUrl(ISkyPixelHttpApi.IExplore.API_LATEST_VIDEO, new Object[0]);
    }

    public static String getExploreUserFollowingWorkUrl() {
        return getUrl(ISkyPixelHttpApi.IExplore.API_USER_FOLLOWING_WORKS, new Object[0]);
    }

    public static String getExploreSearchPhotoUrl() {
        return getUrl(ISkyPixelHttpApi.IExplore.API_SEARCH_PHOTO, new Object[0]);
    }

    public static String getExploreSearchVideoUrl() {
        return getUrl(ISkyPixelHttpApi.IExplore.API_SEARCH_VIDEO, new Object[0]);
    }

    public static String getExploreBannerAdsUrl() {
        return getUrl(ISkyPixelHttpApi.IExplore.API_BANNER_ADS, new Object[0]);
    }

    public static String getExploreArtWorkDetailUrl() {
        return getUrl(ISkyPixelHttpApi.IExplore.API_ARTWORK_DETAIL, new Object[0]);
    }

    public static String getExploreLikeListUrl(String type, String id) {
        return getUrl(ISkyPixelHttpApi.IExplore.API_LIKE_LIST, type, id);
    }

    public static String getExploreLikeAddUrl(String type, String id) {
        return getUrl(ISkyPixelHttpApi.IExplore.API_LIKE_ADD, type, id);
    }

    public static String getExploreLikeRemoveUrl(String type, String id) {
        return getUrl(ISkyPixelHttpApi.IExplore.API_LIKE_REMOVE, type, id);
    }

    public static String getExploreFollowAddUrl(String id) {
        return getUrl(ISkyPixelHttpApi.IExplore.API_FOLLOW_ADD, id);
    }

    public static String getExploreFollowRemoveUrl(String id) {
        return getUrl(ISkyPixelHttpApi.IExplore.API_FOLLOW_REMOVE, id);
    }

    public static String getExploreFollowCheckUrl(String id) {
        return getUrl(ISkyPixelHttpApi.IExplore.API_FOLLOW_CHECK, id);
    }

    public static String getExploreFollowerListUrl(String id) {
        return getUrl(ISkyPixelHttpApi.IExplore.API_FOLLOWER_LIST, id);
    }

    public static String getExploreFollowingListUrl(String id) {
        return getUrl(ISkyPixelHttpApi.IExplore.API_FOLLOWING_LIST, id);
    }

    public static String getExploreDeletePhotoUrl(String id) {
        return getUrl(ISkyPixelHttpApi.IExplore.API_DELETE_PHOTO, id);
    }

    public static String getExploreDeleteVideoUrl(String id) {
        return getUrl(ISkyPixelHttpApi.IExplore.API_DELETE_VIDEO, id);
    }

    public static String getExploreDeleteFreeEyeUrl(String id) {
        return getUrl(ISkyPixelHttpApi.IExplore.API_DELETE_FREE_EYE, id);
    }

    public static String getExploreSearchUrl() {
        return getUrl(ISkyPixelHttpApi.IExplore.API_SEARCH_URL, new Object[0]);
    }

    public static String getExplorePhotoCommentUrl(String id) {
        return getUrl(ISkyPixelHttpApi.IExplore.API_COMMENT_PHOTO, id);
    }

    public static String getExploreVideoCommentUrl(String id) {
        return getUrl(ISkyPixelHttpApi.IExplore.API_COMMENT_VIDEO, id);
    }

    public static String getExploreFreeEyeCommentUrl(String id) {
        return getUrl(ISkyPixelHttpApi.IExplore.API_COMMENT_FREE_EYE, id);
    }

    public static String getExploreFavoritesUrl(String id) {
        return getUrl(ISkyPixelHttpApi.IExplore.API_FAVORITES, id);
    }

    public static String getExploreGuideListUrl() {
        return getUrl(ISkyPixelHttpApi.IExplore.API_GUIDE_LIST, new Object[0]);
    }

    public static String getExploreFavoritesAddUrl(String type, String id) {
        return getUrl(ISkyPixelHttpApi.IExplore.API_FAVORITES_ADD, type, id);
    }

    public static String getExploreFavoritesRemoveUrl(String type, String id) {
        return getUrl(ISkyPixelHttpApi.IExplore.API_FAVORITES_REMOVE, type, id);
    }

    public static String getExploreGuideListCommentsUrl(String id) {
        return getUrl(ISkyPixelHttpApi.IExplore.API_GL_COMMENTS, id);
    }

    public static String getExploreGuideListDetailUrl(String id, String token) {
        if (!TextUtils.isEmpty(token)) {
            return getUrl(ISkyPixelHttpApi.IExplore.API_GL_DETAIL_WITH_TOKEN, id, token);
        }
        return getUrl(ISkyPixelHttpApi.IExplore.API_GL_DETAIL, id);
    }

    public static String getExploreGuideListLikeAddUrl(String id) {
        return getUrl(ISkyPixelHttpApi.IExplore.API_GL_LIKE_ADD, id);
    }

    public static String getExploreGuideListLikeRemoveUrl(String id) {
        return getUrl(ISkyPixelHttpApi.IExplore.API_GL_LIKE_REMOVE, id);
    }

    public static String getExploreGuideListShareUrl(String id) {
        return getUrl(ISkyPixelHttpApi.IExplore.API_GL_SHARE, id);
    }

    public static String getMineArtworkTagsUrl() {
        return getUrl(ISkyPixelHttpApi.IMine.API_ARTWORK_TAGS, new Object[0]);
    }

    public static String getMineArtworkListUrl(String id, int page, int pageSize, String token) {
        if (TextUtils.isEmpty(token)) {
            return getUrl(ISkyPixelHttpApi.IMine.API_ARTWORK_LIST, id, Integer.valueOf(page), Integer.valueOf(pageSize));
        }
        return getUrl(ISkyPixelHttpApi.IMine.API_ARTWORK_LIST_WITH_TOKEN, id, Integer.valueOf(page), Integer.valueOf(pageSize), token);
    }

    public static String getMineVideoArtWorkDetailUrl(String id) {
        return getUrl(ISkyPixelHttpApi.IMine.API_ARTWORK_VIDEO_DETAIL, id);
    }

    public static String getMinePhotoArtWorkDetailUrl(String id) {
        return getUrl(ISkyPixelHttpApi.IMine.API_ARTWORK_PHOTO_DETAIL, id);
    }

    public static String getCouponHasNewGiftCardsUrl(String token) {
        return getUrl(ISkyPixelHttpApi.ICoupon.API_HAS_NEW_GIFT_CARDS, token);
    }

    public static String getCouponGiftCardsInfoUrl(String token, String lang) {
        return getUrl(ISkyPixelHttpApi.ICoupon.API_GIFT_CARDS_INFO, token, lang);
    }

    public static String getCouponGiftCardsDetailUrl(String id, String lang) {
        return getUrl(ISkyPixelHttpApi.ICoupon.API_GIFT_CARDS_DETAIL, id, lang);
    }

    public static String getCouponGiftCardsMsgUrl(String token, boolean containRead) {
        return getUrl(ISkyPixelHttpApi.ICoupon.API_GIFT_CARDS_MSG, token, Boolean.valueOf(containRead));
    }

    public static String getCouponGiftCardsShareUrl(String id, String lang) {
        return getUrl(ISkyPixelHttpApi.ICoupon.API_GIFT_CARDS_SHARE, id, lang);
    }

    public static String getCouponGiftCardsClearMsgUrl(String token) {
        return getUrl(ISkyPixelHttpApi.ICoupon.API_GIFT_CARDS_CLEAR_MSG, token);
    }

    public static String getCouponGiftCardsPopupUrl(String token, String lang) {
        return getUrl(ISkyPixelHttpApi.ICoupon.API_GIFT_CARDS_POPUP, token, lang);
    }

    public static String getShareUploadImageUrl() {
        return getUrl(ISkyPixelHttpApi.IShare.API_UPLOAD_IMAGE, new Object[0]);
    }

    public static String getShareUploadVideoUrl() {
        return getUrl(ISkyPixelHttpApi.IShare.API_UPLOAD_VIDEO, new Object[0]);
    }

    public static String getShareCompleteGetContestUrl(String type) {
        return getUrl(ISkyPixelHttpApi.IShare.API_SHARE_COMPLETE_GET_CONTEST, type);
    }

    public static String getShareCompleteJointContestUrl() {
        return getUrl(ISkyPixelHttpApi.IShare.API_SHARE_COMPLETE_JOIN_CONTEST, new Object[0]);
    }

    public static String getShareUploadVideoCoverUrl(String id) {
        return getUrl(ISkyPixelHttpApi.IShare.API_UPLOAD_VIDEO_COVER, id);
    }

    public static String getShareVideoDirectUploadFinishUrl() {
        return getUrl(ISkyPixelHttpApi.IShare.API_VIDEO_DIRECT_UPLOAD_FINISH, new Object[0]);
    }

    public static String getShareVideoDirectUploadInitUrl() {
        return getUrl(ISkyPixelHttpApi.IShare.API_VIDEO_DIRECT_UPLOAD_INIT, new Object[0]);
    }

    public static String getShareFreeEyeVideoSplitedCollectionsUrl() {
        return getUrl(ISkyPixelHttpApi.IShare.API_FREE_EYE_VIDEO_SPLITED_COLLECTIONS, new Object[0]);
    }

    public static String getShareFreeEyeTokenWidthBucketsUrl() {
        return getUrl(ISkyPixelHttpApi.IShare.API_FREE_EYE_TOKEN_WITH_BUCKETS, new Object[0]);
    }

    public static String getSharePanoUploadTokenUrl() {
        return getUrl(ISkyPixelHttpApi.IShare.API_PANO_UPLOAD_TOKEN, new Object[0]);
    }

    public static String getSharePanoUploadUrl() {
        return getUrl(ISkyPixelHttpApi.IShare.API_UPLOAD_PANO, new Object[0]);
    }

    public static String getSharePanoUploadDoneUrl() {
        return getUrl(ISkyPixelHttpApi.IShare.API_PANO_UPLOAD_DONE, new Object[0]);
    }

    public static String getAccountForgotPasswordUrl() {
        return getUrl(ISkyPixelHttpApi.IAccount.API_FORGOT_PASSWORD, new Object[0]);
    }

    public static String getAccountRegisterUrl() {
        return getUrl(ISkyPixelHttpApi.IAccount.API_REGISTER, new Object[0]);
    }

    public static String getAccountNewSignInUrl() {
        return getUrl(ISkyPixelHttpApi.IAccount.API_NEW_SIGN_IN, new Object[0]);
    }

    public static String getAccountGetValidateCodeImageUrl(String key) {
        return getUrl(ISkyPixelHttpApi.IAccount.API_GET_VALIDATE_CODE_IMAGE, key);
    }

    public static String getAccountLogoutUrl() {
        return getUrl(ISkyPixelHttpApi.IAccount.API_LOGOUT, new Object[0]);
    }

    public static String getAccountMyProfilesUrl() {
        return getUrl(ISkyPixelHttpApi.IAccount.API_GET_MY_PROFILES, new Object[0]);
    }

    public static String getAccountUpdateAvatarUrl() {
        return getUrl(ISkyPixelHttpApi.IAccount.API_UPDATE_MY_PROFILES_AVATAR, new Object[0]);
    }

    private static String getUrl(String api, Object... args) {
        if (args == null || args.length <= 0) {
            return generateUrl(api);
        }
        return String.format(Locale.US, generateUrl(api), args);
    }

    private static String generateUrl(String... route) {
        return Util.generateUrl(ISkyPixelHttpApi.DOMAIN, sDomainTest, route);
    }

    public static String getDomain() {
        return Util.getDomain(ISkyPixelHttpApi.DOMAIN, sDomainTest);
    }
}
