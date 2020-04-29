package dji.midware.data.config.P3;

import android.support.v4.app.FrameMetricsAggregator;
import dji.midware.data.manager.P3.DataBase;
import dji.midware.data.model.P3.DataFlyc2GetPushRtkAbnormalStatus;
import dji.midware.data.model.P3.DataFlycGetPushMassCenterCaliStatus;
import dji.midware.interfaces.CmdIdInterface;

public class CmdIdFlyc2 extends DJICmdSetBase {

    public enum CmdIdType implements CmdIdInterface {
        GetWaypointInfo(29),
        RequestSalesStrategy(48),
        MASS_CENTER_CALI_STATUS(20, false, DataFlycGetPushMassCenterCaliStatus.class),
        GetAutoFlyShowFwType(72),
        RTK_ABNORMAL_STATUS(10, false, DataFlyc2GetPushRtkAbnormalStatus.class),
        Other(FrameMetricsAggregator.EVERY_DURATION);
        
        private static CmdIdType[] items = values();
        private Class<? extends DataBase> cls;
        private int data;
        private DataBase dataBase;
        private boolean isBlock = true;
        private boolean isMix = false;
        private boolean isNeedCcode = false;

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

        private CmdIdType(int _data, boolean isBlock2, boolean isMix2, boolean isNeedCcode2, Class<? extends DataBase> cls2) {
            this.data = _data;
            this.isMix = isMix2;
            this.isBlock = isBlock2;
            this.cls = cls2;
            this.isNeedCcode = isNeedCcode2;
            this.dataBase = DataUtil.getDataBaseInstRefl(cls2);
        }

        private CmdIdType(int _data, boolean isNeedCcode2) {
            this.data = _data;
            this.isNeedCcode = isNeedCcode2;
        }

        public int value() {
            return this.data;
        }

        public boolean isBlock() {
            return this.isBlock;
        }

        public boolean isMix() {
            return this.isMix;
        }

        public Class<? extends DataBase> getDataModel() {
            return this.cls;
        }

        public boolean isNeedCcode() {
            return this.isNeedCcode;
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
