package com.dji.findmydrone.ui;

import dji.service.IDJIService;
import java.util.Comparator;

final /* synthetic */ class DebugApp$$Lambda$0 implements Comparator {
    static final Comparator $instance = new DebugApp$$Lambda$0();

    private DebugApp$$Lambda$0() {
    }

    public int compare(Object obj, Object obj2) {
        return DebugApp.lambda$initAppService$0$DebugApp((IDJIService) obj, (IDJIService) obj2);
    }
}
