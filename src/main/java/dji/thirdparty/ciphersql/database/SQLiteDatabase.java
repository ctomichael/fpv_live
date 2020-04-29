package dji.thirdparty.ciphersql.database;

import android.content.ContentValues;
import android.content.Context;
import android.os.Debug;
import android.os.SystemClock;
import android.text.TextUtils;
import android.util.Log;
import android.util.Pair;
import com.mapzen.android.lost.internal.Clock;
import dji.component.accountcenter.IMemberProtocol;
import dji.thirdparty.ciphersql.CrossProcessCursorWrapper;
import dji.thirdparty.ciphersql.Cursor;
import dji.thirdparty.ciphersql.CursorWrapper;
import dji.thirdparty.ciphersql.DatabaseErrorHandler;
import dji.thirdparty.ciphersql.DatabaseUtils;
import dji.thirdparty.ciphersql.SQLException;
import dji.thirdparty.ciphersql.database.SQLiteDebug;
import java.io.File;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;
import java.util.concurrent.locks.ReentrantLock;
import java.util.regex.Pattern;

public class SQLiteDatabase extends SQLiteClosable {
    private static final String COMMIT_SQL = "COMMIT;";
    public static final int CONFLICT_ABORT = 2;
    public static final int CONFLICT_FAIL = 3;
    public static final int CONFLICT_IGNORE = 4;
    public static final int CONFLICT_NONE = 0;
    public static final int CONFLICT_REPLACE = 5;
    public static final int CONFLICT_ROLLBACK = 1;
    private static final String[] CONFLICT_VALUES = {"", " OR ROLLBACK ", " OR ABORT ", " OR FAIL ", " OR IGNORE ", " OR REPLACE "};
    public static final int CREATE_IF_NECESSARY = 268435456;
    private static final Pattern EMAIL_IN_DB_PATTERN = Pattern.compile("[\\w\\.\\-]+@[\\w\\.\\-]+");
    private static final int EVENT_DB_CORRUPT = 75004;
    private static final int EVENT_DB_OPERATION = 52000;
    static final String GET_LOCK_LOG_PREFIX = "GETLOCK:";
    private static final String KEY_ENCODING = "UTF-8";
    private static final int LOCK_ACQUIRED_WARNING_THREAD_TIME_IN_MS = 100;
    private static final int LOCK_ACQUIRED_WARNING_TIME_IN_MS = 300;
    private static final int LOCK_ACQUIRED_WARNING_TIME_IN_MS_ALWAYS_PRINT = 2000;
    private static final int LOCK_WARNING_WINDOW_IN_MS = 20000;
    private static final String LOG_SLOW_QUERIES_PROPERTY = "db.log.slow_query_threshold";
    public static final int MAX_SQL_CACHE_SIZE = 250;
    private static final int MAX_WARNINGS_ON_CACHESIZE_CONDITION = 1;
    public static final String MEMORY = ":memory:";
    public static final int NO_LOCALIZED_COLLATORS = 16;
    public static final int OPEN_READONLY = 1;
    public static final int OPEN_READWRITE = 0;
    private static final int OPEN_READ_MASK = 1;
    private static final int QUERY_LOG_SQL_LENGTH = 64;
    private static final int SLEEP_AFTER_YIELD_QUANTUM = 1000;
    public static final String SQLCIPHER_ANDROID_VERSION = "3.5.7";
    public static final int SQLITE_MAX_LIKE_PATTERN_LENGTH = 50000;
    private static final String TAG = "Database";
    private static WeakHashMap<SQLiteDatabase, Object> sActiveDatabases = new WeakHashMap<>();
    private static int sQueryLogTimeInMillis = 0;
    private int mCacheFullWarnings;
    Map<String, SQLiteCompiledSql> mCompiledQueries;
    private final DatabaseErrorHandler mErrorHandler;
    private CursorFactory mFactory;
    private int mFlags;
    private boolean mInnerTransactionIsSuccessful;
    private long mLastLockMessageTime;
    private String mLastSqlStatement;
    private final ReentrantLock mLock;
    private long mLockAcquiredThreadTime;
    private long mLockAcquiredWallTime;
    private boolean mLockingEnabled;
    private int mMaxSqlCacheSize;
    long mNativeHandle;
    private int mNumCacheHits;
    private int mNumCacheMisses;
    private String mPath;
    private String mPathForLogs;
    private WeakHashMap<SQLiteClosable, Object> mPrograms;
    private final int mSlowQueryThreshold;
    private Throwable mStackTrace;
    private final Map<String, SyncUpdateInfo> mSyncUpdateInfo;
    int mTempTableSequence;
    private String mTimeClosed;
    private String mTimeOpened;
    private boolean mTransactionIsSuccessful;
    private SQLiteTransactionListener mTransactionListener;

    public interface CursorFactory {
        Cursor newCursor(SQLiteDatabase sQLiteDatabase, SQLiteCursorDriver sQLiteCursorDriver, String str, SQLiteQuery sQLiteQuery);
    }

    public interface LibraryLoader {
        void loadLibraries(String... strArr);
    }

    private native void dbclose();

    private native void dbopen(String str, int i);

    private native void enableSqlProfiling(String str);

    private native void enableSqlTracing(String str);

    /* access modifiers changed from: private */
    public native void key(byte[] bArr) throws SQLException;

    /* access modifiers changed from: private */
    public native void key_mutf8(char[] cArr) throws SQLException;

    private native int native_getDbLookaside();

    private native void native_key(char[] cArr) throws SQLException;

    private native void native_rawExecSQL(String str);

    private native void native_rekey(String str) throws SQLException;

    private native int native_status(int i, boolean z);

    private native void rekey(byte[] bArr) throws SQLException;

    public static native int releaseMemory();

    public static native void setICURoot(String str);

    /* access modifiers changed from: package-private */
    public native int lastChangeCount();

    /* access modifiers changed from: package-private */
    public native long lastInsertRow();

    /* access modifiers changed from: package-private */
    public native void native_execSQL(String str) throws SQLException;

    /* access modifiers changed from: package-private */
    public native void native_setLocale(String str, int i);

    public int status(int operation, boolean reset) {
        return native_status(operation, reset);
    }

    public void changePassword(String password) throws SQLiteException {
        if (!isOpen()) {
            throw new SQLiteException("database not open");
        } else if (password != null) {
            byte[] keyMaterial = getBytes(password.toCharArray());
            rekey(keyMaterial);
            for (byte b : keyMaterial) {
            }
        }
    }

