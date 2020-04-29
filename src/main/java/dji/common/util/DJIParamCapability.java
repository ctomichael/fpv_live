package dji.common.util;

import dji.fieldAnnotation.EXClassNullAway;

@EXClassNullAway
public class DJIParamCapability {
    protected boolean isSupported;

    public DJIParamCapability(boolean isSupported2) {
        this.isSupported = isSupported2;
    }

    public boolean isSupported() {
        return this.isSupported;
    }
}
