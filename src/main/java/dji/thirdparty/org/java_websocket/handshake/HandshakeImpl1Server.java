package dji.thirdparty.org.java_websocket.handshake;

public class HandshakeImpl1Server extends HandshakedataImpl1 implements ServerHandshakeBuilder {
    private short httpstatus;
    private String httpstatusmessage;

    public String getHttpStatusMessage() {
        return this.httpstatusmessage;
    }

    public short getHttpStatus() {
        return this.httpstatus;
    }

    public void setHttpStatusMessage(String message) {
        this.httpstatusmessage = message;
    }

    public void setHttpStatus(short status) {
        this.httpstatus = status;
    }
}
