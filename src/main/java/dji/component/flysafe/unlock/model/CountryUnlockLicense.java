package dji.component.flysafe.unlock.model;

import android.support.annotation.Keep;
import dji.component.flysafe.util.ProtobufHelper;
import dji.fieldAnnotation.EXClassNullAway;
import dji.flysafe.v3.License;
import dji.flysafe.v3.LicenseDataCountry;
import dji.midware.data.model.P3.DataWhiteListRequestLicense;

@Keep
@EXClassNullAway
public class CountryUnlockLicense extends WhiteListLicense {
    private int countryId;

    public int getCountryId() {
        return this.countryId;
    }

    public void setCountryId(int countryId2) {
        this.countryId = countryId2;
    }

    public CountryUnlockLicense(DataWhiteListRequestLicense licenseData, int index) {
        super(licenseData, index);
        this.countryId = licenseData.getCountryUnlockId();
    }

    public CountryUnlockLicense(License protobufLicense) {
        super(protobufLicense);
        LicenseDataCountry prtbCountry = protobufLicense.data.country;
        if (prtbCountry != null) {
            this.countryId = ProtobufHelper.toInt(prtbCountry.country_number);
        }
    }

    public CountryUnlockLicense() {
    }
}
