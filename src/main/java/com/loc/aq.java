package com.loc;

import android.content.Context;
import android.os.Looper;
import java.lang.Thread;
import java.lang.ref.WeakReference;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/* compiled from: SDKLogHandler */
public final class aq extends an implements Thread.UncaughtExceptionHandler {
    private static ExecutorService e;
    private static Set<Integer> f = Collections.synchronizedSet(new HashSet());
    private static WeakReference<Context> g;
    private static final ThreadFactory h = new ThreadFactory() {
        /* class com.loc.aq.AnonymousClass2 */
        private final AtomicInteger a = new AtomicInteger(1);

        public final Thread newThread(Runnable runnable) {
            return new Thread(runnable, "pama#" + this.a.getAndIncrement());
        }
    };
    /* access modifiers changed from: private */
    public Context d;
    private List<Object> i;

    private aq(Context context) {
        this.d = context;
        try {
            this.b = Thread.getDefaultUncaughtExceptionHandler();
            if (this.b == null) {
                Thread.setDefaultUncaughtExceptionHandler(this);
                this.c = true;
                return;
            }
            String obj = this.b.toString();
            if (obj.startsWith("com.amap.apis.utils.core.dynamiccore") || (obj.indexOf("com.amap.api") == -1 && obj.indexOf("com.loc") == -1)) {
                Thread.setDefaultUncaughtExceptionHandler(this);
                this.c = true;
                return;
            }
            this.c = false;
        } catch (Throwable th) {
            th.printStackTrace();
        }
    }

    public static synchronized aq a(Context context, ac acVar) throws t {
        aq aqVar;
        synchronized (aq.class) {
            if (acVar == null) {
                throw new t("sdk info is null");
            } else if (acVar.a() == null || "".equals(acVar.a())) {
                throw new t("sdk name is invalid");
            } else {
                try {
                    new as().a(context);
                    if (!f.add(Integer.valueOf(acVar.hashCode()))) {
                        aqVar = (aq) an.a;
                    } else {
                        if (an.a == null) {
                            an.a = new aq(context);
                        } else {
                            an.a.c = false;
                        }
                        an.a.a(context, acVar, an.a.c);
                        aqVar = (aq) an.a;
                    }
                } catch (Throwable th) {
                    th.printStackTrace();
                }
            }
        }
        return aqVar;
    }

    public static void a(ac acVar, String str, t tVar) {
        if (tVar != null) {
            a(acVar, str, tVar.c(), tVar.d(), tVar.b());
        }
    }

    public static void a(ac acVar, String str, String str2, String str3, String str4) {
        try {
            if (an.a != null) {
                StringBuilder sb = new StringBuilder("path:");
                sb.append(str).append(",type:").append(str2).append(",gsid:").append(str3).append(",code:").append(str4);
                an.a.a(acVar, sb.toString(), "networkError");
            }
        } catch (Throwable th) {
        }
    }

    public static synchronized void b() {
        synchronized (aq.class) {
            try {
                if (e != null) {
                    e.shutdown();
                }
                bc.a();
            } catch (Throwable th) {
                th.printStackTrace();
            }
            try {
                if (!(an.a == null || Thread.getDefaultUncaughtExceptionHandler() != an.a || an.a.b == null)) {
                    Thread.setDefaultUncaughtExceptionHandler(an.a.b);
                }
                an.a = null;
            } catch (Throwable th2) {
                th2.printStackTrace();
            }
        }
        return;
    }

    public static void b(ac acVar, String str, String str2) {
        try {
            if (an.a != null) {
                an.a.a(acVar, str, str2);
            }
        } catch (Throwable th) {
        }
    }

    public static void b(Throwable th, String str, String str2) {
        try {
            if (an.a != null) {
                an.a.a(th, 1, str, str2);
            }
        } catch (Throwable th2) {
        }
    }

    public static void c() {
        if (g != null && g.get() != null) {
            ao.b(g.get());
        } else if (an.a != null) {
            an.a.a();
        }
    }

    public static synchronized ExecutorService d() {
        ExecutorService executorService;
        synchronized (aq.class) {
            try {
                if (e == null || e.isShutdown()) {
                    e = new ThreadPoolExecutor(1, 1, 0, TimeUnit.MILLISECONDS, new LinkedBlockingQueue(256), h);
                }
            } catch (Throwable th) {
                th.printStackTrace();
            }
            executorService = e;
        }
        return executorService;
    }

    /* access modifiers changed from: protected */
    public final void a() {
        ao.b(this.d);
    }

    /* access modifiers changed from: protected */
    public final void a(final Context context, final ac acVar, final boolean z) {
        try {
            ExecutorService d2 = d();
            if (d2 != null && !d2.isShutdown()) {
                d2.submit(new Runnable() {
                    /* class com.loc.aq.AnonymousClass1 */

                    public final void run() {
                        try {
                            synchronized (Looper.getMainLooper()) {
                                new ba(context, true).a(acVar);
                            }
                            if (z) {
                                ar.a(aq.this.d);
                            }
                        } catch (Throwable th) {
                            th.printStackTrace();
                        }
                    }
                });
            }
        } catch (RejectedExecutionException e2) {
        } catch (Throwable th) {
            th.printStackTrace();
        }
    }

    /* access modifiers changed from: protected */
    public final void a(ac acVar, String str, String str2) {
        ar.b(acVar, this.d, str2, str);
    }

    /* access modifiers changed from: protected */
    public final void a(Throwable th, int i2, String str, String str2) {
        ar.a(this.d, th, i2, str, str2);
    }

    public final void uncaughtException(Thread thread, Throwable th) {
        int i2 = 0;
        while (i2 < this.i.size() && i2 < 10) {
            try {
                this.i.get(i2);
                i2++;
            } catch (Throwable th2) {
            }
        }
        if (th != null) {
            a(th, 0, null, null);
            if (this.b != null) {
                try {
                    Thread.setDefaultUncaughtExceptionHandler(this.b);
                } catch (Throwable th3) {
                }
                this.b.uncaughtException(thread, th);
            }
        }
    }
}
