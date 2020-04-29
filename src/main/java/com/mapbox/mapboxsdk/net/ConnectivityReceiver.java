package com.mapbox.mapboxsdk.net;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.UiThread;
import com.mapbox.mapboxsdk.log.Logger;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class ConnectivityReceiver extends BroadcastReceiver {
    @SuppressLint({"StaticFieldLeak"})
    private static ConnectivityReceiver INSTANCE = null;
    private static final String LOG_CONNECTED = "connected - true";
    private static final String LOG_NOT_CONNECTED = "connected - false";
    private static final String TAG = "Mbgl-ConnectivityReceiver";
    private int activationCounter;
    @Nullable
    private Boolean connected;
    private Context context;
    @NonNull
    private List<ConnectivityListener> listeners = new CopyOnWriteArrayList();

    public static synchronized ConnectivityReceiver instance(@NonNull Context context2) {
        ConnectivityReceiver connectivityReceiver;
        synchronized (ConnectivityReceiver.class) {
            if (INSTANCE == null) {
                INSTANCE = new ConnectivityReceiver(context2.getApplicationContext());
                INSTANCE.addListener(new NativeConnectivityListener());
            }
            connectivityReceiver = INSTANCE;
        }
        return connectivityReceiver;
    }

    private ConnectivityReceiver(@NonNull Context context2) {
        this.context = context2;
    }

    @UiThread
    public void activate() {
        if (this.activationCounter == 0) {
            this.context.registerReceiver(this, new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE"));
        }
        this.activationCounter++;
    }

    @UiThread
    public void deactivate() {
        this.activationCounter--;
        if (this.activationCounter == 0) {
            this.context.unregisterReceiver(INSTANCE);
        }
    }

    public void onReceive(@NonNull Context context2, Intent intent) {
        if (this.connected == null) {
            notifyListeners(isNetworkActive());
        }
    }

    public void setConnected(Boolean connected2) {
        boolean state;
        this.connected = connected2;
        if (connected2 != null) {
            state = connected2.booleanValue();
        } else {
            state = isNetworkActive();
        }
        notifyListeners(state);
    }

    private void notifyListeners(boolean isConnected) {
        Logger.v(TAG, isConnected ? LOG_CONNECTED : LOG_NOT_CONNECTED);
        for (ConnectivityListener listener : this.listeners) {
            listener.onNetworkStateChanged(isConnected);
        }
    }

    public void addListener(@NonNull ConnectivityListener listener) {
        this.listeners.add(listener);
    }

    public void removeListener(@NonNull ConnectivityListener listener) {
        this.listeners.remove(listener);
    }

    public boolean isConnected() {
        return this.connected != null ? this.connected.booleanValue() : isNetworkActive();
    }

    private boolean isNetworkActive() {
        NetworkInfo activeNetwork = ((ConnectivityManager) this.context.getSystemService("connectivity")).getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnected();
    }
}
