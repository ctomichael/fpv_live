package dji.midware.data.model.P3;

import android.support.annotation.Keep;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.model.base.DJICommonDataBase;

@Keep
@EXClassNullAway
public class DataEyeGetPushPseudoCameraParams extends DJICommonDataBase {
    private static DataEyeGetPushPseudoCameraParams instance = null;

    public static synchronized DataEyeGetPushPseudoCameraParams getInstance() {
        DataEyeGetPushPseudoCameraParams dataEyeGetPushPseudoCameraParams;
        synchronized (DataEyeGetPushPseudoCameraParams.class) {
            if (instance == null) {
                instance = new DataEyeGetPushPseudoCameraParams();
            }
            dataEyeGetPushPseudoCameraParams = instance;
        }
        return dataEyeGetPushPseudoCameraParams;
    }

    public int getVersion() {
        return getVersion(-1);
    }

    public int getVersion(int index) {
        return ((Integer) get(0, 1, Integer.class, index)).intValue();
    }

    public PseudoCameraMode getCameraMode() {
        return getCameraMode(-1);
    }

    public PseudoCameraMode getCameraMode(int index) {
        return PseudoCameraMode.find(((Integer) get(1, 1, Integer.class, index)).intValue());
    }

    public PseudoCameraMissionState getMissionState() {
        return getMissionState(-1);
    }

    public PseudoCameraMissionState getMissionState(int index) {
        return PseudoCameraMissionState.find(((Integer) get(2, 1, Integer.class, index)).intValue());
    }

    public int getMissionId() {
        return ((Integer) get(3, 4, Integer.class)).intValue();
    }

    public boolean isInPanoCapture() {
        boolean inPanoMode;
        PseudoCameraMode pseudoCameraMode = getCameraMode();
        if (pseudoCameraMode == PseudoCameraMode.PSEUDO_CAMERA_MODE_PANO_3x3 || pseudoCameraMode == PseudoCameraMode.PSEUDO_CAMERA_MODE_PANO_1x3 || pseudoCameraMode == PseudoCameraMode.PSEUDO_CAMERA_MODE_PANO_3x1 || pseudoCameraMode == PseudoCameraMode.PSEUDO_CAMERA_MODE_PANO_180 || pseudoCameraMode == PseudoCameraMode.PSEUDO_CAMERA_MODE_PANO_SUPER_RESOLUTION || pseudoCameraMode == PseudoCameraMode.PSEUDO_CAMERA_MODE_PANO_SPHERE) {
            inPanoMode = true;
        } else {
            inPanoMode = false;
        }
        if (!inPanoMode || getRecDataLen() < 9 || (getMissionState() != PseudoCameraMissionState.PSEUDO_CAMERA_MISSION_STATE_RUNNING && getMissionState() != PseudoCameraMissionState.PSEUDO_CAMERA_MISSION_STATE_PROCESSING)) {
            return false;
        }
        return true;
    }

    public int getCaptureTotal() {
        if (isInPanoCapture()) {
            return ((Integer) get(7, 1, Integer.class)).intValue();
        }
        return -1;
    }

    public int getCaptureDone() {
        if (isInPanoCapture()) {
            return ((Integer) get(8, 1, Integer.class)).intValue();
        }
        return -1;
    }

    public int getProcessProgress() {
        if (getVersion() == 2) {
            return ((Integer) get(9, 1, Integer.class)).intValue();
        }
        return -1;
    }

    /* access modifiers changed from: protected */
    public void doPack() {
    }

    @Keep
    public enum PseudoCameraMode {
        PSEUDO_CAMERA_MODE_NONE(2),
        PSEUDO_CAMERA_MODE_PANO_1x3(3),
        PSEUDO_CAMERA_MODE_BOKEH(4),
        PSEUDO_CAMERA_MODE_GESTURE(5),
        PSEUDO_CAMERA_MODE_PANO_3x1(6),
        PSEUDO_CAMERA_MODE_PANO_3x3(7),
        PSEUDO_CAMERA_MODE_PANO_SPHERE(8),
        PSEUDO_CAMERA_MODE_PANO_180(9),
        PSEUDO_CAMERA_MODE_PANO_SUPER_RESOLUTION(10),
        OTHER(255);
        
        private final int data;

        private PseudoCameraMode(int _data) {
            this.data = _data;
        }

        public int value() {
            return this.data;
        }

        public boolean _equals(int b) {
            return this.data == b;
        }

        public static PseudoCameraMode find(int b) {
            PseudoCameraMode result = OTHER;
            PseudoCameraMode[] values = values();
            for (PseudoCameraMode tmp : values) {
                if (tmp._equals(b)) {
                    return tmp;
                }
            }
            return result;
        }
    }

    @Keep
    public enum PseudoCameraMissionState {
        PSEUDO_CAMERA_MISSION_STATE_IDLE(0),
        PSEUDO_CAMERA_MISSION_STATE_PREPARE(1),
        PSEUDO_CAMERA_MISSION_STATE_RUNNING(2),
        PSEUDO_CAMERA_MISSION_STATE_RELEASE(3),
        PSEUDO_CAMERA_MISSION_STATE_FORBIDDEN(4),
        PSEUDO_CAMERA_MISSION_STATE_PROCESSING(5),
        OTHER(255);
        
        private final int data;

        private PseudoCameraMissionState(int _data) {
            this.data = _data;
        }

        public int value() {
            return this.data;
        }

        public boolean _equals(int b) {
            return this.data == b;
        }

        public static PseudoCameraMissionState find(int b) {
            PseudoCameraMissionState result = OTHER;
            PseudoCameraMissionState[] values = values();
            for (PseudoCameraMissionState tmp : values) {
                if (tmp._equals(b)) {
                    return tmp;
                }
            }
            return result;
        }
    }
}
