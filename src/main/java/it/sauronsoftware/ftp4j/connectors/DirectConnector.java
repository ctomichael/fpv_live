package it.sauronsoftware.ftp4j.connectors;

import it.sauronsoftware.ftp4j.FTPConnector;
import java.io.IOException;
import java.net.Socket;

public class DirectConnector extends FTPConnector {
    public Socket connectForCommunicationChannel(String host, int port) throws IOException {
        return tcpConnectForCommunicationChannel(host, port);
    }

    public Socket connectForDataTransferChannel(String host, int port) throws IOException {
        return tcpConnectForDataTransferChannel(host, port);
    }
}
