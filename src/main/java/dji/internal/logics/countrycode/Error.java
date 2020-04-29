package dji.internal.logics.countrycode;

import dji.fieldAnnotation.EXClassNullAway;

@EXClassNullAway
public enum Error {
    NO_ERROR(0),
    UNAVAILABLE(100),
    PARAM_ERROR(101);
    
    private int errorCode;

    private Error(int i) {
        this.errorCode = i;
    }

    public int getCode() {
        return this.errorCode;
    }
}
