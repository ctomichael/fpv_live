package dji.common.camera;

import dji.fieldAnnotation.EXClassNullAway;

@EXClassNullAway
public enum CameraSSDVideoLicense {
    LicenseKeyTypeCinemaDNG(0),
    LicenseKeyTypeProRes422HQ(1),
    LicenseKeyTypeProRes4444XQ(2),
    Unknown(255);
    
    private int value;

    private CameraSSDVideoLicense(int value2) {
        this.value = value2;
    }

    public int value() {
        return this.value;
    }

    private boolean _equals(int b) {
        return this.value == b;
    }

    public static CameraSSDVideoLicense find(int value2) {
        CameraSSDVideoLicense result = Unknown;
        for (int i = 0; i < values().length; i++) {
            if (values()[i]._equals(value2)) {
                return values()[i];
            }
        }
        return result;
    }
}
