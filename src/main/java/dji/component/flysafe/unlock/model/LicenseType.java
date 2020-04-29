package dji.component.flysafe.unlock.model;

import android.support.annotation.Keep;
import dji.fieldAnnotation.EXClassNullAway;
import dji.flysafe.v3.LicenseData;

@Keep
@EXClassNullAway
public enum LicenseType {
    GEO_UNLOCK(0, GeoUnlockLicense.class),
    CIRCLE_UNLOCK_AREA(1, CircleUnlockAreaLicense.class),
    COUNTRY_UNLOCK(2, CountryUnlockLicense.class),
    PARAMETER_CONFIGURATION(3, HeightUnlockLicense.class),
    PENTAGON_UNLOCK_AREA(4, PentagonUnlockAreaLicense.class),
    UNKNOWN(255);
    
    private Class<? extends WhiteListLicense> mMatchClass;
    private int value;

    private LicenseType(int value2) {
        this.value = value2;
    }

    private LicenseType(int value2, Class<? extends WhiteListLicense> geoUnlockLicenseClass) {
        this.value = value2;
        this.mMatchClass = geoUnlockLicenseClass;
    }

    public Class<? extends WhiteListLicense> getMatchClass() {
        return this.mMatchClass;
    }

    public int value() {
        return this.value;
    }

    public boolean _equals(int b) {
        return this.value == b;
    }

    public static LicenseType find(int value2) {
        LicenseType result = UNKNOWN;
        for (int i = 0; i < values().length; i++) {
            if (values()[i]._equals(value2)) {
                return values()[i];
            }
        }
        return result;
    }

    public static LicenseType find(LicenseData protobufLicenseData) {
        if (protobufLicenseData.country != null) {
            return COUNTRY_UNLOCK;
        }
        if (protobufLicenseData.circle != null) {
            return CIRCLE_UNLOCK_AREA;
        }
        if (protobufLicenseData.area != null) {
            return GEO_UNLOCK;
        }
        if (protobufLicenseData.polygon != null) {
            return PENTAGON_UNLOCK_AREA;
        }
        if (protobufLicenseData.height != null) {
            return PARAMETER_CONFIGURATION;
        }
        return UNKNOWN;
    }
}
