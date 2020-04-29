package com.amap.location.common.model;

import com.amap.location.common.util.f;
import dji.component.accountcenter.IMemberProtocol;

public class WiFi {
    public boolean connected;
    public int frequency;
    public long lastUpdateUtcMills = 0;
    public long mac;
    public int rssi = -113;
    public String ssid;
    public long timestamp;
    public int type;

    public WiFi(long j, String str, int i, int i2, long j2) {
        this.mac = j;
        this.ssid = str == null ? "" : str;
        this.rssi = i;
        this.frequency = i2;
        this.timestamp = j2;
    }

    public WiFi(long j, String str, int i, int i2, long j2, long j3, boolean z, int i3) {
        this.mac = j;
        this.ssid = str == null ? "" : str;
        this.rssi = i;
        this.frequency = i2;
        this.timestamp = j2;
        this.lastUpdateUtcMills = j3;
        this.connected = z;
        this.type = i3;
    }

    public WiFi(long j, String str, int i, int i2, long j2, boolean z) {
        this.mac = j;
        this.ssid = str == null ? "" : str;
        this.rssi = i;
        this.frequency = i2;
        this.timestamp = j2;
        this.connected = z;
    }

    public WiFi clone() {
        return new WiFi(this.mac, this.ssid, this.rssi, this.frequency, this.timestamp, this.lastUpdateUtcMills, this.connected, this.type);
    }

    public String getKey() {
        return this.connected + "#" + this.mac;
    }

    public String toString() {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("WiFi:[");
        stringBuffer.append("mac:" + f.a(this.mac) + ",");
        stringBuffer.append("ssid:" + this.ssid + ",");
        stringBuffer.append("rssi:" + this.rssi + ",");
        stringBuffer.append("freq:" + this.frequency + ",");
        stringBuffer.append("time:" + this.timestamp + ",");
        stringBuffer.append("utc:" + this.lastUpdateUtcMills + ",");
        stringBuffer.append("conn:" + this.connected + ",");
        stringBuffer.append("type:" + this.type + ",");
        stringBuffer.append(IMemberProtocol.STRING_SEPERATOR_RIGHT);
        return stringBuffer.toString();
    }
}
