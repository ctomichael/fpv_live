package dji.internal.logics.countrycode;

import dji.fieldAnnotation.EXClassNullAway;
import java.io.Serializable;

@EXClassNullAway
public class CountryCodeResponse implements Serializable {
    private String msg;
    private CountryCode result;
    private int status;

    public int getStatus() {
        return this.status;
    }

    public String getMsg() {
        return this.msg;
    }

    public CountryCode getCountryCode() {
        return this.result;
    }
}
