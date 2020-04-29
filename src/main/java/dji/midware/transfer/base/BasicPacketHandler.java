package dji.midware.transfer.base;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.SystemClock;
import dji.midware.data.model.P3.DataCommonTransferFileData;
import java.util.List;

public class BasicPacketHandler implements IPacketHandler {
    private static final int MSG_TRANSFER_TIMEOUT = 99;
    private static final int PACK_SEND_PERIOD_STEP_NANOSECOND = 150000;
    private static final int PACK_WAIT_RC_MILLISECOND = 200;
    /* access modifiers changed from: private */
    public final Object WindowLock = new Object();
    private boolean isBlocking;
    private boolean isDestroyed;
    private DataCommonTransferFileData mPack;
    private long mPackAckLostTime;
    private TransferTimeoutHandler mTransferTimeoutHandler;
    private HandlerThread mTransferTimeoutHandlerThread;
    /* access modifiers changed from: private */
    public int mWindowSize;

    public BasicPacketHandler(FileTransferTask task) {
        this.mPack = DataCommonTransferFileData.newInstance(task.mReceiveType, task.mReceiverId);
        this.mTransferTimeoutHandlerThread = new HandlerThread("file_transfer_basic");
        this.mTransferTimeoutHandlerThread.start();
        this.mTransferTimeoutHandler = new TransferTimeoutHandler(this.mTransferTimeoutHandlerThread.getLooper(), task);
        this.mWindowSize = task.mWindowSize;
    }

    public synchronized void sendPack(FileTransferTask task, int sequence, byte[] data, boolean isReSend) {
        if (this.isDestroyed) {
            task.LOGD("send pack exception isDestroyed");
        } else {
            task.mSendCount++;
            if (sequence % 200 == 1 && sequence == task.mTotalNum - 1) {
                task.LOGD("开始发送 sequence=" + sequence + (isReSend ? " timeout=" + task.mPackReSendArray[sequence % this.mWindowSize] : "") + " free=" + task.mFreeQueueNum.get());
            }
            task.mSendStatus[sequence % this.mWindowSize] = false;
            this.mPack.setSequence(sequence);
            this.mPack.setFileData(data);
            while (this.mPack.start() < 0) {
                if (!this.isBlocking) {
                    task.LOGD("send pack blocking...... sequence=" + sequence);
                    this.isBlocking = true;
                    if (task.mPackSendPeriod < task.strategy.packSendPeriodMaxNanosecond) {
                        task.setPackSendPeriod(task.strategy.packSendPeriodMaxNanosecond);
                        task.LOGD("blocking...packet send period speed down " + task.mPackSendPeriod);
                    }
                }
                SystemClock.sleep(250);
            }
            if (this.isBlocking) {
                task.LOGD("send pack successfully...... sequence=" + sequence);
            }
            this.isBlocking = false;
            this.mTransferTimeoutHandler.sendMessageDelayed(this.mTransferTimeoutHandler.obtainMessage(99, sequence, 0, data), (long) task.strategy.packTimeoutMillisecond);
        }
    }

    public void handlePackAckReceive(FileTransferTask task, int ackSequence, int nextConsequentSequence, List<Integer> list) {
        if (this.isDestroyed) {
            task.LOGD("handler packet ack exception isDestroyed");
            return;
        }
        task.mAckCount++;
        if (ackSequence < 0 || nextConsequentSequence < 0 || task.mSendSequence < ackSequence || task.mSendSequence < nextConsequentSequence || task.mWindowSequence > nextConsequentSequence || task.mWindowSequence == task.mTotalNum) {
            task.mExceptionAckCount++;
            if (task.mExceptionAckCount % 200 == 1 || task.mWindowSequence == task.mTotalNum) {
                task.LOGE("异常数据包: AckSeq=" + ackSequence + " nextConsequentSeq=" + nextConsequentSequence + " free=" + task.mFreeQueueNum.get());
                return;
            }
            return;
        }
        if (System.currentTimeMillis() - this.mPackAckLostTime >= 500 || nextConsequentSequence == task.mTotalNum || ackSequence + 1 == nextConsequentSequence) {
            task.LOGD("正常数据包: AckSeq=" + ackSequence + " nextConsequentSeq=" + nextConsequentSequence + " free=" + task.mFreeQueueNum.get());
        }
        if (ackSequence + 1 != nextConsequentSequence) {
            this.mPackAckLostTime = System.currentTimeMillis();
        } else if (System.currentTimeMillis() - this.mPackAckLostTime >= 1000) {
            this.mPackAckLostTime = System.currentTimeMillis();
            task.setPackSendPeriod(Math.max(task.strategy.packSendPeriodMinNanosecond, task.mPackSendPeriod - PACK_SEND_PERIOD_STEP_NANOSECOND));
        }
        synchronized (this.WindowLock) {
            if (ackSequence >= task.mWindowSequence) {
                task.mSendStatus[ackSequence % this.mWindowSize] = true;
            }
            task.mCurrentNum = nextConsequentSequence;
            if (task.mTransferListener != null) {
                task.mTransferListener.onProgress(task.mCurrentNum, task.mTotalNum);
            }
            while (task.mWindowSequence < nextConsequentSequence) {
                task.mFreeQueueNum.incrementAndGet();
                task.mWindowSequence++;
            }
        }
        if (nextConsequentSequence == task.mTotalNum) {
            task.handleTransferOver();
            destroy();
        }
    }

