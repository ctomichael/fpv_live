package dji.thirdparty.org.java_websocket.client;

import dji.component.accountcenter.IMemberProtocol;
import dji.thirdparty.org.java_websocket.SocketChannelIOHelper;
import dji.thirdparty.org.java_websocket.WebSocket;
import dji.thirdparty.org.java_websocket.WebSocketAdapter;
import dji.thirdparty.org.java_websocket.WebSocketFactory;
import dji.thirdparty.org.java_websocket.WebSocketImpl;
import dji.thirdparty.org.java_websocket.WrappedByteChannel;
import dji.thirdparty.org.java_websocket.drafts.Draft;
import dji.thirdparty.org.java_websocket.drafts.Draft_10;
import dji.thirdparty.org.java_websocket.exceptions.InvalidHandshakeException;
import dji.thirdparty.org.java_websocket.handshake.HandshakeImpl1Client;
import dji.thirdparty.org.java_websocket.handshake.Handshakedata;
import dji.thirdparty.org.java_websocket.handshake.ServerHandshake;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.URI;
import java.nio.ByteBuffer;
import java.nio.channels.ByteChannel;
import java.nio.channels.CancelledKeyException;
import java.nio.channels.ClosedByInterruptException;
import java.nio.channels.NotYetConnectedException;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.nio.channels.spi.SelectorProvider;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

public abstract class WebSocketClient extends WebSocketAdapter implements Runnable {
    static final /* synthetic */ boolean $assertionsDisabled = (!WebSocketClient.class.desiredAssertionStatus());
    private SocketChannel channel;
    private CountDownLatch closeLatch;
    /* access modifiers changed from: private */
    public WebSocketImpl conn;
    private CountDownLatch connectLatch;
    private Draft draft;
    private Map<String, String> headers;
    private InetSocketAddress proxyAddress;
    private Thread readthread;
    private int timeout;
    protected URI uri;
    /* access modifiers changed from: private */
    public ByteChannel wrappedchannel;
    private Thread writethread;
    private WebSocketClientFactory wsfactory;

    public interface WebSocketClientFactory extends WebSocketFactory {
        ByteChannel wrapChannel(SocketChannel socketChannel, SelectionKey selectionKey, String str, int i) throws IOException;
    }

    public abstract void onClose(int i, String str, boolean z);

    public abstract void onError(Exception exc);

    public abstract void onMessage(String str);

    public abstract void onOpen(ServerHandshake serverHandshake);

    public WebSocketClient(URI serverURI) {
        this(serverURI, new Draft_10());
    }

    public WebSocketClient(URI serverUri, Draft draft2) {
        this(serverUri, draft2, null, 0);
    }

    public WebSocketClient(URI serverUri, Draft draft2, Map<String, String> headers2, int connecttimeout) {
        this.uri = null;
        this.conn = null;
        this.channel = null;
        this.wrappedchannel = null;
        this.connectLatch = new CountDownLatch(1);
        this.closeLatch = new CountDownLatch(1);
        this.timeout = 0;
        this.wsfactory = new DefaultWebSocketClientFactory(this);
        this.proxyAddress = null;
        if (serverUri == null) {
            throw new IllegalArgumentException();
        } else if (draft2 == null) {
            throw new IllegalArgumentException("null as draft is permitted for `WebSocketServer` only!");
        } else {
            this.uri = serverUri;
            this.draft = draft2;
            this.headers = headers2;
            this.timeout = connecttimeout;
            try {
                this.channel = SelectorProvider.provider().openSocketChannel();
                this.channel.configureBlocking(true);
            } catch (IOException e) {
                this.channel = null;
                onWebsocketError(null, e);
            }
            if (this.channel == null) {
                this.conn = (WebSocketImpl) this.wsfactory.createWebSocket(this, draft2, (Socket) null);
                this.conn.close(-1, "Failed to create or configure SocketChannel.");
                return;
            }
            this.conn = (WebSocketImpl) this.wsfactory.createWebSocket(this, draft2, this.channel.socket());
        }
    }

