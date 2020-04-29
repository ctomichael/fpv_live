package dji.component.accountcenter;

import org.json.JSONObject;

public class FlightInfo {
    public static final int DEVICE_TYPE_BATTERY = 11;
    public static final int DEVICE_TYPE_CAMERA = 1;
    public static final int DEVICE_TYPE_MC = 3;
    public static final int DEVICE_TYPE_NONE = 0;
    public static final int DEVICE_TYPE_RC = 14;
    private static final String STRING_ACTIVEDAY = "activeday";
    private static final String STRING_APPVERSION = "appVersion";
    private static final String STRING_DEVICENAME = "deviceName";
    private static final String STRING_DEVICETYPE = "deviceType";
    private static final String STRING_EMAIL = "email";
    private static final String STRING_EMPTY = "";
    private static final String STRING_FIRMWARE_VERSION = "firmwareVersion";
    private static final String STRING_IP = "ip";
    private static final String STRING_PRODUCTTYPE = "productType";
    private static final String STRING_SN = "sn";
    public String mActiveDay = "";
    public String mAppVersion = "";
    public String mDeviceName = "";
    public int mDeviceType = 0;
    public String mEmail = "";
    public String mFirmwareVersion = "";
    public String mIp = "";
    public String mProductType = "";
    public String mSerialNum = "";

    public static FlightInfo parseJSON(JSONObject json, FlightInfo flight) {
        if (json != null) {
            if (flight == null) {
                flight = new FlightInfo();
            }
            try {
                flight.mSerialNum = json.optString("sn", "");
                flight.mDeviceType = json.optInt("deviceType", 0);
                flight.mProductType = json.optString("productType", "");
                flight.mEmail = json.optString("email", "");
                flight.mAppVersion = json.optString("appVersion", "");
                flight.mDeviceName = json.optString("deviceName", "");
                flight.mActiveDay = json.optString("activeday", "");
                flight.mIp = json.optString("ip", "");
                flight.mFirmwareVersion = json.optString("firmwareVersion", "");
            } catch (Exception e) {
            }
        }
        return flight;
    }

    public boolean equals(Object o) {
        boolean ret = super.equals(o);
        if (ret || !(o instanceof FlightInfo)) {
            return ret;
        }
        FlightInfo flight = (FlightInfo) o;
        if (flight.mSerialNum == null || !flight.mSerialNum.equals(this.mSerialNum)) {
            return ret;
        }
        return true;
    }

    public int hashCode() {
        if (this.mSerialNum != null) {
            return this.mSerialNum.hashCode() + 527;
        }
        return 17;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("name[").append(this.mDeviceName).append(IMemberProtocol.STRING_SEPERATOR_RIGHT);
        sb.append("sn[").append(this.mSerialNum).append(IMemberProtocol.STRING_SEPERATOR_RIGHT);
        return sb.toString();
    }
}
