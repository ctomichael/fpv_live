package com.dji.mapkit.core;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.os.Build;
import android.support.annotation.Keep;
import android.util.Log;

@Keep
public class NetworkStateReceiver extends BroadcastReceiver {
    private static final String TAG = "NetworkStateReceiver";
    private OnNetworkStateChangeListener listener;

    public interface OnNetworkStateChangeListener {
        void onNetworkStateChange(boolean z);
    }

    public NetworkStateReceiver(OnNetworkStateChangeListener listener2) {
        this.listener = listener2;
    }

    public void onReceive(Context context, Intent intent) {
        boolean isConnected = false;
        if (Build.VERSION.SDK_INT < 21) {
            ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService("connectivity");
            NetworkInfo wifiNetworkInfo = connectivityManager.getNetworkInfo(1);
            NetworkInfo dataNetworkInfo = connectivityManager.getNetworkInfo(0);
            if (wifiNetworkInfo != null && wifiNetworkInfo.isConnected()) {
                isConnected = true;
            } else if (dataNetworkInfo == null || !dataNetworkInfo.isConnected()) {
                isConnected = false;
            } else {
                isConnected = true;
            }
        } else {
            ConnectivityManager connectivityManager2 = (ConnectivityManager) context.getSystemService("connectivity");
            Network[] networks = connectivityManager2.getAllNetworks();
            StringBuilder sb = new StringBuilder();
            for (Network network : networks) {
                NetworkInfo networkInfo = connectivityManager2.getNetworkInfo(network);
                sb.append(" " + networkInfo.getTypeName() + " connect is " + networkInfo.isConnected());
                if (isConnected || networkInfo.isConnected()) {
                    isConnected = true;
                } else {
                    isConnected = false;
                }
            }
            Log.i(TAG, "network infos: " + sb.toString());
        }
        this.listener.onNetworkStateChange(isConnected);
    }
}
