package dji.midware.tlv.reader;

import dji.fieldAnnotation.EXClassNullAway;
import dji.log.DJILog;
import dji.midware.natives.GroudStation;
import dji.midware.tlv.DJIMapBasePack;
import dji.midware.tlv.mapParser.DJIMapBasePackItemParser;
import dji.midware.util.BytesUtil;

@EXClassNullAway
public class DJIMapPackReader {
    public DJIMapBasePack parse(byte[] data) {
        return parse(data, 0, data.length);
    }

    public DJIMapBasePack parse(byte[] data, int offset, int length) {
        int offsetStart = offset;
        DJIMapBasePack mapPack = new DJIMapBasePack();
        mapPack.tlvHeader = new DJISTLVPackReader().parse(data, offset);
        int offset2 = offset + 20;
        if (mapPack.tlvHeader == null) {
            return null;
        }
        mapPack.mapStartIndex = BytesUtil.getInt(data, offset2, 4);
        int offset3 = offset2 + 4;
        mapPack.mapItemCount = BytesUtil.getInt(data, offset3, 4);
        int offset4 = offset3 + 4;
        DJIMapBasePackItemParser mapItemParser = new DJIMapBasePackItemParser();
        int size = ((((length - 20) - 4) - 4) - 2) / mapItemParser.length();
        for (int i = 0; i < size; i++) {
            mapPack.addMapItem(mapItemParser.parse(data, offset4, mapItemParser.length()));
            offset4 += mapItemParser.length();
        }
        mapPack.crc = BytesUtil.getShort(data, offset4, 2);
        int length2 = offset4 - offsetStart;
        byte[] mapBytes = new byte[length2];
        System.arraycopy(data, offsetStart, mapBytes, 0, length2);
        short crc = GroudStation.native_calcCrc16(mapBytes);
        if (mapPack.crc == crc) {
            return mapPack;
        }
        DJILog.e("tlv", "map pack crc incorrect:" + ((int) mapPack.crc) + " src is:" + ((int) crc), new Object[0]);
        return null;
    }
}
