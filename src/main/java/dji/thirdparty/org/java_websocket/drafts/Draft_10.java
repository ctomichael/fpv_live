package dji.thirdparty.org.java_websocket.drafts;

import dji.sdksharedlib.keycatalog.DJISDKCacheKeys;
import dji.thirdparty.org.java_websocket.WebSocket;
import dji.thirdparty.org.java_websocket.drafts.Draft;
import dji.thirdparty.org.java_websocket.exceptions.InvalidDataException;
import dji.thirdparty.org.java_websocket.exceptions.InvalidFrameException;
import dji.thirdparty.org.java_websocket.exceptions.InvalidHandshakeException;
import dji.thirdparty.org.java_websocket.exceptions.LimitExedeedException;
import dji.thirdparty.org.java_websocket.exceptions.NotSendableException;
import dji.thirdparty.org.java_websocket.framing.CloseFrameBuilder;
import dji.thirdparty.org.java_websocket.framing.FrameBuilder;
import dji.thirdparty.org.java_websocket.framing.Framedata;
import dji.thirdparty.org.java_websocket.framing.FramedataImpl1;
import dji.thirdparty.org.java_websocket.handshake.ClientHandshake;
import dji.thirdparty.org.java_websocket.handshake.ClientHandshakeBuilder;
import dji.thirdparty.org.java_websocket.handshake.HandshakeBuilder;
import dji.thirdparty.org.java_websocket.handshake.Handshakedata;
import dji.thirdparty.org.java_websocket.handshake.ServerHandshake;
import dji.thirdparty.org.java_websocket.handshake.ServerHandshakeBuilder;
import dji.thirdparty.org.java_websocket.util.Base64;
import dji.thirdparty.org.java_websocket.util.Charsetfunctions;
import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import kotlin.jvm.internal.ByteCompanionObject;
import org.bouncycastle.asn1.eac.CertificateBody;

public class Draft_10 extends Draft {
    static final /* synthetic */ boolean $assertionsDisabled = (!Draft_10.class.desiredAssertionStatus());
    private Framedata fragmentedframe = null;
    private ByteBuffer incompleteframe;
    private final Random reuseableRandom = new Random();

    private class IncompleteException extends Throwable {
        private static final long serialVersionUID = 7330519489840500997L;
        private int preferedsize;

        public IncompleteException(int preferedsize2) {
            this.preferedsize = preferedsize2;
        }

        public int getPreferedSize() {
            return this.preferedsize;
        }
    }

