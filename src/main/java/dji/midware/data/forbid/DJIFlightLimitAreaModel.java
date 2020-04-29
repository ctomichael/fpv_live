package dji.midware.data.forbid;

import android.support.annotation.Keep;
import dji.fieldAnnotation.EXClassNullAway;

@Keep
@EXClassNullAway
public class DJIFlightLimitAreaModel {
    public int area_id = 0;
    public int innerRadius;
    public int latitude;
    public int longitude;
    public int outerRadius;
    public int type;

    public boolean equals(Object o) {
        boolean z = true;
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        DJIFlightLimitAreaModel that = (DJIFlightLimitAreaModel) o;
        if (this.latitude != that.latitude || this.longitude != that.longitude || this.innerRadius != that.innerRadius || this.outerRadius != that.outerRadius || this.type != that.type) {
            return false;
        }
        if (this.area_id != that.area_id) {
            z = false;
        }
        return z;
    }

    public int hashCode() {
        return (((((((((this.latitude * 31) + this.longitude) * 31) + this.innerRadius) * 31) + this.outerRadius) * 31) + this.type) * 31) + this.area_id;
    }
}
