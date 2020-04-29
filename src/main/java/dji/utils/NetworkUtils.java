package dji.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.text.TextUtils;
import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.LinkedList;

public class NetworkUtils {

    public enum NetworkType {
        NETWORK_ETHERNET,
        NETWORK_WIFI,
        NETWORK_4G,
        NETWORK_3G,
        NETWORK_2G,
        NETWORK_UNKNOWN,
        NETWORK_NO
    }

    public static NetworkType getNetworkType(Context context) {
        NetworkType netType = NetworkType.NETWORK_NO;
        NetworkInfo info = getActiveNetworkInfo(context);
        if (info == null || !info.isAvailable()) {
            return netType;
        }
        if (info.getType() == 9) {
            return NetworkType.NETWORK_ETHERNET;
        }
        if (info.getType() == 1) {
            return NetworkType.NETWORK_WIFI;
        }
        if (info.getType() != 0) {
            return NetworkType.NETWORK_UNKNOWN;
        }
        switch (info.getSubtype()) {
            case 1:
            case 2:
            case 4:
            case 7:
            case 11:
            case 16:
                return NetworkType.NETWORK_2G;
            case 3:
            case 5:
            case 6:
            case 8:
            case 9:
            case 10:
            case 12:
            case 14:
            case 15:
            case 17:
                return NetworkType.NETWORK_3G;
            case 13:
            case 18:
                return NetworkType.NETWORK_4G;
            default:
                String subtypeName = info.getSubtypeName();
                if (subtypeName.equalsIgnoreCase("TD-SCDMA") || subtypeName.equalsIgnoreCase("WCDMA") || subtypeName.equalsIgnoreCase("CDMA2000")) {
                    return NetworkType.NETWORK_3G;
                }
                return NetworkType.NETWORK_UNKNOWN;
        }
    }

    public static boolean isNetworkConnected(Context context) {
        NetworkInfo networkInfo = getActiveNetworkInfo(context);
        if (Build.VERSION.SDK_INT < 23 || networkInfo == null || networkInfo.getType() != 1) {
            if (networkInfo == null || !networkInfo.isConnected()) {
                return false;
            }
            return true;
        } else if (networkInfo.getState() == NetworkInfo.State.CONNECTED) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean isWifiConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getApplicationContext().getSystemService("connectivity");
        if (cm != null) {
            try {
                if (Build.VERSION.SDK_INT < 22) {
                    NetworkInfo networkInfo = cm.getNetworkInfo(1);
                    if (networkInfo == null || !networkInfo.isConnected()) {
                        return false;
                    }
                    return true;
                }
                Network[] networks = cm.getAllNetworks();
                int length = networks.length;
                int i = 0;
                while (i < length) {
                    NetworkInfo ni = cm.getNetworkInfo(networks[i]);
                    if (ni == null || ni.getType() != 1) {
                        i++;
                    } else if (ni.getState() != NetworkInfo.State.CONNECTED) {
                        return false;
                    } else {
                        return true;
                    }
                }
            } catch (RuntimeException e) {
                e.printStackTrace();
                return false;
            }
        }
        return false;
    }

    public static boolean isWifiAvailable(Context context, String ip) {
        return isWifiConnected(context) && isAvailableByPing(ip);
    }

    public static String getWifiSSID(Context context) {
        if (!isWifiConnected(context)) {
            return "";
        }
        WifiInfo wifiInfo = ((WifiManager) context.getApplicationContext().getSystemService("wifi")).getConnectionInfo();
        if (wifiInfo == null) {
            return "";
        }
        return wifiInfo.getSSID().replaceAll("\"", "");
    }

    public static boolean isUsingMobileData(Context context) {
        NetworkInfo info = getActiveNetworkInfo(context);
        return info != null && info.isAvailable() && info.getType() == 0;
    }

    public static boolean is4GConnected(Context context) {
        NetworkInfo info = getActiveNetworkInfo(context);
        return info != null && info.isAvailable() && info.getSubtype() == 13;
    }

    public static boolean isAvailableByPing(String ip) {
        if (TextUtils.isEmpty(ip)) {
            ip = "223.5.5.5";
        }
        try {
            if (Runtime.getRuntime().exec("ping -c 3 -w 100 " + ip).waitFor() == 0) {
                return true;
            }
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } catch (InterruptedException e2) {
            e2.printStackTrace();
            return false;
        }
    }

    public static boolean isAvailabeleByPing() {
        return isAvailableByPing(null);
    }

    private static NetworkInfo getActiveNetworkInfo(Context context) {
        ConnectivityManager cm;
        if (context != null) {
            cm = (ConnectivityManager) context.getApplicationContext().getSystemService("connectivity");
        } else {
            cm = (ConnectivityManager) AppUtils.getApp().getSystemService("connectivity");
        }
        if (cm == null) {
            return null;
        }
        return cm.getActiveNetworkInfo();
    }

    public static String getIPAddress(boolean useIPv4) {
        boolean isIPv4;
        String hostAddress;
        try {
            Enumeration<NetworkInterface> nis = NetworkInterface.getNetworkInterfaces();
            LinkedList<InetAddress> adds = new LinkedList<>();
            while (nis.hasMoreElements()) {
                NetworkInterface ni = nis.nextElement();
                if (ni.isUp() && !ni.isLoopback()) {
                    Enumeration<InetAddress> addresses = ni.getInetAddresses();
                    while (addresses.hasMoreElements()) {
                        adds.addFirst(addresses.nextElement());
                    }
                }
            }
            Iterator it2 = adds.iterator();
            while (it2.hasNext()) {
                InetAddress add = (InetAddress) it2.next();
                if (!add.isLoopbackAddress()) {
                    String hostAddress2 = add.getHostAddress();
                    if (hostAddress2.indexOf(58) < 0) {
                        isIPv4 = true;
                    } else {
                        isIPv4 = false;
                    }
                    if (useIPv4) {
                        if (isIPv4) {
                            return hostAddress2;
                        }
                    } else if (!isIPv4) {
                        int index = hostAddress2.indexOf(37);
                        if (index < 0) {
                            hostAddress = hostAddress2.toUpperCase();
                        } else {
                            hostAddress = hostAddress2.substring(0, index).toUpperCase();
                        }
                        return hostAddress;
                    }
                }
            }
        } catch (SocketException e) {
            e.printStackTrace();
        }
        return "";
    }
}
