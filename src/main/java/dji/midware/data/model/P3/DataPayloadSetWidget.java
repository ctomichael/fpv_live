package dji.midware.data.model.P3;

import android.support.annotation.Keep;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.config.P3.CmdIdPayloadSDK;
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
public class DataPayloadSetWidget extends DataBase implements DJIDataSyncListener {
    private int widgetIndex;
    private int widgetType;
    private int widgetValue;

    public DataPayloadSetWidget setWidgetType(int type) {
        this.widgetType = type;
        return this;
    }

    public DataPayloadSetWidget setWidgetIndex(int index) {
        this.widgetIndex = index;
        return this;
    }

    public DataPayloadSetWidget setWidgetValue(int value) {
        this.widgetValue = value;
        return this;
    }

    /* access modifiers changed from: protected */
    public void doPack() {
        this._sendData = new byte[6];
        this._sendData[0] = (byte) this.widgetType;
        this._sendData[1] = (byte) this.widgetIndex;
        System.arraycopy(BytesUtil.getBytes(this.widgetValue), 0, this._sendData, 2, 4);
    }

    public void start(DJIDataCallBack callBack) {
        SendPack pack = new SendPack();
        pack.senderType = DeviceType.APP.value();
        pack.receiverType = DeviceType.GIMBAL.value();
        pack.receiverId = 0;
        pack.cmdType = DataConfig.CMDTYPE.REQUEST.value();
        pack.isNeedAck = DataConfig.NEEDACK.YES.value();
        pack.encryptType = DataConfig.EncryptType.NO.value();
        pack.cmdSet = CmdSet.PayloadSDK.value();
        pack.cmdId = CmdIdPayloadSDK.CmdIdType.setValueForPayLoadWidget.value();
        pack.timeOut = 1000;
        pack.repeatTimes = 2;
        start(pack, callBack);
    }
}
