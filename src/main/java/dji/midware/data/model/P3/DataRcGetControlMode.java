package dji.midware.data.model.P3;

import android.support.annotation.Keep;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.config.P3.CmdIdRc;
import dji.midware.data.config.P3.CmdSet;
import dji.midware.data.config.P3.DataConfig;
import dji.midware.data.config.P3.DeviceType;
import dji.midware.data.manager.P3.DataBase;
import dji.midware.data.model.P3.DataRcSetControlMode;
import dji.midware.data.packages.P3.SendPack;
import dji.midware.interfaces.DJIDataCallBack;
import dji.midware.interfaces.DJIDataSyncListener;
import java.util.ArrayList;

@Keep
@EXClassNullAway
public class DataRcGetControlMode extends DataBase implements DJIDataSyncListener {
    private static DataRcGetControlMode instance = null;
    private ArrayList<DataRcSetControlMode.ChannelCustomModel> arrayList;
    private DataRcSetControlMode.ControlMode controlMode;
    private DeviceType mReceiverType = DeviceType.OSD;

    public static synchronized DataRcGetControlMode getInstance() {
        DataRcGetControlMode dataRcGetControlMode;
        synchronized (DataRcGetControlMode.class) {
            if (instance == null) {
                instance = new DataRcGetControlMode();
            }
            dataRcGetControlMode = instance;
        }
        return dataRcGetControlMode;
    }

    public DataRcSetControlMode.ControlMode getControlType() {
        this.controlMode = DataRcSetControlMode.ControlMode.find(((Integer) get(0, 1, Integer.class)).intValue());
        return this.controlMode;
    }

    public DataRcGetControlMode setReceiverType(DeviceType deviceType) {
        this.mReceiverType = deviceType;
        return this;
    }

    public ArrayList<DataRcSetControlMode.ChannelCustomModel> getChannels() {
        if (this.arrayList == null) {
            this.arrayList = new ArrayList<>(4);
        }
        for (int i = 0; i < 4; i++) {
            int value = ((Integer) get(i + 1, 1, Integer.class)).intValue();
            DataRcSetControlMode.ChannelCustomModel channelCustomModel = new DataRcSetControlMode.ChannelCustomModel();
            channelCustomModel.direction = value >> 7;
            channelCustomModel.function = value & -129;
            if (this.arrayList.size() > i) {
                this.arrayList.set(i, channelCustomModel);
            } else {
                this.arrayList.add(i, channelCustomModel);
            }
        }
        return this.arrayList;
    }

    /* access modifiers changed from: protected */
    public void doPack() {
    }

    public void start(DJIDataCallBack callBack) {
        SendPack pack = new SendPack();
        pack.senderType = DeviceType.APP.value();
        pack.receiverType = this.mReceiverType != null ? this.mReceiverType.value() : DeviceType.OSD.value();
        pack.cmdType = DataConfig.CMDTYPE.REQUEST.value();
        pack.isNeedAck = DataConfig.NEEDACK.YES.value();
        pack.encryptType = DataConfig.EncryptType.NO.value();
        pack.cmdSet = CmdSet.RC.value();
        pack.cmdId = CmdIdRc.CmdIdType.GetControlMode.value();
        pack.data = getSendData();
        start(pack, callBack);
    }
}
