package dji.midware.data.model.P3;

import android.support.annotation.Keep;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.model.P3.DataCameraSetFocusParam;
import dji.midware.data.model.P3.DataCameraTauExterParamType;
import dji.midware.data.model.P3.DataCameraTauFFCMode;
import dji.midware.data.model.P3.DataCameraTauParamAGC;
import dji.midware.data.model.P3.DataCameraTauParamGainMode;
import dji.midware.data.model.P3.DataCameraTauParamROI;
import dji.midware.data.model.P3.DataCameraTauParamThermometricEnable;
import dji.midware.data.model.base.DJICameraDataBase;

@Keep
@EXClassNullAway
public class DataCameraGetPushTauParam extends DJICameraDataBase {
    private static DataCameraGetPushTauParam instance = null;

    public static synchronized DataCameraGetPushTauParam getInstance() {
        DataCameraGetPushTauParam dataCameraGetPushTauParam;
        synchronized (DataCameraGetPushTauParam.class) {
            if (instance == null) {
                instance = new DataCameraGetPushTauParam();
            }
            dataCameraGetPushTauParam = instance;
        }
        return dataCameraGetPushTauParam;
    }

    /* access modifiers changed from: protected */
    public void setPushRecData(byte[] data) {
        super.setPushRecData(data);
    }

    public int getImageFormat() {
        return ((Integer) get(0, 1, Integer.class)).intValue();
    }

    public int getVideoFormat() {
        return ((Integer) get(1, 1, Integer.class)).intValue();
    }

    public int getVideoFps() {
        return ((Integer) get(2, 1, Integer.class)).intValue();
    }

    public DataCameraSetFocusParam.ZoomMode getZoomMode() {
        return DataCameraSetFocusParam.ZoomMode.find(((Integer) get(3, 1, Integer.class)).intValue());
    }

    public int getZoomScale() {
        DataCameraSetFocusParam.ZoomMode mode = getZoomMode();
        if (mode == DataCameraSetFocusParam.ZoomMode.STEP || mode == DataCameraSetFocusParam.ZoomMode.CONTINUOUS) {
            return ((Integer) get(4, 2, Integer.class)).intValue() & 255;
        }
        return ((Integer) get(4, 2, Integer.class)).intValue();
    }

    public int getDigitalFilter() {
        return ((Integer) get(6, 1, Integer.class)).intValue();
    }

    public DataCameraTauParamAGC.AGCType getAGC() {
        return getAGC(-1);
    }

    public DataCameraTauParamAGC.AGCType getAGC(int index) {
        return DataCameraTauParamAGC.AGCType.find(((Integer) get(7, 1, Integer.class, index)).intValue());
    }

    public int getDDE() {
        return ((Short) get(8, 2, Short.class)).shortValue();
    }

    public int getACE() {
        return getACE(-1);
    }

    public int getACE(int index) {
        return ((Short) get(10, 2, Short.class, index)).shortValue();
    }

    public int getSSO() {
        return getSSO(-1);
    }

    public int getSSO(int index) {
        return ((Integer) get(12, 2, Integer.class, index)).intValue();
    }

    public int getContrast() {
        return getContrast(-1);
    }

    public int getContrast(int index) {
        return ((Integer) get(14, 1, Integer.class, index)).intValue();
    }

    public int getBrightness() {
        return getBrightness(-1);
    }

    public int getBrightness(int index) {
        return ((Integer) get(15, 2, Integer.class, index)).intValue();
    }

    public float getThermometricXAxis() {
        return ((Float) get(17, 4, Float.class)).floatValue();
    }

    public float getThermometricYAxis() {
        return ((Float) get(21, 4, Float.class)).floatValue();
    }

    public float getThermometricTemp() {
        return ((Float) get(25, 4, Float.class)).floatValue();
    }

    public int getShotCountDown() {
        return ((Integer) get(29, 1, Integer.class)).intValue();
    }

    public DataCameraTauParamROI.ROIType getROIType() {
        return DataCameraTauParamROI.ROIType.find(((Integer) get(30, 1, Integer.class)).intValue());
    }

