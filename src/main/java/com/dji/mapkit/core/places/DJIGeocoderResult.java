package com.dji.mapkit.core.places;

import android.support.annotation.Keep;
import java.util.ArrayList;

@Keep
public class DJIGeocoderResult {
    public ArrayList<FirstLevel> results;
    public String status;

    @Keep
    public static class SecondLevel {
        public String long_name;
        public String short_name;
        public ArrayList<String> types;
    }

    @Keep
    public static class FirstLevel {
        public ArrayList<SecondLevel> address_components;
        public String formatted_address;
        public ArrayList<String> types;
    }

    /* JADX WARNING: Removed duplicated region for block: B:3:0x000c  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static com.dji.mapkit.core.places.DJIGeocoderResult.FirstLevel getStreetAdress(com.dji.mapkit.core.places.DJIGeocoderResult r4) {
        /*
            java.util.ArrayList<com.dji.mapkit.core.places.DJIGeocoderResult$FirstLevel> r1 = r4.results
            java.util.Iterator r1 = r1.iterator()
        L_0x0006:
            boolean r2 = r1.hasNext()
            if (r2 == 0) goto L_0x0029
            java.lang.Object r0 = r1.next()
            com.dji.mapkit.core.places.DJIGeocoderResult$FirstLevel r0 = (com.dji.mapkit.core.places.DJIGeocoderResult.FirstLevel) r0
            java.util.ArrayList<java.lang.String> r2 = r0.types
            java.lang.String r3 = "street_address"
            boolean r2 = r2.contains(r3)
            if (r2 != 0) goto L_0x0028
            java.util.ArrayList<java.lang.String> r2 = r0.types
            java.lang.String r3 = "route"
            boolean r2 = r2.contains(r3)
            if (r2 == 0) goto L_0x0006
        L_0x0028:
            return r0
        L_0x0029:
            r0 = 0
            goto L_0x0028
        */
        throw new UnsupportedOperationException("Method not decompiled: com.dji.mapkit.core.places.DJIGeocoderResult.getStreetAdress(com.dji.mapkit.core.places.DJIGeocoderResult):com.dji.mapkit.core.places.DJIGeocoderResult$FirstLevel");
    }
}
