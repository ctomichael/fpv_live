package com.mapbox.android.telemetry;

import android.os.Bundle;

class ComServerInformation implements EnvironmentResolver {
    ComServerInformation() {
    }

    public void nextChain(EnvironmentResolver chain) {
    }

    public ServerInformation obtainServerInformation(Bundle appMetaData) {
        return new ServerInformation(Environment.COM);
    }
}
