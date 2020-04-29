package dji.thirdparty.ciphersql.database;

public class SQLiteDiskIOException extends SQLiteException {
    public SQLiteDiskIOException() {
    }

    public SQLiteDiskIOException(String error) {
        super(error);
    }
}
