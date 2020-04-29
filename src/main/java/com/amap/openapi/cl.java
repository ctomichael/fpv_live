package com.amap.openapi;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.text.TextUtils;
import com.amap.location.common.log.ALLog;
import dji.diagnostics.model.DJIDiagnosticsError;

/* compiled from: EventManager */
public class cl {
    private static volatile cl a = null;
    private a b;
    private Object c = new Object();

    /* compiled from: EventManager */
    static class a extends SQLiteOpenHelper {
        a(Context context) {
            super(context, "OffEvent.db", (SQLiteDatabase.CursorFactory) null, 1);
        }

        public void onCreate(SQLiteDatabase sQLiteDatabase) {
            cm.a(sQLiteDatabase);
        }

        public void onUpgrade(SQLiteDatabase sQLiteDatabase, int i, int i2) {
        }
    }

    private cl() {
    }

    public static cl a() {
        if (a == null) {
            synchronized (cl.class) {
                if (a == null) {
                    a = new cl();
                }
            }
        }
        return a;
    }

    /* JADX WARNING: Removed duplicated region for block: B:43:0x00c6 A[SYNTHETIC, Splitter:B:43:0x00c6] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private java.lang.String a(int r13) {
        /*
            r12 = this;
            java.lang.StringBuilder r10 = new java.lang.StringBuilder
            r10.<init>()
            java.util.ArrayList r11 = new java.util.ArrayList
            r11.<init>()
            r0 = 0
            com.amap.openapi.cl$a r1 = r12.b     // Catch:{ Exception -> 0x0108 }
            android.database.sqlite.SQLiteDatabase r0 = r1.getWritableDatabase()     // Catch:{ Exception -> 0x0108 }
        L_0x0011:
            if (r0 != 0) goto L_0x0018
            java.lang.String r0 = r10.toString()
        L_0x0017:
            return r0
        L_0x0018:
            r9 = 0
            r0.beginTransaction()     // Catch:{ Throwable -> 0x0104, all -> 0x00ff }
            java.lang.String r1 = "ACL"
            r2 = 2
            java.lang.String[] r2 = new java.lang.String[r2]     // Catch:{ Throwable -> 0x0104, all -> 0x00ff }
            r3 = 0
            java.lang.String r4 = com.amap.openapi.cm.a     // Catch:{ Throwable -> 0x0104, all -> 0x00ff }
            r2[r3] = r4     // Catch:{ Throwable -> 0x0104, all -> 0x00ff }
            r3 = 1
            java.lang.String r4 = com.amap.openapi.cm.b     // Catch:{ Throwable -> 0x0104, all -> 0x00ff }
            r2[r3] = r4     // Catch:{ Throwable -> 0x0104, all -> 0x00ff }
            r3 = 0
            r4 = 0
            r5 = 0
            r6 = 0
            java.lang.String r7 = "frequency DESC"
            java.lang.String r8 = java.lang.String.valueOf(r13)     // Catch:{ Throwable -> 0x0104, all -> 0x00ff }
            android.database.Cursor r2 = r0.query(r1, r2, r3, r4, r5, r6, r7, r8)     // Catch:{ Throwable -> 0x0104, all -> 0x00ff }
            if (r2 == 0) goto L_0x00ef
            boolean r1 = r2.moveToFirst()     // Catch:{ Throwable -> 0x006a }
            if (r1 == 0) goto L_0x00ef
        L_0x0043:
            boolean r1 = r2.isAfterLast()     // Catch:{ Throwable -> 0x006a }
            if (r1 != 0) goto L_0x00cf
            r1 = 0
            java.lang.String r3 = r2.getString(r1)     // Catch:{ Throwable -> 0x006a }
            r1 = 1
            int r4 = r2.getInt(r1)     // Catch:{ Throwable -> 0x006a }
            if (r3 == 0) goto L_0x00bf
            java.lang.String r1 = "_"
            java.lang.String[] r1 = r3.split(r1)     // Catch:{ Throwable -> 0x006a }
            if (r1 == 0) goto L_0x0066
            int r5 = r1.length     // Catch:{ Throwable -> 0x006a }
            r6 = 3
            if (r5 == r6) goto L_0x0092
            int r5 = r1.length     // Catch:{ Throwable -> 0x006a }
            r6 = 4
            if (r5 == r6) goto L_0x0092
        L_0x0066:
            r2.moveToNext()     // Catch:{ Throwable -> 0x006a }
            goto L_0x0043
        L_0x006a:
            r1 = move-exception
        L_0x006b:
            java.lang.String r3 = "@_18_7_@"
            java.lang.StringBuilder r4 = new java.lang.StringBuilder     // Catch:{ all -> 0x00c3 }
            java.lang.String r5 = "@_18_7_3_@"
            r4.<init>(r5)     // Catch:{ all -> 0x00c3 }
            java.lang.String r1 = android.util.Log.getStackTraceString(r1)     // Catch:{ all -> 0x00c3 }
            java.lang.StringBuilder r1 = r4.append(r1)     // Catch:{ all -> 0x00c3 }
            java.lang.String r1 = r1.toString()     // Catch:{ all -> 0x00c3 }
            com.amap.location.common.log.ALLog.trace(r3, r1)     // Catch:{ all -> 0x00c3 }
            if (r2 == 0) goto L_0x008a
            r2.close()     // Catch:{ Throwable -> 0x0102 }
        L_0x008a:
            r0.endTransaction()     // Catch:{ Throwable -> 0x0102 }
        L_0x008d:
            java.lang.String r0 = r10.toString()
            goto L_0x0017
        L_0x0092:
            int r1 = r1.length     // Catch:{ Throwable -> 0x006a }
            r5 = 4
            if (r1 != r5) goto L_0x00cd
            r1 = 0
        L_0x0097:
            int r5 = r10.length()     // Catch:{ Throwable -> 0x006a }
            if (r5 == 0) goto L_0x00a3
            java.lang.String r5 = "#"
            r10.append(r5)     // Catch:{ Throwable -> 0x006a }
        L_0x00a3:
            java.lang.StringBuilder r1 = r10.append(r1)     // Catch:{ Throwable -> 0x006a }
            java.lang.String r5 = "|"
            java.lang.StringBuilder r1 = r1.append(r5)     // Catch:{ Throwable -> 0x006a }
            java.lang.StringBuilder r1 = r1.append(r3)     // Catch:{ Throwable -> 0x006a }
            java.lang.String r5 = "|"
            java.lang.StringBuilder r1 = r1.append(r5)     // Catch:{ Throwable -> 0x006a }
            r1.append(r4)     // Catch:{ Throwable -> 0x006a }
            r11.add(r3)     // Catch:{ Throwable -> 0x006a }
        L_0x00bf:
            r2.moveToNext()     // Catch:{ Throwable -> 0x006a }
            goto L_0x0043
        L_0x00c3:
            r1 = move-exception
        L_0x00c4:
            if (r2 == 0) goto L_0x00c9
            r2.close()     // Catch:{ Throwable -> 0x00fd }
        L_0x00c9:
            r0.endTransaction()     // Catch:{ Throwable -> 0x00fd }
        L_0x00cc:
            throw r1
        L_0x00cd:
            r1 = 1
            goto L_0x0097
        L_0x00cf:
            java.util.Iterator r3 = r11.iterator()     // Catch:{ Throwable -> 0x006a }
        L_0x00d3:
            boolean r1 = r3.hasNext()     // Catch:{ Throwable -> 0x006a }
            if (r1 == 0) goto L_0x00ef
            java.lang.Object r1 = r3.next()     // Catch:{ Throwable -> 0x006a }
            java.lang.String r1 = (java.lang.String) r1     // Catch:{ Throwable -> 0x006a }
            java.lang.String r4 = "ACL"
            java.lang.String r5 = "id=?"
            r6 = 1
            java.lang.String[] r6 = new java.lang.String[r6]     // Catch:{ Throwable -> 0x006a }
            r7 = 0
            r6[r7] = r1     // Catch:{ Throwable -> 0x006a }
            r0.delete(r4, r5, r6)     // Catch:{ Throwable -> 0x006a }
            goto L_0x00d3
        L_0x00ef:
            r0.setTransactionSuccessful()     // Catch:{ Throwable -> 0x006a }
            if (r2 == 0) goto L_0x00f7
            r2.close()     // Catch:{ Throwable -> 0x00fb }
        L_0x00f7:
            r0.endTransaction()     // Catch:{ Throwable -> 0x00fb }
            goto L_0x008d
        L_0x00fb:
            r0 = move-exception
            goto L_0x008d
        L_0x00fd:
            r0 = move-exception
            goto L_0x00cc
        L_0x00ff:
            r1 = move-exception
            r2 = r9
            goto L_0x00c4
        L_0x0102:
            r0 = move-exception
            goto L_0x008d
        L_0x0104:
            r1 = move-exception
            r2 = r9
            goto L_0x006b
        L_0x0108:
            r1 = move-exception
            goto L_0x0011
        */
        throw new UnsupportedOperationException("Method not decompiled: com.amap.openapi.cl.a(int):java.lang.String");
    }

    /* JADX DEBUG: Failed to find minimal casts for resolve overloaded methods, cast all args instead
     method: ClspMth{android.content.ContentValues.put(java.lang.String, java.lang.Integer):void}
     arg types: [java.lang.String, int]
     candidates:
      ClspMth{android.content.ContentValues.put(java.lang.String, java.lang.Byte):void}
      ClspMth{android.content.ContentValues.put(java.lang.String, java.lang.Float):void}
      ClspMth{android.content.ContentValues.put(java.lang.String, java.lang.String):void}
      ClspMth{android.content.ContentValues.put(java.lang.String, java.lang.Long):void}
      ClspMth{android.content.ContentValues.put(java.lang.String, java.lang.Boolean):void}
      ClspMth{android.content.ContentValues.put(java.lang.String, byte[]):void}
      ClspMth{android.content.ContentValues.put(java.lang.String, java.lang.Double):void}
      ClspMth{android.content.ContentValues.put(java.lang.String, java.lang.Short):void}
      ClspMth{android.content.ContentValues.put(java.lang.String, java.lang.Integer):void} */
    /* JADX WARNING: Removed duplicated region for block: B:11:0x0051 A[SYNTHETIC, Splitter:B:11:0x0051] */
    /* JADX WARNING: Removed duplicated region for block: B:13:0x0055 A[SYNTHETIC, Splitter:B:13:0x0055] */
    /* JADX WARNING: Removed duplicated region for block: B:26:0x009d A[SYNTHETIC, Splitter:B:26:0x009d] */
    /* JADX WARNING: Removed duplicated region for block: B:36:? A[RETURN, SYNTHETIC] */
    /* JADX WARNING: Removed duplicated region for block: B:9:0x0032 A[Catch:{ Throwable -> 0x0076 }] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void a(java.lang.String r11) {
        /*
            r10 = this;
            r8 = 0
            r9 = 0
            com.amap.openapi.cl$a r0 = r10.b     // Catch:{ Throwable -> 0x00a7, all -> 0x0099 }
            android.database.sqlite.SQLiteDatabase r0 = r0.getWritableDatabase()     // Catch:{ Throwable -> 0x00a7, all -> 0x0099 }
            java.lang.String r1 = "ACL"
            r2 = 1
            java.lang.String[] r2 = new java.lang.String[r2]     // Catch:{ Throwable -> 0x00a7, all -> 0x0099 }
            r3 = 0
            java.lang.String r4 = com.amap.openapi.cm.b     // Catch:{ Throwable -> 0x00a7, all -> 0x0099 }
            r2[r3] = r4     // Catch:{ Throwable -> 0x00a7, all -> 0x0099 }
            java.lang.String r3 = "id=?"
            r4 = 1
            java.lang.String[] r4 = new java.lang.String[r4]     // Catch:{ Throwable -> 0x00a7, all -> 0x0099 }
            r5 = 0
            r4[r5] = r11     // Catch:{ Throwable -> 0x00a7, all -> 0x0099 }
            r5 = 0
            r6 = 0
            r7 = 0
            android.database.Cursor r1 = r0.query(r1, r2, r3, r4, r5, r6, r7)     // Catch:{ Throwable -> 0x00a7, all -> 0x0099 }
            if (r1 == 0) goto L_0x00aa
            boolean r2 = r1.moveToFirst()     // Catch:{ Throwable -> 0x0076 }
            if (r2 == 0) goto L_0x00aa
            r2 = 0
            int r2 = r1.getInt(r2)     // Catch:{ Throwable -> 0x0076 }
        L_0x0030:
            if (r2 != 0) goto L_0x0055
            android.content.ContentValues r2 = new android.content.ContentValues     // Catch:{ Throwable -> 0x0076 }
            r2.<init>()     // Catch:{ Throwable -> 0x0076 }
            java.lang.String r3 = "id"
            r2.put(r3, r11)     // Catch:{ Throwable -> 0x0076 }
            java.lang.String r3 = "frequency"
            r4 = 1
            java.lang.Integer r4 = java.lang.Integer.valueOf(r4)     // Catch:{ Throwable -> 0x0076 }
            r2.put(r3, r4)     // Catch:{ Throwable -> 0x0076 }
            java.lang.String r3 = "ACL"
            r4 = 0
            r0.insert(r3, r4, r2)     // Catch:{ Throwable -> 0x0076 }
        L_0x004f:
            if (r1 == 0) goto L_0x0054
            r1.close()     // Catch:{ Throwable -> 0x00a1 }
        L_0x0054:
            return
        L_0x0055:
            android.content.ContentValues r3 = new android.content.ContentValues     // Catch:{ Throwable -> 0x0076 }
            r3.<init>()     // Catch:{ Throwable -> 0x0076 }
            java.lang.String r4 = "frequency"
            int r2 = r2 + 1
            java.lang.Integer r2 = java.lang.Integer.valueOf(r2)     // Catch:{ Throwable -> 0x0076 }
            r3.put(r4, r2)     // Catch:{ Throwable -> 0x0076 }
            java.lang.String r2 = "ACL"
            java.lang.String r4 = "id=?"
            r5 = 1
            java.lang.String[] r5 = new java.lang.String[r5]     // Catch:{ Throwable -> 0x0076 }
            r6 = 0
            r5[r6] = r11     // Catch:{ Throwable -> 0x0076 }
            r0.update(r2, r3, r4, r5)     // Catch:{ Throwable -> 0x0076 }
            goto L_0x004f
        L_0x0076:
            r0 = move-exception
        L_0x0077:
            java.lang.String r2 = "@_18_7_@"
            java.lang.StringBuilder r3 = new java.lang.StringBuilder     // Catch:{ all -> 0x00a5 }
            java.lang.String r4 = "@_18_7_4_@"
            r3.<init>(r4)     // Catch:{ all -> 0x00a5 }
            java.lang.String r0 = android.util.Log.getStackTraceString(r0)     // Catch:{ all -> 0x00a5 }
            java.lang.StringBuilder r0 = r3.append(r0)     // Catch:{ all -> 0x00a5 }
            java.lang.String r0 = r0.toString()     // Catch:{ all -> 0x00a5 }
            com.amap.location.common.log.ALLog.trace(r2, r0)     // Catch:{ all -> 0x00a5 }
            if (r1 == 0) goto L_0x0054
            r1.close()     // Catch:{ Throwable -> 0x0097 }
            goto L_0x0054
        L_0x0097:
            r0 = move-exception
            goto L_0x0054
        L_0x0099:
            r0 = move-exception
            r1 = r8
        L_0x009b:
            if (r1 == 0) goto L_0x00a0
            r1.close()     // Catch:{ Throwable -> 0x00a3 }
        L_0x00a0:
            throw r0
        L_0x00a1:
            r0 = move-exception
            goto L_0x0054
        L_0x00a3:
            r1 = move-exception
            goto L_0x00a0
        L_0x00a5:
            r0 = move-exception
            goto L_0x009b
        L_0x00a7:
            r0 = move-exception
            r1 = r8
            goto L_0x0077
        L_0x00aa:
            r2 = r9
            goto L_0x0030
        */
        throw new UnsupportedOperationException("Method not decompiled: com.amap.openapi.cl.a(java.lang.String):void");
    }

    private void b(Context context) {
        if (this.b == null) {
            this.b = new a(context);
        }
    }

    public void a(Context context) {
        String a2;
        synchronized (this.c) {
            b(context);
        }
        if (TextUtils.isEmpty(com.amap.location.common.a.c(context))) {
            ALLog.trace("@_18_7_@", "@_18_7_5_@");
            return;
        }
        boolean a3 = cp.a(context, 500);
        int b2 = cp.b(context, 500);
        ALLog.trace("@_18_7_@", "@_18_7_1_@(" + a3 + "," + b2 + ")");
        if (a3 && b2 > 0 && (a2 = a(b2)) != null && a2.length() > 0) {
            com.amap.location.offline.upload.a.a(DJIDiagnosticsError.Navigation.QUICK_SHOT_STOP, a2.getBytes());
            int i = 0;
            String[] split = a2.split("#");
            if (split != null && split.length > 0) {
                i = split.length;
            }
            cp.c(context, i);
            ALLog.trace("@_18_7_@", "@_18_7_2_@" + i);
        }
    }

    public void a(Context context, bs bsVar) {
        synchronized (this.c) {
            b(context);
        }
        if (!TextUtils.isEmpty(bsVar.f)) {
            a(bsVar.f.replace(":", "_"));
        }
    }
}