    public void changePassword(char[] password) throws SQLiteException {
        if (!isOpen()) {
            throw new SQLiteException("database not open");
        } else if (password != null) {
            byte[] keyMaterial = getBytes(password);
            rekey(keyMaterial);
            for (byte b : keyMaterial) {
            }
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:22:0x005a A[Catch:{ all -> 0x0063 }] */
    /* JADX WARNING: Removed duplicated region for block: B:27:0x0066 A[SYNTHETIC, Splitter:B:27:0x0066] */
    /* JADX WARNING: Removed duplicated region for block: B:30:0x006b A[Catch:{ IOException -> 0x0092 }] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static void loadICUData(android.content.Context r12, java.io.File r13) {
        /*
            r8 = 0
            r4 = 0
            java.io.File r3 = new java.io.File
            java.lang.String r10 = "icu"
            r3.<init>(r13, r10)
            java.io.File r2 = new java.io.File
            java.lang.String r10 = "icudt46l.dat"
            r2.<init>(r3, r10)
            boolean r10 = r3.exists()     // Catch:{ Exception -> 0x00a9 }
            if (r10 != 0) goto L_0x001b
            r3.mkdirs()     // Catch:{ Exception -> 0x00a9 }
        L_0x001b:
            boolean r10 = r2.exists()     // Catch:{ Exception -> 0x00a9 }
            if (r10 != 0) goto L_0x0074
            java.util.zip.ZipInputStream r5 = new java.util.zip.ZipInputStream     // Catch:{ Exception -> 0x00a9 }
            android.content.res.AssetManager r10 = r12.getAssets()     // Catch:{ Exception -> 0x00a9 }
            java.lang.String r11 = "icudt46l.zip"
            java.io.InputStream r10 = r10.open(r11)     // Catch:{ Exception -> 0x00a9 }
            r5.<init>(r10)     // Catch:{ Exception -> 0x00a9 }
            r5.getNextEntry()     // Catch:{ Exception -> 0x00ab, all -> 0x00a2 }
            java.io.FileOutputStream r9 = new java.io.FileOutputStream     // Catch:{ Exception -> 0x00ab, all -> 0x00a2 }
            r9.<init>(r2)     // Catch:{ Exception -> 0x00ab, all -> 0x00a2 }
            r10 = 1024(0x400, float:1.435E-42)
            byte[] r0 = new byte[r10]     // Catch:{ Exception -> 0x0048, all -> 0x00a5 }
        L_0x003d:
            int r7 = r5.read(r0)     // Catch:{ Exception -> 0x0048, all -> 0x00a5 }
            if (r7 <= 0) goto L_0x0072
            r10 = 0
            r9.write(r0, r10, r7)     // Catch:{ Exception -> 0x0048, all -> 0x00a5 }
            goto L_0x003d
        L_0x0048:
            r1 = move-exception
            r4 = r5
            r8 = r9
        L_0x004b:
            java.lang.String r10 = "Database"
            java.lang.String r11 = "Error copying icu dat file"
            android.util.Log.e(r10, r11, r1)     // Catch:{ all -> 0x0063 }
            boolean r10 = r2.exists()     // Catch:{ all -> 0x0063 }
            if (r10 == 0) goto L_0x005d
            r2.delete()     // Catch:{ all -> 0x0063 }
        L_0x005d:
            java.lang.RuntimeException r10 = new java.lang.RuntimeException     // Catch:{ all -> 0x0063 }
            r10.<init>(r1)     // Catch:{ all -> 0x0063 }
            throw r10     // Catch:{ all -> 0x0063 }
        L_0x0063:
            r10 = move-exception
        L_0x0064:
            if (r4 == 0) goto L_0x0069
            r4.close()     // Catch:{ IOException -> 0x0092 }
        L_0x0069:
            if (r8 == 0) goto L_0x0071
            r8.flush()     // Catch:{ IOException -> 0x0092 }
            r8.close()     // Catch:{ IOException -> 0x0092 }
        L_0x0071:
            throw r10
        L_0x0072:
            r4 = r5
            r8 = r9
        L_0x0074:
            if (r4 == 0) goto L_0x0079
            r4.close()     // Catch:{ IOException -> 0x0082 }
        L_0x0079:
            if (r8 == 0) goto L_0x0081
            r8.flush()     // Catch:{ IOException -> 0x0082 }
            r8.close()     // Catch:{ IOException -> 0x0082 }
        L_0x0081:
            return
        L_0x0082:
            r6 = move-exception
            java.lang.String r10 = "Database"
            java.lang.String r11 = "Error in closing streams IO streams after expanding ICU dat file"
            android.util.Log.e(r10, r11, r6)
            java.lang.RuntimeException r10 = new java.lang.RuntimeException
            r10.<init>(r6)
            throw r10
        L_0x0092:
            r6 = move-exception
            java.lang.String r10 = "Database"
            java.lang.String r11 = "Error in closing streams IO streams after expanding ICU dat file"
            android.util.Log.e(r10, r11, r6)
            java.lang.RuntimeException r10 = new java.lang.RuntimeException
            r10.<init>(r6)
            throw r10
        L_0x00a2:
            r10 = move-exception
            r4 = r5
            goto L_0x0064
        L_0x00a5:
            r10 = move-exception
            r4 = r5
            r8 = r9
            goto L_0x0064
        L_0x00a9:
            r1 = move-exception
            goto L_0x004b
        L_0x00ab:
            r1 = move-exception
            r4 = r5
            goto L_0x004b
        */
        throw new UnsupportedOperationException("Method not decompiled: dji.thirdparty.ciphersql.database.SQLiteDatabase.loadICUData(android.content.Context, java.io.File):void");
    }

    public static synchronized void loadLibs(Context context) {
        synchronized (SQLiteDatabase.class) {
            loadLibs(context, context.getFilesDir());
        }
    }

    public static synchronized void loadLibs(Context context, File workingDir) {
        synchronized (SQLiteDatabase.class) {
            loadLibs(context, workingDir, new LibraryLoader() {
                /* class dji.thirdparty.ciphersql.database.SQLiteDatabase.AnonymousClass1 */

                public void loadLibraries(String... libNames) {
                    for (String libName : libNames) {
                        System.loadLibrary(libName);
                    }
                }
            });
        }
    }

    public static synchronized void loadLibs(Context context, LibraryLoader libraryLoader) {
        synchronized (SQLiteDatabase.class) {
            loadLibs(context, context.getFilesDir(), libraryLoader);
        }
    }

    public static synchronized void loadLibs(Context context, File workingDir, LibraryLoader libraryLoader) {
        synchronized (SQLiteDatabase.class) {
            libraryLoader.loadLibraries("djisqlcipher");
        }
    }

    /* access modifiers changed from: package-private */
    public void addSQLiteClosable(SQLiteClosable closable) {
        lock();
        try {
            this.mPrograms.put(closable, null);
        } finally {
            unlock();
        }
    }

    /* access modifiers changed from: package-private */
    public void removeSQLiteClosable(SQLiteClosable closable) {
        lock();
        try {
            this.mPrograms.remove(closable);
        } finally {
            unlock();
        }
    }

    /* access modifiers changed from: protected */
    public void onAllReferencesReleased() {
        if (isOpen()) {
            if (SQLiteDebug.DEBUG_SQL_CACHE) {
                this.mTimeClosed = getTime();
            }
            dbclose();
            synchronized (sActiveDatabases) {
                sActiveDatabases.remove(this);
            }
        }
    }

    public void setLockingEnabled(boolean lockingEnabled) {
        this.mLockingEnabled = lockingEnabled;
    }

    /* access modifiers changed from: package-private */
    public void onCorruption() {
        Log.e(TAG, "Calling error handler for corrupt database (detected) " + this.mPath);
        this.mErrorHandler.onCorruption(this);
    }

    /* access modifiers changed from: package-private */
    public void lock() {
        if (this.mLockingEnabled) {
            this.mLock.lock();
            if (SQLiteDebug.DEBUG_LOCK_TIME_TRACKING && this.mLock.getHoldCount() == 1) {
                this.mLockAcquiredWallTime = SystemClock.elapsedRealtime();
                this.mLockAcquiredThreadTime = Debug.threadCpuTimeNanos();
            }
        }
    }

    private void lockForced() {
        this.mLock.lock();
        if (SQLiteDebug.DEBUG_LOCK_TIME_TRACKING && this.mLock.getHoldCount() == 1) {
            this.mLockAcquiredWallTime = SystemClock.elapsedRealtime();
            this.mLockAcquiredThreadTime = Debug.threadCpuTimeNanos();
        }
    }

    /* access modifiers changed from: package-private */
    public void unlock() {
        if (this.mLockingEnabled) {
            if (SQLiteDebug.DEBUG_LOCK_TIME_TRACKING && this.mLock.getHoldCount() == 1) {
                checkLockHoldTime();
            }
            this.mLock.unlock();
        }
    }

    private void unlockForced() {
        if (SQLiteDebug.DEBUG_LOCK_TIME_TRACKING && this.mLock.getHoldCount() == 1) {
            checkLockHoldTime();
        }
        this.mLock.unlock();
    }

    private void checkLockHoldTime() {
        long elapsedTime = SystemClock.elapsedRealtime();
        long lockedTime = elapsedTime - this.mLockAcquiredWallTime;
        if ((lockedTime >= 2000 || Log.isLoggable(TAG, 2) || elapsedTime - this.mLastLockMessageTime >= 20000) && lockedTime > 300) {
            int threadTime = (int) ((Debug.threadCpuTimeNanos() - this.mLockAcquiredThreadTime) / Clock.MS_TO_NS);
            if (threadTime > 100 || lockedTime > 2000) {
                this.mLastLockMessageTime = elapsedTime;
                String msg = "lock held on " + this.mPath + " for " + lockedTime + "ms. Thread time was " + threadTime + "ms";
                if (SQLiteDebug.DEBUG_LOCK_TIME_TRACKING_STACK_TRACE) {
                    Log.d(TAG, msg, new Exception());
                } else {
                    Log.d(TAG, msg);
                }
            }
        }
    }

    public void beginTransaction() {
        beginTransactionWithListener(null);
    }

    public void beginTransactionWithListener(SQLiteTransactionListener transactionListener) {
        lockForced();
        if (!isOpen()) {
            throw new IllegalStateException("database not open");
        }
        try {
            if (this.mLock.getHoldCount() <= 1) {
                execSQL("BEGIN EXCLUSIVE;");
                this.mTransactionListener = transactionListener;
                this.mTransactionIsSuccessful = true;
                this.mInnerTransactionIsSuccessful = false;
                if (transactionListener != null) {
                    transactionListener.onBegin();
                }
                if (1 == 0) {
                    unlockForced();
                }
            } else if (this.mInnerTransactionIsSuccessful) {
                IllegalStateException e = new IllegalStateException("Cannot call beginTransaction between calling setTransactionSuccessful and endTransaction");
                Log.e(TAG, "beginTransaction() failed", e);
                throw e;
            } else if (1 == 0) {
                unlockForced();
            }
        } catch (RuntimeException e2) {
            execSQL("ROLLBACK;");
            throw e2;
        } catch (Throwable th) {
            if (0 == 0) {
                unlockForced();
            }
            throw th;
        }
    }

    public void endTransaction() {
        RuntimeException savedException;
        if (!isOpen()) {
            throw new IllegalStateException("database not open");
        } else if (!this.mLock.isHeldByCurrentThread()) {
            throw new IllegalStateException("no transaction pending");
        } else {
            try {
                if (this.mInnerTransactionIsSuccessful) {
                    this.mInnerTransactionIsSuccessful = false;
                } else {
                    this.mTransactionIsSuccessful = false;
                }
                if (this.mLock.getHoldCount() != 1) {
                    this.mTransactionListener = null;
                    unlockForced();
                    return;
                }
                savedException = null;
                if (this.mTransactionListener != null) {
                    if (this.mTransactionIsSuccessful) {
                        this.mTransactionListener.onCommit();
                    } else {
                        this.mTransactionListener.onRollback();
                    }
                }
                if (this.mTransactionIsSuccessful) {
                    execSQL(COMMIT_SQL);
                } else {
                    try {
                        execSQL("ROLLBACK;");
                        if (savedException != null) {
                            throw savedException;
                        }
                    } catch (SQLException e) {
                        Log.d(TAG, "exception during rollback, maybe the DB previously performed an auto-rollback");
                    }
                }
                this.mTransactionListener = null;
                unlockForced();
            } catch (RuntimeException e2) {
                savedException = e2;
                this.mTransactionIsSuccessful = false;
            } catch (Throwable th) {
                this.mTransactionListener = null;
                unlockForced();
                throw th;
            }
        }
    }

    public void setTransactionSuccessful() {
        if (!isOpen()) {
            throw new IllegalStateException("database not open");
        } else if (!this.mLock.isHeldByCurrentThread()) {
            throw new IllegalStateException("no transaction pending");
        } else if (this.mInnerTransactionIsSuccessful) {
            throw new IllegalStateException("setTransactionSuccessful may only be called once per call to beginTransaction");
        } else {
            this.mInnerTransactionIsSuccessful = true;
        }
    }

    public boolean inTransaction() {
        return this.mLock.getHoldCount() > 0;
    }

    public boolean isDbLockedByCurrentThread() {
        return this.mLock.isHeldByCurrentThread();
    }

    public boolean isDbLockedByOtherThreads() {
        return !this.mLock.isHeldByCurrentThread() && this.mLock.isLocked();
    }

    @Deprecated
    public boolean yieldIfContended() {
        if (!isOpen()) {
            return false;
        }
        return yieldIfContendedHelper(false, -1);
    }

    public boolean yieldIfContendedSafely() {
        if (!isOpen()) {
            return false;
        }
        return yieldIfContendedHelper(true, -1);
    }

    public boolean yieldIfContendedSafely(long sleepAfterYieldDelay) {
        if (!isOpen()) {
            return false;
        }
        return yieldIfContendedHelper(true, sleepAfterYieldDelay);
    }

    private boolean yieldIfContendedHelper(boolean checkFullyYielded, long sleepAfterYieldDelay) {
        long j;
        if (this.mLock.getQueueLength() == 0) {
            this.mLockAcquiredWallTime = SystemClock.elapsedRealtime();
            this.mLockAcquiredThreadTime = Debug.threadCpuTimeNanos();
            return false;
        }
        setTransactionSuccessful();
        SQLiteTransactionListener transactionListener = this.mTransactionListener;
        endTransaction();
        if (!checkFullyYielded || !isDbLockedByCurrentThread()) {
            if (sleepAfterYieldDelay > 0) {
                long remainingDelay = sleepAfterYieldDelay;
                while (remainingDelay > 0) {
                    if (remainingDelay < 1000) {
                        j = remainingDelay;
                    } else {
                        j = 1000;
                    }
                    try {
                        Thread.sleep(j);
                    } catch (InterruptedException e) {
                        Thread.interrupted();
                    }
                    remainingDelay -= 1000;
                    if (this.mLock.getQueueLength() == 0) {
                        break;
                    }
                }
            }
            beginTransactionWithListener(transactionListener);
            return true;
        }
        throw new IllegalStateException("Db locked more than once. yielfIfContended cannot yield");
    }

    public Map<String, String> getSyncedTables() {
        HashMap<String, String> tables;
        synchronized (this.mSyncUpdateInfo) {
            tables = new HashMap<>();
            for (String table : this.mSyncUpdateInfo.keySet()) {
                SyncUpdateInfo info = this.mSyncUpdateInfo.get(table);
                if (info.deletedTable != null) {
                    tables.put(table, info.deletedTable);
                }
            }
        }
        return tables;
    }

    private static class SyncUpdateInfo {
        String deletedTable;
        String foreignKey;
        String masterTable;

        SyncUpdateInfo(String masterTable2, String deletedTable2, String foreignKey2) {
            this.masterTable = masterTable2;
            this.deletedTable = deletedTable2;
            this.foreignKey = foreignKey2;
        }
    }

    public static SQLiteDatabase openDatabase(String path, String password, CursorFactory factory, int flags) {
        return openDatabase(path, password, factory, flags, (SQLiteDatabaseHook) null);
    }

    public static SQLiteDatabase openDatabase(String path, char[] password, CursorFactory factory, int flags) {
        return openDatabase(path, password, factory, flags, (SQLiteDatabaseHook) null, (DatabaseErrorHandler) null);
    }

    public static SQLiteDatabase openDatabase(String path, String password, CursorFactory factory, int flags, SQLiteDatabaseHook hook) {
        return openDatabase(path, password, factory, flags, hook, (DatabaseErrorHandler) null);
    }

    public static SQLiteDatabase openDatabase(String path, char[] password, CursorFactory factory, int flags, SQLiteDatabaseHook hook) {
        return openDatabase(path, password, factory, flags, hook, (DatabaseErrorHandler) null);
    }

    public static SQLiteDatabase openDatabase(String path, String password, CursorFactory factory, int flags, SQLiteDatabaseHook hook, DatabaseErrorHandler errorHandler) {
        return openDatabase(path, password == null ? null : password.toCharArray(), factory, flags, hook, errorHandler);
    }

    /* JADX WARNING: Removed duplicated region for block: B:10:0x0011  */
    /* JADX WARNING: Removed duplicated region for block: B:13:0x0018  */
    /* JADX WARNING: Removed duplicated region for block: B:16:0x001e A[SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static dji.thirdparty.ciphersql.database.SQLiteDatabase openDatabase(java.lang.String r7, char[] r8, dji.thirdparty.ciphersql.database.SQLiteDatabase.CursorFactory r9, int r10, dji.thirdparty.ciphersql.database.SQLiteDatabaseHook r11, dji.thirdparty.ciphersql.DatabaseErrorHandler r12) {
        /*
            r2 = 0
            if (r12 == 0) goto L_0x0026
            r1 = r12
        L_0x0004:
            dji.thirdparty.ciphersql.database.SQLiteDatabase r3 = new dji.thirdparty.ciphersql.database.SQLiteDatabase     // Catch:{ SQLiteDatabaseCorruptException -> 0x002c }
            r3.<init>(r7, r9, r10, r1)     // Catch:{ SQLiteDatabaseCorruptException -> 0x002c }
            r3.openDatabaseInternal(r8, r11)     // Catch:{ SQLiteDatabaseCorruptException -> 0x0056 }
            r2 = r3
        L_0x000d:
            boolean r4 = dji.thirdparty.ciphersql.database.SQLiteDebug.DEBUG_SQL_STATEMENTS
            if (r4 == 0) goto L_0x0014
            r2.enableSqlTracing(r7)
        L_0x0014:
            boolean r4 = dji.thirdparty.ciphersql.database.SQLiteDebug.DEBUG_SQL_TIME
            if (r4 == 0) goto L_0x001b
            r2.enableSqlProfiling(r7)
        L_0x001b:
            java.util.WeakHashMap<dji.thirdparty.ciphersql.database.SQLiteDatabase, java.lang.Object> r5 = dji.thirdparty.ciphersql.database.SQLiteDatabase.sActiveDatabases
            monitor-enter(r5)
            java.util.WeakHashMap<dji.thirdparty.ciphersql.database.SQLiteDatabase, java.lang.Object> r4 = dji.thirdparty.ciphersql.database.SQLiteDatabase.sActiveDatabases     // Catch:{ all -> 0x0053 }
            r6 = 0
            r4.put(r2, r6)     // Catch:{ all -> 0x0053 }
            monitor-exit(r5)     // Catch:{ all -> 0x0053 }
            return r2
        L_0x0026:
            dji.thirdparty.ciphersql.DefaultDatabaseErrorHandler r1 = new dji.thirdparty.ciphersql.DefaultDatabaseErrorHandler
            r1.<init>()
            goto L_0x0004
        L_0x002c:
            r0 = move-exception
        L_0x002d:
            java.lang.String r4 = "Database"
            java.lang.StringBuilder r5 = new java.lang.StringBuilder
            r5.<init>()
            java.lang.String r6 = "Calling error handler for corrupt database "
            java.lang.StringBuilder r5 = r5.append(r6)
            java.lang.StringBuilder r5 = r5.append(r7)
            java.lang.String r5 = r5.toString()
            android.util.Log.e(r4, r5, r0)
            r1.onCorruption(r2)
            dji.thirdparty.ciphersql.database.SQLiteDatabase r2 = new dji.thirdparty.ciphersql.database.SQLiteDatabase
            r2.<init>(r7, r9, r10, r1)
            r2.openDatabaseInternal(r8, r11)
            goto L_0x000d
        L_0x0053:
            r4 = move-exception
            monitor-exit(r5)     // Catch:{ all -> 0x0053 }
            throw r4
        L_0x0056:
            r0 = move-exception
            r2 = r3
            goto L_0x002d
        */
        throw new UnsupportedOperationException("Method not decompiled: dji.thirdparty.ciphersql.database.SQLiteDatabase.openDatabase(java.lang.String, char[], dji.thirdparty.ciphersql.database.SQLiteDatabase$CursorFactory, int, dji.thirdparty.ciphersql.database.SQLiteDatabaseHook, dji.thirdparty.ciphersql.DatabaseErrorHandler):dji.thirdparty.ciphersql.database.SQLiteDatabase");
    }

