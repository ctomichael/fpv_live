package dji.publics.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import dji.midware.data.manager.P3.DJIProductManager;
import dji.midware.data.manager.P3.ServiceManager;

public class NetworkUtils {
    public static final int NETWORN_2G = 2;
    public static final int NETWORN_3G = 3;
    public static final int NETWORN_4G = 4;
    public static final int NETWORN_MOBILE = 5;
    public static final int NETWORN_NONE = 0;
    public static final int NETWORN_WIFI = 1;

    public static boolean checkNetAvaiable(Context context) {
        NetworkInfo info = ((ConnectivityManager) context.getSystemService("connectivity")).getActiveNetworkInfo();
        if (info == null || !info.isAvailable()) {
            return false;
        }
        if (!DJIProductManager.getInstance().getType().isFromWifi() || !ServiceManager.getInstance().isConnected()) {
            return true;
        }
        return false;
    }

    public static int getNetworkState(Context context) {
        NetworkInfo activeNetInfo;
        NetworkInfo.State state;
        ConnectivityManager connManager = (ConnectivityManager) context.getSystemService("connectivity");
        if (connManager == null || (activeNetInfo = connManager.getActiveNetworkInfo()) == null || !activeNetInfo.isAvailable()) {
            return 0;
        }
        NetworkInfo wifiInfo = connManager.getNetworkInfo(1);
        if (wifiInfo != null && (state = wifiInfo.getState()) != null && (state == NetworkInfo.State.CONNECTED || state == NetworkInfo.State.CONNECTING)) {
            return 1;
        }
        NetworkInfo networkInfo = connManager.getNetworkInfo(0);
        if (networkInfo == null) {
            return 0;
        }
        NetworkInfo.State state2 = networkInfo.getState();
        String strSubTypeName = networkInfo.getSubtypeName();
        if (state2 == null) {
            return 0;
        }
        if (state2 != NetworkInfo.State.CONNECTED && state2 != NetworkInfo.State.CONNECTING) {
            return 0;
        }
        switch (activeNetInfo.getSubtype()) {
            case 1:
            case 2:
            case 4:
            case 7:
            case 11:
                return 2;
            case 3:
            case 5:
            case 6:
            case 8:
            case 9:
            case 10:
            case 12:
            case 14:
            case 15:
                return 3;
            case 13:
                return 4;
            default:
                if (strSubTypeName.equalsIgnoreCase("TD-SCDMA") || strSubTypeName.equalsIgnoreCase("WCDMA") || strSubTypeName.equalsIgnoreCase("CDMA2000")) {
                    return 3;
                }
                return 5;
        }
    }
}
