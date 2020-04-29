package com.amap.location.offline;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.amap.location.common.model.AmapLoc;
import com.amap.location.common.model.FPS;
import com.amap.openapi.co;

public class OfflineProvider extends ContentProvider {
    private Context a;
    private OfflineManager b;

    private synchronized void a() {
        if (this.b == null) {
            this.b = OfflineManager.getInstance();
            this.b.init(this.a, new OfflineConfig(), new a());
        }
    }

    public int delete(@NonNull Uri uri, @Nullable String str, @Nullable String[] strArr) {
        return 0;
    }

    @Nullable
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        return null;
    }

    public boolean onCreate() {
        synchronized (this) {
            this.a = getContext().getApplicationContext();
        }
        return true;
    }

    /* JADX DEBUG: Failed to find minimal casts for resolve overloaded methods, cast all args instead
     method: com.amap.openapi.co.a(boolean, com.amap.location.common.model.AmapLoc):android.database.Cursor
     arg types: [int, ?[OBJECT, ARRAY]]
     candidates:
      com.amap.openapi.co.a(java.lang.String, java.lang.String):com.amap.location.common.model.FPS
      com.amap.openapi.co.a(boolean, com.amap.location.common.model.AmapLoc):android.database.Cursor */
    /* JADX DEBUG: Failed to find minimal casts for resolve overloaded methods, cast all args instead
     method: com.amap.openapi.co.a(boolean, com.amap.location.common.model.AmapLoc):android.database.Cursor
     arg types: [int, com.amap.location.common.model.AmapLoc]
     candidates:
      com.amap.openapi.co.a(java.lang.String, java.lang.String):com.amap.location.common.model.FPS
      com.amap.openapi.co.a(boolean, com.amap.location.common.model.AmapLoc):android.database.Cursor */
    @Nullable
    public Cursor query(@NonNull Uri uri, @Nullable String[] strArr, @Nullable String str, @Nullable String[] strArr2, @Nullable String str2) {
        int i;
        AmapLoc amapLoc = null;
        a();
        if (!this.b.isEnable()) {
            return co.a(false, (AmapLoc) null);
        }
        if (strArr2 == null || strArr2.length < 5) {
            return co.a(true, (AmapLoc) null);
        }
        String str3 = strArr2[0];
        FPS a2 = co.a(strArr2[1], strArr2[2]);
        try {
            i = Integer.parseInt(strArr2[4]);
        } catch (Exception e) {
            i = 0;
        }
        if (a2 != null) {
            amapLoc = this.b.getLocation(a2, i, false, str3);
        }
        return co.a(true, amapLoc);
    }

    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String str, @Nullable String[] strArr) {
        a();
        if (!this.b.isEnable()) {
            return 0;
        }
        if (strArr == null || strArr.length < 5) {
            return 1;
        }
        String str2 = strArr[0];
        FPS a2 = co.a(strArr[1], strArr[2]);
        AmapLoc a3 = co.a(strArr[3]);
        if (!(a2 == null || a3 == null)) {
            this.b.correctLocation(a2, a3, str2);
        }
        return 1;
    }
}
