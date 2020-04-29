package com.mapbox.android.telemetry;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import com.mapbox.android.telemetry.TelemetryEnabler;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class MapboxTelemetry implements FullQueueCallback, ServiceTaskCallback {
    private static final String LOG_TAG = "MapboxTelemetry";
    private static final String NON_NULL_APPLICATION_CONTEXT_REQUIRED = "Non-null application context required.";
    static Context applicationContext = null;
    private static AtomicReference<String> sAccessToken = new AtomicReference<>("");
    private CopyOnWriteArraySet<AttachmentListener> attachmentListeners = null;
    private CertificateBlacklist certificateBlacklist;
    private Clock clock = null;
    private ConfigurationClient configurationClient;
    private final ExecutorService executorService;
    private Callback httpCallback;
    private final EventsQueue queue;
    private final SchedulerFlusher schedulerFlusher;
    private TelemetryClient telemetryClient;
    private final TelemetryEnabler telemetryEnabler;
    private CopyOnWriteArraySet<TelemetryListener> telemetryListeners = null;
    private String userAgent;

    public MapboxTelemetry(Context context, String accessToken, String userAgent2) {
        initializeContext(context);
        setAccessToken(context, accessToken);
        this.userAgent = userAgent2;
        this.schedulerFlusher = new SchedulerFlusherFactory(applicationContext, obtainAlarmReceiver()).supply();
        this.telemetryEnabler = new TelemetryEnabler(true);
        initializeTelemetryListeners();
        initializeAttachmentListeners();
        this.httpCallback = getHttpCallback(this.telemetryListeners);
        this.executorService = ExecutorServiceFactory.create("MapboxTelemetryExecutor", 3, 20);
        this.queue = EventsQueue.create(this, this.executorService);
    }

    MapboxTelemetry(Context context, String accessToken, String userAgent2, EventsQueue queue2, TelemetryClient telemetryClient2, Callback httpCallback2, SchedulerFlusher schedulerFlusher2, Clock clock2, TelemetryEnabler telemetryEnabler2, ExecutorService executorService2) {
        initializeContext(context);
        setAccessToken(context, accessToken);
        this.userAgent = userAgent2;
        this.telemetryClient = telemetryClient2;
        this.schedulerFlusher = schedulerFlusher2;
        this.clock = clock2;
        this.telemetryEnabler = telemetryEnabler2;
        initializeTelemetryListeners();
        initializeAttachmentListeners();
        this.httpCallback = httpCallback2;
        this.executorService = executorService2;
        this.queue = queue2;
    }

    public void onFullQueue(List<Event> fullQueue) {
        if (TelemetryEnabler.State.ENABLED.equals(this.telemetryEnabler.obtainTelemetryState()) && !TelemetryUtils.adjustWakeUpMode(applicationContext)) {
            sendEvents(fullQueue, false);
        }
    }

    public void onTaskRemoved() {
        flushEnqueuedEvents();
        unregisterTelemetry();
    }

    public boolean push(Event event) {
        if (sendEventIfWhitelisted(event)) {
            return true;
        }
        return pushToQueue(event);
    }

    public boolean enable() {
        if (!TelemetryEnabler.isEventsEnabled(applicationContext)) {
            return false;
        }
        startTelemetry();
        return true;
    }

    public boolean disable() {
        if (!TelemetryEnabler.isEventsEnabled(applicationContext)) {
            return false;
        }
        stopTelemetry();
        return true;
    }

    public boolean updateSessionIdRotationInterval(SessionInterval interval) {
        final long intervalHours = (long) interval.obtainInterval();
        executeRunnable(new Runnable() {
            /* class com.mapbox.android.telemetry.MapboxTelemetry.AnonymousClass1 */

            public void run() {
                try {
                    SharedPreferences.Editor editor = TelemetryUtils.obtainSharedPreferences(MapboxTelemetry.applicationContext).edit();
                    editor.putLong(MapboxTelemetryConstants.SESSION_ROTATION_INTERVAL_MILLIS, TimeUnit.HOURS.toMillis(intervalHours));
                    editor.apply();
                } catch (Throwable throwable) {
                    Log.e(MapboxTelemetry.LOG_TAG, throwable.toString());
                }
            }
        });
        return true;
    }

    public void updateDebugLoggingEnabled(boolean isDebugLoggingEnabled) {
        if (this.telemetryClient != null) {
            this.telemetryClient.updateDebugLoggingEnabled(isDebugLoggingEnabled);
        }
    }

    public void updateUserAgent(String userAgent2) {
        if (isUserAgentValid(userAgent2)) {
            this.telemetryClient.updateUserAgent(TelemetryUtils.createFullUserAgent(userAgent2, applicationContext));
        }
    }

    public boolean updateAccessToken(String accessToken) {
        if (!isAccessTokenValid(accessToken) || !updateTelemetryClient(accessToken)) {
            return false;
        }
        sAccessToken.set(accessToken);
        return true;
    }

    public boolean addTelemetryListener(TelemetryListener listener) {
        return this.telemetryListeners.add(listener);
    }

    public boolean removeTelemetryListener(TelemetryListener listener) {
        return this.telemetryListeners.remove(listener);
    }

    public boolean addAttachmentListener(AttachmentListener listener) {
        return this.attachmentListeners.add(listener);
    }

    public boolean removeAttachmentListener(AttachmentListener listener) {
        return this.attachmentListeners.remove(listener);
    }

    /* access modifiers changed from: package-private */
    public boolean isQueueEmpty() {
        return this.queue.isEmpty();
    }

    /* access modifiers changed from: package-private */
    public boolean checkRequiredParameters(String accessToken, String userAgent2) {
        boolean areValidParameters = areRequiredParametersValid(accessToken, userAgent2);
        if (areValidParameters) {
            initializeTelemetryClient();
        }
        return areValidParameters;
    }

    private void initializeContext(Context context) {
        if (applicationContext != null) {
            return;
        }
        if (context == null || context.getApplicationContext() == null) {
            throw new IllegalArgumentException(NON_NULL_APPLICATION_CONTEXT_REQUIRED);
        }
        applicationContext = context.getApplicationContext();
    }

    private boolean areRequiredParametersValid(String accessToken, String userAgent2) {
        return isAccessTokenValid(accessToken) && isUserAgentValid(userAgent2);
    }

    private boolean isAccessTokenValid(String accessToken) {
        if (TelemetryUtils.isEmpty(accessToken)) {
            return false;
        }
        sAccessToken.set(accessToken);
        return true;
    }

    private boolean isUserAgentValid(String userAgent2) {
        if (TelemetryUtils.isEmpty(userAgent2)) {
            return false;
        }
        this.userAgent = userAgent2;
        return true;
    }

    private void initializeTelemetryClient() {
        if (this.configurationClient == null) {
            this.configurationClient = new ConfigurationClient(applicationContext, TelemetryUtils.createFullUserAgent(this.userAgent, applicationContext), sAccessToken.get(), new OkHttpClient());
        }
        if (this.certificateBlacklist == null) {
            this.certificateBlacklist = new CertificateBlacklist(applicationContext, this.configurationClient);
        }
        if (this.telemetryClient == null) {
            this.telemetryClient = createTelemetryClient(sAccessToken.get(), this.userAgent);
        }
    }

    private TelemetryClient createTelemetryClient(String accessToken, String userAgent2) {
        this.telemetryClient = new TelemetryClientFactory(accessToken, TelemetryUtils.createFullUserAgent(userAgent2, applicationContext), new Logger(), this.certificateBlacklist).obtainTelemetryClient(applicationContext);
        return this.telemetryClient;
    }

    private boolean updateTelemetryClient(String accessToken) {
        if (this.telemetryClient == null) {
            return false;
        }
        this.telemetryClient.updateAccessToken(accessToken);
        return true;
    }

    private AlarmReceiver obtainAlarmReceiver() {
        return new AlarmReceiver(new SchedulerCallback() {
            /* class com.mapbox.android.telemetry.MapboxTelemetry.AnonymousClass2 */

            public void onPeriodRaised() {
                MapboxTelemetry.this.flushEnqueuedEvents();
            }

            public void onError() {
            }
        });
    }

    /* access modifiers changed from: private */
    public synchronized void flushEnqueuedEvents() {
        final List<Event> currentEvents = this.queue.flush();
        if (!currentEvents.isEmpty()) {
            executeRunnable(new Runnable() {
                /* class com.mapbox.android.telemetry.MapboxTelemetry.AnonymousClass3 */

                public void run() {
                    try {
                        MapboxTelemetry.this.sendEvents(currentEvents, false);
                    } catch (Throwable throwable) {
                        Log.e(MapboxTelemetry.LOG_TAG, throwable.toString());
                    }
                }
            });
        }
    }

    private boolean isNetworkConnected() {
        try {
            NetworkInfo activeNetwork = ((ConnectivityManager) applicationContext.getSystemService("connectivity")).getActiveNetworkInfo();
            if (activeNetwork == null) {
                return false;
            }
            return activeNetwork.isConnected();
        } catch (Exception e) {
            return false;
        }
    }

    /* access modifiers changed from: private */
    public synchronized void sendEvents(List<Event> events, boolean serializeNulls) {
        if (isNetworkConnected() && checkRequiredParameters(sAccessToken.get(), this.userAgent)) {
            this.telemetryClient.sendEvents(events, this.httpCallback, serializeNulls);
        }
    }

    private void initializeTelemetryListeners() {
        this.telemetryListeners = new CopyOnWriteArraySet<>();
    }

    private void initializeAttachmentListeners() {
        this.attachmentListeners = new CopyOnWriteArraySet<>();
    }

    private boolean pushToQueue(Event event) {
        if (TelemetryEnabler.State.ENABLED.equals(this.telemetryEnabler.obtainTelemetryState())) {
            return this.queue.push(event);
        }
        return false;
    }

    private void unregisterTelemetry() {
        this.schedulerFlusher.unregister();
    }

    private synchronized boolean sendEventIfWhitelisted(Event event) {
        boolean isEventSent;
        isEventSent = false;
        switch (event.obtainType()) {
            case TURNSTILE:
            case CRASH:
                final List<Event> events = Collections.singletonList(event);
                executeRunnable(new Runnable() {
                    /* class com.mapbox.android.telemetry.MapboxTelemetry.AnonymousClass4 */

                    public void run() {
                        try {
                            MapboxTelemetry.this.sendEvents(events, true);
                        } catch (Throwable throwable) {
                            Log.e(MapboxTelemetry.LOG_TAG, throwable.toString());
                        }
                    }
                });
                isEventSent = true;
                break;
            case VIS_ATTACHMENT:
                sendAttachment(event);
                isEventSent = true;
                break;
        }
        return isEventSent;
    }

    private void startTelemetry() {
        if (TelemetryEnabler.State.ENABLED.equals(this.telemetryEnabler.obtainTelemetryState())) {
            startAlarm();
            enableLocationCollector(true);
        }
    }

    private void startAlarm() {
        this.schedulerFlusher.register();
        this.schedulerFlusher.schedule(obtainClock().giveMeTheElapsedRealtime());
    }

    private Clock obtainClock() {
        if (this.clock == null) {
            this.clock = new Clock();
        }
        return this.clock;
    }

    private void stopTelemetry() {
        if (TelemetryEnabler.State.ENABLED.equals(this.telemetryEnabler.obtainTelemetryState())) {
            flushEnqueuedEvents();
            unregisterTelemetry();
            enableLocationCollector(false);
        }
    }

    private synchronized void enableLocationCollector(final boolean enable) {
        executeRunnable(new Runnable() {
            /* class com.mapbox.android.telemetry.MapboxTelemetry.AnonymousClass5 */

            public void run() {
                try {
                    SharedPreferences.Editor editor = TelemetryUtils.obtainSharedPreferences(MapboxTelemetry.applicationContext).edit();
                    editor.putBoolean(MapboxTelemetryConstants.LOCATION_COLLECTOR_ENABLED, enable);
                    editor.apply();
                } catch (Throwable throwable) {
                    Log.e(MapboxTelemetry.LOG_TAG, throwable.toString());
                }
            }
        });
    }

    private static synchronized void setAccessToken(@NonNull Context context, @NonNull String accessToken) {
        synchronized (MapboxTelemetry.class) {
            if (!TelemetryUtils.isEmpty(accessToken)) {
                if (sAccessToken.getAndSet(accessToken).isEmpty()) {
                    LocalBroadcastManager.getInstance(context).sendBroadcast(new Intent(MapboxTelemetryConstants.ACTION_TOKEN_CHANGED));
                }
            }
        }
    }

    private void executeRunnable(Runnable command) {
        try {
            this.executorService.execute(command);
        } catch (RejectedExecutionException rex) {
            Log.e(LOG_TAG, rex.toString());
        }
    }

    private void sendAttachment(Event event) {
        if (checkNetworkAndParameters().booleanValue()) {
            this.telemetryClient.sendAttachment(convertEventToAttachment(event), this.attachmentListeners);
        }
    }

    private Attachment convertEventToAttachment(Event event) {
        return (Attachment) event;
    }

    private Boolean checkNetworkAndParameters() {
        return Boolean.valueOf(isNetworkConnected() && checkRequiredParameters(sAccessToken.get(), this.userAgent));
    }

    private static Callback getHttpCallback(final Set<TelemetryListener> listeners) {
        return new Callback() {
            /* class com.mapbox.android.telemetry.MapboxTelemetry.AnonymousClass6 */

            public void onFailure(Call call, IOException e) {
                for (TelemetryListener telemetryListener : listeners) {
                    telemetryListener.onHttpFailure(e.getMessage());
                }
            }

            public void onResponse(Call call, Response response) throws IOException {
                ResponseBody body = response.body();
                if (body != null) {
                    body.close();
                }
                for (TelemetryListener telemetryListener : listeners) {
                    telemetryListener.onHttpResponse(response.isSuccessful(), response.code());
                }
            }
        };
    }

    private static final class ExecutorServiceFactory {
        private ExecutorServiceFactory() {
        }

        /* access modifiers changed from: private */
        public static synchronized ExecutorService create(String name, int maxSize, long keepAliveSeconds) {
            ThreadPoolExecutor threadPoolExecutor;
            synchronized (ExecutorServiceFactory.class) {
                threadPoolExecutor = new ThreadPoolExecutor(0, maxSize, keepAliveSeconds, TimeUnit.SECONDS, new LinkedBlockingQueue(), threadFactory(name));
            }
            return threadPoolExecutor;
        }

        private static ThreadFactory threadFactory(final String name) {
            return new ThreadFactory() {
                /* class com.mapbox.android.telemetry.MapboxTelemetry.ExecutorServiceFactory.AnonymousClass1 */

                public Thread newThread(Runnable runnable) {
                    return new Thread(runnable, name);
                }
            };
        }
    }
}
