package dji.pilot.fpv.camera;

import dji.fieldAnnotation.EXClassNullAway;

@EXClassNullAway
public interface ICameraDefine {
    public static final int AFC_CAMERA_VERSION = 9;
    public static final boolean AFC_DEFAULT_ENABLILITY = true;
    public static final int AFC_METERING = 3;
    public static final String AFC_SWITCH = "afc_switch";
    public static final int FOCUS_LONG_PRESS = 1;
    public static final int GIMBAL_CTRL_LONG_PRESS = 0;
    public static final String KEY_FPV_LONG_PRESS_CTRL = "key_fpv_long_press_ctrl";
}
