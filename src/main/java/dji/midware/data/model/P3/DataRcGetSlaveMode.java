package dji.midware.data.model.P3;

import android.support.annotation.Keep;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.config.P3.CmdIdRc;
import dji.midware.data.config.P3.CmdSet;
import dji.midware.data.config.P3.DataConfig;
import dji.midware.data.config.P3.DeviceType;
import dji.midware.data.manager.P3.DataBase;
import dji.midware.data.model.P3.DataRcSetSlaveMode;
import dji.midware.data.packages.P3.SendPack;
import dji.midware.interfaces.DJIDataCallBack;
import dji.midware.interfaces.DJIDataSyncListener;
import java.util.ArrayList;

@Keep
@EXClassNullAway
public class DataRcGetSlaveMode extends DataBase implements DJIDataSyncListener {
    private static DataRcGetSlaveMode instance = null;
    private ArrayList<DataRcSetSlaveMode.SlaveCustomModel> arrayList;
    private DataRcSetSlaveMode.ControlMode controlMode;

    public static synchronized DataRcGetSlaveMode getInstance() {
        DataRcGetSlaveMode dataRcGetSlaveMode;
        synchronized (DataRcGetSlaveMode.class) {
            if (instance == null) {
                instance = new DataRcGetSlaveMode();
            }
            dataRcGetSlaveMode = instance;
        }
        return dataRcGetSlaveMode;
    }

    public DataRcSetSlaveMode.ControlMode getControlType() {
        this.controlMode = DataRcSetSlaveMode.ControlMode.find(((Integer) get(0, 1, Integer.class)).intValue());
        return this.controlMode;
    }

    public ArrayList<DataRcSetSlaveMode.SlaveCustomModel> getChannels() {
        if (this.arrayList == null) {
            this.arrayList = new ArrayList<>(4);
        }
        for (int i = 0; i < 4; i++) {
            int value = ((Integer) get(i + 1, 1, Integer.class)).intValue();
            DataRcSetSlaveMode.SlaveCustomModel channelCustomModel = new DataRcSetSlaveMode.SlaveCustomModel();
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
        pack.receiverType = DeviceType.OSD.value();
        pack.cmdType = DataConfig.CMDTYPE.REQUEST.value();
        pack.isNeedAck = DataConfig.NEEDACK.YES.value();
        pack.encryptType = DataConfig.EncryptType.NO.value();
        pack.cmdSet = CmdSet.RC.value();
        pack.cmdId = CmdIdRc.CmdIdType.GetSlaveMode.value();
        pack.data = getSendData();
        start(pack, callBack);
    }
}
