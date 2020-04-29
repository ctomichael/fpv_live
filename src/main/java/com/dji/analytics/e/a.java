package com.dji.analytics.e;

import com.dji.analytics.DJIA;
import com.dji.analytics.ReportConfig;
import com.dji.analytics.f.c;
import dji.pilot.fpv.util.DJIFlurryReport;
import dji.thirdparty.afinal.FinalHttp;
import dji.thirdparty.afinal.utils.HttpsHelper;
import java.util.HashMap;
import java.util.Map;
import org.apache.http.Header;
import org.apache.http.HeaderElement;
import org.apache.http.HttpEntity;
import org.apache.http.entity.ByteArrayEntity;
import org.json.JSONException;
import org.json.JSONObject;

/* compiled from: HttpSender */
public class a {
    FinalHttp a;
    private final String b;
    private final C0024a c = C0024a.POST;
    private final b d;
    private ReportConfig.a e = ReportConfig.a.LOG_DATA;

    /* renamed from: com.dji.analytics.e.a$a  reason: collision with other inner class name */
    /* compiled from: HttpSender */
    public enum C0024a {
        POST
    }

    /* compiled from: HttpSender */
    public enum b {
        MSGPACK {
            public String a() {
                return "application/x-msgpack";
            }
        };

        public abstract String a();
    }

    public a(String str) {
        this.b = str;
        this.d = b.MSGPACK;
        this.a = new FinalHttp();
        this.a.configSSLSocketFactory(HttpsHelper.getDJISSLSocketFactoryForApache());
        this.a.configTimeout(30000);
    }

    /* JADX DEBUG: Failed to find minimal casts for resolve overloaded methods, cast all args instead
     method: dji.thirdparty.afinal.FinalHttp.postSync(java.lang.String, org.apache.http.Header[], org.apache.http.HttpEntity, java.lang.String):java.lang.Object
     arg types: [java.lang.String, org.apache.http.Header[], org.apache.http.entity.ByteArrayEntity, java.lang.String]
     candidates:
      dji.thirdparty.afinal.FinalHttp.postSync(java.lang.String, org.apache.http.Header[], dji.thirdparty.afinal.http.AjaxParams, java.lang.String):java.lang.Object
      dji.thirdparty.afinal.FinalHttp.postSync(java.lang.String, org.apache.http.Header[], org.apache.http.HttpEntity, java.lang.String):java.lang.Object */
    public boolean a(byte[] bArr, ReportConfig reportConfig) {
        HashMap<String, String> a2 = a(bArr);
        Header[] headerArr = new Header[a2.size()];
        int i = 0;
        for (Map.Entry entry : a2.entrySet()) {
            headerArr[i] = a((String) entry.getKey(), (String) entry.getValue());
            i++;
        }
        Object postSync = this.a.postSync(this.b, headerArr, (HttpEntity) new ByteArrayEntity(bArr), b.MSGPACK.a());
        if (postSync != null) {
            try {
                JSONObject jSONObject = new JSONObject((String) postSync);
                if (DJIA.DEV_FLAG) {
                    DJIA.log.a(DJIA.LOG_TAG, "res data=" + postSync.toString());
                }
                if (!jSONObject.getBoolean(DJIFlurryReport.FlightRecord.V2_EVENT_FLIGHTDATA_SYNCHRONOUS_SUBKEY)) {
                    return false;
                }
                if (DJIA.DEV_FLAG) {
                    DJIA.log.a(DJIA.LOG_TAG, "send success");
                }
                return true;
            } catch (JSONException e2) {
                DJIA.log.b(DJIA.LOG_TAG, "Error in parse res data error=" + e2.toString());
                return false;
            }
        } else if (!DJIA.DEV_FLAG) {
            return false;
        } else {
            DJIA.log.b(DJIA.LOG_TAG, "res data is null");
            return false;
        }
    }

    private HashMap<String, String> a(byte[] bArr) {
        HashMap<String, String> httpHeader = DJIA.getConfig().getHttpHeader();
        httpHeader.put("appid", DJIA.getConfig().getAppId());
        httpHeader.put("type", this.e.a());
        httpHeader.put("sign", c.a(bArr, DJIA.getConfig().getAppKey()));
        return httpHeader;
    }

    private Header a(final String str, final String str2) {
        return new Header() {
            /* class com.dji.analytics.e.a.AnonymousClass1 */

            public String getName() {
                return str;
            }

            public String getValue() {
                return str2;
            }

            public HeaderElement[] getElements() {
                return new HeaderElement[0];
            }
        };
    }

    public void a(ReportConfig.a aVar) {
        this.e = aVar;
    }
}
