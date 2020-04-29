package dji.midware.transfer.base;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.os.SystemClock;
import android.util.SparseArray;
import com.mapzen.android.lost.internal.FusionEngine;
import dji.midware.data.model.P3.DataCommonTransferFileDataExtended;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class ExtendedPacketHandler implements IPacketHandler {
    private static final int MILLISECOND_ACK_TIMEOUT = 30000;
    private static final int MILLISECOND_FULL_QUEUE = 400;
    private static final int MILLISECOND_RESEND_PROTECT_STATUS = 1000;
    private static final int MSG_ACK_RECEIVE_TIMEOUT = 4;
    private static final int MSG_FULL_QUEUE_TIMEOUT = 1;
    private static final int MSG_RESEND_LOST_LIST = 2;
    private static final int MSG_RESEND_PROTECT_TIMEOUT = 3;
    private static final int PACKET_RESEND_MAX_TIMES = 30;
    private static final int PACK_SEND_PERIOD_STEP_NANOSECOND = 75000;
    private long blockTime;
    private boolean isBlocking;
    private boolean isDestroyed;
    private boolean isLostStatus;
    private long mAckTime;
    private ExtendHandler mExtendedHandler;
    private HandlerThread mExtendedHandlerThread = new HandlerThread("file_transfer_extended");
    private DataCommonTransferFileDataExtended mPack;
    private boolean[] mResendProtectStatus;
    private SparseArray<byte[]> mSendData;
    private int mWindowSize;

    /* JADX DEBUG: Failed to find minimal casts for resolve overloaded methods, cast all args instead
     method: ClspMth{java.util.Arrays.fill(boolean[], boolean):void}
     arg types: [boolean[], int]
     candidates:
      ClspMth{java.util.Arrays.fill(double[], double):void}
      ClspMth{java.util.Arrays.fill(byte[], byte):void}
      ClspMth{java.util.Arrays.fill(long[], long):void}
      ClspMth{java.util.Arrays.fill(char[], char):void}
      ClspMth{java.util.Arrays.fill(short[], short):void}
      ClspMth{java.util.Arrays.fill(java.lang.Object[], java.lang.Object):void}
      ClspMth{java.util.Arrays.fill(int[], int):void}
      ClspMth{java.util.Arrays.fill(float[], float):void}
      ClspMth{java.util.Arrays.fill(boolean[], boolean):void} */
    public ExtendedPacketHandler(FileTransferTask task) {
        this.mPack = DataCommonTransferFileDataExtended.newInstance(task.mReceiveType, task.mReceiverId);
        this.mExtendedHandlerThread.start();
        this.mExtendedHandler = new ExtendHandler(this.mExtendedHandlerThread.getLooper(), task);
        this.mWindowSize = task.mWindowSize;
        this.mResendProtectStatus = new boolean[this.mWindowSize];
        this.mSendData = new SparseArray<>(this.mWindowSize);
        Arrays.fill(this.mResendProtectStatus, false);
    }

    public synchronized void sendPack(FileTransferTask task, int sequence, byte[] data, boolean isReSend) {
        if (this.isDestroyed) {
            task.LOGD("send pack exception isDestroyed");
        } else {
            if (!isReSend || data != null) {
                this.mResendProtectStatus[sequence % this.mWindowSize] = false;
                this.mSendData.put(sequence % this.mWindowSize, data);
            } else {
                data = this.mSendData.get(sequence % this.mWindowSize);
                if (data == null) {
                    task.LOGD("resend packet exception sequence=" + sequence);
                }
            }
            task.mSendCount++;
            this.mPack.setSequence(sequence);
            this.mPack.setFileData(data);
            while (true) {
                if (this.mPack.start() >= 0) {
                    if (this.isBlocking) {
                        task.LOGD("send pack successfully...... sequence=" + sequence);
                    }
                    this.isBlocking = false;
                    if (sequence % 200 == 1 || sequence == task.mTotalNum - 1) {
                        task.LOGD("send packet sequence=" + sequence + (isReSend ? " timeout=" + task.mPackReSendArray[sequence % this.mWindowSize] : "") + " free=" + task.mFreeQueueNum.get());
                    }
                    if (isReSend && this.mExtendedHandler != null) {
                        this.mExtendedHandler.sendMessageDelayed(this.mExtendedHandler.obtainMessage(3, sequence, 0), 1000);
                    }
                } else if (this.isDestroyed) {
                    task.LOGD("when send pack blocking, transfer is over, break return!");
                    break;
                } else {
                    if (!this.isBlocking) {
                        this.blockTime = System.currentTimeMillis();
                        task.LOGD("send pack blocking...... sequence=" + sequence);
                        this.isBlocking = true;
                        if (task.mPackSendPeriod < task.strategy.packSendPeriodMaxNanosecond) {
                            task.setPackSendPeriod(task.strategy.packSendPeriodMaxNanosecond);
                            task.LOGD("blocking...packet send period speed down " + task.mPackSendPeriod);
                        }
                    }
                    SystemClock.sleep(250);
                    if (System.currentTimeMillis() - this.blockTime > FusionEngine.RECENT_UPDATE_THRESHOLD_IN_MILLIS) {
                        task.stop("send packet blocking in 60s...stop the transfer!!!");
                    }
                }
            }
        }
    }

    public void handlePackAckReceive(FileTransferTask task, int maxSequence, int nextConsequentSequence, List<Integer> packetLoss) {
        if (this.isDestroyed) {
            task.LOGD("handler packet ack exception isDestroyed");
            return;
        }
        if (this.mExtendedHandler != null) {
            this.mExtendedHandler.removeMessages(4);
            this.mExtendedHandler.sendEmptyMessageDelayed(4, 30000);
        }
        task.mAckCount++;
        if (maxSequence < 0 || task.mSendSequence < maxSequence || task.mWindowSequence == task.mTotalNum) {
            task.mExceptionAckCount++;
            task.LOGE("invalid ack: maxSeq=" + maxSequence + " free=" + task.mFreeQueueNum.get());
            return;
        }
        task.LOGD("valid ack: maxSeq=" + maxSequence + " free=" + task.mFreeQueueNum.get() + " lost=" + (packetLoss == null ? 0 : packetLoss.size()));
        boolean isAckTick = false;
        if (System.currentTimeMillis() - this.mAckTime >= 1000) {
            isAckTick = true;
            this.mAckTime = System.currentTimeMillis();
        }
        int nextNeededSeq = maxSequence + 1;
        boolean isSpeedUp = true;
        if (packetLoss == null || packetLoss.size() <= 0) {
            this.isLostStatus = false;
            isSpeedUp = isAckTick;
        } else {
            this.isLostStatus = true;
            nextNeededSeq = packetLoss.get(0).intValue();
            if (task.mPackSendPeriod <= task.strategy.packSendPeriodMinNanosecond) {
                task.LOGD("ack has lost data, set pack send period to max");
                task.setPackSendPeriod(task.strategy.packSendPeriodMaxNanosecond);
            }
            if (isAckTick) {
                Iterator<Integer> it2 = packetLoss.iterator();
                while (true) {
                    if (!it2.hasNext()) {
                        break;
                    }
                    if (task.mPackReSendArray[it2.next().intValue() % this.mWindowSize] >= 10) {
                        isSpeedUp = false;
                        break;
                    }
                }
            } else {
                isSpeedUp = false;
            }
            if (this.mExtendedHandler != null) {
                this.mExtendedHandler.sendMessage(this.mExtendedHandler.obtainMessage(2, packetLoss));
            }
        }
        if (nextNeededSeq > task.mWindowSequence) {
            task.mFreeQueueNum.addAndGet(nextNeededSeq - task.mWindowSequence);
            task.mWindowSequence = nextNeededSeq;
        }
        task.mCurrentNum = task.mWindowSequence;
        if (isSpeedUp && task.mPackSendPeriod > task.strategy.packSendPeriodMinNanosecond && !this.isBlocking) {
            task.setPackSendPeriod(Math.max(task.mPackSendPeriod - PACK_SEND_PERIOD_STEP_NANOSECOND, task.strategy.packSendPeriodMinNanosecond));
            task.LOGD("packet send period speed up " + task.mPackSendPeriod);
        }
        if (task.mTransferListener != null) {
            task.mTransferListener.onProgress(task.mCurrentNum, task.mTotalNum);
        }
        if (task.mWindowSequence == task.mTotalNum) {
            if (this.mExtendedHandler != null) {
                this.mExtendedHandler.removeCallbacksAndMessages(null);
            }
            task.handleTransferOver();
            destroy();
        }
    }

    public void handleFullQueue() {
        if (this.mExtendedHandler != null) {
            this.mExtendedHandler.removeMessages(1);
            this.mExtendedHandler.sendEmptyMessageDelayed(1, 400);
        }
    }

    public void destroy() {
        if (!this.isDestroyed) {
            this.isDestroyed = true;
            if (this.mExtendedHandler != null) {
                this.mExtendedHandler.removeCallbacksAndMessages(null);
                this.mExtendedHandler = null;
            }
            this.mExtendedHandlerThread.quit();
        }
    }

    private class ExtendHandler extends Handler {
        FileTransferTask task;

        ExtendHandler(Looper looper, FileTransferTask task2) {
            super(looper);
            this.task = task2;
        }

        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    ExtendedPacketHandler.this.handleFullQueueTimeout(this.task);
                    return;
                case 2:
                    ExtendedPacketHandler.this.handlePacketResend(this.task, (List) msg.obj);
                    return;
                case 3:
                    ExtendedPacketHandler.this.handlePacketResendProtectTimeout(msg.arg1);
                    return;
                case 4:
                    ExtendedPacketHandler.this.handlePacketAckTimeout(this.task);
                    return;
                default:
                    return;
            }
        }
    }

    /* access modifiers changed from: private */
    public void handlePacketResend(FileTransferTask task, List<Integer> packetLoss) {
        long start = System.currentTimeMillis();
        int sendCount = 0;
        task.LOGD("resend lost packets begin!! start=" + packetLoss.get(0) + " size=" + packetLoss.size());
        Iterator<Integer> it2 = packetLoss.iterator();
        while (true) {
            if (!it2.hasNext()) {
                break;
            }
            Integer sequence = it2.next();
            if (this.isDestroyed) {
                task.LOGD("resend lost packets stop as transfer over!!");
                break;
            } else if (!this.mResendProtectStatus[sequence.intValue() % this.mWindowSize]) {
                int[] iArr = task.mPackReSendArray;
                int intValue = sequence.intValue() % this.mWindowSize;
                int i = iArr[intValue] + 1;
                iArr[intValue] = i;
                if (i > 30) {
                    task.stop("stop reason:" + sequence + " retry times=" + task.mPackReSendArray[sequence.intValue() % this.mWindowSize]);
                } else {
                    task.mResendCount++;
                    sendCount++;
                    this.mResendProtectStatus[sequence.intValue() % this.mWindowSize] = true;
                    int resendTimes = task.mPackReSendArray[sequence.intValue() % this.mWindowSize];
                    if (resendTimes == 10) {
                        if (((double) task.mPackSendPeriod) < ((double) task.strategy.packSendPeriodMaxNanosecond) * 0.5d) {
                            task.setPackSendPeriod((int) (((double) task.strategy.packSendPeriodMaxNanosecond) * 0.5d));
                            task.LOGD("packet send period speed down " + task.mPackSendPeriod);
                        }
                    } else if (resendTimes == 20 && task.mPackSendPeriod < task.strategy.packSendPeriodMaxNanosecond) {
                        task.setPackSendPeriod(task.strategy.packSendPeriodMaxNanosecond);
                        task.LOGD("packet send period speed down " + task.mPackSendPeriod);
                    }
                    sendPack(task, sequence.intValue(), null, true);
                    try {
                        task.speedControl(task.mPackSendPeriod);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        task.LOGD("resend lost packets end!! sendCount=" + sendCount + " cost=" + (System.currentTimeMillis() - start) + "ms");
    }

    /* access modifiers changed from: private */
    public void handleFullQueueTimeout(FileTransferTask task) {
        task.LOGD("handle full queue time out mCurrentNum=" + task.mCurrentNum + " isLostStatus=" + this.isLostStatus);
        if (task.mCurrentNum < task.mTotalNum) {
            int currentNum = task.mCurrentNum;
            if (!this.isLostStatus && (task.mSendSequence >= task.mTotalNum || task.isWindowFull())) {
                List<Integer> packetLoss = new ArrayList<>();
                int windowRightSequence = Math.min(task.mTotalNum - 1, (this.mWindowSize + currentNum) - 1);
                while (currentNum <= windowRightSequence) {
                    packetLoss.add(Integer.valueOf(currentNum));
                    currentNum++;
                }
                if (this.mExtendedHandler != null && packetLoss.size() > 0) {
                    this.mExtendedHandler.sendMessage(this.mExtendedHandler.obtainMessage(2, packetLoss));
                }
            }
            if (task.mSendSequence >= task.mTotalNum) {
                task.LOGD("data send completed, continue check full queue");
                if (this.mExtendedHandler != null) {
                    this.mExtendedHandler.sendEmptyMessageDelayed(1, 400);
                }
            }
            task.isFullQueue = false;
        }
    }

    /* access modifiers changed from: private */
    public void handlePacketResendProtectTimeout(int sequence) {
        this.mResendProtectStatus[sequence % this.mWindowSize] = false;
    }

    /* access modifiers changed from: private */
    public void handlePacketAckTimeout(FileTransferTask task) {
        task.stop("stop reason: wait ack timeout");
    }
}
