package it.sauronsoftware.ftp4j.connectors;

import it.sauronsoftware.ftp4j.FTPConnector;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class SOCKS5Connector extends FTPConnector {
    private String socks5host;
    private String socks5pass;
    private int socks5port;
    private String socks5user;

    public SOCKS5Connector(String socks5host2, int socks5port2, String socks5user2, String socks5pass2) {
        this.socks5host = socks5host2;
        this.socks5port = socks5port2;
        this.socks5user = socks5user2;
        this.socks5pass = socks5pass2;
    }

    public SOCKS5Connector(String socks5host2, int socks5port2) {
        this(socks5host2, socks5port2, null, null);
    }

    /* JADX INFO: Can't fix incorrect switch cases order, some code will duplicate */
    private Socket socksConnect(String host, int port, boolean forDataTransfer) throws IOException {
        boolean authentication = (this.socks5user == null || this.socks5pass == null) ? false : true;
        Socket socket = null;
        InputStream in2 = null;
        OutputStream out = null;
        if (forDataTransfer) {
            try {
                socket = tcpConnectForDataTransferChannel(this.socks5host, this.socks5port);
            } catch (IOException e) {
                throw e;
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
            socket = tcpConnectForCommunicationChannel(this.socks5host, this.socks5port);
        }
        in2 = socket.getInputStream();
        out = socket.getOutputStream();
        out.write(5);
        if (authentication) {
            out.write(1);
            out.write(2);
        } else {
            out.write(1);
            out.write(0);
        }
        if (read(in2) != 5) {
            throw new IOException("SOCKS5Connector: invalid proxy response");
        }
        int aux = read(in2);
        if (authentication) {
            if (aux != 2) {
                throw new IOException("SOCKS5Connector: proxy doesn't support username/password authentication method");
            }
            byte[] user = this.socks5user.getBytes("UTF-8");
            byte[] pass = this.socks5pass.getBytes("UTF-8");
            int userLength = user.length;
            int passLength = pass.length;
            if (userLength > 255) {
                throw new IOException("SOCKS5Connector: username too long");
            } else if (passLength > 255) {
                throw new IOException("SOCKS5Connector: password too long");
            } else {
                out.write(1);
                out.write(userLength);
                out.write(user);
                out.write(passLength);
                out.write(pass);
                if (read(in2) != 1) {
                    throw new IOException("SOCKS5Connector: invalid proxy response");
                } else if (read(in2) != 0) {
                    throw new IOException("SOCKS5Connector: authentication failed");
                }
            }
        } else if (aux != 0) {
            throw new IOException("SOCKS5Connector: proxy requires authentication");
        }
        out.write(5);
        out.write(1);
        out.write(0);
        out.write(3);
        byte[] domain = host.getBytes("UTF-8");
        if (domain.length > 255) {
            throw new IOException("SOCKS5Connector: domain name too long");
        }
        out.write(domain.length);
        out.write(domain);
        out.write(port >> 8);
        out.write(port);
        if (read(in2) != 5) {
            throw new IOException("SOCKS5Connector: invalid proxy response");
        }
        switch (read(in2)) {
            case 0:
                in2.skip(1);
                int aux2 = read(in2);
                if (aux2 == 1) {
                    in2.skip(4);
                } else if (aux2 == 3) {
                    in2.skip((long) read(in2));
                } else if (aux2 == 4) {
                    in2.skip(16);
                } else {
                    throw new IOException("SOCKS5Connector: invalid proxy response");
                }
                in2.skip(2);
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
            case 1:
                throw new IOException("SOCKS5Connector: general failure");
            case 2:
                throw new IOException("SOCKS5Connector: connection not allowed by ruleset");
            case 3:
                throw new IOException("SOCKS5Connector: network unreachable");
            case 4:
                throw new IOException("SOCKS5Connector: host unreachable");
            case 5:
                throw new IOException("SOCKS5Connector: connection refused by destination host");
            case 6:
                throw new IOException("SOCKS5Connector: TTL expired");
            case 7:
                throw new IOException("SOCKS5Connector: command not supported / protocol error");
            case 8:
                throw new IOException("SOCKS5Connector: address type not supported");
            default:
                throw new IOException("SOCKS5Connector: invalid proxy response");
        }
        throw e;
    }

    private int read(InputStream in2) throws IOException {
        int aux = in2.read();
        if (aux >= 0) {
            return aux;
        }
        throw new IOException("SOCKS5Connector: connection closed by the proxy");
    }

    public Socket connectForCommunicationChannel(String host, int port) throws IOException {
        return socksConnect(host, port, false);
    }

    public Socket connectForDataTransferChannel(String host, int port) throws IOException {
        return socksConnect(host, port, true);
    }
}
