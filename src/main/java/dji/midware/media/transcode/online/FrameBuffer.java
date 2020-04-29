package dji.midware.media.transcode.online;

import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.media.MediaLogger;
import dji.midware.media.transcode.online.Frame;
import java.util.Locale;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.TimeUnit;

@EXClassNullAway
public class FrameBuffer {
    private static boolean DEBUG = false;
    private final int frameCapacity;
    private int generated = 0;
    private final ArrayBlockingQueue<Frame> pool = new ArrayBlockingQueue<>(this.poolCapacity);
    private final int poolCapacity;
    private final ArrayBlockingQueue<Frame> queue = new ArrayBlockingQueue<>(this.poolCapacity);
    private final Frame.FrameType type;

    public FrameBuffer(int frameCapacity2, int poolCapacity2, Frame.FrameType type2) {
        this.frameCapacity = frameCapacity2;
        this.poolCapacity = poolCapacity2;
        this.type = type2;
    }

    public void init() {
        this.pool.clear();
        this.queue.clear();
        this.generated = 0;
    }

    public void deinit() {
        this.pool.clear();
        this.queue.clear();
    }

    public Frame getFrame() {
        Frame frame = this.pool.peek();
        if (frame != null) {
            this.pool.poll();
        } else {
            synchronized (this.pool) {
                if (this.generated < this.poolCapacity) {
                    this.generated++;
                    frame = new Frame(this.type, this.frameCapacity);
                } else {
                    frame = null;
                }
            }
        }
        return frame;
    }

    public void release(Frame frame) {
        this.pool.offer(frame);
    }

    public int getQueueSize() {
        return this.queue.size();
    }

    public Frame poll() {
        try {
            return this.queue.poll(500, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            return null;
        }
    }

    public Frame peek() {
        return this.queue.peek();
    }

    public void queue(Frame frame) {
        try {
            this.queue.put(frame);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        MediaLogger.i(DEBUG, this.type.toString(), String.format(Locale.US, "Try to queue in a frame.  Index=%d, Queue size=%d", Long.valueOf(frame.getIndex()), Integer.valueOf(this.queue.size())));
    }
}
