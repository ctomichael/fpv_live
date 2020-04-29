package dji.midware.tlv.dataParser;

import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.tlv.DJISTLVBasePack;

@EXClassNullAway
public class DJISTLVBasePackDataParser implements IDJISTLVPackDataParser {
    public DJISTLVBasePack.DJISTLVBasePackData parse(byte[] data, int offset, int length) {
        return new DJISTLVBasePack.DJISTLVBasePackData();
    }

    public byte[] serialize(DJISTLVBasePack.DJISTLVBasePackData data) {
        return new byte[0];
    }

    public int length() {
        return 0;
    }
}
