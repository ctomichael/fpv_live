package com.mapbox.mapboxsdk;

import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.annotation.VisibleForTesting;
import android.text.TextUtils;
import com.mapbox.android.accounts.v1.MapboxAccounts;
import com.mapbox.mapboxsdk.constants.MapboxConstants;
import com.mapbox.mapboxsdk.log.Logger;

class AccountsManager {
    private static final String PREFERENCE_TIMESTAMP = "com.mapbox.mapboxsdk.accounts.timestamp";
    private static final String PREFERENCE_USER_ID = "com.mapbox.mapboxsdk.accounts.userid";
    private static final String TAG = "Mbgl-AccountsManager";
    private boolean isManaged;
    private SharedPreferences sharedPreferences;
    private String skuToken;
    private long timestamp;

    AccountsManager() {
        this.isManaged = isSkuTokenManaged();
        initialize();
    }

    @VisibleForTesting
    AccountsManager(SharedPreferences sharedPreferences2, boolean isManaged2) {
        this.sharedPreferences = sharedPreferences2;
        this.isManaged = isManaged2;
        initialize();
    }

    private void initialize() {
        retrieveSkuTokenAndTimestamp();
        if (this.isManaged) {
            validateRotation(validateUserId());
        }
    }

    private boolean isSkuTokenManaged() {
        try {
            ApplicationInfo appInfo = retrieveApplicationInfo();
            if (appInfo.metaData != null) {
                return appInfo.metaData.getBoolean(MapboxConstants.KEY_META_DATA_MANAGE_SKU_TOKEN, true);
            }
            return true;
        } catch (Exception exception) {
            Logger.e(TAG, "Failed to read the package metadata: ", exception);
            return true;
        }
    }

    private ApplicationInfo retrieveApplicationInfo() throws PackageManager.NameNotFoundException {
        return Mapbox.getApplicationContext().getPackageManager().getApplicationInfo(Mapbox.getApplicationContext().getPackageName(), 128);
    }

    private void retrieveSkuTokenAndTimestamp() {
        SharedPreferences sharedPreferences2 = getSharedPreferences();
        this.skuToken = sharedPreferences2.getString(MapboxConstants.KEY_PREFERENCE_SKU_TOKEN, "");
        this.timestamp = sharedPreferences2.getLong(PREFERENCE_TIMESTAMP, 0);
    }

    private String validateUserId() {
        String userId = getSharedPreferences().getString(PREFERENCE_USER_ID, "");
        if (!TextUtils.isEmpty(userId)) {
            return userId;
        }
        String userId2 = generateUserId();
        SharedPreferences.Editor editor = getSharedPreferences().edit();
        editor.putString(PREFERENCE_USER_ID, userId2);
        editor.apply();
        return userId2;
    }

    private void validateRotation(String userId) {
        if (TextUtils.isEmpty(this.skuToken) || this.timestamp == 0) {
            this.skuToken = generateSkuToken(userId);
            this.timestamp = persistRotation(this.skuToken);
        }
    }

    /* access modifiers changed from: package-private */
    public String getSkuToken() {
        if (!this.isManaged) {
            this.skuToken = getSharedPreferences().getString(MapboxConstants.KEY_PREFERENCE_SKU_TOKEN, "");
        } else if (isExpired()) {
            this.skuToken = generateSkuToken(getSharedPreferences().getString(PREFERENCE_USER_ID, ""));
            this.timestamp = persistRotation(this.skuToken);
        }
        return this.skuToken;
    }

    private boolean isExpired() {
        return isExpired(getNow(), this.timestamp);
    }

    static boolean isExpired(long now, long then) {
        return now - then > 3600000;
    }

    private long persistRotation(String skuToken2) {
        long now = getNow();
        SharedPreferences.Editor editor = getSharedPreferences().edit();
        editor.putLong(PREFERENCE_TIMESTAMP, now);
        editor.putString(MapboxConstants.KEY_PREFERENCE_SKU_TOKEN, skuToken2);
        editor.apply();
        return now;
    }

    @NonNull
    private SharedPreferences getSharedPreferences() {
        if (this.sharedPreferences == null) {
            this.sharedPreferences = Mapbox.getApplicationContext().getSharedPreferences("MapboxSharedPreferences", 0);
        }
        return this.sharedPreferences;
    }

    static long getNow() {
        return System.currentTimeMillis();
    }

    @NonNull
    private String generateUserId() {
        return MapboxAccounts.obtainEndUserId();
    }

    @NonNull
    private String generateSkuToken(String userId) {
        return MapboxAccounts.obtainMapsSkuUserToken(userId);
    }
}
