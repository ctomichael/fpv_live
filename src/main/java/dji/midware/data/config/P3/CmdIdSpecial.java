package dji.midware.data.config.P3;

import android.support.v4.app.FrameMetricsAggregator;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.manager.P3.DataBase;
import dji.midware.data.model.P3.DataOldSpecialControl;
import dji.midware.interfaces.CmdIdInterface;

@EXClassNullAway
public class CmdIdSpecial extends DJICmdSetBase {

    public enum CmdIdType implements CmdIdInterface {
        Control(1, false, DataOldSpecialControl.class),
        JoySitckSetParams(2),
        NewControl(3, false, true, (Class<? extends DataBase>) null),
        LockRcControl(240),
        Other(FrameMetricsAggregator.EVERY_DURATION);
        
        private static CmdIdType[] items = values();
        private Class<? extends DataBase> cls;
        private int data;
        private DataBase dataBase;
        private boolean isBlock = true;
        private boolean isMix = false;

        private CmdIdType(int _data) {
            this.data = _data;
        }

        private CmdIdType(int _data, boolean isBlock2, Class<? extends DataBase> cls2) {
            this.data = _data;
            this.isBlock = isBlock2;
            this.cls = cls2;
            this.dataBase = DataUtil.getDataBaseInstRefl(cls2);
        }

        private CmdIdType(int _data, boolean isBlock2, boolean isMix2, Class<? extends DataBase> cls2) {
            this.data = _data;
            this.isMix = isMix2;
            this.isBlock = isBlock2;
            this.cls = cls2;
            this.dataBase = DataUtil.getDataBaseInstRefl(cls2);
        }

        private CmdIdType(int _data, boolean isBlock2, Class<? extends DataBase> cls2, DataBase dataBase2) {
            this.data = _data;
            this.isBlock = isBlock2;
            this.cls = cls2;
            this.dataBase = dataBase2;
        }

        public int value() {
            return this.data;
        }

        public boolean isMix() {
            return this.isMix;
        }

        public boolean isBlock() {
            return this.isBlock;
        }

        public Class<? extends DataBase> getDataModel() {
            return this.cls;
        }

        public boolean isNeedCcode() {
            return true;
        }

        public DataBase getDataBase() {
            return this.dataBase;
        }

        public boolean _equals(int b) {
            return this.data == b;
        }

        public static CmdIdType find(int b) {
            CmdIdType result = Other;
            for (int i = 0; i < items.length; i++) {
                if (items[i]._equals(b)) {
                    return items[i];
                }
            }
            return result;
        }
    }

    /* access modifiers changed from: protected */
    public CmdIdInterface[] getCurCmdIdValues() {
        return CmdIdType.values();
    }

    public String getDataModelName(int deviceType, int cmdId) {
        return DataUtil.getDataModelName(DeviceType.find(deviceType).toString(), CmdIdType.find(cmdId).toString());
    }

    public String getDataModelName(int deviceType, int deviceId, int cmdId) {
        return getDataModelName(deviceType, cmdId);
    }
}
