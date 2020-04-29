package dji.pilot.configs;

import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.model.P3.DataCameraGetMode;
import dji.midware.data.model.P3.DataRcSetMaster;

@EXClassNullAway
public class DeviceStatusConfig {
    public static int ImageTransmitterType = 1;
    public static int bandWidth = -1;
    public static DataRcSetMaster.MODE curRcMode = DataRcSetMaster.MODE.OTHER;
    public static int mArmActionType = 0;
    public static float mAutoMcs = -1.0f;
    public static int mBwType = 0;
    public static int mChannel = -1;
    public static int mDownwardFreqType = 0;
    public static int mIsAuto = -1;
    public static int mUpwardFreqType = -1;
    public static int mcs = -1;
    public static DataCameraGetMode.MODE tmpMode = DataCameraGetMode.MODE.TAKEPHOTO;
}
