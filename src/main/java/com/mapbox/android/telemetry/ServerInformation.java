package com.mapbox.android.telemetry;

class ServerInformation {
    private String accessToken;
    private Environment environment;
    private String hostname;

    ServerInformation(Environment environment2) {
        this.environment = environment2;
    }

    /* access modifiers changed from: package-private */
    public Environment getEnvironment() {
        return this.environment;
    }

    /* access modifiers changed from: package-private */
    public String getHostname() {
        return this.hostname;
    }

    /* access modifiers changed from: package-private */
    public void setHostname(String hostname2) {
        this.hostname = hostname2;
    }

    /* access modifiers changed from: package-private */
    public String getAccessToken() {
        return this.accessToken;
    }

    /* access modifiers changed from: package-private */
    public void setAccessToken(String accessToken2) {
        this.accessToken = accessToken2;
    }
}