    public static SQLiteDatabase openOrCreateDatabase(File file, String password, CursorFactory factory, SQLiteDatabaseHook databaseHook) {
        return openOrCreateDatabase(file, password, factory, databaseHook, (DatabaseErrorHandler) null);
    }

    public static SQLiteDatabase openOrCreateDatabase(String path, String password, CursorFactory factory, SQLiteDatabaseHook databaseHook) {
        return openDatabase(path, password, factory, (int) CREATE_IF_NECESSARY, databaseHook);
    }

    public static SQLiteDatabase openOrCreateDatabase(File file, String password, CursorFactory factory, SQLiteDatabaseHook databaseHook, DatabaseErrorHandler errorHandler) {
        return openOrCreateDatabase(file == null ? null : file.getPath(), password, factory, databaseHook, errorHandler);
    }

    public static SQLiteDatabase openOrCreateDatabase(String path, String password, CursorFactory factory, SQLiteDatabaseHook databaseHook, DatabaseErrorHandler errorHandler) {
        return openDatabase(path, password == null ? null : password.toCharArray(), factory, (int) CREATE_IF_NECESSARY, databaseHook, errorHandler);
    }

    public static SQLiteDatabase openOrCreateDatabase(String path, char[] password, CursorFactory factory, SQLiteDatabaseHook databaseHook) {
        return openDatabase(path, password, factory, (int) CREATE_IF_NECESSARY, databaseHook);
    }

    public static SQLiteDatabase openOrCreateDatabase(String path, char[] password, CursorFactory factory, SQLiteDatabaseHook databaseHook, DatabaseErrorHandler errorHandler) {
        return openDatabase(path, password, factory, (int) CREATE_IF_NECESSARY, databaseHook, errorHandler);
    }

