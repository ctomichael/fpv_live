package com.amap.openapi;

import android.database.Cursor;
import android.net.Uri;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.util.SparseIntArray;
import com.amap.location.common.network.IHttpClient;
import com.amap.location.common.util.e;
import com.amap.location.uptunnel.core.db.DBProvider;
import com.amap.openapi.bi;
import com.amap.openapi.bj;
import com.loc.fc;
import java.util.concurrent.Executor;

/* compiled from: CountTunnel */
public class ec {
    /* access modifiers changed from: private */
    public Uri a;
    /* access modifiers changed from: private */
    public DBProvider b;
    /* access modifiers changed from: private */
    public dt c;
    /* access modifiers changed from: private */
    public dp d;
    /* access modifiers changed from: private */
    public IHttpClient e;
    /* access modifiers changed from: private */
    public int f;
    private bj g;
    private bi h;
    /* access modifiers changed from: private */
    public SparseIntArray i = new SparseIntArray();
    /* access modifiers changed from: private */
    public volatile long j;

    /* compiled from: CountTunnel */
    class a implements bi.b<c> {
        a() {
        }

        /* JADX INFO: finally extract failed */
        private boolean b(long j) {
            int e = e();
            if (e > 0) {
                j -= (long) (e * 24);
            }
            long j2 = (j / 24) + ((long) (j % 24 > 0 ? 1 : 0));
            if (j2 <= 0) {
                return true;
            }
            try {
                Cursor a2 = ec.this.b.a(ec.this.a, new String[]{"ID"}, null, null, null, (j2 - 1) + ", 1");
                if (a2 != null) {
                    try {
                        if (a2.moveToFirst()) {
                            boolean z = ec.this.b.a(ec.this.a, "ID <= ?", new String[]{String.valueOf(a2.getLong(0))}) > 0;
                            e.a(a2);
                            return z;
                        }
                    } catch (Exception e2) {
                        e.a(a2);
                        return false;
                    } catch (Throwable th) {
                        e.a(a2);
                        throw th;
                    }
                }
                e.a(a2);
                return false;
            } catch (Exception e3) {
                return false;
            }
        }

        private int e() {
            return ec.this.b.a(ec.this.a, "time < ?", new String[]{String.valueOf(System.currentTimeMillis() - ec.this.d.h())});
        }

        public void a() {
        }

