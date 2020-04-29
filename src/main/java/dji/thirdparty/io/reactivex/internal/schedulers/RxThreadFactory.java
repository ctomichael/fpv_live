package dji.thirdparty.io.reactivex.internal.schedulers;

import dji.component.accountcenter.IMemberProtocol;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicLong;

public final class RxThreadFactory extends AtomicLong implements ThreadFactory {
    private static final long serialVersionUID = -7789753024099756196L;
    final String prefix;
    final int priority;

    public RxThreadFactory(String prefix2) {
        this(prefix2, 5);
    }

    public RxThreadFactory(String prefix2, int priority2) {
        this.prefix = prefix2;
        this.priority = priority2;
    }

    public Thread newThread(Runnable r) {
        Thread t = new Thread(r, this.prefix + '-' + incrementAndGet());
        t.setPriority(this.priority);
        t.setDaemon(true);
        return t;
    }

    public String toString() {
        return "RxThreadFactory[" + this.prefix + IMemberProtocol.STRING_SEPERATOR_RIGHT;
    }
}
