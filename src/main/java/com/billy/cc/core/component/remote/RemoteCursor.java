package com.billy.cc.core.component.remote;

import android.database.Cursor;
import android.database.MatrixCursor;
import android.os.Bundle;
import android.os.IBinder;
import com.billy.cc.core.component.RemoteCCService;
import com.billy.cc.core.component.remote.IRemoteCCService;

public class RemoteCursor extends MatrixCursor {
    static final String[] DEFAULT_COLUMNS = {"cc"};
    private static final String KEY_BINDER_WRAPPER = "BinderWrapper";
    private Bundle binderExtras;

    private static class CCCursorHolder {
        /* access modifiers changed from: private */
        public static final RemoteCursor INSTANCE = new RemoteCursor(RemoteCursor.DEFAULT_COLUMNS, RemoteCCService.getInstance());

        private CCCursorHolder() {
        }
    }

    private RemoteCursor(String[] columnNames, IBinder binder) {
        super(columnNames);
        this.binderExtras = new Bundle();
        this.binderExtras.putParcelable(KEY_BINDER_WRAPPER, new BinderWrapper(binder));
    }

    public static RemoteCursor getInstance() {
        return CCCursorHolder.INSTANCE;
    }

    public Bundle getExtras() {
        return this.binderExtras;
    }

    public static IRemoteCCService getRemoteCCService(Cursor cursor) {
        if (cursor == null) {
            return null;
        }
        Bundle bundle = cursor.getExtras();
        bundle.setClassLoader(BinderWrapper.class.getClassLoader());
        BinderWrapper binderWrapper = (BinderWrapper) bundle.getParcelable(KEY_BINDER_WRAPPER);
        if (binderWrapper != null) {
            return IRemoteCCService.Stub.asInterface(binderWrapper.getBinder());
        }
        return null;
    }
}
