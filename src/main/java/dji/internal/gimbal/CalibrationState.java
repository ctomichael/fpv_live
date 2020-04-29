package dji.internal.gimbal;

import dji.fieldAnnotation.EXClassNullAway;

@EXClassNullAway
public enum CalibrationState {
    DEFAULT,
    START,
    FAIL,
    SUCCESS
}
