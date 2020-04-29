package dji.midware.broadcastReceivers;

import android.annotation.TargetApi;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.os.Build;
import android.support.annotation.Keep;
import android.support.annotation.Nullable;
import dji.fieldAnnotation.EXClassNullAway;
import dji.log.DJILog;
import dji.midware.link.DJILinkDaemonService;
import dji.midware.link.DJILinkType;
import dji.midware.util.NetworkUtils;
import dji.midware.util.ProcessUtils;
import dji.midware.wifi.DJIMultiNetworkMgr;
import java.io.IOException;
import java.util.concurrent.TimeoutException;
import org.greenrobot.eventbus.EventBus;

@Keep
@EXClassNullAway
public class DJINetWorkReceiver extends BroadcastReceiver {
    private static final String TAG = "DJINetWorkReceiver";
    private static volatile boolean isConnected = false;
    private static boolean isOnline = false;
    private static volatile boolean isWifiConnected = false;
    private static long pingTime = 0;

    @Keep
    public enum DJINetWorkStatusEvent {
        CONNECT_OK,
        CONNECT_LOSE,
        WIFI_CHANGED,
        CONNECT_OK_WIFI,
        CONNECT_LOSE_WIFI,
        WIFI_ENABLE,
        WIFI_DISABLE
    }

    public DJINetWorkReceiver(Context context) {
        DJIMultiNetworkMgr.getInstance().init(context);
        checkNetworkStatus(context);
    }

    public DJINetWorkReceiver() {
    }

    public void onReceive(Context context, Intent intent) {
        DJILog.logWriteD(TAG, "receive action " + intent.getAction(), TAG, new Object[0]);
        checkNetworkStatus(context);
        if (intent.getAction().equals("android.net.conn.CONNECTIVITY_CHANGE")) {
            EventBus.getDefault().post(DJINetWorkStatusEvent.WIFI_CHANGED);
        }
        if (intent.getAction().equals("android.net.wifi.STATE_CHANGE")) {
            NetworkInfo info = (NetworkInfo) intent.getParcelableExtra("networkInfo");
            if (info.getState().equals(NetworkInfo.State.DISCONNECTED)) {
                if (isWifiConnected) {
                    DJILog.logWriteD(TAG, "wifi connected", TAG, new Object[0]);
                    EventBus.getDefault().post(DJINetWorkStatusEvent.CONNECT_LOSE_WIFI);
                    isWifiConnected = false;
                }
            } else if (info.getState().equals(NetworkInfo.State.CONNECTED) && !isWifiConnected) {
                DJILog.logWriteD(TAG, "wifi disconnected", TAG, new Object[0]);
                EventBus.getDefault().post(DJINetWorkStatusEvent.CONNECT_OK_WIFI);
                isWifiConnected = true;
            }
        }
        if (intent.getAction().equals("android.net.wifi.WIFI_STATE_CHANGED")) {
            int wifistate = intent.getIntExtra("wifi_state", 1);
            if (wifistate == 1) {
                EventBus.getDefault().post(DJINetWorkStatusEvent.WIFI_DISABLE);
            } else if (wifistate == 3) {
                EventBus.getDefault().post(DJINetWorkStatusEvent.WIFI_ENABLE);
            }
        }
        NetworkUtils.refreshConnectivity();
    }

    private void checkNetworkStatus(Context context) {
        NetworkInfo networkInfo = ((ConnectivityManager) context.getSystemService("connectivity")).getActiveNetworkInfo();
        if (networkInfo != null) {
            DJILog.d("checkNetworkStatus", "networkInfo : " + networkInfo.toString(), new Object[0]);
        } else {
            DJILog.d("checkNetworkStatus", "networkInfo null", new Object[0]);
        }
        if (networkInfo == null || !networkInfo.isConnected()) {
            DJILog.i("checkNetworkStatus", "unconnect", new Object[0]);
            if (isConnected) {
                isConnected = false;
                EventBus.getDefault().post(DJINetWorkStatusEvent.CONNECT_LOSE);
            }
        } else {
            DJILog.i("checkNetworkStatus", "connect", new Object[0]);
            if (!isConnected) {
                if (networkInfo.getType() == 1) {
                    isNetworkOnline();
                }
                isConnected = true;
                EventBus.getDefault().post(DJINetWorkStatusEvent.CONNECT_OK);
            }
        }
        if (DJIMultiNetworkMgr.isVersionSupported()) {
            DJIMultiNetworkMgr.getInstance().checkForDisable();
        }
    }

    public static boolean isConnected() {
        return isConnected;
    }

    public static boolean isUsingSimNetwork(Context context) {
        return (getNetWorkStatus(context) && !isWifiConnected(context)) || DJIMultiNetworkMgr.getInstance().isEnabled();
    }

