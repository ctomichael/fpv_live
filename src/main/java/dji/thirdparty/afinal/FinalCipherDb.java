package dji.thirdparty.afinal;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import dji.thirdparty.afinal.db.sqlite.CursorUtils;
import dji.thirdparty.afinal.db.sqlite.DbModel;
import dji.thirdparty.afinal.db.sqlite.ManyToOneLazyLoader;
import dji.thirdparty.afinal.db.sqlite.OneToManyLazyLoader;
import dji.thirdparty.afinal.db.sqlite.SqlBuilder;
import dji.thirdparty.afinal.db.sqlite.SqlInfo;
import dji.thirdparty.afinal.db.table.KeyValue;
import dji.thirdparty.afinal.db.table.ManyToOne;
import dji.thirdparty.afinal.db.table.OneToMany;
import dji.thirdparty.afinal.db.table.TableInfo;
import dji.thirdparty.afinal.exception.DbException;
import dji.thirdparty.ciphersql.database.SQLiteDatabase;
import dji.thirdparty.ciphersql.database.SQLiteException;
import dji.thirdparty.ciphersql.database.SQLiteOpenHelper;
import dji.thirdparty.ciphersql.database.SQLiteStatement;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

public class FinalCipherDb extends FinalDb {
    private static final String TAG = "FinalCipherDb";
    private static HashMap<String, FinalCipherDb> daoMap = new HashMap<>();
    private DaoConfig config;
    /* access modifiers changed from: private */
    public SQLiteDatabase db;
    private SQLiteStatement statement;
    private String statementKey;
    private SQLiteStatement statementWhere;
    private TableInfo tableInfo;

    public interface DbUpdateListener {
        void onDowngrade(SQLiteDatabase sQLiteDatabase, int i, int i2);

        void onUpgrade(SQLiteDatabase sQLiteDatabase, int i, int i2);
    }

    private FinalCipherDb(DaoConfig config2) {
        this.db = generateDb(config2);
        this.config = config2;
    }

    private static synchronized FinalCipherDb getInstance(DaoConfig daoConfig) {
        FinalCipherDb dao;
        synchronized (FinalCipherDb.class) {
            dao = daoMap.get(daoConfig.getDbName());
            if (dao == null) {
                dao = new FinalCipherDb(daoConfig);
                daoMap.put(daoConfig.getDbName(), dao);
            }
        }
        return dao;
    }

    public void reopen() {
        SQLiteDatabase tmpDb = null;
        if (this.db != null) {
            tmpDb = this.db;
        }
        this.db = generateDb(this.config);
        if (!(tmpDb == null || this.db == null)) {
            tmpDb.close();
        }
        for (TableInfo tableInfo2 : TableInfo.getTableInfoMap().values()) {
            tableInfo2.setCheckDatabese(false);
        }
    }

    private SQLiteDatabase generateDb(DaoConfig config2) {
        if (config2 == null) {
            throw new DbException("daoConfig is null");
        } else if (config2.getContext() == null) {
            throw new DbException("android context is null");
        } else if (config2.getTargetDirectory() != null && config2.getTargetDirectory().trim().length() > 0) {
            return createDbFileOnSDCard(config2.getTargetDirectory(), config2.getDbName(), config2.getDbVersion(), config2.getDbUpdateListener());
        } else {
            return new SqliteDbHelper(config2.getContext().getApplicationContext(), config2.getDbName(), config2.getDbVersion(), config2.getDbUpdateListener()).getWritableDatabase(config2.pwdKey);
        }
    }

    public static FinalCipherDb create(Context context) {
        DaoConfig config2 = new DaoConfig();
        config2.setContext(context);
        return create(config2);
    }

    public static FinalCipherDb create(Context context, boolean isDebug) {
        DaoConfig config2 = new DaoConfig();
        config2.setContext(context);
        config2.setDebug(isDebug);
        return create(config2);
    }

    public static FinalCipherDb create(Context context, String dbName) {
        DaoConfig config2 = new DaoConfig();
        config2.setContext(context);
        config2.setDbName(dbName);
        return create(config2);
    }

    public static FinalCipherDb create(Context context, String dbName, boolean isDebug) {
        DaoConfig config2 = new DaoConfig();
        config2.setContext(context);
        config2.setDbName(dbName);
        config2.setDebug(isDebug);
        return create(config2);
    }

