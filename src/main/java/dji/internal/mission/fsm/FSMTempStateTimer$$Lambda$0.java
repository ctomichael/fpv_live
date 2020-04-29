package dji.internal.mission.fsm;

import dji.thirdparty.rx.functions.Action1;

final /* synthetic */ class FSMTempStateTimer$$Lambda$0 implements Action1 {
    private final Runnable arg$1;

    FSMTempStateTimer$$Lambda$0(Runnable runnable) {
        this.arg$1 = runnable;
    }

    public void call(Object obj) {
        this.arg$1.run();
    }
}
