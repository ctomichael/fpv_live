package dji.midware.data.config.P3;

import android.util.SparseArray;
import dji.fieldAnnotation.EXClassNullAway;
import dji.log.DJILog;

@EXClassNullAway
public enum CmdSet {
    COMMON(0, new CmdIdCommon()),
    SPECIAL(1, new CmdIdSpecial()),
    CAMERA(2, new CmdIdCamera()),
    FLYC(3, new CmdIdFlyc()),
    GIMBAL(4, new CmdIdGimbal()),
    CENTER(5, new CmdIdCenter()),
    RC(6, new CmdIdRc()),
    WIFI(7, new CmdIdWifi()),
    DM368(8, new CmdIdDm368()),
    OSD(9, new CmdIdOsd()),
    EYE(10, new CmdIdEYE()),
    SIMULATOR(11, new CmdIdSimulator()),
    BATTERY(12),
    SMARTBATTERY(13, new CmdIdSmartBattery()),
    ADS_B(17, new CmdIdADS_B()),
    Glass(21, new CmdIdGlass()),
    Flight(31, new CmdIdFlight()),
    RTK(15, new CmdIdRTK()),
    Module4G(24, new CmdIdModule4G()),
    OnboardSDK(25, new CmdIdOnBoardSDK()),
    NarrowBand(32, new CmdIdNarrowBand()),
    FLYC2(34, new CmdIdFlyc2()),
    PayloadSDK(60, new CmdIdPayloadSDK()),
    RECOGNIZE(238, new CmdIdRecognize()),
    OTHER(100);
    
    private static final String TAG = "CmdSet";
    private static CmdSet[] cmdSets = values();
    private static SparseArray<CmdSet> sIdCmdsetSpArray = new SparseArray<>();
    private DJICmdSetBase cls;
    private int data;

    static {
        for (int i = 0; i < cmdSets.length; i++) {
            sIdCmdsetSpArray.put(cmdSets[i].value(), cmdSets[i]);
        }
    }

    private CmdSet(int _data) {
        this.data = _data;
    }

    private CmdSet(int _data, DJICmdSetBase cls2) {
        this.data = _data;
        this.cls = getExtraCmdSetInterface(cls2);
        if (this.cls == null) {
            this.cls = cls2;
        }
    }

    public int value() {
        return this.data;
    }

    public DJICmdSetBase cmdIdClass() {
        return this.cls;
    }

    public boolean _equals(int b) {
        return this.data == b;
    }

    public static CmdSet find(int b) {
        return sIdCmdsetSpArray.get(b, OTHER);
    }

    private DJICmdSetBase getExtraCmdSetInterface(DJICmdSetBase cls2) {
        if (cls2 == null) {
            return null;
        }
        String className = cls2.getClass().getSimpleName();
        try {
            Class clazz = Class.forName("com.dji.midware.extension.config." + className + "Extra");
            if (clazz != null) {
                return (DJICmdSetBase) clazz.newInstance();
            }
            return null;
        } catch (Exception e) {
            DJILog.d(TAG, "No exist extra class : " + className, new Object[0]);
            return null;
        }
    }
}
