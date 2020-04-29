package dji.midware.parser.plugins;

import dji.fieldAnnotation.EXClassNullAway;

@EXClassNullAway
public class DJIRingBufferModel {
    public int bodyLen;
    public byte[] header;
    public int secondHeaderLen;
}
