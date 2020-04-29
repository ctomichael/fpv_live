package dji.midware.sockets.pub;

import android.net.LocalSocket;
import android.net.LocalSocketAddress;
import com.lmax.disruptor.EventHandler;
import com.lmax.disruptor.InsufficientCapacityException;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.TimeoutBlockingWaitStrategy;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;
import dji.log.DJILog;
import dji.logic.manager.DJIUSBWifiSwitchManager;
import dji.midware.data.manager.P3.DJIServiceInterface;
import dji.midware.data.manager.P3.ServiceManager;
import dji.midware.media.DJIVideoProcessTimeTest;
import dji.midware.usb.P3.RecvBufferEvent;
import dji.midware.usb.P3.RecvBufferEventFactory;
import dji.midware.util.save.StreamDataObserver;
import dji.pilot.fpv.model.IEventObjects;
import java.io.InputStream;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

public abstract class DJIStreamLocalSocket implements DJIServiceInterface, EventHandler<RecvBufferEvent> {
    protected static final int MAX_BUFFER_READ = 16384;
    private static final String TAG = "DJIStreamLocalSocket";
    protected volatile Timer mCheckTimer = null;
    protected final byte[] mInBuffer = new byte[16384];
    protected volatile LocalSocket mLocalSocket = null;
    protected final Object mLock = new Object();
    private StreamDataObserver mNeedPackObserver;
    protected volatile InputStream mReadInputStream = null;
    protected Thread mRecvThread = null;
    /* access modifiers changed from: private */
    public RingBuffer<RecvBufferEvent> mRingBuffer;
    protected final String mSocketName;
    protected volatile boolean mbRecvData = false;

    /* access modifiers changed from: protected */
    public abstract void parse(byte[] bArr, int i, int i2);

    public DJIStreamLocalSocket(String name) {
        this.mSocketName = name;
        startTimers();
    }

    public void destroy() {
        destroyTimers();
        saveLog("ExitSocket = destroy");
        exitSocket();
    }

    public boolean isConnected() {
        return this.mbRecvData && this.mReadInputStream != null;
    }

    public boolean isOK() {
        return isConnected();
    }

    public void setDataMode(boolean dataMode) {
    }

    public void pauseRecvThread() {
    }

    public void resumeRecvThread() {
    }

    public void pauseParseThread() {
    }

    public void resumeParseThread() {
    }

    public void pauseService(boolean isPause) {
    }

    public void startStream() {
    }

    public void stopStream() {
    }

    /* access modifiers changed from: protected */
    public void startTimers() {
        destroyTimers();
        this.mCheckTimer = new Timer("DJIStreamLocalSocketTimer");
        this.mCheckTimer.schedule(new TimerTask() {
            /* class dji.midware.sockets.pub.DJIStreamLocalSocket.AnonymousClass1 */

            public void run() {
                DJIStreamLocalSocket.this.checkConneted();
            }
        }, (long) IEventObjects.PopViewItem.DURATION_DISAPPEAR, 1500);
        saveLog("startTimers");
    }

    /* access modifiers changed from: protected */
    public void destroyTimers() {
        if (this.mCheckTimer != null) {
            this.mCheckTimer.cancel();
            this.mCheckTimer.purge();
            this.mCheckTimer = null;
            saveLog("destroyTimers");
        }
    }

    /* access modifiers changed from: protected */
    public void checkConneted() {
        if (ServiceManager.getContext() == null) {
            saveLog("ExitSocket = ServiceManager.getContext() == null");
            exitSocket();
        } else if (DJIUSBWifiSwitchManager.getInstance().isUSBAoaConnected()) {
            saveLog("ExitSocket = DJIUSBWifiSwitchManager.getInstance().isUSBAoaConnected()");
            exitSocket();
        } else if (!isConnected()) {
            connect();
        }
    }

    /* access modifiers changed from: protected */
    public void connect() {
        closeSocket();
        try {
            this.mLocalSocket = new LocalSocket();
            this.mLocalSocket.connect(new LocalSocketAddress(this.mSocketName, LocalSocketAddress.Namespace.RESERVED));
            this.mLocalSocket.setSoTimeout(5000);
            this.mReadInputStream = this.mLocalSocket.getInputStream();
            startRecvThread();
            if (isConnected()) {
                onConnect();
            }
        } catch (Exception e) {
            saveLog("localSocketError = " + e);
            closeSocket();
            if (isConnected()) {
                onConnect();
            }
        } catch (Throwable th) {
            if (isConnected()) {
                onConnect();
            }
            throw th;
        }
    }

