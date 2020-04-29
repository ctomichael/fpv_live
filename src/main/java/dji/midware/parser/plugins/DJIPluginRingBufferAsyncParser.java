package dji.midware.parser.plugins;

import dji.midware.media.SaveLogHelper;
import dji.midware.parser.plugins.DJIPluginRingBufferParser;
import dji.midware.util.BytesUtil;
import java.util.Arrays;
import org.greenrobot.eventbus.EventBus;

public class DJIPluginRingBufferAsyncParser extends DJIPluginRingBufferParser {
    private static final String FILE_LOG_TAG = "async_parser";
    private static final String TAG = "DJIPluginRingBufferAsyn";
    private ByteBufferQueue findPackBufferQueue;
    /* access modifiers changed from: private */
    public boolean isParsing = false;
    private int[] kmpNext;
    private long lastLogTime = -1;
    private long lastParseTime = 0;
    private SaveLogHelper logHelper;
    /* access modifiers changed from: private */
    public ByteBufferQueue offerBufferQueue;
    private int packCount = 0;
    /* access modifiers changed from: private */
    public Object parseLock = new Object();
    private Runnable parseTask = new Runnable() {
        /* class dji.midware.parser.plugins.DJIPluginRingBufferAsyncParser.AnonymousClass1 */

        /* JADX WARNING: Code restructure failed: missing block: B:25:0x0042, code lost:
            dji.midware.parser.plugins.DJIPluginRingBufferAsyncParser.access$600(r3.this$0);
         */
        /* JADX WARNING: No exception handlers in catch block: Catch:{  } */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public void run() {
            /*
                r3 = this;
            L_0x0000:
                dji.midware.parser.plugins.DJIPluginRingBufferAsyncParser r1 = dji.midware.parser.plugins.DJIPluginRingBufferAsyncParser.this
                boolean r1 = r1.isParsing
                if (r1 == 0) goto L_0x0029
                dji.midware.parser.plugins.DJIPluginRingBufferAsyncParser r1 = dji.midware.parser.plugins.DJIPluginRingBufferAsyncParser.this
                java.lang.Object r2 = r1.parseLock
                monitor-enter(r2)
                dji.midware.parser.plugins.DJIPluginRingBufferAsyncParser r1 = dji.midware.parser.plugins.DJIPluginRingBufferAsyncParser.this     // Catch:{ all -> 0x002f }
                boolean r1 = r1.checkOfferQueue()     // Catch:{ all -> 0x002f }
                if (r1 != 0) goto L_0x0020
                dji.midware.parser.plugins.DJIPluginRingBufferAsyncParser r1 = dji.midware.parser.plugins.DJIPluginRingBufferAsyncParser.this     // Catch:{ InterruptedException -> 0x002a }
                java.lang.Object r1 = r1.parseLock     // Catch:{ InterruptedException -> 0x002a }
                r1.wait()     // Catch:{ InterruptedException -> 0x002a }
            L_0x0020:
                dji.midware.parser.plugins.DJIPluginRingBufferAsyncParser r1 = dji.midware.parser.plugins.DJIPluginRingBufferAsyncParser.this     // Catch:{ all -> 0x002f }
                boolean r1 = r1.isParsing     // Catch:{ all -> 0x002f }
                if (r1 != 0) goto L_0x0032
                monitor-exit(r2)     // Catch:{ all -> 0x002f }
            L_0x0029:
                return
            L_0x002a:
                r0 = move-exception
                r0.printStackTrace()     // Catch:{ all -> 0x002f }
                goto L_0x0020
            L_0x002f:
                r1 = move-exception
                monitor-exit(r2)     // Catch:{ all -> 0x002f }
                throw r1
            L_0x0032:
                dji.midware.parser.plugins.DJIPluginRingBufferAsyncParser r1 = dji.midware.parser.plugins.DJIPluginRingBufferAsyncParser.this     // Catch:{ all -> 0x002f }
                boolean r1 = r1.checkOfferQueue()     // Catch:{ all -> 0x002f }
                if (r1 != 0) goto L_0x003c
                monitor-exit(r2)     // Catch:{ all -> 0x002f }
                goto L_0x0000
            L_0x003c:
                dji.midware.parser.plugins.DJIPluginRingBufferAsyncParser r1 = dji.midware.parser.plugins.DJIPluginRingBufferAsyncParser.this     // Catch:{ all -> 0x002f }
                r1.prepareFindPackBufferQueue()     // Catch:{ all -> 0x002f }
                monitor-exit(r2)     // Catch:{ all -> 0x002f }
                dji.midware.parser.plugins.DJIPluginRingBufferAsyncParser r1 = dji.midware.parser.plugins.DJIPluginRingBufferAsyncParser.this
                r1.findPack()
                goto L_0x0000
            */
            throw new UnsupportedOperationException("Method not decompiled: dji.midware.parser.plugins.DJIPluginRingBufferAsyncParser.AnonymousClass1.run():void");
        }
    };
    private Thread parseThread;
    private long parseTime = 0;
    private ByteBufferQueue tempBufferQueue;
    private Object threadLock = new Object();

