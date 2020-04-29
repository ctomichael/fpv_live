package com.dji.findmydrone.ui;

import android.content.Context;
import dji.component.appstate.IAppStateService;
import dji.service.IDJIService;

class FakeAppStateService implements IDJIService, IAppStateService {
    FakeAppStateService() {
    }

    public String getAppVersion() {
        return null;
    }

    public boolean isInFpv() {
        return false;
    }

    public boolean isForeGround() {
        return false;
    }

    public boolean isInnerApp() {
        return false;
    }

    public void setIsInnerApp(boolean isInnerApp) {
    }

    public boolean isDebugApp() {
        return false;
    }

    public void setIsDebugApp(boolean isDebugApp) {
    }

    public void setFlavor(String flavor) {
    }

    public String getFlavor() {
        return null;
    }

    public boolean getNetWorkStatus() {
        return false;
    }

    public String getBuildNumber() {
        return null;
    }

    public String getAppVersionString() {
        return null;
    }

    public String getUUID() {
        return null;
    }

    public void init(Context context) {
    }

    public String getName() {
        return IAppStateService.NAME;
    }

    public int priority() {
        return 0;
    }
}
