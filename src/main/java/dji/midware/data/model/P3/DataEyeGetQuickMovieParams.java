package dji.midware.data.model.P3;

import android.support.annotation.Keep;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.config.P3.CmdIdEYE;
import dji.midware.data.config.P3.CmdSet;
import dji.midware.data.config.P3.DataConfig;
import dji.midware.data.config.P3.DeviceType;
import dji.midware.data.manager.P3.DataBase;
import dji.midware.data.model.P3.DataEyeSetQuickMovieParams;
import dji.midware.data.packages.P3.SendPack;
import dji.midware.interfaces.DJIDataCallBack;
import dji.midware.interfaces.DJIDataSyncListener;
import java.util.ArrayList;

@Keep
@EXClassNullAway
public class DataEyeGetQuickMovieParams extends DataBase implements DJIDataSyncListener {
    private static DataEyeGetQuickMovieParams instance = null;
    private ArrayList<Integer> requestedIndexs;

    public static synchronized DataEyeGetQuickMovieParams getInstance() {
        DataEyeGetQuickMovieParams dataEyeGetQuickMovieParams;
        synchronized (DataEyeGetQuickMovieParams.class) {
            if (instance == null) {
                instance = new DataEyeGetQuickMovieParams();
            }
            dataEyeGetQuickMovieParams = instance;
        }
        return dataEyeGetQuickMovieParams;
    }

    public DataEyeGetQuickMovieParams setRequestedIndex(ArrayList<Integer> indexs) {
        this.requestedIndexs = indexs;
        return this;
    }

    public ArrayList<DataEyeSetQuickMovieParams.ActionParam> getQuickMovieParams() {
        Number data;
        if (this._recData == null) {
            return null;
        }
        ArrayList<DataEyeSetQuickMovieParams.ActionParam> params = new ArrayList<>();
        int offset = 0;
        while (offset < this._recData.length) {
            int offset2 = offset + 1;
            int actionType = ((Integer) get(offset, 1, Integer.class)).intValue();
            int offset3 = offset2 + 1;
            int size = ((Integer) get(offset2, 1, Integer.class)).intValue();
            if (actionType == DataEyeSetQuickMovieParams.ActionParamIndex.ACTION_TYPE.getValue() || actionType == DataEyeSetQuickMovieParams.ActionParamIndex.IS_START.getValue() || actionType == DataEyeSetQuickMovieParams.ActionParamIndex.PROGRESS.getValue() || actionType == DataEyeSetQuickMovieParams.ActionParamIndex.END_OF_PARAMS.getValue()) {
                data = get(offset3, size, Integer.class);
            } else {
                data = get(offset3, size, Float.class);
            }
            offset = offset3 + size;
            params.add(new DataEyeSetQuickMovieParams.ActionParam(DataEyeSetQuickMovieParams.ActionParamIndex.find(actionType), data));
        }
        return params;
    }

    /* access modifiers changed from: protected */
    public void doPack() {
        if (this.requestedIndexs == null) {
            this._sendData = new byte[1];
            return;
        }
        this._sendData = new byte[this.requestedIndexs.size()];
        for (int i = 0; i < this.requestedIndexs.size(); i++) {
            this._sendData[i] = (byte) this.requestedIndexs.get(i).intValue();
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
        pack.cmdId = CmdIdEYE.CmdIdType.GetQuickMovieParams.value();
        start(pack, callBack);
    }
}
