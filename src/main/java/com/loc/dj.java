package com.loc;

final class dj {
    static String a;
    static final String[] b = new String[0];

    static synchronized void a(String str) {
        synchronized (dj.class) {
            a = str;
        }
    }
}
