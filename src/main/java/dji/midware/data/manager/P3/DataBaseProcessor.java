package dji.midware.data.manager.P3;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.lmax.disruptor.EventHandler;
import com.lmax.disruptor.InsufficientCapacityException;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.TimeoutBlockingWaitStrategy;
import com.lmax.disruptor.TimeoutException;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;
import dji.fieldAnnotation.EXClassNullAway;
import dji.log.DJILog;
import dji.log.DJILogHelper;
import dji.log.DJILogUtils;
import dji.midware.data.config.P3.Ccode;
import dji.midware.data.manager.P3.DataBeanEvent;
import dji.midware.data.packages.P3.Pack;
import dji.midware.data.packages.P3.RecvPack;
import dji.midware.data.packages.P3.SendPack;
import dji.midware.interfaces.DJIDataCallBack;
import dji.midware.util.BackgroundLooper;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

@EXClassNullAway
public class DataBaseProcessor {
    private static final int MSG_SENDCMD = 10;
    private static final int MSG_SETMSG = 12;
    private static final int MSG_TIMEOUT = 11;
    private static final int NEED_LOG_TIME = 25;
    private static final String TAG = "DataBaseProcessor";
    /* access modifiers changed from: private */
    public ConcurrentHashMap<Integer, DataBean> dataBeanMap;
    Runnable debugPackRunnable;
    /* access modifiers changed from: private */
    public Handler handler;
    private HandlerThread handlerThread;
    private boolean isCanLogTrace;
    boolean isDebugPackNum;
    private int mDisruptorSendCount;
    private ThreadFactory mDisruptorThreadFactory;
    private int mNormalLogTime;
    private Disruptor<DataBeanEvent> mSpecialSendDisruptor;
    /* access modifiers changed from: private */
    public Thread mSpecialSendThread;
    ConcurrentHashMap<String, Integer> packCmdMap;
    AtomicInteger packCount;
    AtomicInteger packSizeCount;

    public static class DataBean {
        DJIDataCallBack dataCallBack;
        boolean hasAck;
        boolean notifyInMainThread;
        ProcessCallback processCallback;
        RecvPack recvPack;
        SendPack sendPack;
    }

    public interface ProcessCallback {
        void onResult(SendPack sendPack, DJIDataCallBack dJIDataCallBack, RecvPack recvPack, boolean z);
    }

    public static DataBaseProcessor getInstance() {
        return SingletonHolder.INSTANCE;
    }

    private DataBaseProcessor() {
        this.isDebugPackNum = false;
        this.mDisruptorThreadFactory = new ThreadFactory() {
            /* class dji.midware.data.manager.P3.DataBaseProcessor.AnonymousClass2 */

            public Thread newThread(Runnable r) {
                Thread ret = new Thread(r, "special-send-disruptor");
                ret.setPriority(9);
                return ret;
            }
        };
        this.mDisruptorSendCount = 0;
        this.isCanLogTrace = true;
        this.mNormalLogTime = 0;
        this.handlerThread = new HandlerThread(TAG);
        this.handlerThread.start();
        this.handler = new MyHandler(this.handlerThread.getLooper());
        this.dataBeanMap = new ConcurrentHashMap<>();
        if (this.isDebugPackNum) {
            this.packSizeCount = new AtomicInteger();
            this.packCount = new AtomicInteger();
            this.packCmdMap = new ConcurrentHashMap<>();
            this.debugPackRunnable = new Runnable() {
                /* class dji.midware.data.manager.P3.DataBaseProcessor.AnonymousClass1 */

                public void run() {
                    StringBuilder sb = new StringBuilder();
                    sb.append("========pack size=").append(DataBaseProcessor.this.packSizeCount.get()).append(" byte, pack num=").append(DataBaseProcessor.this.packCount.get());
                    for (String key : DataBaseProcessor.this.packCmdMap.keySet()) {
                        sb.append("\n cmd is:").append(key).append(" count=").append(DataBaseProcessor.this.packCmdMap.get(key));
                    }
                    DJILogHelper.getInstance().LOGD("PackCount", sb.toString());
                    DataBaseProcessor.this.packSizeCount.set(0);
                    DataBaseProcessor.this.packCount.set(0);
                    DataBaseProcessor.this.packCmdMap.clear();
                    BackgroundLooper.postDelayed(DataBaseProcessor.this.debugPackRunnable, 1000);
                }
            };
            BackgroundLooper.postDelayed(this.debugPackRunnable, 1000);
        }
    }