    private void closeSocket() {
        if (this.mLocalSocket != null) {
            synchronized (this.mLock) {
                if (this.mReadInputStream != null) {
                    try {
                        this.mReadInputStream.close();
                        this.mReadInputStream = null;
                    } catch (Exception e) {
                        this.mLocalSocket = null;
                    } catch (Exception e2) {
                        this.mReadInputStream = null;
                    } catch (Throwable th) {
                        this.mReadInputStream = null;
                        throw th;
                    }
                }
                this.mLocalSocket.shutdownInput();
                this.mLocalSocket.close();
                this.mLocalSocket = null;
            }
            onDisconnect();
        }
    }

    /* access modifiers changed from: protected */
    public void startRecvThread() {
        closeRecvThread();
        this.mbRecvData = true;
        this.mRecvThread = new Thread(createRecvRunnable(), "LocalSockerStartRecv");
        this.mRecvThread.setPriority(9);
        this.mRecvThread.start();
    }

    private void closeRecvThread() {
        if (this.mbRecvData) {
            this.mbRecvData = false;
        }
    }

    private Runnable createRecvRunnable() {
        return new Runnable() {
            /* class dji.midware.sockets.pub.DJIStreamLocalSocket.AnonymousClass2 */
            ThreadFactory disruptorThreadFactory = new ThreadFactory() {
                /* class dji.midware.sockets.pub.DJIStreamLocalSocket.AnonymousClass2.AnonymousClass1 */

                public Thread newThread(Runnable r) {
                    Thread ret = new Thread(r, "dpadStream-localSocket-disruptor");
                    ret.setPriority(9);
                    return ret;
                }
            };
            Disruptor<RecvBufferEvent> mRecvDisruptor;

            private void initDisruptor() {
                this.mRecvDisruptor = new Disruptor<>(new RecvBufferEventFactory(), 512, this.disruptorThreadFactory, ProducerType.SINGLE, new TimeoutBlockingWaitStrategy(200, TimeUnit.MICROSECONDS));
                DJIStreamLocalSocket.this.setRingBuffer(this.mRecvDisruptor.getRingBuffer());
                this.mRecvDisruptor.handleEventsWith(DJIStreamLocalSocket.this);
                this.mRecvDisruptor.start();
            }

            public void run() {
                initDisruptor();
                while (DJIStreamLocalSocket.this.mbRecvData && DJIStreamLocalSocket.this.mReadInputStream != null) {
                    try {
                        int recvLen = DJIStreamLocalSocket.this.mReadInputStream.read(DJIStreamLocalSocket.this.mInBuffer, 0, 16384);
                        if (recvLen > 0) {
                            if (DJIStreamLocalSocket.this.mRingBuffer != null) {
                                DJIStreamLocalSocket.this.putPackToDisruptorQueue(DJIStreamLocalSocket.this.mInBuffer, recvLen);
                            }
                        } else if (recvLen == -1) {
                            DJIStreamLocalSocket.this.saveLog("ExitSocket = " + recvLen);
                            DJIStreamLocalSocket.this.exitSocket();
                        }
                    } catch (Exception e) {
                        DJIStreamLocalSocket.this.saveLog("ExitSocket = exception: " + e.getMessage());
                        DJIStreamLocalSocket.this.exitSocket();
                    }
                }
                if (this.mRecvDisruptor != null) {
                    this.mRecvDisruptor.shutdown();
                }
            }
        };
    }

    /* access modifiers changed from: private */
    public void putPackToDisruptorQueue(byte[] bytes, int length) {
        long sequence = -1;
        try {
            sequence = this.mRingBuffer.tryNext();
            this.mRingBuffer.get(sequence).setBuffer(bytes, length);
            DJIVideoProcessTimeTest.getInstance().onVideoDataProcessing(DJIVideoProcessTimeTest.VideoProcessPoint.DataRead, bytes, 0);
            if (sequence != -1) {
                this.mRingBuffer.publish(sequence);
            }
        } catch (InsufficientCapacityException e) {
            e.printStackTrace();
            saveLog("stream socket put to buffer return -1!!!");
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

    /* access modifiers changed from: protected */
    public void exitSocket() {
        closeRecvThread();
        closeSocket();
    }

    public void setRingBuffer(RingBuffer<RecvBufferEvent> ringBuffer) {
        this.mRingBuffer = ringBuffer;
    }

    public void onEvent(RecvBufferEvent event, long sequence, boolean endOfBatch) {
        if (this.mNeedPackObserver == null) {
            this.mNeedPackObserver = StreamDataObserver.getInstance(StreamDataObserver.ObservingPoint.DPadUdpServiceParse);
        }
        this.mNeedPackObserver.onDataRecv(StreamDataObserver.ObservingContext.ByteRate, (long) event.getLength());
        parse(event.getBuffer(), 0, event.getLength());
    }

    /* access modifiers changed from: private */
    public void saveLog(String msg) {
        DJILog.logWriteE(TAG, msg, TAG, new Object[0]);
    }
}
