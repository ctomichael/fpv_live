package dji.midware.sockets.pub;

import dji.fieldAnnotation.EXClassNullAway;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@EXClassNullAway
public abstract class SocketClient {
    public static String INADDR_ANY = "0.0.0.0";
    protected String TAG = getClass().getSimpleName();
    /* access modifiers changed from: private */
    public InetSocketAddress address;
    /* access modifiers changed from: private */
    public byte[] buffer = new byte[4096];
    /* access modifiers changed from: private */
    public Timer checkTimer;
    /* access modifiers changed from: private */
    public InputStream input;
    /* access modifiers changed from: private */
    public boolean isConnecting = false;
    /* access modifiers changed from: private */
    public boolean isUSB = true;
    /* access modifiers changed from: private */
    public OutputStream out;
    private Thread parseThread = null;
    protected RcvBufferBean rcvBufferBean = new RcvBufferBean();
    private Thread receiveThread = null;
    private Runnable runnable = new Runnable() {
        /* class dji.midware.sockets.pub.SocketClient.AnonymousClass4 */

        public void run() {
            while (SocketClient.this.isConnected()) {
                try {
                    int length = SocketClient.this.input.available();
                    SocketClient.this.input.read(SocketClient.this.buffer, 0, length);
                    if (length != 0) {
                        SocketClient.this.rcvBufferBean.put(SocketClient.this.buffer, length);
                    }
                    Thread.sleep(1);
                } catch (IOException e) {
                    SocketClient.this.LOGE("" + e.getMessage());
                    SocketClient.this.sendError();
                    return;
                } catch (Exception e2) {
                    SocketClient.this.LOGE("" + e2.getMessage());
                    return;
                }
            }
        }
    };
    private Runnable runnableForParse = new Runnable() {
        /* class dji.midware.sockets.pub.SocketClient.AnonymousClass5 */

        public void run() {
            while (SocketClient.this.isConnected()) {
                SocketClient.this.parse();
                try {
                    Thread.sleep(1);
                } catch (InterruptedException e) {
                    SocketClient.this.LOGE("" + e.getMessage());
                }
            }
        }
    };
    private ExecutorService sendThreadPool;
    /* access modifiers changed from: private */
    public Socket socket;

    public abstract void LOGD(String str);

    public abstract void LOGE(String str);

    /* access modifiers changed from: protected */
    public abstract boolean getHeartStatus();

    /* access modifiers changed from: protected */
    public abstract void parse();

    /* access modifiers changed from: protected */
    public abstract void resetHeartStatus();

    public SocketClient(String ip, int port) {
        LOGD("初始化");
        this.isUSB = ip.equals(INADDR_ANY);
        this.sendThreadPool = Executors.newSingleThreadExecutor();
        this.address = new InetSocketAddress(ip, port);
        connect();
        receiveMessage();
        parseMessage();
        startTimers();
    }

    public boolean isConnected() {
        try {
            if (this.socket == null || this.socket.isClosed() || !this.socket.isConnected()) {
                return false;
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendmessage(final byte[] data) {
        this.sendThreadPool.execute(new Runnable() {
            /* class dji.midware.sockets.pub.SocketClient.AnonymousClass1 */

            public void run() {
                if (SocketClient.this.isConnected()) {
                    try {
                        SocketClient.this.out.write(data, 0, data.length);
                        SocketClient.this.out.flush();
                        SocketClient.this.LOGD("发送成功");
                    } catch (IOException e) {
                        SocketClient.this.LOGE("" + e.getMessage());
                        if (!SocketClient.this.isUSB) {
                            SocketClient.this.sendError();
                        }
                    } catch (Exception e2) {
                        e2.printStackTrace();
                    }
                }
            }
        });
    }

    public void destroy() {
        if (this.checkTimer != null) {
            this.checkTimer.cancel();
            this.checkTimer = null;
        }
        try {
            if (this.socket != null) {
                this.socket.close();
                this.socket = null;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void connect() {
        if (!this.isConnecting) {
            new Thread(new Runnable() {
                /* class dji.midware.sockets.pub.SocketClient.AnonymousClass2 */

                public void run() {
                    boolean unused = SocketClient.this.isConnecting = true;
                    try {
                        if (SocketClient.this.socket != null && !SocketClient.this.socket.isClosed()) {
                            SocketClient.this.socket.close();
                            Socket unused2 = SocketClient.this.socket = null;
                        }
                    } catch (IOException e) {
                        SocketClient.this.LOGE("重连 " + e.getMessage());
                    }
                    try {
                        SocketClient.this.resetHeartStatus();
                        Socket unused3 = SocketClient.this.socket = new Socket();
                        SocketClient.this.socket.setKeepAlive(true);
                        SocketClient.this.socket.setSoLinger(true, 0);
                        SocketClient.this.socket.setReuseAddress(true);
                        SocketClient.this.socket.setSoTimeout(10);
                        SocketClient.this.socket.connect(SocketClient.this.address, 1000);
                        if (SocketClient.this.out != null) {
                            SocketClient.this.out.close();
                        }
                        if (SocketClient.this.input != null) {
                            SocketClient.this.input.close();
                        }
                        OutputStream unused4 = SocketClient.this.out = SocketClient.this.socket.getOutputStream();
                        InputStream unused5 = SocketClient.this.input = SocketClient.this.socket.getInputStream();
                    } catch (Exception e2) {
                        SocketClient.this.LOGE("重连出错 " + e2.getMessage());
                    }
                    boolean unused6 = SocketClient.this.isConnecting = false;
                }
            }, "socketClientDaemon").start();
        }
    }

    private void startTimers() {
        new Thread(new Runnable() {
            /* class dji.midware.sockets.pub.SocketClient.AnonymousClass3 */

            public void run() {
                Timer unused = SocketClient.this.checkTimer = new Timer();
                SocketClient.this.checkTimer.schedule(new TimerTask() {
                    /* class dji.midware.sockets.pub.SocketClient.AnonymousClass3.AnonymousClass1 */

                    public void run() {
                        SocketClient.this.checkAndReConnect();
                        SocketClient.this.checkReceiveThread();
                    }
                }, 1000, 2000);
            }
        }, "socketClientDaemon").start();
    }

    /* access modifiers changed from: private */
    public void checkAndReConnect() {
        if (!isOK()) {
            connect();
            LOGD("重连");
            return;
        }
        LOGD("tcp 目前连接正常");
    }

    /* access modifiers changed from: private */
    public void checkReceiveThread() {
        if (!this.receiveThread.isAlive() && isConnected()) {
            LOGD("receiveThread restart");
            this.receiveThread.interrupt();
            this.receiveThread = null;
            receiveMessage();
        }
        if (!this.parseThread.isAlive() && isConnected()) {
            LOGD("parseThread restart");
            this.parseThread.interrupt();
            this.parseThread = null;
            parseMessage();
        }
    }

    private void receiveMessage() {
        this.receiveThread = new Thread(this.runnable, "socketClientRece");
        this.receiveThread.setPriority(10);
        this.receiveThread.start();
    }

    private void parseMessage() {
        this.parseThread = new Thread(this.runnableForParse, "ParseDemon");
        this.parseThread.setPriority(10);
        this.parseThread.start();
    }

    /* access modifiers changed from: private */
    public void sendError() {
        try {
            if (this.socket != null && !this.socket.isClosed()) {
                this.socket.close();
                this.socket = null;
            }
        } catch (Exception e) {
            LOGE("" + e.getMessage());
        }
    }

    private boolean isOK() {
        return isConnected() && getHeartStatus();
    }
}
