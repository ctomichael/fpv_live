package com.amap.openapi;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Looper;
import android.support.annotation.NonNull;
import com.amap.location.common.network.IHttpClient;
import com.amap.location.common.util.e;
import com.amap.location.uptunnel.core.db.DBProvider;
import com.amap.openapi.bi;
import com.amap.openapi.bj;
import java.util.ArrayList;
import java.util.concurrent.Executor;

/* compiled from: DataTunnel */
public class ee {
    private String a = "DataTunnel";
    /* access modifiers changed from: private */
    public String b = null;
    /* access modifiers changed from: private */
    public Uri c;
    /* access modifiers changed from: private */
    public DBProvider d;
    /* access modifiers changed from: private */
    public dt e;
    /* access modifiers changed from: private */
    public dq f;
    /* access modifiers changed from: private */
    public IHttpClient g;
    /* access modifiers changed from: private */
    public int h;
    private bi<c> i;
    private bj j;

    /* compiled from: DataTunnel */
    class a implements bi.b<c> {
        a() {
        }

        /* JADX INFO: finally extract failed */
        /* JADX WARNING: Code restructure failed: missing block: B:36:0x00f6, code lost:
            com.amap.location.common.util.e.a(r1);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:40:0x0109, code lost:
            r0 = move-exception;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:41:0x010a, code lost:
            com.amap.location.common.util.e.a(r1);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:42:0x010d, code lost:
            throw r0;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:53:?, code lost:
            return false;
         */
        /* JADX WARNING: Failed to process nested try/catch */
        /* JADX WARNING: Removed duplicated region for block: B:40:0x0109 A[ExcHandler: all (r0v8 'th' java.lang.Throwable A[CUSTOM_DECLARE]), Splitter:B:18:0x0055] */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        private boolean a(long r12, long r14) {
            /*
                r11 = this;
                r8 = 0
                r7 = 1
                r3 = 0
                r6 = 0
                int r0 = r11.e()
                if (r0 <= 0) goto L_0x0037
                com.amap.openapi.ee r0 = com.amap.openapi.ee.this
                com.amap.location.uptunnel.core.db.DBProvider r0 = r0.d
                com.amap.openapi.ee r1 = com.amap.openapi.ee.this
                android.net.Uri r1 = r1.c
                java.lang.String[] r2 = new java.lang.String[r7]
                java.lang.String r4 = "sum(size)"
                r2[r6] = r4
                r4 = r3
                r5 = r3
                android.database.Cursor r1 = r0.a(r1, r2, r3, r4, r5)
                if (r1 == 0) goto L_0x0045
                boolean r0 = r1.moveToFirst()     // Catch:{ Exception -> 0x004a, all -> 0x0050 }
                if (r0 == 0) goto L_0x0045
                r0 = 0
                long r2 = r1.getLong(r0)     // Catch:{ Exception -> 0x004a, all -> 0x0050 }
                long r2 = r12 - r2
                long r14 = r14 - r2
                com.amap.location.common.util.e.a(r1)
            L_0x0037:
                com.amap.openapi.ee r0 = com.amap.openapi.ee.this
                com.amap.location.uptunnel.core.db.DBProvider r0 = r0.d
                android.database.sqlite.SQLiteDatabase r0 = r0.c()
                if (r0 != 0) goto L_0x009f
                r0 = r6
            L_0x0044:
                return r0
            L_0x0045:
                com.amap.location.common.util.e.a(r1)
                r0 = r6
                goto L_0x0044
            L_0x004a:
                r0 = move-exception
                com.amap.location.common.util.e.a(r1)
                r0 = r6
                goto L_0x0044
            L_0x0050:
                r0 = move-exception
                com.amap.location.common.util.e.a(r1)
                throw r0
            L_0x0055:
                java.lang.StringBuilder r4 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x00f5, all -> 0x0109 }
                java.lang.String r5 = "delete from "
                r4.<init>(r5)     // Catch:{ Exception -> 0x00f5, all -> 0x0109 }
                com.amap.openapi.ee r5 = com.amap.openapi.ee.this     // Catch:{ Exception -> 0x00f5, all -> 0x0109 }
                java.lang.String r5 = r5.b     // Catch:{ Exception -> 0x00f5, all -> 0x0109 }
                java.lang.StringBuilder r4 = r4.append(r5)     // Catch:{ Exception -> 0x00f5, all -> 0x0109 }
                java.lang.String r5 = " where ID < ( select ID from "
                java.lang.StringBuilder r4 = r4.append(r5)     // Catch:{ Exception -> 0x00f5, all -> 0x0109 }
                com.amap.openapi.ee r5 = com.amap.openapi.ee.this     // Catch:{ Exception -> 0x00f5, all -> 0x0109 }
                java.lang.String r5 = r5.b     // Catch:{ Exception -> 0x00f5, all -> 0x0109 }
                java.lang.StringBuilder r4 = r4.append(r5)     // Catch:{ Exception -> 0x00f5, all -> 0x0109 }
                java.lang.String r5 = " limit "
                java.lang.StringBuilder r4 = r4.append(r5)     // Catch:{ Exception -> 0x00f5, all -> 0x0109 }
                com.amap.openapi.ee r5 = com.amap.openapi.ee.this     // Catch:{ Exception -> 0x00f5, all -> 0x0109 }
                com.amap.openapi.dq r5 = r5.f     // Catch:{ Exception -> 0x00f5, all -> 0x0109 }
                int r5 = r5.a()     // Catch:{ Exception -> 0x00f5, all -> 0x0109 }
                java.lang.StringBuilder r4 = r4.append(r5)     // Catch:{ Exception -> 0x00f5, all -> 0x0109 }
                java.lang.String r5 = ", 1)"
                java.lang.StringBuilder r4 = r4.append(r5)     // Catch:{ Exception -> 0x00f5, all -> 0x0109 }
                java.lang.String r4 = r4.toString()     // Catch:{ Exception -> 0x00f5, all -> 0x0109 }
                r0.execSQL(r4)     // Catch:{ Exception -> 0x00f5, all -> 0x0109 }
                long r14 = r14 - r2
                com.amap.location.common.util.e.a(r1)
            L_0x009f:
                int r1 = (r14 > r8 ? 1 : (r14 == r8 ? 0 : -1))
                if (r1 <= 0) goto L_0x010e
                java.lang.StringBuilder r1 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x00f1 }
                java.lang.String r2 = "select sum(size) from (select * from "
                r1.<init>(r2)     // Catch:{ Exception -> 0x00f1 }
                com.amap.openapi.ee r2 = com.amap.openapi.ee.this     // Catch:{ Exception -> 0x00f1 }
                java.lang.String r2 = r2.b     // Catch:{ Exception -> 0x00f1 }
                java.lang.StringBuilder r1 = r1.append(r2)     // Catch:{ Exception -> 0x00f1 }
                java.lang.String r2 = " limit 0, "
                java.lang.StringBuilder r1 = r1.append(r2)     // Catch:{ Exception -> 0x00f1 }
                com.amap.openapi.ee r2 = com.amap.openapi.ee.this     // Catch:{ Exception -> 0x00f1 }
                com.amap.openapi.dq r2 = r2.f     // Catch:{ Exception -> 0x00f1 }
                int r2 = r2.a()     // Catch:{ Exception -> 0x00f1 }
                java.lang.StringBuilder r1 = r1.append(r2)     // Catch:{ Exception -> 0x00f1 }
                java.lang.String r2 = ")"
                java.lang.StringBuilder r1 = r1.append(r2)     // Catch:{ Exception -> 0x00f1 }
                java.lang.String r1 = r1.toString()     // Catch:{ Exception -> 0x00f1 }
                r2 = 0
                android.database.Cursor r1 = r0.rawQuery(r1, r2)     // Catch:{ Exception -> 0x00f1 }
                if (r1 == 0) goto L_0x00fc
                boolean r2 = r1.moveToFirst()     // Catch:{ Exception -> 0x0102, all -> 0x0109 }
                if (r2 == 0) goto L_0x00fc
                r2 = 0
                long r2 = r1.getLong(r2)     // Catch:{ Exception -> 0x0102, all -> 0x0109 }
                int r4 = (r2 > r8 ? 1 : (r2 == r8 ? 0 : -1))
                if (r4 > 0) goto L_0x0055
                com.amap.location.common.util.e.a(r1)
                r0 = r6
                goto L_0x0044
            L_0x00f1:
                r0 = move-exception
                r0 = r6
                goto L_0x0044
            L_0x00f5:
                r0 = move-exception
                com.amap.location.common.util.e.a(r1)
                r0 = r6
                goto L_0x0044
            L_0x00fc:
                com.amap.location.common.util.e.a(r1)
                r0 = r6
                goto L_0x0044
            L_0x0102:
                r0 = move-exception
                com.amap.location.common.util.e.a(r1)
                r0 = r6
                goto L_0x0044
            L_0x0109:
                r0 = move-exception
                com.amap.location.common.util.e.a(r1)
                throw r0
            L_0x010e:
                r0 = r7
                goto L_0x0044
            */
            throw new UnsupportedOperationException("Method not decompiled: com.amap.openapi.ee.a.a(long, long):boolean");
        }

