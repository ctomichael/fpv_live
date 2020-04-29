package dji.midware.data.model.P3;

import android.support.annotation.Keep;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.config.P3.DeviceType;
import dji.midware.data.config.P3.ProductType;
import dji.midware.data.manager.P3.DJIProductManager;
import dji.midware.data.model.P3.DataCameraGetImageSize;
import dji.midware.data.model.P3.DataCameraGetIso;
import dji.midware.data.model.P3.DataCameraSetCameraRotationMode;
import dji.midware.data.model.P3.DataCameraSetExposureMode;
import dji.midware.data.model.P3.DataCameraSetPhoto;
import dji.midware.data.model.P3.DataCameraSetVideoEncode;
import dji.midware.data.model.base.DJICameraDataBase;
import dji.midware.data.packages.P3.Pack;

@Keep
@EXClassNullAway
public class DataCameraGetPushShotParams extends DJICameraDataBase {
    private static DataCameraGetPushShotParams instance = null;

    public static synchronized DataCameraGetPushShotParams getInstance() {
        DataCameraGetPushShotParams dataCameraGetPushShotParams;
        synchronized (DataCameraGetPushShotParams.class) {
            if (instance == null) {
                instance = new DataCameraGetPushShotParams();
            }
            dataCameraGetPushShotParams = instance;
        }
        return dataCameraGetPushShotParams;
    }

    /* access modifiers changed from: protected */
    public void setPushRecPack(Pack pack) {
        ProductType productType = DJIProductManager.getInstance().getType();
        if ((productType != ProductType.PM420 && productType != ProductType.PM420PRO && productType != ProductType.PM420PRO_RTK) || pack == null || pack.senderType == DeviceType.CAMERA.value()) {
            super.setPushRecPack(pack);
        }
    }

    public int getApertureSize() {
        return ((Integer) get(0, 2, Integer.class)).intValue();
    }

    public int getRealApertureSize() {
        return ((Integer) get(38, 2, Integer.class)).intValue();
    }

    public int getUserRawShutter() {
        return ((Integer) get(2, 3, Integer.class)).intValue();
    }

    public int getShutter() {
        return ((Integer) get(2, 2, Integer.class)).intValue() & -32769;
    }

    public boolean isReciprocal() {
        return (((Integer) get(2, 2, Integer.class)).intValue() >> 15) == 1;
    }

    public int getShutterSpeedDecimal() {
        return ((Integer) get(4, 1, Integer.class)).intValue();
    }

    public String getShutterString() {
        int shutter = getShutter();
        int decimal = getShutterSpeedDecimal();
        String result = decimal == 0 ? shutter + "" : shutter + "." + decimal;
        if (isReciprocal()) {
            return "1/" + result;
        }
        return result;
    }

    public int getRelShutter() {
        return ((Integer) get(40, 2, Integer.class)).intValue() & -32769;
    }

    public int getRealRawShutter() {
        return ((Integer) get(40, 3, Integer.class)).intValue();
    }

    public boolean isRelReciprocal() {
        return (((Integer) get(40, 2, Integer.class)).intValue() >> 15) == 1;
    }

    public int getRelShutterSpeedDecimal() {
        return ((Integer) get(42, 1, Integer.class)).intValue();
    }

    public String getRelShutterString() {
        int shutter = getRelShutter();
        int decimal = getRelShutterSpeedDecimal();
        String result = decimal == 0 ? shutter + "" : shutter + "." + decimal;
        if (isRelReciprocal()) {
            return "1/" + result;
        }
        return result;
    }

    public String getRelShutterStringForShow() {
        int shutter = getRelShutter();
        int decimal = getRelShutterSpeedDecimal();
        String result = decimal == 0 ? shutter + "" : shutter + "." + decimal;
        if (isRelReciprocal()) {
            return "1/" + result;
        }
        return result + "″";
    }

    public float getRelShutterSpeed() {
        int shutter = getRelShutter();
        int decimal = getRelShutterSpeedDecimal();
        String data = decimal == 0 ? shutter + "" : shutter + "." + decimal;
        if (isRelReciprocal()) {
            return 1.0f / Float.parseFloat(data);
        }
        return Float.parseFloat(data);
    }

    public int getISO() {
        return DataCameraGetIso.TYPE.find(((Integer) get(5, 1, Integer.class)).intValue()).value();
    }

    public int getExposureCompensation() {
        return getExposureCompensation(-1);
    }

    public int getExposureCompensation(int index) {
        return ((Integer) get(6, 1, Integer.class, index)).intValue();
    }

    public int getRelISO() {
        return ((Integer) get(43, 4, Integer.class)).intValue();
    }

    public int getRelExposureCompensation() {
        return ((Integer) get(47, 1, Integer.class)).intValue();
    }

