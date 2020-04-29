package dji.midware.usb.P3;

public class RecvBufferEvent {
    private byte[] mBuffer = new byte[16384];
    private int mLength;

    public void setBuffer(byte[] buffer, int length) {
        System.arraycopy(buffer, 0, this.mBuffer, 0, length);
        this.mLength = length;
    }

    public byte[] getBuffer() {
        return this.mBuffer;
    }

    public int getLength() {
        return this.mLength;
    }
}
