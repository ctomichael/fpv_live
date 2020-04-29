package dji.thirdparty.ciphersql.database;

import android.database.Cursor;
import dji.thirdparty.ciphersql.database.SQLiteDatabase;

public interface SQLiteCursorDriver {
    void cursorClosed();

    void cursorDeactivated();

    void cursorRequeried(Cursor cursor);

    dji.thirdparty.ciphersql.Cursor query(SQLiteDatabase.CursorFactory cursorFactory, String[] strArr);

    void setBindArguments(String[] strArr);
}
