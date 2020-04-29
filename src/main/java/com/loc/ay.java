package com.loc;

import android.content.Context;
import android.content.ContextWrapper;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/* compiled from: DB */
public final class ay extends SQLiteOpenHelper {
    /* access modifiers changed from: private */
    public static boolean b = true;
    /* access modifiers changed from: private */
    public static boolean c = false;
    private au a;

    /* compiled from: DB */
    public static class a extends ContextWrapper {
        private String a;
        private Context b;

        public a(Context context, String str) {
            super(context);
            this.a = str;
            this.b = context;
        }

        /* JADX WARNING: No exception handlers in catch block: Catch:{  } */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public final java.io.File getDatabasePath(java.lang.String r7) {
            /*
                r6 = this;
                boolean r0 = com.loc.ay.b     // Catch:{ Throwable -> 0x00a7 }
                if (r0 == 0) goto L_0x0011
                android.content.Context r0 = r6.b     // Catch:{ Throwable -> 0x00a7 }
                java.lang.String r1 = "android.permission.WRITE_EXTERNAL_STORAGE"
                boolean r0 = com.loc.ad.a(r0, r1)     // Catch:{ Throwable -> 0x00a7 }
                if (r0 != 0) goto L_0x0016
            L_0x0011:
                java.io.File r0 = super.getDatabasePath(r7)     // Catch:{ Throwable -> 0x00a7 }
            L_0x0015:
                return r0
            L_0x0016:
                java.lang.StringBuilder r0 = new java.lang.StringBuilder     // Catch:{ Throwable -> 0x00a7 }
                r0.<init>()     // Catch:{ Throwable -> 0x00a7 }
                java.lang.String r1 = r6.a     // Catch:{ Throwable -> 0x00a7 }
                java.lang.StringBuilder r0 = r0.append(r1)     // Catch:{ Throwable -> 0x00a7 }
                java.lang.String r1 = "/"
                java.lang.StringBuilder r0 = r0.append(r1)     // Catch:{ Throwable -> 0x00a7 }
                java.lang.StringBuilder r0 = r0.append(r7)     // Catch:{ Throwable -> 0x00a7 }
                java.lang.String r2 = r0.toString()     // Catch:{ Throwable -> 0x00a7 }
                java.io.File r0 = new java.io.File     // Catch:{ Throwable -> 0x00a7 }
                java.lang.String r1 = r6.a     // Catch:{ Throwable -> 0x00a7 }
                r0.<init>(r1)     // Catch:{ Throwable -> 0x00a7 }
                boolean r1 = r0.exists()     // Catch:{ Throwable -> 0x00a7 }
                if (r1 != 0) goto L_0x0040
                r0.mkdirs()     // Catch:{ Throwable -> 0x00a7 }
            L_0x0040:
                boolean r0 = com.loc.ay.c     // Catch:{ Throwable -> 0x00a7 }
                if (r0 == 0) goto L_0x004c
                boolean r0 = com.loc.ay.b     // Catch:{ Throwable -> 0x00a7 }
                if (r0 != 0) goto L_0x0081
            L_0x004c:
                boolean unused = com.loc.ay.c = true     // Catch:{ Throwable -> 0x00a7 }
                java.lang.StringBuilder r0 = new java.lang.StringBuilder     // Catch:{ Throwable -> 0x00a7 }
                r0.<init>()     // Catch:{ Throwable -> 0x00a7 }
                java.lang.String r1 = r6.a     // Catch:{ Throwable -> 0x00a7 }
                java.lang.StringBuilder r0 = r0.append(r1)     // Catch:{ Throwable -> 0x00a7 }
                java.lang.String r1 = java.io.File.separator     // Catch:{ Throwable -> 0x00a7 }
                java.lang.StringBuilder r0 = r0.append(r1)     // Catch:{ Throwable -> 0x00a7 }
                long r4 = java.lang.System.currentTimeMillis()     // Catch:{ Throwable -> 0x00a7 }
                java.lang.StringBuilder r0 = r0.append(r4)     // Catch:{ Throwable -> 0x00a7 }
                java.lang.String r1 = ".db"
                java.lang.StringBuilder r0 = r0.append(r1)     // Catch:{ Throwable -> 0x00a7 }
                java.lang.String r0 = r0.toString()     // Catch:{ Throwable -> 0x00a7 }
                java.io.File r1 = new java.io.File     // Catch:{ Throwable -> 0x00a7 }
                r1.<init>(r0)     // Catch:{ Throwable -> 0x00a7 }
                boolean r0 = r1.createNewFile()     // Catch:{ Throwable -> 0x00a7 }
                if (r0 == 0) goto L_0x0099
                r1.delete()     // Catch:{ Throwable -> 0x00a7 }
            L_0x0081:
                r1 = 0
                java.io.File r0 = new java.io.File     // Catch:{ Throwable -> 0x00a7 }
                r0.<init>(r2)     // Catch:{ Throwable -> 0x00a7 }
                boolean r2 = r0.exists()     // Catch:{ Throwable -> 0x00a7 }
                if (r2 != 0) goto L_0x00b1
                boolean r1 = r0.createNewFile()     // Catch:{ IOException -> 0x00a2 }
            L_0x0091:
                if (r1 != 0) goto L_0x0015
                java.io.File r0 = super.getDatabasePath(r7)     // Catch:{ Throwable -> 0x00a7 }
                goto L_0x0015
            L_0x0099:
                boolean unused = com.loc.ay.b = false     // Catch:{ Throwable -> 0x00a7 }
                java.io.File r0 = super.getDatabasePath(r7)     // Catch:{ Throwable -> 0x00a7 }
                goto L_0x0015
            L_0x00a2:
                r2 = move-exception
                boolean unused = com.loc.ay.b = false     // Catch:{ Throwable -> 0x00a7 }
                goto L_0x0091
            L_0x00a7:
                r0 = move-exception
                boolean unused = com.loc.ay.b = false
                java.io.File r0 = super.getDatabasePath(r7)
                goto L_0x0015
            L_0x00b1:
                r1 = 1
                goto L_0x0091
            */
            throw new UnsupportedOperationException("Method not decompiled: com.loc.ay.a.getDatabasePath(java.lang.String):java.io.File");
        }

