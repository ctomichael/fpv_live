package dji.apppublic.reflect;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.config.P3.ProductType;
import dji.midware.data.model.P3.DataCommonGetVersion;
import dji.pilot.fpv.control.DJIRedundancySysController;
import dji.pilot.publics.model.DJIUpgradePackListModel;

@EXClassNullAway
public interface AppPubToP3Injectable {
    void dji_gs_Config_setUnitFT(boolean z);

    void downloadRemoteData();

    Dialog generateFaultInjectToolDialog(Context context);

    Dialog generateGpsToolDialog(Context context);

    Dialog generateRedundancyToolDialog(Context context);

    String getActivateShowType(Context context, ProductType productType);

    String getActiveName(ProductType productType);

    String getActivePlaneName(ProductType productType);

    Activity getActivity();

    String getAircraftVersion();

    String getAppVersion();

    String getBatterySN();

    String getDeviceToken();

    String getDeviceVersion();

    DataCommonGetVersion getFromKey(String str);

    Integer getLoaderByte(String str, int i);

    String getLoaderFromConfig(String str);

    String getNeedUpgradeDevices();

    ProductType getProductTypeByName(String str);

    String getRcVersionName();

    String getSN();

    String getSnV2();

    DJIUpgradePackListModel.DJIUpgradePack getUpgradePack(ProductType productType);

    String getVersion(String str);

    boolean isDevelopPackage();

    Boolean isFactoryMode();

    boolean isFactoryPackage();

    boolean isGetted(String str);

    boolean isInFpv();

    boolean isInMainActivity();

    boolean isInnerPackage();

    boolean isNewApp();

    boolean isSmartMode();

    void openInnerTools(Context context);

    void resetCenterPoint();

    void sensorPopWindow(DJIRedundancySysController.RedundancyErrorInfo redundancyErrorInfo);

    void sensorPopWindow(Integer num);

    void setCameraSN(String str);

    void setCountryCode(String str);

    void setFlycSN(String str);

    void setRcSN(String str);

    void startActivate();

    void startDJICareActivity(Context context, String str);
}
