package dji.component.privacy;

import android.support.annotation.Keep;
import android.support.annotation.NonNull;

@Keep
public enum PrivacyType {
    GPS_PHONE("key_privacy_state_gps_phone"),
    GPS_DJI_DEVICE("key_privacy_state_gps_dji_device"),
    SN_DJI_DEVICE("key_privacy_state_sn_dji_device"),
    INFO_USER("key_privacy_state_user_info"),
    USER_IMPROVEMENT("key_privacy_state_user_improvement");
    
    private String key;

    private PrivacyType(@NonNull String key2) {
        this.key = key2;
    }

    @NonNull
    public String getKey() {
        return this.key;
    }
}
