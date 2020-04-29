package dji.midware.data.model.P3;

import android.support.annotation.Keep;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.model.base.DJICameraDataBase;

@Keep
@EXClassNullAway
public class DataCameraGetPushShotInfo extends DJICameraDataBase {
    private static DataCameraGetPushShotInfo instance = null;

    public static synchronized DataCameraGetPushShotInfo getInstance() {
        DataCameraGetPushShotInfo dataCameraGetPushShotInfo;
        synchronized (DataCameraGetPushShotInfo.class) {
            if (instance == null) {
                instance = new DataCameraGetPushShotInfo();
            }
            dataCameraGetPushShotInfo = instance;
        }
        return dataCameraGetPushShotInfo;
    }

    /* access modifiers changed from: protected */
    public void setPushRecData(byte[] data) {
        super.setPushRecData(data);
    }

    public boolean isShotConnected() {
        return (((Integer) get(0, 1, Integer.class)).intValue() & 128) != 0;
    }

    public ShotType getShotType() {
        return ShotType.find((((Integer) get(0, 1, Integer.class)).intValue() >>> 5) & 1);
    }

    public ShotFDType getShotFDType() {
        return getShotFDType(-1);
    }

    public ShotFDType getShotFDType(int index) {
        return ShotFDType.find((((Integer) get(0, 1, Integer.class, index)).intValue() >>> 6) & 1);
    }

    public ZoomFocusType getZoomFocusType() {
        return getZoomFocusType(-1);
    }

    public ZoomFocusType getZoomFocusType(int index) {
        ZoomFocusType type = ZoomFocusType.ManualZoomFocus;
        if (getShotFDType(index) == ShotFDType.ZoomShotFD) {
            return ZoomFocusType.find((((Integer) get(0, 1, Integer.class, index)).intValue() >>> 4) & 1);
        }
        return type;
    }

    public ShotFocusMode getShotFocusMode() {
        return ShotFocusMode.find((((Integer) get(0, 1, Integer.class)).intValue() >>> 2) & 3);
    }

    public FuselageFocusMode getFuselageFocusMode() {
        return getFuselageFocusMode(-1);
    }

    public FuselageFocusMode getFuselageFocusMode(int index) {
        return FuselageFocusMode.find(((Integer) get(0, 1, Integer.class, index)).intValue() & 3);
    }

    public int getShotFocusMaxStroke() {
        return getShotFocusMaxStroke(-1);
    }

    public int getShotFocusMaxStroke(int index) {
        return ((Integer) get(1, 2, Integer.class, index)).intValue();
    }

    public int getShotFocusCurStroke() {
        return ((Integer) get(3, 2, Integer.class)).intValue();
    }

    public float getObjDistance() {
        return ((Float) get(5, 4, Float.class)).floatValue();
    }

    public int getMinAperture() {
        return getMinAperture(-1);
    }

    public int getMinAperture(int index) {
        return ((Integer) get(9, 2, Integer.class, index)).intValue();
    }

    public int getMaxAperture() {
        return getMaxAperture(-1);
    }

    public int getMaxAperture(int index) {
        return ((Integer) get(11, 2, Integer.class, index)).intValue();
    }

    public float getSpotAFAxisX() {
        return ((Float) get(13, 4, Float.class)).floatValue();
    }

    public float getSpotAFAxisY() {
        return ((Float) get(17, 4, Float.class)).floatValue();
    }

    public int getFocusStatus() {
        return ((Integer) get(21, 1, Integer.class)).intValue() & 3;
    }

    public float getMFFocusProbability() {
        return (((float) ((Integer) get(22, 1, Integer.class)).intValue()) * 1.0f) / 255.0f;
    }

    public int getMinFocusDistance() {
        return getMinFocusDistance(-1);
    }

    public int getMinFocusDistance(int index) {
        return ((Integer) get(23, 2, Integer.class, index)).intValue();
    }

    public int getMaxFocusDistance() {
        return getMaxFocusDistance(-1);
    }

    public int getMaxFocusDistance(int index) {
        return ((Integer) get(25, 2, Integer.class, index)).intValue();
    }

