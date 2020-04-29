package com.amap.location.common.database;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import java.util.HashMap;

public abstract class AbstractContentProvider extends ContentProvider {
    protected UriMatcher a = new UriMatcher(-1);
    protected HashMap<Integer, SQLiteOpenHelper> b = new HashMap<>();
    protected HashMap<Integer, String> c = new HashMap<>();
    private final String d = "AbstractContentProvider";
    private String e = "";

    private <T> T a(T t, Object obj) {
        if (t != null) {
            return t;
        }
        throw new NullPointerException(String.valueOf(obj));
    }

    public int a(Uri uri, ContentValues contentValues, String str, String[] strArr) {
        a(uri, "uri");
        try {
            int match = this.a.match(uri);
            SQLiteOpenHelper sQLiteOpenHelper = this.b.get(Integer.valueOf(match));
            if (sQLiteOpenHelper == null) {
                return 0;
            }
            SQLiteDatabase writableDatabase = sQLiteOpenHelper.getWritableDatabase();
            String str2 = this.c.get(Integer.valueOf(match));
            if (writableDatabase == null || str2 == null) {
                return 0;
            }
            return writableDatabase.update(str2, contentValues, str, strArr);
        } catch (Exception e2) {
            return -1;
        }
    }

    public int a(Uri uri, String str, String[] strArr) {
        a(uri, "uri");
        try {
            int match = this.a.match(uri);
            SQLiteOpenHelper sQLiteOpenHelper = this.b.get(Integer.valueOf(match));
            if (sQLiteOpenHelper == null) {
                return 0;
            }
            SQLiteDatabase writableDatabase = sQLiteOpenHelper.getWritableDatabase();
            String str2 = this.c.get(Integer.valueOf(match));
            if (writableDatabase == null || str2 == null) {
                return 0;
            }
            return writableDatabase.delete(str2, str, strArr);
        } catch (Exception e2) {
            return -1;
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:24:0x0053, code lost:
        r0 = r3;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:27:?, code lost:
        r0.endTransaction();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:29:0x005c, code lost:
        r0 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:30:0x005d, code lost:
        if (r3 != null) goto L_0x005f;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:32:?, code lost:
        r3.endTransaction();
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Removed duplicated region for block: B:26:0x0056 A[SYNTHETIC, Splitter:B:26:0x0056] */
    /* JADX WARNING: Removed duplicated region for block: B:29:0x005c A[ExcHandler: all (r0v1 'th' java.lang.Throwable A[CUSTOM_DECLARE]), Splitter:B:1:0x0009] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public int a(android.net.Uri r7, android.content.ContentValues[] r8) {
        /*
            r6 = this;
            r3 = 0
            r1 = 0
            java.lang.String r0 = "uri"
            r6.a(r7, r0)
            int r2 = r8.length
            android.content.UriMatcher r0 = r6.a     // Catch:{ Exception -> 0x0052, all -> 0x005c }
            int r4 = r0.match(r7)     // Catch:{ Exception -> 0x0052, all -> 0x005c }
            java.util.HashMap<java.lang.Integer, android.database.sqlite.SQLiteOpenHelper> r0 = r6.b     // Catch:{ Exception -> 0x0052, all -> 0x005c }
            java.lang.Integer r5 = java.lang.Integer.valueOf(r4)     // Catch:{ Exception -> 0x0052, all -> 0x005c }
            java.lang.Object r0 = r0.get(r5)     // Catch:{ Exception -> 0x0052, all -> 0x005c }
            android.database.sqlite.SQLiteOpenHelper r0 = (android.database.sqlite.SQLiteOpenHelper) r0     // Catch:{ Exception -> 0x0052, all -> 0x005c }
            if (r0 != 0) goto L_0x001f
            r0 = r1
        L_0x001e:
            return r0
        L_0x001f:
            android.database.sqlite.SQLiteDatabase r3 = r0.getWritableDatabase()     // Catch:{ Exception -> 0x0052, all -> 0x005c }
            java.util.HashMap<java.lang.Integer, java.lang.String> r0 = r6.c     // Catch:{ Exception -> 0x0069, all -> 0x005c }
            java.lang.Integer r4 = java.lang.Integer.valueOf(r4)     // Catch:{ Exception -> 0x0069, all -> 0x005c }
            java.lang.Object r0 = r0.get(r4)     // Catch:{ Exception -> 0x0069, all -> 0x005c }
            java.lang.String r0 = (java.lang.String) r0     // Catch:{ Exception -> 0x0069, all -> 0x005c }
            if (r3 == 0) goto L_0x0033
            if (r0 != 0) goto L_0x003a
        L_0x0033:
            if (r3 == 0) goto L_0x0038
            r3.endTransaction()     // Catch:{ Exception -> 0x0063 }
        L_0x0038:
            r0 = r1
            goto L_0x001e
        L_0x003a:
            r3.beginTransaction()     // Catch:{ Exception -> 0x0069, all -> 0x005c }
        L_0x003d:
            if (r1 >= r2) goto L_0x0048
            r4 = 0
            r5 = r8[r1]     // Catch:{ Exception -> 0x0069, all -> 0x005c }
            r3.insert(r0, r4, r5)     // Catch:{ Exception -> 0x0069, all -> 0x005c }
            int r1 = r1 + 1
            goto L_0x003d
        L_0x0048:
            r3.setTransactionSuccessful()     // Catch:{ Exception -> 0x0069, all -> 0x005c }
            if (r3 == 0) goto L_0x0050
            r3.endTransaction()     // Catch:{ Exception -> 0x0065 }
        L_0x0050:
            r0 = r2
            goto L_0x001e
        L_0x0052:
            r0 = move-exception
            r0 = r3
        L_0x0054:
            if (r0 == 0) goto L_0x0050
            r0.endTransaction()     // Catch:{ Exception -> 0x005a }
            goto L_0x0050
        L_0x005a:
            r0 = move-exception
            goto L_0x0050
        L_0x005c:
            r0 = move-exception
            if (r3 == 0) goto L_0x0062
            r3.endTransaction()     // Catch:{ Exception -> 0x0067 }
        L_0x0062:
            throw r0
        L_0x0063:
            r0 = move-exception
            goto L_0x0038
        L_0x0065:
            r0 = move-exception
            goto L_0x0050
        L_0x0067:
            r1 = move-exception
            goto L_0x0062
        L_0x0069:
            r0 = move-exception
            r0 = r3
            goto L_0x0054
        */
        throw new UnsupportedOperationException("Method not decompiled: com.amap.location.common.database.AbstractContentProvider.a(android.net.Uri, android.content.ContentValues[]):int");
    }

    public long a(Uri uri) {
        a(uri, "uri");
        int match = this.a.match(uri);
        SQLiteOpenHelper sQLiteOpenHelper = this.b.get(Integer.valueOf(match));
        if (sQLiteOpenHelper == null) {
            return 0;
        }
        SQLiteDatabase writableDatabase = sQLiteOpenHelper.getWritableDatabase();
        String str = this.c.get(Integer.valueOf(match));
        if (writableDatabase == null || str == null) {
            return 0;
        }
        return DatabaseUtils.queryNumEntries(writableDatabase, str);
    }

    public long a(Uri uri, ContentValues contentValues) {
        a(uri, "uri");
        try {
            int match = this.a.match(uri);
            SQLiteOpenHelper sQLiteOpenHelper = this.b.get(Integer.valueOf(match));
            if (sQLiteOpenHelper == null) {
                return 0;
            }
            SQLiteDatabase writableDatabase = sQLiteOpenHelper.getWritableDatabase();
            String str = this.c.get(Integer.valueOf(match));
            if (writableDatabase == null || str == null) {
                return 0;
            }
            return writableDatabase.insert(str, null, contentValues);
        } catch (Exception e2) {
            return -1;
        }
    }

    public Cursor a(Uri uri, String[] strArr, String str, String[] strArr2, String str2) {
        return b(uri, strArr, str, strArr2, str2, null);
    }

    public Cursor a(Uri uri, String[] strArr, String str, String[] strArr2, String str2, String str3) {
        a(uri, "uri");
        int match = this.a.match(uri);
        SQLiteOpenHelper sQLiteOpenHelper = this.b.get(Integer.valueOf(match));
        if (sQLiteOpenHelper == null) {
            return null;
        }
        SQLiteDatabase writableDatabase = sQLiteOpenHelper.getWritableDatabase();
        String str4 = this.c.get(Integer.valueOf(match));
        if (writableDatabase == null || str4 == null) {
            return null;
        }
        SQLiteQueryBuilder sQLiteQueryBuilder = new SQLiteQueryBuilder();
        sQLiteQueryBuilder.setTables(str4);
        return sQLiteQueryBuilder.query(writableDatabase, strArr, str, strArr2, null, null, str2, str3);
    }

    public abstract String a();

    public void a(Integer num, String str, SQLiteOpenHelper sQLiteOpenHelper) {
        if (sQLiteOpenHelper != null) {
            this.b.put(num, sQLiteOpenHelper);
            this.c.put(num, str);
            this.a.addURI(this.e, str, num.intValue());
        }
    }

    public Cursor b(Uri uri, String[] strArr, String str, String[] strArr2, String str2, String str3) {
        a(uri, "uri");
        try {
            int match = this.a.match(uri);
            SQLiteOpenHelper sQLiteOpenHelper = this.b.get(Integer.valueOf(match));
            if (sQLiteOpenHelper == null) {
                return null;
            }
            SQLiteDatabase writableDatabase = sQLiteOpenHelper.getWritableDatabase();
            String str4 = this.c.get(Integer.valueOf(match));
            if (writableDatabase == null || str4 == null) {
                return null;
            }
            SQLiteQueryBuilder sQLiteQueryBuilder = new SQLiteQueryBuilder();
            sQLiteQueryBuilder.setTables(str4);
            return sQLiteQueryBuilder.query(writableDatabase, strArr, str, strArr2, null, null, str2, str3);
        } catch (Exception e2) {
            return null;
        }
    }

    /* access modifiers changed from: protected */
    public abstract void b();

    public int bulkInsert(Uri uri, ContentValues[] contentValuesArr) {
        int i = 0;
        a(uri, "uri");
        int match = this.a.match(uri);
        SQLiteOpenHelper sQLiteOpenHelper = this.b.get(Integer.valueOf(match));
        if (sQLiteOpenHelper == null) {
            return 0;
        }
        SQLiteDatabase writableDatabase = sQLiteOpenHelper.getWritableDatabase();
        String str = this.c.get(Integer.valueOf(match));
        if (writableDatabase == null || str == null) {
            return 0;
        }
        int length = contentValuesArr.length;
        writableDatabase.beginTransaction();
        while (i < length) {
            try {
                writableDatabase.insert(str, null, contentValuesArr[i]);
                i++;
            } catch (Throwable th) {
                writableDatabase.endTransaction();
                throw th;
            }
        }
        writableDatabase.setTransactionSuccessful();
        writableDatabase.endTransaction();
        return length;
    }

    public int delete(Uri uri, String str, String[] strArr) {
        a(uri, "uri");
        int match = this.a.match(uri);
        SQLiteOpenHelper sQLiteOpenHelper = this.b.get(Integer.valueOf(match));
        if (sQLiteOpenHelper == null) {
            return 0;
        }
        SQLiteDatabase writableDatabase = sQLiteOpenHelper.getWritableDatabase();
        String str2 = this.c.get(Integer.valueOf(match));
        if (writableDatabase == null || str2 == null) {
            return 0;
        }
        return writableDatabase.delete(str2, str, strArr);
    }

    public String getType(Uri uri) {
        int match = this.a.match(uri);
        return "vnd.android.cursor.dir/" + this.c.get(Integer.valueOf(match));
    }

    public Uri insert(Uri uri, ContentValues contentValues) {
        a(uri, "uri");
        int match = this.a.match(uri);
        SQLiteOpenHelper sQLiteOpenHelper = this.b.get(Integer.valueOf(match));
        if (sQLiteOpenHelper == null) {
            return ContentUris.withAppendedId(uri, -1);
        }
        SQLiteDatabase writableDatabase = sQLiteOpenHelper.getWritableDatabase();
        String str = this.c.get(Integer.valueOf(match));
        return (writableDatabase == null || str == null) ? ContentUris.withAppendedId(uri, -1) : ContentUris.withAppendedId(uri, writableDatabase.insert(str, null, contentValues));
    }

    public boolean onCreate() {
        this.e = a();
        b();
        return false;
    }

    public Cursor query(Uri uri, String[] strArr, String str, String[] strArr2, String str2) {
        return a(uri, strArr, str, strArr2, str2, null);
    }

    public int update(Uri uri, ContentValues contentValues, String str, String[] strArr) {
        a(uri, "uri");
        int match = this.a.match(uri);
        SQLiteOpenHelper sQLiteOpenHelper = this.b.get(Integer.valueOf(match));
        if (sQLiteOpenHelper == null) {
            return 0;
        }
        SQLiteDatabase writableDatabase = sQLiteOpenHelper.getWritableDatabase();
        String str2 = this.c.get(Integer.valueOf(match));
        if (writableDatabase == null || str2 == null) {
            return 0;
        }
        return writableDatabase.update(str2, contentValues, str, strArr);
    }
}
