package com.dji.widget2.wheel;

import android.media.SoundPool;

final /* synthetic */ class DJIWheelView$$Lambda$0 implements SoundPool.OnLoadCompleteListener {
    private final DJIWheelView arg$1;

    DJIWheelView$$Lambda$0(DJIWheelView dJIWheelView) {
        this.arg$1 = dJIWheelView;
    }

    public void onLoadComplete(SoundPool soundPool, int i, int i2) {
        this.arg$1.lambda$onAttachedToWindow$0$DJIWheelView(soundPool, i, i2);
    }
}