    public static SQLiteDatabase openOrCreateDatabase(File file, String password, CursorFactory factory) {
        return openOrCreateDatabase(file, password, factory, (SQLiteDatabaseHook) null);
    }

    public static SQLiteDatabase openOrCreateDatabase(String path, String password, CursorFactory factory) {
        return openDatabase(path, password, factory, (int) CREATE_IF_NECESSARY, (SQLiteDatabaseHook) null);
    }

    public static SQLiteDatabase openOrCreateDatabase(String path, char[] password, CursorFactory factory) {
        return openDatabase(path, password, factory, (int) CREATE_IF_NECESSARY, (SQLiteDatabaseHook) null);
    }

    public static SQLiteDatabase create(CursorFactory factory, String password) {
        return openDatabase(MEMORY, password == null ? null : password.toCharArray(), factory, (int) CREATE_IF_NECESSARY);
    }

    public static SQLiteDatabase create(CursorFactory factory, char[] password) {
        return openDatabase(MEMORY, password, factory, (int) CREATE_IF_NECESSARY);
    }

    public void close() {
        if (isOpen()) {
            lock();
            try {
                closeClosable();
                onAllReferencesReleased();
            } finally {
                unlock();
            }
        }
    }

    private void closeClosable() {
        deallocCachedSqlStatements();
        for (Map.Entry<SQLiteClosable, Object> entry : this.mPrograms.entrySet()) {
            SQLiteClosable program = (SQLiteClosable) entry.getKey();
            if (program != null) {
                program.onAllReferencesReleasedFromContainer();
            }
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:15:0x002c  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public int getVersion() {
        /*
            r6 = this;
            r0 = 0
            r6.lock()
            boolean r4 = r6.isOpen()
            if (r4 != 0) goto L_0x0013
            java.lang.IllegalStateException r4 = new java.lang.IllegalStateException
            java.lang.String r5 = "database not open"
            r4.<init>(r5)
            throw r4
        L_0x0013:
            dji.thirdparty.ciphersql.database.SQLiteStatement r1 = new dji.thirdparty.ciphersql.database.SQLiteStatement     // Catch:{ all -> 0x0029 }
            java.lang.String r4 = "PRAGMA user_version;"
            r1.<init>(r6, r4)     // Catch:{ all -> 0x0029 }
            long r2 = r1.simpleQueryForLong()     // Catch:{ all -> 0x0033 }
            int r4 = (int) r2
            if (r1 == 0) goto L_0x0025
            r1.close()
        L_0x0025:
            r6.unlock()
            return r4
        L_0x0029:
            r4 = move-exception
        L_0x002a:
            if (r0 == 0) goto L_0x002f
            r0.close()
        L_0x002f:
            r6.unlock()
            throw r4
        L_0x0033:
            r4 = move-exception
            r0 = r1
            goto L_0x002a
        */
        throw new UnsupportedOperationException("Method not decompiled: dji.thirdparty.ciphersql.database.SQLiteDatabase.getVersion():int");
    }

    public void setVersion(int version) {
        execSQL("PRAGMA user_version = " + version);
    }

    /* JADX WARNING: Removed duplicated region for block: B:15:0x0030  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public long getMaximumSize() {
        /*
            r6 = this;
            r2 = 0
            r6.lock()
            boolean r4 = r6.isOpen()
            if (r4 != 0) goto L_0x0013
            java.lang.IllegalStateException r4 = new java.lang.IllegalStateException
            java.lang.String r5 = "database not open"
            r4.<init>(r5)
            throw r4
        L_0x0013:
            dji.thirdparty.ciphersql.database.SQLiteStatement r3 = new dji.thirdparty.ciphersql.database.SQLiteStatement     // Catch:{ all -> 0x002d }
            java.lang.String r4 = "PRAGMA max_page_count;"
            r3.<init>(r6, r4)     // Catch:{ all -> 0x002d }
            long r0 = r3.simpleQueryForLong()     // Catch:{ all -> 0x0037 }
            long r4 = r6.getPageSize()     // Catch:{ all -> 0x0037 }
            long r4 = r4 * r0
            if (r3 == 0) goto L_0x0029
            r3.close()
        L_0x0029:
            r6.unlock()
            return r4
        L_0x002d:
            r4 = move-exception
        L_0x002e:
            if (r2 == 0) goto L_0x0033
            r2.close()
        L_0x0033:
            r6.unlock()
            throw r4
        L_0x0037:
            r4 = move-exception
            r2 = r3
            goto L_0x002e
        */
        throw new UnsupportedOperationException("Method not decompiled: dji.thirdparty.ciphersql.database.SQLiteDatabase.getMaximumSize():long");
    }

    /* JADX WARNING: Removed duplicated region for block: B:18:0x004f  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public long setMaximumSize(long r14) {
        /*
            r13 = this;
            r6 = 0
            r13.lock()
            boolean r8 = r13.isOpen()
            if (r8 != 0) goto L_0x0013
            java.lang.IllegalStateException r8 = new java.lang.IllegalStateException
            java.lang.String r9 = "database not open"
            r8.<init>(r9)
            throw r8
        L_0x0013:
            long r4 = r13.getPageSize()     // Catch:{ all -> 0x004c }
            long r2 = r14 / r4
            long r8 = r14 % r4
            r10 = 0
            int r8 = (r8 > r10 ? 1 : (r8 == r10 ? 0 : -1))
            if (r8 == 0) goto L_0x0024
            r8 = 1
            long r2 = r2 + r8
        L_0x0024:
            dji.thirdparty.ciphersql.database.SQLiteStatement r7 = new dji.thirdparty.ciphersql.database.SQLiteStatement     // Catch:{ all -> 0x004c }
            java.lang.StringBuilder r8 = new java.lang.StringBuilder     // Catch:{ all -> 0x004c }
            r8.<init>()     // Catch:{ all -> 0x004c }
            java.lang.String r9 = "PRAGMA max_page_count = "
            java.lang.StringBuilder r8 = r8.append(r9)     // Catch:{ all -> 0x004c }
            java.lang.StringBuilder r8 = r8.append(r2)     // Catch:{ all -> 0x004c }
            java.lang.String r8 = r8.toString()     // Catch:{ all -> 0x004c }
            r7.<init>(r13, r8)     // Catch:{ all -> 0x004c }
            long r0 = r7.simpleQueryForLong()     // Catch:{ all -> 0x0056 }
            long r8 = r0 * r4
            if (r7 == 0) goto L_0x0048
            r7.close()
        L_0x0048:
            r13.unlock()
            return r8
        L_0x004c:
            r8 = move-exception
        L_0x004d:
            if (r6 == 0) goto L_0x0052
            r6.close()
        L_0x0052:
            r13.unlock()
            throw r8
        L_0x0056:
            r8 = move-exception
            r6 = r7
            goto L_0x004d
        */
        throw new UnsupportedOperationException("Method not decompiled: dji.thirdparty.ciphersql.database.SQLiteDatabase.setMaximumSize(long):long");
    }

    public long getPageSize() {
        SQLiteStatement prog = null;
        lock();
        if (!isOpen()) {
            throw new IllegalStateException("database not open");
        }
        try {
            SQLiteStatement prog2 = new SQLiteStatement(this, "PRAGMA page_size;");
            try {
                long size = prog2.simpleQueryForLong();
                if (prog2 != null) {
                    prog2.close();
                }
                unlock();
                return size;
            } catch (Throwable th) {
                th = th;
                prog = prog2;
            }
        } catch (Throwable th2) {
            th = th2;
            if (prog != null) {
                prog.close();
            }
            unlock();
            throw th;
        }
    }

    public void setPageSize(long numBytes) {
        execSQL("PRAGMA page_size = " + numBytes);
    }

    public void markTableSyncable(String table, String deletedTable) {
        if (!isOpen()) {
            throw new SQLiteException("database not open");
        }
        markTableSyncable(table, "_id", table, deletedTable);
    }

    public void markTableSyncable(String table, String foreignKey, String updateTable) {
        if (!isOpen()) {
            throw new SQLiteException("database not open");
        }
        markTableSyncable(table, foreignKey, updateTable, null);
    }

    /* JADX INFO: finally extract failed */
    private void markTableSyncable(String table, String foreignKey, String updateTable, String deletedTable) {
        lock();
        try {
            native_execSQL("SELECT _sync_dirty FROM " + updateTable + " LIMIT 0");
            native_execSQL("SELECT " + foreignKey + " FROM " + table + " LIMIT 0");
            unlock();
            SyncUpdateInfo info = new SyncUpdateInfo(updateTable, deletedTable, foreignKey);
            synchronized (this.mSyncUpdateInfo) {
                this.mSyncUpdateInfo.put(table, info);
            }
        } catch (Throwable th) {
            unlock();
            throw th;
        }
    }

    /* access modifiers changed from: package-private */
    public void rowUpdated(String table, long rowId) {
        SyncUpdateInfo info;
        synchronized (this.mSyncUpdateInfo) {
            info = this.mSyncUpdateInfo.get(table);
        }
        if (info != null) {
            execSQL("UPDATE " + info.masterTable + " SET _sync_dirty=1 WHERE _id=(SELECT " + info.foreignKey + " FROM " + table + " WHERE _id=" + rowId + ")");
        }
    }

    public static String findEditTable(String tables) {
        if (!TextUtils.isEmpty(tables)) {
            int spacepos = tables.indexOf(32);
            int commapos = tables.indexOf(44);
            if (spacepos > 0 && (spacepos < commapos || commapos < 0)) {
                return tables.substring(0, spacepos);
            }
            if (commapos <= 0) {
                return tables;
            }
            if (commapos < spacepos || spacepos < 0) {
                return tables.substring(0, commapos);
            }
            return tables;
        }
        throw new IllegalStateException("Invalid tables");
    }

