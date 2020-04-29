package dji.component.flysafe;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.Size;
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
import io.reactivex.Single;
import java.util.List;

public interface IDJIFlySafe {
    public static final String NAME_FLYSAFE_SERVICE = "FlysafeImpl";

    void addFlysafeWarnListener(OnFlysafeWarnListener onFlysafeWarnListener);

    void addLimitDataChangedListener(OnLimitDataChangedListener onLimitDataChangedListener);

    void checkOneHourApplyTFR();

    void checkThreeMinApplyTFR();

    void configAfterAssetInstall();

    double convertCoord4Log(double d);

    void disableUnlockLicensesJni(@NonNull List<Integer> list, JNIUnlockCommonCallbacks.UnlockCallbackWithErrorEnum unlockCallbackWithErrorEnum);

    void fetchCachedLicenseGroupInfoJni(JNIUnlockCommonCallbacks.JNIUnlockCommonCallbackWith<List<FlyfrbLicenseV3GroupData>> jNIUnlockCommonCallbackWith);

    void fetchCachedLicenseInfoJni(JNIUnlockCommonCallbacks.JNIUnlockCommonCallbackWith<List<FlyfrbLicenseV3Info>> jNIUnlockCommonCallbackWith);

    void fetchHighestOrderNearLimitDataByGps(Double d, Double d2, JNICommonCallbacks.JNICommonCallbackWith<FlyForbidDrawParam> jNICommonCallbackWith);

    void fetchNearLimitDataByGps(Double d, Double d2, JNICommonCallbacks.JNICommonCallbackWith<List<FlyForbidElement>> jNICommonCallbackWith);

    void fetchNearLimitDataByPhoneGps(JNICommonCallbacks.JNICommonCallbackWith<List<FlyForbidElement>> jNICommonCallbackWith);

    void fetchServerLicenseGroupInfoJni(JNIUnlockCommonCallbacks.UnlockCommonCallbackWithInfo<List<FlyfrbLicenseV3GroupData>> unlockCommonCallbackWithInfo);

    void fetchServerLicenseInfoJni(JNIUnlockCommonCallbacks.UnlockCommonCallbackWithInfo<List<FlyfrbLicenseV3Info>> unlockCommonCallbackWithInfo);

    Single<AccountStateBeforeUnlock> getAccountStateObservable();

    JNIWarnModelWrappers.AirportWarningAreaTakeoffState getChinaAirportWarningState();

    List<FlyfrbAreaJniElement> getCurAreasToUnlock();

    String getCurFlyfrbRequestUuid();

    String getCurHttpReqUuid();

    IDbUpgrade getDbUpgradeComponent();

    String getHttpReqAndroidAcc();

    List<FlyForbidElement> getLimitData();

    String getUserIdForAirmapVerify();

    boolean isSpecialUnlockEnable();

    boolean isThereFrbAreaAround();

    void queryFCLicensesJni(JNIUnlockCommonCallbacks.JNIUnlockCommonCallbackWith<List<WhiteListLicense>> jNIUnlockCommonCallbackWith);

    void queryUnlockV3KeyVersionJni(JNIUnlockCommonCallbacks.JNIUnlockCommonCallbackWith<Integer> jNIUnlockCommonCallbackWith);

    void removeFlysafeWarnListener(OnFlysafeWarnListener onFlysafeWarnListener);

    void removeLimitDataChangedListener(OnLimitDataChangedListener onLimitDataChangedListener);

    void setLicenseEnableJni(boolean z, int i, JNIUnlockCommonCallbacks.UnlockCommonCallbackWithInfo<boolean[]> unlockCommonCallbackWithInfo);

    void submitErrorReport(double d, double d2, String str, int i, CommonHttpCallback commonHttpCallback);

    void unlockAreasJni(@Nullable @Size(min = 1) List<Integer> list, JNIUnlockCommonCallbacks.UnlockCallbackWithErrorEnum unlockCallbackWithErrorEnum);

    void unlockAreasLicensesInUavJni(@Nullable @Size(min = 1) List<Integer> list, JNIUnlockCommonCallbacks.UnlockCallbackWithErrorEnum unlockCallbackWithErrorEnum);

    void unlockSwArea(List<Integer> list, JNIUnlockCommonCallbacks.JNIUnlockCallback jNIUnlockCallback);

    void uploadLicenseGroupDataJni(int i, JNIUnlockCommonCallbacks.UnlockCallbackWithErrorEnum unlockCallbackWithErrorEnum);

    @Deprecated
    void verifyBeforUnlock(DJIUnlimitCallback dJIUnlimitCallback);
}
