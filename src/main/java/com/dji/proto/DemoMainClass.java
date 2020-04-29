package com.dji.proto;

import com.dji.fieldAnnotation.EXClassNullAway;
import com.dji.proto.demo.DemoCityName;
import com.dji.proto.demo.DemoLatLngModel;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.net.URL;
import javax.net.ssl.HttpsURLConnection;

@EXClassNullAway
public class DemoMainClass {
    public static void main(String[] args) {
        DJIProtoUtil.setInject(new IDJIProtoInject() {
            /* class com.dji.proto.DemoMainClass.AnonymousClass1 */

            public String getAppName() {
                return "djigo";
            }

            public String getAppVersion() {
                return "1.0.0";
            }
        });
        String cityNameByGps = getCityNameByGps();
    }

    private static String getCityNameByGps() {
        DemoLatLngModel.Builder requestBuilder = new DemoLatLngModel.Builder();
        requestBuilder.lat = 23;
        requestBuilder.lng = 114;
        try {
            InputStream response = sendPost(DJIProtoUtil.encodeDefaultPackage(requestBuilder.build()));
            if (response != null) {
                return DemoCityName.ADAPTER.decode(DJIProtoUtil.decodeDefaultPackage(response)).city;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private static InputStream sendPost(byte[] data) throws Exception {
        HttpsURLConnection con = (HttpsURLConnection) new URL("https://selfsolve.apple.com/wcResults.do").openConnection();
        con.setRequestMethod("POST");
        con.setDoOutput(true);
        DataOutputStream wr = new DataOutputStream(con.getOutputStream());
        wr.write(data);
        wr.flush();
        wr.close();
        if (con.getResponseCode() == 200) {
            return con.getInputStream();
        }
        return null;
    }
}
