package dji.midware.sockets.pub;

import android.net.wifi.WifiManager;
import dji.log.DJILogHelper;
import dji.logic.manager.DJIUSBWifiSwitchManager;
import dji.midware.data.manager.P3.DJIServiceInterface;
import dji.midware.data.manager.P3.ServiceManager;
import dji.midware.data.packages.P3.SendPack;
import dji.pilot.fpv.model.IEventObjects;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

public abstract class DJIUdpServerSocket implements DJIServiceInterface {
    protected static final int MAX_BUFFER_READ = 102400;
    private static final boolean NEED_MULTILOCK = false;
    /* access modifiers changed from: private */
    public static final String TAG = DJIUdpServerSocket.class.getSimpleName();
    protected Timer mCheckTimer = null;
    protected ExecutorService mExecutorService = null;
    protected final String mIP;
    protected final byte[] mInBuffer = new byte[102400];
    protected volatile DatagramPacket mInPacket = null;
    protected WifiManager.MulticastLock mMulticastLock = null;
    protected final byte[] mOutBuffer = new byte[1];
    protected final DatagramPacket mOutPacket = new DatagramPacket(this.mOutBuffer, this.mOutBuffer.length);
    protected final int mPort;
    protected Thread mRecvThread = null;
    protected DatagramSocket mSocket = null;
    protected volatile boolean mbRecvData = false;

    /* access modifiers changed from: protected */
    public abstract void parse(byte[] bArr, int i, int i2);

    public DJIUdpServerSocket(String ip, int port) {
        this.mIP = ip;
        this.mPort = port;
        startTimers();
    }

    public void sendmessage(final SendPack data) {
        if (this.mExecutorService == null || this.mExecutorService.isShutdown() || this.mExecutorService.isTerminated()) {
            data.bufferObject.noUsed();
        } else {
            this.mExecutorService.execute(new Runnable() {
                /* class dji.midware.sockets.pub.DJIUdpServerSocket.AnonymousClass1 */

                public void run() {
                    if (!DJIUdpServerSocket.this.isConnected()) {
                        data.bufferObject.noUsed();
                        return;
                    }
                    try {
                        DJIUdpServerSocket.this.mOutPacket.setData(data.buffer, 0, data.getLength());
                        DJIUdpServerSocket.this.mSocket.send(DJIUdpServerSocket.this.mOutPacket);
                    } catch (Exception e) {
                        DJILogHelper.getInstance().LOGD(DJIUdpServerSocket.TAG, "UDP sendData error:" + e.getMessage());
                    } finally {
                        DJIUdpServerSocket.this.mOutPacket.setData(DJIUdpServerSocket.this.mOutBuffer, 0, 1);
                        data.bufferObject.noUsed();
                    }
                }
            });
        }
    }

    public void destroy() {
        destroyTimers();
        exitSocket();
    }

    public boolean isConnected() {
        return this.mSocket != null;
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
    public void checkConneted() {
        if (ServiceManager.getContext() == null) {
            exitSocket();
        } else if (DJIUSBWifiSwitchManager.getInstance().isUSBAoaConnected()) {
            exitSocket();
        } else if (!isConnected()) {
            connect();
        }
    }

    /* access modifiers changed from: protected */
    public void connect() {
        closeSocket();
        try {
            this.mSocket = new DatagramSocket(this.mPort);
            this.mSocket.setBroadcast(true);
            this.mSocket.setReuseAddress(true);
            this.mSocket.setSoTimeout(5000);
            if (isConnected()) {
                onConnect();
                startRecvThread();
                startSendThread();
            }
        } catch (Exception e) {
            closeSocket();
            if (isConnected()) {
                onConnect();
                startRecvThread();
                startSendThread();
            }
        } catch (Throwable th) {
            if (isConnected()) {
                onConnect();
                startRecvThread();
                startSendThread();
            }
            throw th;
        }
    }

    /* access modifiers changed from: protected */
    public void startTimers() {
        destroyTimers();
        this.mCheckTimer = new Timer(TAG + "Timer");
        this.mCheckTimer.schedule(new TimerTask() {
            /* class dji.midware.sockets.pub.DJIUdpServerSocket.AnonymousClass2 */

            public void run() {
                DJIUdpServerSocket.this.checkConneted();
            }
        }, (long) IEventObjects.PopViewItem.DURATION_DISAPPEAR, 5000);
    }

    /* access modifiers changed from: protected */
    public void destroyTimers() {
        if (this.mCheckTimer != null) {
            this.mCheckTimer.cancel();
            this.mCheckTimer.purge();
            this.mCheckTimer = null;
        }
    }

    /* access modifiers changed from: protected */
    public void requestMultiLock() {
    }

    /* access modifiers changed from: protected */
    public void releaseMultiLock() {
    }

    /* access modifiers changed from: protected */
    public void startSendThread() {
        closeSendThread();
    }

    /* access modifiers changed from: protected */
    public void startRecvThread() {
        closeRecvThread();
        this.mInPacket = new DatagramPacket(new byte[102400], 0, 102400);
        this.mRecvThread = new Thread(createRecvRunnable(), "UdpServer");
        this.mRecvThread.setPriority(9);
        this.mRecvThread.start();
        this.mbRecvData = true;
    }

    private Runnable createRecvRunnable() {
        return new Runnable() {
            /* class dji.midware.sockets.pub.DJIUdpServerSocket.AnonymousClass3 */

            public void run() {
                while (DJIUdpServerSocket.this.mbRecvData && DJIUdpServerSocket.this.mInPacket != null) {
                    DJIUdpServerSocket.this.mInPacket.setData(DJIUdpServerSocket.this.mInBuffer, 0, 102400);
                    try {
                        DJIUdpServerSocket.this.requestMultiLock();
                        DJIUdpServerSocket.this.mSocket.receive(DJIUdpServerSocket.this.mInPacket);
                        int recvLen = DJIUdpServerSocket.this.mInPacket.getLength();
                        if (recvLen > 0) {
                            DJIUdpServerSocket.this.parse(DJIUdpServerSocket.this.mInPacket.getData(), DJIUdpServerSocket.this.mInPacket.getOffset(), recvLen);
                        }
                    } catch (Exception e) {
                        DJIUdpServerSocket.this.exitSocket();
                    } finally {
                        DJIUdpServerSocket.this.releaseMultiLock();
                    }
                }
            }
        };
    }

    private void closeSocket() {
        if (this.mSocket != null) {
            try {
                this.mSocket.close();
            } catch (Exception e) {
            } finally {
                this.mSocket = null;
            }
            onDisconnect();
        }
    }

    private void closeSendThread() {
        if (this.mExecutorService != null) {
            this.mExecutorService.shutdown();
            try {
                if (!this.mExecutorService.awaitTermination(200, TimeUnit.MILLISECONDS)) {
                    this.mExecutorService.shutdownNow();
                    this.mExecutorService.awaitTermination(200, TimeUnit.MILLISECONDS);
                }
            } catch (InterruptedException e) {
                this.mExecutorService.shutdownNow();
            } finally {
                this.mExecutorService = null;
            }
        }
    }

    private void closeRecvThread() {
        this.mbRecvData = false;
        this.mInPacket = null;
    }

    /* access modifiers changed from: protected */
    public void exitSocket() {
        closeSendThread();
        closeRecvThread();
        closeSocket();
    }
}
