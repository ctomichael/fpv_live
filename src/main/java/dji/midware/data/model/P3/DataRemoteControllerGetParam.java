package dji.midware.data.model.P3;

import android.support.annotation.Keep;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.config.P3.CmdIdRc;
import dji.midware.data.config.P3.CmdSet;
import dji.midware.data.config.P3.DataConfig;
import dji.midware.data.config.P3.DeviceType;
import dji.midware.data.manager.P3.DataBase;
import dji.midware.data.packages.P3.SendPack;
import dji.midware.interfaces.DJIDataCallBack;
import dji.midware.interfaces.DJIDataSyncListener;

@Keep
@EXClassNullAway
public class DataRemoteControllerGetParam extends DataBase implements DJIDataSyncListener {
    private static DataRemoteControllerGetParam instance = null;
    private int type = 0;

    public static synchronized DataRemoteControllerGetParam getInstance() {
        DataRemoteControllerGetParam dataRemoteControllerGetParam;
        synchronized (DataRemoteControllerGetParam.class) {
            if (instance == null) {
                instance = new DataRemoteControllerGetParam();
            }
            dataRemoteControllerGetParam = instance;
        }
        return dataRemoteControllerGetParam;
    }

    public void start(DJIDataCallBack callBack) {
        SendPack pack = new SendPack();
        pack.senderType = DeviceType.APP.value();
        pack.receiverType = DeviceType.OSD.value();
        pack.cmdType = DataConfig.CMDTYPE.REQUEST.value();
        pack.isNeedAck = DataConfig.NEEDACK.YES.value();
        pack.encryptType = DataConfig.EncryptType.NO.value();
        pack.cmdSet = CmdSet.RC.value();
        pack.cmdId = CmdIdRc.CmdIdType.GetRCParam.value();
        start(pack, callBack);
    }

    /* access modifiers changed from: protected */
    public void doPack() {
        this._sendData = new byte[1];
        this._sendData[0] = (byte) this.type;
    }

    public DataRemoteControllerGetParam setType(ParamType paramType) {
        this.type = paramType.value;
        return this;
    }

    public int[] getChannelRange() {
        return new int[]{((Integer) get(1, 1, Integer.class)).intValue(), ((Integer) get(2, 1, Integer.class)).intValue()};
    }

    public ParamType getParamType() {
        return ParamType.find(((Integer) get(0, 1, Integer.class)).intValue());
    }

    public boolean is2point4GSupported() {
        return (((Integer) get(1, 4, Integer.class)).intValue() & 1) == 1;
    }

    public boolean is5point7GSupported() {
        return (((Integer) get(1, 4, Integer.class)).intValue() & 2) == 2;
    }

    public boolean is5point8GSupported() {
        return (((Integer) get(1, 4, Integer.class)).intValue() & 4) == 4;
    }

    public int getCountryCode() {
        return ((Integer) get(1, 4, Integer.class)).intValue();
    }

    @Keep
    public enum ParamType {
        COUNTRY_CODE(1),
        SUPPORTED_FREQUENCY_BAND(2),
        FREQUENCY_BAND_2POINT4G_RANGE(3),
        FREQUENCY_BAND_5POINT7G_RANGE(4),
        FREQUENCY_BAND_5POINT8G_RANGE(5),
        GPS(6),
        UNKNOWN(255);
        
        /* access modifiers changed from: private */
        public final int value;

        private ParamType(int value2) {
            this.value = value2;
        }

        public int value() {
            return this.value;
        }

        public boolean _equals(int b) {
            return this.value == b;
        }

        public static ParamType find(int value2) {
            for (int i = 0; i < values().length; i++) {
                if (values()[i]._equals(value2)) {
                    return values()[i];
                }
            }
            return UNKNOWN;
        }
    }
}
