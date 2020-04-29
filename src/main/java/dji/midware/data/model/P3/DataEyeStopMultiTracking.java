package dji.midware.data.model.P3;

import android.support.annotation.Keep;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.config.P3.CmdIdEYE;
import dji.midware.data.config.P3.CmdSet;
import dji.midware.data.config.P3.DataConfig;
import dji.midware.data.config.P3.DeviceType;
import dji.midware.data.manager.P3.DataBase;
import dji.midware.data.packages.P3.SendPack;
import dji.midware.interfaces.DJIDataCallBack;
import dji.midware.interfaces.DJIDataSyncListener;
import java.util.ArrayList;

@Keep
@EXClassNullAway
public class DataEyeStopMultiTracking extends DataBase implements DJIDataSyncListener {
    private static DataEyeStopMultiTracking instance = null;
    private int cmdType = 0;
    private ArrayList<Integer> indexList;
    private int numberOfTargets = 0;

    public DataEyeStopMultiTracking setCmdType(int i) {
        this.cmdType = i;
        return this;
    }

    public DataEyeStopMultiTracking setNumberOfTargets(int i) {
        this.numberOfTargets = i;
        return this;
    }

    public DataEyeStopMultiTracking setIndexList(ArrayList<Integer> indexList2) {
        this.indexList = indexList2;
        return this;
    }

    public static synchronized DataEyeStopMultiTracking getInstance() {
        DataEyeStopMultiTracking dataEyeStopMultiTracking;
        synchronized (DataEyeStopMultiTracking.class) {
            if (instance == null) {
                instance = new DataEyeStopMultiTracking();
            }
            dataEyeStopMultiTracking = instance;
        }
        return dataEyeStopMultiTracking;
    }

    /* access modifiers changed from: protected */
    public void doPack() {
        if (this.cmdType == 0 || this.cmdType == 1) {
            this._sendData = new byte[1];
            this._sendData[0] = (byte) this.cmdType;
        } else if (this.indexList != null) {
            int size = this.indexList.size() + 2;
            this._sendData[0] = (byte) this.cmdType;
            this._sendData[1] = (byte) this.numberOfTargets;
            for (int i = 0; i < this.indexList.size(); i++) {
                this._sendData[i + 2] = (byte) this.indexList.get(i).intValue();
            }
        }
    }

    public void start(DJIDataCallBack callBack) {
        SendPack pack = new SendPack();
        pack.senderType = DeviceType.APP.value();
        pack.receiverType = DeviceType.SINGLE.value();
        pack.receiverId = 7;
        pack.cmdType = DataConfig.CMDTYPE.REQUEST.value();
        pack.isNeedAck = DataConfig.NEEDACK.YES.value();
        pack.encryptType = DataConfig.EncryptType.NO.value();
        pack.cmdSet = CmdSet.EYE.value();
        pack.cmdId = CmdIdEYE.CmdIdType.StopMultiTracking.value();
        start(pack, callBack);
    }
}