    private int getKey(Pack pack) {
        return (pack.cmdSet << 24) | (pack.cmdId << 16) | pack.seq;
    }

    public int sendCmd(@NonNull SendPack sendPack, @Nullable DJIDataCallBack dataCallback, @NonNull ProcessCallback processCallback, boolean notifyInMainThread, boolean hasAck, boolean useSpecialChannel) {
        if (this.isDebugPackNum) {
            this.packCount.addAndGet(1);
            this.packSizeCount.addAndGet(sendPack.getLength());
            String key = sendPack.cmdSet + "-" + sendPack.cmdId;
            if (this.packCmdMap.containsKey(key)) {
                this.packCmdMap.put(key, Integer.valueOf(this.packCmdMap.get(key).intValue() + 1));
            } else {
                this.packCmdMap.put(key, 1);
            }
        }
        if (!ServiceManager.getInstance().isConnected()) {
            sendPack.bufferObject.willRepeat(false);
            sendPack.bufferObject.noUsed();
            RecvPack recvPack = RecvPack.obtain(null);
            recvPack.ccode = Ccode.NOCONNECT.value();
            processCallback.onResult(sendPack, dataCallback, recvPack, notifyInMainThread);
            return 0;
        }
        sendPack.bufferObject.willRepeat(true);
        int key2 = getKey(sendPack);
        DataBean data = new DataBean();
        data.sendPack = sendPack;
        data.dataCallBack = dataCallback;
        data.processCallback = processCallback;
        data.notifyInMainThread = notifyInMainThread;
        data.hasAck = hasAck;
        if (useSpecialChannel) {
            if (this.mSpecialSendDisruptor == null) {
                initSpecialChannelDisruptor();
            }
            return putPackToSpecialChannelToSend(data, key2);
        }
        Message msg = Message.obtain();
        msg.what = 10;
        msg.arg1 = key2;
        msg.obj = data;
        this.handler.sendMessage(msg);
        return 0;
    }

    private static class SingletonHolder {
        /* access modifiers changed from: private */
        public static final DataBaseProcessor INSTANCE = new DataBaseProcessor();

        private SingletonHolder() {
        }
    }

    private class MyHandler extends Handler {
        public MyHandler(Looper looper) {
            super(looper);
        }

        public void handleMessage(Message msg) {
            int msgId = msg.what;
            int key = msg.arg1;
            if (msgId == 10) {
                DataBaseProcessor.this.realSendOnePack((DataBean) msg.obj, key);
            } else if (msgId == 11) {
                if (DataBaseProcessor.this.dataBeanMap.containsKey(Integer.valueOf(key))) {
                    DataBean dataBean = (DataBean) msg.obj;
                    SendPack sendPack = dataBean.sendPack;
                    sendPack.repeatTimes--;
                    if (dataBean.sendPack.repeatTimes <= 0) {
                        DataBaseProcessor.this.dataBeanMap.remove(Integer.valueOf(key));
                        dataBean.processCallback.onResult(dataBean.sendPack, dataBean.dataCallBack, null, dataBean.notifyInMainThread);
                    } else {
                        ServiceManager.getInstance().sendmessage(dataBean.sendPack);
                        Message timeOutMsg = Message.obtain();
                        timeOutMsg.what = 11;
                        timeOutMsg.arg1 = key;
                        timeOutMsg.obj = dataBean;
                        DataBaseProcessor.this.handler.sendMessageDelayed(timeOutMsg, (long) dataBean.sendPack.timeOut);
                    }
                }
            } else if (msgId == 12) {
                if (DataBaseProcessor.this.dataBeanMap.containsKey(Integer.valueOf(key))) {
                    DataBean dataBean2 = (DataBean) DataBaseProcessor.this.dataBeanMap.get(Integer.valueOf(key));
                    dataBean2.processCallback.onResult(dataBean2.sendPack, dataBean2.dataCallBack, (RecvPack) msg.obj, dataBean2.notifyInMainThread);
                    DataBaseProcessor.this.dataBeanMap.remove(Integer.valueOf(key));
                } else {
                    DataBaseProcessor.this.dataBeanMap.remove(Integer.valueOf(key));
                }
            }
            super.handleMessage(msg);
        }
    }

