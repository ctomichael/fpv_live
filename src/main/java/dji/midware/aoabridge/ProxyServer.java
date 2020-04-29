package dji.midware.aoabridge;

import android.util.Log;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.MidWare;
import dji.midware.usb.P3.UsbAccessoryService;
import dji.midware.wsbridge.BridgeWSConnectionManager;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import org.greenrobot.eventbus.EventBus;

@EXClassNullAway
public class ProxyServer {
    /* access modifiers changed from: private */
    public ClientInfo curClient;
    /* access modifiers changed from: private */
    public Map<String, ClientInfo> ipMap;
    private int port = -1;
    private ServerSocket server;

    public enum Status {
        Connect,
        DisConnect
    }

    public int getClientCount() {
        return this.ipMap.size();
    }

    public List<String> getAllClientIp() {
        List<String> ips = new ArrayList<>();
        for (String key : this.ipMap.keySet()) {
            ips.add(key);
        }
        return ips;
    }

    public void switchClient(String packageName) {
        ClientInfo info = getClientInfo(packageName);
        if (info != null && info != this.curClient) {
            this.curClient = info;
        }
    }

    public void updateClientInfo(String packageName, int port2) {
        String ipPort = String.format(Locale.US, "%s:%d", Utils.getIp(), Integer.valueOf(port2));
        if (this.ipMap.containsKey(ipPort)) {
            this.ipMap.get(ipPort).packageName = packageName;
        }
    }

    public String getCurrentSelectedPackageName() {
        if (this.curClient != null) {
            return this.curClient.packageName;
        }
        return null;
    }

    public boolean containApp(String packageName) {
        return getClientInfo(packageName) != null;
    }

    public ClientInfo getClientInfo(String packageName) {
        if (packageName == null) {
            return null;
        }
        for (String ip : this.ipMap.keySet()) {
            ClientInfo info = this.ipMap.get(ip);
            if (packageName.equals(info.packageName)) {
                return info;
            }
        }
        return null;
    }

    public ProxyServer(int port2) {
        this.port = port2;
    }

    public void init() {
        new Thread("ProxyServerInit") {
            /* class dji.midware.aoabridge.ProxyServer.AnonymousClass1 */

            public void run() {
                ProxyServer.this.initSocket();
            }
        }.start();
    }

    public void uninit() {
        closeServer();
    }

    /* access modifiers changed from: private */
    public void initSocket() {
        ClientInfo info;
        try {
            this.ipMap = new HashMap();
            this.server = new ServerSocket(this.port);
            while (true) {
                Socket socket = this.server.accept();
                Log.d("AoaConnect", "one client connected " + socket.getPort());
                if (socket == null) {
                    break;
                }
                if (MidWare.isBridgeEnabled()) {
                    info = new ClientInfo(socket, BridgeWSConnectionManager.getInstance().getInputStream(), BridgeWSConnectionManager.getInstance().getOutputStream());
                } else {
                    info = new ClientInfo(socket, socket.getInputStream(), socket.getOutputStream());
                }
                this.ipMap.put(info.ip, info);
                info.start();
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.d("AoaConnect", "server exception happen: " + e.getMessage());
        }
        closeServer();
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

    public void sendByte(byte[] data, int len) {
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

    private class ClientInfo extends Thread {

        /* renamed from: in  reason: collision with root package name */
        public InputStream f3in;
        public String ip;
        public OutputStream out;
        public String packageName;
        public Socket socket;

        public ClientInfo(Socket socket2, InputStream in2, OutputStream out2) {
            setName("ClientInfoThread");
            this.socket = socket2;
            this.f3in = in2;
            this.out = out2;
            if (socket2.getInetAddress() != null && socket2.getInetAddress().getAddress() != null && socket2.getInetAddress().getAddress().length == 4) {
                byte[] address = socket2.getInetAddress().getAddress();
                this.ip = String.format(Locale.US, "%d.%d.%d.%d:%d", Integer.valueOf(address[0] & 255), Integer.valueOf(address[1] & 255), Integer.valueOf(address[2] & 255), Integer.valueOf(address[3] & 255), Integer.valueOf(socket2.getPort()));
            }
        }

        public void run() {
            if (this.f3in != null) {
                EventBus.getDefault().post(Status.Connect);
                try {
                    byte[] buffer = new byte[16384];
                    while (this.f3in != null) {
                        int len = this.f3in.read(buffer);
                        if (len > 0) {
                            byte[] data = new byte[len];
                            System.arraycopy(buffer, 0, data, 0, len);
                            if (ProxyServer.this.curClient == this) {
                                UsbAccessoryService.getInstance().sendmessage(data);
                            }
                        } else {
                            Thread.sleep(50);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            closeClientSocket();
        }

        /* access modifiers changed from: private */
        public void closeClientSocket() {
            Log.d("socketclose", "server close 2: " + System.currentTimeMillis());
            try {
                if (this.out != null) {
                    this.out.close();
                    this.out = null;
                }
                if (this.f3in != null) {
                    this.f3in.close();
                    this.f3in = null;
                }
                if (this.socket != null) {
                    this.socket.close();
                    this.socket = null;
                }
                EventBus.getDefault().post(Status.DisConnect);
                if (ProxyServer.this.ipMap.containsKey(this.ip)) {
                    ProxyServer.this.ipMap.remove(this.ip);
                    if (ProxyServer.this.curClient == this) {
                        ClientInfo unused = ProxyServer.this.curClient = null;
                    }
                }
                Log.d("AoaConnect", "server: one client disconnected : " + this.ip);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        public void sendByte(byte[] data, int len) {
            try {
                this.out.write(data, 0, len);
                this.out.flush();
            } catch (Exception e) {
                Log.d("socketclose", "server close : " + System.currentTimeMillis());
                e.printStackTrace();
                closeClientSocket();
            }
        }
    }
}
