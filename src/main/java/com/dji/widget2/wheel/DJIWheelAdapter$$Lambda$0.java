package com.dji.widget2.wheel;

import android.view.View;

final /* synthetic */ class DJIWheelAdapter$$Lambda$0 implements View.OnClickListener {
    private final DJIWheelAdapter arg$1;
    private final int arg$2;

    DJIWheelAdapter$$Lambda$0(DJIWheelAdapter dJIWheelAdapter, int i) {
        this.arg$1 = dJIWheelAdapter;
        this.arg$2 = i;
    }

    public void onClick(View view) {
        this.arg$1.lambda$onBindViewHolder$0$DJIWheelAdapter(this.arg$2, view);
    }
}
