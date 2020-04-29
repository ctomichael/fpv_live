package dji.thirdparty.org.java_websocket.framing;

import dji.thirdparty.org.java_websocket.exceptions.InvalidDataException;
import dji.thirdparty.org.java_websocket.exceptions.InvalidFrameException;
import dji.thirdparty.org.java_websocket.framing.Framedata;
import dji.thirdparty.org.java_websocket.util.Charsetfunctions;
import java.nio.ByteBuffer;
import java.util.Arrays;

public class FramedataImpl1 implements FrameBuilder {
    protected static byte[] emptyarray = new byte[0];
    protected boolean fin;
    protected Framedata.Opcode optcode;
    protected boolean transferemasked;
    private ByteBuffer unmaskedpayload;

    public FramedataImpl1() {
    }

    public FramedataImpl1(Framedata.Opcode op) {
        this.optcode = op;
        this.unmaskedpayload = ByteBuffer.wrap(emptyarray);
    }

    public FramedataImpl1(Framedata f) {
        this.fin = f.isFin();
        this.optcode = f.getOpcode();
        this.unmaskedpayload = f.getPayloadData();
        this.transferemasked = f.getTransfereMasked();
    }

    public boolean isFin() {
        return this.fin;
    }

    public Framedata.Opcode getOpcode() {
        return this.optcode;
    }

    public boolean getTransfereMasked() {
        return this.transferemasked;
    }

    public ByteBuffer getPayloadData() {
        return this.unmaskedpayload;
    }

    public void setFin(boolean fin2) {
        this.fin = fin2;
    }

    public void setOptcode(Framedata.Opcode optcode2) {
        this.optcode = optcode2;
    }

    public void setPayload(ByteBuffer payload) throws InvalidDataException {
        this.unmaskedpayload = payload;
    }

    public void setTransferemasked(boolean transferemasked2) {
        this.transferemasked = transferemasked2;
    }

    public void append(Framedata nextframe) throws InvalidFrameException {
        ByteBuffer b = nextframe.getPayloadData();
        if (this.unmaskedpayload == null) {
            this.unmaskedpayload = ByteBuffer.allocate(b.remaining());
            b.mark();
            this.unmaskedpayload.put(b);
            b.reset();
        } else {
            b.mark();
            this.unmaskedpayload.position(this.unmaskedpayload.limit());
            this.unmaskedpayload.limit(this.unmaskedpayload.capacity());
            if (b.remaining() > this.unmaskedpayload.remaining()) {
                ByteBuffer tmp = ByteBuffer.allocate(b.remaining() + this.unmaskedpayload.capacity());
                this.unmaskedpayload.flip();
                tmp.put(this.unmaskedpayload);
                tmp.put(b);
                this.unmaskedpayload = tmp;
            } else {
                this.unmaskedpayload.put(b);
            }
            this.unmaskedpayload.rewind();
            b.reset();
        }
        this.fin = nextframe.isFin();
    }

    public String toString() {
        return "Framedata{ optcode:" + getOpcode() + ", fin:" + isFin() + ", payloadlength:" + this.unmaskedpayload.limit() + ", payload:" + Arrays.toString(Charsetfunctions.utf8Bytes(new String(this.unmaskedpayload.array()))) + "}";
    }
}
