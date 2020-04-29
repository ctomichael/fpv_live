package dji.thirdparty.rx.schedulers;

import dji.thirdparty.rx.Scheduler;
import dji.thirdparty.rx.internal.schedulers.NewThreadWorker;
import dji.thirdparty.rx.internal.util.RxThreadFactory;

public final class NewThreadScheduler extends Scheduler {
    private static final NewThreadScheduler INSTANCE = new NewThreadScheduler();
    private static final RxThreadFactory THREAD_FACTORY = new RxThreadFactory(THREAD_NAME_PREFIX);
    private static final String THREAD_NAME_PREFIX = "RxNewThreadScheduler-";

    static NewThreadScheduler instance() {
        return INSTANCE;
    }

    private NewThreadScheduler() {
    }

    public Scheduler.Worker createWorker() {
        return new NewThreadWorker(THREAD_FACTORY);
    }
}