        /* JADX WARNING: Removed duplicated region for block: B:20:0x00d9  */
        /* JADX WARNING: Removed duplicated region for block: B:35:0x0116 A[SYNTHETIC] */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public void a(java.util.ArrayList<com.amap.openapi.ec.c> r15) {
            /*
                r14 = this;
                r7 = 1
                r8 = 0
                com.amap.openapi.ec r0 = com.amap.openapi.ec.this
                android.util.SparseIntArray r0 = r0.i
                int r10 = r0.size()
                r9 = r8
            L_0x000d:
                if (r9 >= r10) goto L_0x012b
                com.amap.openapi.ec r0 = com.amap.openapi.ec.this
                android.util.SparseIntArray r0 = r0.i
                int r11 = r0.keyAt(r9)
                com.amap.openapi.ec r0 = com.amap.openapi.ec.this
                android.util.SparseIntArray r0 = r0.i
                int r12 = r0.valueAt(r9)
                com.amap.openapi.ec r0 = com.amap.openapi.ec.this
                com.amap.location.uptunnel.core.db.DBProvider r0 = r0.b
                com.amap.openapi.ec r1 = com.amap.openapi.ec.this
                android.net.Uri r1 = r1.a
                java.lang.String[] r2 = com.amap.openapi.dw.a
                java.lang.String r3 = "type = ? "
                java.lang.String[] r4 = new java.lang.String[r7]
                java.lang.String r5 = java.lang.String.valueOf(r11)
                r4[r8] = r5
                java.lang.String r5 = "time DESC"
                java.lang.String r6 = "0, 1"
                android.database.Cursor r1 = r0.b(r1, r2, r3, r4, r5, r6)
                if (r1 == 0) goto L_0x0135
                boolean r0 = r1.moveToFirst()     // Catch:{ Exception -> 0x0120, all -> 0x0126 }
                if (r0 == 0) goto L_0x0135
                java.lang.String r0 = "time"
                int r0 = r1.getColumnIndex(r0)     // Catch:{ Exception -> 0x0120, all -> 0x0126 }
                long r2 = r1.getLong(r0)     // Catch:{ Exception -> 0x0120, all -> 0x0126 }
                long r4 = java.lang.System.currentTimeMillis()     // Catch:{ Exception -> 0x0120, all -> 0x0126 }
                long r2 = r4 - r2
                r4 = 0
                int r0 = (r2 > r4 ? 1 : (r2 == r4 ? 0 : -1))
                if (r0 <= 0) goto L_0x0135
                com.amap.openapi.ec r0 = com.amap.openapi.ec.this     // Catch:{ Exception -> 0x0120, all -> 0x0126 }
                com.amap.openapi.dp r0 = r0.d     // Catch:{ Exception -> 0x0120, all -> 0x0126 }
                long r4 = r0.a()     // Catch:{ Exception -> 0x0120, all -> 0x0126 }
                int r0 = (r2 > r4 ? 1 : (r2 == r4 ? 0 : -1))
                if (r0 >= 0) goto L_0x0135
                java.lang.String r0 = "ID"
                int r0 = r1.getColumnIndex(r0)     // Catch:{ Exception -> 0x0120, all -> 0x0126 }
                long r2 = r1.getLong(r0)     // Catch:{ Exception -> 0x0120, all -> 0x0126 }
                com.amap.openapi.ec r0 = com.amap.openapi.ec.this     // Catch:{ Exception -> 0x0120, all -> 0x0126 }
                long r4 = r0.j     // Catch:{ Exception -> 0x0120, all -> 0x0126 }
                int r0 = (r2 > r4 ? 1 : (r2 == r4 ? 0 : -1))
                if (r0 > 0) goto L_0x011b
                r0 = r7
            L_0x0089:
                if (r0 != 0) goto L_0x0135
                java.lang.String r0 = "value"
                int r0 = r1.getColumnIndex(r0)     // Catch:{ Exception -> 0x0120, all -> 0x0126 }
                int r0 = r1.getInt(r0)     // Catch:{ Exception -> 0x0120, all -> 0x0126 }
                android.content.ContentValues r4 = new android.content.ContentValues     // Catch:{ Exception -> 0x0120, all -> 0x0126 }
                r4.<init>()     // Catch:{ Exception -> 0x0120, all -> 0x0126 }
                java.lang.String r5 = "ID"
                java.lang.Long r6 = java.lang.Long.valueOf(r2)     // Catch:{ Exception -> 0x0120, all -> 0x0126 }
                r4.put(r5, r6)     // Catch:{ Exception -> 0x0120, all -> 0x0126 }
                java.lang.String r5 = "value"
                int r0 = r0 + r12
                java.lang.Integer r0 = java.lang.Integer.valueOf(r0)     // Catch:{ Exception -> 0x0120, all -> 0x0126 }
                r4.put(r5, r0)     // Catch:{ Exception -> 0x0120, all -> 0x0126 }
                com.amap.openapi.ec r0 = com.amap.openapi.ec.this     // Catch:{ Exception -> 0x0120, all -> 0x0126 }
                com.amap.location.uptunnel.core.db.DBProvider r0 = r0.b     // Catch:{ Exception -> 0x0120, all -> 0x0126 }
                com.amap.openapi.ec r5 = com.amap.openapi.ec.this     // Catch:{ Exception -> 0x0120, all -> 0x0126 }
                android.net.Uri r5 = r5.a     // Catch:{ Exception -> 0x0120, all -> 0x0126 }
                java.lang.StringBuilder r6 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x0120, all -> 0x0126 }
                java.lang.String r13 = "ID = "
                r6.<init>(r13)     // Catch:{ Exception -> 0x0120, all -> 0x0126 }
                java.lang.StringBuilder r2 = r6.append(r2)     // Catch:{ Exception -> 0x0120, all -> 0x0126 }
                java.lang.String r2 = r2.toString()     // Catch:{ Exception -> 0x0120, all -> 0x0126 }
                r3 = 0
                int r0 = r0.a(r5, r4, r2, r3)     // Catch:{ Exception -> 0x0120, all -> 0x0126 }
                if (r0 < 0) goto L_0x011e
                r0 = r7
            L_0x00d4:
                com.amap.location.common.util.e.a(r1)
            L_0x00d7:
                if (r0 != 0) goto L_0x0116
                long r0 = java.lang.System.currentTimeMillis()
                android.content.ContentValues r2 = new android.content.ContentValues
                r2.<init>()
                java.lang.String r3 = "type"
                java.lang.Integer r4 = java.lang.Integer.valueOf(r11)
                r2.put(r3, r4)
                java.lang.String r3 = "time"
                java.lang.Long r0 = java.lang.Long.valueOf(r0)
                r2.put(r3, r0)
                java.lang.String r0 = "value"
                java.lang.Integer r1 = java.lang.Integer.valueOf(r12)
                r2.put(r0, r1)
                com.amap.openapi.ec r0 = com.amap.openapi.ec.this
                com.amap.location.uptunnel.core.db.DBProvider r0 = r0.b
                com.amap.openapi.ec r1 = com.amap.openapi.ec.this
                android.net.Uri r1 = r1.a
                long r0 = r0.a(r1, r2)
                r2 = -1
                int r0 = (r0 > r2 ? 1 : (r0 == r2 ? 0 : -1))
                if (r0 == 0) goto L_0x012b
            L_0x0116:
                int r0 = r9 + 1
                r9 = r0
                goto L_0x000d
            L_0x011b:
                r0 = r8
                goto L_0x0089
            L_0x011e:
                r0 = r8
                goto L_0x00d4
            L_0x0120:
                r0 = move-exception
                com.amap.location.common.util.e.a(r1)
                r0 = r8
                goto L_0x00d7
            L_0x0126:
                r0 = move-exception
                com.amap.location.common.util.e.a(r1)
                throw r0
            L_0x012b:
                com.amap.openapi.ec r0 = com.amap.openapi.ec.this
                android.util.SparseIntArray r0 = r0.i
                r0.clear()
                return
            L_0x0135:
                r0 = r8
                goto L_0x00d4
            */
            throw new UnsupportedOperationException("Method not decompiled: com.amap.openapi.ec.a.a(java.util.ArrayList):void");
        }

