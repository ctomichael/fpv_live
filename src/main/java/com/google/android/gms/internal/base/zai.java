package com.google.android.gms.internal.base;

import android.graphics.drawable.Drawable;

final class zai extends Drawable.ConstantState {
    int mChangingConfigurations;
    int zanv;

    zai(zai zai) {
        if (zai != null) {
            this.mChangingConfigurations = zai.mChangingConfigurations;
            this.zanv = zai.zanv;
        }
    }

    public final Drawable newDrawable() {
        return new zae(this);
    }

    public final int getChangingConfigurations() {
        return this.mChangingConfigurations;
    }
}
