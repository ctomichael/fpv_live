package com.dji.component.fpv.base;

import com.dji.component.fpv.base.FpvConstant;
import io.reactivex.functions.Consumer;

final /* synthetic */ class FpvConstant$QuickShot$$Lambda$1 implements Consumer {
    static final Consumer $instance = new FpvConstant$QuickShot$$Lambda$1();

    private FpvConstant$QuickShot$$Lambda$1() {
    }

    public void accept(Object obj) {
        FpvConstant.QuickShot.resetQuickShotDistance();
    }
}
