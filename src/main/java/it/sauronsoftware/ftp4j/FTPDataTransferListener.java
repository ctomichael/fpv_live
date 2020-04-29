package it.sauronsoftware.ftp4j;

public interface FTPDataTransferListener {
    void aborted();

    void completed();

    void failed();

    void started();

    void transferred(int i);
}
