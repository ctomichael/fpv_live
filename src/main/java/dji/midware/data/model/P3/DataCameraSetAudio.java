package dji.midware.data.model.P3;

import android.support.annotation.Keep;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.config.P3.Ccode;
import dji.midware.data.config.P3.CmdIdCamera;
import dji.midware.data.config.P3.CmdSet;
import dji.midware.data.config.P3.DataConfig;
import dji.midware.data.config.P3.DeviceType;
import dji.midware.data.manager.P3.DataBase;
import dji.midware.data.packages.P3.SendPack;
import dji.midware.interfaces.DJIDataCallBack;
import dji.midware.interfaces.DJIDataSyncListener;

@Keep
@EXClassNullAway
public class DataCameraSetAudio extends DataBase implements DJIDataSyncListener {
    DJIDataCallBack callBack;
    DataCameraGetAudio getter;
    byte isEnable = -1;
    int src = -1;
    byte toFirstDataRate = -1;
    byte toSecondDataRate = -1;
    int type = -1;

    public DataCameraSetAudio setEnable(boolean enable) {
        if (enable) {
            this.isEnable = 1;
        } else {
            this.isEnable = 0;
        }
        return this;
    }

    /* access modifiers changed from: protected */
    public void doPack() {
        this._sendData = new byte[2];
        this._sendData[0] = (byte) ((this.isEnable << 7) | (this.toSecondDataRate << 1) | (this.toFirstDataRate << 0));
        this._sendData[1] = (byte) ((this.type << 2) | this.src);
    }

    public void start(DJIDataCallBack callBack2) {
        this.callBack = callBack2;
        this.getter = new DataCameraGetAudio();
        if (this.receiverID != -1) {
            this.getter.setReceiverId(this.receiverID);
        }
        this.getter.start(new DJIDataCallBack() {
            /* class dji.midware.data.model.P3.DataCameraSetAudio.AnonymousClass1 */

            public void onSuccess(Object model) {
                if (DataCameraSetAudio.this.isEnable == -1) {
                    if (DataCameraSetAudio.this.getter.isEnable()) {
                        DataCameraSetAudio.this.isEnable = 1;
                    } else {
                        DataCameraSetAudio.this.isEnable = 0;
                    }
                }
                if (DataCameraSetAudio.this.toSecondDataRate == -1) {
                    if (DataCameraSetAudio.this.getter.toSecondDataRate()) {
                        DataCameraSetAudio.this.toSecondDataRate = 1;
                    } else {
                        DataCameraSetAudio.this.toSecondDataRate = 0;
                    }
                }
                if (DataCameraSetAudio.this.toFirstDataRate == -1) {
                    if (DataCameraSetAudio.this.getter.toFirstDataRate()) {
                        DataCameraSetAudio.this.toFirstDataRate = 1;
                    } else {
                        DataCameraSetAudio.this.toFirstDataRate = 0;
                    }
                }
                if (DataCameraSetAudio.this.type == -1) {
                    DataCameraSetAudio.this.type = DataCameraSetAudio.this.getter.getType();
                }
                if (DataCameraSetAudio.this.src == -1) {
                    DataCameraSetAudio.this.src = DataCameraSetAudio.this.getter.getSrc();
                }
                DataCameraSetAudio.this.sendPack();
            }

            public void onFailure(Ccode ccode) {
                if (DataCameraSetAudio.this.isEnable == -1) {
                    DataCameraSetAudio.this.isEnable = 0;
                }
                if (DataCameraSetAudio.this.toSecondDataRate == -1) {
                    DataCameraSetAudio.this.toSecondDataRate = 0;
                }
                if (DataCameraSetAudio.this.toFirstDataRate == -1) {
                    DataCameraSetAudio.this.toFirstDataRate = 0;
                }
                if (DataCameraSetAudio.this.type == -1) {
                    DataCameraSetAudio.this.type = 0;
                }
                if (DataCameraSetAudio.this.src == -1) {
                    DataCameraSetAudio.this.src = 0;
                }
                DataCameraSetAudio.this.sendPack();
            }
        });
    }

    /* access modifiers changed from: private */
    public void sendPack() {
        SendPack pack = new SendPack();
        pack.senderType = DeviceType.APP.value();
        pack.receiverType = DeviceType.CAMERA.value();
        pack.cmdType = DataConfig.CMDTYPE.REQUEST.value();
        pack.isNeedAck = DataConfig.NEEDACK.YES.value();
        pack.encryptType = DataConfig.EncryptType.NO.value();
        pack.cmdSet = CmdSet.CAMERA.value();
        pack.cmdId = CmdIdCamera.CmdIdType.SetAudioParma.value();
        pack.data = getSendData();
        start(pack, this.callBack);
    }
}
