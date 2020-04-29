package dji.midware.data.model.P3;

import android.support.annotation.Keep;
import android.text.TextUtils;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.config.P3.CmdIdFlyc;
import dji.midware.data.config.P3.CmdSet;
import dji.midware.data.config.P3.DataConfig;
import dji.midware.data.config.P3.DeviceType;
import dji.midware.data.manager.P3.DataBase;
import dji.midware.data.packages.P3.SendPack;
import dji.midware.interfaces.DJIDataCallBack;
import dji.midware.interfaces.DJIDataSyncListener;
import dji.midware.media.DJIVideoDecoder;
import dji.midware.natives.GroudStation;
import dji.midware.util.BytesUtil;

@Keep
@EXClassNullAway
public class DataFlycSetGetVerPhone extends DataBase implements DJIDataSyncListener {
    private static DataFlycSetGetVerPhone mInstance = null;
    private VerPhoneCmdType mCmdType = VerPhoneCmdType.GET;
    private String phone = "";
    private int phoneFlag = 0;
    private int time = 0;

    public static synchronized DataFlycSetGetVerPhone getInstance() {
        DataFlycSetGetVerPhone dataFlycSetGetVerPhone;
        synchronized (DataFlycSetGetVerPhone.class) {
            if (mInstance == null) {
                mInstance = new DataFlycSetGetVerPhone();
            }
            dataFlycSetGetVerPhone = mInstance;
        }
        return dataFlycSetGetVerPhone;
    }

    @Keep
    public enum VerPhoneCmdType {
        GET(0),
        SET(1);
        
        public int data = 0;

        private VerPhoneCmdType(int i) {
            this.data = i;
        }
    }

    @Keep
    public enum FlycPhoneStatus {
        BindOk(171),
        NotBind(173),
        NeedBind(172),
        Unknown(170);
        
        int data = 0;

        private FlycPhoneStatus(int i) {
            this.data = i;
        }

        public static FlycPhoneStatus find(int b) {
            FlycPhoneStatus result = Unknown;
            for (int i = 0; i < values().length; i++) {
                if (values()[i].data == b) {
                    return values()[i];
                }
            }
            return result;
        }
    }

    public void setCmdType(VerPhoneCmdType type) {
        this.mCmdType = type;
    }

    public void setCmdTypeGet() {
        this.mCmdType = VerPhoneCmdType.GET;
    }

    public void setCmdTypeSet() {
        this.mCmdType = VerPhoneCmdType.SET;
    }

    public void setPhoneFlag(FlycPhoneStatus flag) {
        this.phoneFlag = flag.data;
    }

    public void setFlagTime(int time2) {
        this.time = time2;
    }

    public void setPhone(String ph) {
        if (!TextUtils.isEmpty(ph)) {
            this.phone = ph.replace("+", "");
        }
    }

    private boolean checkCrc() {
        byte[] trueData = new byte[12];
        System.arraycopy(this._recData, 1, trueData, 0, 12);
        return GroudStation.native_calcCrc16(trueData) == ((Short) get(13, 2, Short.class)).shortValue();
    }

    private byte[] getPhoneByte() {
        long d = 0;
        try {
            d = Long.parseLong(this.phone);
        } catch (Exception e) {
            e.printStackTrace();
        }
        byte[] res = new byte[6];
        System.arraycopy(BytesUtil.getBytes(d), 0, res, 0, res.length);
        return res;
    }

    /* access modifiers changed from: protected */
    public void doPack() {
        if (this.mCmdType == VerPhoneCmdType.GET) {
            this._sendData = new byte[1];
            this._sendData[0] = 0;
            return;
        }
        this._sendData = new byte[15];
        this._sendData[0] = 1;
        byte[] sendData = new byte[12];
        for (int i = 0; i < 12; i++) {
            sendData[i] = 0;
        }
        if (this.time == 0) {
            this.time = (int) (System.currentTimeMillis() / 1000);
        }
        System.arraycopy(BytesUtil.getBytes(this.time), 0, sendData, 0, 4);
        sendData[4] = (byte) this.phoneFlag;
        byte[] phone2 = getPhoneByte();
        System.arraycopy(phone2, 0, sendData, 5, phone2.length);
        short crc = GroudStation.native_calcCrc16(sendData);
        System.arraycopy(sendData, 0, this._sendData, 1, 12);
        System.arraycopy(BytesUtil.getBytes(crc), 0, this._sendData, 13, 2);
    }

    public FlycPhoneStatus getPhoneFlag() {
        if (checkCrc()) {
            return FlycPhoneStatus.find(((Integer) get(5, 1, Integer.class)).intValue());
        }
        return FlycPhoneStatus.Unknown;
    }

    public int getFlagTime() {
        return ((Integer) get(1, 4, Integer.class)).intValue();
    }

    public String getPhone() {
        long phone2 = ((Long) get(6, 6, Long.class)).longValue();
        if (phone2 == 0) {
            return "";
        }
        return phone2 + "";
    }

    public void start(DJIDataCallBack callBack) {
        SendPack pack = new SendPack();
        pack.senderType = DeviceType.APP.value();
        pack.receiverType = DeviceType.FLYC.value();
        pack.cmdType = DataConfig.CMDTYPE.REQUEST.value();
        pack.isNeedAck = DataConfig.NEEDACK.YES.value();
        pack.encryptType = DataConfig.EncryptType.NO.value();
        pack.cmdSet = CmdSet.FLYC.value();
        pack.cmdId = CmdIdFlyc.CmdIdType.GetSetVerPhone.value();
        pack.repeatTimes = 3;
        pack.timeOut = 5000;
        start(pack, callBack);
    }

    public void syncStart() {
        SendPack pack = new SendPack();
        pack.senderType = DeviceType.APP.value();
        pack.receiverType = DeviceType.FLYC.value();
        pack.cmdType = DataConfig.CMDTYPE.REQUEST.value();
        pack.isNeedAck = DataConfig.NEEDACK.YES.value();
        pack.encryptType = DataConfig.EncryptType.NO.value();
        pack.cmdSet = CmdSet.FLYC.value();
        pack.cmdId = CmdIdFlyc.CmdIdType.GetSetVerPhone.value();
        pack.repeatTimes = 1;
        pack.timeOut = DJIVideoDecoder.connectLosedelay;
        syncStart(pack);
    }
}
