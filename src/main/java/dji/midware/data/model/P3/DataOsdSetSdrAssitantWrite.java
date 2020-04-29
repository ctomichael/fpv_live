package dji.midware.data.model.P3;

import android.support.annotation.Keep;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.config.P3.CmdIdOsd;
import dji.midware.data.config.P3.CmdSet;
import dji.midware.data.config.P3.DataConfig;
import dji.midware.data.config.P3.DeviceType;
import dji.midware.data.manager.P3.DataBase;
import dji.midware.data.model.P3.DataOsdSetSdrAssitantRead;
import dji.midware.data.packages.P3.SendPack;
import dji.midware.interfaces.DJIDataCallBack;
import dji.midware.interfaces.DJIDataSyncListener;
import dji.midware.util.BytesUtil;

@Keep
@EXClassNullAway
public class DataOsdSetSdrAssitantWrite extends DataBase implements DJIDataSyncListener {
    private static DataOsdSetSdrAssitantWrite instance = null;
    private int address = 0;
    private DataOsdSetSdrAssitantRead.SdrCpuType cpuType = DataOsdSetSdrAssitantRead.SdrCpuType.CP_A7;
    private DataOsdSetSdrAssitantRead.SdrDataType dataType = DataOsdSetSdrAssitantRead.SdrDataType.Int_Data;
    private DataOsdSetSdrAssitantRead.SdrDeviceType deviceType = DataOsdSetSdrAssitantRead.SdrDeviceType.Sky;
    private boolean isForce23G = false;
    private boolean isForce25G = false;
    private int writeValue = 0;

    public static synchronized DataOsdSetSdrAssitantWrite getInstance() {
        DataOsdSetSdrAssitantWrite dataOsdSetSdrAssitantWrite;
        synchronized (DataOsdSetSdrAssitantWrite.class) {
            if (instance == null) {
                instance = new DataOsdSetSdrAssitantWrite();
            }
            dataOsdSetSdrAssitantWrite = instance;
        }
        return dataOsdSetSdrAssitantWrite;
    }

    public DataOsdSetSdrAssitantWrite setSdrDeviceType(DataOsdSetSdrAssitantRead.SdrDeviceType deviceType2) {
        this.deviceType = deviceType2;
        return this;
    }

    public DataOsdSetSdrAssitantWrite setSdrCpuType(DataOsdSetSdrAssitantRead.SdrCpuType cpuType2) {
        this.cpuType = cpuType2;
        return this;
    }

    public DataOsdSetSdrAssitantWrite setSdrDataType(DataOsdSetSdrAssitantRead.SdrDataType dataType2) {
        this.dataType = dataType2;
        return this;
    }

    public DataOsdSetSdrAssitantWrite setAddress(int address2) {
        this.address = address2;
        return this;
    }

    public DataOsdSetSdrAssitantWrite setWriteValue(int writeValue2) {
        this.writeValue = writeValue2;
        return this;
    }

    public DataOsdSetSdrAssitantWrite setForceFcc() {
        setSdrDeviceType(DataOsdSetSdrAssitantRead.SdrDeviceType.Sky).setSdrCpuType(DataOsdSetSdrAssitantRead.SdrCpuType.CP_A7).setSdrDataType(DataOsdSetSdrAssitantRead.SdrDataType.Byte_Data).setAddress(-65464).setWriteValue(2);
        return this;
    }

    public DataOsdSetSdrAssitantWrite setForce23G() {
        this.deviceType = DataOsdSetSdrAssitantRead.SdrDeviceType.Sky;
        this.isForce23G = true;
        return this;
    }

    public DataOsdSetSdrAssitantWrite setForce25G() {
        this.deviceType = DataOsdSetSdrAssitantRead.SdrDeviceType.Sky;
        this.isForce25G = true;
        return this;
    }

    public int getResult() {
        return ((Integer) get(0, 1, Integer.class)).intValue();
    }

    /* access modifiers changed from: protected */
    public void doPack() {
        if (this.isForce23G) {
            this._sendData = new byte[]{0, 1, 70, 0, -1, -1, -94, 3, 0, 0};
        } else if (this.isForce25G) {
            this._sendData = new byte[]{0, 1, 70, 0, -1, -1, -110, 4, 0, 0};
        } else {
            this._sendData = new byte[11];
            this._sendData[0] = (byte) this.cpuType.value();
            this._sendData[1] = (byte) this.dataType.value();
            System.arraycopy(BytesUtil.getBytes(this.address), 0, this._sendData, 2, 4);
            System.arraycopy(BytesUtil.getBytes(this.writeValue), 0, this._sendData, 6, 4);
        }
    }

    public void start(DJIDataCallBack callBack) {
        SendPack pack = new SendPack();
        pack.senderType = DeviceType.APP.value();
        if (this.deviceType == DataOsdSetSdrAssitantRead.SdrDeviceType.Sky) {
            pack.receiverType = DeviceType.OFDM.value();
        } else {
            pack.receiverType = DeviceType.OSD.value();
        }
        pack.cmdType = DataConfig.CMDTYPE.REQUEST.value();
        pack.isNeedAck = DataConfig.NEEDACK.YES.value();
        pack.encryptType = DataConfig.EncryptType.NO.value();
        pack.cmdSet = CmdSet.OSD.value();
        pack.cmdId = CmdIdOsd.CmdIdType.SetSdrAssitantWrite.value();
        start(pack, callBack);
        resetInnerFlags();
    }

    private void resetInnerFlags() {
        this.isForce23G = false;
        this.isForce25G = false;
    }
}
