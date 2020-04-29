package dji.midware.data.model.P3;

import android.support.annotation.Keep;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.config.P3.CmdIdCamera;
import dji.midware.data.config.P3.CmdSet;
import dji.midware.data.config.P3.DataConfig;
import dji.midware.data.config.P3.DeviceType;
import dji.midware.data.model.base.DJICameraDataBase;
import dji.midware.data.packages.P3.SendPack;
import dji.midware.interfaces.DJIDataCallBack;
import dji.midware.interfaces.DJIDataSyncListener;

@Keep
@EXClassNullAway
public class DataCameraSetSaveOriginalPhoto extends DJICameraDataBase implements DJIDataSyncListener {
    private int hyperLapseFileType = 0;
    private boolean isHyperLapseEnable = true;
    private boolean isPanoEnable = true;
    private int panoFileType = 0;

    public void setPanoEnable(boolean isEnable) {
        this.isPanoEnable = isEnable;
    }

    public void setHyperLapseEnable(boolean isEnable) {
        this.isHyperLapseEnable = isEnable;
    }

    public void setPanoFileType(int panoFileType2) {
        this.panoFileType = panoFileType2;
    }

    public void setHyperLapseFileType(int hyperLapseFileType2) {
        this.hyperLapseFileType = hyperLapseFileType2;
    }

    /* access modifiers changed from: protected */
    public void doPack() {
        this._sendData = new byte[2];
        if (this.isPanoEnable) {
            byte[] bArr = this._sendData;
            bArr[0] = (byte) (bArr[0] | 1);
        }
        byte[] bArr2 = this._sendData;
        bArr2[0] = (byte) (bArr2[0] | (this.panoFileType << 1));
        if (this.isHyperLapseEnable) {
            byte[] bArr3 = this._sendData;
            bArr3[1] = (byte) (bArr3[1] | 1);
        }
        byte[] bArr4 = this._sendData;
        bArr4[1] = (byte) (bArr4[1] | (this.hyperLapseFileType << 1));
    }

    public void start(DJIDataCallBack callBack) {
        SendPack pack = new SendPack();
        pack.senderType = DeviceType.APP.value();
        pack.receiverType = DeviceType.CAMERA.value();
        pack.cmdType = DataConfig.CMDTYPE.REQUEST.value();
        pack.isNeedAck = DataConfig.NEEDACK.YES.value();
        pack.encryptType = DataConfig.EncryptType.NO.value();
        pack.cmdSet = CmdSet.CAMERA.value();
        pack.cmdId = CmdIdCamera.CmdIdType.SetSaveOrigPhoto.value();
        pack.data = getSendData();
        start(pack, callBack);
    }
}
