package com.mapbox.android.telemetry;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class StagingCertificatePins {
    static final Map<String, List<String>> CERTIFICATE_PINS = new HashMap<String, List<String>>() {
        /* class com.mapbox.android.telemetry.StagingCertificatePins.AnonymousClass1 */

        {
            put("api-events-staging.tilestream.net", new ArrayList<String>() {
                /* class com.mapbox.android.telemetry.StagingCertificatePins.AnonymousClass1.AnonymousClass1 */

                {
                    add("3euxrJOrEZI15R4104UsiAkDqe007EPyZ6eTL/XxdAY=");
                    add("5kJvNEMw0KjrCAu7eXY5HZdvyCS13BbA0VJG1RSP91w=");
                    add("r/mIkG3eEpVdm+u/ko/cwxzOMo1bk4TyHIlByibiA5E=");
                }
            });
        }
    };

    StagingCertificatePins() {
    }
}
