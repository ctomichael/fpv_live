package com.dji.permission.source;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

public class SupportFragmentSource extends Source {
    private Fragment mFragment;

    public SupportFragmentSource(Fragment fragment) {
        this.mFragment = fragment;
    }

    public Context getContext() {
        return this.mFragment.getContext();
    }

    public void startActivity(Intent intent) {
        this.mFragment.startActivity(intent);
    }

    public void startActivityForResult(Intent intent, int requestCode) {
        this.mFragment.startActivityForResult(intent, requestCode);
    }
}
