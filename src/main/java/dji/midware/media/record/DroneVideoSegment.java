package dji.midware.media.record;

import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.media.DJIVideoUtil;

@EXClassNullAway
public class DroneVideoSegment {
    public static final int rectify = ((int) (((120.0d / ((double) DJIVideoUtil.getFPS())) * 1000.0d) - ((double) DJIVideoUtil.Midea_Signal_Delay_MSec)));
    private final int endTime;
    private final int startTime;

    public DroneVideoSegment(int startTime2, int endTime2) {
        this.startTime = rectify + startTime2;
        this.endTime = rectify + endTime2;
    }

    public String toString() {
        return this.startTime + "_to_" + this.endTime;
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof DroneVideoSegment)) {
            return false;
        }
        DroneVideoSegment segment = (DroneVideoSegment) obj;
        if (this.startTime == segment.startTime && this.endTime == segment.endTime) {
            return true;
        }
        return false;
    }

    public int getEndTime() {
        return this.endTime;
    }

    public int getStartTime() {
        return this.startTime;
    }
}
