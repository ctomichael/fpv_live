package dji.midware.data.model.P3;

import android.support.annotation.Keep;
import com.dji.cmd.v1.protocol.MlTkModeE;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.model.base.DJICommonDataBase;
import java.util.ArrayList;
import java.util.Objects;

@Keep
@EXClassNullAway
public class DataEyeGetPushMultiTrackingInformation extends DJICommonDataBase {
    private static final int TRACKING_INFORMATION_LENGTH = 19;
    private static DataEyeGetPushMultiTrackingInformation instance;

    public static synchronized DataEyeGetPushMultiTrackingInformation getInstance() {
        DataEyeGetPushMultiTrackingInformation dataEyeGetPushMultiTrackingInformation;
        synchronized (DataEyeGetPushMultiTrackingInformation.class) {
            if (instance == null) {
                instance = new DataEyeGetPushMultiTrackingInformation();
            }
            dataEyeGetPushMultiTrackingInformation = instance;
        }
        return dataEyeGetPushMultiTrackingInformation;
    }

    /* access modifiers changed from: protected */
    public boolean isChanged(byte[] data) {
        return true;
    }

    public int getNumberOfTarget() {
        return ((Integer) get(0, 1, Integer.class)).intValue();
    }

    public ArrayList<MultiTrackingTargetInformation> getMultiTrackingTargetsInformation() {
        int num = getNumberOfTarget();
        ArrayList<MultiTrackingTargetInformation> information = new ArrayList<>();
        for (int i = 0; i < num; i++) {
            MultiTrackingTargetInformation tmp = new MultiTrackingTargetInformation();
            tmp.index = ((Integer) get((i * 19) + 1, 1, Integer.class)).intValue();
            tmp.mode = ((Integer) get((i * 19) + 2, 1, Integer.class)).intValue();
            tmp.centerX = ((Float) get((i * 19) + 3, 4, Float.class)).floatValue();
            tmp.centerY = ((Float) get((i * 19) + 7, 4, Float.class)).floatValue();
            tmp.width = ((Float) get((i * 19) + 11, 4, Float.class)).floatValue();
            tmp.height = ((Float) get((i * 19) + 15, 4, Float.class)).floatValue();
            tmp.type = ((Integer) get((i * 19) + 19, 1, Integer.class)).intValue();
            information.add(tmp);
        }
        return information;
    }

    /* access modifiers changed from: protected */
    public void doPack() {
    }

    @Keep
    public static class MultiTrackingTargetInformation {
        public float centerX;
        public float centerY;
        public float height;
        public int index;
        public MlTkModeE mlTkModeE;
        public int mode;
        public long timestamp;
        public int type;
        public float width;

        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            MultiTrackingTargetInformation that = (MultiTrackingTargetInformation) o;
            if (this.index == that.index && this.mode == that.mode && Float.compare(that.centerX, this.centerX) == 0 && Float.compare(that.centerY, this.centerY) == 0 && Float.compare(that.width, this.width) == 0 && Float.compare(that.height, this.height) == 0 && this.type == that.type && this.timestamp == that.timestamp && this.mlTkModeE == that.mlTkModeE) {
                return true;
            }
            return false;
        }

        public int hashCode() {
            return Objects.hash(Integer.valueOf(this.index), Integer.valueOf(this.mode), Float.valueOf(this.centerX), Float.valueOf(this.centerY), Float.valueOf(this.width), Float.valueOf(this.height), Integer.valueOf(this.type), Long.valueOf(this.timestamp), this.mlTkModeE);
        }

        public String toString() {
            return "MultiTrackingTargetInformation{index=" + this.index + ", mode=" + this.mode + ", centerX=" + this.centerX + ", centerY=" + this.centerY + ", width=" + this.width + ", height=" + this.height + ", type=" + this.type + ", timestamp=" + this.timestamp + ", mlTkModeE=" + this.mlTkModeE + '}';
        }

        public void copyFrom(MultiTrackingTargetInformation other) {
            if (other != null) {
                this.index = other.index;
                this.mode = other.mode;
                this.centerX = other.centerX;
                this.centerY = other.centerY;
                this.width = other.width;
                this.height = other.height;
                this.type = other.type;
                this.timestamp = other.timestamp;
                this.mlTkModeE = other.mlTkModeE;
            }
        }
    }
}
