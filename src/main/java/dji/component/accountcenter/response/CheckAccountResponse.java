package dji.component.accountcenter.response;

import android.support.annotation.Keep;
import android.text.TextUtils;
import com.google.gson.Gson;

@Keep
public class CheckAccountResponse extends AccountCenterBaseResponse {
    public CheckAccountResponseData data;

    public boolean isSuccess() {
        return this.code == 0;
    }

    public CheckAccountResponseData getData() {
        return this.data;
    }

    public static CheckAccountResponse parseString(String reponse) {
        if (!TextUtils.isEmpty(reponse)) {
            try {
                return (CheckAccountResponse) new Gson().fromJson(reponse, CheckAccountResponse.class);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
