package dji.midware.data.model.P3;

import android.support.annotation.Keep;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.config.P3.CmdIdFlyc;
import dji.midware.data.config.P3.CmdSet;
import dji.midware.data.config.P3.DataConfig;
import dji.midware.data.config.P3.DeviceType;
import dji.midware.data.manager.P3.DataBase;
import dji.midware.data.packages.P3.SendPack;
import dji.midware.interfaces.DJIDataCallBack;
import dji.midware.interfaces.DJIDataSyncListener;
import dji.midware.util.BytesUtil;

@Keep
@EXClassNullAway
public class DataFlycStartFollowMeWithInfo extends DataBase implements DJIDataSyncListener {
    private static DataFlycStartFollowMeWithInfo instance = null;
    private FOLLOWMODE followMode = FOLLOWMODE.Relative_mode;
    private short mAltitude = 0;
    private int mAppSource = 1;
    private short mHeading = 0;
    private int mSensitivity = 0;
    private double mUserLatitude;
    private double mUserLongitude;
    private YAWMODE yawMode = YAWMODE.Use_Remote_Controll;

    public static synchronized DataFlycStartFollowMeWithInfo getInstance() {
        DataFlycStartFollowMeWithInfo dataFlycStartFollowMeWithInfo;
        synchronized (DataFlycStartFollowMeWithInfo.class) {
            if (instance == null) {
                instance = new DataFlycStartFollowMeWithInfo();
            }
            dataFlycStartFollowMeWithInfo = instance;
        }
        return dataFlycStartFollowMeWithInfo;
    }

    public int getResult() {
        return ((Integer) get(0, 1, Integer.class)).intValue();
    }

    public DataFlycStartFollowMeWithInfo setFollowMode(FOLLOWMODE followMode2) {
        this.followMode = followMode2;
        return this;
    }

    public DataFlycStartFollowMeWithInfo setYawMode(YAWMODE yawMode2) {
        this.yawMode = yawMode2;
        return this;
    }

    public DataFlycStartFollowMeWithInfo setUserLatitude(double userLatitude) {
        this.mUserLatitude = userLatitude;
        return this;
    }

    public DataFlycStartFollowMeWithInfo setUserLongitude(double userLongitude) {
        this.mUserLongitude = userLongitude;
        return this;
    }

    public DataFlycStartFollowMeWithInfo setAltitude(short attitude) {
        this.mAltitude = attitude;
        return this;
    }

    public DataFlycStartFollowMeWithInfo setHeading(short heading) {
        this.mHeading = heading;
        return this;
    }

    public DataFlycStartFollowMeWithInfo setSensitivity(int sensitivity) {
        this.mSensitivity = sensitivity;
        return this;
    }

    @Keep
    public enum FOLLOWMODE {
        Relative_mode(0),
        Route_mode(1),
        Smart_mode(2);
        
        private int data;

        private FOLLOWMODE(int _data) {
            this.data = _data;
        }

        public int value() {
            return this.data;
        }

        public boolean _equals(int b) {
            return this.data == b;
        }

        public static FOLLOWMODE find(int b) {
            FOLLOWMODE result = Relative_mode;
            for (int i = 0; i < values().length; i++) {
                if (values()[i]._equals(b)) {
                    return values()[i];
                }
            }
            return result;
        }
    }

    @Keep
    public enum YAWMODE {
        Point_To_Target(0),
        Use_Remote_Controll(1);
        
        private int data;

        private YAWMODE(int _data) {
            this.data = _data;
        }

        public int value() {
            return this.data;
        }

        public boolean _equals(int b) {
            return this.data == b;
        }

        public static YAWMODE find(int b) {
            YAWMODE result = Point_To_Target;
            for (int i = 0; i < values().length; i++) {
                if (values()[i]._equals(b)) {
                    return values()[i];
                }
            }
            return result;
        }
    }

    public void start(DJIDataCallBack callBack) {
        SendPack pack = new SendPack();
        pack.senderType = DeviceType.APP.value();
        pack.receiverType = DeviceType.FLYC.value();
        pack.cmdType = DataConfig.CMDTYPE.REQUEST.value();
        pack.isNeedAck = DataConfig.NEEDACK.YES.value();
        pack.encryptType = DataConfig.EncryptType.NO.value();
        pack.cmdSet = CmdSet.FLYC.value();
        pack.cmdId = CmdIdFlyc.CmdIdType.StartFollowMeWithInfo.value();
        pack.timeOut = 3000;
        super.start(pack, callBack);
    }

    /* access modifiers changed from: protected */
    public void doPack() {
        this._sendData = new byte[23];
        this._sendData[0] = (byte) this.followMode.value();
        this._sendData[1] = (byte) this.yawMode.value();
        System.arraycopy(BytesUtil.getBytes(this.mUserLatitude), 0, this._sendData, 2, 8);
        System.arraycopy(BytesUtil.getBytes(this.mUserLongitude), 0, this._sendData, 10, 8);
        System.arraycopy(BytesUtil.getBytes(this.mAltitude), 0, this._sendData, 18, 2);
        System.arraycopy(BytesUtil.getBytes(this.mHeading), 0, this._sendData, 20, 2);
        this._sendData[22] = (byte) ((this.mSensitivity & 63) | (this.mAppSource << 6));
    }
}
