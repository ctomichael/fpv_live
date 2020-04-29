package dji.midware.data.model.P3;

import android.support.annotation.Keep;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.config.P3.CmdIdFlyc;
import dji.midware.data.config.P3.CmdSet;
import dji.midware.data.config.P3.DataConfig;
import dji.midware.data.config.P3.DeviceType;
import dji.midware.data.manager.P3.DataBase;
import dji.midware.data.packages.P3.SendPack;
import dji.midware.data.params.P3.ParamInfo;
import dji.midware.data.params.P3.RangeModel;
import dji.midware.interfaces.DJIDataCallBack;
import dji.midware.interfaces.DJIDataSyncListener;
import dji.midware.util.BytesUtil;
import java.util.HashMap;

@Keep
@EXClassNullAway
public class DataFlycGetParamInfo extends DataBase implements DJIDataSyncListener {
    private static HashMap<Integer, DataFlycGetParamInfo> hashMap = new HashMap<>();
    private Integer index;

    private static synchronized DataFlycGetParamInfo getInstance(Integer index2) {
        DataFlycGetParamInfo instance;
        synchronized (DataFlycGetParamInfo.class) {
            instance = hashMap.get(index2);
            if (instance == null) {
                instance = new DataFlycGetParamInfo(index2);
                hashMap.put(index2, instance);
            }
        }
        return instance;
    }

    public DataFlycGetParamInfo(Integer index2) {
        this.index = index2;
    }

    /* access modifiers changed from: protected */
    public DataBase.DATA_TYPE getDataType() {
        return DataBase.DATA_TYPE.LOCAL;
    }

    private void setRange(RangeModel range, Class<? extends Number> cls) {
        range.minValue = get(7, 4, cls);
        range.maxValue = get(11, 4, cls);
        range.defaultValue = get(15, 4, cls);
    }

    public ParamInfo getInfo() {
        TypeId typeId = TypeId.find(((Integer) get(1, 2, Integer.class)).intValue());
        ParamInfo paramInfo = new ParamInfo();
        RangeModel range = new RangeModel();
        switch (typeId) {
            case INT08S:
            case INT16S:
            case INT32S:
            case INT08U:
            case INT16U:
                paramInfo.type = Integer.class;
                break;
            case INT64S:
            case INT32U:
            case INT64U:
                paramInfo.type = Long.class;
                break;
            case BYTE:
                paramInfo.type = Byte.class;
                break;
            case DOUBLE:
                paramInfo.type = Double.class;
                break;
            default:
                paramInfo.type = Float.class;
                break;
        }
        paramInfo.index = this.index.intValue();
        paramInfo.typeId = typeId;
        paramInfo.size = ((Integer) get(3, 2, Integer.class)).intValue();
        paramInfo.attribute = Attribute.find(((Integer) get(5, 2, Integer.class)).intValue());
        setRange(range, paramInfo.type);
        paramInfo.range = range;
        paramInfo.name = get(19, this._recData.length - 19);
        return paramInfo;
    }

    /* access modifiers changed from: protected */
    public void doPack() {
        this._sendData = new byte[2];
        this._sendData = BytesUtil.getUnsignedBytes(this.index.intValue());
    }

    public void start(DJIDataCallBack callBack) {
        SendPack pack = new SendPack();
        pack.senderType = DeviceType.APP.value();
        pack.receiverType = DeviceType.FLYC.value();
        pack.cmdType = DataConfig.CMDTYPE.REQUEST.value();
        pack.isNeedAck = DataConfig.NEEDACK.YES.value();
        pack.encryptType = DataConfig.EncryptType.NO.value();
        pack.cmdSet = CmdSet.FLYC.value();
        pack.cmdId = CmdIdFlyc.CmdIdType.GetParamInfoByIndex.value();
        start(pack, callBack);
    }

    @Keep
    public enum TypeId {
        INT08U(0),
        INT16U(1),
        INT32U(2),
        INT64U(3),
        INT08S(4),
        INT16S(5),
        INT32S(6),
        INT64S(7),
        FLOAT(8),
        DOUBLE(9),
        BYTE(10),
        STRING(11),
        OTHER(100);
        
        private int data;

        private TypeId(int _data) {
            this.data = _data;
        }

        public int value() {
            return this.data;
        }

        public boolean _equals(int b) {
            return this.data == b;
        }

        public static TypeId find(int b) {
            TypeId result = OTHER;
            for (int i = 0; i < values().length; i++) {
                if (values()[i]._equals(b)) {
                    return values()[i];
                }
            }
            return result;
        }
    }

    @Keep
    public enum Attribute {
        READ_ONLY(0),
        READ_WRITE(1),
        EEPROM_WRITE(2),
        EEPROM_SPECIFIC(4),
        IMPORT_EXPORT(8),
        EEPROM_RW(EEPROM_WRITE.value() | READ_WRITE.value()),
        EEPROM_RW_IE((EEPROM_WRITE.value() | READ_WRITE.value()) | IMPORT_EXPORT.value()),
        OTHER(100);
        
        private int data;

        private Attribute(int _data) {
            this.data = _data;
        }

        public int value() {
            return this.data;
        }

        public boolean _equals(int b) {
            return this.data == b;
        }

        public static Attribute find(int b) {
            Attribute result = OTHER;
            for (int i = 0; i < values().length; i++) {
                if (values()[i]._equals(b)) {
                    return values()[i];
                }
            }
            return result;
        }
    }
}
