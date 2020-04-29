package dji.midware.data.config.P3;

import android.support.v4.app.FrameMetricsAggregator;
import dji.midware.data.manager.P3.DataBase;
import dji.midware.data.model.P3.DataNarrowBandBaseInfoConfig;
import dji.midware.data.model.P3.DataNarrowBandExchangeMode;
import dji.midware.data.model.P3.DataNarrowBandGetPushDeviceList;
import dji.midware.data.model.P3.DataNarrowBandGetPushStateInfo;
import dji.midware.interfaces.CmdIdInterface;

public class CmdIdNarrowBand extends DJICmdSetBase {

    public enum CmdIdType implements CmdIdInterface {
        GetPushStateInfo(3, false, DataNarrowBandGetPushStateInfo.class),
        GetPushDeviceList(4, false, DataNarrowBandGetPushDeviceList.class),
        ExchangeMode(10, false, true, DataNarrowBandExchangeMode.class),
        BaseInfoConfig(13, false, true, DataNarrowBandBaseInfoConfig.class),
        Other(FrameMetricsAggregator.EVERY_DURATION);
        
        private static CmdIdType[] items = values();
        private Class<? extends DataBase> cls;
        private int data;
        private DataBase dataBase;
        private boolean isBlock = true;
        private boolean isMix = false;
        private boolean isNeedCcode = true;

        private CmdIdType(int _data) {
            this.data = _data;
        }

        private CmdIdType(int _data, boolean isBlock2, boolean isMix2, Class<? extends DataBase> cls2) {
            this.data = _data;
            this.isBlock = isBlock2;
            this.isMix = isMix2;
            this.cls = cls2;
            this.dataBase = DataUtil.getDataBaseInstRefl(cls2);
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
            return this.isMix;
        }

        public int value() {
            return this.data;
        }

        public boolean isBlock() {
            return this.isBlock;
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
