package com.amap.openapi;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.SystemClock;
import android.util.Log;
import com.amap.location.common.log.ALLog;
import com.amap.location.common.util.e;
import dji.publics.protocol.ResponseBase;
import java.util.Locale;

/* compiled from: DbManager */
public class u {
    private static final String[] a = {ResponseBase.STRING_ID, "type", "data", "size"};
    private a b;
    private long c = a(true);
    private long d = a(false);

    /* compiled from: DbManager */
    private static class a extends SQLiteOpenHelper {
        a(Context context, String str, int i) {
            super(context, str, (SQLiteDatabase.CursorFactory) null, i);
        }

        public void onCreate(SQLiteDatabase sQLiteDatabase) {
            try {
                sQLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS base (id INTEGER PRIMARY KEY AUTOINCREMENT , type SMALLINT, data BLOB, size INTEGER, time INTEGER);");
            } catch (Exception e) {
            }
        }

        /* JADX DEBUG: Failed to find minimal casts for resolve overloaded methods, cast all args instead
         method: com.amap.location.common.log.ALLog.e(java.lang.String, java.lang.String, java.lang.Throwable, boolean):void
         arg types: [java.lang.String, java.lang.String, java.lang.Exception, int]
         candidates:
          com.amap.location.common.log.ALLog.e(java.lang.String, java.lang.String, boolean, boolean):void
          com.amap.location.common.log.ALLog.e(java.lang.String, java.lang.String, java.lang.Throwable, boolean):void */
        public void onDowngrade(SQLiteDatabase sQLiteDatabase, int i, int i2) {
            try {
                sQLiteDatabase.execSQL("DROP TABLE IF EXISTS base");
                onCreate(sQLiteDatabase);
            } catch (Exception e) {
                ALLog.e("DbManager", "", (Throwable) e, true);
            }
        }

        /* JADX DEBUG: Failed to find minimal casts for resolve overloaded methods, cast all args instead
         method: com.amap.location.common.log.ALLog.e(java.lang.String, java.lang.String, java.lang.Throwable, boolean):void
         arg types: [java.lang.String, java.lang.String, java.lang.Exception, int]
         candidates:
          com.amap.location.common.log.ALLog.e(java.lang.String, java.lang.String, boolean, boolean):void
          com.amap.location.common.log.ALLog.e(java.lang.String, java.lang.String, java.lang.Throwable, boolean):void */
        public void onUpgrade(SQLiteDatabase sQLiteDatabase, int i, int i2) {
            try {
                sQLiteDatabase.execSQL("DROP TABLE IF EXISTS base");
                sQLiteDatabase.execSQL("DROP TABLE IF EXISTS byte_base");
                sQLiteDatabase.execSQL("DROP TABLE IF EXISTS extend");
                onCreate(sQLiteDatabase);
            } catch (Exception e) {
                ALLog.e("DbManager", "", (Throwable) e, true);
            }
        }
    }

    public u(Context context) {
        this.b = new a(context, "aloccoll.db", 4);
    }

    private long a(boolean z) {
        Cursor cursor;
        Cursor cursor2 = null;
        SystemClock.elapsedRealtime();
        try {
            Cursor query = this.b.getReadableDatabase().query("base", new String[]{"SUM(size)"}, z ? "type=?" : "type!=?", new String[]{"0"}, null, null, null);
            try {
                long j = query.moveToFirst() ? query.getLong(0) : 0;
                e.a(query);
                return j;
            } catch (Exception e) {
                cursor = query;
                e.a(cursor);
                return 0;
            } catch (Throwable th) {
                th = th;
                cursor2 = query;
                e.a(cursor2);
                throw th;
            }
        } catch (Exception e2) {
            cursor = null;
            e.a(cursor);
            return 0;
        } catch (Throwable th2) {
            th = th2;
            e.a(cursor2);
            throw th;
        }
    }

    private long a(boolean z, long j) throws Exception {
        Cursor cursor;
        long j2 = 0;
        String str = z ? "type=0" : "type!=0";
        long j3 = -2147483648L;
        while (true) {
            if (j2 >= j) {
                break;
            }
            try {
                Cursor query = this.b.getReadableDatabase().query("base", new String[]{ResponseBase.STRING_ID, "type", "size"}, "id>? AND " + str, new String[]{String.valueOf(j3)}, null, null, "id ASC", "100");
                try {
                    boolean moveToNext = query.moveToNext();
                    if (!moveToNext) {
                        e.a(query);
                        break;
                    }
                    long j4 = j3;
                    long j5 = j2;
                    while (moveToNext) {
                        j4 = query.getLong(0);
                        j5 += (long) query.getInt(2);
                        if (j5 >= j) {
                            break;
                        }
                        moveToNext = query.moveToNext();
                    }
                    e.a(query);
                    j3 = j4;
                    j2 = j5;
                } catch (Throwable th) {
                    th = th;
                    cursor = query;
                }
            } catch (Throwable th2) {
                th = th2;
                cursor = null;
            }
        }
        if (j2 > 0) {
            if (this.b.getWritableDatabase().delete("base", "id<=? AND " + str, new String[]{String.valueOf(j3)}) > 0) {
                if (z) {
                    this.c -= j2;
                    if (this.c < 0) {
                        this.c = 0;
                    }
                } else {
                    this.d -= j2;
                    if (this.d < 0) {
                        this.d = 0;
                    }
                }
            }
        }
        return j2;
        e.a(cursor);
        throw th;
    }