    public int getCurFocusDistance() {
        return ((Integer) get(27, 2, Integer.class)).intValue();
    }

    public int getMinFocusDistanceStep() {
        return getMinFocusDistanceStep(-1);
    }

    public int getMinFocusDistanceStep(int index) {
        return ((Integer) get(29, 2, Integer.class, index)).intValue();
    }

    public boolean isDigitalFocusEnable() {
        return (((Integer) get(31, 1, Integer.class)).intValue() & 1) != 0;
    }

    public boolean isDigitalFocusMEnable() {
        return (((Integer) get(31, 1, Integer.class)).intValue() & 1) != 0;
    }

    public boolean isDigitalFocusAEnable() {
        return ((((Integer) get(31, 1, Integer.class)).intValue() >> 1) & 1) != 0;
    }

    public int getXAxisFocusWindowNum() {
        return ((Integer) get(32, 1, Integer.class)).intValue();
    }

    public int getYAxisFocusWindowNum() {
        return ((Integer) get(33, 1, Integer.class)).intValue();
    }

    public int getMFFocusStatus() {
        return ((Integer) get(34, 1, Integer.class)).intValue();
    }

    public int getFocusWindowStartX() {
        return ((Integer) get(35, 1, Integer.class)).intValue();
    }

    public int getFocusWindowRealNumX() {
        return ((Integer) get(36, 1, Integer.class)).intValue();
    }

    public int getFocusWindowStartY() {
        return ((Integer) get(37, 1, Integer.class)).intValue();
    }

    public int getFocusWindowRealNumY() {
        return ((Integer) get(38, 1, Integer.class)).intValue();
    }

    public int getSupportType() {
        return ((Integer) get(39, 1, Integer.class)).intValue();
    }

    public FocusMotorState getFocusMotorState() {
        return FocusMotorState.find(((Integer) get(40, 1, Integer.class)).intValue());
    }

    public MFDemarcateState getMFDemarcateState() {
        return MFDemarcateState.find(((Integer) get(41, 1, Integer.class)).intValue());
    }

    public MFDemarcateResult getMFDemarcateResult() {
        return MFDemarcateResult.find(((Integer) get(42, 1, Integer.class)).intValue());
    }

    public int getDemarcateValue() {
        return ((Integer) get(43, 2, Integer.class)).intValue();
    }

    public DustReductionStage getDustReductionStage() {
        return DustReductionStage.find(((Integer) get(45, 1, Integer.class)).intValue());
    }

    /* access modifiers changed from: protected */
    public void doPack() {
    }

    @Keep
    public enum MFDemarcateResult {
        IDLE(0),
        COMPLETED(1),
        FAIL(2),
        OTHER(100);
        
        private final int data;

        private MFDemarcateResult(int _data) {
            this.data = _data;
        }

        public int value() {
            return this.data;
        }

        public boolean _equals(int b) {
            return this.data == b;
        }

        public static MFDemarcateResult find(int b) {
            MFDemarcateResult result = IDLE;
            MFDemarcateResult[] values = values();
            for (MFDemarcateResult tmp : values) {
                if (tmp._equals(b)) {
                    return tmp;
                }
            }
            return result;
        }
    }

    @Keep
    public enum MFDemarcateState {
        NOT_DEMARCATE(0),
        DEMARCATED(1),
        DEMARCATING(2),
        OTHER(100);
        
        private final int data;

        private MFDemarcateState(int _data) {
            this.data = _data;
        }

        public int value() {
            return this.data;
        }

        public boolean _equals(int b) {
            return this.data == b;
        }

        public static MFDemarcateState find(int b) {
            MFDemarcateState result = NOT_DEMARCATE;
            MFDemarcateState[] values = values();
            for (MFDemarcateState tmp : values) {
                if (tmp._equals(b)) {
                    return tmp;
                }
            }
            return result;
        }
    }

    @Keep
    public enum FocusMotorState {
        NORMAL(0),
        INIT_FAIL(1),
        STUCK(2),
        BROKEN(3),
        OTHER(100);
        
        private final int data;

        private FocusMotorState(int _data) {
            this.data = _data;
        }