    public static FinalCipherDb create(Context context, String targetDirectory, String dbName) {
        DaoConfig config2 = new DaoConfig();
        config2.setContext(context);
        config2.setDbName(dbName);
        config2.setTargetDirectory(targetDirectory);
        return create(config2);
    }

    public static FinalCipherDb create(Context context, String targetDirectory, String dbName, boolean isDebug) {
        DaoConfig config2 = new DaoConfig();
        config2.setContext(context);
        config2.setTargetDirectory(targetDirectory);
        config2.setDbName(dbName);
        config2.setDebug(isDebug);
        return create(config2);
    }

    public static FinalCipherDb create(Context context, String dbName, boolean isDebug, int dbVersion, DbUpdateListener dbUpdateListener) {
        DaoConfig config2 = new DaoConfig();
        config2.setContext(context);
        config2.setDbName(dbName);
        config2.setDebug(isDebug);
        config2.setDbVersion(dbVersion);
        config2.setDbUpdateListener(dbUpdateListener);
        return create(config2);
    }

    public static FinalCipherDb create(Context context, String targetDirectory, String dbName, boolean isDebug, int dbVersion, DbUpdateListener dbUpdateListener) {
        DaoConfig config2 = new DaoConfig();
        config2.setContext(context);
        config2.setTargetDirectory(targetDirectory);
        config2.setDbName(dbName);
        config2.setDebug(isDebug);
        config2.setDbVersion(dbVersion);
        config2.setDbUpdateListener(dbUpdateListener);
        return create(config2);
    }

    public static FinalCipherDb create(Context context, String targetDirectory, String dbName, boolean isDebug, int dbVersion, String pwd, DbUpdateListener dbUpdateListener) {
        DaoConfig config2 = new DaoConfig();
        config2.setContext(context);
        config2.setTargetDirectory(targetDirectory);
        config2.setDbName(dbName);
        config2.setDebug(isDebug);
        config2.setDbVersion(dbVersion);
        config2.setPwdKey(pwd);
        config2.setDbUpdateListener(dbUpdateListener);
        return create(config2);
    }

    public static FinalCipherDb create(DaoConfig daoConfig) {
        return getInstance(daoConfig);
    }

    public void save(Object entity) {
        checkTableExist(entity.getClass());
        exeSqlInfo(SqlBuilder.buildInsertSql(entity));
    }

    public void saveManyStart(Object entity) {
        this.tableInfo = checkTableExist(entity.getClass());
        this.db.beginTransaction();
        this.statement = this.db.compileStatement(SqlBuilder.buildInsertSqlNoValue(entity));
    }

    public void saveManyByWhereStart(Object entity, String key) {
        this.tableInfo = checkTableExist(entity.getClass());
        this.db.beginTransaction();
        this.statementWhere = this.db.compileStatement(SqlBuilder.buildInsertSqlNoValueWithId(entity, key));
        this.statementKey = key;
    }

    public void saveManyStep(Object entity) {
        if (this.statement != null) {
            this.statement.acquireReference();
            this.statement.clearBindings();
            sqlcipherStatementBindAllStrings(this.statement, SqlBuilder.getSaveKeyValueArrayByEntity(this.tableInfo, entity));
            this.statement.executeUpdateDelete();
            this.statement.releaseReference();
        }
    }

    public void saveManyStepByWhere(Object entity, String value) {
        if (this.statementWhere != null) {
            this.statementWhere.acquireReference();
            this.statementWhere.clearBindings();
            sqlcipherStatementBindAllStrings(this.statementWhere, SqlBuilder.getSaveKeyValueArrayByEntityWithId(this.tableInfo, entity, this.statementKey, value));
            this.statementWhere.executeUpdateDelete();
            this.statementWhere.releaseReference();
        }
    }

    private void sqlcipherStatementBindAllStrings(SQLiteStatement sqLiteStatement, String[] bindArgs) {
        if (bindArgs != null) {
            for (int i = bindArgs.length; i != 0; i--) {
                sqLiteStatement.bindString(i, bindArgs[i - 1]);
            }
        }
    }