    public void handleFullQueue() {
    }

    private class TransferTimeoutHandler extends Handler {
        FileTransferTask task;

        TransferTimeoutHandler(Looper looper, FileTransferTask task2) {
            super(looper);
            this.task = task2;
        }

        /* JADX WARNING: Code restructure failed: missing block: B:25:?, code lost:
            return;
         */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public void handleMessage(android.os.Message r9) {
            /*
                r8 = this;
                int r2 = r9.what
                switch(r2) {
                    case 99: goto L_0x0006;
                    default: goto L_0x0005;
                }
            L_0x0005:
                return
            L_0x0006:
                int r1 = r9.arg1
                java.lang.Object r2 = r9.obj
                byte[] r2 = (byte[]) r2
                r0 = r2
                byte[] r0 = (byte[]) r0
                dji.midware.transfer.base.BasicPacketHandler r2 = dji.midware.transfer.base.BasicPacketHandler.this
                java.lang.Object r3 = r2.WindowLock
                monitor-enter(r3)
                dji.midware.transfer.base.FileTransferTask r2 = r8.task     // Catch:{ all -> 0x005e }
                int r2 = r2.mWindowSequence     // Catch:{ all -> 0x005e }
                if (r1 < r2) goto L_0x005c
                dji.midware.transfer.base.FileTransferTask r2 = r8.task     // Catch:{ all -> 0x005e }
                boolean[] r2 = r2.mSendStatus     // Catch:{ all -> 0x005e }
                dji.midware.transfer.base.BasicPacketHandler r4 = dji.midware.transfer.base.BasicPacketHandler.this     // Catch:{ all -> 0x005e }
                int r4 = r4.mWindowSize     // Catch:{ all -> 0x005e }
                int r4 = r1 % r4
                boolean r2 = r2[r4]     // Catch:{ all -> 0x005e }
                if (r2 != 0) goto L_0x005c
                dji.midware.transfer.base.FileTransferTask r2 = r8.task     // Catch:{ all -> 0x005e }
                dji.midware.transfer.base.FileTransferTask r4 = r8.task     // Catch:{ all -> 0x005e }
                dji.midware.transfer.base.FileTransferStrategies r4 = r4.strategy     // Catch:{ all -> 0x005e }
                int r4 = r4.packSendPeriodMaxNanosecond     // Catch:{ all -> 0x005e }
                r2.setPackSendPeriod(r4)     // Catch:{ all -> 0x005e }
                dji.midware.transfer.base.FileTransferTask r2 = r8.task     // Catch:{ all -> 0x005e }
                long r4 = r2.mResendCount     // Catch:{ all -> 0x005e }
                r6 = 1
                long r4 = r4 + r6
                r2.mResendCount = r4     // Catch:{ all -> 0x005e }
                dji.midware.transfer.base.FileTransferTask r2 = r8.task     // Catch:{ all -> 0x005e }
                dji.midware.transfer.base.FileTransferTask$FileTransferThread r2 = r2.mTransferThread     // Catch:{ all -> 0x005e }
                boolean r2 = r2.isPause     // Catch:{ all -> 0x005e }
                if (r2 == 0) goto L_0x0061
                dji.midware.transfer.base.FileTransferTask r2 = r8.task     // Catch:{ all -> 0x005e }
                java.lang.String r4 = "因遥控器流控暂停重传"
                r2.LOGE(r4)     // Catch:{ all -> 0x005e }
                r2 = 99
                r4 = 0
                android.os.Message r2 = r8.obtainMessage(r2, r1, r4, r0)     // Catch:{ all -> 0x005e }
                r4 = 200(0xc8, double:9.9E-322)
                r8.sendMessageDelayed(r2, r4)     // Catch:{ all -> 0x005e }
            L_0x005c:
                monitor-exit(r3)     // Catch:{ all -> 0x005e }
                goto L_0x0005
            L_0x005e:
                r2 = move-exception
                monitor-exit(r3)     // Catch:{ all -> 0x005e }
                throw r2
            L_0x0061:
                dji.midware.transfer.base.FileTransferTask r2 = r8.task     // Catch:{ all -> 0x005e }
                int[] r2 = r2.mPackReSendArray     // Catch:{ all -> 0x005e }
                dji.midware.transfer.base.BasicPacketHandler r4 = dji.midware.transfer.base.BasicPacketHandler.this     // Catch:{ all -> 0x005e }
                int r4 = r4.mWindowSize     // Catch:{ all -> 0x005e }
                int r4 = r1 % r4
                r5 = r2[r4]     // Catch:{ all -> 0x005e }
                int r5 = r5 + 1
                r2[r4] = r5     // Catch:{ all -> 0x005e }
                dji.midware.transfer.base.FileTransferTask r2 = r8.task     // Catch:{ all -> 0x005e }
                dji.midware.transfer.base.FileTransferStrategies r2 = r2.strategy     // Catch:{ all -> 0x005e }
                int r2 = r2.packSendRetryMaxTimes     // Catch:{ all -> 0x005e }
                if (r5 <= r2) goto L_0x00b0
                dji.midware.transfer.base.FileTransferTask r2 = r8.task     // Catch:{ all -> 0x005e }
                java.lang.StringBuilder r4 = new java.lang.StringBuilder     // Catch:{ all -> 0x005e }
                r4.<init>()     // Catch:{ all -> 0x005e }
                java.lang.String r5 = "stop reason:"
                java.lang.StringBuilder r4 = r4.append(r5)     // Catch:{ all -> 0x005e }
                java.lang.StringBuilder r4 = r4.append(r1)     // Catch:{ all -> 0x005e }
                java.lang.String r5 = " retry times="
                java.lang.StringBuilder r4 = r4.append(r5)     // Catch:{ all -> 0x005e }
                dji.midware.transfer.base.FileTransferTask r5 = r8.task     // Catch:{ all -> 0x005e }
                int[] r5 = r5.mPackReSendArray     // Catch:{ all -> 0x005e }
                dji.midware.transfer.base.BasicPacketHandler r6 = dji.midware.transfer.base.BasicPacketHandler.this     // Catch:{ all -> 0x005e }
                int r6 = r6.mWindowSize     // Catch:{ all -> 0x005e }
                int r6 = r1 % r6
                r5 = r5[r6]     // Catch:{ all -> 0x005e }
                java.lang.StringBuilder r4 = r4.append(r5)     // Catch:{ all -> 0x005e }
                java.lang.String r4 = r4.toString()     // Catch:{ all -> 0x005e }
                r2.stop(r4)     // Catch:{ all -> 0x005e }
                monitor-exit(r3)     // Catch:{ all -> 0x005e }
                goto L_0x0005
            L_0x00b0:
                dji.midware.transfer.base.BasicPacketHandler r2 = dji.midware.transfer.base.BasicPacketHandler.this     // Catch:{ all -> 0x005e }
                dji.midware.transfer.base.FileTransferTask r4 = r8.task     // Catch:{ all -> 0x005e }
                r5 = 1
                r2.sendPack(r4, r1, r0, r5)     // Catch:{ all -> 0x005e }
                goto L_0x005c
            */
            throw new UnsupportedOperationException("Method not decompiled: dji.midware.transfer.base.BasicPacketHandler.TransferTimeoutHandler.handleMessage(android.os.Message):void");
        }
    }

    public void destroy() {
        if (!this.isDestroyed) {
            this.isDestroyed = true;
            if (this.mTransferTimeoutHandler != null) {
                this.mTransferTimeoutHandler.removeCallbacksAndMessages(null);
                this.mTransferTimeoutHandler = null;
            }
            this.mTransferTimeoutHandlerThread.quit();
        }
    }
}
