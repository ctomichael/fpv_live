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

@Keep
@EXClassNullAway
public class DataCameraGetCapabilityRange extends DataBase implements DJIDataSyncListener {
    private int cmdSet;
    private int operation;
    private ParamType paramType;

    /* access modifiers changed from: protected */
    public void doPack() {
        if (this.cmdSet == CmdSet.CAMERA.value()) {
            this._sendData = new byte[3];
            this._sendData[0] = (byte) this.cmdSet;
            this._sendData[1] = (byte) this.operation;
            this._sendData[2] = (byte) this.paramType.paramId;
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
        pack.cmdId = CmdIdCamera.CmdIdType.GetCapabilityRange.value();
        pack.data = getSendData();
        start(pack, callBack);
    }

    public DataCameraGetCapabilityRange setCmdSet(int cmdSet2) {
        this.cmdSet = cmdSet2;
        return this;
    }

    public DataCameraGetCapabilityRange setOperation(int operation2) {
        this.operation = operation2;
        return this;
    }

    public DataCameraGetCapabilityRange setParamId(ParamType paramType2) {
        this.paramType = paramType2;
        return this;
    }

    public int[] getEIRange() {
        if (getCmdSet() != CmdSet.CAMERA.value() || getOperation() != 0 || getParamId() != ParamType.EIRange.paramId) {
            return null;
        }
        int length = ((Integer) get(3, 1, Integer.class)).intValue() / 2;
        int[] result = new int[length];
        for (int i = 0; i < length; i++) {
            result[i] = ((Integer) get((i * 2) + 4, 2, Integer.class)).intValue();
        }
        return result;
    }

    public int getNativeEI() {
        if (getCmdSet() == CmdSet.CAMERA.value() && getOperation() == 0 && getParamId() == ParamType.NativeEI.paramId) {
            return ((Integer) get(4, 2, Integer.class)).intValue();
        }
        return -1;
    }

    public boolean isSupportNDFilter() {
        if (getCmdSet() != CmdSet.CAMERA.value() || getOperation() != 0 || getParamId() != ParamType.LensInfo.paramId) {
            return false;
        }
        if (((Integer) get(4, 2, Integer.class)).intValue() > 0) {
            return true;
        }
        return false;
    }

    public boolean isSupportMechShutter() {
        if (getCmdSet() != CmdSet.CAMERA.value() || getOperation() != 0 || getParamId() != ParamType.LensInfo.paramId) {
            return false;
        }
        if (((Integer) get(6, 2, Integer.class)).intValue() == 1) {
            return true;
        }
        return false;
    }

    public int getCmdSet() {
        return ((Integer) get(0, 1, Integer.class)).intValue();
    }

    public int getOperation() {
        return ((Integer) get(1, 1, Integer.class)).intValue();
    }

    private int getParamId() {
        return ((Integer) get(2, 1, Integer.class)).intValue();
    }

    @Keep
    public enum ParamType {
        EIRange(1),
        NativeEI(2),
        LensInfo(3);
        
        /* access modifiers changed from: private */
        public int paramId;

        private ParamType(int paramId2) {
            this.paramId = paramId2;
        }
    }
}
