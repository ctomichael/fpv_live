package dji.midware.component.rc;

import android.text.TextUtils;
import dji.fieldAnnotation.EXClassNullAway;
import dji.log.DJILog;
import dji.midware.component.DJIComponentManager;
import dji.midware.data.config.P3.ProductType;
import dji.midware.upgradeComponent.DJIUpgradeProductID;
import dji.midware.util.BytesUtil;

@EXClassNullAway
public enum RcHardWareType {
    IN1_GL658A("GL658A", ProductType.Orange, DJIComponentManager.RcComponentType.Inspire),
    IN1_GL690A("GL690A", ProductType.Orange, DJIComponentManager.RcComponentType.Inspire),
    IN1_GL658B("GL658B", ProductType.Orange, DJIComponentManager.RcComponentType.Inspire),
    P3_GL300A("GL300A", ProductType.litchiX, DJIComponentManager.RcComponentType.P3P4),
    P3_GL300B("GL300B", ProductType.litchiX, DJIComponentManager.RcComponentType.P3P4),
    P4_GL300C("GL300C", ProductType.Tomato, DJIComponentManager.RcComponentType.P3P4),
    P3C_GL358wA("GL358wA", ProductType.litchiC, DJIComponentManager.RcComponentType.P3c),
    P3C_GL390wA("GL390wA", ProductType.litchiC, DJIComponentManager.RcComponentType.P3c),
    P3C_GL358wB("GL358wB", ProductType.P34K, DJIComponentManager.RcComponentType.P3w),
    LB2_GL858A("GL858A", ProductType.Grape2, DJIComponentManager.RcComponentType.LB2),
    LB2_GL890A("GL890A", ProductType.Grape2, DJIComponentManager.RcComponentType.LB2),
    P4_PV1("P4_PV1", ProductType.Pomato, DJIComponentManager.RcComponentType.P4P),
    P4_PV2("P4_PV2", ProductType.Pomato, DJIComponentManager.RcComponentType.P4P),
    P4_PVS("GL300L", ProductType.PomatoSDR, DJIComponentManager.RcComponentType.P4PSdr),
    P4_PVSK("GL300K", ProductType.PomatoSDR, DJIComponentManager.RcComponentType.P4PSdr),
    IN2_V3("IN2_V3", ProductType.Orange2, DJIComponentManager.RcComponentType.Inspire2),
    IN2_V4(DJIUpgradeProductID.IN2PRO, ProductType.Orange2, DJIComponentManager.RcComponentType.Cendence),
    POTATO("GL300I", ProductType.Potato, DJIComponentManager.RcComponentType.P4A),
    WM240("WM240RC", ProductType.WM240, DJIComponentManager.RcComponentType.WM240),
    WM240_PIGEON("WM240 RC Ver.B", ProductType.WM240, DJIComponentManager.RcComponentType.WM240),
    WM245("WM245RC", ProductType.WM245, DJIComponentManager.RcComponentType.WM245),
    WM245_PIGEON("WM245 RC Ver.B", ProductType.WM245, DJIComponentManager.RcComponentType.WM245),
    P4_RTK_3288("AG410", ProductType.PomatoRTK, DJIComponentManager.RcComponentType.P4RTK),
    P4_RTK("WM334 1860RC", ProductType.PomatoRTK, DJIComponentManager.RcComponentType.P4RTK),
    MATRICE_SDR("PM420 RC HDVT", ProductType.PM420PRO, DJIComponentManager.RcComponentType.CendenceSDR),
    WM160(DJIUpgradeProductID.WM160RC, ProductType.WM160, DJIComponentManager.RcComponentType.WM160),
    RM500("RM500 HDVT", ProductType.RM500, DJIComponentManager.RcComponentType.RM500);
    
    private static final String TAG = "RcHardWareType";
    protected String hwName;
    protected ProductType productType;
    protected DJIComponentManager.RcComponentType rcComponentType;

    private RcHardWareType(String hwName2, ProductType productType2, DJIComponentManager.RcComponentType rcComponentType2) {
        this.hwName = hwName2;
        this.productType = productType2;
        this.rcComponentType = rcComponentType2;
    }

    public static RcHardWareType getByOsdData(byte[] osdData) {
        int i = 0;
        if (osdData == null) {
            return null;
        }
        String hardwareVer = BytesUtil.getStringUTF8Offset(osdData, 1, 16);
        DJILog.d(TAG, "hardwareVer : " + hardwareVer, new Object[0]);
        if (TextUtils.isEmpty(hardwareVer)) {
            return null;
        }
        RcHardWareType[] types = values();
        int length = types.length;
        while (i < length) {
            RcHardWareType type = types[i];
            if (!hardwareVer.contains(type.hwName)) {
                i++;
            } else if (type == P4_PV1 || type == P4_PV2 || type == IN2_V3 || IN2_V4 == type || POTATO == type || P4_GL300C == type || P4_PVS == type || P4_PVSK == type || P4_RTK_3288 == type || P4_RTK == type || MATRICE_SDR == type || WM240 == type || WM240_PIGEON == type || WM245 == type || WM245_PIGEON == type || MATRICE_SDR == type || RM500 == type || WM160 == type) {
                return type;
            } else {
                return null;
            }
        }
        return null;
    }

    public static RcHardWareType getByDeviceString(String deviceInfo) {
        if (deviceInfo == null) {
            return null;
        }
        if (deviceInfo == null || deviceInfo.isEmpty()) {
            return null;
        }
        RcHardWareType[] types = values();
        int length = types.length;
        int i = 0;
        while (i < length) {
            RcHardWareType type = types[i];
            if (!deviceInfo.contains(type.hwName)) {
                i++;
            } else if (P4_RTK != type) {
                return null;
            } else {
                return type;
            }
        }
        return null;
    }
}
