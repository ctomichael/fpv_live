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
public class DataFlycStartHotPointMissionWithInfo extends DataBase implements DJIDataSyncListener {
    private static DataFlycStartHotPointMissionWithInfo instance = null;
    private double altitude;
    private float angleSpeed;
    private CAMERA_DIR cameraDir;
    private double latitude;
    private double longitude;
    private double radious;
    private ROTATION_DIR rotationDir;
    private TO_START_POINT_MODE toStartPointMode;
    private byte version = 0;

    public static synchronized DataFlycStartHotPointMissionWithInfo getInstance() {
        DataFlycStartHotPointMissionWithInfo dataFlycStartHotPointMissionWithInfo;
        synchronized (DataFlycStartHotPointMissionWithInfo.class) {
            if (instance == null) {
                instance = new DataFlycStartHotPointMissionWithInfo();
            }
            dataFlycStartHotPointMissionWithInfo = instance;
        }
        return dataFlycStartHotPointMissionWithInfo;
    }

    public DataFlycStartHotPointMissionWithInfo setLatitude(double latitude2) {
        this.latitude = latitude2;
        return this;
    }

    public void printResult() {
    }

    public int getResult() {
        return ((Integer) get(0, 1, Integer.class)).intValue();
    }

    public float getMaxRadius() {
        return ((Float) get(1, 4, Float.class)).floatValue();
    }

    public DataFlycStartHotPointMissionWithInfo setLongitude(double longitude2) {
        this.longitude = longitude2;
        return this;
    }

    public DataFlycStartHotPointMissionWithInfo setAltitude(double altitude2) {
        this.altitude = altitude2;
        return this;
    }

    public DataFlycStartHotPointMissionWithInfo setRadious(double radious2) {
        this.radious = radious2;
        return this;
    }

    public DataFlycStartHotPointMissionWithInfo setVelocity(float angleSpeed2) {
        this.angleSpeed = angleSpeed2;
        return this;
    }

    public DataFlycStartHotPointMissionWithInfo setRotationDir(ROTATION_DIR rotationDir2) {
        this.rotationDir = rotationDir2;
        return this;
    }

    public DataFlycStartHotPointMissionWithInfo setToStartPointMode(TO_START_POINT_MODE toStartPointMode2) {
        this.toStartPointMode = toStartPointMode2;
        return this;
    }

    public DataFlycStartHotPointMissionWithInfo setCameraDir(CAMERA_DIR cameraDir2) {
        this.cameraDir = cameraDir2;
        return this;
    }

    @Keep
    public enum ROTATION_DIR {
        Anti_Clockwise(0),
        Clockwise(1);
        
        private int data;

        private ROTATION_DIR(int _data) {
            this.data = _data;
        }

        public int value() {
            return this.data;
        }

        public boolean _equals(int b) {
            return this.data == b;
        }

        public static ROTATION_DIR find(int b) {
            ROTATION_DIR result = Anti_Clockwise;
            for (int i = 0; i < values().length; i++) {
                if (values()[i]._equals(b)) {
                    return values()[i];
                }
            }
            return result;
        }
    }

    @Keep
    public enum TO_START_POINT_MODE {
        North(0),
        South(1),
        West(2),
        Ease(3),
        Initi(4);
        
        private int data;

        private TO_START_POINT_MODE(int _data) {
            this.data = _data;
        }

        public int value() {
            return this.data;
        }

        public boolean _equals(int b) {
            return this.data == b;
        }

        public static TO_START_POINT_MODE find(int b) {
            TO_START_POINT_MODE result = Initi;
            for (int i = 0; i < values().length; i++) {
                if (values()[i]._equals(b)) {
                    return values()[i];
                }
            }
            return result;
        }
    }

    @Keep
    public enum CAMERA_DIR {
        SAME_DIR_AS_SPEED(0),
        Forwards_Hot_Point(1),
        Backforwards_Hot_Point(2),
        Remote_Control(3),
        INVERSE_DIR_AS_SPEED(5);
        
        private int data;

        private CAMERA_DIR(int _data) {
            this.data = _data;
        }

        public int value() {
            return this.data;
        }

        public boolean _equals(int b) {
            return this.data == b;
        }

        public static CAMERA_DIR find(int b) {
            CAMERA_DIR result = SAME_DIR_AS_SPEED;
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
        pack.cmdId = CmdIdFlyc.CmdIdType.StartHotPointMissionWithInfo.value();
        pack.timeOut = 3000;
        super.start(pack, callBack);
    }

    /* access modifiers changed from: protected */
    public void doPack() {
        this._sendData = new byte[51];
        this._sendData[0] = this.version;
        System.arraycopy(BytesUtil.getBytes(this.latitude), 0, this._sendData, 1, 8);
        System.arraycopy(BytesUtil.getBytes(this.longitude), 0, this._sendData, 9, 8);
        System.arraycopy(BytesUtil.getBytes(this.altitude), 0, this._sendData, 17, 8);
        System.arraycopy(BytesUtil.getBytes(this.radious), 0, this._sendData, 25, 8);
        System.arraycopy(BytesUtil.getBytes(this.angleSpeed), 0, this._sendData, 33, 4);
        this._sendData[37] = (byte) this.rotationDir.value();
        this._sendData[38] = (byte) this.toStartPointMode.value();
        this._sendData[39] = (byte) this.cameraDir.value();
    }
}
