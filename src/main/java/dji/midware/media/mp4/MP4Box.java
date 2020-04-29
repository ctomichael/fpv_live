package dji.midware.media.mp4;

import dji.fieldAnnotation.EXClassNullAway;

@EXClassNullAway
public class MP4Box {
    public static final String TAG = MP4Box.class.getName();
    public byte[] box_data;
    public int box_size;
    public String box_type;
    public int length = 0;
    public int start_offset;
    public MP4BoxType type = MP4BoxType.OTHER;

    public MP4Box() {
    }

    public MP4Box(MP4BoxType type2, int length2) {
        this.type = type2;
        this.length = length2;
    }

    public boolean parseData(byte[] data, int offset) {
        this.start_offset = offset;
        this.box_size = MP4BytesUtil.getInt(data, 0);
        int index = 0 + 4;
        this.box_type = MP4BytesUtil.getStringUTF8(data, index, 4);
        int index2 = index + 4;
        this.type = MP4BoxType.find(this.box_type);
        this.length = this.box_size;
        return true;
    }
}
