package dji.midware.data.model.common;

import android.util.Log;
import dji.component.accountcenter.IMemberProtocol;
import dji.fieldAnnotation.EXClassNullAway;
import dji.log.DJILogHelper;
import dji.midware.data.config.P3.CmdIdCommon;
import dji.midware.data.config.P3.CmdSet;
import dji.midware.data.config.P3.DataConfig;
import dji.midware.data.config.P3.DeviceType;
import dji.midware.data.config.P3.ProductType;
import dji.midware.data.manager.P3.DJIProductManager;
import dji.midware.data.manager.P3.DataBase;
import dji.midware.data.model.P3.DataBatteryActiveStatus;
import dji.midware.data.model.P3.DataCameraActiveStatus;
import dji.midware.data.model.P3.DataEagleActiveStatus;
import dji.midware.data.model.P3.DataFlycActiveStatus;
import dji.midware.data.model.P3.DataGimbalActiveStatus;
import dji.midware.data.model.P3.DataOsdActiveStatus;
import dji.midware.data.model.common.DataAbstractGetPushActiveStatus;
import dji.midware.data.packages.P3.SendPack;
import dji.midware.interfaces.DJIDataCallBack;
import dji.midware.interfaces.DJIDataSyncListener;

@EXClassNullAway
public class DataCommonActiveGetVer extends DataBase implements DJIDataSyncListener {
    private DeviceType deviceType;
    private DataAbstractGetPushActiveStatus.DJIActiveVersion version;

    public DataCommonActiveGetVer setDevice(DeviceType deviceType2) {
        this.deviceType = deviceType2;
        return this;
    }

    public DataAbstractGetPushActiveStatus.DJIActiveVersion getVer() {
        return this.version;
    }

    public void setRecData(byte[] data) {
        super.setRecData(data);
        this.version = DataAbstractGetPushActiveStatus.DJIActiveVersion.find(((Integer) get(0, 1, Integer.class)).intValue());
        DJILogHelper.getInstance().LOGD("", "active get version[" + this.version + ";" + this.deviceType + IMemberProtocol.STRING_SEPERATOR_RIGHT, false, true);
        switch (this.deviceType) {
            case CAMERA:
                DataCameraActiveStatus.getInstance().setVersion(this.version);
                return;
            case FLYC:
                DataFlycActiveStatus.getInstance().setVersion(this.version);
                return;
            case BATTERY:
                DataBatteryActiveStatus.getInstance().setVersion(this.version);
                return;
            case OSD:
                DataOsdActiveStatus.getInstance().setVersion(this.version);
                return;
            case GIMBAL:
                DataGimbalActiveStatus.getInstance().setVersion(this.version);
                return;
            case DM368:
                DataEagleActiveStatus.getInstance().setVersion(this.version);
                return;
            default:
                return;
        }
    }

    /* access modifiers changed from: protected */
    public void doPack() {
        this._sendData = new byte[1];
        this._sendData[0] = (byte) DataAbstractGetPushActiveStatus.TYPE.GetVer.value();
    }

    public void start(DJIDataCallBack callBack) {
        SendPack pack = new SendPack();
        pack.senderType = DeviceType.APP.value();
        ProductType productType = DJIProductManager.getInstance().getType();
        if ((productType == ProductType.PM820 || productType == ProductType.PM820PRO) && this.deviceType == DeviceType.BATTERY) {
            pack.receiverId = 0;
            Log.e("pm820", "**into active battery getversion id 0");
        }
        pack.receiverType = this.deviceType.value();
        pack.cmdType = DataConfig.CMDTYPE.REQUEST.value();
        pack.isNeedAck = DataConfig.NEEDACK.YES.value();
        pack.encryptType = DataConfig.EncryptType.NO.value();
        pack.cmdSet = CmdSet.COMMON.value();
        pack.cmdId = CmdIdCommon.CmdIdType.ActiveStatus.value();
        pack.timeOut = 1000;
        pack.repeatTimes = 2;
        start(pack, callBack);
    }
}
