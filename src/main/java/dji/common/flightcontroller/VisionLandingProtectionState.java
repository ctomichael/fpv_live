package dji.common.flightcontroller;

import com.billy.cc.core.component.CCResult;
import dji.fieldAnnotation.EXClassNullAway;

@EXClassNullAway
public enum VisionLandingProtectionState {
    NONE(0),
    ANALYZING(1),
    ANALYSIS_FAILED(2),
    SAFE_TO_LAND(3),
    NOT_SAFE_TO_LAND(4),
    UNKNOWN(255);
    
    private int data;

    private VisionLandingProtectionState(int _data) {
        this.data = _data;
    }

    public int value() {
        return this.data;
    }

    public boolean _equals(int b) {
        return this.data == b;
    }

    public static VisionLandingProtectionState find(int value) {
        VisionLandingProtectionState visionLandingProtectionState = UNKNOWN;
        switch (value) {
            case CCResult.CODE_ERROR_CALLBACK_NOT_INVOKED /*-10*/:
            case -5:
            case -4:
            case -3:
            case -2:
            case -1:
                return ANALYSIS_FAILED;
            case CCResult.CODE_ERROR_TIMEOUT /*-9*/:
            case CCResult.CODE_ERROR_CANCELED /*-8*/:
            case CCResult.CODE_ERROR_CONNECT_FAILED /*-7*/:
            case CCResult.CODE_ERROR_CONTEXT_NULL /*-6*/:
            case 5:
            case 6:
            case 7:
            case 8:
            case 9:
            default:
                return UNKNOWN;
            case 0:
                return NONE;
            case 1:
            case 10:
                return ANALYZING;
            case 2:
                return SAFE_TO_LAND;
            case 3:
            case 4:
                return NOT_SAFE_TO_LAND;
        }
    }
}
