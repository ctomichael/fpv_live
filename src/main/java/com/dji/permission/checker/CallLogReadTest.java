package com.dji.permission.checker;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.provider.CallLog;
import android.support.annotation.RequiresPermission;
import com.dji.permission.Permission;
import com.dji.permission.checker.PermissionTest;

class CallLogReadTest implements PermissionTest {
    private ContentResolver mResolver;

    CallLogReadTest(Context context) {
        this.mResolver = context.getContentResolver();
    }

    /* JADX INFO: finally extract failed */
    @RequiresPermission(Permission.READ_CALL_LOG)
    public boolean test() throws Throwable {
        Cursor cursor = this.mResolver.query(CallLog.Calls.CONTENT_URI, new String[]{"_id", "number", "type"}, null, null, null);
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
