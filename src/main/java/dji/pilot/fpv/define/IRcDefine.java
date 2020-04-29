package dji.pilot.fpv.define;

import dji.fieldAnnotation.EXClassNullAway;
import dji.logic.utils.DJIPublicUtils;

@EXClassNullAway
public interface IRcDefine {

    public interface RcSupportVersion {
        public static final long VERSION_SUPPORT_LINK_AIRCRAFT = DJIPublicUtils.formatVersion("5.21.17.0");
        public static final long VERSION_SUPPORT_UP_CMD = DJIPublicUtils.formatVersion("5.40.0.0");
    }

    public interface RcLogFilePath {
        public static final String DIR_RC_AIRCRAFT_TYPE = "RcAircraftType";
        public static final String FILE_RC_AIRCRAFT_TYPE = "rcaircrafttype.txt";
    }

    public interface RcSPKeys {
        public static final String KEY_RCSP_CUSTOM1_VALUE = "key_rcsp_c1_value";
        public static final String KEY_RCSP_CUSTOM2_VALUE = "key_rcsp_c2_value";
    }
}
