package dji.midware.tlv.mapParser;

import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.tlv.DJIMapBasePack;

@EXClassNullAway
public interface IDJIMapPackItemParser {
    int length();

    DJIMapBasePack.MapBaseItem parse(byte[] bArr, int i, int i2);

    byte[] serialize(DJIMapBasePack.MapBaseItem mapBaseItem);
}
