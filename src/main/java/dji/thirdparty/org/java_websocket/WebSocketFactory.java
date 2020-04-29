package dji.thirdparty.org.java_websocket;

import dji.thirdparty.org.java_websocket.drafts.Draft;
import java.net.Socket;
import java.util.List;

public interface WebSocketFactory {
    WebSocket createWebSocket(WebSocketAdapter webSocketAdapter, Draft draft, Socket socket);

    WebSocket createWebSocket(WebSocketAdapter webSocketAdapter, List<Draft> list, Socket socket);
}
