package com.google.android.gms.dynamic;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import com.google.android.gms.common.annotation.KeepForSdk;
import com.google.android.gms.dynamic.IFragmentWrapper;

@KeepForSdk
public final class SupportFragmentWrapper extends IFragmentWrapper.Stub {
    private Fragment zzie;

    @KeepForSdk
    public static SupportFragmentWrapper wrap(Fragment fragment) {
        if (fragment != null) {
            return new SupportFragmentWrapper(fragment);
        }
        return null;
    }

    private SupportFragmentWrapper(Fragment fragment) {
        this.zzie = fragment;
    }

    public final IObjectWrapper zzae() {
        return ObjectWrapper.wrap(this.zzie.getActivity());
    }

    public final Bundle getArguments() {
        return this.zzie.getArguments();
    }

    public final int getId() {
        return this.zzie.getId();
    }

    public final IFragmentWrapper zzaf() {
        return wrap(this.zzie.getParentFragment());
    }

    public final IObjectWrapper zzag() {
        return ObjectWrapper.wrap(this.zzie.getResources());
    }

    public final boolean getRetainInstance() {
        return this.zzie.getRetainInstance();
    }

    public final String getTag() {
        return this.zzie.getTag();
    }

    public final IFragmentWrapper zzah() {
        return wrap(this.zzie.getTargetFragment());
    }

    public final int getTargetRequestCode() {
        return this.zzie.getTargetRequestCode();
    }

    public final boolean getUserVisibleHint() {
        return this.zzie.getUserVisibleHint();
    }

    public final IObjectWrapper zzai() {
        return ObjectWrapper.wrap(this.zzie.getView());
    }

    public final boolean isAdded() {
        return this.zzie.isAdded();
    }

    public final boolean isDetached() {
        return this.zzie.isDetached();
    }

    public final boolean isHidden() {
        return this.zzie.isHidden();
    }

    public final boolean isInLayout() {
        return this.zzie.isInLayout();
    }

    public final boolean isRemoving() {
        return this.zzie.isRemoving();
    }

    public final boolean isResumed() {
        return this.zzie.isResumed();
    }

    public final boolean isVisible() {
        return this.zzie.isVisible();
    }

    public final void zza(IObjectWrapper iObjectWrapper) {
        this.zzie.registerForContextMenu((View) ObjectWrapper.unwrap(iObjectWrapper));
    }

    public final void setHasOptionsMenu(boolean z) {
        this.zzie.setHasOptionsMenu(z);
    }

    public final void setMenuVisibility(boolean z) {
        this.zzie.setMenuVisibility(z);
    }

    public final void setRetainInstance(boolean z) {
        this.zzie.setRetainInstance(z);
    }

    public final void setUserVisibleHint(boolean z) {
        this.zzie.setUserVisibleHint(z);
    }

    public final void startActivity(Intent intent) {
        this.zzie.startActivity(intent);
    }

    public final void startActivityForResult(Intent intent, int i) {
        this.zzie.startActivityForResult(intent, i);
    }

    public final void zzb(IObjectWrapper iObjectWrapper) {
        this.zzie.unregisterForContextMenu((View) ObjectWrapper.unwrap(iObjectWrapper));
    }
}