    public URI getURI() {
        return this.uri;
    }

    public Draft getDraft() {
        return this.draft;
    }

    public void connect() {
        if (this.writethread != null) {
            throw new IllegalStateException("WebSocketClient objects are not reuseable");
        }
        this.writethread = new Thread(this);
        this.writethread.start();
    }

    public boolean connectBlocking() throws InterruptedException {
        connect();
        this.connectLatch.await();
        return this.conn.isOpen();
    }

    public void close() {
        if (this.writethread != null) {
            this.conn.close(1000);
        }
    }

    public void closeBlocking() throws InterruptedException {
        close();
        this.closeLatch.await();
    }

    public void send(String text) throws NotYetConnectedException {
        this.conn.send(text);
    }

    public void send(byte[] data) throws NotYetConnectedException {
        this.conn.send(data);
    }

    public void run() {
        if (this.writethread == null) {
            this.writethread = Thread.currentThread();
        }
        interruptableRun();
        if (!$assertionsDisabled && this.channel.isOpen()) {
            throw new AssertionError();
        }
    }

    private final void interruptableRun() {
        String host;
        int port;
        if (this.channel != null) {
            try {
                if (this.proxyAddress != null) {
                    host = this.proxyAddress.getHostName();
                    port = this.proxyAddress.getPort();
                } else {
                    host = this.uri.getHost();
                    port = getPort();
                }
                this.channel.connect(new InetSocketAddress(host, port));
                WebSocketImpl webSocketImpl = this.conn;
                ByteChannel createProxyChannel = createProxyChannel(this.wsfactory.wrapChannel(this.channel, null, host, port));
                this.wrappedchannel = createProxyChannel;
                webSocketImpl.channel = createProxyChannel;
                this.timeout = 0;
                sendHandshake();
                this.readthread = new Thread(new WebsocketWriteThread());
                this.readthread.start();
                ByteBuffer buff = ByteBuffer.allocate(WebSocketImpl.RCVBUF);
                while (this.channel.isOpen()) {
                    try {
                        if (SocketChannelIOHelper.read(buff, this.conn, this.wrappedchannel)) {
                            this.conn.decode(buff);
                        } else {
                            this.conn.eot();
                        }
                        if (this.wrappedchannel instanceof WrappedByteChannel) {
                            WrappedByteChannel w = (WrappedByteChannel) this.wrappedchannel;
                            if (w.isNeedRead()) {
                                while (SocketChannelIOHelper.readMore(buff, this.conn, w)) {
                                    this.conn.decode(buff);
                                }
                                this.conn.decode(buff);
                            }
                        }
                    } catch (CancelledKeyException e) {
                        this.conn.eot();
                        return;
                    } catch (IOException e2) {
                        this.conn.eot();
                        return;
                    } catch (RuntimeException e3) {
                        onError(e3);
                        this.conn.closeConnection(1006, e3.getMessage());
                        return;
                    }
                }
            } catch (ClosedByInterruptException e4) {
                onWebsocketError(null, e4);
            } catch (Exception e5) {
                onWebsocketError(this.conn, e5);
                this.conn.closeConnection(-1, e5.getMessage());
            }
        }
    }

    /* access modifiers changed from: private */
    public int getPort() {
        int port = this.uri.getPort();
        if (port != -1) {
            return port;
        }
        String scheme = this.uri.getScheme();
        if (scheme.equals("wss")) {
            return WebSocket.DEFAULT_WSS_PORT;
        }
        if (scheme.equals("ws")) {
            return 80;
        }
        throw new RuntimeException("unkonow scheme" + scheme);
    }

