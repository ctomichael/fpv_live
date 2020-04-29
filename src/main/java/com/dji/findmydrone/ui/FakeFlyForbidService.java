package com.dji.findmydrone.ui;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import dji.component.flysafe.IDJIFlySafe;
import dji.component.flysafe.IDbUpgrade;
import dji.component.flysafe.listener.CommonHttpCallback;
import dji.component.flysafe.listener.DJIUnlimitCallback;
import dji.component.flysafe.listener.JNICommonCallbacks;
import dji.component.flysafe.listener.JNIUnlockCommonCallbacks;
import dji.component.flysafe.listener.OnFlysafeWarnListener;
import dji.component.flysafe.listener.OnLimitDataChangedListener;
import dji.component.flysafe.model.FlyForbidDrawParam;
import dji.component.flysafe.model.FlyForbidElement;
import dji.component.flysafe.model.FlyfrbAreaJniElement;
import dji.component.flysafe.model.JNIWarnModelWrappers;
import dji.component.flysafe.unlock.model.AccountStateBeforeUnlock;
import dji.component.flysafe.unlock.model.FlyfrbLicenseV3GroupData;
import dji.component.flysafe.unlock.model.FlyfrbLicenseV3Info;
import dji.component.flysafe.unlock.model.WhiteListLicense;
import dji.service.IDJIService;
import io.reactivex.Single;
import java.util.Collections;
import java.util.List;

public class FakeFlyForbidService implements IDJIService, IDJIFlySafe {
    public void configAfterAssetInstall() {
    }

    public IDbUpgrade getDbUpgradeComponent() {
        return null;
    }

    public List<FlyfrbAreaJniElement> getCurAreasToUnlock() {
        return Collections.emptyList();
    }

    public void unlockSwArea(List<Integer> list, JNIUnlockCommonCallbacks.JNIUnlockCallback callback) {
    }

    public void queryUnlockV3KeyVersionJni(JNIUnlockCommonCallbacks.JNIUnlockCommonCallbackWith<Integer> jNIUnlockCommonCallbackWith) {
    }

    public void unlockAreasJni(@Nullable List<Integer> list, JNIUnlockCommonCallbacks.UnlockCallbackWithErrorEnum callback) {
    }

    public void unlockAreasLicensesInUavJni(@Nullable List<Integer> list, JNIUnlockCommonCallbacks.UnlockCallbackWithErrorEnum callback) {
    }

    public void disableUnlockLicensesJni(@NonNull List<Integer> list, JNIUnlockCommonCallbacks.UnlockCallbackWithErrorEnum callback) {
    }

    public void fetchCachedLicenseInfoJni(JNIUnlockCommonCallbacks.JNIUnlockCommonCallbackWith<List<FlyfrbLicenseV3Info>> jNIUnlockCommonCallbackWith) {
    }

    public void fetchCachedLicenseGroupInfoJni(JNIUnlockCommonCallbacks.JNIUnlockCommonCallbackWith<List<FlyfrbLicenseV3GroupData>> jNIUnlockCommonCallbackWith) {
    }

    public void fetchServerLicenseGroupInfoJni(JNIUnlockCommonCallbacks.UnlockCommonCallbackWithInfo<List<FlyfrbLicenseV3GroupData>> unlockCommonCallbackWithInfo) {
    }

    public void fetchServerLicenseInfoJni(JNIUnlockCommonCallbacks.UnlockCommonCallbackWithInfo<List<FlyfrbLicenseV3Info>> unlockCommonCallbackWithInfo) {
    }

    public void queryFCLicensesJni(JNIUnlockCommonCallbacks.JNIUnlockCommonCallbackWith<List<WhiteListLicense>> jNIUnlockCommonCallbackWith) {
    }

    public void uploadLicenseGroupDataJni(int groupId, JNIUnlockCommonCallbacks.UnlockCallbackWithErrorEnum callback) {
    }

    public void setLicenseEnableJni(boolean isEnable, int licenseId, JNIUnlockCommonCallbacks.UnlockCommonCallbackWithInfo<boolean[]> unlockCommonCallbackWithInfo) {
    }

    public String getUserIdForAirmapVerify() {
        return "";
    }

    public void verifyBeforUnlock(DJIUnlimitCallback callback) {
    }

    public Single<AccountStateBeforeUnlock> getAccountStateObservable() {
        return null;
    }

    public void submitErrorReport(double lat, double lng, String message, int area_id, CommonHttpCallback submitCallback) {
    }

    public List<FlyForbidElement> getLimitData() {
        return Collections.emptyList();
    }

    public void fetchNearLimitDataByPhoneGps(JNICommonCallbacks.JNICommonCallbackWith<List<FlyForbidElement>> jNICommonCallbackWith) {
    }

    public void fetchNearLimitDataByGps(Double lat, Double lng, JNICommonCallbacks.JNICommonCallbackWith<List<FlyForbidElement>> jNICommonCallbackWith) {
    }

    public void fetchHighestOrderNearLimitDataByGps(Double lat, Double lng, JNICommonCallbacks.JNICommonCallbackWith<FlyForbidDrawParam> jNICommonCallbackWith) {
    }

    public void addLimitDataChangedListener(OnLimitDataChangedListener listener) {
    }

    public void removeLimitDataChangedListener(OnLimitDataChangedListener listener) {
    }

    public JNIWarnModelWrappers.AirportWarningAreaTakeoffState getChinaAirportWarningState() {
        return JNIWarnModelWrappers.AirportWarningAreaTakeoffState.OUTSIDE;
    }

    public boolean isSpecialUnlockEnable() {
        return false;
    }

    public boolean isThereFrbAreaAround() {
        return false;
    }

    public void addFlysafeWarnListener(OnFlysafeWarnListener listener) {
    }

    public void removeFlysafeWarnListener(OnFlysafeWarnListener listener) {
    }

    public double convertCoord4Log(double coordVal) {
        return 0.0d;
    }

    public String getCurHttpReqUuid() {
        return "";
    }

    public String getHttpReqAndroidAcc() {
        return "";
    }

    public String getCurFlyfrbRequestUuid() {
        return "";
    }

    public void checkOneHourApplyTFR() {
    }

    public void checkThreeMinApplyTFR() {
    }

    public void init(Context context) {
    }

    public String getName() {
        return IDJIFlySafe.NAME_FLYSAFE_SERVICE;
    }

    public int priority() {
        return 0;
    }
}
