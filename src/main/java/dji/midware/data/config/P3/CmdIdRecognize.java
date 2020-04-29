package dji.midware.data.config.P3;

import android.support.v4.app.FrameMetricsAggregator;
import dji.midware.data.manager.P3.DataBase;
import dji.midware.data.model.P3.DataRecognizeGetPushIsSupportDJIGO;
import dji.midware.interfaces.CmdIdInterface;

public class CmdIdRecognize extends DJICmdSetBase {

    public enum CmdIdType implements CmdIdInterface {
        GetPushIsSupportDJIGO(1, false, DataRecognizeGetPushIsSupportDJIGO.class),
        Other(FrameMetricsAggregator.EVERY_DURATION);
        
        private Class<? extends DataBase> cls;
        private int data;
        private DataBase dataBase;
        private boolean isBlock = true;
        private boolean isNeedCcode = true;

        private CmdIdType(int _data) {
            this.data = _data;
        }

        private CmdIdType(int _data, boolean isBlock2, Class<? extends DataBase> cls2) {
            this.data = _data;
            this.isBlock = isBlock2;
            this.cls = cls2;
            this.dataBase = DataUtil.getDataBaseInstRefl(cls2);
        }

        private CmdIdType(int _data, boolean isNeedCcode2) {
            this.data = _data;
            this.isNeedCcode = isNeedCcode2;
        }

        public boolean isMix() {
            return false;
        }

        public boolean isBlock() {
            return this.isBlock;
        }

        public int value() {
            return this.data;
        }

        public boolean isNeedCcode() {
            return this.isNeedCcode;
        }

        public Class<? extends DataBase> getDataModel() {
            return this.cls;
        }

        public DataBase getDataBase() {
            return this.dataBase;
        }

        public static CmdIdType find(int b) {
            CmdIdType[] values = values();
            CmdIdType result = Other;
            if (values.length <= 0) {
                return result;
            }
            for (CmdIdType cmdIdType : values) {
                if (cmdIdType.value() == b) {
                    return cmdIdType;
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
