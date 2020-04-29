package com.amap.openapi;

import com.amap.location.common.network.IHttpClient;
import com.amap.location.common.util.b;
import com.loc.fc;
import org.json.JSONException;
import org.json.JSONObject;

/* renamed from: com.amap.openapi.do  reason: invalid class name */
/* compiled from: UploadTunnelCmdTask */
public class Cdo implements Runnable {
    private dt a;
    private IHttpClient b;
    private dn c;

    public Cdo(dt dtVar, dn dnVar, IHttpClient iHttpClient) {
        this.a = dtVar;
        this.c = dnVar;
        this.b = iHttpClient;
    }

    private void a() {
        fc fcVar = new fc();
        JSONObject jSONObject = new JSONObject();
        long currentTimeMillis = System.currentTimeMillis();
        try {
            jSONObject.put("time", b.a(currentTimeMillis, null));
            jSONObject.put("command", this.c.a());
        } catch (JSONException e) {
        }
        int a2 = bk.a(fcVar, this.a.a());
        int b2 = dz.b(fcVar, new int[]{dy.a(fcVar, 100003, dy.a(fcVar, jSONObject.toString().getBytes()), currentTimeMillis)});
        dz.a(fcVar);
        dz.a(fcVar, (byte) 1);
        dz.a(fcVar, a2);
        dz.c(fcVar, b2);
        dz.d(fcVar, dz.b(fcVar));
        ea.a(this.b, this.a.a(2), fcVar.f(), 60000);
    }

