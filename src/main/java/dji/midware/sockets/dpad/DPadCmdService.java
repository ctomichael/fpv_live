package dji.midware.sockets.dpad;

import android.util.Log;
import com.drew.metadata.exif.makernotes.SonyType1MakernoteDirectory;
import com.lmax.disruptor.EventHandler;
import com.lmax.disruptor.InsufficientCapacityException;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.TimeoutBlockingWaitStrategy;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;
import dji.diagnostics.model.DJIDiagnosticsError;
import dji.log.DJILog;
import dji.log.DJILogHelper;
import dji.midware.data.manager.P3.DJIPackManager;
import dji.midware.data.manager.P3.ServiceManager;
import dji.midware.sockets.pub.SocketTcpClient;
import dji.midware.usb.P3.RecvBufferEvent;
import dji.midware.usb.P3.RecvBufferEventFactory;
import dji.midware.util.save.StreamDataObserver;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

public class DPadCmdService extends SocketTcpClient implements EventHandler<RecvBufferEvent> {
    private static final String TAG = "DPadCmdService";
    private static DPadCmdService instance;
    private static String ip = "127.0.0.1";
    private static int port = DJIDiagnosticsError.Gimbal.ROTATION_ERROR;
    private long count = 0;
    ThreadFactory disruptorThreadFactory = new ThreadFactory() {
        /* class dji.midware.sockets.dpad.DPadCmdService.AnonymousClass1 */

        public Thread newThread(Runnable r) {
            Thread ret = new Thread(r, "dpadCmd-disruptor");
            ret.setPriority(9);
            return ret;
        }
    };
    private long lastT = 0;
    private StreamDataObserver mPackRateObserver;
    Disruptor<RecvBufferEvent> mRecvDisruptor;
    private RingBuffer<RecvBufferEvent> mRingBuffer;
    private DJIPackManager packManager = DJIPackManager.getInstance();

    private DPadCmdService() {
        super(ip, port);
        initDisruptor();
    }

    /* access modifiers changed from: protected */
    public void initBufferAttrs() {
        this.mBufferType = 3;
        this.mMinBufferLength = 64;
        this.buffer = new byte[SonyType1MakernoteDirectory.TAG_CONTRAST];
    }

    public static synchronized DPadCmdService getInstance() {
        DPadCmdService dPadCmdService;
        synchronized (DPadCmdService.class) {
            if (instance == null) {
                instance = new DPadCmdService();
            }
            dPadCmdService = instance;
        }
        return dPadCmdService;
    }

    public static void Destroy() {
        if (instance != null) {
            instance.destroy();
        }
    }

    public void startStream() {
    }

    public void stopStream() {
    }

    public void destroy() {
        super.destroy();
        stopStream();
        instance = null;
    }

    /* access modifiers changed from: protected */
    public boolean canDo() {
        if (ServiceManager.getContext() == null) {
            return false;
        }
        return true;
    }

    public void onDisconnect() {
        boolean lastState = this.isConnected;
        super.onDisconnect();
        if (lastState) {
            DPadWifiService.getInstance().onDisconnect();
            DJILog.saveConnectDebug("DpadCmdServices onDisconnect invoke: " + DJILog.getCurrentStack());
        }
    }

    public void onConnect() {
        DPadWifiService.getInstance().onConnect();
        DJILog.saveConnectDebug("DpadCmdServices onConnect invoke: " + DJILog.getCurrentStack());
    }

    public void parse(byte[] buffer, int offset, int len) {
        if (this.mRingBuffer != null) {
            putPackToDisruptorQueue(buffer, len);
        }
    }

    private void putPackToDisruptorQueue(byte[] bytes, int length) {
        long sequence = -1;
        try {
            sequence = this.mRingBuffer.tryNext();
            this.mRingBuffer.get(sequence).setBuffer(bytes, length);
            if (sequence != -1) {
                this.mRingBuffer.publish(sequence);
            }
        } catch (InsufficientCapacityException e) {
            e.printStackTrace();
            saveLog("cmd socket put to buffer return -1!!!");
            if (sequence != -1) {
                this.mRingBuffer.publish(sequence);
            }
        } catch (Throwable th) {
            if (sequence != -1) {
                this.mRingBuffer.publish(sequence);
            }
            throw th;
        }
    }

    private void saveLog(String content) {
        DJILog.logWriteE(TAG, content, TAG, new Object[0]);
    }

    private void printRate(int length) {
        this.count += (long) length;
        if (getTickCount() - this.lastT > 1000) {
            float rate = (((float) this.count) * 1.0f) / 1024.0f;
            if (rate > 1024.0f) {
                Log.d("download", String.format("rate %.2f MB\n", Float.valueOf(rate / 1024.0f)));
            } else {
                Log.d("download", String.format("rate %.2f KB\n", Float.valueOf(rate)));
            }
            this.lastT = getTickCount();
            this.count = 0;
        }
    }

    private long getTickCount() {
        return System.currentTimeMillis();
    }

    /* access modifiers changed from: protected */
    public boolean getHeartStatus() {
        return true;
    }

    public boolean isConnected() {
        return super.isConnected();
    }

    /* access modifiers changed from: protected */
    public void resetHeartStatus() {
    }

    public void LOGD(String s) {
        DJILogHelper.getInstance().LOGD(TAG, s, false, false);
    }

    public void LOGE(String s) {
        DJILogHelper.getInstance().LOGE(TAG, s, false, false);
        DJILog.saveConnectDebug(s);
    }

    public boolean isRemoteOK() {
        return false;
    }

    public void setDataMode(boolean dataMode) {
    }

    public void pauseService(boolean isPause) {
    }

    public void pauseRecvThread() {
    }

    public void resumeRecvThread() {
    }

    public void pauseParseThread() {
    }

    public void resumeParseThread() {
    }

    private void initDisruptor() {
        this.mRecvDisruptor = new Disruptor<>(new RecvBufferEventFactory(), 512, this.disruptorThreadFactory, ProducerType.SINGLE, new TimeoutBlockingWaitStrategy(200, TimeUnit.MICROSECONDS));
        setRingBuffer(this.mRecvDisruptor.getRingBuffer());
        this.mRecvDisruptor.handleEventsWith(this);
        this.mRecvDisruptor.start();
    }

    public void setRingBuffer(RingBuffer<RecvBufferEvent> ringBuffer) {
        this.mRingBuffer = ringBuffer;
    }

    public void onEvent(RecvBufferEvent event, long sequence, boolean endOfBatch) {
        if (this.mPackRateObserver == null) {
            this.mPackRateObserver = StreamDataObserver.getInstance(StreamDataObserver.ObservingPoint.DPadCmdServiceParse);
        }
        this.mPackRateObserver.onDataRecv(StreamDataObserver.ObservingContext.ByteRate, (long) event.getLength());
        this.packManager.parse(event.getBuffer(), 0, event.getLength());
    }
}
