package dji.midware.util;

import android.os.Message;
import dji.fieldAnnotation.EXClassNullAway;
import dji.log.DJILog;
import dji.midware.data.model.common.ByteObject;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

@EXClassNullAway
public class SimpleComsumer extends Thread {
    private static final String TAG = "SimpleComsumer";
    private Callback callback;
    private long curNanos;
    private long lastNanos;
    private BlockingQueue<ByteObject> pendingByteObjectQueue = new LinkedBlockingQueue(2048);
    private BlockingQueue<Message> queue = new LinkedBlockingQueue(2048);

    public interface Callback {
        void invoke(Message message);

        void invoke(ByteObject byteObject);
    }

    public SimpleComsumer(Callback cb, String threadName) {
        super(threadName + "-data-parser");
        this.callback = cb;
        start();
    }

    public int getQueueSize() {
        return this.pendingByteObjectQueue.size();
    }

    public void put(Message msg) {
        if (msg != null) {
            try {
                this.queue.put(msg);
            } catch (Exception e) {
                DJILog.e(TAG, "put error", new Object[0]);
            }
        }
    }

    public void put(ByteObject byteObject) {
        if (byteObject != null) {
            try {
                this.pendingByteObjectQueue.put(byteObject);
            } catch (InterruptedException e) {
                DJILog.e(TAG, Thread.currentThread().getName() + ", thread interrupt" + e, new Object[0]);
            }
        }
    }

    public void run() {
        while (true) {
            try {
                ByteObject byteObject = this.pendingByteObjectQueue.take();
                if (!(byteObject == null || this.callback == null)) {
                    this.callback.invoke(byteObject);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void onDestroy() {
        interrupt();
        this.queue.clear();
        this.callback = null;
    }
}
