package dji.logic.vision;

import dji.fieldAnnotation.EXClassNullAway;
import dji.logic.utils.DJIPublicUtils;

@EXClassNullAway
public interface IVisionResDefine {

    public interface SupportVersion {
        public static final long VERSION_VISION_DEBUG_BRANCH_MAX = DJIPublicUtils.formatVersion("1.0.0.0");
        public static final long VERSION_VISION_IN2_HOMING_SENSE = DJIPublicUtils.formatVersion("1.1.0.74");
        public static final long VERSION_VISION_IN2_TRACK_AUTOFOCUS = DJIPublicUtils.formatVersion("1.1.0.75");
        public static final long VERSION_VISION_KUMAUATX_DYNAMIC_HOME = DJIPublicUtils.formatVersion("1.2.0.99");
        public static final long VERSION_VISION_KUMAUATX_FIXWING_GIMBALCTRL = DJIPublicUtils.formatVersion("1.1.0.91");
        public static final long VERSION_VISION_KUMQUATX_FIXWING = DJIPublicUtils.formatVersion("1.1.0.84");
        public static final long VERSION_VISION_KUMQUATX_PANO = DJIPublicUtils.formatVersion("1.3.0.96");
        public static final long VERSION_VISION_KUMQUAT_AR = DJIPublicUtils.formatVersion("1.1.0.54");
        public static final long VERSION_VISION_KUMQUAT_SELFIE = DJIPublicUtils.formatVersion("1.1.0.21");
        public static final long VERSION_VISION_KUMQUAT_TRACKSPEED = DJIPublicUtils.formatVersion("1.1.0.70");
        public static final long VERSION_VISION_MAVIC_PRO_QUICK_SHOT = DJIPublicUtils.formatVersion("1.2.0.96");
        public static final long VERSION_VISION_PALM_CONTROL_AWAY = DJIPublicUtils.formatVersion("1.1.1.45");
        public static final long VERSION_VISION_PAMATO_DRAWMODE = DJIPublicUtils.formatVersion("1.1.0.90");
        public static final long VERSION_VISION_POMATO_AR = DJIPublicUtils.formatVersion("1.1.0.35");
        public static final long VERSION_VISION_POMATO_DRAW = DJIPublicUtils.formatVersion("1.1.0.60");
        public static final long VERSION_VISION_POMATO_PANO = DJIPublicUtils.formatVersion("1.1.2.5");
        public static final long VERSION_VISION_POTATO_PANO = DJIPublicUtils.formatVersion("1.1.2.5");
        public static final long VERSION_VISION_SPARK_DYNAMIC_HOME = DJIPublicUtils.formatVersion("1.1.1.59");
        public static final long VERSION_VISION_SPARK_PANO_SPHERE = DJIPublicUtils.formatVersion("1.1.1.64");
        public static final long VERSION_VISION_TOMATO_NEW_NAVIGATION = DJIPublicUtils.formatVersion("1.1.0.61");
        public static final long VERSION_VISION_TOMATO_TRACKING_AVOID = DJIPublicUtils.formatVersion("1.0.4.0");
        public static final long VERSION_VISION_WM230_TRACK = DJIPublicUtils.formatVersion("1.0.1.15");
        public static final long VERSION_VISION_WM230_TRACK_SPEED = DJIPublicUtils.formatVersion("1.0.4.0");
        public static final long VERSION_VISION_WM240_HYPER_LAPSE_ADD_SEC = DJIPublicUtils.formatVersion("1.3.0.0");
        public static final long VERSION_VISION_WM240_MAX_PACKGE_VERSION = DJIPublicUtils.formatVersion("1.1.2.0");
        public static final long VERSION_VISION_WM240_MIN_PACKGE_VERSION = DJIPublicUtils.formatVersion("1.1.1.0");
        public static final long VERSION_VISION_WM240_PREPARE_QUICK_SHOT = DJIPublicUtils.formatVersion("0.0.51.2");
        public static final long VERSION_VISION_WM240_SUPPORT_PRODUCT_INFO = DJIPublicUtils.formatVersion("1.3.1.0");
    }
}
