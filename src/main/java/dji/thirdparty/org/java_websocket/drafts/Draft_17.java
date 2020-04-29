package dji.thirdparty.org.java_websocket.drafts;

import dji.thirdparty.org.java_websocket.drafts.Draft;
import dji.thirdparty.org.java_websocket.exceptions.InvalidHandshakeException;
import dji.thirdparty.org.java_websocket.handshake.ClientHandshake;
import dji.thirdparty.org.java_websocket.handshake.ClientHandshakeBuilder;

public class Draft_17 extends Draft_10 {
    public Draft.HandshakeState acceptHandshakeAsServer(ClientHandshake handshakedata) throws InvalidHandshakeException {
        if (readVersion(handshakedata) == 13) {
            return Draft.HandshakeState.MATCHED;
        }
        return Draft.HandshakeState.NOT_MATCHED;
    }

    public ClientHandshakeBuilder postProcessHandshakeRequestAsClient(ClientHandshakeBuilder request) {
        super.postProcessHandshakeRequestAsClient(request);
        request.put("Sec-WebSocket-Version", "13");
        return request;
    }

    public Draft copyInstance() {
        return new Draft_17();
    }
}
