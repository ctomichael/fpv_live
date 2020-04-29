package com.mapzen.android.lost.api;

import android.content.Context;
import com.mapzen.android.lost.internal.LostApiClientImpl;
import com.mapzen.android.lost.internal.LostClientManager;
import java.lang.ref.WeakReference;

public interface LostApiClient {

    public interface ConnectionCallbacks {
        void onConnected();

        void onConnectionSuspended();
    }

    void connect();

    void disconnect();

    boolean isConnected();

    void unregisterConnectionCallbacks(ConnectionCallbacks connectionCallbacks);

    public static final class Builder {
        private WeakReference<ConnectionCallbacks> connectionCallbacks;
        private final Context context;

        public Builder(Context context2) {
            this.context = context2.getApplicationContext();
        }

        public Builder addConnectionCallbacks(ConnectionCallbacks callbacks) {
            this.connectionCallbacks = new WeakReference<>(callbacks);
            return this;
        }

        public LostApiClient build() {
            ConnectionCallbacks callbacks = null;
            if (this.connectionCallbacks != null) {
                callbacks = this.connectionCallbacks.get();
            }
            return new LostApiClientImpl(this.context, callbacks, LostClientManager.shared());
        }
    }
}
