package dji.thirdparty.ciphersql.database;

import android.util.Log;

public abstract class SQLiteProgram extends SQLiteClosable {
    private static final String TAG = "SQLiteProgram";
    boolean mClosed = false;
    private SQLiteCompiledSql mCompiledSql;
    @Deprecated
    protected SQLiteDatabase mDatabase;
    final String mSql;
    @Deprecated
    protected long nHandle = 0;
    @Deprecated
    protected long nStatement = 0;

    private final native void native_clear_bindings();

    /* access modifiers changed from: protected */
    public final native void native_bind_blob(int i, byte[] bArr);

    /* access modifiers changed from: protected */
    public final native void native_bind_double(int i, double d);

    /* access modifiers changed from: protected */
    public final native void native_bind_long(int i, long j);

    /* access modifiers changed from: protected */
    public final native void native_bind_null(int i);

    /* access modifiers changed from: protected */
    public final native void native_bind_string(int i, String str);

    /* access modifiers changed from: protected */
    @Deprecated
    public final native void native_compile(String str);

    /* access modifiers changed from: protected */
    @Deprecated
    public final native void native_finalize();

    SQLiteProgram(SQLiteDatabase db, String sql) {
        this.mDatabase = db;
        this.mSql = sql.trim();
        db.acquireReference();
        db.addSQLiteClosable(this);
        this.nHandle = db.mNativeHandle;
        String prefixSql = this.mSql.length() >= 6 ? this.mSql.substring(0, 6) : this.mSql;
        if (prefixSql.equalsIgnoreCase("INSERT") || prefixSql.equalsIgnoreCase("UPDATE") || prefixSql.equalsIgnoreCase("REPLAC") || prefixSql.equalsIgnoreCase("DELETE") || prefixSql.equalsIgnoreCase("SELECT")) {
            this.mCompiledSql = db.getCompiledStatementForSql(sql);
            if (this.mCompiledSql == null) {
                this.mCompiledSql = new SQLiteCompiledSql(db, sql);
                this.mCompiledSql.acquire();
                db.addToCompiledQueries(sql, this.mCompiledSql);
                if (SQLiteDebug.DEBUG_ACTIVE_CURSOR_FINALIZATION) {
                    Log.v(TAG, "Created DbObj (id#" + this.mCompiledSql.nStatement + ") for sql: " + sql);
                }
            } else if (!this.mCompiledSql.acquire()) {
                long last = this.mCompiledSql.nStatement;
                this.mCompiledSql = new SQLiteCompiledSql(db, sql);
                if (SQLiteDebug.DEBUG_ACTIVE_CURSOR_FINALIZATION) {
                    Log.v(TAG, "** possible bug ** Created NEW DbObj (id#" + this.mCompiledSql.nStatement + ") because the previously created DbObj (id#" + last + ") was not released for sql:" + sql);
                }
            }
            this.nStatement = this.mCompiledSql.nStatement;
            return;
        }
        this.mCompiledSql = new SQLiteCompiledSql(db, sql);
        this.nStatement = this.mCompiledSql.nStatement;
    }

    /* access modifiers changed from: protected */
    public void onAllReferencesReleased() {
        releaseCompiledSqlIfNotInCache();
        this.mDatabase.releaseReference();
        this.mDatabase.removeSQLiteClosable(this);
    }

    /* access modifiers changed from: protected */
    public void onAllReferencesReleasedFromContainer() {
        releaseCompiledSqlIfNotInCache();
        this.mDatabase.releaseReference();
    }

    private void releaseCompiledSqlIfNotInCache() {
        if (this.mCompiledSql != null) {
            synchronized (this.mDatabase.mCompiledQueries) {
                if (!this.mDatabase.mCompiledQueries.containsValue(this.mCompiledSql)) {
                    this.mCompiledSql.releaseSqlStatement();
                    this.mCompiledSql = null;
                    this.nStatement = 0;
                } else {
                    this.mCompiledSql.release();
                }
            }
        }
    }

    public final long getUniqueId() {
        return this.nStatement;
    }

    /* access modifiers changed from: package-private */
    public String getSqlString() {
        return this.mSql;
    }

    /* access modifiers changed from: protected */
    @Deprecated
    public void compile(String sql, boolean forceCompilation) {
    }

    public void bindNull(int index) {
        if (this.mClosed) {
            throw new IllegalStateException("program already closed");
        } else if (!this.mDatabase.isOpen()) {
            throw new IllegalStateException("database " + this.mDatabase.getPath() + " already closed");
        } else {
            acquireReference();
            try {
                native_bind_null(index);
            } finally {
                releaseReference();
            }
        }
    }

    public void bindLong(int index, long value) {
        if (this.mClosed) {
            throw new IllegalStateException("program already closed");
        } else if (!this.mDatabase.isOpen()) {
            throw new IllegalStateException("database " + this.mDatabase.getPath() + " already closed");
        } else {
            acquireReference();
            try {
                native_bind_long(index, value);
            } finally {
                releaseReference();
            }
        }
    }

    public void bindDouble(int index, double value) {
        if (this.mClosed) {
            throw new IllegalStateException("program already closed");
        } else if (!this.mDatabase.isOpen()) {
            throw new IllegalStateException("database " + this.mDatabase.getPath() + " already closed");
        } else {
            acquireReference();
            try {
                native_bind_double(index, value);
            } finally {
                releaseReference();
            }
        }
    }

    public void bindString(int index, String value) {
        if (value == null) {
            throw new IllegalArgumentException("the bind value at index " + index + " is null");
        } else if (this.mClosed) {
            throw new IllegalStateException("program already closed");
        } else if (!this.mDatabase.isOpen()) {
            throw new IllegalStateException("database " + this.mDatabase.getPath() + " already closed");
        } else {
            acquireReference();
            try {
                native_bind_string(index, value);
            } finally {
                releaseReference();
            }
        }
    }

    public void bindBlob(int index, byte[] value) {
        if (value == null) {
            throw new IllegalArgumentException("the bind value at index " + index + " is null");
        } else if (this.mClosed) {
            throw new IllegalStateException("program already closed");
        } else if (!this.mDatabase.isOpen()) {
            throw new IllegalStateException("database " + this.mDatabase.getPath() + " already closed");
        } else {
            acquireReference();
            try {
                native_bind_blob(index, value);
            } finally {
                releaseReference();
            }
        }
    }

    public void clearBindings() {
        if (this.mClosed) {
            throw new IllegalStateException("program already closed");
        } else if (!this.mDatabase.isOpen()) {
            throw new IllegalStateException("database " + this.mDatabase.getPath() + " already closed");
        } else {
            acquireReference();
            try {
                native_clear_bindings();
            } finally {
                releaseReference();
            }
        }
    }

    /* JADX INFO: finally extract failed */
    public void close() {
        if (!this.mClosed && this.mDatabase.isOpen()) {
            this.mDatabase.lock();
            try {
                releaseReference();
                this.mDatabase.unlock();
                this.mClosed = true;
            } catch (Throwable th) {
                this.mDatabase.unlock();
                throw th;
            }
        }
    }
}
