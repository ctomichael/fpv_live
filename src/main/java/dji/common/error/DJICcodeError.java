package dji.common.error;

import dji.midware.data.config.P3.Ccode;

public class DJICcodeError extends DJIError {
    private int errCode;

    public int getErrorCode() {
        return this.errCode;
    }

    public DJICcodeError(DJIError error, Ccode ccode) {
        this(error.getDescription());
        this.errCode = ccode.relValue();
    }

    private DJICcodeError(String desc) {
        super(desc);
    }

    public static DJIError getDJIError(Ccode ccode) {
        return new DJICcodeError(DJIError.getDJIError(ccode), ccode);
    }
}
