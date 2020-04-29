package dji.midware.aoabridge;

import android.app.Application;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.wifi.WifiManager;
import dji.fieldAnnotation.EXClassNullAway;

@EXClassNullAway
public class Utils {
    public static Context getAppContext() {
        try {
            return (Application) Class.forName("android.app.ActivityThread").getMethod("currentApplication", new Class[0]).invoke(null, null);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static boolean isAppInstalled(String packageName) {
        Context ctx = getAppContext();
        if (packageName == null || packageName.isEmpty()) {
            return false;
        }
        try {
            if (ctx.getPackageManager().getApplicationInfo(packageName, 8192) != null) {
                return true;
            }
            return false;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }

    public static String getIp() {
        WifiManager wifiManager = (WifiManager) getAppContext().getApplicationContext().getSystemService("wifi");
        if (!wifiManager.isWifiEnabled()) {
            return null;
        }
        return intToIp(wifiManager.getConnectionInfo().getIpAddress());
    }

    private static String intToIp(int i) {
        return (i & 255) + "." + ((i >> 8) & 255) + "." + ((i >> 16) & 255) + "." + ((i >> 24) & 255);
    }
}
