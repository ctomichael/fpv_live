package com.dji.mapkit.lbs.view;

import android.app.Activity;
import android.app.Application;
import android.app.Dialog;
import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import java.lang.ref.WeakReference;

public class ContextProcessor {
    private Context applicationContext;
    private WeakReference<Activity> weakActivity;
    private WeakReference<Dialog> weakDialog;
    private WeakReference<Fragment> weakFragment;

    public ContextProcessor(Context context) {
        if (!(context instanceof Application)) {
            throw new IllegalArgumentException("ContextProcessor can only be initialized with Application!");
        }
        this.applicationContext = context;
        this.weakActivity = new WeakReference<>(null);
        this.weakFragment = new WeakReference<>(null);
        this.weakDialog = new WeakReference<>(null);
    }

    public ContextProcessor setActivity(Activity activity) {
        this.weakActivity = new WeakReference<>(activity);
        this.weakFragment = new WeakReference<>(null);
        return this;
    }

    public ContextProcessor setFragment(Fragment fragment) {
        this.weakActivity = new WeakReference<>(null);
        this.weakFragment = new WeakReference<>(fragment);
        return this;
    }

    public ContextProcessor setDialog(Dialog dialog) {
        this.weakDialog = new WeakReference<>(dialog);
        return this;
    }

    @Nullable
    public Fragment getFragment() {
        return this.weakFragment.get();
    }

    @Nullable
    public Activity getActivity() {
        if (this.weakActivity.get() != null) {
            return this.weakActivity.get();
        }
        if (this.weakFragment.get() == null || this.weakFragment.get().getActivity() == null) {
            return null;
        }
        return this.weakFragment.get().getActivity();
    }

    @Nullable
    public Dialog getDialog() {
        return this.weakDialog.get();
    }

    public Context getContext() {
        return this.applicationContext;
    }
}
