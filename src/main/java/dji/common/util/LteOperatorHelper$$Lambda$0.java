package dji.common.util;

final /* synthetic */ class LteOperatorHelper$$Lambda$0 implements Runnable {
    private final LteOperatorHelper arg$1;
    private final int arg$2;
    private final int arg$3;

    LteOperatorHelper$$Lambda$0(LteOperatorHelper lteOperatorHelper, int i, int i2) {
        this.arg$1 = lteOperatorHelper;
        this.arg$2 = i;
        this.arg$3 = i2;
    }

    public void run() {
        this.arg$1.lambda$syncNameFromLocate$0$LteOperatorHelper(this.arg$2, this.arg$3);
    }
}
