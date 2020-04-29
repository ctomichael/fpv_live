package dji.midware.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import dji.fieldAnnotation.EXClassNullAway;
import dji.log.DJILog;
import dji.midware.encryption.util.LDMPlusEngine;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.concurrent.atomic.AtomicBoolean;

@EXClassNullAway
public class NetworkUtils {
    private static final String BASE_URL = "developer.dji.com";
    /* access modifiers changed from: private */
    public static final String TAG = NetworkUtils.class.getSimpleName();
    /* access modifiers changed from: private */
    public static AtomicBoolean isChecking = new AtomicBoolean(false);
    private static AtomicBoolean isLDMEnabled = new AtomicBoolean(false);
    /* access modifiers changed from: private */
    public static AtomicBoolean isOnline;

    private static void checkIsDJIBlocked() {
        if (isChecking.compareAndSet(false, true)) {
            new Thread(new Runnable() {
                /* class dji.midware.util.NetworkUtils.AnonymousClass1 */

                public void run() {
                    boolean isDJIBlocked = false;
                    try {
                        InetAddress nodeJSUrl = InetAddress.getByName(NetworkUtils.BASE_URL);
                        if (nodeJSUrl.isLoopbackAddress() || nodeJSUrl.isAnyLocalAddress()) {
                            isDJIBlocked = true;
                        } else {
                            isDJIBlocked = false;
                        }
                        if (isDJIBlocked) {
                            DJILog.i(NetworkUtils.TAG, "AdBlocker is enabled. Won't be able to use Remote services.", new Object[0]);
                        }
                    } catch (UnknownHostException e) {
                        DJILog.e(NetworkUtils.TAG, "Failed checkIsDJIBlocked " + e.getMessage(), new Object[0]);
                    }
                    if (NetworkUtils.isOnline.get() && isDJIBlocked) {
                        NetworkUtils.isOnline.set(false);
                    }
                    NetworkUtils.isChecking.set(false);
                }
            }, "netUtil").start();
        }
    }

    public static void setLDMEnabled(boolean isLDMEnabled2) {
        isLDMEnabled.set(isLDMEnabled2);
        refreshConnectivity();
    }

    public static boolean isLDMEnabled() {
        return isLDMEnabled.get();
    }

    public static boolean isOnline() {
        if (isOnline == null) {
            isOnline = new AtomicBoolean(false);
            refreshConnectivity();
        }
        return isOnline.get();
    }

    public static void refreshConnectivity() {
        boolean isNetworkAvailable;
        Context context = ContextUtil.getContext();
        if (isOnline == null) {
            isOnline = new AtomicBoolean(false);
        }
        if (!shouldAllowNetworkAccess()) {
            isOnline.set(false);
        } else if (context == null) {
            isOnline.set(false);
        } else {
            try {
                NetworkInfo netInfo = ((ConnectivityManager) context.getSystemService("connectivity")).getActiveNetworkInfo();
                if (netInfo == null || !netInfo.isConnectedOrConnecting()) {
                    isNetworkAvailable = false;
                } else {
                    isNetworkAvailable = true;
                }
            } catch (SecurityException e) {
                isNetworkAvailable = true;
                DJILog.i(TAG, "Don't have permission to check connectivity, will assume we are online", new Object[0]);
            }
            isOnline.set(isNetworkAvailable);
            if (isNetworkAvailable) {
                checkIsDJIBlocked();
            }
        }
    }

    public static boolean shouldAllowNetworkAccess() {
        return !LDMPlusEngine.getInstance().isLDMPlusEnabled() && !isLDMEnabled.get();
    }
}
