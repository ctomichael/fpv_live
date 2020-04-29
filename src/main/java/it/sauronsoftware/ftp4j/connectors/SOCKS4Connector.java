package it.sauronsoftware.ftp4j.connectors;

import it.sauronsoftware.ftp4j.FTPConnector;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;

public class SOCKS4Connector extends FTPConnector {
    private String socks4host;
    private int socks4port;
    private String socks4user;

    public SOCKS4Connector(String socks4host2, int socks4port2, String socks4user2) {
        this.socks4host = socks4host2;
        this.socks4port = socks4port2;
        this.socks4user = socks4user2;
    }

    public SOCKS4Connector(String socks4host2, int socks4port2) {
        this(socks4host2, socks4port2, null);
    }

    /* JADX INFO: Can't fix incorrect switch cases order, some code will duplicate */
    private Socket socksConnect(String host, int port, boolean forDataTransfer) throws IOException {
        byte[] address;
        boolean socks4a = false;
        try {
            address = InetAddress.getByName(host).getAddress();
        } catch (Exception e) {
            socks4a = true;
            address = new byte[]{0, 0, 0, 1};
        }
        Socket socket = null;
        InputStream in2 = null;
        OutputStream out = null;
        if (forDataTransfer) {
            try {
                socket = tcpConnectForDataTransferChannel(this.socks4host, this.socks4port);
            } catch (IOException e2) {
                throw e2;
            } catch (Throwable th) {
                if (0 == 0) {
                    if (out != null) {
                        try {
                            out.close();
                        } catch (Throwable th2) {
                        }
                    }
                    if (in2 != null) {
                        in2.close();
                    }
                    if (socket != null) {
                        socket.close();
                    }
                }
                throw th;
            }
        } else {
            socket = tcpConnectForCommunicationChannel(this.socks4host, this.socks4port);
        }
        in2 = socket.getInputStream();
        out = socket.getOutputStream();
        out.write(4);
        out.write(1);
        out.write(port >> 8);
        out.write(port);
        out.write(address);
        if (this.socks4user != null) {
            out.write(this.socks4user.getBytes("UTF-8"));
        }
        out.write(0);
        if (socks4a) {
            out.write(host.getBytes("UTF-8"));
            out.write(0);
        }
        if (read(in2) != 0) {
            throw new IOException("SOCKS4Connector: invalid proxy response");
        }
        switch (read(in2)) {
            case 90:
                in2.skip(6);
                if (1 == 0) {
                    if (out != null) {
                        try {
                            out.close();
                        } catch (Throwable th3) {
                        }
                    }
                    if (in2 != null) {
                        try {
                            in2.close();
                        } catch (Throwable th4) {
                        }
                    }
                    if (socket != null) {
                        try {
                            socket.close();
                        } catch (Throwable th5) {
                        }
                    }
                }
                return socket;
            case 91:
                throw new IOException("SOCKS4Connector: connection refused/failed");
            case 92:
                throw new IOException("SOCKS4Connector: cannot validate the user");
            case 93:
                throw new IOException("SOCKS4Connector: invalid user");
            default:
                throw new IOException("SOCKS4Connector: invalid proxy response");
        }
        throw e2;
    }

    private int read(InputStream in2) throws IOException {
        int aux = in2.read();
        if (aux >= 0) {
            return aux;
        }
        throw new IOException("SOCKS4Connector: connection closed by the proxy");
    }

    public Socket connectForCommunicationChannel(String host, int port) throws IOException {
        return socksConnect(host, port, false);
    }

    public Socket connectForDataTransferChannel(String host, int port) throws IOException {
        return socksConnect(host, port, true);
    }
}
