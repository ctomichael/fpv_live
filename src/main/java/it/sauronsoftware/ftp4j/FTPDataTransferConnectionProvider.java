package it.sauronsoftware.ftp4j;

import java.net.Socket;

interface FTPDataTransferConnectionProvider {
    void dispose();

    Socket openDataTransferConnection() throws FTPDataTransferException;
}
