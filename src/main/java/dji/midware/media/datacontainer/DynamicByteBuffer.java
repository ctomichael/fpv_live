package dji.midware.media.datacontainer;

import android.util.Log;
import dji.fieldAnnotation.EXClassNullAway;
import java.nio.ByteBuffer;

@EXClassNullAway
public class DynamicByteBuffer {
    private static final String TAG = "DynamicByteBuffer";
    private ByteBuffer buffer;
    private boolean direct;
    private Object lockSync = new Object();
    private Thread lockThread = null;
    private int size = 0;

    public DynamicByteBuffer(int capacity, boolean direct2) {
        this.direct = direct2;
        if (direct2) {
            this.buffer = ByteBuffer.allocateDirect(capacity);
        } else {
            this.buffer = ByteBuffer.allocate(capacity);
        }
    }

    public int capacity() {
        return this.buffer.capacity();
    }

    public int size() {
        return this.size;
    }

    public ByteBuffer lockAndReadData() {
        lock();
        return this.buffer;
    }

    public void lock() {
        synchronized (this.lockSync) {
            if (Thread.currentThread().equals(this.lockThread)) {
                throw new RuntimeException("you have locked the object and can't lock it again");
            }
            while (this.lockThread != null) {
                try {
                    this.lockSync.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            this.lockThread = Thread.currentThread();
        }
    }

    public void unLock() {
        synchronized (this.lockSync) {
            if (!Thread.currentThread().equals(this.lockThread)) {
                Log.e(TAG, "you have NOT locked the object and can't unlock it.");
                return;
            }
            this.lockThread = null;
            this.lockSync.notifyAll();
        }
    }

    public ByteBuffer lockAndGetStaticBuffer() {
        lock();
        return this.buffer;
    }

    public void setData(ByteBuffer src) {
        lock();
        this.size = src.remaining();
        if (this.buffer.capacity() < src.remaining()) {
            int newSize = this.buffer.capacity();
            while (newSize < src.remaining()) {
                newSize = (int) (((double) newSize) * 1.5d);
            }
            if (this.direct) {
                this.buffer = ByteBuffer.allocateDirect(newSize);
            } else {
                this.buffer = ByteBuffer.allocate(newSize);
            }
        }
        this.buffer.clear();
        this.buffer.put(src);
        unLock();
    }

    public void setData(byte[] src, int size2) {
        setData(ByteBuffer.wrap(src, 0, size2));
    }

    public void setSize(int newSize) {
        this.size = newSize;
    }
}
