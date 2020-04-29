package com.dji.permission.source;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

public class AppActivitySource extends Source {
    private Activity mActivity;

    public AppActivitySource(Activity activity) {
        this.mActivity = activity;
    }

    public Context getContext() {
        return this.mActivity;
    }

    public void startActivity(Intent intent) {
        this.mActivity.startActivity(intent);
    }

    public void startActivityForResult(Intent intent, int requestCode) {
        this.mActivity.startActivityForResult(intent, requestCode);
    }
}