    public au a(boolean z, int i, long j) {
        Cursor cursor;
        au auVar = new au();
        try {
            cursor = this.b.getReadableDatabase().query("base", a, z ? "type=0" : "type!=0", null, null, null, "id ASC", String.valueOf(i));
            int i2 = 0;
            while (cursor.moveToNext()) {
                try {
                    int i3 = cursor.getInt(3);
                    if (((long) auVar.c) >= j || ((long) (auVar.c + i3)) > j || i2 >= i) {
                        break;
                    }
                    auVar.a = cursor.getLong(0);
                    auVar.b.add(new s(cursor.getInt(1), cursor.getBlob(2)));
                    auVar.c = i3 + auVar.c;
                    i2++;
                } catch (Exception e) {
                } catch (Throwable th) {
                    th = th;
                    e.a(cursor);
                    throw th;
                }
            }
            if (z) {
                ALLog.trace("@_3_2_@", String.format(Locale.getDefault(), "@_3_2_1_@%d，%d, %d", Long.valueOf(auVar.a), Integer.valueOf(i2), Integer.valueOf(auVar.c)));
            } else {
                ALLog.trace("@_3_2_@", String.format(Locale.getDefault(), "@_3_2_2_@%d，%d, %d", Long.valueOf(auVar.a), Integer.valueOf(i2), Integer.valueOf(auVar.c)));
            }
            au auVar2 = auVar.c == 0 ? null : auVar;
            e.a(cursor);
            return auVar2;
        } catch (Exception e2) {
            cursor = null;
            e.a(cursor);
            return null;
        } catch (Throwable th2) {
            th = th2;
            cursor = null;
            e.a(cursor);
            throw th;
        }
    }

