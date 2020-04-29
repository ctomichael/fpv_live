package com.dji.permission.source;

import android.content.Context;
import android.content.Intent;

public class ContextSource extends Source {
    private Context mContext;

    public ContextSource(Context context) {
        this.mContext = context;
    }

    public Context getContext() {
        return this.mContext;
    }

    public void startActivity(Intent intent) {
        this.mContext.startActivity(intent);
    }

    public void startActivityForResult(Intent intent, int requestCode) {
        this.mContext.startActivity(intent);
    }
}
