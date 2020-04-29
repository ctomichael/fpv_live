package dji.component.accountcenter.response;

import android.support.annotation.Keep;

@Keep
public class CheckAccountResponseData {
    private boolean validity = false;

    public boolean isValidity() {
        return this.validity;
    }

    public String toString() {
        return "Data : { validity = " + this.validity + " }";
    }
}
