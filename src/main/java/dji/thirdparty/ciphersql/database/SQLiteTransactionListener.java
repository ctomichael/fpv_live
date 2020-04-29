package dji.thirdparty.ciphersql.database;

public interface SQLiteTransactionListener {
    void onBegin();

    void onCommit();

    void onRollback();
}