        public final SQLiteDatabase openOrCreateDatabase(String str, int i, SQLiteDatabase.CursorFactory cursorFactory) {
            SQLiteDatabase sQLiteDatabase;
            if (getDatabasePath(str) != null) {
                try {
                    sQLiteDatabase = SQLiteDatabase.openOrCreateDatabase(getDatabasePath(str), (SQLiteDatabase.CursorFactory) null);
                } catch (Throwable th) {
                    sQLiteDatabase = null;
                }
                if (sQLiteDatabase != null) {
                    return sQLiteDatabase;
                }
            }
            return SQLiteDatabase.openOrCreateDatabase(this.b.getApplicationContext().getDatabasePath(str), (SQLiteDatabase.CursorFactory) null);
        }

        public final SQLiteDatabase openOrCreateDatabase(String str, int i, SQLiteDatabase.CursorFactory cursorFactory, DatabaseErrorHandler databaseErrorHandler) {
            SQLiteDatabase sQLiteDatabase;
            if (getDatabasePath(str) != null) {
                try {
                    sQLiteDatabase = SQLiteDatabase.openOrCreateDatabase(getDatabasePath(str), (SQLiteDatabase.CursorFactory) null);
                } catch (Throwable th) {
                    sQLiteDatabase = null;
                }
                if (sQLiteDatabase != null) {
                    return sQLiteDatabase;
                }
            }
            return SQLiteDatabase.openOrCreateDatabase(this.b.getApplicationContext().getDatabasePath(str), (SQLiteDatabase.CursorFactory) null);
        }
    }

    public ay(Context context, String str, au auVar) {
        super(context, str, (SQLiteDatabase.CursorFactory) null, 1);
        this.a = auVar;
    }

    public final void onCreate(SQLiteDatabase sQLiteDatabase) {
        this.a.a(sQLiteDatabase);
    }

    public final void onUpgrade(SQLiteDatabase sQLiteDatabase, int i, int i2) {
    }
}
