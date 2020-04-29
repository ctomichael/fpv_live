package dji.midware.data.model.P3;

import android.support.annotation.Keep;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.config.P3.CmdIdGimbal;
import dji.midware.data.config.P3.CmdSet;
import dji.midware.data.config.P3.DataConfig;
import dji.midware.data.config.P3.DeviceType;
import dji.midware.data.manager.P3.DataBase;
import dji.midware.data.packages.P3.SendPack;
import dji.midware.interfaces.DJIDataCallBack;
import dji.midware.interfaces.DJIDataSyncListener;
import dji.midware.util.BytesUtil;
import java.util.ArrayList;
import java.util.Iterator;

@Keep
@EXClassNullAway
public class DataGimbalSetTimelapseParams extends DataBase implements DJIDataSyncListener {
    public static final int MAX_ROTA_POSITION = 5;
    private static final String TAG = "DataGimbalSetTimelapseParams";
    private byte mInfoByte;
    ArrayList<TimelapseRoadObject> roadList = new ArrayList<>();

    @Keep
    private class TimelapseRoadObject {
        public long duration = 0;
        public int pitch = 0;
        public int roll = 0;
        public int yaw = 0;

        public TimelapseRoadObject(long d, int r, int p, int y) {
            this.duration = d;
            this.roll = r;
            this.pitch = p;
            this.yaw = y;
        }
    }

    public DataGimbalSetTimelapseParams setMode(int mode) {
        this.mInfoByte = (byte) (this.mInfoByte | (mode & 1));
        return this;
    }

    public DataGimbalSetTimelapseParams setIsTripod(int value) {
        this.mInfoByte = (byte) (this.mInfoByte | ((value & 1) << 1));
        return this;
    }

    public DataGimbalSetTimelapseParams setStartOrStop(int value) {
        this.mInfoByte = (byte) (this.mInfoByte | ((value & 1) << 2));
        return this;
    }

    public boolean addRoadPosition(long d, int r, int p, int y) {
        if (this.roadList.size() >= 5) {
            return false;
        }
        this.roadList.add(new TimelapseRoadObject(d, r, p, y));
        return true;
    }

    public void start(DJIDataCallBack callBack) {
        SendPack pack = new SendPack();
        pack.senderType = DeviceType.APP.value();
        pack.receiverType = DeviceType.GIMBAL.value();
        pack.cmdType = DataConfig.CMDTYPE.REQUEST.value();
        pack.isNeedAck = DataConfig.NEEDACK.YES.value();
        pack.encryptType = DataConfig.EncryptType.NO.value();
        pack.cmdSet = CmdSet.GIMBAL.value();
        pack.cmdId = CmdIdGimbal.CmdIdType.SetTimelapseParams.value();
        pack.repeatTimes = 5;
        pack.timeOut = 200;
        start(pack, callBack);
    }

    /* access modifiers changed from: protected */
    public void doPack() {
        int sendLength;
        if ((this.mInfoByte & 1) == 1) {
            sendLength = 2;
        } else {
            sendLength = (this.roadList.size() * 10) + 2;
        }
        this._sendData = new byte[sendLength];
        this._sendData[0] = this.mInfoByte;
        this._sendData[1] = (byte) this.roadList.size();
        if ((this.mInfoByte & 1) == 0) {
            int i = 0;
            Iterator<TimelapseRoadObject> it2 = this.roadList.iterator();
            while (it2.hasNext()) {
                TimelapseRoadObject o = it2.next();
                System.arraycopy(BytesUtil.getBytes(o.duration), 0, this._sendData, (i * 10) + 2, 4);
                System.arraycopy(BytesUtil.getBytes(o.roll), 0, this._sendData, (i * 10) + 6, 2);
                System.arraycopy(BytesUtil.getBytes(o.pitch), 0, this._sendData, (i * 10) + 8, 2);
                System.arraycopy(BytesUtil.getBytes(o.yaw), 0, this._sendData, (i * 10) + 10, 2);
                i++;
            }
        }
        this.roadList.clear();
        this.mInfoByte = 0;
    }
}
