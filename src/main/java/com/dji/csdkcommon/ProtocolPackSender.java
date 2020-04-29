package com.dji.csdkcommon;

import dji.midware.data.config.P3.CmdIdFlyc;
import dji.midware.data.config.P3.CmdSet;
import dji.midware.data.config.P3.DataConfig;
import dji.midware.data.config.P3.DeviceType;
import dji.midware.data.manager.P3.DataBase;
import dji.midware.data.packages.P3.RecvPack;
import dji.midware.data.packages.P3.SendPack;
import dji.midware.interfaces.CmdIdInterface;
import dji.midware.interfaces.DJIDataCallBack;
import dji.midware.interfaces.DJIDataSyncListener;

public class ProtocolPackSender extends DataBase implements DJIDataSyncListener {
    private boolean hasResultCode = false;
    private boolean isUseHighSpeedPort;
    private CmdIdInterface mCmdId = CmdIdFlyc.CmdIdType.Other;
    private CmdSet mCmdSet = CmdSet.OTHER;
    private int mCmdType = DataConfig.CMDTYPE.REQUEST.value();
    private byte[] mDataContent = new byte[1];
    private int mDataLength = 1;
    private int mEncryptType = DataConfig.EncryptType.NO.value();
    private int mIsNeedAck = DataConfig.NEEDACK.YES.value();
    private int mReceiverId = 0;
    private DeviceType mReceiverType = DeviceType.FLYC;
    private int mResultCode = 0;
    private int mRetryTime = 2;
    private int mSenderId = 0;
    private DeviceType mSenderType = DeviceType.APP;
    private int mSeq;
    private int mTimeout = 1000;

    public ProtocolPackSender setTimeout(float timeout) {
        this.mTimeout = (int) (1000.0f * timeout);
        return this;
    }

    public ProtocolPackSender setRetryTime(int retryTime) {
        this.mRetryTime = retryTime;
        return this;
    }

    public ProtocolPackSender setDataLength(int dataLength) {
        this.mDataLength = dataLength;
        return this;
    }

    public ProtocolPackSender setDataContent(byte[] dataContent) {
        this.mDataContent = dataContent;
        return this;
    }

    public ProtocolPackSender setCmdSet(CmdSet cmdSet) {
        this.mCmdSet = cmdSet;
        return this;
    }

    public ProtocolPackSender setCmdId(CmdIdInterface cmdId) {
        this.mCmdId = cmdId;
        return this;
    }

    public ProtocolPackSender setReceiverType(DeviceType receiverType) {
        this.mReceiverType = receiverType;
        return this;
    }

    public ProtocolPackSender setReceiverId(int receiverId) {
        this.mReceiverId = receiverId;
        return this;
    }

    public ProtocolPackSender setSenderType(DeviceType senderType) {
        this.mSenderType = senderType;
        return this;
    }

    public ProtocolPackSender setSenderId(int senderId) {
        this.mSenderId = senderId;
        return this;
    }

    public ProtocolPackSender setHasResultCode(boolean hasResultCode2) {
        this.hasResultCode = hasResultCode2;
        return this;
    }

    public int getResultCode() {
        return this.mResultCode;
    }

    public boolean hasResultCode() {
        return this.hasResultCode;
    }

    public ProtocolPackSender setCmdType(int cmdType) {
        this.mCmdType = cmdType;
        return this;
    }

    public ProtocolPackSender setIsNeedAck(int isNeedAck) {
        this.mIsNeedAck = isNeedAck;
        return this;
    }

    public ProtocolPackSender setEncryptType(int encryptType) {
        this.mEncryptType = encryptType;
        return this;
    }

    public ProtocolPackSender setSeq(int seq) {
        this.mSeq = seq;
        return this;
    }

    public ProtocolPackSender setUseHighSpeedPort(boolean useHighSpeedPort) {
        this.isUseHighSpeedPort = useHighSpeedPort;
        return this;
    }

    public RecvPack getRecvPack() {
        return this.recvPack;
    }

    public void setRecData(byte[] data) {
        if (data != null && data.length > 0) {
            if (this.hasResultCode) {
                this.mResultCode = data[0];
                byte[] tmpData = new byte[(data.length - 1)];
                System.arraycopy(data, 1, tmpData, 0, data.length - 1);
                super.setRecData(tmpData);
                return;
            }
            super.setRecData(data);
        }
    }

    /* access modifiers changed from: protected */
    public void doPack() {
        this._sendData = this.mDataContent;
    }

    public void start(DJIDataCallBack callBack) {
        start(generateSendPack(), callBack);
    }

    public void start() {
        start(generateSendPack());
    }

    private SendPack generateSendPack() {
        SendPack pack = new SendPack();
        pack.senderType = DeviceType.APP.value();
        pack.senderId = 0;
        pack.receiverType = this.mReceiverType.value();
        pack.receiverId = this.mReceiverId;
        pack.cmdType = this.mCmdType;
        pack.isNeedAck = this.mIsNeedAck;
        pack.encryptType = this.mEncryptType;
        pack.cmdSet = this.mCmdSet.value();
        pack.cmdId = this.mCmdId.value();
        pack.data = getSendData();
        if (this.mSeq > 0) {
            pack.seq = this.mSeq;
        }
        if (this.mTimeout != 0) {
            pack.timeOut = this.mTimeout;
        }
        pack.repeatTimes = this.mRetryTime;
        return pack;
    }
}
