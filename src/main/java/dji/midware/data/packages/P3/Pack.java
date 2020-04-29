package dji.midware.data.packages.P3;

import dji.fieldAnnotation.EXClassNullAway;

@EXClassNullAway
public abstract class Pack {
    public byte[] buffer;
    public int ccode;
    public int cmdId;
    public int cmdSet;
    public int cmdType;
    protected int crc16;
    protected int crc8;
    public byte[] data;
    public int encryptType;
    public int isNeedAck;
    protected int length;
    public int receiverId;
    public int receiverType;
    public int senderId;
    public int senderType;
    public int seq;
    protected byte sof = 85;
    public int version = 1;

    public int getLength() {
        return this.length;
    }
}
