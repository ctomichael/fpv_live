package com.google.android.gms.dynamic;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import com.google.android.gms.dynamic.DeferredLifecycleHelper;

final class zad implements DeferredLifecycleHelper.zaa {
    private final /* synthetic */ ViewGroup val$container;
    private final /* synthetic */ DeferredLifecycleHelper zari;
    private final /* synthetic */ Bundle zark;
    private final /* synthetic */ FrameLayout zarl;
    private final /* synthetic */ LayoutInflater zarm;

    zad(DeferredLifecycleHelper deferredLifecycleHelper, FrameLayout frameLayout, LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        this.zari = deferredLifecycleHelper;
        this.zarl = frameLayout;
        this.zarm = layoutInflater;
        this.val$container = viewGroup;
        this.zark = bundle;
    }

    public final int getState() {
        return 2;
    }

    public final void zaa(LifecycleDelegate lifecycleDelegate) {
        this.zarl.removeAllViews();
        this.zarl.addView(this.zari.zare.onCreateView(this.zarm, this.val$container, this.zark));
    }
}
