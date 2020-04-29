package dji.midware.util;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class ResettableCountDownLatch {
    private final int initialCount;
    private volatile CountDownLatch latch;

    public ResettableCountDownLatch(int count) {
        this.initialCount = count;
        this.latch = new CountDownLatch(count);
    }

    public void reset() {
        this.latch = new CountDownLatch(this.initialCount);
    }

    public void countDown() {
        this.latch.countDown();
    }

    public void await() throws InterruptedException {
        this.latch.await();
    }

    public boolean await(long timeout, TimeUnit unit) throws InterruptedException {
        return this.latch.await(timeout, unit);
    }
}
