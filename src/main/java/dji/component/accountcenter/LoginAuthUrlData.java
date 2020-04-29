package dji.component.accountcenter;

import android.support.annotation.Keep;

@Keep
public class LoginAuthUrlData {
    public String authUrl;
    public boolean hasShareSession = false;

    public String toString() {
        return "Data : { authUrl = " + this.authUrl + " , hasShareSession =  }";
    }
}
