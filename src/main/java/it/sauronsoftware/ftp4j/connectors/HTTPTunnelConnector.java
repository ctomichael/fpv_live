package it.sauronsoftware.ftp4j.connectors;

import it.sauronsoftware.ftp4j.FTPConnector;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;

public class HTTPTunnelConnector extends FTPConnector {
    private String proxyHost;
    private String proxyPass;
    private int proxyPort;
    private String proxyUser;

    public HTTPTunnelConnector(String proxyHost2, int proxyPort2, String proxyUser2, String proxyPass2) {
        this.proxyHost = proxyHost2;
        this.proxyPort = proxyPort2;
        this.proxyUser = proxyUser2;
        this.proxyPass = proxyPass2;
    }

    public HTTPTunnelConnector(String proxyHost2, int proxyPort2) {
        this(proxyHost2, proxyPort2, null, null);
    }

    private Socket httpConnect(String host, int port, boolean forDataTransfer) throws IOException {
        byte[] CRLF = "\r\n".getBytes("UTF-8");
        String connect = "CONNECT " + host + ":" + port + " HTTP/1.1";
        String hostHeader = "Host: " + host + ":" + port;
        Socket socket = null;
        InputStream in2 = null;
        OutputStream out = null;
        if (forDataTransfer) {
            try {
                socket = tcpConnectForDataTransferChannel(this.proxyHost, this.proxyPort);
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
            socket = tcpConnectForCommunicationChannel(this.proxyHost, this.proxyPort);
        }
        in2 = socket.getInputStream();
        out = socket.getOutputStream();
        out.write(connect.getBytes("UTF-8"));
        out.write(CRLF);
        out.write(hostHeader.getBytes("UTF-8"));
        out.write(CRLF);
        if (!(this.proxyUser == null || this.proxyPass == null)) {
            out.write(("Proxy-Authorization: Basic " + Base64.encode(this.proxyUser + ":" + this.proxyPass)).getBytes("UTF-8"));
            out.write(CRLF);
        }
        out.write(CRLF);
        out.flush();
        ArrayList responseLines = new ArrayList();
        BufferedReader reader = new BufferedReader(new InputStreamReader(in2));
        String line = reader.readLine();
        while (line != null && line.length() > 0) {
            responseLines.add(line);
            line = reader.readLine();
        }
        int size = responseLines.size();
        if (size < 1) {
            throw new IOException("HTTPTunnelConnector: invalid proxy response");
        }
        String response = (String) responseLines.get(0);
        if (!response.startsWith("HTTP/") || response.length() < 12) {
            throw new IOException("HTTPTunnelConnector: invalid proxy response");
        }
        if (!"200".equals(response.substring(9, 12))) {
            StringBuffer msg = new StringBuffer();
            msg.append("HTTPTunnelConnector: connection failed\r\n");
            msg.append("Response received from the proxy:\r\n");
            for (int i = 0; i < size; i++) {
                msg.append((String) responseLines.get(i));
                msg.append("\r\n");
            }
            throw new IOException(msg.toString());
        }
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
    }

    public Socket connectForCommunicationChannel(String host, int port) throws IOException {
        return httpConnect(host, port, false);
    }

    public Socket connectForDataTransferChannel(String host, int port) throws IOException {
        return httpConnect(host, port, true);
    }
}