    public void a() {
        try {
            if (this.b != null) {
                this.b.close();
                this.b = null;
            }
        } catch (Exception e) {
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:32:0x00da A[SYNTHETIC, Splitter:B:32:0x00da] */
    /* JADX WARNING: Removed duplicated region for block: B:35:0x00df A[SYNTHETIC, Splitter:B:35:0x00df] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void a(java.util.List<com.amap.openapi.s> r15) {
        /*
            r14 = this;
            r3 = 0
            r6 = 0
            r4 = 0
            r1 = 0
            r0 = 0
            com.amap.openapi.u$a r2 = r14.b     // Catch:{ Exception -> 0x00c5, all -> 0x00d4 }
            android.database.sqlite.SQLiteDatabase r2 = r2.getWritableDatabase()     // Catch:{ Exception -> 0x00c5, all -> 0x00d4 }
            r2.beginTransaction()     // Catch:{ Exception -> 0x00f7, all -> 0x00ed }
            java.lang.String r1 = "INSERT INTO base(type,data,size,time) VALUES(?,?,?,?)"
            android.database.sqlite.SQLiteStatement r1 = r2.compileStatement(r1)     // Catch:{ Exception -> 0x00f7, all -> 0x00ed }
            long r8 = java.lang.System.currentTimeMillis()     // Catch:{ Exception -> 0x00f9, all -> 0x00f2 }
            java.util.Iterator r10 = r15.iterator()     // Catch:{ Exception -> 0x00f9, all -> 0x00f2 }
        L_0x001f:
            boolean r0 = r10.hasNext()     // Catch:{ Exception -> 0x00f9, all -> 0x00f2 }
            if (r0 == 0) goto L_0x0058
            java.lang.Object r0 = r10.next()     // Catch:{ Exception -> 0x00f9, all -> 0x00f2 }
            com.amap.openapi.s r0 = (com.amap.openapi.s) r0     // Catch:{ Exception -> 0x00f9, all -> 0x00f2 }
            r11 = 1
            int r12 = r0.b()     // Catch:{ Exception -> 0x00f9, all -> 0x00f2 }
            long r12 = (long) r12     // Catch:{ Exception -> 0x00f9, all -> 0x00f2 }
            r1.bindLong(r11, r12)     // Catch:{ Exception -> 0x00f9, all -> 0x00f2 }
            r11 = 2
            byte[] r12 = r0.c()     // Catch:{ Exception -> 0x00f9, all -> 0x00f2 }
            r1.bindBlob(r11, r12)     // Catch:{ Exception -> 0x00f9, all -> 0x00f2 }
            long r12 = r0.a()     // Catch:{ Exception -> 0x00f9, all -> 0x00f2 }
            r11 = 3
            r1.bindLong(r11, r12)     // Catch:{ Exception -> 0x00f9, all -> 0x00f2 }
            r11 = 4
            r1.bindLong(r11, r8)     // Catch:{ Exception -> 0x00f9, all -> 0x00f2 }
            r1.executeInsert()     // Catch:{ Exception -> 0x00f9, all -> 0x00f2 }
            int r0 = r0.b()     // Catch:{ Exception -> 0x00f9, all -> 0x00f2 }
            if (r0 != 0) goto L_0x0056
            long r6 = r6 + r12
            int r0 = r3 + 1
            r3 = r0
            goto L_0x001f
        L_0x0056:
            long r4 = r4 + r12
            goto L_0x001f
        L_0x0058:
            r2.setTransactionSuccessful()     // Catch:{ Exception -> 0x00f9, all -> 0x00f2 }
            long r10 = r14.c     // Catch:{ Exception -> 0x00f9, all -> 0x00f2 }
            long r10 = r10 + r6
            r14.c = r10     // Catch:{ Exception -> 0x00f9, all -> 0x00f2 }
            long r10 = r14.d     // Catch:{ Exception -> 0x00f9, all -> 0x00f2 }
            long r10 = r10 + r4
            r14.d = r10     // Catch:{ Exception -> 0x00f9, all -> 0x00f2 }
            java.lang.String r0 = "@_3_2_@"
            java.util.Locale r10 = java.util.Locale.getDefault()     // Catch:{ Exception -> 0x00f9, all -> 0x00f2 }
            java.lang.StringBuilder r11 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x00f9, all -> 0x00f2 }
            java.lang.String r12 = "@_3_2_3_@"
            r11.<init>(r12)     // Catch:{ Exception -> 0x00f9, all -> 0x00f2 }
            java.lang.StringBuilder r8 = r11.append(r8)     // Catch:{ Exception -> 0x00f9, all -> 0x00f2 }
            java.lang.String r9 = ";@_3_2_4_@%d，%d;@_3_2_5_@%d，%d"
            java.lang.StringBuilder r8 = r8.append(r9)     // Catch:{ Exception -> 0x00f9, all -> 0x00f2 }
            java.lang.String r8 = r8.toString()     // Catch:{ Exception -> 0x00f9, all -> 0x00f2 }
            r9 = 5
            java.lang.Object[] r9 = new java.lang.Object[r9]     // Catch:{ Exception -> 0x00f9, all -> 0x00f2 }
            r11 = 0
            int r12 = r15.size()     // Catch:{ Exception -> 0x00f9, all -> 0x00f2 }
            java.lang.Integer r12 = java.lang.Integer.valueOf(r12)     // Catch:{ Exception -> 0x00f9, all -> 0x00f2 }
            r9[r11] = r12     // Catch:{ Exception -> 0x00f9, all -> 0x00f2 }
            r11 = 1
            java.lang.Long r6 = java.lang.Long.valueOf(r6)     // Catch:{ Exception -> 0x00f9, all -> 0x00f2 }
            r9[r11] = r6     // Catch:{ Exception -> 0x00f9, all -> 0x00f2 }
            r6 = 2
            java.lang.Integer r7 = java.lang.Integer.valueOf(r3)     // Catch:{ Exception -> 0x00f9, all -> 0x00f2 }
            r9[r6] = r7     // Catch:{ Exception -> 0x00f9, all -> 0x00f2 }
            r6 = 3
            java.lang.Long r4 = java.lang.Long.valueOf(r4)     // Catch:{ Exception -> 0x00f9, all -> 0x00f2 }
            r9[r6] = r4     // Catch:{ Exception -> 0x00f9, all -> 0x00f2 }
            r4 = 4
            int r5 = r15.size()     // Catch:{ Exception -> 0x00f9, all -> 0x00f2 }
            int r3 = r5 - r3
            java.lang.Integer r3 = java.lang.Integer.valueOf(r3)     // Catch:{ Exception -> 0x00f9, all -> 0x00f2 }
            r9[r4] = r3     // Catch:{ Exception -> 0x00f9, all -> 0x00f2 }
            java.lang.String r3 = java.lang.String.format(r10, r8, r9)     // Catch:{ Exception -> 0x00f9, all -> 0x00f2 }
            com.amap.location.common.log.ALLog.trace(r0, r3)     // Catch:{ Exception -> 0x00f9, all -> 0x00f2 }
            if (r1 == 0) goto L_0x00bf
            r1.close()     // Catch:{ Throwable -> 0x00e3 }
        L_0x00bf:
            if (r2 == 0) goto L_0x00c4
            r2.endTransaction()     // Catch:{ Exception -> 0x00e5 }
        L_0x00c4:
            return
        L_0x00c5:
            r2 = move-exception
            r2 = r1
        L_0x00c7:
            if (r0 == 0) goto L_0x00cc
            r0.close()     // Catch:{ Throwable -> 0x00e7 }
        L_0x00cc:
            if (r2 == 0) goto L_0x00c4
            r2.endTransaction()     // Catch:{ Exception -> 0x00d2 }
            goto L_0x00c4
        L_0x00d2:
            r0 = move-exception
            goto L_0x00c4
        L_0x00d4:
            r2 = move-exception
            r3 = r2
            r4 = r0
            r5 = r1
        L_0x00d8:
            if (r4 == 0) goto L_0x00dd
            r4.close()     // Catch:{ Throwable -> 0x00e9 }
        L_0x00dd:
            if (r5 == 0) goto L_0x00e2
            r5.endTransaction()     // Catch:{ Exception -> 0x00eb }
        L_0x00e2:
            throw r3
        L_0x00e3:
            r0 = move-exception
            goto L_0x00bf
        L_0x00e5:
            r0 = move-exception
            goto L_0x00c4
        L_0x00e7:
            r0 = move-exception
            goto L_0x00cc
        L_0x00e9:
            r0 = move-exception
            goto L_0x00dd
        L_0x00eb:
            r0 = move-exception
            goto L_0x00e2
        L_0x00ed:
            r1 = move-exception
            r3 = r1
            r4 = r0
            r5 = r2
            goto L_0x00d8
        L_0x00f2:
            r0 = move-exception
            r3 = r0
            r4 = r1
            r5 = r2
            goto L_0x00d8
        L_0x00f7:
            r1 = move-exception
            goto L_0x00c7
        L_0x00f9:
            r0 = move-exception
            r0 = r1
            goto L_0x00c7
        */
        throw new UnsupportedOperationException("Method not decompiled: com.amap.openapi.u.a(java.util.List):void");
    }

    /* JADX DEBUG: Failed to find minimal casts for resolve overloaded methods, cast all args instead
     method: ClspMth{java.lang.Math.max(long, long):long}
     arg types: [int, long]
     candidates:
      ClspMth{java.lang.Math.max(double, double):double}
      ClspMth{java.lang.Math.max(int, int):int}
      ClspMth{java.lang.Math.max(float, float):float}
      ClspMth{java.lang.Math.max(long, long):long} */
    public boolean a(long j) {
        if (j < 4611686018427387903L && this.c + this.d + j < 10485760) {
            return true;
        }
        long max = Math.max(204800L, j);
        try {
            long a2 = a(false, max);
            if (a2 >= max) {
                return true;
            }
            a(true, max - a2);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean a(au auVar) {
        Exception e;
        boolean z;
        if (auVar == null || auVar.b.size() == 0) {
            return true;
        }
        try {
            boolean z2 = auVar.b.get(0).b() == 0;
            int delete = this.b.getWritableDatabase().delete("base", z2 ? "type=0 AND id<=?" : "type!=0 AND id<=?", new String[]{String.valueOf(auVar.a)});
            if (delete > 0) {
                if (z2) {
                    this.c -= (long) auVar.c;
                    if (this.c < 0) {
                        this.c = 0;
                    }
                } else {
                    this.d -= (long) auVar.c;
                    if (this.d < 0) {
                        this.d = 0;
                    }
                }
            }
            if (z2) {
                try {
                    ALLog.trace("@_3_2_@", String.format(Locale.getDefault(), "@_3_2_6_@%d，%d，%d", Long.valueOf(auVar.a), Integer.valueOf(delete), Integer.valueOf(auVar.c)));
                    return true;
                } catch (Exception e2) {
                    e = e2;
                    z = true;
                    ALLog.trace("@_3_2_@", "@_3_2_8_@" + Log.getStackTraceString(e));
                    return z;
                }
            } else {
                ALLog.trace("@_3_2_@", String.format(Locale.getDefault(), "@_3_2_7_@%d，%d，%d", Long.valueOf(auVar.a), Integer.valueOf(delete), Integer.valueOf(auVar.c)));
                return true;
            }
        } catch (Exception e3) {
            e = e3;
            z = false;
            ALLog.trace("@_3_2_@", "@_3_2_8_@" + Log.getStackTraceString(e));
            return z;
        }
    }

    public int b() {
        return (int) this.c;
    }

    public int c() {
        return (int) this.d;
    }
}
