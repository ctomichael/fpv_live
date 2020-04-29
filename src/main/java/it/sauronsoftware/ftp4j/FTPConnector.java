package it.sauronsoftware.ftp4j;

import dji.pilot.fpv.util.DJIFlurryReport;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;

public abstract class FTPConnector {
    protected int closeTimeout;
    private Socket connectingCommunicationChannelSocket;
    protected int connectionTimeout;
    protected int readTimeout;
    private SocketProvider socketProvider;
    private boolean useSuggestedAddressForDataConnections;

    public abstract Socket connectForCommunicationChannel(String str, int i) throws IOException;

    public abstract Socket connectForDataTransferChannel(String str, int i) throws IOException;

    protected FTPConnector(boolean useSuggestedAddressForDataConnectionsDefValue) {
        this.connectionTimeout = 10;
        this.readTimeout = 10;
        this.closeTimeout = 10;
        String sysprop = System.getProperty(FTPKeys.PASSIVE_DT_USE_SUGGESTED_ADDRESS);
        if ("true".equalsIgnoreCase(sysprop) || DJIFlurryReport.GroundStation.V2_GS_YES_VAL.equalsIgnoreCase(sysprop) || "1".equals(sysprop)) {
            this.useSuggestedAddressForDataConnections = true;
        } else if ("false".equalsIgnoreCase(sysprop) || DJIFlurryReport.GroundStation.V2_GS_NO_VAL.equalsIgnoreCase(sysprop) || "0".equals(sysprop)) {
            this.useSuggestedAddressForDataConnections = false;
        } else {
            this.useSuggestedAddressForDataConnections = useSuggestedAddressForDataConnectionsDefValue;
        }
    }

    protected FTPConnector() {
        this(false);
    }

    public void setConnectionTimeout(int connectionTimeout2) {
        this.connectionTimeout = connectionTimeout2;
    }

    public void setReadTimeout(int readTimeout2) {
        this.readTimeout = readTimeout2;
    }

    public void setCloseTimeout(int closeTimeout2) {
        this.closeTimeout = closeTimeout2;
    }

    public void setUseSuggestedAddressForDataConnections(boolean value) {
        this.useSuggestedAddressForDataConnections = value;
    }

    /* access modifiers changed from: package-private */
    public boolean getUseSuggestedAddressForDataConnections() {
        return this.useSuggestedAddressForDataConnections;
    }

    /* access modifiers changed from: protected */
    public Socket tcpConnectForCommunicationChannel(String host, int port) throws IOException {
        Socket socket = null;
        try {
            if (this.socketProvider != null) {
                socket = this.socketProvider.getSocket();
            } else {
                this.connectingCommunicationChannelSocket = new Socket();
            }
            this.connectingCommunicationChannelSocket.setKeepAlive(true);
            this.connectingCommunicationChannelSocket.setSoTimeout(this.readTimeout * 1000);
            this.connectingCommunicationChannelSocket.setSoLinger(true, this.closeTimeout);
            this.connectingCommunicationChannelSocket.connect(new InetSocketAddress(host, port), this.connectionTimeout * 1000);
            Socket socket2 = this.connectingCommunicationChannelSocket;
            this.connectingCommunicationChannelSocket = socket;
            return socket2;
        } finally {
            this.connectingCommunicationChannelSocket = socket;
        }
    }

    /* access modifiers changed from: protected */
    public Socket tcpConnectForDataTransferChannel(String host, int port) throws IOException {
        Socket socket;
        if (this.socketProvider != null) {
            socket = this.socketProvider.getSocket();
        } else {
            socket = new Socket();
        }
        socket.setSoTimeout(this.readTimeout * 1000);
        socket.setSoLinger(true, this.closeTimeout);
        socket.setReceiveBufferSize(524288);
        socket.setSendBufferSize(524288);
        socket.connect(new InetSocketAddress(host, port), this.connectionTimeout * 1000);
        return socket;
    }

    public void abortConnectForCommunicationChannel() {
        if (this.connectingCommunicationChannelSocket != null) {
            try {
                this.connectingCommunicationChannelSocket.close();
            } catch (Throwable th) {
            }
        }
    }

    public void setSocketProvider(SocketProvider provider) {
        this.socketProvider = provider;
    }
}
