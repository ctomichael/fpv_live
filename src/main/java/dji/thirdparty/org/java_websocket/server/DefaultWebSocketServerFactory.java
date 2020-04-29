package dji.thirdparty.org.java_websocket.server;

import dji.thirdparty.org.java_websocket.WebSocketAdapter;
import dji.thirdparty.org.java_websocket.WebSocketImpl;
import dji.thirdparty.org.java_websocket.drafts.Draft;
import dji.thirdparty.org.java_websocket.server.WebSocketServer;
import java.net.Socket;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.util.List;

public class DefaultWebSocketServerFactory implements WebSocketServer.WebSocketServerFactory {
    public WebSocketImpl createWebSocket(WebSocketAdapter a, Draft d, Socket s) {
        return new WebSocketImpl(a, d);
    }

    public WebSocketImpl createWebSocket(WebSocketAdapter a, List<Draft> d, Socket s) {
        return new WebSocketImpl(a, d);
    }

    public SocketChannel wrapChannel(SocketChannel channel, SelectionKey key) {
        return channel;
    }
}
