package dji.midware.aoabridge;

import android.util.Log;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.aoabridge.AoaController;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import org.greenrobot.eventbus.EventBus;

@EXClassNullAway
public class AppEventClient {
    /* access modifiers changed from: private */
    public Socket client;
    /* access modifiers changed from: private */

    /* renamed from: in  reason: collision with root package name */
    public InputStream f4in;
    /* access modifiers changed from: private */
    public String ip;
    private boolean isConnected = false;
    /* access modifiers changed from: private */
    public OutputStream out;
    /* access modifiers changed from: private */
    public int port;

    public enum Event {
        Connected,
        DisConnected
    }

    public AppEventClient(String ip2, int port2) {
        this.ip = ip2;
        this.port = port2;
    }

    public void init() {
        new Thread("appEventClient") {
            /* class dji.midware.aoabridge.AppEventClient.AnonymousClass1 */

            public void run() {
                while (true) {
                    try {
                        Socket unused = AppEventClient.this.client = new Socket(AppEventClient.this.ip, AppEventClient.this.port);
                        InputStream unused2 = AppEventClient.this.f4in = AppEventClient.this.client.getInputStream();
                        OutputStream unused3 = AppEventClient.this.out = AppEventClient.this.client.getOutputStream();
                        AoaController.get().broadcastClientConnected(true, AppEventClient.this.client.getLocalPort());
                    } catch (Exception e) {
                        e.printStackTrace();
                        AppEventClient.this.closeSocket();
                    }
                    AppEventClient.this.readData();
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e2) {
                        e2.printStackTrace();
                    }
                }
            }
        }.start();
    }

    public void uninit() {
        closeSocket();
    }

    /* access modifiers changed from: private */
    public void readData() {
        byte[] allData;
        if (this.f4in != null) {
            try {
                byte[] buffer = new byte[8192];
                byte[] lastData = null;
                while (true) {
                    int len = this.f4in.read(buffer);
                    if (len > 0) {
                        if (lastData != null) {
                            allData = new byte[(lastData.length + len)];
                            System.arraycopy(lastData, 0, allData, 0, lastData.length);
                            System.arraycopy(buffer, 0, allData, lastData.length, len);
                        } else {
                            allData = new byte[len];
                            System.arraycopy(buffer, 0, allData, 0, len);
                        }
                        int allDataLen = allData.length;
                        byte[] data = new byte[30];
                        int index = 0;
                        while (index + 30 <= allDataLen) {
                            System.arraycopy(allData, index, data, 0, 30);
                            index += 30;
                            DJIBaseCommData commData = new DJIBaseCommData();
                            if (commData.parseData(data, 30) && commData.sender != 99) {
                                AoaController.RcEvent rcEvent = AoaController.RcEvent.values()[commData.eventType];
                                Log.d("AppEventClient", "EventBus post : " + this.isConnected);
                                if (rcEvent == AoaController.RcEvent.Connected) {
                                    this.isConnected = true;
                                } else {
                                    this.isConnected = false;
                                }
                                EventBus.getDefault().post(rcEvent);
                                Log.d("AppEventClient", "isConnected : " + this.isConnected);
                            }
                        }
                        int lastLen = allDataLen - index;
                        if (lastLen > 0) {
                            lastData = new byte[lastLen];
                            System.arraycopy(allData, index, lastData, 0, lastLen);
                        } else {
                            lastData = null;
                        }
                    } else {
                        Thread.sleep(50);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                closeSocket();
            }
        }
    }

    /* access modifiers changed from: private */
    public void closeSocket() {
        try {
            if (this.f4in != null) {
                this.f4in.close();
                this.f4in = null;
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

    public boolean isRcConnected() {
        return this.isConnected;
    }
}
