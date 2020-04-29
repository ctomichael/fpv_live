package dji.thirdparty.org.java_websocket;

import dji.thirdparty.org.java_websocket.WebSocket;
import dji.thirdparty.org.java_websocket.drafts.Draft;
import dji.thirdparty.org.java_websocket.drafts.Draft_10;
import dji.thirdparty.org.java_websocket.drafts.Draft_17;
import dji.thirdparty.org.java_websocket.drafts.Draft_75;
import dji.thirdparty.org.java_websocket.drafts.Draft_76;
import dji.thirdparty.org.java_websocket.exceptions.IncompleteHandshakeException;
import dji.thirdparty.org.java_websocket.exceptions.InvalidDataException;
import dji.thirdparty.org.java_websocket.exceptions.InvalidHandshakeException;
import dji.thirdparty.org.java_websocket.exceptions.WebsocketNotConnectedException;
import dji.thirdparty.org.java_websocket.framing.CloseFrame;
import dji.thirdparty.org.java_websocket.framing.CloseFrameBuilder;
import dji.thirdparty.org.java_websocket.framing.Framedata;
import dji.thirdparty.org.java_websocket.handshake.ClientHandshake;
import dji.thirdparty.org.java_websocket.handshake.ClientHandshakeBuilder;
import dji.thirdparty.org.java_websocket.handshake.Handshakedata;
import dji.thirdparty.org.java_websocket.handshake.ServerHandshake;
import dji.thirdparty.org.java_websocket.server.WebSocketServer;
import dji.thirdparty.org.java_websocket.util.Charsetfunctions;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.ByteChannel;
import java.nio.channels.SelectionKey;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class WebSocketImpl implements WebSocket {
    static final /* synthetic */ boolean $assertionsDisabled = (!WebSocketImpl.class.desiredAssertionStatus());
    public static boolean DEBUG = false;
    public static int RCVBUF = 16384;
    public static final List<Draft> defaultdraftlist = new ArrayList(4);
    public ByteChannel channel;
    private Integer closecode;
    private Boolean closedremotely;
    private String closemessage;
    private Framedata.Opcode current_continuous_frame_opcode;
    private Draft draft;
    private volatile boolean flushandclosestate;
    private ClientHandshake handshakerequest;
    public final BlockingQueue<ByteBuffer> inQueue;
    public SelectionKey key;
    private List<Draft> knownDrafts;
    public final BlockingQueue<ByteBuffer> outQueue;
    private WebSocket.READYSTATE readystate;
    private WebSocket.Role role;
    private ByteBuffer tmpHandshakeBytes;
    public volatile WebSocketServer.WebSocketWorker workerThread;
    private final WebSocketListener wsl;

    static {
        defaultdraftlist.add(new Draft_17());
        defaultdraftlist.add(new Draft_10());
        defaultdraftlist.add(new Draft_76());
        defaultdraftlist.add(new Draft_75());
    }

    public WebSocketImpl(WebSocketListener listener, List<Draft> drafts) {
        this(listener, (Draft) null);
        this.role = WebSocket.Role.SERVER;
        if (drafts == null || drafts.isEmpty()) {
            this.knownDrafts = defaultdraftlist;
        } else {
            this.knownDrafts = drafts;
        }
    }

    public WebSocketImpl(WebSocketListener listener, Draft draft2) {
        this.flushandclosestate = false;
        this.readystate = WebSocket.READYSTATE.NOT_YET_CONNECTED;
        this.draft = null;
        this.current_continuous_frame_opcode = null;
        this.handshakerequest = null;
        this.closemessage = null;
        this.closecode = null;
        this.closedremotely = null;
        if (listener == null || (draft2 == null && this.role == WebSocket.Role.SERVER)) {
            throw new IllegalArgumentException("parameters must not be null");
        }
        this.outQueue = new LinkedBlockingQueue();
        this.inQueue = new LinkedBlockingQueue();
        this.wsl = listener;
        this.role = WebSocket.Role.CLIENT;
        if (draft2 != null) {
            this.draft = draft2.copyInstance();
        }
    }

    @Deprecated
    public WebSocketImpl(WebSocketListener listener, Draft draft2, Socket socket) {
        this(listener, draft2);
    }

    @Deprecated
    public WebSocketImpl(WebSocketListener listener, List<Draft> drafts, Socket socket) {
        this(listener, drafts);
    }

    public void decode(ByteBuffer socketBuffer) {
        if (socketBuffer.hasRemaining() && !this.flushandclosestate) {
            if (DEBUG) {
                System.out.println("process(" + socketBuffer.remaining() + "): {" + (socketBuffer.remaining() > 1000 ? "too big to display" : new String(socketBuffer.array(), socketBuffer.position(), socketBuffer.remaining())) + "}");
            }
            if (this.readystate == WebSocket.READYSTATE.OPEN) {
                decodeFrames(socketBuffer);
            } else if (decodeHandshake(socketBuffer)) {
                decodeFrames(socketBuffer);
            }
            if (!$assertionsDisabled && !isClosing() && !isFlushAndClose() && socketBuffer.hasRemaining()) {
                throw new AssertionError();
            }
        }
    }

    private boolean decodeHandshake(ByteBuffer socketBufferNew) {
        ByteBuffer socketBuffer;
        if (this.tmpHandshakeBytes == null) {
            socketBuffer = socketBufferNew;
        } else {
            if (this.tmpHandshakeBytes.remaining() < socketBufferNew.remaining()) {
                ByteBuffer buf = ByteBuffer.allocate(this.tmpHandshakeBytes.capacity() + socketBufferNew.remaining());
                this.tmpHandshakeBytes.flip();
                buf.put(this.tmpHandshakeBytes);
                this.tmpHandshakeBytes = buf;
            }
            this.tmpHandshakeBytes.put(socketBufferNew);
            this.tmpHandshakeBytes.flip();
            socketBuffer = this.tmpHandshakeBytes;
        }
        socketBuffer.mark();
        try {
            if (this.draft == null && isFlashEdgeCase(socketBuffer) == Draft.HandshakeState.MATCHED) {
                write(ByteBuffer.wrap(Charsetfunctions.utf8Bytes(this.wsl.getFlashPolicy(this))));
                close(-3, "");
                return false;
            }
            try {
                if (this.role != WebSocket.Role.SERVER) {
                    if (this.role == WebSocket.Role.CLIENT) {
                        this.draft.setParseMode(this.role);
                        Handshakedata tmphandshake = this.draft.translateHandshake(socketBuffer);
                        if (!(tmphandshake instanceof ServerHandshake)) {
                            flushAndClose(1002, "Wwrong http function", false);
                            return false;
                        }
                        ServerHandshake handshake = (ServerHandshake) tmphandshake;
                        if (this.draft.acceptHandshakeAsClient(this.handshakerequest, handshake) == Draft.HandshakeState.MATCHED) {
                            try {
                                this.wsl.onWebsocketHandshakeReceivedAsClient(this, this.handshakerequest, handshake);
                                open(handshake);
                                return true;
                            } catch (InvalidDataException e) {
                                flushAndClose(e.getCloseCode(), e.getMessage(), false);
                                return false;
                            } catch (RuntimeException e2) {
                                this.wsl.onWebsocketError(this, e2);
                                flushAndClose(-1, e2.getMessage(), false);
                                return false;
                            }
                        } else {
                            close(1002, "draft " + this.draft + " refuses handshake");
                        }
                    }
                    return false;
                } else if (this.draft == null) {
                    for (Draft d : this.knownDrafts) {
                        Draft d2 = d.copyInstance();
                        try {
                            d2.setParseMode(this.role);
                            socketBuffer.reset();
                            Handshakedata tmphandshake2 = d2.translateHandshake(socketBuffer);
                            if (!(tmphandshake2 instanceof ClientHandshake)) {
                                flushAndClose(1002, "wrong http function", false);
                                return false;
                            }
                            ClientHandshake handshake2 = (ClientHandshake) tmphandshake2;
                            if (d2.acceptHandshakeAsServer(handshake2) == Draft.HandshakeState.MATCHED) {
                                try {
                                    write(d2.createHandshake(d2.postProcessHandshakeResponseAsServer(handshake2, this.wsl.onWebsocketHandshakeReceivedAsServer(this, d2, handshake2)), this.role));
                                    this.draft = d2;
                                    open(handshake2);
                                    return true;
                                } catch (InvalidDataException e3) {
                                    flushAndClose(e3.getCloseCode(), e3.getMessage(), false);
                                    return false;
                                } catch (RuntimeException e4) {
                                    this.wsl.onWebsocketError(this, e4);
                                    flushAndClose(-1, e4.getMessage(), false);
                                    return false;
                                }
                            } else {
                                continue;
                            }
                        } catch (InvalidHandshakeException e5) {
                        }
                    }
                    if (this.draft == null) {
                        close(1002, "no draft matches");
                    }
                    return false;
                } else {
                    Handshakedata tmphandshake3 = this.draft.translateHandshake(socketBuffer);
                    if (!(tmphandshake3 instanceof ClientHandshake)) {
                        flushAndClose(1002, "wrong http function", false);
                        return false;
                    }
                    ClientHandshake handshake3 = (ClientHandshake) tmphandshake3;
                    if (this.draft.acceptHandshakeAsServer(handshake3) == Draft.HandshakeState.MATCHED) {
                        open(handshake3);
                        return true;
                    }
                    close(1002, "the handshake did finaly not match");
                    return false;
                }
            } catch (InvalidHandshakeException e6) {
                close(e6);
            }
        } catch (IncompleteHandshakeException e7) {
            if (this.tmpHandshakeBytes == null) {
                socketBuffer.reset();
                int newsize = e7.getPreferedSize();
                if (newsize == 0) {
                    newsize = socketBuffer.capacity() + 16;
                } else if (!$assertionsDisabled && e7.getPreferedSize() < socketBuffer.remaining()) {
                    throw new AssertionError();
                }
                this.tmpHandshakeBytes = ByteBuffer.allocate(newsize);
                this.tmpHandshakeBytes.put(socketBufferNew);
            } else {
                this.tmpHandshakeBytes.position(this.tmpHandshakeBytes.limit());
                this.tmpHandshakeBytes.limit(this.tmpHandshakeBytes.capacity());
            }
        }
    }

    private void decodeFrames(ByteBuffer socketBuffer) {
        if (!this.flushandclosestate) {
            try {
                for (Framedata f : this.draft.translateFrame(socketBuffer)) {
                    if (DEBUG) {
                        System.out.println("matched frame: " + f);
                    }
                    if (!this.flushandclosestate) {
                        Framedata.Opcode curop = f.getOpcode();
                        boolean fin = f.isFin();
                        if (curop == Framedata.Opcode.CLOSING) {
                            int code = 1005;
                            String reason = "";
                            if (f instanceof CloseFrame) {
                                CloseFrame cf = (CloseFrame) f;
                                code = cf.getCloseCode();
                                reason = cf.getMessage();
                            }
                            if (this.readystate == WebSocket.READYSTATE.CLOSING) {
                                closeConnection(code, reason, true);
                            } else if (this.draft.getCloseHandshakeType() == Draft.CloseHandshakeType.TWOWAY) {
                                close(code, reason, true);
                            } else {
                                flushAndClose(code, reason, false);
                            }
                        } else if (curop == Framedata.Opcode.PING) {
                            this.wsl.onWebsocketPing(this, f);
                        } else if (curop == Framedata.Opcode.PONG) {
                            this.wsl.onWebsocketPong(this, f);
                        } else if (!fin || curop == Framedata.Opcode.CONTINUOUS) {
                            if (curop != Framedata.Opcode.CONTINUOUS) {
                                if (this.current_continuous_frame_opcode != null) {
                                    throw new InvalidDataException(1002, "Previous continuous frame sequence not completed.");
                                }
                                this.current_continuous_frame_opcode = curop;
                            } else if (fin) {
                                if (this.current_continuous_frame_opcode == null) {
                                    throw new InvalidDataException(1002, "Continuous frame sequence was not started.");
                                }
                                this.current_continuous_frame_opcode = null;
                            } else if (this.current_continuous_frame_opcode == null) {
                                throw new InvalidDataException(1002, "Continuous frame sequence was not started.");
                            }
                            try {
                                this.wsl.onWebsocketMessageFragment(this, f);
                            } catch (RuntimeException e) {
                                this.wsl.onWebsocketError(this, e);
                            }
                        } else if (this.current_continuous_frame_opcode != null) {
                            throw new InvalidDataException(1002, "Continuous frame sequence not completed.");
                        } else if (curop == Framedata.Opcode.TEXT) {
                            try {
                                this.wsl.onWebsocketMessage(this, Charsetfunctions.stringUtf8(f.getPayloadData()));
                            } catch (RuntimeException e2) {
                                this.wsl.onWebsocketError(this, e2);
                            }
                        } else if (curop == Framedata.Opcode.BINARY) {
                            try {
                                this.wsl.onWebsocketMessage(this, f.getPayloadData());
                            } catch (RuntimeException e3) {
                                this.wsl.onWebsocketError(this, e3);
                            }
                        } else {
                            throw new InvalidDataException(1002, "non control or continious frame expected");
                        }
                    } else {
                        return;
                    }
                }
            } catch (InvalidDataException e1) {
                this.wsl.onWebsocketError(this, e1);
                close(e1);
            }
        }
    }

    private void close(int code, String message, boolean remote) {
        if (this.readystate != WebSocket.READYSTATE.CLOSING && this.readystate != WebSocket.READYSTATE.CLOSED) {
            if (this.readystate == WebSocket.READYSTATE.OPEN) {
                if (code != 1006) {
                    if (this.draft.getCloseHandshakeType() != Draft.CloseHandshakeType.NONE) {
                        if (!remote) {
                            try {
                                this.wsl.onWebsocketCloseInitiated(this, code, message);
                            } catch (RuntimeException e) {
                                this.wsl.onWebsocketError(this, e);
                            }
                        }
                        try {
                            sendFrame(new CloseFrameBuilder(code, message));
                        } catch (InvalidDataException e2) {
                            this.wsl.onWebsocketError(this, e2);
                            flushAndClose(1006, "generated frame is invalid", false);
                        }
                    }
                    flushAndClose(code, message, remote);
                } else if ($assertionsDisabled || !remote) {
                    this.readystate = WebSocket.READYSTATE.CLOSING;
                    flushAndClose(code, message, false);
                    return;
                } else {
                    throw new AssertionError();
                }
            } else if (code != -3) {
                flushAndClose(-1, message, false);
            } else if ($assertionsDisabled || remote) {
                flushAndClose(-3, message, true);
            } else {
                throw new AssertionError();
            }
            if (code == 1002) {
                flushAndClose(code, message, remote);
            }
            this.readystate = WebSocket.READYSTATE.CLOSING;
            this.tmpHandshakeBytes = null;
        }
    }

    public void close(int code, String message) {
        close(code, message, false);
    }

    /* access modifiers changed from: protected */
    /* JADX WARNING: Code restructure failed: missing block: B:27:0x0040, code lost:
        r0 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:28:0x0041, code lost:
        r3.wsl.onWebsocketError(r3, r0);
     */
    /* JADX WARNING: Exception block dominator not found, dom blocks: [] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public synchronized void closeConnection(int r4, java.lang.String r5, boolean r6) {
        /*
            r3 = this;
            monitor-enter(r3)
            dji.thirdparty.org.java_websocket.WebSocket$READYSTATE r1 = r3.readystate     // Catch:{ all -> 0x0036 }
            dji.thirdparty.org.java_websocket.WebSocket$READYSTATE r2 = dji.thirdparty.org.java_websocket.WebSocket.READYSTATE.CLOSED     // Catch:{ all -> 0x0036 }
            if (r1 != r2) goto L_0x0009
        L_0x0007:
            monitor-exit(r3)
            return
        L_0x0009:
            java.nio.channels.SelectionKey r1 = r3.key     // Catch:{ all -> 0x0036 }
            if (r1 == 0) goto L_0x0012
            java.nio.channels.SelectionKey r1 = r3.key     // Catch:{ all -> 0x0036 }
            r1.cancel()     // Catch:{ all -> 0x0036 }
        L_0x0012:
            java.nio.channels.ByteChannel r1 = r3.channel     // Catch:{ all -> 0x0036 }
            if (r1 == 0) goto L_0x001b
            java.nio.channels.ByteChannel r1 = r3.channel     // Catch:{ IOException -> 0x0039 }
            r1.close()     // Catch:{ IOException -> 0x0039 }
        L_0x001b:
            dji.thirdparty.org.java_websocket.WebSocketListener r1 = r3.wsl     // Catch:{ RuntimeException -> 0x0040 }
            r1.onWebsocketClose(r3, r4, r5, r6)     // Catch:{ RuntimeException -> 0x0040 }
        L_0x0020:
            dji.thirdparty.org.java_websocket.drafts.Draft r1 = r3.draft     // Catch:{ all -> 0x0036 }
            if (r1 == 0) goto L_0x0029
            dji.thirdparty.org.java_websocket.drafts.Draft r1 = r3.draft     // Catch:{ all -> 0x0036 }
            r1.reset()     // Catch:{ all -> 0x0036 }
        L_0x0029:
            r1 = 0
            r3.handshakerequest = r1     // Catch:{ all -> 0x0036 }
            dji.thirdparty.org.java_websocket.WebSocket$READYSTATE r1 = dji.thirdparty.org.java_websocket.WebSocket.READYSTATE.CLOSED     // Catch:{ all -> 0x0036 }
            r3.readystate = r1     // Catch:{ all -> 0x0036 }
            java.util.concurrent.BlockingQueue<java.nio.ByteBuffer> r1 = r3.outQueue     // Catch:{ all -> 0x0036 }
            r1.clear()     // Catch:{ all -> 0x0036 }
            goto L_0x0007
        L_0x0036:
            r1 = move-exception
            monitor-exit(r3)
            throw r1
        L_0x0039:
            r0 = move-exception
            dji.thirdparty.org.java_websocket.WebSocketListener r1 = r3.wsl     // Catch:{ all -> 0x0036 }
            r1.onWebsocketError(r3, r0)     // Catch:{ all -> 0x0036 }
            goto L_0x001b
        L_0x0040:
            r0 = move-exception
            dji.thirdparty.org.java_websocket.WebSocketListener r1 = r3.wsl     // Catch:{ all -> 0x0036 }
            r1.onWebsocketError(r3, r0)     // Catch:{ all -> 0x0036 }
            goto L_0x0020
        */
        throw new UnsupportedOperationException("Method not decompiled: dji.thirdparty.org.java_websocket.WebSocketImpl.closeConnection(int, java.lang.String, boolean):void");
    }

    /* access modifiers changed from: protected */
    public void closeConnection(int code, boolean remote) {
        closeConnection(code, "", remote);
    }

    public void closeConnection() {
        if (this.closedremotely == null) {
            throw new IllegalStateException("this method must be used in conjuction with flushAndClose");
        }
        closeConnection(this.closecode.intValue(), this.closemessage, this.closedremotely.booleanValue());
    }

    public void closeConnection(int code, String message) {
        closeConnection(code, message, false);
    }

    /* access modifiers changed from: protected */
    public synchronized void flushAndClose(int code, String message, boolean remote) {
        if (!this.flushandclosestate) {
            this.closecode = Integer.valueOf(code);
            this.closemessage = message;
            this.closedremotely = Boolean.valueOf(remote);
            this.flushandclosestate = true;
            this.wsl.onWriteDemand(this);
            try {
                this.wsl.onWebsocketClosing(this, code, message, remote);
            } catch (RuntimeException e) {
                this.wsl.onWebsocketError(this, e);
            }
            if (this.draft != null) {
                this.draft.reset();
            }
            this.handshakerequest = null;
        }
        return;
    }

    /* JADX DEBUG: Failed to find minimal casts for resolve overloaded methods, cast all args instead
     method: dji.thirdparty.org.java_websocket.WebSocketImpl.closeConnection(int, boolean):void
     arg types: [int, int]
     candidates:
      dji.thirdparty.org.java_websocket.WebSocketImpl.closeConnection(int, java.lang.String):void
      dji.thirdparty.org.java_websocket.WebSocket.closeConnection(int, java.lang.String):void
      dji.thirdparty.org.java_websocket.WebSocketImpl.closeConnection(int, boolean):void */
    public void eot() {
        if (getReadyState() == WebSocket.READYSTATE.NOT_YET_CONNECTED) {
            closeConnection(-1, true);
        } else if (this.flushandclosestate) {
            closeConnection(this.closecode.intValue(), this.closemessage, this.closedremotely.booleanValue());
        } else if (this.draft.getCloseHandshakeType() == Draft.CloseHandshakeType.NONE) {
            closeConnection(1000, true);
        } else if (this.draft.getCloseHandshakeType() != Draft.CloseHandshakeType.ONEWAY) {
            closeConnection(1006, true);
        } else if (this.role == WebSocket.Role.SERVER) {
            closeConnection(1006, true);
        } else {
            closeConnection(1000, true);
        }
    }

    public void close(int code) {
        close(code, "", false);
    }

    public void close(InvalidDataException e) {
        close(e.getCloseCode(), e.getMessage(), false);
    }

    public void send(String text) throws WebsocketNotConnectedException {
        if (text == null) {
            throw new IllegalArgumentException("Cannot send 'null' data to a WebSocketImpl.");
        }
        send(this.draft.createFrames(text, this.role == WebSocket.Role.CLIENT));
    }

    public void send(ByteBuffer bytes) throws IllegalArgumentException, WebsocketNotConnectedException {
        if (bytes == null) {
            throw new IllegalArgumentException("Cannot send 'null' data to a WebSocketImpl.");
        }
        send(this.draft.createFrames(bytes, this.role == WebSocket.Role.CLIENT));
    }

    public void send(byte[] bytes) throws IllegalArgumentException, WebsocketNotConnectedException {
        send(ByteBuffer.wrap(bytes));
    }

    private void send(Collection<Framedata> frames) {
        if (!isOpen()) {
            throw new WebsocketNotConnectedException();
        }
        for (Framedata f : frames) {
            sendFrame(f);
        }
    }

    public void sendFrame(Framedata framedata) {
        if (DEBUG) {
            System.out.println("send frame: " + framedata);
        }
        write(this.draft.createBinaryFrame(framedata));
    }

    public boolean hasBufferedData() {
        return !this.outQueue.isEmpty();
    }

    private Draft.HandshakeState isFlashEdgeCase(ByteBuffer request) throws IncompleteHandshakeException {
        request.mark();
        if (request.limit() > Draft.FLASH_POLICY_REQUEST.length) {
            return Draft.HandshakeState.NOT_MATCHED;
        }
        if (request.limit() < Draft.FLASH_POLICY_REQUEST.length) {
            throw new IncompleteHandshakeException(Draft.FLASH_POLICY_REQUEST.length);
        }
        int flash_policy_index = 0;
        while (request.hasRemaining()) {
            if (Draft.FLASH_POLICY_REQUEST[flash_policy_index] != request.get()) {
                request.reset();
                return Draft.HandshakeState.NOT_MATCHED;
            }
            flash_policy_index++;
        }
        return Draft.HandshakeState.MATCHED;
    }

    public void startHandshake(ClientHandshakeBuilder handshakedata) throws InvalidHandshakeException {
        if ($assertionsDisabled || this.readystate != WebSocket.READYSTATE.CONNECTING) {
            this.handshakerequest = this.draft.postProcessHandshakeRequestAsClient(handshakedata);
            try {
                this.wsl.onWebsocketHandshakeSentAsClient(this, this.handshakerequest);
                write(this.draft.createHandshake(this.handshakerequest, this.role));
            } catch (InvalidDataException e) {
                throw new InvalidHandshakeException("Handshake data rejected by client.");
            } catch (RuntimeException e2) {
                this.wsl.onWebsocketError(this, e2);
                throw new InvalidHandshakeException("rejected because of" + e2);
            }
        } else {
            throw new AssertionError("shall only be called once");
        }
    }

    private void write(ByteBuffer buf) {
        if (DEBUG) {
            System.out.println("write(" + buf.remaining() + "): {" + (buf.remaining() > 1000 ? "too big to display" : new String(buf.array())) + "}");
        }
        this.outQueue.add(buf);
        this.wsl.onWriteDemand(this);
    }

    private void write(List<ByteBuffer> bufs) {
        for (ByteBuffer b : bufs) {
            write(b);
        }
    }

    private void open(Handshakedata d) {
        if (DEBUG) {
            System.out.println("open using draft: " + this.draft.getClass().getSimpleName());
        }
        this.readystate = WebSocket.READYSTATE.OPEN;
        try {
            this.wsl.onWebsocketOpen(this, d);
        } catch (RuntimeException e) {
            this.wsl.onWebsocketError(this, e);
        }
    }

    public boolean isConnecting() {
        if ($assertionsDisabled || !this.flushandclosestate || this.readystate == WebSocket.READYSTATE.CONNECTING) {
            return this.readystate == WebSocket.READYSTATE.CONNECTING;
        }
        throw new AssertionError();
    }

    public boolean isOpen() {
        if ($assertionsDisabled || this.readystate != WebSocket.READYSTATE.OPEN || !this.flushandclosestate) {
            return this.readystate == WebSocket.READYSTATE.OPEN;
        }
        throw new AssertionError();
    }

    public boolean isClosing() {
        return this.readystate == WebSocket.READYSTATE.CLOSING;
    }

    public boolean isFlushAndClose() {
        return this.flushandclosestate;
    }

    public boolean isClosed() {
        return this.readystate == WebSocket.READYSTATE.CLOSED;
    }

    public WebSocket.READYSTATE getReadyState() {
        return this.readystate;
    }

    public int hashCode() {
        return super.hashCode();
    }

    public String toString() {
        return super.toString();
    }

    public InetSocketAddress getRemoteSocketAddress() {
        return this.wsl.getRemoteSocketAddress(this);
    }

    public InetSocketAddress getLocalSocketAddress() {
        return this.wsl.getLocalSocketAddress(this);
    }

    public Draft getDraft() {
        return this.draft;
    }

    public void close() {
        close(1000);
    }
}
