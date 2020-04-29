package dji.midware.data.config.P3;

import android.support.annotation.Keep;
import dji.fieldAnnotation.EXClassNullAway;

@Keep
@EXClassNullAway
public enum DeviceType {
    WHO(0),
    CAMERA(1),
    APP(2),
    FLYC(3),
    GIMBAL(4),
    CENTER(5),
    RC(6),
    WIFI(7),
    DM368(8),
    OFDM(9, "OSD"),
    PC(10),
    BATTERY(11),
    DIGITAL(12),
    DM368_G(13, false),
    OSD(14, false),
    TRANSFORM(15),
    TRANSFORM_G(16, false),
    SINGLE(17),
    DOUBLE(18),
    FPGA(19),
    FPGA_G(20, false),
    GPS(26),
    WIFI_G(27),
    GLASS(28),
    BROADCAST(31),
    OTHER(100);
    
    private static DeviceType[] items = values();
    private int data;
    private boolean isRemote = true;
    private String name = null;

    private DeviceType(int _data) {
        this.data = _data;
    }

    private DeviceType(int _data, boolean isRemote2) {
        this.data = _data;
        this.isRemote = isRemote2;
    }

    private DeviceType(int _data, String name2) {
        this.data = _data;
        this.name = name2;
    }

    private DeviceType(int _data, String name2, boolean isRemote2) {
        this.data = _data;
        this.name = name2;
        this.isRemote = isRemote2;
    }

    public int value() {
        return this.data;
    }

    public boolean isRemote() {
        return this.isRemote;
    }

    public boolean _equals(int b) {
        return this.data == b;
    }

    public String toString() {
        if (this.name == null) {
            return super.toString();
        }
        return this.name;
    }

    public static DeviceType find(int b) {
        DeviceType result = OTHER;
        for (int i = 0; i < items.length; i++) {
            if (items[i]._equals(b)) {
                return items[i];
            }
        }
        return result;
    }

    public static boolean isGround(int device) {
        return DM368_G.value() == device || FPGA_G.value() == device || TRANSFORM_G.value() == device || RC.value() == device || WIFI_G.value() == device || OSD.value() == device;
    }

    public static boolean isGround(DeviceType device) {
        return isGround(device.value());
    }

    public static boolean isRemote(int device) {
        return device == FLYC.value() || device == DIGITAL.value() || device == FPGA.value() || device == CENTER.value() || device == DM368.value() || device == OFDM.value() || device == SINGLE.value() || device == TRANSFORM.value() || device == WIFI.value() || device == CAMERA.value() || device == GIMBAL.value() || device == BATTERY.value();
    }

    public static boolean isRemote(DeviceType device) {
        return isRemote(device.value());
    }

    public static boolean isFLYC(DeviceType type) {
        return type == FLYC;
    }

    public static boolean isCAMERA(DeviceType type) {
        return type == CAMERA;
    }

    public static boolean isDM368_G(DeviceType type) {
        return type == DM368_G;
    }

    public static boolean isFPGA_G(DeviceType type) {
        return type == FPGA_G;
    }

    public static boolean isTRANSFORM_G(DeviceType type) {
        return type == TRANSFORM_G;
    }

    public static boolean isOSD(DeviceType type) {
        return type == OSD;
    }
}
