package com.mapzen.android.lost.internal;

import com.mapzen.android.lost.api.LostApiClient;

class ClientCallbackWrapper {
    private Object callback;
    private LostApiClient client;

    public <T> ClientCallbackWrapper(LostApiClient client2, T callback2) {
        this.client = client2;
        this.callback = callback2;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ClientCallbackWrapper wrapper = (ClientCallbackWrapper) o;
        if (this.client.equals(wrapper.client)) {
            return this.callback.equals(wrapper.callback);
        }
        return false;
    }

    public int hashCode() {
        return (this.client.hashCode() * 31) + this.callback.hashCode();
    }
}
