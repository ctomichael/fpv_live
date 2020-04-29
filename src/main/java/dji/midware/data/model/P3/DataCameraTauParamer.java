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

@Keep
@EXClassNullAway
public abstract class DataCameraTauParamer extends DataBase implements DJIDataSyncListener {
    protected ParamCmd mParamCmd = ParamCmd.OTHER;
    protected byte[] mParams = null;
    protected boolean mbGet = false;

    public DataCameraTauParamer setOpt(boolean bGet) {
        this.mbGet = bGet;
        return this;
    }

    /* access modifiers changed from: protected */
    public void doPack() {
        int length;
        if (this.mParams != null) {
            length = this.mParams.length;
        } else {
            length = 0;
        }
        this._sendData = new byte[(length + 1)];
        byte[] bArr = this._sendData;
        bArr[0] = (byte) (((byte) (this.mbGet ? 0 : 128)) | bArr[0]);
        byte[] bArr2 = this._sendData;
        bArr2[0] = (byte) (bArr2[0] | this.mParamCmd.value());
        if (this.mParams != null && this.mParams.length > 0) {
            System.arraycopy(this.mParams, 0, this._sendData, 1, this.mParams.length);
        }
    }

    public void start(DJIDataCallBack callBack) {
        SendPack pack = new SendPack();
        pack.senderType = DeviceType.APP.value();
        pack.receiverType = DeviceType.CAMERA.value();
        pack.cmdType = DataConfig.CMDTYPE.REQUEST.value();
        pack.isNeedAck = DataConfig.NEEDACK.YES.value();
        pack.encryptType = DataConfig.EncryptType.NO.value();
        pack.cmdSet = CmdSet.CAMERA.value();
        pack.cmdId = CmdIdCamera.CmdIdType.TauParam.value();
        pack.timeOut = DJIVideoDecoder.connectLosedelay;
        start(pack, callBack);
    }

    @Keep
    public enum ParamCmd {
        PICTURE_ROTATE(1),
        INTEREST_REGION(2),
        DIGITAL_INC(3),
        SCENE_CONTRAST(4),
        SCENE_OPTIMIZATE(5),
        AGC(6),
        REGION_THERMOMETRIC(7),
        BRIGHTNESS(8),
        ISOTHERM_ENABLE(9),
        ISOTHERM_UNIT(10),
        ISOTHERM_LOWER(11),
        ISOTHERM_MIDDLE(12),
        ISOTHERM_UPPER(13),
        THERMOMETRIC_TYPE(14),
        GAIN_MODE(15),
        TEMP_UNIT_IN_SPOT(16),
        FFC_MODE(17),
        TRIGGER_FFC(18),
        EXTER_PARAM_TYPE(21),
        EXTER_PARAMS(22),
        AREA_AXIS(23),
        OTHER(100);
        
        private int data;

        private ParamCmd(int _data) {
            this.data = _data;
        }

        public int value() {
            return this.data;
        }

        public boolean _equals(int b) {
            return this.data == b;
        }

        public static ParamCmd find(int b) {
            ParamCmd result = OTHER;
            for (int i = 0; i < values().length; i++) {
                if (values()[i]._equals(b)) {
                    return values()[i];
                }
            }
            return result;
        }
    }
}
