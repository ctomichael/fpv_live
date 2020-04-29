package dji.midware.data.model.P3;

import dji.midware.data.config.P3.CmdIdCamera;
import dji.midware.data.config.P3.CmdSet;
import dji.midware.data.config.P3.DataConfig;
import dji.midware.data.config.P3.DeviceType;
import dji.midware.data.manager.P3.DataBase;
import dji.midware.data.packages.P3.SendPack;
import dji.midware.interfaces.DJIDataCallBack;
import dji.midware.util.BytesUtil;

public class DataCameraParamsOption extends DataBase {
    private static final int CMD_COUNT = 1;
    private static final int CMD_OPTION_BYTE_LENGTH = 2;
    private static final int DATA_BYTE_LENGTH = 1;
    private static final int GETTER_VALUE = 0;
    private static final int SETTER_VALUE = 1;
    private ParamsCMD cmdOption;
    private byte data;
    private boolean isGetter = false;
    private boolean isSetter = false;

    public DataCameraParamsOption setCmdOption(ParamsCMD cmdOption2) {
        this.cmdOption = cmdOption2;
        return this;
    }

    public DataCameraParamsOption isSetter(boolean isSetter2) {
        this.isSetter = isSetter2;
        this.isGetter = !isSetter2;
        return this;
    }

    public DataCameraParamsOption isGetter(boolean isGetter2) {
        this.isGetter = isGetter2;
        this.isSetter = !isGetter2;
        return this;
    }

    public void setPhotoSize(PreviewPhotoSize photoSize) {
        this.data = (byte) photoSize.value;
    }

    public boolean isScreenNailEnabled() {
        if (ParamsCMD.find(((Integer) get(3, 2, Integer.class)).intValue()) != ParamsCMD.SCREEN_NAIL_HD_SWITCH) {
            return false;
        }
        if (((Integer) get(5, 1, Integer.class)).intValue() == 1) {
            return true;
        }
        return false;
    }

    /* access modifiers changed from: protected */
    public void doPack() {
        if (this.isSetter) {
            this._sendData = new byte[6];
            this._sendData[0] = 1;
            this._sendData[1] = 1;
            System.arraycopy(BytesUtil.getBytes(this.cmdOption.value), 0, this._sendData, 2, 2);
            this._sendData[4] = 1;
            this._sendData[5] = this.data;
            return;
        }
        this._sendData = new byte[4];
        this._sendData[0] = 0;
        this._sendData[1] = 1;
        System.arraycopy(BytesUtil.getBytes(this.cmdOption.value), 0, this._sendData, 2, 2);
    }

    public void start(DJIDataCallBack callBack) {
        SendPack pack = new SendPack();
        pack.senderType = DeviceType.APP.value();
        pack.receiverType = DeviceType.CAMERA.value();
        pack.cmdType = DataConfig.CMDTYPE.REQUEST.value();
        pack.isNeedAck = DataConfig.NEEDACK.YES.value();
        pack.encryptType = DataConfig.EncryptType.NO.value();
        pack.cmdSet = CmdSet.CAMERA.value();
        pack.cmdId = CmdIdCamera.CmdIdType.ParamsOption.value();
        pack.repeatTimes = 3;
        start(pack, callBack);
    }

    public enum ParamsCMD {
        SCREEN_NAIL_HD_SWITCH(19),
        UNKNOWN(255);
        
        /* access modifiers changed from: private */
        public final int value;

        private ParamsCMD(int value2) {
            this.value = value2;
        }

        public int value() {
            return this.value;
        }

        private boolean _equals(int b) {
            return this.value == b;
        }

        public static ParamsCMD find(int b) {
            ParamsCMD result = UNKNOWN;
            for (int i = 0; i < values().length; i++) {
                if (values()[i]._equals(b)) {
                    return values()[i];
                }
            }
            return result;
        }
    }

    public enum PreviewPhotoSize {
        DEFAULT(0),
        X_SMALL(1),
        SMALL(2),
        MEDIUM(3),
        LARGE(4),
        X_LARGE(5),
        UNKNOWN(255);
        
        /* access modifiers changed from: private */
        public final int value;

        private PreviewPhotoSize(int value2) {
            this.value = value2;
        }

        public int value() {
            return this.value;
        }

        private boolean _equals(int b) {
            return this.value == b;
        }

        public static PreviewPhotoSize find(int b) {
            PreviewPhotoSize result = UNKNOWN;
            for (int i = 0; i < values().length; i++) {
                if (values()[i]._equals(b)) {
                    return values()[i];
                }
            }
            return result;
        }
    }
}
