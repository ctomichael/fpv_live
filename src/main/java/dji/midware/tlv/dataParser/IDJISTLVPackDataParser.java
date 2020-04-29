package dji.midware.tlv.dataParser;

import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.tlv.DJISTLVBasePack;

@EXClassNullAway
public interface IDJISTLVPackDataParser {
    int length();

    DJISTLVBasePack.DJISTLVBasePackData parse(byte[] bArr, int i, int i2);

    byte[] serialize(DJISTLVBasePack.DJISTLVBasePackData dJISTLVBasePackData);
}
