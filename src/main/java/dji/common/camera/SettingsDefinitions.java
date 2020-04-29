package dji.common.camera;

import com.drew.metadata.exif.makernotes.LeicaMakernoteDirectory;
import com.drew.metadata.exif.makernotes.SonyType6MakernoteDirectory;
import com.drew.metadata.iptc.IptcDirectory;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.model.P3.DataCameraSetPhoto;
import dji.midware.media.DJIVideoDecoder;
import dji.pilot.publics.model.ICameraResMode;
import it.sauronsoftware.ftp4j.FTPCodes;

@EXClassNullAway
public class SettingsDefinitions {

    public enum StreamQuality {
        NORMAL,
        HIGH_QUALITY
    }

    public static class CameraPhotoAEBParam {
        public int captureCount;
        public int exposureOffset;
    }

    public enum CameraType {
        DJICameraTypeFC350(0),
        DJICameraTypeFC550(1),
        DJICameraTypeFC260(2),
        DJICameraTypeFC300S(3),
        DJICameraTypeFC300X(4),
        DJICameraTypeFC550Raw(5),
        DJICameraTypeFC330X(6),
        DJICameraTypeTau640(7),
        DJICameraTypeTau336(8),
        DJICameraTypeFC220(9),
        DJICameraTypeFC300XW(10),
        DJICameraTypeCV600(11),
        DJICameraTypeFC65XXUnknown(12),
        DJICameraTypeFC6310(13),
        DJICameraTypeFC6510(14),
        DJICameraTypeFC6520(15),
        DJICameraTypeFC6532(16),
        DJICameraTypeFC6540(17),
        DJICameraTypeFC220S(18),
        DJICameraTypeFC1102(19),
        DJICameraTypeGD600(20),
        DJICameraTypeFC6310A(21),
        DJICameraTypeP3SE(22),
        DJICameraTypeFC230(23),
        DJICameraTypeFC240(25),
        DJICameraTypeFC1705(26),
        DJICameraTypeHG330(27),
        DJICameraTypeFC6310S(28),
        DJICameraTypeFC240_1(29),
        DJIPayloadCamera(31),
        DJICameraTypeFC245_IMX477(38),
        DJICameraFPV(39),
        DJICameraTypeFC2403(40),
        DJICameraTypeFC160(44),
        DJICameraTypeHasselH6D50C(166),
        DJICameraTypeHasselH6D100C(167),
        OTHER(255);
        
        private static volatile CameraType[] sValues = null;
        private final int data;

        private CameraType(int _data) {
            this.data = _data;
        }

        public int value() {
            return this.data;
        }

        private boolean _equals(int b) {
            return this.data == b;
        }

        public static CameraType find(int b) {
            if (sValues == null) {
                sValues = values();
            }
            CameraType result = OTHER;
            for (int i = 0; i < sValues.length; i++) {
                if (sValues[i]._equals(b)) {
                    return sValues[i];
                }
            }
            return result;
        }
    }

    public enum CameraMode {
        SHOOT_PHOTO(0),
        RECORD_VIDEO(1),
        PLAYBACK(2),
        MEDIA_DOWNLOAD(4),
        BROADCAST(5),
        UNKNOWN(255);
        
        private final int value;

        private CameraMode(int value2) {
            this.value = value2;
        }

        public int value() {
            return this.value;
        }

        private boolean _equals(int b) {
            return this.value == b;
        }

        public static CameraMode find(int value2) {
            CameraMode result = UNKNOWN;
            for (int i = 0; i < values().length; i++) {
                if (values()[i]._equals(value2)) {
                    return values()[i];
                }
            }
            return result;
        }
    }

    public enum ShootPhotoMode {
        SINGLE(0, DataCameraSetPhoto.TYPE.SINGLE),
        HDR(1, DataCameraSetPhoto.TYPE.HDR),
        BURST(2, DataCameraSetPhoto.TYPE.BURST),
        AEB(3, DataCameraSetPhoto.TYPE.AEB),
        INTERVAL(4, DataCameraSetPhoto.TYPE.TIME),
        TIME_LAPSE(5, DataCameraSetPhoto.TYPE.TIME),
        PANORAMA(6, DataCameraSetPhoto.TYPE.APP_FULLVIEW),
        RAW_BURST(7, DataCameraSetPhoto.TYPE.RAWBURST),
        SHALLOW_FOCUS(8, DataCameraSetPhoto.TYPE.BOKEH),
        EHDR(9, DataCameraSetPhoto.TYPE.HDR_PLUS),
        HYPER_LIGHT(10, DataCameraSetPhoto.TYPE.HYPER_NIGHT),
        HYPER_LAPSE(11, DataCameraSetPhoto.TYPE.HYPER_LAPSE),
        UNKNOWN(255, DataCameraSetPhoto.TYPE.OTHER);
        
        private final DataCameraSetPhoto.TYPE type;
        private final int value;

        private ShootPhotoMode(int value2, DataCameraSetPhoto.TYPE type2) {
            this.value = value2;
            this.type = type2;
        }

        public int value() {
            return this.value;
        }

        public int getInternalTypeValue() {
            if (this.type != null) {
                return this.type.value();
            }
            return -1;
        }

        private boolean _equals(int b) {
            return this.value == b;
        }

        public static ShootPhotoMode find(int value2) {
            ShootPhotoMode result = INTERVAL;
            for (int i = 0; i < values().length; i++) {
                if (values()[i]._equals(value2)) {
                    return values()[i];
                }
            }
            return result;
        }

        public static ShootPhotoMode find(DataCameraSetPhoto.TYPE photoType) {
            if (photoType == null) {
                return SINGLE;
            }
            ShootPhotoMode shootPhotoMode = SINGLE;
            for (int i = 0; i < values().length; i++) {
                if (values()[i].getInternalTypeValue() == photoType.value()) {
                    return values()[i];
                }
            }
            return shootPhotoMode;
        }
    }

    public enum ExposureMode {
        PROGRAM(1),
        SHUTTER_PRIORITY(2),
        APERTURE_PRIORITY(3),
        MANUAL(4),
        CINE(7),
        UNKNOWN(255);
        
        private final int value;

        private ExposureMode(int value2) {
            this.value = value2;
        }

        public int value() {
            return this.value;
        }

        public boolean _equals(int b) {
            return this.value == b;
        }

        public static ExposureMode find(int value2) {
            ExposureMode result = UNKNOWN;
            for (int i = 0; i < values().length; i++) {
                if (values()[i]._equals(value2)) {
                    return values()[i];
                }
            }
            return result;
        }
    }

    public enum ExposureState {
        NORMAL(0),
        UNDEREXPOSED(1),
        OVEREXPOSED(2),
        UNKNOWN(255);
        
        private final int value;

        private ExposureState(int value2) {
            this.value = value2;
        }

        public int value() {
            return this.value;
        }

        public boolean _equals(int b) {
            return this.value == b;
        }

        public static ExposureState find(int value2) {
            ExposureState result = UNKNOWN;
            for (int i = 0; i < values().length; i++) {
                if (values()[i]._equals(value2)) {
                    return values()[i];
                }
            }
            return result;
        }
    }

    public enum VideoFileFormat {
        MOV(0),
        MP4(1),
        TIFF_SEQ(2),
        SEQ(3),
        UNKNOWN(255);
        
        private final int value;

        private VideoFileFormat(int value2) {
            this.value = value2;
        }

        public int value() {
            return this.value;
        }

        private boolean _equals(int b) {
            return this.value == b;
        }

        public static VideoFileFormat find(int value2) {
            VideoFileFormat result = UNKNOWN;
            for (int i = 0; i < values().length; i++) {
                if (values()[i]._equals(value2)) {
                    return values()[i];
                }
            }
            return result;
        }
    }

