package com.amap.openapi;

import android.text.TextUtils;
import com.amap.location.common.model.CellState;
import com.amap.location.common.model.CellStatus;
import com.amap.location.common.util.f;
import com.amap.location.security.Core;

/* compiled from: EncryptUtil */
public class cn {
    public static long a(long j) {
        try {
            return Core.encMac(f.a(j));
        } catch (Throwable th) {
            return -1;
        }
    }

    public static long a(String str) {
        if (TextUtils.isEmpty(str)) {
            return -1;
        }
        try {
            return Core.encMac(str);
        } catch (Throwable th) {
            return -1;
        }
    }

    public static String a(CellStatus cellStatus) {
        if (cellStatus == null || cellStatus.mainCell == null) {
            return "";
        }
        CellState cellState = cellStatus.mainCell;
        return cellState.type == 2 ? cellState.sid + ":" + cellState.nid + ":" + cellState.bid : (cellState.type == 0 || cellState.mcc == 0 || cellState.mcc == 65535) ? "" : cellState.mcc + ":" + cellState.mnc + ":" + cellState.lac + ":" + cellState.cid;
    }
}
