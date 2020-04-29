package com.dji.permission.source;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;

public class FragmentSource extends Source {
    private Fragment mFragment;

    public FragmentSource(Fragment fragment) {
        this.mFragment = fragment;
    }

    public Context getContext() {
        return this.mFragment.getActivity();
    }

    public void startActivity(Intent intent) {
        this.mFragment.startActivity(intent);
    }

    public void startActivityForResult(Intent intent, int requestCode) {
        this.mFragment.startActivityForResult(intent, requestCode);
    }
}