    public SQLiteStatement compileStatement(String sql) throws SQLException {
        lock();
        if (!isOpen()) {
            throw new IllegalStateException("database not open");
        }
        try {
            return new SQLiteStatement(this, sql);
        } finally {
            unlock();
        }
    }

    public Cursor query(boolean distinct, String table, String[] columns, String selection, String[] selectionArgs, String groupBy, String having, String orderBy, String limit) {
        return queryWithFactory(null, distinct, table, columns, selection, selectionArgs, groupBy, having, orderBy, limit);
    }

    public Cursor queryWithFactory(CursorFactory cursorFactory, boolean distinct, String table, String[] columns, String selection, String[] selectionArgs, String groupBy, String having, String orderBy, String limit) {
        if (isOpen()) {
            return rawQueryWithFactory(cursorFactory, SQLiteQueryBuilder.buildQueryString(distinct, table, columns, selection, groupBy, having, orderBy, limit), selectionArgs, findEditTable(table));
        }
        throw new IllegalStateException("database not open");
    }

    public Cursor query(String table, String[] columns, String selection, String[] selectionArgs, String groupBy, String having, String orderBy) {
        return query(false, table, columns, selection, selectionArgs, groupBy, having, orderBy, null);
    }

    public Cursor query(String table, String[] columns, String selection, String[] selectionArgs, String groupBy, String having, String orderBy, String limit) {
        return query(false, table, columns, selection, selectionArgs, groupBy, having, orderBy, limit);
    }

    public Cursor rawQuery(String sql, String[] selectionArgs) {
        return rawQueryWithFactory(null, sql, selectionArgs, null);
    }

    public Cursor rawQuery(String sql, Object[] args) {
        if (!isOpen()) {
            throw new IllegalStateException("database not open");
        }
        long timeStart = 0;
        if (this.mSlowQueryThreshold != -1) {
            timeStart = System.currentTimeMillis();
        }
        SQLiteDirectCursorDriver driver = new SQLiteDirectCursorDriver(this, sql, null);
        Cursor cursor = null;
        try {
            cursor = driver.query(this.mFactory, args);
            return new CrossProcessCursorWrapper(cursor);
        } finally {
            if (this.mSlowQueryThreshold != -1) {
                int count = -1;
                if (cursor != null) {
                    count = cursor.getCount();
                }
                long duration = System.currentTimeMillis() - timeStart;
                if (duration >= ((long) this.mSlowQueryThreshold)) {
                    Log.v(TAG, "query (" + duration + " ms): " + driver.toString() + ", args are <redacted>, count is " + count);
                }
            }
        }
    }

    public Cursor rawQueryWithFactory(CursorFactory cursorFactory, String sql, String[] selectionArgs, String editTable) {
        if (!isOpen()) {
            throw new IllegalStateException("database not open");
        }
        long timeStart = 0;
        if (this.mSlowQueryThreshold != -1) {
            timeStart = System.currentTimeMillis();
        }
        SQLiteCursorDriver driver = new SQLiteDirectCursorDriver(this, sql, editTable);
        Cursor cursor = null;
        if (cursorFactory == null) {
            cursorFactory = this.mFactory;
        }
        try {
            cursor = driver.query(cursorFactory, selectionArgs);
            return new CrossProcessCursorWrapper(cursor);
        } finally {
            if (this.mSlowQueryThreshold != -1) {
                int count = -1;
                if (cursor != null) {
                    count = cursor.getCount();
                }
                long duration = System.currentTimeMillis() - timeStart;
                if (duration >= ((long) this.mSlowQueryThreshold)) {
                    Log.v(TAG, "query (" + duration + " ms): " + driver.toString() + ", args are <redacted>, count is " + count);
                }
            }
        }
    }

    public Cursor rawQuery(String sql, String[] selectionArgs, int initialRead, int maxRead) {
        CursorWrapper cursorWrapper = (CursorWrapper) rawQueryWithFactory(null, sql, selectionArgs, null);
        ((SQLiteCursor) cursorWrapper.getWrappedCursor()).setLoadStyle(initialRead, maxRead);
        return cursorWrapper;
    }

    public long insert(String table, String nullColumnHack, ContentValues values) {
        try {
            return insertWithOnConflict(table, nullColumnHack, values, 0);
        } catch (SQLException e) {
            Log.e(TAG, "Error inserting <redacted values> into " + table, e);
            return -1;
        }
    }

    public long insertOrThrow(String table, String nullColumnHack, ContentValues values) throws SQLException {
        return insertWithOnConflict(table, nullColumnHack, values, 0);
    }

    public long replace(String table, String nullColumnHack, ContentValues initialValues) {
        try {
            return insertWithOnConflict(table, nullColumnHack, initialValues, 5);
        } catch (SQLException e) {
            Log.e(TAG, "Error inserting <redacted values> into " + table, e);
            return -1;
        }
    }

    public long replaceOrThrow(String table, String nullColumnHack, ContentValues initialValues) throws SQLException {
        return insertWithOnConflict(table, nullColumnHack, initialValues, 5);
    }

    public long insertWithOnConflict(String table, String nullColumnHack, ContentValues initialValues, int conflictAlgorithm) {
        if (!isOpen()) {
            throw new IllegalStateException("database not open");
        }
        StringBuilder sql = new StringBuilder(152);
        sql.append("INSERT");
        sql.append(CONFLICT_VALUES[conflictAlgorithm]);
        sql.append(" INTO ");
        sql.append(table);
        StringBuilder values = new StringBuilder(40);
        Set<Map.Entry<String, Object>> entrySet = null;
        if (initialValues == null || initialValues.size() <= 0) {
            sql.append("(" + nullColumnHack + ") ");
            values.append("NULL");
        } else {
            entrySet = initialValues.valueSet();
            sql.append('(');
            boolean needSeparator = false;
            for (Map.Entry<String, Object> entry : entrySet) {
                if (needSeparator) {
                    sql.append(", ");
                    values.append(", ");
                }
                needSeparator = true;
                sql.append((String) entry.getKey());
                values.append('?');
            }
            sql.append(')');
        }
        sql.append(" VALUES(");
        sql.append((CharSequence) values);
        sql.append(");");
        lock();
        SQLiteStatement statement = null;
        try {
            statement = compileStatement(sql.toString());
            if (entrySet != null) {
                int size = entrySet.size();
                Iterator<Map.Entry<String, Object>> entriesIter = entrySet.iterator();
                for (int i = 0; i < size; i++) {
                    DatabaseUtils.bindObjectToProgram(statement, i + 1, entriesIter.next().getValue());
                }
            }
            statement.execute();
            long insertedRowId = lastChangeCount() > 0 ? lastInsertRow() : -1;
            if (insertedRowId == -1) {
                Log.e(TAG, "Error inserting <redacted values> using <redacted sql> into " + table);
            } else if (Log.isLoggable(TAG, 2)) {
                Log.v(TAG, "Inserting row " + insertedRowId + " from <redacted values> using <redacted sql> into " + table);
            }
            if (statement != null) {
                statement.close();
            }
            unlock();
            return insertedRowId;
        } catch (SQLiteDatabaseCorruptException e) {
            onCorruption();
            throw e;
        } catch (Throwable th) {
            if (statement != null) {
                statement.close();
            }
            unlock();
            throw th;
        }
    }

    public int delete(String table, String whereClause, String[] whereArgs) {
        lock();
        if (!isOpen()) {
            throw new IllegalStateException("database not open");
        }
        SQLiteStatement statement = null;
        try {
            SQLiteStatement statement2 = compileStatement("DELETE FROM " + table + (!TextUtils.isEmpty(whereClause) ? " WHERE " + whereClause : ""));
            if (whereArgs != null) {
                int numArgs = whereArgs.length;
                for (int i = 0; i < numArgs; i++) {
                    DatabaseUtils.bindObjectToProgram(statement2, i + 1, whereArgs[i]);
                }
            }
            statement2.execute();
            int lastChangeCount = lastChangeCount();
            if (statement2 != null) {
                statement2.close();
            }
            unlock();
            return lastChangeCount;
        } catch (SQLiteDatabaseCorruptException e) {
            onCorruption();
            throw e;
        } catch (Throwable th) {
            if (statement != null) {
                statement.close();
            }
            unlock();
            throw th;
        }
    }

    public int update(String table, ContentValues values, String whereClause, String[] whereArgs) {
        return updateWithOnConflict(table, values, whereClause, whereArgs, 0);
    }

    public int updateWithOnConflict(String table, ContentValues values, String whereClause, String[] whereArgs, int conflictAlgorithm) {
        if (values == null || values.size() == 0) {
            throw new IllegalArgumentException("Empty values");
        }
        StringBuilder sql = new StringBuilder(120);
        sql.append("UPDATE ");
        sql.append(CONFLICT_VALUES[conflictAlgorithm]);
        sql.append(table);
        sql.append(" SET ");
        Set<Map.Entry<String, Object>> entrySet = values.valueSet();
        Iterator<Map.Entry<String, Object>> entriesIter = entrySet.iterator();
        while (entriesIter.hasNext()) {
            sql.append((String) entriesIter.next().getKey());
            sql.append("=?");
            if (entriesIter.hasNext()) {
                sql.append(", ");
            }
        }
        if (!TextUtils.isEmpty(whereClause)) {
            sql.append(" WHERE ");
            sql.append(whereClause);
        }
        lock();
        if (!isOpen()) {
            throw new IllegalStateException("database not open");
        }
        SQLiteStatement statement = null;
        try {
            SQLiteStatement statement2 = compileStatement(sql.toString());
            int size = entrySet.size();
            Iterator<Map.Entry<String, Object>> entriesIter2 = entrySet.iterator();
            int bindArg = 1;
            for (int i = 0; i < size; i++) {
                DatabaseUtils.bindObjectToProgram(statement2, bindArg, entriesIter2.next().getValue());
                bindArg++;
            }
            if (whereArgs != null) {
                for (String str : whereArgs) {
                    statement2.bindString(bindArg, str);
                    bindArg++;
                }
            }
            statement2.execute();
            int numChangedRows = lastChangeCount();
            if (Log.isLoggable(TAG, 2)) {
                Log.v(TAG, "Updated " + numChangedRows + " rows using <redacted values> and <redacted sql> for " + table);
            }
            if (statement2 != null) {
                statement2.close();
            }
            unlock();
            return numChangedRows;
        } catch (SQLiteDatabaseCorruptException e) {
            onCorruption();
            throw e;
        } catch (SQLException e2) {
            Log.e(TAG, "Error updating <redacted values> using <redacted sql> for " + table);
            throw e2;
        } catch (Throwable th) {
            if (statement != null) {
                statement.close();
            }
            unlock();
            throw th;
        }
    }

