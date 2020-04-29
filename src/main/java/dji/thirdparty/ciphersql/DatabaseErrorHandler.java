package dji.thirdparty.ciphersql;

import dji.thirdparty.ciphersql.database.SQLiteDatabase;

public interface DatabaseErrorHandler {
    void onCorruption(SQLiteDatabase sQLiteDatabase);
}
