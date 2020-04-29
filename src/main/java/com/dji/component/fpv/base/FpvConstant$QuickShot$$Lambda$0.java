package com.dji.component.fpv.base;

import dji.utils.Optional;
import io.reactivex.functions.Function;

final /* synthetic */ class FpvConstant$QuickShot$$Lambda$0 implements Function {
    static final Function $instance = new FpvConstant$QuickShot$$Lambda$0();

    private FpvConstant$QuickShot$$Lambda$0() {
    }

    public Object apply(Object obj) {
        return ((Optional) obj).get();
    }
}
