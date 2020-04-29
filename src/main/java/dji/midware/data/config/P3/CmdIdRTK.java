package dji.midware.data.config.P3;

import android.support.v4.app.FrameMetricsAggregator;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.manager.P3.DataBase;
import dji.midware.data.model.P3.DataCenterRTKPushStatus;
import dji.midware.data.model.P3.DataRTKGetPushStateInfo;
import dji.midware.data.model.P3.DataRTKGetReferenceStationSource;
import dji.midware.data.model.P3.DataRTKPushBeidouSnrAnt1;
import dji.midware.data.model.P3.DataRTKPushBeidouSnrAnt2;
import dji.midware.data.model.P3.DataRTKPushBeidouSnrAntB;
import dji.midware.data.model.P3.DataRTKPushGalileoSnrAnt1;
import dji.midware.data.model.P3.DataRTKPushGalileoSnrAnt2;
import dji.midware.data.model.P3.DataRTKPushGalileoSnrAntB;
import dji.midware.data.model.P3.DataRTKPushGlonassSnrAnt1;
import dji.midware.data.model.P3.DataRTKPushGlonassSnrAnt2;
import dji.midware.data.model.P3.DataRTKPushGlonassSnrAntB;
import dji.midware.data.model.P3.DataRTKPushGpsSnrAnt1;
import dji.midware.data.model.P3.DataRTKPushGpsSnrAnt2;
import dji.midware.data.model.P3.DataRTKPushGpsSnrAntB;
import dji.midware.interfaces.CmdIdInterface;

@EXClassNullAway
public class CmdIdRTK extends DJICmdSetBase {

    public enum CmdIdType implements CmdIdInterface {
        PushStatus(9, false, false, DataCenterRTKPushStatus.class),
        PushGpsSnrDataAnt1(16, false, false, DataRTKPushGpsSnrAnt1.class),
        PushGlonassSnrDataAnt1(17, false, false, DataRTKPushGlonassSnrAnt1.class),
        PushBeidouSnrDataAnt1(18, false, false, DataRTKPushBeidouSnrAnt1.class),
        PushGaleoSnrAnt1(26, false, false, DataRTKPushGalileoSnrAnt1.class),
        PushGpsSnrDataAnt2(19, false, false, DataRTKPushGpsSnrAnt2.class),
        PushGlonassSnrDataAnt2(20, false, false, DataRTKPushGlonassSnrAnt2.class),
        PushBeidouSnrDataAnt2(21, false, false, DataRTKPushBeidouSnrAnt2.class),
        PushGaleoSnrAnt2(27, false, false, DataRTKPushGalileoSnrAnt2.class),
        PushGpsSnrDataAntB(22, false, false, DataRTKPushGpsSnrAntB.class),
        PushGlonassSnrDataAntB(23, false, false, DataRTKPushGlonassSnrAntB.class),
        PushBeidouSnrDataAntB(24, false, false, DataRTKPushBeidouSnrAntB.class),
        PushGaleoSnrAntB(28, false, false, DataRTKPushGalileoSnrAntB.class),
        RTKSnrGetEnable(251),
        RTKSnrSetEnable(252),
        SetMultiParam(244),
        GetMultiParam(245),
        NrtkDataTransfer(41),
        InitNRTK(52),
        setRTKType(53),
        SendNRTKGGA(54),
        GetPushNRTKData(55, false, false, DataRTKGetPushStateInfo.class),
        NRTKActivate(56),
        GetReferenceStationSource(61, false, false, DataRTKGetReferenceStationSource.class),
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

        private CmdIdType(int _data, boolean isBlock2, boolean isMix2, Class<? extends DataBase> cls2) {
            this.data = _data;
            this.isBlock = isBlock2;
            this.cls = cls2;
            this.isMix = isMix2;
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

    /* access modifiers changed from: protected */
    public CmdIdInterface[] getCurCmdIdValues() {
        return CmdIdType.values();
    }

    public String getDataModelName(int deviceType, int deviceId, int cmdId) {
        return DataUtil.getDataModelName(DeviceType.find(deviceType).toString(), CmdIdType.find(cmdId).toString());
    }
}
