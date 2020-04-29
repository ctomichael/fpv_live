package com.dji.permission.checker;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.provider.Telephony;
import android.support.annotation.RequiresApi;
import com.dji.permission.checker.PermissionTest;

class SmsReadTest implements PermissionTest {
    private ContentResolver mResolver;

    SmsReadTest(Context context) {
        this.mResolver = context.getContentResolver();
    }

    /* JADX INFO: finally extract failed */
    @RequiresApi(api = 19)
    public boolean test() throws Throwable {
        Cursor cursor = this.mResolver.query(Telephony.Sms.CONTENT_URI, new String[]{"_id", "address", "person", "body"}, null, null, null);
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
