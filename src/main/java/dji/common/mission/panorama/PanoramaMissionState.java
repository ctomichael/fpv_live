package dji.common.mission.panorama;

import com.billy.cc.core.component.CCUtil;
import dji.common.mission.MissionState;
import dji.fieldAnnotation.EXClassNullAway;

@EXClassNullAway
public class PanoramaMissionState extends MissionState {
    public static final PanoramaMissionState DISCONNECTED = new PanoramaMissionState("DISCONNECTED");
    public static final PanoramaMissionState EXECUTING = new PanoramaMissionState("EXECUTING");
    public static final PanoramaMissionState NOT_SUPPORTED = new PanoramaMissionState("NOT_SUPPORTED");
    public static final PanoramaMissionState READY_TO_EXECUTE = new PanoramaMissionState("READY_TO_EXECUTE");
    public static final PanoramaMissionState READY_TO_SETUP = new PanoramaMissionState("READY_TO_SETUP");
    public static final PanoramaMissionState SETTING_UP = new PanoramaMissionState("SETTING_UP");
    public static final PanoramaMissionState UNKNOWN = new PanoramaMissionState(CCUtil.PROCESS_UNKNOWN);

    public PanoramaMissionState(String name) {
        super(name);
    }
}
