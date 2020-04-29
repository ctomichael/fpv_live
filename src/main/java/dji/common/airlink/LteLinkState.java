package dji.common.airlink;

import dji.fieldAnnotation.EXClassNullAway;

@EXClassNullAway
public enum LteLinkState {
    UNPAIRED,
    PAIRED_NOT_CONNECT,
    CONNECTED,
    USING,
    UNKNOWN
}
