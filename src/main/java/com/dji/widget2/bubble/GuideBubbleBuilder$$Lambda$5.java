package com.dji.widget2.bubble;

import android.view.View;

final /* synthetic */ class GuideBubbleBuilder$$Lambda$5 implements View.OnClickListener {
    private final GuideBubbleBuilder arg$1;
    private final MaskView arg$2;
    private final View.OnLayoutChangeListener arg$3;

    GuideBubbleBuilder$$Lambda$5(GuideBubbleBuilder guideBubbleBuilder, MaskView maskView, View.OnLayoutChangeListener onLayoutChangeListener) {
        this.arg$1 = guideBubbleBuilder;
        this.arg$2 = maskView;
        this.arg$3 = onLayoutChangeListener;
    }

    public void onClick(View view) {
        this.arg$1.lambda$null$4$GuideBubbleBuilder(this.arg$2, this.arg$3, view);
    }
}
