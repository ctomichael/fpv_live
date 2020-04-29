package com.dji.cmd.v1.base;

public abstract class Protocol {
    protected int cmdId = 0;
    protected int cmdSet = 0;
    protected byte[] innerReq = null;
    protected byte[] innerRsp = null;
    protected int needAck = -1;
    protected int reqSize = -1;
    protected int rspSize = -1;

    public enum Type {
        REQ,
        RSP
    }

    public byte[] getReqBytes() {
        if (this.innerReq == null || this.innerReq.length <= 0) {
            return null;
        }
        byte[] res = new byte[this.innerReq.length];
        System.arraycopy(this.innerReq, 0, res, 0, this.innerReq.length);
        return res;
    }

    public byte[] getRspBytes() {
        byte[] res = new byte[this.innerRsp.length];
        System.arraycopy(this.innerRsp, 0, res, 0, this.innerRsp.length);
        return res;
    }

    public void setRspBytes(byte[] data) {
        this.innerRsp = new byte[data.length];
        System.arraycopy(data, 0, this.innerRsp, 0, data.length);
    }

    public void setReqBytes(byte[] data) {
        this.innerReq = new byte[data.length];
        System.arraycopy(data, 0, this.innerReq, 0, data.length);
    }

    public void createRspBytes() {
        if (this.rspSize > 0) {
            this.innerRsp = new byte[this.rspSize];
        }
    }

    public void createReqBytes() {
        if (this.reqSize > 0) {
            this.innerReq = new byte[this.reqSize];
        }
    }

    public int getCmdId() {
        return this.cmdId;
    }

    public int getCmdSet() {
        return this.cmdSet;
    }

    public int needAck() {
        if (this.needAck >= 0) {
            return this.needAck;
        }
        if (this.cmdSet == 1) {
            return 0;
        }
        return 2;
    }

    private void checkData(Type type) {
        if (type == Type.REQ) {
            if (this.innerReq == null && this.reqSize > 0) {
                this.innerReq = new byte[this.reqSize];
            }
        } else if (type == Type.RSP && this.innerRsp == null && this.rspSize > 0) {
            this.innerRsp = new byte[this.rspSize];
        }
    }

    public boolean setValue(Type type, byte value, int offset, int len) {
        checkData(type);
        return ProtocolBytesUtil.setValue(value, type == Type.REQ ? this.innerReq : this.innerRsp, offset, len);
    }

    public boolean setValue(Type type, short value, int offset, int len) {
        checkData(type);
        return ProtocolBytesUtil.setValue(value, type == Type.REQ ? this.innerReq : this.innerRsp, offset, len);
    }

    public boolean setValue(Type type, int value, int offset, int len) {
        checkData(type);
        return ProtocolBytesUtil.setValue(value, type == Type.REQ ? this.innerReq : this.innerRsp, offset, len);
    }

    public boolean setValue(Type type, long value, int offset, int len) {
        checkData(type);
        return ProtocolBytesUtil.setValue(value, type == Type.REQ ? this.innerReq : this.innerRsp, offset, len);
    }

    public boolean setValue(Type type, byte value, int offset, int len, int bitOffset, int bitLen) {
        checkData(type);
        return ProtocolBytesUtil.setValue(value, type == Type.REQ ? this.innerReq : this.innerRsp, offset, len, bitOffset, bitLen);
    }

    public boolean setValue(Type type, short value, int offset, int len, int bitOffset, int bitLen) {
        checkData(type);
        return ProtocolBytesUtil.setValue(value, type == Type.REQ ? this.innerReq : this.innerRsp, offset, len, bitOffset, bitLen);
    }

    public boolean setValue(Type type, int value, int offset, int len, int bitOffset, int bitLen) {
        checkData(type);
        return ProtocolBytesUtil.setValue(value, type == Type.REQ ? this.innerReq : this.innerRsp, offset, len, bitOffset, bitLen);
    }

    public boolean setValue(Type type, long value, int offset, int len, int bitOffset, int bitLen) {
        checkData(type);
        return ProtocolBytesUtil.setValue(value, type == Type.REQ ? this.innerReq : this.innerRsp, offset, len, bitOffset, bitLen);
    }

    public boolean setValue(Type type, float value, int offset, int len) {
        checkData(type);
        return ProtocolBytesUtil.setValue(value, type == Type.REQ ? this.innerReq : this.innerRsp, offset, len);
    }

    public boolean setValue(Type type, double value, int offset, int len) {
        checkData(type);
        return ProtocolBytesUtil.setValue(value, type == Type.REQ ? this.innerReq : this.innerRsp, offset, len);
    }

    public byte getByte(Type type, int offset, int len, int bitOffset, int bitLen) {
        checkData(type);
        return ProtocolBytesUtil.getByte(type == Type.REQ ? this.innerReq : this.innerRsp, offset, len, bitOffset, bitLen);
    }

    public short getShort(Type type, int offset, int len, int bitOffset, int bitLen) {
        checkData(type);
        return ProtocolBytesUtil.getShort(type == Type.REQ ? this.innerReq : this.innerRsp, offset, len, bitOffset, bitLen);
    }

    public int getInt(Type type, int offset, int len, int bitOffset, int bitLen) {
        checkData(type);
        return ProtocolBytesUtil.getInt(type == Type.REQ ? this.innerReq : this.innerRsp, offset, len, bitOffset, bitLen);
    }

    public long getLong(Type type, int offset, int len, int bitOffset, int bitLen) {
        checkData(type);
        return ProtocolBytesUtil.getLong(type == Type.REQ ? this.innerReq : this.innerRsp, offset, len, bitOffset, bitLen);
    }

    public byte getByte(Type type, int offset) {
        checkData(type);
        return ProtocolBytesUtil.getByte(type == Type.REQ ? this.innerReq : this.innerRsp, offset);
    }

    public short getShort(Type type, int offset, int len) {
        checkData(type);
        return ProtocolBytesUtil.getShort(type == Type.REQ ? this.innerReq : this.innerRsp, offset, len);
    }

    public int getInt(Type type, int offset, int len) {
        checkData(type);
        return ProtocolBytesUtil.getInt(type == Type.REQ ? this.innerReq : this.innerRsp, offset, len);
    }

    public long getLong(Type type, int offset, int len) {
        checkData(type);
        return ProtocolBytesUtil.getLong(type == Type.REQ ? this.innerReq : this.innerRsp, offset, len);
    }

    public float getFloat(Type type, int offset, int len) {
        checkData(type);
        return ProtocolBytesUtil.getFloat(type == Type.REQ ? this.innerReq : this.innerRsp, offset, len);
    }

    public double getDouble(Type type, int offset, int len) {
        checkData(type);
        return ProtocolBytesUtil.getDouble(type == Type.REQ ? this.innerReq : this.innerRsp, offset, len);
    }

    public byte getByte(Type type, int offset, int len) {
        checkData(type);
        return ProtocolBytesUtil.getByte(type == Type.REQ ? this.innerReq : this.innerRsp, offset, len);
    }
}
