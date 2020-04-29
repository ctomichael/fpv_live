package dji.midware.media;

import android.os.Handler;
import android.os.Message;
import android.support.v4.util.Pools;
import dji.publics.DJIExecutor;
import java.util.ArrayDeque;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class DJIVideoProcessTimeTest {
    private static final int FRAME_INFO_QUEUE_MAX_SIZE = 50;
    private static final int GEN_ID_DEFAULT_OFFSET = 6;
    private static final int MSG_ADD_LISTENER = 1;
    private static final int MSG_CLEAR_LISTENER = 3;
    private static final int MSG_OUTPUT = 0;
    private static final int MSG_REMOVE_LISTENER = 2;
    private static final String TAG = "DJIVideoProcessTimeTest";
    private static DJIVideoProcessTimeTest instance;
    /* access modifiers changed from: private */
    public boolean enable = false;
    private FrameInfoQueue frameInfoQueue = new FrameInfoQueue();
    private Handler handler = new Handler(DJIExecutor.getLooper()) {
        /* class dji.midware.media.DJIVideoProcessTimeTest.AnonymousClass1 */

        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    if (msg.obj != null && (msg.obj instanceof FrameInfo)) {
                        FrameInfo frameInfo = (FrameInfo) msg.obj;
                        Iterator it2 = DJIVideoProcessTimeTest.this.listenerList.iterator();
                        while (it2.hasNext()) {
                            ((VideoProcessTimeListener) it2.next()).onResultOutput(frameInfo);
                        }
                        frameInfo.recycle();
                        return;
                    }
                    return;
                case 1:
                    if (msg.obj != null && (msg.obj instanceof VideoProcessTimeListener)) {
                        VideoProcessTimeListener addListener = (VideoProcessTimeListener) msg.obj;
                        if (!DJIVideoProcessTimeTest.this.listenerList.contains(addListener)) {
                            DJIVideoProcessTimeTest.this.listenerList.add(addListener);
                        }
                        if (!DJIVideoProcessTimeTest.this.enable) {
                            DJIVideoProcessTimeTest.this.start();
                            return;
                        }
                        return;
                    }
                    return;
                case 2:
                    if (msg.obj != null && (msg.obj instanceof VideoProcessTimeListener)) {
                        VideoProcessTimeListener removeListener = (VideoProcessTimeListener) msg.obj;
                        if (DJIVideoProcessTimeTest.this.listenerList.contains(removeListener)) {
                            if (DJIVideoProcessTimeTest.this.listenerList.size() == 1) {
                                DJIVideoProcessTimeTest.this.stop();
                            }
                            DJIVideoProcessTimeTest.this.listenerList.remove(removeListener);
                            return;
                        }
                        return;
                    }
                    return;
                case 3:
                    DJIVideoProcessTimeTest.this.stop();
                    DJIVideoProcessTimeTest.this.listenerList.clear();
                    return;
                default:
                    return;
            }
        }
    };
    /* access modifiers changed from: private */
    public LinkedList<VideoProcessTimeListener> listenerList = new LinkedList<>();

    public interface VideoProcessTimeListener {
        void onResultOutput(FrameInfo frameInfo);
    }

    public enum VideoProcessPoint {
        DataRead,
        BeforeParse,
        VideoRecv,
        DecoderServerIn,
        DecoderCodecIn,
        DecoderCodecOut
    }

    public static DJIVideoProcessTimeTest getInstance() {
        if (instance == null) {
            instance = new DJIVideoProcessTimeTest();
        }
        return instance;
    }

    private DJIVideoProcessTimeTest() {
    }

    public void addListener(VideoProcessTimeListener listener) {
        this.handler.obtainMessage(1, listener).sendToTarget();
    }

    public void removeListener(VideoProcessTimeListener listener) {
        this.handler.obtainMessage(2, listener).sendToTarget();
    }

    public void clearListeners() {
        this.handler.obtainMessage(3).sendToTarget();
    }

    public static class FrameInfo {
        private static Pools.SynchronizedPool<FrameInfo> sPool = new Pools.SynchronizedPool<>(100);
        private static final long[] zeroArr = new long[VideoProcessPoint.values().length];
        public long id;
        public long pts;
        public long[] timeStamps = new long[VideoProcessPoint.values().length];

        private FrameInfo() {
        }

        public static FrameInfo obtain() {
            FrameInfo instance = sPool.acquire();
            if (instance == null) {
                return new FrameInfo();
            }
            instance.clear();
            return instance;
        }

        /* access modifiers changed from: package-private */
        public void setTimeStamp(VideoProcessPoint point, long timeStamp) {
            this.timeStamps[point.ordinal()] = timeStamp;
        }

        public long getTimeStamp(VideoProcessPoint point) {
            return this.timeStamps[point.ordinal()];
        }

        private void clear() {
            this.id = 0;
            this.pts = 0;
            System.arraycopy(zeroArr, 0, this.timeStamps, 0, zeroArr.length);
        }

        public void recycle() {
            sPool.release(this);
        }
    }

    private class FrameInfoQueue {
        private Queue<FrameInfo> frameInfoQueue;
        private ReentrantReadWriteLock lock;
        private ReentrantReadWriteLock.ReadLock rLock;
        private ReentrantReadWriteLock.WriteLock wLock;

        private FrameInfoQueue() {
            this.lock = new ReentrantReadWriteLock();
            this.rLock = this.lock.readLock();
            this.wLock = this.lock.writeLock();
            this.frameInfoQueue = new ArrayDeque(50);
        }

        public boolean queueIn(FrameInfo frameInfo) {
            if (this.frameInfoQueue.size() >= 50) {
                poll().recycle();
            }
            this.wLock.lock();
            try {
                if (!this.frameInfoQueue.contains(frameInfo)) {
                    this.frameInfoQueue.add(frameInfo);
                }
                this.wLock.unlock();
                return true;
            } catch (Exception e) {
                this.wLock.unlock();
                return false;
            } catch (Throwable th) {
                this.wLock.unlock();
                throw th;
            }
        }

        public FrameInfo poll() {
            this.wLock.lock();
            try {
                return this.frameInfoQueue.poll();
            } finally {
                this.wLock.unlock();
            }
        }

        public void poll(FrameInfo frameInfo) {
            FrameInfo headFrameInfo;
            this.wLock.lock();
            while (!this.frameInfoQueue.isEmpty() && (headFrameInfo = this.frameInfoQueue.poll()) != frameInfo) {
                try {
                    headFrameInfo.recycle();
                } finally {
                    this.wLock.unlock();
                }
            }
        }

        public void clear() {
            this.wLock.lock();
            try {
                for (FrameInfo frameInfo : this.frameInfoQueue) {
                    frameInfo.recycle();
                }
                this.frameInfoQueue.clear();
            } finally {
                this.wLock.unlock();
            }
        }

        public FrameInfo findById(long id) {
            this.rLock.lock();
            try {
                for (FrameInfo frameInfo : this.frameInfoQueue) {
                    if (frameInfo.id == id) {
                        return frameInfo;
                    }
                }
                this.rLock.unlock();
                return null;
            } finally {
                this.rLock.unlock();
            }
        }

        public FrameInfo findByPts(long pts) {
            this.rLock.lock();
            try {
                for (FrameInfo frameInfo : this.frameInfoQueue) {
                    if (frameInfo.pts == pts) {
                        return frameInfo;
                    }
                }
                this.rLock.unlock();
                return null;
            } finally {
                this.rLock.unlock();
            }
        }

        public int size() {
            return this.frameInfoQueue.size();
        }
    }

    private long genId(byte[] data, int offset) {
        long rst;
        int offset2 = offset + 6;
        if (data == null || offset2 < 0 || offset2 >= data.length) {
            return -1;
        }
        long rst2 = -1;
        for (int i = Math.min(data.length, (offset2 + 8) - 1) - 1; i >= offset2; i--) {
            if (rst2 < 0) {
                rst = 0;
            } else {
                rst = rst2 << 8;
            }
            rst2 = rst + (((long) data[i]) & 255);
        }
        return rst2;
    }

    /* access modifiers changed from: private */
    public void start() {
        this.enable = true;
    }

    /* access modifiers changed from: private */
    public void stop() {
        this.enable = false;
        this.frameInfoQueue.clear();
    }

    public void onVideoDataProcessing(VideoProcessPoint processPoint, byte[] data, int offset, boolean needOutputRst) {
        if (this.enable) {
            long time = System.currentTimeMillis();
            long id = genId(data, offset);
            if (id >= 0) {
                FrameInfo frameInfo = this.frameInfoQueue.findById(id);
                if (frameInfo == null) {
                    frameInfo = FrameInfo.obtain();
                    frameInfo.id = id;
                }
                frameInfo.setTimeStamp(processPoint, time);
                if (needOutputRst) {
                    outputRst(frameInfo);
                } else {
                    this.frameInfoQueue.queueIn(frameInfo);
                }
            }
        }
    }

    public void onVideoDataProcessing(VideoProcessPoint processPoint, byte[] data, int offset) {
        onVideoDataProcessing(processPoint, data, offset, false);
    }

    private void outputRst(FrameInfo frameInfo) {
        if (frameInfo != null) {
            this.frameInfoQueue.poll(frameInfo);
            this.handler.obtainMessage(0, frameInfo).sendToTarget();
        }
    }
}