        public int value() {
            return this.data;
        }

        public boolean _equals(int b) {
            return this.data == b;
        }

        public static FocusMotorState find(int b) {
            FocusMotorState result = NORMAL;
            FocusMotorState[] values = values();
            for (FocusMotorState tmp : values) {
                if (tmp._equals(b)) {
                    return tmp;
                }
            }
            return result;
        }
    }

    @Keep
    public enum ShotFDType {
        FixedShotFD(0),
        ZoomShotFD(1),
        OTHER(6);
        
        private int data;

        private ShotFDType(int _data) {
            this.data = _data;
        }

        public int value() {
            return this.data;
        }

        public boolean _equals(int b) {
            return this.data == b;
        }

        public static ShotFDType find(int b) {
            ShotFDType result = OTHER;
            for (int i = 0; i < values().length; i++) {
                if (values()[i]._equals(b)) {
                    return values()[i];
                }
            }
            return result;
        }
    }

    @Keep
    public enum ZoomFocusType {
        ManualZoomFocus(0),
        AutoZoomFocus(1),
        OTHER(6);
        
        private int data;

        private ZoomFocusType(int _data) {
            this.data = _data;
        }

        public int value() {
            return this.data;
        }

        public boolean _equals(int b) {
            return this.data == b;
        }

        public static ZoomFocusType find(int b) {
            ZoomFocusType result = OTHER;
            for (int i = 0; i < values().length; i++) {
                if (values()[i]._equals(b)) {
                    return values()[i];
                }
            }
            return result;
        }
    }

    @Keep
    public enum ShotType {
        AF(0),
        MF(1),
        OTHER(6);
        
        private int data;

        private ShotType(int _data) {
            this.data = _data;
        }

        public int value() {
            return this.data;
        }

        public boolean _equals(int b) {
            return this.data == b;
        }

        public static ShotType find(int b) {
            ShotType result = OTHER;
            for (int i = 0; i < values().length; i++) {
                if (values()[i]._equals(b)) {
                    return values()[i];
                }
            }
            return result;
        }
    }

    @Keep
    public enum ShotFocusMode {
        Manual(0),
        Auto(1),
        OTHER(6);
        
        private int data;

        private ShotFocusMode(int _data) {
            this.data = _data;
        }

        public int value() {
            return this.data;
        }

        public boolean _equals(int b) {
            return this.data == b;
        }

        public static ShotFocusMode find(int b) {
            ShotFocusMode result = OTHER;
            for (int i = 0; i < values().length; i++) {
                if (values()[i]._equals(b)) {
                    return values()[i];
                }
            }
            return result;
        }
    }

    @Keep
    public enum FuselageFocusMode {
        Manual(0),
        OneAuto(1),
        ContinuousAuto(2),
        AutoFocusTracking(3),
        OTHER(6);
        
        private int data;

        private FuselageFocusMode(int _data) {
            this.data = _data;
        }

        public int value() {
            return this.data;
        }

        public boolean _equals(int b) {
            return this.data == b;
        }

        public static FuselageFocusMode find(int b) {
            FuselageFocusMode result = OTHER;
            for (int i = 0; i < values().length; i++) {
                if (values()[i]._equals(b)) {
                    return values()[i];
                }
            }
            return result;
        }
    }

    @Keep
    public enum DustReductionStage {
        DEFAULT_STATUS(0),
        INIT_STAGE(1),
        REMOVE_LENS_STAGE(2),
        LENS_REMOVED(3),
        START_DUST_REDUCTION_STAGE(4),
        DONE_DUST_REDUCTION_STAGE(5),
        CONNECT_LENS_STAGE(6),
        OTHER(7);
        
        private int data;

        private DustReductionStage(int _data) {
            this.data = _data;
        }

        public int value() {
            return this.data;
        }

        public boolean _equals(int b) {
            return this.data == b;
        }

        public static DustReductionStage find(int b) {
            DustReductionStage result = OTHER;
            for (int i = 0; i < values().length; i++) {
                if (values()[i]._equals(b)) {
                    return values()[i];
                }
            }
            return result;
        }
    }
}
