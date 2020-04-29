package com.amap.openapi;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;
import com.amap.location.common.log.ALLog;
import com.amap.location.common.model.AmapLoc;
import com.amap.location.security.Core;
import dji.publics.protocol.ResponseBase;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/* compiled from: OfflineDatabase */
public class by {
    private static final String[] a = {ResponseBase.STRING_ID, ResponseBase.STRING_LAT, ResponseBase.STRING_LNG, "acc", "conf", "timestamp"};
    private static final String[] b = {ResponseBase.STRING_ID, "originid", "frequency"};
    private static volatile by c = null;
    private bx d;
    private cb e;
    private ReadWriteLock f = new ReentrantReadWriteLock();

    private by(Context context) {
        this.d = new bx(context);
        this.e = new cb(context);
    }

    /* JADX WARNING: Removed duplicated region for block: B:23:0x0047 A[SYNTHETIC, Splitter:B:23:0x0047] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private int a(android.database.sqlite.SQLiteDatabase r10, java.lang.String r11, long r12) {
        /*
            r9 = this;
            r8 = 0
            r0 = 1
            java.lang.String[] r2 = new java.lang.String[r0]     // Catch:{ Throwable -> 0x003a, all -> 0x0044 }
            r0 = 0
            java.lang.String r1 = "frequency"
            r2[r0] = r1     // Catch:{ Throwable -> 0x003a, all -> 0x0044 }
            java.lang.String r3 = "id=?"
            r0 = 1
            java.lang.String[] r4 = new java.lang.String[r0]     // Catch:{ Throwable -> 0x003a, all -> 0x0044 }
            r0 = 0
            java.lang.String r1 = java.lang.String.valueOf(r12)     // Catch:{ Throwable -> 0x003a, all -> 0x0044 }
            r4[r0] = r1     // Catch:{ Throwable -> 0x003a, all -> 0x0044 }
            r5 = 0
            r6 = 0
            r7 = 0
            r0 = r10
            r1 = r11
            android.database.Cursor r1 = r0.query(r1, r2, r3, r4, r5, r6, r7)     // Catch:{ Throwable -> 0x003a, all -> 0x0044 }
            if (r1 == 0) goto L_0x0033
            boolean r0 = r1.moveToFirst()     // Catch:{ Throwable -> 0x0054, all -> 0x0051 }
            if (r0 == 0) goto L_0x0033
            r0 = 0
            int r0 = r1.getInt(r0)     // Catch:{ Throwable -> 0x0054, all -> 0x0051 }
            if (r1 == 0) goto L_0x0032
            r1.close()     // Catch:{ Throwable -> 0x004b }
        L_0x0032:
            return r0
        L_0x0033:
            if (r1 == 0) goto L_0x0038
            r1.close()     // Catch:{ Throwable -> 0x004d }
        L_0x0038:
            r0 = -1
            goto L_0x0032
        L_0x003a:
            r0 = move-exception
            r0 = r8
        L_0x003c:
            if (r0 == 0) goto L_0x0038
            r0.close()     // Catch:{ Throwable -> 0x0042 }
            goto L_0x0038
        L_0x0042:
            r0 = move-exception
            goto L_0x0038
        L_0x0044:
            r0 = move-exception
        L_0x0045:
            if (r8 == 0) goto L_0x004a
            r8.close()     // Catch:{ Throwable -> 0x004f }
        L_0x004a:
            throw r0
        L_0x004b:
            r1 = move-exception
            goto L_0x0032
        L_0x004d:
            r0 = move-exception
            goto L_0x0038
        L_0x004f:
            r1 = move-exception
            goto L_0x004a
        L_0x0051:
            r0 = move-exception
            r8 = r1
            goto L_0x0045
        L_0x0054:
            r0 = move-exception
            r0 = r1
            goto L_0x003c
        */
        throw new UnsupportedOperationException("Method not decompiled: com.amap.openapi.by.a(android.database.sqlite.SQLiteDatabase, java.lang.String, long):int");
    }

    private ContentValues a(ContentValues contentValues, long j, String str, long j2, int i) {
        contentValues.clear();
        contentValues.put(ResponseBase.STRING_ID, Long.valueOf(j));
        if (!TextUtils.isEmpty(str)) {
            contentValues.put("originid", str);
        } else {
            contentValues.put("originid", Long.valueOf(j2));
        }
        contentValues.put("frequency", Integer.valueOf(i));
        return contentValues;
    }

    private ContentValues a(ContentValues contentValues, ci ciVar) {
        contentValues.clear();
        contentValues.put(ResponseBase.STRING_ID, Long.valueOf(ciVar.a()));
        contentValues.put(ResponseBase.STRING_LAT, Integer.valueOf(ciVar.b()));
        contentValues.put(ResponseBase.STRING_LNG, Integer.valueOf(ciVar.c()));
        contentValues.put("acc", Short.valueOf(ciVar.d()));
        contentValues.put("conf", Byte.valueOf(ciVar.e()));
        contentValues.put("timestamp", Long.valueOf(System.currentTimeMillis() / 1000));
        return contentValues;
    }

    public static by a(@NonNull Context context) {
        if (c == null) {
            synchronized (by.class) {
                if (c == null) {
                    c = new by(context);
                }
            }
        }
        return c;
    }

    private HashSet<Long> a(List<Long> list) {
        HashSet<Long> hashSet = new HashSet<>();
        if (list != null) {
            for (Long l : list) {
                hashSet.add(Long.valueOf(cn.a(l.longValue())));
            }
        }
        return hashSet;
    }

    private void a(SQLiteDatabase sQLiteDatabase, String str, ContentValues contentValues) {
        try {
            sQLiteDatabase.replace(str, null, contentValues);
        } catch (Throwable th) {
        }
    }

    private void a(SQLiteDatabase sQLiteDatabase, String str, ContentValues contentValues, boolean z) {
        if (z) {
            sQLiteDatabase.insertWithOnConflict(str, null, contentValues, 4);
        } else {
            sQLiteDatabase.insertWithOnConflict(str, null, contentValues, 5);
        }
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
    private void a(SQLiteDatabase sQLiteDatabase, String str, HashSet<Long> hashSet, ContentValues contentValues) {
        contentValues.clear();
        contentValues.put(ResponseBase.STRING_LAT, (Integer) 0);
        contentValues.put(ResponseBase.STRING_LNG, (Integer) 0);
        contentValues.put("acc", (Integer) 0);
        contentValues.put("conf", (Integer) -1);
        contentValues.put("timestamp", Long.valueOf(System.currentTimeMillis() / 1000));
        Iterator<Long> it2 = hashSet.iterator();
        while (it2.hasNext()) {
            contentValues.put(ResponseBase.STRING_ID, Long.valueOf(it2.next().longValue()));
            a(sQLiteDatabase, str, contentValues);
        }
    }

    private void a(SQLiteDatabase sQLiteDatabase, HashSet<Long> hashSet, HashSet<Long> hashSet2) {
        if (hashSet != null) {
            Iterator<Long> it2 = hashSet.iterator();
            while (it2.hasNext()) {
                sQLiteDatabase.delete("CL", "id=?", new String[]{String.valueOf(it2.next().longValue())});
            }
        }
        if (hashSet2 != null) {
            Iterator<Long> it3 = hashSet2.iterator();
            while (it3.hasNext()) {
                sQLiteDatabase.delete("AP", "id=?", new String[]{String.valueOf(it3.next())});
            }
        }
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
    private void a(String str, ContentValues contentValues, long j) {
        contentValues.clear();
        contentValues.put("conf", (Integer) 0);
        this.d.getWritableDatabase().update(str, contentValues, "id=?", new String[]{String.valueOf(j)});
    }

    private boolean a(SQLiteDatabase sQLiteDatabase, String str, long j, ContentValues contentValues) {
        int a2 = a(sQLiteDatabase, str, j);
        if (a2 < 0) {
            return false;
        }
        contentValues.clear();
        contentValues.put("frequency", Integer.valueOf(a2 + 1));
        sQLiteDatabase.update(str, contentValues, "id=?", new String[]{String.valueOf(j)});
        return true;
    }

    private HashSet<Long> b(List<String> list) {
        HashSet<Long> hashSet = new HashSet<>();
        if (list != null) {
            for (String str : list) {
                long a2 = cn.a(str);
                if (a2 != -1) {
                    hashSet.add(Long.valueOf(a2));
                }
            }
        }
        return hashSet;
    }

    private void b(SQLiteDatabase sQLiteDatabase, String str, long j, ContentValues contentValues) {
        try {
            contentValues.clear();
            contentValues.put("time", Long.valueOf(System.currentTimeMillis() / 1000));
            sQLiteDatabase.update(str, contentValues, "id=?", new String[]{String.valueOf(j)});
        } catch (Throwable th) {
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:31:?, code lost:
        r2.close();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:37:?, code lost:
        r10.close();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:41:0x00c7, code lost:
        r0 = th;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:44:0x00cc, code lost:
        r0 = th;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:45:0x00cd, code lost:
        r2 = r10;
        r1 = null;
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Removed duplicated region for block: B:16:0x006e A[SYNTHETIC, Splitter:B:16:0x006e] */
    /* JADX WARNING: Removed duplicated region for block: B:19:0x0073  */
    /* JADX WARNING: Removed duplicated region for block: B:30:0x00b5 A[SYNTHETIC, Splitter:B:30:0x00b5] */
    /* JADX WARNING: Removed duplicated region for block: B:36:0x00bf A[SYNTHETIC, Splitter:B:36:0x00bf] */
    /* JADX WARNING: Removed duplicated region for block: B:41:0x00c7 A[ExcHandler: all (th java.lang.Throwable), Splitter:B:4:0x0023] */
    /* JADX WARNING: Removed duplicated region for block: B:49:? A[RETURN, SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public com.amap.openapi.bs a(java.lang.String r15, long r16) {
        /*
            r14 = this;
            r11 = 0
            r8 = 0
            com.amap.openapi.bx r0 = r14.d     // Catch:{ Throwable -> 0x0096, all -> 0x00bb }
            android.database.sqlite.SQLiteDatabase r0 = r0.getReadableDatabase()     // Catch:{ Throwable -> 0x0096, all -> 0x00bb }
            java.lang.String r1 = "CL"
            java.lang.String[] r2 = com.amap.openapi.by.a     // Catch:{ Throwable -> 0x0096, all -> 0x00bb }
            java.lang.String r3 = "id=?"
            r4 = 1
            java.lang.String[] r4 = new java.lang.String[r4]     // Catch:{ Throwable -> 0x0096, all -> 0x00bb }
            r5 = 0
            java.lang.String r6 = java.lang.String.valueOf(r16)     // Catch:{ Throwable -> 0x0096, all -> 0x00bb }
            r4[r5] = r6     // Catch:{ Throwable -> 0x0096, all -> 0x00bb }
            r5 = 0
            r6 = 0
            r7 = 0
            android.database.Cursor r10 = r0.query(r1, r2, r3, r4, r5, r6, r7)     // Catch:{ Throwable -> 0x0096, all -> 0x00bb }
            if (r10 == 0) goto L_0x0094
            boolean r1 = r10.moveToFirst()     // Catch:{ Throwable -> 0x00cc, all -> 0x00c7 }
            if (r1 == 0) goto L_0x0094
            r1 = 5
            long r12 = r10.getLong(r1)     // Catch:{ Throwable -> 0x00cc, all -> 0x00c7 }
            r2 = 15552000(0xed4e00, double:7.683709E-317)
            long r2 = r2 + r12
            long r4 = java.lang.System.currentTimeMillis()     // Catch:{ Throwable -> 0x00cc, all -> 0x00c7 }
            r6 = 1000(0x3e8, double:4.94E-321)
            long r4 = r4 / r6
            int r1 = (r2 > r4 ? 1 : (r2 == r4 ? 0 : -1))
            if (r1 <= 0) goto L_0x0081
            r0 = 1
            int r6 = r10.getInt(r0)     // Catch:{ Throwable -> 0x00cc, all -> 0x00c7 }
            r0 = 2
            int r7 = r10.getInt(r0)     // Catch:{ Throwable -> 0x00cc, all -> 0x00c7 }
            r0 = 3
            int r8 = r10.getInt(r0)     // Catch:{ Throwable -> 0x00cc, all -> 0x00c7 }
            r0 = 4
            int r9 = r10.getInt(r0)     // Catch:{ Throwable -> 0x00cc, all -> 0x00c7 }
            com.amap.openapi.bs r1 = new com.amap.openapi.bs     // Catch:{ Throwable -> 0x00cc, all -> 0x00c7 }
            r2 = 1
            r3 = r15
            r4 = r16
            r1.<init>(r2, r3, r4, r6, r7, r8, r9)     // Catch:{ Throwable -> 0x00cc, all -> 0x00c7 }
            r2 = 604800(0x93a80, double:2.98811E-318)
            long r2 = r2 + r12
            long r4 = java.lang.System.currentTimeMillis()     // Catch:{ Throwable -> 0x00d0, all -> 0x00c7 }
            r6 = 1000(0x3e8, double:4.94E-321)
            long r4 = r4 / r6
            int r0 = (r2 > r4 ? 1 : (r2 == r4 ? 0 : -1))
            if (r0 >= 0) goto L_0x006c
            r0 = 1
            r1.h = r0     // Catch:{ Throwable -> 0x00d0, all -> 0x00c7 }
        L_0x006c:
            if (r10 == 0) goto L_0x0071
            r10.close()     // Catch:{ Throwable -> 0x00c3 }
        L_0x0071:
            if (r1 != 0) goto L_0x0080
            com.amap.openapi.bs r1 = new com.amap.openapi.bs
            r2 = 0
            r6 = 0
            r7 = 0
            r8 = 0
            r9 = -1
            r3 = r15
            r4 = r16
            r1.<init>(r2, r3, r4, r6, r7, r8, r9)
        L_0x0080:
            return r1
        L_0x0081:
            java.lang.String r1 = "CL"
            java.lang.String r2 = "id=?"
            r3 = 1
            java.lang.String[] r3 = new java.lang.String[r3]     // Catch:{ Throwable -> 0x00cc, all -> 0x00c7 }
            r4 = 0
            java.lang.String r5 = java.lang.String.valueOf(r16)     // Catch:{ Throwable -> 0x00cc, all -> 0x00c7 }
            r3[r4] = r5     // Catch:{ Throwable -> 0x00cc, all -> 0x00c7 }
            r0.delete(r1, r2, r3)     // Catch:{ Throwable -> 0x00cc, all -> 0x00c7 }
        L_0x0094:
            r1 = r11
            goto L_0x006c
        L_0x0096:
            r0 = move-exception
            r2 = r8
            r1 = r11
        L_0x0099:
            java.lang.String r3 = "@_18_4_@"
            java.lang.StringBuilder r4 = new java.lang.StringBuilder     // Catch:{ all -> 0x00c9 }
            java.lang.String r5 = "@_18_4_1_@"
            r4.<init>(r5)     // Catch:{ all -> 0x00c9 }
            java.lang.String r0 = android.util.Log.getStackTraceString(r0)     // Catch:{ all -> 0x00c9 }
            java.lang.StringBuilder r0 = r4.append(r0)     // Catch:{ all -> 0x00c9 }
            java.lang.String r0 = r0.toString()     // Catch:{ all -> 0x00c9 }
            com.amap.location.common.log.ALLog.trace(r3, r0)     // Catch:{ all -> 0x00c9 }
            if (r2 == 0) goto L_0x0071
            r2.close()     // Catch:{ Throwable -> 0x00b9 }
            goto L_0x0071
        L_0x00b9:
            r0 = move-exception
            goto L_0x0071
        L_0x00bb:
            r0 = move-exception
            r10 = r8
        L_0x00bd:
            if (r10 == 0) goto L_0x00c2
            r10.close()     // Catch:{ Throwable -> 0x00c5 }
        L_0x00c2:
            throw r0
        L_0x00c3:
            r0 = move-exception
            goto L_0x0071
        L_0x00c5:
            r1 = move-exception
            goto L_0x00c2
        L_0x00c7:
            r0 = move-exception
            goto L_0x00bd
        L_0x00c9:
            r0 = move-exception
            r10 = r2
            goto L_0x00bd
        L_0x00cc:
            r0 = move-exception
            r2 = r10
            r1 = r11
            goto L_0x0099
        L_0x00d0:
            r0 = move-exception
            r2 = r10
            goto L_0x0099
        */
        throw new UnsupportedOperationException("Method not decompiled: com.amap.openapi.by.a(java.lang.String, long):com.amap.openapi.bs");
    }

    /* JADX WARNING: Removed duplicated region for block: B:24:0x008b A[SYNTHETIC, Splitter:B:24:0x008b] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public java.util.List<java.lang.String> a(int r12, int r13) {
        /*
            r11 = this;
            r9 = 0
            java.util.ArrayList r10 = new java.util.ArrayList
            r10.<init>()
            com.amap.openapi.cb r0 = r11.e     // Catch:{ Throwable -> 0x0096, all -> 0x0088 }
            android.database.sqlite.SQLiteDatabase r0 = r0.getReadableDatabase()     // Catch:{ Throwable -> 0x0096, all -> 0x0088 }
            java.lang.String r1 = "CL"
            java.lang.String[] r2 = com.amap.openapi.by.b     // Catch:{ Throwable -> 0x0096, all -> 0x0088 }
            java.lang.StringBuilder r3 = new java.lang.StringBuilder     // Catch:{ Throwable -> 0x0096, all -> 0x0088 }
            java.lang.String r4 = "frequency>="
            r3.<init>(r4)     // Catch:{ Throwable -> 0x0096, all -> 0x0088 }
            java.lang.StringBuilder r3 = r3.append(r12)     // Catch:{ Throwable -> 0x0096, all -> 0x0088 }
            java.lang.String r4 = " AND time<"
            java.lang.StringBuilder r3 = r3.append(r4)     // Catch:{ Throwable -> 0x0096, all -> 0x0088 }
            long r4 = java.lang.System.currentTimeMillis()     // Catch:{ Throwable -> 0x0096, all -> 0x0088 }
            r6 = 1000(0x3e8, double:4.94E-321)
            long r4 = r4 / r6
            r6 = 86400(0x15180, double:4.26873E-319)
            long r4 = r4 - r6
            java.lang.StringBuilder r3 = r3.append(r4)     // Catch:{ Throwable -> 0x0096, all -> 0x0088 }
            java.lang.String r3 = r3.toString()     // Catch:{ Throwable -> 0x0096, all -> 0x0088 }
            r4 = 0
            r5 = 0
            r6 = 0
            java.lang.String r7 = "frequency DESC"
            java.lang.String r8 = java.lang.String.valueOf(r13)     // Catch:{ Throwable -> 0x0096, all -> 0x0088 }
            android.database.Cursor r1 = r0.query(r1, r2, r3, r4, r5, r6, r7, r8)     // Catch:{ Throwable -> 0x0096, all -> 0x0088 }
            if (r1 == 0) goto L_0x0080
            boolean r0 = r1.moveToFirst()     // Catch:{ Throwable -> 0x005f }
            if (r0 == 0) goto L_0x0080
        L_0x004d:
            boolean r0 = r1.isAfterLast()     // Catch:{ Throwable -> 0x005f }
            if (r0 != 0) goto L_0x0080
            r0 = 1
            java.lang.String r0 = r1.getString(r0)     // Catch:{ Throwable -> 0x005f }
            r10.add(r0)     // Catch:{ Throwable -> 0x005f }
            r1.moveToNext()     // Catch:{ Throwable -> 0x005f }
            goto L_0x004d
        L_0x005f:
            r0 = move-exception
        L_0x0060:
            java.lang.String r2 = "@_18_4_@"
            java.lang.StringBuilder r3 = new java.lang.StringBuilder     // Catch:{ all -> 0x0093 }
            java.lang.String r4 = "@_18_4_3_@"
            r3.<init>(r4)     // Catch:{ all -> 0x0093 }
            java.lang.String r0 = android.util.Log.getStackTraceString(r0)     // Catch:{ all -> 0x0093 }
            java.lang.StringBuilder r0 = r3.append(r0)     // Catch:{ all -> 0x0093 }
            java.lang.String r0 = r0.toString()     // Catch:{ all -> 0x0093 }
            com.amap.location.common.log.ALLog.trace(r2, r0)     // Catch:{ all -> 0x0093 }
            if (r1 == 0) goto L_0x007f
            r1.close()     // Catch:{ Throwable -> 0x008f }
        L_0x007f:
            return r10
        L_0x0080:
            if (r1 == 0) goto L_0x007f
            r1.close()     // Catch:{ Throwable -> 0x0086 }
            goto L_0x007f
        L_0x0086:
            r0 = move-exception
            goto L_0x007f
        L_0x0088:
            r0 = move-exception
        L_0x0089:
            if (r9 == 0) goto L_0x008e
            r9.close()     // Catch:{ Throwable -> 0x0091 }
        L_0x008e:
            throw r0
        L_0x008f:
            r0 = move-exception
            goto L_0x007f
        L_0x0091:
            r1 = move-exception
            goto L_0x008e
        L_0x0093:
            r0 = move-exception
            r9 = r1
            goto L_0x0089
        L_0x0096:
            r0 = move-exception
            r1 = r9
            goto L_0x0060
        */
        throw new UnsupportedOperationException("Method not decompiled: com.amap.openapi.by.a(int, int):java.util.List");
    }

    /* JADX WARNING: Removed duplicated region for block: B:50:0x0117 A[SYNTHETIC, Splitter:B:50:0x0117] */
    /* JADX WARNING: Removed duplicated region for block: B:53:0x011c A[Catch:{ Throwable -> 0x0123 }] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void a() {
        /*
            r12 = this;
            r10 = 7776000(0x76a700, double:3.8418545E-317)
            r8 = 0
            com.amap.openapi.bx r0 = r12.d     // Catch:{ Throwable -> 0x000d }
            android.database.sqlite.SQLiteDatabase r0 = r0.getWritableDatabase()     // Catch:{ Throwable -> 0x000d }
        L_0x000a:
            if (r0 != 0) goto L_0x0010
        L_0x000c:
            return
        L_0x000d:
            r0 = move-exception
            r0 = r8
            goto L_0x000a
        L_0x0010:
            r0.beginTransaction()     // Catch:{ Throwable -> 0x012d, all -> 0x0112 }
            long r2 = java.lang.System.currentTimeMillis()     // Catch:{ Throwable -> 0x012d, all -> 0x0112 }
            r4 = 1000(0x3e8, double:4.94E-321)
            long r2 = r2 / r4
            long r2 = r2 - r10
            java.lang.String r1 = "AP"
            java.lang.String r4 = "timestamp<?"
            r5 = 1
            java.lang.String[] r5 = new java.lang.String[r5]     // Catch:{ Throwable -> 0x012d, all -> 0x0112 }
            r6 = 0
            java.lang.String r2 = java.lang.String.valueOf(r2)     // Catch:{ Throwable -> 0x012d, all -> 0x0112 }
            r5[r6] = r2     // Catch:{ Throwable -> 0x012d, all -> 0x0112 }
            r0.delete(r1, r4, r5)     // Catch:{ Throwable -> 0x012d, all -> 0x0112 }
            long r2 = java.lang.System.currentTimeMillis()     // Catch:{ Throwable -> 0x012d, all -> 0x0112 }
            r4 = 1000(0x3e8, double:4.94E-321)
            long r2 = r2 / r4
            long r2 = r2 - r10
            java.lang.String r1 = "CL"
            java.lang.String r4 = "timestamp<?"
            r5 = 1
            java.lang.String[] r5 = new java.lang.String[r5]     // Catch:{ Throwable -> 0x012d, all -> 0x0112 }
            r6 = 0
            java.lang.String r2 = java.lang.String.valueOf(r2)     // Catch:{ Throwable -> 0x012d, all -> 0x0112 }
            r5[r6] = r2     // Catch:{ Throwable -> 0x012d, all -> 0x0112 }
            r0.delete(r1, r4, r5)     // Catch:{ Throwable -> 0x012d, all -> 0x0112 }
            java.lang.String r1 = "AP"
            r2 = 1
            java.lang.String[] r2 = new java.lang.String[r2]     // Catch:{ Throwable -> 0x012d, all -> 0x0112 }
            r3 = 0
            java.lang.String r4 = "id"
            r2[r3] = r4     // Catch:{ Throwable -> 0x012d, all -> 0x0112 }
            r3 = 0
            r4 = 0
            r5 = 0
            r6 = 0
            java.lang.String r7 = "timestamp DESC,frequency DESC LIMIT 200000,-1"
            android.database.Cursor r9 = r0.query(r1, r2, r3, r4, r5, r6, r7)     // Catch:{ Throwable -> 0x012d, all -> 0x0112 }
            if (r9 == 0) goto L_0x00b9
            boolean r1 = r9.moveToFirst()     // Catch:{ Throwable -> 0x008a, all -> 0x0125 }
            if (r1 == 0) goto L_0x00b9
        L_0x0068:
            boolean r1 = r9.isAfterLast()     // Catch:{ Throwable -> 0x008a, all -> 0x0125 }
            if (r1 != 0) goto L_0x00b9
            r1 = 0
            long r2 = r9.getLong(r1)     // Catch:{ Throwable -> 0x008a, all -> 0x0125 }
            java.lang.String r1 = "AP"
            java.lang.String r4 = "id=?"
            r5 = 1
            java.lang.String[] r5 = new java.lang.String[r5]     // Catch:{ Throwable -> 0x008a, all -> 0x0125 }
            r6 = 0
            java.lang.String r2 = java.lang.String.valueOf(r2)     // Catch:{ Throwable -> 0x008a, all -> 0x0125 }
            r5[r6] = r2     // Catch:{ Throwable -> 0x008a, all -> 0x0125 }
            r0.delete(r1, r4, r5)     // Catch:{ Throwable -> 0x008a, all -> 0x0125 }
            r9.moveToNext()     // Catch:{ Throwable -> 0x008a, all -> 0x0125 }
            goto L_0x0068
        L_0x008a:
            r1 = move-exception
            r2 = r8
            r3 = r9
        L_0x008d:
            java.lang.String r4 = "@_18_4_@"
            java.lang.StringBuilder r5 = new java.lang.StringBuilder     // Catch:{ all -> 0x012a }
            java.lang.String r6 = "@_18_4_11_@"
            r5.<init>(r6)     // Catch:{ all -> 0x012a }
            java.lang.String r1 = android.util.Log.getStackTraceString(r1)     // Catch:{ all -> 0x012a }
            java.lang.StringBuilder r1 = r5.append(r1)     // Catch:{ all -> 0x012a }
            java.lang.String r1 = r1.toString()     // Catch:{ all -> 0x012a }
            com.amap.location.common.log.ALLog.trace(r4, r1)     // Catch:{ all -> 0x012a }
            if (r3 == 0) goto L_0x00ac
            r3.close()     // Catch:{ Throwable -> 0x00b6 }
        L_0x00ac:
            if (r2 == 0) goto L_0x00b1
            r2.close()     // Catch:{ Throwable -> 0x00b6 }
        L_0x00b1:
            r0.endTransaction()     // Catch:{ Throwable -> 0x00b6 }
            goto L_0x000c
        L_0x00b6:
            r0 = move-exception
            goto L_0x000c
        L_0x00b9:
            java.lang.String r1 = "CL"
            r2 = 1
            java.lang.String[] r2 = new java.lang.String[r2]     // Catch:{ Throwable -> 0x008a, all -> 0x0125 }
            r3 = 0
            java.lang.String r4 = "id"
            r2[r3] = r4     // Catch:{ Throwable -> 0x008a, all -> 0x0125 }
            r3 = 0
            r4 = 0
            r5 = 0
            r6 = 0
            java.lang.String r7 = "timestamp DESC,frequency DESC LIMIT 200000,-1"
            android.database.Cursor r2 = r0.query(r1, r2, r3, r4, r5, r6, r7)     // Catch:{ Throwable -> 0x008a, all -> 0x0125 }
            if (r2 == 0) goto L_0x00fd
            boolean r1 = r2.moveToFirst()     // Catch:{ Throwable -> 0x00fa, all -> 0x0128 }
            if (r1 == 0) goto L_0x00fd
        L_0x00d8:
            boolean r1 = r2.isAfterLast()     // Catch:{ Throwable -> 0x00fa, all -> 0x0128 }
            if (r1 != 0) goto L_0x00fd
            r1 = 0
            long r4 = r2.getLong(r1)     // Catch:{ Throwable -> 0x00fa, all -> 0x0128 }
            java.lang.String r1 = "CL"
            java.lang.String r3 = "id=?"
            r6 = 1
            java.lang.String[] r6 = new java.lang.String[r6]     // Catch:{ Throwable -> 0x00fa, all -> 0x0128 }
            r7 = 0
            java.lang.String r4 = java.lang.String.valueOf(r4)     // Catch:{ Throwable -> 0x00fa, all -> 0x0128 }
            r6[r7] = r4     // Catch:{ Throwable -> 0x00fa, all -> 0x0128 }
            r0.delete(r1, r3, r6)     // Catch:{ Throwable -> 0x00fa, all -> 0x0128 }
            r2.moveToNext()     // Catch:{ Throwable -> 0x00fa, all -> 0x0128 }
            goto L_0x00d8
        L_0x00fa:
            r1 = move-exception
            r3 = r9
            goto L_0x008d
        L_0x00fd:
            r0.setTransactionSuccessful()     // Catch:{ Throwable -> 0x00fa, all -> 0x0128 }
            if (r9 == 0) goto L_0x0105
            r9.close()     // Catch:{ Throwable -> 0x010f }
        L_0x0105:
            if (r2 == 0) goto L_0x010a
            r2.close()     // Catch:{ Throwable -> 0x010f }
        L_0x010a:
            r0.endTransaction()     // Catch:{ Throwable -> 0x010f }
            goto L_0x000c
        L_0x010f:
            r0 = move-exception
            goto L_0x000c
        L_0x0112:
            r1 = move-exception
            r2 = r8
            r9 = r8
        L_0x0115:
            if (r9 == 0) goto L_0x011a
            r9.close()     // Catch:{ Throwable -> 0x0123 }
        L_0x011a:
            if (r2 == 0) goto L_0x011f
            r2.close()     // Catch:{ Throwable -> 0x0123 }
        L_0x011f:
            r0.endTransaction()     // Catch:{ Throwable -> 0x0123 }
        L_0x0122:
            throw r1
        L_0x0123:
            r0 = move-exception
            goto L_0x0122
        L_0x0125:
            r1 = move-exception
            r2 = r8
            goto L_0x0115
        L_0x0128:
            r1 = move-exception
            goto L_0x0115
        L_0x012a:
            r1 = move-exception
            r9 = r3
            goto L_0x0115
        L_0x012d:
            r1 = move-exception
            r2 = r8
            r3 = r8
            goto L_0x008d
        */
        throw new UnsupportedOperationException("Method not decompiled: com.amap.openapi.by.a():void");
    }

    /* JADX DEBUG: Failed to find minimal casts for resolve overloaded methods, cast all args instead
     method: com.amap.openapi.by.a(android.database.sqlite.SQLiteDatabase, java.lang.String, android.content.ContentValues, boolean):void
     arg types: [android.database.sqlite.SQLiteDatabase, java.lang.String, android.content.ContentValues, int]
     candidates:
      com.amap.openapi.by.a(android.database.sqlite.SQLiteDatabase, java.lang.String, java.util.HashSet<java.lang.Long>, android.content.ContentValues):void
      com.amap.openapi.by.a(android.database.sqlite.SQLiteDatabase, java.lang.String, long, android.content.ContentValues):boolean
      com.amap.openapi.by.a(com.amap.openapi.ck, java.util.List<java.lang.Long>, java.util.List<java.lang.String>, android.content.Context):void
      com.amap.openapi.by.a(android.database.sqlite.SQLiteDatabase, java.lang.String, android.content.ContentValues, boolean):void */
    public void a(bs bsVar) {
        long j = bsVar.g;
        String str = bsVar.f;
        if (!TextUtils.isEmpty(str)) {
            ContentValues contentValues = new ContentValues();
            this.f.readLock().lock();
            try {
                SQLiteDatabase writableDatabase = this.d.getWritableDatabase();
                SQLiteDatabase writableDatabase2 = this.e.getWritableDatabase();
                if (bsVar.a) {
                    if (bsVar.e > 60) {
                        a(writableDatabase, "CL", j, contentValues);
                    }
                    if (bsVar.h) {
                        a(contentValues, j, str, 0, 100000);
                        a(writableDatabase2, "CL", contentValues, true);
                    }
                } else {
                    a(contentValues, j, str, 0, 0);
                    a(writableDatabase2, "CL", contentValues, true);
                    a(writableDatabase2, "CL", j, contentValues);
                }
            } catch (Throwable th) {
                ALLog.trace("@_18_4_@", "@_18_4_5_@" + Log.getStackTraceString(th));
            } finally {
                this.f.readLock().unlock();
            }
        }
    }

    /* JADX DEBUG: Failed to find minimal casts for resolve overloaded methods, cast all args instead
     method: com.amap.openapi.by.a(android.database.sqlite.SQLiteDatabase, java.lang.String, android.content.ContentValues, boolean):void
     arg types: [android.database.sqlite.SQLiteDatabase, java.lang.String, android.content.ContentValues, int]
     candidates:
      com.amap.openapi.by.a(android.database.sqlite.SQLiteDatabase, java.lang.String, java.util.HashSet<java.lang.Long>, android.content.ContentValues):void
      com.amap.openapi.by.a(android.database.sqlite.SQLiteDatabase, java.lang.String, long, android.content.ContentValues):boolean
      com.amap.openapi.by.a(com.amap.openapi.ck, java.util.List<java.lang.Long>, java.util.List<java.lang.String>, android.content.Context):void
      com.amap.openapi.by.a(android.database.sqlite.SQLiteDatabase, java.lang.String, android.content.ContentValues, boolean):void */
    public void a(bu buVar) {
        SQLiteDatabase sQLiteDatabase;
        SQLiteDatabase sQLiteDatabase2;
        if (buVar.b != null && buVar.b.size() > 0) {
            SQLiteDatabase sQLiteDatabase3 = null;
            try {
                sQLiteDatabase3 = this.d.getWritableDatabase();
                sQLiteDatabase = this.e.getWritableDatabase();
                sQLiteDatabase2 = sQLiteDatabase3;
            } catch (Throwable th) {
                sQLiteDatabase = null;
                sQLiteDatabase2 = sQLiteDatabase3;
            }
            if (sQLiteDatabase2 != null && sQLiteDatabase != null) {
                new StringBuilder();
                new StringBuilder();
                new StringBuilder();
                ContentValues contentValues = new ContentValues();
                this.f.readLock().lock();
                try {
                    sQLiteDatabase2.beginTransaction();
                    sQLiteDatabase.beginTransaction();
                    for (Map.Entry<Long, bt> entry : buVar.b.entrySet()) {
                        bt btVar = (bt) entry.getValue();
                        if (btVar != null) {
                            if (btVar.d) {
                                if (btVar.g > 60) {
                                    a(sQLiteDatabase2, "AP", btVar.a, contentValues);
                                }
                                if (btVar.h) {
                                    a(contentValues, btVar.a, null, btVar.b, 100000);
                                    a(sQLiteDatabase, "AP", contentValues, true);
                                }
                            } else {
                                a(contentValues, btVar.a, null, btVar.b, 0);
                                a(sQLiteDatabase, "AP", contentValues, true);
                                a(sQLiteDatabase, "AP", btVar.a, contentValues);
                            }
                        }
                    }
                    sQLiteDatabase2.setTransactionSuccessful();
                    sQLiteDatabase.setTransactionSuccessful();
                    try {
                        sQLiteDatabase2.endTransaction();
                        sQLiteDatabase.endTransaction();
                    } catch (Throwable th2) {
                    }
                    this.f.readLock().unlock();
                    return;
                } catch (Throwable th3) {
                }
            } else {
                return;
            }
        } else {
            return;
        }
        this.f.readLock().unlock();
    }

    /* JADX DEBUG: Failed to find minimal casts for resolve overloaded methods, cast all args instead
     method: com.amap.openapi.by.a(android.database.sqlite.SQLiteDatabase, java.lang.String, android.content.ContentValues, boolean):void
     arg types: [android.database.sqlite.SQLiteDatabase, java.lang.String, android.content.ContentValues, int]
     candidates:
      com.amap.openapi.by.a(android.database.sqlite.SQLiteDatabase, java.lang.String, java.util.HashSet<java.lang.Long>, android.content.ContentValues):void
      com.amap.openapi.by.a(android.database.sqlite.SQLiteDatabase, java.lang.String, long, android.content.ContentValues):boolean
      com.amap.openapi.by.a(com.amap.openapi.ck, java.util.List<java.lang.Long>, java.util.List<java.lang.String>, android.content.Context):void
      com.amap.openapi.by.a(android.database.sqlite.SQLiteDatabase, java.lang.String, android.content.ContentValues, boolean):void */
    public void a(bu buVar, AmapLoc amapLoc) {
        SQLiteDatabase sQLiteDatabase;
        SQLiteDatabase sQLiteDatabase2;
        SQLiteDatabase sQLiteDatabase3;
        try {
            sQLiteDatabase = this.d.getWritableDatabase();
            try {
                sQLiteDatabase2 = this.e.getWritableDatabase();
                sQLiteDatabase3 = sQLiteDatabase;
            } catch (Throwable th) {
                sQLiteDatabase2 = null;
                sQLiteDatabase3 = sQLiteDatabase;
                if (sQLiteDatabase3 != null) {
                    return;
                }
            }
        } catch (Throwable th2) {
            sQLiteDatabase = null;
        }
        if (sQLiteDatabase3 != null && sQLiteDatabase2 != null) {
            new StringBuilder();
            ContentValues contentValues = new ContentValues();
            try {
                sQLiteDatabase3.beginTransaction();
                sQLiteDatabase2.beginTransaction();
                for (Map.Entry<Long, bt> entry : buVar.b.entrySet()) {
                    bt btVar = (bt) entry.getValue();
                    if (btVar != null && btVar.d && btVar.g > 60 && Core.gd(btVar.e, btVar.f, amapLoc.getLat(), amapLoc.getLon()) > 100.0d) {
                        a("AP", contentValues, btVar.a);
                        a(contentValues, btVar.a, null, btVar.b, 100000);
                        a(sQLiteDatabase2, "AP", contentValues, false);
                    }
                }
                sQLiteDatabase3.setTransactionSuccessful();
                sQLiteDatabase2.setTransactionSuccessful();
                try {
                    sQLiteDatabase3.endTransaction();
                    sQLiteDatabase2.endTransaction();
                } catch (Throwable th3) {
                }
            } catch (Throwable th4) {
            }
        }
    }

    public void a(ck ckVar) {
        SQLiteDatabase sQLiteDatabase;
        try {
            sQLiteDatabase = this.d.getWritableDatabase();
        } catch (Throwable th) {
            sQLiteDatabase = null;
        }
        if (sQLiteDatabase != null) {
            ContentValues contentValues = new ContentValues();
            try {
                sQLiteDatabase.beginTransaction();
                for (int i = 0; i < ckVar.b(); i++) {
                    ci b2 = ckVar.b(i);
                    if (b2 != null) {
                        a(contentValues, b2);
                        a(sQLiteDatabase, "AP", contentValues);
                    }
                }
                for (int i2 = 0; i2 < ckVar.a(); i2++) {
                    ci a2 = ckVar.a(i2);
                    if (a2 != null) {
                        a(contentValues, a2);
                        a(sQLiteDatabase, "CL", contentValues);
                    }
                }
                sQLiteDatabase.setTransactionSuccessful();
                try {
                    sQLiteDatabase.endTransaction();
                } catch (Throwable th2) {
                }
            } catch (Throwable th3) {
            }
        }
    }

    public void a(ck ckVar, List<Long> list, List<String> list2, Context context) {
        SQLiteDatabase sQLiteDatabase;
        SQLiteDatabase sQLiteDatabase2 = null;
        SQLiteDatabase sQLiteDatabase3 = null;
        try {
            sQLiteDatabase2 = this.d.getWritableDatabase();
            sQLiteDatabase3 = this.e.getWritableDatabase();
            sQLiteDatabase = sQLiteDatabase2;
        } catch (Throwable th) {
            sQLiteDatabase = sQLiteDatabase2;
        }
        if (sQLiteDatabase != null && sQLiteDatabase3 != null) {
            ContentValues contentValues = new ContentValues();
            this.f.writeLock().lock();
            try {
                sQLiteDatabase.beginTransaction();
                sQLiteDatabase3.beginTransaction();
                HashSet<Long> a2 = a(list);
                HashSet hashSet = (HashSet) a2.clone();
                for (int i = 0; i < ckVar.b(); i++) {
                    ci b2 = ckVar.b(i);
                    if (b2 != null) {
                        a2.remove(Long.valueOf(b2.a()));
                        a(contentValues, b2);
                        a(sQLiteDatabase, "AP", contentValues);
                        if (b2.e() <= 60) {
                            hashSet.remove(Long.valueOf(b2.a()));
                            b(sQLiteDatabase3, "AP", b2.a(), contentValues);
                        }
                    }
                }
                a(sQLiteDatabase, "AP", a2, contentValues);
                HashSet<Long> b3 = b(list2);
                HashSet hashSet2 = (HashSet) b3.clone();
                for (int i2 = 0; i2 < ckVar.a(); i2++) {
                    ci a3 = ckVar.a(i2);
                    if (a3 != null) {
                        b3.remove(Long.valueOf(a3.a()));
                        a(contentValues, a3);
                        a(sQLiteDatabase, "CL", contentValues);
                        if (a3.e() <= 60) {
                            hashSet2.remove(Long.valueOf(a3.a()));
                            b(sQLiteDatabase3, "CL", a3.a(), contentValues);
                        }
                    }
                }
                a(sQLiteDatabase, "CL", b3, contentValues);
                a(sQLiteDatabase3, hashSet2, hashSet);
                a();
                sQLiteDatabase.setTransactionSuccessful();
                sQLiteDatabase3.setTransactionSuccessful();
                try {
                    sQLiteDatabase.endTransaction();
                    sQLiteDatabase3.endTransaction();
                } catch (Throwable th2) {
                }
                this.f.writeLock().unlock();
                return;
            } catch (Throwable th3) {
            }
        } else {
            return;
        }
        this.f.writeLock().unlock();
    }

    /* JADX WARNING: Removed duplicated region for block: B:27:0x00ba A[SYNTHETIC, Splitter:B:27:0x00ba] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void a(java.lang.String r17, com.amap.openapi.bu r18) {
        /*
            r16 = this;
            r10 = 0
            r0 = r16
            com.amap.openapi.bx r2 = r0.d     // Catch:{ Throwable -> 0x0166, all -> 0x015e }
            android.database.sqlite.SQLiteDatabase r2 = r2.getReadableDatabase()     // Catch:{ Throwable -> 0x0166, all -> 0x015e }
            java.lang.String r3 = "AP"
            java.lang.String[] r4 = com.amap.openapi.by.a     // Catch:{ Throwable -> 0x0166, all -> 0x015e }
            java.lang.StringBuilder r5 = new java.lang.StringBuilder     // Catch:{ Throwable -> 0x0166, all -> 0x015e }
            java.lang.String r6 = "id IN ("
            r5.<init>(r6)     // Catch:{ Throwable -> 0x0166, all -> 0x015e }
            r0 = r17
            java.lang.StringBuilder r5 = r5.append(r0)     // Catch:{ Throwable -> 0x0166, all -> 0x015e }
            java.lang.String r6 = ")"
            java.lang.StringBuilder r5 = r5.append(r6)     // Catch:{ Throwable -> 0x0166, all -> 0x015e }
            java.lang.String r5 = r5.toString()     // Catch:{ Throwable -> 0x0166, all -> 0x015e }
            r6 = 0
            r7 = 0
            r8 = 0
            r9 = 0
            android.database.Cursor r4 = r2.query(r3, r4, r5, r6, r7, r8, r9)     // Catch:{ Throwable -> 0x0166, all -> 0x015e }
            if (r4 == 0) goto L_0x014e
            boolean r3 = r4.moveToFirst()     // Catch:{ Throwable -> 0x006d, all -> 0x00b7 }
            if (r3 == 0) goto L_0x014e
        L_0x0037:
            boolean r3 = r4.isAfterLast()     // Catch:{ Throwable -> 0x006d, all -> 0x00b7 }
            if (r3 != 0) goto L_0x0133
            r3 = 0
            long r6 = r4.getLong(r3)     // Catch:{ Throwable -> 0x006d, all -> 0x00b7 }
            r3 = 5
            long r8 = r4.getLong(r3)     // Catch:{ Throwable -> 0x006d, all -> 0x00b7 }
            r10 = 7776000(0x76a700, double:3.8418545E-317)
            long r10 = r10 + r8
            long r12 = java.lang.System.currentTimeMillis()     // Catch:{ Throwable -> 0x006d, all -> 0x00b7 }
            r14 = 1000(0x3e8, double:4.94E-321)
            long r12 = r12 / r14
            int r3 = (r10 > r12 ? 1 : (r10 == r12 ? 0 : -1))
            if (r3 >= 0) goto L_0x008f
            java.lang.String r3 = "AP"
            java.lang.String r5 = "id=?"
            r8 = 1
            java.lang.String[] r8 = new java.lang.String[r8]     // Catch:{ Throwable -> 0x006d, all -> 0x00b7 }
            r9 = 0
            java.lang.String r6 = java.lang.String.valueOf(r6)     // Catch:{ Throwable -> 0x006d, all -> 0x00b7 }
            r8[r9] = r6     // Catch:{ Throwable -> 0x006d, all -> 0x00b7 }
            r2.delete(r3, r5, r8)     // Catch:{ Throwable -> 0x006d, all -> 0x00b7 }
            r4.moveToNext()     // Catch:{ Throwable -> 0x006d, all -> 0x00b7 }
            goto L_0x0037
        L_0x006d:
            r2 = move-exception
            r3 = r4
        L_0x006f:
            java.lang.String r4 = "@_18_4_@"
            java.lang.StringBuilder r5 = new java.lang.StringBuilder     // Catch:{ all -> 0x0162 }
            java.lang.String r6 = "@_18_4_2_@"
            r5.<init>(r6)     // Catch:{ all -> 0x0162 }
            java.lang.String r2 = android.util.Log.getStackTraceString(r2)     // Catch:{ all -> 0x0162 }
            java.lang.StringBuilder r2 = r5.append(r2)     // Catch:{ all -> 0x0162 }
            java.lang.String r2 = r2.toString()     // Catch:{ all -> 0x0162 }
            com.amap.location.common.log.ALLog.trace(r4, r2)     // Catch:{ all -> 0x0162 }
            if (r3 == 0) goto L_0x008e
            r3.close()     // Catch:{ Throwable -> 0x0158 }
        L_0x008e:
            return
        L_0x008f:
            r3 = 1
            int r5 = r4.getInt(r3)     // Catch:{ Throwable -> 0x006d, all -> 0x00b7 }
            r3 = 2
            int r10 = r4.getInt(r3)     // Catch:{ Throwable -> 0x006d, all -> 0x00b7 }
            r3 = 3
            int r11 = r4.getInt(r3)     // Catch:{ Throwable -> 0x006d, all -> 0x00b7 }
            r3 = 4
            int r12 = r4.getInt(r3)     // Catch:{ Throwable -> 0x006d, all -> 0x00b7 }
            r0 = r18
            java.util.HashMap<java.lang.Long, com.amap.openapi.bt> r3 = r0.b     // Catch:{ Throwable -> 0x006d, all -> 0x00b7 }
            java.lang.Long r6 = java.lang.Long.valueOf(r6)     // Catch:{ Throwable -> 0x006d, all -> 0x00b7 }
            java.lang.Object r3 = r3.get(r6)     // Catch:{ Throwable -> 0x006d, all -> 0x00b7 }
            com.amap.openapi.bt r3 = (com.amap.openapi.bt) r3     // Catch:{ Throwable -> 0x006d, all -> 0x00b7 }
            if (r3 != 0) goto L_0x00be
            r4.moveToNext()     // Catch:{ Throwable -> 0x006d, all -> 0x00b7 }
            goto L_0x0037
        L_0x00b7:
            r2 = move-exception
        L_0x00b8:
            if (r4 == 0) goto L_0x00bd
            r4.close()     // Catch:{ Throwable -> 0x015b }
        L_0x00bd:
            throw r2
        L_0x00be:
            r6 = 1
            r3.d = r6     // Catch:{ Throwable -> 0x006d, all -> 0x00b7 }
            r3.g = r12     // Catch:{ Throwable -> 0x006d, all -> 0x00b7 }
            r3.e = r5     // Catch:{ Throwable -> 0x006d, all -> 0x00b7 }
            r3.f = r10     // Catch:{ Throwable -> 0x006d, all -> 0x00b7 }
            r6 = 60
            if (r12 <= r6) goto L_0x011c
            if (r11 <= 0) goto L_0x011c
            r6 = 2000(0x7d0, float:2.803E-42)
            if (r11 >= r6) goto L_0x011c
            r0 = r18
            int r6 = r0.c     // Catch:{ Throwable -> 0x006d, all -> 0x00b7 }
            int r6 = r6 + 1
            r0 = r18
            r0.c = r6     // Catch:{ Throwable -> 0x006d, all -> 0x00b7 }
            r0 = r18
            java.lang.StringBuilder r6 = r0.d     // Catch:{ Throwable -> 0x006d, all -> 0x00b7 }
            long r12 = r3.b     // Catch:{ Throwable -> 0x006d, all -> 0x00b7 }
            java.lang.StringBuilder r6 = r6.append(r12)     // Catch:{ Throwable -> 0x006d, all -> 0x00b7 }
            java.lang.String r7 = ";"
            r6.append(r7)     // Catch:{ Throwable -> 0x006d, all -> 0x00b7 }
            r0 = r18
            java.lang.StringBuilder r6 = r0.e     // Catch:{ Throwable -> 0x006d, all -> 0x00b7 }
            java.lang.StringBuilder r6 = r6.append(r10)     // Catch:{ Throwable -> 0x006d, all -> 0x00b7 }
            java.lang.String r7 = ","
            java.lang.StringBuilder r6 = r6.append(r7)     // Catch:{ Throwable -> 0x006d, all -> 0x00b7 }
            java.lang.StringBuilder r5 = r6.append(r5)     // Catch:{ Throwable -> 0x006d, all -> 0x00b7 }
            java.lang.String r6 = ","
            java.lang.StringBuilder r5 = r5.append(r6)     // Catch:{ Throwable -> 0x006d, all -> 0x00b7 }
            java.lang.StringBuilder r5 = r5.append(r11)     // Catch:{ Throwable -> 0x006d, all -> 0x00b7 }
            java.lang.String r6 = ","
            java.lang.StringBuilder r5 = r5.append(r6)     // Catch:{ Throwable -> 0x006d, all -> 0x00b7 }
            int r6 = r3.c     // Catch:{ Throwable -> 0x006d, all -> 0x00b7 }
            java.lang.StringBuilder r5 = r5.append(r6)     // Catch:{ Throwable -> 0x006d, all -> 0x00b7 }
            java.lang.String r6 = ";"
            r5.append(r6)     // Catch:{ Throwable -> 0x006d, all -> 0x00b7 }
        L_0x011c:
            r6 = 604800(0x93a80, double:2.98811E-318)
            long r6 = r6 + r8
            long r8 = java.lang.System.currentTimeMillis()     // Catch:{ Throwable -> 0x006d, all -> 0x00b7 }
            r10 = 1000(0x3e8, double:4.94E-321)
            long r8 = r8 / r10
            int r5 = (r6 > r8 ? 1 : (r6 == r8 ? 0 : -1))
            if (r5 >= 0) goto L_0x012e
            r5 = 1
            r3.h = r5     // Catch:{ Throwable -> 0x006d, all -> 0x00b7 }
        L_0x012e:
            r4.moveToNext()     // Catch:{ Throwable -> 0x006d, all -> 0x00b7 }
            goto L_0x0037
        L_0x0133:
            r0 = r18
            java.lang.StringBuilder r2 = r0.e     // Catch:{ Throwable -> 0x006d, all -> 0x00b7 }
            int r2 = r2.length()     // Catch:{ Throwable -> 0x006d, all -> 0x00b7 }
            if (r2 <= 0) goto L_0x014e
            r0 = r18
            java.lang.StringBuilder r2 = r0.e     // Catch:{ Throwable -> 0x006d, all -> 0x00b7 }
            r0 = r18
            java.lang.StringBuilder r3 = r0.e     // Catch:{ Throwable -> 0x006d, all -> 0x00b7 }
            int r3 = r3.length()     // Catch:{ Throwable -> 0x006d, all -> 0x00b7 }
            int r3 = r3 + -1
            r2.deleteCharAt(r3)     // Catch:{ Throwable -> 0x006d, all -> 0x00b7 }
        L_0x014e:
            if (r4 == 0) goto L_0x008e
            r4.close()     // Catch:{ Throwable -> 0x0155 }
            goto L_0x008e
        L_0x0155:
            r2 = move-exception
            goto L_0x008e
        L_0x0158:
            r2 = move-exception
            goto L_0x008e
        L_0x015b:
            r3 = move-exception
            goto L_0x00bd
        L_0x015e:
            r2 = move-exception
            r4 = r10
            goto L_0x00b8
        L_0x0162:
            r2 = move-exception
            r4 = r3
            goto L_0x00b8
        L_0x0166:
            r2 = move-exception
            r3 = r10
            goto L_0x006f
        */
        throw new UnsupportedOperationException("Method not decompiled: com.amap.openapi.by.a(java.lang.String, com.amap.openapi.bu):void");
    }

    /* JADX WARNING: Removed duplicated region for block: B:24:0x008f A[SYNTHETIC, Splitter:B:24:0x008f] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public java.util.List<java.lang.Long> b(int r12, int r13) {
        /*
            r11 = this;
            r9 = 0
            java.util.ArrayList r10 = new java.util.ArrayList
            r10.<init>()
            com.amap.openapi.cb r0 = r11.e     // Catch:{ Throwable -> 0x009a, all -> 0x008c }
            android.database.sqlite.SQLiteDatabase r0 = r0.getReadableDatabase()     // Catch:{ Throwable -> 0x009a, all -> 0x008c }
            java.lang.String r1 = "AP"
            java.lang.String[] r2 = com.amap.openapi.by.b     // Catch:{ Throwable -> 0x009a, all -> 0x008c }
            java.lang.StringBuilder r3 = new java.lang.StringBuilder     // Catch:{ Throwable -> 0x009a, all -> 0x008c }
            java.lang.String r4 = "frequency>="
            r3.<init>(r4)     // Catch:{ Throwable -> 0x009a, all -> 0x008c }
            java.lang.StringBuilder r3 = r3.append(r12)     // Catch:{ Throwable -> 0x009a, all -> 0x008c }
            java.lang.String r4 = " AND time<"
            java.lang.StringBuilder r3 = r3.append(r4)     // Catch:{ Throwable -> 0x009a, all -> 0x008c }
            long r4 = java.lang.System.currentTimeMillis()     // Catch:{ Throwable -> 0x009a, all -> 0x008c }
            r6 = 1000(0x3e8, double:4.94E-321)
            long r4 = r4 / r6
            r6 = 86400(0x15180, double:4.26873E-319)
            long r4 = r4 - r6
            java.lang.StringBuilder r3 = r3.append(r4)     // Catch:{ Throwable -> 0x009a, all -> 0x008c }
            java.lang.String r3 = r3.toString()     // Catch:{ Throwable -> 0x009a, all -> 0x008c }
            r4 = 0
            r5 = 0
            r6 = 0
            java.lang.String r7 = "frequency DESC"
            java.lang.String r8 = java.lang.String.valueOf(r13)     // Catch:{ Throwable -> 0x009a, all -> 0x008c }
            android.database.Cursor r1 = r0.query(r1, r2, r3, r4, r5, r6, r7, r8)     // Catch:{ Throwable -> 0x009a, all -> 0x008c }
            if (r1 == 0) goto L_0x0084
            boolean r0 = r1.moveToFirst()     // Catch:{ Throwable -> 0x0063 }
            if (r0 == 0) goto L_0x0084
        L_0x004d:
            boolean r0 = r1.isAfterLast()     // Catch:{ Throwable -> 0x0063 }
            if (r0 != 0) goto L_0x0084
            r0 = 1
            long r2 = r1.getLong(r0)     // Catch:{ Throwable -> 0x0063 }
            java.lang.Long r0 = java.lang.Long.valueOf(r2)     // Catch:{ Throwable -> 0x0063 }
            r10.add(r0)     // Catch:{ Throwable -> 0x0063 }
            r1.moveToNext()     // Catch:{ Throwable -> 0x0063 }
            goto L_0x004d
        L_0x0063:
            r0 = move-exception
        L_0x0064:
            java.lang.String r2 = "@_18_4_@"
            java.lang.StringBuilder r3 = new java.lang.StringBuilder     // Catch:{ all -> 0x0097 }
            java.lang.String r4 = "@_18_4_4_@"
            r3.<init>(r4)     // Catch:{ all -> 0x0097 }
            java.lang.String r0 = android.util.Log.getStackTraceString(r0)     // Catch:{ all -> 0x0097 }
            java.lang.StringBuilder r0 = r3.append(r0)     // Catch:{ all -> 0x0097 }
            java.lang.String r0 = r0.toString()     // Catch:{ all -> 0x0097 }
            com.amap.location.common.log.ALLog.trace(r2, r0)     // Catch:{ all -> 0x0097 }
            if (r1 == 0) goto L_0x0083
            r1.close()     // Catch:{ Throwable -> 0x0093 }
        L_0x0083:
            return r10
        L_0x0084:
            if (r1 == 0) goto L_0x0083
            r1.close()     // Catch:{ Throwable -> 0x008a }
            goto L_0x0083
        L_0x008a:
            r0 = move-exception
            goto L_0x0083
        L_0x008c:
            r0 = move-exception
        L_0x008d:
            if (r9 == 0) goto L_0x0092
            r9.close()     // Catch:{ Throwable -> 0x0095 }
        L_0x0092:
            throw r0
        L_0x0093:
            r0 = move-exception
            goto L_0x0083
        L_0x0095:
            r1 = move-exception
            goto L_0x0092
        L_0x0097:
            r0 = move-exception
            r9 = r1
            goto L_0x008d
        L_0x009a:
            r0 = move-exception
            r1 = r9
            goto L_0x0064
        */
        throw new UnsupportedOperationException("Method not decompiled: com.amap.openapi.by.b(int, int):java.util.List");
    }

    /* JADX WARNING: Removed duplicated region for block: B:50:0x00dc A[SYNTHETIC, Splitter:B:50:0x00dc] */
    /* JADX WARNING: Removed duplicated region for block: B:53:0x00e1 A[Catch:{ Throwable -> 0x00e8 }] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void b() {
        /*
            r10 = this;
            r8 = 0
            com.amap.openapi.cb r0 = r10.e     // Catch:{ Throwable -> 0x000a }
            android.database.sqlite.SQLiteDatabase r0 = r0.getWritableDatabase()     // Catch:{ Throwable -> 0x000a }
        L_0x0007:
            if (r0 != 0) goto L_0x000d
        L_0x0009:
            return
        L_0x000a:
            r0 = move-exception
            r0 = r8
            goto L_0x0007
        L_0x000d:
            r0.beginTransaction()     // Catch:{ Throwable -> 0x00f2, all -> 0x00d7 }
            java.lang.String r1 = "AP"
            r2 = 1
            java.lang.String[] r2 = new java.lang.String[r2]     // Catch:{ Throwable -> 0x00f2, all -> 0x00d7 }
            r3 = 0
            java.lang.String r4 = "id"
            r2[r3] = r4     // Catch:{ Throwable -> 0x00f2, all -> 0x00d7 }
            r3 = 0
            r4 = 0
            r5 = 0
            r6 = 0
            java.lang.String r7 = "frequency DESC LIMIT 10000,-1"
            android.database.Cursor r9 = r0.query(r1, r2, r3, r4, r5, r6, r7)     // Catch:{ Throwable -> 0x00f2, all -> 0x00d7 }
            if (r9 == 0) goto L_0x007e
            boolean r1 = r9.moveToFirst()     // Catch:{ Throwable -> 0x0051, all -> 0x00ea }
            if (r1 == 0) goto L_0x007e
        L_0x002f:
            boolean r1 = r9.isAfterLast()     // Catch:{ Throwable -> 0x0051, all -> 0x00ea }
            if (r1 != 0) goto L_0x007e
            r1 = 0
            long r2 = r9.getLong(r1)     // Catch:{ Throwable -> 0x0051, all -> 0x00ea }
            java.lang.String r1 = "AP"
            java.lang.String r4 = "id=?"
            r5 = 1
            java.lang.String[] r5 = new java.lang.String[r5]     // Catch:{ Throwable -> 0x0051, all -> 0x00ea }
            r6 = 0
            java.lang.String r2 = java.lang.String.valueOf(r2)     // Catch:{ Throwable -> 0x0051, all -> 0x00ea }
            r5[r6] = r2     // Catch:{ Throwable -> 0x0051, all -> 0x00ea }
            r0.delete(r1, r4, r5)     // Catch:{ Throwable -> 0x0051, all -> 0x00ea }
            r9.moveToNext()     // Catch:{ Throwable -> 0x0051, all -> 0x00ea }
            goto L_0x002f
        L_0x0051:
            r1 = move-exception
            r2 = r8
            r3 = r9
        L_0x0054:
            java.lang.String r4 = "@_18_4_@"
            java.lang.StringBuilder r5 = new java.lang.StringBuilder     // Catch:{ all -> 0x00ef }
            java.lang.String r6 = "@_18_4_12_@"
            r5.<init>(r6)     // Catch:{ all -> 0x00ef }
            java.lang.String r1 = android.util.Log.getStackTraceString(r1)     // Catch:{ all -> 0x00ef }
            java.lang.StringBuilder r1 = r5.append(r1)     // Catch:{ all -> 0x00ef }
            java.lang.String r1 = r1.toString()     // Catch:{ all -> 0x00ef }
            com.amap.location.common.log.ALLog.trace(r4, r1)     // Catch:{ all -> 0x00ef }
            if (r3 == 0) goto L_0x0073
            r3.close()     // Catch:{ Throwable -> 0x007c }
        L_0x0073:
            if (r2 == 0) goto L_0x0078
            r2.close()     // Catch:{ Throwable -> 0x007c }
        L_0x0078:
            r0.endTransaction()     // Catch:{ Throwable -> 0x007c }
            goto L_0x0009
        L_0x007c:
            r0 = move-exception
            goto L_0x0009
        L_0x007e:
            java.lang.String r1 = "CL"
            r2 = 1
            java.lang.String[] r2 = new java.lang.String[r2]     // Catch:{ Throwable -> 0x0051, all -> 0x00ea }
            r3 = 0
            java.lang.String r4 = "id"
            r2[r3] = r4     // Catch:{ Throwable -> 0x0051, all -> 0x00ea }
            r3 = 0
            r4 = 0
            r5 = 0
            r6 = 0
            java.lang.String r7 = "frequency DESC LIMIT 10000,-1"
            android.database.Cursor r2 = r0.query(r1, r2, r3, r4, r5, r6, r7)     // Catch:{ Throwable -> 0x0051, all -> 0x00ea }
            if (r2 == 0) goto L_0x00c2
            boolean r1 = r2.moveToFirst()     // Catch:{ Throwable -> 0x00bf, all -> 0x00ed }
            if (r1 == 0) goto L_0x00c2
        L_0x009d:
            boolean r1 = r2.isAfterLast()     // Catch:{ Throwable -> 0x00bf, all -> 0x00ed }
            if (r1 != 0) goto L_0x00c2
            r1 = 0
            long r4 = r2.getLong(r1)     // Catch:{ Throwable -> 0x00bf, all -> 0x00ed }
            java.lang.String r1 = "CL"
            java.lang.String r3 = "id=?"
            r6 = 1
            java.lang.String[] r6 = new java.lang.String[r6]     // Catch:{ Throwable -> 0x00bf, all -> 0x00ed }
            r7 = 0
            java.lang.String r4 = java.lang.String.valueOf(r4)     // Catch:{ Throwable -> 0x00bf, all -> 0x00ed }
            r6[r7] = r4     // Catch:{ Throwable -> 0x00bf, all -> 0x00ed }
            r0.delete(r1, r3, r6)     // Catch:{ Throwable -> 0x00bf, all -> 0x00ed }
            r2.moveToNext()     // Catch:{ Throwable -> 0x00bf, all -> 0x00ed }
            goto L_0x009d
        L_0x00bf:
            r1 = move-exception
            r3 = r9
            goto L_0x0054
        L_0x00c2:
            r0.setTransactionSuccessful()     // Catch:{ Throwable -> 0x00bf, all -> 0x00ed }
            if (r9 == 0) goto L_0x00ca
            r9.close()     // Catch:{ Throwable -> 0x00d4 }
        L_0x00ca:
            if (r2 == 0) goto L_0x00cf
            r2.close()     // Catch:{ Throwable -> 0x00d4 }
        L_0x00cf:
            r0.endTransaction()     // Catch:{ Throwable -> 0x00d4 }
            goto L_0x0009
        L_0x00d4:
            r0 = move-exception
            goto L_0x0009
        L_0x00d7:
            r1 = move-exception
            r2 = r8
            r9 = r8
        L_0x00da:
            if (r9 == 0) goto L_0x00df
            r9.close()     // Catch:{ Throwable -> 0x00e8 }
        L_0x00df:
            if (r2 == 0) goto L_0x00e4
            r2.close()     // Catch:{ Throwable -> 0x00e8 }
        L_0x00e4:
            r0.endTransaction()     // Catch:{ Throwable -> 0x00e8 }
        L_0x00e7:
            throw r1
        L_0x00e8:
            r0 = move-exception
            goto L_0x00e7
        L_0x00ea:
            r1 = move-exception
            r2 = r8
            goto L_0x00da
        L_0x00ed:
            r1 = move-exception
            goto L_0x00da
        L_0x00ef:
            r1 = move-exception
            r9 = r3
            goto L_0x00da
        L_0x00f2:
            r1 = move-exception
            r2 = r8
            r3 = r8
            goto L_0x0054
        */
        throw new UnsupportedOperationException("Method not decompiled: com.amap.openapi.by.b():void");
    }

    /* JADX DEBUG: Failed to find minimal casts for resolve overloaded methods, cast all args instead
     method: com.amap.openapi.by.a(android.database.sqlite.SQLiteDatabase, java.lang.String, android.content.ContentValues, boolean):void
     arg types: [android.database.sqlite.SQLiteDatabase, java.lang.String, android.content.ContentValues, int]
     candidates:
      com.amap.openapi.by.a(android.database.sqlite.SQLiteDatabase, java.lang.String, java.util.HashSet<java.lang.Long>, android.content.ContentValues):void
      com.amap.openapi.by.a(android.database.sqlite.SQLiteDatabase, java.lang.String, long, android.content.ContentValues):boolean
      com.amap.openapi.by.a(com.amap.openapi.ck, java.util.List<java.lang.Long>, java.util.List<java.lang.String>, android.content.Context):void
      com.amap.openapi.by.a(android.database.sqlite.SQLiteDatabase, java.lang.String, android.content.ContentValues, boolean):void */
    public void b(bs bsVar) {
        long j = bsVar.g;
        String str = bsVar.f;
        ContentValues contentValues = new ContentValues();
        try {
            a("CL", contentValues, j);
            a(contentValues, j, str, 0, 100000);
            a(this.e.getWritableDatabase(), "CL", contentValues, false);
        } catch (Throwable th) {
            ALLog.trace("@_18_4_@", "@_18_4_7_@" + Log.getStackTraceString(th));
        }
    }

    public void c() {
        try {
            SQLiteDatabase writableDatabase = this.d.getWritableDatabase();
            SQLiteDatabase writableDatabase2 = this.e.getWritableDatabase();
            writableDatabase.delete("CL", null, null);
            writableDatabase.delete("AP", null, null);
            writableDatabase2.delete("CL", null, null);
            writableDatabase2.delete("AP", null, null);
        } catch (Throwable th) {
        }
    }
}
