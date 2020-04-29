package dji.common.gimbal;

import dji.fieldAnnotation.EXClassNullAway;

@EXClassNullAway
public enum BalanceState {
    BALANCED,
    TILTING_LEFT,
    TILTING_RIGHT,
    UNKNOWN
}
