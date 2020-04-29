package dji.thirdparty.io.reactivex.schedulers;

import dji.thirdparty.io.reactivex.Scheduler;
import dji.thirdparty.io.reactivex.internal.schedulers.ComputationScheduler;
import dji.thirdparty.io.reactivex.internal.schedulers.ExecutorScheduler;
import dji.thirdparty.io.reactivex.internal.schedulers.IoScheduler;
import dji.thirdparty.io.reactivex.internal.schedulers.NewThreadScheduler;
import dji.thirdparty.io.reactivex.internal.schedulers.SchedulerPoolFactory;
import dji.thirdparty.io.reactivex.internal.schedulers.SingleScheduler;
import dji.thirdparty.io.reactivex.internal.schedulers.TrampolineScheduler;
import dji.thirdparty.io.reactivex.plugins.RxJavaPlugins;
import java.util.concurrent.Callable;
import java.util.concurrent.Executor;

public final class Schedulers {
    static final Scheduler COMPUTATION = RxJavaPlugins.initComputationScheduler(new Callable<Scheduler>() {
        /* class dji.thirdparty.io.reactivex.schedulers.Schedulers.AnonymousClass2 */

        public Scheduler call() throws Exception {
            return ComputationHolder.DEFAULT;
        }
    });
    static final Scheduler IO = RxJavaPlugins.initIoScheduler(new Callable<Scheduler>() {
        /* class dji.thirdparty.io.reactivex.schedulers.Schedulers.AnonymousClass3 */

        public Scheduler call() throws Exception {
            return IoHolder.DEFAULT;
        }
    });
    static final Scheduler NEW_THREAD = RxJavaPlugins.initNewThreadScheduler(new Callable<Scheduler>() {
        /* class dji.thirdparty.io.reactivex.schedulers.Schedulers.AnonymousClass4 */

        public Scheduler call() throws Exception {
            return NewThreadHolder.DEFAULT;
        }
    });
    static final Scheduler SINGLE = RxJavaPlugins.initSingleScheduler(new Callable<Scheduler>() {
        /* class dji.thirdparty.io.reactivex.schedulers.Schedulers.AnonymousClass1 */

        public Scheduler call() throws Exception {
            return SingleHolder.DEFAULT;
        }
    });
    static final Scheduler TRAMPOLINE = TrampolineScheduler.instance();

    static final class SingleHolder {
        static final Scheduler DEFAULT = new SingleScheduler();

        SingleHolder() {
        }
    }

    static final class ComputationHolder {
        static final Scheduler DEFAULT = new ComputationScheduler();

        ComputationHolder() {
        }
    }

    static final class IoHolder {
        static final Scheduler DEFAULT = new IoScheduler();

        IoHolder() {
        }
    }

    static final class NewThreadHolder {
        static final Scheduler DEFAULT = NewThreadScheduler.instance();

        NewThreadHolder() {
        }
    }

    private Schedulers() {
        throw new IllegalStateException("No instances!");
    }

    public static Scheduler computation() {
        return RxJavaPlugins.onComputationScheduler(COMPUTATION);
    }

    public static Scheduler io() {
        return RxJavaPlugins.onIoScheduler(IO);
    }

    public static Scheduler trampoline() {
        return TRAMPOLINE;
    }

    public static Scheduler newThread() {
        return RxJavaPlugins.onNewThreadScheduler(NEW_THREAD);
    }

    public static Scheduler single() {
        return RxJavaPlugins.onSingleScheduler(SINGLE);
    }

    public static Scheduler from(Executor executor) {
        return new ExecutorScheduler(executor);
    }

    public static void shutdown() {
        computation().shutdown();
        io().shutdown();
        newThread().shutdown();
        single().shutdown();
        trampoline().shutdown();
        SchedulerPoolFactory.shutdown();
    }

    public static void start() {
        computation().start();
        io().start();
        newThread().start();
        single().start();
        trampoline().start();
        SchedulerPoolFactory.start();
    }
}
