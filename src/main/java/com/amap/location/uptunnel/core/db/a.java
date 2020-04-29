package com.amap.location.uptunnel.core.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.amap.location.common.HeaderConfig;
import com.amap.openapi.dv;

/* compiled from: DBOpenHelper */
public class a extends SQLiteOpenHelper {
    public a(Context context) {
        super(context, HeaderConfig.getProcessName() + "_uptunnel.db", (SQLiteDatabase.CursorFactory) null, 1);
    }

    public void onCreate(SQLiteDatabase sQLiteDatabase) {
        sQLiteDatabase.execSQL("create table if not exists count (ID integer PRIMARY KEY AUTOINCREMENT NOT NULL, type integer, value integer, time long)");
        sQLiteDatabase.execSQL(dv.a("event"));
        sQLiteDatabase.execSQL(dv.a("key_log"));
        sQLiteDatabase.execSQL(dv.a("log"));
        sQLiteDatabase.execSQL(dv.a("data_block"));
    }

    public void onUpgrade(SQLiteDatabase sQLiteDatabase, int i, int i2) {
    }
}
