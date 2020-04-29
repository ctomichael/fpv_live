package com.dji.permission.checker;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.provider.CallLog;
import android.support.annotation.RequiresPermission;
import com.amap.location.common.model.AmapLoc;
import com.dji.permission.Permission;
import dji.publics.protocol.ResponseBase;

class CallLogWriteTest implements PermissionTest {
    private ContentResolver mResolver;

    CallLogWriteTest(Context context) {
        this.mResolver = context.getContentResolver();
    }

    /* JADX DEBUG: Failed to find minimal casts for resolve overloaded methods, cast all args instead
     method: ClspMth{android.content.ContentValues.put(java.lang.String, java.lang.Integer):void}
     arg types: [java.lang.String, int]
     candidates:
      ClspMth{android.content.ContentValues.put(java.lang.String, java.lang.Byte):void}
      ClspMth{android.content.ContentValues.put(java.lang.String, java.lang.Float):void}
      ClspMth{android.content.ContentValues.put(java.lang.String, java.lang.String):void}
      ClspMth{android.content.ContentValues.put(java.lang.String, java.lang.Long):void}
      ClspMth{android.content.ContentValues.put(java.lang.String, java.lang.Boolean):void}
      ClspMth{android.content.ContentValues.put(java.lang.String, byte[]):void}
      ClspMth{android.content.ContentValues.put(java.lang.String, java.lang.Double):void}
      ClspMth{android.content.ContentValues.put(java.lang.String, java.lang.Short):void}
      ClspMth{android.content.ContentValues.put(java.lang.String, java.lang.Integer):void} */
    @RequiresPermission(Permission.WRITE_CALL_LOG)
    public boolean test() throws Throwable {
        try {
            ContentValues content = new ContentValues();
            content.put("type", (Integer) 1);
            content.put("number", "1");
            content.put(ResponseBase.STRING_DATE, (Integer) 20080808);
            content.put(AmapLoc.TYPE_NEW, "0");
            return ContentUris.parseId(this.mResolver.insert(CallLog.Calls.CONTENT_URI, content)) > 0;
        } finally {
            this.mResolver.delete(CallLog.Calls.CONTENT_URI, "number=?", new String[]{"1"});
        }
    }
}
