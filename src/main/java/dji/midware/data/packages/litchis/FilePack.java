package dji.midware.data.packages.litchis;

import dji.fieldAnnotation.EXClassNullAway;

@EXClassNullAway
public class FilePack {
    public byte[] buffer;
    public int cmdId;
    public int cmdType;
    public byte[] data;
    public int isLastFlag;
    public int length;
    public int seq;
    public int sessionId;
    public int totalLength;
    public int version = 1;
}
