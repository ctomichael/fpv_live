package dji.internal.logics.countrycode;

import android.content.Context;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import com.dji.frame.util.V_JsonUtil;
import dji.fieldAnnotation.EXClassNullAway;
import java.util.HashMap;
import java.util.TimeZone;

@EXClassNullAway
public class Verifier {
    private static final String ccJsonForV1 = "{\n\"America/New_York\":\"US\",\n\"Asia/Bangkok\":\"TH\",\n\"Asia/Chongqing\":\"CN\",\n\"Asia/Dubai\":\"AE\",\n\"Asia/Harbin\":\"CN\",\n\"Asia/Jakarta\":\"ID\",\n\"Asia/Macao\":\"MO\",\n\"Asia/Macau\":\"MO\",\n\"Asia/Seoul\":\"KR\",\n\"Asia/Shanghai\":\"CN\",\n\"Asia/Singapore\":\"SG\",\n\"Asia/Taipei\":\"TW\",\n\"Brazil/Acre\":\"BR\",\n\"Brazil/DeNoronha\":\"BR\",\n\"Brazil/East\":\"BR\",\n\"Brazil/West\":\"BR\",\n\"Canada/Atlantic\":\"CA\",\n\"Canada/Central\":\"CA\",\n\"Canada/East-Saskatchewan\":\"CA\",\n\"Canada/Eastern\":\"CA\",\n\"Canada/Mountain\":\"CA\",\n\"Canada/Newfoundland\":\"CA\",\n\"Canada/Pacific\":\"CA\",\n\"Canada/Saskatchewan\":\"CA\",\n\"Canada/Yukon\":\"CA\",\n\"Europe/London\":\"GB\",\n\"Europe/Moscow\":\"RU\",\n\"Japan\":\"JP\",\n\"Mexico/BajaNorte\":\"MX\",\n\"Mexico/BajaSur\":\"MX\",\n\"Mexico/General\":\"MX\",\n\"Singapore\":\"SG\"\n}\n";
    private static final HashMap<String, String> timeZoneMap = ((HashMap) V_JsonUtil.getGson().fromJson(ccJsonForV1, HashMap.class));

    /* access modifiers changed from: package-private */
    public boolean verifyLanguageWithCountryCode(String language, String countryCode) {
        if (TextUtils.isEmpty(language) || TextUtils.isEmpty(countryCode)) {
            return false;
        }
        return true;
    }

    /* access modifiers changed from: package-private */
    public boolean verifyTimeZoneWithCountryCode(String timeZone, String countryCode) {
        if (TextUtils.isEmpty(timeZone) || TextUtils.isEmpty(countryCode) || !timeZoneMap.containsKey(timeZone)) {
            return false;
        }
        return timeZoneMap.get(timeZone).toLowerCase().equals(timeZone.toLowerCase());
    }

    /* access modifiers changed from: package-private */
    public String getMMC(Context context) {
        String networkOperator = ((TelephonyManager) context.getSystemService("phone")).getNetworkOperator();
        if (!TextUtils.isEmpty(networkOperator)) {
            return String.valueOf(Integer.parseInt(networkOperator.substring(0, 3)));
        }
        return null;
    }

    /* access modifiers changed from: package-private */
    public String getCountryCodeByLocale(Context context) {
        return context.getResources().getConfiguration().locale.getCountry();
    }

    /* access modifiers changed from: package-private */
    public String getLanguageByLocale(Context context) {
        return context.getResources().getConfiguration().locale.getLanguage();
    }

    /* access modifiers changed from: package-private */
    public String getTimeZoneByLocale() {
        return TimeZone.getDefault().getID();
    }

    /* access modifiers changed from: package-private */
    public boolean verifyConsistency(String countryCode, String locale, String language, String timeZone) {
        if (TextUtils.isEmpty(countryCode) || TextUtils.isEmpty(locale) || TextUtils.isEmpty(language) || TextUtils.isEmpty(timeZone) || !verifyLanguageWithCountryCode(language, countryCode) || !verifyTimeZoneWithCountryCode(timeZone, countryCode)) {
            return false;
        }
        return true;
    }

    /* access modifiers changed from: package-private */
    public String verifyIPWithCountryCode(String countryCodeIP, Context context) {
        if (TextUtils.isEmpty(countryCodeIP) || !verifyConsistency(countryCodeIP, getCountryCodeByLocale(context), getLanguageByLocale(context), getTimeZoneByLocale())) {
            return null;
        }
        return countryCodeIP;
    }
}
