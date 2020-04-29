package com.mapbox.android.telemetry;

import android.os.Bundle;

class StagingServerInformation implements EnvironmentResolver {
    private static final String KEY_META_DATA_STAGING_ACCESS_TOKEN = "com.mapbox.TestEventsAccessToken";
    private static final String KEY_META_DATA_STAGING_SERVER = "com.mapbox.TestEventsServer";
    private EnvironmentResolver chain;

    StagingServerInformation() {
    }

    public void nextChain(EnvironmentResolver chain2) {
        this.chain = chain2;
    }

    public ServerInformation obtainServerInformation(Bundle appMetaData) {
        String hostname = appMetaData.getString(KEY_META_DATA_STAGING_SERVER);
        String accessToken = appMetaData.getString(KEY_META_DATA_STAGING_ACCESS_TOKEN);
        if (TelemetryUtils.isEmpty(hostname) || TelemetryUtils.isEmpty(accessToken)) {
            return this.chain.obtainServerInformation(appMetaData);
        }
        return obtainStagingServerInformation(hostname, accessToken);
    }

    private ServerInformation obtainStagingServerInformation(String hostname, String accessToken) {
        ServerInformation staging = new ServerInformation(Environment.STAGING);
        staging.setHostname(hostname);
        staging.setAccessToken(accessToken);
        return staging;
    }
}
