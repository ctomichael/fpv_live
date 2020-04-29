package dji.internal.logics.countrycode;

import dji.fieldAnnotation.EXClassNullAway;
import java.io.Serializable;

@EXClassNullAway
public class CountryCode implements Serializable {
    String country_code;
    String country_name;

    public CountryCode(String countryCode, String contryName) {
        this.country_code = countryCode;
        this.country_name = contryName;
    }

    public String getCountryCode() {
        return this.country_code;
    }

    public String getCountryName() {
        return this.country_name;
    }

    public void setCountry_code(String country_code2) {
        this.country_code = country_code2;
    }

    public void setContryName(String contryName) {
        this.country_name = contryName;
    }
}
