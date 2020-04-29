package dji.thirdparty.ciphersql.database;

public class SQLiteAbortException extends SQLiteException {
    public SQLiteAbortException() {
    }

    public SQLiteAbortException(String error) {
        super(error);
    }
}