    public boolean isIsothermEnable() {
        return (((Integer) get(31, 1, Integer.class)).intValue() & 1) != 0;
    }

    public int getIsothermUnit() {
        return getIsothermUnit(-1);
    }

    public int getIsothermUnit(int index) {
        return ((Integer) get(32, 1, Integer.class, index)).intValue();
    }

    public short getIsothermLower() {
        return getIsothermLower(-1);
    }

    public short getIsothermLower(int index) {
        return ((Short) get(33, 2, Short.class, index)).shortValue();
    }

    public short getIsothermMiddle() {
        return getIsothermMiddle(-1);
    }

    public short getIsothermMiddle(int index) {
        return ((Short) get(35, 2, Short.class, index)).shortValue();
    }

    public short getIsothermUpper() {
        return getIsothermUpper(-1);
    }

    public short getIsothermUpper(int index) {
        return ((Short) get(37, 2, Short.class, index)).shortValue();
    }

    public DataCameraTauParamThermometricEnable.ThermometricType getThermometricType() {
        return getThermometricType(-1);
    }

    public DataCameraTauParamThermometricEnable.ThermometricType getThermometricType(int index) {
        return DataCameraTauParamThermometricEnable.ThermometricType.find(((Integer) get(39, 1, Integer.class, index)).intValue());
    }

    public boolean isThermometricEnable() {
        return (((Integer) get(39, 1, Integer.class)).intValue() & 1) != 0;
    }

    public int getObjectControl() {
        return ((Integer) get(40, 1, Integer.class)).intValue();
    }

    public DataCameraTauParamGainMode.GainMode getGainMode() {
        return DataCameraTauParamGainMode.GainMode.find(((Integer) get(41, 1, Integer.class)).intValue());
    }

    public VideoResolution getVideoResolution() {
        return VideoResolution.find(((Integer) get(42, 1, Integer.class)).intValue());
    }

    public LenFocusLength getLenFocusLength() {
        return LenFocusLength.find(((Integer) get(43, 1, Integer.class)).intValue());
    }

    public LenFps getLenFps() {
        return LenFps.find(((Integer) get(44, 1, Integer.class)).intValue());
    }

    public int getPhotoInterval() {
        return getPhotoInterval(-1);
    }

    public int getPhotoInterval(int index) {
        return ((Integer) get(45, 1, Integer.class, index)).intValue();
    }

    public int getTemperatureMode() {
        return ((Integer) get(46, 1, Integer.class)).intValue();
    }

    public DataCameraTauFFCMode.FFCMode getFFCMode() {
        return DataCameraTauFFCMode.FFCMode.find(((Integer) get(47, 1, Integer.class)).intValue());
    }

    public boolean supportSpotThermometric() {
        return supportSpotThermometric(-1);
    }

    public boolean supportSpotThermometric(int index) {
        return (((Integer) get(48, 1, Integer.class, index)).intValue() & 1) != 0;
    }

    public boolean isThermometricValid() {
        return isThermometricValid(-1);
    }

    public boolean isThermometricValid(int index) {
        return (((Integer) get(48, 1, Integer.class, index)).intValue() & 128) != 0;
    }

    public DataCameraTauExterParamType.ExterParamType getExterParamType() {
        return DataCameraTauExterParamType.ExterParamType.find(((Integer) get(49, 1, Integer.class)).intValue());
    }

    public short getTargetEmissivity() {
        return ((Short) get(50, 2, Short.class)).shortValue();
    }

    public short getAtmosphereTransmission() {
        return ((Short) get(52, 2, Short.class)).shortValue();
    }

    public short getAtmosphereTemperature() {
        return ((Short) get(54, 2, Short.class)).shortValue();
    }

    public short getBackgroundTemperature() {
        return ((Short) get(56, 2, Short.class)).shortValue();
    }

    public short getWindowTransmission() {
        return getWindowTransmission(-1);
    }

    public short getWindowTransmission(int index) {
        return ((Short) get(58, 2, Short.class, index)).shortValue();
    }

    public short getWindowTemperature() {
        return ((Short) get(60, 2, Short.class)).shortValue();
    }

    public short getWindowReflection() {
        return getWindowReflection(-1);
    }

