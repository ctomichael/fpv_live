package com.google.android.gms.dynamic;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import com.google.android.gms.common.annotation.KeepForSdk;
import com.google.android.gms.dynamic.IFragmentWrapper;

@SuppressLint({"NewApi"})
@KeepForSdk
public final class FragmentWrapper extends IFragmentWrapper.Stub {
    private Fragment zzia;

    @KeepForSdk
    public static FragmentWrapper wrap(Fragment fragment) {
        if (fragment != null) {
            return new FragmentWrapper(fragment);
        }
        return null;
    }

    private FragmentWrapper(Fragment fragment) {
        this.zzia = fragment;
    }

    public final IObjectWrapper zzae() {
        return ObjectWrapper.wrap(this.zzia.getActivity());
    }

    public final Bundle getArguments() {
        return this.zzia.getArguments();
    }

    public final int getId() {
        return this.zzia.getId();
    }

    public final IFragmentWrapper zzaf() {
        return wrap(this.zzia.getParentFragment());
    }

    public final IObjectWrapper zzag() {
        return ObjectWrapper.wrap(this.zzia.getResources());
    }

    public final boolean getRetainInstance() {
        return this.zzia.getRetainInstance();
    }

    public final String getTag() {
        return this.zzia.getTag();
    }

    public final IFragmentWrapper zzah() {
        return wrap(this.zzia.getTargetFragment());
    }

    public final int getTargetRequestCode() {
        return this.zzia.getTargetRequestCode();
    }

    public final boolean getUserVisibleHint() {
        return this.zzia.getUserVisibleHint();
    }

    public final IObjectWrapper zzai() {
        return ObjectWrapper.wrap(this.zzia.getView());
    }

    public final boolean isAdded() {
        return this.zzia.isAdded();
    }

    public final boolean isDetached() {
        return this.zzia.isDetached();
    }

    public final boolean isHidden() {
        return this.zzia.isHidden();
    }

    public final boolean isInLayout() {
        return this.zzia.isInLayout();
    }

    public final boolean isRemoving() {
        return this.zzia.isRemoving();
    }

    public final boolean isResumed() {
        return this.zzia.isResumed();
    }

    public final boolean isVisible() {
        return this.zzia.isVisible();
    }

    public final void zza(IObjectWrapper iObjectWrapper) {
        this.zzia.registerForContextMenu((View) ObjectWrapper.unwrap(iObjectWrapper));
    }

    public final void setHasOptionsMenu(boolean z) {
        this.zzia.setHasOptionsMenu(z);
    }

    public final void setMenuVisibility(boolean z) {
        this.zzia.setMenuVisibility(z);
    }

    public final void setRetainInstance(boolean z) {
        this.zzia.setRetainInstance(z);
    }

    public final void setUserVisibleHint(boolean z) {
        this.zzia.setUserVisibleHint(z);
    }

    public final void startActivity(Intent intent) {
        this.zzia.startActivity(intent);
    }

    public final void startActivityForResult(Intent intent, int i) {
        this.zzia.startActivityForResult(intent, i);
    }

    public final void zzb(IObjectWrapper iObjectWrapper) {
        this.zzia.unregisterForContextMenu((View) ObjectWrapper.unwrap(iObjectWrapper));
    }
}
