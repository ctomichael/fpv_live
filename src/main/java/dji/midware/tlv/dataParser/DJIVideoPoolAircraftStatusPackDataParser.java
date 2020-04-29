package dji.midware.tlv.dataParser;

import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.tlv.DJISTLVBasePack;
import dji.midware.tlv.DJIVideoPoolAircraftStatusPack;
import dji.midware.util.BytesUtil;

@EXClassNullAway
public class DJIVideoPoolAircraftStatusPackDataParser implements IDJISTLVPackDataParser {
    public DJISTLVBasePack.DJISTLVBasePackData parse(byte[] data, int offset, int length) {
        DJIVideoPoolAircraftStatusPack.DJIVideoPoolAircraftStatusPackData packData = new DJIVideoPoolAircraftStatusPack.DJIVideoPoolAircraftStatusPackData();
        packData.caMediaTimeMs = BytesUtil.getInt(data, offset, 4);
        int offset2 = offset + 4;
        packData.lat = BytesUtil.getDouble(data, offset2, 8);
        int offset3 = offset2 + 8;
        packData.lng = BytesUtil.getDouble(data, offset3, 8);
        int offset4 = offset3 + 8;
        packData.alt = BytesUtil.getFloat(data, offset4, 4);
        int offset5 = offset4 + 4;
        packData.pitch = BytesUtil.getFloat(data, offset5, 4);
        int offset6 = offset5 + 4;
        packData.row = BytesUtil.getFloat(data, offset6, 4);
        int offset7 = offset6 + 4;
        packData.yaw = BytesUtil.getFloat(data, offset7, 4);
        int offset8 = offset7 + 4;
        packData.speedX = BytesUtil.getFloat(data, offset8, 4);
        int offset9 = offset8 + 4;
        packData.speedY = BytesUtil.getFloat(data, offset9, 4);
        int offset10 = offset9 + 4;
        packData.speedZ = BytesUtil.getFloat(data, offset10, 4);
        packData.mcFlightMode = BytesUtil.getByte(data, offset10 + 4, 1);
        return packData;
    }

    public byte[] serialize(DJISTLVBasePack.DJISTLVBasePackData data) {
        byte[] bytes = new byte[length()];
        if (data instanceof DJIVideoPoolAircraftStatusPack.DJIVideoPoolAircraftStatusPackData) {
            DJIVideoPoolAircraftStatusPack.DJIVideoPoolAircraftStatusPackData packData = (DJIVideoPoolAircraftStatusPack.DJIVideoPoolAircraftStatusPackData) data;
            byte[] caMediaTimeMs = BytesUtil.getBytes(packData.caMediaTimeMs);
            byte[] lat = BytesUtil.getBytes(packData.lat);
            byte[] lng = BytesUtil.getBytes(packData.lng);
            byte[] alt = BytesUtil.getBytes(packData.alt);
            byte[] pitch = BytesUtil.getBytes(packData.pitch);
            byte[] row = BytesUtil.getBytes(packData.row);
            byte[] yaw = BytesUtil.getBytes(packData.yaw);
            byte[] speedX = BytesUtil.getBytes(packData.speedX);
            byte[] speedY = BytesUtil.getBytes(packData.speedY);
            byte[] speedZ = BytesUtil.getBytes(packData.speedZ);
            byte[] mcFlightMode = {packData.mcFlightMode};
            BytesUtil.arraycopy(caMediaTimeMs, bytes, 0);
            int offset = 0 + 4;
            BytesUtil.arraycopy(lat, bytes, offset);
            int offset2 = offset + 8;
            BytesUtil.arraycopy(lng, bytes, offset2);
            int offset3 = offset2 + 8;
            BytesUtil.arraycopy(alt, bytes, offset3);
            int offset4 = offset3 + 4;
            BytesUtil.arraycopy(pitch, bytes, offset4);
            int offset5 = offset4 + 4;
            BytesUtil.arraycopy(row, bytes, offset5);
            int offset6 = offset5 + 4;
            BytesUtil.arraycopy(yaw, bytes, offset6);
            int offset7 = offset6 + 4;
            BytesUtil.arraycopy(speedX, bytes, offset7);
            int offset8 = offset7 + 4;
            BytesUtil.arraycopy(speedY, bytes, offset8);
            int offset9 = offset8 + 4;
            BytesUtil.arraycopy(speedZ, bytes, offset9);
            BytesUtil.arraycopy(mcFlightMode, bytes, offset9 + 4);
        }
        return bytes;
    }

    public int length() {
        return new DJIVideoPoolAircraftStatusPack.DJIVideoPoolAircraftStatusPackData().length();
    }
}