        private int e() {
            return ee.this.d.a(ee.this.c, "time < ?", new String[]{String.valueOf(System.currentTimeMillis() - ee.this.f.h())});
        }

        public void a() {
        }

        public void a(ArrayList<c> arrayList) {
            ContentValues[] contentValuesArr = new ContentValues[arrayList.size()];
            int i = 0;
            while (true) {
                int i2 = i;
                if (i2 < arrayList.size()) {
                    c cVar = arrayList.get(i2);
                    ContentValues contentValues = new ContentValues();
                    contentValues.put("type", Integer.valueOf(cVar.a));
                    contentValues.put("time", Long.valueOf(cVar.b));
                    contentValues.put("size", Long.valueOf(cVar.a()));
                    contentValues.put("value", cVar.c);
                    contentValuesArr[i2] = contentValues;
                    i = i2 + 1;
                } else {
                    ee.this.d.a(ee.this.c, contentValuesArr);
                    return;
                }
            }
        }

        /* JADX INFO: finally extract failed */
        public boolean a(long j) {
            Cursor a2 = ee.this.d.a(ee.this.c, new String[]{"sum(size)"}, null, null, null);
            if (a2 != null) {
                try {
                    if (a2.moveToFirst()) {
                        long j2 = a2.getLong(0);
                        if (j2 + j > ee.this.f.g()) {
                            boolean a3 = a(j2, (j2 + j) - ee.this.f.g());
                            e.a(a2);
                            return a3;
                        }
                        e.a(a2);
                        return true;
                    }
                } catch (Exception e) {
                    e.a(a2);
                    return false;
                } catch (Throwable th) {
                    e.a(a2);
                    throw th;
                }
            }
            e.a(a2);
            return false;
        }