    public static synchronized boolean isNetworkOnline() {
        Exception e;
        boolean z = false;
        synchronized (DJINetWorkReceiver.class) {
            if (NetworkUtils.shouldAllowNetworkAccess()) {
                long nTime = System.currentTimeMillis();
                if (nTime - pingTime < 1000) {
                    z = isOnline;
                } else {
                    try {
                        if (ProcessUtils.executeCommand("ping -c 1 www.dji.com", 200) == 0) {
                            z = true;
                        }
                        isOnline = z;
                    } catch (IOException e2) {
                        e = e2;
                        e.printStackTrace();
                        isOnline = false;
                        pingTime = nTime;
                        z = isOnline;
                        return z;
                    } catch (InterruptedException e3) {
                        e = e3;
                        e.printStackTrace();
                        isOnline = false;
                        pingTime = nTime;
                        z = isOnline;
                        return z;
                    } catch (TimeoutException e4) {
                        e = e4;
                        e.printStackTrace();
                        isOnline = false;
                        pingTime = nTime;
                        z = isOnline;
                        return z;
                    }
                    pingTime = nTime;
                    z = isOnline;
                }
            }
        }
        return z;
    }

    public static boolean getNetWorkStatus(Context context) {
        NetworkInfo networkInfo = ((ConnectivityManager) context.getSystemService("connectivity")).getActiveNetworkInfo();
        if (networkInfo != null) {
            DJILog.d("getMockNetWorkStatus", "networkInfo : " + networkInfo.toString(), new Object[0]);
        } else {
            DJILog.d("getMockNetWorkStatus", "networkInfo null", new Object[0]);
        }
        if (networkInfo == null || !networkInfo.isConnected()) {
            return false;
        }
        if (DJIMultiNetworkMgr.getInstance().isEnabled()) {
            return true;
        }
        DJILinkDaemonService linkDaemonService = DJILinkDaemonService.getInstance();
        if (networkInfo.getType() == 1 && linkDaemonService != null && linkDaemonService.getLinkType() == DJILinkType.WIFI) {
            return false;
        }
        return true;
    }

    public static boolean getNetWorkStatusNoPing(Context context) {
        NetworkInfo networkInfo = ((ConnectivityManager) context.getSystemService("connectivity")).getActiveNetworkInfo();
        if (networkInfo != null) {
            DJILog.d("getMockNetWorkStatus", "networkInfo : " + networkInfo.toString(), new Object[0]);
        } else {
            DJILog.d("getMockNetWorkStatus", "networkInfo null", new Object[0]);
        }
        if (networkInfo == null || !networkInfo.isConnected()) {
            return false;
        }
        return true;
    }

    public static boolean isWifiConnected(Context context) {
        if (isNetworkConnectedByType(context, 1)) {
            DJILog.i("isWifiConnected", "connect", new Object[0]);
            return true;
        }
        DJILog.i("isWifiConnected", "unconnect", new Object[0]);
        return false;
    }

    public static boolean isNetworkConnectedByType(Context context, int networkType) {
        NetworkInfo networkInfo;
        if (Build.VERSION.SDK_INT >= 23) {
            networkInfo = getNetworkInfo_23(context, networkType);
        } else {
            networkInfo = getNetworkInfo_below23(context, networkType);
        }
        return networkInfo != null && networkInfo.isConnected();
    }

    public static boolean isNetworkConnectedOrConnectingByType(Context context, int networkType) {
        NetworkInfo networkInfo;
        if (Build.VERSION.SDK_INT >= 23) {
            networkInfo = getNetworkInfo_23(context, networkType);
        } else {
            networkInfo = getNetworkInfo_below23(context, networkType);
        }
        return networkInfo != null && networkInfo.isConnectedOrConnecting();
    }

    @Nullable
    private static NetworkInfo getNetworkInfo_below23(Context context, int networkType) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService("connectivity");
        if (connectivityManager != null) {
            return connectivityManager.getNetworkInfo(networkType);
        }
        return null;
    }

    @Nullable
    @TargetApi(23)
    private static NetworkInfo getNetworkInfo_23(Context context, int networkType) {
        Network[] networks;
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService("connectivity");
        if (!(connectivityManager == null || (networks = connectivityManager.getAllNetworks()) == null || networks.length <= 0)) {
            for (Network network : networks) {
                NetworkInfo networkInfo = connectivityManager.getNetworkInfo(network);
                if (networkInfo != null && networkInfo.getType() == networkType) {
                    return networkInfo;
                }
            }
        }
        return null;
    }

    public static boolean isMobileDataEnable(Context context) {
        ConnectivityManager connectMgr = (ConnectivityManager) context.getSystemService("connectivity");
        try {
            if (((Boolean) connectMgr.getClass().getMethod("getMobileDataEnabled", null).invoke(connectMgr, null)).booleanValue()) {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
