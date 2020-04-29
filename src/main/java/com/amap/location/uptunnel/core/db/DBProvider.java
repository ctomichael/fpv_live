package com.amap.location.uptunnel.core.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import com.amap.location.common.database.AbstractContentProvider;
import dji.component.accountcenter.IMemberProtocol;

public class DBProvider extends AbstractContentProvider {
    public static String d = "com.amap.android.uptunnel.dbPersistent";
    private static Object e = new Object();
    private static volatile DBProvider f;
    private static Context g;
    private a h;

    private DBProvider(Context context) {
        g = context;
        onCreate();
    }

    public static Uri a(String str) {
        return Uri.parse("content://" + d + IMemberProtocol.PARAM_SEPERATOR + str);
    }

    public static DBProvider a(Context context) {
        try {
            if (f == null) {
                synchronized (e) {
                    if (f == null) {
                        f = new DBProvider(context.getApplicationContext());
                    }
                }
            }
        } catch (Throwable th) {
        }
        return f;
    }

    public String a() {
        return d;
    }

    /* JADX DEBUG: Failed to find minimal casts for resolve overloaded methods, cast all args instead
     method: com.amap.location.common.database.AbstractContentProvider.a(java.lang.Integer, java.lang.String, android.database.sqlite.SQLiteOpenHelper):void
     arg types: [int, java.lang.String, com.amap.location.uptunnel.core.db.a]
     candidates:
      com.amap.location.common.database.AbstractContentProvider.a(android.net.Uri, java.lang.String, java.lang.String[]):int
      com.amap.location.common.database.AbstractContentProvider.a(java.lang.Integer, java.lang.String, android.database.sqlite.SQLiteOpenHelper):void */
    /* access modifiers changed from: protected */
    public void b() {
        a aVar = new a(g);
        this.h = aVar;
        a((Integer) 1, "count", (SQLiteOpenHelper) aVar);
        a((Integer) 2, "event", (SQLiteOpenHelper) aVar);
        a((Integer) 3, "key_log", (SQLiteOpenHelper) aVar);
        a((Integer) 4, "log", (SQLiteOpenHelper) aVar);
        a((Integer) 5, "data_block", (SQLiteOpenHelper) aVar);
    }

    public SQLiteDatabase c() {
        try {
            return this.h.getWritableDatabase();
        } catch (Exception e2) {
            return null;
        }
    }
}
