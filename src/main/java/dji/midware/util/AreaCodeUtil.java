package dji.midware.util;

import dji.fieldAnnotation.EXClassNullAway;
import dji.logic.manager.DJIUSBWifiSwitchManager;
import dji.midware.data.config.P3.DeviceType;
import dji.midware.data.config.P3.ProductType;
import dji.midware.data.manager.P3.DJIProductManager;

@EXClassNullAway
public class AreaCodeUtil {
    public static DeviceType getGetAreaCodeDeviceType(boolean isRemote) {
        boolean isMammothRC;
        boolean isWm230RC;
        boolean isWm160RC;
        if (isRemote) {
            return DeviceType.WIFI;
        }
        ProductType pType = DJIProductManager.getInstance().getType();
        if (!pType.equals(ProductType.Mammoth) || !DJIUSBWifiSwitchManager.getInstance().isRcWifiConnected(pType)) {
            isMammothRC = false;
        } else {
            isMammothRC = true;
        }
        if (!pType.equals(ProductType.WM230) || !DJIUSBWifiSwitchManager.getInstance().isUSBAoaConnected()) {
            isWm230RC = false;
        } else {
            isWm230RC = true;
        }
        if (!pType.equals(ProductType.WM160) || !DJIUSBWifiSwitchManager.getInstance().isUSBAoaConnected()) {
            isWm160RC = false;
        } else {
            isWm160RC = true;
        }
        if (pType.equals(ProductType.KumquatX) || pType.equals(ProductType.KumquatS) || pType == ProductType.WM240 || pType == ProductType.WM245 || pType.equals(ProductType.PM420) || pType.equals(ProductType.PM420PRO) || pType.equals(ProductType.PM420PRO_RTK)) {
            return DeviceType.OFDM;
        }
        if (pType.equals(ProductType.Orange2) || pType.equals(ProductType.M200) || pType.equals(ProductType.M210) || pType.equals(ProductType.M210RTK) || pType.equals(ProductType.Pomato) || ProductType.Potato == pType || isMammothRC || ProductType.PomatoSDR == pType) {
            return DeviceType.OSD;
        }
        if (isWm230RC || isWm160RC) {
            return DeviceType.WIFI_G;
        }
        if (pType.equals(ProductType.Mammoth) || pType.equals(ProductType.WM230) || pType.equals(ProductType.WM160)) {
            return DeviceType.WIFI;
        }
        return DeviceType.OFDM;
    }

    public static DeviceType getSetAreaCodeDeviceType(boolean isRemote) {
        if (ProductType.PomatoSDR == DJIProductManager.getInstance().getType()) {
            return DeviceType.OFDM;
        }
        return getGetAreaCodeDeviceType(isRemote);
    }
}
