package dji.midware.sockets.pub;

import android.util.Log;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.manager.P3.DJIServiceInterface;
import dji.midware.data.packages.P3.SendPack;
import dji.midware.natives.UDT;
import dji.midware.util.save.StreamDataObserver;
import dji.publics.LogReport.base.Fields;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@EXClassNullAway
public abstract class DJIUdtSocket implements DJIServiceInterface {
    /* access modifiers changed from: private */
    public int MaxBuffLen = 4096;
    protected String TAG = getClass().getSimpleName();
    /* access modifiers changed from: private */
    public byte[] buffer = new byte[this.MaxBuffLen];
    protected String ip;
    protected volatile boolean isConnecting = false;
    protected String localIp;
    protected String localPort;
    protected String port;
    private Thread receiveThread;
    private Runnable runnable = new Runnable() {
        /* class dji.midware.sockets.pub.DJIUdtSocket.AnonymousClass2 */

        public void run() {
            while (DJIUdtSocket.this.isConnected() && DJIUdtSocket.this.socket != -1) {
                int recvLen = UDT.recv(DJIUdtSocket.this.socket, DJIUdtSocket.this.buffer, 0, DJIUdtSocket.this.MaxBuffLen, 0);
                if (DJIUdtSocket.this.udtObserver == null) {
                    StreamDataObserver unused = DJIUdtSocket.this.udtObserver = StreamDataObserver.getInstance(StreamDataObserver.ObservingPoint.UdtRunnable);
                }
                DJIUdtSocket.this.udtObserver.onDataRecv(StreamDataObserver.ObservingContext.ByteRate, (long) recvLen);
                if (recvLen >= 0) {
                    if (recvLen > 0) {
                        DJIUdtSocket.this.parse(DJIUdtSocket.this.buffer, recvLen);
                    }
                } else if (recvLen == -2) {
                    DJIUdtSocket.this.closeSocket();
                    DJIUdtSocket.this.onDisconnect();
                    DJIUdtSocket.this.LOGE("udt 连接断开 recvLen=" + recvLen + " ip:" + DJIUdtSocket.this.ip + "port:" + DJIUdtSocket.this.port);
                } else if (recvLen == -1) {
                    DJIUdtSocket.this.LOGE("udt 无数据 recvLen=" + recvLen);
                }
            }
            DJIUdtSocket.this.LOGE("socket recv thread over");
        }
    };
    private ExecutorService sendThreadPool;
    protected int sleepTime = 100;
    protected volatile int socket = -1;
    /* access modifiers changed from: private */
    public StreamDataObserver udtObserver;

    public abstract void LOGD(String str);

    public abstract void LOGE(String str);

    /* access modifiers changed from: protected */
    public abstract void connect();

    /* access modifiers changed from: protected */
    public abstract void parse(byte[] bArr, int i);

    /* access modifiers changed from: protected */
    public abstract void sendError();

    /* access modifiers changed from: protected */
    public abstract void startTimers();

    static {
        Log.e(Fields.Dgo_update.STEP, "step UDT.startup() start " + System.currentTimeMillis());
        UDT.startup();
        Log.e(Fields.Dgo_update.STEP, "step UDT.startup() end " + System.currentTimeMillis());
    }

    public DJIUdtSocket(String ip2, String port2, String localIp2, String localPort2) {
        this.ip = ip2;
        this.port = port2;
        this.localIp = localIp2;
        this.localPort = localPort2;
        this.sendThreadPool = Executors.newSingleThreadExecutor();
        this.receiveThread = new Thread(this.runnable, "udtReceTHread");
        startTimers();
    }

    public boolean isConnected() {
        return this.socket != -1 && this.isConnecting;
    }

    public void sendmessage(final SendPack data) {
        if (this.sendThreadPool == null || this.sendThreadPool.isShutdown() || this.sendThreadPool.isTerminated()) {
            data.bufferObject.noUsed();
        } else {
            this.sendThreadPool.execute(new Runnable() {
                /* class dji.midware.sockets.pub.DJIUdtSocket.AnonymousClass1 */

                public void run() {
                    if (!DJIUdtSocket.this.isConnected() || DJIUdtSocket.this.socket == -1) {
                        data.bufferObject.noUsed();
                        return;
                    }
                    if (UDT.send(DJIUdtSocket.this.socket, data.buffer, 0, data.getLength(), 0) != data.getLength()) {
                        DJIUdtSocket.this.sendError();
                    }
                    data.bufferObject.noUsed();
                }
            });
        }
    }

    public void destroy() {
        if (this.sendThreadPool != null) {
            this.sendThreadPool.shutdown();
            this.sendThreadPool = null;
        }
        LOGE("udt destroy 1 " + this.port);
        LOGE("udt destroy 2 " + this.port);
    }

    /* access modifiers changed from: protected */
    public void checkReceiveThread() {
        if (this.receiveThread == null) {
            receiveMessage();
        } else if (!this.receiveThread.isAlive() && isConnected()) {
            LOGD("receiveThread restart");
            this.receiveThread.interrupt();
            this.receiveThread = null;
            receiveMessage();
        }
    }

    private void receiveMessage() {
        this.receiveThread = new Thread(this.runnable, "UdtReceiver");
        this.receiveThread.setPriority(9);
        this.receiveThread.start();
    }

    /* access modifiers changed from: protected */
    public void closeSocket() {
        if (this.socket != -1) {
            this.isConnecting = false;
            UDT.close(this.socket);
            this.socket = -1;
        }
    }

    public boolean isOK() {
        return isConnected();
    }
}
