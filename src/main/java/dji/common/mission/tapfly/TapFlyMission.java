package dji.common.mission.tapfly;

import android.graphics.PointF;
import dji.fieldAnnotation.EXClassNullAway;

@EXClassNullAway
public class TapFlyMission {
    public boolean isHorizontalObstacleAvoidanceEnabled;
    public float speed;
    public TapFlyMode tapFlyMode;
    public PointF target;

    public int hashCode() {
        int result;
        int i;
        int i2 = 0;
        if (this.tapFlyMode != null) {
            result = this.tapFlyMode.hashCode();
        } else {
            result = 0;
        }
        int i3 = result * 31;
        if (this.isHorizontalObstacleAvoidanceEnabled) {
            i = 1;
        } else {
            i = 0;
        }
        int floatToIntBits = (((i3 + i) * 31) + Float.floatToIntBits(this.speed)) * 31;
        if (this.target != null) {
            i2 = this.target.hashCode();
        }
        return floatToIntBits + i2;
    }

    public boolean equals(Object o) {
        boolean z;
        if (o == null || !(o instanceof TapFlyMission)) {
            return false;
        }
        if (this == o) {
            return true;
        }
        if (this.target == null || ((TapFlyMission) o).target == null) {
            if (this.target == null || ((TapFlyMission) o).target == null) {
                return false;
            }
        } else if (!this.target.equals(((TapFlyMission) o).target)) {
            return false;
        }
        if (this.tapFlyMode == ((TapFlyMission) o).tapFlyMode && this.isHorizontalObstacleAvoidanceEnabled == ((TapFlyMission) o).isHorizontalObstacleAvoidanceEnabled && this.speed == ((TapFlyMission) o).speed) {
            z = true;
        } else {
            z = false;
        }
        return z;
    }
}
