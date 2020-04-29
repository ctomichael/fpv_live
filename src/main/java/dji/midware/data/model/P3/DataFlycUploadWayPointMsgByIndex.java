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
import java.util.ArrayList;

@Keep
@EXClassNullAway
public class DataFlycUploadWayPointMsgByIndex extends DataBase implements DJIDataSyncListener {
    private static DataFlycUploadWayPointMsgByIndex instance = null;
    private ArrayList<ACTION> actionList;
    private int actionNum;
    private int actionRepeat;
    private int actionTimeTimit = 999;
    private float altitude;
    private int cameraActionInterval;
    private int cameraActionType;
    private float dampingDis = 0.0f;
    private boolean hasAction;
    private boolean hasSpeed;
    private int index;
    private double latitude;
    private double longitude;
    private ArrayList<Integer> paramsList;
    private short tgtPitch = 0;
    private short tgtYaw;
    private TURNMODE turnMode;
    private int wpSpeed;

    public static synchronized DataFlycUploadWayPointMsgByIndex getInstance() {
        DataFlycUploadWayPointMsgByIndex dataFlycUploadWayPointMsgByIndex;
        synchronized (DataFlycUploadWayPointMsgByIndex.class) {
            if (instance == null) {
                instance = new DataFlycUploadWayPointMsgByIndex();
            }
            dataFlycUploadWayPointMsgByIndex = instance;
        }
        return dataFlycUploadWayPointMsgByIndex;
    }

    public int getResult() {
        return ((Integer) get(0, 1, Integer.class)).intValue();
    }

    public DataFlycUploadWayPointMsgByIndex isHasSpeed(boolean hasSpeed2) {
        this.hasSpeed = hasSpeed2;
        return this;
    }

    public DataFlycUploadWayPointMsgByIndex setWpSpeed(int wpSpeed2) {
        this.wpSpeed = wpSpeed2;
        return this;
    }

    public DataFlycUploadWayPointMsgByIndex setCameraActionType(int cameraActionType2) {
        this.cameraActionType = cameraActionType2;
        return this;
    }

    public DataFlycUploadWayPointMsgByIndex setCameraActionInterval(int cameraActionInterval2) {
        this.cameraActionInterval = cameraActionInterval2;
        return this;
    }

    public DataFlycUploadWayPointMsgByIndex setIndex(int index2) {
        this.index = index2;
        return this;
    }

    public DataFlycUploadWayPointMsgByIndex setLatitude(double latitude2) {
        this.latitude = latitude2;
        return this;
    }

    public DataFlycUploadWayPointMsgByIndex setLongtitude(double longitude2) {
        this.longitude = longitude2;
        return this;
    }

    public DataFlycUploadWayPointMsgByIndex setAltitude(float altitude2) {
        this.altitude = altitude2;
        return this;
    }

    public DataFlycUploadWayPointMsgByIndex setDampingDis(float dampingDis2) {
        this.dampingDis = dampingDis2;
        return this;
    }

    public DataFlycUploadWayPointMsgByIndex setTgtYaw(short tgtYaw2) {
        this.tgtYaw = tgtYaw2;
        return this;
    }

    public DataFlycUploadWayPointMsgByIndex setTgtPitch(short tgtPitch2) {
        this.tgtPitch = tgtPitch2;
        return this;
    }

    public DataFlycUploadWayPointMsgByIndex setActionTimeTimit(short actionTimeTimit2) {
        this.actionTimeTimit = actionTimeTimit2;
        return this;
    }

    public DataFlycUploadWayPointMsgByIndex setTurnMode(TURNMODE turnMode2) {
        this.turnMode = turnMode2;
        return this;
    }

    public DataFlycUploadWayPointMsgByIndex setHasAction(boolean hasAction2) {
        this.hasAction = hasAction2;
        return this;
    }

    public DataFlycUploadWayPointMsgByIndex setActionNum(int actionNum2) {
        this.actionNum = actionNum2;
        return this;
    }

    public DataFlycUploadWayPointMsgByIndex setActionRepeat(int actionRepeat2) {
        this.actionRepeat = actionRepeat2;
        return this;
    }

    public DataFlycUploadWayPointMsgByIndex setActionList(ArrayList<ACTION> actionList2) {
        this.actionList = actionList2;
        return this;
    }

    public DataFlycUploadWayPointMsgByIndex setParamsList(ArrayList<Integer> paramsList2) {
        this.paramsList = paramsList2;
        return this;
    }