    /* JADX WARNING: Code restructure failed: missing block: B:118:?, code lost:
        return;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:121:?, code lost:
        return;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:61:0x0221, code lost:
        if (r6 <= 0) goto L_0x0223;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:66:0x02b8, code lost:
        r2 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:69:?, code lost:
        com.amap.openapi.dl.a((int) dji.diagnostics.model.DJIDiagnosticsError.Navigation.QUICK_SHOT_START, android.util.Log.getStackTraceString(r2).getBytes());
     */
    /* JADX WARNING: Code restructure failed: missing block: B:71:0x02cb, code lost:
        if (r14.size() > 0) goto L_0x02cd;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:72:0x02cd, code lost:
        r2 = 0;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:73:0x02cf, code lost:
        r3 = r2;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:74:0x02d3, code lost:
        if (r3 < r14.size()) goto L_0x02d5;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:75:0x02d5, code lost:
        com.amap.location.common.util.e.a((android.database.Cursor) r14.get(r3));
        r2 = r3 + 1;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:83:0x0302, code lost:
        r14.clear();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:84:0x0307, code lost:
        r2 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:85:0x0308, code lost:
        r4 = r2;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:86:0x030d, code lost:
        if (r14.size() > 0) goto L_0x030f;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:87:0x030f, code lost:
        r2 = 0;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:88:0x0311, code lost:
        r3 = r2;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:89:0x0315, code lost:
        if (r3 < r14.size()) goto L_0x0317;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:90:0x0317, code lost:
        com.amap.location.common.util.e.a((android.database.Cursor) r14.get(r3));
        r2 = r3 + 1;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:91:0x0324, code lost:
        r14.clear();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:92:0x0327, code lost:
        throw r4;
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Removed duplicated region for block: B:84:0x0307 A[ExcHandler: all (r2v0 'th' java.lang.Throwable A[CUSTOM_DECLARE]), Splitter:B:1:0x0007] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void run() {
        /*
            r22 = this;
            java.util.ArrayList r14 = new java.util.ArrayList
            r14.<init>()
            r0 = r22
            com.amap.openapi.dt r2 = r0.a     // Catch:{ Throwable -> 0x02b8, all -> 0x0307 }
            int r6 = r2.c()     // Catch:{ Throwable -> 0x02b8, all -> 0x0307 }
            r2 = -1
            if (r6 != r2) goto L_0x002f
            int r2 = r14.size()
            if (r2 <= 0) goto L_0x002e
            r2 = 0
            r3 = r2
        L_0x0018:
            int r2 = r14.size()
            if (r3 >= r2) goto L_0x002b
            java.lang.Object r2 = r14.get(r3)
            android.database.Cursor r2 = (android.database.Cursor) r2
            com.amap.location.common.util.e.a(r2)
            int r2 = r3 + 1
            r3 = r2
            goto L_0x0018
        L_0x002b:
            r14.clear()
        L_0x002e:
            return
        L_0x002f:
            r0 = r22
            com.amap.openapi.dt r2 = r0.a     // Catch:{ Throwable -> 0x02b8, all -> 0x0307 }
            com.amap.location.uptunnel.core.db.DBProvider r2 = r2.b()     // Catch:{ Throwable -> 0x02b8, all -> 0x0307 }
            android.database.sqlite.SQLiteDatabase r12 = r2.c()     // Catch:{ Throwable -> 0x02b8, all -> 0x0307 }
            r0 = r22
            com.amap.openapi.dn r3 = r0.c     // Catch:{ Throwable -> 0x02b8, all -> 0x0307 }
            int r3 = r3.b()     // Catch:{ Throwable -> 0x02b8, all -> 0x0307 }
            android.net.Uri r3 = com.amap.openapi.dt.b(r3)     // Catch:{ Throwable -> 0x02b8, all -> 0x0307 }
            r0 = r22
            com.amap.openapi.dn r4 = r0.c     // Catch:{ Throwable -> 0x02b8, all -> 0x0307 }
            int r4 = r4.b()     // Catch:{ Throwable -> 0x02b8, all -> 0x0307 }
            java.lang.String r13 = com.amap.openapi.dt.c(r4)     // Catch:{ Throwable -> 0x02b8, all -> 0x0307 }
            long r4 = r2.a(r3)     // Catch:{ Throwable -> 0x02b8, all -> 0x0307 }
            r8 = 0
            int r4 = (r4 > r8 ? 1 : (r4 == r8 ? 0 : -1))
            if (r4 > 0) goto L_0x007f
            r22.a()     // Catch:{ Throwable -> 0x02b8, all -> 0x0307 }
            int r2 = r14.size()
            if (r2 <= 0) goto L_0x002e
            r2 = 0
            r3 = r2
        L_0x0068:
            int r2 = r14.size()
            if (r3 >= r2) goto L_0x007b
            java.lang.Object r2 = r14.get(r3)
            android.database.Cursor r2 = (android.database.Cursor) r2
            com.amap.location.common.util.e.a(r2)
            int r2 = r3 + 1
            r3 = r2
            goto L_0x0068
        L_0x007b:
            r14.clear()
            goto L_0x002e
        L_0x007f:
            java.lang.StringBuilder r4 = new java.lang.StringBuilder     // Catch:{ Throwable -> 0x02b8, all -> 0x0307 }
            java.lang.String r5 = "select max(ID) from "
            r4.<init>(r5)     // Catch:{ Throwable -> 0x02b8, all -> 0x0307 }
            java.lang.StringBuilder r4 = r4.append(r13)     // Catch:{ Throwable -> 0x02b8, all -> 0x0307 }
            java.lang.String r4 = r4.toString()     // Catch:{ Throwable -> 0x02b8, all -> 0x0307 }
            r5 = 0
            android.database.Cursor r4 = r12.rawQuery(r4, r5)     // Catch:{ Throwable -> 0x02b8, all -> 0x0307 }
            r14.add(r4)     // Catch:{ Throwable -> 0x02b8, all -> 0x0307 }
            r4.moveToFirst()     // Catch:{ Throwable -> 0x02b8, all -> 0x0307 }
            r5 = 0
            long r16 = r4.getLong(r5)     // Catch:{ Throwable -> 0x02b8, all -> 0x0307 }
            com.amap.location.common.util.e.a(r4)     // Catch:{ Throwable -> 0x02b8, all -> 0x0307 }
            r4 = -1
            r7 = 1
            if (r6 != r7) goto L_0x0187
            r4 = 1
            java.lang.String[] r4 = new java.lang.String[r4]     // Catch:{ Throwable -> 0x02b8, all -> 0x0307 }
            r5 = 0
            java.lang.String r6 = "ID"
            r4[r5] = r6     // Catch:{ Throwable -> 0x02b8, all -> 0x0307 }
            r5 = 0
            r6 = 0
            r7 = 0
            java.lang.String r8 = "0,1"
            android.database.Cursor r4 = r2.b(r3, r4, r5, r6, r7, r8)     // Catch:{ Throwable -> 0x02b8, all -> 0x0307 }
            r14.add(r4)     // Catch:{ Throwable -> 0x02b8, all -> 0x0307 }
            r4.moveToFirst()     // Catch:{ Throwable -> 0x02b8, all -> 0x0307 }
            r5 = 0
            long r10 = r4.getLong(r5)     // Catch:{ Throwable -> 0x02b8, all -> 0x0307 }
            com.amap.location.common.util.e.a(r4)     // Catch:{ Throwable -> 0x02b8, all -> 0x0307 }
        L_0x00c8:
            r4 = 0
            int r4 = (r10 > r4 ? 1 : (r10 == r4 ? 0 : -1))
            if (r4 <= 0) goto L_0x02e2
            int r4 = (r16 > r10 ? 1 : (r16 == r10 ? 0 : -1))
            if (r4 < 0) goto L_0x02e2
            r12 = 0
            java.util.ArrayList r15 = new java.util.ArrayList     // Catch:{ Throwable -> 0x02b8, all -> 0x0307 }
            r15.<init>()     // Catch:{ Throwable -> 0x02b8, all -> 0x0307 }
            com.loc.fc r18 = new com.loc.fc     // Catch:{ Throwable -> 0x02b8, all -> 0x0307 }
            r18.<init>()     // Catch:{ Throwable -> 0x02b8, all -> 0x0307 }
            java.lang.String[] r4 = com.amap.openapi.dv.a     // Catch:{ Throwable -> 0x02b8, all -> 0x0307 }
            java.lang.StringBuilder r5 = new java.lang.StringBuilder     // Catch:{ Throwable -> 0x02b8, all -> 0x0307 }
            java.lang.String r6 = " id >= "
            r5.<init>(r6)     // Catch:{ Throwable -> 0x02b8, all -> 0x0307 }
            java.lang.StringBuilder r5 = r5.append(r10)     // Catch:{ Throwable -> 0x02b8, all -> 0x0307 }
            java.lang.String r6 = " and id <= "
            java.lang.StringBuilder r5 = r5.append(r6)     // Catch:{ Throwable -> 0x02b8, all -> 0x0307 }
            r0 = r16
            java.lang.StringBuilder r5 = r5.append(r0)     // Catch:{ Throwable -> 0x02b8, all -> 0x0307 }
            java.lang.String r5 = r5.toString()     // Catch:{ Throwable -> 0x02b8, all -> 0x0307 }
            r6 = 0
            r7 = 0
            android.database.Cursor r19 = r2.a(r3, r4, r5, r6, r7)     // Catch:{ Throwable -> 0x02b8, all -> 0x0307 }
            r0 = r19
            r14.add(r0)     // Catch:{ Throwable -> 0x02b8, all -> 0x0307 }
            if (r19 == 0) goto L_0x02e2
            int r4 = r19.getCount()     // Catch:{ Throwable -> 0x02b8, all -> 0x0307 }
            if (r4 == 0) goto L_0x02e2
            r8 = -1
            r4 = -1
            r6 = r12
        L_0x0115:
            boolean r12 = r19.moveToNext()     // Catch:{ Throwable -> 0x02b8, all -> 0x0307 }
            if (r12 == 0) goto L_0x0165
            r4 = 0
            r0 = r19
            long r4 = r0.getLong(r4)     // Catch:{ Throwable -> 0x02b8, all -> 0x0307 }
            r10 = 1
            long r10 = r10 + r4
            r4 = 1
            r0 = r19
            int r12 = r0.getInt(r4)     // Catch:{ Throwable -> 0x02b8, all -> 0x0307 }
            r4 = 2
            r0 = r19
            byte[] r13 = r0.getBlob(r4)     // Catch:{ Throwable -> 0x02b8, all -> 0x0307 }
            r4 = 3
            r0 = r19
            long r4 = r0.getLong(r4)     // Catch:{ Throwable -> 0x02b8, all -> 0x0307 }
            r20 = -1
            int r20 = (r8 > r20 ? 1 : (r8 == r20 ? 0 : -1))
            if (r20 != 0) goto L_0x0141
            r8 = r4
        L_0x0141:
            r20 = 4
            int r20 = r19.getInt(r20)     // Catch:{ Throwable -> 0x02b8, all -> 0x0307 }
            r0 = r18
            int r13 = com.amap.openapi.dy.a(r0, r13)     // Catch:{ Throwable -> 0x02b8, all -> 0x0307 }
            r0 = r18
            int r12 = com.amap.openapi.dy.a(r0, r12, r13, r4)     // Catch:{ Throwable -> 0x02b8, all -> 0x0307 }
            java.lang.Integer r12 = java.lang.Integer.valueOf(r12)     // Catch:{ Throwable -> 0x02b8, all -> 0x0307 }
            r15.add(r12)     // Catch:{ Throwable -> 0x02b8, all -> 0x0307 }
            r0 = r20
            long r12 = (long) r0     // Catch:{ Throwable -> 0x02b8, all -> 0x0307 }
            long r6 = r6 + r12
            r12 = 400000(0x61a80, double:1.976263E-318)
            int r12 = (r6 > r12 ? 1 : (r6 == r12 ? 0 : -1))
            if (r12 < 0) goto L_0x0115
        L_0x0165:
            r6 = r4
            com.amap.location.common.util.e.a(r19)     // Catch:{ Throwable -> 0x02b8, all -> 0x0307 }
            int r4 = r15.size()     // Catch:{ Throwable -> 0x02b8, all -> 0x0307 }
            int[] r12 = new int[r4]     // Catch:{ Throwable -> 0x02b8, all -> 0x0307 }
            r4 = 0
            r5 = r4
        L_0x0171:
            int r4 = r15.size()     // Catch:{ Throwable -> 0x02b8, all -> 0x0307 }
            if (r5 >= r4) goto L_0x0226
            java.lang.Object r4 = r15.get(r5)     // Catch:{ Throwable -> 0x02b8, all -> 0x0307 }
            java.lang.Integer r4 = (java.lang.Integer) r4     // Catch:{ Throwable -> 0x02b8, all -> 0x0307 }
            int r4 = r4.intValue()     // Catch:{ Throwable -> 0x02b8, all -> 0x0307 }
            r12[r5] = r4     // Catch:{ Throwable -> 0x02b8, all -> 0x0307 }
            int r4 = r5 + 1
            r5 = r4
            goto L_0x0171
        L_0x0187:
            if (r6 != 0) goto L_0x0223
            r6 = 1
            long r10 = r16 + r6
            r6 = 0
        L_0x018f:
            r8 = 400000(0x61a80, double:1.976263E-318)
            int r8 = (r6 > r8 ? 1 : (r6 == r8 ? 0 : -1))
            if (r8 >= 0) goto L_0x021d
            java.lang.StringBuilder r8 = new java.lang.StringBuilder     // Catch:{ Throwable -> 0x02b8, all -> 0x0307 }
            java.lang.String r9 = "select min(ID) from (select * from "
            r8.<init>(r9)     // Catch:{ Throwable -> 0x02b8, all -> 0x0307 }
            java.lang.StringBuilder r8 = r8.append(r13)     // Catch:{ Throwable -> 0x02b8, all -> 0x0307 }
            java.lang.String r9 = " where id < "
            java.lang.StringBuilder r8 = r8.append(r9)     // Catch:{ Throwable -> 0x02b8, all -> 0x0307 }
            java.lang.StringBuilder r8 = r8.append(r10)     // Catch:{ Throwable -> 0x02b8, all -> 0x0307 }
            java.lang.String r9 = " order by ID desc limit 0, 50)"
            java.lang.StringBuilder r8 = r8.append(r9)     // Catch:{ Throwable -> 0x02b8, all -> 0x0307 }
            java.lang.String r8 = r8.toString()     // Catch:{ Throwable -> 0x02b8, all -> 0x0307 }
            r9 = 0
            android.database.Cursor r15 = r12.rawQuery(r8, r9)     // Catch:{ Throwable -> 0x02b8, all -> 0x0307 }
            r14.add(r15)     // Catch:{ Throwable -> 0x02b8, all -> 0x0307 }
            if (r15 == 0) goto L_0x021a
            boolean r8 = r15.moveToFirst()     // Catch:{ Throwable -> 0x02b8, all -> 0x0307 }
            if (r8 == 0) goto L_0x021a
            r8 = 0
            long r8 = r15.getLong(r8)     // Catch:{ Throwable -> 0x02b8, all -> 0x0307 }
            com.amap.location.common.util.e.a(r15)     // Catch:{ Throwable -> 0x02b8, all -> 0x0307 }
            r18 = 0
            int r15 = (r8 > r18 ? 1 : (r8 == r18 ? 0 : -1))
            if (r15 <= 0) goto L_0x021d
            java.lang.StringBuilder r15 = new java.lang.StringBuilder     // Catch:{ Throwable -> 0x02b8, all -> 0x0307 }
            java.lang.String r18 = "select sum(size) from "
            r0 = r18
            r15.<init>(r0)     // Catch:{ Throwable -> 0x02b8, all -> 0x0307 }
            java.lang.StringBuilder r15 = r15.append(r13)     // Catch:{ Throwable -> 0x02b8, all -> 0x0307 }
            java.lang.String r18 = " where ID >= "
            r0 = r18
            java.lang.StringBuilder r15 = r15.append(r0)     // Catch:{ Throwable -> 0x02b8, all -> 0x0307 }
            java.lang.StringBuilder r15 = r15.append(r8)     // Catch:{ Throwable -> 0x02b8, all -> 0x0307 }
            java.lang.String r18 = " and ID < "
            r0 = r18
            java.lang.StringBuilder r15 = r15.append(r0)     // Catch:{ Throwable -> 0x02b8, all -> 0x0307 }
            java.lang.StringBuilder r10 = r15.append(r10)     // Catch:{ Throwable -> 0x02b8, all -> 0x0307 }
            java.lang.String r10 = r10.toString()     // Catch:{ Throwable -> 0x02b8, all -> 0x0307 }
            r11 = 0
            android.database.Cursor r10 = r12.rawQuery(r10, r11)     // Catch:{ Throwable -> 0x02b8, all -> 0x0307 }
            r14.add(r10)     // Catch:{ Throwable -> 0x02b8, all -> 0x0307 }
            r10.moveToFirst()     // Catch:{ Throwable -> 0x02b8, all -> 0x0307 }
            r11 = 0
            long r18 = r10.getLong(r11)     // Catch:{ Throwable -> 0x02b8, all -> 0x0307 }
            long r6 = r6 + r18
            com.amap.location.common.util.e.a(r10)     // Catch:{ Throwable -> 0x02b8, all -> 0x0307 }
            r10 = r8
            goto L_0x018f
        L_0x021a:
            com.amap.location.common.util.e.a(r15)     // Catch:{ Throwable -> 0x02b8, all -> 0x0307 }
        L_0x021d:
            r8 = 0
            int r6 = (r6 > r8 ? 1 : (r6 == r8 ? 0 : -1))
            if (r6 > 0) goto L_0x00c8
        L_0x0223:
            r10 = r4
            goto L_0x00c8
        L_0x0226:
            r0 = r22
            com.amap.openapi.dt r4 = r0.a     // Catch:{ Throwable -> 0x02b8, all -> 0x0307 }
            android.content.Context r4 = r4.a()     // Catch:{ Throwable -> 0x02b8, all -> 0x0307 }
            r0 = r18
            int r4 = com.amap.openapi.bk.a(r0, r4)     // Catch:{ Throwable -> 0x02b8, all -> 0x0307 }
            r0 = r18
            int r5 = com.amap.openapi.dz.b(r0, r12)     // Catch:{ Throwable -> 0x02b8, all -> 0x0307 }
            com.amap.openapi.dz.a(r18)     // Catch:{ Throwable -> 0x02b8, all -> 0x0307 }
            r12 = 1
            r0 = r18
            com.amap.openapi.dz.a(r0, r12)     // Catch:{ Throwable -> 0x02b8, all -> 0x0307 }
            r0 = r18
            com.amap.openapi.dz.a(r0, r4)     // Catch:{ Throwable -> 0x02b8, all -> 0x0307 }
            r0 = r18
            com.amap.openapi.dz.c(r0, r5)     // Catch:{ Throwable -> 0x02b8, all -> 0x0307 }
            int r4 = com.amap.openapi.dz.b(r18)     // Catch:{ Throwable -> 0x02b8, all -> 0x0307 }
            r0 = r18
            com.amap.openapi.dz.d(r0, r4)     // Catch:{ Throwable -> 0x02b8, all -> 0x0307 }
            r0 = r22
            com.amap.location.common.network.IHttpClient r4 = r0.b     // Catch:{ Throwable -> 0x02b8, all -> 0x0307 }
            r0 = r22
            com.amap.openapi.dt r5 = r0.a     // Catch:{ Throwable -> 0x02b8, all -> 0x0307 }
            r0 = r22
            com.amap.openapi.dn r12 = r0.c     // Catch:{ Throwable -> 0x02b8, all -> 0x0307 }
            int r12 = r12.b()     // Catch:{ Throwable -> 0x02b8, all -> 0x0307 }
            java.lang.String r5 = r5.a(r12)     // Catch:{ Throwable -> 0x02b8, all -> 0x0307 }
            byte[] r12 = r18.f()     // Catch:{ Throwable -> 0x02b8, all -> 0x0307 }
            r13 = 120000(0x1d4c0, float:1.68156E-40)
            boolean r4 = com.amap.openapi.ea.a(r4, r5, r12, r13)     // Catch:{ Throwable -> 0x02b8, all -> 0x0307 }
            if (r4 != 0) goto L_0x00c8
            java.lang.StringBuilder r4 = new java.lang.StringBuilder     // Catch:{ Throwable -> 0x02b8, all -> 0x0307 }
            java.lang.String r5 = "UpTunnel fail,条数是:"
            r4.<init>(r5)     // Catch:{ Throwable -> 0x02b8, all -> 0x0307 }
            int r5 = r15.size()     // Catch:{ Throwable -> 0x02b8, all -> 0x0307 }
            java.lang.StringBuilder r4 = r4.append(r5)     // Catch:{ Throwable -> 0x02b8, all -> 0x0307 }
            java.lang.String r5 = ", 最后一条 id  是:"
            java.lang.StringBuilder r4 = r4.append(r5)     // Catch:{ Throwable -> 0x02b8, all -> 0x0307 }
            java.lang.StringBuilder r4 = r4.append(r10)     // Catch:{ Throwable -> 0x02b8, all -> 0x0307 }
            java.lang.String r5 = ",第一条时间："
            java.lang.StringBuilder r4 = r4.append(r5)     // Catch:{ Throwable -> 0x02b8, all -> 0x0307 }
            java.lang.StringBuilder r4 = r4.append(r8)     // Catch:{ Throwable -> 0x02b8, all -> 0x0307 }
            java.lang.String r5 = ",最后一条时间："
            java.lang.StringBuilder r4 = r4.append(r5)     // Catch:{ Throwable -> 0x02b8, all -> 0x0307 }
            java.lang.StringBuilder r4 = r4.append(r6)     // Catch:{ Throwable -> 0x02b8, all -> 0x0307 }
            java.lang.String r4 = r4.toString()     // Catch:{ Throwable -> 0x02b8, all -> 0x0307 }
            r5 = 800001(0xc3501, float:1.12104E-39)
            byte[] r4 = r4.getBytes()     // Catch:{ Throwable -> 0x02b8, all -> 0x0307 }
            com.amap.openapi.dl.a(r5, r4)     // Catch:{ Throwable -> 0x02b8, all -> 0x0307 }
            goto L_0x00c8
        L_0x02b8:
            r2 = move-exception
            r3 = 800001(0xc3501, float:1.12104E-39)
            java.lang.String r2 = android.util.Log.getStackTraceString(r2)     // Catch:{ Exception -> 0x0328, all -> 0x0307 }
            byte[] r2 = r2.getBytes()     // Catch:{ Exception -> 0x0328, all -> 0x0307 }
            com.amap.openapi.dl.a(r3, r2)     // Catch:{ Exception -> 0x0328, all -> 0x0307 }
        L_0x02c7:
            int r2 = r14.size()
            if (r2 <= 0) goto L_0x002e
            r2 = 0
            r3 = r2
        L_0x02cf:
            int r2 = r14.size()
            if (r3 >= r2) goto L_0x0302
            java.lang.Object r2 = r14.get(r3)
            android.database.Cursor r2 = (android.database.Cursor) r2
            com.amap.location.common.util.e.a(r2)
            int r2 = r3 + 1
            r3 = r2
            goto L_0x02cf
        L_0x02e2:
            int r2 = r14.size()
            if (r2 <= 0) goto L_0x002e
            r2 = 0
            r3 = r2
        L_0x02ea:
            int r2 = r14.size()
            if (r3 >= r2) goto L_0x02fd
            java.lang.Object r2 = r14.get(r3)
            android.database.Cursor r2 = (android.database.Cursor) r2
            com.amap.location.common.util.e.a(r2)
            int r2 = r3 + 1
            r3 = r2
            goto L_0x02ea
        L_0x02fd:
            r14.clear()
            goto L_0x002e
        L_0x0302:
            r14.clear()
            goto L_0x002e
        L_0x0307:
            r2 = move-exception
            r4 = r2
            int r2 = r14.size()
            if (r2 <= 0) goto L_0x0327
            r2 = 0
            r3 = r2
        L_0x0311:
            int r2 = r14.size()
            if (r3 >= r2) goto L_0x0324
            java.lang.Object r2 = r14.get(r3)
            android.database.Cursor r2 = (android.database.Cursor) r2
            com.amap.location.common.util.e.a(r2)
            int r2 = r3 + 1
            r3 = r2
            goto L_0x0311
        L_0x0324:
            r14.clear()
        L_0x0327:
            throw r4
        L_0x0328:
            r2 = move-exception
            goto L_0x02c7
        */
        throw new UnsupportedOperationException("Method not decompiled: com.amap.openapi.Cdo.run():void");
    }
}