    public static int readVersion(Handshakedata handshakedata) {
        String vers = handshakedata.getFieldValue("Sec-WebSocket-Version");
        if (vers.length() <= 0) {
            return -1;
        }
        try {
            return new Integer(vers.trim()).intValue();
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    public Draft.HandshakeState acceptHandshakeAsClient(ClientHandshake request, ServerHandshake response) throws InvalidHandshakeException {
        if (!request.hasFieldValue("Sec-WebSocket-Key") || !response.hasFieldValue("Sec-WebSocket-Accept")) {
            return Draft.HandshakeState.NOT_MATCHED;
        }
        if (generateFinalKey(request.getFieldValue("Sec-WebSocket-Key")).equals(response.getFieldValue("Sec-WebSocket-Accept"))) {
            return Draft.HandshakeState.MATCHED;
        }
        return Draft.HandshakeState.NOT_MATCHED;
    }

    public Draft.HandshakeState acceptHandshakeAsServer(ClientHandshake handshakedata) throws InvalidHandshakeException {
        int v = readVersion(handshakedata);
        if (v == 7 || v == 8) {
            return basicAccept(handshakedata) ? Draft.HandshakeState.MATCHED : Draft.HandshakeState.NOT_MATCHED;
        }
        return Draft.HandshakeState.NOT_MATCHED;
    }

    public ByteBuffer createBinaryFrame(Framedata framedata) {
        int i;
        ByteBuffer mes = framedata.getPayloadData();
        boolean mask = this.role == WebSocket.Role.CLIENT;
        int sizebytes = mes.remaining() <= 125 ? 1 : mes.remaining() <= 65535 ? 2 : 8;
        if (sizebytes > 1) {
            i = sizebytes + 1;
        } else {
            i = sizebytes;
        }
        ByteBuffer buf = ByteBuffer.allocate((mask ? 4 : 0) + i + 1 + mes.remaining());
        buf.put((byte) (((byte) (framedata.isFin() ? -128 : 0)) | fromOpcode(framedata.getOpcode())));
        byte[] payloadlengthbytes = toByteArray((long) mes.remaining(), sizebytes);
        if ($assertionsDisabled || payloadlengthbytes.length == sizebytes) {
            if (sizebytes == 1) {
                buf.put((byte) ((mask ? Byte.MIN_VALUE : 0) | payloadlengthbytes[0]));
            } else if (sizebytes == 2) {
                buf.put((byte) ((mask ? -128 : 0) | 126));
                buf.put(payloadlengthbytes);
            } else if (sizebytes == 8) {
                buf.put((byte) ((mask ? -128 : 0) | CertificateBody.profileType));
                buf.put(payloadlengthbytes);
            } else {
                throw new RuntimeException("Size representation not supported/specified");
            }
            if (mask) {
                ByteBuffer maskkey = ByteBuffer.allocate(4);
                maskkey.putInt(this.reuseableRandom.nextInt());
                buf.put(maskkey.array());
                for (int i2 = 0; i2 < mes.limit(); i2++) {
                    buf.put((byte) (mes.get() ^ maskkey.get(i2 % 4)));
                }
            } else {
                buf.put(mes);
            }
            if ($assertionsDisabled || buf.remaining() == 0) {
                buf.flip();
                return buf;
            }
            throw new AssertionError(buf.remaining());
        }
        throw new AssertionError();
    }

    public List<Framedata> createFrames(ByteBuffer binary, boolean mask) {
        FrameBuilder curframe = new FramedataImpl1();
        try {
            curframe.setPayload(binary);
            curframe.setFin(true);
            curframe.setOptcode(Framedata.Opcode.BINARY);
            curframe.setTransferemasked(mask);
            return Collections.singletonList(curframe);
        } catch (InvalidDataException e) {
            throw new NotSendableException(e);
        }
    }

    public List<Framedata> createFrames(String text, boolean mask) {
        FrameBuilder curframe = new FramedataImpl1();
        try {
            curframe.setPayload(ByteBuffer.wrap(Charsetfunctions.utf8Bytes(text)));
            curframe.setFin(true);
            curframe.setOptcode(Framedata.Opcode.TEXT);
            curframe.setTransferemasked(mask);
            return Collections.singletonList(curframe);
        } catch (InvalidDataException e) {
            throw new NotSendableException(e);
        }
    }

    private byte fromOpcode(Framedata.Opcode opcode) {
        if (opcode == Framedata.Opcode.CONTINUOUS) {
            return 0;
        }
        if (opcode == Framedata.Opcode.TEXT) {
            return 1;
        }
        if (opcode == Framedata.Opcode.BINARY) {
            return 2;
        }
        if (opcode == Framedata.Opcode.CLOSING) {
            return 8;
        }
        if (opcode == Framedata.Opcode.PING) {
            return 9;
        }
        if (opcode == Framedata.Opcode.PONG) {
            return 10;
        }
        throw new RuntimeException("Don't know how to handle " + opcode.toString());
    }

    private String generateFinalKey(String in2) {
        try {
            return Base64.encodeBytes(MessageDigest.getInstance("SHA1").digest((in2.trim() + "258EAFA5-E914-47DA-95CA-C5AB0DC85B11").getBytes()));
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    public ClientHandshakeBuilder postProcessHandshakeRequestAsClient(ClientHandshakeBuilder request) {
        request.put("Upgrade", "websocket");
        request.put(DJISDKCacheKeys.CONNECTION, "Upgrade");
        request.put("Sec-WebSocket-Version", "8");
        byte[] random = new byte[16];
        this.reuseableRandom.nextBytes(random);
        request.put("Sec-WebSocket-Key", Base64.encodeBytes(random));
        return request;
    }

    public HandshakeBuilder postProcessHandshakeResponseAsServer(ClientHandshake request, ServerHandshakeBuilder response) throws InvalidHandshakeException {
        response.put("Upgrade", "websocket");
        response.put(DJISDKCacheKeys.CONNECTION, request.getFieldValue(DJISDKCacheKeys.CONNECTION));
        response.setHttpStatusMessage("Switching Protocols");
        String seckey = request.getFieldValue("Sec-WebSocket-Key");
        if (seckey == null) {
            throw new InvalidHandshakeException("missing Sec-WebSocket-Key");
        }
        response.put("Sec-WebSocket-Accept", generateFinalKey(seckey));
        return response;
    }

    private byte[] toByteArray(long val, int bytecount) {
        byte[] buffer = new byte[bytecount];
        int highest = (bytecount * 8) - 8;
        for (int i = 0; i < bytecount; i++) {
            buffer[i] = (byte) ((int) (val >>> (highest - (i * 8))));
        }
        return buffer;
    }

    private Framedata.Opcode toOpcode(byte opcode) throws InvalidFrameException {
        switch (opcode) {
            case 0:
                return Framedata.Opcode.CONTINUOUS;
            case 1:
                return Framedata.Opcode.TEXT;
            case 2:
                return Framedata.Opcode.BINARY;
            case 3:
            case 4:
            case 5:
            case 6:
            case 7:
            default:
                throw new InvalidFrameException("unknow optcode " + ((int) ((short) opcode)));
            case 8:
                return Framedata.Opcode.CLOSING;
            case 9:
                return Framedata.Opcode.PING;
            case 10:
                return Framedata.Opcode.PONG;
        }
    }

    public List<Framedata> translateFrame(ByteBuffer buffer) throws LimitExedeedException, InvalidDataException {
        List<Framedata> frames = new LinkedList<>();
        if (this.incompleteframe != null) {
            try {
                buffer.mark();
                int available_next_byte_count = buffer.remaining();
                int expected_next_byte_count = this.incompleteframe.remaining();
                if (expected_next_byte_count > available_next_byte_count) {
                    this.incompleteframe.put(buffer.array(), buffer.position(), available_next_byte_count);
                    buffer.position(buffer.position() + available_next_byte_count);
                    return Collections.emptyList();
                }
                this.incompleteframe.put(buffer.array(), buffer.position(), expected_next_byte_count);
                buffer.position(buffer.position() + expected_next_byte_count);
                frames.add(translateSingleFrame((ByteBuffer) this.incompleteframe.duplicate().position(0)));
                this.incompleteframe = null;
            } catch (IncompleteException e) {
                int limit = this.incompleteframe.limit();
                ByteBuffer extendedframe = ByteBuffer.allocate(checkAlloc(e.getPreferedSize()));
                if ($assertionsDisabled || extendedframe.limit() > this.incompleteframe.limit()) {
                    this.incompleteframe.rewind();
                    extendedframe.put(this.incompleteframe);
                    this.incompleteframe = extendedframe;
                    return translateFrame(buffer);
                }
                throw new AssertionError();
            }
        }
        while (buffer.hasRemaining()) {
            buffer.mark();
            try {
                frames.add(translateSingleFrame(buffer));
            } catch (IncompleteException e2) {
                buffer.reset();
                this.incompleteframe = ByteBuffer.allocate(checkAlloc(e2.getPreferedSize()));
                this.incompleteframe.put(buffer);
                return frames;
            }
        }
        return frames;
    }

    public Framedata translateSingleFrame(ByteBuffer buffer) throws IncompleteException, InvalidDataException {
        FrameBuilder frame;
        int maxpacketsize = buffer.remaining();
        int realpacketsize = 2;
        if (maxpacketsize < 2) {
            throw new IncompleteException(2);
        }
        byte b1 = buffer.get();
        boolean FIN = (b1 >> 8) != 0;
        byte rsv = (byte) ((b1 & ByteCompanionObject.MAX_VALUE) >> 4);
        if (rsv != 0) {
            throw new InvalidFrameException("bad rsv " + ((int) rsv));
        }
        byte b2 = buffer.get();
        boolean MASK = (b2 & Byte.MIN_VALUE) != 0;
        int payloadlength = (byte) (b2 & ByteCompanionObject.MAX_VALUE);
        Framedata.Opcode optcode = toOpcode((byte) (b1 & 15));
        if (FIN || !(optcode == Framedata.Opcode.PING || optcode == Framedata.Opcode.PONG || optcode == Framedata.Opcode.CLOSING)) {
            if (payloadlength < 0 || payloadlength > 125) {
                if (optcode == Framedata.Opcode.PING || optcode == Framedata.Opcode.PONG || optcode == Framedata.Opcode.CLOSING) {
                    throw new InvalidFrameException("more than 125 octets");
                } else if (payloadlength == 126) {
                    realpacketsize = 2 + 2;
                    if (maxpacketsize < realpacketsize) {
                        throw new IncompleteException(realpacketsize);
                    }
                    byte[] sizebytes = new byte[3];
                    sizebytes[1] = buffer.get();
                    sizebytes[2] = buffer.get();
                    payloadlength = new BigInteger(sizebytes).intValue();
                } else {
                    realpacketsize = 2 + 8;
                    if (maxpacketsize < realpacketsize) {
                        throw new IncompleteException(realpacketsize);
                    }
                    byte[] bytes = new byte[8];
                    for (int i = 0; i < 8; i++) {
                        bytes[i] = buffer.get();
                    }
                    long length = new BigInteger(bytes).longValue();
                    if (length > 2147483647L) {
                        throw new LimitExedeedException("Payloadsize is to big...");
                    }
                    payloadlength = (int) length;
                }
            }
            int realpacketsize2 = realpacketsize + (MASK ? 4 : 0) + payloadlength;
            if (maxpacketsize < realpacketsize2) {
                throw new IncompleteException(realpacketsize2);
            }
            ByteBuffer payload = ByteBuffer.allocate(checkAlloc(payloadlength));
            if (MASK) {
                byte[] maskskey = new byte[4];
                buffer.get(maskskey);
                for (int i2 = 0; i2 < payloadlength; i2++) {
                    payload.put((byte) (buffer.get() ^ maskskey[i2 % 4]));
                }
            } else {
                payload.put(buffer.array(), buffer.position(), payload.limit());
                buffer.position(buffer.position() + payload.limit());
            }
            if (optcode == Framedata.Opcode.CLOSING) {
                frame = new CloseFrameBuilder();
            } else {
                frame = new FramedataImpl1();
                frame.setFin(FIN);
                frame.setOptcode(optcode);
            }
            payload.flip();
            frame.setPayload(payload);
            return frame;
        }
        throw new InvalidFrameException("control frames may no be fragmented");
    }

    public void reset() {
        this.incompleteframe = null;
    }

    public Draft copyInstance() {
        return new Draft_10();
    }

    public Draft.CloseHandshakeType getCloseHandshakeType() {
        return Draft.CloseHandshakeType.TWOWAY;
    }
}
