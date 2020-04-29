package com.dji.analytics.d;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import com.dji.analytics.DJIA;
import com.dji.analytics.ReportConfig;

/* compiled from: NetStateManager */
public class b {
    private static volatile a a;
    private static BroadcastReceiver b = new BroadcastReceiver() {
        /* class com.dji.analytics.d.b.AnonymousClass1 */

        public void onReceive(Context context, Intent intent) {
            b.b(context);
            b.a();
        }
    };

    /* compiled from: NetStateManager */
    public enum a {
        WIFI_CONN,
        MOBILE_CONN,
        LOST
    }

    public static void a(Context context) {
        if (context != null) {
            try {
                IntentFilter intentFilter = new IntentFilter();
                intentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
                context.registerReceiver(b, intentFilter);
            } catch (Exception e) {
                DJIA.log.b(DJIA.LOG_TAG, "error in register receiver " + e);
            }
        }
    }

    public static void b(Context context) {
        a aVar;
        if (context != null) {
            NetworkInfo activeNetworkInfo = ((ConnectivityManager) context.getSystemService("connectivity")).getActiveNetworkInfo();
            if (activeNetworkInfo == null || !activeNetworkInfo.isConnected()) {
                aVar = a.LOST;
            } else if (activeNetworkInfo.getType() == 1) {
                aVar = a.WIFI_CONN;
            } else {
                aVar = a.MOBILE_CONN;
            }
            a = aVar;
            if (DJIA.DEV_FLAG) {
                DJIA.log.a(DJIA.LOG_TAG, "ProbeNetworkState NetState is " + a);
            }
        }
    }

    public static void a() {
        if (a == a.WIFI_CONN || a == a.MOBILE_CONN) {
            c.a().b();
        }
    }

    public static boolean a(ReportConfig reportConfig) {
        if (reportConfig == null) {
            return false;
        }
        if (a == a.LOST) {
            if (!DJIA.DEV_FLAG) {
                return false;
            }
            DJIA.log.a(DJIA.LOG_TAG, "NetState is lost");
            return false;
        } else if (a != a.MOBILE_CONN) {
            return true;
        } else {
            if (DJIA.DEV_FLAG) {
                DJIA.log.a(DJIA.LOG_TAG, "NetState is mobile_conn");
            }
            return reportConfig.is4GEnable;
        }
    }
}
