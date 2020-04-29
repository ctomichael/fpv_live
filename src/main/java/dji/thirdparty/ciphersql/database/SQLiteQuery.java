package dji.thirdparty.ciphersql.database;

import android.os.SystemClock;
import android.util.Log;
import dji.thirdparty.ciphersql.CursorWindow;

public class SQLiteQuery extends SQLiteProgram {
    private static final String TAG = "Cursor";
    private String[] mBindArgs;
    private Object[] mObjectBindArgs;
    private int mOffsetIndex;

    private final native int native_column_count();

    private final native String native_column_name(int i);

    private final native int native_fill_window(CursorWindow cursorWindow, int i, int i2, int i3, int i4);

    SQLiteQuery(SQLiteDatabase db, String query, int offsetIndex, String[] bindArgs) {
        super(db, query);
        this.mOffsetIndex = offsetIndex;
        this.mBindArgs = bindArgs;
    }

    SQLiteQuery(SQLiteDatabase db, String query, int offsetIndex, Object[] bindArgs) {
        super(db, query);
        this.mOffsetIndex = offsetIndex;
        this.mObjectBindArgs = bindArgs;
        this.mBindArgs = new String[(this.mObjectBindArgs != null ? this.mObjectBindArgs.length : 0)];
    }

    /* access modifiers changed from: package-private */
    public int fillWindow(CursorWindow window, int maxRead, int lastPos) {
        int numRows;
        long uptimeMillis = SystemClock.uptimeMillis();
        this.mDatabase.lock();
        try {
            acquireReference();
            window.acquireReference();
            numRows = native_fill_window(window, window.getStartPosition(), this.mOffsetIndex, maxRead, lastPos);
            if (SQLiteDebug.DEBUG_SQL_STATEMENTS) {
                Log.d(TAG, "fillWindow(): " + this.mSql);
            }
            window.releaseReference();
            releaseReference();
            this.mDatabase.unlock();
        } catch (IllegalStateException e) {
            numRows = 0;
            window.releaseReference();
            releaseReference();
            this.mDatabase.unlock();
        } catch (SQLiteDatabaseCorruptException e2) {
            this.mDatabase.onCorruption();
            throw e2;
        } catch (Throwable th) {
            window.releaseReference();
            throw th;
        }
        return numRows;
    }

    /* access modifiers changed from: package-private */
    public int columnCountLocked() {
        acquireReference();
        try {
            return native_column_count();
        } finally {
            releaseReference();
        }
    }

    /* access modifiers changed from: package-private */
    public String columnNameLocked(int columnIndex) {
        acquireReference();
        try {
            return native_column_name(columnIndex);
        } finally {
            releaseReference();
        }
    }

    public String toString() {
        return "SQLiteQuery: " + this.mSql;
    }

    /* access modifiers changed from: package-private */
    public void requery() {
        if (this.mBindArgs != null) {
            int len = this.mBindArgs.length;
            try {
                if (this.mObjectBindArgs != null) {
                    bindArguments(this.mObjectBindArgs);
                    return;
                }
                for (int i = 0; i < len; i++) {
                    super.bindString(i + 1, this.mBindArgs[i]);
                }
            } catch (SQLiteMisuseException e) {
                StringBuilder errMsg = new StringBuilder("mSql " + this.mSql);
                for (int i2 = 0; i2 < len; i2++) {
                    errMsg.append(" ");
                    errMsg.append(this.mBindArgs[i2]);
                }
                errMsg.append(" ");
                throw new IllegalStateException(errMsg.toString(), e);
            }
        }
    }

    public void bindNull(int index) {
        this.mBindArgs[index - 1] = null;
        if (!this.mClosed) {
            super.bindNull(index);
        }
    }

    public void bindLong(int index, long value) {
        this.mBindArgs[index - 1] = Long.toString(value);
        if (!this.mClosed) {
            super.bindLong(index, value);
        }
    }

    public void bindDouble(int index, double value) {
        this.mBindArgs[index - 1] = Double.toString(value);
        if (!this.mClosed) {
            super.bindDouble(index, value);
        }
    }

    public void bindString(int index, String value) {
        this.mBindArgs[index - 1] = value;
        if (!this.mClosed) {
            super.bindString(index, value);
        }
    }

    public void bindArguments(Object[] args) {
        if (args != null && args.length > 0) {
            for (int i = 0; i < args.length; i++) {
                Object value = args[i];
                if (value == null) {
                    bindNull(i + 1);
                } else if (value instanceof Double) {
                    bindDouble(i + 1, ((Double) value).doubleValue());
                } else if (value instanceof Float) {
                    bindDouble(i + 1, Double.valueOf((double) ((Number) value).floatValue()).doubleValue());
                } else if (value instanceof Long) {
                    bindLong(i + 1, ((Long) value).longValue());
                } else if (value instanceof Integer) {
                    bindLong(i + 1, Long.valueOf((long) ((Number) value).intValue()).longValue());
                } else if (value instanceof Boolean) {
                    bindLong(i + 1, ((Boolean) value).booleanValue() ? 1 : 0);
                } else if (value instanceof byte[]) {
                    bindBlob(i + 1, (byte[]) value);
                } else {
                    bindString(i + 1, value.toString());
                }
            }
        }
    }
}
