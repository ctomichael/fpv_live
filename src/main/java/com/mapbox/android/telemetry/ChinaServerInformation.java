package com.mapbox.android.telemetry;

import android.os.Bundle;

class ChinaServerInformation implements EnvironmentResolver {
    private static final String KEY_META_DATA_CN_SERVER = "com.mapbox.CnEventsServer";
    private EnvironmentResolver chain;

    ChinaServerInformation() {
    }

    public void nextChain(EnvironmentResolver chain2) {
        this.chain = chain2;
    }

    public ServerInformation obtainServerInformation(Bundle appMetaData) {
        if (appMetaData.getBoolean(KEY_META_DATA_CN_SERVER)) {
            return new ServerInformation(Environment.CHINA);
        }
        return this.chain.obtainServerInformation(appMetaData);
    }
}
