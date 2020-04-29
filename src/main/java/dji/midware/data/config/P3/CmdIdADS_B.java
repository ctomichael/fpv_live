package dji.midware.data.config.P3;

import android.support.v4.app.FrameMetricsAggregator;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.manager.P3.DataBase;
import dji.midware.data.model.P3.DataADSBGetPushAvoidanceAction;
import dji.midware.data.model.P3.DataADSBGetPushData;
import dji.midware.data.model.P3.DataADSBGetPushOriginal;
import dji.midware.data.model.P3.DataADSBGetPushUnlockInfo;
import dji.midware.data.model.P3.DataADSBGetPushWarning;
import dji.midware.interfaces.CmdIdInterface;

@EXClassNullAway
public class CmdIdADS_B extends DJICmdSetBase {

    public enum CmdIdType implements CmdIdInterface {
        GetPushData(2, false, DataADSBGetPushData.class),
        GetPushWarning(8, false, DataADSBGetPushWarning.class),
        GetPushOriginal(9, false, DataADSBGetPushOriginal.class),
        SendWhiteList(16),
        RequestLicense(17),
        SetLicenseEnabled(18),
        GetLicenseId(19),
        GetPushUnlockInfo(20, false, DataADSBGetPushUnlockInfo.class),
        SetUserId(21),
        GetKeyVersion(22),
        GetPushAvoidanceAction(23, false, DataADSBGetPushAvoidanceAction.class),
        Other(FrameMetricsAggregator.EVERY_DURATION);
        
        private static CmdIdType[] items = values();
        private Class<? extends DataBase> cls;
        private int data;
        private DataBase dataBase;
        private boolean isBlock = true;
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
            return false;
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

    /* access modifiers changed from: protected */
    public CmdIdInterface[] getCurCmdIdValues() {
        return CmdIdType.values();
    }

    public String getDataModelName(int deviceType, int deviceId, int cmdId) {
        return DataUtil.getDataModelName(DeviceType.find(deviceType).toString(), CmdIdType.find(cmdId).toString());
    }
}
