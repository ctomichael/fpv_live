package com.loc;

import android.util.Log;
import dji.component.accountcenter.IMemberProtocol;

final class dk {
    private static boolean a = false;
    private static int b = -1;

    private static String a() {
        if (b == -1) {
            StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
            int length = stackTrace.length;
            int i = 0;
            int i2 = 0;
            while (true) {
                if (i >= length) {
                    break;
                } else if (stackTrace[i].getMethodName().equals("getTraceInfo")) {
                    b = i2 + 1;
                    break;
                } else {
                    i++;
                    i2++;
                }
            }
        }
        StackTraceElement stackTraceElement = Thread.currentThread().getStackTrace()[b + 1];
        return stackTraceElement.getFileName() + ":" + stackTraceElement.getLineNumber() + " - [" + stackTraceElement.getMethodName() + IMemberProtocol.STRING_SEPERATOR_RIGHT;
    }

    static void a(String str) {
        if (a && str != null) {
            Log.d("HttpDnsSDK", Thread.currentThread().getId() + " - " + a() + " - " + str);
        }
    }

    static void a(Throwable th) {
        if (a) {
            th.printStackTrace();
        }
    }

    static void b(String str) {
        if (a && str != null) {
            Log.e("HttpDnsSDK", Thread.currentThread().getId() + " - " + a() + " - " + str);
        }
    }
}
