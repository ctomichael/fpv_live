package dji.common.remotecontroller;

import android.support.annotation.Nullable;
import dji.common.remotecontroller.GimbalStickMapping;
import dji.fieldAnnotation.EXClassNullAway;

@EXClassNullAway
public class GimbalMapping {
    public GimbalMappingStyle gimbalMappingStyle;
    public GimbalStickMapping leftHorizontal;
    public GimbalStickMapping leftVertical;
    public GimbalStickMapping rightHorizontal;
    public GimbalStickMapping rightVertical;

    public GimbalMapping() {
        this.gimbalMappingStyle = GimbalMappingStyle.DEFAULT;
        this.leftVertical = new GimbalStickMapping.Builder().mappingTarget(GimbalStickMappingTarget.NONE).isReversed(false).build();
        this.leftHorizontal = new GimbalStickMapping.Builder().mappingTarget(GimbalStickMappingTarget.YAW).isReversed(false).build();
        this.rightVertical = new GimbalStickMapping.Builder().mappingTarget(GimbalStickMappingTarget.PITCH).isReversed(false).build();
        this.rightHorizontal = new GimbalStickMapping.Builder().mappingTarget(GimbalStickMappingTarget.NONE).isReversed(false).build();
    }

    public GimbalMapping(@Nullable GimbalStickMapping leftVertical2, @Nullable GimbalStickMapping leftHorizontal2, @Nullable GimbalStickMapping rightVertical2, @Nullable GimbalStickMapping rightHorizontal2) {
        this.gimbalMappingStyle = GimbalMappingStyle.CUSTOM;
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
        if (this.gimbalMappingStyle != null) {
            result = this.gimbalMappingStyle.hashCode();
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
        if (o == null || !(o instanceof GimbalMapping)) {
            return false;
        }
        if (this == o) {
            return true;
        }
        if (this.leftVertical == null || ((GimbalMapping) o).leftVertical == null) {
            if (this.leftVertical == null || ((GimbalMapping) o).leftVertical == null) {
                return false;
            }
        } else if (!this.leftVertical.equals(((GimbalMapping) o).leftVertical)) {
            return false;
        }
        if (this.leftHorizontal == null || ((GimbalMapping) o).leftHorizontal == null) {
            if (this.leftHorizontal == null || ((GimbalMapping) o).leftHorizontal == null) {
                return false;
            }
        } else if (!this.leftHorizontal.equals(((GimbalMapping) o).leftHorizontal)) {
            return false;
        }
        if (this.rightVertical == null || ((GimbalMapping) o).rightVertical == null) {
            if (this.rightVertical == null || ((GimbalMapping) o).rightVertical == null) {
                return false;
            }
        } else if (!this.rightVertical.equals(((GimbalMapping) o).rightVertical)) {
            return false;
        }
        if (this.rightHorizontal == null || ((GimbalMapping) o).rightHorizontal == null) {
            if (this.rightHorizontal == null || ((GimbalMapping) o).rightHorizontal == null) {
                return false;
            }
        } else if (!this.rightHorizontal.equals(((GimbalMapping) o).rightHorizontal)) {
            return false;
        }
        if (this.gimbalMappingStyle == ((GimbalMapping) o).gimbalMappingStyle) {
            z = true;
        } else {
            z = false;
        }
        return z;
    }
}
