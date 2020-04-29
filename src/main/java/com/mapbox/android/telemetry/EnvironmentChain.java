package com.mapbox.android.telemetry;

class EnvironmentChain {
    EnvironmentChain() {
    }

    /* access modifiers changed from: package-private */
    public EnvironmentResolver setup() {
        EnvironmentResolver com2 = new ComServerInformation();
        EnvironmentResolver staging = new StagingServerInformation();
        staging.nextChain(com2);
        EnvironmentResolver rootOfTheChain = new ChinaServerInformation();
        rootOfTheChain.nextChain(staging);
        return rootOfTheChain;
    }
}