    public short getWindowReflection(int index) {
        return ((Short) get(62, 2, Short.class, index)).shortValue();
    }

    public short getWindowReflectedTemperature() {
        return ((Short) get(64, 2, Short.class)).shortValue();
    }

    public int getAreaThermometricLeft() {
        return ((Integer) get(66, 2, Integer.class)).intValue();
    }

    public int getAreaThermometricTop() {
        return ((Integer) get(68, 2, Integer.class)).intValue();
    }

    public int getAreaThermometricRight() {
        return ((Integer) get(70, 2, Integer.class)).intValue();
    }

    public int getAreaThermometricBottom() {
        return ((Integer) get(72, 2, Integer.class)).intValue();
    }

    public float getAreaThermometricAverage() {
        return ((Float) get(74, 4, Float.class)).floatValue();
    }

    public float getAreaThermometricMin() {
        return ((Float) get(78, 4, Float.class)).floatValue();
    }

    public float getAreaThermometricMax() {
        return ((Float) get(82, 4, Float.class)).floatValue();
    }

    public int getAreaThermometricMinX() {
        return ((Integer) get(86, 2, Integer.class)).intValue();
    }

    public int getAreaThermometricMinY() {
        return ((Integer) get(88, 2, Integer.class)).intValue();
    }

    public int getAreaThermometricMaxX() {
        return ((Integer) get(90, 2, Integer.class)).intValue();
    }

    public int getDisplayModeOfLiveStream() {
        return ((Integer) get(94, 1, Integer.class)).intValue();
    }

    public int getPipModeOfLiveStream() {
        return ((Integer) get(95, 1, Integer.class)).intValue();
    }

    public Integer getBlendingLevel() {
        return (Integer) get(96, 1, Integer.class);
    }

    public Integer getHorizontalPosition() {
        return Integer.valueOf(((Byte) get(97, 1, Byte.class)).byteValue());
    }

    public Integer getVerticalPosition() {
        return Integer.valueOf(((Byte) get(98, 1, Byte.class)).byteValue());
    }

    public int getVideoFileFormat() {
        return ((Integer) get(99, 1, Integer.class)).intValue();
    }

    public int getAreaThermometricMaxY() {
        return ((Integer) get(92, 2, Integer.class)).intValue();
    }

    /* access modifiers changed from: protected */
    public void doPack() {
    }

    @Keep
    public enum VideoResolution {
        VR_640(0),
        VR_336(1),
        UNKNOWN(255);
        
        private int data;

        private VideoResolution(int _data) {
            this.data = _data;
        }

        public int value() {
            return this.data;
        }

        public boolean _equals(int b) {
            return this.data == b;
        }

        public static VideoResolution find(int b) {
            VideoResolution result = UNKNOWN;
            for (int i = 0; i < values().length; i++) {
                if (values()[i]._equals(b)) {
                    return values()[i];
                }
            }
            return result;
        }
    }

    @Keep
    public enum LenFocusLength {
        LFL_68(0),
        LFL_75(1),
        LFL_90(2),
        LFL_130(3),
        LFL_190(4),
        LFL_250(5),
        UNKNOWN(255);
        
        private int data;

        private LenFocusLength(int _data) {
            this.data = _data;
        }

        public int value() {
            return this.data;
        }

        public boolean _equals(int b) {
            return this.data == b;
        }

        public static LenFocusLength find(int b) {
            LenFocusLength result = UNKNOWN;
            for (int i = 0; i < values().length; i++) {
                if (values()[i]._equals(b)) {
                    return values()[i];
                }
            }
            return result;
        }
    }

    @Keep
    public enum LenFps {
        FPS_LESS_9(0),
        FPS_30(4),
        UNKNOWN(255);
        
        private int data;

        private LenFps(int _data) {
            this.data = _data;
        }

        public int value() {
            return this.data;
        }

        public boolean _equals(int b) {
            return this.data == b;
        }

        public static LenFps find(int b) {
            LenFps result = UNKNOWN;
            for (int i = 0; i < values().length; i++) {
                if (values()[i]._equals(b)) {
                    return values()[i];
                }
            }
            return result;
        }
    }
}