    public void execSQL(String sql) throws SQLException {
        long uptimeMillis = SystemClock.uptimeMillis();
        lock();
        if (!isOpen()) {
            throw new IllegalStateException("database not open");
        }
        try {
            native_execSQL(sql);
            unlock();
        } catch (SQLiteDatabaseCorruptException e) {
            onCorruption();
            throw e;
        } catch (Throwable th) {
            unlock();
            throw th;
        }
    }

    public void rawExecSQL(String sql) {
        long uptimeMillis = SystemClock.uptimeMillis();
        lock();
        if (!isOpen()) {
            throw new IllegalStateException("database not open");
        }
        try {
            native_rawExecSQL(sql);
            unlock();
        } catch (SQLiteDatabaseCorruptException e) {
            onCorruption();
            throw e;
        } catch (Throwable th) {
            unlock();
            throw th;
        }
    }

    public void execSQL(String sql, Object[] bindArgs) throws SQLException {
        if (bindArgs == null) {
            throw new IllegalArgumentException("Empty bindArgs");
        }
        long uptimeMillis = SystemClock.uptimeMillis();
        lock();
        if (!isOpen()) {
            throw new IllegalStateException("database not open");
        }
        SQLiteStatement statement = null;
        try {
            SQLiteStatement statement2 = compileStatement(sql);
            if (bindArgs != null) {
                int numArgs = bindArgs.length;
                for (int i = 0; i < numArgs; i++) {
                    DatabaseUtils.bindObjectToProgram(statement2, i + 1, bindArgs[i]);
                }
            }
            statement2.execute();
            if (statement2 != null) {
                statement2.close();
            }
            unlock();
        } catch (SQLiteDatabaseCorruptException e) {
            onCorruption();
            throw e;
        } catch (Throwable th) {
            if (statement != null) {
                statement.close();
            }
            unlock();
            throw th;
        }
    }

    /* access modifiers changed from: protected */
    public void finalize() {
        if (isOpen()) {
            Log.e(TAG, "close() was never explicitly called on database '" + this.mPath + "' ", this.mStackTrace);
            closeClosable();
            onAllReferencesReleased();
        }
    }

    public SQLiteDatabase(String path, char[] password, CursorFactory factory, int flags) {
        this(path, factory, flags, (DatabaseErrorHandler) null);
        openDatabaseInternal(password, null);
    }

    public SQLiteDatabase(String path, char[] password, CursorFactory factory, int flags, SQLiteDatabaseHook databaseHook) {
        this(path, factory, flags, (DatabaseErrorHandler) null);
        openDatabaseInternal(password, databaseHook);
    }

    private SQLiteDatabase(String path, CursorFactory factory, int flags, DatabaseErrorHandler errorHandler) {
        this.mLock = new ReentrantLock(true);
        this.mLockAcquiredWallTime = 0;
        this.mLockAcquiredThreadTime = 0;
        this.mLastLockMessageTime = 0;
        this.mLastSqlStatement = null;
        this.mNativeHandle = 0;
        this.mTempTableSequence = 0;
        this.mPathForLogs = null;
        this.mCompiledQueries = new HashMap();
        this.mMaxSqlCacheSize = 250;
        this.mTimeOpened = null;
        this.mTimeClosed = null;
        this.mStackTrace = null;
        this.mLockingEnabled = true;
        this.mSyncUpdateInfo = new HashMap();
        if (path == null) {
            throw new IllegalArgumentException("path should not be null");
        }
        this.mFlags = flags;
        this.mPath = path;
        this.mSlowQueryThreshold = -1;
        this.mStackTrace = new DatabaseObjectNotClosedException().fillInStackTrace();
        this.mFactory = factory;
        this.mPrograms = new WeakHashMap<>();
        this.mErrorHandler = errorHandler;
    }