    public enum VideoResolution {
        RESOLUTION_640x480(0, 0, PhotoAspectRatio.RATIO_4_3.value()),
        RESOLUTION_640x512(1, 26, 5),
        RESOLUTION_1280x720(2, 4, PhotoAspectRatio.RATIO_16_9.value()),
        RESOLUTION_1920x1080(3, 10, PhotoAspectRatio.RATIO_16_9.value()),
        RESOLUTION_2704x1520(4, 24, PhotoAspectRatio.RATIO_16_9.value()),
        RESOLUTION_2720x1530(5, 31, PhotoAspectRatio.RATIO_16_9.value()),
        RESOLUTION_3840x1572(6, 34, 3),
        RESOLUTION_3840x2160(7, 16, PhotoAspectRatio.RATIO_16_9.value()),
        RESOLUTION_4096x2160(8, 22, 4),
        RESOLUTION_4608x2160(9, 27, -1),
        RESOLUTION_4608x2592(10, 28, PhotoAspectRatio.RATIO_16_9.value()),
        RESOLUTION_5280x2160(11, 32, 3),
        RESOLUTION_MAX(12, ICameraResMode.ICameraVideoResolutionRes.VR_MAX, -1),
        NO_SSD_VIDEO(13, 254, -1),
        RESOLUTION_5760X3240(14, 35, PhotoAspectRatio.RATIO_16_9.value()),
        RESOLUTION_6016X3200(15, 36, 4),
        RESOLUTION_2048x1080(16, 37, 4),
        RESOLUTION_5280x2972(17, 33, PhotoAspectRatio.RATIO_16_9.value()),
        RESOLUTION_336x256(18, 38, -1),
        RESOLUTION_3712x2088(19, 16, PhotoAspectRatio.RATIO_16_9.value()),
        RESOLUTION_3944x2088(20, 22, 4),
        RESOLUTION_2688x1512(21, 45, PhotoAspectRatio.RATIO_16_9.value()),
        RESOLUTION_640x360(22, 46, PhotoAspectRatio.RATIO_16_9.value()),
        UNKNOWN(255, 255, -1);
        
        private final int cmdValue;
        private final int ratio;
        private final int value;

        private VideoResolution(int value2, int cmdValue2, int ratioIndex) {
            this.value = value2;
            this.cmdValue = cmdValue2;
            this.ratio = ratioIndex;
        }

        public int value() {
            return this.value;
        }

        public int cmdValue() {
            return this.cmdValue;
        }

        public int ratio() {
            return this.ratio;
        }

        private boolean _equals(int b) {
            return this.value == b;
        }

        public static VideoResolution find(int value2) {
            VideoResolution result = UNKNOWN;
            for (int i = 0; i < values().length; i++) {
                if (values()[i]._equals(value2)) {
                    return values()[i];
                }
            }
            return result;
        }

        private boolean equalCmdValue(int b) {
            return this.cmdValue == b;
        }

        public static VideoResolution findWithCmdValue(int value2) {
            VideoResolution result = UNKNOWN;
            for (int i = 0; i < values().length; i++) {
                if (values()[i].equalCmdValue(value2)) {
                    return values()[i];
                }
            }
            return result;
        }
    }

    public enum VideoFrameRate {
        FRAME_RATE_23_DOT_976_FPS(0, 1),
        FRAME_RATE_24_FPS(1, 13),
        FRAME_RATE_25_FPS(2, 2),
        FRAME_RATE_29_DOT_970_FPS(3, 3),
        FRAME_RATE_30_FPS(4, 14),
        FRAME_RATE_47_DOT_950_FPS(5, 4),
        FRAME_RATE_48_FPS(6, 15),
        FRAME_RATE_50_FPS(7, 5),
        FRAME_RATE_59_DOT_940_FPS(8, 6),
        FRAME_RATE_60_FPS(9, 16),
        FRAME_RATE_96_FPS(10, 11),
        FRAME_RATE_100_FPS(11, 10),
        FRAME_RATE_120_FPS(12, 7),
        FRAME_RATE_240_FPS(13, 8),
        FRAME_RATE_7_DOT_5_FPS(14, 21),
        FRAME_RATE_90_FPS(15, 17),
        FRAME_RATE_8_DOT_7_FPS(16, 23),
        UNKNOWN(255, 255);
        
        private final int cmdValue;
        private final int value;

        private VideoFrameRate(int value2, int cmdValue2) {
            this.value = value2;
            this.cmdValue = cmdValue2;
        }

        public int value() {
            return this.value;
        }

        public int cmdValue() {
            return this.cmdValue;
        }

        private boolean _equals(int b) {
            return this.value == b;
        }

        private boolean cmdValueEquals(int b) {
            return this.cmdValue == b;
        }

        public static VideoFrameRate find(int value2) {
            VideoFrameRate result = UNKNOWN;
            for (int i = 0; i < values().length; i++) {
                if (values()[i]._equals(value2)) {
                    return values()[i];
                }
            }
            return result;
        }

        public static VideoFrameRate findFrameRateWithCmdValue(int cmdValue2) {
            VideoFrameRate result = UNKNOWN;
            for (int i = 0; i < values().length; i++) {
                if (values()[i].cmdValueEquals(cmdValue2)) {
                    return values()[i];
                }
            }
            return result;
        }
    }

    public enum VideoStandard {
        PAL(0),
        NTSC(1),
        UNKNOWN(255);
        
        private final int value;

        private VideoStandard(int value2) {
            this.value = value2;
        }

        public int value() {
            return this.value;
        }

        private boolean _equals(int b) {
            return this.value == b;
        }

        public static VideoStandard find(int value2) {
            VideoStandard result = UNKNOWN;
            for (int i = 0; i < values().length; i++) {
                if (values()[i]._equals(value2)) {
                    return values()[i];
                }
            }
            return result;
        }
    }

    public enum PhotoFileFormat {
        RAW(0),
        JPEG(1),
        RAW_AND_JPEG(2),
        TIFF_8_BIT(3),
        TIFF_14_BIT(4),
        TIFF_14_BIT_LINEAR_LOW_TEMP_RESOLUTION(5),
        TIFF_14_BIT_LINEAR_HIGH_TEMP_RESOLUTION(6),
        RADIOMETRIC_JPEG(7),
        RADIOMETRIC_JPEG_LOW(8),
        RADIOMETRIC_JPEG_HIGH(9),
        UNKNOWN(255);
        
        private final int value;

        private PhotoFileFormat(int value2) {
            this.value = value2;
        }

        public int value() {
            return this.value;
        }

        private boolean _equals(int b) {
            return this.value == b;
        }

        public static PhotoFileFormat find(int value2) {
            PhotoFileFormat result = UNKNOWN;
            for (int i = 0; i < values().length; i++) {
                if (values()[i]._equals(value2)) {
                    return values()[i];
                }
            }
            return result;
        }
    }

    public enum PhotoTimeLapseFileFormat {
        VIDEO(0),
        JPEG_AND_VIDEO(1),
        UNKNOWN(255);
        
        private final int value;

        private PhotoTimeLapseFileFormat(int value2) {
            this.value = value2;
        }

        public int value() {
            return this.value;
        }

        private boolean _equals(int b) {
            return this.value == b;
        }

        public static PhotoTimeLapseFileFormat find(int value2) {
            PhotoTimeLapseFileFormat result = UNKNOWN;
            for (int i = 0; i < values().length; i++) {
                if (values()[i]._equals(value2)) {
                    return values()[i];
                }
            }
            return result;
        }
    }

    public enum PhotoAspectRatio {
        RATIO_4_3(0, 0),
        RATIO_16_9(1, 1),
        RATIO_3_2(2, 2),
        UNKNOWN(255, 6);
        
        private final int cmdValue;
        private final int ordinalValue;

        private PhotoAspectRatio(int ordinalValue2, int cmdValue2) {
            this.ordinalValue = ordinalValue2;
            this.cmdValue = cmdValue2;
        }

        public int value() {
            return this.cmdValue;
        }

        private boolean _equals(int b) {
            return this.cmdValue == b;
        }

