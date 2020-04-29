package com.dji.mapkit.core.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class KeyValueDB extends SQLiteOpenHelper {
    private static String DATABASE_NAME = "_app";
    private static String DATABASE_TABLE = "_cache";
    private static int DATABASE_VERSION = 1;
    private static final String KEY = "KEY";
    private static final String KEY_CREATED_AT = "KEY_CREATED_AT";
    private static final String PERSIST = "PERSIST";
    private static final String TAG = KeyValueDB.class.getSimpleName();
    private static final String VALUE = "VALUE";
    private static Context sContext;
    private static KeyValueDB sInstance;

    private static String createDBQueryBuilder(String database) {
        return "CREATE TABLE " + database + "(" + KEY + " TEXT PRIMARY KEY," + VALUE + " TEXT," + PERSIST + " INTEGER," + KEY_CREATED_AT + " DATETIME)";
    }

    private static String alterTableQueryBuilder(String table, long count, long limit) {
        return "DELETE FROM " + table + " WHERE " + KEY + " IN (SELECT " + KEY + " FROM " + DATABASE_TABLE + " ORDER BY " + KEY_CREATED_AT + " ASC LIMIT " + String.valueOf(count - limit) + ");";
    }

    private static String getState() {
        return "State: " + DATABASE_TABLE + " on " + DATABASE_NAME + " @ " + DATABASE_VERSION;
    }

    public static void init(Context context) {
        init(context, DATABASE_NAME, DATABASE_TABLE);
    }

    private static void init(Context context, String databaseName, String tableName) {
        sContext = context;
        setDBName(databaseName);
        setTableName(tableName);
    }

    private static void setDBName(String name) {
        DATABASE_NAME = name;
    }

    private static void setTableName(String name) {
        DATABASE_TABLE = name;
    }

    private static void setDatabaseVersion(int version) {
        DATABASE_VERSION = version;
    }

    private static synchronized KeyValueDB getInstance(Context context) {
        KeyValueDB keyValueDB;
        synchronized (KeyValueDB.class) {
            if (sInstance == null) {
                sInstance = new KeyValueDB(context.getApplicationContext());
            }
            keyValueDB = sInstance;
        }
        return keyValueDB;
    }

    private KeyValueDB(Context context) {
        super(context, DATABASE_NAME, (SQLiteDatabase.CursorFactory) null, DATABASE_VERSION);
    }

    public void onCreate(SQLiteDatabase db) {
        Log.v(TAG, "onCreate");
        flush(db);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.v(TAG, "onUpgrade");
        flush(db);
    }

    public static synchronized long set(String key, String value, Boolean persist) {
        long j;
        synchronized (KeyValueDB.class) {
            j = set(sContext, key, value, persist);
        }
        return j;
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
    public static synchronized long set(Context context, String key, String value, Boolean persist) {
        long row;
        synchronized (KeyValueDB.class) {
            Log.i(TAG, getState());
            String key2 = DatabaseUtils.sqlEscapeString(key);
            SQLiteDatabase db = getInstance(context).getWritableDatabase();
            row = 0;
            if (db != null) {
                ContentValues values = new ContentValues();
                values.put(KEY, key2);
                values.put(VALUE, value);
                if (persist.booleanValue()) {
                    values.put(PERSIST, (Integer) 1);
                } else {
                    values.put(PERSIST, (Integer) 0);
                }
                values.put(KEY_CREATED_AT, "time('now')");
                try {
                    row = db.replace(DATABASE_TABLE, null, values);
                } catch (SQLiteException e) {
                    flush(e, db);
                    set(context, key2, value, persist);
                }
                db.close();
            }
        }
        return row;
    }

    public static synchronized String get(String key, String defaultValue) {
        String str;
        synchronized (KeyValueDB.class) {
            str = get(sContext, key, defaultValue);
        }
        return str;
    }

    public static synchronized String get(Context context, String key, String defaultValue) {
        String value;
        synchronized (KeyValueDB.class) {
            Log.i(TAG, getState());
            String key2 = DatabaseUtils.sqlEscapeString(key);
            Log.v(TAG, "getting cache: " + key2);
            SQLiteDatabase db = getInstance(context).getReadableDatabase();
            value = defaultValue;
            if (db != null) {
                Cursor c = null;
                try {
                    c = db.query(DATABASE_TABLE, new String[]{VALUE}, "KEY=?", new String[]{key2}, null, null, null);
                } catch (SQLiteException e) {
                    flush(e, db);
                    get(context, key2, defaultValue);
                }
                if (c != null) {
                    if (c.moveToNext()) {
                        value = c.getString(c.getColumnIndex(VALUE));
                    }
                    Log.v(TAG, "get cache size:" + String.valueOf(value.length()));
                    c.close();
                }
                db.close();
            }
        }
        return value;
    }

    public static synchronized long clearCacheByLimit(long limit) {
        long clearCacheByLimit;
        synchronized (KeyValueDB.class) {
            clearCacheByLimit = clearCacheByLimit(sContext, limit);
        }
        return clearCacheByLimit;
    }

    public static synchronized long clearCacheByLimit(Context context, long limit) {
        long numRows;
        synchronized (KeyValueDB.class) {
            SQLiteDatabase db = getInstance(context).getWritableDatabase();
            numRows = 0;
            if (db != null) {
                Cursor c = null;
                try {
                    c = db.query(DATABASE_TABLE, null, null, null, null, null, null);
                } catch (SQLiteException e) {
                    flush(e, db);
                    clearCacheByLimit(context, limit);
                }
                if (c != null) {
                    long count = (long) c.getCount();
                    Log.v(TAG, "cached rows" + String.valueOf(count));
                    if (count > limit) {
                        try {
                            db.execSQL(alterTableQueryBuilder(DATABASE_TABLE, count, limit));
                        } catch (SQLiteException e2) {
                            flush(e2, db);
                            clearCacheByLimit(context, limit);
                        }
                    }
                    numRows = count - ((long) c.getCount());
                    c.close();
                }
                db.close();
            }
        }
        return numRows;
    }

    private static void flush(Exception e, SQLiteDatabase db) {
        e.printStackTrace();
        flush(db);
    }

    private static void flush(SQLiteDatabase db) {
        try {
            db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE);
            db.execSQL(createDBQueryBuilder(DATABASE_TABLE));
        } catch (SQLException e) {
            e.printStackTrace();
            throw new IllegalStateException("SQLException while flush. Have to drop caching");
        }
    }
}