    /*  JADX ERROR: StackOverflowError in pass: MarkFinallyVisitor
        java.lang.StackOverflowError
        	at jadx.core.dex.nodes.InsnNode.isSame(InsnNode.java:294)
        	at jadx.core.dex.instructions.IfNode.isSame(IfNode.java:123)
        	at jadx.core.dex.visitors.MarkFinallyVisitor.sameInsns(MarkFinallyVisitor.java:451)
        	at jadx.core.dex.visitors.MarkFinallyVisitor.compareBlocks(MarkFinallyVisitor.java:436)
        	at jadx.core.dex.visitors.MarkFinallyVisitor.checkBlocksTree(MarkFinallyVisitor.java:408)
        	at jadx.core.dex.visitors.MarkFinallyVisitor.checkBlocksTree(MarkFinallyVisitor.java:411)
        */
    private void openDatabaseInternal(char[] r8, dji.thirdparty.ciphersql.database.SQLiteDatabaseHook r9) {
        /*
            r7 = this;
            r5 = 0
            r3 = 1
            byte[] r2 = r7.getBytes(r8)
            java.lang.String r4 = r7.mPath
            int r6 = r7.mFlags
            r7.dbopen(r4, r6)
            dji.thirdparty.ciphersql.database.SQLiteDatabase$2 r4 = new dji.thirdparty.ciphersql.database.SQLiteDatabase$2     // Catch:{ RuntimeException -> 0x0032 }
            r4.<init>(r2)     // Catch:{ RuntimeException -> 0x0032 }
            r7.keyDatabase(r9, r4)     // Catch:{ RuntimeException -> 0x0032 }
            r3 = 0
            if (r3 == 0) goto L_0x0025
            r7.dbclose()
            boolean r4 = dji.thirdparty.ciphersql.database.SQLiteDebug.DEBUG_SQL_CACHE
            if (r4 == 0) goto L_0x0025
            java.lang.String r4 = r7.getTime()
            r7.mTimeClosed = r4
        L_0x0025:
            if (r2 == 0) goto L_0x0085
            int r4 = r2.length
            if (r4 <= 0) goto L_0x0085
            int r4 = r2.length
        L_0x002b:
            if (r5 >= r4) goto L_0x0085
            byte r0 = r2[r5]
            int r5 = r5 + 1
            goto L_0x002b
        L_0x0032:
            r1 = move-exception
            boolean r4 = r7.containsNull(r8)     // Catch:{ all -> 0x0067 }
            if (r4 == 0) goto L_0x0066
            dji.thirdparty.ciphersql.database.SQLiteDatabase$3 r4 = new dji.thirdparty.ciphersql.database.SQLiteDatabase$3     // Catch:{ all -> 0x0067 }
            r4.<init>(r8)     // Catch:{ all -> 0x0067 }
            r7.keyDatabase(r9, r4)     // Catch:{ all -> 0x0067 }
            if (r2 == 0) goto L_0x0049
            int r4 = r2.length     // Catch:{ all -> 0x0067 }
            if (r4 <= 0) goto L_0x0049
            r7.rekey(r2)     // Catch:{ all -> 0x0067 }
        L_0x0049:
            r3 = 0
            if (r3 == 0) goto L_0x0059
            r7.dbclose()
            boolean r4 = dji.thirdparty.ciphersql.database.SQLiteDebug.DEBUG_SQL_CACHE
            if (r4 == 0) goto L_0x0059
            java.lang.String r4 = r7.getTime()
            r7.mTimeClosed = r4
        L_0x0059:
            if (r2 == 0) goto L_0x0085
            int r4 = r2.length
            if (r4 <= 0) goto L_0x0085
            int r4 = r2.length
        L_0x005f:
            if (r5 >= r4) goto L_0x0085
            byte r0 = r2[r5]
            int r5 = r5 + 1
            goto L_0x005f
        L_0x0066:
            throw r1     // Catch:{ all -> 0x0067 }
        L_0x0067:
            r4 = move-exception
            if (r3 == 0) goto L_0x0077
            r7.dbclose()
            boolean r6 = dji.thirdparty.ciphersql.database.SQLiteDebug.DEBUG_SQL_CACHE
            if (r6 == 0) goto L_0x0077
            java.lang.String r6 = r7.getTime()
            r7.mTimeClosed = r6
        L_0x0077:
            if (r2 == 0) goto L_0x0084
            int r6 = r2.length
            if (r6 <= 0) goto L_0x0084
            int r6 = r2.length
        L_0x007d:
            if (r5 >= r6) goto L_0x0084
            byte r0 = r2[r5]
            int r5 = r5 + 1
            goto L_0x007d
        L_0x0084:
            throw r4
        L_0x0085:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: dji.thirdparty.ciphersql.database.SQLiteDatabase.openDatabaseInternal(char[], dji.thirdparty.ciphersql.database.SQLiteDatabaseHook):void");
    }

    private boolean containsNull(char[] data) {
        if (data == null || data.length <= 0) {
            return false;
        }
        for (char datum : data) {
            if (datum == 0) {
                return true;
            }
        }
        return false;
    }

    private void keyDatabase(SQLiteDatabaseHook databaseHook, Runnable keyOperation) {
        if (databaseHook != null) {
            databaseHook.preKey(this);
        }
        if (keyOperation != null) {
            keyOperation.run();
        }
        if (databaseHook != null) {
            databaseHook.postKey(this);
        }
        if (SQLiteDebug.DEBUG_SQL_CACHE) {
            this.mTimeOpened = getTime();
        }
        try {
            Cursor cursor = rawQuery("select count(*) from sqlite_master;", new String[0]);
            if (cursor != null) {
                cursor.moveToFirst();
                int i = cursor.getInt(0);
                cursor.close();
            }
        } catch (RuntimeException e) {
            Log.e(TAG, e.getMessage(), e);
            throw e;
        }
    }

    private String getTime() {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS ", Locale.US).format(Long.valueOf(System.currentTimeMillis()));
    }

    public boolean isReadOnly() {
        return (this.mFlags & 1) == 1;
    }

    public boolean isOpen() {
        return this.mNativeHandle != 0;
    }

    public boolean needUpgrade(int newVersion) {
        return newVersion > getVersion();
    }

    public final String getPath() {
        return this.mPath;
    }

    private String getPathForLogs() {
        if (this.mPathForLogs != null) {
            return this.mPathForLogs;
        }
        if (this.mPath == null) {
            return null;
        }
        if (this.mPath.indexOf(64) == -1) {
            this.mPathForLogs = this.mPath;
        } else {
            this.mPathForLogs = EMAIL_IN_DB_PATTERN.matcher(this.mPath).replaceAll("XX@YY");
        }
        return this.mPathForLogs;
    }

    public void setLocale(Locale locale) {
        lock();
        try {
            native_setLocale(locale.toString(), this.mFlags);
        } finally {
            unlock();
        }
    }

    /* access modifiers changed from: package-private */
    /* JADX WARNING: Code restructure failed: missing block: B:29:?, code lost:
        return;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void addToCompiledQueries(java.lang.String r7, dji.thirdparty.ciphersql.database.SQLiteCompiledSql r8) {
        /*
            r6 = this;
            int r2 = r6.mMaxSqlCacheSize
            if (r2 != 0) goto L_0x0032
            boolean r2 = dji.thirdparty.ciphersql.database.SQLiteDebug.DEBUG_SQL_CACHE
            if (r2 == 0) goto L_0x0031
            java.lang.String r2 = "Database"
            java.lang.StringBuilder r3 = new java.lang.StringBuilder
            r3.<init>()
            java.lang.String r4 = "|NOT adding_sql_to_cache|"
            java.lang.StringBuilder r3 = r3.append(r4)
            java.lang.String r4 = r6.getPath()
            java.lang.StringBuilder r3 = r3.append(r4)
            java.lang.String r4 = "|"
            java.lang.StringBuilder r3 = r3.append(r4)
            java.lang.StringBuilder r3 = r3.append(r7)
            java.lang.String r3 = r3.toString()
            android.util.Log.v(r2, r3)
        L_0x0031:
            return
        L_0x0032:
            r1 = 0
            java.util.Map<java.lang.String, dji.thirdparty.ciphersql.database.SQLiteCompiledSql> r3 = r6.mCompiledQueries
            monitor-enter(r3)
            java.util.Map<java.lang.String, dji.thirdparty.ciphersql.database.SQLiteCompiledSql> r2 = r6.mCompiledQueries     // Catch:{ all -> 0x0044 }
            java.lang.Object r2 = r2.get(r7)     // Catch:{ all -> 0x0044 }
            r0 = r2
            dji.thirdparty.ciphersql.database.SQLiteCompiledSql r0 = (dji.thirdparty.ciphersql.database.SQLiteCompiledSql) r0     // Catch:{ all -> 0x0044 }
            r1 = r0
            if (r1 == 0) goto L_0x0047
            monitor-exit(r3)     // Catch:{ all -> 0x0044 }
            goto L_0x0031
        L_0x0044:
            r2 = move-exception
            monitor-exit(r3)     // Catch:{ all -> 0x0044 }
            throw r2
        L_0x0047:
            java.util.Map<java.lang.String, dji.thirdparty.ciphersql.database.SQLiteCompiledSql> r2 = r6.mCompiledQueries     // Catch:{ all -> 0x0044 }
            int r2 = r2.size()     // Catch:{ all -> 0x0044 }
            int r4 = r6.mMaxSqlCacheSize     // Catch:{ all -> 0x0044 }
            if (r2 != r4) goto L_0x008c
            int r2 = r6.mCacheFullWarnings     // Catch:{ all -> 0x0044 }
            int r2 = r2 + 1
            r6.mCacheFullWarnings = r2     // Catch:{ all -> 0x0044 }
            r4 = 1
            if (r2 != r4) goto L_0x008a
            java.lang.String r2 = "Database"
            java.lang.StringBuilder r4 = new java.lang.StringBuilder     // Catch:{ all -> 0x0044 }
            r4.<init>()     // Catch:{ all -> 0x0044 }
            java.lang.String r5 = "Reached MAX size for compiled-sql statement cache for database "
            java.lang.StringBuilder r4 = r4.append(r5)     // Catch:{ all -> 0x0044 }
            java.lang.String r5 = r6.getPath()     // Catch:{ all -> 0x0044 }
            java.lang.StringBuilder r4 = r4.append(r5)     // Catch:{ all -> 0x0044 }
            java.lang.String r5 = "; i.e., NO space for this sql statement in cache: "
            java.lang.StringBuilder r4 = r4.append(r5)     // Catch:{ all -> 0x0044 }
            java.lang.StringBuilder r4 = r4.append(r7)     // Catch:{ all -> 0x0044 }
            java.lang.String r5 = ". Please change your sql statements to use '?' for bindargs, instead of using actual values"
            java.lang.StringBuilder r4 = r4.append(r5)     // Catch:{ all -> 0x0044 }
            java.lang.String r4 = r4.toString()     // Catch:{ all -> 0x0044 }
            android.util.Log.w(r2, r4)     // Catch:{ all -> 0x0044 }
        L_0x008a:
            monitor-exit(r3)     // Catch:{ all -> 0x0044 }
            goto L_0x0031
        L_0x008c:
            java.util.Map<java.lang.String, dji.thirdparty.ciphersql.database.SQLiteCompiledSql> r2 = r6.mCompiledQueries     // Catch:{ all -> 0x0044 }
            r2.put(r7, r8)     // Catch:{ all -> 0x0044 }
            boolean r2 = dji.thirdparty.ciphersql.database.SQLiteDebug.DEBUG_SQL_CACHE     // Catch:{ all -> 0x0044 }
            if (r2 == 0) goto L_0x008a
            java.lang.String r2 = "Database"
            java.lang.StringBuilder r4 = new java.lang.StringBuilder     // Catch:{ all -> 0x0044 }
            r4.<init>()     // Catch:{ all -> 0x0044 }
            java.lang.String r5 = "|adding_sql_to_cache|"
            java.lang.StringBuilder r4 = r4.append(r5)     // Catch:{ all -> 0x0044 }
            java.lang.String r5 = r6.getPath()     // Catch:{ all -> 0x0044 }
            java.lang.StringBuilder r4 = r4.append(r5)     // Catch:{ all -> 0x0044 }
            java.lang.String r5 = "|"
            java.lang.StringBuilder r4 = r4.append(r5)     // Catch:{ all -> 0x0044 }
            java.util.Map<java.lang.String, dji.thirdparty.ciphersql.database.SQLiteCompiledSql> r5 = r6.mCompiledQueries     // Catch:{ all -> 0x0044 }
            int r5 = r5.size()     // Catch:{ all -> 0x0044 }
            java.lang.StringBuilder r4 = r4.append(r5)     // Catch:{ all -> 0x0044 }
            java.lang.String r5 = "|"
            java.lang.StringBuilder r4 = r4.append(r5)     // Catch:{ all -> 0x0044 }
            java.lang.StringBuilder r4 = r4.append(r7)     // Catch:{ all -> 0x0044 }
            java.lang.String r4 = r4.toString()     // Catch:{ all -> 0x0044 }
            android.util.Log.v(r2, r4)     // Catch:{ all -> 0x0044 }
            goto L_0x008a
        */
        throw new UnsupportedOperationException("Method not decompiled: dji.thirdparty.ciphersql.database.SQLiteDatabase.addToCompiledQueries(java.lang.String, dji.thirdparty.ciphersql.database.SQLiteCompiledSql):void");
    }

    private void deallocCachedSqlStatements() {
        synchronized (this.mCompiledQueries) {
            for (SQLiteCompiledSql compiledSql : this.mCompiledQueries.values()) {
                compiledSql.releaseSqlStatement();
            }
            this.mCompiledQueries.clear();
        }
    }

