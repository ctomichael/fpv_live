package dji.midware.tlv;

import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.media.DJIVideoDecoder;
import dji.midware.tlv.DJISTLVBasePack;

@EXClassNullAway
public class DJIVideoPoolRemoteTimeTagPack extends DJISTLVBasePack {
    public DJIVideoPoolRemoteTimeTagPack() {
        this.type = 3;
    }

    /* access modifiers changed from: protected */
    public DJISTLVBasePack.DJISTLVBasePackData packData() {
        return new DJIVideoPoolRemoteTimeTagPackData();
    }

    public static DJISTLVBasePack testData() {
        DJISTLVBasePack pack = new DJIVideoPoolRemoteTimeTagPack();
        pack.index = 1;
        pack.headCrc = 1;
        DJIVideoPoolRemoteTimeTagPackData.testData(pack.payload);
        return pack;
    }

    public static class DJIVideoPoolRemoteTimeTagPackData extends DJISTLVBasePack.DJISTLVBasePackData {
        public int caMediaTime;
        public int localTime;
        public int remoteTime;

        public int length() {
            return 12;
        }

        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append("{ ").append("localTime:").append(this.localTime).append(" ").append("remoteTime:").append(this.remoteTime).append(" ").append("caMediaTime:").append(this.caMediaTime).append(" ").append(" }");
            return sb.toString();
        }

        public static void testData(DJISTLVBasePack.DJISTLVBasePackData basePackData) {
            DJIVideoPoolRemoteTimeTagPackData packData = (DJIVideoPoolRemoteTimeTagPackData) basePackData;
            packData.localTime = (int) System.currentTimeMillis();
            packData.remoteTime = ((int) System.currentTimeMillis()) + 1000;
            packData.caMediaTime = ((int) System.currentTimeMillis()) + DJIVideoDecoder.connectLosedelay;
        }
    }
}
