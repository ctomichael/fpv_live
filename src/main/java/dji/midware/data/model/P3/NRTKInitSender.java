package dji.midware.data.model.P3;

import com.drew.metadata.exif.makernotes.CanonMakernoteDirectory;
import dji.midware.data.config.P3.CmdIdRTK;
import dji.midware.data.config.P3.CmdSet;
import dji.midware.data.config.P3.DataConfig;
import dji.midware.data.config.P3.DeviceType;
import dji.midware.data.manager.P3.DataBase;
import dji.midware.data.packages.P3.SendPack;
import dji.midware.interfaces.DJIDataCallBack;
import dji.midware.interfaces.DJIDataSyncListener;
import dji.midware.util.BytesUtil;

public class NRTKInitSender extends DataBase implements DJIDataSyncListener {
    private String deviceID;
    private String deviceType;
    private String key;
    private String secret;

    public void setQXKey(String key2) {
        this.key = key2;
    }

    public void setQXSecret(String secret2) {
        this.secret = secret2;
    }

    public void setQXDeviceID(String deviceID2) {
        this.deviceID = deviceID2;
    }

    public void setQXDeviceType(String deviceType2) {
        this.deviceType = deviceType2;
    }

    /* access modifiers changed from: protected */
    public void doPack() {
        this._sendData = new byte[CanonMakernoteDirectory.TAG_VRD_OFFSET];
        byte[] keyByte = BytesUtil.getBytesUTF8(this.key);
        System.arraycopy(keyByte, 0, this._sendData, 0, keyByte.length);
        byte[] secretByte = BytesUtil.getBytesUTF8(this.secret);
        System.arraycopy(secretByte, 0, this._sendData, 16, secretByte.length);
        byte[] deviceIDByte = BytesUtil.getBytesUTF8(this.deviceID);
        System.arraycopy(deviceIDByte, 0, this._sendData, 144, deviceIDByte.length);
        byte[] deviceTypeByte = BytesUtil.getBytesUTF8(this.deviceType);
        System.arraycopy(deviceTypeByte, 0, this._sendData, 176, deviceTypeByte.length);
    }

    public void start(DJIDataCallBack callBack) {
        SendPack pack = new SendPack();
        pack.senderType = DeviceType.APP.value();
        pack.receiverType = DeviceType.OSD.value();
        pack.cmdType = DataConfig.CMDTYPE.REQUEST.value();
        pack.isNeedAck = DataConfig.NEEDACK.YES.value();
        pack.encryptType = DataConfig.EncryptType.NO.value();
        pack.cmdSet = CmdSet.RTK.value();
        pack.cmdId = CmdIdRTK.CmdIdType.InitNRTK.value();
        start(pack, callBack);
    }
}
