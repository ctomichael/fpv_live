package com.mapzen.android.lost.internal;

import android.content.Context;
import android.location.Location;
import com.mapzen.android.lost.internal.LocationEngine;
import java.io.File;

public class MockEngine extends LocationEngine {
    public static final String MOCK_PROVIDER = "mock";
    private Location location;
    private File traceFile;
    protected TraceThread traceThread;
    private TraceThreadFactory traceThreadFactory;

    public MockEngine(Context context, LocationEngine.Callback callback, TraceThreadFactory traceThreadFactory2) {
        super(context, callback);
        this.traceThreadFactory = traceThreadFactory2;
    }

    public Location getLastLocation() {
        return this.location;
    }

    public boolean isProviderEnabled(String provider) {
        return false;
    }

    /* access modifiers changed from: protected */
    public void enable() {
        if (this.traceFile != null) {
            this.traceThread = this.traceThreadFactory.createTraceThread(getContext(), this.traceFile, this, new ThreadSleepFactory());
            this.traceThread.start();
        }
    }

    /* access modifiers changed from: protected */
    public void disable() {
        if (this.traceThread != null) {
            this.traceThread.cancel();
        }
    }

    public void setLocation(Location location2) {
        this.location = location2;
        if (getCallback() != null) {
            getCallback().reportLocation(location2);
        }
    }

    public void setTrace(File file) {
        this.traceFile = file;
    }

    public File getTrace() {
        return this.traceFile;
    }
}
