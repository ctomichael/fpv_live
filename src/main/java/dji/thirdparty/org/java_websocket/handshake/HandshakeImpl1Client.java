package dji.thirdparty.org.java_websocket.handshake;

public class HandshakeImpl1Client extends HandshakedataImpl1 implements ClientHandshakeBuilder {
    private String resourcedescriptor;

    public void setResourceDescriptor(String resourcedescriptor2) throws IllegalArgumentException {
        this.resourcedescriptor = resourcedescriptor2;
    }

    public String getResourceDescriptor() {
        return this.resourcedescriptor;
    }
}