    public int getTimeCountdown() {
        return ((Integer) get(48, 1, Integer.class)).intValue();
    }

    public int getCtrObjectForOne() {
        return ((Integer) get(7, 1, Integer.class)).intValue();
    }

    public int getCtrObjectForTwo() {
        return ((Integer) get(8, 1, Integer.class)).intValue();
    }

    public DataCameraGetImageSize.SizeType getImageSize() {
        return getImageSize(-1);
    }

    public DataCameraGetImageSize.SizeType getImageSize(int index) {
        return DataCameraGetImageSize.SizeType.find(((Integer) get(9, 1, Integer.class, index)).intValue());
    }

    public DataCameraGetImageSize.RatioType getImageRatio() {
        return getImageRatio(-1);
    }

    public DataCameraGetImageSize.RatioType getImageRatio(int index) {
        return DataCameraGetImageSize.RatioType.find(((Integer) get(10, 1, Integer.class, index)).intValue());
    }

    public int getImageQuality() {
        return ((Integer) get(11, 1, Integer.class)).intValue();
    }

    public int getImageFormat() {
        return getImageFormat(-1);
    }

    public int getImageFormat(int index) {
        return ((Integer) get(12, 1, Integer.class, index)).intValue();
    }

    public int getVideoFormat(int... index) {
        return ((Integer) get(13, 1, Integer.class)).intValue();
    }

    public int getVideoFps() {
        return getVideoFps(-1);
    }

    public int getVideoFps(int index) {
        return ((Integer) get(14, 1, Integer.class, index)).intValue();
    }

    public int getVideoFov() {
        return getVideoFov(-1);
    }

    public int getVideoFov(int index) {
        return ((Integer) get(15, 1, Integer.class, index)).intValue();
    }

    public VideoType getVideoRecordMode() {
        return getVideoRecordMode(-1);
    }

    public VideoType getVideoRecordMode(int index) {
        return VideoType.find(((Integer) get(73, 1, Integer.class, index)).intValue());
    }

    public int getVideoContastEnhance() {
        return ((Integer) get(72, 1, Integer.class)).intValue();
    }

    public int getTimelapseSaveType() {
        return getTimelapseSaveType(-1);
    }

    public int getTimelapseSaveType(int index) {
        return ((Integer) get(74, 1, Integer.class, index)).intValue();
    }

    public int getVideoRecordIntervalTime() {
        return getVideoRecordIntervalTime(-1);
    }

    public int getVideoRecordIntervalTime(int index) {
        return ((Integer) get(75, 2, Integer.class, index)).intValue();
    }

    public int getTimelapseDuration() {
        return getTimelapseDuration(-1);
    }

    public int getTimelapseDuration(int index) {
        return ((Integer) get(77, 4, Integer.class, index)).intValue();
    }

    public int getTimelapseTimeCountDown() {
        return ((Integer) get(81, 2, Integer.class)).intValue();
    }

    public int getTimelapseRecordedFrame() {
        return ((Integer) get(83, 4, Integer.class)).intValue();
    }

    public int getOpticsScale() {
        return ((Integer) get(87, 2, Integer.class)).intValue();
    }

    public int getDigitalZoomScale() {
        return getDigitalZoomScale(-1);
    }

    public int getDigitalZoomScale(int index) {
        return ((Integer) get(89, 2, Integer.class, index)).intValue();
    }

    public int getRawBurstCount() {
        return ((Integer) get(91, 1, Integer.class)).intValue();
    }

    public int getVideoSecondOpen() {
        return getVideoSecondOpen(-1);
    }

    public int getVideoSecondOpen(int index) {
        return ((Integer) get(16, 1, Integer.class, index)).intValue();
    }

    public int getVideoSecondRatio() {
        return getVideoSecondRatio(-1);
    }

    public int getVideoSecondRatio(int index) {
        return ((Integer) get(17, 1, Integer.class, index)).intValue();
    }

    public int getVideoQuality() {
        return ((Integer) get(18, 1, Integer.class)).intValue();
    }

    public int getVideoStoreFormat() {
        return ((Integer) get(19, 1, Integer.class)).intValue();
    }

    public DataCameraSetExposureMode.ExposureMode getExposureMode() {
        return getExposureMode(-1);
    }

    public DataCameraSetExposureMode.ExposureMode getExposureMode(int index) {
        return DataCameraSetExposureMode.ExposureMode.find(((Integer) get(20, 1, Integer.class, index)).intValue());
    }

    public int getSceneMode() {
        return ((Integer) get(21, 1, Integer.class)).intValue();
    }

    public int getMetering() {
        return ((Integer) get(22, 1, Integer.class)).intValue();
    }

    public int getWhiteBalance() {
        return getWhiteBalance(-1);
    }

    public int getWhiteBalance(int index) {
        return ((Integer) get(23, 1, Integer.class, index)).intValue();
    }

