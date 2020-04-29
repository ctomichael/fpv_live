package com.loc;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ConcurrentSkipListSet;

final class dg {
    private static dg a = new dg();
    private static ConcurrentMap b;
    private static ConcurrentSkipListSet c;

    private dg() {
        b = new ConcurrentHashMap();
        c = new ConcurrentSkipListSet();
    }

    static dg a() {
        return a;
    }

    static dh a(String str) {
        return (dh) b.get(str);
    }

    static void a(String str, dh dhVar) {
        b.put(str, dhVar);
    }

    static int b() {
        return b.size();
    }

    static boolean b(String str) {
        return c.contains(str);
    }

    static void c() {
        b.clear();
        c.clear();
    }

    static void c(String str) {
        c.add(str);
    }

    static ArrayList d() {
        return new ArrayList(b.keySet());
    }

    static void d(String str) {
        c.remove(str);
    }
}
