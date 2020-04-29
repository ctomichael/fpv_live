package dji.midware.sockets.pub;

import dji.fieldAnnotation.EXClassNullAway;
import dji.log.DJILogHelper;
import dji.midware.data.manager.P3.DJIServiceInterface;
import dji.midware.data.packages.P3.SendPack;
import dji.midware.natives.UDT;
import dji.sdksharedlib.hardware.abstractions.remotecontroller.DJIRCAbstraction;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@EXClassNullAway
public abstract class DJISwUdpSocket implements DJIServiceInterface {
    protected String TAG = getClass().getSimpleName();
    protected ArrayList<IpPortConfig> configList;
    protected IpPortConfig currentConfig;
    /* access modifiers changed from: private */
    public volatile long sendCount = 0;
    private ExecutorService sendThreadPool;

    public abstract void LOGD(String str);

    public abstract void LOGE(String str);

    /* access modifiers changed from: protected */
    public abstract void closeSocket();

    /* access modifiers changed from: protected */
    public abstract void connect();

    /* access modifiers changed from: protected */
    public abstract void startTimers();

    static /* synthetic */ long access$010(DJISwUdpSocket x0) {
        long j = x0.sendCount;
        x0.sendCount = j - 1;
        return j;
    }

    public DJISwUdpSocket(ArrayList<IpPortConfig> list) {
        this.configList = list;
        this.sendThreadPool = Executors.newSingleThreadExecutor();
    }

    public void sendmessage(final SendPack data) {
        if (this.sendThreadPool == null || this.sendThreadPool.isShutdown() || this.sendThreadPool.isTerminated()) {
            data.bufferObject.noUsed();
            return;
        }
        this.sendCount++;
        this.sendThreadPool.execute(new Runnable() {
            /* class dji.midware.sockets.pub.DJISwUdpSocket.AnonymousClass1 */

            public void run() {
                if (!DJISwUdpSocket.this.isConnected() || DJISwUdpSocket.this.sendCount > 5) {
                    DJILogHelper.getInstance().LOGE(DJISwUdpSocket.this.TAG, "sendCount > 5");
                    DJISwUdpSocket.access$010(DJISwUdpSocket.this);
                    data.bufferObject.noUsed();
                    return;
                }
                if (data.getLength() > 13 && data.buffer[9] == 1 && data.buffer[10] == 2) {
                    UDT.SwUdpJoyStickSend(data.buffer, 0, data.getLength());
                } else {
                    UDT.SwUdpSend(data.buffer, 0, data.getLength());
                }
                DJISwUdpSocket.access$010(DJISwUdpSocket.this);
                data.bufferObject.noUsed();
            }
        });
    }

    public void destroy() {
        String port = DJIRCAbstraction.DEFAULT_FIRMWARE_VERSION;
        if (this.currentConfig != null) {
            port = String.valueOf(this.currentConfig.port);
        }
        LOGE("sw udt destroy 1 " + port);
        closeSocket();
        LOGE("sw udt destroy 2 " + port);
        this.currentConfig = null;
    }
}
