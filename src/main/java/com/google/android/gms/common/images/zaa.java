package com.google.android.gms.common.images;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import com.google.android.gms.common.internal.Asserts;
import com.google.android.gms.internal.base.zak;

public abstract class zaa {
    final zab zamu;
    private int zamv = 0;
    protected int zamw = 0;
    private boolean zamx = false;
    private boolean zamy = true;
    private boolean zamz = false;
    private boolean zana = true;

    public zaa(Uri uri, int i) {
        this.zamu = new zab(uri);
        this.zamw = i;
    }

    /* access modifiers changed from: protected */
    public abstract void zaa(Drawable drawable, boolean z, boolean z2, boolean z3);

    /* access modifiers changed from: package-private */
    public final void zaa(Context context, Bitmap bitmap, boolean z) {
        Asserts.checkNotNull(bitmap);
        zaa(new BitmapDrawable(context.getResources(), bitmap), z, false, true);
    }

    /* access modifiers changed from: package-private */
    public final void zaa(Context context, zak zak) {
        if (this.zana) {
            zaa(null, false, true, false);
        }
    }

    /* access modifiers changed from: package-private */
    public final void zaa(Context context, zak zak, boolean z) {
        Drawable drawable = null;
        if (this.zamw != 0) {
            drawable = context.getResources().getDrawable(this.zamw);
        }
        zaa(drawable, z, false, false);
    }

    /* access modifiers changed from: protected */
    public final boolean zaa(boolean z, boolean z2) {
        return this.zamy && !z2 && !z;
    }
}
