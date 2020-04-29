package dji.midware.aoabridge;

import android.util.Log;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.aoabridge.AoaController;
import dji.midware.util.DJIEventBusUtil;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

@EXClassNullAway
public class AppClient {
    /* access modifiers changed from: private */
    public Socket client;
    /* access modifiers changed from: private */

    /* renamed from: in  reason: collision with root package name */
    public InputStream f5in;
    /* access modifiers changed from: private */
    public String ip;
    /* access modifiers changed from: private */
    public boolean isRcConnected = false;
    /* access modifiers changed from: private */
    public OutputStream out;
    /* access modifiers changed from: private */
    public int port;

    public enum Status {
        Connected,
        DisConnected
    }

    public AppClient(String ip2, int port2) {
        this.ip = ip2;
        this.port = port2;
    }

    public void init() {
        DJIEventBusUtil.register(this);
        new Thread("AppClientInit") {
            /* class dji.midware.aoabridge.AppClient.AnonymousClass1 */

            public void run() {
                try {
                    Log.d("AoaConnect", "client start");
                    Socket unused = AppClient.this.client = new Socket(AppClient.this.ip, AppClient.this.port);
                    Log.d("AoaConnect", "client connected");
                    InputStream unused2 = AppClient.this.f5in = AppClient.this.client.getInputStream();
                    OutputStream unused3 = AppClient.this.out = AppClient.this.client.getOutputStream();
                    EventBus.getDefault().post(Status.Connected);
                    AoaController.get().broadcastClientConnected(false, AppClient.this.client.getLocalPort());
                } catch (Exception e) {
                    e.printStackTrace();
                    AppClient.this.closeSocket();
                }
            }
        }.start();
        startReadThread();
    }

    public void uninit() {
        closeSocket();
        DJIEventBusUtil.unRegister(this);
    }

    /* access modifiers changed from: private */
    public void closeSocket() {
        try {
            if (this.f5in != null) {
                this.f5in.close();
                this.f5in = null;
            }
            if (this.out != null) {
                this.out.close();
                this.out = null;
            }
            if (this.client != null) {
                this.client.close();
                this.client = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void startReadThread() {
        new Thread("AppClient") {
            /* class dji.midware.aoabridge.AppClient.AnonymousClass2 */

            public void run() {
                byte[] buffer = new byte[8192];
                while (true) {
                    if (AppClient.this.isRcConnected || AppClient.this.f5in == null) {
                        try {
                            Thread.sleep(50);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    } else {
                        try {
                            AppClient.this.f5in.read(buffer);
                        } catch (IOException e2) {
                            e2.printStackTrace();
                        }
                    }
                }
            }
        }.start();
    }

    private void readData() {
        if (this.f5in != null) {
            try {
                byte[] buffer = new byte[8192];
                int len = this.f5in.read(buffer);
                while (len != -1) {
                    len = this.f5in.read(buffer);
                }
            } catch (Exception e) {
                e.printStackTrace();
                closeSocket();
            }
        }
    }

    public InputStream getInputStream() {
        return this.f5in;
    }

    public OutputStream getOuputStream() {
        return this.out;
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEvent3BackgroundThread(AoaController.RcEvent rcEvent) {
        if (rcEvent == AoaController.RcEvent.Connected) {
            this.isRcConnected = true;
        } else {
            this.isRcConnected = false;
        }
    }
}
