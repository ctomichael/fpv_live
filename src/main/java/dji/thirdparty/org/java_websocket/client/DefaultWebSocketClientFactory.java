package dji.thirdparty.org.java_websocket.client;

import dji.thirdparty.org.java_websocket.WebSocket;
import dji.thirdparty.org.java_websocket.WebSocketAdapter;
import dji.thirdparty.org.java_websocket.WebSocketImpl;
import dji.thirdparty.org.java_websocket.client.WebSocketClient;
import dji.thirdparty.org.java_websocket.drafts.Draft;
import java.net.Socket;
import java.nio.channels.ByteChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.util.List;

public class DefaultWebSocketClientFactory implements WebSocketClient.WebSocketClientFactory {
    private final WebSocketClient webSocketClient;

    public DefaultWebSocketClientFactory(WebSocketClient webSocketClient2) {
        this.webSocketClient = webSocketClient2;
    }

    public WebSocket createWebSocket(WebSocketAdapter a, Draft d, Socket s) {
        return new WebSocketImpl(this.webSocketClient, d);
    }

    public WebSocket createWebSocket(WebSocketAdapter a, List<Draft> d, Socket s) {
        return new WebSocketImpl(this.webSocketClient, d);
    }

    public ByteChannel wrapChannel(SocketChannel channel, SelectionKey c, String host, int port) {
        if (c == null) {
        }
        return channel;
    }
}
