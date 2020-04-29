package dji.component.notification;

import dji.utils.function.Consumer;
import dji.utils.function.Consumer$$CC;

final /* synthetic */ class DJITip$$Lambda$0 implements Consumer {
    static final Consumer $instance = new DJITip$$Lambda$0();

    private DJITip$$Lambda$0() {
    }

    public void accept(Object obj) {
        DJITip.lambda$static$0$DJITip(obj);
    }

    public Consumer andThen(Consumer consumer) {
        return Consumer$$CC.andThen(this, consumer);
    }
}
