package com.mapbox.mapboxsdk;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.UiThread;
import com.mapbox.mapboxsdk.constants.MapboxConstants;
import com.mapbox.mapboxsdk.exceptions.MapboxConfigurationException;
import com.mapbox.mapboxsdk.log.Logger;
import com.mapbox.mapboxsdk.maps.TelemetryDefinition;
import com.mapbox.mapboxsdk.net.ConnectivityReceiver;
import com.mapbox.mapboxsdk.storage.FileSource;
import com.mapbox.mapboxsdk.utils.ThreadUtils;

@UiThread
@SuppressLint({"StaticFieldLeak"})
public final class Mapbox {
    private static Mapbox INSTANCE = null;
    private static final String TAG = "Mbgl-Mapbox";
    private static ModuleProvider moduleProvider;
    @Nullable
    private String accessToken;
    @Nullable
    private AccountsManager accounts;
    private Context context;
    @Nullable
    private TelemetryDefinition telemetry;

    @UiThread
    @NonNull
    public static synchronized Mapbox getInstance(@NonNull Context context2, @Nullable String accessToken2) {
        Mapbox mapbox;
        synchronized (Mapbox.class) {
            ThreadUtils.init(context2);
            ThreadUtils.checkThread(TAG);
            if (INSTANCE == null) {
                Context appContext = context2.getApplicationContext();
                FileSource.initializeFileDirsPaths(appContext);
                INSTANCE = new Mapbox(appContext, accessToken2);
                if (isAccessTokenValid(accessToken2)) {
                    initializeTelemetry();
                    INSTANCE.accounts = new AccountsManager();
                }
                ConnectivityReceiver.instance(appContext);
            }
            mapbox = INSTANCE;
        }
        return mapbox;
    }

    Mapbox(@NonNull Context context2, @Nullable String accessToken2) {
        this.context = context2;
        this.accessToken = accessToken2;
    }

    @Nullable
    public static String getAccessToken() {
        validateMapbox();
        return INSTANCE.accessToken;
    }

    public static void setAccessToken(String accessToken2) {
        validateMapbox();
        INSTANCE.accessToken = accessToken2;
        if (INSTANCE.telemetry != null) {
            INSTANCE.telemetry.disableTelemetrySession();
            INSTANCE.telemetry = null;
        }
        if (isAccessTokenValid(accessToken2)) {
            initializeTelemetry();
            INSTANCE.accounts = new AccountsManager();
        } else {
            INSTANCE.accounts = null;
        }
        FileSource.getInstance(getApplicationContext()).setAccessToken(accessToken2);
    }

    public static String getSkuToken() {
        if (INSTANCE.accounts != null) {
            return INSTANCE.accounts.getSkuToken();
        }
        throw new MapboxConfigurationException("A valid access token parameter is required when using a Mapbox service.\nPlease see https://www.mapbox.com/help/create-api-access-token/ to learn how to create one.\nMore information in this guide https://www.mapbox.com/help/first-steps-android-sdk/#access-tokens.Currently provided token is: " + INSTANCE.accessToken);
    }

    @NonNull
    public static Context getApplicationContext() {
        validateMapbox();
        return INSTANCE.context;
    }

    public static synchronized void setConnected(Boolean connected) {
        synchronized (Mapbox.class) {
            validateMapbox();
            ConnectivityReceiver.instance(INSTANCE.context).setConnected(connected);
        }
    }

    public static synchronized Boolean isConnected() {
        Boolean valueOf;
        synchronized (Mapbox.class) {
            validateMapbox();
            valueOf = Boolean.valueOf(ConnectivityReceiver.instance(INSTANCE.context).isConnected());
        }
        return valueOf;
    }

    private static void initializeTelemetry() {
        try {
            INSTANCE.telemetry = getModuleProvider().obtainTelemetry();
        } catch (Exception exception) {
            Logger.e(TAG, "Error occurred while initializing telemetry", exception);
            MapStrictMode.strictModeViolation("Error occurred while initializing telemetry", exception);
        }
    }

    @Nullable
    public static TelemetryDefinition getTelemetry() {
        return INSTANCE.telemetry;
    }

    @NonNull
    public static ModuleProvider getModuleProvider() {
        if (moduleProvider == null) {
            moduleProvider = new ModuleProviderImpl();
        }
        return moduleProvider;
    }

    private static void validateMapbox() {
        if (INSTANCE == null) {
            throw new MapboxConfigurationException();
        }
    }

    static boolean isAccessTokenValid(@Nullable String accessToken2) {
        if (accessToken2 == null) {
            return false;
        }
        String accessToken3 = accessToken2.trim().toLowerCase(MapboxConstants.MAPBOX_LOCALE);
        if (accessToken3.length() == 0) {
            return false;
        }
        if (accessToken3.startsWith("pk.") || accessToken3.startsWith("sk.")) {
            return true;
        }
        return false;
    }

    public static boolean hasInstance() {
        return INSTANCE != null;
    }
}
