package com.amap.openapi;

import android.database.sqlite.SQLiteDatabase;
import dji.publics.protocol.ResponseBase;

/* compiled from: EventTable */
public class cm {
    public static String a = ResponseBase.STRING_ID;
    public static String b = "frequency";
    private static final String c = ("CREATE TABLE IF NOT EXISTS ACL ( " + a + " TEXT PRIMARY KEY, " + b + " INTEGER DEFAULT 0);");

    public static void a(SQLiteDatabase sQLiteDatabase) {
        sQLiteDatabase.execSQL(c);
    }
}
