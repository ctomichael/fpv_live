package com.amap.openapi;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import com.amap.openapi.bi.a;
import java.util.ArrayList;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/* compiled from: SaveController */
public class bi<Item extends a> {
    private Handler.Callback a = new Handler.Callback() {
        /* class com.amap.openapi.bi.AnonymousClass1 */

        public boolean handleMessage(Message message) {
            try {
                return bi.this.a(message);
            } catch (Exception e) {
                return true;
            }
        }
    };
    private int b = 0;
    private ReentrantReadWriteLock c = new ReentrantReadWriteLock();
    private b<Item> d;
    private Handler e;
    private ArrayList<Item> f = new ArrayList<>();
    private long g;
    private boolean h;

    /* compiled from: SaveController */
    public interface a {
        long a();
    }

    /* compiled from: SaveController */
    public interface b<Item extends a> {
        void a();

        void a(ArrayList<Item> arrayList);

        boolean a(long j);

        void b();

        long c();

        long d();
    }

    /* access modifiers changed from: private */
    public boolean a(Message message) {
        switch (message.what) {
            case 1:
                b((a) message.obj);
                return true;
            case 2:
                d();
                return true;
            case 3:
                c();
                return true;
            case 4:
                this.d.a();
                return true;
            default:
                return true;
        }
    }

    /* JADX INFO: finally extract failed */
    private void b(Item item) {
        this.f.add(item);
        this.g += item.a();
        if (this.g >= this.d.c()) {
            try {
                this.c.readLock().lock();
                if (this.e != null) {
                    this.e.removeMessages(2);
                }
                this.c.readLock().unlock();
                d();
            } catch (Throwable th) {
                this.c.readLock().unlock();
                throw th;
            }
        } else {
            e();
        }
    }

    private void c() {
        d();
        this.d.b();
        this.d = null;
    }

    private void d() {
        this.h = false;
        if (this.d.a(this.g)) {
            this.d.a(this.f);
        }
        this.f.clear();
        this.g = 0;
    }

    /* JADX INFO: finally extract failed */
    private void e() {
        if (!this.h) {
            try {
                this.c.readLock().lock();
                if (this.e != null) {
                    this.e.sendEmptyMessageDelayed(2, this.d.d());
                }
                this.c.readLock().unlock();
                this.h = true;
            } catch (Throwable th) {
                this.c.readLock().unlock();
                throw th;
            }
        }
    }

    public void a() {
        this.c.writeLock().lock();
        try {
            if (this.b == 1) {
                this.b = 2;
                this.e.removeCallbacksAndMessages(null);
                if (this.e.getLooper() == Looper.myLooper()) {
                    c();
                } else {
                    this.e.sendEmptyMessage(3);
                }
                this.e = null;
            }
        } finally {
            this.c.writeLock().unlock();
        }
    }

    public void a(s sVar) {
        try {
            this.c.readLock().lock();
            if (this.e != null) {
                if (this.e.getLooper() == Looper.myLooper()) {
                    b(sVar);
                } else {
                    this.e.obtainMessage(1, sVar).sendToTarget();
                }
            }
        } finally {
            this.c.readLock().unlock();
        }
    }

    public void a(b<s> bVar, Looper looper) {
        if (bVar == null || looper == null) {
            throw new RuntimeException("business 和 looper 都不能为 null");
        }
        try {
            this.c.writeLock().lock();
            if (this.b == 0) {
                this.d = bVar;
                this.e = new Handler(looper, this.a);
                if (Looper.myLooper() == looper) {
                    this.d.a();
                } else {
                    this.e.sendEmptyMessage(4);
                }
                this.b = 1;
            }
        } finally {
            this.c.writeLock().unlock();
        }
    }

    public void b() {
        try {
            this.c.readLock().lock();
            if (this.e != null) {
                if (this.e.getLooper() == Looper.myLooper()) {
                    this.e.removeMessages(2);
                    d();
                } else {
                    this.e.removeMessages(2);
                    this.e.obtainMessage(2).sendToTarget();
                }
            }
        } finally {
            this.c.readLock().unlock();
        }
    }
}
