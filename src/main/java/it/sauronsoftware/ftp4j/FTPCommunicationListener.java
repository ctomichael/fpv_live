package it.sauronsoftware.ftp4j;

public interface FTPCommunicationListener {
    void received(String str);

    void sent(String str);
}
