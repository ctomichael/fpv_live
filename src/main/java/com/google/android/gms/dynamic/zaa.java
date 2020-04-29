package com.google.android.gms.dynamic;

import android.os.Bundle;
import com.google.android.gms.dynamic.DeferredLifecycleHelper;
import java.util.Iterator;

final class zaa implements OnDelegateCreatedListener<T> {
    private final /* synthetic */ DeferredLifecycleHelper zari;

    zaa(DeferredLifecycleHelper deferredLifecycleHelper) {
        this.zari = deferredLifecycleHelper;
    }

    /* JADX DEBUG: Failed to find minimal casts for resolve overloaded methods, cast all args instead
     method: com.google.android.gms.dynamic.DeferredLifecycleHelper.zaa(com.google.android.gms.dynamic.DeferredLifecycleHelper, com.google.android.gms.dynamic.LifecycleDelegate):com.google.android.gms.dynamic.LifecycleDelegate
     arg types: [com.google.android.gms.dynamic.DeferredLifecycleHelper, T]
     candidates:
      com.google.android.gms.dynamic.DeferredLifecycleHelper.zaa(com.google.android.gms.dynamic.DeferredLifecycleHelper, android.os.Bundle):android.os.Bundle
      com.google.android.gms.dynamic.DeferredLifecycleHelper.zaa(android.os.Bundle, com.google.android.gms.dynamic.DeferredLifecycleHelper$zaa):void
      com.google.android.gms.dynamic.DeferredLifecycleHelper.zaa(com.google.android.gms.dynamic.DeferredLifecycleHelper, com.google.android.gms.dynamic.LifecycleDelegate):com.google.android.gms.dynamic.LifecycleDelegate */
    public final void onDelegateCreated(T t) {
        LifecycleDelegate unused = this.zari.zare = (LifecycleDelegate) t;
        Iterator it2 = this.zari.zarg.iterator();
        while (it2.hasNext()) {
            ((DeferredLifecycleHelper.zaa) it2.next()).zaa(this.zari.zare);
        }
        this.zari.zarg.clear();
        Bundle unused2 = this.zari.zarf = null;
    }
}
