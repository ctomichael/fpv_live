package com.dji.analytics;

import android.content.Context;
import dji.component.flysafe.FlyForbidProtocol;
import java.io.Serializable;
import java.util.HashMap;

public class ReportConfig implements Serializable {
    private String a;
    private String b;
    private HashMap<String, String> c;
    public boolean is4GEnable = false;

    public String getEventReportUrl() {
        return "https://statistical-report.djiservice.org/api/report/clientContext";
    }

    public synchronized void addExtraData(String str, String str2) {
        if (this.c == null) {
            this.c = new HashMap<>();
        }
        this.c.put(str, str2);
    }

    public HashMap<String, String> getExtraData() {
        return this.c;
    }

    public String getBaseInfoReportUrl() {
        return "https://statistical-report.djiservice.org/api/report/clientUUID";
    }

    public HashMap<String, String> getHttpHeader() {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("ostype", FlyForbidProtocol.PLATFORM_NAME_FOR_JNI);
        return hashMap;
    }

    public int getTimeOut() {
        return 30000;
    }

    public int getSendItemCount() {
        return 5;
    }

    public void setAppKey(String str) {
        this.a = str;
    }

    public String getAppKey() {
        return this.a;
    }

    public void setAppId(String str) {
        this.b = str;
    }

    public String getAppId() {
        return this.b;
    }

    public Context getContext() {
        return DJIA.mContext;
    }

    public int getDelayMillSeconds() {
        if (!DJIA.DEV_FLAG) {
            return 180000;
        }
        return 500;
    }

    public enum a {
        INIT_DATA("init_data"),
        LOG_DATA("log_data");
        
        private String c;

        private a(String str) {
            this.c = str;
        }

        public String a() {
            return this.c;
        }
    }
}
