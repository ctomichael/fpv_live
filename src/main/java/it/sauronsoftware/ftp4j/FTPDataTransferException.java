package it.sauronsoftware.ftp4j;

public class FTPDataTransferException extends Exception {
    private static final long serialVersionUID = 1;

    public FTPDataTransferException() {
    }

    public FTPDataTransferException(String message, Throwable cause) {
        super(message, cause);
    }

    public FTPDataTransferException(String message) {
        super(message);
    }

    public FTPDataTransferException(Throwable cause) {
        super(cause);
    }
}
