package dji.midware.tlv.writer;

import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.natives.GroudStation;
import dji.midware.tlv.DJISTLVBasePack;
import dji.midware.tlv.DJISTLVPackFactory;
import dji.midware.util.BytesUtil;

@EXClassNullAway
public class DJISTLVPackWriter {
    public static byte[] testSerialize(DJISTLVBasePack pack) {
        return new DJISTLVPackWriter().serialize(pack);
    }

    public byte[] serialize(DJISTLVBasePack pack) {
        if (pack == null) {
            return new byte[20];
        }
        byte[] magic = {pack.magic};
        byte[] type = BytesUtil.getBytes(pack.type);
        byte[] version = BytesUtil.getBytes(pack.version);
        byte[] length = BytesUtil.getBytes(pack.length);
        byte[] index = BytesUtil.getBytes(pack.index);
        byte[] payload = DJISTLVPackFactory.getDataParser(DJISTLVPackFactory.Type.find(pack.type)).serialize(pack.payload);
        int payloadLen = payload.length;
        byte[] header = new byte[(magic.length + type.length + version.length + length.length + index.length)];
        byte[] result = new byte[(header.length + 1 + payloadLen + 2)];
        BytesUtil.arraycopy(magic, header, 0);
        int offset = 0 + 1;
        BytesUtil.arraycopy(type, header, offset);
        int offset2 = offset + 4;
        BytesUtil.arraycopy(version, header, offset2);
        int offset3 = offset2 + 4;
        BytesUtil.arraycopy(length, header, offset3);
        int offset4 = offset3 + 4;
        BytesUtil.arraycopy(index, header, offset4);
        int offset5 = offset4 + 4;
        pack.headCrc = GroudStation.native_calcCrc8(header);
        byte[] headCrc = {pack.headCrc};
        BytesUtil.arraycopy(header, result, 0);
        BytesUtil.arraycopy(headCrc, result, offset5);
        BytesUtil.arraycopy(payload, result, offset5 + 1);
        int offset6 = payloadLen + 18;
        pack.packCrc = GroudStation.native_calcCrc16(result, offset6);
        BytesUtil.arraycopy(BytesUtil.getBytes(pack.packCrc), result, offset6);
        return result;
    }
}
