package com.dji.findmydrone.ui;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import dji.apppublic.reflect.AppPubToP3Injectable;
import dji.midware.data.config.P3.ProductType;
import dji.midware.data.model.P3.DataCommonGetVersion;
import dji.pilot.fpv.control.DJIRedundancySysController;
import dji.pilot.publics.model.DJIUpgradePackListModel;

public class FakeInjectManager implements AppPubToP3Injectable {
    public void openInnerTools(Context ctx) {
        try {
            Class.forName("com.dji.tools.base.InnerToolsGlobalDialog").getMethod("showInnerTools", Context.class).invoke(null, ctx);
        } catch (Exception e) {
        }
    }

    public boolean isDevelopPackage() {
        return true;
    }

    public boolean isInnerPackage() {
        return true;
    }

    public boolean isFactoryPackage() {
        return false;
    }

    public void dji_gs_Config_setUnitFT(boolean ft) {
    }

    public String getAircraftVersion() {
        return "";
    }

    public boolean isSmartMode() {
        return false;
    }

    public Boolean isFactoryMode() {
        return Boolean.FALSE;
    }

    public boolean isInMainActivity() {
        return false;
    }

    public Activity getActivity() {
        return null;
    }

    public String getActivePlaneName(ProductType type) {
        return "";
    }

    public ProductType getProductTypeByName(String name) {
        return ProductType.WM240;
    }

    public String getDeviceToken() {
        return "";
    }

    public String getSnV2() {
        return "";
    }

    public void startActivate() {
    }

    public String getBatterySN() {
        return "";
    }

    public void setCameraSN(String sn) {
    }

    public void setRcSN(String sn) {
    }

    public void setFlycSN(String sn) {
    }

    public void sensorPopWindow(DJIRedundancySysController.RedundancyErrorInfo rei) {
    }

    public void sensorPopWindow(Integer strId) {
    }

    public String getSN() {
        return "";
    }

    public String getDeviceVersion() {
        return "";
    }

    public String getActiveName(ProductType type) {
        return null;
    }

    public String getActivateShowType(Context context, ProductType type) {
        return null;
    }

    public String getAppVersion() {
        return "";
    }

    public void setCountryCode(String code) {
    }

    public boolean isNewApp() {
        return false;
    }

    public boolean isInFpv() {
        return false;
    }

    public void resetCenterPoint() {
    }

    public void downloadRemoteData() {
    }

    public boolean isGetted(String key) {
        return false;
    }

    public String getLoaderFromConfig(String key) {
        return "";
    }

    public String getVersion(String key) {
        return "";
    }

    public DataCommonGetVersion getFromKey(String key) {
        return null;
    }

    public Integer getLoaderByte(String formatModule, int num) {
        return 0;
    }

    public DJIUpgradePackListModel.DJIUpgradePack getUpgradePack(ProductType productType) {
        return null;
    }

    public String getRcVersionName() {
        return "";
    }

    public String getNeedUpgradeDevices() {
        return "";
    }

    public void startDJICareActivity(Context context, String sn) {
    }

    public Dialog generateGpsToolDialog(Context context) {
        return null;
    }

    public Dialog generateFaultInjectToolDialog(Context context) {
        return null;
    }

    public Dialog generateRedundancyToolDialog(Context context) {
        return null;
    }
}