        public void b() {
        }

        public long c() {
            return ee.this.f.c();
        }

        public long d() {
            return ee.this.f.e();
        }
    }

    /* compiled from: DataTunnel */
    class b implements bj.a {
        b() {
        }

        /* JADX WARNING: Code restructure failed: missing block: B:8:0x0069, code lost:
            com.amap.location.common.util.e.a(r17);
         */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public java.lang.Object a(long r24) {
            /*
                r23 = this;
                com.amap.openapi.ee$d r8 = new com.amap.openapi.ee$d
                r8.<init>()
                r12 = 0
                com.loc.fc r9 = new com.loc.fc
                r9.<init>()
                r10 = -1
                r2 = 0
                java.util.ArrayList r16 = new java.util.ArrayList
                r16.<init>()
                r14 = r2
            L_0x0016:
                int r2 = (r12 > r24 ? 1 : (r12 == r24 ? 0 : -1))
                if (r2 >= 0) goto L_0x006c
                r0 = r23
                com.amap.openapi.ee r2 = com.amap.openapi.ee.this
                com.amap.location.uptunnel.core.db.DBProvider r2 = r2.d
                r0 = r23
                com.amap.openapi.ee r3 = com.amap.openapi.ee.this
                android.net.Uri r3 = r3.c
                java.lang.String[] r4 = com.amap.openapi.dv.a
                r5 = 0
                r6 = 0
                java.lang.StringBuilder r7 = new java.lang.StringBuilder
                java.lang.String r17 = "ID limit "
                r0 = r17
                r7.<init>(r0)
                java.lang.StringBuilder r7 = r7.append(r14)
                java.lang.String r17 = ", "
                r0 = r17
                java.lang.StringBuilder r7 = r7.append(r0)
                r0 = r23
                com.amap.openapi.ee r0 = com.amap.openapi.ee.this
                r17 = r0
                com.amap.openapi.dq r17 = r17.f
                int r17 = r17.a()
                r0 = r17
                java.lang.StringBuilder r7 = r7.append(r0)
                java.lang.String r7 = r7.toString()
                android.database.Cursor r17 = r2.a(r3, r4, r5, r6, r7)
                if (r17 == 0) goto L_0x0069
                int r2 = r17.getCount()     // Catch:{ Exception -> 0x0074 }
                if (r2 != 0) goto L_0x0079
            L_0x0069:
                com.amap.location.common.util.e.a(r17)     // Catch:{ Exception -> 0x0074 }
            L_0x006c:
                r2 = 0
                int r2 = (r10 > r2 ? 1 : (r10 == r2 ? 0 : -1))
                if (r2 >= 0) goto L_0x00e5
                r2 = 0
            L_0x0073:
                return r2
            L_0x0074:
                r2 = move-exception
                com.amap.location.common.util.e.a(r17)
                goto L_0x006c
            L_0x0079:
                r0 = r23
                com.amap.openapi.ee r2 = com.amap.openapi.ee.this
                com.amap.openapi.dq r2 = r2.f
                int r2 = r2.a()
                long r2 = (long) r2
                long r14 = r14 + r2
                r2 = r10
                r6 = r12
            L_0x0089:
                boolean r4 = r17.moveToNext()     // Catch:{ Exception -> 0x00da, all -> 0x00e0 }
                if (r4 == 0) goto L_0x00d3
                r4 = 0
                r0 = r17
                long r4 = r0.getLong(r4)     // Catch:{ Exception -> 0x00da, all -> 0x00e0 }
                r10 = 1
                r0 = r17
                int r10 = r0.getInt(r10)     // Catch:{ Exception -> 0x00da, all -> 0x00e0 }
                r11 = 2
                r0 = r17
                byte[] r11 = r0.getBlob(r11)     // Catch:{ Exception -> 0x00da, all -> 0x00e0 }
                r12 = 3
                r0 = r17
                long r12 = r0.getLong(r12)     // Catch:{ Exception -> 0x00da, all -> 0x00e0 }
                r18 = 4
                int r18 = r17.getInt(r18)     // Catch:{ Exception -> 0x00da, all -> 0x00e0 }
                r0 = r18
                long r0 = (long) r0     // Catch:{ Exception -> 0x00da, all -> 0x00e0 }
                r20 = r0
                long r20 = r20 + r6
                int r19 = (r20 > r24 ? 1 : (r20 == r24 ? 0 : -1))
                if (r19 > 0) goto L_0x00d3
                r0 = r18
                long r2 = (long) r0     // Catch:{ Exception -> 0x00da, all -> 0x00e0 }
                long r6 = r6 + r2
                int r2 = com.amap.openapi.dy.a(r9, r11)     // Catch:{ Exception -> 0x00da, all -> 0x00e0 }
                int r2 = com.amap.openapi.dy.a(r9, r10, r2, r12)     // Catch:{ Exception -> 0x00da, all -> 0x00e0 }
                java.lang.Integer r2 = java.lang.Integer.valueOf(r2)     // Catch:{ Exception -> 0x00da, all -> 0x00e0 }
                r0 = r16
                r0.add(r2)     // Catch:{ Exception -> 0x00da, all -> 0x00e0 }
                r2 = r4
                goto L_0x0089
            L_0x00d3:
                com.amap.location.common.util.e.a(r17)
                r10 = r2
                r12 = r6
                goto L_0x0016
            L_0x00da:
                r2 = move-exception
                com.amap.location.common.util.e.a(r17)
                r2 = 0
                goto L_0x0073
            L_0x00e0:
                r2 = move-exception
                com.amap.location.common.util.e.a(r17)
                throw r2
            L_0x00e5:
                int r2 = r16.size()
                int[] r4 = new int[r2]
                r2 = 0
                r3 = r2
            L_0x00ed:
                int r2 = r16.size()
                if (r3 >= r2) goto L_0x0105
                r0 = r16
                java.lang.Object r2 = r0.get(r3)
                java.lang.Integer r2 = (java.lang.Integer) r2
                int r2 = r2.intValue()
                r4[r3] = r2
                int r2 = r3 + 1
                r3 = r2
                goto L_0x00ed
            L_0x0105:
                r0 = r23
                com.amap.openapi.ee r2 = com.amap.openapi.ee.this
                com.amap.openapi.dt r2 = r2.e
                android.content.Context r2 = r2.a()
                int r2 = com.amap.openapi.bk.a(r9, r2)
                int r3 = com.amap.openapi.dz.b(r9, r4)
                com.amap.openapi.dz.a(r9)
                r4 = 1
                com.amap.openapi.dz.a(r9, r4)
                com.amap.openapi.dz.a(r9, r2)
                com.amap.openapi.dz.c(r9, r3)
                int r2 = com.amap.openapi.dz.b(r9)
                com.amap.openapi.dz.d(r9, r2)
                byte[] r2 = r9.f()
                r8.a = r2
                r8.b = r10
                r8.c = r12
                r2 = r8
                goto L_0x0073
            */
            throw new UnsupportedOperationException("Method not decompiled: com.amap.openapi.ee.b.a(long):java.lang.Object");
        }

