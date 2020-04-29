package dji.pilot.publics.model;

import dji.fieldAnnotation.EXClassNullAway;

@EXClassNullAway
public interface DJIUpgradeCode {
    public static final int CODE_CHECK_STATUS = 102;
    public static final int CODE_MODULE_NOTDETECT = 106;
    public static final int CODE_REQUEST_RECEIVE_DATA = 103;
    public static final int CODE_REQUEST_UPGRADE_MODE = 101;
    public static final int CODE_TRANSLATE_DATA = 104;
    public static final int COEE_TRANSLATE_COMPLETE = 105;
    public static final int RESULT_FAIL = 1;
    public static final int RESULT_OK = 0;
}
