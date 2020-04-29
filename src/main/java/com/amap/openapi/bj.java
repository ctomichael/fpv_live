package com.amap.openapi;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.SystemClock;
import com.amap.location.common.util.f;
import java.lang.ref.WeakReference;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/* compiled from: UploadController */
public class bj {
    private int a = 0;
    private ReentrantReadWriteLock b = new ReentrantReadWriteLock();
    private int c = 0;
    /* access modifiers changed from: private */
    public Context d;
    private Handler e;
    private a f;
    private long g;
    private b h;
    private int i;
    private Executor j;
    private Handler.Callback k = new Handler.Callback() {
        /* class com.amap.openapi.bj.AnonymousClass1 */

        public boolean handleMessage(Message message) {
            try {
                return bj.this.a(message);
            } catch (Exception e) {
                bj.this.e();
                return true;
            }
        }
    };

    /* compiled from: UploadController */
    public interface a {
        Object a(long j);

        void a();

        void a(int i);

        void a(int i, Object obj);

        boolean a(Object obj);

        void b();

        void b(Object obj);

        boolean b(int i);

        long c();

        long c(int i);

        int d();

        long d(int i);

        long e();

        int f();

        void g();

        Executor h();
    }

    /* compiled from: UploadController */
    static class b implements Runnable {
        WeakReference<a> a;
        WeakReference<bj> b;
        volatile boolean c;
        Object d;
        final int e;

        b(bj bjVar, a aVar, Object obj, int i) {
            this.b = new WeakReference<>(bjVar);
            this.a = new WeakReference<>(aVar);
            this.d = obj;
            this.e = i;
        }

        public void a() {
            this.c = true;
        }

        /* JADX DEBUG: Failed to find minimal casts for resolve overloaded methods, cast all args instead
         method: com.amap.openapi.bj.a(com.amap.openapi.bj, com.amap.openapi.bj$b, boolean):void
         arg types: [com.amap.openapi.bj, com.amap.openapi.bj$b, int]
         candidates:
          com.amap.openapi.bj.a(android.content.Context, com.amap.openapi.bj$a, android.os.Looper):void
          com.amap.openapi.bj.a(com.amap.openapi.bj, com.amap.openapi.bj$b, boolean):void */
        public void run() {
            boolean z;
            if (!this.c) {
                bj bjVar = this.b.get();
                a aVar = this.a.get();
                if (bjVar != null && aVar != null) {
                    if (f.a(bjVar.d) < this.e) {
                        bjVar.a(this, false);
                        return;
                    }
                    try {
                        z = aVar.a(this.d);
                    } catch (Throwable th) {
                        z = false;
                    }
                    if (!this.c) {
                        bjVar.a(this, z);
                    }
                }
            }
        }
    }

    /* access modifiers changed from: private */
    public void a(b bVar, boolean z) {
        try {
            this.b.readLock().lock();
            if (this.e != null) {
                this.e.obtainMessage(z ? 103 : 104, bVar).sendToTarget();
            }
        } finally {
            this.b.readLock().unlock();
        }
    }

    /* JADX INFO: finally extract failed */
    /* access modifiers changed from: private */
    public boolean a(Message message) {
        switch (message.what) {
            case 101:
                if (message.arg1 == 1) {
                    this.i = 0;
                }
                c();
                break;
            case 102:
                this.f.a(this.h.e, this.h.d);
                this.h.a();
                this.h = null;
                this.i++;
                this.f.a(-1);
                e();
                break;
            case 103:
                b bVar = (b) message.obj;
                if (message.obj == this.h) {
                    this.h = null;
                    try {
                        this.b.readLock().lock();
                        if (this.e != null) {
                            this.e.removeMessages(102);
                        }
                        this.b.readLock().unlock();
                        this.f.a(bVar.e, bVar.d);
                        this.f.b(bVar.d);
                        this.f.a(1);
                        c();
                        break;
                    } catch (Throwable th) {
                        this.b.readLock().unlock();
                        throw th;
                    }
                }
                break;
            case 104:
                if (message.obj == this.h) {
                    this.f.a(this.h.e, this.h.d);
                    this.h = null;
                    try {
                        this.b.readLock().lock();
                        if (this.e != null) {
                            this.e.removeMessages(102);
                        }
                        this.b.readLock().unlock();
                        this.i++;
                        this.f.a(0);
                        e();
                        break;
                    } catch (Throwable th2) {
                        this.b.readLock().unlock();
                        throw th2;
                    }
                }
                break;
            case 105:
                this.f.a();
                break;
            case 106:
                b();
                break;
        }
        return true;
    }

