package com.mapbox.android.telemetry;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import okhttp3.CertificatePinner;

class CertificatePinnerFactory {
    private static final Map<Environment, Map<String, List<String>>> CERTIFICATES_PINS = new HashMap<Environment, Map<String, List<String>>>() {
        /* class com.mapbox.android.telemetry.CertificatePinnerFactory.AnonymousClass1 */

        {
            put(Environment.STAGING, StagingCertificatePins.CERTIFICATE_PINS);
            put(Environment.COM, ComCertificatePins.CERTIFICATE_PINS);
            put(Environment.CHINA, ChinaCertificatePins.CERTIFICATE_PINS);
        }
    };
    private static final String SHA256_PIN_FORMAT = "sha256/%s";

    CertificatePinnerFactory() {
    }

    /* access modifiers changed from: package-private */
    public CertificatePinner provideCertificatePinnerFor(Environment environment, CertificateBlacklist certificateBlacklist) {
        CertificatePinner.Builder certificatePinnerBuilder = new CertificatePinner.Builder();
        addCertificatesPins(removeBlacklistedPins(provideCertificatesPinsFor(environment), certificateBlacklist), certificatePinnerBuilder);
        return certificatePinnerBuilder.build();
    }

    /* access modifiers changed from: package-private */
    public Map<String, List<String>> provideCertificatesPinsFor(Environment environment) {
        return CERTIFICATES_PINS.get(environment);
    }

    private void addCertificatesPins(Map<String, List<String>> pins, CertificatePinner.Builder builder) {
        for (Map.Entry<String, List<String>> entry : pins.entrySet()) {
            Iterator it2 = ((List) entry.getValue()).iterator();
            while (it2.hasNext()) {
                builder.add((String) entry.getKey(), String.format(SHA256_PIN_FORMAT, (String) it2.next()));
            }
        }
    }

    private Map<String, List<String>> removeBlacklistedPins(Map<String, List<String>> pins, CertificateBlacklist certificateBlacklist) {
        String key = retrievePinKey(pins);
        List<String> hashList = pins.get(key);
        if (hashList != null) {
            pins.put(key, removeBlacklistedHashes(certificateBlacklist, hashList));
        }
        return pins;
    }

    private String retrievePinKey(Map<String, List<String>> pins) {
        return pins.keySet().iterator().next();
    }

    private List<String> removeBlacklistedHashes(CertificateBlacklist certificateBlacklist, List<String> hashList) {
        for (String hash : hashList) {
            if (certificateBlacklist.isBlacklisted(hash)) {
                hashList.remove(hash);
            }
        }
        return hashList;
    }
}
