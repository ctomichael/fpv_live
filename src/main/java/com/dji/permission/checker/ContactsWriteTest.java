package com.dji.permission.checker;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.database.Cursor;
import android.provider.ContactsContract;

class ContactsWriteTest implements PermissionTest {
    private static final String DISPLAY_NAME = "PERMISSION";
    private ContentResolver mResolver;

    ContactsWriteTest(ContentResolver resolver) {
        this.mResolver = resolver;
    }

    public boolean test() throws Throwable {
        Cursor cursor = this.mResolver.query(ContactsContract.Data.CONTENT_URI, new String[]{"raw_contact_id"}, "mimetype=? and data1=?", new String[]{"vnd.android.cursor.item/name", DISPLAY_NAME}, null);
        if (cursor == null) {
            return false;
        }
        if (cursor.moveToFirst()) {
            long rawContactId = cursor.getLong(0);
            cursor.close();
            return update(rawContactId);
        }
        cursor.close();
        return insert();
    }

    private boolean insert() {
        ContentValues values = new ContentValues();
        values.put("raw_contact_id", Long.valueOf(ContentUris.parseId(this.mResolver.insert(ContactsContract.RawContacts.CONTENT_URI, values))));
        values.put("data1", DISPLAY_NAME);
        values.put("data2", DISPLAY_NAME);
        values.put("mimetype", "vnd.android.cursor.item/name");
        return ContentUris.parseId(this.mResolver.insert(ContactsContract.Data.CONTENT_URI, values)) > 0;
    }

    private void delete(long contactId, long dataId) {
        this.mResolver.delete(ContactsContract.RawContacts.CONTENT_URI, "_id=?", new String[]{Long.toString(contactId)});
        this.mResolver.delete(ContactsContract.Data.CONTENT_URI, "_id=?", new String[]{Long.toString(dataId)});
    }

    private boolean update(long rawContactId) {
        ContentValues values = new ContentValues();
        values.put("raw_contact_id", Long.valueOf(rawContactId));
        values.put("data1", DISPLAY_NAME);
        values.put("data2", DISPLAY_NAME);
        values.put("mimetype", "vnd.android.cursor.item/name");
        return ContentUris.parseId(this.mResolver.insert(ContactsContract.Data.CONTENT_URI, values)) > 0;
    }
}
