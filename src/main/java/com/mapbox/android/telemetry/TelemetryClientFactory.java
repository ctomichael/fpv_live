package com.mapbox.android.telemetry;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import com.mapbox.android.telemetry.TelemetryClientSettings;
import java.util.HashMap;
import java.util.Map;

class TelemetryClientFactory {
    private static final String LOG_TAG = "TelemetryClientFactory";
    private static final String RETRIEVING_APP_META_DATA_ERROR_MESSAGE = "Failed when retrieving app meta-data: %s";
    private final Map<Environment, TelemetryClientBuild> BUILD_TELEMETRY_CLIENT = new HashMap<Environment, TelemetryClientBuild>() {
        /* class com.mapbox.android.telemetry.TelemetryClientFactory.AnonymousClass1 */

        {
            put(Environment.CHINA, new TelemetryClientBuild() {
                /* class com.mapbox.android.telemetry.TelemetryClientFactory.AnonymousClass1.AnonymousClass1 */

                public TelemetryClient build(ServerInformation serverInformation) {
                    return TelemetryClientFactory.this.buildTelemetryClient(Environment.CHINA, TelemetryClientFactory.this.certificateBlacklist);
                }
            });
            put(Environment.STAGING, new TelemetryClientBuild() {
                /* class com.mapbox.android.telemetry.TelemetryClientFactory.AnonymousClass1.AnonymousClass2 */

                public TelemetryClient build(ServerInformation serverInformation) {
                    return TelemetryClientFactory.this.buildTelemetryClientCustom(serverInformation, TelemetryClientFactory.this.certificateBlacklist);
                }
            });
            put(Environment.COM, new TelemetryClientBuild() {
                /* class com.mapbox.android.telemetry.TelemetryClientFactory.AnonymousClass1.AnonymousClass3 */

                public TelemetryClient build(ServerInformation serverInformation) {
                    return TelemetryClientFactory.this.buildTelemetryClient(Environment.COM, TelemetryClientFactory.this.certificateBlacklist);
                }
            });
        }
    };
    private final String accessToken;
    /* access modifiers changed from: private */
    public final CertificateBlacklist certificateBlacklist;
    private final Logger logger;
    private final String userAgent;

    TelemetryClientFactory(String accessToken2, String userAgent2, Logger logger2, CertificateBlacklist certificateBlacklist2) {
        this.accessToken = accessToken2;
        this.userAgent = userAgent2;
        this.logger = logger2;
        this.certificateBlacklist = certificateBlacklist2;
    }

    /* access modifiers changed from: package-private */
    public TelemetryClient obtainTelemetryClient(Context context) {
        EnvironmentResolver setupChain = new EnvironmentChain().setup();
        try {
            ApplicationInfo appInformation = context.getPackageManager().getApplicationInfo(context.getPackageName(), 128);
            if (!(appInformation == null || appInformation.metaData == null)) {
                ServerInformation serverInformation = setupChain.obtainServerInformation(appInformation.metaData);
                return this.BUILD_TELEMETRY_CLIENT.get(serverInformation.getEnvironment()).build(serverInformation);
            }
        } catch (Exception exception) {
            this.logger.error(LOG_TAG, String.format(RETRIEVING_APP_META_DATA_ERROR_MESSAGE, exception.getMessage()));
        }
        return buildTelemetryClient(Environment.COM, this.certificateBlacklist);
    }

    /* access modifiers changed from: private */
    public TelemetryClient buildTelemetryClient(Environment environment, CertificateBlacklist certificateBlacklist2) {
        return new TelemetryClient(this.accessToken, this.userAgent, new TelemetryClientSettings.Builder().environment(environment).build(), this.logger, certificateBlacklist2);
    }

    /* access modifiers changed from: private */
    public TelemetryClient buildTelemetryClientCustom(ServerInformation serverInformation, CertificateBlacklist certificateBlacklist2) {
        Environment environment = serverInformation.getEnvironment();
        String hostname = serverInformation.getHostname();
        return new TelemetryClient(serverInformation.getAccessToken(), this.userAgent, new TelemetryClientSettings.Builder().environment(environment).baseUrl(TelemetryClientSettings.configureUrlHostname(hostname)).build(), this.logger, certificateBlacklist2);
    }
}
