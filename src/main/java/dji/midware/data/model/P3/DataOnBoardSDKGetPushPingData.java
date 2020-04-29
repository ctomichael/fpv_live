package dji.midware.data.model.P3;

import dji.midware.data.config.P3.CmdIdOnBoardSDK;
import dji.midware.data.config.P3.CmdSet;
import dji.midware.data.config.P3.DataConfig;
import dji.midware.data.config.P3.DeviceType;
import dji.midware.data.manager.P3.DataBase;
import dji.midware.data.packages.P3.SendPack;
import dji.midware.interfaces.DJIDataAsync2Listener;
import dji.midware.util.BytesUtil;
import java.util.Random;

public class DataOnBoardSDKGetPushPingData extends DataBase implements DJIDataAsync2Listener {
    private static long mSeq = 0;
    private int mDataLen;
    private Random mRandom;
    private long mReceiveTime;
    private int mReceiverType;

    private DataOnBoardSDKGetPushPingData() {
        this.mRandom = new Random();
    }

    public static DataOnBoardSDKGetPushPingData getInstance() {
        return Holder.INSTANCE;
    }

    private static class Holder {
        /* access modifiers changed from: private */
        public static DataOnBoardSDKGetPushPingData INSTANCE = new DataOnBoardSDKGetPushPingData();

        private Holder() {
        }
    }

    public DataOnBoardSDKGetPushPingData setReceiverType(int type) {
        this.mReceiverType = type;
        return this;
    }

    public DataOnBoardSDKGetPushPingData setDataLen(int len) {
        this.mDataLen = len;
        return this;
    }

    /* access modifiers changed from: protected */
    public void doPack() {
        this._sendData = new byte[(this.mDataLen + 12)];
        increaseSeq();
        byte[] seqBytes = BytesUtil.getBytes(mSeq);
        System.arraycopy(seqBytes, 0, this._sendData, 0, seqBytes.length);
        byte[] timeBytes = BytesUtil.getBytes(System.currentTimeMillis());
        System.arraycopy(timeBytes, 0, this._sendData, 4, timeBytes.length);
        if (this.mDataLen > 0) {
            for (int index = 12; index < this.mDataLen + 12; index++) {
                this._sendData[index] = (byte) this.mRandom.nextInt();
            }
        }
    }

    public void start() {
        SendPack pack = new SendPack();
        pack.senderType = DeviceType.APP.value();
        pack.receiverType = this.mReceiverType;
        pack.cmdType = DataConfig.CMDTYPE.REQUEST.value();
        pack.isNeedAck = DataConfig.NEEDACK.NO.value();
        pack.encryptType = DataConfig.EncryptType.NO.value();
        pack.cmdSet = CmdSet.OnboardSDK.value();
        pack.cmdId = CmdIdOnBoardSDK.CmdIdType.totalLinkDebug.value();
        super.start(pack);
    }

    private void increaseSeq() {
        if (mSeq == 2147483647L) {
            mSeq = 0;
        } else {
            mSeq++;
        }
    }

    public void setRecData(byte[] data) {
        this.mReceiveTime = System.currentTimeMillis();
        super.setRecData(data);
    }

    public long getInnerSeq() {
        return ((Long) get(0, 4, Long.class)).longValue();
    }

    public long getSendTime() {
        return ((Long) get(4, 8, Long.class)).longValue();
    }
}
