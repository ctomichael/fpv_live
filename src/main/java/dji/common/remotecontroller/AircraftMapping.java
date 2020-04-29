package dji.common.remotecontroller;

import android.support.annotation.Nullable;
import dji.common.remotecontroller.AircraftStickMapping;
import dji.fieldAnnotation.EXClassNullAway;

@EXClassNullAway
public class AircraftMapping {
    private static final int DJI_RC_CONTROL_CHANNEL_SIZE = 4;
    public AircraftMappingStyle aircraftMappingStyle;
    public AircraftStickMapping leftHorizontal;
    public AircraftStickMapping leftVertical;
    public AircraftStickMapping rightHorizontal;
    public AircraftStickMapping rightVertical;

    public AircraftMapping(AircraftMappingStyle style) {
        this.aircraftMappingStyle = style;
        if (style == AircraftMappingStyle.STYLE_1) {
            this.leftVertical = new AircraftStickMapping.Builder().mappingTarget(AircraftStickMappingTarget.PITCH).isReversed(false).build();
            this.leftHorizontal = new AircraftStickMapping.Builder().mappingTarget(AircraftStickMappingTarget.YAW).isReversed(false).build();
            this.rightVertical = new AircraftStickMapping.Builder().mappingTarget(AircraftStickMappingTarget.THROTTLE).isReversed(false).build();
            this.rightHorizontal = new AircraftStickMapping.Builder().mappingTarget(AircraftStickMappingTarget.ROLL).isReversed(false).build();
        } else if (style == AircraftMappingStyle.STYLE_2) {
            this.leftVertical = new AircraftStickMapping.Builder().mappingTarget(AircraftStickMappingTarget.THROTTLE).isReversed(false).build();
            this.leftHorizontal = new AircraftStickMapping.Builder().mappingTarget(AircraftStickMappingTarget.YAW).isReversed(false).build();
            this.rightVertical = new AircraftStickMapping.Builder().mappingTarget(AircraftStickMappingTarget.PITCH).isReversed(false).build();
            this.rightHorizontal = new AircraftStickMapping.Builder().mappingTarget(AircraftStickMappingTarget.ROLL).isReversed(false).build();
        } else if (style == AircraftMappingStyle.STYLE_3) {
            this.leftVertical = new AircraftStickMapping.Builder().mappingTarget(AircraftStickMappingTarget.PITCH).isReversed(false).build();
            this.leftHorizontal = new AircraftStickMapping.Builder().mappingTarget(AircraftStickMappingTarget.ROLL).isReversed(false).build();
            this.rightVertical = new AircraftStickMapping.Builder().mappingTarget(AircraftStickMappingTarget.THROTTLE).isReversed(false).build();
            this.rightHorizontal = new AircraftStickMapping.Builder().mappingTarget(AircraftStickMappingTarget.YAW).isReversed(false).build();
        }
    }

    public AircraftMapping(@Nullable AircraftStickMapping leftVertical2, @Nullable AircraftStickMapping leftHorizontal2, @Nullable AircraftStickMapping rightVertical2, @Nullable AircraftStickMapping rightHorizontal2) {
        this.aircraftMappingStyle = AircraftMappingStyle.STYLE_CUSTOM;
        this.leftVertical = leftVertical2;
        this.leftHorizontal = leftHorizontal2;
        this.rightVertical = rightVertical2;
        this.rightHorizontal = rightHorizontal2;
    }

    public int hashCode() {
        int result;
        int i;
        int i2;
        int i3;
        int i4 = 0;
        if (this.aircraftMappingStyle != null) {
            result = this.aircraftMappingStyle.hashCode();
        } else {
            result = 0;
        }
        int i5 = result * 31;
        if (this.leftVertical != null) {
            i = this.leftVertical.hashCode();
        } else {
            i = 0;
        }
        int i6 = (i5 + i) * 31;
        if (this.leftHorizontal != null) {
            i2 = this.leftHorizontal.hashCode();
        } else {
            i2 = 0;
        }
        int i7 = (i6 + i2) * 31;
        if (this.rightVertical != null) {
            i3 = this.rightVertical.hashCode();
        } else {
            i3 = 0;
        }
        int i8 = (i7 + i3) * 31;
        if (this.rightHorizontal != null) {
            i4 = this.rightHorizontal.hashCode();
        }
        return i8 + i4;
    }

    public boolean equals(Object o) {
        boolean z;
        if (o == null || !(o instanceof AircraftMapping)) {
            return false;
        }
        if (this == o) {
            return true;
        }
        if (this.leftVertical == null || ((AircraftMapping) o).leftVertical == null) {
            if (this.leftVertical == null || ((AircraftMapping) o).leftVertical == null) {
                return false;
            }
        } else if (!this.leftVertical.equals(((AircraftMapping) o).leftVertical)) {
            return false;
        }
        if (this.leftHorizontal == null || ((AircraftMapping) o).leftHorizontal == null) {
            if (this.leftHorizontal == null || ((AircraftMapping) o).leftHorizontal == null) {
                return false;
            }
        } else if (!this.leftHorizontal.equals(((AircraftMapping) o).leftHorizontal)) {
            return false;
        }
        if (this.rightVertical == null || ((AircraftMapping) o).rightVertical == null) {
            if (this.rightVertical == null || ((AircraftMapping) o).rightVertical == null) {
                return false;
            }
        } else if (!this.rightVertical.equals(((AircraftMapping) o).rightVertical)) {
            return false;
        }
        if (this.rightHorizontal == null || ((AircraftMapping) o).rightHorizontal == null) {
            if (this.rightHorizontal == null || ((AircraftMapping) o).rightHorizontal == null) {
                return false;
            }
        } else if (!this.rightHorizontal.equals(((AircraftMapping) o).rightHorizontal)) {
            return false;
        }
        if (this.aircraftMappingStyle == ((AircraftMapping) o).aircraftMappingStyle) {
            z = true;
        } else {
            z = false;
        }
        return z;
    }
}
