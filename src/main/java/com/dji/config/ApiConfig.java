package com.dji.config;

import com.dji.fieldAnnotation.EXClassNullAway;

@EXClassNullAway
public class ApiConfig {
    private static final String TAG = ApiConfig.class.getSimpleName();
    private static volatile ApiConfig sConfig;
    private static volatile boolean sHasConfig = false;
    private boolean isDebug = false;
    private boolean isFlyForbidBeta = false;
    private boolean isSDKBeta = false;
    private boolean isUpgradeBeta = false;

    public static ApiConfig getConfig() {
        if (sConfig == null) {
            synchronized (ApiConfig.class) {
                if (sConfig == null) {
                    sConfig = new ApiConfig();
                }
            }
        }
        return sConfig;
    }

    public boolean isFlyForbidBeta() {
        return this.isFlyForbidBeta;
    }

    public boolean isUpgradeBeta() {
        return this.isUpgradeBeta;
    }

    public boolean isSDKBeta() {
        return this.isSDKBeta;
    }

    public boolean isDebug() {
        return this.isDebug;
    }

    public void setDebug(boolean debug) {
        this.isDebug = debug;
    }

    public void setFlyForbidBeta(boolean flyForbidBeta) {
        this.isFlyForbidBeta = flyForbidBeta;
    }

    public void setSDKBeta(boolean SDKBeta) {
        this.isSDKBeta = SDKBeta;
    }

    public void setUpgradeBeta(boolean upgradeBeta) {
        this.isUpgradeBeta = upgradeBeta;
    }
}