    /* access modifiers changed from: package-private */
    /* JADX WARNING: Code restructure failed: missing block: B:15:0x003c, code lost:
        if (r1 == false) goto L_0x00cb;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:16:0x003e, code lost:
        r8.mNumCacheHits++;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:18:0x0046, code lost:
        if (dji.thirdparty.ciphersql.database.SQLiteDebug.DEBUG_SQL_CACHE == false) goto L_0x00c1;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:19:0x0048, code lost:
        android.util.Log.v(dji.thirdparty.ciphersql.database.SQLiteDatabase.TAG, "|cache_stats|" + getPath() + "|" + r8.mCompiledQueries.size() + "|" + r8.mNumCacheHits + "|" + r8.mNumCacheMisses + "|" + r1 + "|" + r8.mTimeOpened + "|" + r8.mTimeClosed + "|" + r9);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:26:0x00cb, code lost:
        r8.mNumCacheMisses++;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:31:?, code lost:
        return r2;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public dji.thirdparty.ciphersql.database.SQLiteCompiledSql getCompiledStatementForSql(java.lang.String r9) {
        /*
            r8 = this;
            r2 = 0
            java.util.Map<java.lang.String, dji.thirdparty.ciphersql.database.SQLiteCompiledSql> r5 = r8.mCompiledQueries
            monitor-enter(r5)
            int r4 = r8.mMaxSqlCacheSize     // Catch:{ all -> 0x00c8 }
            if (r4 != 0) goto L_0x002e
            boolean r4 = dji.thirdparty.ciphersql.database.SQLiteDebug.DEBUG_SQL_CACHE     // Catch:{ all -> 0x00c8 }
            if (r4 == 0) goto L_0x002a
            java.lang.String r4 = "Database"
            java.lang.StringBuilder r6 = new java.lang.StringBuilder     // Catch:{ all -> 0x00c8 }
            r6.<init>()     // Catch:{ all -> 0x00c8 }
            java.lang.String r7 = "|cache NOT found|"
            java.lang.StringBuilder r6 = r6.append(r7)     // Catch:{ all -> 0x00c8 }
            java.lang.String r7 = r8.getPath()     // Catch:{ all -> 0x00c8 }
            java.lang.StringBuilder r6 = r6.append(r7)     // Catch:{ all -> 0x00c8 }
            java.lang.String r6 = r6.toString()     // Catch:{ all -> 0x00c8 }
            android.util.Log.v(r4, r6)     // Catch:{ all -> 0x00c8 }
        L_0x002a:
            r4 = 0
            monitor-exit(r5)     // Catch:{ all -> 0x00c8 }
            r3 = r2
        L_0x002d:
            return r4
        L_0x002e:
            java.util.Map<java.lang.String, dji.thirdparty.ciphersql.database.SQLiteCompiledSql> r4 = r8.mCompiledQueries     // Catch:{ all -> 0x00c8 }
            java.lang.Object r4 = r4.get(r9)     // Catch:{ all -> 0x00c8 }
            r0 = r4
            dji.thirdparty.ciphersql.database.SQLiteCompiledSql r0 = (dji.thirdparty.ciphersql.database.SQLiteCompiledSql) r0     // Catch:{ all -> 0x00c8 }
            r2 = r0
            if (r2 == 0) goto L_0x00c5
            r1 = 1
        L_0x003b:
            monitor-exit(r5)     // Catch:{ all -> 0x00c8 }
            if (r1 == 0) goto L_0x00cb
            int r4 = r8.mNumCacheHits
            int r4 = r4 + 1
            r8.mNumCacheHits = r4
        L_0x0044:
            boolean r4 = dji.thirdparty.ciphersql.database.SQLiteDebug.DEBUG_SQL_CACHE
            if (r4 == 0) goto L_0x00c1
            java.lang.String r4 = "Database"
            java.lang.StringBuilder r5 = new java.lang.StringBuilder
            r5.<init>()
            java.lang.String r6 = "|cache_stats|"
            java.lang.StringBuilder r5 = r5.append(r6)
            java.lang.String r6 = r8.getPath()
            java.lang.StringBuilder r5 = r5.append(r6)
            java.lang.String r6 = "|"
            java.lang.StringBuilder r5 = r5.append(r6)
            java.util.Map<java.lang.String, dji.thirdparty.ciphersql.database.SQLiteCompiledSql> r6 = r8.mCompiledQueries
            int r6 = r6.size()
            java.lang.StringBuilder r5 = r5.append(r6)
            java.lang.String r6 = "|"
            java.lang.StringBuilder r5 = r5.append(r6)
            int r6 = r8.mNumCacheHits
            java.lang.StringBuilder r5 = r5.append(r6)
            java.lang.String r6 = "|"
            java.lang.StringBuilder r5 = r5.append(r6)
            int r6 = r8.mNumCacheMisses
            java.lang.StringBuilder r5 = r5.append(r6)
            java.lang.String r6 = "|"
            java.lang.StringBuilder r5 = r5.append(r6)
            java.lang.StringBuilder r5 = r5.append(r1)
            java.lang.String r6 = "|"
            java.lang.StringBuilder r5 = r5.append(r6)
            java.lang.String r6 = r8.mTimeOpened
            java.lang.StringBuilder r5 = r5.append(r6)
            java.lang.String r6 = "|"
            java.lang.StringBuilder r5 = r5.append(r6)
            java.lang.String r6 = r8.mTimeClosed
            java.lang.StringBuilder r5 = r5.append(r6)
            java.lang.String r6 = "|"
            java.lang.StringBuilder r5 = r5.append(r6)
            java.lang.StringBuilder r5 = r5.append(r9)
            java.lang.String r5 = r5.toString()
            android.util.Log.v(r4, r5)
        L_0x00c1:
            r3 = r2
            r4 = r2
            goto L_0x002d
        L_0x00c5:
            r1 = 0
            goto L_0x003b
        L_0x00c8:
            r4 = move-exception
            monitor-exit(r5)     // Catch:{ all -> 0x00c8 }
            throw r4
        L_0x00cb:
            int r4 = r8.mNumCacheMisses
            int r4 = r4 + 1
            r8.mNumCacheMisses = r4
            goto L_0x0044
        */
        throw new UnsupportedOperationException("Method not decompiled: dji.thirdparty.ciphersql.database.SQLiteDatabase.getCompiledStatementForSql(java.lang.String):dji.thirdparty.ciphersql.database.SQLiteCompiledSql");
    }

    public boolean isInCompiledSqlCache(String sql) {
        boolean containsKey;
        synchronized (this.mCompiledQueries) {
            containsKey = this.mCompiledQueries.containsKey(sql);
        }
        return containsKey;
    }

    public void purgeFromCompiledSqlCache(String sql) {
        synchronized (this.mCompiledQueries) {
            this.mCompiledQueries.remove(sql);
        }
    }

    public void resetCompiledSqlCache() {
        synchronized (this.mCompiledQueries) {
            this.mCompiledQueries.clear();
        }
    }

    public synchronized int getMaxSqlCacheSize() {
        return this.mMaxSqlCacheSize;
    }

    public synchronized void setMaxSqlCacheSize(int cacheSize) {
        if (cacheSize > 250 || cacheSize < 0) {
            throw new IllegalStateException("expected value between 0 and 250");
        } else if (cacheSize < this.mMaxSqlCacheSize) {
            throw new IllegalStateException("cannot set cacheSize to a value less than the value set with previous setMaxSqlCacheSize() call.");
        } else {
            this.mMaxSqlCacheSize = cacheSize;
        }
    }

    static ArrayList<SQLiteDebug.DbStats> getDbStats() {
        String dbName;
        ArrayList<SQLiteDebug.DbStats> dbStatsList = new ArrayList<>();
        Iterator<SQLiteDatabase> it2 = getActiveDatabases().iterator();
        while (it2.hasNext()) {
            SQLiteDatabase db = it2.next();
            if (db != null && db.isOpen()) {
                int lookasideUsed = db.native_getDbLookaside();
                String path = db.getPath();
                int indx = path.lastIndexOf(IMemberProtocol.PARAM_SEPERATOR);
                String lastnode = path.substring(indx != -1 ? indx + 1 : 0);
                ArrayList<Pair<String, String>> attachedDbs = getAttachedDbs(db);
                if (attachedDbs != null) {
                    for (int i = 0; i < attachedDbs.size(); i++) {
                        Pair<String, String> p = attachedDbs.get(i);
                        long pageCount = getPragmaVal(db, ((String) p.first) + ".page_count;");
                        if (i == 0) {
                            dbName = lastnode;
                        } else {
                            lookasideUsed = 0;
                            dbName = "  (attached) " + ((String) p.first);
                            if (((String) p.second).trim().length() > 0) {
                                int idx = ((String) p.second).lastIndexOf(IMemberProtocol.PARAM_SEPERATOR);
                                dbName = dbName + " : " + ((String) p.second).substring(idx != -1 ? idx + 1 : 0);
                            }
                        }
                        if (pageCount > 0) {
                            dbStatsList.add(new SQLiteDebug.DbStats(dbName, pageCount, db.getPageSize(), lookasideUsed));
                        }
                    }
                }
            }
        }
        return dbStatsList;
    }

    private static ArrayList<SQLiteDatabase> getActiveDatabases() {
        ArrayList<SQLiteDatabase> databases = new ArrayList<>();
        synchronized (sActiveDatabases) {
            databases.addAll(sActiveDatabases.keySet());
        }
        return databases;
    }

    private static long getPragmaVal(SQLiteDatabase db, String pragma) {
        if (!db.isOpen()) {
            return 0;
        }
        SQLiteStatement prog = null;
        try {
            SQLiteStatement prog2 = new SQLiteStatement(db, "PRAGMA " + pragma);
            try {
                long simpleQueryForLong = prog2.simpleQueryForLong();
                if (prog2 == null) {
                    return simpleQueryForLong;
                }
                prog2.close();
                return simpleQueryForLong;
            } catch (Throwable th) {
                th = th;
                prog = prog2;
            }
        } catch (Throwable th2) {
            th = th2;
            if (prog != null) {
                prog.close();
            }
            throw th;
        }
    }

    private static ArrayList<Pair<String, String>> getAttachedDbs(SQLiteDatabase dbObj) {
        if (!dbObj.isOpen()) {
            return null;
        }
        ArrayList<Pair<String, String>> attachedDbs = new ArrayList<>();
        Cursor c = dbObj.rawQuery("pragma database_list;", (String[]) null);
        while (c.moveToNext()) {
            attachedDbs.add(new Pair(c.getString(1), c.getString(2)));
        }
        c.close();
        return attachedDbs;
    }

    private byte[] getBytes(char[] data) {
        if (data == null || data.length == 0) {
            return null;
        }
        ByteBuffer byteBuffer = Charset.forName(KEY_ENCODING).encode(CharBuffer.wrap(data));
        byte[] result = new byte[byteBuffer.limit()];
        byteBuffer.get(result);
        return result;
    }
}
