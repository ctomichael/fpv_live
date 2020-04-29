package com.billy.cc.core.component.remote;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Binder;
import android.os.Process;
import com.billy.cc.core.component.CC;

public class RemoteProvider extends ContentProvider {
    public static final String[] PROJECTION_MAIN = {"cc"};
    public static final String URI_SUFFIX = "com.billy.cc.core.remote";

    public boolean onCreate() {
        CC.log("RemoteProvider onCreated! class:%s", getClass().getName());
        return false;
    }

    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        if (CC.isRemoteCCEnabled() || Binder.getCallingUid() == Process.myUid()) {
            return RemoteCursor.getInstance();
        }
        return null;
    }

    public String getType(Uri uri) {
        return null;
    }

    public Uri insert(Uri uri, ContentValues values) {
        return null;
    }

    public int delete(Uri uri, String selection, String[] selectionArgs) {
        return 0;
    }

    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return 0;
    }
}
