package dji.midware.tlv;

import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.tlv.DJISTLVBasePack;

@EXClassNullAway
public class DJIVideoPoolAircraftStatusPack extends DJISTLVBasePack {
    public DJIVideoPoolAircraftStatusPack() {
        this.type = 1;
    }

    /* access modifiers changed from: protected */
    public DJISTLVBasePack.DJISTLVBasePackData packData() {
        return new DJIVideoPoolAircraftStatusPackData();
    }

    public static DJISTLVBasePack testData() {
        DJISTLVBasePack pack = new DJIVideoPoolRemoteTimeTagPack();
        pack.index = 1;
        pack.headCrc = 1;
        DJIVideoPoolAircraftStatusPackData.testData(pack.payload);
        return pack;
    }

    public static class DJIVideoPoolAircraftStatusPackData extends DJISTLVBasePack.DJISTLVBasePackData {
        public float alt;
        public int caMediaTimeMs;
        public double lat;
        public double lng;
        public byte mcFlightMode;
        public float pitch;
        public float row;
        public float speedX;
        public float speedY;
        public float speedZ;
        public float yaw;

        public int length() {
            return 49;
        }

        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append("{ ").append("caMediaTimeMs:").append(this.caMediaTimeMs).append(" ").append("lat:").append(this.lat).append(" ").append("lng:").append(this.lng).append(" ").append("alt:").append(this.alt).append(" ").append("pitch:").append(this.pitch).append(" ").append("row:").append(this.row).append(" ").append("yaw:").append(this.yaw).append(" ").append("speedX:").append(this.speedX).append(" ").append("speedY:").append(this.speedY).append(" ").append("speedZ:").append(this.speedZ).append(" ").append("mcFlightMode:").append((int) this.mcFlightMode).append(" ").append(" }");
            return sb.toString();
        }

        public static void testData(DJISTLVBasePack.DJISTLVBasePackData basePackData) {
            DJIVideoPoolAircraftStatusPackData packData = (DJIVideoPoolAircraftStatusPackData) basePackData;
            packData.caMediaTimeMs = (int) System.currentTimeMillis();
            packData.lat = 23.16d;
            packData.lng = 113.23d;
            packData.alt = 100.0f;
            packData.pitch = 0.56f;
            packData.row = 0.04f;
            packData.yaw = 0.32f;
            packData.speedX = 70.3f;
            packData.speedY = 20.5f;
            packData.speedZ = 10.2f;
            packData.mcFlightMode = 2;
        }
    }
}
