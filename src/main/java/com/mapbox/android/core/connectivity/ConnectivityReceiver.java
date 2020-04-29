package com.mapbox.android.core.connectivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.support.annotation.UiThread;
import java.util.Iterator;
import java.util.concurrent.CopyOnWriteArrayList;

public class ConnectivityReceiver extends BroadcastReceiver {
    private int activationCounter;
    private Boolean connectedFlag = null;
    private CopyOnWriteArrayList<ConnectivityListener> connectivityListeners = new CopyOnWriteArrayList<>();
    private Context context;

    public ConnectivityReceiver(Context context2) {
        this.context = context2;
    }

    private static boolean getSystemConnectivity(Context context2) {
        try {
            ConnectivityManager cm = (ConnectivityManager) context2.getSystemService("connectivity");
            if (cm == null) {
                return false;
            }
            return cm.getActiveNetworkInfo().isConnectedOrConnecting();
        } catch (Exception e) {
            return false;
        }
    }

    private boolean getManagedConnectivity() {
        if (this.connectedFlag == null) {
            return getSystemConnectivity(this.context);
        }
        return this.connectedFlag.booleanValue();
    }

    public static boolean isConnected(Context context2) {
        return getSystemConnectivity(context2);
    }

    public boolean isConnected() {
        return getManagedConnectivity();
    }

    public Boolean getConnectedFlag() {
        return this.connectedFlag;
    }

    public void setConnectedFlag(Boolean connectedFlag2) {
        this.connectedFlag = connectedFlag2;
    }

    public void addConnectivityListener(ConnectivityListener listener) {
        if (!this.connectivityListeners.contains(listener)) {
            this.connectivityListeners.add(listener);
        }
    }

    public boolean removeConnectivityListener(ConnectivityListener listener) {
        return this.connectivityListeners.remove(listener);
    }

    @UiThread
    public void requestConnectivityUpdates() {
        if (this.activationCounter == 0) {
            this.context.registerReceiver(this, new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE"));
        }
        this.activationCounter++;
    }

    @UiThread
    public void removeConnectivityUpdates() {
        this.activationCounter--;
        if (this.activationCounter == 0) {
            this.context.unregisterReceiver(this);
        }
    }

    public void onReceive(Context context2, Intent intent) {
        boolean connected = getManagedConnectivity();
        Iterator<ConnectivityListener> it2 = this.connectivityListeners.iterator();
        while (it2.hasNext()) {
            it2.next().onConnectivityChanged(connected);
        }
    }
}
