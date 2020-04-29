package dji.pilot.publics.model;

public interface ICameraResMode {

    public interface ICameraFpsRes {
        public static final String FPSSTR_120 = "120fps";
        public static final String FPSSTR_180 = "180fps";
        public static final String FPSSTR_192 = "192fps";
        public static final String FPSSTR_200 = "200fps";
        public static final String FPSSTR_24 = "24fps";
        public static final String FPSSTR_240 = "240fps";
        public static final String FPSSTR_25 = "25fps";
        public static final String FPSSTR_30 = "30fps";
        public static final String FPSSTR_400 = "400fps";
        public static final String FPSSTR_48 = "48fps";
        public static final String FPSSTR_50 = "50fps";
        public static final String FPSSTR_60 = "60fps";
        public static final String FPSSTR_90 = "90fps";
        public static final String FPSSTR_96 = "96fps";
        public static final String FPSSTR_REAL_120 = "119.99fps";
        public static final String FPSSTR_REAL_24 = "23.976fps";
        public static final String FPSSTR_REAL_30 = "29.97fps";
        public static final String FPSSTR_REAL_48 = "47.95fps";
        public static final String FPSSTR_REAL_60 = "59.94fps";
        public static final int FPS_100 = 10;
        public static final int FPS_120 = 7;
        public static final int FPS_15 = 0;
        public static final int FPS_180 = 12;
        public static final int FPS_192 = 18;
        public static final int FPS_200 = 19;
        public static final int FPS_24 = 1;
        public static final int FPS_240 = 8;
        public static final int FPS_24_FULL = 13;
        public static final int FPS_25 = 2;
        public static final int FPS_30 = 3;
        public static final int FPS_30_FULL = 14;
        public static final int FPS_400 = 20;
        public static final int FPS_48 = 4;
        public static final int FPS_480 = 9;
        public static final int FPS_48_FULL = 15;
        public static final int FPS_50 = 5;
        public static final int FPS_60 = 6;
        public static final int FPS_60_FULL = 16;
        public static final int FPS_90 = 17;
        public static final int FPS_96 = 11;
    }

    public interface ICameraVideoResolutionRes {
        public static final String VRSTR_1280_720P = "1280x720";
        public static final String VRSTR_1920_1080P = "1920x1080";
        public static final String VRSTR_2704_1520P = "2704x1520";
        public static final String VRSTR_2720_1530P = "2720x1530";
        public static final String VRSTR_3840_1572P = "3840x1572";
        public static final String VRSTR_3840_2160P = "3840x2160";
        public static final String VRSTR_4096_2048P = "4096x2048";
        public static final String VRSTR_4096_2160P = "4096x2160";
        public static final String VRSTR_5280_2160P = "5280x2160";
        public static final String VRSTR_MAX = "Center";
        public static final String VRSTR_T1080P_RAW = "@T1080P(SSD RAW)";
        public static final String VRSTR_T2704_RAW = "@T2.7K(SSD RAW)";
        public static final String VRSTR_T4K_RAW = "@T4K(SSD RAW)";
        public static final String VRSTR_UNSET = "Unset";
        public static final int VR_1280_720P = 4;
        public static final int VR_1920_1080P = 10;
        public static final int VR_2704_1520P = 24;
        public static final int VR_2720_1530P = 31;
        public static final int VR_3840_1572P = 34;
        public static final int VR_3840_1920P = 14;
        public static final int VR_3840_2160P = 16;
        public static final int VR_3840_2880P = 18;
        public static final int VR_4096_2048P = 20;
        public static final int VR_4096_2160P = 22;
        public static final int VR_4608_2160P = 27;
        public static final int VR_4608_2592P = 28;
        public static final int VR_5280_2160P = 32;
        public static final int VR_INVALID = -1;
        public static final int VR_MAX = 253;
        public static final int VR_UNSET = 254;
    }
}
