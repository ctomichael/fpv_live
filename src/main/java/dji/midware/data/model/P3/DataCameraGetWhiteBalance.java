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
public class DataCameraGetWhiteBalance extends DataBase implements DJIDataSyncListener {
    private static DataCameraGetWhiteBalance instance = null;

    public static synchronized DataCameraGetWhiteBalance getInstance() {
        DataCameraGetWhiteBalance dataCameraGetWhiteBalance;
        synchronized (DataCameraGetWhiteBalance.class) {
            if (instance == null) {
                instance = new DataCameraGetWhiteBalance();
            }
            dataCameraGetWhiteBalance = instance;
        }
        return dataCameraGetWhiteBalance;
    }

    public Type getType() {
        return Type.find(((Short) get(0, 1, Short.class)).shortValue());
    }

    public ColorType getColorType() {
        return ColorType.find(((Short) get(1, 1, Short.class)).shortValue());
    }

    /* access modifiers changed from: protected */
    public void doPack() {
    }

    public void start(DJIDataCallBack callBack) {
        SendPack pack = new SendPack();
        pack.senderType = DeviceType.APP.value();
        pack.receiverType = DeviceType.CAMERA.value();
        pack.cmdType = DataConfig.CMDTYPE.REQUEST.value();
        pack.isNeedAck = DataConfig.NEEDACK.YES.value();
        pack.encryptType = DataConfig.EncryptType.NO.value();
        pack.cmdSet = CmdSet.CAMERA.value();
        pack.cmdId = CmdIdCamera.CmdIdType.GetWhiteBalance.value();
        pack.data = getSendData();
        start(pack, callBack);
    }

    @Keep
    public enum Type {
        AUTO(0),
        FineDay(1),
        Cloudy(2),
        Water(3),
        Filament(4),
        Fluorescent(5),
        Color(6),
        OTHER(100);
        
        private int data;

        private Type(int _data) {
            this.data = _data;
        }

        public int value() {
            return this.data;
        }

        public boolean _equals(int b) {
            return this.data == b;
        }

        public static Type find(int b) {
            Type result = OTHER;
            for (int i = 0; i < values().length; i++) {
                if (values()[i]._equals(b)) {
                    return values()[i];
                }
            }
            return result;
        }
    }

    @Keep
    public enum ColorType {
        T2000K(0),
        T2500K(1),
        T3000K(2),
        T3500K(3),
        T4000K(4),
        T4500K(5),
        T5000K(6),
        T5500K(7),
        T6000K(8),
        T6500K(9),
        T7000K(10),
        T7500K(11),
        T8000K(12),
        T8500K(13),
        T9000K(14),
        T9500K(15),
        T10000K(16),
        OTHER(100);
        
        private int data;

        private ColorType(int _data) {
            this.data = _data;
        }

        public int value() {
            return this.data;
        }

        public boolean _equals(int b) {
            return this.data == b;
        }

        public static ColorType find(int b) {
            ColorType result = OTHER;
            for (int i = 0; i < values().length; i++) {
                if (values()[i]._equals(b)) {
                    return values()[i];
                }
            }
            return result;
        }
    }
}