    public void saveManyEnd() {
        if (this.statement != null) {
            this.db.setTransactionSuccessful();
            this.db.endTransaction();
            this.tableInfo = null;
            this.statement.close();
            this.statement = null;
        }
    }

    public void saveManyEndByWhere() {
        if (this.statementWhere != null) {
            this.db.setTransactionSuccessful();
            this.db.endTransaction();
            this.tableInfo = null;
            this.statementWhere.close();
            this.statementWhere = null;
        }
    }

    public boolean saveBindId(Object entity) {
        checkTableExist(entity.getClass());
        List<KeyValue> entityKvList = SqlBuilder.getSaveKeyValueListByEntity(entity);
        if (entityKvList == null || entityKvList.size() <= 0) {
            return false;
        }
        TableInfo tf = TableInfo.get(entity.getClass());
        ContentValues cv = new ContentValues();
        insertContentValues(entityKvList, cv);
        Long id = Long.valueOf(this.db.insert(tf.getTableName(), null, cv));
        if (id.longValue() == -1) {
            return false;
        }
        tf.getId().setValue(entity, id);
        return true;
    }

    private void insertContentValues(List<KeyValue> list, ContentValues cv) {
        if (list == null || cv == null) {
            Log.w(TAG, "insertContentValues: List<KeyValue> is empty or ContentValues is empty!");
            return;
        }
        for (KeyValue kv : list) {
            cv.put(kv.getKey(), kv.getValue().toString());
        }
    }

    public void update(Object entity) {
        checkTableExist(entity.getClass());
        exeSqlInfo(SqlBuilder.getUpdateSqlAsSqlInfo(entity));
    }

    public void update(Object entity, String strWhere) {
        checkTableExist(entity.getClass());
        exeSqlInfo(SqlBuilder.getUpdateSqlAsSqlInfo(entity, strWhere));
    }

    public void delete(Object entity) {
        checkTableExist(entity.getClass());
        exeSqlInfo(SqlBuilder.buildDeleteSql(entity));
    }

    public void deleteById(Class<?> clazz, Object id) {
        checkTableExist(clazz);
        exeSqlInfo(SqlBuilder.buildDeleteSql(clazz, id));
    }

    public void deleteByWhere(Class<?> clazz, String strWhere) {
        checkTableExist(clazz);
        String sql = SqlBuilder.buildDeleteSql(clazz, strWhere);
        debugSql(sql);
        this.db.execSQL(sql);
    }

    public void deleteAll(Class<?> clazz) {
        checkTableExist(clazz);
        String sql = SqlBuilder.buildDeleteSql(clazz, (String) null);
        debugSql(sql);
        this.db.execSQL(sql);
    }

    public void dropTable(Class<?> clazz) {
        checkTableExist(clazz);
        TableInfo table = TableInfo.get(clazz);
        String sql = "DROP TABLE " + table.getTableName();
        debugSql(sql);
        this.db.execSQL(sql);
        table.setCheckDatabese(false);
    }

