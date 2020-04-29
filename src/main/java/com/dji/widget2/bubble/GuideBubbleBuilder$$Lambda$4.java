package com.dji.widget2.bubble;

import android.view.View;

final /* synthetic */ class GuideBubbleBuilder$$Lambda$4 implements View.OnLayoutChangeListener {
    private final GuideBubbleBuilder arg$1;
    private final Runnable arg$2;

    GuideBubbleBuilder$$Lambda$4(GuideBubbleBuilder guideBubbleBuilder, Runnable runnable) {
        this.arg$1 = guideBubbleBuilder;
        this.arg$2 = runnable;
    }

    public void onLayoutChange(View view, int i, int i2, int i3, int i4, int i5, int i6, int i7, int i8) {
        this.arg$1.lambda$null$3$GuideBubbleBuilder(this.arg$2, view, i, i2, i3, i4, i5, i6, i7, i8);
    }
}
