package dji.logic.camera;

import dji.fieldAnnotation.EXClassNullAway;

@EXClassNullAway
public interface ICameraResDefine {

    public interface SupportVersion {
        public static final String SPARK_CAMERA_TRACKING_MAXIMUM_SUPPORT_VERSION = "1.1.1.50";
    }

    public interface In2ProtocolVersion {
        public static final int IN2_CAMERA_SUPPORT_AFC = 1;
        public static final int IN2_CAMERA_SUPPORT_BPC = 4;
        public static final int IN2_CAMERA_SUPPORT_CDNG_RESOLUTION_EXTEND = 5;
        public static final int IN2_CAMERA_SUPPORT_EI = 3;
        public static final int IN2_CAMERA_SUPPORT_LICENSE_INIT_STATUS = Integer.MAX_VALUE;
        public static final int IN2_CAMERA_SUPPORT_PRORES_RAW = 6;
        public static final int IN2_CAMERA_SUPPORT_SSD_EXFAT = 2;
        public static final int IN2_CAMERA_SUPPORT_SSD_EXFAT_RAW_VERSION = 3;
        public static final int IN2_CAMERA_SUPPORT_VIDEO_SYNC = Integer.MAX_VALUE;
    }
}
