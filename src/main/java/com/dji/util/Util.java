package com.dji.util;

import android.annotation.TargetApi;
import android.app.Application;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import com.dji.api.base.IHttpApi;
import com.dji.config.ApiConfig;
import com.dji.fieldAnnotation.EXClassNullAway;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.TimeoutException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@EXClassNullAway
public class Util {
    public static String findHost(String domain) {
        if (domain == null || domain.trim().equals("")) {
            return "";
        }
        Matcher matcher = Pattern.compile("(?<=\\.)((\\w)+\\.)+\\w+").matcher(domain);
        if (matcher.find()) {
            return matcher.group();
        }
        return "";
    }

    public static String generateUrl(String domain, String testDomain, String... route) {
        StringBuilder sb = new StringBuilder();
        if (!ApiConfig.getConfig().isDebug() || TextUtils.isEmpty(testDomain)) {
            sb.append(domain);
        } else {
            sb.append(testDomain);
        }
        if (route != null && route.length > 0) {
            for (String str : route) {
                sb.append(IHttpApi.PARAM_SEPARATOR).append(str);
            }
        }
        return sb.toString();
    }

    public static String generateUrl(String domain, String... route) {
        return generateUrl(domain, domain, route);
    }

    public static String getDomain(String domain, String testDomain) {
        return (!ApiConfig.getConfig().isDebug() || TextUtils.isEmpty(testDomain)) ? domain : testDomain;
    }

    public static boolean shouldEnableDebugMode(Context context) {
        if (!isOfficeWifi(context)) {
            return false;
        }
        int returnVal = -1;
        try {
            returnVal = executeCommand("ping -c 1 -t 10 portal.djicorp.com", 50);
        } catch (IOException | InterruptedException | TimeoutException e) {
        }
        if (returnVal == 0) {
            return true;
        }
        return false;
    }

    private static boolean isOfficeWifi(Context context) {
        WifiManager wifiManager;
        WifiInfo wifiInfo;
        if (!isWifiConnected(context) || (wifiManager = (WifiManager) context.getApplicationContext().getSystemService("wifi")) == null || (wifiInfo = wifiManager.getConnectionInfo()) == null) {
            return false;
        }
        return "\"AP-Office-Se\"".equals(wifiInfo.getSSID());
    }

    private static boolean isWifiConnected(Context context) {
        if (Build.VERSION.SDK_INT >= 23) {
            return isWifiConnected_23(context);
        }
        return isWifiConnected_below23(context);
    }

    private static boolean isWifiConnected_below23(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService("connectivity");
        if (connectivityManager == null) {
            return false;
        }
        NetworkInfo wifiNetInfo = connectivityManager.getNetworkInfo(1);
        if (wifiNetInfo == null || !wifiNetInfo.isConnected()) {
            return false;
        }
        return true;
    }

    @TargetApi(23)
    private static boolean isWifiConnected_23(Context context) {
        Network[] networks;
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService("connectivity");
        if (connectivityManager == null || (networks = connectivityManager.getAllNetworks()) == null || networks.length <= 0) {
            return false;
        }
        for (Network network : networks) {
            NetworkInfo networkInfo = connectivityManager.getNetworkInfo(network);
            if (networkInfo != null && networkInfo.getType() == 1) {
                return networkInfo.isConnected();
            }
        }
        return false;
    }

    private static int executeCommand(String command, long timeout) throws IOException, InterruptedException, TimeoutException {
        Process process = Runtime.getRuntime().exec(command);
        Worker worker = new Worker(process);
        worker.start();
        try {
            worker.join(timeout);
            if (worker.exit != null) {
                int intValue = worker.exit.intValue();
                process.destroy();
                return intValue;
            }
            throw new TimeoutException();
        } catch (InterruptedException ex) {
            worker.interrupt();
            Thread.currentThread().interrupt();
            throw ex;
        } catch (Throwable th) {
            process.destroy();
            throw th;
        }
    }

    private static class Worker extends Thread {
        /* access modifiers changed from: private */
        @Nullable
        public Integer exit;
        private final Process process;

        private Worker(Process process2) {
            this.process = process2;
        }

        public void run() {
            try {
                this.exit = Integer.valueOf(this.process.waitFor());
            } catch (InterruptedException e) {
            }
        }
    }

    public static Application getApplication() throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        return (Application) Class.forName("android.app.ActivityThread").getMethod("currentApplication", new Class[0]).invoke(null, null);
    }
}
