package dji.midware.data.model.P3;

import android.support.annotation.Keep;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.config.P3.CmdIdCamera;
import dji.midware.data.config.P3.CmdSet;
import dji.midware.data.config.P3.DataConfig;
import dji.midware.data.config.P3.DeviceType;
import dji.midware.data.manager.P3.DataBase;
import dji.midware.data.packages.P3.SendPack;
import dji.midware.interfaces.DJIDataCallBack;
import dji.midware.interfaces.DJIDataSyncListener;
import dji.midware.media.DJIVideoDecoder;
import kotlin.jvm.internal.ByteCompanionObject;

@Keep
@EXClassNullAway
public class DataCameraSetVideoCaption extends DataBase implements DJIDataSyncListener {
    private static DataCameraSetVideoCaption mInstance = null;
    private byte mValue = 0;

    public static synchronized DataCameraSetVideoCaption getInstance() {
        DataCameraSetVideoCaption dataCameraSetVideoCaption;
        synchronized (DataCameraSetVideoCaption.class) {
            if (mInstance == null) {
                mInstance = new DataCameraSetVideoCaption();
            }
            dataCameraSetVideoCaption = mInstance;
        }
        return dataCameraSetVideoCaption;
    }

    public DataCameraSetVideoCaption reset() {
        this.mValue = 0;
        return this;
    }

    public DataCameraSetVideoCaption setGenerateVideoCaption(boolean enable) {
        if (enable) {
            this.mValue = (byte) (this.mValue | 128);
        } else {
            this.mValue = (byte) (this.mValue & ByteCompanionObject.MAX_VALUE);
        }
        return this;
    }

    public void setShowCameraEVParam(boolean enable) {
        if (enable) {
            this.mValue = (byte) (this.mValue | 1);
        } else {
            this.mValue = (byte) (this.mValue & -2);
        }
    }

    /* access modifiers changed from: protected */
    public void doPack() {
        this._sendData = new byte[1];
        this._sendData[0] = this.mValue;
    }

    public void start(DJIDataCallBack callBack) {
        SendPack pack = new SendPack();
        pack.senderType = DeviceType.APP.value();
        pack.receiverType = DeviceType.CAMERA.value();
        pack.cmdType = DataConfig.CMDTYPE.REQUEST.value();
        pack.isNeedAck = DataConfig.NEEDACK.YES.value();
        pack.encryptType = DataConfig.EncryptType.NO.value();
        pack.cmdSet = CmdSet.CAMERA.value();
        pack.cmdId = CmdIdCamera.CmdIdType.SetVideoCaption.value();
        pack.data = getSendData();
        pack.timeOut = DJIVideoDecoder.connectLosedelay;
        start(pack, callBack);
    }
}
