package com.amap.openapi;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/* compiled from: LocationDbOpenHelper */
class bx extends SQLiteOpenHelper {
    bx(Context context) {
        super(context, "OffLocation.db", (SQLiteDatabase.CursorFactory) null, 1);
    }

    public void onCreate(SQLiteDatabase sQLiteDatabase) {
        bv.a(sQLiteDatabase);
        bw.a(sQLiteDatabase);
    }

    public void onUpgrade(SQLiteDatabase sQLiteDatabase, int i, int i2) {
    }
}
