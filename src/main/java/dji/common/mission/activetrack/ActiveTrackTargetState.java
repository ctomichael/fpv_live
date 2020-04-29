package dji.common.mission.activetrack;

import dji.fieldAnnotation.EXClassNullAway;

@EXClassNullAway
public enum ActiveTrackTargetState {
    CANNOT_CONFIRM,
    WAITING_FOR_CONFIRMATION,
    TRACKING_WITH_HIGH_CONFIDENCE,
    TRACKING_WITH_LOW_CONFIDENCE,
    UNKNOWN
}
