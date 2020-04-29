package dji.component.accountcenter;

import android.support.annotation.Keep;
import android.text.TextUtils;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Keep
public class MemberInfo {
    public static final int ACCOUNT_TYPE_COMPANY = 1;
    public static final int ACCOUNT_TYPE_PERSONAL = 0;
    public static final int GENDER_FEMALE = 2;
    public static final int GENDER_MALE = 1;
    public static final int GENDER_UNKNOWN = 0;
    private static final String STRING_EMPTY = "";
    public static final String VIP_LEVEL_00 = "00";
    public static final String VIP_LEVEL_01 = "01";
    public static final String VIP_LEVEL_02 = "02";
    public String mAbout = "";
    public String mAccount = "";
    public int mAccountType = 0;
    public String mAuthCookieKey = "";
    public String mAuthCookieName = "";
    public String mAvatarUrl = "";
    public String mBio = "";
    public String mCity = "";
    public String mCompany = "";
    public String mCompanyDesc = "";
    public String mCountry = "";
    public String mCountryCode = "";
    public String mCreateTime = "";
    public String mCurrency = "";
    public long mDdsAvailable = 0;
    public long mDdsAvailableSoon = 0;
    @Deprecated
    public String mEmail = "";
    public String mFackBookUrl = "";
    public int mFavoritesCount = 0;
    public String mFirstName = "";
    public final List<FlightInfo> mFlights = new ArrayList();
    public String mGear = "";
    public int mGender = 1;
    public String mGooglePlusUrl = "";
    public String mId = "";
    public boolean mIsPhoneRegister = false;
    public String mLastName = "";
    public int mLikesCount = 0;
    public String mName = "";
    public String mNickName = "";
    public String mPassword = "";
    public String mPhoneArea = "";
    public String mPhoneNumber = "";
    public int mPhotosCount = 0;
    public String mPinterestUrl = "";
    public Region mRegion = null;
    public String mState = "";
    public String mToken = "";
    public String mTumblrUrl = "";
    public String mTwitterUrl = "";
    public String mUid = "";
    public String mUsedEmail = "";
    public int mVideosCount = 0;
    public String mVipLevel = "00";
    public String mWebPage = "";

    public void copyOf(MemberInfo mi) {
        this.mId = mi.mId;
        this.mUid = mi.mUid;
        this.mEmail = mi.mEmail;
        this.mUsedEmail = mi.mUsedEmail;
        this.mAccount = mi.mAccount;
        this.mNickName = mi.mNickName;
        this.mPassword = mi.mPassword;
        this.mAvatarUrl = mi.mAvatarUrl;
        this.mAbout = mi.mAbout;
        this.mGender = mi.mGender;
        this.mFirstName = mi.mFirstName;
        this.mLastName = mi.mLastName;
        this.mName = mi.mName;
        this.mBio = mi.mBio;
        this.mWebPage = mi.mWebPage;
        this.mCountry = mi.mCountry;
        this.mCountryCode = mi.mCountryCode;
        this.mState = mi.mState;
        this.mCity = mi.mCity;
        this.mGear = mi.mGear;
        this.mFackBookUrl = mi.mFackBookUrl;
        this.mTwitterUrl = mi.mTwitterUrl;
        this.mGooglePlusUrl = mi.mGooglePlusUrl;
        this.mPinterestUrl = mi.mPinterestUrl;
        this.mTumblrUrl = mi.mTumblrUrl;
        this.mAccountType = mi.mAccountType;
        this.mCompany = mi.mCompany;
        this.mCompanyDesc = mi.mCompanyDesc;
        this.mCreateTime = mi.mCreateTime;
        this.mLikesCount = mi.mLikesCount;
        this.mFavoritesCount = mi.mFavoritesCount;
        this.mPhotosCount = mi.mPhotosCount;
        this.mVideosCount = mi.mVideosCount;
        this.mPhoneArea = mi.mPhoneArea;
        this.mIsPhoneRegister = mi.mIsPhoneRegister;
        this.mAuthCookieName = mi.mAuthCookieName;
        this.mAuthCookieKey = mi.mAuthCookieKey;
        this.mVipLevel = mi.mVipLevel;
        this.mToken = mi.mToken;
        this.mRegion = mi.mRegion;
        this.mDdsAvailable = mi.mDdsAvailable;
        this.mDdsAvailableSoon = mi.mDdsAvailableSoon;
        this.mCurrency = mi.mCurrency;
        this.mFlights.clear();
        if (!mi.mFlights.isEmpty()) {
            this.mFlights.addAll(mi.mFlights);
        }
    }

    public void resetInfo() {
        this.mId = "";
        this.mAccount = "";
        this.mNickName = "";
        this.mPassword = "";
        this.mAvatarUrl = "";
        this.mAbout = "";
        this.mGender = 0;
        this.mFirstName = "";
        this.mLastName = "";
        this.mName = "";
        this.mBio = "";
        this.mWebPage = "";
        this.mCountry = "";
        this.mCountryCode = "";
        this.mState = "";
        this.mCity = "";
        this.mGear = "";
        this.mFackBookUrl = "";
        this.mTwitterUrl = "";
        this.mGooglePlusUrl = "";
        this.mPinterestUrl = "";
        this.mTumblrUrl = "";
        this.mAccountType = 0;
        this.mCompany = "";
        this.mCompanyDesc = "";
        this.mCreateTime = "";
        this.mPhoneArea = "";
        this.mToken = "";
        this.mIsPhoneRegister = false;
        this.mAuthCookieName = "";
        this.mAuthCookieKey = "";
        this.mVipLevel = "00";
        this.mLikesCount = 0;
        this.mFavoritesCount = 0;
        this.mPhotosCount = 0;
        this.mVideosCount = 0;
        this.mRegion = null;
        this.mDdsAvailable = 0;
        this.mDdsAvailableSoon = 0;
        this.mCurrency = "";
        this.mFlights.clear();
    }

    public boolean isVip() {
        return TextUtils.equals(this.mVipLevel, VIP_LEVEL_01) || TextUtils.equals(this.mVipLevel, "02");
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof MemberInfo)) {
            return false;
        }
        MemberInfo that = (MemberInfo) o;
        if (!Objects.equals(this.mId, that.mId) || !Objects.equals(this.mToken, that.mToken)) {
            return false;
        }
        return true;
    }

    public int hashCode() {
        if (this.mId != null) {
            return 17 + (this.mId.hashCode() * 31);
        }
        if (this.mToken != null) {
            return 17 + (this.mToken.hashCode() * 31);
        }
        return 17;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder(56);
        sb.append("id[").append(this.mId).append(IMemberProtocol.STRING_SEPERATOR_RIGHT);
        sb.append("email[").append(this.mEmail).append(IMemberProtocol.STRING_SEPERATOR_RIGHT);
        sb.append("pwd[").append(this.mPassword).append(IMemberProtocol.STRING_SEPERATOR_RIGHT);
        sb.append("nickname[").append(this.mNickName).append(IMemberProtocol.STRING_SEPERATOR_RIGHT);
        sb.append("token[").append(this.mToken).append(IMemberProtocol.STRING_SEPERATOR_RIGHT);
        sb.append("cookieName[").append(this.mAuthCookieName).append(IMemberProtocol.STRING_SEPERATOR_RIGHT);
        sb.append("cookieKey[").append(this.mAuthCookieKey).append(IMemberProtocol.STRING_SEPERATOR_RIGHT);
        sb.append("vipLevel[").append(this.mVipLevel).append(IMemberProtocol.STRING_SEPERATOR_RIGHT);
        return sb.toString();
    }
}
