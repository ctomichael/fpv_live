package com.mapbox.mapboxsdk.http;

import android.support.annotation.NonNull;
import com.mapbox.mapboxsdk.Mapbox;

public class HttpRequestUrl {
    private HttpRequestUrl() {
    }

    public static String buildResourceUrl(@NonNull String host, String resourceUrl, int querySize, boolean offline) {
        String resourceUrl2;
        if (!isValidMapboxEndpoint(host)) {
            return resourceUrl;
        }
        if (querySize == 0) {
            resourceUrl2 = resourceUrl + "?";
        } else {
            resourceUrl2 = resourceUrl + "&";
        }
        if (offline) {
            return resourceUrl2 + "offline=true";
        }
        return resourceUrl2 + "sku=" + Mapbox.getSkuToken();
    }

    private static boolean isValidMapboxEndpoint(String host) {
        return host.equals("mapbox.com") || host.endsWith(".mapbox.com") || host.equals("mapbox.cn") || host.endsWith(".mapbox.cn");
    }
}
