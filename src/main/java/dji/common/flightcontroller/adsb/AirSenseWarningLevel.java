package dji.common.flightcontroller.adsb;

import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.model.P3.DataADSBGetPushWarning;

@EXClassNullAway
public enum AirSenseWarningLevel {
    LEVEL_0(DataADSBGetPushWarning.DJIWarningType.None),
    LEVEL_1(DataADSBGetPushWarning.DJIWarningType.First),
    LEVEL_2(DataADSBGetPushWarning.DJIWarningType.Second),
    LEVEL_3(DataADSBGetPushWarning.DJIWarningType.Three),
    LEVEL_4(DataADSBGetPushWarning.DJIWarningType.Four),
    UNKNOWN(DataADSBGetPushWarning.DJIWarningType.OTHER);
    
    private DataADSBGetPushWarning.DJIWarningType type;

    private AirSenseWarningLevel(DataADSBGetPushWarning.DJIWarningType status) {
        this.type = status;
    }

    public int value() {
        return this.type.value();
    }

    private boolean belongsTo(DataADSBGetPushWarning.DJIWarningType status) {
        return this.type == status;
    }

    public static AirSenseWarningLevel find(DataADSBGetPushWarning.DJIWarningType status) {
        AirSenseWarningLevel[] values = values();
        for (AirSenseWarningLevel cs : values) {
            if (cs.belongsTo(status)) {
                return cs;
            }
        }
        return UNKNOWN;
    }
}