        public boolean a(long j) {
            long j2 = j * 24;
            try {
                long a2 = ec.this.b.a(ec.this.a) * 24;
                if (a2 + j2 > ec.this.d.g()) {
                    return b((j2 + a2) - ec.this.d.g());
                }
                return true;
            } catch (Exception e) {
                return false;
            }
        }

        public void b() {
        }

        public long c() {
            return ec.this.d.c();
        }

        public long d() {
            return ec.this.d.e();
        }
    }

    /* compiled from: CountTunnel */
    class b implements bj.a {
        b() {
        }

        public Object a(long j) {
            d dVar = new d();
            long j2 = j / 24;
            if (j2 <= 0) {
                return null;
            }
            Cursor b = ec.this.b.b(ec.this.a, dw.a, null, null, null, "0, " + j2);
            if (b != null) {
                try {
                    if (b.getCount() > 0) {
                        fc fcVar = new fc();
                        int a2 = bk.a(fcVar, ec.this.c.a());
                        int count = b.getCount();
                        int[] iArr = new int[count];
                        int i = 0;
                        long j3 = -1;
                        while (b.moveToNext()) {
                            j3 = b.getLong(b.getColumnIndex("ID"));
                            iArr[i] = dx.a(fcVar, b.getInt(b.getColumnIndex("type")), b.getInt(b.getColumnIndex("value")), b.getLong(b.getColumnIndex("time")));
                            i++;
                        }
                        int a3 = dz.a(fcVar, iArr);
                        dz.a(fcVar);
                        dz.a(fcVar, (byte) 0);
                        dz.a(fcVar, a2);
                        dz.b(fcVar, a3);
                        dz.d(fcVar, dz.b(fcVar));
                        dVar.a = fcVar.f();
                        dVar.b = j3;
                        dVar.c = (long) (count * 24);
                        long unused = ec.this.j = j3;
                        e.a(b);
                        return dVar;
                    }
                } catch (Exception e) {
                    e.a(b);
                } catch (Throwable th) {
                    e.a(b);
                    throw th;
                }
            }
            e.a(b);
            return null;
        }

        public void a() {
        }

        public void a(int i) {
            long unused = ec.this.j = -1;
        }

        public void a(int i, Object obj) {
            if (obj instanceof d) {
                ec.this.c.a(ec.this.f, i, ((d) obj).c);
            }
        }

        public boolean a(Object obj) {
            if (obj instanceof d) {
                return ea.a(ec.this.e, ec.this.c.a(ec.this.f), ((d) obj).a, ec.this.d.f());
            }
            return false;
        }

        public void b() {
        }

        public void b(Object obj) {
            if (obj instanceof d) {
                ec.this.b.a(ec.this.a, "ID <= ? ", new String[]{String.valueOf(((d) obj).b)});
            }
        }

        public boolean b(int i) {
            return ec.this.d.c(i);
        }

        public long c() {
            try {
                return ec.this.b.a(ec.this.a) * 24;
            } catch (Exception e) {
                return 0;
            }
        }

        public long c(int i) {
            return ec.this.d.b(i) - ec.this.c.a(ec.this.f, i);
        }

        public int d() {
            return 3;
        }

        public long d(int i) {
            return ec.this.d.a(i);
        }

        public long e() {
            return ec.this.d.d();
        }

        public int f() {
            return ec.this.d.f();
        }

        public void g() {
            long unused = ec.this.j = -1;
        }

        public Executor h() {
            return null;
        }
    }

    /* compiled from: CountTunnel */
    static class c implements bi.a {
        /* access modifiers changed from: private */
        public static final c a = new c();

        c() {
        }

        public long a() {
            return 1;
        }
    }

    /* compiled from: CountTunnel */
    static class d {
        byte[] a;
        long b;
        long c;

        d() {
        }
    }

    public void a() {
        this.h.a();
        this.g.a();
    }

    public void a(int i2) {
        this.i.put(i2, this.i.get(i2) + 1);
        this.d.b();
        this.h.a((s) c.a);
    }

    public void a(@NonNull dt dtVar, @NonNull dp dpVar, @NonNull IHttpClient iHttpClient, @NonNull Looper looper) {
        this.c = dtVar;
        this.f = 1;
        this.d = new eb(dpVar);
        this.e = iHttpClient;
        this.b = dtVar.b();
        this.a = dt.b(this.f);
        this.h = new bi();
        this.h.a(new a(), looper);
        this.g = new bj();
        this.g.a(dtVar.a(), new b(), looper);
        this.g.a(20000);
    }

    public void b(int i2) {
        if (i2 != -1) {
            this.d.b();
            this.g.a(20000);
        }
    }
}