    private void sendHandshake() throws InvalidHandshakeException {
        String path;
        String part1 = this.uri.getPath();
        String part2 = this.uri.getQuery();
        if (part1 == null || part1.length() == 0) {
            path = IMemberProtocol.PARAM_SEPERATOR;
        } else {
            path = part1;
        }
        if (part2 != null) {
            path = path + "?" + part2;
        }
        int port = getPort();
        HandshakeImpl1Client handshake = new HandshakeImpl1Client();
        handshake.setResourceDescriptor(path);
        handshake.put("Host", this.uri.getHost() + (port != 80 ? ":" + port : ""));
        if (this.headers != null) {
            for (Map.Entry<String, String> kv : this.headers.entrySet()) {
                handshake.put((String) kv.getKey(), (String) kv.getValue());
            }
        }
        this.conn.startHandshake(handshake);
    }

    public WebSocket.READYSTATE getReadyState() {
        return this.conn.getReadyState();
    }

    public final void onWebsocketMessage(WebSocket conn2, String message) {
        onMessage(message);
    }

    public final void onWebsocketMessage(WebSocket conn2, ByteBuffer blob) {
        onMessage(blob);
    }

    public final void onWebsocketOpen(WebSocket conn2, Handshakedata handshake) {
        this.connectLatch.countDown();
        onOpen((ServerHandshake) handshake);
    }

    public final void onWebsocketClose(WebSocket conn2, int code, String reason, boolean remote) {
        this.connectLatch.countDown();
        this.closeLatch.countDown();
        if (this.readthread != null) {
            this.readthread.interrupt();
        }
        onClose(code, reason, remote);
    }

    public final void onWebsocketError(WebSocket conn2, Exception ex) {
        onError(ex);
    }

    public final void onWriteDemand(WebSocket conn2) {
    }

    public void onWebsocketCloseInitiated(WebSocket conn2, int code, String reason) {
        onCloseInitiated(code, reason);
    }

    public void onWebsocketClosing(WebSocket conn2, int code, String reason, boolean remote) {
        onClosing(code, reason, remote);
    }

    public void onCloseInitiated(int code, String reason) {
    }

    public void onClosing(int code, String reason, boolean remote) {
    }

    public WebSocket getConnection() {
        return this.conn;
    }

    public final void setWebSocketFactory(WebSocketClientFactory wsf) {
        this.wsfactory = wsf;
    }

    public final WebSocketFactory getWebSocketFactory() {
        return this.wsfactory;
    }

    public InetSocketAddress getLocalSocketAddress(WebSocket conn2) {
        if (this.channel != null) {
            return (InetSocketAddress) this.channel.socket().getLocalSocketAddress();
        }
        return null;
    }

    public InetSocketAddress getRemoteSocketAddress(WebSocket conn2) {
        if (this.channel != null) {
            return (InetSocketAddress) this.channel.socket().getLocalSocketAddress();
        }
        return null;
    }

    public void onMessage(ByteBuffer bytes) {
    }

    public class DefaultClientProxyChannel extends AbstractClientProxyChannel {
        public DefaultClientProxyChannel(ByteChannel towrap) {
            super(towrap);
        }

        public String buildHandShake() {
            StringBuilder b = new StringBuilder();
            String host = WebSocketClient.this.uri.getHost();
            b.append("CONNECT ");
            b.append(host);
            b.append(":");
            b.append(WebSocketClient.this.getPort());
            b.append(" HTTP/1.1\n");
            b.append("Host: ");
            b.append(host);
            b.append("\n");
            return b.toString();
        }
    }

    private class WebsocketWriteThread implements Runnable {
        private WebsocketWriteThread() {
        }

        public void run() {
            Thread.currentThread().setName("WebsocketWriteThread");
            while (!Thread.interrupted()) {
                try {
                    SocketChannelIOHelper.writeBlocking(WebSocketClient.this.conn, WebSocketClient.this.wrappedchannel);
                } catch (IOException e) {
                    WebSocketClient.this.conn.eot();
                    return;
                } catch (InterruptedException e2) {
                    return;
                }
            }
        }
    }

    public ByteChannel createProxyChannel(ByteChannel towrap) {
        if (this.proxyAddress != null) {
            return new DefaultClientProxyChannel(towrap);
        }
        return towrap;
    }

    public void setProxy(InetSocketAddress proxyaddress) {
        this.proxyAddress = proxyaddress;
    }
}
