package com.amap.location;

import com.amap.location.common.HeaderConfig;
import com.amap.location.common.a;
import com.amap.location.common.network.IHttpClient;

public class BasicLocateConfig {
    private IHttpClient mHttpClient;
    private String mUtdid = "";

    public void enableGetPrivateID(boolean z) {
        a.a(z);
    }

    public String getAdiu() {
        return a.a();
    }

    public IHttpClient getHttpClient() {
        return this.mHttpClient;
    }

    public String getLicense() {
        return HeaderConfig.getLicense();
    }

    public String getMapkey() {
        return HeaderConfig.getMapkey();
    }

    public byte getProductId() {
        return HeaderConfig.getProductId();
    }

    public String getProductVersion() {
        return HeaderConfig.getProductVerion();
    }

    public String getUtdid() {
        return this.mUtdid;
    }

    public void setAdiu(String str) {
        a.a(str);
    }

    public void setHttpClient(IHttpClient iHttpClient) {
        this.mHttpClient = iHttpClient;
    }

    public void setLicense(String str) {
        HeaderConfig.setLicense(str);
    }

    public void setMapkey(String str) {
        HeaderConfig.setMapkey(str);
    }

    public void setProductId(byte b) {
        HeaderConfig.setProductId(b);
    }

    public void setProductVersion(String str) {
        HeaderConfig.setProductVerion(str);
    }

    public void setUtdid(String str) {
        this.mUtdid = str;
    }
}
