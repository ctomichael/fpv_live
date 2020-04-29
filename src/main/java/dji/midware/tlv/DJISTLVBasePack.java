package dji.midware.tlv;

import dji.fieldAnnotation.EXClassNullAway;

@EXClassNullAway
public class DJISTLVBasePack {
    public static final byte DEFAULT_MAGIC = 0;
    public byte headCrc;
    public int index;
    public int length = length();
    public byte magic = 0;
    public short packCrc;
    public DJISTLVBasePackData payload = packData();
    public int type;
    public float version = 1.0f;

    /* access modifiers changed from: protected */
    public DJISTLVBasePackData packData() {
        return new DJISTLVBasePackData();
    }

    public int length() {
        return this.payload.length() + 20;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("magic:").append((int) this.magic).append(" ").append("type:").append(this.type).append(" ").append("version:").append(this.version).append(" ").append("length:").append(this.length).append(" ").append("index:").append(this.index).append(" ").append("headCrc:").append((int) this.headCrc).append(" ").append("payload:").append(this.payload.toString()).append(" ").append("packCrc:").append((int) this.packCrc).append(" ");
        return sb.toString();
    }

    public static DJISTLVBasePack testData() {
        DJISTLVBasePack pack = new DJISTLVBasePack();
        pack.length = 160;
        pack.index = 1;
        pack.headCrc = 1;
        return pack;
    }

    public boolean checkMagic() {
        return this.magic == 0;
    }

    public static class DJISTLVBasePackData {
        public int length() {
            return 0;
        }

        public String toString() {
            return "DJISTLVBasePackData {}";
        }
    }
}
