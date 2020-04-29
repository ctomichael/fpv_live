package it.sauronsoftware.ftp4j;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Iterator;
import javax.net.ssl.SSLSocketFactory;

public class FTPCommunicationChannel {
    private String charsetName = null;
    private ArrayList communicationListeners = new ArrayList();
    private Socket connection = null;
    private NVTASCIIReader reader = null;
    private NVTASCIIWriter writer = null;

    public FTPCommunicationChannel(Socket connection2, String charsetName2) throws IOException {
        this.connection = connection2;
        this.charsetName = charsetName2;
        InputStream inStream = connection2.getInputStream();
        OutputStream outStream = connection2.getOutputStream();
        this.reader = new NVTASCIIReader(inStream, charsetName2);
        this.writer = new NVTASCIIWriter(outStream, charsetName2);
    }

    public void addCommunicationListener(FTPCommunicationListener listener) {
        this.communicationListeners.add(listener);
    }

    public void removeCommunicationListener(FTPCommunicationListener listener) {
        this.communicationListeners.remove(listener);
    }

    public void close() {
        try {
            this.connection.close();
        } catch (Exception e) {
        }
    }

    public FTPCommunicationListener[] getCommunicationListeners() {
        int size = this.communicationListeners.size();
        FTPCommunicationListener[] ret = new FTPCommunicationListener[size];
        for (int i = 0; i < size; i++) {
            ret[i] = (FTPCommunicationListener) this.communicationListeners.get(i);
        }
        return ret;
    }

    private String read() throws IOException {
        String line = this.reader.readLine();
        if (line == null) {
            throw new IOException("FTPConnection closed");
        }
        Iterator iter = this.communicationListeners.iterator();
        while (iter.hasNext()) {
            ((FTPCommunicationListener) iter.next()).received(line);
        }
        return line;
    }

    public void sendFTPCommand(String command) throws IOException {
        this.writer.writeLine(command);
        Iterator iter = this.communicationListeners.iterator();
        while (iter.hasNext()) {
            ((FTPCommunicationListener) iter.next()).sent(command);
        }
    }

    public FTPReply readFTPReply() throws IOException, FTPIllegalReplyException {
        int aux;
        int code = 0;
        ArrayList messages = new ArrayList();
        while (true) {
            String statement = read();
            if (statement.trim().length() != 0) {
                if (statement.startsWith("\n")) {
                    statement = statement.substring(1);
                }
                int l = statement.length();
                if (code != 0 || l >= 3) {
                    try {
                        aux = Integer.parseInt(statement.substring(0, 3));
                    } catch (Exception e) {
                        if (code == 0) {
                            throw new FTPIllegalReplyException();
                        }
                        aux = 0;
                    }
                    if (code == 0 || aux == 0 || aux == code) {
                        if (code == 0) {
                            code = aux;
                        }
                        if (aux > 0) {
                            if (l <= 3) {
                                if (l == 3) {
                                    break;
                                }
                                messages.add(statement);
                            } else {
                                char s = statement.charAt(3);
                                messages.add(statement.substring(4, l));
                                if (s == ' ') {
                                    break;
                                } else if (s != '-') {
                                    throw new FTPIllegalReplyException();
                                }
                            }
                        } else {
                            messages.add(statement);
                        }
                    } else {
                        throw new FTPIllegalReplyException();
                    }
                } else {
                    throw new FTPIllegalReplyException();
                }
            }
        }
        int size = messages.size();
        String[] m = new String[size];
        for (int i = 0; i < size; i++) {
            m[i] = (String) messages.get(i);
        }
        return new FTPReply(code, m);
    }

    public void changeCharset(String charsetName2) throws IOException {
        this.charsetName = charsetName2;
        this.reader.changeCharset(charsetName2);
        this.writer.changeCharset(charsetName2);
    }

    /* JADX DEBUG: Failed to find minimal casts for resolve overloaded methods, cast all args instead
     method: ClspMth{javax.net.ssl.SSLSocketFactory.createSocket(java.net.Socket, java.lang.String, int, boolean):java.net.Socket throws java.io.IOException}
     arg types: [java.net.Socket, java.lang.String, int, int]
     candidates:
      ClspMth{javax.net.SocketFactory.createSocket(java.lang.String, int, java.net.InetAddress, int):java.net.Socket throws java.io.IOException, java.net.UnknownHostException}
      ClspMth{javax.net.SocketFactory.createSocket(java.net.InetAddress, int, java.net.InetAddress, int):java.net.Socket throws java.io.IOException}
      ClspMth{javax.net.ssl.SSLSocketFactory.createSocket(java.net.Socket, java.lang.String, int, boolean):java.net.Socket throws java.io.IOException} */
    public void ssl(SSLSocketFactory sslSocketFactory) throws IOException {
        this.connection = sslSocketFactory.createSocket(this.connection, this.connection.getInetAddress().getHostName(), this.connection.getPort(), true);
        InputStream inStream = this.connection.getInputStream();
        OutputStream outStream = this.connection.getOutputStream();
        this.reader = new NVTASCIIReader(inStream, this.charsetName);
        this.writer = new NVTASCIIWriter(outStream, this.charsetName);
    }
}