    public void setMsg(Pack pack) {
        Message msg = Message.obtain();
        int key = getKey(pack);
        msg.what = 12;
        msg.arg1 = key;
        msg.obj = pack;
        this.handler.sendMessage(msg);
    }

    /* access modifiers changed from: private */
    public void realSendOnePack(DataBean dataBean, int key) {
        if (dataBean.hasAck) {
            this.dataBeanMap.put(Integer.valueOf(key), dataBean);
            dataBean.sendPack.bufferObject.willRepeat(true);
            ServiceManager.getInstance().sendmessage(dataBean.sendPack);
            Message timeOutMsg = Message.obtain();
            timeOutMsg.what = 11;
            timeOutMsg.arg1 = key;
            timeOutMsg.obj = dataBean;
            this.handler.sendMessageDelayed(timeOutMsg, (long) dataBean.sendPack.timeOut);
            return;
        }
        ServiceManager.getInstance().sendmessage(dataBean.sendPack);
        dataBean.sendPack.bufferObject.willRepeat(false);
        dataBean.sendPack.bufferObject.noUsed();
    }

    public void startSpecialSendChannel() {
        initSpecialChannelDisruptor();
    }

    public void stopSpecialSendChannel(long milliseconds) throws TimeoutException {
        if (this.mSpecialSendDisruptor != null) {
            this.mSpecialSendDisruptor.shutdown(milliseconds, TimeUnit.MILLISECONDS);
            this.mSpecialSendDisruptor = null;
            this.mSpecialSendThread = null;
        }
    }

    private void initSpecialChannelDisruptor() {
        this.mSpecialSendDisruptor = new Disruptor<>(new DataBeanEvent.DataBeanEventFactory(), 1024, this.mDisruptorThreadFactory, ProducerType.SINGLE, new TimeoutBlockingWaitStrategy(200, TimeUnit.MICROSECONDS));
        this.mSpecialSendDisruptor.handleEventsWith(new SpecialDataBeanConsumer());
        this.mSpecialSendDisruptor.start();
    }

    private void logDisruptorCapa(RingBuffer<DataBeanEvent> ringBuffer) {
        if (this.mDisruptorSendCount % 500 == 0) {
            DJILog.saveConnectDebug("---send disruptor capacity: " + ringBuffer.remainingCapacity());
            this.mDisruptorSendCount = 0;
        }
        this.mDisruptorSendCount++;
    }

    private int putPackToSpecialChannelToSend(DataBean dataBean, int key) {
        RingBuffer<DataBeanEvent> ringBuffer = this.mSpecialSendDisruptor.getRingBuffer();
        long seq = -1;
        try {
            seq = ringBuffer.tryNext();
            DataBeanEvent beanEvent = ringBuffer.get(seq);
            beanEvent.setDataBean(dataBean);
            beanEvent.setKey(key);
            this.isCanLogTrace = true;
            if (seq != -1) {
                ringBuffer.publish(seq);
            }
        } catch (InsufficientCapacityException e) {
            e.printStackTrace();
            logSendThread((int) seq);
            if (seq != -1) {
                ringBuffer.publish(seq);
            }
        } catch (Throwable th) {
            if (seq != -1) {
                ringBuffer.publish(seq);
            }
            throw th;
        }
        return (int) seq;
    }

    private void logSendThread(int seq) {
        if (this.mNormalLogTime % 25 == 0) {
            DJILog.saveConnectDebug("---send disruptor exception, seq: " + seq);
            this.mNormalLogTime = 0;
        }
        this.mNormalLogTime++;
        if (this.isCanLogTrace) {
            this.isCanLogTrace = false;
            DJILog.saveConnectDebug("---send disruptor exception, handler thread trace: " + DJILogUtils.getThreadStack(this.handlerThread));
            DJILog.saveConnectDebug("---send disruptor exception, disruptor thread trace: " + DJILogUtils.getThreadStack(this.mSpecialSendThread));
        }
    }

    private class SpecialDataBeanConsumer implements EventHandler<DataBeanEvent> {
        private SpecialDataBeanConsumer() {
        }

        public void onEvent(DataBeanEvent event, long sequence, boolean endOfBatch) throws Exception {
            if (DataBaseProcessor.this.mSpecialSendThread == null) {
                Thread unused = DataBaseProcessor.this.mSpecialSendThread = Thread.currentThread();
            }
            DataBaseProcessor.this.realSendOnePack(event.getDataBean(), event.getKey());
        }
    }
}