    private void b() {
        this.f.b();
        if (this.h != null) {
            this.h.a();
        }
        if (this.c == 2) {
            ((ExecutorService) this.j).shutdown();
        }
        this.j = null;
        this.f = null;
        this.h = null;
    }

    private void c() {
        if (this.h == null) {
            int a2 = f.a(this.d);
            if (a2 == -1) {
                e();
            } else if (!this.f.b(a2)) {
                e();
            } else {
                long c2 = this.f.c(a2);
                if (c2 <= 0) {
                    e();
                    return;
                }
                long c3 = this.f.c();
                if (c3 <= 0) {
                    e();
                    return;
                }
                long min = Math.min(this.f.d(a2), c2);
                if (c3 >= min || SystemClock.elapsedRealtime() - this.g >= this.f.e()) {
                    Object a3 = this.f.a(min);
                    if (a3 == null) {
                        e();
                        return;
                    }
                    this.g = SystemClock.elapsedRealtime();
                    if (a2 != f.a(this.d)) {
                        this.f.g();
                        e();
                        return;
                    }
                    try {
                        this.b.readLock().lock();
                        if (this.e != null) {
                            this.h = new b(this, this.f, a3, a2);
                            d().execute(this.h);
                            this.e.sendEmptyMessageDelayed(102, (long) this.f.f());
                        }
                    } catch (Throwable th) {
                    } finally {
                        this.b.readLock().unlock();
                    }
                } else {
                    e();
                }
            }
        }
    }

    private Executor d() {
        if (this.j != null) {
            return this.j;
        }
        Executor h2 = this.f.h();
        if (h2 != null) {
            this.c = 1;
            this.j = h2;
            return this.j;
        }
        this.j = new ThreadPoolExecutor(1, 1, 0, TimeUnit.MILLISECONDS, new LinkedBlockingQueue(1024), new ThreadFactory() {
            /* class com.amap.openapi.bj.AnonymousClass2 */

            public Thread newThread(Runnable runnable) {
                return new Thread(runnable, "UploadController");
            }
        });
        this.c = 2;
        return this.j;
    }

    /* access modifiers changed from: private */
    public void e() {
        try {
            this.b.readLock().lock();
            if (this.e != null && ((this.f.d() <= 0 || this.i < this.f.d()) && !this.e.hasMessages(101))) {
                this.e.sendMessageDelayed(this.e.obtainMessage(101, 0, 0), this.f.e());
            }
        } finally {
            this.b.readLock().unlock();
        }
    }

    public void a() {
        try {
            this.b.writeLock().lock();
            if (this.a == 1) {
                this.a = 2;
                this.e.removeCallbacksAndMessages(null);
                if (this.e.getLooper() == Looper.myLooper()) {
                    b();
                } else {
                    this.e.sendEmptyMessage(106);
                }
                this.e = null;
            }
        } finally {
            this.b.writeLock().unlock();
        }
    }

    /* JADX DEBUG: Failed to find minimal casts for resolve overloaded methods, cast all args instead
     method: ClspMth{java.lang.Math.max(long, long):long}
     arg types: [int, long]
     candidates:
      ClspMth{java.lang.Math.max(double, double):double}
      ClspMth{java.lang.Math.max(int, int):int}
      ClspMth{java.lang.Math.max(float, float):float}
      ClspMth{java.lang.Math.max(long, long):long} */
    public void a(long j2) {
        try {
            this.b.readLock().lock();
            if (this.e != null) {
                this.e.removeMessages(101);
                this.e.sendMessageDelayed(this.e.obtainMessage(101, 1, 0), Math.max(0L, j2));
            }
        } finally {
            this.b.readLock().unlock();
        }
    }

    public void a(Context context, a aVar, Looper looper) {
        if (context == null || aVar == null || looper == null) {
            throw new RuntimeException("params not be null!");
        }
        try {
            this.b.writeLock().lock();
            if (this.a == 0) {
                this.d = context;
                this.f = aVar;
                this.e = new Handler(looper, this.k);
                if (Looper.myLooper() == looper) {
                    this.f.a();
                } else {
                    this.e.sendEmptyMessage(105);
                }
                this.a = 1;
            }
        } finally {
            this.b.writeLock().unlock();
        }
    }
}
