package dji.midware.tlv.mapParser;

import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.tlv.DJIMapBasePack;
import dji.midware.util.BytesUtil;

@EXClassNullAway
public class DJIMapBasePackItemParser implements IDJIMapPackItemParser {
    private static final int LENGTH = 16;

    public int length() {
        return 16;
    }

    public DJIMapBasePack.MapBaseItem parse(byte[] data, int offset, int length) {
        DJIMapBasePack.MapBaseItem item = new DJIMapBasePack.MapBaseItem();
        item.type = BytesUtil.getInt(data, offset, 4);
        int offset2 = offset + 4;
        item.version = BytesUtil.getFloat(data, offset2, 4);
        item.offset = BytesUtil.getLong(data, offset2 + 4, 8);
        return item;
    }

    public byte[] serialize(DJIMapBasePack.MapBaseItem data) {
        byte[] bytes = new byte[16];
        if (data != null) {
            byte[] type = BytesUtil.getBytes(data.type);
            byte[] version = BytesUtil.getBytes(data.version);
            byte[] offset = BytesUtil.getBytes(data.offset);
            BytesUtil.arraycopy(type, bytes, 0);
            int bOffset = 0 + 4;
            BytesUtil.arraycopy(version, bytes, bOffset);
            BytesUtil.arraycopy(offset, bytes, bOffset + 4);
        }
        return bytes;
    }
}
