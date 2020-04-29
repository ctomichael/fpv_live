package com.mapzen.android.lost.internal;

import android.content.Context;
import android.os.IBinder;
import com.mapzen.android.lost.api.LostApiClient;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class FusedLocationServiceConnectionManager {
    private ConnectState connectState = ConnectState.IDLE;
    Set<LostApiClient.ConnectionCallbacks> connectionCallbacks = new HashSet();
    private EventCallbacks eventCallbacks;

    private enum ConnectState {
        IDLE,
        CONNECTING,
        CONNECTED
    }

    public interface EventCallbacks {
        void onConnect(Context context);

        void onDisconnect();

        void onServiceConnected(IBinder iBinder);
    }

    public void setEventCallbacks(EventCallbacks callbacks) {
        this.eventCallbacks = callbacks;
    }

    public void addCallbacks(LostApiClient.ConnectionCallbacks callbacks) {
        if (callbacks != null) {
            this.connectionCallbacks.add(callbacks);
        }
    }

    public void removeCallbacks(LostApiClient.ConnectionCallbacks callbacks) {
        if (callbacks != null) {
            this.connectionCallbacks.remove(callbacks);
        }
    }

    public boolean isConnected() {
        return this.connectState == ConnectState.CONNECTED;
    }

    public boolean isConnecting() {
        return this.connectState == ConnectState.CONNECTING;
    }

    public void connect(Context context, LostApiClient.ConnectionCallbacks callbacks) {
        addCallbacks(callbacks);
        if (this.connectState == ConnectState.IDLE) {
            this.connectState = ConnectState.CONNECTING;
            if (this.eventCallbacks != null) {
                this.eventCallbacks.onConnect(context);
            }
        }
    }

    public void disconnect() {
        if (this.connectState != ConnectState.IDLE) {
            this.connectState = ConnectState.IDLE;
            if (this.eventCallbacks != null) {
                this.eventCallbacks.onDisconnect();
            }
        }
    }

    public void onServiceConnected(IBinder binder) {
        if (this.connectState != ConnectState.IDLE) {
            this.connectState = ConnectState.CONNECTED;
            if (this.eventCallbacks != null) {
                this.eventCallbacks.onServiceConnected(binder);
            }
            if (!this.connectionCallbacks.isEmpty()) {
                Iterator it2 = new ArrayList<>(this.connectionCallbacks).iterator();
                while (it2.hasNext()) {
                    ((LostApiClient.ConnectionCallbacks) it2.next()).onConnected();
                }
            }
        }
    }

    public void onServiceDisconnected() {
        if (this.connectState != ConnectState.IDLE) {
            this.connectState = ConnectState.IDLE;
            if (!this.connectionCallbacks.isEmpty()) {
                Iterator it2 = new ArrayList<>(this.connectionCallbacks).iterator();
                while (it2.hasNext()) {
                    ((LostApiClient.ConnectionCallbacks) it2.next()).onConnectionSuspended();
                }
            }
        }
    }

    public Set<LostApiClient.ConnectionCallbacks> getConnectionCallbacks() {
        return this.connectionCallbacks;
    }
}
