package dji.thirdparty.ciphersql.database;

public class SQLiteDoneException extends SQLiteException {
    public SQLiteDoneException() {
    }

    public SQLiteDoneException(String error) {
        super(error);
    }
}
