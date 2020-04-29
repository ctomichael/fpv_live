package com.amap.openapi;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/* compiled from: StatisticsDbOpenHelper */
public class cb extends SQLiteOpenHelper {
    cb(Context context) {
        super(context, "OffStatistics.db", (SQLiteDatabase.CursorFactory) null, 1);
    }

    public void onCreate(SQLiteDatabase sQLiteDatabase) {
        bz.a(sQLiteDatabase);
        ca.a(sQLiteDatabase);
    }

    public void onUpgrade(SQLiteDatabase sQLiteDatabase, int i, int i2) {
    }
}
