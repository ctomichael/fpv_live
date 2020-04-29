package dji.midware.data.model.P3;

import android.support.annotation.Keep;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.config.P3.CmdIdEYE;
import dji.midware.data.config.P3.CmdSet;
import dji.midware.data.config.P3.DataConfig;
import dji.midware.data.config.P3.DeviceType;
import dji.midware.data.manager.P3.DataBase;
import dji.midware.data.model.P3.DataSingleVisualParam;
import dji.midware.data.packages.P3.SendPack;
import dji.midware.interfaces.DJIDataCallBack;
import dji.midware.interfaces.DJIDataSyncListener;
import dji.midware.util.BytesUtil;

@Keep
@EXClassNullAway
public class DataSingleSetTrackSelect extends DataBase implements DJIDataSyncListener {
    private boolean isHotTracking = false;
    private float mCenterX = 0.0f;
    private float mCenterY = 0.0f;
    private int mException = 0;
    private float mHeight = 0.0f;
    private DataSingleVisualParam.TrackingMode mMode = DataSingleVisualParam.TrackingMode.HEADLESS_FOLLOW;
    private short mSessionId = 0;
    private float mWidth = 0.0f;

    public DataSingleSetTrackSelect setMode(DataSingleVisualParam.TrackingMode mode) {
        this.mMode = mode;
        return this;
    }

    public DataSingleSetTrackSelect setCenterX(float centerX, float centerY, float width, float height) {
        this.mCenterX = centerX;
        this.mCenterY = centerY;
        this.mWidth = width;
        this.mHeight = height;
        return this;
    }

    public DataSingleSetTrackSelect setSessionId(short sessionId) {
        this.mSessionId = sessionId;
        return this;
    }

    public DataSingleSetTrackSelect setIsHotTracking(boolean hotTracking) {
        this.isHotTracking = hotTracking;
        return this;
    }

    /* access modifiers changed from: protected */
    public void doPack() {
        this._sendData = new byte[21];
        this._sendData[0] = (byte) this.mMode.value();
        System.arraycopy(BytesUtil.getBytes(this.mCenterX), 0, this._sendData, 1, 4);
        System.arraycopy(BytesUtil.getBytes(this.mCenterY), 0, this._sendData, 5, 4);
        System.arraycopy(BytesUtil.getBytes(this.mWidth), 0, this._sendData, 9, 4);
        System.arraycopy(BytesUtil.getBytes(this.mHeight), 0, this._sendData, 13, 4);
        this._sendData[17] = (byte) this.mException;
        System.arraycopy(BytesUtil.getBytes(this.mSessionId), 0, this._sendData, 18, 2);
        if (this.isHotTracking) {
            this._sendData[20] = 4;
        } else {
            this._sendData[20] = 0;
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
        pack.cmdId = CmdIdEYE.CmdIdType.SetTrackSelect.value();
        pack.timeOut = 3000;
        pack.repeatTimes = 1;
        start(pack, callBack);
    }
}
