package com.mapbox.android.telemetry;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.util.Log;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

class ConfigurationClient implements Callback {
    private static final String ACCESS_TOKEN_QUERY_PARAMETER = "access_token";
    private static final String CHINA_CONFIG_ENDPOINT = "api.mapbox.cn";
    private static final String COM_CONFIG_ENDPOINT = "api.mapbox.com";
    private static final long DAY_IN_MILLIS = 86400000;
    private static final Map<Environment, String> ENDPOINTS = new HashMap<Environment, String>() {
        /* class com.mapbox.android.telemetry.ConfigurationClient.AnonymousClass1 */

        {
            put(Environment.COM, ConfigurationClient.COM_CONFIG_ENDPOINT);
            put(Environment.STAGING, ConfigurationClient.COM_CONFIG_ENDPOINT);
            put(Environment.CHINA, ConfigurationClient.CHINA_CONFIG_ENDPOINT);
        }
    };
    private static final String EVENT_CONFIG_SEGMENT = "events-config";
    private static final String HTTPS_SCHEME = "https";
    private static final String LOG_TAG = "ConfigurationClient";
    private static final String MAPBOX_CONFIG_SYNC_KEY_TIMESTAMP = "mapboxConfigSyncTimestamp";
    private static final String USER_AGENT_REQUEST_HEADER = "User-Agent";
    private final String accessToken;
    private final OkHttpClient client;
    private final Context context;
    private final List<ConfigurationChangeHandler> handlers = new CopyOnWriteArrayList();
    private final String userAgent;

    ConfigurationClient(Context context2, String userAgent2, String accessToken2, OkHttpClient client2) {
        this.context = context2;
        this.userAgent = userAgent2;
        this.accessToken = accessToken2;
        this.client = client2;
    }

    /* access modifiers changed from: package-private */
    public void addHandler(ConfigurationChangeHandler handler) {
        this.handlers.add(handler);
    }

    /* access modifiers changed from: package-private */
    public boolean shouldUpdate() {
        return System.currentTimeMillis() - TelemetryUtils.obtainSharedPreferences(this.context).getLong(MAPBOX_CONFIG_SYNC_KEY_TIMESTAMP, 0) >= 86400000;
    }

    /* access modifiers changed from: package-private */
    public void update() {
        this.client.newCall(new Request.Builder().url(generateRequestUrl(this.context, this.accessToken)).header(USER_AGENT_REQUEST_HEADER, this.userAgent).build()).enqueue(this);
    }

    public void onFailure(Call call, IOException e) {
        saveTimestamp();
    }

    public void onResponse(Call call, Response response) throws IOException {
        ResponseBody body;
        saveTimestamp();
        if (response != null && (body = response.body()) != null) {
            for (ConfigurationChangeHandler handler : this.handlers) {
                if (handler != null) {
                    handler.onUpdate(body.string());
                }
            }
        }
    }

    private void saveTimestamp() {
        SharedPreferences.Editor editor = TelemetryUtils.obtainSharedPreferences(this.context).edit();
        editor.putLong(MAPBOX_CONFIG_SYNC_KEY_TIMESTAMP, System.currentTimeMillis());
        editor.apply();
    }

    private static HttpUrl generateRequestUrl(Context context2, String accessToken2) {
        return new HttpUrl.Builder().scheme(HTTPS_SCHEME).host(determineConfigEndpoint(context2)).addPathSegment(EVENT_CONFIG_SEGMENT).addQueryParameter(ACCESS_TOKEN_QUERY_PARAMETER, accessToken2).build();
    }

    private static String determineConfigEndpoint(Context context2) {
        EnvironmentResolver setupChain = new EnvironmentChain().setup();
        try {
            ApplicationInfo appInformation = context2.getPackageManager().getApplicationInfo(context2.getPackageName(), 128);
            if (!(appInformation == null || appInformation.metaData == null)) {
                return ENDPOINTS.get(setupChain.obtainServerInformation(appInformation.metaData).getEnvironment());
            }
        } catch (PackageManager.NameNotFoundException exception) {
            Log.e(LOG_TAG, exception.getMessage());
        }
        return COM_CONFIG_ENDPOINT;
    }
}
