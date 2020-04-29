package dji.thirdparty.ciphersql;

public class CursorWrapper extends android.database.CursorWrapper implements Cursor {
    private final Cursor mCursor;

    public CursorWrapper(Cursor cursor) {
        super(cursor);
        this.mCursor = cursor;
    }

    public int getType(int columnIndex) {
        return this.mCursor.getType(columnIndex);
    }

    public Cursor getWrappedCursor() {
        return this.mCursor;
    }
}
