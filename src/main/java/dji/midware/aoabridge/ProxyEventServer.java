package dji.midware.aoabridge;

import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.aoabridge.AoaController;
import dji.midware.aoabridge.DJIBaseCommData;
import dji.midware.data.manager.P3.DataEvent;
import dji.midware.data.manager.P3.ServiceManager;
import dji.midware.util.DJIEventBusUtil;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

@EXClassNullAway
public class ProxyEventServer {
    /* access modifiers changed from: private */
    public EventClientInfo curClient;
    private DataEvent dataEvent = DataEvent.ConnectLose;
    /* access modifiers changed from: private */
    public Map<String, EventClientInfo> ipMap;
    private int port = -1;
    private ServerSocket server;

    public ProxyEventServer(int port2) {
        this.port = port2;
    }

    public void init() {
        new Thread("ProxyEvent") {
            /* class dji.midware.aoabridge.ProxyEventServer.AnonymousClass1 */

            public void run() {
                ProxyEventServer.this.initSocket();
            }
        }.start();
    }

    public void uninit() {
        closeServer();
        DJIEventBusUtil.unRegister(this);
    }

    public void switchClient(String packageName) {
        EventClientInfo info = getEventClientInfo(packageName);
        if (info != null && info != this.curClient) {
            if (this.curClient != null && ServiceManager.getInstance().isConnected()) {
                sendEvent(AoaController.RcEvent.DisConnected);
            }
            this.curClient = info;
            if (ServiceManager.getInstance().isConnected()) {
                sendEvent(AoaController.RcEvent.Connected);
            }
        }
    }

    /* access modifiers changed from: private */
    public void sendEvent() {
        AoaController.RcEvent event;
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        boolean isConnected = ServiceManager.getInstance().isConnected();
        if (ServiceManager.getInstance().isConnected()) {
            event = AoaController.RcEvent.Connected;
        } else {
            event = AoaController.RcEvent.DisConnected;
        }
        sendEvent(event);
    }

    public EventClientInfo getEventClientInfo(String packageName) {
        if (packageName == null) {
            return null;
        }
        for (String ip : this.ipMap.keySet()) {
            EventClientInfo info = this.ipMap.get(ip);
            if (packageName.equals(info.packageName)) {
                return info;
            }
        }
        return null;
    }

    private void sendEvent(AoaController.RcEvent event) {
        DJIBaseCommData commData = new DJIBaseCommData();
        commData.setNumberWithTag(5, DJIBaseCommData.DJIBaseCommDataTag.DJIBaseCommData_Who.ordinal());
        commData.setNumberWithTag(event.ordinal(), DJIBaseCommData.DJIBaseCommDataTag.DJIBaseCommData_Event.ordinal());
        byte[] data = commData.packedData();
        sendByte(data, data.length);
    }

    /* access modifiers changed from: private */
    public void initSocket() {
        DJIEventBusUtil.register(this);
        try {
            this.ipMap = new HashMap();
            this.server = new ServerSocket(this.port);
            while (true) {
                Socket socket = this.server.accept();
                if (socket != null) {
                    EventClientInfo info = new EventClientInfo(socket, socket.getInputStream(), socket.getOutputStream());
                    this.ipMap.put(info.ip, info);
                    info.start();
                } else {
                    return;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void closeServer() {
        closeServerSocket();
        try {
            if (this.server != null) {
                this.server.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void closeServerSocket() {
        String[] ips = new String[this.ipMap.size()];
        this.ipMap.keySet().toArray(ips);
        for (String ip : ips) {
            this.ipMap.get(ip).closeClientSocket();
        }
    }

    private void sendByte(byte[] data, int len) {
        if (this.ipMap.size() != 0) {
            if (this.curClient == null) {
                Iterator<String> it2 = this.ipMap.keySet().iterator();
                if (it2.hasNext()) {
                    this.curClient = this.ipMap.get(it2.next());
                }
            }
            if (this.curClient != null) {
                this.curClient.sendByte(data, len);
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEvent3BackgroundThread(DataEvent event) {
        sendEvent();
    }

    public void updateClientInfo(String packageName, int port2) {
        String ipPort = String.format(Locale.US, "%s:%d", Utils.getIp(), Integer.valueOf(port2));
        if (this.ipMap.containsKey(ipPort)) {
            this.ipMap.get(ipPort).packageName = packageName;
        }
    }

    public boolean containApp(String packageName) {
        return getEventClientInfo(packageName) != null;
    }

    private class EventClientInfo extends Thread {

        /* renamed from: in  reason: collision with root package name */
        public InputStream f6in;
        public String ip;
        public OutputStream out;
        public String packageName;
        public Socket socket;

        public EventClientInfo(Socket socket2, InputStream in2, OutputStream out2) {
            setName("EventClientInfoThread");
            this.socket = socket2;
            this.f6in = in2;
            this.out = out2;
            if (socket2.getInetAddress() != null && socket2.getInetAddress().getAddress() != null && socket2.getInetAddress().getAddress().length == 4) {
                byte[] address = socket2.getInetAddress().getAddress();
                this.ip = String.format(Locale.US, "%d.%d.%d.%d:%d", Integer.valueOf(address[0] & 255), Integer.valueOf(address[1] & 255), Integer.valueOf(address[2] & 255), Integer.valueOf(address[3] & 255), Integer.valueOf(socket2.getPort()));
            }
        }

        public void run() {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            ProxyEventServer.this.sendEvent();
            DJIBaseCommData commData = new DJIBaseCommData();
            commData.setNumberWithTag(99, DJIBaseCommData.DJIBaseCommDataTag.DJIBaseCommData_Who.ordinal());
            commData.setNumberWithTag(0, DJIBaseCommData.DJIBaseCommDataTag.DJIBaseCommData_Event.ordinal());
            byte[] noUseData = commData.packedData();
            while (sendByte(noUseData, noUseData.length)) {
                try {
                    Thread.sleep(500);
                } catch (Exception e2) {
                    e2.printStackTrace();
                }
            }
            closeClientSocket();
        }

        /* access modifiers changed from: private */
        public void closeClientSocket() {
            try {
                if (this.out != null) {
                    this.out.close();
                    this.out = null;
                }
                if (this.f6in != null) {
                    this.f6in.close();
                    this.f6in = null;
                }
                if (this.socket != null) {
                    this.socket.close();
                    this.socket = null;
                }
                if (ProxyEventServer.this.ipMap.containsKey(this.ip)) {
                    ProxyEventServer.this.ipMap.remove(this.ip);
                    if (ProxyEventServer.this.curClient == this) {
                        EventClientInfo unused = ProxyEventServer.this.curClient = null;
                    }
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        public boolean sendByte(byte[] data, int len) {
            try {
                this.out.write(data, 0, len);
                this.out.flush();
                return true;
            } catch (Exception e) {
                e.printStackTrace();
                closeClientSocket();
                return false;
            }
        }
    }
}
