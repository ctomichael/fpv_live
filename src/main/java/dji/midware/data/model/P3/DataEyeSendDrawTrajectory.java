package dji.midware.data.model.P3;

import android.support.annotation.Keep;
import com.drew.metadata.exif.makernotes.LeicaMakernoteDirectory;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.config.P3.CmdIdEYE;
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
public class DataEyeSendDrawTrajectory extends DataBase implements DJIDataSyncListener {
    private static DataEyeSendDrawTrajectory instance = null;
    private byte mCount = 0;
    private byte mFragment = 0;
    private byte mSequence = 0;
    private short[] mTrajectoryLocation = null;
    private boolean mbLastFragment = false;

    public static synchronized DataEyeSendDrawTrajectory getInstance() {
        DataEyeSendDrawTrajectory dataEyeSendDrawTrajectory;
        synchronized (DataEyeSendDrawTrajectory.class) {
            if (instance == null) {
                instance = new DataEyeSendDrawTrajectory();
            }
            dataEyeSendDrawTrajectory = instance;
        }
        return dataEyeSendDrawTrajectory;
    }

    public DataEyeSendDrawTrajectory setSequence(byte sequence) {
        this.mSequence = sequence;
        return this;
    }

    public DataEyeSendDrawTrajectory setFragment(byte fragment) {
        this.mFragment = fragment;
        return this;
    }

    public DataEyeSendDrawTrajectory setIsLastFragment(boolean last) {
        this.mbLastFragment = last;
        return this;
    }

    public DataEyeSendDrawTrajectory setCount(byte count) {
        this.mCount = count;
        return this;
    }

    public DataEyeSendDrawTrajectory setTrajectory(short[] trajectory) {
        this.mTrajectoryLocation = trajectory;
        return this;
    }

    /* access modifiers changed from: protected */
    public void doPack() {
        byte b = 1;
        this._sendData = new byte[((this.mCount * 2 * 2) + 4)];
        this._sendData[0] = this.mSequence;
        this._sendData[1] = this.mFragment;
        byte[] bArr = this._sendData;
        byte b2 = bArr[2];
        if (!this.mbLastFragment) {
            b = 0;
        }
        bArr[2] = (byte) (b | b2);
        this._sendData[3] = this.mCount;
        for (int i = 0; i < this.mCount; i++) {
            System.arraycopy(BytesUtil.getBytes(this.mTrajectoryLocation[i * 2]), 0, this._sendData, (i * 4) + 4, 2);
            System.arraycopy(BytesUtil.getBytes(this.mTrajectoryLocation[(i * 2) + 1]), 0, this._sendData, (i * 4) + 4 + 2, 2);
        }
    }

    public void start(DJIDataCallBack callBack) {
        SendPack pack = new SendPack();
        pack.senderType = DeviceType.APP.value();
        pack.receiverType = DeviceType.SINGLE.value();
        pack.receiverId = 7;
        pack.cmdType = DataConfig.CMDTYPE.REQUEST.value();
        pack.isNeedAck = DataConfig.NEEDACK.YES.value();
        pack.encryptType = DataConfig.EncryptType.NO.value();
        pack.cmdSet = CmdSet.EYE.value();
        pack.cmdId = CmdIdEYE.CmdIdType.SendDrawTrajectory.value();
        pack.timeOut = LeicaMakernoteDirectory.TAG_CAMERA_TEMPERATURE;
        pack.repeatTimes = 4;
        start(pack, callBack);
    }
}
