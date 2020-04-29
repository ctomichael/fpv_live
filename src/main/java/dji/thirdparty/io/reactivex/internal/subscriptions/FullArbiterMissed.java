package dji.thirdparty.io.reactivex.internal.subscriptions;

import java.util.concurrent.atomic.AtomicLong;

/* compiled from: FullArbiter */
class FullArbiterMissed extends FullArbiterPad1 {
    final AtomicLong missedRequested = new AtomicLong();

    FullArbiterMissed() {
    }
}
