package dji.midware.encryption.util;

import android.text.TextUtils;
import android.util.Base64;
import dji.fieldAnnotation.EXClassNullAway;
import dji.log.DJILog;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

@EXClassNullAway
public class LDMPlusEngine {
    private static final String AIRCRAFT_SN = "aicraft_sn";
    private static final String APP_KEY = "app_key";
    private static final String FEATURES = "features";
    private static final String LDM_PLUS = "silent_mode";
    private static final String LICENSED_TO = "licensed_to";
    private static final String PHONE = "phone";
    private static final String TAG = LDMPlusEngine.class.getSimpleName();
    private String connectedAircraftSerial;
    private String connectedAppKey;
    private String loadedLicenseContent;

    private LDMPlusEngine() {
    }

    private static class LazyHolder {
        /* access modifiers changed from: private */
        public static final LDMPlusEngine INSTANCE = new LDMPlusEngine();

        private LazyHolder() {
        }
    }

    public static LDMPlusEngine getInstance() {
        return LazyHolder.INSTANCE;
    }

    public void loadRawLicenseFileContent(String rawContent) {
        int firstCurlyBracketIndex;
        String b64Content = rawContent.replace("\n", "");
        if (!TextUtils.isEmpty(b64Content)) {
            String ub64Content = new String(Base64.decode(b64Content.getBytes(), 2));
            if (!TextUtils.isEmpty(b64Content) && (firstCurlyBracketIndex = ub64Content.indexOf("{")) > 0) {
                String b64Signature = ub64Content.substring(0, firstCurlyBracketIndex).replace("\n", "");
                if (!TextUtils.isEmpty(b64Signature)) {
                    String ub64Signature = new String(Base64.decode(b64Signature.getBytes(), 2));
                    if (!TextUtils.isEmpty(ub64Signature)) {
                        boolean RSAVerifyWithPublicKey = AES256Encryption.RSAVerifyWithPublicKey(ub64Signature);
                        this.loadedLicenseContent = ub64Content.substring(firstCurlyBracketIndex);
                    }
                }
            }
        }
    }

    private String getLicensedAppKey() {
        if (!TextUtils.isEmpty(this.loadedLicenseContent)) {
            try {
                String licenseAppKey = new JSONObject(this.loadedLicenseContent).getString(APP_KEY);
                if (!TextUtils.isEmpty(licenseAppKey)) {
                    return licenseAppKey;
                }
            } catch (JSONException e) {
            }
        }
        return "";
    }

    public boolean doesLicenseHaveAppKey() {
        return !TextUtils.isEmpty(getLicensedAppKey());
    }

    public String getLicenseOwner() {
        if (!TextUtils.isEmpty(this.loadedLicenseContent)) {
            try {
                String licenseOwner = new JSONObject(this.loadedLicenseContent).getString(LICENSED_TO);
                if (!TextUtils.isEmpty(licenseOwner)) {
                    return licenseOwner;
                }
            } catch (JSONException e) {
            }
        }
        return "";
    }

    public String getPhoneNum() {
        if (!TextUtils.isEmpty(this.loadedLicenseContent)) {
            try {
                String phoneNum = new JSONObject(this.loadedLicenseContent).getString(PHONE);
                if (!TextUtils.isEmpty(phoneNum)) {
                    return phoneNum;
                }
            } catch (JSONException e) {
            }
        }
        return "";
    }

    public boolean isLicenseLoaded() {
        return !TextUtils.isEmpty(this.loadedLicenseContent);
    }

    public boolean isLicenseValid() {
        if (!isLicenseLoaded()) {
            return false;
        }
        return true;
    }

    public boolean doesLicenseHaveAircraftSerial() {
        JSONArray aircraftSN = getLicensedAircraftSerialNumber();
        if (aircraftSN == null || aircraftSN.length() <= 0) {
            return false;
        }
        return true;
    }

    private JSONArray getLicensedAircraftSerialNumber() {
        try {
            return new JSONObject(this.loadedLicenseContent).getJSONArray(AIRCRAFT_SN);
        } catch (JSONException e) {
            DJILog.e(TAG, "Failed to parse JSONaicraft_sn " + e.getMessage(), new Object[0]);
            return null;
        }
    }

    public boolean isLDMPlusEnabled() {
        if (!isLicenseLoaded()) {
            return false;
        }
        try {
            JSONObject features = new JSONObject(this.loadedLicenseContent).getJSONObject(FEATURES);
            if (features != null && !features.getBoolean(LDM_PLUS)) {
                return false;
            }
        } catch (JSONException e) {
            DJILog.e(TAG, "Failed to parse JSONfeatures " + e.getMessage(), new Object[0]);
        }
        if (doesLicenseHaveAppKey()) {
            String licensedAppKey = getLicensedAppKey();
            if (TextUtils.isEmpty(licensedAppKey) || !licensedAppKey.equals(this.connectedAppKey)) {
                return false;
            }
            return true;
        } else if (!doesLicenseHaveAircraftSerial()) {
            return false;
        } else {
            if (TextUtils.isEmpty(this.connectedAircraftSerial)) {
                return true;
            }
            try {
                JSONArray aircraftSN = getLicensedAircraftSerialNumber();
                if (aircraftSN == null || aircraftSN.length() <= 0) {
                    return false;
                }
                for (int i = 0; i < aircraftSN.length(); i++) {
                    String eachSN = aircraftSN.getString(i);
                    if (!TextUtils.isEmpty(eachSN) && eachSN.equals(this.connectedAircraftSerial)) {
                        return true;
                    }
                }
                return false;
            } catch (JSONException e2) {
                DJILog.e(TAG, "Failed to parse JSONaicraft_sn " + e2.getMessage(), new Object[0]);
                return false;
            }
        }
    }

    public void setConnectedAppKey(String appKey) {
        this.connectedAppKey = appKey;
    }

    public void setConnectedAircraftSerial(String aircraftSerial) {
        this.connectedAircraftSerial = aircraftSerial;
    }
}
