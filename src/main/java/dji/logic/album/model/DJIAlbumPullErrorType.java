package dji.logic.album.model;

import android.support.annotation.Keep;
import dji.fieldAnnotation.EXClassNullAway;

@Keep
@EXClassNullAway
public enum DJIAlbumPullErrorType {
    ERROR_REQ,
    NO_SUCH_FILE,
    DATA_NOMATCH,
    TIMEOUT,
    CLIENT_ABORT,
    SERVER_ABORT,
    DISCONNECT,
    UNKNOW
}
