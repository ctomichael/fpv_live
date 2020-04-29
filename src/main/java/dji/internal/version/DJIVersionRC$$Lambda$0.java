package dji.internal.version;

import dji.internal.version.DJIVersionRC;

final /* synthetic */ class DJIVersionRC$$Lambda$0 implements Runnable {
    private final DJIVersionRC arg$1;
    private final DJIVersionRC.RcVersionListenerOnce arg$2;

    DJIVersionRC$$Lambda$0(DJIVersionRC dJIVersionRC, DJIVersionRC.RcVersionListenerOnce rcVersionListenerOnce) {
        this.arg$1 = dJIVersionRC;
        this.arg$2 = rcVersionListenerOnce;
    }

    public void run() {
        this.arg$1.lambda$getVersion$0$DJIVersionRC(this.arg$2);
    }
}
