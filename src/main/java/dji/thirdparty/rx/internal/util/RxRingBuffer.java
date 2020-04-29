package dji.thirdparty.rx.internal.util;

import dji.thirdparty.rx.Observer;
import dji.thirdparty.rx.Subscription;
import dji.thirdparty.rx.exceptions.MissingBackpressureException;
import dji.thirdparty.rx.internal.operators.NotificationLite;
import dji.thirdparty.rx.internal.util.unsafe.SpmcArrayQueue;
import dji.thirdparty.rx.internal.util.unsafe.SpscArrayQueue;
import dji.thirdparty.rx.internal.util.unsafe.UnsafeAccess;
import java.util.Queue;

public class RxRingBuffer implements Subscription {
    public static final int SIZE = _size;
    public static ObjectPool<Queue<Object>> SPMC_POOL = new ObjectPool<Queue<Object>>() {
        /* class dji.thirdparty.rx.internal.util.RxRingBuffer.AnonymousClass2 */

        /* access modifiers changed from: protected */
        public SpmcArrayQueue<Object> createObject() {
            return new SpmcArrayQueue<>(RxRingBuffer.SIZE);
        }
    };
    public static ObjectPool<Queue<Object>> SPSC_POOL = new ObjectPool<Queue<Object>>() {
        /* class dji.thirdparty.rx.internal.util.RxRingBuffer.AnonymousClass1 */

        /* access modifiers changed from: protected */
        public SpscArrayQueue<Object> createObject() {
            return new SpscArrayQueue<>(RxRingBuffer.SIZE);
        }
    };
    static int _size;
    private static final NotificationLite<Object> on = NotificationLite.instance();
    private final ObjectPool<Queue<Object>> pool;
    private Queue<Object> queue;
    private final int size;
    public volatile Object terminalState;

    public static RxRingBuffer getSpscInstance() {
        if (UnsafeAccess.isUnsafeAvailable()) {
            return new RxRingBuffer(SPSC_POOL, SIZE);
        }
        return new RxRingBuffer();
    }

    public static RxRingBuffer getSpmcInstance() {
        if (UnsafeAccess.isUnsafeAvailable()) {
            return new RxRingBuffer(SPMC_POOL, SIZE);
        }
        return new RxRingBuffer();
    }

    static {
        _size = 128;
        if (PlatformDependent.isAndroid()) {
            _size = 16;
        }
        String sizeFromProperty = System.getProperty("rx.ring-buffer.size");
        if (sizeFromProperty != null) {
            try {
                _size = Integer.parseInt(sizeFromProperty);
            } catch (Exception e) {
                System.err.println("Failed to set 'rx.buffer.size' with value " + sizeFromProperty + " => " + e.getMessage());
            }
        }
    }

    private RxRingBuffer(Queue<Object> queue2, int size2) {
        this.queue = queue2;
        this.pool = null;
        this.size = size2;
    }

    private RxRingBuffer(ObjectPool<Queue<Object>> pool2, int size2) {
        this.pool = pool2;
        this.queue = pool2.borrowObject();
        this.size = size2;
    }

    public synchronized void release() {
        Queue<Object> q = this.queue;
        ObjectPool<Queue<Object>> p = this.pool;
        if (!(p == null || q == null)) {
            q.clear();
            this.queue = null;
            p.returnObject(q);
        }
    }

    public void unsubscribe() {
        release();
    }

    RxRingBuffer() {
        this(new SynchronizedQueue(SIZE), SIZE);
    }

    public void onNext(Object o) throws MissingBackpressureException {
        boolean iae = false;
        boolean mbe = false;
        synchronized (this) {
            Queue<Object> q = this.queue;
            if (q != null) {
                mbe = !q.offer(on.next(o));
            } else {
                iae = true;
            }
        }
        if (iae) {
            throw new IllegalStateException("This instance has been unsubscribed and the queue is no longer usable.");
        } else if (mbe) {
            throw new MissingBackpressureException();
        }
    }

    public void onCompleted() {
        if (this.terminalState == null) {
            this.terminalState = on.completed();
        }
    }

    public void onError(Throwable t) {
        if (this.terminalState == null) {
            this.terminalState = on.error(t);
        }
    }

    public int available() {
        return this.size - count();
    }

    public int capacity() {
        return this.size;
    }

    public int count() {
        Queue<Object> q = this.queue;
        if (q == null) {
            return 0;
        }
        return q.size();
    }

    public boolean isEmpty() {
        Queue<Object> q = this.queue;
        if (q == null) {
            return true;
        }
        return q.isEmpty();
    }

    public Object poll() {
        Object o = null;
        synchronized (this) {
            Queue<Object> q = this.queue;
            if (q != null) {
                o = q.poll();
                Object ts = this.terminalState;
                if (o == null && ts != null && q.peek() == null) {
                    o = ts;
                    this.terminalState = null;
                }
            }
        }
        return o;
    }

    public Object peek() {
        Object o;
        synchronized (this) {
            Queue<Object> q = this.queue;
            if (q == null) {
                o = null;
            } else {
                o = q.peek();
                Object ts = this.terminalState;
                if (o == null && ts != null && q.peek() == null) {
                    o = ts;
                }
            }
        }
        return o;
    }

    public boolean isCompleted(Object o) {
        return on.isCompleted(o);
    }

    public boolean isError(Object o) {
        return on.isError(o);
    }

    public Object getValue(Object o) {
        return on.getValue(o);
    }

    public boolean accept(Object o, Observer child) {
        return on.accept(child, o);
    }

    public Throwable asError(Object o) {
        return on.getError(o);
    }

    public boolean isUnsubscribed() {
        return this.queue == null;
    }
}
