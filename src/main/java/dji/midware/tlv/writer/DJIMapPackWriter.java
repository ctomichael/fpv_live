package dji.midware.tlv.writer;

import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.natives.GroudStation;
import dji.midware.tlv.DJIMapBasePack;
import dji.midware.tlv.mapParser.DJIMapBasePackItemParser;
import dji.midware.util.BytesUtil;

@EXClassNullAway
public class DJIMapPackWriter {
    public byte[] serialize(DJIMapBasePack mapPack) {
        if (mapPack == null) {
            return new byte[30];
        }
        byte[] tlvHeader = new DJISTLVPackWriter().serialize(mapPack.tlvHeader);
        byte[] mapStartIndex = BytesUtil.getBytes(mapPack.mapStartIndex);
        byte[] mapItemCount = BytesUtil.getBytes(mapPack.mapItemCount);
        DJIMapBasePackItemParser mapItemParser = new DJIMapBasePackItemParser();
        byte[] mapItems = new byte[(mapItemParser.length() * mapPack.mapItemSize())];
        for (int i = 0; i < mapPack.mapItemSize(); i++) {
            byte[] mapItem = mapItemParser.serialize(mapPack.getMapItem(i));
            BytesUtil.arraycopy(mapItem, mapItems, mapItem.length * i);
        }
        byte[] data = new byte[(tlvHeader.length + mapStartIndex.length + mapItemCount.length + mapItems.length + 2)];
        BytesUtil.arraycopy(tlvHeader, data, 0);
        int offset = 0 + tlvHeader.length;
        BytesUtil.arraycopy(mapStartIndex, data, offset);
        int offset2 = offset + mapStartIndex.length;
        BytesUtil.arraycopy(mapItemCount, data, offset2);
        int offset3 = offset2 + mapItemCount.length;
        BytesUtil.arraycopy(mapItems, data, offset3);
        int offset4 = offset3 + mapItems.length;
        mapPack.crc = GroudStation.native_calcCrc16(data, data.length - 2);
        BytesUtil.arraycopy(BytesUtil.getBytes(mapPack.crc), data, offset4);
        return data;
    }
}
