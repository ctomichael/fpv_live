package com.amap.openapi;

import android.content.Context;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/* compiled from: UpTunnel */
public class dl {
    public static boolean a = false;
    private static du b;
    private static final ReentrantReadWriteLock c = new ReentrantReadWriteLock();

    public static void a() {
        try {
            c.writeLock().lock();
            if (b != null) {
                b.a();
                b = null;
            }
        } finally {
            c.writeLock().unlock();
        }
    }

    public static void a(int i) {
        try {
            c.readLock().lock();
            if (b != null) {
                b.a(i);
            }
        } finally {
            c.readLock().unlock();
        }
    }

    public static void a(int i, byte[] bArr) {
        if (bArr != null && bArr.length != 0 && bArr.length < 400000) {
            try {
                c.readLock().lock();
                if (b != null) {
                    b.a(i, bArr);
                }
            } finally {
                c.readLock().unlock();
            }
        }
    }

    public static void a(Context context, dk dkVar) {
        try {
            c.writeLock().lock();
            if (b == null) {
                b = new du(context.getApplicationContext(), dkVar);
            }
        } finally {
            c.writeLock().unlock();
        }
    }
}