    public int getColorTemp() {
        return getColorTemp(-1);
    }

    public int getColorTemp(int index) {
        return ((Integer) get(24, 1, Integer.class, index)).intValue();
    }

    public boolean isMCTFEnable() {
        return ((Integer) get(25, 1, Integer.class)).intValue() != 0;
    }

    public int getMCTFStrength() {
        return ((Integer) get(26, 1, Integer.class)).intValue();
    }

    public int getSharpe() {
        return ((Integer) get(27, 1, Integer.class)).intValue();
    }

    public int getContrast() {
        return ((Integer) get(28, 1, Integer.class)).intValue();
    }

    public int getSaturation() {
        return ((Integer) get(29, 1, Integer.class)).intValue();
    }

    public int getTonal() {
        return ((Integer) get(30, 1, Integer.class)).intValue();
    }

    public int getDigitalFilter() {
        return ((Integer) get(31, 1, Integer.class)).intValue();
    }

    public int getAntiFlicker() {
        return ((Integer) get(32, 1, Integer.class)).intValue();
    }

    public int getContinuous() {
        return ((Integer) get(33, 1, Integer.class)).intValue();
    }

    public int getTimeParamsType() {
        return getTimeParamsType(-1);
    }

    public int getTimeParamsType(int index) {
        return ((Integer) get(34, 1, Integer.class, index)).intValue();
    }

    public int getTimeParamsNum() {
        return getTimeParamsNum(-1);
    }

    public int getTimeParamsNum(int index) {
        return ((Integer) get(35, 1, Integer.class, index)).intValue();
    }

    public int getTimeParamsPeriod() {
        return getTimeParamsPeriod(-1);
    }

    public int getTimeParamsPeriod(int index) {
        return ((Integer) get(36, 2, Integer.class, index)).intValue();
    }

    public int getCapMinShutter() {
        return ((Integer) get(49, 2, Integer.class)).intValue() & -32769;
    }

    public boolean isCapMinShutterReciprocal() {
        return (((Integer) get(49, 2, Integer.class)).intValue() >> 15) == 1;
    }

    public int getCapMinShutterDecimal() {
        return ((Integer) get(51, 1, Integer.class)).intValue();
    }

    public String getCapMinShutterStr() {
        int shutter = getCapMinShutter();
        int decimal = getCapMinShutterDecimal();
        String result = decimal == 0 ? shutter + "" : shutter + "." + decimal;
        if (isCapMinShutterReciprocal()) {
            return "1/" + result;
        }
        return result;
    }

    public int getCapMaxShutter() {
        return ((Integer) get(52, 2, Integer.class)).intValue() & -32769;
    }

    public boolean isCapMaxShutterReciprocal() {
        return (((Integer) get(52, 2, Integer.class)).intValue() >> 15) == 1;
    }

    public int getCapMaxShutterDecimal() {
        return ((Integer) get(54, 1, Integer.class)).intValue();
    }

    public String getCapMaxShutterStr() {
        int shutter = getCapMaxShutter();
        int decimal = getCapMaxShutterDecimal();
        String result = decimal == 0 ? shutter + "" : shutter + "." + decimal;
        if (isCapMaxShutterReciprocal()) {
            return "1/" + result;
        }
        return result;
    }

    public int getRawCapMinShutter() {
        return ((Integer) get(49, 3, Integer.class)).intValue();
    }

    public int getRawCapMaxShutter() {
        return ((Integer) get(52, 3, Integer.class)).intValue();
    }

    public int getVideoStandard() {
        return getVideoStandard(-1);
    }

    public int getVideoStandard(int index) {
        return ((Integer) get(55, 1, Integer.class, index)).intValue();
    }

    public boolean isAELock() {
        return isAELock(-1);
    }

    public boolean isAELock(int index) {
        return ((Integer) get(56, 1, Integer.class, index)).intValue() == 1;
    }

    public int getAEBNumber() {
        return ((Integer) get(60, 1, Integer.class)).intValue();
    }

    public int getLedArmControl() {
        return ((Integer) get(66, 1, Integer.class)).intValue();
    }

    public int getConstrastEhance() {
        return ((Integer) get(72, 1, Integer.class)).intValue();
    }

    public DataCameraSetPhoto.TYPE getPhotoType() {
        return getPhotoType(-1);
    }

    public DataCameraSetPhoto.TYPE getPhotoType(int index) {
        return DataCameraSetPhoto.TYPE.find(((Integer) get(57, 1, Integer.class, index)).intValue());
    }

    public int getSpotAreaBottomRightPos() {
        return ((Integer) get(58, 1, Integer.class)).intValue();
    }

    public int getCapMinAperture() {
        return ((Integer) get(62, 2, Integer.class)).intValue();
    }

    public int getCapMaxAperture() {
        return ((Integer) get(64, 2, Integer.class)).intValue();
    }

