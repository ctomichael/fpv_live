package dji.thirdparty.ciphersql.database;

public class SQLiteConstraintException extends SQLiteException {
    public SQLiteConstraintException() {
    }

    public SQLiteConstraintException(String error) {
        super(error);
    }
}
