package com.dji.component.fpv.widget.histogram;

import dji.utils.Optional;
import io.reactivex.functions.Consumer;

final /* synthetic */ class HistogramShell$$Lambda$1 implements Consumer {
    private final HistogramShell arg$1;

    HistogramShell$$Lambda$1(HistogramShell histogramShell) {
        this.arg$1 = histogramShell;
    }

    public void accept(Object obj) {
        this.arg$1.lambda$onAttachedToWindow$1$HistogramShell((Optional) obj);
    }
}
