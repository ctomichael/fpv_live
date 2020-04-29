package com.dji.permission.checker;

import android.database.Cursor;

interface PermissionTest {
    boolean test() throws Throwable;

    public static class CursorTest {
        public static void read(Cursor cursor) {
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                switch (cursor.getType(0)) {
                    case 0:
                    case 4:
                        return;
                    default:
                        cursor.getString(0);
                        return;
                }
            }
        }
    }
}
