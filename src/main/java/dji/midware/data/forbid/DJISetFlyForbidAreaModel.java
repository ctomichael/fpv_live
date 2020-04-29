package dji.midware.data.forbid;

import android.support.annotation.Keep;
import com.drew.metadata.exif.makernotes.LeicaMakernoteDirectory;
import dji.fieldAnnotation.EXClassNullAway;

@Keep
@EXClassNullAway
public class DJISetFlyForbidAreaModel {
    public int contryCode;
    public int id;
    public int latitude;
    public int longitude;
    public int radius;
    public int type;

    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof DJISetFlyForbidAreaModel)) {
            return false;
        }
        DJISetFlyForbidAreaModel modelObj = (DJISetFlyForbidAreaModel) obj;
        if (modelObj.latitude == this.latitude && modelObj.longitude == this.longitude && modelObj.radius == this.radius && modelObj.contryCode == this.contryCode && modelObj.type == this.type && modelObj.id == this.id) {
            return true;
        }
        return false;
    }

    public int hashCode() {
        return (((((((((this.latitude * 31) + this.longitude) * 31) + this.radius) * 31) + this.contryCode) * 31) + this.type) * 31) + this.id;
    }

    public static DJISetFlyForbidAreaModel generateEmptyFlushModel() {
        DJISetFlyForbidAreaModel emptyFlushModel = new DJISetFlyForbidAreaModel();
        emptyFlushModel.latitude = 0;
        emptyFlushModel.longitude = 0;
        emptyFlushModel.radius = 1;
        emptyFlushModel.contryCode = LeicaMakernoteDirectory.TAG_WB_BLUE_LEVEL;
        emptyFlushModel.type = 1;
        emptyFlushModel.id = 10086;
        return emptyFlushModel;
    }
}
