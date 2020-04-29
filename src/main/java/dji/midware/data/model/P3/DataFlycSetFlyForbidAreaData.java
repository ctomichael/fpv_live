package dji.midware.data.model.P3;

import android.support.annotation.Keep;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.config.P3.CmdIdFlyc;
import dji.midware.data.config.P3.CmdSet;
import dji.midware.data.config.P3.DataConfig;
import dji.midware.data.config.P3.DeviceType;
import dji.midware.data.forbid.DJISetFlyForbidAreaModel;
import dji.midware.data.manager.P3.DataBase;
import dji.midware.data.packages.P3.SendPack;
import dji.midware.interfaces.DJIDataCallBack;
import dji.midware.interfaces.DJIDataSyncListener;
import dji.midware.util.BytesUtil;
import java.util.ArrayList;
import java.util.List;

@Keep
@EXClassNullAway
public class DataFlycSetFlyForbidAreaData extends DataBase implements DJIDataSyncListener {
    private static DataFlycSetFlyForbidAreaData instance = null;
    private int fragNum = 0;
    private int mRepeatTime = 0;
    private List<DJISetFlyForbidAreaModel> setLimitAreamModels = new ArrayList();

    public static synchronized DataFlycSetFlyForbidAreaData getInstance() {
        DataFlycSetFlyForbidAreaData dataFlycSetFlyForbidAreaData;
        synchronized (DataFlycSetFlyForbidAreaData.class) {
            if (instance == null) {
                instance = new DataFlycSetFlyForbidAreaData();
            }
            dataFlycSetFlyForbidAreaData = instance;
        }
        return dataFlycSetFlyForbidAreaData;
    }

    public DataFlycSetFlyForbidAreaData setList(List<DJISetFlyForbidAreaModel> mSetList) {
        if (mSetList == null) {
            this.setLimitAreamModels.clear();
        } else {
            this.setLimitAreamModels.clear();
            this.setLimitAreamModels.addAll(mSetList);
        }
        return this;
    }

    public DataFlycSetFlyForbidAreaData setFragNum(int value) {
        this.fragNum = value;
        return this;
    }

    public DataFlycSetFlyForbidAreaData setRepeatTime(int time) {
        this.mRepeatTime = time;
        return this;
    }

    /* access modifiers changed from: protected */
    public void doPack() {
        for (int i = this.setLimitAreamModels.size() - 1; i >= 0; i--) {
            if (this.setLimitAreamModels.get(i) == null) {
                this.setLimitAreamModels.remove(i);
            }
        }
        int size = this.setLimitAreamModels.size();
        this._sendData = new byte[((size * 17) + 5)];
        this._sendData[0] = BytesUtil.getByte(size);
        this._sendData[1] = BytesUtil.getByte(this.fragNum);
        this._sendData[2] = 0;
        this._sendData[3] = 0;
        this._sendData[4] = 0;
        for (int i2 = 0; i2 < size; i2++) {
            BytesUtil.arraycopy(BytesUtil.getBytes(this.setLimitAreamModels.get(i2).latitude), this._sendData, (i2 * 17) + 5);
            BytesUtil.arraycopy(BytesUtil.getBytes(this.setLimitAreamModels.get(i2).longitude), this._sendData, (i2 * 17) + 5 + 4);
            BytesUtil.arraycopy(BytesUtil.getUnsignedBytes(this.setLimitAreamModels.get(i2).radius), this._sendData, (i2 * 17) + 5 + 8);
            BytesUtil.arraycopy(BytesUtil.getUnsignedBytes(this.setLimitAreamModels.get(i2).contryCode), this._sendData, (i2 * 17) + 5 + 10);
            BytesUtil.arraycopy(new byte[]{BytesUtil.getByte(this.setLimitAreamModels.get(i2).type)}, this._sendData, (i2 * 17) + 5 + 12);
            BytesUtil.arraycopy(BytesUtil.getBytes(this.setLimitAreamModels.get(i2).id), this._sendData, (i2 * 17) + 5 + 13);
        }
    }

    public void start(DJIDataCallBack callBack) {
        SendPack pack = new SendPack();
        pack.timeOut = 5000;
        if (this.mRepeatTime != 0) {
            pack.repeatTimes = this.mRepeatTime;
        }
        pack.senderType = DeviceType.APP.value();
        pack.receiverType = DeviceType.FLYC.value();
        pack.cmdType = DataConfig.CMDTYPE.REQUEST.value();
        pack.isNeedAck = DataConfig.NEEDACK.YES.value();
        pack.encryptType = DataConfig.EncryptType.NO.value();
        pack.cmdSet = CmdSet.FLYC.value();
        pack.cmdId = CmdIdFlyc.CmdIdType.SetFlyForbidAreaData.value();
        pack.data = getSendData();
        start(pack, callBack);
    }
}
