package dji.midware.tlv;

import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.tlv.DJISTLVBasePack;

@EXClassNullAway
public class DJIVideoPoolFlightAnalyticsPack extends DJISTLVBasePack {
    public DJIVideoPoolFlightAnalyticsPack() {
        this.type = 2;
    }

    /* access modifiers changed from: protected */
    public DJISTLVBasePack.DJISTLVBasePackData packData() {
        return new DJIVideoPoolFlightAnalyticsPackData();
    }

    public static DJISTLVBasePack testData() {
        DJISTLVBasePack pack = new DJIVideoPoolFlightAnalyticsPack();
        pack.index = 1;
        pack.headCrc = 1;
        DJIVideoPoolFlightAnalyticsPackData.testData(pack.payload);
        return pack;
    }

    public static class DJIVideoPoolFlightAnalyticsPackData extends DJISTLVBasePack.DJISTLVBasePackData {
        public float accl;
        public int caMediaTimeMs;
        public float cameraSpeedPitch;
        public float cameraSpeedRotate;
        public float cameraSpeedYaw;
        public float jerk;
        public float normalizedSpeed;

        public int length() {
            return 28;
        }

        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append("{ ").append("caMediaTimeMs:").append(this.caMediaTimeMs).append(" ").append("normalizedSpeed:").append(this.normalizedSpeed).append(" ").append("accl:").append(this.accl).append(" ").append("jerk:").append(this.jerk).append(" ").append("cameraSpeedPitch:").append(this.cameraSpeedPitch).append(" ").append("cameraSpeedYaw:").append(this.cameraSpeedYaw).append(" ").append("cameraSpeedRotate:").append(this.cameraSpeedRotate).append(" ").append(" }");
            return sb.toString();
        }

        public static void testData(DJISTLVBasePack.DJISTLVBasePackData basePackData) {
            DJIVideoPoolFlightAnalyticsPackData packData = (DJIVideoPoolFlightAnalyticsPackData) basePackData;
            packData.caMediaTimeMs = (int) System.currentTimeMillis();
            packData.normalizedSpeed = 0.82f;
            packData.accl = 14.5f;
            packData.jerk = 3.54f;
            packData.cameraSpeedPitch = 43.5f;
            packData.cameraSpeedYaw = 3.5f;
            packData.cameraSpeedRotate = 4.5f;
        }
    }
}
