package com.amap.location.common.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.TextUtils;
import com.amap.location.common.model.CellStatus;
import java.util.List;
import kotlin.jvm.internal.LongCompanionObject;

/* compiled from: NetUtil */
public class f {
    public static int a(Context context) {
        try {
            NetworkInfo activeNetworkInfo = ((ConnectivityManager) context.getSystemService("connectivity")).getActiveNetworkInfo();
            if (activeNetworkInfo != null && activeNetworkInfo.isConnected()) {
                int type = activeNetworkInfo.getType();
                if (type == 1 || type == 0) {
                    return type;
                }
            }
        } catch (Throwable th) {
        }
        return -1;
    }

    public static long a(String str) {
        if (TextUtils.isEmpty(str)) {
            return 0;
        }
        long j = 0;
        int i = 0;
        for (int length = str.length() - 1; length >= 0; length--) {
            long charAt = (long) str.charAt(length);
            if (charAt >= 48 && charAt <= 57) {
                j += (charAt - 48) << i;
                i += 4;
            } else if (charAt >= 97 && charAt <= 102) {
                j += ((charAt - 97) + 10) << i;
                i += 4;
            } else if (charAt >= 65 && charAt <= 70) {
                j += ((charAt - 65) + 10) << i;
                i += 4;
            } else if (!(charAt == 58 || charAt == 124)) {
                return 0;
            }
        }
        if (i != 48) {
            return 0;
        }
        return j;
    }

    /* JADX DEBUG: Failed to find minimal casts for resolve overloaded methods, cast all args instead
     method: com.amap.location.common.util.b.a(long, int, boolean):byte[]
     arg types: [long, int, int]
     candidates:
      com.amap.location.common.util.b.a(int, int, int):int
      com.amap.location.common.util.b.a(long, long, long):long
      com.amap.location.common.util.b.a(byte[], java.lang.String, java.lang.String):java.lang.String
      com.amap.location.common.util.b.a(java.lang.String, java.lang.String, java.lang.String):byte[]
      com.amap.location.common.util.b.a(long, int, boolean):byte[] */
    public static String a(long j) {
        if (j < 0 || j > 281474976710655L) {
            return null;
        }
        return b.a(b.a(j, 6, true), (String) null, ":");
    }

    public static void a(CellStatus.HistoryCell historyCell, List<CellStatus.HistoryCell> list, int i) {
        if (historyCell != null && list != null) {
            int size = list.size();
            if (size == 0) {
                list.add(historyCell);
                return;
            }
            long j = LongCompanionObject.MAX_VALUE;
            int i2 = 0;
            int i3 = -1;
            while (true) {
                if (i2 >= size) {
                    break;
                }
                CellStatus.HistoryCell historyCell2 = list.get(i2);
                if (!historyCell.equals(historyCell2)) {
                    j = Math.min(j, historyCell2.lastUpdateTimeMills);
                    int i4 = j == historyCell2.lastUpdateTimeMills ? i2 : i3;
                    i2++;
                    i3 = i4;
                } else if (historyCell.rssi != historyCell2.rssi) {
                    historyCell2.lastUpdateTimeMills = historyCell.lastUpdateTimeMills;
                    historyCell2.rssi = historyCell.rssi;
                    i3 = -1;
                } else {
                    i3 = -1;
                }
            }
            if (i3 < 0) {
                return;
            }
            if (size < i) {
                list.add(historyCell);
            } else if (historyCell.lastUpdateTimeMills > j && i3 < size) {
                list.remove(i3);
                list.add(historyCell);
            }
        }
    }

    public static boolean a(int i) {
        return i >= 0 && i <= 65535;
    }

    public static boolean b(int i) {
        return i >= 0 && i <= 268435455;
    }

    public static boolean c(int i) {
        return i > 0 && i <= 32767;
    }

    public static boolean d(int i) {
        return i >= 0 && i <= 65535;
    }

    public static boolean e(int i) {
        return i >= 0 && i <= 65535;
    }
}
