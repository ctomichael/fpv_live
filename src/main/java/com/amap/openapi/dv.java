package com.amap.openapi;

/* compiled from: BinaryTable */
public class dv {
    public static final String[] a = {"ID", "type", "value", "time", "size"};

    public static String a(String str) {
        return "create table if not exists " + str + " (ID integer PRIMARY KEY AUTOINCREMENT NOT NULL, type integer, value blob, time long, size integer);";
    }
}