        public static PhotoAspectRatio find(int value) {
            PhotoAspectRatio result = UNKNOWN;
            for (int i = 0; i < values().length; i++) {
                if (values()[i]._equals(value)) {
                    return values()[i];
                }
            }
            return result;
        }
    }

    public enum PhotoBurstCount {
        BURST_COUNT_2(2),
        BURST_COUNT_3(3),
        BURST_COUNT_5(5),
        BURST_COUNT_7(7),
        BURST_COUNT_10(10),
        BURST_COUNT_14(14),
        CONTINUOUS(255),
        UNKNOWN(241);
        
        private final int value;

        private PhotoBurstCount(int value2) {
            this.value = value2;
        }

        public int value() {
            return this.value;
        }

        private boolean _equals(int b) {
            return this.value == b;
        }

        public static PhotoBurstCount find(int value2) {
            PhotoBurstCount result = UNKNOWN;
            for (int i = 0; i < values().length; i++) {
                if (values()[i]._equals(value2)) {
                    return values()[i];
                }
            }
            return result;
        }
    }

    public enum PhotoAEBCount {
        AEB_COUNT_3(3),
        AEB_COUNT_5(5),
        AEB_COUNT_7(7),
        UNKNOWN(255);
        
        private final int value;

        private PhotoAEBCount(int value2) {
            this.value = value2;
        }

        public int value() {
            return this.value;
        }

        private boolean _equals(int b) {
            return this.value == b;
        }

        public static PhotoAEBCount find(int value2) {
            PhotoAEBCount result = UNKNOWN;
            for (int i = 0; i < values().length; i++) {
                if (values()[i]._equals(value2)) {
                    return values()[i];
                }
            }
            return result;
        }
    }

    public static class PhotoTimeIntervalSettings {
        private final int captureCount;
        private final int timeIntervalInSeconds;

        public PhotoTimeIntervalSettings(int captureCount2, int timeIntervalInSeconds2) {
            this.captureCount = captureCount2;
            this.timeIntervalInSeconds = timeIntervalInSeconds2;
        }

        public int getCaptureCount() {
            return this.captureCount;
        }

        public int getTimeIntervalInSeconds() {
            return this.timeIntervalInSeconds;
        }

