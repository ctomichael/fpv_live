package dji.thirdparty.ciphersql.database;

import dji.thirdparty.ciphersql.SQLException;

public class SQLiteException extends SQLException {
    public SQLiteException() {
    }

    public SQLiteException(String error) {
        super(error);
    }
}
