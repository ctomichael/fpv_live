package dji.midware.sockets.pub;

import dji.fieldAnnotation.EXClassNullAway;
import java.io.IOException;
import java.net.ServerSocket;
import java.util.Timer;
import java.util.TimerTask;

@EXClassNullAway
public abstract class SocketServer extends DJISocket {
    private boolean accepting = false;
    /* access modifiers changed from: private */
    public Timer checkTimer;
    /* access modifiers changed from: private */
    public ServerSocket serverSocket;

    public SocketServer(String ip, int port) {
        super(ip, port, null, 0);
        this.isServer = true;
    }

    public void destroy() {
        if (this.checkTimer != null) {
            this.checkTimer.cancel();
            this.checkTimer = null;
        }
        try {
            if (this.serverSocket != null) {
                this.serverSocket.close();
                this.serverSocket = null;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        super.destroy();
    }

    /* access modifiers changed from: protected */
    public void closeSocket() {
        super.closeSocket();
    }

    /* access modifiers changed from: private */
    public void checkConneted() {
        accept();
    }

    private void accept() {
        if (!this.accepting) {
            this.accepting = true;
            toAccept();
            this.accepting = false;
        }
    }

    private void toAccept() {
        if (this.socket != null) {
            try {
                this.socket.sendUrgentData(255);
            } catch (Exception e) {
                LOGE("socket 心跳出错");
                closeSocket();
                this.socket = null;
                onDisconnect();
            }
        } else {
            LOGE("监听客户端  start");
            try {
                this.socket = this.serverSocket.accept();
                LOGE("客户端有新的连接");
                try {
                    this.out = this.socket.getOutputStream();
                    this.input = this.socket.getInputStream();
                    onConnect();
                    LOGE("新的连接 work");
                } catch (IOException e2) {
                    LOGE("出错 " + e2.getMessage());
                }
                LOGE("监听客户端  end");
            } catch (Exception e3) {
            }
        }
    }

    /* access modifiers changed from: protected */
    public void connect() {
        new Thread(new Runnable() {
            /* class dji.midware.sockets.pub.SocketServer.AnonymousClass1 */

            public void run() {
                try {
                    if (SocketServer.this.serverSocket != null && !SocketServer.this.serverSocket.isClosed()) {
                        SocketServer.this.serverSocket.close();
                        ServerSocket unused = SocketServer.this.serverSocket = null;
                    }
                } catch (IOException e) {
                    SocketServer.this.LOGE("重连 " + e.getMessage());
                }
                SocketServer.this.toConnect();
            }
        }, "socketConnect").start();
    }

    /* access modifiers changed from: private */
    public void toConnect() {
        if (this.serverSocket == null) {
            try {
                resetHeartStatus();
                this.serverSocket = new ServerSocket();
                this.serverSocket.setReuseAddress(true);
                this.serverSocket.bind(this.address);
            } catch (Exception e) {
                LOGE("连接出错 " + e.getMessage());
            }
        }
    }

    /* access modifiers changed from: protected */
    public void startTimers() {
        new Thread(new Runnable() {
            /* class dji.midware.sockets.pub.SocketServer.AnonymousClass2 */

            public void run() {
                Timer unused = SocketServer.this.checkTimer = new Timer();
                SocketServer.this.checkTimer.schedule(new TimerTask() {
                    /* class dji.midware.sockets.pub.SocketServer.AnonymousClass2.AnonymousClass1 */

                    public void run() {
                        SocketServer.this.checkConneted();
                        SocketServer.this.checkReceiveThread();
                    }
                }, 100, 1000);
            }
        }, "SocketDaemon").start();
    }

    /* access modifiers changed from: protected */
    public void sendError() {
    }

    public boolean isOK() {
        return isConnected() && getHeartStatus();
    }
}
