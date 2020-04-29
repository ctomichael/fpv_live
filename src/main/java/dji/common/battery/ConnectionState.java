package dji.common.battery;

import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.model.P3.DataCenterGetPushBatteryCommon;

@EXClassNullAway
public enum ConnectionState {
    NORMAL(DataCenterGetPushBatteryCommon.ConnStatus.NORMAL),
    INVALID(DataCenterGetPushBatteryCommon.ConnStatus.INVALID),
    EXCEPTION(DataCenterGetPushBatteryCommon.ConnStatus.EXCEPTION),
    UNKNOWN(DataCenterGetPushBatteryCommon.ConnStatus.OTHER);
    
    private DataCenterGetPushBatteryCommon.ConnStatus connStatus;

    private ConnectionState(DataCenterGetPushBatteryCommon.ConnStatus status) {
        this.connStatus = status;
    }

    public int value() {
        return this.connStatus.value();
    }

    private boolean belongsTo(DataCenterGetPushBatteryCommon.ConnStatus status) {
        return this.connStatus == status;
    }

    public static ConnectionState find(DataCenterGetPushBatteryCommon.ConnStatus status) {
        ConnectionState[] values = values();
        for (ConnectionState cs : values) {
            if (cs.belongsTo(status)) {
                return cs;
            }
        }
        return UNKNOWN;
    }
}
