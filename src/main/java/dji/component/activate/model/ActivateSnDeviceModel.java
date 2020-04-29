package dji.component.activate.model;

import android.support.annotation.Keep;
import dji.fieldAnnotation.EXClassNullAway;
import dji.thirdparty.afinal.annotation.sqlite.Id;
import java.util.List;

@Keep
@EXClassNullAway
public class ActivateSnDeviceModel {
    public static final int ACTIVE_TYPE_EAGLE = 2;
    public static final int ACTIVE_TYPE_NORMAL = 1;
    public static final int ENABLE_STATE_NOT_CHECK = -1;
    public static final int ENABLE_STATE_NOT_EXIST = 0;
    public static final int ENABLE_STATE_REPEAT_SUCCESS = 2;
    public static final int ENABLE_STATE_SUCCESS = 1;
    public List<DJISnModel> item;
    public int status;
    public String status_msg;

    @Keep
    public static class DJISnModel {
        public int activeType = 1;
        public byte[] data;
        public int deviceIndex;
        public int deviceType;
        public int enabled = -1;
        public int moduleType;
        public int productType;
        @Id
        public String sn = "";
        public long timestamp;

        public String toString() {
            return "deviceType=" + this.deviceType + " sn=" + this.sn + " data=" + this.data + " index=" + this.deviceIndex;
        }
    }
}
