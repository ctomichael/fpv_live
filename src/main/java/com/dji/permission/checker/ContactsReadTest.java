package com.dji.permission.checker;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.provider.ContactsContract;
import com.dji.permission.checker.PermissionTest;

class ContactsReadTest implements PermissionTest {
    private ContentResolver mResolver;

    ContactsReadTest(Context context) {
        this.mResolver = context.getContentResolver();
    }

    /* JADX INFO: finally extract failed */
    public boolean test() throws Throwable {
        Cursor cursor = this.mResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, new String[]{"_id", "data1"}, null, null, null);
        if (cursor == null) {
            return false;
        }
        try {
            PermissionTest.CursorTest.read(cursor);
            cursor.close();
            return true;
        } catch (Throwable th) {
            cursor.close();
            throw th;
        }
    }
}