        public void a() {
        }

        public void a(int i) {
        }

        public void a(int i, Object obj) {
            if (obj instanceof d) {
                ee.this.e.a(ee.this.h, i, ((d) obj).c);
            }
        }

        public boolean a(Object obj) {
            if (obj instanceof d) {
                return ea.a(ee.this.g, ee.this.e.a(ee.this.h), ((d) obj).a, ee.this.f.f());
            }
            return false;
        }

        public void b() {
        }

        public void b(Object obj) {
            if (obj instanceof d) {
                ee.this.d.a(ee.this.c, "ID <= ? ", new String[]{String.valueOf(((d) obj).b)});
            }
        }

        public boolean b(int i) {
            return ee.this.f.c(i);
        }

        public long c() {
            Cursor a2 = ee.this.d.a(ee.this.c, new String[]{"sum(size)"}, null, null, null);
            long j = 0;
            if (a2 != null) {
                try {
                    if (a2.moveToFirst()) {
                        j = a2.getLong(0);
                    }
                } catch (Exception e) {
                    e.a(a2);
                } catch (Throwable th) {
                    e.a(a2);
                    throw th;
                }
            }
            e.a(a2);
            return j;
        }

        public long c(int i) {
            return ee.this.f.b(i) - ee.this.e.a(ee.this.h, i);
        }