    public boolean autoTurnOffForeLed() {
        return (((Integer) get(66, 1, Integer.class)).intValue() & 1) != 0;
    }

    public int getExposureStatus() {
        return ((Integer) get(67, 1, Integer.class)).intValue();
    }

    public boolean isLockedGimbalWhenShot() {
        return ((Integer) get(68, 1, Integer.class)).intValue() == 1;
    }

    public DataCameraSetVideoEncode.VideoEncodeType getPrimaryVideoEncodeType() {
        return getPrimaryVideoEncodeType(-1);
    }

    public DataCameraSetVideoEncode.VideoEncodeType getPrimaryVideoEncodeType(int index) {
        return DataCameraSetVideoEncode.VideoEncodeType.find(((Integer) get(69, 1, Integer.class, index)).intValue() & 15);
    }

    public DataCameraSetVideoEncode.VideoEncodeType getSecondaryVideoEncodeType() {
        return getSecondaryVideoEncodeType(-1);
    }

    public DataCameraSetVideoEncode.VideoEncodeType getSecondaryVideoEncodeType(int index) {
        return DataCameraSetVideoEncode.VideoEncodeType.find((((Integer) get(69, 1, Integer.class, index)).intValue() & 240) >> 4);
    }

    public boolean autoAEUnlock() {
        return ((Integer) get(70, 1, Integer.class)).intValue() == 0;
    }

    public boolean isVideoCaptionEnable() {
        return (((Integer) get(71, 1, Integer.class)).intValue() & 128) != 0;
    }

    public int getRawBurstNumber() {
        return ((Integer) get(91, 1, Integer.class)).intValue();
    }

    public int getMinIso() {
        return ((Integer) get(95, 1, Integer.class)).intValue();
    }

    public int getMaxIso() {
        return ((Integer) get(96, 1, Integer.class)).intValue();
    }

    public boolean isHyperlapseSaveOrigin() {
        return (((Integer) get(97, 1, Integer.class)).intValue() & 1) == 1;
    }

    public boolean isPanoSaveOrigin() {
        return ((((Integer) get(97, 1, Integer.class)).intValue() >> 1) & 1) == 1;
    }

    public int getFusionStorageFormat() {
        return ((Integer) get(99, 1, Integer.class)).intValue();
    }

    @Keep
    public enum FusionType {
        NONE(0),
        HYPERLAPSE(1),
        PANO(2);
        
        private int mValue;

        private FusionType(int i) {
            this.mValue = i;
        }

        public int value() {
            return this.mValue;
        }

        public boolean _equals(int b) {
            return this.mValue == b;
        }

        public static FusionType find(int b) {
            FusionType result = NONE;
            for (int i = 0; i < values().length; i++) {
                if (values()[i]._equals(b)) {
                    return values()[i];
                }
            }
            return result;
        }
    }

    public DataCameraSetCameraRotationMode.RotationAngleType getRotationAngleType() {
        return DataCameraSetCameraRotationMode.RotationAngleType.find(((Integer) get(92, 1, Integer.class)).intValue() >> 4);
    }

    public boolean isMechanicShutterEnable() {
        return isMechanicShutterEnable(-1);
    }

    public boolean isMechanicShutterEnable(int index) {
        return (((Integer) get(93, 1, Integer.class, index)).intValue() & 1) != 0;
    }

    public PanoMode getPanoMode() {
        return PanoMode.find(((Integer) get(61, 1, Integer.class)).intValue());
    }

    @Keep
    public enum VideoType {
        Normal(0),
        TimeLapse(1),
        Slow(2),
        HyperLapse(9),
        OTHER(255);
        
        private int data;

        private VideoType(int _data) {
            this.data = _data;
        }

        public int value() {
            return this.data;
        }

        public boolean _equals(int b) {
            return this.data == b;
        }

        public static VideoType find(int b) {
            VideoType result = OTHER;
            for (int i = 0; i < values().length; i++) {
                if (values()[i]._equals(b)) {
                    return values()[i];
                }
            }
            return result;
        }
    }

    @Keep
    public enum PanoMode {
        Auto360(1),
        Ball(2),
        Self(3),
        Manual(4),
        Auto180(5),
        VERTICAL(6),
        SECTORIAL(7),
        OTHER(0);
        
        private int data;

        private PanoMode(int _data) {
            this.data = _data;
        }

        public int value() {
            return this.data;
        }

        public boolean _equals(int b) {
            return this.data == b;
        }

        public static PanoMode find(int b) {
            PanoMode result = OTHER;
            for (int i = 0; i < values().length; i++) {
                if (values()[i]._equals(b)) {
                    return values()[i];
                }
            }
            return result;
        }
    }

    /* access modifiers changed from: protected */
    public void doPack() {
    }
}
