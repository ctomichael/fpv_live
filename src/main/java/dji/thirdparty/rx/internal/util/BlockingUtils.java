package dji.thirdparty.rx.internal.util;

import dji.thirdparty.rx.Subscription;
import dji.thirdparty.rx.annotations.Experimental;
import java.util.concurrent.CountDownLatch;

@Experimental
public final class BlockingUtils {
    private BlockingUtils() {
    }

    @Experimental
    public static void awaitForComplete(CountDownLatch latch, Subscription subscription) {
        if (latch.getCount() != 0) {
            try {
                latch.await();
            } catch (InterruptedException e) {
                subscription.unsubscribe();
                Thread.currentThread().interrupt();
                throw new RuntimeException("Interrupted while waiting for subscription to complete.", e);
            }
        }
    }
}
