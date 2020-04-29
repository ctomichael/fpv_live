package com.mapbox.android.telemetry;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.X509TrustManager;
import okhttp3.ConnectionSpec;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;

class TelemetryClientSettings {
    private static final String CHINA_EVENTS_HOST = "events.mapbox.cn";
    private static final String COM_EVENTS_HOST = "events.mapbox.com";
    /* access modifiers changed from: private */
    public static final Map<Environment, String> HOSTS = new HashMap<Environment, String>() {
        /* class com.mapbox.android.telemetry.TelemetryClientSettings.AnonymousClass1 */

        {
            put(Environment.STAGING, TelemetryClientSettings.STAGING_EVENTS_HOST);
            put(Environment.COM, TelemetryClientSettings.COM_EVENTS_HOST);
            put(Environment.CHINA, TelemetryClientSettings.CHINA_EVENTS_HOST);
        }
    };
    private static final String HTTPS_SCHEME = "https";
    private static final String STAGING_EVENTS_HOST = "api-events-staging.tilestream.net";
    private final HttpUrl baseUrl;
    private final OkHttpClient client;
    private boolean debugLoggingEnabled;
    private Environment environment;
    private final HostnameVerifier hostnameVerifier;
    private final SSLSocketFactory sslSocketFactory;
    private final X509TrustManager x509TrustManager;

    TelemetryClientSettings(Builder builder) {
        this.environment = builder.environment;
        this.client = builder.client;
        this.baseUrl = builder.baseUrl;
        this.sslSocketFactory = builder.sslSocketFactory;
        this.x509TrustManager = builder.x509TrustManager;
        this.hostnameVerifier = builder.hostnameVerifier;
        this.debugLoggingEnabled = builder.debugLoggingEnabled;
    }

    /* access modifiers changed from: package-private */
    public Environment getEnvironment() {
        return this.environment;
    }

    /* access modifiers changed from: package-private */
    public OkHttpClient getClient(CertificateBlacklist certificateBlacklist) {
        return configureHttpClient(certificateBlacklist, new GzipRequestInterceptor());
    }

    /* access modifiers changed from: package-private */
    public OkHttpClient getAttachmentClient(CertificateBlacklist certificateBlacklist) {
        return configureHttpClient(certificateBlacklist, null);
    }

    /* access modifiers changed from: package-private */
    public HttpUrl getBaseUrl() {
        return this.baseUrl;
    }

    /* access modifiers changed from: package-private */
    public boolean isDebugLoggingEnabled() {
        return this.debugLoggingEnabled;
    }

    /* access modifiers changed from: package-private */
    public Builder toBuilder() {
        return new Builder().environment(this.environment).client(this.client).baseUrl(this.baseUrl).sslSocketFactory(this.sslSocketFactory).x509TrustManager(this.x509TrustManager).hostnameVerifier(this.hostnameVerifier).debugLoggingEnabled(this.debugLoggingEnabled);
    }

    static HttpUrl configureUrlHostname(String eventsHost) {
        HttpUrl.Builder builder = new HttpUrl.Builder().scheme(HTTPS_SCHEME);
        builder.host(eventsHost);
        return builder.build();
    }

    static final class Builder {
        HttpUrl baseUrl = null;
        OkHttpClient client = new OkHttpClient();
        boolean debugLoggingEnabled = false;
        Environment environment = Environment.COM;
        HostnameVerifier hostnameVerifier = null;
        SSLSocketFactory sslSocketFactory = null;
        X509TrustManager x509TrustManager = null;

        Builder() {
        }

        /* access modifiers changed from: package-private */
        public Builder environment(Environment environment2) {
            this.environment = environment2;
            return this;
        }

        /* access modifiers changed from: package-private */
        public Builder client(OkHttpClient client2) {
            if (client2 != null) {
                this.client = client2;
            }
            return this;
        }

        /* access modifiers changed from: package-private */
        public Builder baseUrl(HttpUrl baseUrl2) {
            if (baseUrl2 != null) {
                this.baseUrl = baseUrl2;
            }
            return this;
        }

        /* access modifiers changed from: package-private */
        public Builder sslSocketFactory(SSLSocketFactory sslSocketFactory2) {
            this.sslSocketFactory = sslSocketFactory2;
            return this;
        }

        /* access modifiers changed from: package-private */
        public Builder x509TrustManager(X509TrustManager x509TrustManager2) {
            this.x509TrustManager = x509TrustManager2;
            return this;
        }

        /* access modifiers changed from: package-private */
        public Builder hostnameVerifier(HostnameVerifier hostnameVerifier2) {
            this.hostnameVerifier = hostnameVerifier2;
            return this;
        }

        /* access modifiers changed from: package-private */
        public Builder debugLoggingEnabled(boolean debugLoggingEnabled2) {
            this.debugLoggingEnabled = debugLoggingEnabled2;
            return this;
        }

        /* access modifiers changed from: package-private */
        public TelemetryClientSettings build() {
            if (this.baseUrl == null) {
                this.baseUrl = TelemetryClientSettings.configureUrlHostname((String) TelemetryClientSettings.HOSTS.get(this.environment));
            }
            return new TelemetryClientSettings(this);
        }
    }

    private OkHttpClient configureHttpClient(CertificateBlacklist certificateBlacklist, Interceptor interceptor) {
        OkHttpClient.Builder builder = this.client.newBuilder().retryOnConnectionFailure(true).certificatePinner(new CertificatePinnerFactory().provideCertificatePinnerFor(this.environment, certificateBlacklist)).connectionSpecs(Arrays.asList(ConnectionSpec.MODERN_TLS, ConnectionSpec.COMPATIBLE_TLS));
        if (interceptor != null) {
            builder.addInterceptor(interceptor);
        }
        if (isSocketFactoryUnset(this.sslSocketFactory, this.x509TrustManager)) {
            builder.sslSocketFactory(this.sslSocketFactory, this.x509TrustManager);
            builder.hostnameVerifier(this.hostnameVerifier);
        }
        return builder.build();
    }

    private boolean isSocketFactoryUnset(SSLSocketFactory sslSocketFactory2, X509TrustManager x509TrustManager2) {
        return (sslSocketFactory2 == null || x509TrustManager2 == null) ? false : true;
    }
}
