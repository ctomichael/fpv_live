package dji.thirdparty.org.java_websocket.drafts;

import dji.sdksharedlib.keycatalog.DJISDKCacheKeys;
import dji.thirdparty.org.java_websocket.drafts.Draft;
import dji.thirdparty.org.java_websocket.exceptions.InvalidDataException;
import dji.thirdparty.org.java_websocket.exceptions.InvalidHandshakeException;
import dji.thirdparty.org.java_websocket.exceptions.NotSendableException;
import dji.thirdparty.org.java_websocket.framing.FrameBuilder;
import dji.thirdparty.org.java_websocket.framing.Framedata;
import dji.thirdparty.org.java_websocket.framing.FramedataImpl1;
import dji.thirdparty.org.java_websocket.handshake.ClientHandshake;
import dji.thirdparty.org.java_websocket.handshake.ClientHandshakeBuilder;
import dji.thirdparty.org.java_websocket.handshake.HandshakeBuilder;
import dji.thirdparty.org.java_websocket.handshake.ServerHandshake;
import dji.thirdparty.org.java_websocket.handshake.ServerHandshakeBuilder;
import dji.thirdparty.org.java_websocket.util.Charsetfunctions;
import java.nio.ByteBuffer;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class Draft_75 extends Draft {
    public static final byte CR = 13;
    public static final byte END_OF_FRAME = -1;
    public static final byte LF = 10;
    public static final byte START_OF_FRAME = 0;
    protected ByteBuffer currentFrame;
    private boolean inframe = false;
    protected boolean readingState = false;
    protected List<Framedata> readyframes = new LinkedList();
    private final Random reuseableRandom = new Random();

    public Draft.HandshakeState acceptHandshakeAsClient(ClientHandshake request, ServerHandshake response) {
        return (!request.getFieldValue("WebSocket-Origin").equals(response.getFieldValue("Origin")) || !basicAccept(response)) ? Draft.HandshakeState.NOT_MATCHED : Draft.HandshakeState.MATCHED;
    }

    public Draft.HandshakeState acceptHandshakeAsServer(ClientHandshake handshakedata) {
        if (!handshakedata.hasFieldValue("Origin") || !basicAccept(handshakedata)) {
            return Draft.HandshakeState.NOT_MATCHED;
        }
        return Draft.HandshakeState.MATCHED;
    }

    public ByteBuffer createBinaryFrame(Framedata framedata) {
        if (framedata.getOpcode() != Framedata.Opcode.TEXT) {
            throw new RuntimeException("only text frames supported");
        }
        ByteBuffer pay = framedata.getPayloadData();
        ByteBuffer b = ByteBuffer.allocate(pay.remaining() + 2);
        b.put((byte) 0);
        pay.mark();
        b.put(pay);
        pay.reset();
        b.put((byte) -1);
        b.flip();
        return b;
    }

    public List<Framedata> createFrames(ByteBuffer binary, boolean mask) {
        throw new RuntimeException("not yet implemented");
    }

    public List<Framedata> createFrames(String text, boolean mask) {
        FrameBuilder frame = new FramedataImpl1();
        try {
            frame.setPayload(ByteBuffer.wrap(Charsetfunctions.utf8Bytes(text)));
            frame.setFin(true);
            frame.setOptcode(Framedata.Opcode.TEXT);
            frame.setTransferemasked(mask);
            return Collections.singletonList(frame);
        } catch (InvalidDataException e) {
            throw new NotSendableException(e);
        }
    }

    public ClientHandshakeBuilder postProcessHandshakeRequestAsClient(ClientHandshakeBuilder request) throws InvalidHandshakeException {
        request.put("Upgrade", "WebSocket");
        request.put(DJISDKCacheKeys.CONNECTION, "Upgrade");
        if (!request.hasFieldValue("Origin")) {
            request.put("Origin", "random" + this.reuseableRandom.nextInt());
        }
        return request;
    }

    public HandshakeBuilder postProcessHandshakeResponseAsServer(ClientHandshake request, ServerHandshakeBuilder response) throws InvalidHandshakeException {
        response.setHttpStatusMessage("Web Socket Protocol Handshake");
        response.put("Upgrade", "WebSocket");
        response.put(DJISDKCacheKeys.CONNECTION, request.getFieldValue(DJISDKCacheKeys.CONNECTION));
        response.put("WebSocket-Origin", request.getFieldValue("Origin"));
        response.put("WebSocket-Location", "ws://" + request.getFieldValue("Host") + request.getResourceDescriptor());
        return response;
    }

    /* access modifiers changed from: protected */
    public List<Framedata> translateRegularFrame(ByteBuffer buffer) throws InvalidDataException {
        while (buffer.hasRemaining()) {
            byte newestByte = buffer.get();
            if (newestByte == 0) {
                if (this.readingState) {
                    return null;
                }
                this.readingState = true;
            } else if (newestByte == -1) {
                if (!this.readingState) {
                    return null;
                }
                if (this.currentFrame != null) {
                    this.currentFrame.flip();
                    FramedataImpl1 curframe = new FramedataImpl1();
                    curframe.setPayload(this.currentFrame);
                    curframe.setFin(true);
                    curframe.setOptcode(this.inframe ? Framedata.Opcode.CONTINUOUS : Framedata.Opcode.TEXT);
                    this.readyframes.add(curframe);
                    this.currentFrame = null;
                    buffer.mark();
                }
                this.readingState = false;
                this.inframe = false;
            } else if (!this.readingState) {
                return null;
            } else {
                if (this.currentFrame == null) {
                    this.currentFrame = createBuffer();
                } else if (!this.currentFrame.hasRemaining()) {
                    this.currentFrame = increaseBuffer(this.currentFrame);
                }
                this.currentFrame.put(newestByte);
            }
        }
        if (this.readingState) {
            FramedataImpl1 curframe2 = new FramedataImpl1();
            this.currentFrame.flip();
            curframe2.setPayload(this.currentFrame);
            curframe2.setFin(false);
            curframe2.setOptcode(this.inframe ? Framedata.Opcode.CONTINUOUS : Framedata.Opcode.TEXT);
            this.inframe = true;
            this.readyframes.add(curframe2);
        }
        List<Framedata> list = this.readyframes;
        this.readyframes = new LinkedList();
        this.currentFrame = null;
        return list;
    }

    public List<Framedata> translateFrame(ByteBuffer buffer) throws InvalidDataException {
        List<Framedata> frames = translateRegularFrame(buffer);
        if (frames != null) {
            return frames;
        }
        throw new InvalidDataException(1002);
    }

    public void reset() {
        this.readingState = false;
        this.currentFrame = null;
    }

    public Draft.CloseHandshakeType getCloseHandshakeType() {
        return Draft.CloseHandshakeType.NONE;
    }

    public ByteBuffer createBuffer() {
        return ByteBuffer.allocate(INITIAL_FAMESIZE);
    }

    public ByteBuffer increaseBuffer(ByteBuffer full) {
        full.flip();
        ByteBuffer newbuffer = ByteBuffer.allocate(full.capacity() * 2);
        newbuffer.put(full);
        return newbuffer;
    }

    public Draft copyInstance() {
        return new Draft_75();
    }
}
