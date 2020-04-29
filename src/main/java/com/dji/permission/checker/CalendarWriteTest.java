package com.dji.permission.checker;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.provider.CalendarContract;
import android.support.annotation.RequiresApi;
import com.mapbox.mapboxsdk.style.layers.Property;
import dji.publics.protocol.ResponseBase;
import java.util.TimeZone;

class CalendarWriteTest implements PermissionTest {
    private static final String ACCOUNT = "permission@gmail.com";
    private static final String NAME = "PERMISSION";
    private ContentResolver mResolver;

    CalendarWriteTest(Context context) {
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
            TimeZone timeZone = TimeZone.getDefault();
            ContentValues value = new ContentValues();
            value.put("name", NAME);
            value.put("account_name", ACCOUNT);
            value.put(ResponseBase.STRING_ACCOUNT_TYPE, "LOCAL");
            value.put("calendar_displayName", NAME);
            value.put(Property.VISIBLE, (Integer) 1);
            value.put("calendar_color", (Integer) -16776961);
            value.put("calendar_access_level", (Integer) 700);
            value.put("sync_events", (Integer) 1);
            value.put("calendar_timezone", timeZone.getID());
            value.put("ownerAccount", NAME);
            value.put("canOrganizerRespond", (Integer) 0);
            return ContentUris.parseId(this.mResolver.insert(CalendarContract.Calendars.CONTENT_URI.buildUpon().appendQueryParameter("caller_is_syncadapter", "true").appendQueryParameter("account_name", NAME).appendQueryParameter(ResponseBase.STRING_ACCOUNT_TYPE, "LOCAL").build(), value)) > 0;
        } finally {
            this.mResolver.delete(CalendarContract.Calendars.CONTENT_URI.buildUpon().build(), "account_name=?", new String[]{ACCOUNT});
        }
    }
}
