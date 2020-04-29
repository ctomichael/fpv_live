package dji.midware.ar.min3d.parser;

import dji.fieldAnnotation.EXClassNullAway;
import java.io.DataInput;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;

@EXClassNullAway
public class LittleEndianDataInputStream extends InputStream implements DataInput {
    private DataInputStream d;

    /* renamed from: in  reason: collision with root package name */
    private InputStream f21in;
    private byte[] w = new byte[8];

    public LittleEndianDataInputStream(InputStream in2) {
        this.f21in = in2;
        this.d = new DataInputStream(in2);
    }

    public int available() throws IOException {
        return this.d.available();
    }

    public final short readShort() throws IOException {
        this.d.readFully(this.w, 0, 2);
        return (short) (((this.w[1] & 255) << 8) | (this.w[0] & 255));
    }

    public String readString(int length) throws IOException {
        if (length == 0) {
            return null;
        }
        byte[] b = new byte[length];
        this.d.readFully(b);
        return new String(b, "US-ASCII");
    }

    public final int readUnsignedShort() throws IOException {
        this.d.readFully(this.w, 0, 2);
        return ((this.w[1] & 255) << 8) | (this.w[0] & 255);
    }

    public final char readChar() throws IOException {
        this.d.readFully(this.w, 0, 2);
        return (char) (((this.w[1] & 255) << 8) | (this.w[0] & 255));
    }

    public final int readInt() throws IOException {
        this.d.readFully(this.w, 0, 4);
        return (this.w[3] << 24) | ((this.w[2] & 255) << Tnaf.POW_2_WIDTH) | ((this.w[1] & 255) << 8) | (this.w[0] & 255);
    }

    public final long readLong() throws IOException {
        this.d.readFully(this.w, 0, 8);
        return (((long) this.w[7]) << 56) | (((long) (this.w[6] & 255)) << 48) | (((long) (this.w[5] & 255)) << 40) | (((long) (this.w[4] & 255)) << 32) | (((long) (this.w[3] & 255)) << 24) | (((long) (this.w[2] & 255)) << 16) | (((long) (this.w[1] & 255)) << 8) | ((long) (this.w[0] & 255));
    }

    public final float readFloat() throws IOException {
        return Float.intBitsToFloat(readInt());
    }

    public final double readDouble() throws IOException {
        return Double.longBitsToDouble(readLong());
    }

    public final int read(byte[] b, int off, int len) throws IOException {
        return this.f21in.read(b, off, len);
    }

    public final void readFully(byte[] b) throws IOException {
        this.d.readFully(b, 0, b.length);
    }

    public final void readFully(byte[] b, int off, int len) throws IOException {
        this.d.readFully(b, off, len);
    }

    public final int skipBytes(int n) throws IOException {
        return this.d.skipBytes(n);
    }

    public final boolean readBoolean() throws IOException {
        return this.d.readBoolean();
    }

    public final byte readByte() throws IOException {
        return this.d.readByte();
    }

    public int read() throws IOException {
        return this.f21in.read();
    }

    public final int readUnsignedByte() throws IOException {
        return this.d.readUnsignedByte();
    }

    @Deprecated
    public final String readLine() throws IOException {
        return this.d.readLine();
    }

    public final String readUTF() throws IOException {
        return this.d.readUTF();
    }

    public final void close() throws IOException {
        this.d.close();
    }
}