    public void dropDb() {
        Cursor cursor = this.db.rawQuery("SELECT name FROM sqlite_master WHERE type ='table' AND name != 'sqlite_sequence'", (String[]) null);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                this.db.execSQL("DROP TABLE " + cursor.getString(0));
            }
        }
        if (cursor != null) {
            cursor.close();
        }
        for (TableInfo tableInfo2 : TableInfo.getTableInfoMap().values()) {
            tableInfo2.setCheckDatabese(false);
        }
    }

    private void exeSqlInfo(SqlInfo sqlInfo) {
        if (sqlInfo != null) {
            debugSql(sqlInfo.getSql());
            this.db.execSQL(sqlInfo.getSql(), sqlInfo.getBindArgsAsArray());
            return;
        }
        Log.e(TAG, "sava error:sqlInfo is null");
    }

    public <T> T findById(Object id, Class<T> clazz) {
        checkTableExist(clazz);
        SqlInfo sqlInfo = SqlBuilder.getSelectSqlAsSqlInfo(clazz, id);
        if (sqlInfo != null) {
            debugSql(sqlInfo.getSql());
            Cursor cursor = this.db.rawQuery(sqlInfo.getSql(), sqlInfo.getBindArgsAsStringArray());
            try {
                if (cursor.moveToNext()) {
                    return CursorUtils.getEntity(cursor, clazz, this);
                }
                cursor.close();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                cursor.close();
            }
        }
        return null;
    }

    public <T> T findWithManyToOneById(Object id, Class<T> clazz) {
        checkTableExist(clazz);
        String sql = SqlBuilder.getSelectSQL(clazz, id);
        debugSql(sql);
        DbModel dbModel = findDbModelBySQL(sql);
        if (dbModel != null) {
            return loadManyToOne(dbModel, CursorUtils.dbModel2Entity(dbModel, clazz), clazz, new Class[0]);
        }
        return null;
    }

    public <T> T findWithManyToOneById(Object id, Class<T> clazz, Class<?>... findClass) {
        checkTableExist(clazz);
        String sql = SqlBuilder.getSelectSQL(clazz, id);
        debugSql(sql);
        DbModel dbModel = findDbModelBySQL(sql);
        if (dbModel != null) {
            return loadManyToOne(dbModel, CursorUtils.dbModel2Entity(dbModel, clazz), clazz, findClass);
        }
        return null;
    }

    public <T> T loadManyToOne(DbModel dbModel, T entity, Class<T> clazz, Class<?>... findClass) {
        T manyEntity;
        if (entity != null) {
            try {
                for (ManyToOne many : TableInfo.get((Class<?>) clazz).manyToOneMap.values()) {
                    Object id = null;
                    if (dbModel != null) {
                        id = dbModel.get(many.getColumn());
                    } else if (many.getValue(entity).getClass() == ManyToOneLazyLoader.class && many.getValue(entity) != null) {
                        id = ((ManyToOneLazyLoader) many.getValue(entity)).getFieldValue();
                    }
                    if (id != null) {
                        boolean isFind = false;
                        if (findClass == null || findClass.length == 0) {
                            isFind = true;
                        }
                        int length = findClass.length;
                        int i = 0;
                        while (true) {
                            if (i >= length) {
                                break;
                            } else if (many.getManyClass() == findClass[i]) {
                                isFind = true;
                                break;
                            } else {
                                i++;
                            }
                        }
                        if (isFind && (manyEntity = findById(Integer.valueOf(id.toString()), many.getManyClass())) != null) {
                            if (many.getValue(entity).getClass() == ManyToOneLazyLoader.class) {
                                if (many.getValue(entity) == null) {
                                    many.setValue(entity, new ManyToOneLazyLoader(entity, clazz, many.getManyClass(), this));
                                }
                                ((ManyToOneLazyLoader) many.getValue(entity)).set(manyEntity);
                            } else {
                                many.setValue(entity, manyEntity);
                            }
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return entity;
    }

    public <T> T findWithOneToManyById(Object id, Class<T> clazz) {
        checkTableExist(clazz);
        String sql = SqlBuilder.getSelectSQL(clazz, id);
        debugSql(sql);
        DbModel dbModel = findDbModelBySQL(sql);
        if (dbModel != null) {
            return loadOneToMany(CursorUtils.dbModel2Entity(dbModel, clazz), clazz, new Class[0]);
        }
        return null;
    }

    public <T> T findWithOneToManyById(Object id, Class<T> clazz, Class<?>... findClass) {
        checkTableExist(clazz);
        String sql = SqlBuilder.getSelectSQL(clazz, id);
        debugSql(sql);
        DbModel dbModel = findDbModelBySQL(sql);
        if (dbModel != null) {
            return loadOneToMany(CursorUtils.dbModel2Entity(dbModel, clazz), clazz, findClass);
        }
        return null;
    }

    public <T> T loadOneToMany(T entity, Class<T> clazz, Class<?>... findClass) {
        List<?> list;
        if (entity != null) {
            try {
                Collection<OneToMany> ones = TableInfo.get((Class<?>) clazz).oneToManyMap.values();
                Object id = TableInfo.get((Class<?>) clazz).getId().getValue(entity);
                for (OneToMany one : ones) {
                    boolean isFind = false;
                    if (findClass == null || findClass.length == 0) {
                        isFind = true;
                    }
                    int length = findClass.length;
                    int i = 0;
                    while (true) {
                        if (i >= length) {
                            break;
                        } else if (one.getOneClass() == findClass[i]) {
                            isFind = true;
                            break;
                        } else {
                            i++;
                        }
                    }
                    if (isFind && (list = findAllByWhere(one.getOneClass(), one.getColumn() + "=" + id)) != null) {
                        if (one.getDataType() == OneToManyLazyLoader.class) {
                            ((OneToManyLazyLoader) one.getValue(entity)).setList(list);
                        } else {
                            one.setValue(entity, list);
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return entity;
    }

    public <T> List<T> findAll(Class<T> clazz) {
        checkTableExist(clazz);
        return findAllBySql(clazz, SqlBuilder.getSelectSQL(clazz));
    }

    public <T> List<T> findAll(Class<T> clazz, String orderBy) {
        checkTableExist(clazz);
        return findAllBySql(clazz, SqlBuilder.getSelectSQL(clazz) + " ORDER BY " + orderBy);
    }

    public <T> List<T> findAll(Class<T> clazz, String orderBy, String limit) {
        checkTableExist(clazz);
        return findAllBySql(clazz, SqlBuilder.getSelectSQL(clazz) + " ORDER BY " + orderBy + " LIMIT " + limit);
    }

    public <T> List<T> findAllByWhere(Class<T> clazz, String strWhere) {
        checkTableExist(clazz);
        return findAllBySql(clazz, SqlBuilder.getSelectSQLByWhere(clazz, strWhere));
    }

    public <T> List<T> findAllByWhere(Class<T> clazz, String strWhere, String orderBy) {
        checkTableExist(clazz);
        return findAllBySql(clazz, SqlBuilder.getSelectSQLByWhere(clazz, strWhere) + " ORDER BY " + orderBy);
    }

    private <T> List<T> findAllBySql(Class<T> clazz, String strSQL) {
        checkTableExist(clazz);
        debugSql(strSQL);
        Cursor cursor = this.db.rawQuery(strSQL, (String[]) null);
        try {
            List<T> list = new ArrayList<>();
            while (cursor.moveToNext()) {
                list.add(CursorUtils.getEntity(cursor, clazz, this));
            }
            if (cursor != null) {
                cursor.close();
            }
            return list;
        } catch (Exception e) {
            e.printStackTrace();
            if (cursor != null) {
                cursor.close();
            }
            return null;
        } catch (Throwable th) {
            if (cursor != null) {
                cursor.close();
            }
            throw th;
        }
    }

    public DbModel findDbModelBySQL(String strSQL) {
        DbModel dbModel = null;
        debugSql(strSQL);
        Cursor cursor = this.db.rawQuery(strSQL, (String[]) null);
        try {
            if (cursor.moveToNext()) {
                dbModel = CursorUtils.getDbModel(cursor);
            } else {
                cursor.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            cursor.close();
        }
        return dbModel;
    }

    public List<DbModel> findDbModelListBySQL(String strSQL) {
        debugSql(strSQL);
        Cursor cursor = this.db.rawQuery(strSQL, (String[]) null);
        List<DbModel> dbModelList = new ArrayList<>();
        while (cursor.moveToNext()) {
            try {
                dbModelList.add(CursorUtils.getDbModel(cursor));
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                cursor.close();
            }
        }
        return dbModelList;
    }

    public TableInfo checkTableExist(Class<?> clazz) {
        TableInfo tableInfo2 = TableInfo.get(clazz);
        if (!tableIsExist(tableInfo2)) {
            String sql = SqlBuilder.getCreatTableSQL(clazz);
            debugSql(sql);
            Log.d(TAG, "db  isReadOnly=" + this.db.isReadOnly());
            this.db.execSQL(sql);
        }
        return tableInfo2;
    }

    private boolean tableIsExist(TableInfo table) {
        if (table.isCheckDatabese()) {
            return true;
        }
        Cursor cursor = null;
        try {
            String sql = "SELECT COUNT(*) AS c FROM sqlite_master WHERE type ='table' AND name ='" + table.getTableName() + "' ";
            debugSql(sql);
            Cursor cursor2 = this.db.rawQuery(sql, (String[]) null);
            if (cursor2 == null || !cursor2.moveToNext() || cursor2.getInt(0) <= 0) {
                if (cursor2 != null) {
                    cursor2.close();
                }
                return false;
            }
            table.setCheckDatabese(true);
            if (cursor2 != null) {
                cursor2.close();
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            if (cursor != null) {
                cursor.close();
            }
        } catch (Throwable th) {
            if (cursor != null) {
                cursor.close();
            }
            throw th;
        }
    }

    private void debugSql(String sql) {
        if (this.config != null && this.config.isDebug()) {
            Log.d("Debug SQL", ">>>>>>  " + sql);
        }
    }

    public static class DaoConfig {
        private DbUpdateListener dbUpdateListener;
        private int dbVersion = 1;
        private boolean debug = true;
        private Context mContext = null;
        private String mDbName = "dji.db";
        /* access modifiers changed from: private */
        public String pwdKey = "";
        private String targetDirectory;

        public void setPwdKey(String pwdKey2) {
            this.pwdKey = pwdKey2;
        }

        public Context getContext() {
            return this.mContext;
        }

        public void setContext(Context context) {
            this.mContext = context;
        }

        public String getDbName() {
            return this.mDbName;
        }

        public void setDbName(String dbName) {
            this.mDbName = dbName;
        }

        public int getDbVersion() {
            return this.dbVersion;
        }

        public void setDbVersion(int dbVersion2) {
            this.dbVersion = dbVersion2;
        }

        public boolean isDebug() {
            return this.debug;
        }

        public void setDebug(boolean debug2) {
            this.debug = debug2;
        }

        public DbUpdateListener getDbUpdateListener() {
            return this.dbUpdateListener;
        }

        public void setDbUpdateListener(DbUpdateListener dbUpdateListener2) {
            this.dbUpdateListener = dbUpdateListener2;
        }

        public String getTargetDirectory() {
            return this.targetDirectory;
        }

        public void setTargetDirectory(String targetDirectory2) {
            this.targetDirectory = targetDirectory2;
        }
    }

    private SQLiteDatabase createDbFileOnSDCard(String sdcardPath, String dbfilename, int mNewVersion, DbUpdateListener dbUpdateListener) {
        File dbf = new File(sdcardPath, dbfilename);
        SQLiteDatabase db2 = null;
        if (!dbf.exists()) {
            try {
                if (dbf.createNewFile()) {
                    db2 = SQLiteDatabase.openDatabase(dbf.getPath(), "", (SQLiteDatabase.CursorFactory) null, 16);
                }
            } catch (IOException ioex) {
                throw new DbException("数据库文件创建失败", ioex);
            }
        } else {
            db2 = SQLiteDatabase.openDatabase(dbf.getPath(), "", (SQLiteDatabase.CursorFactory) null, 16);
        }
        Log.d(TAG, "db null");
        if (db2 != null) {
            int version = db2.getVersion();
            Log.d(TAG, "db cur version=" + version + " isReadOnly=" + db2.isReadOnly());
            if (version != mNewVersion) {
                if (db2.isReadOnly()) {
                    throw new SQLiteException("Can't upgrade read-only database from version " + db2.getVersion() + " to " + mNewVersion + ": " + dbfilename);
                }
                db2.beginTransaction();
                if (mNewVersion != 0) {
                    if (dbUpdateListener != null) {
                        Log.d(TAG, "db new version=" + mNewVersion);
                        if (version > mNewVersion) {
                            dbUpdateListener.onDowngrade(db2, version, mNewVersion);
                        } else {
                            dbUpdateListener.onUpgrade(db2, version, mNewVersion);
                        }
                    } else {
                        dropDb();
                    }
                }
                try {
                    db2.setVersion(mNewVersion);
                    db2.setTransactionSuccessful();
                } finally {
                    db2.endTransaction();
                }
            }
        }
        return db2;
    }

    class SqliteDbHelper extends SQLiteOpenHelper {
        private DbUpdateListener mDbUpdateListener;

        public SqliteDbHelper(Context context, String name, int version, DbUpdateListener dbUpdateListener) {
            super(context, name, null, version);
            this.mDbUpdateListener = dbUpdateListener;
        }

        public void onCreate(SQLiteDatabase db) {
        }

        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            if (this.mDbUpdateListener != null) {
                this.mDbUpdateListener.onUpgrade(db, oldVersion, newVersion);
                return;
            }
            SQLiteDatabase unused = FinalCipherDb.this.db = db;
            FinalCipherDb.this.dropDb();
        }
    }
}