    public enum AOA_BUFFER_EVENT {
        FULL
    }

    public DJIPluginRingBufferAsyncParser(int bufferSize, DJIRingBufferModel bufferModel, DJIPluginRingBufferParser.DJIRingBufferParserListener listener) {
        super(bufferSize, bufferModel, listener);
        this.findPackBufferQueue = new ByteBufferQueue(bufferSize);
        this.offerBufferQueue = new ByteBufferQueue(bufferSize);
        this.kmpNext = BytesUtil.kmpNext(bufferModel.header);
    }

    /* access modifiers changed from: private */
    public void logToFile(String msg) {
        if (this.logHelper == null) {
            this.logHelper = new SaveLogHelper(FILE_LOG_TAG, 5000);
        }
        this.logHelper.log(msg);
    }

    private class ByteBufferQueue {
        private static final int MAX_BUFFER_SIZE = 1572864;
        private static final int MAX_JUDGE_TIME_WINDOW = 10000;
        private static final int MAX_TIME_REACH_MAX_BSIZE = 4;
        /* access modifiers changed from: private */
        public byte[] buffer;
        private int headIndex = 0;
        private long mCurJudgeMillonSecond = 0;
        private int mCurReachMaxBufferTime = 0;
        private int tailIndex = 0;
        private byte[] tempBuf;

        public ByteBufferQueue(int bufferSize) {
            this.buffer = new byte[bufferSize];
        }

        public boolean isEmpty() {
            return this.headIndex == this.tailIndex;
        }

        public int remain() {
            if (this.tailIndex >= this.headIndex) {
                return ((this.buffer.length - this.tailIndex) + this.headIndex) - 1;
            }
            return (this.headIndex - this.tailIndex) - 1;
        }

        public int size() {
            return (this.buffer.length - 1) - remain();
        }

        public void headIncrease(int length) {
            if (length <= this.buffer.length) {
                this.headIndex += length;
                if (this.headIndex >= this.buffer.length) {
                    this.headIndex -= this.buffer.length;
                }
            }
        }

        private void tailIncrease(int length) {
            if (length <= this.buffer.length) {
                this.tailIndex += length;
                if (this.tailIndex >= this.buffer.length) {
                    this.tailIndex -= this.buffer.length;
                }
            }
        }

        public byte[] getBuffer() {
            return this.buffer;
        }

        public int getHeadIndex() {
            return this.headIndex;
        }

        public void setHeadIndex(int headIndex2) {
            this.headIndex = headIndex2;
        }

        public int getTailIndex() {
            return this.tailIndex;
        }

        public void setTailIndex(int tailIndex2) {
            this.tailIndex = tailIndex2;
        }

        public void offer(byte[] buf, int offset, int length, boolean isToHead) {
            if (length > remain()) {
                tryToExpandCapacity(length - remain());
            }
            if (isToHead) {
                if (this.headIndex >= length) {
                    System.arraycopy(buf, offset, this.buffer, this.headIndex - length, length);
                    this.headIndex -= length;
                    return;
                }
                int curSize = size();
                System.arraycopy(this.buffer, this.headIndex, this.buffer, length, curSize);
                System.arraycopy(buf, offset, this.buffer, 0, length);
                this.headIndex = 0;
                this.tailIndex = curSize + length;
            } else if (this.tailIndex <= this.headIndex) {
                System.arraycopy(buf, offset, this.buffer, this.tailIndex, length);
                tailIncrease(length);
            } else {
                int bufferRemain = this.buffer.length - this.tailIndex;
                int dataRemain = length - bufferRemain;
                if (dataRemain > 0) {
                    System.arraycopy(buf, offset, this.buffer, this.tailIndex, bufferRemain);
                    System.arraycopy(buf, offset + bufferRemain, this.buffer, 0, dataRemain);
                    this.tailIndex = dataRemain;
                    return;
                }
                System.arraycopy(buf, offset, this.buffer, this.tailIndex, length);
                tailIncrease(length);
            }
        }

        public void offer(byte[] buf, int offset, int length) {
            offer(buf, offset, length, false);
        }

        public void offer(ByteBufferQueue byteBufferQueue, int length, boolean isToHead) {
            int length2 = Math.min(length, byteBufferQueue.size());
            byteBufferQueue.rebuildBuffer();
            offer(byteBufferQueue.getBuffer(), byteBufferQueue.getHeadIndex(), length2, isToHead);
        }

        public int getCapicity() {
            return this.buffer.length - 1;
        }

