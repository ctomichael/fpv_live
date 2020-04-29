package dji.midware.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import com.dji.frame.interfaces.V_CallBack_ReceiveData;
import dji.fieldAnnotation.EXClassNullAway;
import java.net.UnknownHostException;

@EXClassNullAway
public class WifiStateUtil {
    private static boolean isWifiConnected = false;

    public static boolean isWifiActive(Context ctx) {
        boolean z;
        boolean z2;
        ConnectivityManager cm = (ConnectivityManager) ctx.getApplicationContext().getSystemService("connectivity");
        if (cm != null) {
            try {
                if (Build.VERSION.SDK_INT < 23) {
                    NetworkInfo networkInfo = cm.getNetworkInfo(1);
                    if (networkInfo != null) {
                        z2 = networkInfo.isConnected();
                    } else {
                        z2 = false;
                    }
                    isWifiConnected = z2;
                } else {
                    for (Network n : cm.getAllNetworks()) {
                        NetworkInfo ni = cm.getNetworkInfo(n);
                        if (ni.getType() == 1) {
                            if (ni.getState() == NetworkInfo.State.CONNECTED) {
                                z = true;
                            } else {
                                z = false;
                            }
                            isWifiConnected = z;
                        }
                    }
                }
            } catch (RuntimeException e) {
                e.printStackTrace();
                isWifiConnected = false;
            }
        } else {
            isWifiConnected = false;
        }
        return isWifiConnected;
    }

    public static String getWifiSSID(Context ctx) {
        if (!isWifiActive(ctx)) {
            return "";
        }
        WifiInfo wifiInfo = ((WifiManager) ctx.getSystemService("wifi")).getConnectionInfo();
        if (wifiInfo == null) {
            return "";
        }
        return wifiInfo.getSSID().replaceAll("\"", "");
    }

    public static void getLevel(final boolean isLevel, final V_CallBack_ReceiveData callback) {
        new Thread(new Runnable() {
            /* class dji.midware.util.WifiStateUtil.AnonymousClass1 */

            public void run() {
                callback.exec(Integer.valueOf(WifiStateUtil.ping("192.168.1.1", isLevel)));
            }
        }).start();
    }

    public static void getRcLevel(final boolean isLevel, final V_CallBack_ReceiveData callback) {
        new Thread(new Runnable() {
            /* class dji.midware.util.WifiStateUtil.AnonymousClass2 */

            public void run() {
                callback.exec(Integer.valueOf(WifiStateUtil.ping("192.168.1.2", isLevel)));
            }
        }).start();
    }

    public static int ping(String ip, boolean isLevel) {
        long TotalTime = 0;
        int PingCnt = 0;
        int AvgTime = 300;
        int signalLevel = 0;
        int i = 0;
        while (true) {
            if (i >= 3) {
                break;
            }
            int status = -1;
            long LastTime = System.currentTimeMillis();
            try {
                if (ProcessUtils.isReachable(ip, 300)) {
                    status = 0;
                }
            } catch (UnknownHostException e) {
                e.printStackTrace();
            } catch (Exception e2) {
                e2.printStackTrace();
            }
            if (status != 0) {
                PingCnt = 0;
                break;
            }
            TotalTime += System.currentTimeMillis() - LastTime;
            PingCnt++;
            i++;
        }
        if (PingCnt == 3) {
            AvgTime = ((int) TotalTime) / 3;
            if (AvgTime < 100) {
                AvgTime = 0;
                signalLevel = 4;
            } else if (AvgTime < 250) {
                signalLevel = 3;
            } else if (AvgTime < 500) {
                signalLevel = 2;
            } else if (AvgTime < 1000) {
                signalLevel = 1;
            }
        }
        if (isLevel) {
            return signalLevel;
        }
        int t = (1000 - AvgTime) / 10;
        if (t < 0) {
            t = 0;
        }
        return t;
    }

    public static boolean ping(String ip, int timeOut) {
        try {
            return ProcessUtils.isReachable(ip, timeOut);
        } catch (UnknownHostException e) {
            e.printStackTrace();
            return false;
        } catch (Exception e2) {
            e2.printStackTrace();
            return false;
        }
    }
}
