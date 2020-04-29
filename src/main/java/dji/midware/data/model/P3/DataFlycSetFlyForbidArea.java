package dji.midware.data.model.P3;

import android.support.annotation.Keep;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.config.P3.CmdIdFlyc;
import dji.midware.data.config.P3.CmdSet;
import dji.midware.data.config.P3.DataConfig;
import dji.midware.data.config.P3.DeviceType;
import dji.midware.data.forbid.DJISetFlightLimitAreaModel;
import dji.midware.data.manager.P3.DataBase;
import dji.midware.data.packages.P3.SendPack;
import dji.midware.interfaces.DJIDataCallBack;
import dji.midware.interfaces.DJIDataSyncListener;
import dji.midware.util.BytesUtil;
import java.util.ArrayList;

@Keep
@EXClassNullAway
public class DataFlycSetFlyForbidArea extends DataBase implements DJIDataSyncListener {
    private static DataFlycSetFlyForbidArea instance = null;
    private ArrayList<DJISetFlightLimitAreaModel> setLimitAreamModels = new ArrayList<>();

    public static synchronized DataFlycSetFlyForbidArea getInstance() {
        DataFlycSetFlyForbidArea dataFlycSetFlyForbidArea;
        synchronized (DataFlycSetFlyForbidArea.class) {
            if (instance == null) {
                instance = new DataFlycSetFlyForbidArea();
            }
            dataFlycSetFlyForbidArea = instance;
        }
        return dataFlycSetFlyForbidArea;
    }

    public DataFlycSetFlyForbidArea setList(ArrayList<DJISetFlightLimitAreaModel> mSetList) {
        this.setLimitAreamModels.clear();
        this.setLimitAreamModels.addAll(mSetList);
        return this;
    }

    /* access modifiers changed from: protected */
    public void doPack() {
        int size = this.setLimitAreamModels.size();
        this._sendData = new byte[((size * 17) + 5)];
        this._sendData[0] = BytesUtil.getByte(size);
        BytesUtil.arraycopy(BytesUtil.getBytes(0), this._sendData, 1);
        for (int i = 0; i < size; i++) {
            BytesUtil.arraycopy(BytesUtil.getBytes(this.setLimitAreamModels.get(i).latitude), this._sendData, (i * 17) + 5);
            BytesUtil.arraycopy(BytesUtil.getBytes(this.setLimitAreamModels.get(i).longitude), this._sendData, (i * 17) + 5 + 4);
            BytesUtil.arraycopy(BytesUtil.getUnsignedBytes(this.setLimitAreamModels.get(i).radius), this._sendData, (i * 17) + 5 + 8);
            BytesUtil.arraycopy(BytesUtil.getUnsignedBytes(this.setLimitAreamModels.get(i).contryCode), this._sendData, (i * 17) + 5 + 10);
            BytesUtil.arraycopy(new byte[]{BytesUtil.getByte(this.setLimitAreamModels.get(i).type)}, this._sendData, (i * 17) + 5 + 12);
            BytesUtil.arraycopy(BytesUtil.getBytes(this.setLimitAreamModels.get(i).revers), this._sendData, (i * 17) + 5 + 13);
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
        pack.cmdId = CmdIdFlyc.CmdIdType.SetFlyForbidArea.value();
        pack.data = getSendData();
        start(pack, callBack);
    }
}
