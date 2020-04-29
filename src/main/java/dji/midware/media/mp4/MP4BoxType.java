package dji.midware.media.mp4;

import dji.fieldAnnotation.EXClassNullAway;

@EXClassNullAway
public enum MP4BoxType {
    ftyp("ftyp"),
    wide("wide"),
    mdat("mdat"),
    moov("moov"),
    mvhd("mvhd"),
    udta("udta"),
    trak("trak"),
    tkhd("tkhd"),
    edts("edts"),
    mdia("mdia"),
    mdhd("mdhd"),
    hdlr("hdlr"),
    minf("minf"),
    vmhd("vmhd"),
    dinf("dinf"),
    stbl("stbl"),
    stsd("stsd"),
    avc1("avc1"),
    avcC("avcC"),
    stts("stts"),
    ctts("ctts"),
    stsc("stsc"),
    stsz("stsz"),
    stco("stco"),
    stss("stss"),
    OTHER("other");
    
    private String value = "";

    private MP4BoxType(String _value) {
        this.value = _value;
    }

    public String value() {
        return this.value;
    }

    public boolean _equals(String type) {
        return this.value.equals(type);
    }

    public static MP4BoxType find(String type) {
        MP4BoxType result = OTHER;
        for (int i = 0; i < values().length; i++) {
            if (values()[i]._equals(type)) {
                return values()[i];
            }
        }
        return result;
    }
}
