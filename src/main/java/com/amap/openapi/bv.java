package com.amap.openapi;

import android.database.sqlite.SQLiteDatabase;
import dji.publics.protocol.ResponseBase;

/* compiled from: LocationApTable */
public class bv {
    public static String a = ResponseBase.STRING_ID;
    public static String b = ResponseBase.STRING_LAT;
    public static String c = ResponseBase.STRING_LNG;
    public static String d = "acc";
    public static String e = "conf";
    public static String f = "timestamp";
    public static String g = "frequency";
    private static final String h = ("CREATE TABLE IF NOT EXISTS AP ( " + a + " LONG PRIMARY KEY, " + b + " INTEGER, " + c + " INTEGER, " + d + " INTEGER, " + e + " INTEGER, " + f + " LONG, " + g + " INTEGER DEFAULT 0);");

    public static void a(SQLiteDatabase sQLiteDatabase) {
        sQLiteDatabase.execSQL(h);
    }
}
