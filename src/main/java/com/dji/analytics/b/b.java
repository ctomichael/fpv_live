package com.dji.analytics.b;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import com.dji.analytics.DJIA;
import java.util.Arrays;

/* compiled from: SqliteHelper */
class b extends SQLiteOpenHelper {
    private final String a = "CREATE TABLE dji_analytics_reports (_id INTEGER PRIMARY KEY AUTOINCREMENT, reportid VARCHAR(50), report BLOB, state INTEGER)";
    private final String b = "DROP TABLE IF EXISTS dji_analytics_reports";
    private final String c = "CREATE INDEX reportid ON dji_analytics_reports(reportid)";

    b(Context context) {
        super(context, "com.dji.dji_analytics_a_db", (SQLiteDatabase.CursorFactory) null, 2);
    }

    public void onCreate(SQLiteDatabase sQLiteDatabase) {
        try {
            sQLiteDatabase.execSQL(this.a);
            sQLiteDatabase.execSQL(this.c);
        } catch (SQLiteException e) {
            DJIA.log.b(DJIA.LOG_TAG, "Error in create db." + e.toString());
        }
    }

    public void onUpgrade(SQLiteDatabase sQLiteDatabase, int i, int i2) {
        sQLiteDatabase.execSQL(this.b);
        onCreate(sQLiteDatabase);
    }

    /* access modifiers changed from: package-private */
    public boolean a(String str, String str2, ContentValues contentValues) {
        try {
            getWritableDatabase().insert(str, str2, contentValues);
            if (DJIA.DEV_FLAG) {
                DJIA.log.a(DJIA.LOG_TAG, "insert " + contentValues.toString() + " into " + str);
            }
            return true;
        } catch (SQLException e) {
            if (DJIA.DEV_FLAG) {
                DJIA.log.b(DJIA.LOG_TAG, "Error in insert data " + e);
            }
            return false;
        }
    }

    /* access modifiers changed from: package-private */
    public boolean a(String str, String str2, String[] strArr) {
        try {
            getWritableDatabase().delete(str, str2, strArr);
            if (DJIA.DEV_FLAG) {
                DJIA.log.a(DJIA.LOG_TAG, "delete from  " + str2 + " " + Arrays.toString(strArr) + " into " + str);
            }
            return true;
        } catch (SQLException e) {
            if (DJIA.DEV_FLAG) {
                DJIA.log.b(DJIA.LOG_TAG, "Error in delete data " + e);
            }
            return false;
        }
    }

    /* access modifiers changed from: package-private */
    public boolean a(String str, ContentValues contentValues, String str2, String[] strArr) {
        try {
            getWritableDatabase().update(str, contentValues, str2, strArr);
            if (DJIA.DEV_FLAG) {
                DJIA.log.a(DJIA.LOG_TAG, "update from  " + str2 + " " + Arrays.toString(strArr) + " into " + str + " " + contentValues.toString());
            }
            return true;
        } catch (SQLException e) {
            if (DJIA.DEV_FLAG) {
                DJIA.log.b(DJIA.LOG_TAG, "Error in delete data " + e);
            }
            return false;
        }
    }

    /* access modifiers changed from: package-private */
    public Cursor a(String str) {
        Cursor cursor;
        SQLException e;
        try {
            Cursor rawQuery = getReadableDatabase().rawQuery(str, null);
            try {
                rawQuery.moveToFirst();
                if (!DJIA.DEV_FLAG) {
                    return rawQuery;
                }
                DJIA.log.a(DJIA.LOG_TAG, "exce sql" + str + " data len is " + rawQuery.getCount());
                return rawQuery;
            } catch (SQLException e2) {
                e = e2;
                cursor = rawQuery;
                if (DJIA.DEV_FLAG) {
                    DJIA.log.b(DJIA.LOG_TAG, "Error in insert data " + e);
                }
                if (cursor != null) {
                    cursor.close();
                }
                return null;
            }
        } catch (SQLException e3) {
            e = e3;
            cursor = null;
        }
    }
}