        public int d() {
            return 3;
        }

        public long d(int i) {
            return ee.this.f.a(i);
        }

        public long e() {
            return ee.this.f.d();
        }

        public int f() {
            return ee.this.f.f();
        }

        public void g() {
        }

        public Executor h() {
            return null;
        }
    }

    /* compiled from: DataTunnel */
    static class c implements bi.a {
        int a;
        long b;
        byte[] c;

        c() {
        }

        public long a() {
            return (long) ((this.c == null ? 0 : this.c.length) + 24);
        }
    }

    /* compiled from: DataTunnel */
    static class d {
        byte[] a;
        long b;
        long c;

        d() {
        }
    }

    public void a() {
        this.i.a();
        this.j.a();
    }

    public void a(int i2) {
        if (i2 != -1) {
            this.f.b();
            this.j.a(20000);
        }
    }

    public void a(int i2, byte[] bArr) {
        this.f.b();
        c cVar = new c();
        cVar.a = i2;
        cVar.b = System.currentTimeMillis();
        cVar.c = bArr;
        this.i.a((s) cVar);
    }

    public void a(@NonNull dt dtVar, @NonNull dq dqVar, @NonNull IHttpClient iHttpClient, int i2, @NonNull Looper looper) {
        this.b = dt.c(i2);
        this.a += this.b;
        this.e = dtVar;
        this.h = i2;
        this.f = new ed(dqVar);
        this.g = iHttpClient;
        this.d = dtVar.b();
        this.c = dt.b(i2);
        this.i = new bi<>();
        this.j = new bj();
        this.i.a(new a(), looper);
        this.j.a(dtVar.a(), new b(), looper);
        this.j.a(20000);
    }

    public void b() {
        this.i.b();
    }
}