    @Keep
    public enum TURNMODE {
        CLOCKWISE(0),
        ANTI_CLOSEWISE(1);
        
        private int data;

        private TURNMODE(int data2) {
            this.data = data2;
        }

        public int value() {
            return this.data;
        }

        public boolean _equals(int b) {
            return this.data == b;
        }

        public static TURNMODE find(int b) {
            TURNMODE result = CLOCKWISE;
            for (int i = 0; i < values().length; i++) {
                if (values()[i]._equals(b)) {
                    return values()[i];
                }
            }
            return result;
        }
    }

    @Keep
    public enum ACTION {
        WP_ACTION_STAY(0),
        WP_ACTION_SIMPLE_SHOT(1),
        WP_ACTION_VIDEO_START(2),
        WP_ACTION_VIDEO_STOP(3),
        WP_ACTION_CRAFT_YAW(4),
        WP_ACTION_GIMBAL_PITCH(5),
        WP_ACTION_CRAFT_YAW_DELTA(6),
        WP_ACTION_CAMERA_ZOOM(7),
        WP_ACTION_CAMERA_FOCUS(8),
        WP_ACTION_PICTURES_GROUP(11),
        WP_ACTION_RC_LOCK(12);
        
        private int data;

        private ACTION(int data2) {
            this.data = data2;
        }

        public int value() {
            return this.data;
        }

        public boolean _equals(int b) {
            return this.data == b;
        }

        public static ACTION find(int b) {
            ACTION result = WP_ACTION_STAY;
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
        pack.cmdId = CmdIdFlyc.CmdIdType.UploadWayPointMsgByIndex.value();
        pack.data = getSendData();
        pack.timeOut = 3000;
        start(pack, callBack);
    }

    public byte[] getSendDataForRecord() {
        return super.getSendData();
    }

    /* access modifiers changed from: protected */
    public void doPack() {
        int i;
        int i2 = 1;
        this._sendData = new byte[90];
        this._sendData[0] = BytesUtil.getByte(this.index);
        System.arraycopy(BytesUtil.getBytes(this.latitude), 0, this._sendData, 1, 8);
        System.arraycopy(BytesUtil.getBytes(this.longitude), 0, this._sendData, 9, 8);
        System.arraycopy(BytesUtil.getBytes(this.altitude), 0, this._sendData, 17, 4);
        System.arraycopy(BytesUtil.getBytes(this.dampingDis), 0, this._sendData, 21, 4);
        System.arraycopy(BytesUtil.getBytes(this.tgtYaw), 0, this._sendData, 25, 2);
        System.arraycopy(BytesUtil.getBytes(this.tgtPitch), 0, this._sendData, 27, 2);
        this._sendData[29] = (byte) this.turnMode.value();
        byte[] bArr = this._sendData;
        if (this.hasSpeed) {
            i = 1;
        } else {
            i = 0;
        }
        bArr[30] = (byte) i;
        System.arraycopy(BytesUtil.getBytes(this.wpSpeed), 0, this._sendData, 31, 2);
        this._sendData[33] = (byte) this.cameraActionType;
        System.arraycopy(BytesUtil.getBytes(this.cameraActionInterval), 0, this._sendData, 34, 2);
        byte[] bArr2 = this._sendData;
        if (!this.hasAction) {
            i2 = 0;
        }
        bArr2[38] = (byte) i2;
        System.arraycopy(BytesUtil.getBytes(this.actionTimeTimit), 0, this._sendData, 39, 2);
        if (this.hasAction) {
            this._sendData[41] = (byte) ((this.actionRepeat << 4) + this.actionNum);
            for (int i3 = 0; i3 < 16; i3++) {
                if (i3 < this.actionNum) {
                    this._sendData[i3 + 42] = (byte) this.actionList.get(i3).value();
                    System.arraycopy(BytesUtil.getBytes(this.paramsList.get(i3).intValue()), 0, this._sendData, (i3 * 2) + 58, 2);
                } else {
                    this._sendData[i3 + 42] = 0;
                    System.arraycopy(BytesUtil.getBytes(0), 0, this._sendData, (i3 * 2) + 58, 2);
                }
            }
            return;
        }
        this._sendData[41] = 0;
        for (int i4 = 0; i4 < 16; i4++) {
            this._sendData[i4 + 42] = 0;
            System.arraycopy(BytesUtil.getBytes(0), 0, this._sendData, (i4 * 2) + 58, 2);
        }
    }
}
