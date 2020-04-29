package com.dji.component.fpv.widget.preview;

import dji.log.DJILog;
import io.reactivex.functions.Consumer;

final /* synthetic */ class PreviewHelper$$Lambda$1 implements Consumer {
    static final Consumer $instance = new PreviewHelper$$Lambda$1();

    private PreviewHelper$$Lambda$1() {
    }

    public void accept(Object obj) {
        DJILog.logWriteD(PreviewHelper.TAG, "resetSignal", new Object[0]);
    }
}
