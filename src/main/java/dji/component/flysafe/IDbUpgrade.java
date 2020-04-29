package dji.component.flysafe;

import android.content.Context;
import dji.component.flysafe.dbupgrade.listener.DJIDataUpgradeListener;
import dji.component.flysafe.dbupgrade.listener.DJIDbUpgradeCheckListener;
import dji.component.flysafe.dbupgrade.listener.DJIDbUpgradeStateChangeListener;
import dji.component.flysafe.dbupgrade.listener.DbUpgradeCheckStateChangeListener;
import dji.component.flysafe.dbupgrade.listener.OnDbForceUgradeStateChangedListener;
import dji.component.flysafe.dbupgrade.listener.OnDbUpgradeCheckListener;
import dji.component.flysafe.dbupgrade.model.FlysafeDataUpgradeModelWrapper;
import dji.component.flysafe.listener.JNICommonCallbacks;
import dji.component.flysafe.listener.OnDatabaseChangedListener;

public interface IDbUpgrade {
    void addDbCheckListener(DJIDbUpgradeCheckListener dJIDbUpgradeCheckListener);

    void addDbUgProgressListener(DJIDataUpgradeListener dJIDataUpgradeListener);

    void addDbUpgradeCheckStateChangeListener(DbUpgradeCheckStateChangeListener dbUpgradeCheckStateChangeListener);

    void addDbUpgradeStateChangeListener(DJIDbUpgradeStateChangeListener dJIDbUpgradeStateChangeListener);

    void addDbVersionCheckListener(OnDbUpgradeCheckListener onDbUpgradeCheckListener);

    void addForceUpgradeStateListener(OnDbForceUgradeStateChangedListener onDbForceUgradeStateChangedListener);

    void addOnBasicDbListener(OnDatabaseChangedListener onDatabaseChangedListener);

    void addOnPreciseDbListener(OnDatabaseChangedListener onDatabaseChangedListener);

    void checkBasicDbVer();

    void checkPreciseDbVerManual(boolean z);

    void checkPreciseDbVerManualWithTimeout();

    void checkRemoteVersionFromUser();

    void checkRemoteVersionWithCnn();

    void clearCache(FlysafeDataUpgradeModelWrapper.TypeDataUpgrade typeDataUpgrade);

    void configAfterAssetInstall();

    String getAppPreciseDbVersion();

    String getBasicDbVersion();

    FlysafeDataUpgradeModelWrapper.DbCacheState getCacheState(FlysafeDataUpgradeModelWrapper.TypeDataUpgrade typeDataUpgrade);

    FlysafeDataUpgradeModelWrapper.DbUpgradeCheckStateWrapper getCurDbUpgradeCheckState();

    int getCurProgress();

    FlysafeDataUpgradeModelWrapper.DbUpgradeStateWrapper getDbUpgradeStateWrapper();

    String getDownloadedDbPathByType(FlysafeDataUpgradeModelWrapper.TypeDataUpgrade typeDataUpgrade);

    String getUAVPreciseDbVersion();

    void init(Context context);

    boolean isFinishChecking();

    boolean isOfflineDbUgInfoExist(String str, String str2);

    boolean isStartingUgradeProgress();

    void logDatabaseUpgrade(String str, String str2);

    boolean needForceUpdateLockMotor();

    void removeDbCheckListener(DJIDbUpgradeCheckListener dJIDbUpgradeCheckListener);

    void removeDbUgProgressListener(DJIDataUpgradeListener dJIDataUpgradeListener);

    void removeDbUpgradeCheckStateChangeListener(DbUpgradeCheckStateChangeListener dbUpgradeCheckStateChangeListener);

    void removeDbUpgradeStateChangeListener(DJIDbUpgradeStateChangeListener dJIDbUpgradeStateChangeListener);

    void removeDbVersionCheckListener(OnDbUpgradeCheckListener onDbUpgradeCheckListener);

    void removeForceUpgradeStateListener(OnDbForceUgradeStateChangedListener onDbForceUgradeStateChangedListener);

    void removeOnBasicDbListener(OnDatabaseChangedListener onDatabaseChangedListener);

    void removeOnPreciseDbListener(OnDatabaseChangedListener onDatabaseChangedListener);

    void replaceBasicDatabaseFile(String str, JNICommonCallbacks.JNIEnumCallback jNIEnumCallback);

    void replacePreciseDatabaseFile(String str, JNICommonCallbacks.JNIEnumCallback jNIEnumCallback);

    void setUseCache(FlysafeDataUpgradeModelWrapper.TypeDataUpgrade typeDataUpgrade, boolean z);

    void startCheckForInnerTool(String str);

    void startDownload();

    void startUploadData();
}
