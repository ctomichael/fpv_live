package com.amap.openapi;

import android.database.sqlite.SQLiteDatabase;
import dji.publics.protocol.ResponseBase;

/* compiled from: StatisticsClTable */
public class ca {
    public static String a = ResponseBase.STRING_ID;
    public static String b = "originid";
    public static String c = "frequency";
    public static String d = "time";
    private static final String e = ("CREATE TABLE IF NOT EXISTS CL ( " + a + " LONG PRIMARY KEY, " + b + " TEXT, " + c + " INTEGER DEFAULT 0, " + d + " LONG DEFAULT 0);");

    public static void a(SQLiteDatabase sQLiteDatabase) {
        sQLiteDatabase.execSQL(e);
    }
}
