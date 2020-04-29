package dji.common.util;

import dji.fieldAnnotation.EXClassNullAway;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

@EXClassNullAway
public class LatchHelper {
    private static LatchHelper uniqueInstance = null;
    private CountDownLatch latch = null;

    public static synchronized LatchHelper getInstance() {
        LatchHelper latchHelper;
        synchronized (LatchHelper.class) {
            if (uniqueInstance == null) {
                uniqueInstance = new LatchHelper();
            }
            latchHelper = uniqueInstance;
        }
        return latchHelper;
    }

    public synchronized void setUpLatch(int count) {
        this.latch = new CountDownLatch(count);
    }

    public void countDownLatch() {
        if (this.latch != null) {
            this.latch.countDown();
        }
    }

    public void waitForLatch(long seconds) {
        try {
            this.latch.await(seconds, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
