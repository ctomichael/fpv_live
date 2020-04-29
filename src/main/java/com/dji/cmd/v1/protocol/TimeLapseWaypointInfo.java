package com.dji.cmd.v1.protocol;

import com.dji.cmd.v1.base.ProtocolBytesUtil;

public class TimeLapseWaypointInfo {
    public static int DATA_LENGTH = 64;
    private byte[] innerData;

    public TimeLapseWaypointInfo() {
        this.innerData = null;
        this.innerData = new byte[DATA_LENGTH];
    }

    public TimeLapseWaypointInfo(byte[] bytes) {
        this.innerData = null;
        this.innerData = new byte[bytes.length];
        System.arraycopy(bytes, 0, this.innerData, 0, bytes.length);
    }

    public static TimeLapseWaypointInfo dataFromBytes(byte[] bytes) {
        TimeLapseWaypointInfo res = new TimeLapseWaypointInfo();
        res.innerData = new byte[bytes.length];
        System.arraycopy(bytes, 0, res.innerData, 0, bytes.length);
        return res;
    }

    public byte[] getBytes() {
        byte[] res = new byte[this.innerData.length];
        System.arraycopy(this.innerData, 0, res, 0, this.innerData.length);
        return res;
    }

    public long getIndex() {
        return ProtocolBytesUtil.getLong(this.innerData, 0, 8);
    }

    public TimeLapseWaypointInfo setIndex(long index) {
        ProtocolBytesUtil.setValue(index, this.innerData, 0, 8);
        return this;
    }

    public float getPx() {
        return ProtocolBytesUtil.getFloat(this.innerData, 8, 4);
    }

    public TimeLapseWaypointInfo setPx(float px) {
        ProtocolBytesUtil.setValue(px, this.innerData, 8, 4);
        return this;
    }

    public float getPy() {
        return ProtocolBytesUtil.getFloat(this.innerData, 12, 4);
    }

    public TimeLapseWaypointInfo setPy(float py) {
        ProtocolBytesUtil.setValue(py, this.innerData, 12, 4);
        return this;
    }

    public float getPz() {
        return ProtocolBytesUtil.getFloat(this.innerData, 16, 4);
    }

    public TimeLapseWaypointInfo setPz(float pz) {
        ProtocolBytesUtil.setValue(pz, this.innerData, 16, 4);
        return this;
    }

    public float getHeight() {
        return ProtocolBytesUtil.getFloat(this.innerData, 20, 4);
    }

    public TimeLapseWaypointInfo setHeight(float height) {
        ProtocolBytesUtil.setValue(height, this.innerData, 20, 4);
        return this;
    }

    public float getGimbalPitch() {
        return ProtocolBytesUtil.getFloat(this.innerData, 24, 4);
    }

    public TimeLapseWaypointInfo setGimbalPitch(float gimbalPitch) {
        ProtocolBytesUtil.setValue(gimbalPitch, this.innerData, 24, 4);
        return this;
    }

    public float getGimbalRoll() {
        return ProtocolBytesUtil.getFloat(this.innerData, 28, 4);
    }

    public TimeLapseWaypointInfo setGimbalRoll(float gimbalRoll) {
        ProtocolBytesUtil.setValue(gimbalRoll, this.innerData, 28, 4);
        return this;
    }

    public float getGimbalYaw() {
        return ProtocolBytesUtil.getFloat(this.innerData, 32, 4);
    }

    public TimeLapseWaypointInfo setGimbalYaw(float gimbalYaw) {
        ProtocolBytesUtil.setValue(gimbalYaw, this.innerData, 32, 4);
        return this;
    }

    public double getLongitude() {
        return ProtocolBytesUtil.getDouble(this.innerData, 36, 8);
    }

    public TimeLapseWaypointInfo setLongitude(double longitude) {
        ProtocolBytesUtil.setValue(longitude, this.innerData, 36, 8);
        return this;
    }

    public double getLatitude() {
        return ProtocolBytesUtil.getDouble(this.innerData, 44, 8);
    }

    public TimeLapseWaypointInfo setLatitude(double latitude) {
        ProtocolBytesUtil.setValue(latitude, this.innerData, 44, 8);
        return this;
    }

    public float getAltitude() {
        return ProtocolBytesUtil.getFloat(this.innerData, 52, 4);
    }

    public TimeLapseWaypointInfo setAltitude(float altitude) {
        ProtocolBytesUtil.setValue(altitude, this.innerData, 52, 4);
        return this;
    }

    public long getTrajId() {
        return ProtocolBytesUtil.getLong(this.innerData, 56, 8);
    }

    public TimeLapseWaypointInfo setTrajId(long trajId) {
        ProtocolBytesUtil.setValue(trajId, this.innerData, 56, 8);
        return this;
    }

    public String toString() {
        return "px=" + getPx() + " py=" + getPy() + " pz=" + getPz() + " trajId=" + getTrajId() + "index=" + getIndex() + " lat=" + getLatitude() + "lng=" + getLongitude();
    }
}
