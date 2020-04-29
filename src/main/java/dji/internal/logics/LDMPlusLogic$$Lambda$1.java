package dji.internal.logics;

import dji.internal.logics.LDMPlusLogic;

final /* synthetic */ class LDMPlusLogic$$Lambda$1 implements Runnable {
    private final LDMPlusLogic arg$1;
    private final LDMPlusLogic.LDMPlusListener arg$2;

    LDMPlusLogic$$Lambda$1(LDMPlusLogic lDMPlusLogic, LDMPlusLogic.LDMPlusListener lDMPlusListener) {
        this.arg$1 = lDMPlusLogic;
        this.arg$2 = lDMPlusListener;
    }

    public void run() {
        this.arg$1.lambda$null$0$LDMPlusLogic(this.arg$2);
    }
}
