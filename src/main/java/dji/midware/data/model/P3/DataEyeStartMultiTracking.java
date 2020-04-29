package dji.midware.data.model.P3;

import android.support.annotation.Keep;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.config.P3.CmdIdEYE;
import dji.midware.data.config.P3.CmdSet;
import dji.midware.data.config.P3.DataConfig;
import dji.midware.data.config.P3.DeviceType;
import dji.midware.data.manager.P3.DataBase;
import dji.midware.data.model.P3.DataSingleVisualParam;
import dji.midware.data.packages.P3.SendPack;
import dji.midware.interfaces.DJIDataCallBack;
import dji.midware.interfaces.DJIDataSyncListener;
import java.util.ArrayList;

@Keep
@EXClassNullAway
public class DataEyeStartMultiTracking extends DataBase implements DJIDataSyncListener {
    private static DataEyeStartMultiTracking instance = null;
    private ArrayList<Integer> indexList;
    private int numberOfTracking;
    private DataSingleVisualParam.TrackingMode trackingMode = DataSingleVisualParam.TrackingMode.HEADLESS_FOLLOW;

    public static synchronized DataEyeStartMultiTracking getInstance() {
        DataEyeStartMultiTracking dataEyeStartMultiTracking;
        synchronized (DataEyeStartMultiTracking.class) {
            if (instance == null) {
                instance = new DataEyeStartMultiTracking();
            }
            dataEyeStartMultiTracking = instance;
        }
        return dataEyeStartMultiTracking;
    }

    public DataEyeStartMultiTracking setTrackingMode(DataSingleVisualParam.TrackingMode trackingMode2) {
        this.trackingMode = trackingMode2;
        return this;
    }

    public DataEyeStartMultiTracking setNumberOfTracking(int i) {
        this.numberOfTracking = i;
        return this;
    }

    public DataEyeStartMultiTracking setIndexList(ArrayList<Integer> indexList2) {
        this.indexList = indexList2;
        return this;
    }

    /* access modifiers changed from: protected */
    public void doPack() {
        if (this.indexList != null) {
            this._sendData = new byte[(this.indexList.size() + 2)];
            this._sendData[0] = (byte) this.trackingMode.value();
            this._sendData[1] = (byte) this.numberOfTracking;
            for (int i = 0; i < this.indexList.size(); i++) {
                this._sendData[i + 2] = (byte) this.indexList.get(i).intValue();
            }
            return;
        }
        this._sendData = new byte[1];
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
        pack.cmdId = CmdIdEYE.CmdIdType.StartMultiTracking.value();
        start(pack, callBack);
    }
}