        private void tryToExpandCapacity(int length) {
            DJIPluginRingBufferAsyncParser.this.logToFile("expend:" + (this == DJIPluginRingBufferAsyncParser.this.offerBufferQueue ? "offer queue" : "find queue") + " length=" + length + " head=" + this.headIndex + " tail=" + this.tailIndex + " capicity=" + getCapicity());
            rebuildBuffer();
            if (size() >= MAX_BUFFER_SIZE) {
                DJIPluginRingBufferAsyncParser.this.logToFile("expend too long, need clean.");
                clean();
                long curMillisTime = System.currentTimeMillis();
                if (this.mCurJudgeMillonSecond - curMillisTime > 10000) {
                    this.mCurReachMaxBufferTime = 0;
                    this.mCurJudgeMillonSecond = curMillisTime;
                }
                this.mCurReachMaxBufferTime++;
                DJIPluginRingBufferAsyncParser.this.logToFile("expend too long, judge time = " + this.mCurReachMaxBufferTime);
                if (this.mCurReachMaxBufferTime >= 4) {
                    EventBus.getDefault().post(AOA_BUFFER_EVENT.FULL);
                    this.mCurReachMaxBufferTime = 0;
                    return;
                }
                return;
            }
            byte[] cache = this.buffer;
            this.buffer = new byte[newCapacity(this.buffer.length, length)];
            int lastHead = this.headIndex;
            int lastLen = size();
            this.headIndex = 0;
            this.tailIndex = 0;
            offer(cache, lastHead, lastLen);
        }

        /* JADX DEBUG: Failed to find minimal casts for resolve overloaded methods, cast all args instead
         method: ClspMth{java.lang.Math.max(float, float):float}
         arg types: [int, float]
         candidates:
          ClspMth{java.lang.Math.max(double, double):double}
          ClspMth{java.lang.Math.max(int, int):int}
          ClspMth{java.lang.Math.max(long, long):long}
          ClspMth{java.lang.Math.max(float, float):float} */
        private int newCapacity(int originLength, int length) {
            int size = 0;
            do {
                size += (int) Math.max(1.0f, 0.125f * ((float) originLength));
            } while (size <= length);
            return size + originLength;
        }

        public void remove(int length) {
            if (length >= size()) {
                clean();
            } else if (length < this.buffer.length - this.headIndex) {
                this.headIndex += length;
            } else {
                this.headIndex = length - (this.buffer.length - this.headIndex);
            }
        }

        public void rebuildBuffer() {
            if (this.tailIndex < this.headIndex) {
                int cacheLen = this.buffer.length - this.headIndex;
                if (this.tempBuf == null || this.tempBuf.length < cacheLen) {
                    this.tempBuf = new byte[cacheLen];
                }
                System.arraycopy(this.buffer, this.headIndex, this.tempBuf, 0, cacheLen);
                System.arraycopy(this.buffer, 0, this.buffer, cacheLen, this.tailIndex);
                System.arraycopy(this.tempBuf, 0, this.buffer, 0, cacheLen);
                this.headIndex = 0;
                tailIncrease(cacheLen);
            }
        }

        public void clean() {
            this.headIndex = 0;
            this.tailIndex = 0;
        }

        public String toString(int from, int to) {
            StringBuffer sb = new StringBuffer();
            int i = from;
            while (i != to) {
                int nextIndex = i == this.buffer.length + -1 ? 0 : i + 1;
                sb.append(Integer.toHexString(this.buffer[i] & 255));
                if (nextIndex == to) {
                    break;
                }
                sb.append(",");
                i = nextIndex;
            }
            return sb.toString();
        }

        public String toString(int len) {
            int to = this.headIndex + len;
            if (to >= this.buffer.length) {
                to -= this.buffer.length;
            }
            return toString(this.headIndex, to);
        }

        public String getDataString() {
            return "h=" + this.headIndex + " t=" + this.tailIndex + " data=" + Arrays.toString(this.buffer);
        }
    }

    public void parse(byte[] buffer, int offset, int count) {
        if (this.parseThread == null) {
            startParseThread();
        }
        offerData(buffer, offset, count);
    }

    private void startParseThread() {
        synchronized (this.threadLock) {
            if (!this.isParsing || this.parseThread == null) {
                this.isParsing = true;
                this.parseThread = new Thread(this.parseTask, FILE_LOG_TAG);
                this.parseThread.setPriority(9);
                this.parseThread.start();
                logToFile("startParseThread");
            }
        }
    }

    private void stopParseThread() {
        synchronized (this.threadLock) {
            this.isParsing = false;
            synchronized (this.parseLock) {
                this.parseLock.notifyAll();
            }
            this.parseThread = null;
            logToFile("stopParseThread");
        }
    }

    public void shutDown() {
        super.shutDown();
        stopParseThread();
    }

