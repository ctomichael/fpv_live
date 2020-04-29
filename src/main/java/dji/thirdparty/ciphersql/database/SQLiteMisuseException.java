package dji.thirdparty.ciphersql.database;

public class SQLiteMisuseException extends SQLiteException {
    public SQLiteMisuseException() {
    }

    public SQLiteMisuseException(String error) {
        super(error);
    }
}