        public String toString() {
            return this.captureCount + "," + this.timeIntervalInSeconds;
        }

        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || !(o instanceof PhotoTimeIntervalSettings)) {
                return false;
            }
            PhotoTimeIntervalSettings that = (PhotoTimeIntervalSettings) o;
            if (this.captureCount != that.captureCount) {
                return false;
            }
            if (this.timeIntervalInSeconds != that.timeIntervalInSeconds) {
                return false;
            }
            return true;
        }

        public int hashCode() {
            return (this.captureCount * 31) + this.timeIntervalInSeconds;
        }
    }

    public enum ShutterSpeed {
        SHUTTER_SPEED_1_8000(1.25E-4f),
        SHUTTER_SPEED_1_6400(1.5625E-4f),
        SHUTTER_SPEED_1_6000(1.6666666E-4f),
        SHUTTER_SPEED_1_5000(2.0E-4f),
        SHUTTER_SPEED_1_4000(2.5E-4f),
        SHUTTER_SPEED_1_3200(3.125E-4f),
        SHUTTER_SPEED_1_3000(3.3333333E-4f),
        SHUTTER_SPEED_1_2500(4.0E-4f),
        SHUTTER_SPEED_1_2000(5.0E-4f),
        SHUTTER_SPEED_1_1600(6.25E-4f),
        SHUTTER_SPEED_1_1500(6.6666666E-4f),
        SHUTTER_SPEED_1_1250(8.0E-4f),
        SHUTTER_SPEED_1_1000(0.001f),
        SHUTTER_SPEED_1_800(0.00125f),
        SHUTTER_SPEED_1_725(0.0013793104f),
        SHUTTER_SPEED_1_640(0.0015625f),
        SHUTTER_SPEED_1_500(0.002f),
        SHUTTER_SPEED_1_400(0.0025f),
        SHUTTER_SPEED_1_350(0.0028571428f),
        SHUTTER_SPEED_1_320(0.003125f),
        SHUTTER_SPEED_1_250(0.004f),
        SHUTTER_SPEED_1_240(0.004166667f),
        SHUTTER_SPEED_1_200(0.005f),
        SHUTTER_SPEED_1_180(0.0055555557f),
        SHUTTER_SPEED_1_160(0.00625f),
        SHUTTER_SPEED_1_125(0.008f),
        SHUTTER_SPEED_1_120(0.008333334f),
        SHUTTER_SPEED_1_100(0.01f),
        SHUTTER_SPEED_1_90(0.011111111f),
        SHUTTER_SPEED_1_80(0.0125f),
        SHUTTER_SPEED_1_60(0.016666668f),
        SHUTTER_SPEED_1_50(0.02f),
        SHUTTER_SPEED_1_40(0.025f),
        SHUTTER_SPEED_1_30(0.033333335f),
        SHUTTER_SPEED_1_25(0.04f),
        SHUTTER_SPEED_1_20(0.05f),
        SHUTTER_SPEED_1_15(0.06666667f),
        SHUTTER_SPEED_1_12_DOT_5(0.08f),
        SHUTTER_SPEED_1_10(0.1f),
        SHUTTER_SPEED_1_8(0.125f),
        SHUTTER_SPEED_1_6_DOT_25(0.16f),
        SHUTTER_SPEED_1_5(0.2f),
        SHUTTER_SPEED_1_4(0.25f),
        SHUTTER_SPEED_1_3(0.33333334f),
        SHUTTER_SPEED_1_2_DOT_5(0.4f),
        SHUTTER_SPEED_1_2(0.5f),
        SHUTTER_SPEED_1_1_DOT_67(0.5988024f),
        SHUTTER_SPEED_1_1_DOT_25(0.8f),
        SHUTTER_SPEED_1(1.0f),
        SHUTTER_SPEED_1_DOT_3(1.3f),
        SHUTTER_SPEED_1_DOT_6(1.6f),
        SHUTTER_SPEED_2(2.0f),
        SHUTTER_SPEED_2_DOT_5(2.5f),
        SHUTTER_SPEED_3(3.0f),
        SHUTTER_SPEED_3_DOT_2(3.2f),
        SHUTTER_SPEED_4(4.0f),
        SHUTTER_SPEED_5(5.0f),
        SHUTTER_SPEED_6(6.0f),
        SHUTTER_SPEED_7(7.0f),
        SHUTTER_SPEED_8(8.0f),
        SHUTTER_SPEED_9(9.0f),
        SHUTTER_SPEED_10(10.0f),
        SHUTTER_SPEED_13(13.0f),
        SHUTTER_SPEED_15(15.0f),
        SHUTTER_SPEED_20(20.0f),
        SHUTTER_SPEED_25(25.0f),
        SHUTTER_SPEED_30(30.0f),
        UNKNOWN(255.0f);
        
        private final float shutterSpeed;

        private ShutterSpeed(float shutter) {
            this.shutterSpeed = shutter;
        }

        public float value() {
            return this.shutterSpeed;
        }

        public static ShutterSpeed find(float shutter) {
            ShutterSpeed[] values = values();
            for (ShutterSpeed value : values) {
                if (value.shutterSpeed == shutter) {
                    return value;
                }
            }
            return null;
        }
    }

    public enum ISO {
        AUTO(0),
        ISO_100(3),
        ISO_200(4),
        ISO_400(5),
        ISO_800(6),
        ISO_1600(7),
        ISO_3200(8),
        ISO_6400(9),
        ISO_12800(10),
        ISO_25600(11),
        FIXED(255),
        UNKNOWN(65535);
        
        private final int value;

        private ISO(int value2) {
            this.value = value2;
        }

        public int value() {
            return this.value;
        }

        private boolean _equals(int b) {
            return this.value == b;
        }

        public static ISO find(int value2) {
            ISO result = UNKNOWN;
            for (int i = 0; i < values().length; i++) {
                if (values()[i]._equals(value2)) {
                    return values()[i];
                }
            }
            return result;
        }
    }

    public enum Aperture {
        F_1_DOT_6(160),
        F_1_DOT_7(170),
        F_1_DOT_8(180),
        F_2(200),
        F_2_DOT_2(FTPCodes.SERVICE_READY_FOR_NEW_USER),
        F_2_DOT_4(240),
        F_2_DOT_5(250),
        F_2_DOT_6(260),
        F_2_DOT_8(280),
        F_3_DOT_2(320),
        F_3_DOT_4(340),
        F_3_DOT_5(FTPCodes.PENDING_FURTHER_INFORMATION),
        F_4(400),
        F_4_DOT_5(FTPCodes.FILE_ACTION_NOT_TAKEN),
        F_4_DOT_8(480),
        F_5(500),
        F_5_DOT_6(560),
        F_6_DOT_3(IptcDirectory.TAG_CONTACT),
        F_6_DOT_8(680),
        F_7_DOT_1(710),
        F_8(LeicaMakernoteDirectory.TAG_CAMERA_TEMPERATURE),
        F_9(900),
        F_9_DOT_6(960),
        F_10(1000),
        F_11(1100),
        F_13(SonyType6MakernoteDirectory.TAG_MAKERNOTE_THUMB_LENGTH),
        F_14(1400),
        F_16(1600),
        F_18(1800),
        F_19(1900),
        F_20(DJIVideoDecoder.connectLosedelay),
        F_22(2200),
        UNKNOWN(255);
        
        private final int value;

        private Aperture(int value2) {
            this.value = value2;
        }

        public int value() {
            return this.value;
        }

        private boolean _equals(int b) {
            return this.value == b;
        }

        public static Aperture find(int value2) {
            Aperture result = UNKNOWN;
            for (int i = 0; i < values().length; i++) {
                if (values()[i]._equals(value2)) {
                    return values()[i];
                }
            }
            return result;
        }
    }

    public enum WhiteBalancePreset {
        AUTO(0),
        SUNNY(1),
        CLOUDY(2),
        WATER_SURFACE(3),
        INDOOR_INCANDESCENT(4),
        INDOOR_FLUORESCENT(5),
        CUSTOM(6),
        PRESET_NEUTRAL(7),
        UNKNOWN(255);
        
        private final int value;

        private WhiteBalancePreset(int value2) {
            this.value = value2;
        }

        public int value() {
            return this.value;
        }

        private boolean _equals(int b) {
            return this.value == b;
        }

        public static WhiteBalancePreset find(int value2) {
            WhiteBalancePreset result = UNKNOWN;
            for (int i = 0; i < values().length; i++) {
                if (values()[i]._equals(value2)) {
                    return values()[i];
                }
            }
            return result;
        }
    }

    public enum MeteringMode {
        CENTER(0),
        AVERAGE(1),
        SPOT(2),
        UNKNOWN(255);
        
        private final int value;

        private MeteringMode(int value2) {
            this.value = value2;
        }

        public int value() {
            return this.value;
        }

        private boolean _equals(int b) {
            return this.value == b;
        }

        public static MeteringMode find(int value2) {
            MeteringMode result = UNKNOWN;
            for (int i = 0; i < values().length; i++) {
                if (values()[i]._equals(value2)) {
                    return values()[i];
                }
            }
            return result;
        }
    }

    public enum ExposureCompensation {
        N_5_0(1),
        N_4_7(2),
        N_4_3(3),
        N_4_0(4),
        N_3_7(5),
        N_3_3(6),
        N_3_0(7),
        N_2_7(8),
        N_2_3(9),
        N_2_0(10),
        N_1_7(11),
        N_1_3(12),
        N_1_0(13),
        N_0_7(14),
        N_0_3(15),
        N_0_0(16),
        P_0_3(17),
        P_0_7(18),
        P_1_0(19),
        P_1_3(20),
        P_1_7(21),
        P_2_0(22),
        P_2_3(23),
        P_2_7(24),
        P_3_0(25),
        P_3_3(26),
        P_3_7(27),
        P_4_0(28),
        P_4_3(29),
        P_4_7(30),
        P_5_0(31),
        FIXED(255),
        UNKNOWN(65535);
        
        private final int value;

        private ExposureCompensation(int value2) {
            this.value = value2;
        }

        public int value() {
            return this.value;
        }

        private boolean _equals(int b) {
            return this.value == b;
        }

        public static ExposureCompensation find(int value2) {
            ExposureCompensation result = UNKNOWN;
            for (int i = 0; i < values().length; i++) {
                if (values()[i]._equals(value2)) {
                    return values()[i];
                }
            }
            return result;
        }
    }

    public enum AntiFlickerFrequency {
        AUTO(0),
        MANUAL_60HZ(1),
        MANUAL_50HZ(2),
        DISABLED(3),
        UNKNOWN(255);
        
        private final int value;

        private AntiFlickerFrequency(int value2) {
            this.value = value2;
        }

        public int value() {
            return this.value;
        }

        private boolean _equals(int b) {
            return this.value == b;
        }

        public static AntiFlickerFrequency find(int value2) {
            AntiFlickerFrequency result = UNKNOWN;
            for (int i = 0; i < values().length; i++) {
                if (values()[i]._equals(value2)) {
                    return values()[i];
                }
            }
            return result;
        }
    }

    public enum PictureStylePresetType {
        STANDARD(0),
        LANDSCAPE(1),
        SOFT(2),
        CUSTOM(3),
        UNKNOWN(255);
        
        private int value;

        private PictureStylePresetType(int value2) {
            this.value = value2;
        }

        public int value() {
            return this.value;
        }

        private boolean _equals(int b) {
            return this.value == b;
        }

        public static PictureStylePresetType find(int value2) {
            PictureStylePresetType result = UNKNOWN;
            for (int i = 0; i < values().length; i++) {
                if (values()[i]._equals(value2)) {
                    return values()[i];
                }
            }
            return result;
        }
    }

    public static class PictureStylePreset {
        private final int contrast;
        private final int saturation;
        private final int sharpness;

        public int getSaturation() {
            return this.saturation;
        }

        public int getContrast() {
            return this.contrast;
        }

        public int getSharpness() {
            return this.sharpness;
        }

        public PictureStylePresetType presetType() {
            PictureStylePresetType type = PictureStylePresetType.CUSTOM;
            if (this.contrast == 0 && this.sharpness == 0 && this.saturation == 0) {
                return PictureStylePresetType.STANDARD;
            }
            if (this.contrast == 1 && this.sharpness == 1 && this.saturation == 0) {
                return PictureStylePresetType.LANDSCAPE;
            }
            if (this.contrast == 0 && this.sharpness == -1 && this.saturation == 0) {
                return PictureStylePresetType.SOFT;
            }
            return type;
        }

        private PictureStylePreset(Builder builder) {
            this.saturation = builder.saturation;
            this.sharpness = builder.sharpness;
            this.contrast = builder.contrast;
        }

        public int hashCode() {
            return (((this.sharpness * 31) + this.saturation) * 31) + this.contrast;
        }

        public boolean equals(Object o) {
            if (o == null || !(o instanceof PictureStylePreset)) {
                return false;
            }
            if (this == o) {
                return true;
            }
            return this.sharpness == ((PictureStylePreset) o).sharpness && this.contrast == ((PictureStylePreset) o).contrast && this.saturation == ((PictureStylePreset) o).saturation;
        }

        public static final class Builder {
            /* access modifiers changed from: private */
            public int contrast;
            /* access modifiers changed from: private */
            public int saturation;
            /* access modifiers changed from: private */
            public int sharpness;

            public Builder sharpness(int sharpness2) {
                this.sharpness = sharpness2;
                return this;
            }

            public Builder saturation(int saturation2) {
                this.saturation = saturation2;
                return this;
            }

            public Builder contrast(int contrast2) {
                this.contrast = contrast2;
                return this;
            }

            public PictureStylePreset build() {
                return new PictureStylePreset(this);
            }
        }
    }

    public enum FocusMode {
        MANUAL(0),
        AUTO(1),
        AFC(2),
        UNKNOWN(255);
        
        private final int value;

        private FocusMode(int value2) {
            this.value = value2;
        }

        public int value() {
            return this.value;
        }

        private boolean _equals(int b) {
            return this.value == b;
        }

        public static FocusMode find(int value2) {
            FocusMode result = UNKNOWN;
            for (int i = 0; i < values().length; i++) {
                if (values()[i]._equals(value2)) {
                    return values()[i];
                }
            }
            return result;
        }
    }

    public enum FileIndexMode {
        RESET(0),
        SEQUENCE(1),
        UNKNOWN(255);
        
        private final int value;

        private FileIndexMode(int value2) {
            this.value = value2;
        }

        public int value() {
            return this.value;
        }

        private boolean _equals(int b) {
            return this.value == b;
        }

        public static FileIndexMode find(int value2) {
            FileIndexMode result = UNKNOWN;
            for (int i = 0; i < values().length; i++) {
                if (values()[i]._equals(value2)) {
                    return values()[i];
                }
            }
            return result;
        }
    }

    public enum CustomSettingsProfile {
        DEFAULT(0),
        PROFILE_1(1),
        PROFILE_2(2),
        PROFILE_3(3),
        PROFILE_4(4),
        UNKNOWN(255);
        
        private final int value;

        private CustomSettingsProfile(int value2) {
            this.value = value2;
        }

        public int value() {
            return this.value;
        }

        private boolean _equals(int b) {
            return this.value == b;
        }

        public static CustomSettingsProfile find(int value2) {
            CustomSettingsProfile result = UNKNOWN;
            for (int i = 0; i < values().length; i++) {
                if (values()[i]._equals(value2)) {
                    return values()[i];
                }
            }
            return result;
        }
    }

    public enum SSDLegacyColor {
        NONE(0),
        D_CINELIKE(44),
        D_LOG(45),
        D_COLOR_1(46),
        D_Color_2(47),
        D_COLOR_3(48),
        UNKNOWN(255);
        
        private final int value;

        private SSDLegacyColor(int value2) {
            this.value = value2;
        }

        public int value() {
            return this.value;
        }

        private boolean _equals(int b) {
            return this.value == b;
        }

        public static SSDLegacyColor find(int value2) {
            SSDLegacyColor result = UNKNOWN;
            for (int i = 0; i < values().length; i++) {
                if (values()[i]._equals(value2)) {
                    return values()[i];
                }
            }
            return result;
        }
    }

    public enum CameraColor {
        NONE(0),
        ART(1),
        REMINISCENCE(2),
        INVERSE(3),
        BLACK_AND_WHITE(4),
        BRIGHT(5),
        D_CINELIKE(6),
        PORTRAIT(7),
        M_31(14),
        DELTA(15),
        K_DX(16),
        DK79(17),
        PRISMO(18),
        JUGO(19),
        VISION_4(20),
        VISION_6(21),
        TRUE_COLOR(22),
        D_LOG(23),
        SOLARIZE(38),
        POSTERIZE(39),
        WHITEBOARD(40),
        BLACKBOARD(41),
        AQUA(42),
        TRUE_COLOR_EXT(43),
        FILM_A(45),
        FILM_B(46),
        FILM_C(47),
        FILM_D(48),
        FILM_E(49),
        FILM_F(50),
        FILM_G(51),
        FILM_H(52),
        FILM_I(53),
        HLG(54),
        UNKNOWN(255);
        
        private final int value;

        private CameraColor(int value2) {
            this.value = value2;
        }

        public int value() {
            return this.value;
        }

        private boolean _equals(int b) {
            return this.value == b;
        }

        public static CameraColor find(int value2) {
            CameraColor result = UNKNOWN;
            for (int i = 0; i < values().length; i++) {
                if (values()[i]._equals(value2)) {
                    return values()[i];
                }
            }
            return result;
        }
    }

    public enum PlaybackMode {
        SINGLE_PHOTO_PREVIEW(0),
        SINGLE_VIDEO_PLAYBACK_START(2),
        SINGLE_VIDEO_PLAYBACK_PAUSED(3),
        MULTIPLE_FILES_EDIT(4),
        MULTIPLE_MEDIA_FILE_PREVIEW(5),
        MEDIA_FILE_DOWNLOAD(6),
        UNKNOWN(255);
        
        private final int value;

        private PlaybackMode(int value2) {
            this.value = value2;
        }

        public int value() {
            return this.value;
        }

        private boolean _equals(int b) {
            return this.value == b;
        }

        public static PlaybackMode find(int value2) {
            PlaybackMode result = UNKNOWN;
            for (int i = 0; i < values().length; i++) {
                if (values()[i]._equals(value2)) {
                    return values()[i];
                }
            }
            return result;
        }
    }

    public enum FileType {
        JPEG(0),
        DNG(1),
        VIDEO(2),
        UNKNOWN(255);
        
        private final int value;

        private FileType(int value2) {
            this.value = value2;
        }

        public int value() {
            return this.value;
        }

        private boolean _equals(int b) {
            return this.value == b;
        }

        public static FileType find(int value2) {
            FileType result = UNKNOWN;
            for (int i = 0; i < values().length; i++) {
                if (values()[i]._equals(value2)) {
                    return values()[i];
                }
            }
            return result;
        }
    }

    public enum PlaybackDeletionState {
        NONE(0),
        FAILED(1),
        DELETING(2),
        SUCCESSFUL(3),
        UNKNOWN(255);
        
        private final int value;

        private PlaybackDeletionState(int value2) {
            this.value = value2;
        }

        public int value() {
            return this.value;
        }

        private boolean _equals(int b) {
            return this.value == b;
        }

        public static PlaybackDeletionState find(int value2) {
            PlaybackDeletionState result = UNKNOWN;
            for (int i = 0; i < values().length; i++) {
                if (values()[i]._equals(value2)) {
                    return values()[i];
                }
            }
            return result;
        }
    }

    public enum FocusStatus {
        IDLE(0),
        FOCUSING(1),
        SUCCESSFUL(2),
        FAILED(3),
        UNKNOWN(255);
        
        private final int value;

        private FocusStatus(int value2) {
            this.value = value2;
        }

        public int value() {
            return this.value;
        }

        private boolean _equals(int b) {
            return this.value == b;
        }

        public static FocusStatus find(int value2) {
            FocusStatus result = UNKNOWN;
            for (int i = 0; i < values().length; i++) {
                if (values()[i]._equals(value2)) {
                    return values()[i];
                }
            }
            return result;
        }
    }

    public enum ThermalROI {
        FULL(0, 0),
        SKY_EXCLUDED_33(1, 1),
        SKY_EXCLUDED_50(2, 2),
        UNKNOWN(255, 100);
        
        private final int innerValue;
        private final int value;

        private ThermalROI(int value2, int innerValue2) {
            this.value = value2;
            this.innerValue = innerValue2;
        }

        public int value() {
            return this.value;
        }

        public int getInnerValue() {
            return this.innerValue;
        }

        private boolean _equals(int b) {
            return this.value == b;
        }

        private boolean _innerEquals(int b) {
            return this.innerValue == b;
        }

        public static ThermalROI find(int value2) {
            ThermalROI result = UNKNOWN;
            for (int i = 0; i < values().length; i++) {
                if (values()[i]._equals(value2)) {
                    return values()[i];
                }
            }
            return result;
        }

        public static ThermalROI innerFind(int innerValue2) {
            ThermalROI result = UNKNOWN;
            for (int i = 0; i < values().length; i++) {
                if (values()[i]._innerEquals(innerValue2)) {
                    return values()[i];
                }
            }
            return result;
        }
    }

    public enum ThermalPalette {
        WHITE_HOT(0, 24),
        BLACK_HOT(1, 25),
        FUSION(2, 26),
        RAINBOW(3, 27),
        IRONBOW_1(4, 28),
        ICE_FIRE(5, 29),
        SEPIA(6, 30),
        GLOWBOW(7, 31),
        IRONBOW_2(8, 32),
        COLOR_1(9, 33),
        COLOR_2(10, 34),
        RAIN(11, 35),
        RED_HOT(12, 36),
        GREEN_HOT(13, 37),
        ARCTIC(14, 49),
        HOT_SPOT(15, 53),
        RAINBOW2(16, 54),
        GRAY(17, 55),
        HOT_METAL(18, 56),
        COLD_SPOT(19, 57),
        UNKNOWN(255, 255);
        
        private final int innerValue;
        private final int value;

        private ThermalPalette(int value2, int iValue) {
            this.value = value2;
            this.innerValue = iValue;
        }

        public int value() {
            return this.value;
        }

        public int getInnerValue() {
            return this.innerValue;
        }

        private boolean _innerEquals(int b) {
            return this.innerValue == b;
        }

        private boolean _equals(int b) {
            return this.value == b;
        }

        public static ThermalPalette innerFind(int innerValue2) {
            ThermalPalette result = UNKNOWN;
            for (int i = 0; i < values().length; i++) {
                if (values()[i]._innerEquals(innerValue2)) {
                    return values()[i];
                }
            }
            return result;
        }

        public static ThermalPalette find(int value2) {
            ThermalPalette result = UNKNOWN;
            for (int i = 0; i < values().length; i++) {
                if (values()[i]._equals(value2)) {
                    return values()[i];
                }
            }
            return result;
        }
    }

    public enum ThermalScene {
        LINEAR(0, 0),
        DEFAULT(1, 1),
        SEA_SKY(2, 2),
        OUTDOOR(3, 3),
        INDOOR(4, 4),
        MANUAL(5, 5),
        PROFILE_1(6, 6),
        PROFILE_2(7, 7),
        PROFILE_3(8, 8),
        UNKNOWN(255, 100);
        
        private final int innerValue;
        private final int value;

        private ThermalScene(int value2, int innerValue2) {
            this.value = value2;
            this.innerValue = innerValue2;
        }

        public int value() {
            return this.value;
        }

        public int getInnerValue() {
            return this.innerValue;
        }

        private boolean _equals(int b) {
            return this.value == b;
        }

        private boolean _innerEquals(int b) {
            return this.innerValue == b;
        }

        public static ThermalScene find(int value2) {
            ThermalScene result = UNKNOWN;
            for (int i = 0; i < values().length; i++) {
                if (values()[i]._equals(value2)) {
                    return values()[i];
                }
            }
            return result;
        }

        public static ThermalScene innerFind(int innerValue2) {
            ThermalScene result = UNKNOWN;
            for (int i = 0; i < values().length; i++) {
                if (values()[i]._innerEquals(innerValue2)) {
                    return values()[i];
                }
            }
            return result;
        }
    }

    public enum ThermalIsothermUnit {
        PERCENTAGE(0),
        CELSIUS(1),
        UNKNOWN(255);
        
        private final int value;

        private ThermalIsothermUnit(int value2) {
            this.value = value2;
        }

        public int value() {
            return this.value;
        }

        private boolean _equals(int b) {
            return this.value == b;
        }
    }

    public enum ThermalGainMode {
        AUTO(0),
        LOW(1),
        HIGH(2),
        UNKNOWN(100);
        
        private final int value;

        private ThermalGainMode(int value2) {
            this.value = value2;
        }

        public int value() {
            return this.value;
        }

        private boolean _equals(int b) {
            return this.value == b;
        }

        public static ThermalGainMode find(int value2) {
            ThermalGainMode result = UNKNOWN;
            for (int i = 0; i < values().length; i++) {
                if (values()[i]._equals(value2)) {
                    return values()[i];
                }
            }
            return result;
        }
    }

    public enum ThermalResolution {
        RESOLUTION_336x256(0),
        RESOLUTION_640x512(1),
        UNKNOWN(255);
        
        private final int value;

        private ThermalResolution(int value2) {
            this.value = value2;
        }

        public int value() {
            return this.value;
        }

        private boolean _equals(int b) {
            return this.value == b;
        }
    }

    public enum ThermalFrameRateUpperBound {
        UPPER_BOUND_8_DOT_3_HZ(0),
        UPPER_BOUND_30_HZ(4),
        UNKNOWN(255);
        
        private final int value;

        private ThermalFrameRateUpperBound(int value2) {
            this.value = value2;
        }

        public int value() {
            return this.value;
        }

        private boolean _equals(int b) {
            return this.value == b;
        }

        public static ThermalFrameRateUpperBound find(int value2) {
            ThermalFrameRateUpperBound result = UNKNOWN;
            for (int i = 0; i < values().length; i++) {
                if (values()[i]._equals(value2)) {
                    return values()[i];
                }
            }
            return result;
        }
    }

    public enum ThermalLensFocalLength {
        LENGTH_6_DOT_8_MM(0),
        LENGTH_7_DOT_5_MM(1),
        LENGTH_9_MM(2),
        LENGTH_13_MM(3),
        LENGTH_19_MM(4),
        LENGTH_25_MM(5),
        UNKNOWN(255);
        
        private final int value;

        private ThermalLensFocalLength(int value2) {
            this.value = value2;
        }

        public int value() {
            return this.value;
        }

        private boolean _equals(int b) {
            return this.value == b;
        }
    }

    public enum ThermalDigitalZoomFactor {
        X_1(0),
        X_2(1),
        X_4(2),
        X_8(3),
        UNKNOWN(255);
        
        private final int value;

        private ThermalDigitalZoomFactor(int value2) {
            this.value = value2;
        }

        public int value() {
            return this.value;
        }

        private boolean _equals(int b) {
            return this.value == b;
        }
    }

    public static class OpticalZoomSpec {
        private final int focalLengthStep;
        private final int maxFocalLength;
        private final int minFocalLength;

        public OpticalZoomSpec(int maxFocalLength2, int minFocalLength2, int focalLengthStep2) {
            this.maxFocalLength = maxFocalLength2;
            this.minFocalLength = minFocalLength2;
            this.focalLengthStep = focalLengthStep2;
        }

        public int getMaxFocalLength() {
            return this.maxFocalLength;
        }

        public int getMinFocalLength() {
            return this.minFocalLength;
        }

        public int getFocalLengthStep() {
            return this.focalLengthStep;
        }

        public int hashCode() {
            return (((this.maxFocalLength * 31) + this.minFocalLength) * 31) + this.focalLengthStep;
        }

        public boolean equals(Object o) {
            if (o == null || !(o instanceof OpticalZoomSpec)) {
                return false;
            }
            if (this == o) {
                return true;
            }
            return this.maxFocalLength == ((OpticalZoomSpec) o).maxFocalLength && this.minFocalLength == ((OpticalZoomSpec) o).minFocalLength && this.focalLengthStep == ((OpticalZoomSpec) o).focalLengthStep;
        }
    }

    public enum ZoomSpeed {
        SLOWEST(72),
        SLOW(73),
        MODERATELY_SLOW(74),
        NORMAL(75),
        MODERATELY_FAST(76),
        FAST(77),
        FASTEST(78);
        
        private final int value;

        private ZoomSpeed(int value2) {
            this.value = value2;
        }

        public int value() {
            return this.value;
        }

        private boolean _equals(int b) {
            return this.value == b;
        }
    }

    public enum ZoomDirection {
        ZOOM_OUT(0),
        ZOOM_IN(1),
        UNKNOWN(255);
        
        private final int value;

        private ZoomDirection(int value2) {
            this.value = value2;
        }

        public int value() {
            return this.value;
        }

        private boolean _equals(int b) {
            return this.value == b;
        }

        public static ZoomDirection find(int value2) {
            ZoomDirection result = UNKNOWN;
            for (int i = 0; i < values().length; i++) {
                if (values()[i]._equals(value2)) {
                    return values()[i];
                }
            }
            return result;
        }
    }

    public enum TapZoomState {
        IDLE(0),
        ZOOM_IN(1),
        ZOOM_OUT(2),
        ZOOM_LIMITED(3);
        
        private final int value;

        private TapZoomState(int value2) {
            this.value = value2;
        }

        public int value() {
            return this.value;
        }

        private boolean _equals(int b) {
            return this.value == b;
        }

        public static TapZoomState find(int value2) {
            TapZoomState result = IDLE;
            for (int i = 0; i < values().length; i++) {
                if (values()[i]._equals(value2)) {
                    return values()[i];
                }
            }
            return result;
        }
    }

    public static class ThermalProfile {
        private final ThermalLensFocalLength focalLength;
        private final ThermalFrameRateUpperBound frameRateUpperBound;
        private final ThermalResolution resolution;
        private final ThermalVersion version;

        public ThermalProfile(Builder builder) {
            this.resolution = builder.resolution;
            this.frameRateUpperBound = builder.frameRateUpperBound;
            this.focalLength = builder.focalLength;
            this.version = builder.version;
        }

        public ThermalResolution getResolution() {
            return this.resolution;
        }

        public ThermalFrameRateUpperBound getFrameRateUpperBound() {
            return this.frameRateUpperBound;
        }

        public ThermalLensFocalLength getFocalLength() {
            return this.focalLength;
        }

        public ThermalVersion getVersion() {
            return this.version;
        }

        public String toString() {
            return this.resolution.toString() + " " + this.frameRateUpperBound.toString() + " " + this.focalLength.toString() + " " + this.version.name();
        }

        public int hashCode() {
            int i = 0;
            int hashCode = (((((this.resolution == null ? 0 : this.resolution.hashCode()) * 31) + (this.frameRateUpperBound == null ? 0 : this.frameRateUpperBound.hashCode())) * 31) + (this.focalLength == null ? 0 : this.focalLength.hashCode())) * 31;
            if (this.version != null) {
                i = this.version.hashCode();
            }
            return hashCode + i;
        }

        public boolean equals(Object o) {
            if (o == null || !(o instanceof ThermalProfile)) {
                return false;
            }
            if (this == o) {
                return true;
            }
            return this.resolution == ((ThermalProfile) o).resolution && this.frameRateUpperBound == ((ThermalProfile) o).frameRateUpperBound && this.focalLength == ((ThermalProfile) o).focalLength && this.version == ((ThermalProfile) o).version;
        }

        public static final class Builder {
            /* access modifiers changed from: private */
            public ThermalLensFocalLength focalLength;
            /* access modifiers changed from: private */
            public ThermalFrameRateUpperBound frameRateUpperBound;
            /* access modifiers changed from: private */
            public ThermalResolution resolution;
            /* access modifiers changed from: private */
            public ThermalVersion version;

            public Builder resolution(ThermalResolution resolution2) {
                this.resolution = resolution2;
                return this;
            }

            public Builder frameRateUpperBound(ThermalFrameRateUpperBound frameRateUpperBound2) {
                this.frameRateUpperBound = frameRateUpperBound2;
                return this;
            }

            public Builder focalLength(ThermalLensFocalLength focalLength2) {
                this.focalLength = focalLength2;
                return this;
            }

            public Builder version(ThermalVersion version2) {
                this.version = version2;
                return this;
            }

            public ThermalProfile build() {
                return new ThermalProfile(this);
            }
        }
    }

    public enum ThermalFFCMode {
        MANUAL(0),
        AUTO(1),
        UNKNOWN(255);
        
        private final int value;

        private ThermalFFCMode(int value2) {
            this.value = value2;
        }

        public int value() {
            return this.value;
        }

        private boolean _equals(int b) {
            return this.value == b;
        }

        public static ThermalFFCMode find(int value2) {
            ThermalFFCMode result = UNKNOWN;
            for (int i = 0; i < values().length; i++) {
                if (values()[i]._equals(value2)) {
                    return values()[i];
                }
            }
            return result;
        }
    }

    public enum ThermalCustomExternalSceneSettingsProfile {
        PROFILE_1(0),
        PROFILE_2(1),
        PROFILE_3(2),
        UNKNOWN(99);
        
        private final int value;

        private ThermalCustomExternalSceneSettingsProfile(int value2) {
            this.value = value2;
        }

        public int value() {
            return this.value;
        }

        private boolean _equals(int b) {
            return this.value == b;
        }

        public static ThermalCustomExternalSceneSettingsProfile find(int value2) {
            ThermalCustomExternalSceneSettingsProfile result = UNKNOWN;
            for (int i = 0; i < values().length; i++) {
                if (values()[i]._equals(value2)) {
                    return values()[i];
                }
            }
            return result;
        }
    }

    public enum Orientation {
        LANDSCAPE(0),
        PORTRAIT(1),
        UNKNOWN(255);
        
        private final int value;

        private Orientation(int value2) {
            this.value = value2;
        }

        public int value() {
            return this.value;
        }

        private boolean _equals(int b) {
            return this.value == b;
        }

        public static Orientation find(int value2) {
            Orientation result = UNKNOWN;
            for (int i = 0; i < values().length; i++) {
                if (values()[i]._equals(value2)) {
                    return values()[i];
                }
            }
            return result;
        }
    }

    public enum VideoFileCompressionStandard {
        H264(0, 0),
        H265(1, 1),
        Unknown(255, 100);
        
        private final int ordinalValue;
        private final int value;

        private VideoFileCompressionStandard(int ordinalValue2, int value2) {
            this.ordinalValue = ordinalValue2;
            this.value = value2;
        }

        public int value() {
            return this.value;
        }

        private boolean _equals(int b) {
            return this.value == b;
        }

        public static VideoFileCompressionStandard find(int value2) {
            VideoFileCompressionStandard result = Unknown;
            for (int i = 0; i < values().length; i++) {
                if (values()[i]._equals(value2)) {
                    return values()[i];
                }
            }
            return result;
        }
    }

    public enum PhotoPanoramaMode {
        PANORAMA_MODE_3X1(3),
        PANORAMA_MODE_3X3(7),
        PANORAMA_MODE_1X3(6),
        PANORAMA_MODE_SPHERE(8),
        PANORAMA_MODE_180(9),
        PANORAMA_MODE_SUPER_RESOLUTION(10),
        UNKNOWN(255);
        
        private final int value;

        private PhotoPanoramaMode(int value2) {
            this.value = value2;
        }

        public int value() {
            return this.value;
        }

        private boolean _equals(int b) {
            return this.value == b;
        }

        public static PhotoPanoramaMode find(int value2) {
            PhotoPanoramaMode result = UNKNOWN;
            for (int i = 0; i < values().length; i++) {
                if (values()[i]._equals(value2)) {
                    return values()[i];
                }
            }
            return result;
        }
    }

    public enum DisplayMode {
        VISUAL_ONLY(0),
        THERMAL_ONLY(1),
        PIP(2),
        MSX(3),
        OTHER(255);
        
        private int data;

        private DisplayMode(int _data) {
            this.data = _data;
        }

        public int value() {
            return this.data;
        }

        private boolean _equals(int b) {
            return this.data == b;
        }

        public static DisplayMode find(int b) {
            DisplayMode result = OTHER;
            for (int i = 0; i < values().length; i++) {
                if (values()[i]._equals(b)) {
                    return values()[i];
                }
            }
            return result;
        }
    }

    public enum SensorCleaningState {
        IDLE(0),
        INITIATING(1),
        WAITING_FOR_LENS_REMOVAL(2),
        READY(3),
        CLEANING(4),
        FINISHING(5),
        WAITING_FOR_LENS_REMOUNT(6),
        UNKNOWN(255);
        
        private final int value;

        private SensorCleaningState(int value2) {
            this.value = value2;
        }

        public int value() {
            return this.value;
        }

        private boolean _equals(int b) {
            return this.value == b;
        }

        public static SensorCleaningState find(int value2) {
            SensorCleaningState result = UNKNOWN;
            for (int i = 0; i < values().length; i++) {
                if (values()[i]._equals(value2)) {
                    return values()[i];
                }
            }
            return result;
        }
    }

    public enum PIPPosition {
        IR_CENTER(0),
        IR_BOTTOM_RIGHT(1),
        IR_CENTER_RIGHT(2),
        IR_TOP_RIGHT(3),
        IR_BOTTOM_CENTER(4),
        IR_TOP_CENTER(5),
        IR_BOTTOM_LEFT(6),
        IR_CENTER_LEFT(7),
        IR_TOP_LEFT(8),
        SIDE_BY_SIDE(9),
        ALIGN(10),
        OTHER(255);
        
        private int data;

        private PIPPosition(int _data) {
            this.data = _data;
        }

        public int value() {
            return this.data;
        }

        private boolean _equals(int b) {
            return this.data == b;
        }

        public static PIPPosition find(int b) {
            PIPPosition result = OTHER;
            for (int i = 0; i < values().length; i++) {
                if (values()[i]._equals(b)) {
                    return values()[i];
                }
            }
            return result;
        }
    }

    public enum NDFilterMode {
        AUTO(0),
        ON(1),
        OFF(2),
        UNKNOWN(255);
        
        private final int value;

        private NDFilterMode(int value2) {
            this.value = value2;
        }

        public int value() {
            return this.value;
        }

        private boolean _equals(int b) {
            return this.value == b;
        }

        public static NDFilterMode find(int value2) {
            NDFilterMode result = UNKNOWN;
            for (int i = 0; i < values().length; i++) {
                if (values()[i]._equals(value2)) {
                    return values()[i];
                }
            }
            return result;
        }
    }

    public enum EIColor {
        D_LOG(50),
        REC_709(51),
        UNKNOWN(255);
        
        private final int value;

        private EIColor(int value2) {
            this.value = value2;
        }

        public int value() {
            return this.value;
        }

        private boolean _equals(int b) {
            return this.value == b;
        }

        public static EIColor find(int value2) {
            EIColor result = UNKNOWN;
            for (int i = 0; i < values().length; i++) {
                if (values()[i]._equals(value2)) {
                    return values()[i];
                }
            }
            return result;
        }
    }

    public enum TemperatureUnit {
        FAHRENHEIT(1),
        CELSIUS(2),
        OTHER(100);
        
        private int data;

        private TemperatureUnit(int _data) {
            this.data = _data;
        }

        public int value() {
            return this.data;
        }

        public boolean _equals(int b) {
            return this.data == b;
        }

        public static TemperatureUnit find(int b) {
            TemperatureUnit result = OTHER;
            for (int i = 0; i < values().length; i++) {
                if (values()[i]._equals(b)) {
                    return values()[i];
                }
            }
            return result;
        }
    }

    public enum SSDColor {
        STANDARD(0),
        DLOG(1),
        REC709(2),
        CINE_LIKE(3),
        RAW_COLOR(254),
        UNKNOWN(255);
        
        private final int value;

        private SSDColor(int value2) {
            this.value = value2;
        }

        public int value() {
            return this.value;
        }

        private boolean _equals(int b) {
            return this.value == b;
        }

        public static SSDColor find(int value2) {
            SSDColor result = UNKNOWN;
            for (int i = 0; i < values().length; i++) {
                if (values()[i]._equals(value2)) {
                    return values()[i];
                }
            }
            return result;
        }
    }

    public enum VideoFov {
        DEFAULT(0),
        MIDDLE(1),
        NARROW(2),
        WIDE(3),
        UNKNOWN(255);
        
        private final int value;

        private VideoFov(int value2) {
            this.value = value2;
        }

        public int value() {
            return this.value;
        }

        private boolean _equals(int b) {
            return this.value == b;
        }

        public static VideoFov find(int value2) {
            VideoFov result = UNKNOWN;
            for (int i = 0; i < values().length; i++) {
                if (values()[i]._equals(value2)) {
                    return values()[i];
                }
            }
            return result;
        }
    }

    public enum ExposureSensitivityMode {
        ISO(1),
        EI(2),
        UNKNOWN(255);
        
        private final int value;

        private ExposureSensitivityMode(int value2) {
            this.value = value2;
        }

        public int value() {
            return this.value;
        }

        private boolean _equals(int b) {
            return this.value == b;
        }

        public static ExposureSensitivityMode find(int value2) {
            ExposureSensitivityMode result = UNKNOWN;
            for (int i = 0; i < values().length; i++) {
                if (values()[i]._equals(value2)) {
                    return values()[i];
                }
            }
            return result;
        }
    }

    public static class SSDClipFileName {
        private final int clipID;
        private String equipmentLabel;
        private final int reelID;

        public SSDClipFileName(String label, int reelID2, int clipID2) {
            this.equipmentLabel = label.toUpperCase();
            this.reelID = reelID2;
            this.clipID = clipID2;
        }

        public String getEquipmentLabel() {
            return this.equipmentLabel;
        }

        public int getReelID() {
            return this.reelID;
        }

        public int getClipID() {
            return this.clipID;
        }

        public boolean equals(Object o) {
            boolean z = true;
            if (this == o) {
                return true;
            }
            if (!(o instanceof SSDClipFileName)) {
                return false;
            }
            SSDClipFileName that = (SSDClipFileName) o;
            if (this.reelID != that.reelID || this.clipID != that.clipID) {
                return false;
            }
            if (this.equipmentLabel != null) {
                z = this.equipmentLabel.equals(that.equipmentLabel);
            } else if (that.equipmentLabel != null) {
                z = false;
            }
            return z;
        }

        public int hashCode() {
            return ((((this.equipmentLabel != null ? this.equipmentLabel.hashCode() : 0) * 31) + this.reelID) * 31) + this.clipID;
        }

        public String toString() {
            return this.equipmentLabel + this.reelID + "_" + this.clipID + ".clip";
        }
    }

    public enum StorageLocation {
        SDCARD(0),
        INTERNAL_STORAGE(1),
        UNKNOWN(255);
        
        private int value;

        private StorageLocation(int value2) {
            this.value = value2;
        }

        public int value() {
            return this.value;
        }

        private boolean _equals(int b) {
            return this.value == b;
        }

        public static StorageLocation find(int value2) {
            StorageLocation result = UNKNOWN;
            for (int i = 0; i < values().length; i++) {
                if (values()[i]._equals(value2)) {
                    return values()[i];
                }
            }
            return result;
        }
    }

    public enum SDCardStateOperationState {
        NORMAL(0),
        NOT_INSERTED(1),
        INVALID(2),
        READ_ONLY(3),
        FORMAT_NEEDED(4),
        FORMATTING(5),
        INVALID_FILE_SYSTEM(6),
        BUSY(7),
        FULL(8),
        SLOW(9),
        UNKNOWN_ERROR(10),
        NO_REMAIN_FILE_INDICES(11),
        INITIALIZING(12),
        FORMAT_RECOMMENDED(13),
        RECOVERING_FILES(14),
        WRITING_SLOWLY(15),
        USB_CONNECTED(99),
        OTHER(100);
        
        private int data;

        private SDCardStateOperationState(int _data) {
            this.data = _data;
        }

        public int value() {
            return this.data;
        }

        public boolean _equals(int b) {
            return this.data == b;
        }

        public static SDCardStateOperationState find(int b) {
            SDCardStateOperationState result = OTHER;
            for (int i = 0; i < values().length; i++) {
                if (values()[i]._equals(b)) {
                    return values()[i];
                }
            }
            return result;
        }
    }
}