    private void offerData(byte[] buffer, int offset, int count) {
        synchronized (this.parseLock) {
            this.offerBufferQueue.offer(buffer, offset, count);
            if (checkOfferQueue()) {
                this.parseLock.notifyAll();
            }
        }
    }

    /* access modifiers changed from: private */
    public boolean checkOfferQueue() {
        return this.offerBufferQueue.size() > 10;
    }

    /* JADX DEBUG: Failed to find minimal casts for resolve overloaded methods, cast all args instead
     method: dji.midware.parser.plugins.DJIPluginRingBufferAsyncParser.ByteBufferQueue.offer(dji.midware.parser.plugins.DJIPluginRingBufferAsyncParser$ByteBufferQueue, int, boolean):void
     arg types: [dji.midware.parser.plugins.DJIPluginRingBufferAsyncParser$ByteBufferQueue, int, int]
     candidates:
      dji.midware.parser.plugins.DJIPluginRingBufferAsyncParser.ByteBufferQueue.offer(byte[], int, int):void
      dji.midware.parser.plugins.DJIPluginRingBufferAsyncParser.ByteBufferQueue.offer(dji.midware.parser.plugins.DJIPluginRingBufferAsyncParser$ByteBufferQueue, int, boolean):void */
    /* access modifiers changed from: private */
    public void prepareFindPackBufferQueue() {
        if (this.findPackBufferQueue.size() > 0) {
            this.offerBufferQueue.offer(this.findPackBufferQueue, this.findPackBufferQueue.size(), true);
        }
        this.tempBufferQueue = this.findPackBufferQueue;
        this.findPackBufferQueue = this.offerBufferQueue;
        this.offerBufferQueue = this.tempBufferQueue;
        this.offerBufferQueue.clean();
    }

    /* access modifiers changed from: private */
    public void findPack() {
        while (true) {
            if (this.findPackBufferQueue.size() > this.bufferModel.header.length) {
                if (this.isParsing) {
                    int packHeadIndex = BytesUtil.kmp(this.findPackBufferQueue.buffer, this.bufferModel.header, this.findPackBufferQueue.getHeadIndex(), this.findPackBufferQueue.getTailIndex(), this.kmpNext);
                    if (packHeadIndex >= 0) {
                        this.findPackBufferQueue.setHeadIndex(packHeadIndex);
                        int dataOffset = this.findPackBufferQueue.getHeadIndex() + this.bufferModel.header.length;
                        if (dataOffset >= this.findPackBufferQueue.getBuffer().length) {
                            dataOffset -= this.findPackBufferQueue.getBuffer().length;
                        }
                        if (this.findPackBufferQueue.remain() < this.bufferModel.header.length + this.bufferModel.secondHeaderLen) {
                            break;
                        }
                        this.packCount++;
                        if (this.bufferModel.secondHeaderLen + dataOffset <= this.findPackBufferQueue.getTailIndex()) {
                            int parseRst = this.listener.parseSecondHeader(this.findPackBufferQueue.buffer, dataOffset, this.bufferModel.secondHeaderLen);
                            if (parseRst >= 0) {
                                this.bufferModel.bodyLen = parseRst;
                                if (this.findPackBufferQueue.remain() < this.bufferModel.header.length + parseRst + this.bufferModel.secondHeaderLen) {
                                    break;
                                }
                                int dataOffset2 = dataOffset + this.bufferModel.secondHeaderLen;
                                if (this.findPackBufferQueue.getBuffer().length - dataOffset2 < parseRst) {
                                    this.findPackBufferQueue.rebuildBuffer();
                                    dataOffset2 = this.findPackBufferQueue.getHeadIndex() + this.bufferModel.header.length + this.bufferModel.secondHeaderLen;
                                }
                                if (dataOffset2 + parseRst > this.findPackBufferQueue.getTailIndex()) {
                                    break;
                                }
                                this.listener.onGetBody(this.findPackBufferQueue.getBuffer(), dataOffset2, parseRst);
                                this.packCount++;
                                this.findPackBufferQueue.remove(this.bufferModel.header.length + this.bufferModel.secondHeaderLen + parseRst);
                            } else if (!this.findPackBufferQueue.isEmpty()) {
                                this.findPackBufferQueue.headIncrease(1);
                            }
                        } else {
                            break;
                        }
                    } else if (!this.findPackBufferQueue.isEmpty()) {
                        this.findPackBufferQueue.setHeadIndex((this.findPackBufferQueue.getTailIndex() - this.bufferModel.header.length) + 1);
                        if (this.findPackBufferQueue.getHeadIndex() < 0) {
                            this.findPackBufferQueue.headIncrease(this.findPackBufferQueue.getBuffer().length);
                        }
                    }
                } else {
                    logToFile("findpack into isParsing false");
                    break;
                }
            } else {
                break;
            }
        }
        this.findPackBufferQueue.rebuildBuffer();
        this.packCount = 0;
    }
}
