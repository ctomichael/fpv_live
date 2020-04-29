package com.dji.permission.checker;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.provider.VoicemailContract;
import android.support.annotation.RequiresApi;
import android.text.TextUtils;
import dji.publics.protocol.ResponseBase;

class AddVoicemailTest implements PermissionTest {
    private ContentResolver mResolver;

    AddVoicemailTest(Context context) {
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
    @RequiresApi(api = 14)
    public boolean test() throws Throwable {
        try {
            Uri mBaseUri = VoicemailContract.Voicemails.CONTENT_URI;
            ContentValues contentValues = new ContentValues();
            contentValues.put(ResponseBase.STRING_DATE, Long.valueOf(System.currentTimeMillis()));
            contentValues.put("number", "1");
            contentValues.put("duration", (Integer) 1);
            contentValues.put("source_package", "permission");
            contentValues.put("source_data", "permission");
            contentValues.put("is_read", (Integer) 0);
            long id = ContentUris.parseId(this.mResolver.insert(mBaseUri, contentValues));
            if (this.mResolver.delete(mBaseUri, "_id=?", new String[]{Long.toString(id)}) > 0) {
                return true;
            }
            return false;
        } catch (Exception e) {
            String message = e.getMessage();
            if (TextUtils.isEmpty(message)) {
                return false;
            }
            if (message.toLowerCase().contains("add_voicemail")) {
                return false;
            }
            return true;
        }
    }
}
