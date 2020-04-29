package dji.thirdparty.rx.singles;

import dji.thirdparty.rx.Single;
import dji.thirdparty.rx.SingleSubscriber;
import dji.thirdparty.rx.annotations.Experimental;
import dji.thirdparty.rx.internal.operators.BlockingOperatorToFuture;
import dji.thirdparty.rx.internal.util.BlockingUtils;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicReference;

@Experimental
public class BlockingSingle<T> {
    private final Single<? extends T> single;

    private BlockingSingle(Single<? extends T> single2) {
        this.single = single2;
    }

    @Experimental
    public static <T> BlockingSingle<T> from(Single<? extends T> single2) {
        return new BlockingSingle<>(single2);
    }

    @Experimental
    public T value() {
        final AtomicReference<T> returnItem = new AtomicReference<>();
        final AtomicReference<Throwable> returnException = new AtomicReference<>();
        final CountDownLatch latch = new CountDownLatch(1);
        BlockingUtils.awaitForComplete(latch, this.single.subscribe(new SingleSubscriber<T>() {
            /* class dji.thirdparty.rx.singles.BlockingSingle.AnonymousClass1 */

            public void onSuccess(T value) {
                returnItem.set(value);
                latch.countDown();
            }

            public void onError(Throwable error) {
                returnException.set(error);
                latch.countDown();
            }
        }));
        Throwable throwable = (Throwable) returnException.get();
        if (throwable == null) {
            return returnItem.get();
        }
        if (throwable instanceof RuntimeException) {
            throw ((RuntimeException) throwable);
        }
        throw new RuntimeException(throwable);
    }

    @Experimental
    public Future<T> toFuture() {
        return BlockingOperatorToFuture.toFuture(this.single.toObservable());
    }
}
